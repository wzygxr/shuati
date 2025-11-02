#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
#include <functional>

using namespace std;

// Journey - CF1336F
// 测试链接 : https://codeforces.com/problemset/problem/1336/F

const int MAXN = 150001;
const int MAXT = MAXN * 50; // 线段树节点数

int n, k;

// 邻接表存储树
int head[MAXN], nxt[MAXN << 1], to[MAXN << 1], cntg;

// 树上信息
int dep[MAXN], dfn[MAXN], rev[MAXN], siz[MAXN], anc[MAXN][21], id;

// 线段树相关
int ls[MAXT], rs[MAXT], sum[MAXT], cntt;

// 回收站
int st[MAXT], top;

// 链信息
vector<pair<int, int>> chains[MAXN];

// 答案
long long ans;

// 树状数组
int bit[MAXN];

// 添加边
void addEdge(int u, int v) {
    nxt[++cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
}

// 初始化线段树节点
int newNode() {
    if (top > 0) {
        return st[top--];
    }
    return ++cntt;
}

// 更新节点信息
void pushUp(int rt) {
    sum[rt] = sum[ls[rt]] + sum[rs[rt]];
}

// 更新线段树
void update(int &rt, int l, int r, int pos, int val) {
    if (rt == 0) {
        rt = newNode();
    }
    sum[rt] += val;
    if (l == r) {
        return;
    }
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        if (ls[rt] == 0) {
            ls[rt] = newNode();
        }
        update(ls[rt], l, mid, pos, val);
    } else {
        if (rs[rt] == 0) {
            rs[rt] = newNode();
        }
        update(rs[rt], mid + 1, r, pos, val);
    }
    pushUp(rt);
}

// 查询线段树
int query(int rt, int l, int r, int L, int R) {
    if (rt == 0 || L > r || R < l) {
        return 0;
    }
    if (L <= l && r <= R) {
        return sum[rt];
    }
    int mid = (l + r) >> 1;
    int res = 0;
    if (L <= mid) {
        res += query(ls[rt], l, mid, L, R);
    }
    if (R > mid) {
        res += query(rs[rt], mid + 1, r, L, R);
    }
    return res;
}

// 合并线段树
int merge(int a, int &b) {
    if (a == 0 || b == 0) {
        return a + b;
    }
    sum[a] += sum[b];
    ls[a] = merge(ls[a], ls[b]);
    rs[a] = merge(rs[a], rs[b]);
    st[++top] = b;
    b = 0;
    return a;
}

// 删除线段树节点
void del(int &rt) {
    if (rt == 0) {
        return;
    }
    del(ls[rt]);
    del(rs[rt]);
    st[++top] = rt;
    ls[rt] = rs[rt] = sum[rt] = 0;
    rt = 0;
}

// 树状数组操作
int lowbit(int x) {
    return x & (-x);
}

void addBit(int x, int val) {
    x++;
    for (int i = x; i < MAXN; i += lowbit(i)) {
        bit[i] += val;
    }
}

int sumBit(int x) {
    x++;
    int res = 0;
    for (int i = x; i > 0; i -= lowbit(i)) {
        res += bit[i];
    }
    return res;
}

// DFS预处理
void dfs1(int u, int fa) {
    dep[u] = dep[fa] + 1;
    anc[u][0] = fa;
    siz[u] = 1;
    dfn[u] = ++id;
    rev[id] = u;
    
    // 预处理祖先
    for (int i = 1; i <= 20; i++) {
        anc[u][i] = anc[anc[u][i - 1]][i - 1];
    }
    
    // 遍历子节点
    for (int i = head[u]; i; i = nxt[i]) {
        int v = to[i];
        if (v != fa) {
            dfs1(v, u);
            siz[u] += siz[v];
        }
    }
}

// 跳到指定深度
int jump(int x, int d) {
    for (int i = 20; i >= 0; i--) {
        if ((d >> i) & 1) {
            x = anc[x][i];
        }
    }
    return x;
}

