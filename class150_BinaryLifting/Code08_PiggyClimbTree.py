# 洛谷 P5588 小猪佩奇爬树
# 题目描述：给定一棵树，每个节点有一个颜色，计算每种颜色的所有节点中，
# 有多少对节点(u, v)满足u是v的祖先或者v是u的祖先
# 
# 最优解算法：树上倍增 + DFS序 + 颜色统计
# 时间复杂度：O(n log n + m)
# 空间复杂度：O(n log n + c)，其中c是颜色数量
# 
# 解题思路：
# 1. 使用DFS序判断祖先-后代关系
# 2. 对于每种颜色的节点，利用DFS序的性质，按时间戳排序后统计满足条件的节点对
# 3. 使用树状数组优化子树内节点计数的查询

import math
from collections import defaultdict

class PiggyClimbTree:
    def __init__(self, n):
        """
        构造函数，初始化数据结构
        :param n: 节点数量
        """
        self.n = n
        self.LOG = int(math.ceil(math.log(n) / math.log(2))) + 1
        self.parent = [[-1] * (n + 1) for _ in range(self.LOG)]  # parent[j][u] 表示u的2^j级祖先
        self.depth = [0] * (n + 1)  # 每个节点的深度
        self.color = [0] * (n + 1)  # 每个节点的颜色
        self.adj = defaultdict(list)  # 邻接表
        self.in_time = [0] * (n + 1)  # DFS入时间戳
        self.out_time = [0] * (n + 1)  # DFS出时间戳
        self.time = 0  # 时间戳计数器
        self.color_nodes = defaultdict(list)  # 存储每种颜色的所有节点
        
    def add_edge(self, u, v):
        """
        添加树的边
        :param u: 父节点
        :param v: 子节点
        """
        self.adj[u].append(v)
        self.adj[v].append(u)
        
    def set_color(self, node, c):
        """
        设置节点颜色
        :param node: 节点编号
        :param c: 颜色
        """
        self.color[node] = c
        self.color_nodes[c].append(node)
        
    def preprocess(self):
        """
        预处理树结构，构建DFS序和倍增表
        """
        # DFS预处理深度、父节点和时间戳
        self.time = 0
        self.dfs(1, -1, 0)
        
        # 构建倍增表
        for j in range(1, self.LOG):
            for i in range(1, self.n + 1):
                if self.parent[j-1][i] != -1:
                    self.parent[j][i] = self.parent[j-1][self.parent[j-1][i]]
                    
    def dfs(self, u, p, d):
        """
        深度优先搜索，预处理父节点、深度和时间戳
        :param u: 当前节点
        :param p: 父节点
        :param d: 深度
        """
        self.parent[0][u] = p
        self.depth[u] = d
        self.time += 1
        self.in_time[u] = self.time
        
        for v in self.adj[u]:
            if v != p:
                self.dfs(v, u, d + 1)
                
        self.out_time[u] = self.time
        
    def is_ancestor(self, u, v):
        """
        判断节点u是否是节点v的祖先
        :param u: 可能的祖先节点
        :param v: 可能的后代节点
        :return: 如果u是v的祖先，返回True；否则返回False
        """
        # u是v的祖先当且仅当v的入时间在u的入时间之后，且出时间在u的出时间之前
        return self.in_time[u] <= self.in_time[v] and self.out_time[v] <= self.out_time[u]
        
    def lca(self, u, v):
        """
        查找两个节点的最近公共祖先
        :param u: 节点u
        :param v: 节点v
        :return: 最近公共祖先
        """
        if self.depth[u] < self.depth[v]:
            u, v = v, u
            
        # 将u提升到v的深度
        for j in range(self.LOG - 1, -1, -1):
            if self.depth[u] - (1 << j) >= self.depth[v]:
                u = self.parent[j][u]
                
        if u == v:
            return u
            
        # 同时提升两个节点
        for j in range(self.LOG - 1, -1, -1):
            if self.parent[j][u] != -1 and self.parent[j][u] != self.parent[j][v]:
                u = self.parent[j][u]
                v = self.parent[j][v]
                
        return self.parent[0][u]
        
    def calculate_color_pairs(self):
        """
        计算每种颜色的符合条件的节点对数量
        :return: 颜色到符合条件节点对数量的映射
        """
        result = defaultdict(int)
        
        # 对于每种颜色，计算其中有多少对节点满足祖先-后代关系
        for c, nodes in self.color_nodes.items():
            count = 0
            
            # 对节点按in_time排序，这样在DFS序中，祖先节点会排在后代节点前面
            nodes.sort(key=lambda x: self.in_time[x])
            
            # 对于每对节点，检查是否满足祖先-后代关系
            for i in range(len(nodes)):
                for j in range(i + 1, len(nodes)):
                    u = nodes[i]
                    v = nodes[j]
                    
                    # 检查u是否是v的祖先或者v是否是u的祖先
                    if self.is_ancestor(u, v) or self.is_ancestor(v, u):
                        count += 1
                        
            result[c] = count
            
        return result
        
    def calculate_color_pairs_optimized(self):
        """
        更高效的计算方法：利用DFS序的性质优化计算
        :return: 颜色到符合条件节点对数量的映射
        """
        result = defaultdict(int)
        
        for c, nodes in self.color_nodes.items():
            # 按in_time排序
            nodes.sort(key=lambda x: self.in_time[x])
            
            # 计算每个节点的子树中包含的同色节点数
            count = 0
            
            # 使用线段树或Fenwick树来高效查询子树中的节点数
            # 这里为了简化，使用数组实现一个简单的前缀和查询
            tree = [0] * (self.n + 2)
            
            # 按in_time顺序处理节点
            for node in nodes:
                # 查询当前节点子树中已经处理过的同色节点数
                subtree_count = self.query(tree, self.out_time[node]) - self.query(tree, self.in_time[node] - 1)
                count += subtree_count
                
                # 将当前节点加入树中
                self.update(tree, self.in_time[node], 1)
                
            result[c] = count
            
        return result
        
    def update(self, tree, idx, delta):
        """
        树状数组的更新操作
        """
        while idx < len(tree):
            tree[idx] += delta
            idx += idx & -idx  # 加上最低位的1
            
    def query(self, tree, idx):
        """
        树状数组的查询操作（前缀和）
        """
        sum_val = 0
        while idx > 0:
            sum_val += tree[idx]
            idx -= idx & -idx  # 减去最低位的1
        return sum_val

def main():
    """
    主方法，用于测试
    """
    # 示例测试
    n = 5
    tree = PiggyClimbTree(n)
    
    # 添加边（1为根节点）
    tree.add_edge(1, 2)
    tree.add_edge(1, 3)
    tree.add_edge(2, 4)
    tree.add_edge(2, 5)
    
    # 设置颜色
    tree.set_color(1, 1)
    tree.set_color(2, 1)
    tree.set_color(3, 2)
    tree.set_color(4, 1)
    tree.set_color(5, 2)
    
    # 预处理
    tree.preprocess()
    
    # 计算结果
    result = tree.calculate_color_pairs()
    
    # 输出结果
    print(f"颜色1的符合条件节点对数量: {result[1]}")  # 应为3对
    print(f"颜色2的符合条件节点对数量: {result[2]}")  # 应为0对
    
    # 使用优化方法计算
    optimized_result = tree.calculate_color_pairs_optimized()
    print(f"优化方法 - 颜色1的符合条件节点对数量: {optimized_result[1]}")
    print(f"优化方法 - 颜色2的符合条件节点对数量: {optimized_result[2]}")

if __name__ == "__main__":
    main()