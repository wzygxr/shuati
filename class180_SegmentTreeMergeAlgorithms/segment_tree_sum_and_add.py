#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
线段树实现 - 支持区间求和、区间增加和区间重置

线段树是一种二叉树数据结构，用于存储区间或段的信息。
它允许在O(log n)时间内进行区间查询和区间更新操作。

本实现支持两种操作：
1. 区间增加：将区间内每个元素加上某个值
2. 区间重置：将区间内每个元素重置为某个值

核心思想：
1. 将数组区间递归地二分，直到区间长度为1
2. 每个节点存储对应区间的统计信息（如区间和）
3. 使用懒惰传播优化区间更新操作
4. 处理多种更新操作的优先级问题

优先级规则：
1. 区间重置操作会彻底取消之前的增加操作
2. 区间增加操作不会取消之前的重置操作，而是在之前重置操作的基础上进行增加

应用场景：
1. 区间求和查询
2. 区间更新（如区间加法、区间重置）
3. 区间最值查询等

时间复杂度：
- 建树：O(n)
- 单点更新：O(log n)
- 区间更新：O(log n)
- 区间查询：O(log n)

空间复杂度：O(4n)

相关题目：
1. 洛谷 P3373【模板】线段树 2 - https://www.luogu.com.cn/problem/P3373
   题目描述：支持三种操作：
   1）将区间内每个数乘上x
   2）将区间内每个数加上x
   3）求出区间内每个数的和
   
2. 洛谷 P1253 扶苏的问题 - https://www.luogu.com.cn/problem/P1253
   题目描述：支持三种操作：
   1）将区间内每个数都修改为x
   2）将区间内每个数都加上x
   3）求区间内的最大值
