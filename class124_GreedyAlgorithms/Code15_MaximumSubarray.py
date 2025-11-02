# 最大子数组和（Maximum Subarray）
# 题目来源：LeetCode 53
# 题目链接：https://leetcode.cn/problems/maximum-subarray/
# 
# 问题描述：
# 给定一个整数数组nums，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
# 
# 算法思路：
# 使用Kadane算法（贪心策略）：
# 1. 遍历数组，维护当前子数组和和最大子数组和
# 2. 如果当前子数组和为负数，重置为当前元素值（因为负数会减小后续和）
# 3. 否则，继续累加当前元素
# 4. 每次更新最大子数组和
# 
# 时间复杂度：O(n) - 只需遍历数组一次
# 空间复杂度：O(1) - 只使用了常数额外空间
# 
# 是否最优解：是。Kadane算法是该问题的最优解法。
# 
# 适用场景：
# 1. 连续子数组和问题
# 2. 最大收益问题
# 
# 异常处理：
# 1. 处理空数组情况
# 2. 处理全负数数组
# 
# 工程化考量：
# 1. 输入验证：检查数组是否为空
# 2. 边界条件：处理单元素数组
# 3. 性能优化：一次遍历完成计算
# 
# 相关题目：
# 1. LeetCode 152. 乘积最大子数组 - 类似问题，但需要处理负数
# 2. LeetCode 121. 买卖股票的最佳时机 - 最大差值问题
# 3. LeetCode 918. 环形子数组的最大和 - 环形数组版本
# 4. 牛客网 NC140 排序 - 各种排序算法实现
# 5. LintCode 41. 最大子数组 - 与本题相同
# 6. HackerRank - Maximum Subarray Sum - 类似问题
# 7. CodeChef - MAXSUBA - 最大子数组问题
# 8. AtCoder ABC139D - ModSum - 数学相关
# 9. Codeforces 1370C - Number Game - 博弈论相关
# 10. POJ 2479 - Maximum sum - 双最大子数组和

class Solution:
    """
    计算最大子数组和
    
    Args:
        nums: List[int] - 整数数组
    
    Returns:
        int - 最大子数组和
    """
    def maxSubArray(self, nums):
        # 边界条件检查
        if not nums:
            return 0
        
        n = len(nums)
        if n == 1:
            return nums[0]  # 只有一个元素，直接返回
        
        max_sum = nums[0]  # 最大子数组和
        current_sum = nums[0]  # 当前子数组和
        
        for i in range(1, n):
            # 如果当前子数组和为负数，重置为当前元素值
            if current_sum < 0:
                current_sum = nums[i]
            else:
                # 否则，继续累加当前元素
                current_sum += nums[i]
            
            # 更新最大子数组和
            if current_sum > max_sum:
                max_sum = current_sum
        
        return max_sum


def main():
    solution = Solution()
    
    # 测试用例1: 基本情况 - 正数数组
    nums1 = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
    result1 = solution.maxSubArray(nums1)
    print("测试用例1:")
    print(f"数组: {nums1}")
    print(f"最大子数组和: {result1}")
    print("期望输出: 6")
    print()
    
    # 测试用例2: 基本情况 - 全正数数组
    nums2 = [1, 2, 3, 4, 5]
    result2 = solution.maxSubArray(nums2)
    print("测试用例2:")
    print(f"数组: {nums2}")
    print(f"最大子数组和: {result2}")
    print("期望输出: 15")
    print()
    
    # 测试用例3: 基本情况 - 全负数数组
    nums3 = [-2, -3, -1, -5]
    result3 = solution.maxSubArray(nums3)
    print("测试用例3:")
    print(f"数组: {nums3}")
    print(f"最大子数组和: {result3}")
    print("期望输出: -1")
    print()
    
    # 测试用例4: 边界情况 - 单元素数组
    nums4 = [5]
    result4 = solution.maxSubArray(nums4)
    print("测试用例4:")
    print(f"数组: {nums4}")
    print(f"最大子数组和: {result4}")
    print("期望输出: 5")
    print()
    
    # 测试用例5: 边界情况 - 空数组
    nums5 = []
    result5 = solution.maxSubArray(nums5)
    print("测试用例5:")
    print(f"数组: {nums5}")
    print(f"最大子数组和: {result5}")
    print("期望输出: 0")
    print()
    
    # 测试用例6: 复杂情况 - 混合数组
    nums6 = [8, -19, 5, -4, 20]
    result6 = solution.maxSubArray(nums6)
    print("测试用例6:")
    print(f"数组: {nums6}")
    print(f"最大子数组和: {result6}")
    print("期望输出: 21")


if __name__ == "__main__":
    main()