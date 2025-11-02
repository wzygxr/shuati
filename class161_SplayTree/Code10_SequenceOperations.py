#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
题目：序列操作 (Sequence Operations)
来源：HDU 3436
网址：http://acm.hdu.edu.cn/showproblem.php?pid=3436

问题描述：
维护一个序列，支持以下操作：
1. TOP x: 将元素x移动到序列开头
2. QUERY x: 查询元素x在序列中的位置
3. RANK x: 查询序列中第x个位置的元素

时间复杂度：每个操作平均O(log n)
空间复杂度：O(n)

解题思路：
使用Splay树维护序列，每个节点存储子树大小用于快速定位
通过splay操作实现高效的区间操作和位置查询
"""

import sys

class SplayNode:
    def __init__(self, key):
        self.key = key      # 节点值
        self.size = 1       # 子树大小
        self.left = None    # 左子树
        self.right = None   # 右子树
        self.parent = None  # 父节点

class SplayTree:
    def __init__(self):
        self.root = None
        self.node_map = {}
    
    def maintain(self, x):
        """维护子树大小"""
        if x is not None:
            x.size = 1
            if x.left is not None:
                x.size += x.left.size
            if x.right is not None:
                x.size += x.right.size
    
    def left_rotate(self, x):
        """左旋操作"""
        y = x.right
        if y is not None:
            x.right = y.left
            if y.left is not None:
                y.left.parent = x
            y.parent = x.parent
        
        if x.parent is None:
            self.root = y
        elif x == x.parent.left:
            x.parent.left = y
        else:
            x.parent.right = y
        
        if y is not None:
            y.left = x
        x.parent = y
        
        self.maintain(x)
        self.maintain(y)
    
    def right_rotate(self, x):
        """右旋操作"""
        y = x.left
        if y is not None:
            x.left = y.right
            if y.right is not None:
                y.right.parent = x
            y.parent = x.parent
        
        if x.parent is None:
            self.root = y
        elif x == x.parent.left:
            x.parent.left = y
        else:
            x.parent.right = y
        
        if y is not None:
            y.right = x
        x.parent = y
        
        self.maintain(x)
        self.maintain(y)
    
    def splay(self, x):
        """Splay操作：将节点x旋转到根"""
        while x.parent is not None:
            if x.parent.parent is None:
                # 父节点是根节点
                if x == x.parent.left:
                    self.right_rotate(x.parent)
                else:
                    self.left_rotate(x.parent)
            else:
                parent = x.parent
                grand_parent = parent.parent
                
                if parent.left == x and grand_parent.left == parent:
                    # LL情况
                    self.right_rotate(grand_parent)
                    self.right_rotate(parent)
                elif parent.right == x and grand_parent.right == parent:
                    # RR情况
                    self.left_rotate(grand_parent)
                    self.left_rotate(parent)
                elif parent.left == x and grand_parent.right == parent:
                    # LR情况
                    self.right_rotate(parent)
                    self.left_rotate(grand_parent)
                else:
                    # RL情况
                    self.left_rotate(parent)
                    self.right_rotate(grand_parent)
    
    def insert(self, key):
        """插入节点"""
        new_node = SplayNode(key)
        self.node_map[key] = new_node
        
        if self.root is None:
            self.root = new_node
            return
        
        current = self.root
        parent = None
        
        while current is not None:
            parent = current
            if key < current.key:
                current = current.left
            else:
                current = current.right
        
        if key < parent.key:
            parent.left = new_node
        else:
            parent.right = new_node
        new_node.parent = parent
        
        self.splay(new_node)
    
    def find(self, key):
        """查找节点"""
        current = self.root
        while current is not None:
            if key == current.key:
                self.splay(current)
                return current
            elif key < current.key:
                current = current.left
            else:
                current = current.right
        return None
    
    def get_kth(self, k):
        """获取第k小的元素"""
        if self.root is None or k <= 0 or k > self.root.size:
            return None
        
        current = self.root
        while current is not None:
            left_size = current.left.size if current.left is not None else 0
            
            if k == left_size + 1:
                self.splay(current)
                return current
            elif k <= left_size:
                current = current.left
            else:
                k -= left_size + 1
                current = current.right
        return None
    
    def get_rank(self, x):
        """获取节点的排名"""
        if x is None:
            return -1
        self.splay(x)
        return (x.left.size + 1) if x.left is not None else 1
    
    def move_to_front(self, key):
        """将节点移动到开头"""
        node = self.find(key)
        if node is None:
            return
        
        # 如果已经是第一个节点，不需要移动
        if node.left is None:
            return
        
        # 分离左子树
        left_tree = node.left
        node.left = None
        left_tree.parent = None
        self.maintain(node)
        
        # 找到左子树的最大节点
        max_node = left_tree
        while max_node.right is not None:
            max_node = max_node.right
        self.splay(max_node)
        
        # 将原节点插入到左子树最大节点的右侧
        max_node.right = node
        node.parent = max_node
        self.maintain(max_node)
        
        self.root = max_node

def main():
    data = sys.stdin.read().split()
    idx = 0
    
    T = int(data[idx]); idx += 1
    
    for t in range(1, T + 1):
        n = int(data[idx]); idx += 1
        m = int(data[idx]); idx += 1
        
        # 初始化Splay树
        splay_tree = SplayTree()
        
        # 插入初始序列
        for i in range(1, n + 1):
            splay_tree.insert(i)
        
        print(f"Case {t}:")
        
        for _ in range(m):
            op = data[idx]; idx += 1
            x = int(data[idx]); idx += 1
            
            if op == "TOP":
                splay_tree.move_to_front(x)
            elif op == "QUERY":
                node = splay_tree.find(x)
                if node is not None:
                    print(splay_tree.get_rank(node))
            elif op == "RANK":
                kth_node = splay_tree.get_kth(x)
                if kth_node is not None:
                    print(kth_node.key)

if __name__ == "__main__":
    main()