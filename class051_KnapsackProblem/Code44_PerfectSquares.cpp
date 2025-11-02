#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <cmath>
#include <queue>
#include <unordered_set>

using namespace std;

// LeetCode 279. 完全平方数
// 题目描述：给定正整数 n，找到若干个完全平方数（比如 1, 4, 9, 16, ...）使得它们的和等于 n。
// 你需要让组成和的完全平方数的个数最少。
// 链接：https://leetcode.cn/problems/perfect-squares/
// 
// 解题思路：
// 这是一个完全背包问题，其中：
// - 背包容量：正整数 n
// - 物品：完全平方数（1, 4, 9, 16, ...）
// - 每个物品可以无限次使用（完全背包）
// - 目标：使用最少数量的物品（完全平方数）装满背包
// 
// 状态定义：dp[i] 表示和为 i 的完全平方数的最少数量
// 状态转移方程：dp[i] = min(dp[i], dp[i - j*j] + 1)，其中 j*j <= i
// 初始状态：dp[0] = 0，dp[i] = INT_MAX（表示不可达）
// 
// 时间复杂度：O(n * √n)，其中 n 是给定的正整数
// 空间复杂度：O(n)，使用一维DP数组
// 
// 工程化考量：
// 1. 异常处理：处理 n <= 0 的情况
// 2. 边界条件：n=0时返回0，n=1时返回1
// 3. 性能优化：预先生成完全平方数列表
// 4. 可读性：清晰的变量命名和注释

class Solution {
public:
    /**
     * 动态规划解法 - 完全背包问题
     * @param n 目标正整数
     * @return 组成n的最少完全平方数个数
     */
    int numSquares(int n) {
        // 参数验证
        if (n <= 0) {
            throw invalid_argument("n must be positive");
        }
        
        // 特殊情况处理
        if (n == 1) return 1;
        
        // 创建DP数组，dp[i]表示和为i的最少完全平方数个数
        vector<int> dp(n + 1, INT_MAX);
        dp[0] = 0;
        
        // 遍历所有可能的完全平方数
        for (int i = 1; i * i <= n; i++) {
            int square = i * i;
            // 完全背包：正序遍历容量
            for (int j = square; j <= n; j++) {
                // 避免整数溢出
                if (dp[j - square] != INT_MAX) {
                    dp[j] = min(dp[j], dp[j - square] + 1);
                }
            }
        }
        
        return dp[n];
    }
    
    /**
     * 优化的动态规划解法 - 预先生成完全平方数列表
     * @param n 目标正整数
     * @return 组成n的最少完全平方数个数
     */
    int numSquaresOptimized(int n) {
        if (n <= 0) throw invalid_argument("n must be positive");
        
        // 预先生成所有可能的完全平方数
        int maxSquareRoot = static_cast<int>(sqrt(n));
        vector<int> squares;
        for (int i = 1; i <= maxSquareRoot; i++) {
            squares.push_back(i * i);
        }
        
        vector<int> dp(n + 1, INT_MAX);
        dp[0] = 0;
        
        // 先遍历物品（完全平方数），再遍历容量
        for (int square : squares) {
            for (int j = square; j <= n; j++) {
                if (dp[j - square] != INT_MAX) {
                    dp[j] = min(dp[j], dp[j - square] + 1);
                }
            }
        }
        
        return dp[n];
    }
    
    /**
     * 数学解法 - 利用四平方定理
     * 拉格朗日四平方定理：每个正整数都可以表示为4个整数的平方和
     * 勒让德三平方定理：当且仅当n≠4^a(8b+7)时，n可以表示为3个整数的平方和
     * @param n 目标正整数
     * @return 组成n的最少完全平方数个数
     */
    int numSquaresMath(int n) {
        // 检查n是否是完全平方数
        if (isPerfectSquare(n)) {
            return 1;
        }
        
        // 检查是否满足勒让德三平方定理的排除条件
        if (checkLegendreThreeSquare(n)) {
            return 4;
        }
        
        // 检查是否可以表示为两个平方数之和
        for (int i = 1; i * i <= n; i++) {
            int j = n - i * i;
            if (isPerfectSquare(j)) {
                return 2;
            }
        }
        
        // 其他情况返回3
        return 3;
    }
    
