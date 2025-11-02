import time

"""
LeetCode 304. 二维区域和检索 - 矩阵不可变 (Range Sum Query 2D - Immutable)

题目描述:
给定一个二维矩阵 matrix，计算其子矩形范围内元素的总和，该子矩阵的左上角为 (row1, col1) ，右下角为 (row2, col2) 。
你可以假设矩阵不可变。
会多次调用 sumRegion 方法。

示例1:
输入:
matrix = [
  [3, 0, 1, 4, 2],
  [5, 6, 3, 2, 1],
  [1, 2, 0, 1, 5],
  [4, 1, 0, 1, 7],
  [1, 0, 3, 0, 5]
]
sumRegion(2, 1, 4, 3) -> 8
sumRegion(1, 1, 2, 2) -> 11
sumRegion(1, 2, 2, 4) -> 12

提示:
1. 你可以假设矩阵的长和宽不超过 200 。
2. sumRegion 函数会被调用多次。

题目链接: https://leetcode.com/problems/range-sum-query-2d-immutable/

解题思路:
这个问题可以使用二维前缀和来解决：
1. 预处理矩阵，计算每个位置 (i, j) 到 (0, 0) 的矩形区域内所有元素的和，存储在 prefix_sum 数组中
2. 利用前缀和数组，可以在 O(1) 时间内计算任意子矩阵的和

二维前缀和的计算公式：
prefix_sum[i][j] = matrix[i-1][j-1] + prefix_sum[i-1][j] + prefix_sum[i][j-1] - prefix_sum[i-1][j-1]

子矩阵和的计算公式：
sumRegion(row1, col1, row2, col2) = prefix_sum[row2+1][col2+1] - prefix_sum[row1][col2+1] - prefix_sum[row2+1][col1] + prefix_sum[row1][col1]

时间复杂度:
- 构造函数: O(m*n)，其中 m 是矩阵的行数，n 是矩阵的列数
- sumRegion 方法: O(1)

空间复杂度: O(m*n)，用于存储前缀和数组

这是最优解，因为我们需要在 O(1) 时间内回答任意子矩阵和查询，预处理是必要的，且预处理的时间复杂度已经是最优的。
"""

class NumMatrix:
    """
    二维区域和检索类，用于高效计算子矩阵元素和
    """
    def __init__(self, matrix):
        """
        初始化 NumMatrix 对象
        
        Args:
            matrix: 输入的二维矩阵
        """
        if not matrix or not matrix[0]:
            self.prefix_sum = []
            self.rows = 0
            self.cols = 0
            return
        
        self.rows = len(matrix)
        self.cols = len(matrix[0])
        
        # 创建前缀和数组，比原矩阵多一行一列，便于处理边界情况
        self.prefix_sum = [[0] * (self.cols + 1) for _ in range(self.rows + 1)]
        
        # 计算前缀和数组
        for i in range(1, self.rows + 1):
            for j in range(1, self.cols + 1):
                # 当前位置的值 = 原矩阵对应位置的值 + 上方区域的和 + 左方区域的和 - 左上重叠区域的和
                self.prefix_sum[i][j] = matrix[i - 1][j - 1] + self.prefix_sum[i - 1][j] + \
                                       self.prefix_sum[i][j - 1] - self.prefix_sum[i - 1][j - 1]
    
    def sumRegion(self, row1, col1, row2, col2):
        """
        计算从 (row1, col1) 到 (row2, col2) 的矩形区域内所有元素的和
        
        Args:
            row1: 左上角行索引
            col1: 左上角列索引
            row2: 右下角行索引
            col2: 右下角列索引
        
        Returns:
            子矩阵内所有元素的和
        """
        # 边界检查
        if not self.prefix_sum:
            return 0
        
        # 确保索引有效
        row1 = max(0, row1)
        col1 = max(0, col1)
        row2 = min(self.rows - 1, row2)
        col2 = min(self.cols - 1, col2)
        
        if row1 > row2 or col1 > col2:
            return 0
        
        # 使用前缀和公式计算子矩阵和
        # 子矩阵和 = 右下角前缀和 - 左上角上方前缀和 - 左上角左方前缀和 + 左上角前缀和
        return (self.prefix_sum[row2 + 1][col2 + 1] - self.prefix_sum[row1][col2 + 1] - 
                self.prefix_sum[row2 + 1][col1] + self.prefix_sum[row1][col1])
    
    def getPrefixSum(self):
        """
        获取前缀和数组，用于调试
        
        Returns:
            前缀和数组
        """
        return self.prefix_sum

"""
打印矩阵，用于调试

Args:
    matrix: 要打印的矩阵
"""
def print_matrix(matrix):
    for row in matrix:
        print(f"[{', '.join(map(str, row))}]")

# 测试代码
def main():
    # 测试用例1
    matrix1 = [
        [3, 0, 1, 4, 2],
        [5, 6, 3, 2, 1],
        [1, 2, 0, 1, 5],
        [4, 1, 0, 1, 7],
        [1, 0, 3, 0, 5]
    ]
    
    numMatrix1 = NumMatrix(matrix1)
    print("测试用例1 - sumRegion(2, 1, 4, 3):", numMatrix1.sumRegion(2, 1, 4, 3))  # 预期输出: 8
    print("测试用例1 - sumRegion(1, 1, 2, 2):", numMatrix1.sumRegion(1, 1, 2, 2))  # 预期输出: 11
    print("测试用例1 - sumRegion(1, 2, 2, 4):", numMatrix1.sumRegion(1, 2, 2, 4))  # 预期输出: 12
    
    # 测试用例2 - 空矩阵
    matrix2 = []
    numMatrix2 = NumMatrix(matrix2)
    print("测试用例2 - 空矩阵:", numMatrix2.sumRegion(0, 0, 0, 0))  # 预期输出: 0
    
    # 测试用例3 - 只有一个元素的矩阵
    matrix3 = [[5]]
    numMatrix3 = NumMatrix(matrix3)
    print("测试用例3 - 单元素矩阵:", numMatrix3.sumRegion(0, 0, 0, 0))  # 预期输出: 5
    
    # 测试用例4 - 边界情况
    print("测试用例4 - 越界索引:", numMatrix1.sumRegion(-1, -1, 10, 10))  # 预期输出: 应该正确处理越界
    
    # 测试用例5 - 多次调用
    start_time = time.time()
    total = 0
    for i in range(1000):
        total += numMatrix1.sumRegion(0, 0, 4, 4)
    end_time = time.time()
    
    print("测试用例5 - 多次调用结果:", total)
    print("测试用例5 - 多次调用耗时:", (end_time - start_time) * 1000, "ms")

if __name__ == "__main__":
    main()