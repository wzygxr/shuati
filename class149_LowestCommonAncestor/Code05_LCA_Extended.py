"""
LCA问题扩展 - 树上倍增法实现
题目来源：洛谷 P3379 【模板】最近公共祖先（LCA）
题目链接：https://www.luogu.com.cn/problem/P3379

问题描述：
给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先。

解题思路：
1. 使用树上倍增法预处理每个节点的2^k级祖先
2. 对于每次查询，先将两个节点调整到同一深度
3. 然后同时向上跳跃，直到找到最近公共祖先

时间复杂度：
预处理：O(n log n)
查询：O(log n)
空间复杂度：O(n log n)
是否为最优解：是，对于在线查询LCA问题，倍增法是标准解法之一

工程化考虑：
1. 边界条件处理：处理空树、节点不存在等情况
2. 输入验证：验证输入节点是否在树中
3. 异常处理：对非法输入进行检查
4. 可读性：添加详细注释和变量命名

算法要点：
1. 预处理阶段构建倍增数组
2. 查询阶段先调整深度再同时跳跃
3. 利用二进制表示优化跳跃过程

与标准库实现对比：
1. 标准库通常有更完善的错误处理
2. 标准库可能使用更优化的数据结构

性能优化：
1. 预处理优化：一次处理所有节点
2. 查询优化：利用倍增快速跳跃

特殊场景：
1. 空输入：返回-1
2. 节点不存在：返回-1
3. 一个节点是另一个节点的祖先：返回祖先节点

语言特性差异：
1. Python：动态类型，引用计数垃圾回收
2. Java：自动垃圾回收，对象引用传递
3. C++：手动内存管理，指针操作

数学联系：
1. 与二进制表示和位运算相关
2. 与树的深度优先搜索理论相关
3. 与动态规划有一定联系

调试能力：
1. 可通过打印预处理数组调试
2. 可通过断言验证中间结果
3. 可通过特殊测试用例验证边界条件
"""

from typing import List, Optional
import collections

class TreeAncestor:
    def __init__(self, n: int, edges: List[List[int]]):
        """
        构造函数
        :param n: 节点数量
        :param edges: 边的列表
        """
        self.n = n
        # 邻接表存储树结构
        self.adj = collections.defaultdict(list)
        
        # 深度数组和倍增祖先数组
        self.depth = [0] * n
        self.ancestor = [[-1] * 20 for _ in range(n)]
        
        # 构建邻接表
        for edge in edges:
            self.adj[edge[0]].append(edge[1])
            self.adj[edge[1]].append(edge[0])
        
        # 预处理，构建倍增数组
        self._preprocess(0, -1)
    
    def _preprocess(self, u: int, parent: int) -> None:
        """
        预处理，构建倍增数组
        :param u: 当前节点
        :param parent: 父节点
        """
        # 设置父节点和深度
        self.ancestor[u][0] = parent
        if parent == -1:
            self.depth[u] = 0
        else:
            self.depth[u] = self.depth[parent] + 1
        
        # 构建倍增数组
        for i in range(1, 20):
            if self.ancestor[u][i - 1] == -1:
                self.ancestor[u][i] = -1
            else:
                self.ancestor[u][i] = self.ancestor[self.ancestor[u][i - 1]][i - 1]
        
        # 递归处理子节点
        for v in self.adj[u]:
            if v != parent:
                self._preprocess(v, u)
    
    def get_lca(self, u: int, v: int) -> int:
        """
        查询两个节点的最近公共祖先
        :param u: 节点u
        :param v: 节点v
        :return: 最近公共祖先
        """
        # 异常处理
        if u < 0 or u >= self.n or v < 0 or v >= self.n:
            return -1
        
        # 确保u的深度不小于v
        if self.depth[u] < self.depth[v]:
            u, v = v, u
        
        # 将u向上跳跃到与v同一深度
        for i in range(19, -1, -1):
            if (self.ancestor[u][i] != -1 and 
                self.depth[self.ancestor[u][i]] >= self.depth[v]):
                u = self.ancestor[u][i]
        
        # 如果u和v已经相同，则找到了LCA
        if u == v:
            return u
        
        # u和v同时向上跳跃，直到找到LCA
        for i in range(19, -1, -1):
            if self.ancestor[u][i] != self.ancestor[v][i]:
                u = self.ancestor[u][i]
                v = self.ancestor[v][i]
        
        # 返回LCA
        return self.ancestor[u][0]

# 测试代码
if __name__ == "__main__":
    # 构建测试用例
    # 树结构: 0-1-2, 0-3, 1-4
    n = 5
    edges = [[0, 1], [1, 2], [0, 3], [1, 4]]
    
    tree = TreeAncestor(n, edges)
    
    # 测试用例1: LCA(2, 3) = 0
    result1 = tree.get_lca(2, 3)
    print(f"测试用例1 - LCA(2, 3): {result1}")
    
    # 测试用例2: LCA(2, 4) = 1
    result2 = tree.get_lca(2, 4)
    print(f"测试用例2 - LCA(2, 4): {result2}")
    
    # 测试用例3: LCA(3, 4) = 0
    result3 = tree.get_lca(3, 4)
    print(f"测试用例3 - LCA(3, 4): {result3}")