#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
剪枝体系 (Pruning Techniques)

算法原理：
剪枝是一种优化技术，通过提前终止不可能产生最优解的搜索分支来减少搜索空间，
从而提高算法效率。剪枝技术广泛应用于回溯算法、博弈树搜索、分支限界等场景。

算法特点：
1. 减少搜索空间，提高算法效率
2. 不影响最终结果的正确性
3. 需要设计合适的剪枝条件
4. 剪枝效果与问题特性密切相关

应用场景：
- 回溯算法（N皇后、数独等）
- 博弈树搜索（Alpha-Beta剪枝）
- 分支限界算法
- 组合优化问题

剪枝类型：
1. 可行性剪枝：提前判断当前分支是否可能产生可行解
2. 最优性剪枝：提前判断当前分支是否可能产生更优解
3. 记忆化剪枝：避免重复计算相同子问题
4. 启发式剪枝：基于启发信息进行剪枝

时间复杂度：取决于具体问题和剪枝效果
空间复杂度：取决于具体实现
"""

from typing import List, Tuple, Dict
import time


class PruningTechniques:
    @staticmethod
    def solve_n_queens(n: int) -> int:
        """
        N皇后问题 - 可行性剪枝示例
        
        Args:
            n: 皇后数量
            
        Returns:
            所有解的数量
        """
        queens = [0] * n  # queens[i]表示第i行皇后所在的列
        return PruningTechniques._backtrack(queens, 0, n)
    
    @staticmethod
    def _backtrack(queens: List[int], row: int, n: int) -> int:
        """
        回溯算法求解N皇后问题
        
        Args:
            queens: 皇后位置数组
            row: 当前行
            n: 皇后数量
            
        Returns:
            解的数量
        """
        # 递归终止条件
        if row == n:
            return 1  # 找到一个解
        
        count = 0
        
        # 在当前行尝试每一列
        for col in range(n):
            # 可行性剪枝：检查当前位置是否合法
            if PruningTechniques._is_valid(queens, row, col):
                queens[row] = col  # 放置皇后
                count += PruningTechniques._backtrack(queens, row + 1, n)  # 递归处理下一行
                # 回溯时不需要显式重置，因为下一次循环会覆盖
        
        return count
    
    @staticmethod
    def _is_valid(queens: List[int], row: int, col: int) -> bool:
        """
        检查在(row, col)位置放置皇后是否合法
        
        Args:
            queens: 皇后位置数组
            row: 行号
            col: 列号
            
        Returns:
            是否合法
        """
        # 检查之前行的皇后是否与当前位置冲突
        for i in range(row):
            # 检查列冲突
            if queens[i] == col:
                return False
            
            # 检查对角线冲突
            if abs(queens[i] - col) == abs(i - row):
                return False
        
        return True
    
    @staticmethod
    def knapsack_with_pruning(items: List[Tuple[int, int]], capacity: int) -> int:
        """
        0-1背包问题求解（带剪枝优化）
        
        Args:
            items: 物品列表 [(重量, 价值), ...]
            capacity: 背包容量
            
        Returns:
            最大价值
        """
        # 按价值密度排序，用于剪枝
        sorted_items = sorted(items, key=lambda x: x[1] / x[0], reverse=True)
        
        return PruningTechniques._knapsack_backtrack(
            sorted_items, capacity, 0, 0, 0, 0
        )
    
    @staticmethod
    def _knapsack_backtrack(items: List[Tuple[int, int]], capacity: int, 
                          current_index: int, current_weight: int, 
                          current_value: int, best_value: int) -> int:
        """
        0-1背包回溯算法（带剪枝）
        
        Args:
            items: 物品列表 [(重量, 价值), ...]
            capacity: 背包容量
            current_index: 当前物品索引
            current_weight: 当前重量
            current_value: 当前价值
            best_value: 当前最优价值
            
        Returns:
            最大价值
        """
        # 更新最优解
        best_value = max(best_value, current_value)
        
        # 递归终止条件
        if current_index == len(items):
            return best_value
        
        # 最优性剪枝：计算上界
        upper_bound = PruningTechniques._calculate_upper_bound(
            items, capacity, current_index, current_weight, current_value
        )
        
        # 如果上界不大于当前最优值，则剪枝
        if upper_bound <= best_value:
            return best_value
        
        current_item = items[current_index]
        
        # 选择当前物品（可行性剪枝）
        if current_weight + current_item[0] <= capacity:
            best_value = PruningTechniques._knapsack_backtrack(
                items, capacity, current_index + 1, 
                current_weight + current_item[0], 
                current_value + current_item[1], 
                best_value
            )
        
        # 不选择当前物品
        best_value = PruningTechniques._knapsack_backtrack(
            items, capacity, current_index + 1, 
            current_weight, current_value, best_value
        )
        
        return best_value
    
    @staticmethod
    def _calculate_upper_bound(items: List[Tuple[int, int]], capacity: int, 
                             current_index: int, current_weight: int, 
                             current_value: int) -> int:
        """
        计算0-1背包问题的上界（用于最优性剪枝）
        
        Args:
            items: 物品列表 [(重量, 价值), ...]
            capacity: 背包容量
            current_index: 当前物品索引
            current_weight: 当前重量
            current_value: 当前价值
            
        Returns:
            上界估计值
        """
        remaining_capacity = capacity - current_weight
        bound = current_value
        
        # 贪心法计算上界：按价值密度选择物品
        for i in range(current_index, len(items)):
            if remaining_capacity <= 0:
                break
                
            weight, value = items[i]
            if weight <= remaining_capacity:
                # 完全装入
                bound += value
                remaining_capacity -= weight
            else:
                # 部分装入
                bound += int((value / weight) * remaining_capacity)
                remaining_capacity = 0
        
        return int(bound)
    
    class FibonacciWithMemoization:
        """记忆化斐波那契数列 - 记忆化剪枝示例"""
        
        def __init__(self):
            self.memo: Dict[int, int] = {}
        
        def fibonacci(self, n: int) -> int:
            """
            计算第n个斐波那契数（带记忆化）
            
            Args:
                n: 序号
                
            Returns:
                第n个斐波那契数
            """
            # 基础情况
            if n <= 1:
                return n
            
            # 记忆化剪枝：如果已经计算过，直接返回
            if n in self.memo:
                return self.memo[n]
            
            # 递归计算并存储结果
            result = self.fibonacci(n - 1) + self.fibonacci(n - 2)
            self.memo[n] = result
            
            return result
        
        def clear_memo(self) -> None:
            """清空记忆化缓存"""
            self.memo.clear()
    
    class AlphaBetaPruning:
        """Alpha-Beta剪枝 - 博弈树搜索剪枝示例"""
        
        MAX_DEPTH = 6  # 最大搜索深度
        WIN_SCORE = 10000  # 获胜分数
        LOSE_SCORE = -10000  # 失败分数
        
        @staticmethod
        def alpha_beta_search(board: List[List[int]], depth: int, alpha: int, beta: int, 
                            is_maximizing: bool) -> int:
            """
            Alpha-Beta剪枝搜索
            
            Args:
                board: 棋盘状态
                depth: 当前深度
                alpha: Alpha值
                beta: Beta值
                is_maximizing: 是否最大化玩家
                
            Returns:
                评估分数
            """
            # 终止条件：达到最大深度或游戏结束
            if depth == 0 or PruningTechniques.AlphaBetaPruning._is_game_over(board):
                return PruningTechniques.AlphaBetaPruning._evaluate_board(board)
            
            if is_maximizing:
                max_eval = float('-inf')
                moves = PruningTechniques.AlphaBetaPruning._generate_moves(board, True)
                
                for move in moves:
                    # 执行移动
                    PruningTechniques.AlphaBetaPruning._make_move(board, move, True)
                    
                    # 递归搜索
                    eval_score = PruningTechniques.AlphaBetaPruning.alpha_beta_search(
                        board, depth - 1, alpha, beta, False
                    )
                    
                    # 撤销移动
                    PruningTechniques.AlphaBetaPruning._undo_move(board, move)
                    
                    max_eval = max(max_eval, eval_score)
                    alpha = max(alpha, eval_score)
                    
                    # Alpha-Beta剪枝
                    if beta <= alpha:
                        break  # beta剪枝
                
                return int(max_eval)
            else:
                min_eval = float('inf')
                moves = PruningTechniques.AlphaBetaPruning._generate_moves(board, False)
                
                for move in moves:
                    # 执行移动
                    PruningTechniques.AlphaBetaPruning._make_move(board, move, False)
                    
                    # 递归搜索
                    eval_score = PruningTechniques.AlphaBetaPruning.alpha_beta_search(
                        board, depth - 1, alpha, beta, True
                    )
                    
                    # 撤销移动
                    PruningTechniques.AlphaBetaPruning._undo_move(board, move)
                    
                    min_eval = min(min_eval, eval_score)
                    beta = min(beta, eval_score)
                    
                    # Alpha-Beta剪枝
                    if beta <= alpha:
                        break  # alpha剪枝
                
                return int(min_eval)
        
        @staticmethod
        def _is_game_over(board: List[List[int]]) -> bool:
            """
            检查游戏是否结束
            
            Args:
                board: 棋盘
                
            Returns:
                是否结束
            """
            # 简化实现，实际游戏中需要根据具体规则判断
            return False
        
        @staticmethod
        def _evaluate_board(board: List[List[int]]) -> int:
            """
            评估棋盘状态
            
            Args:
                board: 棋盘
                
            Returns:
                评估分数
            """
            # 简化实现，实际评估函数需要根据具体游戏设计
            return 0
        
        @staticmethod
        def _generate_moves(board: List[List[int]], is_maximizing: bool) -> List[List[int]]:
            """
            生成所有可能的移动
            
            Args:
                board: 棋盘
                is_maximizing: 是否最大化玩家
                
            Returns:
                移动列表
            """
            # 简化实现，实际需要根据具体游戏生成移动
            return []
        
        @staticmethod
        def _make_move(board: List[List[int]], move: List[int], is_maximizing: bool) -> None:
            """
            执行移动
            
            Args:
                board: 棋盘
                move: 移动
                is_maximizing: 是否最大化玩家
            """
            # 简化实现，实际需要根据具体游戏执行移动
            pass
        
        @staticmethod
        def _undo_move(board: List[List[int]], move: List[int]) -> None:
            """
            撤销移动
            
            Args:
                board: 棋盘
                move: 移动
            """
            # 简化实现，实际需要根据具体游戏撤销移动
            pass


def main():
    """测试示例"""
    print("=== 剪枝技术测试 ===")
    
    # 测试N皇后问题
    print("\n1. N皇后问题剪枝测试:")
    n_values = [4, 8]
    for n in n_values:
        start_time = time.time()
        solutions = PruningTechniques.solve_n_queens(n)
        end_time = time.time()
        
        print(f"{n}皇后问题: {solutions}个解, 时间: {(end_time - start_time) * 1000:.2f} ms")
    
    # 测试0-1背包问题
    print("\n2. 0-1背包问题剪枝测试:")
    items = [(10, 60), (20, 100), (30, 120), (15, 80), (25, 90)]
    capacity = 50
    
    start_time = time.time()
    max_value = PruningTechniques.knapsack_with_pruning(items, capacity)
    end_time = time.time()
    
    print(f"背包容量: {capacity}, 最大价值: {max_value}, 时间: {(end_time - start_time) * 1000:.2f} ms")
    
    # 测试记忆化斐波那契
    print("\n3. 记忆化斐波那契剪枝测试:")
    fib = PruningTechniques.FibonacciWithMemoization()
    
    fib_indices = [30, 35, 40]
    for n in fib_indices:
        fib.clear_memo()  # 清空缓存
        start_time2 = time.time()
        result = fib.fibonacci(n)
        end_time2 = time.time()
        
        print(f"F({n}) = {result}, 时间: {(end_time2 - start_time2) * 1000:.2f} ms")
    
    # 测试Alpha-Beta剪枝（概念演示）
    print("\n4. Alpha-Beta剪枝概念演示:")
    print("Alpha-Beta剪枝在博弈树搜索中能有效减少节点访问数量")
    print("对于深度为d的完全二叉树，不剪枝需要访问O(b^d)个节点")
    print("使用Alpha-Beta剪枝后，最好情况下只需要访问O(b^(d/2))个节点")


if __name__ == "__main__":
    main()