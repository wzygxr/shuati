#include <iostream>
#include <vector>
#include <string>
#include <queue>
#include <map>
#include <set>
#include <climits>
#include <cmath>
#include <algorithm>
#include <stack>
#include <unordered_map>
#include <unordered_set>
#include <functional>
#include <tuple>
#include <cstring>
#include <float.h>

using namespace std;

// ==================== 优化体系：Knuth优化 ====================
/**
 * Knuth优化的DP算法
 * 
 * 问题描述：
 * 解决区间DP问题，其中状态转移方程满足四边形不等式
 * 
 * 解题思路：
 * 1. 使用Knuth优化将时间复杂度从O(n^3)降低到O(n^2)
 * 2. 维护最优转移点数组opt[i][j]，表示计算dp[i][j]时的最优k值
 * 3. 根据opt[i][j-1] ≤ opt[i][j] ≤ opt[i+1][j]的性质进行剪枝
 * 
 * 参数：
 *     n: 区间长度
 *     costFunc: 计算区间(i,j)代价的函数
 * 
 * 返回：
 *     pair<vector<vector<long long>>, vector<vector<int>>>: 包含dp数组和opt数组的结果
 * 
 * 时间复杂度：O(n^2)
 * 空间复杂度：O(n^2)
 */
pair<vector<vector<long long>>, vector<vector<int>>> knuthOptimization(int n, const function<long long(int, int)>& costFunc) {
    // 初始化dp和opt数组
    const long long INF = LLONG_MAX;
    vector<vector<long long>> dp(n + 1, vector<long long>(n + 1, INF));
    vector<vector<int>> opt(n + 1, vector<int>(n + 1, 0));
    
    // 初始化长度为1的区间
    for (int i = 1; i <= n; ++i) {
        dp[i][i] = 0;
        opt[i][i] = i;
    }
    
    // 枚举区间长度
    for (int length = 2; length <= n; ++length) {
        // 枚举起始点
        for (int i = 1; i + length - 1 <= n; ++i) {
            int j = i + length - 1;
            // 初始化为无穷大
            dp[i][j] = INF;
            // 根据Knuth优化的性质，最优k在opt[i][j-1]到opt[i+1][j]之间
            int upperK = (i + 1 <= j) ? opt[i + 1][j] : j - 1;
            
            for (int k = opt[i][j-1]; k <= min(upperK, j-1); ++k) {
                if (dp[i][k] != INF && dp[k+1][j] != INF) {
                    long long cost = costFunc(i, j);
                    if (cost != INF) {
                        long long current = dp[i][k] + dp[k+1][j] + cost;
                        if (current < dp[i][j]) {
                            dp[i][j] = current;
                            opt[i][j] = k;
                        }
                    }
                }
            }
        }
    }
    
    return {dp, opt};
}

// ==================== 优化体系：Divide & Conquer Optimization ====================
/**
 * 计算dp[i][l..r]，其中最优转移点在opt_l..opt_r之间
 */
void solveDivideConquer(int i, int l, int r, int opt_l, int opt_r, vector<vector<long long>>& dp, 
                       const function<long long(int, int)>& costFunc) {
    if (l > r) return;
    
    int mid = (l + r) / 2;
    int best_k = opt_l;
    const long long INF = LLONG_MAX;
    dp[i][mid] = INF;
    
    // 在opt_l到min(mid, opt_r)之间寻找最优k
    for (int k = opt_l; k <= min(mid, opt_r); ++k) {
        if (dp[i-1][k] != INF) {
            long long cost = costFunc(k, mid);
            if (cost != INF) {
                long long current = dp[i-1][k] + cost;
                if (current < dp[i][mid]) {
                    dp[i][mid] = current;
                    best_k = k;
                }
            }
        }
    }
    
    // 递归处理左右子区间
    solveDivideConquer(i, l, mid - 1, opt_l, best_k, dp, costFunc);
    solveDivideConquer(i, mid + 1, r, best_k, opt_r, dp, costFunc);
}

/**
 * Divide & Conquer Optimization（分治优化）
 * 
 * 问题描述：
 * 解决形如dp[i][j] = min{dp[i-1][k] + cost(k, j)}，其中k < j
 * 当转移满足决策单调性时使用
 * 
 * 解题思路：
 * 1. 利用决策单调性，使用分治法优化DP
 * 2. 对于dp[i][j]，当i固定时，最优转移点k随着j的增加而单调不减
 * 3. 使用分治的方式计算每个区间的最优决策
 * 
 * 参数：
 *     n: 维度1
 *     m: 维度2
 *     costFunc: 计算cost(k,j)的函数
 * 
 * 返回：
 *     vector<vector<long long>>: DP数组
 * 
 * 时间复杂度：O(n*m log m)
 * 空间复杂度：O(n*m)
 */
vector<vector<long long>> divideConquerOptimization(int n, int m, const function<long long(int, int)>& costFunc) {
    // 初始化dp数组
    const long long INF = LLONG_MAX;
    vector<vector<long long>> dp(n + 1, vector<long long>(m + 1, INF));
    dp[0][0] = 0;
    
    // 对每个i应用分治优化
    for (int i = 1; i <= n; ++i) {
        solveDivideConquer(i, 1, m, 0, m, dp, costFunc);
    }
    
    return dp;
}

// ==================== 优化体系：SMAWK算法（行最小查询） ====================
/**
 * 行压缩：只保留可能成为最小值的行
 */
vector<int> reduceRows(const vector<int>& rows, const vector<vector<double>>& matrix) {
    vector<int> stack;
    for (int i : rows) {
        while (stack.size() >= 2) {
            int j1 = stack.back();
            stack.pop_back();
            int j2 = stack.back();
            stack.push_back(j1);  // 恢复栈状态
            
            // 比较两个行在列stack.size()-2处的值（因为索引从0开始）
            if (matrix[j2][stack.size()-2] <= matrix[i][stack.size()-2]) {
                break;
            } else {
                stack.pop_back();
            }
        }
        stack.push_back(i);
    }
    return stack;
}

/**
 * 递归实现SMAWK算法
 */
vector<int> smawkRec(const vector<int>& rows, const vector<int>& cols, const vector<vector<double>>& matrix) {
    if (rows.empty()) return {};
    
    // 行压缩
    vector<int> reducedRows = reduceRows(rows, matrix);
    
    // 递归求解列数为奇数的子问题
    vector<int> halfCols;
    for (size_t i = 1; i < cols.size(); i += 2) {
        halfCols.push_back(cols[i]);
    }
    
    vector<int> minCols(reducedRows.size(), -1);
    
    if (!halfCols.empty()) {
        // 递归求解
        vector<int> result = smawkRec(reducedRows, halfCols, matrix);
        // 复制结果
        for (size_t i = 0; i < result.size(); ++i) {
            minCols[i] = result[i];
        }
    }
    
    // 扩展结果到所有列
    vector<int> result(rows.size());
    int k = 0;  // minCols的索引
    
    for (size_t i = 0; i < rows.size(); ++i) {
        int row = rows[i];
        // 确定当前行的最小值可能在哪个区间
        int start = 0;
        if (i > 0 && k > 0 && minCols[k-1] != -1) {
            start = minCols[k-1];
        }
        int end = (k < (int)minCols.size() && minCols[k] != -1) ? minCols[k] : cols.back();
        
        // 在这个区间内查找最小值
        double minVal = DBL_MAX;
        int minCol = start;
        
        // 找到start和end在cols中的索引
        size_t startIdx = 0, endIdx = cols.size() - 1;
        for (size_t idx = 0; idx < cols.size(); ++idx) {
            if (cols[idx] == start) startIdx = idx;
            if (cols[idx] == end) endIdx = idx;
        }
        
        for (size_t idx = startIdx; idx <= endIdx; ++idx) {
            int col = cols[idx];
            if (col < (int)matrix[0].size() && matrix[row][col] < minVal) {
                minVal = matrix[row][col];
                minCol = col;
            }
        }
        
        result[i] = minCol;
        
        // 如果当前行在reducedRows中，且不是最后一行，k前进
        if (k < (int)reducedRows.size() && row == reducedRows[k]) {
            k++;
        }
    }
    
    return result;
}

/**
 * SMAWK算法用于在Monge矩阵中快速查找每行的最小值
 * 
 * 问题描述：
 * 给定一个Monge矩阵，快速找到每行的最小值位置
 * 
 * 解题思路：
 * 1. Monge矩阵满足性质：matrix[i][j] + matrix[i+1][j+1] ≤ matrix[i][j+1] + matrix[i+1][j]
 * 2. SMAWK算法利用这一性质，可以在O(m+n)时间内找到每行的最小值
 * 3. 主要步骤包括行压缩和递归求解
 * 
 * 参数：
 *     matrix: 一个Monge矩阵
 * 
 * 返回：
 *     每行最小值的列索引列表
 * 
 * 时间复杂度：O(m+n)，其中m是行数，n是列数
 * 空间复杂度：O(m+n)
 */
vector<int> smawk(const vector<vector<double>>& matrix) {
    int m = matrix.size();
    if (m == 0) return {};
    int n = matrix[0].size();
    
    // 构造行索引和列索引数组
    vector<int> rows(m), cols(n);
    for (int i = 0; i < m; ++i) rows[i] = i;
    for (int j = 0; j < n; ++j) cols[j] = j;
    
    // 调用递归实现
    return smawkRec(rows, cols, matrix);
}

