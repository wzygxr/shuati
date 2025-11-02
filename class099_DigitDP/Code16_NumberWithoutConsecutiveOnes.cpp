#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <bitset>

using namespace std;

/**
 * 不含连续1的非负整数
 * 题目来源：LeetCode 600. 不含连续1的非负整数
 * 题目链接：https://leetcode.cn/problems/non-negative-integers-without-consecutive-ones/
 * 
 * 题目描述：
 * 给定一个正整数 n，返回在 [0, n] 范围内不含连续1的非负整数的个数。
 * 
 * 解题思路：
 * 1. 数位DP方法：使用数位DP框架，逐位确定二进制数字
 * 2. 状态设计需要记录：
 *    - 当前处理位置
 *    - 是否受上界限制
 *    - 前一位是否为1
 * 3. 关键点：当前位不能与前一位同时为1
 * 
 * 时间复杂度分析：
 * - 状态数：二进制位数 × 2 × 2 ≈ 32 × 4 = 128
 * - 每个状态处理2种选择
 * - 总复杂度：O(256) 非常高效
 * 
 * 空间复杂度分析：
 * - 记忆化数组：32 × 2 × 2 ≈ 128个状态
 * 
 * 最优解分析：
 * 这是标准的最优解，利用数位DP处理二进制约束条件
 */

class Solution {
public:
    /**
     * 计算[0, n]中不含连续1的二进制数的个数
     * 时间复杂度: O(log n)
     * 空间复杂度: O(log n)
     */
    int findIntegers(int n) {
        if (n == 0) return 1;
        if (n == 1) return 2;
        
        // 将n转换为二进制字符串
        string binary = bitset<32>(n).to_string();
        // 去除前导零
        size_t start = binary.find('1');
        if (start == string::npos) {
            binary = "0";
        } else {
            binary = binary.substr(start);
        }
        
        int len = binary.length();
        
        // 记忆化数组：dp[pos][isLimit][lastIsOne]
        vector<vector<vector<int>>> dp(len, vector<vector<int>>(2, vector<int>(2, -1)));
        
        return dfs(binary, 0, true, false, dp);
    }
    
    /**
     * 数学方法（斐波那契数列）- 更高效的解法
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * 解题思路：
     * 1. 观察发现，不含连续1的二进制数个数满足斐波那契数列
     * 2. 对于k位二进制数，有效数字个数为fib(k+2)
     * 3. 利用这个性质可以快速计算
     */
    int findIntegersMath(int n) {
        if (n == 0) return 1;
        if (n == 1) return 2;
        
        // 预处理斐波那契数列
        vector<int> fib(32, 0);
        fib[0] = 1;
        fib[1] = 2;
        for (int i = 2; i < 32; i++) {
            fib[i] = fib[i-1] + fib[i-2];
        }
        
        string binary = bitset<32>(n).to_string();
        size_t start = binary.find('1');
        if (start == string::npos) {
            binary = "0";
        } else {
            binary = binary.substr(start);
        }
        
        int len = binary.length();
        int ans = 0;
        bool prevBit = false; // 前一位是否为1
        
        for (int i = 0; i < len; i++) {
            if (binary[i] == '1') {
                // 如果当前位为1，可以选择填0，后面位可以任意填
                ans += fib[len - i - 1];
                
                // 如果前一位也是1，说明出现了连续1，后面的数字都不满足条件
                if (prevBit) {
                    return ans;
                }
                prevBit = true;
            } else {
                prevBit = false;
            }
        }
        
        // 加上n本身（如果n本身满足条件）
        return ans + 1;
    }
    
private:
    /**
     * 数位DP递归函数（二进制版本）
     * 
     * @param binary 二进制字符串
     * @param pos 当前处理位置
     * @param isLimit 是否受到上界限制
     * @param lastIsOne 前一位是否为1
     * @param dp 记忆化数组
     * @return 满足条件的数字个数
     */
    int dfs(const string& binary, int pos, bool isLimit, bool lastIsOne, 
            vector<vector<vector<int>>>& dp) {
        // 递归终止条件：处理完所有二进制位
        if (pos == binary.length()) {
            return 1; // 成功构造一个有效数字
        }
        
        // 记忆化搜索
        int limitIndex = isLimit ? 1 : 0;
        int lastIndex = lastIsOne ? 1 : 0;
        if (dp[pos][limitIndex][lastIndex] != -1) {
            return dp[pos][limitIndex][lastIndex];
        }
        
        int ans = 0;
        
        // 确定当前位可选数字范围（二进制只有0和1）
        int up = isLimit ? (binary[pos] - '0') : 1;
        
        // 枚举当前位可选数字
        for (int d = 0; d <= up; d++) {
            // 检查约束条件：不能有连续的1
            if (lastIsOne && d == 1) {
                continue; // 连续1，跳过
            }
            
            // 递归处理下一位
            ans += dfs(binary, pos + 1, isLimit && (d == up), d == 1, dp);
        }
        
        // 记忆化存储
        dp[pos][limitIndex][lastIndex] = ans;
        return ans;
    }
};

/**
 * 单元测试函数
 */
