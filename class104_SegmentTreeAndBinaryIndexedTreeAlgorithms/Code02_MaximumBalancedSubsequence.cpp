/**
 * LeetCode 2784. 平衡子序列的最大和 (Maximum Balanced Subsequence Sum)
 * 题目链接: https://leetcode.cn/problems/maximum-balanced-subsequence-sum/
 * 
 * 题目描述:
 * 给定一个长度为n的数组nums，定义平衡子序列为满足以下条件的子序列：
 * 对于子序列中任意两个下标i和j（i在j的左边），必须满足nums[j] - nums[i] >= j - i
 * 求所有平衡子序列中元素和的最大值。
 * 
 * 解题思路:
 * 使用树状数组优化动态规划的方法解决此问题。
 * 1. 首先将约束条件nums[j] - nums[i] >= j - i变形为nums[j] - j >= nums[i] - i
 *    这样我们定义一个新的指标值：nums[i] - i
 * 2. 对于每个元素nums[i]，我们计算其指标值nums[i] - i
 * 3. 使用树状数组维护以指标值为维度的动态规划状态
 *    dp[k]表示以指标值不超过sort[k]的元素结尾的平衡子序列的最大和
 * 4. 遍历数组，对于每个元素，查询之前指标值不超过当前指标值的最大dp值
 *    然后更新当前指标值对应的dp值
 * 
 * 时间复杂度分析:
 * - 离散化: O(n log n)
 * - 遍历更新: O(n log n)
 * - 总时间复杂度: O(n log n)
 * 空间复杂度: O(n) 用于存储离散化数组和树状数组
 * 
 * 工程化考量:
 * 1. 异常处理: 处理空数组和边界情况
 * 2. 性能优化: 使用离散化减少空间占用
 * 3. 边界测试: 测试单元素、全正数、全负数等场景
 * 4. 可读性: 清晰的变量命名和注释
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_map>
#include <climits>
#include <cassert>
#include <chrono>

using namespace std;

class FenwickTree {
private:
    vector<long long> tree;
    int size;
    
public:
    /**
     * 构造函数，初始化树状数组
     * 
     * @param n 树状数组的大小
     */
    FenwickTree(int n) : size(n), tree(n + 1, LLONG_MIN) {}
    
    /**
     * 更新树状数组
     * 
     * @param index 要更新的位置
     * @param value 新的值
     */
    void update(int index, long long value) {
        while (index <= size) {
            if (value > tree[index]) {
                tree[index] = value;
            }
            index += index & -index;
        }
    }
    
    /**
     * 查询前缀最大值
     * 
     * @param index 查询的结束位置
     * @return 前缀最大值
     */
    long long query(int index) {
        long long result = LLONG_MIN;
        while (index > 0) {
            if (tree[index] > result) {
                result = tree[index];
            }
            index -= index & -index;
        }
        return result;
    }
};

/**
 * 计算平衡子序列的最大和
 * 
 * @param nums 输入数组
 * @return 平衡子序列的最大和
 * @throws invalid_argument 如果输入为空数组
 */
long long maxBalancedSubsequenceSum(vector<int>& nums) {
    // 异常处理：空数组
    if (nums.empty()) {
        throw invalid_argument("输入数组不能为空");
    }
    
    int n = nums.size();
    
    // 特殊情况：单元素数组
    if (n == 1) {
        return nums[0];
    }
    
    // 计算指标值：nums[i] - i
    vector<long long> indicators(n);
    for (int i = 0; i < n; i++) {
        indicators[i] = (long long)nums[i] - i;
    }
    
    // 离散化处理
    vector<long long> sorted_indicators = indicators;
    sort(sorted_indicators.begin(), sorted_indicators.end());
    sorted_indicators.erase(unique(sorted_indicators.begin(), sorted_indicators.end()), sorted_indicators.end());
    
    unordered_map<long long, int> rank_map;
    for (int i = 0; i < sorted_indicators.size(); i++) {
        rank_map[sorted_indicators[i]] = i + 1;
    }
    
    // 初始化树状数组
    FenwickTree fenwick(sorted_indicators.size());
    
    // 遍历数组进行动态规划
    for (int i = 0; i < n; i++) {
        // 获取当前指标的排名
        int k = rank_map[indicators[i]];
        
        // 查询之前指标值不超过当前指标值的最大和
        long long pre_max = fenwick.query(k);
        
        // 计算当前状态值
        long long current_val = nums[i];
        if (pre_max > 0) {
            current_val += pre_max;
        }
        
        // 更新树状数组
        fenwick.update(k, current_val);
    }
    
    // 返回最大值
    return fenwick.query(sorted_indicators.size());
}

/**
 * 测试函数，验证算法正确性
 */
void testMaxBalancedSubsequenceSum() {
    cout << "开始测试平衡子序列最大和算法..." << endl;
    
    // 测试用例1: 正常情况
    vector<int> nums1 = {3, 5, 6, 9};
    long long result1 = maxBalancedSubsequenceSum(nums1);
    cout << "测试用例1: {3, 5, 6, 9} -> " << result1 << endl;
    assert(result1 == 23 && "测试用例1失败");
    
    // 测试用例2: 包含负数
    vector<int> nums2 = {-2, -1, -3, -4};
    long long result2 = maxBalancedSubsequenceSum(nums2);
    cout << "测试用例2: {-2, -1, -3, -4} -> " << result2 << endl;
    assert(result2 == -1 && "测试用例2失败");
    
    // 测试用例3: 混合正负数
    vector<int> nums3 = {10, -2, 5, -3, 8};
    long long result3 = maxBalancedSubsequenceSum(nums3);
    cout << "测试用例3: {10, -2, 5, -3, 8} -> " << result3 << endl;
    
    // 测试用例4: 单元素
    vector<int> nums4 = {7};
    long long result4 = maxBalancedSubsequenceSum(nums4);
    cout << "测试用例4: {7} -> " << result4 << endl;
    assert(result4 == 7 && "测试用例4失败");
    
    // 测试用例5: 全正数
    vector<int> nums5 = {1, 2, 3, 4, 5};
    long long result5 = maxBalancedSubsequenceSum(nums5);
    cout << "测试用例5: {1, 2, 3, 4, 5} -> " << result5 << endl;
    
    // 测试用例6: 边界情况 - 空数组
    vector<int> nums6;
    try {
        maxBalancedSubsequenceSum(nums6);
        assert(false && "应该抛出异常");
    } catch (const invalid_argument& e) {
        cout << "测试用例6: 空数组异常处理通过 - " << e.what() << endl;
    }
    
    cout << "所有测试用例通过！" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "开始性能测试..." << endl;
    
    // 大规模数据测试
    vector<int> large_nums;
    for (int i = 0; i < 10000; i++) {
        large_nums.push_back(i);
    }
    
    auto start = chrono::high_resolution_clock::now();
    long long result = maxBalancedSubsequenceSum(large_nums);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "大规模测试: 数组长度" << large_nums.size() 
         << "，结果" << result 
         << "，耗时" << duration.count() << "毫秒" << endl;
}

int main() {
    // 运行测试
    testMaxBalancedSubsequenceSum();
    
    // 性能测试
    performanceTest();
    
    return 0;
}