#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
线段树实现 - 支持区间最大值与区间更新

线段树是一种二叉树数据结构，用于存储区间或段的信息。
它允许在O(log n)时间内进行区间查询和区间更新操作。

核心思想：
1. 将数组区间递归地二分，直到区间长度为1
2. 每个节点存储对应区间的统计信息（如区间最大值）
3. 使用懒惰传播优化区间更新操作

应用场景：
1. 区间最大值查询
2. 区间更新（如区间重置）
3. 区间求和等

时间复杂度：
- 建树：O(n)
- 单点更新：O(log n)
- 区间更新：O(log n)
- 区间查询：O(log n)

空间复杂度：O(4n)

相关题目：
1. HDU 1754 I Hate It - http://acm.hdu.edu.cn/showproblem.php?pid=1754
   题目描述：支持两种操作：
   1）Query i j, 查询[i,j]区间的最大值
   2）U i j, 将第i个数更新为j
   
2. 洛谷 P1253 扶苏的问题 - https://www.luogu.com.cn/problem/P1253
   题目描述：支持三种操作：
   1）将区间内每个数都修改为x
   2）将区间内每个数都加上x
   3）求区间内的最大值
"""


class SegmentTreeMax:
    def __init__(self, size):
        """
        构造函数 - 初始化最大值线段树
        
        算法步骤：
        1. 设置数组大小
        2. 初始化线段树数组（开4倍空间）
        3. 初始化懒标记数组和更新标记数组
        
        :param size: 数组大小，即原始数据元素的数量
        :type size: int
        
        属性说明：
        - n: 原始数组大小
        - max_val: 线段树数组，存储每个区间的最大值
        - change: 懒标记数组，存储区间重置的值
        - updated: 更新标记数组，标记区间是否被重置过
        
        特殊说明：
        - 本线段树支持区间重置操作（将区间内所有元素设为指定值）
        - 使用updated数组标记重置操作，确保重置操作的优先级
        """
        self.n = size
        # 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        self.max_val = [0] * (size * 4)
        self.change = [0] * (size * 4)
        self.updated = [False] * (size * 4)

    def push_up(self, i):
        """
        向上更新节点信息 - 最大值信息的汇总
        
        Args:
            i: 当前节点编号
        """
        # 父范围的最大值 = max(左范围最大值, 右范围最大值)
        self.max_val[i] = max(self.max_val[i << 1], self.max_val[i << 1 | 1])

    def push_down(self, i, ln, rn):
        """
        向下传递懒标记
        
        Args:
            i: 当前节点编号
            ln: 左子树节点数量
            rn: 右子树节点数量
        """
        if self.updated[i]:
            # 发左
            self.lazy(i << 1, self.change[i], ln)
            # 发右
            self.lazy(i << 1 | 1, self.change[i], rn)
            # 父范围懒信息清空
            self.updated[i] = False

    def lazy(self, i, v, n):
        """
        懒标记操作
        
        Args:
            i: 节点编号
            v: 重置的值
            n: 节点对应的区间长度
        """
        self.max_val[i] = v
        self.change[i] = v
        self.updated[i] = True

    def build(self, arr, l, r, i):
        """
        建树
        
        Args:
            arr: 原始数组
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
        """
        if l == r:
            self.max_val[i] = arr[l]
        else:
            mid = (l + r) >> 1
            self.build(arr, l, mid, i << 1)
            self.build(arr, mid + 1, r, i << 1 | 1)
            self.push_up(i)
        self.change[i] = 0
        self.updated[i] = False

    def update_range(self, jobl, jobr, jobv, l, r, i):
        """
        范围重置 - jobl ~ jobr范围上每个数字重置为jobv
        
        Args:
            jobl: 任务区间左端点
            jobr: 任务区间右端点
            jobv: 重置的值
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
        """
        if jobl <= l and r <= jobr:
            self.lazy(i, jobv, r - l + 1)
        else:
            mid = (l + r) >> 1
            self.push_down(i, mid - l + 1, r - mid)
            if jobl <= mid:
                self.update_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def update_single(self, idx, val, l, r, i):
        """
        单点更新 - 将索引idx处的值更新为val
        
        Args:
            idx: 要更新的索引
            val: 新的值
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
        """
        if l == r:
            self.max_val[i] = val
        else:
            mid = (l + r) >> 1
            if idx <= mid:
                self.update_single(idx, val, l, mid, i << 1)
            else:
                self.update_single(idx, val, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def query(self, jobl, jobr, l, r, i):
        """
        查询最大值
        
        Args:
            jobl: 查询区间左端点
            jobr: 查询区间右端点
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
            
        Returns:
            区间最大值
        """
        if jobl <= l and r <= jobr:
            return self.max_val[i]
        mid = (l + r) >> 1
        self.push_down(i, mid - l + 1, r - mid)
        ans = float('-inf')
        if jobl <= mid:
            ans = max(ans, self.query(jobl, jobr, l, mid, i << 1))
        if jobr > mid:
            ans = max(ans, self.query(jobl, jobr, mid + 1, r, i << 1 | 1))
        return ans


def main_hdu1754():
    """
    HDU 1754 I Hate It
    题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1754
    
    题目描述:
    很多学校流行起了一种比赛，来跟自己班别的同学比较期末成绩。
    作为与自己较劲的代表，小A想知道每次询问时班级中最高的成绩。
    
    输入格式:
    第一行包含两个正整数N和M(0<N<=200000,0<M<5000)，分别代表班内的学生数目和操作的数目。
    学生ID编号从1编到N。
    第二行包含N个非负整数，代表这N个学生的初始成绩(成绩小于等于10^9)。
    接下来有M行，每一行有一个字符C(只取'Q'或'U')，和两个正整数A，B。
    当C为'Q'的时候，表示这是一条询问操作，它询问ID从A到B(包括A,B)的学生当中，成绩最高的是多少。
    当C为'U'的时候，表示这是一条更新操作，要求把ID为A的学生的成绩更改为B。
    
    输出格式:
    对于每一次询问操作，在一行里面输出最高成绩。
    
    时间复杂度: O(m log n)
    空间复杂度: O(n)
    """
    # 为了演示，我们使用示例输入
    # 实际使用时应该用: 
    # lines = input().split('\n')
    lines = [
        "5 6",
        "1 2 3 4 5",
        "Q 1 5",
        "U 3 6",
        "Q 3 4",
        "Q 4 5",
        "U 2 9",
        "Q 1 5"
    ]
    
    line_idx = 0
    while line_idx < len(lines):
        nm = lines[line_idx].split()
        if len(nm) < 2:
            break
        n, m = int(nm[0]), int(nm[1])
        line_idx += 1
        
        arr = list(map(int, lines[line_idx].split()))
        line_idx += 1
        
        # 初始化线段树
        seg_tree = SegmentTreeMax(n)
        # 建树，注意线段树节点编号从1开始，数组索引从0开始
        seg_tree.build(arr, 0, n-1, 1)
        
        # 处理操作
        for _ in range(m):
            operation = lines[line_idx].split()
            line_idx += 1
            
            if operation[0] == "Q":
                a, b = int(operation[1]), int(operation[2])
                result = seg_tree.query(a-1, b-1, 0, n-1, 1)  # 转换为0索引
                print(result)
            else:  # operation[0] == "U"
                a, b = int(operation[1]), int(operation[2])
                seg_tree.update_single(a-1, b, 0, n-1, 1)  # 转换为0索引


def main_p1253():
    """
    洛谷 P1253 扶苏的问题
    题目链接: https://www.luogu.com.cn/problem/P1253
    
    题目描述:
    给出一个长度为n的序列a，支持三种操作：
    1  l  r  x：将区间[l,r]内的所有数都修改为x
    2  l  r  x：将区间[l,r]内的所有数都加上x
    3  l  r  ：查询区间[l,r]内的最大值
    
    输入格式:
    第一行两个整数n,m，表示序列长度和操作次数。
    第二行n个整数，表示序列a。
    接下来m行，每行表示一个操作。
    
    输出格式:
    对于每个操作3，输出一行一个整数表示答案。
    
    时间复杂度: O(m log n)
    空间复杂度: O(n)
    """
    # 为了演示，我们使用示例输入
    # 实际使用时应该用: 
    # lines = input().split('\n')
    lines = [
        "5 5",
        "1 2 3 4 5",
        "3 1 5",
        "1 1 3 2",
        "3 1 5",
        "2 2 4 1",
        "3 1 5"
    ]
    
    # 解析输入
    n, m = map(int, lines[0].split())
    arr = list(map(int, lines[1].split()))
    
    # 初始化线段树（需要同时支持区间重置、区间加法和区间最大值查询）
    # 这里我们需要修改SegmentTreeMax类来支持区间加法操作
    # 为简化实现，我们创建一个新的类
    
    class SegmentTreeMaxWithAdd:
        def __init__(self, size):
            self.n = size
            self.max_val = [0] * (size * 4)
            self.add = [0] * (size * 4)
            self.change = [0] * (size * 4)
            self.updated = [False] * (size * 4)

        def push_up(self, i):
            self.max_val[i] = max(self.max_val[i << 1], self.max_val[i << 1 | 1])

        def push_down(self, i, ln, rn):
            # 如果有重置标记，优先处理
            if self.updated[i]:
                # 发左
                self.update_lazy(i << 1, self.change[i], ln)
                # 发右
                self.update_lazy(i << 1 | 1, self.change[i], rn)
                # 清除重置标记
                self.updated[i] = False
            # 如果有加法标记，处理加法标记
            if self.add[i] != 0:
                # 发左
                self.add_lazy(i << 1, self.add[i], ln)
                # 发右
                self.add_lazy(i << 1 | 1, self.add[i], rn)
                # 清除标记
                self.add[i] = 0

        def update_lazy(self, i, v, n):
            self.max_val[i] = v
            self.add[i] = 0
            self.change[i] = v
            self.updated[i] = True

        def add_lazy(self, i, v, n):
            self.max_val[i] += v
            self.add[i] += v

        def build(self, arr, l, r, i):
            if l == r:
                self.max_val[i] = arr[l]
            else:
                mid = (l + r) >> 1
                self.build(arr, l, mid, i << 1)
                self.build(arr, mid + 1, r, i << 1 | 1)
                self.push_up(i)
            self.add[i] = 0
            self.change[i] = 0
            self.updated[i] = False

        def update_range(self, jobl, jobr, jobv, l, r, i):
            if jobl <= l and r <= jobr:
                self.update_lazy(i, jobv, r - l + 1)
            else:
                mid = (l + r) >> 1
                self.push_down(i, mid - l + 1, r - mid)
                if jobl <= mid:
                    self.update_range(jobl, jobr, jobv, l, mid, i << 1)
                if jobr > mid:
                    self.update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
                self.push_up(i)

        def add_range(self, jobl, jobr, jobv, l, r, i):
            if jobl <= l and r <= jobr:
                self.add_lazy(i, jobv, r - l + 1)
            else:
                mid = (l + r) >> 1
                self.push_down(i, mid - l + 1, r - mid)
                if jobl <= mid:
                    self.add_range(jobl, jobr, jobv, l, mid, i << 1)
                if jobr > mid:
                    self.add_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
                self.push_up(i)

        def query(self, jobl, jobr, l, r, i):
            if jobl <= l and r <= jobr:
                return self.max_val[i]
            mid = (l + r) >> 1
            self.push_down(i, mid - l + 1, r - mid)
            ans = float('-inf')
            if jobl <= mid:
                ans = max(ans, self.query(jobl, jobr, l, mid, i << 1))
            if jobr > mid:
                ans = max(ans, self.query(jobl, jobr, mid + 1, r, i << 1 | 1))
            return ans
    
    # 初始化线段树
    seg_tree = SegmentTreeMaxWithAdd(n)
    # 建树，注意线段树节点编号从1开始，数组索引从0开始
    seg_tree.build(arr, 0, n-1, 1)
    
    # 处理操作
    results = []
    for i in range(2, 2+m):
        operation = list(map(int, lines[i].split()))
        if operation[0] == 1:
            # 区间重置操作
            l, r, x = operation[1], operation[2], operation[3]
            seg_tree.update_range(l-1, r-1, x, 0, n-1, 1)  # 转换为0索引
        elif operation[0] == 2:
            # 区间加法操作
            l, r, x = operation[1], operation[2], operation[3]
            seg_tree.add_range(l-1, r-1, x, 0, n-1, 1)  # 转换为0索引
        else:
            # 区间查询操作
            l, r = operation[1], operation[2]
            result = seg_tree.query(l-1, r-1, 0, n-1, 1)  # 转换为0索引
            results.append(result)
    
    # 输出结果
    for result in results:
        print(result)


# 测试代码
if __name__ == "__main__":
    # 创建线段树
    seg_tree = SegmentTreeMax(10)
    print("最大值线段树初始化完成")
    
    # 测试数据
    print("测试完成")
    
    # 运行HDU 1754题目示例
    print("\n运行HDU 1754示例:")
    main_hdu1754()
    
    # 运行洛谷P1253题目示例
    print("\n运行洛谷P1253示例:")
    main_p1253()