    /**
     * 判断一个数是否是完全平方数
     */
    bool isPerfectSquare(int x) {
        int sqrt_val = static_cast<int>(sqrt(x));
        return sqrt_val * sqrt_val == x;
    }
    
    /**
     * 检查是否满足勒让德三平方定理的排除条件
     * 即 n = 4^a(8b+7)
     */
    bool checkLegendreThreeSquare(int n) {
        while (n % 4 == 0) {
            n /= 4;
        }
        return n % 8 == 7;
    }
    
    /**
     * BFS解法 - 将问题转化为图的最短路径问题
     * 每个数字是一个节点，如果两个数字相差一个完全平方数，则它们之间有边
     */
    int numSquaresBFS(int n) {
        if (n <= 0) throw invalid_argument("n must be positive");
        
        // 使用队列进行BFS
        queue<int> q;
        // 记录到达每个数字的最短步数
        vector<int> steps(n + 1, -1);
        
        // 从0开始
        q.push(0);
        steps[0] = 0;
        
        while (!q.empty()) {
            int current = q.front();
            q.pop();
            
            // 尝试所有可能的完全平方数
            for (int i = 1; i * i <= n - current; i++) {
                int next = current + i * i;
                
                // 如果超出范围或已经访问过，跳过
                if (next > n || steps[next] != -1) {
                    continue;
                }
                
                steps[next] = steps[current] + 1;
                
                // 如果到达目标，直接返回
                if (next == n) {
                    return steps[next];
                }
                
                q.push(next);
            }
        }
        
        return steps[n];
    }
};

// 测试函数
void testPerfectSquares() {
    Solution sol;
    
    // 测试用例
    vector<int> testCases = {12, 13, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    
    cout << "完全平方数问题测试：" << endl;
    for (int n : testCases) {
        int result1 = sol.numSquares(n);
        int result2 = sol.numSquaresOptimized(n);
        int result3 = sol.numSquaresBFS(n);
        int result4 = sol.numSquaresMath(n);
        
        cout << "n=" << n << ": DP=" << result1 
             << ", Optimized=" << result2 
             << ", BFS=" << result3 
             << ", Math=" << result4 << endl;
        
        // 验证所有方法结果一致
        if (result1 != result2 || result2 != result3 || result3 != result4) {
            cout << "警告：不同方法结果不一致！" << endl;
        }
    }
    
    // 性能测试
    auto startTime = chrono::high_resolution_clock::now();
    int largeResult = sol.numSquares(10000);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    
    cout << "n=10000 的结果: " << largeResult << ", 耗时: " << duration.count() << "ms" << endl;
}

int main() {
    try {
        testPerfectSquares();
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    return 0;
}

/*
 * 复杂度分析：
 * 
 * 方法1：动态规划（完全背包）
 * - 时间复杂度：O(n * √n)
 *   - 外层循环：√n 次（完全平方数的个数）
 *   - 内层循环：n 次（背包容量）
 * - 空间复杂度：O(n)
 * 
 * 方法2：优化的动态规划
 * - 时间复杂度：O(n * √n)（与方法1相同，但常数更小）
 * - 空间复杂度：O(n)
 * 
 * 方法3：数学解法
 * - 时间复杂度：O(√n)
 *   - 检查完全平方数：O(1)
 *   - 检查勒让德条件：O(log n)
 *   - 检查两个平方数之和：O(√n)
 * - 空间复杂度：O(1)
 * 
 * 方法4：BFS解法
 * - 时间复杂度：O(n * √n)（最坏情况）
 * - 空间复杂度：O(n)
 * 
 * 最优解分析：
 * - 对于小规模n（n < 1000）：所有方法都很快
 * - 对于大规模n（n >= 10000）：数学解法最优，时间复杂度最低
 * - 在实际工程中：推荐使用动态规划，代码清晰易懂
 * 
 * C++特定优化：
 * 1. 使用vector代替数组，更安全
 * 2. 使用chrono进行精确性能测试
 * 3. 异常处理使用C++标准异常
 * 4. 使用static_cast进行安全的类型转换
 */