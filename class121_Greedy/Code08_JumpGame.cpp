/**
 * 跳跃游戏
 * 
 * 题目描述：
 * 给定一个非负整数数组 nums ，你最初位于数组的第一个下标。
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * 判断你是否能够到达最后一个下标。
 * 
 * 解题思路：
 * 1. 维护一个变量maxReach表示当前能到达的最远位置
 * 2. 遍历数组，对于每个位置i：
 *    - 如果i > maxReach，说明无法到达位置i，直接返回false
 *    - 否则更新maxReach = max(maxReach, i + nums[i])
 * 3. 遍历结束后，如果maxReach >= nums.length - 1，说明可以到达最后一个下标
 * 
 * 贪心策略的正确性：
 * 我们并不关心具体是如何跳到某个位置的，只关心能跳到的最远位置。
 * 如果能跳到位置i，那么位置0...i-1都一定能跳到。
 * 
 * 时间复杂度：O(n)，只需要遍历数组一次
 * 空间复杂度：O(1)，只使用了常数个额外变量
 * 
 * 相关题目：
 * - LeetCode 55: https://leetcode.cn/problems/jump-game/
 */

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * 跳跃游戏问题的贪心解法
 * 
 * @param nums 非负整数数组，表示每个位置可以跳跃的最大长度
 * @return bool 是否能够到达最后一个下标
 */
bool canJump(vector<int>& nums) {
    // 边界条件处理：如果数组为空或只有一个元素，则一定可以到达
    if (nums.empty() || nums.size() <= 1) {
        return true;
    }

    // 1. 初始化能到达的最远位置
    int maxReach = 0;

    // 2. 遍历数组，注意只需要遍历到倒数第二个元素
    for (int i = 0; i < nums.size() - 1; i++) {
        // 3. 如果当前位置无法到达，直接返回false
        if (i > maxReach) {
            return false;
        }

        // 4. 更新能到达的最远位置
        maxReach = max(maxReach, i + nums[i]);

        // 5. 提前优化：如果已经能到达最后一个位置，直接返回true
        if (maxReach >= nums.size() - 1) {
            return true;
        }
    }

    // 6. 最后判断是否能到达最后一个位置
    return maxReach >= nums.size() - 1;
}

// 测试代码
int main() {
    // 测试用例1
    // 输入: nums = [2,3,1,1,4]
    // 输出: true
    vector<int> nums1 = {2, 3, 1, 1, 4};
    cout << "测试用例1结果: " << (canJump(nums1) ? "true" : "false") << endl; // 期望输出: true

    // 测试用例2
    // 输入: nums = [3,2,1,0,4]
    // 输出: false
    vector<int> nums2 = {3, 2, 1, 0, 4};
    cout << "测试用例2结果: " << (canJump(nums2) ? "true" : "false") << endl; // 期望输出: false

    // 测试用例3：边界情况
    // 输入: nums = [0]
    // 输出: true
    vector<int> nums3 = {0};
    cout << "测试用例3结果: " << (canJump(nums3) ? "true" : "false") << endl; // 期望输出: true

    // 测试用例4：单个元素
    // 输入: nums = [1]
    // 输出: true
    vector<int> nums4 = {1};
    cout << "测试用例4结果: " << (canJump(nums4) ? "true" : "false") << endl; // 期望输出: true

    // 测试用例5：复杂情况
    // 输入: nums = [1,1,1,0]
    // 输出: true
    vector<int> nums5 = {1, 1, 1, 0};
    cout << "测试用例5结果: " << (canJump(nums5) ? "true" : "false") << endl; // 期望输出: true

    return 0;
}