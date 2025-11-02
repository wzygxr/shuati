#include <iostream>
#include <cstring>
using namespace std;

/**
 * 最长回文子序列（Longest Palindromic Subsequence） - C++实现
 * 
 * 题目描述：
 * 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
 * 
 * 题目来源：LeetCode 516. 最长回文子序列
 * 题目链接：https://leetcode.cn/problems/longest-palindromic-subsequence/
 * 
 * 解题思路分析：
 * 1. 严格位置依赖的动态规划：自底向上填表，避免递归开销
 * 2. 空间优化版本：利用滚动数组思想，只保存必要的状态
 * 
 * 时间复杂度分析：
 * - 动态规划：O(n²) - 需要遍历所有可能的子串区间
 * - 空间优化DP：O(n²) - 需要遍历所有可能的子串区间
 * 
 * 空间复杂度分析：
 * - 动态规划：O(n²) - DP数组
 * - 空间优化DP：O(n) - 只使用一维数组
 * 
 * 是否最优解：是 - 区间动态规划是解决此类回文问题的标准方法
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性，处理空字符串等特殊情况
 * 2. 边界处理：处理单字符、空字符串等边界情况
 * 3. 性能优化：空间压缩降低内存使用，减少不必要的计算
 * 4. 可测试性：提供完整的测试用例，覆盖各种边界场景
 * 
 * C++特性：
 * - 使用字符数组而非string，性能更高但需要手动管理内存
 * - 需要手动计算字符串长度
 * - 使用静态数组，需要预设最大尺寸
 */

#define MAXN 1000  // 假设字符串最大长度为1000

