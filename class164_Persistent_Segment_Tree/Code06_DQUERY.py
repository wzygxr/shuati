# -*- coding: utf-8 -*-
"""
SPOJ DQUERY - D-query

题目描述:
给定一个长度为N的序列，进行Q次查询，每次查询区间[l,r]中不同数字的个数。

解题思路:
使用可持久化线段树解决区间不同元素个数问题。
1. 对于每个位置i，记录上一次出现相同数字的位置last[i]
2. 对于每个位置i，建立线段树，将位置i处的值设为1，位置last[i]处的值设为0
3. 查询区间[l,r]时，查询第r个版本的线段树在区间[l,r]上的和

时间复杂度: O((n + q) log n)
空间复杂度: O(n log n)

示例:
输入:
5
1 1 2 1 3
3
1 5
2 4
3 5

输出:
3
2
3
"""

class PersistentSegmentTree:
    """可持久化线段树实现"""
    
    def __init__(self, n):
        """
        初始化主席树
        :param n: 数组长度
        """
        self.n = n
        # 每个版本线段树的根节点
        self.root = [0] * (n + 1)
        # 线段树节点信息
        self.left = [0] * (n * 50)
        self.right = [0] * (n * 50)
        self.sum = [0] * (n * 50)
        # 线段树节点计数器
        self.cnt = 0
    
    def build(self, l, r):
        """
        构建空线段树
        :param l: 区间左端点
        :param r: 区间右端点
        :return: 根节点编号
        """
        rt = self.cnt + 1
        self.cnt += 1
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
        rt = self.cnt + 1
        self.cnt += 1
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
    n = int(input())
    arr = list(map(int, input().split()))
    
    # 记录每个数字上一次出现的位置
    last = {}
    
    # 构建主席树
    pst = PersistentSegmentTree(n)
    pst.root[0] = pst.build(1, n)
    
    # 构建各个版本
    for i in range(n):
        val = arr[i]
        # 先将当前位置设为1
        pst.root[i + 1] = pst.update(i + 1, 1, 1, n, pst.root[i])
        # 如果这个数字之前出现过，将之前位置设为0
        if val in last:
            pos = last[val]
            pst.root[i + 1] = pst.update(pos, 0, 1, n, pst.root[i + 1])
        last[val] = i + 1
    
    q = int(input())
    # 处理查询
    for _ in range(q):
        l, r = map(int, input().split())
        ans = pst.query(l, r, 1, n, pst.root[r])
        print(ans)


if __name__ == "__main__":
    main()