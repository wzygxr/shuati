# LeetCode 1986. 完成任务的最少工作时间段
# 给你一个任务数组 tasks ，其中 tasks[i] 是一个正整数，表示第 i 个任务的持续时间。
# 同时给你一个正整数 sessionTime ，表示单个会话中可以完成任务的最长时间。
# 你可以按照任意顺序完成任务。
# 返回完成所有任务所需的最少会话数。
# 测试链接 : https://leetcode.cn/problems/minimum-number-of-work-sessions-to-finish-the-tasks/

"""
算法详解：完成任务的最少工作时间段（LeetCode 1986）

问题描述：
给你一个任务数组 tasks ，其中 tasks[i] 是一个正整数，表示第 i 个任务的持续时间。
同时给你一个正整数 sessionTime ，表示单个会话中可以完成任务的最长时间。
你可以按照任意顺序完成任务。
返回完成所有任务所需的最少会话数。

算法思路：
这是一个典型的分组覆盖问题，可以使用状态压缩动态规划解决。
1. 使用状态压缩表示任务选择状态
2. 预处理每个状态是否可以在一个会话内完成
3. 使用动态规划计算完成所有任务的最少会话数

时间复杂度分析：
1. 预处理所有状态：O(2^n * n)
2. 动态规划：O(2^n * n)
3. 总体时间复杂度：O(2^n * n)

空间复杂度分析：
1. 状态数组：O(2^n)
2. dp数组：O(2^n)
3. 总体空间复杂度：O(2^n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 边界处理：正确处理空任务列表的情况
3. 性能优化：预处理可在一个会话内完成的状态
4. 状态压缩：使用位运算优化状态表示

极端场景验证：
1. 任务数量达到14个（题目限制）的情况
2. 所有任务时间都等于会话时间的情况
3. 所有任务时间都远小于会话时间的情况
4. 所有任务时间都接近会话时间的情况
"""

def minSessions(tasks, sessionTime):
    """
    计算完成所有任务所需的最少会话数
    
    Args:
        tasks (List[int]): 任务持续时间列表
        sessionTime (int): 单个会话的最大时间
    
    Returns:
        int: 最少会话数
    """
    # 异常处理：检查输入参数的有效性
    if not tasks:
        return 0
    
    if sessionTime <= 0:
        raise ValueError("会话时间必须为正整数")
    
    n = len(tasks)
    
    # 限制检查：根据题目描述，任务数不超过14
    if n > 14:
        raise ValueError("任务数不能超过14个")
    
    # 预处理：计算每个状态是否可以在一个会话内完成
    valid = [False] * (1 << n)
    for mask in range(1 << n):
        total_time = 0
        for i in range(n):
            # 检查第i个任务是否被选中
            if (mask & (1 << i)) != 0:
                total_time += tasks[i]
        # 如果总时间不超过会话时间，则该状态有效
        valid[mask] = total_time <= sessionTime
    
    # dp[mask] 表示完成任务状态为mask时所需的最少会话数
    dp = [float('inf')] * (1 << n)
    # 空状态需要0个会话
    dp[0] = 0
    
    # 动态规划填表
    for mask in range(1, 1 << n):
        # 枚举mask的所有子集
        subset = mask
        while subset > 0:
            # 如果子集可以在一个会话内完成
            if valid[subset]:
                # 更新状态：dp[mask] = min(dp[mask], dp[mask ^ subset] + 1)
                # mask ^ subset 表示从mask中去掉subset后的状态
                if dp[mask ^ subset] != float('inf'):
                    dp[mask] = min(dp[mask], dp[mask ^ subset] + 1)
            # 枚举下一个子集
            subset = (subset - 1) & mask
    
    # 返回完成所有任务的最少会话数
    return dp[(1 << n) - 1]

# 优化版本：使用记忆化搜索
def minSessionsOptimized(tasks, sessionTime):
    """
    计算完成所有任务所需的最少会话数（优化版本）
    
    Args:
        tasks (List[int]): 任务持续时间列表
        sessionTime (int): 单个会话的最大时间
    
    Returns:
        int: 最少会话数
    """
    # 异常处理：检查输入参数的有效性
    if not tasks:
        return 0
    
    if sessionTime <= 0:
        raise ValueError("会话时间必须为正整数")
    
    n = len(tasks)
    
    # 限制检查：根据题目描述，任务数不超过14
    if n > 14:
        raise ValueError("任务数不能超过14个")
    
    # 记忆化缓存
    memo = {}
    valid_cache = {}
    
    def dfs(mask):
        # 基础情况：空状态
        if mask == 0:
            return 0
        
        # 记忆化：如果已经计算过，直接返回结果
        if mask in memo:
            return memo[mask]
        
        # 计算当前状态是否可以在一个会话内完成
        if mask not in valid_cache:
            total_time = 0
            for i in range(n):
                # 检查第i个任务是否被选中
                if (mask & (1 << i)) != 0:
                    total_time += tasks[i]
            # 如果总时间不超过会话时间，则该状态有效
            valid_cache[mask] = total_time <= sessionTime
        
        # 如果当前状态可以在一个会话内完成，直接返回1
        if valid_cache[mask]:
            memo[mask] = 1
            return 1
        
        # 枚举所有子集，找到最优解
        result = float('inf')
        subset = mask
        while subset > 0:
            # 检查子集是否可以在一个会话内完成
            if subset not in valid_cache:
                total_time = 0
                for i in range(n):
                    # 检查第i个任务是否被选中
                    if (subset & (1 << i)) != 0:
                        total_time += tasks[i]
                # 如果总时间不超过会话时间，则该状态有效
                valid_cache[subset] = total_time <= sessionTime
            
            if valid_cache[subset]:
                # 递归计算剩余任务的最少会话数
                result = min(result, 1 + dfs(mask ^ subset))
            
            # 枚举下一个子集
            subset = (subset - 1) & mask
        
        # 缓存结果
        memo[mask] = result
        return result
    
    # 记忆化搜索
    return dfs((1 << n) - 1)

# 测试方法
if __name__ == "__main__":
    # 测试用例1
    tasks1 = [1, 2, 3]
    sessionTime1 = 3
    print(f"Test 1 (DP method): {minSessions(tasks1, sessionTime1)}")
    print(f"Test 1 (DFS method): {minSessionsOptimized(tasks1, sessionTime1)}")
    # 期望输出: 2
    
    # 测试用例2
    tasks2 = [3, 1, 3, 1, 1]
    sessionTime2 = 8
    print(f"Test 2 (DP method): {minSessions(tasks2, sessionTime2)}")
    print(f"Test 2 (DFS method): {minSessionsOptimized(tasks2, sessionTime2)}")
    # 期望输出: 2
    
    # 测试用例3
    tasks3 = [1, 2, 3, 4, 5]
    sessionTime3 = 15
    print(f"Test 3 (DP method): {minSessions(tasks3, sessionTime3)}")
    print(f"Test 3 (DFS method): {minSessionsOptimized(tasks3, sessionTime3)}")
    # 期望输出: 1
    
    # 测试用例4
    tasks4 = []
    sessionTime4 = 5
    print(f"Test 4 (DP method): {minSessions(tasks4, sessionTime4)}")
    print(f"Test 4 (DFS method): {minSessionsOptimized(tasks4, sessionTime4)}")
    # 期望输出: 0