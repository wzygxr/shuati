"""
LeetCode 1235. Maximum Profit in Job Scheduling

题目描述：
你有 n 个工作，每个工作有开始时间 startTime[i]，结束时间 endTime[i] 和利润 profit[i]。
你需要选择一个工作子集，使得总利润最大化，且所选工作的时间范围不重叠。
注意：如果一个工作在时间 X 结束，另一个工作可以在时间 X 开始（它们不重叠）。

解题思路：
这是一个经典的动态规划问题，类似于背包问题的变种。我们需要在有限的工作选择下，选择利润最大的工作组合。

算法步骤：
1. 将所有工作按开始时间排序
2. 使用动态规划，定义 dfs(i) 表示从第 i 个工作开始能得到的最大利润
3. 对于每个工作，我们可以选择做或不做
4. 如果做，我们需要找到下一个不冲突的工作，这可以通过二分查找实现
5. 状态转移方程：
   dfs(i) = max(dfs(i+1), profit[i] + dfs(j))
   其中 j 是第一个开始时间 >= 当前工作结束时间的工作索引

时间复杂度：O(n * log n)
空间复杂度：O(n)

相关题目：
- LeetCode 1751. 最多可以参加的会议数目 II (动态规划 + 二分查找)
- LeetCode 435. 无重叠区间 (贪心)
- LeetCode 646. 最长数对链 (贪心)
"""

import bisect

def jobScheduling(startTime, endTime, profit):
    """
    计算最大利润
    
    Args:
        startTime: 工作开始时间列表
        endTime: 工作结束时间列表
        profit: 工作利润列表
    
    Returns:
        能获得的最大利润
    """
    n = len(startTime)
    
    # 创建工作列表并按开始时间排序
    jobs = list(zip(startTime, endTime, profit))
    jobs.sort(key=lambda x: x[0])
    
    # dp[i] 表示从第 i 个工作开始能得到的最大利润
    dp = [0] * (n + 1)
    
    # 从后往前填充 dp 数组
    for i in range(n - 1, -1, -1):
        # 不选择当前工作
        skip = dp[i + 1]
        
        # 选择当前工作，找到下一个不冲突的工作
        # 使用二分查找找到第一个开始时间 >= 当前工作结束时间的工作
        target = jobs[i][1]  # 当前工作结束时间
        # 提取所有工作的开始时间用于二分查找
        start_times = [job[0] for job in jobs]
        next_job_index = bisect.bisect_left(start_times, target, i + 1)
        take = jobs[i][2] + dp[next_job_index]  # jobs[i][2] 是利润
        
        # 取最大值
        dp[i] = max(skip, take)
    
    return dp[0]

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    startTime1 = [1, 2, 3, 3]
    endTime1 = [3, 4, 5, 6]
    profit1 = [50, 10, 40, 70]
    print("测试用例1:")
    print(f"输入: startTime = {startTime1}, endTime = {endTime1}, profit = {profit1}")
    print(f"输出: {jobScheduling(startTime1, endTime1, profit1)}")  # 期望输出: 120
    
    # 测试用例2
    startTime2 = [1, 2, 3, 4, 6]
    endTime2 = [3, 5, 10, 6, 9]
    profit2 = [20, 20, 100, 70, 60]
    print("\n测试用例2:")
    print(f"输入: startTime = {startTime2}, endTime = {endTime2}, profit = {profit2}")
    print(f"输出: {jobScheduling(startTime2, endTime2, profit2)}")  # 期望输出: 150
    
    # 测试用例3
    startTime3 = [1, 1, 1]
    endTime3 = [2, 3, 4]
    profit3 = [5, 6, 4]
    print("\n测试用例3:")
    print(f"输入: startTime = {startTime3}, endTime = {endTime3}, profit = {profit3}")
    print(f"输出: {jobScheduling(startTime3, endTime3, profit3)}")  # 期望输出: 6