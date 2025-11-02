#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <string>
#include <cstring>
using namespace std;

// LeetCode 664. 奇怪的打印机
// 打印机有以下两个特殊要求：每次打印一个字符序列；每次可以打印任意数量的相同字符。
// 测试链接: https://leetcode.cn/problems/strange-printer/
// 
// 解题思路:
// 1. 状态定义：dp[i][j]表示打印区间[i,j]所需的最小打印次数
// 2. 状态转移：考虑两种策略：单独打印首字符，或者与后面相同字符一起打印
// 3. 时间复杂度：O(n^3)
// 4. 空间复杂度：O(n^2)
//
// 工程化考量:
// 1. 异常处理：检查输入字符串合法性
// 2. 边界处理：处理空字符串和单字符情况
// 3. 性能优化：使用区间DP标准模板
// 4. 测试覆盖：设计全面的测试用例

/**
 * 区间DP解法
 * 时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
 * 空间复杂度: O(n^2) - dp数组占用空间
 * 
 * 解题思路:
 * 1. 状态定义：dp[i][j]表示打印字符串s在区间[i,j]所需的最小打印次数
 * 2. 状态转移：
 *    - 基础情况：dp[i][i] = 1（单个字符需要1次打印）
 *    - 如果s[i] == s[j]，则dp[i][j] = dp[i][j-1]（可以一起打印）
 *    - 否则，枚举分割点k：dp[i][j] = min(dp[i][k] + dp[k+1][j])
 * 3. 填表顺序：按区间长度从小到大
 */
int strangePrinter(string s) {
    // 异常处理
    if (s.empty()) {
        return 0;
    }
    
    int n = s.length();
    
    // 状态定义：dp[i][j]表示打印区间[i,j]所需的最小打印次数
    vector<vector<int>> dp(n, vector<int>(n, INT_MAX));
    
    // 初始化：单个字符需要1次打印
    for (int i = 0; i < n; i++) {
        dp[i][i] = 1;
    }
    
    // 枚举区间长度，从2开始
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            
            // 策略1：如果首尾字符相同，可以一起打印
            if (s[i] == s[j]) {
                dp[i][j] = dp[i][j - 1];
            } else {
                // 策略2：枚举分割点k，将区间分为[i,k]和[k+1,j]
                for (int k = i; k < j; k++) {
                    dp[i][j] = min(dp[i][j], dp[i][k] + dp[k + 1][j]);
                }
            }
        }
    }
    
    return dp[0][n - 1];
}

/**
 * 优化版本 - 减少不必要的分割点枚举
 * 时间复杂度: O(n^3) 但实际运行更快
 * 空间复杂度: O(n^2)
 * 
 * 优化思路:
 * 1. 当s[i] == s[k]时，可以优化状态转移
 * 2. 减少重复计算
 */
int strangePrinterOptimized(string s) {
    if (s.empty()) {
        return 0;
    }
    
    int n = s.length();
    vector<vector<int>> dp(n, vector<int>(n, INT_MAX));
    
    // 初始化
    for (int i = 0; i < n; i++) {
        dp[i][i] = 1;
    }
    
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            
            // 关键优化：如果s[i] == s[k]，可以优化转移
            for (int k = i; k < j; k++) {
                int temp = dp[i][k] + dp[k + 1][j];
                if (s[i] == s[k]) {
                    // 进一步优化：如果首字符与分割点字符相同
                    temp = min(temp, dp[i][k] + (k + 1 <= j ? dp[k + 1][j] - 1 : 0));
                }
                dp[i][j] = min(dp[i][j], temp);
            }
            
            // 特殊处理：首尾字符相同的情况
            if (s[i] == s[j]) {
                dp[i][j] = min(dp[i][j], dp[i][j - 1]);
            }
        }
    }
    
    return dp[0][n - 1];
}

/**
 * 记忆化搜索版本 - 递归+记忆化
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n^2)
 * 
 * 优点: 代码更直观，易于理解
 * 缺点: 递归深度可能较大
 */
int dfs(string& s, int i, int j, vector<vector<int>>& memo) {
    if (i > j) {
        return 0;
    }
    
    if (memo[i][j] != -1) {
        return memo[i][j];
    }
    
    // 基础情况：单个字符
    if (i == j) {
        return 1;
    }
    
    int result = INT_MAX;
    
    // 策略1：单独打印首字符，然后打印剩余部分
    result = min(result, 1 + dfs(s, i + 1, j, memo));
    
    // 策略2：如果首字符与后面某个字符相同，可以一起打印
    for (int k = i + 1; k <= j; k++) {
        if (s[i] == s[k]) {
            result = min(result, dfs(s, i, k - 1, memo) + dfs(s, k + 1, j, memo));
        }
    }
    
    memo[i][j] = result;
    return result;
}

int strangePrinterMemo(string s) {
    if (s.empty()) {
        return 0;
    }
    
    int n = s.length();
    vector<vector<int>> memo(n, vector<int>(n, -1));
    
    return dfs(s, 0, n - 1, memo);
}

/**
 * 单元测试方法
 */
void test() {
    // 测试用例1：示例输入
    string s1 = "aaabbb";
    int result1 = strangePrinter(s1);
    cout << "Test 1 - Input: " << s1 << ", Expected: 2, Actual: " << result1 << endl;
    
    // 测试用例2：单个字符
    string s2 = "a";
    int result2 = strangePrinter(s2);
    cout << "Test 2 - Input: " << s2 << ", Expected: 1, Actual: " << result2 << endl;
    
    // 测试用例3：所有字符相同
    string s3 = "aaaaaaaa";
    int result3 = strangePrinter(s3);
    cout << "Test 3 - Input: " << s3 << ", Expected: 1, Actual: " << result3 << endl;
    
    // 测试用例4：交替字符
    string s4 = "ababab";
    int result4 = strangePrinter(s4);
    cout << "Test 4 - Input: " << s4 << ", Expected: 4, Actual: " << result4 << endl;
    
    // 验证不同方法的正确性
    int result1_opt = strangePrinterOptimized(s1);
    int result1_memo = strangePrinterMemo(s1);
    cout << "Validation - Basic: " << result1 << ", Optimized: " << result1_opt << ", Memo: " << result1_memo << endl;
}

/**
 * 性能测试方法
 */
void performanceTest() {
    // 生成测试数据
    string testStr;
    for (int i = 0; i < 100; i++) {
        testStr += 'a' + i % 26;
    }
    
    auto start = chrono::high_resolution_clock::now();
    int result = strangePrinter(testStr);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "Performance Test - Length: " << testStr.length() 
         << ", Result: " << result << ", Time: " << duration.count() << "ms" << endl;
}

int main() {
    string s;
    getline(cin, s);
    
    int result = strangePrinter(s);
    cout << result << endl;
    
    return 0;
}