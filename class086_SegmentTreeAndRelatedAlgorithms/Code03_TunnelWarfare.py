"""
题目解析:
有n个房子排成一排，相邻房子有地道连接。支持摧毁、恢复和查询操作。

解题思路:
使用线段树维护每个区间的连续1的前缀和后缀长度，其中1表示房子未被摧毁。

关键技术点:
1. 查询操作需要根据位置在区间中的位置进行不同处理
2. 区间合并时考虑跨区间的情况

复杂度分析:
- 时间复杂度：
  - 建树：O(n)
  - 单次操作：O(log n)
  - 总时间复杂度：O((n + m) log n)
- 空间复杂度：O(n)

补充题目:
1. LeetCode 715. Range 模块 - https://leetcode.cn/problems/range-module/
2. LeetCode 729. 我的日程安排表 I - https://leetcode.cn/problems/my-calendar-i/
3. LeetCode 731. 我的日程安排表 II - https://leetcode.cn/problems/my-calendar-ii/
4. LeetCode 732. 我的日程安排表 III - https://leetcode.cn/problems/my-calendar-iii/
5. Codeforces 52C - Circular RMQ - https://codeforces.com/problemset/problem/52/C
6. Codeforces 242E - XOR on Segment - https://codeforces.com/problemset/problem/242/E
7. 洛谷 P2894 [USACO08FEB] Hotel G - https://www.luogu.com.cn/problem/P2894
"""

class TunnelWarfareTree:
    def __init__(self, n):
        self.n = n
        self.pre = [0] * (4 * n)
        self.suf = [0] * (4 * n)
        self.build(1, 1, n)
    
    def up(self, l, r, i):
        self.pre[i] = self.pre[i << 1]
        self.suf[i] = self.suf[i << 1 | 1]
        mid = (l + r) >> 1
        if self.pre[i << 1] == mid - l + 1:
            self.pre[i] += self.pre[i << 1 | 1]
        if self.suf[i << 1 | 1] == r - mid:
            self.suf[i] += self.suf[i << 1]
    
    def build(self, i, l, r):
        if l == r:
            self.pre[i] = self.suf[i] = 1
        else:
            mid = (l + r) >> 1
            self.build(i << 1, l, mid)
            self.build(i << 1 | 1, mid + 1, r)
            self.up(l, r, i)
    
    def update(self, jobi, jobv, l, r, i):
        if l == r:
            self.pre[i] = self.suf[i] = jobv
        else:
            mid = (l + r) >> 1
            if jobi <= mid:
                self.update(jobi, jobv, l, mid, i << 1)
            else:
                self.update(jobi, jobv, mid + 1, r, i << 1 | 1)
            self.up(l, r, i)
    
    def query(self, jobi, l, r, i):
        if l == r:
            return self.pre[i]
        else:
            mid = (l + r) >> 1
            if jobi <= mid:
                if jobi > mid - self.suf[i << 1]:
                    return self.suf[i << 1] + self.pre[i << 1 | 1]
                else:
                    return self.query(jobi, l, mid, i << 1)
            else:
                if mid + self.pre[i << 1 | 1] >= jobi:
                    return self.suf[i << 1] + self.pre[i << 1 | 1]
                else:
                    return self.query(jobi, mid + 1, r, i << 1 | 1)

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    while idx < len(data):
        n = int(data[idx])
        idx += 1
        m = int(data[idx])
        idx += 1
        
        tree = TunnelWarfareTree(n)
        stack_arr = []
        
        results = []
        for _ in range(m):
            op = data[idx]
            idx += 1
            
            if op == "D":
                x = int(data[idx])
                idx += 1
                tree.update(x, 0, 1, n, 1)
                stack_arr.append(x)
            elif op == "R":
                x = stack_arr.pop()
                tree.update(x, 1, 1, n, 1)
            else:  # op == "Q"
                x = int(data[idx])
                idx += 1
                results.append(str(tree.query(x, 1, n, 1)))
        
        print('\n'.join(results))

if __name__ == "__main__":
    main()