// ==================== 优化体系：Aliens Trick（二分约束参数+可行性DP） ====================
/**
 * Aliens Trick（二分约束参数+可行性DP）
 * 
 * 问题描述：
 * 解决带约束的优化问题，通常形如最小化总成本，同时满足某些约束条件
 * 
 * 解题思路：
 * 1. 将约束条件转化为参数λ，构造拉格朗日函数
 * 2. 对λ进行二分查找，使用可行性DP判断当前λ下是否满足约束
 * 3. 根据可行性DP的结果调整二分区间
 * 
 * 参数：
 *     costFunc: 计算带参数λ的成本函数，返回[value, constraint]数组
 *     checkFunc: 检查当前解是否满足约束的函数
 *     left: 二分左边界
 *     right: 二分右边界
 *     eps: 精度要求
 * 
 * 返回：
 *     pair<double, double>: 最优参数lambda和对应最优解
 * 
 * 时间复杂度：O(log((right-left)/eps) * T(DP))，其中T(DP)是一次DP的时间复杂度
 */
pair<double, double> aliensTrick(
    const function<pair<double, double>(double)>& costFunc, 
    const function<bool(double)>& checkFunc,
    double left, double right, double eps = 1e-7) {
    double bestLambda = left;
    double bestValue = 0.0;
    
    while (right - left > eps) {
        double mid = (left + right) / 2;
        // 计算当前参数下的解和约束值
        auto [currentValue, constraintValue] = costFunc(mid);
        
        if (checkFunc(constraintValue)) {
            // 满足约束，尝试更小的参数
            right = mid;
            bestLambda = mid;
            bestValue = currentValue;
        } else {
            // 不满足约束，需要增大参数
            left = mid;
        }
    }
    
    return {bestLambda, bestValue};
}

// ==================== 图上DP→最短路：分层图建模 ====================
/**
 * 分层图Dijkstra算法
 * 
 * 问题描述：
 * 给定一个图，允许最多使用k次特殊操作（如跳跃、免费通行等），求最短路径
 * 
 * 解题思路：
 * 1. 构建分层图，每层代表使用不同次数的特殊操作
 * 2. 对于每个节点u，在第i层表示到达u时已经使用了i次特殊操作
 * 3. 使用Dijkstra算法在分层图上寻找最短路径
 * 
 * 参数：
 *     n: 节点数量
 *     m: 边的数量
 *     edges: 边的列表，每个元素为[u, v, w]表示u到v的权为w的边
 *     k: 允许使用的特殊操作次数
 * 
 * 返回：
 *     long long: 从节点0到节点n-1的最短路径长度，-1表示不可达
 * 
 * 时间复杂度：O((n*k + m*k) log(n*k))
 * 空间复杂度：O(n*k + m*k)
 */
long long layeredGraphDijkstra(int n, int m, const vector<tuple<int, int, long long>>& edges, int k) {
    // 构建分层图的邻接表
    vector<vector<pair<int, long long>>> graph(n * (k + 1));
    
    // 添加普通边（不使用特殊操作）
    for (const auto& [u, v, w] : edges) {
        for (int i = 0; i <= k; ++i) {
            int from_node = u + i * n;
            graph[from_node].emplace_back(v + i * n, w);
        }
    }
    
    // 添加使用特殊操作的边（如果允许的话）
    for (const auto& [u, v, w] : edges) {
        for (int i = 0; i < k; ++i) {
            // 这里假设特殊操作可以免费通行（权为0），具体根据问题调整
            int from_node = u + i * n;
            graph[from_node].emplace_back(v + (i + 1) * n, 0);
        }
    }
    
    // Dijkstra算法
    const long long INF = LLONG_MAX;
    vector<long long> dist(n * (k + 1), INF);
    dist[0] = 0;  // 假设起点是节点0
    
    // 使用优先队列，按距离排序
    priority_queue<pair<long long, int>, vector<pair<long long, int>>, greater<>> heap;
    heap.emplace(0, 0);
    
    while (!heap.empty()) {
        auto [d, u] = heap.top();
        heap.pop();
        
        if (d > dist[u]) continue;
        
        for (const auto& [v, w] : graph[u]) {
            if (dist[v] > d + w) {
                dist[v] = d + w;
                heap.emplace(dist[v], v);
            }
        }
    }
    
    // 取所有层中到达终点的最小值
    long long result = INF;
    for (int i = 0; i <= k; ++i) {
        if (dist[n - 1 + i * n] < result) {
            result = dist[n - 1 + i * n];
        }
    }
    
    return result != INF ? result : -1;
}

// ==================== 冷门模型：期望DP遇环的方程组解（高斯消元） ====================
/**
 * 高斯消元法求解线性方程组
 * 
 * 问题描述：
 * 求解形如Ax = b的线性方程组
 * 
 * 解题思路：
 * 1. 构建增广矩阵
 * 2. 进行高斯消元，将矩阵转化为行阶梯形
 * 3. 回代求解
 * 
 * 参数：
 *     matrix: 增广矩阵，每行最后一个元素是b的值
 * 
 * 返回：
 *     vector<double>: 方程组的解数组
 * 
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 */
vector<double> gaussianElimination(vector<vector<double>> matrix) {
    int n = matrix.size();
    const double eps = 1e-9;
    
    // 高斯消元过程
    for (int i = 0; i < n; ++i) {
        // 找到主元行（当前列中绝对值最大的行）
        int maxRow = i;
        for (int j = i; j < n; ++j) {
            if (abs(matrix[j][i]) > abs(matrix[maxRow][i])) {
                maxRow = j;
            }
        }
        
        // 交换主元行和当前行
        swap(matrix[i], matrix[maxRow]);
        
        // 如果主元为0，方程组可能有无穷多解或无解
        if (abs(matrix[i][i]) < eps) continue;
        
        // 消元过程
        for (int j = i + 1; j < n; ++j) {
            double factor = matrix[j][i] / matrix[i][i];
            for (int k = i; k <= n; ++k) {
                matrix[j][k] -= factor * matrix[i][k];
            }
        }
    }
    
    // 回代求解
    vector<double> x(n);
    for (int i = n - 1; i >= 0; --i) {
        x[i] = matrix[i][n];
        for (int j = i + 1; j < n; ++j) {
            x[i] -= matrix[i][j] * x[j];
        }
        x[i] /= matrix[i][i];
    }
    
    return x;
}

/**
 * 期望DP处理有环情况（使用高斯消元）
 * 
 * 问题描述：
 * 在有环的状态转移图中计算期望
 * 
 * 解题思路：
 * 1. 对于每个状态，建立期望方程
 * 2. 使用高斯消元求解方程组
 * 
 * 参数：
 *     n: 状态数量
 *     transitions: 转移概率列表，transitions[i]是一个列表，每个元素为(j, p)表示从i转移到j的概率为p
 * 
 * 返回：
 *     vector<double>: 每个状态的期望值数组
 * 
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 */
vector<double> expectationDPWithCycles(int n, const vector<vector<pair<int, double>>>& transitions) {
    // 构建线性方程组的增广矩阵
    vector<vector<double>> matrix(n, vector<double>(n + 1, 0.0));
    
    for (int i = 0; i < n; ++i) {
        matrix[i][i] = 1.0;  // 方程左边：E[i] - sum(p_ij * E[j]) = cost[i]
        
        // 假设每个状态的代价为1，具体根据问题调整
        double cost = 1.0;
        matrix[i][n] = cost;
        
        for (const auto& [j, p] : transitions[i]) {
            if (i != j) {  // 避免自环的特殊处理
                matrix[i][j] -= p;
            }
        }
    }
    
    // 使用高斯消元求解
    return gaussianElimination(matrix);
}

// ==================== 冷门模型：插头DP（轮廓线DP） ====================
/**
 * 插头DP（轮廓线DP）示例：求网格中哈密顿回路的数量
 * 
 * 问题描述：
 * 给定一个网格，求其中哈密顿回路的数量
 * 
 * 解题思路：
 * 1. 使用轮廓线DP，记录当前处理到的位置和轮廓线状态
 * 2. 插头表示连接的状态，通常用二进制表示
 * 3. 使用字典优化空间复杂度
 * 
 * 参数：
 *     grid: 网格，1表示可通行，0表示障碍物
 * 
 * 返回：
 *     long long: 哈密顿回路的数量
 * 
 * 时间复杂度：O(n*m*4^min(n,m))
 * 空间复杂度：O(4^min(n,m))
 */
