#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
图像渲染 (Flood Fill)
有一幅以 m x n 的二维整数数组表示的图画 image ，其中 image[i][j] 表示该图画的像素值大小。
你也被给予三个整数 sr , sc 和 newColor 。你应该从像素 image[sr][sc] 开始对图像进行 上色填充 。
为了完成 上色工作 ，从初始像素开始，记录初始坐标的 上下左右四个方向上 像素值与初始坐标相同的相连像素点，
接着再记录这四个方向上符合条件的像素点与他们对应 四个方向上 像素值与初始坐标相同的相连像素点，……，重复该过程。
将所有有记录的像素点的颜色值改为 newColor 。
最后返回 经过上色渲染后的图像 。

测试链接: https://leetcode.cn/problems/flood-fill/

解题思路:
使用Flood Fill算法，从起始点开始进行深度优先搜索(DFS)或广度优先搜索(BFS)，将所有与起始点相连且颜色相同的像素点修改为新颜色。

时间复杂度: O(m*n) - 最坏情况下需要遍历整个图像
空间复杂度: O(m*n) - 递归调用栈的深度最多为m*n (DFS) 或队列空间最多为m*n (BFS)
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
1. 空图像
2. 单像素图像
3. 所有像素颜色相同
4. 新颜色与原颜色相同

