"""
子数组最大累加和问题（Kadane算法）- Python实现
题目描述：给你一个整数数组 nums，返回非空子数组的最大累加和
测试链接：https://leetcode.cn/problems/maximum-subarray/

算法核心思想：
1. Kadane算法是解决最大子数组和问题的经典动态规划算法
2. 对于每个位置，我们有两个选择：
   a) 将当前元素加入到之前的子数组中
   b) 以当前元素开始一个新的子数组
3. 取这两个选择中的较大值作为以当前元素结尾的最大子数组和

时间复杂度分析：
- 最优时间复杂度：O(n) - 只需遍历数组一次
- 空间复杂度：O(1) - 优化后只需常数空间

工程化考量：
1. 异常处理：对空数组和边界情况进行处理
2. 鲁棒性：处理极端输入（全负数、全正数、混合情况）
3. 可测试性：提供完整的测试用例和性能分析
4. 文档化：详细的文档字符串和类型注解
"""

from typing import List, Tuple
import time

class MaximumSubarray:
    """
    最大子数组和问题的解决方案类
    提供多种实现方式和工具方法
    """
    
    @staticmethod
    def max_subarray(nums: List[int]) -> int:
        """
        使用Kadane算法计算最大子数组和（空间优化版本）
        
        算法细节：
        - 维护当前子数组和和全局最大和
        - 对于每个元素，决定是加入前面子数组还是重新开始
        - 时间复杂度：O(n)，空间复杂度：O(1)
        
        Args:
            nums: 整数列表，输入数组
            
        Returns:
            int: 最大子数组和
            
        Raises:
            ValueError: 如果输入数组为空
            
        Examples:
            >>> MaximumSubarray.max_subarray([-2, 1, -3, 4, -1, 2, 1, -5, 4])
            6
            >>> MaximumSubarray.max_subarray([-1, -2, -3, -4])
            -1
        """
        # 边界检查：处理空数组情况
        if not nums:
            raise ValueError("输入数组不能为空")
        
        max_sum = current_sum = nums[0]
        
        # 从第二个元素开始遍历
        for num in nums[1:]:
            # 关键决策：加入前面子数组或重新开始
            # 如果前面子数组和为负，重新开始更优
            current_sum = max(num, current_sum + num)
            # 更新全局最大值
            max_sum = max(max_sum, current_sum)
        
        return max_sum
    
    @staticmethod
    def max_subarray_dp(nums: List[int]) -> int:
        """
        动态规划版本（用于教学和理解）
        
        算法细节：
        - 使用dp数组记录以每个位置结尾的最大子数组和
        - 更直观地展示状态转移过程
        - 时间复杂度：O(n)，空间复杂度：O(n)
        
        Args:
            nums: 整数列表
            
        Returns:
            int: 最大子数组和
        """
        if not nums:
            raise ValueError("输入数组不能为空")
        
        n = len(nums)
        dp = [0] * n
        dp[0] = nums[0]
        max_sum = nums[0]
        
        for i in range(1, n):
            dp[i] = max(nums[i], dp[i-1] + nums[i])
            max_sum = max(max_sum, dp[i])
        
        return max_sum
    
    @staticmethod
    def find_max_subarray_positions(nums: List[int]) -> Tuple[int, int, int]:
        """
        找到最大子数组的位置和和值
        
        Args:
            nums: 整数列表
            
        Returns:
            Tuple[int, int, int]: (起始索引, 结束索引, 最大和)
            
        Examples:
            >>> MaximumSubarray.find_max_subarray_positions([-2, 1, -3, 4, -1, 2, 1, -5, 4])
            (3, 6, 6)
        """
        if not nums:
            raise ValueError("输入数组不能为空")
        
        max_sum = float('-inf')
        current_sum = float('-inf')
        left = right = current_left = 0
        
        for i, num in enumerate(nums):
            if current_sum >= 0:
                current_sum += num
            else:
                current_sum = num
                current_left = i
            
            if current_sum > max_sum:
                max_sum = current_sum
                left = current_left
                right = i
        
        return left, right, max_sum
    
    @staticmethod
    def test_all_methods():
        """测试所有实现方法的一致性"""
        test_cases = [
            ([-2, 1, -3, 4, -1, 2, 1, -5, 4], 6),
            ([-1, -2, -3, -4], -1),
            ([1, 2, 3, 4], 10),
            ([5], 5),
            ([0, -1, 2, -3, 4], 4),
            ([-1], -1),
            ([1, -1, 1, -1, 1], 1)
        ]
        
        print("=== 最大子数组和算法测试 ===")
        
        for i, (nums, expected) in enumerate(test_cases, 1):
            try:
                result1 = MaximumSubarray.max_subarray(nums)
                result2 = MaximumSubarray.max_subarray_dp(nums)
                
                print(f"测试用例 {i}:")
                print(f"  输入: {nums}")
                print(f"  期望: {expected}")
                print(f"  方法1结果: {result1} {'✓' if result1 == expected else '✗'}")
                print(f"  方法2结果: {result2} {'✓' if result2 == expected else '✗'}")
                print(f"  方法一致性: {'一致' if result1 == result2 else '不一致'}")
                
                # 测试位置记录功能
                left, right, sum_val = MaximumSubarray.find_max_subarray_positions(nums)
                print(f"  最大子数组位置: [{left}, {right}], 和: {sum_val}")
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
        result = MaximumSubarray.max_subarray(large_array)
        end_time = time.time()
        
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        
        print(f"计算结果: {result}")
        print(f"执行时间: {duration:.2f} 毫秒")
        print("=== 性能测试结束 ===")


def max_subarray_simple(nums: List[int]) -> int:
    """
    简化版本：适合快速实现和面试
    
    Args:
        nums: 整数列表
        
    Returns:
        int: 最大子数组和
    """
    if not nums:
        return 0
    
    max_sum = current_sum = nums[0]
    
    for num in nums[1:]:
        current_sum = max(num, current_sum + num)
        max_sum = max(max_sum, current_sum)
    
    return max_sum


if __name__ == "__main__":
    # 运行功能测试
    MaximumSubarray.test_all_methods()
    
    # 运行性能测试（可选）
    # MaximumSubarray.performance_test()
    
    # 简单使用示例
    test_nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
    result = max_subarray_simple(test_nums)
    print(f"简单版本测试: {test_nums} -> {result}")

"""
扩展思考与工程化考量：

1. Python语言特性优势：
   - 动态类型：开发快速，适合原型
   - 内置函数：max()等函数优化良好
   - 列表推导：简洁的数据处理

2. 异常处理策略：
   - 使用ValueError处理非法输入
   - 提供详细的错误信息
   - 支持多种调用方式

3. 测试驱动开发：
   - 单元测试覆盖边界情况
   - 性能测试验证大数据处理
   - 文档测试确保示例正确

4. 代码质量保证：
   - 类型注解提高可读性
   - 文档字符串详细说明
   - 遵循PEP8编码规范

5. 多语言对比：
   - Python：开发效率高，适合数据分析
   - Java：企业级应用，生态完善
   - C++：性能最优，适合系统级开发
"""