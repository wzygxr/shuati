/**
 * SPOJ EDIST - Edit Distance
 * 
 * 题目来源：SPOJ EDIST - Edit Distance
 * 题目链接：https://www.spoj.com/problems/EDIST/
 * 
 * 题目描述：
 * 给定两个字符串A和B。我们需要将A转换为B，可以进行以下三种操作：
 * 1. 插入一个字符
 * 2. 删除一个字符
 * 3. 替换一个字符
 * 每种操作的代价都是1。求将A转换为B的最小代价。
 * 
 * 输入格式：
 * 第一行包含一个整数t（1 ≤ t ≤ 150），表示测试用例的数量。
 * 接下来2*t行，每两个连续行包含两个字符串A和B。
 * 字符串只包含小写字母，长度不超过2000。
 * 
 * 输出格式：
 * 对于每个测试用例，输出将A转换为B的最小代价。
 * 
 * 示例：
 * 输入：
 * 1
 * abcde
 * aebd
 * 输出：
 * 3
 * 
 * 解释：
 * 将"abcde"转换为"aebd"的最小操作序列：
 * 1. 删除'c'：abde
 * 2. 删除'd'：abe
 * 3. 替换'b'为'e'：aee
 * 4. 删除'e'：ae
 * 实际上最优解是：
 * 1. 删除'c'：abde
 * 2. 删除'd'：abe
 * 3. 替换'b'为'e'：aee
 * 4. 删除第二个'e'：aed
 * 5. 替换'd'为'd'：aed -> aed (无操作)
 * 或者更优的解：
 * 1. 删除'b'：acde
 * 2. 删除'c'：ade
 * 3. 替换'd'为'b'：abe
 * 4. 插入'd'：abed
 * 实际最优解是3步操作。
 * 
 * 解题思路：
 * 这是一个经典的编辑距离问题，可以使用动态规划解决。
 * 状态定义：dp[i][j]表示将字符串A的前i个字符转换为字符串B的前j个字符的最小代价
 * 状态转移：
 *   如果A[i-1] == B[j-1]，则dp[i][j] = dp[i-1][j-1]
 *   否则dp[i][j] = min(dp[i-1][j] + 1, dp[i][j-1] + 1, dp[i-1][j-1] + 1)
 * 边界条件：
 *   dp[0][j] = j（将空字符串转换为B的前j个字符需要j次插入操作）
 *   dp[i][0] = i（将A的前i个字符转换为空字符串需要i次删除操作）
 * 
 * 算法复杂度分析：
 * - 时间复杂度：O(m*n) - m和n分别是两个字符串的长度
 * - 空间复杂度：O(m*n) - 二维dp数组
 * 
 * 工程化考量：
 * 1. 边界处理：正确处理空字符串的情况
 * 2. 性能优化：可以使用滚动数组优化空间复杂度到O(min(m,n))
 * 3. 代码质量：清晰的变量命名和详细的注释说明
 * 
 * 相关题目：
 * - LeetCode 72. 编辑距离
 * - AtCoder Educational DP Contest E - Knapsack 2
 * - 牛客网 动态规划专题 - 字符串编辑
 */

// 为避免编译问题，使用基本C++实现，不依赖STL容器
#define MAXN 2005

// 手动实现字符串长度函数
int strlen(const char* s) {
    int len = 0;
    while (s[len] != '\0') {
        len++;
    }
    return len;
}

// 手动实现最小值函数
int min(int a, int b) {
    return (a < b) ? a : b;
}

int min3(int a, int b, int c) {
    return min(min(a, b), c);
}

/**
 * 计算两个字符串的编辑距离
 * 
 * @param word1 第一个字符串
 * @param word2 第二个字符串
 * @return 编辑距离
 */
int minDistance(const char* word1, const char* word2) {
    int m = strlen(word1);
    int n = strlen(word2);
    
    // dp[i][j]表示将word1的前i个字符转换为word2的前j个字符的最小代价
    int dp[MAXN][MAXN];
    
    // 初始化边界条件
    // 将空字符串转换为word2的前j个字符需要j次插入操作
    for (int j = 0; j <= n; j++) {
        dp[0][j] = j;
    }
    
    // 将word1的前i个字符转换为空字符串需要i次删除操作
    for (int i = 0; i <= m; i++) {
        dp[i][0] = i;
    }
    
    // 填充dp表
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            // 如果当前字符相同，则不需要额外操作
            if (word1[i - 1] == word2[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                // 如果当前字符不同，则取三种操作的最小值
                dp[i][j] = min3(
                    dp[i - 1][j] + 1,      // 删除操作
                    dp[i][j - 1] + 1,      // 插入操作
                    dp[i - 1][j - 1] + 1   // 替换操作
                );
            }
        }
    }
    
    return dp[m][n]; // 返回编辑距离
}

/**
 * 空间优化版本
 * 
 * @param word1 第一个字符串
 * @param word2 第二个字符串
 * @return 编辑距离
 */
int minDistanceOptimized(const char* word1, const char* word2) {
    int m = strlen(word1);
    int n = strlen(word2);
    
    // 只需要两行来存储状态
    int prev[MAXN];
    int curr[MAXN];
    
    // 初始化边界条件
    // 将空字符串转换为word2的前j个字符需要j次插入操作
    for (int j = 0; j <= n; j++) {
        prev[j] = j;
    }
    
    // 填充dp表
    for (int i = 1; i <= m; i++) {
        curr[0] = i; // 将word1的前i个字符转换为空字符串需要i次删除操作
        
        for (int j = 1; j <= n; j++) {
            // 如果当前字符相同，则不需要额外操作
            if (word1[i - 1] == word2[j - 1]) {
                curr[j] = prev[j - 1];
            } else {
                // 如果当前字符不同，则取三种操作的最小值
                curr[j] = min3(
                    prev[j] + 1,      // 删除操作
                    curr[j - 1] + 1,  // 插入操作
                    prev[j - 1] + 1   // 替换操作
                );
            }
        }
        
        // 交换prev和curr
        for (int k = 0; k <= n; k++) {
            prev[k] = curr[k];
        }
    }
    
    return prev[n]; // 返回编辑距离
}

// 由于C++环境限制，我们只提供函数实现，不包含main函数测试
// 在实际使用中，可以按以下方式调用：
// const char* word1 = "abcde";
// const char* word2 = "aebd";
// int result1 = minDistance(word1, word2);
// int result2 = minDistanceOptimized(word1, word2);