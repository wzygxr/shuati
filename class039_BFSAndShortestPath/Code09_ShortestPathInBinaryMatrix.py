# 最短路径（二进制矩阵）
# 给你一个 n x n 的二进制矩阵 grid ，返回矩阵中最短 畅通路径 的长度
# 如果不存在这样的路径，返回 -1
# 二进制矩阵中的 畅通路径 是一条从 左上角 单元格（即，(0, 0)）到 右下角 单元格（即，(n - 1, n - 1)）的路径
# 该路径同时满足以下要求：
# 路径途经的所有单元格的值都是 0
# 路径中所有相邻的单元格应当在 8 个方向之一 上连通（即，相邻两单元之间彼此不同且共享一条边或者一个角）
# 畅通路径的长度 是该路径途经的单元格总数
# 测试链接 : https://leetcode.com/problems/shortest-path-in-binary-matrix/
# 
# 算法思路：
# 使用标准BFS解决最短路径问题
# 由于是8方向连通，需要考虑8个方向的移动
# 从起点(0,0)开始BFS搜索，直到到达终点(n-1,n-1)
# 
# 时间复杂度：O(n^2)，其中n是矩阵的边长，每个单元格最多被访问一次
# 空间复杂度：O(n^2)，用于存储队列和访问状态
# 
# 工程化考量：
# 1. 边界检查：确保移动后的位置在矩阵范围内
# 2. 特殊情况处理：起点或终点为1时直接返回-1
# 3. 8方向移动：需要考虑8个方向而不是4个方向

from collections import deque

def shortestPathBinaryMatrix(grid):
    """
    计算二进制矩阵中最短畅通路径的长度
    
    Args:
        grid: List[List[int]] - n x n 的二进制矩阵
        
    Returns:
        int - 最短路径长度，如果不存在则返回-1
    """
    n = len(grid)
    # 特殊情况：起点或终点为1
    if grid[0][0] == 1 or grid[n - 1][n - 1] == 1:
        return -1
    # 特殊情况：只有一个单元格
    if n == 1:
        return 1
    
    # 8个方向的移动：上、右上、右、右下、下、左下、左、左上
    move = [(-1, -1), (-1, 0), (-1, 1), (0, 1), (1, 1), (1, 0), (1, -1), (0, -1)]
    
    # 访问状态数组
    visited = [[False] * n for _ in range(n)]
    
    # 队列用于BFS
    queue = deque()
    
    # 起点入队
    visited[0][0] = True
    queue.append((0, 0))
    level = 1
    
    # BFS搜索
    while queue:
        level += 1
        size = len(queue)
        # 处理当前层的所有节点
        for _ in range(size):
            x, y = queue.popleft()
            
            # 向8个方向扩展
            for dx, dy in move:
                nx, ny = x + dx, y + dy
                
                # 检查边界、是否已访问、是否为畅通路径
                if (0 <= nx < n and 0 <= ny < n and 
                    not visited[nx][ny] and grid[nx][ny] == 0):
                    # 如果到达终点
                    if nx == n - 1 and ny == n - 1:
                        return level
                    visited[nx][ny] = True
                    queue.append((nx, ny))
    
    return -1