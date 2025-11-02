// 最长有效括号 (Longest Valid Parentheses)
// 给你一个只包含 '(' 和 ')' 的字符串，找出最长有效（格式正确且连续）括号子串的长度。
// 测试链接 : https://leetcode.cn/problems/longest-valid-parentheses/

#include <iostream>
#include <vector>
#include <string>
#include <stack>
#include <algorithm>
using namespace std;

class Solution {
public:
    // 方法1：动态规划
    // 时间复杂度：O(n) - 遍历字符串一次
    // 空间复杂度：O(n) - dp数组
    // 核心思路：dp[i]表示以s[i]结尾的最长有效括号长度
    int longestValidParentheses(string s) {
        int n = s.length();
        if (n == 0) return 0;
        
        vector<int> dp(n, 0);
        int maxLen = 0;
        
        for (int i = 1; i < n; i++) {
            if (s[i] == ')') {
                if (s[i - 1] == '(') {
                    // 情况1："...()"
                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                } else if (i - dp[i - 1] > 0 && s[i - dp[i - 1] - 1] == '(') {
                    // 情况2："...(有效括号序列)"
                    dp[i] = dp[i - 1] + 
                           (i - dp[i - 1] >= 2 ? dp[i - dp[i - 1] - 2] : 0) + 2;
                }
                maxLen = max(maxLen, dp[i]);
            }
        }
        
        return maxLen;
    }
    
    // 方法2：使用栈
    // 时间复杂度：O(n) - 遍历字符串一次
    // 空间复杂度：O(n) - 栈的空间
    // 核心思路：使用栈记录未匹配的左括号位置
    int longestValidParentheses2(string s) {
        int n = s.length();
        if (n == 0) return 0;
        
        stack<int> st;
        st.push(-1); // 哨兵节点，表示有效括号序列的开始前一个位置
        int maxLen = 0;
        
        for (int i = 0; i < n; i++) {
            if (s[i] == '(') {
                st.push(i);
            } else {
                st.pop();
                if (st.empty()) {
                    st.push(i); // 更新哨兵节点
                } else {
                    maxLen = max(maxLen, i - st.top());
                }
            }
        }
        
        return maxLen;
    }
    
    // 方法3：双向扫描
    // 时间复杂度：O(n) - 两次遍历
    // 空间复杂度：O(1) - 只使用常数空间
    // 核心思路：从左到右和从右到左各扫描一次，处理左右括号不平衡的情况
    int longestValidParentheses3(string s) {
        int n = s.length();
        if (n == 0) return 0;
        
        int left = 0, right = 0;
        int maxLen = 0;
        
        // 从左到右扫描
        for (int i = 0; i < n; i++) {
            if (s[i] == '(') {
                left++;
            } else {
                right++;
            }
            
            if (left == right) {
                maxLen = max(maxLen, 2 * right);
            } else if (right > left) {
                left = right = 0; // 重置计数器
            }
        }
        
        // 从右到左扫描
        left = right = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (s[i] == '(') {
                left++;
            } else {
                right++;
            }
            
            if (left == right) {
                maxLen = max(maxLen, 2 * left);
            } else if (left > right) {
                left = right = 0; // 重置计数器
            }
        }
        
        return maxLen;
    }
};

// 测试用例和性能对比
int main() {
    Solution solution;
    
    // 测试用例1
    string s1 = "(()";
    cout << "测试用例1 - s: \"(()\"" << endl;
    cout << "方法1结果: " << solution.longestValidParentheses(s1) << endl;
    cout << "方法2结果: " << solution.longestValidParentheses2(s1) << endl;
    cout << "方法3结果: " << solution.longestValidParentheses3(s1) << endl;
    cout << "预期结果: 2" << endl << endl;
    
    // 测试用例2
    string s2 = ")()())";
    cout << "测试用例2 - s: \")()())\"" << endl;
    cout << "方法1结果: " << solution.longestValidParentheses(s2) << endl;
    cout << "方法2结果: " << solution.longestValidParentheses2(s2) << endl;
    cout << "方法3结果: " << solution.longestValidParentheses3(s2) << endl;
    cout << "预期结果: 4" << endl << endl;
    
    // 测试用例3
    string s3 = "";
    cout << "测试用例3 - s: \"\"" << endl;
    cout << "方法1结果: " << solution.longestValidParentheses(s3) << endl;
    cout << "方法2结果: " << solution.longestValidParentheses2(s3) << endl;
    cout << "方法3结果: " << solution.longestValidParentheses3(s3) << endl;
    cout << "预期结果: 0" << endl << endl;
    
    // 测试用例4
    string s4 = "()(()";
    cout << "测试用例4 - s: \"()(()\"" << endl;
    cout << "方法1结果: " << solution.longestValidParentheses(s4) << endl;
    cout << "方法2结果: " << solution.longestValidParentheses2(s4) << endl;
    cout << "方法3结果: " << solution.longestValidParentheses3(s4) << endl;
    cout << "预期结果: 2" << endl;
    
    return 0;
}