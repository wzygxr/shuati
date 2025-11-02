"""
LeetCode 493. 翻转对 (Reverse Pairs)

题目描述：
给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i,j) 称作一个重要翻转对。
你需要返回给定数组中的重要翻转对的数量。

解题思路：
使用树状数组（Fenwick Tree）+ 离散化来高效统计重要翻转对的数量。
核心思想：
1. 从右向左遍历数组
2. 对于每个元素 nums[i]，需要统计右侧已经遍历过的元素中满足 nums[i] > 2*nums[j] 的个数
3. 使用树状数组维护已经遍历过的元素的出现次数
4. 通过离散化处理大数值范围问题

时间复杂度分析：
- 离散化处理：O(n log n)
- 树状数组操作：O(n log n)
- 总时间复杂度：O(n log n)

空间复杂度分析：
- 离散化数组：O(n)
- 树状数组：O(n)
- 总空间复杂度：O(n)

工程化考量：
1. 边界条件处理：处理空数组、单个元素等情况
2. 数值溢出处理：使用Python的int类型自动处理大整数
3. 离散化优化：使用set去重并排序
4. 异常处理：检查输入参数合法性

算法技巧：
- 离散化：将大范围的数值映射到小范围的索引
- 树状数组：高效统计元素出现次数
- 逆序遍历：从右向左处理，便于统计右侧元素
- 二分查找：快速定位满足条件的边界

适用场景：
- 需要统计数组中满足特定条件的翻转对数量
- 数值范围较大，需要离散化处理
- 对时间复杂度要求较高的场景

测试用例：
输入：nums = [1,3,2,3,1]
输出：2
解释：两个重要翻转对：(1,4) 和 (3,4)
"""

from typing import List
import bisect


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
    翻转对统计解决方案类
    """
    
    def reversePairs(self, nums: List[int]) -> int:
        """
        计算重要翻转对的数量
        
        Args:
            nums: 输入数组
            
        Returns:
            重要翻转对的数量
            
        算法步骤：
        1. 离散化处理数组元素
        2. 从右向左遍历数组
        3. 对于每个元素 nums[i]，需要找到满足 nums[i] > 2*nums[j] 的 nums[j] 的范围
        4. 使用树状数组统计该范围内的元素个数
        5. 更新树状数组，记录当前元素的出现
        """
        if not nums:
            return 0
        
        n = len(nums)
        
        # 1. 离散化处理：收集所有需要离散化的值
        value_set = set()
        for num in nums:
            value_set.add(num)
            value_set.add(2 * num)
        
        # 构建离散化映射
        sorted_values = sorted(value_set)
        value_map = {val: idx + 1 for idx, val in enumerate(sorted_values)}
        
        # 2. 使用树状数组统计
        tree = FenwickTree(len(sorted_values))
        count = 0
        
        # 从右向左遍历
        for i in range(n - 1, -1, -1):
            current_num = nums[i]
            
            # 找到满足 nums[i] > 2*nums[j] 的最大 nums[j]
            # 即：nums[j] < nums[i] / 2.0
            max_allowed = (current_num - 1) // 2  # 使用整数除法避免浮点误差
            
            # 在有序列表中查找小于等于 max_allowed 的最大值的位置
            pos = bisect.bisect_right(sorted_values, max_allowed) - 1
            
            if pos >= 0:
                target_idx = pos + 1  # 树状数组索引从1开始
                count += tree.query(target_idx)
            
            # 更新树状数组，记录当前元素的出现
            current_idx = value_map[current_num]
            tree.update(current_idx, 1)
        
        return count
    
    def reversePairsMergeSort(self, nums: List[int]) -> int:
        """
        方法二：使用归并排序统计翻转对（备选方案）
        
        解题思路：
        1. 使用归并排序的过程统计重要翻转对的数量
        2. 在合并两个有序数组之前，先统计满足条件的翻转对
        3. 这种方法同样具有 O(n log n) 的时间复杂度
        
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        """
        if not nums:
            return 0
        
        return self._mergeSort(nums, 0, len(nums) - 1)
    
    def _mergeSort(self, nums: List[int], left: int, right: int) -> int:
        """
        归并排序递归函数
        
        Args:
            nums: 原始数组
            left: 起始位置
            right: 结束位置
            
        Returns:
            翻转对数量
        """
        if left >= right:
            return 0
        
        mid = left + (right - left) // 2
        count = self._mergeSort(nums, left, mid) + self._mergeSort(nums, mid + 1, right)
        
        # 统计满足条件的翻转对
        j = mid + 1
        for i in range(left, mid + 1):
            while j <= right and nums[i] > 2 * nums[j]:
                j += 1
            count += j - (mid + 1)
        
        # 合并两个有序数组
        self._merge(nums, left, mid, right)
        
        return count
    
    def _merge(self, nums: List[int], left: int, mid: int, right: int):
        """
        合并两个有序数组
        
        Args:
            nums: 原始数组
            left: 起始位置
            mid: 中间位置
            right: 结束位置
        """
        temp = []
        i, j = left, mid + 1
        
        while i <= mid and j <= right:
            if nums[i] <= nums[j]:
                temp.append(nums[i])
                i += 1
            else:
                temp.append(nums[j])
                j += 1
        
        while i <= mid:
            temp.append(nums[i])
            i += 1
        
        while j <= right:
            temp.append(nums[j])
            j += 1
        
        # 将临时数组复制回原数组
        for idx in range(len(temp)):
            nums[left + idx] = temp[idx]


def test_reverse_pairs():
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
    nums1 = [1, 3, 2, 3, 1]
    result1 = solution.reversePairs(nums1)
    print(f"测试用例1结果：{result1} (期望：2)")
    
    # 测试用例2：边界情况
    nums2 = [2, 4, 3, 5, 1]
    result2 = solution.reversePairs(nums2)
    print(f"测试用例2结果：{result2} (期望：3)")
    
    # 测试用例3：空数组
    nums3 = []
    result3 = solution.reversePairs(nums3)
    print(f"测试用例3结果：{result3} (期望：0)")
    
    # 测试用例4：大数值
    nums4 = [2147483647, 2147483647, 2147483647, 2147483647, 2147483647]
    result4 = solution.reversePairs(nums4)
    print(f"测试用例4结果：{result4} (期望：0)")
    
    # 测试用例5：负数情况
    nums5 = [-5, -5]
    result5 = solution.reversePairs(nums5)
    print(f"测试用例5结果：{result5} (期望：1)")
    
    print("所有测试用例执行完成！")


if __name__ == "__main__":
    test_reverse_pairs()