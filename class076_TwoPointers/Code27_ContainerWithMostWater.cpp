#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>

/**
 * LeetCode 11. 盛最多水的容器 (Container With Most Water)
 * 
 * 题目描述:
 * 给定一个长度为 n 的整数数组 height。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i])。
 * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 * 返回容器可以储存的最大水量。
 * 说明：你不能倾斜容器。
 * 
 * 示例1:
 * 输入: [1,8,6,2,5,4,8,3,7]
 * 输出: 49
 * 解释: 图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。
 * 
 * 示例2:
 * 输入: height = [1,1]
 * 输出: 1
 * 
 * 提示:
 * n == height.length
 * 2 <= n <= 10^5
 * 0 <= height[i] <= 10^4
 * 
 * 题目链接: https://leetcode.com/problems/container-with-most-water/
 * 
 * 解题思路:
 * 这道题可以使用双指针的方法来解决：
 * 
 * 方法一（暴力解法）：
 * 遍历所有可能的两条线的组合，计算每个组合能容纳的水量，找出最大值。
 * 时间复杂度：O(n^2)，空间复杂度：O(1)
 * 
 * 方法二（双指针）：
 * 1. 初始化两个指针 left 和 right 分别指向数组的开头和结尾
 * 2. 计算当前指针所指两条线能容纳的水量：min(height[left], height[right]) * (right - left)
 * 3. 更新最大水量
 * 4. 移动较短的那条线对应的指针（因为如果移动较长的线，容纳的水量只会更小）
 * 5. 重复步骤2-4，直到两个指针相遇
 * 时间复杂度：O(n)，空间复杂度：O(1)
 * 
 * 最优解是方法二，时间复杂度 O(n)，空间复杂度 O(1)。
 */

/**
 * 解法一: 暴力解法（不推荐，可能会超时）
 * 
 * @param height 输入数组
 * @return 最大盛水量
 * @throws std::invalid_argument 如果输入数组长度小于2
 */
int maxAreaBruteForce(const std::vector<int>& height) {
    // 参数校验
    if (height.size() < 2) {
        throw std::invalid_argument("输入数组长度必须至少为2");
    }
    
    int maxArea = 0;
    // 遍历所有可能的两条线的组合
    for (size_t i = 0; i < height.size(); i++) {
        for (size_t j = i + 1; j < height.size(); j++) {
            // 计算当前组合的盛水量
            int currentArea = std::min(height[i], height[j]) * static_cast<int>(j - i);
            // 更新最大盛水量
            maxArea = std::max(maxArea, currentArea);
        }
    }
    return maxArea;
}

/**
 * 解法二: 双指针（最优解）
 * 
 * @param height 输入数组
 * @return 最大盛水量
 */
int maxArea(const std::vector<int>& height) {
    // 参数校验
    if (height.size() < 2) {
        throw std::invalid_argument("输入数组长度必须至少为2");
    }
    
    int maxArea = 0;
    int left = 0;  // 左指针，初始指向数组开头
    int right = static_cast<int>(height.size() - 1);  // 右指针，初始指向数组结尾
    
    while (left < right) {
        // 计算当前盛水量
        int currentHeight = std::min(height[left], height[right]);
        int currentWidth = right - left;
        int currentArea = currentHeight * currentWidth;
        
        // 更新最大盛水量
        maxArea = std::max(maxArea, currentArea);
        
        // 移动较短的那条线对应的指针
        if (height[left] < height[right]) {
            left++;
        } else {
            right--;
        }
    }
    
    return maxArea;
}

/**
 * 解法三: 双指针优化版
 * 跳过相同高度的柱子，减少不必要的计算
 * 
 * @param height 输入数组
 * @return 最大盛水量
 */
int maxAreaOptimized(const std::vector<int>& height) {
    // 参数校验
    if (height.size() < 2) {
        throw std::invalid_argument("输入数组长度必须至少为2");
    }
    
    int maxArea = 0;
    int left = 0;
    int right = static_cast<int>(height.size() - 1);
    
    while (left < right) {
        // 计算当前盛水量
        int currentHeight = std::min(height[left], height[right]);
        int currentWidth = right - left;
        int currentArea = currentHeight * currentWidth;
        
        // 更新最大盛水量
        maxArea = std::max(maxArea, currentArea);
        
        // 移动较短的那条线对应的指针
        // 跳过相同高度的柱子
        if (height[left] < height[right]) {
            int currentLeftHeight = height[left];
            while (left < right && height[left] <= currentLeftHeight) {
                left++;
            }
        } else {
            int currentRightHeight = height[right];
            while (left < right && height[right] <= currentRightHeight) {
                right--;
            }
        }
    }
    
    return maxArea;
}

/**
 * 打印向量内容
 */
void printVector(const std::vector<int>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); i++) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

/**
 * 测试函数
 */