long long plugDP(const vector<vector<int>>& grid) {
    int n = grid.size();
    if (n == 0) return 0;
    int m = grid[0].size();
    
    // 使用unordered_map优化
    using StateMap = unordered_map<long long, long long>;
    StateMap dp;
    
    // 初始状态：左上角没有插头
    dp[0] = 1;
    
    for (int i = 0; i < n; ++i) {
        // 新的一行开始，需要将状态左移一位
        StateMap newDp;
        for (const auto& [state, cnt] : dp) {
            // 左移一位，移除最左边的插头
            long long newState = state << 1;
            newDp[newState] += cnt;
        }
        dp.swap(newDp);
        
        for (int j = 0; j < m; ++j) {
            StateMap newDp2;
            
            for (const auto& [state, cnt] : dp) {
                // 当前位置左边和上边的插头状态
                int left = (state >> (2 * j)) & 3;
                int up = (state >> (2 * (j + 1))) & 3;
                
                // 如果当前位置是障碍物，跳过
                if (grid[i][j] == 0) {
                    // 只有当左右插头都不存在时才合法
                    if (left == 0 && up == 0) {
                        newDp2[state] += cnt;
                    }
                    continue;
                }
                
                // 处理各种插头组合情况
                // 1. 没有左插头和上插头
                if (left == 0 && up == 0) {
                    // 只能创建新的插头对（用于回路的开始）
                    if (i < n - 1 && j < m - 1 && grid[i+1][j] == 1 && grid[i][j+1] == 1) {
                        long long newState = state | (1LL << (2 * j)) | (2LL << (2 * (j + 1)));
                        newDp2[newState] += cnt;
                    }
                }
                // 2. 只有左插头
                else if (left != 0 && up == 0) {
                    // 向下延伸
                    if (i < n - 1 && grid[i+1][j] == 1) {
                        newDp2[state] += cnt;
                    }
                    // 向右延伸
                    if (j < m - 1 && grid[i][j+1] == 1) {
                        long long newState = (state & ~(3LL << (2 * j))) | (static_cast<long long>(left) << (2 * (j + 1)));
                        newDp2[newState] += cnt;
                    }
                }
                // 3. 只有上插头
                else if (left == 0 && up != 0) {
                    // 向右延伸
                    if (j < m - 1 && grid[i][j+1] == 1) {
                        newDp2[state] += cnt;
                    }
                    // 向下延伸
                    if (i < n - 1 && grid[i+1][j] == 1) {
                        long long newState = (state & ~(3LL << (2 * (j + 1)))) | (static_cast<long long>(up) << (2 * j));
                        newDp2[newState] += cnt;
                    }
                }
                // 4. 同时有左插头和上插头
                else {
                    // 合并插头
                    long long newState = (state & ~(3LL << (2 * j))) & ~(3LL << (2 * (j + 1)));
                    
                    // 如果是形成回路的最后一步
                    if (left == up) {
                        // 检查是否所有插头都已连接
                        if (newState == 0 && i == n - 1 && j == m - 1) {
                            newDp2[newState] += cnt;
                        }
                    } else {
                        // 合并两个不同的插头
                        newDp2[newState] += cnt;
                    }
                }
            }
            
            dp.swap(newDp2);
        }
    }
    
    // 最终状态应该是没有任何插头（形成回路）
    return dp.count(0) ? dp[0] : 0;
}

// ==================== 冷门模型：树上背包的优化 ====================
/**
 * 树上背包的DFS处理函数
 */
void dfsTreeKnapsack(int u, int parent, int capacity, const vector<vector<int>>& tree, 
                    const vector<int>& weights, const vector<int>& values, 
                    vector<vector<int>>& dp, vector<int>& size) {
    // 初始化当前节点
    size[u] = 1;
    if (weights[u] <= capacity) {
        dp[u][weights[u]] = values[u];
    }
    
    // 对每个子节点，按照子树大小排序，小的先合并
    vector<pair<int, int>> children;
    for (int v : tree[u]) {
        if (v != parent) {
            dfsTreeKnapsack(v, u, capacity, tree, weights, values, dp, size);
            children.emplace_back(size[v], v);
        }
    }
    
    // 按子树大小排序
    sort(children.begin(), children.end());
    
    for (const auto& [sz, v] : children) {
        // 逆序遍历容量，避免重复计算
        for (int i = min(size[u], capacity); i >= 0; --i) {
            if (dp[u][i] == 0 && i != 0) continue;
            for (int j = 1; j <= min(sz, capacity - i); ++j) {
                if (dp[v][j] > 0 && i + j <= capacity) {
                    dp[u][i + j] = max(dp[u][i + j], dp[u][i] + dp[v][j]);
                }
            }
        }
        
        // 更新子树大小
        size[u] += sz;
    }
}

/**
 * 树上背包的优化实现（小到大合并）
 * 
 * 问题描述：
 * 在树上选择一些节点，使得总重量不超过容量，且总价值最大
 * 
 * 解题思路：
 * 1. 使用后序遍历处理子树
 * 2. 使用小到大合并的策略优化复杂度
 * 3. 对于每个节点，维护一个容量为capacity的背包
 * 
 * 参数：
 *     root: 根节点
 *     capacity: 背包容量
 *     tree: 树的邻接表
 *     weights: 每个节点的重量
 *     values: 每个节点的价值
 * 
 * 返回：
 *     int: 最大价值
 * 
 * 时间复杂度：O(n*capacity^2)，但通过小到大合并可以降低常数
 * 空间复杂度：O(n*capacity)
 */
int treeKnapsackOptimized(int root, int capacity, const vector<vector<int>>& tree, 
                         const vector<int>& weights, const vector<int>& values) {
    int n = tree.size();
    // 初始化dp数组
    vector<vector<int>> dp(n, vector<int>(capacity + 1, 0));
    vector<int> size(n, 0);
    
    // 深度优先搜索处理子树
    dfsTreeKnapsack(root, -1, capacity, tree, weights, values, dp, size);
    
    // 返回根节点的最大价值
    return *max_element(dp[root].begin(), dp[root].end());
}

// ==================== 补充题目与应用 ====================
// 以下是一些使用上述高级DP技术的经典题目及其代码实现

/**
 * LeetCode 72. 编辑距离
 * 题目链接：https://leetcode-cn.com/problems/edit-distance/
 * 
 * 问题描述：
 * 给你两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数。
 * 你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
 * 
 * 解题思路：
 * 使用二维DP，dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数。
 * 
 * 时间复杂度：O(m*n)
 * 空间复杂度：O(m*n)
 */
int editDistance(const string& word1, const string& word2) {
    int m = word1.size();
    int n = word2.size();
    // dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数
    vector<vector<int>> dp(m + 1, vector<int>(n + 1));
    
    // 初始化边界
    for (int i = 0; i <= m; ++i) dp[i][0] = i;
    for (int j = 0; j <= n; ++j) dp[0][j] = j;
    
    // 动态规划填表
    for (int i = 1; i <= m; ++i) {
        for (int j = 1; j <= n; ++j) {
            if (word1[i-1] == word2[j-1]) {
                dp[i][j] = dp[i-1][j-1];
            } else {
                dp[i][j] = min({dp[i-1][j], dp[i][j-1], dp[i-1][j-1]}) + 1;
            }
        }
    }
    
    return dp[m][n];
}

/**
 * LeetCode 300. 最长递增子序列
 * 题目链接：https://leetcode-cn.com/problems/longest-increasing-subsequence/
 * 
 * 问题描述：
 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
 * 
 * 解题思路：
 * 使用贪心 + 二分查找优化的DP方法。
 * tails[i]表示长度为i+1的递增子序列的末尾元素的最小值。
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
int lengthOfLIS(vector<int>& nums) {
    if (nums.empty()) return 0;
    
    vector<int> tails;
    for (int num : nums) {
        // 二分查找num应该插入的位置
        auto it = lower_bound(tails.begin(), tails.end(), num);
        if (it == tails.end()) {
            tails.push_back(num);
        } else {
            *it = num;
        }
    }
    
    return tails.size();
}

/**
 * LeetCode 322. 零钱兑换
 * 题目链接：https://leetcode-cn.com/problems/coin-change/
 * 
 * 问题描述：
 * 给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
 * 计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回-1。
 * 
 * 解题思路：
 * 使用完全背包的思想，dp[i]表示凑成金额i所需的最少硬币数。
 * 
 * 时间复杂度：O(amount * n)
 * 空间复杂度：O(amount)
 */
int coinChange(vector<int>& coins, int amount) {
    // 初始化dp数组为无穷大
    const int INF = INT_MAX - 1; // 避免溢出
    vector<int> dp(amount + 1, INF);
    dp[0] = 0; // 凑成金额0需要0个硬币
    
    for (int coin : coins) {
        for (int i = coin; i <= amount; ++i) {
            if (dp[i - coin] != INF) {
                dp[i] = min(dp[i], dp[i - coin] + 1);
            }
        }
    }
    
    return dp[amount] != INF ? dp[amount] : -1;
}

/**
 * 矩阵链乘法问题
 * 题目来源：算法导论
 * 
 * 问题描述：
 * 给定一系列矩阵，计算乘法顺序使得标量乘法的次数最少。
 * 
 * 解题思路：
 * 使用区间DP，dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数。
 * 可以使用Knuth优化进一步降低时间复杂度。
 * 
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 */
pair<vector<vector<long long>>, vector<vector<int>>> matrixChainOrder(const vector<int>& p) {
    int n = p.size() - 1; // 矩阵的个数
    // dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数
    const long long INF = LLONG_MAX;
    vector<vector<long long>> dp(n + 1, vector<long long>(n + 1, INF));
    // s[i][j]记录最优分割点
    vector<vector<int>> s(n + 1, vector<int>(n + 1, 0));
    
    // 单个矩阵的代价为0
    for (int i = 1; i <= n; ++i) {
        dp[i][i] = 0;
    }
    
    // 枚举区间长度
    for (int length = 2; length <= n; ++length) {
        for (int i = 1; i <= n - length + 1; ++i) {
            int j = i + length - 1;
            dp[i][j] = INF;
            // 枚举分割点
            for (int k = i; k < j; ++k) {
                // 计算当前分割点的代价
                long long cost = dp[i][k] + dp[k + 1][j] + static_cast<long long>(p[i - 1]) * p[k] * p[j];
                if (cost < dp[i][j]) {
                    dp[i][j] = cost;
                    s[i][j] = k;
                }
            }
        }
    }
    
    return {dp, s};
}

