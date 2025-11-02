#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
排序算法完整实现 - Python版本
包含归并排序、快速排序、堆排序的完整实现和详细注释

时间复杂度分析：
- 归并排序: O(n log n) 平均和最坏情况
- 快速排序: O(n log n) 平均, O(n²) 最坏  
- 堆排序: O(n log n) 平均和最坏情况

空间复杂度分析：
- 归并排序: O(n) 需要辅助数组
- 快速排序: O(log n) 递归栈空间
- 堆排序: O(1) 原地排序

稳定性分析：
- 归并排序: 稳定
- 快速排序: 不稳定
- 堆排序: 不稳定

题目相关:
- 912. 排序数组: https://leetcode.cn/problems/sort-an-array/
- 215. 数组中的第K个最大元素: https://leetcode.cn/problems/kth-largest-element-in-an-array/
- 75. 颜色分类: https://leetcode.cn/problems/sort-colors/
- 56. 合并区间: https://leetcode.cn/problems/merge-intervals/
- 剑指Offer 51. 数组中的逆序对: https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
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

工程化考量：
- 异常处理：对空数组、单元素数组进行特殊处理
- 边界条件：处理各种边界情况，如已排序、逆序、全相同数组
- 性能优化：
  - 快速排序的小数组优化（长度小于16时使用插入排序）
  - 三数取中法选择基准值避免最坏情况
  - 归并排序使用辅助数组避免频繁创建销毁
- 稳定性：归并排序保证稳定性，快速排序和堆排序不稳定
- 可读性：清晰的函数命名和详细注释

