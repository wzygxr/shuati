#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
CodeChef MSTICK - 区间最值查询 - Sparse Table应用
题目链接：https://www.codechef.com/problems/MSTICK

【算法核心思想】
使用Sparse Table同时预处理区间最大值和最小值，实现O(1)查询。
对于每个查询，分别查询最大值和最小值，然后计算它们的差值。

【时间复杂度分析】
- 预处理：O(n log n) - 构建两个ST表
- 查询：O(1) - 每次查询两次ST表查询
- 总时间复杂度：O(n log n + q)

【空间复杂度分析】
- 最大值ST表：O(n log n)
- 最小值ST表：O(n log n)
- 总空间复杂度：O(n log n)
"""

import math
import time
import random
from typing import List, Tuple

class SparseTable:
    """
    通用的Sparse Table实现类
    支持最大值和最小值查询
    """
    
    def __init__(self, arr: List[int], is_max_query: bool):
        self.is_max_query = is_max_query
        self.n = len(arr)
        if self.n == 0:
            return
            
        self.max_log = math.floor(math.log2(self.n)) + 1
        self.st = [[0] * self.max_log for _ in range(self.n)]
        self.log_table = [0] * (self.n + 1)
        
        self._preprocess_log()
        
        # 初始化第一层
        for i in range(self.n):
            self.st[i][0] = arr[i]
        
        # 动态规划构建ST表
        for j in range(1, self.max_log):
            step = 1 << j
            for i in range(self.n - step + 1):
                if self.is_max_query:
                    self.st[i][j] = max(self.st[i][j-1], self.st[i + (1 << (j-1))][j-1])
                else:
                    self.st[i][j] = min(self.st[i][j-1], self.st[i + (1 << (j-1))][j-1])
    
    def _preprocess_log(self):
        """预处理log2值"""
        self.log_table[1] = 0
        for i in range(2, self.n + 1):
            self.log_table[i] = self.log_table[i // 2] + 1
    
    def query(self, l: int, r: int) -> int:
        """查询区间[l, r]的最值"""
        if l > r:
            raise ValueError("Invalid range")
            
        length = r - l + 1
        k = self.log_table[length]
        
        if self.is_max_query:
            return max(self.st[l][k], self.st[r - (1 << k) + 1][k])
        else:
            return min(self.st[l][k], self.st[r - (1 << k) + 1][k])

class RangeMinMaxQuery:
    """
    区间最值查询解决方案类
    """
    
    def __init__(self, arr: List[int]):
        self.arr = arr
        self.max_st = SparseTable(arr, True)   # 最大值查询
        self.min_st = SparseTable(arr, False)  # 最小值查询
    
    def query_difference(self, l: int, r: int) -> int:
        """
        查询区间[l, r]的最大值和最小值的差值
        
        Args:
            l: 区间左端点（包含）
            r: 区间右端点（包含）
            
        Returns:
            最大值和最小值的差值
        """
        if l < 0 or r >= len(self.arr) or l > r:
            raise ValueError("Invalid query range")
            
        max_val = self.max_st.query(l, r)
        min_val = self.min_st.query(l, r)
        
        return max_val - min_val
    
    def query_min_max(self, l: int, r: int) -> Tuple[int, int]:
        """
        分别查询区间[l, r]的最小值和最大值
        
        Args:
            l: 区间左端点（包含）
            r: 区间右端点（包含）
            
        Returns:
            (最小值, 最大值)
        """
        if l < 0 or r >= len(self.arr) or l > r:
            raise ValueError("Invalid query range")
            
        min_val = self.min_st.query(l, r)
        max_val = self.max_st.query(l, r)
        
        return min_val, max_val

def test_codechef_mstick():
    """单元测试函数"""
    print("=== CodeChef MSTICK 测试 ===")
    
    # 测试用例1：CodeChef示例
    arr1 = [1, 2, 3, 4, 5]
    solver1 = RangeMinMaxQuery(arr1)
    
    print("测试用例1 - CodeChef示例:")
    print(f"查询[0,4]: {solver1.query_difference(0, 4)} (期望: 4)")
    print(f"查询[1,3]: {solver1.query_difference(1, 3)} (期望: 2)")
    print(f"查询[2,4]: {solver1.query_difference(2, 4)} (期望: 2)")
    
    # 测试用例2：所有元素相同
    arr2 = [5, 5, 5, 5, 5]
    solver2 = RangeMinMaxQuery(arr2)
    print("\n测试用例2 - 所有元素相同:")
    print(f"查询[0,4]: {solver2.query_difference(0, 4)} (期望: 0)")
    
    # 测试用例3：递减序列
    arr3 = [5, 4, 3, 2, 1]
    solver3 = RangeMinMaxQuery(arr3)
    print("\n测试用例3 - 递减序列:")
    print(f"查询[0,4]: {solver3.query_difference(0, 4)} (期望: 4)")
    
    # 测试用例4：随机数组
    arr4 = [3, 7, 1, 9, 4, 6, 2, 8, 5]
    solver4 = RangeMinMaxQuery(arr4)
    print("\n测试用例4 - 随机数组:")
    print(f"查询[0,8]: {solver4.query_difference(0, 8)} (期望: 8)")
    print(f"查询[2,6]: {solver4.query_difference(2, 6)} (期望: 8)")
    
    # 分别查询最小值和最大值
    min_val, max_val = solver4.query_min_max(2, 6)
    print(f"区间[2,6]的最小值: {min_val}, 最大值: {max_val}")
    
    # 性能测试
    large_arr = [random.randint(0, 1000000) for _ in range(100000)]
    large_solver = RangeMinMaxQuery(large_arr)
    
    start_time = time.time()
    for _ in range(1000):
        l = random.randint(0, len(large_arr) - 100)
        r = l + random.randint(0, 99)
        large_solver.query_difference(l, r)
    end_time = time.time()
    
    print(f"\n性能测试: 1000次查询耗时 {(end_time - start_time) * 1000:.2f}ms")
    
    # 边界测试
    print("\n边界测试:")
    try:
        solver1.query_difference(-1, 4)
    except ValueError as e:
        print(f"边界测试1通过: {e}")
    
    try:
        solver1.query_difference(3, 2)
    except ValueError as e:
        print(f"边界测试2通过: {e}")
    
    print("\n=== 测试完成 ===")

if __name__ == "__main__":
    test_codechef_mstick()