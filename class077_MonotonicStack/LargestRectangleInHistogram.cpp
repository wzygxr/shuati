#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
using namespace std;

/**
 * Largest Rectangle in Histogram (柱状图中最大的矩形)
 * 
 * 题目描述:
 * 给定 n 个非负整数，用来表示柱状图中各个柱子的高度，每个柱子彼此相邻，且宽度为 1 。
 * 求在该柱状图中，能够勾勒出来的矩形的最大面积。
 * 
 * 解题思路:
 * 使用单调栈来解决。维护一个单调递增栈，栈中存储柱子的索引。
 * 当遇到一个比栈顶元素高度小的柱子时，说明以栈顶元素为高度的矩形右边界确定了。
 * 此时可以计算以栈顶元素为高度的矩形面积。
 * 
 * 时间复杂度: O(n)，每个元素最多入栈和出栈各一次
 * 空间复杂度: O(n)，栈的空间
 * 
 * 测试链接: https://leetcode.cn/problems/largest-rectangle-in-histogram/
 */
class Solution {
public:
    int largestRectangleArea(vector<int>& heights) {
        int n = heights.size();
        stack<int> stk; // 单调递增栈，存储索引
        int maxArea = 0;
        
        // 遍历每个柱子
        for (int i = 0; i < n; i++) {
            // 当栈不为空且当前高度小于栈顶索引对应的高度时
            while (!stk.empty() && heights[stk.top()] > heights[i]) {
                int height = heights[stk.top()];
                stk.pop();
                // 计算宽度：如果栈为空，宽度为i；否则宽度为i - stk.top() - 1
                int width = stk.empty() ? i : i - stk.top() - 1;
                maxArea = max(maxArea, height * width);
            }
            stk.push(i); // 将当前索引压入栈
        }
        
        // 处理栈中剩余元素
        while (!stk.empty()) {
            int height = heights[stk.top()];
            stk.pop();
            // 计算宽度：如果栈为空，宽度为n；否则宽度为n - stk.top() - 1
            int width = stk.empty() ? n : n - stk.top() - 1;
            maxArea = max(maxArea, height * width);
        }
        
        return maxArea;
    }
};

// 测试函数
void test() {
    Solution solution;
    
    // 测试用例1
    vector<int> heights1 = {2, 1, 5, 6, 2, 3};
    int result1 = solution.largestRectangleArea(heights1);
    // 预期输出: 10
    cout << "测试用例1输出: " << result1 << endl;
    
    // 测试用例2
    vector<int> heights2 = {2, 4};
    int result2 = solution.largestRectangleArea(heights2);
    // 预期输出: 4
    cout << "测试用例2输出: " << result2 << endl;
    
    // 测试用例3
    vector<int> heights3 = {1, 2, 3, 4, 5};
    int result3 = solution.largestRectangleArea(heights3);
    // 预期输出: 9
    cout << "测试用例3输出: " << result3 << endl;
}

int main() {
    test();
    return 0;
}