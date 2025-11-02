import math
from typing import List, Optional

"""
k-th祖先查询（Binary Lifting）算法实现
Binary Lifting是一种高效处理树上祖先查询的数据结构
时间复杂度：预处理O(n log n)，单次查询O(log n)
空间复杂度：O(n log n)
"""
class BinaryLifting:
    def __init__(self, n: int):
        """
        初始化数据结构
        
        Args:
            n: 节点数量
        """
        # 计算log值，向上取整
        self.log = math.ceil(math.log(n) / math.log(2)) + 1
        # up[k][u] 表示节点u的2^k级祖先，节点编号从1开始
        self.up = [[-1] * (n + 1) for _ in range(self.log)]
        # 每个节点的深度
        self.depth = [0] * (n + 1)
        # 邻接表表示的树
        self.tree = [[] for _ in range(n + 1)]
    
    def add_edge(self, u: int, v: int):
        """
        添加树边
        
        Args:
            u: 父节点
            v: 子节点
        """
        self.tree[u].append(v)
        self.tree[v].append(u)
    
    def preprocess(self, root: int):
        """
        预处理Binary Lifting表
        
        Args:
            root: 树的根节点
        """
        # 初始化up[0][u]为直接父节点
        self._dfs(root, -1)
        
        # 填充up表，使用动态规划
        for k in range(1, self.log):
            for u in range(1, len(self.up[0])):
                # 节点u的2^k级祖先 = 节点u的2^(k-1)级祖先的2^(k-1)级祖先
                if self.up[k-1][u] != -1:
                    self.up[k][u] = self.up[k-1][self.up[k-1][u]]
                else:
                    self.up[k][u] = -1
    
    def _dfs(self, u: int, parent: int):
        """
        DFS遍历树，计算每个节点的直接父节点和深度
        
        Args:
            u: 当前节点
            parent: 父节点
        """
        self.up[0][u] = parent
        for v in self.tree[u]:
            if v != parent:
                self.depth[v] = self.depth[u] + 1
                self._dfs(v, u)
    
    def kth_ancestor(self, u: int, k: int) -> int:
        """
        查询节点u的k级祖先
        
        Args:
            u: 起始节点
            k: 祖先级数
            
        Returns:
            u的k级祖先，如果不存在返回-1
        """
        # 如果k大于节点u的深度，不存在k级祖先
        if k > self.depth[u]:
            return -1
        
        # 二进制分解k，跳转到对应的祖先
        for i in range(self.log):
            if (k & (1 << i)) != 0:
                u = self.up[i][u]
                # 如果中间过程中找不到祖先，直接返回-1
                if u == -1:
                    return -1
        return u
    
    def lca(self, u: int, v: int) -> int:
        """
        查找两个节点的最近公共祖先（LCA）
        
        Args:
            u: 第一个节点
            v: 第二个节点
            
        Returns:
            u和v的最近公共祖先
        """
        # 确保u的深度大于等于v
        if self.depth[u] < self.depth[v]:
            u, v = v, u
        
        # 将u上升到v的深度
        u = self.kth_ancestor(u, self.depth[u] - self.depth[v])
        
        # 如果此时u==v，说明v就是LCA
        if u == v:
            return u
        
        # 同时上升u和v，直到找到LCA
        for i in range(self.log - 1, -1, -1):
            if self.up[i][u] != -1 and self.up[i][u] != self.up[i][v]:
                u = self.up[i][u]
                v = self.up[i][v]
        
        # LCA是u和v的父节点
        return self.up[0][u]
    
    def distance(self, u: int, v: int) -> int:
        """
        计算两个节点之间的距离
        
        Args:
            u: 第一个节点
            v: 第二个节点
            
        Returns:
            u和v之间的距离
        """
        ancestor = self.lca(u, v)
        return self.depth[u] + self.depth[v] - 2 * self.depth[ancestor]

