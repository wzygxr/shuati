// LeetCode 856. Score of Parentheses (括号的分数)
// 测试链接 : https://leetcode.cn/problems/score-of-parentheses/

#include <iostream>
#include <stack>
#include <string>
#include <algorithm>
using namespace std;

class LC856_ScoreOfParentheses {
public:
    int scoreOfParentheses(string s) {
        stack<int> stk;
        stk.push(0); // 初始化栈底为0
        
        for (char c : s) {
            if (c == '(') {
                stk.push(0); // 遇到左括号，压入0
            } else {
                int v = stk.top(); stk.pop(); // 弹出当前值
                int w = stk.top(); stk.pop(); // 弹出前一个值
                // 计算当前括号对的分数并加到前一个值上
                stk.push(w + max(2 * v, 1));
            }
        }
        
        return stk.top(); // 返回最终结果
    }
};

// 测试函数
int main() {
    LC856_ScoreOfParentheses solution;
    
    // 测试用例1
    string s1 = "()";
    cout << "输入: " << s1 << endl;
    cout << "输出: " << solution.scoreOfParentheses(s1) << endl;
    cout << "期望: 1" << endl << endl;
    
    // 测试用例2
    string s2 = "(())";
    cout << "输入: " << s2 << endl;
    cout << "输出: " << solution.scoreOfParentheses(s2) << endl;
    cout << "期望: 2" << endl << endl;
    
    // 测试用例3
    string s3 = "()()";
    cout << "输入: " << s3 << endl;
    cout << "输出: " << solution.scoreOfParentheses(s3) << endl;
    cout << "期望: 2" << endl << endl;
    
    // 测试用例4
    string s4 = "(()(()))";
    cout << "输入: " << s4 << endl;
    cout << "输出: " << solution.scoreOfParentheses(s4) << endl;
    cout << "期望: 6" << endl << endl;
    
    return 0;
}