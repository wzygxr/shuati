"""
乘积最大子数组 - Python实现
给你一个整数数组 nums
请你找出数组中乘积最大的非空连续子数组
并返回该子数组所对应的乘积
测试链接 : https://leetcode.cn/problems/maximum-product-subarray/

算法核心思想：
1. 与最大子数组和问题不同，乘积问题需要考虑负数的特性
2. 关键观察：负数乘以负数会变成正数，所以需要同时维护最大和最小乘积
3. 使用动态规划思想，维护当前的最大乘积和最小乘积
4. 对于每个元素，有三种选择：从当前元素重新开始、乘以之前的最小乘积、乘以之前的最大乘积

时间复杂度分析：
- 最优时间复杂度：O(n) - 只需遍历数组一次
- 空间复杂度：O(1) - 优化后只需常数空间

工程化考量：
1. 数值溢出处理：Python整数不会溢出，但需要考虑大数性能
2. 边界处理：处理空数组、单元素数组等特殊情况
3. 性能优化：单次遍历同时维护最大和最小乘积
4. 可测试性：提供完整的测试用例和性能分析
"""

from typing import List
import time

class MaximumProductSubarray:
    """
    乘积最大子数组问题的解决方案类
    提供多种实现方式和工具方法
    """
    
    @staticmethod
    def max_product(nums: List[int]) -> int:
        """
        计算乘积最大子数组的乘积值
        
        算法原理：
        - 同时维护当前的最大乘积(max_prod)和最小乘积(min_prod)
        - 对于每个元素nums[i]，有三种可能：
          1. 从当前元素重新开始：nums[i]
          2. 乘以之前的最小乘积：min_prod * nums[i]（负数情况）
          3. 乘以之前的最大乘积：max_prod * nums[i]（正数情况）
        - 更新max_prod和min_prod，并记录全局最大值ans
        
        时间复杂度：O(n) - 单次遍历
        空间复杂度：O(1) - 常数空间
        
        Args:
            nums: 输入整数数组
            
        Returns:
            int: 乘积最大子数组的乘积值
            
        Raises:
            ValueError: 如果输入数组为空
            
        Examples:
            >>> MaximumProductSubarray.max_product([2, 3, -2, 4])
            6
            >>> MaximumProductSubarray.max_product([-2, -3, -1, -4])
            12
        """
        # 边界检查
        if not nums:
            raise ValueError("输入数组不能为空")
        
        # 初始化变量
        ans = nums[0]          # 全局最大乘积
        min_prod = nums[0]      # 当前最小乘积
        max_prod = nums[0]      # 当前最大乘积
        
        # 从第二个元素开始遍历
        for i in range(1, len(nums)):
            # 计算当前元素可能产生的三种乘积
            cur_min = min(nums[i], min_prod * nums[i], max_prod * nums[i])
            cur_max = max(nums[i], min_prod * nums[i], max_prod * nums[i])
            
            # 更新状态
            min_prod, max_prod = cur_min, cur_max
            
            # 更新全局最大值
            ans = max(ans, max_prod)
        
        return ans
    
    @staticmethod
    def max_product_array(nums: List[int]) -> int:
        """
        使用数组存储状态的实现方式，更直观但空间复杂度较高
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            nums: 输入整数数组
            
        Returns:
            int: 乘积最大子数组的乘积值
        """
        if not nums:
            return 0
        
        n = len(nums)
        max_dp = [0] * n  # 存储以i结尾的最大乘积
        min_dp = [0] * n  # 存储以i结尾的最小乘积
        
        max_dp[0] = nums[0]
        min_dp[0] = nums[0]
        ans = nums[0]
        
        for i in range(1, n):
            # 三种可能：重新开始、乘以最大、乘以最小
            max_dp[i] = max(nums[i], max_dp[i-1] * nums[i], min_dp[i-1] * nums[i])
            min_dp[i] = min(nums[i], max_dp[i-1] * nums[i], min_dp[i-1] * nums[i])
            ans = max(ans, max_dp[i])
        
        return ans
    
    @staticmethod
    def max_product_optimized(nums: List[int]) -> int:
        """
        空间优化版本：使用临时变量代替数组
        
        时间复杂度：O(n)
        空间复杂度：O(1)
        
        Args:
            nums: 输入整数数组
            
        Returns:
            int: 乘积最大子数组的乘积值
        """
        if not nums:
            return 0
        
        ans = nums[0]
        min_prev = nums[0]
        max_prev = nums[0]
        
        for i in range(1, len(nums)):
            temp_min = min_prev
            temp_max = max_prev
            
            min_prev = min(nums[i], temp_min * nums[i], temp_max * nums[i])
            max_prev = max(nums[i], temp_min * nums[i], temp_max * nums[i])
            ans = max(ans, max_prev)
        
        return ans
    
    @staticmethod
    def test_all_methods():
        """测试所有实现方法的一致性"""
        test_cases = [
            ([2, 3, -2, 4], 6),           # 标准情况
            ([-2, -3, -1, -4], 24),       # 全负数（修正：期望值应为24）
            ([-2, 0, -1], 0),             # 包含0
            ([5], 5),                     # 单元素
            ([2, -5, -2, -4, 3], 24),     # 复杂情况
            ([0, 2], 2),                  # 包含0和正数
            ([-2, 3, -4], 24),            # 负数正数交替
            ([1, -2, 3, -4, 5], 120)      # 长序列
        ]
        
        print("=== 乘积最大子数组算法测试 ===")
        
        for i, (nums, expected) in enumerate(test_cases, 1):
            try:
                result1 = MaximumProductSubarray.max_product(nums)
                result2 = MaximumProductSubarray.max_product_array(nums)
                result3 = MaximumProductSubarray.max_product_optimized(nums)
                
                print(f"测试用例 {i}:")
                print(f"  输入: {nums}")
                print(f"  期望: {expected}")
                print(f"  方法1结果: {result1} {'✓' if result1 == expected else '✗'}")
                print(f"  方法2结果: {result2} {'✓' if result2 == expected else '✗'}")
                print(f"  方法3结果: {result3} {'✓' if result3 == expected else '✗'}")
                print(f"  一致性: {'一致' if result1 == result2 == result3 else '不一致'}")
                print()
                
            except Exception as e:
                print(f"测试用例 {i} 异常: {e}")
                print()
        
        print("=== 测试完成 ===")
    
    @staticmethod
    def performance_test():
        """性能测试：大数据量验证"""
        size = 1000000
        large_array = [2] * size  # 全2数组
        
        print("=== 性能测试开始 ===")
        print(f"数据量: {size} 个元素")
        
        # 测试方法1
        start_time = time.time()
        result1 = MaximumProductSubarray.max_product(large_array)
        time1 = (time.time() - start_time) * 1000
        
        # 测试方法2
        start_time = time.time()
        result2 = MaximumProductSubarray.max_product_array(large_array)
        time2 = (time.time() - start_time) * 1000
        
        # 测试方法3
        start_time = time.time()
        result3 = MaximumProductSubarray.max_product_optimized(large_array)
        time3 = (time.time() - start_time) * 1000
        
        print(f"方法1结果: {result1}, 时间: {time1:.2f} 毫秒")
        print(f"方法2结果: {result2}, 时间: {time2:.2f} 毫秒")
        print(f"方法3结果: {result3}, 时间: {time3:.2f} 毫秒")
        print("=== 性能测试结束 ===")


