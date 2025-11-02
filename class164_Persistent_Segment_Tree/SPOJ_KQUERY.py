# -*- coding: utf-8 -*-
"""
SPOJ KQUERY - K-query

题目描述:
给定一个长度为N的序列，进行Q次查询，每次查询区间[l,r]中大于K的数的个数。

解题思路:
使用可持久化线段树（主席树）结合离线处理解决区间大于K的数的个数问题。
1. 将所有查询按K值从大到小排序
2. 将所有元素按值从大到小排序
3. 按顺序处理查询，对于每个查询，将所有大于K的元素插入到主席树中
4. 查询区间[l,r]中元素的个数

时间复杂度: O((n + q) log n)
空间复杂度: O(n log n)

示例:
输入:
5
5 1 2 3 4
3
2 4 1
4 4 4
1 5 2

输出:
2
0
3
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
        self.cnt += 1
        rt = self.cnt
        self.sum[rt] = 0
        if l < r:
            mid = (l + r) // 2
            self.left[rt] = self.build(l, mid)
            self.right[rt] = self.build(mid + 1, r)
        return rt
    
    def insert(self, pos, l, r, pre):
        """
        在线段树中插入一个值
        :param pos: 要插入的位置
        :param l: 区间左端点
        :param r: 区间右端点
        :param pre: 前一个版本的节点编号
        :return: 新节点编号
        """
        self.cnt += 1
        rt = self.cnt
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
    
    def query(self, L, R, l, r, rt):
        """
        查询区间和
        :param L: 查询区间左端点
        :param R: 查询区间右端点
        :param l: 当前区间左端点
        :param r: 当前区间右端点
        :param rt: 当前节点编号
        :return: 区间和
        """
        if L <= l and r <= R:
            return self.sum[rt]
        
        mid = (l + r) // 2
        ans = 0
        if L <= mid:
            ans += self.query(L, R, l, mid, self.left[rt])
        if R > mid:
            ans += self.query(L, R, mid + 1, r, self.right[rt])
        return ans


def main():
    """主函数"""
    import sys
    import bisect
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    
    # 读取原始数组
    arr = [0] * (n + 1)
    elements = []
    idx = 1
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        elements.append((arr[i], i))  # (value, index)
        idx += 1
    
    q = int(data[idx])
    idx += 1
    
    queries = []
    answers = [0] * q
    
    # 读取查询
    for i in range(q):
        l = int(data[idx])
        r = int(data[idx + 1])
        k = int(data[idx + 2])
        queries.append((k, l, r, i))  # (k, l, r, id)
        idx += 3
    
    # 排序
    # 将elements按值从大到小排序
    elements.sort(key=lambda x: x[0], reverse=True)
    # 将queries按k值从大到小排序
    queries.sort(key=lambda x: x[0], reverse=True)
    
    # 初始化可持久化线段树
    pst = PersistentSegmentTree(n)
    
    # 构建主席树
    pst.root[0] = pst.build(1, n)
    
    # 离线处理查询
    elem_idx = 0
    for k, l, r, query_id in queries:
        # 将所有大于k的元素插入到主席树中
        while elem_idx < n and elements[elem_idx][0] > k:
            pos = elements[elem_idx][1]  # 元素的位置
            pst.root[elem_idx + 1] = pst.insert(pos, 1, n, pst.root[elem_idx])
            elem_idx += 1
        # 查询区间[l, r]中元素的个数
        answers[query_id] = pst.query(l, r, 1, n, pst.root[elem_idx])
    
    # 输出结果
    for ans in answers:
        print(ans)


if __name__ == "__main__":
    main()