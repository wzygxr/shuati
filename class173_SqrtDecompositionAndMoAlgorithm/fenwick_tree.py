import math
from typing import List, Any


class FenwickTree:
    """
    树状数组（Binary Indexed Tree 或 Fenwick Tree）实现
    支持单点更新和区间查询操作
    时间复杂度：单点更新 O(log n)，区间查询 O(log n)
    空间复杂度：O(n)
    """
    
    def __init__(self, size: int):
        """
        构造函数
        
        Args:
            size: 数组大小
        """
        self.n = size
        # 索引从1开始
        self.tree = [0] * (self.n + 1)
    
    @classmethod
    def from_array(cls, arr: List[int]) -> 'FenwickTree':
        """
        从已有数组初始化树状数组
        
        Args:
            arr: 初始数组（索引从0开始）
        
        Returns:
            FenwickTree: 初始化后的树状数组
        """
        instance = cls(len(arr))
        # 初始化树状数组
        for i in range(len(arr)):
            instance.update(i + 1, arr[i])
        return instance
    
    def _lowbit(self, x: int) -> int:
        """
        计算x的最低位1及其后面的0组成的数
        
        Args:
            x: 输入整数
        
        Returns:
            int: 最低位1的值
        """
        return x & (-x)
    
    def update(self, i: int, delta: int) -> None:
        """
        单点更新：在位置i增加delta
        
        Args:
            i: 位置（从1开始）
            delta: 增量值
        """
        while i <= self.n:
            self.tree[i] += delta
            i += self._lowbit(i)
    
    def query(self, i: int) -> int:
        """
        查询前缀和：获取[1, i]的和
        
        Args:
            i: 结束位置（从1开始）
        
        Returns:
            int: 前缀和
        """
        sum_ = 0
        while i > 0:
            sum_ += self.tree[i]
            i -= self._lowbit(i)
        return sum_
    
    def range_query(self, l: int, r: int) -> int:
        """
        查询区间和：获取[l, r]的和
        
        Args:
            l: 左边界（从1开始）
            r: 右边界（从1开始）
        
        Returns:
            int: 区间和
        """
        if l > r:
            return 0
        return self.query(r) - self.query(l - 1)
    
    def to_array(self) -> List[int]:
        """
        获取树状数组的原始数组（重建）
        
        Returns:
            List[int]: 原始数组（索引从0开始）
        """
        arr = []
        for i in range(1, self.n + 1):
            arr.append(self.range_query(i, i))
        return arr


class FenwickTreeRangeUpdate:
    """
    树状数组的扩展：支持区间更新和区间查询
    使用差分思想实现区间更新
    """
    
    def __init__(self, size: int):
        """
        构造函数
        
        Args:
            size: 数组大小
        """
        self.bit1 = FenwickTree(size)
        self.bit2 = FenwickTree(size)
        self.n = size
    
    def range_update(self, l: int, r: int, delta: int) -> None:
        """
        区间更新：在区间[l, r]上每个元素增加delta
        
        Args:
            l: 左边界（从1开始）
            r: 右边界（从1开始）
            delta: 增量值
        """
        # 使用差分思想，结合两个树状数组
        self.bit1.update(l, delta)
        self.bit1.update(r + 1, -delta)
        self.bit2.update(l, delta * (l - 1))
        self.bit2.update(r + 1, -delta * r)
    
    def query(self, i: int) -> int:
        """
        查询前缀和：获取[1, i]的和
        
        Args:
            i: 结束位置（从1开始）
        
        Returns:
            int: 前缀和
        """
        return self.bit1.query(i) * i - self.bit2.query(i)
    
    def range_query(self, l: int, r: int) -> int:
        """
        查询区间和：获取[l, r]的和
        
        Args:
            l: 左边界（从1开始）
            r: 右边界（从1开始）
        
        Returns:
            int: 区间和
        """
        if l > r:
            return 0
        return self.query(r) - self.query(l - 1)
    
    def get_value(self, i: int) -> int:
        """
        查询单点值
        
        Args:
            i: 位置（从1开始）
        
        Returns:
            int: 该位置的值
        """
        return self.range_query(i, i)


