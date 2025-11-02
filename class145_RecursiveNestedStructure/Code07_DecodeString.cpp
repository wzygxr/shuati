// LeetCode 394. Decode String (字符串解码)
// 题目来源：https://leetcode.cn/problems/decode-string/
//
// 题目描述：
// 给定一个经过编码的字符串，返回它解码后的字符串。
// 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。
// 注意 k 保证为正整数。
// 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
// 此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k 。
//
// 解题思路：
// 使用栈处理嵌套结构，遇到数字时构建完整数字，遇到字母时构建字符串
// 遇到左括号时，将当前数字和字符串入栈
// 遇到右括号时，出栈并重复字符串
//
// 时间复杂度：O(n)，其中n是输出字符串的长度
// 空间复杂度：O(n)，栈的空间复杂度

#include <iostream>
#include <stack>
#include <string>
using namespace std;

class Solution {
public:
    string decodeString(string s) {
        stack<int> numStack;
        stack<string> strStack;
        string currentStr = "";
        int currentNum = 0;
        
        for (char c : s) {
            if (isdigit(c)) {
                currentNum = currentNum * 10 + (c - '0');
            } else if (c == '[') {
                // 将当前数字和字符串入栈
                numStack.push(currentNum);
                strStack.push(currentStr);
                currentNum = 0;
                currentStr = "";
            } else if (c == ']') {
                // 出栈并重复字符串
                int repeatTimes = numStack.top();
                numStack.pop();
                string previousStr = strStack.top();
                strStack.pop();
                
                // 重复当前字符串
                string repeatedStr = "";
                for (int i = 0; i < repeatTimes; i++) {
                    repeatedStr += currentStr;
                }
                
                currentStr = previousStr + repeatedStr;
            } else {
                // 字母字符，添加到当前字符串
                currentStr += c;
            }
        }
        
        return currentStr;
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    string s1 = "3[a]2[bc]";
    cout << "输入: " << s1 << endl;
    cout << "输出: " << solution.decodeString(s1) << endl;
    cout << "期望: aaabcbc" << endl << endl;
    
    // 测试用例2
    string s2 = "3[a2[c]]";
    cout << "输入: " << s2 << endl;
    cout << "输出: " << solution.decodeString(s2) << endl;
    cout << "期望: accaccacc" << endl << endl;
    
    // 测试用例3
    string s3 = "2[abc]3[cd]ef";
    cout << "输入: " << s3 << endl;
    cout << "输出: " << solution.decodeString(s3) << endl;
    cout << "期望: abcabccdcdcdef" << endl << endl;
    
    return 0;
}

/**
 * 算法分析：
 * 
 * 时间复杂度分析：
 * - 每个字符最多被处理一次，因此时间复杂度为O(n)
 * - 字符串重复操作的时间复杂度取决于重复次数和字符串长度
 * - 总时间复杂度为O(n)，其中n是输出字符串的长度
 * 
 * 空间复杂度分析：
 * - 栈的空间复杂度为O(n)
 * - 字符串构建过程中的空间复杂度为O(n)
 * - 总空间复杂度为O(n)
 * 
 * 算法优化思路：
 * 1. 可以优化字符串拼接操作，使用stringstream或预分配空间
 * 2. 对于大规模输入，可以考虑使用更高效的数据结构
 * 3. 可以优化数字构建过程，避免重复计算
 * 
 * 工程化考量：
 * 1. 异常处理：添加对非法格式的检查，如不匹配的括号
 * 2. 边界条件：处理空字符串、单个字符等特殊情况
 * 3. 性能优化：对于大规模字符串，可以考虑使用移动语义
 * 4. 内存管理：在C++中需要注意字符串拷贝的开销
 * 
 * 相关题目对比：
 * 1. LeetCode 726. Number of Atoms：处理化学式中的原子计数
 * 2. LeetCode 856. Score of Parentheses：计算括号的分数
 * 3. LeetCode 385. Mini Parser：解析嵌套的整数列表结构
 * 
 * 算法应用场景：
 * 1. 字符串模板引擎
 * 2. 配置文件解析
 * 3. 数据压缩解压
 * 4. 代码生成器
 * 
 * C++语言特性利用：
 * 1. 使用stack容器管理嵌套结构
 * 2. 利用isdigit函数判断字符类型
 * 3. 使用引用传递避免不必要的拷贝
 * 4. 利用RAII原则自动管理资源
 */