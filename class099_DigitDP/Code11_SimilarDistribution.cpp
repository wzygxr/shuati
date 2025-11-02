// 洛谷P4127 [AHOI2009] 同类分布
// 题目链接: https://www.luogu.com.cn/problem/P4127
// 题目描述: 给出两个数a,b，求出[a,b]中各位数字之和能整除原数的数的个数。

#include <iostream>
#include <vector>
#include <string>
#include <cstring>
using namespace std;

class SimilarDistribution {
private:
    // 记忆化数组，用于优化递归过程
    static long long dp[20][163][163][2][2];
    
    /**
     * 数位DP递归函数
     * 
     * @param digits 数字的字符数组
     * @param pos 当前处理到第几位
     * @param sum 当前数位和
     * @param mod 当前数值对s_sum的余数
     * @param isLimit 是否受到上界限制
     * @param isNum 是否已开始填数字（处理前导零）
     * @param s_sum 目标数位和
     * @return 从当前状态开始，符合条件的数的个数
     */
    static long long dfs(const vector<int>& digits, int pos, int sum, int mod, bool isLimit, bool isNum, int s_sum) {
        // 递归终止条件
        if (pos == digits.size()) {
            // 只有当已经填了数字，且数位和等于s_sum，且数值能被s_sum整除时才算符合条件
            return isNum && sum == s_sum && mod == 0 ? 1 : 0;
        }
        
        // 记忆化搜索 - 只有当没有限制且已经开始填数字时才可以使用缓存
        if (!isLimit && isNum && dp[pos][sum][mod][0][0] != -1) {
            return dp[pos][sum][mod][0][0];
        }
        
        long long ans = 0;
        
        // 如果还没开始填数字，可以选择跳过当前位（处理前导零）
        if (!isNum) {
            ans += dfs(digits, pos + 1, sum, mod, false, false, s_sum);
        }
        
        // 确定当前位可以填入的数字范围
        int upper = isLimit ? digits[pos] : 9;
        
        // 枚举当前位可以填入的数字
        int start = isNum ? 0 : 1;
        for (int d = start; d <= upper; d++) {
            int newSum = sum + d;
            // 如果新的数位和已经超过了s_sum，可以提前剪枝
            if (newSum > s_sum) {
                continue;
            }
            
            // 更新当前数值对s_sum的余数
            int newMod = (mod * 10 + d) % s_sum;
            bool newIsLimit = isLimit && (d == upper);
            bool newIsNum = isNum || (d > 0);
            
            // 递归处理下一位
            ans += dfs(digits, pos + 1, newSum, newMod, newIsLimit, newIsNum, s_sum);
        }
        
        // 记忆化存储
        if (!isLimit && isNum) {
            dp[pos][sum][mod][0][0] = ans;
        }
        
        return ans;
    }
    
    /**
     * 优化版数位DP递归函数
     */
    static long long dfsOptimized(const vector<int>& digits, int pos, int sum, int mod, bool isLimit, bool isNum, int s_sum) {
        // 递归终止条件
        if (pos == digits.size()) {
            return isNum && sum == s_sum && mod == 0 ? 1 : 0;
        }
        
        // 记忆化搜索
        if (!isLimit && isNum && dp[pos][0][mod][0][0] != -1) {
            return dp[pos][0][mod][0][0];
        }
        
        long long ans = 0;
        
        // 处理前导零
        if (!isNum) {
            ans += dfsOptimized(digits, pos + 1, sum, mod, false, false, s_sum);
        }
        
        // 确定当前位可以填入的数字范围
        int upper = isLimit ? digits[pos] : 9;
        
        // 枚举当前位可以填入的数字
        int start = isNum ? 0 : 1;
        for (int d = start; d <= upper; d++) {
            int newSum = sum + d;
            if (newSum > s_sum) {
                continue;
            }
            
            int newMod = (mod * 10 + d) % s_sum;
            bool newIsLimit = isLimit && (d == upper);
            bool newIsNum = isNum || (d > 0);
            
            ans += dfsOptimized(digits, pos + 1, newSum, newMod, newIsLimit, newIsNum, s_sum);
        }
        
        // 记忆化存储
        if (!isLimit && isNum) {
            dp[pos][0][mod][0][0] = ans;
        }
        
        return ans;
    }
    
