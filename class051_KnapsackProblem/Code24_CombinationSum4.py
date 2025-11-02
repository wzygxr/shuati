# LeetCode 377. 组合总和 Ⅳ
# 题目描述：给你一个由 不同 整数组成的数组 nums ，和一个目标整数 target 。
# 请你从 nums 中找出并返回总和为 target 的元素组合的个数。
# 注意：顺序不同的序列被视作不同的组合。
# 链接：https://leetcode.cn/problems/combination-sum-iv/
# 
# 解题思路：
# 这是一个完全背包问题的变种，但是与传统的完全背包问题不同，这里需要计算的是排列数而不是组合数。
# 对于排列数，我们需要先遍历容量（target），再遍历物品（nums数组），这样可以确保不同顺序的序列被视为不同的组合。
# 
# 状态定义：dp[i] 表示总和为i的元素组合的个数
# 状态转移方程：dp[i] += dp[i - num]，对于每个num，如果i >= num
# 初始状态：dp[0] = 1，表示总和为0的组合只有一种（空组合）
# 
# 时间复杂度：O(target * n)，其中n是nums数组的长度
# 空间复杂度：O(target)，使用一维DP数组

def combination_sum4(nums, target):
    """
    找出总和为target的元素组合的个数
    
    Args:
        nums: 不同整数组成的数组
        target: 目标整数
    
    Returns:
        int: 总和为target的元素组合的个数
    """
    # 参数验证
    if not nums:
        return 0
    
    # 创建一维DP数组，dp[i]表示总和为i的元素组合的个数
    dp = [0] * (target + 1)
    
    # 初始状态：总和为0的组合只有一种（空组合）
    dp[0] = 1
    
    # 注意：为了计算排列数，我们先遍历容量（target），再遍历物品（nums数组）
    # 这样可以确保不同顺序的序列被视为不同的组合
    for i in range(1, target + 1):
        for num in nums:
            # 状态转移：如果当前容量i大于等于物品重量num
            if i >= num:
                dp[i] += dp[i - num]
    
    # 返回结果：总和为target的元素组合的个数
    return dp[target]

def combination_sum4_optimized(nums, target):
    """
    优化版本：剪枝处理
    
    Args:
        nums: 不同整数组成的数组
        target: 目标整数
    
    Returns:
        int: 总和为target的元素组合的个数
    """
    # 参数验证
    if not nums:
        return 0
    
    # 排序nums数组，方便后续剪枝
    nums.sort()
    
    # 创建一维DP数组
    dp = [0] * (target + 1)
    dp[0] = 1
    
    # 先遍历容量，再遍历物品
    for i in range(1, target + 1):
        for num in nums:
            # 剪枝：如果num大于i，后续的num会更大，不需要继续遍历
            if num > i:
                break
            dp[i] += dp[i - num]
    
    return dp[target]

def combination_sum4_dfs(nums, target):
    """
    递归+记忆化搜索实现
    
    Args:
        nums: 不同整数组成的数组
        target: 目标整数
    
    Returns:
        int: 总和为target的元素组合的个数
    """
    # 参数验证
    if not nums:
        return 0
    
    # 使用字典作为缓存
    memo = {}
    
    def dfs(remaining):
        """
        递归辅助函数
        
        Args:
            remaining: 剩余需要达到的目标值
        
        Returns:
            int: 达到剩余目标值的组合数
        """
        # 基础情况：剩余目标值为0，返回1（表示找到一种组合）
        if remaining == 0:
            return 1
        
        # 基础情况：剩余目标值小于0，返回0（表示无法找到组合）
        if remaining < 0:
            return 0
        
        # 检查缓存
        if remaining in memo:
            return memo[remaining]
        
        # 计算所有可能的组合数
        count = 0
        for num in nums:
            # 递归计算使用当前num后的组合数
            count += dfs(remaining - num)
        
        # 缓存结果
        memo[remaining] = count
        
        return count
    
    # 调用递归函数
    return dfs(target)

def combination_sum4_dfs_with_lru_cache(nums, target):
    """
    使用functools.lru_cache的优化DFS实现
    
    Args:
        nums: 不同整数组成的数组
        target: 目标整数
    
    Returns:
        int: 总和为target的元素组合的个数
    """
    from functools import lru_cache
    
    # 参数验证
    if not nums:
        return 0
    
    # 使用lru_cache装饰器缓存结果
    @lru_cache(maxsize=None)
    def dfs(remaining):
        # 基础情况
        if remaining == 0:
            return 1
        if remaining < 0:
            return 0
        
        # 计算组合数
        count = 0
        for num in nums:
            count += dfs(remaining - num)
        
        return count
    
    result = dfs(target)
    # 清除缓存
    dfs.cache_clear()
    return result

def combination_sum4_backtracking(nums, target):
    """
    回溯算法实现（注意：对于大数会超时，仅作为参考）
    
    Args:
        nums: 不同整数组成的数组
        target: 目标整数
    
    Returns:
        int: 总和为target的元素组合的个数
    """
    # 参数验证
    if not nums:
        return 0
    
    # 结果计数器
    count = 0
    
    def backtrack(remaining):
        nonlocal count
        # 找到一个有效组合
        if remaining == 0:
            count += 1
            return
        
        # 超过目标值，直接返回
        if remaining < 0:
            return
        
        # 尝试每个数字
        for num in nums:
            backtrack(remaining - num)
    
    # 调用回溯函数
    backtrack(target)
    
    return count

def combination_sum4_bfs(nums, target):
    """
    使用BFS的实现方式
    
    Args:
        nums: 不同整数组成的数组
        target: 目标整数
    
    Returns:
        int: 总和为target的元素组合的个数
    """
    if not nums:
        return 0
    
    # dp[i]表示总和为i的元素组合的个数
    dp = [0] * (target + 1)
    dp[0] = 1
    
    # BFS思想：从小到大计算每个值的组合数
    for i in range(target + 1):
        # 如果当前值i无法达到，跳过
        if dp[i] == 0:
            continue
        
        # 尝试在当前值的基础上添加每个数字
        for num in nums:
            # 确保不会超出target
            if i + num <= target:
                dp[i + num] += dp[i]
    
    return dp[target]

def combination_sum4_with_overflow_check(nums, target):
    """
    包含溢出检查的版本
    
    Args:
        nums: 不同整数组成的数组
        target: 目标整数
    
    Returns:
        int: 总和为target的元素组合的个数
    """
    import sys
    
    if not nums:
        return 0
    
    dp = [0] * (target + 1)
    dp[0] = 1
    
    for i in range(1, target + 1):
        for num in nums:
            if i >= num:
                # 检查溢出
                if dp[i] > sys.maxsize - dp[i - num]:
                    return -1  # 表示溢出
                dp[i] += dp[i - num]
    
    return dp[target]

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1, 2, 3]
    target1 = 4
    print(f"测试用例1结果: {combination_sum4(nums1, target1)}")  # 预期输出: 7
    
    # 测试用例2
    nums2 = [9]
    target2 = 3
    print(f"测试用例2结果: {combination_sum4(nums2, target2)}")  # 预期输出: 0
    
    # 测试用例3
    nums3 = [1, 2, 4]
    target3 = 32
    print(f"测试用例3结果: {combination_sum4(nums3, target3)}")  # 大数测试
    
    # 测试用例4
    nums4 = [1, 50]
    target4 = 100
    print(f"测试用例4结果: {combination_sum4(nums4, target4)}")  # 预期输出: 3