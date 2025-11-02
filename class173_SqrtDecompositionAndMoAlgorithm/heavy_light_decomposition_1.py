/**
 * 树链剖分（Heavy-Light Decomposition）算法实现 - Python版本
 * 
 * 算法核心思想：
 * 1. 将树分解为多条重链（heavy chains）和轻链（light chains）
 * 2. 重链上的节点在DFS序中是连续的
 * 3. 使用线段树等数据结构维护每条链上的信息
 * 
 * 时间复杂度：
 * - 预处理：O(n)
 * - 路径查询/更新：O(log²n) 或 O(log n)（取决于底层数据结构）
 * 
 * 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. 树上路径查询（最大值、最小值、和等）
 * 2. 树上路径更新
 * 3. 子树查询和更新
 * 4. LCA（最近公共祖先）查询
 * 
 * 题目来源：
 * - Codeforces 343D - Water Tree
 * - SPOJ QTREE - Query on a tree
 * - HDU 3966 - Aragorn's Story
 * - POJ 3237 - Tree
 * - 洛谷 P3384 【模板】轻重链剖分
 */

class HeavyLightDecomposition:
    def __init__(self, n):
        """
        构造函数
        :param n: 节点数量
        """
        self.n = n
        self.tree = [[] for _ in range(n + 1)]  # 树的邻接表表示
        self.parent = [0] * (n + 1)            # 父节点数组
        self.depth = [0] * (n + 1)             # 深度数组
        self.size = [0] * (n + 1)              # 子树大小
        self.heavy = [-1] * (n + 1)            # 重儿子节点
        self.head = [-1] * (n + 1)             # 链头节点
        self.pos = [0] * (n + 1)               # DFS序位置
        self.cur_pos = 0                       # 当前DFS序位置
        
        # 线段树相关（用于维护链上信息）
        self.seg_tree = [0] * (4 * n)          # 线段树数组
        self.lazy = [0] * (4 * n)              # 懒标记数组
        self.arr = [0] * (n + 1)               # 原始数组值
    
    def add_edge(self, u, v):
        """
        添加边
        :param u: 节点u
        :param v: 节点v
        """
        self.tree[u].append(v)
        self.tree[v].append(u)
    
    def dfs1(self, u, p):
        """
        第一次DFS：计算子树大小和重儿子
        :param u: 当前节点
        :param p: 父节点
        """
        self.parent[u] = p
        self.depth[u] = self.depth[p] + 1
        self.size[u] = 1
        
        max_size = 0
        for v in self.tree[u]:
            if v == p:
                continue
            self.dfs1(v, u)
            self.size[u] += self.size[v]
            if self.size[v] > max_size:
                max_size = self.size[v]
                self.heavy[u] = v
    
    def dfs2(self, u, h):
        """
        第二次DFS：构建重链
        :param u: 当前节点
        :param h: 链头节点
        """
        self.head[u] = h
        self.pos[u] = self.cur_pos
        self.cur_pos += 1
        
        if self.heavy[u] != -1:
            self.dfs2(self.heavy[u], h)
        
        for v in self.tree[u]:
            if v == self.parent[u] or v == self.heavy[u]:
                continue
            self.dfs2(v, v)
    
    def build(self, root):
        """
        初始化树链剖分
        :param root: 根节点
        """
        self.dfs1(root, 0)
        self.dfs2(root, root)
        self._build_seg_tree(1, 0, self.n - 1)
    
    def _build_seg_tree(self, idx, l, r):
        """
        构建线段树
        :param idx: 线段树节点索引
        :param l: 区间左边界
        :param r: 区间右边界
        """
        if l == r:
            self.seg_tree[idx] = self.arr[l]
            return
        mid = (l + r) // 2
        self._build_seg_tree(2 * idx, l, mid)
        self._build_seg_tree(2 * idx + 1, mid + 1, r)
        self.seg_tree[idx] = self.seg_tree[2 * idx] + self.seg_tree[2 * idx + 1]
    
    def _query_seg_tree(self, idx, seg_l, seg_r, l, r):
        """
        线段树区间查询
        """
        if l > seg_r or r < seg_l:
            return 0
        if self.lazy[idx] != 0:
            self.seg_tree[idx] += (seg_r - seg_l + 1) * self.lazy[idx]
            if seg_l != seg_r:
                self.lazy[2 * idx] += self.lazy[idx]
                self.lazy[2 * idx + 1] += self.lazy[idx]
            self.lazy[idx] = 0
        if l <= seg_l and seg_r <= r:
            return self.seg_tree[idx]
        mid = (seg_l + seg_r) // 2
        left_res = self._query_seg_tree(2 * idx, seg_l, mid, l, r)
        right_res = self._query_seg_tree(2 * idx + 1, mid + 1, seg_r, l, r)
        return left_res + right_res
    
    def _update_seg_tree(self, idx, seg_l, seg_r, l, r, val):
        """
        线段树区间更新
        """
        if self.lazy[idx] != 0:
            self.seg_tree[idx] += (seg_r - seg_l + 1) * self.lazy[idx]
            if seg_l != seg_r:
                self.lazy[2 * idx] += self.lazy[idx]
                self.lazy[2 * idx + 1] += self.lazy[idx]
            self.lazy[idx] = 0
        if l > seg_r or r < seg_l:
            return
        if l <= seg_l and seg_r <= r:
            self.seg_tree[idx] += (seg_r - seg_l + 1) * val
            if seg_l != seg_r:
                self.lazy[2 * idx] += val
                self.lazy[2 * idx + 1] += val
            return
        mid = (seg_l + seg_r) // 2
        self._update_seg_tree(2 * idx, seg_l, mid, l, r, val)
        self._update_seg_tree(2 * idx + 1, mid + 1, seg_r, l, r, val)
        self.seg_tree[idx] = self.seg_tree[2 * idx] + self.seg_tree[2 * idx + 1]
    
    def query_path(self, u, v):
        """
        路径查询：查询u到v路径上的和
        :param u: 节点u
        :param v: 节点v
        :return: 路径和
        """
        res = 0
        while self.head[u] != self.head[v]:
            if self.depth[self.head[u]] < self.depth[self.head[v]]:
                u, v = v, u
            res += self._query_seg_tree(1, 0, self.n - 1, self.pos[self.head[u]], self.pos[u])
            u = self.parent[self.head[u]]
        if self.depth[u] > self.depth[v]:
            u, v = v, u
        res += self._query_seg_tree(1, 0, self.n - 1, self.pos[u], self.pos[v])
        return res
    
    def update_path(self, u, v, val):
        """
        路径更新：将u到v路径上的值增加val
        :param u: 节点u
        :param v: 节点v
        :param val: 增加值
        """
        while self.head[u] != self.head[v]:
            if self.depth[self.head[u]] < self.depth[self.head[v]]:
                u, v = v, u
            self._update_seg_tree(1, 0, self.n - 1, self.pos[self.head[u]], self.pos[u], val)
            u = self.parent[self.head[u]]
        if self.depth[u] > self.depth[v]:
            u, v = v, u
        self._update_seg_tree(1, 0, self.n - 1, self.pos[u], self.pos[v], val)
    
    def query_subtree(self, u):
        """
        子树查询：查询以u为根的子树的和
        :param u: 根节点
        :return: 子树和
        """
        return self._query_seg_tree(1, 0, self.n - 1, self.pos[u], self.pos[u] + self.size[u] - 1)
    
    def update_subtree(self, u, val):
        """
        子树更新：将以u为根的子树的值增加val
        :param u: 根节点
        :param val: 增加值
        """
        self._update_seg_tree(1, 0, self.n - 1, self.pos[u], self.pos[u] + self.size[u] - 1, val)
    
    def set_node_value(self, u, val):
        """
        设置节点值
        :param u: 节点
        :param val: 值
        """
        self.arr[self.pos[u]] = val
    
    def get_lca(self, u, v):
        """
        获取LCA（最近公共祖先）
        :param u: 节点u
        :param v: 节点v
        :return: 最近公共祖先
        """
        while self.head[u] != self.head[v]:
            if self.depth[self.head[u]] < self.depth[self.head[v]]:
                u, v = v, u
            u = self.parent[self.head[u]]
        return u if self.depth[u] < self.depth[v] else v
    
    def get_kth_ancestor(self, u, k):
        """
        获取节点u的第k级祖先
        :param u: 节点u
        :param k: 祖先级别
        :return: 第k级祖先节点，如果不存在则返回-1
        """
        if self.depth[u] < k:
            return -1
        
        while k > 0:
            head_depth = self.depth[self.head[u]]
            current_chain_length = self.depth[u] - head_depth + 1
            
            if k >= current_chain_length:
                k -= current_chain_length
                u = self.parent[self.head[u]]
            else:
                for _ in range(k):
                    u = self.parent[u]
                return u
        
        return u


