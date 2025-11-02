"""分治法求解数组最大值问题 (Python版本)

问题描述:
给定一个数组，找出其中的最大值。

解法思路:
使用分治法，将数组不断二分，直到只有一个元素时直接返回，
然后比较左右两部分的最大值，返回较大者。

算法特点:
1. 分治策略：将大问题分解为小问题
2. 递归实现：通过递归不断分解问题
3. 合并结果：比较子问题的解得到原问题的解

时间复杂度分析:
T(n) = 2*T(n/2) + O(1)
根据主定理，时间复杂度为 O(n)

空间复杂度分析:
递归调用栈的深度为 O(log n)
空间复杂度为 O(log n)

相关题目扩展:
1. LeetCode 53. 最大子数组和 (分治解法)
2. 求解数组中最大值和最小值
3. 求解数组中第k大元素
4. 分治法求解最大子矩阵和

工程化考量:
1. 异常处理：检查空数组情况
2. 边界处理：处理只有一个元素的数组
3. 性能优化：对于小规模数据可直接遍历
4. 可配置性：可扩展为求解任意范围内的最值

与标准库对比:
Python标准库中max()函数通常使用迭代而非递归，避免栈溢出风险

语言特性差异:
Java: 使用Math.max()函数
C++: 使用std::max()函数
Python: 使用内置max()函数或自定义比较

极端场景考虑:
1. 空数组：需要特殊处理
2. 单元素数组：直接返回
3. 大规模数组：可能栈溢出，需改用迭代
4. 所有元素相同：任一元素都是最大值

调试技巧:
1. 打印递归调用过程中的中间结果
2. 使用断言验证左右子数组的最值正确性
3. 性能测试：比较不同规模数据的执行时间
"""

import random
from typing import List


def max_value(arr):
    """
    入口方法，求数组中的最大值

    :param arr: 输入数组
    :return: 数组中的最大值
    :raises ValueError: 如果数组为空
    """
    # 异常处理：检查数组是否为空
    if not arr:
        raise ValueError("数组不能为空")

    # 调用分治方法求解
    return _find_max(arr, 0, len(arr) - 1)


def _find_max(arr, left, right):
    """
    分治法求解数组指定范围内的最大值

    :param arr: 数组
    :param left: 左边界（包含）
    :param right: 右边界（包含）
    :return: 指定范围内的最大值
    """
    # 基本情况：只有一个元素时直接返回
    if left == right:
        return arr[left]

    # 分解：计算中点，将数组分为两部分
    mid = (left + right) // 2

    # 递归求解：分别求左右两部分的最大值
    left_max = _find_max(arr, left, mid)
    right_max = _find_max(arr, mid + 1, right)

    # 合并：返回左右两部分最大值中的较大者
    return max(left_max, right_max)


# ==================== 题目1：LeetCode 53. 最大子数组和 (分治解法) ====================
def max_sub_array(nums: List[int]) -> int:
    """
    题目来源：LeetCode 53. Maximum Subarray
    题目链接：https://leetcode.com/problems/maximum-subarray/
    中文链接：https://leetcode.cn/problems/maximum-subarray/
    
    题目描述：
    给定一个整数数组 nums，找到一个具有最大和的连续子数组，返回其最大和。
    
    示例 1：
    输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
    输出：6
    解释：连续子数组 [4,-1,2,1] 的和最大，为 6。
    
    时间复杂度：O(n*log n) - 分治法
    空间复杂度：O(log n) - 递归栈
    
    最优解：Kadane算法，时间O(n)，空间O(1)
    """
    if not nums:
        raise ValueError("数组不能为空")
    return _max_sub_array_divide(nums, 0, len(nums) - 1)


def _max_sub_array_divide(nums: List[int], left: int, right: int) -> int:
    """分治法求解最大子数组和"""
    if left == right:
        return nums[left]

    mid = left + (right - left) // 2
    left_max = _max_sub_array_divide(nums, left, mid)
    right_max = _max_sub_array_divide(nums, mid + 1, right)

    # 计算跨越中点的最大子数组和
    left_cross_max = float('-inf')
    left_sum = 0
    for i in range(mid, left - 1, -1):
        left_sum += nums[i]
        left_cross_max = max(left_cross_max, left_sum)

    right_cross_max = float('-inf')
    right_sum = 0
    for i in range(mid + 1, right + 1):
        right_sum += nums[i]
        right_cross_max = max(right_cross_max, right_sum)

    cross_max = left_cross_max + right_cross_max
    return int(max(left_max, right_max, cross_max))


