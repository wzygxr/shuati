#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
后缀数组（Suffix Array）实现
包含：
1. 倍增法构造后缀数组 O(n log n)
2. 计算height数组 O(n)
3. ST表预处理用于LCP查询 O(n log n) 预处理，O(1) 查询
时间复杂度：构造O(n log n)，查询O(1)
空间复杂度：O(n log n)
"""

import math
from typing import List, Tuple

class SuffixArray:
    def __init__(self, text: str):
        """
        构造函数，构建后缀数组
        
        Args:
            text (str): 输入文本
            
        Raises:
            ValueError: 如果输入文本为None
        """
        if text is None:
            raise ValueError("输入文本不能为None")
        
        self.text = text
        self.n = len(text)
        self.suffix_array = []  # 后缀数组，存储排序后的后缀起始位置
        self.rank = []         # rank[i]表示起始位置为i的后缀的排名
        self.height = []       # height[i]表示后缀数组中第i个和第i-1个后缀的LCP
        self.st_table = []     # ST表，用于LCP区间查询
        self.log_n = 0         # log2(n)的上界
        
        # 构造后缀数组
        self._build_suffix_array()
        
        # 计算height数组
        self._compute_height()
        
        # 构建ST表
        self._build_st()
    
    def _build_suffix_array(self) -> None:
        """
        使用倍增法构建后缀数组
        """
        # 初始化
        self.suffix_array = list(range(self.n))
        self.rank = [ord(c) for c in self.text]  # 初始排名为字符的ASCII值
        
        # 倍增排序
        k = 1
        while k < self.n:
            # 按照当前排名和k位置后的排名进行稳定排序
            # 构建二元组 (rank[i], rank[i+k] if i+k < n else -1)
            def compare_suffix(i: int) -> Tuple[int, int]:
                return (self.rank[i], self.rank[i + k] if i + k < self.n else -1)
            
            # 排序
            self.suffix_array.sort(key=compare_suffix)
            
            # 更新排名
            new_rank = [0] * self.n
            new_rank[self.suffix_array[0]] = 0
            for i in range(1, self.n):
                # 如果当前后缀与前一个后缀的排名相同，则给予相同的排名
                prev = self.suffix_array[i-1]
                curr = self.suffix_array[i]
                if (self.rank[prev] == self.rank[curr] and 
                    compare_suffix(prev)[1] == compare_suffix(curr)[1]):
                    new_rank[curr] = new_rank[prev]
                else:
                    new_rank[curr] = new_rank[prev] + 1
            
            # 更新rank数组
            self.rank = new_rank
            
            # 倍增步长
            k *= 2
    
    def _compute_height(self) -> None:
        """
        计算height数组
        利用性质：height[rank[i]] >= height[rank[i-1]] - 1
        """
        self.height = [0] * self.n
        # 构建rank到suffix的映射
        rank_to_suffix = [0] * self.n
        for i in range(self.n):
            rank_to_suffix[self.rank[i]] = i
        
        k = 0  # 当前LCP长度
        for i in range(self.n):
            r = self.rank[i]
            if r == 0:
                self.height[r] = 0  # 排名为0的后缀没有前一个后缀
                continue
            
            # 获取前一个排名的后缀起始位置
            j = rank_to_suffix[r - 1]
            
            # 从上一轮的k-1开始比较（利用性质优化）
            if k > 0:
                k -= 1
            
            # 扩展LCP
            while (i + k < self.n and j + k < self.n and 
                   self.text[i + k] == self.text[j + k]):
                k += 1
            
            self.height[r] = k
    
    def _build_st(self) -> None:
        """
        构建ST表用于RMQ（区间最小值查询）
        """
        # 计算log2(n)的上界
        self.log_n = 0
        temp = 1
        while temp * 2 <= self.n:
            temp *= 2
            self.log_n += 1
        
        # 初始化ST表
        self.st_table = [[0] * self.n for _ in range(self.log_n + 1)]
        
        # 填充第0层（原始height数组）
        for i in range(self.n):
            self.st_table[0][i] = self.height[i]
        
        # 构建其余层
        for k in range(1, self.log_n + 1):
            for i in range(self.n - (1 << k) + 1):
                # st_table[k][i] = min(st_table[k-1][i], st_table[k-1][i + (1 << (k-1))])
                self.st_table[k][i] = min(
                    self.st_table[k-1][i],
                    self.st_table[k-1][i + (1 << (k-1))]
                )
    
    def _query_min(self, l: int, r: int) -> int:
        """
        计算区间[l, r]的最小值
        
        Args:
            l (int): 左边界（包含）
            r (int): 右边界（包含）
            
        Returns:
            int: 区间最小值
        """
        if l > r:
            l, r = r, l
        
        # 计算区间长度的对数
        len_range = r - l + 1
        k = 0
        while (1 << (k + 1)) <= len_range:
            k += 1
        
        # 查询最小值
        return min(
            self.st_table[k][l],
            self.st_table[k][r - (1 << k) + 1]
        )
    
    def get_lcp(self, i: int, j: int) -> int:
        """
        计算两个后缀的最长公共前缀（LCP）
        
        Args:
            i (int): 第一个后缀的起始位置
            j (int): 第二个后缀的起始位置
            
        Returns:
            int: LCP长度
            
        Raises:
            IndexError: 如果后缀起始位置超出范围
        """
        if i < 0 or i >= self.n or j < 0 or j >= self.n:
            raise IndexError("后缀起始位置超出范围")
        
        if i == j:
            return self.n - i  # 同一个后缀，LCP就是后缀长度
        
        # 获取两个后缀的排名
        r1 = self.rank[i]
        r2 = self.rank[j]
        
        # 确保r1 < r2
        if r1 > r2:
            r1, r2 = r2, r1
        
        # 后缀排序中，LCP(r1, r2) = min{height[r1+1], height[r1+2], ..., height[r2]}
        return self._query_min(r1 + 1, r2)
    
    def get_suffix_array(self) -> List[int]:
        """
        获取后缀数组
        
        Returns:
            List[int]: 后缀数组的拷贝
        """
        return self.suffix_array.copy()
    
    def get_height_array(self) -> List[int]:
        """
        获取height数组
        
        Returns:
            List[int]: height数组的拷贝
        """
        return self.height.copy()
    
    def get_rank_array(self) -> List[int]:
        """
        获取rank数组
        
        Returns:
            List[int]: rank数组的拷贝
        """
        return self.rank.copy()
    
    def find_pattern(self, pattern: str) -> List[int]:
        """
        查找模式串在文本串中所有出现的位置
        
        Args:
            pattern (str): 模式串
            
        Returns:
            List[int]: 所有匹配位置的起始索引列表
            
        Raises:
            ValueError: 如果模式串为None
        """
        if pattern is None:
            raise ValueError("模式串不能为None")
        
        m = len(pattern)
        if m == 0:
            # 空模式串匹配所有位置
            return list(range(self.n + 1))
        
        if m > self.n:
            return []  # 无匹配
        
        # 使用二分查找找到第一个匹配的位置
        left, right = 0, self.n - 1
        first_pos = -1
        
        while left <= right:
            mid = (left + right) // 2
            cmp = self._compare_suffix_with_pattern(self.suffix_array[mid], pattern)
            
            if cmp >= 0:
                if cmp == 0:
                    first_pos = mid
                right = mid - 1
            else:
                left = mid + 1
        
        if first_pos == -1:
            return []  # 无匹配
        
        # 找到最后一个匹配的位置
        left, right = first_pos, self.n - 1
        last_pos = first_pos
        
        while left <= right:
            mid = (left + right) // 2
            if self._compare_suffix_with_pattern(self.suffix_array[mid], pattern) == 0:
                last_pos = mid
                left = mid + 1
            else:
                right = mid - 1
        
        # 收集所有匹配位置
        positions = [self.suffix_array[i] for i in range(first_pos, last_pos + 1)]
        
        # 按位置排序
        positions.sort()
        
        return positions
    
    def _compare_suffix_with_pattern(self, pos: int, pattern: str) -> int:
        """
        比较以pos开始的后缀和模式串
        
        Args:
            pos (int): 后缀起始位置
            pattern (str): 模式串
            
        Returns:
            int: 比较结果，-1表示后缀小，0表示相等，1表示后缀大
        """
        m = len(pattern)
        for i in range(m):
            if pos + i >= self.n:
                # 后缀已结束，模式串未结束，后缀小
                return -1
            c1 = self.text[pos + i]
            c2 = pattern[i]
            if c1 != c2:
                return -1 if c1 < c2 else 1
        # 前缀相同，说明匹配
        return 0
    
    def count_distinct_substrings(self) -> int:
        """
        计算文本中的不同子串数量
        利用后缀数组性质：不同子串数量 = n(n+1)/2 - Σheight[i]
        
        Returns:
            int: 不同子串数量
        """
        total = self.n * (self.n + 1) // 2
        sum_height = sum(self.height)
        return total - sum_height


# 测试代码
if __name__ == "__main__":
    # 测试用例1：基本功能测试
    text1 = "banana"
    sa1 = SuffixArray(text1)
    print("=== 测试用例1 ===")
    print(f"文本: {text1}")
    print(f"后缀数组: {sa1.get_suffix_array()}")
    print(f"排名数组: {sa1.get_rank_array()}")
    print(f"Height数组: {sa1.get_height_array()}")
    
    # 测试LCP查询
    print(f"LCP(1, 3) (ana和ana): {sa1.get_lcp(1, 3)}")  # 应该是3
    print(f"LCP(0, 2) (banana和nana): {sa1.get_lcp(0, 2)}")  # 应该是0
    
    # 测试模式匹配
    matches1 = sa1.find_pattern("ana")
    print(f"查找模式'ana': {matches1}")  # 应该是[1, 3]
    
    # 测试不同子串数量
    print(f"不同子串数量: {sa1.count_distinct_substrings()}")  # 应该是15
    
    # 测试用例2：边界情况
    text2 = "aaa"
    sa2 = SuffixArray(text2)
    print("\n=== 测试用例2 ===")
    print(f"文本: {text2}")
    print(f"不同子串数量: {sa2.count_distinct_substrings()}")  # 应该是3
    
    # 测试用例3：更长文本
    text3 = "mississippi"
    sa3 = SuffixArray(text3)
    print("\n=== 测试用例3 ===")
    print(f"文本: {text3}")
    print(f"LCP(1, 4) (issi和ippi): {sa3.get_lcp(1, 4)}")
    matches3 = sa3.find_pattern("issi")
    print(f"查找模式'issi': {matches3}")