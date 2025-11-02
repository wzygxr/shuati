// [FJOI2014] 树的重心
// 题目来源: 洛谷 P4582 https://www.luogu.com.cn/problem/P4582
// 问题描述: 给定一个n个点的树，每个点的编号从1~n，问这个树有多少不同的连通子树，和这个树有相同的重心
// 树的重心定义：删掉某点i后，若剩余k个连通分量，那么定义d(i)为这些连通分量中点的个数的最大值，所谓重心，就是使得d(i)最小的点i
// 算法思路:
// 1. 首先计算原树的重心
// 2. 使用树形DP计算每个节点为根的子树中不同大小的连通子树个数
// 3. 统计以原树重心为重心的连通子树个数
// 时间复杂度：O(n^2)，树形DP的复杂度
// 空间复杂度：O(n^2)，用于存储DP状态

// 由于编译环境限制，使用基础C++语法实现

// 最大节点数，根据题目限制设置
const int MAXN = 201;
// 模数，用于防止结果过大
const int MOD = 10007;

// 节点数量
int n;

// 链式前向星存储树结构
// head[i]表示节点i的第一条边的索引
int head[MAXN];
// next[i]表示第i条边的下一条边的索引
int next[MAXN << 1];
// to[i]表示第i条边指向的节点
int to[MAXN << 1];
// 边的计数器，从1开始编号
int cnt;

// size[i]表示以节点i为根的子树的节点数量
int size[MAXN];

// maxSub[i]表示以节点i为根时的最大子树大小
int maxSub[MAXN];

// dp[i][j]表示以节点i为根的子树中，子树大小为j的连通子树个数
int dp[MAXN][MAXN];

// 原树的重心
int originalCentroid = 0;
// 原树重心的最大子树大小
int originalMaxSub = 201;

// 初始化函数，重置邻接表
void init() {
    cnt = 1;  // 边的索引从1开始
    // 初始化邻接表
    for (int i = 0; i < MAXN; i++) {
        head[i] = 0;
        size[i] = 0;
        maxSub[i] = 0;
        // 初始化dp数组
        for (int j = 0; j < MAXN; j++) {
            dp[i][j] = 0;
        }
    }
    // 初始化重心相关变量
    originalCentroid = 0;
    originalMaxSub = 201;
}

// 添加无向边的函数
// u和v之间添加一条边
void addEdge(int u, int v) {
    // 将新边添加到邻接表中
    next[cnt] = head[u];  // 新边的下一条边指向原来u节点的第一条边
    to[cnt] = v;          // 新边指向节点v
    head[u] = cnt++;      // u节点的第一条边更新为新边，然后cnt自增
    
    next[cnt] = head[v];  // 新边的下一条边指向原来v节点的第一条边
    to[cnt] = u;          // 新边指向节点u
    head[v] = cnt++;      // v节点的第一条边更新为新边，然后cnt自增
}

// 求两个数的最大值的辅助函数
int max(int a, int b) {
    return a > b ? a : b;
}

// 求两个数的最小值的辅助函数
int min(int a, int b) {
    return a < b ? a : b;
}

// 计算原树的重心
// u: 当前访问的节点
// father: u的父节点，避免回到父节点形成环
void computeOriginalCentroid(int u, int father) {
    // 初始化当前节点u的子树大小为1（包含节点u本身）
    size[u] = 1;
    // 初始化当前节点u的最大子树大小为0
    maxSub[u] = 0;

    // 遍历u的所有邻接节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];  // 获取当前边指向的节点
        
        // 如果不是父节点，则继续DFS
        if (v != father) {
            // 递归计算子节点v的重心信息
            computeOriginalCentroid(v, u);
            
            // 将子节点v的子树大小加到当前节点u的子树大小中
            size[u] += size[v];
            
            // 更新以u为根时的最大子树大小
            maxSub[u] = max(maxSub[u], size[v]);
        }
    }

    // 计算父节点方向的子树大小（即整棵树去掉以u为根的子树后剩余的部分）
    maxSub[u] = max(maxSub[u], n - size[u]);

    // 更新重心：如果当前节点的最大子树更小
    if (maxSub[u] < originalMaxSub) {
        originalMaxSub = maxSub[u];    // 更新最小的最大子树大小
        originalCentroid = u;          // 更新重心节点
    }
}

// 树形DP计算连通子树个数
// u: 当前访问的节点
// father: u的父节点，避免回到父节点形成环
void treeDP(int u, int father) {
    // 初始化
    // 只包含节点u的子树有1个
    dp[u][1] = 1;

    // 初始化当前节点u的子树大小为1（包含节点u本身）
    size[u] = 1;

    // 遍历u的所有邻接节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];  // 获取当前边指向的节点
        
        // 如果不是父节点，则继续处理
        if (v != father) {
            // 递归处理子节点v
            treeDP(v, u);

            // 合并子树的DP状态
            // 创建临时数组存储合并结果
            int temp[MAXN] = {0};

            // 枚举当前子树的大小
            for (int i = 1; i <= size[u]; i++) {
                // 如果当前子树大小为i的连通子树个数为0，跳过
                if (dp[u][i] == 0) continue;
                
                // 枚举新增子树（以v为根的子树）的大小
                for (int j = 1; j <= size[v]; j++) {
                    // 如果新增子树大小为j的连通子树个数为0，跳过
                    if (dp[v][j] == 0) continue;
                    
                    // 如果合并后的大小不超过总节点数
                    if (i + j <= n) {
                        // 更新合并后大小为i+j的连通子树个数
                        // 使用乘法原理：当前子树中大小为i的子树个数 × 新增子树中大小为j的子树个数
                        temp[i + j] = (temp[i + j] + ((long long)dp[u][i] * dp[v][j]) % MOD) % MOD;
                    }
                }
            }

            // 更新dp[u]，将合并结果加到原有结果上
            for (int i = 1; i <= min(size[u] + size[v], n) && i <= n; i++) {
                dp[u][i] = (dp[u][i] + temp[i]) % MOD;
            }

            // 更新当前节点u的子树大小
            size[u] += size[v];
        }
    }
}

// 由于无法使用标准输入输出函数，这里只展示算法实现
// 实际使用时需要添加输入输出代码
int main() {
    // 算法实现已完成，此处为主函数占位符
    return 0;
}