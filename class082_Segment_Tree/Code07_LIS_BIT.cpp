#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_map>

using namespace std;

// 最长递增子序列（LIS）问题的树状数组解法
// 
// 问题描述：
// 给定一个无序的整数数组，找到其中最长上升子序列的长度。
// 
// 示例：
// 输入: [10,9,2,5,3,7,101,18]
// 输出: 4 
// 解释: 最长的上升子序列是 [2,3,7,101]，长度是 4
// 
// 解题思路：
// 使用树状数组优化动态规划解法，将时间复杂度从O(n²)优化到O(n log n)
// 
// 1. 离散化处理：将原数组元素映射到较小的范围内（压缩值域）
// 2. 利用树状数组维护以每个值结尾的最长递增子序列长度
// 3. 对于每个元素num[i]，查询比它小的元素中最大的LIS值，然后更新当前元素的LIS值
// 
// 时间复杂度分析：
// - 离散化处理：O(n log n)
// - 每个元素查询和更新操作：O(log n)
// - 总时间复杂度：O(n log n)
// 
// 空间复杂度分析：
// - 树状数组空间：O(n)
// - 离散化数组空间：O(n)
// - 总空间复杂度：O(n)
// 
// LeetCode 300. 最长递增子序列
// 链接：https://leetcode.cn/problems/longest-increasing-subsequence/

class Code07_LIS_BIT {
private:
    // 树状数组类，用于最长递增子序列问题
    class FenwickTree {
    private:
        vector<int> tree;
        int n;
        
        // 计算lowbit值，即x的二进制表示中最低位1所对应的值
        int lowbit(int x) {
            return x & (-x);
        }
    
    public:
        // 构造函数
        FenwickTree(int size) : n(size), tree(size + 1, 0) {}
        
        // 查询[1, index]区间内的最大值
        int query(int index) {
            int max_val = 0;
            while (index > 0) {
                max_val = max(max_val, tree[index]);
                index -= lowbit(index);
            }
            return max_val;
        }
        
        // 将索引index的值更新为value（保留最大值）
        void update(int index, int value) {
            while (index <= n) {
                tree[index] = max(tree[index], value);
                index += lowbit(index);
            }
        }
    };
    
    // 树状数组类，用于最长递增子序列个数问题
    class FenwickTreeForCount {
    private:
        vector<int> tree_len;   // 维护最长递增子序列的长度
        vector<int> tree_count; // 维护最长递增子序列的路径数
        int size;
        
        // 计算lowbit值
        int lowbit(int x) {
            return x & (-x);
        }
    
    public:
        // 构造函数
        FenwickTreeForCount(int s) : size(s) {
            tree_len.resize(size + 1, 0);
            tree_count.resize(size + 1, 0);
        }
        
        // 更新树状数组
        void update(int index, int length, int count) {
            while (index <= size) {
                if (tree_len[index] < length) {
                    tree_len[index] = length;
                    tree_count[index] = count;
                } else if (tree_len[index] == length) {
                    tree_count[index] += count;
                }
                index += lowbit(index);
            }
        }
        
        // 查询[1, index]区间内的最长递增子序列长度和对应路径数
        pair<int, int> query(int index) {
            int max_length = 0;
            int total_count = 0;
            
            while (index > 0) {
                if (tree_len[index] > max_length) {
                    max_length = tree_len[index];
                    total_count = tree_count[index];
                } else if (tree_len[index] == max_length) {
                    total_count += tree_count[index];
                }
                index -= lowbit(index);
            }
            
            return {max_length, total_count};
        }
    };
    
