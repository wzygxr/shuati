#include <vector>
#include <algorithm>
#include <iostream>
using namespace std;

/**
 * 子集II
 * 测试链接：https://leetcode.cn/problems/subsets-ii/
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）。
 * 解集不能包含重复的子集。返回的解集中，子集可以按任意顺序排列。
 * 
 * 解题思路：
 * 由于数组可能包含重复元素，需要先对数组排序，然后使用回溯法，在递归时跳过重复元素。
 * 
 * 时间复杂度：O(n * 2^n) - 最坏情况下需要生成2^n个子集
 * 空间复杂度：O(n) - 递归深度为n
 */
class Solution {
public:
    /**
     * 使用回溯法生成所有不重复子集
     * @param nums 输入数组（可能包含重复元素）
     * @return 所有不重复子集的向量
     */
    vector<vector<int>> subsetsWithDup(vector<int>& nums) {
        vector<vector<int>> result;
        // 先排序，便于跳过重复元素
        sort(nums.begin(), nums.end());
        vector<int> current;
        backtrack(nums, 0, current, result);
        return result;
    }
    
private:
    /**
     * 回溯辅助函数
     * @param nums 排序后的数组
     * @param start 当前起始位置
     * @param current 当前子集
     * @param result 结果向量
     */
    void backtrack(vector<int>& nums, int start, vector<int>& current, vector<vector<int>>& result) {
        // 添加当前子集到结果
        result.push_back(current);
        
        for (int i = start; i < nums.size(); i++) {
            // 跳过重复元素，避免生成重复子集
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }
            
            current.push_back(nums[i]);
            backtrack(nums, i + 1, current, result);
            current.pop_back();
        }
    }
};

// 测试函数
int main() {
    Solution solution;
    
    vector<int> test1 = {1, 2, 2};
    vector<int> test2 = {0};
    vector<int> test3 = {1, 1, 2};
    
    auto result1 = solution.subsetsWithDup(test1);
    auto result2 = solution.subsetsWithDup(test2);
    auto result3 = solution.subsetsWithDup(test3);
    
    cout << "Test 1 size: " << result1.size() << endl;
    cout << "Test 2 size: " << result2.size() << endl;
    cout << "Test 3 size: " << result3.size() << endl;
    
    // 打印第一个测试用例的结果
    cout << "Test 1 subsets:" << endl;
    for (const auto& subset : result1) {
        cout << "[";
        for (int num : subset) {
            cout << num << " ";
        }
        cout << "]" << endl;
    }
    
    return 0;
}