// 求LCA
int lca(int x, int y) {
    if (dep[x] < dep[y]) {
        swap(x, y);
    }
    for (int i = 20; i >= 0; i--) {
        if (dep[anc[x][i]] >= dep[y]) {
            x = anc[x][i];
        }
    }
    if (x == y) {
        return x;
    }
    for (int i = 20; i >= 0; i--) {
        if (anc[x][i] != anc[y][i]) {
            x = anc[x][i];
            y = anc[y][i];
        }
    }
    return anc[x][0];
}

// 处理不同LCA的情况
void dfs2(int u, int fa) {
    // 先递归处理子节点
    for (int i = head[u]; i; i = nxt[i]) {
        int v = to[i];
        if (v != fa) {
            dfs2(v, u);
        }
    }
    
    // 统计贡献
    for (auto &chain : chains[u]) {
        ans += sumBit(dfn[chain.first]) + sumBit(dfn[chain.second]);
    }
    
    // 更新树状数组
    for (auto &chain : chains[u]) {
        if (dep[chain.first] - dep[u] >= k) {
            int node = jump(chain.first, dep[chain.first] - dep[u] - k);
            addBit(dfn[node], 1);
            addBit(dfn[node] + siz[node], -1);
        }
        if (dep[chain.second] - dep[u] >= k) {
            int node = jump(chain.second, dep[chain.second] - dep[u] - k);
            addBit(dfn[node], 1);
            addBit(dfn[node] + siz[node], -1);
        }
    }
}

// 清空树状数组
void clearBit() {
    memset(bit, 0, sizeof(bit));
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int m;
    cin >> n >> m >> k;
    
    // 建树
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        addEdge(u, v);
        addEdge(v, u);
    }
    
    // 预处理
    dfs1(1, 0);
    
    // 读入链信息
    for (int i = 1; i <= m; i++) {
        int x, y;
        cin >> x >> y;
        if (dfn[x] > dfn[y]) {
            swap(x, y);
        }
        int l = lca(x, y);
        chains[l].push_back({x, y});
    }
    
    // 处理不同LCA的情况
    dfs2(1, 0);
    
    cout << ans << endl;
    
    return 0;
}#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
#include <functional>

using namespace std;

// Journey - CF1336F
// 测试链接 : https://codeforces.com/problemset/problem/1336/F

const int MAXN = 150001;
const int MAXT = MAXN * 50; // 线段树节点数

int n, k;

// 邻接表存储树
int head[MAXN], nxt[MAXN << 1], to[MAXN << 1], cntg;

// 树上信息
int dep[MAXN], dfn[MAXN], rev[MAXN], siz[MAXN], anc[MAXN][21], id;

// 线段树相关
int ls[MAXT], rs[MAXT], sum[MAXT], cntt;

// 回收站
int st[MAXT], top;

// 链信息
vector<pair<int, int>> chains[MAXN];

// 答案
long long ans;

// 树状数组
int bit[MAXN];

// 添加边
void addEdge(int u, int v) {
    nxt[++cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
}

// 初始化线段树节点
int newNode() {
    if (top > 0) {
        return st[top--];
    }
    return ++cntt;
}

// 更新节点信息
void pushUp(int rt) {
    sum[rt] = sum[ls[rt]] + sum[rs[rt]];
}

// 更新线段树
void update(int &rt, int l, int r, int pos, int val) {
    if (rt == 0) {
        rt = newNode();
    }
    sum[rt] += val;
    if (l == r) {
        return;
    }
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        if (ls[rt] == 0) {
            ls[rt] = newNode();
        }
        update(ls[rt], l, mid, pos, val);
    } else {
        if (rs[rt] == 0) {
            rs[rt] = newNode();
        }
        update(rs[rt], mid + 1, r, pos, val);
    }
    pushUp(rt);
}

// 查询线段树
int query(int rt, int l, int r, int L, int R) {
    if (rt == 0 || L > r || R < l) {
        return 0;
    }
    if (L <= l && r <= R) {
        return sum[rt];
    }
    int mid = (l + r) >> 1;
    int res = 0;
    if (L <= mid) {
        res += query(ls[rt], l, mid, L, R);
    }
    if (R > mid) {
        res += query(rs[rt], mid + 1, r, L, R);
    }
    return res;
}

