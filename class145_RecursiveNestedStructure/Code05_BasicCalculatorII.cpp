// LeetCode 227. Basic Calculator II (基本计算器 II)
// 题目来源：https://leetcode.cn/problems/basic-calculator-ii/
//
// 题目描述：
// 给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。
// 整数除法仅保留整数部分。
// 你可以假设给定的表达式总是有效的。所有中间结果将在 [-2^31, 2^31 - 1] 的范围内。
// 注意：不允许使用任何将字符串作为数学表达式计算的内置函数，比如 eval() 。
//
// 示例：
// 输入：s = "3+2*2"
// 输出：7
//
// 输入：s = " 3/2 "
// 输出：1
//
// 输入：s = " 3+5 / 2 "
// 输出：5
//
// 解题思路：
// 使用栈处理运算符优先级，乘除法优先级高于加减法
// 遇到乘除法时立即计算，加减法先入栈等待
// 最后计算栈中剩余的加减法
//
// 时间复杂度：O(n)，其中n是字符串的长度
// 空间复杂度：O(n)，栈的空间复杂度

#include <iostream>
#include <stack>
#include <string>
#include <cmath>
using namespace std;

class Solution {
public:
    int calculate(string s) {
        stack<int> stk;
        char sign = '+';
        int num = 0;
        int n = s.length();
        
        for (int i = 0; i < n; ++i) {
            // 跳过空格
            if (s[i] == ' ') continue;
            
            // 如果是数字，构建完整的数字
            if (isdigit(s[i])) {
                num = num * 10 + (s[i] - '0');
            }
            
            // 如果是运算符或者到达字符串末尾
            if (!isdigit(s[i]) || i == n - 1) {
                if (sign == '+') {
                    stk.push(num);
                } else if (sign == '-') {
                    stk.push(-num);
                } else if (sign == '*') {
                    int top = stk.top();
                    stk.pop();
                    stk.push(top * num);
                } else if (sign == '/') {
                    int top = stk.top();
                    stk.pop();
                    // 处理负数除法问题，确保向零截断
                    stk.push(top / num);
                }
                
                // 更新符号和重置数字
                sign = s[i];
                num = 0;
            }
        }
        
        // 计算栈中所有数字的和
        int result = 0;
        while (!stk.empty()) {
            result += stk.top();
            stk.pop();
        }
        
        return result;
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    string s1 = "3+2*2";
    cout << "输入: " << s1 << endl;
    cout << "输出: " << solution.calculate(s1) << endl;
    cout << "期望: 7" << endl << endl;
    
    // 测试用例2
    string s2 = " 3/2 ";
    cout << "输入: " << s2 << endl;
    cout << "输出: " << solution.calculate(s2) << endl;
    cout << "期望: 1" << endl << endl;
    
    // 测试用例3
    string s3 = " 3+5 / 2 ";
    cout << "输入: " << s3 << endl;
    cout << "输出: " << solution.calculate(s3) << endl;
    cout << "期望: 5" << endl << endl;
    
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
 * - 总空间复杂度为O(n)
 * 
 * 算法优化思路：
 * 1. 可以优化数字构建过程，避免重复计算
 * 2. 对于大规模输入，可以考虑使用更高效的数据结构
 * 3. 可以优化栈操作，减少不必要的入栈出栈
 * 
 * 工程化考量：
 * 1. 异常处理：添加对非法字符和表达式的检查
 * 2. 边界条件：处理空字符串、单个数字等特殊情况
 * 3. 性能优化：对于大规模计算，可以考虑并行处理
 * 4. 内存管理：在C++中需要注意避免内存泄漏
 * 
 * 相关题目对比：
 * 1. LeetCode 224. Basic Calculator：只包含加减法和括号
 * 2. LeetCode 227. Basic Calculator II：包含加减乘除，但不包含括号（本题）
 * 3. LeetCode 772. Basic Calculator III：包含加减乘除和括号
 * 
 * 算法应用场景：
 * 1. 计算器应用开发
 * 2. 公式引擎实现
 * 3. 配置文件解析
 * 4. 模板引擎计算
 * 
 * C++语言特性利用：
 * 1. 使用stack容器管理运算符优先级
 * 2. 利用isdigit函数判断字符类型
 * 3. 使用引用传递避免不必要的拷贝
 * 4. 利用RAII原则自动管理资源
 */