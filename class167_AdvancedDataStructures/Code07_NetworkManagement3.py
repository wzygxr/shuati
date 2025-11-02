#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
网络管理问题 - 树链剖分 + 树状数组套线段树解法 (Python版本)

问题描述：
给定一棵包含n个节点的树，每个节点有一个点权。
支持以下两种操作：
1. 更新操作 0 x y：将节点x的点权修改为y
2. 查询操作 k x y：查询节点x到节点y路径上第k大的点权值，如果路径上节点数不足k个，则输出"invalid request!"

算法思路：
这是一个树上路径第k大查询问题，采用树链剖分 + 树状数组套线段树的解决方案。

数据结构设计：
1. 使用树链剖分将树上路径查询转化为区间查询问题
2. 外层使用树状数组维护DFS序上的信息
3. 内层使用权值线段树维护每个位置上数字的出现次数
4. 通过离散化处理大数值范围

核心思想：
1. 通过DFS序将树上操作转化为序列操作
2. 利用树状数组维护前缀信息，在线段树上进行第k大查询
3. 树上路径[x,y]的查询转化为4个DFS序区间的组合操作

时间复杂度分析：
1. 预处理阶段：O(n log n) - DFS序和离散化排序
2. 单次更新操作：O(log n * log s) - 树状数组更新路径上各节点的线段树操作
3. 单次查询操作：O(log²n * log s) - 树链剖分跳转 + 树状数组查询 + 线段树第k大查询
其中n为节点数，s为离散化后的值域大小

空间复杂度分析：
1. 存储树结构：O(n)
2. 树状数组：O(n)
3. 线段树节点：最坏情况下O(n * log s)，实际使用中远小于该值
4. 树链剖分辅助数组：O(n)
总体空间复杂度：O(n * log s)

算法优势：
1. 支持动态修改和查询操作
2. 可以处理任意树上路径查询
3. 相比于树链剖分套线段树，实现更简单

算法劣势：
1. 空间消耗较大
2. 常数因子较大

适用场景：
1. 树上动态路径第k大查询
2. 树上节点权值可以动态修改
3. 查询和更新操作混合进行

测试链接：https://www.luogu.com.cn/problem/P4175

输入格式：
第一行包含两个整数n和m，分别表示节点数和操作数
第二行包含n个整数，表示每个节点的初始点权
接下来n-1行，每行包含两个整数u和v，表示节点u和v之间有一条边
接下来m行，每行描述一个操作：
  - "0 x y" 表示更新操作
  - "k x y" 表示查询操作（k > 0）

