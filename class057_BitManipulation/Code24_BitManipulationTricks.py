import sys
from typing import List

class BitManipulationTricks:
    """
    位运算技巧大全
    测试链接：综合题目，展示各种位运算技巧的实际应用
    
    题目描述：
    本文件包含各种实用的位运算技巧，涵盖基础操作、高级技巧和实际应用场景。
    
    解题思路：
    位运算技巧的核心是利用二进制表示的特性，通过位操作实现高效的计算和数据处理。
    
    时间复杂度：各种技巧的时间复杂度不同，但通常为O(1)或O(log n)
    空间复杂度：O(1) - 只使用常数个变量
    """
    
    # ==================== 基础位操作技巧 ====================
    
    @staticmethod
    def is_power_of_two(n: int) -> bool:
        """
        判断一个数是否是2的幂
        原理：2的幂在二进制中只有一个1
        """
        return n > 0 and (n & (n - 1)) == 0
    
    @staticmethod
    def is_power_of_four(n: int) -> bool:
        """
        判断一个数是否是4的幂
        原理：4的幂在二进制中只有一个1，且1出现在奇数位
        """
        return n > 0 and (n & (n - 1)) == 0 and (n & 0x55555555) != 0
    
    @staticmethod
    def count_ones(n: int) -> int:
        """
        计算一个数的二进制表示中1的个数（Brian Kernighan算法）
        """
        count = 0
        while n != 0:
            n &= (n - 1)  # 清除最右边的1
            count += 1
        return count
    
    @staticmethod
    def hamming_distance(x: int, y: int) -> int:
        """
        计算两个数的汉明距离（不同位的个数）
        """
        return BitManipulationTricks.count_ones(x ^ y)
    
    @staticmethod
    def reverse_bits(n: int) -> int:
        """
        反转一个整数的二进制位（32位）
        """
        result = 0
        for i in range(32):
            result <<= 1
            result |= (n & 1)
            n >>= 1
        return result
    
    # ==================== 高级位操作技巧 ====================
    
    @staticmethod
    def swap(a: int, b: int) -> tuple[int, int]:
        """
        不使用临时变量交换两个数
        """
        print(f"交换前: a = {a}, b = {b}")
        a = a ^ b
        b = a ^ b
        a = a ^ b
        print(f"交换后: a = {a}, b = {b}")
        return a, b
    
    @staticmethod
    def single_number(nums: List[int]) -> int:
        """
        找到只出现一次的数字（其他数字都出现两次）
        """
        result = 0
        for num in nums:
            result ^= num
        return result
    
    @staticmethod
    def single_number_ii(nums: List[int]) -> int:
        """
        找到只出现一次的数字（其他数字都出现三次）
        """
        ones, twos = 0, 0
        for num in nums:
            ones = (ones ^ num) & ~twos
            twos = (twos ^ num) & ~ones
        return ones
    
    @staticmethod
    def missing_number(nums: List[int]) -> int:
        """
        找到数组中缺失的数字（0到n中缺失一个）
        """
        n = len(nums)
        result = n  # 因为0到n有n+1个数，但数组只有n个
        for i in range(n):
            result ^= i ^ nums[i]
        return result
    
    # ==================== 位掩码技巧 ====================
    
    @staticmethod
    def subsets(nums: List[int]) -> List[List[int]]:
        """
        使用位掩码表示集合（子集生成）
        """
        result = []
        n = len(nums)
        total = 1 << n
        
        for mask in range(total):
            subset = []
            for i in range(n):
                if mask & (1 << i):
                    subset.append(nums[i])
            result.append(subset)
        return result
    
    @staticmethod
    def has_bit_pattern(num: int, pattern: int) -> bool:
        """
        判断一个数是否包含特定的位模式
        """
        return (num & pattern) == pattern
    
    @staticmethod
    def set_bit(num: int, pos: int) -> int:
        """
        设置特定位为1
        """
        return num | (1 << pos)
    
    @staticmethod
    def clear_bit(num: int, pos: int) -> int:
        """
        清除特定位（设为0）
        """
        return num & ~(1 << pos)
    
    @staticmethod
    def toggle_bit(num: int, pos: int) -> int:
        """
        切换特定位（0变1，1变0）
        """
        return num ^ (1 << pos)
    
    @staticmethod
    def check_bit(num: int, pos: int) -> bool:
        """
        检查特定位是否为1
        """
        return (num & (1 << pos)) != 0
    
    # ==================== 位运算在算法中的应用 ====================
    
    @staticmethod
    def add(a: int, b: int) -> int:
        """
        使用位运算实现加法
        """
        # 由于Python整数没有固定位数，需要限制在32位范围内
        MASK = 0xFFFFFFFF
        MAX_INT = 0x7FFFFFFF
        
        a = a & MASK
        b = b & MASK
        
        while b != 0:
            carry = (a & b) << 1
            a = a ^ b
            b = carry & MASK
        
        if a > MAX_INT:
            a = ~(a ^ MASK)
        
        return a
    
    @staticmethod
    def subtract(a: int, b: int) -> int:
        """
        使用位运算实现减法
        """
        return BitManipulationTricks.add(a, BitManipulationTricks.add(~b, 1))
    
    @staticmethod
    def is_odd(n: int) -> bool:
        """
        快速判断奇偶性
        """
        return (n & 1) == 1
    
    @staticmethod
    def abs_val(n: int) -> int:
        """
        快速计算绝对值（32位整数）
        """
        mask = n >> 31
        return (n + mask) ^ mask
    
    @staticmethod
    def power_of_two(n: int) -> int:
        """
        快速计算2的n次方
        """
        return 1 << n
    
    @staticmethod
    def is_multiple_of_power_of_two(n: int, power: int) -> bool:
        """
        快速判断是否是2的幂的倍数
        """
        return (n & ((1 << power) - 1)) == 0
    
    # ==================== 位运算在优化中的应用 ====================
    
    @staticmethod
    def log2(n: int) -> int:
        """
        快速计算log2（整数部分）
        """
        if n <= 0:
            return -1
        return n.bit_length() - 1
    
    @staticmethod
    def next_power_of_two(n: int) -> int:
        """
        快速计算下一个2的幂（大于等于n的最小2的幂）
        """
        if n <= 0:
            return 1
        n -= 1
        n |= n >> 1
        n |= n >> 2
        n |= n >> 4
        n |= n >> 8
        n |= n >> 16
        return n + 1
    
    @staticmethod
    def prev_power_of_two(n: int) -> int:
        """
        快速计算前一个2的幂（小于等于n的最大2的幂）
        """
        if n <= 0:
            return 0
        n |= n >> 1
        n |= n >> 2
        n |= n >> 4
        n |= n >> 8
        n |= n >> 16
        return n - (n >> 1)

