#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Codeforces 1009F. Dominant Indices - Python实现（长链剖分经典题目）
题目链接：https://codeforces.com/problemset/problem/1009/F

题目描述：
给定一棵有根树，根节点为1。对于每个节点u，定义d(u,x)为u的子树中深度为x的节点数量。
对于每个节点u，需要找到一个最小的x，使得d(u,x)最大。

算法思路：
1. 长链剖分：将树分解为长链，优化空间和时间复杂度
2. 树上启发式合并：利用长链剖分的性质，实现O(n)时间复杂度的算法
3. 深度统计：对于每个节点，统计其子树中各个深度的节点数量

时间复杂度：O(n) - 每个节点处理一次
空间复杂度：O(n) - 使用长链剖分优化空间

最优解验证：这是最优解，长链剖分是解决此类问题的标准方法
"""

import sys
from typing import List

class Solution:
    """Dominant Indices解决方案类"""
    
    def solve(self, edges: List[List[int]], n: int) -> List[int]:
        """
        解决Dominant Indices问题
        
        Args:
            edges: 树的边列表，节点编号从1开始
            n: 节点数量
            
        Returns:
            List[int]: 每个节点的答案数组
        """
        # 构建树
        tree = [[] for _ in range(n + 1)]
        for u, v in edges:
            tree[u].append(v)
            tree[v].append(u)
        
        # 初始化数组
        depth = [0] * (n + 1)
        son = [0] * (n + 1)  # 重儿子
        length = [0] * (n + 1)  # 链长度
        ans = [0] * (n + 1)  # 答案数组
        cnt = [0] * (n + 2)  # 深度计数
        
        def dfs1(u: int, parent: int):
            """第一次DFS：预处理深度、重儿子、链长度"""
            depth[u] = depth[parent] + 1
            length[u] = 1
            son[u] = 0
            
            for v in tree[u]:
                if v == parent:
                    continue
                dfs1(v, u)
                
                # 更新链长度和重儿子
                if length[v] + 1 > length[u]:
                    length[u] = length[v] + 1
                    son[u] = v
        
        def dfs2(u: int, parent: int):
            """第二次DFS：长链剖分，计算答案"""
            # 如果有重儿子，先处理重儿子（继承重儿子的信息）
            if son[u] != 0:
                dfs2(son[u], u)
                ans[u] = ans[son[u]] + 1  # 继承重儿子的答案
            
            # 处理当前节点的深度
            cnt[depth[u]] += 1
            if (cnt[depth[u]] > cnt[ans[u] + depth[u]] or 
                (cnt[depth[u]] == cnt[ans[u] + depth[u]] and depth[u] < ans[u] + depth[u])):
                ans[u] = 0  # 当前深度更优
            
            # 处理轻儿子
            for v in tree[u]:
                if v == parent or v == son[u]:
                    continue
                dfs2(v, u)
                
                # 合并轻儿子的信息
                for d in range(depth[v], depth[v] + length[v]):
                    cnt[d] += 1
                    if (cnt[d] > cnt[ans[u] + depth[u]] or 
                        (cnt[d] == cnt[ans[u] + depth[u]] and d < ans[u] + depth[u])):
                        ans[u] = d - depth[u]
        
        # 第一次DFS：计算深度、重儿子、链长度
        dfs1(1, 0)
        
        # 第二次DFS：长链剖分，计算答案
        dfs2(1, 0)
        
        return ans[1:n+1]

def run_tests():
    """运行测试用例验证算法正确性"""
    solution = Solution()
    
    print("=== Codeforces 1009F. Dominant Indices测试 ===")
    
    # 测试用例1：链状树
    n1 = 5
    edges1 = [
        [1, 2], [2, 3], [3, 4], [4, 5]
    ]
    result1 = solution.solve(edges1, n1)
    print(f"测试用例1 - 链状树: {result1}")
    
    # 测试用例2：星形树
    n2 = 5
    edges2 = [
        [1, 2], [1, 3], [1, 4], [1, 5]
    ]
    result2 = solution.solve(edges2, n2)
    print(f"测试用例2 - 星形树: {result2}")
    
    # 测试用例3：完全二叉树
    n3 = 7
    edges3 = [
        [1, 2], [1, 3], [2, 4], [2, 5], [3, 6], [3, 7]
    ]
    result3 = solution.solve(edges3, n3)
    print(f"测试用例3 - 完全二叉树: {result3}")
    
    print("=== 所有测试用例执行完成！ ===")

if __name__ == "__main__":
    run_tests()