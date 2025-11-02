# LeetCode 494. 目标和
# 题目描述：给你一个整数数组 nums 和一个整数 target 。
# 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：
# 例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，然后串联起来得到表达式 "+2-1" 。
# 返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。
# 链接：https://leetcode.cn/problems/target-sum/
# 
# 解题思路：
# 这是一个0-1背包问题的变种。我们可以将问题转化为：找到一个子集P，使得sum(P) - sum(N) = target，其中N是数组中不在P中的元素。
# 可以证明：sum(P) - sum(N) = target => sum(P) = (sum(nums) + target) / 2
# 因此，问题转化为：在数组nums中，有多少个子集的和等于(sum(nums) + target) / 2。
# 
# 状态定义：dp[j] 表示和为j的子集数目
# 状态转移方程：dp[j] += dp[j - nums[i]]，其中nums[i]是当前元素，且j >= nums[i]
# 初始状态：dp[0] = 1，表示和为0的子集数目为1（空集）
# 
# 时间复杂度：O(n * sum)，其中n是数组的长度，sum是数组元素的和
# 空间复杂度：O(sum)，使用一维DP数组

def find_target_sum_ways(nums: list[int], target: int) -> int:
    """
    计算有多少种不同的方法构造运算结果等于target的表达式
    
    参数:
        nums: 整数数组
        target: 目标值
    
    返回:
        不同表达式的数目
    """
    n = len(nums)
    if n == 0:
        return 1 if target == 0 else 0
    
    # 计算数组元素的总和
    total_sum = sum(nums)
    
    # 检查是否有解
    # 1. total_sum < abs(target)：总和小于目标值的绝对值，无解
    # 2. (total_sum + target) % 2 != 0：sum + target必须是偶数，否则无法平均分成两部分
    if total_sum < abs(target) or (total_sum + target) % 2 != 0:
        return 0
    
    # 计算目标和：sum(P) = (total_sum + target) // 2
    target_sum = (total_sum + target) // 2
    if target_sum < 0:
        return 0  # 目标和为负数，无解
    
    # 创建DP数组，dp[j]表示和为j的子集数目
    dp = [0] * (target_sum + 1)
    
    # 初始状态：和为0的子集数目为1（空集）
    dp[0] = 1
    
    # 填充DP数组
    for num in nums:
        # 注意：这里我们从后往前遍历，避免重复使用同一个元素
        for j in range(target_sum, num - 1, -1):
            dp[j] += dp[j - num]
    
    return dp[target_sum]

def find_target_sum_ways_2d(nums: list[int], target: int) -> int:
    """
    使用二维DP数组的实现
    dp[i][j]表示使用前i个元素，和为j的子集数目
    """
    n = len(nums)
    if n == 0:
        return 1 if target == 0 else 0
    
    total_sum = sum(nums)
    
    if total_sum < abs(target) or (total_sum + target) % 2 != 0:
        return 0
    
    target_sum = (total_sum + target) // 2
    if target_sum < 0:
        return 0
    
    # 创建二维DP数组
    dp = [[0] * (target_sum + 1) for _ in range(n + 1)]
    
    # 初始状态：使用前0个元素，和为0的子集数目为1（空集）
    dp[0][0] = 1
    
    # 填充DP数组
    for i in range(1, n + 1):
        num = nums[i - 1]  # 当前元素
        for j in range(target_sum + 1):
            # 不选择当前元素
            dp[i][j] = dp[i - 1][j]
            # 选择当前元素（如果j >= num）
            if j >= num:
                dp[i][j] += dp[i - 1][j - num]
    
    return dp[n][target_sum]

def find_target_sum_ways_backtrack(nums: list[int], target: int) -> int:
    """
    使用回溯法的实现
    注意：这种方法对于大数组可能效率不高
    """
    count = [0]  # 使用列表来存储结果，以便在递归中修改
    
    def backtrack(index, current_sum):
        # 已经处理完所有元素
        if index == len(nums):
            if current_sum == target:
                count[0] += 1
            return
        
        # 尝试加上当前元素
        backtrack(index + 1, current_sum + nums[index])
        
        # 尝试减去当前元素
        backtrack(index + 1, current_sum - nums[index])
    
    backtrack(0, 0)
    return count[0]

def find_target_sum_ways_memo(nums: list[int], target: int) -> int:
    """
    使用记忆化递归的实现
    """
    # 创建记忆化缓存，键为(index, current_sum)，值为对应的方法数
    memo = {}
    
    def backtrack_memo(index, current_sum):
        # 已经处理完所有元素
        if index == len(nums):
            return 1 if current_sum == target else 0
        
        # 生成缓存键
        key = (index, current_sum)
        
        # 检查是否已经计算过
        if key in memo:
            return memo[key]
        
        # 计算两种情况的结果之和
        add = backtrack_memo(index + 1, current_sum + nums[index])
        subtract = backtrack_memo(index + 1, current_sum - nums[index])
        
        # 存储结果到缓存
        memo[key] = add + subtract
        
        return add + subtract
    
    return backtrack_memo(0, 0)

