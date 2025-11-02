#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
using namespace std;

/**
 * Trapping Rain Water（接雨水）
 * 
 * 题目描述:
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 * 
 * 解题思路:
 * 使用单调栈来解决。维护一个单调递减栈，栈中存储柱子的索引。
 * 当遇到一个比栈顶元素高度大的柱子时，说明可能形成了凹槽可以接雨水。
 * 计算凹槽的面积即为接雨水量。
 * 
 * 时间复杂度: O(n)，每个元素最多入栈和出栈各一次
 * 空间复杂度: O(n)，栈的空间
 * 
 * 测试链接: https://leetcode.cn/problems/trapping-rain-water/
 */
class Solution {
public:
    int trap(vector<int>& height) {
        int n = height.size();
        stack<int> stk; // 单调递减栈，存储索引
        int water = 0;
        
        // 遍历每个柱子
        for (int i = 0; i < n; i++) {
            // 当栈不为空且当前高度大于栈顶索引对应的高度时
            while (!stk.empty() && height[stk.top()] < height[i]) {
                int bottom = height[stk.top()];
                stk.pop();
                if (stk.empty()) break; // 如果栈为空，无法形成凹槽
                
                // 计算凹槽的宽度和高度
                int width = i - stk.top() - 1;
                int minHeight = min(height[stk.top()], height[i]);
                water += width * (minHeight - bottom);
            }
            stk.push(i); // 将当前索引压入栈
        }
        
        return water;
    }
};

// 测试函数
void test() {
    Solution solution;
    
    // 测试用例1
    vector<int> height1 = {0,1,0,2,1,0,1,3,2,1,2,1};
    int result1 = solution.trap(height1);
    // 预期输出: 6
    cout << "测试用例1输出: " << result1 << endl;
    
    // 测试用例2
    vector<int> height2 = {4,2,0,3,2,5};
    int result2 = solution.trap(height2);
    // 预期输出: 9
    cout << "测试用例2输出: " << result2 << endl;
}

int main() {
    test();
    return 0;
}