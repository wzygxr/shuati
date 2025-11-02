#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
翻转对问题
给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i,j) 称作一个重要翻转对。
你需要返回给定数组中的重要翻转对的数量。

示例 1:
输入: [1,3,2,3,1]
输出: 2

示例 2:
输入: [2,4,3,5,1]
输出: 3

提示：
1 <= nums.length <= 5 * 10^4
-2^31 <= nums[i] <= 2^31 - 1

解题思路：
使用归并排序的思想，在归并的过程中统计翻转对的数量。
对于区间 [l, r]，我们将其分为 [l, mid] 和 [mid + 1, r]，先统计左右子区间内的翻转对数量，
然后统计跨越左右子区间的翻转对数量，最后进行归并排序。

时间复杂度分析：
- 归并排序的时间复杂度为 O(n log n)
- 每一层递归中，统计翻转对的时间为 O(n)
- 总时间复杂度为 O(n log n)

空间复杂度分析：
- 需要额外的辅助数组存储临时数据，空间复杂度为 O(n)
- 递归调用栈的深度为 O(log n)
- 总空间复杂度为 O(n)

LeetCode 493. 翻转对
链接：https://leetcode.cn/problems/reverse-pairs/
"""

class Code06_ReversePairs:
    """
    翻转对问题的解决方案类
    提供了计算重要翻转对数量的方法，以及其他相关问题的解决方案
    """
    
    def reversePairs(self, nums):
        """
        计算重要翻转对的数量
        
        Args:
            nums: 输入数组
            
        Returns:
            重要翻转对的数量
        """
        # 处理空数组或只有一个元素的情况
        if not nums or len(nums) < 2:
            return 0
        
        # 转换为long类型，防止溢出
        arr = list(map(int, nums))
        
        # 调用归并排序并统计翻转对
        return self._merge_sort(arr, 0, len(arr) - 1)
    
    def _merge_sort(self, arr, l, r):
        """
        归并排序并统计区间[l, r]内的翻转对数量
        
        Args:
            arr: 数组
            l: 左边界
            r: 右边界
            
        Returns:
            区间内的翻转对数量
        """
        # 递归终止条件：只有一个元素时，没有翻转对
        if l == r:
            return 0
        
        # 计算中间位置
        m = l + ((r - l) >> 1)
        
        # 统计左半部分、右半部分以及跨越中间的翻转对数量
        return (
            self._merge_sort(arr, l, m) + 
            self._merge_sort(arr, m + 1, r) + 
            self._merge(arr, l, m, r)
        )
    
    def _merge(self, arr, l, m, r):
        """
        合并两个有序数组，并统计跨越中间的翻转对数量
        
        Args:
            arr: 数组
            l: 左边界
            m: 中间位置
            r: 右边界
            
        Returns:
            跨越中间的翻转对数量
        """
        # 统计翻转对数量
        ans = 0
        # 计算满足 arr[i] > 2 * arr[j] 的对数
        j = m + 1
        for i in range(l, m + 1):
            # 对于每个i，找到最大的j使得 arr[i] > 2 * arr[j]
            while j <= r and arr[i] > 2 * arr[j]:
                j += 1
            # j - (m + 1) 就是满足条件的j的数量
            ans += j - (m + 1)
        
        # 归并排序
        help_arr = [0] * (r - l + 1)
        i = 0
        p1 = l
        p2 = m + 1
        
        while p1 <= m and p2 <= r:
            help_arr[i] = arr[p1] if arr[p1] <= arr[p2] else arr[p2]
            p1 += 1 if arr[p1] <= arr[p2] else 0
            p2 += 1 if arr[p1 - 1] > arr[p2 - 1] else 0
            i += 1
        
        while p1 <= m:
            help_arr[i] = arr[p1]
            p1 += 1
            i += 1
        
        while p2 <= r:
            help_arr[i] = arr[p2]
            p2 += 1
            i += 1
        
        # 将辅助数组中的元素复制回原数组
        for i in range(len(help_arr)):
            arr[l + i] = help_arr[i]
        
        return ans
    
    # ==================== 补充题目：LeetCode 315. 计算右侧小于当前元素的个数 ====================
    """
    LeetCode 315. 计算右侧小于当前元素的个数
    链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
    题目：给定一个整数数组 nums，按要求返回一个新数组 counts。
    数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
    
    解题思路：
    1. 离散化处理数组元素
    2. 使用树状数组从右向左扫描，统计比当前元素小的元素个数
    """
    def countSmaller(self, nums):
        """
        计算右侧小于当前元素的个数
        
        Args:
            nums: 输入数组
            
        Returns:
            结果数组，其中counts[i]表示nums[i]右侧小于nums[i]的元素个数
        """
        if not nums:
            return []
        
        n = len(nums)
        result = [0] * n
        
        # 离散化处理
        sorted_nums = sorted(nums)
        # 创建离散化映射
        for i in range(n):
            # 使用bisect_left找到元素在排序数组中的位置
            # 映射到1~n
            nums[i] = self._bisect_left(sorted_nums, nums[i]) + 1
        
        # 树状数组实现
        class FenwickTree:
            def __init__(self, size):
                self.n = size
                self.tree = [0] * (size + 1)
            
            def lowbit(self, x):
                return x & -x
            
            def update(self, index, delta):
                while index <= self.n:
                    self.tree[index] += delta
                    index += self.lowbit(index)
            
            def query(self, index):
                sum_val = 0
                while index > 0:
                    sum_val += self.tree[index]
                    index -= self.lowbit(index)
                return sum_val
        
        bit = FenwickTree(n)
        
        # 从右向左扫描
        for i in range(n - 1, -1, -1):
            # 查询比当前元素小的个数（即查询[1, nums[i]-1]的和）
            result[i] = bit.query(nums[i] - 1)
            # 更新树状数组，当前元素出现次数+1
            bit.update(nums[i], 1)
        
        return result
    
    # ==================== 补充题目：LeetCode 327. 区间和的个数 ====================
    """
    LeetCode 327. 区间和的个数
    链接：https://leetcode.cn/problems/count-of-range-sum/
    题目：给定一个整数数组 nums 以及两个整数 lower 和 upper 。
    求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的 区间和的个数 。
    区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。
    
    解题思路：
    1. 计算前缀和数组
    2. 使用归并排序的思想，在归并过程中统计满足条件的区间和数量
    """
    def countRangeSum(self, nums, lower, upper):
        """
        计算区间和的个数
        
        Args:
            nums: 输入数组
            lower: 区间和的下界
            upper: 区间和的上界
            
        Returns:
            满足条件的区间和个数
        """
        if not nums:
            return 0
        
        n = len(nums)
        prefix_sum = [0] * (n + 1)
        
        # 计算前缀和
        for i in range(n):
            prefix_sum[i + 1] = prefix_sum[i] + nums[i]
        
        # 使用归并排序的方法统计符合条件的区间和
        def merge_sort(arr, left, right):
            if left >= right:
                return 0
            
            mid = left + (right - left) // 2
            count = merge_sort(arr, left, mid) + merge_sort(arr, mid + 1, right)
            
            # 统计跨越中间的符合条件的区间和
            i = left
            L = mid + 1  # 第一个大于等于 (arr[i] + lower) 的位置
            R = mid + 1  # 第一个大于 (arr[i] + upper) 的位置
            
            while i <= mid:
                # 找到L和R的位置
                while L <= right and arr[L] - arr[i] < lower:
                    L += 1
                while R <= right and arr[R] - arr[i] <= upper:
                    R += 1
                count += R - L
                i += 1
            
            # 归并两个有序数组
            merge(arr, left, mid, right)
            
            return count
        
        def merge(arr, left, mid, right):
            temp = []
            i = left
            j = mid + 1
            
            while i <= mid and j <= right:
                if arr[i] <= arr[j]:
                    temp.append(arr[i])
                    i += 1
                else:
                    temp.append(arr[j])
                    j += 1
            
            while i <= mid:
                temp.append(arr[i])
                i += 1
            
            while j <= right:
                temp.append(arr[j])
                j += 1
            
            for k in range(len(temp)):
                arr[left + k] = temp[k]
        
        return merge_sort(prefix_sum, 0, n)
    
    def _bisect_left(self, sorted_arr, target):
        """
        二分查找，找到第一个大于等于target的元素的索引
        
        Args:
            sorted_arr: 已排序的数组
            target: 目标值
            
        Returns:
            索引位置
        """
        left, right = 0, len(sorted_arr)
        while left < right:
            mid = left + (right - left) // 2
            if sorted_arr[mid] < target:
                left = mid + 1
            else:
                right = mid
        return left

# 测试函数
def test_solution():
    solution = Code06_ReversePairs()
    
    # 测试LeetCode 493. 翻转对
    print("LeetCode 493 测试用例1:")
    nums1 = [1, 3, 2, 3, 1]
    print(f"输入: {nums1}")
    print(f"输出: {solution.reversePairs(nums1)}")
    print(f"期望: 2\n")
    
    print("LeetCode 493 测试用例2:")
    nums2 = [2, 4, 3, 5, 1]
    print(f"输入: {nums2}")
    print(f"输出: {solution.reversePairs(nums2)}")
    print(f"期望: 3\n")
    
    # 测试LeetCode 315. 计算右侧小于当前元素的个数
    print("LeetCode 315 测试用例:")
    nums3 = [5, 2, 6, 1]
    print(f"输入: {nums3}")
    result3 = solution.countSmaller(nums3)
    print(f"输出: {result3}")
    print(f"期望: [2, 1, 1, 0]\n")
    
    # 测试LeetCode 327. 区间和的个数
    print("LeetCode 327 测试用例:")
    nums4 = [-2, 5, -1]
    lower, upper = -2, 2
    print(f"输入: nums = {nums4}, lower = {lower}, upper = {upper}")
    print(f"输出: {solution.countRangeSum(nums4, lower, upper)}")
    print(f"期望: 3")

if __name__ == "__main__":
    test_solution()