/**
 * 旅行商问题
 * 题目来源：算法竞赛经典问题
 * 
 * 问题描述：
 * 给定一个完全图，找到一条访问每个城市恰好一次并返回起点的最短路径。
 * 
 * 解题思路：
 * 使用状态压缩DP，dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度。
 * 
 * 时间复杂度：O(n^2 * 2^n)
 * 空间复杂度：O(n * 2^n)
 */
long long travelingSalesmanProblem(const vector<vector<long long>>& graph) {
    int n = graph.size();
    // dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度
    const long long INF = LLONG_MAX;
    vector<vector<long long>> dp(1 << n, vector<long long>(n, INF));
    
    // 初始状态：只访问了起点，路径长度为0
    for (int i = 0; i < n; ++i) {
        dp[1 << i][i] = 0;
    }
    
    // 枚举所有可能的状态
    for (int mask = 1; mask < (1 << n); ++mask) {
        // 枚举当前所在的城市
        for (int u = 0; u < n; ++u) {
            if (!(mask & (1 << u))) continue;
            // 枚举下一个要访问的城市
            for (int v = 0; v < n; ++v) {
                if (mask & (1 << v)) continue;
                int newMask = mask | (1 << v);
                if (dp[mask][u] != INF && graph[u][v] != INF) {
                    if (dp[newMask][v] > dp[mask][u] + graph[u][v]) {
                        dp[newMask][v] = dp[mask][u] + graph[u][v];
                    }
                }
            }
        }
    }
    
    // 找到最短的回路
    long long result = INF;
    for (int u = 0; u < n; ++u) {
        if (dp[(1 << n) - 1][u] != INF && graph[u][0] != INF) {
            result = min(result, dp[(1 << n) - 1][u] + graph[u][0]);
        }
    }
    
    return result != INF ? result : -1;
}

/**
 * LeetCode 1039. 多边形三角剖分的最低得分
 * 题目链接：https://leetcode-cn.com/problems/minimum-score-triangulation-of-polygon/
 * 
 * 问题描述：
 * 给定一个凸多边形，将其三角剖分，使得所有三角形的顶点乘积之和最小。
 * 
 * 解题思路：
 * 使用区间DP，dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分。
 * 
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 */
int minimumScoreTriangulation(vector<int>& values) {
    int n = values.size();
    // dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分
    vector<vector<int>> dp(n, vector<int>(n, 0));
    
    // 枚举区间长度
    for (int length = 3; length <= n; ++length) {
        for (int i = 0; i <= n - length; ++i) {
            int j = i + length - 1;
            dp[i][j] = INT_MAX;
            // 枚举中间点
            for (int k = i + 1; k < j; ++k) {
                dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j] + values[i] * values[k] * values[j]);
            }
        }
    }
    
    return dp[0][n - 1];
}

/**
 * LeetCode 877. 石子游戏
 * 题目链接：https://leetcode-cn.com/problems/stone-game/
 * 
 * 问题描述：
 * 给定一个表示石子堆的数组，两个玩家轮流从两端取石子，每次只能取一个，取到最后一个石子的人获胜。
 * 判断先手是否必胜。
 * 
 * 解题思路：
 * 使用区间DP，dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分。
 * 
 * 时间复杂度：O(n^2)
 * 空间复杂度：O(n^2)
 */
bool stoneGame(vector<int>& piles) {
    int n = piles.size();
    // dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分
    vector<vector<int>> dp(n, vector<int>(n, 0));
    
    // 初始化单个石子堆
    for (int i = 0; i < n; ++i) {
        dp[i][i] = piles[i];
    }
    
    // 枚举区间长度
    for (int length = 2; length <= n; ++length) {
        for (int i = 0; i <= n - length; ++i) {
            int j = i + length - 1;
            // 先手可以选择取左边或右边
            dp[i][j] = max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
        }
    }
    
    // 先手净胜分大于0则必胜
    return dp[0][n - 1] > 0;
}

/**
 * LeetCode 233. 数字1的个数
 * 题目链接：https://leetcode-cn.com/problems/number-of-digit-one/
 * 
 * 问题描述：
 * 给定一个整数 n，计算所有小于等于 n 的非负整数中数字1出现的个数。
 * 
 * 解题思路：
 * 使用数位DP，逐位处理每一位上1出现的次数。
 * 
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n)
 */
int countDigitOne(int n) {
    if (n <= 0) return 0;
    
    string s = to_string(n);
    int length = s.size();
    int count = 0;
    
    // 逐位处理
    for (int i = 0; i < length; ++i) {
        int high = 0;
        if (i > 0) {
            high = stoi(s.substr(0, i));
        }
        int current = s[i] - '0';
        int low = 0;
        if (i < length - 1) {
            low = stoi(s.substr(i + 1));
        }
        long long digit = pow(10, length - i - 1);
        
        if (current == 0) {
            // 当前位为0，高位决定
            count += high * digit;
        } else if (current == 1) {
            // 当前位为1，高位+低位+1
            count += high * digit + low + 1;
        } else {
            // 当前位大于1，高位+1
            count += (high + 1) * digit;
        }
    }
    
    return count;
}

/**
 * 树节点定义
 */
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

/**
 * 树形DP处理函数 - 打家劫舍III
 */
pair<int, int> robDFS(TreeNode* node) {
    if (!node) return {0, 0};
    
    auto left = robDFS(node->left);
    auto right = robDFS(node->right);
    
    // rob_current表示偷当前节点，not_rob_current表示不偷当前节点
    int rob_current = node->val + left.second + right.second;
    int not_rob_current = max(left.first, left.second) + max(right.first, right.second);
    
    return {rob_current, not_rob_current};
}

/**
 * LeetCode 337. 打家劫舍 III
 * 题目链接：https://leetcode-cn.com/problems/house-robber-iii/
 * 
 * 问题描述：
 * 在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。
 * 这个地区只有一个入口，我们称之为“根”。除了“根”之外，每栋房子有且只有一个“父“房子与之相连。
 * 一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。
 * 如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
 * 计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
 * 
 * 解题思路：
 * 使用树形DP，对于每个节点，维护两个状态：偷或不偷。
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(h)，h为树的高度
 */
int rob(TreeNode* root) {
    auto result = robDFS(root);
    return max(result.first, result.second);
}

/**
 * 蒙斯特曼问题
 * 题目来源：算法竞赛问题
 * 
 * 问题描述：
 * 在网格中放置怪物，使得任何两个怪物都不在同一行、同一列或对角线上。
 * 
 * 解题思路：
 * 使用状态压缩DP，dp[mask]表示处理到当前行，已放置的列的状态为mask时的方案数。
 * 
 * 时间复杂度：O(n * 2^n)
 * 空间复杂度：O(2^n)
 */
