# LeetCode 1049. 最后一块石头的重量 II
# 题目描述：有一堆石头，每块石头的重量都是正整数。
# 每一回合，从中选出任意两块石头，然后将它们一起粉碎。假设石头的重量分别为 x 和 y，且 x <= y。那么粉碎的可能结果如下：
# 如果 x == y，那么两块石头都会被完全粉碎；
# 如果 x != y，那么重量为 x 的石头将会完全粉碎，而重量为 y 的石头新重量为 y-x。
# 最后，最多只会剩下一块石头。返回此石头的最小可能重量。如果没有石头剩下，就返回 0。
# 链接：https://leetcode.cn/problems/last-stone-weight-ii/
# 
# 解题思路：
# 这是一个经典的0-1背包问题的变种。我们的目标是将石头分成两组，使得两组的重量差最小。
# 设石头总重量为sum，我们希望找到一个子集，其总重量尽可能接近sum/2。
# 这样，两组的重量差就是sum - 2 * subsetSum，我们需要最小化这个值。
# 
# 状态定义：dp[j]表示是否能组成重量为j的子集
# 状态转移方程：dp[j] = dp[j] or dp[j - stones[i]]
# 初始状态：dp[0] = True（空子集的重量为0）
# 
# 时间复杂度：O(n * target)，其中n是石头数量，target是sum/2
# 空间复杂度：O(target)，使用一维DP数组

def last_stone_weight_ii(stones: list[int]) -> int:
    """
    计算最后一块石头的最小可能重量
    
    参数:
        stones: 石头的重量数组
    
    返回:
        最后一块石头的最小可能重量
    """
    if not stones:
        return 0
    
    # 计算石头总重量
    total_sum = sum(stones)
    
    # 目标是找到尽可能接近total_sum/2的子集和
    target = total_sum // 2
    
    # 创建DP数组，dp[j]表示是否能组成重量为j的子集
    dp = [False] * (target + 1)
    
    # 初始状态：空子集的重量为0是可以组成的
    dp[0] = True
    
    # 遍历每一块石头
    for stone in stones:
        # 逆序遍历重量，避免重复使用同一块石头
        for j in range(target, stone - 1, -1):
            # 如果j-stone可以组成，那么j也可以组成
            dp[j] = dp[j] or dp[j - stone]
    
    # 找到最大的j，使得dp[j]为True，且j <= target
    max_subset_sum = 0
    for j in range(target, -1, -1):
        if dp[j]:
            max_subset_sum = j
            break
    
    # 两组的重量差就是total_sum - 2 * max_subset_sum
    return total_sum - 2 * max_subset_sum

def last_stone_weight_ii_2d(stones: list[int]) -> int:
    """
    使用二维DP数组的版本
    
    参数:
        stones: 石头的重量数组
    
    返回:
        最后一块石头的最小可能重量
    """
    if not stones:
        return 0
    
    # 计算石头总重量
    total_sum = sum(stones)
    
    target = total_sum // 2
    n = len(stones)
    
    # 创建二维DP数组，dp[i][j]表示前i个石头是否能组成重量为j的子集
    dp = [[False] * (target + 1) for _ in range(n + 1)]
    
    # 初始状态：空子集的重量为0是可以组成的
    for i in range(n + 1):
        dp[i][0] = True
    
    # 填充DP数组
    for i in range(1, n + 1):
        for j in range(1, target + 1):
            # 不选第i个石头
            dp[i][j] = dp[i-1][j]
            
            # 选第i个石头（如果j >= stones[i-1]）
            if j >= stones[i-1]:
                dp[i][j] = dp[i][j] or dp[i-1][j - stones[i-1]]
    
    # 找到最大的j，使得dp[n][j]为True
    max_subset_sum = 0
    for j in range(target, -1, -1):
        if dp[n][j]:
            max_subset_sum = j
            break
    
    return total_sum - 2 * max_subset_sum

def last_stone_weight_ii_bit_set(stones: list[int]) -> int:
    """
    使用DP数组记录可达的重量集合
    
    参数:
        stones: 石头的重量数组
    
    返回:
        最后一块石头的最小可能重量
    """
    if not stones:
        return 0
    
    # 使用布尔数组模拟位集合，记录可达的重量
    dp = [False] * 1501  # 根据约束，最大可能的总重量是30 * 100 = 3000，所以target最多是1500
    dp[0] = True
    
    total_sum = 0
    
    for stone in stones:
        total_sum += stone
        # 逆序更新，避免重复使用同一块石头
        for j in range(min(total_sum, 1500), stone - 1, -1):
            dp[j] = dp[j] or dp[j - stone]
    
    # 寻找最小可能的重量差
    min_weight = total_sum
    for j in range(total_sum // 2 + 1):
        if dp[j]:
            min_weight = min(min_weight, total_sum - 2 * j)
    
    return min_weight

from functools import lru_cache

def last_stone_weight_ii_recursive(stones: list[int]) -> int:
    """
    递归+记忆化搜索实现
    这个方法对于较大的输入可能会超时，但展示了递归的思路
    
    参数:
        stones: 石头的重量数组
    
    返回:
        最后一块石头的最小可能重量
    """
    if not stones:
        return 0
    
    # 计算石头总重量
    total_sum = sum(stones)
    target = total_sum // 2
    n = len(stones)
    
    @lru_cache(maxsize=None)
    def dfs(index: int, current_sum: int) -> int:
        """
        递归辅助函数，寻找最大的子集和不超过target
        
        参数:
            index: 当前处理的石头索引
            current_sum: 当前子集和
        
        返回:
            最大的子集和不超过target
        """
        # 基础情况：处理完所有石头或当前和已经超过target
        if index == n or current_sum > target:
            return current_sum if current_sum <= target else 0
        
        # 选择当前石头
        take_sum = dfs(index + 1, current_sum + stones[index])
        
        # 不选择当前石头
        not_take_sum = dfs(index + 1, current_sum)
        
        return max(take_sum, not_take_sum)
    
    max_subset_sum = dfs(0, 0)
    return total_sum - 2 * max_subset_sum

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
    
    # 测试二维DP版本
    print(f"测试用例2 (二维DP): {last_stone_weight_ii_2d(stones2)}")
    
    # 测试位集合版本
    print(f"测试用例2 (位集合): {last_stone_weight_ii_bit_set(stones2)}")
    
    # 测试递归版本
    print(f"测试用例2 (递归): {last_stone_weight_ii_recursive(stones2)}")