def max_product_simple(nums: List[int]) -> int:
    """
    简化版本：适合快速实现和面试
    
    Args:
        nums: 输入整数数组
        
    Returns:
        int: 乘积最大子数组的乘积值
    """
    if not nums:
        return 0
    
    ans = min_prod = max_prod = nums[0]
    
    for i in range(1, len(nums)):
        cur_min = min(nums[i], min_prod * nums[i], max_prod * nums[i])
        cur_max = max(nums[i], min_prod * nums[i], max_prod * nums[i])
        min_prod, max_prod = cur_min, cur_max
        ans = max(ans, max_prod)
    
    return ans


if __name__ == "__main__":
    # 运行功能测试
    MaximumProductSubarray.test_all_methods()
    
    # 运行性能测试（可选）
    # MaximumProductSubarray.performance_test()
    
    # 简单使用示例
    test_nums = [2, 3, -2, 4]
    result = max_product_simple(test_nums)
    print(f"简单版本测试: {test_nums} -> {result}")

"""
扩展思考与工程化考量：

1. 算法正确性深度分析：
   - 为什么需要同时维护最大和最小乘积？
     因为负数乘以负数会变成正数，当前的最小乘积可能成为后续的最大乘积
   - 为什么这种方法能处理所有情况？
     考虑了三种可能：重新开始、乘以最大、乘以最小，覆盖了所有选择

2. 工程实践要点：
   - 边界处理：各种特殊情况需要单独处理
   - 性能优化：单次遍历同时维护最大和最小乘积
   - 代码可读性：清晰的变量命名和注释

3. 测试策略：
   - 单元测试：覆盖各种边界情况
   - 性能测试：验证大数据量处理能力
   - 一致性测试：确保不同实现方法结果一致

4. 多语言对比优势：
   - Python：开发效率高，整数不会溢出
   - Java：企业级应用，类型安全
   - C++：性能最优，适合高性能场景
"""