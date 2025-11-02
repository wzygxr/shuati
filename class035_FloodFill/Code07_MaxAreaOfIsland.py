#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
岛屿的最大面积
给你一个大小为 m * n 的二进制矩阵 grid 。
岛屿 是由一些相邻的 1 (代表土地) 构成的组合，这里的「相邻」要求两个 1 必须在 水平或者竖直的四个方向上 相邻。
你可以假设 grid 的四个边缘都被 0（代表水）包围着。
岛屿的面积是岛上值为 1 的单元格的数目。
计算并返回 grid 中最大的岛屿面积。如果没有岛屿，则返回面积为 0 。

测试链接: https://leetcode.cn/problems/max-area-of-island/

解题思路:
遍历整个矩阵，当遇到值为1的单元格时，使用DFS计算该岛屿的面积，并更新最大面积。
在DFS过程中，将访问过的1标记为0，避免重复计算。

时间复杂度: O(m*n) - 最坏情况下需要遍历整个矩阵
空间复杂度: O(m*n) - 递归调用栈的深度最多为m*n
是否最优解: 是

工程化考量:
1. 异常处理：检查输入是否为空
2. 边界条件：处理全0矩阵和全1矩阵
3. 可配置性：可以扩展支持8个方向的连接

语言特性差异:
Java: 递归实现简洁，有自动内存管理
C++: 可以选择递归或使用栈手动实现
Python: 递归实现简洁，但有递归深度限制

极端输入场景:
1. 空矩阵
2. 全0矩阵
3. 全1矩阵
4. 单个1元素

性能优化:
1. 原地修改避免额外空间
2. 提前终止条件
3. 使用方向数组简化代码
"""

from typing import List


class Solution:
    def __init__(self):
        # 四个方向的偏移量：上、下、左、右
        self.dx = [-1, 1, 0, 0]
        self.dy = [0, 0, -1, 1]
    
    def maxAreaOfIsland(self, grid: List[List[int]]) -> int:
        """
        计算最大岛屿面积
        
        Args:
            grid: 二进制矩阵
            
        Returns:
            最大岛屿面积
        """
        # 边界条件检查
        if not grid or not grid[0]:
            return 0
        
        max_area = 0
        rows, cols = len(grid), len(grid[0])
        
        # 遍历整个矩阵
        for i in range(rows):
            for j in range(cols):
                # 如果当前单元格是陆地，计算其所在岛屿的面积
                if grid[i][j] == 1:
                    area = self.dfs(grid, i, j, rows, cols)
                    max_area = max(max_area, area)
        
        return max_area
    
    def dfs(self, grid: List[List[int]], x: int, y: int, rows: int, cols: int) -> int:
        """
        深度优先搜索计算岛屿面积
        
        Args:
            grid: 二进制矩阵
            x: 当前行坐标
            y: 当前列坐标
            rows: 行数
            cols: 列数
            
        Returns:
            当前岛屿的面积
        """
        # 边界检查和值检查
        if x < 0 or x >= rows or y < 0 or y >= cols or grid[x][y] != 1:
            return 0
        
        # 标记当前单元格已访问
        grid[x][y] = 0
        
        # 计算当前单元格的贡献（1）加上四个方向相邻单元格的贡献
        area = 1
        for i in range(4):
            new_x = x + self.dx[i]
            new_y = y + self.dy[i]
            area += self.dfs(grid, new_x, new_y, rows, cols)
        
        return area


# 测试方法
def print_grid(grid: List[List[int]]) -> None:
    """打印网格"""
    for row in grid:
        print(' '.join(map(str, row)))


def main():
    solution = Solution()
    
    # 测试用例1
    grid1 = [
        [0,0,1,0,0,0,0,1,0,0,0,0,0],
        [0,0,0,0,0,0,0,1,1,1,0,0,0],
        [0,1,1,0,1,0,0,0,0,0,0,0,0],
        [0,1,0,0,1,1,0,0,1,0,1,0,0],
        [0,1,0,0,1,1,0,0,1,1,1,0,0],
        [0,0,0,0,0,0,0,0,0,0,1,0,0],
        [0,0,0,0,0,0,0,1,1,1,0,0,0],
        [0,0,0,0,0,0,0,1,1,0,0,0,0]
    ]
    
    print("测试用例1:")
    print("网格:")
    print_grid(grid1)
    print("最大岛屿面积:", solution.maxAreaOfIsland(grid1))
    
    # 测试用例2
    grid2 = [[0,0,0,0,0,0,0,0]]
    print("\n测试用例2:")
    print("网格:")
    print_grid(grid2)
    print("最大岛屿面积:", solution.maxAreaOfIsland(grid2))


if __name__ == "__main__":
    main()