def max_sub_array_optimal(nums: List[int]) -> int:
    """
    最优解：Kadane算法
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    if not nums:
        raise ValueError("数组不能为空")

    max_sum = nums[0]
    cur_sum = nums[0]

    for i in range(1, len(nums)):
        cur_sum = max(nums[i], cur_sum + nums[i])
        max_sum = max(max_sum, cur_sum)

    return max_sum


# ==================== 题目2：LeetCode 169. 多数元素 (分治解法) ====================
def majority_element(nums: List[int]) -> int:
    """
    题目来源：LeetCode 169. Majority Element
    题目链接：https://leetcode.com/problems/majority-element/
    中文链接：https://leetcode.cn/problems/majority-element/
    
    时间复杂度：O(n*log n) - 分治法
    空间复杂度：O(log n)
    
    最优解：摩尔投票算法，时间O(n)，空间O(1)
    """
    if not nums:
        raise ValueError("数组不能为空")
    return _majority_element_divide(nums, 0, len(nums) - 1)


def _majority_element_divide(nums: List[int], left: int, right: int) -> int:
    """分治法求解多数元素"""
    if left == right:
        return nums[left]

    mid = left + (right - left) // 2
    left_major = _majority_element_divide(nums, left, mid)
    right_major = _majority_element_divide(nums, mid + 1, right)

    if left_major == right_major:
        return left_major

    left_count = sum(1 for i in range(left, right + 1) if nums[i] == left_major)
    right_count = sum(1 for i in range(left, right + 1) if nums[i] == right_major)

    return left_major if left_count > right_count else right_major


def majority_element_optimal(nums: List[int]) -> int:
    """
    最优解：摩尔投票算法
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    if not nums:
        raise ValueError("数组不能为空")

    candidate = nums[0]
    count = 1

    for i in range(1, len(nums)):
        if count == 0:
            candidate = nums[i]
            count = 1
        elif nums[i] == candidate:
            count += 1
        else:
            count -= 1

    return candidate


# ==================== 题目3：LeetCode 215. 数组中的第K个最大元素 ====================
def find_kth_largest(nums: List[int], k: int) -> int:
    """
    题目来源：LeetCode 215. Kth Largest Element in an Array
    题目链接：https://leetcode.com/problems/kth-largest-element-in-an-array/
    中文链接：https://leetcode.cn/problems/kth-largest-element-in-an-array/
    
    快速选择算法（基于分治思想）
    平均时间复杂度：O(n)
    最坏时间复杂度：O(n^2)
    空间复杂度：O(log n)
    """
    if not nums or k < 1 or k > len(nums):
        raise ValueError("参数非法")
    return _quick_select(nums, 0, len(nums) - 1, len(nums) - k)


def _quick_select(nums: List[int], left: int, right: int, k: int) -> int:
    """快速选择算法"""
    if left == right:
        return nums[left]

    pivot_index = left + random.randint(0, right - left)
    pivot_index = _partition(nums, left, right, pivot_index)

    if k == pivot_index:
        return nums[k]
    elif k < pivot_index:
        return _quick_select(nums, left, pivot_index - 1, k)
    else:
        return _quick_select(nums, pivot_index + 1, right, k)


def _partition(nums: List[int], left: int, right: int, pivot_index: int) -> int:
    """分区操作"""
    pivot_value = nums[pivot_index]
    nums[pivot_index], nums[right] = nums[right], nums[pivot_index]

    store_index = left
    for i in range(left, right):
        if nums[i] < pivot_value:
            nums[store_index], nums[i] = nums[i], nums[store_index]
            store_index += 1

    nums[store_index], nums[right] = nums[right], nums[store_index]
    return store_index


