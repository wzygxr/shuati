"""
Alyona and a tree (Codeforces 739B)
算法：倍增法 + 二分查找 + 树上差分

【算法原理】
1. 对于每个节点v，我们需要找到最远的祖先u，使得dist(u,v) <= a[v]
2. 这意味着所有从u到v路径上的节点都是满足条件的祖先
3. 使用树上差分标记这个区间：diff[v]++, diff[parent(u)]--
4. 最后通过DFS回溯累加差分数组，得到每个节点的答案

【复杂度分析】
- 时间复杂度：O(N log N)
  预处理倍增数组: O(N log N)
  二分查找每个节点: O(N log N)
  DFS回溯统计: O(N)
- 空间复杂度：O(N log N)
  邻接表存储树: O(N)
  倍增数组stjump和stsum: O(N log N)

【工程化考量】
1. 使用邻接表作为高效的树存储结构
2. Python中使用defaultdict简化邻接表的构建
3. 输入优化：使用sys.stdin.read一次性读取所有输入数据
4. 边界处理：根节点(1)的特殊处理

【最优解分析】
此解法是本题的最优解，时间复杂度为O(N log N)
暴力枚举每个节点的所有祖先需要O(N^2)时间，远不如本解法高效
"""

import sys
from collections import defaultdict

class AlyonaTreeSolver:
    """
    Alyona and a tree问题求解器
    使用倍增法、二分查找和树上差分算法解决问题
    """
    
    def __init__(self, n, a_values, edges):
        """
        初始化求解器
        
        Args:
            n: 节点数量
            a_values: 每个节点的权值数组
            edges: 树的边列表，每个元素为(p, v, w)表示父节点、子节点和边权
        """
        self.n = n
        self.a = a_values  # 节点权值数组
        self.LIMIT = 18  # 倍增数组的大小限制，2^18足够处理2e5规模的树
        
        # 构建邻接表表示的树
        self.tree = defaultdict(list)
        for p, v, w in edges:
            self.tree[p].append((v, w))
            self.tree[v].append((p, w))
        
        # 初始化数据结构
        self.deep = [0] * (n + 1)  # 节点深度数组
        self.dist = [0] * (n + 1)  # 节点到根节点的距离数组
        self.stjump = [[0] * self.LIMIT for _ in range(n + 1)]  # 倍增跳跃数组
        self.stsum = [[0] * self.LIMIT for _ in range(n + 1)]  # 倍增距离和数组
        self.diff = [0] * (n + 1)  # 差分数组
        self.dfn = [0] * (n + 1)  # 进入时间戳
        self.dfn2 = [0] * (n + 1)  # 离开时间戳
        self.dfc = 0  # 时间戳计数器
    
    def dfs1(self, u, f, d):
        """
        第一次DFS：预处理深度、距离和倍增数组
        
        Args:
            u: 当前节点
            f: 父节点
            d: 当前节点到根节点的距离
        """
        self.deep[u] = self.deep[f] + 1  # 设置当前节点深度
        self.dist[u] = d  # 设置当前节点到根节点的距离
        self.dfc += 1  # 增加时间戳
        self.dfn[u] = self.dfc  # 设置进入时间戳
        self.stjump[u][0] = f  # 设置直接父节点
        self.stsum[u][0] = d - self.dist[f]  # 设置到父节点的距离
        
        # 预处理倍增数组
        for p in range(1, self.LIMIT):
            self.stjump[u][p] = self.stjump[self.stjump[u][p-1]][p-1]
            self.stsum[u][p] = self.stsum[u][p-1] + self.stsum[self.stjump[u][p-1]][p-1]
        
        # 递归处理所有子节点
        for v, w in self.tree[u]:
            if v != f:  # 避免回到父节点
                self.dfs1(v, u, d + w)
        
        self.dfn2[u] = self.dfc  # 设置离开时间戳
    
    def find_farthest_ancestor(self, v):
        """
        二分查找满足条件的最远祖先
        找到最远的祖先u，使得dist(u,v) <= a[v]
        
        Args:
            v: 当前节点
            
        Returns:
            最远的满足条件的祖先节点编号
        """
        # 计算v节点的最远允许距离边界
        need = self.dist[v] - self.a[v]
        
        # 如果允许距离超过根节点，直接返回根节点
        if need < 0:
            return 1  # 根节点
        
        u = v  # 从v开始向上查找
        
        # 【倍增法向上查找】
        # 从最高幂次开始，尽可能向上跳，但保持祖先的距离 > need
        for p in range(self.LIMIT - 1, -1, -1):
            # 确保祖先存在且其距离仍大于need
            if self.stjump[u][p] > 0 and self.dist[self.stjump[u][p]] > need:
                u = self.stjump[u][p]
        
        # 最终确定最远的满足条件的祖先
        if self.dist[u] > need:
            return self.stjump[u][0]  # 如果当前节点距离仍大于need，再向上跳一级
        else:
            return u  # 当前节点距离已满足条件
    
    def dfs2(self, u, f):
        """
        第二次DFS：回溯计算差分数组的累加结果
        将子节点的差分值累加到父节点，得到每个节点的最终答案
        
        Args:
            u: 当前节点
            f: 父节点
        """
        # 递归处理所有子节点
        for v, w in self.tree[u]:
            if v != f:  # 避免回到父节点
                self.dfs2(v, u)  # 后序遍历，先处理子节点
                self.diff[u] += self.diff[v]  # 累加子节点的差分值
    
    def solve(self):
        """
        解决问题的主函数
        
        Returns:
            每个节点满足条件的祖先数量数组
        """
        # 预处理深度、距离和倍增数组
        self.dfs1(1, 0, 0)  # 根节点为1，父节点为0（无效节点），距离为0
        
        # 【树上差分标记】
        # 对每个节点v，找到满足条件的最远祖先u，并标记区间[u, v]
        for v in range(1, self.n + 1):
            u = self.find_farthest_ancestor(v)  # 找到最远的满足条件的祖先
            
            # 执行区间标记：所有从u到v的祖先都满足条件
            # 这等价于：在v处+1，在u的父节点处-1（如果u不是根节点）
            if u != 1:  # 如果u不是根节点
                self.diff[self.stjump[u][0]] -= 1  # u的父节点减1，结束区间
                self.diff[v] += 1  # v处加1，开始区间
            else:  # 如果u是根节点
                self.diff[v] += 1  # 直接在v处加1，区间从根节点开始
        
        # 计算差分数组的累加结果，得到每个节点的最终答案
        self.dfs2(1, 0)
        
        return self.diff[1:self.n + 1]  # 返回1~n的结果

def main():
    """
    主函数：读取输入数据，调用求解器，输出结果
    """
    # 输入优化：一次性读取所有输入数据
    input_data = sys.stdin.read()
    tokens = input_data.split()
    ptr = 0
    
    # 读取节点数量
    n = int(tokens[ptr])
    ptr += 1
    
    # 读取每个节点的权值
    a = [0] * (n + 1)  # a[0]不使用
    for i in range(1, n + 1):
        a[i] = int(tokens[ptr])
        ptr += 1
    
    # 读取树的边（题目中树是有根的，父节点编号小于子节点）
    edges = []
    for i in range(2, n + 1):  # 节点2~n，每个节点有一个父节点
        p = int(tokens[ptr])  # 父节点
        w = int(tokens[ptr + 1])  # 边权
        edges.append((p, i, w))  # (父节点, 子节点, 边权)
        ptr += 2
    
    # 创建求解器并求解
    solver = AlyonaTreeSolver(n, a, edges)
    result = solver.solve()
    
    # 输出结果，每个节点的满足条件的祖先数量
    print(' '.join(map(str, result)))

if __name__ == "__main__":
    main()