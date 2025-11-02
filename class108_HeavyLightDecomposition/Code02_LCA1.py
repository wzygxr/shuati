import sys
from collections import deque

# 重链剖分解决LCA查询，Python版
# 题目来源：洛谷P3379 【模板】最近公共祖先（LCA）
# 题目链接：https://www.luogu.com.cn/problem/P3379
#
# 题目描述：
# 如题，给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先。
# 一共有n个节点，给定n-1条边，节点连成一棵树，给定头节点编号root
# 一共有m条查询，每条查询给定a和b，打印a和b的最低公共祖先
# 请用树链剖分的方式实现
# 1 <= n、m <= 5 * 10^5
#
# 解题思路：
# 使用树链剖分解决LCA问题
# 1. 树链剖分：通过两次DFS将树划分为多条重链
# 2. LCA查询：利用树链剖分的性质，当两个节点不在同一重链上时，
#    将深度较大的节点跳到其重链顶端的父节点，直到两个节点在同一重链上
# 3. 迭代实现：为避免递归爆栈，使用迭代方式实现DFS
#
# 算法步骤：
# 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算重链顶端）
# 2. 对于LCA查询：
#    - 当两个节点不在同一重链上时，将深度较大的节点跳到其重链顶端的父节点
#    - 重复此过程直到两个节点在同一重链上
#    - 此时深度较小的节点即为LCA
#
# 时间复杂度分析：
# - 树链剖分预处理：O(n)
# - 每次LCA查询：O(log n)
# - 总体复杂度：O(n + m log n)
# 空间复杂度：O(n)
#
# 是否为最优解：
# 树链剖分解决LCA问题是一种高效的解决方案，
# 时间复杂度已经达到了理论下限，是最优解之一。
#
# 相关题目链接：
# 1. 洛谷P3379 【模板】最近公共祖先（LCA）（本题）：https://www.luogu.com.cn/problem/P3379
# 2. 洛谷P3384 【模板】重链剖分/树链剖分：https://www.luogu.com.cn/problem/P3384
# 3. LeetCode 1483. 树节点的第 K 个祖先：https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/
# 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
#
# Java实现参考：Code02_LCA1.java
# Python实现参考：Code02_LCA1.py（当前文件）
# C++实现参考：Code02_LCA2.java（注释部分）

class HLD_LCA:
    def __init__(self, n, root):
        # 图的邻接表表示
        self.n = n
        self.root = root
        
        # 链式前向星存图的数组
        self.head = [0] * (n + 1)
        self.next_edge = [0] * (2 * n + 1)
        self.to_edge = [0] * (2 * n + 1)
        self.cnt_edge = 0
        
        # 树链剖分相关数组
        self.fa = [0] * (n + 1)       # 父节点
        self.dep = [0] * (n + 1)      # 深度
        self.siz = [0] * (n + 1)      # 子树大小
        self.son = [0] * (n + 1)      # 重儿子
        self.top = [0] * (n + 1)      # 所在重链的顶部节点
        
        # 用于迭代实现的栈
        self.stack = []
    
    def add_edge(self, u, v):
        """添加无向边"""
        self.cnt_edge += 1
        self.next_edge[self.cnt_edge] = self.head[u]
        self.to_edge[self.cnt_edge] = v
        self.head[u] = self.cnt_edge
    
    def dfs1_iterative(self):
        """第一次dfs的迭代实现，计算fa, dep, siz, son"""
        # 栈中存储 (当前节点, 父节点, 边索引)
        # 边索引为-1表示第一次访问该节点
        self.stack = [(self.root, 0, -1)]
        
        while self.stack:
            u, f, edge = self.stack.pop()
            
            if edge == -1:
                # 第一次访问节点u
                self.fa[u] = f
                self.dep[u] = self.dep[f] + 1
                self.siz[u] = 1
                edge = self.head[u]
            else:
                # 处理完一条边，继续下一条边
                edge = self.next_edge[edge]
            
            if edge != 0:
                # 还有边未处理，将当前状态重新入栈
                self.stack.append((u, f, edge))
                v = self.to_edge[edge]
                if v != f:
                    # 将子节点入栈
                    self.stack.append((v, u, -1))
            else:
                # 所有边处理完毕，计算重儿子
                for e in range(self.head[u], 0, -1 if self.head[u] else 0):
                    if not e:
                        break
                    v = self.to_edge[e]
                    if v != f:
                        self.siz[u] += self.siz[v]
                        if self.son[u] == 0 or self.siz[self.son[u]] < self.siz[v]:
                            self.son[u] = v
                    if self.next_edge[e] == 0:
                        break
                    e = self.next_edge[e]
    
    def dfs2_iterative(self):
        """第二次dfs的迭代实现，计算top"""
        # 栈中存储 (当前节点, 重链顶端, 边索引)
        # 边索引为-1表示第一次访问该节点
        # 边索引为-2表示重儿子处理完毕，回到当前节点
        self.stack = [(self.root, self.root, -1)]
        
        while self.stack:
            u, t, edge = self.stack.pop()
            
            if edge == -1:
                # 第一次访问节点u，设置其所在重链的顶端
                self.top[u] = t
                if self.son[u] == 0:
                    continue
                # 先处理重儿子
                self.stack.append((u, t, -2))
                self.stack.append((self.son[u], t, -1))
                continue
            elif edge == -2:
                # 重儿子处理完毕，处理轻儿子
                edge = self.head[u]
            else:
                # 处理完一条边，继续下一条边
                edge = self.next_edge[edge]
            
            if edge != 0:
                # 还有边未处理，将当前状态重新入栈
                self.stack.append((u, t, edge))
                v = self.to_edge[edge]
                if v != self.fa[u] and v != self.son[u]:
                    # 轻儿子作为新重链的顶端
                    self.stack.append((v, v, -1))
    
    def lca(self, a, b):
        """使用树链剖分求两个节点的最近公共祖先"""
        # 当两个节点不在同一条重链上时
        while self.top[a] != self.top[b]:
            # 保证a所在重链深度更深
            if self.dep[self.top[a]] < self.dep[self.top[b]]:
                a, b = b, a
            # 跳到重链顶端的父节点
            a = self.fa[self.top[a]]
        
        # 当两个节点在同一条重链上时，深度较小的节点即为LCA
        return a if self.dep[a] <= self.dep[b] else b

def main():
    # 读取输入
    line = sys.stdin.readline().split()
    n, m, root = int(line[0]), int(line[1]), int(line[2])
    
    # 创建HLD_LCA对象
    hld = HLD_LCA(n, root)
    
    # 读取边信息
    for _ in range(n - 1):
        line = sys.stdin.readline().split()
        u, v = int(line[0]), int(line[1])
        hld.add_edge(u, v)
        hld.add_edge(v, u)
    
    # 树链剖分
    hld.dfs1_iterative()
    hld.dfs2_iterative()
    
    # 处理查询
    for _ in range(m):
        line = sys.stdin.readline().split()
        a, b = int(line[0]), int(line[1])
        print(hld.lca(a, b))

if __name__ == "__main__":
    main()