# LeetCode 416. 分割等和子集
# 题目描述：给你一个只包含正整数的非空数组 nums。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
# 链接：https://leetcode.cn/problems/partition-equal-subset-sum/
# 
# 解题思路：
# 这是一个0-1背包问题的变种。问题可以转化为：是否存在一个子集，其和等于整个数组和的一半。
# 
# 状态定义：dp[j] 表示是否能组成和为j的子集
# 状态转移方程：dp[j] = dp[j] || dp[j - nums[i]]
# 初始状态：dp[0] = True，表示空子集的和为0是可以组成的
# 
# 时间复杂度：O(n * target)，其中n是数组长度，target是数组和的一半
# 空间复杂度：O(target)，使用一维DP数组

def can_partition(nums: list[int]) -> bool:
    """
    判断是否可以将数组分割成两个和相等的子集
    
    参数:
        nums: 非空数组，只包含正整数
    
    返回:
        是否可以分割
    """
    if len(nums) < 2:
        return False
    
    # 计算数组总和
    total_sum = sum(nums)
    
    # 如果总和是奇数，不可能分成两个和相等的子集
    if total_sum % 2 != 0:
        return False
    
    # 目标和为总和的一半
    target = total_sum // 2
    
    # 创建DP数组，dp[j]表示是否能组成和为j的子集
    dp = [False] * (target + 1)
    
    # 初始状态：空子集的和为0是可以组成的
    dp[0] = True
    
    # 遍历每个数字
    for num in nums:
        # 逆序遍历，避免重复使用同一个数字
        for j in range(target, num - 1, -1):
            # 更新状态：不选当前数字 或 选当前数字（如果可以的话）
            dp[j] = dp[j] or dp[j - num]
    
    return dp[target]

def can_partition_2d(nums: list[int]) -> bool:
    """
    使用二维DP数组的版本（更直观但空间效率较低）
    
    参数:
        nums: 非空数组，只包含正整数
    
    返回:
        是否可以分割
    """
    if len(nums) < 2:
        return False
    
    # 计算数组总和
    total_sum = sum(nums)
    
    # 如果总和是奇数，不可能分成两个和相等的子集
    if total_sum % 2 != 0:
        return False
    
    # 目标和为总和的一半
    target = total_sum // 2
    n = len(nums)
    
    # 创建二维DP数组，dp[i][j]表示前i个数字是否能组成和为j的子集
    dp = [[False] * (target + 1) for _ in range(n + 1)]
    
    # 初始状态：空子集的和为0是可以组成的
    for i in range(n + 1):
        dp[i][0] = True
    
    # 填充DP数组
    for i in range(1, n + 1):
        for j in range(1, target + 1):
            # 不选第i个数字
            dp[i][j] = dp[i-1][j]
            
            # 选第i个数字（如果可以的话）
            if j >= nums[i-1]:
                dp[i][j] = dp[i][j] or dp[i-1][j - nums[i-1]]
    
    return dp[n][target]

def can_partition_optimized(nums: list[int]) -> bool:
    """
    优化的一维DP版本，提前处理一些边界情况
    
    参数:
        nums: 非空数组，只包含正整数
    
    返回:
        是否可以分割
    """
    if len(nums) < 2:
        return False
    
    # 计算数组总和
    total_sum = sum(nums)
    max_num = max(nums)
    
    # 如果总和是奇数，不可能分成两个和相等的子集
    if total_sum % 2 != 0:
        return False
    
    # 目标和为总和的一半
    target = total_sum // 2
    
    # 如果最大的数字大于目标和，不可能分割
    if max_num > target:
        return False
    
    # 创建DP数组
    dp = [False] * (target + 1)
    dp[0] = True
    
    for num in nums:
        for j in range(target, num - 1, -1):
            dp[j] = dp[j] or dp[j - num]
    
    return dp[target]

from functools import lru_cache

def can_partition_recursive(nums: list[int]) -> bool:
    """
    使用递归+记忆化搜索实现
    这个方法对于较大的输入可能会超时，但展示了递归的思路
    
    参数:
        nums: 非空数组，只包含正整数
    
    返回:
        是否可以分割
    """
    if len(nums) < 2:
        return False
    
    # 计算数组总和
    total_sum = sum(nums)
    
    # 如果总和是奇数，不可能分成两个和相等的子集
    if total_sum % 2 != 0:
        return False
    
    # 目标和为总和的一半
    target = total_sum // 2
    n = len(nums)
    
    @lru_cache(maxsize=None)
    def dfs(index: int, current_sum: int) -> bool:
        """
        递归辅助函数
        
        参数:
            index: 当前处理的索引
            current_sum: 当前子集和
        
        返回:
            是否能组成目标和
        """
        # 找到目标和
        if current_sum == target:
            return True
        
        # 超过目标和或处理完所有元素
        if current_sum > target or index == n:
            return False
        
        # 递归调用：选当前元素 或 不选当前元素
        return dfs(index + 1, current_sum + nums[index]) or dfs(index + 1, current_sum)
    
    return dfs(0, 0)

def can_partition_bit_set(nums: list[int]) -> bool:
    """
    使用位操作优化的版本
    对于较大的数组但元素值不大的情况，位操作可以更高效
    
    参数:
        nums: 非空数组，只包含正整数
    
    返回:
        是否可以分割
    """
    if len(nums) < 2:
        return False
    
    # 计算数组总和
    total_sum = sum(nums)
    
    # 如果总和是奇数，不可能分成两个和相等的子集
    if total_sum % 2 != 0:
        return False
    
    # 目标和为总和的一半
    target = total_sum // 2
    
    # 使用位集表示可达的和
    # bitset的第i位为1表示和为i是可达的
    bitset = 1  # 初始状态，和为0是可达的
    
    for num in nums:
        # 位操作：当前可达的和 | (之前可达的和 + 当前数字)
        # 使用位移操作，将之前的状态左移num位，然后与原来的状态进行或操作
        bitset |= bitset << num
        
        # 优化：如果已经能达到目标和，可以提前返回
        if bitset & (1 << target):
            return True
    
    # 检查目标和是否可达
    return bool(bitset & (1 << target))

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1, 5, 11, 5]
    print(f"测试用例1结果: {can_partition(nums1)}")  # 预期输出: True
    
    # 测试用例2
    nums2 = [1, 2, 3, 5]
    print(f"测试用例2结果: {can_partition(nums2)}")  # 预期输出: False
    
    # 测试不同实现
    print("\n测试不同实现:")
    print(f"二维DP版本 (测试用例1): {can_partition_2d(nums1)}")
    print(f"优化版本 (测试用例1): {can_partition_optimized(nums1)}")
    print(f"递归版本 (测试用例1): {can_partition_recursive(nums1)}")
    print(f"位操作版本 (测试用例1): {can_partition_bit_set(nums1)}")
    
    # 测试用例3
    nums3 = [1, 2, 3, 4, 5, 6, 7]
    print(f"\n测试用例3结果: {can_partition(nums3)}")  # 预期输出: True
    
    # 测试用例4
    nums4 = [100, 100, 100, 100, 100, 100, 100, 100]
    print(f"\n测试用例4结果: {can_partition(nums4)}")  # 预期输出: True
    
    # 测试用例5
    nums5 = [1, 2, 5]
    print(f"\n测试用例5结果: {can_partition(nums5)}")  # 预期输出: False