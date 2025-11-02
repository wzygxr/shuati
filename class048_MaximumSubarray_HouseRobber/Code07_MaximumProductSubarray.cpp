#include <vector>
#include <algorithm>
#include <climits>
#include <stdexcept>
#include <iostream>

using namespace std;

/**
 * 乘积最大子数组 - C++实现
 * 给你一个整数数组 nums
 * 请你找出数组中乘积最大的非空连续子数组
 * 并返回该子数组所对应的乘积
 * 测试链接 : https://leetcode.cn/problems/maximum-product-subarray/
 * 
 * 算法核心思想：
 * 1. 与最大子数组和问题不同，乘积问题需要考虑负数的特性
 * 2. 关键观察：负数乘以负数会变成正数，所以需要同时维护最大和最小乘积
 * 3. 使用动态规划思想，维护当前的最大乘积和最小乘积
 * 4. 对于每个元素，有三种选择：从当前元素重新开始、乘以之前的最小乘积、乘以之前的最大乘积
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n) - 只需遍历数组一次
 * - 空间复杂度：O(1) - 优化后只需常数空间
 * 
 * 工程化考量：
 * 1. 数值溢出处理：使用long long类型避免整数溢出
 * 2. 边界处理：处理空数组、单元素数组等特殊情况
 * 3. 性能优化：单次遍历同时维护最大和最小乘积
 */

/**
 * 计算乘积最大子数组的乘积值
 * 
 * @param nums 输入整数数组
 * @return 乘积最大子数组的乘积值
 * @throws invalid_argument 如果输入数组为空
 */
int maxProduct(vector<int>& nums) {
    // 边界检查
    if (nums.empty()) {
        throw invalid_argument("输入数组不能为空");
    }
    
    // 使用long long类型避免整数溢出
    long long ans = nums[0];      // 全局最大乘积
    long long minProd = nums[0];  // 当前最小乘积
    long long maxProd = nums[0];  // 当前最大乘积
    long long curmin, curmax;     // 临时变量
    
    // 从第二个元素开始遍历
    for (int i = 1; i < nums.size(); i++) {
        // 计算当前元素可能产生的三种乘积
        curmin = min((long long)nums[i], min(minProd * nums[i], maxProd * nums[i]));
        curmax = max((long long)nums[i], max(minProd * nums[i], maxProd * nums[i]));
        
        // 更新状态
        minProd = curmin;
        maxProd = curmax;
        
        // 更新全局最大值
        ans = max(ans, maxProd);
    }
    
    return (int)ans;
}

/**
 * 另一种实现：使用数组存储状态，更直观但空间复杂度较高
 */
int maxProductArray(vector<int>& nums) {
    if (nums.empty()) {
        return 0;
    }
    
    int n = nums.size();
    vector<long long> maxDp(n);  // 存储以i结尾的最大乘积
    vector<long long> minDp(n);  // 存储以i结尾的最小乘积
    
    maxDp[0] = nums[0];
    minDp[0] = nums[0];
    long long ans = nums[0];
    
    for (int i = 1; i < n; i++) {
        // 三种可能：重新开始、乘以最大、乘以最小
        maxDp[i] = max((long long)nums[i], max(maxDp[i-1] * nums[i], minDp[i-1] * nums[i]));
        minDp[i] = min((long long)nums[i], min(maxDp[i-1] * nums[i], minDp[i-1] * nums[i]));
        ans = max(ans, maxDp[i]);
    }
    
    return (int)ans;
}

/**
 * 空间优化版本：使用两个变量代替数组
 */
int maxProductOptimized(vector<int>& nums) {
    if (nums.empty()) {
        return 0;
    }
    
    long long ans = nums[0];
    long long minPrev = nums[0];
    long long maxPrev = nums[0];
    
    for (int i = 1; i < nums.size(); i++) {
        long long tempMin = minPrev;
        long long tempMax = maxPrev;
        
        minPrev = min((long long)nums[i], min(tempMin * nums[i], tempMax * nums[i]));
        maxPrev = max((long long)nums[i], max(tempMin * nums[i], tempMax * nums[i]));
        ans = max(ans, maxPrev);
    }
    
    return (int)ans;
}

/**
 * 测试函数：验证算法正确性
 */
void testMaxProduct() {
    vector<vector<int>> testCases = {
        {2, 3, -2, 4},           // 期望: 6
        {-2, -3, -1, -4},        // 期望: 12
        {-2, 0, -1},             // 期望: 0
        {5},                      // 期望: 5
        {2, -5, -2, -4, 3},      // 期望: 24
        {0, 2},                   // 期望: 2
        {-2, 3, -4},             // 期望: 24
        {1, -2, 3, -4, 5}        // 期望: 120
    };
    
    vector<int> expected = {6, 12, 0, 5, 24, 2, 24, 120};
    
    cout << "=== 乘积最大子数组算法测试 ===" << endl;
    
    for (size_t i = 0; i < testCases.size(); ++i) {
        int result1 = maxProduct(testCases[i]);
        int result2 = maxProductArray(testCases[i]);
        int result3 = maxProductOptimized(testCases[i]);
        
        cout << "测试用例 " << i+1 << ": ";
        cout << "输入: [";
        for (size_t j = 0; j < testCases[i].size(); ++j) {
            cout << testCases[i][j];
            if (j < testCases[i].size() - 1) cout << ", ";
        }
        cout << "]";
        cout << ", 结果=" << result1 << ", 期望=" << expected[i];
        cout << ", 状态=" << (result1 == expected[i] ? "通过" : "失败");
        cout << ", 方法一致性=" << (result1 == result2 && result2 == result3 ? "一致" : "不一致") << endl;
    }
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * 性能测试：大数据量验证
 */
void performanceTest() {
    const int SIZE = 1000000;
    vector<int> largeArray(SIZE, 2);  // 全2数组，最大乘积是2^SIZE
    
    cout << "=== 性能测试开始 ===" << endl;
    cout << "数据量: " << SIZE << " 个元素" << endl;
    
    auto start = chrono::high_resolution_clock::now();
    int result = maxProductOptimized(largeArray);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "计算结果: " << result << " (可能溢出，主要测试性能)" << endl;
    cout << "执行时间: " << duration.count() << " 微秒" << endl;
    cout << "=== 性能测试结束 ===" << endl;
}

int main() {
    // 运行功能测试
    testMaxProduct();
    
    // 运行性能测试（可选）
    // performanceTest();
    
    return 0;
}

/*
 * 扩展思考与工程化考量：
 * 
 * 1. 算法变体分析：
 *    - 乘积最小子数组：类似思路，但关注最小值
 *    - 乘积为特定值的子数组：使用哈希表记录前缀乘积
 *    - 乘积小于K的子数组：滑动窗口方法
 * 
 * 2. 工程应用场景：
 *    - 信号处理：寻找信号中的最大乘积段
 *    - 金融分析：计算收益率的最大连续乘积
 *    - 数据分析：寻找具有最大乘积的数据子集
 * 
 * 3. 性能优化策略：
 *    - 单次遍历：同时维护最大和最小乘积
 *    - 空间优化：使用常数空间代替数组
 *    - 数值处理：选择合适的数值类型避免溢出
 * 
 * 4. 代码质量保证：
 *    - 单元测试：覆盖各种边界情况
 *    - 性能测试：验证大数据量下的表现
 *    - 异常处理：确保程序的健壮性
 */