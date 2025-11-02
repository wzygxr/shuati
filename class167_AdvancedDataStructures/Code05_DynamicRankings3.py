#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
动态排名问题 - 树状数组套线段树解法 (Python版本)

基础问题：洛谷 P2617 Dynamic Rankings
题目链接: https://www.luogu.com.cn/problem/P2617

问题描述：
给定一个长度为n的数组arr，下标从1到n，支持以下两种操作：
1. 查询操作 Q x y z：查询arr[x..y]区间内第z小的数字
2. 更新操作 C x y：将arr[x]位置的数字修改为y

算法思路：
这是一个经典的动态区间第k小问题，采用树状数组套线段树的数据结构来解决。

数据结构设计：
1. 外层使用树状数组(BIT)维护前缀信息
2. 内层使用权值线段树维护每个位置上数字的出现次数
3. 通过离散化处理大数值范围，将[0, 10^9]映射到[1, s]范围内

核心思想：
1. 对于查询操作，利用树状数组的前缀和特性，通过差分思想获取区间[x, y]的信息
2. 在线段树上进行二分查找，确定第k小的数字
3. 对于更新操作，先删除旧值，再插入新值

时间复杂度分析：
1. 预处理阶段：O(n log n) - 主要是离散化排序的时间复杂度
2. 单次查询操作：O(log n * log s) - 树状数组查询路径上各节点的线段树操作
3. 单次更新操作：O(log n * log s) - 树状数组更新路径上各节点的线段树操作
其中n为数组长度，s为离散化后的值域大小

空间复杂度分析：
1. 存储原始数组：O(n)
2. 树状数组：O(n)
3. 线段树节点：最坏情况下O(n * log s)，实际使用中远小于该值
总体空间复杂度：O(n * log s)

算法优势：
1. 支持动态修改和查询操作
2. 相比于平衡树套线段树，实现更简单
3. 常数因子较小，实际运行效率较高

算法劣势：
1. 空间消耗较大，特别是在线段树节点较多时
2. 实现复杂度高于单一数据结构

适用场景：
1. 需要频繁进行区间第k小查询
2. 数组元素可以动态修改
3. 查询和更新操作混合进行

更多类似题目：
1. HDU 2665 Kth number (静态区间第k小) - https://acm.hdu.edu.cn/showproblem.php?pid=2665
2. POJ 2761 Feed the dogs (静态区间第k小) - http://poj.org/problem?id=2761
3. Codeforces 786B Legacy (线段树优化建图) - https://codeforces.com/problemset/problem/786/B
4. SPOJ KQUERY - K-query (区间第k大查询) - https://www.spoj.com/problems/KQUERY/
5. LOJ 2587 「APIO2018」新家 (树状数组套线段树) - https://loj.ac/p/2587
6. AtCoder ARC033D Plane Partition (二维树状数组应用) - https://atcoder.jp/contests/arc033/tasks/arc033_4
7. UVa 12345 Dynamic len(RMQ) (动态区间问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3596
8. AcWing 241 楼兰图腾 (树状数组基础应用) - https://www.acwing.com/problem/content/243/
9. CodeChef DQUERY D-query (离线查询，莫队算法) - https://www.codechef.com/problems/DQUERY
10. HackerRank Median Updates (动态维护中位数) - https://www.hackerrank.com/challenges/median
11. 牛客网 NC15047 第k小数 (离线处理) - https://ac.nowcoder.com/acm/problem/15047
12. 51Nod 1107 斜率小于0的连线数量 (逆序对扩展) - https://www.51nod.com/Challenge/Problem.html#problemId=1107
13. SGU 417 Blackberry Jam (二维前缀和) - https://codeforces.com/problemsets/acmsguru/problem/99999/417
14. Codeforces 369E Valera and Queries (线段树优化) - https://codeforces.com/problemset/problem/369/E
15. UVA 11525 Permutation (树状数组构造排列) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2516

工程化考量：
1. 异常处理：在Python中需要注意索引越界和类型转换错误
2. 内存优化：Python中列表的内存管理较为灵活，但仍需注意大数据量下的内存使用
3. 性能优化：Python的递归深度有限，需要注意线段树的递归深度
4. 代码可读性：使用类封装提高代码复用性和可维护性
5. 测试与调试：添加断言和调试输出，便于问题定位

Python语言特性注意事项：
1. Python的递归深度默认限制为1000，对于深层线段树可能需要调整sys.setrecursionlimit()
2. Python的整数精度无限制，无需处理溢出问题
3. 使用bisect模块可以高效实现二分查找功能
4. 输入输出速度在Python中较慢，对于大数据量可以使用sys.stdin.readline()优化

输入格式：
第一行包含两个整数n和m，分别表示数组长度和操作次数
第二行包含n个整数，表示初始数组元素
接下来m行，每行描述一个操作：
  - "Q x y z" 表示查询操作
  - "C x y" 表示更新操作

输出格式：
对于每个查询操作，输出一行包含一个整数，表示查询结果

优化技巧：
1. 离散化时可以使用set去重，然后排序，提高效率
2. 使用对象属性而不是全局变量，提高代码的封装性
3. 对于Python中的递归深度问题，可以考虑使用非递归实现或调整递归深度限制
4. 使用位运算优化lowbit操作，提高计算效率
5. 对于大数据量输入，使用快速IO方法（如sys.stdin.readline）提高读取速度
"""

import sys
from bisect import bisect_left


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


def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    solver = DynamicRankings(n, m)
    
    # 读取初始数组
    for i in range(1, n + 1):
        solver.arr[i] = int(data[idx])
        idx += 1
    
    # 读取所有操作
    for i in range(1, m + 1):
        op = data[idx]
        idx += 1
        solver.ques[i][0] = 1 if op == "Q" else 2
        solver.ques[i][1] = int(data[idx])
        idx += 1
        solver.ques[i][2] = int(data[idx])
        idx += 1
        if solver.ques[i][0] == 1:
            solver.ques[i][3] = int(data[idx])
            idx += 1
    
    # 预处理
    solver.prepare()
    
    # 处理所有操作
    for i in range(1, m + 1):
        op = solver.ques[i][0]
        x = solver.ques[i][1]
        y = solver.ques[i][2]
        if op == 1:
            z = solver.ques[i][3]
            print(solver.number(x, y, z))
        else:
            solver.update(x, y)


if __name__ == "__main__":
    main()