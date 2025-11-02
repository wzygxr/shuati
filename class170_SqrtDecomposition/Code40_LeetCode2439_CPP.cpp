#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <ctime>
#include <cstdlib>
using namespace std;

/**
 * LeetCode 2439. 最小化数组中的最大值
 * 题目要求：将数组分成k个子数组，最小化子数组最大值
 * 核心技巧：分块 + 贪心
 * 时间复杂度：O(n log n) / 操作
 * 测试链接：https://leetcode.cn/problems/minimize-maximum-of-array/
 *
 * 该问题的最优解法是前缀和贪心，而不是分块。前缀和贪心能达到O(n)的时间复杂度，是最优解。
 * 贪心的思路是：对于每个位置，计算前缀和的平均值（向上取整），这是当前前缀能达到的最小可能最大值。
 */

/**
 * 前缀和贪心算法
 * @param nums 输入数组
 * @return 最小的可能的子数组最大值
 */
int minimizeArrayValue(const vector<int>& nums) {
    long long prefixSum = 0;
    int result = 0;
    
    for (int i = 0; i < nums.size(); ++i) {
        prefixSum += nums[i];
        // 计算前缀和的平均值，向上取整
        long long currentMax = (prefixSum + i) / (i + 1);
        result = max(result, static_cast<int>(currentMax));
    }
    
    return result;
}

/**
 * 二分查找解法（次优解）
 * @param nums 输入数组
 * @return 最小的可能的子数组最大值
 */
int minimizeArrayValueBinarySearch(const vector<int>& nums) {
    int left = 0;
    int right = *max_element(nums.begin(), nums.end());
    
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (canMinimize(nums, mid)) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    
    return left;
}

/**
 * 检查是否可以通过调整使得所有元素都不超过maxValue
 * @param nums 输入数组
 * @param maxValue 最大允许值
 * @return 是否可以调整
 */
bool canMinimize(const vector<int>& nums, int maxValue) {
    long long extra = 0;
    for (int i = nums.size() - 1; i >= 0; --i) {
        long long current = nums[i] + extra;
        if (current > maxValue) {
            extra = current - maxValue;
        } else {
            extra = 0;
        }
    }
    return extra == 0;
}

/**
 * 正确性测试函数
 */
void correctnessTest() {
    cout << "=== 正确性测试 ===\n";
    
    // 测试用例1
    vector<int> nums1 = {3, 7, 1, 6};
    cout << "测试用例1: [3, 7, 1, 6]\n";
    cout << "前缀和贪心法结果: " << minimizeArrayValue(nums1) << '\n';  // 应为5
    cout << "二分查找法结果: " << minimizeArrayValueBinarySearch(nums1) << '\n';  // 应为5
    
    // 测试用例2
    vector<int> nums2 = {10, 1};
    cout << "\n测试用例2: [10, 1]\n";
    cout << "前缀和贪心法结果: " << minimizeArrayValue(nums2) << '\n';  // 应为10
    cout << "二分查找法结果: " << minimizeArrayValueBinarySearch(nums2) << '\n';  // 应为10
    
    // 测试用例3
    vector<int> nums3 = {1, 2, 3, 4, 5};
    cout << "\n测试用例3: [1, 2, 3, 4, 5]\n";
    cout << "前缀和贪心法结果: " << minimizeArrayValue(nums3) << '\n';  // 应为3
    cout << "二分查找法结果: " << minimizeArrayValueBinarySearch(nums3) << '\n';  // 应为3
    
    // 测试用例4：全部相同
    vector<int> nums4 = {5, 5, 5, 5};
    cout << "\n测试用例4: [5, 5, 5, 5]\n";
    cout << "前缀和贪心法结果: " << minimizeArrayValue(nums4) << '\n';  // 应为5
    cout << "二分查找法结果: " << minimizeArrayValueBinarySearch(nums4) << '\n';  // 应为5
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "\n=== 性能测试 ===\n";
    
    // 生成大规模测试数据
    int n = 100000;
    vector<int> nums(n);
    srand(42);
    for (int i = 0; i < n; ++i) {
        nums[i] = rand() % 1000000 + 1;
    }
    
    // 测试前缀和贪心法
    clock_t startTime = clock();
    int result1 = minimizeArrayValue(nums);
    clock_t endTime = clock();
    double greedyTime = (double)(endTime - startTime) * 1000.0 / CLOCKS_PER_SEC;
    cout << "前缀和贪心法处理1e5数据耗时: " << greedyTime << "ms\n";
    
    // 测试二分查找法
    startTime = clock();
    int result2 = minimizeArrayValueBinarySearch(nums);
    endTime = clock();
    double binaryTime = (double)(endTime - startTime) * 1000.0 / CLOCKS_PER_SEC;
    cout << "二分查找法处理1e5数据耗时: " << binaryTime << "ms\n";
    
    // 验证结果一致性
    cout << "结果一致性验证: " << (result1 == result2 ? "一致" : "不一致") << '\n';
    
    // 计算性能比率
    cout << "性能比率 (二分/贪心): " << binaryTime / greedyTime << "x\n";
}

