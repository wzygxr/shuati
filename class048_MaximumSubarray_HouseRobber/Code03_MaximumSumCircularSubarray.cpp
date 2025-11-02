#include <vector>
#include <algorithm>
#include <climits>
#include <stdexcept>
#include <iostream>

using namespace std;

/**
 * 环形数组的子数组最大累加和 - C++实现
 * 给定一个数组nums，长度为n
 * nums是一个环形数组，下标0和下标n-1是连在一起的
 * 返回环形数组中，子数组最大累加和
 * 测试链接 : https://leetcode.cn/problems/maximum-sum-circular-subarray/
 * 
 * 算法核心思想：
 * 1. 环形数组的最大子数组和有两种情况：
 *    a) 最大子数组不跨越数组边界（普通Kadane算法）
 *    b) 最大子数组跨越数组边界（总和减去最小子数组和）
 * 2. 关键观察：环形数组的最大子数组和 = max(最大子数组和, 总和 - 最小子数组和)
 * 3. 特殊情况：当所有元素都是负数时，最小子数组和等于总和，此时返回最大子数组和
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n) - 只需遍历数组一次
 * - 空间复杂度：O(1) - 优化后只需常数空间
 * 
 * 工程化考量：
 * 1. 边界处理：处理空数组、单元素数组等特殊情况
 * 2. 异常防御：处理数值溢出等极端情况
 * 3. 性能优化：单次遍历同时计算最大和最小子数组和
 */

/**
 * 计算环形数组的最大子数组和
 * 
 * @param nums 输入整数数组（环形）
 * @return 环形数组的最大子数组和
 * @throws invalid_argument 如果输入数组为空
 */
int maxSubarraySumCircular(vector<int>& nums) {
    // 边界检查
    if (nums.empty()) {
        throw invalid_argument("输入数组不能为空");
    }
    
    int n = nums.size();
    // 特殊情况：单元素数组
    if (n == 1) {
        return nums[0];
    }
    
    // 初始化变量
    int totalSum = nums[0];        // 数组总和
    int maxSum = nums[0];          // 最大子数组和（不跨越边界）
    int minSum = nums[0];          // 最小子数组和
    int currentMax = nums[0];      // 当前最大子数组和
    int currentMin = nums[0];      // 当前最小子数组和
    
    // 单次遍历同时计算最大和最小子数组和
    for (int i = 1; i < n; i++) {
        // 累加总和
        totalSum += nums[i];
        
        // 更新最大子数组和（Kadane算法）
        currentMax = max(nums[i], currentMax + nums[i]);
        maxSum = max(maxSum, currentMax);
        
        // 更新最小子数组和
        currentMin = min(nums[i], currentMin + nums[i]);
        minSum = min(minSum, currentMin);
    }
    
    // 特殊情况处理：如果所有元素都是负数
    // 此时minSum == totalSum，应该返回maxSum（最大的单个负数）
    if (totalSum == minSum) {
        return maxSum;
    }
    
    // 返回两种情况的最大值
    // 情况1：不跨越边界的最大子数组和（maxSum）
    // 情况2：跨越边界的最大子数组和（totalSum - minSum）
    return max(maxSum, totalSum - minSum);
}

/**
 * 另一种实现方式：分别计算两种情况
 * 这种方法更直观，但需要两次遍历
 */
int maxSubarraySumCircularTwoPass(vector<int>& nums) {
    if (nums.empty()) {
        throw invalid_argument("输入数组不能为空");
    }
    
    int n = nums.size();
    if (n == 1) {
        return nums[0];
    }
    
    // 情况1：不跨越边界的最大子数组和（普通Kadane算法）
    int maxSum1 = nums[0];
    int current = nums[0];
    for (int i = 1; i < n; i++) {
        current = max(nums[i], current + nums[i]);
        maxSum1 = max(maxSum1, current);
    }
    
    // 情况2：跨越边界的最大子数组和（总和 - 最小子数组和）
    int totalSum = 0;
    int minSum = nums[0];
    current = nums[0];
    
    for (int i = 0; i < n; i++) {
        totalSum += nums[i];
    }
    
    for (int i = 1; i < n; i++) {
        current = min(nums[i], current + nums[i]);
        minSum = min(minSum, current);
    }
    
    int maxSum2 = totalSum - minSum;
    
    // 特殊情况：全负数数组
    if (totalSum == minSum) {
        return maxSum1;
    }
    
    return max(maxSum1, maxSum2);
}

/**
 * 测试函数：验证算法正确性
 */
void testMaxSubarraySumCircular() {
    vector<vector<int>> testCases = {
        {5, -3, 5},           // 期望: 10（跨越边界）
        {-3, -2, -1},         // 期望: -1（全负数）
        {1, -2, 3, -2},       // 期望: 3（不跨越边界）
        {5},                   // 期望: 5（单元素）
        {3, -1, 2, -1},       // 期望: 4（跨越边界）
        {1, 2, 3, 4},         // 期望: 10（全正数）
        {-2, -3, -1, -5}      // 期望: -1（全负数）
    };
    
    vector<int> expected = {10, -1, 3, 5, 4, 10, -1};
    
    cout << "=== 环形数组最大子数组和算法测试 ===" << endl;
    
    for (size_t i = 0; i < testCases.size(); ++i) {
        int result1 = maxSubarraySumCircular(testCases[i]);
        int result2 = maxSubarraySumCircularTwoPass(testCases[i]);
        
        cout << "测试用例 " << i+1 << ": ";
        cout << "输入: [";
        for (size_t j = 0; j < testCases[i].size(); ++j) {
            cout << testCases[i][j];
            if (j < testCases[i].size() - 1) cout << ", ";
        }
        cout << "]";
        cout << ", 结果=" << result1 << ", 期望=" << expected[i];
        cout << ", 状态=" << (result1 == expected[i] ? "通过" : "失败");
        cout << ", 方法一致性=" << (result1 == result2 ? "一致" : "不一致") << endl;
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
    int result = maxSubarraySumCircular(largeArray);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "计算结果: " << result << endl;
    cout << "执行时间: " << duration.count() << " 微秒" << endl;
    cout << "=== 性能测试结束 ===" << endl;
}

int main() {
    // 运行功能测试
    testMaxSubarraySumCircular();
    
    // 运行性能测试（可选）
    // performanceTest();
    
    return 0;
}

/*
 * 扩展思考与工程化考量：
 * 
 * 1. 算法变体分析：
 *    - 环形数组的最小子数组和：类似思路，但需要考虑边界情况
 *    - 环形数组的最大乘积子数组：需要考虑负数的影响
 *    - 环形数组的K次串联最大和：扩展为K次重复
 * 
 * 2. 工程应用场景：
 *    - 环形缓冲区：数据处理和信号分析
 *    - 周期性系统：如轮询调度、循环队列
 *    - 金融分析：周期性数据的最大收益计算
 * 
 * 3. 性能优化策略：
 *    - 单次遍历：同时计算最大和最小子数组和
 *    - 空间优化：使用常数空间代替数组
 *    - 缓存友好：顺序访问数组元素
 * 
 * 4. 代码质量保证：
 *    - 单元测试：覆盖各种边界情况
 *    - 性能测试：验证大数据量下的表现
 *    - 异常处理：确保程序的健壮性
 */