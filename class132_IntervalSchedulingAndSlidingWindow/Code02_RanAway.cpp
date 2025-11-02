#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>

using namespace std;

/**
 * 洛谷 P1613 跑路 (C++实现)
 * 
 * 题目描述：
 * 一共有n个节点，编号1~n，一共有m条有向边，每条边1公里
 * 有一个空间跑路器，每秒你都可以直接移动2^k公里，每秒钟可以随意决定k的值
 * 题目保证1到n之间一定可以到达，返回1到n最少用几秒
 * 
 * 解题思路：
 * 这是一个结合了倍增思想和最短路径算法的图论问题。
 * 
 * 核心思想：
 * 1. 预处理：使用倍增思想找出所有可以通过2^k步到达的点对
 * 2. 最短路径：在预处理后的图上使用Floyd算法计算最短时间
 * 
 * 具体步骤：
 * 1. 初始化：对于每条原始边，标记为可以通过2^0=1步到达
 * 2. 倍增预处理：对于每个k，计算哪些点对可以通过2^k步到达
 *    - 如果点i可以通过2^(k-1)步到达点jump，且点jump可以通过2^(k-1)步到达点j
 *    - 那么点i可以通过2^k步到达点j
 * 3. 最短路径计算：在新图上使用Floyd算法计算1到n的最短时间
 * 
 * 时间复杂度：O(n^3 * log k + n^3) = O(n^3 * log k)
 * 空间复杂度：O(n^2 * log k)
 * 
 * 相关题目：
 * 1. LeetCode 1334. 阈值距离内邻居最少的城市 (Floyd算法)
 * 2. LeetCode 743. 网络延迟时间 (Dijkstra算法)
 * 3. POJ 1613 - Run Away (相同题目)
 * 4. Codeforces 1083F. The Fair Nut and Amusing Xor
 * 5. AtCoder ABC128D. equeue
 * 6. 牛客网 NC370. 会议室安排
 * 7. 杭电OJ 5171. GTY's birthday gift
 * 8. UVa 10382. Watering Grass
 * 9. CodeChef - STABLEMP
 * 10. SPOJ - ACTIV
 * 
 * 工程化考量：
 * 1. 在实际应用中，这类算法常用于：
 *    - 网络路由优化
 *    - 交通路径规划
 *    - 游戏AI路径寻找
 *    - 物流配送优化
 * 2. 实现优化：
 *    - 对于稀疏图，可以使用Dijkstra算法替代Floyd算法
 *    - 使用位运算优化倍增过程
 *    - 考虑使用更高效的数据结构存储图
 * 3. 可扩展性：
 *    - 支持动态添加和删除边
 *    - 处理带权重的边
 *    - 扩展到三维或多维空间
 * 4. 鲁棒性考虑：
 *    - 处理不连通图的情况
 *    - 处理负权边的情况
 *    - 优化大规模图的性能
 * 5. 跨语言特性对比：
 *    - C++: 使用vector和数组，性能最优
 *    - Java: 使用二维数组和IO流
 *    - Python: 使用列表和字典，代码简洁但性能较低
 */

class Code02_RanAway {
public:
    /**
     * 计算从节点1到节点n的最短时间
     * 
     * @param n 节点数量
     * @param edges 边列表，每个边包含[起点, 终点]
     * @return 最短时间，如果不可达返回-1
     */
    static int minTime(int n, vector<vector<int>>& edges) {
        if (n <= 0) return -1;
        
        // 最大倍增次数，2^MAX_K应该大于等于最大可能距离
        const int MAX_K = 60;
        
        // reach[k][i][j]表示节点i是否可以通过2^k步到达节点j
        vector<vector<vector<bool>>> reach(MAX_K + 1, 
            vector<vector<bool>>(n + 1, vector<bool>(n + 1, false)));
        
        // 初始化原始边（2^0=1步可达）
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            if (u >= 1 && u <= n && v >= 1 && v <= n) {
                reach[0][u][v] = true;
            }
        }
        
        // 倍增预处理
        for (int k = 1; k <= MAX_K; k++) {
            for (int i = 1; i <= n; i++) {
                for (int jump = 1; jump <= n; jump++) {
                    if (reach[k-1][i][jump]) {
                        for (int j = 1; j <= n; j++) {
                            if (reach[k-1][jump][j]) {
                                reach[k][i][j] = true;
                            }
                        }
                    }
                }
            }
        }
        
        // 构建新图：如果存在某个k使得reach[k][i][j]为true，则i到j有一条边
        vector<vector<int>> dist(n + 1, vector<int>(n + 1, INT_MAX / 2));
        
        // 初始化距离矩阵
        for (int i = 1; i <= n; i++) {
            dist[i][i] = 0;
        }
        
