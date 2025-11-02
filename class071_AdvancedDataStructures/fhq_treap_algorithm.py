#!/usr/bin/env python3
"""
FHQ Treap算法实现 - Python版本

本文件实现了FHQ Treap数据结构，支持以下操作：
1. 插入、删除、查询
2. 区间翻转、区间加
3. 区间和、区间最小值、区间最大值查询
4. 动态顺序统计

时间复杂度：所有操作均为O(log n)
空间复杂度：O(n)
"""

import random
import sys

class Node:
    """FHQ Treap节点类"""
    
    def __init__(self, value):
        self.value = value
        self.priority = random.randint(0, 1000000)
        self.size = 1
        self.sum = value
        self.min = value
        self.max = value
        self.add = 0
        self.rev = False
        self.left = None
        self.right = None
    
    def push_down(self):
        """下传标记"""
        # 处理翻转标记
        if self.rev:
            self.left, self.right = self.right, self.left
            if self.left:
                self.left.rev ^= True
            if self.right:
                self.right.rev ^= True
            self.rev = False
        
        # 处理加法标记
        if self.add != 0:
            if self.left:
                self.left.value += self.add
                self.left.sum += self.add * self.left.size
                self.left.min += self.add
                self.left.max += self.add
                self.left.add += self.add
            if self.right:
                self.right.value += self.add
                self.right.sum += self.add * self.right.size
                self.right.min += self.add
                self.right.max += self.add
                self.right.add += self.add
            self.add = 0
    
    def push_up(self):
        """上传信息"""
        self.size = 1
        self.sum = self.value
        self.min = self.max = self.value
        
        if self.left:
            self.size += self.left.size
            self.sum += self.left.sum
            self.min = min(self.min, self.left.min)
            self.max = max(self.max, self.left.max)
        
        if self.right:
            self.size += self.right.size
            self.sum += self.right.sum
            self.min = min(self.min, self.right.min)
            self.max = max(self.max, self.right.max)


class ImplicitFHQTreap:
    """隐式键FHQ Treap实现"""
    
    def __init__(self):
        self.root = None
    
    def get_size(self, node):
        """获取节点子树大小"""
        return node.size if node else 0
    
    def split(self, root, k):
        """分裂操作"""
        if not root:
            return None, None
        
        root.push_down()
        left_size = self.get_size(root.left)
        
        if left_size + 1 <= k:
            l, r = self.split(root.right, k - left_size - 1)
            root.right = l
            root.push_up()
            return root, r
        else:
            l, r = self.split(root.left, k)
            root.left = r
            root.push_up()
            return l, root
    
    def merge(self, a, b):
        """合并操作"""
        if not a:
            return b
        if not b:
            return a
        
        if a.priority > b.priority:
            a.push_down()
            a.right = self.merge(a.right, b)
            a.push_up()
            return a
        else:
            b.push_down()
            b.left = self.merge(a, b.left)
            b.push_up()
            return b
    
    def insert(self, pos, value):
        """在指定位置插入值"""
        l, r = self.split(self.root, pos)
        node = Node(value)
        self.root = self.merge(self.merge(l, node), r)
    
    def delete(self, pos):
        """删除指定位置的值"""
        l, r = self.split(self.root, pos + 1)
        l1, r1 = self.split(l, pos)
        self.root = self.merge(l1, r)
    
    def reverse(self, l, r):
        """翻转区间 [l, r]"""
        l1, r1 = self.split(self.root, r + 1)
        l2, r2 = self.split(l1, l)
        
        if r2:
            r2.rev ^= True
        
        self.root = self.merge(self.merge(l2, r2), r1)
    
    def range_add(self, l, r, val):
        """区间加操作"""
        l1, r1 = self.split(self.root, r + 1)
        l2, r2 = self.split(l1, l)
        
        if r2:
            r2.value += val
            r2.sum += val * r2.size
            r2.min += val
            r2.max += val
            r2.add += val
        
        self.root = self.merge(self.merge(l2, r2), r1)
    
    def query_sum(self, l, r):
        """查询区间和"""
        l1, r1 = self.split(self.root, r + 1)
        l2, r2 = self.split(l1, l)
        result = r2.sum if r2 else 0
        self.root = self.merge(self.merge(l2, r2), r1)
        return result
    
    def query_min(self, l, r):
        """查询区间最小值"""
        l1, r1 = self.split(self.root, r + 1)
        l2, r2 = self.split(l1, l)
        result = r2.min if r2 else float('inf')
        self.root = self.merge(self.merge(l2, r2), r1)
        return result
    
    def query_max(self, l, r):
        """查询区间最大值"""
        l1, r1 = self.split(self.root, r + 1)
        l2, r2 = self.split(l1, l)
        result = r2.max if r2 else float('-inf')
        self.root = self.merge(self.merge(l2, r2), r1)
        return result
    
    def size(self):
        """获取树的大小"""
        return self.get_size(self.root)
    
    def inorder_traversal(self):
        """中序遍历，用于调试"""
        result = []
        self._inorder_traversal(self.root, result)
        print(" ".join(map(str, result)))
    
    def _inorder_traversal(self, node, result):
        if not node:
            return
        
        node.push_down()
        self._inorder_traversal(node.left, result)
        result.append(node.value)
        self._inorder_traversal(node.right, result)


