# 乘积最大子数组 (Maximum Product Subarray)
# 给你一个整数数组 nums ，请你找出数组中乘积最大的非空连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。
# 测试链接 : https://leetcode.cn/problems/maximum-product-subarray/

from typing import List
import sys
import time

class Solution:
    """
    乘积最大子数组 - 动态规划处理正负号问题
    
    时间复杂度分析：
    - 暴力解法：O(n^2) - 枚举所有子数组
    - 动态规划：O(n) - 线性扫描一次
    - 空间优化：O(1) - 只保存必要的前一个状态
    
    空间复杂度分析：
    - 暴力解法：O(1) - 只保存当前最大值
    - 动态规划：O(n) - dp数组存储所有状态
    - 空间优化：O(1) - 工程首选
    
    工程化考量：
    1. 正负号处理：需要同时维护最大值和最小值
    2. 边界处理：空数组、单元素数组、包含0的数组
    3. 性能优化：空间优化版本应对大规模数据
    4. Python特性：利用多重赋值简化变量交换
    """
    
    # 方法1：动态规划（同时维护最大值和最小值）
    # 时间复杂度：O(n) - 遍历数组一次
    # 空间复杂度：O(n) - 使用两个dp数组
    # 核心思路：由于存在负数，最小值可能变成最大值，需要同时维护
    def maxProduct1(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        n = len(nums)
        max_dp = [0] * n  # 存储以i结尾的最大乘积
        min_dp = [0] * n  # 存储以i结尾的最小乘积
        
        max_dp[0] = nums[0]
        min_dp[0] = nums[0]
        result = nums[0]
        
        for i in range(1, n):
            # 三种可能：当前数字、当前数字×最大乘积、当前数字×最小乘积
            num = nums[i]
            option1 = num
            option2 = num * max_dp[i-1]
            option3 = num * min_dp[i-1]
            
            max_dp[i] = max(option1, option2, option3)
            min_dp[i] = min(option1, option2, option3)
            
            result = max(result, max_dp[i])
        
        return result

    # 方法2：空间优化的动态规划
    # 时间复杂度：O(n) - 与方法1相同
    # 空间复杂度：O(1) - 只使用常数空间
    # 优化：使用变量代替数组，减少空间使用
    def maxProduct2(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        n = len(nums)
        max_so_far = nums[0]  # 当前最大乘积
        min_so_far = nums[0]  # 当前最小乘积
        result = nums[0]
        
        for i in range(1, n):
            num = nums[i]
            # 使用多重赋值避免临时变量
            max_so_far, min_so_far = (
                max(num, num * max_so_far, num * min_so_far),
                min(num, num * max_so_far, num * min_so_far)
            )
            
            result = max(result, max_so_far)
        
        return result

    # 方法3：分治解法（用于对比）
    # 时间复杂度：O(n log n) - 分治递归
    # 空间复杂度：O(log n) - 递归栈深度
    # 核心思路：将数组分成左右两部分，最大乘积可能在左、右或跨越中间
    def maxProduct3(self, nums: List[int]) -> int:
        if not nums:
            return 0
        return self.divide_and_conquer(nums, 0, len(nums)-1)
    
    def divide_and_conquer(self, nums: List[int], left: int, right: int) -> int:
        if left == right:
            return nums[left]
        
        mid = left + (right - left) // 2
        
        # 左半部分的最大乘积
        left_max = self.divide_and_conquer(nums, left, mid)
        # 右半部分的最大乘积
        right_max = self.divide_and_conquer(nums, mid+1, right)
        # 跨越中间的最大乘积
        cross_max = self.max_crossing_product(nums, left, mid, right)
        
        return max(left_max, right_max, cross_max)
    
    def max_crossing_product(self, nums: List[int], left: int, mid: int, right: int) -> int:
        # 从左到右计算包含mid的最大乘积
        left_max = nums[mid]
        left_min = nums[mid]
        product = nums[mid]
        
        for i in range(mid-1, left-1, -1):
            product *= nums[i]
            left_max = max(left_max, product)
            left_min = min(left_min, product)
        
        # 从右到左计算包含mid+1的最大乘积
        right_max = nums[mid+1]
        right_min = nums[mid+1]
        product = nums[mid+1]
        
        for i in range(mid+2, right+1):
            product *= nums[i]
            right_max = max(right_max, product)
            right_min = min(right_min, product)
        
        # 跨越中间的最大乘积可能是各种组合
        return max(left_max * right_max, left_max * right_min,
                  left_min * right_max, left_min * right_min)

    # 方法4：暴力解法（用于对比）
    # 时间复杂度：O(n^2) - 枚举所有子数组
    # 空间复杂度：O(1) - 只保存当前最大值
    # 问题：效率低，仅用于教学目的
    def maxProduct4(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        n = len(nums)
        result = -sys.maxsize - 1
        
        for i in range(n):
            product = 1
            for j in range(i, n):
                product *= nums[j]
                result = max(result, product)
        
        return result

    # 方法5：前缀积解法（处理0的特殊情况）
    # 时间复杂度：O(n) - 遍历数组两次
    # 空间复杂度：O(1) - 只使用常数空间
    # 核心思路：计算前缀积，遇到0时重置
    def maxProduct5(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        n = len(nums)
        result = nums[0]
        product = 1
        
        # 从左到右计算
        for i in range(n):
            product *= nums[i]
            result = max(result, product)
            if nums[i] == 0:
                product = 1  # 遇到0重置
        
        # 从右到左计算（处理负数情况）
        product = 1
        for i in range(n-1, -1, -1):
            product *= nums[i]
            result = max(result, product)
            if nums[i] == 0:
                product = 1  # 遇到0重置
        
        return result

def test_case(solution: Solution, nums: List[int], expected: int, description: str):
    """测试用例函数"""
    result1 = solution.maxProduct1(nums)
    result2 = solution.maxProduct2(nums)
    result3 = solution.maxProduct3(nums)
    result5 = solution.maxProduct5(nums)
    
    all_correct = (result1 == expected and result2 == expected and 
                  result3 == expected and result5 == expected)
    
    status = "✓" if all_correct else "✗"
    print(f"{description}: {status}")
    
    if not all_correct:
        print(f"  方法1: {result1} | 方法2: {result2} | 方法3: {result3} | "
              f"方法5: {result5} | 预期: {expected}")

def performance_test(solution: Solution, nums: List[int]):
    """性能测试函数"""
    print(f"\n性能测试 n={len(nums)}:")
    
    start = time.time()
    result2 = solution.maxProduct2(nums)
    end = time.time()
    print(f"空间优化方法: {result2}, 耗时: {(end - start) * 1000:.2f}ms")
    
    start = time.time()
    result5 = solution.maxProduct5(nums)
    end = time.time()
    print(f"前缀积方法: {result5}, 耗时: {(end - start) * 1000:.2f}ms")

if __name__ == "__main__":
    solution = Solution()
    
    print("=== 乘积最大子数组测试 ===")
    
    # 边界测试
    test_case(solution, [], 0, "空数组")
    test_case(solution, [5], 5, "单元素数组")
    test_case(solution, [-5], -5, "单负数元素")
    
    # LeetCode示例测试
    test_case(solution, [2, 3, -2, 4], 6, "示例1")
    test_case(solution, [-2, 0, -1], 0, "示例2")
    test_case(solution, [-2, 3, -4], 24, "示例3")
    
    # 常规测试
    test_case(solution, [1, 2, 3, 4, 5], 120, "全正数")
    test_case(solution, [-1, -2, -3, -4, -5], 120, "全负数（偶数个）")
    test_case(solution, [-1, -2, -3, -4], 24, "全负数（奇数个）")
    
    # 包含0的测试
    test_case(solution, [2, 0, 3, 4], 12, "包含0")
    test_case(solution, [-2, 0, 3, 4], 12, "负数后接0")
    test_case(solution, [0, 0, 0, 5], 5, "多个0")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    large_nums = [(i % 10) - 5 for i in range(1000)]  # -5到4的循环数字
    performance_test(solution, large_nums)

"""
算法总结与工程化思考：

1. 问题本质：处理正负号的最大连续子数组乘积问题
   - 关键洞察：负数可能使最小值变成最大值，需要同时维护最大最小值
   - 状态转移：max = max(num, num*max, num*min), min = min(num, num*max, num*min)

2. 时间复杂度对比：
   - 暴力解法：O(n^2) - 不可接受
   - 分治解法：O(n log n) - 可接受但非最优
   - 动态规划：O(n) - 推荐
   - 空间优化：O(n) - 工程首选

3. 空间复杂度对比：
   - 暴力解法：O(1) - 但效率低
   - 分治解法：O(log n) - 递归栈
   - 动态规划：O(n) - 数组存储
   - 空间优化：O(1) - 最优

4. Python特性利用：
   - 多重赋值语法简化变量交换
   - 内置max/min函数简化比较逻辑
   - 列表推导式简化数组操作

5. 特殊情况处理：
   - 包含0的情况：乘积会重置为1
   - 全负数情况：需要考虑乘积的正负性
   - 单个元素情况：直接返回该元素

6. 工程选择依据：
   - 一般情况：方法2（空间优化动态规划）
   - 需要分治思路：方法3（分治解法）
   - 简单实现：方法5（前缀积解法）

7. 调试技巧：
   - 分别跟踪最大值和最小值的变化
   - 验证包含0时的重置逻辑
   - 检查负数相乘的正负号处理

8. 关联题目：
   - 最大子数组和（Kadane算法）
   - 最大连续1的个数
   - 子数组最小乘积的最大值
"""