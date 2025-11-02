/**
 * LeetCode 327. 区间和的个数
 * 题目链接: https://leetcode.com/problems/count-of-range-sum/
 * 
 * 题目描述:
 * 给定一个整数数组 nums，返回区间和在 [lower, upper] 范围内的区间个数（包含 lower 和 upper）。
 * 区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。
 * 
 * 示例:
 * 输入: nums = [-2,5,-1], lower = -2, upper = 2
 * 输出: 3
 * 解释: 三个区间分别是: [0,0], [2,2], [0,2]，它们对应的区间和分别是 -2, -1, 2。
 * 
 * 解题思路:
 * 方法一：线段树 + 前缀和 + 离散化
 * 1. 计算前缀和数组 prefixSum
 * 2. 问题转化为：对于每个 j，统计有多少个 i < j 满足 lower ≤ prefixSum[j] - prefixSum[i] ≤ upper
 * 3. 等价于：prefixSum[j] - upper ≤ prefixSum[i] ≤ prefixSum[j] - lower
 * 4. 使用线段树统计前缀和的出现次数
 * 5. 从右向左遍历，动态维护前缀和的出现次数
 * 
 * 时间复杂度分析:
 * - 前缀和计算: O(n)
 * - 离散化: O(n log n)
 * - 线段树操作: O(n log n)
 * - 总时间复杂度: O(n log n)
 * 
 * 空间复杂度分析:
 * - 前缀和数组: O(n)
 * - 离散化映射: O(n)
 * - 线段树: O(n)
 * - 总空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 处理空数组和边界值
 * 2. 数值溢出: 使用long long类型防止整数溢出
 * 3. 边界情况: 处理lower > upper的情况
 * 4. 性能优化: 使用离散化减少线段树大小
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <set>
#include <map>

using namespace std;

/**
 * 线段树类 - 用于统计数值出现次数
 */
class SegmentTree {
private:
    vector<int> tree;
    int n;
    
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
    
    int query(int l, int r, int idx, int ql, int qr) {
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
    SegmentTree(int size) {
        this->n = size;
        this->tree = vector<int>(4 * size, 0);
    }
    
    void update(int pos, int val) {
        update(0, n - 1, 1, pos, val);
    }
    
    int query(int ql, int qr) {
        if (ql > qr) return 0;
        return query(0, n - 1, 1, ql, qr);
    }
};

/**
 * 主函数 - 计算区间和个数
 */
int countRangeSum(vector<int>& nums, int lower, int upper) {
    // 处理空数组情况
    if (nums.empty()) {
        return 0;
    }
    
    // 处理lower > upper的情况
    if (lower > upper) {
        return 0;
    }
    
    int n = nums.size();
    
    // 计算前缀和数组，使用long long防止溢出
    vector<long long> prefixSum(n + 1, 0);
    for (int i = 0; i < n; i++) {
        prefixSum[i + 1] = prefixSum[i] + nums[i];
    }
    
    // 收集所有需要离散化的值
    set<long long> values;
    for (long long sum : prefixSum) {
        values.insert(sum);
        values.insert(sum - lower);
        values.insert(sum - upper);
    }
    
    // 离散化映射
    map<long long, int> mapping;
    int idx = 0;
    for (long long num : values) {
        mapping[num] = idx++;
    }
    
    // 初始化线段树
    SegmentTree segTree(idx);
    
    int count = 0;
    // 从右向左遍历前缀和
    for (int i = n; i >= 0; i--) {
        long long current = prefixSum[i];
        long long leftBound = current - upper;
        long long rightBound = current - lower;
        
        int leftIdx = mapping[leftBound];
        int rightIdx = mapping[rightBound];
        
        // 查询满足条件的区间个数
        count += segTree.query(leftIdx, rightIdx);
        
        // 更新当前前缀和的出现次数
        int currentIdx = mapping[current];
        segTree.update(currentIdx, 1);
    }
    
    return count;
}

/**
 * 测试函数
 */
int main() {
    // 测试用例1: 示例输入
    vector<int> nums1 = {-2, 5, -1};
    int result1 = countRangeSum(nums1, -2, 2);
    cout << "测试用例1: " << result1 << endl; // 期望输出: 3
    
    // 测试用例2: 空数组
    vector<int> nums2 = {};
    int result2 = countRangeSum(nums2, 0, 0);
    cout << "测试用例2: " << result2 << endl; // 期望输出: 0
    
    // 测试用例3: 单个元素
    vector<int> nums3 = {1};
    int result3 = countRangeSum(nums3, 1, 1);
    cout << "测试用例3: " << result3 << endl; // 期望输出: 1
    
    // 测试用例4: 全零数组
    vector<int> nums4 = {0, 0, 0, 0};
    int result4 = countRangeSum(nums4, 0, 0);
    cout << "测试用例4: " << result4 << endl; // 期望输出: 10
    
    // 测试用例5: 边界情况 lower > upper
    vector<int> nums5 = {1, 2, 3};
    int result5 = countRangeSum(nums5, 5, 3);
    cout << "测试用例5: " << result5 << endl; // 期望输出: 0
    
    // 测试用例6: 大数测试
    vector<int> nums6 = {2147483647, -2147483648, 0};
    int result6 = countRangeSum(nums6, -1, 1);
    cout << "测试用例6: " << result6 << endl; // 期望输出: 2
    
    return 0;
}

/**
 * 算法复杂度详细分析:
 * 
 * 时间复杂度:
 * 1. 前缀和计算: O(n)
 * 2. 离散化集合构建: O(n log n)
 * 3. 离散化映射构建: O(n)
 * 4. 线段树操作(更新和查询): O(n log n)
 * 总时间复杂度: O(n log n)
 * 
 * 空间复杂度:
 * 1. 前缀和数组: O(n)
 * 2. 离散化集合: O(n)
 * 3. 离散化映射: O(n)
 * 4. 线段树: O(n)
 * 总空间复杂度: O(n)
 * 
 * 算法优化点:
 * 1. 前缀和技巧: 将区间和问题转化为前缀和之差
 * 2. 离散化处理: 减少线段树的大小
 * 3. 从右向左遍历: 避免重复计算
 * 4. 使用long long类型: 防止整数溢出
 * 
 * 面试要点:
 * 1. 理解前缀和的应用场景
 * 2. 掌握线段树在统计问题中的应用
 * 3. 能够处理数值溢出问题
 * 4. 分析复杂的时间空间复杂度
 */