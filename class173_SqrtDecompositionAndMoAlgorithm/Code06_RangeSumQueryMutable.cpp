// LeetCode 307. Range Sum Query - Mutable - 分块算法实现 (C++版本)
// 题目来源: LeetCode
// 链接: https://leetcode.com/problems/range-sum-query-mutable/
// 题目大意: 实现一个支持区间和查询和单点更新的数据结构
// 约束条件: 
// - 1 <= nums.length <= 3 * 10^4
// - -100 <= nums[i] <= 100
// - 最多调用 3 * 10^4 次 update 和 sumRange 操作

// 分块算法分析:
// 时间复杂度: 
// - 构造函数: O(n)，需要初始化数组和分块信息
// - update操作: O(1)，只需要更新单个元素和对应的块和
// - sumRange操作: O(√n)，需要遍历部分块和部分元素
// 空间复杂度: O(n)，需要存储原始数组和块和数组

// 最优解验证:
// 分块算法是解决此类问题的经典方法之一，在更新和查询操作之间提供了良好的平衡。
// 对于频繁更新和查询的场景，分块算法通常比线段树更简单且常数更小。

#include <iostream>
#include <vector>
#include <cmath>
#include <stdexcept>
#include <chrono>

using namespace std;

class Code06_RangeSumQueryMutable {
private:
    vector<int> nums;           // 原始数组
    vector<int> blockSum;        // 每个块的和
    int blockSize;              // 块大小
    int blockCount;             // 块数量
    
public:
    /**
     * 构造函数 - 初始化分块数据结构
     * 
     * @param nums 初始数组
     * 
     * 算法步骤:
     * 1. 计算合适的块大小，通常选择sqrt(n)
     * 2. 初始化块和数组
     * 3. 计算每个块的初始和
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    Code06_RangeSumQueryMutable(vector<int>& nums) {
        if (nums.empty()) {
            throw invalid_argument("数组不能为空");
        }
        
        this->nums = nums;
        
        // 计算块大小，通常选择sqrt(n)
        this->blockSize = static_cast<int>(sqrt(nums.size()));
        this->blockCount = (nums.size() + blockSize - 1) / blockSize;
        
        // 初始化块和数组
        this->blockSum.resize(blockCount, 0);
        
        // 计算每个块的初始和
        for (int i = 0; i < nums.size(); i++) {
            int blockIndex = i / blockSize;
            blockSum[blockIndex] += nums[i];
        }
    }
    
    /**
     * 更新操作 - 将索引i处的值更新为val
     * 
     * @param i 要更新的索引
     * @param val 新的值
     * 
     * 算法步骤:
     * 1. 验证索引有效性
     * 2. 计算值的变化量
     * 3. 更新原始数组
     * 4. 更新对应的块和
     * 
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * 异常处理:
     * - 索引越界: 抛出invalid_argument异常
     */
    void update(int i, int val) {
        // 边界检查
        if (i < 0 || i >= nums.size()) {
            throw invalid_argument("索引越界: " + to_string(i));
        }
        
        // 计算值的变化量
        int delta = val - nums[i];
        
        // 更新原始数组
        nums[i] = val;
        
        // 更新对应的块和
        int blockIndex = i / blockSize;
        blockSum[blockIndex] += delta;
    }
    
