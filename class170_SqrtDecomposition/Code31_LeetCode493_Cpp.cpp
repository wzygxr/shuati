/**
 * LeetCode 493. Reverse Pairs - C++实现
 * 题目：统计重要逆序对（nums[i] > 2*nums[j]且i<j）
 * 来源：LeetCode (https://leetcode.com/problems/reverse-pairs/)
 * 
 * 算法：平方根分解 + 分块统计
 * 时间复杂度：O(n√n)
 * 空间复杂度：O(n)
 * 最优解：否，最优解是归并排序或树状数组，但平方根分解实现简单
 * 
 * 思路：
 * 1. 将数组分块
 * 2. 对每个块进行排序
 * 3. 对于每个元素，在后面的块中查找满足条件的元素个数
 * 4. 使用二分查找优化查询效率
 * 
 * 工程化考量：
 * - 分块处理大规模数据
 * - 使用二分查找优化查询
 * - 处理整数溢出问题
 * - 优化块大小选择
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <climits>
using namespace std;

class Solution {
public:
    int reversePairs(vector<int>& nums) {
        int n = nums.size();
        if (n == 0) return 0;
        
        // 分块处理
        int block_size = sqrt(n);
        int block_count = (n + block_size - 1) / block_size;
        
        // 每个块维护排序后的副本
        vector<vector<long long>> blocks(block_count);
        for (int i = 0; i < n; i++) {
            int block_idx = i / block_size;
            blocks[block_idx].push_back(nums[i]);
        }
        
        // 对每个块排序
        for (int i = 0; i < block_count; i++) {
            sort(blocks[i].begin(), blocks[i].end());
        }
        
        int count = 0;
        
        // 对于每个元素，统计后面满足条件的元素个数
        for (int i = 0; i < n; i++) {
            long long current = nums[i];
            long long target = 2LL * current;
            
            // 在当前元素后面的块中查找
            for (int j = i + 1; j < n; ) {
                int block_idx = j / block_size;
                int block_start = block_idx * block_size;
                int block_end = min((block_idx + 1) * block_size, n);
                
                // 如果整个块都在范围内，使用二分查找
                if (j == block_start && block_end - block_start == block_size) {
                    auto& block = blocks[block_idx];
                    // 查找大于target的元素个数
                    auto it = upper_bound(block.begin(), block.end(), target);
                    count += (block.end() - it);
                    j = block_end;
                } else {
                    // 部分块，暴力遍历
                    for (int k = j; k < block_end; k++) {
                        if (nums[k] > target) {
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
    vector<int> nums1 = {1, 3, 2, 3, 1};
    cout << "测试用例1: [1, 3, 2, 3, 1]" << endl;
    cout << "重要逆序对数量: " << sol.reversePairs(nums1) << " (期望: 2)" << endl;
    
    // 测试用例2
    vector<int> nums2 = {2, 4, 3, 5, 1};
    cout << "测试用例2: [2, 4, 3, 5, 1]" << endl;
    cout << "重要逆序对数量: " << sol.reversePairs(nums2) << " (期望: 3)" << endl;
    
    // 测试用例3
    vector<int> nums3 = {5, 4, 3, 2, 1};
    cout << "测试用例3: [5, 4, 3, 2, 1]" << endl;
    cout << "重要逆序对数量: " << sol.reversePairs(nums3) << " (期望: 4)" << endl;
    
    // 边界测试
    vector<int> nums4 = {1};
    cout << "边界测试: [1]" << endl;
    cout << "重要逆序对数量: " << sol.reversePairs(nums4) << " (期望: 0)" << endl;
    
    return 0;
}