def test_hld():
    """
    测试用例：验证树链剖分功能
    """
    # 创建一个有5个节点的树
    n = 5
    hld = HeavyLightDecomposition(n)
    
    # 构建树结构
    hld.add_edge(1, 2)
    hld.add_edge(1, 3)
    hld.add_edge(2, 4)
    hld.add_edge(2, 5)
    
    # 初始化节点值
    for i in range(1, n + 1):
        hld.set_node_value(i, i)  # 简单地将节点值设为节点编号
    
    # 构建树链剖分
    hld.build(1)
    
    # 测试路径查询
    print(f"路径1-4的和: {hld.query_path(1, 4)}")  # 应该输出1+2+4=7
    print(f"路径3-5的和: {hld.query_path(3, 5)}")  # 应该输出3+1+2+5=11
    
    # 测试路径更新
    hld.update_path(1, 4, 10)
    print(f"更新后路径1-4的和: {hld.query_path(1, 4)}")  # 应该输出7+30=37
    
    # 测试子树查询
    print(f"以2为根的子树和: {hld.query_subtree(2)}")  # 应该输出2+4+5=11
    
    # 测试LCA
    print(f"节点4和节点5的LCA: {hld.get_lca(4, 5)}")  # 应该输出2
    
    # 测试Kth祖先
    print(f"节点5的第2级祖先: {hld.get_kth_ancestor(5, 2)}")  # 应该输出1
    
    print("树链剖分测试完成！")


if __name__ == "__main__":
    test_hld()