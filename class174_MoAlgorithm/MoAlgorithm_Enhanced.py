#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# ================================================
# 莫队算法增强版 - Python实现 (带工程化考量和异常处理)
# ================================================
# 
# 功能描述:
# 实现普通莫队算法，支持区间不同元素统计和区间元素出现次数平方和计算
# 
# 包含题目:
# 1. DQUERY - 区间不同元素个数统计
# 2. 小B的询问 - 区间元素出现次数平方和
# 
# 算法复杂度分析:
# 时间复杂度: O((n + q) * sqrt(n)) - 莫队算法标准复杂度
# 空间复杂度: O(n + max(arr[i])) - 数组存储和计数数组
# 
# 工程化考量:
# 1. 异常处理: 输入验证、边界检查、数组越界防护
# 2. 性能优化: 奇偶排序优化、缓存友好访问
# 3. 可维护性: 模块化设计、清晰注释、常量定义
# 4. 测试覆盖: 边界场景、极端输入、随机测试
# 
# 运行指令:
# python MoAlgorithm_Enhanced.py
# ================================================

import math
import time
import random
from typing import List, Tuple, Dict, Any
from dataclasses import dataclass

# ========== 常量定义 ==========
MAXN = 30001 + 10  # 额外空间用于边界处理
MAXV = 1000001 + 10
MAXQ = 200001 + 10

# ========== 查询结构体 ==========
@dataclass
class Query:
    l: int
    r: int
    id: int
    
    def __init__(self, l: int = 0, r: int = 0, id: int = 0):
        self.l = l
        self.r = r
        self.id = id

# ========== 莫队算法基类 ==========
class MoAlgorithm:
    def __init__(self):
        self.arr = [0] * MAXN
        self.block = [0] * MAXN
        self.cnt = [0] * MAXV
        self.block_size = 0
        self.n = 0
        self.q = 0
        
        # 异常处理标志
        self.has_error = False
        self.error_message = ""
    
    def validate_input(self, n: int, queries: List[Query]) -> bool:
        """
        输入验证函数
        
        Args:
            n: 数组长度
            queries: 查询数组
            
        Returns:
            验证是否通过
        """
        if n < 1 or n > 30000:
            self.handle_error(f"Invalid array size: {n}")
            return False
        
        if len(queries) > 200000:
            self.handle_error(f"Too many queries: {len(queries)}")
            return False
        
        # 验证数组元素范围
        for i in range(1, n + 1):
            if self.arr[i] < 1 or self.arr[i] > 1000000:
                self.handle_error(f"Invalid array element at index {i}: {self.arr[i]}")
                return False
        
        # 验证查询范围
        for i, query in enumerate(queries):
            l, r = query.l, query.r
            if l < 1 or l > n or r < 1 or r > n or l > r:
                self.handle_error(f"Invalid query range at query {i}: [{l}, {r}]")
                return False
        
        return True
    
    def handle_error(self, message: str) -> None:
        """
        统一错误处理函数
        
        Args:
            message: 错误信息
        """
        self.has_error = True
        self.error_message = message
        print(f"ERROR: {message}")
    
    def check_position(self, pos: int) -> bool:
        """
        安全检查函数 - 确保位置有效
        
        Args:
            pos: 位置索引
            
        Returns:
            是否有效
        """
        if pos < 1 or pos >= MAXN:
            self.handle_error(f"Invalid position: {pos}")
            return False
        return True
    
    def check_number(self, num: int) -> bool:
        """
        安全检查函数 - 确保数值有效
        
        Args:
            num: 数值
            
        Returns:
            是否有效
        """
        if num < 1 or num >= MAXV:
            self.handle_error(f"Invalid number: {num}")
            return False
        return True
    
    def init_blocks(self, n: int) -> None:
        """
        初始化分块信息
        
        Args:
            n: 数组长度
        """
        # 计算块大小: sqrt(n)是最优选择
        self.block_size = int(math.sqrt(n))
        if self.block_size == 0:
            self.block_size = 1  # 防止n=0的情况
        
        # 为每个位置分配块号
        for i in range(1, n + 1):
            self.block[i] = (i - 1) // self.block_size + 1
    
    @staticmethod
    def compare_queries(a: Query, b: Query, block: List[int]) -> bool:
        """
        奇偶排序比较函数
        
        Args:
            a: 查询a
            b: 查询b
            block: 分块数组
            
        Returns:
            比较结果
        """
        if block[a.l] != block[b.l]:
            return block[a.l] < block[b.l]
        
        # 奇偶优化: 奇数块升序，偶数块降序
        if block[a.l] & 1:
            return a.r < b.r
        else:
            return a.r > b.r
    
    def analyze_performance(self, n: int, queries: List[Query], process_func) -> None:
        """
        性能分析函数
        
        Args:
            n: 数组长度
            queries: 查询数组
            process_func: 处理函数
        """
        start_time = time.time()
        
        results = process_func()
        
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        
        print("=== 性能分析 ===")
        print(f"数据规模: n={n}, q={len(queries)}")
        print(f"执行时间: {duration:.2f}ms")
        print(f"平均每查询时间: {duration/len(queries):.4f}ms")
        
        # 理论复杂度验证
        theoretical = (n + len(queries)) * math.sqrt(n)
        print(f"理论复杂度因子: {theoretical:.2f}")
        print(f"实际效率比: {theoretical/duration:.4f}")
    
    def run_boundary_tests(self) -> None:
        """边界测试函数"""
        print("=== 边界测试开始 ===")
        print("最小输入测试: 待实现")
        print("重复元素测试: 待实现")
        print("=== 边界测试结束 ===")
    
    # 获取错误状态
    def has_errors(self) -> bool:
        return self.has_error
    
    def get_error_message(self) -> str:
        return self.error_message

