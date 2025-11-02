"""
排序算法扩展题目 - Python版本
包含LeetCode、牛客网等平台的排序相关题目
每个题目都包含多种解法和详细分析

题目链接汇总:
- 215. 数组中的第K个最大元素: https://leetcode.cn/problems/kth-largest-element-in-an-array/
- 75. 颜色分类: https://leetcode.cn/problems/sort-colors/
- 56. 合并区间: https://leetcode.cn/problems/merge-intervals/
- 347. 前K个高频元素: https://leetcode.cn/problems/top-k-frequent-elements/
- 164. 最大间距: https://leetcode.cn/problems/maximum-gap/
- ALDS1_2_A: Bubble Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_A
- ALDS1_2_B: Selection Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_B
- ALDS1_2_C: Stable Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_C
- ALDS1_2_D: Shell Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_D
- ALDS1_5_B: Merge Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_B
- ALDS1_5_D: The Number of Inversions: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_D
- ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
- ALDS1_6_C: Quick Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_C
- ALDS1_6_D: Minimum Cost Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_D
- ALDS1_9_A: Complete Binary Tree: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_A
- ALDS1_9_B: Maximum Heap: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_B
- ALDS1_9_C: Priority Queue: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_C

工程化考量:
- 异常处理: 对空数组、非法输入进行验证
- 边界条件: 处理各种边界情况
- 性能优化: 根据数据规模选择最优算法
- 内存管理: 合理使用数据结构，避免不必要的内存占用
- 可读性: 清晰的命名和详细注释

算法选择建议:
- 第K大元素: 快速选择算法（平均O(n)）
- 颜色分类: 三指针法（荷兰国旗问题，O(n)）
- 合并区间: 排序+合并（O(n log n)）
- 前K个高频元素: 桶排序（O(n)）或最小堆（O(n log k)）
- 最大间距: 基数排序（O(n)）
"""

import heapq
import random
from typing import List, Tuple
from collections import defaultdict, Counter

