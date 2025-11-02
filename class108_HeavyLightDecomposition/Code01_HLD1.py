import sys

# 重链剖分模版题，Python版
# 题目来源：洛谷P3384 【模板】重链剖分/树链剖分
# 题目链接：https://www.luogu.com.cn/problem/P3384
#
# 题目描述：
# 如题，已知一棵包含N个结点的树（连通且无环），每个节点上包含一个数值，需要支持以下操作：
# 操作 1 x y z : x到y的路径上，每个节点值增加z
# 操作 2 x y   : x到y的路径上，打印所有节点值的累加和
# 操作 3 x z   : x为头的子树上，每个节点值增加z
# 操作 4 x     : x为头的子树上，打印所有节点值的累加和
# 1 <= n、m <= 10^5
# 1 <= MOD <= 2^30
# 输入的值都为int类型
# 查询操作时，打印(查询结果 % MOD)，题目会给定MOD值
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
# 3. 对于路径加法操作：将路径分解为多段重链进行区间更新
# 4. 对于子树加法操作：直接对子树对应的连续区间进行更新
# 5. 对于路径查询操作：将路径分解为多段重链进行区间查询
# 6. 对于子树查询操作：直接对子树对应的连续区间进行查询
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
# 1. 洛谷P3384 【模板】重链剖分/树链剖分（本题）：https://www.luogu.com.cn/problem/P3384
# 2. 洛谷P2590 [ZJOI2008]树的统计：https://www.luogu.com.cn/problem/P2590
# 3. 洛谷P3178 [HAOI2015]树上操作：https://www.luogu.com.cn/problem/P3178
# 4. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
# 5. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
#
# Java实现参考：Code01_HLD1.java
# Python实现参考：Code01_HLD1.py（当前文件）

class SegmentTree:
    """线段树类，用于区间修改和区间查询"""
    
    def __init__(self, n):
        self.n = n
        self.sum = [0] * (4 * n)      # 区间和
        self.add_tag = [0] * (4 * n)  # 懒标记
        self.MOD = 0  # 取模数
    
    def up(self, i):
        """向上更新"""
        self.sum[i] = (self.sum[i << 1] + self.sum[i << 1 | 1]) % self.MOD
    
    def lazy(self, i, v, n):
        """懒标记下传"""
        self.sum[i] = (self.sum[i] + v * n) % self.MOD
        self.add_tag[i] = (self.add_tag[i] + v) % self.MOD
    
    def down(self, i, ln, rn):
        """下传懒标记"""
        if self.add_tag[i] != 0:
            self.lazy(i << 1, self.add_tag[i], ln)
            self.lazy(i << 1 | 1, self.add_tag[i], rn)
            self.add_tag[i] = 0
    
    def build(self, arr, seg, l, r, i):
        """构建线段树"""
        if l == r:
            self.sum[i] = arr[seg[l]] % self.MOD
            return
        mid = (l + r) >> 1
        self.build(arr, seg, l, mid, i << 1)
        self.build(arr, seg, mid + 1, r, i << 1 | 1)
        self.up(i)
    
    def add(self, jobl, jobr, jobv, l, r, i):
        """区间加法"""
        if jobl <= l and r <= jobr:
            self.lazy(i, jobv, r - l + 1)
            return
        mid = (l + r) >> 1
        self.down(i, mid - l + 1, r - mid)
        if jobl <= mid:
            self.add(jobl, jobr, jobv, l, mid, i << 1)
        if jobr > mid:
            self.add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
        self.up(i)
    
    def query(self, jobl, jobr, l, r, i):
        """区间查询"""
        if jobl <= l and r <= jobr:
            return self.sum[i]
        mid = (l + r) >> 1
        self.down(i, mid - l + 1, r - mid)
        ans = 0
        if jobl <= mid:
            ans = (ans + self.query(jobl, jobr, l, mid, i << 1)) % self.MOD
        if jobr > mid:
            ans = (ans + self.query(jobl, jobr, mid + 1, r, i << 1 | 1)) % self.MOD
        return ans


