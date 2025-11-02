"""
贪心算法数学相关问题集合 - Python版本
包含与数学相关的贪心算法问题

算法专题概览：
本文件包含10个与数学相关的贪心算法问题，展示了贪心算法在数学优化中的应用：
1. 数字组合优化（最大数、十-二进制数）
2. 计算器操作优化（坏了的计算器）
3. 差值优化（最小差值、最大单元数）
4. 字符串构造（快乐字符串）
5. 能量管理（任务能量）
6. 硬币分配（最大硬币数）
7. 字符交换（字符串相同）

贪心算法数学应用核心思想：
数学相关的贪心问题通常需要深入理解问题的数学性质，
通过数学分析找到最优的贪心策略。

工程化最佳实践：
1. 异常处理：检查输入参数的有效性
2. 边界条件：处理空输入、极值等特殊情况
3. 性能优化：选择合适的数学方法和数据结构
4. 可读性：清晰的变量命名和详细注释
"""

import heapq
from functools import cmp_to_key

def largest_number(nums):
    """
    题目1: LeetCode 179. 最大数
    问题描述：给定一组非负整数，重新排列每个数的顺序使之组成一个最大的整数。

    算法思路：自定义排序规则，比较两个数字拼接后的结果
    1. 贪心策略：对于任意两个数字a和b，如果a+b > b+a，则a应该排在b前面
    2. 使用自定义比较函数进行排序
    
    Args:
        nums (List[int]): 非负整数列表
    
    Returns:
        str: 重新排列后组成的最大整数字符串
    
    时间复杂度: O(n log n)，其中n是数字个数，主要是排序的时间复杂度
    空间复杂度: O(n)，存储字符串的空间
    """
    # 异常处理：空数字列表
    if not nums:
        return ""
    
    # 将数字转换为字符串
    # 时间复杂度：O(n)
    str_nums = list(map(str, nums))
    
    # 自定义排序函数
    def compare(a, b):
        if a + b > b + a:
            return -1  # a应该排在b前面
        elif a + b < b + a:
            return 1   # b应该排在a前面
        else:
            return 0
    
    # 排序（降序排列）
    # 时间复杂度：O(n log n)
    str_nums.sort(key=cmp_to_key(compare))
    
    # 处理前导零的情况
    if str_nums[0] == "0":
        return "0"
    
    # 拼接结果
    # 时间复杂度：O(n)
    return ''.join(str_nums)

def broken_calc(start, target):
    """
    题目2: LeetCode 991. 坏了的计算器
    问题描述：有一个坏了的计算器，只能进行两种操作：乘2和减1，求从start到target的最少操作次数。

    算法思路：反向思考，从target到start，只能进行除2和加1操作
    1. 贪心策略：反向操作，当target > start时，优先进行除2操作
    2. 如果target是奇数，先加1使其变为偶数
    
    Args:
        start (int): 起始数字
        target (int): 目标数字
    
    Returns:
        int: 最少操作次数
    
    时间复杂度: O(log target)，主要是除2操作的次数
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：start >= target的情况
    if start >= target:
        return start - target  # 只能减1操作
    
    operations = 0    # 操作次数
    current = target  # 当前数字
    
    # 反向操作
    # 时间复杂度：O(log target)
    while current > start:
        operations += 1
        if current % 2 == 0:
            current //= 2
        else:
            current += 1
    
    return operations + (start - current)

def smallest_range_ii(nums, k):
    """
    题目3: LeetCode 910. 最小差值 II
    问题描述：对于数组中的每个元素，可以选择加上k或减去k，求变换后数组的最大值与最小值的差的最小值。

    算法思路：排序后寻找最佳分割点
    1. 贪心策略：排序后，对于前i个元素加k，后n-i个元素减k
    2. 遍历所有可能的分割点，找到最小差值
    
    Args:
        nums (List[int]): 整数数组
        k (int): 可以加减的数值
    
    Returns:
        int: 变换后数组的最大值与最小值的差的最小值
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：数组长度不足
    if len(nums) <= 1:
        return 0
    
    # 排序数组
    # 时间复杂度：O(n log n)
    nums.sort()
    n = len(nums)
    result = nums[-1] - nums[0]  # 初始差值（都不变换的情况）
    
    # 尝试每个可能的分割点
    # 时间复杂度：O(n)
    for i in range(n - 1):
        # 前i+1个元素加k，后n-i-1个元素减k
        high = max(nums[-1] - k, nums[i] + k)  # 最大值
        low = min(nums[0] + k, nums[i + 1] - k)  # 最小值
        result = min(result, high - low)
    
    return result