# ==================== 题目4：LeetCode 240. 搜索二维矩阵 II ====================
def search_matrix(matrix: List[List[int]], target: int) -> bool:
    """
    题目来源：LeetCode 240. Search a 2D Matrix II
    链接：https://leetcode.com/problems/search-a-2d-matrix-ii/
    中文：https://leetcode.cn/problems/search-a-2d-matrix-ii/
    
    最优解：从右上角或左下角搜索
    时间复杂度：O(m+n)
    空间复杂度：O(1)
    """
    if not matrix or not matrix[0]:
        return False

    row = 0
    col = len(matrix[0]) - 1

    while row < len(matrix) and col >= 0:
        if matrix[row][col] == target:
            return True
        elif matrix[row][col] > target:
            col -= 1
        else:
            row += 1

    return False


def test():
    """测试方法"""
    print("========== 原始测试：分治求数组最大值 ==========")
    
    # 测试用例1：普通数组
    arr1 = [3, 8, 7, 6, 4, 5, 1, 2]
    print("数组最大值 :", max_value(arr1))

    # 测试用例2：单元素数组
    arr2 = [42]
    print("单元素数组最大值 :", max_value(arr2))

    # 测试用例3：负数数组
    arr3 = [-5, -2, -8, -1]
    print("负数数组最大值 :", max_value(arr3))

    # 测试用例4：相同元素数组
    arr4 = [5, 5, 5, 5]
    print("相同元素数组最大值 :", max_value(arr4))

    # 测试用例5：大规模数组
    arr5 = list(range(10000))
    print("大规模数组最大值 :", max_value(arr5))

    # 异常测试：空数组
    try:
        arr6 = []
        max_value(arr6)
    except ValueError as e:
        print("空数组异常处理:", e)
    
    print("\n========== 题目1测试：LeetCode 53 最大子数组和 ==========")
    nums1 = [-2,1,-3,4,-1,2,1,-5,4]
    print("分治法结果:", max_sub_array(nums1))
    print("最优解(Kadane)结果:", max_sub_array_optimal(nums1))
    
    nums2 = [5,4,-1,7,8]
    print("测试用例2:", max_sub_array_optimal(nums2))
    
    print("\n========== 题目2测试：LeetCode 169 多数元素 ==========")
    nums3 = [3,2,3]
    print("分治法结果:", majority_element(nums3))
    print("最优解(摩尔投票)结果:", majority_element_optimal(nums3))
    
    nums4 = [2,2,1,1,1,2,2]
    print("测试用例2:", majority_element_optimal(nums4))
    
    print("\n========== 题目3测试：LeetCode 215 第K大元素 ==========")
    nums5 = [3,2,1,5,6,4]
    print("第2大元素:", find_kth_largest(nums5.copy(), 2))
    
    nums6 = [3,2,3,1,2,4,5,5,6]
    print("第4大元素:", find_kth_largest(nums6.copy(), 4))
    
    print("\n========== 题目4测试：LeetCode 240 搜索矩阵 ==========")
    matrix = [
        [1,4,7,11,15],
        [2,5,8,12,19],
        [3,6,9,16,22],
        [10,13,14,17,24],
        [18,21,23,26,30]
    ]
    print("搜索5:", search_matrix(matrix, 5))
    print("搜索20:", search_matrix(matrix, 20))


# ==================== 补充题目5：归并排序 ====================
def merge_sort(nums):
    """
    归并排序算法实现
    
    题目来源：经典排序算法
    
    题目描述：
    实现归并排序算法，将一个数组排序。
    
    解题思路：
    1. 将数组分成两半，分别排序
    2. 合并两个已排序的子数组
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    
    参数:
        nums: 待排序数组
    
    返回:
        排序后的数组
    """
    if len(nums) <= 1:
        return nums.copy()  # 返回副本以避免修改原数组
    
    mid = len(nums) // 2
    left = merge_sort(nums[:mid])
    right = merge_sort(nums[mid:])
    
    return _merge(left, right)

def _merge(left, right):
    """
    合并两个已排序的数组
    
    参数:
        left: 左子数组
        right: 右子数组
    
    返回:
        合并后的有序数组
    """
    result = []
    i = j = 0
    
    # 合并两个数组
    while i < len(left) and j < len(right):
        if left[i] <= right[j]:
            result.append(left[i])
            i += 1
        else:
            result.append(right[j])
            j += 1
    
    # 添加剩余元素
    result.extend(left[i:])
    result.extend(right[j:])
    
    return result

