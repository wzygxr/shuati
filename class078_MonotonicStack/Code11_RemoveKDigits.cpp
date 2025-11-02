#include <string>
#include <stack>
#include <algorithm>
#include <iostream>
using namespace std;

/**
 * 移掉K位数字
 * 
 * 题目描述：
 * 给你一个以字符串表示的非负整数 num 和一个整数 k ，移除这个数中的 k 位数字，
 * 使得剩下的数字最小。请你以字符串形式返回这个最小的数字。
 * 
 * 测试链接：https://leetcode.cn/problems/remove-k-digits/
 * 
 * 解题思路：
 * 使用单调栈来解决这个问题。维护一个单调递增的栈，从左到右遍历数字字符串：
 * 1. 如果当前数字比栈顶数字小，且还有可移除的位数(k>0)，则弹出栈顶数字并减少k
 * 2. 将当前数字入栈
 * 3. 如果遍历完还有剩余的k，从栈顶移除k个数字
 * 4. 处理前导零并返回结果
 * 
 * 具体步骤：
 * 1. 创建一个栈用于存储结果数字
 * 2. 遍历字符串中的每个字符
 * 3. 当栈不为空、当前字符小于栈顶字符且k>0时，弹出栈顶元素并减少k
 * 4. 将当前字符入栈（注意避免前导零）
 * 5. 如果遍历完还有剩余的k，从栈顶移除k个数字
 * 6. 将栈中元素构造成字符串并处理特殊情况
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入栈和出栈各一次，n为字符串长度
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间最多为n
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解之一
 */
class Solution {
public:
    string removeKdigits(string num, int k) {
        // 边界条件检查
        if (k >= num.length()) {
            return "0";
        }
        
        // 使用栈存储结果数字
        stack<char> st;
        
        // 遍历字符串中的每个字符
        for (int i = 0; i < num.length(); i++) {
            char digit = num[i];
            
            // 当栈不为空、当前字符小于栈顶字符且还有可移除的位数时
            while (!st.empty() && k > 0 && st.top() > digit) {
                st.pop();  // 弹出栈顶元素
                k--;       // 减少可移除位数
            }
            
            // 避免前导零：如果栈为空且当前字符是'0'，则不入栈
            if (!st.empty() || digit != '0') {
                st.push(digit);
            }
        }
        
        // 如果遍历完还有剩余的k，从栈顶移除k个数字
        while (k > 0 && !st.empty()) {
            st.pop();
            k--;
        }
        
        // 构造结果字符串
        string result = "";
        while (!st.empty()) {
            result += st.top();
            st.pop();
        }
        
        // 因为栈是后进先出，需要反转字符串
        reverse(result.begin(), result.end());
        
        // 处理空结果或全零情况
        return result.empty() ? "0" : result;
    }
};

// 测试用例
int main() {
    Solution solution;
    
    // 测试用例1
    string num1 = "1432219";
    int k1 = 3;
    cout << "测试用例1: num=" << num1 << ", k=" << k1 << endl;
    cout << "输出: " << solution.removeKdigits(num1, k1) << endl; // 期望输出: "1219"
    
    // 测试用例2
    string num2 = "10200";
    int k2 = 1;
    cout << "测试用例2: num=" << num2 << ", k=" << k2 << endl;
    cout << "输出: " << solution.removeKdigits(num2, k2) << endl; // 期望输出: "200"
    
    // 测试用例3
    string num3 = "10";
    int k3 = 2;
    cout << "测试用例3: num=" << num3 << ", k=" << k3 << endl;
    cout << "输出: " << solution.removeKdigits(num3, k3) << endl; // 期望输出: "0"
    
    // 测试用例4
    string num4 = "112";
    int k4 = 1;
    cout << "测试用例4: num=" << num4 << ", k=" << k4 << endl;
    cout << "输出: " << solution.removeKdigits(num4, k4) << endl; // 期望输出: "11"
    
    return 0;
}