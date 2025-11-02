#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
排队问题 - 树状数组套线段树解法 (Python版本)

问题描述：
给定一个长度为n的数组arr，下标从1到n。
如果存在i < j，并且arr[i] > arr[j]，那么(i,j)就叫做一个逆序对。
首先打印原始arr中有多少逆序对，然后进行m次操作：
操作 a b：交换arr中a位置和b位置的数，打印数组中逆序对的数量。

算法思路：
这是一个动态维护逆序对数量的问题。采用树状数组套线段树的数据结构来解决。

数据结构设计：
1. 外层使用树状数组(BIT)维护位置信息
2. 内层使用权值线段树维护每个位置上数字的出现次数
3. 通过离散化处理大数值范围，将[1, 10^9]映射到[1, s]范围内

核心思想：
1. 初始时，从左到右依次处理每个元素，通过查询前面元素中比当前元素大的数量来统计初始逆序对
2. 交换操作时，通过数学推导计算交换带来的逆序对数量变化
3. 利用树状数组维护前缀信息，在线段树上进行区间查询和单点更新

时间复杂度分析：
1. 预处理阶段：O(n log n) - 主要是离散化排序的时间复杂度
2. 初始逆序对计算：O(n log n * log s) - 对每个元素查询前面比它大的元素数量
3. 单次交换操作：O(log n * log s) - 计算交换带来的逆序对变化并更新数据结构
其中n为数组长度，s为离散化后的值域大小

空间复杂度分析：
1. 存储原始数组：O(n)
2. 树状数组：O(n)
3. 线段树节点：最坏情况下O(n * log s)，实际使用中远小于该值
总体空间复杂度：O(n * log s)

算法优势：
1. 支持动态交换操作和逆序对数量的实时维护
2. 相比于朴素O(n²)算法，效率大幅提升
3. 实现相对简单，常数因子较小

算法劣势：
1. 空间消耗较大
2. 交换操作的逆序对变化计算较为复杂

适用场景：
1. 需要动态维护数组的逆序对数量
2. 数组元素可以交换但整体结构保持不变
3. 查询操作频繁

测试链接：https://www.luogu.com.cn/problem/P1975

输入格式：
第一行包含一个整数n，表示数组长度
第二行包含n个整数，表示初始数组元素
第三行包含一个整数m，表示操作次数
接下来m行，每行包含两个整数a和b，表示交换操作

