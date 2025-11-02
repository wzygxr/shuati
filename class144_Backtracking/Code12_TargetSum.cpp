#include <vector>
#include <iostream>

using namespace std;

/**
 * LeetCode 494. 目标和
 * 
 * 题目描述：
 * 给你一个非负整数数组 nums 和一个整数 target 。
 * 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个表达式 。
 * 返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。
 * 
 * 示例：
 * 输入：nums = [1,1,1,1,1], target = 3
 * 输出：5
 * 
 * 输入：nums = [1], target = 1
 * 输出：1
 * 
 * 提示：
 * 1 <= nums.length <= 20
 * 0 <= nums[i] <= 1000
 * 0 <= sum(nums[i]) <= 1000
 * -1000 <= target <= 1000
 * 
 * 链接：https://leetcode.cn/problems/target-sum/
 */

class Solution {
public:
    /**
     * 计算目标和的表达式数目（回溯算法）
     * 
     * 算法思路：
     * 1. 使用回溯算法遍历所有可能的符号组合
     * 2. 对于每个数字，有两种选择：加号或减号
     * 3. 递归处理下一个数字
     * 4. 当处理完所有数字时，检查结果是否等于目标值
     * 
     * 时间复杂度：O(2^n)
     * 空间复杂度：O(n)
     * 
     * @param nums 数组
     * @param target 目标值
     * @return 表达式数目
     */
    int findTargetSumWays(vector<int>& nums, int target) {
        return backtrack(nums, target, 0, 0);
    }

private:
    /**
     * 回溯函数计算目标和的表达式数目
     * 
     * @param nums 数组
     * @param target 目标值
     * @param index 当前处理的索引
     * @param sum 当前和
     * @return 表达式数目
     */
    int backtrack(vector<int>& nums, int target, int index, int sum) {
        // 终止条件：已处理完所有数字
        if (index == nums.size()) {
            return sum == target ? 1 : 0;
        }
        
        // 选择加号
        int add = backtrack(nums, target, index + 1, sum + nums[index]);
        // 选择减号
        int subtract = backtrack(nums, target, index + 1, sum - nums[index]);
        
        return add + subtract;
    }
};

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 1, 1, 1, 1};
    int target1 = 3;
    int result1 = solution.findTargetSumWays(nums1, target1);
    cout << "输入: nums = [1,1,1,1,1], target = " << target1 << endl;
    cout << "输出: " << result1 << endl;
    
    // 测试用例2
    vector<int> nums2 = {1};
    int target2 = 1;
    int result2 = solution.findTargetSumWays(nums2, target2);
    cout << "\n输入: nums = [1], target = " << target2 << endl;
    cout << "输出: " << result2 << endl;
    
    // 测试用例3
    vector<int> nums3 = {1, 0};
    int target3 = 1;
    int result3 = solution.findTargetSumWays(nums3, target3);
    cout << "\n输入: nums = [1,0], target = " << target3 << endl;
    cout << "输出: " << result3 << endl;
    
    return 0;
}