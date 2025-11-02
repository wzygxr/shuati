# Problem: Codeforces 813E - Army Creation
# Link: https://codeforces.com/contest/813/problem/E
# Description: 给定一个数组，每次查询区间[l,r]中最多能选出多少个数，使得每个数出现次数不超过k
# Solution: 使用可持久化线段树解决区间限制计数问题
# Time Complexity: O(nlogn) for preprocessing, O(logn) for each query
# Space Complexity: O(nlogn)

import sys

class Node:
    def __init__(self):
        self.l = 0
        self.r = 0
        self.sum = 0

class PersistentSegmentTree:
    def __init__(self, maxn=100005):
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
    line = sys.stdin.readline().split()
    n, k = int(line[0]), int(line[1])
    
    line = sys.stdin.readline().split()
    a = [0] * (n + 1)
    prev = [0] * (n + 1)
    last = [0] * (n + 1)
    
    for i in range(1, n + 1):
        a[i] = int(line[i - 1])
    
    # 预处理prev数组
    for i in range(1, n + 1):
        if last[a[i]] != 0:
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
    last_ans = 0
    
    for _ in range(q):
        line = sys.stdin.readline().split()
        l = int(line[0])
        r = int(line[1])
        
        # 解密
        l = (l + last_ans) % n + 1
        r = (r + last_ans) % n + 1
        
        if l > r:
            l, r = r, l
        
        last_ans = pst.query(pst.root[l - 1], pst.root[r], 1, n, l, r)
        print(last_ans)

if __name__ == "__main__":
    main()