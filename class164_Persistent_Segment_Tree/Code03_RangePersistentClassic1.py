# -*- coding: utf-8 -*-
"""
范围修改的可持久化线段树，经典的方式，python版

题目来源: SPOJ TTM - To the moon
题目链接: https://www.spoj.com/problems/TTM/

题目描述:
给定一个长度为n的数组arr，下标1~n，时间戳t=0，arr认为是0版本的数组
一共有m条操作，每条操作为如下四种类型中的一种
C x y z : 当前时间戳t版本的数组，[x..y]范围每个数字增加z，得到t+1版本数组，并且t++
Q x y   : 当前时间戳t版本的数组，打印[x..y]范围累加和
H x y z : z版本的数组，打印[x..y]范围的累加和
B x     : 当前时间戳t设置成x

解题思路:
使用可持久化线段树解决带历史版本的区间修改问题。
1. 对于每次修改操作，只创建被修改路径上的新节点，共享未修改的部分
2. 使用懒惰标记技术处理区间修改
3. 通过clone函数实现节点的复制，确保历史版本的完整性
4. 在需要下传懒惰标记时，先复制子节点再进行操作

时间复杂度: O((n + m) log n)
空间复杂度: O(n log n)

1 <= n、m <= 10^5
-10^9 <= arr[i] <= +10^9

示例:
输入:
5 10
5 6 7 8 9
Q 1 5
C 2 4 10
Q 1 5
H 1 5 0
B 3
Q 1 5
C 1 5 20
Q 1 5
H 1 5 3
Q 1 5

输出:
35
55
35
55
75
55
"""

class PersistentSegmentTree:
    """可持久化线段树实现"""
    
    def __init__(self, n):
        """
        初始化可持久化线段树
        :param n: 数组长度
        """
        self.n = n
        # 每个版本线段树的根节点
        self.root = [0] * (n + 1)
        # 线段树节点信息
        self.left = [0] * (n * 70)
        self.right = [0] * (n * 70)
        self.sum = [0] * (n * 70)
        self.add = [0] * (n * 70)
        # 线段树节点计数器
        self.cnt = 0
        self.t = 0
    
    def clone(self, i):
        """
        克隆节点
        :param i: 要克隆的节点编号
        :return: 新节点编号
        """
        self.cnt += 1
        rt = self.cnt
        self.left[rt] = self.left[i]
        self.right[rt] = self.right[i]
        self.sum[rt] = self.sum[i]
        self.add[rt] = self.add[i]
        return rt
    
    def up(self, i):
        """
        更新节点信息
        :param i: 节点编号
        """
        self.sum[i] = self.sum[self.left[i]] + self.sum[self.right[i]]
    
    def lazy(self, i, v, n):
        """
        懒更新操作
        :param i: 节点编号
        :param v: 增加的值
        :param n: 区间长度
        """
        self.sum[i] += v * n
        self.add[i] += v
    
    def down(self, i, ln, rn):
        """
        下传懒更新标记
        :param i: 节点编号
        :param ln: 左子区间长度
        :param rn: 右子区间长度
        """
        if self.add[i] != 0:
            self.left[i] = self.clone(self.left[i])
            self.right[i] = self.clone(self.right[i])
            self.lazy(self.left[i], self.add[i], ln)
            self.lazy(self.right[i], self.add[i], rn)
            self.add[i] = 0
    
    def build(self, arr, l, r):
        """
        建立线段树
        :param arr: 原始数组
        :param l: 区间左端点
        :param r: 区间右端点
        :return: 根节点编号
        """
        self.cnt += 1
        rt = self.cnt
        self.add[rt] = 0
        if l == r:
            self.sum[rt] = arr[l]
        else:
            mid = (l + r) // 2
            self.left[rt] = self.build(arr, l, mid)
            self.right[rt] = self.build(arr, mid + 1, r)
            self.up(rt)
        return rt
    
    def add_range(self, jobl, jobr, jobv, l, r, i):
        """
        区间增加操作
        :param jobl: 操作区间左端点
        :param jobr: 操作区间右端点
        :param jobv: 增加的值
        :param l: 当前区间左端点
        :param r: 当前区间右端点
        :param i: 当前节点编号
        :return: 新节点编号
        """
        rt = self.clone(i)
        if jobl <= l and r <= jobr:
            self.lazy(rt, jobv, r - l + 1)
        else:
            mid = (l + r) // 2
            self.down(rt, mid - l + 1, r - mid)
            if jobl <= mid:
                self.left[rt] = self.add_range(jobl, jobr, jobv, l, mid, self.left[rt])
            if jobr > mid:
                self.right[rt] = self.add_range(jobl, jobr, jobv, mid + 1, r, self.right[rt])
            self.up(rt)
        return rt
    
    def query(self, jobl, jobr, l, r, i):
        """
        区间查询操作
        :param jobl: 查询区间左端点
        :param jobr: 查询区间右端点
        :param l: 当前区间左端点
        :param r: 当前区间右端点
        :param i: 当前节点编号
        :return: 区间和
        """
        if jobl <= l and r <= jobr:
            return self.sum[i]
        mid = (l + r) // 2
        self.down(i, mid - l + 1, r - mid)
        ans = 0
        if jobl <= mid:
            ans += self.query(jobl, jobr, l, mid, self.left[i])
        if jobr > mid:
            ans += self.query(jobl, jobr, mid + 1, r, self.right[i])
        return ans


def main():
    """主函数"""
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    m = int(data[1])
    
    # 读取原始数组
    arr = [0] * (n + 1)
    for i in range(n):
        arr[i + 1] = int(data[2 + i])
    
    # 构建可持久化线段树
    pst = PersistentSegmentTree(n)
    pst.root[0] = pst.build(arr, 1, n)
    
    # 处理操作
    idx = 2 + n
    results = []
    for i in range(m):
        op = data[idx]
        if op == "C":
            x = int(data[idx + 1])
            y = int(data[idx + 2])
            z = int(data[idx + 3])
            pst.root[pst.t + 1] = pst.add_range(x, y, z, 1, n, pst.root[pst.t])
            pst.t += 1
            idx += 4
        elif op == "Q":
            x = int(data[idx + 1])
            y = int(data[idx + 2])
            result = pst.query(x, y, 1, n, pst.root[pst.t])
            results.append(str(result))
            idx += 3
        elif op == "H":
            x = int(data[idx + 1])
            y = int(data[idx + 2])
            z = int(data[idx + 3])
            result = pst.query(x, y, 1, n, pst.root[z])
            results.append(str(result))
            idx += 4
        else:  # B
            x = int(data[idx + 1])
            pst.t = x
            idx += 2
    
    # 输出结果
    if results:
        print('\n'.join(results))


if __name__ == "__main__":
    main()