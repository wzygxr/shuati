/**
 * LeetCode 315. Count of Smaller Numbers After Self - C++实现
 * 题目：统计每个元素后面比它小的元素个数
 * 来源：LeetCode (https://leetcode.com/problems/count-of-smaller-numbers-after-self/)
 * 
 * 算法：平方根分解 + 分块统计
 * 时间复杂度：O(n√n)
 * 空间复杂度：O(n)
 * 最优解：否，最优解是树状数组或归并排序，但平方根分解实现简单
 * 
 * 思路：
 * 1. 将值域分块
 * 2. 从右向左遍历数组
 * 3. 对于每个元素，在对应的块中统计比它小的元素个数
 * 4. 更新块统计信息
 * 
 * 工程化考量：
 * - 值域分块处理大规模数据
 * - 从右向左遍历避免重复统计
 * - 处理负数和大数值范围
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
    vector<int> countSmaller(vector<int>& nums) {
        int n = nums.size();
        if (n == 0) return {};
        
        // 找到最小值和最大值
        int min_val = *min_element(nums.begin(), nums.end());
        int max_val = *max_element(nums.begin(), nums.end());
        
        // 值域大小
        int range = max_val - min_val + 1;
        
        // 分块处理
        int block_size = sqrt(range);
        int block_count = (range + block_size - 1) / block_size;
        
        // 块统计信息
        vector<int> block_sum(block_count, 0);
        vector<vector<int>> blocks(block_count);
        
        // 初始化块
        for (int i = 0; i < block_count; i++) {
            blocks[i].resize(block_size, 0);
        }
        
        vector<int> result(n, 0);
        
        // 从右向左遍历
        for (int i = n - 1; i >= 0; i--) {
            int num = nums[i];
            int val_index = num - min_val;
            int block_idx = val_index / block_size;
            int pos_in_block = val_index % block_size;
            
            // 统计比当前元素小的元素个数
            int count = 0;
            
            // 统计当前块中比当前元素小的部分
            for (int j = 0; j < pos_in_block; j++) {
                count += blocks[block_idx][j];
            }
            
            // 统计前面块中的元素
            for (int j = 0; j < block_idx; j++) {
                count += block_sum[j];
            }
            
            result[i] = count;
            
            // 更新统计信息
            blocks[block_idx][pos_in_block]++;
            block_sum[block_idx]++;
        }
        
        return result;
    }
};

int main() {
    Solution sol;
    
    // 测试用例1
    vector<int> nums1 = {5, 2, 6, 1};
    vector<int> result1 = sol.countSmaller(nums1);
    cout << "测试用例1: [5, 2, 6, 1]" << endl;
    cout << "结果: ";
    for (int num : result1) {
        cout << num << " ";
    }
    cout << "(期望: [2, 1, 1, 0])" << endl;
    
    // 测试用例2
    vector<int> nums2 = {-1, -1};
    vector<int> result2 = sol.countSmaller(nums2);
    cout << "测试用例2: [-1, -1]" << endl;
    cout << "结果: ";
    for (int num : result2) {
        cout << num << " ";
    }
    cout << "(期望: [0, 0])" << endl;
    
    // 测试用例3
    vector<int> nums3 = {1, 9, 7, 8, 5};
    vector<int> result3 = sol.countSmaller(nums3);
    cout << "测试用例3: [1, 9, 7, 8, 5]" << endl;
    cout << "结果: ";
    for (int num : result3) {
        cout << num << " ";
    }
    cout << "(期望: [0, 3, 1, 1, 0])" << endl;
    
    return 0;
}