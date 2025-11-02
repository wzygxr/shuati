# LeetCode 494. 目标和
# 题目描述：给你一个整数数组 nums 和一个整数 target 。
# 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：
# 例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，得到表达式 "+2-1" 。
# 返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。
# 链接：https://leetcode.cn/problems/target-sum/
# 
# 解题思路：
# 这是一个背包问题的变种，我们可以将问题转化为：
# 找到一个子集，使得该子集中的元素和与其余元素和的差等于target
# 设所有元素的和为sum，子集和为subsetSum，则：
# subsetSum - (sum - subsetSum) = target
# 即 2*subsetSum = sum + target
# 因此 subsetSum = (sum + target) / 2
# 
# 所以问题转化为：找到和为subsetSum的子集数目
# 这是一个0-1背包问题（每个元素只能选或不选）
# 
# 状态定义：dp[i] 表示和为i的子集数目
# 状态转移方程：dp[i] += dp[i - num]，其中num是当前元素，且i >= num
# 初始状态：dp[0] = 1 表示和为0的子集有一个（空集）
# 
# 时间复杂度：O(n * target)，其中n是数组长度
# 空间复杂度：O(target)，使用一维DP数组

from typing import List, Dict, Optional


def find_target_sum_ways(nums: List[int], target: int) -> int:
    """
    计算可以通过添加'+'或'-'使得表达式结果等于target的不同表达式数目
    
    Args:
        nums: 整数数组
        target: 目标和
    
    Returns:
        int: 不同表达式的数目
    """
    # 参数验证
    if not nums:
        return 0
    
    # 计算所有元素的和
    total_sum = sum(nums)
    
    # 检查是否有解的条件
    # 1. total_sum + target 必须是非负数
    # 2. total_sum + target 必须是偶数
    if total_sum < abs(target) or (total_sum + target) % 2 != 0:
        return 0
    
    # 计算目标子集和
    subset_sum = (total_sum + target) // 2
    
    # 创建一维DP数组，dp[i]表示和为i的子集数目
    dp = [0] * (subset_sum + 1)
    
    # 初始状态：和为0的子集有一个（空集）
    dp[0] = 1
    
    # 对于每个元素，逆序遍历子集和（0-1背包问题）
    for num in nums:
        # 从大到小遍历，避免重复使用同一个元素
        for i in range(subset_sum, num - 1, -1):
            dp[i] += dp[i - num]
    
    # 返回结果：和为subset_sum的子集数目
    return dp[subset_sum]


def find_target_sum_ways_optimized(nums: List[int], target: int) -> int:
    """
    优化版本：处理可能的大数问题（使用适当的数据类型）
    """
    if not nums:
        return 0
    
    total_sum = sum(nums)
    
    if total_sum < abs(target) or (total_sum + target) % 2 != 0:
        return 0
    
    subset_sum = (total_sum + target) // 2
    
    # 使用Python的int类型（可以处理大数）
    dp = [0] * (subset_sum + 1)
    dp[0] = 1
    
    for num in nums:
        for i in range(subset_sum, num - 1, -1):
            dp[i] += dp[i - num]
    
    return dp[subset_sum]


def find_target_sum_ways_2d(nums: List[int], target: int) -> int:
    """
    二维DP数组实现
    dp[i][j]表示前i个元素中和为j的子集数目
    """
    if not nums:
        return 0
    
    total_sum = sum(nums)
    
    if total_sum < abs(target) or (total_sum + target) % 2 != 0:
        return 0
    
    subset_sum = (total_sum + target) // 2
    n = len(nums)
    
    # 创建二维DP数组
    dp = [[0] * (subset_sum + 1) for _ in range(n + 1)]
    
    # 初始化：前0个元素中和为0的子集有一个（空集）
    dp[0][0] = 1
    
    # 填充DP数组
    for i in range(1, n + 1):
        num = nums[i - 1]
        for j in range(subset_sum + 1):
            # 不选当前元素
            dp[i][j] = dp[i - 1][j]
            # 选当前元素（如果可以的话）
            if j >= num:
                dp[i][j] += dp[i - 1][j - num]
    
    return dp[n][subset_sum]


