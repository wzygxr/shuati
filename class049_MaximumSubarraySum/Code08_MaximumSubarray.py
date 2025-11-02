# LeetCode 53. 最大子数组和
# 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
# 子数组 是数组中的一个连续部分。
# 测试链接 : https://leetcode.cn/problems/maximum-subarray/


"""
解题思路:
这是经典的Kadane算法问题。

状态定义:
dp[i] 表示以 nums[i] 结尾的最大子数组和

状态转移:
dp[i] = max(nums[i], dp[i-1] + nums[i])
即要么从当前元素重新开始，要么将当前元素加入之前的子数组

优化:
由于当前状态只与前一个状态有关，可以使用一个变量代替数组

时间复杂度: O(n) - 需要遍历数组一次
空间复杂度: O(1) - 只需要常数个变量存储状态

是否最优解: 是，这是该问题的最优解法
"""


class Code08_MaximumSubarray:
    @staticmethod
    def maxSubArray(nums):
        """
        计算最大子数组和

        Args:
            nums: List[int] - 输入的整数数组

        Returns:
            int - 最大子数组和
        """
        # dp表示以当前元素结尾的最大子数组和
        dp = nums[0]
        # maxSum表示全局最大子数组和
        maxSum = nums[0]
        
        # 从第二个元素开始遍历
        for i in range(1, len(nums)):
            # 要么从当前元素重新开始，要么将当前元素加入之前的子数组
            dp = max(nums[i], dp + nums[i])
            # 更新全局最大值
            maxSum = max(maxSum, dp)
        
        return maxSum
    
    '''
    相关题目扩展:
    1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
    2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
    3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
    4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
    5. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
    '''


# 测试代码
if __name__ == "__main__":
    # 测试用例1
    nums1 = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
    result1 = Code08_MaximumSubarray.maxSubArray(nums1)
    print(f"输入数组: {nums1}")
    print(f"最大子数组和: {result1}")
    # 预期输出: 6 ([4, -1, 2, 1]的和为6)

    # 测试用例2
    nums2 = [1]
    result2 = Code08_MaximumSubarray.maxSubArray(nums2)
    print(f"输入数组: {nums2}")
    print(f"最大子数组和: {result2}")
    # 预期输出: 1

    # 测试用例3
    nums3 = [5, 4, -1, 7, 8]
    result3 = Code08_MaximumSubarray.maxSubArray(nums3)
    print(f"输入数组: {nums3}")
    print(f"最大子数组和: {result3}")
    # 预期输出: 23