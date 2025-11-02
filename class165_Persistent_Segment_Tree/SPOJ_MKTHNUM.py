# -*- coding: utf-8 -*-
"""
SPOJ MKTHNUM - K-th Number

题目描述:
给定一个长度为N的序列，进行M次查询，每次查询区间[l,r]中第K小的数。

解题思路:
使用可持久化线段树（主席树）解决静态区间第K小问题。
1. 对所有数值进行离散化处理
2. 对每个位置建立权值线段树，第i棵线段树表示前i个数的信息
3. 利用前缀和思想，通过第r棵和第l-1棵线段树的差得到区间[l,r]的信息
4. 在线段树上二分查找第k小的数

时间复杂度: O(n log n + m log n)
空间复杂度: O(n log n)

示例:
输入:
7 3
1 5 2 6 3 7 4
2 5 3
4 7 1
1 7 3

输出:
5
6
3
"""

import bisect

class PersistentSegmentTree:
    """可持久化线段树（主席树）实现"""
    
    def __init__(self, n):
        """
        初始化主席树
        :param n: 离散化后的值域大小
        """
        self.n = n
        # 每个版本线段树的根节点
        self.root = [0] * (n + 1)
        # 线段树节点信息
        self.left = [0] * (n * 20)
        self.right = [0] * (n * 20)
        self.sum = [0] * (n * 20)
        # 线段树节点计数器
        self.cnt = 0
    
    def build(self, l, r):
        """
        构建空线段树
        :param l: 区间左端点
        :param r: 区间右端点
        :return: 根节点编号
        """
        rt = self.cnt + 1
        self.cnt += 1
        self.sum[rt] = 0
        if l < r:
            mid = (l + r) // 2
            self.left[rt] = self.build(l, mid)
            self.right[rt] = self.build(mid + 1, r)
        return rt
    
    def insert(self, pos, l, r, pre):
        """
        在线段树中插入一个值
        :param pos: 要插入的值（离散化后的坐标）
        :param l: 区间左端点
        :param r: 区间右端点
        :param pre: 前一个版本的节点编号
        :return: 新节点编号
        """
        rt = self.cnt + 1
        self.cnt += 1
        self.left[rt] = self.left[pre]
        self.right[rt] = self.right[pre]
        self.sum[rt] = self.sum[pre] + 1
        
        if l < r:
            mid = (l + r) // 2
            if pos <= mid:
                self.left[rt] = self.insert(pos, l, mid, self.left[rt])
            else:
                self.right[rt] = self.insert(pos, mid + 1, r, self.right[rt])
        return rt
    
    def query(self, k, l, r, u, v):
        """
        查询区间第k小的数
        :param k: 第k小
        :param l: 区间左端点
        :param r: 区间右端点
        :param u: 前一个版本的根节点
        :param v: 当前版本的根节点
        :return: 第k小的数在离散化数组中的位置
        """
        if l >= r:
            return l
        mid = (l + r) // 2
        # 计算左子树中数的个数
        x = self.sum[self.left[v]] - self.sum[self.left[u]]
        if x >= k:
            # 第k小在左子树中
            return self.query(k, l, mid, self.left[u], self.left[v])
        else:
            # 第k小在右子树中
            return self.query(k - x, mid + 1, r, self.right[u], self.right[v])


def main():
    # 读取输入
    n, m = map(int, input().split())
    
    # 原始数组和离散化数组
    arr = [0] * (n + 1)
    sorted_arr = [0] * (n + 1)
    
    # 读取原始数组
    values = list(map(int, input().split()))
    for i in range(1, n + 1):
        arr[i] = values[i - 1]
        sorted_arr[i] = arr[i]
    
    # 离散化处理
    sorted_arr = sorted_arr[1:n + 1]
    sorted_arr.sort()
    
    # 去重
    unique_sorted = [sorted_arr[0]]
    for i in range(1, n):
        if sorted_arr[i] != sorted_arr[i - 1]:
            unique_sorted.append(sorted_arr[i])
    
    size = len(unique_sorted)
    
    # 构建主席树
    pst = PersistentSegmentTree(size)
    pst.root[0] = pst.build(1, size)
    
    # 为每个位置建立线段树
    for i in range(1, n + 1):
        # 查找arr[i]在离散化数组中的位置
        pos = bisect.bisect_left(unique_sorted, arr[i]) + 1
        pst.root[i] = pst.insert(pos, 1, size, pst.root[i - 1])
    
    # 处理查询
    for _ in range(m):
        l, r, k = map(int, input().split())
        pos = pst.query(k, 1, size, pst.root[l - 1], pst.root[r])
        print(unique_sorted[pos - 1])


if __name__ == "__main__":
    main()