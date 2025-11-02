# UVa 10003 Cutting Sticks
# 题目来源：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=944
# 题目大意：有一根长度为L的木棍，上面有n个切割点。每次切割的费用等于当前木棍的长度。
# 要求找出切割所有切割点的最小总费用。
#
# 解题思路：
# 1. 这是一个经典的区间动态规划问题，类似于石子合并问题
# 2. dp[i][j]表示切割区间[i,j]内所有切割点的最小费用
# 3. 状态转移：枚举最后一个切割点k，dp[i][j] = min(dp[i][k] + dp[k][j] + (cuts[j] - cuts[i]))
# 4. 需要将切割点排序，并在两端添加0和L作为边界
#
# 时间复杂度：O(n^3) - 三层循环：区间长度、区间起点、分割点
# 空间复杂度：O(n^2) - dp数组占用空间
#
# 工程化考虑：
# 1. 输入验证：检查输入是否合法
# 2. 边界处理：处理没有切割点的特殊情况
# 3. 排序处理：确保切割点按顺序排列
# 4. 异常处理：对于不合法输入给出适当提示

import sys

def solve(cuts, n):
    """
    主函数：解决切木棍问题
    时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
    空间复杂度: O(n^2) - dp数组占用空间
    """
    # dp[i][j]表示切割区间[i,j]内所有切割点的最小费用
    dp = [[0] * n for _ in range(n)]
    
    # 枚举区间长度，从2开始（至少需要两个端点）
    for length in range(2, n):
        # 枚举区间起点i
        for i in range(n - length):
            # 计算区间终点j
            j = i + length
            dp[i][j] = float('inf')
            
            # 枚举最后一个切割点k
            for k in range(i + 1, j):
                # 状态转移方程
                # dp[i][k]：切割左半部分的费用
                # dp[k][j]：切割右半部分的费用
                # cuts[j] - cuts[i]：当前切割的费用（当前木棍长度）
                dp[i][j] = min(dp[i][j], 
                               dp[i][k] + dp[k][j] + (cuts[j] - cuts[i]))
    
    return dp[0][n - 1]

if __name__ == "__main__":
    # 读取输入
    try:
        while True:
            line = input().strip()
            if line == "0":
                break
            L = int(line)
            n = int(input().strip())
            
            cuts = list(map(int, input().split()))
            # 添加边界点
            cuts = [0] + cuts + [L]
            # 排序切割点
            cuts.sort()
            
            result = solve(cuts, n + 2)
            print(f"The minimum cutting is {result}.")
    except EOFError:
        pass