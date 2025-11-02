"""
SPOJ GSS1. Can you answer these queries I (Python版本)
题目链接: https://www.spoj.com/problems/GSS1/
题目描述: 给定一个数组，查询区间[l,r]内的最大子段和

解题思路:
使用线段树实现区间最大子段和查询
每个节点维护四个值：
1. 区间和(sum)
2. 区间最大子段和(maxSum)
3. 包含左端点的最大子段和(prefixMaxSum)
4. 包含右端点的最大子段和(suffixMaxSum)

时间复杂度分析:
- 构建线段树: O(n)
- 区间查询: O(log n)
空间复杂度: O(4n) 线段树需要约4n的空间
"""

class MaximumSubarraySum:
    def __init__(self, nums):
        """
        初始化线段树
        :param nums: 输入数组
        """
        self.n = len(nums)
        self.nums = nums[:]
        # 线段树数组，每个节点存储(sum, maxSum, prefixMaxSum, suffixMaxSum)
        self.tree = [(0, float('-inf'), float('-inf'), float('-inf'))] * (4 * self.n)
        # 构建线段树
        self._build(1, 0, self.n - 1)
    
    def _build(self, node, start, end):
        """
        构建线段树
        :param node: 当前节点索引
        :param start: 区间起始位置
        :param end: 区间结束位置
        """
        if start == end:
            val = self.nums[start]
            self.tree[node] = (val, val, val, val)
        else:
            mid = (start + end) // 2
            self._build(2 * node, start, mid)
            self._build(2 * node + 1, mid + 1, end)
            
            # 获取左右子树的信息
            left_sum, left_max_sum, left_prefix_max, left_suffix_max = self.tree[2 * node]
            right_sum, right_max_sum, right_prefix_max, right_suffix_max = self.tree[2 * node + 1]
            
            # 计算当前节点的信息
            sum_val = left_sum + right_sum
            prefix_max = max(left_prefix_max, left_sum + right_prefix_max)
            suffix_max = max(right_suffix_max, right_sum + left_suffix_max)
            max_sum = max(left_max_sum, right_max_sum, left_suffix_max + right_prefix_max)
            
            self.tree[node] = (sum_val, max_sum, prefix_max, suffix_max)
    
    def _query(self, node, start, end, l, r):
        """
        查询线段树中指定区间的最大子段和信息
        :param node: 当前节点索引
        :param start: 区间起始位置
        :param end: 区间结束位置
        :param l: 查询区间起始位置
        :param r: 查询区间结束位置
        :return: (sum, maxSum, prefixMaxSum, suffixMaxSum)
        """
        if r < start or end < l:
            return (0, float('-inf'), float('-inf'), float('-inf'))
        if l <= start and end <= r:
            return self.tree[node]
        
        mid = (start + end) // 2
        left_result = self._query(2 * node, start, mid, l, r)
        right_result = self._query(2 * node + 1, mid + 1, end, l, r)
        
        # 合并左右子树的结果
        left_sum, left_max_sum, left_prefix_max, left_suffix_max = left_result
        right_sum, right_max_sum, right_prefix_max, right_suffix_max = right_result
        
        sum_val = left_sum + right_sum
        prefix_max = max(left_prefix_max, left_sum + right_prefix_max)
        suffix_max = max(right_suffix_max, right_sum + left_suffix_max)
        max_sum = max(left_max_sum, right_max_sum, left_suffix_max + right_prefix_max)
        
        return (sum_val, max_sum, prefix_max, suffix_max)
    
    def max_subarray_sum(self, l, r):
        """
        查询区间最大子段和
        :param l: 查询区间起始位置
        :param r: 查询区间结束位置
        :return: 区间最大子段和
        """
        _, max_sum, _, _ = self._query(1, 0, self.n - 1, l, r)
        return max_sum

# 测试代码
if __name__ == "__main__":
    # 示例测试
    nums = [1, -2, 3, 4, -5, 6]
    solution = MaximumSubarraySum(nums)
    
    print("初始数组:", nums)
    print("区间[0,5]的最大子段和:", solution.max_subarray_sum(0, 5))  # 3+4-5+6 = 8
    print("区间[1,4]的最大子段和:", solution.max_subarray_sum(1, 4))  # 3+4 = 7
    print("区间[2,3]的最大子段和:", solution.max_subarray_sum(2, 3))  # max(3, 4, 3+4) = 7