long long monsterGame(const vector<vector<int>>& grid) {
    int n = grid.size();
    // dp[mask]表示处理到当前行，已放置的列的状态为mask时的方案数
    vector<long long> dp(1 << n, 0);
    dp[0] = 1;
    
    for (int i = 0; i < n; ++i) {
        vector<long long> newDp(1 << n, 0);
        for (int mask = 0; mask < (1 << n); ++mask) {
            if (dp[mask] == 0) continue;
            // 枚举所有可能的放置位置
            for (int j = 0; j < n; ++j) {
                // 检查是否可以在(i,j)放置怪物
                if (!(mask & (1 << j)) && grid[i][j] == 1) {
                    // 检查对角线
                    bool valid = true;
                    for (int k = 0; k < i; ++k) {
                        if (mask & (1 << k) && abs(k - j) == i - k) {
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        newDp[mask | (1 << j)] += dp[mask];
                    }
                }
            }
        }
        dp.swap(newDp);
    }
    
    return dp[(1 << n) - 1];
}

/**
 * 三维背包问题
 * 题目来源：算法竞赛问题
 * 
 * 问题描述：
 * 有n个物品，每个物品有体积、重量、价值三个属性，背包有体积和重量两个限制，求最大价值。
 * 
 * 解题思路：
 * 使用三维DP，dp[i][j][k]表示前i个物品，体积为j，重量为k时的最大价值。
 * 
 * 时间复杂度：O(n * V * W)
 * 空间复杂度：O(n * V * W)
 */
int threeDimensionKnapsack(int n, const pair<int, int>& capacity, const vector<tuple<int, int, int>>& items) {
    int V = capacity.first;
    int W = capacity.second;
    // 初始化dp数组
    vector<vector<vector<int>>> dp(n + 1, vector<vector<int>>(V + 1, vector<int>(W + 1, 0)));
    
    for (int i = 1; i <= n; ++i) {
        int v = get<0>(items[i-1]);
        int w = get<1>(items[i-1]);
        int val = get<2>(items[i-1]);
        
        for (int j = 0; j <= V; ++j) {
            for (int k = 0; k <= W; ++k) {
                // 不选当前物品
                dp[i][j][k] = dp[i-1][j][k];
                // 选当前物品（如果有足够的空间）
                if (j >= v && k >= w) {
                    dp[i][j][k] = max(dp[i][j][k], dp[i-1][j-v][k-w] + val);
                }
            }
        }
    }
    
    return dp[n][V][W];
}

/**
 * 凸包优化技巧示例
 * 题目来源：算法竞赛问题
 * 
 * 问题描述：
 * 当状态转移方程形如dp[i] = min{dp[j] + a[i] * b[j] + c}时，可以使用凸包优化。
 * 
 * 解题思路：
 * 将转移方程转换为直线的形式，维护凸包以快速查询最小值。
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
class ConvexHullTrick {
private:
    struct Line {
        double k, b;
        Line(double k_ = 0, double b_ = 0) : k(k_), b(b_) {}
    };
    deque<Line> dq;
    
    // 获取队列中倒数第n个元素
    Line getNthLast(int n) {
        vector<Line> temp(dq.begin(), dq.end());
        return temp[temp.size() - n];
    }
    
public:
    // 添加一条直线
    void addLine(double k, double b) {
        // 当队列中至少有两条直线时，检查是否需要删除末尾的直线
        while (dq.size() >= 2) {
            Line l1 = getNthLast(2);
            Line l2 = dq.back();
            
            // 判断直线l1和l2的交点是否在l2和新直线的交点右侧
            if ((l2.b - l1.b) * (k - l2.k) >= (b - l2.b) * (l2.k - l1.k)) {
                dq.pop_back();
            } else {
                break;
            }
        }
        dq.emplace_back(k, b);
    }
    
    // 查询x对应的最小值
    double query(double x) {
        while (dq.size() >= 2) {
            Line l1 = dq.front();
            dq.pop_front();
            Line l2 = dq.front();
            
            if (l1.k * x + l1.b >= l2.k * x + l2.b) {
                // 继续弹出
                continue;
            } else {
                dq.push_front(l1);  // 恢复l1
                break;
            }
        }
        
        if (dq.empty()) {
            return DBL_MAX;
        }
        Line l = dq.front();
        return l.k * x + l.b;
    }
};

// ==================== 高级优化体系：SMAWK算法（行最小查询） ====================
/**
 * SMAWK算法用于在Monge矩阵中快速查找每行的最小值
 * 
 * 问题描述：
 * 给定一个Monge矩阵，快速找到每行的最小值位置
 * 
 * 解题思路：
 * 1. Monge矩阵满足性质：matrix[i][j] + matrix[i+1][j+1] ≤ matrix[i][j+1] + matrix[i+1][j]
 * 2. SMAWK算法利用这一性质，可以在O(m+n)时间内找到每行的最小值
 * 3. 主要步骤包括行压缩和递归求解
 * 
 * 应用题目：
 * - POJ 3156 Interconnect
 * - Codeforces 472D Design Tutorial: Inverse the Problem
 * - SPOJ MCQUERY
 * 
 * 时间复杂度：O(m+n)，其中m是行数，n是列数
 * 空间复杂度：O(m+n)
 */
class SMAWK {
private:
    // 行压缩：只保留可能成为最小值的行
    static vector<int> reduceRows(const vector<vector<int>>& matrix, const vector<int>& rows) {
        vector<int> stack;
        for (int i : rows) {
            while (stack.size() >= 2) {
                int j1 = stack.back();
                stack.pop_back();
                int j2 = stack.back();
                stack.pop_back();
                stack.push_back(j1); // 恢复栈状态
                
                // 比较两个行在列stack.size()-2处的值
                int col = stack.size() - 2;
                if (col < matrix[0].size()) {
                    if (matrix[j2][col] <= matrix[i][col]) {
                        break;
                    } else {
                        stack.pop_back(); // 移除j1
                    }
                } else {
                    break;
                }
            }
            stack.push_back(i);
        }
        return stack;
    }
    
    // 递归实现SMAWK算法
    static vector<int> smawkRec(const vector<vector<int>>& matrix, const vector<int>& rows, const vector<int>& cols) {
        int m = rows.size();
        vector<int> result(m, -1);
        
        if (m == 0) {
            return result;
        }
        
        // 行压缩
        vector<int> reducedRows = reduceRows(matrix, rows);
        
        // 递归求解列数为奇数的子问题
        vector<int> halfCols;
        for (int i = 1; i < cols.size(); i += 2) {
            halfCols.push_back(cols[i]);
        }
        
        vector<int> minCols(reducedRows.size(), -1);
        
        if (!halfCols.empty()) {
            // 递归求解
            vector<int> subResult = smawkRec(matrix, reducedRows, halfCols);
            // 复制结果
            for (int i = 0; i < subResult.size(); ++i) {
                minCols[i] = subResult[i];
            }
        }
        
        // 扩展结果到所有列
        int k = 0; // minCols的索引
        
        for (int i = 0; i < m; ++i) {
            int row = rows[i];
            // 确定当前行的最小值可能在哪个区间
            int start = 0;
            if (i > 0 && k > 0 && minCols[k-1] != -1) {
                start = minCols[k-1];
            }
            int end = (k < minCols.size() && minCols[k] != -1) ? minCols[k] : cols.back();
            
            // 在这个区间内查找最小值
            int minVal = INT_MAX;
            int minCol = start;
            
            // 找到start和end在cols中的索引
            int startIdx = 0, endIdx = cols.size() - 1;
            for (int idx = 0; idx < cols.size(); ++idx) {
                if (cols[idx] == start) startIdx = idx;
                if (cols[idx] == end) endIdx = idx;
            }
            
            for (int idx = startIdx; idx <= endIdx; ++idx) {
                int col = cols[idx];
                if (col < matrix[0].size() && matrix[row][col] < minVal) {
                    minVal = matrix[row][col];
                    minCol = col;
                }
            }
            
            result[i] = minCol;
            
            // 如果当前行在reducedRows中，且不是最后一行，k前进
            if (k < reducedRows.size() && row == reducedRows[k]) {
                ++k;
            }
        }
        
        return result;
    }

public:
    // SMAWK算法主入口
    static vector<int> solve(const vector<vector<int>>& matrix) {
        if (matrix.empty() || matrix[0].empty()) {
            return {};
        }
        
        int m = matrix.size();
        int n = matrix[0].size();
        
        // 构造行索引和列索引数组
        vector<int> rows(m);
        vector<int> cols(n);
        for (int i = 0; i < m; ++i) rows[i] = i;
        for (int j = 0; j < n; ++j) cols[j] = j;
        
        // 调用递归实现
        return smawkRec(matrix, rows, cols);
    }
    
    // 应用示例：寻找每一行的最小元素
    static vector<int> findRowMins(const vector<vector<int>>& matrix) {
        vector<int> minCols = solve(matrix);
        vector<int> result;
        for (int i = 0; i < matrix.size(); ++i) {
            result.push_back(matrix[i][minCols[i]]);
        }
        return result;
    }
};

// ==================== 高级优化体系：Aliens Trick（二分约束参数+可行性DP） ====================
/**
 * Aliens Trick（二分约束参数+可行性DP）
 * 
 * 问题描述：
 * 解决带约束的优化问题，通常形如最小化总成本，同时满足某些约束条件
 * 
 * 解题思路：
 * 1. 将约束条件转化为参数λ，构造拉格朗日函数
 * 2. 对λ进行二分查找，使用可行性DP判断当前λ下是否满足约束
 * 3. 根据可行性DP的结果调整二分区间
 * 
 * 应用题目：
 * - Codeforces 739E Gosha is Hunting
 * - POJ 3686 The Windy's
 * - SPOJ QTREE5
 * 
 * 时间复杂度：O(log((right-left)/eps) * T(DP))，其中T(DP)是一次DP的时间复杂度
 * 空间复杂度：O(DP空间复杂度)
 */
class AliensTrick {
private:
    // 结果类
    struct Result {
        double lambdaVal;
        double value;
        
        Result(double l, double v) : lambdaVal(l), value(v) {}
    };
    
    // 成本函数结果类
    struct CostFunctionResult {
        double value;
        int constraint;
        
        CostFunctionResult(double v, int c) : value(v), constraint(c) {}
    };
    
    // Aliens Trick主入口
    template<typename CostFunc, typename CheckFunc>
    static Result solve(CostFunc costFunc, CheckFunc checkFunc, double left, double right, double eps = 1e-7) {
        double bestLambda = left;
        double bestValue = 0.0;
        
        // 二分查找参数lambda
        while (right - left > eps) {
            double mid = (left + right) / 2;
            // 计算当前参数下的解和约束值
            CostFunctionResult result = costFunc(mid);
            
            if (checkFunc(result.constraint)) {
                // 满足约束，尝试更小的参数
                right = mid;
                bestLambda = mid;
                bestValue = result.value;
            } else {
                // 不满足约束，需要增大参数
                left = mid;
            }
        }
        
        return Result(bestLambda, bestValue);
    }

public:
    // 应用示例：将数组分成恰好k个部分，使得最大子数组和最小（LeetCode 410的变种）
    static double splitArrayK(const vector<int>& nums, int k) {
        // 计算数组元素和作为二分上限
        long long sumVal = 0;
        for (int num : nums) sumVal += num;
        
        // 成本函数：使用DP计算在给定lambda下的最小成本
        auto costFunc = [&nums](double lambda) -> CostFunctionResult {
            int n = nums.size();
            const double INF = 1e18;
            vector<double> dp(n + 1, INF);
            vector<int> cnt(n + 1, 0);
            
            dp[0] = 0;
            cnt[0] = 0;
            
            for (int i = 1; i <= n; ++i) {
                long long sumSeg = 0;
                for (int j = i - 1; j >= 0; --j) {
                    sumSeg += nums[j];
                    if (dp[j] != INF) {
                        double current = dp[j] + 1LL * sumSeg * sumSeg + lambda; // lambda作为惩罚项
                        if (current < dp[i]) {
                            dp[i] = current;
                            cnt[i] = cnt[j] + 1;
                        }
                    }
                }
            }
            
            return CostFunctionResult(dp[n], cnt[n]);
        };
        
        // 约束检查函数：确保分割次数不超过k
        auto checkFunc = [k](int constraint) -> bool {
            return constraint <= k;
        };
        
        // 执行Aliens Trick
        Result result = solve(costFunc, checkFunc, 0.0, 1LL * sumVal * sumVal, 1e-7);
        return result.value;
    }
};

// ==================== 图上DP→最短路：分层图建模 ====================
/**
 * 分层图Dijkstra算法
 * 
 * 问题描述：
 * 给定一个图，允许最多使用k次特殊操作（如跳跃、免费通行等），求最短路径
 * 
 * 解题思路：
 * 1. 构建分层图，每层代表使用不同次数的特殊操作
 * 2. 对于每个节点u，在第i层表示到达u时已经使用了i次特殊操作
 * 3. 使用Dijkstra算法在分层图上寻找最短路径
 * 
 * 应用题目：
 * - LeetCode 787. K 站中转内最便宜的航班
 * - POJ 3159 Candies
 * - HDU 2957 Safety Assessment
 * 
 * 时间复杂度：O((n*k + m*k) log(n*k))
 * 空间复杂度：O(n*k + m*k)
 */
class LayeredGraphShortestPath {
private:
    // 边类
    struct Edge {
        int to;
        int weight;
        
        Edge(int t, int w) : to(t), weight(w) {}
    };
    
    // 分层图最短路径算法
    static int solve(int n, const vector<vector<Edge>>& edges, int k, int start, int end) {
        // 构建分层图的邻接表
        vector<vector<Edge>> layeredGraph(n * (k + 1));
        int totalNodes = n * (k + 1);
        
        // 添加普通边（不使用特殊操作）
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j <= k; ++j) {
                int fromNode = i + j * n;
                for (const Edge& edge : edges[i]) {
                    layeredGraph[fromNode].emplace_back(edge.to + j * n, edge.weight);
                }
            }
        }
        
        // 添加使用特殊操作的边（如果允许的话）
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < k; ++j) {
                int fromNode = i + j * n;
                for (const Edge& edge : edges[i]) {
                    // 这里假设特殊操作可以免费通行（权为0），具体根据问题调整
                    layeredGraph[fromNode].emplace_back(edge.to + (j + 1) * n, 0);
                }
            }
        }
        
        // Dijkstra算法
        const int INF = INT_MAX;
        vector<int> dist(totalNodes, INF);
        dist[start] = 0; // 起始点在第0层
        
        // 使用优先队列，按距离排序
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
        pq.emplace(0, start);
        
        while (!pq.empty()) {
            auto [currentDist, u] = pq.top();
            pq.pop();
            
            if (currentDist > dist[u]) {
                continue;
            }
            
            for (const Edge& edge : layeredGraph[u]) {
                int v = edge.to;
                int w = edge.weight;
                if (dist[v] > currentDist + w && currentDist != INF) {
                    dist[v] = currentDist + w;
                    pq.emplace(dist[v], v);
                }
            }
        }
        
        // 取所有层中到达终点的最小值
        int result = INF;
        for (int i = 0; i <= k; ++i) {
            if (dist[end + i * n] < result) {
                result = dist[end + i * n];
            }
        }
        
        return (result != INF) ? result : -1;
    }

