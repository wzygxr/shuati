# POJ 2728. Desert King
# 题目链接: http://poj.org/problem?id=2728
# 
# 题目描述:
# 沙漠中有n个村庄，每个村庄有坐标(x, y, z)。需要修建水管连接所有村庄。
# 水管的成本包括两部分：水平距离成本和垂直高度成本。
# 求最小化总成本与总水平距离的比值。
#
# 解题思路:
# 最优比率生成树问题，使用0-1分数规划：
# 1. 二分搜索可能的比率r
# 2. 对于每个r，构建新图，边权为cost - r * dist
# 3. 计算最小生成树，如果总权值小于0，说明r偏大；否则r偏小
# 4. 使用Prim算法计算最小生成树
#
# 时间复杂度: O(n^2 * log(max_ratio))，其中n是村庄数量
# 空间复杂度: O(n^2)
# 是否为最优解: 是，这是解决最优比率生成树问题的标准方法

import math

EPS = 1e-6
INF = 1e9

def prim(n, villages, r):
    # 计算距离和成本矩阵
    dist = [[0.0] * n for _ in range(n)]
    cost = [[0.0] * n for _ in range(n)]
    
    for i in range(n):
        for j in range(i + 1, n):
            dx = villages[i][0] - villages[j][0]
            dy = villages[i][1] - villages[j][1]
            dz = villages[i][2] - villages[j][2]
            
            dist[i][j] = dist[j][i] = math.sqrt(dx * dx + dy * dy)
            cost[i][j] = cost[j][i] = abs(dz)
    
    min_edge = [INF] * n
    visited = [False] * n
    min_edge[0] = 0
    
    total = 0.0
    
    for _ in range(n):
        u = -1
        # 找到距离MST最近的顶点
        for j in range(n):
            if not visited[j] and (u == -1 or min_edge[j] < min_edge[u]):
                u = j
        
        if u == -1:
            break
        
        visited[u] = True
        total += min_edge[u]
        
        # 更新相邻顶点的距离
        for v in range(n):
            if not visited[v]:
                edge_cost = cost[u][v] - r * dist[u][v]
                if edge_cost < min_edge[v]:
                    min_edge[v] = edge_cost
    
    return total

def desert_king(n, villages):
    left, right = 0, 100000
    
    # 二分搜索
    while right - left > EPS:
        mid = (left + right) / 2
        total = prim(n, villages, mid)
        
        if total < 0:
            right = mid
        else:
            left = mid
    
    return left

def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    
    while True:
        n = int(input[ptr]); ptr += 1
        if n == 0:
            break
        
        villages = []
        for _ in range(n):
            x = float(input[ptr]); ptr += 1
            y = float(input[ptr]); ptr += 1
            z = float(input[ptr]); ptr += 1
            villages.append((x, y, z))
        
        result = desert_king(n, villages)
        print(f"{result:.3f}")

if __name__ == "__main__":
    main()