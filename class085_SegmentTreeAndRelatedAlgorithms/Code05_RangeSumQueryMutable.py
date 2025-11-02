# 307. 区域和检索 - 数组可修改 - 线段树实现
# 题目来源：LeetCode 307 https://leetcode.cn/problems/range-sum-query-mutable/
# 
# 题目描述：
# 给你一个数组 nums ，请你完成两类查询。
# 其中一类查询要求更新数组 nums 下标对应的值
# 另一类查询要求返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和，其中 left <= right
# 实现 NumArray 类：
# NumArray(int[] nums) 用整数数组 nums 初始化对象
# void update(int index, int val) 将 nums[index] 的值更新为 val
# int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和
# 
# 解题思路：
# 使用线段树来高效处理单点更新和区间求和操作
# 1. 线段树是一种二叉树结构，每个节点代表一个区间
# 2. 叶子节点代表数组中的单个元素
# 3. 非叶子节点代表其子节点区间的合并结果（这里是区间和）
# 
# 核心思想：
# 线段树的特性：
# 1. 树状结构：线段树是一棵完全二叉树，便于用数组存储
# 2. 区间划分：每个节点代表一个区间，根节点代表整个区间[0, n-1]
# 3. 递归构建：非叶子节点的值等于其左右子节点值的和
# 4. 高效操作：单点更新和区间查询的时间复杂度均为O(log n)
# 
# 时间复杂度分析：
# - 构建线段树：O(n)
# - 单点更新：O(log n)
# - 区间查询：O(log n)
# 空间复杂度：O(n)
#
# 请同学们务必参考如下代码中关于输入、输出的处理
# 这是输入输出处理效率很高的写法
# 提交以下的code，提交时请把类名改成"Main"，可以直接通过

