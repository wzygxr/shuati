// LeetCode 1092. 最短公共超序列
// 给你两个字符串 str1 和 str2，返回同时以 str1 和 str2 作为子序列的最短字符串。
// 如果答案不止一个，则可以返回满足条件的任意一个答案。
// 测试链接 : https://leetcode.cn/problems/shortest-common-supersequence/

/*
 * 算法详解：最短公共超序列（LeetCode 1092）
 * 
 * 问题描述：
 * 给你两个字符串 str1 和 str2，返回同时以 str1 和 str2 作为子序列的最短字符串。
 * 超序列是指包含给定序列为子序列的序列。
 * 
 * 算法思路：
 * 1. 首先计算str1和str2的最长公共子序列(LCS)
 * 2. 通过LCS构造最短公共超序列
 * 3. 使用双指针技术，分别指向str1和str2的开头
 * 4. 遍历LCS中的所有字符，对于每个字符：
 *    - 将str1中在该字符之前的部分添加到结果中
 *    - 将str2中在该字符之前的部分添加到结果中
 *    - 添加该字符本身
 * 5. 最后将str1和str2剩余的部分添加到结果中
 * 
 * 时间复杂度分析：
 * 1. 计算LCS：O(m*n)
 * 2. 构造超序列：O(m+n)
 * 3. 总体时间复杂度：O(m*n)
 * 
 * 空间复杂度分析：
 * 1. dp数组：O(m*n)
 * 2. LCS字符串：O(min(m,n))
 * 3. 结果字符串：O(m+n)
 * 4. 总体空间复杂度：O(m*n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入是否为空
 * 2. 边界处理：正确处理空字符串的情况
 * 3. 内存优化：可复用部分计算结果
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
char* shortestCommonSupersequence(char* str1, char* str2) {
    // 异常处理：检查输入是否为空
    if (str1 == 0 || str2 == 0) {
        return "";
    }
    
    int m = strlen(str1);
    int n = strlen(str2);
    
    if (m == 0) {
        return str2;
    }
    
    if (n == 0) {
        return str1;
    }
    
    // 计算LCS长度和构造dp表
    int dp[501][501]; // 假设最大长度为500
    
    // 填充dp表
    for (int i = 0; i <= m; i++) {
        dp[i][0] = 0;
    }
    for (int j = 0; j <= n; j++) {
        dp[0][j] = 0;
    }
    
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
    
    // 通过dp表回溯构造LCS
    char lcs[501];
    int lcsLen = 0;
    int i = m, j = n;
    while (i > 0 && j > 0) {
        if (str1[i - 1] == str2[j - 1]) {
            lcs[lcsLen++] = str1[i - 1];
            i--;
            j--;
        } else if (dp[i - 1][j] > dp[i][j - 1]) {
            i--;
        } else {
            j--;
        }
    }
    
    // 反转LCS字符串，因为我们是从后往前构造的
    for (int k = 0; k < lcsLen / 2; k++) {
        char temp = lcs[k];
        lcs[k] = lcs[lcsLen - 1 - k];
        lcs[lcsLen - 1 - k] = temp;
    }
    lcs[lcsLen] = '\0';
    
    // 通过LCS构造最短公共超序列
    char result[1001]; // 假设结果最大长度为1000
    int resultLen = 0;
    int p1 = 0, p2 = 0;
    
    // 遍历LCS中的每个字符
    for (int k = 0; k < lcsLen; k++) {
        char ch = lcs[k];
        
        // 将str1中在该字符之前的部分添加到结果中
        while (p1 < m && str1[p1] != ch) {
            result[resultLen++] = str1[p1];
            p1++;
        }
        
        // 将str2中在该字符之前的部分添加到结果中
        while (p2 < n && str2[p2] != ch) {
            result[resultLen++] = str2[p2];
            p2++;
        }
        
        // 添加该字符本身
        result[resultLen++] = ch;
        p1++;
        p2++;
    }
    
    // 添加str1和str2剩余的部分
    while (p1 < m) {
        result[resultLen++] = str1[p1];
        p1++;
    }
    
    while (p2 < n) {
        result[resultLen++] = str2[p2];
        p2++;
    }
    
    result[resultLen] = '\0';
    return result;
}
*/