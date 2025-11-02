#include <iostream>
#include <vector>
#include <string>
#include <cstring>
#include <algorithm>
#include <chrono>
#include <cmath>

using namespace std;

/**
 * AtCoder ABC135 D - Digits Parade
 * 题目链接：https://atcoder.jp/contests/abc135/tasks/abc135_d
 * 
 * 题目描述：
 * 给定一个由数字和'?'组成的字符串S，'?'可以替换成0-9的任意数字。
 * 求有多少种替换方案使得结果能被13整除，结果对10^9+7取模。
 * 
 * 解题思路：
 * 1. 动态规划方法：使用DP框架，逐位确定数字
 * 2. 状态设计需要记录：
 *    - 当前处理位置
 *    - 当前数字对13的余数
 * 3. 关键点：'?'可以替换为0-9的任意数字
 * 
 * 时间复杂度分析：
 * - 状态数：字符串长度 × 13 ≈ 10^5 × 13 = 1.3×10^6
 * - 每个状态处理最多10种选择
 * - 总复杂度：O(13×10^5) 在可接受范围内
 * 
 * 空间复杂度分析：
 * - DP数组：10^5 × 13 ≈ 1.3MB
 * 
 * 最优解分析：
 * 这是标准的最优解，利用DP处理模运算和通配符替换
 */

class DigitsParade {
private:
    static const int MOD = 1000000007;
    static const int DIVISOR = 13;
    
public:
    /**
     * 计算有多少种替换方案使得结果能被13整除
     * 时间复杂度: O(n * 13)
     * 空间复杂度: O(n * 13)
     */
    int countDivisibleBy13(const string& s) {
        int n = s.length();
        
        // dp[i][r] 表示处理到第i位，当前余数为r的方案数
        vector<vector<long long>> dp(n + 1, vector<long long>(DIVISOR, 0));
        dp[0][0] = 1;  // 初始状态：余数为0有1种方案（空数字）
        
        // 从高位到低位动态规划
        for (int i = 0; i < n; i++) {
            for (int r = 0; r < DIVISOR; r++) {
                if (dp[i][r] == 0) continue;
                
                if (s[i] == '?') {
                    // '?'可以替换为0-9的任意数字
                    for (int d = 0; d <= 9; d++) {
                        int newR = (r * 10 + d) % DIVISOR;
                        dp[i + 1][newR] = (dp[i + 1][newR] + dp[i][r]) % MOD;
                    }
                } else {
                    // 固定数字
                    int d = s[i] - '0';
                    int newR = (r * 10 + d) % DIVISOR;
                    dp[i + 1][newR] = (dp[i + 1][newR] + dp[i][r]) % MOD;
                }
            }
        }
        
        return dp[n][0];
    }
    
    /**
     * 使用记忆化DFS的替代解法（更符合数位DP传统风格）
     * 时间复杂度: O(n * 13)
     * 空间复杂度: O(n * 13)
     */
    int countDivisibleBy13DFS(const string& s) {
        int n = s.length();
        vector<vector<long long>> memo(n, vector<long long>(DIVISOR, -1));
        
        return dfs(s, 0, 0, memo);
    }
    
private:
    long long dfs(const string& s, int pos, int remainder, vector<vector<long long>>& memo) {
        // 递归终止条件：处理完所有字符
        if (pos == s.length()) {
            return (remainder == 0) ? 1 : 0;
        }
        
        // 记忆化搜索
        if (memo[pos][remainder] != -1) {
            return memo[pos][remainder];
        }
        
        long long count = 0;
        
        if (s[pos] == '?') {
            // '?'可以替换为0-9的任意数字
            for (int d = 0; d <= 9; d++) {
                int newRemainder = (remainder * 10 + d) % DIVISOR;
                count = (count + dfs(s, pos + 1, newRemainder, memo)) % MOD;
            }
        } else {
            // 固定数字
            int d = s[pos] - '0';
            int newRemainder = (remainder * 10 + d) % DIVISOR;
            count = (count + dfs(s, pos + 1, newRemainder, memo)) % MOD;
        }
        
        // 记忆化存储
        memo[pos][remainder] = count;
        return count;
    }
};

/**
 * 单元测试函数
 */
