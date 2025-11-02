#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
稀疏表（Sparse Table）实现 (Python版本)

算法思路：
稀疏表是一种用于解决范围查询问题的数据结构，特别适用于：
1. 范围最小值查询（RMQ）
2. 范围最大值查询
3. 其他满足结合律和幂等性的操作

稀疏表通过预处理，在O(n log n)时间内构建一个二维数组，
然后在O(1)时间内回答任何范围查询。

时间复杂度：
- 预处理：O(n log n)
- 查询：O(1)
空间复杂度：O(n log n)

应用场景：
1. 数据库：范围查询优化
2. 图像处理：区域统计信息计算
3. 金融：时间序列分析中的极值查询
4. 算法竞赛：优化动态规划中的范围查询
"""

import math

class SparseTable:
    """稀疏表实现类"""
    
    def __init__(self, data, is_min=True):
        """
        构造函数
        :param data: 输入数组
        :param is_min: 是否是最小值查询（True）或最大值查询（False）
        """
        if not data:
            raise ValueError("输入数组不能为空")
        
        self.data = data
        self.n = len(data)
        self.is_min = is_min
        
        # 计算log表
        self._precompute_log_table()
        
        # 计算稀疏表
        self._compute_sparse_table()
    
    def _precompute_log_table(self):
        """预计算log2值表"""
        self.log_table = [0] * (self.n + 1)
        self.log_table[1] = 0
        for i in range(2, self.n + 1):
            self.log_table[i] = self.log_table[i // 2] + 1
    
    def _compute_sparse_table(self):
        """计算稀疏表"""
        # 计算稀疏表的大小
        k = self.log_table[self.n] + 1
        self.st = [[0 for _ in range(self.n)] for _ in range(k)]
        
        # 初始化第一行（区间长度为1）
        for i in range(self.n):
            self.st[0][i] = i  # 存储索引而不是值，便于范围查询
        
        # 填充其他行
        for j in range(1, k):
            for i in range(self.n - (1 << j) + 1):
                prev_len = 1 << (j - 1)
                left = self.st[j - 1][i]
                right = self.st[j - 1][i + prev_len]
                
                # 根据查询类型选择最小或最大值
                if self.is_min:
                    self.st[j][i] = left if self.data[left] <= self.data[right] else right
                else:
                    self.st[j][i] = left if self.data[left] >= self.data[right] else right
    
    def query(self, left, right):
        """
        区间查询操作
        时间复杂度：O(1)
        :param left: 左边界（包含）
        :param right: 右边界（包含）
        :return: 区间内的最小/最大值
        """
        if left < 0 or right >= self.n or left > right:
            raise ValueError("查询范围无效")
        
        length = right - left + 1
        k = self.log_table[length]
        
        left_idx = self.st[k][left]
        right_idx = self.st[k][right - (1 << k) + 1]
        
        if self.is_min:
            return min(self.data[left_idx], self.data[right_idx])
        else:
            return max(self.data[left_idx], self.data[right_idx])
    
    def process_offline_queries(self, queries):
        """
        离线查询处理
        :param queries: 包含多个查询的数组，每个查询是 [left, right] 形式
        :return: 查询结果数组
        """
        results = []
        for left, right in queries:
            results.append(self.query(left, right))
        return results
    
    @staticmethod
    def test_sparse_table():
        """测试稀疏表"""
        print("=== 测试稀疏表 ===")
        
        data = [1, 3, 5, 7, 9, 11, 13, 15, 17]
        
        # 测试最小值查询
        print("测试最小值查询:")
        min_st = SparseTable(data, True)
        print(f"区间[1, 5]的最小值: {min_st.query(1, 5)}")  # 应该是3
        print(f"区间[0, 8]的最小值: {min_st.query(0, 8)}")  # 应该是1
        print(f"区间[4, 7]的最小值: {min_st.query(4, 7)}")  # 应该是9
        
        # 测试最大值查询
        print("\n测试最大值查询:")
        max_st = SparseTable(data, False)
        print(f"区间[1, 5]的最大值: {max_st.query(1, 5)}")  # 应该是11
        print(f"区间[0, 8]的最大值: {max_st.query(0, 8)}")  # 应该是17
        print(f"区间[4, 7]的最大值: {max_st.query(4, 7)}")  # 应该是15
        
        # 测试离线查询
        print("\n测试离线查询:")
        queries = [
            [0, 2], [1, 5], [3, 7], [2, 8]
        ]
        
        min_results = min_st.process_offline_queries(queries)
        print("离线最小值查询结果:", min_results)
        
        max_results = max_st.process_offline_queries(queries)
        print("离线最大值查询结果:", max_results)
        
        # 性能测试
        print("\n=== 性能测试 ===")
        import random
        import time
        
        # 生成大数据集
        n = 100000
        large_data = [random.randint(1, 1000000) for _ in range(n)]
        
        # 构建稀疏表
        start_time = time.time()
        large_st = SparseTable(large_data, True)
        build_time = time.time() - start_time
        
        # 执行大量查询
        num_queries = 100000
        queries = []
        for _ in range(num_queries):
            # 生成有效的查询范围
            left = random.randint(0, n-2)
            right = random.randint(left+1, min(left+1000, n-1))
            queries.append((left, right))
        
        start_time = time.time()
        for left, right in queries:
            large_st.query(left, right)
        query_time = time.time() - start_time
        
        print(f"构建100000个元素的稀疏表时间: {build_time*1000:.2f} ms")
        print(f"执行100000次查询时间: {query_time*1000:.2f} ms")
        print(f"平均每次查询时间: {query_time*1000000/num_queries:.4f} μs")

if __name__ == "__main__":
    SparseTable.test_sparse_table()