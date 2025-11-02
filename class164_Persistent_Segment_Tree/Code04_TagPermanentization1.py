# -*- coding: utf-8 -*-
"""
标记永久化，范围增加 + 查询累加和，python版

题目描述:
给定一个长度为n的数组arr，下标1~n，一共有m条操作，操作类型如下
1 x y k : 将区间[x, y]每个数加上k
2 x y   : 打印区间[x, y]的累加和
这就是普通线段树，请用标记永久化的方式实现

解题思路:
使用标记永久化的线段树解决区间修改和区间查询问题。
1. 使用标记永久化技术减少空间占用
2. 对于区间修改操作，在路径上的每个节点都记录标记信息
3. 对于区间查询操作，累积路径上的标记信息

时间复杂度: O(m log n)
空间复杂度: O(n)

测试链接: https://www.luogu.com.cn/problem/P3372
"""

class TagPermanentization:
    """标记永久化线段树实现"""
    
    def __init__(self, n):
        """
        初始化标记永久化线段树
        :param n: 数组长度
        """
        self.n = n
        # 原始数组
        self.arr = [0] * (n + 1)
        # 不是真实累加和，而是之前的任务中
        # 不考虑被上方范围截住的任务，只考虑来到当前范围 或者 往下走的任务
        # 累加和变成了什么
        self.sum = [0] * ((n + 1) * 4)
        # 不再是懒更新信息，变成标记信息
        self.add_tag = [0] * ((n + 1) * 4)
    
    def build(self, l, r, i):
        """
        构建线段树
        :param l: 区间左端点
        :param r: 区间右端点
        :param i: 当前节点编号
        """
        if l == r:
            self.sum[i] = self.arr[l]
        else:
            mid = (l + r) // 2
            self.build(l, mid, i * 2)
            self.build(mid + 1, r, i * 2 + 1)
            self.sum[i] = self.sum[i * 2] + self.sum[i * 2 + 1]
        self.add_tag[i] = 0
    
    def add(self, jobl, jobr, jobv, l, r, i):
        """
        区间增加操作
        :param jobl: 操作区间左端点
        :param jobr: 操作区间右端点
        :param jobv: 增加的值
        :param l: 当前区间左端点
        :param r: 当前区间右端点
        :param i: 当前节点编号
        """
        a = max(jobl, l)
        b = min(jobr, r)
        self.sum[i] += jobv * (b - a + 1)
        if jobl <= l and r <= jobr:
            self.add_tag[i] += jobv
        else:
            mid = (l + r) // 2
            if jobl <= mid:
                self.add(jobl, jobr, jobv, l, mid, i * 2)
            if jobr > mid:
                self.add(jobl, jobr, jobv, mid + 1, r, i * 2 + 1)
    
    def query(self, jobl, jobr, add_history, l, r, i):
        """
        区间查询操作
        :param jobl: 查询区间左端点
        :param jobr: 查询区间右端点
        :param add_history: 累积的标记信息
        :param l: 当前区间左端点
        :param r: 当前区间右端点
        :param i: 当前节点编号
        :return: 区间和
        """
        if jobl <= l and r <= jobr:
            return self.sum[i] + add_history * (r - l + 1)
        mid = (l + r) // 2
        ans = 0
        if jobl <= mid:
            ans += self.query(jobl, jobr, add_history + self.add_tag[i], l, mid, i * 2)
        if jobr > mid:
            ans += self.query(jobl, jobr, add_history + self.add_tag[i], mid + 1, r, i * 2 + 1)
        return ans


def main():
    """主函数"""
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    m = int(data[1])
    
    # 初始化标记永久化线段树
    tp = TagPermanentization(n)
    
    # 读取原始数组
    idx = 2
    for i in range(1, n + 1):
        tp.arr[i] = int(data[idx])
        idx += 1
    
    # 构建线段树
    tp.build(1, n, 1)
    
    results = []
    # 处理操作
    for i in range(m):
        op = int(data[idx])
        if op == 1:
            jobl = int(data[idx + 1])
            jobr = int(data[idx + 2])
            jobv = int(data[idx + 3])
            tp.add(jobl, jobr, jobv, 1, n, 1)
            idx += 4
        else:
            jobl = int(data[idx + 1])
            jobr = int(data[idx + 2])
            result = tp.query(jobl, jobr, 0, 1, n, 1)
            results.append(str(result))
            idx += 3
    
    # 输出结果
    if results:
        print('\n'.join(results))


if __name__ == "__main__":
    main()