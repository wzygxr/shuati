#!/usr/bin/env python3
"""
Code26: CF911G Mass Change Queries (Advanced) - Python版本
线段树分裂 + 值域线段树 + 懒标记
"""

import sys
from typing import Optional, List

class SegmentTreeNode:
    def __init__(self, l: int, r: int):
        self.l = l
        self.r = r
        self.sum = 0
        self.lazy = -1
        self.left: Optional['SegmentTreeNode'] = None
        self.right: Optional['SegmentTreeNode'] = None

class SegmentTree:
    def __init__(self, arr: List[int]):
        self.n = 100
        self.roots: List[Optional[SegmentTreeNode]] = [None] * 101
        
        for i in range(1, 101):
            self.roots[i] = SegmentTreeNode(1, self.n)
        
        for i, val in enumerate(arr):
            self._update_value(self.roots[val], i + 1, i + 1, 1)
    
    def _push_down(self, node: SegmentTreeNode) -> None:
        if node.l == node.r:
            return
        
        mid = (node.l + node.r) // 2
        if not node.left:
            node.left = SegmentTreeNode(node.l, mid)
        if not node.right:
            node.right = SegmentTreeNode(mid + 1, node.r)
        
        if node.lazy != -1:
            node.left.lazy = node.lazy
            node.right.lazy = node.lazy
            node.left.sum = (mid - node.l + 1) * node.lazy
            node.right.sum = (node.r - mid) * node.lazy
            node.lazy = -1
    
    def _update_value(self, node: SegmentTreeNode, L: int, R: int, val: int) -> None:
        if L <= node.l and node.r <= R:
            node.sum += val
            return
        
        self._push_down(node)
        mid = (node.l + node.r) // 2
        
        if L <= mid:
            if not node.left:
                node.left = SegmentTreeNode(node.l, mid)
            self._update_value(node.left, L, R, val)
        if R > mid:
            if not node.right:
                node.right = SegmentTreeNode(mid + 1, node.r)
            self._update_value(node.right, L, R, val)
        
        left_sum = node.left.sum if node.left else 0
        right_sum = node.right.sum if node.right else 0
        node.sum = left_sum + right_sum
    
    def _split(self, node: Optional[SegmentTreeNode], L: int, R: int) -> Optional[SegmentTreeNode]:
        if not node or R < node.l or L > node.r:
            return None
        
        if L <= node.l and node.r <= R:
            result = SegmentTreeNode(node.l, node.r)
            result.sum = node.sum
            result.lazy = node.lazy
            result.left = node.left
            result.right = node.right
            
            node.sum = 0
            node.lazy = -1
            node.left = None
            node.right = None
            
            return result
        
        self._push_down(node)
        mid = (node.l + node.r) // 2
        
        left_split = None
        right_split = None
        
        if L <= mid:
            left_split = self._split(node.left, L, R) if node.left else None
        if R > mid:
            right_split = self._split(node.right, L, R) if node.right else None
        
        left_sum = node.left.sum if node.left else 0
        right_sum = node.right.sum if node.right else 0
        node.sum = left_sum + right_sum
        
        if left_split or right_split:
            result = SegmentTreeNode(node.l, node.r)
            result.left = left_split
            result.right = right_split
            result.sum = (left_split.sum if left_split else 0) + (right_split.sum if right_split else 0)
            return result
        
        return None
    
    def _merge(self, a: Optional[SegmentTreeNode], b: Optional[SegmentTreeNode]) -> Optional[SegmentTreeNode]:
        if not a:
            return b
        if not b:
            return a
        
        if a.l == a.r:
            a.sum += b.sum
            return a
        
        self._push_down(a)
        self._push_down(b)
        
        a.left = self._merge(a.left, b.left)
        a.right = self._merge(a.right, b.right)
        
        left_sum = a.left.sum if a.left else 0
        right_sum = a.right.sum if a.right else 0
        a.sum = left_sum + right_sum
        
        return a
    
    def _subtract(self, a: Optional[SegmentTreeNode], b: Optional[SegmentTreeNode]) -> Optional[SegmentTreeNode]:
        if not b:
            return a
        if not a:
            return None
        
        if a.l == a.r:
            a.sum -= b.sum
            if a.sum <= 0:
                return None
            return a
        
        self._push_down(a)
        self._push_down(b)
        
        a.left = self._subtract(a.left, b.left)
        a.right = self._subtract(a.right, b.right)
        
        left_sum = a.left.sum if a.left else 0
        right_sum = a.right.sum if a.right else 0
        a.sum = left_sum + right_sum
        
        if a.sum == 0:
            return None
        return a
    
    def _query_range(self, node: Optional[SegmentTreeNode], L: int, R: int) -> int:
        if not node or R < node.l or L > node.r:
            return 0
        
        if L <= node.l and node.r <= R:
            return node.sum
        
        self._push_down(node)
        mid = (node.l + node.r) // 2
        res = 0
        
        if L <= mid and node.left:
            res += self._query_range(node.left, L, R)
        if R > mid and node.right:
            res += self._query_range(node.right, L, R)
        
        return res
    
    def mass_change(self, l: int, r: int, x: int, y: int) -> None:
        if x == y:
            return
        
        split_tree = self._split(self.roots[x], l, r)
        if split_tree:
            self.roots[y] = self._merge(self.roots[y], split_tree)
            self.roots[x] = self._subtract(self.roots[x], split_tree)
    
    def query_sum(self, l: int, r: int) -> int:
        total = 0
        for i in range(1, 101):
            count = self._query_range(self.roots[i], l, r)
            total += count * i
        return total

def main():
    data = sys.stdin.read().split()
    idx = 0
    
    n = int(data[idx]); idx += 1
    arr = list(map(int, data[idx:idx + n])); idx += n
    
    stree = SegmentTree(arr)
    
    q = int(data[idx]); idx += 1
    results = []
    
    for _ in range(q):
        type_ = int(data[idx]); idx += 1
        if type_ == 1:
            l = int(data[idx]); idx += 1
            r = int(data[idx]); idx += 1
            x = int(data[idx]); idx += 1
            y = int(data[idx]); idx += 1
            stree.mass_change(l, r, x, y)
        else:
            l = int(data[idx]); idx += 1
            r = int(data[idx]); idx += 1
            result = stree.query_sum(l, r)
            results.append(str(result))
    
    print('\n'.join(results))

if __name__ == "__main__":
    main()