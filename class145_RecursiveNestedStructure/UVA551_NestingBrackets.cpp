// UVA 551 Nesting a Bunch of Brackets (多种类型括号匹配)
// 测试链接 : https://onlinejudge.org/external/5/551.pdf

#include <iostream>
#include <stack>
#include <string>
using namespace std;

class UVA551_NestingBrackets {
public:
    string checkBrackets(string s) {
        stack<char> stk;
        stack<int> positions;
        
        for (int i = 0; i < s.length(); i++) {
            char c = s[i];
            
            if (c == '(' || c == '[' || c == '{' || c == '<') {
                stk.push(c);
                positions.push(i + 1); // 位置从1开始计数
            } else if (c == ')' || c == ']' || c == '}' || c == '>') {
                if (stk.empty()) {
                    return "NO " + to_string(i + 1); // 不匹配的位置
                }
                
                char top = stk.top(); stk.pop();
                positions.pop();
                
                // 检查括号类型是否匹配
                if (!isMatchingPair(top, c)) {
                    return "NO " + to_string(i + 1); // 不匹配的位置
                }
            }
        }
        
        if (!stk.empty()) {
            return "NO " + to_string(positions.top()); // 未匹配的括号位置
        }
        
        return "YES";
    }
    
private:
    bool isMatchingPair(char open, char close) {
        return (open == '(' && close == ')') ||
               (open == '[' && close == ']') ||
               (open == '{' && close == '}') ||
               (open == '<' && close == '>');
    }
};

// 测试函数
int main() {
    UVA551_NestingBrackets solution;
    
    // 测试用例1
    string s1 = "([]){}";
    cout << "输入: " << s1 << endl;
    cout << "输出: " << solution.checkBrackets(s1) << endl;
    cout << "期望: YES" << endl << endl;
    
    // 测试用例2
    string s2 = "([)]";
    cout << "输入: " << s2 << endl;
    cout << "输出: " << solution.checkBrackets(s2) << endl;
    cout << "期望: NO 3" << endl << endl;
    
    return 0;
}