void testFindIntegers() {
    cout << "=== 测试不含连续1的非负整数 ===" << endl;
    
    Solution sol;
    
    // 测试用例1: 小数字
    int n1 = 5;
    int result1 = sol.findIntegers(n1);
    int result1Math = sol.findIntegersMath(n1);
    cout << "n = " << n1 << endl;
    cout << "DP结果: " << result1 << endl;
    cout << "数学结果: " << result1Math << endl;
    cout << "结果一致: " << (result1 == result1Math) << endl;
    cout << "预期: [0,5]中不含连续1的数字有0,1,2,4,5共5个" << endl;
    cout << endl;
    
    // 测试用例2: 中等数字
    int n2 = 10;
    int result2 = sol.findIntegers(n2);
    int result2Math = sol.findIntegersMath(n2);
    cout << "n = " << n2 << endl;
    cout << "DP结果: " << result2 << endl;
    cout << "数学结果: " << result2Math << endl;
    cout << "结果一致: " << (result2 == result2Math) << endl;
    cout << endl;
    
    // 测试用例3: 边界情况
    int n3 = 1;
    int result3 = sol.findIntegers(n3);
    int result3Math = sol.findIntegersMath(n3);
    cout << "n = " << n3 << endl;
    cout << "DP结果: " << result3 << endl;
    cout << "数学结果: " << result3Math << endl;
    cout << "结果一致: " << (result3 == result3Math) << endl;
    cout << "预期: [0,1]中所有数字都满足，共2个" << endl;
    cout << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    Solution sol;
    vector<int> testCases = {100, 1000, 10000, 100000, 1000000, 10000000};
    
    for (int n : testCases) {
        auto startTimeDP = chrono::high_resolution_clock::now();
        int resultDP = sol.findIntegers(n);
        auto endTimeDP = chrono::high_resolution_clock::now();
        
        auto startTimeMath = chrono::high_resolution_clock::now();
        int resultMath = sol.findIntegersMath(n);
        auto endTimeMath = chrono::high_resolution_clock::now();
        
        auto timeDP = chrono::duration_cast<chrono::nanoseconds>(endTimeDP - startTimeDP);
        auto timeMath = chrono::duration_cast<chrono::nanoseconds>(endTimeMath - startTimeMath);
        
        cout << "n = " << n << endl;
        cout << "DP方法耗时: " << timeDP.count() << "ns" << endl;
        cout << "数学方法耗时: " << timeMath.count() << "ns" << endl;
        cout << "加速比: " << (double)timeDP.count() / timeMath.count() << "倍" << endl;
        cout << "结果一致: " << (resultDP == resultMath) << endl;
        cout << endl;
    }
}

/**
 * 调试函数：验证特定范围内的结果
 */
void debugFindIntegers() {
    cout << "=== 调试不含连续1的非负整数 ===" << endl;
    
    Solution sol;
    
    for (int n = 0; n <= 20; n++) {
        int count = 0;
        string validNumbers;
        
        for (int i = 0; i <= n; i++) {
            // 检查二进制表示是否包含"11"
            bool hasConsecutiveOnes = false;
            int temp = i;
            int lastBit = 0;
            while (temp > 0) {
                int bit = temp & 1;
                if (bit == 1 && lastBit == 1) {
                    hasConsecutiveOnes = true;
                    break;
                }
                lastBit = bit;
                temp >>= 1;
            }
            
            if (!hasConsecutiveOnes) {
                count++;
                if (validNumbers.length() < 100) { // 限制输出长度
                    validNumbers += to_string(i) + " ";
                }
            }
        }
        
        int dpResult = sol.findIntegers(n);
        int mathResult = sol.findIntegersMath(n);
        
        cout << "n = " << n << ", 有效数字个数: " << count << endl;
        cout << "DP结果: " << dpResult << ", 数学结果: " << mathResult << endl;
        cout << "结果一致: " << (count == dpResult && dpResult == mathResult) << endl;
        
        if (n <= 10) {
            cout << "有效数字: " << validNumbers << endl;
        }
        cout << endl;
    }
}

/**
 * 工程化考量总结：
 * 1. 两种解法：提供DP和数学两种解法，便于理解和选择
 * 2. 性能优化：数学方法更高效，DP方法更通用
 * 3. 边界处理：正确处理n=0和n=1的情况
 * 4. 状态设计：合理设计状态参数，减少状态数
 * 5. 二进制处理：使用bitset进行二进制转换
 * 
 * 算法特色：
 * 1. 二进制处理：针对二进制数的特殊约束
 * 2. 斐波那契性质：发现并利用数学规律
 * 3. 记忆化搜索：DP解法避免重复计算
 * 4. 提前终止：数学解法在发现连续1时提前返回
 * 
 * C++实现特点：
 * 1. 使用bitset进行二进制转换
 * 2. 使用vector代替原生数组
 * 3. 提供完整的测试框架
 * 4. 使用chrono进行性能测试
 */

int main() {
    // 运行功能测试
    testFindIntegers();
    
    // 运行性能测试
    performanceTest();
    
    // 调试模式
    debugFindIntegers();
    
    // 边界测试
    cout << "=== 边界测试 ===" << endl;
    Solution sol;
    cout << "n=0: " << sol.findIntegers(0) << endl;
    cout << "n=1: " << sol.findIntegers(1) << endl;
    cout << "n=2: " << sol.findIntegers(2) << endl;
    cout << "n=3: " << sol.findIntegers(3) << endl;
    
    return 0;
}