/**
 * 边界情况测试
 */
void boundaryTest() {
    cout << "\n=== 边界情况测试 ===\n";
    
    // 测试n=1的情况
    vector<int> nums1 = {5};
    cout << "n=1, nums=[5]\n";
    cout << "前缀和贪心法结果: " << minimizeArrayValue(nums1) << '\n';  // 应为5
    
    // 测试全为0的情况
    vector<int> nums2 = {0, 0, 0, 0};
    cout << "\n全为0: [0, 0, 0, 0]\n";
    cout << "前缀和贪心法结果: " << minimizeArrayValue(nums2) << '\n';  // 应为0
    
    // 测试递增序列
    vector<int> nums3 = {1, 100, 1000, 10000};
    cout << "\n递增序列: [1, 100, 1000, 10000]\n";
    cout << "前缀和贪心法结果: " << minimizeArrayValue(nums3) << '\n';  // 应为3367
    
    // 测试递减序列
    vector<int> nums4 = {10000, 1000, 100, 1};
    cout << "\n递减序列: [10000, 1000, 100, 1]\n";
    cout << "前缀和贪心法结果: " << minimizeArrayValue(nums4) << '\n';  // 应为10000
}

/**
 * 算法效率对比函数
 */
void algorithmComparison() {
    cout << "\n=== 算法效率对比 ===\n";
    
    // 测试不同大小的数组
    vector<int> sizes = {100, 1000, 10000, 100000, 1000000};
    
    for (int size : sizes) {
        vector<int> nums(size);
        srand(42);
        for (int i = 0; i < size; ++i) {
            nums[i] = rand() % 1000000 + 1;
        }
        
        cout << "\n数组大小: " << size << '\n';
        
        // 前缀和贪心法
        clock_t startTime = clock();
        int result1 = minimizeArrayValue(nums);
        clock_t endTime = clock();
        double greedyTime = (double)(endTime - startTime) * 1000.0 / CLOCKS_PER_SEC;
        cout << "前缀和贪心法耗时: " << greedyTime << "ms\n";
        
        // 二分查找法
        startTime = clock();
        int result2 = minimizeArrayValueBinarySearch(nums);
        endTime = clock();
        double binaryTime = (double)(endTime - startTime) * 1000.0 / CLOCKS_PER_SEC;
        cout << "二分查找法耗时: " << binaryTime << "ms\n";
        
        // 验证结果一致性
        cout << "结果一致: " << (result1 == result2 ? "是" : "否") << '\n';
        
        // 计算加速比
        cout << "加速比: " << binaryTime / greedyTime << "x\n";
    }
}

/**
 * 内存使用分析
 */
void memoryAnalysis() {
    cout << "\n=== 内存使用分析 ===\n";
    
    // 分析不同算法的内存占用
    cout << "前缀和贪心法内存复杂度: O(1) 常量额外空间\n";
    cout << "二分查找法内存复杂度: O(1) 常量额外空间\n";
    cout << "注意：两种算法都不需要额外的数据结构存储中间结果\n";
    cout << "C++中vector<int>的内存占用: 每个int占4字节，不包括容器本身的开销\n";
}

/**
 * C++优化版本：使用内联和const引用
 */
