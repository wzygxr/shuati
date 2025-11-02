import heapq

# LeetCode 1293. 网格中的最短路径
# 题目链接: https://leetcode.cn/problems/shortest-path-in-a-grid-with-obstacles-elimination/
# 题目描述: 给你一个 m * n 的网格，其中每个单元格不是 0 （空）就是 1 （障碍物）。
# 每一步，您都可以在空白单元格中上、下、左、右移动。
# 如果您最多可以消除 k 个障碍物，请找出从左上角 (0, 0) 到右下角 (m-1, n-1) 的最短路径，
# 并返回通过该路径所需的步数。如果找不到这样的路径，则返回 -1。
#
# 解题思路:
# 这道题可以使用A*算法来解决。状态不仅包括位置(x,y)，还包括已经消除的障碍物数量。
# 我们使用优先队列来存储状态，状态包括:
# 1. 当前位置(x,y)
# 2. 已经走过的步数
# 3. 已经消除的障碍物数量
# 4. 估价函数f = g + h，其中g是已经走过的步数，h是启发函数(曼哈顿距离)
#
# 时间复杂度: O(M*N*K*log(M*N*K))，其中M和N是网格的行数和列数，K是最多可以消除的障碍物数量
# 空间复杂度: O(M*N*K)

class Node:
    def __init__(self, x, y, steps, obstacles, f):
        self.x = x
        self.y = y
        self.steps = steps
        self.obstacles = obstacles
        self.f = f
    
    def __lt__(self, other):
        return self.f < other.f

def shortestPath(grid, k):
    """
    使用A*算法求解网格中的最短路径
    
    Args:
        grid: 二维列表，表示网格，0表示空地，1表示障碍物
        k: 整数，最多可以消除的障碍物数量
    
    Returns:
        整数，最短路径的步数，如果无法到达则返回-1
    """
    m = len(grid)
    n = len(grid[0])
    
    # 特殊情况：起点就是终点
    if m == 1 and n == 1:
        return 0
    
    # 如果k足够大，可以直接走曼哈顿距离最短路径
    if k >= m + n - 2:
        return m + n - 2
    
    # visited[x][y][obs]表示在位置(x,y)且已经消除obs个障碍物的状态是否已经访问过
    visited = [[[False for _ in range(k + 1)] for _ in range(n)] for _ in range(m)]
    
    # 方向数组：上、右、下、左
    move = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    # 优先队列，存储节点
    pq = []
    
    # 初始状态
    startX, startY = 0, 0
    startObstacles = 1 if grid[0][0] == 1 else 0
    if startObstacles <= k:
        h = manhattanDistance(0, 0, m - 1, n - 1)
        heapq.heappush(pq, Node(startX, startY, 0, startObstacles, h))
        visited[startX][startY][startObstacles] = True
    
    while pq:
        current = heapq.heappop(pq)
        x, y = current.x, current.y
        steps = current.steps
        obstacles = current.obstacles
        
        # 到达终点
        if x == m - 1 and y == n - 1:
            return steps
        
        # 四个方向探索
        for dx, dy in move:
            nx, ny = x + dx, y + dy
            
            # 检查边界
            if 0 <= nx < m and 0 <= ny < n:
                # 计算新的障碍物数量
                newObstacles = obstacles + grid[nx][ny]
                
                # 如果障碍物数量不超过k且该状态未访问过
                if newObstacles <= k and not visited[nx][ny][newObstacles]:
                    visited[nx][ny][newObstacles] = True
                    newSteps = steps + 1
                    h = manhattanDistance(nx, ny, m - 1, n - 1)
                    f = newSteps + h
                    heapq.heappush(pq, Node(nx, ny, newSteps, newObstacles, f))
    
    return -1

def manhattanDistance(x1, y1, x2, y2):
    """
    计算曼哈顿距离
    
    Args:
        x1, y1: 第一个点的坐标
        x2, y2: 第二个点的坐标
    
    Returns:
        整数，两点之间的曼哈顿距离
    """
    return abs(x1 - x2) + abs(y1 - y2)

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    grid1 = [[0,0,0],[1,1,0],[0,0,0],[0,1,1],[0,0,0]]
    k1 = 1
    print("测试用例1结果:", shortestPath(grid1, k1))  # 期望输出: 6
    
    # 测试用例2
    grid2 = [[0,1,1],[1,1,1],[1,0,0]]
    k2 = 1
    print("测试用例2结果:", shortestPath(grid2, k2))  # 期望输出: -1