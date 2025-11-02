"""
排序算法扩展题目 - Python版本 (第一部分)
包含更多LeetCode、牛客网、剑指Offer等平台的排序相关题目
每个题目都包含多种解法和详细分析

时间复杂度分析：详细分析每种解法的时间复杂度
空间复杂度分析：分析内存使用情况
最优解判断：确定是否为最优解，如果不是则寻找最优解
"""

import heapq
import random
from typing import List, Tuple
import sys

class ExtendedSortProblems:
    """
    题目1: 88. 合并两个有序数组
    来源: LeetCode
    链接: https://leetcode.cn/problems/merge-sorted-array/
    难度: 简单
    
    时间复杂度: O(m + n)
    空间复杂度: O(1)
    是否最优解: 是
    """
    @staticmethod
    def merge_sorted_arrays(nums1: List[int], m: int, nums2: List[int], n: int) -> None:
        """
        合并两个有序数组到nums1中
        
        Args:
            nums1: 第一个有序数组，有足够的空间容纳合并后的结果
            m: nums1中有效元素的数量
            nums2: 第二个有序数组
            n: nums2中元素的数量
        """
        if m < 0 or n < 0:
            raise ValueError("Invalid input parameters")
        
        p1 = m - 1  # nums1有效部分的末尾
        p2 = n - 1  # nums2的末尾
        p = m + n - 1  # 合并后的末尾
        
        # 从后向前合并，避免覆盖nums1中的元素
        while p1 >= 0 and p2 >= 0:
            if nums1[p1] > nums2[p2]:
                nums1[p] = nums1[p1]
                p1 -= 1
            else:
                nums1[p] = nums2[p2]
                p2 -= 1
            p -= 1
        
        # 如果nums2还有剩余元素，直接复制到nums1前面
        while p2 >= 0:
            nums1[p] = nums2[p2]
            p2 -= 1
            p -= 1
    
    """
    题目2: 973. 最接近原点的K个点
    来源: LeetCode
    链接: https://leetcode.cn/problems/k-closest-points-to-origin/
    难度: 中等
    
    时间复杂度: O(n) 平均
    空间复杂度: O(1)
    是否最优解: 是
    """
    @staticmethod
    def k_closest(points: List[List[int]], k: int) -> List[List[int]]:
        """
        找到距离原点最近的k个点
        
        Args:
            points: 点坐标列表，每个点格式为[x, y]
            k: 需要返回的点的数量
            
        Returns:
            距离原点最近的k个点
        """
        if not points or k <= 0 or k > len(points):
            raise ValueError("Invalid input parameters")
        
        # 使用快速选择算法找到第k小的距离
        ExtendedSortProblems._quick_select(points, 0, len(points) - 1, k)
        
        # 返回前k个点
        return points[:k]
    
    @staticmethod
    def _quick_select(points: List[List[int]], left: int, right: int, k: int) -> None:
        """快速选择算法实现"""
        if left >= right:
            return
        
        # 随机选择pivot
        pivot_index = random.randint(left, right)
        pivot_dist = ExtendedSortProblems._distance(points[pivot_index])
        
        # 分区操作
        i = left
        for j in range(left, right + 1):
            if ExtendedSortProblems._distance(points[j]) <= pivot_dist:
                points[i], points[j] = points[j], points[i]
                i += 1
        
        # 根据分区结果决定下一步
        if i == k:
            return
        elif i < k:
            ExtendedSortProblems._quick_select(points, i, right, k)
        else:
            ExtendedSortProblems._quick_select(points, left, i - 1, k)
    
    @staticmethod
    def _distance(point: List[int]) -> int:
        """计算点到原点的距离平方（避免开方运算）"""
        return point[0] * point[0] + point[1] * point[1]
    
    """
    题目3: 1054. 距离相等的条形码
    来源: LeetCode
    链接: https://leetcode.cn/problems/distant-barcodes/
    难度: 中等
    
    时间复杂度: O(n log k) - k为不同条形码的数量
    空间复杂度: O(n)
    是否最优解: 是
    """
    @staticmethod
    def rearrange_barcodes(barcodes: List[int]) -> List[int]:
        """
        重新排列条形码，使相邻条形码不相等
        
        Args:
            barcodes: 条形码列表
            
        Returns:
            重新排列后的条形码列表
        """
        if not barcodes:
            return []
        
        # 统计频率
        from collections import Counter
        freq_map = Counter(barcodes)
        
        # 最大堆，按频率排序
        max_heap = []
        for code, freq in freq_map.items():
            heapq.heappush(max_heap, (-freq, code))
        
        result = [0] * len(barcodes)
        index = 0
        
        # 间隔填充，先填偶数位置，再填奇数位置
        while max_heap:
            neg_freq, code = heapq.heappop(max_heap)
            freq = -neg_freq
            
            # 填充所有当前条形码
            for _ in range(freq):
                if index >= len(result):
                    index = 1  # 切换到奇数位置
                result[index] = code
                index += 2
        
        return result
    
    """
    题目4: 324. 摆动排序 II
    来源: LeetCode
    链接: https://leetcode.cn/problems/wiggle-sort-ii/
    难度: 中等
    
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    是否最优解: 是（对于通用情况）
    """
    @staticmethod
    def wiggle_sort(nums: List[int]) -> None:
        """
        摆动排序：nums[0] < nums[1] > nums[2] < nums[3]...
        
        Args:
            nums: 待排序数组
        """
        if len(nums) <= 1:
            return
        
        # 复制并排序数组
        sorted_nums = sorted(nums)
        
        n = len(nums)
        mid = (n + 1) // 2  # 中间位置（向上取整）
        
        # 双指针填充：左半部分从大到小，右半部分从大到小
        left = mid - 1
        right = n - 1
        index = 0
        
        while index < n:
            if index % 2 == 0:
                # 偶数位置：取左半部分（较小的数）
                nums[index] = sorted_nums[left]
                left -= 1
            else:
                # 奇数位置：取右半部分（较大的数）
                nums[index] = sorted_nums[right]
                right -= 1
            index += 1
    
    """
    题目5: 280. 摆动排序
    来源: LeetCode
    链接: https://leetcode.cn/problems/wiggle-sort/
    难度: 中等
    
    时间复杂度: O(n)
    空间复杂度: O(1)
    是否最优解: 是
    """
    @staticmethod
    def wiggle_sort_280(nums: List[int]) -> None:
        """
        摆动排序：nums[0] <= nums[1] >= nums[2] <= nums[3]...
        
        Args:
            nums: 待排序数组
        """
        if len(nums) <= 1:
            return
        
        # 一次遍历，根据需要交换相邻元素
        for i in range(len(nums) - 1):
            if (i % 2 == 0 and nums[i] > nums[i + 1]) or \
               (i % 2 == 1 and nums[i] < nums[i + 1]):
                # 交换相邻元素
                nums[i], nums[i + 1] = nums[i + 1], nums[i]
    
    """
    题目6: 493. 翻转对
    来源: LeetCode
    链接: https://leetcode.cn/problems/reverse-pairs/
    难度: 困难
    
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    是否最优解: 是
    """
    @staticmethod
    def reverse_pairs_493(nums: List[int]) -> int:
        """
        统计数组中翻转对的数量（nums[i] > 2 * nums[j] 且 i < j）
        
        Args:
            nums: 整数数组
            
        Returns:
            翻转对的数量
        """
        if len(nums) <= 1:
            return 0
        
        temp = [0] * len(nums)
        return ExtendedSortProblems._merge_sort_count_pairs(nums, 0, len(nums) - 1, temp)
    
    @staticmethod
    def _merge_sort_count_pairs(nums: List[int], left: int, right: int, temp: List[int]) -> int:
        """归并排序统计翻转对"""
        if left >= right:
            return 0
        
        mid = left + (right - left) // 2
        count = 0
        
        count += ExtendedSortProblems._merge_sort_count_pairs(nums, left, mid, temp)
        count += ExtendedSortProblems._merge_sort_count_pairs(nums, mid + 1, right, temp)
        count += ExtendedSortProblems._count_pairs(nums, left, mid, right)
        ExtendedSortProblems._merge(nums, left, mid, right, temp)
        
        return count
    
    @staticmethod
    def _count_pairs(nums: List[int], left: int, mid: int, right: int) -> int:
        """统计满足 nums[i] > 2 * nums[j] 的对数"""
        count = 0
        j = mid + 1
        
        # 统计满足 nums[i] > 2 * nums[j] 的对数
        for i in range(left, mid + 1):
            while j <= right and nums[i] > 2 * nums[j]:
                j += 1
            count += (j - (mid + 1))
        
        return count
    
    @staticmethod
    def _merge(nums: List[int], left: int, mid: int, right: int, temp: List[int]) -> None:
        """归并操作"""
        for i in range(left, right + 1):
            temp[i] = nums[i]
        
        i, k, j = left, left, mid + 1
        
        while i <= mid and j <= right:
            if temp[i] <= temp[j]:
                nums[k] = temp[i]
                i += 1
            else:
                nums[k] = temp[j]
                j += 1
            k += 1
        
        while i <= mid:
            nums[k] = temp[i]
            i += 1
            k += 1
        
        while j <= right:
            nums[k] = temp[j]
            j += 1
            k += 1