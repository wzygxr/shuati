# Problem: SPOJ DQUERY - D-query
# Link: https://www.spoj.com/problems/DQUERY/
# Description: 给定一个包含n个数字的序列，每次查询区间[l,r]中不同数字的个数
# Solution: 使用可持久化线段树(主席树)解决区间不同元素个数问题
# Time Complexity: O(nlogn) for preprocessing, O(logn) for each query
# Space Complexity: O(nlogn)

import sys

class Node:
    def __init__(self):
        self.l = 0
        self.r = 0
        self.sum = 0

class PersistentSegmentTree:
    def __init__(self, maxn=30005):
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
    
    def insert(self, pre, l, r, pos, val):
        now = self.create_node()
        self.T[now].sum = self.T[pre].sum + val
        
        if l == r:
            return now
        
        mid = (l + r) >> 1
        if pos <= mid:
            self.T[now].l = self.insert(self.T[pre].l, l, mid, pos, val)
            self.T[now].r = self.T[pre].r
        else:
            self.T[now].l = self.T[pre].l
            self.T[now].r = self.insert(self.T[pre].r, mid + 1, r, pos, val)
        return now
    
    def query(self, u, v, l, r, L, R):
        if L <= l and r <= R:
            return self.T[v].sum - self.T[u].sum
        if L > r or R < l:
            return 0
        
        mid = (l + r) >> 1
        return self.query(self.T[u].l, self.T[v].l, l, mid, L, R) + \
               self.query(self.T[u].r, self.T[v].r, mid + 1, r, L, R)

def main():
    n = int(sys.stdin.readline())
    
    line = sys.stdin.readline().split()
    a = [0] * (n + 1)
    prev = [0] * (n + 1)
    last = [0] * 1000005
    
    for i in range(1, n + 1):
        a[i] = int(line[i - 1])
    
    # 预处理prev数组
    for i in range(1, n + 1):
        prev[i] = last[a[i]]
        last[a[i]] = i
    
    # 构建主席树
    pst = PersistentSegmentTree(n + 1)
    pst.root[0] = pst.create_node()
    
    for i in range(1, n + 1):
        if prev[i] == 0:
            # 第一次出现，在位置i加1
            pst.root[i] = pst.insert(pst.root[i - 1], 1, n, i, 1)
        else:
            # 不是第一次出现，先在prev[i]位置减1，再在i位置加1
            temp = pst.insert(pst.root[i - 1], 1, n, prev[i], -1)
            pst.root[i] = pst.insert(temp, 1, n, i, 1)
    
    q = int(sys.stdin.readline())
    for _ in range(q):
        line = sys.stdin.readline().split()
        l, r = int(line[0]), int(line[1])
        print(pst.query(pst.root[l - 1], pst.root[r], 1, n, l, r))

if __name__ == "__main__":
    main()