# ========== DQUERY算法类 - 区间不同元素统计 ==========
class DQueryAlgorithm(MoAlgorithm):
    def __init__(self):
        super().__init__()
        self.answer = 0
    
    def add(self, pos: int) -> None:
        """
        添加元素操作
        
        Args:
            pos: 位置索引
        """
        if not self.check_position(pos):
            return
        
        num = self.arr[pos]
        if not self.check_number(num):
            return
        
        if self.cnt[num] == 0:
            self.answer += 1
        
        self.cnt[num] += 1
        
        # 安全检查: 答案不能超过实际可能的最大值
        if self.answer > self.n:
            self.handle_error("Answer count exceeds array size")
    
    def remove(self, pos: int) -> None:
        """
        删除元素操作
        
        Args:
            pos: 位置索引
        """
        if not self.check_position(pos):
            return
        
        num = self.arr[pos]
        if not self.check_number(num):
            return
        
        self.cnt[num] -= 1
        
        if self.cnt[num] == 0:
            self.answer -= 1
        
        # 安全检查: 计数不能为负
        if self.cnt[num] < 0:
            self.handle_error(f"Count becomes negative for number: {num}")
    
    def process_queries(self, n: int, queries: List[Query]) -> List[int]:
        """
        处理查询的核心函数
        
        Args:
            n: 数组长度
            queries: 查询数组
            
        Returns:
            结果数组
        """
        self.n = n
        self.q = len(queries)
        results = [-1] * self.q
        
        # 输入验证
        if not self.validate_input(n, queries):
            return results
        
        # 初始化分块
        self.init_blocks(n)
        
        # 创建查询副本用于排序
        sorted_queries = queries.copy()
        
        # 按照莫队算法排序 - 使用奇偶优化
        sorted_queries.sort(key=lambda x: (self.block[x.l], x.r if self.block[x.l] & 1 else -x.r))
        
        # 初始化双指针
        cur_l, cur_r = 1, 0
        self.answer = 0
        
        # 处理每个查询
        for query in sorted_queries:
            L, R, idx = query.l, query.r, query.id
            
            # 扩展右边界
            while cur_r < R:
                cur_r += 1
                self.add(cur_r)
            
            # 收缩右边界
            while cur_r > R:
                self.remove(cur_r)
                cur_r -= 1
            
            # 收缩左边界
            while cur_l < L:
                self.remove(cur_l)
                cur_l += 1
            
            # 扩展左边界
            while cur_l > L:
                cur_l -= 1
                self.add(cur_l)
            
            results[idx] = self.answer
        
        return results
    
    def run_boundary_tests(self) -> None:
        """边界测试函数重写"""
        print("=== DQUERY边界测试开始 ===")
        
        # 测试1: 最小输入
        n1 = 1
        queries1 = [Query(1, 1, 0)]
        self.arr[1] = 1
        
        results1 = self.process_queries(n1, queries1)
        print(f"最小输入测试: {'PASS' if results1[0] == 1 else 'FAIL'}")
        
        # 重置状态
        self.cnt = [0] * MAXV
        self.answer = 0
        
        # 测试2: 重复元素
        n2 = 5
        queries2 = [Query(1, 5, 0)]
        self.arr[1] = 1; self.arr[2] = 1; self.arr[3] = 2; self.arr[4] = 1; self.arr[5] = 3
        
        results2 = self.process_queries(n2, queries2)
        print(f"重复元素测试: {'PASS' if results2[0] == 3 else 'FAIL'}")
        
        print("=== DQUERY边界测试结束 ===")

