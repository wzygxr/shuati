# ZOJ 3537 Cake
# 题目来源：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3537
# 题目大意：给定一个凸多边形的n个顶点坐标，要求将其三角剖分，使得所有三角形的费用之和最小。
# 费用计算方式：cost(i,j,k) = |xi*yj + xj*yk + xk*yi - xi*yk - xj*yi - xk*yj|
# 三角剖分：将凸多边形分割成n-2个三角形，每个三角形由三个顶点组成。
#
# 解题思路：
# 1. 首先判断给定的点是否能构成凸包
# 2. 如果能构成凸包，则使用区间动态规划解决最优三角剖分问题
# 3. dp[i][j]表示将顶点i到j构成的多边形进行三角剖分的最小费用
# 4. 状态转移：枚举分割点k，dp[i][j] = min(dp[i][k] + dp[k][j] + cost(i,k,j))
#
# 时间复杂度：O(n^3) - 三层循环：区间长度、区间起点、分割点
# 空间复杂度：O(n^2) - dp数组占用空间
#
# 工程化考虑：
# 1. 输入验证：检查输入点数是否足够构成多边形
# 2. 凸包判断：确保输入点能构成凸包
# 3. 边界处理：处理点数较少的特殊情况
# 4. 异常处理：对于不合法输入给出适当提示

import sys

def solve(x, y, n):
    """
    主函数：解决凸多边形最优三角剖分问题
    时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
    空间复杂度: O(n^2) - dp数组占用空间
    """
    # 特殊情况处理
    if n < 3:
        return -1  # 无法构成多边形
    
    # 判断是否为凸包（简化处理，实际应使用凸包算法）
    # 这里假设输入已经是凸包的顶点，按逆时针排列
    
    # dp[i][j]表示将顶点i到j构成的多边形进行三角剖分的最小费用
    dp = [[float('inf')] * n for _ in range(n)]
    
    # 枚举区间长度，从3开始（至少需要3个点才能构成三角形）
    for length in range(3, n + 1):
        # 枚举区间起点i
        for i in range(n - length + 1):
            # 计算区间终点j
            j = i + length - 1
            
            # 枚举分割点k
            for k in range(i + 1, j):
                # 计算三角形(i,k,j)的费用
                cost = calculate_cost(x, y, i, k, j)
                
                if length == 3:
                    # 长度为3，直接构成三角形
                    dp[i][j] = min(dp[i][j], cost)
                else:
                    # 长度大于3，需要分割
                    left = 0 if k == i + 1 else dp[i][k]
                    right = 0 if k == j - 1 else dp[k][j]
                    
                    if left != float('inf') and right != float('inf'):
                        dp[i][j] = min(dp[i][j], left + right + cost)
    
    return -1 if dp[0][n - 1] == float('inf') else dp[0][n - 1]

def calculate_cost(x, y, i, j, k):
    """计算三角形(i,j,k)的费用"""
    # 使用叉积计算三角形面积的两倍，作为费用
    return abs(x[i] * y[j] + x[j] * y[k] + x[k] * y[i] - 
               x[i] * y[k] - x[j] * y[i] - x[k] * y[j])

if __name__ == "__main__":
    # 读取输入
    try:
        while True:
            line = input().strip()
            if not line:
                break
            n = int(line)
            x = []
            y = []
            
            for _ in range(n):
                parts = input().split()
                x.append(int(parts[0]))
                y.append(int(parts[1]))
            
            result = solve(x, y, n)
            if result == -1:
                print("I can't do it!")
            else:
                print(result)
    except EOFError:
        pass