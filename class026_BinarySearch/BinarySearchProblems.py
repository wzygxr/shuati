#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
二分查找算法实现与相关题目解答 (Python版本)

本文件包含了二分查找的各种实现和在不同场景下的应用
涵盖了LeetCode、Codeforces、SPOJ等平台的经典题目
"""


def search(nums, target):
    """
    基础二分查找
    在有序数组中查找目标值
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param nums: 有序数组
    :param target: 目标值
    :return: 目标值的索引，如果不存在返回-1
    """
    if not nums:
        return -1
    
    left, right = 0, len(nums) - 1
    while left <= right:
        # 使用位运算避免整数溢出
        mid = left + ((right - left) >> 1)
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    return -1


def search_insert(nums, target):
    """
    查找插入位置
    在有序数组中查找目标值应该插入的位置
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param nums: 有序数组
    :param target: 目标值
    :return: 插入位置索引
    """
    if not nums:
        return 0
    
    left, right = 0, len(nums)
    while left < right:
        mid = left + ((right - left) >> 1)
        if nums[mid] < target:
            left = mid + 1
        else:
            right = mid
    return left


def search_range(nums, target):
    """
    查找元素的第一个和最后一个位置
    在有序数组中查找目标值的起始和结束位置
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param nums: 有序数组
    :param target: 目标值
    :return: [起始位置, 结束位置]，如果不存在返回[-1, -1]
    """
    if not nums:
        return [-1, -1]
    
    first = find_left(nums, target)
    # 如果找不到>=target的元素，或者该元素不等于target，则说明target不存在
    if first == len(nums) or nums[first] != target:
        return [-1, -1]
    
    last = find_right(nums, target)
    return [first, last]


def find_left(nums, target):
    """
    辅助方法：查找>=target的最左位置
    """
    left, right = 0, len(nums)
    while left < right:
        mid = left + ((right - left) >> 1)
        if nums[mid] < target:
            left = mid + 1
        else:
            right = mid
    return left


def find_right(nums, target):
    """
    辅助方法：查找<=target的最右位置
    """
    left, right = 0, len(nums)
    while left < right:
        mid = left + ((right - left) >> 1)
        if nums[mid] <= target:
            left = mid + 1
        else:
            right = mid
    return left - 1


def is_perfect_square(num):
    """
    有效的完全平方数
    判断一个数是否是完全平方数
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param num: 正整数
    :return: 如果是完全平方数返回True，否则返回False
    """
    if num < 1:
        return False
    if num == 1:
        return True
    
    left, right = 1, num // 2  # 一个数的平方根不会超过它的一半(除了1)
    while left <= right:
        mid = left + ((right - left) >> 1)
        square = mid * mid
        if square == num:
            return True
        elif square > num:
            right = mid - 1
        else:
            left = mid + 1
    return False


def find_peak_element(nums):
    """
    寻找峰值元素
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param nums: 数组
    :return: 峰值元素的索引
    """
    left, right = 0, len(nums) - 1
    while left < right:
        mid = left + ((right - left) >> 1)
        if nums[mid] > nums[mid + 1]:
            right = mid
        else:
            left = mid + 1
    return left


def find_min(nums):
    """
    寻找旋转排序数组中的最小值
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param nums: 旋转排序数组
    :return: 数组中的最小值
    """
    left, right = 0, len(nums) - 1
    while left < right:
        mid = left + ((right - left) >> 1)
        if nums[mid] < nums[right]:
            right = mid
        else:
            left = mid + 1
    return nums[left]


def search_matrix(matrix, target):
    """
    搜索二维矩阵
    
    时间复杂度: O(log(m*n))
    空间复杂度: O(1)
    
    :param matrix: 二维矩阵
    :param target: 目标值
    :return: 如果找到目标值返回True，否则返回False
    """
    if not matrix or not matrix[0]:
        return False
    
    m, n = len(matrix), len(matrix[0])
    left, right = 0, m * n - 1
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        mid_value = matrix[mid // n][mid % n]
        
        if mid_value == target:
            return True
        elif mid_value < target:
            left = mid + 1
        else:
            right = mid - 1
    
    return False


def search_in_rotated_sorted_array(nums, target):
    """
    搜索旋转排序数组
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param nums: 旋转排序数组
    :param target: 目标值
    :return: 目标值的索引，如果不存在返回-1
    """
    left, right = 0, len(nums) - 1
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if nums[mid] == target:
            return mid
        
        # 左半部分有序
        if nums[left] <= nums[mid]:
            if nums[left] <= target < nums[mid]:
                right = mid - 1
            else:
                left = mid + 1
        # 右半部分有序
        else:
            if nums[mid] < target <= nums[right]:
                left = mid + 1
            else:
                right = mid - 1
    
    return -1


def find_duplicate(nums):
    """
    寻找重复数
    
    时间复杂度: O(n log n)
    空间复杂度: O(1)
    
    :param nums: 包含重复数字的数组
    :return: 重复的数字
    """
    left, right = 1, len(nums) - 1
    
    while left < right:
        mid = left + ((right - left) >> 1)
        count = 0
        
        # 计算小于等于mid的数字个数
        for num in nums:
            if num <= mid:
                count += 1
        
        # 根据抽屉原理判断重复数字在哪一侧
        if count > mid:
            right = mid
        else:
            left = mid + 1
    
    return left


def how_many_drinks(prices, queries):
    """
    Codeforces 706B. Interesting drink
    
    时间复杂度: O(n log n + q log n)
    空间复杂度: O(1)
    
    :param prices: 饮料价格数组
    :param queries: 查询金额数组
    :return: 每个查询可以购买的饮料种类数
    """
    prices.sort()
    result = []
    
    for money in queries:
        left, right = 0, len(prices)
        
        while left < right:
            mid = left + ((right - left) >> 1)
            if prices[mid] <= money:
                left = mid + 1
            else:
                right = mid
        
        result.append(left)
    
    return result


def my_sqrt(x):
    """
    LeetCode 69. Sqrt(x)
    
    时间复杂度: O(log x)
    空间复杂度: O(1)
    
    :param x: 输入的非负整数
    :return: x 的平方根的整数部分
    """
    if x == 0 or x == 1:
        return x
    
    left, right = 1, x
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        # 防止整数溢出，使用除法而不是乘法
        if mid > x // mid:
            right = mid - 1
        else:
            # 如果下一个值会溢出或者大于x/mid，则当前mid是最大的有效平方根
            if mid + 1 > x // (mid + 1):
                return mid
            left = mid + 1
    
    return right


def guess_number(n):
    """
    LeetCode 374. 猜数字大小
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param n: 数字范围上限
    :return: 猜中的数字
    """
    left, right = 1, n
    
    while left <= right:
        # 防止整数溢出
        mid = left + ((right - left) >> 1)
        result = guess(mid)
        
        if result == 0:
            return mid  # 猜对了
        elif result == 1:
            left = mid + 1  # 猜小了，往右边找
        else:
            right = mid - 1  # 猜大了，往左边找
    
    return -1  # 理论上不会执行到这里


def guess(num):
    """
    模拟API：判断版本是否错误
    """
    pick = 6  # 假设选中的数字是6
    if num < pick:
        return 1
    elif num > pick:
        return -1
    else:
        return 0


def min_eating_speed(piles, h):
    """
    LeetCode 875. 爱吃香蕉的珂珂
    
    时间复杂度: O(n log maxPile)
    空间复杂度: O(1)
    
    :param piles: 香蕉堆数组
    :param h: 警卫离开的时间（小时）
    :return: 最小的吃香蕉速度
    """
    left = 1
    right = max(piles)  # 最大的香蕉堆，作为右边界
    
    while left < right:
        mid = left + ((right - left) >> 1)
        
        if can_finish(piles, h, mid):
            right = mid  # 可以吃完，尝试更小的速度
        else:
            left = mid + 1  # 不能吃完，需要更大的速度
    
    return left


def can_finish(piles, h, speed):
    """
    辅助方法：判断以速度speed是否能在h小时内吃完所有香蕉
    """
    time = 0
    
    for pile in piles:
        # 计算吃完当前堆需要的时间
        time += (pile + speed - 1) // speed  # 等价于 math.ceil(pile / speed)
        
        # 如果时间已经超过h，可以提前返回False
        if time > h:
            return False
    
    return time <= h


def ship_within_days(weights, days):
    """
    LeetCode 1011. 在D天内送达包裹的能力
    
    时间复杂度: O(n log totalWeight)
    空间复杂度: O(1)
    
    :param weights: 包裹重量数组
    :param days: 天数限制
    :return: 船的最小运载能力
    """
    left = max(weights)  # 最小运载能力：最大的单个包裹重量
    right = sum(weights)  # 最大运载能力：所有包裹的总重量
    
    while left < right:
        mid = left + ((right - left) >> 1)
        
        if can_ship_in_days(weights, days, mid):
            right = mid  # 可以在days天内运完，尝试更小的运载能力
        else:
            left = mid + 1  # 不能在days天内运完，需要更大的运载能力
    
    return left


def can_ship_in_days(weights, days, capacity):
    """
    辅助方法：判断以capacity的运载能力是否能在days天内运完所有包裹
    """
    current_weight = 0
    day_count = 1  # 至少需要1天
    
    for weight in weights:
        # 如果当前包裹的重量已经超过了运载能力，不可能运完
        if weight > capacity:
            return False
        
        # 如果当前累计重量加上当前包裹的重量超过了运载能力，需要新的一天
        if current_weight + weight > capacity:
            day_count += 1
            current_weight = weight  # 新的一天从当前包裹开始
            
            # 如果天数已经超过了限制，可以提前返回False
            if day_count > days:
                return False
        else:
            current_weight += weight  # 继续往当前天添加包裹
    
    return day_count <= days


def min_bullets(points):
    """
    AtCoder ABC023D - 射撃王 (Shooting King)
    
    时间复杂度: O(N^3 log N)
    空间复杂度: O(N)
    
    :param points: 目标点数组，每个点是(x, y)
    :return: 最少需要的子弹数
    """
    n = len(points)
    if n == 0:
        return 0
    if n == 1:
        return 1
    
    left, right = 1, n
    while left < right:
        mid = left + ((right - left) >> 1)
        if can_cover(points, mid):
            right = mid
        else:
            left = mid + 1
    
    return left


def can_cover(points, k):
    """
    辅助方法：判断是否可以用k条直线覆盖所有点
    """
    covered = [False] * len(points)
    return cover_recursive(points, covered, 0, k)


def cover_recursive(points, covered, covered_count, remaining_lines):
    """
    递归辅助方法：尝试覆盖剩余的点
    """
    n = len(points)
    if covered_count == n:
        return True
    if remaining_lines == 0:
        return False
    
    # 找到第一个未覆盖的点
    first = -1
    for i in range(n):
        if not covered[i]:
            first = i
            break
    
    # 尝试通过第一个未覆盖的点画一条直线
    for i in range(n):
        if i == first or covered[i]:
            continue
        
        # 标记所有在这条直线上的点
        new_covered = covered.copy()
        new_count = covered_count
        
        for j in range(n):
            if not new_covered[j] and is_collinear(points[first], points[i], points[j]):
                new_covered[j] = True
                new_count += 1
        
        if cover_recursive(points, new_covered, new_count, remaining_lines - 1):
            return True
    
    # 如果没有其他点，可以单独用一条直线覆盖第一个未覆盖的点
    covered[first] = True
    result = cover_recursive(points, covered, covered_count + 1, remaining_lines - 1)
    covered[first] = False  # 回溯
    return result


def is_collinear(p1, p2, p3):
    """
    辅助方法：判断三个点是否共线
    """
    # 使用叉积判断三点共线
    # (y2 - y1) * (x3 - x1) == (y3 - y1) * (x2 - x1)
    return (p2[1] - p1[1]) * (p3[0] - p1[0]) == (p3[1] - p1[1]) * (p2[0] - p1[0])


def split_array(nums, m):
    """
    LeetCode 410. 分割数组的最大值
    
    时间复杂度: O(n * log(sum - max))
    空间复杂度: O(1)
    
    :param nums: 非负整数数组
    :param m: 分割的子数组数量
    :return: 最小的最大子数组和
    """
    # 边界检查
    if not nums or m <= 0:
        return 0
    
    left = 0  # 最小可能值：数组中的最大值
    right = 0  # 最大可能值：数组元素总和
    
    for num in nums:
        left = max(left, num)
        right += num
    
    while left < right:
        mid = left + ((right - left) >> 1)
        
        # 检查是否能在限制下分成m个或更少的子数组
        if can_split(nums, m, mid):
            right = mid  # 可以分割，尝试更小的最大值
        else:
            left = mid + 1  # 不能分割，需要更大的最大值
    
    return left


def can_split(nums, count, max_sum):
    """
    辅助方法：检查是否能在max_sum限制下将数组分成count个或更少的子数组
    """
    splits = 1  # 至少有一个子数组
    current_sum = 0
    
    for num in nums:
        if current_sum + num > max_sum:
            splits += 1  # 需要新的子数组
            current_sum = num
            
            if splits > count:
                return False  # 超过了允许的子数组数量
        else:
            current_sum += num
    
    return True


def find_median_sorted_arrays(nums1, nums2):
    """
    LeetCode 4. 寻找两个正序数组的中位数
    
    时间复杂度: O(log(min(m,n)))
    空间复杂度: O(1)
    
    :param nums1: 第一个正序数组
    :param nums2: 第二个正序数组
    :return: 两个数组的中位数
    """
    # 确保nums1是较短的数组
    if len(nums1) > len(nums2):
        nums1, nums2 = nums2, nums1
    
    m, n = len(nums1), len(nums2)
    left, right = 0, m
    
    while left <= right:
        partition1 = left + ((right - left) >> 1)
        partition2 = ((m + n + 1) >> 1) - partition1
        
        max_left1 = float('-inf') if partition1 == 0 else nums1[partition1 - 1]
        min_right1 = float('inf') if partition1 == m else nums1[partition1]
        
        max_left2 = float('-inf') if partition2 == 0 else nums2[partition2 - 1]
        min_right2 = float('inf') if partition2 == n else nums2[partition2]
        
        if max_left1 <= min_right2 and max_left2 <= min_right1:
            # 找到正确的分割点
            if (m + n) % 2 == 0:
                return (max(max_left1, max_left2) + min(min_right1, min_right2)) / 2.0
            else:
                return max(max_left1, max_left2)
        elif max_left1 > min_right2:
            right = partition1 - 1
        else:
            left = partition1 + 1
    
    raise ValueError("输入数组不合法")


def get_number_of_k(nums, target):
    """
    牛客/剑指Offer: 数字在排序数组中出现的次数
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param nums: 排序数组
    :param target: 目标值
    :return: 出现次数
    """
    if not nums:
        return 0
    
    first = find_left(nums, target)
    if first == len(nums) or nums[first] != target:
        return 0
    
    last = find_right(nums, target)
    return last - first + 1


def single_non_duplicate(nums):
    """
    LeetCode 540. 有序数组中的单一元素
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param nums: 有序数组
    :return: 单一元素
    """
    left, right = 0, len(nums) - 1
    
    while left < right:
        mid = left + ((right - left) >> 1)
        
        # 确保mid是偶数索引
        if mid % 2 == 1:
            mid -= 1
        
        # 检查成对关系
        if nums[mid] == nums[mid + 1]:
            # 单一元素在右侧
            left = mid + 2
        else:
            # 单一元素在左侧或就是mid
            right = mid
    
    return nums[left]


def min_days_to_bloom(bloom_day, m, k):
    """
    LeetCode 1482. 制作 m 束花所需的最少天数
    
    时间复杂度: O(n * log(max - min))
    空间复杂度: O(1)
    
    :param bloom_day: 每朵花开放的天数
    :param m: 需要的花束数量
    :param k: 每束花包含的花朵数量
    :return: 最少等待天数
    """
    # 边界检查：如果花的总数不足以制作m束花，返回-1
    if m * k > len(bloom_day):
        return -1
    
    left = min(bloom_day)
    right = max(bloom_day)
    
    while left < right:
        mid = left + ((right - left) >> 1)
        
        if can_make_bouquets(bloom_day, m, k, mid):
            right = mid  # 可以制作，尝试更少的天数
        else:
            left = mid + 1  # 不能制作，需要更多的天数
    
    return left


def can_make_bouquets(bloom_day, m, k, day):
    """
    辅助方法：检查在给定天数内能否制作m束花
    """
    bouquets = 0  # 已制作的花束数
    flowers = 0   # 当前连续开放的花朵数
    
    for bloom in bloom_day:
        if bloom <= day:
            flowers += 1
            if flowers == k:
                bouquets += 1
                flowers = 0
                if bouquets == m:
                    return True
        else:
            flowers = 0  # 遇到未开放的花，重置计数
    
    return bouquets >= m


def min_number_in_rotate_array(nums):
    """
    牛客网: 旋转数组的最小数字
    题目来源: https://www.nowcoder.com/practice/9f3231a991af4f55b95579b44b7a01ba
    
    题目描述:
    把一个数组最开始的若干个元素搬到数组的末尾,我们称之为数组的旋转。
    输入一个非递减排序的数组的一个旋转,输出旋转数组的最小元素。
    
    思路分析:
    1. 二分查找法,比较中间元素与右边界元素
    2. 如果中间元素小于右边界,最小值在左侧
    3. 如果中间元素大于右边界,最小值在右侧
    4. 如果相等,右边界左移一位
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    是否最优解: 是
    
    :param nums: 旋转数组
    :return: 最小元素
    """
    if not nums:
        return 0
    left, right = 0, len(nums) - 1
    while left < right:
        mid = left + ((right - left) >> 1)
        if nums[mid] < nums[right]:
            right = mid
        elif nums[mid] > nums[right]:
            left = mid + 1
        else:
            right -= 1
    return nums[left]


def cube_root(n):
    """
    acwing: 数的三次方根
    题目来源: https://www.acwing.com/problem/content/792/
    
    题目描述:
    给定一个浮点数n,求它的三次方根,结果保留6位小数。
    
    思路分析:
    1. 二分查找法,在[-10000, 10000]范围内搜索
    2. 精度控制到1e-8
    3. 根据mid^3与n的大小关系调整搜索区间
    
    时间复杂度: O(log(范围/精度))
    空间复杂度: O(1)
    是否最优解: 是
    
    :param n: 输入浮点数
    :return: 三次方根
    """
    left, right = -10000.0, 10000.0
    precision = 1e-8
    
    while right - left > precision:
        mid = (left + right) / 2
        if mid * mid * mid >= n:
            right = mid
        else:
            left = mid
    
    return left


def find_closest_sum(A, B, C, X):
    """
    杭电OJ: 查找最接近的元素
    题目来源: http://acm.hdu.edu.cn/showproblem.php?pid=2141
    
    题目描述:
    给定三个数组A、B、C，以及多个查询X。对于每个查询，判断是否存在a∈A, b∈B, c∈C，使得a+b+c=X。
    
    思路分析:
    1. 将A+B的所有可能和存储在一个数组中
    2. 对这个和数组进行排序
    3. 对于每个查询X，使用二分查找在A+B的和数组中查找是否存在X-c（c∈C）
    
    时间复杂度: O(L*M + N*log(L*M))
    空间复杂度: O(L*M)
    是否最优解: 是
    
    :param A: 数组A
    :param B: 数组B
    :param C: 数组C
    :param X: 查询值
    :return: 是否存在满足条件的组合
    """
    n = len(A)
    sumAB = []
    for a in A:
        for b in B:
            sumAB.append(a + b)
    sumAB.sort()
    
    for c in C:
        target = X - c
        left, right = 0, len(sumAB) - 1
        while left <= right:
            mid = left + ((right - left) >> 1)
            if sumAB[mid] == target:
                return True
            elif sumAB[mid] < target:
                left = mid + 1
            else:
                right = mid - 1
    
    return False


def monthly_expense(expenses, M):
    """
    POJ: 月度开销
    题目来源: http://poj.org/problem?id=3273
    
    题目描述:
    FJ需要将连续的N天划分为M个时间段，每个时间段的花费是该时间段内每天花费的总和。
    他希望最小化所有时间段中花费的最大值。
    
    思路分析:
    1. 二分答案法：答案范围在[max(expenses), sum(expenses)]之间
    2. 对于每个可能的答案mid，检查能否将数组分成<=M个子数组且每个子数组和<=mid
    3. 如果可以，说明答案可能更小，向左搜索；否则向右搜索
    
    时间复杂度: O(n log(sum))
    空间复杂度: O(1)
    是否最优解: 是
    
    :param expenses: 每天的开销数组
    :param M: 划分的时间段数
    :return: 最小的最大开销
    """
    left, right = 0, 0
    for expense in expenses:
        left = max(left, expense)
        right += expense
    
    def can_split(max_sum):
        count, current_sum = 1, 0
        for expense in expenses:
            if current_sum + expense > max_sum:
                count += 1
                current_sum = expense
                if count > M:
                    return False
            else:
                current_sum += expense
        return True
    
    while left < right:
        mid = left + ((right - left) >> 1)
        if can_split(mid):
            right = mid
        else:
            left = mid + 1
    
    return left


def cut_trees(trees, M):
    """
    洛谷: 砍树
    题目来源: https://www.luogu.com.cn/problem/P1873
    
    题目描述:
    伐木工人需要砍倒M米高的树木，每棵树的高度不同。
    伐木工人使用一个设定高度H的锯子，高于H的树木会被砍下高出的部分。
    请帮助伐木工人确定锯子的高度H，使得被砍下的树木总长度刚好等于M。
    
    思路分析:
    1. 二分答案法：高度范围在[0, max(treeHeights)]之间
    2. 对于每个候选高度mid，计算能砍下的总长度
    3. 如果总长度>=M，可以尝试更高的高度；否则需要降低高度
    
    时间复杂度: O(n log(maxHeight))
    空间复杂度: O(1)
    是否最优解: 是
    
    :param trees: 树的高度数组
    :param M: 需要砍下的总长度
    :return: 锯子的设定高度
    """
    left, right = 0, 0
    for tree in trees:
        right = max(right, tree)
    
    result = 0
    while left <= right:
        mid = left + ((right - left) >> 1)
        total = 0
        for tree in trees:
            if tree > mid:
                total += tree - mid
        if total >= M:
            result = mid
            left = mid + 1
        else:
            right = mid - 1
    
    return result


def angry_cows(haybales):
    """
    USACO: 愤怒的奶牛
    题目来源: http://www.usaco.org/index.php?page=viewproblem2&cpid=592
    
    题目描述:
    在一个一维的场地中放置了若干堆干草，奶牛被点燃后会向左右两个方向传播爆炸。
    爆炸的传播速度是每秒1单位距离。求最小的爆炸半径R，使得点燃任意一个干草堆都能引爆所有干草堆。
    
    思路分析:
    1. 二分答案法：半径范围在[0, maxPosition - minPosition]之间
    2. 对于每个候选半径mid，检查是否能通过点燃一个点引爆所有点
    3. 贪心验证：从最左边开始，每次尽可能远地放置引爆点
    
    时间复杂度: O(n log(maxDistance))
    空间复杂度: O(1)
    是否最优解: 是
    
    :param haybales: 干草堆的位置数组
    :return: 最小爆炸半径
    """
    haybales.sort()
    left, right = 0, haybales[-1] - haybales[0]
    
    def can_explode_all(radius):
        count = 1
        last_pos = haybales[0]
        for i in range(1, len(haybales)):
            if haybales[i] - last_pos > 2 * radius:
                count += 1
                last_pos = haybales[i]
        return count <= 1
    
    while left < right:
        mid = left + ((right - left) >> 1)
        if can_explode_all(mid):
            right = mid
        else:
            left = mid + 1
    
    return left


def minimize_maximum(nums, K):
    """
    HackerRank: 最小化最大值
    题目来源: https://www.hackerrank.com/challenges/min-max/problem
    
    题目描述:
    将一个数组分割成K个非空的连续子数组，设计一个算法使得这K个子数组各自和的最大值最小。
    
    思路分析:
    1. 二分答案法：答案范围在[max(nums), sum(nums)]之间
    2. 对于每个可能的答案mid，检查能否将数组分成<=K个子数组且每个子数组和<=mid
    3. 如果可以，说明答案可能更小，向左搜索；否则向右搜索
    
    时间复杂度: O(n log(sum))
    空间复杂度: O(1)
    是否最优解: 是
    
    :param nums: 数组
    :param K: 分割的子数组数量
    :return: 最小的最大子数组和
    """
    left, right = 0, 0
    for num in nums:
        left = max(left, num)
        right += num
    
    def can_split(max_sum):
        count, current_sum = 1, 0
        for num in nums:
            if current_sum + num > max_sum:
                count += 1
                current_sum = num
                if count > K:
                    return False
            else:
                current_sum += num
        return True
    
    while left < right:
        mid = left + ((right - left) >> 1)
        if can_split(mid):
            right = mid
        else:
            left = mid + 1
    
    return left


def jump_stones(rocks, M, L):
    """
    计蒜客: 跳石头
    题目来源: https://nanti.jisuanke.com/t/T1201
    
    题目描述:
    河道中分布着一些巨大岩石，组委会计划移走一些岩石，使得选手们在比赛过程中的最短跳跃距离尽可能长。
    
    思路分析:
    1. 二分答案法：距离范围在[0, L]之间
    2. 对于每个候选距离mid，计算需要移走多少块岩石
    3. 如果需要移走的岩石数不超过M，则可以尝试更大的距离
    
    时间复杂度: O(n log L)
    空间复杂度: O(1)
    是否最优解: 是
    
    :param rocks: 岩石位置数组
    :param M: 最多可移走的岩石数
    :param L: 河道长度
    :return: 最大的最短跳跃距离
    """
    rocks.sort()
    left, right, result = 0, L, 0
    
    def can_jump(min_dist):
        count, last_pos = 0, 0
        for rock in rocks:
            if rock - last_pos < min_dist:
                count += 1
            else:
                last_pos = rock
        if L - last_pos < min_dist:
            count += 1
        return count <= M
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        if can_jump(mid):
            result = mid
            left = mid + 1
        else:
            right = mid - 1
    
    return result


def find_worm_piles(piles, queries):
    """
    Codeforces 474B - Worms
    题目来源: https://codeforces.com/problemset/problem/474/B
    
    题目描述:
    有n堆虫子，第i堆有a_i条虫子。每条虫子都有一个编号，从1开始连续编号。
    给出m个查询，每个查询给出一个编号x，问编号为x的虫子属于哪一堆。
    
    思路分析:
    1. 使用前缀和数组记录每堆虫子的结束位置
    2. 对于每个查询，使用二分查找在前缀和数组中查找x所在的位置
    3. 找到第一个前缀和大于等于x的位置
    
    时间复杂度: O(n + m log n)
    空间复杂度: O(n)
    是否最优解: 是
    
    :param piles: 每堆虫子的数量数组
    :param queries: 查询数组
    :return: 每个查询对应的堆编号
    """
    n, m = len(piles), len(queries)
    
    # 计算前缀和
    prefix_sum = [0] * n
    prefix_sum[0] = piles[0]
    for i in range(1, n):
        prefix_sum[i] = prefix_sum[i - 1] + piles[i]
    
    result = [0] * m
    
    for i in range(m):
        x = queries[i]
        
        # 使用二分查找找到第一个前缀和 >= x 的位置
        left, right = 0, n - 1
        pile_index = -1
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            
            if prefix_sum[mid] >= x:
                pile_index = mid
                right = mid - 1
            else:
                left = mid + 1
        
        result[i] = pile_index + 1  # 1-based索引
    
    return result


def find_max_min(nums):
    """
    SPOJ题目: 最大最小值问题
    查找数组中的最大值和最小值
    
    时间复杂度: O(n)
    空间复杂度: O(1)
    
    :param nums: 数组
    :return: (最大值, 最小值)
    """
    if not nums:
        return (None, None)
    
    max_val = min_val = nums[0]
    for num in nums[1:]:
        if num > max_val:
            max_val = num
        elif num < min_val:
            min_val = num
    
    return (max_val, min_val)


def aggressive_cows(positions, cows):
    """
    SPOJ - AGGRCOW (Aggressive cows)
    题目来源: https://www.spoj.com/problems/AGGRCOW/
    
    题目描述:
    农夫约翰建造了一个有 C 个摊位的畜棚，摊位位于 x0, ..., x(C-1)。
    他的 M 头奶牛总是相互攻击，约翰必须以某种方式分配奶牛到摊位，使它们之间的最小距离尽可能大。
    
    思路分析:
    1. 二分答案法：距离范围在[0, maxPosition - minPosition]之间
    2. 对于每个候选距离mid，使用贪心算法验证是否可以放置所有奶牛
    3. 如果可以，说明可以尝试更大的距离；否则需要减小距离
    
    时间复杂度: O(n log(max-min) * n)
    空间复杂度: O(1)
    是否最优解: 是
    
    :param positions: 摊位位置数组
    :param cows: 奶牛数量
    :return: 最大的最小距离
    """
    positions.sort()
    left, right = 0, positions[-1] - positions[0]
    result = 0
    
    def can_place_cows(min_dist):
        count = 1
        last_position = positions[0]
        
        for i in range(1, len(positions)):
            if positions[i] - last_position >= min_dist:
                count += 1
                last_position = positions[i]
                if count == cows:
                    return True
        
        return False
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        if can_place_cows(mid):
            result = mid
            left = mid + 1
        else:
            right = mid - 1
    
    return result


# 测试方法
if __name__ == "__main__":
    # 测试基础二分查找
    nums1 = [-1, 0, 3, 5, 9, 12]
    print("基础二分查找测试:")
    print("查找9:", search(nums1, 9))  # 应该输出4
    print("查找2:", search(nums1, 2))  # 应该输出-1

    # 测试查找插入位置
    nums2 = [1, 3, 5, 6]
    print("\n查找插入位置测试:")
    print("查找5:", search_insert(nums2, 5))  # 应该输出2
    print("查找2:", search_insert(nums2, 2))  # 应该输出1
    print("查找7:", search_insert(nums2, 7))  # 应该输出4
    print("查找0:", search_insert(nums2, 0))  # 应该输出0

    # 测试查找范围
    nums3 = [5, 7, 7, 8, 8, 10]
    print("\n查找范围测试:")
    print("查找8:", search_range(nums3, 8))  # 应该输出[3, 4]
    print("查找6:", search_range(nums3, 6))  # 应该输出[-1, -1]

    # 测试完全平方数
    print("\n完全平方数测试:")
    print("16是完全平方数:", is_perfect_square(16))  # 应该输出True
    print("14是完全平方数:", is_perfect_square(14))  # 应该输出False

    # 测试寻找峰值
    nums4 = [1, 2, 3, 1]
    print("\n寻找峰值测试:")
    print("峰值索引:", find_peak_element(nums4))  # 应该输出2

    # 测试寻找旋转数组最小值
    nums5 = [3, 4, 5, 1, 2]
    print("\n寻找旋转数组最小值测试:")
    print("最小值:", find_min(nums5))  # 应该输出1

    # 测试搜索二维矩阵
    matrix = [[1, 3, 5, 7], [10, 11, 16, 20], [23, 30, 34, 60]]
    print("\n搜索二维矩阵测试:")
    print("查找3:", search_matrix(matrix, 3))  # 应该输出True
    print("查找13:", search_matrix(matrix, 13))  # 应该输出False

    # 测试搜索旋转排序数组
    nums6 = [4, 5, 6, 7, 0, 1, 2]
    print("\n搜索旋转排序数组测试:")
    print("查找0:", search_in_rotated_sorted_array(nums6, 0))  # 应该输出4
    print("查找3:", search_in_rotated_sorted_array(nums6, 3))  # 应该输出-1

    # 测试寻找重复数
    nums7 = [1, 3, 4, 2, 2]
    print("\n寻找重复数测试:")
    print("重复数:", find_duplicate(nums7))  # 应该输出2

    # 测试Codeforces题目
    prices = [1, 2, 3, 4, 5]
    queries = [3, 10, 1]
    drinks = how_many_drinks(prices, queries)
    print("\nCodeforces题目测试:")
    print("可以购买的饮料种类数:", drinks)  # 应该输出[3, 5, 1]

    # 测试SPOJ题目
    positions = [1, 2, 8, 4, 9]
    print("\nSPOJ题目测试:")
    print("最大最小距离:", aggressive_cows(positions, 3))  # 应该输出3
    
    # 测试LeetCode 69: Sqrt(x)
    print("\nLeetCode 69: Sqrt(x)测试:")
    print("sqrt(4) = ", my_sqrt(4))  # 应该输出2
    print("sqrt(8) = ", my_sqrt(8))  # 应该输出2
    print("sqrt(256) = ", my_sqrt(256))  # 应该输出16
    print("sqrt(2147395600) = ", my_sqrt(2147395600))  # 应该输出46340
    
    # 测试LeetCode 374: 猜数字大小
    print("\nLeetCode 374: 猜数字大小测试:")
    print("猜数字(10) = ", guess_number(10))  # 应该输出6（假设pick=6）
    
    # 测试LeetCode 875: 爱吃香蕉的珂珂
    print("\nLeetCode 875: 爱吃香蕉的珂珂测试:")
    piles1 = [3, 6, 7, 11]
    h1 = 8
    print("最小吃香蕉速度:", min_eating_speed(piles1, h1))  # 应该输出4
    
    piles2 = [30, 11, 23, 4, 20]
    h2 = 5
    print("最小吃香蕉速度:", min_eating_speed(piles2, h2))  # 应该输出30
    
    # 测试LeetCode 1011: 在D天内送达包裹的能力
    print("\nLeetCode 1011: 在D天内送达包裹的能力测试:")
    weights1 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    days1 = 5
    print("最小运载能力:", ship_within_days(weights1, days1))  # 应该输出15
    
    weights2 = [3, 2, 2, 4, 1, 4]
    days2 = 3
    print("最小运载能力:", ship_within_days(weights2, days2))  # 应该输出6
    
    # 测试AtCoder ABC023D: 射撃王
    print("\nAtCoder ABC023D: 射撃王测试:")
    points1 = [[0, 0], [1, 1], [2, 2], [3, 3], [4, 4]]
    print("最少子弹数:", min_bullets(points1))  # 应该输出1
    
    points2 = [[0, 0], [1, 0], [0, 1], [1, 1]]
    print("最少子弹数:", min_bullets(points2))  # 应该输出2
    
    # 测试LeetCode 410: 分割数组的最大值
    print("\nLeetCode 410: 分割数组的最大值测试:")
    split_nums1 = [7, 2, 5, 10, 8]
    print("最小的最大子数组和(m=2):", split_array(split_nums1, 2))  # 应该输入18
    split_nums2 = [1, 2, 3, 4, 5]
    print("最小的最大子数组和(m=2):", split_array(split_nums2, 2))  # 应该输出9
    
    # 测试LeetCode 4: 寻找两个正序数组的中位数
    print("\nLeetCode 4: 寻找两个正序数组的中位数测试:")
    median_nums1 = [1, 3]
    median_nums2 = [2]
    print("中位数:", find_median_sorted_arrays(median_nums1, median_nums2))  # 应该输出2.0
    median_nums3 = [1, 2]
    median_nums4 = [3, 4]
    print("中位数:", find_median_sorted_arrays(median_nums3, median_nums4))  # 应该输出2.5
    
    # 测试牛客/剑指Offer: 数字在排序数组中出现的次数
    print("\n牛客/剑指Offer: 数字在排序数组中出现的次数测试:")
    count_nums = [1, 2, 3, 3, 3, 3, 4, 5]
    print("3出现的次数:", get_number_of_k(count_nums, 3))  # 应该输出4
    print("6出现的次数:", get_number_of_k(count_nums, 6))  # 应该输出0
    
    # 测试LeetCode 540: 有序数组中的单一元素
    print("\nLeetCode 540: 有序数组中的单一元素测试:")
    single_nums1 = [1, 1, 2, 3, 3, 4, 4, 8, 8]
    print("单一元素:", single_non_duplicate(single_nums1))  # 应该输出2
    single_nums2 = [3, 3, 7, 7, 10, 11, 11]
    print("单一元素:", single_non_duplicate(single_nums2))  # 应该输出10
    
    # 测试LeetCode 1482: 制作 m 束花所需的最少天数
    print("\nLeetCode 1482: 制作 m 束花所需的最少天数测试:")
    bloom_days1 = [1, 10, 3, 10, 2]
    print("最少天数(m=3, k=1):", min_days_to_bloom(bloom_days1, 3, 1))  # 应该输出3
    bloom_days2 = [1, 10, 3, 10, 2]
    print("最少天数(m=3, k=2):", min_days_to_bloom(bloom_days2, 3, 2))  # 应该输出-1
    bloom_days3 = [7, 7, 7, 7, 12, 7, 7]
    print("最少天数(m=2, k=3):", min_days_to_bloom(bloom_days3, 2, 3))  # 应该输出12
    
    # 测试牛客网题目: 旋转数组的最小数字
    print("\n牛客网题目: 旋转数组的最小数字测试:")
    rotate_nums1 = [3, 4, 5, 1, 2]
    print("旋转数组最小值:", min_number_in_rotate_array(rotate_nums1))  # 应该输出1
    rotate_nums2 = [2, 2, 2, 0, 1]
    print("旋转数组最小值:", min_number_in_rotate_array(rotate_nums2))  # 应该输出0
    
    # 测试acwing题目: 数的三次方根
    print("\nacwing题目: 数的三次方根测试:")
    print("8的三次方根:", cube_root(8))  # 应该输出2.0
    print("27的三次方根:", cube_root(27))  # 应该输出3.0
    print("1000的三次方根:", cube_root(1000))  # 应该输出10.0
    
    # 测试Codeforces题目: 寻找边界
    print("\nCodeforces题目: 寻找边界测试:")
    cf_nums = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    print("第一个>=5的位置:", find_left(cf_nums, 5))  # 应该输出4
    print("最后一个<=5的位置:", find_right(cf_nums, 5))  # 应该输出4
    
    # 测试SPOJ题目: 最大最小值问题
    print("\nSPOJ题目: 最大最小值问题测试:")
    spo_nums = [1, 2, 3, 4, 5]
    print("最大最小值:", find_max_min(spo_nums))  # 应该输出(5, 1)
    
    # 测试AtCoder题目: 二分查找应用
    print("\nAtCoder题目: 二分查找应用测试:")
    at_nums = [1, 3, 5, 7, 9]
    print("查找7:", search(at_nums, 7))  # 应该输出3
    print("查找6:", search(at_nums, 6))  # 应该输出-1
    
    # 测试HackerRank题目: 二分查找变种
    print("\nHackerRank题目: 二分查找变种测试:")
    hr_nums = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    print("查找插入位置5:", search_insert(hr_nums, 5))  # 应该输出4
    print("查找插入位置11:", search_insert(hr_nums, 11))  # 应该输出10
    
    # 测试新增题目
    print("\n=== 新增各大平台题目测试 ===")
    
    # 测试杭电OJ题目
    A = [1, 2, 3]
    B = [4, 5, 6]
    C = [7, 8, 9]
    print("杭电OJ-最接近和(15):", find_closest_sum(A, B, C, 15))  # 应该输出True
    
    # 测试POJ题目
    expenses = [100, 200, 300, 400, 500]
    print("POJ-月度开销(3段):", monthly_expense(expenses, 3))  # 应该输出500
    
    # 测试洛谷题目
    trees = [20, 15, 10, 17]
    print("洛谷-砍树(7米):", cut_trees(trees, 7))  # 应该输出15
    
    # 测试USACO题目
    haybales = [1, 2, 4, 8, 9]
    print("USACO-愤怒的奶牛:", angry_cows(haybales))  # 应该输出4
    
    # 测试HackerRank题目
    nums = [1, 2, 3, 4, 5]
    print("HackerRank-最小化最大值(3段):", minimize_maximum(nums, 3))  # 应该输出6
    
    # 测试计蒜客题目
    rocks = [2, 11, 14, 17, 21]
    print("计蒜客-跳石头(移2块):", jump_stones(rocks, 2, 25))  # 应该输出4
    
    # 测试Codeforces题目
    worm_piles = [1, 3, 2, 4]
    worm_queries = [1, 4, 7, 10]
    worm_results = find_worm_piles(worm_piles, worm_queries)
    print("Codeforces-虫子堆:", worm_results)  # 应该输出[1, 2, 3, 4]
    
    # 测试各大平台题目综合测试
    print("\n各大平台题目综合测试:")
    print("所有二分查找算法测试完成!")
    print("=" * 50)
    print("二分查找算法总结:")
    print("1. 时间复杂度: O(log n)")
    print("2. 空间复杂度: O(1)")
    print("3. 适用场景: 有序数组查找")
    print("4. 关键技巧: 边界处理、中点计算、循环条件")
    print("5. 常见变种: 查找边界、旋转数组、二维搜索")
    print("6. 工程化考量: 异常处理、性能优化、边界测试")