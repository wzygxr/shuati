#include <iostream>
#include <stack>
#include <unordered_map>
#include <string>
#include <chrono>
#include <random>
#include <vector>
using namespace std;

using namespace std;

/**
 * 有效的括号 - C++实现
 * 
 * 题目描述：
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
 * 有效字符串需满足：
 * 1. 左括号必须用相同类型的右括号闭合。
 * 2. 左括号必须以正确的顺序闭合。
 * 
 * 测试链接：https://leetcode.cn/problems/valid-parentheses/
 * 题目来源：LeetCode
 * 难度：简单
 * 
 * 核心算法：栈
 * 
 * 解题思路：
 * 1. 使用栈来存储遇到的左括号
 * 2. 遍历字符串中的每个字符：
 *    - 如果是左括号，压入栈中
 *    - 如果是右括号，检查栈顶是否匹配
 *    - 如果不匹配或栈为空，返回false
 * 3. 遍历结束后，检查栈是否为空
 * 
 * 时间复杂度分析：
 * O(n) - 需要遍历字符串一次，n为字符串长度
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间最多为n
 * 
 * C++语言特性：
 * - 使用std::stack容器
 * - 使用std::unordered_map存储括号映射
 * - 使用RAII原则管理资源
 * - 使用const引用避免不必要的拷贝
 */
class Code20_ValidParentheses {
public:
    /**
     * 主解法：使用栈判断括号有效性
     * @param s 输入字符串
     * @return 如果括号有效返回true，否则返回false
     */
    static bool isValid(const string& s) {
        // 边界条件检查
        if (s.empty() || s.length() % 2 != 0) {
            return false;
        }
        
        // 使用栈存储左括号
        stack<char> st;
        
        // 使用unordered_map存储括号映射关系
        unordered_map<char, char> bracketMap = {
            {')', '('},
            {'}', '{'},
            {']', '['}
        };
        
        // 遍历字符串中的每个字符
        for (char c : s) {
            if (c == '(' || c == '{' || c == '[') {
                // 如果是左括号，压入栈中
                st.push(c);
            } else {
                // 如果是右括号，检查栈顶是否匹配
                if (st.empty() || st.top() != bracketMap[c]) {
                    return false;
                }
                st.pop();
            }
        }
        
        // 检查栈是否为空
        return st.empty();
    }
    
    /**
     * 优化解法：使用数组模拟栈（性能优化）
     */
    static bool isValidOptimized(const string& s) {
        // 边界条件检查
        if (s.empty() || s.length() % 2 != 0) {
            return false;
        }
        
        int n = s.length();
        // 使用字符数组模拟栈
        vector<char> stack;
        stack.reserve(n);  // 预分配空间
        
        for (char c : s) {
            if (c == '(' || c == '{' || c == '[') {
                // 左括号入栈
                stack.push_back(c);
            } else {
                // 右括号，检查栈是否为空
                if (stack.empty()) {
                    return false;
                }
                
                // 检查括号是否匹配
                char topChar = stack.back();
                if ((c == ')' && topChar != '(') ||
                    (c == '}' && topChar != '{') ||
                    (c == ']' && topChar != '[')) {
                    return false;
                }
                stack.pop_back();
            }
        }
        
        // 检查栈是否为空
        return stack.empty();
    }
    
    /**
     * 扩展解法：支持更多括号类型
     */
    static bool isValidExtended(const string& s) {
        if (s.empty() || s.length() % 2 != 0) {
            return false;
        }
        
        stack<char> st;
        
        // 扩展的括号映射（支持更多括号类型）
        unordered_map<char, char> extendedMap = {
            {')', '('},
            {'}', '{'},
            {']', '['},
            {'>', '<'},  // 支持尖括号
            // {'»', '«'}   // 支持双角括号（注释掉，避免编码问题）
        };
        
        for (char c : s) {
            if (c == '(' || c == '{' || c == '[' || c == '<') { //  || c == '«' 注释掉双角括号
                st.push(c);
            } else if (extendedMap.find(c) != extendedMap.end()) {
                if (st.empty() || st.top() != extendedMap[c]) {
                    return false;
                }
                st.pop();
            } else {
                // 忽略非括号字符（扩展功能）
                continue;
            }
        }
        
        return st.empty();
    }
    
    /**
     * 单元测试函数
     */
    static void testIsValid() {
        cout << "=== 有效的括号单元测试 ===" << endl;
        
        // 测试用例1：有效括号
        string s1 = "()";
        bool result1 = isValid(s1);
        cout << "测试用例1: " << s1 << endl;
        cout << "输出: " << (result1 ? "true" : "false") << endl;
        cout << "期望: true" << endl;
        
        // 测试用例2：有效嵌套括号
        string s2 = "()[]{}";
        bool result2 = isValid(s2);
        cout << "\n测试用例2: " << s2 << endl;
        cout << "输出: " << (result2 ? "true" : "false") << endl;
        cout << "期望: true" << endl;
        
        // 测试用例3：复杂有效括号
        string s3 = "([{}])";
        bool result3 = isValid(s3);
        cout << "\n测试用例3: " << s3 << endl;
        cout << "输出: " << (result3 ? "true" : "false") << endl;
        cout << "期望: true" << endl;
        
        // 测试用例4：无效括号（不匹配）
        string s4 = "(]";
        bool result4 = isValid(s4);
        cout << "\n测试用例4: " << s4 << endl;
        cout << "输出: " << (result4 ? "true" : "false") << endl;
        cout << "期望: false" << endl;
        
        // 测试用例5：无效括号（顺序错误）
        string s5 = "([)]";
        bool result5 = isValid(s5);
        cout << "\n测试用例5: " << s5 << endl;
        cout << "输出: " << (result5 ? "true" : "false") << endl;
        cout << "期望: false" << endl;
        
        // 测试用例6：边界情况 - 空字符串
        string s6 = "";
        bool result6 = isValid(s6);
        cout << "\n测试用例6: 空字符串" << endl;
        cout << "输出: " << (result6 ? "true" : "false") << endl;
        cout << "期望: true" << endl;
        
        // 测试用例7：边界情况 - 奇数长度
        string s7 = "(()";
        bool result7 = isValid(s7);
        cout << "\n测试用例7: " << s7 << endl;
        cout << "输出: " << (result7 ? "true" : "false") << endl;
        cout << "期望: false" << endl;
    }
    
