/**
 * LeetCode 600. 不含连续1的非负整数 - C++实现
 * 题目链接：https://leetcode.cn/problems/non-negative-integers-without-consecutive-ones/
 * 
 * 题目描述：
 * 给定一个正整数 n，统计在 [0, n] 范围的非负整数中，有多少个整数的二进制表示中不存在连续的1。
 * 
 * 解题思路：
 * 使用数位动态规划（Digit DP）解决该问题。核心思想是逐位处理二进制数字，确保不出现连续的1。
 * 
 * 算法分析：
 * 时间复杂度：O(L) 其中L是数字n的二进制位数
 * 空间复杂度：O(L) 用于存储DP状态
 * 
 * C++实现特点：
 * 1. 使用bitset或手动处理二进制位
 * 2. 使用vector进行动态内存管理
 * 3. 优化递归深度和内存使用
 */

#include <iostream>
#include <vector>
#include <string>
#include <bitset>
#include <chrono>

using namespace std;

class Solution {
private:
    // 数位DP记忆化数组：dp[pos][pre][limit]
    vector<vector<vector<int>>> dp;
    vector<int> bits;
    int len;
    
    /**
     * 数位DP核心递归函数
     * 
     * @param pos 当前处理的位置
     * @param pre 前一位数字（0或1）
     * @param limit 是否受到上界限制
     * @param lead 是否有前导零
     * @return 从当前状态开始，满足条件的数字个数
     */
    int dfs(int pos, int pre, bool limit, bool lead) {
        // 递归终止条件：处理完所有二进制位
        if (pos == len) {
            return 1;
        }
        
        // 记忆化搜索优化
        if (!limit && !lead) {
            int limitIndex = limit ? 1 : 0;
            if (dp[pos][pre][limitIndex] != -1) {
                return dp[pos][pre][limitIndex];
            }
        }
        
        int result = 0;
        int maxDigit = limit ? bits[pos] : 1;
        
        for (int digit = 0; digit <= maxDigit; digit++) {
            // 约束条件：不能有连续的1
            if (pre == 1 && digit == 1) {
                continue;
            }
            
            bool newLimit = limit && (digit == maxDigit);
            bool newLead = lead && (digit == 0);
            
            result += dfs(pos + 1, digit, newLimit, newLead);
        }
        
        // 记忆化存储
        if (!limit && !lead) {
            int limitIndex = limit ? 1 : 0;
            dp[pos][pre][limitIndex] = result;
        }
        
        return result;
    }

public:
    /**
     * 主函数：统计在[0, n]范围内二进制表示中不含连续1的非负整数个数
     * 
     * @param n 目标数字
     * @return 满足条件的数字个数
     */
    int findIntegers(int n) {
        if (n < 0) return 0;
        if (n == 0) return 1;
        
        // 将数字转换为二进制字符串
        string binaryStr = bitset<32>(n).to_string();
        // 去除前导零
        binaryStr = binaryStr.substr(binaryStr.find('1'));
        len = binaryStr.length();
        bits.resize(len);
        
        for (int i = 0; i < len; i++) {
            bits[i] = binaryStr[i] - '0';
        }
        
        // 初始化DP数组：len × 2 × 2
        dp.resize(len, vector<vector<int>>(2, vector<int>(2, -1)));
        
        return dfs(0, 0, true, true);
    }
};

/**
 * 斐波那契数列方法 - 替代解法，时间复杂度O(L)，空间复杂度O(1)
 * 数学发现：不含连续1的二进制数个数满足斐波那契数列规律
 */
class SolutionFibonacci {
public:
    int findIntegers(int n) {
        if (n < 0) return 0;
        if (n == 0) return 1;
        
        // 预处理斐波那契数列
        int fib[32] = {0};
        fib[0] = 1;
        fib[1] = 2;
        for (int i = 2; i < 32; i++) {
            fib[i] = fib[i-1] + fib[i-2];
        }
        
        string binary = bitset<32>(n).to_string();
        binary = binary.substr(binary.find('1'));
        int len = binary.length();
        int result = 0;
        bool prevBit = false;
        
        for (int i = 0; i < len; i++) {
            if (binary[i] == '1') {
                result += fib[len - i - 1];
                if (prevBit) {
                    return result;
                }
                prevBit = true;
            } else {
                prevBit = false;
            }
        }
        
        return result + 1;
    }
};

// 测试函数
int main() {
    Solution solutionDP;
    SolutionFibonacci solutionFib;
    
    // 测试用例1：n=5
    int n1 = 5;
    int result1 = solutionDP.findIntegers(n1);
    int result1Fib = solutionFib.findIntegers(n1);
    cout << "测试用例1 - n = " << n1 << " (二进制: " << bitset<32>(n1).to_string().substr(32-3) << ")" << endl;
    cout << "数位DP结果: " << result1 << endl;
    cout << "斐波那契方法结果: " << result1Fib << endl;
    cout << "期望结果: 5" << endl;
    cout << "测试结果: " << (result1 == 5 && result1Fib == 5 ? "通过" : "失败") << endl;
    cout << endl;
    
    // 测试用例2：n=1
    int n2 = 1;
    int result2 = solutionDP.findIntegers(n2);
    int result2Fib = solutionFib.findIntegers(n2);
    cout << "测试用例2 - n = " << n2 << " (二进制: " << bitset<32>(n2).to_string().substr(32-1) << ")" << endl;
    cout << "数位DP结果: " << result2 << endl;
    cout << "斐波那契方法结果: " << result2Fib << endl;
    cout << "期望结果: 2" << endl;
    cout << "测试结果: " << (result2 == 2 && result2Fib == 2 ? "通过" : "失败") << endl;
    cout << endl;
    
    // 测试用例3：n=2
    int n3 = 2;
    int result3 = solutionDP.findIntegers(n3);
    int result3Fib = solutionFib.findIntegers(n3);
    cout << "测试用例3 - n = " << n3 << " (二进制: " << bitset<32>(n3).to_string().substr(32-2) << ")" << endl;
    cout << "数位DP结果: " << result3 << endl;
    cout << "斐波那契方法结果: " << result3Fib << endl;
    cout << "期望结果: 3" << endl;
    cout << "测试结果: " << (result3 == 3 && result3Fib == 3 ? "通过" : "失败") << endl;
    cout << endl;
    
    // 性能测试：n=10^9
    int n4 = 1000000000;
    
    auto start = chrono::high_resolution_clock::now();
    int result4 = solutionDP.findIntegers(n4);
    auto end = chrono::high_resolution_clock::now();
    auto durationDP = chrono::duration_cast<chrono::microseconds>(end - start);
    
    start = chrono::high_resolution_clock::now();
    int result4Fib = solutionFib.findIntegers(n4);
    end = chrono::high_resolution_clock::now();
    auto durationFib = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "性能测试 - n = " << n4 << endl;
    cout << "数位DP结果: " << result4 << "，耗时: " << durationDP.count() << "微秒" << endl;
    cout << "斐波那契方法结果: " << result4Fib << "，耗时: " << durationFib.count() << "微秒" << endl;
    cout << "斐波那契方法比数位DP快 " << (double)durationDP.count() / durationFib.count() << " 倍" << endl;
    
    return 0;
}