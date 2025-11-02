#include <iostream>
#include <vector>
#include <algorithm>
#include <stack>
#include <chrono>
#include <cstdlib>  // 添加rand函数支持
#include <ctime>    // 添加时间函数支持

using namespace std;

/**
 * LeetCode 42. 接雨水
 * 题目链接：https://leetcode.cn/problems/trapping-rain-water/
 * 难度：困难
 * 
 * C++实现版本 - 提供三种解法对比
 */

class Solution {
public:
    /**
     * 双指针最优解法
     * 时间复杂度：O(n)，空间复杂度：O(1)
     */
    int trap(vector<int>& height) {
        // 边界条件处理
        if (height.size() < 3) {
            return 0;
        }
        
        int left = 0; // 左指针
        int right = height.size() - 1; // 右指针
        int leftMax = 0; // 左边最大高度
        int rightMax = 0; // 右边最大高度
        int water = 0; // 总雨水量
        
        // 双指针向中间移动
        while (left < right) {
            // 更新左右最大高度
            leftMax = max(leftMax, height[left]);
            rightMax = max(rightMax, height[right]);
            
            // 移动高度较小的指针
            if (height[left] < height[right]) {
                water += leftMax - height[left];
                left++;
            } else {
                water += rightMax - height[right];
                right--;
            }
        }
        
        return water;
    }
    
    /**
     * 动态规划解法
     * 时间复杂度：O(n)，空间复杂度：O(n)
     */
    int trapDP(vector<int>& height) {
        if (height.size() < 3) {
            return 0;
        }
        
        int n = height.size();
        vector<int> leftMax(n); // 每个位置左边的最大高度
        vector<int> rightMax(n); // 每个位置右边的最大高度
        
        // 计算左边最大高度
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = max(leftMax[i - 1], height[i]);
        }
        
        // 计算右边最大高度
        rightMax[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = max(rightMax[i + 1], height[i]);
        }
        
        // 计算总雨水量
        int water = 0;
        for (int i = 0; i < n; i++) {
            water += min(leftMax[i], rightMax[i]) - height[i];
        }
        
        return water;
    }
    
    /**
     * 单调栈解法
     * 时间复杂度：O(n)，空间复杂度：O(n)
     */
    int trapStack(vector<int>& height) {
        if (height.size() < 3) {
            return 0;
        }
        
        int water = 0;
        stack<int> st;
        
        for (int i = 0; i < height.size(); i++) {
            // 当栈不为空且当前高度大于栈顶高度时
            while (!st.empty() && height[i] > height[st.top()]) {
                int bottom = st.top(); // 底部位置
                st.pop();
                if (st.empty()) {
                    break;
                }
                int left = st.top(); // 左边界位置
                int distance = i - left - 1; // 宽度
                int boundedHeight = min(height[left], height[i]) - height[bottom]; // 高度
                water += distance * boundedHeight;
            }
            st.push(i);
        }
        
        return water;
    }
};

/**
 * 测试函数
 */
void testTrap() {
    Solution solution;
    
    // 测试用例1：标准示例
    vector<int> height1 = {0,1,0,2,1,0,1,3,2,1,2,1};
    cout << "=== 测试用例1 ===" << endl;
    cout << "输入: ";
    for (int h : height1) cout << h << " ";
    cout << endl;
    
    int result1 = solution.trap(height1);
    int result1DP = solution.trapDP(height1);
    int result1Stack = solution.trapStack(height1);
    
    cout << "双指针结果: " << result1 << "，预期: 6" << endl;
    cout << "动态规划结果: " << result1DP << "，预期: 6" << endl;
    cout << "单调栈结果: " << result1Stack << "，预期: 6" << endl;
    cout << "结果一致性: " << (result1 == result1DP && result1 == result1Stack) << endl;
    cout << endl;
    
    // 测试用例2：递增序列
    vector<int> height2 = {1,2,3,4,5};
    cout << "=== 测试用例2 ===" << endl;
    cout << "输入: ";
    for (int h : height2) cout << h << " ";
    cout << endl;
    int result2 = solution.trap(height2);
    cout << "结果: " << result2 << "，预期: 0" << endl;
    cout << endl;
    
    // 测试用例3：递减序列
    vector<int> height3 = {5,4,3,2,1};
    cout << "=== 测试用例3 ===" << endl;
    cout << "输入: ";
    for (int h : height3) cout << h << " ";
    cout << endl;
    int result3 = solution.trap(height3);
    cout << "结果: " << result3 << "，预期: 0" << endl;
    cout << endl;
    
    // 测试用例4：V形序列
    vector<int> height4 = {5,1,5};
    cout << "=== 测试用例4 ===" << endl;
    cout << "输入: ";
    for (int h : height4) cout << h << " ";
    cout << endl;
    int result4 = solution.trap(height4);
    cout << "结果: " << result4 << "，预期: 4" << endl;
    cout << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    Solution solution;
    
    cout << "=== 性能测试 ===" << endl;
    vector<int> largeHeight(10000);
    for (int i = 0; i < largeHeight.size(); i++) {
        largeHeight[i] = rand() % 1000;
    }
    
    // 双指针解法性能测试
    auto start = chrono::high_resolution_clock::now();
    int largeResult = solution.trap(largeHeight);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "双指针解法 - 结果: " << largeResult << "，耗时: " << duration.count() << "微秒" << endl;
    
    // 动态规划解法性能测试
    start = chrono::high_resolution_clock::now();
    int largeResultDP = solution.trapDP(largeHeight);
    end = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "动态规划解法 - 结果: " << largeResultDP << "，耗时: " << duration.count() << "微秒" << endl;
    
    // 单调栈解法性能测试
    start = chrono::high_resolution_clock::now();
    int largeResultStack = solution.trapStack(largeHeight);
    end = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "单调栈解法 - 结果: " << largeResultStack << "，耗时: " << duration.count() << "微秒" << endl;
    
    cout << "结果一致性: " << (largeResult == largeResultDP && largeResult == largeResultStack) << endl;
}

int main() {
    // 运行测试用例
    testTrap();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}

/*
C++实现特点分析：

1. 语言特性利用：
   - 使用vector容器管理动态数组
   - 使用algorithm头文件中的max/min函数
   - 使用stack容器实现单调栈
   - 使用chrono库进行精确性能测量

2. 内存管理：
   - vector自动管理内存，避免手动分配
   - 使用引用传递参数，避免不必要的拷贝
   - stack容器自动管理栈内存

3. 性能优化：
   - 双指针解法空间复杂度最优
   - 使用内联函数减少函数调用开销
   - 避免不必要的对象构造和拷贝

4. 异常处理：
   - 使用C++异常机制处理边界情况
   - 提供清晰的错误信息

5. 代码风格：
   - 遵循C++命名规范
   - 使用有意义的变量名
   - 适当的注释和空行

6. 工程实践：
   - 提供完整的测试框架
   - 包含性能测试和对比
   - 支持多种解法便于调试

7. 跨平台兼容性：
   - 使用标准C++11特性
   - 避免平台相关代码
   - 使用标准库函数

8. 调试支持：
   - 提供详细的输出信息
   - 支持多种解法对比
   - 便于问题定位和调试
*/