def min_number_operations(target):
    """
    题目4: LeetCode 1526. 形成目标数组的子数组最少增加次数
    问题描述：给定目标数组，每次可以选择任意子数组并将其中每个元素增加1，求最少操作次数。

    算法思路：相邻元素差值法
    1. 贪心策略：第i个元素比第i-1个元素多出的部分需要额外的操作次数
    2. 第一个元素需要的操作次数等于其值
    
    Args:
        target (List[int]): 目标数组
    
    Returns:
        int: 最少操作次数
    
    时间复杂度: O(n)，其中n是数组长度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：空目标数组
    if not target:
        return 0
    
    # 第一个元素需要的操作次数
    operations = target[0]
    
    # 计算后续元素需要的额外操作次数
    # 时间复杂度：O(n)
    for i in range(1, len(target)):
        if target[i] > target[i - 1]:
            operations += target[i] - target[i - 1]
    
    return operations

def minimum_swap(s1, s2):
    """
    题目5: LeetCode 1247. 交换字符使得字符串相同
    问题描述：给定两个长度相等的字符串，求使两个字符串相同的最少交换次数。

    算法思路：统计不匹配的位置类型
    1. 贪心策略：统计两种不匹配类型(xy和yx)的数量
    2. 每两个相同类型的不匹配可以通过一次交换解决
    
    Args:
        s1 (str): 第一个字符串
        s2 (str): 第二个字符串
    
    Returns:
        int: 最少交换次数，无法完成返回-1
    
    时间复杂度: O(n)，其中n是字符串长度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：字符串长度不匹配
    if len(s1) != len(s2):
        return -1
    
    xy = 0  # s1[i]='x', s2[i]='y'的不匹配数量
    yx = 0  # s1[i]='y', s2[i]='x'的不匹配数量
    
    # 统计不匹配类型
    # 时间复杂度：O(n)
    for i in range(len(s1)):
        c1, c2 = s1[i], s2[i]
        if c1 == 'x' and c2 == 'y':
            xy += 1
        elif c1 == 'y' and c2 == 'x':
            yx += 1
    
    # 如果总的不匹配数是奇数，无法完成
    if (xy + yx) % 2 != 0:
        return -1
    
    # 计算最少交换次数
    # 每两个相同类型的不匹配可以通过一次交换解决
    return xy // 2 + yx // 2 + (xy % 2) * 2

def max_coins(piles):
    """
    题目6: LeetCode 1561. 你可以获得的最大硬币数目
    问题描述：有3n堆硬币，每次选择3堆，你拿第二多的那堆，求能获得的最大硬币数。

    算法思路：排序后每次取第二大的堆
    1. 贪心策略：排序后，每次选择最大的3堆，你拿第二大的那堆
    2. 从大到小每隔一个取一个
    
    Args:
        piles (List[int]): 硬币堆数量列表
    
    Returns:
        int: 能获得的最大硬币数
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：空硬币堆列表
    if not piles:
        return 0
    
    # 排序硬币堆
    # 时间复杂度：O(n log n)
    piles.sort()
    result = 0  # 获得的硬币数
    n = len(piles)
    
    # 每次取第二大的堆（从倒数第二个开始，每隔一个取一个）
    # 时间复杂度：O(n)
    for i in range(n - 2, n // 3 - 1, -2):
        result += piles[i]
    
    return result

def min_partitions(n):
    """
    题目7: LeetCode 1689. 十-二进制数的最少数目
    问题描述：十-二进制数是只包含0和1的十进制数，求表示数字n需要的最少十-二进制数数目。

    算法思路：最少数量等于数字中最大的数字
    1. 贪心策略：每个位置需要的十-二进制数数目等于该位置的数字
    2. 总数等于所有位置的最大值
    
    Args:
        n (str): 表示正整数的字符串
    
    Returns:
        int: 最少十-二进制数数目
    
    时间复杂度: O(n)，其中n是字符串长度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：空字符串
    if not n:
        return 0
    
    max_digit = 0  # 最大数字
    
    # 找到数字中最大的数字
    # 时间复杂度：O(n)
    for char in n:
        max_digit = max(max_digit, int(char))
    
    return max_digit

