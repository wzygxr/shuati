# KATHTHI
# 题目链接: https://www.spoj.com/problems/KATHTHI/
# 
# 算法思路：
# 这是一个典型的0-1 BFS问题
# 将网格看作图，每个单元格是一个节点
# 如果移动到相同字符的单元格，边权为0
# 如果移动到不同字符的单元格，边权为1
# 使用双端队列，权值为0的节点放在队首，权值为1的节点放在队尾
# 
# 时间复杂度：O(n * m)，其中n和m分别是网格的行数和列数
# 空间复杂度：O(n * m)，用于存储距离数组和队列

from collections import deque
import sys

def min_changes(grid, n, m):
    # 四个方向的移动：上、右、下、左
    move = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    # distance[i][j]表示从起点(0,0)到(i,j)的最小变化次数
    distance = [[float('inf')] * m for _ in range(n)]
    
    # 双端队列，用于0-1 BFS
    dq = deque()
    dq.appendleft((0, 0))
    distance[0][0] = 0
    
    while dq:
        # 从队首取出节点
        x, y = dq.popleft()
        
        # 如果到达终点
        if x == n - 1 and y == m - 1:
            return distance[x][y]
        
        # 向四个方向扩展
        for dx, dy in move:
            nx, ny = x + dx, y + dy
            
            # 检查边界
            if 0 <= nx < n and 0 <= ny < m:
                # 如果字符相同，权重为0；否则权重为1
                weight = 1 if grid[x][y] != grid[nx][ny] else 0
                
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
    t = int(sys.stdin.readline())
    
    for _ in range(t):
        n, m = map(int, sys.stdin.readline().split())
        grid = []
        
        for _ in range(n):
            grid.append(sys.stdin.readline().strip())
        
        print(min_changes(grid, n, m))

if __name__ == "__main__":
    main()