输出格式：
第一行输出初始逆序对数量
接下来m行，每行输出一次交换操作后的逆序对数量
"""

import sys


class LineUp:
    def __init__(self, n, m):
        self.MAXN = 20001
        self.MAXT = self.MAXN * 80
        self.INF = 1000000001
        self.n = n
        self.m = m
        self.s = 0

        # 原始数组，下标从1开始
        self.arr = [0] * self.MAXN

        # 离散化数组，存储所有可能出现的数值并排序
        self.sorted = [0] * (self.MAXN + 2)

        # 树状数组，root[i]表示以节点i为根的线段树根节点编号
        self.root = [0] * self.MAXN

        # 线段树节点信息
        self.left = [0] * self.MAXT   # 左子节点编号
        self.right = [0] * self.MAXT  # 右子节点编号
        self.sum = [0] * self.MAXT    # 节点维护的区间和（数字出现次数）

        # 线段树节点计数器
        self.cnt = 0

        # 当前逆序对总数
        self.ans = 0

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

    def innerAdd(self, jobi, jobv, l, r, i):
        """
        线段树单点修改，增加或减少某个值的计数
        :param jobi: 需要操作的值（离散化后的索引）
        :param jobv: 操作的数值（+1表示增加，-1表示减少）
        :param l: 线段树当前节点维护的区间左端点
        :param r: 线段树当前节点维护的区间右端点
        :param i: 线段树当前节点编号（0表示需要新建节点）
        :return: 更新后的节点编号
        """
        if i == 0:
            self.cnt += 1
            i = self.cnt
        if l == r:
            self.sum[i] += jobv
        else:
            mid = (l + r) // 2
            if jobi <= mid:
                self.left[i] = self.innerAdd(jobi, jobv, l, mid, self.left[i])
            else:
                self.right[i] = self.innerAdd(jobi, jobv, mid + 1, r, self.right[i])
            self.sum[i] = self.sum[self.left[i]] + self.sum[self.right[i]]
        return i

    def innerQuery(self, jobl, jobr, l, r, i):
        """
        查询线段树上某个值域区间内的元素数量
        :param jobl: 查询值域区间左端点
        :param jobr: 查询值域区间右端点
        :param l: 线段树当前节点维护的区间左端点
        :param r: 线段树当前节点维护的区间右端点
        :param i: 线段树当前节点编号
        :return: 值域[jobl, jobr]内元素的数量
        """
        if i == 0:
            return 0
        # 当前节点维护的区间完全包含在查询区间内
        if jobl <= l and r <= jobr:
            return self.sum[i]
        mid = (l + r) // 2
        ans = 0
        # 查询左子树
        if jobl <= mid:
            ans += self.innerQuery(jobl, jobr, l, mid, self.left[i])
        # 查询右子树
        if jobr > mid:
            ans += self.innerQuery(jobl, jobr, mid + 1, r, self.right[i])
        return ans

    def add(self, i, v):
        """
        在树状数组中增加或减少某个位置上值的计数
        :param i: 数组位置
        :param v: 操作数值（+1表示增加，-1表示减少）
        """
        j = i
        while j <= self.n:
            self.root[j] = self.innerAdd(self.arr[i], v, 1, self.s, self.root[j])
            j += self.lowbit(j)

    def query(self, al, ar, numl, numr):
        """
        查询区间[al, ar]中，值域[numl, numr]范围内元素的数量
        :param al: 查询区间左端点
        :param ar: 查询区间右端点
        :param numl: 值域区间左端点
        :param numr: 值域区间右端点
        :return: 满足条件的元素数量
        """
        ans = 0
        # 收集区间[1, ar]涉及的树状数组节点（前缀信息）
        i = ar
        while i > 0:
            ans += self.innerQuery(numl, numr, 1, self.s, self.root[i])
            i -= self.lowbit(i)
        # 减去区间[1, al-1]涉及的树状数组节点（用于差分）
        i = al - 1
        while i > 0:
            ans -= self.innerQuery(numl, numr, 1, self.s, self.root[i])
            i -= self.lowbit(i)
        return ans

    def compute(self, a, b):
        """
        交换a和b位置的数字，并更新逆序对数量
        保证a在前，b在后
        :param a: 位置a
        :param b: 位置b
        """
        # 减去交换前由arr[a]和arr[b]贡献的逆序对数量
        # arr[a]与区间(a,b)中比它小的元素形成的逆序对
        self.ans -= self.query(a + 1, b - 1, 1, self.arr[a] - 1)
        # 区间(a,b)中比arr[a]大的元素与arr[a]形成的逆序对
        self.ans += self.query(a + 1, b - 1, self.arr[a] + 1, self.s)
        # arr[b]与区间(a,b)中比它小的元素形成的逆序对
        self.ans -= self.query(a + 1, b - 1, self.arr[b] + 1, self.s)
        # 区间(a,b)中比arr[b]大的元素与arr[b]形成的逆序对
        self.ans += self.query(a + 1, b - 1, 1, self.arr[b] - 1)

        # 处理arr[a]和arr[b]直接形成的逆序对
        if self.arr[a] < self.arr[b]:
            self.ans += 1  # 交换后会形成逆序对
        elif self.arr[a] > self.arr[b]:
            self.ans -= 1  # 交换后逆序对消失

        # 更新数据结构中的值
        self.add(a, -1)  # 删除位置a的旧值
        self.add(b, -1)  # 删除位置b的旧值

        # 交换两个位置的值
        tmp = self.arr[a]
        self.arr[a] = self.arr[b]
        self.arr[b] = tmp

        # 插入位置a和b的新值
        self.add(a, 1)
        self.add(b, 1)

    def prepare(self):
        """
        预处理函数，包括离散化和初始化树状数组
        """
        self.s = 0
        # 收集初始数组中的所有值
        for i in range(1, self.n + 1):
            self.s += 1
            self.sorted[self.s] = self.arr[i]

        # 添加边界值以处理边界情况
        self.s += 1
        self.sorted[self.s] = -self.INF
        self.s += 1
        self.sorted[self.s] = self.INF

        # 对所有值进行排序
        self.sorted[1:self.s + 1] = sorted(self.sorted[1:self.s + 1])

        # 去重，得到离散化后的值域
        len_unique = 1
        for i in range(2, self.s + 1):
            if self.sorted[len_unique] != self.sorted[i]:
                len_unique += 1
                self.sorted[len_unique] = self.sorted[i]
        self.s = len_unique

        # 将原数组中的值替换为离散化后的索引，并初始化树状数组
        for i in range(1, self.n + 1):
            self.arr[i] = self.kth(self.arr[i])
            self.add(i, 1)


def main():
    import sys
    input = sys.stdin.read
    data = input().split()

    idx = 0
    n = int(data[idx])
    idx += 1

    solver = LineUp(n, 0)

    # 读取初始数组
    for i in range(1, n + 1):
        solver.arr[i] = int(data[idx])
        idx += 1

    # 预处理
    solver.prepare()

    # 计算初始逆序对数量
    for i in range(2, n + 1):
        # 查询位置1到i-1中比arr[i]大的元素数量
        solver.ans += solver.query(1, i - 1, solver.arr[i] + 1, solver.s)
    print(solver.ans)

    m = int(data[idx])
    idx += 1

    # 处理所有交换操作
    for i in range(1, m + 1):
        a = int(data[idx])
        idx += 1
        b = int(data[idx])
        idx += 1

        # 确保a <= b
        if a > b:
            tmp = a
            a = b
            b = tmp

        # 执行交换操作并更新逆序对数量
        solver.compute(a, b)
        print(solver.ans)


if __name__ == "__main__":
    main()