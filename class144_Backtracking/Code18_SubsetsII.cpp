#include <iostream>
#include <vector>
#include <algorithm>
#include <string>

using namespace std;

/**
 * LeetCode 90. 子集 II
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）。
 * 解集不能包含重复的子集。返回的解集中，子集可以按任意顺序排列。
 * 
 * 示例：
 * 输入：nums = [1,2,2]
 * 输出：[[],[1],[1,2],[1,2,2],[2],[2,2]]
 * 
 * 输入：nums = [0]
 * 输出：[[],[0]]
 * 
 * 提示：
 * 1 <= nums.length <= 10
 * -10 <= nums[i] <= 10
 * 
 * 链接：https://leetcode.cn/problems/subsets-ii/
 * 
 * 算法思路：
 * 1. 先对数组进行排序，使相同元素相邻
 * 2. 使用回溯算法生成所有子集
 * 3. 在回溯过程中，对于重复元素，只选择第一个出现的，跳过后续相同的元素
 * 4. 这样可以避免生成重复的子集
 * 
 * 时间复杂度：O(n * 2^n)，其中n是数组长度，共有2^n个子集，每个子集需要O(n)时间复制
 * 空间复杂度：O(n)，递归栈深度和存储路径的空间
 */
class Solution {
public:
    vector<vector<int>> subsetsWithDup(vector<int>& nums) {
        vector<vector<int>> result;
        vector<int> path;
        sort(nums.begin(), nums.end());
        backtrack(nums, 0, path, result);
        return result;
    }

private:
    void backtrack(vector<int>& nums, int start, vector<int>& path, vector<vector<int>>& result) {
        // 每一步都添加到结果中
        result.push_back(path);
        
        // 从start开始遍历，避免重复
        for (int i = start; i < nums.size(); i++) {
            // 跳过重复元素：如果当前元素与前一个相同且不是第一个出现的，则跳过
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }
            
            path.push_back(nums[i]);  // 选择当前元素
            backtrack(nums, i + 1, path, result);  // 递归处理下一个元素
            path.pop_back();  // 撤销选择
        }
    }
};

// 测试函数
void testSubsetsII() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 2, 2};
    vector<vector<int>> result1 = solution.subsetsWithDup(nums1);
    cout << "输入: nums = [1, 2, 2]" << endl;
    cout << "输出: [";
    for (int i = 0; i < result1.size(); i++) {
        cout << "[";
        for (int j = 0; j < result1[i].size(); j++) {
            cout << result1[i][j];
            if (j < result1[i].size() - 1) cout << ",";
        }
        cout << "]";
        if (i < result1.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    // 测试用例2
    vector<int> nums2 = {0};
    vector<vector<int>> result2 = solution.subsetsWithDup(nums2);
    cout << "\n输入: nums = [0]" << endl;
    cout << "输出: [";
    for (int i = 0; i < result2.size(); i++) {
        cout << "[";
        for (int j = 0; j < result2[i].size(); j++) {
            cout << result2[i][j];
            if (j < result2[i].size() - 1) cout << ",";
        }
        cout << "]";
        if (i < result2.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
}

int main() {
    testSubsetsII();
    return 0;
}