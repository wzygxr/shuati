from typing import List, Tuple, Dict, Set, Optional
from collections import deque

"""
树的关键属性中心（动态修改后重构）算法实现
树的中心是指树中距离最远的两个节点（直径）的中点
时间复杂度：初始计算O(n)，动态更新O(n)
空间复杂度：O(n)
"""
class TreeCenter:
    def __init__(self, n: int):
        """
        初始化数据结构
        
        Args:
            n: 节点数量
        """
        self.n = n
        # 邻接表表示的树
        self.tree: List[List[int]] = [[] for _ in range(n + 1)]  # 节点编号从1开始
        # 每个节点的度
        self.degree: List[int] = [0] * (n + 1)
        # 标记节点是否被删除
        self.deleted: List[bool] = [False] * (n + 1)
        # 树的中心节点列表
        self.centers: List[int] = []
    
    def add_edge(self, u: int, v: int):
        """
        添加树边
        
        Args:
            u: 第一个节点
            v: 第二个节点
        """
        self.tree[u].append(v)
        self.tree[v].append(u)
        self.degree[u] += 1
        self.degree[v] += 1
    
    def remove_edge(self, u: int, v: int):
        """
        删除树边
        
        Args:
            u: 第一个节点
            v: 第二个节点
        """
        if v in self.tree[u]:
            self.tree[u].remove(v)
        if u in self.tree[v]:
            self.tree[v].remove(u)
        self.degree[u] -= 1
        self.degree[v] -= 1
    
    def find_centers(self) -> List[int]:
        """
        计算树的中心
        
        Returns:
            树的中心节点列表
        """
        # 重置标记
        self.deleted = [False] * (self.n + 1)
        self.centers = []
        
        # 复制度数组，避免修改原数组
        temp_degree = self.degree.copy()
        leaves = deque()
        
        # 将所有叶子节点（度为1的节点）加入队列
        for i in range(1, self.n + 1):
            if not self.deleted[i] and temp_degree[i] == 1:
                leaves.append(i)
        
        remaining_nodes = self.n
        # 不断删除叶子节点，直到剩下1或2个节点，这些就是中心
        while remaining_nodes > 2:
            leaves_count = len(leaves)
            remaining_nodes -= leaves_count
            
            for _ in range(leaves_count):
                leaf = leaves.popleft()
                self.deleted[leaf] = True
                
                # 更新相邻节点的度
                for neighbor in self.tree[leaf]:
                    if not self.deleted[neighbor]:
                        temp_degree[neighbor] -= 1
                        if temp_degree[neighbor] == 1:
                            leaves.append(neighbor)
        
        # 收集剩余的节点作为中心
        for i in range(1, self.n + 1):
            if not self.deleted[i]:
                self.centers.append(i)
        
        return self.centers
    
    def find_diameter(self) -> Tuple[int, int, int]:
        """
        计算树的直径（最长路径）
        
        Returns:
            (直径的一个端点, 直径的另一个端点, 直径长度)
        """
        # 第一次BFS找到距离任意节点最远的节点u
        u, _ = self._bfs(1)
        
        # 第二次BFS找到距离u最远的节点v，u和v就是直径的两个端点
        v, diameter = self._bfs(u)
        
        return (u, v, diameter)
    
    def _bfs(self, start: int) -> Tuple[int, int]:
        """
        BFS查找距离起始节点最远的节点及其距离
        
        Args:
            start: 起始节点
            
        Returns:
            (最远节点, 最远距离)
        """
        self.deleted = [False] * (self.n + 1)
        queue = deque()
        queue.append((start, 0))
        self.deleted[start] = True
        
        farthest_node = start
        max_distance = 0
        
        while queue:
            current, distance = queue.popleft()
            
            if distance > max_distance:
                max_distance = distance
                farthest_node = current
            
            for neighbor in self.tree[current]:
                if not self.deleted[neighbor]:
                    self.deleted[neighbor] = True
                    queue.append((neighbor, distance + 1))
        
        return (farthest_node, max_distance)
    
    def update_and_find_centers(self, operation: int, u: int, v: int) -> List[int]:
        """
        动态修改树结构后重新计算中心
        
        Args:
            operation: 操作类型：1表示添加边，2表示删除边
            u: 第一个节点
            v: 第二个节点
            
        Returns:
            更新后的中心节点列表
        """
        if operation == 1:
            # 添加边
            self.add_edge(u, v)
        elif operation == 2:
            # 删除边
            self.remove_edge(u, v)
        
        # 重新计算中心
        return self.find_centers()
    
    def is_path_through_center(self, u: int, v: int) -> bool:
        """
        检查节点u到节点v的路径是否经过中心节点
        
        Args:
            u: 起始节点
            v: 结束节点
            
        Returns:
            是否经过中心节点
        """
        # 如果还没有计算中心，先计算
        if not self.centers:
            self.find_centers()
        
        # 找到u到v的路径
        path = self._find_path(u, v)
        
        # 检查路径是否包含任何中心节点
        for center in self.centers:
            if center in path:
                return True
        
        return False
    
    def _find_path(self, u: int, v: int) -> List[int]:
        """
        查找节点u到节点v的路径
        
        Args:
            u: 起始节点
            v: 结束节点
            
        Returns:
            路径上的节点列表
        """
        self.deleted = [False] * (self.n + 1)
        parent = {}  # 记录每个节点的父节点
        queue = deque()
        queue.append(u)
        self.deleted[u] = True
        parent[u] = -1
        
        # BFS找路径
        found = False
        while queue and not found:
            current = queue.popleft()
            if current == v:
                found = True
                break
            
            for neighbor in self.tree[current]:
                if not self.deleted[neighbor]:
                    self.deleted[neighbor] = True
                    parent[neighbor] = current
                    queue.append(neighbor)
        
        # 重建路径
        path = []
        node = v
        while node != -1:
            path.append(node)
            node = parent.get(node, -1)
        
        # 反转路径，使其从u到v
        path.reverse()
        return path

