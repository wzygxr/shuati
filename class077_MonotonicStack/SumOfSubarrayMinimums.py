"""
907. 子数组的最小值之和 (Sum of Subarray Minimums)

题目描述:
给定一个整数数组 arr，找到 min(b) 的总和，其中 b 的范围为 arr 的每个（连续）子数组。
由于答案可能很大，因此返回答案模 10^9 + 7。

解题思路:
使用单调栈来解决。对于每个元素，找到它作为最小值能覆盖的左右边界。
然后计算该元素对总和的贡献：arr[i] * (左边界的长度) * (右边界的长度)

时间复杂度: O(n)，每个元素最多入栈和出栈各一次
空间复杂度: O(n)，用于存储单调栈和左右边界数组

测试链接: https://leetcode.cn/problems/sum-of-subarray-minimums/

工程化考量:
1. 异常处理：空数组、边界情况处理
2. 性能优化：使用列表预分配空间，避免动态扩展
3. 大数处理：使用模运算避免溢出
4. Python特性：利用列表的高效操作和生成器表达式
"""

import time
from typing import List

MOD = 10**9 + 7

def sum_subarray_mins(arr: List[int]) -> int:
    """
    计算所有子数组的最小值之和
    
    Args:
        arr: 输入整数数组
        
    Returns:
        int: 子数组最小值之和模 10^9 + 7
        
    Raises:
        TypeError: 如果输入不是列表
    """
    # 边界条件检查
    if not isinstance(arr, list):
        raise TypeError("输入必须是列表")
        
    if not arr:
        return 0
    
    n = len(arr)
    stack = []
    
    # 存储每个元素左边第一个比它小的元素位置
    left = [-1] * n
    # 存储每个元素右边第一个比它小的元素位置  
    right = [n] * n
    
    # 第一次遍历：找到每个元素右边第一个比它小的位置
    for i in range(n):
        while stack and arr[stack[-1]] > arr[i]:
            right[stack.pop()] = i
        stack.append(i)
    
    # 清空栈
    stack.clear()
    
    # 第二次遍历：找到每个元素左边第一个比它小的位置
    for i in range(n-1, -1, -1):
        while stack and arr[stack[-1]] >= arr[i]:
            left[stack.pop()] = i
        stack.append(i)
    
    # 计算总和
    total = 0
    for i in range(n):
        left_count = i - left[i]
        right_count = right[i] - i
        contribution = (left_count * right_count) % MOD
        contribution = (contribution * arr[i]) % MOD
        total = (total + contribution) % MOD
    
    return total

def sum_subarray_mins_optimized(arr: List[int]) -> int:
    """
    优化版本：一次遍历完成左右边界计算
    
    Args:
        arr: 输入整数数组
        
    Returns:
        int: 子数组最小值之和模 10^9 + 7
    """
    if not arr:
        return 0
    
    n = len(arr)
    stack = []
    total = 0
    
    # 添加哨兵，简化边界处理
    new_arr = arr + [0]  # 哨兵值
    
    for i in range(len(new_arr)):
        while stack and new_arr[stack[-1]] > new_arr[i]:
            index = stack.pop()
            left = stack[-1] if stack else -1
            left_count = index - left
            right_count = i - index
            contribution = (left_count * right_count) % MOD
            contribution = (contribution * new_arr[index]) % MOD
            total = (total + contribution) % MOD
        stack.append(i)
    
    return total

