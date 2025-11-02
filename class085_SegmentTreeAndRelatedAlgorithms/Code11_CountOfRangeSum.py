# 327. 区间和的个数 - 线段树实现
# 题目来源：LeetCode 327 https://leetcode.cn/problems/count-of-range-sum/
# 
# 题目描述：
# 给你一个整数数组 nums 以及两个整数 lower 和 upper 。求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的区间和的个数。
# 区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。
# 
# 解题思路：
# 使用线段树配合前缀和与离散化来解决区间和个数问题
# 1. 计算前缀和数组，将区间和问题转换为前缀和差值问题
# 2. 对于前缀和prefix_sum[i]，需要找到满足lower <= prefix_sum[j] - prefix_sum[i] <= upper的j个数
# 3. 转换为prefix_sum[i] + lower <= prefix_sum[j] <= prefix_sum[i] + upper
# 4. 从右向左遍历前缀和数组，使用线段树维护已处理的前缀和信息
# 5. 对于当前前缀和prefix_sum[i]，在线段树中查询满足条件的前缀和个数
# 6. 将当前前缀和加入线段树，供后续元素查询使用
# 
# 时间复杂度分析：
# - 计算前缀和：O(n)
# - 离散化：O(n log n)
# - 构建线段树：O(n)
# - 处理每个前缀和：O(log n)
# - 总时间复杂度：O(n log n)
# 空间复杂度：O(n)

class SegmentTree:
    def __init__(self, size):
        """
        初始化线段树
        :param size: 线段树维护的区间大小
        
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        self.n = size
        # 线段树通常需要4倍空间
        self.tree = [0] * (4 * self.n)
    
    def update(self, index, val):
        """
        更新线段树中的某个位置的值
        :param index: 要更新的位置索引
        :param val: 要增加的值
        
        时间复杂度: O(log n)
        """
        self._update_helper(1, 0, self.n - 1, index, val)
    
    def _update_helper(self, node, start, end, index, val):
        """
        更新辅助函数
        :param node: 当前节点索引
        :param start: 当前节点维护的区间左边界
        :param end: 当前节点维护的区间右边界
        :param index: 要更新的位置索引
        :param val: 要增加的值
        """
        if start == end:
            # 叶子节点，直接更新
            self.tree[node] += val
        else:
            mid = start + (end - start) // 2
            # 根据位置决定更新左子树还是右子树
            if index <= mid:
                # 在左子树中更新
                self._update_helper(2 * node, start, mid, index, val)
            else:
                # 在右子树中更新
                self._update_helper(2 * node + 1, mid + 1, end, index, val)
            # 更新当前节点的值（合并子节点）
            self.tree[node] = self.tree[2 * node] + self.tree[2 * node + 1]
    
    def query(self, left, right):
        """
        查询区间和
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间[left, right]内元素的和
        
        时间复杂度: O(log n)
        """
        # 处理边界情况
        if left < 0:
            left = 0
        if right >= self.n:
            right = self.n - 1
        if left > right:
            return 0
        
        return self._query_helper(1, 0, self.n - 1, left, right)
    
    def _query_helper(self, node, start, end, left, right):
        """
        查询辅助函数
        :param node: 当前节点索引
        :param start: 当前节点维护的区间左边界
        :param end: 当前节点维护的区间右边界
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间[left, right]内元素的和
        """
        # 查询区间与当前节点维护区间无交集，返回0
        if right < start or end < left:
            return 0
        # 当前节点维护区间完全包含在查询区间内，返回节点值
        if left <= start and end <= right:
            return self.tree[node]
        # 部分重叠，递归查询左右子树
        mid = start + (end - start) // 2
        return (self._query_helper(2 * node, start, mid, left, right) +
                self._query_helper(2 * node + 1, mid + 1, end, left, right))


def count_range_sum(nums, lower, upper):
    """
    计算区间和的个数
    :param nums: 整数数组
    :param lower: 区间和的下限
    :param upper: 区间和的上限
    :return: 区间和在[lower, upper]范围内的个数
    
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    """
    # 处理边界情况
    if not nums:
        return 0
    
    n = len(nums)
    # 计算前缀和数组，prefix_sum[0] = 0, prefix_sum[i] = nums[0] + ... + nums[i-1]
    prefix_sum = [0] * (n + 1)
    for i in range(n):
        prefix_sum[i + 1] = prefix_sum[i] + nums[i]
    
    # 离散化处理，收集所有可能需要的值
    unique_values = set()
    for sum_val in prefix_sum:
        unique_values.add(sum_val)              # 当前前缀和
        unique_values.add(sum_val - lower)      # 用于查询下界
        unique_values.add(sum_val - upper)      # 用于查询上界
    
    # 将唯一值排序并映射到连续的索引
    sorted_values = sorted(unique_values)
    
    # 创建值到索引的映射
    value_to_index = {val: idx for idx, val in enumerate(sorted_values)}
    
    count = 0
    # 构建线段树，维护离散化后的值域信息
    segment_tree = SegmentTree(len(sorted_values))
    
    # 从右到左遍历前缀和，使用线段树查询符合条件的区间和
    for i in range(n, -1, -1):
        current = prefix_sum[i]
        # 查询满足lower <= prefix_sum[j] - prefix_sum[i] <= upper的j的数量
        # 即查询prefix_sum[j]在[current + lower, current + upper]范围内的数量
        # 转换为查询离散化后的索引范围
        left = value_to_index[current + lower]
        right = value_to_index[current + upper]
        count += segment_tree.query(left, right)
        
        # 将当前前缀和添加到线段树中，供后续元素查询使用
        segment_tree.update(value_to_index[current], 1)
    
    return count


# 测试代码
if __name__ == "__main__":
    # 测试用例1: nums = [-2,5,-1], lower = -2, upper = 2
    # 输出: 3
    # 解释：区间和分别为-2, -1, 2，都在[-2,2]范围内
    nums1 = [-2, 5, -1]
    lower1 = -2
    upper1 = 2
    print(f"测试用例1: {count_range_sum(nums1, lower1, upper1)}")  # 输出: 3
    
    # 测试用例2: nums = [0], lower = 0, upper = 0
    # 输出: 1
    # 解释：只有一个区间和0，在[0,0]范围内
    nums2 = [0]
    lower2 = 0
    upper2 = 0
    print(f"测试用例2: {count_range_sum(nums2, lower2, upper2)}")  # 输出: 1
    
    # 测试用例3: nums = [1,2,3,4,5], lower = 5, upper = 10
    # 输出: 4
    # 解释：有4个区间和在[5,10]范围内
    nums3 = [1, 2, 3, 4, 5]
    lower3 = 5
    upper3 = 10
    print(f"测试用例3: {count_range_sum(nums3, lower3, upper3)}")  # 输出: 4
    
    # 测试边界情况
    nums4 = []
    print(f"测试用例4(空数组): {count_range_sum(nums4, 0, 0)}")  # 输出: 0