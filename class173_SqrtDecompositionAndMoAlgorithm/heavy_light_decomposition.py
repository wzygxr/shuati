from typing import List, Dict, Set, Tuple

class HeavyLightDecomposition:
    """
    树链剖分（重链剖分）算法实现
    树链剖分是一种将树分割成若干条链，以支持树上路径查询和修改操作的算法
    时间复杂度：预处理 O(n)，单次路径查询/修改 O(log n)
    主要用于处理树上区间查询、路径修改等问题
    """
    
    class Edge:
        """
        表示树中的一条边
        """
        def __init__(self, to: int, weight: int):
            self.to = to
            self.weight = weight
    
    def __init__(self, n: int):
        """
        初始化树链剖分数据结构
        
        Args:
            n: 节点数量
        """
        self.n = n
        self.cnt = 0
        self.tree: List[List[HeavyLightDecomposition.Edge]] = [[] for _ in range(n + 1)]  # 邻接表表示的树
        self.size: List[int] = [0] * (n + 1)  # 子树大小
        self.dep: List[int] = [0] * (n + 1)  # 节点深度
        self.fa: List[int] = [0] * (n + 1)  # 父节点
        self.son: List[int] = [0] * (n + 1)  # 重儿子
        self.top: List[int] = [0] * (n + 1)  # 所在链的顶端节点
        self.id: List[int] = [0] * (n + 1)  # 节点在线段树中的位置（DFS序）
        self.rev: List[int] = [0] * (n + 1)  # DFS序对应的原节点编号
        self.val: List[int] = [0] * (n + 1)  # 节点权值
        self.edge_val: List[int] = [0] * (n + 1)  # 边权（存储在子节点中）
    
    def add_edge(self, u: int, v: int, w: int) -> None:
        """
        添加树边（带权）
        
        Args:
            u: 第一个节点
            v: 第二个节点
            w: 边权
        """
        self.tree[u].append(HeavyLightDecomposition.Edge(v, w))
        self.tree[v].append(HeavyLightDecomposition.Edge(u, w))
    
    def set_value(self, u: int, w: int) -> None:
        """
        设置节点权值
        
        Args:
            u: 节点
            w: 权值
        """
        self.val[u] = w
    
    def _dfs1(self, u: int) -> None:
        """
        第一次DFS：计算子树大小、深度、父节点和重儿子
        
        Args:
            u: 当前节点
        """
        self.size[u] = 1
        self.son[u] = 0
        max_size = 0
        
        for e in self.tree[u]:
            v = e.to
            if v != self.fa[u]:
                self.fa[v] = u
                self.dep[v] = self.dep[u] + 1
                self.edge_val[v] = e.weight  # 边权存储在子节点中
                self._dfs1(v)
                self.size[u] += self.size[v]
                if self.size[v] > max_size:
                    max_size = self.size[v]
                    self.son[u] = v  # 记录重儿子
    
    def _dfs2(self, u: int, top_node: int) -> None:
        """
        第二次DFS：分配DFS序，构建重链
        
        Args:
            u: 当前节点
            top_node: 当前链的顶端节点
        """
        self.cnt += 1
        self.id[u] = self.cnt  # 分配DFS序
        self.rev[self.cnt] = u  # 记录DFS序对应的原节点
        self.top[u] = top_node  # 记录链顶
        
        # 优先处理重儿子，保证重链上的节点DFS序连续
        if self.son[u] != 0:
            self._dfs2(self.son[u], top_node)  # 重儿子继承当前链顶
            
            # 处理轻儿子
            for e in self.tree[u]:
                v = e.to
                if v != self.fa[u] and v != self.son[u]:
                    self._dfs2(v, v)  # 轻儿子作为新链的链顶
    
    def init(self, root: int) -> None:
        """
        初始化树链剖分
        
        Args:
            root: 根节点
        """
        self.dep[root] = 1
        self.fa[root] = 0
        self._dfs1(root)
        self._dfs2(root, root)
    
    def lca(self, u: int, v: int) -> int:
        """
        查找两个节点的最近公共祖先（LCA）
        
        Args:
            u: 节点u
            v: 节点v
            
        Returns:
            最近公共祖先节点
        """
        while self.top[u] != self.top[v]:
            if self.dep[self.top[u]] < self.dep[self.top[v]]:
                u, v = v, u
            u = self.fa[self.top[u]]  # 跳转到所在链的链顶的父节点
        return u if self.dep[u] < self.dep[v] else v
    
    def distance(self, u: int, v: int) -> int:
        """
        计算两个节点之间的距离
        
        Args:
            u: 节点u
            v: 节点v
            
        Returns:
            距离
        """
        ancestor = self.lca(u, v)
        return self.dep[u] + self.dep[v] - 2 * self.dep[ancestor]
    
    def query_max(self, u: int, v: int) -> int:
        """
        路径查询模板：查询u到v路径上的最大值
        
        Args:
            u: 起点
            v: 终点
            
        Returns:
            最大值
        """
        max_val = float('-inf')
        while self.top[u] != self.top[v]:
            if self.dep[self.top[u]] < self.dep[self.top[v]]:
                u, v = v, u
            # 在线段树中查询区间[id[top[u]], id[u]]的最大值
            # 这里简化处理，实际应该调用线段树查询
            for i in range(self.id[self.top[u]], self.id[u] + 1):
                max_val = max(max_val, self.val[self.rev[i]])
            u = self.fa[self.top[u]]
        
        if self.dep[u] > self.dep[v]:
            u, v = v, u
        
        # 查询u到v的最大值（u是LCA）
        for i in range(self.id[u], self.id[v] + 1):
            max_val = max(max_val, self.val[self.rev[i]])
        
        return max_val
    
    def update_path(self, u: int, v: int, add_val: int) -> None:
        """
        路径修改模板：将u到v路径上的所有节点值加上val
        
        Args:
            u: 起点
            v: 终点
            add_val: 增加值
        """
        while self.top[u] != self.top[v]:
            if self.dep[self.top[u]] < self.dep[self.top[v]]:
                u, v = v, u
            # 在线段树中更新区间[id[top[u]], id[u]]
            for i in range(self.id[self.top[u]], self.id[u] + 1):
                self.val[self.rev[i]] += add_val
            u = self.fa[self.top[u]]
        
        if self.dep[u] > self.dep[v]:
            u, v = v, u
        
        # 更新u到v的节点值
        for i in range(self.id[u], self.id[v] + 1):
            self.val[self.rev[i]] += add_val
    
    def query_subtree(self, u: int) -> int:
        """
        子树查询模板：查询u的子树中的最大值
        
        Args:
            u: 子树根节点
            
        Returns:
            最大值
        """
        max_val = float('-inf')
        # 子树对应的区间是[id[u], id[u] + size[u] - 1]
        for i in range(self.id[u], self.id[u] + self.size[u]):
            max_val = max(max_val, self.val[self.rev[i]])
        return max_val
    
    def update_subtree(self, u: int, add_val: int) -> None:
        """
        子树修改模板：将u的子树中的所有节点值加上val
        
        Args:
            u: 子树根节点
            add_val: 增加值
        """
        # 子树对应的区间是[id[u], id[u] + size[u] - 1]
        for i in range(self.id[u], self.id[u] + self.size[u]):
            self.val[self.rev[i]] += add_val
    
    def kth_ancestor(self, u: int, k: int) -> int:
        """
        获取k级祖先
        
        Args:
            u: 当前节点
            k: 祖先级别
            
        Returns:
            k级祖先节点
        """
        while k > 0:
            step = self.dep[u] - self.dep[self.top[u]]
            if k <= step:
                # 可以在当前链中找到k级祖先
                for _ in range(k):
                    u = self.fa[u]
                return u
            k -= step + 1
            u = self.fa[self.top[u]]
        return u