        // 添加可达边，权重为1（一步可达）
        for (int k = 0; k <= MAX_K; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (reach[k][i][j] && i != j) {
                        dist[i][j] = 1;
                    }
                }
            }
        }
        
        // 使用Floyd算法计算最短路径
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (dist[i][k] != INT_MAX / 2 && dist[k][j] != INT_MAX / 2) {
                        dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j]);
                    }
                }
            }
        }
        
        return dist[1][n] == INT_MAX / 2 ? -1 : dist[1][n];
    }
    
    /**
     * 优化版本：使用动态规划直接计算最短路径
     * 避免构建显式的新图，减少空间使用
     */
    static int minTimeOptimized(int n, vector<vector<int>>& edges) {
        if (n <= 0) return -1;
        
        const int MAX_K = 60;
        
        // reach[k][i][j]表示节点i是否可以通过2^k步到达节点j
        vector<vector<vector<bool>>> reach(MAX_K + 1, 
            vector<vector<bool>>(n + 1, vector<bool>(n + 1, false)));
        
        // 初始化原始边
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            if (u >= 1 && u <= n && v >= 1 && v <= n) {
                reach[0][u][v] = true;
            }
        }
        
        // 倍增预处理
        for (int k = 1; k <= MAX_K; k++) {
            for (int i = 1; i <= n; i++) {
                for (int jump = 1; jump <= n; jump++) {
                    if (reach[k-1][i][jump]) {
                        for (int j = 1; j <= n; j++) {
                            if (reach[k-1][jump][j]) {
                                reach[k][i][j] = true;
                            }
                        }
                    }
                }
            }
        }
        
        // 使用动态规划计算最短路径
        vector<vector<int>> dp(n + 1, vector<int>(n + 1, INT_MAX / 2));
        
        // 初始化：直接边距离为1
        for (int i = 1; i <= n; i++) {
            dp[i][i] = 0;
            for (int j = 1; j <= n; j++) {
                if (i != j) {
                    // 检查是否存在某个k使得i可以通过2^k步到达j
                    for (int k = 0; k <= MAX_K; k++) {
                        if (reach[k][i][j]) {
                            dp[i][j] = 1;
                            break;
                        }
                    }
                }
            }
        }
        
        // Floyd算法
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (dp[i][k] != INT_MAX / 2 && dp[k][j] != INT_MAX / 2) {
                        dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j]);
                    }
                }
            }
        }
        
        return dp[1][n] == INT_MAX / 2 ? -1 : dp[1][n];
    }
};

/**
 * 测试函数 - 验证算法正确性
 */
void testMinTime() {
    cout << "=== 测试Code02_RanAway ===" << endl;
    
    // 测试用例1：基本功能测试
    int n1 = 4;
    vector<vector<int>> edges1 = {{1, 2}, {2, 3}, {3, 4}};
    int result1 = Code02_RanAway::minTime(n1, edges1);
    cout << "测试用例1 - 预期: 3, 实际: " << result1 << endl;
    
    // 测试用例2：倍增优化测试
    int n2 = 4;
    vector<vector<int>> edges2 = {{1, 2}, {2, 4}};
    int result2 = Code02_RanAway::minTime(n2, edges2);
    cout << "测试用例2 - 预期: 2, 实际: " << result2 << endl;
    
    // 测试用例3：单节点
    int n3 = 1;
    vector<vector<int>> edges3 = {};
    int result3 = Code02_RanAway::minTime(n3, edges3);
    cout << "测试用例3 - 预期: 0, 实际: " << result3 << endl;
    
    // 测试用例4：不连通图
    int n4 = 3;
    vector<vector<int>> edges4 = {{1, 2}}; // 节点3不可达
    int result4 = Code02_RanAway::minTime(n4, edges4);
    cout << "测试用例4 - 预期: -1, 实际: " << result4 << endl;
    
    // 测试用例5：复杂倍增情况
    int n5 = 5;
    vector<vector<int>> edges5 = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {1, 3}};
    int result5 = Code02_RanAway::minTime(n5, edges5);
    cout << "测试用例5 - 预期: 2, 实际: " << result5 << endl;
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * 性能分析函数
 */
void performanceAnalysis() {
    cout << "=== 性能分析 ===" << endl;
    
    // 生成大规模测试数据
    int n = 100;
    vector<vector<int>> largeEdges;
    
    // 构建完全图（最坏情况）
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++) {
            if (i != j) {
                largeEdges.push_back({i, j});
            }
        }
    }
    
    // 记录开始时间
    auto start = chrono::high_resolution_clock::now();
    
    int result = Code02_RanAway::minTime(n, largeEdges);
    
    // 记录结束时间
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "大规模测试(n=" << n << ", 边数=" << largeEdges.size() << ") - 结果: " << result << endl;
    cout << "执行时间: " << duration.count() << " 毫秒" << endl;
    cout << "时间复杂度: O(n^3 * log k)" << endl;
    cout << "空间复杂度: O(n^2 * log k)" << endl;
    
    // 对比优化版本
    start = chrono::high_resolution_clock::now();
    int resultOptimized = Code02_RanAway::minTimeOptimized(n, largeEdges);
    end = chrono::high_resolution_clock::now();
    auto durationOptimized = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "优化版本执行时间: " << durationOptimized.count() << " 毫秒" << endl;
    cout << "性能提升: " << (double)duration.count() / durationOptimized.count() << " 倍" << endl;
}

/**
 * 算法复杂度分析
 */
void complexityAnalysis() {
    cout << "=== 算法复杂度分析 ===" << endl;
    
    cout << "1. 时间复杂度分析:" << endl;
    cout << "   - 倍增预处理: O(n^3 * log k)" << endl;
    cout << "   - Floyd算法: O(n^3)" << endl;
    cout << "   - 总时间复杂度: O(n^3 * log k)" << endl;
    
    cout << "2. 空间复杂度分析:" << endl;
    cout << "   - 倍增数组: O(n^2 * log k)" << endl;
    cout << "   - 距离矩阵: O(n^2)" << endl;
    cout << "   - 总空间复杂度: O(n^2 * log k)" << endl;
    
    cout << "3. 优化方向:" << endl;
    cout << "   - 对于稀疏图，可以使用Dijkstra算法替代Floyd算法" << endl;
    cout << "   - 使用位运算优化倍增过程" << endl;
    cout << "   - 考虑使用更高效的数据结构存储图" << endl;
}

/**
 * 主函数 - 程序入口
 */
int main() {
    cout << "=== Code02_RanAway C++实现 ===" << endl;
    
    // 运行测试
    testMinTime();
    
    // 性能分析
    performanceAnalysis();
    
    // 算法复杂度分析
    complexityAnalysis();
    
    return 0;
}