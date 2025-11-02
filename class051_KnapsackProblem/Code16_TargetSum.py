# LeetCode 494. 目标和
# 题目描述：给你一个整数数组 nums 和一个整数 target 。
# 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：
# 例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，得到表达式 "+2-1" 。
# 返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。
# 链接：https://leetcode.cn/problems/target-sum/
# 
# 解题思路：
# 这是一个01背包问题的变形。我们需要将数组分成两个部分：正数部分和负数部分，使得它们的和等于target。
# 设正数部分的和为sum_pos，负数部分的和为sum_neg，整个数组的和为sum。
# 则有：sum_pos - sum_neg = target
# 又因为：sum_pos + sum_neg = sum
# 联立解得：sum_pos = (sum + target) / 2
# 因此问题转化为：找到一个子集，其和等于(sum + target)/2，这样的子集有多少个？
# 注意：必须满足sum + target为偶数且sum + target >= 0，否则不存在这样的子集。
# 
# 时间复杂度：O(n * target)
# 空间复杂度：O(target)

def find_target_sum_ways(nums, target):
    """
    计算可以构造目标和的不同表达式的数目
    
    解题思路：
    这是一个01背包问题的变形。我们需要将数组分成两个部分：正数部分和负数部分，使得它们的和等于target。
    设正数部分的和为sum_pos，负数部分的和为sum_neg，整个数组的和为sum。
    则有：sum_pos - sum_neg = target
    又因为：sum_pos + sum_neg = sum
    联立解得：sum_pos = (sum + target) / 2
    因此问题转化为：找到一个子集，其和等于(sum + target)/2，这样的子集有多少个？
    注意：必须满足sum + target为偶数且sum + target >= 0，否则不存在这样的子集。
    
    Args:
        nums: 输入的整数数组
        target: 目标和
    
    Returns:
        int: 满足条件的表达式数目
    """
    # 参数验证
    if not nums:
        return 0
    
    # 计算数组总和
    total_sum = sum(nums)
    
    # 检查是否存在解的条件
    # 1. sum + target必须是非负数
    # 2. sum + target必须是偶数
    if total_sum + target < 0 or (total_sum + target) % 2 != 0:
        return 0
    
    # 计算目标和，即我们需要找的子集的和
    # 通过数学推导：sum_pos = (sum + target) / 2
    target_sum = (total_sum + target) // 2
    
    # 创建dp数组，dp[j]表示和为j的子集数目
    # 这是一个计数型的01背包问题
    dp = [0] * (target_sum + 1)
    dp[0] = 1  # 基础情况：空集的和为0，只有一种方式（不选择任何元素）
    
    # 遍历每个物品（数字）
    for num in nums:
        # 01背包问题，逆序遍历容量
        # 这样可以保证每个元素只使用一次
        for j in range(target_sum, num - 1, -1):
            # 状态转移：当前和j的方式数目 = 不选当前数字的方式数目 + 选当前数字的方式数目
            # dp[j] = 不选择当前数字 + 选择当前数字
            # 不选择当前数字：dp[j]（保持原值）
            # 选择当前数字：dp[j - num]（前一个状态）
            dp[j] += dp[j - num]
    
    # 返回和为target_sum的子集数目
    return dp[target_sum]

def find_target_sum_ways_optimized(nums, target):
    """
    优化版本：增加一些剪枝条件
    
    Args:
        nums: 输入的整数数组
        target: 目标和
    
    Returns:
        int: 满足条件的表达式数目
    """
    # 参数验证
    if not nums:
        return 0
    
    # 计算数组总和和最大值
    total_sum = sum(nums)
    max_num = max(nums)
    
    # 检查是否存在解的条件
    # 1. target不能超过数组总和的绝对值范围
    # 2. sum + target必须是偶数
    if target > total_sum or target < -total_sum or (total_sum + target) % 2 != 0:
        return 0
    
    target_sum = (total_sum + target) // 2
    
    # 提前剪枝：如果目标和小于0或大于总和，返回0
    if target_sum < 0 or target_sum > total_sum:
        return 0
    
    # 创建dp数组
    dp = [0] * (target_sum + 1)
    dp[0] = 1
    
    for num in nums:
        # 优化：如果当前数字大于target_sum，可以跳过
        # 因为当前数字本身就比目标和大，无法用于组成目标和
        if num > target_sum:
            continue
        
        for j in range(target_sum, num - 1, -1):
            dp[j] += dp[j - num]
    
    return dp[target_sum]

# 测试用例
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

'''
示例:
输入: nums = [1,1,1,1,1], target = 3
输出: 5
解释: 有5种不同的表达式使结果等于3。

输入: nums = [1], target = 1
输出: 1

时间复杂度: O(n * target)
  - 外层循环遍历所有元素：O(n)
  - 内层循环遍历目标值：O(target)
空间复杂度: O(target)
  - 一维DP数组的空间消耗
'''