/**
 * LeetCode 315. 计算右侧小于当前元素的个数
 * 题目链接: https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 * 
 * 题目描述:
 * 给定一个整数数组 nums，按要求返回一个新数组 counts。
 * 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例:
 * 输入: [5,2,6,1]
 * 输出: [2,1,1,0]
 * 解释:
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 * 
 * 解题思路:
 * 方法一：线段树 + 离散化
 * 1. 对数组进行离散化处理，将原始数值映射到连续的索引
 * 2. 从右向左遍历数组，使用线段树统计每个数值出现的次数
 * 3. 对于当前元素，查询线段树中比它小的所有数值的总出现次数
 * 4. 将当前元素插入线段树中
 * 
 * 时间复杂度分析:
 * - 离散化: O(n log n)
 * - 线段树操作: O(n log n)
 * - 总时间复杂度: O(n log n)
 * 
 * 空间复杂度分析:
 * - 离散化映射: O(n)
 * - 线段树: O(n)
 * - 总空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 处理空数组和单个元素的情况
 * 2. 边界情况: 处理重复元素和极端值
 * 3. 性能优化: 使用离散化减少线段树大小
 * 4. 可读性: 详细的注释和清晰的代码结构
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_map>

using namespace std;

/**
 * 线段树类 - 用于统计数值出现次数
 */
class SegmentTree {
private:
    vector<int> tree;
    int n;
    
    /**
     * 递归更新实现
     */
    void update(int l, int r, int idx, int pos, int val) {
        if (l == r) {
            tree[idx] += val;
            return;
        }
        
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            update(l, mid, idx << 1, pos, val);
        } else {
            update(mid + 1, r, idx << 1 | 1, pos, val);
        }
        
        tree[idx] = tree[idx << 1] + tree[idx << 1 | 1];
    }
    
    /**
     * 递归查询实现
     */
    int query(int l, int r, int idx, int ql, int qr) {
        if (ql > qr) return 0;
        if (ql <= l && r <= qr) {
            return tree[idx];
        }
        
        int mid = (l + r) >> 1;
        int sum = 0;
        if (ql <= mid) {
            sum += query(l, mid, idx << 1, ql, qr);
        }
        if (qr > mid) {
            sum += query(mid + 1, r, idx << 1 | 1, ql, qr);
        }
        
        return sum;
    }
    
public:
    /**
     * 构造函数
     * @param size 线段树大小
     */
    SegmentTree(int size) {
        this->n = size;
        this->tree = vector<int>(4 * n, 0);
    }
    
    /**
     * 单点更新 - 在位置pos处增加val
     * @param pos 位置
     * @param val 增加值
     */
    void update(int pos, int val) {
        update(0, n - 1, 1, pos, val);
    }
    
    /**
     * 区间查询 - 查询区间[0, pos-1]的和
     * @param pos 位置
     * @return 区间和
     */
    int query(int pos) {
        if (pos <= 0) return 0;
        return query(0, n - 1, 1, 0, pos - 1);
    }
};

/**
 * 主函数 - 计算右侧小于当前元素的个数
 * @param nums 输入数组
 * @return 结果数组
 */
vector<int> countSmaller(vector<int>& nums) {
    // 处理空数组情况
    if (nums.empty()) {
        return {};
    }
    
    int n = nums.size();
    vector<int> result;
    
    // 单个元素情况
    if (n == 1) {
        result.push_back(0);
        return result;
    }
    
    // 离散化处理
    vector<int> sorted = nums;
    sort(sorted.begin(), sorted.end());
    
    unordered_map<int, int> mapping;
    int idx = 0;
    for (int i = 0; i < n; i++) {
        if (mapping.find(sorted[i]) == mapping.end()) {
            mapping[sorted[i]] = idx++;
        }
    }
    
    // 初始化线段树
    SegmentTree segTree(idx);
    
    // 从右向左遍历
    for (int i = n - 1; i >= 0; i--) {
        int pos = mapping[nums[i]];
        // 查询比当前元素小的数量
        int count = segTree.query(pos);
        result.insert(result.begin(), count);
        // 更新当前元素出现次数
        segTree.update(pos, 1);
    }
    
    return result;
}

/**
 * 测试函数
 */
int main() {
    // 测试用例1: 示例输入
    vector<int> nums1 = {5, 2, 6, 1};
    vector<int> result1 = countSmaller(nums1);
    cout << "测试用例1: ";
    for (int num : result1) {
        cout << num << " ";
    }
    cout << endl; // 期望输出: 2 1 1 0
    
    // 测试用例2: 空数组
    vector<int> nums2 = {};
    vector<int> result2 = countSmaller(nums2);
    cout << "测试用例2: ";
    for (int num : result2) {
        cout << num << " ";
    }
    cout << endl; // 期望输出: (空)
    
    // 测试用例3: 单个元素
    vector<int> nums3 = {1};
    vector<int> result3 = countSmaller(nums3);
    cout << "测试用例3: ";
    for (int num : result3) {
        cout << num << " ";
    }
    cout << endl; // 期望输出: 0
    
    // 测试用例4: 重复元素
    vector<int> nums4 = {2, 2, 2, 2};
    vector<int> result4 = countSmaller(nums4);
    cout << "测试用例4: ";
    for (int num : result4) {
        cout << num << " ";
    }
    cout << endl; // 期望输出: 0 0 0 0
    
    // 测试用例5: 递减序列
    vector<int> nums5 = {5, 4, 3, 2, 1};
    vector<int> result5 = countSmaller(nums5);
    cout << "测试用例5: ";
    for (int num : result5) {
        cout << num << " ";
    }
    cout << endl; // 期望输出: 4 3 2 1 0
    
    // 测试用例6: 递增序列
    vector<int> nums6 = {1, 2, 3, 4, 5};
    vector<int> result6 = countSmaller(nums6);
    cout << "测试用例6: ";
    for (int num : result6) {
        cout << num << " ";
    }
    cout << endl; // 期望输出: 0 0 0 0 0
    
    return 0;
}

/**
 * 算法复杂度详细分析:
 * 
 * 时间复杂度:
 * 1. 数组排序: O(n log n)
 * 2. 离散化映射构建: O(n)
 * 3. 线段树操作(更新和查询): O(n log n)
 * 总时间复杂度: O(n log n)
 * 
 * 空间复杂度:
 * 1. 排序数组: O(n)
 * 2. 离散化映射: O(n)
 * 3. 线段树: O(n)
 * 4. 结果数组: O(n)
 * 总空间复杂度: O(n)
 * 
 * 算法优化点:
 * 1. 离散化处理: 将原始数值映射到连续索引，减少线段树大小
 * 2. 从右向左遍历: 避免重复计算，每个元素只处理一次
 * 3. 线段树优化: 使用递归实现，代码清晰易懂
 * 
 * 面试要点:
 * 1. 理解离散化的必要性
 * 2. 掌握线段树的基本操作
 * 3. 能够分析时间空间复杂度
 * 4. 处理边界情况和异常输入
 */