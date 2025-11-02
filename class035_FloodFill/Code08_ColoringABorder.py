#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
边框着色 (Coloring A Border)
给你一个大小为 m x n 的整数矩阵 grid ，表示一个网格。另给你三个整数 row、col 和 color 。
两相同颜色的相邻像素称为一个连通分量。连通分量的边界是指连通分量中满足以下条件之一的所有像素：
1. 在上、下、左、右任意一个方向上与不属于同一连通分量的网格块相邻
2. 在网格的边界上（第一行/列或最后一行/列）
请你使用指定颜色 color 为所有包含 grid[row][col] 的连通分量的边界进行着色，并返回最终的网格 grid 。

测试链接: https://leetcode.cn/problems/coloring-a-border/

解题思路:
1. 首先使用Flood Fill算法找到包含起始点(row, col)的整个连通分量
2. 在遍历过程中判断每个像素是否为边界像素
3. 将边界像素着色为指定颜色

判断边界像素的条件:
1. 像素在网格边界上（第一行/列或最后一行/列）
2. 像素在四个方向上至少有一个相邻像素不属于同一连通分量

时间复杂度: O(m*n) - 最坏情况下需要访问所有格子
空间复杂度: O(m*n) - 递归栈深度和辅助数组空间
是否最优解: 是

工程化考量:
1. 异常处理：检查输入是否为空，坐标是否越界
2. 特殊情况：如果新颜色与原颜色相同，则直接返回原图像
3. 可配置性：可以扩展支持8个方向的连接

语言特性差异:
Java: 递归实现简洁，有自动内存管理
C++: 可以选择递归或使用栈手动实现，需要手动管理内存
Python: 递归实现简洁，但有递归深度限制

极端输入场景:
1. 空网格
2. 单像素网格
3. 所有像素颜色相同
4. 新颜色与原颜色相同
5. 大规模网格（可能导致栈溢出）

性能优化:
1. 提前判断边界条件
2. 使用方向数组简化代码
3. 根据数据规模选择DFS或BFS

与其他算法的联系:
1. DFS/BFS: 核心算法
2. 图论: 连通分量问题
3. 几何: 边界判断
"""

from typing import List


class Solution:
    def __init__(self):
        # 四个方向的偏移量：上、下、左、右
        self.dx = [-1, 1, 0, 0]
        self.dy = [0, 0, -1, 1]
    
    def color_border(self, grid: List[List[int]], row: int, col: int, color: int) -> List[List[int]]:
        """
        边框着色主函数
        
        Args:
            grid: 网格矩阵
            row: 起始行坐标
            col: 起始列坐标
            color: 新颜色值
            
        Returns:
            着色后的网格
        """
        # 边界条件检查
        if not grid or not grid[0]:
            return grid
        
        rows, cols = len(grid), len(grid[0])
        
        # 检查坐标是否越界
        if row < 0 or row >= rows or col < 0 or col >= cols:
            return grid
        
        original_color = grid[row][col]
        
        # 如果新颜色与原颜色相同，直接返回原图像
        if original_color == color:
            return grid
        
        # 使用辅助数组标记访问状态和边界
        visited = [[False] * cols for _ in range(rows)]
        is_border = [[False] * cols for _ in range(rows)]
        
        # 执行DFS找到连通分量并标记边界
        self._dfs(grid, row, col, original_color, visited, is_border, rows, cols)
        
        # 对边界进行着色
        for i in range(rows):
            for j in range(cols):
                if is_border[i][j]:
                    grid[i][j] = color
        
        return grid
    
    def _dfs(self, grid: List[List[int]], x: int, y: int, original_color: int,
             visited: List[List[bool]], is_border: List[List[bool]],
             rows: int, cols: int) -> None:
        """
        深度优先搜索找到连通分量并标记边界
        
        Args:
            grid: 网格矩阵
            x: 当前行坐标
            y: 当前列坐标
            original_color: 原始颜色
            visited: 访问标记数组
            is_border: 边界标记数组
            rows: 行数
            cols: 列数
        """
        # 边界检查和颜色检查
        if (x < 0 or x >= rows or y < 0 or y >= cols or 
            grid[x][y] != original_color or visited[x][y]):
            return
        
        # 标记为已访问
        visited[x][y] = True
        
        # 判断是否为边界像素
        border = False
        
        # 条件1: 像素在网格边界上
        if x == 0 or x == rows - 1 or y == 0 or y == cols - 1:
            border = True
        
        # 条件2: 四个方向上至少有一个相邻像素不属于同一连通分量
        if not border:
            for i in range(4):
                new_x = x + self.dx[i]
                new_y = y + self.dy[i]
                # 如果相邻像素颜色不同或者是未访问的相同颜色像素（说明不属于同一连通分量）
                if (0 <= new_x < rows and 0 <= new_y < cols):
                    if grid[new_x][new_y] != original_color and not visited[new_x][new_y]:
                        border = True
                        break
        
        # 标记为边界像素
        if border:
            is_border[x][y] = True
        
        # 递归处理四个方向的相邻像素
        for i in range(4):
            new_x = x + self.dx[i]
            new_y = y + self.dy[i]
            self._dfs(grid, new_x, new_y, original_color, visited, is_border, rows, cols)


# 测试方法
def print_grid(grid: List[List[int]]) -> None:
    """打印网格"""
    for row in grid:
        print(' '.join(map(str, row)))


def main():
    solution = Solution()
    
    # 测试用例1
    grid1 = [[1, 1], [1, 2]]
    print("测试用例1:")
    print("原网格:")
    print_grid(grid1)
    solution.color_border(grid1, 0, 0, 3)
    print("着色后:")
    print_grid(grid1)
    
    # 测试用例2
    grid2 = [[1, 2, 2], [2, 3, 2]]
    print("\n测试用例2:")
    print("原网格:")
    print_grid(grid2)
    solution.color_border(grid2, 0, 1, 3)
    print("着色后:")
    print_grid(grid2)


if __name__ == "__main__":
    main()