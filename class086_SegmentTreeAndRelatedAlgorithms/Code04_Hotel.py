"""
题目解析:
有n个房间，初始都为空房。支持查找连续空房间和清空房间操作。

解题思路:
使用线段树维护每个区间的连续空房间信息，包括最长连续空房间长度、前缀和后缀长度。

关键技术点:
1. 查询最左边满足条件的区间需要特殊处理
2. 区间合并时需要考虑左右子区间的连接情况

复杂度分析:
- 时间复杂度：
  - 建树：O(n)
  - 单次操作：O(log n)
  - 总时间复杂度：O((n + m) log n)
- 空间复杂度：O(n)

补充题目:
1. LeetCode 699. 掉落的方块 - https://leetcode.cn/problems/falling-squares/
2. LeetCode 850. 矩形面积 II - https://leetcode.cn/problems/rectangle-area-ii/
3. Codeforces 438D - The Child and Sequence - https://codeforces.com/problemset/problem/438/D
4. Codeforces 558E - A Simple Task - https://codeforces.com/problemset/problem/558/E
5. 洛谷 P4198 楼房重建 - https://www.luogu.com.cn/problem/P4198
"""

class HotelTree:
    def __init__(self, n):
        self.n = n
        self.len = [0] * (4 * n)
        self.pre = [0] * (4 * n)
        self.suf = [0] * (4 * n)
        self.change = [0] * (4 * n)
        self.update = [False] * (4 * n)
        self.build(1, 1, n)
    
    def up(self, i, ln, rn):
        l = i << 1
        r = i << 1 | 1
        self.len[i] = max(self.len[l], self.len[r], self.suf[l] + self.pre[r])
        self.pre[i] = self.pre[l] if self.len[l] < ln else self.pre[l] + self.pre[r]
        self.suf[i] = self.suf[r] if self.len[r] < rn else self.suf[l] + self.suf[r]
    
    def down(self, i, ln, rn):
        if self.update[i]:
            l = i << 1
            r = i << 1 | 1
            self.len[l] = self.pre[l] = self.suf[l] = 0 if self.change[i] == 1 else ln
            self.change[l] = self.change[i]
            self.update[l] = True
            
            self.len[r] = self.pre[r] = self.suf[r] = 0 if self.change[i] == 1 else rn
            self.change[r] = self.change[i]
            self.update[r] = True
            
            self.update[i] = False
    
    def lazy_update(self, i, v, n):
        self.len[i] = self.pre[i] = self.suf[i] = 0 if v == 1 else n
        self.change[i] = v
        self.update[i] = True
    
    def build(self, i, l, r):
        if l == r:
            self.len[i] = self.pre[i] = self.suf[i] = 1
        else:
            mid = (l + r) >> 1
            self.build(i << 1, l, mid)
            self.build(i << 1 | 1, mid + 1, r)
            self.up(i, mid - l + 1, r - mid)
        self.update[i] = False
    
    def update_range(self, jobl, jobr, jobv, l, r, i):
        if jobl <= l and r <= jobr:
            self.lazy_update(i, jobv, r - l + 1)
        else:
            mid = (l + r) >> 1
            ln = mid - l + 1
            rn = r - mid
            self.down(i, ln, rn)
            if jobl <= mid:
                self.update_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            self.up(i, ln, rn)
    
    def query_left(self, x, l, r, i):
        if l == r:
            return l
        else:
            mid = (l + r) >> 1
            ln = mid - l + 1
            rn = r - mid
            self.down(i, ln, rn)
            # 最先查左边
            if self.len[i << 1] >= x:
                return self.query_left(x, l, mid, i << 1)
            # 然后查中间向两边扩展的可能区域
            if self.suf[i << 1] + self.pre[i << 1 | 1] >= x:
                return mid - self.suf[i << 1] + 1
            # 前面都没有再最后查右边
            return self.query_left(x, mid + 1, r, i << 1 | 1)

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    m = int(data[1])
    
    tree = HotelTree(n)
    
    results = []
    idx = 2
    for _ in range(m):
        op = int(data[idx])
        idx += 1
        
        if op == 1:
            x = int(data[idx])
            idx += 1
            if tree.len[1] < x:
                left = 0
            else:
                left = tree.query_left(x, 1, n, 1)
                tree.update_range(left, left + x - 1, 1, 1, n, 1)
            results.append(str(left))
        else:
            x = int(data[idx])
            idx += 1
            y = int(data[idx])
            idx += 1
            tree.update_range(x, min(x + y - 1, n), 0, 1, n, 1)
    
    print('\n'.join(results))

if __name__ == "__main__":
    main()