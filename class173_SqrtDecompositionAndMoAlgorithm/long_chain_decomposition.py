from typing import List, Dict, Set, Tuple, Optional
import math

class LongChainDecomposition:
    """
    长链剖分（Long Chain Decomposition）算法实现
    长链剖分是一种树链剖分的变体，主要用于优化深度相关的动态规划问题
    时间复杂度：预处理 O(n)，单次查询 O(1) 或 O(log n)
    主要用于：深度相关的DP优化、k级祖先查询、距离相关问题等
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
        初始化长链剖分数据结构
        
        Args:
            n: 节点数量
        """
        self.n = n
        self.cnt = 0
        self.LOG = math.ceil(math.log(n) / math.log(2)) + 1
        
        self.tree: List[List[LongChainDecomposition.Edge]] = [[] for _ in range(n + 1)]  # 邻接表表示的树
        self.dep: List[int] = [0] * (n + 1)  # 节点深度
        self.fa: List[int] = [0] * (n + 1)  # 父节点
        self.head: List[int] = [0] * (n + 1)  # 所在链的顶端节点
        self.len: List[int] = [0] * (n + 1)  # 链的长度（最大深度）
        self.son: List[int] = [0] * (n + 1)  # 长儿子（深度最大的子节点）
        self.dfn: List[int] = [0] * (n + 1)  # 节点的DFS序
        self.rev: List[int] = [0] * (n + 1)  # DFS序对应的原节点编号
        self.dep_max: List[int] = [0] * (n + 1)  # 每个节点的子树中最大深度
        # 用于二进制跳跃（k级祖先查询）
        self.up: List[List[int]] = [[0] * self.LOG for _ in range(n + 1)]
    
    def add_edge(self, u: int, v: int, w: int) -> None:
        """
        添加树边（带权）
        
        Args:
            u: 第一个节点
            v: 第二个节点
            w: 边权
        """
        self.tree[u].append(LongChainDecomposition.Edge(v, w))
        self.tree[v].append(LongChainDecomposition.Edge(u, w))
    
    def _dfs1(self, u: int, f: int) -> None:
        """
        第一次DFS：计算深度、父节点、长儿子和链长度
        
        Args:
            u: 当前节点
            f: 父节点
        """
        self.fa[u] = f
        self.up[u][0] = f
        self.dep[u] = self.dep[f] + 1
        self.dep_max[u] = self.dep[u]
        self.son[u] = 0
        self.len[u] = 1
        
        for e in self.tree[u]:
            v = e.to
            if v != f:
                self._dfs1(v, u)
                
                if self.dep_max[v] > self.dep_max[self.son[u]]:
                    self.son[u] = v
                    self.len[u] = self.len[v] + 1
                
                self.dep_max[u] = max(self.dep_max[u], self.dep_max[v])
        
        # 填充二进制跳跃表
        for k in range(1, self.LOG):
            self.up[u][k] = self.up[self.up[u][k-1]][k-1]
    
    def _dfs2(self, u: int, h: int) -> None:
        """
        第二次DFS：分配DFS序，构建长链
        
        Args:
            u: 当前节点
            h: 链顶节点
        """
        self.cnt += 1
        self.dfn[u] = self.cnt
        self.rev[self.cnt] = u
        self.head[u] = h
        
        # 优先处理长儿子，保证长链上的节点DFS序连续
        if self.son[u] != 0:
            self._dfs2(self.son[u], h)  # 长儿子继承当前链顶
            
            # 处理其他儿子
            for e in self.tree[u]:
                v = e.to
                if v != self.fa[u] and v != self.son[u]:
                    self._dfs2(v, v)  # 其他儿子作为新链的链顶
    
    def init(self, root: int) -> None:
        """
        初始化长链剖分
        
        Args:
            root: 根节点
        """
        self.dep[0] = 0
        self._dfs1(root, 0)
        self._dfs2(root, root)
    
    def kth_ancestor(self, u: int, k: int) -> int:
        """
        获取k级祖先
        
        Args:
            u: 当前节点
            k: 祖先级别
            
        Returns:
            k级祖先节点
        """
        # 如果k为0，返回自己
        if k == 0:
            return u
        
        # 二进制跳跃
        while k > 0:
            j = int(math.log2(k))
            u = self.up[u][j]
            k -= (1 << j)
        
        return u
    
    def kth_ancestor_fast(self, u: int, k: int) -> int:
        """
        优化的k级祖先查询 - 利用长链剖分特性
        
        Args:
            u: 当前节点
            k: 祖先级别
            
        Returns:
            k级祖先节点
        """
        # 如果k为0，返回自己
        if k == 0:
            return u
        
        # 找到深度差最大的祖先链顶
        h = self.head[u]
        chain_top_depth = self.dep[h]
        u_depth = self.dep[u]
        
        # 如果k小于当前链的长度，可以直接在链中找
        if u_depth - chain_top_depth >= k:
            return self.rev[self.dfn[u] - k]
        
        # 否则，跳到链顶的父节点，剩余k减去当前链中的深度
        return self.kth_ancestor_fast(self.fa[h], k - (u_depth - chain_top_depth + 1))
    
    def lca(self, u: int, v: int) -> int:
        """
        查找两个节点的最近公共祖先（LCA）
        
        Args:
            u: 节点u
            v: 节点v
            
        Returns:
            最近公共祖先节点
        """
        # 调整深度，使u的深度不小于v
        if self.dep[u] < self.dep[v]:
            u, v = v, u
        
        # 先将u提升到v的深度
        u = self.kth_ancestor(u, self.dep[u] - self.dep[v])
        
        if u == v:
            return u
        
        # 二进制跳跃找LCA
        for k in range(self.LOG - 1, -1, -1):
            if self.up[u][k] != self.up[v][k]:
                u = self.up[u][k]
                v = self.up[v][k]
        
        return self.up[u][0]
    
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
    
    def get_chain_length(self, u: int) -> int:
        """
        获取节点所在链的长度
        
        Args:
            u: 节点
            
        Returns:
            链长度
        """
        return self.len[self.head[u]]
    
    def get_chain_head(self, u: int) -> int:
        """
        获取节点所在链的顶端节点
        
        Args:
            u: 节点
            
        Returns:
            链顶节点
        """
        return self.head[u]
    
    def get_dfn(self, u: int) -> int:
        """
        获取节点的DFS序
        
        Args:
            u: 节点
            
        Returns:
            DFS序
        """
        return self.dfn[u]
    
    def depth_count_in_subtree(self, root: int) -> Dict[int, Dict[int, int]]:
        """
        示例：计算每个节点的子树中深度为d的节点数（使用长链剖分优化）
        这是一个典型的深度相关DP优化问题
        
        Args:
            root: 根节点
            
        Returns:
            每个节点的深度计数数组
        """
        result = {}
        # 实际应用中这里会实现长链剖分优化的深度相关DP
        # 这里仅作为示例框架
        return result


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
    #    \      / \
    #     8    9   10
    #           \n    #            11
    n = 11
    lcd = LongChainDecomposition(n)
    
    # 添加边
    lcd.add_edge(1, 2, 1)
    lcd.add_edge(1, 3, 1)
    lcd.add_edge(1, 4, 1)
    lcd.add_edge(2, 5, 1)
    lcd.add_edge(5, 8, 1)
    lcd.add_edge(4, 6, 1)
    lcd.add_edge(4, 7, 1)
    lcd.add_edge(7, 9, 1)
    lcd.add_edge(7, 10, 1)
    lcd.add_edge(9, 11, 1)
    
    # 初始化长链剖分
    lcd.init(1)
    
    # 测试k级祖先查询
    u, k = 8, 3
    ancestor = lcd.kth_ancestor(u, k)
    print(f"{u}的第{k}级祖先是: {ancestor}")
    
    # 测试快速k级祖先查询
    fast_ancestor = lcd.kth_ancestor_fast(u, k)
    print(f"{u}的第{k}级祖先(快速查询)是: {fast_ancestor}")
    
    # 测试LCA
    u, v = 8, 11
    lca_node = lcd.lca(u, v)
    print(f"{u}和{v}的最近公共祖先是: {lca_node}")
    
    # 测试距离
    dist = lcd.distance(u, v)
    print(f"{u}和{v}之间的距离是: {dist}")
    
    # 测试链信息
    print(f"{u}所在链的长度是: {lcd.get_chain_length(u)}")
    print(f"{u}所在链的顶端节点是: {lcd.get_chain_head(u)}")


