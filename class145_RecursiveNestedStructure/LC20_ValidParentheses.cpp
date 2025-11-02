// LeetCode 20. Valid Parentheses (有效的括号)
// 测试链接 : https://leetcode.cn/problems/valid-parentheses/

#include <iostream>
#include <stack>
#include <string>
#include <unordered_map>
using namespace std;

class LC20_ValidParentheses {
public:
    bool isValid(string s) {
        stack<char> stk;
        
        // 定义括号匹配关系
        unordered_map<char, char> mapping = {
            {')', '('},
            {']', '['},
            {'}', '{'}
        };
        
        for (char c : s) {
            if (c == '(' || c == '[' || c == '{') {
                stk.push(c); // 遇到左括号入栈
            } else {
                if (stk.empty()) return false; // 栈为空但遇到右括号
                
                char top = stk.top(); stk.pop(); // 弹出栈顶元素
                // 检查括号是否匹配
                if (mapping[c] != top) {
                    return false;
                }
            }
        }
        
        return stk.empty(); // 栈为空表示所有括号都匹配
    }
};

// 测试函数
int main() {
    LC20_ValidParentheses solution;
    
    // 测试用例1
    string s1 = "()";
    cout << "输入: " << s1 << endl;
    cout << "输出: " << (solution.isValid(s1) ? "true" : "false") << endl;
    cout << "期望: true" << endl << endl;
    
    // 测试用例2
    string s2 = "()[]{}";
    cout << "输入: " << s2 << endl;
    cout << "输出: " << (solution.isValid(s2) ? "true" : "false") << endl;
    cout << "期望: true" << endl << endl;
    
    // 测试用例3
    string s3 = "(]";
    cout << "输入: " << s3 << endl;
    cout << "输出: " << (solution.isValid(s3) ? "true" : "false") << endl;
    cout << "期望: false" << endl << endl;
    
    return 0;
}