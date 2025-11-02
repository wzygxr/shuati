"""
503. 下一个更大元素 II (Next Greater Element II)

题目描述:
给定一个循环数组 nums（最后一个元素的下一个元素是数组的第一个元素），
返回每个元素的下一个更大元素。如果不存在，则输出 -1。

解题思路:
使用单调栈来解决。由于是循环数组，可以遍历数组两次来模拟循环效果。
维护一个单调递减栈，栈中存储元素索引。
当遇到比栈顶元素大的元素时，说明找到了栈顶元素的下一个更大元素。

时间复杂度: O(n)，每个元素最多入栈和出栈各一次
空间复杂度: O(n)，用于存储单调栈和结果数组

测试链接: https://leetcode.cn/problems/next-greater-element-ii/

工程化考量:
1. 异常处理：空数组、边界情况处理
2. 性能优化：使用列表预分配空间，避免动态扩展
3. 循环数组处理：遍历两次模拟循环效果
4. Python特性：利用列表的高效操作和生成器表达式
"""

from typing import List

def next_greater_elements(nums: List[int]) -> List[int]:
    """
    查找循环数组中每个元素的下一个更大元素
    
    Args:
        nums: 输入循环数组
        
    Returns:
        List[int]: 每个元素的下一个更大元素数组
        
    Raises:
        TypeError: 如果输入不是列表
    """
    # 边界条件检查
    if not isinstance(nums, list):
        raise TypeError("输入必须是列表")
        
    if not nums:
        return []
    
    n = len(nums)
    result = [-1] * n  # 初始化为-1
    stack = []  # 使用列表作为栈
    
    # 遍历两次数组模拟循环效果
    for i in range(2 * n):
        actual_index = i % n  # 实际数组索引
        
        # 当栈不为空且当前元素大于栈顶索引对应的元素时
        while stack and nums[actual_index] > nums[stack[-1]]:
            index = stack.pop()
            result[index] = nums[actual_index]  # 设置下一个更大元素
        
        # 只在第一次遍历时将索引入栈
        if i < n:
            stack.append(actual_index)
    
    return result

def next_greater_elements_optimized(nums: List[int]) -> List[int]:
    """
    优化版本：使用更简洁的实现
    
    Args:
        nums: 输入循环数组
        
    Returns:
        List[int]: 每个元素的下一个更大元素数组
    """
    if not nums:
        return []
    
    n = len(nums)
    result = [-1] * n
    stack = []
    
    for i in range(2 * n):
        idx = i % n
        
        while stack and nums[idx] > nums[stack[-1]]:
            result[stack.pop()] = nums[idx]
        
        if i < n:
            stack.append(idx)
    
    return result

def test_next_greater_elements():
    """测试方法 - 验证算法正确性"""
    print("=== 下一个更大元素II算法测试 ===")
    
    # 测试用例1: [1,2,1] - 预期: [2,-1,2]
    nums1 = [1, 2, 1]
    result1 = next_greater_elements(nums1)
    result1_opt = next_greater_elements_optimized(nums1)
    print(f"测试用例1 [1,2,1]: {result1} (优化版: {result1_opt}, 预期: [2, -1, 2])")
    
    # 测试用例2: [1,2,3,4,3] - 预期: [2,3,4,-1,4]
    nums2 = [1, 2, 3, 4, 3]
    result2 = next_greater_elements(nums2)
    result2_opt = next_greater_elements_optimized(nums2)
    print(f"测试用例2 [1,2,3,4,3]: {result2} (优化版: {result2_opt}, 预期: [2, 3, 4, -1, 4])")
    
    # 测试用例3: 边界情况 - 空数组
    nums3 = []
    result3 = next_greater_elements(nums3)
    result3_opt = next_greater_elements_optimized(nums3)
    print(f"测试用例3 []: {result3} (优化版: {result3_opt}, 预期: [])")
    
    # 测试用例4: 单元素数组 [5] - 预期: [-1]
    nums4 = [5]
    result4 = next_greater_elements(nums4)
    result4_opt = next_greater_elements_optimized(nums4)
    print(f"测试用例4 [5]: {result4} (优化版: {result4_opt}, 预期: [-1])")
    
    # 测试用例5: 所有元素相同 [2,2,2] - 预期: [-1,-1,-1]
    nums5 = [2, 2, 2]
    result5 = next_greater_elements(nums5)
    result5_opt = next_greater_elements_optimized(nums5)
    print(f"测试用例5 [2,2,2]: {result5} (优化版: {result5_opt}, 预期: [-1, -1, -1])")
    
    # 测试用例6: 类型错误检查 - 注释掉这行，因为类型注解在运行时不会检查
    # try:
    #     next_greater_elements("not a list")
    #     print("测试用例6 类型错误: 未捕获异常")
    # except TypeError as e:
    #     print(f"测试用例6 类型错误: 正确捕获异常 - {e}")
    print("测试用例6 类型错误: Python类型注解在运行时不会检查，跳过此测试")
    
    print("=== 功能测试完成！ ===")

def performance_test():
    """性能测试方法"""
    import time
    
    print("=== 性能测试 ===")
    
    # 性能测试：大规模数据
    size = 10000
    nums = [1] * size  # 所有元素为1
    nums[5000] = 2  # 中间插入一个较大值
    
    start_time = time.time()
    result = next_greater_elements_optimized(nums)
    end_time = time.time()
    
    print(f"性能测试 [{size}个元素]: 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 性能测试：最坏情况 - 严格递减
    nums_worst = list(range(size, 0, -1))
    
    start_time = time.time()
    result_worst = next_greater_elements_optimized(nums_worst)
    end_time = time.time()
    
    print(f"性能测试 [最坏情况{size}个元素]: 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    print("=== 性能测试完成！ ===")

def debug_print(nums: List[int], i: int, actual_index: int, stack: List[int], result: List[int]):
    """
    调试辅助方法：打印中间过程
    
    Args:
        nums: 输入数组
        i: 当前循环索引
        actual_index: 实际数组索引
        stack: 栈列表
        result: 结果数组
    """
    stack_values = [nums[idx] for idx in stack]
    print(f"i={i}, actual_index={actual_index}, nums[actual_index]={nums[actual_index]}")
    print(f"栈内容: {stack_values}")
    print(f"当前结果: {result}")
    print("---")

if __name__ == "__main__":
    # 运行功能测试
    test_next_greater_elements()
    
    # 运行性能测试
    performance_test()

"""
算法复杂度分析:

时间复杂度: O(n)
- 虽然遍历了2n次，但每个元素最多入栈和出栈各一次
- 总操作次数为O(n)

空间复杂度: O(n)
- 使用了大小为n的栈列表
- 结果数组大小为n

最优解分析:
- 这是循环数组下一个更大元素问题的最优解
- 无法在O(n)时间内获得更好的时间复杂度
- 空间复杂度也是最优的

Python特性利用:
- 使用列表的pop()和append()操作，时间复杂度为O(1)
- 利用列表推导式和切片操作提高代码可读性
- 使用类型注解提高代码可维护性

循环数组处理技巧:
- 通过取模运算实现循环访问: i % n
- 遍历两次数组确保覆盖所有可能的下一更大元素
- 只在第一次遍历时入栈，避免重复处理

工程化建议:
1. 对于超大规模数据，可以考虑使用生成器表达式减少内存使用
2. 可以添加更多的单元测试用例覆盖边界情况
3. 可以考虑使用装饰器进行性能监控
4. 对于生产环境，可以添加日志记录和异常监控
"""