# ==================== 补充题目6：二分查找 ====================
def binary_search(nums, target):
    """
    二分查找算法实现（递归版本）
    
    题目来源：经典搜索算法
    
    题目描述：
    在一个排序数组中查找目标值，如果找到返回索引，否则返回-1。
    
    解题思路：
    1. 将区间不断二分，比较中间元素与目标值
    2. 根据比较结果调整搜索区间
    
    时间复杂度：O(log n)
    空间复杂度：O(log n) - 递归栈深度
    
    参数:
        nums: 已排序数组
        target: 目标值
    
    返回:
        目标值的索引，如果不存在返回-1
    """
    if not nums:
        return -1
    
    return _binary_search_helper(nums, target, 0, len(nums) - 1)

def _binary_search_helper(nums, target, left, right):
    """
    二分查找递归辅助函数
    
    参数:
        nums: 已排序数组
        target: 目标值
        left: 左边界
        right: 右边界
    
    返回:
        目标值的索引，如果不存在返回-1
    """
    if left > right:
        return -1
    
    mid = left + (right - left) // 2
    
    if nums[mid] == target:
        return mid
    elif nums[mid] > target:
        return _binary_search_helper(nums, target, left, mid - 1)
    else:
        return _binary_search_helper(nums, target, mid + 1, right)

def binary_search_optimal(nums, target):
    """
    二分查找最优解（迭代版本）
    
    时间复杂度：O(log n)
    空间复杂度：O(1)
    
    参数:
        nums: 已排序数组
        target: 目标值
    
    返回:
        目标值的索引，如果不存在返回-1
    """
    if not nums:
        return -1
    
    left, right = 0, len(nums) - 1
    
    while left <= right:
        mid = left + (right - left) // 2
        
        if nums[mid] == target:
            return mid
        elif nums[mid] > target:
            right = mid - 1
        else:
            left = mid + 1
    
    return -1

# ==================== 补充题目7：快速幂算法 ====================
def quick_pow(a, n):
    """
    快速幂算法实现（递归版本）
    
    题目来源：经典算法题
    
    题目描述：
    计算 a 的 n 次方，要求时间复杂度优于 O(n)。
    
    解题思路：
    1. 使用分治法，将 a^n 分解为 (a^(n/2))^2
    2. 递归计算子问题
    
    时间复杂度：O(log n)
    空间复杂度：O(log n) - 递归栈深度
    
    参数:
        a: 底数
        n: 指数
    
    返回:
        a 的 n 次方结果
    """
    # 处理特殊情况
    if n == 0:
        return 1.0
    if a == 0:
        return 0.0
    
    # 处理负数指数
    if n < 0:
        a = 1.0 / a
        n = -n
    
    return _quick_pow_helper(a, n)