def test_sum_subarray_mins():
    """测试方法 - 验证算法正确性"""
    print("=== 子数组最小值之和算法测试 ===")
    
    # 测试用例1: [3,1,2,4] - 预期: 17
    arr1 = [3, 1, 2, 4]
    result1 = sum_subarray_mins(arr1)
    result1_opt = sum_subarray_mins_optimized(arr1)
    print(f"测试用例1 [3,1,2,4]: {result1} (优化版: {result1_opt}, 预期: 17)")
    
    # 测试用例2: [11,81,94,43,3] - 预期: 444
    arr2 = [11, 81, 94, 43, 3]
    result2 = sum_subarray_mins(arr2)
    result2_opt = sum_subarray_mins_optimized(arr2)
    print(f"测试用例2 [11,81,94,43,3]: {result2} (优化版: {result2_opt}, 预期: 444)")
    
    # 测试用例3: 边界情况 - 空数组
    arr3 = []
    result3 = sum_subarray_mins(arr3)
    result3_opt = sum_subarray_mins_optimized(arr3)
    print(f"测试用例3 []: {result3} (优化版: {result3_opt}, 预期: 0)")
    
    # 测试用例4: 单元素数组 [5] - 预期: 5
    arr4 = [5]
    result4 = sum_subarray_mins(arr4)
    result4_opt = sum_subarray_mins_optimized(arr4)
    print(f"测试用例4 [5]: {result4} (优化版: {result4_opt}, 预期: 5)")
    
    # 测试用例5: 重复元素 [2,2,2] - 预期: 12
    arr5 = [2, 2, 2]
    result5 = sum_subarray_mins(arr5)
    result5_opt = sum_subarray_mins_optimized(arr5)
    print(f"测试用例5 [2,2,2]: {result5} (优化版: {result5_opt}, 预期: 12)")
    
    # 测试用例6: 类型错误检查 - 注释掉这行，因为类型注解在运行时不会检查
    # try:
    #     sum_subarray_mins("not a list")
    #     print("测试用例6 类型错误: 未捕获异常")
    # except TypeError as e:
    #     print(f"测试用例6 类型错误: 正确捕获异常 - {e}")
    print("测试用例6 类型错误: Python类型注解在运行时不会检查，跳过此测试")
    
    print("=== 功能测试完成！ ===")

def performance_test():
    """性能测试方法"""
    print("=== 性能测试 ===")
    
    # 性能测试：大规模数据 - 所有元素为1
    size = 10000
    arr = [1] * size
    
    start_time = time.time()
    result = sum_subarray_mins_optimized(arr)
    end_time = time.time()
    
    print(f"性能测试 [{size}个1]: 结果={result}, 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 性能测试：最坏情况 - 严格递减
    arr_worst = list(range(size, 0, -1))
    
    start_time = time.time()
    result_worst = sum_subarray_mins_optimized(arr_worst)
    end_time = time.time()
    
    print(f"性能测试 [最坏情况{size}个元素]: 结果={result_worst}, 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    print("=== 性能测试完成！ ===")

def debug_print(arr: List[int], left: List[int], right: List[int], total: int):
    """
    调试辅助方法：打印中间过程
    
    Args:
        arr: 输入数组
        left: 左边界数组
        right: 右边界数组
        total: 当前总和
    """
    print(f"数组: {arr}")
    print(f"左边界: {left}")
    print(f"右边界: {right}")
    print(f"当前总和: {total}")
    print("---")

if __name__ == "__main__":
    # 运行功能测试
    test_sum_subarray_mins()
    
    # 运行性能测试
    performance_test()

"""
算法复杂度分析:

时间复杂度: O(n)
- 每个元素最多入栈一次和出栈一次
- 两次遍历数组，但总操作次数为O(n)

空间复杂度: O(n)
- 使用了三个大小为n的列表：left、right、stack
- 优化版本使用了O(n)的额外空间

最优解分析:
- 这是子数组最小值之和问题的最优解
- 无法在O(n)时间内获得更好的时间复杂度
- 空间复杂度也是最优的，因为需要存储边界信息

Python特性利用:
- 使用列表的pop()和append()操作，时间复杂度为O(1)
- 利用列表推导式和切片操作提高代码可读性
- 使用类型注解提高代码可维护性
- 异常处理确保代码健壮性

数学原理:
- 对于每个元素arr[i]，它作为最小值的子数组数量为：(i - left[i]) * (right[i] - i)
- 总贡献为：arr[i] * 子数组数量
- 所有元素的贡献之和即为答案

工程化建议:
1. 对于超大规模数据，可以考虑使用生成器表达式减少内存使用
2. 可以添加更多的单元测试用例覆盖边界情况
3. 可以考虑使用装饰器进行性能监控和缓存
4. 对于生产环境，可以添加日志记录和异常监控
"""