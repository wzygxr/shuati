# Problem: AtCoder ABC339 G - Smaller Sum
# Link: https://atcoder.jp/contests/abc339/tasks/abc339_g
# Description: 给定一个数组，每次在线查询区间[l,r]中不超过x的元素和
# Solution: 使用可持久化线段树解决在线区间和查询问题
# Time Complexity: O(nlogn) for preprocessing, O(logn) for each query
# Space Complexity: O(nlogn)

import sys

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
    
    def insert(self, pre, l, r, val, b_val):
        now = self.create_node()
        self.T[now].sum = self.T[pre].sum + b_val
        
        if l == r:
            return now
        
        mid = (l + r) >> 1
        if val <= mid:
            self.T[now].l = self.insert(self.T[pre].l, l, mid, val, b_val)
            self.T[now].r = self.T[pre].r
        else:
            self.T[now].l = self.T[pre].l
            self.T[now].r = self.insert(self.T[pre].r, mid + 1, r, val, b_val)
        return now
    
    def query(self, u, v, l, r, val):
        if l == r:
            if l <= val:
                return self.T[v].sum - self.T[u].sum
            else:
                return 0
        
        mid = (l + r) >> 1
        if val <= mid:
            return self.query(self.T[u].l, self.T[v].l, l, mid, val)
        else:
            leftSum = self.T[self.T[v].l].sum - self.T[self.T[u].l].sum
            return leftSum + self.query(self.T[u].r, self.T[v].r, mid + 1, r, val)

def main():
    n = int(sys.stdin.readline())
    
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
        left, right = 1, len(b) - 1
        while left <= right:
            mid = (left + right) // 2
            if b[mid] == x:
                return mid
            elif b[mid] < x:
                left = mid + 1
            else:
                right = mid - 1
        return left
    
    # 构建主席树
    pst = PersistentSegmentTree(n + 1)
    pst.root[0] = pst.create_node()
    
    for i in range(1, n + 1):
        pst.root[i] = pst.insert(pst.root[i - 1], 1, sz, get_id(a[i]), a[i])
    
    q = int(sys.stdin.readline())
    last_ans = 0
    
    for _ in range(q):
        line = sys.stdin.readline().split()
        alpha = int(line[0])
        beta = int(line[1])
        gamma = int(line[2])
        
        # 解密
        l = (alpha ^ last_ans) % n + 1
        r = (beta ^ last_ans) % n + 1
        x = gamma ^ last_ans
        
        if l > r:
            l, r = r, l
        
        last_ans = pst.query(pst.root[l - 1], pst.root[r], 1, sz, get_id(x))
        print(last_ans)

if __name__ == "__main__":
    main()