    /**
     * 区间和查询 - 计算索引left到right的区间和
     * 
     * @param left 区间左边界（包含）
     * @param right 区间右边界（包含）
     * @return 区间和
     * 
     * 算法步骤:
     * 1. 验证边界有效性
     * 2. 计算左右边界所在的块
     * 3. 如果左右边界在同一个块内，直接遍历计算
     * 4. 否则分三部分计算:
     *    - 左边不完整块
     *    - 中间完整块
     *    - 右边不完整块
     * 
     * 时间复杂度: O(√n)
     * 空间复杂度: O(1)
     * 
     * 异常处理:
     * - 边界越界: 抛出invalid_argument异常
     * - left > right: 抛出invalid_argument异常
     */
    int sumRange(int left, int right) {
        // 边界检查
        if (left < 0 || right >= nums.size() || left > right) {
            throw invalid_argument("区间边界无效: [" + to_string(left) + ", " + to_string(right) + "]");
        }
        
        // 如果左右边界相同，直接返回该位置的值
        if (left == right) {
            return nums[left];
        }
        
        int sum = 0;
        int leftBlock = left / blockSize;
        int rightBlock = right / blockSize;
        
        // 如果左右边界在同一个块内
        if (leftBlock == rightBlock) {
            // 直接遍历该块内的元素
            for (int i = left; i <= right; i++) {
                sum += nums[i];
            }
        } else {
            // 计算左边不完整块的和
            for (int i = left; i < (leftBlock + 1) * blockSize; i++) {
                sum += nums[i];
            }
            
            // 计算中间完整块的和
            for (int block = leftBlock + 1; block < rightBlock; block++) {
                sum += blockSum[block];
            }
            
            // 计算右边不完整块的和
            for (int i = rightBlock * blockSize; i <= right; i++) {
                sum += nums[i];
            }
        }
        
        return sum;
    }
    
    /**
     * 获取数组长度
     * 
     * @return 数组长度
     */
    int size() const {
        return nums.size();
    }
    
    /**
     * 获取块大小
     * 
     * @return 块大小
     */
    int getBlockSize() const {
        return blockSize;
    }
    
    /**
     * 获取块数量
     * 
     * @return 块数量
     */
    int getBlockCount() const {
        return blockCount;
    }
    
    /**
     * 调试方法 - 打印数据结构状态
     * 用于调试和问题定位
     */
    void printStructure() const {
        cout << "=== 分块数据结构状态 ===" << endl;
        cout << "数组长度: " << nums.size() << endl;
        cout << "块大小: " << blockSize << endl;
        cout << "块数量: " << blockCount << endl;
        
        cout << "原始数组: ";
        for (int num : nums) {
            cout << num << " ";
        }
        cout << endl;
        
        cout << "块和数组: ";
        for (int sum : blockSum) {
            cout << sum << " ";
        }
        cout << endl;
        
        // 验证块和正确性
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }
        
        int blockSumTotal = 0;
        for (int sum : blockSum) {
            blockSumTotal += sum;
        }
        
        cout << "数组总和: " << totalSum << endl;
        cout << "块和总和: " << blockSumTotal << endl;
        cout << "一致性检查: " << (totalSum == blockSumTotal ? "通过" : "失败") << endl;
    }
};

/**
 * 单元测试函数 - 验证算法的正确性
 * 
 * 测试用例设计:
 * 1. 正常情况测试
 * 2. 边界情况测试
 * 3. 异常情况测试
 * 4. 性能测试
 */
void testCode06_RangeSumQueryMutable() {
    // 测试用例1: 正常情况
    cout << "=== 测试用例1: 正常情况 ===" << endl;
    vector<int> nums1 = {1, 3, 5, 7, 9, 11};
    Code06_RangeSumQueryMutable numArray1(nums1);
    
    // 测试初始区间和
    cout << "sumRange(0, 2) = " << numArray1.sumRange(0, 2) << endl; // 期望: 1+3+5=9
    cout << "sumRange(1, 4) = " << numArray1.sumRange(1, 4) << endl; // 期望: 3+5+7+9=24
    
    // 测试更新操作
    numArray1.update(1, 10);
    cout << "更新后 sumRange(0, 2) = " << numArray1.sumRange(0, 2) << endl; // 期望: 1+10+5=16
    
    // 测试用例2: 边界情况
    cout << "\n=== 测试用例2: 边界情况 ===" << endl;
    vector<int> nums2 = {2};
    Code06_RangeSumQueryMutable numArray2(nums2);
    
    cout << "sumRange(0, 0) = " << numArray2.sumRange(0, 0) << endl; // 期望: 2
    numArray2.update(0, 5);
    cout << "更新后 sumRange(0, 0) = " << numArray2.sumRange(0, 0) << endl; // 期望: 5
    
    // 测试用例3: 异常情况
    cout << "\n=== 测试用例3: 异常情况 ===" << endl;
    try {
        vector<int> nums3 = {};
        Code06_RangeSumQueryMutable numArray3(nums3);
    } catch (const invalid_argument& e) {
        cout << "正确捕获异常: " << e.what() << endl;
    }
    
    // 测试用例4: 大规模数据性能测试
    cout << "\n=== 测试用例4: 性能测试 ===" << endl;
    int size = 10000;
    vector<int> nums4(size);
    for (int i = 0; i < size; i++) {
        nums4[i] = i + 1;
    }
    
    Code06_RangeSumQueryMutable numArray4(nums4);
    
    auto startTime = chrono::high_resolution_clock::now();
    int result = numArray4.sumRange(0, size - 1);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    cout << "大规模数据区间和: " << result << endl;
    cout << "查询时间: " << duration.count() << "微秒" << endl;
    
    // 验证结果正确性
    long expected = static_cast<long>(size) * (size + 1) / 2;
    cout << "期望结果: " << expected << endl;
    cout << "结果正确性: " << (result == expected ? "正确" : "错误") << endl;
    
    cout << "\n=== 所有测试用例执行完成 ===" << endl;
}

