"""
比特位计数（多种解法）
测试链接：https://leetcode.cn/problems/counting-bits/

题目描述：
给定一个非负整数 n，计算从 0 到 n 的每个整数的二进制表示中 1 的个数，并返回一个数组。

解题思路：
1. 直接计算法：对每个数使用位运算计算1的个数
2. 动态规划法：利用已知结果推导新结果
3. 位运算优化：利用位运算特性快速计算
4. 查表法：预计算小范围结果，大范围复用

时间复杂度分析：
- 直接计算法：O(nk)，k为平均位数
- 动态规划法：O(n)
- 位运算优化：O(n)
- 查表法：O(n)

空间复杂度分析：
- 直接计算法：O(1) 或 O(n) 存储结果
- 动态规划法：O(n)
- 位运算优化：O(n)
- 查表法：O(n)
"""

from typing import List
import time

class Solution:
    def countBits1(self, n: int) -> List[int]:
        """
        方法1：直接计算法（朴素解法）
        对每个数使用位运算计算1的个数
        时间复杂度：O(nk)，k为平均位数
        空间复杂度：O(n)
        """
        result = [0] * (n + 1)
        
        for i in range(n + 1):
            result[i] = self.count_ones(i)
        
        return result
    
    def count_ones(self, num: int) -> int:
        """
        计算一个数的二进制表示中1的个数
        时间复杂度：O(k)，k为数的位数
        空间复杂度：O(1)
        """
        count = 0
        while num != 0:
            count += num & 1  # 检查最低位是否为1
            num >>= 1        # 右移
        return count
    
    def countBits2(self, n: int) -> List[int]:
        """
        方法2：Brian Kernighan算法
        利用 num & (num-1) 快速消除最低位的1
        时间复杂度：O(nk)，但k通常较小
        空间复杂度：O(n)
        """
        result = [0] * (n + 1)
        
        for i in range(n + 1):
            result[i] = self.count_ones_brian_kernighan(i)
        
        return result
    
    def count_ones_brian_kernighan(self, num: int) -> int:
        """
        Brian Kernighan算法计算1的个数
        时间复杂度：O(k)，k为1的个数（比位数更优）
        空间复杂度：O(1)
        """
        count = 0
        while num != 0:
            num &= num - 1  # 消除最低位的1
            count += 1
        return count
    
    def countBits3(self, n: int) -> List[int]:
        """
        方法3：动态规划法（最高有效位）
        利用已知结果推导新结果：result[i] = result[i - highBit] + 1
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        result = [0] * (n + 1)
        high_bit = 0  # 当前最高有效位
        
        for i in range(1, n + 1):
            # 检查是否是2的幂（最高有效位发生变化）
            if i & (i - 1) == 0:
                high_bit = i
            result[i] = result[i - high_bit] + 1
        
        return result
    
    def countBits4(self, n: int) -> List[int]:
        """
        方法4：动态规划法（最低有效位）
        利用 result[i] = result[i >> 1] + (i & 1)
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        result = [0] * (n + 1)
        
        for i in range(1, n + 1):
            result[i] = result[i >> 1] + (i & 1)
        
        return result
    
    def countBits5(self, n: int) -> List[int]:
        """
        方法5：动态规划法（最低设置位）
        利用 result[i] = result[i & (i-1)] + 1
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        result = [0] * (n + 1)
        
        for i in range(1, n + 1):
            result[i] = result[i & (i - 1)] + 1
        
        return result
    
    def countBits6(self, n: int) -> List[int]:
        """
        方法6：查表法（适用于小范围）
        预计算0-255的1的个数，大数复用结果
        时间复杂度：O(n)
        空间复杂度：O(256 + n)
        """
        # 预计算0-255的1的个数
        table = [0] * 256
        for i in range(256):
            table[i] = self.count_ones_brian_kernighan(i)
        
        result = [0] * (n + 1)
        
        for i in range(n + 1):
            # 将32位整数分成4个字节分别查表
            result[i] = (table[i & 0xFF] + 
                         table[(i >> 8) & 0xFF] + 
                         table[(i >> 16) & 0xFF] + 
                         table[(i >> 24) & 0xFF])
        
        return result
    
    def countBits7(self, n: int) -> List[int]:
        """
        方法7：位运算并行计算
        使用位运算技巧并行计算1的个数
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        result = [0] * (n + 1)
        
        for i in range(n + 1):
            result[i] = self.parallel_count_ones(i)
        
        return result
    
    def parallel_count_ones(self, num: int) -> int:
        """
        并行计算1的个数（位运算技巧）
        时间复杂度：O(1) 对于固定位数的整数
        空间复杂度：O(1)
        """
        # 步骤1：每2位统计1的个数
        num = (num & 0x55555555) + ((num >> 1) & 0x55555555)
        # 步骤2：每4位统计1的个数
        num = (num & 0x33333333) + ((num >> 2) & 0x33333333)
        # 步骤3：每8位统计1的个数
        num = (num & 0x0F0F0F0F) + ((num >> 4) & 0x0F0F0F0F)
        # 步骤4：每16位统计1的个数
        num = (num & 0x00FF00FF) + ((num >> 8) & 0x00FF00FF)
        # 步骤5：每32位统计1的个数
        num = (num & 0x0000FFFF) + ((num >> 16) & 0x0000FFFF)
        
        return num

