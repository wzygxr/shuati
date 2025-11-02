# 分割等和子集 (Partition Equal Subset Sum)
# 给你一个只包含正整数的非空数组 nums 。请你判断是否可以将这个数组分割成两个子集，
# 使得两个子集的元素和相等。
# 测试链接 : https://leetcode.cn/problems/partition-equal-subset-sum/

class Code20_PartitionEqualSubsetSum:
    
    # 使用动态规划解决分割等和子集问题
    # 核心思想：将问题转化为背包问题，判断是否能选出若干元素使其和等于总和的一半
    # 时间复杂度: O(n * sum)
    # 空间复杂度: O(sum)
    @staticmethod
    def canPartition(nums):
        # 计算数组元素总和
        total_sum = sum(nums)
        
        # 如果总和是奇数，则无法分割成两个相等的子集
        if total_sum % 2 != 0:
            return False
        
        # 目标和为总和的一半
        target = total_sum // 2
        
        # dp[i] 表示是否能选出若干元素使其和等于i
        dp = [False] * (target + 1)
        # 初始状态：和为0总是可以实现（不选择任何元素）
        dp[0] = True
        
        # 状态转移：枚举每个元素
        for num in nums:
            # 从后往前更新，避免重复使用同一元素
            for i in range(target, num - 1, -1):
                dp[i] = dp[i] or dp[i - num]
        
        # 返回是否能选出若干元素使其和等于target
        return dp[target]

# 测试代码
if __name__ == "__main__":
    solution = Code20_PartitionEqualSubsetSum()
    
    # 测试用例1: 可以分割
    nums1 = [1, 5, 11, 5]
    result1 = solution.canPartition(nums1)
    print(f"测试用例1: {nums1}, 结果: {result1}")  # 期望输出: True
    
    # 测试用例2: 不能分割
    nums2 = [1, 2, 3, 5]
    result2 = solution.canPartition(nums2)
    print(f"测试用例2: {nums2}, 结果: {result2}")  # 期望输出: False
    
    # 测试用例3: 单个元素
    nums3 = [1]
    result3 = solution.canPartition(nums3)
    print(f"测试用例3: {nums3}, 结果: {result3}")  # 期望输出: False
    
    # 测试用例4: 两个相等元素
    nums4 = [2, 2]
    result4 = solution.canPartition(nums4)
    print(f"测试用例4: {nums4}, 结果: {result4}")  # 期望输出: True