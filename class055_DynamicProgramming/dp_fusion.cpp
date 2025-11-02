// -*- coding: utf-8 -*-
/**
 * DP融合场景：DP+数论、DP+字符串、DP+计算几何
 *
 * 问题描述：
 * 动态规划(DP)可以与其他领域的算法和数据结构结合，形成强大的问题解决方法。
 * 本文件实现了三种主要的融合场景：
 * 1. DP+数论（模意义下的动态规划）
 * 2. DP+字符串（基于后缀自动机的计数）
 * 3. DP+计算几何（凸包上的动态规划）
 *
 * 时间复杂度：
 * - DP+数论：根据具体问题而定，通常为O(n^2)或O(n^3)
 * - DP+字符串：O(n)或O(n^2)
 * - DP+计算几何：O(n^2)或O(n log n)
 *
 * 空间复杂度：O(n^2)
 *
 * 相关题目：
 * 1. LeetCode 518. 零钱兑换 II（模意义）
 * 2. LeetCode 682. 棒球比赛（字符串DP）
 * 3. LeetCode 873. 最长的斐波那契子序列的长度（序列DP）
 */

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <map>
#include <cmath>
#include <climits>
#include <float.h>

using namespace std;

// ==================== DP+数论（模意义）====================

/**
 * LeetCode 518. 零钱兑换 II（模意义下的变种）
 * 题目链接：https://leetcode-cn.com/problems/coin-change-2/
 *
 * 问题描述：
 * 给定不同面额的硬币和一个总金额。计算可以凑成总金额的硬币组合数。
 * 假设每一种面额的硬币有无限个。要求结果对给定的模数取余。
 *
 * 解题思路：
 * 使用动态规划，定义dp[i]表示凑成金额i的组合数。
 * 状态转移方程：dp[i] = (dp[i] + dp[i-coin]) % mod，其中coin是硬币面额。
 *
 * @param amount 总金额
 * @param coins 硬币面额数组
 * @param mod 模数
 * @return 可以凑成总金额的硬币组合数对mod取余的结果
 */
int coinChangeMod(int amount, vector<int>& coins, int mod) {
    vector<long long> dp(amount + 1, 0);
    dp[0] = 1;  // 凑成金额0的方式只有1种（不选任何硬币）
    
    // 遍历每种硬币
    for (int coin : coins) {
        // 遍历金额，从coin开始
        for (int i = coin; i <= amount; ++i) {
            dp[i] = (dp[i] + dp[i - coin]) % mod;
        }
    }
    
    return dp[amount];
}

/**
 * 矩阵乘法（模意义下）
 *
 * @param a 矩阵a
 * @param b 矩阵b
 * @param mod 模数
 * @return 矩阵a和矩阵b的乘积对mod取余的结果
 */
vector<vector<long long>> matrixMultiply(vector<vector<long long>>& a, vector<vector<long long>>& b, int mod) {
    int n = a.size();
    int m = b[0].size();
    int k = b.size();
    vector<vector<long long>> result(n, vector<long long>(m, 0));
    
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < m; ++j) {
            for (int p = 0; p < k; ++p) {
                result[i][j] = (result[i][j] + a[i][p] * b[p][j]) % mod;
            }
        }
    }
    
    return result;
}

/**
 * 矩阵快速幂（模意义下）
 *
 * 问题描述：
 * 计算矩阵的幂，结果对mod取余。
 *
 * 解题思路：
 * 使用快速幂算法，将矩阵的乘法在模意义下进行。
 *
 * @param matrix 输入矩阵
 * @param power 幂次
 * @param mod 模数
 * @return 矩阵的幂对mod取余的结果
 */
vector<vector<long long>> matrixPowerMod(vector<vector<long long>>& matrix, int power, int mod) {
    int n = matrix.size();
    // 初始化结果为单位矩阵
    vector<vector<long long>> result(n, vector<long long>(n, 0));
    for (int i = 0; i < n; ++i) {
        result[i][i] = 1;
    }
    
    // 快速幂算法
    while (power > 0) {
        if (power % 2 == 1) {
            // 矩阵乘法
            result = matrixMultiply(result, matrix, mod);
        }
        // 矩阵自乘
        matrix = matrixMultiply(matrix, matrix, mod);
        power /= 2;
    }
    
    return result;
}

// ==================== DP+字符串（SAM相关）====================

/**
 * LeetCode 516. 最长回文子序列
 * 题目链接：https://leetcode-cn.com/problems/longest-palindromic-subsequence/
 *
 * 问题描述：
 * 给定一个字符串s，找到其中最长的回文子序列。可以假设s的最大长度为1000。
 *
 * 解题思路：
 * 使用区间DP，定义dp[i][j]表示字符串s在区间[i,j]内的最长回文子序列的长度。
 * 状态转移方程：
 * - 如果s[i] == s[j]，则dp[i][j] = dp[i+1][j-1] + 2
 * - 否则，dp[i][j] = max(dp[i+1][j], dp[i][j-1])
 *
 * @param s 输入字符串
 * @return 最长回文子序列的长度
 */
int longestPalindromicSubseq(string s) {
    int n = s.size();
    // dp[i][j]表示字符串s在区间[i,j]内的最长回文子序列的长度
    vector<vector<int>> dp(n, vector<int>(n, 0));
    
    // 初始化单个字符的情况
    for (int i = 0; i < n; ++i) {
        dp[i][i] = 1;
    }
    
    // 枚举区间长度
    for (int length = 2; length <= n; ++length) {
        // 枚举起点
        for (int i = 0; i <= n - length; ++i) {
            int j = i + length - 1;
            if (s[i] == s[j]) {
                dp[i][j] = dp[i+1][j-1] + 2;
            } else {
                dp[i][j] = max(dp[i+1][j], dp[i][j-1]);
            }
        }
    }
    
    return dp[0][n-1];
}

/**
 * 后缀自动机（Suffix Automaton）
 *
 * 后缀自动机是一个可以表示字符串的所有子串的数据结构。
 * 它可以用于解决许多字符串问题，如子串匹配、最长重复子串等。
 */
class SuffixAutomaton {
private:
    struct State {
        int len;  // 该状态能接受的最长字符串的长度
        int link;  // 后缀链接
        map<char, int> next;  // 转移函数
        int endposSize;  // endpos集合的大小
        
        State() : len(0), link(-1), endposSize(0) {}
    };
    
    int size;
    int last;
    vector<State> states;
    
    void extend(char c) {
        int p = last;
        int curr = size;
        size++;
        states.push_back(State());
        states[curr].len = states[p].len + 1;
        
        while (p != -1 && states[p].next.find(c) == states[p].next.end()) {
            states[p].next[c] = curr;
            p = states[p].link;
        }
        
        if (p == -1) {
            states[curr].link = 0;
        } else {
            int q = states[p].next[c];
            if (states[p].len + 1 == states[q].len) {
                states[curr].link = q;
            } else {
                int clone = size;
                size++;
                states.push_back(State());
                states[clone].len = states[p].len + 1;
                states[clone].next = states[q].next;
                states[clone].link = states[q].link;
                
                while (p != -1 && states[p].next.find(c) != states[p].next.end() && states[p].next[c] == q) {
                    states[p].next[c] = clone;
                    p = states[p].link;
                }
                
                states[q].link = clone;
                states[curr].link = clone;
            }
        }
        
        last = curr;
    }
    
    void calcEndposSize() {
        // 按len排序
        vector<int> order(size);
        for (int i = 0; i < size; ++i) {
            order[i] = i;
        }
        sort(order.begin(), order.end(), [this](int a, int b) {
            return states[a].len > states[b].len;
        });
        
        // 初始化为1（每个状态至少对应一个结束位置）
        for (int i = 1; i < size; ++i) {
            states[i].endposSize = 1;
        }
        
        // 从长到短更新
        for (int u : order) {
            if (states[u].link != -1) {
                states[states[u].link].endposSize += states[u].endposSize;
            }
        }
    }
    
public:
    SuffixAutomaton(string s) {
        size = 1;
        last = 0;
        states.push_back(State());
        
        // 构建后缀自动机
        for (char c : s) {
            extend(c);
        }
        
        // 计算endpos集合的大小
        calcEndposSize();
    }
    
    /**
     * 计算不同子串的数量
     */
    int countSubstrings() {
        int count = 0;
        for (int i = 1; i < size; ++i) {
            count += states[i].len - states[states[i].link].len;
        }
        return count;
    }
    
    // ==================== 优化体系：Knuth优化 ====================
    
    // Knuth优化用于优化形如dp[i][j] = min{dp[i][k] + dp[k+1][j]} + w(i,j)的DP
    // 当满足四边形不等式时，最优转移点单调
    
    struct KnuthOptimizationResult {
        vector<vector<int>> dp;
        vector<vector<int>> opt;
        
        KnuthOptimizationResult(const vector<vector<int>>& dp, const vector<vector<int>>& opt)
            : dp(dp), opt(opt) {}
    };
    
    using CostFunction = function<int(int, int)>;
    
