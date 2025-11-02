#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
康威生命游戏实现 (Python版本)

算法思路：
康威生命游戏是一个细胞自动机，每个细胞根据其周围8个邻居的状态按照以下规则演化：
1. 活细胞周围活细胞数少于2个，则死亡（孤独）
2. 活细胞周围活细胞数为2或3个，则继续存活
3. 活细胞周围活细胞数多于3个，则死亡（拥挤）
4. 死细胞周围活细胞数为3个，则复活（繁殖）

时间复杂度：O(m*n) 每代
空间复杂度：O(m*n)

应用场景：
1. 生物学：细胞自动机模型
2. 物理学：粒子系统模拟
3. 艺术：生成艺术图案
4. 教育：复杂系统教学
"""

import copy
import time

class GameOfLife:
    """康威生命游戏实现类"""
    
    def __init__(self, initial_board):
        """
        构造函数
        :param initial_board: 初始棋盘状态，二维列表，1表示活细胞，0表示死细胞
        """
        if not initial_board or not initial_board[0]:
            raise ValueError("初始棋盘不能为空")
        
        self.rows = len(initial_board)
        self.cols = len(initial_board[0])
        self.board = copy.deepcopy(initial_board)
    
    def next_generation(self):
        """
        计算下一代的状态
        时间复杂度：O(m*n)
        空间复杂度：O(m*n)
        """
        # 创建新的棋盘来存储下一代状态
        new_board = [[0 for _ in range(self.cols)] for _ in range(self.rows)]
        
        # 遍历每个细胞
        for i in range(self.rows):
            for j in range(self.cols):
                # 计算周围活细胞数量
                live_neighbors = self._count_live_neighbors(i, j)
                
                # 应用生命游戏规则
                if self.board[i][j] == 1:
                    # 活细胞
                    if live_neighbors < 2 or live_neighbors > 3:
                        new_board[i][j] = 0  # 死亡
                    else:
                        new_board[i][j] = 1  # 存活
                else:
                    # 死细胞
                    if live_neighbors == 3:
                        new_board[i][j] = 1  # 繁殖
                    else:
                        new_board[i][j] = 0  # 保持死亡
        
        # 更新棋盘
        self.board = new_board
    
    def next_generation_in_place(self):
        """
        使用原地算法计算下一代状态
        时间复杂度：O(m*n)
        空间复杂度：O(1)
        使用特殊标记：2表示从活到死，-1表示从死到活
        """
        # 遍历每个细胞
        for i in range(self.rows):
            for j in range(self.cols):
                # 计算周围活细胞数量
                live_neighbors = self._count_live_neighbors_with_markers(i, j)
                
                # 应用生命游戏规则
                if self.board[i][j] == 1:
                    # 活细胞
                    if live_neighbors < 2 or live_neighbors > 3:
                        self.board[i][j] = 2  # 标记为从活到死
                    # 否则保持为1，继续存活
                else:
                    # 死细胞
                    if live_neighbors == 3:
                        self.board[i][j] = -1  # 标记为从死到活
                    # 否则保持为0，继续死亡
        
        # 解析标记，恢复真实状态
        for i in range(self.rows):
            for j in range(self.cols):
                if self.board[i][j] == 2:
                    self.board[i][j] = 0  # 死亡
                elif self.board[i][j] == -1:
                    self.board[i][j] = 1  # 新生
    
    def _count_live_neighbors(self, row, col):
        """
        计算指定位置周围的活细胞数量
        :param row: 行索引
        :param col: 列索引
        :return: 活细胞数量
        """
        count = 0
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
            if 0 <= new_row < self.rows and 0 <= new_col < self.cols:
                if self.board[new_row][new_col] == 1:
                    count += 1
        
        return count
    
    def _count_live_neighbors_with_markers(self, row, col):
        """
        在原地算法中计算周围的活细胞数量（考虑标记）
        :param row: 行索引
        :param col: 列索引
        :return: 活细胞数量
        """
        count = 0
        directions = [
            (-1, -1), (-1, 0), (-1, 1),
            (0, -1),           (0, 1),
            (1, -1),  (1, 0), (1, 1)
        ]
        
        for dr, dc in directions:
            new_row = row + dr
            new_col = col + dc
            
            if 0 <= new_row < self.rows and 0 <= new_col < self.cols:
                # 1或2表示之前是活细胞
                if self.board[new_row][new_col] == 1 or self.board[new_row][new_col] == 2:
                    count += 1
        
        return count
    
    def get_board(self):
        """
        获取当前棋盘状态
        :return: 棋盘状态的深拷贝
        """
        return copy.deepcopy(self.board)
    
    def print_board(self):
        """
        打印棋盘状态
        """
        for i in range(self.rows):
            for j in range(self.cols):
                print("█" if self.board[i][j] == 1 else "·", end=" ")
            print()
        print()
    
    @staticmethod
    def test_game_of_life():
        """测试生命游戏"""
        print("=== 测试康威生命游戏 ===")
        
        # 测试用例：闪烁器（Blinker）
        blinker = [
            [0, 1, 0],
            [0, 1, 0],
            [0, 1, 0]
        ]
        
        print("闪烁器 - 初始状态:")
        game = GameOfLife(blinker)
        game.print_board()
        
        print("第1代（原地算法）:")
        game.next_generation_in_place()
        game.print_board()
        
        print("第2代（原地算法）:")
        game.next_generation_in_place()
        game.print_board()
        
        # 测试用例：滑翔机（Glider）
        glider = [
            [0, 1, 0],
            [0, 0, 1],
            [1, 1, 1]
        ]
        
        print("滑翔机 - 初始状态:")
        glider_game = GameOfLife(glider)
        glider_game.print_board()
        
        for i in range(1, 5):
            print(f"滑翔机 - 第{i}代:")
            glider_game.next_generation()
            glider_game.print_board()
        
        # 测试用例：高斯帕机枪（Gosper Glider Gun）
        print("高斯帕机枪 - 初始状态:")
        gosper_glider_gun = [
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        ]
        
        # 添加高斯帕机枪的图案
        # 第一部分
        gosper_glider_gun[5][1] = 1
        gosper_glider_gun[6][1] = 1
        gosper_glider_gun[5][2] = 1
        gosper_glider_gun[6][2] = 1
        
        # 第二部分
        gosper_glider_gun[5][11] = 1
        gosper_glider_gun[6][11] = 1
        gosper_glider_gun[7][11] = 1
        gosper_glider_gun[4][12] = 1
        gosper_glider_gun[8][12] = 1
        gosper_glider_gun[3][13] = 1
        gosper_glider_gun[9][13] = 1
        gosper_glider_gun[3][14] = 1
        gosper_glider_gun[9][14] = 1
        gosper_glider_gun[6][15] = 1
        gosper_glider_gun[4][16] = 1
        gosper_glider_gun[8][16] = 1
        gosper_glider_gun[5][17] = 1
        gosper_glider_gun[6][17] = 1
        gosper_glider_gun[7][17] = 1
        gosper_glider_gun[6][18] = 1
        
        # 第三部分
        gosper_glider_gun[3][21] = 1
        gosper_glider_gun[4][21] = 1
        gosper_glider_gun[5][21] = 1
        gosper_glider_gun[3][22] = 1
        gosper_glider_gun[4][22] = 1
        gosper_glider_gun[5][22] = 1
        gosper_glider_gun[2][23] = 1
        gosper_glider_gun[6][23] = 1
        gosper_glider_gun[1][25] = 1
        gosper_glider_gun[2][25] = 1
        gosper_glider_gun[6][25] = 1
        gosper_glider_gun[7][25] = 1
        
        # 第四部分
        gosper_glider_gun[3][35] = 1
        gosper_glider_gun[4][35] = 1
        gosper_glider_gun[3][36] = 1
        gosper_glider_gun[4][36] = 1
        
        gosper_game = GameOfLife(gosper_glider_gun)
        print("高斯帕机枪 - 前几代:")
        for i in range(1, 3):
            print(f"第{i}代:")
            gosper_game.next_generation()
            gosper_game.print_board()

if __name__ == "__main__":
    GameOfLife.test_game_of_life()