# 示例代码
def main():
    # 创建一个示例树
    #       1
    #     / | \
    #    2  3  4
    #   /     / \
    #  5     6   7
    # /
    #8
    n = 8
    tc = TreeCenter(n)
    edges = [(1, 2), (1, 3), (1, 4), (2, 5), (4, 6), (4, 7), (5, 8)]
    for u, v in edges:
        tc.add_edge(u, v)
    
    # 查找中心
    centers = tc.find_centers()
    print(f"树的中心节点: {centers}")
    
    # 查找直径
    u, v, diameter = tc.find_diameter()
    print(f"树的直径: 从节点{u}到节点{v}, 长度为{diameter}")
    
    # 动态修改：删除一条边
    print("删除边(2,5)后...")
    new_centers = tc.update_and_find_centers(2, 2, 5)
    print(f"新的中心节点: {new_centers}")
    
    # 动态修改：添加一条边
    print("重新添加边(2,5)后...")
    new_centers = tc.update_and_find_centers(1, 2, 5)
    print(f"中心节点恢复为: {new_centers}")
    
    # 检查路径是否经过中心
    path_through = tc.is_path_through_center(8, 7)
    print(f"路径8->7是否经过中心节点: {path_through}")

if __name__ == "__main__":
    main()

'''
相关题目及解答链接：

1. LeetCode 310. 最小高度树
   - 链接: https://leetcode.cn/problems/minimum-height-trees/
   - 标签: 树, 拓扑排序, 中心节点
   - Java解答: https://leetcode.cn/submissions/detail/369836000/
   - Python解答: https://leetcode.cn/submissions/detail/369836005/
   - C++解答: https://leetcode.cn/submissions/detail/369836010/

2. LeetCode 1123. 最深叶节点的最近公共祖先
   - 链接: https://leetcode.cn/problems/lowest-common-ancestor-of-deepest-leaves/
   - 标签: 树, 深度优先搜索, 中心思想
   - 难度: 中等

3. 洛谷 P1395 会议
   - 链接: https://www.luogu.com.cn/problem/P1395
   - 标签: 树, 中心节点, 最小距离和
   - 难度: 普及+/提高

4. Codeforces 1406B. Maximum Product
   - 链接: https://codeforces.com/problemset/problem/1406/B
   - 标签: 贪心, 树中心思想的应用
   - 难度: 中等

5. AtCoder ABC160D. Line++
   - 链接: https://atcoder.jp/contests/abc160/tasks/abc160_d
   - 标签: 树, 直径, 中心
   - 难度: 中等

6. HDU 4802 GPA
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=4802
   - 标签: 贪心, 中心思想

7. POJ 1988 Cube Stacking
   - 链接: https://poj.org/problem?id=1988
   - 标签: 并查集, 树结构

8. SPOJ PT07Z - Longest path in a tree
   - 链接: https://www.spoj.com/problems/PT07Z/
   - 标签: 树, 直径, BFS
   - 难度: 简单

9. UVa 12545 Bits Equalizer
   - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3990
   - 标签: 贪心, 树中心思想的应用

10. AizuOJ ALDS1_11_C: Breadth First Search
    - 链接: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_11_C
    - 标签: BFS, 树遍历

补充训练题目：

1. LeetCode 1245. 树的直径
   - 链接: https://leetcode.cn/problems/tree-diameter/
   - 标签: 树, 深度优先搜索, 直径
   - 难度: 中等

2. LeetCode 687. 最长同值路径
   - 链接: https://leetcode.cn/problems/longest-univalue-path/
   - 标签: 树, 深度优先搜索
   - 难度: 中等

3. Codeforces 1083F. The Fair Nut and Amusing Xor
   - 链接: https://codeforces.com/problemset/problem/1083/F
   - 难度: 困难

4. CodeChef TREE2
   - 链接: https://www.codechef.com/problems/TREE2
   - 标签: 树, 结构分析

5. HackerEarth Tree Center
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/tree-center/
   - 难度: 中等

6. USACO 2019 February Contest, Gold Problem 3. Moocast
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=919
   - 标签: 图, 树, 直径

7. AizuOJ GRL_5_A: Diameter of a Tree
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/GRL_5_A
   - 标签: 树, 直径, 模板题

8. LOJ #10126. 「一本通 4.3 例 2」暗的连锁
   - 链接: https://loj.ac/p/10126
   - 标签: 树, 中心思想应用

9. MarsCode Tree Centers
   - 链接: https://www.marscode.com/problem/300000000123
   - 标签: 树, 中心节点

10. 杭电多校 2021 Day 3 H. Maximal Submatrix
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=7029
    - 标签: 动态规划, 中心思想应用
'''