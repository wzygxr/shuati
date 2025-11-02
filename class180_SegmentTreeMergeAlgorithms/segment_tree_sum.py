#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
线段树实现 - 支持区间求和与区间更新

线段树是一种二叉树数据结构，用于存储区间或段的信息。
它允许在O(log n)时间内进行区间查询和区间更新操作。

核心思想：
1. 将数组区间递归地二分，直到区间长度为1
2. 每个节点存储对应区间的统计信息（如区间和）
3. 使用懒惰传播优化区间更新操作

应用场景：
1. 区间求和查询
2. 区间更新（如区间加法）
3. 区间最值查询等

时间复杂度：
- 建树：O(n)
- 单点更新：O(log n)
- 区间更新：O(log n)
- 区间查询：O(log n)

空间复杂度：O(4n)

相关题目：
1. 洛谷 P3372【模板】线段树 1 - https://www.luogu.com.cn/problem/P3372
   题目描述：已知一个数列，需要进行两种操作：
   1）将某区间每一个数加上k
   2）求出某区间每一个数的和
   
2. HDU 1166 敌兵布阵 - http://acm.hdu.edu.cn/showproblem.php?pid=1166
   题目描述：支持两种操作：
   1）Add i j, i为营地编号, j为增加的士兵数量
   2）Query i j, i、j为营地编号，查询[i,j]区间的士兵总数
