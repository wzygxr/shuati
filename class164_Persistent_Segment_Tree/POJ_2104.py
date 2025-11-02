# -*- coding: utf-8 -*-
"""
POJ 2104 - K-th Number

题目描述:
给定一个长度为N的序列，进行M次查询，每次查询区间[l,r]中第K小的数。

解题思路:
使用可持久化线段树（主席树）解决静态区间第K小问题。
1. 对所有数值进行离散化处理
2. 对每个位置建立权值线段树，第i棵线段树表示前i个数的信息
3. 利用前缀和思想，通过第r棵和第l-1棵线段树的差得到区间[l,r]的信息
4. 在线段树上二分查找第k小的数

时间复杂度: O(n log n + m log n)
空间复杂度: O(n log n)

示例:
输入:
7 3
1 5 2 6 3 7 4
2 5 3
4 7 1
1 7 3

输出:
5
6
3
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
        # 离散化后的数组
        self.sorted_vals = [0] * (n + 1)
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
    
    def insert(self, pos, l, r, pre):
        """
        在线段树中插入一个值
        :param pos: 要插入的值（离散化后的坐标）
        :param l: 区间左端点
        :param r: 区间右端点
        :param pre: 前一个版本的节点编号
        :return: 新节点编号
        """
        self.cnt += 1
        rt = self.cnt
        self.left[rt] = self.left[pre]
        self.right[rt] = self.right[pre]
        self.sum[rt] = self.sum[pre] + 1
        
        if l < r:
            mid = (l + r) // 2
            if pos <= mid:
                self.left[rt] = self.insert(pos, l, mid, self.left[rt])
            else:
                self.right[rt] = self.insert(pos, mid + 1, r, self.right[rt])
        return rt
    
    def query(self, k, l, r, u, v):
        """
        查询区间第k小的数
        :param k: 第k小
        :param l: 区间左端点
        :param r: 区间右端点
        :param u: 前一个版本的根节点
        :param v: 当前版本的根节点
        :return: 第k小的数在离散化数组中的位置
        """
        if l >= r:
            return l
        mid = (l + r) // 2
        # 计算左子树中数的个数
        x = self.sum[self.left[v]] - self.sum[self.left[u]]
        if x >= k:
            # 第k小在左子树中
            return self.query(k, l, mid, self.left[u], self.left[v])
        else:
            # 第k小在右子树中
            return self.query(k - x, mid + 1, r, self.right[u], self.right[v])
    
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
    
    # 初始化可持久化线段树
    pst = PersistentSegmentTree(n)
    
    # 读取原始数组
    idx = 2
    for i in range(1, n + 1):
        pst.arr[i] = int(data[idx])
        pst.sorted_vals[i] = pst.arr[i]
        idx += 1
    
    # 离散化处理
    pst.sorted_vals[1:n+1] = sorted(pst.sorted_vals[1:n+1])
    # 去重
    size = 1
    for i in range(2, n + 1):
        if pst.sorted_vals[i] != pst.sorted_vals[size]:
            size += 1
            pst.sorted_vals[size] = pst.sorted_vals[i]
    
    # 构建主席树
    pst.root[0] = pst.build(1, size)
    for i in range(1, n + 1):
        pos = pst.get_id(pst.arr[i], size)
        pst.root[i] = pst.insert(pos, 1, size, pst.root[i - 1])
    
    # 处理查询
    results = []
    for i in range(m):
        l = int(data[idx])
        r = int(data[idx + 1])
        k = int(data[idx + 2])
        pos = pst.query(k, 1, size, pst.root[l - 1], pst.root[r])
        results.append(str(pst.sorted_vals[pos]))
        idx += 3
    
    # 输出结果
    print('\n'.join(results))


if __name__ == "__main__":
    main()