import math
import random
from typing import List, Optional, Tuple

class KdTree:
    """
    K-D树（K-Dimensional Tree）实现
    
    概述：
    K-D树是一种用于高维空间数据索引的数据结构，特别适合范围查询和最近邻搜索。
    它通过递归地将空间划分为垂直于坐标轴的超平面，将数据集组织成树形结构。
    
    适用场景：
    - 高维空间中的范围查询
    - 最近邻搜索
    - 计算机图形学中的碰撞检测
    - 机器学习中的快速聚类
    
    时间复杂度：
    - 构建树：O(n log n)，其中n是数据点数量
    - 范围查询：平均O(n^(1-1/k))，最坏O(n)，其中k是维度
    - 最近邻搜索：平均O(log n)，最坏O(n)
    
    空间复杂度：
    - O(n)，其中n是数据点数量
    """
    
    class Node:
        """
        K-D树节点定义
        """
        def __init__(self, point: List[float], split_dim: int):
            self.point = point      # 数据点坐标
            self.left = None        # 左子树
            self.right = None       # 右子树
            self.split_dim = split_dim  # 分割维度
    
    def __init__(self, k: int):
        """
        构造函数
        
        Args:
            k: 空间维度
            
        Raises:
            ValueError: 如果维度不是正数
        """
        if k <= 0:
            raise ValueError("维度必须为正数")
        self.k = k
        self.root = None
        self.random = random.Random()
    
    def build(self, points: List[List[float]]) -> None:
        """
        构建K-D树
        
        Args:
            points: 数据点数组
            
        Raises:
            ValueError: 如果数据点维度不一致
        """
        if not points:
            return
        
        # 验证所有点的维度是否一致
        for point in points:
            if len(point) != self.k:
                raise ValueError(f"所有点必须具有相同的维度：{self.k}")
        
        # 创建副本以避免修改原始数据
        points_copy = [point.copy() for point in points]
        self.root = self._build_tree(points_copy, 0)
    
    def _build_tree(self, points: List[List[float]], depth: int) -> Optional[Node]:
        """
        递归构建K-D树
        
        Args:
            points: 数据点列表
            depth: 当前深度
            
        Returns:
            构建好的子树根节点
        """
        if not points:
            return None
        
        # 根据深度选择分割维度
        split_dim = depth % self.k
        
        # 根据分割维度对点进行排序
        points.sort(key=lambda x: x[split_dim])
        
        # 选择中间点作为根节点
        median_index = len(points) // 2
        node = self.Node(points[median_index], split_dim)
        
        # 递归构建左右子树
        node.left = self._build_tree(points[:median_index], depth + 1)
        node.right = self._build_tree(points[median_index + 1:], depth + 1)
        
        return node
    
    def range_query(self, lower_bound: List[float], upper_bound: List[float]) -> List[List[float]]:
        """
        范围查询
        
        Args:
            lower_bound: 下界
            upper_bound: 上界
            
        Returns:
            范围内的所有点
            
        Raises:
            ValueError: 如果范围参数无效
        """
        self._validate_range(lower_bound, upper_bound)
        result = []
        self._range_query(self.root, lower_bound, upper_bound, result)
        return result
    
    def _range_query(self, node: Optional[Node], lower_bound: List[float], 
                    upper_bound: List[float], result: List[List[float]]) -> None:
        """
        递归执行范围查询
        
        Args:
            node: 当前节点
            lower_bound: 下界
            upper_bound: 上界
            result: 结果集
        """
        if node is None:
            return
        
        # 检查当前点是否在范围内
        in_range = True
        for i in range(self.k):
            if node.point[i] < lower_bound[i] or node.point[i] > upper_bound[i]:
                in_range = False
                break
        
        if in_range:
            result.append(node.point.copy())
        
        # 根据分割维度决定是否需要搜索左右子树
        split_dim = node.split_dim
        if node.point[split_dim] >= lower_bound[split_dim]:
            self._range_query(node.left, lower_bound, upper_bound, result)
        if node.point[split_dim] <= upper_bound[split_dim]:
            self._range_query(node.right, lower_bound, upper_bound, result)
    
    def nearest_neighbor(self, query_point: List[float]) -> List[float]:
        """
        最近邻搜索
        
        Args:
            query_point: 查询点
            
        Returns:
            最近的点
            
        Raises:
            ValueError: 如果查询点维度不匹配
            RuntimeError: 如果树为空
        """
        self._validate_point(query_point)
        if self.root is None:
            raise RuntimeError("K-D树为空")
        
        best_point = self.root.point.copy()
        best_distance = self._distance(query_point, self.root.point)
        
        best_distance = self._nearest_neighbor(self.root, query_point, best_distance, best_point)
        
        return best_point
    
    def _nearest_neighbor(self, node: Optional[Node], query_point: List[float], 
                         best_distance: float, best_point: List[float]) -> float:
        """
        递归执行最近邻搜索
        
        Args:
            node: 当前节点
            query_point: 查询点
            best_distance: 当前最佳距离
            best_point: 当前最佳点（引用传递）
            
        Returns:
            更新后的最佳距离
        """
        if node is None:
            return best_distance
        
        # 计算当前点与查询点的距离
        current_distance = self._distance(query_point, node.point)
        if current_distance < best_distance:
            best_distance = current_distance
            # 更新best_point的内容而不是替换引用
            best_point[:] = node.point[:]
        
        split_dim = node.split_dim
        first_child = None
        second_child = None
        
        # 确定优先搜索的子树
        if query_point[split_dim] < node.point[split_dim]:
            first_child = node.left
            second_child = node.right
        else:
            first_child = node.right
            second_child = node.left
        
        # 优先搜索更可能包含最近点的子树
        best_distance = self._nearest_neighbor(first_child, query_point, best_distance, best_point)
        
        # 判断是否需要搜索另一个子树
        plane_distance = abs(query_point[split_dim] - node.point[split_dim])
        if plane_distance < best_distance:
            best_distance = self._nearest_neighbor(second_child, query_point, best_distance, best_point)
        
        return best_distance
    
    def _distance(self, p1: List[float], p2: List[float]) -> float:
        """
        计算两个点之间的欧几里得距离
        
        Args:
            p1: 第一个点
            p2: 第二个点
            
        Returns:
            距离
        """
        sum_sq = 0.0
        for i in range(self.k):
            diff = p1[i] - p2[i]
            sum_sq += diff * diff
        return math.sqrt(sum_sq)
    
    def _validate_range(self, lower_bound: List[float], upper_bound: List[float]) -> None:
        """
        验证范围参数
        
        Args:
            lower_bound: 下界
            upper_bound: 上界
            
        Raises:
            ValueError: 如果范围参数无效
        """
        if len(lower_bound) != self.k or len(upper_bound) != self.k:
            raise ValueError(f"范围参数维度必须为：{self.k}")
        for i in range(self.k):
            if lower_bound[i] > upper_bound[i]:
                raise ValueError("下界不能大于上界")
    
    def _validate_point(self, point: List[float]) -> None:
        """
        验证点参数
        
        Args:
            point: 点
            
        Raises:
            ValueError: 如果点维度不匹配
        """
        if len(point) != self.k:
            raise ValueError(f"点的维度必须为：{self.k}")
    
    def height(self) -> int:
        """
        获取树的高度
        
        Returns:
            树的高度
        """
        return self._height(self.root)
    
    def _height(self, node: Optional[Node]) -> int:
        """
        递归计算树高
        
        Args:
            node: 当前节点
            
        Returns:
            子树高度
        """
        if node is None:
            return 0
        return max(self._height(node.left), self._height(node.right)) + 1
    
    def size(self) -> int:
        """
        获取树中的节点数
        
        Returns:
            节点数
        """
        return self._size(self.root)
    
    def _size(self, node: Optional[Node]) -> int:
        """
        递归计算节点数
        
        Args:
            node: 当前节点
            
        Returns:
            子树节点数
        """
        if node is None:
            return 0
        return self._size(node.left) + self._size(node.right) + 1

