#include <iostream>
#include <vector>
#include <string>
#include <algorithm>

// 编辑距离
// 给你两个单词 word1 和 word2，计算将 word1 转换成 word2 所使用的最少操作数
// 你可以对一个单词进行如下三种操作：
// 插入一个字符
// 删除一个字符
// 替换一个字符
// 测试链接 : https://leetcode.cn/problems/edit-distance/
class Solution {
public:
    /*
     * 算法思路：
     * 使用动态规划解决编辑距离问题
     * dp[i][j] 表示将 word1 的前 i 个字符转换为 word2 的前 j 个字符所需的最小操作数
     * 
     * 状态转移方程：
     * 如果 word1[i-1] == word2[j-1]，则不需要操作
     *   dp[i][j] = dp[i-1][j-1]
     * 否则，取三种操作的最小值：
     *   dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
     * 其中：
     *   dp[i-1][j] + 1 表示删除操作（删除 word1 的第 i 个字符）
     *   dp[i][j-1] + 1 表示插入操作（在 word1 的第 i 个位置后插入 word2 的第 j 个字符）
     *   dp[i-1][j-1] + 1 表示替换操作（将 word1 的第 i 个字符替换为 word2 的第 j 个字符）
     * 
     * 边界条件：
     * dp[i][0] = i，表示将 word1 的前 i 个字符转换为空字符串需要 i 次删除操作
     * dp[0][j] = j，表示将空字符串转换为 word2 的前 j 个字符需要 j 次插入操作
     * 
     * 时间复杂度：O(m*n)，其中m为word1的长度，n为word2的长度
     * 空间复杂度：O(m*n)
     */
    int minDistance1(std::string word1, std::string word2) {
        int m = word1.length();
        int n = word2.length();
        // dp[i][j] 表示word1[0...i-1]转换为word2[0...j-1]所需的最小操作数
        std::vector<std::vector<int>> dp(m + 1, std::vector<int>(n + 1));
        
        // 初始化边界条件
        for (int i = 0; i <= m; ++i) {
            dp[i][0] = i; // 将word1转换为空字符串，需要i次删除操作
        }
        for (int j = 0; j <= n; ++j) {
            dp[0][j] = j; // 将空字符串转换为word2，需要j次插入操作
        }
        
        // 填充dp表
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (word1[i - 1] == word2[j - 1]) {
                    // 当前字符相同，不需要操作
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 取三种操作的最小值
                    dp[i][j] = std::min({dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]}) + 1;
                }
            }
        }
        
        return dp[m][n];
    }

    /*
     * 空间优化版本
     * 观察状态转移方程，dp[i][j]只依赖于dp[i-1][j-1]、dp[i-1][j]和dp[i][j-1]
     * 可以使用一维数组优化空间复杂度
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(min(m,n))
     */
    int minDistance2(std::string word1, std::string word2) {
        // 为了节省空间，确保第二个参数是较短的字符串
        if (word1.length() < word2.length()) {
            std::swap(word1, word2);
        }
        
        int m = word1.length();
        int n = word2.length();
        // 使用一维数组存储当前行的数据
        std::vector<int> dp(n + 1);
        // 初始化dp[0][j] = j
        for (int j = 0; j <= n; ++j) {
            dp[j] = j;
        }
        
        // 按行填充dp表
        for (int i = 1; i <= m; ++i) {
            int pre = dp[0]; // 保存左上角的值(dp[i-1][j-1])
            dp[0] = i; // 更新dp[i][0] = i
            for (int j = 1; j <= n; ++j) {
                int temp = dp[j]; // 保存当前dp[j]，用于下一轮的pre
                if (word1[i - 1] == word2[j - 1]) {
                    // 当前字符相同，不需要操作
                    dp[j] = pre;
                } else {
                    // 取三种操作的最小值
                    dp[j] = std::min({dp[j], dp[j - 1], pre}) + 1;
                }
                pre = temp; // 更新pre为下一轮的左上角值
            }
        }
        
        return dp[n];
    }
};

// 单元测试
int main() {
    Solution solution;
    
    // 测试用例1: word1 = "horse", word2 = "ros"
    // 预期输出: 3
    // 解释: 
    // horse -> rorse (替换 'h' 为 'r')
    // rorse -> rose (删除 'r')
    // rose -> ros (删除 'e')
    std::cout << "Test 1: " << solution.minDistance1("horse", "ros") << std::endl; // 应输出3
    std::cout << "Test 1 (Space Optimized): " << solution.minDistance2("horse", "ros") << std::endl; // 应输出3
    
    // 测试用例2: word1 = "intention", word2 = "execution"
    // 预期输出: 5
    // 解释: 
    // intention -> inention (删除 't')
    // inention -> enention (替换 'i' 为 'e')
    // enention -> exention (替换 'n' 为 'x')
    // exention -> exection (替换 'n' 为 'c')
    // exection -> execution (插入 'u')
    std::cout << "Test 2: " << solution.minDistance1("intention", "execution") << std::endl; // 应输出5
    std::cout << "Test 2 (Space Optimized): " << solution.minDistance2("intention", "execution") << std::endl; // 应输出5
    
    // 边界测试: 空字符串
    std::cout << "Test 3 (Empty String): " << solution.minDistance1("", "abc") << std::endl; // 应输出3
    std::cout << "Test 3 (Empty String, Space Optimized): " << solution.minDistance2("", "abc") << std::endl; // 应输出3
    
    // 边界测试: 相同字符串
    std::cout << "Test 4 (Same String): " << solution.minDistance1("abc", "abc") << std::endl; // 应输出0
    std::cout << "Test 4 (Same String, Space Optimized): " << solution.minDistance2("abc", "abc") << std::endl; // 应输出0
    
    // 边界测试: 单字符不同
    std::cout << "Test 5 (Single Char Different): " << solution.minDistance1("a", "b") << std::endl; // 应输出1
    std::cout << "Test 5 (Single Char Different, Space Optimized): " << solution.minDistance2("a", "b") << std::endl; // 应输出1
    
    return 0;
}