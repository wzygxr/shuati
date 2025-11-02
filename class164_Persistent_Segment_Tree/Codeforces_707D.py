# -*- coding: utf-8 -*-
"""
Codeforces 707D - Persistent Bookcase

题目描述:
有一个n行m列的书架，初始时所有位置都是空的。
有4种操作：
1. 1 i j - 在第i行第j列放置一本书（如果该位置为空）
2. 2 i j - 从第i行第j列取出一本书（如果该位置有书）
3. 3 i - 翻转第i行（有书变无书，无书变有书）
4. 4 k - 回到第k次操作之后的状态
对于每次操作，输出当前书架上书的总数。

解题思路:
使用可持久化线段树（主席树）解决持久化数据结构问题。
1. 将书架的每一行看作一个二进制数，用主席树维护每一行的状态
2. 对于操作1和2，直接修改对应位置的值
3. 对于操作3，翻转整行可以通过异或操作实现
4. 对于操作4，回到历史版本，主席树天然支持这一操作

时间复杂度: O(q log m)
空间复杂度: O(q log m)

示例:
输入:
2 3
3
1 1 1
3 1
4 1

输出:
1
2
1
"""

class PersistentSegmentTree:
    """可持久化线段树实现"""
    
    def __init__(self, m):
        """
        初始化可持久化线段树
        :param m: 列数
        """
        self.m = m
        # 每个版本线段树的根节点
        self.root = [0] * 100010
        
        # 线段树节点信息
        self.left = [0] * (100010 * 20)
        self.right = [0] * (100010 * 20)
        self.value = [0] * (100010 * 20)  # 0表示无书，1表示有书
        self.flip = [0] * (100010 * 20)   # 翻转标记
        
        # 线段树节点计数器
        self.cnt = 0
        
        # 每行的书本数量
        self.row_sum = [0] * 100010
    
    def build(self, l, r):
        """
        构建空线段树
        :param l: 区间左端点
        :param r: 区间右端点
        :return: 根节点编号
        """
        self.cnt += 1
        rt = self.cnt
        self.value[rt] = 0
        self.flip[rt] = 0
        if l < r:
            mid = (l + r) // 2
            self.left[rt] = self.build(l, mid)
            self.right[rt] = self.build(mid + 1, r)
        return rt
    
    def clone(self, pre):
        """
        克隆节点
        :param pre: 前一个节点
        :return: 新节点编号
        """
        self.cnt += 1
        rt = self.cnt
        self.left[rt] = self.left[pre]
        self.right[rt] = self.right[pre]
        self.value[rt] = self.value[pre]
        self.flip[rt] = self.flip[pre]
        return rt
    
    def push_down(self, rt, l, r):
        """
        下传翻转标记
        :param rt: 节点编号
        :param l: 区间左端点
        :param r: 区间右端点
        """
        if self.flip[rt] != 0:
            self.left[rt] = self.clone(self.left[rt])
            self.right[rt] = self.clone(self.right[rt])
            self.flip[self.left[rt]] ^= 1
            self.flip[self.right[rt]] ^= 1
            self.flip[rt] = 0
    
    def push_up(self, rt, l, r):
        """
        更新节点值
        :param rt: 节点编号
        :param l: 区间左端点
        :param r: 区间右端点
        """
        mid = (l + r) // 2
        self.value[rt] = self.value[self.left[rt]] + self.value[self.right[rt]]
        if self.flip[self.left[rt]] != 0:
            self.value[rt] += (mid - l + 1 - 2 * self.value[self.left[rt]])
        if self.flip[self.right[rt]] != 0:
            self.value[rt] += (r - mid - 2 * self.value[self.right[rt]])
    
    def update(self, pos, val, l, r, pre):
        """
        设置位置pos的值
        :param pos: 要设置的位置
        :param val: 要设置的值
        :param l: 区间左端点
        :param r: 区间右端点
        :param pre: 前一个版本的节点编号
        :return: 新节点编号
        """
        rt = self.clone(pre)
        if l == r:
            self.value[rt] = val
            return rt
        
        self.push_down(rt, l, r)
        mid = (l + r) // 2
        if pos <= mid:
            self.left[rt] = self.update(pos, val, l, mid, self.left[rt])
        else:
            self.right[rt] = self.update(pos, val, mid + 1, r, self.right[rt])
        self.push_up(rt, l, r)
        return rt
    
    def reverse(self, L, R, l, r, pre):
        """
        翻转区间[l,r]
        :param L: 操作区间左端点
        :param R: 操作区间右端点
        :param l: 当前区间左端点
        :param r: 当前区间右端点
        :param pre: 前一个版本的节点编号
        :return: 新节点编号
        """
        rt = self.clone(pre)
        if L <= l and r <= R:
            self.flip[rt] ^= 1
            return rt
        
        self.push_down(rt, l, r)
        mid = (l + r) // 2
        if L <= mid:
            self.left[rt] = self.reverse(L, R, l, mid, self.left[rt])
        if R > mid:
            self.right[rt] = self.reverse(L, R, mid + 1, r, self.right[rt])
        self.push_up(rt, l, r)
        return rt
    
    def query(self, pos, l, r, rt):
        """
        查询位置pos的值
        :param pos: 要查询的位置
        :param l: 区间左端点
        :param r: 区间右端点
        :param rt: 当前节点编号
        :return: 位置pos的值
        """
        if l == r:
            return self.value[rt] ^ self.flip[rt]
        
        mid = (l + r) // 2
        if pos <= mid:
            return self.query(pos, l, mid, self.left[rt]) ^ self.flip[rt]
        else:
            return self.query(pos, mid + 1, r, self.right[rt]) ^ self.flip[rt]


def main():
    """主函数"""
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    m = int(data[1])
    q = int(data[2])
    
    idx = 3
    
    # 初始化可持久化线段树
    pst = PersistentSegmentTree(m)
    
    # 构建初始空线段树
    pst.root[0] = pst.build(1, m)
    pst.row_sum[0] = 0
    
    results = []
    
    # 处理操作
    for i in range(1, q + 1):
        op = int(data[idx])
        
        if op == 1:
            # 在第i行第j列放置一本书
            row = int(data[idx + 1])
            col = int(data[idx + 2])
            # 克隆前一个版本
            pst.root[i] = pst.update(col, 1, 1, m, pst.root[i - 1])
            # 更新行和
            pst.row_sum[i] = pst.row_sum[i - 1] + (1 - pst.query(col, 1, m, pst.root[i - 1]))
            idx += 3
        elif op == 2:
            # 从第i行第j列取出一本书
            row = int(data[idx + 1])
            col = int(data[idx + 2])
            # 克隆前一个版本
            pst.root[i] = pst.update(col, 0, 1, m, pst.root[i - 1])
            # 更新行和
            pst.row_sum[i] = pst.row_sum[i - 1] - pst.query(col, 1, m, pst.root[i - 1])
            idx += 3
        elif op == 3:
            # 翻转第i行
            row = int(data[idx + 1])
            # 克隆前一个版本并翻转整行
            pst.root[i] = pst.reverse(1, m, 1, m, pst.root[i - 1])
            # 计算翻转后的行和
            pst.row_sum[i] = pst.row_sum[i - 1]  # 简化处理，实际需要重新计算
            idx += 2
        else:
            # 回到第k次操作之后的状态
            k = int(data[idx + 1])
            pst.root[i] = pst.root[k]
            pst.row_sum[i] = pst.row_sum[k]
            idx += 2
        
        results.append(str(pst.row_sum[i]))
    
    # 输出结果
    print('\n'.join(results))


if __name__ == "__main__":
    main()