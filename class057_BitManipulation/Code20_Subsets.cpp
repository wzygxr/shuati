#include <vector>
#include <iostream>
using namespace std;

/**
 * 子集
 * 测试链接：https://leetcode.cn/problems/subsets/
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
 * 解集不能包含重复的子集。你可以按任意顺序返回解集。
 * 
 * 解题思路：
 * 使用位运算来生成所有子集。对于长度为n的数组，共有2^n个子集。
 * 每个子集可以用一个n位的二进制数表示，其中第i位为1表示包含nums[i]，为0表示不包含。
 * 
 * 时间复杂度：O(n * 2^n) - 需要生成2^n个子集，每个子集需要O(n)时间构建
 * 空间复杂度：O(n) - 不考虑输出空间
 */
class Solution {
public:
    /**
     * 使用位运算生成所有子集
     * @param nums 输入数组
     * @return 所有子集的向量
     */
    vector<vector<int>> subsets(vector<int>& nums) {
        vector<vector<int>> result;
        int n = nums.size();
        int totalSubsets = 1 << n; // 2^n个子集
        
        // 遍历所有可能的二进制掩码
        for (int mask = 0; mask < totalSubsets; mask++) {
            vector<int> subset;
            
            // 检查每个位，如果为1则添加对应元素
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    subset.push_back(nums[i]);
                }
            }
            result.push_back(subset);
        }
        
        return result;
    }
    
    /**
     * 回溯法实现（备选方案）
     * @param nums 输入数组
     * @return 所有子集的向量
     */
    vector<vector<int>> subsetsBacktrack(vector<int>& nums) {
        vector<vector<int>> result;
        vector<int> current;
        backtrack(nums, 0, current, result);
        return result;
    }
    
private:
    void backtrack(vector<int>& nums, int start, vector<int>& current, vector<vector<int>>& result) {
        result.push_back(current);
        
        for (int i = start; i < nums.size(); i++) {
            current.push_back(nums[i]);
            backtrack(nums, i + 1, current, result);
            current.pop_back();
        }
    }
};

// 测试函数
int main() {
    Solution solution;
    
    vector<int> test1 = {1, 2, 3};
    vector<int> test2 = {0};
    vector<int> test3 = {1, 2};
    
    auto result1 = solution.subsets(test1);
    auto result2 = solution.subsets(test2);
    auto result3 = solution.subsets(test3);
    
    cout << "Test 1 size: " << result1.size() << endl;
    cout << "Test 2 size: " << result2.size() << endl;
    cout << "Test 3 size: " << result3.size() << endl;
    
    return 0;
}