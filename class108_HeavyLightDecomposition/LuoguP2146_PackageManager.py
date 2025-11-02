import sys

# 洛谷P2146[NOI2015]软件包管理器，Python版
# 题目来源：洛谷P2146 [NOI2015]软件包管理器
# 题目链接：https://www.luogu.com.cn/problem/P2146
#
# 题目描述：
# Linux用户和OSX用户都对软件包管理器不会陌生。
# 通过软件包管理器，我们可以安装、删除和更新软件包。
# 软件包之间存在依赖关系，当要安装一个软件包时，需要先安装它的所有依赖。
# 当要卸载一个软件包时，需要同时卸载所有它依赖的软件包。
# 这些依赖关系形成一棵树的结构，其中根节点为空软件包。
# 有两种操作：
# install x：安装软件包x，需要安装从根到x路径上的所有软件包
# uninstall x：卸载软件包x，需要卸载以x为根的子树中的所有软件包
# 每次操作后输出实际安装或卸载的软件包数量
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
# Java实现参考：LuoguP2146_PackageManager.java
# Python实现参考：LuoguP2146_PackageManager.py（当前文件）
# C++实现参考：Code04_PackageManager1.cpp

class SegmentTree:
    """线段树类，用于区间修改和区间查询"""
    
    def __init__(self, n):
        self.n = n
        self.sum = [0] * (4 * n)    # 区间和
        self.lazy = [-1] * (4 * n)  # 懒标记：-1表示无标记，0表示设置为0，1表示设置为1
    
    def up(self, i):
        """向上更新"""
        self.sum[i] = self.sum[i << 1] + self.sum[i << 1 | 1]
    
    def down(self, i, ln, rn):
        """下传懒标记"""
        if self.lazy[i] != -1:
            self.sum[i << 1] = self.lazy[i] * ln
            self.sum[i << 1 | 1] = self.lazy[i] * rn
            self.lazy[i << 1] = self.lazy[i]
            self.lazy[i << 1 | 1] = self.lazy[i]
            self.lazy[i] = -1
    
    def update(self, jobl, jobr, jobv, l, r, i):
        """区间更新"""
        if jobl <= l and r <= jobr:
            self.sum[i] = jobv * (r - l + 1)
            self.lazy[i] = jobv
            return
        mid = (l + r) >> 1
        self.down(i, mid - l + 1, r - mid)
        if jobl <= mid:
            self.update(jobl, jobr, jobv, l, mid, i << 1)
        if jobr > mid:
            self.update(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
        self.up(i)
    
    def query(self, jobl, jobr, l, r, i):
        """区间查询"""
        if jobl <= l and r <= jobr:
            return self.sum[i]
        mid = (l + r) >> 1
        self.down(i, mid - l + 1, r - mid)
        ans = 0
        if jobl <= mid:
            ans += self.query(jobl, jobr, l, mid, i << 1)
        if jobr > mid:
            ans += self.query(jobl, jobr, mid + 1, r, i << 1 | 1)
        return ans


class HLD_PackageManager:
    """树链剖分软件包管理器"""
    
    def __init__(self, n):
        self.n = n
        
        # 图的邻接表表示
        self.head = [0] * (n + 1)
        self.next_edge = [0] * (2 * n + 1)
        self.to_edge = [0] * (2 * n + 1)
        self.cnt_edge = 0
        
        # 树链剖分相关数组
        self.fa = [0] * (n + 1)       # 父节点
        self.dep = [0] * (n + 1)      # 深度
        self.siz = [0] * (n + 1)      # 子树大小
        self.son = [0] * (n + 1)      # 重儿子
        self.top = [0] * (n + 1)      # 所在重链的顶部节点
        self.dfn = [0] * (n + 1)      # dfs序
        self.rnk = [0] * (n + 1)      # dfs序对应的节点
        self.cnt_dfn = 0              # dfs序计数器
        
        # 线段树
        self.seg_tree = SegmentTree(n)
    
    def add_edge(self, u, v):
        """添加无向边"""
        self.cnt_edge += 1
        self.next_edge[self.cnt_edge] = self.head[u]
        self.to_edge[self.cnt_edge] = v
        self.head[u] = self.cnt_edge
    
    def dfs1(self, u, f):
        """第一次dfs，计算树链剖分所需信息"""
        self.fa[u] = f
        self.dep[u] = self.dep[f] + 1
        self.siz[u] = 1
        self.son[u] = 0
        
        e = self.head[u]
        while e:
            v = self.to_edge[e]
            if v != f:
                self.dfs1(v, u)
                self.siz[u] += self.siz[v]
                if self.son[u] == 0 or self.siz[self.son[u]] < self.siz[v]:
                    self.son[u] = v
            e = self.next_edge[e]
    
    def dfs2(self, u, t):
        """第二次dfs，计算重链剖分"""
        self.top[u] = t
        self.cnt_dfn += 1
        self.dfn[u] = self.cnt_dfn
        self.rnk[self.cnt_dfn] = u
        
        if self.son[u] == 0:
            return
        self.dfs2(self.son[u], t)
        
        e = self.head[u]
        while e:
            v = self.to_edge[e]
            if v != self.fa[u] and v != self.son[u]:
                self.dfs2(v, v)
            e = self.next_edge[e]
    
    def install(self, x):
        """安装软件包：安装从根节点到x的路径上所有软件包"""
        # 先查询安装前从根到x路径上的安装情况
        installed_before = 0
        temp = x
        while temp != 0:
            installed_before += self.seg_tree.query(self.dfn[self.top[temp]], self.dfn[temp], 1, self.n, 1)
            temp = self.fa[self.top[temp]]
        
        # 安装从根到x路径上的所有软件包
        temp = x
        while temp != 0:
            self.seg_tree.update(self.dfn[self.top[temp]], self.dfn[temp], 1, 1, self.n, 1)
            temp = self.fa[self.top[temp]]
        
        # 查询安装后从根到x路径上的安装情况
        installed_after = 0
        temp = x
        while temp != 0:
            installed_after += self.seg_tree.query(self.dfn[self.top[temp]], self.dfn[temp], 1, self.n, 1)
            temp = self.fa[self.top[temp]]
        
        return installed_after - installed_before
    
    def uninstall(self, x):
        """卸载软件包：卸载以x为根的子树中的所有软件包"""
        # 先查询卸载前x子树中已安装的软件包数量
        installed_before = self.seg_tree.query(self.dfn[x], self.dfn[x] + self.siz[x] - 1, 1, self.n, 1)
        
        # 卸载x子树中的所有软件包（设置为0）
        self.seg_tree.update(self.dfn[x], self.dfn[x] + self.siz[x] - 1, 0, 1, self.n, 1)
        
        return installed_before  # 返回卸载的软件包数量


def main():
    n = int(sys.stdin.readline())
    
    # 创建HLD PackageManager对象
    pm = HLD_PackageManager(n)
    
    # 构建树结构（0是根节点，表示空软件包）
    for i in range(1, n):
        parent = int(sys.stdin.readline())
        pm.add_edge(parent, i)
        pm.add_edge(i, parent)
    
    # 树链剖分
    pm.dfs1(0, 0)
    pm.dfs2(0, 0)
    
    q = int(sys.stdin.readline())
    for _ in range(q):
        line = sys.stdin.readline().split()
        operation = line[0]
        x = int(line[1])
        
        if operation == "install":
            print(pm.install(x))
        else:  # uninstall
            print(pm.uninstall(x))


if __name__ == "__main__":
    main()