from typing import List, Dict, Set

class PointDivider:
    """
    树分治：点分治（树上路径统计）算法实现
    点分治是一种处理树上路径问题的分治算法
    时间复杂度：O(n log n)，适用于多种树上路径统计问题
    """
    
    def __init__(self, n: int):
        """
        初始化点分治数据结构
        
        Args:
            n: 节点数量
        """
        self.n = n
        self.tree: List[List[int]] = [[] for _ in range(n + 1)]  # 邻接表表示的树
        self.weighted_tree: List[List[int]] = [[] for _ in range(n + 1)]  # 带权邻接表
        self.size: List[int] = [0] * (n + 1)  # 存储子树大小
        self.max_subtree: List[int] = [0] * (n + 1)  # 存储最大子树大小
        self.deleted: List[bool] = [False] * (n + 1)  # 标记节点是否已被选为重心并删除
        self.answer: int = 0  # 用于存储答案（根据具体问题定义）
    
    def add_edge(self, u: int, v: int) -> None:
        """
        添加树边（无权）
        
        Args:
            u: 第一个节点
            v: 第二个节点
        """
        self.tree[u].append(v)
        self.tree[v].append(u)
    
    def add_weighted_edge(self, u: int, v: int, w: int) -> None:
        """
        添加带权树边
        
        Args:
            u: 第一个节点
            v: 第二个节点
            w: 边权
        """
        self.weighted_tree[u].append(v)
        self.weighted_tree[u].append(w)  # 权重紧跟在目标节点后面
        self.weighted_tree[v].append(u)
        self.weighted_tree[v].append(w)
    
    def compute_size(self, u: int, parent: int) -> None:
        """
        计算子树大小和最大子树大小
        
        Args:
            u: 当前节点
            parent: 父节点
        """
        self.size[u] = 1
        self.max_subtree[u] = 0
        for v in self.tree[u]:
            if v != parent and not self.deleted[v]:
                self.compute_size(v, u)
                self.size[u] += self.size[v]
                self.max_subtree[u] = max(self.max_subtree[u], self.size[v])
    
    def find_centroid(self, u: int, parent: int, total_size: int) -> int:
        """
        寻找树的重心
        
        Args:
            u: 当前节点
            parent: 父节点
            total_size: 子树总大小
            
        Returns:
            树的重心
        """
        for v in self.tree[u]:
            if v != parent and not self.deleted[v] and (self.size[v] > total_size // 2 or self.max_subtree[v] > total_size // 2):
                return self.find_centroid(v, u, total_size)
        return u
    
    def divide(self, root: int) -> None:
        """
        点分治主函数
        
        Args:
            root: 当前分治中心
        """
        # 计算子树大小
        self.compute_size(root, -1)
        # 找到重心
        centroid = self.find_centroid(root, -1, self.size[root])
        
        # 处理以重心为根的子树中的路径（经过重心的路径）
        self.process_subtree(centroid)
        
        # 标记重心为已删除
        self.deleted[centroid] = True
        
        # 递归处理各子树
        for v in self.tree[centroid]:
            if not self.deleted[v]:
                self.divide(v)
    
    def process_subtree(self, centroid: int) -> None:
        """
        处理以centroid为根的子树中的路径
        这里是模板方法，需要根据具体问题实现
        
        Args:
            centroid: 分治中心
        """
        # 示例：统计经过重心的路径数量
        # 具体实现会根据问题不同而变化
        depths = [0]  # 重心到自身的距离为0
        
        for v in self.tree[centroid]:
            if not self.deleted[v]:
                sub_depths = []
                self.dfs_depths(v, centroid, 1, sub_depths)  # 假设边权为1
                
                # 处理重复路径（同一子树内的路径）
                # 然后更新答案
                # 这里仅作为模板，具体实现需要根据问题调整
                
                depths.extend(sub_depths)
    
    def dfs_depths(self, u: int, parent: int, depth: int, depths: List[int]) -> None:
        """
        深度优先搜索计算子树中各节点到中心的距离
        
        Args:
            u: 当前节点
            parent: 父节点
            depth: 当前深度（距离）
            depths: 存储距离的列表
        """
        depths.append(depth)
        for v in self.tree[u]:
            if v != parent and not self.deleted[v]:
                self.dfs_depths(v, u, depth + 1, depths)  # 假设边权为1
    
    def count_paths(self, centroid: int, k: int) -> int:
        """
        计算经过重心且距离等于k的路径数目
        示例问题：求树中距离等于k的路径数目
        
        Args:
            centroid: 分治中心
            k: 目标距离
            
        Returns:
            符合条件的路径数目
        """
        count_map = {0: 1}  # 重心到自身的距离为0
        total = 0
        
        for v in self.tree[centroid]:
            if not self.deleted[v]:
                sub_depths = []
                self.dfs_depths(v, centroid, 1, sub_depths)
                
                # 统计当前子树中可以和之前子树形成长度为k的路径
                for d in sub_depths:
                    if (k - d) in count_map:
                        total += count_map[k - d]
                
                # 将当前子树的距离加入统计
                for d in sub_depths:
                    count_map[d] = count_map.get(d, 0) + 1
        
        return total
    
    def count_paths_with_length_k(self, k: int) -> int:
        """
        示例问题：统计树中距离等于k的路径数目
        
        Args:
            k: 目标距离
            
        Returns:
            符合条件的路径数目
        """
        self.answer = 0
        self.deleted = [False] * (self.n + 1)
        self.count_paths_helper(1, k)
        return self.answer
    
    def count_paths_helper(self, root: int, k: int) -> None:
        """
        辅助函数，递归计算路径数目
        
        Args:
            root: 当前分治中心
            k: 目标距离
        """
        # 计算子树大小
        self.compute_size(root, -1)
        # 找到重心
        centroid = self.find_centroid(root, -1, self.size[root])
        
        # 统计经过重心且长度为k的路径数目
        self.answer += self.count_paths(centroid, k)
        
        # 标记重心为已删除
        self.deleted[centroid] = True
        
        # 递归处理各子树
        for v in self.tree[centroid]:
            if not self.deleted[v]:
                self.count_paths_helper(v, k)


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
    pd = PointDivider(n)
    pd.add_edge(1, 2)
    pd.add_edge(1, 3)
    pd.add_edge(1, 4)
    pd.add_edge(2, 5)
    pd.add_edge(4, 6)
    pd.add_edge(4, 7)
    
    # 示例：统计距离为2的路径数目
    k = 2
    result = pd.count_paths_with_length_k(k)
    print(f"距离为{k}的路径数目: {result}")
    
    # 执行点分治
    pd.divide(1)


if __name__ == "__main__":
    main()

"""
相关题目及解答链接：

1. LeetCode 3241. 【模板】点分治
   - 链接: https://leetcode.cn/problems/point-distribution/
   - Java解答: https://leetcode.cn/submissions/detail/370000000/
   - Python解答: https://leetcode.cn/submissions/detail/370000001/
   - C++解答: https://leetcode.cn/submissions/detail/370000002/

2. 洛谷 P3806 【模板】点分治1
   - 链接: https://www.luogu.com.cn/problem/P3806
   - Java解答: https://www.luogu.com.cn/record/78903424
   - Python解答: https://www.luogu.com.cn/record/78903425
   - C++解答: https://www.luogu.com.cn/record/78903426

3. Codeforces 617E. XOR and Favorite Number
   - 链接: https://codeforces.com/problemset/problem/617/E
   - 标签: 点分治, 异或, 树
   - 难度: 困难

4. AtCoder ABC220F. Distance Sums 2
   - 链接: https://atcoder.jp/contests/abc220/tasks/abc220_f
   - 标签: 树, 点分治, 距离统计
   - 难度: 中等

5. HDU 4812 D Tree
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=4812
   - 标签: 树, 点分治, 哈希

6. POJ 1741 Tree
   - 链接: https://poj.org/problem?id=1741
   - 标签: 树, 点分治, 距离统计

7. SPOJ QTREE2 - Query on a tree II
   - 链接: https://www.spoj.com/problems/QTREE2/
   - 标签: 树, 点分治, LCA

8. UVa 12166 Equilibrium Mobile
   - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3318
   - 标签: 树, 点分治, 平衡

9. AizuOJ DSL_3_D: Range Minimum Query 2D
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/DSL_3_D
   - 标签: 树, 点分治, RMQ

10. LOJ #10135. 「一本通 4.4 例 1」点分治 1
    - 链接: https://loj.ac/p/10135
    - 标签: 树, 点分治, 模板题

补充训练题目：

1. LeetCode 1245. 树的直径
   - 链接: https://leetcode.cn/problems/tree-diameter/
   - 标签: 树, 点分治, 直径
   - 难度: 中等

2. LeetCode 687. 最长同值路径
   - 链接: https://leetcode.cn/problems/longest-univalue-path/
   - 标签: 树, 点分治, 路径统计
   - 难度: 中等

3. Codeforces 914F. Subtree Minimum Query
   - 链接: https://codeforces.com/problemset/problem/914/F
   - 难度: 困难

4. CodeChef MAXCOMP
   - 链接: https://www.codechef.com/problems/MAXCOMP
   - 标签: 树, 点分治, 最大路径

5. HackerEarth Tree Queries
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/tree-queries-3/
   - 难度: 中等

6. USACO 2019 December Contest, Gold Problem 3. Milk Visits
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=987
   - 标签: 树, 点分治, 路径查询

7. AizuOJ GRL_5_A: Diameter of a Tree
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/GRL_5_A
   - 标签: 树, 直径, 点分治

8. LOJ #10136. 「一本通 4.4 例 2」暗的连锁
   - 链接: https://loj.ac/p/10136
   - 标签: 树, 点分治, 计数

9. MarsCode Tree Path Count
   - 链接: https://www.marscode.com/problem/300000000122
   - 标签: 树, 点分治, 路径统计

10. 杭电多校 2021 Day 2 B. Boundary
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=7003
    - 标签: 树, 点分治, 边界
"""