# 测试代码
def test_kd_tree():
    """
    测试K-D树的功能
    """
    try:
        # 创建一个2维的K-D树
        kd_tree = KdTree(2)
        
        # 构建数据点
        points = [
            [2.0, 3.0],
            [5.0, 4.0],
            [9.0, 6.0],
            [4.0, 7.0],
            [8.0, 1.0],
            [7.0, 2.0]
        ]
        
        # 构建K-D树
        kd_tree.build(points)
        
        print(f"K-D树构建完成，高度：{kd_tree.height()}")
        print(f"节点数量：{kd_tree.size()}")
        
        # 测试范围查询
        lower_bound = [3.0, 2.0]
        upper_bound = [8.0, 6.0]
        range_result = kd_tree.range_query(lower_bound, upper_bound)
        print("\n范围查询结果 [(3,2) 到 (8,6)]:")
        for point in range_result:
            print(point)
        
        # 测试最近邻搜索
        query_point = [3.5, 4.5]
        nearest = kd_tree.nearest_neighbor(query_point)
        print(f"\n查询点 {query_point} 的最近邻：{nearest}")
        
        # 边界情况测试
        try:
            invalid_point = [1.0]  # 维度不匹配
            kd_tree.nearest_neighbor(invalid_point)
        except ValueError as e:
            print(f"\n异常测试成功：{e}")
            
        # 空树测试
        try:
            empty_tree = KdTree(2)
            empty_tree.nearest_neighbor([1.0, 2.0])
        except RuntimeError as e:
            print(f"空树测试成功：{e}")
            
    except Exception as e:
        print(f"错误：{e}")

if __name__ == "__main__":
    test_kd_tree()