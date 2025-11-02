# Problem: 洛谷P3834 - 【模板】可持久化线段树2 (静态区间第k小)
# Link: https://www.luogu.com.cn/problem/P3834
# Description: 给定一个包含n个数字的序列，每次查询区间[l,r]中第k小的数
# Solution: 使用可持久化线段树(主席树)解决静态区间第k小问题
# Time Complexity: O(nlogn) for preprocessing, O(logn) for each query
# Space Complexity: O(nlogn)

import sys
from bisect import bisect_left

class Node:
    def __init__(self):
        self.l = 0
        self.r = 0
        self.sum = 0

class PersistentSegmentTree:
    def __init__(self, maxn=200005):
        self.MAXN = maxn
        self.T = [Node() for _ in range(40 * maxn)]
        self.root = [0] * maxn
        self.cnt = 0
    
    def create_node(self, l=0, r=0, sum=0):
        self.cnt += 1
        self.T[self.cnt].l = l
        self.T[self.cnt].r = r
        self.T[self.cnt].sum = sum
        return self.cnt
    
    def insert(self, pre, l, r, val):
        now = self.create_node()
        self.T[now].sum = self.T[pre].sum + 1
        
        if l == r:
            return now
        
        mid = (l + r) >> 1
        if val <= mid:
            self.T[now].l = self.insert(self.T[pre].l, l, mid, val)
            self.T[now].r = self.T[pre].r
        else:
            self.T[now].l = self.T[pre].l
            self.T[now].r = self.insert(self.T[pre].r, mid + 1, r, val)
        return now
    
    def query(self, u, v, l, r, k):
        if l == r:
            return l
        
        mid = (l + r) >> 1
        x = self.T[self.T[v].l].sum - self.T[self.T[u].l].sum
        
        if k <= x:
            return self.query(self.T[u].l, self.T[v].l, l, mid, k)
        else:
            return self.query(self.T[u].r, self.T[v].r, mid + 1, r, k - x)

def main():
    line = sys.stdin.readline().split()
    n, m = int(line[0]), int(line[1])
    
    line = sys.stdin.readline().split()
    a = [0] * (n + 1)
    b = [0] * (n + 1)
    
    for i in range(1, n + 1):
        a[i] = int(line[i - 1])
        b[i] = a[i]
    
    # 离散化
    b = b[1:]  # 去掉索引0
    b.sort()
    # 去重
    unique_b = []
    for x in b:
        if not unique_b or unique_b[-1] != x:
            unique_b.append(x)
    b = [0] + unique_b  # 加回索引0
    sz = len(b) - 1
    
    # 获取值的离散化ID
    def get_id(x):
        return bisect_left(b, x, 1, len(b))
    
    # 构建主席树
    pst = PersistentSegmentTree(n + 1)
    pst.root[0] = pst.create_node()
    
    for i in range(1, n + 1):
        pst.root[i] = pst.insert(pst.root[i - 1], 1, sz, get_id(a[i]))
    
    # 处理查询
    for _ in range(m):
        line = sys.stdin.readline().split()
        l, r, k = int(line[0]), int(line[1]), int(line[2])
        id = pst.query(pst.root[l - 1], pst.root[r], 1, sz, k)
        print(b[id])

if __name__ == "__main__":
    main()