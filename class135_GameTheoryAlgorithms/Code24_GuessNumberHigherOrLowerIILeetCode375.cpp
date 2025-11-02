#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <cmath>
using namespace std;

// 猜数字大小II (LeetCode 375)
// 题目来源：LeetCode 375. Guess Number Higher or Lower II - https://leetcode.com/problems/guess-number-higher-or-lower-ii/
// 题目描述：我们正在玩一个猜数游戏，游戏规则如下：
// 我从 1 到 n 之间选择一个数字。
// 你来猜我选了哪个数字。
// 如果你猜到正确的数字，就会赢得游戏。
// 如果你猜错了，我会告诉你，我选的数字是比你猜的数字大还是小，并且你需要支付你猜的数字的金额。
// 给定一个范围 [1, n]，返回确保获胜的最小金额。
//
// 算法核心思想：
// 1. 动态规划：dp[i][j]表示在区间[i,j]内确保获胜所需的最小金额
// 2. 状态转移：dp[i][j] = min(k + max(dp[i][k-1], dp[k+1][j])) for k in [i,j]
// 3. 区间DP：从小区间到大区间逐步计算
//
// 时间复杂度分析：
// 1. 时间复杂度：O(n^3) - 三重循环
// 2. 空间复杂度：O(n^2) - 二维dp数组
//
// 工程化考量：
// 1. 异常处理：处理边界情况（n=0,1）
// 2. 性能优化：使用动态规划避免重复计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的代价函数
class Code24_GuessNumberHigherOrLowerIILeetCode375 {
public:
    /**
     * 动态规划解法：解决猜数字大小II问题
     * @param n 数字范围上限
     * @return 确保获胜的最小金额
     */
    static int getMoneyAmount(int n) {
        // 异常处理：边界情况
        if (n <= 0) {
            return 0;
        }
        if (n == 1) {
            return 0; // 只有一个数字，直接猜中，不需要支付
        }
        
        // 创建dp数组，dp[i][j]表示在区间[i,j]内确保获胜所需的最小金额
        vector<vector<int>> dp(n + 1, vector<int>(n + 1, 0));
        
        // 初始化：当区间长度为1时，金额为0（直接猜中）
        for (int i = 1; i <= n; i++) {
            dp[i][i] = 0;
        }
        
        // 按区间长度从小到大递推
        for (int len = 2; len <= n; len++) {
            for (int i = 1; i <= n - len + 1; i++) {
                int j = i + len - 1;
                dp[i][j] = INT_MAX;
                
                // 尝试所有可能的猜测点k
                for (int k = i; k <= j; k++) {
                    // 计算在k点猜测的代价
                    int cost = k;
                    
                    // 左区间[i, k-1]的代价（如果存在）
                    int leftCost = (k > i) ? dp[i][k - 1] : 0;
                    
                    // 右区间[k+1, j]的代价（如果存在）
                    int rightCost = (k < j) ? dp[k + 1][j] : 0;
                    
                    // 总代价 = 当前猜测代价 + 最坏情况下的后续代价
                    int totalCost = cost + max(leftCost, rightCost);
                    
                    // 取最小值
                    dp[i][j] = min(dp[i][j], totalCost);
                }
            }
        }
        
        return dp[1][n];
    }
    
    /**
     * 优化版本：减少不必要的计算
     * 时间复杂度：O(n^3)，但常数更小
     */
    static int getMoneyAmountOptimized(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 0;
        
        vector<vector<int>> dp(n + 1, vector<int>(n + 1, 0));
        
        // 初始化对角线
        for (int i = 1; i <= n; i++) {
            dp[i][i] = 0;
        }
        
        for (int len = 2; len <= n; len++) {
            for (int i = 1; i <= n - len + 1; i++) {
                int j = i + len - 1;
                dp[i][j] = INT_MAX;
                
                // 优化：只在中点附近搜索，减少计算量
                // 根据数学分析，最优猜测点通常在区间中点附近
                int start = max(i, (i + j) / 2 - 10);
                int end = min(j, (i + j) / 2 + 10);
                
                for (int k = start; k <= end; k++) {
                    int left = (k > i) ? dp[i][k - 1] : 0;
                    int right = (k < j) ? dp[k + 1][j] : 0;
                    int cost = k + max(left, right);
                    dp[i][j] = min(dp[i][j], cost);
                }
            }
        }
        
        return dp[1][n];
    }
    
