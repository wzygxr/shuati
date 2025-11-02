#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
SPOJ FREQUENT - 区间频繁值查询 - Sparse Table应用
题目链接：https://www.spoj.com/problems/FREQUENT/

【题目描述】
给定一个非降序数组，多次查询区间内出现次数最多的数的出现次数。
由于数组是非降序的，相同的数字会连续出现，这大大简化了问题。

【算法核心思想】
结合游程编码和Sparse Table解决区间频繁值查询问题。
由于数组是非降序的，可以将连续的相同数字压缩为游程，然后使用Sparse Table查询游程长度的最大值。

【时间复杂度分析】
- 预处理：O(n) - 游程编码 + O(m log m) - Sparse Table构建（m为游程数量）
- 查询：O(1) - 每次查询最多3次ST表查询
- 总时间复杂度：O(n + m log m + q)

【空间复杂度分析】
- 游程数组：O(n)
- Sparse Table：O(m log m)
- 总空间复杂度：O(n + m log m)
"""

import math
import time
import random
from typing import List
from dataclasses import dataclass

@dataclass
class Run:
    """游程结构体"""
    value: int      # 游程的值
    start: int      # 游程开始位置
    end: int        # 游程结束位置
    
    @property
    def length(self):
        """游程长度"""
        return self.end - self.start + 1

class SparseTable:
    """
    Sparse Table实现类（最大值查询）
    """
    
    def __init__(self, arr: List[int]):
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
                self.st[i][j] = max(self.st[i][j-1], self.st[i + (1 << (j-1))][j-1])
    
    def _preprocess_log(self):
        """预处理log2值"""
        self.log_table[1] = 0
        for i in range(2, self.n + 1):
            self.log_table[i] = self.log_table[i // 2] + 1
    
    def query(self, l: int, r: int) -> int:
        """查询区间最大值"""
        if l > r:
            return 0
        length = r - l + 1
        k = self.log_table[length]
        return max(self.st[l][k], self.st[r - (1 << k) + 1][k])

class FrequentQuerySolver:
    """
    频繁值查询解决方案类
    """
    
    def __init__(self, arr: List[int]):
        self.arr = arr
        self.runs: List[Run] = []
        self.run_index = [0] * len(arr)
        self.st = None
        
        # 执行游程编码
        self._run_length_encoding()
        
        # 构建Sparse Table
        self._build_sparse_table()
    
    def _run_length_encoding(self):
        """游程编码：将连续的相同数字压缩为游程"""
        if not self.arr:
            return
            
        current_value = self.arr[0]
        start = 0
        
        for i in range(1, len(self.arr)):
            if self.arr[i] != current_value:
                # 结束当前游程
                self.runs.append(Run(current_value, start, i - 1))
                # 填充run_index
                for j in range(start, i):
                    self.run_index[j] = len(self.runs) - 1
                # 开始新游程
                current_value = self.arr[i]
                start = i
        
        # 处理最后一个游程
        self.runs.append(Run(current_value, start, len(self.arr) - 1))
        for j in range(start, len(self.arr)):
            self.run_index[j] = len(self.runs) - 1
    
    def _build_sparse_table(self):
        """构建Sparse Table用于查询游程长度最大值"""
        lengths = [run.length for run in self.runs]
        self.st = SparseTable(lengths)
    
    def query(self, l: int, r: int) -> int:
        """
        查询区间[l, r]内出现次数最多的数的出现次数
        
        Args:
            l: 区间左端点（包含）
            r: 区间右端点（包含）
            
        Returns:
            区间最大值出现次数
        """
        if l > r or l < 0 or r >= len(self.arr):
            raise ValueError("Invalid query range")
            
        left_run_idx = self.run_index[l]
        right_run_idx = self.run_index[r]
        
        # 情况1：查询区间完全在一个游程内
        if left_run_idx == right_run_idx:
            return r - l + 1
        
        # 情况2：查询区间跨越多个游程
        max_freq = 0
        
        # 处理左边界游程
        left_run = self.runs[left_run_idx]
        max_freq = max(max_freq, left_run.end - l + 1)
        
        # 处理右边界游程
        right_run = self.runs[right_run_idx]
        max_freq = max(max_freq, r - right_run.start + 1)
        
        # 处理中间游程（如果存在）
        if right_run_idx - left_run_idx > 1:
            max_freq = max(max_freq, self.st.query(left_run_idx + 1, right_run_idx - 1))
        
        return max_freq
    
    def get_runs_info(self) -> List[dict]:
        """获取游程信息（用于调试）"""
        return [{
            'value': run.value,
            'start': run.start,
            'end': run.end,
            'length': run.length
        } for run in self.runs]

def test_spoj_frequent():
    """单元测试函数"""
    print("=== SPOJ FREQUENT 测试 ===")
    
    # 测试用例1：SPOJ示例
    arr1 = [-1, -1, 1, 1, 1, 1, 3, 10, 10, 10]
    solver1 = FrequentQuerySolver(arr1)
    
    print("测试用例1 - SPOJ示例:")
    print(f"查询[0,1]: {solver1.query(0, 1)} (期望: 1)")
    print(f"查询[0,5]: {solver1.query(0, 5)} (期望: 2)")
    print(f"查询[5,9]: {solver1.query(5, 9)} (期望: 4)")
    
    # 测试用例2：所有元素相同
    arr2 = [5, 5, 5, 5, 5]
    solver2 = FrequentQuerySolver(arr2)
    print("\n测试用例2 - 所有元素相同:")
    print(f"查询[0,4]: {solver2.query(0, 4)} (期望: 5)")
    
    # 测试用例3：每个元素都不同
    arr3 = [1, 2, 3, 4, 5]
    solver3 = FrequentQuerySolver(arr3)
    print("\n测试用例3 - 每个元素都不同:")
    print(f"查询[0,4]: {solver3.query(0, 4)} (期望: 1)")
    
    # 测试用例4：混合情况
    arr4 = [1, 1, 2, 2, 2, 3, 3, 3, 3, 4]
    solver4 = FrequentQuerySolver(arr4)
    print("\n测试用例4 - 混合情况:")
    print(f"查询[0,9]: {solver4.query(0, 9)} (期望: 4)")
    print(f"查询[2,6]: {solver4.query(2, 6)} (期望: 3)")
    
    # 性能测试
    large_arr = [0] * 100000
    current = 0
    for i in range(len(large_arr)):
        if random.random() < 0.1:  # 10%概率改变值
            current = random.randint(0, 99)
        large_arr[i] = current
    
    large_solver = FrequentQuerySolver(large_arr)
    
    start_time = time.time()
    for _ in range(1000):
        l = random.randint(0, len(large_arr) - 100)
        r = l + random.randint(0, 99)
        large_solver.query(l, r)
    end_time = time.time()
    
    print(f"\n性能测试: 1000次查询耗时 {(end_time - start_time) * 1000:.2f}ms")
    
    print("\n=== 测试完成 ===")

if __name__ == "__main__":
    test_spoj_frequent()