void test() {
    // 测试用例1
    std::vector<int> height1 = {1, 8, 6, 2, 5, 4, 8, 3, 7};
    int expected1 = 49;
    std::cout << "测试用例1:\n";
    std::cout << "输入数组: ";
    printVector(height1);
    std::cout << std::endl;
    int result1 = maxArea(height1);
    std::cout << "最大盛水量: " << result1 << std::endl;
    std::cout << "验证结果: " << (result1 == expected1 ? "true" : "false") << std::endl;
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> height2 = {1, 1};
    int expected2 = 1;
    std::cout << "测试用例2:\n";
    std::cout << "输入数组: ";
    printVector(height2);
    std::cout << std::endl;
    int result2 = maxArea(height2);
    std::cout << "最大盛水量: " << result2 << std::endl;
    std::cout << "验证结果: " << (result2 == expected2 ? "true" : "false") << std::endl;
    std::cout << std::endl;
    
    // 测试用例3 - 边界情况：所有元素递增
    std::vector<int> height3 = {1, 2, 3, 4, 5};
    int expected3 = 6; // 由索引0和4的元素组成的容器
    std::cout << "测试用例3（递增数组）:\n";
    std::cout << "输入数组: ";
    printVector(height3);
    std::cout << std::endl;
    int result3 = maxArea(height3);
    std::cout << "最大盛水量: " << result3 << std::endl;
    std::cout << "验证结果: " << (result3 == expected3 ? "true" : "false") << std::endl;
    std::cout << std::endl;
    
    // 测试用例4 - 边界情况：所有元素递减
    std::vector<int> height4 = {5, 4, 3, 2, 1};
    int expected4 = 6; // 由索引0和4的元素组成的容器
    std::cout << "测试用例4（递减数组）:\n";
    std::cout << "输入数组: ";
    printVector(height4);
    std::cout << std::endl;
    int result4 = maxArea(height4);
    std::cout << "最大盛水量: " << result4 << std::endl;
    std::cout << "验证结果: " << (result4 == expected4 ? "true" : "false") << std::endl;
    std::cout << std::endl;
    
    // 测试用例5 - 边界情况：只有两个元素，高度不同
    std::vector<int> height5 = {3, 5};
    int expected5 = 3; // 由索引0和1的元素组成的容器
    std::cout << "测试用例5（两个元素）:\n";
    std::cout << "输入数组: ";
    printVector(height5);
    std::cout << std::endl;
    int result5 = maxArea(height5);
    std::cout << "最大盛水量: " << result5 << std::endl;
    std::cout << "验证结果: " << (result5 == expected5 ? "true" : "false") << std::endl;
    std::cout << std::endl;
    
    // 测试用例6 - 边界情况：包含0
    std::vector<int> height6 = {0, 0, 0, 0, 0};
    int expected6 = 0; // 所有元素都是0，盛水量为0
    std::cout << "测试用例6（全零数组）:\n";
    std::cout << "输入数组: ";
    printVector(height6);
    std::cout << std::endl;
    int result6 = maxArea(height6);
    std::cout << "最大盛水量: " << result6 << std::endl;
    std::cout << "验证结果: " << (result6 == expected6 ? "true" : "false") << std::endl;
    std::cout << std::endl;
}

/**
 * 性能测试
 */
void performanceTest() {
    // 创建一个大数组进行性能测试
    const size_t size = 100000;
    std::vector<int> largeArray(size);
    
    // 生成测试数据：交替增加和减少
    for (size_t i = 0; i < size; i++) {
        largeArray[i] = i % 100;  // 0-99循环
    }
    
    // 测试解法二的性能
    auto start = std::chrono::high_resolution_clock::now();
    int result2 = maxArea(largeArray);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "解法二（双指针）耗时: " << duration << "ms, 最大盛水量: " << result2 << std::endl;
    
    // 测试解法三的性能
    start = std::chrono::high_resolution_clock::now();
    int result3 = maxAreaOptimized(largeArray);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "解法三（优化双指针）耗时: " << duration << "ms, 最大盛水量: " << result3 << std::endl;
    
    // 验证两种解法结果一致
    bool resultsConsistent = (result2 == result3);
    std::cout << "所有解法结果一致: " << (resultsConsistent ? "true" : "false") << std::endl;
}

/**
 * 边界条件测试
 */
void boundaryTest() {
    try {
        // 测试长度为1的输入
        maxArea(std::vector<int>{5});
        std::cout << "边界测试失败：长度为1的输入没有抛出异常" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "边界测试通过：长度为1的输入正确抛出异常: " << e.what() << std::endl;
    }
    
    try {
        // 测试空输入
        maxArea(std::vector<int>{});
        std::cout << "边界测试失败：空输入没有抛出异常" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "边界测试通过：空输入正确抛出异常: " << e.what() << std::endl;
    }
}

int main() {
    std::cout << "=== 测试用例 ===" << std::endl;
    test();
    
    std::cout << "=== 性能测试 ===" << std::endl;
    performanceTest();
    
    std::cout << "=== 边界条件测试 ===" << std::endl;
    boundaryTest();
    
    return 0;
}