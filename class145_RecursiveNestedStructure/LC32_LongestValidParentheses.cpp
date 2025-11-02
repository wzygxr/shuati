// LeetCode 32. Longest Valid Parentheses (最长有效括号)
// 测试链接 : https://leetcode.cn/problems/longest-valid-parentheses/

#include <iostream>
#include <stack>
#include <string>
#include <algorithm>
using namespace std;

class LC32_LongestValidParentheses {
public:
    int longestValidParentheses(string s) {
        stack<int> stk;
        stk.push(-1); // 初始化栈底为-1
        int maxLen = 0;
        
        for (int i = 0; i < s.length(); i++) {
            if (s[i] == '(') {
                stk.push(i); // 遇到左括号，压入索引
            } else {
                stk.pop(); // 遇到右括号，弹出栈顶
                if (stk.empty()) {
                    // 栈为空，压入当前索引作为新的基准
                    stk.push(i);
                } else {
                    // 计算当前有效括号长度
                    maxLen = max(maxLen, i - stk.top());
                }
            }
        }
        
        return maxLen;
    }
};

// 测试函数
int main() {
    LC32_LongestValidParentheses solution;
    
    // 测试用例1
    string s1 = "(()";
    cout << "输入: " << s1 << endl;
    cout << "输出: " << solution.longestValidParentheses(s1) << endl;
    cout << "期望: 2" << endl << endl;
    
    // 测试用例2
    string s2 = ")()())";
    cout << "输入: " << s2 << endl;
    cout << "输出: " << solution.longestValidParentheses(s2) << endl;
    cout << "期望: 4" << endl << endl;
    
    // 测试用例3
    string s3 = "";
    cout << "输入: " << s3 << endl;
    cout << "输出: " << solution.longestValidParentheses(s3) << endl;
    cout << "期望: 0" << endl << endl;
    
    return 0;
}