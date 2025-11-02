"""
LeetCode 51. N 皇后

题目描述：
n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
给你一个整数 n ，返回所有不同的 n 皇后问题 的解决方案。
每一种解法包含一个不同的 n 皇后问题 的棋子放置方案，该方案中 'Q' 和 '.' 分别代表了皇后和空位。

示例：
输入：n = 4
输出：[[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]

输入：n = 1
输出：[["Q"]]

提示：
1 <= n <= 9

链接：https://leetcode.cn/problems/n-queens/
"""

class Solution:
    def solveNQueens(self, n):
        """
        解决N皇后问题
        
        算法思路：
        1. 使用回溯算法解决N皇后问题
        2. 按行放置皇后，每行放置一个
        3. 对于每一行，尝试在每一列放置皇后
        4. 检查当前位置是否与已放置的皇后冲突
        5. 如果不冲突，递归处理下一行
        6. 如果冲突，尝试下一列
        7. 如果所有列都尝试过都不行，回溯到上一行
        
        时间复杂度：O(N!)，第一行有N种选择，第二行最多有N-1种选择，以此类推
        空间复杂度：O(N^2)，棋盘空间和递归栈深度
        
        :param n: 皇后数量和棋盘大小
        :return: 所有解决方案
        """
        result = []
        board = [['.' for _ in range(n)] for _ in range(n)]
        
        self.backtrack(result, board, 0)
        return result
    
    def backtrack(self, result, board, row):
        """
        回溯函数解决N皇后问题
        
        :param result: 结果列表
        :param board: 棋盘
        :param row: 当前行
        """
        # 终止条件：已放置完所有皇后
        if row == len(board):
            # 将棋盘转换为字符串列表
            solution = [''.join(row) for row in board]
            result.append(solution)
            return
        
        # 在当前行的每一列尝试放置皇后
        for col in range(len(board)):
            if self.isValid(board, row, col):
                board[row][col] = 'Q'
                self.backtrack(result, board, row + 1)
                board[row][col] = '.'  # 回溯
    
    def isValid(self, board, row, col):
        """
        检查在指定位置放置皇后是否合法
        
        :param board: 棋盘
        :param row: 行索引
        :param col: 列索引
        :return: 是否合法
        """
        # 检查列
        for i in range(row):
            if board[i][col] == 'Q':
                return False
        
        # 检查左上对角线
        i, j = row - 1, col - 1
        while i >= 0 and j >= 0:
            if board[i][j] == 'Q':
                return False
            i -= 1
            j -= 1
        
        # 检查右上对角线
        i, j = row - 1, col + 1
        while i >= 0 and j < len(board):
            if board[i][j] == 'Q':
                return False
            i -= 1
            j += 1
        
        return True


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    n1 = 4
    result1 = solution.solveNQueens(n1)
    print(f"输入: n = {n1}")
    print("输出:")
    for solution_board in result1:
        for row in solution_board:
            print(row)
        print()
    
    # 测试用例2
    n2 = 1
    result2 = solution.solveNQueens(n2)
    print(f"输入: n = {n2}")
    print("输出:")
    for solution_board in result2:
        for row in solution_board:
            print(row)
        print()


if __name__ == "__main__":
    main()