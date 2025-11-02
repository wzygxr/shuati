# Problem: 洛谷P3919 - 【模板】可持久化线段树1（可持久化数组）
# Link: https://www.luogu.com.cn/problem/P3919
# Description: 维护一个长度为N的数组，支持在某个历史版本上修改某一个位置上的值，以及访问某个历史版本上的某一位置的值
# Solution: 使用可持久化线段树实现可持久化数组
# Time Complexity: O(logn) for each operation
# Space Complexity: O(n + mlogn) where m is the number of operations

import sys

class Node:
    def __init__(self):
        self.l = 0
        self.r = 0
        self.val = 0

class PersistentSegmentTree:
    def __init__(self, maxn=1000005):
        self.MAXN = maxn
        self.T = [Node() for _ in range(40 * maxn)]
        self.root = [0] * maxn
        self.cnt = 0
    
    def create_node(self, l=0, r=0, val=0):
        self.cnt += 1
        self.T[self.cnt].l = l
        self.T[self.cnt].r = r
        self.T[self.cnt].val = val
        return self.cnt
    
    def build(self, a, l, r):
        now = self.create_node()
        
        if l == r:
            self.T[now].val = a[l]
            return now
        
        mid = (l + r) >> 1
        self.T[now].l = self.build(a, l, mid)
        self.T[now].r = self.build(a, mid + 1, r)
        return now
    
    def update(self, pre, l, r, p, x):
        now = self.create_node()
        
        if l == r:
            self.T[now].val = x
            return now
        
        mid = (l + r) >> 1
        if p <= mid:
            self.T[now].l = self.update(self.T[pre].l, l, mid, p, x)
            self.T[now].r = self.T[pre].r
        else:
            self.T[now].l = self.T[pre].l
            self.T[now].r = self.update(self.T[pre].r, mid + 1, r, p, x)
        return now
    
    def query(self, u, l, r, p):
        if l == r:
            return self.T[u].val
        
        mid = (l + r) >> 1
        if p <= mid:
            return self.query(self.T[u].l, l, mid, p)
        else:
            return self.query(self.T[u].r, mid + 1, r, p)

def main():
    line = sys.stdin.readline().split()
    n, m = int(line[0]), int(line[1])
    
    line = sys.stdin.readline().split()
    a = [0] * (n + 1)
    
    for i in range(1, n + 1):
        a[i] = int(line[i - 1])
    
    # 构建初始版本
    pst = PersistentSegmentTree(n + 1)
    pst.root[0] = pst.build(a, 1, n)
    
    # 处理操作
    for i in range(1, m + 1):
        line = sys.stdin.readline().split()
        v = int(line[0])
        
        if line[1] == "1":
            # 修改操作
            p = int(line[2])
            x = int(line[3])
            pst.root[i] = pst.update(pst.root[v], 1, n, p, x)
        else:
            # 查询操作
            p = int(line[2])
            print(pst.query(pst.root[v], 1, n, p))
            pst.root[i] = pst.root[v]  # 生成一样的版本

if __name__ == "__main__":
    main()