算法选择建议：
- 数据量小（n < 50）：插入排序
- 需要稳定排序：归并排序
- 一般情况：快速排序（带优化）
- 内存受限：堆排序
- 最坏情况要求：堆排序
"""

import random
import time
import sys
from typing import List, Tuple

class SortAlgorithms:
    """
    排序算法类，包含多种经典排序算法的实现
    
    工程化特性：
    1. 所有方法都是静态方法，便于直接调用
    2. 包含详细的输入验证和异常处理
    3. 提供性能测试和正确性验证
    4. 支持稳定性分析和演示
    """
    
    @staticmethod
    def merge_sort(arr: List[int]) -> None:
        """
        归并排序主函数
        时间复杂度: O(n log n) - 在所有情况下都是这个复杂度，包括最好、平均和最坏情况
        空间复杂度: O(n) - 需要一个与原数组相同大小的辅助数组
        稳定性: 稳定 - 相等元素的相对位置在排序后不会改变
        
        算法原理：
        1. 分治法：将数组不断二分直到只有一个元素
        2. 合并：将两个有序数组合并成一个有序数组
        3. 递归处理：自底向上构建有序数组
        
        适用场景：
        - 需要稳定排序
        - 链表排序
        - 外部排序（数据量大无法全部加载到内存）
        
        相关题目：
        - ALDS1_5_B: Merge Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_B
        - ALDS1_5_D: The Number of Inversions: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_D
        
        工程化考量：
        - 对于小数组（长度小于16），可考虑使用插入排序优化
        - 可以使用迭代版本避免递归栈溢出
        - 可以复用辅助数组避免频繁创建销毁
        
        Args:
            arr: 待排序数组
        """
        # 边界条件检查：空数组或单元素数组无需排序
        if len(arr) <= 1:
            return
        
        # 创建辅助数组用于归并操作，避免在递归中频繁创建销毁
        helper = [0] * len(arr)
        SortAlgorithms._merge_sort(arr, 0, len(arr) - 1, helper)
    
    @staticmethod
    def _merge_sort(arr: List[int], left: int, right: int, helper: List[int]) -> None:
        """
        归并排序递归实现
        核心思想：分治法，将数组分成两半分别排序，然后合并
        
        Args:
            arr: 待排序数组
            left: 左边界
            right: 右边界
            helper: 辅助数组
        """
        # 递归终止条件：子数组只有一个元素或为空
        if left >= right:
            return
        
        # 计算中点，避免整数溢出
        mid = left + (right - left) // 2
        
        # 递归排序左半部分
        SortAlgorithms._merge_sort(arr, left, mid, helper)
        # 递归排序右半部分
        SortAlgorithms._merge_sort(arr, mid + 1, right, helper)
        # 合并两个有序数组
        SortAlgorithms._merge(arr, left, mid, right, helper)
    
    @staticmethod
    def _merge(arr: List[int], left: int, mid: int, right: int, helper: List[int]) -> None:
        """
        合并两个有序数组
        关键步骤：双指针合并，保证稳定性
        
        Args:
            arr: 原数组
            left: 左边界
            mid: 中间位置
            right: 右边界
            helper: 辅助数组
        """
        # 复制数据到辅助数组，避免在合并过程中覆盖未处理的数据
        for i in range(left, right + 1):
            helper[i] = arr[i]
        
        # 初始化三个指针：
        # i: 左半部分指针
        # j: 右半部分指针
        # k: 原数组指针
        i, j, k = left, mid + 1, left
        
        # 合并两个有序数组
        while i <= mid and j <= right:
            # 相等时取左边的元素，保证稳定性
            if helper[i] <= helper[j]:
                arr[k] = helper[i]
                i += 1
            else:
                arr[k] = helper[j]
                j += 1
            k += 1
        
        # 处理剩余元素
        # 左半部分剩余元素
        while i <= mid:
            arr[k] = helper[i]
            i += 1
            k += 1
        
        # 右半部分剩余元素
        while j <= right:
            arr[k] = helper[j]
            j += 1
            k += 1
    
    @staticmethod
    def quick_sort(arr: List[int]) -> None:
        """
        快速排序主函数
        时间复杂度: 平均O(n log n)，最坏O(n²)
        空间复杂度: O(log n) 平均情况下的递归栈空间，最坏O(n)
        稳定性: 不稳定 - 元素的相对位置可能改变
        
        算法原理：
        1. 分治法：选取基准值，将数组分成两部分
        2. 递归处理：对左右两部分分别进行快速排序
        3. 合并：不需要额外合并操作
        
        适用场景：
        - 一般情况下的排序
        - 数据量较大
        
        相关题目：
        - ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
        - ALDS1_6_C: Quick Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_C
        
        工程化考量：
        - 小数组优化：当数组长度较小时使用插入排序
        - 三数取中法选择基准值，避免最坏情况
        - 随机化基准选择也可避免最坏情况
        - 可以使用迭代版本避免递归栈溢出
        
        Args:
            arr: 待排序数组
        """
        # 边界条件检查：空数组或单元素数组无需排序
        if len(arr) <= 1:
            return
        
        SortAlgorithms._quick_sort(arr, 0, len(arr) - 1)
    
    @staticmethod
    def _quick_sort(arr: List[int], left: int, right: int) -> None:
        """
        快速排序递归实现
        核心思想：分治法，选取基准值，将数组分成两部分
        
        Args:
            arr: 待排序数组
            left: 左边界
            right: 右边界
        """
        # 递归终止条件
        if left >= right:
            return
        
        # 小数组优化：当数组长度较小时使用插入排序
        if right - left + 1 < 16:
            SortAlgorithms._insertion_sort(arr, left, right)
            return
        
        # 三数取中法选择基准值，避免最坏情况
        pivot = SortAlgorithms._median_of_three(arr, left, right)
        
        # 分区操作
        equal_left, equal_right = SortAlgorithms._partition(arr, left, right, pivot)
        
        # 递归排序左右部分
        SortAlgorithms._quick_sort(arr, left, equal_left - 1)
        SortAlgorithms._quick_sort(arr, equal_right + 1, right)
    
    @staticmethod
    def _median_of_three(arr: List[int], left: int, right: int) -> int:
        """
        三数取中法选择基准值
        优化策略：避免快速排序的最坏情况
        
        Args:
            arr: 数组
            left: 左边界
            right: 右边界
            
        Returns:
            基准值
        """
        mid = left + (right - left) // 2
        
        # 对左中右三个数排序
        if arr[left] > arr[mid]:
            SortAlgorithms._swap(arr, left, mid)
        if arr[left] > arr[right]:
            SortAlgorithms._swap(arr, left, right)
        if arr[mid] > arr[right]:
            SortAlgorithms._swap(arr, mid, right)
        
        # 将中间值放到right-1位置，作为基准值
        SortAlgorithms._swap(arr, mid, right - 1)
        return arr[right - 1]
    
    @staticmethod
    def _partition(arr: List[int], left: int, right: int, pivot: int) -> Tuple[int, int]:
        """
        快速排序分区操作
        荷兰国旗问题变种：将数组分成小于、等于、大于基准值的三部分
        
        Args:
            arr: 数组
            left: 左边界
            right: 右边界
            pivot: 基准值
            
        Returns:
            等于区域的左右边界
        """
        less = left - 1        # 小于区域右边界
        more = right           # 大于区域左边界
        i = left               # 当前指针
        
        while i < more:
            if arr[i] < pivot:
                less += 1
                SortAlgorithms._swap(arr, less, i)
                i += 1
            elif arr[i] > pivot:
                more -= 1
                SortAlgorithms._swap(arr, more, i)
            else:
                i += 1
        
        # 将基准值放回等于区域
        SortAlgorithms._swap(arr, more, right - 1)
        
        return less + 1, more
    
    @staticmethod
    def _insertion_sort(arr: List[int], left: int, right: int) -> None:
        """
        插入排序（用于小数组优化）
        时间复杂度: O(n²) 但常数项很小
        
        Args:
            arr: 数组
            left: 左边界
            right: 右边界
        """
        for i in range(left + 1, right + 1):
            key = arr[i]
            j = i - 1
            
            # 将大于key的元素向后移动
            while j >= left and arr[j] > key:
                arr[j + 1] = arr[j]
                j -= 1
            arr[j + 1] = key
    
    @staticmethod
    def heap_sort(arr: List[int]) -> None:
        """
        堆排序主函数
        时间复杂度: O(n log n) - 在所有情况下都是这个复杂度
        空间复杂度: O(1) - 原地排序算法
        稳定性: 不稳定 - 元素的相对位置可能改变
        
        算法原理：
        1. 构建最大堆：从最后一个非叶子节点开始
        2. 逐个提取堆顶元素
        3. 重新堆化：将剩余元素重新构建成最大堆
        
        适用场景：
        - 内存受限
        - 最坏情况要求
        
        相关题目：
        - ALDS1_9_A: Complete Binary Tree: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_A
        - ALDS1_9_B: Maximum Heap: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_B
        - ALDS1_9_C: Priority Queue: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_C
        
        工程化考量：
        - 原地排序，内存使用效率高
        - 最坏情况时间复杂度有保证
        - 常数因子相对较大，实际性能可能不如快速排序
        
        Args:
            arr: 待排序数组
        """
        # 边界条件检查：空数组或单元素数组无需排序
        if len(arr) <= 1:
            return
        
        n = len(arr)
        
        # 构建最大堆：从最后一个非叶子节点开始
        # 最后一个非叶子节点的索引是 n//2 - 1
        for i in range(n // 2 - 1, -1, -1):
            SortAlgorithms._heapify(arr, n, i)
        
        # 逐个提取堆顶元素
        for i in range(n - 1, 0, -1):
            # 将堆顶元素（最大值）与当前末尾元素交换
            SortAlgorithms._swap(arr, 0, i)
            # 对剩余元素重新堆化，堆大小减1
            SortAlgorithms._heapify(arr, i, 0)
    
    @staticmethod
    def _heapify(arr: List[int], n: int, i: int) -> None:
        """
        堆化操作：维护最大堆性质
        核心思想：下沉操作，确保父节点大于等于子节点
        
        Args:
            arr: 数组
            n: 堆大小
            i: 当前节点索引
        """
        largest = i        # 假设当前节点最大
        left = 2 * i + 1   # 左子节点
        right = 2 * i + 2  # 右子节点
        
        # 比较左子节点
        if left < n and arr[left] > arr[largest]:
            largest = left
        
        # 比较右子节点
        if right < n and arr[right] > arr[largest]:
            largest = right
        
        # 如果最大值不是当前节点，需要交换并继续堆化
        if largest != i:
            SortAlgorithms._swap(arr, i, largest)
            SortAlgorithms._heapify(arr, n, largest)
    
    @staticmethod
    def _swap(arr: List[int], i: int, j: int) -> None:
        """
        交换数组元素
        基础操作，但要注意边界检查
        
        Args:
            arr: 数组
            i: 索引1
            j: 索引2
        """
        if i == j:  # 相同索引不需要交换
            return
        
        arr[i], arr[j] = arr[j], arr[i]
    
    @staticmethod
    def generate_random_array(size: int) -> List[int]:
        """
        生成随机测试数组
        
        Args:
            size: 数组大小
            
        Returns:
            随机数组
        """
        return [random.randint(0, size * 10) for _ in range(size)]
    
    @staticmethod
    def is_sorted(arr: List[int]) -> bool:
        """
        检查数组是否已排序
        
        Args:
            arr: 数组
            
        Returns:
            是否已排序
        """
        for i in range(1, len(arr)):
            if arr[i] < arr[i - 1]:
                return False
        return True
    
    @staticmethod
    def print_array(arr: List[int], name: str = "") -> None:
        """
        打印数组
        
        Args:
            arr: 数组
            name: 数组名称
        """
        if name:
            print(f"{name}: ", end="")
        print(arr)
    
    @staticmethod
    def test_sort_algorithms() -> None:
        """
        测试函数：验证排序算法的正确性
        包含边界测试、性能测试、稳定性测试
        """
        print("=== 排序算法测试开始 ===")
        
        # 测试用例设计
        test_cases = [
            [],                            # 空数组
            [1],                           # 单元素
            [1, 2, 3],                     # 已排序
            [3, 2, 1],                     # 逆序
            [1, 1, 1],                     # 全相同
            [5, 2, 8, 1, 9],              # 普通情况
            [3, 1, 4, 1, 5, 9, 2, 6]      # 重复元素
        ]
        
        algorithms = ["归并排序", "快速排序", "堆排序"]
        algorithm_funcs = [
            SortAlgorithms.merge_sort,
            SortAlgorithms.quick_sort,
            SortAlgorithms.heap_sort
        ]
        
        for i, test_case in enumerate(test_cases):
            print(f"\n测试用例 {i + 1}: {test_case}")
            
            for j, (algo_name, algo_func) in enumerate(zip(algorithms, algorithm_funcs)):
                arr = test_case.copy()
                expected = sorted(test_case)  # 使用内置排序作为基准
                
                algo_func(arr)
                
                correct = arr == expected
                print(f"{algo_name}: {arr} - {'✓' if correct else '✗'}")
                
                if not correct:
                    print(f"预期: {expected}")
        
        # 性能测试
        SortAlgorithms.performance_test()
        
        print("=== 排序算法测试结束 ===")
    
    @staticmethod
    def performance_test() -> None:
        """
        性能测试：比较不同排序算法在大数据量下的表现
        """
        print("\n=== 性能测试 ===")
        
        size = 10000
        data = SortAlgorithms.generate_random_array(size)
        
        algorithms = ["归并排序", "快速排序", "堆排序"]
        algorithm_funcs = [
            SortAlgorithms.merge_sort,
            SortAlgorithms.quick_sort,
            SortAlgorithms.heap_sort
        ]
        
        for algo_name, algo_func in zip(algorithms, algorithm_funcs):
            test_data = data.copy()
            start_time = time.time()
            
            algo_func(test_data)
            
            end_time = time.time()
            duration = (end_time - start_time) * 1000  # 转换为毫秒
            
            print(f"{algo_name}: {duration:.2f} ms")
            
            # 验证排序正确性
            correct = SortAlgorithms.is_sorted(test_data)
            print(f"  排序正确性: {'✓' if correct else '✗'}")
    
    @staticmethod
    def demonstrate_stability() -> None:
        """
        演示排序算法的稳定性
        稳定性：相等元素的相对顺序在排序后保持不变
        """
        print("\n=== 稳定性测试 ===")
        
        # 创建包含重复元素的测试数据，每个元素包含值和原始索引
        test_data = [
            (3, 'a'), (1, 'b'), (2, 'c'), (1, 'd'), (3, 'e'), (2, 'f')
        ]
        
        print("原始数据:", test_data)
        
        # 测试归并排序的稳定性
        merge_data = test_data.copy()
        # 使用归并排序，只比较第一个元素（数值）
        merge_data.sort(key=lambda x: x[0])  # Python内置的归并排序是稳定的
        print("稳定排序结果:", merge_data)
        
        # 演示不稳定排序可能的结果
        print("注意：快速排序和堆排序是不稳定的")
        print("不稳定排序可能的结果示例:")
        unstable_example = [
            (1, 'b'), (1, 'd'), (2, 'c'), (2, 'f'), (3, 'a'), (3, 'e')
        ]
        print("稳定结果:", unstable_example)
        
        # 可能的不稳定结果（相对顺序改变）
        unstable_possible = [
            (1, 'd'), (1, 'b'), (2, 'f'), (2, 'c'), (3, 'e'), (3, 'a')
        ]
        print("不稳定结果:", unstable_possible)


def main():
    """
    主函数：演示排序算法的使用
    """
    # 基础功能演示
    arr = [64, 34, 25, 12, 22, 11, 90]
    print("原始数组:", arr)
    
    # 测试不同排序算法
    arr1 = arr.copy()
    SortAlgorithms.merge_sort(arr1)
    print("归并排序:", arr1)
    
    arr2 = arr.copy()
    SortAlgorithms.quick_sort(arr2)
    print("快速排序:", arr2)
    
    arr3 = arr.copy()
    SortAlgorithms.heap_sort(arr3)
    print("堆排序:  ", arr3)
    
    # 运行完整测试套件，包括基础算法和扩展题目
    run_all_tests_with_extended()


# ==============================
# 扩展题目实现 - Python版本
# ==============================

class ExtendedProblems:
    """
    排序相关的扩展题目实现
    """
    
    @staticmethod
    def sort_array(nums):
        """
        题目1: 912. 排序数组
        来源: LeetCode
        链接: https://leetcode.cn/problems/sort-an-array/
        
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        是否最优解: 是
        
        工程化考量：
        - 根据数据规模选择合适的排序算法
        - 可以结合多种算法的优点进行优化
        """
        if not nums or len(nums) <= 1:
            return nums
        
        # 使用快速排序进行排序
        SortAlgorithms.quick_sort(nums)
        return nums
    
    @staticmethod
    def quick_select(nums, left, right, k):
        """
        快速选择算法
        用于找第K个元素
        """
        if left == right:
            return nums[left]
        
        # 随机选择pivot
        import random
        pivot_index = random.randint(left, right)
        nums[pivot_index], nums[right] = nums[right], nums[pivot_index]
        
        pivot = nums[right]
        i = left
        for j in range(left, right):
            if nums[j] < pivot:
                nums[i], nums[j] = nums[j], nums[i]
                i += 1
        nums[i], nums[right] = nums[right], nums[i]
        
        if i == k:
            return nums[i]
        elif i < k:
            return ExtendedProblems.quick_select(nums, i + 1, right, k)
        else:
            return ExtendedProblems.quick_select(nums, left, i - 1, k)
    
    @staticmethod
    def find_kth_largest(nums, k):
        """
        题目2: 215. 数组中的第K个最大元素
        来源: LeetCode
        链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
        
        使用快速选择算法
        时间复杂度: O(n) 平均情况
        空间复杂度: O(1)
        是否最优解: 是
        """
        if not nums or k < 1 or k > len(nums):
            raise ValueError("Invalid input")
        
        return ExtendedProblems.quick_select(nums, 0, len(nums) - 1, len(nums) - k)
    
    @staticmethod
    def sort_colors(nums):
        """
        题目3: 75. 颜色分类 (荷兰国旗问题)
        来源: LeetCode
        链接: https://leetcode.cn/problems/sort-colors/
        
        三指针法
        时间复杂度: O(n)
        空间复杂度: O(1)
        是否最优解: 是
        """
        if not nums or len(nums) <= 1:
            return
        
        p0 = 0              # 下一个0的位置
        curr = 0            # 当前指针
        p2 = len(nums) - 1  # 下一个2的位置
        
        while curr <= p2:
            if nums[curr] == 0:
                nums[curr], nums[p0] = nums[p0], nums[curr]
                p0 += 1
                curr += 1
            elif nums[curr] == 2:
                nums[curr], nums[p2] = nums[p2], nums[curr]
                p2 -= 1
            else:
                curr += 1
    
    @staticmethod
    def merge_intervals(intervals):
        """
        题目4: 56. 合并区间
        来源: LeetCode
        链接: https://leetcode.cn/problems/merge-intervals/
        
        相关题目：
        - ALDS1_6_D: Minimum Cost Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_D
        
        时间复杂度: O(n log n)
        空间复杂度: O(log n)
        是否最优解: 是
        """
        if not intervals or len(intervals) <= 1:
            return intervals
        
        # 按起始位置排序
        intervals.sort(key=lambda x: x[0])
        
        result = [intervals[0]]
        
        for i in range(1, len(intervals)):
            interval = intervals[i]
            last = result[-1]
            
            if interval[0] <= last[1]:
                # 重叠，合并
                last[1] = max(last[1], interval[1])
            else:
                # 不重叠，添加新区间
                result.append(interval)
        
        return result
    
    @staticmethod
    def merge_and_count(nums, left, mid, right, temp):
        """
        合并并统计逆序对
        """
        for i in range(left, right + 1):
            temp[i] = nums[i]
        
        i, j, k = left, mid + 1, left
        count = 0
        
        while i <= mid and j <= right:
            if temp[i] <= temp[j]:
                nums[k] = temp[i]
                i += 1
            else:
                count += (mid - i + 1)
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
        
        return count
    
    @staticmethod
    def merge_count_pairs(nums, left, right, temp):
        """
        递归统计逆序对
        """
        if left >= right:
            return 0
        
        mid = left + (right - left) // 2
        count = 0
        
        count += ExtendedProblems.merge_count_pairs(nums, left, mid, temp)
        count += ExtendedProblems.merge_count_pairs(nums, mid + 1, right, temp)
        count += ExtendedProblems.merge_and_count(nums, left, mid, right, temp)
        
        return count
    
    @staticmethod
    def reverse_pairs(nums):
        """
        题目6: 剑指Offer 51. 数组中的逆序对
        来源: LeetCode
        链接: https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
        
        相关题目：
        - ALDS1_5_D: The Number of Inversions: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_D
        
        使用归并排序统计逆序对
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        是否最优解: 是
        """
        if not nums or len(nums) <= 1:
            return 0
        
        temp = [0] * len(nums)
        return ExtendedProblems.merge_count_pairs(nums, 0, len(nums) - 1, temp)
    
    @staticmethod
    def test():
        """
        测试所有扩展题目
        """
        print("\n=== 扩展题目测试 ===")
        
        # 测试题目1: 排序数组
        print("\n题目1: 排序数组")
        arr1 = [5, 2, 3, 1]
        print(f"输入: {arr1}")
        ExtendedProblems.sort_array(arr1)
        print(f"输出: {arr1}")
        
        # 测试题目2: 第K个最大元素
        print("\n题目2: 第K个最大元素")
        arr2 = [3, 2, 1, 5, 6, 4]
        k = 2
        print(f"输入: {arr2}, k={k}")
        result2 = ExtendedProblems.find_kth_largest(arr2.copy(), k)
        print(f"输出: {result2}")
        
        # 测试题目3: 颜色分类
        print("\n题目3: 颜色分类")
        arr3 = [2, 0, 2, 1, 1, 0]
        print(f"输入: {arr3}")
        ExtendedProblems.sort_colors(arr3)
        print(f"输出: {arr3}")
        
        # 测试题目4: 合并区间
        print("\n题目4: 合并区间")
        intervals = [[1, 3], [2, 6], [8, 10], [15, 18]]
        print(f"输入: {intervals}")
        merged = ExtendedProblems.merge_intervals(intervals)
        print(f"输出: {merged}")
        
        # 测试题目6: 逆序对
        print("\n题目6: 数组中的逆序对")
        arr6 = [7, 5, 6, 4]
        print(f"输入: {arr6}")
        result6 = ExtendedProblems.reverse_pairs(arr6.copy())
        print(f"逆序对数量: {result6}")


# ==============================
# 扩展题目实现 - Python版本
# ==============================

class MergeKSortedLists:
    """
    题目6: 23. 合并K个排序链表
    来源: LeetCode
    链接: https://leetcode.cn/problems/merge-k-sorted-lists/
    
    解法分析:
    1. 优先队列法 (最优解) - 时间复杂度: O(n log k), 空间复杂度: O(k)
    2. 分治法 - 时间复杂度: O(n log k), 空间复杂度: O(log k)
    """
    
    class ListNode:
        def __init__(self, val=0, next=None):
            self.val = val
            self.next = next
    
    @staticmethod
    def merge_k_lists_priority_queue(lists):
        """
        解法1: 优先队列法
        时间复杂度: O(n log k) - n是所有节点总数，k是链表数量
        空间复杂度: O(k) - 优先队列大小
        """
        if not lists:
            return None
        
        import heapq
        
        # 优先队列
        min_heap = []
        
        # 将所有链表头节点加入优先队列
        for i, head in enumerate(lists):
            if head:
                # 使用元组(值, 索引, 节点)以处理相同值的情况
                heapq.heappush(min_heap, (head.val, i, head))
        
        # 虚拟头节点
        dummy = MergeKSortedLists.ListNode(0)
        current = dummy
        
        # 依次取出最小节点
        while min_heap:
            val, i, node = heapq.heappop(min_heap)
            current.next = node
            current = current.next
            
            # 如果有后续节点，加入优先队列
            if node.next:
                heapq.heappush(min_heap, (node.next.val, i, node.next))
        
        return dummy.next
    
    @staticmethod
    def merge_two_lists(l1, l2):
        """
        合并两个有序链表
        """
        dummy = MergeKSortedLists.ListNode(0)
        current = dummy
        
        while l1 and l2:
            if l1.val <= l2.val:
                current.next = l1
                l1 = l1.next
            else:
                current.next = l2
                l2 = l2.next
            current = current.next
        
        current.next = l1 if l1 else l2
        return dummy.next
    
    @staticmethod
    def merge_k_lists_helper(lists, left, right):
        """
        分治辅助函数
        """
        if left == right:
            return lists[left]
        if left + 1 == right:
            return MergeKSortedLists.merge_two_lists(lists[left], lists[right])
        
        mid = left + (right - left) // 2
        l1 = MergeKSortedLists.merge_k_lists_helper(lists, left, mid)
        l2 = MergeKSortedLists.merge_k_lists_helper(lists, mid + 1, right)
        
        return MergeKSortedLists.merge_two_lists(l1, l2)
    
    @staticmethod
    def merge_k_lists_divide_and_conquer(lists):
        """
        解法2: 分治法
        时间复杂度: O(n log k)
        空间复杂度: O(log k) - 递归栈空间
        """
        if not lists:
            return None
        return MergeKSortedLists.merge_k_lists_helper(lists, 0, len(lists) - 1)
    
    @staticmethod
    def print_list(head):
        """
        打印链表
        """
        result = []
        current = head
        while current:
            result.append(str(current.val))
            current = current.next
        print(' '.join(result))
    
    @staticmethod
    def test():
        """
        测试函数
        """
        print("\n=== 合并K个排序链表测试 ===")
        
        # 创建测试数据
        l1 = MergeKSortedLists.ListNode(1)
        l1.next = MergeKSortedLists.ListNode(4)
        l1.next.next = MergeKSortedLists.ListNode(5)
        
        l2 = MergeKSortedLists.ListNode(1)
        l2.next = MergeKSortedLists.ListNode(3)
        l2.next.next = MergeKSortedLists.ListNode(4)
        
        l3 = MergeKSortedLists.ListNode(2)
        l3.next = MergeKSortedLists.ListNode(6)
        
        lists = [l1, l2, l3]
        
        # 测试优先队列法
        result1 = MergeKSortedLists.merge_k_lists_priority_queue(lists)
        print("优先队列法结果:", end=" ")
        MergeKSortedLists.print_list(result1)


class MaximumGap:
    """
    题目7: 164. 最大间距
    来源: LeetCode
    链接: https://leetcode.cn/problems/maximum-gap/
    
    解法分析:
    1. 基数排序 (最优解) - 时间复杂度: O(n), 空间复杂度: O(n)
    2. 排序后遍历 - 时间复杂度: O(n log n)
    """
    
    @staticmethod
    def counting_sort_by_digit(nums, exp):
        """
        按位计数排序
        """
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
        
        # 复制到原数组
        for i in range(n):
            nums[i] = output[i]
    
    @staticmethod
    def radix_sort(nums):
        """
        基数排序
        """
        if not nums:
            return
        
        # 找出最大值
        max_val = max(nums)
        
        # 按位排序
        exp = 1
        while max_val // exp > 0:
            MaximumGap.counting_sort_by_digit(nums, exp)
            exp *= 10
    
    @staticmethod
    def maximum_gap_radix_sort(nums):
        """
        解法1: 基数排序
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        if len(nums) < 2:
            return 0
        
        # 基数排序
        sorted_nums = nums.copy()
        MaximumGap.radix_sort(sorted_nums)
        
        # 计算最大间距
        max_gap = 0
        for i in range(1, len(sorted_nums)):
            max_gap = max(max_gap, sorted_nums[i] - sorted_nums[i - 1])
        
        return max_gap
    
    @staticmethod
    def maximum_gap_sort(nums):
        """
        解法2: 排序后遍历
        时间复杂度: O(n log n)
        """
        if len(nums) < 2:
            return 0
        
        sorted_nums = sorted(nums)
        
        max_gap = 0
        for i in range(1, len(sorted_nums)):
            max_gap = max(max_gap, sorted_nums[i] - sorted_nums[i - 1])
        
        return max_gap
    
    @staticmethod
    def test():
        """
        测试函数
        """
        print("\n=== 最大间距测试 ===")
        
        nums = [3, 6, 9, 1]
        print(f"数组: {nums}")
        
        result1 = MaximumGap.maximum_gap_radix_sort(nums)
        result2 = MaximumGap.maximum_gap_sort(nums)
        
        print(f"基数排序结果: {result1}")
        print(f"普通排序结果: {result2}")


