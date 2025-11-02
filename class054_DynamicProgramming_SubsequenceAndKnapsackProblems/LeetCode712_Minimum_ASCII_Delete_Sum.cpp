// LeetCode 712. 两个字符串的最小ASCII删除和
// 给定两个字符串s1 和 s2，返回使两个字符串相等所需删除字符的 ASCII 值的最小和。
// 测试链接 : https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/

/*
 * 算法详解：两个字符串的最小ASCII删除和（LeetCode 712）
 * 
 * 问题描述：
 * 给定两个字符串s1 和 s2，返回使两个字符串相等所需删除字符的 ASCII 值的最小和。
 * 
 * 算法思路：
 * 这个问题可以看作是LCS问题的变种，但目标函数从最大化长度变为最小化ASCII值和。
 * 1. 计算s1和s2的最大ASCII公共子序列值和
 * 2. 计算s1中所有字符的ASCII值和
 * 3. 计算s2中所有字符的ASCII值和
 * 4. 最小删除和 = s1的ASCII和 + s2的ASCII和 - 2*最大ASCII公共子序列值和
 * 
 * 时间复杂度分析：
 * 1. 计算最大ASCII公共子序列：O(m*n)
 * 2. 计算字符串ASCII和：O(m+n)
 * 3. 总体时间复杂度：O(m*n)
 * 
 * 空间复杂度分析：
 * 1. dp数组：O(m*n)
 * 2. 总体空间复杂度：O(m*n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入是否为空
 * 2. 边界处理：正确处理空字符串的情况
 * 3. 整数溢出：注意ASCII值累加可能导致的整数溢出
 * 
 * 极端场景验证：
 * 1. 输入字符串长度达到边界情况
 * 2. 两个字符串完全相同的情况
 * 3. 两个字符串完全不同的情况
 * 4. 一个字符串为空的情况
 * 5. 两个字符串都为空的情况
 */

// 由于环境限制，此处只提供算法核心实现思路，不包含完整的可编译代码
// 在实际使用中，需要根据具体环境添加适当的头文件和类型定义

/*
int minimumDeleteSum(char* s1, char* s2) {
    // 异常处理：检查输入是否为空
    if (s1 == 0 || s2 == 0) {
        return 0;
    }
    
    int m = strlen(s1);
    int n = strlen(s2);
    
    if (m == 0) {
        return calculateASCIISum(s2);
    }
    
    if (n == 0) {
        return calculateASCIISum(s1);
    }
    
    // 计算最大ASCII公共子序列值和
    int maxASCIICommonSubsequence = maxASCIICommonSubsequence(s1, s2);
    
    // 计算两个字符串的ASCII值和
    int asciiSum1 = calculateASCIISum(s1);
    int asciiSum2 = calculateASCIISum(s2);
    
    // 返回最小删除和
    return asciiSum1 + asciiSum2 - 2 * maxASCIICommonSubsequence;
}

// 计算字符串中所有字符的ASCII值和
int calculateASCIISum(char* s) {
    int sum = 0;
    int len = strlen(s);
    for (int i = 0; i < len; i++) {
        sum += (int) s[i];
    }
    return sum;
}

// 计算两个字符串的最大ASCII公共子序列值和
int maxASCIICommonSubsequence(char* s1, char* s2) {
    int m = strlen(s1);
    int n = strlen(s2);
    
    // dp[i][j] 表示 s1[0..i-1] 和 s2[0..j-1] 的最大ASCII公共子序列值和
    int dp[501][501] = {0}; // 假设最大长度为500
    
    // 填充dp表
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (s1[i - 1] == s2[j - 1]) {
                // 字符相同，将ASCII值加到公共子序列值和中
                dp[i][j] = dp[i - 1][j - 1] + (int) s1[i - 1];
            } else {
                // 字符不同，选择值和较大的情况
                int a = dp[i - 1][j];
                int b = dp[i][j - 1];
                dp[i][j] = (a > b) ? a : b;
            }
        }
    }
    
    return dp[m][n];
}

// 直接使用动态规划解决最小ASCII删除和问题（不通过转换）
int minimumDeleteSumDirect(char* s1, char* s2) {
    // 异常处理：检查输入是否为空
    if (s1 == 0 || s2 == 0) {
        return 0;
    }
    
    int m = strlen(s1);
    int n = strlen(s2);
    
    if (m == 0) {
        return calculateASCIISum(s2);
    }
    
    if (n == 0) {
        return calculateASCIISum(s1);
    }
    
    // dp[i][j] 表示使s1[0..i-1]和s2[0..j-1]相等的最小ASCII删除和
    int dp[501][501] = {0}; // 假设最大长度为500
    
    // 初始化边界条件
    // 删除s1的所有字符
    for (int i = 1; i <= m; i++) {
        dp[i][0] = dp[i - 1][0] + (int) s1[i - 1];
    }
    // 删除s2的所有字符
    for (int j = 1; j <= n; j++) {
        dp[0][j] = dp[0][j - 1] + (int) s2[j - 1];
    }
    
    // 填充dp表
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (s1[i - 1] == s2[j - 1]) {
                // 字符相同，不需要删除
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                // 字符不同，选择删除和较小的操作
                // 1. 删除s1[i-1]：dp[i-1][j] + ASCII(s1[i-1])
                // 2. 删除s2[j-1]：dp[i][j-1] + ASCII(s2[j-1])
                int deleteS1 = dp[i - 1][j] + (int) s1[i - 1];
                int deleteS2 = dp[i][j - 1] + (int) s2[j - 1];
                dp[i][j] = (deleteS1 < deleteS2) ? deleteS1 : deleteS2;
            }
        }
    }
    
    return dp[m][n];
}
*/