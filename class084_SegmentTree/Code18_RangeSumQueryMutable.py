# Range Sum Query - Mutable (区间求和 - 可变)
# 题目来源: LeetCode 307. Range Sum Query - Mutable
# 题目链接: https://leetcode.com/problems/range-sum-query-mutable/
# 题目链接: https://leetcode.cn/problems/range-sum-query-mutable
# 
# 题目描述:
# 给你一个数组 nums ，请你完成两类查询:
# 1. 一类查询要求更新数组 nums 下标对应的值
# 2. 一类查询要求返回数组 nums 中索引 left 和索引 right 之间（包含）的 nums 元素的和，其中 left <= right
#
# 实现 NumArray 类：
# NumArray(int[] nums) 用整数数组 nums 初始化对象
# void update(int index, int val) 将 nums[index] 的值更新为 val
# int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（包含）的 nums 元素的和
#
# 解题思路:
# 1. 使用线段树实现区间求和和单点更新
# 2. 线段树的每个节点存储对应区间的元素和
# 3. 更新操作时，从根节点开始，找到对应的叶子节点并更新，然后逐层向上更新父节点
# 4. 查询操作时，从根节点开始，根据查询区间与当前节点区间的关系进行递归查询
#
# 时间复杂度分析:
# - 构建线段树: O(n)
# - 单点更新: O(log n)
# - 区间查询: O(log n)
# 空间复杂度: O(n)

class NumArray:
    def __init__(self, nums):
        if len(nums) > 0:
            self.n = len(nums)
            self.data = nums[:]
            self.tree = [0] * (4 * self.n)
            self._build_tree(0, 0, self.n - 1)
    
    # 构建线段树
    def _build_tree(self, tree_index, l, r):
        if l == r:
            self.tree[tree_index] = self.data[l]
            return
        
        mid = l + (r - l) // 2
        left_tree_index = 2 * tree_index + 1
        right_tree_index = 2 * tree_index + 2
        
        # 构建左子树
        self._build_tree(left_tree_index, l, mid)
        # 构建右子树
        self._build_tree(right_tree_index, mid + 1, r)
        
        # 当前节点的值等于左右子树值的和
        self.tree[tree_index] = self.tree[left_tree_index] + self.tree[right_tree_index]
    
    def update(self, index, val):
        if self.n == 0:
            return
        self.data[index] = val
        self._update_tree(0, 0, self.n - 1, index, val)
    
    # 更新线段树
    def _update_tree(self, tree_index, l, r, index, val):
        if l == r:
            self.tree[tree_index] = val
            return
        
        mid = l + (r - l) // 2
        left_tree_index = 2 * tree_index + 1
        right_tree_index = 2 * tree_index + 2
        
        if index >= l and index <= mid:
            # 要更新的索引在左子树
            self._update_tree(left_tree_index, l, mid, index, val)
        else:
            # 要更新的索引在右子树
            self._update_tree(right_tree_index, mid + 1, r, index, val)
        
        # 更新当前节点的值
        self.tree[tree_index] = self.tree[left_tree_index] + self.tree[right_tree_index]
    
    def sumRange(self, left, right):
        if self.n == 0:
            return 0
        return self._query_tree(0, 0, self.n - 1, left, right)
    
    # 查询区间和
    def _query_tree(self, tree_index, l, r, query_l, query_r):
        if l == query_l and r == query_r:
            return self.tree[tree_index]
        
        mid = l + (r - l) // 2
        left_tree_index = 2 * tree_index + 1
        right_tree_index = 2 * tree_index + 2
        
        if query_r <= mid:
            # 查询区间完全在左子树
            return self._query_tree(left_tree_index, l, mid, query_l, query_r)
        elif query_l > mid:
            # 查询区间完全在右子树
            return self._query_tree(right_tree_index, mid + 1, r, query_l, query_r)
        else:
            # 查询区间跨越左右子树
            left_result = self._query_tree(left_tree_index, l, mid, query_l, mid)
            right_result = self._query_tree(right_tree_index, mid + 1, r, mid + 1, query_r)
            return left_result + right_result

# 测试代码
if __name__ == "__main__":
    # 测试用例
    nums = [1, 3, 5]
    numArray = NumArray(nums)
    
    print("初始数组:", nums)
    print("sumRange(0, 2):", numArray.sumRange(0, 2))  # 应该返回 9
    
    numArray.update(1, 2)  # nums = [1, 2, 5]
    print("更新索引1为2后:", [1, 2, 5])
    print("sumRange(0, 2):", numArray.sumRange(0, 2))  # 应该返回 8