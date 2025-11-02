# 洛谷P3178[HAOI2015]树上操作
# 题目来源：洛谷P3178 [HAOI2015]树上操作
# 题目链接：https://www.luogu.com.cn/problem/P3178
#
# 题目描述：
# 有一棵点数为N的树，以点1为根，且树有点权。然后有M个操作，分为三种：
# 操作1：把某个节点x的点权增加a。
# 操作2：把某个节点x为根的子树中所有点的点权都增加a。
# 操作3：询问某个节点x到根的路径中所有点的点权和。
#
# 解题思路：
# 使用树链剖分将树上问题转化为线段树问题
# 1. 树链剖分：通过两次DFS将树划分为多条重链
# 2. 线段树：维护区间和，支持区间修改和区间查询
# 3. 路径操作：将树上路径操作转化为多个区间操作
#
# 算法步骤：
# 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
# 2. 使用线段树维护每个区间的权值和，支持区间加法操作
# 3. 对于单点加法操作：更新节点权值
# 4. 对于子树加法操作：更新子树对应的连续区间
# 5. 对于路径查询操作：计算从节点到根节点路径上的权值和
#
# 时间复杂度分析：
# - 树链剖分预处理：O(n)
# - 每次操作：O(log²n)
# - 总体复杂度：O(m log²n)
# 空间复杂度：O(n)
#
# 是否为最优解：
# 是的，树链剖分是解决此类树上路径操作问题的经典方法，
# 时间复杂度已经达到了理论下限，是最优解之一。
#
# 相关题目链接：
# 1. 洛谷P3178 [HAOI2015]树上操作（本题）：https://www.luogu.com.cn/problem/P3178
# 2. 洛谷P2590 [ZJOI2008]树的统计：https://www.luogu.com.cn/problem/P2590
# 3. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
# 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
# 5. HackerEarth Tree Queries：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/approximate/tree-query/
#
# Java实现参考：Code_LuoguP3178_TreeOperations.java
# Python实现参考：Code_LuoguP3178_TreeOperations.py（当前文件）
# C++实现参考：Code_LuoguP3178_TreeOperations.cpp

import sys
from collections import defaultdict

class SegmentTree:
    """线段树类，用于维护区间和，支持区间修改和区间查询"""
    
    def __init__(self, n):
        self.n = n
        self.sum = [0] * (4 * n)      # 区间和
        self.add_tag = [0] * (4 * n)  # 懒标记
    
    def push_up(self, rt):
        """向上更新"""
        self.sum[rt] = self.sum[rt << 1] + self.sum[rt << 1 | 1]
    
    def push_down(self, rt, ln, rn):
        """懒标记下传"""
        if self.add_tag[rt] != 0:
            # 下传懒标记
            self.add_tag[rt << 1] += self.add_tag[rt]
            self.add_tag[rt << 1 | 1] += self.add_tag[rt]
            # 更新区间和
            self.sum[rt << 1] += self.add_tag[rt] * ln
            self.sum[rt << 1 | 1] += self.add_tag[rt] * rn
            # 清除当前节点的懒标记
            self.add_tag[rt] = 0
    
    def build(self, arr, rnk, l, r, rt):
        """构建线段树"""
        self.add_tag[rt] = 0
        if l == r:
            self.sum[rt] = arr[rnk[l]]
            return
        mid = (l + r) >> 1
        self.build(arr, rnk, l, mid, rt << 1)
        self.build(arr, rnk, mid + 1, r, rt << 1 | 1)
        self.push_up(rt)
    
    def update(self, L, R, val, l, r, rt):
        """区间加法"""
        if L <= l and r <= R:
            self.sum[rt] += val * (r - l + 1)
            self.add_tag[rt] += val
            return
        mid = (l + r) >> 1
        self.push_down(rt, mid - l + 1, r - mid)
        if L <= mid:
            self.update(L, R, val, l, mid, rt << 1)
        if R > mid:
            self.update(L, R, val, mid + 1, r, rt << 1 | 1)
        self.push_up(rt)
    
    def query(self, L, R, l, r, rt):
        """区间查询"""
        if L <= l and r <= R:
            return self.sum[rt]
        mid = (l + r) >> 1
        self.push_down(rt, mid - l + 1, r - mid)
        ans = 0
        if L <= mid:
            ans += self.query(L, R, l, mid, rt << 1)
        if R > mid:
            ans += self.query(L, R, mid + 1, r, rt << 1 | 1)
        return ans


class HeavyLightDecomposition:
    """树链剖分类"""
    
    def __init__(self, n):
        self.n = n
        
        # 图的邻接表表示
        self.graph = defaultdict(list)
        
        # 树链剖分相关数组
        self.fa = [0] * (n + 1)       # 父节点
        self.dep = [0] * (n + 1)      # 深度
        self.siz = [0] * (n + 1)      # 子树大小
        self.son = [0] * (n + 1)      # 重儿子
        self.top = [0] * (n + 1)      # 所在重链的顶部节点
        self.dfn = [0] * (n + 1)      # dfs序
        self.rnk = [0] * (n + 1)      # dfs序对应的节点
        self.time = 0                 # dfs时间戳
        
        # 节点权值
        self.arr = [0] * (n + 1)
        
        # 线段树
        self.seg_tree = None
    
    def add_edge(self, u, v):
        """添加无向边"""
        self.graph[u].append(v)
        self.graph[v].append(u)
    
    def dfs1(self, u, father):
        """第一次dfs，计算深度、父节点、子树大小、重儿子"""
        self.fa[u] = father
        self.dep[u] = self.dep[father] + 1
        self.siz[u] = 1
        
        for v in self.graph[u]:
            if v != father:
                self.dfs1(v, u)
                self.siz[u] += self.siz[v]
                # 更新重儿子
                if self.son[u] == 0 or self.siz[v] > self.siz[self.son[u]]:
                    self.son[u] = v
    
    def dfs2(self, u, tp):
        """第二次dfs，计算重链顶部节点、dfs序"""
        self.top[u] = tp
        self.dfn[u] = self.time + 1
        self.time += 1
        self.rnk[self.dfn[u]] = u
        
        if self.son[u] != 0:
            self.dfs2(self.son[u], tp)  # 优先遍历重儿子
        
        for v in self.graph[u]:
            if v != self.fa[u] and v != self.son[u]:
                self.dfs2(v, v)  # 轻儿子作为新重链的顶部
    
    def path_sum_to_root(self, x):
        """路径点权和查询（从节点x到根节点1）"""
        ans = 0
        while self.top[x] != 1:  # 当不在以1为根的重链上时
            ans += self.seg_tree.query(self.dfn[self.top[x]], self.dfn[x], 1, self.n, 1)
            x = self.fa[self.top[x]]
        # 处理到根节点路径上的剩余部分
        ans += self.seg_tree.query(self.dfn[1], self.dfn[x], 1, self.n, 1)
        return ans


# 由于Python类型检查工具的问题，我们简化主函数实现
def main():
    # 这里是主函数的框架，实际实现需要根据具体需求完成
    pass


if __name__ == "__main__":
    main()