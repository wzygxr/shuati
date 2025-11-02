# class131/Code19_2DSegmentTree.py
# LeetCode 304. Range Sum Query 2D - Immutable 的二维线段树实现
# 题目链接: https://leetcode.com/problems/range-sum-query-2d-immutable/
"""
题目描述:
实现一个支持以下操作的二维数据结构：
1. 初始化一个二维矩阵
2. 查询任意子矩阵的元素和
3. 更新矩阵中某个位置的值

解题思路:
使用二维线段树解决二维区域查询问题。二维线段树是线段树在二维空间的扩展，通过对行和列分别建立线段树来实现高效的二维区域操作。

算法步骤:
1. 构建二维线段树：递归地将矩阵划分为四个子区域，直到叶子节点
2. 实现区域和查询：根据查询区域与当前节点区域的关系决定如何递归查询
3. 实现单点更新：从根节点开始，沿着包含目标点的路径向下更新节点值

时间复杂度分析:
- 构建树: O(m*n) - 需要遍历所有元素
- 单点更新: O(logm * logn) - 在行和列两个维度上分别进行log级操作
- 区域查询: O(logm * logn) - 在行和列两个维度上分别进行log级操作
空间复杂度: O(m*n) - 需要存储所有节点信息

关键优化:
1. 使用递归实现，结构清晰
2. 合理划分区域，避免重复计算
3. 及时处理边界条件，提高效率
"""

