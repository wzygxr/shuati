# -*- coding: utf-8 -*-
"""
Codeforces 813E - Army Creation

题目描述:
给定一个长度为n的数组和k，有q个查询，每个查询给出l和r，
要求在区间[l,r]中最多能选出多少个数，使得每种数字最多选k个。

解题思路:
使用可持久化线段树（主席树）结合贪心策略解决带限制的区间元素选择问题。
1. 对于每个位置i，预处理出从位置i开始，每种数字最多选k个时能选到的最远位置
2. 对于每个查询[l,r]，在预处理的基础上使用主席树进行区间查询
3. 使用贪心策略，尽可能多地选择满足条件的数字

时间复杂度: O((n + q) log n)
空间复杂度: O(n log n)

示例:
输入:
5 2
1 2 1 2 3
3
1 3
2 4
1 5

输出:
3
3
5
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
        # 记录每种数字出现的位置
        self.positions = [[] for _ in range(n + 1)]
        # 每个版本线段树的根节点
        self.root = [0] * (n + 2)
        
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
    
    def insert(self, pos, val, l, r, pre):
        """
        在线段树中插入一个值
        :param pos: 要插入的位置
        :param val: 插入的值
        :param l: 区间左端点
        :param r: 区间右端点
        :param pre: 前一个版本的节点编号
        :return: 新节点编号
        """
        self.cnt += 1
        rt = self.cnt
        self.left[rt] = self.left[pre]
        self.right[rt] = self.right[pre]
        self.sum[rt] = self.sum[pre] + val
        
        if l < r:
            mid = (l + r) // 2
            if pos <= mid:
                self.left[rt] = self.insert(pos, val, l, mid, self.left[rt])
            else:
                self.right[rt] = self.insert(pos, val, mid + 1, r, self.right[rt])
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
    k = int(data[1])
    
    # 读取原始数组
    arr = [0] * (n + 1)
    positions = [[] for _ in range(n + 1)]
    idx = 2
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        positions[arr[i]].append(i)
        idx += 1
    
    # 初始化可持久化线段树
    pst = PersistentSegmentTree(n)
    pst.arr = arr
    pst.positions = positions
    
    # 构建主席树
    pst.root[0] = pst.build(1, n)
    
    # 预处理：对于每个位置i，计算从该位置开始最多能选多少个数
    next_pos = [n + 1] * (n + 2)  # next_pos[i]表示位置i之后第一个不能选的位置
    
    # 对每种数字，计算其限制位置
    for i in range(1, n + 1):
        if len(positions[i]) > k:
            # 如果数字i出现次数超过k，需要限制
            for j in range(len(positions[i]) - k):
                # 从第j个位置开始，第j+k个位置就是限制位置
                start = positions[i][j]
                limit = positions[i][j + k]
                next_pos[start] = min(next_pos[start], limit)
    
    # 构建主席树，维护next数组的信息
    for i in range(1, n + 1):
        pst.root[i] = pst.insert(i, next_pos[i], 1, n, pst.root[i - 1])
    
    q = int(data[idx])
    idx += 1
    
    results = []
    # 处理查询
    for i in range(q):
        l = int(data[idx])
        r = int(data[idx + 1])
        
        # 贪心策略：尽可能多地选择满足条件的数字
        ans = 0
        pos = l
        while pos <= r:
            # 查询位置pos的next值
            next_pos_val = pst.query(pos, pos, 1, n, pst.root[pos])
            if next_pos_val > r:
                # 可以选到r位置
                ans += r - pos + 1
                break
            else:
                # 只能选到next_pos_val-1位置
                ans += next_pos_val - pos
                pos = next_pos_val
        
        results.append(str(ans))
        idx += 2
    
    # 输出结果
    print('\n'.join(results))


if __name__ == "__main__":
    main()