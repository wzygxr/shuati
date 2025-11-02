"""
环形数组的子数组最大累加和 - Python实现
给定一个数组nums，长度为n
nums是一个环形数组，下标0和下标n-1是连在一起的
返回环形数组中，子数组最大累加和
测试链接 : https://leetcode.cn/problems/maximum-sum-circular-subarray/

算法核心思想：
1. 环形数组的最大子数组和有两种情况：
   a) 最大子数组不跨越数组边界（普通Kadane算法）
   b) 最大子数组跨越数组边界（总和减去最小子数组和）
2. 关键观察：环形数组的最大子数组和 = max(最大子数组和, 总和 - 最小子数组和)
3. 特殊情况：当所有元素都是负数时，最小子数组和等于总和，此时返回最大子数组和

时间复杂度分析：
- 最优时间复杂度：O(n) - 只需遍历数组一次
- 空间复杂度：O(1) - 优化后只需常数空间

工程化考量：
1. 边界处理：处理空数组、单元素数组等特殊情况
2. 异常防御：处理数值溢出等极端情况
3. 性能优化：单次遍历同时计算最大和最小子数组和
4. 可测试性：提供完整的测试用例和性能分析
"""

from typing import List
import time

class MaximumSumCircularSubarray:
    """
    环形数组最大子数组和问题的解决方案类
    提供多种实现方式和工具方法
    """
    
    @staticmethod
    def max_subarray_sum_circular(nums: List[int]) -> int:
        """
        计算环形数组的最大子数组和（单次遍历优化版本）
        
        算法原理：
        - 情况1：最大子数组不跨越边界，即普通Kadane算法的结果
        - 情况2：最大子数组跨越边界，即总和减去最小子数组和
        - 特殊情况：当所有元素都是负数时，最小子数组和等于总和
        
        时间复杂度：O(n) - 单次遍历
        空间复杂度：O(1) - 常数空间
        
        Args:
            nums: 整数列表（环形数组）
            
        Returns:
            int: 环形数组的最大子数组和
            
        Raises:
            ValueError: 如果输入数组为空
            
        Examples:
            >>> MaximumSumCircularSubarray.max_subarray_sum_circular([5, -3, 5])
            10
            >>> MaximumSumCircularSubarray.max_subarray_sum_circular([-3, -2, -1])
            -1
        """
        # 边界检查
        if not nums:
            raise ValueError("输入数组不能为空")
        
        n = len(nums)
        # 特殊情况：单元素数组
        if n == 1:
            return nums[0]
        
        # 初始化变量
        total_sum = nums[0]        # 数组总和
        max_sum = nums[0]          # 最大子数组和（不跨越边界）
        min_sum = nums[0]          # 最小子数组和
        current_max = nums[0]      # 当前最大子数组和
        current_min = nums[0]      # 当前最小子数组和
        
        # 单次遍历同时计算最大和最小子数组和
        for i in range(1, n):
            # 累加总和
            total_sum += nums[i]
            
            # 更新最大子数组和（Kadane算法）
            current_max = max(nums[i], current_max + nums[i])
            max_sum = max(max_sum, current_max)
            
            # 更新最小子数组和
            current_min = min(nums[i], current_min + nums[i])
            min_sum = min(min_sum, current_min)
        
        # 特殊情况处理：如果所有元素都是负数
        # 此时min_sum == total_sum，应该返回max_sum（最大的单个负数）
        if total_sum == min_sum:
            return max_sum
        
        # 返回两种情况的最大值
        return max(max_sum, total_sum - min_sum)
    
    @staticmethod
    def max_subarray_sum_circular_two_pass(nums: List[int]) -> int:
        """
        两次遍历版本：更直观的实现方式
        
        算法细节：
        - 第一次遍历计算不跨越边界的最大子数组和
        - 第二次遍历计算跨越边界的最大子数组和
        - 比较两种情况的最大值
        
        时间复杂度：O(n) - 两次遍历
        空间复杂度：O(1) - 常数空间
        
        Args:
            nums: 整数列表
            
        Returns:
            int: 环形数组的最大子数组和
        """
        if not nums:
            raise ValueError("输入数组不能为空")
        
        n = len(nums)
        if n == 1:
            return nums[0]
        
        # 情况1：不跨越边界的最大子数组和
        max_sum1 = nums[0]
        current = nums[0]
        for i in range(1, n):
            current = max(nums[i], current + nums[i])
            max_sum1 = max(max_sum1, current)
        
        # 情况2：跨越边界的最大子数组和
        total_sum = sum(nums)
        
        min_sum = nums[0]
        current = nums[0]
        for i in range(1, n):
            current = min(nums[i], current + nums[i])
            min_sum = min(min_sum, current)
        
        max_sum2 = total_sum - min_sum
        
        # 特殊情况：全负数数组
        if total_sum == min_sum:
            return max_sum1
        
        return max(max_sum1, max_sum2)
    
    @staticmethod
    def test_all_methods():
        """测试所有实现方法的一致性"""
        test_cases = [
            ([5, -3, 5], 10),           # 跨越边界
            ([-3, -2, -1], -1),         # 全负数
            ([1, -2, 3, -2], 3),       # 不跨越边界
            ([5], 5),                   # 单元素
            ([3, -1, 2, -1], 4),        # 跨越边界
            ([1, 2, 3, 4], 10),         # 全正数
            ([-2, -3, -1, -5], -1)      # 全负数
        ]
        
        print("=== 环形数组最大子数组和算法测试 ===")
        
        for i, (nums, expected) in enumerate(test_cases, 1):
            try:
                result1 = MaximumSumCircularSubarray.max_subarray_sum_circular(nums)
                result2 = MaximumSumCircularSubarray.max_subarray_sum_circular_two_pass(nums)
                
                print(f"测试用例 {i}:")
                print(f"  输入: {nums}")
                print(f"  期望: {expected}")
                print(f"  方法1结果: {result1} {'✓' if result1 == expected else '✗'}")
                print(f"  方法2结果: {result2} {'✓' if result2 == expected else '✗'}")
                print(f"  方法一致性: {'一致' if result1 == result2 else '不一致'}")
                print()
                
            except Exception as e:
                print(f"测试用例 {i} 异常: {e}")
                print()
        
        print("=== 测试完成 ===")
    
    @staticmethod
    def performance_test():
        """性能测试：大数据量验证"""
        size = 1000000
        large_array = [1] * size  # 全1数组
        
        print("=== 性能测试开始 ===")
        print(f"数据量: {size} 个元素")
        
        start_time = time.time()
        result = MaximumSumCircularSubarray.max_subarray_sum_circular(large_array)
        end_time = time.time()
        
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        
        print(f"计算结果: {result}")
        print(f"执行时间: {duration:.2f} 毫秒")
        print("=== 性能测试结束 ===")