class SegmentTree:
    def __init__(self, nums):
        """
        线段树实现
        线段树是一种完全二叉树，可以用数组来存储
        对于节点i，其左子节点为2*i+1，右子节点为2*i+2
        :param nums: 原始数组
        
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        self.n = len(nums)
        # 线段树通常需要4倍空间，以确保足够的节点存储
        # 对于n个元素的数组，线段树最多需要2*n-1个节点，但为了简化计算通常使用4*n
        self.tree = [0] * (self.n * 4)
        self.build_tree(nums, 0, 0, self.n - 1)
    
    def build_tree(self, nums, node, start, end):
        """
        构建线段树
        采用递归方式构建线段树，每个节点维护一个区间的信息
        叶子节点对应数组中的单个元素，非叶子节点对应区间的合并结果
        :param nums: 原始数组
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        
        时间复杂度: O(n)
        空间复杂度: O(log n) - 递归栈空间
        """
        if start == end:
            # 叶子节点，存储数组元素值
            # 叶子节点对应原数组中的一个具体元素
            self.tree[node] = nums[start]
        else:
            # 非叶子节点，需要递归构建左右子树
            mid = (start + end) // 2
            # 递归构建左子树
            # 左子节点索引为2*node+1，表示区间[start, mid]
            self.build_tree(nums, 2 * node + 1, start, mid)
            # 递归构建右子树
            # 右子节点索引为2*node+2，表示区间[mid+1, end]
            self.build_tree(nums, 2 * node + 2, mid + 1, end)
            # 合并左右子树的结果，存储区间和
            # 非叶子节点的值等于其左右子节点值的和
            self.tree[node] = self.tree[2 * node + 1] + self.tree[2 * node + 2]
    
    def update(self, index, val):
        """
        更新数组中某个位置的值
        通过递归找到对应的叶子节点并更新，然后逐层向上更新父节点
        :param index: 要更新的数组索引
        :param val: 新的值
        
        时间复杂度: O(log n)
        """
        # 调用辅助函数进行更新操作
        # 从根节点(索引0)开始，区间为[0, n-1]
        self._update_helper(0, 0, self.n - 1, index, val)
    
    def _update_helper(self, node, start, end, index, val):
        """
        更新辅助函数
        递归查找目标位置并更新，然后逐层向上更新父节点
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        :param index: 要更新的数组索引
        :param val: 新的值
        """
        if start == end:
            # 找到叶子节点，更新值
            # 当区间只有一个元素时，说明找到了目标位置
            self.tree[node] = val
        else:
            # 非叶子节点，需要继续查找目标位置
            mid = (start + end) // 2
            if index <= mid:
                # 要更新的索引在左子树中
                # 目标索引在左半区间[start, mid]内
                self._update_helper(2 * node + 1, start, mid, index, val)
            else:
                # 要更新的索引在右子树中
                # 目标索引在右半区间[mid+1, end]内
                self._update_helper(2 * node + 2, mid + 1, end, index, val)
            # 更新父节点的值（区间和）
            # 子节点更新后，需要更新当前节点的值
            self.tree[node] = self.tree[2 * node + 1] + self.tree[2 * node + 2]
    
    def sum_range(self, left, right):
        """
        查询区间和
        通过递归查询指定区间内的元素和
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间[left, right]内元素的和
        
        时间复杂度: O(log n)
        """
        # 调用辅助函数进行查询操作
        # 从根节点(索引0)开始，区间为[0, n-1]
        return self._sum_range_helper(0, 0, self.n - 1, left, right)
    
    def _sum_range_helper(self, node, start, end, left, right):
        """
        查询区间和辅助函数
        递归查询指定区间内的元素和
        优化策略：
        1. 如果当前区间与查询区间无交集，返回0
        2. 如果当前区间完全包含在查询区间内，直接返回当前节点值
        3. 否则递归查询左右子树
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间[left, right]与当前节点区间交集内元素的和
        """
        # 优化1：如果查询区间与当前区间无交集，返回0
        # 查询区间在当前区间左侧或右侧
        if right < start or end < left:
            return 0
        # 优化2：如果当前区间完全包含在查询区间内，直接返回当前节点值
        # 这是线段树查询的关键优化点
        if left <= start and end <= right:
            return self.tree[node]
        # 部分重叠，递归查询左右子树
        # 当前区间与查询区间部分重叠，需要进一步细分
        mid = (start + end) // 2
        # 递归查询左子树和右子树，返回结果之和
        return (self._sum_range_helper(2 * node + 1, start, mid, left, right) +
                self._sum_range_helper(2 * node + 2, mid + 1, end, left, right))


class NumArray:
    def __init__(self, nums):
        """
        构造函数，用整数数组 nums 初始化对象
        创建线段树实例并用初始数组构建线段树
        :param nums: 初始数组
        
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        # 创建线段树实例
        # 线段树将负责维护数组的区间和信息
        self.st = SegmentTree(nums)
    
    def update(self, index, val):
        """
        将 nums[index] 的值更新为 val
        通过线段树更新指定位置的值
        :param index: 要更新的数组索引
        :param val: 新的值
        
        时间复杂度: O(log n)
        """
        # 调用线段树的更新方法
        # 线段树会找到对应的叶子节点并更新，然后逐层向上更新父节点
        self.st.update(index, val)
    
    def sumRange(self, left, right):
        """
        返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和
        通过线段树查询指定区间的和
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间和
        
        时间复杂度: O(log n)
        """
        # 调用线段树的查询方法
        # 线段树会递归查询指定区间的和
        return self.st.sum_range(left, right)


# 测试方法
if __name__ == "__main__":
    # 测试用例:
    # ["NumArray", "sumRange", "update", "sumRange"]
    # [[[1, 3, 5]], [0, 2], [1, 2], [0, 2]]
    # 输出:
    # [null, 9, null, 8]
    
    nums = [1, 3, 5]
    numArray = NumArray(nums)
    print(numArray.sumRange(0, 2))  # 应该输出 9 (1+3+5)
    numArray.update(1, 2)   # nums = [1,2,5]
    print(numArray.sumRange(0, 2))  # 应该输出 8 (1+2+5)