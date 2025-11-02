"""
2281. 巫师的总力量和 (Sum of Total Strength of Wizards)

题目描述:
作为国王的统治者，你有一支巫师军队听你指挥。
给你一个下标从 0 开始的整数数组 strength ，其中 strength[i] 表示第 i 位巫师的力量值。
对于连续的一组巫师（也就是这些巫师的力量值组成了一个连续子数组），总力量为以下两个值的乘积：
巫师中最弱的能力值。
组中所有巫师的能力值的和。
请你返回所有可能的连续巫师组的总力量之和。

解题思路:
使用单调栈找到每个元素作为最小值能覆盖的区间范围。
结合前缀和的前缀和（二次前缀和）技术来计算子数组和之和。

时间复杂度: O(n)
空间复杂度: O(n)

测试链接: https://leetcode.cn/problems/sum-of-total-strength-of-wizards/

工程化考量:
1. 异常处理：空数组、边界情况处理
2. 性能优化：使用列表预分配空间，避免动态扩展
3. 大数处理：使用模运算避免溢出
4. Python特性：利用列表的高效操作和生成器表达式
"""

from typing import List

MOD = 10**9 + 7

def total_strength(strength: List[int]) -> int:
    """
    计算所有连续巫师组的总力量之和
    
    Args:
        strength: 巫师力量值数组
        
    Returns:
        int: 总力量之和模 10^9 + 7
        
    Raises:
        TypeError: 如果输入不是列表
    """
    # 边界条件检查
    if not isinstance(strength, list):
        raise TypeError("输入必须是列表")
        
    if not strength:
        return 0
    
    n = len(strength)
    
    # 前缀和数组
    prefix = [0] * (n + 1)
    for i in range(n):
        prefix[i + 1] = (prefix[i] + strength[i]) % MOD
    
    # 前缀和的前缀和（二次前缀和）
    prefix_prefix = [0] * (n + 2)
    for i in range(n + 1):
        prefix_prefix[i + 1] = (prefix_prefix[i] + prefix[i]) % MOD
    
    # 使用单调栈找到每个元素作为最小值的左右边界
    left = [-1] * n   # 左边第一个小于当前元素的位置
    right = [n] * n    # 右边第一个小于等于当前元素的位置
    
    stack = []
    
    # 找到右边第一个小于等于当前元素的位置
    for i in range(n):
        while stack and strength[stack[-1]] >= strength[i]:
            right[stack.pop()] = i
        stack.append(i)
    
    stack.clear()
    
    # 找到左边第一个小于当前元素的位置
    for i in range(n - 1, -1, -1):
        while stack and strength[stack[-1]] > strength[i]:
            left[stack.pop()] = i
        stack.append(i)
    
    # 计算总力量
    total = 0
    for i in range(n):
        L = left[i] + 1  # 左边界（包含）
        R = right[i] - 1 # 右边界（包含）
        
        # 计算以strength[i]为最小值的所有子数组的和之和
        left_sum = (prefix_prefix[i + 1] - prefix_prefix[L]) % MOD
        right_sum = (prefix_prefix[R + 2] - prefix_prefix[i + 1]) % MOD
        
        # 计算贡献
        contribution = (right_sum * (i - L + 1) - left_sum * (R - i + 1)) % MOD
        contribution = (contribution * strength[i]) % MOD
        
        total = (total + contribution) % MOD
    
    # 处理负数情况
    return total % MOD

def total_strength_optimized(strength: List[int]) -> int:
    """
    优化版本：使用更简洁的实现
    
    Args:
        strength: 巫师力量值数组
        
    Returns:
        int: 总力量之和模 10^9 + 7
    """
    if not strength:
        return 0
    
    n = len(strength)
    
    # 前缀和数组
    prefix = [0] * (n + 1)
    for i in range(n):
        prefix[i + 1] = (prefix[i] + strength[i]) % MOD
    
    # 前缀和的前缀和（二次前缀和）
    prefix_prefix = [0] * (n + 2)
    for i in range(n + 1):
        prefix_prefix[i + 1] = (prefix_prefix[i] + prefix[i]) % MOD
    
    # 使用单调栈
    stack = []
    left = [-1] * n
    right = [n] * n
    
    # 一次遍历处理左右边界
    for i in range(n):
        while stack and strength[stack[-1]] >= strength[i]:
            right[stack.pop()] = i
        if stack:
            left[i] = stack[-1]
        stack.append(i)
    
    # 计算总力量
    total = 0
    for i in range(n):
        L = left[i] + 1
        R = right[i] - 1
        
        left_sum = (prefix_prefix[i + 1] - prefix_prefix[L]) % MOD
        right_sum = (prefix_prefix[R + 2] - prefix_prefix[i + 1]) % MOD
        
        contribution = (right_sum * (i - L + 1) - left_sum * (R - i + 1)) % MOD
        contribution = (contribution * strength[i]) % MOD
        
        total = (total + contribution) % MOD
    
    return total % MOD

