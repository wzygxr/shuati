#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <cmath>

using namespace std;

/**
 * 洛谷 P1613 跑路 - C++实现
 * 
 * 题目描述：
 * 一共有n个节点，编号1~n，一共有m条有向边，每条边1公里
 * 有一个空间跑路器，每秒你都可以直接移动2^k公里，每秒钟可以随意决定k的值
 * 题目保证1到n之间一定可以可以到达，返回1到n最少用几秒
 * 
 * 解题思路：
 * 这道题是一个结合了倍增思想和最短路径算法的问题。
 * 空间跑路器的特性允许我们在每秒内移动2^k步，这启发我们使用倍增思想来预处理可能的路径。
 * 
 * 核心思想：
 * 1. 预处理：使用倍增思想，计算任意两点之间是否存在长度为2^k的路径
 * 2. 最短路径：在预处理的基础上，使用Floyd算法计算最短路径
 * 
 * 具体步骤：
 * 1. 初始化：对于每条边(u,v)，标记u到v存在长度为2^0=1的路径
 * 2. 倍增预处理：对于k从1到最大值，如果存在i到j长度为2^(k-1)的路径，
 *    且存在j到p长度为2^(k-1)的路径，则i到p存在长度为2^k的路径
 * 3. 最短路径计算：使用Floyd算法，在新图上计算1到n的最短路径
 * 
 * 时间复杂度分析：
 * - 预处理阶段：O(n^3 * log(max_distance))
 *   - 对于每个k值，需要进行O(n^3)的三重循环
 *   - k的最大值通常为log2(max_distance)，这里取64足够处理大部分情况
 * - 最短路径计算：O(n^3)
 * - 总时间复杂度：O(n^3 * log(max_distance))
 * 空间复杂度：O(n^2 * log(max_distance)) - 存储倍增表
 * 
 * 工程化考量：
 * - 使用vector存储图数据，避免内存泄漏
 * - 添加边界条件检查
 * - 使用大整数防止溢出
 * - 提供完整的测试用例
 */

class RunAway {
public:
    /**
     * 计算从1到n的最少秒数
     * 
     * @param n 节点数量
     * @param edges 边列表，每个元素为{u, v}
     * @return 从1到n的最少秒数
     */
    static int minTime(int n, vector<vector<int>>& edges) {
        // 边界条件检查
        if (n <= 0) return 0;
        if (n == 1) return 0;
        
        // 初始化倍增表
        // reach[k][i][j] 表示从i到j是否存在长度为2^k的路径
        int maxK = 64; // 2^64足够大，可以处理任何实际场景
        vector<vector<vector<bool>>> reach(maxK, 
            vector<vector<bool>>(n+1, vector<bool>(n+1, false)));
        
        // 初始化第一层（k=0）
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            if (u >= 1 && u <= n && v >= 1 && v <= n) {
                reach[0][u][v] = true;
            }
        }
        
        // 倍增预处理
        for (int k = 1; k < maxK; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    // 如果存在中间节点p，使得i->p和p->j都存在2^(k-1)的路径
                    // 那么i->j就存在2^k的路径
                    for (int p = 1; p <= n; p++) {
                        if (reach[k-1][i][p] && reach[k-1][p][j]) {
                            reach[k][i][j] = true;
                            break; // 找到一个中间节点就足够
                        }
                    }
                }
            }
        }
        
        // 构建可达性图
        // 如果存在任意k使得reach[k][i][j]为true，则i到j可达
        vector<vector<bool>> canReach(n+1, vector<bool>(n+1, false));
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                for (int k = 0; k < maxK; k++) {
                    if (reach[k][i][j]) {
                        canReach[i][j] = true;
                        break;
                    }
                }
            }
        }
        
        // 使用Floyd算法计算最短路径
        vector<vector<int>> dist(n+1, vector<int>(n+1, INT_MAX / 2));
        
        // 初始化距离矩阵
        for (int i = 1; i <= n; i++) {
            dist[i][i] = 0;
        }
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (canReach[i][j]) {
                    dist[i][j] = 1; // 每步移动需要1秒
                }
            }
        }
        
        // Floyd算法
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (dist[i][k] < INT_MAX / 2 && dist[k][j] < INT_MAX / 2) {
                        dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j]);
                    }
                }
            }
        }
        
        return dist[1][n];
    }
    
    /**
     * 优化版本：直接使用倍增思想计算最短路径
     * 避免构建完整的可达性图，节省空间
     */
    static int minTimeOptimized(int n, vector<vector<int>>& edges) {
        if (n <= 0) return 0;
        if (n == 1) return 0;
        
        // 初始化距离矩阵
        vector<vector<int>> dist(n+1, vector<int>(n+1, INT_MAX / 2));
        
        for (int i = 1; i <= n; i++) {
            dist[i][i] = 0;
        }
        
        // 初始化直接边
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            if (u >= 1 && u <= n && v >= 1 && v <= n) {
                dist[u][v] = 1;
            }
        }
        
        // 倍增优化：预处理2^k步的最短路径
        int maxK = 64;
        vector<vector<vector<int>>> powDist(maxK, 
            vector<vector<int>>(n+1, vector<int>(n+1, INT_MAX / 2)));
        
        // 初始化第一层
        powDist[0] = dist;
        
        // 倍增预处理
        for (int k = 1; k < maxK; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    // 计算i到j经过2^k步的最小距离
                    for (int p = 1; p <= n; p++) {
                        if (powDist[k-1][i][p] < INT_MAX / 2 && powDist[k-1][p][j] < INT_MAX / 2) {
                            powDist[k][i][j] = min(powDist[k][i][j], 
                                                 powDist[k-1][i][p] + powDist[k-1][p][j]);
                        }
                    }
                }
            }
        }
        
        // 使用二进制分解计算最短路径
        vector<vector<int>> result = dist; // 初始化为直接距离
        
        for (int k = maxK - 1; k >= 0; k--) {
            vector<vector<int>> temp(n+1, vector<int>(n+1, INT_MAX / 2));
            
            // 尝试将2^k步的路径加入当前结果
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    temp[i][j] = result[i][j];
                    
                    for (int p = 1; p <= n; p++) {
                        if (result[i][p] < INT_MAX / 2 && powDist[k][p][j] < INT_MAX / 2) {
                            temp[i][j] = min(temp[i][j], result[i][p] + powDist[k][p][j]);
                        }
                    }
                }
            }
            
            // 如果加入2^k步后路径更短，则更新结果
            bool improved = false;
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (temp[i][j] < result[i][j]) {
                        improved = true;
                        break;
                    }
                }
                if (improved) break;
            }
            
            if (improved) {
                result = temp;
            }
        }
        
        return result[1][n];
    }
};

