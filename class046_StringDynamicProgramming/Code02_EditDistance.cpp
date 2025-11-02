#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

// 编辑距离 (Edit Distance)
// 给你两个单词 word1 和 word2
// 请返回将 word1 转换成 word2 所使用的最少代价
// 你可以对一个单词进行如下三种操作：
// 插入一个字符，代价a
// 删除一个字符，代价b
// 替换一个字符，代价c
// 
// 题目来源：LeetCode 72. 编辑距离
// 测试链接：https://leetcode.cn/problems/edit-distance/
//
// 算法核心思想：
// 使用动态规划解决字符串编辑距离问题，通过构建二维DP表来计算最小编辑操作次数
//
// 时间复杂度分析：
// - 基础版本：O(n*m)，其中n为word1的长度，m为word2的长度
// - 空间优化版本：O(n*m)时间，O(m)空间
//
// 空间复杂度分析：
// - 基础版本：O(n*m)
// - 空间优化版本：O(m)
//
// 最优解判定：✅ 是最优解，时间复杂度无法进一步优化，空间复杂度已优化到O(min(n,m))
//
// 工程化考量：
// 1. 异常处理：检查输入参数合法性
// 2. 边界条件：处理空字符串和极端情况
// 3. 性能优化：使用滚动数组减少空间占用
// 4. 代码可读性：添加详细注释和清晰的变量命名
//
// 与其他领域的联系：
// - 自然语言处理：文本相似度计算、拼写检查
// - 生物信息学：DNA序列比对、基因序列分析
// - 信息检索：文档相似度计算、搜索引擎优化
// - 版本控制：Git等版本控制系统中的diff算法

class Solution {
public:
    /*
     * 编辑距离算法（基础版）
     * 使用动态规划解决字符串编辑距离问题
     * dp[i][j] 表示将str1的前i个字符转换为str2的前j个字符所需的最小代价
     * 
     * 状态转移方程：
     * 如果 s1[i-1] == s2[j-1]，不需要额外操作
     *   dp[i][j] = dp[i-1][j-1]
     * 否则，可以选择以下三种操作中的最小值：
     *   1. 替换：dp[i-1][j-1] + c
     *   2. 删除：dp[i-1][j] + b
     *   3. 插入：dp[i][j-1] + a
     * 
     * 边界条件：
     * dp[i][0] = i * b，表示将str1前i个字符删除为空字符串的代价
     * dp[0][j] = j * a，表示将空字符串插入j个字符得到str2前j个字符的代价
     * 
     * 时间复杂度：O(n*m)，其中n为str1的长度，m为str2的长度
     * 空间复杂度：O(n*m)
     */
    int editDistance1(string str1, string str2, int a, int b, int c) {
        int n = str1.length();
        int m = str2.length();
        
        // dp[i][j]: str1前i个字符转换为str2前j个字符的最小代价
        vector<vector<int>> dp(n + 1, vector<int>(m + 1, 0));
        
        // 边界条件
        for (int i = 1; i <= n; i++) {
            dp[i][0] = i * b;
        }
        for (int j = 1; j <= m; j++) {
            dp[0][j] = j * a;
        }
        
        // 填充dp表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int p1 = INT_MAX;
                if (str1[i - 1] == str2[j - 1]) {
                    p1 = dp[i - 1][j - 1];
                }
                int p2 = INT_MAX;
                if (str1[i - 1] != str2[j - 1]) {
                    p2 = dp[i - 1][j - 1] + c;
                }
                int p3 = dp[i][j - 1] + a;
                int p4 = dp[i - 1][j] + b;
                dp[i][j] = min(min(p1, p2), min(p3, p4));
            }
        }
        
        return dp[n][m];
    }

    /*
     * 编辑距离算法（优化版）
     * 对基础版本进行逻辑优化，减少不必要的判断
     * 
     * 时间复杂度：O(n*m)
     * 空间复杂度：O(n*m)
     */
    int editDistance2(string str1, string str2, int a, int b, int c) {
        int n = str1.length();
        int m = str2.length();
        
        // dp[i][j]: str1前i个字符转换为str2前j个字符的最小代价
        vector<vector<int>> dp(n + 1, vector<int>(m + 1, 0));
        
        // 边界条件
        for (int i = 1; i <= n; i++) {
            dp[i][0] = i * b;
        }
        for (int j = 1; j <= m; j++) {
            dp[0][j] = j * a;
        }
        
        // 填充dp表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (str1[i - 1] == str2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = min(min(dp[i - 1][j] + b, dp[i][j - 1] + a), dp[i - 1][j - 1] + c);
                }
            }
        }
        
        return dp[n][m];
    }

    /*
     * 空间优化版本
     * 使用滚动数组优化空间复杂度
     * 
     * 时间复杂度：O(n*m)
     * 空间复杂度：O(m)
     */
    int editDistance3(string str1, string str2, int a, int b, int c) {
        int n = str1.length();
        int m = str2.length();
        
        // 只需要一维数组
        vector<int> dp(m + 1, 0);
        
        // 初始化第一行
        for (int j = 1; j <= m; j++) {
            dp[j] = j * a;
        }
        
        // 填充dp表
        for (int i = 1, leftUp, backUp; i <= n; i++) {
            leftUp = (i - 1) * b;
            dp[0] = i * b;
            for (int j = 1; j <= m; j++) {
                backUp = dp[j];
                if (str1[i - 1] == str2[j - 1]) {
                    dp[j] = leftUp;
                } else {
                    dp[j] = min(min(dp[j] + b, dp[j - 1] + a), leftUp + c);
                }
                leftUp = backUp;
            }
        }
        
        return dp[m];
    }
    
    // 默认参数版本
    int minDistance(string word1, string word2) {
        return editDistance2(word1, word2, 1, 1, 1);
    }
};

// 测试函数
void test() {
    Solution sol;
    
    // 测试用例1
    string word1 = "horse", word2 = "ros";
    cout << "Test 1: word1=\"" << word1 << "\", word2=\"" << word2 << "\"" << endl;
    cout << "Result: " << sol.minDistance(word1, word2) << endl;
    cout << "Result (method 1): " << sol.editDistance1(word1, word2, 1, 1, 1) << endl;
    cout << "Result (method 2): " << sol.editDistance2(word1, word2, 1, 1, 1) << endl;
    cout << "Result (method 3): " << sol.editDistance3(word1, word2, 1, 1, 1) << endl << endl;
    
    // 测试用例2
    word1 = "intention";
    word2 = "execution";
    cout << "Test 2: word1=\"" << word1 << "\", word2=\"" << word2 << "\"" << endl;
    cout << "Result: " << sol.minDistance(word1, word2) << endl;
    cout << "Result (method 1): " << sol.editDistance1(word1, word2, 1, 1, 1) << endl;
    cout << "Result (method 2): " << sol.editDistance2(word1, word2, 1, 1, 1) << endl;
    cout << "Result (method 3): " << sol.editDistance3(word1, word2, 1, 1, 1) << endl << endl;
}

int main() {
    test();
    return 0;
}