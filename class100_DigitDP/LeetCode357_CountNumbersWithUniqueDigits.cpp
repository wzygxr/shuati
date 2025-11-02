/**
 * LeetCode 357. 统计各位数字都不同的数字个数 - C++实现
 * 题目链接：https://leetcode.cn/problems/count-numbers-with-unique-digits/
 * 
 * 题目描述：
 * 给你一个整数 n ，统计并返回各位数字都不同的数字 x 的个数，其中 0 <= x < 10^n。
 * 
 * 解题思路：
 * 使用数位动态规划（Digit DP）解决该问题。我们需要统计各位数字都不相同的数字个数。
 * 状态定义：
 * dp[pos][mask][limit][lead] 表示处理到第pos位，已使用的数字状态为mask，
 * limit表示是否受到上界限制，lead表示是否有前导零
 * 
 * 算法分析：
 * 时间复杂度：O(n * 2^10 * 2 * 2) = O(n)
 * 空间复杂度：O(n * 2^10)
 * 
 * C++实现特点：
 * 1. 使用固定大小数组实现记忆化
 * 2. 使用位运算优化状态表示
 * 3. 手动管理内存和状态
 * 
 * 最优解分析：
 * 这是数位DP的标准解法，对于此类计数问题是最优解。也可以使用数学方法通过排列组合直接计算，
 * 但数位DP方法更加通用，易于扩展到其他数字约束问题。
 * 
 * 工程化考量：
 * 1. 位运算优化：使用位掩码表示已使用的数字状态
 * 2. 边界处理：正确处理n=0, n=1等边界情况
 * 3. 性能优化：使用记忆化搜索避免重复计算
 * 4. 代码可读性：清晰的变量命名和详细注释
 * 
 * 相关题目链接：
 * - LeetCode 357: https://leetcode.cn/problems/count-numbers-with-unique-digits/
 * - AcWing 1082: https://www.acwing.com/problem/content/1084/
 * 
 * 多语言实现：
 * - Java: LeetCode357_CountNumbersWithUniqueDigits.java
 * - Python: LeetCode357_CountNumbersWithUniqueDigits.py
 * - C++: LeetCode357_CountNumbersWithUniqueDigits.cpp
 */

#include <iostream>
#include <cstring>
#include <string>
#include <algorithm>
using namespace std;

class Solution {
private:
    // 数位DP记忆化数组
    // dp[pos][mask][limit][lead]
    int dp[15][1024][2][2];
    // 存储上界的每一位
    int digits[15];
    // 上界的长度
    int len;
    