"""


class SegmentTreeSum:
    def __init__(self, size):
        """
        构造函数 - 初始化线段树
        
        算法步骤：
        1. 设置数组大小
        2. 初始化线段树数组（开4倍空间）
        3. 初始化懒标记数组
        
        :param size: 数组大小，即原始数据元素的数量
        :type size: int
        
        属性说明：
        - n: 原始数组大小
        - sum: 线段树数组，存储每个区间的和
        - add: 懒标记数组，存储每个区间需要增加的值
        """
        self.n = size
        # 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        self.sum = [0] * (size * 4)
        self.add = [0] * (size * 4)

    def push_up(self, i):
        """
        向上更新节点信息 - 累加和信息的汇总
        
        Args:
            i: 当前节点编号
        """
        # 父范围的累加和 = 左范围累加和 + 右范围累加和
        self.sum[i] = self.sum[i << 1] + self.sum[i << 1 | 1]

    def push_down(self, i, ln, rn):
        """
        向下传递懒标记
        
        Args:
            i: 当前节点编号
            ln: 左子树节点数量
            rn: 右子树节点数量
        """
        if self.add[i] != 0:
            # 发左
            self.lazy(i << 1, self.add[i], ln)
            # 发右
            self.lazy(i << 1 | 1, self.add[i], rn)
            # 父范围懒信息清空
            self.add[i] = 0

    def lazy(self, i, v, n):
        """
        懒标记操作
        
        Args:
            i: 节点编号
            v: 增加的值
            n: 节点对应的区间长度
        """
        self.sum[i] += v * n
        self.add[i] += v

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
            self.sum[i] = arr[l]
        else:
            mid = (l + r) >> 1
            self.build(arr, l, mid, i << 1)
            self.build(arr, mid + 1, r, i << 1 | 1)
            self.push_up(i)
        self.add[i] = 0

    def add_range(self, jobl, jobr, jobv, l, r, i):
        """
        范围修改 - jobl ~ jobr范围上每个数字增加jobv
        
        Args:
            jobl: 任务区间左端点
            jobr: 任务区间右端点
            jobv: 增加的值
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
                self.add_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.add_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
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
            self.sum[i] = val
        else:
            mid = (l + r) >> 1
            if idx <= mid:
                self.update_single(idx, val, l, mid, i << 1)
            else:
                self.update_single(idx, val, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def add_single(self, idx, val, l, r, i):
        """
        单点增加 - 将索引idx处的值增加val
        
        Args:
            idx: 要增加的索引
            val: 增加的值
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
        """
        if l == r:
            self.sum[i] += val
        else:
            mid = (l + r) >> 1
            if idx <= mid:
                self.add_single(idx, val, l, mid, i << 1)
            else:
                self.add_single(idx, val, mid + 1, r, i << 1 | 1)
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
            ans += self.query(jobl, jobr, l, mid, i << 1)
        if jobr > mid:
            ans += self.query(jobl, jobr, mid + 1, r, i << 1 | 1)
        return ans


def main_p3372():
    """
    洛谷 P3372【模板】线段树 1
    题目链接: https://www.luogu.com.cn/problem/P3372
    
    题目描述:
    如题，已知一个数列，你需要进行下面两种操作：
    1. 将某区间每一个数加上 k
    2. 求出某区间每一个数的和
    
    输入格式:
    第一行包含两个整数 n, m，分别表示该数列数字的个数和操作的总个数。
    第二行包含 n 个用空格分隔的整数，其中第 i 个数字表示数列第 i 项的初始值。
    接下来 m 行每行包含 3 或 4 个整数，表示一个操作:
    - 1 x y k: 将区间 [x, y] 内每个数加上 k
    - 2 x y: 输出区间 [x, y] 内每个数的和
    
    输出格式:
    对于每个 2 操作，输出一行答案。
    
    时间复杂度: O(m log n)
    空间复杂度: O(n)
    """
    import sys
    input = sys.stdin.read if sys.stdin.isatty() else lambda: "5 5\n1 5 4 2 3\n2 2 4\n1 2 3 2\n2 3 4\n1 1 5 1\n2 1 4"
    
    # 为了演示，我们使用示例输入
    # 实际使用时应该用: 
    # lines = input().split('\n')
    lines = [
        "5 5",
        "1 5 4 2 3",
        "2 2 4",
        "1 2 3 2",
        "2 3 4",
        "1 1 5 1",
        "2 1 4"
    ]
    
    # 解析输入
    n, m = map(int, lines[0].split())
    arr = list(map(int, lines[1].split()))
    
    # 初始化线段树
    seg_tree = SegmentTreeSum(n)
    # 建树，注意线段树节点编号从1开始，数组索引从0开始
    seg_tree.build(arr, 0, n-1, 1)
    
    # 处理操作
    results = []
    for i in range(2, 2+m):
        operation = list(map(int, lines[i].split()))
        if operation[0] == 1:
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


def main_hdu1166():
    """
    HDU 1166 敌兵布阵
    题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1166
    
    题目描述:
    C国的死对头A国这段时间正在进行军事演习，所以C国间谍头子Derek和他手下Tidy又开始忙乎了。
    A国在海岸线沿直线布置了N个工兵营地,Derek和Tidy的任务就是要监视这些工兵营地的活动情况。
    由于采取了先进的雷达系统，所以每个工兵营地的人数C国都掌握的一清二楚,
    每个工兵营地的人数都有可能发生变动，可能增加或减少若干人手,
    但这些都逃不过C国的监视。上级下达了一个任务：给定一些部队的调集信息，
    要求你实时地报告某段连续营地的士兵总数。
    
    输入格式:
    第一行一个整数T，表示有T组测试数据。
    每组测试数据第一行一个正整数N（N<=50000）,表示营地个数。
    接下来有N个正整数,第i个正整数ai代表第i个营地开始时有ai个人（1<=ai<=50）。
    接下来每行有一条命令，命令有4种形式：
    (1) Add i j,i和j为正整数,表示第i个营地增加j个人（j不超过30）
    (2) Sub i j ,i和j为正整数,表示第i个营地减少j个人（j不超过30）;
    (3) Query i j ,i和j为正整数,i<=j，表示询问第i到第j个营地的总人数;
    (4) End 表示结束，这条命令在每组数据最后出现;
    
    输出格式:
    对于每组测试数据,输出Case #:，#表示当前是第几组测试数据。
    对于每个Query询问，输出一个整数并回车,表示询问的段中的总人数,这个数保持在int以内。
    
    时间复杂度: O(m log n)
    空间复杂度: O(n)
    """
    # 为了演示，我们使用示例输入
    # 实际使用时应该用: 
    # lines = input().split('\n')
    lines = [
        "1",
        "10",
        "1 2 3 4 5 6 7 8 9 10",
        "Query 1 3",
        "Add 3 6",
        "Query 2 7",
        "Sub 10 2",
        "Add 6 3",
        "Query 3 10",
        "End"
    ]
    
    T = int(lines[0])
    line_idx = 1
    
    for case in range(1, T+1):
        print(f"Case {case}:")
        n = int(lines[line_idx])
        line_idx += 1
        arr = list(map(int, lines[line_idx].split()))
        line_idx += 1
        
        # 初始化线段树
        seg_tree = SegmentTreeSum(n)
        # 建树，注意线段树节点编号从1开始，数组索引从0开始
        seg_tree.build(arr, 0, n-1, 1)
        
        # 处理操作
        while True:
            operation = lines[line_idx].split()
            line_idx += 1
            
            if operation[0] == "End":
                break
            elif operation[0] == "Add":
                i, j = int(operation[1]), int(operation[2])
                seg_tree.add_single(i-1, j, 0, n-1, 1)  # 转换为0索引
            elif operation[0] == "Sub":
                i, j = int(operation[1]), int(operation[2])
                seg_tree.add_single(i-1, -j, 0, n-1, 1)  # 转换为0索引
            elif operation[0] == "Query":
                i, j = int(operation[1]), int(operation[2])
                result = seg_tree.query(i-1, j-1, 0, n-1, 1)  # 转换为0索引
                print(result)


# 测试代码
if __name__ == "__main__":
    # 创建线段树
    seg_tree = SegmentTreeSum(10)
    print("线段树初始化完成")
    
    # 测试数据
    print("测试完成")
    
    # 运行洛谷P3372题目示例
    print("\n运行洛谷P3372示例:")
    main_p3372()
    
    # 运行HDU 1166题目示例
    print("\n运行HDU 1166示例:")
    main_hdu1166()