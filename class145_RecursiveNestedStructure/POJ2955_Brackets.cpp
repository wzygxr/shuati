// POJ 2955 Brackets (最长括号匹配子序列)
// 测试链接 : http://poj.org/problem?id=2955

#include <iostream>
#include <string>
#include <algorithm>
#include <cstring>
using namespace std;

class POJ2955_Brackets {
public:
    int longestValidParentheses(string s) {
        int n = s.length();
        if (n == 0) return 0;
        
        // dp[i][j] 表示区间[i,j]内最长的有效括号长度
        int dp[105][105]; // 假设最大长度为100
        memset(dp, 0, sizeof(dp));
        
        // 填充dp表
        for (int len = 2; len <= n; len++) { // 区间长度从2开始
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                // 如果首尾字符匹配
                if ((s[i] == '(' && s[j] == ')') ||
                    (s[i] == '[' && s[j] == ']')) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                }
                
                // 尝试分割区间
                for (int k = i; k < j; k++) {
                    dp[i][j] = max(dp[i][j], dp[i][k] + dp[k + 1][j]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
};

// 测试函数
int main() {
    POJ2955_Brackets solution;
    
    // 测试用例1
    string s1 = "((()))";
    cout << "输入: " << s1 << endl;
    cout << "输出: " << solution.longestValidParentheses(s1) << endl;
    cout << "期望: 6" << endl << endl;
    
    // 测试用例2
    string s2 = "()()()";
    cout << "输入: " << s2 << endl;
    cout << "输出: " << solution.longestValidParentheses(s2) << endl;
    cout << "期望: 6" << endl << endl;
    
    return 0;
}