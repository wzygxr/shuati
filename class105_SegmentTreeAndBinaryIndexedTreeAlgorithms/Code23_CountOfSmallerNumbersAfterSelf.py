"""
LeetCode 315. 计算右侧小于当前元素的个数 (Count of Smaller Numbers After Self)

题目描述：
给定一个整数数组 nums，按要求返回一个新数组 counts。
数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。

解题思路：
使用树状数组（Fenwick Tree）+ 离散化来高效统计右侧小于当前元素的个数。
核心思想：
1. 从右向左遍历数组
2. 对于每个元素 nums[i]，需要统计右侧已经遍历过的元素中小于 nums[i] 的个数
3. 使用树状数组维护已经遍历过的元素的出现次数
4. 通过离散化处理大数值范围问题

时间复杂度分析：
- 离散化处理：O(n log n)
- 树状数组操作：O(n log n)
- 总时间复杂度：O(n log n)

空间复杂度分析：
- 离散化数组：O(n)
- 树状数组：O(n)
- 结果数组：O(n)
- 总空间复杂度：O(n)

工程化考量：
1. 边界条件处理：处理空数组、单个元素等情况
2. 数值范围处理：使用离散化处理大数值范围
3. 异常处理：检查输入参数合法性
4. 性能优化：使用树状数组提高统计效率

算法技巧：
- 离散化：将大范围的数值映射到小范围的索引
- 树状数组：高效统计元素出现次数
- 逆序遍历：从右向左处理，便于统计右侧元素

适用场景：
- 需要统计数组中每个元素右侧小于它的元素个数
- 数值范围较大，需要离散化处理
- 对时间复杂度要求较高的场景

测试用例：
输入：nums = [5,2,6,1]
输出：[2,1,1,0]
解释：
5 的右侧有 2 个更小的元素 (2 和 1)
2 的右侧仅有 1 个更小的元素 (1)
6 的右侧有 1 个更小的元素 (1)
1 的右侧有 0 个更小的元素
"""

from typing import List


class FenwickTree:
    """
    树状数组（Fenwick Tree）实现
    用于高效统计元素出现次数
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


class Solution:
    """
    右侧小于当前元素个数统计解决方案类
    """
    
    def countSmaller(self, nums: List[int]) -> List[int]:
        """
        计算右侧小于当前元素的个数
        
        Args:
            nums: 输入数组
            
        Returns:
            结果数组，counts[i] 表示 nums[i] 右侧小于 nums[i] 的元素个数
            
        算法步骤：
        1. 离散化处理数组元素
        2. 从右向左遍历数组
        3. 对于每个元素，查询树状数组中比它小的元素个数
        4. 更新树状数组，记录当前元素的出现
        """
        if not nums:
            return []
        
        n = len(nums)
        result = [0] * n
        
        # 1. 离散化处理
        sorted_nums = sorted(nums)
        rank_map = {}
        rank = 1
        
        for i in range(n):
            if i == 0 or sorted_nums[i] != sorted_nums[i - 1]:
                rank_map[sorted_nums[i]] = rank
                rank += 1
        
        # 2. 使用树状数组统计
        tree = FenwickTree(rank - 1)
        
        # 从右向左遍历
        for i in range(n - 1, -1, -1):
            current_rank = rank_map[nums[i]]
            
            # 查询比当前元素小的元素个数（即排名比当前小的元素）
            count = tree.query(current_rank - 1)
            result[i] = count
            
            # 更新树状数组，记录当前元素的出现
            tree.update(current_rank, 1)
        
        return result
    
    def countSmallerMergeSort(self, nums: List[int]) -> List[int]:
        """
        方法二：使用归并排序统计逆序对（备选方案）
        
        解题思路：
        1. 使用归并排序的过程统计右侧小于当前元素的个数
        2. 在合并两个有序数组时，统计右侧较小元素的个数
        3. 这种方法同样具有 O(n log n) 的时间复杂度
        
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        """
        if not nums:
            return []
        
        n = len(nums)
        indexes = list(range(n))
        counts = [0] * n
        
        self._mergeSort(nums, indexes, counts, 0, n - 1)
        return counts
    
    def _mergeSort(self, nums: List[int], indexes: List[int], counts: List[int], start: int, end: int):
        """
        归并排序递归函数
        
        Args:
            nums: 原始数组
            indexes: 索引数组
            counts: 计数数组
            start: 起始位置
            end: 结束位置
        """
        if start >= end:
            return
        
        mid = start + (end - start) // 2
        self._mergeSort(nums, indexes, counts, start, mid)
        self._mergeSort(nums, indexes, counts, mid + 1, end)
        self._merge(nums, indexes, counts, start, mid, end)
    
    def _merge(self, nums: List[int], indexes: List[int], counts: List[int], start: int, mid: int, end: int):
        """
        合并两个有序数组
        
        Args:
            nums: 原始数组
            indexes: 索引数组
            counts: 计数数组
            start: 起始位置
            mid: 中间位置
            end: 结束位置
        """
        temp_indexes = [0] * (end - start + 1)
        left, right = start, mid + 1
        right_count = 0
        index = 0
        
        while left <= mid and right <= end:
            if nums[indexes[right]] < nums[indexes[left]]:
                temp_indexes[index] = indexes[right]
                right_count += 1
                right += 1
            else:
                temp_indexes[index] = indexes[left]
                counts[indexes[left]] += right_count
                left += 1
            index += 1
        
        while left <= mid:
            temp_indexes[index] = indexes[left]
            counts[indexes[left]] += right_count
            left += 1
            index += 1
        
        while right <= end:
            temp_indexes[index] = indexes[right]
            right += 1
            index += 1
        
        # 将临时数组复制回原数组
        for i in range(len(temp_indexes)):
            indexes[start + i] = temp_indexes[i]


def test_count_smaller():
    """
    测试函数：验证算法正确性
    
    测试用例设计：
    1. 正常情况测试
    2. 边界情况测试
    3. 空数组测试
    4. 重复元素测试
    """
    solution = Solution()
    
    # 测试用例1：正常情况
    nums1 = [5, 2, 6, 1]
    result1 = solution.countSmaller(nums1)
    print(f"测试用例1结果：{result1} (期望：[2, 1, 1, 0])")
    
    # 测试用例2：边界情况
    nums2 = [-1]
    result2 = solution.countSmaller(nums2)
    print(f"测试用例2结果：{result2} (期望：[0])")
    
    # 测试用例3：空数组
    nums3 = []
    result3 = solution.countSmaller(nums3)
    print(f"测试用例3结果：{result3} (期望：[])")
    
    # 测试用例4：重复元素
    nums4 = [2, 2, 2, 2]
    result4 = solution.countSmaller(nums4)
    print(f"测试用例4结果：{result4} (期望：[0, 0, 0, 0])")
    
    # 测试用例5：大数值
    nums5 = [2147483647, -2147483648, 0, 1]
    result5 = solution.countSmaller(nums5)
    print(f"测试用例5结果：{result5} (期望：[3, 0, 0, 0])")
    
    print("所有测试用例执行完成！")


if __name__ == "__main__":
    test_count_smaller()