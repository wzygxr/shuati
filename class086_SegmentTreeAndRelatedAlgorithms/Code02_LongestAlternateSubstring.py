"""
题目解析:
给定一个字符串，初始全为'L'，每次操作翻转一个位置的字符，求每次操作后最长的LR交替子串长度。

解题思路:
使用线段树维护每个区间的最长交替子串长度，以及前缀和后缀的最长交替长度。

关键技术点:
1. 区间合并时需要判断中间连接处是否可以连接
2. 单点更新时需要重新计算区间信息

复杂度分析:
- 时间复杂度：
  - 建树：O(n)
  - 单次操作：O(log n)
  - 总时间复杂度：O(n + q log n)
- 空间复杂度：O(n)

线段树常见变种:
1. 动态开点线段树：适用场景：数据范围很大，但实际使用较少的情况
2. 可持久化线段树（主席树）：适用场景：需要保存历史版本的信息
3. 扫描线 + 线段树：适用场景：平面几何问题，如矩形面积并
4. 树链剖分 + 线段树：适用场景：树上路径操作
5. 线段树合并：适用场景：动态维护多个集合的信息

补充题目:
1. LeetCode 315. 计算右侧小于当前元素的个数 - https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
2. LeetCode 327. 区间和的个数 - https://leetcode.cn/problems/count-of-range-sum/
3. LeetCode 493. 翻转对 - https://leetcode.cn/problems/reverse-pairs/
4. Codeforces 339D - Xenia and Bit Operations - https://codeforces.com/problemset/problem/339/D
5. Codeforces 380C - Sereja and Brackets - https://codeforces.com/problemset/problem/380/C
"""

class AlternateSubstringTree:
    def __init__(self, n):
        self.n = n
        self.arr = [0] * (n + 1)  # 0 represents 'L', 1 represents 'R'
        self.len = [0] * (4 * n)
        self.pre = [0] * (4 * n)
        self.suf = [0] * (4 * n)
        self.build(1, 1, n)
    
    def up(self, l, r, i):
        self.len[i] = max(self.len[i << 1], self.len[i << 1 | 1])
        self.pre[i] = self.pre[i << 1]
        self.suf[i] = self.suf[i << 1 | 1]
        mid = (l + r) >> 1
        ln = mid - l + 1
        rn = r - mid
        if self.arr[mid] != self.arr[mid + 1]:
            self.len[i] = max(self.len[i], self.suf[i << 1] + self.pre[i << 1 | 1])
            if self.len[i << 1] == ln:
                self.pre[i] = ln + self.pre[i << 1 | 1]
            if self.len[i << 1 | 1] == rn:
                self.suf[i] = rn + self.suf[i << 1]
    
    def build(self, i, l, r):
        if l == r:
            self.len[i] = self.pre[i] = self.suf[i] = 1
        else:
            mid = (l + r) >> 1
            self.build(i << 1, l, mid)
            self.build(i << 1 | 1, mid + 1, r)
            self.up(l, r, i)
    
    def reverse_char(self, jobi, l, r, i):
        if l == r:
            self.arr[jobi] ^= 1
        else:
            mid = (l + r) >> 1
            if jobi <= mid:
                self.reverse_char(jobi, l, mid, i << 1)
            else:
                self.reverse_char(jobi, mid + 1, r, i << 1 | 1)
            self.up(l, r, i)

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    q = int(data[1])
    
    tree = AlternateSubstringTree(n)
    
    results = []
    idx = 2
    for _ in range(q):
        index = int(data[idx])
        idx += 1
        tree.reverse_char(index, 1, n, 1)
        results.append(str(tree.len[1]))
    
    print('\n'.join(results))

if __name__ == "__main__":
    main()