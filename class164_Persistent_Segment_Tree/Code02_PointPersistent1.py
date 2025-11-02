# -*- coding: utf-8 -*-
"""
单点修改的可持久化线段树模版题2，python版

题目来源: 洛谷 P3834 【模板】可持久化线段树2
题目链接: https://www.luogu.com.cn/problem/P3834

题目描述:
给定一个长度为n的数组arr，下标1~n，一共有m条查询
每条查询 l r k : 打印arr[l..r]中第k小的数字

解题思路:
使用可持久化线段树（主席树）解决静态区间第K小问题。
1. 对数组元素进行离散化处理，将大范围的值映射到小范围的排名
2. 对于每个位置i，建立一个线段树版本，维护前i个元素中每个排名的出现次数
3. 利用前缀和的思想，通过两个版本的线段树相减得到区间信息
4. 在线段树上二分查找第K小的元素

时间复杂度: O((n + m) log n)
空间复杂度: O(n log n)

1 <= n、m <= 2 * 10^5
0 <= arr[i] <= 10^9

示例:
输入:
5 5
25957 6405 15770 26287 6556
2 2 1
3 4 1
4 5 1
1 2 2
4 4 1

输出:
6405
15770
26287
25957
26287
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
        self.left = [0] * (n * 22)
        self.right = [0] * (n * 22)
        self.size = [0] * (n * 22)
        # 线段树节点计数器
        self.cnt = 0
    
    def build(self, l, r):
        """
        构建线段树
        :param l: 区间左端点
        :param r: 区间右端点
        :return: 根节点编号
        """
        rt = self.cnt + 1
        self.cnt += 1
        self.size[rt] = 0
        if l < r:
            mid = (l + r) // 2
            self.left[rt] = self.build(l, mid)
            self.right[rt] = self.build(mid + 1, r)
        return rt
    
    def insert(self, jobi, l, r, i):
        """
        插入一个排名为jobi的数字
        :param jobi: 要插入的数字的排名
        :param l: 区间左端点
        :param r: 区间右端点
        :param i: 当前节点编号
        :return: 新节点编号
        """
        rt = self.cnt + 1
        self.cnt += 1
        self.left[rt] = self.left[i]
        self.right[rt] = self.right[i]
        self.size[rt] = self.size[i] + 1
        if l < r:
            mid = (l + r) // 2
            if jobi <= mid:
                self.left[rt] = self.insert(jobi, l, mid, self.left[rt])
            else:
                self.right[rt] = self.insert(jobi, mid + 1, r, self.right[rt])
        return rt
    
    def query(self, jobk, l, r, u, v):
        """
        查询第jobk小的数字的排名
        :param jobk: 要查询的第几小
        :param l: 区间左端点
        :param r: 区间右端点
        :param u: 老版本节点编号
        :param v: 新版本节点编号
        :return: 第jobk小的数字的排名
        """
        if l == r:
            return l
        lsize = self.size[self.left[v]] - self.size[self.left[u]]
        mid = (l + r) // 2
        if lsize >= jobk:
            return self.query(jobk, l, mid, self.left[u], self.left[v])
        else:
            return self.query(jobk - lsize, mid + 1, r, self.right[u], self.right[v])


def kth(num, sorted_arr, s):
    """
    返回num在所有值中排名多少
    :param num: 要查询排名的数值
    :param sorted_arr: 排序后的数组
    :param s: 数组长度
    :return: num的排名
    """
    left, right, ans = 1, s, 0
    while left <= right:
        mid = (left + right) // 2
        if sorted_arr[mid] <= num:
            ans = mid
            left = mid + 1
        else:
            right = mid - 1
    return ans


def prepare(arr, n):
    """
    权值做离散化并且去重 + 生成各版本的线段树
    :param arr: 原始数组
    :param n: 数组长度
    :return: 离散化后的数组、数组长度、各版本线段树的根节点
    """
    # 收集权值排序并且去重做离散化
    sorted_arr = [0] * (n + 1)
    for i in range(1, n + 1):
        sorted_arr[i] = arr[i]
    
    # 排序并去重
    sorted_arr[1:n+1] = sorted(sorted_arr[1:n+1])
    s = 1
    for i in range(2, n + 1):
        if sorted_arr[s] != sorted_arr[i]:
            s += 1
            sorted_arr[s] = sorted_arr[i]
    
    # 构建可持久化线段树
    pst = PersistentSegmentTree(n)
    pst.root[0] = pst.build(1, s)
    
    # 生成各版本的线段树
    for i in range(1, n + 1):
        x = kth(arr[i], sorted_arr, s)
        pst.root[i] = pst.insert(x, 1, s, pst.root[i - 1])
    
    return sorted_arr, s, pst


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
    
    # 准备工作
    sorted_arr, s, pst = prepare(arr, n)
    
    # 处理查询
    idx = 2 + n
    results = []
    for i in range(m):
        l = int(data[idx])
        r = int(data[idx + 1])
        k = int(data[idx + 2])
        rank = pst.query(k, 1, s, pst.root[l - 1], pst.root[r])
        results.append(str(sorted_arr[rank]))
        idx += 3
    
    # 输出结果
    print('\n'.join(results))


if __name__ == "__main__":
    main()