#include <iostream>
#include <vector>
using namespace std;

/**
 * 寻找数组中心索引 (Find Pivot Index)
 * 
 * 题目描述:
 * 给你一个整数数组 nums，请编写一个能够返回数组 "中心索引" 的方法。
 * 中心索引是数组的一个索引，其左侧所有元素相加的和等于右侧所有元素相加的和。
 * 如果数组不存在中心索引，返回 -1。如果数组有多个中心索引，应该返回最靠近左边的那一个。
 * 
 * 注意：中心索引可能出现在数组的两端。
 * 
 * 示例:
 * 输入：nums = [1, 7, 3, 6, 5, 6]
 * 输出：3
 * 解释：
 * 索引 3 (nums[3] = 6) 的左侧数之和 (1+7+3 = 11)，右侧数之和 (5+6 = 11)，二者相等。
 * 
 * 输入：nums = [1, 2, 3]
 * 输出：-1
 * 解释：数组中不存在满足此条件的中心索引。
 * 
 * 输入：nums = [2, 1, -1]
 * 输出：0
 * 解释：
 * 索引 0 的左侧不存在元素，视作和为 0；右侧数之和为 1 + (-1) = 0，二者相等。
 * 
 * 提示:
 * nums 的长度范围为 [0, 10000]。
 * 任何一个 nums[i] 将会是一个范围在 [-1000, 1000]的整数。
 * 
 * 题目链接: https://leetcode.com/problems/find-pivot-index/
 * 
 * 解题思路:
 * 使用前缀和的思想，计算整个数组的总和，然后遍历数组，维护左侧元素的和，
 * 当左侧和等于总和减去左侧和减去当前元素时，找到中心索引。
 * 
 * 时间复杂度: O(n) - 需要遍历数组两次
 * 空间复杂度: O(1) - 只使用常数额外空间
 */

class Solution {
public:
    /**
     * 寻找数组的中心索引
     * 
     * @param nums 输入数组
     * @return 中心索引，如果不存在返回-1
     */
    int pivotIndex(vector<int>& nums) {
        // 边界情况处理
        if (nums.empty()) {
            return -1;
        }
        
        // 计算数组总和
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }
        
        // 维护左侧元素的和
        int leftSum = 0;
        
        // 遍历数组寻找中心索引
        for (int i = 0; i < nums.size(); i++) {
            // 右侧和 = 总和 - 左侧和 - 当前元素
            int rightSum = totalSum - leftSum - nums[i];
            
            // 如果左侧和等于右侧和，返回当前索引
            if (leftSum == rightSum) {
                return i;
            }
            
            // 更新左侧和，包括当前元素
            leftSum += nums[i];
        }
        
        // 没有找到中心索引
        return -1;
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 7, 3, 6, 5, 6};
    int result1 = solution.pivotIndex(nums1);
    // 预期输出: 3
    cout << "测试用例1: " << result1 << endl;

    // 测试用例2
    vector<int> nums2 = {1, 2, 3};
    int result2 = solution.pivotIndex(nums2);
    // 预期输出: -1
    cout << "测试用例2: " << result2 << endl;
    
    // 测试用例3
    vector<int> nums3 = {2, 1, -1};
    int result3 = solution.pivotIndex(nums3);
    // 预期输出: 0
    cout << "测试用例3: " << result3 << endl;
    
    // 测试用例4
    vector<int> nums4 = {};
    int result4 = solution.pivotIndex(nums4);
    // 预期输出: -1
    cout << "测试用例4: " << result4 << endl;
    
    return 0;
}