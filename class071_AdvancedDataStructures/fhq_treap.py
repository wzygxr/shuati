#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
FHQ-Treap (无旋Treap) 实现
支持操作：
- 序列维护
- 区间翻转
- 区间加标记
- 区间查询（求和、最值等）

时间复杂度：所有操作均为 O(log n) 均摊
空间复杂度：O(n)

设计要点：
1. 利用随机优先级维护树的平衡
2. 支持split和merge两个核心操作
3. 延迟标记处理区间操作
4. 工程化考量：异常处理、边界检查

典型应用场景：
- 序列区间操作
- 动态维护有序序列
- 需要支持分裂和合并的场景
"""

import random
from typing import List, Tuple, Optional

class FHQTreap:
    def __init__(self):
        """
        初始化FHQ Treap
        """
        self.root = None
        self.size = 0
        
    class Node:
        def __init__(self, key: int):
            """
            节点类
            
            Args:
                key (int): 节点值
            """
            self.key = key
            self.priority = random.randint(0, 2**31 - 1)  # 随机优先级
            self.size = 1  # 子树大小
            self.sum = key  # 子树和
            self.min_val = key  # 子树最小值
            self.max_val = key  # 子树最大值
            self.add = 0  # 加法标记
            self.rev = False  # 翻转标记
            self.left: Optional['FHQTreap.Node'] = None  # 左子树
            self.right: Optional['FHQTreap.Node'] = None  # 右子树
            
        def push_down(self) -> None:
            """
            下传标记
            """
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
                    self.left.key += self.add
                    self.left.sum += self.add * self.left.size
                    self.left.min_val += self.add
                    self.left.max_val += self.add
                    self.left.add += self.add
                    
                if self.right:
                    self.right.key += self.add
                    self.right.sum += self.add * self.right.size
                    self.right.min_val += self.add
                    self.right.max_val += self.add
                    self.right.add += self.add
                    
                self.add = 0
                
        def push_up(self) -> None:
            """
            上传信息
            """
            self.size = 1
            self.sum = self.key
            self.min_val = self.key
            self.max_val = self.key
            
            if self.left:
                self.size += self.left.size
                self.sum += self.left.sum
                self.min_val = min(self.min_val, self.left.min_val)
                self.max_val = max(self.max_val, self.left.max_val)
                
            if self.right:
                self.size += self.right.size
                self.sum += self.right.sum
                self.min_val = min(self.min_val, self.right.min_val)
                self.max_val = max(self.max_val, self.right.max_val)
    
    def _get_size(self, node: Optional['Node']) -> int:
        """
        获取节点的子树大小
        
        Args:
            node (Optional[Node]): 节点
            
        Returns:
            int: 子树大小
        """
        return node.size if node else 0
    
    def _split(self, root: Optional['Node'], k: int) -> Tuple[Optional['Node'], Optional['Node']]:
        """
        分裂操作：将树按大小分裂为两部分
        
        Args:
            root (Optional[Node]): 当前根节点
            k (int): 左树的大小
            
        Returns:
            Tuple[Optional[Node], Optional[Node]]: 分裂结果 [左树, 右树]
        """
        if not root:
            return None, None
            
        root.push_down()
        left_size = self._get_size(root.left)
        
        if left_size + 1 <= k:
            # 根节点及其左子树属于左树
            right1, right2 = self._split(root.right, k - left_size - 1)
            root.right = right1
            root.push_up()
            return root, right2  # type: ignore
        else:
            # 根节点及其右子树属于右树
            left1, left2 = self._split(root.left, k)
            root.left = left2
            root.push_up()
            return left1, root  # type: ignore
    
    def _merge(self, a: Optional['Node'], b: Optional['Node']) -> Optional['Node']:
        """
        合并操作：合并两棵树
        
        Args:
            a (Optional[Node]): 左树
            b (Optional[Node]): 右树
            
        Returns:
            Optional[Node]: 合并后的根节点
        """
        if not a:
            return b
        if not b:
            return a
            
        # 确保a的优先级高于b，维护Treap性质
        if a.priority > b.priority:
            a.push_down()
            a.right = self._merge(a.right, b)
            a.push_up()
            return a  # type: ignore
        else:
            b.push_down()
            b.left = self._merge(a, b.left)
            b.push_up()
            return b  # type: ignore
    
    def insert(self, pos: int, key: int) -> None:
        """
        插入节点到指定位置
        
        Args:
            pos (int): 位置（从0开始）
            key (int): 节点值
            
        Raises:
            ValueError: 位置超出范围
        """
        if pos < 0 or pos > self.size:
            raise ValueError("Position out of bounds")
            
        left, right = self._split(self.root, pos)
        self.root = self._merge(self._merge(left, self.Node(key)), right)
        self.size += 1
    
    def delete(self, pos: int) -> None:
        """
        删除指定位置的节点
        
        Args:
            pos (int): 位置（从0开始）
            
        Raises:
            ValueError: 位置超出范围
        """
        if pos < 0 or pos >= self.size:
            raise ValueError("Position out of bounds")
            
        left, mid_right = self._split(self.root, pos + 1)
        left2, mid = self._split(left, pos)
        self.root = self._merge(left2, mid_right)
        self.size -= 1
    
    def reverse(self, l: int, r: int) -> None:
        """
        翻转区间 [l, r]
        
        Args:
            l (int): 左边界（从0开始）
            r (int): 右边界（从0开始，包含）
            
        Raises:
            ValueError: 区间无效
        """
        if l < 0 or r >= self.size or l > r:
            raise ValueError("Invalid range")
            
        left, mid_right = self._split(self.root, r + 1)
        left2, mid = self._split(left, l)
        
        if mid:
            mid.rev ^= True
            
        self.root = self._merge(self._merge(left2, mid), mid_right)
    
    def range_add(self, l: int, r: int, val: int) -> None:
        """
        区间加操作
        
        Args:
            l (int): 左边界（从0开始）
            r (int): 右边界（从0开始，包含）
            val (int): 要加的值
            
        Raises:
            ValueError: 区间无效
        """
        if l < 0 or r >= self.size or l > r:
            raise ValueError("Invalid range")
            
        left, mid_right = self._split(self.root, r + 1)
        left2, mid = self._split(left, l)
        
        if mid:
            mid.key += val
            mid.sum += val * mid.size
            mid.min_val += val
            mid.max_val += val
            mid.add += val
            
        self.root = self._merge(self._merge(left2, mid), mid_right)
    
    def query_sum(self, l: int, r: int) -> int:
        """
        查询区间和
        
        Args:
            l (int): 左边界（从0开始）
            r (int): 右边界（从0开始，包含）
            
        Returns:
            int: 区间和
            
        Raises:
            ValueError: 区间无效
        """
        if l < 0 or r >= self.size or l > r:
            raise ValueError("Invalid range")
            
        left, mid_right = self._split(self.root, r + 1)
        left2, mid = self._split(left, l)
        
        sum_val = mid.sum if mid else 0
        
        self.root = self._merge(self._merge(left2, mid), mid_right)
        return sum_val
    
    def query_min(self, l: int, r: int) -> int:
        """
        查询区间最小值
        
        Args:
            l (int): 左边界（从0开始）
            r (int): 右边界（从0开始，包含）
            
        Returns:
            int: 区间最小值
            
        Raises:
            ValueError: 区间无效
        """
        if l < 0 or r >= self.size or l > r:
            raise ValueError("Invalid range")
            
        left, mid_right = self._split(self.root, r + 1)
        left2, mid = self._split(left, l)
        
        min_val = mid.min_val if mid else float('inf')
        
        self.root = self._merge(self._merge(left2, mid), mid_right)
        return int(min_val) if min_val != float('inf') else float('inf')  # type: ignore
    
    def query_max(self, l: int, r: int) -> int:
        """
        查询区间最大值
        
        Args:
            l (int): 左边界（从0开始）
            r (int): 右边界（从0开始，包含）
            
        Returns:
            int: 区间最大值
            
        Raises:
            ValueError: 区间无效
        """
        if l < 0 or r >= self.size or l > r:
            raise ValueError("Invalid range")
            
        left, mid_right = self._split(self.root, r + 1)
        left2, mid = self._split(left, l)
        
        max_val = mid.max_val if mid else float('-inf')
        
        self.root = self._merge(self._merge(left2, mid), mid_right)
        return int(max_val) if max_val != float('-inf') else float('-inf')  # type: ignore
    
    def get_size(self) -> int:
        """
        获取树的大小
        
        Returns:
            int: 树的大小
        """
        return self.size
    
    def _inorder_traversal(self, node: Optional['Node']) -> List[int]:
        """
        中序遍历，用于调试
        
        Args:
            node (Optional[Node]): 节点
            
        Returns:
            List[int]: 遍历结果
        """
        if not node:
            return []
            
        node.push_down()
        result = []
        result.extend(self._inorder_traversal(node.left))
        result.append(node.key)
        result.extend(self._inorder_traversal(node.right))
        return result
    
    def to_list(self) -> List[int]:
        """
        将树转换为列表（中序遍历）
        
        Returns:
            List[int]: 树中元素的列表
        """
        return self._inorder_traversal(self.root)


# 测试函数
if __name__ == "__main__":
    # 测试用例1：基本功能测试
    treap = FHQTreap()
    
    # 测试插入
    for i in range(10):
        treap.insert(i, i + 1)
    
    print("初始序列:", treap.to_list())  # 应该是 [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    
    # 测试区间翻转
    treap.reverse(2, 7)
    print("翻转区间[2,7]:", treap.to_list())  # 应该是 [1, 2, 8, 7, 6, 5, 4, 3, 9, 10]
    
    # 测试区间加
    treap.range_add(3, 6, 10)
    print("区间[3,6]加10:", treap.to_list())  # 应该是 [1, 2, 8, 17, 16, 15, 14, 3, 9, 10]
    
    # 测试查询
    print("区间[2,8]和:", treap.query_sum(2, 8))  # 应该是 8+17+16+15+14+3+9 = 82
    print("区间[2,8]最小值:", treap.query_min(2, 8))  # 应该是 3
    print("区间[2,8]最大值:", treap.query_max(2, 8))  # 应该是 17
    
    # 测试删除
    treap.delete(4)
    print("删除位置4后:", treap.to_list())  # 应该是 [1, 2, 8, 17, 15, 14, 3, 9, 10]