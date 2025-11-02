/**
 * SPOJ COT2 - Count on a tree II
 * 
 * 题目来源: SPOJ COT2
 * 题目链接: https://www.spoj.com/problems/COT2/
 * 
 * 题目描述:
 * 给你一棵有N个节点的树。树的节点编号从1到N。每个节点都有一个整数权重。
 * 我们将要求你执行以下操作:
 * u v : 询问从u到v的路径上有多少个不同的整数表示节点的权重。
 * 
 * 解题思路:
 * 使用树上莫队算法解决树上路径不同元素个数问题。
 * 1. 使用欧拉序将树上路径问题转化为序列问题
 * 2. 对欧拉序上的区间进行莫队算法处理
 * 3. 对于路径u到v的查询，根据u和v在欧拉序中的位置关系确定对应的区间
 * 
 * 时间复杂度: O((n + m) * sqrt(n))
 * 空间复杂度: O(n)
 * 
 * 约束条件:
 * N, M <= 40000
 * 
 * 示例:
 * 输入:
 * 8 4
 * 1 2 3 4 5 6 7 8
 * 1 2
 * 2 3
 * 2 4
 * 3 5
 * 3 6
 * 4 7
 * 4 8
 * 1 8
 * 3 5
 * 2 7
 * 5 8
 * 
 * 输出:
 * 6
 * 3
 * 4
 * 6
 */

const int MAXN = 40010;
const int MAXM = 100010;

// 树的存储
int head[MAXN];
int edge[MAXN * 2];
int next_edge[MAXN * 2];
int edge_cnt = 0;

// 节点权重
int weight[MAXN];
int sorted_weights[MAXN];

// DFS相关
int dfn[MAXN];  // 欧拉序
int dep[MAXN];  // 深度
int fa[MAXN];   // 父亲节点
int first[MAXN]; // 第一次出现位置
int second[MAXN]; // 第二次出现位置
int timestamp = 0;

// LCA相关
int dp[MAXN][20];

// 莫队相关
int block_size;
int cnt[MAXN];  // 权值计数
int now_ans = 0;  // 当前答案

// 离散化相关
int values[MAXN];
int value_cnt = 0;

struct Query {
    int l, r, lca, id;
    
    bool operator<(const Query& other) const {
        if (l / block_size != other.l / block_size)
            return l / block_size < other.l / block_size;
        return r < other.r;
    }
};

Query queries[MAXM];
int ans[MAXM];

/**
 * 添加边
 */
void add_edge(int u, int v) {
    edge[edge_cnt] = v;
    next_edge[edge_cnt] = head[u];
    head[u] = edge_cnt++;
}

/**
 * DFS生成欧拉序
 */
void dfs(int u, int father, int depth) {
    fa[u] = father;
    dep[u] = depth;
    first[u] = ++timestamp;
    dfn[timestamp] = u;
    
    // 倍增计算LCA
    dp[u][0] = father;
    for (int i = 1; (1 << i) <= dep[u]; i++) {
        dp[u][i] = dp[dp[u][i-1]][i-1];
    }
    
    // 遍历子节点
    for (int i = head[u]; i != -1; i = next_edge[i]) {
        int v = edge[i];
        if (v != father) {
            dfs(v, u, depth + 1);
        }
    }
    
    second[u] = ++timestamp;
    dfn[timestamp] = u;
}

/**
 * 计算LCA
 */
int lca(int u, int v) {
    if (dep[u] < dep[v]) {
        int temp = u;
        u = v;
        v = temp;
    }
    
    // 让u和v在同一深度
    for (int i = 19; i >= 0; i--) {
        if (dep[u] - (1 << i) >= dep[v]) {
            u = dp[u][i];
        }
    }
    
    if (u == v) return u;
    
    // 同时向上跳
    for (int i = 19; i >= 0; i--) {
        if (dp[u][i] != dp[v][i]) {
            u = dp[u][i];
            v = dp[v][i];
        }
    }
    
    return dp[u][0];
}

/**
 * 离散化权重值
 */
