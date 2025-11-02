/**
 * LibreOJ #6277 数列分块入门1 - C++实现
 * 题目：区间加法，单点查询
 * 来源：LibreOJ (https://loj.ac/p/6277)
 * 
 * 算法：平方根分解（分块算法）
 * 时间复杂度：O(n√n)
 * 空间复杂度：O(n)
 * 最优解：是，对于这种简单的区间更新单点查询，平方根分解是最优解之一
 * 
 * 思路：
 * 1. 将数组分成√n个块，每个块大小为block_size
 * 2. 维护每个块的加法标记（懒标记）
 * 3. 区间更新时，完整块直接更新标记，不完整块暴力更新
 * 4. 单点查询时，返回原值加上所在块的标记
 * 
 * 工程化考量：
 * - 使用懒标记减少不必要的更新操作
 * - 块大小选择√n，平衡查询和更新效率
 * - 边界处理：正确处理区间边界和块边界
 */

#include <iostream>
#include <vector>
#include <cmath>
using namespace std;

class BlockArray {
private:
    vector<int> arr;        // 原始数组
    vector<int> block_add;  // 每个块的加法标记
    int block_size;         // 块大小
    int block_count;        // 块数量

public:
    BlockArray(vector<int>& nums) {
        arr = nums;
        int n = arr.size();
        block_size = sqrt(n);
        block_count = (n + block_size - 1) / block_size;
        block_add.resize(block_count, 0);
    }
    
    // 区间加法操作
    void rangeAdd(int l, int r, int val) {
        int block_l = l / block_size;
        int block_r = r / block_size;
        
        // 同一个块内，直接暴力更新
        if (block_l == block_r) {
            for (int i = l; i <= r; i++) {
                arr[i] += val;
            }
            return;
        }
        
        // 更新左边界不完整块
        for (int i = l; i < (block_l + 1) * block_size; i++) {
            arr[i] += val;
        }
        
        // 更新中间完整块
        for (int i = block_l + 1; i < block_r; i++) {
            block_add[i] += val;
        }
        
        // 更新右边界不完整块
        for (int i = block_r * block_size; i <= r; i++) {
            arr[i] += val;
        }
    }
    
    // 单点查询
    int pointQuery(int index) {
        int block_idx = index / block_size;
        return arr[index] + block_add[block_idx];
    }
};

int main() {
    // 测试用例
    vector<int> nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    BlockArray ba(nums);
    
    // 测试区间加法
    ba.rangeAdd(2, 7, 5);  // 给索引2-7的元素加5
    
    // 测试单点查询
    cout << "索引3的值: " << ba.pointQuery(3) << endl;  // 应该输出9 (4+5)
    cout << "索引8的值: " << ba.pointQuery(8) << endl;  // 应该输出9 (9+0)
    
    // 边界测试
    ba.rangeAdd(0, 9, 10);  // 给所有元素加10
    cout << "索引0的值: " << ba.pointQuery(0) << endl;  // 应该输出11 (1+10)
    cout << "索引9的值: " << ba.pointQuery(9) << endl;  // 应该输出20 (10+10)
    
    return 0;
}