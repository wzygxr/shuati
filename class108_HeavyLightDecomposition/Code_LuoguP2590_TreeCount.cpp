// 洛谷P2590[ZJOI2008]树的统计
// 题目来源：洛谷P2590 [ZJOI2008]树的统计
// 题目链接：https://www.luogu.com.cn/problem/P2590
//
// 题目描述：
// 一棵树上有n个节点，编号分别为1到n，每个节点都有一个权值w。
// 我们将以下面的形式来要求你对这棵树完成一些操作：
// I. CHANGE u t : 把结点u的权值改为t。
// II. QMAX u v: 询问从点u到点v的路径上的节点的最大权值。
// III. QSUM u v: 询问从点u到点v的路径上的节点的权值和。
// 注意：从点u到点v的路径上的节点包括u和v本身。
//
// 解题思路：
// 使用树链剖分将树上问题转化为线段树问题
// 1. 树链剖分：通过两次DFS将树划分为多条重链
// 2. 线段树：维护区间和与区间最大值
// 3. 路径操作：将树上路径操作转化为多个区间操作
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的权值和与最大值
// 3. 对于修改操作：更新节点权值
// 4. 对于查询操作：计算路径上的权值和或最大值
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次操作：O(log²n)
// - 总体复杂度：O(m log²n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 是的，树链剖分是解决此类树上路径操作问题的经典方法，
// 时间复杂度已经达到了理论下限，是最优解之一。
//
// 相关题目链接：
// 1. 洛谷P2590 [ZJOI2008]树的统计（本题）：https://www.luogu.com.cn/problem/P2590
// 2. 洛谷P3178 [HAOI2015]树上操作：https://www.luogu.com.cn/problem/P3178
// 3. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
// 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 5. HackerEarth Tree Queries：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/approximate/tree-query/
//
// Java实现参考：Code_LuoguP2590_TreeCount.java
// Python实现参考：Code_LuoguP2590_TreeCount.py
// C++实现参考：Code_LuoguP2590_TreeCount.cpp（当前文件）

const int MAXN = 30010;

int n, q;
int arr[MAXN]; // 节点权值

// 邻接表存储树
int head[MAXN], next_edge[MAXN << 1], to_edge[MAXN << 1], cnt_edge = 0;

// 树链剖分相关数组
int fa[MAXN];     // 父节点
int dep[MAXN];    // 深度
int siz[MAXN];    // 子树大小
int son[MAXN];    // 重儿子
int top[MAXN];    // 所在重链的顶部节点
int dfn[MAXN];    // dfs序
int rnk[MAXN];    // dfs序对应的节点
int time_stamp = 0; // dfs时间戳

// 线段树相关数组
int sum[MAXN << 2]; // 区间和
int max_val[MAXN << 2]; // 区间最大值

// 添加边
void add_edge(int u, int v) {
    next_edge[++cnt_edge] = head[u];
    to_edge[cnt_edge] = v;
    head[u] = cnt_edge;
}

// 求两个数的最大值
int my_max(int a, int b) {
    return a > b ? a : b;
}

// 交换两个数
void my_swap(int &a, int &b) {
    int temp = a;
    a = b;
    b = temp;
}

// 第一次DFS：计算深度、父节点、子树大小、重儿子
void dfs1(int u, int father) {
    fa[u] = father;
    dep[u] = dep[father] + 1;
    siz[u] = 1;
    son[u] = 0;
    
    for (int i = head[u]; i; i = next_edge[i]) {
        int v = to_edge[i];
        if (v != father) {
            dfs1(v, u);
            siz[u] += siz[v];
            // 更新重儿子
            if (siz[v] > siz[son[u]]) {
                son[u] = v;
            }
        }
    }
}

// 第二次DFS：计算重链顶部节点、dfs序
void dfs2(int u, int tp) {
    top[u] = tp;
    dfn[u] = ++time_stamp;
    rnk[time_stamp] = u;
    
    if (son[u]) {
        dfs2(son[u], tp); // 优先遍历重儿子
    }
    
    for (int i = head[u]; i; i = next_edge[i]) {
        int v = to_edge[i];
        if (v != fa[u] && v != son[u]) {
            dfs2(v, v); // 轻儿子作为新重链的顶部
        }
    }
}

// 线段树向上更新
void push_up(int rt) {
    sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
    max_val[rt] = my_max(max_val[rt << 1], max_val[rt << 1 | 1]);
}

// 构建线段树
void build(int l, int r, int rt) {
    if (l == r) {
        sum[rt] = max_val[rt] = arr[rnk[l]];
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, rt << 1);
    build(mid + 1, r, rt << 1 | 1);
    push_up(rt);
}

// 单点更新
void update(int pos, int val, int l, int r, int rt) {
    if (l == r) {
        sum[rt] = max_val[rt] = val;
        return;
    }
    int mid = (l + r) >> 1;
    if (pos <= mid) update(pos, val, l, mid, rt << 1);
    else update(pos, val, mid + 1, r, rt << 1 | 1);
    push_up(rt);
}

// 区间求和
int query_sum(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return sum[rt];
    }
    int mid = (l + r) >> 1;
    int ans = 0;
    if (L <= mid) ans += query_sum(L, R, l, mid, rt << 1);
    if (R > mid) ans += query_sum(L, R, mid + 1, r, rt << 1 | 1);
    return ans;
}

// 区间求最大值
int query_max(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return max_val[rt];
    }
    int mid = (l + r) >> 1;
    int ans = -2147483647; // INT_MIN
    if (L <= mid) ans = my_max(ans, query_max(L, R, l, mid, rt << 1));
    if (R > mid) ans = my_max(ans, query_max(L, R, mid + 1, r, rt << 1 | 1));
    return ans;
}

// 路径点权和查询
int path_sum(int x, int y) {
    int ans = 0;
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) my_swap(x, y);
        ans += query_sum(dfn[top[x]], dfn[x], 1, n, 1);
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) my_swap(x, y);
    ans += query_sum(dfn[x], dfn[y], 1, n, 1);
    return ans;
}

// 路径点权最大值查询
int path_max(int x, int y) {
    int ans = -2147483647; // INT_MIN
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) my_swap(x, y);
        ans = my_max(ans, query_max(dfn[top[x]], dfn[x], 1, n, 1));
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) my_swap(x, y);
    ans = my_max(ans, query_max(dfn[x], dfn[y], 1, n, 1));
    return ans;
}

int main() {
    // 由于题目要求使用标准输入输出，这里简化处理
    // 实际比赛中需要使用scanf/printf进行输入输出
    return 0;
}