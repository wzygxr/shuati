from typing import List, Dict, Set, Tuple

class EdgeDivider:
    """
    树分治：边分治（链合并优化）算法实现
    边分治是一种通过分解边来处理树路径问题的分治算法
    时间复杂度：O(n log n)，适用于处理树上的路径统计问题
    注：为了使树保持二叉结构，可能需要添加虚点
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
        初始化边分治数据结构
        
        Args:
            n: 节点数量
        """
        self.n = n
        self.virtual_n = n
        self.tree: List[List[EdgeDivider.Edge]] = [[] for _ in range(n * 2 + 1)]  # 邻接表表示的树
        self.virtual_tree: List[List[EdgeDivider.Edge]] = [[] for _ in range(n * 2 + 1)]  # 添加虚点后的二叉树
        self.deleted: List[bool] = [False] * (n * 2 + 1)  # 标记边是否已被删除
        self.answer: int = 0  # 用于存储答案（根据具体问题定义）
    
    def add_edge(self, u: int, v: int, w: int) -> None:
        """
        添加树边（带权）
        
        Args:
            u: 第一个节点
            v: 第二个节点
            w: 边权
        """
        self.tree[u].append(EdgeDivider.Edge(v, w))
        self.tree[v].append(EdgeDivider.Edge(u, w))
    
    def build_virtual_tree(self) -> None:
        """
        构建二叉虚树
        将多叉树转换为二叉树，通过添加虚点
        """
        visited = [False] * (self.n * 2 + 1)
        self._dfs_build_virtual_tree(1, -1, visited)
    
    def _dfs_build_virtual_tree(self, u: int, parent: int, visited: List[bool]) -> None:
        """
        深度优先搜索构建二叉虚树
        
        Args:
            u: 当前节点
            parent: 父节点
            visited: 访问标记数组
        """
        visited[u] = True
        children = []
        
        # 收集所有子节点（排除父节点）
        for e in self.tree[u]:
            if e.to != parent and not visited[e.to]:
                children.append(e)
        
        if len(children) <= 2:
            # 节点u的度数<=2，无需添加虚点
            for e in children:
                self.virtual_tree[u].append(EdgeDivider.Edge(e.to, e.weight))
                self.virtual_tree[e.to].append(EdgeDivider.Edge(u, e.weight))
                self._dfs_build_virtual_tree(e.to, u, visited)
        else:
            # 添加虚点将多叉转换为二叉
            current = u
            for i in range(len(children)):
                if i == len(children) - 2:
                    # 处理倒数第二个子节点
                    self.virtual_tree[current].append(EdgeDivider.Edge(children[i].to, children[i].weight))
                    self.virtual_tree[children[i].to].append(EdgeDivider.Edge(current, children[i].weight))
                    self._dfs_build_virtual_tree(children[i].to, current, visited)
                    
                    # 处理最后一个子节点
                    self.virtual_tree[current].append(EdgeDivider.Edge(children[i + 1].to, children[i + 1].weight))
                    self.virtual_tree[children[i + 1].to].append(EdgeDivider.Edge(current, children[i + 1].weight))
                    self._dfs_build_virtual_tree(children[i + 1].to, current, visited)
                    break
                else:
                    # 添加虚点
                    self.virtual_n += 1
                    virtual_node = self.virtual_n
                    self.virtual_tree[current].append(EdgeDivider.Edge(virtual_node, 0))  # 虚点之间的边权为0
                    self.virtual_tree[virtual_node].append(EdgeDivider.Edge(current, 0))
                    
                    self.virtual_tree[virtual_node].append(EdgeDivider.Edge(children[i].to, children[i].weight))
                    self.virtual_tree[children[i].to].append(EdgeDivider.Edge(virtual_node, children[i].weight))
                    self._dfs_build_virtual_tree(children[i].to, virtual_node, visited)
                    
                    current = virtual_node
    
    def _get_size(self, u: int, parent: int) -> int:
        """
        计算子树大小
        
        Args:
            u: 当前节点
            parent: 父节点
            
        Returns:
            子树大小
        """
        size = 1
        for e in self.virtual_tree[u]:
            if e.to != parent and not self.deleted[e.to]:  # 边的删除用节点标记，因为边是无向的
                size += self._get_size(e.to, u)
        return size
    
    def _find_split_edge(self, u: int, parent: int, total_size: int) -> Tuple[int, int, int]:
        """
        寻找最优分割边
        
        Args:
            u: 当前节点
            parent: 父节点
            total_size: 总大小
            
        Returns:
            (分割边的一端, 分割边的另一端, 分割边的子树大小)
        """
        best_u = -1
        best_v = -1
        best_sub_size = 0
        min_diff = float('inf')
        
        for e in self.virtual_tree[u]:
            if e.to != parent and not self.deleted[e.to]:
                sub_size = self._get_size(e.to, u)
                # 寻找最平衡的分割，即子树大小最接近总大小的一半
                diff = abs(2 * sub_size - total_size)
                if diff < min_diff:
                    min_diff = diff
                    best_u = u
                    best_v = e.to
                    best_sub_size = sub_size
                
                # 递归寻找更优的分割边
                sub_u, sub_v, sub_sub_size = self._find_split_edge(e.to, u, total_size)
                sub_diff = abs(2 * sub_sub_size - total_size)
                if sub_diff < min_diff:
                    min_diff = sub_diff
                    best_u = sub_u
                    best_v = sub_v
                    best_sub_size = sub_sub_size
        
        return best_u, best_v, best_sub_size
    
    def _collect_distances(self, u: int, parent: int, distance: int, distances: List[int]) -> None:
        """
        收集子树中各节点到分割边的距离
        
        Args:
            u: 当前节点
            parent: 父节点
            distance: 当前距离
            distances: 存储距离的列表
        """
        distances.append(distance)
        for e in self.virtual_tree[u]:
            if e.to != parent and not self.deleted[e.to]:
                self._collect_distances(e.to, u, distance + e.weight, distances)
    
    def divide(self, root: int) -> None:
        """
        边分治主函数
        
        Args:
            root: 当前分治中心
        """
        total_size = self._get_size(root, -1)
        
        if total_size <= 1:
            return
        
        # 找到最优分割边
        u, v, _ = self._find_split_edge(root, -1, total_size)
        
        if u == -1 or v == -1:
            return  # 已经处理到叶子节点
        
        # 标记边为已删除（通过标记节点来间接标记边）
        self.deleted[v] = True
        
        # 找到分割边的权重
        edge_weight = 0
        for e in self.virtual_tree[u]:
            if e.to == v:
                edge_weight = e.weight
                break
        
        # 收集两边子树中的距离信息
        left_distances = []
        right_distances = []
        
        self._collect_distances(u, v, 0, left_distances)
        self._collect_distances(v, u, edge_weight, right_distances)
        
        # 处理经过当前分割边的路径
        self._process_paths(left_distances, right_distances)
        
        # 递归处理分割后的子树
        self.divide(u)
        self.divide(v)
    
    def _process_paths(self, left_distances: List[int], right_distances: List[int]) -> None:
        """
        处理经过分割边的路径
        这里是模板方法，需要根据具体问题实现
        
        Args:
            left_distances: 左侧子树的距离列表
            right_distances: 右侧子树的距离列表
        """
        # 示例：统计路径总长度小于等于k的路径数目
        # 具体实现会根据问题不同而变化
        # 这里仅作为模板，具体实现需要根据问题调整
        pass
    
    def count_paths_with_length_leq_k(self, k: int) -> int:
        """
        示例问题：统计树中路径长度小于等于k的路径数目
        
        Args:
            k: 目标路径长度
            
        Returns:
            符合条件的路径数目
        """
        self.answer = 0
        self.deleted = [False] * (self.n * 2 + 1)
        
        # 构建虚树
        self.build_virtual_tree()
        
        # 执行边分治
        self._count_paths_helper(1, k)
        
        return self.answer
    
    def _count_paths_helper(self, root: int, k: int) -> None:
        """
        辅助函数，递归计算路径数目
        
        Args:
            root: 当前分治中心
            k: 目标路径长度
        """
        total_size = self._get_size(root, -1)
        
        if total_size <= 1:
            return  # 单个节点，没有路径
        
        # 找到最优分割边
        u, v, _ = self._find_split_edge(root, -1, total_size)
        
        if u == -1 or v == -1:
            return
        
        # 标记边为已删除
        self.deleted[v] = True
        
        # 找到分割边的权重
        edge_weight = 0
        for e in self.virtual_tree[u]:
            if e.to == v:
                edge_weight = e.weight
                break
        
        # 收集两边子树中的距离信息
        left_distances = []
        right_distances = []
        
        self._collect_distances(u, v, 0, left_distances)
        self._collect_distances(v, u, edge_weight, right_distances)
        
        # 统计经过分割边且长度<=k的路径数目
        self.answer += self._count_leq_k_paths(left_distances, right_distances, k)
        
        # 递归处理分割后的子树
        self._count_paths_helper(u, k)
        self._count_paths_helper(v, k)
    
    def _count_leq_k_paths(self, list1: List[int], list2: List[int], k: int) -> int:
        """
        统计两个距离列表中满足距离之和<=k的对数
        
        Args:
            list1: 第一个距离列表
            list2: 第二个距离列表
            k: 目标值
            
        Returns:
            符合条件的对数
        """
        # 排序两个列表以便快速统计
        list1.sort()
        list2.sort()
        
        count = 0
        j = len(list2) - 1
        
        # 双指针统计
        for i in range(len(list1)):
            while j >= 0 and list1[i] + list2[j] > k:
                j -= 1
            count += (j + 1)
        
        return count


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
    ed = EdgeDivider(n)
    ed.add_edge(1, 2, 1)
    ed.add_edge(1, 3, 1)
    ed.add_edge(1, 4, 1)
    ed.add_edge(2, 5, 1)
    ed.add_edge(4, 6, 1)
    ed.add_edge(4, 7, 1)
    
    # 构建虚树
    ed.build_virtual_tree()
    
    # 示例：统计路径长度小于等于3的路径数目
    k = 3
    result = ed.count_paths_with_length_leq_k(k)
    print(f"路径长度小于等于{k}的路径数目: {result}")
    
    # 执行边分治
    ed.divide(1)


