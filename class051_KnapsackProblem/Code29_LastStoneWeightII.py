# LeetCode 1049. 最后一块石头的重量 II
# 题目描述：有一堆石头，每块石头的重量都是正整数。每一回合，从中选出任意两块石头，然后将它们一起粉碎。
# 假设石头的重量分别为x和y，且x <= y。那么粉碎的可能结果如下：
# - 如果x == y，那么两块石头都会被完全粉碎；
# - 如果x != y，那么重量为x的石头会被完全粉碎，而重量为y的石头新重量为y - x。
# 最后，最多只会剩下一块石头。返回此石头的最小可能重量。如果没有石头剩下，就返回0。
# 链接：https://leetcode.cn/problems/last-stone-weight-ii/
# 
# 解题思路：
# 这是一个0-1背包问题的变种，问题可以转化为：
# 将石头分成两组，使得两组的重量差最小。那么剩下的石头重量就是两组重量的差的最小值。
# 等价于：找到一组石头，使得它们的重量尽可能接近总重量的一半。
# 
# 状态定义：dp[i] 表示是否可以组成和为i的子集
# 状态转移方程：dp[i] = dp[i] || dp[i - stone]，其中 stone 是当前石头的重量，且 i >= stone
# 初始状态：dp[0] = True，表示和为0的子集存在（空集）
# 
# 时间复杂度：O(n * target)，其中 n 是石头的数量，target 是总重量的一半
# 空间复杂度：O(target)，使用一维DP数组

from typing import List


def last_stone_weight_ii(stones: List[int]) -> int:
    """
    计算最后一块石头的最小可能重量
    
    Args:
        stones: 石头重量数组
    
    Returns:
        int: 最后一块石头的最小可能重量
    """
    # 参数验证
    if not stones:
        return 0
    
    # 计算石头总重量
    total_sum = sum(stones)
    
    # 计算目标和：总重量的一半（向下取整）
    target = total_sum // 2
    
    # 创建一维DP数组，dp[i]表示是否可以组成和为i的子集
    dp = [False] * (target + 1)
    
    # 初始状态：和为0的子集存在（空集）
    dp[0] = True
    
    # 对于每个石头，逆序遍历目标和（0-1背包问题）
    for stone in stones:
        for i in range(target, stone - 1, -1):
            # 状态转移：如果dp[i - stone]为True，说明可以组成和为i - stone的子集，
            # 那么再加上当前石头stone，就可以组成和为i的子集
            dp[i] = dp[i] or dp[i - stone]
    
    # 找到最大的i，使得dp[i]为True，其中i <= target
    # 剩下的石头重量就是total_sum - 2 * i
    for i in range(target, -1, -1):
        if dp[i]:
            return total_sum - 2 * i
    
    return 0  # 理论上不会到达这里


def last_stone_weight_ii_2d(stones: List[int]) -> int:
    """
    二维DP数组实现
    dp[i][j]表示前i个石头中是否可以选择一些石头，使得它们的和为j
    """
    if not stones:
        return 0
    
    total_sum = sum(stones)
    target = total_sum // 2
    n = len(stones)
    
    # 创建二维DP数组
    dp = [[False] * (target + 1) for _ in range(n + 1)]
    
    # 初始化：前0个石头可以组成和为0的子集
    for i in range(n + 1):
        dp[i][0] = True
    
    # 填充DP数组
    for i in range(1, n + 1):
        stone = stones[i - 1]
        for j in range(1, target + 1):
            # 不选当前石头
            dp[i][j] = dp[i - 1][j]
            # 选当前石头（如果可以的话）
            if j >= stone:
                dp[i][j] = dp[i][j] or dp[i - 1][j - stone]
    
    # 找到最大的j，使得dp[n][j]为True，其中j <= target
    for j in range(target, -1, -1):
        if dp[n][j]:
            return total_sum - 2 * j
    
    return 0


