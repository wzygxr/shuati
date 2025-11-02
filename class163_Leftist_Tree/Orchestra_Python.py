#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
CodeForces 627E Orchestra（管弦乐队）
题目链接: https://codeforces.com/problemset/problem/627/E

题目描述：给定一个N×M的矩阵，其中每个元素是0或1。我们要找出所有大小为a×b的子矩阵，
使得这些子矩阵中至少包含k个1。请输出满足条件的子矩阵数量。

解题思路：使用滑动窗口和左偏树来维护每列的滑动窗口中的最大值

时间复杂度：O(N*M*a*log b)，在实际应用中表现良好
空间复杂度：O(N*M)
"""

class LeftistTreeNode:
    def __init__(self, value, row, col):
        self.value = value
        self.row = row
        self.col = col
        self.dist = 0
        self.left = None
        self.right = None

def merge(a, b):
    """合并两个左偏树"""
    if a is None:
        return b
    if b is None:
        return a
    
    # 维护大根堆性质
    if a.value < b.value:
        a, b = b, a
    
    # 递归合并右子树
    a.right = merge(a.right, b)
    
    # 维护左偏性质
    if a.left is None or (a.right is not None and a.left.dist < a.right.dist):
        a.left, a.right = a.right, a.left
    
    # 更新距离
    a.dist = 0 if a.right is None else a.right.dist + 1
    return a

def get_max(root):
    """获取堆顶元素（最大值）"""
    if root is None:
        return 0
    return root.value

def remove(root, target_row, target_col):
    """移除特定位置的元素"""
    if root is None:
        return None
    
    if root.row == target_row and root.col == target_col:
        return merge(root.left, root.right)
    
    # 递归删除
    root.left = remove(root.left, target_row, target_col)
    root.right = remove(root.right, target_row, target_col)
    
    # 重新维护左偏性质
    if root.left is None or (root.right is not None and root.left.dist < root.right.dist):
        root.left, root.right = root.right, root.left
    
    root.dist = 0 if root.right is None else root.right.dist + 1
    return root

def count_valid_submatrices(matrix, a, b, k):
    """计算满足条件的子矩阵数量"""
    n = len(matrix)
    if n == 0:
        return 0
    m = len(matrix[0])
    
    # 预处理每个位置向上连续的1的数量
    up_counts = [[0] * m for _ in range(n)]
    for j in range(m):
        up_counts[0][j] = matrix[0][j]
        for i in range(1, n):
            up_counts[i][j] = 0 if matrix[i][j] == 0 else up_counts[i-1][j] + 1
    
    result = 0
    
    # 遍历所有可能的起始行
    for top_row in range(n - a + 1):
        bottom_row = top_row + a - 1
        
        # 对于每一列，计算在[a×b]窗口内的有效高度
        window_counts = [[0] * m for _ in range(n)]
        for j in range(m):
            window_counts[bottom_row][j] = min(up_counts[bottom_row][j], a)
        
        # 使用滑动窗口和左偏树维护每列的滑动窗口最大值
        for left_col in range(m - b + 1):
            right_col = left_col + b - 1
            
            # 为每个行创建一个左偏树来维护该行的b列窗口中的最大值
            row_heaps = [None] * n
            
            # 初始化每个行的左偏树
            for i in range(n):
                for j in range(left_col, right_col + 1):
                    row_heaps[i] = merge(row_heaps[i], LeftistTreeNode(window_counts[i][j], i, j))
            
            # 统计当前窗口内的有效1的数量
            count_ones = 0
            for i in range(top_row, bottom_row + 1):
                count_ones += get_max(row_heaps[i])
            
            if count_ones >= k:
                result += 1
    
    return result

# 主测试函数
def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    a = int(input[ptr])
    ptr += 1
    b = int(input[ptr])
    ptr += 1
    k = int(input[ptr])
    ptr += 1
    
    matrix = []
    for _ in range(n):
        row = list(map(int, input[ptr:ptr+m]))
        ptr += m
        matrix.append(row)
    
    result = count_valid_submatrices(matrix, a, b, k)
    print(result)

if __name__ == "__main__":
    main()