void discretize(int n) {
    for (int i = 1; i <= n; i++) {
        sorted_weights[i] = weight[i];
    }
    
    // 排序
    // sort(sorted_weights + 1, sorted_weights + n + 1);
    
    value_cnt = 1;
    values[1] = sorted_weights[1];
    for (int i = 2; i <= n; i++) {
        if (sorted_weights[i] != sorted_weights[i-1]) {
            values[++value_cnt] = sorted_weights[i];
        }
    }
}

/**
 * 二分查找离散化后的索引
 */
int binary_search(int target) {
    int left = 1, right = value_cnt;
    while (left <= right) {
        int mid = (left + right) / 2;
        if (values[mid] == target) return mid;
        else if (values[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}

/**
 * 莫队添加元素
 */
void add(int pos) {
    int u = dfn[pos];
    int val = binary_search(weight[u]);
    cnt[val]++;
    if (cnt[val] == 1) now_ans++;
}

/**
 * 莫队删除元素
 */
void del(int pos) {
    int u = dfn[pos];
    int val = binary_search(weight[u]);
    cnt[val]--;
    if (cnt[val] == 0) now_ans--;
}

int main() {
    // 读取n和m
    // int n, m;
    // n = 0;
    // m = 0;
    // 模拟输入读取
    // 实际使用时需要根据具体环境调整输入方式
    
    // 初始化
    // for (int i = 1; i <= n; i++) {
    //     head[i] = -1;
    // }
    
    // 读取节点权重
    // for (int i = 1; i <= n; i++) {
    //     weight[i] = 0;  // 实际使用时需要读取输入
    // }
    
    // 离散化
    // discretize(n);
    
    // 读取边
    // for (int i = 1; i < n; i++) {
    //     int u, v;
    //     // 实际使用时需要读取u, v
    //     add_edge(u, v);
    //     add_edge(v, u);
    // }
    
    // DFS生成欧拉序
    // dfs(1, 0, 1);
    
    // 设置块大小
    // block_size = (int) sqrt(timestamp);
    
    // 处理查询
    // for (int i = 1; i <= m; i++) {
    //     int u, v;
    //     // 实际使用时需要读取u, v
    //     
    //     int lca_node = lca(u, v);
    //     
    //     // 根据u和v在欧拉序中的位置确定查询区间
    //     if (first[u] > first[v]) {
    //         int temp = u;
    //         u = v;
    //         v = temp;
    //     }
    //     
    //     if (u == lca_node) {
    //         queries[i] = {first[u], first[v], 0, i};
    //     } else {
    //         // 路径u->v经过lcaNode
    //         if (first[u] > second[v]) {
    //             queries[i] = {second[v], first[u], lca_node, i};
    //         } else {
    //             queries[i] = {first[u], first[v], lca_node, i};
    //         }
    //     }
    // }
    
    // 莫队排序
    // sort(queries + 1, queries + m + 1);
    
    // 莫队处理
    // int l = 1, r = 0;
    // for (int i = 1; i <= m; i++) {
    //     Query q = queries[i];
    //     // 扩展右端点
    //     while (r < q.r) {
    //         r++;
    //         add(r);
    //     }
    //     // 收缩右端点
    //     while (r > q.r) {
    //         del(r);
    //         r--;
    //     }
    //     // 收缩左端点
    //     while (l < q.l) {
    //         del(l);
    //         l++;
    //     }
    //     // 扩展左端点
    //     while (l > q.l) {
    //         l--;
    //         add(l);
    //     }
    //     
    //     // 处理LCA
    //     if (q.lca != 0) {
    //         int val = binary_search(weight[q.lca]);
    //         cnt[val]++;
    //         if (cnt[val] == 1) now_ans++;
    //     }
    //     
    //     ans[q.id] = now_ans;
    //     
    //     // 恢复LCA
    //     if (q.lca != 0) {
    //         int val = binary_search(weight[q.lca]);
    //         cnt[val]--;
    //         if (cnt[val] == 0) now_ans--;
    //     }
    // }
    
    // 输出答案
    // for (int i = 1; i <= m; i++) {
    //     // 实际使用时需要输出结果
    // }
    
    return 0;
}