"""
456. 132 模式 (132 Pattern)

题目描述:
给你一个整数数组 nums ，数组中共有 n 个整数。
132 模式的子序列 由三个整数 nums[i]、nums[j] 和 nums[k] 组成，
并同时满足：i < j < k 和 nums[i] < nums[k] < nums[j]。
如果 nums 中存在 132 模式的子序列，返回 true；否则，返回 false。

解题思路:
使用单调栈来解决。从右往左遍历数组，维护一个单调递减栈。
同时记录一个变量 second，表示可能的最大中间值（即3后面的最大2）。
当遇到比 second 小的元素时，说明找到了132模式。

时间复杂度: O(n)，每个元素最多入栈和出栈各一次
空间复杂度: O(n)，用于存储单调栈

测试链接: https://leetcode.cn/problems/132-pattern/

工程化考量:
1. 异常处理：空数组、单元素数组边界情况
2. 性能优化：使用列表模拟栈，避免不必要的操作
3. 代码可读性：详细注释和有意义变量名
4. Python特性：利用列表的高效操作
"""

import time
from typing import List

def find132pattern(nums: List[int]) -> bool:
    """
    判断数组中是否存在132模式的子序列
    
    Args:
        nums: 输入整数数组
        
    Returns:
        bool: 如果存在132模式返回True，否则返回False
        
    Raises:
        TypeError: 如果输入不是列表
    """
    # 边界条件检查
    if not isinstance(nums, list):
        raise TypeError("输入必须是列表")
        
    if len(nums) < 3:
        return False  # 至少需要3个元素才能形成132模式
    
    n = len(nums)
    stack = []  # 使用列表作为栈
    second = float('-inf')  # 记录可能的最大中间值（3后面的最大2）
    
    # 从右往左遍历数组
    for i in range(n - 1, -1, -1):
        # 如果当前元素小于second，说明找到了132模式
        if nums[i] < second:
            return True
        
        # 维护单调递减栈，找到更大的元素作为3，并更新second
        while stack and nums[i] > nums[stack[-1]]:
            # 更新second为栈顶元素（即当前找到的最大2）
            second = nums[stack.pop()]
        
        # 将当前索引入栈
        stack.append(i)
    
    return False  # 没有找到132模式


def test_132_pattern():
    """测试方法 - 验证算法正确性"""
    print("=== 132模式算法测试 ===")
    
    # 测试用例1: [1, 2, 3, 4] - 预期: False
    nums1 = [1, 2, 3, 4]
    result1 = find132pattern(nums1)
    print(f"测试用例1 [1, 2, 3, 4]: {result1} (预期: False)")
    
    # 测试用例2: [3, 1, 4, 2] - 预期: True
    nums2 = [3, 1, 4, 2]
    result2 = find132pattern(nums2)
    print(f"测试用例2 [3, 1, 4, 2]: {result2} (预期: True)")
    
    # 测试用例3: [-1, 3, 2, 0] - 预期: True
    nums3 = [-1, 3, 2, 0]
    result3 = find132pattern(nums3)
    print(f"测试用例3 [-1, 3, 2, 0]: {result3} (预期: True)")
    
    # 测试用例4: [1, 0, 1, -4, -3] - 预期: False
    nums4 = [1, 0, 1, -4, -3]
    result4 = find132pattern(nums4)
    print(f"测试用例4 [1, 0, 1, -4, -3]: {result4} (预期: False)")
    
    # 测试用例5: 边界情况 - 空数组
    nums5 = []
    result5 = find132pattern(nums5)
    print(f"测试用例5 []: {result5} (预期: False)")
    
    # 测试用例6: 边界情况 - 两个元素
    nums6 = [1, 2]
    result6 = find132pattern(nums6)
    print(f"测试用例6 [1, 2]: {result6} (预期: False)")
    
    # 测试用例7: 重复元素 [1, 3, 2, 4, 5, 6, 7, 8, 9, 10] - 预期: True
    nums7 = [1, 3, 2, 4, 5, 6, 7, 8, 9, 10]
    result7 = find132pattern(nums7)
    print(f"测试用例7 [1, 3, 2, 4, 5, 6, 7, 8, 9, 10]: {result7} (预期: True)")
    
    # 测试用例8: 类型错误检查
    try:
        find132pattern("not a list")
        print("测试用例8 类型错误: 未捕获异常")
    except TypeError as e:
        print(f"测试用例8 类型错误: 正确捕获异常 - {e}")
    
    print("=== 所有测试用例执行完成！ ===")


def performance_test():
    """性能测试方法"""
    print("=== 性能测试 ===")
    
    # 性能测试：大规模数据 - 严格递增
    size = 10000
    nums = list(range(size))  # 严格递增，预期False
    
    start_time = time.time()
    result = find132pattern(nums)
    end_time = time.time()
    
    print(f"性能测试 [{size}个元素]: {result} (预期: False), 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 性能测试：最坏情况 - 严格递减
    nums_worst = list(range(size, 0, -1))  # 严格递减，预期False
    
    start_time = time.time()
    result_worst = find132pattern(nums_worst)
    end_time = time.time()
    
    print(f"性能测试 [最坏情况{size}个元素]: {result_worst} (预期: False), 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    print("=== 性能测试完成！ ===")


def debug_print(nums: List[int], i: int, stack: List[int], second: float):
    """
    调试辅助方法：打印中间过程
    
    Args:
        nums: 输入数组
        i: 当前索引
        stack: 栈列表
        second: 当前second值
    """
    stack_values = [nums[idx] for idx in stack]
    print(f"i={i}, nums[i]={nums[i]}, second={second}")
    print(f"栈内容: {stack_values}")
    print("---")


if __name__ == "__main__":
    # 运行功能测试
    test_132_pattern()
    
    # 运行性能测试
    performance_test()


"""
算法复杂度分析:

时间复杂度: O(n)
- 每个元素最多入栈一次和出栈一次
- 虽然有两层循环，但内层循环的总操作次数不超过n次

空间复杂度: O(n)
- 使用了一个大小为n的列表作为栈
- 没有使用递归，栈空间为O(1)

最优解分析:
- 这是132模式问题的最优解
- 无法在O(n)时间内获得更好的时间复杂度
- 空间复杂度也是最优的，因为需要存储中间结果

Python特性利用:
- 使用列表的pop()和append()操作，时间复杂度为O(1)
- 利用列表切片和推导式提高代码可读性
- 使用类型注解提高代码可维护性
- 异常处理确保代码健壮性

工程化建议:
1. 对于大规模数据，可以考虑使用生成器表达式减少内存使用
2. 可以添加更多的单元测试用例覆盖边界情况
3. 可以考虑使用装饰器进行性能监控
4. 对于生产环境，可以添加日志记录和监控
"""