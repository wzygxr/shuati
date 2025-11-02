from typing import List, Optional, Tuple

class NestedTree:
    """
    树套树（Tree-of-Trees）实现
    
    概述：
    树套树是一种用于多维范围查询的数据结构，通过将一维数据结构（如线段树、树状数组等）进行嵌套，
    来实现对多维空间的高效管理和查询。本实现采用线段树套线段树的结构，用于二维范围查询。
    
    适用场景：
    - 二维平面上的范围查询（如矩形区域查询）
    - 二维前缀和查询
    - 二维区间最大/最小值查询
    - 二维区间和查询
    - 多维数据的统计分析
    
    时间复杂度：
    - 构建树：O(n log n)，其中n是数据规模
    - 更新操作：O(log^2 n)
    - 区间查询：O(log^2 n)
    
    空间复杂度：
    - O(n log n)，其中n是数据规模
    """
    
    class InnerNode:
        """
        内层线段树节点
        """
        def __init__(self, start: int, end: int):
            self.left = None       # 左子树
            self.right = None      # 右子树
            self.start = start     # 当前区间的起始位置
            self.end = end         # 当前区间的结束位置
            self.sum = 0           # 当前区间的和
    
    class Node:
        """
        外层线段树节点
        """
        def __init__(self, start: int, end: int):
            self.left = None        # 左子树
            self.right = None       # 右子树
            self.inner_root = None  # 内层线段树的根节点
            self.start = start      # 当前区间的起始位置
            self.end = end          # 当前区间的结束位置
            self.sum = 0            # 当前区间的和
    
    def __init__(self, data: List[List[int]]):
        """
        构造函数
        
        Args:
            data: 二维数组数据
            
        Raises:
            ValueError: 如果数据无效
        """
        if not data or not data[0]:
            raise ValueError("输入数据不能为空")
        
        # 创建数据的深拷贝
        self.data = [row.copy() for row in data]
        self.rows = len(data)
        self.cols = len(data[0])
        
        # 构建外层线段树
        self.root = self._build_outer_tree(0, self.rows - 1)
    
    def _build_outer_tree(self, start: int, end: int) -> Node:
        """
        构建外层线段树
        
        Args:
            start: 起始行索引
            end: 结束行索引
            
        Returns:
            构建好的外层线段树根节点
        """
        node = self.Node(start, end)
        
        if start == end:
            # 叶子节点，构建内层线段树
            row_data = self._get_data_row(start)
            node.inner_root = self._build_inner_tree(row_data, 0, self.cols - 1)
            node.sum = self._sum_inner_tree(node.inner_root)
        else:
            mid = start + (end - start) // 2
            node.left = self._build_outer_tree(start, mid)
            node.right = self._build_outer_tree(mid + 1, end)
            
            # 合并子节点的和
            node.sum = (node.left.sum if node.left else 0) + \
                      (node.right.sum if node.right else 0)
        
        return node
    
    def _get_data_row(self, row: int) -> List[int]:
        """
        获取指定行的数据
        
        Args:
            row: 行索引
            
        Returns:
            行数据
        """
        row_data = [0] * self.cols
        if 0 <= row < self.rows:
            row_data = self.data[row].copy()
        return row_data
    
    def _build_inner_tree(self, row_data: List[int], start: int, end: int) -> InnerNode:
        """
        构建内层线段树
        
        Args:
            row_data: 行数据
            start: 起始列索引
            end: 结束列索引
            
        Returns:
            构建好的内层线段树根节点
        """
        node = self.InnerNode(start, end)
        
        if start == end:
            # 叶子节点，直接赋值
            node.sum = row_data[start]
        else:
            mid = start + (end - start) // 2
            node.left = self._build_inner_tree(row_data, start, mid)
            node.right = self._build_inner_tree(row_data, mid + 1, end)
            
            # 合并子节点的和
            node.sum = (node.left.sum if node.left else 0) + \
                      (node.right.sum if node.right else 0)
        
        return node
    
    def _sum_inner_tree(self, root: InnerNode) -> int:
        """
        计算内层线段树的总和
        
        Args:
            root: 内层线段树根节点
            
        Returns:
            树的总和
        """
        return root.sum if root else 0
    
    def _update_outer_tree(self, node: Node, row: int, col: int, diff: int) -> None:
        """
        更新外层线段树
        
        Args:
            node: 当前节点
            row: 行索引
            col: 列索引
            diff: 差值
        """
        if not node or row < node.start or row > node.end:
            return
        
        # 更新节点的和
        node.sum += diff
        
        if node.start == node.end:
            # 叶子节点，更新内层线段树
            self._update_inner_tree(node.inner_root, col, diff)
        else:
            # 递归更新左右子树
            self._update_outer_tree(node.left, row, col, diff)
            self._update_outer_tree(node.right, row, col, diff)
    
    def _update_inner_tree(self, node: InnerNode, col: int, diff: int) -> None:
        """
        更新内层线段树
        
        Args:
            node: 当前节点
            col: 列索引
            diff: 差值
        """
        if not node or col < node.start or col > node.end:
            return
        
        # 更新节点的和
        node.sum += diff
        
        if node.start == node.end:
            # 叶子节点，不需要继续递归
            return
        
        # 递归更新左右子树
        self._update_inner_tree(node.left, col, diff)
        self._update_inner_tree(node.right, col, diff)
    
    def _query_outer_tree(self, node: Node, row1: int, row2: int, 
                         col1: int, col2: int) -> int:
        """
        查询外层线段树
        
        Args:
            node: 当前节点
            row1: 起始行索引
            row2: 结束行索引
            col1: 起始列索引
            col2: 结束列索引
            
        Returns:
            区域和
        """
        if not node or row2 < node.start or row1 > node.end:
            return 0  # 不相交，返回0
        
        if row1 <= node.start and node.end <= row2:
            # 当前节点完全包含在查询范围内，查询内层线段树
            return self._query_inner_tree(node.inner_root, col1, col2)
        
        # 部分重叠，递归查询左右子树
        return (self._query_outer_tree(node.left, row1, row2, col1, col2) + 
                self._query_outer_tree(node.right, row1, row2, col1, col2))
    
    def _query_inner_tree(self, node: InnerNode, col1: int, col2: int) -> int:
        """
        查询内层线段树
        
        Args:
            node: 当前节点
            col1: 起始列索引
            col2: 结束列索引
            
        Returns:
            区域和
        """
        if not node or col2 < node.start or col1 > node.end:
            return 0  # 不相交，返回0
        
        if col1 <= node.start and node.end <= col2:
            # 当前节点完全包含在查询范围内
            return node.sum
        
        # 部分重叠，递归查询左右子树
        return (self._query_inner_tree(node.left, col1, col2) + 
                self._query_inner_tree(node.right, col1, col2))
    
    def _get_outer_tree_height(self, node: Node) -> int:
        """
        计算外层线段树的高度
        
        Args:
            node: 当前节点
            
        Returns:
            树的高度
        """
        if not node:
            return 0
        left_height = self._get_outer_tree_height(node.left)
        right_height = self._get_outer_tree_height(node.right)
        return max(left_height, right_height) + 1
    
    def update(self, row: int, col: int, value: int) -> None:
        """
        更新二维数组中指定位置的值
        
        Args:
            row: 行索引
            col: 列索引
            value: 新值
            
        Raises:
            IndexError: 如果索引超出范围
        """
        if row < 0 or row >= self.rows or col < 0 or col >= self.cols:
            raise IndexError("索引超出范围")
        
        # 计算差值
        diff = value - self.data[row][col]
        self.data[row][col] = value  # 更新原始数据
        
        # 更新线段树
        self._update_outer_tree(self.root, row, col, diff)
    
    def query_range_sum(self, row1: int, col1: int, row2: int, col2: int) -> int:
        """
        查询二维区域和
        
        Args:
            row1: 起始行索引
            col1: 起始列索引
            row2: 结束行索引
            col2: 结束列索引
            
        Returns:
            区域和
            
        Raises:
            ValueError: 如果区域参数无效
        """
        # 验证输入参数
        if (row1 < 0 or row2 >= self.rows or col1 < 0 or col2 >= self.cols or 
            row1 > row2 or col1 > col2):
            raise ValueError("无效的查询区域")
        
        return self._query_outer_tree(self.root, row1, row2, col1, col2)
    
    def get_total_sum(self) -> int:
        """
        获取整个矩阵的和
        
        Returns:
            矩阵总和
        """
        return self.root.sum if self.root else 0
    
    def get_height(self) -> int:
        """
        获取树的高度
        
        Returns:
            树的高度
        """
        return self._get_outer_tree_height(self.root)