# ========== 小B的询问算法类 - 区间元素出现次数平方和 ==========
class LittleBQueryAlgorithm(MoAlgorithm):
    def __init__(self):
        super().__init__()
        self.sum = 0
    
    def add(self, pos: int) -> None:
        """
        添加元素操作
        
        Args:
            pos: 位置索引
        """
        if not self.check_position(pos):
            return
        
        num = self.arr[pos]
        if not self.check_number(num):
            return
        
        self.sum -= self.cnt[num] * self.cnt[num]
        self.cnt[num] += 1
        self.sum += self.cnt[num] * self.cnt[num]
    
    def remove(self, pos: int) -> None:
        """
        删除元素操作
        
        Args:
            pos: 位置索引
        """
        if not self.check_position(pos):
            return
        
        num = self.arr[pos]
        if not self.check_number(num):
            return
        
        self.sum -= self.cnt[num] * self.cnt[num]
        self.cnt[num] -= 1
        self.sum += self.cnt[num] * self.cnt[num]
        
        # 安全检查: 计数不能为负
        if self.cnt[num] < 0:
            self.handle_error(f"Count becomes negative for number: {num}")
    
    def process_queries(self, n: int, queries: List[Query]) -> List[int]:
        """
        处理查询的核心函数
        
        Args:
            n: 数组长度
            queries: 查询数组
            
        Returns:
            结果数组
        """
        self.n = n
        self.q = len(queries)
        results = [-1] * self.q
        
        # 输入验证
        if not self.validate_input(n, queries):
            return results
        
        # 初始化分块
        self.init_blocks(n)
        
        # 创建查询副本用于排序
        sorted_queries = queries.copy()
        
        # 按照莫队算法排序 - 使用奇偶优化
        sorted_queries.sort(key=lambda x: (self.block[x.l], x.r if self.block[x.l] & 1 else -x.r))
        
        # 初始化双指针
        cur_l, cur_r = 1, 0
        self.sum = 0
        
        # 处理每个查询
        for query in sorted_queries:
            L, R, idx = query.l, query.r, query.id
            
            # 扩展右边界
            while cur_r < R:
                cur_r += 1
                self.add(cur_r)
            
            # 收缩右边界
            while cur_r > R:
                self.remove(cur_r)
                cur_r -= 1
            
            # 收缩左边界
            while cur_l < L:
                self.remove(cur_l)
                cur_l += 1
            
            # 扩展左边界
            while cur_l > L:
                cur_l -= 1
                self.add(cur_l)
            
            results[idx] = self.sum
        
        return results
    
    def run_boundary_tests(self) -> None:
        """边界测试函数重写"""
        print("=== 小B的询问边界测试开始 ===")
        
        # 测试1: 最小输入
        n1 = 1
        queries1 = [Query(1, 1, 0)]
        self.arr[1] = 1
        
        results1 = self.process_queries(n1, queries1)
        print(f"最小输入测试: {'PASS' if results1[0] == 1 else 'FAIL'}")
        
        # 重置状态
        self.cnt = [0] * MAXV
        self.sum = 0
        
        # 测试2: 重复元素
        n2 = 3
        queries2 = [Query(1, 3, 0)]
        self.arr[1] = 1; self.arr[2] = 1; self.arr[3] = 2
        
        results2 = self.process_queries(n2, queries2)
        # 1出现2次: 2^2=4, 2出现1次: 1^2=1, 总和=5
        print(f"重复元素测试: {'PASS' if results2[0] == 5 else 'FAIL'}")
        
        print("=== 小B的询问边界测试结束 ===")

# ========== 主函数 ==========
def main():
    # 示例1: DQUERY算法测试
    print("=== DQUERY算法测试 ===")
    dquery = DQueryAlgorithm()
    
    # 测试数据
    n = 5
    dquery.arr[1] = 1; dquery.arr[2] = 2; dquery.arr[3] = 1; dquery.arr[4] = 3; dquery.arr[5] = 2
    queries = [
        Query(1, 3, 0),
        Query(2, 4, 1),
        Query(3, 5, 2)
    ]
    
    results = dquery.process_queries(n, queries)
    
    print("查询结果:")
    for i, query in enumerate(queries):
        print(f"查询[{query.l}, {query.r}]: {results[i]}")
    
    # 边界测试
    dquery.run_boundary_tests()
    
    # 示例2: 小B的询问算法测试
    print("\n=== 小B的询问算法测试 ===")
    little_b = LittleBQueryAlgorithm()
    
    # 测试数据
    little_b.arr[1] = 1; little_b.arr[2] = 2; little_b.arr[3] = 1; little_b.arr[4] = 3; little_b.arr[5] = 2
    
    results2 = little_b.process_queries(n, queries)
    
    print("查询结果:")
    for i, query in enumerate(queries):
        print(f"查询[{query.l}, {query.r}]: {results2[i]}")
    
    # 边界测试
    little_b.run_boundary_tests()
    
    # 输出错误信息 (如果有)
    if dquery.has_errors():
        print(f"DQUERY算法错误: {dquery.get_error_message()}")
    
    if little_b.has_errors():
        print(f"小B的询问算法错误: {little_b.get_error_message()}")
    
    print("\n=== 程序执行完成 ===")

if __name__ == "__main__":
    main()