#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 52. N皇后 II

题目描述：
n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
给定一个整数 n，返回 n 皇后不同的解决方案的数量。

示例：
输入: 4
输出: 2
解释: 4 皇后问题存在两个不同的解法。

输入: 1
输出: 1

提示：
1 <= n <= 9

链接：https://leetcode.cn/problems/n-queens-ii/

算法思路：
1. 使用回溯算法解决N皇后问题
2. 按行放置皇后，每行放置一个
3. 对于每一行，尝试在每一列放置皇后
4. 检查当前位置是否与已放置的皇后冲突
5. 如果不冲突，递归处理下一行
6. 如果冲突，尝试下一列
7. 如果所有列都尝试过都不行，回溯到上一行
8. 与N-Queens I不同的是，这里只需要统计解的数量，不需要记录具体解

剪枝策略：
1. 可行性剪枝：在放置皇后时检查是否与已放置的皇后冲突
2. 约束传播：一旦某一行无法放置皇后，立即回溯

时间复杂度：O(N!)，第一行有N种选择，第二行最多有N-1种选择，以此类推
空间复杂度：O(N)，递归栈深度和三个布尔数组的空间

工程化考量：
1. 使用三个布尔数组优化冲突检查：
   - cols[i]：第i列是否已有皇后
   - diag1[i]：第i条主对角线是否已有皇后
   - diag2[i]：第i条副对角线是否已有皇后
2. 主对角线标识：row - col + n - 1（避免负数索引）
3. 副对角线标识：row + col
4. 边界处理：处理n=1的特殊情况
5. 性能优化：使用位运算可以进一步优化空间
6. 异常处理：验证输入参数的有效性
"""

class LeetCode52_NQueensII:
    def __init__(self):
        self.count = 0
    
    def total_n_queens(self, n: int) -> int:
        """
        计算N皇后问题的不同解决方案数量
        
        Args:
            n: 皇后数量和棋盘大小
            
        Returns:
            不同解决方案的数量
        """
        # 边界条件检查
        if n <= 0:
            return 0
        
        # 优化的冲突检查数组
        cols = [False] * n              # 列冲突检查
        diag1 = [False] * (2 * n - 1)   # 主对角线冲突检查
        diag2 = [False] * (2 * n - 1)   # 副对角线冲突检查
        
        self.count = 0
        self._backtrack(n, 0, cols, diag1, diag2)
        return self.count
    
    def _backtrack(self, n: int, row: int, cols: list, diag1: list, diag2: list) -> None:
        """
        回溯函数计算N皇后问题的解数量
        
        Args:
            n: 棋盘大小
            row: 当前行
            cols: 列冲突检查数组
            diag1: 主对角线冲突检查数组
            diag2: 副对角线冲突检查数组
        """
        # 终止条件：已放置完所有皇后
        if row == n:
            self.count += 1
            return
        
        # 在当前行的每一列尝试放置皇后
        for col in range(n):
            # 计算对角线索引
            d1 = row - col + n - 1  # 主对角线索引（避免负数）
            d2 = row + col          # 副对角线索引
            
            # 可行性剪枝：检查是否与已放置的皇后冲突
            if not cols[col] and not diag1[d1] and not diag2[d2]:
                # 放置皇后
                cols[col] = True
                diag1[d1] = True
                diag2[d2] = True
                
                # 递归处理下一行
                self._backtrack(n, row + 1, cols, diag1, diag2)
                
                # 回溯：撤销放置
                cols[col] = False
                diag1[d1] = False
                diag2[d2] = False
    
    def total_n_queens2(self, n: int) -> int:
        """
        解法二：使用位运算优化的N皇后问题解法
        
        Args:
            n: 皇后数量和棋盘大小
            
        Returns:
            不同解决方案的数量
        """
        if n <= 0:
            return 0
        
        self.count = 0
        # 使用位运算表示列、主对角线、副对角线的占用情况
        self._backtrack2(n, 0, 0, 0, 0)
        return self.count
    
    def _backtrack2(self, n: int, row: int, cols: int, diag1: int, diag2: int) -> None:
        """
        使用位运算的回溯函数
        
        Args:
            n: 棋盘大小
            row: 当前行
            cols: 列占用情况（二进制位表示）
            diag1: 主对角线占用情况
            diag2: 副对角线占用情况
        """
        # 终止条件：已放置完所有皇后
        if row == n:
            self.count += 1
            return
        
        # 计算可以放置皇后的位置
        # (~(cols | diag1 | diag2)) 表示不与任何皇后冲突的位置
        # ((1 << n) - 1) 用于限制在n位范围内
        available_positions = ((1 << n) - 1) & (~(cols | diag1 | diag2))
        
        # 遍历所有可以放置皇后的位置
        while available_positions != 0:
            # 获取最右边的可用位置
            position = available_positions & (-available_positions)
            
            # 在该位置放置皇后
            self._backtrack2(n, row + 1, 
                           cols | position, 
                           (diag1 | position) << 1, 
                           (diag2 | position) >> 1)
            
            # 移除已处理的位置
            available_positions &= available_positions - 1


def main():
    """测试示例"""
    solution = LeetCode52_NQueensII()
    
    # 测试用例1
    n1 = 4
    result1 = solution.total_n_queens(n1)
    print(f"输入: n = {n1}")
    print(f"输出: {result1}")
    
    # 测试用例2
    n2 = 1
    result2 = solution.total_n_queens(n2)
    print(f"\n输入: n = {n2}")
    print(f"输出: {result2}")
    
    # 测试用例3
    n3 = 8
    result3 = solution.total_n_queens(n3)
    print(f"\n输入: n = {n3}")
    print(f"输出: {result3}")
    
    # 测试解法二
    print("\n=== 解法二测试 ===")
    result4 = solution.total_n_queens2(n1)
    print(f"输入: n = {n1}")
    print(f"输出: {result4}")


if __name__ == "__main__":
    main()