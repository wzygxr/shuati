#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 289. Game of Life 解决方案

题目链接: https://leetcode.com/problems/game-of-life/
题目描述: 实现康威生命游戏的下一个状态
解题思路: 使用原地算法，通过特殊标记避免额外空间

时间复杂度: O(m*n) - m行n列
空间复杂度: O(1) - 原地算法
"""

from typing import List

class Solution:
    def gameOfLife(self, board: List[List[int]]) -> None:
        """
        计算生命游戏的下一个状态（原地算法）
        
        Args:
            board: 当前状态的二维数组
        """
        # 检查输入有效性
        if not board or not board[0]:
            return
        
        rows = len(board)
        cols = len(board[0])
        
        # 编码规则：
        # 0: 死细胞 -> 死细胞
        # 1: 活细胞 -> 活细胞
        # 2: 活细胞 -> 死细胞
        # 3: 死细胞 -> 活细胞
        
        # 第一遍遍历：计算每个细胞的下一个状态并用编码标记
        for i in range(rows):
            for j in range(cols):
                live_neighbors = self._count_live_neighbors(board, i, j)
                
                # 应用生命游戏规则
                if board[i][j] == 1:  # 活细胞
                    if live_neighbors < 2 or live_neighbors > 3:
                        board[i][j] = 2  # 标记为将死亡
                    # 否则保持为1，继续存活
                else:  # 死细胞
                    if live_neighbors == 3:
                        board[i][j] = 3  # 标记为将复活
                    # 否则保持为0，继续死亡
        
        # 第二遍遍历：解码，将标记转换回0和1
        for i in range(rows):
            for j in range(cols):
                board[i][j] %= 2  # 2 -> 0, 3 -> 1
    
    def _count_live_neighbors(self, board: List[List[int]], row: int, col: int) -> int:
        """
        计算指定位置周围活细胞的数量
        
        Args:
            board: 当前状态的二维数组
            row: 行索引
            col: 列索引
            
        Returns:
            周围活细胞的数量
        """
        live_neighbors = 0
        rows = len(board)
        cols = len(board[0])
        
        # 8个方向的偏移
        directions = [
            (-1, -1), (-1, 0), (-1, 1),
            (0, -1),           (0, 1),
            (1, -1),  (1, 0), (1, 1)
        ]
        
        for dr, dc in directions:
            new_row = row + dr
            new_col = col + dc
            
            # 检查边界并计算活细胞
            if 0 <= new_row < rows and 0 <= new_col < cols:
                # 1和2表示原始状态为活细胞（1：保持活，2：将死亡）
                if board[new_row][new_col] == 1 or board[new_row][new_col] == 2:
                    live_neighbors += 1
        
        return live_neighbors
    
    def gameOfLifeWithExtraSpace(self, board: List[List[int]]) -> None:
        """
        使用额外空间计算下一个状态（用于对比）
        
        Args:
            board: 当前状态的二维数组
        """
        # 检查输入有效性
        if not board or not board[0]:
            return
        
        rows = len(board)
        cols = len(board[0])
        
        # 创建新数组存储下一个状态
        next_board = [[0 for _ in range(cols)] for _ in range(rows)]
        
        # 计算每个细胞的下一个状态
        for i in range(rows):
            for j in range(cols):
                live_neighbors = self._count_live_neighbors(board, i, j)
                
                # 应用生命游戏规则
                if board[i][j] == 1:  # 活细胞
                    if live_neighbors < 2 or live_neighbors > 3:
                        next_board[i][j] = 0  # 死亡：人口稀少或过度拥挤
                    else:
                        next_board[i][j] = 1  # 存活
                else:  # 死细胞
                    if live_neighbors == 3:
                        next_board[i][j] = 1  # 繁殖
                    else:
                        next_board[i][j] = 0  # 保持死亡
        
        # 更新原数组
        for i in range(rows):
            for j in range(cols):
                board[i][j] = next_board[i][j]
    
    def print_board(self, board: List[List[int]]) -> None:
        """
        打印棋盘状态
        
        Args:
            board: 棋盘状态
        """
        if not board:
            print("null")
            return
        
        for i in range(len(board)):
            for j in range(len(board[0])):
                print(board[i][j], end=" ")
            print()
        print()
    
    @staticmethod
    def test_solution():
        """测试方法"""
        solution = Solution()
        
        # 测试用例1：闪烁器（Blinker）
        print("=== 测试用例1：闪烁器 ===")
        board1 = [
            [0, 1, 0],
            [0, 1, 0],
            [0, 1, 0]
        ]
        
        print("初始状态:")
        solution.print_board(board1)
        
        solution.gameOfLife(board1)
        print("下一个状态:")
        solution.print_board(board1)
        
        # 测试用例2：滑翔机（Glider）
        print("=== 测试用例2：滑翔机 ===")
        board2 = [
            [0, 1, 0, 0],
            [0, 0, 1, 0],
            [1, 1, 1, 0],
            [0, 0, 0, 0]
        ]
        
        print("初始状态:")
        solution.print_board(board2)
        
        solution.gameOfLife(board2)
        print("下一个状态:")
        solution.print_board(board2)
        
        # 测试用例3：使用额外空间的方法
        print("=== 测试用例3：使用额外空间的方法 ===")
        board3 = [
            [0, 1, 0],
            [0, 1, 0],
            [0, 1, 0]
        ]
        
        print("初始状态:")
        solution.print_board(board3)
        
        solution.gameOfLifeWithExtraSpace(board3)
        print("下一个状态（额外空间）:")
        solution.print_board(board3)

if __name__ == "__main__":
    Solution.test_solution()