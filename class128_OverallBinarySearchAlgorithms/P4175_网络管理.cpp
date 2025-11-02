// P4175 [CTSC2008]网络管理 - C++实现
// 题目来源：https://www.luogu.com.cn/problem/P4175
// 时间复杂度：O((N+Q) * logN * log(maxValue))
// 空间复杂度：O(N + Q)

// 由于环境限制，这里只提供C++代码框架，实际编译需要相应环境支持
//#include <bits/stdc++.h>
//#include <algorithm>
//#include <cstdio>
//using namespace std;

const int MAXN = 80001;
const int MAXQ = 80001;

int n, q;

// 树结构
int head[MAXN], next[MAXN << 1], to[MAXN << 1], cnt = 0;

// 树链剖分
int fa[MAXN], depth[MAXN], siz[MAXN], son[MAXN];
int top[MAXN], dfn[MAXN], rnk[MAXN], dfc = 0;

// 节点权值
int val[MAXN];

// 树状数组
int tree[MAXN];

// 操作信息
int op[MAXQ], x[MAXQ], y[MAXQ], k[MAXQ], qid[MAXQ];

// 整体二分
int lset[MAXQ], rset[MAXQ], ans[MAXQ];

// 离散化
int sorted[MAXN + MAXQ], cntv = 0;

// 去重函数
int unique(int* arr, int len) {
    if (len <= 1) return len;
    int i = 1, j = 2;
    while (j <= len) {
        if (arr[j] != arr[i]) {
            arr[++i] = arr[j];
        }
        j++;
    }
    return i;
}