def find_target_sum_ways_optimized(nums: list[int], target: int) -> int:
    """
    优化版本，考虑数组中包含0的情况
    """
    n = len(nums)
    if n == 0:
        return 1 if target == 0 else 0
    
    total_sum = 0
    zero_count = 0
    
    # 计算总和和0的个数
    for num in nums:
        total_sum += num
        if num == 0:
            zero_count += 1
    
    # 检查是否有解
    if total_sum < abs(target) or (total_sum + target) % 2 != 0:
        return 0
    
    target_sum = (total_sum + target) // 2
    if target_sum < 0:
        return 0
    
    # 过滤掉0，单独处理
    non_zero_nums = [num for num in nums if num != 0]
    
    # 创建DP数组
    dp = [0] * (target_sum + 1)
    dp[0] = 1
    
    # 填充DP数组
    for num in non_zero_nums:
        for j in range(target_sum, num - 1, -1):
            dp[j] += dp[j - num]
    
    # 每个0有两种选择（+0或-0），所以总方法数乘以2^zero_count
    return dp[target_sum] * (2 ** zero_count)

def print_all_expressions(nums: list[int], target: int) -> list[str]:
    """
    打印所有可能的表达式
    注意：这个方法仅用于教学目的，对于大数组可能效率不高
    
    参数:
        nums: 整数数组
        target: 目标值
    
    返回:
        所有可能的表达式列表
    """
    result = []
    
    def backtrack_expressions(index, current_sum, current_expr):
        if index == len(nums):
            if current_sum == target:
                result.append(current_expr)
            return
        
        num = nums[index]
        
        # 尝试加上当前元素
        backtrack_expressions(index + 1, current_sum + num,
                            current_expr + '+' + str(num))
        
        # 尝试减去当前元素
        backtrack_expressions(index + 1, current_sum - num,
                            current_expr + '-' + str(num))
    
    # 第一个数特殊处理，不需要前面的符号
    if nums:
        backtrack_expressions(1, nums[0], str(nums[0]))
        
        # 尝试第一个数为负数的情况
        backtrack_expressions(1, -nums[0], '-' + str(nums[0]))
    
    print("所有可能的表达式:")
    for expr in result:
        print(expr)
    print(f"总共有 {len(result)} 种不同的表达式。")
    
    return result

def find_target_sum_ways_bfs(nums: list[int], target: int) -> int:
    """
    使用广度优先搜索(BFS)的实现
    """
    from collections import defaultdict
    
    # BFS队列，存储当前索引和当前和
    queue = defaultdict(int)
    queue[0] = 1  # 初始状态：和为0，方法数为1
    
    # 逐个处理数组元素
    for num in nums:
        next_queue = defaultdict(int)
        for current_sum, count in queue.items():
            # 加上当前元素
            next_queue[current_sum + num] += count
            # 减去当前元素
            next_queue[current_sum - num] += count
        queue = next_queue
    
    # 返回目标和对应的方法数
    return queue.get(target, 0)

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1, 1, 1, 1, 1]
    target1 = 3
    print(f"测试用例1结果: {find_target_sum_ways(nums1, target1)} (预期: 5)")
    print_all_expressions(nums1, target1)
    print("---------------------------")
    
    # 测试用例2
    nums2 = [1]
    target2 = 1
    print(f"测试用例2结果: {find_target_sum_ways(nums2, target2)} (预期: 1)")
    print("---------------------------")
    
    # 测试用例3
    nums3 = [1, 2, 3, 4, 5]
    target3 = 3
    print(f"测试用例3结果: {find_target_sum_ways(nums3, target3)} (预期: 5)")
    print("---------------------------")
    
    # 测试用例4 - 无法满足的情况
    nums4 = [1, 2, 3]
    target4 = 7
    print(f"测试用例4结果: {find_target_sum_ways(nums4, target4)} (预期: 0)")
    
    # 测试用例5 - 包含0的情况
    nums5 = [0, 0, 0, 0, 0, 0, 0, 0, 1]
    target5 = 1
    print(f"\n测试用例5结果 (包含0): {find_target_sum_ways(nums5, target5)} (预期: 256)")
    
    # 测试各种实现方法
    print("\n测试各种实现方法:")
    print(f"二维DP版本: {find_target_sum_ways_2d(nums1, target1)}")
    print(f"回溯版本: {find_target_sum_ways_backtrack(nums1, target1)}")
    print(f"记忆化递归版本: {find_target_sum_ways_memo(nums1, target1)}")
    print(f"优化版本: {find_target_sum_ways_optimized(nums1, target1)}")
    print(f"BFS版本: {find_target_sum_ways_bfs(nums1, target1)}")
    
    # 测试大数组性能对比
    print("\n测试大数组性能对比:")
    import time
    
    # 创建一个较大的测试用例
    large_nums = [1] * 20  # 20个1
    large_target = 10
    
    # 测试DP版本
    start_time = time.time()
    dp_result = find_target_sum_ways(large_nums, large_target)
    dp_time = time.time() - start_time
    print(f"DP版本结果: {dp_result}, 耗时: {dp_time:.6f}秒")
    
    # 测试优化版本
    start_time = time.time()
    optimized_result = find_target_sum_ways_optimized(large_nums, large_target)
    optimized_time = time.time() - start_time
    print(f"优化版本结果: {optimized_result}, 耗时: {optimized_time:.6f}秒")
    
    # 注意：回溯版本在大数组上可能需要很长时间，这里不进行测试