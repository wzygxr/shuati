"""
LeetCode 307. Range Sum Query - Mutable (Python版本 - 线段树解法)
题目链接: https://leetcode.com/problems/range-sum-query-mutable/
题目描述: 给定一个整数数组 nums，处理以下类型的多个查询:
1. 更新数组中某个索引位置的值
2. 计算数组中从索引 left 到 right (包含)之间的元素之和

解题思路:
使用线段树实现区间求和查询和单点更新操作
1. 线段树每个节点存储对应区间的元素和
2. 构建线段树时，叶子节点存储数组元素，非叶子节点存储子节点的和
3. 更新操作时，从根节点开始，沿着包含目标索引的路径向下更新节点值
4. 查询操作时，根据查询区间与当前节点区间的关系决定如何递归查询

时间复杂度分析:
- 构建线段树: O(n)
- 单点更新: O(log n)
- 区间查询: O(log n)
空间复杂度: O(4n) 线段树需要约4n的空间

算法详解:
线段树是一种二叉树数据结构，每个节点代表数组的一个区间。对于区间求和查询问题，
线段树的每个节点存储其对应区间的元素和。通过递归地将区间划分为两部分，我们可以
高效地处理区间查询和单点更新操作。

线段树结构:
1. 每个节点代表一个区间[l,r]
2. 叶子节点代表单个元素
3. 非叶子节点的值等于其左右子节点值的和
"""

class NumArray:
    def __init__(self, nums):
        """
        初始化线段树
        :param nums: 输入数组
        """
        self.n = len(nums)
        self.nums = nums[:]
        self.tree = [0] * (4 * self.n)  # 线段树数组大小通常设为4n
        self.build_tree(0, 0, self.n - 1)  # 从根节点开始构建线段树

    def build_tree(self, node, start, end):
        """
        构建线段树
        :param node: 当前节点在线段树数组中的索引
        :param start: 当前节点所代表区间的起始位置
        :param end: 当前节点所代表区间的结束位置
        """
        # 如果是叶子节点，直接存储数组元素
        if start == end:
            self.tree[node] = self.nums[start]
        else:
            # 计算中点，将区间分为两部分
            mid = start + (end - start) // 2
            # 递归构建左子树
            self.build_tree(2 * node + 1, start, mid)
            # 递归构建右子树
            self.build_tree(2 * node + 2, mid + 1, end)
            # 当前节点的值等于左右子节点值的和
            self.tree[node] = self.tree[2 * node + 1] + self.tree[2 * node + 2]

    def update(self, index, val):
        """
        更新数组中某个索引位置的值
        :param index: 要更新的数组元素索引
        :param val: 新的值
        """
        delta = val - self.nums[index]
        self.nums[index] = val
        self.update_tree(0, 0, self.n - 1, index, delta)

    def update_tree(self, node, start, end, idx, delta):
        """
        更新线段树中的值
        :param node: 当前节点在线段树数组中的索引
        :param start: 当前节点所代表区间的起始位置
        :param end: 当前节点所代表区间的结束位置
        :param idx: 要更新的数组元素索引
        :param delta: 变化量
        """
        # 如果当前节点区间包含要更新的索引
        if start <= idx <= end:
            self.tree[node] += delta
            # 如果不是叶子节点，继续递归更新子节点
            if start != end:
                mid = start + (end - start) // 2
                self.update_tree(2 * node + 1, start, mid, idx, delta)
                self.update_tree(2 * node + 2, mid + 1, end, idx, delta)

    def sum_range(self, left, right):
        """
        计算数组中从索引 left 到 right (包含)之间的元素之和
        :param left: 查询区间的起始位置
        :param right: 查询区间的结束位置
        :return: 查询区间的元素和
        """
        return self.query_tree(0, 0, self.n - 1, left, right)

    def query_tree(self, node, start, end, l, r):
        """
        查询线段树中指定区间的元素和
        :param node: 当前节点在线段树数组中的索引
        :param start: 当前节点所代表区间的起始位置
        :param end: 当前节点所代表区间的结束位置
        :param l: 查询区间的起始位置
        :param r: 查询区间的结束位置
        :return: 查询区间的元素和
        """
        # 如果查询区间与当前节点区间无交集，返回0
        if r < start or end < l:
            return 0
        # 如果当前节点区间完全包含在查询区间内，直接返回当前节点的值
        if l <= start and end <= r:
            return self.tree[node]
        # 计算中点
        mid = start + (end - start) // 2
        # 递归查询左右子树
        left_sum = self.query_tree(2 * node + 1, start, mid, l, r)
        right_sum = self.query_tree(2 * node + 2, mid + 1, end, l, r)
        # 返回左右子树查询结果的和
        return left_sum + right_sum


# 测试代码
if __name__ == "__main__":
    # 示例测试
    nums = [1, 3, 5]
    num_array = NumArray(nums)
    
    print("初始数组:", nums)
    print("sum_range(0, 2):", num_array.sum_range(0, 2))  # 返回 1 + 3 + 5 = 9
    
    num_array.update(1, 2)  # nums = [1, 2, 5]
    print("更新索引1为2后:", [1, 2, 5])
    print("sum_range(0, 2):", num_array.sum_range(0, 2))  # 返回 1 + 2 + 5 = 8