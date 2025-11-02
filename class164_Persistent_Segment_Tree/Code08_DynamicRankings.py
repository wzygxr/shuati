# -*- coding: utf-8 -*-
"""
Luogu P2617 Dynamic Rankings

题目描述:
给定一个含有n个数的序列a1,a2…an，需要支持两种操作：
Q l r k 表示查询下标在区间[l,r]中的第k小的数；
C x y 表示将ax改为y。

解题思路:
使用树状数组套主席树解决动态区间第K小问题。
1. 对所有可能出现的数值进行离散化处理
2. 使用树状数组维护主席树，支持单点修改和区间查询
3. 对于修改操作，先删除原值再插入新值
4. 对于查询操作，利用树状数组前缀和思想，通过多个主席树的差得到区间信息
5. 在线段树上二分查找第K小的数

时间复杂度: O(m log^2 n)
空间复杂度: O(n log^2 n)
"""

class DynamicRankings:
    """动态区间第K小问题实现"""
    
    def __init__(self, n):
        """
        初始化动态区间第K小问题
        :param n: 数组长度
        """
        self.n = n
        # 原始数组
        self.arr = [0] * (n + 1)
        # 离散化后的数组
        self.sorted_vals = [0] * (n * 2 + 1)
        # 树状数组套主席树
        self.root = [0] * (n + 1)
        
        # 线段树节点信息
        self.left = [0] * (n * 100)
        self.right = [0] * (n * 100)
        self.sum = [0] * (n * 100)
        
        # 线段树节点计数器
        self.cnt = 0
        
        # 修改操作记录
        self.uL = []
        self.uR = []
    
    def lowbit(self, x):
        """
        lowbit操作
        :param x: 数值
        :return: 最低位的1
        """
        return x & (-x)
    
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
    
    def insert(self, pos, l, r, pre, val):
        """
        在线段树中插入一个值
        :param pos: 要插入的值（离散化后的坐标）
        :param l: 区间左端点
        :param r: 区间右端点
        :param pre: 前一个版本的节点编号
        :param val: 插入的值（1表示插入，-1表示删除）
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
                self.left[rt] = self.insert(pos, l, mid, self.left[rt], val)
            else:
                self.right[rt] = self.insert(pos, mid + 1, r, self.right[rt], val)
        return rt
    
    def update(self, x, pos, val, limit):
        """
        在树状数组位置x处插入值
        :param x: 树状数组位置
        :param pos: 值的位置（离散化后）
        :param val: 插入的值（1表示插入，-1表示删除）
        :param limit: 值域上限
        """
        i = x
        while i <= limit:
            self.root[i] = self.insert(pos, 1, self.cnt, self.root[i], val)
            i += self.lowbit(i)
    
    def query_sum(self, x):
        """
        查询区间和
        :param x: 树状数组位置
        :return: 前缀和
        """
        ans = 0
        i = x
        while i > 0:
            ans += self.sum[self.root[i]]
            i -= self.lowbit(i)
        return ans
    
    def query(self, k, l, r, limit):
        """
        查询区间第k小的数
        :param k: 第k小
        :param l: 区间左端点
        :param r: 区间右端点
        :param limit: 值域上限
        :return: 第k小的数在离散化数组中的位置
        """
        # 收集查询需要的主席树根节点
        self.uL.clear()
        self.uR.clear()
        i = l - 1
        while i > 0:
            self.uL.append(self.root[i])
            i -= self.lowbit(i)
        i = r
        while i > 0:
            self.uR.append(self.root[i])
            i -= self.lowbit(i)
        
        L, R = 1, limit
        while L < R:
            mid = (L + R) // 2
            tmp = 0
            for node in self.uR:
                tmp += self.sum[self.left[node]]
            for node in self.uL:
                tmp -= self.sum[self.left[node]]
            
            if tmp >= k:
                for i in range(len(self.uR)):
                    self.uR[i] = self.left[self.uR[i]]
                for i in range(len(self.uL)):
                    self.uL[i] = self.left[self.uL[i]]
                R = mid
            else:
                for i in range(len(self.uR)):
                    self.uR[i] = self.right[self.uR[i]]
                for i in range(len(self.uL)):
                    self.uL[i] = self.right[self.uL[i]]
                L = mid + 1
                k -= tmp
        return L
    
    def get_id(self, val, size):
        """
        离散化查找数值对应的排名
        :param val: 要查找的值
        :param size: 数组长度
        :return: 值的排名
        """
        import bisect
        pos = bisect.bisect_left(self.sorted_vals[1:size+1], val)
        return pos + 1


def main():
    """主函数"""
    import sys
    import bisect
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    m = int(data[1])
    
    # 初始化动态区间第K小问题
    dr = DynamicRankings(n)
    
    # 读取原始数组
    idx = 2
    for i in range(1, n + 1):
        dr.arr[i] = int(data[idx])
        dr.sorted_vals[i] = dr.arr[i]
        idx += 1
    
    # 读取操作
    ops = []
    op_x = []
    op_y = []
    op_cnt = 0
    
    for i in range(m):
        op_type = data[idx]
        x = int(data[idx + 1])
        y = int(data[idx + 2])
        ops.append(op_type)
        op_x.append(x)
        op_y.append(y)
        if op_type == "C":
            # 修改操作需要将新值加入离散化数组
            dr.sorted_vals[op_cnt + n + 1] = y
            op_cnt += 1
        idx += 3
    
    # 离散化处理
    dr.sorted_vals[1:n + op_cnt + 1] = sorted(dr.sorted_vals[1:n + op_cnt + 1])
    # 去重
    size = 1
    for i in range(2, n + op_cnt + 1):
        if dr.sorted_vals[i] != dr.sorted_vals[size]:
            size += 1
            dr.sorted_vals[size] = dr.sorted_vals[i]
    
    # 构建空主席树
    for i in range(1, n + 1):
        dr.root[i] = dr.build(1, size)
    
    # 初始化树状数组
    for i in range(1, n + 1):
        pos = dr.get_id(dr.arr[i], size)
        dr.update(i, pos, 1, n)
    
    # 处理操作
    results = []
    modify_id = 0
    for i in range(m):
        if ops[i] == "Q":
            # 查询操作
            l = op_x[i]
            r = op_y[i]
            k = int(data[idx + 2])  # 读取k值
            pos = dr.query(k, l, r, size)
            results.append(str(dr.sorted_vals[pos]))
            idx += 3
        else:
            # 修改操作
            x = op_x[i]
            y = op_y[i]
            # 删除原值
            pos1 = dr.get_id(dr.arr[x], size)
            dr.update(x, pos1, -1, n)
            # 更新数组
            dr.arr[x] = y
            # 插入新值
            pos2 = dr.get_id(y, size)
            dr.update(x, pos2, 1, n)
    
    # 输出结果
    if results:
        print('\n'.join(results))


if __name__ == "__main__":
    main()