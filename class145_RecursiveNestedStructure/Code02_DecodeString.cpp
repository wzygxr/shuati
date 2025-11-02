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
// 使用递归处理嵌套结构，遇到左括号时递归处理括号内的字符串
// 用全局变量where记录当前处理到的位置，用于递归返回时告知上游函数从哪继续处理
// 数字表示后续字符串的重复次数
//
// 时间复杂度：O(n)，其中n是输出字符串的长度
// 空间复杂度：O(n)，递归调用栈的深度

#include <iostream>
#include <string>
using namespace std;

class Solution {
public:
    int where; // 全局变量，记录当前处理到的位置，用于递归返回时告知上游函数从哪继续处理
    
    string decodeString(string str) {
        where = 0;
        return f(str, 0);
    }
    
    // s[i....]开始计算，遇到字符串终止 或者 遇到 ] 停止
    // 返回 : 自己负责的这一段字符串的结果
    // 返回之间，更新全局变量where，为了上游函数知道从哪继续！
    string f(string s, int i) {
        string path = "";
        int cnt = 0;
        
        while (i < s.length() && s[i] != ']') {
            if ((s[i] >= 'a' && s[i] <= 'z') || (s[i] >= 'A' && s[i] <= 'Z')) {
                path += s[i++];
            } else if (s[i] >= '0' && s[i] <= '9') {
                cnt = cnt * 10 + s[i++] - '0';
            } else {
                // 遇到 [ 
                // cnt = 7 * ? 
                path += get(cnt, f(s, i + 1));
                i = where + 1;
                cnt = 0;
            }
        }
        
        where = i;
        return path;
    }
    
    // 将字符串重复指定次数
    string get(int cnt, string str) {
        string result = "";
        for (int i = 0; i < cnt; i++) {
            result += str;
        }
        return result;
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    string s1 = "3[a2[c]]";
    cout << "输入: " << s1 << endl;
    cout << "输出: " << solution.decodeString(s1) << endl;
    cout << "期望: accaccacc" << endl << endl;
    
    // 测试用例2
    string s2 = "2[abc]3[cd]ef";
    cout << "输入: " << s2 << endl;
    cout << "输出: " << solution.decodeString(s2) << endl;
    cout << "期望: abcabccdcdcdef" << endl << endl;
    
    // 测试用例3
    string s3 = "abc3[cd]xyz";
    cout << "输入: " << s3 << endl;
    cout << "输出: " << solution.decodeString(s3) << endl;
    cout << "期望: abccdcdcdxyz" << endl << endl;
    
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
 * - 递归调用栈的深度为O(n)
 * - 字符串构建过程中的空间复杂度为O(n)
 * - 总空间复杂度为O(n)
 * 
 * 算法优化思路：
 * 1. 使用迭代+栈的方法可以避免递归，减少栈空间的使用
 * 2. 可以优化字符串拼接操作，使用stringstream或预分配空间
 * 3. 对于大规模输入，可以考虑使用更高效的数据结构
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
 * 1. 使用string类管理字符串，避免手动内存管理
 * 2. 利用运算符重载简化字符串操作
 * 3. 使用引用传递避免不必要的拷贝
 * 4. 利用RAII原则自动管理资源
 */