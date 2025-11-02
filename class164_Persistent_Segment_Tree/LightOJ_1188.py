# -*- coding: utf-8 -*-
"""
LightOJ 1188 - Fast Queries

题目描述:
给定一个长度为N的序列，进行Q次查询，每次查询区间[l,r]中不同数字的个数。

解题思路:
使用可持久化线段树（主席树）解决区间不同元素个数问题。
1. 对于每个位置i，记录上一次出现相同数字的位置last[i]
2. 对于每个位置i，建立线段树，将位置i处的值设为1，位置last[i]处的值设为0
3. 查询区间[l,r]时，查询第r个版本的线段树在区间[l,r]上的和

时间复杂度: O((n + q) log n)
空间复杂度: O(n log n)

示例:
输入:
2
5 3
1 1 2 1 3
1 5
2 4
3 5
3 2
1 2 3
1 2
2 3

输出:
Case 1:
3
2
3
Case 2:
2
2
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
        # 记录每个数字上一次出现的位置
        self.last = {}
        # 每个版本线段树的根节点
        self.root = [0] * (n + 1)
        
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
    
    def update(self, pos, val, l, r, pre):
        """
        更新线段树中的一个位置
        :param pos: 要更新的位置
        :param val: 更新的值
        :param l: 区间左端点
        :param r: 区间右端点
        :param pre: 前一个版本的节点编号
        :return: 新节点编号
        """
        self.cnt += 1
        rt = self.cnt
        self.left[rt] = self.left[pre]
        self.right[rt] = self.right[pre]
        
        if l == r:
            self.sum[rt] = val
            return rt
        
        mid = (l + r) // 2
        if pos <= mid:
            self.left[rt] = self.update(pos, val, l, mid, self.left[rt])
        else:
            self.right[rt] = self.update(pos, val, mid + 1, r, self.right[rt])
        self.sum[rt] = self.sum[self.left[rt]] + self.sum[self.right[rt]]
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
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    t = int(data[idx])
    idx += 1
    
    results = []
    
    for case_num in range(1, t + 1):
        results.append(f"Case {case_num}:")
        
        n = int(data[idx])
        q = int(data[idx + 1])
        idx += 2
        
        # 读取原始数组
        arr = [0] * (n + 1)
        for i in range(1, n + 1):
            arr[i] = int(data[idx])
            idx += 1
        
        # 初始化可持久化线段树
        pst = PersistentSegmentTree(n)
        pst.arr = arr
        pst.last = {}
        
        # 构建空线段树
        pst.cnt = 0
        pst.root[0] = pst.build(1, n)
        
        # 构建主席树
        for i in range(1, n + 1):
            val = arr[i]
            # 先将当前位置设为1
            pst.root[i] = pst.update(i, 1, 1, n, pst.root[i - 1])
            # 如果这个数字之前出现过，将之前位置设为0
            if val in pst.last:
                pos = pst.last[val]
                pst.root[i] = pst.update(pos, 0, 1, n, pst.root[i])
            pst.last[val] = i
        
        # 处理查询
        for i in range(q):
            l = int(data[idx])
            r = int(data[idx + 1])
            ans = pst.query(l, r, 1, n, pst.root[r])
            results.append(str(ans))
            idx += 2
    
    # 输出结果
    print('\n'.join(results))


if __name__ == "__main__":
    main()