def test_solution():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：n = 2
    n1 = 2
    result1 = solution.countBits3(n1)
    print(f"测试用例1 - n = {n1}")
    print(f"结果: {result1}")
    print("预期: [0, 1, 1]")
    
    # 测试用例2：n = 5
    n2 = 5
    result2 = solution.countBits3(n2)
    print(f"测试用例2 - n = {n2}")
    print(f"结果: {result2}")
    print("预期: [0, 1, 1, 2, 1, 2]")
    
    # 测试用例3：n = 0
    n3 = 0
    result3 = solution.countBits3(n3)
    print(f"测试用例3 - n = {n3}")
    print(f"结果: {result3}")
    print("预期: [0]")
    
    # 性能测试
    n4 = 1000000
    start_time = time.time()
    result4 = solution.countBits3(n4)
    end_time = time.time()
    print(f"性能测试 - n = {n4}")
    print(f"耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # 所有方法结果对比
    print("\n=== 所有方法结果对比 ===")
    test_n = 10
    print(f"测试 n = {test_n}")
    print(f"方法1 (直接计算): {solution.countBits1(test_n)}")
    print(f"方法2 (Brian Kernighan): {solution.countBits2(test_n)}")
    print(f"方法3 (动态规划-最高位): {solution.countBits3(test_n)}")
    print(f"方法4 (动态规划-最低位): {solution.countBits4(test_n)}")
    print(f"方法5 (动态规划-最低设置位): {solution.countBits5(test_n)}")
    print(f"方法6 (查表法): {solution.countBits6(test_n)}")
    print(f"方法7 (并行计算): {solution.countBits7(test_n)}")
    
    # 复杂度分析
    print("\n=== 复杂度分析 ===")
    print("方法1 - 直接计算法:")
    print("  时间复杂度: O(nk)，k为平均位数")
    print("  空间复杂度: O(n)")
    
    print("方法2 - Brian Kernighan算法:")
    print("  时间复杂度: O(nk)，k为1的个数")
    print("  空间复杂度: O(n)")
    
    print("方法3 - 动态规划(最高位):")
    print("  时间复杂度: O(n)")
    print("  空间复杂度: O(n)")
    
    print("方法4 - 动态规划(最低位):")
    print("  时间复杂度: O(n)")
    print("  空间复杂度: O(n)")
    
    print("方法5 - 动态规划(最低设置位):")
    print("  时间复杂度: O(n)")
    print("  空间复杂度: O(n)")
    
    print("方法6 - 查表法:")
    print("  时间复杂度: O(n)")
    print("  空间复杂度: O(256 + n)")
    
    print("方法7 - 并行计算:")
    print("  时间复杂度: O(n)")
    print("  空间复杂度: O(n)")
    
    # 工程化考量
    print("\n=== 工程化考量 ===")
    print("1. 算法选择：方法3/4/5 (动态规划) 最优")
    print("2. 性能优化：避免O(nk)的朴素解法")
    print("3. 空间优化：动态规划法空间复杂度最优")
    print("4. 实际应用：适合处理大规模数据")
    
    # 算法技巧总结
    print("\n=== 算法技巧总结 ===")
    print("1. 动态规划思想：利用已知结果推导新结果")
    print("2. 位运算技巧：Brian Kernighan算法消除最低位1")
    print("3. 并行计算：使用位运算并行统计1的个数")
    print("4. 查表优化：预计算小范围结果，大范围复用")
    
    # Python特殊处理说明
    print("\n=== Python特殊处理说明 ===")
    print("Python整数是动态大小的，位运算仍然有效：")
    print("  使用 >> 进行右移操作")
    print("  使用 & 进行位与操作")
    print("  注意Python的整数没有固定位数限制")

if __name__ == "__main__":
    test_solution()