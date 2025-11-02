```cpp
#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;

// 洛谷P2146 软件包管理器 - 树链剖分解法
// 题目描述：
// Linux用户和OSX用户都对软件包管理器不会陌生。
// 通过软件包管理器，我们可以安装、删除和更新软件包。
// 软件包之间存在依赖关系，当要安装一个软件包时，需要先安装它的所有依赖。
// 当要卸载一个软件包时，需要同时卸载所有它依赖的软件包。
// 这些依赖关系形成一棵树的结构，其中根节点为空软件包。
// 有两种操作：
// install x：安装软件包x，需要安装从根到x路径上的所有软件包
// uninstall x：卸载软件包x，需要卸载以x为根的子树中的所有软件包
// 每次操作后输出实际安装或卸载的软件包数量
// 测试链接：https://www.luogu.com.cn/problem/P2146

const int MAXN = 100001;

// 图相关
int head[MAXN], next_edge[MAXN << 1], to_edge[MAXN << 1], cnt_edge = 0;

// 树链剖分相关
int fa[MAXN], dep[MAXN], siz[MAXN], son[MAXN], top[MAXN], dfn[MAXN], rnk[MAXN], cnt_dfn = 0;

// 线段树相关
int sum[MAXN << 2], lazy[MAXN << 2];

// 添加边
void add_edge(int u, int v) {
    next_edge[++cnt_edge] = head[u];
    to_edge[cnt_edge] = v;
    head[u] = cnt_edge;
}

// 第一次dfs，计算树链剖分所需信息
void dfs1(int u, int f) {
    fa[u] = f;
    dep[u] = dep[f] + 1;
    siz[u] = 1;
    son[u] = 0;
    
    for (int e = head[u], v; e; e = next_edge[e]) {
        v = to_edge[e];
        if (v != f) {
            dfs1(v, u);
            siz[u] += siz[v];
            if (son[u] == 0 || siz[son[u]] < siz[v]) {
                son[u] = v;
            }
        }
    }
}

// 第二次dfs，计算重链剖分
void dfs2(int u, int t) {
    top[u] = t;
    dfn[u] = ++cnt_dfn;
    rnk[cnt_dfn] = u;
    
    if (son[u] == 0) return;
    dfs2(son[u], t);
    
    for (int e = head[u], v; e; e = next_edge[e]) {
        v = to_edge[e];
        if (v != fa[u] && v != son[u]) {
            dfs2(v, v);
        }
    }
}

// 线段树操作
void up(int i) {
    sum[i] = sum[i << 1] + sum[i << 1 | 1];
}

// 设置懒标记：-1表示无标记，0表示设置为0，1表示设置为1
void down(int i, int ln, int rn) {
    if (lazy[i] != -1) {
        sum[i << 1] = lazy[i] * ln;
        sum[i << 1 | 1] = lazy[i] * rn;
        lazy[i << 1] = lazy[i];
        lazy[i << 1 | 1] = lazy[i];
        lazy[i] = -1;
    }
}

// 区间更新
void update(int jobl, int jobr, int jobv, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        sum[i] = jobv * (r - l + 1);
        lazy[i] = jobv;
        return;
    }
    int mid = (l + r) >> 1;
    down(i, mid - l + 1, r - mid);
    if (jobl <= mid) update(jobl, jobr, jobv, l, mid, i << 1);
    if (jobr > mid) update(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
    up(i);
}

// 区间查询
int query(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return sum[i];
    }
    int mid = (l + r) >> 1;
    down(i, mid - l + 1, r - mid);
    int ans = 0;
    if (jobl <= mid) ans += query(jobl, jobr, l, mid, i << 1);
    if (jobr > mid) ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
    return ans;
}

// 安装软件包：安装从根节点到x的路径上所有软件包
int install(int x, int n) {
    // 先查询安装前从根到x路径上的安装情况
    int installed_before = 0;
    int temp = x;
    while (temp != 0) {
        installed_before += query(dfn[top[temp]], dfn[temp], 1, n, 1);
        temp = fa[top[temp]];
    }
    
    // 安装从根到x路径上的所有软件包
    temp = x;
    while (temp != 0) {
        update(dfn[top[temp]], dfn[temp], 1, 1, n, 1);
        temp = fa[top[temp]];
    }
    
    // 查询安装后从根到x路径上的安装情况
    int installed_after = 0;
    temp = x;
    while (temp != 0) {
        installed_after += query(dfn[top[temp]], dfn[temp], 1, n, 1);
        temp = fa[top[temp]];
    }
    
    return installed_after - installed_before;
}

// 卸载软件包：卸载以x为根的子树中的所有软件包
int uninstall(int x, int n) {
    // 先查询卸载前x子树中已安装的软件包数量
    int installed_before = query(dfn[x], dfn[x] + siz[x] - 1, 1, n, 1);
    
    // 卸载x子树中的所有软件包（设置为0）
    update(dfn[x], dfn[x] + siz[x] - 1, 0, 1, n, 1);
    
    return installed_before; // 返回卸载的软件包数量
}

int main() {
    int n;
    scanf("%d", &n);
    
    // 初始化懒标记数组
    memset(lazy, -1, sizeof(lazy));
    
    // 构建树结构（0是根节点，表示空软件包）
    for (int i = 1; i <= n - 1; i++) {
        int parent;
        scanf("%d", &parent);
        add_edge(parent, i);
        add_edge(i, parent);
    }
    
    // 树链剖分
    dfs1(0, 0);
    dfs2(0, 0);
    
    int q;
    scanf("%d", &q);
    for (int i = 0; i < q; i++) {
        char operation[20];
        int x;
        scanf("%s%d", operation, &x);
        
        if (operation[0] == 'i') {  // install
            printf("%d\n", install(x, n));
        } else {  // uninstall
            printf("%d\n", uninstall(x, n));
        }
    }
    
    return 0;
}
```