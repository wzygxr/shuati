# 石子合并
# 在一条直线上有n堆石子，每堆有一个重量。现在要合并这些石子成为一堆，
# 每次只能合并相邻的两堆石子，合并的代价为这两堆石子的重量之和。
# 求合并所有石子的最小代价。
# 测试链接 : https://www.luogu.com.cn/problem/P1880

import sys

def stone_merge(stones):
    """
    区间动态规划解法
    时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
    空间复杂度: O(n^2) - dp数组占用空间
    解题思路:
    1. 状态定义：minDp[i][j]表示合并区间[i,j]石子的最小代价，maxDp[i][j]表示最大代价
    2. 状态转移：枚举分割点k，minDp[i][j] = min(minDp[i][k] + minDp[k+1][j]) + sum[i][j]
    3. 前缀和优化：使用前缀和快速计算区间和
    """
    n = len(stones)
    
    # 计算前缀和
    pre_sum = [0] * (n + 1)
    for i in range(1, n + 1):
        pre_sum[i] = pre_sum[i - 1] + stones[i - 1]
    
    # minDp[i][j]表示合并区间[i,j]石子的最小代价
    min_dp = [[0] * (n + 1) for _ in range(n + 1)]
    # maxDp[i][j]表示合并区间[i,j]石子的最大代价
    max_dp = [[0] * (n + 1) for _ in range(n + 1)]
    
    # 枚举区间长度，从2开始（至少要有2堆石子才能合并）
    for length in range(2, n + 1):
        # 枚举区间起点i
        for i in range(1, n - length + 2):
            # 计算区间终点j
            j = i + length - 1
            min_dp[i][j] = sys.maxsize
            max_dp[i][j] = -sys.maxsize - 1
            
            # 枚举分割点k
            for k in range(i, j):
                # 计算区间[i,j]的石子重量和
                sum_val = pre_sum[j] - pre_sum[i - 1]
                
                # 更新最小代价
                min_dp[i][j] = min(min_dp[i][j], 
                                   min_dp[i][k] + min_dp[k + 1][j] + sum_val)
                
                # 更新最大代价
                max_dp[i][j] = max(max_dp[i][j], 
                                   max_dp[i][k] + max_dp[k + 1][j] + sum_val)
    
    return min_dp[1][n], max_dp[1][n]


if __name__ == "__main__":
    # 读取输入
    n = int(input().strip())
    stones = list(map(int, input().strip().split()))
    
    # 计算结果
    min_cost, max_cost = stone_merge(stones)
    
    # 输出结果
    print(min_cost)  # 最小代价
    print(max_cost)  # 最大代价