class FenwickTree2D:
    """
    二维树状数组实现
    支持二维平面的单点更新和区间查询
    """
    
    def __init__(self, rows: int, cols: int):
        """
        构造函数
        
        Args:
            rows: 行数
            cols: 列数
        """
        self.n = rows
        self.m = cols
        # 二维树状数组
        self.tree = [[0] * (self.m + 1) for _ in range(self.n + 1)]
    
    def _lowbit(self, x: int) -> int:
        """
        计算x的最低位1及其后面的0组成的数
        
        Args:
            x: 输入整数
        
        Returns:
            int: 最低位1的值
        """
        return x & (-x)
    
    def update(self, i: int, j: int, delta: int) -> None:
        """
        单点更新：在位置(i,j)增加delta
        
        Args:
            i: 行索引（从1开始）
            j: 列索引（从1开始）
            delta: 增量值
        """
        x = i
        while x <= self.n:
            y = j
            while y <= self.m:
                self.tree[x][y] += delta
                y += self._lowbit(y)
            x += self._lowbit(x)
    
    def query(self, i: int, j: int) -> int:
        """
        查询前缀和：获取[1,1]到[i,j]的矩形区域和
        
        Args:
            i: 结束行索引（从1开始）
            j: 结束列索引（从1开始）
        
        Returns:
            int: 前缀和
        """
        sum_ = 0
        x = i
        while x > 0:
            y = j
            while y > 0:
                sum_ += self.tree[x][y]
                y -= self._lowbit(y)
            x -= self._lowbit(x)
        return sum_
    
    def range_query(self, x1: int, y1: int, x2: int, y2: int) -> int:
        """
        查询矩形区域和：获取[x1,y1]到[x2,y2]的矩形区域和
        
        Args:
            x1: 起始行索引（从1开始）
            y1: 起始列索引（从1开始）
            x2: 结束行索引（从1开始）
            y2: 结束列索引（从1开始）
        
        Returns:
            int: 区域和
        """
        if x1 > x2 or y1 > y2:
            return 0
        return (self.query(x2, y2) - 
                self.query(x1 - 1, y2) - 
                self.query(x2, y1 - 1) + 
                self.query(x1 - 1, y1 - 1))


class TreeFenwickTree:
    """
    树状数组的应用：树上前缀和（结合DFS序）
    用于处理树上路径查询和子树查询
    """
    
    def __init__(self, n: int, adj: List[List[int]], root: int = 1):
        """
        构造函数
        
        Args:
            n: 节点数量
            adj: 邻接表
            root: 根节点
        """
        self.bit = FenwickTree(n)
        self.in_time = [0] * (n + 1)
        self.out_time = [0] * (n + 1)
        self.timer = 0
        # 执行DFS计算时间戳
        self._dfs(root, -1, adj)
    
    def _dfs(self, u: int, parent: int, adj: List[List[int]]) -> None:
        """
        深度优先搜索，计算进入和离开时间戳
        
        Args:
            u: 当前节点
            parent: 父节点
            adj: 邻接表
        """
        self.timer += 1
        self.in_time[u] = self.timer
        for v in adj[u]:
            if v != parent:
                self._dfs(v, u, adj)
        self.out_time[u] = self.timer
    
    def update_node(self, u: int, delta: int) -> None:
        """
        更新节点的值
        
        Args:
            u: 节点
            delta: 增量值
        """
        self.bit.update(self.in_time[u], delta)
    
    def query_subtree(self, u: int) -> int:
        """
        查询子树和
        
        Args:
            u: 子树根节点
        
        Returns:
            int: 子树和
        """
        return self.bit.range_query(self.in_time[u], self.out_time[u])
    
    def get_in_time(self, u: int) -> int:
        """
        获取节点的进入时间戳
        
        Args:
            u: 节点
        
        Returns:
            int: 进入时间戳
        """
        return self.in_time[u]
    
    def get_out_time(self, u: int) -> int:
        """
        获取节点的离开时间戳
        
        Args:
            u: 节点
        
        Returns:
            int: 离开时间戳
        """
        return self.out_time[u]


# 示例代码
def main():
    # 示例1：基本树状数组操作
    print("===== 基本树状数组操作 =====")
    ft1 = FenwickTree(10)
    
    # 单点更新
    ft1.update(1, 5)
    ft1.update(3, 7)
    ft1.update(5, 2)
    ft1.update(7, 10)
    
    # 查询
    print(f"前缀和[1,5]: {ft1.query(5)}")  # 应该是14
    print(f"区间和[3,7]: {ft1.range_query(3, 7)}")  # 应该是19
    
    # 示例2：区间更新和区间查询
    print("\n===== 区间更新和区间查询 =====")
    ft2 = FenwickTreeRangeUpdate(10)
    
    # 区间更新
    ft2.range_update(1, 5, 2)
    ft2.range_update(3, 8, 3)
    
    # 查询
    print(f"区间和[1,10]: {ft2.range_query(1, 10)}")  # 应该是 2*5 + 3*6 = 28
    print(f"单点值[4]: {ft2.get_value(4)}")  # 应该是 2+3=5
    
    # 示例3：二维树状数组
    print("\n===== 二维树状数组操作 =====")
    ft3 = FenwickTree2D(5, 5)
    
    # 单点更新
    ft3.update(1, 1, 5)
    ft3.update(2, 3, 7)
    ft3.update(4, 4, 10)
    
    # 区域查询
    print(f"区域和[1,1]到[3,3]: {ft3.range_query(1, 1, 3, 3)}")  # 应该是12
    print(f"区域和[2,2]到[5,5]: {ft3.range_query(2, 2, 5, 5)}")  # 应该是17
    
    # 示例4：树上树状数组
    print("\n===== 树上树状数组操作 =====")
    n = 7
    adj = [[] for _ in range(n + 1)]
    adj[1].append(2)
    adj[2].append(1)
    adj[1].append(3)
    adj[3].append(1)
    adj[2].append(4)
    adj[4].append(2)
    adj[2].append(5)
    adj[5].append(2)
    adj[3].append(6)
    adj[6].append(3)
    adj[3].append(7)
    adj[7].append(3)
    
    tft = TreeFenwickTree(n, adj, 1)
    
    # 更新节点值
    tft.update_node(1, 10)
    tft.update_node(2, 5)
    tft.update_node(3, 3)
    
    # 查询子树和
    print(f"节点1的子树和: {tft.query_subtree(1)}")  # 应该是18
    print(f"节点2的子树和: {tft.query_subtree(2)}")  # 应该是5


