// UVA 551 Nesting a Bunch of Brackets
// 题目来源：https://onlinejudge.org/external/5/551.pdf
//
// 题目描述：
// 验证括号字符串是否有效匹配。支持多种括号类型：(), [], {}, <>
// 要求括号必须正确嵌套和匹配。
//
// 解题思路：
// 使用栈来验证括号匹配，遇到左括号入栈，遇到右括号检查栈顶是否匹配
// 如果匹配则出栈，否则返回错误
//
// 时间复杂度：O(n)，每个字符处理一次
// 空间复杂度：O(n)，栈的空间复杂度

#include <iostream>
#include <stack>
#include <string>
#include <unordered_map>
using namespace std;

class Solution {
public:
    /**
     * 验证括号字符串是否有效
     * @param s 括号字符串
     * @return 是否有效匹配
     */
    bool isValid(string s) {
        stack<char> stk;
        unordered_map<char, char> matching = {
            {')', '('},
            {']', '['},
            {'}', '{'},
            {'>', '<'}
        };
        
        for (char c : s) {
            if (c == '(' || c == '[' || c == '{' || c == '<') {
                // 左括号，入栈
                stk.push(c);
            } else if (c == ')' || c == ']' || c == '}' || c == '>') {
                // 右括号，检查匹配
                if (stk.empty() || stk.top() != matching[c]) {
                    return false;
                }
                stk.pop();
            }
            // 忽略其他字符
        }
        
        // 栈必须为空才表示所有括号都匹配
        return stk.empty();
    }
    
    /**
     * 增强版本：返回详细的错误信息
     * @param s 括号字符串
     * @return 验证结果和错误位置
     */
    pair<bool, int> isValidEnhanced(string s) {
        stack<pair<char, int>> stk; // 存储字符和位置
        unordered_map<char, char> matching = {
            {')', '('},
            {']', '['},
            {'}', '{'},
            {'>', '<'}
        };
        
        for (int i = 0; i < s.length(); i++) {
            char c = s[i];
            if (c == '(' || c == '[' || c == '{' || c == '<') {
                // 左括号，入栈（带位置信息）
                stk.push({c, i});
            } else if (c == ')' || c == ']' || c == '}' || c == '>') {
                // 右括号，检查匹配
                if (stk.empty()) {
                    return {false, i}; // 多余的右括号
                }
                if (stk.top().first != matching[c]) {
                    return {false, i}; // 不匹配的括号
                }
                stk.pop();
            }
        }
        
        if (!stk.empty()) {
            return {false, stk.top().second}; // 未匹配的左括号
        }
        
        return {true, -1}; // 有效匹配
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1：有效匹配
    string s1 = "()[]{}<>";
    cout << "输入: " << s1 << endl;
    cout << "输出: " << (solution.isValid(s1) ? "有效" : "无效") << endl;
    cout << "期望: 有效" << endl << endl;
    
    // 测试用例2：无效匹配 - 不匹配的括号
    string s2 = "([)]";
    cout << "输入: " << s2 << endl;
    cout << "输出: " << (solution.isValid(s2) ? "有效" : "无效") << endl;
    cout << "期望: 无效" << endl << endl;
    
    // 测试用例3：无效匹配 - 多余的右括号
    string s3 = "()]";
    cout << "输入: " << s3 << endl;
    cout << "输出: " << (solution.isValid(s3) ? "有效" : "无效") << endl;
    cout << "期望: 无效" << endl << endl;
    
    // 测试用例4：无效匹配 - 未匹配的左括号
    string s4 = "([]";
    cout << "输入: " << s4 << endl;
    cout << "输出: " << (solution.isValid(s4) ? "有效" : "无效") << endl;
    cout << "期望: 无效" << endl << endl;
    
    // 增强版本测试
    cout << "增强版本测试:" << endl;
    auto result1 = solution.isValidEnhanced(s1);
    cout << s1 << ": " << (result1.first ? "有效" : "无效") 
         << (result1.second != -1 ? " 错误位置: " + to_string(result1.second) : "") << endl;
    
    auto result2 = solution.isValidEnhanced(s2);
    cout << s2 << ": " << (result2.first ? "有效" : "无效") 
         << (result2.second != -1 ? " 错误位置: " + to_string(result2.second) : "") << endl;
    
    auto result3 = solution.isValidEnhanced(s3);
    cout << s3 << ": " << (result3.first ? "有效" : "无效") 
         << (result3.second != -1 ? " 错误位置: " + to_string(result3.second) : "") << endl;
    
    auto result4 = solution.isValidEnhanced(s4);
    cout << s4 << ": " << (result4.first ? "有效" : "无效") 
         << (result4.second != -1 ? " 错误位置: " + to_string(result4.second) : "") << endl;
    
    return 0;
}

/**
 * 算法分析：
 * 
 * 时间复杂度分析：
 * - 每个字符最多被处理一次，因此时间复杂度为O(n)
 * - 栈操作的时间复杂度为O(1)
 * - 总时间复杂度为O(n)
 * 
 * 空间复杂度分析：
 * - 栈的空间复杂度为O(n)
 * - 哈希表的空间复杂度为O(1)（固定大小）
 * - 总空间复杂度为O(n)
 * 
 * 算法优化思路：
 * 1. 可以优化栈操作，使用数组模拟栈减少开销
 * 2. 对于特定场景，可以只支持部分括号类型
 * 3. 可以添加缓存机制，对于重复验证的字符串进行缓存
 * 
 * 工程化考量：
 * 1. 异常处理：添加对非法字符的处理
 * 2. 边界条件：处理空字符串、单个字符等特殊情况
 * 3. 性能优化：对于大规模字符串，可以考虑使用更高效的数据结构
 * 4. 内存管理：在C++中需要注意避免内存泄漏
 * 
 * 相关题目对比：
 * 1. LeetCode 20. Valid Parentheses：验证括号匹配（基础版本）
 * 2. LeetCode 32. Longest Valid Parentheses：最长有效括号子串
 * 3. POJ 2955 Brackets：找到最长的正确匹配括号子序列
 * 
 * 算法应用场景：
 * 1. 编译器语法分析
 * 2. 配置文件验证
 * 3. 代码格式化工具
 * 4. 数学表达式验证
 * 
 * C++语言特性利用：
 * 1. 使用unordered_map存储括号匹配关系
 * 2. 使用stack处理嵌套结构
 * 3. 使用pair返回多个值
 * 4. 利用RAII原则自动管理资源
 * 
 * 扩展功能：
 * 1. 支持自定义括号对
 * 2. 提供详细的错误信息（位置和类型）
 * 3. 支持忽略特定字符（如空格、注释）
 * 4. 支持多种编码格式
 */