#include <iostream>
#include <string>
#include <stack>
#include <vector>
#include <algorithm>
#include <chrono>
#include <random>
#include <mutex>
using namespace std;

using namespace std;

/**
 * 使括号有效的最少删除 - C++实现
 * 
 * 题目描述：
 * 给你一个由 '('、')' 和小写字母组成的字符串 s。
 * 你需要从字符串中删除最少数目的 '(' 或者 ')'（可以删除任意位置的括号)，使得剩下的「括号字符串」有效。
 * 请返回任意一个合法字符串。
 * 
 * 测试链接：https://leetcode.cn/problems/minimum-remove-to-make-valid-parentheses/
 * 题目来源：LeetCode
 * 难度：中等
 * 
 * 核心算法：栈 + 标记删除
 * 
 * 解题思路：
 * 1. 使用栈来记录左括号的位置
 * 2. 遍历字符串，遇到左括号入栈，遇到右括号时：
 *    - 如果栈不为空，弹出栈顶（匹配成功）
 *    - 如果栈为空，标记这个右括号需要删除
 * 3. 遍历结束后，栈中剩余的左括号都需要删除
 * 4. 根据标记构建结果字符串
 * 
 * 时间复杂度分析：
 * O(n) - 需要遍历字符串两次，n为字符串长度
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间最多为n，标记数组需要n空间
 * 
 * C++语言特性：
 * - 使用std::stack容器
 * - 使用std::string和std::string_view
 * - 使用RAII原则管理资源
 * - 使用const引用避免不必要的拷贝
 */
class Code18_MinimumRemoveToMakeValidParentheses {
public:
    /**
     * 主解法：使用栈和标记数组
     * @param s 输入字符串
     * @return 有效的括号字符串
     */
    static string minRemoveToMakeValid(const string& s) {
        if (s.empty()) {
            return s;
        }
        
        int n = s.length();
        // 标记数组：true表示保留，false表示删除
        vector<bool> keep(n, true);
        
        // 使用栈记录左括号的位置
        stack<int> st;
        
        // 第一次遍历：标记需要删除的括号
        for (int i = 0; i < n; i++) {
            char c = s[i];
            if (c == '(') {
                // 左括号入栈，暂时标记为保留
                st.push(i);
            } else if (c == ')') {
                if (st.empty()) {
                    // 没有匹配的左括号，这个右括号需要删除
                    keep[i] = false;
                } else {
                    // 有匹配的左括号，弹出栈顶
                    st.pop();
                }
            }
            // 字母字符保持保留状态
        }
        
        // 栈中剩余的左括号都需要删除
        while (!st.empty()) {
            keep[st.top()] = false;
            st.pop();
        }
        
        // 构建结果字符串
        string result;
        for (int i = 0; i < n; i++) {
            if (keep[i]) {
                result += s[i];
            }
        }
        
        return result;
    }
    
    /**
     * 优化解法：两次遍历法（空间优化）
     * 第一次遍历：删除多余的右括号
     * 第二次遍历：删除多余的左括号
     */
    static string minRemoveToMakeValidOptimized(const string& s) {
        if (s.empty()) {
            return s;
        }
        
        // 第一次遍历：删除多余的右括号
        string firstPass;
        int balance = 0;  // 括号平衡计数器
        
        for (char c : s) {
            if (c == '(') {
                balance++;
                firstPass += c;
            } else if (c == ')') {
                if (balance > 0) {
                    balance--;
                    firstPass += c;
                }
                // 如果balance <= 0，不添加这个右括号
            } else {
                firstPass += c;
            }
        }
        
        // 第二次遍历：删除多余的左括号（从右向左）
        string result;
        int removeLeft = balance;  // 需要删除的左括号数量
        
        for (int i = firstPass.length() - 1; i >= 0; i--) {
            char c = firstPass[i];
            if (c == '(' && removeLeft > 0) {
                removeLeft--;
            } else {
                result += c;
            }
        }
        
        reverse(result.begin(), result.end());
        return result;
    }
    
