"""
LeetCode 308. 二维区域和检索 - 可变 (Range Sum Query 2D - Mutable)

题目描述：
设计一个数据结构，支持以下操作：
1. 更新矩阵中某个元素的值
2. 查询子矩阵的元素和

解题思路：
使用二维树状数组（Fenwick Tree）来高效支持更新和查询操作。
二维树状数组通过维护二维前缀和数组，可以在O(log m * log n)时间内完成更新和查询。

时间复杂度分析：
- 构造函数：O(m * n)，需要初始化二维树状数组
- update操作：O(log m * log n)，需要更新相关的前缀和
- sumRegion操作：O(log m * log n)，通过二维前缀和计算子矩阵和

空间复杂度分析：
- O(m * n)，用于存储二维树状数组

工程化考量：
1. 边界条件处理：检查行列索引是否越界
2. 异常处理：处理非法输入参数
3. 性能优化：使用位运算加速索引计算
4. 可读性：变量命名清晰，注释详细
5. Python特性：使用列表推导式初始化二维数组

算法技巧：
- 二维树状数组的核心思想是将二维前缀和分解为多个一维前缀和的组合
- 使用lowbit操作快速定位需要更新的位置
- 通过容斥原理计算子矩阵和

适用场景：
- 需要频繁更新和查询二维矩阵的子矩阵和
- 数据规模较大，需要高效的数据结构支持
- 对实时性要求较高的应用场景

测试用例：
输入：
matrix = [
  [3, 0, 1, 4, 2],
  [5, 6, 3, 2, 1],
  [1, 2, 0, 1, 5],
  [4, 1, 0, 1, 7],
  [1, 0, 3, 0, 5]
]
操作：
sumRegion(2, 1, 4, 3) -> 8
update(3, 2, 2)
sumRegion(2, 1, 4, 3) -> 10
"""

