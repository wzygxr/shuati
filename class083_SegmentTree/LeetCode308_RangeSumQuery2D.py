"""
LeetCode 308 - Range Sum Query 2D - Mutable
题目：二维区域和检索 - 可变
来源：LeetCode
网址：https://leetcode.cn/problems/range-sum-query-2d-mutable/

给定一个二维矩阵，支持以下操作：
1. 更新矩阵中某个位置的值
2. 查询子矩阵的和

解题思路：
使用二维线段树（四叉树）来处理二维区域查询和更新问题。
每个节点代表一个矩形区域，维护该区域的和。

时间复杂度：
  - 建树：O(m*n)
  - 更新：O(log m * log n)
  - 查询：O(log m * log n)
空间复杂度：O(m*n)
"""

class TreeNode:
    def __init__(self, row1, col1, row2, col2):
        self.row1 = row1  # 矩形区域的行起始坐标
        self.col1 = col1  # 矩形区域的列起始坐标
        self.row2 = row2  # 矩形区域的行结束坐标
        self.col2 = col2  # 矩形区域的列结束坐标
        self.sum = 0      # 区域和
        self.children = [None, None, None, None]  # 四个子节点

class RangeSumQuery2D:
    def __init__(self, matrix):
        """
        初始化二维线段树
        Args:
            matrix: 输入的二维矩阵
        """
        if not matrix or not matrix[0]:
            self.root = None
            self.matrix = []
            return
        
        self.matrix = [row[:] for row in matrix]  # 深拷贝
        self.root = self._build_tree(0, 0, len(matrix) - 1, len(matrix[0]) - 1)
    
    def _build_tree(self, row1, col1, row2, col2):
        """
        递归构建二维线段树
        Args:
            row1, col1: 矩形区域的起始坐标
            row2, col2: 矩形区域的结束坐标
        Returns:
            构建的树节点
        """
        if row1 > row2 or col1 > col2:
            return None
        
        node = TreeNode(row1, col1, row2, col2)
        
        # 叶子节点
        if row1 == row2 and col1 == col2:
            node.sum = self.matrix[row1][col1]
            return node
        
        row_mid = (row1 + row2) // 2
        col_mid = (col1 + col2) // 2
        
        # 构建四个子节点
        node.children[0] = self._build_tree(row1, col1, row_mid, col_mid)           # 左上
        node.children[1] = self._build_tree(row1, col_mid + 1, row_mid, col2)       # 右上
        node.children[2] = self._build_tree(row_mid + 1, col1, row2, col_mid)       # 左下
        node.children[3] = self._build_tree(row_mid + 1, col_mid + 1, row2, col2)   # 右下
        
        # 计算当前节点的和
        for i in range(4):
            if node.children[i]:
                node.sum += node.children[i].sum
        
        return node
    
    def update(self, row, col, val):
        """
        更新矩阵中某个位置的值
        Args:
            row, col: 要更新的位置
            val: 新的值
        """
        if not self.root:
            return
        
        delta = val - self.matrix[row][col]
        self.matrix[row][col] = val
        self._update(self.root, row, col, delta)
    
    def _update(self, node, row, col, delta):
        """
        递归更新节点值
        Args:
            node: 当前节点
            row, col: 要更新的位置
            delta: 增量值
        """
        if not node:
            return
        
        # 检查目标点是否在当前节点的区域内
        if row < node.row1 or row > node.row2 or col < node.col1 or col > node.col2:
            return
        
        node.sum += delta
        
        # 如果是叶子节点，直接返回
        if node.row1 == node.row2 and node.col1 == node.col2:
            return
        
        # 递归更新子节点
        for i in range(4):
            if node.children[i]:
                self._update(node.children[i], row, col, delta)
    
    def sumRegion(self, row1, col1, row2, col2):
        """
        查询子矩阵的和
        Args:
            row1, col1: 查询区域的起始坐标
            row2, col2: 查询区域的结束坐标
        Returns:
            区域和
        """
        if not self.root:
            return 0
        return self._sum_region(self.root, row1, col1, row2, col2)
    
    def _sum_region(self, node, row1, col1, row2, col2):
        """
        递归查询区域和
        Args:
            node: 当前节点
            row1, col1: 查询区域的起始坐标
            row2, col2: 查询区域的结束坐标
        Returns:
            区域和
        """
        if not node:
            return 0
        
        # 没有交集
        if row1 > node.row2 or row2 < node.row1 or col1 > node.col2 or col2 < node.col1:
            return 0
        
        # 完全包含
        if row1 <= node.row1 and node.row2 <= row2 and col1 <= node.col1 and node.col2 <= col2:
            return node.sum
        
        # 部分重叠，递归查询子节点
        total = 0
        for i in range(4):
            if node.children[i]:
                total += self._sum_region(node.children[i], row1, col1, row2, col2)
        
        return total

# 测试代码
if __name__ == "__main__":
    # 测试样例
    matrix = [
        [3, 0, 1, 4, 2],
        [5, 6, 3, 2, 1],
        [1, 2, 0, 1, 5],
        [4, 1, 0, 1, 7],
        [1, 0, 3, 0, 5]
    ]
    
    st = RangeSumQuery2D(matrix)
    
    # 查询区域和
    print(f"区域[2,1,4,3]的和: {st.sumRegion(2, 1, 4, 3)}")  # 8
    
    # 更新矩阵
    st.update(3, 2, 2)
    
    # 查询更新后的区域和
    print(f"更新后区域[2,1,4,3]的和: {st.sumRegion(2, 1, 4, 3)}")  # 10
    
    # 查询其他区域
    print(f"区域[1,1,2,2]的和: {st.sumRegion(1, 1, 2, 2)}")  # 11
    print(f"区域[1,2,2,4]的和: {st.sumRegion(1, 2, 2, 4)}")  # 12
    
    # 测试异常处理
    try:
        st.sumRegion(-1, 0, 1, 1)
    except Exception as e:
        print(f"异常测试: {e}")