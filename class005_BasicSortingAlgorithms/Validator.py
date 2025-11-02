#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
选择排序、冒泡排序、插入排序的验证与扩展练习

选择排序(Selection Sort):
- 工作原理：每次从未排序的部分中找到最小元素，放到已排序部分的末尾
- 时间复杂度：O(n²) - 最好、平均、最坏情况都相同
- 空间复杂度：O(1) - 原地排序
- 稳定性：不稳定
- 适用场景：数据量小且对稳定性无要求

冒泡排序(Bubble Sort):
- 工作原理：相邻元素两两比较，如果顺序错误就交换，每轮将最大元素"冒泡"到末尾
- 时间复杂度：O(n²) - 最坏和平均情况，O(n) - 最好情况(已排序)
- 空间复杂度：O(1) - 原地排序
- 稳定性：稳定
- 适用场景：数据量小且要求稳定性

插入排序(Insertion Sort):
- 工作原理：将未排序元素插入到已排序序列的适当位置
- 时间复杂度：O(n²) - 最坏情况，O(n) - 最好情况(已排序)
- 空间复杂度：O(1) - 原地排序
- 稳定性：稳定
- 适用场景：小规模数据或基本有序的数据

相关题目:
1. LintCode 463. Sort Integers - https://www.lintcode.com/problem/sort-integers/
   题目描述：给定一个整数数组，使用选择排序、冒泡排序或插入排序等O(n²)算法将其按升序排序
   示例：
   输入: [3, 2, 1, 4, 5]
   输出: [1, 2, 3, 4, 5]
   
2. LeetCode 912. Sort an Array - https://leetcode.cn/problems/sort-an-array/
   题目描述：给定一个整数数组，将其按升序排序
   示例：
   输入: [5,2,3,1]
   输出: [1,2,3,5]
   
3. 牛客网 - 最小的K个数 - https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
   题目描述：输入n个整数，找出其中最小的K个数
   
4. LintCode 464. Sort Integers II - https://www.lintcode.com/problem/sort-integers-ii/
   题目描述：给定一个整数数组，使用快速排序、归并排序、堆排序等O(nlogn)算法将其按升序排序
   示例：
   输入: [3, 2, 1, 4, 5]
   输出: [1, 2, 3, 4, 5]
   
5. LeetCode 75. Sort Colors - https://leetcode.cn/problems/sort-colors/
   题目描述：给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums，
            原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列
   示例：
   输入: [2,0,2,1,1,0]
   输出: [0,0,1,1,2,2]
   
6. LeetCode 215. Kth Largest Element in an Array - https://leetcode.cn/problems/kth-largest-element-in-an-array/
   题目描述：给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素
   示例：
   输入: [3,2,1,5,6,4], k = 2
   输出: 5