    /**
     * 单元测试函数
     */
    static void testMinRemoveToMakeValid() {
        cout << "=== 使括号有效的最少删除单元测试 ===" << endl;
        
        // 测试用例1：常规情况
        string s1 = "lee(t(c)o)de)";
        string result1 = minRemoveToMakeValid(s1);
        cout << "测试用例1: " << s1 << endl;
        cout << "输出: " << result1 << endl;
        cout << "期望: lee(t(c)o)de 或 lee(t(co)de) 或 lee(t(c)ode)" << endl;
        
        // 测试用例2：需要删除多个括号
        string s2 = "a)b(c)d";
        string result2 = minRemoveToMakeValid(s2);
        cout << "\n测试用例2: " << s2 << endl;
        cout << "输出: " << result2 << endl;
        cout << "期望: ab(c)d" << endl;
        
        // 测试用例3：删除所有括号
        string s3 = "))((";
        string result3 = minRemoveToMakeValid(s3);
        cout << "\n测试用例3: " << s3 << endl;
        cout << "输出: " << result3 << endl;
        cout << "期望: \"\" (空字符串)" << endl;
        
        // 测试用例4：已经是有效括号
        string s4 = "(a(b(c)d))";
        string result4 = minRemoveToMakeValid(s4);
        cout << "\n测试用例4: " << s4 << endl;
        cout << "输出: " << result4 << endl;
        cout << "期望: (a(b(c)d))" << endl;
        
        // 测试用例5：纯字母字符串
        string s5 = "abcdefg";
        string result5 = minRemoveToMakeValid(s5);
        cout << "\n测试用例5: " << s5 << endl;
        cout << "输出: " << result5 << endl;
        cout << "期望: abcdefg" << endl;
    }
    
    /**
     * 性能对比测试：标记数组法 vs 两次遍历法
     */
    static void performanceComparison() {
        cout << "\n=== 性能对比测试 ===" << endl;
        
        // 生成测试数据
        int n = 100000;
        string testData;
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<int> dist(0, 2);
        
        // 生成包含括号和字母的混合字符串
        for (int i = 0; i < n; i++) {
            int type = dist(gen);
            switch (type) {
                case 0: testData += '('; break;
                case 1: testData += ')'; break;
                case 2: testData += 'a' + dist(gen) % 26; break;
            }
        }
        
        // 测试标记数组法
        auto startTime1 = chrono::high_resolution_clock::now();
        string result1 = minRemoveToMakeValid(testData);
        auto endTime1 = chrono::high_resolution_clock::now();
        auto duration1 = chrono::duration_cast<chrono::milliseconds>(endTime1 - startTime1);
        
        // 测试两次遍历法
        auto startTime2 = chrono::high_resolution_clock::now();
        string result2 = minRemoveToMakeValidOptimized(testData);
        auto endTime2 = chrono::high_resolution_clock::now();
        auto duration2 = chrono::duration_cast<chrono::milliseconds>(endTime2 - startTime2);
        
        cout << "数据规模: " << n << "个字符" << endl;
        cout << "标记数组法执行时间: " << duration1.count() << "ms" << endl;
        cout << "两次遍历法执行时间: " << duration2.count() << "ms" << endl;
        cout << "结果长度对比: " << result1.length() << " vs " << result2.length() << endl;
        cout << "结果是否相等: " << (result1 == result2) << endl;
    }
    
    /**
     * 正确性验证：验证两种解法结果是否一致
     */
    static void correctnessVerification() {
        cout << "\n=== 正确性验证 ===" << endl;
        
        vector<string> testCases = {
            "lee(t(c)o)de)",
            "a)b(c)d",
            "))((",
            "(a(b(c)d))",
            "abcdefg",
            "",
            "((a)(b)((c)))d))",
            "()()()()",
            "(((((())))))",
            "))))((((()"
        };
        
        bool allPassed = true;
        for (size_t i = 0; i < testCases.size(); i++) {
            const string& s = testCases[i];
            string result1 = minRemoveToMakeValid(s);
            string result2 = minRemoveToMakeValidOptimized(s);
            
            bool isValid1 = isValidParentheses(result1);
            bool isValid2 = isValidParentheses(result2);
            bool resultsEqual = (result1 == result2);
            
            if (!isValid1 || !isValid2 || !resultsEqual) {
                cout << "测试用例 " << i << " 失败:" << endl;
                cout << "输入: " << s << endl;
                cout << "解法1结果: " << result1 << " (有效: " << isValid1 << ")" << endl;
                cout << "解法2结果: " << result2 << " (有效: " << isValid2 << ")" << endl;
                cout << "结果相等: " << resultsEqual << endl;
                allPassed = false;
            }
        }
        
        if (allPassed) {
            cout << "所有测试用例通过！" << endl;
        }
    }
    
    /**
     * 验证括号字符串是否有效
     */
    static bool isValidParentheses(const string& s) {
        int balance = 0;
        for (char c : s) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
                if (balance < 0) return false;
            }
        }
        return balance == 0;
    }
    
    /**
     * 主运行函数
     */
    static void run() {
        // 运行单元测试
        testMinRemoveToMakeValid();
        
        // 运行性能对比测试
        performanceComparison();
        
        // 运行正确性验证
        correctnessVerification();
        
        cout << "\n=== 算法验证完成 ===" << endl;
    }
};

// 程序入口点
int main() {
    Code18_MinimumRemoveToMakeValidParentheses::run();
    return 0;
}