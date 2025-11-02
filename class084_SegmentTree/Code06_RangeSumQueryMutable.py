# Range Sum Query - Mutable (区间求和 - 可变)
# 题目来源: LeetCode 307. Range Sum Query - Mutable
# 题目链接: https://leetcode.cn/problems/range-sum-query-mutable
# 题目链接: https://leetcode.com/problems/range-sum-query-mutable
# 
# 题目描述:
# 给你一个数组 nums ，请你完成两类查询：
# 1. 一类查询要求更新数组 nums 下标对应的值
# 2. 一类查询要求返回数组 nums 中，索引 left 和 right 之间的元素之和，包含 left 和 right 两点
# 实现 NumArray 类：
# NumArray(int[] nums) 用整数数组 nums 初始化对象
# void update(int index, int val) 将 nums[index] 的值更新为 val
# int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间
# (包含)的元素之和
#
# 解题思路:
# 1. 使用线段树维护数组区间和信息
# 2. 支持单点更新和区间查询操作
# 3. 线段树的每个节点存储对应区间的元素和
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
        # 更新当前节点的值为左右子节点值的和
        self.tree[node] = self.tree[2 * node + 1] + self.tree[2 * node + 2]

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

        # 更新当前节点的值
        self.tree[node] = self.tree[2 * node + 1] + self.tree[2 * node + 2]

    def sum_range(self, left, right):
        """
        查询区间和
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间和
        """
        return self._sum_range_helper(0, 0, self.n - 1, left, right)

    def _sum_range_helper(self, node, start, end, left, right):
        """
        查询区间和辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前数组区间开始索引
        :param end: 当前数组区间结束索引
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间和
        """
        # 当前区间与查询区间无交集
        if right < start or left > end:
            return 0

        # 当前区间完全包含在查询区间内
        if left <= start and end <= right:
            return self.tree[node]

        # 当前区间与查询区间有部分交集，递归查询左右子树
        mid = (start + end) // 2
        left_sum = self._sum_range_helper(2 * node + 1, start, mid, left, right)
        right_sum = self._sum_range_helper(2 * node + 2, mid + 1, end, left, right)
        return left_sum + right_sum


class NumArray:
    def __init__(self, nums):
        """
        初始化NumArray对象
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

    def sum_range(self, left, right):
        """
        返回数组中索引left和索引right之间的元素之和
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间和
        """
        return self.st.sum_range(left, right)


# 测试方法
if __name__ == "__main__":
    nums = [1, 3, 5]
    num_array = NumArray(nums)

    # 查询索引0到2的和: 1 + 3 + 5 = 9
    print(num_array.sum_range(0, 2))  # 输出: 9

    # 更新索引1的值为2，数组变为[1, 2, 5]
    num_array.update(1, 2)

    # 查询索引0到2的和: 1 + 2 + 5 = 8
    print(num_array.sum_range(0, 2))  # 输出: 8