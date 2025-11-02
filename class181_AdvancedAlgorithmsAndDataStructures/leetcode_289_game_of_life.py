#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 289. 生命游戏 (Game of Life) - Python版本
题目链接：https://leetcode.com/problems/game-of-life/

算法思路：
使用原地算法，通过特殊标记来同时保存当前状态和下一状态
标记规则：
  - 如果细胞从活到死，标记为2（当前为活，下一状态为死）
  - 如果细胞从死到活，标记为-1（当前为死，下一状态为活）

时间复杂度：O(m*n)
空间复杂度：O(1)

工程化考量：
1. 边界检查：确保不越界访问
2. 原地修改：避免额外空间使用
3. 标记解析：两遍遍历确保正确性
4. 异常处理：空输入检查
5. Python特性：使用列表推导式和生成器表达式优化性能
"""

from typing import List

class Solution:
    def gameOfLife(self, board: List[List[int]]) -> None:
        """
        原地算法解决生命游戏问题
        Args:
            board: 细胞面板，会被原地修改
        """
        if not board or not board[0]:
            return
        
        m, n = len(board), len(board[0])
        
        # 第一遍遍历：应用规则并标记
        for i in range(m):
            for j in range(n):
                live_neighbors = self.count_live_neighbors(board, i, j, m, n)
                
                # 规则1和2：活细胞死亡条件
                if board[i][j] == 1 and (live_neighbors < 2 or live_neighbors > 3):
                    board[i][j] = 2  # 标记为从活到死
                # 规则4：死细胞复活条件
                elif board[i][j] == 0 and live_neighbors == 3:
                    board[i][j] = -1  # 标记为从死到活
                # 规则3：活细胞存活条件（保持1不变）
                # 其他情况：死细胞保持死亡（保持0不变）
        
        # 第二遍遍历：解析标记
        for i in range(m):
            for j in range(n):
                if board[i][j] == 2:
                    board[i][j] = 0  # 从活到死
                elif board[i][j] == -1:
                    board[i][j] = 1  # 从死到活

    def count_live_neighbors(self, board: List[List[int]], i: int, j: int, m: int, n: int) -> int:
        """
        计算细胞周围活细胞数量（考虑标记）
        Args:
            board: 细胞面板
            i: 当前细胞行索引
            j: 当前细胞列索引
            m: 面板行数
            n: 面板列数
        Returns:
            活细胞数量
        """
        count = 0
        
        # 8个方向的偏移量
        directions = [
            (-1, -1), (-1, 0), (-1, 1),
            (0, -1),           (0, 1),
            (1, -1),  (1, 0), (1, 1)
        ]
        
        for dx, dy in directions:
            ni, nj = i + dx, j + dy
            
            # 检查边界
            if 0 <= ni < m and 0 <= nj < n:
                # 当前为活细胞(1)或标记为从活到死(2)的都算作活细胞
                if board[ni][nj] == 1 or board[ni][nj] == 2:
                    count += 1
        
        return count

    def gameOfLifeWithExtraSpace(self, board: List[List[int]]) -> None:
        """
        使用额外空间的解法（非原地，更容易理解）
        Args:
            board: 细胞面板
        """
        if not board or not board[0]:
            return
        
        m, n = len(board), len(board[0])
        next_board = [[0] * n for _ in range(m)]
        
        # 复制当前状态
        for i in range(m):
            for j in range(n):
                next_board[i][j] = board[i][j]
        
        # 应用规则
        for i in range(m):
            for j in range(n):
                live_neighbors = self.count_live_neighbors_simple(board, i, j, m, n)
                
                if board[i][j] == 1:
                    # 活细胞规则
                    if live_neighbors < 2 or live_neighbors > 3:
                        next_board[i][j] = 0  # 死亡
                    else:
                        next_board[i][j] = 1  # 存活
                else:
                    # 死细胞规则
                    if live_neighbors == 3:
                        next_board[i][j] = 1  # 复活
                    else:
                        next_board[i][j] = 0  # 保持死亡
        
        # 更新原数组
        for i in range(m):
            for j in range(n):
                board[i][j] = next_board[i][j]

    def count_live_neighbors_simple(self, board: List[List[int]], i: int, j: int, m: int, n: int) -> int:
        """
        简单计算活细胞数量（不考虑标记）
        """
        count = 0
        directions = [
            (-1, -1), (-1, 0), (-1, 1),
            (0, -1),           (0, 1),
            (1, -1),  (1, 0), (1, 1)
        ]
        
        for dx, dy in directions:
            ni, nj = i + dx, j + dy
            
            if 0 <= ni < m and 0 <= nj < n and board[ni][nj] == 1:
                count += 1
        
        return count

    def gameOfLifeOptimized(self, board: List[List[int]]) -> None:
        """
        优化版本：使用生成器表达式和列表推导式
        Python特性优化版本
        """
        if not board or not board[0]:
            return
        
        m, n = len(board), len(board[0])
        
        # 使用列表推导式创建方向列表
        directions = [(dx, dy) for dx in (-1, 0, 1) for dy in (-1, 0, 1) if not (dx == 0 and dy == 0)]
        
        # 第一遍遍历
        for i in range(m):
            for j in range(n):
                # 使用生成器表达式计算活细胞数量
                live_neighbors = sum(
                    1 for dx, dy in directions
                    if 0 <= i + dx < m and 0 <= j + dy < n
                    and (board[i + dx][j + dy] == 1 or board[i + dx][j + dy] == 2)
                )
                
                if board[i][j] == 1 and (live_neighbors < 2 or live_neighbors > 3):
                    board[i][j] = 2
                elif board[i][j] == 0 and live_neighbors == 3:
                    board[i][j] = -1
        
        # 第二遍遍历
        for i in range(m):
            for j in range(n):
                if board[i][j] == 2:
                    board[i][j] = 0
                elif board[i][j] == -1:
                    board[i][j] = 1

def print_board(board: List[List[int]]) -> None:
    """
    打印面板
    Args:
        board: 细胞面板
    """
    for row in board:
        print(' '.join('█' if cell == 1 else '·' for cell in row))
    print()

def test_game_of_life():
    """
    单元测试
    """
    solution = Solution()
    
    # 测试用例1：闪烁器（Blinker）
    print("=== 测试用例1：闪烁器 ===")
    board1 = [
        [0, 1, 0],
        [0, 1, 0],
        [0, 1, 0]
    ]
    
    print("初始状态:")
    print_board(board1)
    
    solution.gameOfLife(board1)
    print("下一代状态:")
    print_board(board1)
    
    # 测试用例2：滑翔机（Glider）
    print("=== 测试用例2：滑翔机 ===")
    board2 = [
        [0, 1, 0],
        [0, 0, 1],
        [1, 1, 1]
    ]
    
    print("初始状态:")
    print_board(board2)
    
    solution.gameOfLife(board2)
    print("下一代状态:")
    print_board(board2)
    
    # 测试用例3：稳定图案（方块）
    print("=== 测试用例3：稳定图案 ===")
    board3 = [
        [1, 1],
        [1, 1]
    ]
    
    print("初始状态:")
    print_board(board3)
    
    solution.gameOfLife(board3)
    print("下一代状态（应该保持不变）:")
    print_board(board3)
    
    # 测试用例4：边界情况
    print("=== 测试用例4：单细胞 ===")
    board4 = [[1]]
    
    print("初始状态:")
    print_board(board4)
    
    solution.gameOfLife(board4)
    print("下一代状态（应该死亡）:")
    print_board(board4)
    
    # 测试用例5：使用额外空间版本
    print("=== 测试用例5：额外空间版本 ===")
    board5 = [
        [0, 0, 0, 0, 0],
        [0, 0, 1, 0, 0],
        [0, 0, 1, 0, 0],
        [0, 0, 1, 0, 0],
        [0, 0, 0, 0, 0]
    ]
    
    print("初始状态:")
    print_board(board5)
    
    solution.gameOfLifeWithExtraSpace(board5)
    print("下一代状态（额外空间版本）:")
    print_board(board5)
    
    # 测试用例6：优化版本
    print("=== 测试用例6：优化版本 ===")
    board6 = [
        [0, 1, 0],
        [0, 1, 0],
        [0, 1, 0]
    ]
    
    print("初始状态:")
    print_board(board6)
    
    solution.gameOfLifeOptimized(board6)
    print("下一代状态（优化版本）:")
    print_board(board6)

if __name__ == "__main__":
    test_game_of_life()