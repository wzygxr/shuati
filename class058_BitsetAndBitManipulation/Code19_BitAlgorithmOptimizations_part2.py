"""
位算法优化实现 - 第二部分
包含LeetCode多个位算法优化相关题目的解决方案

题目列表:
6. LeetCode 135 - 分发糖果
7. LeetCode 149 - 直线上最多的点数
8. LeetCode 152 - 乘积最大子数组
9. LeetCode 169 - 多数元素
10. LeetCode 229 - 求众数 II
"""

import time
from typing import List
import sys
import math
from functools import lru_cache
from collections import defaultdict

class BitAlgorithmOptimizations:
    """位算法优化类"""
    
    @staticmethod
    def candy(ratings: List[int]) -> int:
        """
        LeetCode 135 - 分发糖果
        老师想给孩子们分发糖果，有 N 个孩子站成了一条直线，老师会根据每个孩子的表现，预先给他们评分。
        要求每个孩子至少分配到 1 个糖果，相邻的孩子中，评分高的孩子必须获得更多的糖果。
        
        方法: 两次遍历
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        n = len(ratings)
        candies = [1] * n
        
        # 从左到右遍历
        for i in range(1, n):
            if ratings[i] > ratings[i-1]:
                candies[i] = candies[i-1] + 1
        
        # 从右到左遍历
        for i in range(n-2, -1, -1):
            if ratings[i] > ratings[i+1]:
                candies[i] = max(candies[i], candies[i+1] + 1)
        
        return sum(candies)
    
    @staticmethod
    def max_points(points: List[List[int]]) -> int:
        """
        LeetCode 149 - 直线上最多的点数
        给定一个二维平面，平面上有 n 个点，求最多有多少个点在同一条直线上。
        
        方法: 斜率统计 + 最大公约数
        时间复杂度: O(n^2)
        空间复杂度: O(n)
        """
        if len(points) < 3:
            return len(points)
        
        max_count = 0
        
        for i in range(len(points)):
            slope_count = defaultdict(int)
            duplicate = 1  # 重复点计数
            
            for j in range(i + 1, len(points)):
                dx = points[j][0] - points[i][0]
                dy = points[j][1] - points[i][1]
                
                if dx == 0 and dy == 0:
                    duplicate += 1
                    continue
                
                # 计算斜率（使用最大公约数约分）
                g = math.gcd(dx, dy)
                dx //= g
                dy //= g
                
                slope_count[(dx, dy)] += 1
            
            max_count = max(max_count, duplicate)
            for count in slope_count.values():
                max_count = max(max_count, count + duplicate)
        
        return max_count
    
    @staticmethod
    def max_product(nums: List[int]) -> int:
        """
        LeetCode 152 - 乘积最大子数组
        给你一个整数数组 nums ，请你找出数组中乘积最大的连续子数组，并返回该子数组所对应的乘积。
        
        方法: 动态规划
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        if not nums:
            return 0
        
        max_product = nums[0]
        min_product = nums[0]
        result = nums[0]
        
        for i in range(1, len(nums)):
            if nums[i] < 0:
                max_product, min_product = min_product, max_product
            
            max_product = max(nums[i], max_product * nums[i])
            min_product = min(nums[i], min_product * nums[i])
            
            result = max(result, max_product)
        
        return result
    
    @staticmethod
    def majority_element(nums: List[int]) -> int:
        """
        LeetCode 169 - 多数元素
        给定一个大小为 n 的数组，找到其中的多数元素。多数元素是指在数组中出现次数大于 ⌊ n/2 ⌋ 的元素。
        
        方法: Boyer-Moore投票算法
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        candidate = nums[0]
        count = 1
        
        for i in range(1, len(nums)):
            if count == 0:
                candidate = nums[i]
                count = 1
            elif nums[i] == candidate:
                count += 1
            else:
                count -= 1
        
        return candidate
    
    @staticmethod
    def majority_element_ii(nums: List[int]) -> List[int]:
        """
        LeetCode 229 - 求众数 II
        给定一个大小为 n 的整数数组，找出其中所有出现超过 ⌊ n/3 ⌋ 次的元素。
        
        方法: Boyer-Moore投票算法扩展
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        if not nums:
            return []
        
        candidate1, candidate2 = 0, 0
        count1, count2 = 0, 0
        
        # 第一轮投票
        for num in nums:
            if num == candidate1:
                count1 += 1
            elif num == candidate2:
                count2 += 1
            elif count1 == 0:
                candidate1 = num
                count1 = 1
            elif count2 == 0:
                candidate2 = num
                count2 = 1
            else:
                count1 -= 1
                count2 -= 1
        
        # 第二轮验证
        count1 = count2 = 0
        for num in nums:
            if num == candidate1:
                count1 += 1
            elif num == candidate2:
                count2 += 1
        
        result = []
        n = len(nums)
        if count1 > n // 3:
            result.append(candidate1)
        if count2 > n // 3:
            result.append(candidate2)
        
        return result


