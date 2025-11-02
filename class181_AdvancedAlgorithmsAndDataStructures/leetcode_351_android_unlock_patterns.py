#!/usr/bin/env python3
"""
LeetCode 351. 安卓系统手势解锁 (Android Unlock Patterns) - Python版本

题目来源：https://leetcode.cn/problems/android-unlock-patterns/

题目描述：
我们都知道安卓有个手势解锁的界面，是一个 3 x 3 的点所绘制出来的网格。
用户可以设置一个 "解锁模式"，通过连接特定序列中的点，形成一个解锁手势。
一个有效的解锁模式需要满足以下两个条件：
1. 所有点是不同的
2. 如果两个点之间有其他点，则必须先经过这些点

算法思路：
这是一个回溯算法问题，可以使用以下方法解决：
1. 回溯法：枚举所有可能的路径
2. 动态规划：记忆化搜索优化
3. 状态压缩：使用位运算优化空间

虽然这不是经典的生命游戏问题，但可以看作是在一个网格上模拟状态变化的问题，
与生命游戏有相似的网格状态处理思想。

时间复杂度：
- 回溯法：O(9!)
- 空间复杂度：O(9)

应用场景：
1. 密码学：模式识别和安全验证
2. 游戏开发：路径搜索和状态机
3. 图论：哈密顿路径问题

相关题目：
1. LeetCode 289. 生命游戏
2. LeetCode 79. 单词搜索
3. LeetCode 212. 单词搜索 II
"""

import time

# 记录两个点之间必须经过的中间点
skip = [[0 for _ in range(10)] for _ in range(10)]

# 初始化跳跃规则
skip[1][3] = skip[3][1] = 2
skip[1][7] = skip[7][1] = 4
skip[3][9] = skip[9][3] = 6
skip[7][9] = skip[9][7] = 8
skip[1][9] = skip[9][1] = skip[3][7] = skip[7][3] = skip[2][8] = skip[8][2] = skip[4][6] = skip[6][4] = 5

def number_of_patterns_backtrack(m, n):
    """
    方法1：回溯法
    时间复杂度：O(9!)
    空间复杂度：O(9)
    :param m: 最小步数
    :param n: 最大步数
    :return: 满足条件的解锁模式数量
    """
    visited = [False] * 10
    
    def dfs_backtrack(current, visited, length, m, n):
        """
        回溯搜索函数
        :param current: 当前位置
        :param visited: 已访问位置数组
        :param length: 当前路径长度
        :param m: 最小步数
        :param n: 最大步数
        :return: 从当前位置开始的有效路径数量
        """
        if length > n:
            return 0
        
        count = 0
        # 如果当前长度满足要求，计数加1
        if length >= m:
            count += 1
        
        # 标记当前位置为已访问
        visited[current] = True
        
        # 尝试移动到下一个位置
        for next_pos in range(1, 10):
            # 如果下一个位置已访问，跳过
            if visited[next_pos]:
                continue
            
            # 检查是否可以移动到下一个位置
            skip_point = skip[current][next_pos]
            # 如果不需要跳跃，或者跳跃点已被访问，可以移动
            if skip_point == 0 or visited[skip_point]:
                count += dfs_backtrack(next_pos, visited, length + 1, m, n)
        
        # 回溯：取消标记当前位置
        visited[current] = False
        
        return count
    
    count = 0
    # 从1,2,5开始搜索（利用对称性优化）
    # 1,3,7,9是对称的，2,4,6,8是对称的
    count += dfs_backtrack(1, visited, 1, m, n) * 4  # 1,3,7,9
    count += dfs_backtrack(2, visited, 1, m, n) * 4  # 2,4,6,8
    count += dfs_backtrack(5, visited, 1, m, n)      # 5
    
    return count

def number_of_patterns_memoization(m, n):
    """
    方法2：带记忆化的回溯法
    时间复杂度：O(9 * 2^9)
    空间复杂度：O(2^9)
    :param m: 最小步数
    :param n: 最大步数
    :return: 满足条件的解锁模式数量
    """
    # 使用位掩码表示已访问的点
    memo = [[-1 for _ in range(10)] for _ in range(1 << 9)]
    
    def dfs_memoization(current, visited_mask, length, m, n, memo):
        """
        带记忆化的回溯搜索函数
        :param current: 当前位置
        :param visited_mask: 已访问位置的位掩码
        :param length: 当前路径长度
        :param m: 最小步数
        :param n: 最大步数
        :param memo: 记忆化数组
        :return: 从当前位置开始的有效路径数量
        """
        if length > n:
            return 0
        
        # 如果已经计算过，直接返回结果
        if memo[visited_mask][current] != -1:
            return memo[visited_mask][current]
        
        count = 0
        # 如果当前长度满足要求，计数加1
        if length >= m:
            count += 1
        
        # 尝试移动到下一个位置
        for next_pos in range(1, 10):
            # 如果下一个位置已访问，跳过
            if (visited_mask & (1 << (next_pos - 1))) != 0:
                continue
            
            # 检查是否可以移动到下一个位置
            skip_point = skip[current][next_pos]
            # 如果不需要跳跃，或者跳跃点已被访问，可以移动
            if skip_point == 0 or (visited_mask & (1 << (skip_point - 1))) != 0:
                count += dfs_memoization(next_pos, visited_mask | (1 << (next_pos - 1)), length + 1, m, n, memo)
        
        # 记忆化结果
        memo[visited_mask][current] = count
        return count
    
    count = 0
    # 从1,2,5开始搜索（利用对称性优化）
    count += dfs_memoization(1, 1 << (1-1), 1, m, n, memo) * 4  # 1,3,7,9
    count += dfs_memoization(2, 1 << (2-1), 1, m, n, memo) * 4  # 2,4,6,8
    count += dfs_memoization(5, 1 << (5-1), 1, m, n, memo)      # 5
    
    return count