    /**
     * 数位DP核心函数
     * 
     * @param pos 当前处理到第几位
     * @param mask 已使用的数字状态（用位运算表示）
     * @param limit 是否受到上界限制
     * @param lead 是否有前导零
     * @return 满足条件的数字个数
     */
    int dfs(int pos, int mask, bool limit, bool lead) {
        // 递归终止条件：处理完所有数位
        if (pos == len) {
            // 每个数字（包括0）都应该被计算在内
            return 1;
        }
        
        // 记忆化搜索优化：如果该状态已经计算过，直接返回结果
        if (!limit && !lead && dp[pos][mask][limit ? 1 : 0][lead ? 1 : 0] != -1) {
            return dp[pos][mask][limit ? 1 : 0][lead ? 1 : 0];
        }
        
        int result = 0;
        
        // 确定当前位可以填入的数字范围
        int maxDigit = limit ? digits[pos] : 9;
        
        // 如果有前导零，可以继续选择前导零
        if (lead) {
            result += dfs(pos + 1, mask, false, true);
        }
        
        // 枚举当前位可以填入的数字
        for (int digit = (lead ? 1 : 0); digit <= maxDigit; digit++) {
            // 如果该数字已经使用过，跳过
            if ((mask >> digit) & 1) {
                continue;
            }
            
            // 递归处理下一位，更新mask
            result += dfs(pos + 1, mask | (1 << digit), limit && (digit == maxDigit), false);
        }
        
        // 记忆化存储结果
        if (!limit && !lead) {
            dp[pos][mask][limit ? 1 : 0][lead ? 1 : 0] = result;
        }
        
        return result;
    }
    
public:
    /**
     * 主函数：统计各位数字都不同的数字个数
     * 
     * @param n 指数
     * @return 各位数字都不同的数字个数
     * 
     * 时间复杂度：O(n * 2^10)
     * 空间复杂度：O(n * 2^10)
     */
    int countNumbersWithUniqueDigits(int n) {
        // 特殊情况处理
        if (n == 0) {
            return 1;
        }
        
        // 计算上界 10^n
        long long upper = 1;
        for (int i = 0; i < n; i++) {
            upper *= 10;
        }
        
        // 对于n=2的情况，upper=100，但我们不需要包含100这个数
        // 我们需要计算[0, 99]范围内的数字
        upper--; // 10^n - 1
        
        // 将上界转换为数字数组
        string upperStr = to_string(upper);
        len = upperStr.length();
        
        for (int i = 0; i < len; i++) {
            digits[i] = upperStr[i] - '0';
        }
        
        // 初始化DP数组
        memset(dp, -1, sizeof(dp));
        
        // 从最高位开始进行数位DP
        return dfs(0, 0, true, true);
    }
    
    /**
     * 数学方法实现 - 替代解法，时间复杂度O(n)，空间复杂度O(1)
     * 使用排列组合直接计算结果
     * 
     * @param n 指数
     * @return 各位数字都不同的数字个数
     */
    int countNumbersWithUniqueDigitsMath(int n) {
        if (n == 0) return 1;
        if (n == 1) return 10;
        
        // 第一位有9种选择(1-9)，第二位有9种选择(0-9除去第一位)，
        // 第三位有8种选择，以此类推
        int result = 10; // 一位数的情况
        int uniqueDigits = 9; // 两位数开始的首位选择
        
        for (int i = 2; i <= min(n, 10); i++) {
            uniqueDigits *= (11 - i); // 第i位的选择数
            result += uniqueDigits;
        }
        
        return result;
    }
};

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1
    int n1 = 2;
    int result1 = solution.countNumbersWithUniqueDigits(n1);
    int result1Math = solution.countNumbersWithUniqueDigitsMath(n1);
    cout << "测试用例1 - n = " << n1 << endl;
    cout << "数位DP结果: " << result1 << endl;
    cout << "数学方法结果: " << result1Math << endl;
    cout << "期望输出: 91" << endl;
    cout << "测试结果: " << (result1 == 91 && result1Math == 91 ? "通过" : "失败") << endl;
    cout << endl;
    
    // 测试用例2
    int n2 = 0;
    int result2 = solution.countNumbersWithUniqueDigits(n2);
    int result2Math = solution.countNumbersWithUniqueDigitsMath(n2);
    cout << "测试用例2 - n = " << n2 << endl;
    cout << "数位DP结果: " << result2 << endl;
    cout << "数学方法结果: " << result2Math << endl;
    cout << "期望输出: 1" << endl;
    cout << "测试结果: " << (result2 == 1 && result2Math == 1 ? "通过" : "失败") << endl;
    cout << endl;
    
    // 测试用例3
    int n3 = 1;
    int result3 = solution.countNumbersWithUniqueDigits(n3);
    int result3Math = solution.countNumbersWithUniqueDigitsMath(n3);
    cout << "测试用例3 - n = " << n3 << endl;
    cout << "数位DP结果: " << result3 << endl;
    cout << "数学方法结果: " << result3Math << endl;
    cout << "期望输出: 10" << endl;
    cout << "测试结果: " << (result3 == 10 && result3Math == 10 ? "通过" : "失败") << endl;
    cout << endl;
    
    return 0;
}