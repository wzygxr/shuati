#include <iostream>
#include <string>
#include <vector>
using namespace std;

// 移除K个数字
// 题目描述：给定一个以字符串表示的非负整数 num，移除这个数中的 k 位数字，使得剩下的数字最小。
// 注意：输出不能含有前导零，但如果结果为0，必须保留这个零。
// 测试链接: https://leetcode.cn/problems/remove-k-digits/

class Code21_RemoveKDigits {
public:
    /**
     * 移除K个数字的贪心解法
     * 
     * 解题思路：
     * 1. 使用贪心策略，维护一个递增的序列
     * 2. 遍历字符串中的每一个字符，对于当前字符：
     *    a. 如果栈不为空，且栈顶元素大于当前字符，且还能移除数字（k>0），则弹出栈顶元素
     *    b. 将当前字符入栈
     * 3. 移除完k个数字后，如果还需要移除数字（可能序列是递增的），继续从栈顶移除
     * 4. 构建结果字符串，移除前导零
     * 5. 如果结果为空，返回"0"
     * 
     * 贪心策略的正确性：
     * - 为了使剩下的数字最小，我们希望前面的数字尽可能小
     * - 当遇到一个较小的数字时，如果前面有较大的数字且还能移除，应该移除前面较大的数字
     * - 这样可以保证前面的位置上的数字尽可能小，从而使整个数字最小
     * 
     * 时间复杂度：O(n)，其中n是字符串的长度
     * - 每个字符最多被入栈和出栈一次
     * 
     * 空间复杂度：O(n)，用于存储栈和结果字符串
     * 
     * @param num 以字符串表示的非负整数
     * @param k 需要移除的数字个数
     * @return 移除k个数字后得到的最小数字（字符串形式）
     */
    static string removeKdigits(string num, int k) {
        // 边界条件处理
        if (num.empty()) {
            return "0";
        }
        
        int n = num.size();
        // 如果需要移除的数字个数大于等于字符串长度，结果为"0"
        if (k >= n) {
            return "0";
        }

        // 使用字符串模拟栈，提高效率
        string stack;

        // 遍历字符串中的每一个字符
        for (char c : num) {
            // 当栈不为空，且栈顶元素大于当前字符，且还能移除数字时，弹出栈顶元素
            while (!stack.empty() && stack.back() > c && k > 0) {
                stack.pop_back();
                k--;
            }
            // 将当前字符入栈
            stack.push_back(c);
        }

        // 如果还需要移除数字，从栈顶继续移除
        while (k > 0) {
            stack.pop_back();
            k--;
        }

        // 移除前导零
        int start = 0;
        while (start < stack.size() && stack[start] == '0') {
            start++;
        }

        // 提取结果
        string result = stack.substr(start);

        // 如果结果为空，返回"0"
        return result.empty() ? "0" : result;
    }
};

// 测试方法
int main() {
    // 测试用例1
    // 输入: num = "1432219", k = 3
    // 输出: "1219"
    cout << "测试用例1结果: " << Code21_RemoveKDigits::removeKdigits("1432219", 3) << endl; // 期望输出: "1219"

    // 测试用例2
    // 输入: num = "10200", k = 1
    // 输出: "200"
    cout << "测试用例2结果: " << Code21_RemoveKDigits::removeKdigits("10200", 1) << endl; // 期望输出: "200"

    // 测试用例3
    // 输入: num = "10", k = 2
    // 输出: "0"
    cout << "测试用例3结果: " << Code21_RemoveKDigits::removeKdigits("10", 2) << endl; // 期望输出: "0"

    // 测试用例4：边界情况 - 递增序列
    // 输入: num = "12345", k = 2
    // 输出: "123"
    cout << "测试用例4结果: " << Code21_RemoveKDigits::removeKdigits("12345", 2) << endl; // 期望输出: "123"

    // 测试用例5：边界情况 - 递减序列
    // 输入: num = "54321", k = 2
    // 输出: "321"
    cout << "测试用例5结果: " << Code21_RemoveKDigits::removeKdigits("54321", 2) << endl; // 期望输出: "321"

    // 测试用例6：包含前导零的情况
    // 输入: num = "10001", k = 1
    // 输出: "0001" -> "1"
    cout << "测试用例6结果: " << Code21_RemoveKDigits::removeKdigits("10001", 1) << endl; // 期望输出: "1"

    // 测试用例7：更复杂的情况
    // 输入: num = "10200", k = 1
    // 输出: "200"
    cout << "测试用例7结果: " << Code21_RemoveKDigits::removeKdigits("10200", 1) << endl; // 期望输出: "200"

    return 0;
}