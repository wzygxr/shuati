# LeetCode 918. 环形子数组的最大和
# 给定一个长度为 n 的环形整数数组 nums ，返回 nums 的非空子数组的最大可能和。
# 环形数组意味着数组的末端将会与开头相连呈环状。
# 测试链接 : https://leetcode.cn/problems/maximum-sum-circular-subarray/


"""
解题思路:
这是最大子数组和问题的环形变种。在环形数组中，最大子数组可能有两种情况：
1. 不跨越数组边界：直接使用Kadane算法求解
2. 跨越数组边界：可以转换为求最小子数组和，然后用总和减去最小子数组和

对于第二种情况，如果最大子数组跨越了边界，那么中间未被选中的部分就是一个连续的最小子数组。
因此，我们可以计算总和减去最小子数组和，就得到了跨越边界的最大子数组和。

特殊情况：如果所有元素都是负数，那么最小子数组和等于总和，会导致结果为0，
但实际上子数组不能为空，所以这种情况应该直接返回最大子数组和。

时间复杂度: O(n) - 需要遍历数组三次（最大子数组和、最小子数组和、总和）
空间复杂度: O(1) - 只需要常数个变量存储状态

是否最优解: 是，这是该问题的最优解法
"""


class Code09_MaximumSumCircularSubarray:
    @staticmethod
    def maxSubarraySumCircular(nums):
        """
        计算环形子数组的最大和

        Args:
            nums: List[int] - 输入的整数数组

        Returns:
            int - 环形子数组的最大和
        """
        if not nums:
            return 0
        if len(nums) == 1:
            return nums[0]
        
        # 计算最大子数组和（不跨越边界）
        max_kadane = Code09_MaximumSumCircularSubarray._kadane_max(nums)
        
        # 计算总和
        total_sum = sum(nums)
        
        # 计算最小子数组和
        min_kadane = Code09_MaximumSumCircularSubarray._kadane_min(nums)
        
        # 计算跨越边界的最大子数组和
        max_circular = total_sum - min_kadane
        
        # 特殊情况：如果所有元素都是负数，max_circular会是0，但子数组不能为空
        # 所以应该返回不跨越边界的最大子数组和
        if max_circular == 0:
            return max_kadane
        
        # 返回两种情况的最大值
        return max(max_kadane, max_circular)
    
    @staticmethod
    def _kadane_max(nums):
        """Kadane算法求最大子数组和"""
        dp = nums[0]
        max_sum = nums[0]
        
        for i in range(1, len(nums)):
            dp = max(nums[i], dp + nums[i])
            max_sum = max(max_sum, dp)
        
        return max_sum
    
    @staticmethod
    def _kadane_min(nums):
        """Kadane算法求最小子数组和"""
        dp = nums[0]
        min_sum = nums[0]
        
        for i in range(1, len(nums)):
            dp = min(nums[i], dp + nums[i])
            min_sum = min(min_sum, dp)
        
        return min_sum
    
    '''
    相关题目扩展:
    1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
    2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
    3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
    4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
    5. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
    '''


# 测试代码
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1, -2, 3, -2]
    result1 = Code09_MaximumSumCircularSubarray.maxSubarraySumCircular(nums1)
    print(f"输入数组: {nums1}")
    print(f"环形子数组的最大和: {result1}")
    # 预期输出: 3

    # 测试用例2
    nums2 = [5, -3, 5]
    result2 = Code09_MaximumSumCircularSubarray.maxSubarraySumCircular(nums2)
    print(f"输入数组: {nums2}")
    print(f"环形子数组的最大和: {result2}")
    # 预期输出: 10

    # 测试用例3
    nums3 = [3, -1, 2, -1]
    result3 = Code09_MaximumSumCircularSubarray.maxSubarraySumCircular(nums3)
    print(f"输入数组: {nums3}")
    print(f"环形子数组的最大和: {result3}")
    # 预期输出: 4

    # 测试用例4
    nums4 = [-2, -3, -1]
    result4 = Code09_MaximumSumCircularSubarray.maxSubarraySumCircular(nums4)
    print(f"输入数组: {nums4}")
    print(f"环形子数组的最大和: {result4}")
    # 预期输出: -1