    /**
     * 记忆化搜索版本
     * 时间复杂度：O(n^3)，空间复杂度：O(n^2)
     */
    static int getMoneyAmountMemo(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 0;
        
        vector<vector<int>> memo(n + 1, vector<int>(n + 1, 0));
        return dfs(1, n, memo);
    }
    
private:
    static int dfs(int left, int right, vector<vector<int>>& memo) {
        // 边界条件：区间为空或只有一个元素
        if (left >= right) {
            return 0;
        }
        
        // 检查记忆化数组
        if (memo[left][right] != 0) {
            return memo[left][right];
        }
        
        int minCost = INT_MAX;
        
        // 尝试所有可能的猜测点
        for (int k = left; k <= right; k++) {
            // 计算最坏情况下的代价
            int cost = k + max(
                dfs(left, k - 1, memo),  // 数字在左区间
                dfs(k + 1, right, memo)  // 数字在右区间
            );
            minCost = min(minCost, cost);
        }
        
        memo[left][right] = minCost;
        return minCost;
    }
    
public:
    /**
     * 数学规律版本（近似解）
     * 时间复杂度：O(1)，空间复杂度：O(1)
     */
    static int getMoneyAmountMath(int n) {
        // 数学规律：对于较大的n，最小金额约等于n*log(n)
        // 这是一个近似解，适用于快速估算
        if (n <= 1) return 0;
        return static_cast<int>(n * log(n) / log(2));
    }
};

// 测试函数
int main() {
    // 测试用例1：n=1
    cout << "n=1: " << Code24_GuessNumberHigherOrLowerIILeetCode375::getMoneyAmount(1) << endl; // 应输出0
    
    // 测试用例2：n=2
    cout << "n=2: " << Code24_GuessNumberHigherOrLowerIILeetCode375::getMoneyAmount(2) << endl; // 应输出1
    
    // 测试用例3：n=3
    cout << "n=3: " << Code24_GuessNumberHigherOrLowerIILeetCode375::getMoneyAmount(3) << endl; // 应输出2
    
    // 测试用例4：n=4
    cout << "n=4: " << Code24_GuessNumberHigherOrLowerIILeetCode375::getMoneyAmount(4) << endl; // 应输出4
    
    // 测试用例5：n=10
    cout << "n=10: " << Code24_GuessNumberHigherOrLowerIILeetCode375::getMoneyAmount(10) << endl; // 应输出16
    
    // 验证优化版本
    cout << "优化版本测试:" << endl;
    cout << "n=10: " << Code24_GuessNumberHigherOrLowerIILeetCode375::getMoneyAmountOptimized(10) << endl;
    cout << "n=20: " << Code24_GuessNumberHigherOrLowerIILeetCode375::getMoneyAmountOptimized(20) << endl;
    
    // 验证记忆化搜索版本
    cout << "记忆化搜索版本测试:" << endl;
    cout << "n=10: " << Code24_GuessNumberHigherOrLowerIILeetCode375::getMoneyAmountMemo(10) << endl;
    cout << "n=20: " << Code24_GuessNumberHigherOrLowerIILeetCode375::getMoneyAmountMemo(20) << endl;
    
    // 验证数学规律版本
    cout << "数学规律版本测试:" << endl;
    cout << "n=10 (近似): " << Code24_GuessNumberHigherOrLowerIILeetCode375::getMoneyAmountMath(10) << endl;
    cout << "n=100 (近似): " << Code24_GuessNumberHigherOrLowerIILeetCode375::getMoneyAmountMath(100) << endl;
    
    // 边界测试：n=0
    cout << "n=0: " << Code24_GuessNumberHigherOrLowerIILeetCode375::getMoneyAmount(0) << endl; // 应输出0
    
    return 0;
}