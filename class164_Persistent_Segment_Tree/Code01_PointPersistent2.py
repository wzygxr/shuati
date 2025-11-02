# -*- coding: utf-8 -*-
"""
单点修改的可持久化线段树模版题1，python版

题目来源: 洛谷 P3919 【模板】可持久化线段树1（可持久化数组）
题目链接: https://www.luogu.com.cn/problem/P3919

题目描述:
给定一个长度为n的数组arr，下标1~n，原始数组认为是0号版本
一共有m条操作，每条操作是如下两种类型中的一种
v 1 x y : 基于v号版本的数组，把x位置的值设置成y，生成新版本的数组
v 2 x   : 基于v号版本的数组，打印x位置的值，生成新版本的数组和v版本一致
每条操作后得到的新版本数组，版本编号为操作的计数

解题思路:
使用可持久化线段树（主席树）解决可持久化数组问题。
1. 对于每次修改操作，只创建被修改路径上的新节点，共享未修改的部分
2. 对于查询操作，直接在对应版本的线段树上查询

时间复杂度: O((n + m) log n)
空间复杂度: O(n log n)

1 <= n, m <= 10^6

示例:
输入:
5 10
59 64 65 97 51
0 1 1 10
0 2 2 20
0 3 3 30
0 4 4 40
0 5 5 50
1 2 1 100
1 2 2 200
1 2 3 300
1 2 4 400
1 2 5 500

输出:
10
20
30
40
50
100
200
300
400
500
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
        self.left = [0] * (n * 23)
        self.right = [0] * (n * 23)
        self.value = [0] * (n * 23)
        # 线段树节点计数器
        self.cnt = 0
    
    def build(self, arr, l, r):
        """
        构建线段树
        :param arr: 原始数组
        :param l: 区间左端点
        :param r: 区间右端点
        :return: 根节点编号
        """
        rt = self.cnt + 1
        self.cnt += 1
        if l == r:
            self.value[rt] = arr[l]
        else:
            mid = (l + r) // 2
            self.left[rt] = self.build(arr, l, mid)
            self.right[rt] = self.build(arr, mid + 1, r)
        return rt
    
    def update(self, jobi, jobv, l, r, i):
        """
        更新线段树中的一个位置
        :param jobi: 要修改的位置
        :param jobv: 要设置的值
        :param l: 区间左端点
        :param r: 区间右端点
        :param i: 当前节点编号
        :return: 新节点编号
        """
        rt = self.cnt + 1
        self.cnt += 1
        self.left[rt] = self.left[i]
        self.right[rt] = self.right[i]
        self.value[rt] = self.value[i]
        if l == r:
            self.value[rt] = jobv
        else:
            mid = (l + r) // 2
            if jobi <= mid:
                self.left[rt] = self.update(jobi, jobv, l, mid, self.left[rt])
            else:
                self.right[rt] = self.update(jobi, jobv, mid + 1, r, self.right[rt])
        return rt
    
    def query(self, jobi, l, r, i):
        """
        查询线段树中某个位置的值
        :param jobi: 要查询的位置
        :param l: 区间左端点
        :param r: 区间右端点
        :param i: 当前节点编号
        :return: 位置jobi的值
        """
        if l == r:
            return self.value[i]
        mid = (l + r) // 2
        if jobi <= mid:
            return self.query(jobi, l, mid, self.left[i])
        else:
            return self.query(jobi, mid + 1, r, self.right[i])


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
    for i in range(1, m + 1):
        version = int(data[idx])
        op = int(data[idx + 1])
        x = int(data[idx + 2])
        if op == 1:
            y = int(data[idx + 3])
            pst.root[i] = pst.update(x, y, 1, n, pst.root[version])
            idx += 4
        else:
            pst.root[i] = pst.root[version]
            results.append(str(pst.query(x, 1, n, pst.root[i])))
            idx += 3
    
    # 输出结果
    if results:
        print('\n'.join(results))


if __name__ == "__main__":
    main()