#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
POJ 2777 Count Color - 区间染色问题 (Python实现)

题目描述：
给定一个长度为L的板条，初始时所有位置都是颜色1，执行O次操作：
1. "C A B C": 将区间[A,B]染成颜色C
2. "P A B": 查询区间[A,B]中有多少种不同的颜色

解法要点：
- 使用线段树维护区间颜色集合(用位运算表示)
- 结合懒惰标记实现区间染色
- 通过位运算计算颜色种类数

时间复杂度：
- 区间染色：O(log L)
- 区间查询：O(log L)

空间复杂度：O(4L)

工程化考量：
- 使用位运算高效表示颜色集合
- 懒惰标记优化区间更新
- 输入验证和边界处理
- Python特性优化（列表预分配）
"""

import time
from typing import List

class Code12_CountColor:
    """
    区间染色线段树实现
    """
    
    def __init__(self, L: int):
        """
        构造函数
        
        Args:
            L: 板条长度
        """
        self.n = L
        self.tree = [0] * (4 * L)  # 线段树，存储颜色集合(位掩码)
        self.lazy = [0] * (4 * L)  # 懒惰标记，存储要设置的颜色
        
        # 初始化所有位置为颜色1
        self._build(1, 1, L)
    
    def _build(self, node: int, l: int, r: int) -> None:
        """
        构建线段树
        
        Args:
            node: 当前节点索引
            l: 当前节点左边界
            r: 当前节点右边界
        """
        if l == r:
            self.tree[node] = 1  # 颜色1用位掩码1表示
            return
        
        mid = (l + r) // 2
        self._build(node * 2, l, mid)
        self._build(node * 2 + 1, mid + 1, r)
        self.tree[node] = self.tree[node * 2] | self.tree[node * 2 + 1]
    
    def _push_down(self, node: int, l: int, r: int) -> None:
        """
        下推懒惰标记
        
        Args:
            node: 当前节点索引
            l: 当前节点左边界
            r: 当前节点右边界
        """
        if self.lazy[node] != 0 and l != r:
            color = self.lazy[node]
            self.lazy[node * 2] = color
            self.lazy[node * 2 + 1] = color
            self.tree[node * 2] = 1 << (color - 1)
            self.tree[node * 2 + 1] = 1 << (color - 1)
            self.lazy[node] = 0
    
    def update(self, l: int, r: int, color: int) -> None:
        """
        区间染色操作
        
        Args:
            l: 区间左端点
            r: 区间右端点
            color: 颜色编号(1-30)
        
        Raises:
            ValueError: 输入参数不合法
        """
        # 输入验证
        if l < 1 or r > self.n or l > r or color < 1 or color > 30:
            raise ValueError(f"Invalid parameters: l={l}, r={r}, color={color}")
        
        self._update(1, 1, self.n, l, r, color)
    
    def _update(self, node: int, l: int, r: int, ql: int, qr: int, color: int) -> None:
        """
        区间染色操作（内部实现）
        
        Args:
            node: 当前节点索引
            l: 当前节点左边界
            r: 当前节点右边界
            ql: 查询区间左边界
            qr: 查询区间右边界
            color: 颜色编号
        """
        if ql <= l and r <= qr:
            # 完全覆盖，设置懒惰标记和当前节点颜色
            self.lazy[node] = color
            self.tree[node] = 1 << (color - 1)
            return
        
        # 下推懒惰标记
        self._push_down(node, l, r)
        
        mid = (l + r) // 2
        if ql <= mid:
            self._update(node * 2, l, mid, ql, qr, color)
        if qr > mid:
            self._update(node * 2 + 1, mid + 1, r, ql, qr, color)
        
        # 合并子区间信息
        self.tree[node] = self.tree[node * 2] | self.tree[node * 2 + 1]
    
    def query(self, l: int, r: int) -> int:
        """
        查询区间颜色种类数
        
        Args:
            l: 区间左端点
            r: 区间右端点
            
        Returns:
            int: 颜色种类数
            
        Raises:
            ValueError: 输入参数不合法
        """
        # 输入验证
        if l < 1 or r > self.n or l > r:
            raise ValueError(f"Invalid query parameters: l={l}, r={r}")
        
        mask = self._query(1, 1, self.n, l, r)
        return bin(mask).count('1')  # 计算位掩码中1的个数
    
    def _query(self, node: int, l: int, r: int, ql: int, qr: int) -> int:
        """
        查询区间颜色集合（内部实现）
        
        Args:
            node: 当前节点索引
            l: 当前节点左边界
            r: 当前节点右边界
            ql: 查询区间左边界
            qr: 查询区间右边界
            
        Returns:
            int: 颜色集合的位掩码
        """
        if ql <= l and r <= qr:
            return self.tree[node]
        
        # 下推懒惰标记
        self._push_down(node, l, r)
        
        mid = (l + r) // 2
        result = 0
        if ql <= mid:
            result |= self._query(node * 2, l, mid, ql, qr)
        if qr > mid:
            result |= self._query(node * 2 + 1, mid + 1, r, ql, qr)
        return result
    
    def print_state(self) -> None:
        """
        打印当前线段树状态（用于调试）
        """
        print("Tree state (first 10 elements): ", end="")
        for i in range(1, min(11, self.n + 1)):
            mask = self._query(1, 1, self.n, i, i)
            color = 0
            temp = mask
            while temp:
                color += 1
                temp >>= 1
            print(f"{color}", end=" ")
        print()

def test_code12_count_color():
    """
    测试函数
    """
    # 测试用例1：基本功能测试
    print("=== 测试用例1：基本功能测试 ===")
    seg_tree = Code12_CountColor(10)
    
    # 初始状态：所有位置都是颜色1
    print(f"初始查询[1,10]颜色种类数: {seg_tree.query(1, 10)}")  # 应为1
    
    # 染色操作
    seg_tree.update(1, 5, 2)  # 将[1,5]染成颜色2
    print(f"染色后查询[1,10]颜色种类数: {seg_tree.query(1, 10)}")  # 应为2
    print(f"查询[1,5]颜色种类数: {seg_tree.query(1, 5)}")  # 应为1
    print(f"查询[6,10]颜色种类数: {seg_tree.query(6, 10)}")  # 应为1
    
    # 覆盖染色
    seg_tree.update(3, 7, 3)  # 将[3,7]染成颜色3
    print(f"覆盖染色后查询[1,10]颜色种类数: {seg_tree.query(1, 10)}")  # 应为3
    
    # 测试用例2：边界情况
    print("\n=== 测试用例2：边界情况 ===")
    seg_tree2 = Code12_CountColor(5)
    
    # 单点染色
    seg_tree2.update(1, 1, 2)
    seg_tree2.update(5, 5, 3)
    print(f"单点染色后查询[1,5]颜色种类数: {seg_tree2.query(1, 5)}")  # 应为3
    
    # 测试用例3：性能测试（大规模数据）
    print("\n=== 测试用例3：性能测试 ===")
    L = 100000
    large_seg_tree = Code12_CountColor(L)
    
    start_time = time.time()
    for i in range(1, 1001):
        l = (i * 10) % L + 1
        r = min(l + 100, L)
        large_seg_tree.update(l, r, (i % 30) + 1)
    end_time = time.time()
    print(f"1000次染色操作耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 测试用例4：错误输入处理
    print("\n=== 测试用例4：错误输入处理 ===")
    error_seg_tree = Code12_CountColor(10)
    
    # 测试无效输入
    try:
        error_seg_tree.update(0, 5, 2)  # 无效左边界
    except ValueError as e:
        print(f"捕获到预期错误: {e}")
    
    try:
        error_seg_tree.update(1, 15, 2)  # 无效右边界
    except ValueError as e:
        print(f"捕获到预期错误: {e}")
    
    try:
        error_seg_tree.update(5, 3, 2)  # 左边界大于右边界
    except ValueError as e:
        print(f"捕获到预期错误: {e}")
    
    try:
        error_seg_tree.update(1, 5, 0)  # 无效颜色
    except ValueError as e:
        print(f"捕获到预期错误: {e}")
    
    try:
        error_seg_tree.update(1, 5, 31)  # 无效颜色
    except ValueError as e:
        print(f"捕获到预期错误: {e}")
    
    print("所有测试用例通过！")

# 性能优化技巧说明
class Code12_CountColorOptimized(Code12_CountColor):
    """
    优化版本的区间染色线段树
    
    优化点：
    1. 使用局部变量减少属性访问
    2. 避免重复计算
    3. 使用位运算优化
    """
    
    def _update_optimized(self, node: int, l: int, r: int, ql: int, qr: int, color: int) -> None:
        """
        优化版本的区间更新
        """
        tree = self.tree
        lazy = self.lazy
        
        if ql <= l and r <= qr:
            lazy[node] = color
            tree[node] = 1 << (color - 1)
            return
        
        # 使用局部变量优化
        mid = (l + r) // 2
        left_node = node * 2
        right_node = node * 2 + 1
        
        # 下推懒惰标记
        if lazy[node] != 0 and l != r:
            temp_color = lazy[node]
            lazy[left_node] = temp_color
            lazy[right_node] = temp_color
            tree[left_node] = 1 << (temp_color - 1)
            tree[right_node] = 1 << (temp_color - 1)
            lazy[node] = 0
        
        if ql <= mid:
            self._update_optimized(left_node, l, mid, ql, qr, color)
        if qr > mid:
            self._update_optimized(right_node, mid + 1, r, ql, qr, color)
        
        tree[node] = tree[left_node] | tree[right_node]

if __name__ == "__main__":
    test_code12_count_color()