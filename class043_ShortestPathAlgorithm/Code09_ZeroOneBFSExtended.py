# 01-BFS扩展练习题：使网格图至少有一条有效路径的最小代价
# 给你一个 m x n 的网格图 grid 。 grid 中每个格子都有一个数字，对应着从起点到终点的路径策略。
# 当你处于格子 grid[i][j] 时，你可以执行以下操作：
# - 如果 grid[i][j] == 1，你可以移动到下一个格子 (i+1, j)
# - 如果 grid[i][j] == 2，你可以移动到下一个格子 (i-1, j)
# - 如果 grid[i][j] == 3，你可以移动到下一个格子 (i, j+1)
# - 如果 grid[i][j] == 4，你可以移动到下一个格子 (i, j-1)
# 注意：网格图中可能会有无效数字，如果 grid[i][j] == 0，意味着该格子不能通行。
# 在一个操作中，你可以修改任意格子的数字为 1, 2, 3 或 4。
# 请你返回让网格图至少有一条有效路径的最小操作代价。
# 测试链接: https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/
# 
# 算法思路：
# 这是一道典型的01-BFS问题。
# 1. 将网格图看作图论问题，每个格子是一个节点
# 2. 如果按照当前格子的指示方向移动，边权为0（不需要修改）
# 3. 如果改变方向移动，边权为1（需要一次操作修改格子）
# 4. 使用双端队列实现01-BFS，边权为0的节点加入队首，边权为1的节点加入队尾
#
# 具体实现：
# 1. 使用状态(x, y, dist)表示当前位置和到达该位置的最小代价
# 2. 按照格子指示方向移动权重为0
# 3. 改变方向移动权重为1
# 4. 使用双端队列存储待处理的状态
# 5. 权重为0的边添加到队首，权重为1的边添加到队尾
#
# 时间复杂度：O(M * N)
# 空间复杂度：O(M * N)
#
# 相关题目链接：
# 1. LeetCode 1368. 使网格图至少有一条有效路径的最小代价 - https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/
# 2. AtCoder ABC176 D - Wizard in Maze - https://atcoder.jp/contests/abc176/tasks/abc176_d
# 3. SPOJ KATHTHI - https://www.spoj.com/problems/KATHTHI/
# 4. UVA 11573 Ocean Currents - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=27&page=show_problem&problem=2620
# 5. Codeforces 590C Three States - https://codeforces.com/problemset/problem/590/C
# 6. LeetCode 542. 01 Matrix - https://leetcode.cn/problems/01-matrix/
# 7. 洛谷 P4568 飞行路线 - https://www.luogu.com.cn/problem/P4568
# 8. HDU 5037 Frog - https://acm.hdu.edu.cn/showproblem.php?pid=5037
# 9. 牛客 NC50522 跳楼机 - https://ac.nowcoder.com/acm/problem/50522
# 10. ZOJ 3808 ZOJ3808 - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367908
# 11. POJ 3663 Costume Party - http://poj.org/problem?id=3663
# 12. 51Nod 1459 迷宫游戏 - https://www.51nod.com/Challenge/Problem.html#problemId=1459
# 13. 洛谷 P1379 八数码难题 - https://www.luogu.com.cn/problem/P1379
# 14. LeetCode 773. Sliding Puzzle - https://leetcode.cn/problems/sliding-puzzle/
# 15. Codeforces 1063B Labyrinth - https://codeforces.com/problemset/problem/1063/B

import sys
from collections import deque

# 常量定义
MAXN = 101
INF = float('inf')

# 全局变量
m = n = 0
grid = [[0 for _ in range(MAXN)] for _ in range(MAXN)]
dist = [[INF for _ in range(MAXN)] for _ in range(MAXN)]
visited = [[False for _ in range(MAXN)] for _ in range(MAXN)]

# 四个方向：下、上、右、左
dx = [1, -1, 0, 0]
dy = [0, 0, 1, -1]

# 01-BFS实现
def bfs01():
    global m, n, grid, dist, visited, dx, dy
    
    # 初始化距离数组为无穷大
    for i in range(m):
        for j in range(n):
            dist[i][j] = INF
            visited[i][j] = False
    
    # 双端队列，用于存储待处理的状态
    dq = deque()
    
    # 起点入队，距离为0
    dist[0][0] = 0
    dq.appendleft((0, 0, 0))
    
    # 当双端队列不为空时，继续处理
    while dq:
        # 从队首取出状态（距离最小的状态）
        x, y, d = dq.popleft()
        
        # 如果已经访问过，跳过（避免重复处理）
        if visited[x][y]:
            continue
        
        # 标记为已访问
        visited[x][y] = True
        
        # 到达终点，返回距离
        if x == m - 1 and y == n - 1:
            return d
        
        # 四个方向扩展
        for i in range(4):
            nx = x + dx[i]
            ny = y + dy[i]
            
            # 检查边界，如果超出边界则跳过
            if nx < 0 or nx >= m or ny < 0 or ny >= n:
                continue
            
            # 计算边权
            # 如果当前格子的指示方向与移动方向一致，边权为0（不需要修改）
            # 否则边权为1（需要一次操作修改格子）
            cost = 0 if grid[x][y] == i + 1 else 1
            
            # 如果未访问且距离更短，则更新距离并添加到队列
            if not visited[nx][ny] and d + cost < dist[nx][ny]:
                dist[nx][ny] = d + cost
                # 根据边权决定加入队首还是队尾
                # 边权为0的节点加入队首
                if cost == 0:
                    dq.appendleft((nx, ny, d + cost))
                # 边权为1的节点加入队尾
                else:
                    dq.append((nx, ny, d + cost))
    
    # 无法到达终点，返回-1
    return -1

def main():
    global m, n, grid
    
    # 读取行列数
    line = sys.stdin.readline().strip().split()
    m = int(line[0])
    n = int(line[1])
    
    # 读取网格
    for i in range(m):
        line = sys.stdin.readline().strip().split()
        for j in range(n):
            grid[i][j] = int(line[j])
    
    # 计算结果并输出
    print(bfs01())

if __name__ == "__main__":
    main()