class DynamicOrderStatistics:
    """动态顺序统计类"""
    
    def __init__(self):
        self.treap = ImplicitFHQTreap()
    
    def insert(self, pos, value):
        """插入元素"""
        self.treap.insert(pos, value)
    
    def delete(self, pos):
        """删除元素"""
        self.treap.delete(pos)
    
    def query_sum(self, l, r):
        """查询区间和"""
        return self.treap.query_sum(l, r)
    
    def query_min(self, l, r):
        """查询区间最小值"""
        return self.treap.query_min(l, r)
    
    def query_max(self, l, r):
        """查询区间最大值"""
        return self.treap.query_max(l, r)
    
    def reverse(self, l, r):
        """翻转区间"""
        self.treap.reverse(l, r)
    
    def range_add(self, l, r, val):
        """区间加"""
        self.treap.range_add(l, r, val)
    
    def print_sequence(self):
        """打印序列"""
        self.treap.inorder_traversal()


def test_fhq_treap():
    """测试函数"""
    print("=== 测试FHQ Treap算法 ===")
    
    # 测试动态顺序统计
    print("\n=== 测试动态顺序统计 ===")
    dos = DynamicOrderStatistics()
    
    # 插入元素
    dos.insert(0, 1)
    dos.insert(1, 3)
    dos.insert(2, 5)
    dos.insert(3, 7)
    dos.insert(4, 9)
    
    print("初始序列: ", end="")
    dos.print_sequence()
    
    print(f"序列和 [0,4]: {dos.query_sum(0, 4)}")
    print(f"序列最小值 [0,4]: {dos.query_min(0, 4)}")
    print(f"序列最大值 [0,4]: {dos.query_max(0, 4)}")
    
    # 测试区间操作
    print("\n=== 测试区间操作 ===")
    dos.range_add(1, 3, 10)
    print("区间[1,3]加10后序列: ", end="")
    dos.print_sequence()
    
    print(f"区间[1,3]加10后序列和: {dos.query_sum(0, 4)}")
    
    # 测试翻转操作
    print("\n=== 测试翻转操作 ===")
    dos.reverse(1, 3)
    print("翻转区间[1,3]后序列: ", end="")
    dos.print_sequence()
    
    # 测试删除操作
    print("\n=== 测试删除操作 ===")
    dos.delete(2)
    print("删除位置2后序列: ", end="")
    dos.print_sequence()
    
    print(f"删除后序列和 [0,3]: {dos.query_sum(0, 3)}")


if __name__ == "__main__":
    test_fhq_treap()