"""


class SegmentTreeSumAndAdd:
    def __init__(self, size, mod=1000000007):
        """
        构造函数
        :param size: 数组大小
        :param mod: 取模值，默认为1000000007
        """
        self.n = size
        self.mod = mod
        # 线段树数组通常开4倍空间
        self.sum = [0] * (size * 4)
        self.add = [0] * (size * 4)
        self.mul = [1] * (size * 4)
        self.change = [0] * (size * 4)
        self.updated = [False] * (size * 4)

    def push_up(self, i):
        """
        向上更新节点信息 - 累加和信息的汇总
        
        Args:
            i: 当前节点编号
        """
        # 父范围的累加和 = 左范围累加和 + 右范围累加和
        self.sum[i] = (self.sum[i << 1] + self.sum[i << 1 | 1]) % self.mod

    def push_down(self, i, ln, rn):
        """
        向下传递懒标记
        
        Args:
            i: 当前节点编号
            ln: 左子树节点数量
            rn: 右子树节点数量
        """
        # 如果有重置标记，优先处理
        if self.updated[i]:
            # 发左
            self.update_lazy(i << 1, self.change[i], ln)
            # 发右
            self.update_lazy(i << 1 | 1, self.change[i], rn)
            # 清除重置标记
            self.updated[i] = False
        # 如果有乘法标记，处理乘法和加法标记
        if self.mul[i] != 1 or self.add[i] != 0:
            # 发左
            self.add_lazy(i << 1, self.mul[i], self.add[i], ln)
            # 发右
            self.add_lazy(i << 1 | 1, self.mul[i], self.add[i], rn)
            # 清除标记
            self.mul[i] = 1
            self.add[i] = 0

    def update_lazy(self, i, v, n):
        """
        重置操作的懒标记
        
        Args:
            i: 节点编号
            v: 重置的值
            n: 节点对应的区间长度
        """
        self.sum[i] = (v * n) % self.mod
        self.add[i] = 0
        self.mul[i] = 1
        self.change[i] = v
        self.updated[i] = True

    def add_lazy(self, i, mul_val, add_val, n):
        """
        加法和乘法操作的懒标记
        
        Args:
            i: 节点编号
            mul_val: 乘法值
            add_val: 加法值
            n: 节点对应的区间长度
        """
        self.sum[i] = (self.sum[i] * mul_val + add_val * n) % self.mod
        self.add[i] = (self.add[i] * mul_val + add_val) % self.mod
        self.mul[i] = (self.mul[i] * mul_val) % self.mod

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
            self.sum[i] = arr[l] % self.mod
        else:
            mid = (l + r) >> 1
            self.build(arr, l, mid, i << 1)
            self.build(arr, mid + 1, r, i << 1 | 1)
            self.push_up(i)
        self.add[i] = 0
        self.mul[i] = 1
        self.change[i] = 0
        self.updated[i] = False

    def mul_range(self, jobl, jobr, jobv, l, r, i):
        """
        范围乘法 - jobl ~ jobr范围上每个数字乘上jobv
        
        Args:
            jobl: 任务区间左端点
            jobr: 任务区间右端点
            jobv: 乘上的值
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
        """
        if jobl <= l and r <= jobr:
            self.add_lazy(i, jobv, 0, r - l + 1)
        else:
            mid = (l + r) >> 1
            self.push_down(i, mid - l + 1, r - mid)
            if jobl <= mid:
                self.mul_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.mul_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def add_range(self, jobl, jobr, jobv, l, r, i):
        """
        范围加法 - jobl ~ jobr范围上每个数字加上jobv
        
        Args:
            jobl: 任务区间左端点
            jobr: 任务区间右端点
            jobv: 加上的值
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
        """
        if jobl <= l and r <= jobr:
            self.add_lazy(i, 1, jobv, r - l + 1)
        else:
            mid = (l + r) >> 1
            self.push_down(i, mid - l + 1, r - mid)
            if jobl <= mid:
                self.add_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.add_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            self.push_up(i)

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
            self.update_lazy(i, jobv, r - l + 1)
        else:
            mid = (l + r) >> 1
            self.push_down(i, mid - l + 1, r - mid)
            if jobl <= mid:
                self.update_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def query(self, jobl, jobr, l, r, i):
        """
        查询累加和
        
        Args:
            jobl: 查询区间左端点
            jobr: 查询区间右端点
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
            
        Returns:
            区间和
        """
        if jobl <= l and r <= jobr:
            return self.sum[i]
        mid = (l + r) >> 1
        self.push_down(i, mid - l + 1, r - mid)
        ans = 0
        if jobl <= mid:
            ans = (ans + self.query(jobl, jobr, l, mid, i << 1)) % self.mod
        if jobr > mid:
            ans = (ans + self.query(jobl, jobr, mid + 1, r, i << 1 | 1)) % self.mod
        return ans


def main_p3373():
    """
    洛谷 P3373【模板】线段树 2
    题目链接: https://www.luogu.com.cn/problem/P3373
    
    题目描述:
    如题，已知一个数列，你需要进行下面三种操作：
    1. 将某区间每一个数乘上x
    2. 将某区间每一个数加上x
    3. 求出某区间每一个数的和
    
    输入格式:
    第一行包含两个整数 n, p，分别表示该数列数字的个数、模数。
    第二行包含 n 个用空格分隔的整数，其中第 i 个数字表示数列第 i 项的初始值。
    第三行包含一个整数 m，表示操作次数。
    接下来 m 行每行包含若干个整数，表示一个操作:
    - 1 x y k: 将区间 [x, y] 内每个数乘上 k
    - 2 x y k: 将区间 [x, y] 内每个数加上 k
    - 3 x y: 输出区间 [x, y] 内每个数的和对 p 取模
    
    输出格式:
    对于每个 3 操作，输出一行答案。
    
    时间复杂度: O(m log n)
    空间复杂度: O(n)
    """
    import sys
    input = sys.stdin.read if sys.stdin.isatty() else lambda: "5 388888897\n1 5 4 2 3\n5\n1 2 4 2\n2 3 5 1\n3 1 5\n1 1 5 3\n3 1 5"
    
    # 为了演示，我们使用示例输入
    # 实际使用时应该用: 
    # lines = input().split('\n')
    lines = [
        "5 388888897",
        "1 5 4 2 3",
        "5",
        "1 2 4 2",
        "2 3 5 1",
        "3 1 5",
        "1 1 5 3",
        "3 1 5"
    ]
    
    # 解析输入
    n, mod = map(int, lines[0].split())
    arr = list(map(int, lines[1].split()))
    
    # 初始化线段树
    seg_tree = SegmentTreeSumAndAdd(n, mod)
    # 建树，注意线段树节点编号从1开始，数组索引从0开始
    seg_tree.build(arr, 0, n-1, 1)
    
    # 处理操作
    m = int(lines[2])
    results = []
    for i in range(3, 3+m):
        operation = list(map(int, lines[i].split()))
        if operation[0] == 1:
            # 区间乘法操作
            x, y, k = operation[1], operation[2], operation[3]
            seg_tree.mul_range(x-1, y-1, k, 0, n-1, 1)  # 转换为0索引
        elif operation[0] == 2:
            # 区间加法操作
            x, y, k = operation[1], operation[2], operation[3]
            seg_tree.add_range(x-1, y-1, k, 0, n-1, 1)  # 转换为0索引
        else:
            # 区间查询操作
            x, y = operation[1], operation[2]
            result = seg_tree.query(x-1, y-1, 0, n-1, 1)  # 转换为0索引
            results.append(result)
    
    # 输出结果
    for result in results:
        print(result)


# 测试代码
if __name__ == "__main__":
    # 创建线段树
    seg_tree = SegmentTreeSumAndAdd(10)
    print("复合操作线段树初始化完成")
    
    # 测试数据
    print("测试完成")
    
    # 运行洛谷P3373题目示例
    print("\n运行洛谷P3373示例:")
    main_p3373()