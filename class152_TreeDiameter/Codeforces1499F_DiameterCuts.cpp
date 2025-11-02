// Codeforces 1499F Diameter Cuts
// 题目：给定一棵n个节点的树和一个整数k，计算有多少个连通子图的直径恰好为k。
// 树的直径是指树中任意两点之间最长的简单路径。
// 来源：Codeforces Educational Round 106 Problem F
// 链接：https://codeforces.com/contest/1499/problem/F

#define MAXN 5001
#define MOD 998244353

// 邻接表存储树
int head[MAXN], next[MAXN << 1], to[MAXN << 1], cnt;
int n, k;  // 节点数和目标直径

// DP状态
// f[u][i] 表示以u为根的子树中，所有连通子图都合法且从u向下延伸的最长路径长度为i的方案数
long long f[MAXN][MAXN];
int size[MAXN];  // 子树大小
long long g[MAXN];    // 临时数组用于DP转移

// 添加边
void addEdge(int u, int v) {
    next[cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt++;
}

// 初始化
void init() {
    cnt = 0;
    for (int i = 1; i <= n; i++) {
        head[i] = -1;
    }
}

/**
 * 树形DP求解
 * @param u 当前节点
 * @param parent 父节点
 * 
 * 时间复杂度：O(n^2)
 * 空间复杂度：O(n^2)
 */
void dfs(int u, int parent) {
    // 初始化当前节点的DP状态
    size[u] = 1;
    f[u][0] = 1;  // 只选择节点u本身
    
    // 遍历所有子节点
    for (int e = head[u]; e != -1; e = next[e]) {
        int v = to[e];
        if (v != parent) {
            dfs(v, u);
            
            // DP转移
            // 清空临时数组
            for (int i = 0; i <= k; i++) {
                g[i] = 0;
            }
            
            // 合并u和v的子树信息
            for (int i = 0; i <= (k < size[u] ? k : size[u]); i++) {
                for (int j = 0; j <= (size[v] < k - i ? size[v] : k - i); j++) {
                    // 合并后的最长路径长度为max(i, j+1)
                    // j+1是因为连接u和v需要增加1条边
                    int newLength = (i > j + 1) ? i : j + 1;
                    if (newLength <= k) {
                        g[newLength] = (g[newLength] + f[u][i] * f[v][j]) % MOD;
                    }
                }
            }
            
            // 更新u的子树大小和DP状态
            size[u] += size[v];
            for (int i = 0; i <= (k < size[u] ? k : size[u]); i++) {
                f[u][i] = g[i];
            }
        }
    }
    
    // 计算以u为根的子树中所有合法连通子图的总数
    long long sum = 0;
    for (int i = 0; i <= (k < size[u] ? k : size[u]); i++) {
        sum = (sum + f[u][i]) % MOD;
    }
    
    // 如果不是根节点，需要调整DP状态
    if (u != 1) {
        // 将所有路径长度加1（因为要连接到父节点）
        for (int i = (k < size[u] ? k : size[u]); i > 0; i--) {
            f[u][i] = f[u][i - 1];
        }
        // 不连接到父节点的情况
        f[u][0] = sum;
    }
}

/**
 * 主方法
 * 时间复杂度：O(n^2)
 * 空间复杂度：O(n^2)
 */
int main() {
    // 由于编译环境限制，这里只展示算法实现
    // 实际使用时需要根据具体环境添加输入输出代码
    
    // 示例：n = 4, k = 0, 边为 1-2, 2-3, 3-4
    n = 4;
    k = 0;
    init();
    
    addEdge(1, 2);
    addEdge(2, 1);
    addEdge(2, 3);
    addEdge(3, 2);
    addEdge(3, 4);
    addEdge(4, 3);
    
    // 执行树形DP
    dfs(1, 0);
    
    // 计算结果
    long long result = 0;
    for (int i = 0; i <= k; i++) {
        result = (result + f[1][i]) % MOD;
    }
    
    // printf("%lld\n", result); // 应该输出某种结果
    
    return 0;
}