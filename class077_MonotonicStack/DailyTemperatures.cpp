#include <iostream>
#include <vector>
#include <stack>
using namespace std;

/**
 * Daily Temperatures (每日温度)
 * 
 * 题目描述:
 * 给定一个整数数组 temperatures ，表示每天的温度，返回一个数组 answer，
 * 其中 answer[i] 是指对于第 i 天，下一个更高温度出现在几天后。
 * 如果气温在这之后都不会升高，请在该位置用 0 来代替。
 * 
 * 解题思路:
 * 使用单调栈来解决。维护一个单调递减栈，栈中存储索引。
 * 当遇到一个比栈顶元素温度高的温度时，说明找到了栈顶元素的下一个更高温度。
 * 
 * 时间复杂度: O(n)，每个元素最多入栈和出栈各一次
 * 空间复杂度: O(n)，栈的空间
 * 
 * 测试链接: https://leetcode.cn/problems/daily-temperatures/
 */
class Solution {
public:
    vector<int> dailyTemperatures(vector<int>& temperatures) {
        int n = temperatures.size();
        vector<int> answer(n, 0);
        stack<int> stk; // 单调递减栈，存储索引
        
        for (int i = 0; i < n; i++) {
            // 当栈不为空且当前温度大于栈顶索引对应的温度时
            while (!stk.empty() && temperatures[stk.top()] < temperatures[i]) {
                int index = stk.top();
                stk.pop();
                answer[index] = i - index; // 计算天数差
            }
            stk.push(i); // 将当前索引压入栈
        }
        
        return answer;
    }
};

// 测试函数
void printVector(const vector<int>& vec) {
    for (int val : vec) {
        cout << val << " ";
    }
    cout << endl;
}

int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> temperatures1 = {73, 74, 75, 71, 69, 72, 76, 73};
    vector<int> result1 = solution.dailyTemperatures(temperatures1);
    // 预期输出: [1, 1, 4, 2, 1, 1, 0, 0]
    cout << "测试用例1输出: ";
    printVector(result1);
    
    // 测试用例2
    vector<int> temperatures2 = {30, 40, 50, 60};
    vector<int> result2 = solution.dailyTemperatures(temperatures2);
    // 预期输出: [1, 1, 1, 0]
    cout << "测试用例2输出: ";
    printVector(result2);
    
    // 测试用例3
    vector<int> temperatures3 = {30, 60, 90};
    vector<int> result3 = solution.dailyTemperatures(temperatures3);
    // 预期输出: [1, 1, 0]
    cout << "测试用例3输出: ";
    printVector(result3);
    
    return 0;
}