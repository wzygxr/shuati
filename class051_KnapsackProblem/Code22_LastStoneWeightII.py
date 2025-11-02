# LeetCode 1049. 最后一块石头的重量 II
# 题目描述：有一堆石头，每块石头的重量都是正整数。
# 每一回合，从中选出任意两块石头，然后将它们一起粉碎。假设石头的重量分别为x和y，且x <= y。
# 那么粉碎的可能结果如下：
# - 如果x == y，那么两块石头都会被完全粉碎；
# - 如果x != y，那么重量为x的石头将会完全粉碎，而重量为y的石头新重量为y-x。
# 最后，最多只会剩下一块石头。返回此石头最小的可能重量。如果没有石头剩下，就返回0。
# 链接：https://leetcode.cn/problems/last-stone-weight-ii/
# 
# 解题思路：
# 这是一个可以转化为01背包问题的变种。我们的目标是将石头分成两堆，使得它们的重量尽可能接近，
# 这样最后剩下的石头重量就会最小（等于两堆重量之差）。
# 
# 具体分析：
# 1. 如果我们能将石头分成两堆，重量分别为sum1和sum2，那么最终剩下的石头重量为|sum1 - sum2|
# 2. 由于sum1 + sum2 = totalSum（石头总重量），所以剩下的重量为|totalSum - 2*sum1|
# 3. 为了最小化这个值，我们需要让sum1尽可能接近totalSum/2
# 4. 这转化为：在石头中选择一些，使得它们的总重量不超过totalSum/2，且尽可能接近totalSum/2
# 5. 这正是一个01背包问题，背包容量为totalSum/2，物品重量为石头重量，目标是最大化能装入的重量
# 
# 状态定义：dp[i] 表示是否可以组成和为i的石头堆
# 状态转移方程：dp[i] = dp[i] || dp[i - stones[j]]，其中j遍历所有石头，且i >= stones[j]
# 初始状态：dp[0] = True（表示和为0可以通过不选任何石头来实现）
# 
# 时间复杂度：O(n * target)，其中n是石头数量，target是总重量的一半
# 空间复杂度：O(target)，使用一维DP数组

def last_stone_weight_ii(stones):
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
    if len(stones) == 1:
        return stones[0]
    
    # 计算石头总重量
    total_sum = sum(stones)
    
    # 目标是找到不超过total_sum/2的最大子集和
    target = total_sum // 2
    
    # 创建DP数组，dp[i]表示是否可以组成和为i的石头堆
    dp = [False] * (target + 1)
    
    # 初始状态：和为0可以通过不选任何石头来实现
    dp[0] = True
    
    # 遍历每个石头（物品）
    for stone in stones:
        # 逆序遍历目标和（容量），防止重复使用同一个石头
        for i in range(target, stone - 1, -1):
            # 状态转移：选择当前石头或不选择当前石头
            dp[i] = dp[i] or dp[i - stone]
    
    # 找到最大的i，使得dp[i]为True
    max_sum = 0
    for i in range(target, -1, -1):
        if dp[i]:
            max_sum = i
            break
    
    # 最后一块石头的最小可能重量为总重量减去两倍的最大子集和
    return total_sum - 2 * max_sum

def last_stone_weight_ii_optimized(stones):
    """
    优化版本：使用一维DP数组记录可以达到的最大和
    
    Args:
        stones: 石头重量数组
    
    Returns:
        int: 最后一块石头的最小可能重量
    """
    # 参数验证
    if not stones:
        return 0
    if len(stones) == 1:
        return stones[0]
    
    # 计算石头总重量
    total_sum = sum(stones)
    
    # 目标是找到不超过total_sum/2的最大子集和
    target = total_sum // 2
    
    # 创建DP数组，dp[i]表示是否可以组成和为i的石头堆
    dp = [False] * (target + 1)
    dp[0] = True
    
    # 记录当前可以达到的最大和
    current_max = 0
    
    for stone in stones:
        # 逆序遍历，但只遍历到当前可能的最大和+stone
        for i in range(min(target, current_max + stone), stone - 1, -1):
            if dp[i - stone]:
                dp[i] = True
                # 更新当前可以达到的最大和
                current_max = max(current_max, i)
                # 如果已经可以达到target，提前结束
                if current_max == target:
                    return total_sum - 2 * target
    
    return total_sum - 2 * current_max

