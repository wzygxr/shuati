"""
位运算算法实现
包含LeetCode多个位运算相关题目的解决方案

题目列表:
1. LeetCode 136 - 只出现一次的数字
2. LeetCode 137 - 只出现一次的数字 II
3. LeetCode 260 - 只出现一次的数字 III
4. LeetCode 191 - 位1的个数
5. LeetCode 231 - 2的幂
6. LeetCode 342 - 4的幂
7. LeetCode 371 - 两整数之和
8. LeetCode 201 - 数字范围按位与
9. LeetCode 338 - 比特位计数
10. LeetCode 405 - 数字转换为十六进制数

时间复杂度分析:
- 位运算操作: O(1) 或 O(n)
- 空间复杂度: O(1) 或 O(n)

工程化考量:
1. 位运算技巧: 使用位运算优化算法
2. 边界处理: 处理负数、零等边界情况
3. 性能优化: 利用位运算的常数时间优势
4. 可读性: 添加详细注释说明位运算原理
"""

import time
from typing import List
import sys

class BitwiseOperations:
    """位运算算法类"""
    
    @staticmethod
    def single_number(nums: List[int]) -> int:
        """
        LeetCode 136 - 只出现一次的数字
        给定一个非空整数数组，除了某个元素只出现一次外，其余每个元素均出现两次。找出那个只出现一次的元素。
        
        方法: 异或运算
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        原理: a ^ a = 0, a ^ 0 = a
        所有出现两次的数字异或后为0，最后剩下的就是只出现一次的数字
        """
        result = 0
        for num in nums:
            result ^= num
        return result
    
    @staticmethod
    def single_number_ii(nums: List[int]) -> int:
        """
        LeetCode 137 - 只出现一次的数字 II
        给定一个非空整数数组，除了某个元素只出现一次外，其余每个元素均出现三次。找出那个只出现一次的元素。
        
        方法: 位计数法
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        原理: 统计每个位上1出现的次数，对3取模
        如果某位上1出现的次数不是3的倍数，说明只出现一次的数字在该位为1
        """
        result = 0
        
        # 遍历32位
        for i in range(32):
            count = 0
            
            # 统计第i位为1的数字个数
            for num in nums:
                if (num >> i) & 1:
                    count += 1
            
            # 如果count % 3 != 0，说明只出现一次的数字在该位为1
            if count % 3 != 0:
                result |= (1 << i)
        
        # 处理负数（Python中整数是有符号的）
        if result >= (1 << 31):
            result -= (1 << 32)
        
        return result
    
    @staticmethod
    def single_number_iii(nums: List[int]) -> List[int]:
        """
        LeetCode 260 - 只出现一次的数字 III
        给定一个整数数组，其中恰好有两个元素只出现一次，其余所有元素均出现两次。找出只出现一次的那两个元素。
        
        方法: 分组异或
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        原理:
        1. 先对所有数字异或，得到两个不同数字的异或结果
        2. 找到异或结果中为1的某一位（两个数字在该位不同）
        3. 根据这一位将数组分成两组，分别异或得到两个数字
        """
        # 第一步：计算所有数字的异或
        xor_all = 0
        for num in nums:
            xor_all ^= num
        
        # 第二步：找到为1的最低位
        diff_bit = 1
        while (xor_all & diff_bit) == 0:
            diff_bit <<= 1
        
        # 第三步：根据diff_bit分组异或
        num1, num2 = 0, 0
        for num in nums:
            if num & diff_bit:
                num1 ^= num
            else:
                num2 ^= num
        
        return [num1, num2]
    
    @staticmethod
    def hamming_weight(n: int) -> int:
        """
        LeetCode 191 - 位1的个数
        编写一个函数，输入是一个无符号整数，返回其二进制表达式中数字位数为 '1' 的个数。
        
        方法1: 循环检查二进制位
        方法2: Brian Kernighan算法
        时间复杂度: O(1) - 固定32位
        空间复杂度: O(1)
        """
        # 方法1: 循环检查
        count = 0
        while n:
            count += (n & 1)
            n >>= 1
        return count
        
        # 方法2: Brian Kernighan算法（更高效）
        # count = 0
        # while n:
        #     n &= (n - 1)  # 清除最低位的1
        #     count += 1
        # return count
    
    @staticmethod
    def is_power_of_two(n: int) -> bool:
        """
        LeetCode 231 - 2的幂
        给定一个整数，编写一个函数来判断它是否是 2 的幂次方。
        
        方法: 位运算性质
        时间复杂度: O(1)
        空间复杂度: O(1)
        
        原理: 2的幂的二进制表示中只有一位是1
        n > 0 and (n & (n - 1)) == 0
        """
        return n > 0 and (n & (n - 1)) == 0
    
    @staticmethod
    def is_power_of_four(n: int) -> bool:
        """
        LeetCode 342 - 4的幂
        给定一个整数，编写一个函数来判断它是否是 4 的幂次方。
        
        方法: 位运算 + 数学性质
        时间复杂度: O(1)
        空间复杂度: O(1)
        
        原理:
        1. 必须是2的幂: n > 0 and (n & (n - 1)) == 0
        2. 4的幂的1出现在奇数位: (n & 0x55555555) != 0
        """
        return n > 0 and (n & (n - 1)) == 0 and (n & 0x55555555) != 0
    
    @staticmethod
    def get_sum(a: int, b: int) -> int:
        """
        LeetCode 371 - 两整数之和
        不使用运算符 + 和 - ，计算两整数 a 、b 之和。
        
        方法: 位运算模拟加法
        时间复杂度: O(1) - 最多32次循环
        空间复杂度: O(1)
        
        原理:
        1. 异或运算得到无进位和
        2. 与运算左移一位得到进位
        3. 循环直到进位为0
        """
        # 32位整数处理
        MASK = 0xFFFFFFFF
        MAX_INT = 0x7FFFFFFF
        
        a &= MASK
        b &= MASK
        
        while b != 0:
            carry = ((a & b) << 1) & MASK  # 进位
            a = (a ^ b) & MASK             # 无进位和
            b = carry                      # 进位作为新的b
        
        # 处理负数
        return a if a <= MAX_INT else ~(a ^ MASK)
    
    @staticmethod
    def range_bitwise_and(left: int, right: int) -> int:
        """
        LeetCode 201 - 数字范围按位与
        给定两个整数 left 和 right，返回区间 [left, right] 内所有数字按位与的结果。
        
        方法: 寻找公共前缀
        时间复杂度: O(1) - 最多32次循环
        空间复杂度: O(1)
        
        原理: 区间内所有数字的按位与结果等于它们的公共二进制前缀
        """
        shift = 0
        
        # 找到公共前缀
        while left < right:
            left >>= 1
            right >>= 1
            shift += 1
        
        return left << shift
    
    @staticmethod
    def count_bits(n: int) -> List[int]:
        """
        LeetCode 338 - 比特位计数
        给定一个非负整数 num。对于 0 ≤ i ≤ num 范围内的每个数字 i ，计算其二进制数中的 1 的数目。
        
        方法1: 直接计算每个数字的汉明重量
        方法2: 动态规划 + 最高有效位
        方法3: 动态规划 + 最低设置位
        
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        result = [0] * (n + 1)
        
        # 方法1: 直接计算（简单但较慢）
        # for i in range(n + 1):
        #     result[i] = BitwiseOperations.hamming_weight(i)
        
        # 方法2: 动态规划 + 最高有效位（推荐）
        # result[0] = 0
        # highest_bit = 0
        # for i in range(1, n + 1):
        #     if (i & (i - 1)) == 0:  # 检查是否是2的幂
        #         highest_bit = i
        #     result[i] = result[i - highest_bit] + 1
        
        # 方法3: 动态规划 + 最低设置位（最优）
        result[0] = 0
        for i in range(1, n + 1):
            result[i] = result[i & (i - 1)] + 1
        
        return result
    
    @staticmethod
    def to_hex(num: int) -> str:
        """
        LeetCode 405 - 数字转换为十六进制数
        给定一个整数，编写一个算法将这个数转换为十六进制数。对于负整数，我们通常使用补码运算方法。
        
        方法: 位运算 + 查表
        时间复杂度: O(1) - 最多8次循环
        空间复杂度: O(1)
        """
        if num == 0:
            return "0"
        
        hex_chars = "0123456789abcdef"
        result = []
        # 处理负数补码
        n = num if num >= 0 else (1 << 32) + num
        
        while n > 0:
            digit = n & 0xf  # 取最低4位
            result.append(hex_chars[digit])
            n >>= 4          # 右移4位
        
        return ''.join(reversed(result))
    
    @staticmethod
    def swap_numbers(a: int, b: int) -> tuple[int, int]:
        """
        额外题目: 交换两个数字（不使用临时变量）
        使用异或运算交换两个整数
        """
        a = a ^ b
        b = a ^ b
        a = a ^ b
        return a, b
    
    @staticmethod
    def is_odd(n: int) -> bool:
        """
        额外题目: 判断奇偶性
        使用位运算判断数字的奇偶性
        """
        return (n & 1) == 1
    
    @staticmethod
    def absolute_value(n: int) -> int:
        """
        额外题目: 取绝对值
        使用位运算计算整数的绝对值
        """
        mask = n >> 31           # 对于负数，mask = -1；对于正数，mask = 0
        return (n + mask) ^ mask # 对于负数：n + (-1) = n-1，然后异或-1相当于取反


class PerformanceTester:
    """性能测试工具类"""
    
    @staticmethod
    def test_single_number():
        """测试只出现一次的数字性能"""
        print("=== 只出现一次的数字性能测试 ===")
        
        # 生成测试数据
        nums = [i for i in range(500000)] * 2
        nums.append(1000000)  # 只出现一次的数字
        
        start = time.time()
        result = BitwiseOperations.single_number(nums)
        elapsed = (time.time() - start) * 1e6  # 微秒
        
        print(f"single_number: 结果={result}, 耗时={elapsed:.2f} μs")
    
    @staticmethod
    def test_hamming_weight():
        """测试汉明重量性能"""
        print("\n=== 汉明重量性能测试 ===")
        
        test_num = 0xFFFFFFFF  # 所有位都是1
        
        start = time.time()
        result = BitwiseOperations.hamming_weight(test_num)
        elapsed = (time.time() - start) * 1e6  # 微秒
        
        print(f"hamming_weight: 结果={result}, 耗时={elapsed:.2f} μs")
    
    @staticmethod
    def run_unit_tests():
        """运行单元测试"""
        print("=== 位运算算法单元测试 ===")
        
        # 测试single_number
        nums1 = [2, 2, 1]
        assert BitwiseOperations.single_number(nums1) == 1
        
        nums2 = [4, 1, 2, 1, 2]
        assert BitwiseOperations.single_number(nums2) == 4
        
        # 测试is_power_of_two
        assert BitwiseOperations.is_power_of_two(1) == True
        assert BitwiseOperations.is_power_of_two(16) == True
        assert BitwiseOperations.is_power_of_two(3) == False
        
        # 测试get_sum
        assert BitwiseOperations.get_sum(1, 2) == 3
        assert BitwiseOperations.get_sum(-1, 1) == 0
        
        print("所有单元测试通过!")
    
    @staticmethod
    def complexity_analysis():
        """复杂度分析"""
        print("\n=== 复杂度分析 ===")
        
        algorithms = {
            "single_number": ("O(n)", "O(1)"),
            "single_number_ii": ("O(n)", "O(1)"),
            "single_number_iii": ("O(n)", "O(1)"),
            "hamming_weight": ("O(1)", "O(1)"),
            "is_power_of_two": ("O(1)", "O(1)"),
            "get_sum": ("O(1)", "O(1)"),
            "count_bits": ("O(n)", "O(n)")
        }
        
        for name, (time_complexity, space_complexity) in algorithms.items():
            print(f"{name}: 时间复杂度={time_complexity}, 空间复杂度={space_complexity}")


def main():
    """主函数"""
    print("位运算算法实现")
    print("包含LeetCode多个位运算相关题目的解决方案")
    print("=" * 50)
    
    # 运行单元测试
    PerformanceTester.run_unit_tests()
    
    # 运行性能测试
    PerformanceTester.test_single_number()
    PerformanceTester.test_hamming_weight()
    
    # 复杂度分析
    PerformanceTester.complexity_analysis()
    
    # 示例使用
    print("\n=== 示例使用 ===")
    
    # 只出现一次的数字示例
    nums = [4, 1, 2, 1, 2]
    print(f"数组: {nums}")
    print(f"只出现一次的数字: {BitwiseOperations.single_number(nums)}")
    
    # 2的幂示例
    test_num = 16
    print(f"{test_num} 是2的幂: {BitwiseOperations.is_power_of_two(test_num)}")
    
    # 汉明重量示例
    n = 11  # 二进制: 1011
    print(f"{n} 的汉明重量: {BitwiseOperations.hamming_weight(n)}")
    
    # 数字交换示例
    a, b = 5, 10
    print(f"交换前: a={a}, b={b}")
    a, b = BitwiseOperations.swap_numbers(a, b)
    print(f"交换后: a={a}, b={b}")
    
    # 十六进制转换示例
    num = 255
    print(f"{num} 的十六进制: {BitwiseOperations.to_hex(num)}")


if __name__ == "__main__":
    main()