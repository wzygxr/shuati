# Range Add Query (区间加法更新)
# 题目描述:
# 实现一个支持区间加法更新和单点查询的数据结构
# 支持以下操作：
# 1. 对区间[l, r]内所有元素加上一个值val
# 2. 查询某个位置的值
# 测试链接: https://leetcode.cn/problems/range-addition/
#
# 解题思路:
# 1. 使用带懒惰传播的线段树实现区间更新和单点查询
# 2. 懒惰传播用于延迟更新，避免不必要的计算
# 3. 区间更新时，只在必要时才将更新操作传递给子节点
# 4. 查询时确保所有相关的懒惰标记都被处理
#
# 时间复杂度: 
# - 区间更新: O(log n)
# - 单点查询: O(log n)
# 空间复杂度: O(n)

class SegmentTree:
    def __init__(self, size):
        """
        初始化带懒惰传播的线段树
        :param size: 数组大小
        """
        self.n = size
        # 线段树需要4*n的空间
        self.tree = [0] * (4 * self.n)  # 存储区间和
        self.lazy = [0] * (4 * self.n)  # 懒惰标记数组

    def range_add(self, l, r, val):
        """
        区间加法更新 [l, r] 区间内每个元素加上 val
        :param l: 区间左边界
        :param r: 区间右边界
        :param val: 要增加的值
        """
        self._range_add_helper(0, 0, self.n - 1, l, r, val)

    def _range_add_helper(self, node, start, end, l, r, val):
        """
        区间加法更新辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前数组区间开始索引
        :param end: 当前数组区间结束索引
        :param l: 更新区间左边界
        :param r: 更新区间右边界
        :param val: 要增加的值
        """
        # 1. 先处理懒惰标记
        self._push_down(node, start, end)

        # 2. 当前区间与更新区间无交集
        if start > r or end < l:
            return

        # 3. 当前区间完全包含在更新区间内
        if start >= l and end <= r:
            # 更新当前节点的值
            self.tree[node] += val * (end - start + 1)
            # 如果不是叶子节点，设置懒惰标记
            if start != end:
                self.lazy[2 * node + 1] += val
                self.lazy[2 * node + 2] += val
            return

        # 4. 当前区间与更新区间有部分交集，递归处理左右子树
        mid = (start + end) // 2
        self._range_add_helper(2 * node + 1, start, mid, l, r, val)
        self._range_add_helper(2 * node + 2, mid + 1, end, l, r, val)

        # 更新当前节点的值
        self.tree[node] = self.tree[2 * node + 1] + self.tree[2 * node + 2]

    def query(self, index):
        """
        查询单点的值
        :param index: 要查询的索引
        :return: 索引处的值
        """
        return self._query_helper(0, 0, self.n - 1, index)

    def _query_helper(self, node, start, end, index):
        """
        查询单点值辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前数组区间开始索引
        :param end: 当前数组区间结束索引
        :param index: 要查询的索引
        :return: 索引处的值
        """
        # 1. 先处理懒惰标记
        self._push_down(node, start, end)

        # 2. 找到叶子节点
        if start == end:
            return self.tree[node]

        # 3. 递归查询左右子树
        mid = (start + end) // 2
        if index <= mid:
            return self._query_helper(2 * node + 1, start, mid, index)
        else:
            return self._query_helper(2 * node + 2, mid + 1, end, index)

    def _push_down(self, node, start, end):
        """
        下推懒惰标记
        :param node: 当前线段树节点索引
        :param start: 当前数组区间开始索引
        :param end: 当前数组区间结束索引
        """
        # 如果当前节点没有懒惰标记，直接返回
        if self.lazy[node] == 0:
            return

        # 将懒惰标记下推到子节点
        mid = (start + end) // 2
        # 更新左子节点
        self.tree[2 * node + 1] += self.lazy[node] * (mid - start + 1)
        # 更新右子节点
        self.tree[2 * node + 2] += self.lazy[node] * (end - mid)

        # 如果子节点不是叶子节点，继续传递懒惰标记
        if start != mid:
            self.lazy[2 * node + 1] += self.lazy[node]
        if mid + 1 != end:
            self.lazy[2 * node + 2] += self.lazy[node]

        # 清除当前节点的懒惰标记
        self.lazy[node] = 0


class RangeAddQuery:
    def __init__(self, size):
        """
        初始化RangeAddQuery对象
        :param size: 数组大小
        """
        self.n = size
        self.st = SegmentTree(size)

    def range_add(self, l, r, val):
        """
        对区间[l, r]内所有元素加上val
        :param l: 区间左边界
        :param r: 区间右边界
        :param val: 要增加的值
        """
        self.st.range_add(l, r, val)

    def query(self, index):
        """
        查询索引index处的值
        :param index: 要查询的索引
        :return: 索引处的值
        """
        return self.st.query(index)


# 测试方法
if __name__ == "__main__":
    raq = RangeAddQuery(5)

    # 对区间[1, 3]内所有元素加上2
    raq.range_add(1, 3, 2)

    # 查询各个位置的值
    # 初始数组为[0, 0, 0, 0, 0]
    # 操作后变为[0, 2, 2, 2, 0]
    print(raq.query(0))  # 输出: 0
    print(raq.query(1))  # 输出: 2
    print(raq.query(2))  # 输出: 2
    print(raq.query(3))  # 输出: 2
    print(raq.query(4))  # 输出: 0

    # 对区间[2, 4]内所有元素加上3
    raq.range_add(2, 4, 3)

    # 查询各个位置的值
    # 数组变为[0, 2, 5, 5, 3]
    print(raq.query(0))  # 输出: 0
    print(raq.query(1))  # 输出: 2
    print(raq.query(2))  # 输出: 5
    print(raq.query(3))  # 输出: 5
    print(raq.query(4))  # 输出: 3