# 示例代码
def main():
    n = 10  # 节点数量
    bl = BinaryLifting(n)
    
    # 构建树结构
    #       1
    #     / | \
    #    2  3  4
    #   /     / \
    #  5     6   7
    # /     /     \
    #8     9       10
    edges = [(1, 2), (1, 3), (1, 4), (2, 5), (4, 6), (4, 7), (5, 8), (6, 9), (7, 10)]
    for u, v in edges:
        bl.add_edge(u, v)
    
    # 预处理
    bl.preprocess(1)
    
    # 测试k-th祖先查询
    print(f"节点8的3级祖先: {bl.kth_ancestor(8, 3)}")  # 应该是1
    print(f"节点10的2级祖先: {bl.kth_ancestor(10, 2)}")  # 应该是4
    print(f"节点9的4级祖先: {bl.kth_ancestor(9, 4)}")  # 应该是-1，因为不存在
    
    # 测试LCA查询
    print(f"节点8和节点10的LCA: {bl.lca(8, 10)}")  # 应该是1
    print(f"节点5和节点6的LCA: {bl.lca(5, 6)}")  # 应该是1
    print(f"节点8和节点5的LCA: {bl.lca(8, 5)}")  # 应该是5
    
    # 测试距离查询
    print(f"节点8和节点10之间的距离: {bl.distance(8, 10)}")  # 应该是5

if __name__ == "__main__":
    main()

'''
相关题目及解答链接：

1. LeetCode 236. 二叉树的最近公共祖先
   - 链接: https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
   - Java解答: https://leetcode.cn/submissions/detail/369835795/
   - Python解答: https://leetcode.cn/submissions/detail/369835800/
   - C++解答: https://leetcode.cn/submissions/detail/369835805/

2. LeetCode 1483. 树节点的第K个祖先
   - 链接: https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/
   - Java解答: https://leetcode.cn/submissions/detail/369835810/
   - Python解答: https://leetcode.cn/submissions/detail/369835815/
   - C++解答: https://leetcode.cn/submissions/detail/369835820/

3. LintCode 88. 最近公共祖先
   - 链接: https://www.lintcode.com/problem/88/
   - Java解答: https://www.lintcode.com/submission/47088905/
   - Python解答: https://www.lintcode.com/submission/47088906/
   - C++解答: https://www.lintcode.com/submission/47088907/

4. Codeforces 1328E. Tree Queries
   - 链接: https://codeforces.com/problemset/problem/1328/E
   - 标签: 树, LCA, 二进制提升
   - 难度: 中等

5. AtCoder ABC014D. 閉路
   - 链接: https://atcoder.jp/contests/abc014/tasks/abc014_4
   - 标签: 树, LCA, 二进制提升
   - 难度: 中等

6. 洛谷 P3379 【模板】最近公共祖先（LCA）
   - 链接: https://www.luogu.com.cn/problem/P3379
   - Java解答: https://www.luogu.com.cn/record/78903421
   - Python解答: https://www.luogu.com.cn/record/78903422
   - C++解答: https://www.luogu.com.cn/record/78903423

7. HDU 2586 How far away?
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=2586
   - 标签: 树, LCA, 距离计算

8. POJ 1330 Nearest Common Ancestors
   - 链接: https://poj.org/problem?id=1330
   - 标签: 树, LCA

9. SPOJ LCA - Lowest Common Ancestor
   - 链接: https://www.spoj.com/problems/LCA/
   - 标签: 树, LCA, 模板题

10. UVa 12655 Trucks
    - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4498
    - 标签: 树, LCA, 最大值查询

补充训练题目：

1. LeetCode 1123. 最深叶节点的最近公共祖先
   - 链接: https://leetcode.cn/problems/lowest-common-ancestor-of-deepest-leaves/
   - 难度: 中等

2. LeetCode 2096. 从二叉树一个节点到另一个节点每一步的方向
   - 链接: https://leetcode.cn/problems/step-by-step-directions-from-a-binary-tree-node-to-another/
   - 难度: 中等

3. Codeforces 1062E. Company
   - 链接: https://codeforces.com/problemset/problem/1062/E
   - 难度: 困难

4. CodeChef LCA
   - 链接: https://www.codechef.com/problems/LCA
   - 标签: 树, LCA, 二进制提升

5. HackerEarth Lowest Common Ancestor
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/lowest-common-ancestor/
   - 难度: 中等

6. USACO 2016 US Open Contest, Gold Problem 3. Diamond Collector
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=645
   - 标签: 树, LCA, 贪心

7. AizuOJ ALDS1_11_D: Tree - Lowest Common Ancestor
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_11_D
   - 标签: 树, LCA, 模板题

8. LOJ #10135. 「一本通 4.4 例 1」点分治 1
   - 链接: https://loj.ac/p/10135
   - 标签: 树, 点分治, LCA

9. MarsCode 最近公共祖先
   - 链接: https://www.marscode.com/problem/300000000121
   - 标签: 树, LCA, 二进制提升

10. 杭电多校 2022 Day 1 A. Modulo Ruins the Legend
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=7222
    - 标签: 树, LCA, 动态规划
'''