// 洛谷P2602 数字计数
// 题目链接: https://www.luogu.com.cn/problem/P2602
// 题目描述: 给定两个正整数a,b，求在区间[a,b]中的数，数字0~9的出现次数之和。

#include <iostream>
#include <vector>
#include <string>
#include <cstring>
using namespace std;

class DigitCountEnhanced {
private:
    // 记忆化数组，用于优化递归过程
    static long long dp[20][20][2][2];
    
    /**
     * 数位DP递归函数
     * 
     * @param digits 数字的字符数组
     * @param pos 当前处理到第几位
     * @param count 当前已经出现digit的次数
     * @param isLimit 是否受到上界限制
     * @param isNum 是否已开始填数字（处理前导零）
     * @param digit 要统计的数字
     * @return 从当前状态开始，digit出现的次数总和
     */
    static long long dfs(const vector<int>& digits, int pos, int count, bool isLimit, bool isNum, int digit) {
        // 递归终止条件
        if (pos == digits.size()) {
            return count;
        }
        
        // 记忆化搜索 - 只有当没有限制且已经开始填数字时才可以使用缓存
        if (!isLimit && isNum && dp[pos][count][0][0] != -1) {
            return dp[pos][count][0][0];
        }
        
        long long ans = 0;
        
        // 如果还没开始填数字，可以选择跳过当前位（处理前导零）
        if (!isNum) {
            ans += dfs(digits, pos + 1, count, false, false, digit);
        }
        
        // 确定当前位可以填入的数字范围
        int upper = isLimit ? digits[pos] : 9;
        
        // 枚举当前位可以填入的数字
        int start = isNum ? 0 : 1;
        for (int d = start; d <= upper; d++) {
            int newCount = count;
            if (d == digit) {
                newCount++;
            }
            
            bool newIsLimit = isLimit && (d == upper);
            bool newIsNum = isNum || (d > 0);
            
            // 递归处理下一位
            ans += dfs(digits, pos + 1, newCount, newIsLimit, newIsNum, digit);
        }
        
        // 记忆化存储
        if (!isLimit && isNum) {
            dp[pos][count][0][0] = ans;
        }
        
        return ans;
    }
    
