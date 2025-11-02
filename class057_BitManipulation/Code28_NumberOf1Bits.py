"""
位1的个数
测试链接：https://leetcode.cn/problems/number-of-1-bits/

题目描述：
编写一个函数，输入是一个无符号整数（以二进制串的形式），返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。

解题思路：
1. 逐位检查：检查每一位是否为1
2. 快速方法：使用 n & (n-1) 技巧快速消除最低位的1
3. 查表法：使用预计算的表来快速计算
4. 分治法：使用分治思想并行计算

时间复杂度：O(1) - 最多32次操作
空间复杂度：O(1) - 只使用常数个变量
"""

class Code28_NumberOf1Bits:
    """
    位1的个数解决方案
    """
    
    @staticmethod
    def hamming_weight1(n: int) -> int:
        """
        方法1：逐位检查
        时间复杂度：O(k) - k是1的个数，最坏情况O(32)
        空间复杂度：O(1)
        
        Args:
            n: 32位无符号整数
            
        Returns:
            1的个数
        """
        # Python中整数可能超过32位，需要限制为32位
        n = n & 0xFFFFFFFF
        count = 0
        
        for i in range(32):
            # 检查最低位是否为1
            if (n & 1) == 1:
                count += 1
            # 右移处理下一位
            n = n >> 1
        
        return count
    
    @staticmethod
    def hamming_weight2(n: int) -> int:
        """
        方法2：快速方法（最优解）
        使用 n & (n-1) 技巧快速消除最低位的1
        时间复杂度：O(k) - k是1的个数
        空间复杂度：O(1)
        
        Args:
            n: 32位无符号整数
            
        Returns:
            1的个数
        """
        # Python中整数可能超过32位，需要限制为32位
        n = n & 0xFFFFFFFF
        count = 0
        
        while n != 0:
            # 每次操作消除最低位的1
            n = n & (n - 1)
            count += 1
        
        return count
    
    @staticmethod
    def hamming_weight3(n: int) -> int:
        """
        方法3：查表法（适合多次调用）
        时间复杂度：O(1) - 固定4次查表操作
        空间复杂度：O(256) - 预计算表
        
        Args:
            n: 32位无符号整数
            
        Returns:
            1的个数
        """
        # 预计算0-255的1的个数
        if not hasattr(Code28_NumberOf1Bits, '_bit_count_table'):
            Code28_NumberOf1Bits._bit_count_table = [
                bin(i).count('1') for i in range(256)
            ]
        
        n = n & 0xFFFFFFFF
        
        # 将32位分成4个8位字节
        return (Code28_NumberOf1Bits._bit_count_table[n & 0xFF] +
                Code28_NumberOf1Bits._bit_count_table[(n >> 8) & 0xFF] +
                Code28_NumberOf1Bits._bit_count_table[(n >> 16) & 0xFF] +
                Code28_NumberOf1Bits._bit_count_table[(n >> 24) & 0xFF])
    
    @staticmethod
    def hamming_weight4(n: int) -> int:
        """
        方法4：分治法（并行计算）
        时间复杂度：O(log32) = O(1)
        空间复杂度：O(1)
        
        Args:
            n: 32位无符号整数
            
        Returns:
            1的个数
        """
        n = n & 0xFFFFFFFF
        
        # 分治思想：先计算每2位的1的个数，再计算每4位，依此类推
        n = (n & 0x55555555) + ((n >> 1) & 0x55555555)  # 每2位
        n = (n & 0x33333333) + ((n >> 2) & 0x33333333)  # 每4位
        n = (n & 0x0F0F0F0F) + ((n >> 4) & 0x0F0F0F0F)  # 每8位
        n = (n & 0x00FF00FF) + ((n >> 8) & 0x00FF00FF)  # 每16位
        n = (n & 0x0000FFFF) + ((n >> 16) & 0x0000FFFF) # 每32位
        return n
    
    @staticmethod
    def hamming_weight5(n: int) -> int:
        """
        方法5：Python内置方法
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        Args:
            n: 32位无符号整数
            
        Returns:
            1的个数
        """
        n = n & 0xFFFFFFFF
        return bin(n).count('1')
    
    @staticmethod
    def test():
        """测试方法"""
        # 测试用例1：正常情况
        n1 = 11  # 二进制: 1011
        result1 = Code28_NumberOf1Bits.hamming_weight1(n1)
        result2 = Code28_NumberOf1Bits.hamming_weight2(n1)
        result3 = Code28_NumberOf1Bits.hamming_weight3(n1)
        result4 = Code28_NumberOf1Bits.hamming_weight4(n1)
        result5 = Code28_NumberOf1Bits.hamming_weight5(n1)
        print(f"测试用例1 - 输入: {n1} (二进制: 1011)")
        print(f"方法1结果: {result1} (预期: 3)")
        print(f"方法2结果: {result2} (预期: 3)")
        print(f"方法3结果: {result3} (预期: 3)")
        print(f"方法4结果: {result4} (预期: 3)")
        print(f"方法5结果: {result5} (预期: 3)")
        
        # 测试用例2：边界情况（全0）
        n2 = 0
        result6 = Code28_NumberOf1Bits.hamming_weight1(n2)
        print(f"测试用例2 - 输入: {n2}")
        print(f"方法1结果: {result6} (预期: 0)")
        
        # 测试用例3：边界情况（全1）
        n3 = 0xFFFFFFFF  # 二进制全1
        result7 = Code28_NumberOf1Bits.hamming_weight1(n3)
        print(f"测试用例3 - 输入: {n3}")
        print(f"方法1结果: {result7} (预期: 32)")
        
        # 复杂度分析
        print("\n=== 复杂度分析 ===")
        print("方法1 - 逐位检查:")
        print("  时间复杂度: O(32) - 固定32次操作")
        print("  空间复杂度: O(1)")
        
        print("方法2 - 快速方法:")
        print("  时间复杂度: O(k) - k是1的个数")
        print("  空间复杂度: O(1)")
        
        print("方法3 - 查表法:")
        print("  时间复杂度: O(1) - 固定4次查表操作")
        print("  空间复杂度: O(256)")
        
        print("方法4 - 分治法:")
        print("  时间复杂度: O(log32) = O(1)")
        print("  空间复杂度: O(1)")
        
        print("方法5 - Python内置:")
        print("  时间复杂度: O(1)")
        print("  空间复杂度: O(1)")
        
        # 工程化考量
        print("\n=== 工程化考量 ===")
        print("1. 方法选择：")
        print("   - 实际工程：方法5（内置方法）最优")
        print("   - 面试场景：方法2（快速方法）最优")
        print("   - 多次调用：方法3（查表法）最优")
        print("2. 边界处理：Python整数可能超过32位，需要限制")
        print("3. 性能优化：根据1的个数选择最优方法")
        print("4. 可读性：添加详细注释说明算法原理")
        
        # Python特性考量
        print("\n=== Python特性考量 ===")
        print("1. 整数表示：Python整数是动态大小的，需要手动限制为32位")
        print("2. 内置函数：bin(n).count('1') 是最简洁的方法")
        print("3. 类属性：使用类属性存储预计算表")
        print("4. 位操作：使用& 0xFFFFFFFF确保32位操作")
        
        # 算法技巧总结
        print("\n=== 算法技巧总结 ===")
        print("1. n & (n-1) 技巧：快速消除最低位的1")
        print("2. 分治思想：并行计算提高效率")
        print("3. 查表优化：空间换时间策略")
        print("4. 内置函数：利用语言内置功能")
        print("5. 边界处理：确保正确处理32位整数")

if __name__ == "__main__":
    Code28_NumberOf1Bits.test()