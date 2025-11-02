"""
AtCoder ABC185F. Range Xor Query (Python版本)
题目链接: https://atcoder.jp/contests/abc185/tasks/abc185_f
题目描述: 给定一个数组，支持两种操作：
1. 更新数组中某个位置的值
2. 查询区间[l,r]内所有元素的异或值

解题思路:
使用线段树实现区间异或查询和单点更新操作
1. 线段树每个节点存储对应区间的异或值
2. 利用异或的性质：a ^ a = 0, a ^ 0 = a

时间复杂度分析:
- 构建线段树: O(n)
- 单点更新: O(log n)
- 区间查询: O(log n)
空间复杂度: O(4n) 线段树需要约4n的空间
"""


class SegmentTreeXOR:
    def __init__(self, arr):
        """
        初始化线段树
        :param arr: 输入数组
        """
        self.n = len(arr)
        self.nums = arr[:]
        # 线段树数组，大小为4*n，使用1-based indexing
        self.tree = [0] * (4 * self.n)
        # 构建线段树
        self._build(1, 0, self.n - 1)
    
    def _build(self, node, start, end):
        """
        构建线段树
        递归地构建线段树，每个节点存储对应区间的异或值
        :param node: 当前节点索引（1-based）
        :param start: 区间起始位置（0-based）
        :param end: 区间结束位置（0-based）
        """
        # 叶子节点：区间只包含一个元素
        if start == end:
            self.tree[node] = self.nums[start]
        else:
            # 非叶子节点：递归构建左右子树
            mid = (start + end) // 2
            # 构建左子树
            self._build(2 * node, start, mid)
            # 构建右子树
            self._build(2 * node + 1, mid + 1, end)
            # 当前节点的值为左右子节点值的异或
            self.tree[node] = self.tree[2 * node] ^ self.tree[2 * node + 1]
    
    def _update(self, node, start, end, idx, val):
        """
        更新线段树中的值
        递归查找目标位置并更新，然后向上回溯更新父节点的值
        :param node: 当前节点索引（1-based）
        :param start: 区间起始位置（0-based）
        :param end: 区间结束位置（0-based）
        :param idx: 要更新的数组索引（0-based）
        :param val: 新的值
        """
        # 叶子节点，直接更新
        if start == end:
            self.nums[idx] = val
            self.tree[node] = val
        else:
            # 非叶子节点，递归更新
            mid = (start + end) // 2
            # 根据索引位置决定更新左子树还是右子树
            if idx <= mid:
                self._update(2 * node, start, mid, idx, val)
            else:
                self._update(2 * node + 1, mid + 1, end, idx, val)
            # 更新当前节点的值为左右子节点值的异或
            self.tree[node] = self.tree[2 * node] ^ self.tree[2 * node + 1]
    
    def _query(self, node, start, end, l, r):
        """
        查询线段树中指定区间的异或值
        根据查询区间与当前节点区间的关系，决定是直接返回、递归查询还是分段查询
        :param node: 当前节点索引（1-based）
        :param start: 当前节点区间起始位置（0-based）
        :param end: 当前节点区间结束位置（0-based）
        :param l: 查询区间起始位置（0-based）
        :param r: 查询区间结束位置（0-based）
        :return: 区间[l, r]内元素的异或值
        """
        # 当前节点区间与查询区间无交集，返回0（异或的单位元）
        if r < start or end < l:
            return 0
        # 当前节点区间完全包含在查询区间内，直接返回节点值
        if l <= start and end <= r:
            return self.tree[node]
        # 部分重叠，需要递归查询
        mid = (start + end) // 2
        # 递归查询左子树
        p1 = self._query(2 * node, start, mid, l, r)
        # 递归查询右子树
        p2 = self._query(2 * node + 1, mid + 1, end, l, r)
        # 返回左右子树查询结果的异或
        return p1 ^ p2
    
    def update(self, idx, val):
        """
        更新指定位置的值
        :param idx: 数组索引（0-based）
        :param val: 新的值
        """
        self._update(1, 0, self.n - 1, idx, val)
    
    def xor_range(self, l, r):
        """
        查询区间异或值
        :param l: 区间起始位置（0-based）
        :param r: 区间结束位置（0-based）
        :return: 区间[l, r]内元素的异或值
        """
        return self._query(1, 0, self.n - 1, l, r)


# 测试代码
if __name__ == "__main__":
    # 示例测试
    nums = [1, 3, 5, 7, 9]
    solution = SegmentTreeXOR(nums)
    
    print("初始数组:", nums)
    print("区间[0,2]的异或值:", solution.xor_range(0, 2))  # 1^3^5 = 7
    print("区间[1,3]的异或值:", solution.xor_range(1, 3))  # 3^5^7 = 1
    
    solution.update(1, 2)  # 将索引1的值从3更新为2
    print("更新索引1的值为2后:")
    print("区间[0,2]的异或值:", solution.xor_range(0, 2))  # 1^2^5 = 6
    print("区间[1,3]的异或值:", solution.xor_range(1, 3))  # 2^5^7 = 0