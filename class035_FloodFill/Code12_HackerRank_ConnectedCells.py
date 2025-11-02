from typing import List
from collections import deque

class Solution:
    """
    HackerRank - Connected Cells in a Grid (Python版本)
    题目链接: https://www.hackerrank.com/challenges/connected-cell-in-a-grid/problem
    
    解题思路:
    使用Flood Fill算法（8连通）遍历整个矩阵，计算每个连通区域的大小，并记录最大值。
    
    时间复杂度: O(m*n)
    空间复杂度: O(m*n)
    是否最优解: 是
    """
    
    def __init__(self):
        # 八个方向的偏移量
        self.dx = [-1, -1, -1, 0, 0, 1, 1, 1]
        self.dy = [-1, 0, 1, -1, 1, -1, 0, 1]
    
    def connectedCell(self, matrix: List[List[int]]) -> int:
        """DFS版本"""
        if not matrix or not matrix[0]:
            return 0
        
        m, n = len(matrix), len(matrix[0])
        max_region = 0
        visited = [[False] * n for _ in range(m)]
        
        for i in range(m):
            for j in range(n):
                if matrix[i][j] == 1 and not visited[i][j]:
                    region_size = self._dfs(matrix, i, j, visited, m, n)
                    max_region = max(max_region, region_size)
        
        return max_region
    
    def _dfs(self, matrix: List[List[int]], x: int, y: int, 
            visited: List[List[bool]], m: int, n: int) -> int:
        """深度优先搜索辅助函数"""
        if x < 0 or x >= m or y < 0 or y >= n or matrix[x][y] == 0 or visited[x][y]:
            return 0
        
        visited[x][y] = True
        size = 1
        
        for i in range(8):
            nx, ny = x + self.dx[i], y + self.dy[i]
            size += self._dfs(matrix, nx, ny, visited, m, n)
        
        return size
    
    def connectedCellBFS(self, matrix: List[List[int]]) -> int:
        """BFS版本"""
        if not matrix or not matrix[0]:
            return 0
        
        m, n = len(matrix), len(matrix[0])
        max_region = 0
        visited = [[False] * n for _ in range(m)]
        
        for i in range(m):
            for j in range(n):
                if matrix[i][j] == 1 and not visited[i][j]:
                    region_size = self._bfs(matrix, i, j, visited, m, n)
                    max_region = max(max_region, region_size)
        
        return max_region
    
    def _bfs(self, matrix: List[List[int]], x: int, y: int, 
             visited: List[List[bool]], m: int, n: int) -> int:
        """广度优先搜索辅助函数"""
        queue = deque()
        queue.append((x, y))
        visited[x][y] = True
        size = 0
        
        while queue:
            i, j = queue.popleft()
            size += 1
            
            for k in range(8):
                ni, nj = i + self.dx[k], j + self.dy[k]
                
                if (0 <= ni < m and 0 <= nj < n and 
                    matrix[ni][nj] == 1 and not visited[ni][nj]):
                    visited[ni][nj] = True
                    queue.append((ni, nj))
        
        return size

def print_matrix(matrix: List[List[int]]) -> None:
    """打印矩阵"""
    for row in matrix:
        print(' '.join(map(str, row)))

def main():
    solution = Solution()
    
    # 测试用例1
    matrix1 = [
        [1, 1, 0, 0],
        [0, 1, 1, 0],
        [0, 0, 1, 0],
        [1, 0, 0, 0]
    ]
    
    print("测试用例1 - 标准网格:")
    print("矩阵:")
    print_matrix(matrix1)
    print(f"DFS版本最大连通区域大小: {solution.connectedCell(matrix1)}")
    
    matrix1_copy = [row[:] for row in matrix1]
    print(f"BFS版本最大连通区域大小: {solution.connectedCellBFS(matrix1_copy)}")

if __name__ == "__main__":
    main()