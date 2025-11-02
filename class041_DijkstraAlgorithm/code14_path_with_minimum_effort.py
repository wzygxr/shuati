import heapq

# 最小体力消耗路径
# 你准备参加一场远足活动。给你一个二维 rows x columns 的地图 heights
# 其中 heights[row][col] 表示格子 (row, col) 的高度
# 一开始你在最左上角的格子 (0, 0) ，且你希望去最右下角的格子 (rows-1, columns-1) 
# （注意下标从 0 开始编号）。你每次可以往 上，下，左，右 四个方向之一移动
# 你想要找到耗费 体力 最小的一条路径
# 一条路径耗费的体力值是路径上，相邻格子之间高度差绝对值的最大值
# 请你返回从左上角走到右下角的最小 体力消耗值
# 测试链接 ：https://leetcode.cn/problems/path-with-minimum-effort/

def minimum_effort_path(heights):
    """
    使用Dijkstra算法求解最小体力消耗路径
    时间复杂度: O(mn*log(mn)) 其中m和n分别是地图的行数和列数
    空间复杂度: O(mn)
    """
    # 移动方向: 上、右、下、左
    move = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    n = len(heights)
    m = len(heights[0])
    
    # distance[i][j]表示从起点(0,0)到点(i,j)的最小体力消耗
    distance = [[float('inf')] * m for _ in range(n)]
    # 起点体力消耗为0
    distance[0][0] = 0
    
    # visited[i][j]表示点(i,j)是否已经确定了最短路径
    visited = [[False] * m for _ in range(n)]
    
    # 优先队列，按体力消耗从小到大排序
    # 存储 (体力消耗, 行, 列)
    heap = [(0, 0, 0)]
    
    while heap:
        c, x, y = heapq.heappop(heap)
        
        # 如果已经处理过，跳过
        if visited[x][y]:
            continue
        
        # 如果到达终点，直接返回结果
        if x == n - 1 and y == m - 1:
            # 常见剪枝
            # 发现终点直接返回
            # 不用等都结束
            return c
        
        # 标记为已处理
        visited[x][y] = True
        
        # 向四个方向扩展
        for dx, dy in move:
            nx, ny = x + dx, y + dy
            
            # 检查边界条件和是否已访问
            if 0 <= nx < n and 0 <= ny < m and not visited[nx][ny]:
                # 计算通过当前路径到达新点的体力消耗
                # 是当前路径上所有相邻格子高度差绝对值的最大值
                nc = max(c, abs(heights[x][y] - heights[nx][ny]))
                
                # 如果新的体力消耗更小，则更新
                if nc < distance[nx][ny]:
                    distance[nx][ny] = nc
                    heapq.heappush(heap, (nc, nx, ny))
    
    return -1

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    heights1 = [[1,2,2],[3,8,2],[5,3,5]]
    print("测试用例1结果:", minimum_effort_path(heights1))  # 期望输出: 2
    
    # 测试用例2
    heights2 = [[1,2,3],[3,8,4],[5,3,5]]
    print("测试用例2结果:", minimum_effort_path(heights2))  # 期望输出: 1
    
    # 测试用例3
    heights3 = [[1,2,1,1,1],[1,2,1,2,1],[1,2,1,2,1],[1,2,1,2,1],[1,1,1,2,1]]
    print("测试用例3结果:", minimum_effort_path(heights3))  # 期望输出: 0