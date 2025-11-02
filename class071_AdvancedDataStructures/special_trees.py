import sys
from collections import defaultdict, deque

class DominatorTree:
    """
    支配树 (Dominator Tree) Python 实现
    使用Lengauer-Tarjan算法构建支配树
    
    时间复杂度：O(E log V)
    空间复杂度：O(V + E)
    
    设计要点：
    1. 深度优先搜索建立时间戳和DFS树
    2. 使用并查集优化半支配点的计算
    3. 按时间戳逆序处理节点
    
    典型应用场景：
    - 程序分析中的控制流分析
    - 网络流中的瓶颈分析
    - 寻找必经点
    """
    
    def __init__(self, n):
        """
        初始化支配树
        
        Args:
            n: 节点数量（节点编号从0开始）
        """
        self.n = n
        self.g = [[] for _ in range(n)]      # 原图
        self.rg = [[] for _ in range(n)]     # 反向图
        self.bucket = [[] for _ in range(n)] # 桶，用于存储待处理节点
        self.semi = [i for i in range(n)]    # 半支配点
        self.idom = [-1] * n                 # 直接支配点
        self.vertex = [0] * (n + 1)          # 时间戳 -> 节点映射
        self.label = [i for i in range(n)]   # 并查集标签
        self.dfn = [0] * n                   # 节点 -> 时间戳映射
        self.parent = [-1] * n               # DFS树父节点
        self.dom_tree = [[] for _ in range(n)] # 支配树
        self.time_stamp = 0
    
    def add_edge(self, u, v):
        """
        添加有向边
        
        Args:
            u: 起点
            v: 终点
        """
        if u < 0 or u >= self.n or v < 0 or v >= self.n:
            raise ValueError("Node index out of range")
        self.g[u].append(v)
        self.rg[v].append(u)
    
    def _find(self, x):
        """
        并查集查找操作，带路径压缩
        
        Args:
            x: 查找的节点
            
        Returns:
            根节点
        """
        if self.label[x] != x:
            fx = self._find(self.label[x])
            # 路径压缩时，选择semi序更小的节点作为代表
            if self.dfn[self.semi[fx]] < self.dfn[self.semi[self.label[x]]]:
                self.label[x] = fx
        return self.label[x]
    
    def _dfs(self, u):
        """
        深度优先搜索，建立时间戳和DFS树
        
        Args:
            u: 当前节点
        """
        self.time_stamp += 1
        self.dfn[u] = self.time_stamp
        self.vertex[self.time_stamp] = u
        
        for v in self.g[u]:
            if not self.dfn[v]:
                self.parent[v] = u
                self._dfs(v)
    
    def build(self, root):
        """
        构建支配树
        
        Args:
            root: 根节点
        """
        if root < 0 or root >= self.n:
            raise ValueError("Root node index out of range")
        
        # 初始化时间戳和直接支配点
        self.time_stamp = 0
        self.dfn = [0] * self.n
        self.idom = [-1] * self.n
        
        # DFS建立时间戳
        self._dfs(root)
        
        # 初始化semi和label
        for i in range(1, self.time_stamp + 1):
            u = self.vertex[i]
            self.semi[u] = u
            self.label[u] = u
        
        # 按时间戳逆序处理节点
        for i in range(self.time_stamp, 1, -1):
            u = self.vertex[i]
            
            # 计算semi[u]
            for v in self.rg[u]:
                if not self.dfn[v]:
                    continue  # v没有被访问到
                
                if self.dfn[v] < self.dfn[u]:
                    # v是u的前驱节点在DFS树中的祖先
                    if self.dfn[self.semi[u]] > self.dfn[v]:
                        self.semi[u] = v
                else:
                    # 通过并查集找到v所在集合的代表节点
                    self._find(v)
                    if self.dfn[self.semi[u]] > self.dfn[self.semi[self.label[v]]]:
                        self.semi[u] = self.semi[self.label[v]]
            
            # 将u加入semi[u]的桶中
            self.bucket[self.semi[u]].append(u)
            
            # 处理parent[u]的桶中的节点
            for v in self.bucket[self.parent[u]]:
                self._find(v)
                if self.semi[self.label[v]] == self.semi[v]:
                    self.idom[v] = self.semi[v]
                else:
                    self.idom[v] = self.label[v]
            self.bucket[self.parent[u]].clear()
            
            # 更新u的label为其父节点
            self.label[u] = self.parent[u]
        
        # 处理剩余的节点，更新直接支配点
        for i in range(2, self.time_stamp + 1):
            u = self.vertex[i]
            if self.idom[u] != self.semi[u]:
                self.idom[u] = self.idom[self.idom[u]]
        
        # 构建支配树的邻接表
        for i in range(self.n):
            if i == root:
                continue
            if self.idom[i] != -1:
                self.dom_tree[self.idom[i]].append(i)
    
    def is_dominating(self, u, v):
        """
        判断u是否支配v
        
        Args:
            u: 潜在支配点
            v: 被支配点
            
        Returns:
            bool: u是否支配v
        """
        if u == v:
            return True
        if self.idom[v] == -1:
            return False
        
        # 沿着支配树向上查找u
        current = v
        while current != -1 and current != u:
            current = self.idom[current]
        
        return current == u
    
    def get_idom(self, v):
        """
        获取节点v的直接支配点
        
        Args:
            v: 节点
            
        Returns:
            直接支配点
        """
        if v < 0 or v >= self.n:
            raise ValueError("Node index out of range")
        return self.idom[v]
    
    def print_dom_tree(self):
        """
        打印支配树结构
        """
        for u in range(self.n):
            if self.dom_tree[u]:
                print(f"Node {u} -> {self.dom_tree[u]}")