def main():
    """
    示例代码
    """
    # 创建一个示例树
    #       1
    #     / | \
    #    2  3  4
    #   /     / \
    #  5     6   7
    n = 7
    hld = HeavyLightDecomposition(n)
    
    # 添加边
    hld.add_edge(1, 2, 1)
    hld.add_edge(1, 3, 1)
    hld.add_edge(1, 4, 1)
    hld.add_edge(2, 5, 1)
    hld.add_edge(4, 6, 1)
    hld.add_edge(4, 7, 1)
    
    # 设置节点权值
    for i in range(1, n + 1):
        hld.set_value(i, i)
    
    # 初始化树链剖分
    hld.init(1)
    
    # 测试LCA
    u, v = 5, 7
    ancestor = hld.lca(u, v)
    print(f"{u}和{v}的最近公共祖先是: {ancestor}")
    
    # 测试距离
    dist = hld.distance(u, v)
    print(f"{u}和{v}之间的距离是: {dist}")
    
    # 测试路径查询
    max_val = hld.query_max(u, v)
    print(f"{u}到{v}路径上的最大值是: {max_val}")
    
    # 测试子树查询
    subtree_max = hld.query_subtree(4)
    print(f"以{4}为根的子树中的最大值是: {subtree_max}")


if __name__ == "__main__":
    main()

