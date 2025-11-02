#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
后缀数组和后缀自动机实现 (Python版本)

包括后缀数组(SA)、最长公共前缀(LCP)、后缀自动机(SAM)等实现

后缀结构在字符串处理中有着广泛的应用，包括：
1. 字符串匹配
2. 最长重复子串查找
3. 字典序排序
4. 生物信息学中的序列分析
"""

from typing import List, Dict, Tuple
import math

class SuffixStructuresAdvanced:
    """后缀结构实现"""
    
    class SuffixArray:
        """后缀数组实现"""
        
        def __init__(self, s: str):
            """
            初始化后缀数组
            :param s: 输入字符串
            """
            self.s = s + "$"  # 添加终止符
            self.sa = []      # 后缀数组
            self.rank = []    # 排名数组
            self.height = []  # LCP数组
            
            self._build_suffix_array()
            self._build_lcp()
        
        def _build_suffix_array(self) -> None:
            """构建后缀数组（使用倍增算法）"""
            n = len(self.s)
            self.sa = list(range(n))
            self.rank = [ord(c) for c in self.s]
            tmp = [0] * n
            
            # 倍增排序
            k = 1
            while k < n:
                # 定义排序键函数
                def key_func(i):
                    return (self.rank[i], self.rank[i + k] if i + k < n else -1)
                
                # 按键值排序
                self.sa.sort(key=key_func)
                
                # 更新rank数组
                tmp[self.sa[0]] = 0
                for i in range(1, n):
                    tmp[self.sa[i]] = tmp[self.sa[i-1]] + (1 if key_func(self.sa[i-1]) != key_func(self.sa[i]) else 0)
                
                self.rank, tmp = tmp, self.rank
                k <<= 1
                
                if self.rank[self.sa[-1]] == n - 1:
                    break
        
        def _build_lcp(self) -> None:
            """构建LCP数组（使用Kasai算法）"""
            n = len(self.s)
            self.height = [0] * n
            inv = [0] * n
            
            # 计算rank的逆数组
            for i in range(n):
                inv[self.sa[i]] = i
            
            # Kasai算法
            k = 0
            for i in range(n):
                if inv[i] == n - 1:
                    k = 0
                    continue
                
                j = self.sa[inv[i] + 1]
                while i + k < n and j + k < n and self.s[i + k] == self.s[j + k]:
                    k += 1
                
                self.height[inv[i]] = k
                if k > 0:
                    k -= 1
        
        def get_sa(self) -> List[int]:
            """
            获取后缀数组
            :return: 后缀数组
            """
            return self.sa[:]
        
        def get_rank(self) -> List[int]:
            """
            获取排名数组
            :return: 排名数组
            """
            return self.rank[:]
        
        def get_lcp(self) -> List[int]:
            """
            获取LCP数组
            :return: LCP数组
            """
            return self.height[:]
        
        class LCP_RMQ:
            """使用RMQ维护LCP数组查询区间最小值"""
            
            def __init__(self, arr: List[int]):
                """
                初始化RMQ
                :param arr: 输入数组
                """
                self.n = len(arr)
                self.logn = self.n.bit_length()
                self.st = [[0] * self.logn for _ in range(self.n)]
                
                # 初始化
                for i in range(self.n):
                    self.st[i][0] = arr[i]
                
                # 构建Sparse Table
                j = 1
                while (1 << j) <= self.n:
                    i = 0
                    while i + (1 << j) <= self.n:
                        self.st[i][j] = min(self.st[i][j-1], self.st[i + (1 << (j-1))][j-1])
                        i += 1
                    j += 1
            
            def query(self, l: int, r: int) -> int:
                """
                查询区间[l, r]的最小值
                :param l: 左边界
                :param r: 右边界
                :return: 区间最小值
                """
                if l > r:
                    return 0
                k = (r - l + 1).bit_length() - 1
                return min(self.st[l][k], self.st[r - (1 << k) + 1][k])
    
    class SuffixAutomaton:
        """后缀自动机实现"""
        
        class State:
            """状态类"""
            
            def __init__(self):
                self.len = 0                    # 从初始状态到当前状态的最长字符串长度
                self.link = -1                  # 后缀链接
                self.next: Dict[str, int] = {}  # 转移函数
                self.end_pos_size = 0           # right集合大小
                self.is_clone = False           # 是否为克隆节点
        
        def __init__(self, s: str):
            """
            初始化后缀自动机
            :param s: 输入字符串
            """
            self.states: List[SuffixStructuresAdvanced.SuffixAutomaton.State] = []
            self.states.append(SuffixStructuresAdvanced.SuffixAutomaton.State())  # 初始状态
            self.last = 0
            
            # 逐个字符构建SAM
            for c in s:
                self._extend(c)
        
        def _extend(self, c: str) -> None:
            """
            扩展SAM
            :param c: 字符
            """
            cur = len(self.states)
            state = SuffixStructuresAdvanced.SuffixAutomaton.State()
            state.len = self.states[self.last].len + 1
            state.end_pos_size = 1
            self.states.append(state)
            
            p = self.last
            # 更新转移函数
            while p != -1 and c not in self.states[p].next:
                self.states[p].next[c] = cur
                p = self.states[p].link
            
            if p == -1:
                self.states[cur].link = 0
            else:
                q = self.states[p].next[c]
                if self.states[p].len + 1 == self.states[q].len:
                    self.states[cur].link = q
                else:
                    clone = len(self.states)
                    cloned_state = SuffixStructuresAdvanced.SuffixAutomaton.State()
                    cloned_state.len = self.states[p].len + 1
                    cloned_state.next = self.states[q].next.copy()
                    cloned_state.link = self.states[q].link
                    cloned_state.is_clone = True
                    self.states.append(cloned_state)
                    
                    while p != -1 and self.states[p].next.get(c) == q:
                        self.states[p].next[c] = clone
                        p = self.states[p].link
                    
                    self.states[q].link = clone
                    self.states[cur].link = clone
            
            self.last = cur
        
        def calculate_end_pos_size(self) -> None:
            """计算每个状态的right集合大小"""
            # 按长度降序排列状态
            order = list(range(len(self.states)))
            order.sort(key=lambda x: self.states[x].len, reverse=True)
            
            # 按逆拓扑序更新end_pos_size
            for x in order:
                if self.states[x].link != -1:
                    self.states[self.states[x].link].end_pos_size += self.states[x].end_pos_size
        
        def count_distinct_substrings(self) -> int:
            """
            子串计数DP边界处理
            :return: 不同子串数量
            """
            count = 0
            for i in range(1, len(self.states)):
                count += self.states[i].len - self.states[self.states[i].link].len
            return count
        
        def get_state_count(self) -> int:
            """
            获取状态数量
            :return: 状态数量
            """
            return len(self.states)
        
        def is_substring(self, t: str) -> bool:
            """
            检查字符串是否为原字符串的子串
            :param t: 待检查字符串
            :return: 是否为子串
            """
            v = 0
            for c in t:
                if c not in self.states[v].next:
                    return False
                v = self.states[v].next[c]
            return True


def main():
    """测试方法"""
    # 测试后缀数组
    print("=== 后缀数组测试 ===")
    text = "banana"
    sa = SuffixStructuresAdvanced.SuffixArray(text)
    
    print(f"原字符串: {text}")
    print(f"后缀数组: {sa.get_sa()}")
    print(f"排名数组: {sa.get_rank()}")
    print(f"LCP数组: {sa.get_lcp()}")
    
    # 测试LCP RMQ
    rmq = SuffixStructuresAdvanced.SuffixArray.LCP_RMQ(sa.get_lcp())
    print(f"LCP[1,3]区间最小值: {rmq.query(1, 3)}")
    
    # 测试后缀自动机
    print("\n=== 后缀自动机测试 ===")
    pattern = "abcbcba"
    sam = SuffixStructuresAdvanced.SuffixAutomaton(pattern)
    
    print(f"原字符串: {pattern}")
    print(f"状态数量: {sam.get_state_count()}")
    print(f"不同子串数量: {sam.count_distinct_substrings()}")
    
    # 测试子串查询
    print(f"是否包含'bc': {sam.is_substring('bc')}")
    print(f"是否包含'xyz': {sam.is_substring('xyz')}")
    
    # 计算right集合大小
    sam.calculate_end_pos_size()
    print("计算right集合大小完成")


if __name__ == "__main__":
    main()