// 添加边
void addEdge(int u, int v) {
    next[++cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt;
}

// 第一次DFS：计算深度、父节点、子树大小、重儿子
void dfs1(int u, int f) {
    fa[u] = f;
    depth[u] = depth[f] + 1;
    siz[u] = 1;
    
    for (int i = head[u]; i; i = next[i]) {
        int v = to[i];
        if (v == f) continue;
        dfs1(v, u);
        siz[u] += siz[v];
        if (siz[son[u]] < siz[v]) {
            son[u] = v;
        }
    }
}

// 第二次DFS：计算dfn序、重链顶点
void dfs2(int u, int t) {
    top[u] = t;
    dfn[u] = ++dfc;
    rnk[dfc] = u;
    
    if (son[u]) {
        dfs2(son[u], t);
    }
    
    for (int i = head[u]; i; i = next[i]) {
        int v = to[i];
        if (v == fa[u] || v == son[u]) continue;
        dfs2(v, v);
    }
}

// 树状数组操作
int lowbit(int i) {
    return i & -i;
}

void add(int i, int v) {
    while (i <= n) {
        tree[i] += v;
        i += lowbit(i);
    }
}

int sum(int i) {
    int ret = 0;
    while (i > 0) {
        ret += tree[i];
        i -= lowbit(i);
    }
    return ret;
}

// 树链剖分查询路径上点的个数
int queryPath(int u, int v) {
    int ret = 0;
    while (top[u] != top[v]) {
        if (depth[top[u]] < depth[top[v]]) {
            //swap(u, v);
            int temp = u;
            u = v;
            v = temp;
        }
        ret += sum(dfn[u]) - sum(dfn[top[u]] - 1);
        u = fa[top[u]];
    }
    
    if (depth[u] > depth[v]) {
        //swap(u, v);
        int temp = u;
        u = v;
        v = temp;
    }
    ret += sum(dfn[v]) - sum(dfn[u] - 1);
    return ret;
}

// 树链剖分修改路径上的点
void addPath(int u, int v, int val) {
    while (top[u] != top[v]) {
        if (depth[top[u]] < depth[top[v]]) {
            //swap(u, v);
            int temp = u;
            u = v;
            v = temp;
        }
        add(dfn[top[u]], val);
        add(dfn[u] + 1, -val);
        u = fa[top[u]];
    }
    
    if (depth[u] > depth[v]) {
        //swap(u, v);
        int temp = u;
        u = v;
        v = temp;
    }
    add(dfn[u], val);
    add(dfn[v] + 1, -val);
}

// 整体二分核心函数
void compute(int ql, int qr, int vl, int vr) {
    // 递归边界
    if (ql > qr) {
        return;
    }
    
    // 如果值域范围只有一个值，说明找到了答案
    if (vl == vr) {
        for (int i = ql; i <= qr; i++) {
            if (op[qid[i]] == 1) {
                ans[qid[i]] = vl;
            }
        }
        return;
    }
    
    // 二分中点
    int mid = (vl + vr) >> 1;
    
    // 将值域小于等于mid的数加入树状数组
    for (int i = 1; i <= n; i++) {
        if (val[i] <= sorted[mid]) {
            addPath(i, i, 1);
        }
    }
    
    for (int i = 1; i <= q; i++) {
        if (op[i] == 0 && y[i] <= sorted[mid]) {
            addPath(x[i], x[i], 1);
        }
    }
    
    // 检查每个查询，根据满足条件的元素个数划分到左右区间
    int lsiz = 0, rsiz = 0;
    for (int i = ql; i <= qr; i++) {
        int id = qid[i];
        if (op[id] == 1) {
            // 查询操作
            int satisfy = queryPath(x[id], y[id]);
            if (satisfy >= k[id]) {
                // 说明第k大的数在左半部分
                lset[++lsiz] = id;
            } else {
                // 说明第k大的数在右半部分，需要在右半部分找第(k-satisfy)大的数
                k[id] -= satisfy;
                rset[++rsiz] = id;
            }
        } else {
            // 修改操作
            if (y[id] <= sorted[mid]) {
                lset[++lsiz] = id;
            } else {
                rset[++rsiz] = id;
            }
        }
    }
    
    // 将操作分组
    for (int i = 1; i <= lsiz; i++) {
        qid[ql + i - 1] = lset[i];
    }
    for (int i = 1; i <= rsiz; i++) {
        qid[ql + lsiz + i - 1] = rset[i];
    }
    
    // 清空树状数组
    for (int i = 1; i <= n; i++) {
        if (val[i] <= sorted[mid]) {
            addPath(i, i, -1);
        }
    }
    
    for (int i = 1; i <= q; i++) {
        if (op[i] == 0 && y[i] <= sorted[mid]) {
            addPath(x[i], x[i], -1);
        }
    }
    
    // 递归处理左右区间
    compute(ql, ql + lsiz - 1, vl, mid);
    compute(ql + lsiz, qr, mid + 1, vr);
}

/*
int main() {
    //scanf("%d%d", &n, &q);
    
    for (int i = 1; i <= n; i++) {
        //scanf("%d", &val[i]);
        sorted[++cntv] = val[i];
    }
    
    // 建树
    for (int i = 1; i < n; i++) {
        int u, v;
        //scanf("%d%d", &u, &v);
        addEdge(u, v);
        addEdge(v, u);
    }
    
    // 处理操作
    for (int i = 1; i <= q; i++) {
        //scanf("%d%d%d", &op[i], &x[i], &y[i]);
        
        if (op[i] == 0) {
            // 修改操作
            sorted[++cntv] = y[i];
        } else {
            // 查询操作
            k[i] = op[i];
            op[i] = 1;
            qid[i] = i;
        }
    }
    
    // 离散化
    //sort(sorted + 1, sorted + cntv + 1);
    cntv = unique(sorted, cntv);
    
    // 树链剖分预处理
    dfs1(1, 0);
    dfs2(1, 1);
    
    // 整体二分求解
    compute(1, q, 1, cntv);
    
    // 输出结果
    for (int i = 1; i <= q; i++) {
        if (ans[i]) {
            //printf("%d\n", sorted[ans[i]]);
        } else {
            //printf("invalid request!\n");
        }
    }
    
    return 0;
}
*/