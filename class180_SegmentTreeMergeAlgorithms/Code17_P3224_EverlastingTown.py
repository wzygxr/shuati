# 测试链接 : https://www.luogu.com.cn/problem/P3224
# P3224 [HNOI2012]永无乡 - Python实现

import sys
from collections import defaultdict

class FastIO:
    def __init__(self):
        self.stdin = sys.stdin
        self.buffer = []
        
    def readline(self):
        while not self.buffer:
            self.buffer = self.stdin.readline().split()
        return self.buffer.pop(0)
    
    def readint(self):
        return int(self.readline())

class SegmentTreeNode:
    """线段树节点类"""
    __slots__ = ('l', 'r', 'cnt', 'left', 'right')
    
    def __init__(self):
        self.l = -1
        self.r = -1
        self.cnt = 0
        self.left = None
        self.right = None

class SegmentTreeMerge:
    """线段树合并类"""
    
    def __init__(self, maxn=100010, maxm=10000000):
        self.maxn = maxn
        self.maxm = maxm
        self.nodes = []
        self.roots = {}
        self.node_cnt = 0
        
        # 预分配节点
        for _ in range(maxm):
            self.nodes.append(SegmentTreeNode())
    
    def new_node(self):
        """创建新节点"""
        if self.node_cnt >= self.maxm:
            import gc
            gc.collect()
            return -1
        
        node = self.nodes[self.node_cnt]
        node.left = node.right = None
        node.cnt = 0
        self.node_cnt += 1
        return self.node_cnt - 1
    
    def push_up(self, rt):
        """更新节点信息"""
        if rt == -1:
            return
        
        node = self.nodes[rt]
        node.cnt = 0
        
        if node.left is not None:
            node.cnt += self.nodes[node.l].cnt
        if node.right is not None:
            node.cnt += self.nodes[node.r].cnt
    
    def update(self, rt, l, r, pos):
        """单点更新"""
        if l == r:
            node = self.nodes[rt]
            node.cnt += 1
            return
        
        mid = (l + r) // 2
        node = self.nodes[rt]
        
        if pos <= mid:
            if node.left is None:
                new_rt = self.new_node()
                node.l = new_rt
                node.left = self.nodes[new_rt]
            self.update(node.l, l, mid, pos)
        else:
            if node.right is None:
                new_rt = self.new_node()
                node.r = new_rt
                node.right = self.nodes[new_rt]
            self.update(node.r, mid + 1, r, pos)
        
        self.push_up(rt)
    
    def merge(self, p, q, l, r):
        """线段树合并"""
        if p == -1:
            return q
        if q == -1:
            return p
        
        if l == r:
            p_node = self.nodes[p]
            q_node = self.nodes[q]
            p_node.cnt += q_node.cnt
            return p
        
        mid = (l + r) // 2
        p_node = self.nodes[p]
        q_node = self.nodes[q]
        
        if p_node.left is not None and q_node.left is not None:
            p_node.l = self.merge(p_node.l, q_node.l, l, mid)
        elif q_node.left is not None:
            p_node.l = q_node.l
            p_node.left = q_node.left
        
        if p_node.right is not None and q_node.right is not None:
            p_node.r = self.merge(p_node.r, q_node.r, mid + 1, r)
        elif q_node.right is not None:
            p_node.r = q_node.r
            p_node.right = q_node.right
        
        self.push_up(p)
        return p
    
    def query_kth(self, rt, l, r, k):
        """查询第k小"""
        if l == r:
            return l
        
        mid = (l + r) // 2
        node = self.nodes[rt]
        
        left_cnt = 0
        if node.left is not None:
            left_cnt = self.nodes[node.l].cnt
        
        if k <= left_cnt:
            return self.query_kth(node.l, l, mid, k)
        else:
            return self.query_kth(node.r, mid + 1, r, k - left_cnt)

class DSU:
    """并查集类"""
    
    def __init__(self, n):
        self.parent = list(range(n+1))
        self.size = [1] * (n+1)
    
    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y, st):
        """合并操作"""
        px = self.find(x)
        py = self.find(y)
        if px == py:
            return
        
        # 按秩合并
        if self.size[px] < self.size[py]:
            px, py = py, px
        
        # 合并线段树
        st.roots[px] = st.merge(st.roots[px], st.roots[py], 1, st.maxn)
        self.parent[py] = px
        self.size[px] += self.size[py]

def main():
    io = FastIO()
    
    n = io.readint()
    m = io.readint()
    
    # 初始化并查集
    dsu = DSU(n)
    
    # 初始化线段树合并
    st = SegmentTreeMerge()
    
    # 重要度映射
    rank_to_id = [0] * (n+1)
    id_to_rank = [0] * (n+1)
    
    # 读入重要度
    for i in range(1, n+1):
        rank = io.readint()
        rank_to_id[rank] = i
        id_to_rank[i] = rank
    
    # 初始化线段树
    for i in range(1, n+1):
        st.roots[i] = st.new_node()
        st.update(st.roots[i], 1, n, id_to_rank[i])
    
    # 读入初始桥
    for _ in range(m):
        u = io.readint()
        v = io.readint()
        dsu.union(u, v, st)
    
    q = io.readint()
    results = []
    
    for _ in range(q):
        op = io.readline()
        
        if op == "B":
            # 建桥操作
            u = io.readint()
            v = io.readint()
            dsu.union(u, v, st)
        else:
            # 查询操作
            u = io.readint()
            k = io.readint()
            
            root = dsu.find(u)
            if st.roots[root] == -1 or st.nodes[st.roots[root]].cnt < k:
                results.append("-1")
            else:
                rank = st.query_kth(st.roots[root], 1, n, k)
                results.append(str(rank_to_id[rank]))
    
    # 输出结果
    print("\n".join(results))

if __name__ == "__main__":
    main()

"""
算法详解：

1. 问题分析：
   需要维护动态连通性，并支持查询连通块内第k小的元素。
   由于需要动态合并连通块并查询第k小，需要高效的数据结构。

2. 核心思路：
   - 使用并查集维护连通性
   - 每个连通块维护一棵线段树，记录重要度的分布
   - 合并连通块时合并对应的线段树
   - 查询时在线段树上二分查找第k小

3. 算法步骤：
   a. 初始化：每个岛单独一个连通块，对应一棵线段树
   b. 建桥操作：合并两个连通块，同时合并对应的线段树
   c. 查询操作：在对应连通块的线段树上查询第k小

4. 时间复杂度分析：
   - 线段树合并：O(n log n)
   - 查询操作：O(log n)
   - 总体复杂度：O((n+m) log n)

5. 优化技巧：
   - 按秩合并：小树合并到大树，减少合并深度
   - 动态开点：节省空间
   - 线段树合并：避免重复计算

6. 类似题目：
   - P4556 [Vani有约会]雨天的尾巴
   - P5298 [PKUWC2018]Minimax
   - CF911G Mass Change Queries
   - P6773 [NOI2020]命运

7. 应用场景：
   - 动态连通性问题
   - 连通块内统计查询
   - 支持合并和查询的数据结构
"""