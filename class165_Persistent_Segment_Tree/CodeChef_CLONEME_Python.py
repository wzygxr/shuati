# Problem: CodeChef CLONEME - Cloning
# Link: https://www.codechef.com/JUNE17/problems/CLONEME
# Description: 给定一个数组，每次查询两个区间是否可以通过重新排列变得相同
# Solution: 使用可持久化线段树和哈希技术解决区间相等性查询问题
# Time Complexity: O(nlogn) for preprocessing, O(logn) for each query
# Space Complexity: O(nlogn)

import sys

class Node:
    def __init__(self):
        self.l = 0
        self.r = 0
        self.sum = 0
        self.hash = 0

class PersistentSegmentTree:
    def __init__(self, maxn=100005):
        self.MAXN = maxn
        self.T = [Node() for _ in range(40 * maxn)]
        self.root = [0] * maxn
        self.cnt = 0
    
    def create_node(self, l=0, r=0, sum=0, hash=0):
        self.cnt += 1
        self.T[self.cnt].l = l
        self.T[self.cnt].r = r
        self.T[self.cnt].sum = sum
        self.T[self.cnt].hash = hash
        return self.cnt
    
    def insert(self, pre, l, r, val):
        now = self.create_node()
        self.T[now].sum = self.T[pre].sum + 1
        self.T[now].hash = self.T[pre].hash + val * val
        
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
    
    def query(self, u, v, l, r, L, R):
        if L <= l and r <= R:
            return (self.T[v].sum - self.T[u].sum, self.T[v].hash - self.T[u].hash)
        if L > r or R < l:
            return (0, 0)
        
        mid = (l + r) >> 1
        left_sum, left_hash = self.query(self.T[u].l, self.T[v].l, l, mid, L, R)
        right_sum, right_hash = self.query(self.T[u].r, self.T[v].r, mid + 1, r, L, R)
        
        return (left_sum + right_sum, left_hash + right_hash)

def main():
    t = int(sys.stdin.readline())
    
    for _ in range(t):
        line = sys.stdin.readline().split()
        n, q = int(line[0]), int(line[1])
        
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
            pst.root[i] = pst.insert(pst.root[i - 1], 1, sz, get_id(a[i]))
        
        # 处理查询
        for _ in range(q):
            line = sys.stdin.readline().split()
            l1, r1, l2, r2 = int(line[0]), int(line[1]), int(line[2]), int(line[3])
            
            sum1, hash1 = pst.query(pst.root[l1 - 1], pst.root[r1], 1, sz, 1, sz)
            sum2, hash2 = pst.query(pst.root[l2 - 1], pst.root[r2], 1, sz, 1, sz)
            
            if sum1 == sum2 and hash1 == hash2:
                print("YES")
            else:
                print("NO")

if __name__ == "__main__":
    main()