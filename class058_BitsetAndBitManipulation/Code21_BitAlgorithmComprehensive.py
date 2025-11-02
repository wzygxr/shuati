"""
综合位算法实现
包含LeetCode多个综合位算法相关题目的解决方案

题目列表:
1. LeetCode 191 - 位1的个数
2. LeetCode 231 - 2的幂
3. LeetCode 342 - 4的幂
4. LeetCode 268 - 丢失的数字
5. LeetCode 136 - 只出现一次的数字
6. LeetCode 137 - 只出现一次的数字 II
7. LeetCode 260 - 只出现一次的数字 III
8. LeetCode 190 - 颠倒二进制位
9. LeetCode 338 - 比特位计数
10. LeetCode 201 - 数字范围按位与

时间复杂度分析:
- 位操作: O(1) 到 O(n)
- 空间复杂度: O(1) 到 O(n)

工程化考量:
1. 位运算优化: 使用位运算替代算术运算
2. 状态压缩: 使用位掩码压缩状态
3. 性能优化: 利用位运算的并行性
4. 边界处理: 处理整数边界、负数等
"""

import time
from typing import List
import sys

class BitAlgorithmComprehensive:
    """综合位算法类"""
    
    @staticmethod
    def hamming_weight(n: int) -> int:
        """
        LeetCode 191 - 位1的个数
        编写一个函数，输入是一个无符号整数，返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。
        
        方法: Brian Kernighan算法
        时间复杂度: O(k) - k为1的个数
        空间复杂度: O(1)
        
        原理: n & (n-1) 会清除最低位的1
        """
        count = 0
        while n != 0:
            n &= (n - 1)
            count += 1
        return count
    
    @staticmethod
    def is_power_of_two(n: int) -> bool:
        """
        LeetCode 231 - 2的幂
        给定一个整数，编写一个函数来判断它是否是 2 的幂次方。
        
        方法: 位运算
        时间复杂度: O(1)
        空间复杂度: O(1)
        
        原理: 2的幂的二进制表示中只有1个1
        """
        return n > 0 and (n & (n - 1)) == 0
    
    @staticmethod
    def is_power_of_four(n: int) -> bool:
        """
        LeetCode 342 - 4的幂
        给定一个整数，写一个函数来判断它是否是 4 的幂次方。
        
        方法: 位运算 + 数学
        时间复杂度: O(1)
        空间复杂度: O(1)
        
        原理: 4的幂是2的幂，且1出现在奇数位
        """
        return n > 0 and (n & (n - 1)) == 0 and (n & 0x55555555) != 0
    
    @staticmethod
    def missing_number(nums: List[int]) -> int:
        """
        LeetCode 268 - 丢失的数字
        给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数。
        
        方法: 位运算（异或）
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        原理: a ^ a = 0, a ^ 0 = a
        """
        n = len(nums)
        result = n  # 因为nums包含0到n-1，所以初始化为n
        
        for i in range(n):
            result ^= i
            result ^= nums[i]
        
        return result
    
    @staticmethod
    def single_number(nums: List[int]) -> int:
        """
        LeetCode 136 - 只出现一次的数字
        给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
        
        方法: 位运算（异或）
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        result = 0
        for num in nums:
            result ^= num
        return result
    
    @staticmethod
    def single_number_ii(nums: List[int]) -> int:
        """
        LeetCode 137 - 只出现一次的数字 II
        给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现三次。找出那个只出现了一次的元素。
        
        方法: 位运算（状态机）
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        原理: 使用两个变量记录状态，模拟三进制计数
        """
        ones = 0
        twos = 0
        
        for num in nums:
            ones = (ones ^ num) & ~twos
            twos = (twos ^ num) & ~ones
        
        return ones
    
    @staticmethod
    def single_number_iii(nums: List[int]) -> List[int]:
        """
        LeetCode 260 - 只出现一次的数字 III
        给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。 找出只出现一次的那两个元素。
        
        方法: 位运算（分组异或）
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 获取两个不同数字的异或结果
        xor_result = 0
        for num in nums:
            xor_result ^= num
        
        # 找到最低位的1（两个数字不同的位）
        diff_bit = xor_result & -xor_result
        
        # 根据该位分组
        num1 = 0
        num2 = 0
        for num in nums:
            if num & diff_bit:
                num1 ^= num
            else:
                num2 ^= num
        
        return [num1, num2]
    
    @staticmethod
    def reverse_bits(n: int) -> int:
        """
        LeetCode 190 - 颠倒二进制位
        颠倒给定的 32 位无符号整数的二进制位。
        
        方法: 位运算（分治）
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        # 确保是32位无符号整数
        n = n & 0xFFFFFFFF
        
        # 分治颠倒
        n = (n >> 16) | (n << 16) & 0xFFFFFFFF
        n = ((n & 0xFF00FF00) >> 8) | ((n & 0x00FF00FF) << 8) & 0xFFFFFFFF
        n = ((n & 0xF0F0F0F0) >> 4) | ((n & 0x0F0F0F0F) << 4) & 0xFFFFFFFF
        n = ((n & 0xCCCCCCCC) >> 2) | ((n & 0x33333333) << 2) & 0xFFFFFFFF
        n = ((n & 0xAAAAAAAA) >> 1) | ((n & 0x55555555) << 1) & 0xFFFFFFFF
        
        return n
    
    @staticmethod
    def count_bits(n: int) -> List[int]:
        """
        LeetCode 338 - 比特位计数
        给定一个非负整数 num。对于 0 ≤ i ≤ num 范围中的每个数字 i ，计算其二进制数中的 1 的数目并将它们作为数组返回。
        
        方法: 动态规划 + 位运算
        时间复杂度: O(n)
        空间复杂度: O(n)
        
        原理: i的1的个数 = i/2的1的个数 + i的最低位是否为1
        """
        result = [0] * (n + 1)
        
        for i in range(1, n + 1):
            result[i] = result[i >> 1] + (i & 1)
        
        return result
    
    @staticmethod
    def range_bitwise_and(m: int, n: int) -> int:
        """
        LeetCode 201 - 数字范围按位与
        给定范围 [m, n]，返回此范围内所有数字的按位与（包含 m, n 两端点）。
        
        方法: 位运算（找公共前缀）
        时间复杂度: O(1)
        空间复杂度: O(1)
        
        原理: 结果就是m和n的二进制公共前缀
        """
        shift = 0
        
        # 找到公共前缀
        while m < n:
            m >>= 1
            n >>= 1
            shift += 1
        
        return m << shift


class PerformanceTester:
    """性能测试工具类"""
    
    @staticmethod
    def test_hamming_weight():
        """测试位1的个数性能"""
        print("=== 位1的个数性能测试 ===")
        
        n = 0b10101010101010101010101010101010
        
        start = time.time()
        count = BitAlgorithmComprehensive.hamming_weight(n)
        elapsed = (time.time() - start) * 1e9  # 纳秒
        
        print(f"hammingWeight: 数字{n}的1的个数={count}, 耗时={elapsed:.2f} ns")
    
    @staticmethod
    def test_single_number():
        """测试只出现一次的数字性能"""
        print("\n=== 只出现一次的数字性能测试 ===")
        
        nums = [4, 1, 2, 1, 2]
        
        start = time.time()
        result = BitAlgorithmComprehensive.single_number(nums)
        elapsed = (time.time() - start) * 1e9  # 纳秒
        
        print(f"singleNumber: 结果={result}, 耗时={elapsed:.2f} ns")
    
    @staticmethod
    def test_count_bits():
        """测试比特位计数性能"""
        print("\n=== 比特位计数性能测试 ===")
        
        n = 1000000
        
        start = time.time()
        result = BitAlgorithmComprehensive.count_bits(n)
        elapsed = (time.time() - start) * 1000  # 毫秒
        
        print(f"countBits: n={n}, 耗时={elapsed:.2f} ms")
        print(f"示例结果: [0,1,1,2,1,2,...,{result[n]}]")
    
    @staticmethod
    def run_unit_tests():
        """运行单元测试"""
        print("=== 综合位算法单元测试 ===")
        
        # 测试位1的个数
        assert BitAlgorithmComprehensive.hamming_weight(0b1011) == 3
        
        # 测试2的幂
        assert BitAlgorithmComprehensive.is_power_of_two(16) == True
        assert BitAlgorithmComprehensive.is_power_of_two(15) == False
        
        # 测试只出现一次的数字
        nums = [2, 2, 1]
        assert BitAlgorithmComprehensive.single_number(nums) == 1
        
        print("所有单元测试通过!")
    
    @staticmethod
    def complexity_analysis():
        """复杂度分析"""
        print("\n=== 复杂度分析 ===")
        
        algorithms = {
            "hamming_weight": ("O(k)", "O(1)"),
            "is_power_of_two": ("O(1)", "O(1)"),
            "is_power_of_four": ("O(1)", "O(1)"),
            "missing_number": ("O(n)", "O(1)"),
            "single_number": ("O(n)", "O(1)"),
            "single_number_ii": ("O(n)", "O(1)"),
            "single_number_iii": ("O(n)", "O(1)"),
            "reverse_bits": ("O(1)", "O(1)"),
            "count_bits": ("O(n)", "O(n)"),
            "range_bitwise_and": ("O(1)", "O(1)")
        }
        
        for name, (time_complexity, space_complexity) in algorithms.items():
            print(f"{name}: 时间复杂度={time_complexity}, 空间复杂度={space_complexity}")


def main():
    """主函数"""
    print("综合位算法实现")
    print("包含LeetCode多个综合位算法相关题目的解决方案")
    print("=" * 50)
    
    # 运行单元测试
    PerformanceTester.run_unit_tests()
    
    # 运行性能测试
    PerformanceTester.test_hamming_weight()
    PerformanceTester.test_single_number()
    PerformanceTester.test_count_bits()
    
    # 复杂度分析
    PerformanceTester.complexity_analysis()
    
    # 示例使用
    print("\n=== 示例使用 ===")
    
    # 位1的个数示例
    num = 0b1011
    print(f"数字{num}的1的个数: {BitAlgorithmComprehensive.hamming_weight(num)}")
    
    # 2的幂示例
    print(f"16是2的幂: {'是' if BitAlgorithmComprehensive.is_power_of_two(16) else '否'}")
    
    # 只出现一次的数字示例
    nums = [4, 1, 2, 1, 2]
    print(f"只出现一次的数字: {BitAlgorithmComprehensive.single_number(nums)}")
    
    # 比特位计数示例
    bits = BitAlgorithmComprehensive.count_bits(5)
    print(f"比特位计数(0-5): {' '.join(map(str, bits))}")
    
    # 数字范围按位与示例
    m, n = 5, 7
    print(f"数字范围按位与[{m}, {n}]: {BitAlgorithmComprehensive.range_bitwise_and(m, n)}")


if __name__ == "__main__":
    main()