// 最短公共超序列 (Shortest Common Supersequence)
// 给出两个字符串 str1 和 str2，返回同时以 str1 和 str2 作为子序列的最短字符串。
// 如果答案不止一个，则可以返回满足条件的任意一个答案。
// 测试链接 : https://leetcode.cn/problems/shortest-common-supersequence/

class Solution {
public:
    // 使用动态规划解决最短公共超序列问题
    // 核心思想：先求最长公共子序列，然后根据LCS构造最短公共超序列
    // 时间复杂度: O(m * n)
    // 空间复杂度: O(m * n)
    void shortestCommonSupersequence(char* str1, int str1Len, char* str2, int str2Len, char* result) {
        int m = str1Len;
        int n = str2Len;
        
        // dp[i][j] 表示str1前i个字符和str2前j个字符的最长公共子序列长度
        int dp[1001][1001];  // 假设字符串最大长度为1000
        
        // 初始化dp数组
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = 0;
            }
        }
        
        // 计算最长公共子序列长度
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1[i - 1] == str2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    int a = dp[i - 1][j];
                    int b = dp[i][j - 1];
                    dp[i][j] = (a > b) ? a : b;
                }
            }
        }
        
        // 根据dp数组构造最短公共超序列
        char temp[2001];  // 临时存储结果
        int tempIndex = 0;
        int i = m, j = n;
        
        // 从后往前构造结果
        while (i > 0 || j > 0) {
            // 如果其中一个字符串已经处理完，添加另一个字符串的剩余字符
            if (i == 0) {
                temp[tempIndex++] = str2[j - 1];
                j--;
            } else if (j == 0) {
                temp[tempIndex++] = str1[i - 1];
                i--;
            }
            // 如果当前字符相同，添加该字符并同时移动两个指针
            else if (str1[i - 1] == str2[j - 1]) {
                temp[tempIndex++] = str1[i - 1];
                i--;
                j--;
            }
            // 如果当前字符不同，根据dp值决定移动哪个指针
            else if (dp[i - 1][j] > dp[i][j - 1]) {
                temp[tempIndex++] = str1[i - 1];
                i--;
            } else {
                temp[tempIndex++] = str2[j - 1];
                j--;
            }
        }
        
        // 反转结果并返回
        for (int k = 0; k < tempIndex; k++) {
            result[k] = temp[tempIndex - 1 - k];
        }
        result[tempIndex] = '\0';  // 添加字符串结束符
    }
};