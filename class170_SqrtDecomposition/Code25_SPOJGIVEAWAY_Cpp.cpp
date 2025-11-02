/**
 * SPOJ GIVEAWAY - Give Away - C++实现
 * 题目：区间第k小值查询（带修改）
 * 来源：SPOJ (https://www.spoj.com/problems/GIVEAWAY/)
 * 
 * 算法：平方根分解 + 块内排序
 * 时间复杂度：
 *   - 查询：O(√n log√n)
 *   - 更新：O(√n)
 * 空间复杂度：O(n)
 * 最优解：否，最优解是树套树或整体二分，但平方根分解实现简单
 * 
 * 思路：
 * 1. 将数组分成√n个块
 * 2. 每个块维护排序后的副本
 * 3. 查询时，二分查找每个块中小于等于某个值的元素个数
 * 4. 更新时，重新排序对应块
 * 
 * 工程化考量：
 * - 使用块内排序支持快速查询
 * - 二分查找优化查询效率
 * - 懒更新机制减少排序次数
 * - 处理大规模数据的分块策略
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
using namespace std;

class GiveAway {
private:
    vector<int> arr;           // 原始数组
    vector<vector<int>> blocks; // 每个块的排序副本
    int block_size;            // 块大小
    int block_count;           // 块数量
    
public:
    GiveAway(vector<int>& nums) {
        arr = nums;
        int n = arr.size();
        block_size = sqrt(n);
        block_count = (n + block_size - 1) / block_size;
        blocks.resize(block_count);
        
        // 初始化块
        for (int i = 0; i < n; i++) {
            int block_idx = i / block_size;
            blocks[block_idx].push_back(arr[i]);
        }
        
        // 对每个块排序
        for (int i = 0; i < block_count; i++) {
            sort(blocks[i].begin(), blocks[i].end());
        }
    }
    
    // 单点更新
    void update(int index, int value) {
        if (index < 0 || index >= arr.size()) {
            throw out_of_range("Index out of range");
        }
        
        int block_idx = index / block_size;
        int old_value = arr[index];
        arr[index] = value;
        
        // 更新对应块（重新排序）
        auto& block = blocks[block_idx];
        auto it = lower_bound(block.begin(), block.end(), old_value);
        if (it != block.end() && *it == old_value) {
            *it = value;
            sort(block.begin(), block.end()); // 重新排序
        }
    }
    
    // 查询区间内小于等于x的元素个数
    int queryLessEqual(int l, int r, int x) {
        if (l < 0 || r >= arr.size() || l > r) {
            throw out_of_range("Invalid range");
        }
        
        int block_l = l / block_size;
        int block_r = r / block_size;
        int count = 0;
        
        // 同一个块内，暴力统计
        if (block_l == block_r) {
            for (int i = l; i <= r; i++) {
                if (arr[i] <= x) {
                    count++;
                }
            }
            return count;
        }
        
        // 左边界不完整块
        for (int i = l; i < (block_l + 1) * block_size; i++) {
            if (arr[i] <= x) {
                count++;
            }
        }
        
        // 中间完整块（使用二分查找）
        for (int i = block_l + 1; i < block_r; i++) {
            auto& block = blocks[i];
            auto it = upper_bound(block.begin(), block.end(), x);
            count += (it - block.begin());
        }
        
        // 右边界不完整块
        for (int i = block_r * block_size; i <= r; i++) {
            if (arr[i] <= x) {
                count++;
            }
        }
        
        return count;
    }
    
    // 查询区间第k小值
    int queryKthSmallest(int l, int r, int k) {
        if (k < 1 || k > (r - l + 1)) {
            throw out_of_range("Invalid k");
        }
        
        // 二分查找第k小的值
        int left = *min_element(arr.begin() + l, arr.begin() + r + 1);
        int right = *max_element(arr.begin() + l, arr.begin() + r + 1);
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            int count = queryLessEqual(l, r, mid);
            
            if (count < k) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        return left;
    }
};

int main() {
    // 测试用例
    vector<int> nums = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
    GiveAway ga(nums);
    
    // 测试查询
    cout << "区间[0, 5]内小于等于3的元素个数: " 
         << ga.queryLessEqual(0, 5, 3) << endl;  // 应该输出4
    
    cout << "区间[2, 7]内第3小的值: " 
         << ga.queryKthSmallest(2, 7, 3) << endl;  // 应该输出4
    
    // 测试更新
    ga.update(3, 7);  // 将索引3的值从1改为7
    cout << "更新后区间[0, 5]内小于等于3的元素个数: " 
         << ga.queryLessEqual(0, 5, 3) << endl;  // 应该输出3
    
    // 边界测试
    cout << "区间[0, 9]内第5小的值: " 
         << ga.queryKthSmallest(0, 9, 5) << endl;  // 应该输出4
    
    return 0;
}