def max_subarray_sum_circular_simple(nums: List[int]) -> int:
    """
    简化版本：适合快速实现和面试
    
    Args:
        nums: 整数列表
        
    Returns:
        int: 环形数组的最大子数组和
    """
    if not nums:
        return 0
    
    n = len(nums)
    if n == 1:
        return nums[0]
    
    total = max_sum = min_sum = current_max = current_min = nums[0]
    
    for i in range(1, n):
        total += nums[i]
        current_max = max(nums[i], current_max + nums[i])
        max_sum = max(max_sum, current_max)
        current_min = min(nums[i], current_min + nums[i])
        min_sum = min(min_sum, current_min)
    
    return max_sum if total == min_sum else max(max_sum, total - min_sum)


if __name__ == "__main__":
    # 运行功能测试
    MaximumSumCircularSubarray.test_all_methods()
    
    # 运行性能测试（可选）
    # MaximumSumCircularSubarray.performance_test()
    
    # 简单使用示例
    test_nums = [5, -3, 5]
    result = max_subarray_sum_circular_simple(test_nums)
    print(f"简单版本测试: {test_nums} -> {result}")

"""
扩展思考与工程化考量：

1. 算法正确性深度分析：
   - 环形数组的特性决定了最大子数组和的两种可能情况
   - 特殊情况处理（全负数）保证了算法的完备性
   - 数学证明：两种情况的并集覆盖了所有可能性

2. 工程实践要点：
   - 边界处理：空数组、单元素数组等特殊情况
   - 性能优化：单次遍历 vs 两次遍历的选择
   - 代码可读性：清晰的变量命名和注释

3. 测试策略：
   - 单元测试：覆盖各种边界情况
   - 性能测试：验证大数据量处理能力
   - 一致性测试：确保不同实现方法结果一致

4. 多语言对比优势：
   - Python：开发效率高，适合快速原型
   - Java：企业级应用，类型安全
   - C++：性能最优，适合高性能场景
"""