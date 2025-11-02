// 移掉K位数字（Remove K Digits）
// 题目来源：LeetCode 402
// 题目链接：https://leetcode.cn/problems/remove-k-digits/
// 
// 问题描述：
// 给定一个以字符串表示的非负整数num，移除这个数中的k位数字，使得剩下的数字最小。
// 
// 算法思路：
// 使用贪心策略，结合单调栈：
// 1. 遍历数字字符串，维护一个单调递增栈
// 2. 当遇到比栈顶小的数字时，弹出栈顶元素（移除数字）
// 3. 直到移除k个数字或栈为空
// 4. 如果遍历完成后还没有移除k个数字，从栈顶继续移除
// 5. 处理前导零和空栈情况
// 
// 时间复杂度：O(n) - 每个元素最多入栈出栈一次
// 空间复杂度：O(n) - 栈的空间
// 
// 是否最优解：是。这是该问题的最优解法。
// 
// 适用场景：
// 1. 数字最小化问题
// 2. 字符串处理问题
// 
// 异常处理：
// 1. 处理k大于等于数字长度的情况
// 2. 处理前导零情况
// 3. 处理空字符串情况
// 
// 工程化考量：
// 1. 输入验证：检查参数是否合法
// 2. 边界条件：处理各种边界情况
// 3. 性能优化：使用字符串构建提高效率
// 
// 相关题目：
// 1. LeetCode 321. 拼接最大数 - 类似数字拼接问题
// 2. LeetCode 316. 去除重复字母 - 类似字符处理问题
// 3. LeetCode 738. 单调递增的数字 - 数字单调性问题
// 4. 牛客网 NC140 排序 - 各种排序算法实现
// 5. LintCode 1254. 移除K位数字 - 与本题相同
// 6. HackerRank - Largest Permutation - 最大排列问题
// 7. CodeChef - DIGITREM - 数字移除问题
// 8. AtCoder ABC155D - Pairs - 数字配对问题
// 9. Codeforces 1324C - Frog Jumps - 贪心跳跃问题
// 10. POJ 1700 - Crossing River - 经典过河问题

#include <string>
#include <stack>
#include <algorithm>
using namespace std;

/**
 * 移除k位数字使得剩下的数字最小
 * 
 * @param num 数字字符串
 * @param k 要移除的数字个数
 * @return 移除后的最小数字字符串
 */
string removeKdigits(string num, int k) {
    // 边界条件检查
    if (num.empty() || k >= num.length()) {
        return "0";
    }
    
    if (k == 0) {
        return num; // 不需要移除任何数字
    }
    
    stack<char> stack;
    
    for (int i = 0; i < num.length(); i++) {
        char currentChar = num[i];
        
        // 当栈不为空，且k>0，且当前数字小于栈顶数字时，弹出栈顶
        while (!stack.empty() && k > 0 && currentChar < stack.top()) {
            stack.pop();
            k--;
        }
        
        // 将当前数字压入栈中
        stack.push(currentChar);
    }
    
    // 如果还有k个数字需要移除，从栈顶移除（因为栈是单调递增的）
    while (k > 0 && !stack.empty()) {
        stack.pop();
        k--;
    }
    
    // 构建结果字符串
    string result;
    while (!stack.empty()) {
        result += stack.top();
        stack.pop();
    }
    reverse(result.begin(), result.end());
    
    // 处理前导零
    int startIndex = 0;
    while (startIndex < result.length() && result[startIndex] == '0') {
        startIndex++;
    }
    
    // 如果所有数字都是0，返回"0"
    if (startIndex == result.length()) {
        return "0";
    }
    
    return result.substr(startIndex);
}

// 测试函数
int main() {
    // 测试用例1: 基本情况
    string num1 = "1432219";
    int k1 = 3;
    string result1 = removeKdigits(num1, k1);
    
    // 测试用例2: 简单情况
    string num2 = "10200";
    int k2 = 1;
    string result2 = removeKdigits(num2, k2);
    
    // 测试用例3: 复杂情况
    string num3 = "10";
    int k3 = 2;
    string result3 = removeKdigits(num3, k3);
    
    // 测试用例4: 边界情况 - 移除所有数字
    string num4 = "12345";
    int k4 = 5;
    string result4 = removeKdigits(num4, k4);
    
    // 测试用例5: 边界情况 - 不移除数字
    string num5 = "12345";
    int k5 = 0;
    string result5 = removeKdigits(num5, k5);
    
    // 测试用例6: 复杂情况 - 前导零处理
    string num6 = "100200";
    int k6 = 1;
    string result6 = removeKdigits(num6, k6);
    
    return 0;
}