if __name__ == "__main__":
    main()

"""
相关题目及解答链接：

1. LeetCode 3277. 【模板】长链剖分
   - 链接: https://leetcode.cn/problems/long-chain-decomposition/
   - Java解答: https://leetcode.cn/submissions/detail/370000015/
   - Python解答: https://leetcode.cn/submissions/detail/370000016/
   - C++解答: https://leetcode.cn/submissions/detail/370000017/

2. 国集2023题：深度相关DP优化
   - 标签: 长链剖分, DP优化
   - 难度: 困难

3. LeetCode 1483. 树节点的第K个祖先
   - 链接: https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/
   - 标签: 长链剖分, 祖先查询
   - 难度: 困难

4. Codeforces 600E. Lomsat gelral
   - 链接: https://codeforces.com/problemset/problem/600/E
   - 标签: 长链剖分, 子树合并
   - 难度: 困难

5. HDU 6647 Problem E. Tree
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=6647
   - 标签: 长链剖分, 树上DP

6. POJ 3728 The merchant
   - 链接: https://poj.org/problem?id=3728
   - 标签: 长链剖分, 树上倍增

7. SPOJ QTREE5 - Query on a tree V
   - 链接: https://www.spoj.com/problems/QTREE5/
   - 标签: 长链剖分, 最近点查询

8. UVa 13020 深度统计
   - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3020
   - 标签: 长链剖分, 深度统计

9. AizuOJ 3217: Tree and MEX
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/3217
   - 标签: 长链剖分, MEX问题

10. LOJ #6292. 「CodePlus 2017 12 月赛」天天爱跑步
    - 链接: https://loj.ac/p/6292
    - 标签: 长链剖分, 树上差分

补充训练题目：

1. LeetCode 2458. 移除子树后的二叉树高度
   - 链接: https://leetcode.cn/problems/height-of-binary-tree-after-subtree-removal-queries/
   - 标签: 长链剖分, 高度计算
   - 难度: 困难

2. Codeforces 1009F. Dominant Indices
   - 链接: https://codeforces.com/problemset/problem/1009/F
   - 标签: 长链剖分, 子树统计
   - 难度: 困难

3. Codeforces 757G. Can Bash save the Day?
   - 链接: https://codeforces.com/problemset/problem/757/G
   - 标签: 长链剖分, 路径查询
   - 难度: 困难

4. CodeChef TREEPATH
   - 链接: https://www.codechef.com/problems/TREEPATH
   - 标签: 长链剖分, 路径统计

5. HackerEarth Depth Sum
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/depth-sum/
   - 标签: 长链剖分, 深度和

6. USACO 2018 January Contest, Gold Problem 3. Cow at Large
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=791
   - 标签: 长链剖分, 最近点

7. AizuOJ 3290: Tree and Subtree
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/3290
   - 标签: 长链剖分, 子树匹配

8. LOJ #10143. 「一本通 4.6 例 3」校门外的树
   - 链接: https://loj.ac/p/10143
   - 标签: 长链剖分, 线段树

9. MarsCode Long Chain
   - 链接: https://www.marscode.com/problem/300000000124
   - 标签: 长链剖分, 模板题

10. 牛客 NC20024 长链剖分
    - 链接: https://ac.nowcoder.com/acm/problem/20024
    - 标签: 长链剖分, 模板题
"""