class BloomFilter:
    """
    使用位集实现布隆过滤器（简化版）
    """
    
    def __init__(self, size: int):
        self.size = size
        self.bitmap = [0] * ((size + 31) // 32)
    
    def add(self, value: int):
        """添加元素到布隆过滤器"""
        hash1 = self._hash1(value)
        hash2 = self._hash2(value)
        hash3 = self._hash3(value)
        
        self._set_bit(hash1 % self.size)
        self._set_bit(hash2 % self.size)
        self._set_bit(hash3 % self.size)
    
    def contains(self, value: int) -> bool:
        """检查元素是否可能在布隆过滤器中"""
        hash1 = self._hash1(value)
        hash2 = self._hash2(value)
        hash3 = self._hash3(value)
        
        return (self._check_bit(hash1 % self.size) and 
                self._check_bit(hash2 % self.size) and 
                self._check_bit(hash3 % self.size))
    
    def _set_bit(self, pos: int):
        """设置特定位"""
        index = pos // 32
        bit = pos % 32
        self.bitmap[index] |= (1 << bit)
    
    def _check_bit(self, pos: int) -> bool:
        """检查特定位"""
        index = pos // 32
        bit = pos % 32
        return (self.bitmap[index] & (1 << bit)) != 0
    
    def _hash1(self, value: int) -> int:
        """第一个哈希函数"""
        return value * 31
    
    def _hash2(self, value: int) -> int:
        """第二个哈希函数"""
        return value * 17 + 12345
    
    def _hash3(self, value: int) -> int:
        """第三个哈希函数"""
        return value * 13 + 67890

# ==================== 测试代码 ====================

def main():
    print("=== 基础位操作测试 ===")
    print(f"8是2的幂: {BitManipulationTricks.is_power_of_two(8)}")  # True
    print(f"16是4的幂: {BitManipulationTricks.is_power_of_four(16)}")  # True
    print(f"5的二进制中1的个数: {BitManipulationTricks.count_ones(5)}")  # 2
    print(f"1和4的汉明距离: {BitManipulationTricks.hamming_distance(1, 4)}")  # 2
    
    print("\n=== 高级位操作测试 ===")
    a, b = 5, 3
    a, b = BitManipulationTricks.swap(a, b)
    nums1 = [2, 2, 1]
    print(f"只出现一次的数字: {BitManipulationTricks.single_number(nums1)}")  # 1
    nums2 = [0, 1, 3]
    print(f"缺失的数字: {BitManipulationTricks.missing_number(nums2)}")  # 2
    
    print("\n=== 位掩码测试 ===")
    nums3 = [1, 2, 3]
    print(f"子集数量: {len(BitManipulationTricks.subsets(nums3))}")  # 8
    print(f"5包含模式101: {BitManipulationTricks.has_bit_pattern(5, 5)}")  # True
    
    print("\n=== 算法应用测试 ===")
    print(f"5 + 3 = {BitManipulationTricks.add(5, 3)}")  # 8
    print(f"5 - 3 = {BitManipulationTricks.subtract(5, 3)}")  # 2
    print(f"5是奇数: {BitManipulationTricks.is_odd(5)}")  # True
    print(f"-5的绝对值: {BitManipulationTricks.abs_val(-5)}")  # 5
    
    print("\n=== 优化技巧测试 ===")
    print(f"log2(8) = {BitManipulationTricks.log2(8)}")  # 3
    print(f"15的下一个2的幂: {BitManipulationTricks.next_power_of_two(15)}")  # 16
    print(f"15的前一个2的幂: {BitManipulationTricks.prev_power_of_two(15)}")  # 8
    
    print("\n=== 布隆过滤器测试 ===")
    filter = BloomFilter(100)
    filter.add(42)
    filter.add(123)
    print(f"过滤器包含42: {filter.contains(42)}")  # True
    print(f"过滤器包含456: {filter.contains(456)}")  # False（可能误判）
    
    print("\n=== 算法原理说明 ===")
    print("位运算技巧的核心是利用二进制表示的特性：")
    print("1. 与运算(&): 用于掩码操作和清除位")
    print("2. 或运算(|): 用于设置位")
    print("3. 异或运算(^): 用于切换位和交换数值")
    print("4. 取反运算(~): 用于求补码")
    print("5. 移位运算(<<, >>): 用于快速乘除2的幂")

if __name__ == "__main__":
    main()