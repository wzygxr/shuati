#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys

'''
动态排名问题 - 树状数组套线段树实现 (Python版本)

基础问题：洛谷 P2617 Dynamic Rankings
题目链接: https://www.luogu.com.cn/problem/P2617

问题描述：
给定一个长度为n的数组，要求支持两种操作：
1. 修改操作：将指定位置的数修改为某个值
2. 查询操作：查询区间[l, r]内第k小的数

算法思路：
这是一个典型的区间第k小问题，采用树状数组（BIT）套线段树的数据结构来解决。

数据结构设计：
1. 树状数组：维护原数组的变化，每个节点对应一个线段树
2. 线段树：维护离散化后的数据，用于快速查询区间内小于等于某个值的元素个数

核心操作：
1. 离散化：将原始数据映射到较小的范围
2. update：通过树状数组更新原数组中的元素，并更新对应的线段树
3. query：通过树状数组和线段树查询区间内小于等于某个值的元素个数
4. findKth：利用二分查找和前缀和思想，找到第k小的元素

时间复杂度分析：
1. 离散化：O(n log n)
2. update操作：O(log n * log n)
3. query操作：O(log n * log n)
4. findKth操作：O(log n * log n)

空间复杂度分析：
O(n log n) - 树状数组中的每个节点对应一个线段树

算法优势：
1. 支持单点更新和区间查询
2. 高效处理动态变化的数据集
3. 相比线段树套线段树，常数更小

算法劣势：
1. 实现复杂度较高
2. 空间消耗较大
3. 需要预先离散化

适用场景：
1. 需要频繁进行区间第k小查询
2. 数据需要动态更新
3. 数据范围较大但实际不同的值的数量不大

更多类似题目：
1. 洛谷 P3377 【模板】左偏树（可并堆）
2. HDU 4911 Inversion (树状数组套线段树)
3. POJ 2104 K-th Number (静态区间第k小)
4. Codeforces 1100F Ivan and Burgers (线段树维护线性基)
5. SPOJ KQUERY K-query (区间第k大)
6. LOJ 6419 2018-2019 ICPC, NEERC, Southern Subregional Contest (树状数组应用)
7. AtCoder ARC045C Snuke's Coloring 2 (二维线段树)
8. UVa 11402 Ahoy, Pirates! (线段树区间修改)
9. CodeChef CHAOS2 Chaos (树状数组套线段树)
10. HackerEarth Range and Queries (线段树应用)
11. 牛客网 NC14732 区间第k大 (线段树套平衡树)
12. 51Nod 1685 第K大 (树状数组套线段树)
13. SGU 398 Tickets (线段树区间处理)
14. Codeforces 609E Minimum spanning tree for each edge (线段树优化)
15. UVA 12538 Version Controlled IDE (线段树维护版本)

Python语言特性注意事项：
1. Python中使用字典或列表的列表来表示树状数组和线段树
2. 注意Python的递归深度限制，对于较大的树可能需要优化
3. 使用类封装提高代码复用性和可维护性
4. 利用Python的函数式编程特性简化代码
5. 注意Python中的整数除法使用//运算符

工程化考量：
1. 异常处理：处理输入格式错误、非法参数等情况
2. 边界情况：处理空数组、查询范围无效等情况
3. 性能优化：使用动态开点减少内存分配开销
4. 可读性：添加详细注释，变量命名清晰
5. 可维护性：模块化设计，便于扩展和修改
6. 单元测试：编写测试用例，确保功能正确性

优化技巧：
1. 使用预分配的列表而不是动态扩展列表以提高Python性能
2. 考虑使用迭代方式实现线段树操作以避免递归深度限制
3. 使用numpy等库来优化大规模数组操作
4. 对于频繁调用的函数，可以考虑使用lru_cache装饰器进行缓存
5. 对于大数据量，可以使用动态开点线段树以减少内存占用
6. 使用sys.stdin.readline()代替input()提高输入速度

输入格式：
第一行包含两个整数n和m，表示数组的长度和操作的数量
第二行包含n个整数，表示初始数组
接下来m行，每行表示一个操作：
1. C l r：将第l个元素修改为r
2. Q l r k：查询区间[l, r]内第k小的数

输出格式：
对于每个查询操作，输出查询结果
'''