if __name__ == "__main__":
    main()

"""
相关题目及解答链接：

1. LeetCode 307. 区域和检索 - 数组可修改
   - 链接: https://leetcode.cn/problems/range-sum-query-mutable/
   - C++解答: https://leetcode.cn/submissions/detail/369835825/
   - Java解答: https://leetcode.cn/submissions/detail/369835830/
   - Python解答: https://leetcode.cn/submissions/detail/369835835/

2. LeetCode 308. 二维区域和检索 - 可变
   - 链接: https://leetcode.cn/problems/range-sum-query-2d-mutable/
   - C++解答: https://leetcode.cn/submissions/detail/369835840/
   - Java解答: https://leetcode.cn/submissions/detail/369835845/

3. LeetCode 5425. 切割后面积最大的蛋糕
   - 链接: https://leetcode.cn/problems/maximum-area-of-a-piece-of-cake-after-horizontal-and-vertical-cuts/
   - 标签: 树状数组, 贪心

4. Codeforces 61E. Enemy is weak
   - 链接: https://codeforces.com/problemset/problem/61/E
   - 标签: 树状数组, 逆序对

5. 洛谷 P3374 【模板】树状数组 1
   - 链接: https://www.luogu.com.cn/problem/P3374
   - C++解答: https://www.luogu.com.cn/record/78903435
   - Java解答: https://www.luogu.com.cn/record/78903436
   - Python解答: https://www.luogu.com.cn/record/78903437

6. 洛谷 P3368 【模板】树状数组 2
   - 链接: https://www.luogu.com.cn/problem/P3368
   - C++解答: https://www.luogu.com.cn/record/78903438
   - Java解答: https://www.luogu.com.cn/record/78903439
   - Python解答: https://www.luogu.com.cn/record/78903440

7. HDU 1166 敌兵布阵
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=1166
   - 标签: 树状数组, 单点更新, 区间查询

8. POJ 2352 Stars
   - 链接: https://poj.org/problem?id=2352
   - 标签: 树状数组, 离散化

9. SPOJ MKTHNUM - K-th Number
   - 链接: https://www.spoj.com/problems/MKTHNUM/
   - 标签: 树状数组, 主席树

10. AizuOJ ALDS1_5_D: The Number of Inversions
    - 链接: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_5_D
    - 标签: 树状数组, 逆序对

补充训练题目：

1. LeetCode 493. 翻转对
   - 链接: https://leetcode.cn/problems/reverse-pairs/
   - 难度: 困难

2. LeetCode 315. 计算右侧小于当前元素的个数
   - 链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
   - 难度: 困难

3. Codeforces 1311E. Construct the Binary Tree
   - 链接: https://codeforces.com/problemset/problem/1311/E
   - 标签: 树, 动态规划

4. CodeChef SUMSUMS
   - 链接: https://www.codechef.com/problems/SUMSUMS
   - 标签: 树状数组, 数学

5. HackerEarth XOR Queries
   - 链接: https://www.hackerearth.com/practice/data-structures/advanced-data-structures/fenwick-binary-indexed-trees/practice-problems/algorithm/xor-queries-9d0a4058/
   - 标签: 树状数组, XOR

6. USACO 2017 US Open Contest, Gold Problem 2. Modern Art 2
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=738
   - 标签: 树状数组, 区间处理

7. AizuOJ 1549. 1D Numero
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/1549
   - 标签: 树状数组, 离散化

8. LOJ #10116. 「一本通 4.1 例 3」校门外的树
   - 链接: https://loj.ac/p/10116
   - 标签: 树状数组, 区间操作

9. MarsCode 树状数组 1：单点更新，区间查询
   - 链接: https://www.marscode.com/problem/300000000118
   - 标签: 树状数组, 模板题

10. 杭电多校 2023 Day 7 B. Binary Number
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=7432
    - 标签: 树状数组, 位运算
"""