class HLD:
    """树链剖分类"""
    
    def __init__(self, n, root, MOD):
        self.n = n
        self.root = root
        self.MOD = MOD
        
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
        self.seg = [0] * (n + 1)      # dfs序对应的节点
        self.cnt_dfn = 0              # dfs序计数器
        
        # 用于迭代实现的栈
        self.stack = []
        
        # 线段树
        self.seg_tree = SegmentTree(n)
        self.seg_tree.MOD = MOD
    
    def add_edge(self, u, v):
        """添加无向边"""
        self.cnt_edge += 1
        self.next_edge[self.cnt_edge] = self.head[u]
        self.to_edge[self.cnt_edge] = v
        self.head[u] = self.cnt_edge
    
    def dfs1_iterative(self):
        """第一次dfs的迭代实现，计算fa, dep, siz, son"""
        # 栈中存储 (当前节点, 父节点, 边索引)
        # 边索引为-1表示第一次访问该节点
        self.stack = [(self.root, 0, -1)]
        
        while self.stack:
            u, f, edge = self.stack.pop()
            
            if edge == -1:
                # 第一次访问节点u
                self.fa[u] = f
                self.dep[u] = self.dep[f] + 1
                self.siz[u] = 1
                edge = self.head[u]
            else:
                # 处理完一条边，继续下一条边
                edge = self.next_edge[edge]
            
            if edge != 0:
                # 还有边未处理，将当前状态重新入栈
                self.stack.append((u, f, edge))
                v = self.to_edge[edge]
                if v != f:
                    # 将子节点入栈
                    self.stack.append((v, u, -1))
            else:
                # 所有边处理完毕，计算重儿子
                e = self.head[u]
                while e != 0:
                    v = self.to_edge[e]
                    if v != f:
                        self.siz[u] += self.siz[v]
                        if self.son[u] == 0 or self.siz[self.son[u]] < self.siz[v]:
                            self.son[u] = v
                    e = self.next_edge[e]
    
    def dfs2_iterative(self):
        """第二次dfs的迭代实现，计算top, dfn, seg"""
        # 栈中存储 (当前节点, 重链顶端, 边索引)
        # 边索引为-1表示第一次访问该节点
        # 边索引为-2表示重儿子处理完毕，回到当前节点
        self.stack = [(self.root, self.root, -1)]
        self.cnt_dfn = 0
        
        while self.stack:
            u, t, edge = self.stack.pop()
            
            if edge == -1:
                # 第一次访问节点u，设置其所在重链的顶端
                self.top[u] = t
                self.cnt_dfn += 1
                self.dfn[u] = self.cnt_dfn
                self.seg[self.cnt_dfn] = u
                
                if self.son[u] == 0:
                    continue
                    
                # 先处理重儿子
                self.stack.append((u, t, -2))
                self.stack.append((self.son[u], t, -1))
                continue
            elif edge == -2:
                # 重儿子处理完毕，处理轻儿子
                edge = self.head[u]
            else:
                # 处理完一条边，继续下一条边
                edge = self.next_edge[edge]
            
            if edge != 0:
                # 还有边未处理，将当前状态重新入栈
                self.stack.append((u, t, edge))
                v = self.to_edge[edge]
                if v != self.fa[u] and v != self.son[u]:
                    # 轻儿子作为新重链的顶端
                    self.stack.append((v, v, -1))
    
    def path_add(self, x, y, v):
        """路径加法: 从x到y的路径上所有节点值增加v"""
        # 当两个节点不在同一条重链上时
        while self.top[x] != self.top[y]:
            # 保证x所在重链深度更深
            if self.dep[self.top[x]] < self.dep[self.top[y]]:
                x, y = y, x
            # 对x到其重链顶端这一段区间进行操作
            self.seg_tree.add(self.dfn[self.top[x]], self.dfn[x], v, 1, self.n, 1)
            # 跳到重链顶端的父节点，继续处理
            x = self.fa[self.top[x]]
        
        # 当两个节点在同一条重链上时，直接对区间进行操作
        # 保证dfn[x] <= dfn[y]
        if self.dep[x] > self.dep[y]:
            x, y = y, x
        self.seg_tree.add(self.dfn[x], self.dfn[y], v, 1, self.n, 1)
    
    def subtree_add(self, x, v):
        """子树加法: x的子树上所有节点值增加v"""
        # x的子树在dfs序上是连续的区间[dfn[x], dfn[x] + siz[x] - 1]
        self.seg_tree.add(self.dfn[x], self.dfn[x] + self.siz[x] - 1, v, 1, self.n, 1)
    
    def path_sum(self, x, y):
        """路径查询: 查询从x到y的路径上所有节点值的和"""
        ans = 0
        # 当两个节点不在同一条重链上时
        while self.top[x] != self.top[y]:
            # 保证x所在重链深度更深
            if self.dep[self.top[x]] < self.dep[self.top[y]]:
                x, y = y, x
            # 查询x到其重链顶端这一段区间的和
            ans = (ans + self.seg_tree.query(self.dfn[self.top[x]], self.dfn[x], 1, self.n, 1)) % self.MOD
            # 跳到重链顶端的父节点，继续处理
            x = self.fa[self.top[x]]
        
        # 当两个节点在同一条重链上时，直接查询区间[dfn[x], dfn[y]]的和
        # 保证dfn[x] <= dfn[y]
        if self.dep[x] > self.dep[y]:
            x, y = y, x
        ans = (ans + self.seg_tree.query(self.dfn[x], self.dfn[y], 1, self.n, 1)) % self.MOD
        return ans
    
    def subtree_sum(self, x):
        """子树查询: 查询x的子树上所有节点值的和"""
        # x的子树在dfs序上是连续的区间[dfn[x], dfn[x] + siz[x] - 1]
        return self.seg_tree.query(self.dfn[x], self.dfn[x] + self.siz[x] - 1, 1, self.n, 1)


def main():
    # 读取输入
    line = sys.stdin.readline().split()
    n, m, root, MOD = int(line[0]), int(line[1]), int(line[2]), int(line[3])
    
    # 读入每个节点的初始权值
    arr = [0] + list(map(int, sys.stdin.readline().split()))
    
    # 创建HLD对象
    hld = HLD(n, root, MOD)
    
    # 读取边信息
    for _ in range(n - 1):
        line = sys.stdin.readline().split()
        u, v = int(line[0]), int(line[1])
        hld.add_edge(u, v)
        hld.add_edge(v, u)
    
    # 树链剖分
    hld.dfs1_iterative()
    hld.dfs2_iterative()
    
    # 构建线段树
    hld.seg_tree.build(arr, hld.seg, 1, n, 1)
    
    # 处理操作
    for _ in range(m):
        line = list(map(int, sys.stdin.readline().split()))
        op = line[0]
        
        if op == 1:
            x, y, v = line[1], line[2], line[3]
            hld.path_add(x, y, v)
        elif op == 2:
            x, y = line[1], line[2]
            print(hld.path_sum(x, y))
        elif op == 3:
            x, v = line[1], line[2]
            hld.subtree_add(x, v)
        else:  # op == 4
            x = line[1]
            print(hld.subtree_sum(x))


if __name__ == "__main__":
    main()