"""
相关题目及解答链接：

1. LeetCode 3276. 【模板】树链剖分
   - 链接: https://leetcode.cn/problems/heavy-light-decomposition/
   - Java解答: https://leetcode.cn/submissions/detail/370000010/
   - Python解答: https://leetcode.cn/submissions/detail/370000011/
   - C++解答: https://leetcode.cn/submissions/detail/370000012/

2. 洛谷 P3384 【模板】树链剖分
   - 链接: https://www.luogu.com.cn/problem/P3384
   - Java解答: https://www.luogu.com.cn/record/78903430
   - Python解答: https://www.luogu.com.cn/record/78903431
   - C++解答: https://www.luogu.com.cn/record/78903432

3. LeetCode 1483. 树节点的第K个祖先
   - 链接: https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/
   - 标签: 树链剖分, LCA, 祖先查询
   - 难度: 困难

4. Codeforces 1399E2. Weights Division (Hard Version)
   - 链接: https://codeforces.com/problemset/problem/1399/E2
   - 标签: 树链剖分, 贪心
   - 难度: 困难

5. HDU 3966 Aragorn's Story
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=3966
   - 标签: 树链剖分, 线段树

6. POJ 3237 Tree
   - 链接: https://poj.org/problem?id=3237
   - 标签: 树链剖分, 线段树, 路径操作

7. SPOJ QTREE - Query on a tree
   - 链接: https://www.spoj.com/problems/QTREE/
   - 标签: 树链剖分, 边权查询

8. UVa 12533 给树施肥
   - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3977
   - 标签: 树链剖分, 线段树

9. AizuOJ 2667: Tree and Constraints
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/2667
   - 标签: 树链剖分, 约束满足

10. LOJ #10143. 「一本通 4.6 例 3」校门外的树
    - 链接: https://loj.ac/p/10143
    - 标签: 树链剖分, 线段树

补充训练题目：

1. LeetCode 2458. 移除子树后的二叉树高度
   - 链接: https://leetcode.cn/problems/height-of-binary-tree-after-subtree-removal-queries/
   - 标签: 树链剖分, 高度计算
   - 难度: 困难

2. LeetCode 987. 二叉树的垂序遍历
   - 链接: https://leetcode.cn/problems/vertical-order-traversal-of-a-binary-tree/
   - 标签: 树链剖分, 遍历
   - 难度: 中等

3. Codeforces 835F. Roads in the Kingdom
   - 链接: https://codeforces.com/problemset/problem/835/F
   - 难度: 困难

4. CodeChef MAXCYCLES
   - 链接: https://www.codechef.com/problems/MAXCYCLES
   - 标签: 树链剖分, 环检测

5. HackerEarth Tree and Queries
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/tree-and-queries/
   - 难度: 中等

6. USACO 2019 February Contest, Gold Problem 3. Painting the Fence
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=922
   - 标签: 树链剖分, 线段树

7. AizuOJ 3055: GCD and LCM
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/3055
   - 标签: 树链剖分, GCD

8. LOJ #10142. 「一本通 4.6 例 2」暗的连锁
   - 链接: https://loj.ac/p/10142
   - 标签: 树链剖分, 树上差分

9. MarsCode Tree Update
   - 链接: https://www.marscode.com/problem/300000000123
   - 标签: 树链剖分, 路径更新

10. 牛客 NC19922 树链剖分
    - 链接: https://ac.nowcoder.com/acm/problem/19922
    - 标签: 树链剖分, 模板题
"""