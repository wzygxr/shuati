# 最后一块石头的重量 II
# 
# 问题描述：
# 有一堆石头，用整数数组 stones 表示，其中 stones[i] 表示第 i 块石头的重量。
# 每一回合，从中选出任意两块石头，然后将它们一起粉碎。
# 假设石头的重量分别为 x 和 y，且 x <= y，粉碎结果：
# - 如果 x == y，那么两块石头都会被完全粉碎；
# - 如果 x != y，那么重量为 x 的石头将会完全粉碎，而重量为 y 的石头新重量为 y-x。
# 最后，最多只会剩下一块石头，返回此石头最小的可能重量。如果没有石头剩下，就返回 0。
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
# 时间复杂度：O(n * sum)，其中n是石头数量，sum是总重量
# 空间复杂度：O(sum)，使用一维DP数组
# 
# 测试链接 : https://leetcode.cn/problems/last-stone-weight-ii/

def lastStoneWeightII(nums):
    """
    计算最后一块石头的最小可能重量
    
    解题思路：
    1. 这道题可以转化为将石头分为两堆，使得两堆重量差最小
    2. 假设两堆分别为 A 和 B，A >= B
    3. 最终剩下的石头重量就是 A - B
    4. 要使 A - B 最小，就要使 B 尽可能接近 sum/2
    5. 问题转化为：在不超过 sum/2 的前提下，背包最多能装多少重量的石头
    6. 这就是一个标准的01背包问题
    
    Args:
        nums: 石头重量数组
    
    Returns:
        最后一块石头的最小可能重量
    """
    # 参数验证
    if not nums:
        return 0
    
    # 计算所有石头的总重量
    total_sum = 0
    for num in nums:
        total_sum += num
    
    # nums中随意选择数字
    # 累加和一定要 <= sum / 2
    # 又尽量接近
    near_val = near(nums, total_sum // 2)
    # 返回两堆石头重量差的最小值
    # 其中一堆重量为near_val，另一堆重量为total_sum-near_val
    # 重量差为 (total_sum-near_val) - near_val = total_sum - 2*near_val
    return total_sum - near_val - near_val

def near(nums, t):
    """
    非负数组nums中，子序列累加和不超过t，但是最接近t的累加和是多少
    01背包问题(子集累加和尽量接近t) + 空间压缩
    
    解题思路：
    使用01背包问题的解法，dp[j]表示容量为j的背包最多能装的石头重量
    状态转移方程：dp[j] = max(dp[j], dp[j - num] + num)
    
    Args:
        nums: 数组
        t: 目标值
    
    Returns:
        不超过t但最接近t的子序列累加和
    """
    # dp[j] 表示在容量为j的背包中能装入的最大重量
    dp = [0] * (t + 1)
    
    # 遍历每个石头（物品）
    for num in nums:
        # 倒序遍历背包容量，确保每个物品只使用一次
        for j in range(t, num - 1, -1):
            # 状态转移方程：
            # dp[j] = max(不选择当前石头, 选择当前石头)
            # 不选择当前石头：dp[j]（保持原值）
            # 选择当前石头：dp[j - num] + num（前一个状态+当前石头重量）
            # dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-nums[i]]+nums[i])
            dp[j] = max(dp[j], dp[j - num] + num)
    
    # 返回容量为t的背包能装入的最大重量
    return dp[t]

# LeetCode 1049. 最后一块石头的重量 II
# 题目描述：有一堆石头，用整数数组 stones 表示，每一回合，从中选出任意两块石头，然后将它们一起粉碎。
# 假设石头的重量分别为 x 和 y，且 x <= y，粉碎结果：
# 如果 x == y，那么两块石头都会被完全粉碎；
# 如果 x != y，那么重量为 x 的石头将会完全粉碎，而重量为 y 的石头新重量为 y-x。
# 最后，最多只会剩下一块石头，返回此石头最小的可能重量。如果没有石头剩下，就返回 0。
# 链接：https://leetcode.cn/problems/last-stone-weight-ii/
def lastStoneWeightIILeetcode(stones):
    """
    计算最后一块石头的最小可能重量（LeetCode版本）
    
    解题思路：
    1. 这道题可以转化为将石头分为两堆，使得两堆重量差最小
    2. 假设两堆分别为 A 和 B，A >= B
    3. 最终剩下的石头重量就是 A - B
    4. 要使 A - B 最小，就要使 B 尽可能接近 sum/2
    5. 问题转化为：在不超过 sum/2 的前提下，背包最多能装多少重量的石头
    6. 这就是一个标准的01背包问题
    
    Args:
        stones: 石头重量数组
    
    Returns:
        最后一块石头的最小可能重量
    """
    # 参数验证
    if not stones:
        return 0
    
    # 计算所有石头的总重量
    total_sum = sum(stones)
    
    # 目标是使其中一堆石头的重量尽可能接近总重量的一半
    target = total_sum // 2
    
    # dp[j] 表示容量为j的背包最多能装的石头重量
    dp = [0] * (target + 1)
    
    # 遍历每个石头（物品）
    for stone in stones:
        # 倒序遍历背包容量，确保每个物品只使用一次
        for j in range(target, stone - 1, -1):
            # 状态转移方程：
            # dp[j] = max(不选择当前石头, 选择当前石头)
            # 不选择当前石头：dp[j]（保持原值）
            # 选择当前石头：dp[j - stone] + stone（前一个状态+当前石头重量）
            dp[j] = max(dp[j], dp[j - stone] + stone)
    
    # 返回两堆石头重量差的最小值
    # 其中一堆重量为dp[target]，另一堆重量为total_sum-dp[target]
    # 重量差为 (total_sum-dp[target]) - dp[target] = total_sum - 2*dp[target]
    return total_sum - 2 * dp[target]

'''
解题思路：
1. 这道题可以转化为将石头分为两堆，使得两堆重量差最小
2. 假设两堆分别为 A 和 B，A >= B
3. 最终剩下的石头重量就是 A - B
4. 要使 A - B 最小，就要使 B 尽可能接近 sum/2
5. 问题转化为：在不超过 sum/2 的前提下，背包最多能装多少重量的石头
6. 这就是一个标准的01背包问题

示例:
输入: stones = [2,7,4,1,8,1]
输出: 1
解释: 
  最优分法:
  选 2,8,1 放一堆，总重量是11
  选 7,4,1 放另一堆，总重量是12
  最后剩下石头重量 = 12 - 11 = 1

时间复杂度: O(n * sum)
  - 外层循环遍历所有石头：O(n)
  - 内层循环遍历背包容量：O(sum)
空间复杂度: O(sum)
  - 一维DP数组的空间消耗
'''