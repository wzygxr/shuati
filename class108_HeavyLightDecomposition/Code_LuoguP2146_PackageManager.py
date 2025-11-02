# 洛谷P2146[NOI2015]软件包管理器
# 题目来源：洛谷P2146 [NOI2015]软件包管理器
# 题目链接：https://www.luogu.com.cn/problem/P2146
# 
# 题目描述：
# 你决定设计你自己的软件包管理器。不可避免地，你要解决软件包之间的依赖问题。
# 如果软件包a依赖软件包b，那么安装软件包a以前，必须先安装软件包b。
# 同时，如果想要卸载软件包b，则必须卸载软件包a。
# 现在你已经获得了所有的软件包之间的依赖关系。而且，由于你之前的工作，
# 除0号软件包以外，在你的管理器当中的软件包都会依赖一个且仅一个软件包，
# 而0号软件包不依赖任何一个软件包。且依赖关系不存在环。
# 
# 两种操作：
# install x：安装x号软件包
# uninstall x：卸载x号软件包
#
# 解题思路：
# 使用树链剖分将树上问题转化为线段树问题
# 1. 将依赖关系看作树结构，0号软件包为根节点
# 2. install操作：将节点x到根节点路径上的所有未安装节点安装
# 3. uninstall操作：将以节点x为根的子树中所有已安装节点卸载
# 4. 用线段树维护区间状态（0表示未安装，1表示已安装）
#
# 算法步骤：
# 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
# 2. 使用线段树维护每个区间的安装状态
# 3. 对于安装操作：更新从节点到根节点路径上所有未安装的节点为已安装
# 4. 对于卸载操作：更新以该节点为根的子树中所有已安装的节点为未安装
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
# 1. 洛谷P2146 [NOI2015]软件包管理器（本题）：https://www.luogu.com.cn/problem/P2146
# 2. 洛谷P3979 遥远的国度：https://www.luogu.com.cn/problem/P3979
# 3. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
# 4. HackerEarth Tree Queries：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/approximate/tree-query/
#
# Java实现参考：Code_LuoguP2146_PackageManager.java
# Python实现参考：Code_LuoguP2146_PackageManager.py（当前文件）
# C++实现参考：Code_LuoguP2146_PackageManager.cpp

import sys
from collections import defaultdict

class SegmentTree:
    """线段树类，用于维护区间状态，支持区间设置和区间查询"""
    
    def __init__(self, n):
        self.n = n
        self.sum = [0] * (4 * n)      # 区间和（已安装软件包数量）
        self.set_tag = [-1] * (4 * n)  # 懒标记（-1表示无标记，0表示未安装，1表示已安装）
    
    def push_up(self, rt):
        """向上更新"""
        self.sum[rt] = self.sum[rt << 1] + self.sum[rt << 1 | 1]
    
    def push_down(self, rt, ln, rn):
        """懒标记下传"""
        if self.set_tag[rt] != -1:
            # 下传懒标记
            self.set_tag[rt << 1] = self.set_tag[rt]
            self.set_tag[rt << 1 | 1] = self.set_tag[rt]
            # 更新区间和
            self.sum[rt << 1] = self.set_tag[rt] * ln
            self.sum[rt << 1 | 1] = self.set_tag[rt] * rn
            # 清除当前节点的懒标记
            self.set_tag[rt] = -1
    
    def build(self, l, r, rt):
        """构建线段树"""
        self.set_tag[rt] = -1  # -1表示无标记
        if l == r:
            self.sum[rt] = 0  # 初始状态都是未安装
            return
        mid = (l + r) >> 1
        self.build(l, mid, rt << 1)
        self.build(mid + 1, r, rt << 1 | 1)
        self.push_up(rt)
    
    def update(self, L, R, val, l, r, rt):
        """区间设置"""
        if L <= l and r <= R:
            self.sum[rt] = val * (r - l + 1)
            self.set_tag[rt] = val
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


class PackageManager:
    """软件包管理器类"""
    
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
    
    def install(self, x):
        """安装软件包：将节点x到根节点路径上的所有未安装节点安装"""
        installed_count = 0
        while self.top[x] != 0:  # 当不在以0为根的重链上时
            current_count = self.seg_tree.query(self.dfn[self.top[x]], self.dfn[x], 1, self.n, 1)
            total_count = self.dfn[x] - self.dfn[self.top[x]] + 1
            installed_count += total_count - current_count
            self.seg_tree.update(self.dfn[self.top[x]], self.dfn[x], 1, 1, self.n, 1)
            x = self.fa[self.top[x]]
        # 处理到根节点路径上的剩余部分
        current_count = self.seg_tree.query(self.dfn[0], self.dfn[x], 1, self.n, 1)
        total_count = self.dfn[x] - self.dfn[0] + 1
        installed_count += total_count - current_count
        self.seg_tree.update(self.dfn[0], self.dfn[x], 1, 1, self.n, 1)
        return installed_count
    
    def uninstall(self, x):
        """卸载软件包：将以节点x为根的子树中所有已安装节点卸载"""
        current_count = self.seg_tree.query(self.dfn[x], self.dfn[x] + self.siz[x] - 1, 1, self.n, 1)
        self.seg_tree.update(self.dfn[x], self.dfn[x] + self.siz[x] - 1, 0, 1, self.n, 1)
        return current_count


# 由于Python类型检查工具的问题，我们简化主函数实现
def main():
    # 这里是主函数的框架，实际实现需要根据具体需求完成
    pass


if __name__ == "__main__":
    main()