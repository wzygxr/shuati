# 308. 二维区域和检索 - 可变
# 给你一个 2D 矩阵 matrix，请你完成两类查询：
# 1. 更新矩阵中某个单元格的值
# 2. 计算子矩形范围内元素的总和，该子矩阵的左上角为 (row1, col1) ，右下角为 (row2, col2)
# 实现 NumMatrix 类：
# NumMatrix(int[][] matrix) 给定整数矩阵 matrix 进行初始化
# void update(int row, int col, int val) 更新 matrix[row][col] 的值到 val
# int sumRegion(int row1, int col1, int row2, int col2) 返回子矩阵的总和
# 测试链接 : https://leetcode.cn/problems/range-sum-query-2d-mutable/
# 请同学们务必参考如下代码中关于输入、输出的处理
# 这是输入输出处理效率很高的写法

class NumMatrix:
    def __init__(self, matrix):
        """
        :type matrix: List[List[int]]
        构造函数
        时间复杂度: O(m*n)
        空间复杂度: O(m*n)
        """
        if not matrix or not matrix[0]:
            return
        
        self.matrix = matrix
        self.m = len(matrix)
        self.n = len(matrix[0])
        # 初始化线段树
        self.tree = [[0 for _ in range(self.n * 2)] for _ in range(self.m * 2)]
        
        # 构建线段树
        self.buildTree()
    
    def buildTree(self):
        """
        构建二维线段树
        时间复杂度: O(m*n)
        """
        # 先构建每行的一维线段树
        for i in range(self.m):
            for j in range(self.n):
                self.tree[i + self.m][j + self.n] = self.matrix[i][j]
            
            # 构建行的线段树
            for j in range(self.n - 1, 0, -1):
                self.tree[i + self.m][j] = self.tree[i + self.m][j << 1] + self.tree[i + self.m][j << 1 | 1]
        
        # 构建列的线段树
        for i in range(self.m - 1, 0, -1):
            for j in range(2 * self.n):
                self.tree[i][j] = self.tree[i << 1][j] + self.tree[i << 1 | 1][j]
    
    def update(self, row, col, val):
        """
        更新矩阵中某个位置的值
        时间复杂度: O(log m * log n)
        :type row: int
        :type col: int
        :type val: int
        """
        # 计算差值
        delta = val - self.matrix[row][col]
        self.matrix[row][col] = val
        
        # 更新线段树
        i = row + self.m
        while i > 0:
            j = col + self.n
            while j > 0:
                self.tree[i][j] += delta
                j >>= 1
            i >>= 1
    
    def sumRow(self, row, col1, col2):
        """
        计算某一行范围内列的和
        时间复杂度: O(log n)
        :type row: int
        :type col1: int
        :type col2: int
        :rtype: int
        """
        sum_val = 0
        j, k = col1 + self.n, col2 + self.n + 1
        while j < k:
            if j & 1:
                sum_val += self.tree[row][j]
                j += 1
            if k & 1:
                k -= 1
                sum_val += self.tree[row][k]
            j >>= 1
            k >>= 1
        return sum_val
    
    def sumRegion(self, row1, col1, row2, col2):
        """
        查询子矩阵的总和
        时间复杂度: O(log m * log n)
        :type row1: int
        :type col1: int
        :type row2: int
        :type col2: int
        :rtype: int
        """
        sum_val = 0
        
        # 处理行范围
        i = row1 + self.m
        while i <= row2 + self.m:
            r1, r2 = i, i
            
            # 找到完整的区间
            while r1 > 0 and r1 % 2 == 0 and r2 + 1 <= row2 + self.m:
                r1 >>= 1
                r2 = (r2 + 1) >> 1
            
            # 处理列范围
            sum_val += self.sumRow(r1, col1, col2)
            
            # 移动到下一个区间
            if r1 * 2 <= row2 + self.m:
                i = r1 * 2 + 1
            else:
                break
        
        return sum_val

# 测试方法
if __name__ == "__main__":
    # 测试用例:
    # matrix = [
    #   [3, 0, 1, 4, 2],
    #   [5, 6, 3, 2, 1],
    #   [1, 2, 0, 1, 5],
    #   [4, 1, 0, 1, 7],
    #   [1, 0, 3, 0, 5]
    # ]
    # sumRegion(2, 1, 4, 3) => 8
    # update(3, 2, 2)
    # sumRegion(2, 1, 4, 3) => 10
    
    matrix = [
        [3, 0, 1, 4, 2],
        [5, 6, 3, 2, 1],
        [1, 2, 0, 1, 5],
        [4, 1, 0, 1, 7],
        [1, 0, 3, 0, 5]
    ]
    
    numMatrix = NumMatrix(matrix)
    print(numMatrix.sumRegion(2, 1, 4, 3))  # 应该输出 8
    numMatrix.update(3, 2, 2)
    print(numMatrix.sumRegion(2, 1, 4, 3))  # 应该输出 10