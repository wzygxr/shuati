#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
高级数据结构实现

包含：
1. 斐波那契堆（Fibonacci Heap）
2. 块状链表（Unrolled Linked List）

时间复杂度分析：
- 斐波那契堆：
  - 插入：O(1) 均摊
  - 查找最小值：O(1)
  - 合并：O(1)
  - 提取最小值：O(log n) 均摊
  - 减少键值：O(1) 均摊
  - 删除：O(log n) 均摊

- 块状链表：
  - 插入：O(1) 均摊
  - 删除：O(1) 均摊
  - 查找：O(n/b)，其中b是块大小
  - 遍历：O(n)
"""

import math
from collections import defaultdict

# ============================================
# 1. 斐波那契堆（Fibonacci Heap）实现
# ============================================

class FibonacciHeapNode:
    """
    斐波那契堆节点类
    """
    def __init__(self, key, value=None):
        """
        初始化斐波那契堆节点
        
        Args:
            key: 节点的键值，用于比较
            value: 节点存储的值（可选）
        """
        self.key = key                # 键值
        self.value = value or key     # 存储的值
        self.degree = 0               # 子节点的数量
        self.mark = False             # 是否被标记（是否失去过子节点）
        self.parent = None            # 父节点引用
        self.child = None             # 第一个子节点引用
        self.left = self              # 左兄弟引用
        self.right = self             # 右兄弟引用

class FibonacciHeap:
    """
    斐波那契堆实现
    特点：
    1. 支持快速插入、合并和减少键值操作（均为O(1)均摊时间复杂度）
    2. 适合需要频繁插入和减少键值的场景，如Dijkstra算法
    3. 结构复杂但性能优秀
    """
    
    def __init__(self):
        """
        初始化空的斐波那契堆
        """
        self.min_node = None  # 指向最小键值的节点
        self.node_count = 0   # 堆中节点的数量
    
    def is_empty(self):
        """
        检查堆是否为空
        
        Returns:
            bool: 如果堆为空返回True，否则返回False
        """
        return self.min_node is None
    
    def insert(self, key, value=None):
        """
        插入新节点到斐波那契堆
        时间复杂度：O(1) 均摊
        
        Args:
            key: 节点的键值
            value: 节点存储的值（可选）
            
        Returns:
            FibonacciHeapNode: 新插入的节点引用
        """
        # 创建新节点
        new_node = FibonacciHeapNode(key, value)
        
        # 将新节点添加到根链表
        if self.min_node is None:
            # 空堆的情况
            self.min_node = new_node
        else:
            # 非空堆，将新节点插入到min_node的右边
            self._link_nodes(self.min_node, new_node)
            # 更新最小节点
            if new_node.key < self.min_node.key:
                self.min_node = new_node
        
        # 增加节点计数
        self.node_count += 1
        return new_node
    
    def _link_nodes(self, left, right):
        """
        将right节点链接到left节点的右边，形成双向循环链表
        
        Args:
            left: 左边的节点
            right: 右边的节点
        """
        left.right.left = right
        right.right = left.right
        left.right = right
        right.left = left
    
    def extract_min(self):
        """
        提取并返回堆中键值最小的节点
        时间复杂度：O(log n) 均摊
        
        Returns:
            FibonacciHeapNode: 键值最小的节点，如果堆为空返回None
        """
        if self.is_empty():
            return None
        
        # 保存要返回的最小节点
        min_node = self.min_node
        
        # 如果只有一个节点
        if min_node.left == min_node and min_node.child is None:
            self.min_node = None
            self.node_count = 0
            return min_node
        
        # 将最小节点的所有子节点提升到根链表
        if min_node.child is not None:
            child = min_node.child
            while True:
                next_child = child.right
                # 断开子节点与父节点的连接
                child.parent = None
                # 将子节点插入到根链表
                self._link_nodes(min_node, child)
                # 移动到下一个子节点
                child = next_child
                if child == min_node.child:
                    break
        
        # 从根链表中移除最小节点
        min_node.left.right = min_node.right
        min_node.right.left = min_node.left
        
        # 如果最小节点是其唯一的根节点，则更新min_node为其任意子节点
        if min_node == min_node.right:
            self.min_node = min_node.child
        else:
            # 否则，将min_node设置为任意一个根节点，然后执行合并操作
            self.min_node = min_node.right
            # 执行合并操作，确保每个度数的树只有一个
            self._consolidate()
        
        # 减少节点计数
        self.node_count -= 1
        return min_node
    
    def _consolidate(self):
        """
        合并具有相同度数的树，确保每个度数的树最多只有一个
        这是extract_min操作中的关键步骤
        """
        # 创建度数表，用于存储不同度数的根节点
        max_degree = int(math.log2(self.node_count)) + 1 if self.node_count > 0 else 0
        degree_table = [None] * (max_degree + 2)
        
        # 获取根链表中的第一个节点
        current = self.min_node
        if current is None:
            return
        
        # 收集所有根节点
        roots = []
        while True:
            roots.append(current)
            current = current.right
            if current == self.min_node:
                break
        
        # 处理每个根节点
        for root in roots:
            degree = root.degree
            # 合并具有相同度数的树
            while degree_table[degree] is not None:
                other = degree_table[degree]
                # 确保root是键值较小的节点
                if root.key > other.key:
                    root, other = other, root
                
                # 将other作为root的子节点
                self._link_as_child(other, root)
                
                # 清空度数表中该度数的条目
                degree_table[degree] = None
                degree += 1
            
            # 将合并后的树存入度数表
            degree_table[degree] = root
        
        # 重新设置min_node
        self.min_node = None
        for i in range(len(degree_table)):
            if degree_table[i] is not None:
                if self.min_node is None:
                    self.min_node = degree_table[i]
                    # 初始化循环链表
                    degree_table[i].left = degree_table[i]
                    degree_table[i].right = degree_table[i]
                else:
                    # 将节点添加到根链表
                    self._link_nodes(self.min_node, degree_table[i])
                    # 更新最小节点
                    if degree_table[i].key < self.min_node.key:
                        self.min_node = degree_table[i]
    
    def _link_as_child(self, child_node, parent_node):
        """
        将child_node链接为parent_node的子节点
        
        Args:
            child_node: 子节点
            parent_node: 父节点
        """
        # 从根链表中移除子节点
        child_node.left.right = child_node.right
        child_node.right.left = child_node.left
        
        # 重置子节点的引用
        child_node.parent = parent_node
        child_node.mark = False
        
        # 将子节点添加到父节点的子节点列表
        if parent_node.child is None:
            parent_node.child = child_node
            child_node.left = child_node
            child_node.right = child_node
        else:
            self._link_nodes(parent_node.child, child_node)
        
        # 增加父节点的度数
        parent_node.degree += 1
    
    def decrease_key(self, node, new_key):
        """
        减少指定节点的键值
        时间复杂度：O(1) 均摊
        
        Args:
            node: 要减少键值的节点
            new_key: 新的键值
            
        Raises:
            ValueError: 如果新键值大于当前键值
        """
        if new_key > node.key:
            raise ValueError("新键值必须小于当前键值")
        
        node.key = new_key
        parent = node.parent
        
        # 如果节点在根链表或父节点的键值小于等于当前节点的键值，则只需更新min_node
        if parent is None or parent.key <= node.key:
            # 更新最小节点
            if node.key < self.min_node.key:
                self.min_node = node
        else:
            # 否则，执行级联剪切操作
            self._cut(node, parent)
            self._cascading_cut(parent)
    
    def _cut(self, node, parent):
        """
        将node从parent的子节点列表中移除，并添加到根链表
        
        Args:
            node: 要剪切的节点
            parent: 父节点
        """
        # 减少父节点的度数
        parent.degree -= 1
        
        # 如果node是父节点的唯一子节点
        if parent.child == node and node.right == node:
            parent.child = None
        else:
            parent.child = node.right
            # 从子节点链表中移除node
            node.left.right = node.right
            node.right.left = node.left
        
        # 将node添加到根链表
        self._link_nodes(self.min_node, node)
        node.parent = None
        node.mark = False
        
        # 更新最小节点
        if node.key < self.min_node.key:
            self.min_node = node
    
    def _cascading_cut(self, node):
        """
        级联剪切操作，向上检查并剪切被标记的节点
        
        Args:
            node: 开始检查的节点
        """
        parent = node.parent
        if parent is not None:
            if not node.mark:
                # 第一次失去子节点，标记该节点
                node.mark = True
            else:
                # 已经被标记，再次失去子节点，执行剪切
                self._cut(node, parent)
                self._cascading_cut(parent)
    
    def delete(self, node):
        """
        删除指定节点
        时间复杂度：O(log n) 均摊
        
        Args:
            node: 要删除的节点
        """
        # 将节点的键值减少到负无穷
        self.decrease_key(node, float('-inf'))
        # 提取最小节点（即刚被设置为负无穷的节点）
        self.extract_min()
    
    def merge(self, other_heap):
        """
        合并两个斐波那契堆
        时间复杂度：O(1)
        
        Args:
            other_heap: 要合并的另一个斐波那契堆
        """
        # 如果另一个堆为空，直接返回
        if other_heap.is_empty():
            return
        
        # 如果当前堆为空，直接复制另一个堆的属性
        if self.is_empty():
            self.min_node = other_heap.min_node
            self.node_count = other_heap.node_count
            return
        
        # 合并两个根链表
        self._link_nodes(self.min_node, other_heap.min_node)
        # 更新最小节点
        if other_heap.min_node.key < self.min_node.key:
            self.min_node = other_heap.min_node
        # 更新节点计数
        self.node_count += other_heap.node_count
    
    def get_min(self):
        """
        获取堆中键值最小的节点但不移除
        时间复杂度：O(1)
        
        Returns:
            FibonacciHeapNode: 键值最小的节点，如果堆为空返回None
        """
        return self.min_node
    
    def size(self):
        """
        获取堆中节点的数量
        
        Returns:
            int: 节点数量
        """
        return self.node_count

# ============================================
# 2. 块状链表（Unrolled Linked List）实现
# ============================================

class Block:
    """
    块状链表中的块类
    """
    def __init__(self, max_size=16):
        """
        初始化块
        
        Args:
            max_size: 块的最大容量
        """
        self.data = []             # 存储数据的列表
        self.max_size = max_size   # 块的最大容量
        self.next = None           # 指向下一个块的引用

class UnrolledLinkedList:
    """
    块状链表实现
    特点：
    1. 结合了数组和链表的优点
    2. 减少了纯链表的内存开销和指针操作
    3. 支持高效的区间操作
    """
    
    def __init__(self, block_size=16):
        """
        初始化块状链表
        
        Args:
            block_size: 每个块的最大容量，默认为16
        """
        self.head = Block(block_size)  # 头节点
        self.tail = self.head          # 尾节点
        self.block_size = block_size   # 块大小
        self.size = 0                  # 元素总数
    
    def is_empty(self):
        """
        检查链表是否为空
        
        Returns:
            bool: 如果链表为空返回True，否则返回False
        """
        return self.size == 0
    
    def _get_block_and_index(self, index):
        """
        获取指定索引所在的块和在块内的位置
        
        Args:
            index: 元素索引
            
        Returns:
            tuple: (块引用, 块内索引)
            
        Raises:
            IndexError: 如果索引无效
        """
        if index < 0 or index >= self.size:
            raise IndexError("索引超出范围")
        
        current_block = self.head
        while len(current_block.data) <= index:
            index -= len(current_block.data)
            current_block = current_block.next
        
        return current_block, index
    
    def get(self, index):
        """
        获取指定索引的元素
        时间复杂度：O(n/b)，其中b是块大小
        
        Args:
            index: 元素索引
            
        Returns:
            索引位置的元素
        """
        block, block_index = self._get_block_and_index(index)
        return block.data[block_index]
    
    def set(self, index, value):
        """
        设置指定索引的元素值
        时间复杂度：O(n/b)
        
        Args:
            index: 元素索引
            value: 新的值
        """
        block, block_index = self._get_block_and_index(index)
        block.data[block_index] = value
    
    def insert(self, index, value):
        """
        在指定位置插入元素
        时间复杂度：O(n/b)均摊
        
        Args:
            index: 插入位置
            value: 要插入的值
        """
        if index < 0 or index > self.size:
            raise IndexError("索引超出范围")
        
        # 处理空链表的情况
        if self.is_empty():
            self.head.data.append(value)
            self.size += 1
            return
        
        # 找到插入位置所在的块
        current_block = self.head
        prev_block = None
        current_index = 0
        
        while current_block and current_index + len(current_block.data) <= index:
            current_index += len(current_block.data)
            prev_block = current_block
            current_block = current_block.next
        
        # 如果需要创建新的块（插入到末尾）
        if current_block is None:
            current_block = Block(self.block_size)
            prev_block.next = current_block
            self.tail = current_block
            block_index = 0
        else:
            block_index = index - current_index
        
        # 插入元素
        current_block.data.insert(block_index, value)
        self.size += 1
        
        # 如果块过大，分割块
        if len(current_block.data) > self.block_size:
            self._split_block(current_block)
    
    def _split_block(self, block):
        """
        分割过大的块
        
        Args:
            block: 要分割的块
        """
        # 创建新块
        new_block = Block(self.block_size)
        
        # 计算分割点
        split_point = len(block.data) // 2
        
        # 移动后半部分数据到新块
        new_block.data = block.data[split_point:]
        block.data = block.data[:split_point]
        
        # 插入新块到链表中
        new_block.next = block.next
        block.next = new_block
        
        # 更新尾节点
        if block == self.tail:
            self.tail = new_block
    
    def append(self, value):
        """
        在链表末尾添加元素
        时间复杂度：O(1)均摊
        
        Args:
            value: 要添加的值
        """
        # 如果尾块已满，创建新块
        if len(self.tail.data) >= self.block_size:
            new_block = Block(self.block_size)
            self.tail.next = new_block
            self.tail = new_block
        
        # 添加元素到尾块
        self.tail.data.append(value)
        self.size += 1
    
    def prepend(self, value):
        """
        在链表头部添加元素
        时间复杂度：O(1)均摊
        
        Args:
            value: 要添加的值
        """
        # 如果头块已满，创建新块
        if len(self.head.data) >= self.block_size:
            new_block = Block(self.block_size)
            new_block.next = self.head
            self.head = new_block
        
        # 添加元素到头块的开头
        self.head.data.insert(0, value)
        self.size += 1
    
    def delete(self, index):
        """
        删除指定位置的元素
        时间复杂度：O(n/b)
        
        Args:
            index: 要删除的元素索引
            
        Returns:
            被删除的元素值
        """
        block, block_index = self._get_block_and_index(index)
        
        # 删除元素
        value = block.data.pop(block_index)
        self.size -= 1
        
        # 如果链表只有一个块且为空，保持头块不变
        if self.size == 0:
            self.head.data = []
            self.tail = self.head
            return value
        
        # 如果块变得太小，尝试合并
        if block != self.head and len(block.data) < self.block_size // 2:
            self._merge_or_redistribute(block)
        
        return value
    
    def _merge_or_redistribute(self, block):
        """
        合并或重新分配块中的元素
        
        Args:
            block: 需要处理的块
        """
        # 找到前一个块
        prev_block = self.head
        while prev_block and prev_block.next != block:
            prev_block = prev_block.next
        
        # 尝试从下一个块借元素
        if block.next and len(block.next.data) > self.block_size // 2:
            # 从下一个块的开头借一个元素
            block.data.append(block.next.data.pop(0))
        # 尝试向前一个块借元素
        elif prev_block and len(prev_block.data) > self.block_size // 2:
            # 从前一个块的末尾借一个元素
            block.data.insert(0, prev_block.data.pop())
        # 合并到前一个块
        elif prev_block:
            prev_block.data.extend(block.data)
            prev_block.next = block.next
            # 更新尾节点
            if block == self.tail:
                self.tail = prev_block
        # 合并到下一个块（这种情况只可能是头块）
        elif block.next:
            block.next.data = block.data + block.next.data
            self.head = block.next
    
    def __getitem__(self, index):
        """
        支持通过索引访问元素，例如：list[index]
        """
        return self.get(index)
    
    def __setitem__(self, index, value):
        """
        支持通过索引设置元素，例如：list[index] = value
        """
        self.set(index, value)
    
    def __delitem__(self, index):
        """
        支持通过索引删除元素，例如：del list[index]
        """
        self.delete(index)
    
    def __len__(self):
        """
        返回链表长度，例如：len(list)
        """
        return self.size
    
    def __iter__(self):
        """
        支持迭代，例如：for item in list
        """
        current_block = self.head
        while current_block:
            for item in current_block.data:
                yield item
            current_block = current_block.next
    
    def __str__(self):
        """
        返回链表的字符串表示
        """
        return str(list(self))
    
    def __repr__(self):
        """
        返回链表的详细字符串表示
        """
        return f"UnrolledLinkedList(size={self.size}, block_size={self.block_size}, data={list(self)})"
    
    # 区间操作方法
    def insert_range(self, index, values):
        """
        在指定位置插入多个元素
        
        Args:
            index: 插入位置
            values: 要插入的值列表
        """
        for i, value in enumerate(values):
            self.insert(index + i, value)
    
    def delete_range(self, start, end):
        """
        删除指定范围的元素
        
        Args:
            start: 起始索引（包含）
            end: 结束索引（不包含）
        """
        for _ in range(start, end):
            self.delete(start)
    
    def get_range(self, start, end):
        """
        获取指定范围的元素
        
        Args:
            start: 起始索引（包含）
            end: 结束索引（不包含）
            
        Returns:
            list: 范围内的元素列表
        """
        result = []
        for i in range(start, end):
            result.append(self.get(i))
        return result

# 测试代码
def test_fibonacci_heap():
    """
    测试斐波那契堆的基本操作
    """
    print("=== 测试斐波那契堆 ===")
    
    # 创建斐波那契堆
    fib_heap = FibonacciHeap()
    
    # 测试插入
    print("\n测试插入操作:")
    nodes = []
    for i in range(10, 0, -1):
        node = fib_heap.insert(i, f"value_{i}")
        nodes.append(node)
        print(f"插入键值 {i}, 当前最小键值: {fib_heap.get_min().key if fib_heap.get_min() else None}")
    
    print(f"堆大小: {fib_heap.size()}")
    
    # 测试获取最小值
    print("\n测试获取最小值:")
    min_node = fib_heap.get_min()
    print(f"最小值节点: 键值={min_node.key}, 值={min_node.value}")
    
    # 测试减少键值
    print("\n测试减少键值:")
    fib_heap.decrease_key(nodes[0], 0)
    print(f"将键值10减少到0后，最小值: {fib_heap.get_min().key}")
    
    # 测试提取最小值
    print("\n测试提取最小值:")
    extracted = fib_heap.extract_min()
    print(f"提取的最小值: 键值={extracted.key}, 值={extracted.value}")
    print(f"提取后最小值: {fib_heap.get_min().key}")
    print(f"提取后堆大小: {fib_heap.size()}")
    
    # 测试删除操作
    print("\n测试删除操作:")
    node_to_delete = nodes[2]  # 键值8的节点
    print(f"删除键值为{node_to_delete.key}的节点")
    fib_heap.delete(node_to_delete)
    print(f"删除后堆大小: {fib_heap.size()}")
    
    # 测试合并堆
    print("\n测试合并堆:")
    fib_heap2 = FibonacciHeap()
    for i in range(100, 90, -1):
        fib_heap2.insert(i)
    
    fib_heap.merge(fib_heap2)
    print(f"合并后堆大小: {fib_heap.size()}")
    print(f"合并后最小值: {fib_heap.get_min().key}")
    
    # 测试批量提取最小值
    print("\n测试批量提取最小值:")
    for _ in range(5):
        min_node = fib_heap.extract_min()
        print(f"提取: {min_node.key}")
    
    print(f"最终堆大小: {fib_heap.size()}")

def test_unrolled_linked_list():
    """
    测试块状链表的基本操作
    """
    print("\n=== 测试块状链表 ===")
    
    # 创建块状链表，设置较小的块大小以便测试块分割和合并
    ull = UnrolledLinkedList(block_size=4)
    
    # 测试添加元素
    print("\n测试添加元素:")
    for i in range(1, 11):
        ull.append(i)
    print(f"添加1-10后: {ull}")
    print(f"链表大小: {len(ull)}")
    
    # 测试插入元素
    print("\n测试插入元素:")
    ull.insert(5, 99)
    print(f"在位置5插入99后: {ull}")
    ull.prepend(0)
    print(f"在头部插入0后: {ull}")
    
    # 测试访问和修改元素
    print("\n测试访问和修改元素:")
    print(f"位置6的值: {ull[6]}")
    ull[6] = 100
    print(f"修改位置6后: {ull}")
    
    # 测试删除元素
    print("\n测试删除元素:")
    deleted = ull.delete(3)
    print(f"删除位置3的元素({deleted})后: {ull}")
    del ull[0]  # 测试通过del语句删除
    print(f"删除位置0后: {ull}")
    
    # 测试迭代
    print("\n测试迭代:")
    print("迭代结果:", end=" ")
    for item in ull:
        print(item, end=" ")
    print()
    
    # 测试区间操作
    print("\n测试区间操作:")
    ull.insert_range(2, [200, 201, 202])
    print(f"插入范围后: {ull}")
    
    range_values = ull.get_range(3, 7)
    print(f"获取范围[3,7): {range_values}")
    
    ull.delete_range(1, 4)
    print(f"删除范围[1,4)后: {ull}")
    
    # 测试边界情况
    print("\n测试边界情况:")
    empty_ull = UnrolledLinkedList()
    print(f"空链表: {empty_ull}, 大小: {len(empty_ull)}")
    empty_ull.append(42)
    print(f"添加一个元素后: {empty_ull}")
    del empty_ull[0]
    print(f"删除唯一元素后: {empty_ull}, 是否为空: {empty_ull.is_empty()}")

if __name__ == "__main__":
    # 运行测试
    test_fibonacci_heap()
    test_unrolled_linked_list()

# ================================
# C++ 代码等效实现注释版 - 斐波那契堆
'''
#include <iostream>
#include <vector>
#include <cmath>
#include <stdexcept>

class FibonacciHeapNode {
publc:
    int key;
    void* value;
    int degree;
    bool mark;
    FibonacciHeapNode* parent;
    FibonacciHeapNode* child;
    FibonacciHeapNode* left;
    FibonacciHeapNode* right;
    
    FibonacciHeapNode(int k, void* v = nullptr) : 
        key(k), value(v), degree(0), mark(false),
        parent(nullptr), child(nullptr), left(this), right(this) {}
};

class FibonacciHeap {
private:
    FibonacciHeapNode* minNode;
    int nodeCount;
    
    void linkNodes(FibonacciHeapNode* left, FibonacciHeapNode* right) {
        left->right->left = right;
        right->right = left->right;
        left->right = right;
        right->left = left;
    }
    
    void linkAsChild(FibonacciHeapNode* childNode, FibonacciHeapNode* parentNode) {
        // 从根链表移除子节点
        childNode->left->right = childNode->right;
        childNode->right->left = childNode->left;
        
        // 重置子节点引用
        childNode->parent = parentNode;
        childNode->mark = false;
        
        // 添加到父节点的子节点列表
        if (parentNode->child == nullptr) {
            parentNode->child = childNode;
            childNode->left = childNode;
            childNode->right = childNode;
        } else {
            linkNodes(parentNode->child, childNode);
        }
        
        parentNode->degree++;
    }
    
    void cut(FibonacciHeapNode* node, FibonacciHeapNode* parent) {
        parent->degree--;
        
        if (parent->child == node && node->right == node) {
            parent->child = nullptr;
        } else {
            parent->child = node->right;
            node->left->right = node->right;
            node->right->left = node->left;
        }
        
        linkNodes(minNode, node);
        node->parent = nullptr;
        node->mark = false;
        
        if (node->key < minNode->key) {
            minNode = node;
        }
    }
    
    void cascadingCut(FibonacciHeapNode* node) {
        FibonacciHeapNode* parent = node->parent;
        if (parent != nullptr) {
            if (!node->mark) {
                node->mark = true;
            } else {
                cut(node, parent);
                cascadingCut(parent);
            }
        }
    }
    
    void consolidate() {
        if (minNode == nullptr) return;
        
        int maxDegree = log2(nodeCount) + 1;
        std::vector<FibonacciHeapNode*> degreeTable(maxDegree + 2, nullptr);
        
        // 收集所有根节点
        std::vector<FibonacciHeapNode*> roots;
        FibonacciHeapNode* current = minNode;
        do {
            roots.push_back(current);
            current = current->right;
        } while (current != minNode);
        
        for (auto root : roots) {
            int degree = root->degree;
            while (degreeTable[degree] != nullptr) {
                FibonacciHeapNode* other = degreeTable[degree];
                if (root->key > other->key) {
                    std::swap(root, other);
                }
                
                linkAsChild(other, root);
                degreeTable[degree] = nullptr;
                degree++;
            }
            degreeTable[degree] = root;
        }
        
        minNode = nullptr;
        for (auto node : degreeTable) {
            if (node != nullptr) {
                if (minNode == nullptr) {
                    minNode = node;
                    node->left = node;
                    node->right = node;
                } else {
                    linkNodes(minNode, node);
                    if (node->key < minNode->key) {
                        minNode = node;
                    }
                }
            }
        }
    }
    
public:
    FibonacciHeap() : minNode(nullptr), nodeCount(0) {}
    
    bool isEmpty() const {
        return minNode == nullptr;
    }
    
    FibonacciHeapNode* insert(int key, void* value = nullptr) {
        FibonacciHeapNode* newNode = new FibonacciHeapNode(key, value);
        
        if (minNode == nullptr) {
            minNode = newNode;
        } else {
            linkNodes(minNode, newNode);
            if (newNode->key < minNode->key) {
                minNode = newNode;
            }
        }
        
        nodeCount++;
        return newNode;
    }
    
    FibonacciHeapNode* extractMin() {
        if (isEmpty()) return nullptr;
        
        FibonacciHeapNode* minNode = this->minNode;
        
        // 处理子节点
        if (minNode->child != nullptr) {
            FibonacciHeapNode* child = minNode->child;
            do {
                FibonacciHeapNode* nextChild = child->right;
                child->parent = nullptr;
                linkNodes(minNode, child);
                child = nextChild;
            } while (child != minNode->child);
        }
        
        // 从根链表移除
        minNode->left->right = minNode->right;
        minNode->right->left = minNode->left;
        
        if (minNode == minNode->right) {
            this->minNode = nullptr;
        } else {
            this->minNode = minNode->right;
            consolidate();
        }
        
        nodeCount--;
        return minNode;
    }
    
    void decreaseKey(FibonacciHeapNode* node, int newKey) {
        if (newKey > node->key) {
            throw std::invalid_argument("New key must be smaller than current key");
        }
        
        node->key = newKey;
        FibonacciHeapNode* parent = node->parent;
        
        if (parent == nullptr || parent->key <= node->key) {
            if (node->key < minNode->key) {
                minNode = node;
            }
        } else {
            cut(node, parent);
            cascadingCut(parent);
        }
    }
    
    void deleteNode(FibonacciHeapNode* node) {
        decreaseKey(node, INT_MIN);
        extractMin();
    }
    
    void merge(FibonacciHeap& otherHeap) {
        if (otherHeap.isEmpty()) return;
        if (isEmpty()) {
            minNode = otherHeap.minNode;
            nodeCount = otherHeap.nodeCount;
            return;
        }
        
        linkNodes(minNode, otherHeap.minNode);
        if (otherHeap.minNode->key < minNode->key) {
            minNode = otherHeap.minNode;
        }
        
        nodeCount += otherHeap.nodeCount;
    }
    
    FibonacciHeapNode* getMin() const {
        return minNode;
    }
    
    int size() const {
        return nodeCount;
    }
    
    ~FibonacciHeap() {
        // 析构函数实现，释放所有节点内存
        while (!isEmpty()) {
            delete extractMin();
        }
    }
};
'''

# ================================
# Java 代码等效实现注释版 - 块状链表
'''
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Block {
    List<Object> data;
    int maxSize;
    Block next;
    
    Block(int maxSize) {
        this.data = new ArrayList<>();
        this.maxSize = maxSize;
        this.next = null;
    }
}

public class UnrolledLinkedList implements Iterable<Object> {
    private Block head;
    private Block tail;
    private int blockSize;
    private int size;
    
    public UnrolledLinkedList(int blockSize) {
        this.head = new Block(blockSize);
        this.tail = this.head;
        this.blockSize = blockSize;
        this.size = 0;
    }
    
    public UnrolledLinkedList() {
        this(16); // 默认块大小
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    private Pair<Block, Integer> getBlockAndIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        Block currentBlock = head;
        while (currentBlock.data.size() <= index) {
            index -= currentBlock.data.size();
            currentBlock = currentBlock.next;
        }
        
        return new Pair<>(currentBlock, index);
    }
    
    public Object get(int index) {
        Pair<Block, Integer> result = getBlockAndIndex(index);
        return result.first.data.get(result.second);
    }
    
    public void set(int index, Object value) {
        Pair<Block, Integer> result = getBlockAndIndex(index);
        result.first.data.set(result.second, value);
    }
    
    public void insert(int index, Object value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        if (isEmpty()) {
            head.data.add(value);
            size++;
            return;
        }
        
        Block currentBlock = head;
        Block prevBlock = null;
        int currentIndex = 0;
        
        while (currentBlock != null && currentIndex + currentBlock.data.size() <= index) {
            currentIndex += currentBlock.data.size();
            prevBlock = currentBlock;
            currentBlock = currentBlock.next;
        }
        
        Block targetBlock;
        int blockIndex;
        
        if (currentBlock == null) {
            targetBlock = new Block(blockSize);
            prevBlock.next = targetBlock;
            tail = targetBlock;
            blockIndex = 0;
        } else {
            targetBlock = currentBlock;
            blockIndex = index - currentIndex;
        }
        
        targetBlock.data.add(blockIndex, value);
        size++;
        
        if (targetBlock.data.size() > blockSize) {
            splitBlock(targetBlock);
        }
    }
    
    private void splitBlock(Block block) {
        Block newBlock = new Block(blockSize);
        int splitPoint = block.data.size() / 2;
        
        // 移动后半部分数据
        for (int i = splitPoint; i < block.data.size(); i++) {
            newBlock.data.add(block.data.get(i));
        }
        // 删除原块中的后半部分
        for (int i = block.data.size() - 1; i >= splitPoint; i--) {
            block.data.remove(i);
        }
        
        // 插入新块
        newBlock.next = block.next;
        block.next = newBlock;
        
        if (block == tail) {
            tail = newBlock;
        }
    }
    
    public void append(Object value) {
        if (tail.data.size() >= blockSize) {
            Block newBlock = new Block(blockSize);
            tail.next = newBlock;
            tail = newBlock;
        }
        tail.data.add(value);
        size++;
    }
    
    public void prepend(Object value) {
        if (head.data.size() >= blockSize) {
            Block newBlock = new Block(blockSize);
            newBlock.next = head;
            head = newBlock;
        }
        head.data.add(0, value);
        size++;
    }
    
    public Object delete(int index) {
        Pair<Block, Integer> result = getBlockAndIndex(index);
        Block block = result.first;
        int blockIndex = result.second;
        
        Object value = block.data.remove(blockIndex);
        size--;
        
        if (size == 0) {
            head.data.clear();
            tail = head;
            return value;
        }
        
        if (block != head && block.data.size() < blockSize / 2) {
            mergeOrRedistribute(block);
        }
        
        return value;
    }
    
    private void mergeOrRedistribute(Block block) {
        // 找到前一个块
        Block prevBlock = head;
        while (prevBlock != null && prevBlock.next != block) {
            prevBlock = prevBlock.next;
        }
        
        // 尝试从下一个块借
        if (block.next != null && block.next.data.size() > blockSize / 2) {
            block.data.add(block.next.data.remove(0));
        }
        // 尝试从前一个块借
        else if (prevBlock != null && prevBlock.data.size() > blockSize / 2) {
            block.data.add(0, prevBlock.data.remove(prevBlock.data.size() - 1));
        }
        // 合并到前一个块
        else if (prevBlock != null) {
            prevBlock.data.addAll(block.data);
            prevBlock.next = block.next;
            if (block == tail) {
                tail = prevBlock;
            }
        }
        // 合并到下一个块
        else if (block.next != null) {
            block.next.data.addAll(0, block.data);
            head = block.next;
        }
    }
    
    public void insertRange(int index, List<Object> values) {
        for (int i = 0; i < values.size(); i++) {
            insert(index + i, values.get(i));
        }
    }
    
    public List<Object> getRange(int start, int end) {
        List<Object> result = new ArrayList<>();
        for (int i = start; i < end; i++) {
            result.add(get(i));
        }
        return result;
    }
    
    public void deleteRange(int start, int end) {
        for (int i = 0; i < end - start; i++) {
            delete(start);
        }
    }
    
    public int size() {
        return size;
    }
    
    @Override
    public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            private Block currentBlock = head;
            private int currentIndex = 0;
            private int visited = 0;
            
            @Override
            public boolean hasNext() {
                return visited < size;
            }
            
            @Override
            public Object next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                
                while (currentIndex >= currentBlock.data.size()) {
                    currentBlock = currentBlock.next;
                    currentIndex = 0;
                }
                
                Object value = currentBlock.data.get(currentIndex);
                currentIndex++;
                visited++;
                return value;
            }
        };
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Object obj : this) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(obj);
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }
    
    // 辅助类
    private static class Pair<K, V> {
        K first;
        V second;
        
        Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }
    }
}
'''