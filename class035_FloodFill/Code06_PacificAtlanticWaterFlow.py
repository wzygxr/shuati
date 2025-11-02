#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
太平洋大西洋水流问题
有一个 m × n 的矩形岛屿，与 太平洋 和 大西洋 相邻。
"太平洋" 处于大陆的左边界和上边界，而 "大西洋" 处于大陆的右边界和下边界。
这个岛被分割成一个由若干方形单元格组成的网格。给定一个 m x n 的整数矩阵 heights，
heights[r][c] 表示坐标 (r, c) 上单元格 高于海平面的高度 。
岛上雨水较多，如果相邻单元格的高度 小于或等于 当前单元格的高度，雨水可以直接向北、南、东、西流向相邻单元格。
水可以从海洋附近的任何单元格流入海洋。
返回网格坐标 result 的 2D 列表 ，其中 result[i] = [ri, ci] 表示雨水从单元格 (ri, ci) 流动 既可流向太平洋也可流向大西洋 。

测试链接: https://leetcode.cn/problems/pacific-atlantic-water-flow/

解题思路:
采用逆向思维，从海洋边界开始进行DFS/BFS搜索，找到所有能够流向对应海洋的单元格。
分别计算能够流向太平洋和大西洋的单元格集合，两者交集即为答案。

时间复杂度: O(m*n) - 每个单元格最多被访问常数次
空间复杂度: O(m*n) - 需要额外空间存储访问状态
是否最优解: 是

工程化考量:
1. 异常处理：检查输入是否为空
2. 边界条件：处理单行、单列情况
3. 可配置性：可以扩展到更多海洋的情况

语言特性差异:
Java: 对象引用和垃圾回收
C++: 指针操作和手动内存管理
Python: 动态类型和自动内存管理

极端输入场景:
1. 空网格
2. 单元素网格
3. 所有元素高度相同
4. 递增/递减矩阵

性能优化:
1. 使用布尔数组标记访问状态
2. 逆向搜索减少重复计算
3. 方向数组简化代码
"""

from typing import List


class Solution:
    def __init__(self):
        # 四个方向的偏移量：上、下、左、右
        self.dx = [-1, 1, 0, 0]
        self.dy = [0, 0, -1, 1]
    
    def pacificAtlantic(self, heights: List[List[int]]) -> List[List[int]]:
        """
        太平洋大西洋水流问题主函数
        
        Args:
            heights: 高度矩阵
            
        Returns:
            能同时流向太平洋和大西洋的单元格坐标列表
        """
        result = []
        
        # 边界条件检查
        if not heights or not heights[0]:
            return result
        
        rows, cols = len(heights), len(heights[0])
        
        # 记录能流向太平洋的单元格
        can_reach_pacific = [[False] * cols for _ in range(rows)]
        # 记录能流向大西洋的单元格
        can_reach_atlantic = [[False] * cols for _ in range(rows)]
        
        # 从太平洋边界开始DFS
        # 上边界（第一行）
        for j in range(cols):
            self.dfs(heights, 0, j, can_reach_pacific, rows, cols)
        # 左边界（第一列）
        for i in range(rows):
            self.dfs(heights, i, 0, can_reach_pacific, rows, cols)
        
        # 从大西洋边界开始DFS
        # 下边界（最后一行）
        for j in range(cols):
            self.dfs(heights, rows - 1, j, can_reach_atlantic, rows, cols)
        # 右边界（最后一列）
        for i in range(rows):
            self.dfs(heights, i, cols - 1, can_reach_atlantic, rows, cols)
        
        # 找到同时能流向太平洋和大西洋的单元格
        for i in range(rows):
            for j in range(cols):
                if can_reach_pacific[i][j] and can_reach_atlantic[i][j]:
                    result.append([i, j])
        
        return result
    
    def dfs(self, heights: List[List[int]], x: int, y: int, can_reach: List[List[bool]], rows: int, cols: int) -> None:
        """
        深度优先搜索，标记能流向指定海洋的单元格
        
        Args:
            heights: 高度矩阵
            x: 当前行坐标
            y: 当前列坐标
            can_reach: 访问标记数组
            rows: 行数
            cols: 列数
        """
        # 如果已经访问过，直接返回
        if can_reach[x][y]:
            return
        
        # 标记为已访问
        can_reach[x][y] = True
        
        # 向四个方向扩展
        for i in range(4):
            new_x = x + self.dx[i]
            new_y = y + self.dy[i]
            
            # 检查边界和高度条件（水只能从低处流向高处，即逆向搜索）
            if (0 <= new_x < rows and 0 <= new_y < cols and 
                heights[new_x][new_y] >= heights[x][y]):
                self.dfs(heights, new_x, new_y, can_reach, rows, cols)


# 测试方法
def print_matrix(matrix: List[List[int]]) -> None:
    """打印矩阵"""
    for row in matrix:
        print(' '.join(map(str, row)))


def print_result(result: List[List[int]]) -> None:
    """打印结果"""
    for cell in result:
        print(f"[{cell[0]}, {cell[1]}]")


def main():
    solution = Solution()
    
    # 测试用例1
    heights1 = [
        [1, 2, 2, 3, 5],
        [3, 2, 3, 4, 4],
        [2, 4, 5, 3, 1],
        [6, 7, 1, 4, 5],
        [5, 1, 1, 2, 4]
    ]
    
    print("测试用例1:")
    print("高度矩阵:")
    print_matrix(heights1)
    result1 = solution.pacificAtlantic(heights1)
    print("能同时流向太平洋和大西洋的单元格:")
    print_result(result1)
    
    # 测试用例2
    heights2 = [[1]]
    print("\n测试用例2:")
    print("高度矩阵:")
    print_matrix(heights2)
    result2 = solution.pacificAtlantic(heights2)
    print("能同时流向太平洋和大西洋的单元格:")
    print_result(result2)


if __name__ == "__main__":
    main()