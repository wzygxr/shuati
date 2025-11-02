#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
稀疏表（Sparse Table）实现 - RMQ问题解决方案 (Python版本)

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

相关题目：
1. LeetCode 2444. 统计定界子数组的数目
2. POJ 3264 Balanced Lineup
3. SPOJ RMQSQ - Range Minimum Query
"""

import math
import random
import time

class SparseTableRMQ:
    """稀疏表实现类"""
    
    def __init__(self, data):
        """
        构造函数
        :param data: 输入数组
        """
        if not data:
            raise ValueError("输入数组不能为空")
        
        self.data = data[:]
        self.n = len(data)
        
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
        self.st_min = [[0 for _ in range(self.n)] for _ in range(k)]
        self.st_max = [[0 for _ in range(self.n)] for _ in range(k)]
        
        # 初始化第一行（区间长度为1）
        for i in range(self.n):
            self.st_min[0][i] = self.data[i]
            self.st_max[0][i] = self.data[i]
        
        # 填充其他行
        for j in range(1, k):
            for i in range(self.n - (1 << j) + 1):
                prev_len = 1 << (j - 1)
                # 范围最小值查询
                self.st_min[j][i] = min(self.st_min[j - 1][i], self.st_min[j - 1][i + prev_len])
                # 范围最大值查询
                self.st_max[j][i] = max(self.st_max[j - 1][i], self.st_max[j - 1][i + prev_len])
    
    def query_min(self, left, right):
        """
        范围最小值查询
        时间复杂度：O(1)
        :param left: 左边界（包含）
        :param right: 右边界（包含）
        :return: 区间内的最小值
        """
        if left < 0 or right >= self.n or left > right:
            raise ValueError("查询范围无效")
        
        length = right - left + 1
        k = self.log_table[length]
        
        return min(self.st_min[k][left], self.st_min[k][right - (1 << k) + 1])
    
    def query_max(self, left, right):
        """
        范围最大值查询
        时间复杂度：O(1)
        :param left: 左边界（包含）
        :param right: 右边界（包含）
        :return: 区间内的最大值
        """
        if left < 0 or right >= self.n or left > right:
            raise ValueError("查询范围无效")
        
        length = right - left + 1
        k = self.log_table[length]
        
        return max(self.st_max[k][left], self.st_max[k][right - (1 << k) + 1])
    
    def batch_query(self, queries, is_min_query=True):
        """
        批量处理范围查询
        :param queries: 查询数组，每个查询是[left, right]形式的元组
        :param is_min_query: 是否是最小值查询，否则是最大值查询
        :return: 查询结果数组
        """
        if not queries:
            raise ValueError("查询数组不能为空")
        
        results = []
        for query in queries:
            if len(query) != 2:
                raise ValueError("查询格式无效")
            
            if is_min_query:
                results.append(self.query_min(query[0], query[1]))
            else:
                results.append(self.query_max(query[0], query[1]))
        
        return results
    
    @staticmethod
    def test_sparse_table():
        """测试稀疏表"""
        print("=== 测试稀疏表 ===")
        
        data = [1, 3, 5, 7, 9, 11, 13, 15, 17]
        
        st = SparseTableRMQ(data)
        
        # 测试最小值查询
        print("测试最小值查询:")
        print("区间[1, 5]的最小值:", st.query_min(1, 5))  # 应该是3
        print("区间[0, 8]的最小值:", st.query_min(0, 8))  # 应该是1
        print("区间[4, 7]的最小值:", st.query_min(4, 7))  # 应该是9
        
        # 测试最大值查询
        print("\n测试最大值查询:")
        print("区间[1, 5]的最大值:", st.query_max(1, 5))  # 应该是11
        print("区间[0, 8]的最大值:", st.query_max(0, 8))  # 应该是17
        print("区间[4, 7]的最大值:", st.query_max(4, 7))  # 应该是15
        
        # 测试批量查询
        print("\n测试批量查询:")
        queries = [
            [0, 2], [1, 5], [3, 7], [2, 8]
        ]
        
        min_results = st.batch_query(queries, True)
        print("批量最小值查询结果:", min_results)
        
        max_results = st.batch_query(queries, False)
        print("批量最大值查询结果:", max_results)
        
        # 性能测试
        print("\n=== 性能测试 ===")
        
        # 生成大数据集
        n = 100000
        large_data = [random.randint(1, 1000000) for _ in range(n)]
        
        # 构建稀疏表
        start_time = time.time()
        large_st = SparseTableRMQ(large_data)
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
            large_st.query_min(left, right)
        query_time = time.time() - start_time
        
        print(f"构建100000个元素的稀疏表时间: {build_time*1000:.2f} ms")
        print(f"执行100000次查询时间: {query_time*1000:.2f} ms")
        print(f"平均每次查询时间: {query_time*1000000/num_queries:.4f} μs")

if __name__ == "__main__":
    SparseTableRMQ.test_sparse_table()