void testDigitsParade() {
    cout << "=== 测试Digits Parade ===" << endl;
    
    DigitsParade dp;
    
    // 测试用例1: 简单情况
    string s1 = "??";
    int result1 = dp.countDivisibleBy13(s1);
    int result1DFS = dp.countDivisibleBy13DFS(s1);
    cout << "输入: " << s1 << endl;
    cout << "DP结果: " << result1 << endl;
    cout << "DFS结果: " << result1DFS << endl;
    cout << "结果一致: " << (result1 == result1DFS) << endl;
    cout << "预期: 100种组合中有几个能被13整除" << endl;
    cout << endl;
    
    // 测试用例2: 固定数字
    string s2 = "13";
    int result2 = dp.countDivisibleBy13(s2);
    int result2DFS = dp.countDivisibleBy13DFS(s2);
    cout << "输入: " << s2 << endl;
    cout << "DP结果: " << result2 << endl;
    cout << "DFS结果: " << result2DFS << endl;
    cout << "结果一致: " << (result2 == result2DFS) << endl;
    cout << "预期: 13能被13整除，所以为1" << endl;
    cout << endl;
    
    // 测试用例3: 混合情况
    string s3 = "1?2";
    int result3 = dp.countDivisibleBy13(s3);
    int result3DFS = dp.countDivisibleBy13DFS(s3);
    cout << "输入: " << s3 << endl;
    cout << "DP结果: " << result3 << endl;
    cout << "DFS结果: " << result3DFS << endl;
    cout << "结果一致: " << (result3 == result3DFS) << endl;
    cout << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    DigitsParade dp;
    
    // 生成测试用例
    string longString(1000, '?');
    
    // 测试DP方法
    auto startTimeDP = chrono::high_resolution_clock::now();
    int resultDP = dp.countDivisibleBy13(longString);
    auto endTimeDP = chrono::high_resolution_clock::now();
    
    // 测试DFS方法
    auto startTimeDFS = chrono::high_resolution_clock::now();
    int resultDFS = dp.countDivisibleBy13DFS(longString);
    auto endTimeDFS = chrono::high_resolution_clock::now();
    
    auto durationDP = chrono::duration_cast<chrono::milliseconds>(endTimeDP - startTimeDP);
    auto durationDFS = chrono::duration_cast<chrono::milliseconds>(endTimeDFS - startTimeDFS);
    
    cout << "字符串长度: " << longString.length() << endl;
    cout << "DP方法耗时: " << durationDP.count() << "ms" << endl;
    cout << "DFS方法耗时: " << durationDFS.count() << "ms" << endl;
    cout << "结果一致: " << (resultDP == resultDFS) << endl;
    cout << endl;
}

/**
 * 调试函数：验证特定字符串的替换方案
 */
void debugDigitsParade() {
    cout << "=== 调试Digits Parade ===" << endl;
    
    DigitsParade dp;
    
    vector<string> testCases = {
        "0", "1", "13", "26", "39", "52",
        "1?", "?3", "??", "1?3"
    };
    
    for (const string& s : testCases) {
        int result = dp.countDivisibleBy13(s);
        cout << "输入: " << s << ", 方案数: " << result << endl;
        
        // 对于短字符串，可以手动验证
        if (s.length() <= 2 && s.find('?') != string::npos) {
            cout << "  具体方案: ";
            int count = 0;
            int maxNum = pow(10, s.length());
            for (int i = 0; i < maxNum; i++) {
                string candidate = to_string(i);
                while (candidate.length() < s.length()) {
                    candidate = "0" + candidate;
                }
                
                bool match = true;
                for (int j = 0; j < s.length(); j++) {
                    if (s[j] != '?' && s[j] != candidate[j]) {
                        match = false;
                        break;
                    }
                }
                
                if (match && i % 13 == 0) {
                    cout << candidate << " ";
                    count++;
                }
            }
            cout << endl << "  手动计数: " << count << endl;
        }
        cout << endl;
    }
}

/**
 * 工程化考量总结：
 * 1. 模运算：结果对10^9+7取模，避免溢出
 * 2. 状态设计：合理设计状态参数，减少状态数
 * 3. 两种实现：提供DP和DFS两种解法，便于理解
 * 4. 性能优化：使用迭代DP避免递归栈开销
 * 5. 边界处理：正确处理空字符串和全'?'情况
 * 
 * 算法特色：
 * 1. 通配符处理：'?'可以替换为任意数字
 * 2. 模运算约束：结果必须能被13整除
 * 3. 动态规划：从高位到低位逐步计算
 * 4. 记忆化搜索：DFS解法更符合数位DP传统
 * 
 * C++实现特点：
 * 1. 使用vector代替数组，更安全
 * 2. 使用constexpr定义常量
 * 3. 避免使用全局变量
 * 4. 提供完整的测试框架
 */

int main() {
    // 运行功能测试
    testDigitsParade();
    
    // 运行性能测试
    performanceTest();
    
    // 调试模式
    debugDigitsParade();
    
    // 边界测试
    cout << "=== 边界测试 ===" << endl;
    DigitsParade dp;
    cout << "空字符串: " << dp.countDivisibleBy13("") << endl;
    cout << "单个'?': " << dp.countDivisibleBy13("?") << endl;
    cout << "全'?': " << dp.countDivisibleBy13("???") << endl;
    
    return 0;
}