public:
    // 应用示例：LeetCode 787. K 站中转内最便宜的航班
    static int findCheapestPrice(int n, const vector<vector<int>>& flights, int src, int dst, int k) {
        // 构建图的邻接表
        vector<vector<Edge>> edges(n);
        for (const auto& flight : flights) {
            edges[flight[0]].emplace_back(flight[1], flight[2]);
        }
        
        // 调用分层图算法，注意这里k站中转意味着可以乘坐k+1次航班
        return solve(n, edges, k + 1, src, dst);
    }
};

// ==================== 冷门模型：期望DP遇环的方程组解（高斯消元） ====================
/**
 * 期望DP处理有环情况（使用高斯消元）
 * 
 * 问题描述：
 * 在有环的状态转移图中计算期望
 * 
 * 解题思路：
 * 1. 对于每个状态，建立期望方程
 * 2. 使用高斯消元求解方程组
 * 
 * 应用题目：
 * - LeetCode 837. 新21点
 * - POJ 3744 Scout YYF I
 * - HDU 4405 Aeroplane chess
 * 
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 */
class ExpectationDPWithGaussian {
private:
    // 转移类
    struct Transition {
        int to;
        double probability;
        
        Transition(int t, double p) : to(t), probability(p) {}
    };
    
    // 高斯消元法求解线性方程组
    static vector<double> gaussianElimination(vector<vector<double>> matrix) {
        int n = matrix.size();
        const double eps = 1e-9;
        
        // 高斯消元过程
        for (int i = 0; i < n; ++i) {
            // 找到主元行（当前列中绝对值最大的行）
            int maxRow = i;
            for (int j = i; j < n; ++j) {
                if (abs(matrix[j][i]) > abs(matrix[maxRow][i])) {
                    maxRow = j;
                }
            }
            
            // 交换主元行和当前行
            if (maxRow != i) {
                swap(matrix[i], matrix[maxRow]);
            }
            
            // 如果主元为0，方程组可能有无穷多解或无解
            if (abs(matrix[i][i]) < eps) {
                // 这里简化处理，假设方程组总是有解
                continue;
            }
            
            // 消元过程
            for (int j = 0; j < n; ++j) {
                if (j != i && abs(matrix[j][i]) > eps) {
                    double factor = matrix[j][i] / matrix[i][i];
                    for (int k = i; k <= n; ++k) {
                        matrix[j][k] -= factor * matrix[i][k];
                    }
                }
            }
        }
        
        // 回代求解
        vector<double> x(n);
        for (int i = 0; i < n; ++i) {
            x[i] = matrix[i][n] / matrix[i][i];
        }
        
        return x;
    }
    
    // 期望DP主入口
    static vector<double> solve(int n, const vector<vector<Transition>>& transitions, const vector<double>& cost) {
        // 构建线性方程组的增广矩阵
        vector<vector<double>> matrix(n, vector<double>(n + 1, 0.0));
        
        for (int i = 0; i < n; ++i) {
            matrix[i][i] = 1.0; // 方程左边：E[i] - sum(p_ij * E[j]) = cost[i]
            matrix[i][n] = cost[i];
            
            for (const Transition& t : transitions[i]) {
                if (i != t.to) { // 避免自环的特殊处理
                    matrix[i][t.to] -= t.probability;
                }
            }
        }
        
        // 使用高斯消元求解
        return gaussianElimination(matrix);
    }

public:
    // 应用示例：LeetCode 837. 新21点（简化版本）
    static double new21Game(int N, int K, int W) {
        if (K == 0 || N >= K + W) {
            return 1.0;
        }
        
        int n = K + W;
        vector<vector<Transition>> transitions(n + 1);
        vector<double> cost(n + 1, 0.0);
        
        // 构建转移概率
        for (int i = 0; i < K; ++i) {
            for (int w = 1; w <= W; ++w) {
                int nextState = min(i + w, n);
                transitions[i].emplace_back(nextState, 1.0 / W);
            }
        }
        
        // 终止状态的期望为是否<=N
        for (int i = K; i <= n; ++i) {
            cost[i] = (i <= N) ? 1.0 : 0.0;
            transitions[i].emplace_back(i, 1.0); // 自环
        }
        
        vector<double> result = solve(n + 1, transitions, cost);
        return result[0];
    }
};

// ==================== 冷门模型：插头DP（轮廓线DP）====================
/**
 * 插头DP（轮廓线DP）示例：求网格中哈密顿回路的数量
 * 
 * 问题描述：
 * 给定一个网格，求其中哈密顿回路的数量
 * 
 * 解题思路：
 * 1. 使用轮廓线DP，记录当前处理到的位置和轮廓线状态
 * 2. 插头表示连接的状态，通常用二进制表示
 * 3. 使用哈希表优化空间复杂度
 * 4. 实现合法性判定与对称剪枝
 * 
 * 应用题目：
 * - HDU 1693 Eat the Trees
 * - SPOJ MATCH2 Match the Brackets II
 * - Codeforces 1435F Cyclic Shifts Sorting
 * 
 * 时间复杂度：O(n*m*4^min(n,m))
 * 空间复杂度：O(4^min(n,m))
 */
