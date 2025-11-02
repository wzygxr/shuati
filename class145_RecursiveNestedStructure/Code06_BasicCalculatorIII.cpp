// LeetCode 772. Basic Calculator III (基本计算器 III)
// 题目来源：https://leetcode.cn/problems/basic-calculator-iii/
//
// 题目描述：
// 实现一个基本的计算器来计算简单的表达式字符串。
// 表达式字符串可以包含左括号 ( 和右括号 )，加号 + 或减号 -，非负整数和空格。
// 表达式字符串只包含非负整数，+，-，*，/ 操作符，左括号 ( ，右括号 ) 和空格。
// 整数除法应该截断小数部分。
// 你可以假定给定的表达式总是有效的。
// 所有中间结果的范围为 [-2^31, 2^31 - 1]。
//
// 解题思路：
// 使用递归处理嵌套结构，遇到左括号时递归处理括号内的表达式
// 使用两个vector分别存储数字和操作符
// 乘除法优先级高于加减法，需要特殊处理
//
// 时间复杂度：O(n)，其中n是字符串的长度
// 空间复杂度：O(n)，递归调用栈的深度和存储数字操作符的额外空间

#include <iostream>
#include <vector>
#include <string>
using namespace std;

class Solution {
public:
    int where; // 全局变量，记录当前处理到的位置，用于递归返回时告知上游函数从哪继续处理
    
    int calculate(string s) {
        where = 0;
        return f(s, 0);
    }
    
    // s[i....]开始计算，遇到字符串终止 或者 遇到)停止
    // 返回 : 自己负责的这一段，计算的结果
    // 返回之间，更新全局变量where，为了上游函数知道从哪继续！
    int f(string s, int i) {
        int cur = 0;
        vector<int> numbers;
        vector<char> ops;
        
        while (i < s.length() && s[i] != ')') {
            if (s[i] >= '0' && s[i] <= '9') {
                cur = cur * 10 + s[i++] - '0';
            } else if (s[i] != '(') {
                // 遇到了运算符 + - * /
                push(numbers, ops, cur, s[i++]);
                cur = 0;
            } else {
                // i (.....)
                // 遇到了左括号！
                cur = f(s, i + 1);
                i = where + 1;
            }
        }
        
        push(numbers, ops, cur, '+');
        where = i;
        return compute(numbers, ops);
    }
    
    // 根据操作符处理数字，乘除法优先级高需要特殊处理
    void push(vector<int>& numbers, vector<char>& ops, int cur, char op) {
        int n = numbers.size();
        if (n == 0 || ops[n - 1] == '+' || ops[n - 1] == '-') {
            numbers.push_back(cur);
            ops.push_back(op);
        } else {
            int topNumber = numbers[n - 1];
            char topOp = ops[n - 1];
            if (topOp == '*') {
                numbers[n - 1] = topNumber * cur;
            } else {
                numbers[n - 1] = topNumber / cur;
            }
            ops[n - 1] = op;
        }
    }
    
    // 计算最终结果，只处理加减法
    int compute(vector<int> numbers, vector<char> ops) {
        int n = numbers.size();
        int ans = numbers[0];
        for (int i = 1; i < n; i++) {
            ans += ops[i - 1] == '+' ? numbers[i] : -numbers[i];
        }
        return ans;
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    string s1 = "1 + 1";
    cout << "输入: " << s1 << endl;
    cout << "输出: " << solution.calculate(s1) << endl;
    cout << "期望: 2" << endl << endl;
    
    // 测试用例2
    string s2 = "6-4/2";
    cout << "输入: " << s2 << endl;
    cout << "输出: " << solution.calculate(s2) << endl;
    cout << "期望: 4" << endl << endl;
    
    // 测试用例3
    string s3 = "2*(5+5*2)/3+(6/2+8)";
    cout << "输入: " << s3 << endl;
    cout << "输出: " << solution.calculate(s3) << endl;
    cout << "期望: 21" << endl << endl;
    
    return 0;
}

/**
 * 算法分析：
 * 
 * 时间复杂度分析：
 * - 每个字符最多被处理一次，因此时间复杂度为O(n)
 * - 递归深度取决于括号嵌套的层数，最坏情况下为O(n)
 * - 总时间复杂度为O(n)
 * 
 * 空间复杂度分析：
 * - 递归调用栈的深度为O(n)
 * - numbers和ops向量的空间复杂度为O(n)
 * - 总空间复杂度为O(n)
 * 
 * 算法优化思路：
 * 1. 使用迭代+栈的方法可以避免递归，减少栈空间的使用
 * 2. 可以优化数字构建过程，避免重复计算
 * 3. 对于大规模输入，可以考虑使用更高效的数据结构
 * 
 * 工程化考量：
 * 1. 异常处理：添加对非法字符和表达式的检查
 * 2. 边界条件：处理空字符串、单个数字等特殊情况
 * 3. 性能优化：对于大规模计算，可以考虑并行处理
 * 4. 内存管理：在C++中需要注意避免内存泄漏
 * 
 * 相关题目对比：
 * 1. LeetCode 224. Basic Calculator：只包含加减法和括号
 * 2. LeetCode 227. Basic Calculator II：包含加减乘除，但不包含括号
 * 3. LeetCode 772. Basic Calculator III：包含加减乘除和括号（本题）
 * 
 * 算法应用场景：
 * 1. 计算器应用开发
 * 2. 公式引擎实现
 * 3. 配置文件解析
 * 4. 模板引擎计算
 * 
 * C++语言特性利用：
 * 1. 使用vector容器管理动态数组
 * 2. 利用引用传递避免不必要的拷贝
 * 3. 使用标准库函数简化字符串处理
 * 4. 利用RAII原则自动管理资源
 */