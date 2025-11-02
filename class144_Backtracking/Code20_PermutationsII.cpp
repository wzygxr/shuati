#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

/**
 * LeetCode 47. 全排列 II
 * 
 * 题目描述：
 * 给定一个可包含重复数字的序列 nums ，按任意顺序返回所有不重复的全排列。
 * 
 * 示例：
 * 输入：nums = [1,1,2]
 * 输出：[[1,1,2],[1,2,1],[2,1,1]]
 * 
 * 输入：nums = [1,2,3]
 * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 * 
 * 提示：
 * 1 <= nums.length <= 8
 * -10 <= nums[i] <= 10
 * 
 * 链接：https://leetcode.cn/problems/permutations-ii/
 * 
 * 算法思路：
 * 1. 先对数组进行排序，使相同元素相邻
 * 2. 使用回溯算法生成所有排列
 * 3. 使用布尔数组标记已使用的元素
 * 4. 对于重复元素，确保相同元素的相对顺序，避免生成重复排列
 * 
 * 时间复杂度：O(n * n!)，其中n是数组长度，共有n!个排列，每个排列需要O(n)时间复制
 * 空间复杂度：O(n)，递归栈深度和存储路径的空间
 */
class Solution {
public:
    vector<vector<int>> permuteUnique(vector<int>& nums) {
        vector<vector<int>> result;
        vector<int> path;
        vector<bool> used(nums.size(), false);
        sort(nums.begin(), nums.end());
        backtrack(nums, used, path, result);
        return result;
    }

private:
    void backtrack(vector<int>& nums, vector<bool>& used, vector<int>& path, vector<vector<int>>& result) {
        // 终止条件：排列长度等于数组长度
        if (path.size() == nums.size()) {
            result.push_back(path);
            return;
        }
        
        for (int i = 0; i < nums.size(); i++) {
            // 跳过已使用的元素
            if (used[i]) {
                continue;
            }
            
            // 去重关键：如果当前元素与前一个相同，且前一个元素未被使用，则跳过
            // 这样可以确保相同元素的相对顺序，避免生成重复排列
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                continue;
            }
            
            used[i] = true;
            path.push_back(nums[i]);
            backtrack(nums, used, path, result);
            path.pop_back();
            used[i] = false;
        }
    }
};

// 测试函数
void testPermutationsII() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 1, 2};
    vector<vector<int>> result1 = solution.permuteUnique(nums1);
    cout << "输入: nums = [1, 1, 2]" << endl;
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
    vector<int> nums2 = {1, 2, 3};
    vector<vector<int>> result2 = solution.permuteUnique(nums2);
    cout << "\n输入: nums = [1, 2, 3]" << endl;
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
    testPermutationsII();
    return 0;
}