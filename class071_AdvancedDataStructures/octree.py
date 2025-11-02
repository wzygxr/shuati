import math
from typing import List, Optional, Tuple, Dict, Any

class Octree:
    """
    八叉树（Octree）实现
    
    概述：
    八叉树是一种用于三维空间数据索引的数据结构，通过递归地将三维空间划分为八个子空间（八分象限）。
    每个节点代表三维空间中的一个立方体区域，可以包含点数据或进一步划分为八个子节点。
    
    适用场景：
    - 三维空间中的范围查询
    - 空间分割和最近邻搜索
    - 三维图形处理和碰撞检测
    - 点云数据压缩和处理
    - 三维空间索引
    
    时间复杂度：
    - 构建树：O(n log n)，其中n是数据点数量
    - 插入/查询/删除：平均O(log n)，最坏O(n)
    
    空间复杂度：
    - O(n)，其中n是数据点数量
    """
    
    class Point3D:
        """
        三维点坐标类
        """
        def __init__(self, x: float, y: float, z: float):
            self.x = x
            self.y = y
            self.z = z
        
        def __repr__(self):
            return f"({self.x:.2f}, {self.y:.2f}, {self.z:.2f})"
        
        def __eq__(self, other):
            if not isinstance(other, Octree.Point3D):
                return False
            return (self.x == other.x and 
                    self.y == other.y and 
                    self.z == other.z)
        
        def __hash__(self):
            return hash((self.x, self.y, self.z))
    
    class Node:
        """
        八叉树节点类
        """
        def __init__(self, center: Point3D, size: float):
            self.center = center          # 节点中心坐标
            self.size = size              # 节点大小（立方体边长）
            self.points = []              # 存储在此节点的点
            self.children = [None] * 8    # 子节点，最多8个
            self.is_leaf = True           # 是否为叶节点
        
        def get_child_index(self, point: Point3D) -> int:
            """
            获取点应该放入哪个子节点的索引
            
            Args:
                point: 要定位的点
                
            Returns:
                子节点索引（0-7）
            """
            index = 0
            if point.x >= self.center.x:
                index |= 1
            if point.y >= self.center.y:
                index |= 2
            if point.z >= self.center.z:
                index |= 4
            return index
        
        def split(self, max_points_per_node: int, min_size: float) -> None:
            """
            分裂节点为8个子节点
            
            Args:
                max_points_per_node: 每个节点最大点数
                min_size: 最小节点大小
            """
            if not self.is_leaf:
                return
            
            half_size = self.size / 2
            
            # 创建8个子节点，对应八个八分象限
            for i in range(8):
                # 计算子节点中心坐标
                offset_x = -half_size/2 if (i & 1) == 0 else half_size/2
                offset_y = -half_size/2 if (i & 2) == 0 else half_size/2
                offset_z = -half_size/2 if (i & 4) == 0 else half_size/2
                
                child_center = Octree.Point3D(
                    self.center.x + offset_x,
                    self.center.y + offset_y,
                    self.center.z + offset_z
                )
                
                self.children[i] = Octree.Node(child_center, half_size)
            
            # 将当前节点的点分配到子节点
            for point in self.points:
                child_index = self.get_child_index(point)
                self.children[child_index].insert(point, max_points_per_node, min_size)
            
            self.points.clear()  # 清空当前节点的点
            self.is_leaf = False  # 标记为非叶节点
        
        def insert(self, point: Point3D, max_points_per_node: int, min_size: float) -> None:
            """
            插入点到节点
            
            Args:
                point: 要插入的点
                max_points_per_node: 每个节点最大点数
                min_size: 最小节点大小
            """
            # 如果当前节点不是叶节点，找到对应的子节点插入
            if not self.is_leaf:
                child_index = self.get_child_index(point)
                self.children[child_index].insert(point, max_points_per_node, min_size)
                return
            
            # 将点添加到当前节点
            self.points.append(point)
            
            # 如果点数量超过阈值且节点大小足够大，则分裂
            if len(self.points) > max_points_per_node and self.size > min_size:
                self.split(max_points_per_node, min_size)
    
    def __init__(self, min_bound: Point3D, max_bound: Point3D, max_points_per_node: int = 8):
        """
        构造函数
        
        Args:
            min_bound: 空间最小值边界
            max_bound: 空间最大值边界
            max_points_per_node: 每个节点最大点数
            
        Raises:
            ValueError: 如果边界或参数无效
        """
        self._validate_bounds(min_bound, max_bound)
        if max_points_per_node <= 0:
            raise ValueError("每个节点的最大点数必须大于0")
        
        self.min_bound = min_bound
        self.max_bound = max_bound
        self.max_points_per_node = max_points_per_node
        
        # 计算初始节点的中心和大小
        center = Octree.Point3D(
            (min_bound.x + max_bound.x) / 2,
            (min_bound.y + max_bound.y) / 2,
            (min_bound.z + max_bound.z) / 2
        )
        
        size = max(
            max(max_bound.x - min_bound.x, max_bound.y - min_bound.y),
            max_bound.z - min_bound.z
        )
        
        self.min_size = size / (2 ** 20)  # 设置最小节点大小，避免无限分割
        self.root = Octree.Node(center, size)
    
    def insert(self, point: Point3D) -> None:
        """
        插入单个点
        
        Args:
            point: 要插入的点
            
        Raises:
            ValueError: 如果点超出边界
        """
        self._validate_point(point)
        self.root.insert(point, self.max_points_per_node, self.min_size)
    
    def insert_all(self, points: List[Point3D]) -> None:
        """
        批量插入点
        
        Args:
            points: 要插入的点集合
        """
        for point in points:
            self.insert(point)
    
    def range_query(self, min_bound: Point3D, max_bound: Point3D) -> List[Point3D]:
        """
        范围查询，返回指定立方体区域内的所有点
        
        Args:
            min_bound: 查询区域的最小值边界
            max_bound: 查询区域的最大值边界
            
        Returns:
            查询区域内的所有点
            
        Raises:
            ValueError: 如果查询边界无效
        """
        self._validate_bounds(min_bound, max_bound)
        results = []
        self._range_query(self.root, min_bound, max_bound, results)
        return results
    
    def _range_query(self, node: Node, min_bound: Point3D, max_bound: Point3D, 
                     results: List[Point3D]) -> None:
        """
        递归执行范围查询
        
        Args:
            node: 当前节点
            min_bound: 查询区域的最小值边界
            max_bound: 查询区域的最大值边界
            results: 结果列表（引用传递）
        """
        # 如果节点区域与查询区域不相交，直接返回
        if not self._is_node_overlap_with_range(node, min_bound, max_bound):
            return
        
        # 如果是叶节点，检查每个点是否在查询范围内
        if node.is_leaf:
            for point in node.points:
                if self._is_point_in_range(point, min_bound, max_bound):
                    results.append(point)
        else:
            # 对于非叶节点，递归查询子节点
            for child in node.children:
                if child:
                    self._range_query(child, min_bound, max_bound, results)
    
    def nearest_neighbor(self, query_point: Point3D) -> Point3D:
        """
        最近邻搜索
        
        Args:
            query_point: 查询点
            
        Returns:
            最近的点
            
        Raises:
            ValueError: 如果查询点超出边界
            RuntimeError: 如果树为空
        """
        self._validate_point(query_point)
        
        best_point = None
        best_distance = float('inf')
        
        self._nearest_neighbor(self.root, query_point, best_point, best_distance)
        
        if best_distance == float('inf'):
            raise RuntimeError("八叉树中没有数据点")
        
        return best_point
    
    def _nearest_neighbor(self, node: Node, query_point: Point3D, 
                          best_point: Optional[Point3D], best_distance: float) -> None:
        """
        递归执行最近邻搜索
        
        Args:
            node: 当前节点
            query_point: 查询点
            best_point: 当前最佳点（引用传递）
            best_distance: 当前最佳距离（引用传递）
        """
        # 使用列表包装best_point和best_distance以实现引用传递
        best_point_wrapper = [best_point]
        best_distance_wrapper = [best_distance]
        
        self._nearest_neighbor_helper(node, query_point, best_point_wrapper, best_distance_wrapper)
        
        # 更新外部变量
        best_point = best_point_wrapper[0]
        best_distance = best_distance_wrapper[0]
    
    def _nearest_neighbor_helper(self, node: Node, query_point: Point3D, 
                                best_point: List[Optional[Point3D]], 
                                best_distance: List[float]) -> None:
        """
        最近邻搜索的递归辅助函数
        
        Args:
            node: 当前节点
            query_point: 查询点
            best_point: 包含当前最佳点的列表（引用传递）
            best_distance: 包含当前最佳距离的列表（引用传递）
        """
        # 如果是叶节点，检查每个点
        if node.is_leaf:
            for point in node.points:
                distance = self._calculate_distance(query_point, point)
                if distance < best_distance[0]:
                    best_distance[0] = distance
                    best_point[0] = point
        else:
            # 对于非叶节点，先确定查询点所在的子节点
            child_index = node.get_child_index(query_point)
            
            # 优先搜索包含查询点的子节点
            self._nearest_neighbor_helper(node.children[child_index], query_point, 
                                        best_point, best_distance)
            
            # 然后检查其他子节点，看是否可能包含更近的点
            for i in range(8):
                if i == child_index or node.children[i] is None:
                    continue
                
                # 计算查询点到子节点区域的最小距离
                distance_to_child = self._calculate_distance_to_node(node.children[i], query_point)
                if distance_to_child < best_distance[0]:
                    self._nearest_neighbor_helper(node.children[i], query_point, 
                                                best_point, best_distance)
    
    def _calculate_distance(self, p1: Point3D, p2: Point3D) -> float:
        """
        计算两个点之间的欧几里得距离
        
        Args:
            p1: 第一个点
            p2: 第二个点
            
        Returns:
            欧几里得距离
        """
        dx = p1.x - p2.x
        dy = p1.y - p2.y
        dz = p1.z - p2.z
        return math.sqrt(dx * dx + dy * dy + dz * dz)
    
    def _calculate_distance_to_node(self, node: Node, point: Point3D) -> float:
        """
        计算点到节点区域的最小距离
        
        Args:
            node: 目标节点
            point: 查询点
            
        Returns:
            最小距离
        """
        dx = max(0, abs(point.x - node.center.x) - node.size / 2)
        dy = max(0, abs(point.y - node.center.y) - node.size / 2)
        dz = max(0, abs(point.z - node.center.z) - node.size / 2)
        return math.sqrt(dx * dx + dy * dy + dz * dz)
    
    def _is_point_in_range(self, point: Point3D, min_bound: Point3D, max_bound: Point3D) -> bool:
        """
        检查点是否在指定范围内
        
        Args:
            point: 要检查的点
            min_bound: 范围最小值
            max_bound: 范围最大值
            
        Returns:
            如果点在范围内返回True，否则返回False
        """
        return (point.x >= min_bound.x and point.x <= max_bound.x and
                point.y >= min_bound.y and point.y <= max_bound.y and
                point.z >= min_bound.z and point.z <= max_bound.z)
    
    def _is_node_overlap_with_range(self, node: Node, min_bound: Point3D, 
                                  max_bound: Point3D) -> bool:
        """
        检查节点区域是否与查询范围相交
        
        Args:
            node: 要检查的节点
            min_bound: 查询范围最小值
            max_bound: 查询范围最大值
            
        Returns:
            如果相交返回True，否则返回False
        """
        node_min_x = node.center.x - node.size / 2
        node_max_x = node.center.x + node.size / 2
        node_min_y = node.center.y - node.size / 2
        node_max_y = node.center.y + node.size / 2
        node_min_z = node.center.z - node.size / 2
        node_max_z = node.center.z + node.size / 2
        
        # 快速排斥测试
        return not (node_max_x < min_bound.x or node_min_x > max_bound.x or
                   node_max_y < min_bound.y or node_min_y > max_bound.y or
                   node_max_z < min_bound.z or node_min_z > max_bound.z)
    
    def _validate_bounds(self, min_bound: Point3D, max_bound: Point3D) -> None:
        """
        验证边界参数
        
        Args:
            min_bound: 最小值边界
            max_bound: 最大值边界
            
        Raises:
            ValueError: 如果边界无效
        """
        if min_bound is None or max_bound is None:
            raise ValueError("边界不能为None")
        if (min_bound.x > max_bound.x or 
            min_bound.y > max_bound.y or 
            min_bound.z > max_bound.z):
            raise ValueError("最小值边界不能大于最大值边界")
    
    def _validate_point(self, point: Point3D) -> None:
        """
        验证点是否在树的边界内
        
        Args:
            point: 要验证的点
            
        Raises:
            ValueError: 如果点超出边界
        """
        if point is None:
            raise ValueError("点不能为None")
        if not self._is_point_in_range(point, self.min_bound, self.max_bound):
            raise ValueError(f"点超出八叉树的边界: {point}")
    
    def height(self) -> int:
        """
        计算树的高度
        
        Returns:
            树的高度
        """
        return self._height(self.root)
    
    def _height(self, node: Node) -> int:
        """
        递归计算树高
        
        Args:
            node: 当前节点
            
        Returns:
            子树高度
        """
        if node.is_leaf:
            return 1
        max_height = 0
        for child in node.children:
            if child:
                max_height = max(max_height, self._height(child))
        return max_height + 1
    
    def size(self) -> int:
        """
        获取树中点的总数
        
        Returns:
            点的数量
        """
        return self._size(self.root)
    
    def _size(self, node: Node) -> int:
        """
        递归计算节点数
        
        Args:
            node: 当前节点
            
        Returns:
            子树中点的数量
        """
        if node.is_leaf:
            return len(node.points)
        total_size = 0
        for child in node.children:
            if child:
                total_size += self._size(child)
        return total_size

