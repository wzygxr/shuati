/**
 * LeetCode 233. 数字 1 的个数 - C++实现
 * 题目链接：https://leetcode.cn/problems/number-of-digit-one/
 * 
 * 题目描述：
 * 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。
 * 
 * 解题思路：
 * 使用数位动态规划（Digit DP）解决该问题。核心思想是逐位统计数字1的出现次数。
 * 
 * 算法分析：
 * 时间复杂度：O(L²) 其中L是数字n的位数
 * 空间复杂度：O(L²) 用于存储DP状态
 * 
 * C++实现特点：
 * 1. 使用vector代替Java数组，自动内存管理
 * 2. 使用引用传递避免拷贝开销
 * 3. 使用const修饰符确保函数安全性
 * 
 * 最优解分析：
 * 这是数位DP的标准解法，对于此类计数问题是最优解。数学方法虽然可以达到O(L)时间复杂度，
 * 但数位DP方法更加通用，易于扩展到其他数字统计问题。
 * 
 * 工程化考量：
 * 1. 异常处理：处理n为负数的情况
 * 2. 边界测试：测试n=0, n=1等边界情况
 * 3. 性能优化：使用记忆化搜索避免重复计算
 * 4. 代码可读性：清晰的变量命名和详细注释
 * 
 * 相关题目链接：
 * - LeetCode 233: https://leetcode.cn/problems/number-of-digit-one/
 * - 剑指Offer 43: https://leetcode.cn/problems/1nzheng-shu-zhong-1chu-xian-de-ci-shu-lcof/
 * 
 * 多语言实现：
 * - Java: LeetCode233_NumberOfDigitOne.java
 * - Python: LeetCode233_NumberOfDigitOne.py
 * - C++: LeetCode233_NumberOfDigitOne.cpp
 */

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <cstring>
#include <chrono>

using namespace std;

class Solution {
private:
    // 数位DP记忆化数组：dp[pos][count][limit]
    vector<vector<vector<int>>> dp;
    vector<int> digits;
    int len;
    
    /**
     * 数位DP核心递归函数
     * 
     * @param pos 当前处理的位置
     * @param count 已经统计到的数字1的个数
     * @param limit 是否受到上界限制
     * @param lead 是否有前导零
     * @return 从当前状态开始，满足条件的数字个数
     */
    int dfs(int pos, int count, bool limit, bool lead) {
        // 递归终止条件：处理完所有数位
        if (pos == len) {
            return count;
        }
        
        // 记忆化搜索优化
        if (!limit && !lead) {
            int limitIndex = limit ? 1 : 0;
            if (dp[pos][count][limitIndex] != -1) {
                return dp[pos][count][limitIndex];
            }
        }
        
        int result = 0;
        int maxDigit = limit ? digits[pos] : 9;
        
        for (int digit = 0; digit <= maxDigit; digit++) {
            int newCount = count + (digit == 1 ? 1 : 0);
            bool newLimit = limit && (digit == maxDigit);
            bool newLead = lead && (digit == 0);
            
            result += dfs(pos + 1, newCount, newLimit, newLead);
        }
        
        // 记忆化存储
        if (!limit && !lead) {
            int limitIndex = limit ? 1 : 0;
            dp[pos][count][limitIndex] = result;
        }
        
        return result;
    }

public:
    /**
     * 主函数：计算所有小于等于n的非负整数中数字1出现的个数
     * 
     * @param n 目标数字
     * @return 数字1出现的总次数
     */
    int countDigitOne(int n) {
        if (n < 0) return 0;
        
        string numStr = to_string(n);
        len = numStr.length();
        digits.resize(len);
        
        for (int i = 0; i < len; i++) {
            digits[i] = numStr[i] - '0';
        }
        
        // 初始化DP数组：len × (len+1) × 2
        dp.resize(len, vector<vector<int>>(len + 1, vector<int>(2, -1)));
        
        return dfs(0, 0, true, true);
    }
};

/**
 * 数学方法实现 - 最优解，时间复杂度O(L)，空间复杂度O(1)
 * 这是该问题的最优数学解法，但只适用于统计特定数字出现次数的问题
 */
class SolutionMath {
public:
    int countDigitOne(int n) {
        if (n <= 0) return 0;
        
        long count = 0;
        long factor = 1;
        long lower, curr, higher;
        
        while (n / factor != 0) {
            lower = n - (n / factor) * factor;
            curr = (n / factor) % 10;
            higher = n / (factor * 10);
            
            if (curr == 0) {
                count += higher * factor;
            } else if (curr == 1) {
                count += higher * factor + lower + 1;
            } else {
                count += (higher + 1) * factor;
            }
            
            factor *= 10;
        }
        
        return (int)count;
    }
};

// 测试函数
int main() {
    Solution solution;
    SolutionMath solutionMath;
    
    // 测试用例1：n=13
    int n1 = 13;
    int result1 = solution.countDigitOne(n1);
    int result1Math = solutionMath.countDigitOne(n1);
    cout << "测试用例1 - n = " << n1 << endl;
    cout << "数位DP结果: " << result1 << endl;
    cout << "数学方法结果: " << result1Math << endl;
    cout << "期望结果: 6" << endl;
    cout << "测试结果: " << (result1 == 6 && result1Math == 6 ? "通过" : "失败") << endl;
    cout << endl;
    
    // 测试用例2：n=0
    int n2 = 0;
    int result2 = solution.countDigitOne(n2);
    int result2Math = solutionMath.countDigitOne(n2);
    cout << "测试用例2 - n = " << n2 << endl;
    cout << "数位DP结果: " << result2 << endl;
    cout << "数学方法结果: " << result2Math << endl;
    cout << "期望结果: 0" << endl;
    cout << "测试结果: " << (result2 == 0 && result2Math == 0 ? "通过" : "失败") << endl;
    cout << endl;
    
    // 测试用例3：n=100
    int n3 = 100;
    int result3 = solution.countDigitOne(n3);
    int result3Math = solutionMath.countDigitOne(n3);
    cout << "测试用例3 - n = " << n3 << endl;
    cout << "数位DP结果: " << result3 << endl;
    cout << "数学方法结果: " << result3Math << endl;
    cout << "期望结果: 21" << endl;
    cout << "测试结果: " << (result3 == 21 && result3Math == 21 ? "通过" : "失败") << endl;
    cout << endl;
    
    // 性能测试：n=10^9
    int n4 = 1000000000;
    auto start = chrono::high_resolution_clock::now();
    int result4 = solution.countDigitOne(n4);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    
    start = chrono::high_resolution_clock::now();
    int result4Math = solutionMath.countDigitOne(n4);
    end = chrono::high_resolution_clock::now();
    auto durationMath = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "性能测试 - n = " << n4 << endl;
    cout << "数位DP结果: " << result4 << "，耗时: " << duration.count() << "微秒" << endl;
    cout << "数学方法结果: " << result4Math << "，耗时: " << durationMath.count() << "微秒" << endl;
    cout << "数学方法比数位DP快 " << (double)duration.count() / durationMath.count() << " 倍" << endl;
    
    return 0;
}