/**
 * LeetCode 327. Count of Range Sum - C++实现
 * 题目：统计区间和在[lower, upper]范围内的子数组个数
 * 来源：LeetCode (https://leetcode.com/problems/count-of-range-sum/)
 * 
 * 算法：平方根分解 + 前缀和分块
 * 时间复杂度：O(n√n)
 * 空间复杂度：O(n)
 * 最优解：否，最优解是归并排序或树状数组，但平方根分解是实用的替代方案
 * 
 * 思路：
 * 1. 计算前缀和数组
 * 2. 将前缀和数组分块
 * 3. 对于每个位置i，在前面的块中查找满足条件的j
 * 4. 条件：lower ≤ prefix[i] - prefix[j] ≤ upper
 * 
 * 工程化考量：
 * - 使用前缀和简化区间和计算
 * - 分块处理大规模数据
 * - 排序块内元素以支持二分查找
 * - 处理整数溢出问题
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <climits>
using namespace std;

class Solution {
public:
    int countRangeSum(vector<int>& nums, int lower, int upper) {
        int n = nums.size();
        if (n == 0) return 0;
        
        // 计算前缀和
        vector<long long> prefix(n + 1, 0);
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        
        // 分块处理
        int block_size = sqrt(n);
        int block_count = (n + block_size - 1) / block_size;
        
        // 每个块维护排序后的前缀和
        vector<vector<long long>> blocks(block_count);
        for (int i = 0; i <= n; i++) {
            int block_idx = i / block_size;
            blocks[block_idx].push_back(prefix[i]);
        }
        
        // 对每个块排序
        for (int i = 0; i < block_count; i++) {
            sort(blocks[i].begin(), blocks[i].end());
        }
        
        int count = 0;
        
        // 对于每个位置i，统计满足条件的j
        for (int i = 1; i <= n; i++) {
            long long current = prefix[i];
            
            // 条件：lower ≤ current - prefix[j] ≤ upper
            // 等价于：current - upper ≤ prefix[j] ≤ current - lower
            long long left_bound = current - upper;
            long long right_bound = current - lower;
            
            // 遍历前面的块
            int j = 0;
            while (j < i) {
                int block_idx = j / block_size;
                int block_start = block_idx * block_size;
                int block_end = min((block_idx + 1) * block_size, i);
                
                // 如果整个块都在范围内，使用二分查找
                if (j == block_start && block_end - block_start == block_size) {
                    auto& block = blocks[block_idx];
                    auto left_it = lower_bound(block.begin(), block.end(), left_bound);
                    auto right_it = upper_bound(block.begin(), block.end(), right_bound);
                    count += (right_it - left_it);
                    j = block_end;
                } else {
                    // 部分块，暴力遍历
                    for (int k = j; k < block_end; k++) {
                        long long diff = current - prefix[k];
                        if (diff >= lower && diff <= upper) {
                            count++;
                        }
                    }
                    j = block_end;
                }
            }
        }
        
        return count;
    }
};

int main() {
    Solution sol;
    
    // 测试用例1
    vector<int> nums1 = {-2, 5, -1};
    int lower1 = -2, upper1 = 2;
    cout << "测试用例1: nums = [-2, 5, -1], lower = -2, upper = 2" << endl;
    cout << "结果: " << sol.countRangeSum(nums1, lower1, upper1) << " (期望: 3)" << endl;
    
    // 测试用例2
    vector<int> nums2 = {0, -3, -3, 1, 1, 2};
    int lower2 = 3, upper2 = 5;
    cout << "测试用例2: nums = [0, -3, -3, 1, 1, 2], lower = 3, upper = 5" << endl;
    cout << "结果: " << sol.countRangeSum(nums2, lower2, upper2) << " (期望: 2)" << endl;
    
    // 边界测试
    vector<int> nums3 = {1};
    int lower3 = 0, upper3 = 0;
    cout << "边界测试: nums = [1], lower = 0, upper = 0" << endl;
    cout << "结果: " << sol.countRangeSum(nums3, lower3, upper3) << " (期望: 1)" << endl;
    
    return 0;
}