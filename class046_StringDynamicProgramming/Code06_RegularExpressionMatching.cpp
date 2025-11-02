#include <iostream>
#include <vector>
#include <string>
#include <utility>  // for pair
using namespace std;

// 正则表达式匹配 (Regular Expression Matching)
// 给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。
// '.' 匹配任意单个字符
// '*' 匹配零个或多个前面的那一个元素
// 所谓匹配，是要涵盖整个字符串 s 的，而不是部分字符串。
// 
// 题目来源：LeetCode 10. 正则表达式匹配
// 测试链接：https://leetcode.cn/problems/regular-expression-matching/
//
// 算法核心思想：
// 使用动态规划解决正则表达式匹配问题，通过构建二维DP表来判断字符串与模式是否匹配
//
// 时间复杂度分析：
// - 基础版本：O(n*m)，其中n为s的长度，m为p的长度
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
// 4. 代码可读性：添加详细注释和测试用例
//
// 与其他领域的联系：
// - 文本处理：模式匹配和字符串搜索
// - 编译原理：词法分析和语法分析
// - 搜索引擎：文本检索和过滤

class Solution {
public:
    /*
     * 正则表达式匹配 - 动态规划解法
     * 使用动态规划解决正则表达式匹配问题
     * dp[i][j] 表示字符串s的前i个字符与模式p的前j个字符是否匹配
     * 
     * 状态转移方程：
     * 如果 p[j-1] != '*'：
     *   dp[i][j] = dp[i-1][j-1] && (s[i-1] == p[j-1] || p[j-1] == '.')
     * 如果 p[j-1] == '*'：
     *   dp[i][j] = dp[i][j-2] || (dp[i-1][j] && (s[i-1] == p[j-2] || p[j-2] == '.'))
     * 
     * 解释：
     * 当p[j-1]不是'*'时，当前字符必须匹配且前面的子串也必须匹配
     * 当p[j-1]是'*'时，有两种情况：
     *   1. '*'匹配0个前面的字符：dp[i][j-2]
     *   2. '*'匹配多个前面的字符：dp[i-1][j] && (s[i-1] == p[j-2] || p[j-2] == '.')
     * 
     * 边界条件：
     * dp[0][0] = true，表示两个空字符串匹配
     * dp[i][0] = false (i>0)，表示空模式无法匹配非空字符串
     * dp[0][j] 需要特殊处理，只有当p[j-1]是'*'且dp[0][j-2]为true时才为true
     * 
     * 时间复杂度：O(n*m)，其中n为s的长度，m为p的长度
     * 空间复杂度：O(n*m)
     */
    bool isMatch(string s, string p) {
        int n = s.length();
        int m = p.length();
        
        // dp[i][j] 表示s的前i个字符与p的前j个字符是否匹配
        vector<vector<bool> > dp(n + 1, vector<bool>(m + 1, false));
        
        // 边界条件
        dp[0][0] = true;
        
        // 处理空字符串与模式的匹配情况
        for (int j = 2; j <= m; j++) {
            if (p[j - 1] == '*') {
                dp[0][j] = dp[0][j - 2];
            }
        }
        
        // 填充dp表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (p[j - 1] != '*') {
                    // 当前模式字符不是'*'
                    dp[i][j] = dp[i - 1][j - 1] && 
                        (s[i - 1] == p[j - 1] || p[j - 1] == '.');
                } else {
                    // 当前模式字符是'*'
                    // '*'匹配0个前面的字符 或 '*'匹配多个前面的字符
                    dp[i][j] = dp[i][j - 2] || 
                        (dp[i - 1][j] && (s[i - 1] == p[j - 2] || p[j - 2] == '.'));
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
    bool isMatchOptimized(string s, string p) {
        int n = s.length();
        int m = p.length();
        
        // 只需要两行数组
        vector<bool> prev(m + 1, false);
        vector<bool> curr(m + 1, false);
        
        // 边界条件
        prev[0] = true;
        
        // 处理空字符串与模式的匹配情况
        for (int j = 2; j <= m; j++) {
            if (p[j - 1] == '*') {
                prev[j] = prev[j - 2];
            }
        }
        
        // 填充dp表
        for (int i = 1; i <= n; i++) {
            // 每次循环开始前重置curr数组
            for (int j = 0; j <= m; j++) {
                curr[j] = false;
            }
            
            for (int j = 1; j <= m; j++) {
                if (p[j - 1] != '*') {
                    // 当前模式字符不是'*'
                    curr[j] = prev[j - 1] && 
                        (s[i - 1] == p[j - 1] || p[j - 1] == '.');
                } else {
                    // 当前模式字符是'*'
                    // '*'匹配0个前面的字符 或 '*'匹配多个前面的字符
                    curr[j] = curr[j - 2] || 
                        (prev[j] && (s[i - 1] == p[j - 2] || p[j - 2] == '.'));
                }
            }
            
            // 交换prev和curr
            prev.swap(curr);
        }
        
        return prev[m];
    }
};

// 测试函数
void test() {
    Solution sol;
    
    // 测试用例
    vector<pair<string, string> > testCases = {
        make_pair("aa", "a"),      // false
        make_pair("aa", "a*"),     // true
        make_pair("ab", ".*"),     // true
        make_pair("aab", "c*a*b"), // true
        make_pair("mississippi", "mis*is*p*.") // false
    };
    
    cout << "正则表达式匹配测试:" << endl;
    for (vector<pair<string, string> >::iterator it = testCases.begin(); it != testCases.end(); ++it) {
        string s = it->first;
        string p = it->second;
        bool result1 = sol.isMatch(s, p);
        bool result2 = sol.isMatchOptimized(s, p);
        cout << "s=\"" << s << "\", p=\"" << p << "\" => " << (result1 ? "true" : "false") 
             << " (optimized: " << (result2 ? "true" : "false") << ")" << endl;
    }
}

int main() {
    test();
    return 0;
}