def last_stone_weight_ii_optimized(stones: List[int]) -> int:
    """
    优化版本：提前剪枝
    """
    if not stones:
        return 0
    
    # 计算石头总重量
    total_sum = sum(stones)
    
    # 计算目标和：总重量的一半（向下取整）
    target = total_sum // 2
    
    # 创建一维DP数组，dp[i]表示是否可以组成和为i的子集
    dp = [False] * (target + 1)
    dp[0] = True
    
    # 使用一个变量记录当前可以达到的最大和
    current_max = 0
    
    for stone in stones:
        # 逆序遍历目标和
        for i in range(target, stone - 1, -1):
            if dp[i - stone] and not dp[i]:
                dp[i] = True
                current_max = max(current_max, i)
                # 提前剪枝：如果已经可以达到目标和，直接返回结果
                if current_max == target:
                    return total_sum - 2 * target
    
    # 返回最小可能的最后一块石头重量
    return total_sum - 2 * current_max


def last_stone_weight_ii_dfs(stones: List[int]) -> int:
    """
    递归+记忆化搜索实现
    """
    if not stones:
        return 0
    
    total_sum = sum(stones)
    target = total_sum // 2
    n = len(stones)
    
    # 使用二维列表作为缓存，memo[i][j]表示从第i个石头开始，是否可以组成和为j的子集
    memo = [[-1 for _ in range(target + 1)] for _ in range(n)]  # -1表示未计算，0表示False，1表示True
    
    def dfs(index: int, remaining: int) -> bool:
        """递归辅助函数"""
        # 基础情况：如果剩余和为0，说明找到了一个子集
        if remaining == 0:
            return True
        
        # 基础情况：如果已经考虑完所有石头或者剩余和小于0，返回False
        if index == n or remaining < 0:
            return False
        
        # 检查缓存
        if memo[index][remaining] != -1:
            return memo[index][remaining] == 1
        
        # 尝试两种选择：选或不选当前石头
        # 1. 选当前石头：剩余和减去当前石头的重量，继续考虑下一个石头
        choose = dfs(index + 1, remaining - stones[index])
        
        # 2. 不选当前石头：剩余和不变，继续考虑下一个石头
        not_choose = dfs(index + 1, remaining)
        
        # 缓存结果
        memo[index][remaining] = 1 if (choose or not_choose) else 0
        return memo[index][remaining] == 1
    
    # 从最大的可能和开始，找到最大的可达到的和
    for i in range(target, -1, -1):
        if dfs(0, i):
            return total_sum - 2 * i
    
    return 0


def last_stone_weight_ii_bit(stones: List[int]) -> int:
    """
    位运算优化的DP实现
    每个二进制位表示是否可以组成对应索引的和
    """
    if not stones:
        return 0
    
    total_sum = sum(stones)
    target = total_sum // 2
    
    # 使用位集，每个位表示是否可以组成对应的和
    # bits的第i位为1表示可以组成和为i的子集
    bits = 1  # 0b000...0001，表示和为0是可以的
    
    for stone in stones:
        # 位运算：将当前bits左移stone位，并与原bits进行或操作
        bits |= bits << stone
        
        # 限制bits的范围，避免不必要的计算
        if bits > (1 << (target + 1)) - 1:
            bits &= (1 << (target + 1)) - 1
    
    # 找到最大的i，使得bits的第i位为1，其中i <= target
    for i in range(target, -1, -1):
        if (bits & (1 << i)) != 0:
            return total_sum - 2 * i
    
    return 0


def last_stone_weight_ii_set(stones: List[int]) -> int:
    """
    另一种方法：直接计算可能的重量差异
    使用集合来记录所有可能的重量和
    """
    if not stones:
        return 0
    
    # 使用布尔数组来记录所有可能的重量和
    total_sum = sum(stones)
    target = total_sum // 2
    dp = [False] * (target + 1)
    dp[0] = True
    
    for stone in stones:
        for i in range(target, stone - 1, -1):
            dp[i] = dp[i] or dp[i - stone]
    
    # 找到最大的可能和
    for i in range(target, -1, -1):
        if dp[i]:
            return total_sum - 2 * i
    
    return 0


# 测试代码
if __name__ == "__main__":
    # 测试用例1
    stones1 = [2, 7, 4, 1, 8, 1]
    print(f"测试用例1结果: {last_stone_weight_ii(stones1)}")  # 预期输出: 1
    
    # 测试用例2
    stones2 = [31, 26, 33, 21, 40]
    print(f"测试用例2结果: {last_stone_weight_ii(stones2)}")  # 预期输出: 5
    
    # 测试用例3
    stones3 = [1, 2]
    print(f"测试用例3结果: {last_stone_weight_ii(stones3)}")  # 预期输出: 1