# 测试函数
def test_octree():
    """
    测试八叉树的功能
    """
    try:
        # 定义空间边界
        min_bound = Octree.Point3D(0, 0, 0)
        max_bound = Octree.Point3D(10, 10, 10)
        
        # 创建八叉树
        octree = Octree(min_bound, max_bound, 4)
        
        # 创建一些测试点
        points = [
            Octree.Point3D(1, 1, 1),
            Octree.Point3D(2, 3, 4),
            Octree.Point3D(5, 6, 7),
            Octree.Point3D(8, 9, 10),
            Octree.Point3D(3, 3, 3),
            Octree.Point3D(6, 6, 6),
            Octree.Point3D(9, 9, 9),
            Octree.Point3D(1, 9, 5),
            Octree.Point3D(5, 5, 5)
        ]
        
        # 插入所有点
        octree.insert_all(points)
        
        print(f"八叉树构建完成")
        print(f"树高度: {octree.height()}")
        print(f"点的数量: {octree.size()}")
        
        # 测试范围查询
        query_min = Octree.Point3D(2, 2, 2)
        query_max = Octree.Point3D(7, 7, 7)
        range_results = octree.range_query(query_min, query_max)
        
        print("\n范围查询结果 (2-7, 2-7, 2-7):")
        for point in range_results:
            print(point)
        
        # 测试最近邻搜索
        query_point = Octree.Point3D(4, 4, 4)
        nearest = octree.nearest_neighbor(query_point)
        
        print(f"\n查询点 {query_point} 的最近邻: {nearest}")
        
        # 计算距离
        distance = math.sqrt(
            (query_point.x - nearest.x) ** 2 +
            (query_point.y - nearest.y) ** 2 +
            (query_point.z - nearest.z) ** 2
        )
        print(f"距离: {distance:.4f}")
        
        # 测试边界情况
        try:
            octree.insert(Octree.Point3D(11, 1, 1))  # 超出边界
        except ValueError as e:
            print(f"\n边界测试成功: {e}")
            
        # 测试空树
        try:
            empty_tree = Octree(Octree.Point3D(0, 0, 0), Octree.Point3D(10, 10, 10))
            empty_tree.nearest_neighbor(Octree.Point3D(5, 5, 5))
        except RuntimeError as e:
            print(f"空树测试成功: {e}")
            
    except Exception as e:
        print(f"错误: {e}")

if __name__ == "__main__":
    test_octree()