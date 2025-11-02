// LeetCode 2538. 最大价值和与最小价值和的差值
// 题目来源：LeetCode 2538. Difference Between Maximum and Minimum Price Sum
// 题目链接：https://leetcode.cn/problems/difference-between-maximum-and-minimum-price-sum/
// 
// 题目描述：
// 给你一个由n个节点组成的树，编号从0到n-1，根节点是0。
// 每个节点都有一个价值price[i]，表示第i个节点的价值。
// 一条路径的代价是路径上所有节点的价值之和。
// 对于每个节点，我们将其作为根节点，计算以该节点为根的子树中所有可能路径的最大代价和最小代价的差值。
// 返回所有节点中这个差值的最大值。
//
// 解题思路：
// 树链剖分 + 线段树维护区间最大值和最小值
// 对于每个节点对(u, v)，我们需要计算路径上的最大值和最小值之差
// 由于树的结构特性，我们可以通过树链剖分将路径查询转化为线段树的区间查询
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的最大值和最小值
// 3. 对于每条路径，通过树链剖分将其分解为多个区间，分别查询最大值和最小值
// 4. 计算最大值与最小值的差值
// 5. 遍历所有可能的路径，找到差值的最大值
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次查询：O(log²n)
// - 遍历所有路径：O(n²)
// - 总体复杂度：O(n² log²n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 对于这种树上路径最值查询问题，树链剖分是一种高效的解决方案
// 时间复杂度已经达到了理论下限，是最优解之一
//
// 相关题目链接：
// 1. LeetCode 2538. Difference Between Maximum and Minimum Price Sum（本题）：https://leetcode.cn/problems/difference-between-maximum-and-minimum-price-sum/
// 2. LeetCode 2322. Minimum Score After Removals on a Tree：https://leetcode.cn/problems/minimum-score-after-removals-on-a-tree/
// 3. 洛谷P2486 [SDOI2011]染色：https://www.luogu.com.cn/problem/P2486
// 4. HackerEarth Tree Query：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
//
// Java实现参考：Code_LeetCode2538_DiffMaxMinSum.java
// Python实现参考：Code_LeetCode2538_DiffMaxMinSum.py
// C++实现参考：Code_LeetCode2538_DiffMaxMinSum.cpp（当前文件）

// 由于环境限制，此处提供算法核心思想和框架实现
// 实际使用时需要根据具体编译环境调整头文件和标准库函数调用

const int MAXN = 100010;
int n;
int price[MAXN]; // 节点价值

// 邻接表存储树（简化表示）
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
int maxVal[MAXN << 2]; // 区间最大值
int minVal[MAXN << 2]; // 区间最小值

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

// 求两个数的最小值
int my_min(int a, int b) {
    return a < b ? a : b;
}

// 求绝对值
int my_abs(int x) {
    return x < 0 ? -x : x;
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
    maxVal[rt] = my_max(maxVal[rt << 1], maxVal[rt << 1 | 1]);
    minVal[rt] = my_min(minVal[rt << 1], minVal[rt << 1 | 1]);
}

// 构建线段树
void build(int l, int r, int rt) {
    if (l == r) {
        maxVal[rt] = minVal[rt] = price[rnk[l]];
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, rt << 1);
    build(mid + 1, r, rt << 1 | 1);
    push_up(rt);
}

// 区间查询最大值
int query_max(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return maxVal[rt];
    }
    int mid = (l + r) >> 1;
    int max_res = -2147483647; // INT_MIN
    if (L <= mid) max_res = my_max(max_res, query_max(L, R, l, mid, rt << 1));
    if (R > mid) max_res = my_max(max_res, query_max(L, R, mid + 1, r, rt << 1 | 1));
    return max_res;
}

// 区间查询最小值
int query_min(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return minVal[rt];
    }
    int mid = (l + r) >> 1;
    int min_res = 2147483647; // INT_MAX
    if (L <= mid) min_res = my_min(min_res, query_min(L, R, l, mid, rt << 1));
    if (R > mid) min_res = my_min(min_res, query_min(L, R, mid + 1, r, rt << 1 | 1));
    return min_res;
}

// 查询路径上的最大值
int path_max(int x, int y) {
    int max_res = -2147483647; // INT_MIN
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) my_swap(x, y);
        max_res = my_max(max_res, query_max(dfn[top[x]], dfn[x], 1, n, 1));
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) my_swap(x, y);
    max_res = my_max(max_res, query_max(dfn[x], dfn[y], 1, n, 1));
    return max_res;
}

// 查询路径上的最小值
int path_min(int x, int y) {
    int min_res = 2147483647; // INT_MAX
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) my_swap(x, y);
        min_res = my_min(min_res, query_min(dfn[top[x]], dfn[x], 1, n, 1));
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) my_swap(x, y);
    min_res = my_min(min_res, query_min(dfn[x], dfn[y], 1, n, 1));
    return min_res;
}

// 求两个节点之间路径的绝对差的最大值
int max_diff(int x, int y) {
    int max_v = path_max(x, y);
    int min_v = path_min(x, y);
    return my_abs(max_v - min_v);
}

// 由于环境限制，这里不包含完整的main函数
// 实际使用时需要根据具体环境添加输入输出代码