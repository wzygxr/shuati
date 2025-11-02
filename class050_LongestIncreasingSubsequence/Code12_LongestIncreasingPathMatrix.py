#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
矩阵中的最长递增路径 - LeetCode 329
题目来源：https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
难度：困难
题目描述：给定一个 m x n 的整数矩阵 matrix ，找出其中最长递增路径的长度。
对于每个单元格，你可以往上，下，左，右四个方向移动。 你不能在对角线方向上移动或移动到边界外（即不允许环绕）。

核心思路：
1. 这道题是LIS问题的二维变体，我们需要在矩阵中寻找最长的递增路径
2. 使用深度优先搜索(DFS) + 记忆化搜索(Memoization)来避免重复计算
3. 对于每个单元格，我们从四个方向进行探索，只考虑值严格大于当前单元格的相邻单元格
4. 用一个dp数组存储每个单元格的最长递增路径长度，避免重复计算

复杂度分析：
时间复杂度：O(m*n)，其中m和n分别是矩阵的行数和列数。每个单元格只会被访问一次
空间复杂度：O(m*n)，用于存储dp数组
"""

from typing import List
import time
import random


def longestIncreasingPath(matrix: List[List[int]]) -> int:
    """
    最优解法：深度优先搜索 + 记忆化搜索
    
    参数:
        matrix: 输入矩阵
    返回:
        最长递增路径的长度
    """
    # 边界情况处理
    if not matrix or not matrix[0]:
        return 0
    
    rows = len(matrix)
    cols = len(matrix[0])
    # dp[i][j]表示以(i,j)为起点的最长递增路径长度
    dp = [[0 for _ in range(cols)] for _ in range(rows)]
    max_length = 0
    
    # 定义四个方向的移动：上、右、下、左
    directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    def dfs(i: int, j: int) -> int:
        """
        深度优先搜索函数，计算从(i,j)出发的最长递增路径长度
        
        参数:
            i: 当前行索引
            j: 当前列索引
        返回:
            从(i,j)出发的最长递增路径长度
        """
        # 如果已经计算过以(i,j)为起点的最长路径长度，直接返回
        if dp[i][j] != 0:
            return dp[i][j]
        
        max_length = 1  # 路径至少包含当前单元格，长度为1
        
        # 探索四个方向
        for di, dj in directions:
            ni, nj = i + di, j + dj
            
            # 检查新位置是否有效，且值严格大于当前位置
            if (0 <= ni < rows and 0 <= nj < cols and matrix[ni][nj] > matrix[i][j]):
                # 递归计算从新位置出发的最长路径长度，并更新当前位置的最长路径长度
                length = 1 + dfs(ni, nj)
                max_length = max(max_length, length)
        
        # 记忆化结果
        dp[i][j] = max_length
        return max_length
    
    # 遍历每个单元格，寻找最长路径
    for i in range(rows):
        for j in range(cols):
            max_length = max(max_length, dfs(i, j))
    
    return max_length


def longestIncreasingPathAlternative(matrix: List[List[int]]) -> int:
    """
    另一种实现方式：使用lru_cache进行记忆化（仅在Python中可用）
    注意：为了使用lru_cache，我们需要将矩阵转换为元组或者在函数外部访问
    
    参数:
        matrix: 输入矩阵
    返回:
        最长递增路径的长度
    """
    if not matrix or not matrix[0]:
        return 0
    
    rows, cols = len(matrix), len(matrix[0])
    # 转换为全局变量以便在内部函数中使用
    global_matrix = matrix
    
    # 定义四个方向
    directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    # 使用记忆化搜索
    memo = {}  # (i,j) -> 最长路径长度
    
    def dfs(i: int, j: int) -> int:
        """
        带记忆化的深度优先搜索函数
        """
        if (i, j) in memo:
            return memo[(i, j)]
        
        max_len = 1
        
        for di, dj in directions:
            ni, nj = i + di, j + dj
            if (0 <= ni < rows and 0 <= nj < cols and 
                global_matrix[ni][nj] > global_matrix[i][j]):
                max_len = max(max_len, 1 + dfs(ni, nj))
        
        memo[(i, j)] = max_len
        return max_len
    
    result = 0
    for i in range(rows):
        for j in range(cols):
            result = max(result, dfs(i, j))
    
    return result


def longestIncreasingPathExplained(matrix: List[List[int]]) -> int:
    """
    解释性更强的版本，添加了更详细的注释和中间变量
    
    参数:
        matrix: 输入矩阵
    返回:
        最长递增路径的长度
    """
    # 边界条件检查
    if not matrix or not matrix[0]:
        return 0
    
    # 获取矩阵的维度
    rows, cols = len(matrix), len(matrix[0])
    
    # 创建记忆化数组
    # memo[i][j]表示从位置(i,j)出发能够得到的最长递增路径长度
    memo = [[0 for _ in range(cols)] for _ in range(rows)]
    
    # 定义四个可能的移动方向：上、右、下、左
    directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    def depth_first_search(row: int, col: int) -> int:
        """
        对特定位置进行深度优先搜索，找出从该位置出发的最长递增路径
        
        参数:
            row: 当前行索引
            col: 当前列索引
        返回:
            从该位置出发的最长递增路径长度
        """
        # 如果该位置已经计算过，直接返回存储的值
        if memo[row][col] > 0:
            return memo[row][col]
        
        # 初始路径长度为1（只有当前单元格自身）
        max_path_length = 1
        
        # 尝试从当前位置向四个方向移动
        for direction in directions:
            # 计算新位置的坐标
            new_row = row + direction[0]
            new_col = col + direction[1]
            
            # 检查新位置是否有效：
            # 1. 不超出矩阵边界
            # 2. 新位置的值必须严格大于当前位置的值（保持递增）
            is_valid_move = (0 <= new_row < rows and 
                           0 <= new_col < cols and 
                           matrix[new_row][new_col] > matrix[row][col])
            
            if is_valid_move:
                # 递归计算从新位置出发的最长路径长度
                # 并将结果加1（包含当前位置）
                path_length = 1 + depth_first_search(new_row, new_col)
                # 更新最长路径长度
                max_path_length = max(max_path_length, path_length)
        
        # 将结果存储在记忆化数组中，避免重复计算
        memo[row][col] = max_path_length
        return max_path_length
    
    # 对矩阵中的每个单元格进行搜索，找出全局最长递增路径
    longest_path = 0
    for i in range(rows):
        for j in range(cols):
            current_longest = depth_first_search(i, j)
            longest_path = max(longest_path, current_longest)
    
    return longest_path


def printMatrix(matrix: List[List[int]]):
    """
    打印矩阵
    
    参数:
        matrix: 要打印的矩阵
    """
    for row in matrix:
        print(row)


def runAllSolutionsTest(matrix: List[List[int]]):
    """
    运行所有解法的对比测试
    
    参数:
        matrix: 输入矩阵
    """
    print(f"\n对比测试：")
    printMatrix(matrix)
    
    # 测试DFS + 记忆化解法
    start_time = time.time()
    result1 = longestIncreasingPath(matrix)
    end_time = time.time()
    print(f"DFS + 记忆化解法结果: {result1}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    # 测试另一种实现方式
    start_time = time.time()
    result2 = longestIncreasingPathAlternative(matrix)
    end_time = time.time()
    print(f"另一种实现方式结果: {result2}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    # 测试解释性版本
    start_time = time.time()
    result3 = longestIncreasingPathExplained(matrix)
    end_time = time.time()
    print(f"解释性版本结果: {result3}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    print("-" * 40)


def performanceTest(size: int):
    """
    性能测试函数
    
    参数:
        size: 矩阵大小 (size x size)
    """
    # 生成随机测试数据
    matrix = [[random.randint(0, 1000) for _ in range(size)] for _ in range(size)]
    
    print(f"\n性能测试：矩阵大小 = {size}x{size}")
    
    # 测试DFS + 记忆化解法
    start_time = time.time()
    result1 = longestIncreasingPath(matrix)
    end_time = time.time()
    print(f"DFS + 记忆化解法耗时: {(end_time - start_time) * 1000:.3f} ms, 结果: {result1}")


def testCase():
    """
    测试用例
    """
    # 测试用例1
    matrix1 = [
        [9, 9, 4],
        [6, 6, 8],
        [2, 1, 1]
    ]
    print("测试用例1：")
    printMatrix(matrix1)
    print(f"结果: {longestIncreasingPath(matrix1)}，预期: 4")
    print()
    
    # 测试用例2
    matrix2 = [
        [3, 4, 5],
        [3, 2, 6],
        [2, 2, 1]
    ]
    print("测试用例2：")
    printMatrix(matrix2)
    print(f"结果: {longestIncreasingPath(matrix2)}，预期: 4")
    print()
    
    # 测试用例3：边界情况
    matrix3 = [[1]]
    print("测试用例3：")
    printMatrix(matrix3)
    print(f"结果: {longestIncreasingPath(matrix3)}，预期: 1")
    
    # 运行所有解法的对比测试
    runAllSolutionsTest(matrix1)
    runAllSolutionsTest(matrix2)
    runAllSolutionsTest(matrix3)
    
    # 性能测试
    print("性能测试:")
    print("-" * 40)
    performanceTest(50)
    performanceTest(100)
    
    # 特殊测试用例：完全相同的元素
    print("\n特殊测试用例：完全相同的元素")
    matrixSame = [
        [5, 5, 5],
        [5, 5, 5],
        [5, 5, 5]
    ]
    printMatrix(matrixSame)
    print(f"结果: {longestIncreasingPath(matrixSame)}")
    
    # 特殊测试用例：严格递增的矩阵
    print("\n特殊测试用例：严格递增的矩阵")
    matrixIncreasing = [
        [1, 2, 3],
        [4, 5, 6],
        [7, 8, 9]
    ]
    printMatrix(matrixIncreasing)
    print(f"结果: {longestIncreasingPath(matrixIncreasing)}")


if __name__ == "__main__":
    """
    主函数入口
    """
    testCase()