def last_stone_weight_ii_2d(stones):
    """
    二维DP实现，更容易理解
    
    Args:
        stones: 石头重量数组
    
    Returns:
        int: 最后一块石头的最小可能重量
    """
    # 参数验证
    if not stones:
        return 0
    if len(stones) == 1:
        return stones[0]
    
    total_sum = sum(stones)
    target = total_sum // 2
    n = len(stones)
    
    # dp[i][j]表示前i个石头是否可以组成和为j的石头堆
    dp = [[False] * (target + 1) for _ in range(n + 1)]
    
    # 初始化：前0个石头只能组成和为0的石头堆
    dp[0][0] = True
    
    # 遍历每个石头
    for i in range(1, n + 1):
        stone = stones[i - 1]
        # 遍历每个可能的和
        for j in range(target + 1):
            # 不选择当前石头
            dp[i][j] = dp[i - 1][j]
            
            # 选择当前石头（如果j >= stone）
            if j >= stone:
                dp[i][j] = dp[i][j] or dp[i - 1][j - stone]
    
    # 找到最大的j，使得dp[n][j]为True
    max_sum = 0
    for j in range(target, -1, -1):
        if dp[n][j]:
            max_sum = j
            break
    
    return total_sum - 2 * max_sum

def last_stone_weight_ii_dfs(stones):
    """
    递归+记忆化搜索实现
    
    Args:
        stones: 石头重量数组
    
    Returns:
        int: 最后一块石头的最小可能重量
    """
    # 参数验证
    if not stones:
        return 0
    if len(stones) == 1:
        return stones[0]
    
    # 计算石头总重量
    total_sum = sum(stones)
    
    # 目标是找到不超过total_sum/2的最大子集和
    target = total_sum // 2
    
    # 使用记忆化搜索
    memo = {}
    
    def dfs(index, current_sum):
        # 基础情况：已经处理完所有石头，或者当前和已经达到目标
        if index == len(stones) or current_sum == target:
            return current_sum
        
        # 生成记忆化键
        key = (index, current_sum)
        if key in memo:
            return memo[key]
        
        # 不选择当前石头
        not_take = dfs(index + 1, current_sum)
        
        # 选择当前石头（如果当前和加上石头重量不超过目标）
        take = current_sum
        if current_sum + stones[index] <= target:
            take = dfs(index + 1, current_sum + stones[index])
        
        # 记录结果
        memo[key] = max(not_take, take)
        return memo[key]
    
    # 尝试找到最大的不超过target的子集和
    max_sum = dfs(0, 0)
    
    return total_sum - 2 * max_sum

def last_stone_weight_ii_bitwise(stones):
    """
    位运算优化版本
    使用位图记录所有可能的子集和
    
    Args:
        stones: 石头重量数组
    
    Returns:
        int: 最后一块石头的最小可能重量
    """
    # 参数验证
    if not stones:
        return 0
    if len(stones) == 1:
        return stones[0]
    
    # 计算石头总重量
    total_sum = sum(stones)
    target = total_sum // 2
    
    # 使用位掩码表示所有可能的子集和
    # bit[i] = 1表示可以组成和为i的子集
    dp = 1  # 初始状态：可以组成和为0的子集
    
    for stone in stones:
        # 对于每个石头，更新可能的子集和集合
        dp |= dp << stone
    
    # 找到最大的i <= target，使得dp的第i位为1
    max_sum = 0
    for i in range(target, -1, -1):
        if (dp & (1 << i)) != 0:
            max_sum = i
            break
    
    return total_sum - 2 * max_sum

# 测试用例
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
    
    # 测试用例4
    stones4 = [1]
    print(f"测试用例4结果: {last_stone_weight_ii(stones4)}")  # 预期输出: 1