class Code04_LongestPalindromicSubsequence {
public:
    /**
     * 方法3：严格位置依赖的动态规划
     * 
     * 算法思想：自底向上填表，从小区间开始逐步计算大区间的最长回文子序列长度
     * 通过明确的递推关系，避免递归开销，提高算法效率
     * 
     * 状态定义：dp[l][r] 表示字符串s[l...r]的最长回文子序列长度
     * 状态转移方程：
     * - 当l == r：dp[l][r] = 1（单个字符）
     * - 当l+1 == r：dp[l][r] = 2（如果s[l] == s[r]）或1（否则）
     * - 当r-l > 1：
     *   - 如果s[l] == s[r]：dp[l][r] = dp[l+1][r-1] + 2
     *   - 否则：dp[l][r] = max(dp[l+1][r], dp[l][r-1])
     * 
     * 时间复杂度：O(n²) - 需要遍历所有可能的子串区间
     * 空间复杂度：O(n²) - 使用二维DP数组
     * 
     * @param str 字符串
     * @return 最长回文子序列长度
     * 
     * C++注意事项：
     * - 使用静态数组，需要预设最大尺寸MAXN
     * - 需要手动计算字符串长度
     * - 注意数组边界检查，避免越界访问
     */
    static int longestPalindromeSubseq3(char* str) {
        // 输入验证
        if (str == nullptr) {
            return 0;
        }
        
        // 获取字符串长度
        int n = 0;
        while (str[n] != '\0') n++;
        
        // 处理空字符串情况
        if (n == 0) {
            return 0;
        }
        
        // 创建DP数组
        int dp[MAXN][MAXN];
        
        // 初始化DP数组
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = 0;
            }
        }
        
        // 按区间长度从小到大填表
        for (int l = n - 1; l >= 0; l--) {
            // 初始化长度为1的区间
            dp[l][l] = 1;
            
            // 初始化长度为2的区间
            if (l + 1 < n) {
                dp[l][l + 1] = (str[l] == str[l + 1]) ? 2 : 1;
            }
            
            // 填充长度大于2的区间
            for (int r = l + 2; r < n; r++) {
                if (str[l] == str[r]) {
                    // 如果两端字符相等
                    dp[l][r] = 2 + dp[l + 1][r - 1];
                } else {
                    // 如果两端字符不相等
                    if (dp[l + 1][r] > dp[l][r - 1]) {
                        dp[l][r] = dp[l + 1][r];
                    } else {
                        dp[l][r] = dp[l][r - 1];
                    }
                }
            }
        }
        
        // 返回整个字符串的最长回文子序列长度
        return dp[0][n - 1];
    }

    /**
     * 方法4：严格位置依赖的动态规划 + 空间压缩
     * 
     * 算法思想：利用滚动数组思想，将空间复杂度从O(n²)优化到O(n)
     * 观察发现：在计算第l行时，只需要第l+1行的dp值和当前行已经计算的部分
     * 因此可以使用一维数组来存储状态，通过滚动更新来节省空间
     * 
     * 状态定义：dp[r] 表示当前行第r列的最长回文子序列长度
     * 状态转移：
     * - 当l == r：dp[r] = 1
     * - 当l+1 == r：dp[r] = 2（如果s[l] == s[r]）或1（否则）
     * - 当r-l > 1：
     *   - 如果s[l] == s[r]：dp[r] = leftDown + 2
     *   - 否则：dp[r] = max(dp[r], dp[r-1])
     * 
     * 时间复杂度：O(n²) - 需要遍历所有可能的子串区间
     * 空间复杂度：O(n) - 只使用一维数组
     * 
     * @param str 字符串
     * @return 最长回文子序列长度
     * 
     * C++优化技巧：
     * - 使用一维静态数组，避免动态内存分配
     * - 通过leftDown变量保存dp[l+1][r-1]的值
     * - 注意数组边界，确保不越界访问
     */
    static int longestPalindromeSubseq4(char* str) {
        // 输入验证
        if (str == nullptr) {
            return 0;
        }
        
        // 获取字符串长度
        int n = 0;
        while (str[n] != '\0') n++;
        
        // 处理空字符串情况
        if (n == 0) {
            return 0;
        }
        
        // 创建一维DP数组
        int dp[MAXN];
        
        // 初始化DP数组
        for (int i = 0; i < n; i++) {
            dp[i] = 0;
        }
        
        // 按区间长度从小到大填表
        for (int l = n - 1, leftDown = 0, backup; l >= 0; l--) {
            // dp[l] : 想象中的dp[l][l]
            dp[l] = 1;
            
            // 初始化长度为2的区间
            if (l + 1 < n) {
                leftDown = dp[l + 1];
                // dp[l+1] : 想象中的dp[l][l+1]
                dp[l + 1] = (str[l] == str[l + 1]) ? 2 : 1;
            }
            
            // 填充长度大于2的区间
            for (int r = l + 2; r < n; r++) {
                backup = dp[r];
                if (str[l] == str[r]) {
                    // 如果两端字符相等
                    dp[r] = 2 + leftDown;
                } else {
                    // 如果两端字符不相等
                    if (dp[r] < dp[r - 1]) {
                        dp[r] = dp[r - 1];
                    }
                }
                leftDown = backup;
            }
        }
        
        // 返回整个字符串的最长回文子序列长度
        return dp[n - 1];
    }
    
    /**
     * 测试方法：验证最长回文子序列算法的正确性
     * 
     * 测试用例设计：
     * 1. 正常情况测试：存在回文子序列
     * 2. 边界情况测试：单字符、空字符串等
     * 3. 特殊情况测试：全相同字符、无回文等
     * 4. 复杂情况测试：长字符串
     * 
     * 测试目的：确保各种实现方法结果一致，验证算法正确性
     */
    static void test() {
        cout << "=== 最长回文子序列算法测试 ===" << endl;
        
        // 测试用例1：正常情况 - 存在回文子序列
        char str1[] = "bbbab";
        cout << "测试用例1 - 正常情况:" << endl;
        cout << "字符串: " << str1 << endl;
        cout << "动态规划: " << longestPalindromeSubseq3(str1) << endl;
        cout << "空间优化DP: " << longestPalindromeSubseq4(str1) << endl;
        cout << "预期结果: 4" << endl;
        cout << endl;
        
        // 测试用例2：存在回文子序列
        char str2[] = "cbbd";
        cout << "测试用例2 - 存在回文子序列:" << endl;
        cout << "字符串: " << str2 << endl;
        cout << "动态规划: " << longestPalindromeSubseq3(str2) << endl;
        cout << "空间优化DP: " << longestPalindromeSubseq4(str2) << endl;
        cout << "预期结果: 2" << endl;
        cout << endl;
        
        // 测试用例3：单字符
        char str3[] = "a";
        cout << "测试用例3 - 单字符:" << endl;
        cout << "字符串: " << str3 << endl;
        cout << "动态规划: " << longestPalindromeSubseq3(str3) << endl;
        cout << "空间优化DP: " << longestPalindromeSubseq4(str3) << endl;
        cout << "预期结果: 1" << endl;
        cout << endl;
        
        // 测试用例4：全相同字符
        char str4[] = "aaaa";
        cout << "测试用例4 - 全相同字符:" << endl;
        cout << "字符串: " << str4 << endl;
        cout << "动态规划: " << longestPalindromeSubseq3(str4) << endl;
        cout << "空间优化DP: " << longestPalindromeSubseq4(str4) << endl;
        cout << "预期结果: 4" << endl;
        cout << endl;
        
        // 测试用例5：空字符串
        char str5[] = "";
        cout << "测试用例5 - 空字符串:" << endl;
        cout << "字符串: \"\"" << endl;
        cout << "动态规划: " << longestPalindromeSubseq3(str5) << endl;
        cout << "空间优化DP: " << longestPalindromeSubseq4(str5) << endl;
        cout << "预期结果: 0" << endl;
        
        cout << endl << "=== 测试完成 ===" << endl;
    }
};

// 主函数：运行测试用例
int main() {
    Code04_LongestPalindromicSubsequence::test();
    return 0;
}