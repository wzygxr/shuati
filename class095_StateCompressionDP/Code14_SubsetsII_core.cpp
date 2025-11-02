// 子集 II (Subsets II)
// 给你一个整数数组 nums ，其中可能包含重复元素，
// 请你返回该数组所有可能的子集（幂集）。
// 解集不能包含重复的子集。返回的解集中，子集可以按任意顺序排列。
// 测试链接 : https://leetcode.cn/problems/subsets-ii/

#include <vector>
#include <algorithm>
#include <set>
using namespace std;

class Solution {
public:
    // 使用状态压缩解决子集II问题
    // 核心思想：用二进制位表示元素选择状态，通过位运算生成所有子集
    // 时间复杂度: O(n * 2^n)
    // 空间复杂度: O(n)
    vector<vector<int>> subsetsWithDup(vector<int>& nums) {
        // 先对数组排序，便于处理重复元素
        sort(nums.begin(), nums.end());
        
        int n = nums.size();
        // 用于存储所有不重复的子集
        set<vector<int>> result_set;
        
        // 枚举所有可能的状态（0到2^n-1）
        for (int mask = 0; mask < (1 << n); mask++) {
            vector<int> subset;
            // 根据mask生成对应的子集
            for (int i = 0; i < n; i++) {
                // 如果第i位为1，表示选择第i个元素
                if (mask & (1 << i)) {
                    subset.push_back(nums[i]);
                }
            }
            // 将子集添加到集合中（自动去重）
            result_set.insert(subset);
        }
        
        // 将Set转换为Vector并返回
        return vector<vector<int>>(result_set.begin(), result_set.end());
    }
};