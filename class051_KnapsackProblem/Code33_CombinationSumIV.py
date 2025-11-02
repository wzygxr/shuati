# LeetCode 377. 组合总和 IV
# 题目描述：给你一个由 不同 整数组成的数组 nums ，和一个目标整数 target 。
# 请你从 nums 中找出并返回总和为 target 的元素组合的个数。
# 注意：顺序不同的序列被视作不同的组合。
# 链接：https://leetcode.cn/problems/combination-sum-iv/
# 
# 解题思路：
// 这是一个与完全背包相关但关注排列而非组合的问题：
// - 数字可以多次使用（完全背包的特点）
// - 顺序不同的序列视为不同的组合（与组合数问题的关键区别）
// 
// 状态定义：dp[i] 表示凑成目标值i的不同排列数
// 状态转移方程：dp[i] += dp[i - num]，其中num是nums中的每个元素，且i >= num
// 初始状态：dp[0] = 1（凑成目标值0只有一种方式：不选择任何数字）
// 
// 时间复杂度：O(target * n)，其中n是数组nums的长度
// 空间复杂度：O(target)，使用一维DP数组

from typing import List
from functools import lru_cache


def combinationSum4(nums: List[int], target: int) -> int:
    """
    计算凑成目标值的不同排列数
    
    Args:
        nums: 不同整数组成的数组
        target: 目标整数
    
    Returns:
        总和为target的元素组合的个数
    """
    # 参数验证
    if target < 0:
        return 0
    if target == 0:
        return 1
    if not nums:
        return 0
    
    # 创建DP数组，dp[i]表示凑成目标值i的不同排列数
    dp = [0] * (target + 1)
    dp[0] = 1  # 凑成目标值0只有一种方式：不选择任何数字
    
    # 遍历目标值，从1到target
    # 注意：与零钱兑换II不同，这里我们先遍历目标值，再遍历数组元素，这样可以考虑不同顺序的排列
    for i in range(1, target + 1):
        # 遍历数组中的每个元素
        for num in nums:
            # 如果当前元素小于等于剩余需要凑成的目标值，更新dp[i]
            if num <= i:
                dp[i] += dp[i - num]
    
    return dp[target]


def combinationSum4DFS(nums: List[int], target: int) -> int:
    """
    递归+记忆化搜索实现
    """
    # 参数验证
    if target < 0:
        return 0
    if target == 0:
        return 1
    if not nums:
        return 0
    
    @lru_cache(maxsize=None)
    def dfs(remain: int) -> int:
        """
        递归辅助函数
        
        Args:
            remain: 剩余需要凑成的目标值
            
        Returns:
            凑成剩余目标值的不同排列数
        """
        # 基础情况：如果剩余目标值为0，说明找到了一种排列
        if remain == 0:
            return 1
        
        ways = 0
        
        # 尝试使用每个元素
        for num in nums:
            if num <= remain:
                # 递归计算剩余值的排列数
                ways += dfs(remain - num)
        
        return ways
    
    return dfs(target)


def combinationSum4Optimized(nums: List[int], target: int) -> int:
    """
    优化版本：提前排序和剪枝
    """
    # 参数验证
    if target < 0:
        return 0
    if target == 0:
        return 1
    if not nums:
        return 0
    
    # 对数组进行排序，以便在后续处理中进行剪枝
    nums.sort()
    
    # 创建DP数组
    dp = [0] * (target + 1)
    dp[0] = 1
    
    # 遍历目标值
    for i in range(1, target + 1):
        # 遍历数组中的元素
        for num in nums:
            # 如果当前元素大于剩余需要凑成的目标值，由于数组已排序，后面的元素更大，可以提前退出循环
            if num > i:
                break
            dp[i] += dp[i - num]
    
    return dp[target]


def combinationSum4MemoArray(nums: List[int], target: int) -> int:
    """
    递归+数组缓存实现
    """
    # 参数验证
    if target < 0:
        return 0
    if target == 0:
        return 1
    if not nums:
        return 0
    
    # 使用数组作为缓存，初始值为-1表示未计算
    memo = [-1] * (target + 1)
    memo[0] = 1  # 凑成目标值0只有一种方式
    
    def dfs(remain: int) -> int:
        """
        递归辅助函数
        
        Args:
            remain: 剩余需要凑成的目标值
            
        Returns:
            凑成剩余目标值的不同排列数
        """
        # 基础情况：如果剩余目标值为0，说明找到了一种排列
        if remain == 0:
            return 1
        
        # 检查缓存
        if memo[remain] != -1:
            return memo[remain]
        
        ways = 0
        
        # 尝试使用每个元素
        for num in nums:
            if num <= remain:
                ways += dfs(remain - num)
        
        # 缓存结果
        memo[remain] = ways
        return ways
    
    return dfs(target)


# 测试函数
def test_combination_sum4():
    # 测试用例1
    nums1 = [1, 2, 3]
    target1 = 4
    print(f"测试用例1结果: {combinationSum4(nums1, target1)}")  # 预期输出: 7 ([1,1,1,1], [1,1,2], [1,2,1], [1,3], [2,1,1], [2,2], [3,1])
    
    # 测试用例2
    nums2 = [9]
    target2 = 3
    print(f"测试用例2结果: {combinationSum4(nums2, target2)}")  # 预期输出: 0
    
    # 测试DFS实现
    print(f"测试用例1 (DFS): {combinationSum4DFS(nums1, target1)}")
    
    # 测试优化版本
    print(f"测试用例1 (优化版本): {combinationSum4Optimized(nums1, target1)}")
    
    # 测试数组缓存实现
    print(f"测试用例1 (数组缓存): {combinationSum4MemoArray(nums1, target1)}")


# 执行测试
if __name__ == "__main__":
    test_combination_sum4()