class PerformanceTester:
    """性能测试工具类"""
    
    @staticmethod
    def test_divide():
        """测试两数相除性能"""
        print("=== 两数相除性能测试 ===")
        
        dividend = 2**31 - 1
        divisor = 2
        
        start = time.time()
        result = BitAlgorithmOptimizations.divide(dividend, divisor)
        elapsed = (time.time() - start) * 1e6  # 微秒
        
        print(f"divide: {dividend} / {divisor} = {result}, 耗时={elapsed:.2f} μs")
    
    @staticmethod
    def test_my_pow():
        """测试快速幂性能"""
        print("\n=== 快速幂性能测试 ===")
        
        x = 2.0
        n = 1000000
        
        start = time.time()
        result = BitAlgorithmOptimizations.my_pow(x, n)
        elapsed = (time.time() - start) * 1e6  # 微秒
        
        print(f"my_pow: {x}^{n} = {result}, 耗时={elapsed:.2f} μs")
    
    @staticmethod
    def run_unit_tests():
        """运行单元测试"""
        print("=== 位算法优化单元测试 ===")
        
        # 测试两数相除
        assert BitAlgorithmOptimizations.divide(10, 3) == 3
        assert BitAlgorithmOptimizations.divide(7, -3) == -2
        
        # 测试快速幂
        assert abs(BitAlgorithmOptimizations.my_pow(2.0, 10) - 1024.0) < 1e-9
        
        # 测试多数元素
        nums = [2, 2, 1, 1, 1, 2, 2]
        assert BitAlgorithmOptimizations.majority_element(nums) == 2
        
        print("所有单元测试通过!")
    
    @staticmethod
    def complexity_analysis():
        """复杂度分析"""
        print("\n=== 复杂度分析 ===")
        
        algorithms = {
            "divide": ("O(log n)", "O(1)"),
            "my_pow": ("O(log n)", "O(1)"),
            "get_permutation": ("O(n^2)", "O(n)"),
            "gray_code": ("O(2^n)", "O(2^n)"),
            "can_complete_circuit": ("O(n)", "O(1)"),
            "candy": ("O(n)", "O(n)"),
            "max_points": ("O(n^2)", "O(n)"),
            "max_product": ("O(n)", "O(1)"),
            "majority_element": ("O(n)", "O(1)")
        }
        
        for name, (time_complexity, space_complexity) in algorithms.items():
            print(f"{name}: 时间复杂度={time_complexity}, 空间复杂度={space_complexity}")


def main():
    """主函数"""
    print("位算法优化实现")
    print("包含LeetCode多个位算法优化相关题目的解决方案")
    print("=" * 50)
    
    # 运行单元测试
    PerformanceTester.run_unit_tests()
    
    # 运行性能测试
    PerformanceTester.test_divide()
    PerformanceTester.test_my_pow()
    
    # 复杂度分析
    PerformanceTester.complexity_analysis()
    
    # 示例使用
    print("\n=== 示例使用 ===")
    
    # 两数相除示例
    dividend, divisor = 10, 3
    print(f"{dividend} / {divisor} = {BitAlgorithmOptimizations.divide(dividend, divisor)}")
    
    # 快速幂示例
    x, n = 2.0, 10
    print(f"{x}^{n} = {BitAlgorithmOptimizations.my_pow(x, n)}")
    
    # 格雷编码示例
    gray_n = 3
    gray_codes = BitAlgorithmOptimizations.gray_code(gray_n)
    print(f"格雷编码(n={gray_n}): {gray_codes[:5]}...")
    
    # 多数元素示例
    nums = [2, 2, 1, 1, 1, 2, 2]
    print(f"数组: {nums}")
    print(f"多数元素: {BitAlgorithmOptimizations.majority_element(nums)}")


if __name__ == "__main__":
    main()