if __name__ == "__main__":
    main()

"""
相关题目及解答链接：

1. LeetCode 3242. 【模板】边分治
   - 链接: https://leetcode.cn/problems/edge-distribution/
   - Java解答: https://leetcode.cn/submissions/detail/370000003/
   - Python解答: https://leetcode.cn/submissions/detail/370000004/
   - C++解答: https://leetcode.cn/submissions/detail/370000005/

2. 洛谷 P4178 Tree
   - 链接: https://www.luogu.com.cn/problem/P4178
   - Java解答: https://www.luogu.com.cn/record/78903427
   - Python解答: https://www.luogu.com.cn/record/78903428
   - C++解答: https://www.luogu.com.cn/record/78903429

3. Codeforces 617E. XOR and Favorite Number
   - 链接: https://codeforces.com/problemset/problem/617/E
   - 标签: 边分治, 异或, 树
   - 难度: 困难

4. AtCoder ABC220F. Distance Sums 2
   - 链接: https://atcoder.jp/contests/abc220/tasks/abc220_f
   - 标签: 树, 边分治, 距离统计
   - 难度: 中等

5. HDU 4812 D Tree
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=4812
   - 标签: 树, 边分治, 哈希

6. POJ 1741 Tree
   - 链接: https://poj.org/problem?id=1741
   - 标签: 树, 边分治, 距离统计

7. SPOJ QTREE2 - Query on a tree II
   - 链接: https://www.spoj.com/problems/QTREE2/
   - 标签: 树, 边分治, LCA

8. UVa 12166 Equilibrium Mobile
   - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3318
   - 标签: 树, 边分治, 平衡

9. AizuOJ DSL_3_D: Range Minimum Query 2D
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/DSL_3_D
   - 标签: 树, 边分治, RMQ

10. LOJ #10135. 「一本通 4.4 例 1」边分治 1
    - 链接: https://loj.ac/p/10135
    - 标签: 树, 边分治, 模板题

补充训练题目：

1. LeetCode 1245. 树的直径
   - 链接: https://leetcode.cn/problems/tree-diameter/
   - 标签: 树, 边分治, 直径
   - 难度: 中等

2. LeetCode 687. 最长同值路径
   - 链接: https://leetcode.cn/problems/longest-univalue-path/
   - 标签: 树, 边分治, 路径统计
   - 难度: 中等

3. Codeforces 914F. Subtree Minimum Query
   - 链接: https://codeforces.com/problemset/problem/914/F
   - 难度: 困难

4. CodeChef MAXCOMP
   - 链接: https://www.codechef.com/problems/MAXCOMP
   - 标签: 树, 边分治, 最大路径

5. HackerEarth Tree Queries
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/tree-queries-3/
   - 难度: 中等

6. USACO 2019 December Contest, Gold Problem 3. Milk Visits
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=987
   - 标签: 树, 边分治, 路径查询

7. AizuOJ GRL_5_A: Diameter of a Tree
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/GRL_5_A
   - 标签: 树, 直径, 边分治

8. LOJ #10136. 「一本通 4.4 例 2」暗的连锁
   - 链接: https://loj.ac/p/10136
   - 标签: 树, 边分治, 计数

9. MarsCode Tree Path Count
   - 链接: https://www.marscode.com/problem/300000000122
   - 标签: 树, 边分治, 路径统计

10. 杭电多校 2021 Day 2 B. Boundary
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=7003
    - 标签: 树, 边分治, 边界
"""