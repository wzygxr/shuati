# SPOJ THRBL - Trouble of 13-Dots
# 题目来源：SPOJ
# 题目链接：https://www.spoj.com/problems/THRBL/
# 题目大意：
# 13-Dots要去购物中心买一些东西。购物中心是一条街，上面有n家商店排成一行，编号从1到n。
# 第i家商店前面有一个标牌，标牌上写着数字a[i]，表示这家商店的吸引力。
# 13-Dots从商店x开始，想去商店y。他只能从一家商店走到相邻的商店。
# 但是，如果在从x到y的路上（不包括x和y），有任何一家商店的吸引力大于等于x的吸引力，13-Dots就不会去商店y。
# 给定n家商店的吸引力和m个查询，每个查询给出起点和终点，判断13-Dots是否会去那个商店。
#
# 解题思路：
# 对于每个查询(x,y)，我们需要检查从x到y路径上（不包括端点）的所有商店的吸引力是否都小于a[x]。
# 这等价于查询区间内的最大值是否小于a[x]。
# 我们可以使用Sparse Table来预处理区间最大值，然后在O(1)时间内回答每个查询。
#
# 核心思想：
# 1. 使用Sparse Table预处理区间最大值
# 2. 对于每个查询(x,y)，检查区间[x+1, y-1]（或[y+1, x-1]，取决于x和y的大小关系）的最大值是否小于a[x]
#
# 时间复杂度分析：
# - 预处理：O(n log n)
# - 查询：O(1)
# - 总时间复杂度：O(n log n + m)
#
# 空间复杂度分析：
# - O(n log n)
#
# 是否为最优解：
# 是的，对于静态数组的区间最值查询问题，Sparse Table是最优解之一，因为它可以实现O(1)的查询时间复杂度。

import sys
import math

def main():
    # 读取商店数量和查询数量
    line = sys.stdin.readline().split()
    n = int(line[0])
    m = int(line[1])
    
    # 读取每家商店的吸引力
    a = list(map(int, sys.stdin.readline().split()))
    
    # 预处理log2数组
    log2 = [0] * (n + 1)
    log2[1] = 0
    for i in range(2, n + 1):
        log2[i] = log2[i >> 1] + 1
    
    # Sparse Table数组，st[i][j]表示从位置i开始，长度为2^j的区间的最大值
    st = [[0] * 20 for _ in range(n + 1)]
    
    # 初始化Sparse Table的第一层（j=0）
    for i in range(1, n + 1):
        st[i][0] = a[i - 1]  # 转换为0-based索引
    
    # 动态规划构建Sparse Table
    for j in range(1, 20):
        for i in range(1, n + 1):
            if i + (1 << j) - 1 <= n:
                st[i][j] = max(st[i][j - 1], st[i + (1 << (j - 1))][j - 1])
    
    # 查询区间[l,r]内的最大值
    def query_max(l, r):
        if l > r:
            return 0  # 空区间，返回0
        k = log2[r - l + 1]
        return max(st[l][k], st[r - (1 << k) + 1][k])
    
    # 判断13-Dots是否会去商店y
    def can_visit(x, y):
        # 如果x和y是相邻的商店，则13-Dots会去
        if abs(x - y) == 1:
            return True
        
        # 确定查询区间
        if x < y:
            left = x + 1
            right = y - 1
        else:
            left = y + 1
            right = x - 1
        
        # 查询路径上商店的最大吸引力
        max_attraction = query_max(left, right)
        
        # 如果路径上没有商店的吸引力大于等于起点商店的吸引力，则13-Dots会去
        return max_attraction < a[x - 1]  # 转换为0-based索引
    
    # 处理每个查询
    count = 0
    for _ in range(m):
        line = sys.stdin.readline().split()
        x = int(line[0])
        y = int(line[1])
        
        # 判断13-Dots是否会去商店y
        if can_visit(x, y):
            count += 1
    
    print(count)

if __name__ == "__main__":
    main()