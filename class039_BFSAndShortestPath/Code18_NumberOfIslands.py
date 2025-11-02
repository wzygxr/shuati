from collections import deque
from typing import List

# 岛屿数量
# 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
# 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
# 此外，你可以假设该网格的四条边均被水包围。
# 测试链接 : https://leetcode.cn/problems/number-of-islands/
# 
# 算法思路：
# 使用BFS进行岛屿的遍历和标记。遍历整个网格，当遇到未访问的陆地('1')时，
# 启动BFS遍历整个岛屿，并将所有相连的陆地标记为已访问。
# 岛屿数量就是启动BFS的次数。
# 
# 时间复杂度：O(m * n)，其中m和n分别是网格的行数和列数，每个单元格最多被访问一次
# 空间复杂度：O(min(m, n))，BFS队列的最大大小取决于网格的较小维度
# 
# 工程化考量：
# 1. 使用deque实现队列，提高性能
# 2. 使用方向元组简化移动逻辑
# 3. 原地修改网格避免额外空间
# 4. 类型注解提高代码可读性
class Solution:
    def numIslands(self, grid: List[List[str]]) -> int:
        if not grid or not grid[0]:
            return 0
        
        m, n = len(grid), len(grid[0])
        island_count = 0
        
        # 方向元组：上、右、下、左
        directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]
        
        for i in range(m):
            for j in range(n):
                # 找到未访问的陆地
                if grid[i][j] == '1':
                    island_count += 1
                    self._bfs(grid, i, j, m, n, directions)
        
        return island_count
    
    def _bfs(self, grid: List[List[str]], start_x: int, start_y: int, 
             m: int, n: int, directions: List[tuple[int, int]]) -> None:
        """使用BFS遍历并标记整个岛屿"""
        queue = deque()
        queue.append((start_x, start_y))
        grid[start_x][start_y] = '0'  # 标记为已访问
        
        while queue:
            x, y = queue.popleft()
            
            # 向四个方向扩展
            for dx, dy in directions:
                nx, ny = x + dx, y + dy
                
                # 检查边界和是否为未访问的陆地
                if 0 <= nx < m and 0 <= ny < n and grid[nx][ny] == '1':
                    grid[nx][ny] = '0'  # 标记为已访问
                    queue.append((nx, ny))

# 单元测试
def test_num_islands():
    solution = Solution()
    
    # 测试用例1：标准情况
    grid1 = [
        ['1','1','1','1','0'],
        ['1','1','0','1','0'],
        ['1','1','0','0','0'],
        ['0','0','0','0','0']
    ]
    assert solution.numIslands(grid1) == 1, "测试用例1失败"
    print("测试用例1通过")
    
    # 测试用例2：多个岛屿
    grid2 = [
        ['1','1','0','0','0'],
        ['1','1','0','0','0'],
        ['0','0','1','0','0'],
        ['0','0','0','1','1']
    ]
    assert solution.numIslands(grid2) == 3, "测试用例2失败"
    print("测试用例2通过")
    
    # 测试用例3：空网格
    grid3 = []
    assert solution.numIslands(grid3) == 0, "测试用例3失败"
    print("测试用例3通过")
    
    # 测试用例4：全为水
    grid4 = [
        ['0','0','0'],
        ['0','0','0'],
        ['0','0','0']
    ]
    assert solution.numIslands(grid4) == 0, "测试用例4失败"
    print("测试用例4通过")
    
    # 测试用例5：全为陆地
    grid5 = [
        ['1','1','1'],
        ['1','1','1'],
        ['1','1','1']
    ]
    assert solution.numIslands(grid5) == 1, "测试用例5失败"
    print("测试用例5通过")
    
    print("所有测试用例通过！")

if __name__ == "__main__":
    test_num_islands()