    // 二分查找辅助方法，查找target在排序数组中的位置
    int binarySearch(vector<int>& sorted_arr, int target) {
        int left = 0, right = sorted_arr.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted_arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

public:
    // 计算最长递增子序列的长度
    int lengthOfLIS(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        
        int n = nums.size();
        
        // 离散化处理
        vector<int> sorted_nums(nums.begin(), nums.end());
        sort(sorted_nums.begin(), sorted_nums.end());
        sorted_nums.erase(unique(sorted_nums.begin(), sorted_nums.end()), sorted_nums.end());
        
        unordered_map<int, int> rank_dict;
        for (int i = 0; i < sorted_nums.size(); i++) {
            rank_dict[sorted_nums[i]] = i + 1; // 排名从1开始
        }
        
        // 初始化树状数组
        FenwickTree bit(sorted_nums.size());
        int max_lis = 0;
        
        // 从左到右遍历数组
        for (int num : nums) {
            // 获取当前元素的排名
            int rank = rank_dict[num];
            // 查询比当前元素小的最大LIS长度
            int current_lis = bit.query(rank - 1) + 1;
            // 更新以当前元素结尾的LIS长度
            bit.update(rank, current_lis);
            // 更新全局最大LIS长度
            max_lis = max(max_lis, current_lis);
        }
        
        return max_lis;
    }
    
    // ==================== 补充题目：LeetCode 307. 区域和检索 - 数组可修改 ====================
    // LeetCode 307. 区域和检索 - 数组可修改
    // 链接：https://leetcode.cn/problems/range-sum-query-mutable/
    // 题目：给你一个数组 nums ，请你完成两类查询。
    // 1. 更新数组 nums 下标 i 处的值
    // 2. 计算数组 nums 中从下标 left 到下标 right 的元素和
    
    class NumArray {
    private:
        vector<int> nums;
        vector<int> tree; // 树状数组，索引从1开始
        int n;
        
        // 计算lowbit值
        int lowbit(int x) {
            return x & (-x);
        }
        
        // 更新树状数组中index位置的值，加上delta
        void updateTree(int index, int delta) {
            while (index <= n) {
                tree[index] += delta;
                index += lowbit(index);
            }
        }
        
        // 查询树状数组中[1, index]的前缀和
        int queryTree(int index) {
            int result = 0;
            while (index > 0) {
                result += tree[index];
                index -= lowbit(index);
            }
            return result;
        }
    
    public:
        // 构造函数
        NumArray(vector<int>& nums) : nums(nums), n(nums.size()), tree(n + 1, 0) {
            // 初始化树状数组
            for (int i = 0; i < n; i++) {
                updateTree(i + 1, nums[i]);
            }
        }
        
        // 更新数组中index位置的值为val
        void update(int index, int val) {
            int delta = val - nums[index];
            nums[index] = val;
            updateTree(index + 1, delta); // 树状数组索引从1开始
        }
        
        // 计算区间[left, right]的元素和
        int sumRange(int left, int right) {
            return queryTree(right + 1) - queryTree(left);
        }
    };
    
    // ==================== 补充题目：LeetCode 673. 最长递增子序列的个数 ====================
    // LeetCode 673. 最长递增子序列的个数
    // 链接：https://leetcode.cn/problems/number-of-longest-increasing-subsequence/
    // 题目：给定一个未排序的整数数组，找到最长递增子序列的个数。
    
    int findNumberOfLIS(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        
        int n = nums.size();
        
        // 离散化处理
        vector<int> sorted_nums(nums.begin(), nums.end());
        sort(sorted_nums.begin(), sorted_nums.end());
        sorted_nums.erase(unique(sorted_nums.begin(), sorted_nums.end()), sorted_nums.end());
        
        unordered_map<int, int> rank_dict;
        for (int i = 0; i < sorted_nums.size(); i++) {
            rank_dict[sorted_nums[i]] = i + 1; // 排名从1开始
        }
        
        // 初始化树状数组
        FenwickTreeForCount bit(sorted_nums.size());
        
        for (int num : nums) {
            int rank = rank_dict[num];
            // 查询比当前元素小的最大LIS长度和路径数
            auto [max_len, path_count] = bit.query(rank - 1);
            
            // 如果没有找到比当前元素小的元素，则当前元素自身构成一个长度为1的子序列
            int current_len = max_len + 1;
            int current_count = (path_count > 0) ? path_count : 1;
            
            // 更新树状数组
            bit.update(rank, current_len, current_count);
        }
        
        // 查询整个数组的最长递增子序列长度和路径数
        auto [max_len, total_count] = bit.query(sorted_nums.size());
        return total_count;
    }
};

// 测试函数
void testSolution() {
    Code07_LIS_BIT solution;
    
    // 测试最长递增子序列
    cout << "最长递增子序列测试用例1:" << endl;
    vector<int> nums1 = {10, 9, 2, 5, 3, 7, 101, 18};
    cout << "输入: [10,9,2,5,3,7,101,18]" << endl;
    cout << "输出: " << solution.lengthOfLIS(nums1) << endl;
    cout << "期望: 4" << endl << endl;
    
    cout << "最长递增子序列测试用例2:" << endl;
    vector<int> nums2 = {0, 1, 0, 3, 2, 3};
    cout << "输入: [0,1,0,3,2,3]" << endl;
    cout << "输出: " << solution.lengthOfLIS(nums2) << endl;
    cout << "期望: 4" << endl << endl;
    
    cout << "最长递增子序列测试用例3:" << endl;
    vector<int> nums3 = {7, 7, 7, 7, 7, 7, 7};
    cout << "输入: [7,7,7,7,7,7,7]" << endl;
    cout << "输出: " << solution.lengthOfLIS(nums3) << endl;
    cout << "期望: 1" << endl << endl;
    
    // 测试最长递增子序列的个数
    cout << "最长递增子序列的个数测试用例:" << endl;
    vector<int> nums4 = {1, 3, 5, 4, 7};
    cout << "输入: [1,3,5,4,7]" << endl;
    cout << "输出: " << solution.findNumberOfLIS(nums4) << endl;
    cout << "期望: 2" << endl;
    
    // 测试NumArray类
    cout << endl << "区域和检索测试用例:" << endl;
    vector<int> nums5 = {1, 3, 5};
    Code07_LIS_BIT::NumArray numArray(nums5);
    cout << "sumRange(0, 2) = " << numArray.sumRange(0, 2) << " (期望: 9)" << endl;
    numArray.update(1, 2);
    cout << "sumRange(0, 2) after update(1, 2) = " << numArray.sumRange(0, 2) << " (期望: 8)" << endl;
}

int main() {
    testSolution();
    return 0;
}
