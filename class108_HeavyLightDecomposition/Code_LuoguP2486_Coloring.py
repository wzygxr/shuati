# 洛谷P2486[SDOI2011]染色，Python版
# 题目来源：洛谷P2486 [SDOI2011]染色
# 题目链接：https://www.luogu.com.cn/problem/P2486
#
# 题目描述：
# 给定一棵n个节点的无根树，共有m个操作，操作分为两种：
# 1. 将节点a到节点b的路径上的所有点（包括a和b）都染成颜色c。
# 2. 询问节点a到节点b的路径上的颜色段数量。
# 颜色段的定义是极长的连续相同颜色被认为是一段。例如112221由三段组成：11、222、1。
#
# 解题思路：
# 使用树链剖分将树上问题转化为线段树问题
# 1. 树链剖分：通过两次DFS将树划分为多条重链
# 2. 线段树：维护区间颜色段数，需要记录区间左右端点颜色
# 3. 路径操作：将树上路径操作转化为多个区间操作
#
# 算法步骤：
# 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
# 2. 使用线段树维护每个区间的颜色信息：
#    - 区间颜色段数
#    - 区间左端颜色
#    - 区间右端颜色
#    - 懒标记（颜色更新）
# 3. 对于染色操作：更新路径上所有节点的颜色
# 4. 对于查询操作：统计路径上的颜色段数，注意路径连接处颜色相同的合并
#
# 时间复杂度分析：
# - 树链剖分预处理：O(n)
# - 每次操作：O(log²n)
# - 总体复杂度：O(m log²n)
# 空间复杂度：O(n)
#
# 是否为最优解：
# 是的，这是该问题的最优解之一。树链剖分能够将树上路径操作转化为区间操作，
# 再结合线段树的数据结构，可以高效处理大量查询和更新操作。
#
# 相关题目链接：
# 1. 洛谷P2486 [SDOI2011]染色（本题）：https://www.luogu.com.cn/problem/P2486
# 2. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
# 3. 洛谷P2590 [ZJOI2008]树的统计：https://www.luogu.com.cn/problem/P2590
# 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
# 5. HackerEarth Tree Query：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
#
# Java实现参考：Code05_Coloring1.java
# Python实现参考：Code_LuoguP2486_Coloring.py（当前文件）
# C++实现参考：Code05_Coloring1.cpp

import sys
from collections import defaultdict

class SegmentTree:
    """线段树类，用于维护区间颜色段数"""
    
    def __init__(self, n):
        self.n = n
        self.sum = [0] * (4 * n)      # 区间颜色段数
        self.left_color = [0] * (4 * n)  # 区间左端点颜色
        self.right_color = [0] * (4 * n) # 区间右端点颜色
        self.set_color = [-1] * (4 * n)  # 懒标记（-1表示无标记）
    
    def push_up(self, rt):
        """向上更新"""
        # 更新左右端点颜色
        self.left_color[rt] = self.left_color[rt << 1]
        self.right_color[rt] = self.right_color[rt << 1 | 1]
        
        # 更新颜色段数
        self.sum[rt] = self.sum[rt << 1] + self.sum[rt << 1 | 1]
        # 如果左子区间的右端点颜色等于右子区间的左端点颜色，则颜色段数减1
        if self.right_color[rt << 1] == self.left_color[rt << 1 | 1]:
            self.sum[rt] -= 1
    
    def push_down(self, rt, ln, rn):
        """懒标记下传"""
        if self.set_color[rt] != -1:
            # 下传懒标记
            self.set_color[rt << 1] = self.set_color[rt]
            self.set_color[rt << 1 | 1] = self.set_color[rt]
            # 更新左右端点颜色
            self.left_color[rt << 1] = self.set_color[rt]
            self.right_color[rt << 1] = self.set_color[rt]
            self.left_color[rt << 1 | 1] = self.set_color[rt]
            self.right_color[rt << 1 | 1] = self.set_color[rt]
            # 更新颜色段数
            self.sum[rt << 1] = 1
            self.sum[rt << 1 | 1] = 1
            # 清除当前节点的懒标记
            self.set_color[rt] = -1
    
    def build(self, color, rnk, l, r, rt):
        """构建线段树"""
        self.set_color[rt] = -1  # -1表示无标记
        if l == r:
            self.sum[rt] = 1
            self.left_color[rt] = self.right_color[rt] = color[rnk[l]]
            return
        mid = (l + r) >> 1
        self.build(color, rnk, l, mid, rt << 1)
        self.build(color, rnk, mid + 1, r, rt << 1 | 1)
        self.push_up(rt)
    
    def update(self, L, R, val, l, r, rt):
        """区间染色"""
        if L <= l and r <= R:
            self.sum[rt] = 1
            self.left_color[rt] = self.right_color[rt] = val
            self.set_color[rt] = val
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
            return (self.sum[rt], self.left_color[rt], self.right_color[rt])
        mid = (l + r) >> 1
        self.push_down(rt, mid - l + 1, r - mid)
        
        if R <= mid:
            return self.query(L, R, l, mid, rt << 1)
        if L > mid:
            return self.query(L, R, mid + 1, r, rt << 1 | 1)
        
        left_result = self.query(L, R, l, mid, rt << 1)
        right_result = self.query(L, R, mid + 1, r, rt << 1 | 1)
        
        result_sum = left_result[0] + right_result[0]
        if left_result[2] == right_result[1]:
            result_sum -= 1
        result_left = left_result[1]
        result_right = right_result[2]
        return (result_sum, result_left, result_right)


