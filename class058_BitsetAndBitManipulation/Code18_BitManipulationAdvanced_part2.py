"""
高级位操作算法实现 - 第二部分
包含LeetCode多个高级位操作相关题目的解决方案

题目列表:
6. LeetCode 393 - UTF-8 编码验证
7. LeetCode 397 - 整数替换
8. LeetCode 401 - 二进制手表
9. LeetCode 421 - 数组中两个数的最大异或值
10. LeetCode 461 - 汉明距离
"""

import time
from typing import List, Set, Tuple
import sys
from functools import lru_cache

class BitManipulationAdvanced:
    """高级位操作算法类"""
    
    @staticmethod
    def valid_utf8(data: List[int]) -> bool:
        """
        LeetCode 393 - UTF-8 编码验证
        给定一个表示数据的整数数组 data ，返回它是否为有效的 UTF-8 编码。
        
        方法: 位运算检查编码规则
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        UTF-8编码规则:
        1. 1字节字符: 0xxxxxxx
        2. 2字节字符: 110xxxxx 10xxxxxx
        3. 3字节字符: 1110xxxx 10xxxxxx 10xxxxxx
        4. 4字节字符: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
        """
        count = 0  # 后续字节数量
        
        for byte in data:
            if count == 0:
                # 检查首字节
                if (byte >> 5) == 0b110:        # 2字节字符
                    count = 1
                elif (byte >> 4) == 0b1110:     # 3字节字符
                    count = 2
                elif (byte >> 3) == 0b11110:    # 4字节字符
                    count = 3
                elif (byte >> 7) != 0:          # 无效首字节
                    return False
            else:
                # 检查后续字节
                if (byte >> 6) != 0b10:
                    return False
                count -= 1
        
        return count == 0  # 所有多字节字符都完整
    
    @staticmethod
    def integer_replacement(n: int) -> int:
        """
        LeetCode 397 - 整数替换
        给定一个正整数 n ，你可以做如下操作：
        1. 如果 n 是偶数，则用 n / 2替换 n
        2. 如果 n 是奇数，则可以用 n + 1或n - 1替换 n
        返回 n 变为 1 所需的最小替换次数。
        
        方法: 贪心 + 位运算
        时间复杂度: O(log n)
        空间复杂度: O(log n) - 递归深度
        """
        @lru_cache(None)
        def helper(n: int) -> int:
            if n == 1:
                return 0
            if n % 2 == 0:
                return 1 + helper(n // 2)
            else:
                return 1 + min(helper(n + 1), helper(n - 1))
        
        return helper(n)
    
    @staticmethod
    def read_binary_watch(turned_on: int) -> List[str]:
        """
        LeetCode 401 - 二进制手表
        二进制手表顶部有 4 个 LED 代表 小时（0-11），底部的 6 个 LED 代表 分钟（0-59）。
        给定一个非负整数 turnedOn ，表示当前亮着的 LED 的数量，返回二进制手表可能表示的所有时间。
        
        方法: 枚举所有可能的时间组合
        时间复杂度: O(1) - 固定12*60种可能
        空间复杂度: O(1)
        """
        result = []
        
        for h in range(12):
            for m in range(60):
                if bin(h).count('1') + bin(m).count('1') == turned_on:
                    result.append(f"{h}:{m:02d}")
        
        return result
    
    @staticmethod
    def find_maximum_xor(nums: List[int]) -> int:
        """
        LeetCode 421 - 数组中两个数的最大异或值
        给定一个非空数组，数组中元素为 a0, a1, a2, … , an-1，其中 0 ≤ ai < 2^31。
        找到 ai 和 aj 最大的异或 (XOR) 运算结果，其中 0 ≤ i, j < n。
        
        方法: 前缀树 + 贪心
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        max_xor = 0
        mask = 0
        
        for i in range(31, -1, -1):
            mask |= (1 << i)
            prefixes = set()
            
            # 提取前缀
            for num in nums:
                prefixes.add(num & mask)
            
            # 尝试设置当前位为1
            candidate = max_xor | (1 << i)
            for prefix in prefixes:
                if (candidate ^ prefix) in prefixes:
                    max_xor = candidate
                    break
        
        return max_xor
    
    @staticmethod
    def hamming_distance(x: int, y: int) -> int:
        """
        LeetCode 461 - 汉明距离
        两个整数之间的汉明距离指的是这两个数字对应二进制位不同的位置的数目。
        
        方法: 异或 + 统计1的个数
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        xor_val = x ^ y
        return bin(xor_val).count('1')


class PerformanceTester:
    """性能测试工具类"""
    
    @staticmethod
    def test_subsets():
        """测试子集算法性能"""
        print("=== 子集算法性能测试 ===")
        
        # 生成测试数据
        nums = list(range(20))
        
        start = time.time()
        result = BitManipulationAdvanced.subsets(nums)
        elapsed = (time.time() - start) * 1000  # 毫秒
        
        print(f"subsets: 子集数量={len(result)}, 耗时={elapsed:.2f} ms")
    
    @staticmethod
    def test_maximum_xor():
        """测试最大异或值性能"""
        print("\n=== 最大异或值性能测试 ===")
        
        # 生成测试数据
        import random
        nums = [random.randint(0, 1000000) for _ in range(10000)]
        
        start = time.time()
        result = BitManipulationAdvanced.find_maximum_xor(nums)
        elapsed = (time.time() - start) * 1000  # 毫秒
        
        print(f"find_maximum_xor: 结果={result}, 耗时={elapsed:.2f} ms")
    
    @staticmethod
    def run_unit_tests():
        """运行单元测试"""
        print("=== 高级位操作算法单元测试 ===")
        
        # 测试子集
        nums = [1, 2, 3]
        subsets = BitManipulationAdvanced.subsets(nums)
        assert len(subsets) == 8  # 2^3 = 8个子集
        
        # 测试汉明距离
        assert BitManipulationAdvanced.hamming_distance(1, 4) == 2
        
        # 测试UTF-8验证
        utf8_data = [197, 130, 1]
        assert BitManipulationAdvanced.valid_utf8(utf8_data) == True
        
        print("所有单元测试通过!")
    
    @staticmethod
    def complexity_analysis():
        """复杂度分析"""
        print("\n=== 复杂度分析 ===")
        
        algorithms = {
            "subsets": ("O(n * 2^n)", "O(2^n)"),
            "subsets_with_dup": ("O(n * 2^n)", "O(2^n)"),
            "find_repeated_dna_sequences": ("O(n)", "O(n)"),
            "reverse_bits": ("O(1)", "O(1)"),
            "max_product": ("O(n^2 + n*L)", "O(n)"),
            "valid_utf8": ("O(n)", "O(1)"),
            "integer_replacement": ("O(log n)", "O(log n)"),
            "find_maximum_xor": ("O(n)", "O(n)")
        }
        
        for name, (time_complexity, space_complexity) in algorithms.items():
            print(f"{name}: 时间复杂度={time_complexity}, 空间复杂度={space_complexity}")


def main():
    """主函数"""
    print("高级位操作算法实现")
    print("包含LeetCode多个高级位操作相关题目的解决方案")
    print("=" * 50)
    
    # 运行单元测试
    PerformanceTester.run_unit_tests()
    
    # 运行性能测试
    PerformanceTester.test_subsets()
    PerformanceTester.test_maximum_xor()
    
    # 复杂度分析
    PerformanceTester.complexity_analysis()
    
    # 示例使用
    print("\n=== 示例使用 ===")
    
    # 子集示例
    nums = [1, 2, 3]
    print(f"数组: {nums}")
    
    subsets = BitManipulationAdvanced.subsets(nums)
    print(f"子集数量: {len(subsets)}")
    print("前3个子集:")
    for i in range(min(3, len(subsets))):
        print(f"  {subsets[i]}")
    
    # 汉明距离示例
    x, y = 1, 4
    print(f"汉明距离({x}, {y}) = {BitManipulationAdvanced.hamming_distance(x, y)}")
    
    # 二进制表示示例
    n = 43261596  # 00000010100101000001111010011100
    print(f"原始数字: {n}")
    print(f"反转后: {BitManipulationAdvanced.reverse_bits(n)}")


if __name__ == "__main__":
    main()