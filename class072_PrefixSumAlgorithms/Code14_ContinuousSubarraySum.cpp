#include <iostream>
#include <vector>
#include <unordered_map>
using namespace std;

/**
 * 连续的子数组和 (Continuous Subarray Sum)
 * 
 * 题目描述:
 * 给你一个整数数组 nums 和一个整数 k ，编写一个函数来判断该数组是否含有同时满足下述条件的连续子数组：
 * 1. 子数组大小至少为 2
 * 2. 子数组元素总和为 k 的倍数
 * 如果存在，返回 true ；否则，返回 false 。
 * 
 * 示例:
 * 输入：nums = [23,2,4,6,7], k = 6
 * 输出：true
 * 解释：[2,4] 是一个大小为 2 的子数组，总和为 6 。
 * 
 * 输入：nums = [23,2,6,4,7], k = 6
 * 输出：true
 * 解释：[23, 2, 6, 4, 7] 是大小为 5 的子数组，总和为 42 。42 是 6 的倍数，因为 42 = 7 * 6 且 7 是整数。
 * 
 * 输入：nums = [23,2,6,4,7], k = 13
 * 输出：false
 * 
 * 提示:
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^9
 * 0 <= sum(nums[i]) <= 2^31 - 1
 * 1 <= k <= 2^31 - 1
 * 
 * 题目链接: https://leetcode.com/problems/continuous-subarray-sum/
 * 
 * 解题思路:
 * 使用前缀和 + 哈希表的方法。
 * 1. 遍历数组，计算前缀和
 * 2. 对于当前位置的前缀和，计算其模k的余数
 * 3. 如果两个不同位置的前缀和模k的余数相同，说明这两个位置之间的子数组和是k的倍数
 * 4. 同时需要保证两个位置之间的距离至少为2
 * 5. 特殊情况：k为0的情况需要单独处理
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(n) - 哈希表最多存储n个不同的前缀和余数
 */

class Solution {
public:
    /**
     * 判断数组是否含有满足条件的连续子数组
     * 
     * @param nums 输入数组
     * @param k 目标整数
     * @return 是否存在满足条件的子数组
     */
    bool checkSubarraySum(vector<int>& nums, int k) {
        // 边界情况处理
        if (nums.size() < 2) {
            return false;
        }
        
        // 哈希表记录前缀和余数及其第一次出现的位置
        unordered_map<int, int> remainderMap;
        // 初始化：前缀和为0在位置-1出现
        remainderMap[0] = -1;
        
        long long prefixSum = 0;  // 使用long long避免整数溢出
        
        // 遍历数组
        for (int i = 0; i < nums.size(); i++) {
            // 更新前缀和
            prefixSum += nums[i];
            
            // 计算前缀和模k的余数
            // 注意：当k为0时，不能进行模运算
            int remainder = 0;
            if (k != 0) {
                remainder = prefixSum % k;
                // 处理负数余数的情况
                if (remainder < 0) {
                    remainder += k;
                }
            } else {
                remainder = prefixSum;
            }
            
            // 如果当前余数之前出现过，并且两个位置之间的距离至少为2
            if (remainderMap.find(remainder) != remainderMap.end()) {
                if (i - remainderMap[remainder] >= 2) {
                    return true;
                }
            } else {
                // 记录当前余数第一次出现的位置
                remainderMap[remainder] = i;
            }
        }
        
        // 没有找到满足条件的子数组
        return false;
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {23, 2, 4, 6, 7};
    int k1 = 6;
    bool result1 = solution.checkSubarraySum(nums1, k1);
    // 预期输出: true
    cout << "测试用例1: " << (result1 ? "true" : "false") << endl;

    // 测试用例2
    vector<int> nums2 = {23, 2, 6, 4, 7};
    int k2 = 6;
    bool result2 = solution.checkSubarraySum(nums2, k2);
    // 预期输出: true
    cout << "测试用例2: " << (result2 ? "true" : "false") << endl;
    
    // 测试用例3
    vector<int> nums3 = {23, 2, 6, 4, 7};
    int k3 = 13;
    bool result3 = solution.checkSubarraySum(nums3, k3);
    // 预期输出: false
    cout << "测试用例3: " << (result3 ? "true" : "false") << endl;
    
    // 测试用例4 - k=0的情况
    vector<int> nums4 = {0, 0};
    int k4 = 0;
    bool result4 = solution.checkSubarraySum(nums4, k4);
    // 预期输出: true
    cout << "测试用例4: " << (result4 ? "true" : "false") << endl;
    
    return 0;
}