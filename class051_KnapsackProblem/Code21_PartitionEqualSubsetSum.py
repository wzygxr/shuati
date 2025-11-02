# LeetCode 416. 分割等和子集
# 题目描述：给你一个 只包含正整数 的 非空 数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
# 链接：https://leetcode.cn/problems/partition-equal-subset-sum/
# 
# 解题思路：
# 这是一个典型的01背包问题的变种。我们可以将问题转化为：
# 1. 计算数组的总和sum
# 2. 如果sum是奇数，直接返回false（无法分成两个和相等的子集）
# 3. 如果sum是偶数，问题转化为：是否存在一个子集，使得其和为sum/2
# 4. 这相当于01背包问题，容量为sum/2，物品价值和重量都是nums[i]，问是否能恰好装满背包
# 
# 状态定义：dp[i] 表示是否可以选择一些数字，使得它们的和恰好为i
# 状态转移方程：dp[i] = dp[i] || dp[i - nums[j]]，其中j遍历所有数字，且i >= nums[j]
# 初始状态：dp[0] = True（表示和为0可以通过不选任何数字来实现）
# 
# 时间复杂度：O(n * target)，其中n是数组长度，target是sum/2
# 空间复杂度：O(target)，使用一维DP数组

def can_partition(nums):
    """
    判断是否可以将数组分割成两个和相等的子集
    
    Args:
        nums: 非空正整数数组
    
    Returns:
        bool: 是否可以分割
    """
    # 参数验证
    if len(nums) <= 1:
        return False
    
    # 计算数组总和
    total_sum = sum(nums)
    
    # 如果总和是奇数，无法分成两个和相等的子集
    if total_sum % 2 != 0:
        return False
    
    # 目标和为总和的一半
    target = total_sum // 2
    
    # 创建DP数组，dp[i]表示是否可以选择一些数字，使得它们的和恰好为i
    dp = [False] * (target + 1)
    
    # 初始状态：和为0可以通过不选任何数字来实现
    dp[0] = True
    
    # 遍历每个数字（物品）
    for num in nums:
        # 逆序遍历目标和（容量），防止重复使用同一个数字
        for i in range(target, num - 1, -1):
            # 状态转移：选择当前数字或不选择当前数字
            dp[i] = dp[i] or dp[i - num]
        
        # 提前终止：如果已经找到可以组成target的子集，直接返回True
        if dp[target]:
            return True
    
    return dp[target]

def can_partition_optimized(nums):
    """
    优化版本：添加一些剪枝条件
    
    Args:
        nums: 非空正整数数组
    
    Returns:
        bool: 是否可以分割
    """
    # 参数验证
    if len(nums) <= 1:
        return False
    
    # 计算数组总和，并找出最大值
    total_sum = sum(nums)
    max_num = max(nums)
    
    # 如果总和是奇数，无法分成两个和相等的子集
    if total_sum % 2 != 0:
        return False
    
    target = total_sum // 2
    
    # 如果数组中最大值大于target，那么这个数字必须单独在一个子集，但剩下的数字之和无法等于target
    if max_num > target:
        return False
    
    # 如果数组中有元素等于target，直接返回True
    if target in nums:
        return True
    
    # 创建DP数组
    dp = [False] * (target + 1)
    dp[0] = True
    
    for num in nums:
        for i in range(target, num - 1, -1):
            dp[i] = dp[i] or dp[i - num]
            if dp[target]:
                return True
    
    return dp[target]

def can_partition_2d(nums):
    """
    二维DP实现，更容易理解
    
    Args:
        nums: 非空正整数数组
    
    Returns:
        bool: 是否可以分割
    """
    # 参数验证
    if len(nums) <= 1:
        return False
    
    total_sum = sum(nums)
    
    if total_sum % 2 != 0:
        return False
    
    target = total_sum // 2
    n = len(nums)
    
    # dp[i][j]表示前i个数字是否可以组成和为j的子集
    dp = [[False] * (target + 1) for _ in range(n + 1)]
    
    # 初始化：前0个数字只能组成和为0的子集
    dp[0][0] = True
    
    # 遍历每个数字
    for i in range(1, n + 1):
        num = nums[i - 1]
        # 遍历每个可能的和
        for j in range(target + 1):
            # 不选择当前数字
            dp[i][j] = dp[i - 1][j]
            
            # 选择当前数字（如果j >= num）
            if j >= num:
                dp[i][j] = dp[i][j] or dp[i - 1][j - num]
        
        # 提前终止
        if dp[i][target]:
            return True
    
    return dp[n][target]

def can_partition_dfs(nums):
    """
    递归+记忆化搜索实现
    
    Args:
        nums: 非空正整数数组
    
    Returns:
        bool: 是否可以分割
    """
    # 参数验证
    if len(nums) <= 1:
        return False
    
    total_sum = sum(nums)
    
    if total_sum % 2 != 0:
        return False
    
    target = total_sum // 2
    
    # 使用记忆化搜索
    memo = {}
    
    def dfs(index, current_target):
        # 基础情况：找到目标和
        if current_target == 0:
            return True
        
        # 基础情况：超出数组范围或目标和为负
        if index >= len(nums) or current_target < 0:
            return False
        
        # 生成记忆化键
        key = (index, current_target)
        if key in memo:
            return memo[key]
        
        # 选择当前数字或不选择当前数字
        result = (dfs(index + 1, current_target - nums[index]) or 
                 dfs(index + 1, current_target))
        
        # 记忆化结果
        memo[key] = result
        return result
    
    return dfs(0, target)

def can_partition_bitwise(nums):
    """
    位运算优化版本
    使用位图记录所有可能的和
    
    Args:
        nums: 非空正整数数组
    
    Returns:
        bool: 是否可以分割
    """
    # 参数验证
    if len(nums) <= 1:
        return False
    
    total_sum = sum(nums)
    
    if total_sum % 2 != 0:
        return False
    
    target = total_sum // 2
    
    # 使用位掩码表示所有可能的和
    # bit[i] = 1表示可以组成和为i的子集
    dp = 1  # 初始状态：可以组成和为0的子集
    
    for num in nums:
        # 对于每个数字，更新可能的和集合
        dp |= dp << num
    
    # 检查是否可以组成和为target的子集
    return (dp & (1 << target)) != 0

# 测试用例
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
    
    # 测试用例4
    nums4 = [2, 2, 3, 5]
    print(f"测试用例4结果: {can_partition(nums4)}")  # 预期输出: False
    
    # 测试用例5
    nums5 = [1, 2, 3, 4, 5, 6, 7]
    print(f"测试用例5结果: {can_partition(nums5)}")  # 预期输出: True