    /**
     * 优化版数位DP递归函数
     */
    static long long dfsOptimized(const vector<int>& digits, int pos, int count, bool isLimit, bool hasLeadingZero, int digit) {
        // 递归终止条件
        if (pos == digits.size()) {
            return count;
        }
        
        // 记忆化搜索
        if (!isLimit && !hasLeadingZero && dp[pos][count][0][0] != -1) {
            return dp[pos][count][0][0];
        }
        
        long long ans = 0;
        
        // 确定当前位可以填入的数字范围
        int upper = isLimit ? digits[pos] : 9;
        
        // 枚举当前位可以填入的数字
        for (int d = 0; d <= upper; d++) {
            bool newIsLimit = isLimit && (d == upper);
            bool newHasLeadingZero = hasLeadingZero && (d == 0);
            
            int newCount = count;
            if (!newHasLeadingZero && d == digit) {
                newCount++;
            }
            
            // 递归处理下一位
            ans += dfsOptimized(digits, pos + 1, newCount, newIsLimit, newHasLeadingZero, digit);
        }
        
        // 记忆化存储
        if (!isLimit && !hasLeadingZero) {
            dp[pos][count][0][0] = ans;
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
     * 统计[0, n]范围内digit出现的次数
     */
    static long long countDigit(long long n, int digit) {
        if (n < 0) {
            return 0;
        }
        if (n < 10) {
            // 对于小于10的数，直接判断digit是否小于等于n
            return 0LL + (digit == 0 ? 0 : (digit <= n ? 1 : 0));
        }
        
        vector<int> digits = toDigitVector(n);
        
        // 初始化dp为-1，表示未计算过
        memset(dp, -1, sizeof(dp));
        
        // 处理特殊情况：当digit是0时，我们需要额外处理
        if (digit == 0) {
            // 对于0的计数，我们需要计算[1, n]中0的个数
            // 因为在数位DP中，前导零不算作有效数字
            long long total = 0;
            // 直接遍历每一位，计算每一位上0出现的次数
            for (int i = 1; i <= digits.size(); i++) {
                // 计算高位部分
                long long high = n / (long long)pow(10, i);
                // 计算当前位的数字
                int current = (n / (long long)pow(10, i - 1)) % 10;
                // 计算低位部分
                long long low = n % (long long)pow(10, i - 1);
                
                if (current > 0) {
                    total += high * (long long)pow(10, i - 1);
                } else {
                    total += (high - 1) * (long long)pow(10, i - 1) + (low + 1);
                }
            }
            
            return total;
        }
        
        return dfs(digits, 0, 0, true, false, digit);
    }
    
    /**
     * 优化版统计函数
     */
    static long long countDigitOptimized(long long n, int digit) {
        if (n < 0) {
            return 0;
        }
        if (n < 10) {
            return 0LL + (digit == 0 ? 0 : (digit <= n ? 1 : 0));
        }
        
        vector<int> digits = toDigitVector(n);
        
        // 初始化dp为-1，表示未计算过
        memset(dp, -1, sizeof(dp));
        
        return dfsOptimized(digits, 0, 0, true, true, digit);
    }

public:
    /**
     * 数位DP解法
     * 时间复杂度: O(10 * log(b) * 10 * 2)
     * 空间复杂度: O(10 * log(b) * 10 * 2)
     * 
     * 解题思路:
     * 1. 将问题转化为统计[0, b]中每个数字出现的次数减去[0, a-1]中每个数字出现的次数
     * 2. 对于每个数字d(0-9)，使用数位DP统计在[0, n]范围内d出现的次数
     * 3. 状态需要记录：当前处理到第几位、当前已经出现d的次数、是否受到上界限制、是否已经开始填数字（处理前导零）
     * 4. 通过记忆化搜索避免重复计算
     * 
     * 最优解分析:
     * 该解法是标准的数位DP解法，能够高效处理大范围的输入，是解决此类问题的最优通用方法。
     */
    static vector<long long> digitCount(long long a, long long b) {
        vector<long long> result(10, 0);
        // 计算[0, b]中每个数字出现的次数减去[0, a-1]中每个数字出现的次数
        for (int d = 0; d < 10; d++) {
            result[d] = countDigit(b, d) - countDigit(a - 1, d);
        }
        return result;
    }
    
    /**
     * 使用优化版数位DP函数的解决方案
     */
    static vector<long long> digitCountOptimized(long long a, long long b) {
        vector<long long> result(10, 0);
        for (int d = 0; d < 10; d++) {
            result[d] = countDigitOptimized(b, d) - countDigitOptimized(a - 1, d);
        }
        return result;
    }
};

// 初始化静态成员变量
long long DigitCountEnhanced::dp[20][20][2][2];

int main() {
    // 测试用例1: a=1, b=10
    // 预期输出: [1, 2, 1, 1, 1, 1, 1, 1, 1, 1]
    // 解释: 0出现1次(10)，1出现2次(1, 10)，其他数字各出现1次
    long long a1 = 1, b1 = 10;
    vector<long long> result1 = DigitCountEnhanced::digitCount(a1, b1);
    cout << "测试用例1: a=" << a1 << ", b=" << b1 << endl;
    cout << "各数字出现次数: ";
    for (long long cnt : result1) {
        cout << cnt << " ";
    }
    cout << endl;
    
    // 测试用例2: a=123, b=456
    long long a2 = 123, b2 = 456;
    vector<long long> result2 = DigitCountEnhanced::digitCount(a2, b2);
    cout << "\n测试用例2: a=" << a2 << ", b=" << b2 << endl;
    cout << "各数字出现次数: ";
    for (long long cnt : result2) {
        cout << cnt << " ";
    }
    cout << endl;
    
    return 0;
}