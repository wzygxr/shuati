#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
SPOJ - RMQSQ - Range Minimum Query 解决方案

题目链接: https://www.spoj.com/problems/RMQSQ/
题目描述: 经典的范围最小值查询问题
解题思路: 使用稀疏表实现O(1)查询

时间复杂度: 
- 预处理: O(n log n)
- 查询: O(1)
空间复杂度: O(n log n)
"""

import math
from typing import List

class SparseTable:
    """稀疏表实现类"""
    
    def __init__(self, data: List[int]):
        """
        构造函数
        
        Args:
            data: 输入数组
        """
        if not data:
            raise ValueError("输入数组不能为空")
        
        self.data = data
        n = len(data)
        
        # 计算log表
        self._precompute_log_table(n)
        
        # 计算稀疏表
        k = self.log_table[n] + 1
        self.st = [[0 for _ in range(k)] for _ in range(n)]
        
        # 初始化第一列（区间长度为1）
        for i in range(n):
            self.st[i][0] = data[i]
        
        # 填充其他列
        for j in range(1, k):
            for i in range(n - (1 << j) + 1):
                self.st[i][j] = min(self.st[i][j-1], self.st[i + (1 << (j-1))][j-1])
    
    def _precompute_log_table(self, n: int) -> None:
        """预计算log2值表"""
        self.log_table = [0] * (n + 1)
        self.log_table[1] = 0
        for i in range(2, n + 1):
            self.log_table[i] = self.log_table[i // 2] + 1
    
    def query(self, l: int, r: int) -> int:
        """
        区间最小值查询
        时间复杂度：O(1)
        
        Args:
            l: 左边界（包含）
            r: 右边界（包含）
            
        Returns:
            区间内的最小值
        """
        if l < 0 or r >= len(self.data) or l > r:
            raise ValueError("查询范围无效")
        
        length = r - l + 1
        k = self.log_table[length]
        
        return min(self.st[l][k], self.st[r - (1 << k) + 1][k])

class Solution:
    """SPOJ RMQSQ问题解决方案"""
    
    def solve(self, data: List[int], queries: List[List[int]]) -> List[int]:
        """
        解决SPOJ RMQSQ问题
        
        Args:
            data: 输入数组
            queries: 查询列表，每个查询是[l, r]的形式
            
        Returns:
            查询结果列表
        """
        # 构建稀疏表
        sparse_table = SparseTable(data)
        
        # 处理每个查询
        results = []
        for l, r in queries:
            results.append(sparse_table.query(l, r))
        
        return results
    
    @staticmethod
    def test_sparse_table():
        """测试稀疏表"""
        print("=== 测试稀疏表 ===")
        
        data = [1, 3, 5, 7, 9, 11, 13, 15, 17]
        
        # 创建稀疏表
        sparse_table = SparseTable(data)
        
        # 测试查询
        print(f"数组: {data}")
        print(f"区间[1, 5]的最小值: {sparse_table.query(1, 5)}")  # 应该是3
        print(f"区间[0, 8]的最小值: {sparse_table.query(0, 8)}")  # 应该是1
        print(f"区间[4, 7]的最小值: {sparse_table.query(4, 7)}")  # 应该是9
        print(f"区间[2, 2]的最小值: {sparse_table.query(2, 2)}")  # 应该是5
        print(f"区间[6, 8]的最小值: {sparse_table.query(6, 8)}")  # 应该是13
    
    @staticmethod
    def performance_test():
        """性能测试"""
        print("\n=== 性能测试 ===")
        
        import random
        import time
        
        # 生成大数据集
        n = 100000
        large_data = [random.randint(1, 1000000) for _ in range(n)]
        
        # 构建稀疏表
        start_time = time.time()
        large_st = SparseTable(large_data)
        build_time = time.time() - start_time
        
        # 执行大量查询
        num_queries = 100000
        queries = []
        for _ in range(num_queries):
            # 生成有效的查询范围
            left = random.randint(0, n-1)
            right = random.randint(left, min(left+1000, n-1))
            queries.append([left, right])
        
        start_time = time.time()
        for left, right in queries:
            large_st.query(left, right)
        query_time = time.time() - start_time
        
        print(f"构建{n}个元素的稀疏表时间: {build_time*1000:.2f} ms")
        print(f"执行{num_queries}次查询时间: {query_time*1000:.2f} ms")
        print(f"平均每次查询时间: {query_time*1000000/num_queries:.4f} μs")

if __name__ == "__main__":
    Solution.test_sparse_table()
    Solution.performance_test()