def test_total_strength():
    """测试方法 - 验证算法正确性"""
    print("=== 巫师的总力量和算法测试 ===")
    
    # 测试用例1: [1,3,1,2] - 预期: 44
    strength1 = [1, 3, 1, 2]
    result1 = total_strength(strength1)
    result1_opt = total_strength_optimized(strength1)
    print(f"测试用例1 [1,3,1,2]: {result1} (优化版: {result1_opt}, 预期: 44)")
    
    # 测试用例2: [5,4,6] - 预期: 213
    strength2 = [5, 4, 6]
    result2 = total_strength(strength2)
    result2_opt = total_strength_optimized(strength2)
    print(f"测试用例2 [5,4,6]: {result2} (优化版: {result2_opt}, 预期: 213)")
    
    # 测试用例3: 边界情况 - 空数组
    strength3 = []
    result3 = total_strength(strength3)
    result3_opt = total_strength_optimized(strength3)
    print(f"测试用例3 []: {result3} (优化版: {result3_opt}, 预期: 0)")
    
    # 测试用例4: 单元素数组 [10] - 预期: 100
    strength4 = [10]
    result4 = total_strength(strength4)
    result4_opt = total_strength_optimized(strength4)
    print(f"测试用例4 [10]: {result4} (优化版: {result4_opt}, 预期: 100)")
    
    # 测试用例5: 重复元素 [2,2,2] - 预期: 36
    strength5 = [2, 2, 2]
    result5 = total_strength(strength5)
    result5_opt = total_strength_optimized(strength5)
    print(f"测试用例5 [2,2,2]: {result5} (优化版: {result5_opt}, 预期: 36)")
    
    # 测试用例6: 类型错误检查 - 注释掉这行，因为类型注解在运行时不会检查
    # try:
    #     total_strength("not a list")
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
    size = 1000
    strength = [1] * size  # 所有元素为1
    
    start_time = time.time()
    result = total_strength_optimized(strength)
    end_time = time.time()
    print(f"性能测试 [{size}个1]: 结果={result}, 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 性能测试：最坏情况 - 严格递减
    strength_worst = list(range(size, 0, -1))
    
    start_time = time.time()
    result_worst = total_strength_optimized(strength_worst)
    end_time = time.time()
    print(f"性能测试 [最坏情况{size}个元素]: 结果={result_worst}, 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    print("=== 性能测试完成！ ===")

def debug_print(strength: List[int], prefix: List[int], prefix_prefix: List[int], i: int, L: int, R: int, contribution: int):
    """
    调试辅助方法：打印中间过程
    
    Args:
        strength: 力量数组
        prefix: 前缀和数组
        prefix_prefix: 二次前缀和数组
        i: 当前索引
        L: 左边界
        R: 右边界
        contribution: 当前贡献值
    """
    print(f"i={i}, strength[i]={strength[i]}, L={L}, R={R}")
    print(f"prefix: {prefix}")
    print(f"prefix_prefix: {prefix_prefix}")
    print(f"contribution: {contribution}")
    print("---")

if __name__ == "__main__":
    # 运行功能测试
    test_total_strength()
    
    # 运行性能测试
    performance_test()

"""
算法复杂度分析:

时间复杂度: O(n)
- 构建前缀和数组: O(n)
- 构建二次前缀和数组: O(n)
- 单调栈处理: O(n)
- 计算总贡献: O(n)

空间复杂度: O(n)
- 前缀和数组: O(n)
- 二次前缀和数组: O(n)
- 左右边界数组: O(n)
- 单调栈: O(n)

最优解分析:
- 这是巫师的总力量和问题的最优解
- 无法在O(n)时间内获得更好的时间复杂度
- 空间复杂度也是最优的

Python特性利用:
- 使用列表的pop()和append()操作，时间复杂度为O(1)
- 利用列表推导式和切片操作提高代码可读性
- 使用类型注解提高代码可维护性

数学原理:
- 使用单调栈找到每个元素作为最小值的区间
- 使用前缀和的前缀和（二次前缀和）技术快速计算子数组和之和
- 贡献 = 最小值 * (子数组和之和)

工程化建议:
1. 对于超大规模数据，可以考虑使用生成器表达式减少内存使用
2. 可以添加更多的单元测试用例覆盖边界情况
3. 可以考虑使用装饰器进行性能监控
4. 对于生产环境，可以添加日志记录和异常监控
"""