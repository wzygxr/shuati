# LeetCode 416. 分割等和子集
# 题目描述：给你一个 只包含正整数 的 非空 数组 nums 。请你判断是否可以将这个数组分割成两个子集，
# 使得两个子集的元素和相等。
# 链接：https://leetcode.cn/problems/partition-equal-subset-sum/
# 
# 解题思路：
# 这是一个0-1背包问题的应用，问题可以转化为：
# 1. 计算数组的总和 sum
# 2. 如果 sum 是奇数，那么无法将数组分成两个和相等的子集，直接返回 false
# 3. 如果 sum 是偶数，那么问题转化为：是否存在一个子集，使得其和为 sum/2
# 
# 状态定义：dp[i] 表示是否可以从数组中选择一些元素，使得它们的和为 i
# 状态转移方程：dp[i] = dp[i] || dp[i - num]，其中 num 是当前元素，且 i >= num
# 初始状态：dp[0] = true，表示和为0的子集存在（空集）
# 
# 时间复杂度：O(n * target)，其中 n 是数组长度，target 是数组和的一半
# 空间复杂度：O(target)，使用一维DP数组

from typing import List, Optional


def can_partition(nums: List[int]) -> bool:
    """
    判断是否可以将数组分割成两个和相等的子集
    
    Args:
        nums: 非空正整数数组
    
    Returns:
        bool: 是否可以分割成两个和相等的子集
    """
    # 参数验证
    if len(nums) < 2:
        return False
    
    # 计算数组总和
    total_sum = sum(nums)
    
    # 如果总和是奇数，无法分成两个和相等的子集
    if total_sum % 2 != 0:
        return False
    
    # 计算目标和：总和的一半
    target = total_sum // 2
    
    # 创建一维DP数组，dp[i]表示是否可以从数组中选择一些元素，使得它们的和为i
    dp = [False] * (target + 1)
    
    # 初始状态：和为0的子集存在（空集）
    dp[0] = True
    
    # 对于每个元素，逆序遍历目标和（0-1背包问题）
    for num in nums:
        # 逆序遍历，避免重复使用同一个元素
        for i in range(target, num - 1, -1):
            # 状态转移：如果dp[i - num]为True，说明可以组成和为i - num的子集，
            # 那么再加上当前元素num，就可以组成和为i的子集
            dp[i] = dp[i] or dp[i - num]
    
    # 返回是否可以组成和为target的子集
    return dp[target]


def can_partition_optimized(nums: List[int]) -> bool:
    """
    优化版本：提前剪枝
    """
    if len(nums) < 2:
        return False
    
    total_sum = 0
    max_num = 0
    for num in nums:
        total_sum += num
        max_num = max(max_num, num)
    
    # 如果总和是奇数，无法分成两个和相等的子集
    if total_sum % 2 != 0:
        return False
    
    target = total_sum // 2
    
    # 如果最大元素大于目标和，无法分成两个和相等的子集
    if max_num > target:
        return False
    
    # 排序数组，方便后续剪枝
    nums.sort()
    
    dp = [False] * (target + 1)
    dp[0] = True
    
    for num in nums:
        # 剪枝：如果当前元素已经大于目标和，可以跳过
        if num > target:
            continue
        
        for i in range(target, num - 1, -1):
            dp[i] = dp[i] or dp[i - num]
        
        # 提前结束：如果已经找到解，可以直接返回True
        if dp[target]:
            return True
    
    return dp[target]


def can_partition_2d(nums: List[int]) -> bool:
    """
    二维DP数组实现
    dp[i][j]表示前i个元素中是否可以选择一些元素，使得它们的和为j
    """
    if len(nums) < 2:
        return False
    
    total_sum = sum(nums)
    
    if total_sum % 2 != 0:
        return False
    
    target = total_sum // 2
    n = len(nums)
    
    # 创建二维DP数组
    dp = [[False] * (target + 1) for _ in range(n + 1)]
    
    # 初始化：前0个元素可以组成和为0的子集
    for i in range(n + 1):
        dp[i][0] = True
    
    # 填充DP数组
    for i in range(1, n + 1):
        num = nums[i - 1]
        for j in range(1, target + 1):
            # 不选当前元素
            dp[i][j] = dp[i - 1][j]
            # 选当前元素（如果可以的话）
            if j >= num:
                dp[i][j] = dp[i][j] or dp[i - 1][j - num]
        
        # 提前结束：如果已经找到解，可以直接返回True
        if dp[i][target]:
            return True
    
    return dp[n][target]


def can_partition_dfs(nums: List[int]) -> bool:
    """
    递归+记忆化搜索实现
    """
    if len(nums) < 2:
        return False
    
    total_sum = sum(nums)
    
    if total_sum % 2 != 0:
        return False
    
    target = total_sum // 2
    n = len(nums)
    
    # 使用二维列表作为缓存，memo[i][j]表示从第i个元素开始，是否可以组成和为j的子集
    memo = [[-1 for _ in range(target + 1)] for _ in range(n)]  # -1表示未计算，0表示False，1表示True
    
    def dfs(index: int, remaining: int) -> bool:
        """递归辅助函数"""
        # 基础情况：如果剩余和为0，说明找到了一个子集
        if remaining == 0:
            return True
        
        # 基础情况：如果已经考虑完所有元素或者剩余和小于0，返回False
        if index == n or remaining < 0:
            return False
        
        # 检查缓存
        if memo[index][remaining] != -1:
            return memo[index][remaining] == 1
        
        # 尝试两种选择：选或不选当前元素
        # 1. 选当前元素：剩余和减去当前元素的值，继续考虑下一个元素
        choose = dfs(index + 1, remaining - nums[index])
        
        # 2. 不选当前元素：剩余和不变，继续考虑下一个元素
        not_choose = dfs(index + 1, remaining)
        
        # 缓存结果
        memo[index][remaining] = 1 if (choose or not_choose) else 0
        return memo[index][remaining] == 1
    
    # 调用递归函数
    return dfs(0, target)


def can_partition_bit(nums: List[int]) -> bool:
    """
    位运算优化的DP实现
    每个二进制位表示是否可以组成对应索引的和
    """
    if len(nums) < 2:
        return False
    
    total_sum = sum(nums)
    
    if total_sum % 2 != 0:
        return False
    
    target = total_sum // 2
    
    # 使用位集，每个位表示是否可以组成对应的和
    # bits的第i位为1表示可以组成和为i的子集
    # 初始时，只有和为0的情况是可能的
    bits = 1  # 0b000...0001，表示和为0是可以的
    
    for num in nums:
        # 位运算：将当前bits左移num位，并与原bits进行或操作
        # 这样，新的bits中的第i位为1当且仅当原来的bits中的第i位为1（不选当前元素）
        # 或者原来的bits中的第i-num位为1（选当前元素）
        bits |= bits << num
        
        # 检查目标和是否已经可达
        if (bits & (1 << target)) != 0:
            return True
    
    # 检查目标和是否可达
    return (bits & (1 << target)) != 0


# 测试代码
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1, 5, 11, 5]
    print(f"测试用例1结果: {can_partition(nums1)}")  # 预期输出: True
    
    # 测试用例2
    nums2 = [1, 2, 3, 5]
    print(f"测试用例2结果: {can_partition(nums2)}")  # 预期输出: False
    
    # 测试用例3
    nums3 = [1, 2, 5]
    print(f"测试用例3结果: {can_partition(nums3)}")  # 预期输出: False