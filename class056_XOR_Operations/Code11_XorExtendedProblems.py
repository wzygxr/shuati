#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
异或运算扩展题目实现 (Python版本)

本文件包含来自各大算法平台的异或运算题目，包括Codeforces、AtCoder、SPOJ、POJ等
每个题目都有详细的解题思路、复杂度分析和工程化考量
"""

class TrieNode:
    """前缀树节点类"""
    def __init__(self):
        self.children = {}  # 子节点字典
        self.count = 0      # 通过该节点的路径数

class Code11_XorExtendedProblems:
    """
    异或运算扩展题目实现类
    """
    
    @staticmethod
    def little_girl_max_xor(l: int, r: int) -> int:
        """
        题目1: Little Girl and Maximum XOR (Codeforces 276D)
        
        题目来源: Codeforces 276D
        链接: https://codeforces.com/problemset/problem/276/D
        
        题目描述:
        给定两个整数l和r (0 ≤ l ≤ r ≤ 10^18)，找到两个数a, b (l ≤ a ≤ b ≤ r)，
        使得a XOR b的值最大。
        
        解题思路:
        1. 找到l和r二进制表示中第一个不同的位
        2. 从该位开始，后面的所有位都可以设为1
        3. 最大异或值就是(1 << (第一个不同位的位置+1)) - 1
        
        时间复杂度: O(log(max(l, r)))
        空间复杂度: O(1)
        
        工程化考量:
        - 使用Python的int类型处理大数
        - 处理l == r的特殊情况
        - 边界条件检查
        
        Args:
            l: 区间左边界
            r: 区间右边界
            
        Returns:
            最大异或值
        """
        # 特殊情况处理
        if l == r:
            return 0
        
        # 找到第一个不同的位
        xor_val = l ^ r
        # 找到最高位的1
        highest_bit = 1 << (xor_val.bit_length() - 1)
        
        # 构造最大异或值：从最高不同位开始后面全为1
        result = (highest_bit << 1) - 1
        return result
    
    @staticmethod
    def xor_favorite_number(arr: list[int], k: int, queries: list[list[int]]) -> list[int]:
        """
        题目2: XOR and Favorite Number (Codeforces 617E)
        
        题目来源: Codeforces 617E
        链接: https://codeforces.com/problemset/problem/617/E
        
        题目描述:
        给定一个数组a和整数k，以及多个查询[l, r]，
        对于每个查询，统计子数组a[l...r]中有多少个子数组的异或值等于k。
        
        解题思路:
        使用莫队算法(Mo's Algorithm)：
        1. 计算前缀异或数组prefix
        2. 子数组a[i...j]的异或值 = prefix[j] ^ prefix[i-1]
        3. 使用莫队算法处理区间查询
        
        时间复杂度: O((n + q) * √n)
        空间复杂度: O(n + MAX_VALUE)
        
        工程化考量:
        - 使用字典记录频率，避免数组越界
        - 分块大小选择√n
        - 处理大数据量的性能优化
        
        Args:
            arr: 输入数组
            k: 目标异或值
            queries: 查询数组
            
        Returns:
            每个查询的结果
        """
        n = len(arr)
        q = len(queries)
        
        # 计算前缀异或数组
        prefix = [0] * (n + 1)
        for i in range(1, n + 1):
            prefix[i] = prefix[i - 1] ^ arr[i - 1]
        
        # 莫队算法：对查询排序
        block_size = int(n ** 0.5)
        indexed_queries = []
        for i, query in enumerate(queries):
            l, r = query
            indexed_queries.append((l, r, i))
        
        # 按照块排序
        indexed_queries.sort(key=lambda x: (x[0] // block_size, x[1]))
        
        result = [0] * q
        freq = {}
        current_l, current_r = 0, -1
        current_count = 0
        
        # 初始状态：空前缀
        freq[0] = 1
        
        for l, r, idx in indexed_queries:
            # 移动左指针
            while current_l < l:
                xor_value = prefix[current_l]
                freq[xor_value] -= 1
                current_count -= freq.get(xor_value ^ k, 0)
                current_l += 1
            
            while current_l > l:
                current_l -= 1
                xor_value = prefix[current_l]
                current_count += freq.get(xor_value ^ k, 0)
                freq[xor_value] = freq.get(xor_value, 0) + 1
            
            # 移动右指针
            while current_r < r:
                current_r += 1
                xor_value = prefix[current_r + 1]
                current_count += freq.get(xor_value ^ k, 0)
                freq[xor_value] = freq.get(xor_value, 0) + 1
            
            while current_r > r:
                xor_value = prefix[current_r + 1]
                freq[xor_value] -= 1
                current_count -= freq.get(xor_value ^ k, 0)
                current_r -= 1
            
            result[idx] = current_count
        
        return result
    
    @staticmethod
    def xor_longest_path(n: int, edges: list[tuple[int, int, int]]) -> int:
        """
        题目3: The XOR-longest Path (POJ 3764)
        
        题目来源: POJ 3764
        链接: http://poj.org/problem?id=3764
        
        题目描述:
        给定一棵带权树，每条边有一个权值。找到树中最长的一条路径，使得路径上的边权异或值最大。
        
        解题思路:
        1. 计算每个节点到根节点的路径异或值xorPath[u]
        2. 任意两点u和v之间的路径异或值 = xorPath[u] ^ xorPath[v]
        3. 问题转化为在xorPath数组中找出两个数，使得它们的异或值最大
        4. 使用前缀树(Trie)解决最大异或对问题
        
        时间复杂度: O(n * 32)
        空间复杂度: O(n * 32)
        
        工程化考量:
        - 使用邻接表存储树结构
        - 深度优先搜索计算路径异或值
        - 前缀树优化最大异或查询
        
        Args:
            n: 节点数量
            edges: 边列表，每个边为[u, v, weight]
            
        Returns:
            最长异或路径的值
        """
        # 构建邻接表
        graph = [[] for _ in range(n)]
        for u, v, w in edges:
            graph[u].append((v, w))
            graph[v].append((u, w))
        
        # 计算每个节点到根节点(0)的路径异或值
        xor_path = [0] * n
        visited = [False] * n
        
        def dfs(node, parent, current_xor):
            visited[node] = True
            xor_path[node] = current_xor
            for neighbor, weight in graph[node]:
                if neighbor != parent and not visited[neighbor]:
                    dfs(neighbor, node, current_xor ^ weight)
        
        dfs(0, -1, 0)
        
        # 使用前缀树找到最大异或对
        root = TrieNode()
        max_xor = 0
        
        def insert_to_trie(num):
            node = root
            for i in range(31, -1, -1):
                bit = (num >> i) & 1
                if bit not in node.children:
                    node.children[bit] = TrieNode()
                node = node.children[bit]
        
        def query_max_xor(num):
            node = root
            max_val = 0
            for i in range(31, -1, -1):
                bit = (num >> i) & 1
                desired_bit = 1 - bit
                if desired_bit in node.children:
                    max_val |= (1 << i)
                    node = node.children[desired_bit]
                else:
                    node = node.children[bit]
            return max_val
        
        for i in range(n):
            insert_to_trie(xor_path[i])
            max_xor = max(max_xor, query_max_xor(xor_path[i]))
        
        return max_xor
    
    @staticmethod
    def sum_vs_xor(n: int) -> int:
        """
        题目4: Sum vs XOR (HackerRank)
        
        题目来源: HackerRank - Sum vs XOR
        链接: https://www.hackerrank.com/challenges/sum-vs-xor/problem
        
        题目描述:
        给定一个整数n，找出非负整数x的个数，使得x + n == x ^ n。
        
        解题思路:
        数学分析：x + n = x ^ n 当且仅当 x & n = 0
        即x和n在二进制表示中没有重叠的1位。
        1. 计算n的二进制表示中0的个数count
        2. 答案就是2^count
        
        时间复杂度: O(log n)
        空间复杂度: O(1)
        
        工程化考量:
        - 处理n=0的特殊情况
        - 使用Python的int类型处理大数
        - 位运算优化
        
        Args:
            n: 输入整数
            
        Returns:
            满足条件的x的个数
        """
        if n == 0:
            return 1  # 任何x都满足
        
        # 计算n的二进制表示中0的个数
        count_zeros = 0
        temp = n
        while temp > 0:
            if (temp & 1) == 0:
                count_zeros += 1
            temp >>= 1
        
        return 1 << count_zeros

# 测试代码
if __name__ == "__main__":
    # 测试 little_girl_max_xor
    print("Little Girl Max XOR (1, 10):", Code11_XorExtendedProblems.little_girl_max_xor(1, 10))
    
    # 测试 sum_vs_xor
    print("Sum vs XOR (5):", Code11_XorExtendedProblems.sum_vs_xor(5))
    
    # 测试 xor_longest_path (模拟数据)
    edges = [(0, 1, 3), (0, 2, 5), (1, 3, 2), (1, 4, 1)]
    print("XOR Longest Path:", Code11_XorExtendedProblems.xor_longest_path(5, edges))