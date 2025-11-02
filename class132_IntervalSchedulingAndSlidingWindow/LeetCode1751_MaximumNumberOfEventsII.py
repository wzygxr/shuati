"""
LeetCode 1751. 最多可以参加的会议数目 II

题目描述：
给你一个 events 数组，其中 events[i] = [startDayi, endDayi, valuei] ，表示第 i 个会议在 startDayi 天开始，
第 endDayi 天结束，如果你参加这个会议，你能得到价值 valuei 。同时给你一个整数 k 表示你能参加的最多会议数目。
你同一时间只能参加一个会议。如果你选择参加某个会议，那么你必须完整地参加完这个会议。
会议结束日期是包含在会议内的，也就是说你不能同时参加一个开始日期与另一个结束日期相同的两个会议。
请你返回能得到的会议价值最大和。

示例：
输入：events = [[1,2,4],[3,4,3],[2,3,1]], k = 2
输出：7
解释：选择绿色的活动会议 0 和 1，得到总价值和为 4 + 3 = 7 。

解题思路：
这是一个典型的动态规划问题，类似于背包问题的变种。我们需要在有限的会议数量k下，选择价值最大的会议组合。

算法步骤：
1. 按照会议结束时间对所有会议进行排序
2. 使用动态规划，dp[i][j] 表示从前 i 个会议中最多参加 j 个会议所能获得的最大价值
3. 对于每个会议，我们可以选择参加或不参加
4. 如果参加，我们需要找到最后一个与其不冲突的会议，这可以通过二分查找实现
5. 状态转移方程：
   dp[i][j] = max(dp[i-1][j], dp[pre][j-1] + events[i][2])
   其中 pre 是最后一个与会议 i 不冲突的会议索引

时间复杂度：O(n * k + n * log n)
空间复杂度：O(n * k)

相关题目：
- LeetCode 1353. 最多可以参加的会议数目 (贪心解法)
- LeetCode 435. 无重叠区间 (贪心)
- LeetCode 646. 最长数对链 (动态规划 + 贪心)
"""

import bisect
from typing import List

def max_value(events: List[List[int]], k: int) -> int:
    """
    计算最多能参加k个会议获得的最大价值
    
    Args:
        events: 会议数组，每个元素为 [开始时间, 结束时间, 价值]
        k: 最多能参加的会议数量
    
    Returns:
        能获得的最大价值
    """
    n = len(events)
    # 按结束时间排序
    events.sort(key=lambda x: x[1])
    
    # dp[i][j] 表示前i个会议中最多参加j个会议的最大价值
    dp = [[0] * (k + 1) for _ in range(n)]
    
    # 初始化：第一个会议的情况
    for j in range(1, k + 1):
        dp[0][j] = events[0][2]
    
    # 填充dp表
    for i in range(1, n):
        # 找到最后一个与当前会议不冲突的会议索引
        # 使用二分查找找到结束时间小于当前会议开始时间的最右边的会议
        pre = find(events, i - 1, events[i][0])
        
        # 对于每个可能的会议数量j
        for j in range(1, k + 1):
            # 不参加当前会议 vs 参加当前会议
            dp[i][j] = max(
                dp[i - 1][j], 
                (0 if pre == -1 else dp[pre][j - 1]) + events[i][2]
            )
    
    return dp[n - 1][k]


def find(events: List[List[int]], right: int, s: int) -> int:
    """
    使用二分查找找到结束时间小于s的最右边的会议
    
    Args:
        events: 会议数组
        right: 搜索范围的右边界
        s: 目标开始时间
    
    Returns:
        最后一个结束时间小于s的会议索引，如果不存在返回-1
    """
    left = 0
    ans = -1
    
    while left <= right:
        mid = (left + right) // 2
        # 如果当前会议的结束时间小于s，可能是我们要找的会议
        if events[mid][1] < s:
            ans = mid
            left = mid + 1
        else:
            right = mid - 1
    
    return ans


# 测试用例
if __name__ == "__main__":
    # 测试用例1
    events1 = [[1,2,4],[3,4,3],[2,3,1]]
    k1 = 2
    print("测试用例1:")
    print("输入: events = [[1,2,4],[3,4,3],[2,3,1]], k = 2")
    print("输出:", max_value(events1, k1))  # 期望输出: 7
    
    # 测试用例2
    events2 = [[1,2,4],[3,4,3],[2,3,10]]
    k2 = 2
    print("\n测试用例2:")
    print("输入: events = [[1,2,4],[3,4,3],[2,3,10]], k = 2")
    print("输出:", max_value(events2, k2))  # 期望输出: 14
    
    # 测试用例3
    events3 = [[1,1,1],[2,2,2],[3,3,3],[4,4,4]]
    k3 = 3
    print("\n测试用例3:")
    print("输入: events = [[1,1,1],[2,2,2],[3,3,3],[4,4,4]], k = 3")
    print("输出:", max_value(events3, k3))  # 期望输出: 9