"""


import random
import time
import copy


def swap(arr, i, j):
    """
    数组中交换i和j位置的数
    
    Args:
        arr: 数组
        i: 位置i
        j: 位置j
    """
    arr[i], arr[j] = arr[j], arr[i]


def selection_sort(arr):
    """
    选择排序 - Selection Sort
    时间复杂度: O(n²) - 无论什么情况都需要进行n(n-1)/2次比较
    空间复杂度: O(1) - 只使用了常数级别的额外空间
    稳定性: 不稳定 - 相等元素的相对位置可能改变
    
    算法思路：
    1. 在未排序序列中找到最小元素
    2. 将其与未排序序列的第一个元素交换位置
    3. 重复步骤1-2，直到所有元素排序完成

    优点：
    - 实现简单
    - 原地排序，空间复杂度低
    - 交换次数少，最多进行n-1次交换

    缺点：
    - 时间复杂度高，不适合大数据量
    - 不稳定
    - 无法利用数据的有序性优化

    适用场景：
    - 数据量小的情况
    - 对内存使用要求严格的场景
    - 不要求稳定性的场景
    
    Args:
        arr: 待排序数组
    """
    # 边界检查：空数组或单元素数组无需排序
    if arr is None or len(arr) < 2:
        return
    
    # 外层循环控制排序的轮数，需要进行n-1轮
    # 每轮都会确定一个元素的最终位置（当前未排序部分的最小元素）
    for i in range(len(arr) - 1):
        # 假设当前位置i就是未排序部分的最小值位置
        # 从当前位置开始，在未排序部分[i, n-1]中寻找真正的最小值
        min_index = i
        
        # 内层循环在未排序部分[i+1, n-1]中寻找真正的最小值
        # j从i+1开始，因为位置i已经是当前假设的最小值位置
        for j in range(i + 1, len(arr)):
            # 如果找到更小的元素，更新最小值索引
            # 这里使用<而不是<=是为了保持算法的不稳定性
            if arr[j] < arr[min_index]:
                min_index = j
        
        # 如果最小值不在当前位置，则交换
        # 这样可以减少不必要的交换操作（当min_index == i时不需要交换）
        if min_index != i:
            swap(arr, i, min_index)


def bubble_sort(arr):
    """
    冒泡排序 - Bubble Sort
    时间复杂度: O(n²) - 最坏和平均情况，O(n) - 最好情况(已排序)
    空间复杂度: O(1) - 只使用了常数级别的额外空间
    稳定性: 稳定 - 相等元素不会交换位置
    
    算法思路：
    1. 比较相邻的两个元素，如果前面的比后面的大就交换
    2. 每一轮都会将当前未排序部分的最大元素"冒泡"到末尾
    3. 重复步骤1-2，直到所有元素排序完成

    优点：
    - 实现简单，容易理解
    - 稳定排序
    - 原地排序
    - 能够检测数组是否已经有序

    缺点：
    - 时间复杂度高，不适合大数据量
    - 元素交换次数多

    优化：
    - 设置标志位，如果某一轮没有发生交换，说明数组已经有序，可以提前结束

    适用场景：
    - 数据量小的情况
    - 要求稳定性的场景
    - 教学演示
    
    Args:
        arr: 待排序数组
    """
    # 边界检查：空数组或单元素数组无需排序
    if arr is None or len(arr) < 2:
        return
    
    # 外层循环控制排序的轮数，最多需要进行n-1轮
    # 每轮都会确定一个元素的最终位置（当前未排序部分的最大元素）
    # end表示每轮比较的上界，随着排序的进行逐渐减小
    for end in range(len(arr) - 1, 0, -1):
        # 优化标志：记录本轮是否发生交换
        # 如果一轮比较中没有发生任何交换，说明数组已经有序
        swapped = False
        
        # 内层循环进行相邻元素的比较和交换
        # 每轮比较范围逐渐缩小，因为末尾的元素已经有序
        # i从0开始到end-1，比较arr[i]和arr[i+1]
        for i in range(end):
            # 如果前面的元素比后面的大，则交换
            # 这会将较大的元素逐步向右移动（"冒泡"）
            if arr[i] > arr[i + 1]:
                swap(arr, i, i + 1)
                swapped = True
        
        # 如果本轮没有发生交换，说明数组已经有序，可以提前结束
        # 这是冒泡排序的一个重要优化，可以将最好情况的时间复杂度降到O(n)
        if not swapped:
            break


def insertion_sort(arr):
    """
    插入排序 - Insertion Sort
    时间复杂度: O(n²) - 最坏情况，O(n) - 最好情况(已排序)
    空间复杂度: O(1) - 只使用了常数级别的额外空间
    稳定性: 稳定 - 相等元素不会交换位置
    
    算法思路：
    1. 将数组分为已排序和未排序两部分，初始时已排序部分只有第一个元素
    2. 依次取出未排序部分的元素，在已排序部分找到合适的插入位置
    3. 将元素插入到正确位置，重复步骤2-3直到所有元素排序完成

    优点：
    - 实现简单
    - 稳定排序
    - 原地排序
    - 对于小规模或基本有序的数据效率很高
    - 在线算法：可以在接收数据的同时进行排序

    缺点：
    - 时间复杂度高，不适合大数据量
    - 对于逆序数据效率较低

    适用场景：
    - 小规模数据排序
    - 基本有序的数据
    - 在线数据排序
    - 作为高级排序算法的子过程（如快速排序的小数组优化）
    
    Args:
        arr: 待排序数组
    """
    # 边界检查：空数组或单元素数组无需排序
    if arr is None or len(arr) < 2:
        return
    
    # 从第二个元素开始，因为第一个元素可以看作已排序
    # i表示当前要插入的元素位置
    for i in range(1, len(arr)):
        # 从当前位置向前比较，找到合适的插入位置
        # 当前元素为arr[i]，需要在arr[0...i-1]中找到插入位置
        # j从i-1开始向前遍历已排序部分
        for j in range(i - 1, -1, -1):
            # 如果前一个元素大于当前元素，则交换
            # 这实际上是在将当前元素向前移动
            if j >= 0 and arr[j] > arr[j + 1]:
                swap(arr, j, j + 1)
            else:
                # 找到合适的位置，跳出内层循环
                # 当arr[j] <= arr[j+1]时，说明已找到插入位置
                break


def random_array(n, v):
    """
    得到一个随机数组，长度是n，数组中每个数，都在1~v之间，随机得到
    
    Args:
        n: 数组长度
        v: 数值范围上限
        
    Returns:
        随机数组
    """
    return [random.randint(1, v) for _ in range(n)]


def copy_array(arr):
    """
    复制数组
    
    Args:
        arr: 原数组
        
    Returns:
        复制的数组
    """
    return copy.deepcopy(arr)


def same_array(arr1, arr2):
    """
    比较两个数组是否相同
    
    Args:
        arr1: 数组1
        arr2: 数组2
        
    Returns:
        是否相同
    """
    if len(arr1) != len(arr2):
        return False
    
    for i in range(len(arr1)):
        if arr1[i] != arr2[i]:
            return False
    
    return True


def is_sorted(arr):
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


def generate_random_array(size):
    """
    生成随机测试数组
    
    Args:
        size: 数组大小
        
    Returns:
        随机数组
    """
    return [random.randint(0, size * 10) for _ in range(size)]


def performance_test():
    """
    性能测试：比较三种排序算法在不同数据规模下的表现
    """
    print("\n=== 性能测试 ===")
    
    sizes = [100, 500, 1000]
    algorithms = ["选择排序", "冒泡排序", "插入排序"]
    sort_functions = [selection_sort, bubble_sort, insertion_sort]
    
    for size in sizes:
        print(f"\n数组大小: {size}")
        data = generate_random_array(size)
        
        for j, algorithm in enumerate(algorithms):
            test_data = copy_array(data)
            start_time = time.time()
            
            sort_functions[j](test_data)
            
            end_time = time.time()
            duration = (end_time - start_time) * 1000  # 转换为毫秒
            
            print(f"{algorithm}: {duration:.2f} ms")
            
            # 验证排序正确性
            correct = is_sorted(test_data)
            if not correct:
                print("  排序错误!")


def test_sort_algorithms():
    """
    测试函数：验证三种排序算法的正确性
    """
    print("=== 选择排序、冒泡排序、插入排序测试 ===")
    
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
    
    algorithms = ["选择排序", "冒泡排序", "插入排序"]
    sort_functions = [selection_sort, bubble_sort, insertion_sort]
    
    for i, test_case in enumerate(test_cases):
        print(f"\n测试用例 {i + 1}: {test_case}")
        
        for j, algorithm in enumerate(algorithms):
            arr = copy_array(test_case)
            expected = sorted(test_case)  # 使用系统排序作为基准
            
            sort_functions[j](arr)
            
            correct = arr == expected
            print(f"{algorithm}: {arr} - {'✓' if correct else '✗'}")
            
            if not correct:
                print(f"预期: {expected}")


def main():
    """
    主函数：演示排序算法的使用
    """
    # 随机数组最大长度
    N = 200
    # 随机数组每个值，在1~V之间等概率随机
    V = 1000
    # testTimes : 测试次数 (减少测试次数以避免运行时间过长)
    test_times = 5000
    
    print("测试开始")
    for i in range(test_times):
        # 随机得到一个长度，长度在[0~N-1]
        n = random.randint(0, N - 1)
        # 得到随机数组
        arr = random_array(n, V)
        arr1 = copy_array(arr)
        arr2 = copy_array(arr)
        arr3 = copy_array(arr)
        
        selection_sort(arr1)
        bubble_sort(arr2)
        insertion_sort(arr3)
        
        if not same_array(arr1, arr2) or not same_array(arr1, arr3):
            print("出错了!")
            # 当有错了
            # 打印是什么例子，出错的
            # 打印三个功能，各自排序成了什么样
            # 可能要把例子带入，每个方法，去debug！
    
    print("测试结束")
    
    # 额外测试用例演示
    test_sort_algorithms()
    
    # 性能测试
    performance_test()
    
    # 测试额外的题目
    test_additional_problems()


# =========================================================================
# 插入排序的优化版本
# =========================================================================

def insertion_sort_optimized(arr):
    """
    插入排序的优化版本 - 使用赋值代替交换，减少操作次数
    
    优化原理：
    在标准插入排序中，每次比较都可能涉及一次完整的交换操作（3次赋值）
    而在优化版本中，我们先保存当前要插入的元素，然后只进行元素后移操作
    最后再将保存的元素插入到正确位置，这样可以减少赋值操作的次数
    
    性能提升：
    - 对于随机数据，大约可以减少30%-50%的赋值操作
    - 对于接近有序的数据，性能提升更显著
    """
    # 边界检查：空数组或单元素数组无需排序
    if arr is None or len(arr) < 2:
        return
    
    # 从第二个元素开始处理
    for i in range(1, len(arr)):
        # 保存当前要插入的元素
        current = arr[i]
        # j指向已排序部分的最后一个位置
        j = i - 1
        
        # 将大于current的元素向后移动
        # 当已排序部分的元素大于current时，将其向后移动一位
        while j >= 0 and arr[j] > current:
            arr[j + 1] = arr[j]
            j -= 1
        
        # 将current插入到正确位置
        # 此时j+1就是current应该插入的位置
        arr[j + 1] = current

def binary_insertion_sort(arr):
    """
    二分插入排序 - 使用二分查找优化插入排序
    
    优化原理：
    在已排序的部分查找插入位置时，使用二分查找代替线性扫描
    可以将查找过程的时间复杂度从O(n)降低到O(log n)
    但整体排序的时间复杂度仍然是O(n²)，因为元素移动的操作无法避免
    
    适用场景：
    - 数据量较大但仍在可接受范围内的情况
    - 比较操作成本较高的场景
    """
    # 边界检查：空数组或单元素数组无需排序
    if arr is None or len(arr) < 2:
        return
    
    # 从第二个元素开始处理
    for i in range(1, len(arr)):
        # 保存当前要插入的元素
        current = arr[i]
        # 使用二分查找找到插入位置
        # 在已排序部分arr[0...i-1]中查找插入位置
        left, right = 0, i - 1
        
        # 二分查找过程
        # 查找第一个大于current的元素位置
        while left <= right:
            # 使用left + (right - left) // 2而不是(left + right) // 2
            # 可以避免当left和right都很大时可能发生的整数溢出
            mid = left + (right - left) // 2
            if arr[mid] > current:
                # current应该插入到mid或其左侧
                right = mid - 1
            else:
                # current应该插入到mid右侧
                left = mid + 1
        
        # 找到了插入位置left，需要将[left, i-1]的元素后移
        # 注意：Python中切片赋值可以简化这一操作
        # 将arr[left...i-1]的元素向后移动一位到arr[left+1...i]
        arr[left+1:i+1] = arr[left:i]
        
        # 将current插入到正确位置
        arr[left] = current

# =========================================================================
# 经典算法题目的最优解法实现
# =========================================================================

def sort_colors(nums):
    """
    LeetCode 75. 颜色分类 - 最优解法（三指针法/荷兰国旗算法）
    题目链接：https://leetcode.cn/problems/sort-colors/
    
    时间复杂度：O(n) - 仅需一次遍历
    空间复杂度：O(1) - 原地排序
    
    算法思想：
    使用三个指针将数组分为三个区域：
    - [0, p0): 已排序的0区域
    - [p0, curr): 已排序的1区域
    - [p2, n-1]: 已排序的2区域
    - [curr, p2): 待处理的区域
    
    算法步骤：
    1. 初始化p0=0（0的右边界），curr=0（当前处理位置），p2=n-1（2的左边界）
    2. 当curr <= p2时循环：
       a. 如果nums[curr] == 0，交换nums[curr]和nums[p0]，p0++, curr++
       b. 如果nums[curr] == 1，curr++
       c. 如果nums[curr] == 2，交换nums[curr]和nums[p2]，p2--（curr不变）
    
    为什么是最优解：
    - 相比基础排序算法的O(n²)时间复杂度，三指针法只需要O(n)时间
    - 空间复杂度为O(1)，不需要额外空间
    - 只需要一次遍历，效率高
    - 直接利用了问题特性（只有0、1、2三种元素）
    """
    # 防御性编程：检查输入合法性
    if nums is None or len(nums) < 2:
        return
    
    n = len(nums)
    p0 = 0       # 0的右边界（初始为0）
    curr = 0     # 当前遍历的位置
    p2 = n - 1   # 2的左边界（初始为数组末尾）
    
    # 遍历数组直到curr超过p2
    # 循环条件是curr <= p2，因为p2位置的元素尚未处理
    while curr <= p2:
        if nums[curr] == 0:
            # 当前元素为0，放到0的区域
            # 交换后，p0位置的元素一定是0，curr位置的元素是原来p0位置的元素（0、1或2）
            # 由于p0 <= curr，p0位置的元素已经被处理过，所以可以安全地递增curr
            swap(nums, curr, p0)
            curr += 1
            p0 += 1
        elif nums[curr] == 2:
            # 当前元素为2，放到2的区域
            # 交换后，p2位置的元素是原来curr位置的元素（未知），所以curr不能递增
            swap(nums, curr, p2)
            p2 -= 1
            # 注意curr不变，因为交换过来的元素还未处理
        else:
            # 当前元素为1，保持不动，继续处理下一个元素
            # 1的区域自然扩展
            curr += 1

def merge(nums1, m, nums2, n):
    """
    LeetCode 88. 合并两个有序数组 - 最优解法（从后向前合并）
    题目链接：https://leetcode.cn/problems/merge-sorted-array/
    
    时间复杂度：O(m+n) - 仅需一次遍历
    空间复杂度：O(1) - 原地操作
    
    算法思想：
    从两个数组的末尾开始比较，将较大的元素放到nums1的末尾位置
    这样可以避免覆盖nums1中的原始数据，不需要额外空间
    
    算法步骤：
    1. 初始化三个指针：i=m-1（nums1有效元素的末尾），j=n-1（nums2的末尾），k=m+n-1（nums1的末尾）
    2. 比较nums1[i]和nums2[j]，将较大的元素放到nums1[k]的位置
    3. 递减相应的指针，重复步骤2直到处理完所有元素
    4. 如果nums2还有剩余元素，直接复制到nums1的前面（nums1剩余的元素已经在正确位置）
    """
    # 防御性编程：检查输入合法性
    if nums2 is None or n == 0:
        return  # nums2为空，无需合并
    if nums1 is None:
        raise ValueError("nums1 cannot be None")
    if len(nums1) < m + n:
        raise ValueError("nums1 does not have enough space")
    
    i = m - 1     # nums1有效元素的最后一个位置
    j = n - 1     # nums2的最后一个位置
    k = m + n - 1 # nums1的最后一个位置
    
    # 从后向前合并，比较并放置较大的元素
    # 当两个数组都还有元素时进行比较
    while i >= 0 and j >= 0:
        if nums1[i] > nums2[j]:
            # nums1的元素较大，放到nums1的末尾
            nums1[k] = nums1[i]
            i -= 1
        else:
            # nums2的元素较大或相等，放到nums1的末尾
            nums1[k] = nums2[j]
            j -= 1
        k -= 1
    
    # 如果nums2还有剩余元素，直接复制到nums1的前面
    # 注意：如果nums1还有剩余元素，它们已经在正确的位置上，无需处理
    while j >= 0:
        nums1[k] = nums2[j]
        j -= 1
        k -= 1

def move_zeroes(nums):
    """
    LeetCode 283. 移动零 - 最优解法（双指针法）
    题目链接：https://leetcode.cn/problems/move-zeroes/
    
    时间复杂度：O(n) - 仅需一次遍历
    空间复杂度：O(1) - 原地操作
    
    算法思想：
    使用两个指针，一个指向当前应该放置非零元素的位置，另一个遍历整个数组
    当遇到非零元素时，将其移动到第一个指针指向的位置，然后第一个指针前进
    
    算法步骤：
    1. 初始化一个指针non_zero_pos=0，表示下一个非零元素应该放置的位置
    2. 遍历数组，对于每个元素：
       a. 如果元素非零，将其移动到non_zero_pos位置，然后non_zero_pos++
    3. 遍历结束后，将non_zero_pos到数组末尾的所有元素设置为0
    """
    # 防御性编程：检查输入合法性
    if nums is None or len(nums) <= 1:
        return
    
    non_zero_pos = 0  # 下一个非零元素应该放置的位置
    
    # 第一步：将所有非零元素移动到数组前面
    # 遍历整个数组
    for i in range(len(nums)):
        if nums[i] != 0:
            # 将非零元素移动到non_zero_pos位置
            nums[non_zero_pos] = nums[i]
            non_zero_pos += 1
    
    # 第二步：将剩余位置填充为0
    # 将non_zero_pos到数组末尾的所有位置设置为0
    for i in range(non_zero_pos, len(nums)):
        nums[i] = 0

def move_zeroes_optimized(nums):
    """
    LeetCode 283. 移动零 - 优化版本（一次遍历，更少的赋值操作）
    
    优化思路：
    当遇到非零元素时，直接与non_zero_pos位置交换，这样可以减少一些不必要的赋值操作
    特别是当数组中大部分元素都是非零时，这种方法更高效
    """
    # 防御性编程：检查输入合法性
    if nums is None or len(nums) <= 1:
        return
    
    non_zero_pos = 0  # 下一个非零元素应该放置的位置
    
    # 遍历数组
    for i in range(len(nums)):
        if nums[i] != 0:
            # 当两个指针不同时才交换，避免不必要的操作
            # 如果i == non_zero_pos，说明前面没有0，无需交换
            if i != non_zero_pos:
                swap(nums, i, non_zero_pos)
            non_zero_pos += 1

def find_kth_largest(nums, k):
    """
    LeetCode 215. 数组中的第K个最大元素 - 快速选择算法
    题目链接：https://leetcode.cn/problems/kth-largest-element-in-an-array/
    
    时间复杂度：O(n) - 平均情况，O(n²) - 最坏情况
    空间复杂度：O(log n) - 递归调用栈的深度，最坏情况为O(n)
    
    算法思想：
    基于快速排序的分区思想，每次分区后只递归处理包含第k大元素的那一半
    这样可以避免对整个数组进行排序
    
    算法步骤：
    1. 选择一个基准元素，将数组分为两部分：大于基准的和小于基准的
    2. 如果基准元素的位置正好是第k大的位置，返回该元素
    3. 否则，递归处理包含第k大元素的那一半
    """
    # 防御性编程：检查输入合法性
    if nums is None or len(nums) == 0 or k <= 0 or k > len(nums):
        raise ValueError("Invalid input")
    
    # 第k大元素在排序后的数组中的索引是len(nums) - k
    # 例如：数组[1,2,3,4,5]中第2大的元素是4，其索引为5-2=3
    return quick_select(nums, 0, len(nums) - 1, len(nums) - k)

def quick_select(nums, left, right, k):
    """
    快速选择算法的核心实现
    
    参数:
        nums: 数组
        left: 左边界
        right: 右边界
        k: 目标索引（第k小的元素）
    
    返回:
        第k小的元素值
    """
    # 分区操作，返回基准元素的最终位置
    # pivot_index是基准元素在数组中的最终位置
    pivot_index = partition(nums, left, right)
    
    # 如果基准元素的位置正好是k，返回该元素
    if pivot_index == k:
        return nums[pivot_index]
    # 如果基准元素的位置大于k，递归处理左半部分
    elif pivot_index > k:
        return quick_select(nums, left, pivot_index - 1, k)
    # 如果基准元素的位置小于k，递归处理右半部分
    else:
        return quick_select(nums, pivot_index + 1, right, k)

def partition(nums, left, right):
    """
    快速排序的分区操作
    
    参数:
        nums: 数组
        left: 左边界
        right: 右边界
    
    返回:
        基准元素的最终位置
    """
    # 选择最右边的元素作为基准
    # 这是一种简单的选择策略，也可以使用随机选择来避免最坏情况
    pivot = nums[right]
    # i表示小于基准元素的区域的边界
    # 初始时小于基准的区域为空，所以i = left - 1
    i = left - 1
    
    # 遍历[left, right-1]范围内的元素
    for j in range(left, right):
        # 如果当前元素小于基准元素，将其交换到小于区域
        if nums[j] <= pivot:
            # 扩展小于基准的区域
            i += 1
            swap(nums, i, j)
    
    # 将基准元素放到正确的位置
    # 此时i+1是基准元素应该放置的位置
    swap(nums, i + 1, right)
    return i + 1

# =========================================================================
# 算法调试与优化技巧
# =========================================================================

def print_array_details(arr, message):
    """
    打印数组的详细信息，用于调试
    
    参数:
        arr: 要打印的数组
        message: 描述信息
    """
    print(message)
    if arr is None:
        print("Array is null")
        return
    print(f"{arr}")
    print(f"Length: {len(arr)}")
    if len(arr) > 0:
        print(f"First element: {arr[0]}")
        print(f"Last element: {arr[-1]}")
    print()

def analyze_sort_performance(arr, sort_method):
    """
    分析排序算法的性能指标
    
    参数:
        arr: 要排序的数组
        sort_method: 排序方法名称
    """
    # 创建数组副本，避免修改原数组
    arr_copy = copy_array(arr)
    
    print(f"=== {sort_method} 性能分析 ===")
    print(f"数组大小: {len(arr_copy)}")
    
    # 测量排序前是否已排序
    was_sorted = is_sorted(arr_copy)
    print(f"排序前是否有序: {'是' if was_sorted else '否'}")
    
    # 测量排序时间
    import time
    start_time = time.time_ns()
    
    # 根据方法名选择排序算法
    if sort_method == "选择排序":
        selection_sort(arr_copy)
    elif sort_method == "冒泡排序":
        bubble_sort(arr_copy)
    elif sort_method == "插入排序":
        insertion_sort(arr_copy)
    elif sort_method == "优化插入排序":
        insertion_sort_optimized(arr_copy)
    elif sort_method == "二分插入排序":
        binary_insertion_sort(arr_copy)
    else:
        print("未知的排序方法")
        return
    
    end_time = time.time_ns()
    duration_ms = (end_time - start_time) / 1e6  # 转换为毫秒
    print(f"排序耗时: {duration_ms:.4f} ms")
    
    # 验证排序结果
    is_sorted_flag = is_sorted(arr_copy)
    print(f"排序结果是否正确: {'是' if is_sorted_flag else '否'}")
    print()

# =========================================================================
# 工程化改造示例 - 将排序算法封装为可复用组件
# =========================================================================

class SortUtils:
    """
    排序工具类 - 封装了各种排序算法，提供统一的接口
    这个类演示了如何将排序算法工程化为可复用组件
    """
    # 排序算法枚举
    class SortAlgorithm:
        SELECTION_SORT = "selection_sort"
        BUBBLE_SORT = "bubble_sort"
        INSERTION_SORT = "insertion_sort"
        INSERTION_SORT_OPTIMIZED = "insertion_sort_optimized"
        BINARY_INSERTION_SORT = "binary_insertion_sort"
    
    @staticmethod
    def sort(arr, algorithm):
        """
        统一的排序接口
        
        参数:
            arr: 要排序的数组
            algorithm: 选择的排序算法
        """
        # 防御性编程
        if arr is None or len(arr) < 2:
            return
        
        # 根据选择的算法调用相应的排序方法
        if algorithm == SortUtils.SortAlgorithm.SELECTION_SORT:
            selection_sort(arr)
        elif algorithm == SortUtils.SortAlgorithm.BUBBLE_SORT:
            bubble_sort(arr)
        elif algorithm == SortUtils.SortAlgorithm.INSERTION_SORT:
            insertion_sort(arr)
        elif algorithm == SortUtils.SortAlgorithm.INSERTION_SORT_OPTIMIZED:
            insertion_sort_optimized(arr)
        elif algorithm == SortUtils.SortAlgorithm.BINARY_INSERTION_SORT:
            binary_insertion_sort(arr)
        else:
            raise ValueError("Unsupported sorting algorithm")
    
    @staticmethod
    def auto_select_sort(arr):
        """
        根据数据特征自动选择最合适的排序算法
        
        参数:
            arr: 要排序的数组
        """
        # 防御性编程
        if arr is None or len(arr) < 2:
            return
        
        # 分析数据特征
        n = len(arr)
        is_nearly_sorted = SortUtils._is_nearly_sorted(arr)
        has_few_unique = SortUtils._has_few_unique_values(arr)
        
        # 根据数据特征选择算法
        if is_nearly_sorted:
            # 接近有序的数据使用插入排序
            SortUtils.sort(arr, SortUtils.SortAlgorithm.INSERTION_SORT_OPTIMIZED)
        elif n < 1000:
            # 小规模数据使用插入排序
            SortUtils.sort(arr, SortUtils.SortAlgorithm.INSERTION_SORT_OPTIMIZED)
        else:
            # 其他情况使用二分插入排序
            SortUtils.sort(arr, SortUtils.SortAlgorithm.BINARY_INSERTION_SORT)
    
    @staticmethod
    def _is_nearly_sorted(arr):
        """
        判断数组是否接近有序
        
        参数:
            arr: 要检查的数组
        
        返回:
            如果数组接近有序返回True，否则返回False
        """
        inversion_count = 0
        threshold = len(arr) // 2  # 阈值：逆序对数量不超过数组长度的一半
        
        # 计算逆序对数量
        for i in range(len(arr) - 1):
            for j in range(i + 1, len(arr)):
                if arr[i] > arr[j]:
                    inversion_count += 1
                    # 如果超过阈值，提前返回
                    if inversion_count >= threshold:
                        return False
        
        return inversion_count < threshold
    
    @staticmethod
    def _has_few_unique_values(arr):
        """
        判断数组是否有少量唯一值
        
        参数:
            arr: 要检查的数组
        
        返回:
            如果数组有少量唯一值返回True，否则返回False
        """
        # 简单实现：检查是否有超过25%的重复元素
        unique_values = set(arr)
        return len(unique_values) < len(arr) * 0.25


# =========================================================================
# 更多经典题目的实现 - 涉及各大算法平台的高频题目
# =========================================================================

def top_k_frequent(nums, k):
    """
    LeetCode 347. 前 K 个高频元素
    题目链接：https://leetcode.cn/problems/top-k-frequent-elements/
    
    题目描述：给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素
    示例：
    输入: nums = [1,1,1,2,2,3], k = 2
    输出: [1,2]
    
    解题思路：
    1. 使用哈希表统计每个元素的频率
    2. 使用堆（优先队列）或桶排序找到频率最高的 k 个元素
    
    时间复杂度：O(n log k) - 使用最小堆
    空间复杂度：O(n) - 哈希表存储频率
    
    最优解：桶排序，时间复杂度 O(n)，空间复杂度 O(n)
    """
    # 防御性编程
    if nums is None or len(nums) == 0 or k <= 0 or k > len(nums):
        raise ValueError("Invalid input")
    
    # 步骤1：统计频率
    # 使用字典记录每个元素的出现次数
    freq_map = {}
    for num in nums:
        freq_map[num] = freq_map.get(num, 0) + 1
    
    # 步骤2：桶排序 - 按频率分组
    # bucket[i] 存储频率为 i 的所有元素
    # 桶的数量为len(nums)+1，因为频率最大为len(nums)
    bucket = [[] for _ in range(len(nums) + 1)]
    for num, freq in freq_map.items():
        bucket[freq].append(num)
    
    # 步骤3：从高频到低频收集结果
    result = []
    # 从最大频率开始向下遍历
    for i in range(len(bucket) - 1, -1, -1):
        if bucket[i]:
            # 将当前频率的所有元素添加到结果中
            for num in bucket[i]:
                result.append(num)
                # 当收集到k个元素时停止
                if len(result) == k:
                    return result
    
    return result


def relative_sort_array(arr1, arr2):
    """
    LeetCode 1122. 数组的相对排序
    题目链接：https://leetcode.cn/problems/relative-sort-array/
    
    题目描述：给你两个数组，arr1 和 arr2，arr2 中的元素各不相同，arr2 中的每个元素都出现在 arr1 中。
    对 arr1 中的元素进行排序，使 arr1 中项的相对顺序和 arr2 中的相对顺序相同。
    未在 arr2 中出现过的元素需要按照升序放在 arr1 的末尾。
    
    示例：
    输入：arr1 = [2,3,1,3,2,4,6,7,9,2,19], arr2 = [2,1,4,3,9,6]
    输出：[2,2,2,1,4,3,3,9,6,7,19]
    
    解题思路：
    1. 使用计数排序思想，统计 arr1 中每个元素的出现次数
    2. 按照 arr2 的顺序填充结果数组
    3. 将不在 arr2 中的元素排序后放在末尾
    
    时间复杂度：O(n log n) - 主要是排序不在 arr2 中的元素
    空间复杂度：O(n) - 哈希表和结果数组
    
    最优解：计数排序，时间复杂度 O(n + m)，其中 n 是 arr1 的长度，m 是数值范围
    """
    # 防御性编程
    if arr1 is None or arr2 is None:
        raise ValueError("Arrays cannot be None")
    
    # 步骤1：统计 arr1 中每个元素的频率
    count_map = {}
    for num in arr1:
        count_map[num] = count_map.get(num, 0) + 1
    
    # 步骤2：按照 arr2 的顺序填充结果
    result = []
    # 按照arr2中元素的顺序处理
    for num in arr2:
        if num in count_map:
            # 将count_map[num]个num添加到结果中
            result.extend([num] * count_map[num])
            # 从count_map中删除已处理的元素
            del count_map[num]
    
    # 步骤3：将不在 arr2 中的元素排序后放在末尾
    remaining = []
    # 收集剩余的元素
    for num, count in count_map.items():
        remaining.extend([num] * count)
    # 对剩余元素进行排序
    remaining.sort()
    # 将排序后的剩余元素添加到结果末尾
    result.extend(remaining)
    
    return result


def get_least_numbers(arr, k):
    """
    剑指 Offer 40. 最小的k个数
    题目链接：https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
    
    题目描述：输入整数数组 arr ，找出其中最小的 k 个数
    示例：
    输入：arr = [3,2,1], k = 2
    输出：[1,2] 或者 [2,1]
    
    解题思路：
    方法1：排序后取前k个 - 时间复杂度 O(n log n)
    方法2：堆（最大堆）- 时间复杂度 O(n log k)
    方法3：快速选择算法 - 平均时间复杂度 O(n)
    
    最优解：快速选择算法
    时间复杂度：O(n) - 平均情况
    空间复杂度：O(log n) - 递归栈空间
    """
    # 防御性编程
    if arr is None or k <= 0 or k > len(arr):
        return []
    
    # 使用快速选择找到第k小的元素
    # 由于是找最小的k个数，不需要完全排序
    quick_select(arr, 0, len(arr) - 1, k - 1)
    
    # 前k个元素就是结果（不一定有序）
    # 对结果进行排序以满足题目要求
    result = arr[:k]
    result.sort()
    return result


def find_relative_ranks(score):
    """
    LeetCode 506. 相对名次
    题目链接：https://leetcode.cn/problems/relative-ranks/
    
    题目描述：给你一个长度为 n 的整数数组 score ，其中 score[i] 表示第 i 位运动员在比赛中的得分。
    所有得分都互不相同。运动员将根据得分决定名次，其中名次第 1 的运动员得分最高，名次第 2 的运动员得分第 2 高，依此类推。
    
    示例：
    输入：score = [5,4,3,2,1]
    输出：["Gold Medal","Silver Medal","Bronze Medal","4","5"]
    
    解题思路：
    1. 创建索引数组，按分数排序
    2. 根据排序后的索引分配名次
    
    时间复杂度：O(n log n) - 排序的时间复杂度
    空间复杂度：O(n) - 存储索引和结果
    """
    # 防御性编程
    if score is None or len(score) == 0:
        return []
    
    n = len(score)
    # 创建索引数组，用于排序后找到原始位置
    # indices[i]表示原始数组中第i个位置的索引
    indices = list(range(n))
    
    # 按分数从高到低排序索引
    # key=lambda x: score[x]表示按score[indices[i]]的值进行排序
    # reverse=True表示降序排列
    indices.sort(key=lambda x: score[x], reverse=True)
    
    # 根据排序后的索引分配名次
    result = [''] * n
    for i, idx in enumerate(indices):
        # 根据排名分配奖牌或名次
        if i == 0:
            result[idx] = "Gold Medal"
        elif i == 1:
            result[idx] = "Silver Medal"
        elif i == 2:
            result[idx] = "Bronze Medal"
        else:
            # 第4名及以后用数字表示
            result[idx] = str(i + 1)
    
    return result


def sort_array_by_parity_ii(nums):
    """
    LeetCode 922. 按奇偶排序数组 II
    题目链接：https://leetcode.cn/problems/sort-array-by-parity-ii/
    
    题目描述：给定一个非负整数数组 nums，nums 中一半整数是奇数，一半整数是偶数。
    对数组进行排序，以便当 nums[i] 为奇数时，i 也是奇数；当 nums[i] 为偶数时， i 也是偶数。
    
    示例：
    输入：nums = [4,2,5,7]
    输出：[4,5,2,7]
    
    解题思路：
    方法1：使用两个数组分别存储奇数和偶数，然后按要求放回
    方法2：双指针原地交换
    
    最优解：双指针原地交换
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    # 防御性编程
    if nums is None or len(nums) == 0:
        return nums
    
    n = len(nums)
    even_idx = 0  # 偶数位置指针，用于寻找应该放偶数但放了奇数的位置
    odd_idx = 1   # 奇数位置指针，用于寻找应该放奇数但放了偶数的位置
    
    # 当两个指针都在有效范围内时继续循环
    while even_idx < n and odd_idx < n:
        # 找到偶数位置上的奇数
        # 在偶数位置（0,2,4...）上寻找奇数
        while even_idx < n and nums[even_idx] % 2 == 0:
            even_idx += 2
        # 找到奇数位置上的偶数
        # 在奇数位置（1,3,5...）上寻找偶数
        while odd_idx < n and nums[odd_idx] % 2 == 1:
            odd_idx += 2
        # 交换
        # 如果找到了两个错误位置，进行交换
        if even_idx < n and odd_idx < n:
            swap(nums, even_idx, odd_idx)
            # 交换后继续寻找下一个错误位置
            even_idx += 2
            odd_idx += 2
    
    return nums


