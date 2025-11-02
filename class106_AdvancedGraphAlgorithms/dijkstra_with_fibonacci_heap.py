#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
使用斐波那契堆优化的Dijkstra算法实现

应用场景: 网络路由、地图导航
时间复杂度: O(V log V + E) - 使用斐波那契堆
空间复杂度: O(V + E)
"""

import heapq
from typing import List, Tuple, Optional
import math

class Edge:
    """图的边类"""
    
    def __init__(self, to: int, weight: int):
        self.to = to      # 目标节点
        self.weight = weight  # 边的权重

class FibonacciHeapNode:
    """斐波那契堆节点类"""
    
    def __init__(self, vertex: int, distance: int):
        self.vertex = vertex      # 顶点
        self.distance = distance  # 距离
        self.degree = 0           # 节点的度数
        self.marked = False       # 是否被标记
        self.parent: Optional['FibonacciHeapNode'] = None        # 父节点
        self.child: Optional['FibonacciHeapNode'] = None         # 第一个子节点
        self.left: 'FibonacciHeapNode' = self          # 左兄弟节点
        self.right: 'FibonacciHeapNode' = self         # 右兄弟节点

class FibonacciHeap:
    """斐波那契堆实现"""
    
    def __init__(self):
        self.min_node: Optional[FibonacciHeapNode] = None  # 指向最小节点
        self.size = 0         # 堆中节点数量
    
    def is_empty(self) -> bool:
        return self.min_node is None
    
    def insert(self, vertex: int, distance: int) -> FibonacciHeapNode:
        """
        插入新节点到堆中
        时间复杂度：O(1) 均摊
        """
        new_node = FibonacciHeapNode(vertex, distance)
        
        # 将新节点添加到根链表
        if self.min_node is None:
            # 空堆情况
            self.min_node = new_node
        else:
            # 将新节点插入到根链表的min_node旁边
            self._link_root_list(new_node, self.min_node)
            
            # 更新最小节点
            if new_node.distance < self.min_node.distance:
                self.min_node = new_node
        
        # 增加节点计数
        self.size += 1
        return new_node
    
    def extract_min(self) -> Optional[FibonacciHeapNode]:
        """
        提取堆中的最小节点
        时间复杂度：O(log n) 均摊
        """
        if self.is_empty() or self.min_node is None:
            return None
        
        min_node = self.min_node
        
        # 将min的所有子节点提升到根链表
        if min_node.child is not None:
            child = min_node.child
            children = []
            # 收集所有子节点
            current = child
            while True:
                children.append(current)
                current = current.right
                if current == child:
                    break
            
            # 将所有子节点添加到根链表
            for child_node in children:
                # 从子链表中移除child
                self._remove_from_child_list(child_node)
                
                # 添加到根链表
                child_node.parent = None
                if self.min_node is not None:
                    self._link_root_list(child_node, self.min_node)
            
            # 清除min的子节点引用
            min_node.child = None
        
        # 从根链表中移除min
        if min_node.right == min_node:
            # 根链表中只有一个节点
            self.min_node = None
        else:
            # 更新根链表
            self.min_node = min_node.right  # 暂时将min的右侧设为新的min_node
            self._remove_from_root_list(min_node)
            
            # 合并相同度数的树
            self._consolidate()
        
        # 减少节点计数
        self.size -= 1
        
        return min_node
    
    def decrease_key(self, node: FibonacciHeapNode, new_distance: int) -> None:
        """
        减小节点的距离值
        时间复杂度：O(1) 均摊
        """
        if new_distance > node.distance:
            raise ValueError("New distance cannot be greater than current distance")
        
        node.distance = new_distance
        parent = node.parent
        
        # 如果节点在根链表中，或者父节点的距离不大于当前节点，无需其他操作
        if parent is None or parent.distance <= node.distance:
            # 如果是根链表中的节点且距离比当前min_node小，更新min_node
            if parent is None and self.min_node is not None and node.distance < self.min_node.distance:
                self.min_node = node
            return
        
        # 否则，需要进行级联剪枝操作
        self._cut(node, parent)
        self._cascading_cut(parent)
    
    # ==================== 辅助方法 ====================
    
    def _link_root_list(self, node: FibonacciHeapNode, root: FibonacciHeapNode) -> None:
        """将节点链接到根链表"""
        # 在根和根的右侧节点之间插入node
        node.right = root.right
        node.left = root
        root.right.left = node
        root.right = node
    
    def _remove_from_root_list(self, node: FibonacciHeapNode) -> None:
        """从根链表中移除节点"""
        node.left.right = node.right
        node.right.left = node.left
    
    def _remove_from_child_list(self, node: FibonacciHeapNode) -> None:
        """从子链表中移除节点"""
        if node.parent is None:
            return
            
        if node.parent.child == node:
            # 如果是父节点的第一个子节点，更新父节点的child指针
            if node.right != node:
                node.parent.child = node.right
            else:
                node.parent.child = None
        
        # 更新子链表中的双向链接
        node.left.right = node.right
        node.right.left = node.left
    
    def _link_as_child(self, child: FibonacciHeapNode, parent: FibonacciHeapNode) -> None:
        """将一个节点作为另一个节点的子节点"""
        # 从根链表中移除child
        self._remove_from_root_list(child)
        
        # 重置child的状态
        child.parent = parent
        child.marked = False
        
        # 将child添加到parent的子链表中
        if parent.child is None:
            # parent没有子节点
            parent.child = child
            child.left = child
            child.right = child
        else:
            # 将child插入到parent的第一个子节点旁边
            child.right = parent.child.right
            child.left = parent.child
            parent.child.right.left = child
            parent.child.right = child
        
        # 增加parent的度数
        parent.degree += 1
    
    def _consolidate(self) -> None:
        """合并相同度数的树"""
        # 计算最大可能的度数
        max_degree = int(math.log(self.size) / math.log((1 + math.sqrt(5)) / 2)) + 1
        
        # 用于存储不同度数的根节点
        degree_table: List[Optional[FibonacciHeapNode]] = [None] * max_degree
        
        # 遍历所有根节点
        if self.min_node is not None:
            start = self.min_node
            current = start
            roots = []
            
            # 收集所有根节点
            while True:
                roots.append(current)
                current = current.right
                if current == start:
                    break
            
            # 处理每个根节点
            for current in roots:
                degree = current.degree
                next_node = current.right
                
                # 合并相同度数的树
                while degree_table[degree] is not None:
                    other = degree_table[degree]
                    
                    # 确保current的距离不大于other
                    if current.distance > other.distance:
                        current, other = other, current
                    
                    # 将other作为current的子节点
                    self._link_as_child(other, current)
                    
                    # 清除度数表中的条目
                    degree_table[degree] = None
                    degree += 1
                
                # 记录当前度数的根节点
                degree_table[degree] = current
            
            # 重建根链表并找到新的最小节点
            self.min_node = None
            
            for i in range(max_degree):
                if degree_table[i] is not None:
                    # 初始化根链表
                    if self.min_node is None:
                        self.min_node = degree_table[i]
                        if self.min_node is not None:
                            self.min_node.left = self.min_node
                            self.min_node.right = self.min_node
                    else:
                        # 将节点添加到根链表
                        if self.min_node is not None:
                            self._link_root_list(degree_table[i], self.min_node)
                        
                        # 更新最小节点
                        if self.min_node is not None and degree_table[i].distance < self.min_node.distance:
                            self.min_node = degree_table[i]
    
    def _cut(self, node: FibonacciHeapNode, parent: FibonacciHeapNode) -> None:
        """剪切操作：将节点从父节点的子树中移除并添加到根链表"""
        # 从父节点的子链表中移除node
        self._remove_from_child_list(node)
        
        # 减少父节点的度数
        parent.degree -= 1
        
        # 将node添加到根链表
        node.parent = None
        node.marked = False
        if self.min_node is not None:
            self._link_root_list(node, self.min_node)
    
    def _cascading_cut(self, node: FibonacciHeapNode) -> None:
        """级联剪切操作"""
        parent = node.parent
        
        if parent is not None:
            if not node.marked:
                # 如果节点未被标记，标记它
                node.marked = True
            else:
                # 如果节点已被标记，进行剪切并继续级联
                self._cut(node, parent)
                self._cascading_cut(parent)

class DijkstraWithFibonacciHeap:
    """使用斐波那契堆优化的Dijkstra算法"""
    
    def dijkstra(self, graph: List[List[Edge]], start: int) -> List[int]:
        """
        使用斐波那契堆优化的Dijkstra算法
        
        Args:
            graph: 邻接表表示的图
            start: 起始节点
            
        Returns:
            从起始节点到各节点的最短距离数组
        """
        n = len(graph)
        dist = [float('inf')] * n
        dist[start] = 0
        
        # 创建斐波那契堆
        fib_heap = FibonacciHeap()
        nodes: List[Optional[FibonacciHeapNode]] = [None] * n
        
        # 插入所有节点到斐波那契堆
        for i in range(n):
            nodes[i] = fib_heap.insert(i, dist[i])
        
        # Dijkstra算法主循环
        while not fib_heap.is_empty():
            # 提取距离最小的节点
            min_node = fib_heap.extract_min()
            if min_node is None:
                break
            u = min_node.vertex
            
            # 遍历u的所有邻居
            for edge in graph[u]:
                v = edge.to
                weight = edge.weight
                
                # 松弛操作
                if dist[u] != float('inf') and dist[u] + weight < dist[v]:
                    dist[v] = dist[u] + weight
                    # 减小节点v的距离值
                    if nodes[v] is not None:
                        fib_heap.decrease_key(nodes[v], dist[v])
        
        return dist
    
    def dijkstra_with_priority_queue(self, graph: List[List[Edge]], start: int) -> List[int]:
        """
        使用标准优先队列的Dijkstra算法（用于对比）
        
        Args:
            graph: 邻接表表示的图
            start: 起始节点
            
        Returns:
            从起始节点到各节点的最短距离数组
        """
        n = len(graph)
        dist = [float('inf')] * n
        dist[start] = 0
        
        # 使用优先队列（最小堆）
        pq = [(0, start)]  # (distance, vertex)
        
        while pq:
            d, u = heapq.heappop(pq)
            
            # 如果当前距离大于已知最短距离，跳过
            if d > dist[u]:
                continue
            
            # 遍历u的所有邻居
            for edge in graph[u]:
                v = edge.to
                weight = edge.weight
                
                # 松弛操作
                if dist[u] != float('inf') and dist[u] + weight < dist[v]:
                    dist[v] = dist[u] + weight
                    heapq.heappush(pq, (dist[v], v))
        
        return dist
    
    @staticmethod
    def test_dijkstra():
        """测试方法"""
        solution = DijkstraWithFibonacciHeap()
        
        # 创建测试图
        #     10
        # (0)----->(1)
        #  |        |
        #  |5       |1
        #  |        |
        #  v  3     v
        # (2)----->(3)
        #  |        |
        #  |2       |4
        #  |        |
        #  v        v
        # (4)<-----(5)
        #       6
        
        n = 6
        graph = [[] for _ in range(n)]
        
        # 添加边
        graph[0].append(Edge(1, 10))
        graph[0].append(Edge(2, 5))
        graph[1].append(Edge(3, 1))
        graph[2].append(Edge(3, 3))
        graph[2].append(Edge(4, 2))
        graph[3].append(Edge(5, 4))
        graph[5].append(Edge(4, 6))
        
        print("=== 测试Dijkstra算法 ===")
        print("图的邻接表表示:")
        for i in range(n):
            print(f"节点 {i}: ", end="")
            for edge in graph[i]:
                print(f"({edge.to}, {edge.weight}) ", end="")
            print()
        
        start = 0
        print(f"\n从节点 {start} 开始的最短路径:")
        
        # 使用斐波那契堆的Dijkstra算法
        dist1 = solution.dijkstra(graph, start)
        print(f"斐波那契堆优化结果: {dist1}")
        
        # 使用标准优先队列的Dijkstra算法
        dist2 = solution.dijkstra_with_priority_queue(graph, start)
        print(f"标准优先队列结果: {dist2}")
        
        # 验证结果一致性
        print(f"结果一致性: {dist1 == dist2}")

if __name__ == "__main__":
    DijkstraWithFibonacciHeap.test_dijkstra()