class PlugDP {
private:
    // 插头DP求解哈密顿回路数量
    static long long solve(const vector<vector<int>>& grid) {
        if (grid.empty() || grid[0].empty()) {
            return 0;
        }
        
        int n = grid.size();
        int m = grid[0].size();
        
        // 使用哈希表优化空间
        unordered_map<long long, long long> dp;
        dp[0] = 1;
        
        for (int i = 0; i < n; ++i) {
            // 新的一行开始，需要将状态左移两位
            unordered_map<long long, long long> newDp;
            for (const auto& [state, cnt] : dp) {
                // 左移两位，移除最左边的插头
                long long newState = state << 2;
                // 移除可能的高位，只保留m*2位
                newState &= (1LL << (2 * m)) - 1;
                newDp[newState] += cnt;
            }
            dp = newDp;
            
            for (int j = 0; j < m; ++j) {
                unordered_map<long long, long long> newDp2;
                
                for (const auto& [state, cnt] : dp) {
                    // 当前位置左边和上边的插头状态
                    int left = (state >> (2 * j)) & 3;
                    int up = (state >> (2 * (j + 1))) & 3;
                    
                    // 如果当前位置是障碍物，跳过
                    if (grid[i][j] == 0) {
                        // 只有当左右插头都不存在时才合法
                        if (left == 0 && up == 0) {
                            newDp2[state] += cnt;
                        }
                        continue;
                    }
                    
                    // 处理各种插头组合情况
                    // 1. 没有左插头和上插头
                    if (left == 0 && up == 0) {
                        // 只能创建新的插头对（用于回路的开始）
                        if (i < n - 1 && j < m - 1 && 
                            grid[i+1][j] == 1 && grid[i][j+1] == 1) {
                            long long newState = state | (1LL << (2 * j)) | (2LL << (2 * (j + 1)));
                            newDp2[newState] += cnt;
                        }
                    }
                    // 2. 只有左插头
                    else if (left != 0 && up == 0) {
                        // 向下延伸
                        if (i < n - 1 && grid[i+1][j] == 1) {
                            newDp2[state] += cnt;
                        }
                        // 向右延伸
                        if (j < m - 1 && grid[i][j+1] == 1) {
                            long long newState = (state & ~(3LL << (2 * j))) | (1LL * left << (2 * (j + 1)));
                            newDp2[newState] += cnt;
                        }
                    }
                    // 3. 只有上插头
                    else if (left == 0 && up != 0) {
                        // 向右延伸
                        if (j < m - 1 && grid[i][j+1] == 1) {
                            newDp2[state] += cnt;
                        }
                        // 向下延伸
                        if (i < n - 1 && grid[i+1][j] == 1) {
                            long long newState = (state & ~(3LL << (2 * (j + 1)))) | (1LL * up << (2 * j));
                            newDp2[newState] += cnt;
                        }
                    }
                    // 4. 同时有左插头和上插头
                    else {
                        // 合并插头
                        long long newState = (state & ~(3LL << (2 * j))) & ~(3LL << (2 * (j + 1)));
                        
                        // 如果是形成回路的最后一步
                        if (left == up) {
                            // 检查是否所有插头都已连接
                            if (newState == 0 && i == n - 1 && j == m - 1) {
                                newDp2[newState] += cnt;
                            }
                        } else {
                            // 合并两个不同的插头
                            // 这里可以加入更多的合法性检查和剪枝
                            newDp2[newState] += cnt;
                        }
                    }
                }
                
                dp = newDp2;
            }
        }
        
        // 最终状态应该是没有任何插头（形成回路）
        return dp.count(0) ? dp[0] : 0;
    }

public:
    // 应用示例：网格中的回路计数
    static long long countGridCycles(const vector<vector<int>>& grid) {
        return solve(grid);
    }
};

// ==================== 冷门模型：树上背包的优化 ====================
/**
 * 树上背包的优化实现（小到大合并）
 * 
 * 问题描述：
 * 在树上选择一些节点，使得总重量不超过容量，且总价值最大
 * 
 * 解题思路：
 * 1. 使用后序遍历处理子树
 * 2. 使用小到大合并的策略优化复杂度
 * 3. 对于每个节点，维护一个容量为capacity的背包
 * 
 * 应用题目：
 * - HDU 1561 The more, The Better
 * - POJ 2063 Investment
 * - Codeforces 1152F2 Neko Rules the Catniverse
 * 
 * 时间复杂度：O(n*capacity^2)，但通过小到大合并可以降低常数
 * 空间复杂度：O(n*capacity)
 */
class TreeKnapsackOptimized {
private:
    vector<vector<int>> dp;
    vector<int> size;
    const vector<vector<int>>* tree;
    const vector<int>* weights;
    const vector<int>* values;
    int capacity;
    int n;
    
    void dfs(int u, int parent) {
        // 初始化当前节点
        size[u] = 1;
        if ((*weights)[u] <= capacity) {
            dp[u][(*weights)[u]] = max(dp[u][(*weights)[u]], (*values)[u]);
        }
        
        // 对每个子节点，按照子树大小排序，小的先合并
        vector<pair<int, int>> children;
        for (int v : (*tree)[u]) {
            if (v != parent) {
                dfs(v, u);
                children.emplace_back(size[v], v);
            }
        }
        
        // 按子树大小排序（小到大）
        sort(children.begin(), children.end());
        
        for (const auto& [sz, v] : children) {
            // 逆序遍历容量，避免重复计算
            for (int i = min(size[u], capacity); i >= 0; --i) {
                if (dp[u][i] == 0 && i != 0) {
                    continue;
                }
                for (int j = 1; j <= min(sz, capacity - i); ++j) {
                    if (dp[v][j] > 0 && i + j <= capacity) {
                        dp[u][i + j] = max(dp[u][i + j], dp[u][i] + dp[v][j]);
                    }
                }
            }
            
            // 更新子树大小
            size[u] += sz;
        }
    }

public:
    // 树上背包主入口
    int solve(int n, int root, int capacity, const vector<vector<int>>& tree, 
              const vector<int>& weights, const vector<int>& values) {
        this->n = n;
        this->capacity = capacity;
        this->tree = &tree;
        this->weights = &weights;
        this->values = &values;
        
        // 初始化dp数组
        dp.assign(n + 1, vector<int>(capacity + 1, 0));
        size.assign(n + 1, 0);
        
        // 深度优先搜索处理子树
        dfs(root, -1);
        
        // 返回根节点的最大价值
        int maxValue = 0;
        for (int i = 0; i <= capacity; ++i) {
            maxValue = max(maxValue, dp[root][i]);
        }
        return maxValue;
    }
    
    // 应用示例：树上最大价值选择
    static int maxTreeValue(int n, int root, int capacity, const vector<vector<int>>& tree,
                           const vector<int>& weights, const vector<int>& values) {
        TreeKnapsackOptimized optimizer;
        return optimizer.solve(n, root, capacity, tree, weights, values);
    }
};

// ==================== 补充题目与应用 ====================
/**
 * LeetCode 72. 编辑距离
 * 题目链接：https://leetcode-cn.com/problems/edit-distance/
 * 
 * 问题描述：
 * 给你两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数。
 * 你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
 * 
 * 时间复杂度：O(m*n)
 * 空间复杂度：O(m*n)
 */
int editDistance(const string& word1, const string& word2) {
    int m = word1.size();
    int n = word2.size();
    // 初始化dp数组
    vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
    
    // 初始化边界条件
    for (int i = 0; i <= m; ++i) {
        dp[i][0] = i;
    }
    for (int j = 0; j <= n; ++j) {
        dp[0][j] = j;
    }
    
    // 填充dp表
    for (int i = 1; i <= m; ++i) {
        for (int j = 1; j <= n; ++j) {
            if (word1[i-1] == word2[j-1]) {
                dp[i][j] = dp[i-1][j-1];
            } else {
                dp[i][j] = min({dp[i-1][j], dp[i][j-1], dp[i-1][j-1]}) + 1;
            }
        }
    }
    
    return dp[m][n];
}

/**
 * LeetCode 300. 最长递增子序列
 * 题目链接：https://leetcode-cn.com/problems/longest-increasing-subsequence/
 * 
 * 问题描述：
 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
int lengthOfLIS(const vector<int>& nums) {
    if (nums.empty()) {
        return 0;
    }
    
    vector<int> tails;
    
    for (int num : nums) {
        // 二分查找tails中第一个大于等于num的位置
        auto it = lower_bound(tails.begin(), tails.end(), num);
        
        if (it == tails.end()) {
            tails.push_back(num);
        } else {
            *it = num;
        }
    }
    
    return tails.size();
}

/**
 * LeetCode 322. 零钱兑换
 * 题目链接：https://leetcode-cn.com/problems/coin-change/
 * 
 * 问题描述：
 * 给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
 * 计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回-1。
 * 
 * 时间复杂度：O(amount * n)
 * 空间复杂度：O(amount)
 */