def maximum_units(box_types, truck_size):
    """
    题目8: LeetCode 1710. 卡车上的最大单元数
    问题描述：给定箱子类型和卡车容量，求卡车能装载的最大单元数。

    算法思路：按单位容量价值降序排序
    1. 贪心策略：优先装单位容量价值高的箱子
    2. 按每个箱子的单元数降序排序后依次装载
    
    Args:
        box_types (List[List[int]]): 箱子类型列表，每个元素为[箱子数量, 每箱单元数]
        truck_size (int): 卡车容量（箱子数量）
    
    Returns:
        int: 卡车能装载的最大单元数
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：空箱子类型列表或卡车容量为0
    if not box_types or truck_size <= 0:
        return 0
    
    # 按每个箱子的单元数降序排序
    # 时间复杂度：O(n log n)
    box_types.sort(key=lambda x: x[1], reverse=True)
    
    total_units = 0          # 总单元数
    remaining_size = truck_size  # 剩余容量
    
    # 贪心装载箱子
    # 时间复杂度：O(n)
    for box in box_types:
        number_of_boxes, units_per_box = box
        boxes_to_take = min(number_of_boxes, remaining_size)
        total_units += boxes_to_take * units_per_box
        remaining_size -= boxes_to_take
        
        if remaining_size == 0:
            break
    
    return total_units

def longest_diverse_string(a, b, c):
    """
    题目9: LeetCode 1405. 最长快乐字符串
    问题描述：构造最长的字符串，使得不包含连续三个相同字符。

    算法思路：优先使用剩余数量最多的字符
    1. 贪心策略：优先使用剩余数量最多的字符
    2. 使用最大堆维护字符及其剩余数量
    3. 避免连续使用相同字符超过两次
    
    Args:
        a (int): 字符'a'的数量
        b (int): 字符'b'的数量
        c (int): 字符'c'的数量
    
    Returns:
        str: 最长的快乐字符串
    
    时间复杂度: O(n)，其中n是字符总数
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 使用最大堆（用最小堆存储负值来模拟最大堆）
    heap = []
    if a > 0:
        heapq.heappush(heap, (-a, 'a'))
    if b > 0:
        heapq.heappush(heap, (-b, 'b'))
    if c > 0:
        heapq.heappush(heap, (-c, 'c'))
    
    result = []  # 结果字符串
    
    # 构造快乐字符串
    # 时间复杂度：O(n)
    while heap:
        count1, char1 = heapq.heappop(heap)
        count1 = -count1  # 转换为正数
        
        # 检查是否已经连续使用了两个相同字符
        if len(result) >= 2 and result[-1] == char1 and result[-2] == char1:
            # 需要换一个字符
            if not heap:
                break  # 没有其他字符可用
            
            count2, char2 = heapq.heappop(heap)
            count2 = -count2
            
            result.append(char2)
            count2 -= 1
            
            if count2 > 0:
                heapq.heappush(heap, (-count2, char2))
            heapq.heappush(heap, (-count1, char1))  # 把第一个字符放回去
        else:
            # 可以使用当前字符
            result.append(char1)
            count1 -= 1
            
            if count1 > 0:
                heapq.heappush(heap, (-count1, char1))
    
    return ''.join(result)

def minimum_effort(tasks):
    """
    题目10: LeetCode 1665. 完成所有任务的最少初始能量
    问题描述：给定任务列表，每个任务有最小要求能量和实际消耗能量，求完成所有任务的最少初始能量。

    算法思路：按(最小要求 - 实际消耗)降序排序
    1. 贪心策略：按(最小要求 - 实际消耗)降序排序
    2. 优先处理差值大的任务，这样能更好地利用剩余能量
    
    Args:
        tasks (List[List[int]]): 任务列表，每个元素为[实际消耗, 最小要求]
    
    Returns:
        int: 完成所有任务的最少初始能量
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：空任务列表
    if not tasks:
        return 0
    
    # 按(最小要求 - 实际消耗)降序排序
    # 时间复杂度：O(n log n)
    tasks.sort(key=lambda x: x[1] - x[0], reverse=True)
    
    result = 0            # 需要的初始能量
    current_energy = 0    # 当前能量
    
    # 处理每个任务
    # 时间复杂度：O(n)
    for task in tasks:
        actual, minimum = task
        
        if current_energy < minimum:
            # 需要补充能量
            need = minimum - current_energy
            result += need
            current_energy += need
        
        # 完成任务，消耗能量
        current_energy -= actual
    
    return result

# 测试函数
if __name__ == "__main__":
    # 测试最大数
    nums1 = [3, 30, 34, 5, 9]
    print("最大数测试:", largest_number(nums1))  # 期望: "9534330"
    
    # 测试坏了的计算器
    print("坏了的计算器测试:", broken_calc(2, 3))  # 期望: 2
    
    # 测试最小差值II
    nums2 = [1, 3, 6]
    print("最小差值II测试:", smallest_range_ii(nums2, 3))  # 期望: 3
    
    # 测试最大硬币数
    piles = [2, 4, 1, 2, 7, 8]
    print("最大硬币数测试:", max_coins(piles))  # 期望: 9
    
    # 测试最长快乐字符串
    print("最长快乐字符串测试:", longest_diverse_string(1, 1, 7))  # 期望类似: "ccaccbcc"