/**
 * LeetCode 307. Range Sum Query - Mutable - C++实现
 * 题目：支持单点更新的区间和查询
 * 来源：LeetCode (https://leetcode.com/problems/range-sum-query-mutable/)
 * 
 * 算法：平方根分解（分块算法）
 * 时间复杂度：
 *   - 初始化：O(n)
 *   - 单点更新：O(1)
 *   - 区间查询：O(√n)
 * 空间复杂度：O(n)
 * 最优解：是，对于频繁更新和查询的场景，平方根分解是平衡的选择
 * 
 * 思路：
 * 1. 将数组分成√n个块，每个块维护区间和
 * 2. 单点更新时，同时更新原数组和对应块的区间和
 * 3. 区间查询时，完整块直接使用块的和，不完整块暴力求和
 * 
 * 工程化考量：
 * - 块大小选择√n，平衡更新和查询效率
 * - 使用预计算的块和减少查询时间
 * - 边界处理：正确处理区间边界和块边界
 * - 异常处理：检查索引有效性
 */

#include <iostream>
#include <vector>
#include <cmath>
#include <stdexcept>
using namespace std;

class NumArray {
private:
    vector<int> nums;        // 原始数组
    vector<int> block_sum;   // 每个块的和
    int block_size;          // 块大小
    int block_count;         // 块数量

public:
    NumArray(vector<int>& nums) {
        this->nums = nums;
        int n = nums.size();
        block_size = sqrt(n);
        block_count = (n + block_size - 1) / block_size;
        block_sum.resize(block_count, 0);
        
        // 初始化块和
        for (int i = 0; i < n; i++) {
            block_sum[i / block_size] += nums[i];
        }
    }
    
    // 单点更新
    void update(int index, int val) {
        if (index < 0 || index >= nums.size()) {
            throw out_of_range("Index out of range");
        }
        
        int block_idx = index / block_size;
        block_sum[block_idx] += (val - nums[index]);
        nums[index] = val;
    }
    
    // 区间和查询
    int sumRange(int left, int right) {
        if (left < 0 || right >= nums.size() || left > right) {
            throw out_of_range("Invalid range");
        }
        
        int block_l = left / block_size;
        int block_r = right / block_size;
        int sum = 0;
        
        // 同一个块内，直接暴力求和
        if (block_l == block_r) {
            for (int i = left; i <= right; i++) {
                sum += nums[i];
            }
            return sum;
        }
        
        // 左边界不完整块
        for (int i = left; i < (block_l + 1) * block_size; i++) {
            sum += nums[i];
        }
        
        // 中间完整块
        for (int i = block_l + 1; i < block_r; i++) {
            sum += block_sum[i];
        }
        
        // 右边界不完整块
        for (int i = block_r * block_size; i <= right; i++) {
            sum += nums[i];
        }
        
        return sum;
    }
};

int main() {
    // 测试用例
    vector<int> nums = {1, 3, 5, 7, 9, 11};
    NumArray numArray(nums);
    
    // 测试区间查询
    cout << "sumRange(0, 2): " << numArray.sumRange(0, 2) << endl;  // 应该输出9 (1+3+5)
    cout << "sumRange(1, 4): " << numArray.sumRange(1, 4) << endl;  // 应该输出24 (3+5+7+9)
    
    // 测试单点更新
    numArray.update(1, 10);  // 将索引1的值从3改为10
    cout << "After update(1, 10):" << endl;
    cout << "sumRange(0, 2): " << numArray.sumRange(0, 2) << endl;  // 应该输出16 (1+10+5)
    
    // 边界测试
    numArray.update(5, 20);  // 更新最后一个元素
    cout << "sumRange(0, 5): " << numArray.sumRange(0, 5) << endl;  // 应该输出52 (1+10+5+7+9+20)
    
    // 异常测试
    try {
        numArray.update(10, 100);  // 索引越界
    } catch (const exception& e) {
        cout << "Exception caught: " << e.what() << endl;
    }
    
    return 0;
}