def number_of_patterns_dp(m, n):
    """
    方法3：动态规划解法
    时间复杂度：O(n * 9 * 2^9)
    空间复杂度：O(9 * 2^9)
    :param m: 最小步数
    :param n: 最大步数
    :return: 满足条件的解锁模式数量
    """
    # dp[mask][last] 表示使用mask表示的点集，以last结尾的路径数量
    dp = [[0 for _ in range(10)] for _ in range(1 << 9)]
    
    # 初始化：每个点作为起点的路径数量为1
    for i in range(1, 10):
        dp[1 << (i - 1)][i] = 1
    
    result = 0
    
    # 按照路径长度进行动态规划
    for length in range(1, n + 1):
        # 计算长度为length的路径数量
        if length >= m:
            for mask in range(1 << 9):
                for last in range(1, 10):
                    if (mask & (1 << (last - 1))) != 0:
                        result += dp[mask][last]
        
        # 如果还没到最大长度，继续扩展
        if length < n:
            new_dp = [[0 for _ in range(10)] for _ in range(1 << 9)]
            
            for mask in range(1 << 9):
                for last in range(1, 10):
                    if dp[mask][last] > 0:
                        # 尝试从last移动到下一个点
                        for next_pos in range(1, 10):
                            # 如果下一个点已访问，跳过
                            if (mask & (1 << (next_pos - 1))) != 0:
                                continue
                            
                            # 检查是否可以移动到下一个位置
                            skip_point = skip[last][next_pos]
                            # 如果不需要跳跃，或者跳跃点已被访问，可以移动
                            if skip_point == 0 or (mask & (1 << (skip_point - 1))) != 0:
                                new_dp[mask | (1 << (next_pos - 1))][next_pos] += dp[mask][last]
            
            dp = new_dp
    
    return result

def test_android_unlock_patterns():
    """测试函数"""
    print("=== 测试 LeetCode 351. 安卓系统手势解锁 ===")
    
    # 测试用例1
    m1, n1 = 1, 1
    print("测试用例1:")
    print("m:", m1, ", n:", n1)
    print("回溯法结果:", number_of_patterns_backtrack(m1, n1))
    print("记忆化回溯法结果:", number_of_patterns_memoization(m1, n1))
    print("动态规划法结果:", number_of_patterns_dp(m1, n1))
    print("期望结果: 9")
    print()
    
    # 测试用例2
    m2, n2 = 1, 2
    print("测试用例2:")
    print("m:", m2, ", n:", n2)
    print("回溯法结果:", number_of_patterns_backtrack(m2, n2))
    print("记忆化回溯法结果:", number_of_patterns_memoization(m2, n2))
    print("动态规划法结果:", number_of_patterns_dp(m2, n2))
    print("期望结果: 65")
    print()
    
    # 测试用例3
    m3, n3 = 2, 3
    print("测试用例3:")
    print("m:", m3, ", n:", n3)
    print("回溯法结果:", number_of_patterns_backtrack(m3, n3))
    print("记忆化回溯法结果:", number_of_patterns_memoization(m3, n3))
    print("动态规划法结果:", number_of_patterns_dp(m3, n3))
    print()
    
    # 性能测试
    print("=== 性能测试 ===")
    m, n = 3, 7
    
    start_time = time.time()
    result1 = number_of_patterns_backtrack(m, n)
    end_time = time.time()
    print("回溯法计算m={},n={}时间: {:.2f} ms, 结果: {}".format(
        m, n, (end_time - start_time) * 1000, result1))
    
    start_time = time.time()
    result2 = number_of_patterns_memoization(m, n)
    end_time = time.time()
    print("记忆化回溯法计算m={},n={}时间: {:.2f} ms, 结果: {}".format(
        m, n, (end_time - start_time) * 1000, result2))
    
    start_time = time.time()
    result3 = number_of_patterns_dp(m, n)
    end_time = time.time()
    print("动态规划法计算m={},n={}时间: {:.2f} ms, 结果: {}".format(
        m, n, (end_time - start_time) * 1000, result3))

if __name__ == "__main__":
    test_android_unlock_patterns()