// 合并线段树
int merge(int a, int &b) {
    if (a == 0 || b == 0) {
        return a + b;
    }
    sum[a] += sum[b];
    ls[a] = merge(ls[a], ls[b]);
    rs[a] = merge(rs[a], rs[b]);
    st[++top] = b;
    b = 0;
    return a;
}

// 删除线段树节点
void del(int &rt) {
    if (rt == 0) {
        return;
    }
    del(ls[rt]);
    del(rs[rt]);
    st[++top] = rt;
    ls[rt] = rs[rt] = sum[rt] = 0;
    rt = 0;
}

// 树状数组操作
int lowbit(int x) {
    return x & (-x);
}

void addBit(int x, int val) {
    x++;
    for (int i = x; i < MAXN; i += lowbit(i)) {
        bit[i] += val;
    }
}

int sumBit(int x) {
    x++;
    int res = 0;
    for (int i = x; i > 0; i -= lowbit(i)) {
        res += bit[i];
    }
    return res;
}

// DFS预处理
void dfs1(int u, int fa) {
    dep[u] = dep[fa] + 1;
    anc[u][0] = fa;
    siz[u] = 1;
    dfn[u] = ++id;
    rev[id] = u;
    
    // 预处理祖先
    for (int i = 1; i <= 20; i++) {
        anc[u][i] = anc[anc[u][i - 1]][i - 1];
    }
    
    // 遍历子节点
    for (int i = head[u]; i; i = nxt[i]) {
        int v = to[i];
        if (v != fa) {
            dfs1(v, u);
            siz[u] += siz[v];
        }
    }
}

// 跳到指定深度
int jump(int x, int d) {
    for (int i = 20; i >= 0; i--) {
        if ((d >> i) & 1) {
            x = anc[x][i];
        }
    }
    return x;
}

// 求LCA
int lca(int x, int y) {
    if (dep[x] < dep[y]) {
        swap(x, y);
    }
    for (int i = 20; i >= 0; i--) {
        if (dep[anc[x][i]] >= dep[y]) {
            x = anc[x][i];
        }
    }
    if (x == y) {
        return x;
    }
    for (int i = 20; i >= 0; i--) {
        if (anc[x][i] != anc[y][i]) {
            x = anc[x][i];
            y = anc[y][i];
        }
    }
    return anc[x][0];
}

// 处理不同LCA的情况
void dfs2(int u, int fa) {
    // 先递归处理子节点
    for (int i = head[u]; i; i = nxt[i]) {
        int v = to[i];
        if (v != fa) {
            dfs2(v, u);
        }
    }
    
    // 统计贡献
    for (auto &chain : chains[u]) {
        ans += sumBit(dfn[chain.first]) + sumBit(dfn[chain.second]);
    }
    
    // 更新树状数组
    for (auto &chain : chains[u]) {
        if (dep[chain.first] - dep[u] >= k) {
            int node = jump(chain.first, dep[chain.first] - dep[u] - k);
            addBit(dfn[node], 1);
            addBit(dfn[node] + siz[node], -1);
        }
        if (dep[chain.second] - dep[u] >= k) {
            int node = jump(chain.second, dep[chain.second] - dep[u] - k);
            addBit(dfn[node], 1);
            addBit(dfn[node] + siz[node], -1);
        }
    }
}

// 清空树状数组
void clearBit() {
    memset(bit, 0, sizeof(bit));
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int m;
    cin >> n >> m >> k;
    
    // 建树
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        addEdge(u, v);
        addEdge(v, u);
    }
    
    // 预处理
    dfs1(1, 0);
    
    // 读入链信息
    for (int i = 1; i <= m; i++) {
        int x, y;
        cin >> x >> y;
        if (dfn[x] > dfn[y]) {
            swap(x, y);
        }
        int l = lca(x, y);
        chains[l].push_back({x, y});
    }
    
    // 处理不同LCA的情况
    dfs2(1, 0);
    
    cout << ans << endl;
    
    return 0;
}