class TopKFrequentElements:
    """
    题目8: 347. 前K个高频元素
    来源: LeetCode
    链接: https://leetcode.cn/problems/top-k-frequent-elements/
    
    解法分析:
    1. 最小堆法 (最优解) - 时间复杂度: O(n log k), 空间复杂度: O(n)
    2. 桶排序 - 时间复杂度: O(n), 空间复杂度: O(n)
    """
    
    @staticmethod
    def top_k_frequent_min_heap(nums, k):
        """
        解法1: 最小堆
        时间复杂度: O(n log k)
        空间复杂度: O(n)
        """
        if not nums or k <= 0 or k > len(nums):
            return []
        
        # 统计频率
        frequency_map = {}
        for num in nums:
            frequency_map[num] = frequency_map.get(num, 0) + 1
        
        # 最小堆
        import heapq
        min_heap = []
        
        # 保持堆的大小为k
        for num, freq in frequency_map.items():
            heapq.heappush(min_heap, (freq, num))
            if len(min_heap) > k:
                heapq.heappop(min_heap)
        
        # 提取结果
        result = [0] * k
        for i in range(k - 1, -1, -1):
            result[i] = heapq.heappop(min_heap)[1]
        
        return result
    
    @staticmethod
    def top_k_frequent_bucket_sort(nums, k):
        """
        解法2: 桶排序
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        if not nums or k <= 0 or k > len(nums):
            return []
        
        # 统计频率
        frequency_map = {}
        for num in nums:
            frequency_map[num] = frequency_map.get(num, 0) + 1
        
        # 创建桶
        n = len(nums)
        buckets = [[] for _ in range(n + 1)]
        
        for num, freq in frequency_map.items():
            buckets[freq].append(num)
        
        # 从后向前收集k个元素
        result = []
        for i in range(n, -1, -1):
            if buckets[i]:
                for num in buckets[i]:
                    result.append(num)
                    if len(result) == k:
                        return result
        
        return result
    
    @staticmethod
    def test():
        """
        测试函数
        """
        print("\n=== 前K个高频元素测试 ===")
        
        nums = [1, 1, 1, 2, 2, 3]
        k = 2
        
        print(f"数组: {nums}")
        print(f"k = {k}")
        
        result1 = TopKFrequentElements.top_k_frequent_min_heap(nums, k)
        result2 = TopKFrequentElements.top_k_frequent_bucket_sort(nums, k)
        
        print(f"最小堆结果: {result1}")
        print(f"桶排序结果: {result2}")


# 添加新的测试函数

def run_extended_tests():
    """
    运行所有扩展题目的测试
    """
    MergeKSortedLists.test()
    MaximumGap.test()
    TopKFrequentElements.test()
    ExtendedProblems.test()


def run_all_tests_with_extended():
    """
    运行所有测试，包括基础算法和扩展题目
    """
    print("===== 基础排序算法测试 =====")
    # 运行原有的测试
    SortAlgorithms.test_sort_algorithms()
    SortAlgorithms.demonstrate_stability()
    
    print("\n===== 扩展题目测试 =====")
    run_extended_tests()


if __name__ == "__main__":
    main()