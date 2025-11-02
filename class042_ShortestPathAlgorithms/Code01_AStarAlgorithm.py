import heapq
import random
import time

# A*算法模版（对数器验证）
# A*算法是一种启发式搜索算法，结合了Dijkstra算法的完备性和贪心最佳优先搜索的高效性
# 通过估价函数（Heuristic Function）来指导搜索方向
# 核心公式: f(n) = g(n) + h(n)
# 其中:
# f(n) 是从初始状态经由状态n到目标状态的估计代价
# g(n) 是在状态空间中从初始状态到状态n的实际代价
# h(n) 是从状态n到目标状态的最佳路径的估计代价

# 方向数组：上、右、下、左
move_arr = [(-1, 0), (0, 1), (1, 0), (0, -1)]

class Node:
    def __init__(self, x, y, distance, f):
        self.x = x
        self.y = y
        self.distance = distance
        self.f = f
    
    def __lt__(self, other):
        return self.f < other.f

# Dijkstra算法
# grid[i][j] == 0 代表障碍
# grid[i][j] == 1 代表道路
# 只能走上、下、左、右，不包括斜线方向
# 返回从(startX, startY)到(targetX, targetY)的最短距离
# 时间复杂度: O(N*M*log(N*M))，其中N和M是网格的行数和列数
# 空间复杂度: O(N*M)
def minDistance1(grid, startX, startY, targetX, targetY):
    if grid[startX][startY] == 0 or grid[targetX][targetY] == 0:
        return -1
    
    n = len(grid)
    m = len(grid[0])
    
    # 初始化距离数组
    distance = [[float('inf')] * m for _ in range(n)]
    visited = [[False] * m for _ in range(n)]
    
    distance[startX][startY] = 1
    
    # 优先队列，存储(距离, 行, 列)
    heap = [(1, startX, startY)]
    
    while heap:
        dist, x, y = heapq.heappop(heap)
        
        if visited[x][y]:
            continue
        
        visited[x][y] = True
        
        if x == targetX and y == targetY:
            return distance[x][y]
        
        # 四个方向探索
        for dx, dy in move_arr:
            nx, ny = x + dx, y + dy
            
            if 0 <= nx < n and 0 <= ny < m and grid[nx][ny] == 1 and not visited[nx][ny] and distance[x][y] + 1 < distance[nx][ny]:
                distance[nx][ny] = distance[x][y] + 1
                heapq.heappush(heap, (distance[nx][ny], nx, ny))
    
    return -1

# A*算法
# grid[i][j] == 0 代表障碍
# grid[i][j] == 1 代表道路
# 只能走上、下、左、右，不包括斜线方向
# 返回从(startX, startY)到(targetX, targetY)的最短距离
# 时间复杂度: O(N*M*log(N*M))，其中N和M是网格的行数和列数
# 空间复杂度: O(N*M)
# 相比Dijkstra算法，A*算法通过启发函数h(n)指导搜索方向，通常能更快找到最优解
def minDistance2(grid, startX, startY, targetX, targetY):
    if grid[startX][startY] == 0 or grid[targetX][targetY] == 0:
        return -1
    
    n = len(grid)
    m = len(grid[0])
    
    # 初始化距离数组
    distance = [[float('inf')] * m for _ in range(n)]
    visited = [[False] * m for _ in range(n)]
    
    distance[startX][startY] = 1
    
    # 优先队列，存储节点
    heap = [Node(startX, startY, 1, 1 + abs(targetX - startX) + abs(targetY - startY))]
    heapq.heapify(heap)
    
    while heap:
        cur = heapq.heappop(heap)
        x, y = cur.x, cur.y
        
        if visited[x][y]:
            continue
        
        visited[x][y] = True
        
        if x == targetX and y == targetY:
            return distance[x][y]
        
        # 四个方向探索
        for dx, dy in move_arr:
            nx, ny = x + dx, y + dy
            
            if 0 <= nx < n and 0 <= ny < m and grid[nx][ny] == 1 and not visited[nx][ny] and distance[x][y] + 1 < distance[nx][ny]:
                distance[nx][ny] = distance[x][y] + 1
                h = abs(targetX - nx) + abs(targetY - ny)  # 曼哈顿距离启发函数
                heapq.heappush(heap, Node(nx, ny, distance[nx][ny], distance[nx][ny] + h))
    
    return -1

# 生成随机网格用于测试
def randomGrid(n):
    grid = [[0] * n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            if random.random() < 0.3:  # 30%概率是障碍
                grid[i][j] = 0
            else:
                grid[i][j] = 1
    return grid

# 主函数
def main():
    random.seed(time.time())
    
    len_val = 100
    testTime = 1000
    
    print("功能测试开始")
    for i in range(testTime):
        n = random.randint(2, len_val)
        grid = randomGrid(n)
        startX = random.randint(0, n-1)
        startY = random.randint(0, n-1)
        targetX = random.randint(0, n-1)
        targetY = random.randint(0, n-1)
        
        ans1 = minDistance1(grid, startX, startY, targetX, targetY)
        ans2 = minDistance2(grid, startX, startY, targetX, targetY)
        
        if ans1 != ans2:
            print("出错了!")
            return
    
    print("功能测试结束")
    
    print("性能测试开始")
    grid = randomGrid(1000)
    startX, startY = 0, 0
    targetX, targetY = 900, 900
    
    start = time.time()
    ans1 = minDistance1(grid, startX, startY, targetX, targetY)
    end = time.time()
    print(f"运行Dijkstra算法结果: {ans1}, 运行时间(毫秒): {(end - start) * 1000}")
    
    start = time.time()
    ans2 = minDistance2(grid, startX, startY, targetX, targetY)
    end = time.time()
    print(f"运行A*算法结果: {ans2}, 运行时间(毫秒): {(end - start) * 1000}")
    
    print("性能测试结束")

if __name__ == "__main__":
    main()