class VirtualTree:
    """
    虚树 (Virtual Tree) Python 实现
    用于压缩树结构，只保留关键点及其LCA
    
    时间复杂度：O(M log N)，其中M是关键点数量
    空间复杂度：O(N + M)
    
    设计要点：
    1. 使用倍增法预处理LCA
    2. 按DFS序排序关键点
    3. 栈式构建虚树
    
    典型应用场景：
    - 大规模树上的路径问题
    - 只关注部分关键点的树查询
    - 树形DP优化
    """
    
    def __init__(self, n):
        """
        初始化虚树
        
        Args:
            n: 原树节点数量
        """
        self.n = n
        self.g = [[] for _ in range(n)]      # 原树邻接表
        self.depth = [0] * n                 # 节点深度
        
        # 计算log2(n)的上界
        self.log_n = 1
        while (1 << self.log_n) <= n:
            self.log_n += 1
        
        self.up = [[0] * n for _ in range(self.log_n)]  # 倍增表
        self.dfn = [0] * n                   # 时间戳
        self.time_stamp = 0
    
    def add_edge(self, u, v):
        """
        添加原树的无向边
        
        Args:
            u: 节点u
            v: 节点v
        """
        if u < 0 or u >= self.n or v < 0 or v >= self.n:
            raise ValueError("Node index out of range")
        self.g[u].append(v)
        self.g[v].append(u)
    
    def _dfs(self, u, parent_node):
        """
        深度优先搜索，预处理LCA所需信息
        
        Args:
            u: 当前节点
            parent_node: 父节点
        """
        self.time_stamp += 1
        self.dfn[u] = self.time_stamp
        self.up[0][u] = parent_node
        
        # 初始化倍增表
        for k in range(1, self.log_n):
            self.up[k][u] = self.up[k-1][self.up[k-1][u]]
        
        # 递归处理子节点
        for v in self.g[u]:
            if v != parent_node:
                self.depth[v] = self.depth[u] + 1
                self._dfs(v, u)
    
    def build_lca(self, root=0):
        """
        构建LCA所需的数据结构
        
        Args:
            root: 原树的根节点
        """
        if root < 0 or root >= self.n:
            raise ValueError("Root node index out of range")
        
        self.time_stamp = 0
        self.depth[root] = 0
        self._dfs(root, root)
    
    def _lca(self, u, v):
        """
        计算两个节点的最近公共祖先
        
        Args:
            u: 节点u
            v: 节点v
            
        Returns:
            最近公共祖先
        """
        if self.depth[u] < self.depth[v]:
            u, v = v, u
        
        # 将u提升到与v同一深度
        for k in range(self.log_n - 1, -1, -1):
            if self.depth[u] - (1 << k) >= self.depth[v]:
                u = self.up[k][u]
        
        if u == v:
            return u
        
        # 同时提升u和v
        for k in range(self.log_n - 1, -1, -1):
            if self.up[k][u] != self.up[k][v]:
                u = self.up[k][u]
                v = self.up[k][v]
        
        return self.up[0][u]
    
    def build_virtual_tree(self, key_nodes):
        """
        构建虚树
        
        Args:
            key_nodes: 关键点列表
            
        Returns:
            tuple: (虚树邻接表, 虚树根节点)
        """
        if not key_nodes:
            return [], -1
        
        # 按时间戳排序关键点
        sorted_keys = sorted(key_nodes, key=lambda x: self.dfn[x])
        
        # 去重
        unique_keys = []
        prev = None
        for node in sorted_keys:
            if node != prev:
                unique_keys.append(node)
                prev = node
        
        # 计算相邻关键点的LCA并添加
        m = len(unique_keys)
        temp = unique_keys.copy()
        for i in range(m - 1):
            l = self._lca(unique_keys[i], unique_keys[i+1])
            temp.append(l)
        
        # 再次排序和去重
        temp = sorted(temp, key=lambda x: self.dfn[x])
        sorted_keys = []
        prev = None
        for node in temp:
            if node != prev:
                sorted_keys.append(node)
                prev = node
        
        # 初始化虚树邻接表
        virtual_g = [[] for _ in range(self.n)]
        
        # 使用栈构建虚树
        stack = []
        stack.append(sorted_keys[0])
        
        for u in sorted_keys[1:]:
            # 找到u和栈顶节点的LCA
            l = self._lca(u, stack[-1])
            
            # 将栈中深度大于l的节点弹出并建立边
            while len(stack) > 1 and self.depth[stack[-1]] > self.depth[l]:
                v = stack.pop()
                if self.depth[stack[-1]] > self.depth[l]:
                    # 连接v和新的栈顶
                    virtual_g[stack[-1]].append(v)
                    virtual_g[v].append(stack[-1])
                else:
                    # 连接v和l
                    virtual_g[l].append(v)
                    virtual_g[v].append(l)
            
            # 如果栈顶不是l，将l入栈
            if stack[-1] != l:
                stack.append(l)
            stack.append(u)
        
        # 处理栈中剩余的节点
        while len(stack) > 1:
            u = stack.pop()
            virtual_g[stack[-1]].append(u)
            virtual_g[u].append(stack[-1])
        
        return virtual_g, stack[0]
    
    def get_depth(self, u):
        """
        获取节点的深度
        
        Args:
            u: 节点
            
        Returns:
            深度值
        """
        if u < 0 or u >= self.n:
            raise ValueError("Node index out of range")
        return self.depth[u]
    
    def get_dfn(self, u):
        """
        获取节点的时间戳
        
        Args:
            u: 节点
            
        Returns:
            时间戳
        """
        if u < 0 or u >= self.n:
            raise ValueError("Node index out of range")
        return self.dfn[u]

