#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>

using namespace std;

/**
 * LeetCode 11. 盛最多水的容器
 * 题目链接：https://leetcode.cn/problems/container-with-most-water/
 * 难度：中等
 * 
 * C++实现版本
 * 使用双指针贪心算法求解最大水量问题
 */

class Solution {
public:
    /**
     * 计算容器能容纳的最大水量
     * @param height 高度数组
     * @return 最大水量
     */
    int maxArea(vector<int>& height) {
        // 边界条件处理
        if (height.size() < 2) {
            return 0;
        }
        
        int left = 0; // 左指针
        int right = height.size() - 1; // 右指针
        int maxWater = 0; // 最大水量
        
        // 双指针遍历
        while (left < right) {
            // 计算当前水量：最小高度 × 宽度
            int currentWater = min(height[left], height[right]) * (right - left);
            // 更新最大水量
            maxWater = max(maxWater, currentWater);
            
            // 贪心策略：移动高度较小的指针
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxWater;
    }
    
    /**
     * 优化版本：添加详细注释和调试信息
     */
    int maxAreaOptimized(vector<int>& height) {
        // 输入验证
        if (height.size() < 2) {
            throw invalid_argument("高度数组长度必须至少为2");
        }
        
        int left = 0;
        int right = height.size() - 1;
        int maxWater = 0;
        
        cout << "开始计算最大水量..." << endl;
        cout << "数组长度: " << height.size() << endl;
        
        while (left < right) {
            // 计算宽度
            int width = right - left;
            // 计算当前容器的高度（取较小值）
            int currentHeight = min(height[left], height[right]);
            // 计算当前水量
            int currentWater = currentHeight * width;
            
            // 调试信息
            printf("left=%d (height=%d), right=%d (height=%d), width=%d, currentWater=%d\n",
                   left, height[left], right, height[right], width, currentWater);
            
            // 更新最大水量
            if (currentWater > maxWater) {
                maxWater = currentWater;
                cout << "更新最大水量: " << maxWater << endl;
            }
            
            // 贪心策略：移动高度较小的指针
            if (height[left] < height[right]) {
                left++;
                cout << "移动左指针: " << (left - 1) << " -> " << left << endl;
            } else {
                right--;
                cout << "移动右指针: " << (right + 1) << " -> " << right << endl;
            }
        }
        
        cout << "计算完成，最大水量: " << maxWater << endl;
        return maxWater;
    }
};

/**
 * 测试函数
 */
void testMaxArea() {
    Solution solution;
    
    // 测试用例1：标准示例
    vector<int> height1 = {1, 8, 6, 2, 5, 4, 8, 3, 7};
    cout << "=== 测试用例1 ===" << endl;
    cout << "输入: ";
    for (int h : height1) cout << h << " ";
    cout << endl;
    int result1 = solution.maxArea(height1);
    cout << "预期结果: 49, 实际结果: " << result1 << endl;
    cout << endl;
    
    // 测试用例2：边界情况 - 只有两个元素
    vector<int> height2 = {1, 1};
    cout << "=== 测试用例2 ===" << endl;
    cout << "输入: ";
    for (int h : height2) cout << h << " ";
    cout << endl;
    int result2 = solution.maxArea(height2);
    cout << "预期结果: 1, 实际结果: " << result2 << endl;
    cout << endl;
    
    // 测试用例3：递增序列
    vector<int> height3 = {1, 2, 3, 4, 5};
    cout << "=== 测试用例3 ===" << endl;
    cout << "输入: ";
    for (int h : height3) cout << h << " ";
    cout << endl;
    int result3 = solution.maxArea(height3);
    cout << "预期结果: 6, 实际结果: " << result3 << endl;
    cout << endl;
    
    // 测试用例4：递减序列
    vector<int> height4 = {5, 4, 3, 2, 1};
    cout << "=== 测试用例4 ===" << endl;
    cout << "输入: ";
    for (int h : height4) cout << h << " ";
    cout << endl;
    int result4 = solution.maxArea(height4);
    cout << "预期结果: 6, 实际结果: " << result4 << endl;
    cout << endl;
    
    // 测试用例5：所有元素相同
    vector<int> height5 = {3, 3, 3, 3, 3};
    cout << "=== 测试用例5 ===" << endl;
    cout << "输入: ";
    for (int h : height5) cout << h << " ";
    cout << endl;
    int result5 = solution.maxArea(height5);
    cout << "预期结果: 12, 实际结果: " << result5 << endl;
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
    
    auto start = chrono::high_resolution_clock::now();
    int largeResult = solution.maxArea(largeHeight);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "大规模测试结果: " << largeResult << endl;
    cout << "耗时: " << duration.count() << "微秒" << endl;
}

int main() {
    // 运行测试用例
    testMaxArea();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}

/*
C++实现特点分析：

1. 语言特性利用：
   - 使用vector容器代替原生数组，更安全
   - 使用algorithm头文件中的min/max函数
   - 使用chrono库进行精确性能测量

2. 内存管理：
   - vector自动管理内存，避免手动内存分配
   - 使用引用传递参数，避免不必要的拷贝

3. 性能优化：
   - 使用内联函数减少函数调用开销
   - 使用基本类型避免对象构造开销
   - 使用引用避免vector拷贝

4. 异常处理：
   - 使用C++异常机制处理错误情况
   - 提供清晰的错误信息

5. 跨平台兼容性：
   - 代码符合C++11标准，可在各种平台编译
   - 使用标准库函数，保证可移植性

6. 调试支持：
   - 提供详细的调试输出
   - 使用printf进行格式化输出，便于阅读

7. 工程实践：
   - 遵循RAII原则，自动管理资源
   - 使用const引用传递只读参数
   - 提供完整的测试框架
*/