    KnuthOptimizationResult knuthOptimization(int n, CostFunction costFunc) {
        /*
        Knuth优化的DP算法
        
        问题描述：
        解决区间DP问题，其中状态转移方程满足四边形不等式
        
        解题思路：
        1. 使用Knuth优化将时间复杂度从O(n^3)降低到O(n^2)
        2. 维护最优转移点数组opt[i][j]，表示计算dp[i][j]时的最优k值
        3. 根据opt[i][j-1] ≤ opt[i][j] ≤ opt[i+1][j]的性质进行剪枝
        
        参数：
            n: 区间长度
            costFunc: 计算区间(i,j)代价的函数
        
        返回：
            KnuthOptimizationResult: 包含dp数组和opt数组的结果类
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        */
        // 初始化dp和opt数组
        vector<vector<int>> dp(n + 1, vector<int>(n + 1));
        vector<vector<int>> opt(n + 1, vector<int>(n + 1));
        
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
                dp[i][j] = INT_MAX;
                // 根据Knuth优化的性质，最优k在opt[i][j-1]到opt[i+1][j]之间
                int upperK = (i + 1 <= j) ? opt[i + 1][j] : j - 1;
                for (int k = opt[i][j - 1]; k <= min(upperK, j - 1); ++k) {
                    if (dp[i][k] != INT_MAX && dp[k + 1][j] != INT_MAX) {
                        int cost = costFunc(i, j);
                        if (cost != INT_MAX) {
                            int current = dp[i][k] + dp[k + 1][j] + cost;
                            if (current < dp[i][j]) {
                                dp[i][j] = current;
                                opt[i][j] = k;
                            }
                        }
                    }
                }
            }
        }
        
        return KnuthOptimizationResult(dp, opt);
    }
    
    // ==================== 优化体系：Divide & Conquer Optimization ====================
    
    void solveDivideConquer(int i, int l, int r, int opt_l, int opt_r, 
                          vector<vector<int>>& dp, CostFunction costFunc) {
        /*
        计算dp[i][l..r]，其中最优转移点在opt_l..opt_r之间
        */
        if (l > r) {
            return;
        }
        
        int mid = (l + r) / 2;
        int best_k = opt_l;
        
        // 在opt_l到min(mid, opt_r)之间寻找最优k
        for (int k = opt_l; k <= min(mid, opt_r); ++k) {
            if (dp[i - 1][k] != INT_MAX) {
                int cost = costFunc(k, mid);
                if (cost != INT_MAX) {
                    int current = dp[i - 1][k] + cost;
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
    
    vector<vector<int>> divideConquerOptimization(int n, int m, CostFunction costFunc) {
        /*
        Divide & Conquer Optimization（分治优化）
        
        问题描述：
        解决形如dp[i][j] = min{dp[i-1][k] + cost(k, j)}，其中k < j
        当转移满足决策单调性时使用
        
        解题思路：
        1. 利用决策单调性，使用分治法优化DP
        2. 对于dp[i][j]，当i固定时，最优转移点k随着j的增加而单调不减
        3. 使用分治的方式计算每个区间的最优决策
        
        参数：
            n: 维度1
            m: 维度2
            costFunc: 计算cost(k,j)的函数
        
        返回：
            vector<vector<int>>: dp数组
        
        时间复杂度：O(n*m log m)
        空间复杂度：O(n*m)
        */
        // 初始化dp数组
        vector<vector<int>> dp(n + 1, vector<int>(m + 1, INT_MAX));
        dp[0][0] = 0;
        
        // 对每个i应用分治优化
        for (int i = 1; i <= n; ++i) {
            solveDivideConquer(i, 1, m, 0, m, dp, costFunc);
        }
        
        return dp;
    }
    
    // ==================== 优化体系：SMAWK算法（行最小查询） ====================
    
    vector<int> reduceRows(const vector<int>& rows, const vector<vector<int>>& matrix) {
        /*行压缩：只保留可能成为最小值的行*/
        vector<int> stack;
        for (int i : rows) {
            while (stack.size() >= 2) {
                int j1 = stack[stack.size() - 2];
                int j2 = stack[stack.size() - 1];
                // 比较两个行在列stack.size()-1处的值
                if (matrix[j1][stack.size() - 1] <= matrix[i][stack.size() - 1]) {
                    break;
                } else {
                    stack.pop_back();
                }
            }
            stack.push_back(i);
        }
        return stack;
    }
    
    vector<int> smawkRec(const vector<int>& rows, const vector<int>& cols, const vector<vector<int>>& matrix) {
        /*递归实现SMAWK算法*/
        if (rows.empty()) {
            return {};
        }
        
        // 行压缩
        vector<int> reducedRows = reduceRows(rows, matrix);
        
        // 递归求解列数为奇数的子问题
        vector<int> halfCols;
        for (int i = 1; i < cols.size(); i += 2) {
            halfCols.push_back(cols[i]);
        }
        vector<int> minCols(reducedRows.size(), -1);
        
        if (!halfCols.empty()) {
            // 递归求解
            vector<int> result = smawkRec(reducedRows, halfCols, matrix);
            // 复制结果
            for (int i = 0; i < result.size(); ++i) {
                minCols[i] = result[i];
            }
        }
        
        // 扩展结果到所有列
        vector<int> result(rows.size(), 0);
        int k = 0;  // minCols的索引
        
        for (int i = 0; i < rows.size(); ++i) {
            int row = rows[i];
            // 确定当前行的最小值可能在哪个区间
            int start = (i == 0) ? 0 : (k > 0 ? minCols[k - 1] : 0);
            int end = (k < minCols.size()) ? minCols[k] : cols.back();
            
            // 在这个区间内查找最小值
            int minVal = INT_MAX;
            int minCol = start;
            
            // 注意这里cols是原始列的子集，需要在cols中遍历
            auto startIt = find(cols.begin(), cols.end(), start);
            auto endIt = find(cols.begin(), cols.end(), end);
            if (startIt != cols.end() && endIt != cols.end()) {
                for (auto it = startIt; it <= endIt; ++it) {
                    int col = *it;
                    if (col < matrix[0].size() && matrix[row][col] < minVal) {
                        minVal = matrix[row][col];
                        minCol = col;
                    }
                }
            }
            
            result[i] = minCol;
            
            // 如果当前行在reducedRows中，且不是最后一行，k前进
            if (k < reducedRows.size() && row == reducedRows[k]) {
                k++;
            }
        }
        
        return result;
    }
    
    vector<int> smawk(const vector<vector<int>>& matrix) {
        /*
        SMAWK算法用于在Monge矩阵中快速查找每行的最小值
        
        问题描述：
        给定一个Monge矩阵，快速找到每行的最小值位置
        
        解题思路：
        1. Monge矩阵满足性质：matrix[i][j] + matrix[i+1][j+1] ≤ matrix[i][j+1] + matrix[i+1][j]
        2. SMAWK算法利用这一性质，可以在O(m+n)时间内找到每行的最小值
        3. 主要步骤包括行压缩和递归求解
        
        参数：
            matrix: 一个Monge矩阵
        
        返回：
            vector<int>: 每行最小值的列索引
        
        时间复杂度：O(m+n)，其中m是行数，n是列数
        空间复杂度：O(m+n)
        */
        int m = matrix.size();
        if (m == 0) {
            return {};
        }
        int n = matrix[0].size();
        
        // 构造行索引和列索引数组
        vector<int> rows, cols;
        for (int i = 0; i < m; ++i) rows.push_back(i);
        for (int i = 0; i < n; ++i) cols.push_back(i);
        
        // 调用递归实现
        vector<int> result = smawkRec(rows, cols, matrix);
        
        return result;
    }
    
    // ==================== 优化体系：Aliens Trick（二分约束参数+可行性DP） ====================
    
    struct AliensTrickResult {
        double lambda;
        double value;
        
        AliensTrickResult(double lambda, double value)
            : lambda(lambda), value(value) {}
    };
    
    using AliensCostFunction = function<pair<double, double>(double)>;
    using CheckFunction = function<bool(double)>;
    
    AliensTrickResult aliensTrick(AliensCostFunction costFunc, CheckFunction checkFunc,
                                double left, double right, double eps) {
        /*
        Aliens Trick（二分约束参数+可行性DP）
        
        问题描述：
        解决带约束的优化问题，通常形如最小化总成本，同时满足某些约束条件
        
        解题思路：
        1. 将约束条件转化为参数λ，构造拉格朗日函数
        2. 对λ进行二分查找，使用可行性DP判断当前λ下是否满足约束
        3. 根据可行性DP的结果调整二分区间
        
        参数：
            costFunc: 计算带参数λ的成本函数，返回pair<double, double>，其中first是当前值，second是约束值
            checkFunc: 检查当前解是否满足约束的函数
            left: 二分左边界
            right: 二分右边界
            eps: 精度要求
        
        返回：
            AliensTrickResult: 包含最优参数λ和对应最优解的结果类
        
        时间复杂度：O(log((right-left)/eps) * T(DP))，其中T(DP)是一次DP的时间复杂度
        */
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
        
        return AliensTrickResult(bestLambda, bestValue);
    }
    
    // 重载，提供默认精度
    AliensTrickResult aliensTrick(AliensCostFunction costFunc, CheckFunction checkFunc,
                                double left, double right) {
        return aliensTrick(costFunc, checkFunc, left, right, 1e-7);
    }
    
    // ==================== 图上DP→最短路：分层图建模 ====================
    
    int layeredGraphDijkstra(int n, int m, const vector<vector<int>>& edges, int k) {
        /*
        分层图Dijkstra算法
        
        问题描述：
        给定一个图，允许最多使用k次特殊操作（如跳跃、免费通行等），求最短路径
        
        解题思路：
        1. 构建分层图，每层代表使用不同次数的特殊操作
        2. 对于每个节点u，在第i层表示到达u时已经使用了i次特殊操作
        3. 使用Dijkstra算法在分层图上寻找最短路径
        
        参数：
            n: 节点数量
            m: 边的数量
            edges: 边的列表，每个元素为[u, v, w]表示u到v的权为w的边
            k: 允许使用的特殊操作次数
        
        返回：
            int: 从节点0到节点n-1的最短路径长度
        
        时间复杂度：O((n*k + m*k) log(n*k))
        空间复杂度：O(n*k + m*k)
        */
        // 构建分层图的邻接表
        vector<vector<pair<int, int>>> graph(n * (k + 1));
        
        // 添加普通边（不使用特殊操作）
        for (const auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            for (int i = 0; i <= k; ++i) {
                graph[u + i * n].emplace_back(v + i * n, w);
            }
        }
        
        // 添加使用特殊操作的边（如果允许的话）
        for (const auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            for (int i = 0; i < k; ++i) {
                // 这里假设特殊操作可以免费通行（权为0），具体根据问题调整
                graph[u + i * n].emplace_back(v + (i + 1) * n, 0);
            }
        }
        
        // Dijkstra算法
        vector<int> dist(n * (k + 1), INT_MAX);
        dist[0] = 0;  // 假设起点是节点0
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<>> heap;
        heap.emplace(0, 0);  // (距离, 节点)
        
        while (!heap.empty()) {
            auto [d, u] = heap.top();
            heap.pop();
            
            if (d > dist[u]) {
                continue;
            }
            
            for (const auto& [v, w] : graph[u]) {
                if (dist[v] > d + w) {
                    dist[v] = d + w;
                    heap.emplace(dist[v], v);
                }
            }
        }
        
        // 取所有层中到达终点的最小值
        int result = INT_MAX;
        for (int i = 0; i <= k; ++i) {
            if (dist[n - 1 + i * n] < result) {
                result = dist[n - 1 + i * n];
            }
        }
        
        return result;
    }
    
    // ==================== 冷门模型：期望DP遇环的方程组解（高斯消元） ====================
    
    vector<double> gaussianElimination(vector<vector<double>> matrix) {
        /*
        高斯消元法求解线性方程组
        
        问题描述：
        求解形如Ax = b的线性方程组
        
        解题思路：
        1. 构建增广矩阵
        2. 进行高斯消元，将矩阵转化为行阶梯形
        3. 回代求解
        
        参数：
            matrix: 增广矩阵，每行最后一个元素是b的值
        
        返回：
            vector<double>: 方程组的解
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
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
            if (abs(matrix[i][i]) < eps) {
                continue;
            }
            
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
    
    vector<double> expectationDPWithCycles(int n, const vector<vector<pair<int, double>>>& transitions) {
        /*
        期望DP处理有环情况（使用高斯消元）
        
        问题描述：
        在有环的状态转移图中计算期望
        
        解题思路：
        1. 对于每个状态，建立期望方程
        2. 使用高斯消元求解方程组
        
        参数：
            n: 状态数量
            transitions: 转移概率列表，transitions[i]是一个列表，每个元素为[j, p]表示从i转移到j的概率为p
        
        返回：
            vector<double>: 每个状态的期望值
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
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
    
    int plugDP(const vector<vector<int>>& grid) {
        /*
        插头DP（轮廓线DP）示例：求网格中哈密顿回路的数量
        
        问题描述：
        给定一个网格，求其中哈密顿回路的数量
        
        解题思路：
        1. 使用轮廓线DP，记录当前处理到的位置和轮廓线状态
        2. 插头表示连接的状态，通常用二进制表示
        3. 使用哈希表优化空间复杂度
        
        参数：
            grid: 网格，1表示可通行，0表示障碍物
        
        返回：
            int: 哈密顿回路的数量
        
        时间复杂度：O(n*m*4^min(n,m))
        空间复杂度：O(4^min(n,m))
        */
        int n = grid.size();
        if (n == 0) {
            return 0;
        }
        int m = grid[0].size();
        
        // 使用哈希表优化
        unordered_map<long long, int> dp;
        
        // 初始状态：左上角没有插头
        dp[0] = 1;
        
        for (int i = 0; i < n; ++i) {
            // 新的一行开始，需要将状态左移一位
            unordered_map<long long, int> newDp;
            for (const auto& [state, cnt] : dp) {
                // 左移一位，移除最左边的插头
                long long newState = state << 1;
                newDp[newState] += cnt;
            }
            dp = move(newDp);
            
            for (int j = 0; j < m; ++j) {
                unordered_map<long long, int> newDp2;
                
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
                            long long newState = (state & ~(3LL << (2 * j))) | (left << (2 * (j + 1)));
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
                            long long newState = (state & ~(3LL << (2 * (j + 1)))) | (up << (2 * j));
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
                
                dp = move(newDp2);
            }
        }
        
        // 最终状态应该是没有任何插头（形成回路）
        return dp.count(0) ? dp[0] : 0;
    }
    
    // ==================== 冷门模型：树上背包的优化 ====================
    
    void dfsTreeKnapsack(int u, int parent, int capacity, 
                       const vector<vector<int>>& tree, const vector<int>& weights, 
                       const vector<int>& values, vector<vector<int>>& dp, vector<int>& size) {
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
    
    int treeKnapsackOptimized(int root, int capacity, const vector<vector<int>>& tree, 
                            const vector<int>& weights, const vector<int>& values) {
        /*
        树上背包的优化实现（小到大合并）
        
        问题描述：
        在树上选择一些节点，使得总重量不超过容量，且总价值最大
        
        解题思路：
        1. 使用后序遍历处理子树
        2. 使用小到大合并的策略优化复杂度
        3. 对于每个节点，维护一个容量为capacity的背包
        
        参数：
            root: 根节点
            capacity: 背包容量
            tree: 树的邻接表
            weights: 每个节点的重量
            values: 每个节点的价值
        
        返回：
            int: 最大价值
        
        时间复杂度：O(n*capacity^2)，但通过小到大合并可以降低常数
        空间复杂度：O(n*capacity)
        */
        int n = tree.size();
        vector<vector<int>> dp(n, vector<int>(capacity + 1, 0));
        vector<int> size(n, 0);
        
        // 深度优先搜索处理子树
        dfsTreeKnapsack(root, -1, capacity, tree, weights, values, dp, size);
        
        // 返回根节点的最大价值
        int maxVal = 0;
        for (int val : dp[root]) {
            maxVal = max(maxVal, val);
        }
        return maxVal;
    }
    
    // ==================== 补充题目与应用 ====================
    // 以下是一些使用上述高级DP技术的经典题目及其代码实现
    
    // 1. 编辑距离问题（LeetCode 72）
    int editDistance(const string& word1, const string& word2) {
        /*
        LeetCode 72. 编辑距离
        题目链接：https://leetcode-cn.com/problems/edit-distance/
        
        问题描述：
        给你两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数。
        你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
        
        解题思路：
        使用二维DP，dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数。
        
        时间复杂度：O(m*n)
        空间复杂度：O(m*n)
        */
        int m = word1.size();
        int n = word2.size();
        // dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数
        vector<vector<int>> dp(m + 1, vector<int>(n + 1));
        
        // 初始化边界
        for (int i = 0; i <= m; ++i) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; ++j) {
            dp[0][j] = j;
        }
        
        // 动态规划填表
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (word1[i - 1] == word2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = min({dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]}) + 1;
                }
            }
        }
        
        return dp[m][n];
    }
    
    // 2. 最长递增子序列（LeetCode 300）
    int lengthOfLIS(const vector<int>& nums) {
        /*
        LeetCode 300. 最长递增子序列
        题目链接：https://leetcode-cn.com/problems/longest-increasing-subsequence/
        
        问题描述：
        给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
        
        解题思路：
        使用贪心 + 二分查找优化的DP方法。
        tails[i]表示长度为i+1的递增子序列的末尾元素的最小值。
        
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        */
        if (nums.empty()) {
            return 0;
        }
        
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
    
    // 3. 背包问题变种 - 完全背包（LeetCode 322）
    int coinChange(const vector<int>& coins, int amount) {
        /*
        LeetCode 322. 零钱兑换
        题目链接：https://leetcode-cn.com/problems/coin-change/
        
        问题描述：
        给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
        计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回-1。
        
        解题思路：
        使用完全背包的思想，dp[i]表示凑成金额i所需的最少硬币数。
        
        时间复杂度：O(amount * n)
        空间复杂度：O(amount)
        */
        // 初始化dp数组为无穷大
        vector<int> dp(amount + 1, INT_MAX);
        dp[0] = 0;  // 凑成金额0需要0个硬币
        
        for (int coin : coins) {
            for (int i = coin; i <= amount; ++i) {
                if (dp[i - coin] != INT_MAX) {
                    dp[i] = min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] == INT_MAX ? -1 : dp[amount];
    }
    
    // 4. 矩阵链乘法（区间DP的经典应用）
    struct MatrixChainResult {
        vector<vector<int>> dp;
        vector<vector<int>> s;
        
        MatrixChainResult(const vector<vector<int>>& dp, const vector<vector<int>>& s)
            : dp(dp), s(s) {}
    };
    
    MatrixChainResult matrixChainOrder(const vector<int>& p) {
        /*
        矩阵链乘法问题
        题目来源：算法导论
        
        问题描述：
        给定一系列矩阵，计算乘法顺序使得标量乘法的次数最少。
        
        解题思路：
        使用区间DP，dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数。
        可以使用Knuth优化进一步降低时间复杂度。
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = p.size() - 1;  // 矩阵的个数
        // dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数
        vector<vector<int>> dp(n + 1, vector<int>(n + 1));
        // s[i][j]记录最优分割点
        vector<vector<int>> s(n + 1, vector<int>(n + 1));
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            for (int i = 1; i + length - 1 <= n; ++i) {
                int j = i + length - 1;
                dp[i][j] = INT_MAX;
                // 枚举分割点
                for (int k = i; k < j; ++k) {
                    // 计算当前分割点的代价
                    int cost = dp[i][k] + dp[k + 1][j] + p[i - 1] * p[k] * p[j];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        s[i][j] = k;
                    }
                }
            }
        }
        
        return MatrixChainResult(dp, s);
    }
    
    // 5. 旅行商问题（TSP）的DP实现
    int travelingSalesmanProblem(const vector<vector<int>>& graph) {
        /*
        旅行商问题
        题目来源：算法竞赛经典问题
        
        问题描述：
        给定一个完全图，找到一条访问每个城市恰好一次并返回起点的最短路径。
        
        解题思路：
        使用状态压缩DP，dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度。
        
        时间复杂度：O(n^2 * 2^n)
        空间复杂度：O(n * 2^n)
        */
        int n = graph.size();
        // dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度
        vector<vector<int>> dp(1 << n, vector<int>(n, INT_MAX));
        
        // 初始状态：只访问了起点，路径长度为0
        for (int i = 0; i < n; ++i) {
            dp[1 << i][i] = 0;
        }
        
        // 枚举所有可能的状态
        for (int mask = 1; mask < (1 << n); ++mask) {
            // 枚举当前所在的城市
            for (int u = 0; u < n; ++u) {
                if (!(mask & (1 << u))) {
                    continue;
                }
                // 枚举下一个要访问的城市
                for (int v = 0; v < n; ++v) {
                    if (mask & (1 << v)) {
                        continue;
                    }
                    int newMask = mask | (1 << v);
                    if (dp[mask][u] != INT_MAX && graph[u][v] != INT_MAX) {
                        dp[newMask][v] = min(dp[newMask][v], dp[mask][u] + graph[u][v]);
                    }
                }
            }
        }
        
        // 找到最短的回路
        int result = INT_MAX;
        for (int u = 0; u < n; ++u) {
            if (dp[(1 << n) - 1][u] != INT_MAX && graph[u][0] != INT_MAX) {
                result = min(result, dp[(1 << n) - 1][u] + graph[u][0]);
            }
        }
        
        return result;
    }
    
    // 6. 区间DP：最优三角剖分
    int minimumScoreTriangulation(vector<int>& values) {
        /*
        LeetCode 1039. 多边形三角剖分的最低得分
        题目链接：https://leetcode-cn.com/problems/minimum-score-triangulation-of-polygon/
        
        问题描述：
        给定一个凸多边形，将其三角剖分，使得所有三角形的顶点乘积之和最小。
        
        解题思路：
        使用区间DP，dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分。
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = values.size();
        // dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分
        vector<vector<int>> dp(n, vector<int>(n));
        
        // 枚举区间长度
        for (int length = 3; length <= n; ++length) {
            for (int i = 0; i + length - 1 < n; ++i) {
                int j = i + length - 1;
                dp[i][j] = INT_MAX;
                // 枚举中间点
                for (int k = i + 1; k < j; ++k) {
                    dp[i][j] = min(dp[i][j], 
                                  dp[i][k] + dp[k][j] + values[i] * values[k] * values[j]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    // 7. 博弈DP：石子游戏
    bool stoneGame(vector<int>& piles) {
        /*
        LeetCode 877. 石子游戏
        题目链接：https://leetcode-cn.com/problems/stone-game/
        
        问题描述：
        给定一个表示石子堆的数组，两个玩家轮流从两端取石子，每次只能取一个，取到最后一个石子的人获胜。
        判断先手是否必胜。
        
        解题思路：
        使用区间DP，dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分。
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        */
        int n = piles.size();
        // dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分
        vector<vector<int>> dp(n, vector<int>(n));
        
        // 初始化单个石子堆
        for (int i = 0; i < n; ++i) {
            dp[i][i] = piles[i];
        }
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            for (int i = 0; i + length - 1 < n; ++i) {
                int j = i + length - 1;
                // 先手可以选择取左边或右边
                dp[i][j] = max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
            }
        }
        
        // 先手净胜分大于0则必胜
        return dp[0][n - 1] > 0;
    }
    
    // 8. 数位DP：统计1出现的次数
    int countDigitOne(int n) {
        /*
        LeetCode 233. 数字1的个数
        题目链接：https://leetcode-cn.com/problems/number-of-digit-one/
        
        问题描述：
        给定一个整数 n，计算所有小于等于 n 的非负整数中数字1出现的个数。
        
        解题思路：
        使用数位DP，逐位处理每一位上1出现的次数。
        
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        */
        if (n <= 0) {
            return 0;
        }
        
        string s = to_string(n);
        int length = s.size();
        int count = 0;
        
        // 逐位处理
        for (int i = 0; i < length; ++i) {
            long long high = 0;
            if (i > 0) {
                high = stoll(s.substr(0, i));
            }
            int current = s[i] - '0';
            long long low = 0;
            if (i < length - 1) {
                low = stoll(s.substr(i + 1));
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
    
    // 9. 树形DP：打家劫舍III
    struct TreeNode {
        int val;
        TreeNode *left;
        TreeNode *right;
        TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    };
    
    pair<int, int> robDFS(TreeNode* node) {
        if (!node) {
            return {0, 0};
        }
        
        auto left = robDFS(node->left);
        auto right = robDFS(node->right);
        
        // 偷当前节点，不能偷子节点
        int robCurrent = node->val + left.second + right.second;
        // 不偷当前节点，可以选择偷或不偷子节点
        int notRobCurrent = max(left.first, left.second) + max(right.first, right.second);
        
        return {robCurrent, notRobCurrent};
    }
    
    int rob(TreeNode* root) {
        /*
        LeetCode 337. 打家劫舍 III
        题目链接：https://leetcode-cn.com/problems/house-robber-iii/
        
        问题描述：
        在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。
        这个地区只有一个入口，我们称之为“根”。除了“根”之外，每栋房子有且只有一个“父“房子与之相连。
        一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。
        如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
        计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
        
        解题思路：
        使用树形DP，对于每个节点，维护两个状态：偷或不偷。
        
        时间复杂度：O(n)
        空间复杂度：O(h)，h为树的高度
        */
        auto [robRoot, notRobRoot] = robDFS(root);
        return max(robRoot, notRobRoot);
    }
    
    // 10. 状态压缩DP：蒙斯特曼问题
    int monsterGame(const vector<vector<int>>& grid) {
        /*
        蒙斯特曼问题
        题目来源：算法竞赛问题
        
        问题描述：
        在网格中放置怪物，使得任何两个怪物都不在同一行、同一列或对角线上。
        
        解题思路：
        使用状态压缩DP，dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数。
        
        时间复杂度：O(n * 2^n)
        空间复杂度：O(2^n)
        */
        int n = grid.size();
        // dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数
        vector<long long> dp(1 << n, 0);
        dp[0] = 1;
        
        for (int i = 0; i < n; ++i) {
            vector<long long> newDp(1 << n, 0);
            for (int mask = 0; mask < (1 << n); ++mask) {
                if (dp[mask] == 0) {
                    continue;
                }
                // 枚举所有可能的放置位置
                for (int j = 0; j < n; ++j) {
                    // 检查是否可以在(i,j)放置怪物
                    if (!(mask & (1 << j)) && grid[i][j] == 1) {
                        // 检查对角线
                        bool valid = true;
                        for (int k = 0; k < i; ++k) {
                            if ((mask & (1 << k)) && abs(k - j) == i - k) {
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
            dp = move(newDp);
        }
        
        return (int) dp[(1 << n) - 1];
    }
    
    // 11. 高维DP：三维背包
    int threeDimensionKnapsack(int n, const vector<int>& capacity, const vector<vector<int>>& items) {
        /*
        三维背包问题
        题目来源：算法竞赛问题
        
        问题描述：
        有n个物品，每个物品有体积、重量、价值三个属性，背包有体积和重量两个限制，求最大价值。
        
        解题思路：
        使用三维DP，dp[i][j][k]表示前i个物品，体积为j，重量为k时的最大价值。
        
        时间复杂度：O(n * V * W)
        空间复杂度：O(n * V * W)
        */
        int V = capacity[0];
        int W = capacity[1];
        // 初始化dp数组
        vector<vector<vector<int>>> dp(n + 1, vector<vector<int>>(V + 1, vector<int>(W + 1, 0)));
        
        for (int i = 1; i <= n; ++i) {
            int v = items[i-1][0];
            int w = items[i-1][1];
            int val = items[i-1][2];
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
    
    // 12. 斜率优化DP示例
    class ConvexHullTrick {
    public:
        /*
        凸包优化技巧示例
        题目来源：算法竞赛问题
        
        问题描述：
        当状态转移方程形如dp[i] = min{dp[j] + a[i] * b[j] + c}时，可以使用凸包优化。
        
        解题思路：
        将转移方程转换为直线的形式，维护凸包以快速查询最小值。
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        */
        
        struct Line {
            long long k, b;
            Line(long long k, long long b) : k(k), b(b) {}
        };
        
        deque<Line> dq;
        
        // 添加一条直线y = kx + b
        void addLine(long long k, long long b) {
            // 当队列中至少有两条直线时，检查是否需要删除末尾的直线
            while (dq.size() >= 2) {
                Line l1 = dq[dq.size() - 2];
                Line l2 = dq[dq.size() - 1];
                // 判断直线l1和l2的交点是否在l2和新直线的交点右侧
                if ((l2.b - l1.b) * (k - l2.k) >= (b - l2.b) * (l2.k - l1.k)) {
                    dq.pop_back();
                } else {
                    break;
                }
            }
            dq.emplace_back(k, b);
        }
        
        // 查询x处的最小值
        long long query(long long x) {
            // 如果队列中至少有两条直线，且第一条直线在x处的值大于第二条，删除第一条
            while (dq.size() >= 2) {
                Line l1 = dq[0];
                Line l2 = dq[1];
                if (l1.k * x + l1.b >= l2.k * x + l2.b) {
                    dq.pop_front();
                } else {
                    break;
                }
            }
            if (dq.empty()) {
                return LLONG_MAX;
            }
            Line l = dq[0];
            return l.k * x + l.b;
        }
    };
};;

// ==================== DP+计算几何（凸包相关）====================

/**
 * 点结构体
 */
struct Point {
    double x, y;
    Point(double x = 0, double y = 0) : x(x), y(y) {}
    bool operator < (const Point& p) const {
        return x < p.x || (x == p.x && y < p.y);
    }
    
    // ==================== 优化体系：Knuth优化 ====================
    
    // Knuth优化用于优化形如dp[i][j] = min{dp[i][k] + dp[k+1][j]} + w(i,j)的DP
    // 当满足四边形不等式时，最优转移点单调
    
    struct KnuthOptimizationResult {
        vector<vector<int>> dp;
        vector<vector<int>> opt;
        
        KnuthOptimizationResult(const vector<vector<int>>& dp, const vector<vector<int>>& opt)
            : dp(dp), opt(opt) {}
    };
    
    using CostFunction = function<int(int, int)>;
    
    KnuthOptimizationResult knuthOptimization(int n, CostFunction costFunc) {
        /*
        Knuth优化的DP算法
        
        问题描述：
        解决区间DP问题，其中状态转移方程满足四边形不等式
        
        解题思路：
        1. 使用Knuth优化将时间复杂度从O(n^3)降低到O(n^2)
        2. 维护最优转移点数组opt[i][j]，表示计算dp[i][j]时的最优k值
        3. 根据opt[i][j-1] ≤ opt[i][j] ≤ opt[i+1][j]的性质进行剪枝
        
        参数：
            n: 区间长度
            costFunc: 计算区间(i,j)代价的函数
        
        返回：
            KnuthOptimizationResult: 包含dp数组和opt数组的结果类
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        */
        // 初始化dp和opt数组
        vector<vector<int>> dp(n + 1, vector<int>(n + 1));
        vector<vector<int>> opt(n + 1, vector<int>(n + 1));
        
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
                dp[i][j] = INT_MAX;
                // 根据Knuth优化的性质，最优k在opt[i][j-1]到opt[i+1][j]之间
                int upperK = (i + 1 <= j) ? opt[i + 1][j] : j - 1;
                for (int k = opt[i][j - 1]; k <= min(upperK, j - 1); ++k) {
                    if (dp[i][k] != INT_MAX && dp[k + 1][j] != INT_MAX) {
                        int cost = costFunc(i, j);
                        if (cost != INT_MAX) {
                            int current = dp[i][k] + dp[k + 1][j] + cost;
                            if (current < dp[i][j]) {
                                dp[i][j] = current;
                                opt[i][j] = k;
                            }
                        }
                    }
                }
            }
        }
        
        return KnuthOptimizationResult(dp, opt);
    }
    
    // ==================== 优化体系：Divide & Conquer Optimization ====================
    
    void solveDivideConquer(int i, int l, int r, int opt_l, int opt_r, 
                           vector<vector<int>>& dp, CostFunction costFunc) {
        /*
        计算dp[i][l..r]，其中最优转移点在opt_l..opt_r之间
        */
        if (l > r) {
            return;
        }
        
        int mid = (l + r) / 2;
        int best_k = opt_l;
        
        // 在opt_l到min(mid, opt_r)之间寻找最优k
        for (int k = opt_l; k <= min(mid, opt_r); ++k) {
            if (dp[i - 1][k] != INT_MAX) {
                int cost = costFunc(k, mid);
                if (cost != INT_MAX) {
                    int current = dp[i - 1][k] + cost;
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
    
    vector<vector<int>> divideConquerOptimization(int n, int m, CostFunction costFunc) {
        /*
        Divide & Conquer Optimization（分治优化）
        
        问题描述：
        解决形如dp[i][j] = min{dp[i-1][k] + cost(k, j)}，其中k < j
        当转移满足决策单调性时使用
        
        解题思路：
        1. 利用决策单调性，使用分治法优化DP
        2. 对于dp[i][j]，当i固定时，最优转移点k随着j的增加而单调不减
        3. 使用分治的方式计算每个区间的最优决策
        
        参数：
            n: 维度1
            m: 维度2
            costFunc: 计算cost(k,j)的函数
        
        返回：
            vector<vector<int>>: dp数组
        
        时间复杂度：O(n*m log m)
        空间复杂度：O(n*m)
        */
        // 初始化dp数组
        vector<vector<int>> dp(n + 1, vector<int>(m + 1, INT_MAX));
        dp[0][0] = 0;
        
        // 对每个i应用分治优化
        for (int i = 1; i <= n; ++i) {
            solveDivideConquer(i, 1, m, 0, m, dp, costFunc);
        }
        
        return dp;
    }
    
    // ==================== 优化体系：SMAWK算法（行最小查询） ====================
    
    vector<int> reduceRows(const vector<int>& rows, const vector<vector<int>>& matrix) {
        /*行压缩：只保留可能成为最小值的行*/
        stack<int> stk;
        for (int i : rows) {
            while (stk.size() >= 2) {
                int j1 = stk.top();
                stk.pop();
                int j2 = stk.top();
                stk.push(j1); // 恢复栈状态
                
                // 比较两个行在列stk.size()-1处的值
                if (matrix[j2][stk.size() - 1] <= matrix[i][stk.size() - 1]) {
                    break;
                } else {
                    stk.pop();
                }
            }
            stk.push(i);
        }
        
        vector<int> result;
        while (!stk.empty()) {
            result.push_back(stk.top());
            stk.pop();
        }
        reverse(result.begin(), result.end());
        return result;
    }
    
    vector<int> smawkRec(const vector<int>& rows, const vector<int>& cols, const vector<vector<int>>& matrix) {
        /*递归实现SMAWK算法*/
        if (rows.empty()) {
            return {};
        }
        
        // 行压缩
        vector<int> reducedRows = reduceRows(rows, matrix);
        
        // 递归求解列数为奇数的子问题
        vector<int> halfCols;
        for (int i = 1; i < cols.size(); i += 2) {
            halfCols.push_back(cols[i]);
        }
        vector<int> minCols(reducedRows.size(), -1);
        
        if (!halfCols.empty()) {
            // 递归求解
            vector<int> result = smawkRec(reducedRows, halfCols, matrix);
            // 复制结果
            for (int i = 0; i < result.size(); ++i) {
                minCols[i] = result[i];
            }
        }
        
        // 扩展结果到所有列
        vector<int> result(rows.size(), 0);
        int k = 0;  // minCols的索引
        
        for (int i = 0; i < rows.size(); ++i) {
            int row = rows[i];
            // 确定当前行的最小值可能在哪个区间
            int start = (i == 0) ? 0 : (k > 0 ? minCols[k - 1] : 0);
            int end = (k < minCols.size()) ? minCols[k] : cols.back();
            
            // 在这个区间内查找最小值
            int minVal = INT_MAX;
            int minCol = start;
            
            // 注意这里cols是原始列的子集，需要在cols中遍历
            auto startIt = find(cols.begin(), cols.end(), start);
            auto endIt = find(cols.begin(), cols.end(), end);
            if (startIt != cols.end() && endIt != cols.end()) {
                for (auto it = startIt; it <= endIt; ++it) {
                    int col = *it;
                    if (col < matrix[0].size() && matrix[row][col] < minVal) {
                        minVal = matrix[row][col];
                        minCol = col;
                    }
                }
            }
            
            result[i] = minCol;
            
            // 如果当前行在reducedRows中，且不是最后一行，k前进
            if (k < reducedRows.size() && row == reducedRows[k]) {
                k++;
            }
        }
        
        return result;
    }
    
    vector<int> smawk(const vector<vector<int>>& matrix) {
        /*
        SMAWK算法用于在Monge矩阵中快速查找每行的最小值
        
        问题描述：
        给定一个Monge矩阵，快速找到每行的最小值位置
        
        解题思路：
        1. Monge矩阵满足性质：matrix[i][j] + matrix[i+1][j+1] ≤ matrix[i][j+1] + matrix[i+1][j]
        2. SMAWK算法利用这一性质，可以在O(m+n)时间内找到每行的最小值
        3. 主要步骤包括行压缩和递归求解
        
        参数：
            matrix: 一个Monge矩阵
        
        返回：
            vector<int>: 每行最小值的列索引
        
        时间复杂度：O(m+n)，其中m是行数，n是列数
        空间复杂度：O(m+n)
        */
        int m = matrix.size();
        if (m == 0) {
            return {};
        }
        int n = matrix[0].size();
        
        // 构造行索引和列索引数组
        vector<int> rows(m), cols(n);
        iota(rows.begin(), rows.end(), 0);
        iota(cols.begin(), cols.end(), 0);
        
        // 调用递归实现
        vector<int> resultList = smawkRec(rows, cols, matrix);
        
        return resultList;
    }
    
    // ==================== 优化体系：Aliens Trick（二分约束参数+可行性DP） ====================
    
    struct AliensTrickResult {
        double lambda;
        double value;
        
        AliensTrickResult(double lambda, double value)
            : lambda(lambda), value(value) {}
    };
    
    using AliensCostFunction = function<vector<double>(double)>; // 返回[value, constraint]
    using CheckFunction = function<bool(double)>;
    
    AliensTrickResult aliensTrick(AliensCostFunction costFunc, CheckFunction checkFunc,
                                 double left, double right, double eps = 1e-7) {
        /*
        Aliens Trick（二分约束参数+可行性DP）
        
        问题描述：
        解决带约束的优化问题，通常形如最小化总成本，同时满足某些约束条件
        
        解题思路：
        1. 将约束条件转化为参数λ，构造拉格朗日函数
        2. 对λ进行二分查找，使用可行性DP判断当前λ下是否满足约束
        3. 根据可行性DP的结果调整二分区间
        
        参数：
            costFunc: 计算带参数λ的成本函数，返回[value, constraint]数组
            checkFunc: 检查当前解是否满足约束的函数
            left: 二分左边界
            right: 二分右边界
            eps: 精度要求
        
        返回：
            AliensTrickResult: 包含最优参数λ和对应最优解的结果类
        
        时间复杂度：O(log((right-left)/eps) * T(DP))，其中T(DP)是一次DP的时间复杂度
        */
        double bestLambda = left;
        double bestValue = 0.0;
        
        while (right - left > eps) {
            double mid = (left + right) / 2;
            // 计算当前参数下的解和约束值
            vector<double> result = costFunc(mid);
            double currentValue = result[0];
            double constraintValue = result[1];
            
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
        
        return AliensTrickResult(bestLambda, bestValue);
    }
    
    // ==================== 图上DP→最短路：分层图建模 ====================
    
    int layeredGraphDijkstra(int n, int m, vector<vector<int>>& edges, int k) {
        /*
        分层图Dijkstra算法
        
        问题描述：
        给定一个图，允许最多使用k次特殊操作（如跳跃、免费通行等），求最短路径
        
        解题思路：
        1. 构建分层图，每层代表使用不同次数的特殊操作
        2. 对于每个节点u，在第i层表示到达u时已经使用了i次特殊操作
        3. 使用Dijkstra算法在分层图上寻找最短路径
        
        参数：
            n: 节点数量
            m: 边的数量
            edges: 边的列表，每个元素为[u, v, w]表示u到v的权为w的边
            k: 允许使用的特殊操作次数
        
        返回：
            int: 从节点0到节点n-1的最短路径长度
        
        时间复杂度：O((n*k + m*k) log(n*k))
        空间复杂度：O(n*k + m*k)
        */
        // 构建分层图的邻接表
        vector<vector<vector<int>>> graph(n * (k + 1));
        
        // 添加普通边（不使用特殊操作）
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            for (int i = 0; i <= k; ++i) {
                int from = u + i * n;
                graph[from].push_back({v + i * n, w});
            }
        }
        
        // 添加使用特殊操作的边（如果允许的话）
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            for (int i = 0; i < k; ++i) {
                // 这里假设特殊操作可以免费通行（权为0），具体根据问题调整
                int from = u + i * n;
                graph[from].push_back({v + (i + 1) * n, 0});
            }
        }
        
        // Dijkstra算法
        vector<int> dist(n * (k + 1), INT_MAX);
        dist[0] = 0;  // 假设起点是节点0
        // 使用优先队列，按距离排序
        using PII = pair<int, int>; // (距离, 节点)
        priority_queue<PII, vector<PII>, greater<PII>> heap;
        heap.emplace(0, 0);
        
        while (!heap.empty()) {
            auto [d, u] = heap.top();
            heap.pop();
            
            if (d > dist[u]) {
                continue;
            }
            
            for (auto& edge : graph[u]) {
                int v = edge[0];
                int w = edge[1];
                if (dist[v] > d + w) {
                    dist[v] = d + w;
                    heap.emplace(dist[v], v);
                }
            }
        }
        
        // 取所有层中到达终点的最小值
        int result = INT_MAX;
        for (int i = 0; i <= k; ++i) {
            result = min(result, dist[n - 1 + i * n]);
        }
        
        return result;
    }
    
    // ==================== 冷门模型：期望DP遇环的方程组解（高斯消元） ====================
    
    vector<double> gaussianElimination(vector<vector<double>> matrix) {
        /*
        高斯消元法求解线性方程组
        
        问题描述：
        求解形如Ax = b的线性方程组
        
        解题思路：
        1. 构建增广矩阵
        2. 进行高斯消元，将矩阵转化为行阶梯形
        3. 回代求解
        
        参数：
            matrix: 增广矩阵，每行最后一个元素是b的值
        
        返回：
            vector<double>: 方程组的解
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = matrix.size();
        const double eps = 1e-9;
        
        // 高斯消元过程
        for (int i = 0; i < n; ++i) {
            // 找到主元行（当前列中绝对值最大的行）
            int maxRow = i;
            for (int j = i; j < n; ++j) {
                if (fabs(matrix[j][i]) > fabs(matrix[maxRow][i])) {
                    maxRow = j;
                }
            }
            
            // 交换主元行和当前行
            swap(matrix[i], matrix[maxRow]);
            
            // 如果主元为0，方程组可能有无穷多解或无解
            if (fabs(matrix[i][i]) < eps) {
                continue;
            }
            
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
    
    vector<double> expectationDPWithCycles(int n, vector<vector<pair<int, double>>>& transitions) {
        /*
        期望DP处理有环情况（使用高斯消元）
        
        问题描述：
        在有环的状态转移图中计算期望
        
        解题思路：
        1. 对于每个状态，建立期望方程
        2. 使用高斯消元求解方程组
        
        参数：
            n: 状态数量
            transitions: 转移概率列表，transitions[i]是一个列表，每个元素为[j, p]表示从i转移到j的概率为p
        
        返回：
            vector<double>: 每个状态的期望值
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        // 构建线性方程组的增广矩阵
        vector<vector<double>> matrix(n, vector<double>(n + 1, 0.0));
        
        for (int i = 0; i < n; ++i) {
            matrix[i][i] = 1.0;  // 方程左边：E[i] - sum(p_ij * E[j]) = cost[i]
            
            // 假设每个状态的代价为1，具体根据问题调整
            double cost = 1.0;
            matrix[i][n] = cost;
            
            for (auto& transition : transitions[i]) {
                int j = transition.first;
                double p = transition.second;
                if (i != j) {  // 避免自环的特殊处理
                    matrix[i][j] -= p;
                }
            }
        }
        
        // 使用高斯消元求解
        return gaussianElimination(matrix);
    }
    
    // ==================== 冷门模型：插头DP（轮廓线DP） ====================
    
    int plugDP(vector<vector<int>>& grid) {
        /*
        插头DP（轮廓线DP）示例：求网格中哈密顿回路的数量
        
        问题描述：
        给定一个网格，求其中哈密顿回路的数量
        
        解题思路：
        1. 使用轮廓线DP，记录当前处理到的位置和轮廓线状态
        2. 插头表示连接的状态，通常用二进制表示
        3. 使用哈希表优化空间复杂度
        
        参数：
            grid: 网格，1表示可通行，0表示障碍物
        
        返回：
            int: 哈密顿回路的数量
        
        时间复杂度：O(n*m*4^min(n,m))
        空间复杂度：O(4^min(n,m))
        */
        int n = grid.size();
        if (n == 0) {
            return 0;
        }
        int m = grid[0].size();
        
        // 使用哈希表优化
        unordered_map<long long, int> dp;
        
        // 初始状态：左上角没有插头
        dp[0LL] = 1;
        
        for (int i = 0; i < n; ++i) {
            // 新的一行开始，需要将状态左移一位
            unordered_map<long long, int> newDp;
            for (auto& [state, cnt] : dp) {
                // 左移一位，移除最左边的插头
                long long newState = state << 1;
                newDp[newState] += cnt;
            }
            dp = newDp;
            
            for (int j = 0; j < m; ++j) {
                unordered_map<long long, int> newDp2;
                
                for (auto& [state, cnt] : dp) {
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
                            long long newState = (state & ~(3LL << (2 * j))) | (left << (2 * (j + 1)));
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
                            long long newState = (state & ~(3LL << (2 * (j + 1)))) | (up << (2 * j));
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
                
                dp = newDp2;
            }
        }
        
        // 最终状态应该是没有任何插头（形成回路）
        auto it = dp.find(0LL);
        return it != dp.end() ? it->second : 0;
    }
    
    // ==================== 冷门模型：树上背包的优化 ====================
    
    void dfsTreeKnapsack(int u, int parent, int capacity, 
                        vector<vector<int>>& tree, vector<int>& weights, 
                        vector<int>& values, vector<vector<int>>& dp, vector<int>& size) {
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
        
        for (auto& [sz, v] : children) {
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
    
    int treeKnapsackOptimized(int root, int capacity, vector<vector<int>>& tree, 
                            vector<int>& weights, vector<int>& values) {
        /*
        树上背包的优化实现（小到大合并）
        
        问题描述：
        在树上选择一些节点，使得总重量不超过容量，且总价值最大
        
        解题思路：
        1. 使用后序遍历处理子树
        2. 使用小到大合并的策略优化复杂度
        3. 对于每个节点，维护一个容量为capacity的背包
        
        参数：
            root: 根节点
            capacity: 背包容量
            tree: 树的邻接表
            weights: 每个节点的重量
            values: 每个节点的价值
        
        返回：
            int: 最大价值
        
        时间复杂度：O(n*capacity^2)，但通过小到大合并可以降低常数
        空间复杂度：O(n*capacity)
        */
        int n = tree.size();
        vector<vector<int>> dp(n, vector<int>(capacity + 1, 0));
        vector<int> size(n, 0);
        
        // 深度优先搜索处理子树
        dfsTreeKnapsack(root, -1, capacity, tree, weights, values, dp, size);
        
        // 返回根节点的最大价值
        int maxVal = 0;
        for (int val : dp[root]) {
            maxVal = max(maxVal, val);
        }
        return maxVal;
    }
    
    // ==================== 补充题目与应用 ====================
    // 以下是一些使用上述高级DP技术的经典题目及其代码实现
    
    // 1. 编辑距离问题（LeetCode 72）
    int editDistance(string word1, string word2) {
        /*
        LeetCode 72. 编辑距离
        题目链接：https://leetcode-cn.com/problems/edit-distance/
        
        问题描述：
        给你两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数。
        你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
        
        解题思路：
        使用二维DP，dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数。
        
        时间复杂度：O(m*n)
        空间复杂度：O(m*n)
        */
        int m = word1.size();
        int n = word2.size();
        // dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数
        vector<vector<int>> dp(m + 1, vector<int>(n + 1));
        
        // 初始化边界
        for (int i = 0; i <= m; ++i) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; ++j) {
            dp[0][j] = j;
        }
        
        // 动态规划填表
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (word1[i - 1] == word2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = min({dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]}) + 1;
                }
            }
        }
        
        return dp[m][n];
    }
    
    // 2. 最长递增子序列（LeetCode 300）
    int lengthOfLIS(vector<int>& nums) {
        /*
        LeetCode 300. 最长递增子序列
        题目链接：https://leetcode-cn.com/problems/longest-increasing-subsequence/
        
        问题描述：
        给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
        
        解题思路：
        使用贪心 + 二分查找优化的DP方法。
        tails[i]表示长度为i+1的递增子序列的末尾元素的最小值。
        
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        */
        if (nums.empty()) {
            return 0;
        }
        
        vector<int> tails;
        for (int num : nums) {
            // 二分查找num应该插入的位置
            int left = 0, right = tails.size();
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] >= num) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            if (left == tails.size()) {
                tails.push_back(num);
            } else {
                tails[left] = num;
            }
        }
        
        return tails.size();
    }
    
    // 3. 背包问题变种 - 完全背包（LeetCode 322）
    int coinChange(vector<int>& coins, int amount) {
        /*
        LeetCode 322. 零钱兑换
        题目链接：https://leetcode-cn.com/problems/coin-change/
        
        问题描述：
        给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
        计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回-1。
        
        解题思路：
        使用完全背包的思想，dp[i]表示凑成金额i所需的最少硬币数。
        
        时间复杂度：O(amount * n)
        空间复杂度：O(amount)
        */
        // 初始化dp数组为无穷大
        vector<int> dp(amount + 1, INT_MAX);
        dp[0] = 0;  // 凑成金额0需要0个硬币
        
        for (int coin : coins) {
            for (int i = coin; i <= amount; ++i) {
                if (dp[i - coin] != INT_MAX) {
                    dp[i] = min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] == INT_MAX ? -1 : dp[amount];
    }
    
    // 4. 矩阵链乘法（区间DP的经典应用）
    struct MatrixChainResult {
        vector<vector<int>> dp;
        vector<vector<int>> s;
        
        MatrixChainResult(const vector<vector<int>>& dp, const vector<vector<int>>& s)
            : dp(dp), s(s) {}
    };
    
    MatrixChainResult matrixChainOrder(vector<int>& p) {
        /*
        矩阵链乘法问题
        题目来源：算法导论
        
        问题描述：
        给定一系列矩阵，计算乘法顺序使得标量乘法的次数最少。
        
        解题思路：
        使用区间DP，dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数。
        可以使用Knuth优化进一步降低时间复杂度。
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = p.size() - 1;  // 矩阵的个数
        // dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数
        vector<vector<int>> dp(n + 1, vector<int>(n + 1));
        // s[i][j]记录最优分割点
        vector<vector<int>> s(n + 1, vector<int>(n + 1));
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            for (int i = 1; i + length - 1 <= n; ++i) {
                int j = i + length - 1;
                dp[i][j] = INT_MAX;
                // 枚举分割点
                for (int k = i; k < j; ++k) {
                    // 计算当前分割点的代价
                    int cost = dp[i][k] + dp[k + 1][j] + p[i - 1] * p[k] * p[j];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        s[i][j] = k;
                    }
                }
            }
        }
        
        return MatrixChainResult(dp, s);
    }
    
    // 5. 旅行商问题（TSP）的DP实现
    int travelingSalesmanProblem(vector<vector<int>>& graph) {
        /*
        旅行商问题
        题目来源：算法竞赛经典问题
        
        问题描述：
        给定一个完全图，找到一条访问每个城市恰好一次并返回起点的最短路径。
        
        解题思路：
        使用状态压缩DP，dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度。
        
        时间复杂度：O(n^2 * 2^n)
        空间复杂度：O(n * 2^n)
        */
        int n = graph.size();
        // dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度
        vector<vector<int>> dp(1 << n, vector<int>(n, INT_MAX));
        
        // 初始状态：只访问了起点，路径长度为0
        for (int i = 0; i < n; ++i) {
            dp[1 << i][i] = 0;
        }
        
        // 枚举所有可能的状态
        for (int mask = 1; mask < (1 << n); ++mask) {
            // 枚举当前所在的城市
            for (int u = 0; u < n; ++u) {
                if (!(mask & (1 << u))) {
                    continue;
                }
                // 枚举下一个要访问的城市
                for (int v = 0; v < n; ++v) {
                    if (mask & (1 << v)) {
                        continue;
                    }
                    int newMask = mask | (1 << v);
                    if (dp[mask][u] != INT_MAX && graph[u][v] != INT_MAX) {
                        dp[newMask][v] = min(dp[newMask][v], dp[mask][u] + graph[u][v]);
                    }
                }
            }
        }
        
        // 找到最短的回路
        int result = INT_MAX;
        for (int u = 0; u < n; ++u) {
            if (dp[(1 << n) - 1][u] != INT_MAX && graph[u][0] != INT_MAX) {
                result = min(result, dp[(1 << n) - 1][u] + graph[u][0]);
            }
        }
        
        return result;
    }
    
    // 6. 区间DP：最优三角剖分
    int minimumScoreTriangulation(vector<int>& values) {
        /*
        LeetCode 1039. 多边形三角剖分的最低得分
        题目链接：https://leetcode-cn.com/problems/minimum-score-triangulation-of-polygon/
        
        问题描述：
        给定一个凸多边形，将其三角剖分，使得所有三角形的顶点乘积之和最小。
        
        解题思路：
        使用区间DP，dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分。
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = values.size();
        // dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分
        vector<vector<int>> dp(n, vector<int>(n));
        
        // 枚举区间长度
        for (int length = 3; length <= n; ++length) {
            for (int i = 0; i + length - 1 < n; ++i) {
                int j = i + length - 1;
                dp[i][j] = INT_MAX;
                // 枚举中间点
                for (int k = i + 1; k < j; ++k) {
                    dp[i][j] = min(dp[i][j], 
                                 dp[i][k] + dp[k][j] + values[i] * values[k] * values[j]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    // 7. 博弈DP：石子游戏
    bool stoneGame(vector<int>& piles) {
        /*
        LeetCode 877. 石子游戏
        题目链接：https://leetcode-cn.com/problems/stone-game/
        
        问题描述：
        给定一个表示石子堆的数组，两个玩家轮流从两端取石子，每次只能取一个，取到最后一个石子的人获胜。
        判断先手是否必胜。
        
        解题思路：
        使用区间DP，dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分。
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        */
        int n = piles.size();
        // dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分
        vector<vector<int>> dp(n, vector<int>(n));
        
        // 初始化单个石子堆
        for (int i = 0; i < n; ++i) {
            dp[i][i] = piles[i];
        }
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            for (int i = 0; i + length - 1 < n; ++i) {
                int j = i + length - 1;
                // 先手可以选择取左边或右边
                dp[i][j] = max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
            }
        }
        
        // 先手净胜分大于0则必胜
        return dp[0][n - 1] > 0;
    }
    
    // 8. 数位DP：统计1出现的次数
    int countDigitOne(int n) {
        /*
        LeetCode 233. 数字1的个数
        题目链接：https://leetcode-cn.com/problems/number-of-digit-one/
        
        问题描述：
        给定一个整数 n，计算所有小于等于 n 的非负整数中数字1出现的个数。
        
        解题思路：
        使用数位DP，逐位处理每一位上1出现的次数。
        
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        */
        if (n <= 0) {
            return 0;
        }
        
        string s = to_string(n);
        int length = s.size();
        int count = 0;
        
        // 逐位处理
        for (int i = 0; i < length; ++i) {
            long long high = 0;
            if (i > 0) {
                high = stoll(s.substr(0, i));
            }
            int current = s[i] - '0';
            long long low = 0;
            if (i < length - 1) {
                low = stoll(s.substr(i + 1));
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
    
    // 9. 树形DP：打家劫舍III
    struct TreeNode {
        int val;
        TreeNode *left;
        TreeNode *right;
        TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    };
    
    pair<int, int> robDFS(TreeNode* node) {
        if (!node) {
            return {0, 0};
        }
        
        auto left = robDFS(node->left);
        auto right = robDFS(node->right);
        
        // 偷当前节点，不能偷子节点
        int robCurrent = node->val + left.second + right.second;
        // 不偷当前节点，可以选择偷或不偷子节点
        int notRobCurrent = max(left.first, left.second) + max(right.first, right.second);
        
        return {robCurrent, notRobCurrent};
    }
    
    int rob(TreeNode* root) {
        /*
        LeetCode 337. 打家劫舍 III
        题目链接：https://leetcode-cn.com/problems/house-robber-iii/
        
        问题描述：
        在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。
        这个地区只有一个入口，我们称之为“根”。除了“根”之外，每栋房子有且只有一个“父“房子与之相连。
        一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。
        如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
        计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
        
        解题思路：
        使用树形DP，对于每个节点，维护两个状态：偷或不偷。
        
        时间复杂度：O(n)
        空间复杂度：O(h)，h为树的高度
        */
        auto result = robDFS(root);
        return max(result.first, result.second);
    }
    
    // 10. 状态压缩DP：蒙斯特曼问题
    int monsterGame(vector<vector<int>>& grid) {
        /*
        蒙斯特曼问题
        题目来源：算法竞赛问题
        
        问题描述：
        在网格中放置怪物，使得任何两个怪物都不在同一行、同一列或对角线上。
        
        解题思路：
        使用状态压缩DP，dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数。
        
        时间复杂度：O(n * 2^n)
        空间复杂度：O(2^n)
        */
        int n = grid.size();
        // dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数
        vector<long long> dp(1 << n, 0);
        dp[0] = 1;
        
        for (int i = 0; i < n; ++i) {
            vector<long long> newDp(1 << n, 0);
            for (int mask = 0; mask < (1 << n); ++mask) {
                if (dp[mask] == 0) {
                    continue;
                }
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
            dp = newDp;
        }
        
        return dp[(1 << n) - 1];
    }
    
    // 11. 高维DP：三维背包
    int threeDimensionKnapsack(int n, vector<int>& capacity, vector<vector<int>>& items) {
        /*
        三维背包问题
        题目来源：算法竞赛问题
        
        问题描述：
        有n个物品，每个物品有体积、重量、价值三个属性，背包有体积和重量两个限制，求最大价值。
        
        解题思路：
        使用三维DP，dp[i][j][k]表示前i个物品，体积为j，重量为k时的最大价值。
        
        时间复杂度：O(n * V * W)
        空间复杂度：O(n * V * W)
        */
        int V = capacity[0];
        int W = capacity[1];
        // 初始化dp数组
        vector<vector<vector<int>>> dp(n + 1, vector<vector<int>>(V + 1, vector<int>(W + 1, 0)));
        
        for (int i = 1; i <= n; ++i) {
            int v = items[i-1][0];
            int w = items[i-1][1];
            int val = items[i-1][2];
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
    
    // 12. 斜率优化DP示例
    struct ConvexHullTrick {
        /*
        凸包优化技巧示例
        题目来源：算法竞赛问题
        
        问题描述：
        当状态转移方程形如dp[i] = min{dp[j] + a[i] * b[j] + c}时，可以使用凸包优化。
        
        解题思路：
        将转移方程转换为直线的形式，维护凸包以快速查询最小值。
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        */
        
        struct Line {
            long long k, b;
            Line(long long k, long long b) : k(k), b(b) {}
        };
        
        deque<Line> dq;
        
        // 添加一条直线y = kx + b
        void addLine(long long k, long long b) {
            // 当队列中至少有两条直线时，检查是否需要删除末尾的直线
            while (dq.size() >= 2) {
                Line l1 = dq[dq.size() - 2];
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
        
        // 查询x处的最小值
        long long query(long long x) {
            // 如果队列中至少有两条直线，且第一条直线在x处的值大于第二条，删除第一条
            while (dq.size() >= 2) {
                Line l1 = dq[0];
                Line l2 = dq[1];
                if (l1.k * x + l1.b >= l2.k * x + l2.b) {
                    dq.pop_front();
                } else {
                    break;
                }
            }
            if (dq.empty()) {
                return LLONG_MAX;
            }
            Line l = dq[0];
            return l.k * x + l.b;
        }
    };
};

/**
 * 计算叉积 (a - o) × (b - o)
 */
double cross(const Point& o, const Point& a, const Point& b) {
    return (a.x - o.x) * (b.y - o.y) - (a.y - o.y) * (b.x - o.x);
}

/**
 * 计算凸包（Andrew算法）
 *
 * 问题描述：
 * 给定平面上的点集，找出所有在凸包上的点。
 *
 * 解题思路：
 * 1. 将点按x坐标排序，x相同按y排序
 * 2. 构建下凸壳和上凸壳
 * 3. 合并上下凸壳
 *
 * @param points 点集
 * @return 凸包上的点集
 */
vector<Point> convexHull(vector<Point>& points) {
    // 按x坐标排序，x相同按y排序
    sort(points.begin(), points.end());
    int n = points.size();
    
    // 构建下凸壳
    vector<Point> lower;
    for (int i = 0; i < n; ++i) {
        while (lower.size() >= 2 && cross(lower[lower.size()-2], lower.back(), points[i]) <= 0) {
            lower.pop_back();
        }
        lower.push_back(points[i]);
    }
    
    // 构建上凸壳
    vector<Point> upper;
    for (int i = n-1; i >= 0; --i) {
        while (upper.size() >= 2 && cross(upper[upper.size()-2], upper.back(), points[i]) <= 0) {
            upper.pop_back();
        }
        upper.push_back(points[i]);
    }
    
    // 合并上下凸壳，去掉重复的端点
    vector<Point> result;
    for (int i = 0; i < (int)lower.size()-1; ++i) {
        result.push_back(lower[i]);
    }
    for (int i = 0; i < (int)upper.size()-1; ++i) {
        result.push_back(upper[i]);
    }
    return result;
}

/**
 * 凸包优化DP
 *
 * 问题描述：
 * 当DP状态转移方程可以表示为dp[i] = min{dp[j] + a[i] * b[j]} + c[i]的形式时，
 * 可以使用凸包优化将时间复杂度从O(n^2)降低到O(n)或O(n log n)。
 *
 * 解题思路：
 * 对于每个j，维护一条直线y = b[j] * x + dp[j]，然后对于每个i，查询x = a[i]时的最小值。
 * 当b[j]单调递增且a[i]单调递增时，可以使用单调队列优化。
 *
 * @param dp DP数组
 * @param a a数组
 * @param b b数组
 * @return 优化后的DP数组
 */
vector<double> convexHullTrick(vector<double>& dp, vector<double>& a, vector<double>& b) {
    int n = dp.size();
    vector<int> q;  // 单调队列，存储直线的索引
    
    auto getIntersection = [&](int j1, int j2) {
        // 计算两条直线j1和j2的交点x坐标
        // 直线j1: y = b[j1] * x + dp[j1]
        // 直线j2: y = b[j2] * x + dp[j2]
        if (b[j1] == b[j2]) {
            return DBL_MAX;
        }
        return (dp[j2] - dp[j1]) / (b[j1] - b[j2]);
    };
    
    // 初始化队列，加入第一个元素
    q.push_back(0);
    
    // 对于每个i，找到最优的j
    for (int i = 1; i < n; ++i) {
        // 当队列中至少有两个元素，且第一个元素不如第二个元素优时，弹出第一个元素
        while (q.size() >= 2 && (dp[q[0]] + a[i] * b[q[0]] >= dp[q[1]] + a[i] * b[q[1]])) {
            q.erase(q.begin());
        }
        
        // 使用队列中的第一个元素作为最优的j
        dp[i] = min(dp[i], dp[q[0]] + a[i] * b[q[0]]);
        
        // 将当前i加入队列，维护队列的凸壳性质
        while (q.size() >= 2 && (getIntersection(q[q.size()-2], q.back()) >= getIntersection(q.back(), i))) {
            q.pop_back();
        }
        q.push_back(i);
    }
    
    return dp;
}

// 测试代码
int main() {
    // 测试DP+数论
    int amount = 5;
    vector<int> coins = {1, 2, 5};
    int mod = 1e9 + 7;
    cout << "零钱兑换II（模意义）: " << coinChangeMod(amount, coins, mod) << endl;  // 应该输出 4
    
    // 测试矩阵快速幂
    vector<vector<long long>> matrix = {{1, 1}, {1, 0}};
    int power = 5;
    cout << "矩阵快速幂结果: " << endl;
    vector<vector<long long>> result = matrixPowerMod(matrix, power, mod);
    for (auto& row : result) {
        for (auto& num : row) {
            cout << num << " ";
        }
        cout << endl;
    }
    
    // 测试DP+字符串
    string s = "bbbab";
    cout << "最长回文子序列长度: " << longestPalindromicSubseq(s) << endl;  // 应该输出 4
    
    // 测试后缀自动机
    SuffixAutomaton sam("banana");
    cout << "不同子串数量: " << sam.countSubstrings() << endl;  // 应该输出 15
    
    // 测试DP+计算几何
    vector<Point> points = {{0, 0}, {1, 1}, {2, 0}, {1, -1}};
    vector<Point> hull = convexHull(points);
    cout << "凸包上的点: " << endl;
    for (auto& p : hull) {
        cout << "(" << p.x << ", " << p.y << ") " << endl;
    }
    
    // 测试凸包优化DP
    vector<double> dp = {0, DBL_MAX, DBL_MAX, DBL_MAX, DBL_MAX};
    vector<double> a = {1, 2, 3, 4, 5};
    vector<double> b = {1, 2, 3, 4, 5};
    vector<double> optimizedDp = convexHullTrick(dp, a, b);
    cout << "凸包优化DP结果: " << endl;
    for (auto& num : optimizedDp) {
        cout << num << " ";
    }
    cout << endl;
    
    return 0;
}

// ==================== 优化体系：Knuth优化 ====================

// Knuth优化用于优化形如dp[i][j] = min{dp[i][k] + dp[k+1][j]} + w(i,j)的DP
// 当满足四边形不等式时，最优转移点单调
// 四边形不等式：w(a,b) + w(c,d) ≤ w(a,d) + w(c,b)，其中a ≤ c ≤ b ≤ d
// 单调性：w(b,c) ≤ w(a,d)，其中a ≤ b ≤ c ≤ d

pair<vector<vector<int>>, vector<vector<int>>> knuth_optimization(int n, function<int(int, int)> cost_func) {
    /*
    Knuth优化的DP算法
    
    问题描述：
    解决区间DP问题，其中状态转移方程满足四边形不等式
    
    解题思路：
    1. 使用Knuth优化将时间复杂度从O(n^3)降低到O(n^2)
    2. 维护最优转移点数组opt[i][j]，表示计算dp[i][j]时的最优k值
    3. 根据opt[i][j-1] ≤ opt[i][j] ≤ opt[i+1][j]的性质进行剪枝
    
    参数：
        n: 区间长度
        cost_func: 计算区间(i,j)代价的函数
    
    返回：
        pair: (dp数组, opt数组)
    
    时间复杂度：O(n^2)
    空间复杂度：O(n^2)
    */
    // 初始化dp和opt数组
    vector<vector<int>> dp(n + 1, vector<int>(n + 1, 0));
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
            dp[i][j] = INT_MAX;
            // 根据Knuth优化的性质，最优k在opt[i][j-1]到opt[i+1][j]之间
            for (int k = opt[i][j-1]; k <= min(opt[i+1][j], j-1); ++k) {
                if (dp[i][k] != INT_MAX && dp[k+1][j] != INT_MAX) {
                    int current = dp[i][k] + dp[k+1][j] + cost_func(i, j);
                    if (current < dp[i][j]) {
                        dp[i][j] = current;
                        opt[i][j] = k;
                    }
                }
            }
        }
    }
    
    return {dp, opt};
}

// ==================== 优化体系：Divide & Conquer Optimization ====================

vector<vector<int>> divide_conquer_optimization(int n, int m, function<int(int, int)> cost_func) {
    /*
    Divide & Conquer Optimization（分治优化）
    
    问题描述：
    解决形如dp[i][j] = min{dp[i-1][k] + cost(k, j)}，其中k < j
    当转移满足决策单调性时使用
    
    解题思路：
    1. 利用决策单调性，使用分治法优化DP
    2. 对于dp[i][j]，当i固定时，最优转移点k随着j的增加而单调不减
    3. 使用分治的方式计算每个区间的最优决策
    
    参数：
        n: 维度1
        m: 维度2
        cost_func: 计算cost(k,j)的函数
    
    返回：
        vector<vector<int>>: dp数组
    
    时间复杂度：O(n*m log m)
    空间复杂度：O(n*m)
    */
    // 初始化dp数组
    vector<vector<int>> dp(n + 1, vector<int>(m + 1, INT_MAX));
    dp[0][0] = 0;
    
    // 分治优化函数
    function<void(int, int, int, int, int)> solve = [&](int i, int l, int r, int opt_l, int opt_r) {
        /*
        计算dp[i][l..r]，其中最优转移点在opt_l..opt_r之间
        */
        if (l > r) {
            return;
        }
        
        int mid = (l + r) / 2;
        int best_k = opt_l;
        
        // 在opt_l到min(mid-1, opt_r)之间寻找最优k
        for (int k = opt_l; k <= min(mid, opt_r); ++k) {
            if (dp[i-1][k] != INT_MAX && cost_func(k, mid) != INT_MAX) {
                int current = dp[i-1][k] + cost_func(k, mid);
                if (current < dp[i][mid]) {
                    dp[i][mid] = current;
                    best_k = k;
                }
            }
        }
        
        // 递归处理左右子区间
        solve(i, l, mid-1, opt_l, best_k);
        solve(i, mid+1, r, best_k, opt_r);
    };
    
    // 对每个i应用分治优化
    for (int i = 1; i <= n; ++i) {
        solve(i, 1, m, 0, m);
    }
    
    return dp;
}

// ==================== 优化体系：SMAWK算法（行最小查询） ====================

vector<int> smawk(vector<vector<int>>& matrix) {
    /*
    SMAWK算法用于在Monge矩阵中快速查找每行的最小值
    
    问题描述：
    给定一个Monge矩阵，快速找到每行的最小值位置
    
    解题思路：
    1. Monge矩阵满足性质：matrix[i][j] + matrix[i+1][j+1] ≤ matrix[i][j+1] + matrix[i+1][j]
    2. SMAWK算法利用这一性质，可以在O(m+n)时间内找到每行的最小值
    3. 主要步骤包括行压缩和递归求解
    
    参数：
        matrix: 一个Monge矩阵
    
    返回：
        vector<int>: 每行最小值的列索引
    
    时间复杂度：O(m+n)，其中m是行数，n是列数
    空间复杂度：O(m+n)
    */
    int m = matrix.size();
    int n = m > 0 ? matrix[0].size() : 0;
    
    function<vector<int>(vector<int>&)> reduce_rows = [&](vector<int>& rows) -> vector<int> {
        /*行压缩：只保留可能成为最小值的行*/
        vector<int> stack;
        for (int i : rows) {
            while (stack.size() >= 2) {
                int j1 = stack[stack.size()-2];
                int j2 = stack[stack.size()-1];
                // 比较两个行在列stack.size()-1处的值
                if (matrix[j1][stack.size()-1] <= matrix[i][stack.size()-1]) {
                    break;
                } else {
                    stack.pop_back();
                }
            }
            stack.push_back(i);
        }
        return stack;
    };
    
    function<vector<int>(vector<int>&, vector<int>&)> smawk_rec = [&](vector<int>& rows, vector<int>& cols) -> vector<int> {
        /*递归实现SMAWK算法*/
        if (rows.empty()) {
            return {};
        }
        
        // 行压缩
        vector<int> reduced_rows = reduce_rows(rows);
        
        // 递归求解列数为奇数的子问题
        vector<int> half_cols;
        for (int i = 1; i < cols.size(); i += 2) {
            half_cols.push_back(cols[i]);
        }
        vector<int> min_cols(reduced_rows.size(), -1);
        
        if (!half_cols.empty()) {
            // 递归求解
            vector<int> result = smawk_rec(reduced_rows, half_cols);
            // 复制结果
            for (int i = 0; i < result.size(); ++i) {
                min_cols[i] = result[i];
            }
        }
        
        // 扩展结果到所有列
        vector<int> result(rows.size(), 0);
        int k = 0;  // min_cols的索引
        
        for (int i = 0; i < rows.size(); ++i) {
            int row = rows[i];
            // 确定当前行的最小值可能在哪个区间
            int start = (i == 0) ? 0 : (k > 0 ? min_cols[k-1] : 0);
            int end = (k < min_cols.size()) ? min_cols[k] : cols.back();
            
            // 在这个区间内查找最小值
            int min_val = INT_MAX;
            int min_col = start;
            
            // 注意这里cols是原始列的子集，需要在cols中遍历
            auto it_start = find(cols.begin(), cols.end(), start);
            auto it_end = find(cols.begin(), cols.end(), end);
            if (it_start != cols.end() && it_end != cols.end()) {
                for (auto it = it_start; it != next(it_end); ++it) {
                    int col = *it;
                    if (col < matrix[0].size() && matrix[row][col] < min_val) {
                        min_val = matrix[row][col];
                        min_col = col;
                    }
                }
            }
            
            result[i] = min_col;
            
            // 如果当前行在reduced_rows中，且不是最后一行，k前进
            if (k < reduced_rows.size() && row == reduced_rows[k]) {
                k++;
            }
        }
        
        return result;
    };
    
    vector<int> rows(m);
    vector<int> cols(n);
    for (int i = 0; i < m; ++i) rows[i] = i;
    for (int i = 0; i < n; ++i) cols[i] = i;
    
    return smawk_rec(rows, cols);
}

// ==================== 优化体系：Aliens Trick（二分约束参数+可行性DP） ====================

pair<double, double> aliens_trick(function<pair<double, double>(double)> cost_func, 
                                 function<bool(double)> check_func, 
                                 double left, double right, double eps = 1e-7) {
    /*
    Aliens Trick（二分约束参数+可行性DP）
    
    问题描述：
    解决带约束的优化问题，通常形如最小化总成本，同时满足某些约束条件
    
    解题思路：
    1. 将约束条件转化为参数λ，构造拉格朗日函数
    2. 对λ进行二分查找，使用可行性DP判断当前λ下是否满足约束
    3. 根据可行性DP的结果调整二分区间
    
    参数：
        cost_func: 计算带参数λ的成本函数
        check_func: 检查当前解是否满足约束的函数
        left: 二分左边界
        right: 二分右边界
        eps: 精度要求
    
    返回：
        pair: (最优参数λ, 对应的最优解)
    
    时间复杂度：O(log((right-left)/eps) * T(DP))，其中T(DP)是一次DP的时间复杂度
    */
    double best_lam = left;
    double best_value = 0.0;
    
    while (right - left > eps) {
        double mid = (left + right) / 2;
        // 计算当前参数下的解和约束值
        auto [current_value, constraint_value] = cost_func(mid);
        
        if (check_func(constraint_value)) {
            // 满足约束，尝试更小的参数
            right = mid;
            best_lam = mid;
            best_value = current_value;
        } else {
            // 不满足约束，需要增大参数
            left = mid;
        }
    }
    
    return {best_lam, best_value};
}

// ==================== 图上DP→最短路：分层图建模 ====================

int layered_graph_dijkstra(int n, int m, vector<tuple<int, int, int>> edges, int k) {
    /*
    分层图Dijkstra算法
    
    问题描述：
    给定一个图，允许最多使用k次特殊操作（如跳跃、免费通行等），求最短路径
    
    解题思路：
    1. 构建分层图，每层代表使用不同次数的特殊操作
    2. 对于每个节点u，在第i层表示到达u时已经使用了i次特殊操作
    3. 使用Dijkstra算法在分层图上寻找最短路径
    
    参数：
        n: 节点数量
        m: 边的数量
        edges: 边的列表，每个元素为(u, v, w)表示u到v的权为w的边
        k: 允许使用的特殊操作次数
    
    返回：
        int: 从节点1到节点n的最短路径长度
    
    时间复杂度：O((n*k + m*k) log(n*k))
    空间复杂度：O(n*k + m*k)
    */
    // 构建分层图的邻接表
    vector<vector<pair<int, int>>> graph(n * (k + 1));
    
    // 添加普通边（不使用特殊操作）
    for (auto& [u, v, w] : edges) {
        for (int i = 0; i <= k; ++i) {
            graph[u + i * n].emplace_back(v + i * n, w);
        }
    }
    
    // 添加使用特殊操作的边（如果允许的话）
    for (auto& [u, v, w] : edges) {
        for (int i = 0; i < k; ++i) {
            // 这里假设特殊操作可以免费通行（权为0），具体根据问题调整
            graph[u + i * n].emplace_back(v + (i + 1) * n, 0);
        }
    }
    
    // Dijkstra算法
    vector<int> dist(n * (k + 1), INT_MAX);
    dist[0] = 0;  // 假设起点是节点0
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> heap;
    heap.emplace(0, 0);  // (距离, 节点)
    
    while (!heap.empty()) {
        auto [d, u] = heap.top();
        heap.pop();
        
        if (d > dist[u]) {
            continue;
        }
        
        for (auto& [v, w] : graph[u]) {
            if (dist[v] > d + w) {
                dist[v] = d + w;
                heap.emplace(dist[v], v);
            }
        }
    }
    
    // 取所有层中到达终点的最小值
    int result = INT_MAX;
    for (int i = 0; i <= k; ++i) {
        if (dist[n-1 + i * n] < result) {
            result = dist[n-1 + i * n];
        }
    }
    
    return result;
}

// ==================== 冷门模型：期望DP遇环的方程组解（高斯消元） ====================

vector<double> gaussian_elimination(vector<vector<double>> matrix) {
    /*
    高斯消元法求解线性方程组
    
    问题描述：
    求解形如Ax = b的线性方程组
    
    解题思路：
    1. 构建增广矩阵
    2. 进行高斯消元，将矩阵转化为行阶梯形
    3. 回代求解
    
    参数：
        matrix: 增广矩阵，每行最后一个元素是b的值
    
    返回：
        vector<double>: 方程组的解
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    */
    int n = matrix.size();
    const double eps = 1e-9;
    
    // 高斯消元过程
    for (int i = 0; i < n; ++i) {
        // 找到主元行（当前列中绝对值最大的行）
        int max_row = i;
        for (int j = i; j < n; ++j) {
            if (abs(matrix[j][i]) > abs(matrix[max_row][i])) {
                max_row = j;
            }
        }
        
        // 交换主元行和当前行
        swap(matrix[i], matrix[max_row]);
        
        // 如果主元为0，方程组可能有无穷多解或无解
        if (abs(matrix[i][i]) < eps) {
            continue;
        }
        
        // 消元过程
        for (int j = i + 1; j < n; ++j) {
            double factor = matrix[j][i] / matrix[i][i];
            for (int k = i; k <= n; ++k) {
                matrix[j][k] -= factor * matrix[i][k];
            }
        }
    }
    
    // 回代求解
    vector<double> x(n, 0);
    for (int i = n - 1; i >= 0; --i) {
        x[i] = matrix[i][n];
        for (int j = i + 1; j < n; ++j) {
            x[i] -= matrix[i][j] * x[j];
        }
        x[i] /= matrix[i][i];
    }
    
    return x;
}

vector<double> expectation_dp_with_cycles(int n, vector<vector<pair<int, double>>> transitions) {
    /*
    期望DP处理有环情况（使用高斯消元）
    
    问题描述：
    在有环的状态转移图中计算期望
    
    解题思路：
    1. 对于每个状态，建立期望方程
    2. 使用高斯消元求解方程组
    
    参数：
        n: 状态数量
        transitions: 转移概率列表，transitions[i]是一个列表，每个元素为(j, p)表示从i转移到j的概率为p
    
    返回：
        vector<double>: 每个状态的期望值
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    */
    // 构建线性方程组的增广矩阵
    vector<vector<double>> matrix(n, vector<double>(n + 1, 0.0));
    
    for (int i = 0; i < n; ++i) {
        matrix[i][i] = 1.0;  // 方程左边：E[i] - sum(p_ij * E[j]) = cost[i]
        
        // 假设每个状态的代价为1，具体根据问题调整
        double cost = 1.0;
        matrix[i][n] = cost;
        
        for (auto& [j, p] : transitions[i]) {
            if (i != j) {  // 避免自环的特殊处理
                matrix[i][j] -= p;
            }
        }
    }
    
    // 使用高斯消元求解
    return gaussian_elimination(matrix);
}

// ==================== 冷门模型：插头DP（轮廓线DP） ====================

int plug_dp(vector<vector<int>>& grid) {
    /*
    插头DP（轮廓线DP）示例：求网格中哈密顿回路的数量
    
    问题描述：
    给定一个网格，求其中哈密顿回路的数量
    
    解题思路：
    1. 使用轮廓线DP，记录当前处理到的位置和轮廓线状态
    2. 插头表示连接的状态，通常用二进制表示
    3. 使用哈希表优化空间复杂度
    
    参数：
        grid: 网格，1表示可通行，0表示障碍物
    
    返回：
        int: 哈密顿回路的数量
    
    时间复杂度：O(n*m*4^min(n,m))
    空间复杂度：O(4^min(n,m))
    */
    int n = grid.size();
    if (n == 0) {
        return 0;
    }
    int m = grid[0].size();
    
    // 使用哈希表优化
    unordered_map<long long, int> dp;
    
    // 初始状态：左上角没有插头
    dp[0] = 1;
    
    for (int i = 0; i < n; ++i) {
        // 新的一行开始，需要将状态左移一位
        unordered_map<long long, int> new_dp;
        for (auto& [state, cnt] : dp) {
            // 左移一位，移除最左边的插头
            long long new_state = state << 1;
            new_dp[new_state] += cnt;
        }
        dp = move(new_dp);
        
        for (int j = 0; j < m; ++j) {
            unordered_map<long long, int> new_dp;
            
            for (auto& [state, cnt] : dp) {
                // 当前位置左边和上边的插头状态
                int left = (state >> (2 * j)) & 3;
                int up = (state >> (2 * (j + 1))) & 3;
                
                // 如果当前位置是障碍物，跳过
                if (grid[i][j] == 0) {
                    // 只有当左右插头都不存在时才合法
                    if (left == 0 && up == 0) {
                        new_dp[state] += cnt;
                    }
                    continue;
                }
                
                // 处理各种插头组合情况
                // 1. 没有左插头和上插头
                if (left == 0 && up == 0) {
                    // 只能创建新的插头对（用于回路的开始）
                    if (i < n - 1 && j < m - 1 && grid[i+1][j] && grid[i][j+1]) {
                        long long new_state = state | (1LL << (2 * j)) | (2LL << (2 * (j + 1)));
                        new_dp[new_state] += cnt;
                    }
                }
                
                // 2. 只有左插头
                else if (left != 0 && up == 0) {
                    // 向下延伸
                    if (i < n - 1 && grid[i+1][j]) {
                        new_dp[state] += cnt;
                    }
                    // 向右延伸
                    if (j < m - 1 && grid[i][j+1]) {
                        long long new_state = state & ~(3LL << (2 * j)) | (left << (2 * (j + 1)));
                        new_dp[new_state] += cnt;
                    }
                }
                
                // 3. 只有上插头
                else if (left == 0 && up != 0) {
                    // 向右延伸
                    if (j < m - 1 && grid[i][j+1]) {
                        new_dp[state] += cnt;
                    }
                    // 向下延伸
                    if (i < n - 1 && grid[i+1][j]) {
                        long long new_state = state & ~(3LL << (2 * (j + 1))) | (up << (2 * j));
                        new_dp[new_state] += cnt;
                    }
                }
                
                // 4. 同时有左插头和上插头
                else {
                    // 合并插头
                    long long new_state = state & ~(3LL << (2 * j)) & ~(3LL << (2 * (j + 1)));
                    
                    // 如果是形成回路的最后一步
                    if (left == up) {
                        // 检查是否所有插头都已连接
                        if (new_state == 0 && i == n - 1 && j == m - 1) {
                            new_dp[new_state] += cnt;
                        }
                    } else {
                        // 合并两个不同的插头
                        new_dp[new_state] += cnt;
                    }
                }
            }
            
            dp = move(new_dp);
        }
    }
    
    // 最终状态应该是没有任何插头（形成回路）
    return dp.count(0) ? dp[0] : 0;
}

// ==================== 冷门模型：树上背包的优化 ====================

int tree_knapsack_optimized(int root, int capacity, vector<vector<int>>& tree, vector<int>& weights, vector<int>& values) {
    /*
    树上背包的优化实现（小到大合并）
    
    问题描述：
    在树上选择一些节点，使得总重量不超过容量，且总价值最大
    
    解题思路：
    1. 使用后序遍历处理子树
    2. 使用小到大合并的策略优化复杂度
    3. 对于每个节点，维护一个容量为capacity的背包
    
    参数：
        root: 根节点
        capacity: 背包容量
        tree: 树的邻接表
        weights: 每个节点的重量
        values: 每个节点的价值
    
    返回：
        int: 最大价值
    
    时间复杂度：O(n*capacity^2)，但通过小到大合并可以降低常数
    空间复杂度：O(n*capacity)
    */
    int n = tree.size();
    vector<vector<int>> dp(n, vector<int>(capacity + 1, 0));
    vector<int> size(n, 0);
    
    function<void(int, int)> dfs = [&](int u, int parent) {
        // 初始化当前节点
        size[u] = 1;
        if (weights[u] <= capacity) {
            dp[u][weights[u]] = values[u];
        }
        
        // 对每个子节点，按照子树大小排序，小的先合并
        vector<pair<int, int>> children;
        for (int v : tree[u]) {
            if (v != parent) {
                dfs(v, u);
                children.emplace_back(size[v], v);
            }
        }
        
        // 按子树大小排序
        sort(children.begin(), children.end());
        
        for (auto& [sz, v] : children) {
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
    };
    
    dfs(root, -1);
    
    // 返回根节点的最大价值
    return *max_element(dp[root].begin(), dp[root].end());
}

// ==================== 补充题目与应用 ====================
// 以下是一些使用上述高级DP技术的经典题目及其代码实现

// 1. 编辑距离问题（LeetCode 72）
int edit_distance(string word1, string word2) {
    /*
    LeetCode 72. 编辑距离
    题目链接：https://leetcode-cn.com/problems/edit-distance/
    
    问题描述：
    给你两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数。
    你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
    
    解题思路：
    使用二维DP，dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数。
    
    时间复杂度：O(m*n)
    空间复杂度：O(m*n)
    */
    int m = word1.size();
    int n = word2.size();
    // dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数
    vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
    
    // 初始化边界
    for (int i = 0; i <= m; ++i) {
        dp[i][0] = i;
    }
    for (int j = 0; j <= n; ++j) {
        dp[0][j] = j;
    }
    
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

// 2. 最长递增子序列（LeetCode 300）
int length_of_lis(vector<int>& nums) {
    /*
    LeetCode 300. 最长递增子序列
    题目链接：https://leetcode-cn.com/problems/longest-increasing-subsequence/
    
    问题描述：
    给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
    
    解题思路：
    使用贪心 + 二分查找优化的DP方法。
    tails[i]表示长度为i+1的递增子序列的末尾元素的最小值。
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    */
    if (nums.empty()) {
        return 0;
    }
    
    vector<int> tails;
    for (int num : nums) {
        // 二分查找num应该插入的位置
        int left = 0, right = tails.size();
        while (left < right) {
            int mid = (left + right) / 2;
            if (tails[mid] < num) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        if (left == tails.size()) {
            tails.push_back(num);
        } else {
            tails[left] = num;
        }
    }
    
    return tails.size();
}

// 3. 背包问题变种 - 完全背包（LeetCode 322）
int coin_change(vector<int>& coins, int amount) {
    /*
    LeetCode 322. 零钱兑换
    题目链接：https://leetcode-cn.com/problems/coin-change/
    
    问题描述：
    给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
    计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回-1。
    
    解题思路：
    使用完全背包的思想，dp[i]表示凑成金额i所需的最少硬币数。
    
    时间复杂度：O(amount * n)
    空间复杂度：O(amount)
    */
    // 初始化dp数组为无穷大
    vector<int> dp(amount + 1, INT_MAX);
    dp[0] = 0;  // 凑成金额0需要0个硬币
    
    for (int coin : coins) {
        for (int i = coin; i <= amount; ++i) {
            if (dp[i - coin] != INT_MAX) {
                dp[i] = min(dp[i], dp[i - coin] + 1);
            }
        }
    }
    
    return dp[amount] == INT_MAX ? -1 : dp[amount];
}

// 4. 矩阵链乘法（区间DP的经典应用）
pair<vector<vector<int>>, vector<vector<int>>> matrix_chain_order(vector<int>& p) {
    /*
    矩阵链乘法问题
    题目来源：算法导论
    
    问题描述：
    给定一系列矩阵，计算乘法顺序使得标量乘法的次数最少。
    
    解题思路：
    使用区间DP，dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数。
    可以使用Knuth优化进一步降低时间复杂度。
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    */
    int n = p.size() - 1;  // 矩阵的个数
    // dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数
    vector<vector<int>> dp(n + 1, vector<int>(n + 1, 0));
    // s[i][j]记录最优分割点
    vector<vector<int>> s(n + 1, vector<int>(n + 1, 0));
    
    // 枚举区间长度
    for (int length = 2; length <= n; ++length) {
        for (int i = 1; i + length - 1 <= n; ++i) {
            int j = i + length - 1;
            dp[i][j] = INT_MAX;
            // 枚举分割点
            for (int k = i; k < j; ++k) {
                // 计算当前分割点的代价
                int cost = dp[i][k] + dp[k+1][j] + p[i-1] * p[k] * p[j];
                if (cost < dp[i][j]) {
                    dp[i][j] = cost;
                    s[i][j] = k;
                }
            }
        }
    }
    
    return {dp, s};
}

// 5. 旅行商问题（TSP）的DP实现
int traveling_salesman_problem(vector<vector<int>>& graph) {
    /*
    旅行商问题
    题目来源：算法竞赛经典问题
    
    问题描述：
    给定一个完全图，找到一条访问每个城市恰好一次并返回起点的最短路径。
    
    解题思路：
    使用状态压缩DP，dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度。
    
    时间复杂度：O(n^2 * 2^n)
    空间复杂度：O(n * 2^n)
    */
    int n = graph.size();
    // dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度
    vector<vector<int>> dp(1 << n, vector<int>(n, INT_MAX));
    
    // 初始状态：只访问了起点，路径长度为0
    for (int i = 0; i < n; ++i) {
        dp[1 << i][i] = 0;
    }
    
    // 枚举所有可能的状态
    for (int mask = 1; mask < (1 << n); ++mask) {
        // 枚举当前所在的城市
        for (int u = 0; u < n; ++u) {
            if (!(mask & (1 << u))) {
                continue;
            }
            // 枚举下一个要访问的城市
            for (int v = 0; v < n; ++v) {
                if (mask & (1 << v)) {
                    continue;
                }
                int new_mask = mask | (1 << v);
                if (dp[mask][u] != INT_MAX && graph[u][v] != INT_MAX) {
                    dp[new_mask][v] = min(dp[new_mask][v], dp[mask][u] + graph[u][v]);
                }
            }
        }
    }
    
    // 找到最短的回路
    int result = INT_MAX;
    for (int u = 0; u < n; ++u) {
        if (dp[(1 << n) - 1][u] != INT_MAX && graph[u][0] != INT_MAX) {
            result = min(result, dp[(1 << n) - 1][u] + graph[u][0]);
        }
    }
    
    return result;
}

// 6. 区间DP：最优三角剖分
int minimum_score_triangulation(vector<int>& values) {
    /*
    LeetCode 1039. 多边形三角剖分的最低得分
    题目链接：https://leetcode-cn.com/problems/minimum-score-triangulation-of-polygon/
    
    问题描述：
    给定一个凸多边形，将其三角剖分，使得所有三角形的顶点乘积之和最小。
    
    解题思路：
    使用区间DP，dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分。
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    */
    int n = values.size();
    // dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分
    vector<vector<int>> dp(n, vector<int>(n, 0));
    
    // 枚举区间长度
    for (int length = 3; length <= n; ++length) {
        for (int i = 0; i + length - 1 < n; ++i) {
            int j = i + length - 1;
            dp[i][j] = INT_MAX;
            // 枚举中间点
            for (int k = i + 1; k < j; ++k) {
                dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j] + values[i] * values[k] * values[j]);
            }
        }
    }
    
    return dp[0][n-1];
}

// 7. 博弈DP：石子游戏
bool stone_game(vector<int>& piles) {
    /*
    LeetCode 877. 石子游戏
    题目链接：https://leetcode-cn.com/problems/stone-game/
    
    问题描述：
    给定一个表示石子堆的数组，两个玩家轮流从两端取石子，每次只能取一个，取到最后一个石子的人获胜。
    判断先手是否必胜。
    
    解题思路：
    使用区间DP，dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分。
    
    时间复杂度：O(n^2)
    空间复杂度：O(n^2)
    */
    int n = piles.size();
    // dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分
    vector<vector<int>> dp(n, vector<int>(n, 0));
    
    // 初始化单个石子堆
    for (int i = 0; i < n; ++i) {
        dp[i][i] = piles[i];
    }
    
    // 枚举区间长度
    for (int length = 2; length <= n; ++length) {
        for (int i = 0; i + length - 1 < n; ++i) {
            int j = i + length - 1;
            // 先手可以选择取左边或右边
            dp[i][j] = max(piles[i] - dp[i+1][j], piles[j] - dp[i][j-1]);
        }
    }
    
    // 先手净胜分大于0则必胜
    return dp[0][n-1] > 0;
}

// 8. 数位DP：统计1出现的次数
int count_digit_one(int n) {
    /*
    LeetCode 233. 数字1的个数
    题目链接：https://leetcode-cn.com/problems/number-of-digit-one/
    
    问题描述：
    给定一个整数 n，计算所有小于等于 n 的非负整数中数字1出现的个数。
    
    解题思路：
    使用数位DP，逐位处理每一位上1出现的次数。
    
    时间复杂度：O(log n)
    空间复杂度：O(log n)
    */
    if (n <= 0) {
        return 0;
    }
    
    string s = to_string(n);
    int length = s.size();
    int count = 0;
    
    // 逐位处理
    for (int i = 0; i < length; ++i) {
        long long high = 0;
        if (i > 0) {
            high = stoll(s.substr(0, i));
        }
        int current = s[i] - '0';
        long long low = 0;
        if (i < length - 1) {
            low = stoll(s.substr(i+1));
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

// 9. 树形DP：打家劫舍III
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

pair<int, int> rob_dfs(TreeNode* node) {
    if (!node) {
        return {0, 0};
    }
    
    auto [left_rob, left_not_rob] = rob_dfs(node->left);
    auto [right_rob, right_not_rob] = rob_dfs(node->right);
    
    // 偷当前节点，不能偷子节点
    int rob_current = node->val + left_not_rob + right_not_rob;
    // 不偷当前节点，可以选择偷或不偷子节点
    int not_rob_current = max(left_rob, left_not_rob) + max(right_rob, right_not_rob);
    
    return {rob_current, not_rob_current};
}

int rob(TreeNode* root) {
    /*
    LeetCode 337. 打家劫舍 III
    题目链接：https://leetcode-cn.com/problems/house-robber-iii/
    
    问题描述：
    在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。
    这个地区只有一个入口，我们称之为“根”。除了“根”之外，每栋房子有且只有一个“父“房子与之相连。
    一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。
    如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
    计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
    
    解题思路：
    使用树形DP，对于每个节点，维护两个状态：偷或不偷。
    
    时间复杂度：O(n)
    空间复杂度：O(h)，h为树的高度
    */
    auto [rob_root, not_rob_root] = rob_dfs(root);
    return max(rob_root, not_rob_root);
}

// 10. 状态压缩DP：蒙斯特曼问题
int monster_game(vector<vector<int>>& grid) {
    /*
    蒙斯特曼问题
    题目来源：算法竞赛问题
    
    问题描述：
    在网格中放置怪物，使得任何两个怪物都不在同一行、同一列或对角线上。
    
    解题思路：
    使用状态压缩DP，dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数。
    
    时间复杂度：O(n * 2^n)
    空间复杂度：O(2^n)
    */
    int n = grid.size();
    // dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数
    vector<long long> dp(1 << n, 0);
    dp[0] = 1;
    
    for (int i = 0; i < n; ++i) {
        vector<long long> new_dp(1 << n, 0);
        for (int mask = 0; mask < (1 << n); ++mask) {
            if (dp[mask] == 0) {
                continue;
            }
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
                        new_dp[mask | (1 << j)] += dp[mask];
                    }
                }
            }
        }
        dp = move(new_dp);
    }
    
    return dp[(1 << n) - 1];
}

// 11. 高维DP：三维背包
int three_dimension_knapsack(int n, pair<int, int> capacity, vector<tuple<int, int, int>> items) {
    /*
    三维背包问题
    题目来源：算法竞赛问题
    
    问题描述：
    有n个物品，每个物品有体积、重量、价值三个属性，背包有体积和重量两个限制，求最大价值。
    
    解题思路：
    使用三维DP，dp[i][j][k]表示前i个物品，体积为j，重量为k时的最大价值。
    
    时间复杂度：O(n * V * W)
    空间复杂度：O(n * V * W)
    */
    int V = capacity.first;
    int W = capacity.second;
    // 初始化dp数组
    vector<vector<vector<int>>> dp(n + 1, vector<vector<int>>(V + 1, vector<int>(W + 1, 0)));
    
    for (int i = 1; i <= n; ++i) {
        auto [v, w, val] = items[i-1];
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

// 12. 斜率优化DP示例
class ConvexHullTrick {
public:
    /*
    凸包优化技巧示例
    题目来源：算法竞赛问题
    
    问题描述：
    当状态转移方程形如dp[i] = min{dp[j] + a[i] * b[j] + c}时，可以使用凸包优化。
    
    解题思路：
    将转移方程转换为直线的形式，维护凸包以快速查询最小值。
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    */
    
    // 添加一条直线y = kx + b
    void add_line(long long k, long long b) {
        // 当队列中至少有两条直线时，检查是否需要删除末尾的直线
        while (dq.size() >= 2) {
            auto [k1, b1] = dq[dq.size()-2];
            auto [k2, b2] = dq[dq.size()-1];
            // 判断直线k1x+b1和k2x+b2的交点是否在k2x+b2和kx+b的交点右侧
            if ((b2 - b1) * (k - k2) >= (b - b2) * (k2 - k1)) {
                dq.pop_back();
            } else {
                break;
            }
        }
        dq.emplace_back(k, b);
    }
    
    // 查询x处的最小值
    long long query(long long x) {
        // 如果队列中至少有两条直线，且第一条直线在x处的值大于第二条，删除第一条
        while (dq.size() >= 2) {
            auto [k1, b1] = dq[0];
            auto [k2, b2] = dq[1];
            if (k1 * x + b1 >= k2 * x + b2) {
                dq.pop_front();
            } else {
                break;
            }
        }
        if (dq.empty()) {
            return LLONG_MAX;
        }
        auto [k, b] = dq[0];
        return k * x + b;
    }
    
private:
    deque<pair<long long, long long>> dq;
};