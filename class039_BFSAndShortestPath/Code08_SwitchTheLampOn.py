# Switch the Lamp On
# 题目链接: https://www.luogu.com.cn/problem/P4667
# 
# 算法思路：
# 这是一个0-1 BFS问题
# 将网格看作图，每个交点是一个节点
# 如果两个相邻块的方向一致，移动到下一个交点不需要转换，边权为0
# 如果两个相邻块的方向不一致，移动到下一个交点需要转换，边权为1
# 使用双端队列，权值为0的节点放在队首，权值为1的节点放在队尾
# 
# 时间复杂度：O(n * m)，其中n和m分别是网格的行数和列数
# 空间复杂度：O(n * m)，用于存储距离数组和队列

from collections import deque
import sys

def min_switches(grid, n, m):
    # 特殊情况：起点和终点重合
    if n == 1 and m == 1:
        return 0
    
    # 四个方向的移动：
    # 0: 上 (连接当前点和上方交点)
    # 1: 右 (连接当前点和右方交点)
    # 2: 下 (连接当前点和下方交点)
    # 3: 左 (连接当前点和左方交点)
    dx = [-1, 0, 1, 0]
    dy = [0, 1, 0, -1]
    
    # distance[i][j]表示从起点(0,0)到交点(i,j)的最小转换次数
    distance = [[float('inf')] * (m + 1) for _ in range(n + 1)]
    
    # 双端队列，用于0-1 BFS
    dq = deque()
    dq.appendleft((0, 0))
    distance[0][0] = 0
    
    while dq:
        # 从队首取出节点
        x, y = dq.popleft()
        
        # 如果到达终点
        if x == n and y == m:
            return distance[x][y]
        
        # 向四个方向扩展
        for i in range(4):
            nx = x + dx[i]
            ny = y + dy[i]
            
            # 检查边界
            if 0 <= nx <= n and 0 <= ny <= m:
                # 计算权重
                weight = 1
                # 根据当前位置和移动方向判断是否需要转换
                if i == 0 and x > 0 and grid[x - 1][y] == '\\':
                    weight = 0
                elif i == 1 and y < m and grid[x][y] == '/':
                    weight = 0
                elif i == 2 and x < n and grid[x][y] == '\\':
                    weight = 0
                elif i == 3 and y > 0 and grid[x][y - 1] == '/':
                    weight = 0
                
                # 如果新路径更优
                if distance[x][y] + weight < distance[nx][ny]:
                    distance[nx][ny] = distance[x][y] + weight
                    # 根据权重决定放在队首还是队尾
                    if weight == 0:
                        dq.appendleft((nx, ny))
                    else:
                        dq.append((nx, ny))
    
    return -1

def main():
    n, m = map(int, sys.stdin.readline().split())
    grid = []
    
    for _ in range(n):
        grid.append(sys.stdin.readline().strip())
    
    print(min_switches(grid, n, m))

if __name__ == "__main__":
    main()