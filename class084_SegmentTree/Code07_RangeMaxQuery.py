# Range Max Query (区间最大值查询)
# 题目描述:
# 给定一个数组，实现以下操作：
# 1. 更新数组中某个位置的值
# 2. 查询某个区间内的最大值
# 测试链接: https://leetcode.cn/problems/max-value-of-equation/
#
# 解题思路:
# 1. 使用线段树维护数组区间最大值信息
# 2. 支持单点更新和区间查询操作
# 3. 线段树的每个节点存储对应区间的最大值
# 4. 更新操作从根节点到叶子节点递归更新路径上的所有节点
# 5. 查询操作根据查询区间与节点区间的关系进行递归查询
#
# 时间复杂度: 
# - 构建: O(n)
# - 更新: O(log n)
# - 查询: O(log n)
# 空间复杂度: O(n)

class SegmentTree:
    def __init__(self, nums):
        """
        初始化线段树
        :param nums: 输入数组
        """
        self.nums = nums
        self.n = len(nums)
        # 线段树需要4*n的空间
        self.tree = [0] * (4 * self.n)
        # 构建线段树
        self._build_tree(0, 0, self.n - 1)

    def _build_tree(self, node, start, end):
        """
        构建线段树
        :param node: 线段树节点的索引
        :param start: 数组区间开始索引
        :param end: 数组区间结束索引
        """
        # 叶子节点
        if start == end:
            self.tree[node] = self.nums[start]
            return

        # 非叶子节点，递归构建左右子树
        mid = (start + end) // 2
        # 左子节点索引为2*node+1
        self._build_tree(2 * node + 1, start, mid)
        # 右子节点索引为2*node+2
        self._build_tree(2 * node + 2, mid + 1, end)
        # 更新当前节点的值为左右子节点的最大值
        self.tree[node] = max(self.tree[2 * node + 1], self.tree[2 * node + 2])

    def update(self, index, val):
        """
        更新数组中某个索引的值
        :param index: 要更新的数组索引
        :param val: 新的值
        """
        self._update_helper(0, 0, self.n - 1, index, val)

    def _update_helper(self, node, start, end, index, val):
        """
        更新辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前数组区间开始索引
        :param end: 当前数组区间结束索引
        :param index: 要更新的数组索引
        :param val: 新的值
        """
        # 找到叶子节点，更新值
        if start == end:
            self.nums[index] = val
            self.tree[node] = val
            return

        # 在左右子树中查找需要更新的索引
        mid = (start + end) // 2
        if index <= mid:
            # 在左子树中
            self._update_helper(2 * node + 1, start, mid, index, val)
        else:
            # 在右子树中
            self._update_helper(2 * node + 2, mid + 1, end, index, val)

        # 更新当前节点的值为左右子节点的最大值
        self.tree[node] = max(self.tree[2 * node + 1], self.tree[2 * node + 2])

    def range_max(self, left, right):
        """
        查询区间最大值
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间最大值
        """
        return self._range_max_helper(0, 0, self.n - 1, left, right)

    def _range_max_helper(self, node, start, end, left, right):
        """
        查询区间最大值辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前数组区间开始索引
        :param end: 当前数组区间结束索引
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间最大值
        """
        # 当前区间与查询区间无交集
        if right < start or left > end:
            # 返回一个不影响结果的值（对于求最大值操作，返回最小值）
            return float('-inf')

        # 当前区间完全包含在查询区间内
        if left <= start and end <= right:
            return self.tree[node]

        # 当前区间与查询区间有部分交集，递归查询左右子树
        mid = (start + end) // 2
        left_max = self._range_max_helper(2 * node + 1, start, mid, left, right)
        right_max = self._range_max_helper(2 * node + 2, mid + 1, end, left, right)
        return max(left_max, right_max)


class RangeMaxQuery:
    def __init__(self, nums):
        """
        初始化RangeMaxQuery对象
        :param nums: 整数数组
        """
        self.st = SegmentTree(nums)

    def update(self, index, val):
        """
        更新数组中某个索引的值
        :param index: 要更新的数组索引
        :param val: 新的值
        """
        self.st.update(index, val)

    def range_max(self, left, right):
        """
        返回数组中索引left和索引right之间的元素的最大值
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间最大值
        """
        return self.st.range_max(left, right)


# 测试方法
if __name__ == "__main__":
    nums = [1, 3, 5, 7, 9, 11]
    rmq = RangeMaxQuery(nums)

    # 查询索引1到4之间的最大值: max(3, 5, 7, 9) = 9
    print(rmq.range_max(1, 4))  # 输出: 9

    # 更新索引2的值为15，数组变为[1, 3, 15, 7, 9, 11]
    rmq.update(2, 15)

    # 查询索引1到4之间的最大值: max(3, 15, 7, 9) = 15
    print(rmq.range_max(1, 4))  # 输出: 15