# 测试函数
def test_dominator_tree():
    print("===== 测试支配树 =====")
    # 创建一个简单的有向图
    dt = DominatorTree(7)
    dt.add_edge(0, 1)
    dt.add_edge(0, 2)
    dt.add_edge(1, 3)
    dt.add_edge(2, 3)
    dt.add_edge(3, 4)
    dt.add_edge(3, 5)
    dt.add_edge(4, 6)
    dt.add_edge(5, 6)
    
    dt.build(0)
    print("支配树结构:")
    dt.print_dom_tree()
    
    print(f"节点0是否支配节点6: {dt.is_dominating(0, 6)}")
    print(f"节点3是否支配节点6: {dt.is_dominating(3, 6)}")
    print(f"节点1是否支配节点5: {dt.is_dominating(1, 5)}")

def test_virtual_tree():
    print("\n===== 测试虚树 =====")
    # 创建一个简单的无向树
    vt = VirtualTree(7)
    vt.add_edge(0, 1)
    vt.add_edge(1, 2)
    vt.add_edge(1, 3)
    vt.add_edge(3, 4)
    vt.add_edge(3, 5)
    vt.add_edge(5, 6)
    
    vt.build_lca(0)
    
    # 关键点集合
    key_nodes = [0, 2, 4, 6]
    virtual_g, root = vt.build_virtual_tree(key_nodes)
    
    print(f"虚树根节点: {root}")
    print("虚树结构:")
    for u in range(7):
        if virtual_g[u]:
            print(f"Node {u} -> {virtual_g[u]}")

if __name__ == "__main__":
    test_dominator_tree()
    test_virtual_tree()