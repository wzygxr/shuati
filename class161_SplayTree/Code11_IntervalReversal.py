#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
题目：区间翻转 (Interval Reversal)
来源：POJ 3580
网址：http://poj.org/problem?id=3580

问题描述：
维护一个序列，支持以下操作：
1. ADD x y D: 将区间[x,y]中的每个元素加上D
2. REVERSE x y: 将区间[x,y]翻转
3. REVOLVE x y T: 将区间[x,y]循环右移T次
4. INSERT x P: 在位置x后插入P
5. DELETE x: 删除位置x的元素
6. MIN x y: 查询区间[x,y]的最小值

时间复杂度：每个操作平均O(log n)
空间复杂度：O(n)

解题思路：
使用Splay树维护序列，每个节点存储子树大小、最小值、懒标记
通过splay操作实现高效的区间操作
"""

import sys

class SplayNode:
    def __init__(self, key):
        self.key = key          # 节点值
        self.size = 1           # 子树大小
        self.min_val = key       # 子树最小值
        self.add_lazy = 0       # 加法懒标记
        self.rev_lazy = False   # 翻转懒标记
        self.left = None         # 左子树
        self.right = None        # 右子树
        self.parent = None       # 父节点

class SplayTree:
    def __init__(self):
        self.root = None
        self.INF = 10**9
    
    def maintain(self, x):
        """维护子树信息"""
        if x is not None:
            x.size = 1
            x.min_val = x.key
            
            if x.left is not None:
                x.size += x.left.size
                x.min_val = min(x.min_val, x.left.min_val)
            if x.right is not None:
                x.size += x.right.size
                x.min_val = min(x.min_val, x.right.min_val)
    
    def push_down(self, x):
        """下传懒标记"""
        if x is not None:
            if x.add_lazy != 0:
                x.key += x.add_lazy
                if x.left is not None:
                    x.left.add_lazy += x.add_lazy
                    x.left.min_val += x.add_lazy
                if x.right is not None:
                    x.right.add_lazy += x.add_lazy
                    x.right.min_val += x.add_lazy
                x.add_lazy = 0
            
            if x.rev_lazy:
                x.left, x.right = x.right, x.left
                if x.left is not None:
                    x.left.rev_lazy = not x.left.rev_lazy
                if x.right is not None:
                    x.right.rev_lazy = not x.right.rev_lazy
                x.rev_lazy = False
    
    def left_rotate(self, x):
        """左旋操作"""
        y = x.right
        self.push_down(x)
        self.push_down(y)
        
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
        self.push_down(x)
        self.push_down(y)
        
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
        """Splay操作"""
        while x.parent is not None:
            if x.parent.parent is None:
                if x == x.parent.left:
                    self.right_rotate(x.parent)
                else:
                    self.left_rotate(x.parent)
            else:
                parent = x.parent
                grand_parent = parent.parent
                
                if parent.left == x and grand_parent.left == parent:
                    self.right_rotate(grand_parent)
                    self.right_rotate(parent)
                elif parent.right == x and grand_parent.right == parent:
                    self.left_rotate(grand_parent)
                    self.left_rotate(parent)
                elif parent.left == x and grand_parent.right == parent:
                    self.right_rotate(parent)
                    self.left_rotate(grand_parent)
                else:
                    self.left_rotate(parent)
                    self.right_rotate(grand_parent)
    
    def get_kth(self, k):
        """获取第k小的节点"""
        if self.root is None or k <= 0 or k > self.root.size:
            return None
        
        current = self.root
        while current is not None:
            self.push_down(current)
            left_size = current.left.size if current.left is not None else 0
            
            if k == left_size + 1:
                return current
            elif k <= left_size:
                current = current.left
            else:
                k -= left_size + 1
                current = current.right
        return None
    
    def split(self, l, r):
        """分割区间[l, r]"""
        if l > 1:
            left_part = self.get_kth(l - 1)
            self.splay(left_part)
            right_part = left_part.right
            left_part.right = None
            if right_part is not None:
                right_part.parent = None
            self.maintain(left_part)
            
            if right_part is not None:
                mid_part = self.get_kth(r - l + 1)
                self.splay(mid_part)
                remaining = mid_part.right
                mid_part.right = None
                if remaining is not None:
                    remaining.parent = None
                self.maintain(mid_part)
                
                return left_part, mid_part, remaining
        else:
            mid_part = self.get_kth(r)
            self.splay(mid_part)
            remaining = mid_part.right
            mid_part.right = None
            if remaining is not None:
                remaining.parent = None
            self.maintain(mid_part)
            
            return None, mid_part, remaining
        return None, None, None
    
    def merge(self, left, right):
        """合并子树"""
        if left is None:
            return right
        if right is None:
            return left
        
        max_node = left
        while max_node.right is not None:
            max_node = max_node.right
        self.splay(max_node)
        max_node.right = right
        right.parent = max_node
        self.maintain(max_node)
        
        return max_node
    
    def add_interval(self, l, r, d):
        """区间加法"""
        left, mid, right = self.split(l, r)
        if mid is not None:
            mid.add_lazy += d
            mid.min_val += d
        self.root = self.merge(self.merge(left, mid), right)
    
    def reverse_interval(self, l, r):
        """区间翻转"""
        left, mid, right = self.split(l, r)
        if mid is not None:
            mid.rev_lazy = not mid.rev_lazy
        self.root = self.merge(self.merge(left, mid), right)
    
    def revolve_interval(self, l, r, t):
        """区间循环右移"""
        length = r - l + 1
        t %= length
        if t == 0:
            return
        
        left, mid, right = self.split(l, r)
        if mid is not None:
            sub_left, sub_mid, sub_right = self.split(1, length - t)
            mid = self.merge(sub_right, sub_mid)
        self.root = self.merge(self.merge(left, mid), right)
    
    def insert(self, pos, val):
        """插入节点"""
        new_node = SplayNode(val)
        if pos == 0:
            if self.root is None:
                self.root = new_node
            else:
                min_node = self.root
                while min_node.left is not None:
                    min_node = min_node.left
                self.splay(min_node)
                min_node.left = new_node
                new_node.parent = min_node
                self.maintain(min_node)
        else:
            node = self.get_kth(pos)
            self.splay(node)
            new_node.right = node.right
            if node.right is not None:
                node.right.parent = new_node
            node.right = new_node
            new_node.parent = node
            self.maintain(new_node)
            self.maintain(node)
    
    def delete(self, pos):
        """删除节点"""
        node = self.get_kth(pos)
        self.splay(node)
        
        if node.left is None:
            self.root = node.right
            if self.root is not None:
                self.root.parent = None
        elif node.right is None:
            self.root = node.left
            if self.root is not None:
                self.root.parent = None
        else:
            left_tree = node.left
            left_tree.parent = None
            right_tree = node.right
            right_tree.parent = None
            
            max_node = left_tree
            while max_node.right is not None:
                max_node = max_node.right
            self.splay(max_node)
            max_node.right = right_tree
            right_tree.parent = max_node
            self.maintain(max_node)
            self.root = max_node
    
    def query_min(self, l, r):
        """查询区间最小值"""
        left, mid, right = self.split(l, r)
        min_val = mid.min_val if mid is not None else self.INF
        self.root = self.merge(self.merge(left, mid), right)
        return min_val

def main():
    data = sys.stdin.read().split()
    idx = 0
    
    n = int(data[idx]); idx += 1
    
    # 初始化序列
    splay_tree = SplayTree()
    for i in range(n):
        val = int(data[idx]); idx += 1
        splay_tree.insert(i, val)
    
    m = int(data[idx]); idx += 1
    
    for i in range(m):
        op = data[idx]; idx += 1
        
        if op == "ADD":
            x = int(data[idx]); idx += 1
            y = int(data[idx]); idx += 1
            d = int(data[idx]); idx += 1
            splay_tree.add_interval(x, y, d)
        elif op == "REVERSE":
            x = int(data[idx]); idx += 1
            y = int(data[idx]); idx += 1
            splay_tree.reverse_interval(x, y)
        elif op == "REVOLVE":
            x = int(data[idx]); idx += 1
            y = int(data[idx]); idx += 1
            t = int(data[idx]); idx += 1
            splay_tree.revolve_interval(x, y, t)
        elif op == "INSERT":
            pos = int(data[idx]); idx += 1
            val = int(data[idx]); idx += 1
            splay_tree.insert(pos, val)
        elif op == "DELETE":
            pos = int(data[idx]); idx += 1
            splay_tree.delete(pos)
        elif op == "MIN":
            x = int(data[idx]); idx += 1
            y = int(data[idx]); idx += 1
            print(splay_tree.query_min(x, y))

if __name__ == "__main__":
    main()