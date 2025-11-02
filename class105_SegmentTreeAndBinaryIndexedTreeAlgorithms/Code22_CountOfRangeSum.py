"""
LeetCode 327. 区间和的个数 (Count of Range Sum)

题目描述：
给定一个整数数组 nums，以及两个整数 lower 和 upper。
返回区间和的值在区间 [lower, upper] 之间的区间个数（包含等于）。

解题思路：
使用树状数组（Fenwick Tree）+ 离散化来高效统计满足条件的区间和个数。
核心思想：
1. 计算前缀和数组 prefixSum
2. 对于每个前缀和 prefixSum[j]，需要统计有多少个 i < j 满足：
   lower <= prefixSum[j] - prefixSum[i] <= upper
   即：prefixSum[j] - upper <= prefixSum[i] <= prefixSum[j] - lower
3. 使用树状数组维护前缀和的出现次数
4. 通过离散化处理大数值范围问题

时间复杂度分析：
- 前缀和计算：O(n)
- 离散化处理：O(n log n)
- 树状数组操作：O(n log n)
- 总时间复杂度：O(n log n)

空间复杂度分析：
- 前缀和数组：O(n)
- 离散化数组：O(n)
- 树状数组：O(n)
- 总空间复杂度：O(n)

工程化考量：
1. 边界条件处理：处理空数组、lower > upper 等情况
2. 数值溢出处理：使用Python的int类型自动处理大整数
3. 离散化优化：使用set去重并排序
4. 异常处理：检查输入参数合法性

算法技巧：
- 离散化：将大范围的数值映射到小范围的索引
- 树状数组：高效统计前缀和的出现次数
- 容斥原理：通过区间查询统计满足条件的个数

适用场景：
- 需要统计满足特定条件的区间和个数
- 数值范围较大，需要离散化处理
- 对时间复杂度要求较高的场景

测试用例：
输入：nums = [-2,5,-1], lower = -2, upper = 2
输出：3
解释：三个区间和满足条件：[0,0], [2,2], [0,2]
"""

from typing import List


class FenwickTree:
    """
    树状数组（Fenwick Tree）实现
    用于高效统计前缀和的出现次数
    """
    
    def __init__(self, size: int):
        """
        构造函数
        
        Args:
            size: 树状数组大小
        """
        self.size = size
        self.tree = [0] * (size + 1)
    
    def _lowbit(self, x: int) -> int:
        """
        计算lowbit（最低位的1）
        
        Args:
            x: 输入数字
            
        Returns:
            lowbit值
        """
        return x & -x
    
    def update(self, index: int, delta: int):
        """
        更新操作：在指定位置增加一个值
        
        Args:
            index: 位置索引
            delta: 增加值
        """
        while index <= self.size:
            self.tree[index] += delta
            index += self._lowbit(index)
    
    def query(self, index: int) -> int:
        """
        查询前缀和：从1到index的和
        
        Args:
            index: 位置索引
            
        Returns:
            前缀和
        """
        total = 0
        while index > 0:
            total += self.tree[index]
            index -= self._lowbit(index)
        return total
    
    def range_query(self, left: int, right: int) -> int:
        """
        区间查询：从left到right的和
        
        Args:
            left: 左边界
            right: 右边界
            
        Returns:
            区间和
        """
        if left > right:
            return 0
        return self.query(right) - self.query(left - 1)


class Solution:
    """
    区间和个数统计解决方案类
    """
    
    def countRangeSum(self, nums: List[int], lower: int, upper: int) -> int:
        """
        计算满足条件的区间和个数
        
        Args:
            nums: 输入数组
            lower: 区间下界
            upper: 区间上界
            
        Returns:
            满足条件的区间个数
            
        算法步骤：
        1. 计算前缀和数组
        2. 离散化处理所有可能的前缀和值
        3. 使用树状数组统计前缀和出现次数
        4. 遍历前缀和数组，统计满足条件的区间个数
        """
        if not nums:
            return 0
        
        if lower > upper:
            return 0
        
        n = len(nums)
        
        # 1. 计算前缀和数组
        prefix_sum = [0] * (n + 1)
        for i in range(n):
            prefix_sum[i + 1] = prefix_sum[i] + nums[i]
        
        # 2. 离散化处理：收集所有需要离散化的值
        value_set = set()
        for s in prefix_sum:
            value_set.add(s)
            value_set.add(s - lower)
            value_set.add(s - upper)
        
        # 构建离散化映射
        sorted_values = sorted(value_set)
        value_map = {val: idx + 1 for idx, val in enumerate(sorted_values)}
        
        # 3. 使用树状数组统计前缀和出现次数
        tree = FenwickTree(len(sorted_values))
        count = 0
        
        # 从右向左遍历前缀和数组
        for s in prefix_sum:
            left_bound = s - upper
            right_bound = s - lower
            
            # 查询满足条件的区间和个数
            left_idx = value_map[left_bound]
            right_idx = value_map[right_bound]
            
            count += tree.range_query(left_idx, right_idx)
            
            # 更新当前前缀和的出现次数
            current_idx = value_map[s]
            tree.update(current_idx, 1)
        
        return count


def test_count_range_sum():
    """
    测试函数：验证算法正确性
    
    测试用例设计：
    1. 正常情况测试
    2. 边界情况测试
    3. 空数组测试
    4. 大数值测试
    """
    solution = Solution()
    
    # 测试用例1：正常情况
    nums1 = [-2, 5, -1]
    lower1, upper1 = -2, 2
    result1 = solution.countRangeSum(nums1, lower1, upper1)
    print(f"测试用例1结果：{result1} (期望：3)")
    
    # 测试用例2：边界情况
    nums2 = [0]
    lower2, upper2 = 0, 0
    result2 = solution.countRangeSum(nums2, lower2, upper2)
    print(f"测试用例2结果：{result2} (期望：1)")
    
    # 测试用例3：空数组
    nums3 = []
    result3 = solution.countRangeSum(nums3, 0, 0)
    print(f"测试用例3结果：{result3} (期望：0)")
    
    # 测试用例4：大数值
    nums4 = [2147483647, -2147483648, -1, 0]
    lower4, upper4 = -1, 0
    result4 = solution.countRangeSum(nums4, lower4, upper4)
    print(f"测试用例4结果：{result4} (期望：4)")
    
    print("所有测试用例执行完成！")


if __name__ == "__main__":
    test_count_range_sum()