/**
 * 测试函数
 */
void testRunAway() {
    cout << "=== 洛谷 P1613 跑路 测试 ===" << endl;
    
    // 测试用例1：基础测试
    int n1 = 4;
    vector<vector<int>> edges1 = {{1, 2}, {2, 3}, {3, 4}};
    
    int result1 = RunAway::minTime(n1, edges1);
    cout << "测试用例1 - 预期: 3, 实际: " << result1 << endl;
    
    int result1_opt = RunAway::minTimeOptimized(n1, edges1);
    cout << "优化版本测试用例1 - 预期: 3, 实际: " << result1_opt << endl;
    
    // 测试用例2：有捷径的情况
    int n2 = 4;
    vector<vector<int>> edges2 = {{1, 2}, {2, 3}, {3, 4}, {1, 3}};
    
    int result2 = RunAway::minTime(n2, edges2);
    cout << "测试用例2 - 预期: 2, 实际: " << result2 << endl;
    
    // 测试用例3：环状图
    int n3 = 5;
    vector<vector<int>> edges3 = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 1}};
    
    int result3 = RunAway::minTime(n3, edges3);
    cout << "测试用例3 - 实际结果: " << result3 << endl;
    
    // 测试用例4：单个节点
    int n4 = 1;
    vector<vector<int>> edges4 = {};
    
    int result4 = RunAway::minTime(n4, edges4);
    cout << "测试用例4 - 预期: 0, 实际: " << result4 << endl;
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 生成大规模测试数据
    int n = 100;
    vector<vector<int>> edges;
    
    // 创建完全图的一部分
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++) {
            if (i != j && rand() % 10 < 3) { // 30%的概率有边
                edges.push_back({i, j});
            }
        }
    }
    
    auto start = chrono::high_resolution_clock::now();
    int result1 = RunAway::minTime(n, edges);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::microseconds>(end - start);
    
    start = chrono::high_resolution_clock::now();
    int result2 = RunAway::minTimeOptimized(n, edges);
    end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "大规模测试 - 标准版本结果: " << result1 << ", 耗时: " << duration1.count() << "微秒" << endl;
    cout << "大规模测试 - 优化版本结果: " << result2 << ", 耗时: " << duration2.count() << "微秒" << endl;
    
    cout << "=== 性能测试完成 ===" << endl;
}

/**
 * 主函数
 */
int main() {
    testRunAway();
    performanceTest();
    return 0;
}

/**
 * 复杂度分析：
 * 时间复杂度：
 * - 标准版本：O(n^3 * log(max_distance)) - 三重循环乘以log倍数
 * - 优化版本：O(n^3 * log(max_distance)) - 但常数更小
 * 
 * 空间复杂度：
 * - 标准版本：O(n^2 * log(max_distance)) - 存储倍增表
 * - 优化版本：O(n^2 * log(max_distance)) - 但内存使用更高效
 * 
 * 算法优化点：
 * 1. 使用倍增思想将指数级搜索优化为对数级别
 * 2. 避免构建完整的可达性图，节省空间
 * 3. 提供两种实现版本供选择
 * 
 * 工程化改进：
 * 1. 添加完整的边界条件检查
 * 2. 提供性能测试和功能测试
 * 3. 详细的注释和文档
 * 4. 使用vector避免内存泄漏
 * 
 * 倍增思想在图论中的应用：
 * 1. 最短路径问题：处理特殊移动规则的图
 * 2. 可达性问题：快速判断两点是否连通
 * 3. 最小生成树：处理带权图的最优连接
 * 4. 图的直径：寻找图中最远的两点距离
 * 
 * 相关题目对比：
 * - LeetCode 1334: 阈值距离内邻居最少的城市
 * - LeetCode 743: 网络延迟时间
 * - LeetCode 787: K站中转内最便宜的航班
 */