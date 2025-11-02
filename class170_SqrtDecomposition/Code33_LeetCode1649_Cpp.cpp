/**
 * LeetCode 1649. Create Sorted Array through Instructions - C++实现
 * 题目：通过指令创建有序数组的代价计算
 * 来源：LeetCode (https://leetcode.com/problems/create-sorted-array-through-instructions/)
 * 
 * 算法：平方根分解 + 分块统计
 * 时间复杂度：O(n√n)
 * 空间复杂度：O(n)
 * 最优解：否，最优解是树状数组或线段树，但平方根分解实现简单
 * 
 * 思路：
 * 1. 将值域分块
 * 2. 对于每个新元素，统计前面比它小和比它大的元素个数
 * 3. 计算插入代价：min(小于当前元素的个数, 大于当前元素的个数)
 * 4. 使用分块优化统计效率
 * 
 * 工程化考量：
 * - 值域分块处理大规模数据
 * - 动态维护块统计信息
 * - 处理大数值范围
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
    int createSortedArray(vector<int>& instructions) {
        int n = instructions.size();
        if (n == 0) return 0;
        
        // 找到最小值和最大值
        int min_val = *min_element(instructions.begin(), instructions.end());
        int max_val = *max_element(instructions.begin(), instructions.end());
        
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
        
        int cost = 0;
        const int MOD = 1e9 + 7;
        
        // 处理每个指令
        for (int num : instructions) {
            int val_index = num - min_val;
            int block_idx = val_index / block_size;
            int pos_in_block = val_index % block_size;
            
            // 统计比当前元素小的元素个数
            int less_count = 0;
            for (int j = 0; j < block_idx; j++) {
                less_count += block_sum[j];
            }
            for (int j = 0; j < pos_in_block; j++) {
                less_count += blocks[block_idx][j];
            }
            
            // 统计比当前元素大的元素个数
            int greater_count = 0;
            for (int j = block_idx + 1; j < block_count; j++) {
                greater_count += block_sum[j];
            }
            for (int j = pos_in_block + 1; j < block_size; j++) {
                greater_count += blocks[block_idx][j];
            }
            
            // 计算插入代价
            cost = (cost + min(less_count, greater_count)) % MOD;
            
            // 更新统计信息
            blocks[block_idx][pos_in_block]++;
            block_sum[block_idx]++;
        }
        
        return cost;
    }
};

int main() {
    Solution sol;
    
    // 测试用例1
    vector<int> instructions1 = {1, 5, 6, 2};
    cout << "测试用例1: [1, 5, 6, 2]" << endl;
    cout << "创建有序数组的代价: " << sol.createSortedArray(instructions1) 
         << " (期望: 1)" << endl;
    
    // 测试用例2
    vector<int> instructions2 = {1, 2, 3, 6, 5, 4};
    cout << "测试用例2: [1, 2, 3, 6, 5, 4]" << endl;
    cout << "创建有序数组的代价: " << sol.createSortedArray(instructions2) 
         << " (期望: 3)" << endl;
    
    // 测试用例3
    vector<int> instructions3 = {1, 3, 3, 3, 2, 4, 2, 1, 2};
    cout << "测试用例3: [1, 3, 3, 3, 2, 4, 2, 1, 2]" << endl;
    cout << "创建有序数组的代价: " << sol.createSortedArray(instructions3) 
         << " (期望: 4)" << endl;
    
    return 0;
}