性能优化:
1. 提前判断新颜色与原颜色是否相同
2. 使用方向数组简化代码
3. 可以用BFS替代DFS避免栈溢出
"""

from typing import List
from collections import deque


class Solution:
    def __init__(self):
        # 四个方向的偏移量：上、下、左、右
        self.dx = [-1, 1, 0, 0]
        self.dy = [0, 0, -1, 1]
    
    def flood_fill(self, image: List[List[int]], sr: int, sc: int, new_color: int) -> List[List[int]]:
        """
        图像渲染主函数 (DFS版本)
        
        Args:
            image: 二维图像数组
            sr: 起始行坐标
            sc: 起始列坐标
            new_color: 新颜色值
            
        Returns:
            渲染后的图像
        """
        # 边界条件检查
        if not image or not image[0]:
            return image
        
        rows, cols = len(image), len(image[0])
        
        # 检查坐标是否越界
        if sr < 0 or sr >= rows or sc < 0 or sc >= cols:
            return image
        
        original_color = image[sr][sc]
        
        # 如果新颜色与原颜色相同，直接返回原图像
        if original_color == new_color:
            return image
        
        # 执行Flood Fill算法
        self._dfs(image, sr, sc, original_color, new_color, rows, cols)
        
        return image
    
    def _dfs(self, image: List[List[int]], x: int, y: int, original_color: int, new_color: int, rows: int, cols: int) -> None:
        """
        深度优先搜索实现Flood Fill
        
        Args:
            image: 图像数组
            x: 当前行坐标
            y: 当前列坐标
            original_color: 原始颜色
            new_color: 新颜色
            rows: 行数
            cols: 列数
        """
        # 边界检查和颜色检查
        if x < 0 or x >= rows or y < 0 or y >= cols or image[x][y] != original_color:
            return
        
        # 修改当前像素颜色
        image[x][y] = new_color
        
        # 递归处理四个方向的相邻像素
        for i in range(4):
            new_x = x + self.dx[i]
            new_y = y + self.dy[i]
            self._dfs(image, new_x, new_y, original_color, new_color, rows, cols)
    
    def flood_fill_bfs(self, image: List[List[int]], sr: int, sc: int, new_color: int) -> List[List[int]]:
        """
        广度优先搜索实现Flood Fill (非递归版本)
        
        Args:
            image: 二维图像数组
            sr: 起始行坐标
            sc: 起始列坐标
            new_color: 新颜色值
            
        Returns:
            渲染后的图像
        """
        # 边界条件检查
        if not image or not image[0]:
            return image
        
        rows, cols = len(image), len(image[0])
        
        # 检查坐标是否越界
        if sr < 0 or sr >= rows or sc < 0 or sc >= cols:
            return image
        
        original_color = image[sr][sc]
        
        # 如果新颜色与原颜色相同，直接返回原图像
        if original_color == new_color:
            return image
        
        # BFS实现
        queue = deque([(sr, sc)])
        image[sr][sc] = new_color
        
        while queue:
            x, y = queue.popleft()
            
            # 处理四个方向的相邻像素
            for i in range(4):
                new_x = x + self.dx[i]
                new_y = y + self.dy[i]
                
                # 边界检查和颜色检查
                if (0 <= new_x < rows and 0 <= new_y < cols and 
                    image[new_x][new_y] == original_color):
                    image[new_x][new_y] = new_color
                    queue.append((new_x, new_y))
        
        return image


# 测试方法
def print_image(image: List[List[int]]) -> None:
    """打印图像"""
    for row in image:
        print(' '.join(map(str, row)))


def main():
    solution = Solution()
    
    # 测试用例1
    image1 = [[1, 1, 1], [1, 1, 0], [1, 0, 1]]
    print("测试用例1:")
    print("原图像:")
    print_image(image1)
    solution.flood_fill(image1, 1, 1, 2)
    print("渲染后:")
    print_image(image1)
    
    # 测试用例2
    image2 = [[0, 0, 0], [0, 0, 0]]
    print("\n测试用例2:")
    print("原图像:")
    print_image(image2)
    solution.flood_fill_bfs(image2, 0, 0, 2)
    print("渲染后:")
    print_image(image2)


# 补充题目1: 扫雷游戏 (Minesweeper)
# 来源: LeetCode 529
# 题目链接: https://leetcode.cn/problems/minesweeper/
# 题目描述: 
# 给定一个代表游戏板的二维字符矩阵。'M'代表一个未挖出的地雷，'E'代表一个未挖出的空方块，
# 'B'代表没有相邻（上，下，左，右，和所有4个对角线）地雷的已挖出的空白方块，
# 数字（'1' 到 '8'）表示有多少地雷与这块已挖出的方块相邻，'X'则表示一个已挖出的地雷。
# 现在给出在所有未挖出的方块中（'M'或者'E'）的一个点击位置（行和列索引），
# 根据以下规则，返回相应位置被点击后对应的面板：
# 1. 如果一个地雷（'M'）被挖出，游戏就结束了- 把它改为'X'。
# 2. 如果一个没有相邻地雷的空方块（'E'）被挖出，修改它为（'B'），并且所有和其相邻的未挖出方块都应该被递归地揭露。
# 3. 如果一个至少与一个地雷相邻的空方块（'E'）被挖出，修改它为数字（'1'到'8'），表示相邻地雷的数量。
# 4. 如果在此次点击中，若无更多方块可被揭露，则返回面板。
# 
# 解题思路:
# 使用Flood Fill算法，从点击位置开始进行深度优先搜索：
# 1. 如果点击的是地雷(M)，直接标记为X返回
# 2. 如果点击的是空地(E)，计算周围地雷数量：
#    a. 如果周围有地雷，将当前位置标记为对应的数字
#    b. 如果周围没有地雷，将当前位置标记为B，并递归地对周围8个方向进行相同操作
# 
# 时间复杂度: O(m*n) - 最坏情况下需要遍历整个游戏板
# 空间复杂度: O(m*n) - 递归调用栈的深度最多为m*n
# 是否最优解: 是
def updateBoard(board, click):
    if not board or not board[0] or not click or len(click) != 2:
        return board
    
    rows, cols = len(board), len(board[0])
    x, y = click[0], click[1]
    
    # 检查坐标是否越界
    if x < 0 or x >= rows or y < 0 or y >= cols:
        return board
    
    # 如果点击的是地雷，直接标记为X
    if board[x][y] == 'M':
        board[x][y] = 'X'
        return board
    
    # 8个方向的偏移量
    dirs = [
        (-1, -1), (-1, 0), (-1, 1),
        (0, -1),          (0, 1),
        (1, -1),  (1, 0),  (1, 1)
    ]
    
    # DFS实现
    def dfs(x, y):
        # 如果当前位置不是未挖出的空方块，直接返回
        if x < 0 or x >= rows or y < 0 or y >= cols or board[x][y] != 'E':
            return
        
        # 计算周围地雷数量
        mine_count = 0
        for dx, dy in dirs:
            nx, ny = x + dx, y + dy
            if 0 <= nx < rows and 0 <= ny < cols and board[nx][ny] == 'M':
                mine_count += 1
        
        if mine_count > 0:
            # 如果周围有地雷，标记为对应的数字
            board[x][y] = str(mine_count)
        else:
            # 如果周围没有地雷，标记为B，并继续搜索周围的方块
            board[x][y] = 'B'
            for dx, dy in dirs:
                nx, ny = x + dx, y + dy
                dfs(nx, ny)
    
    # 执行DFS
    dfs(x, y)
    
    return board

# 补充题目2: 衣橱整理
# 来源: LeetCode 面试题 04.04
# 题目链接: https://leetcode.cn/problems/robot-in-a-grid-lcci/
# 题目描述:
# 机器人在一个无限大小的网格上行走，从点 (0, 0) 处开始出发，面向北方。该机器人可以接收以下三种类型的命令：
# -2：向左转 90 度
# -1：向右转 90 度
# 1 <= x <= 9：向前移动 x 个单位长度
# 在网格上有一些障碍物，障碍物用数组 obstacles 表示，其中 obstacles[i] = [xi, yi] 表示在 (xi, yi) 处有一个障碍物。
# 机器人无法走到障碍物上，它将停留在障碍物的前一个网格方块上，但仍然可以继续该路线的其余部分。
# 返回从原点到机器人的最大欧式距离的平方。
# 
# 解题思路:
# 使用Flood Fill算法的变种，模拟机器人的移动：
# 1. 使用集合存储障碍物坐标，方便快速查询
# 2. 跟踪机器人当前位置和方向
# 3. 根据命令执行相应操作：
#    a. 转向命令：更新方向
#    b. 移动命令：尝试向前移动，遇到障碍物则停止
# 4. 记录并更新最大距离
# 
# 时间复杂度: O(n) - n为命令数量，每个命令最多执行9次移动
# 空间复杂度: O(m) - m为障碍物数量，用于存储障碍物集合
# 是否最优解: 是
def robotSim(commands, obstacles):
    # 定义四个方向的移动：北、东、南、西
    dirs = [(0, 1), (1, 0), (0, -1), (-1, 0)]
    direction = 0  # 初始方向为北
    x, y = 0, 0  # 初始位置
    max_dist_sq = 0
    
    # 将障碍物存储在集合中，方便快速查询
    obstacle_set = set()
    for obs in obstacles:
        obstacle_set.add((obs[0], obs[1]))
    
    for cmd in commands:
        if cmd == -2:
            # 向左转90度
            direction = (direction + 3) % 4
        elif cmd == -1:
            # 向右转90度
            direction = (direction + 1) % 4
        else:
            # 向前移动cmd步
            for _ in range(cmd):
                nx = x + dirs[direction][0]
                ny = y + dirs[direction][1]
                
                # 检查是否有障碍物
                if (nx, ny) not in obstacle_set:
                    x, y = nx, ny
                    # 更新最大距离平方
                    max_dist_sq = max(max_dist_sq, x * x + y * y)
                else:
                    # 遇到障碍物，停止移动
                    break
    
    return max_dist_sq

# 补充题目3: 岛屿数量 (Number of Islands)
# 来源: LeetCode 200
# 题目链接: https://leetcode.cn/problems/number-of-islands/
# 题目描述:
# 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
# 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
# 此外，你可以假设该网格的四条边均被水包围。
# 
# 解题思路:
# 使用Flood Fill算法，每当遇到一个未访问过的陆地('1')，就进行深度优先搜索：
# 1. 遍历整个网格，寻找未访问过的陆地
# 2. 当找到陆地时，岛屿计数加1，并调用DFS/BFS将与该陆地相连的所有陆地标记为已访问
# 3. 继续遍历，直到所有单元格都被检查过
# 
# 时间复杂度: O(m*n) - 每个单元格最多被访问一次
# 空间复杂度: O(m*n) - 递归调用栈的深度在最坏情况下为m*n
# 是否最优解: 是
def numIslands(grid):
    if not grid or not grid[0]:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    count = 0
    
    # 4个方向的偏移量：上、右、下、左
    dirs = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    # DFS实现
    def dfs(i, j):
        # 边界条件检查
        if i < 0 or i >= rows or j < 0 or j >= cols or grid[i][j] != '1':
            return
        
        # 标记为已访问
        grid[i][j] = '0'
        
        # 向四个方向扩展
        for dx, dy in dirs:
            dfs(i + dx, j + dy)
    
    for i in range(rows):
        for j in range(cols):
            # 找到未访问的陆地
            if grid[i][j] == '1':
                count += 1
                # 使用DFS标记所有相连的陆地
                dfs(i, j)
    
    return count

# 执行主函数
if __name__ == "__main__":
    main()