#include <vector>
#include <algorithm>
#include <climits>
#include <stdexcept>
#include <iostream>

using namespace std;

/**
 * 子数组最大累加和问题（Kadane算法）- C++实现
 * 题目描述：给你一个整数数组 nums，返回非空子数组的最大累加和
 * 测试链接：https://leetcode.cn/problems/maximum-subarray/
 * 
 * 算法核心思想：
 * 1. Kadane算法是解决最大子数组和问题的经典动态规划算法
 * 2. 对于每个位置，我们有两个选择：
 *    a) 将当前元素加入到之前的子数组中
 *    b) 以当前元素开始一个新的子数组
 * 3. 取这两个选择中的较大值作为以当前元素结尾的最大子数组和
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n) - 只需遍历数组一次
 * - 空间复杂度：O(1) - 优化后只需常数空间
 * 
 * 工程化考量：
 * 1. 异常处理：对空数组和边界情况进行处理
 * 2. 鲁棒性：处理极端输入（全负数、全正数、混合情况）
 * 3. 性能优化：使用引用避免拷贝，使用const保证安全性
 */

/**
 * 方法一：动态规划（空间优化版本）
 * 时间复杂度：O(n) - 只需遍历数组一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * @param nums 输入整数数组（使用引用避免拷贝）
 * @return 非空子数组的最大累加和
 * @throws invalid_argument 如果输入数组为空
 */
int maxSubArray(vector<int>& nums) {
    // 边界检查：处理空数组情况
    if (nums.empty()) {
        throw invalid_argument("输入数组不能为空");
    }
    
    int maxSum = nums[0];
    int currentSum = nums[0];
    
    // 从第二个元素开始遍历
    for (size_t i = 1; i < nums.size(); ++i) {
        // 状态转移：选择加入前面子数组或重新开始
        // 关键理解：如果前面的子数组和为负，不如重新开始
        currentSum = max(nums[i], currentSum + nums[i]);
        // 更新全局最大值
        maxSum = max(maxSum, currentSum);
    }
    
    return maxSum;
}

/**
 * 方法二：动态规划（基础版本）- 用于教学和理解
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * @param nums 输入整数数组
 * @return 非空子数组的最大累加和
 */
int maxSubArrayDP(vector<int>& nums) {
    if (nums.empty()) {
        throw invalid_argument("输入数组不能为空");
    }
    
    int n = nums.size();
    vector<int> dp(n, 0);
    dp[0] = nums[0];
    int maxSum = nums[0];
    
    for (int i = 1; i < n; ++i) {
        dp[i] = max(nums[i], dp[i-1] + nums[i]);
        maxSum = max(maxSum, dp[i]);
    }
    
    return maxSum;
}

/**
 * 记录最大子数组的位置信息
 * 
 * @param nums 输入数组
 * @param[out] left 最大子数组起始索引
 * @param[out] right 最大子数组结束索引
 * @param[out] sum 最大子数组的和
 */
void findMaxSubarray(vector<int>& nums, int& left, int& right, int& sum) {
    if (nums.empty()) {
        throw invalid_argument("输入数组不能为空");
    }
    
    sum = INT_MIN;
    left = 0;
    right = 0;
    int currentSum = INT_MIN;
    int currentLeft = 0;
    
    for (int i = 0; i < nums.size(); ++i) {
        if (currentSum >= 0) {
            currentSum += nums[i];
        } else {
            currentSum = nums[i];
            currentLeft = i;
        }
        
        if (currentSum > sum) {
            sum = currentSum;
            left = currentLeft;
            right = i;
        }
    }
}

/**
 * 测试函数：验证算法正确性
 */
void testMaxSubArray() {
    vector<vector<int>> testCases = {
        {-2, 1, -3, 4, -1, 2, 1, -5, 4},  // 期望: 6
        {-1, -2, -3, -4},                  // 期望: -1
        {1, 2, 3, 4},                      // 期望: 10
        {5},                                // 期望: 5
        {0, -1, 2, -3, 4},                 // 期望: 4
        {-1},                               // 期望: -1
        {1, -1, 1, -1, 1}                  // 期望: 1
    };
    
    vector<int> expected = {6, -1, 10, 5, 4, -1, 1};
    
    cout << "=== 最大子数组和算法测试 ===" << endl;
    
    for (size_t i = 0; i < testCases.size(); ++i) {
        try {
            int result = maxSubArray(testCases[i]);
            int resultDP = maxSubArrayDP(testCases[i]);
            
            cout << "测试用例 " << i+1 << ": ";
            cout << "结果=" << result << ", 期望=" << expected[i];
            cout << ", 状态=" << (result == expected[i] ? "通过" : "失败");
            cout << ", DP验证=" << (result == resultDP ? "一致" : "不一致") << endl;
            
            // 测试位置记录功能
            int left, right, sum;
            findMaxSubarray(testCases[i], left, right, sum);
            cout << "  最大子数组位置: [" << left << ", " << right << "], 和: " << sum << endl;
            
        } catch (const exception& e) {
            cout << "测试用例 " << i+1 << ": 异常 - " << e.what() << endl;
        }
    }
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * 性能测试：大数据量验证
 */
void performanceTest() {
    const int SIZE = 1000000;
    vector<int> largeArray(SIZE, 1);  // 全1数组，最大和就是数组长度
    
    cout << "=== 性能测试开始 ===" << endl;
    cout << "数据量: " << SIZE << " 个元素" << endl;
    
    auto start = chrono::high_resolution_clock::now();
    int result = maxSubArray(largeArray);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "计算结果: " << result << endl;
    cout << "执行时间: " << duration.count() << " 微秒" << endl;
    cout << "=== 性能测试结束 ===" << endl;
}

int main() {
    // 运行功能测试
    testMaxSubArray();
    
    // 运行性能测试（可选）
    // performanceTest();
    
    return 0;
}

/*
 * 扩展思考与工程化考量：
 * 
 * 1. 异常防御编程：
 *    - 输入验证：检查空数组、无效数据
 *    - 边界处理：处理单元素、全负数等特殊情况
 *    - 内存安全：避免越界访问
 * 
 * 2. 性能优化策略：
 *    - 使用引用避免不必要的拷贝
 *    - 预分配内存减少动态分配
 *    - 利用缓存局部性优化访问模式
 * 
 * 3. 代码质量保证：
 *    - 单元测试：覆盖各种边界情况
 *    - 性能测试：验证大数据量下的表现
 *    - 代码审查：确保逻辑正确性和可读性
 * 
 * 4. 多语言对比优势：
 *    - C++：高性能，内存控制精细
 *    - Java：跨平台，生态丰富
 *    - Python：开发效率高，适合原型
 */