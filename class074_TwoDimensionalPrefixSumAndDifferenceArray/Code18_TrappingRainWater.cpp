#include <vector>
#include <iostream>
#include <deque>
#include <algorithm>
#include <chrono>
using namespace std;

/**
 * LeetCode 42. 接雨水 (Trapping Rain Water) - C++版本
 * 
 * 题目描述:
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 * 
 * 示例1:
 * 输入: height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出: 6
 * 解释: 上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。
 * 
 * 示例2:
 * 输入: height = [4,2,0,3,2,5]
 * 输出: 9
 * 
 * 提示:
 * n == height.length
 * 1 <= n <= 2 * 10^4
 * 0 <= height[i] <= 10^5
 * 
 * 题目链接: https://leetcode.com/problems/trapping-rain-water/
 * 
 * 解题思路:
 * 这道题可以通过多种方法解决，包括:
 * 1. 暴力解法：计算每个位置能接的雨水量，然后求和
 * 2. 动态规划：预先计算每个位置左右两侧的最高柱子高度
 * 3. 双指针法：使用两个指针从两端向中间移动
 * 4. 单调栈：使用栈来寻找可以接水的凹槽
 * 
 * 这里实现三种解法：双指针法（最优解）、动态规划和单调栈。
 * 
 * 解法一: 双指针法
 * 时间复杂度: O(n)，其中 n 是数组的长度。只需要遍历一次数组。
 * 空间复杂度: O(1)，只使用了常数级别的额外空间。
 * 
 * 解法二: 动态规划
 * 时间复杂度: O(n)，需要两次遍历数组来填充左右最大高度数组。
 * 空间复杂度: O(n)，需要两个长度为 n 的数组来存储左右最大高度。
 * 
 * 解法三: 单调栈
 * 时间复杂度: O(n)，每个元素最多入栈和出栈一次。
 * 空间复杂度: O(n)，最坏情况下，栈的大小可能达到数组长度。
 */
class Solution {
public:
    /**
     * 解法一: 双指针法
     * 使用两个指针从两端向中间移动，每次比较左右两侧的最大值，决定当前位置能接的雨水量。
     * 
     * 算法思路：
     * 1. 使用left和right指针分别指向数组的两端
     * 2. 使用leftMax和rightMax记录左右两侧的最大高度
     * 3. 每次移动较小高度的指针，计算当前位置能接的雨水量
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * @param height 柱子高度数组
     * @return 能接的雨水总量
     */
    int trapTwoPointers(vector<int>& height) {
        if (height.size() < 3) {
            return 0;
        }
        
        int left = 0;
        int right = height.size() - 1;
        int leftMax = 0;
        int rightMax = 0;
        int waterTrapped = 0;
        
        while (left < right) {
            // 更新左右两侧的最大高度
            leftMax = max(leftMax, height[left]);
            rightMax = max(rightMax, height[right]);
            
            // 哪边的最大值较小，哪边可以接水
            if (leftMax < rightMax) {
                // 左侧最大值较小，计算左侧当前位置能接的水量
                waterTrapped += leftMax - height[left];
                left++;
            } else {
                // 右侧最大值较小，计算右侧当前位置能接的水量
                waterTrapped += rightMax - height[right];
                right--;
            }
        }
        
        return waterTrapped;
    }
    
    /**
     * 解法二: 动态规划
     * 预先计算每个位置左右两侧的最高柱子高度，然后计算每个位置能接的雨水量。
     * 
     * 算法思路：
     * 1. 创建leftMax数组，存储每个位置左侧的最高柱子高度
     * 2. 创建rightMax数组，存储每个位置右侧的最高柱子高度
     * 3. 遍历数组，计算每个位置能接的雨水量
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 
     * @param height 柱子高度数组
     * @return 能接的雨水总量
     */
    int trapDynamicProgramming(vector<int>& height) {
        if (height.size() < 3) {
            return 0;
        }
        
        int n = height.size();
        vector<int> leftMax(n);  // 存储每个位置左侧的最高柱子高度
        vector<int> rightMax(n); // 存储每个位置右侧的最高柱子高度
        int waterTrapped = 0;
        
        // 计算每个位置左侧的最高柱子高度
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = max(leftMax[i-1], height[i]);
        }
        
        // 计算每个位置右侧的最高柱子高度
        rightMax[n-1] = height[n-1];
        for (int i = n-2; i >= 0; i--) {
            rightMax[i] = max(rightMax[i+1], height[i]);
        }
        
        // 计算每个位置能接的雨水量
        for (int i = 0; i < n; i++) {
            // 当前位置能接的雨水量 = min(左侧最高柱子高度, 右侧最高柱子高度) - 当前柱子高度
            waterTrapped += min(leftMax[i], rightMax[i]) - height[i];
        }
        