inline int minimizeArrayValueOptimized(const vector<int>& nums) {
    long long prefixSum = 0;
    int result = 0;
    
    for (size_t i = 0; i < nums.size(); ++i) {
        prefixSum += nums[i];
        // 使用更高效的除法操作
        long long currentMax = (prefixSum + static_cast<long long>(i)) / (i + 1LL);
        if (static_cast<int>(currentMax) > result) {
            result = static_cast<int>(currentMax);
        }
    }
    
    return result;
}

/**
 * 优化版本性能测试
 */
void optimizationTest() {
    cout << "\n=== 优化版本性能测试 ===\n";
    
    int n = 1000000;
    vector<int> nums(n);
    srand(42);
    for (int i = 0; i < n; ++i) {
        nums[i] = rand() % 1000000 + 1;
    }
    
    // 测试原始版本
    clock_t startTime = clock();
    int result1 = minimizeArrayValue(nums);
    clock_t endTime = clock();
    double originalTime = (double)(endTime - startTime) * 1000.0 / CLOCKS_PER_SEC;
    cout << "原始版本耗时: " << originalTime << "ms\n";
    
    // 测试优化版本
    startTime = clock();
    int result2 = minimizeArrayValueOptimized(nums);
    endTime = clock();
    double optimizedTime = (double)(endTime - startTime) * 1000.0 / CLOCKS_PER_SEC;
    cout << "优化版本耗时: " << optimizedTime << "ms\n";
    
    // 计算优化比率
    cout << "优化比率: " << originalTime / optimizedTime << "x\n";
    cout << "结果一致: " << (result1 == result2 ? "是" : "否") << '\n';
}

/**
 * 运行所有测试的函数
 */
void runAllTests() {
    correctnessTest();
    performanceTest();
    boundaryTest();
    algorithmComparison();
    memoryAnalysis();
    optimizationTest();
}

/**
 * 主函数
 */
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    runAllTests();
    
    return 0;
}

/**
 * 算法原理解析：
 *
 * 1. 问题分析：
 *    - 给定一个数组，通过调整相邻元素（每次可以将一个元素减1，另一个加1），最小化数组中的最大值
 *    - 关键约束：只能将值从右往左移动，不能从左往右移动
 *    - 这意味着我们需要在保证前缀和的情况下，尽可能平均分配值
 *
 * 2. 前缀和贪心算法：
 *    - 对于每个位置i，计算前i+1个元素的前缀和
 *    - 计算前缀和除以元素个数的平均值（向上取整）
 *    - 这个平均值就是当前前缀中能达到的最小可能的最大值
 *    - 因为不能将值从右往左移动，所以这个最大值是必须接受的下限
 *
 * 3. C++特定优化：
 *    - 使用内联函数减少函数调用开销
 *    - 使用const引用避免不必要的拷贝
 *    - 使用long long类型避免整数溢出
 *    - 使用static_cast进行类型转换，提高代码可读性
 *    - 使用ios::sync_with_stdio(false)和cin.tie(nullptr)加速输入输出
 *
 * 4. 时间复杂度分析：
 *    - 前缀和贪心法：O(n)，只需遍历数组一次
 *    - 二分查找法：O(n log maxVal)，其中maxVal是数组中的最大值
 *    - 前缀和贪心法明显优于二分查找法
 *
 * 5. 空间复杂度分析：
 *    - 两种算法都是O(1)，只需常数额外空间
 *    - 在C++中，vector的内存管理是自动的，但需要注意大规模数据的内存分配
 *
 * 6. 优化技巧：
 *    - 使用long long类型避免前缀和溢出
 *    - 使用整数除法的技巧向上取整：(sum + i) / (i + 1)
 *    - 尽可能减少不必要的计算和分支
 *    - 使用内联函数优化关键路径
 *
 * 7. 边界处理：
 *    - 处理n=1的特殊情况
 *    - 处理数组元素全为0的情况
 *    - 处理数值范围较大的情况，避免整数溢出
 *
 * 8. 工程实现注意事项：
 *    - 在实际项目中，应优先选择前缀和贪心算法
 *    - 对于非常大的数组，需要考虑内存使用和缓存效率
 *    - 在多线程环境中，需要注意数据竞争问题
 *    - 应添加适当的异常处理和参数验证
 */