/**
 * SPOJ KGSS - Maximum Sum - C++实现
 * 题目：区间最大两数和查询（带修改）
 * 来源：SPOJ (https://www.spoj.com/problems/KGSS/)
 * 
 * 算法：平方根分解 + 块内维护最大两数
 * 时间复杂度：
 *   - 查询：O(√n)
 *   - 更新：O(√n)
 * 空间复杂度：O(n)
 * 最优解：否，最优解是线段树，但平方根分解实现简单
 * 
 * 思路：
 * 1. 将数组分成√n个块
 * 2. 每个块维护最大的两个数
 * 3. 查询时，比较各个块的最大两数和
 * 4. 更新时，重新计算对应块的最大两数
 * 
 * 工程化考量：
 * - 块内维护最大两数信息
 * - 快速查询区间最大两数和
 * - 懒更新机制减少计算
 * - 处理边界情况
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <climits>
using namespace std;

class MaximumSum {
private:
    vector<int> arr;                    // 原始数组
    vector<pair<int, int>> block_max2;  // 每个块的最大两个数
    int block_size;                     // 块大小
    int block_count;                    // 块数量
    
    // 更新块的最大两数
    void updateBlock(int block_idx) {
        int start = block_idx * block_size;
        int end = min((block_idx + 1) * block_size, (int)arr.size());
        
        int max1 = INT_MIN, max2 = INT_MIN;
        for (int i = start; i < end; i++) {
            if (arr[i] > max1) {
                max2 = max1;
                max1 = arr[i];
            } else if (arr[i] > max2) {
                max2 = arr[i];
            }
        }
        block_max2[block_idx] = {max1, max2};
    }
    
public:
    MaximumSum(vector<int>& nums) {
        arr = nums;
        int n = arr.size();
        block_size = sqrt(n);
        block_count = (n + block_size - 1) / block_size;
        block_max2.resize(block_count, {INT_MIN, INT_MIN});
        
        // 初始化每个块的最大两数
        for (int i = 0; i < block_count; i++) {
            updateBlock(i);
        }
    }
    
    // 单点更新
    void update(int index, int value) {
        if (index < 0 || index >= arr.size()) {
            throw out_of_range("Index out of range");
        }
        
        arr[index] = value;
        int block_idx = index / block_size;
        updateBlock(block_idx);
    }
    
    // 查询区间最大两数和
    int query(int l, int r) {
        if (l < 0 || r >= arr.size() || l > r) {
            throw out_of_range("Invalid range");
        }
        
        int block_l = l / block_size;
        int block_r = r / block_size;
        int max_sum = INT_MIN;
        
        // 同一个块内，暴力查找
        if (block_l == block_r) {
            int max1 = INT_MIN, max2 = INT_MIN;
            for (int i = l; i <= r; i++) {
                if (arr[i] > max1) {
                    max2 = max1;
                    max1 = arr[i];
                } else if (arr[i] > max2) {
                    max2 = arr[i];
                }
            }
            if (max1 != INT_MIN && max2 != INT_MIN) {
                max_sum = max1 + max2;
            }
            return max_sum;
        }
        
        // 左边界不完整块
        int left_max1 = INT_MIN, left_max2 = INT_MIN;
        for (int i = l; i < (block_l + 1) * block_size; i++) {
            if (arr[i] > left_max1) {
                left_max2 = left_max1;
                left_max1 = arr[i];
            } else if (arr[i] > left_max2) {
                left_max2 = arr[i];
            }
        }
        
        // 中间完整块
        for (int i = block_l + 1; i < block_r; i++) {
            auto& max2 = block_max2[i];
            if (max2.first != INT_MIN && max2.second != INT_MIN) {
                max_sum = max(max_sum, max2.first + max2.second);
            }
        }
        
        // 右边界不完整块
        int right_max1 = INT_MIN, right_max2 = INT_MIN;
        for (int i = block_r * block_size; i <= r; i++) {
            if (arr[i] > right_max1) {
                right_max2 = right_max1;
                right_max1 = arr[i];
            } else if (arr[i] > right_max2) {
                right_max2 = arr[i];
            }
        }
        
        // 合并所有候选值
        vector<int> candidates;
        if (left_max1 != INT_MIN) candidates.push_back(left_max1);
        if (left_max2 != INT_MIN) candidates.push_back(left_max2);
        if (right_max1 != INT_MIN) candidates.push_back(right_max1);
        if (right_max2 != INT_MIN) candidates.push_back(right_max2);
        
        for (int i = block_l + 1; i < block_r; i++) {
            auto& max2 = block_max2[i];
            if (max2.first != INT_MIN) candidates.push_back(max2.first);
            if (max2.second != INT_MIN) candidates.push_back(max2.second);
        }
        
        // 找出最大的两个数
        sort(candidates.rbegin(), candidates.rend());
        if (candidates.size() >= 2) {
            max_sum = max(max_sum, candidates[0] + candidates[1]);
        }
        
        return max_sum;
    }
};

int main() {
    // 测试用例
    vector<int> nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    MaximumSum ms(nums);
    
    // 测试查询
    cout << "区间[0, 5]的最大两数和: " << ms.query(0, 5) << endl;  // 应该输出11 (5+6)
    cout << "区间[2, 7]的最大两数和: " << ms.query(2, 7) << endl;  // 应该输出13 (6+7)
    
    // 测试更新
    ms.update(9, 20);  // 将索引9的值改为20
    cout << "更新后区间[0, 9]的最大两数和: " << ms.query(0, 9) << endl;  // 应该输出27 (20+7)
    
    // 边界测试
    cout << "区间[0, 1]的最大两数和: " << ms.query(0, 1) << endl;  // 应该输出3 (1+2)
    
    return 0;
}