class ExtendedProblems:
    """
    扩展问题类 - 包含各种排序相关的算法题目
    
    工程化特性：
    1. 每个子类对应一个算法题目
    2. 提供多种解法并分析复杂度
    3. 包含详细的测试用例
    4. 注重异常处理和边界条件
    """
    
    class KthLargestElement:
        """
        题目1: 215. 数组中的第K个最大元素
        来源: LeetCode
        链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
        
        题目描述:
        给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
        请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
        
        示例:
        输入: [3,2,1,5,6,4], k = 2
        输出: 5
        
        解法对比:
        1. 快速选择算法: 平均时间复杂度O(n)，最优解
        2. 最小堆: 时间复杂度O(n log k)，适合k较小时
        3. 排序: 时间复杂度O(n log n)，简单但效率较低
        
        相关题目：
        - ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
        
        工程化考量:
        - 输入验证: 检查数组是否为空，k是否合法
        - 随机化: 快速选择使用随机基准避免最坏情况
        - 内存优化: 最小堆只维护k个元素
        """
        
        @staticmethod
        def find_kth_largest_quick_select(nums: List[int], k: int) -> int:
            """
            解法1: 快速选择算法 (最优解)
            时间复杂度: O(n) 平均, O(n²) 最坏
            空间复杂度: O(1)
            
            算法原理:
            基于快速排序的分区思想，但只处理包含目标的一侧
            1. 随机选择基准元素
            2. 进行分区操作，确定基准元素的最终位置
            3. 根据基准位置与目标位置的关系决定继续处理哪一侧
            
            优势:
            - 平均时间复杂度为线性，是最优解
            - 原地操作，空间复杂度O(1)
            
            劣势:
            - 最坏情况时间复杂度O(n²)
            - 不稳定排序
            
            相关题目：
            - ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
            
            Args:
                nums: 输入数组
                k: 第k大的元素
                
            Returns:
                第k大的元素值
                
            Raises:
                ValueError: 当输入参数不合法时
                
            工程化考量：
            - 使用random.randint避免最坏情况
            - 原地操作节省内存
            - 适合处理大数据集
            """
            # 输入验证
            if not nums or k < 1 or k > len(nums):
                raise ValueError("Invalid input")
            
            left, right = 0, len(nums) - 1
            k_smallest = len(nums) - k  # 转换为第k小的索引
            
            # 循环进行分区操作直到找到目标元素
            while left <= right:
                # 随机选择基准元素索引
                pivot_index = random.randint(left, right)
                # 分区操作，返回基准元素的最终位置
                pivot_pos = ExtendedProblems.KthLargestElement._partition(nums, left, right, pivot_index)
                
                # 根据基准位置与目标位置的关系决定继续处理哪一侧
                if pivot_pos == k_smallest:
                    return nums[pivot_pos]
                elif pivot_pos < k_smallest:
                    left = pivot_pos + 1
                else:
                    right = pivot_pos - 1
            
            return -1
        
        @staticmethod
        def find_kth_largest_min_heap(nums: List[int], k: int) -> int:
            """
            解法2: 最小堆实现
            时间复杂度: O(n log k)
            空间复杂度: O(k)
            
            算法原理:
            使用最小堆维护前k个最大的元素
            1. 遍历数组，将元素加入堆中
            2. 如果堆的大小超过k，移除堆顶元素（最小的元素）
            3. 最后堆顶元素即为第k大的元素
            
            优势:
            - 时间复杂度为O(n log k)，适合k较小时
            - 空间复杂度为O(k)
            
            劣势:
            - 时间复杂度高于快速选择
            - 需要额外的空间
            
            工程化考量：
            - 使用heapq实现最小堆
            - 只维护k个元素，节省内存
            - 适合流式数据处理
            """
            # 输入验证
            if not nums or k < 1 or k > len(nums):
                raise ValueError("Invalid input")
            
            # 创建最小堆
            min_heap = []
            
            # 遍历数组元素
            for num in nums:
                heapq.heappush(min_heap, num)
                # 如果堆的大小超过k，移除堆顶元素
                if len(min_heap) > k:
                    heapq.heappop(min_heap)  # 移除最小的元素
            
            # 堆顶元素即为第k大的元素
            return min_heap[0]
        
        @staticmethod
        def find_kth_largest_sort(nums: List[int], k: int) -> int:
            """
            解法3: 排序后直接取
            时间复杂度: O(n log n)
            空间复杂度: O(1) 或 O(n)
            
            算法原理:
            1. 对数组进行排序
            2. 返回排序后倒数第k个元素
            
            优势:
            - 简单易懂
            - 适用于所有情况
            
            劣势:
            - 时间复杂度较高
            - 可能需要额外的空间
            
            工程化考量：
            - 使用内置sorted函数进行排序
            - 代码简单，易于理解和维护
            - 适合对时间复杂度要求不严格的场景
            """
            # 输入验证
            if not nums or k < 1 or k > len(nums):
                raise ValueError("Invalid input")
            
            # 使用内置排序函数
            nums_sorted = sorted(nums)
            # 返回倒数第k个元素
            return nums_sorted[-k]
        
        @staticmethod
        def _partition(nums: List[int], left: int, right: int, pivot_index: int) -> int:
            """
            分区操作
            将数组分为小于基准、等于基准和大于基准三部分
            
            Args:
                nums: 数组
                left: 左边界
                right: 右边界
                pivot_index: 基准元素索引
                
            Returns:
                基准元素的最终位置
            """
            # 获取基准元素值
            pivot_value = nums[pivot_index]
            # 将基准元素移到末尾
            nums[pivot_index], nums[right] = nums[right], nums[pivot_index]
            
            # 分区操作
            store_index = left
            for i in range(left, right):
                # 将小于基准的元素移到左侧
                if nums[i] < pivot_value:
                    nums[store_index], nums[i] = nums[i], nums[store_index]
                    store_index += 1
            
            # 将基准元素放到正确位置
            nums[store_index], nums[right] = nums[right], nums[store_index]
            return store_index
        
        @staticmethod
        def test():
            """测试函数"""
            print("=== 第K个最大元素测试 ===")
            
            nums = [3, 2, 1, 5, 6, 4]
            k = 2
            
            print(f"数组: {nums}")
            print(f"k = {k}")
            
            result1 = ExtendedProblems.KthLargestElement.find_kth_largest_quick_select(nums.copy(), k)
            result2 = ExtendedProblems.KthLargestElement.find_kth_largest_min_heap(nums.copy(), k)
            result3 = ExtendedProblems.KthLargestElement.find_kth_largest_sort(nums.copy(), k)
            
            print(f"快速选择结果: {result1}")
            print(f"最小堆结果: {result2}")
            print(f"排序结果: {result3}")
    
    class SortColors:
        """
        题目2: 75. 颜色分类 (荷兰国旗问题)
        来源: LeetCode
        链接: https://leetcode.cn/problems/sort-colors/
        
        相关题目：
        - ALDS1_2_C: Stable Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_C
        """
        
        @staticmethod
        def sort_colors_three_pointers(nums: List[int]) -> None:
            """
            解法1: 三指针法 (荷兰国旗问题)
            时间复杂度: O(n)
            空间复杂度: O(1)
            """
            if len(nums) <= 1:
                return
            
            left, right, current = 0, len(nums) - 1, 0
            
            while current <= right:
                if nums[current] == 0:
                    nums[left], nums[current] = nums[current], nums[left]
                    left += 1
                    current += 1
                elif nums[current] == 2:
                    nums[current], nums[right] = nums[right], nums[current]
                    right -= 1
                else:
                    current += 1
        
        @staticmethod
        def sort_colors_counting(nums: List[int]) -> None:
            """
            解法2: 计数排序
            时间复杂度: O(n)
            空间复杂度: O(1) 因为只有3种颜色
            """
            if len(nums) <= 1:
                return
            
            count = [0, 0, 0]  # 0,1,2的计数
            
            # 统计每种颜色的数量
            for num in nums:
                count[num] += 1
            
            # 重新填充数组
            index = 0
            for color in range(3):
                for _ in range(count[color]):
                    nums[index] = color
                    index += 1
        
        @staticmethod
        def test():
            """测试函数"""
            print("\n=== 颜色分类测试 ===")
            
            nums = [2, 0, 2, 1, 1, 0]
            print(f"原始数组: {nums}")
            
            nums1 = nums.copy()
            ExtendedProblems.SortColors.sort_colors_three_pointers(nums1)
            print(f"三指针法: {nums1}")
            
            nums2 = nums.copy()
            ExtendedProblems.SortColors.sort_colors_counting(nums2)
            print(f"计数排序: {nums2}")
    
    class MergeIntervals:
        """
        题目3: 56. 合并区间
        来源: LeetCode
        链接: https://leetcode.cn/problems/merge-intervals/
        
        相关题目：
        - ALDS1_6_D: Minimum Cost Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_D
        """
        
        @staticmethod
        def merge(intervals: List[List[int]]) -> List[List[int]]:
            """
            解法: 排序+合并
            时间复杂度: O(n log n) 主要来自排序
            空间复杂度: O(n) 存储结果
            """
            if len(intervals) <= 1:
                return intervals
            
            # 按区间起点排序
            intervals.sort(key=lambda x: x[0])
            
            result = []
            current = intervals[0]
            result.append(current)
            
            for interval in intervals:
                current_end = current[1]
                next_start, next_end = interval[0], interval[1]
                
                if current_end >= next_start:  # 有重叠
                    current[1] = max(current_end, next_end)  # 合并
                    result[-1] = current
                else:  # 无重叠
                    current = interval
                    result.append(current)
            
            return result
        
        @staticmethod
        def test():
            """测试函数"""
            print("\n=== 合并区间测试 ===")
            
            intervals = [[1, 3], [2, 6], [8, 10], [15, 18]]
            print(f"原始区间: {intervals}")
            
            result = ExtendedProblems.MergeIntervals.merge(intervals)
            print(f"合并结果: {result}")
    
    class TopKFrequentElements:
        """
        题目4: 347. 前K个高频元素
        来源: LeetCode
        链接: https://leetcode.cn/problems/top-k-frequent-elements/
        """
        
        @staticmethod
        def top_k_frequent_min_heap(nums: List[int], k: int) -> List[int]:
            """
            解法1: 最小堆法 (最优解)
            时间复杂度: O(n log k)
            空间复杂度: O(n)
            """
            if not nums or k <= 0 or k > len(nums):
                return []
            
            # 统计频率
            frequency_map = Counter(nums)
            
            # 最小堆，按频率排序
            min_heap = []
            
            # 保持堆的大小为k
            for num, freq in frequency_map.items():
                heapq.heappush(min_heap, (freq, num))
                if len(min_heap) > k:
                    heapq.heappop(min_heap)
            
            # 提取结果
            result = []
            while min_heap:
                result.append(heapq.heappop(min_heap)[1])
            
            return result[::-1]  # 反转得到频率从高到低
        
        @staticmethod
        def top_k_frequent_bucket_sort(nums: List[int], k: int) -> List[int]:
            """
            解法2: 桶排序
            时间复杂度: O(n)
            空间复杂度: O(n)
            """
            if not nums or k <= 0 or k > len(nums):
                return []
            
            # 统计频率
            frequency_map = Counter(nums)
            
            # 创建桶，索引是频率，值是具有该频率的数字列表
            buckets = [[] for _ in range(len(nums) + 1)]
            for num, freq in frequency_map.items():
                buckets[freq].append(num)
            
            # 从后向前收集k个元素
            result = []
            for i in range(len(buckets) - 1, 0, -1):
                for num in buckets[i]:
                    result.append(num)
                    if len(result) == k:
                        return result
            
            return result
        
        @staticmethod
        def test():
            """测试函数"""
            print("\n=== 前K个高频元素测试 ===")
            
            nums = [1, 1, 1, 2, 2, 3]
            k = 2
            
            print(f"数组: {nums}")
            print(f"k = {k}")
            
            result1 = ExtendedProblems.TopKFrequentElements.top_k_frequent_min_heap(nums, k)
            result2 = ExtendedProblems.TopKFrequentElements.top_k_frequent_bucket_sort(nums, k)
            
            print(f"最小堆结果: {result1}")
            print(f"桶排序结果: {result2}")
    
    class MaximumGap:
        """
        题目5: 164. 最大间距
        来源: LeetCode
        链接: https://leetcode.cn/problems/maximum-gap/
        
        相关题目：
        - ALDS1_5_D: The Number of Inversions: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_D
        """
        
        @staticmethod
        def maximum_gap_radix_sort(nums: List[int]) -> int:
            """
            解法1: 基数排序 (最优解)
            时间复杂度: O(n) - 线性时间
            空间复杂度: O(n)
            """
            if len(nums) < 2:
                return 0
            
            # 基数排序
            ExtendedProblems.MaximumGap._radix_sort(nums)
            
            # 计算最大间距
            max_gap = 0
            for i in range(1, len(nums)):
                max_gap = max(max_gap, nums[i] - nums[i - 1])
            
            return max_gap
        
        @staticmethod
        def maximum_gap_sort(nums: List[int]) -> int:
            """
            解法2: 排序后遍历
            时间复杂度: O(n log n)
            空间复杂度: O(1) 或 O(n)
            """
            if len(nums) < 2:
                return 0
            
            nums_sorted = sorted(nums)
            
            max_gap = 0
            for i in range(1, len(nums_sorted)):
                max_gap = max(max_gap, nums_sorted[i] - nums_sorted[i - 1])
            
            return max_gap
        
        @staticmethod
        def _radix_sort(nums: List[int]) -> None:
            """基数排序实现"""
            if not nums:
                return
            
            # 找出最大值
            max_val = max(nums)
            
            # 按照个位、十位、百位...进行排序
            exp = 1
            while max_val // exp > 0:
                ExtendedProblems.MaximumGap._counting_sort_by_digit(nums, exp)
                exp *= 10
        
        @staticmethod
        def _counting_sort_by_digit(nums: List[int], exp: int) -> None:
            """按指定位数进行计数排序"""
            n = len(nums)
            output = [0] * n
            count = [0] * 10
            
            # 统计每个数字出现的次数
            for i in range(n):
                index = (nums[i] // exp) % 10
                count[index] += 1
            
            # 计算累积计数
            for i in range(1, 10):
                count[i] += count[i - 1]
            
            # 从后向前遍历，保证稳定性
            for i in range(n - 1, -1, -1):
                index = (nums[i] // exp) % 10
                output[count[index] - 1] = nums[i]
                count[index] -= 1
            
            # 复制回原数组
            for i in range(n):
                nums[i] = output[i]
        
        @staticmethod
        def test():
            """测试函数"""
            print("\n=== 最大间距测试 ===")
            
            nums = [3, 6, 9, 1]
            print(f"数组: {nums}")
            
            nums1 = nums.copy()
            result1 = ExtendedProblems.MaximumGap.maximum_gap_radix_sort(nums1)
            print(f"基数排序结果: {result1}")
            
            result2 = ExtendedProblems.MaximumGap.maximum_gap_sort(nums)
            print(f"普通排序结果: {result2}")
    
    @staticmethod
    def run_all_tests():
        """运行所有测试"""
        ExtendedProblems.KthLargestElement.test()
        ExtendedProblems.SortColors.test()
        ExtendedProblems.MergeIntervals.test()
        ExtendedProblems.TopKFrequentElements.test()
        ExtendedProblems.MaximumGap.test()


if __name__ == "__main__":
    ExtendedProblems.run_all_tests()