# 测试函数
def test_nested_tree():
    """
    测试树套树的功能
    """
    try:
        # 创建一个4x4的二维数组
        matrix = [
            [1, 2, 3, 4],
            [5, 6, 7, 8],
            [9, 10, 11, 12],
            [13, 14, 15, 16]
        ]
        
        # 创建树套树实例
        nested_tree = NestedTree(matrix)
        
        print("树套树构建完成")
        print(f"树高度: {nested_tree.get_height()}")
        print(f"矩阵总和: {nested_tree.get_total_sum()}")
        
        # 测试范围查询
        row1, col1 = 1, 1
        row2, col2 = 2, 3
        sum_result = nested_tree.query_range_sum(row1, col1, row2, col2)
        
        print(f"区域({row1},{col1})到({row2},{col2})的和: {sum_result}")
        
        # 测试更新操作
        nested_tree.update(1, 1, 20)
        print("更新元素(1,1)的值为20后：")
        
        # 重新查询
        sum_result = nested_tree.query_range_sum(row1, col1, row2, col2)
        print(f"区域({row1},{col1})到({row2},{col2})的和: {sum_result}")
        
        print(f"更新后的矩阵总和: {nested_tree.get_total_sum()}")
        
        # 测试边界情况
        try:
            nested_tree.update(-1, 0, 0)  # 无效行索引
        except IndexError as e:
            print(f"边界测试成功: {e}")
            
        try:
            nested_tree.query_range_sum(2, 3, 1, 1)  # 无效的查询区域
        except ValueError as e:
            print(f"查询边界测试成功: {e}")
            
    except Exception as e:
        print(f"错误: {e}")

if __name__ == "__main__":
    test_nested_tree()