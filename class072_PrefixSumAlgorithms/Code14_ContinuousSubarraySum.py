"""
连续的子数组和 (Continuous Subarray Sum)

题目描述:
给你一个整数数组 nums 和一个整数 k ，编写一个函数来判断该数组是否含有同时满足下述条件的连续子数组：
1. 子数组大小至少为 2
2. 子数组元素总和为 k 的倍数
如果存在，返回 true ；否则，返回 false 。

示例:
输入：nums = [23,2,4,6,7], k = 6
输出：true
解释：[2,4] 是一个大小为 2 的子数组，总和为 6 。

输入：nums = [23,2,6,4,7], k = 6
输出：true
解释：[23, 2, 6, 4, 7] 是大小为 5 的子数组，总和为 42 。42 是 6 的倍数，因为 42 = 7 * 6 且 7 是整数。

输入：nums = [23,2,6,4,7], k = 13
输出：false

提示:
1 <= nums.length <= 10^5
0 <= nums[i] <= 10^9
0 <= sum(nums[i]) <= 2^31 - 1
1 <= k <= 2^31 - 1

题目链接: https://leetcode.com/problems/continuous-subarray-sum/

解题思路:
使用前缀和 + 哈希表的方法。
1. 遍历数组，计算前缀和
2. 对于当前位置的前缀和，计算其模k的余数
3. 如果两个不同位置的前缀和模k的余数相同，说明这两个位置之间的子数组和是k的倍数
4. 同时需要保证两个位置之间的距离至少为2
5. 特殊情况：k为0的情况需要单独处理

时间复杂度: O(n) - 需要遍历数组一次
空间复杂度: O(n) - 哈希表最多存储n个不同的前缀和余数
"""

class Solution:
    def checkSubarraySum(self, nums, k):
        """
        判断数组是否含有满足条件的连续子数组
        
        Args:
            nums (List[int]): 输入数组
            k (int): 目标整数
            
        Returns:
            bool: 是否存在满足条件的子数组
        """
        # 边界情况处理
        if not nums or len(nums) < 2:
            return False
        
        # 哈希表记录前缀和余数及其第一次出现的位置
        remainder_map = {0: -1}
        
        # 当前前缀和
        prefix_sum = 0
        
        # 遍历数组
        for i, num in enumerate(nums):
            # 更新前缀和
            prefix_sum += num
            
            # 计算前缀和模k的余数
            # 注意：当k为0时，不能进行模运算
            remainder = prefix_sum % k if k != 0 else prefix_sum
            
            # 处理负数余数的情况
            if remainder < 0 and k != 0:
                remainder += k
            
            # 如果当前余数之前出现过，并且两个位置之间的距离至少为2
            if remainder in remainder_map:
                if i - remainder_map[remainder] >= 2:
                    return True
            else:
                # 记录当前余数第一次出现的位置
                remainder_map[remainder] = i
        
        # 没有找到满足条件的子数组
        return False


# 测试用例
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    nums1 = [23, 2, 4, 6, 7]
    k1 = 6
    result1 = solution.checkSubarraySum(nums1, k1)
    # 预期输出: True
    print("测试用例1:", result1)
    
    # 测试用例2
    nums2 = [23, 2, 6, 4, 7]
    k2 = 6
    result2 = solution.checkSubarraySum(nums2, k2)
    # 预期输出: True
    print("测试用例2:", result2)
    
    # 测试用例3
    nums3 = [23, 2, 6, 4, 7]
    k3 = 13
    result3 = solution.checkSubarraySum(nums3, k3)
    # 预期输出: False
    print("测试用例3:", result3)
    
    # 测试用例4 - k=0的情况
    nums4 = [0, 0]
    k4 = 0
    result4 = solution.checkSubarraySum(nums4, k4)
    # 预期输出: True
    print("测试用例4:", result4)