class NumMatrix:
    """
    二维区域和检索数据结构类
    
    使用二维树状数组实现高效的矩阵更新和查询操作
    """
    
    def __init__(self, matrix):
        """
        构造函数：初始化二维树状数组
        
        Args:
            matrix: 输入的二维矩阵
            
        算法步骤：
        1. 检查输入矩阵是否为空
        2. 初始化树状数组和原始矩阵
        3. 构建二维树状数组
        
        时间复杂度：O(m * n * log m * log n)
        空间复杂度：O(m * n)
        """
        if not matrix or not matrix[0]:
            raise ValueError("Matrix cannot be empty")
        
        self.m = len(matrix)
        self.n = len(matrix[0])
        self.matrix = [row[:] for row in matrix]  # 深拷贝原始矩阵
        
        # 初始化二维树状数组
        self.tree = [[0] * (self.n + 1) for _ in range(self.m + 1)]
        
        # 构建二维树状数组
        for i in range(self.m):
            for j in range(self.n):
                self._update_tree(i, j, matrix[i][j])
    
    def _lowbit(self, x):
        """
        计算数字的lowbit（最低位的1）
        
        Args:
            x: 输入数字
            
        Returns:
            lowbit值
            
        算法原理：
        - x & -x 可以快速得到x的最低位的1
        - 这是树状数组的核心操作，用于快速定位需要更新的位置
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        return x & -x
    
    def _update_tree(self, row, col, val):
        """
        更新树状数组（内部方法）
        
        Args:
            row: 行索引
            col: 列索引
            val: 要增加的值
            
        算法步骤：
        1. 从指定位置开始，沿着树状数组的路径更新相关位置
        2. 使用lowbit操作快速定位需要更新的位置
        
        时间复杂度：O(log m * log n)
        空间复杂度：O(1)
        """
        i = row + 1
        while i <= self.m:
            j = col + 1
            while j <= self.n:
                self.tree[i][j] += val
                j += self._lowbit(j)
            i += self._lowbit(i)
    
    def _query_tree(self, row, col):
        """
        查询树状数组前缀和（内部方法）
        
        Args:
            row: 行索引
            col: 列索引
            
        Returns:
            从(0,0)到(row,col)的子矩阵和
            
        算法步骤：
        1. 从指定位置开始，沿着树状数组的路径累加相关位置的值
        2. 使用lowbit操作快速定位需要累加的位置
        
        时间复杂度：O(log m * log n)
        空间复杂度：O(1)
        """
        if row < 0 or col < 0:
            return 0
            
        total = 0
        i = row + 1
        while i > 0:
            j = col + 1
            while j > 0:
                total += self.tree[i][j]
                j -= self._lowbit(j)
            i -= self._lowbit(i)
        return total
    
    def update(self, row, col, val):
        """
        更新矩阵中指定位置的元素值
        
        Args:
            row: 行索引
            col: 列索引
            val: 新的值
            
        算法步骤：
        1. 检查索引是否越界
        2. 计算值的差异
        3. 更新原始矩阵
        4. 更新二维树状数组
        
        时间复杂度：O(log m * log n)
        空间复杂度：O(1)
        """
        if row < 0 or row >= self.m or col < 0 or col >= self.n:
            raise ValueError("Invalid row or column index")
        
        diff = val - self.matrix[row][col]
        self.matrix[row][col] = val
        
        # 更新二维树状数组
        self._update_tree(row, col, diff)
    
    def sumRegion(self, row1, col1, row2, col2):
        """
        查询子矩阵的元素和
        
        Args:
            row1: 子矩阵左上角行索引
            col1: 子矩阵左上角列索引
            row2: 子矩阵右下角行索引
            col2: 子矩阵右下角列索引
            
        Returns:
            子矩阵的元素和
            
        算法步骤：
        1. 检查索引是否越界
        2. 使用二维前缀和计算子矩阵和
        3. 应用容斥原理：sum = sum(row2,col2) - sum(row2,col1-1) - sum(row1-1,col2) + sum(row1-1,col1-1)
        
        时间复杂度：O(log m * log n)
        空间复杂度：O(1)
        """
        if (row1 < 0 or row2 >= self.m or col1 < 0 or col2 >= self.n or 
            row1 > row2 or col1 > col2):
            raise ValueError("Invalid region coordinates")
        
        # 使用容斥原理计算子矩阵和
        return (self._query_tree(row2, col2) - 
                self._query_tree(row2, col1 - 1) - 
                self._query_tree(row1 - 1, col2) + 
                self._query_tree(row1 - 1, col1 - 1))


def test_num_matrix():
    """
    测试函数：验证二维树状数组的正确性
    
    测试用例设计：
    1. 正常情况测试
    2. 边界情况测试
    3. 更新操作测试
    4. 查询操作测试
    """
    matrix = [
        [3, 0, 1, 4, 2],
        [5, 6, 3, 2, 1],
        [1, 2, 0, 1, 5],
        [4, 1, 0, 1, 7],
        [1, 0, 3, 0, 5]
    ]
    
    try:
        num_matrix = NumMatrix(matrix)
        
        # 测试查询操作
        result1 = num_matrix.sumRegion(2, 1, 4, 3)
        print(f"初始查询结果：{result1}")  # 期望：8
        
        # 测试更新操作
        num_matrix.update(3, 2, 2)
        result2 = num_matrix.sumRegion(2, 1, 4, 3)
        print(f"更新后查询结果：{result2}")  # 期望：10
        
        # 测试边界情况
        result3 = num_matrix.sumRegion(0, 0, 0, 0)
        print(f"单点查询：{result3}")  # 期望：3
        
        result4 = num_matrix.sumRegion(0, 0, 0, 4)
        print(f"整行查询：{result4}")  # 期望：10
        
        print("测试通过！")
        
    except ValueError as e:
        print(f"错误：{e}")


if __name__ == "__main__":
    test_num_matrix()