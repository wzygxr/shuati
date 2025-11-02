#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
块状链表实现 (Python版本)

算法思路：
块状链表是一种结合了数组和链表优点的数据结构。它将元素分块存储在节点中，
每个节点包含一个固定大小的数组和指向下一个节点的指针。

应用场景：
1. 大型序列维护：文本编辑器、数据库索引
2. 数组操作优化：批量更新处理
3. 文件系统：大文件的分块管理

时间复杂度：
- 插入/删除：O(n/b) 均摊，其中b是块大小
- 查找：O(n/b)
- 空间复杂度：O(n)
"""

class Block:
    """块状链表的块类"""
    
    def __init__(self, capacity):
        """
        构造函数
        :param capacity: 块的最大容量
        """
        self.capacity = capacity      # 块的最大容量
        self.array = [None] * capacity  # 块内的数组
        self.size = 0                 # 当前块中元素的数量
        self.next = None              # 指向下一个块
        self.prev = None              # 指向上一个块
    
    def is_full(self):
        """
        检查块是否已满
        :return: 块是否已满
        """
        return self.size == self.capacity
    
    def is_empty(self):
        """
        检查块是否为空
        :return: 块是否为空
        """
        return self.size == 0
    
    def get_size(self):
        """
        获取块的大小
        :return: 块中元素的数量
        """
        return self.size
    
    def get_capacity(self):
        """
        获取块的容量
        :return: 块的最大容量
        """
        return self.capacity
    
    def add(self, value):
        """
        在块的末尾添加元素
        :param value: 要添加的值
        :raises RuntimeError: 如果块已满
        """
        if self.is_full():
            raise RuntimeError("Block is full")
        self.array[self.size] = value
        self.size += 1
    
    def insert(self, index, value):
        """
        在指定位置插入元素
        :param index: 插入位置
        :param value: 要插入的值
        :raises IndexError: 如果索引无效
        :raises RuntimeError: 如果块已满
        """
        if self.is_full():
            raise RuntimeError("Block is full")
        
        if index < 0 or index > self.size:
            raise IndexError(f"Index out of bounds: {index}")
        
        # 移动元素为新元素腾出空间
        for i in range(self.size, index, -1):
            self.array[i] = self.array[i - 1]
        self.array[index] = value
        self.size += 1
    
    def delete(self, index):
        """
        删除指定位置的元素
        :param index: 要删除的元素位置
        :return: 被删除的元素
        :raises IndexError: 如果索引无效
        """
        if index < 0 or index >= self.size:
            raise IndexError(f"Index out of bounds: {index}")
        
        value = self.array[index]
        
        # 移动元素覆盖被删除的元素
        for i in range(index, self.size - 1):
            self.array[i] = self.array[i + 1]
        self.array[self.size - 1] = None  # 清除引用
        self.size -= 1
        
        return value
    
    def get(self, index):
        """
        获取指定位置的元素
        :param index: 元素位置
        :return: 元素值
        :raises IndexError: 如果索引无效
        """
        if index < 0 or index >= self.size:
            raise IndexError(f"Index out of bounds: {index}")
        return self.array[index]
    
    def set(self, index, value):
        """
        设置指定位置的元素值
        :param index: 元素位置
        :param value: 新的元素值
        :return: 原来的元素值
        :raises IndexError: 如果索引无效
        """
        if index < 0 or index >= self.size:
            raise IndexError(f"Index out of bounds: {index}")
        
        old_value = self.array[index]
        self.array[index] = value
        return old_value
    
    def split(self, split_index):
        """
        分割块
        将当前块从指定位置分割，返回包含后半部分元素的新块
        :param split_index: 分割位置
        :return: 包含后半部分元素的新块
        :raises IndexError: 如果分割位置无效
        """
        if split_index < 0 or split_index > self.size:
            raise IndexError(f"Split index out of bounds: {split_index}")
        
        # 创建新块
        new_block = Block(self.capacity)
        
        # 复制后半部分元素到新块
        elements_to_move = self.size - split_index
        for i in range(elements_to_move):
            new_block.array[i] = self.array[split_index + i]
            self.array[split_index + i] = None  # 清除引用
        
        # 更新块大小
        new_block.size = elements_to_move
        self.size = split_index
        
        # 建立双向链接
        new_block.next = self.next
        if self.next is not None:
            self.next.prev = new_block
        self.next = new_block
        new_block.prev = self
        
        return new_block
    
    def merge_next(self):
        """
        合并两个相邻块
        假设当前块和next块是相邻的
        :return: 合并后的块（即当前块）
        :raises RuntimeError: 如果没有下一个块或合并后超出容量
        """
        if self.next is None:
            raise RuntimeError("No next block to merge")
        
        if self.size + self.next.size > self.capacity:
            raise RuntimeError("Merged size exceeds block capacity")
        
        # 复制next块的元素到当前块
        for i in range(self.next.size):
            self.array[self.size + i] = self.next.array[i]
            self.next.array[i] = None  # 清除引用
        self.size += self.next.size
        
        # 更新链接，跳过next块
        next_next = self.next.next
        self.next = next_next
        if next_next is not None:
            next_next.prev = self
        
        return self

class UnrolledLinkedList:
    """块状链表实现"""
    
    def __init__(self, block_capacity=16):
        """
        构造函数
        :param block_capacity: 块的最大容量
        :raises ValueError: 如果块容量小于2
        """
        if block_capacity < 2:
            raise ValueError("Block capacity must be at least 2")
        
        self.block_capacity = block_capacity  # 块的最大容量
        self.head = None                      # 头块指针
        self.tail = None                      # 尾块指针
        self.size = 0                         # 链表元素总数
    
    def is_empty(self):
        """
        检查链表是否为空
        :return: 链表是否为空
        """
        return self.size == 0
    
    def get_size(self):
        """
        获取链表中元素的数量
        :return: 元素数量
        """
        return self.size
    
    def add(self, value):
        """
        在链表末尾添加元素
        时间复杂度：O(n/b) 均摊，其中b是块容量
        :param value: 要添加的值
        """
        if self.is_empty():
            # 空链表，创建第一个块
            self.head = Block(self.block_capacity)
            self.tail = self.head
            self.head.add(value)
        else:
            # 非空链表，检查尾块是否已满
            if self.tail.is_full():
                # 尾块已满，分割为两个半满的块
                self.tail.split(self.tail.get_size() // 2)
                self.tail = self.tail.next  # 更新尾块指针
            self.tail.add(value)
        self.size += 1
    
    def insert(self, index, value):
        """
        在指定位置插入元素
        时间复杂度：O(n/b)
        :param index: 插入位置
        :param value: 要插入的值
        :raises IndexError: 如果索引无效
        """
        if index < 0 or index > self.size:
            raise IndexError(f"Index out of bounds: {index}")
        
        if index == self.size:
            # 在末尾插入，调用add方法
            self.add(value)
            return
        
        if self.is_empty():
            # 空链表，创建第一个块
            self.head = Block(self.block_capacity)
            self.tail = self.head
            self.head.add(value)
        else:
            # 定位到包含插入位置的块和块内索引
            pos = self._find_block_and_index(index)
            block = pos[0]
            block_index = pos[1]
            
            # 检查块是否已满
            if block.is_full():
                # 块已满，分割为两个半满的块
                split_index = block.get_size() // 2
                new_block = block.split(split_index)
                
                # 调整插入位置
                if block_index >= split_index:
                    block = new_block
                    block_index -= split_index
            
            # 在块中插入元素
            block.insert(block_index, value)
            
            # 更新尾块指针（如果需要）
            current = self.head
            while current.next is not None:
                current = current.next
            self.tail = current
        
        self.size += 1
    
    def delete(self, index):
        """
        删除指定位置的元素
        时间复杂度：O(n/b)
        :param index: 要删除的元素位置
        :return: 被删除的元素
        :raises IndexError: 如果索引无效
        """
        if self.is_empty():
            raise RuntimeError("Cannot delete from empty list")
        
        if index < 0 or index >= self.size:
            raise IndexError(f"Index out of bounds: {index}")
        
        # 定位到包含删除位置的块和块内索引
        pos = self._find_block_and_index(index)
        block = pos[0]
        block_index = pos[1]
        
        # 保存要删除的元素值
        value = block.delete(block_index)
        
        # 如果删除后块的大小过小，尝试与相邻块合并（保持块的大小在合理范围）
        if block.get_size() < self.block_capacity // 4 and block != self.head or \
           block.is_empty() and self.size > 0:  # 特殊处理空块
            
            # 优先与前一个块合并
            if block.prev is not None:
                prev_block = block.prev
                # 确保合并后不会超出容量
                if prev_block.get_size() + block.get_size() <= self.block_capacity:
                    # 将要删除的索引调整为前一个块的末尾
                    if block == self.tail:
                        self.tail = prev_block
                    prev_block.merge_next()
                    # 如果当前删除的块是head，更新head指针
                    if block == self.head:
                        self.head = prev_block
            # 否则与后一个块合并
            elif block.next is not None:
                next_block = block.next
                if block.get_size() + next_block.get_size() <= self.block_capacity:
                    if next_block == self.tail:
                        self.tail = block
                    block.merge_next()
            # 特殊情况：只剩一个空块
            elif block.is_empty():
                self.head = None
                self.tail = None
        
        self.size -= 1
        return value
    
    def get(self, index):
        """
        获取指定位置的元素
        时间复杂度：O(n/b)
        :param index: 元素位置
        :return: 元素值
        :raises IndexError: 如果索引无效
        """
        if self.is_empty():
            raise RuntimeError("List is empty")
        
        if index < 0 or index >= self.size:
            raise IndexError(f"Index out of bounds: {index}")
        
        pos = self._find_block_and_index(index)
        return pos[0].get(pos[1])
    
    def set(self, index, value):
        """
        设置指定位置的元素值
        时间复杂度：O(n/b)
        :param index: 元素位置
        :param value: 新的元素值
        :return: 原来的元素值
        :raises IndexError: 如果索引无效
        """
        if self.is_empty():
            raise RuntimeError("List is empty")
        
        if index < 0 or index >= self.size:
            raise IndexError(f"Index out of bounds: {index}")
        
        pos = self._find_block_and_index(index)
        return pos[0].set(pos[1], value)
    
    def clear(self):
        """
        清空链表
        时间复杂度：O(n)
        """
        self.head = None
        self.tail = None
        self.size = 0
    
    def to_array(self):
        """
        将链表内容转换为数组
        时间复杂度：O(n)
        :return: 包含链表所有元素的数组
        """
        if self.is_empty():
            return []
        
        result = []
        current = self.head
        
        while current is not None:
            for i in range(current.get_size()):
                result.append(current.get(i))
            current = current.next
        
        return result
    
    def index_of(self, value):
        """
        查找第一个出现的指定值的索引
        时间复杂度：O(n)
        :param value: 要查找的值
        :return: 元素索引，如果未找到返回-1
        """
        if self.is_empty():
            return -1
        
        index = 0
        current = self.head
        
        while current is not None:
            for i in range(current.get_size()):
                if current.get(i) == value:
                    return index + i
            index += current.get_size()
            current = current.next
        
        return -1
    
    def last_index_of(self, value):
        """
        查找最后一个出现的指定值的索引
        时间复杂度：O(n)
        :param value: 要查找的值
        :return: 元素索引，如果未找到返回-1
        """
        if self.is_empty():
            return -1
        
        index = self.size - 1
        current = self.tail
        current_block_size = current.get_size()
        
        while current is not None:
            for i in range(current_block_size - 1, -1, -1):
                if current.get(i) == value:
                    return index - (current_block_size - 1 - i)
            
            index -= current_block_size
            current = current.prev
            current_block_size = current.get_size() if current is not None else 0
        
        return -1
    
    def contains(self, value):
        """
        检查链表是否包含指定值
        时间复杂度：O(n)
        :param value: 要检查的值
        :return: 是否包含该值
        """
        return self.index_of(value) != -1
    
    def sub_list(self, start, end):
        """
        范围查询：获取从start到end（不包含）的子列表
        时间复杂度：O(n/b + k)，其中k是子列表的大小
        :param start: 起始索引（包含）
        :param end: 结束索引（不包含）
        :return: 子列表
        :raises IndexError: 如果索引无效
        """
        if start < 0 or end > self.size or start > end:
            raise IndexError(f"Invalid range: [{start}, {end})")
        
        sublist = UnrolledLinkedList(self.block_capacity)
        
        if start == end:
            return sublist  # 空的子列表
        
        # 处理跨越多个块的情况
        current_index = start
        while current_index < end:
            sublist.add(self.get(current_index))
            current_index += 1
        
        return sublist
    
    def print_list(self):
        """
        打印链表内容
        时间复杂度：O(n)
        """
        if self.is_empty():
            print("List is empty")
            return
        
        print("UnrolledLinkedList: [", end="")
        current = self.head
        first_element = True
        
        while current is not None:
            for i in range(current.get_size()):
                if not first_element:
                    print(", ", end="")
                else:
                    first_element = False
                print(current.get(i), end="")
            current = current.next
        
        print("]")
    
    def print_block_structure(self):
        """
        打印链表的块结构（用于调试）
        """
        if self.is_empty():
            print("List is empty")
            return
        
        print("Block Structure:")
        block_index = 0
        current = self.head
        
        while current is not None:
            print(f"Block {block_index} (size={current.get_size()}): [", end="")
            
            for i in range(current.get_size()):
                print(current.get(i), end="")
                if i < current.get_size() - 1:
                    print(", ", end="")
            
            print("]")
            
            current = current.next
            block_index += 1
    
    # ==================== 内部辅助方法 ====================
    
    def _find_block_and_index(self, index):
        """
        查找包含指定索引的块和块内索引
        时间复杂度：O(n/b)
        :param index: 元素索引
        :return: 包含块和块内索引的元组
        """
        if self.is_empty() or index < 0 or index >= self.size:
            raise IndexError(f"Index out of bounds: {index}")
        
        # 优化：根据索引位置选择从头还是从尾开始查找
        # 如果索引更靠近头部，从头开始
        if index < self.size / 2:
            current = self.head
            current_index = 0
            
            while current is not None:
                if index < current_index + current.get_size():
                    # 找到了包含索引的块
                    return (current, index - current_index)
                current_index += current.get_size()
                current = current.next
        # 否则从尾开始
        else:
            current = self.tail
            current_index = self.size - 1
            current_block_size = current.get_size()
            
            while current is not None:
                if index >= current_index - current_block_size + 1:
                    # 找到了包含索引的块
                    return (current, index - (current_index - current_block_size + 1))
                current_index -= current_block_size
                current = current.prev
                current_block_size = current.get_size() if current is not None else 0
        
        # 不应该到达这里
        raise IndexError(f"Index not found: {index}")
    
    @staticmethod
    def test_unrolled_linked_list():
        """测试块状链表的各种操作"""
        print("=== 测试块状链表 ===")
        # 使用较小的块容量以便更容易观察块分割和合并
        list_obj = UnrolledLinkedList(4)
        
        # 测试添加操作
        print("\n1. 测试添加操作:")
        for i in range(1, 11):
            list_obj.add(i)
        print("添加1-10后的列表:")
        list_obj.print_list()
        list_obj.print_block_structure()
        
        # 测试获取和设置
        print("\n2. 测试获取和设置操作:")
        print("索引5的值:", list_obj.get(5))  # 应该是 6
        old_value = list_obj.set(5, 100)
        print("设置索引5的值为100，旧值:", old_value)
        print("索引5的新值:", list_obj.get(5))  # 应该是 100
        list_obj.print_list()
        
        # 测试插入操作
        print("\n3. 测试插入操作:")
        list_obj.insert(3, 50)  # 在索引3插入50
        print("在索引3插入50后:")
        list_obj.print_list()
        list_obj.print_block_structure()
        
        list_obj.insert(0, 0)  # 在头部插入0
        print("在头部插入0后:")
        list_obj.print_list()
        
        # 测试删除操作
        print("\n4. 测试删除操作:")
        deleted_value = list_obj.delete(5)  # 删除索引5的值
        print("删除索引5的值:", deleted_value)
        print("删除后:")
        list_obj.print_list()
        list_obj.print_block_structure()
        
        list_obj.delete(0)  # 删除头部
        print("删除头部后:")
        list_obj.print_list()
        
        # 测试查找操作
        print("\n5. 测试查找操作:")
        print("值为100的索引:", list_obj.index_of(100))
        print("值为99的索引:", list_obj.index_of(99))  # 应该是 -1
        print("列表是否包含50:", list_obj.contains(50))
        
        # 测试子列表
        print("\n6. 测试子列表:")
        sublist = list_obj.sub_list(2, 6)
        print("子列表[2,6):")
        sublist.print_list()
        
        # 测试清空操作
        print("\n7. 测试清空操作:")
        list_obj.clear()
        print("清空后:")
        list_obj.print_list()
        print("列表大小:", list_obj.get_size())
        
        # 测试边界情况
        print("\n8. 测试边界情况:")
        try:
            list_obj.get(0)  # 空列表访问
        except Exception as e:
            print("空列表访问异常:", e)
        
        list_obj.add(1)  # 添加一个元素
        list_obj.add(2)  # 添加第二个元素
        print("添加两个元素后:")
        list_obj.print_list()
        
        list_obj.delete(0)  # 删除第一个元素
        list_obj.delete(0)  # 删除第二个元素
        print("删除所有元素后:")
        list_obj.print_list()
        
        # 性能测试
        print("\n=== 性能测试 ===")
        import time
        
        # 测试大量插入操作
        large_list = UnrolledLinkedList(16)
        
        start_time = time.time()
        for i in range(10000):
            large_list.add(i)
        insert_time = time.time() - start_time
        
        print(f"插入10000个元素时间: {insert_time*1000:.2f} ms")
        print(f"链表大小: {large_list.get_size()}")
        
        # 测试随机访问
        start_time = time.time()
        for i in range(1000):
            large_list.get(i * 10)
        access_time = time.time() - start_time
        
        print(f"1000次随机访问时间: {access_time*1000:.2f} ms")

if __name__ == "__main__":
    UnrolledLinkedList.test_unrolled_linked_list()