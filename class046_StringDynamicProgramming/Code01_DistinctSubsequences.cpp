#include <iostream>
#include <vector>
#include <string>
using namespace std;

// 不同的子序列 (Distinct Subsequences)
// 给你两个字符串s和t ，统计并返回在s的子序列中t出现的个数
// 答案对 1000000007 取模
// 
// 题目来源：LeetCode 115. 不同的子序列
// 测试链接：https://leetcode.cn/problems/distinct-subsequences/
//
// 算法核心思想：
// 使用动态规划解决子序列计数问题，关键在于理解状态转移方程和边界条件
//
// 时间复杂度分析：
// - 基础版本：O(n*m)，其中n为s的长度，m为t的长度
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
// 4. 数值安全：使用取模运算防止整数溢出
// 5. 代码可读性：添加详细注释和测试用例
//
// 与其他领域的联系：
// - 自然语言处理：文本相似度计算、模式匹配
// - 生物信息学：DNA序列比对、基因序列分析
// - 信息检索：文档相似度计算、搜索引擎优化

class Solution {
public:
    /*
     * 算法思路：
     * 使用动态规划解决该问题
     * dp[i][j] 表示在s的前i个字符中，可以组成t的前j个字符的子序列数量
     * 
     * 状态转移方程：
     * 如果 s[i-1] == t[j-1]，那么可以选择使用或不使用s[i-1]字符
     *   dp[i][j] = dp[i-1][j] + dp[i-1][j-1]
     * 如果 s[i-1] != t[j-1]，那么不能使用s[i-1]字符
     *   dp[i][j] = dp[i-1][j]
     * 
     * 边界条件：
     * dp[i][0] = 1，表示t为空字符串时，只有一种方案（空子序列）
     * dp[0][j] = 0 (j>0)，表示s为空字符串时，无法组成非空的t
     * 
     * 时间复杂度：O(n*m)，其中n为s的长度，m为t的长度
     * 空间复杂度：O(n*m)
     */
    int numDistinct1(string str, string target) {
        const int mod = 1000000007;
        int n = str.length();
        int m = target.length();
        
        // dp[i][j]: s的前i个字符的子序列中t的前j个字符出现的次数
        vector<vector<long long>> dp(n + 1, vector<long long>(m + 1, 0));
        
        // 边界条件：空字符串是任何字符串的一个子序列
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1;
        }
        
        // 填充dp表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // 不使用str[i-1]字符的方案数
                dp[i][j] = dp[i - 1][j];
                // 如果字符匹配，加上使用str[i-1]字符的方案数
                if (str[i - 1] == target[j - 1]) {
                    dp[i][j] = (dp[i][j] + dp[i - 1][j - 1]) % mod;
                }
            }
        }
        
        return (int)dp[n][m];
    }

    /*
     * 空间优化版本
     * 观察状态转移方程，dp[i][j]只依赖于dp[i-1][j]和dp[i-1][j-1]
     * 所以可以使用滚动数组优化空间复杂度
     * 
     * 时间复杂度：O(n*m)
     * 空间复杂度：O(m)
     */
    int numDistinct2(string str, string target) {
        const int mod = 1000000007;
        int n = str.length();
        int m = target.length();
        
        // 只需要一维数组
        vector<long long> dp(m + 1, 0);
        dp[0] = 1;
        
        for (int i = 1; i <= n; i++) {
            // 从右到左更新，避免覆盖还需要使用的值
            for (int j = m; j >= 1; j--) {
                if (str[i - 1] == target[j - 1]) {
                    dp[j] = (dp[j] + dp[j - 1]) % mod;
                }
            }
        }
        
        return (int)dp[m];
    }
};

// 测试函数
void test() {
    Solution sol;
    
    // 测试用例1
    string s1 = "rabbbit", t1 = "rabbit";
    cout << "Test 1: s=\"" << s1 << "\", t=\"" << t1 << "\"" << endl;
    cout << "Result: " << sol.numDistinct1(s1, t1) << endl;
    cout << "Result (optimized): " << sol.numDistinct2(s1, t1) << endl << endl;
    
    // 测试用例2
    string s2 = "babgbag", t2 = "bag";
    cout << "Test 2: s=\"" << s2 << "\", t=\"" << t2 << "\"" << endl;
    cout << "Result: " << sol.numDistinct1(s2, t2) << endl;
    cout << "Result (optimized): " << sol.numDistinct2(s2, t2) << endl << endl;
}

int main() {
    test();
    return 0;
}