def find_target_sum_ways_dfs(nums: List[int], target: int) -> int:
    """
    递归+记忆化搜索实现
    """
    if not nums:
        return 0
    
    # 计算所有元素的和
    total_sum = sum(nums)
    
    # 检查是否有解的条件
    if total_sum < abs(target) or (total_sum + target) % 2 != 0:
        return 0
    
    subset_sum = (total_sum + target) // 2
    n = len(nums)
    
    # 使用二维列表作为缓存，memo[i][j]表示前i个元素中和为j的子集数目
    memo = [[-1] * (subset_sum + 1) for _ in range(n)]
    
    def dfs(index: int, current_sum: int) -> int:
        """递归辅助函数"""
        # 基础情况：如果已经考虑完所有元素
        if index == n:
            # 如果当前子集和等于目标子集和，返回1，否则返回0
            return 1 if current_sum == subset_sum else 0
        
        # 检查缓存
        if memo[index][current_sum] != -1:
            return memo[index][current_sum]
        
        # 计算当前状态的解
        result = 0
        
        # 选择不将当前元素加入子集
        result += dfs(index + 1, current_sum)
        
        # 选择将当前元素加入子集（如果不会超过目标和）
        if current_sum + nums[index] <= subset_sum:
            result += dfs(index + 1, current_sum + nums[index])
        
        # 缓存结果
        memo[index][current_sum] = result
        return result
    
    # 调用递归函数
    return dfs(0, 0)


def find_target_sum_ways_dfs2(nums: List[int], target: int) -> int:
    """
    另一种递归实现方式，直接计算表达式数目
    """
    if not nums:
        return 0
    
    # 使用字典作为缓存，键为"index,current_sum"，值为该状态下的表达式数目
    memo: Dict[str, int] = {}
    
    def dfs2(index: int, current_sum: int) -> int:
        """另一种递归辅助函数实现"""
        # 基础情况：如果已经考虑完所有元素
        if index == len(nums):
            # 如果当前和等于目标和，返回1，否则返回0
            return 1 if current_sum == target else 0
        
        # 生成缓存键
        key = f"{index},{current_sum}"
        
        # 检查缓存
        if key in memo:
            return memo[key]
        
        # 选择在当前元素前添加'+'
        add = dfs2(index + 1, current_sum + nums[index])
        
        # 选择在当前元素前添加'-'
        subtract = dfs2(index + 1, current_sum - nums[index])
        
        # 计算总表达式数目
        total = add + subtract
        
        # 缓存结果
        memo[key] = total
        return total
    
    # 调用递归函数
    return dfs2(0, 0)


def find_target_sum_ways_alternative(nums: List[int], target: int) -> int:
    """
    另一种动态规划方法，使用二维数组记录到达每个和的路径数
    """
    # 参数验证
    if not nums:
        return 0
    
    # 计算所有元素的和，用于确定可能的和的范围
    total_sum = sum(nums)
    
    # 检查是否有解的条件
    if total_sum < abs(target):
        return 0
    
    # 创建DP数组，dp[i][j]表示前i个元素能组成和为j的表达式数目
    # 由于和可能为负数，我们需要进行偏移，将和范围从[-total_sum, total_sum]映射到[0, 2*total_sum]
    offset = total_sum
    dp = [[0] * (2 * total_sum + 1) for _ in range(len(nums) + 1)]
    
    # 初始状态：前0个元素能组成和为0的表达式有一个（空表达式）
    dp[0][offset] = 1
    
    # 填充DP数组
    for i in range(1, len(nums) + 1):
        num = nums[i - 1]
        for j in range(2 * total_sum + 1):
            # 如果前i-1个元素能组成和为j的表达式
            if dp[i - 1][j] > 0:
                # 添加'+'：和变为j + num
                if j + num < 2 * total_sum + 1:
                    dp[i][j + num] += dp[i - 1][j]
                # 添加'-'：和变为j - num
                if j - num >= 0:
                    dp[i][j - num] += dp[i - 1][j]
    
    # 返回结果：前n个元素能组成和为target的表达式数目
    # 注意需要加上偏移量
    return dp[len(nums)][target + offset]


# 测试代码
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1, 1, 1, 1, 1]
    target1 = 3
    print(f"测试用例1结果: {find_target_sum_ways(nums1, target1)}")  # 预期输出: 5
    
    # 测试用例2
    nums2 = [1]
    target2 = 1
    print(f"测试用例2结果: {find_target_sum_ways(nums2, target2)}")  # 预期输出: 1
    
    # 测试用例3
    nums3 = [1, 2, 3, 4, 5]
    target3 = 3
    print(f"测试用例3结果: {find_target_sum_ways(nums3, target3)}")  # 预期输出: 3