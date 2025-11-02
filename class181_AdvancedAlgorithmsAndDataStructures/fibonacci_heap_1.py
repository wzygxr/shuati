#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
斐波那契堆实现 (Python版本)

算法思路：
斐波那契堆是一种高效的优先队列数据结构，支持多种操作的均摊时间复杂度都很优秀。
它通过延迟合并和标记机制来实现高效的性能。

应用场景：
1. 图算法：最短路径、最小生成树
2. 网络优化：路由算法、流量调度
3. 操作系统：任务调度

时间复杂度：
- 插入：O(1) 均摊
- 提取最小：O(log n) 均摊
- 减小键值：O(1) 均摊
- 删除：O(log n) 均摊
- 合并：O(1)

空间复杂度：O(n)

相关题目：
1. LeetCode 743. 网络延迟时间
2. LeetCode 1584. 连接所有点的最小费用
3. LeetCode 1135. 最低成本联通所有城市
"""

import math
import random

class FibonacciHeapNode:
    """斐波那契堆节点类"""
    
    def __init__(self, key, priority):
        """
        构造函数
        :param key: 节点键值
        :param priority: 节点优先级
        """
        self.key = key                 # 节点键值
        self.priority = priority       # 优先级（用于排序）
        self.degree = 0                # 节点的度数（子节点数量）
        self.marked = False            # 是否被标记（用于级联剪枝）
        self.parent = None             # 父节点
        self.child = None              # 第一个子节点
        self.left = self               # 左侧兄弟节点
        self.right = self              # 右侧兄弟节点

class FibonacciHeap:
    """斐波那契堆实现"""
    
    def __init__(self):
        """构造空堆"""
        self.min_node = None  # 指向最小节点
        self.size = 0         # 堆中节点数量
    
    def is_empty(self):
        """
        检查堆是否为空
        :return: 堆是否为空
        """
        return self.min_node is None
    
    def get_size(self):
        """
        获取堆中元素数量
        :return: 元素数量
        """
        return self.size
    
    def insert(self, key, priority):
        """
        插入新节点到堆中
        时间复杂度：O(1) 均摊
        :param key: 节点键值
        :param priority: 节点优先级
        :return: 新插入的节点
        """
        new_node = FibonacciHeapNode(key, priority)
        
        # 将新节点添加到根链表
        if self.min_node is None:
            # 空堆情况
            self.min_node = new_node
        else:
            # 将新节点插入到根链表的min_node旁边
            self._link_root_list(new_node, self.min_node)
            
            # 更新最小节点
            if new_node.priority < self.min_node.priority:
                self.min_node = new_node
        
        # 增加节点计数
        self.size += 1
        return new_node
    
    def merge(self, other):
        """
        合并两个斐波那契堆
        时间复杂度：O(1)
        :param other: 要合并的另一个堆
        """
        if other is None or other.is_empty():
            return  # 空堆无需合并
        
        if self.is_empty():
            # 如果当前堆为空，直接接管other的min_node
            self.min_node = other.min_node
            self.size = other.size
            return
        
        if self.min_node is not None and other.min_node is not None:
            # 合并两个根链表
            self_right = self.min_node.right
            other_left = other.min_node.left
            
            self.min_node.right = other.min_node
            other.min_node.left = self.min_node
            
            self_right.left = other_left
            other_left.right = self_right
            
            # 更新最小节点
            if other.min_node.priority < self.min_node.priority:
                self.min_node = other.min_node
            
            # 更新节点数量
            self.size += other.size
            
            # 重置other堆，避免悬空引用
            other.min_node = None
            other.size = 0
    
    def extract_min(self):
        """
        提取堆中的最小节点
        时间复杂度：O(log n) 均摊
        :return: 最小节点的键值，如果堆为空返回None
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
        
        return min_node.key if min_node is not None else None

    def decrease_key(self, node, new_priority):
        """
        减小节点的优先级
        时间复杂度：O(1) 均摊
        :param node: 要修改的节点
        :param new_priority: 新的优先级
        :raises ValueError: 如果新优先级大于当前优先级
        """
        if new_priority > node.priority:
            raise ValueError("New priority cannot be greater than current priority")
        
        node.priority = new_priority
        parent = node.parent
        
        # 如果节点在根链表中，或者父节点的优先级不大于当前节点，无需其他操作
        if parent is None or parent.priority <= node.priority:
            # 如果是根链表中的节点且优先级比当前min_node小，更新min_node
            if parent is None and self.min_node is not None and node.priority < self.min_node.priority:
                self.min_node = node
            return
        
        # 否则，需要进行级联剪枝操作
        self._cut(node, parent)
        self._cascading_cut(parent)
    
    def delete(self, node):
        """
        删除指定节点
        时间复杂度：O(log n) 均摊
        :param node: 要删除的节点
        """
        if node is None:
            return
            
        # 将节点优先级设置为负无穷，使其成为新的最小节点
        self.decrease_key(node, float('-inf'))
        
        # 提取最小节点（即刚刚被设置为负无穷的节点）
        self.extract_min()
    
    def get_min(self):
        """
        获取最小节点（不移除）
        时间复杂度：O(1)
        :return: 最小节点的键值，如果堆为空返回None
        """
        return None if self.is_empty() or self.min_node is None else self.min_node.key
    
    # ==================== 辅助方法 ====================
    
    def _link_root_list(self, node, root):
        """
        将节点链接到根链表
        """
        # 在根和根的右侧节点之间插入node
        node.right = root.right
        node.left = root
        root.right.left = node
        root.right = node
    
    def _remove_from_root_list(self, node):
        """
        从根链表中移除节点
        """
        node.left.right = node.right
        node.right.left = node.left
    
    def _remove_from_child_list(self, node):
        """
        从子链表中移除节点
        """
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
    
    def _link_as_child(self, child, parent):
        """
        将一个节点作为另一个节点的子节点
        """
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
    
    def _consolidate(self):
        """
        合并相同度数的树
        """
        # 计算最大可能的度数，理论上不会超过log_phi(n)，其中phi是黄金分割比
        max_degree = int(math.log(self.size) / math.log((1 + math.sqrt(5)) / 2)) + 1
        
        # 用于存储不同度数的根节点
        degree_table = [None] * max_degree
        
        # 遍历所有根节点
        start = self.min_node
        current = start
        roots = []
        
        # 收集所有根节点
        if current is not None:
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
                
                # 确保current的优先级不大于other
                if current.priority > other.priority:
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
                    self._link_root_list(degree_table[i], self.min_node)
                    
                    # 更新最小节点
                    if self.min_node is not None and degree_table[i] is not None and degree_table[i].priority < self.min_node.priority:
                        self.min_node = degree_table[i]
    
    def _cut(self, node, parent):
        """
        剪切操作：将节点从父节点的子树中移除并添加到根链表
        """
        # 从父节点的子链表中移除node
        self._remove_from_child_list(node)
        
        # 减少父节点的度数
        parent.degree -= 1
        
        # 将node添加到根链表
        node.parent = None
        node.marked = False
        self._link_root_list(node, self.min_node)
    
    def _cascading_cut(self, node):
        """
        级联剪切操作
        """
        parent = node.parent
        
        if parent is not None:
            if not node.marked:
                # 如果节点未被标记，标记它
                node.marked = True
            else:
                # 如果节点已被标记，进行剪切并继续级联
                self._cut(node, parent)
                self._cascading_cut(parent)
    
    def print_heap(self):
        """
        打印堆的结构（用于调试）
        """
        if self.is_empty():
            print("Heap is empty")
            return
        
        print("Fibonacci Heap Structure:")
        visited = set()
        self._print_node(self.min_node, 0, visited)
    
    def _print_node(self, node, level, visited):
        """
        递归打印节点及其子节点
        """
        if node is None or node in visited:
            return
        
        visited.add(node)
        
        # 打印缩进
        print("  " * level, end="")
        
        # 打印节点信息
        print(f"Key: {node.key}, Priority: {node.priority}, Degree: {node.degree}, Marked: {node.marked}")
        
        # 递归打印子节点
        if node.child is not None:
            child = node.child
            while True:
                self._print_node(child, level + 1, visited)
                child = child.right
                if child == node.child:
                    break
    
    @staticmethod
    def test_fibonacci_heap():
        """测试斐波那契堆的各种操作"""
        print("=== 测试斐波那契堆 ===")
        heap = FibonacciHeap()
        
        # 测试插入操作
        print("\n1. 测试插入操作:")
        node1 = heap.insert("Task 1", 5)
        node2 = heap.insert("Task 2", 3)
        node3 = heap.insert("Task 3", 8)
        node4 = heap.insert("Task 4", 1)
        node5 = heap.insert("Task 5", 10)
        
        print("插入5个节点后，最小节点:", heap.get_min())  # 应该是 Task 4
        
        # 测试提取最小节点
        print("\n2. 测试提取最小节点:")
        min1 = heap.extract_min()
        print("提取的最小节点:", min1)  # 应该是 Task 4
        print("提取后，最小节点:", heap.get_min())  # 应该是 Task 2
        
        # 测试减小键值
        print("\n3. 测试减小键值:")
        heap.decrease_key(node3, 2)
        print("减小Task 3的优先级后，最小节点:", heap.get_min())  # 应该是 Task 3
        
        # 测试删除节点
        print("\n4. 测试删除节点:")
        heap.delete(node5)
        print("删除Task 5后，最小节点:", heap.get_min())  # 仍然是 Task 3
        
        # 测试合并操作
        print("\n5. 测试合并操作:")
        heap2 = FibonacciHeap()
        heap2.insert("Task A", 4)
        heap2.insert("Task B", 6)
        
        heap.merge(heap2)
        print("合并两个堆后，最小节点:", heap.get_min())  # 仍然是 Task 3
        print("堆大小:", heap.get_size())  # 应该是 5
        
        # 测试提取所有元素
        print("\n6. 测试提取所有元素:")
        print("按优先级提取顺序: ", end="")
        while not heap.is_empty():
            print(heap.extract_min(), end=" ")
        print()
        
        # 测试边界情况
        print("\n7. 测试边界情况:")
        print("空堆获取最小节点:", heap.get_min())  # 应该是 None
        print("空堆提取最小节点:", heap.extract_min())  # 应该是 None
        
        # 性能测试
        print("\n=== 性能测试 ===")
        import time
        
        # 测试大量插入操作
        large_heap = FibonacciHeap()
        
        start_time = time.time()
        nodes = []
        for i in range(10000):
            priority = random.randint(1, 100000)
            node = large_heap.insert(f"Task {i}", priority)
            nodes.append(node)
        insert_time = time.time() - start_time
        
        print(f"插入10000个节点时间: {insert_time*1000:.2f} ms")
        print(f"堆大小: {large_heap.get_size()}")
        
        # 测试随机减小键值操作
        start_time = time.time()
        for i in range(1000):
            node = random.choice(nodes)
            new_priority = random.randint(1, 100)
            try:
                large_heap.decrease_key(node, new_priority)
            except ValueError:
                pass  # 忽略无效操作
        decrease_time = time.time() - start_time
        
        print(f"1000次减小键值操作时间: {decrease_time*1000:.2f} ms")
        
        # 测试提取最小操作
        start_time = time.time()
        extracted_count = 0
        while not large_heap.is_empty() and extracted_count < 1000:
            large_heap.extract_min()
            extracted_count += 1
        extract_time = time.time() - start_time
        
        print(f"提取{extracted_count}个最小节点时间: {extract_time*1000:.2f} ms")

if __name__ == "__main__":
    FibonacciHeap.test_fibonacci_heap()