        return waterTrapped;
    }
    
    /**
     * 解法三: 单调栈
     * 使用栈来寻找可以接水的凹槽，栈中存储的是索引。
     * 
     * 算法思路：
     * 1. 使用单调递减栈存储柱子的索引
     * 2. 当遇到比栈顶高的柱子时，说明找到了一个凹槽
     * 3. 计算凹槽的宽度和高度，累加雨水量
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 
     * @param height 柱子高度数组
     * @return 能接的雨水总量
     */
    int trapMonotonicStack(vector<int>& height) {
        if (height.size() < 3) {
            return 0;
        }
        
        int n = height.size();
        int waterTrapped = 0;
        deque<int> stack; // 存储索引，使用deque作为栈
        
        for (int i = 0; i < n; i++) {
            // 当栈不为空且当前高度大于栈顶索引对应的高度时，说明找到了一个可以接水的凹槽
            while (!stack.empty() && height[i] > height[stack.back()]) {
                int bottom = stack.back(); // 凹槽的底部索引
                stack.pop_back();
                
                if (stack.empty()) {
                    break; // 没有左边界，无法接水
                }
                
                // 计算凹槽的宽度
                int width = i - stack.back() - 1;
                // 计算凹槽的高度：min(左边界高度, 右边界高度) - 底部高度
                int depth = min(height[stack.back()], height[i]) - height[bottom];
                // 累加雨水量
                waterTrapped += width * depth;
            }
            
            stack.push_back(i); // 将当前索引入栈
        }
        
        return waterTrapped;
    }
};

/**
 * 打印数组辅助函数
 */
void printArray(const vector<int>& arr) {
    cout << "[";
    for (size_t i = 0; i < arr.size(); i++) {
        cout << arr[i];
        if (i < arr.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

/**
 * 测试用例和演示代码
 */
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> height1 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
    cout << "测试用例1:" << endl;
    cout << "height = ";
    printArray(height1);
    cout << "双指针法结果: " << solution.trapTwoPointers(height1) << endl; // 预期输出: 6
    cout << "动态规划结果: " << solution.trapDynamicProgramming(height1) << endl; // 预期输出: 6
    cout << "单调栈结果: " << solution.trapMonotonicStack(height1) << endl; // 预期输出: 6
    cout << endl;
    
    // 测试用例2
    vector<int> height2 = {4, 2, 0, 3, 2, 5};
    cout << "测试用例2:" << endl;
    cout << "height = ";
    printArray(height2);
    cout << "双指针法结果: " << solution.trapTwoPointers(height2) << endl; // 预期输出: 9
    cout << "动态规划结果: " << solution.trapDynamicProgramming(height2) << endl; // 预期输出: 9
    cout << "单调栈结果: " << solution.trapMonotonicStack(height2) << endl; // 预期输出: 9
    cout << endl;
    
    // 测试用例3 - 边界情况：只有两根柱子
    vector<int> height3 = {1, 2};
    cout << "测试用例3:" << endl;
    cout << "height = ";
    printArray(height3);
    cout << "双指针法结果: " << solution.trapTwoPointers(height3) << endl; // 预期输出: 0
    cout << "动态规划结果: " << solution.trapDynamicProgramming(height3) << endl; // 预期输出: 0
    cout << "单调栈结果: " << solution.trapMonotonicStack(height3) << endl; // 预期输出: 0
    cout << endl;
    
    // 测试用例4 - 边界情况：单调递增数组
    vector<int> height4 = {1, 2, 3, 4, 5};
    cout << "测试用例4:" << endl;
    cout << "height = ";
    printArray(height4);
    cout << "双指针法结果: " << solution.trapTwoPointers(height4) << endl; // 预期输出: 0
    cout << "动态规划结果: " << solution.trapDynamicProgramming(height4) << endl; // 预期输出: 0
    cout << "单调栈结果: " << solution.trapMonotonicStack(height4) << endl; // 预期输出: 0
    cout << endl;
    
    // 测试用例5 - 边界情况：单调递减数组
    vector<int> height5 = {5, 4, 3, 2, 1};
    cout << "测试用例5:" << endl;
    cout << "height = ";
    printArray(height5);
    cout << "双指针法结果: " << solution.trapTwoPointers(height5) << endl; // 预期输出: 0
    cout << "动态规划结果: " << solution.trapDynamicProgramming(height5) << endl; // 预期输出: 0
    cout << "单调栈结果: " << solution.trapMonotonicStack(height5) << endl; // 预期输出: 0
    cout << endl;
    
    // 性能测试
    cout << "性能测试:" << endl;
    int n = 20000;
    vector<int> height6(n);
    // 生成测试数据：波峰波谷交替
    for (int i = 0; i < n; i++) {
        height6[i] = min(i, n - i); // 形成一个山峰形状
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    int result1 = solution.trapTwoPointers(height6);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "双指针法结果: " << result1 << endl;
    cout << "双指针法耗时: " << duration1.count() << "微秒" << endl;
    
    startTime = chrono::high_resolution_clock::now();
    int result2 = solution.trapDynamicProgramming(height6);
    endTime = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "动态规划结果: " << result2 << endl;
    cout << "动态规划耗时: " << duration2.count() << "微秒" << endl;
    
    startTime = chrono::high_resolution_clock::now();
    int result3 = solution.trapMonotonicStack(height6);
    endTime = chrono::high_resolution_clock::now();
    auto duration3 = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "单调栈结果: " << result3 << endl;
    cout << "单调栈耗时: " << duration3.count() << "微秒" << endl;
    
    return 0;
}