def test_additional_problems():
    """
    测试额外的题目解法
    """
    print("\n=== 额外算法题目测试 ===\n")
    
    # 测试 top_k_frequent
    print("--- LeetCode 347. 前 K 个高频元素 ---")
    freq_test = [1, 1, 1, 2, 2, 3]
    freq_result = top_k_frequent(freq_test, 2)
    print(f"输入: [1,1,1,2,2,3], k=2")
    print(f"输出: {freq_result}")
    print()
    
    # 测试 relative_sort_array
    print("--- LeetCode 1122. 数组的相对排序 ---")
    arr1 = [2, 3, 1, 3, 2, 4, 6, 7, 9, 2, 19]
    arr2 = [2, 1, 4, 3, 9, 6]
    relative_result = relative_sort_array(arr1, arr2)
    print(f"输入: arr1=[2,3,1,3,2,4,6,7,9,2,19], arr2=[2,1,4,3,9,6]")
    print(f"输出: {relative_result}")
    print()
    
    # 测试 get_least_numbers
    print("--- 剑指 Offer 40. 最小的k个数 ---")
    least_test = [3, 2, 1, 5, 6, 4]
    least_result = get_least_numbers(least_test.copy(), 2)
    print(f"输入: [3,2,1,5,6,4], k=2")
    print(f"输出: {least_result}")
    print()
    
    # 测试 find_relative_ranks
    print("--- LeetCode 506. 相对名次 ---")
    score_test = [5, 4, 3, 2, 1]
    rank_result = find_relative_ranks(score_test)
    print(f"输入: [5,4,3,2,1]")
    print(f"输出: {rank_result}")
    print()
    
    # 测试 sort_array_by_parity_ii
    print("--- LeetCode 922. 按奇偶排序数组 II ---")
    parity_test = [4, 2, 5, 7]
    parity_result = sort_array_by_parity_ii(parity_test)
    print(f"输入: [4,2,5,7]")
    print(f"输出: {parity_result}")
    print()


# 为了验证
if __name__ == "__main__":
    main()