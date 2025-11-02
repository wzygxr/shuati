#include <iostream>
#include <cstring>
using namespace std;

/**
 * 最长公共子序列（Longest Common Subsequence） - C++实现
 * 
 * 题目描述：
 * 给定两个字符串text1和text2，返回这两个字符串的最长公共子序列的长度。
 * 如果不存在公共子序列，返回0。
 * 两个字符串的公共子序列是这两个字符串所共同拥有的子序列。
 * 
 * 题目来源：LeetCode 1143. 最长公共子序列
 * 题目链接：https://leetcode.cn/problems/longest-common-subsequence/
 * 
 * 解题思路分析：
 * 1. 严格位置依赖的动态规划：自底向上填表，避免递归开销
 * 2. 空间优化版本：利用滚动数组思想，只保存必要的状态
 * 
 * 时间复杂度分析：
 * - 动态规划：O(m*n) - 需要遍历整个DP表
 * - 空间优化DP：O(m*n) - 需要遍历整个DP表
 * 
 * 空间复杂度分析：
 * - 动态规划：O(m*n) - DP数组
 * - 空间优化DP：O(min(m,n)) - 只使用一维数组
 * 
 * 是否最优解：是 - 动态规划是解决此类字符串匹配问题的标准方法
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

class Code03_LongestCommonSubsequence {
public:
    /**
     * 方法4：严格位置依赖的动态规划
     * 
     * 算法思想：自底向上填表，从起点开始逐步计算每个位置的最长公共子序列长度
     * 通过明确的递推关系，避免递归开销，提高算法效率
     * 
     * 状态定义：dp[i][j] 表示str1前i个字符与str2前j个字符的最长公共子序列长度
     * 状态转移方程：
     * - 当str1[i-1] == str2[j-1]：dp[i][j] = dp[i-1][j-1] + 1
     * - 当str1[i-1] != str2[j-1]：dp[i][j] = max(dp[i-1][j], dp[i][j-1])
     * 
     * 时间复杂度：O(m*n) - 需要遍历整个DP表
     * 空间复杂度：O(m*n) - 使用二维DP数组
     * 
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 最长公共子序列长度
     * 
     * C++注意事项：
     * - 使用静态数组，需要预设最大尺寸MAXN
     * - 需要手动计算字符串长度
     * - 注意数组边界检查，避免越界访问
     */
    static int longestCommonSubsequence4(char* str1, char* str2) {
        // 输入验证
        if (str1 == nullptr || str2 == nullptr) {
            return 0;
        }
        
        // 获取字符串长度
        int n = 0, m = 0;
        while (str1[n] != '\0') n++;
        while (str2[m] != '\0') m++;
        
        // 创建DP数组
        int dp[MAXN + 1][MAXN + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 0;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = 0;
        }
        
        // 填充DP表
        for (int len1 = 1; len1 <= n; len1++) {
            for (int len2 = 1; len2 <= m; len2++) {
                if (str1[len1 - 1] == str2[len2 - 1]) {
                    // 如果最后一个字符相等
                    dp[len1][len2] = 1 + dp[len1 - 1][len2 - 1];
                } else {
                    // 如果最后一个字符不相等
                    if (dp[len1 - 1][len2] > dp[len1][len2 - 1]) {
                        dp[len1][len2] = dp[len1 - 1][len2];
                    } else {
                        dp[len1][len2] = dp[len1][len2 - 1];
                    }
                }
            }
        }
        
        // 返回结果
        return dp[n][m];
    }

    /**
     * 方法5：严格位置依赖的动态规划 + 空间压缩
     * 
     * 算法思想：利用滚动数组思想，将空间复杂度从O(m*n)优化到O(min(m,n))
     * 观察发现：在计算第i行时，只需要第i-1行的dp值和当前行已经计算的部分
     * 因此可以使用一维数组来存储状态，通过滚动更新来节省空间
     * 
     * 状态定义：dp[j] 表示当前行第j列的最长公共子序列长度
     * 状态转移：
     * - 当str1[i-1] == str2[j-1]：dp[j] = 1 + leftUp
     * - 当str1[i-1] != str2[j-1]：dp[j] = max(dp[j], dp[j-1])
     * 
     * 时间复杂度：O(m*n) - 需要遍历整个DP表
     * 空间复杂度：O(min(m,n)) - 只使用一维数组
     * 
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 最长公共子序列长度
     * 
     * C++优化技巧：
     * - 使用一维静态数组，避免动态内存分配
     * - 选择较小的维度作为数组长度，进一步优化空间
     * - 注意数组边界，确保不越界访问
     */
    static int longestCommonSubsequence5(char* str1, char* str2) {
        // 输入验证
        if (str1 == nullptr || str2 == nullptr) {
            return 0;
        }
        
        // 获取字符串长度
        int n = 0, m = 0;
        while (str1[n] != '\0') n++;
        while (str2[m] != '\0') m++;
        
        // 为了优化空间，让较长的字符串作为s1
        char* s1 = str1;
        char* s2 = str2;
        int len1 = n, len2 = m;
        
        if (n < m) {
            s1 = str2;
            s2 = str1;
            len1 = m;
            len2 = n;
        }
        
        // 创建一维DP数组
        int dp[MAXN + 1];
        
        // 初始化DP数组
        for (int i = 0; i <= len2; i++) {
            dp[i] = 0;
        }
        
        // 填充DP数组
        for (int i = 1; i <= len1; i++) {
            int leftUp = 0, backup;
            for (int j = 1; j <= len2; j++) {
                backup = dp[j];
                if (s1[i - 1] == s2[j - 1]) {
                    // 如果最后一个字符相等
                    dp[j] = 1 + leftUp;
                } else {
                    // 如果最后一个字符不相等
                    if (dp[j] < dp[j - 1]) {
                        dp[j] = dp[j - 1];
                    }
                }
                leftUp = backup;
            }
        }
        
        // 返回结果
        return dp[len2];
    }
    
    /**
     * 测试方法：验证最长公共子序列算法的正确性
     * 
     * 测试用例设计：
     * 1. 正常情况测试：存在公共子序列
     * 2. 边界情况测试：不存在公共子序列
     * 3. 特殊情况测试：空字符串、单字符等
     * 4. 复杂情况测试：长字符串
     * 
     * 测试目的：确保各种实现方法结果一致，验证算法正确性
     */
    static void test() {
        cout << "=== 最长公共子序列算法测试 ===" << endl;
        
        // 测试用例1：正常情况 - 存在公共子序列
        char str1[] = "abcde";
        char str2[] = "ace";
        cout << "测试用例1 - 正常情况:" << endl;
        cout << "字符串1: " << str1 << endl;
        cout << "字符串2: " << str2 << endl;
        cout << "动态规划: " << longestCommonSubsequence4(str1, str2) << endl;
        cout << "空间优化DP: " << longestCommonSubsequence5(str1, str2) << endl;
        cout << "预期结果: 3" << endl;
        cout << endl;
        
        // 测试用例2：不存在公共子序列
        char str3[] = "abc";
        char str4[] = "def";
        cout << "测试用例2 - 不存在公共子序列:" << endl;
        cout << "字符串1: " << str3 << endl;
        cout << "字符串2: " << str4 << endl;
        cout << "动态规划: " << longestCommonSubsequence4(str3, str4) << endl;
        cout << "空间优化DP: " << longestCommonSubsequence5(str3, str4) << endl;
        cout << "预期结果: 0" << endl;
        cout << endl;
        
        // 测试用例3：相同字符串
        char str5[] = "abc";
        char str6[] = "abc";
        cout << "测试用例3 - 相同字符串:" << endl;
        cout << "字符串1: " << str5 << endl;
        cout << "字符串2: " << str6 << endl;
        cout << "动态规划: " << longestCommonSubsequence4(str5, str6) << endl;
        cout << "空间优化DP: " << longestCommonSubsequence5(str5, str6) << endl;
        cout << "预期结果: 3" << endl;
        cout << endl;
        
        // 测试用例4：空字符串
        char str7[] = "";
        char str8[] = "abc";
        cout << "测试用例4 - 空字符串:" << endl;
        cout << "字符串1: \"\"" << endl;
        cout << "字符串2: " << str8 << endl;
        cout << "动态规划: " << longestCommonSubsequence4(str7, str8) << endl;
        cout << "空间优化DP: " << longestCommonSubsequence5(str7, str8) << endl;
        cout << "预期结果: 0" << endl;
        cout << endl;
        
        // 测试用例5：单字符
        char str9[] = "a";
        char str10[] = "a";
        cout << "测试用例5 - 单字符:" << endl;
        cout << "字符串1: " << str9 << endl;
        cout << "字符串2: " << str10 << endl;
        cout << "动态规划: " << longestCommonSubsequence4(str9, str10) << endl;
        cout << "空间优化DP: " << longestCommonSubsequence5(str9, str10) << endl;
        cout << "预期结果: 1" << endl;
        
        cout << endl << "=== 测试完成 ===" << endl;
    }
};

// 主函数：运行测试用例
int main() {
    Code03_LongestCommonSubsequence::test();
    return 0;
}