输出格式：
对于每个查询操作，如果路径上节点数不足k个，输出"invalid request!"，否则输出第k大的点权值
"""

import sys
from collections import deque


class NetworkManagement:
    def __init__(self, n, m):
        self.MAXN = 80001
        self.MAXT = self.MAXN * 110
        self.MAXH = 18
        self.n = n
        self.m = m
        self.s = 0

        # 节点权值数组
        self.arr = [0] * self.MAXN

        # 操作记录数组
        self.ques = [[0] * 3 for _ in range(self.MAXN)]

        # 离散化数组，存储所有可能出现的数值并排序
        self.sorted = [0] * (self.MAXN << 1)

        # 链式前向星存储树结构
        self.head = [0] * self.MAXN    # 邻接表头
        self.next = [0] * (self.MAXN << 1)  # 下一条边
        self.to = [0] * (self.MAXN << 1)    # 边指向的节点
        self.cntg = 0  # 边计数器

        # 树状数组，root[i]表示以节点i为根的线段树根节点编号
        self.root = [0] * self.MAXN

        # 线段树节点信息
        self.left = [0] * self.MAXT    # 左子节点编号
        self.right = [0] * self.MAXT   # 右子节点编号
        self.sum = [0] * self.MAXT     # 节点维护的区间和（数字出现次数）

        self.cntt = 0  # 线段树节点计数器

        # 树链剖分和DFS序相关数组
        self.deep = [0] * self.MAXN    # 节点深度
        self.size = [0] * self.MAXN    # 节点子树大小
        self.dfn = [0] * self.MAXN     # 节点DFS序
        self.stjump = [[0] * self.MAXH for _ in range(self.MAXN)]  # 倍增跳转表
        self.cntd = 0  # DFS序计数器

        # 查询时使用的辅助数组
        self.addTree = [0] * self.MAXN     # 需要增加计数的线段树根节点
        self.minusTree = [0] * self.MAXN   # 需要减少计数的线段树根节点

        # 辅助数组元素计数器
        self.cntadd = 0
        self.cntminus = 0

    def addEdge(self, u, v):
        """
        添加一条无向边到链式前向星结构中
        :param u: 起点
        :param v: 终点
        """
        self.cntg += 1
        self.next[self.cntg] = self.head[u]
        self.to[self.cntg] = v
        self.head[u] = self.cntg

    def kth(self, num):
        """
        在已排序的sorted数组中查找数字num的位置（离散化后的值）
        :param num: 待查找的数字
        :return: 离散化后的值，如果未找到返回-1
        """
        left, right = 1, self.s
        while left <= right:
            mid = (left + right) // 2
            if self.sorted[mid] == num:
                return mid
            elif self.sorted[mid] < num:
                left = mid + 1
            else:
                right = mid - 1
        return -1

    def lowbit(self, i):
        """
        计算树状数组的lowbit值
        :param i: 输入数字
        :return: i的lowbit值，即i的二进制表示中最右边的1所代表的数值
        """
        return i & -i

    def dfs2(self):
        """
        DFS迭代版，用于计算树链剖分所需信息，避免递归爆栈
        """
        # DFS迭代版相关变量
        ufe = [[0] * 3 for _ in range(self.MAXN)]
        stackSize = 0

        def push(u, f, e):
            nonlocal stackSize
            ufe[stackSize][0] = u
            ufe[stackSize][1] = f
            ufe[stackSize][2] = e
            stackSize += 1

        def pop():
            nonlocal stackSize
            stackSize -= 1
            u = ufe[stackSize][0]
            f = ufe[stackSize][1]
            e = ufe[stackSize][2]
            return u, f, e

        # 从根节点1开始DFS
        push(1, 0, -1)
        while stackSize > 0:
            u, f, e = pop()
            if e == -1:
                # 第一次访问节点u
                self.deep[u] = self.deep[f] + 1  # 计算节点深度
                self.size[u] = 1                 # 初始化子树大小
                self.dfn[u] = self.cntd + 1      # 分配DFS序
                self.cntd += 1
                self.stjump[u][0] = f            # 初始化倍增跳转表

                # 构建倍增跳转表
                for p in range(1, self.MAXH):
                    self.stjump[u][p] = self.stjump[self.stjump[u][p - 1]][p - 1]

                e = self.head[u]  # 开始处理u的邻接边
            else:
                # 继续处理u的邻接边
                e = self.next[e]

            if e != 0:
                # 还有边需要处理
                push(u, f, e)  # 保存当前状态
                if self.to[e] != f:
                    # 如果邻接点不是父节点，则继续DFS
                    push(self.to[e], u, -1)
            else:
                # 所有邻接边处理完毕，计算子树大小
                e_ptr = self.head[u]
                while e_ptr > 0:
                    if self.to[e_ptr] != f:
                        self.size[u] += self.size[self.to[e_ptr]]
                    e_ptr = self.next[e_ptr]

    def lca(self, a, b):
        """
        计算两个节点的最近公共祖先(LCA)
        :param a: 节点a
        :param b: 节点b
        :return: 节点a和b的最近公共祖先
        """
        # 确保a的深度不小于b
        if self.deep[a] < self.deep[b]:
            a, b = b, a

        # 将a向上跳到与b同一深度
        for p in range(self.MAXH - 1, -1, -1):
            if self.deep[self.stjump[a][p]] >= self.deep[b]:
                a = self.stjump[a][p]

        # 如果a就是b的祖先，直接返回
        if a == b:
            return a

        # a和b一起向上跳，直到它们的父节点相同
        for p in range(self.MAXH - 1, -1, -1):
            if self.stjump[a][p] != self.stjump[b][p]:
                a = self.stjump[a][p]
                b = self.stjump[b][p]

        # 返回最近公共祖先
        return self.stjump[a][0]

    def innerAdd(self, jobi, jobv, l, r, i):
        """
        在线段树中增加或减少某个值的计数
        :param jobi: 需要操作的值（离散化后的索引）
        :param jobv: 操作的数值（+1表示增加，-1表示减少）
        :param l: 线段树当前节点维护的区间左端点
        :param r: 线段树当前节点维护的区间右端点
        :param i: 线段树当前节点编号（0表示需要新建节点）
        :return: 更新后的节点编号
        """
        if i == 0:
            self.cntt += 1  # 新建节点
            i = self.cntt
        if l == r:
            self.sum[i] += jobv  # 叶子节点，直接更新计数
        else:
            mid = (l + r) // 2
            if jobi <= mid:
                # 目标值在左半区间
                self.left[i] = self.innerAdd(jobi, jobv, l, mid, self.left[i])
            else:
                # 目标值在右半区间
                self.right[i] = self.innerAdd(jobi, jobv, mid + 1, r, self.right[i])
            # 更新当前节点的计数（左右子树计数之和）
            self.sum[i] = self.sum[self.left[i]] + self.sum[self.right[i]]
        return i

    def innerQuery(self, jobk, l, r):
        """
        在线段树上二分查找第k大的值
        :param jobk: 查找第k大的值
        :param l: 当前查询区间左端点
        :param r: 当前查询区间右端点
        :return: 第k大值在sorted数组中的索引
        """
        if l == r:
            return l  # 到达叶子节点，返回索引
        mid = (l + r) // 2

        # 计算所有加法操作在线段树左子树上的计数总和
        leftsum = 0
        for i in range(1, self.cntadd + 1):
            leftsum += self.sum[self.left[self.addTree[i]]]

        # 减去所有减法操作在线段树左子树上的计数总和
        for i in range(1, self.cntminus + 1):
            leftsum -= self.sum[self.left[self.minusTree[i]]]

        if jobk <= leftsum:
            # 第k大值在左子树中
            # 更新所有操作涉及的线段树节点为它们的左子节点
            for i in range(1, self.cntadd + 1):
                self.addTree[i] = self.left[self.addTree[i]]
            for i in range(1, self.cntminus + 1):
                self.minusTree[i] = self.left[self.minusTree[i]]
            return self.innerQuery(jobk, l, mid)
        else:
            # 第k大值在右子树中
            # 更新所有操作涉及的线段树节点为它们的右子节点
            for i in range(1, self.cntadd + 1):
                self.addTree[i] = self.right[self.addTree[i]]
            for i in range(1, self.cntminus + 1):
                self.minusTree[i] = self.right[self.minusTree[i]]
            return self.innerQuery(jobk - leftsum, mid + 1, r)

    def add(self, i, val, cnt):
        """
        在树状数组中增加或减少某个位置上值的计数
        :param i: DFS序位置
        :param val: 值（离散化后的索引）
        :param cnt: 操作数值（+1表示增加，-1表示减少）
        """
        while i <= self.n:
            self.root[i] = self.innerAdd(val, cnt, 1, self.s, self.root[i])
            i += self.lowbit(i)

    def update(self, i, v):
        """
        更新节点的点权
        :param i: 需要更新的节点编号
        :param v: 新的点权值
        """
        # 删除旧值
        self.add(self.dfn[i], self.arr[i], -1)
        self.add(self.dfn[i] + self.size[i], self.arr[i], 1)

        # 更新节点权值
        self.arr[i] = self.kth(v)

        # 插入新值
        self.add(self.dfn[i], self.arr[i], 1)
        self.add(self.dfn[i] + self.size[i], self.arr[i], -1)

    def query(self, x, y, k):
        """
        查询树上路径[x, y]中第k大的点权值
        :param x: 路径起点
        :param y: 路径终点
        :param k: 查询第k大
        :return: 第k大的点权值，如果不存在则返回-1
        """
        # 计算最近公共祖先
        lca_node = self.lca(x, y)
        lcafa = self.stjump[lca_node][0]  # LCA的父节点

        # 计算路径上节点数量
        num = self.deep[x] + self.deep[y] - self.deep[lca_node] - self.deep[lcafa]

        # 如果路径上节点数不足k个，返回-1
        if num < k:
            return -1

        # 初始化辅助数组
        self.cntadd = self.cntminus = 0

        # 收集路径x到根节点涉及的树状数组节点
        i = self.dfn[x]
        while i > 0:
            self.cntadd += 1
            self.addTree[self.cntadd] = self.root[i]
            i -= self.lowbit(i)

        # 收集路径y到根节点涉及的树状数组节点
        i = self.dfn[y]
        while i > 0:
            self.cntadd += 1
            self.addTree[self.cntadd] = self.root[i]
            i -= self.lowbit(i)

        # 减去路径lca到根节点涉及的树状数组节点（去重）
        i = self.dfn[lca_node]
        while i > 0:
            self.cntminus += 1
            self.minusTree[self.cntminus] = self.root[i]
            i -= self.lowbit(i)

        # 减去路径lca父节点到根节点涉及的树状数组节点
        i = self.dfn[lcafa]
        while i > 0:
            self.cntminus += 1
            self.minusTree[self.cntminus] = self.root[i]
            i -= self.lowbit(i)

        # 在线段树上二分查找第k大值，并通过sorted数组还原原始值
        # 注意：这里查找的是第(num - k + 1)小的值，等价于第k大的值
        return self.sorted[self.innerQuery(num - k + 1, 1, self.s)]

    def prepare(self):
        """
        预处理函数，包括离散化、DFS序计算和初始化树状数组
        """
        self.s = 0

        # 收集初始节点权值
        for i in range(1, self.n + 1):
            self.s += 1
            self.sorted[self.s] = self.arr[i]

        # 收集所有更新操作中涉及的值
        for i in range(1, self.m + 1):
            if self.ques[i][0] == 0:  # 更新操作
                self.s += 1
                self.sorted[self.s] = self.ques[i][2]

        # 对所有值进行排序
        self.sorted[1:self.s + 1] = sorted(self.sorted[1:self.s + 1])

        # 去重，得到离散化后的值域
        len_unique = 1
        for i in range(2, self.s + 1):
            if self.sorted[len_unique] != self.sorted[i]:
                len_unique += 1
                self.sorted[len_unique] = self.sorted[i]
        self.s = len_unique

        # 将原数组中的值替换为离散化后的索引
        for i in range(1, self.n + 1):
            self.arr[i] = self.kth(self.arr[i])

        # 计算DFS序和树链剖分信息
        self.dfs2()

        # 初始化树状数组
        for i in range(1, self.n + 1):
            self.add(self.dfn[i], self.arr[i], 1)
            self.add(self.dfn[i] + self.size[i], self.arr[i], -1)


def main():
    import sys
    input = sys.stdin.read
    data = input().split()

    idx = 0
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1

    solver = NetworkManagement(n, m)

    # 读取节点初始权值
    for i in range(1, n + 1):
        solver.arr[i] = int(data[idx])
        idx += 1

    # 读取树的边
    for i in range(1, n):
        u = int(data[idx])
        idx += 1
        v = int(data[idx])
        idx += 1
        solver.addEdge(u, v)
        solver.addEdge(v, u)

    # 读取所有操作
    for i in range(1, m + 1):
        solver.ques[i][0] = int(data[idx])
        idx += 1
        solver.ques[i][1] = int(data[idx])
        idx += 1
        solver.ques[i][2] = int(data[idx])
        idx += 1

    # 预处理
    solver.prepare()

    # 处理所有操作
    for i in range(1, m + 1):
        k = solver.ques[i][0]
        x = solver.ques[i][1]
        y = solver.ques[i][2]
        if k == 0:
            # 更新操作
            solver.update(x, y)
        else:
            # 查询操作
            ans = solver.query(x, y, k)
            if ans == -1:
                print("invalid request!")
            else:
                print(ans)


if __name__ == "__main__":
    main()