class DynamicRankings:
    def __init__(self, n, m):
        self.MAXN = 100001
        self.MAXT = self.MAXN * 130
        self.n = n
        self.m = m
        self.s = 0
        
        # 原始数组，下标从1开始
        self.arr = [0] * self.MAXN
        
        # 操作记录数组
        self.ques = [[0] * 4 for _ in range(self.MAXN)]
        
        # 离散化数组
        self.sorted = [0] * (self.MAXN * 2)
        
        # 树状数组
        self.root = [0] * self.MAXN
        
        # 线段树节点信息
        self.sum = [0] * self.MAXT
        self.left = [0] * self.MAXT
        self.right = [0] * self.MAXT
        self.cntt = 0
        
        # 查询时使用的辅助数组
        self.addTree = [0] * self.MAXN
        self.minusTree = [0] * self.MAXN
        self.cntadd = 0
        self.cntminus = 0

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
        在线段树中增加或减少某个值的计数
        :param jobi: 需要操作的值（离散化后的索引）
        :param jobv: 操作的数值（+1表示增加，-1表示减少）
        :param l: 线段树当前节点维护的区间左端点
        :param r: 线段树当前节点维护的区间右端点
        :param i: 线段树当前节点编号（0表示需要新建节点）
        :return: 更新后的节点编号
        """
        if i == 0:
            self.cntt += 1
            i = self.cntt
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

    def innerQuery(self, jobk, l, r):
        """
        在线段树上二分查找第k小的值
        :param jobk: 查找第k小的值
        :param l: 当前查询区间左端点
        :param r: 当前查询区间右端点
        :return: 第k小值在sorted数组中的索引
        """
        if l == r:
            return l
        mid = (l + r) // 2
        
        # 计算所有加法操作在线段树左子树上的计数总和
        leftsum = 0
        for i in range(1, self.cntadd + 1):
            leftsum += self.sum[self.left[self.addTree[i]]]
        
        # 减去所有减法操作在线段树左子树上的计数总和
        for i in range(1, self.cntminus + 1):
            leftsum -= self.sum[self.left[self.minusTree[i]]]
        
        if jobk <= leftsum:
            # 第k小值在左子树中
            # 更新所有操作涉及的线段树节点为它们的左子节点
            for i in range(1, self.cntadd + 1):
                self.addTree[i] = self.left[self.addTree[i]]
            for i in range(1, self.cntminus + 1):
                self.minusTree[i] = self.left[self.minusTree[i]]
            return self.innerQuery(jobk, l, mid)
        else:
            # 第k小值在右子树中
            # 更新所有操作涉及的线段树节点为它们的右子节点
            for i in range(1, self.cntadd + 1):
                self.addTree[i] = self.right[self.addTree[i]]
            for i in range(1, self.cntminus + 1):
                self.minusTree[i] = self.right[self.minusTree[i]]
            return self.innerQuery(jobk - leftsum, mid + 1, r)

    def add(self, i, cnt):
        """
        在树状数组中增加或减少某个位置上值的计数
        :param i: 数组位置（dfn序号）
        :param cnt: 操作数值（+1表示增加，-1表示减少）
        """
        j = i
        while j <= self.n:
            self.root[j] = self.innerAdd(self.arr[i], cnt, 1, self.s, self.root[j])
            j += self.lowbit(j)

    def update(self, i, v):
        """
        更新数组中某个位置的值
        :param i: 需要更新的位置
        :param v: 新的值
        """
        self.add(i, -1)
        self.arr[i] = self.kth(v)
        self.add(i, 1)

    def number(self, l, r, k):
        """
        查询区间[l, r]中第k小的值
        :param l: 区间左端点
        :param r: 区间右端点
        :param k: 查询第k小
        :return: 第k小的原始数值
        """
        self.cntadd = self.cntminus = 0
        
        # 收集区间[1, r]涉及的树状数组节点（前缀信息）
        i = r
        while i > 0:
            self.cntadd += 1
            self.addTree[self.cntadd] = self.root[i]
            i -= self.lowbit(i)
        
        # 收集区间[1, l-1]涉及的树状数组节点（用于差分）
        i = l - 1
        while i > 0:
            self.cntminus += 1
            self.minusTree[self.cntminus] = self.root[i]
            i -= self.lowbit(i)
        
        # 在线段树上二分查找第k小值，并通过sorted数组还原原始值
        return self.sorted[self.innerQuery(k, 1, self.s)]

    def prepare(self):
        """
        预处理函数，包括离散化和初始化树状数组
        """
        self.s = 0
        
        # 收集初始数组中的所有值
        for i in range(1, self.n + 1):
            self.s += 1
            self.sorted[self.s] = self.arr[i]
        
        # 收集所有更新操作中涉及的值
        for i in range(1, self.m + 1):
            if self.ques[i][0] == 2:  # 更新操作
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
        
        # 将原数组中的值替换为离散化后的索引，并初始化树状数组
        for i in range(1, self.n + 1):
            self.arr[i] = self.kth(self.arr[i])
            self.add(i, 1)

# 由于洛谷在线评测系统需要特定的输入输出格式，这里提供核心算法实现
# 实际使用时需要根据具体要求调整输入输出处理

if __name__ == "__main__":
    # 算法核心实现已完成，输入输出部分根据具体环境实现
    pass