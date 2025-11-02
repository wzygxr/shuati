#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Alpha-Beta剪枝算法实现（以井字棋为例）

算法原理：
Alpha-Beta剪枝是一种优化极小极大搜索算法的技术，通过剪掉搜索树中不会影响最终结果的分支来减少搜索节点数量。

算法特点：
1. 减少搜索空间，提高搜索效率
2. 不影响最终结果的正确性
3. 剪枝效果与搜索顺序密切相关
4. 最好情况下可以将时间复杂度从O(b^d)降低到O(b^(d/2))

算法步骤：
1. 在极小极大搜索过程中维护alpha和beta值
2. alpha表示最大化玩家能够保证的最小收益
3. beta表示最小化玩家能够保证的最大损失
4. 当alpha >= beta时，剪枝剩余分支

应用场景：
- 博弈树搜索（井字棋、五子棋、象棋等）
- 决策系统
- 对抗性问题求解

时间复杂度：
- 最坏情况：O(b^d)
- 最好情况：O(b^(d/2))
- 平均情况：O(b^(3d/4))

空间复杂度：O(d)，递归栈深度

工程化考量：
1. 移动排序：优先搜索较优的移动可以提高剪枝效果
2. 迭代加深：结合迭代加深搜索使用
3. 转置表：缓存已计算的节点避免重复计算
4. 启发式评估：设计合理的评估函数
5. 边界处理：处理游戏结束状态
6. 性能优化：通过剪枝减少不必要的计算
"""

class AlphaBetaPruning:
    # 井字棋棋盘状态
    EMPTY = 0
    PLAYER_X = 1
    PLAYER_O = 2
    
    # 评估分数
    WIN_SCORE = 10000
    LOSE_SCORE = -10000
    TIE_SCORE = 0
    
    # 棋盘大小
    BOARD_SIZE = 3
    
    @staticmethod
    def alpha_beta_search(board: list[list[int]], depth: int, alpha: float, beta: float, is_maximizing_player: bool) -> int:
        """
        Alpha-Beta剪枝搜索函数
        
        Args:
            board: 当前棋盘状态
            depth: 当前搜索深度
            alpha: Alpha值（最大化玩家的最小保证收益）
            beta: Beta值（最小化玩家的最大保证损失）
            is_maximizing_player: 是否为最大化玩家（PLAYER_X）
            
        Returns:
            最佳评估分数
        """
        # 检查游戏是否结束或达到最大深度
        game_result = AlphaBetaPruning._evaluate_game(board)
        if game_result != -2 or depth == 0:  # -2表示游戏继续
            return game_result
        
        if is_maximizing_player:
            max_eval = float('-inf')
            moves = AlphaBetaPruning._generate_moves(board)
            
            # 启发式排序：优先考虑中心和角落位置
            moves.sort(key=lambda move: (
                0 if move[0] == 1 and move[1] == 1 else  # 中心位置优先
                1 if (move[0] in [0, 2] and move[1] in [0, 2]) else  # 角落位置次之
                2  # 边位置最后
            ))
            
            for move in moves:
                # 执行移动
                board[move[0]][move[1]] = AlphaBetaPruning.PLAYER_X
                
                # 递归搜索
                eval_score = AlphaBetaPruning.alpha_beta_search(board, depth - 1, alpha, beta, False)
                
                # 撤销移动
                board[move[0]][move[1]] = AlphaBetaPruning.EMPTY
                
                max_eval = max(max_eval, eval_score)
                alpha = max(alpha, eval_score)
                
                # Alpha-Beta剪枝
                if beta <= alpha:
                    break  # beta剪枝
            
            return int(max_eval)
        else:
            min_eval = float('inf')
            moves = AlphaBetaPruning._generate_moves(board)
            
            # 启发式排序：优先考虑中心和角落位置
            moves.sort(key=lambda move: (
                0 if move[0] == 1 and move[1] == 1 else  # 中心位置优先
                1 if (move[0] in [0, 2] and move[1] in [0, 2]) else  # 角落位置次之
                2  # 边位置最后
            ))
            
            for move in moves:
                # 执行移动
                board[move[0]][move[1]] = AlphaBetaPruning.PLAYER_O
                
                # 递归搜索
                eval_score = AlphaBetaPruning.alpha_beta_search(board, depth - 1, alpha, beta, True)
                
                # 撤销移动
                board[move[0]][move[1]] = AlphaBetaPruning.EMPTY
                
                min_eval = min(min_eval, eval_score)
                beta = min(beta, eval_score)
                
                # Alpha-Beta剪枝
                if beta <= alpha:
                    break  # alpha剪枝
            
            return int(min_eval)
    
    @staticmethod
    def get_best_move(board: list[list[int]], depth: int, is_maximizing_player: bool) -> tuple[int, int]:
        """
        获取最佳移动
        
        Args:
            board: 当前棋盘状态
            depth: 搜索深度
            is_maximizing_player: 是否为最大化玩家
            
        Returns:
            最佳移动位置 (row, col)
        """
        best_value = float('-inf') if is_maximizing_player else float('inf')
        best_move = (-1, -1)
        moves = AlphaBetaPruning._generate_moves(board)
        
        # 启发式排序：优先考虑中心和角落位置
        moves.sort(key=lambda move: (
            0 if move[0] == 1 and move[1] == 1 else  # 中心位置优先
            1 if (move[0] in [0, 2] and move[1] in [0, 2]) else  # 角落位置次之
            2  # 边位置最后
        ))
        
        for move in moves:
            # 执行移动
            board[move[0]][move[1]] = AlphaBetaPruning.PLAYER_X if is_maximizing_player else AlphaBetaPruning.PLAYER_O
            
            # 评估移动
            move_value = AlphaBetaPruning.alpha_beta_search(
                board, depth - 1,
                float('-inf') if is_maximizing_player else best_value,
                best_value if is_maximizing_player else float('inf'),
                not is_maximizing_player
            )
            
            # 撤销移动
            board[move[0]][move[1]] = AlphaBetaPruning.EMPTY
            
            # 更新最佳移动
            if is_maximizing_player and move_value > best_value:
                best_value = move_value
                best_move = (move[0], move[1])
            elif not is_maximizing_player and move_value < best_value:
                best_value = move_value
                best_move = (move[0], move[1])
        
        return best_move
    
    @staticmethod
    def _generate_moves(board: list[list[int]]) -> list[tuple[int, int]]:
        """
        生成所有可能的移动
        
        Args:
            board: 当前棋盘状态
            
        Returns:
            所有可能的移动列表
        """
        moves = []
        
        for i in range(AlphaBetaPruning.BOARD_SIZE):
            for j in range(AlphaBetaPruning.BOARD_SIZE):
                if board[i][j] == AlphaBetaPruning.EMPTY:
                    moves.append((i, j))
        
        return moves
    
    @staticmethod
    def _evaluate_game(board: list[list[int]]) -> int:
        """
        评估游戏状态
        
        Args:
            board: 当前棋盘状态
            
        Returns:
            游戏结果：WIN_SCORE（X胜）、LOSE_SCORE（O胜）、TIE_SCORE（平局）、-2（游戏继续）
        """
        # 检查行
        for i in range(AlphaBetaPruning.BOARD_SIZE):
            if (board[i][0] != AlphaBetaPruning.EMPTY and 
                board[i][0] == board[i][1] == board[i][2]):
                return (AlphaBetaPruning.WIN_SCORE if board[i][0] == AlphaBetaPruning.PLAYER_X 
                        else AlphaBetaPruning.LOSE_SCORE)
        
        # 检查列
        for j in range(AlphaBetaPruning.BOARD_SIZE):
            if (board[0][j] != AlphaBetaPruning.EMPTY and 
                board[0][j] == board[1][j] == board[2][j]):
                return (AlphaBetaPruning.WIN_SCORE if board[0][j] == AlphaBetaPruning.PLAYER_X 
                        else AlphaBetaPruning.LOSE_SCORE)
        
        # 检查对角线
        if (board[0][0] != AlphaBetaPruning.EMPTY and 
            board[0][0] == board[1][1] == board[2][2]):
            return (AlphaBetaPruning.WIN_SCORE if board[0][0] == AlphaBetaPruning.PLAYER_X 
                    else AlphaBetaPruning.LOSE_SCORE)
        
        if (board[0][2] != AlphaBetaPruning.EMPTY and 
            board[0][2] == board[1][1] == board[2][0]):
            return (AlphaBetaPruning.WIN_SCORE if board[0][2] == AlphaBetaPruning.PLAYER_X 
                    else AlphaBetaPruning.LOSE_SCORE)
        
        # 检查是否平局
        is_full = all(board[i][j] != AlphaBetaPruning.EMPTY 
                     for i in range(AlphaBetaPruning.BOARD_SIZE) 
                     for j in range(AlphaBetaPruning.BOARD_SIZE))
        
        if is_full:
            return AlphaBetaPruning.TIE_SCORE  # 平局
        
        return -2  # 游戏继续
    
    @staticmethod
    def print_board(board: list[list[int]]) -> None:
        """
        打印棋盘
        
        Args:
            board: 棋盘状态
        """
        print("Current Board:")
        for i in range(AlphaBetaPruning.BOARD_SIZE):
            for j in range(AlphaBetaPruning.BOARD_SIZE):
                if board[i][j] == AlphaBetaPruning.EMPTY:
                    print(". ", end="")
                elif board[i][j] == AlphaBetaPruning.PLAYER_X:
                    print("X ", end="")
                elif board[i][j] == AlphaBetaPruning.PLAYER_O:
                    print("O ", end="")
            print()
        print()
    
    @staticmethod
    def is_valid_move(board: list[list[int]], row: int, col: int) -> bool:
        """
        检查移动是否合法
        
        Args:
            board: 棋盘状态
            row: 行索引
            col: 列索引
            
        Returns:
            是否合法
        """
        return (0 <= row < AlphaBetaPruning.BOARD_SIZE and 
                0 <= col < AlphaBetaPruning.BOARD_SIZE and 
                board[row][col] == AlphaBetaPruning.EMPTY)


def main():
    """测试示例"""
    print("=== Alpha-Beta剪枝算法测试（井字棋） ===")
    
    # 初始化空棋盘
    board = [[0 for _ in range(3)] for _ in range(3)]
    
    # 简单测试：评估空棋盘
    eval_score = AlphaBetaPruning.alpha_beta_search(board, 9, float('-inf'), float('inf'), True)
    print(f"空棋盘评估值: {eval_score}")
    
    # 测试：X在中心位置
    board[1][1] = AlphaBetaPruning.PLAYER_X
    AlphaBetaPruning.print_board(board)
    eval_score = AlphaBetaPruning.alpha_beta_search(board, 8, float('-inf'), float('inf'), False)
    print(f"X在中心位置的评估值: {eval_score}")
    
    # 获取最佳移动
    best_move = AlphaBetaPruning.get_best_move(board, 7, False)
    print(f"O的最佳移动: [{best_move[0]}, {best_move[1]}]")
    
    # 模拟一局游戏
    print("\n=== 模拟游戏 ===")
    # 重置棋盘
    for i in range(3):
        for j in range(3):
            board[i][j] = AlphaBetaPruning.EMPTY
    
    is_x_move = True
    moves_count = 0
    
    while AlphaBetaPruning._evaluate_game(board) == -2 and moves_count < 9:
        AlphaBetaPruning.print_board(board)
        move = AlphaBetaPruning.get_best_move(board, 9 - moves_count, is_x_move)
        
        if move[0] != -1 and move[1] != -1:
            board[move[0]][move[1]] = AlphaBetaPruning.PLAYER_X if is_x_move else AlphaBetaPruning.PLAYER_O
            print(f"{'X' if is_x_move else 'O'} 下在 [{move[0]}, {move[1]}]")
            is_x_move = not is_x_move
            moves_count += 1
        else:
            break
    
    AlphaBetaPruning.print_board(board)
    result = AlphaBetaPruning._evaluate_game(board)
    if result == AlphaBetaPruning.WIN_SCORE:
        print("X 获胜！")
    elif result == AlphaBetaPruning.LOSE_SCORE:
        print("O 获胜！")
    else:
        print("平局！")


if __name__ == "__main__":
    main()