"""
高级位集应用实现 - 第一部分
包含LeetCode多个高级位集应用相关题目的解决方案

题目列表:
1. LeetCode 52 - N皇后 II
2. LeetCode 51 - N皇后
3. LeetCode 37 - 解数独
4. LeetCode 36 - 有效的数独
5. LeetCode 212 - 单词搜索 II

时间复杂度分析:
- 位集操作: O(1) 到 O(2^n)
- 空间复杂度: O(1) 到 O(n)

工程化考量:
1. 位集优化: 使用位集优化回溯算法
2. 状态压缩: 使用位运算压缩状态空间
3. 性能优化: 利用位运算的并行性
4. 边界处理: 处理大数、边界值等
"""

import time
from typing import List, Optional
import sys
from collections import defaultdict

class BitSetApplicationsAdvanced:
    """高级位集应用类"""
    
    @staticmethod
    def total_n_queens(n: int) -> int:
        """
        LeetCode 52 - N皇后 II
        n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
        给定一个整数 n，返回 n 皇后不同的解决方案的数量。
        
        方法: 位运算回溯
        时间复杂度: O(n!)
        空间复杂度: O(n)
        
        原理: 使用位运算记录列、主对角线、副对角线的占用情况
        """
        def solve(row: int, columns: int, diagonals1: int, diagonals2: int) -> int:
            if row == n:
                return 1
            
            count = 0
            # 获取可用的位置
            available_positions = ((1 << n) - 1) & ~(columns | diagonals1 | diagonals2)
            
            while available_positions != 0:
                # 获取最低位的1
                position = available_positions & -available_positions
                available_positions &= available_positions - 1
                
                count += solve(row + 1, 
                             columns | position,
                             (diagonals1 | position) << 1,
                             (diagonals2 | position) >> 1)
            
            return count
        
        return solve(0, 0, 0, 0)
    
    @staticmethod
    def solve_n_queens(n: int) -> List[List[str]]:
        """
        LeetCode 51 - N皇后
        n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
        给定一个整数 n，返回所有不同的 n 皇后问题的解决方案。
        
        方法: 位运算回溯
        时间复杂度: O(n!)
        空间复杂度: O(n^2)
        """
        solutions = []
        board = [['.'] * n for _ in range(n)]
        
        def solve(row: int, columns: int, diagonals1: int, diagonals2: int):
            if row == n:
                solutions.append([''.join(row) for row in board])
                return
            
            available_positions = ((1 << n) - 1) & ~(columns | diagonals1 | diagonals2)
            
            while available_positions != 0:
                position = available_positions & -available_positions
                available_positions &= available_positions - 1
                
                # 找到position对应的列
                col = 0
                temp = position
                while temp > 1:
                    temp >>= 1
                    col += 1
                
                board[row][col] = 'Q'
                solve(row + 1,
                      columns | position,
                      (diagonals1 | position) << 1,
                      (diagonals2 | position) >> 1)
                board[row][col] = '.'
        
        solve(0, 0, 0, 0)
        return solutions
    
    @staticmethod
    def solve_sudoku(board: List[List[str]]) -> None:
        """
        LeetCode 37 - 解数独
        编写一个程序，通过已填充的空格来解决数独问题。
        
        方法: 位运算 + 回溯
        时间复杂度: O(9^m) - m为空格数量
        空间复杂度: O(1)
        """
        # 使用位掩码记录每行、每列、每个3x3宫的数字使用情况
        rows = [0] * 9
        cols = [0] * 9
        boxes = [0] * 9
        
        # 初始化已存在的数字
        for i in range(9):
            for j in range(9):
                if board[i][j] != '.':
                    num = int(board[i][j])
                    mask = 1 << (num - 1)
                    rows[i] |= mask
                    cols[j] |= mask
                    boxes[(i//3)*3 + j//3] |= mask
        
        def solve(row: int, col: int) -> bool:
            if row == 9:
                return True
            if col == 9:
                return solve(row + 1, 0)
            if board[row][col] != '.':
                return solve(row, col + 1)
            
            box_index = (row//3)*3 + col//3
            available = ~(rows[row] | cols[col] | boxes[box_index]) & 0x1FF
            
            while available != 0:
                position = available & -available
                num = (position).bit_length()
                
                # 尝试放置数字
                board[row][col] = str(num)
                rows[row] |= position
                cols[col] |= position
                boxes[box_index] |= position
                
                if solve(row, col + 1):
                    return True
                
                # 回溯
                board[row][col] = '.'
                rows[row] &= ~position
                cols[col] &= ~position
                boxes[box_index] &= ~position
                
                available &= available - 1
            
            return False
        
        solve(0, 0)
    
    @staticmethod
    def is_valid_sudoku(board: List[List[str]]) -> bool:
        """
        LeetCode 36 - 有效的数独
        判断一个 9x9 的数独是否有效。只需要根据以下规则，验证已经填入的数字是否有效即可。
        
        方法: 位运算验证
        时间复杂度: O(1) - 固定81个格子
        空间复杂度: O(1)
        """
        rows = [0] * 9
        cols = [0] * 9
        boxes = [0] * 9
        
        for i in range(9):
            for j in range(9):
                if board[i][j] == '.':
                    continue
                
                num = int(board[i][j])
                mask = 1 << (num - 1)
                box_index = (i//3)*3 + j//3
                
                if (rows[i] & mask) or (cols[j] & mask) or (boxes[box_index] & mask):
                    return False
                
                rows[i] |= mask
                cols[j] |= mask
                boxes[box_index] |= mask
        
        return True
    
    @staticmethod
    def find_words(board: List[List[str]], words: List[str]) -> List[str]:
        """
        LeetCode 212 - 单词搜索 II
        给定一个 m x n 二维字符网格 board 和一个单词（字符串）列表 words，找出所有同时在二维网格和字典中出现的单词。
        
        方法: Trie树 + 回溯
        时间复杂度: O(m*n*4^L) - L为单词最大长度
        空间复杂度: O(k*L) - k为单词数量
        """
        # 构建Trie树
        root = BitSetApplicationsAdvanced.build_trie(words)
        result = []
        
        # 回溯搜索
        for i in range(len(board)):
            for j in range(len(board[0])):
                BitSetApplicationsAdvanced.dfs(board, i, j, root, result)
        
        return result
    
    @staticmethod
    def build_trie(words: List[str]) -> dict:
        """构建Trie树"""
        trie = {}
        for word in words:
            node = trie
            for char in word:
                if char not in node:
                    node[char] = {}
                node = node[char]
            node['#'] = word  # 标记单词结束
        return trie
    
    @staticmethod
    def dfs(board: List[List[str]], i: int, j: int, node: dict, result: List[str]):
        """深度优先搜索"""
        if i < 0 or i >= len(board) or j < 0 or j >= len(board[0]) or board[i][j] == '#':
            return
        
        char = board[i][j]
        if char not in node:
            return
        
        next_node = node[char]
        if '#' in next_node:
            word = next_node['#']
            result.append(word)
            del next_node['#']  # 避免重复
        
        board[i][j] = '#'  # 标记已访问
        # 四个方向搜索
        BitSetApplicationsAdvanced.dfs(board, i+1, j, next_node, result)
        BitSetApplicationsAdvanced.dfs(board, i-1, j, next_node, result)
        BitSetApplicationsAdvanced.dfs(board, i, j+1, next_node, result)
        BitSetApplicationsAdvanced.dfs(board, i, j-1, next_node, result)
        board[i][j] = char  # 恢复