    /**
     * 将数字转换为字符数组形式
     */
    static vector<int> toDigitVector(long long n) {
        vector<int> digits;
        if (n == 0) {
            digits.push_back(0);
            return digits;
        }
        while (n > 0) {
            digits.push_back(n % 10);
            n /= 10;
        }
        reverse(digits.begin(), digits.end());
        return digits;
    }
    
    /**
     * 统计数位和等于s_sum且能被s_sum整除的数的个数
     */
    static long long countNumbersWithSumDivisibleBy(long long n, int s_sum) {
        vector<int> digits = toDigitVector(n);
        
        // 初始化dp为-1，表示未计算过
        memset(dp, -1, sizeof(dp));
        
        return dfs(digits, 0, 0, 0, true, false, s_sum);
    }
    
    /**
     * 优化版统计函数
     */
    static long long countNumbersWithSumDivisibleByOptimized(long long n, int s_sum) {
        vector<int> digits = toDigitVector(n);
        
        // 初始化dp为-1，表示未计算过
        memset(dp, -1, sizeof(dp));
        
        return dfsOptimized(digits, 0, 0, 0, true, false, s_sum);
    }
    
    /**
     * 计算[0, n]中符合条件的数的个数
     */
    static long long countValidNumbers(long long n) {
        if (n < 1) {
            return 0; // 0不符合条件，因为不能除以0
        }
        
        string s = to_string(n);
        int maxSum = s.length() * 9; // 最大可能的数位和
        long long result = 0;
        
        // 枚举所有可能的数位和s
        for (int s_sum = 1; s_sum <= maxSum; s_sum++) {
            // 对于每个数位和s_sum，统计满足条件的数的个数
            result += countNumbersWithSumDivisibleBy(n, s_sum);
        }
        
        return result;
    }

public:
    /**
     * 数位DP解法
     * 时间复杂度: O(log(b) * 162 * 162 * 2 * 2)
     * 空间复杂度: O(log(b) * 162 * 162 * 2 * 2)
     * 
     * 解题思路:
     * 1. 将问题转化为统计[0, b]中符合条件的数的个数减去[0, a-1]中符合条件的数的个数
     * 2. 由于数位和s的最大可能值为9*18=162（假设最多18位数），我们可以枚举数位和s
     * 3. 对于每个数位和s，使用数位DP统计满足以下条件的数x的个数：
     *    - x的数位和等于s
     *    - x能被s整除
     * 4. 状态需要记录：当前处理到第几位、当前数位和、当前数值对s的余数、是否受到上界限制、是否已经开始填数字
     * 5. 通过记忆化搜索避免重复计算
     * 
     * 最优解分析:
     * 该解法是标准的数位DP解法，能够高效处理大范围的输入，是解决此类问题的最优通用方法。
     * 时间复杂度中的162来自于数位和的最大可能值（9*18）。
     */
    static long long similarDistribution(long long a, long long b) {
        // 计算[0, b]中符合条件的数的个数减去[0, a-1]中符合条件的数的个数
        return countValidNumbers(b) - countValidNumbers(a - 1);
    }
};

// 初始化静态成员变量
long long SimilarDistribution::dp[20][163][163][2][2];

int main() {
    // 测试用例1: a=1, b=20
    // 预期输出: 19 (所有数都符合条件，除了那些数位和为0的数)
    long long a1 = 1, b1 = 20;
    long long result1 = SimilarDistribution::similarDistribution(a1, b1);
    cout << "测试用例1: a=" << a1 << ", b=" << b1 << endl;
    cout << "符合条件的数的个数: " << result1 << endl;
    
    // 测试用例2: a=1, b=100
    long long a2 = 1, b2 = 100;
    long long result2 = SimilarDistribution::similarDistribution(a2, b2);
    cout << "\n测试用例2: a=" << a2 << ", b=" << b2 << endl;
    cout << "符合条件的数的个数: " << result2 << endl;
    
    return 0;
}