/**
 * 性能对比测试 - 比较分块算法与暴力算法的性能差异
 */
void performanceComparison() {
    cout << "=== 性能对比测试 ===" << endl;
    
    int size = 100000;
    vector<int> nums(size);
    for (int i = 0; i < size; i++) {
        nums[i] = i + 1;
    }
    
    // 分块算法测试
    Code06_RangeSumQueryMutable blockArray(nums);
    
    auto start1 = chrono::high_resolution_clock::now();
    int blockResult = blockArray.sumRange(0, size - 1);
    auto end1 = chrono::high_resolution_clock::now();
    auto blockTime = chrono::duration_cast<chrono::microseconds>(end1 - start1);
    
    // 暴力算法测试
    auto start2 = chrono::high_resolution_clock::now();
    int bruteResult = 0;
    for (int i = 0; i < size; i++) {
        bruteResult += nums[i];
    }
    auto end2 = chrono::high_resolution_clock::now();
    auto bruteTime = chrono::duration_cast<chrono::microseconds>(end2 - start2);
    
    cout << "分块算法结果: " << blockResult << ", 时间: " << blockTime.count() << "微秒" << endl;
    cout << "暴力算法结果: " << bruteResult << ", 时间: " << bruteTime.count() << "微秒" << endl;
    cout << "性能提升倍数: " << static_cast<double>(bruteTime.count()) / blockTime.count() << "倍" << endl;
    cout << "结果一致性: " << (blockResult == bruteResult ? "一致" : "不一致") << endl;
}

int main() {
    try {
        // 执行单元测试
        testCode06_RangeSumQueryMutable();
        
        // 执行性能对比测试
        performanceComparison();
        
        // 测试调试功能
        cout << "\n=== 调试功能测试 ===" << endl;
        vector<int> testNums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Code06_RangeSumQueryMutable testArray(testNums);
        testArray.printStructure();
        
    } catch (const exception& e) {
        cerr << "测试过程中发生异常: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}

/**
 * C++语言特性差异分析:
 * 
 * 1. 内存管理:
 *    - 使用vector自动管理内存，避免手动内存分配
 *    - vector提供边界检查功能（通过at方法）
 *    - 支持RAII（资源获取即初始化）原则
 * 
 * 2. 异常处理:
 *    - 使用C++标准异常类（invalid_argument等）
 *    - 提供详细的错误信息
 *    - 支持异常安全保证
 * 
 * 3. 性能优化:
 *    - 使用const成员函数提高代码可读性
 *    - 避免不必要的拷贝操作
 *    - 使用高效的标准库容器
 * 
 * 4. 调试支持:
 *    - 提供调试方法打印数据结构状态
 *    - 包含性能测试功能
 *    - 支持单元测试框架集成
 * 
 * 5. 跨平台兼容性:
 *    - 使用标准C++特性，确保跨平台兼容
 *    - 避免平台特定的API调用
 *    - 提供统一的接口设计
 */