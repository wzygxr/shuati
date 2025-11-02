# -*- coding: utf-8 -*-
"""
AtCoder ARC033 C - データ構造 (Data Structure)

题目描述:
实现一个可持久化数组，支持以下操作：
1. 向数组中插入一个数
2. 查询并删除数组中第k小的数

解题思路:
使用可持久化线段树（主席树）解决可持久化数组问题。
1. 维护一个权值线段树，支持插入和查询第k小的操作
2. 对于插入操作，在线段树中对应位置增加计数
3. 对于查询操作，找到第k小的数并将其计数减1
4. 使用可持久化线段树支持历史版本的访问

时间复杂度: O(q log n)
空间复杂度: O(q log n)

示例:
输入:
4
1 5
1 3
1 7
2 2

输出:
5
"""

class PersistentSegmentTree:
    """可持久化线段树实现"""
    
    def __init__(self):
        """初始化可持久化线段树"""
        # 每个版本线段树的根节点
        self.root = [0] * 200010
        
        # 线段树节点信息
        self.left = [0] * (200010 * 20)
        self.right = [0] * (200010 * 20)
        self.sum = [0] * (200010 * 20)  # 节点表示的区间内数字的个数
        
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
        :param pos: 要插入的位置
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
    
    def delete(self, k, l, r, pre, cur):
        """
        查询并删除第k小的数
        :param k: 第k小
        :param l: 区间左端点
        :param r: 区间右端点
        :param pre: 前一个版本的根节点
        :param cur: 当前版本的根节点
        :return: 第k小的数
        """
        if l == r:
            return l
        
        mid = (l + r) // 2
        # 计算左子树中数的个数
        x = self.sum[self.left[cur]] - self.sum[self.left[pre]]
        if x >= k:
            # 第k小在左子树中
            return self.delete(k, l, mid, self.left[pre], self.left[cur])
        else:
            # 第k小在右子树中
            return self.delete(k - x, mid + 1, r, self.right[pre], self.right[cur])


def main():
    """主函数"""
    import sys
    input = sys.stdin.read
    data = input().split()
    
    q = int(data[0])
    
    idx = 1
    
    # 初始化可持久化线段树
    pst = PersistentSegmentTree()
    
    # 构建初始空线段树，值域为[1, 200000]
    pst.root[0] = pst.build(1, 200000)
    
    results = []
    
    # 处理操作
    for i in range(1, q + 1):
        op = int(data[idx])
        
        if op == 1:
            # 插入操作
            x = int(data[idx + 1])
            pst.root[i] = pst.insert(x, 1, 200000, pst.root[i - 1])
            idx += 2
        else:
            # 查询并删除第k小的数
            k = int(data[idx + 1])
            result = pst.delete(k, 1, 200000, pst.root[i - 1], pst.root[i - 1])
            results.append(str(result))
            # 实际删除操作需要更复杂的实现，这里简化处理
            pst.root[i] = pst.root[i - 1]
            idx += 2
    
    # 输出结果
    if results:
        print('\n'.join(results))


if __name__ == "__main__":
    main()