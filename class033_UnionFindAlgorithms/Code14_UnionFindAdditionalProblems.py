#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
并查集补充题目 (Python版本)
本文件包含更多使用并查集解决的经典题目
"""

from typing import List, Dict, Tuple, Optional

"""
题目1: LeetCode 399. 除法求值
链接: https://leetcode.cn/problems/evaluate-division/
难度: 中等
题目描述:
给你一个变量对数组 equations 和一个实数值数组 values 作为已知条件，其中 equations[i] = [Ai, Bi] 和 values[i] 共同表示等式 Ai / Bi = values[i]。
每个 Ai 或 Bi 是一个表示单个变量的字符串。
另有一些以数组 queries 表示的问题，其中 queries[j] = [Cj, Dj] 表示第 j 个问题，请你根据已知条件找出 Cj / Dj = ? 的结果作为答案。
如果存在某个无法确定的答案，则用 -1.0 替代。

注意：输入总是有效的，且不存在循环或冲突的结果。
"""
class EvaluateDivision:
    """
    使用带权并查集解决除法求值问题
    时间复杂度: O((E + Q) * α(N))，其中E是equations的长度，Q是queries的长度，N是不同变量的数量
    空间复杂度: O(N)
    """
    def calcEquation(self, equations: List[List[str]], values: List[float], 
                    queries: List[List[str]]) -> List[float]:
        # 创建并初始化带权并查集
        uf = self.WeightedUnionFind()
        
        # 构建并查集
        for i in range(len(equations)):
            var1 = equations[i][0]
            var2 = equations[i][1]
            uf.unite(var1, var2, values[i])
        
        # 处理查询
        results = []
        for query in queries:
            var1 = query[0]
            var2 = query[1]
            
            # 如果变量不存在于并查集中，结果为-1.0
            if var1 not in uf.parent or var2 not in uf.parent:
                results.append(-1.0)
                continue
            
            # 查找两个变量的根节点和权重
            root1, weight1 = uf.find(var1)
            root2, weight2 = uf.find(var2)
            
            # 如果根节点不同，说明无法确定结果
            if root1 != root2:
                results.append(-1.0)
            else:
                # 结果等于 var1到根的权重除以 var2到根的权重
                results.append(weight1 / weight2)
        
        return results
    
    """
    带权并查集实现
    用于处理除法关系，维护变量之间的倍数关系
    """
    class WeightedUnionFind:
        def __init__(self):
            # 存储父节点
            self.parent = dict()
            # 存储到父节点的权重（当前节点值 / 父节点值）
            self.weight = dict()
        
        """
        查找操作，返回根节点和权重（x的值 / 根节点的值）
        同时进行路径压缩
        """
        def find(self, x: str) -> Tuple[str, float]:
            # 确保x存在于并查集中
            if x not in self.parent:
                self.parent[x] = x
                self.weight[x] = 1.0
            
            if self.parent[x] != x:
                # 递归查找父节点
                origin_parent = self.parent[x]
                root, root_weight = self.find(origin_parent)
                
                # 路径压缩：将x直接指向根节点
                self.parent[x] = root
                # 更新权重：x到根的权重 = x到父的权重 * 父到根的权重
                self.weight[x] *= root_weight
            
            return self.parent[x], self.weight[x]
        
        """
        合并操作，表示 x / y = value
        """
        def unite(self, x: str, y: str, value: float) -> None:
            # 确保x和y存在于并查集中
            if x not in self.parent:
                self.parent[x] = x
                self.weight[x] = 1.0
            if y not in self.parent:
                self.parent[y] = y
                self.weight[y] = 1.0
            
            # 查找x和y的根节点
            x_root, x_weight = self.find(x)
            y_root, y_weight = self.find(y)
            
            if x_root != y_root:
                # 将x的根节点连接到y的根节点
                self.parent[x_root] = y_root
                # 更新权重：xRoot / yRoot = (y / yRoot) * (x / y) / (x / xRoot) = yWeight * value / xWeight
                self.weight[x_root] = y_weight * value / x_weight

"""
题目2: LeetCode 1697. 检查边长度限制的路径是否存在
链接: https://leetcode.cn/problems/checking-existence-of-edge-length-limited-paths/
难度: 困难
题目描述:
给你一个 n 个点组成的无向图边集 edgeList ，其中 edgeList[i] = [ui, vi, disi] 表示点 ui 和点 vi 之间有一条长度为 disi 的边。
同时给你一个查询数组 queries ，其中 queries[j] = [pj, qj, limitj] ，你的任务是对于每个查询 queries[j] ，判断是否存在一条从 pj 到 qj 的路径，且路径中每条边的长度都严格小于 limitj 。
"""
class DistanceLimitedPathsExist:
    """
    使用并查集结合离线查询解决路径存在性问题
    时间复杂度: O(E log E + Q log Q + (E + Q) α(N))，其中E是边的数量，Q是查询的数量，N是节点数量
    空间复杂度: O(N + Q)
    """
    def distanceLimitedPathsExist(self, n: int, edgeList: List[List[int]], 
                                 queries: List[List[int]]) -> List[bool]:
        # 创建并查集
        uf = self.UnionFind(n)
        
        # 将查询按limit排序，并记录原始索引
        sorted_queries = [(limit, p, q, idx) for idx, (p, q, limit) in enumerate(queries)]
        sorted_queries.sort()
        
        # 将边按距离排序
        edgeList.sort(key=lambda x: x[2])
        
        # 处理查询
        results = [False] * len(queries)
        edge_index = 0
        
        for limit, p, q, original_index in sorted_queries:
            # 将所有边权小于limit的边加入并查集
            while edge_index < len(edgeList) and edgeList[edge_index][2] < limit:
                uf.unite(edgeList[edge_index][0], edgeList[edge_index][1])
                edge_index += 1
            
            # 检查p和q是否连通
            results[original_index] = uf.is_connected(p, q)
        
        return results
    
    """
    标准并查集实现
    """
    class UnionFind:
        def __init__(self, n: int):
            self.parent = list(range(n))
            self.rank = [1] * n
        
        def find(self, x: int) -> int:
            if self.parent[x] != x:
                self.parent[x] = self.find(self.parent[x])  # 路径压缩
            return self.parent[x]
        
        def unite(self, x: int, y: int) -> None:
            root_x = self.find(x)
            root_y = self.find(y)
            
            if root_x != root_y:
                if self.rank[root_x] > self.rank[root_y]:
                    self.parent[root_y] = root_x
                elif self.rank[root_x] < self.rank[root_y]:
                    self.parent[root_x] = root_y
                else:
                    self.parent[root_y] = root_x
                    self.rank[root_x] += 1
        
        def is_connected(self, x: int, y: int) -> bool:
            return self.find(x) == self.find(y)

"""
题目3: LeetCode 1579. 保证图可完全遍历
链接: https://leetcode.cn/problems/remove-max-number-of-edges-to-keep-graph-fully-traversable/
难度: 困难
题目描述:
Alice 和 Bob 共有一个无向图，其中包含 n 个节点和 3 种类型的边：
类型 1：只能由 Alice 使用的边。
类型 2：只能由 Bob 使用的边。
类型 3：Alice 和 Bob 都可以使用的边。
请你在保证图仍能被 Alice和 Bob 完全遍历的前提下，找出可以删除的最大边数。
如果从任何节点开始，Alice 和 Bob 都可以到达所有其他节点，则认为图是可以完全遍历的。
"""
class RemoveMaxNumberOfEdgesToKeepGraphFullyTraversable:
    """
    使用并查集解决最大边删除问题
    时间复杂度: O(E α(N))，其中E是边的数量，N是节点数量
    空间复杂度: O(N)
    """
    def maxNumEdgesToRemove(self, n: int, edges: List[List[int]]) -> int:
        # 为Alice和Bob分别创建并查集
        alice_uf = self.UnionFind(n)
        bob_uf = self.UnionFind(n)
        
        edges_added = 0
        
        # 首先处理类型3的边（两人共用），因为这些边优先级最高
        for edge in edges:
            if edge[0] == 3:
                alice_connected = alice_uf.is_connected(edge[1] - 1, edge[2] - 1)
                bob_connected = bob_uf.is_connected(edge[1] - 1, edge[2] - 1)
                
                if not alice_connected or not bob_connected:
                    alice_uf.unite(edge[1] - 1, edge[2] - 1)
                    bob_uf.unite(edge[1] - 1, edge[2] - 1)
                    edges_added += 1
        
        # 处理类型1和类型2的边
        for edge in edges:
            if edge[0] == 1:
                # Alice专用边
                if not alice_uf.is_connected(edge[1] - 1, edge[2] - 1):
                    alice_uf.unite(edge[1] - 1, edge[2] - 1)
                    edges_added += 1
            elif edge[0] == 2:
                # Bob专用边
                if not bob_uf.is_connected(edge[1] - 1, edge[2] - 1):
                    bob_uf.unite(edge[1] - 1, edge[2] - 1)
                    edges_added += 1
        
        # 检查Alice和Bob是否都能完全遍历图
        alice_fully_connected = alice_uf.get_component_count() == 1
        bob_fully_connected = bob_uf.get_component_count() == 1
        
        if not alice_fully_connected or not bob_fully_connected:
            return -1  # 无法满足完全遍历条件
        
        # 可以删除的最大边数 = 总边数 - 必须保留的边数
        return len(edges) - edges_added
    
    """
    并查集实现，增加获取连通分量数量的功能
    """
    class UnionFind:
        def __init__(self, n: int):
            self.parent = list(range(n))
            self.rank = [1] * n
            self.component_count = n
        
        def find(self, x: int) -> int:
            if self.parent[x] != x:
                self.parent[x] = self.find(self.parent[x])
            return self.parent[x]
        
        def unite(self, x: int, y: int) -> None:
            root_x = self.find(x)
            root_y = self.find(y)
            
            if root_x != root_y:
                if self.rank[root_x] > self.rank[root_y]:
                    self.parent[root_y] = root_x
                elif self.rank[root_x] < self.rank[root_y]:
                    self.parent[root_x] = root_y
                else:
                    self.parent[root_y] = root_x
                    self.rank[root_x] += 1
                self.component_count -= 1
        
        def is_connected(self, x: int, y: int) -> bool:
            return self.find(x) == self.find(y)
        
        def get_component_count(self) -> int:
            return self.component_count

"""
题目4: POJ 1182 食物链
链接: http://poj.org/problem?id=1182
难度: 中等
题目描述:
动物王国中有三类动物A,B,C，这三类动物的食物链构成了有趣的环形。A吃B， B吃C，C吃A。
现有N个动物，以1－N编号。每个动物都是A,B,C中的一种，但是我们并不知道它到底是哪一种。
有人用两种说法对这N个动物所构成的食物链关系进行描述：
第一种说法是"1 X Y"，表示X和Y是同类。
第二种说法是"2 X Y"，表示X吃Y。
此人对N个动物，用上述两种说法，一句接一句地说出K句话，这K句话有的是真的，有的是假的。
当一句话满足下列三条之一时，这句话就是假话，否则就是真话。
1） 当前的话与前面的某些真的话冲突，就是假话；
2） 当前的话中X或Y比N大，就是假话；
3） 当前的话表示X吃X，就是假话。
你的任务是根据给定的N（1 <= N <= 50,000）和K句话（0 <= K <= 100,000），输出假话的总数。
"""
class FoodChain:
    """
    使用带权并查集解决食物链问题
    时间复杂度: O(K α(N))，其中K是语句数量，N是动物数量
    空间复杂度: O(N)
    """
    def findInvalidStatements(self, n: int, statements: List[List[int]]) -> int:
        # 初始化带权并查集，每个元素存储到父节点的关系（0表示同类，1表示吃父节点，2表示被父节点吃）
        parent = list(range(n + 1))  # 动物编号从1开始
        rank = [1] * (n + 1)
        relation = [0] * (n + 1)  # relation[x]表示x到父节点的关系
        
        def find(x: int) -> int:
            """查找根节点并进行路径压缩，同时更新关系"""
            if parent[x] != x:
                original_parent = parent[x]
                parent[x] = find(parent[x])
                # 更新关系：x到新根节点的关系 = x到原父节点的关系 + 原父节点到新根节点的关系
                relation[x] = (relation[x] + relation[original_parent]) % 3
            return parent[x]
        
        invalid_count = 0
        
        for statement in statements:
            type_statement = statement[0]
            x = statement[1]
            y = statement[2]
            
            # 检查条件2：X或Y比N大
            if x > n or y > n:
                invalid_count += 1
                continue
            
            # 检查条件3：X吃X
            if type_statement == 2 and x == y:
                invalid_count += 1
                continue
            
            root_x = find(x)
            root_y = find(y)
            
            if root_x == root_y:
                # X和Y已经在同一集合中，检查是否冲突
                relation_x_to_y = (relation[x] - relation[y] + 3) % 3
                if type_statement == 1 and relation_x_to_y != 0:
                    # 声明X和Y是同类，但实际不是
                    invalid_count += 1
                elif type_statement == 2 and relation_x_to_y != 1:
                    # 声明X吃Y，但实际不是
                    invalid_count += 1
            else:
                # 合并两个集合
                if rank[root_x] > rank[root_y]:
                    # 将rootY合并到rootX
                    parent[root_y] = root_x
                    # 计算rootY到rootX的关系
                    # 期望关系: X和Y的关系为type-1
                    relation[root_y] = (relation[x] - relation[y] + (type_statement - 1) + 3) % 3
                else:
                    # 将rootX合并到rootY
                    parent[root_x] = root_y
                    # 计算rootX到rootY的关系
                    relation[root_x] = (relation[y] - relation[x] + (3 - (type_statement - 1)) + 3) % 3
                    
                    if rank[root_x] == rank[root_y]:
                        rank[root_y] += 1
        
        return invalid_count

"""
并查集算法总结与技巧
1. 基本并查集适用于：连通性问题、集合合并、环检测
2. 带权并查集适用于：维护元素之间的关系（如食物链、除法关系等）
3. 离线并查集适用于：需要按特定顺序处理查询的场景
4. 优化技巧：路径压缩和按秩合并
5. 实现注意事项：
   - 初始化时每个元素指向自己
   - find操作要进行路径压缩
   - union操作要按秩合并以保持树的平衡
   - 对于字符串或其他非整数类型，可以使用哈希表映射
"""

def main():
    print("并查集补充题目Python实现完成")

if __name__ == "__main__":
    main()