def _quick_pow_helper(a, n):
    """
    快速幂递归辅助函数
    
    参数:
        a: 底数
        n: 非负指数
    
    返回:
        a 的 n 次方结果
    """
    if n == 0:
        return 1.0
    
    half = _quick_pow_helper(a, n // 2)
    if n % 2 == 0:
        return half * half
    else:
        return half * half * a

def quick_pow_optimal(a, n):
    """
    快速幂最优解（迭代版本）
    
    时间复杂度：O(log n)
    空间复杂度：O(1)
    
    参数:
        a: 底数
        n: 指数
    
    返回:
        a 的 n 次方结果
    """
    # 处理特殊情况
    if n == 0:
        return 1.0
    if a == 0:
        return 0.0
    
    # 处理负数指数
    if n < 0:
        a = 1.0 / a
        n = -n
    
    result = 1.0
    current = a
    
    while n > 0:
        if n % 2 == 1:
            result *= current
        current *= current
        n //= 2
    
    return result

# ==================== 补充题目8：最大子矩阵和 ====================
def max_sub_matrix(matrix):
    """
    最大子矩阵和算法实现
    
    题目来源：经典算法题
    
    题目描述：
    给定一个二维矩阵，找出其中和最大的子矩阵。
    
    解题思路：
    1. 将问题转化为一维最大子数组和问题
    2. 枚举左右边界，计算每一行的前缀和
    3. 使用Kadane算法求解一维最大子数组和
    
    时间复杂度：O(n^3)
    空间复杂度：O(n)
    
    参数:
        matrix: 二维矩阵
    
    返回:
        最大子矩阵和
    """
    if not matrix or not matrix[0]:
        raise ValueError("矩阵不能为空")
    
    rows = len(matrix)
    cols = len(matrix[0])
    max_sum = float('-inf')
    
    # 枚举左右边界
    for left in range(cols):
        row_sum = [0] * rows  # 记录每一行的和
        
        for right in range(left, cols):
            # 计算每一行在左右边界内的和
            for i in range(rows):
                row_sum[i] += matrix[i][right]
            
            # 使用Kadane算法求解一维最大子数组和
            current_sum = 0
            current_max = float('-inf')
            
            for num in row_sum:
                current_sum = max(num, current_sum + num)
                current_max = max(current_max, current_sum)
            
            max_sum = max(max_sum, current_max)
    
    return max_sum

# ==================== 补充题目9：Strassen矩阵乘法 ====================
def strassen_multiply(A, B):
    """
    Strassen矩阵乘法算法实现
    
    题目来源：经典算法题
    
    题目描述：
    实现Strassen矩阵乘法算法，优化传统的O(n^3)矩阵乘法。
    
    解题思路：
    1. 将矩阵分成四个子矩阵
    2. 计算7个中间矩阵（而非传统算法的8个乘法）
    3. 通过中间矩阵计算结果矩阵的四个子矩阵
    
    时间复杂度：O(n^log2(7)) ≈ O(n^2.81)
    空间复杂度：O(n^2)
    
    参数:
        A, B: 待相乘的矩阵
    
    返回:
        矩阵乘积A*B
    """
    # 检查矩阵是否合法
    if not A or not B or not A[0] or not B[0]:
        raise ValueError("矩阵不能为空")
    
    n = len(A)
    m = len(B[0])
    p = len(B)
    
    if len(A[0]) != p:
        raise ValueError("矩阵维度不匹配")
    
    # 对于小矩阵，使用传统乘法以提高效率
    if n <= 64:  # 阈值可以根据实际情况调整
        return _traditional_matrix_multiply(A, B)
    
    # 确保矩阵是方阵且大小为2的幂
    size = 1
    while size < n:
        size <<= 1
    
    # 扩展矩阵到2的幂大小
    A_ext = [[0] * size for _ in range(size)]
    B_ext = [[0] * size for _ in range(size)]
    
    for i in range(n):
        for j in range(n):
            A_ext[i][j] = A[i][j]
    
    for i in range(n):
        for j in range(n):
            B_ext[i][j] = B[i][j]
    
    # 使用Strassen算法计算
    C_ext = _strassen_helper(A_ext, B_ext, size)
    
    # 裁剪回原始大小
    C = [[0] * n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            C[i][j] = C_ext[i][j]
    
    return C

def _traditional_matrix_multiply(A, B):
    """
    传统矩阵乘法实现
    
    参数:
        A, B: 待相乘的矩阵
    
    返回:
        矩阵乘积A*B
    """
    n = len(A)
    m = len(B[0])
    p = len(B)
    
    result = [[0] * m for _ in range(n)]
    
    for i in range(n):
        for k in range(p):
            if A[i][k] == 0:
                continue  # 跳过零元素以优化性能
            for j in range(m):
                result[i][j] += A[i][k] * B[k][j]
    
    return result

def _strassen_helper(A, B, size):
    """
    Strassen算法递归辅助函数
    
    参数:
        A, B: 待相乘的矩阵（大小为2的幂）
        size: 矩阵大小
    
    返回:
        矩阵乘积A*B
    """
    if size == 1:
        return [[A[0][0] * B[0][0]]]
    
    new_size = size // 2
    
    # 分割矩阵
    A11 = [[A[i][j] for j in range(new_size)] for i in range(new_size)]
    A12 = [[A[i][j] for j in range(new_size, size)] for i in range(new_size)]
    A21 = [[A[i][j] for j in range(new_size)] for i in range(new_size, size)]
    A22 = [[A[i][j] for j in range(new_size, size)] for i in range(new_size, size)]
    
    B11 = [[B[i][j] for j in range(new_size)] for i in range(new_size)]
    B12 = [[B[i][j] for j in range(new_size, size)] for i in range(new_size)]
    B21 = [[B[i][j] for j in range(new_size)] for i in range(new_size, size)]
    B22 = [[B[i][j] for j in range(new_size, size)] for i in range(new_size, size)]
    
    # 计算7个中间矩阵
    M1 = _strassen_helper(_add_matrices(A11, A22), 
                          _add_matrices(B11, B22), new_size)
    M2 = _strassen_helper(_add_matrices(A21, A22), B11, new_size)
    M3 = _strassen_helper(A11, _subtract_matrices(B12, B22), new_size)
    M4 = _strassen_helper(A22, _subtract_matrices(B21, B11), new_size)
    M5 = _strassen_helper(_add_matrices(A11, A12), B22, new_size)
    M6 = _strassen_helper(_subtract_matrices(A21, A11), 
                          _add_matrices(B11, B12), new_size)
    M7 = _strassen_helper(_subtract_matrices(A12, A22), 
                          _add_matrices(B21, B22), new_size)
    
    # 计算结果矩阵的子矩阵
    C11 = _add_matrices(_subtract_matrices(_add_matrices(M1, M4), M5), M7)
    C12 = _add_matrices(M3, M5)
    C21 = _add_matrices(M2, M4)
    C22 = _add_matrices(_subtract_matrices(_add_matrices(M1, M3), M2), M6)
    
    # 合并结果矩阵
    C = [[0] * size for _ in range(size)]
    for i in range(new_size):
        for j in range(new_size):
            C[i][j] = C11[i][j]
            C[i][j + new_size] = C12[i][j]
            C[i + new_size][j] = C21[i][j]
            C[i + new_size][j + new_size] = C22[i][j]
    
    return C

def _add_matrices(A, B):
    """
    矩阵加法
    
    参数:
        A, B: 待相加的矩阵
    
    返回:
        矩阵和A+B
    """
    n = len(A)
    result = [[0] * n for _ in range(n)]
    
    for i in range(n):
        for j in range(n):
            result[i][j] = A[i][j] + B[i][j]
    
    return result

def _subtract_matrices(A, B):
    """
    矩阵减法
    
    参数:
        A, B: 待相减的矩阵
    
    返回:
        矩阵差A-B
    """
    n = len(A)
    result = [[0] * n for _ in range(n)]
    
    for i in range(n):
        for j in range(n):
            result[i][j] = A[i][j] - B[i][j]
    
    return result

# ==================== 补充题目10：最近点对问题 ====================
def closest_pair(points):
    """
    最近点对问题算法实现
    
    题目来源：经典算法题
    
    题目描述：
    给定平面上的n个点，找出距离最近的一对点。
    
    解题思路：
    1. 按x坐标排序所有点
    2. 使用分治法递归求解左右两部分的最近点对
    3. 处理跨越中间线的最近点对
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    
    参数:
        points: 点集，格式为[(x1, y1), (x2, y2), ...]
    
    返回:
        最近点对的距离
    """
    if len(points) < 2:
        raise ValueError("至少需要两个点")
    
    # 按x坐标排序
    points_sorted_by_x = sorted(points)
    
    # 创建一个按y坐标排序的数组，用于后续处理
    points_sorted_by_y = sorted(points, key=lambda p: p[1])
    
    return _closest_pair_helper(points_sorted_by_x, 0, len(points) - 1, points_sorted_by_y)

def _closest_pair_helper(points_sorted_by_x, left, right, points_sorted_by_y):
    """
    最近点对问题递归辅助函数
    
    参数:
        points_sorted_by_x: 按x坐标排序的点集
        left: 左边界
        right: 右边界
        points_sorted_by_y: 按y坐标排序的点集
    
    返回:
        最小距离
    """
    # 基本情况：少量点时直接计算
    if right - left <= 3:
        return _brute_force_distance(points_sorted_by_x[left:right+1])
    
    # 分解问题
    mid = left + (right - left) // 2
    mid_x = points_sorted_by_x[mid][0]
    
    # 划分按y排序的点集
    left_points_by_y = []
    right_points_by_y = []
    
    for p in points_sorted_by_y:
        if p[0] <= mid_x:
            left_points_by_y.append(p)
        else:
            right_points_by_y.append(p)
    
    # 递归求解左右两部分
    left_min = _closest_pair_helper(points_sorted_by_x, left, mid, left_points_by_y)
    right_min = _closest_pair_helper(points_sorted_by_x, mid + 1, right, right_points_by_y)
    
    # 合并结果
    min_dist = min(left_min, right_min)
    
    # 处理跨越中间线的点对
    strip = [p for p in points_sorted_by_y if abs(p[0] - mid_x) < min_dist]
    
    # 在带状区域中寻找可能的更近点对
    for i in range(len(strip)):
        # 只需检查后面不超过7个点（数学上可证明）
        j = i + 1
        while j < len(strip) and strip[j][1] - strip[i][1] < min_dist:
            min_dist = min(min_dist, _distance(strip[i], strip[j]))
            j += 1
    
    return min_dist

def _brute_force_distance(points):
    """
    暴力计算点集中的最小距离
    
    参数:
        points: 点集
    
    返回:
        最小距离
    """
    min_dist = float('inf')
    n = len(points)
    
    for i in range(n):
        for j in range(i + 1, n):
            min_dist = min(min_dist, _distance(points[i], points[j]))
    
    return min_dist

def _distance(p1, p2):
    """
    计算两点间的欧氏距离
    
    参数:
        p1, p2: 两个点
    
    返回:
        欧氏距离
    """
    return ((p1[0] - p2[0]) ** 2 + (p1[1] - p2[1]) ** 2) ** 0.5

# ==================== 补充题目11：Karatsuba大整数乘法 ====================
def karatsuba_multiply(num1: str, num2: str) -> str:
    """
    Karatsuba大整数乘法算法实现
    
    题目来源：经典算法题
    
    题目描述：
    实现Karatsuba算法进行大整数乘法运算
    
    解题思路：
    1. 将两个大整数分别拆分为高位和低位两部分
    2. 使用分治思想，将一次4次乘法减少为3次乘法
    3. 通过巧妙的组合方式计算结果
    
    时间复杂度：O(n^log₂3) ≈ O(n^1.585)
    空间复杂度：O(n)
    
    参数:
        num1, num2: 两个大整数的字符串表示
    
    返回:
        两个大整数的乘积
    """
    # 处理特殊情况
    if num1 == "0" or num2 == "0":
        return "0"
    
    # 调用递归辅助函数
    return _karatsuba_helper(num1, num2)

def _karatsuba_helper(num1: str, num2: str) -> str:
    """
    Karatsuba算法递归辅助函数
    
    参数:
        num1, num2: 两个大整数的字符串表示
    
    返回:
        两个大整数的乘积
    """
    # 基本情况：小数字直接计算
    if len(num1) < 10 or len(num2) < 10:
        return str(int(num1) * int(num2))
    
    # 使两个数字长度相等，用0填充较短的数字
    n = max(len(num1), len(num2))
    half = (n + 1) // 2
    
    # 补齐长度
    num1 = num1.zfill(n)
    num2 = num2.zfill(n)
    
    # 将数字分为两部分
    a = num1[:n-half]
    b = num1[n-half:]
    c = num2[:n-half]
    d = num2[n-half:]
    
    # 递归计算三个乘积
    ac = _karatsuba_helper(a, c)
    bd = _karatsuba_helper(b, d)
    abcd = _karatsuba_helper(str(int(a) + int(b)), str(int(c) + int(d)))
    
    # 计算ad + bc = (a+b)(c+d) - ac - bd
    ad_plus_bc = str(int(abcd) - int(ac) - int(bd))
    
    # 组合结果
    # result = ac * 10^(2*half) + (ad+bc) * 10^half + bd
    result = str(int(ac + '0' * (2 * half)) + int(ad_plus_bc + '0' * half) + int(bd))
    
    # 移除前导零
    return result.lstrip('0') or '0'

# 更新测试函数，添加补充题目测试
def test_additional():
    """
    测试方法，包括原始题目和补充题目
    """
    print("========== 原始测试：分治求数组最大值 ==========")
    
    # 测试用例1：普通数组
    arr1 = [3, 8, 7, 6, 4, 5, 1, 2]
    print("数组最大值 :", max_value(arr1))

    # 测试用例2：单元素数组
    arr2 = [42]
    print("单元素数组最大值 :", max_value(arr2))

    # 测试用例3：负数数组
    arr3 = [-5, -2, -8, -1]
    print("负数数组最大值 :", max_value(arr3))

    # 测试用例4：相同元素数组
    arr4 = [5, 5, 5, 5]
    print("相同元素数组最大值 :", max_value(arr4))

    # 测试用例5：大规模数组
    arr5 = list(range(10000))
    print("大规模数组最大值 :", max_value(arr5))

    # 异常测试：空数组
    try:
        arr6 = []
        max_value(arr6)
    except ValueError as e:
        print("空数组异常处理:", e)
    
    print("\n========== 题目1测试：LeetCode 53 最大子数组和 ==========")
    nums1 = [-2,1,-3,4,-1,2,1,-5,4]
    print("分治法结果:", max_sub_array(nums1))
    print("最优解(Kadane)结果:", max_sub_array_optimal(nums1))
    
    nums2 = [5,4,-1,7,8]
    print("测试用例2:", max_sub_array_optimal(nums2))
    
    print("\n========== 题目2测试：LeetCode 169 多数元素 ==========")
    nums3 = [3,2,3]
    print("分治法结果:", majority_element(nums3))
    print("最优解(摩尔投票)结果:", majority_element_optimal(nums3))
    
    nums4 = [2,2,1,1,1,2,2]
    print("测试用例2:", majority_element_optimal(nums4))
    
    print("\n========== 题目3测试：LeetCode 215 第K大元素 ==========")
    nums5 = [3,2,1,5,6,4]
    print("第2大元素:", find_kth_largest(nums5.copy(), 2))
    
    nums6 = [3,2,3,1,2,4,5,5,6]
    print("第4大元素:", find_kth_largest(nums6.copy(), 4))
    
    print("\n========== 题目4测试：LeetCode 240 搜索矩阵 ==========")
    matrix = [
        [1,4,7,11,15],
        [2,5,8,12,19],
        [3,6,9,16,22],
        [10,13,14,17,24],
        [18,21,23,26,30]
    ]
    print("搜索5:", search_matrix(matrix, 5))
    print("搜索20:", search_matrix(matrix, 20))
    
    # 补充题目测试
    print("\n========== 补充题目测试 ==========\n")
    
    # 测试归并排序
    print("1. 归并排序测试:")
    nums_merge = [9, 3, 7, 1, 5, 8, 2, 6, 4]
    print(f"排序前: {nums_merge}")
    sorted_nums = merge_sort(nums_merge)
    print(f"排序后: {sorted_nums}")
    
    # 测试二分查找
    print("\n2. 二分查找测试:")
    nums_binary = [1, 2, 3, 4, 5, 6, 7, 8, 9]
    print(f"查找5: 索引 = {binary_search(nums_binary, 5)}")
    print(f"查找10: 索引 = {binary_search_optimal(nums_binary, 10)}")
    
    # 测试快速幂
    print("\n3. 快速幂测试:")
    print(f"2^10 = {quick_pow(2, 10)}")
    print(f"2^-2 = {quick_pow_optimal(2, -2)}")
    
    # 测试最大子矩阵和
    print("\n4. 最大子矩阵和测试:")
    matrix = [
        [1, 2, -1, -4, -20],
        [-8, -3, 4, 2, 1],
        [3, 8, 10, 1, 3],
        [-4, -1, 1, 7, -6]
    ]
    print(f"最大子矩阵和 = {max_sub_matrix(matrix)}")
    
    # 测试最近点对问题
    print("\n5. 最近点对问题测试:")
    points = [(0, 0), (3, 0), (0, 4), (1, 1), (2, 2)]
    print(f"最近点对距离 = {closest_pair(points):.6f}")
    
    # 测试Karatsuba大整数乘法
    print("\n6. Karatsuba大整数乘法测试:")
    print(f"123456789 * 987654321 = {karatsuba_multiply('123456789', '987654321')}")
    print(f"0 * 12345 = {karatsuba_multiply('0', '12345')}")
    print(f"9999999999 * 9999999999 = {karatsuba_multiply('9999999999', '9999999999')}")

# 运行测试
if __name__ == "__main__":
    test()
    print("\n" + "="*50)
    print("补充题目测试:")
    print("="*50)
    test_additional()