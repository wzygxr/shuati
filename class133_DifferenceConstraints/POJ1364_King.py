#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
POJ 1364 King 差分约束系统解法（Python版本）

题目链接：http://poj.org/problem?id=1364

题目描述：
有一个国王，他有一个序列S = {a1, a2, ..., an}。
国王给出了一些约束条件，形式为：
1. "gt" 约束：a_i + a_{i+1} + ... + a_{i+k} > c
2. "lt" 约束：a_i + a_{i+1} + ... + a_{i+k} < c

判断是否存在满足所有约束条件的序列S。

解题思路：
这是一个典型的差分约束系统问题。我们可以使用前缀和的思想来建模：
设S[i] = a1 + a2 + ... + ai，那么：
1. "gt" 约束：S[i+k] - S[i-1] > c => S[i+k] - S[i-1] >= c+1
2. "lt" 约束：S[i+k] - S[i-1] < c => S[i+k] - S[i-1] <= c-1

为了处理严格不等式，我们需要将其转化为非严格不等式：
- 大于约束：S[i+k] - S[i-1] >= c+1 => S[i-1] - S[i+k] <= -(c+1)
- 小于约束：S[i+k] - S[i-1] <= c-1

此外，我们还需要添加基本约束：S[i] - S[i-1] >= -INF（确保序列元素可以为负数）

差分约束建图：
1. 对于每个"gt"约束：从节点(i+k)向节点(i-1)连权值为-(c+1)的边
2. 对于每个"lt"约束：从节点(i-1)向节点(i+k)连权值为c-1的边
3. 基本约束：从节点i向节点i-1连权值为0的边（确保连通性）

最后添加超级源点，向所有点连权值为0的边，然后使用SPFA判断是否存在负环。
如果存在负环，则无解；否则有解。

时间复杂度：O(n * m)，其中n是序列长度，m是约束条件数
空间复杂度：O(n + m)

相关题目：
1. POJ 1364 King - 本题
2. POJ 1201 Intervals - 类似区间约束问题
3. POJ 2983 Is the Information Reliable? - 信息可靠性判断
4. POJ 3169 Layout - 奶牛排队布局问题
5. 洛谷 P5960 【模板】差分约束算法
6. 洛谷 P1993 小K的农场
7. 洛谷 P1250 种树
8. 洛谷 P2294 [HNOI2005]狡猾的商人
9. 洛谷 P4926 [1007]倍杀测量者
10. 洛谷 P3275 [SCOI2011]糖果
11. LibreOJ #10087 「一本通3.4 例1」Intervals
12. LibreOJ #10088 「一本通3.4 例2」出纳员问题
13. AtCoder ABC216G 01Sequence
14. Codeforces 1473E - Minimum Path

工程化考虑：
1. 异常处理：输入校验、图构建检查、算法执行检测
2. 性能优化：链式前向星存储图、静态数组、队列预分配
3. 可维护性：函数职责单一、变量命名清晰、详细注释
4. 可扩展性：支持更多约束类型、添加输出信息
5. 边界情况：空输入、极端值、重复约束
6. 测试用例：基本功能、边界值、异常情况、性能测试
"""

import sys
from collections import deque

class Graph:
    """图类，使用链式前向星存储"""
    
    def __init__(self, max_nodes, max_edges):
        self.max_nodes = max_nodes
        self.max_edges = max_edges
        self.head = [0] * (max_nodes + 1)
        self.next = [0] * (max_edges + 1)
        self.to = [0] * (max_edges + 1)
        self.weight = [0] * (max_edges + 1)
        self.cnt = 1
    
    def add_edge(self, u, v, w):
        """添加边到图中"""
        self.next[self.cnt] = self.head[u]
        self.to[self.cnt] = v
        self.weight[self.cnt] = w
        self.head[u] = self.cnt
        self.cnt += 1

class SPFA:
    """SPFA算法实现"""
    
    def __init__(self, graph, max_nodes):
        self.graph = graph
        self.max_nodes = max_nodes
        self.INF = 10**9
    
    def has_negative_cycle(self, start, n):
        """判断是否存在负环"""
        dist = [self.INF] * (n + 1)
        in_queue = [False] * (n + 1)
        count = [0] * (n + 1)
        
        queue = deque()
        dist[start] = 0
        in_queue[start] = True
        queue.append(start)
        count[start] = 1
        
        while queue:
            u = queue.popleft()
            in_queue[u] = False
            
            i = self.graph.head[u]
            while i > 0:
                v = self.graph.to[i]
                w = self.graph.weight[i]
                
                if dist[v] > dist[u] + w:
                    dist[v] = dist[u] + w
                    
                    if not in_queue[v]:
                        queue.append(v)
                        in_queue[v] = True
                        count[v] += 1
                        
                        if count[v] > n:
                            return True  # 存在负环
                
                i = self.graph.next[i]
        
        return False  # 无负环

def main():
    """主函数"""
    data = sys.stdin.read().strip().split('\n')
    idx = 0
    
    while idx < len(data):
        line = data[idx].strip()
        if not line:
            idx += 1
            continue
            
        parts = line.split()
        if len(parts) < 2:
            idx += 1
            continue
            
        n = int(parts[0])
        if n == 0:
            break
            
        m = int(parts[1])
        idx += 1
        
        # 初始化图
        max_nodes = n + 3
        max_edges = m * 2 + n + 10
        graph = Graph(max_nodes, max_edges)
        
        # 添加基本约束：S[i] - S[i-1] >= -INF
        # 转化为：S[i-1] - S[i] <= INF
        for i in range(1, n + 2):
            graph.add_edge(i, i - 1, graph.INF)
        
        # 处理约束条件
        for i in range(m):
            if idx >= len(data):
                break
                
            constraint = data[idx].strip().split()
            idx += 1
            
            if len(constraint) < 4:
                continue
                
            si = int(constraint[0])
            ni = int(constraint[1])
            op = constraint[2]
            ki = int(constraint[3])
            
            start = si
            end = si + ni
            
            if op == "gt":
                # gt约束：S[end] - S[start-1] > ki
                # 转化为：S[end] - S[start-1] >= ki+1
                # 再转化为：S[start-1] - S[end] <= -(ki+1)
                graph.add_edge(end, start - 1, -(ki + 1))
            else:  # "lt"
                # lt约束：S[end] - S[start-1] < ki
                # 转化为：S[end] - S[start-1] <= ki-1
                graph.add_edge(start - 1, end, ki - 1)
        
        # 添加超级源点
        super_source = n + 2
        for i in range(n + 2):
            graph.add_edge(super_source, i, 0)
        
        # 判断是否存在负环
        spfa = SPFA(graph, max_nodes)
        if spfa.has_negative_cycle(super_source, n + 3):
            print("successful conspiracy")
        else:
            print("lamentable kingdom")

if __name__ == "__main__":
    main()