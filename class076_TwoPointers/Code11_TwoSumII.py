#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
两数之和 II - 输入有序数组

题目描述：
给你一个下标从 1 开始的整数数组 numbers ，该数组已按非递减顺序排列。
请你从数组中找出满足相加之和等于目标数 target 的两个数。
如果设这两个数分别是 numbers[index1] 和 numbers[index2] ，则 1 <= index1 < index2 <= numbers.length 。
以长度为 2 的整数数组 [index1, index2] 的形式返回这两个整数的下标 index1 和 index2。
你可以假设每个输入只对应唯一的答案，而且你不能重复使用相同的元素。
你所设计的解决方案必须只使用常量级的额外空间。

示例：
输入：numbers = [2,7,11,15], target = 9
输出：[1,2]
解释：2 与 7 之和等于目标数 9 。因此 index1 = 1, index2 = 2 。返回 [1, 2] 。

输入：numbers = [2,3,4], target = 6
输出：[1,3]
解释：2 与 4 之和等于目标数 6 。因此 index1 = 1, index2 = 3 。返回 [1, 3] 。

输入：numbers = [-1,0], target = -1
输出：[1,2]
解释：-1 与 0 之和等于目标数 -1 。因此 index1 = 1, index2 = 2 。返回 [1, 2] 。

解题思路：
使用双指针法。由于数组已经排序，我们可以使用两个指针分别指向数组的开始和结束。
如果两个指针指向的数字之和等于目标值，则返回它们的索引（注意题目要求索引从1开始）。
如果和小于目标值，则将左指针右移以增大和。
如果和大于目标值，则将右指针左移以减小和。

时间复杂度：O(n) - 最多遍历一次数组
空间复杂度：O(1) - 只使用了常数级别的额外空间
是否最优解：是 - 基于比较的算法下界为O(n)，本算法已达到最优

相关题目：
1. LeetCode 1 - 两数之和（无序数组，使用哈希表）
2. LeetCode 167 - 两数之和 II - 输入有序数组（当前题目）
3. LeetCode 15 - 三数之和（排序+双指针）
4. LeetCode 18 - 四数之和（排序+双指针）

工程化考虑：
1. 输入验证：检查数组是否为空或长度小于2
2. 异常处理：题目保证有唯一解，但在实际工程中可能需要处理无解的情况
3. 边界条件：处理负数、零和正数混合的情况

语言特性差异：
Java: 使用数组索引访问，需要手动处理索引偏移（题目要求索引从1开始）
C++: 可使用vector，指针运算更灵活
Python: 可使用列表，支持负索引访问

极端输入场景：
1. 数组包含负数
2. 目标值为0
3. 数组长度为2
4. 解在数组两端