    /**
     * 性能对比测试：栈法 vs 数组模拟栈法
     */
    static void performanceComparison() {
        cout << "\n=== 性能对比测试 ===" << endl;
        
        // 生成测试数据（大规模有效括号字符串）
        int n = 100000;
        string testData;
        stack<char> tempStack;
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<int> dist(0, 2);
        
        // 生成有效括号字符串
        for (int i = 0; i < n; i++) {
            int type = dist(gen);
            char left, right;
            
            switch (type) {
                case 0: left = '('; right = ')'; break;
                case 1: left = '['; right = ']'; break;
                default: left = '{'; right = '}'; break;
            }
            
            // 50%概率添加左括号，50%概率添加右括号（但保持有效性）
            if (dist(gen) % 2 == 0 || tempStack.empty()) {
                testData += left;
                tempStack.push(right);
            } else {
                testData += tempStack.top();
                tempStack.pop();
            }
        }
        
        // 添加剩余的右括号
        while (!tempStack.empty()) {
            testData += tempStack.top();
            tempStack.pop();
        }
        
        // 测试栈法
        auto startTime1 = chrono::high_resolution_clock::now();
        bool result1 = isValid(testData);
        auto endTime1 = chrono::high_resolution_clock::now();
        auto duration1 = chrono::duration_cast<chrono::milliseconds>(endTime1 - startTime1);
        
        // 测试数组模拟栈法
        auto startTime2 = chrono::high_resolution_clock::now();
        bool result2 = isValidOptimized(testData);
        auto endTime2 = chrono::high_resolution_clock::now();
        auto duration2 = chrono::duration_cast<chrono::milliseconds>(endTime2 - startTime2);
        
        cout << "数据规模: " << testData.length() << "个字符" << endl;
        cout << "栈法执行时间: " << duration1.count() << "ms, 结果: " << (result1 ? "true" : "false") << endl;
        cout << "数组模拟栈法执行时间: " << duration2.count() << "ms, 结果: " << (result2 ? "true" : "false") << endl;
        cout << "结果一致性: " << (result1 == result2) << endl;
    }
    
    /**
     * 正确性验证：验证两种解法结果是否一致
     */
    static void correctnessVerification() {
        cout << "\n=== 正确性验证 ===" << endl;
        
        vector<string> testCases = {
            "()",
            "()[]{}",
            "([{}])",
            "(]",
            "([)]",
            "",
            "(()",
            "{[()]}",
            "{{{{}}}}",
            "[[[]]]]",
            "({[}])",
            "((()))",
            "([{}])"
        };
        
        bool allPassed = true;
        for (size_t i = 0; i < testCases.size(); i++) {
            const string& s = testCases[i];
            bool result1 = isValid(s);
            bool result2 = isValidOptimized(s);
            
            if (result1 != result2) {
                cout << "测试用例 " << i << " 不一致:" << endl;
                cout << "输入: " << s << endl;
                cout << "解法1结果: " << (result1 ? "true" : "false") << endl;
                cout << "解法2结果: " << (result2 ? "true" : "false") << endl;
                allPassed = false;
            }
        }
        
        if (allPassed) {
            cout << "所有测试用例结果一致！" << endl;
        }
    }
    
    /**
     * 扩展功能测试：支持更多括号类型
     */
    static void testExtendedFunctionality() {
        cout << "\n=== 扩展功能测试 ===" << endl;
        
        // 测试尖括号
        string s1 = "<>";
        bool result1 = isValidExtended(s1);
        cout << "测试用例1 (尖括号): " << s1 << " -> " << (result1 ? "true" : "false") << " (期望: true)" << endl;
        
        // 测试混合括号（包含非括号字符）
        string s2 = "(hello{world})";
        bool result2 = isValidExtended(s2);
        cout << "测试用例2 (混合字符): " << s2 << " -> " << (result2 ? "true" : "false") << " (期望: true)" << endl;
        
        // 测试无效扩展括号
        string s3 = "<]";
        bool result3 = isValidExtended(s3);
        cout << "测试用例3 (无效扩展): " << s3 << " -> " << (result3 ? "true" : "false") << " (期望: false)" << endl;
    }
    
    /**
     * 主运行函数
     */
    static void run() {
        // 运行单元测试
        testIsValid();
        
        // 运行性能对比测试
        performanceComparison();
        
        // 运行正确性验证
        correctnessVerification();
        
        // 运行扩展功能测试
        testExtendedFunctionality();
        
        cout << "\n=== 有效的括号算法验证完成 ===" << endl;
    }
};

// 程序入口点
int main() {
    Code20_ValidParentheses::run();
    return 0;
}