import time
import random
from typing import List
from collections import deque

class Code19_NumberOfIslands:
    """
    岛屿数量 - Python实现
    
    题目描述：
    给你一个由 '1'（陆地）和 '0'（水）组成的二维网格，请你计算网格中岛屿的数量。
    岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
    
    测试链接：https://leetcode.cn/problems/number-of-islands/
    题目来源：LeetCode
    难度：中等
    
    核心算法：深度优先搜索（DFS）或广度优先搜索（BFS）
    
    解题思路：
    1. 遍历网格中的每个单元格
    2. 当遇到陆地（'1'）时，进行DFS/BFS标记所有相连的陆地
    3. 每次完整的DFS/BFS标记过程计数为一个岛屿
    4. 继续遍历直到所有单元格都被访问
    
    时间复杂度分析：
    O(m*n) - 每个单元格最多被访问一次，m和n分别是网格的行数和列数
    
    空间复杂度分析：
    O(m*n) - 最坏情况下DFS递归栈深度或BFS队列大小可能达到m*n
    
    Python语言特性：
    - 使用列表嵌套表示网格
    - 使用deque实现BFS
    - 使用递归或迭代实现DFS
    - 使用生成器表达式提高内存效率
    """
    
    # 方向数组：上、右、下、左
    DIRECTIONS = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    @staticmethod
    def num_islands_dfs(grid: List[List[str]]) -> int:
        """
        DFS解法：使用递归实现深度优先搜索
        
        Args:
            grid: 二维网格，由'1'和'0'组成
            
        Returns:
            岛屿数量
        """
        if not grid or not grid[0]:
            return 0
        
        m, n = len(grid), len(grid[0])
        count = 0
        
        def dfs(i: int, j: int):
            """DFS辅助函数：标记所有相连的陆地"""
            # 边界检查或已经访问过（标记为'0'）
            if i < 0 or i >= m or j < 0 or j >= n or grid[i][j] != '1':
                return
            
            # 标记当前单元格为已访问（改为'0'）
            grid[i][j] = '0'
            
            # 向四个方向递归搜索
            for di, dj in Code19_NumberOfIslands.DIRECTIONS:
                dfs(i + di, j + dj)
        
        # 遍历所有单元格
        for i in range(m):
            for j in range(n):
                if grid[i][j] == '1':
                    count += 1
                    dfs(i, j)
        
        return count
    
    @staticmethod
    def num_islands_bfs(grid: List[List[str]]) -> int:
        """
        BFS解法：使用队列实现广度优先搜索
        
        Args:
            grid: 二维网格，由'1'和'0'组成
            
        Returns:
            岛屿数量
        """
        if not grid or not grid[0]:
            return 0
        
        m, n = len(grid), len(grid[0])
        count = 0
        
        def bfs(i: int, j: int):
            """BFS辅助函数：使用队列标记所有相连的陆地"""
            queue = deque([(i, j)])
            grid[i][j] = '0'  # 标记为已访问
            
            while queue:
                x, y = queue.popleft()
                
                # 检查四个方向
                for dx, dy in Code19_NumberOfIslands.DIRECTIONS:
                    new_x, new_y = x + dx, y + dy
                    
                    if 0 <= new_x < m and 0 <= new_y < n and grid[new_x][new_y] == '1':
                        queue.append((new_x, new_y))
                        grid[new_x][new_y] = '0'  # 标记为已访问
        
        # 遍历所有单元格
        for i in range(m):
            for j in range(n):
                if grid[i][j] == '1':
                    count += 1
                    bfs(i, j)
        
        return count
    
    @staticmethod
    def num_islands_union_find(grid: List[List[str]]) -> int:
        """
        并查集解法：使用并查集数据结构
        
        Args:
            grid: 二维网格，由'1'和'0'组成
            
        Returns:
            岛屿数量
        """
        if not grid or not grid[0]:
            return 0
        
        m, n = len(grid), len(grid[0])
        
        class UnionFind:
            """并查集实现类"""
            def __init__(self, grid: List[List[str]]):
                self.parent = {}
                self.rank = {}
                self.count = 0
                
                # 初始化并查集
                for i in range(m):
                    for j in range(n):
                        if grid[i][j] == '1':
                            idx = i * n + j
                            self.parent[idx] = idx
                            self.rank[idx] = 0
                            self.count += 1
            
            def find(self, x: int) -> int:
                """查找根节点，带路径压缩"""
                if self.parent[x] != x:
                    self.parent[x] = self.find(self.parent[x])
                return self.parent[x]
            
            def union(self, x: int, y: int):
                """合并两个集合，带按秩合并"""
                root_x = self.find(x)
                root_y = self.find(y)
                
                if root_x != root_y:
                    if self.rank[root_x] > self.rank[root_y]:
                        self.parent[root_y] = root_x
                    elif self.rank[root_x] < self.rank[root_y]:
                        self.parent[root_x] = root_y
                    else:
                        self.parent[root_y] = root_x
                        self.rank[root_x] += 1
                    self.count -= 1
        
        uf = UnionFind(grid)
        
        for i in range(m):
            for j in range(n):
                if grid[i][j] == '1':
                    # 检查右边和下面的邻居
                    if i + 1 < m and grid[i + 1][j] == '1':
                        uf.union(i * n + j, (i + 1) * n + j)
                    if j + 1 < n and grid[i][j + 1] == '1':
                        uf.union(i * n + j, i * n + (j + 1))
        
        return uf.count
    
    @staticmethod
    def test_num_islands():
        """单元测试函数"""
        print("=== 岛屿数量单元测试 ===")
        
        # 测试用例1：常规情况
        grid1 = [
            ['1','1','1','1','0'],
            ['1','1','0','1','0'],
            ['1','1','0','0','0'],
            ['0','0','0','0','0']
        ]
        result1 = Code19_NumberOfIslands.num_islands_dfs([row[:] for row in grid1])
        print("测试用例1 - 网格1:")
        Code19_NumberOfIslands.print_grid(grid1)
        print(f"岛屿数量 (DFS): {result1}")
        print("期望: 1")
        
        # 测试用例2：多个岛屿
        grid2 = [
            ['1','1','0','0','0'],
            ['1','1','0','0','0'],
            ['0','0','1','0','0'],
            ['0','0','0','1','1']
        ]
        result2 = Code19_NumberOfIslands.num_islands_dfs([row[:] for row in grid2])
        print("\n测试用例2 - 网格2:")
        Code19_NumberOfIslands.print_grid(grid2)
        print(f"岛屿数量 (DFS): {result2}")
        print("期望: 3")
        
        # 测试用例3：边界情况 - 空网格
        grid3 = []
        result3 = Code19_NumberOfIslands.num_islands_dfs(grid3)
        print("\n测试用例3 - 空网格")
        print(f"岛屿数量: {result3}")
        print("期望: 0")
        
        # 测试用例4：全陆地
        grid4 = [
            ['1','1','1'],
            ['1','1','1'],
            ['1','1','1']
        ]
        result4 = Code19_NumberOfIslands.num_islands_dfs([row[:] for row in grid4])
        print("\n测试用例4 - 全陆地:")
        Code19_NumberOfIslands.print_grid(grid4)
        print(f"岛屿数量: {result4}")
        print("期望: 1")
        
        # 测试用例5：全水域
        grid5 = [
            ['0','0','0'],
            ['0','0','0'],
            ['0','0','0']
        ]
        result5 = Code19_NumberOfIslands.num_islands_dfs([row[:] for row in grid5])
        print("\n测试用例5 - 全水域:")
        Code19_NumberOfIslands.print_grid(grid5)
        print(f"岛屿数量: {result5}")
        print("期望: 0")
    
    @staticmethod
    def performance_comparison():
        """性能对比测试：DFS vs BFS vs 并查集"""
        print("\n=== 性能对比测试 ===")
        
        # 生成大规模测试网格
        m, n = 1000, 1000
        large_grid = Code19_NumberOfIslands.generate_large_grid(m, n, 0.3)  # 30%陆地
        
        # 测试DFS
        grid_dfs = [row[:] for row in large_grid]
        start_time1 = time.time()
        result1 = Code19_NumberOfIslands.num_islands_dfs(grid_dfs)
        end_time1 = time.time()
        
        # 测试BFS
        grid_bfs = [row[:] for row in large_grid]
        start_time2 = time.time()
        result2 = Code19_NumberOfIslands.num_islands_bfs(grid_bfs)
        end_time2 = time.time()
        
        # 测试并查集
        grid_uf = [row[:] for row in large_grid]
        start_time3 = time.time()
        result3 = Code19_NumberOfIslands.num_islands_union_find(grid_uf)
        end_time3 = time.time()
        
        time1 = (end_time1 - start_time1) * 1000
        time2 = (end_time2 - start_time2) * 1000
        time3 = (end_time3 - start_time3) * 1000
        
        print(f"网格规模: {m} × {n}")
        print(f"DFS执行时间: {time1:.2f}ms, 岛屿数量: {result1}")
        print(f"BFS执行时间: {time2:.2f}ms, 岛屿数量: {result2}")
        print(f"并查集执行时间: {time3:.2f}ms, 岛屿数量: {result3}")
        print(f"结果一致性: {result1 == result2 == result3}")
    
    @staticmethod
    def generate_large_grid(m: int, n: int, land_ratio: float) -> List[List[str]]:
        """生成大规模测试网格"""
        grid = [['0'] * n for _ in range(m)]
        
        for i in range(m):
            for j in range(n):
                if random.random() < land_ratio:
                    grid[i][j] = '1'
        
        return grid
    
    @staticmethod
    def print_grid(grid: List[List[str]]):
        """打印网格（用于调试）"""
        if not grid:
            print("[]")
            return
        
        rows_to_print = min(len(grid), 10)
        cols_to_print = min(len(grid[0]), 10)
        
        for i in range(rows_to_print):
            row_str = ' '.join(grid[i][:cols_to_print])
            if len(grid[0]) > cols_to_print:
                row_str += ' ...'
            print(row_str)
        
        if len(grid) > rows_to_print:
            print("...")
    
    @staticmethod
    def run():
        """主运行函数"""
        # 运行单元测试
        Code19_NumberOfIslands.test_num_islands()
        
        # 运行性能对比测试
        Code19_NumberOfIslands.performance_comparison()
        
        print("\n=== 岛屿数量算法验证完成 ===")


# 程序入口点
if __name__ == "__main__":
    Code19_NumberOfIslands.run()