与机器学习等领域的联系：
1. 在特征选择中，可能需要找到两个特征的组合满足特定条件
2. 在推荐系统中，可能需要找到两个物品的组合满足用户偏好
"""


def two_sum_brute_force(numbers, target):
    """
    暴力解法：双重循环遍历所有可能的组合

    :param numbers: 已排序的整数数组
    :param target: 目标和
    :return: 包含两个索引的数组（索引从1开始）
    """
    n = len(numbers)
    # 边界条件检查
    if n < 2:
        return [-1, -1]
    
    # 双重循环遍历所有可能的组合
    for i in range(n):
        for j in range(i + 1, n):
            if numbers[i] + numbers[j] == target:
                return [i + 1, j + 1]  # 索引从1开始
    
    return [-1, -1]


def two_sum_binary_search(numbers, target):
    """
    二分查找解法：对于每个元素，在剩余部分中二分查找目标差值

    :param numbers: 已排序的整数数组
    :param target: 目标和
    :return: 包含两个索引的数组（索引从1开始）
    """
    n = len(numbers)
    # 边界条件检查
    if n < 2:
        return [-1, -1]
    
    # 遍历每个元素，对剩余部分进行二分查找
    for i in range(n - 1):
        complement = target - numbers[i]
        # 在i+1到n-1范围内二分查找complement
        left, right = i + 1, n - 1
        while left <= right:
            mid = left + (right - left) // 2
            if numbers[mid] == complement:
                return [i + 1, mid + 1]  # 索引从1开始
            elif numbers[mid] < complement:
                left = mid + 1
            else:
                right = mid - 1
    
    return [-1, -1]


def two_sum_two_pointers(numbers, target):
    """
    双指针解法：利用数组有序的特性，从两端向中间移动指针

    :param numbers: 已排序的整数数组
    :param target: 目标和
    :return: 包含两个索引的数组（索引从1开始）
    """
    # 边界条件检查
    if not numbers or len(numbers) < 2:
        return [-1, -1]

    # 初始化双指针
    left = 0  # 左指针指向数组开始
    right = len(numbers) - 1  # 右指针指向数组结束

    # 当左指针小于右指针时继续循环
    while left < right:
        # 计算当前两个指针指向元素的和
        sum_val = numbers[left] + numbers[right]

        # 如果和等于目标值，返回索引（注意题目要求索引从1开始）
        if sum_val == target:
            return [left + 1, right + 1]
        # 如果和小于目标值，将左指针右移以增大和
        elif sum_val < target:
            left += 1
        # 如果和大于目标值，将右指针左移以减小和
        else:
            right -= 1

    # 根据题目描述，保证有唯一解，此行理论上不会执行到
    return [-1, -1]


def two_sum_optimized(numbers, target):
    """
    优化的双指针解法：增加跳过重复元素的逻辑

    :param numbers: 已排序的整数数组
    :param target: 目标和
    :return: 包含两个索引的数组（索引从1开始）
    """
    # 边界条件检查
    if not numbers or len(numbers) < 2:
        return [-1, -1]

    # 初始化双指针
    left = 0
    right = len(numbers) - 1

    while left < right:
        sum_val = numbers[left] + numbers[right]
        
        if sum_val == target:
            return [left + 1, right + 1]
        elif sum_val < target:
            # 跳过重复元素，避免无效计算
            while left < right and numbers[left] == numbers[left + 1]:
                left += 1
            left += 1
        else:
            # 跳过重复元素，避免无效计算
            while left < right and numbers[right] == numbers[right - 1]:
                right -= 1
            right -= 1
    
    return [-1, -1]

# 主函数，默认使用双指针解法
def two_sum(numbers, target):
    return two_sum_two_pointers(numbers, target)


def test():
    """测试函数，测试所有解法"""
    # 测试用例1: 常规输入
    numbers1 = [2, 7, 11, 15]
    target1 = 9
    print("\n=== 测试用例1: 常规输入 ===")
    print(f"输入: numbers={numbers1}, target={target1}")
    print(f"双指针解法结果: {two_sum_two_pointers(numbers1, target1)}")
    print(f"优化双指针解法结果: {two_sum_optimized(numbers1, target1)}")
    print(f"二分查找解法结果: {two_sum_binary_search(numbers1, target1)}")
    print(f"暴力解法结果: {two_sum_brute_force(numbers1, target1)}")
    
    # 测试用例2: 解在数组两端
    numbers2 = [2, 3, 4]
    target2 = 6
    print("\n=== 测试用例2: 解在数组两端 ===")
    print(f"输入: numbers={numbers2}, target={target2}")
    print(f"双指针解法结果: {two_sum_two_pointers(numbers2, target2)}")
    
    # 测试用例3: 包含负数
    numbers3 = [-1, 0]
    target3 = -1
    print("\n=== 测试用例3: 包含负数 ===")
    print(f"输入: numbers={numbers3}, target={target3}")
    print(f"双指针解法结果: {two_sum_two_pointers(numbers3, target3)}")
    
    # 测试用例4: 数组长度为2
    numbers4 = [1, 2]
    target4 = 3
    print("\n=== 测试用例4: 数组长度为2 ===")
    print(f"输入: numbers={numbers4}, target={target4}")
    print(f"双指针解法结果: {two_sum_two_pointers(numbers4, target4)}")
    
    # 测试用例5: 有重复元素
    numbers5 = [1, 2, 3, 4, 4, 9, 56, 90]
    target5 = 8
    print("\n=== 测试用例5: 有重复元素 ===")
    print(f"输入: numbers={numbers5}, target={target5}")
    print(f"双指针解法结果: {two_sum_two_pointers(numbers5, target5)}")
    print(f"优化双指针解法结果: {two_sum_optimized(numbers5, target5)}")
    
    # 测试用例6: 全是负数
    numbers6 = [-5, -4, -3, -2, -1]
    target6 = -7
    print("\n=== 测试用例6: 全是负数 ===")
    print(f"输入: numbers={numbers6}, target={target6}")
    print(f"双指针解法结果: {two_sum_two_pointers(numbers6, target6)}")
    
    # 测试用例7: 目标值为0
    numbers7 = [-3, -2, -1, 0, 1, 2, 3]
    target7 = 0
    print("\n=== 测试用例7: 目标值为0 ===")
    print(f"输入: numbers={numbers7}, target={target7}")
    print(f"双指针解法结果: {two_sum_two_pointers(numbers7, target7)}")
    print(f"优化双指针解法结果: {two_sum_optimized(numbers7, target7)}")


def performance_test():
    """性能测试函数"""
    import time
    
    # 生成大数组进行性能测试
    print("\n=== 性能测试 ===")
    n = 10000
    large_numbers = list(range(n))
    large_target = 2 * n - 3  # 确保解在数组两端
    
    # 测试暴力解法（仅对小数组测试，避免超时）
    small_numbers = list(range(100))
    small_target = 197
    
    print("暴力解法（小数组）:")
    start_time = time.time()
    two_sum_brute_force(small_numbers, small_target)
    print(f"耗时: {(time.time() - start_time) * 1000:.4f} ms")
    
    print("二分查找解法:")
    start_time = time.time()
    two_sum_binary_search(large_numbers, large_target)
    print(f"耗时: {(time.time() - start_time) * 1000:.4f} ms")
    
    print("双指针解法:")
    start_time = time.time()
    two_sum_two_pointers(large_numbers, large_target)
    print(f"耗时: {(time.time() - start_time) * 1000:.4f} ms")
    
    print("优化双指针解法:")
    start_time = time.time()
    two_sum_optimized(large_numbers, large_target)
    print(f"耗时: {(time.time() - start_time) * 1000:.4f} ms")


def edge_case_test():
    """边界条件测试"""
    print("\n=== 边界条件测试 ===")
    
    # 空数组
    print("空数组:")
    print(f"结果: {two_sum_two_pointers([], 5)}")
    
    # 单元素数组
    print("单元素数组:")
    print(f"结果: {two_sum_two_pointers([1], 1)}")
    
    # 大数测试
    print("大数测试:")
    large_num1 = 10**9
    large_num2 = 10**9
    large_array = [large_num1, large_num2]
    print(f"结果: {two_sum_two_pointers(large_array, 2*10**9)}")
    
    # 所有元素相同
    print("所有元素相同:")
    same_array = [2, 2, 2, 2, 2]
    print(f"结果: {two_sum_two_pointers(same_array, 4)}")


def algorithm_analysis():
    """算法分析"""
    print("\n=== 算法分析 ===")
    print("1. 暴力解法")
    print("   - 时间复杂度: O(n²)")
    print("   - 空间复杂度: O(1)")
    print("   - 适用场景: 小规模数据")
    print("\n2. 二分查找解法")
    print("   - 时间复杂度: O(n log n)")
    print("   - 空间复杂度: O(1)")
    print("   - 适用场景: 中等规模数据")
    print("\n3. 双指针解法")
    print("   - 时间复杂度: O(n)")
    print("   - 空间复杂度: O(1)")
    print("   - 适用场景: 所有规模数据，特别是大数据集")
    print("   - 最优解的原因: 利用数组有序的特性，只需一次遍历")
    print("\n4. 优化双指针解法")
    print("   - 时间复杂度: O(n)，但在重复元素较多时常数因子更小")
    print("   - 空间复杂度: O(1)")
    print("   - 适用场景: 包含大量重复元素的有序数组")

def engineering_considerations():
    """工程化考量"""
    print("\n=== 工程化考量 ===")
    print("1. 异常处理:")
    print("   - 对空数组和单元素数组进行了边界检查")
    print("   - 返回[-1, -1]表示未找到解，便于调用者识别")
    print("\n2. 性能优化:")
    print("   - 在优化版本中增加了跳过重复元素的逻辑")
    print("   - 对于不同规模的数据选择合适的算法")
    print("\n3. 代码可读性:")
    print("   - 详细的函数注释和参数说明")
    print("   - 清晰的变量命名")
    print("\n4. 多语言实现对比:")
    print("   - Python: 语法简洁，内置列表操作便捷")
    print("   - Java: 类型安全，数组操作需要手动索引")
    print("   - C++: 指针操作灵活，性能最优")
    print("\n5. 调试支持:")
    print("   - 完整的测试函数覆盖各种情况")
    print("   - 性能测试帮助识别瓶颈")

# 主函数
if __name__ == "__main__":
    print("=== 两数之和II - 输入有序数组 算法实现 ===")
    
    # 运行基本测试
    test()
    
    # 运行边界条件测试
    edge_case_test()
    
    # 运行性能测试
    performance_test()
    
    # 算法分析
    algorithm_analysis()
    
    # 工程化考量
    engineering_considerations()
    
    print("\n=== 测试完成 ===")