int coinChange(const vector<int>& coins, int amount) {
    // 初始化dp数组，dp[i]表示凑成金额i所需的最少硬币数
    vector<int> dp(amount + 1, amount + 1);
    dp[0] = 0; // 基础情况
    
    for (int i = 1; i <= amount; ++i) {
        for (int coin : coins) {
            if (coin <= i) {
                dp[i] = min(dp[i], dp[i - coin] + 1);
            }
        }
    }
    
    return dp[amount] <= amount ? dp[amount] : -1;
}

/**
 * 矩阵链乘法问题
 * 题目来源：算法导论、POJ 1038
 * 
 * 问题描述：
 * 给定一系列矩阵，计算乘法顺序使得标量乘法的次数最少。
 * 
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 */
long long matrixChainOrder(const vector<int>& p) {
    int n = p.size() - 1; // 矩阵的个数
    // 初始化dp数组
    vector<vector<long long>> dp(n + 1, vector<long long>(n + 1, 0));
    
    // 枚举子链长度
    for (int length = 2; length <= n; ++length) {
        for (int i = 1; i <= n - length + 1; ++i) {
            int j = i + length - 1;
            dp[i][j] = LLONG_MAX;
            // 枚举分割点
            for (int k = i; k < j; ++k) {
                // 计算在位置k分割的代价
                long long cost = dp[i][k] + dp[k+1][j] + 1LL * p[i-1] * p[k] * p[j];
                if (cost < dp[i][j]) {
                    dp[i][j] = cost;
                }
            }
        }
    }
    
    return dp[1][n];
}

/**
 * 旅行商问题（TSP）
 * 题目来源：算法竞赛、POJ 2480
 * 
 * 问题描述：
 * 给定一个完全图，找到一条访问每个城市恰好一次并返回起点的最短路径。
 * 
 * 时间复杂度：O(n^2 * 2^n)
 * 空间复杂度：O(n * 2^n)
 */
int tsp(const vector<vector<int>>& graph) {
    int n = graph.size();
    // dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径
    vector<vector<int>> dp(1 << n, vector<int>(n, INT_MAX));
    
    // 初始化：从城市0出发
    dp[1][0] = 0;
    
    // 枚举所有可能的状态
    for (int mask = 1; mask < (1 << n); ++mask) {
        for (int u = 0; u < n; ++u) {
            if (!(mask & (1 << u))) { // 如果u不在mask中，跳过
                continue;
            }
            if (dp[mask][u] == INT_MAX) { // 如果无法到达u，跳过
                continue;
            }
            
            // 尝试从u出发访问未访问的城市v
            for (int v = 0; v < n; ++v) {
                if (mask & (1 << v)) { // 如果v已经访问过，跳过
                    continue;
                }
                if (graph[u][v] == INT_MAX) { // 如果u和v之间没有边，跳过
                    continue;
                }
                
                int newMask = mask | (1 << v);
                if (dp[newMask][v] > dp[mask][u] + graph[u][v] && dp[mask][u] != INT_MAX) {
                    dp[newMask][v] = dp[mask][u] + graph[u][v];
                }
            }
        }
    }
    
    // 找到最短的回路
    int result = INT_MAX;
    for (int u = 0; u < n; ++u) {
        if (graph[u][0] != INT_MAX && dp[(1 << n) - 1][u] != INT_MAX) {
            result = min(result, dp[(1 << n) - 1][u] + graph[u][0]);
        }
    }
    
    return result != INT_MAX ? result : -1;
}

// ==================== 石子游戏 ====================
bool stoneGame(const vector<int>& piles) {
    int n = piles.size();
    vector<vector<int>> dp(n, vector<int>(n, 0));
    
    // 初始化单堆石子的情况
    for (int i = 0; i < n; ++i) {
        dp[i][i] = piles[i];
    }
    
    // 枚举区间长度
    for (int length = 2; length <= n; ++length) {
        for (int i = 0; i <= n - length; ++i) {
            int j = i + length - 1;
            // 当前玩家可以选择左端或右端
            dp[i][j] = max(piles[i] - dp[i+1][j], piles[j] - dp[i][j-1]);
        }
    }
    
    return dp[0][n-1] > 0;
}

// ==================== 数字1的个数 ====================
int countDigitOne(int n) {
    long long count = 0;
    long long factor = 1;
    
    while (n / factor != 0) {
        long long lowerNum = n - (n / factor) * factor;
        long long currNum = (n / factor) % 10;
        long long higherNum = n / (factor * 10);
        
        if (currNum == 0) {
            count += higherNum * factor;
        } else if (currNum == 1) {
            count += higherNum * factor + lowerNum + 1;
        } else {
            count += (higherNum + 1) * factor;
        }
        
        factor *= 10;
    }
    
    return count;
}

// ==================== 测试代码 ====================
int main() {
    cout << "高级动态规划算法实现示例\n" << endl;
    cout << "========== 经典DP题目测试 ==========" << endl;
    
    // 测试编辑距离
    cout << "编辑距离测试: " << editDistance("horse", "ros") << endl;
    
    // 测试最长递增子序列
    vector<int> nums = {10, 9, 2, 5, 3, 7, 101, 18};
    cout << "最长递增子序列测试: " << lengthOfLIS(nums) << endl;
    
    // 测试零钱兑换
    vector<int> coins = {1, 2, 5};
    cout << "零钱兑换测试: " << coinChange(coins, 11) << endl;
    
    // 测试石子游戏
    vector<int> piles = {5, 3, 4, 5};
    cout << "石子游戏测试: " << (stoneGame(piles) ? "true" : "false") << endl;
    
    // 测试数字1的个数
    cout << "数字1的个数测试: " << countDigitOne(13) << endl;
    
    // 测试矩阵链乘法
    vector<int> matrixDims = {30, 35, 15, 5, 10, 20, 25};
    cout << "矩阵链乘法最小标量乘法次数: " << matrixChainOrder(matrixDims) << endl;
    
    // 测试旅行商问题（小规模）
    int tspSize = 4;
    vector<vector<int>> tspGraph(tspSize, vector<int>(tspSize, INT_MAX));
    tspGraph[0][1] = 10;
    tspGraph[0][2] = 15;
    tspGraph[0][3] = 20;
    tspGraph[1][0] = 10;
    tspGraph[1][2] = 35;
    tspGraph[1][3] = 25;
    tspGraph[2][0] = 15;
    tspGraph[2][1] = 35;
    tspGraph[2][3] = 30;
    tspGraph[3][0] = 20;
    tspGraph[3][1] = 25;
    tspGraph[3][2] = 30;
    cout << "旅行商问题最短路径: " << tsp(tspGraph) << endl;
    
    cout << "\n========== 高级优化体系测试 ==========" << endl;
    
    // 测试SMAWK算法
    vector<vector<int>> mongeMatrix = {
        {10, 17, 13, 28, 23},
        {17, 22, 16, 29, 23},
        {24, 28, 22, 34, 24},
        {11, 13, 6, 17, 7},
        {17, 18, 14, 24, 18}
    };
    vector<int> smawkResult = SMAWK::solve(mongeMatrix);
    cout << "SMAWK算法每行最小值的列索引: ";
    for (int idx : smawkResult) {
        cout << idx << " ";
    }
    cout << endl;
    
    // 测试Aliens Trick
    vector<int> splitArray = {7, 2, 5, 10, 8};
    int k = 2;
    cout << "Aliens Trick将数组分成" << k << "部分的最小成本: " 
         << AliensTrick::splitArrayK(splitArray, k) << endl;
    
    // 测试分层图最短路径
    vector<vector<int>> flights = {
        {0, 1, 100},
        {1, 2, 100},
        {0, 2, 500}
    };
    int cities = 3;
    int src = 0, dst = 2, maxStops = 1;
    cout << "分层图Dijkstra找到的最便宜航班: " 
         << LayeredGraphShortestPath::findCheapestPrice(cities, flights, src, dst, maxStops) << endl;
    
    // 测试期望DP（新21点）
    int N = 21, K = 17, W = 10;
    cout << "新21点游戏获胜概率: " << ExpectationDPWithGaussian::new21Game(N, K, W) << endl;
    
    // 测试插头DP（网格中的简单回路）
    vector<vector<int>> grid = {
        {1, 1, 1},
        {1, 1, 1},
        {1, 1, 1}
    };
    cout << "3x3网格中哈密顿回路数量: " << PlugDP::countGridCycles(grid) << endl;
    
    // 测试树上背包优化
    int treeNodes = 5;
    int treeRoot = 1;
    int capacity = 4;
    vector<vector<int>> tree(treeNodes + 1);
    tree[1].push_back(2);
    tree[1].push_back(3);
    tree[2].push_back(4);
    tree[2].push_back(5);
    tree[2].push_back(1);
    tree[3].push_back(1);
    tree[4].push_back(2);
    tree[5].push_back(2);
    vector<int> weights = {0, 1, 2, 1, 2, 1}; // 0号元素不用
    vector<int> values = {0, 10, 20, 5, 10, 3};
    cout << "树上背包最大价值: " 
         << TreeKnapsackOptimized::maxTreeValue(treeNodes, treeRoot, capacity, tree, weights, values) << endl;
    
    cout << "\n所有算法测试完成！" << endl;
    return 0;
}