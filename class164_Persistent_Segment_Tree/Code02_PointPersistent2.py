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
        :param n: 数组大小
        """
        self.n = n
        # 原始数组
        self.arr = [0] * (n + 1)
        # 收集权值排序并且去重做离散化
        self.sorted_vals = [0] * (n + 1)
        # 可持久化线段树需要
        # root[i] : 插入arr[i]之后形成新版本的线段树，记录头节点编号
        # 0号版本的线段树代表一个数字也没有时，每种名次的数字出现的次数
        # i号版本的线段树代表arr[1..i]范围内，每种名次的数字出现的次数
        self.root = [0] * (n + 1)
        
        # 线段树节点信息
        self.left = [0] * (n * 22)
        self.right = [0] * (n * 22)
        # 排名范围内收集了多少个数字
        self.size = [0] * (n * 22)
        
        # 线段树节点计数器
        self.cnt = 0
    
    def kth(self, num, s):
        """
        返回num在所有值中排名多少
        :param num: 要查询排名的数值
        :param s: 离散化后的数组大小
        :return: num的排名
        """
        left, right, ans = 1, s, 0
        while left <= right:
            mid = (left + right) // 2
            if self.sorted_vals[mid] <= num:
                ans = mid
                left = mid + 1
            else:
                right = mid - 1
        return ans
    
    def build(self, l, r):
        """
        排名范围l~r，建立线段树，返回头节点编号
        :param l: 区间左端点
        :param r: 区间右端点
        :return: 头节点编号
        """
        self.cnt += 1
        rt = self.cnt
        self.size[rt] = 0
        if l < r:
            mid = (l + r) // 2
            self.left[rt] = self.build(l, mid)
            self.right[rt] = self.build(mid + 1, r)
        return rt
    
    def insert(self, jobi, l, r, i):
        """
        排名范围l~r，信息在i号节点，增加一个排名为jobi的数字
        返回新的头节点编号
        :param jobi: 要插入的数字的排名
        :param l: 区间左端点
        :param r: 区间右端点
        :param i: 当前节点编号
        :return: 新节点编号
        """
        self.cnt += 1
        rt = self.cnt
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
        排名范围l~r，老版本信息在u号节点，新版本信息在v号节点
        返回，第jobk小的数字，排名多少
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
    
    def prepare(self):
        """权值做离散化并且去重 + 生成各版本的线段树"""
        self.cnt = 0
        for i in range(1, self.n + 1):
            self.sorted_vals[i] = self.arr[i]
        
        # 排序并去重
        self.sorted_vals[1:self.n+1] = sorted(self.sorted_vals[1:self.n+1])
        s = 1
        for i in range(2, self.n + 1):
            if self.sorted_vals[s] != self.sorted_vals[i]:
                s += 1
                self.sorted_vals[s] = self.sorted_vals[i]
        
        self.root[0] = self.build(1, s)
        for i in range(1, self.n + 1):
            x = self.kth(self.arr[i], s)
            self.root[i] = self.insert(x, 1, s, self.root[i - 1])
        
        return s


def main():
    """主函数"""
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    m = int(data[1])
    
    # 初始化可持久化线段树
    pst = PersistentSegmentTree(n)
    
    # 读取原始数组
    idx = 2
    for i in range(1, n + 1):
        pst.arr[i] = int(data[idx])
        idx += 1
    
    # 离散化处理并构建主席树
    s = pst.prepare()
    
    # 处理查询
    results = []
    for i in range(m):
        l = int(data[idx])
        r = int(data[idx + 1])
        k = int(data[idx + 2])
        rank = pst.query(k, 1, s, pst.root[l - 1], pst.root[r])
        results.append(str(pst.sorted_vals[rank]))
        idx += 3
    
    # 输出结果
    print('\n'.join(results))


if __name__ == "__main__":
    main()