class Coloring:
    """染色类"""
    
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
        
        # 节点颜色
        self.color = [0] * (n + 1)
        
        # 线段树
        self.seg_tree = SegmentTree(n)
    
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
    
    def path_color(self, x, y, c):
        """路径染色"""
        while self.top[x] != self.top[y]:
            if self.dep[self.top[x]] < self.dep[self.top[y]]:
                x, y = y, x  # 交换x,y
            self.seg_tree.update(self.dfn[self.top[x]], self.dfn[x], c, 1, self.n, 1)
            x = self.fa[self.top[x]]
        
        if self.dep[x] > self.dep[y]:
            x, y = y, x  # 交换x,y
        
        self.seg_tree.update(self.dfn[x], self.dfn[y], c, 1, self.n, 1)
    
    def path_color_count(self, x, y):
        """路径颜色段数查询"""
        ans = 0
        last_color = -1  # 上一次查询的右端点颜色
        
        while self.top[x] != self.top[y]:
            if self.dep[self.top[x]] < self.dep[self.top[y]]:
                x, y = y, x  # 交换x,y
            result = self.seg_tree.query(self.dfn[self.top[x]], self.dfn[x], 1, self.n, 1)
            ans += result[0]
            # 如果上一次查询的右端点颜色等于当前查询的左端点颜色，则颜色段数减1
            if last_color == result[2]:
                ans -= 1
            last_color = result[1]  # 更新为当前查询的左端点颜色
            x = self.fa[self.top[x]]
        
        if self.dep[x] > self.dep[y]:
            x, y = y, x  # 交换x,y
        
        result = self.seg_tree.query(self.dfn[x], self.dfn[y], 1, self.n, 1)
        ans += result[0]
        # 如果上一次查询的右端点颜色等于当前查询的左端点颜色，则颜色段数减1
        if last_color == result[2]:
            ans -= 1
        
        return ans


def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    m = int(data[1])
    
    # 读取节点初始颜色
    colors = list(map(int, data[2:2+n]))
    
    # 创建染色对象
    coloring = Coloring(n)
    
    # 设置节点颜色
    for i in range(1, n + 1):
        coloring.color[i] = colors[i - 1]
    
    # 读取边信息
    idx = 2 + n
    for _ in range(n - 1):
        u = int(data[idx])
        v = int(data[idx + 1])
        coloring.add_edge(u, v)
        idx += 2
    
    # 树链剖分
    coloring.dfs1(1, 0)
    coloring.dfs2(1, 1)
    
    # 构建线段树
    coloring.seg_tree.build(coloring.color, coloring.rnk, 1, n, 1)
    
    # 处理操作
    results = []
    for _ in range(m):
        op = data[idx]
        if op == "C":
            a = int(data[idx + 1])
            b = int(data[idx + 2])
            c = int(data[idx + 3])
            coloring.path_color(a, b, c)
            idx += 4
        else:  # op == "Q"
            a = int(data[idx + 1])
            b = int(data[idx + 2])
            result = coloring.path_color_count(a, b)
            results.append(str(result))
            idx += 3
    
    # 输出结果
    for result in results:
        print(result)


if __name__ == "__main__":
    main()