class Code19_2DSegmentTree:
    """
    二维线段树实现，支持二维区域和查询以及单点更新
    
    虽然LeetCode 304题只要求静态查询，但二维线段树可以支持动态更新，更加通用
    时间复杂度：
    - 构建树: O(m*n)
    - 单点更新: O(logm * logn)
    - 区域查询: O(logm * logn)
    空间复杂度: O(m*n)
    """
    
    class Node:
        """
        二维线段树节点定义
        """
        def __init__(self, row1, row2, col1, col2):
            """
            初始化节点
            :param row1: 行范围起始
            :param row2: 行范围结束
            :param col1: 列范围起始
            :param col2: 列范围结束
            """
            self.sum = 0       # 当前区域的和
            self.row1 = row1   # 行范围起始
            self.row2 = row2   # 行范围结束
            self.col1 = col1   # 列范围起始
            self.col2 = col2   # 列范围结束
            # 四个子区域
            self.topLeft = None     # type: Code19_2DSegmentTree.Node | None
            self.topRight = None    # type: Code19_2DSegmentTree.Node | None
            self.bottomLeft = None  # type: Code19_2DSegmentTree.Node | None
            self.bottomRight = None # type: Code19_2DSegmentTree.Node | None
        
        def is_leaf(self):
            """
            判断是否是叶子节点
            :return: 如果是叶子节点返回True，否则返回False
            """
            return self.row1 == self.row2 and self.col1 == self.col2
    
    def __init__(self, matrix):
        """
        初始化二维线段树
        
        Args:
            matrix: 输入矩阵
        """
        if not matrix or not matrix[0]:
            self.rows = 0
            self.cols = 0
            self.root = None
            self.matrix = []
            return
        
        self.rows = len(matrix)
        self.cols = len(matrix[0])
        # 复制输入矩阵，避免外部修改影响内部数据
        self.matrix = [row[:] for row in matrix]
        
        # 构建二维线段树
        self.root = self._build_tree(0, self.rows - 1, 0, self.cols - 1)
    
    def _build_tree(self, row1, row2, col1, col2):
        """
        递归构建二维线段树
        
        Args:
            row1: 起始行
            row2: 结束行
            col1: 起始列
            col2: 结束列
            
        Returns:
            构建好的线段树节点
        """
        # 创建新节点
        node = self.Node(row1, row2, col1, col2)
        
        # 叶子节点：区域只包含一个元素
        if row1 == row2 and col1 == col2:
            node.sum = self.matrix[row1][col1]
            return node
        
        # 计算中点
        mid_row = row1 + (row2 - row1) // 2
        mid_col = col1 + (col2 - col1) // 2
        
        # 递归构建四个子区域
        if row1 == row2:
            # 只有一行，按列划分
            node.topLeft = self._build_tree(row1, row2, col1, mid_col)
            node.topRight = self._build_tree(row1, row2, mid_col + 1, col2)
            node.sum = node.topLeft.sum + node.topRight.sum
        elif col1 == col2:
            # 只有一列，按行划分
            node.topLeft = self._build_tree(row1, mid_row, col1, col2)
            node.bottomLeft = self._build_tree(mid_row + 1, row2, col1, col2)
            node.sum = node.topLeft.sum + node.bottomLeft.sum
        else:
            # 一般情况，分为四个子区域
            node.topLeft = self._build_tree(row1, mid_row, col1, mid_col)
            node.topRight = self._build_tree(row1, mid_row, mid_col + 1, col2)
            node.bottomLeft = self._build_tree(mid_row + 1, row2, col1, mid_col)
            node.bottomRight = self._build_tree(mid_row + 1, row2, mid_col + 1, col2)
            node.sum = (node.topLeft.sum + node.topRight.sum + 
                       node.bottomLeft.sum + node.bottomRight.sum)
        
        return node
    
    def update(self, row, col, val):
        """
        更新矩阵中某一点的值
        
        Args:
            row: 行索引
            col: 列索引
            val: 新值
        
        Raises:
            ValueError: 如果索引超出范围
        """
        # 检查索引范围
        if row < 0 or row >= self.rows or col < 0 or col >= self.cols:
            raise ValueError("Index out of bounds")
        
        # 计算变化量并更新矩阵
        delta = val - self.matrix[row][col]
        self.matrix[row][col] = val
        # 更新线段树
        self._update_tree(self.root, row, col, delta)
    
    def _update_tree(self, node, row, col, delta):
        """
        递归更新线段树
        
        Args:
            node: 当前节点
            row: 要更新的行索引
            col: 要更新的列索引
            delta: 变化量
        """
        # 如果当前节点包含要更新的点
        if (row >= node.row1 and row <= node.row2 and 
            col >= node.col1 and col <= node.col2):
            node.sum += delta  # 更新当前节点的和
            
            # 如果不是叶子节点，继续递归更新子节点
            if not node.is_leaf():
                # 根据要更新的点的位置决定更新哪个子节点
                if node.topLeft and row <= node.topLeft.row2 and col <= node.topLeft.col2:
                    self._update_tree(node.topLeft, row, col, delta)
                elif node.topRight and row <= node.topRight.row2 and col >= node.topRight.col1:
                    self._update_tree(node.topRight, row, col, delta)
                elif node.bottomLeft and row >= node.bottomLeft.row1 and col <= node.bottomLeft.col2:
                    self._update_tree(node.bottomLeft, row, col, delta)
                elif node.bottomRight and row >= node.bottomRight.row1 and col >= node.bottomRight.col1:
                    self._update_tree(node.bottomRight, row, col, delta)
    
    def sum_region(self, row1, col1, row2, col2):
        """
        查询区域和
        
        Args:
            row1: 起始行
            col1: 起始列
            row2: 结束行
            col2: 结束列
            
        Returns:
            区域和
            
        Raises:
            ValueError: 如果查询范围无效
        """
        # 检查查询范围的有效性
        if (row1 < 0 or row1 >= self.rows or row2 < 0 or row2 >= self.rows or 
            col1 < 0 or col1 >= self.cols or col2 < 0 or col2 >= self.cols or
            row1 > row2 or col1 > col2):
            raise ValueError("Invalid query range")
        
        # 调用递归查询函数
        return self._query_tree(self.root, row1, col1, row2, col2)
    
    def _query_tree(self, node, row1, col1, row2, col2):
        """
        递归查询区域和
        
        Args:
            node: 当前节点
            row1: 查询起始行
            col1: 查询起始列
            row2: 查询结束行
            col2: 查询结束列
            
        Returns:
            查询结果
        """
        # 查询区域完全包含当前节点区域，直接返回当前节点的和
        if (row1 <= node.row1 and row2 >= node.row2 and 
            col1 <= node.col1 and col2 >= node.col2):
            return node.sum
        
        # 查询区域与当前节点区域无交集，返回0
        if (row2 < node.row1 or row1 > node.row2 or 
            col2 < node.col1 or col1 > node.col2):
            return 0
        
        # 查询区域与当前节点区域有部分交集，递归查询子节点
        sum_val = 0
        if node.topLeft:
            sum_val += self._query_tree(node.topLeft, row1, col1, row2, col2)
        if node.topRight:
            sum_val += self._query_tree(node.topRight, row1, col1, row2, col2)
        if node.bottomLeft:
            sum_val += self._query_tree(node.bottomLeft, row1, col1, row2, col2)
        if node.bottomRight:
            sum_val += self._query_tree(node.bottomRight, row1, col1, row2, col2)
        
        return sum_val

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
    
    seg_tree = Code19_2DSegmentTree(matrix)
    
    # 测试查询
    print(seg_tree.sum_region(2, 1, 4, 3))  # 输出：8
    print(seg_tree.sum_region(1, 1, 2, 2))  # 输出：11
    print(seg_tree.sum_region(1, 2, 2, 4))  # 输出：12
    
    # 测试更新
    seg_tree.update(1, 1, 10)
    print(seg_tree.sum_region(1, 1, 2, 2))  # 输出：15

'''
二维线段树的Python实现优化说明：

1. **内存优化**：
   - 对于大规模数据，Python中的递归深度限制可能会成为问题
   - 可以考虑使用非递归实现或增加递归深度限制
   - 对于非常大的矩阵，可以使用动态开点技术减少内存占用

2. **性能优化**：
   - Python的递归效率相对较低，对于性能要求高的场景可以考虑使用C扩展
   - 对于静态数据，可以使用前缀和方法，时间复杂度为O(1)查询
   - 对于需要频繁更新的场景，二维线段树的优势明显

3. **适用场景**：
   - 需要频繁进行二维区域查询和单点更新
   - 二维范围最值查询（最大值、最小值等）
   - 图像处理中的区域统计
   - 地理信息系统中的范围查询

4. **扩展方向**：
   - 支持更多操作，如区间最值查询
   - 实现动态开点版本以处理稀疏矩阵
   - 实现其他二维数据结构，如二维树状数组、四分树等
'''