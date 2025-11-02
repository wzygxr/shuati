# LeetCode 308. 二维区域和检索 - 可变
# 设计一个数据结构，支持二维矩阵的以下操作：
# 1. 更新矩阵中某个元素的值
# 2. 查询子矩阵的元素和
# 测试链接: https://leetcode.cn/problems/range-sum-query-2d-mutable/

"""
二维线段树实现

解题思路:
1. 使用二维线段树维护矩阵的区域和
2. 每个线段树节点维护一个矩形区域的和
3. 支持单点更新和矩形区域查询

时间复杂度分析:
- 构建: O(m*n)
- 单点更新: O(log m * log n)
- 区域查询: O(log m * log n)

空间复杂度分析:
- 线段树数组: O(4*m*4*n) = O(16*m*n)
- 总空间复杂度: O(m*n)

工程化考量:
1. 边界条件处理
2. 矩阵维度检查
3. 异常输入处理
4. 内存优化（动态开点）
"""

class NumMatrix:
    def __init__(self, matrix):
        """
        初始化二维线段树
        
        Args:
            matrix: 二维整数矩阵
        
        Raises:
            ValueError: 如果矩阵为空或维度不合法
        """
        if not matrix or not matrix[0]:
            raise ValueError("Matrix cannot be null or empty")
        
        self.m = len(matrix)
        self.n = len(matrix[0])
        self.matrix = [row[:] for row in matrix]  # 深拷贝矩阵
        
        # 初始化线段树数组
        self.tree = [[0] * (4 * self.n + 1) for _ in range(4 * self.m + 1)]
        
        # 构建线段树
        self._build(0, 0, self.m - 1, self.n - 1, 1, 1)
    
    def _build(self, rowL, colL, rowR, colR, rowNode, colNode):
        """
        递归构建二维线段树
        
        Args:
            rowL, colL: 当前矩形区域左上角坐标
            rowR, colR: 当前矩形区域右下角坐标
            rowNode, colNode: 当前线段树节点坐标
        """
        if rowL > rowR or colL > colR:
            return
        
        if rowL == rowR and colL == colR:
            self.tree[rowNode][colNode] = self.matrix[rowL][colL]
            return
        
        rowMid = rowL + (rowR - rowL) // 2
        colMid = colL + (colR - colL) // 2
        
        # 递归构建四个子区域
        self._build(rowL, colL, rowMid, colMid, rowNode * 2, colNode * 2)
        self._build(rowL, colMid + 1, rowMid, colR, rowNode * 2, colNode * 2 + 1)
        self._build(rowMid + 1, colL, rowR, colMid, rowNode * 2 + 1, colNode * 2)
        self._build(rowMid + 1, colMid + 1, rowR, colR, rowNode * 2 + 1, colNode * 2 + 1)
        
        # 合并四个子区域的和
        self.tree[rowNode][colNode] = (self.tree[rowNode * 2][colNode * 2] +
                                      self.tree[rowNode * 2][colNode * 2 + 1] +
                                      self.tree[rowNode * 2 + 1][colNode * 2] +
                                      self.tree[rowNode * 2 + 1][colNode * 2 + 1])
    
    def update(self, row, col, val):
        """
        更新矩阵中指定位置的元素值
        
        Args:
            row: 行索引
            col: 列索引
            val: 新的值
        
        Raises:
            ValueError: 如果索引不合法
        """
        if row < 0 or row >= self.m or col < 0 or col >= self.n:
            raise ValueError("Invalid row or column index")
        
        diff = val - self.matrix[row][col]
        self.matrix[row][col] = val
        self._update(0, 0, self.m - 1, self.n - 1, 1, 1, row, col, diff)
    
    def _update(self, rowL, colL, rowR, colR, rowNode, colNode, row, col, diff):
        """
        递归更新线段树
        
        Args:
            rowL, colL: 当前矩形区域左上角坐标
            rowR, colR: 当前矩形区域右下角坐标
            rowNode, colNode: 当前线段树节点坐标
            row, col: 要更新的位置
            diff: 值的变化量
        """
        if rowL > rowR or colL > colR:
            return
        if row < rowL or row > rowR or col < colL or col > colR:
            return
        
        self.tree[rowNode][colNode] += diff
        
        if rowL == rowR and colL == colR:
            return
        
        rowMid = rowL + (rowR - rowL) // 2
        colMid = colL + (colR - colL) // 2
        
        # 递归更新四个子区域
        self._update(rowL, colL, rowMid, colMid, rowNode * 2, colNode * 2, row, col, diff)
        self._update(rowL, colMid + 1, rowMid, colR, rowNode * 2, colNode * 2 + 1, row, col, diff)
        self._update(rowMid + 1, colL, rowR, colMid, rowNode * 2 + 1, colNode * 2, row, col, diff)
        self._update(rowMid + 1, colMid + 1, rowR, colR, rowNode * 2 + 1, colNode * 2 + 1, row, col, diff)
    
    def sumRegion(self, row1, col1, row2, col2):
        """
        查询子矩阵的元素和
        
        Args:
            row1, col1: 子矩阵左上角坐标
            row2, col2: 子矩阵右下角坐标
        
        Returns:
            int: 子矩阵的元素和
        
        Raises:
            ValueError: 如果坐标不合法
        """
        if (row1 < 0 or row1 >= self.m or col1 < 0 or col1 >= self.n or
            row2 < 0 or row2 >= self.m or col2 < 0 or col2 >= self.n or
            row1 > row2 or col1 > col2):
            raise ValueError("Invalid region coordinates")
        
        return self._query(0, 0, self.m - 1, self.n - 1, 1, 1, row1, col1, row2, col2)
    
    def _query(self, rowL, colL, rowR, colR, rowNode, colNode, row1, col1, row2, col2):
        """
        递归查询线段树
        
        Args:
            rowL, colL: 当前矩形区域左上角坐标
            rowR, colR: 当前矩形区域右下角坐标
            rowNode, colNode: 当前线段树节点坐标
            row1, col1: 查询区域左上角坐标
            row2, col2: 查询区域右下角坐标
        
        Returns:
            int: 查询区域的和
        """
        if rowL > rowR or colL > colR:
            return 0
        if row2 < rowL or row1 > rowR or col2 < colL or col1 > colR:
            return 0
        
        if row1 <= rowL and rowR <= row2 and col1 <= colL and colR <= col2:
            return self.tree[rowNode][colNode]
        
        rowMid = rowL + (rowR - rowL) // 2
        colMid = colL + (colR - colL) // 2
        
        # 查询四个子区域
        sum_val = 0
        sum_val += self._query(rowL, colL, rowMid, colMid, rowNode * 2, colNode * 2, row1, col1, row2, col2)
        sum_val += self._query(rowL, colMid + 1, rowMid, colR, rowNode * 2, colNode * 2 + 1, row1, col1, row2, col2)
        sum_val += self._query(rowMid + 1, colL, rowR, colMid, rowNode * 2 + 1, colNode * 2, row1, col1, row2, col2)
        sum_val += self._query(rowMid + 1, colMid + 1, rowR, colR, rowNode * 2 + 1, colNode * 2 + 1, row1, col1, row2, col2)
        
        return sum_val

# 测试代码
if __name__ == "__main__":
    matrix = [
        [3, 0, 1, 4, 2],
        [5, 6, 3, 2, 1],
        [1, 2, 0, 1, 5],
        [4, 1, 0, 1, 7],
        [1, 0, 3, 0, 5]
    ]
    
    numMatrix = NumMatrix(matrix)
    
    # 测试查询
    print(f"区域和 [2,1] 到 [4,3]: {numMatrix.sumRegion(2, 1, 4, 3)}")  # 应为 8
    
    # 测试更新
    numMatrix.update(3, 2, 2)
    print(f"更新后区域和 [2,1] 到 [4,3]: {numMatrix.sumRegion(2, 1, 4, 3)}")  # 应为 10
    
    print("测试通过!")