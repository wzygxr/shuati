"""
颠倒二进制位
测试链接：https://leetcode.cn/problems/reverse-bits/

题目描述：
颠倒给定的 32 位无符号整数的二进制位。

解题思路：
1. 逐位反转：从最低位开始，依次将每一位移动到对应的高位位置
2. 分治反转：使用分治思想，先交换16位块，再交换8位块，依此类推
3. 查表法：对于8位进行预计算，然后组合成32位

时间复杂度：O(1) - 固定32次操作
空间复杂度：O(1) - 只使用常数个变量
"""

class Code27_ReverseBits:
    """
    颠倒二进制位解决方案
    """
    
    @staticmethod
    def reverse_bits1(n: int) -> int:
        """
        方法1：逐位反转
        时间复杂度：O(1) - 固定32次循环
        空间复杂度：O(1)
        
        Args:
            n: 32位无符号整数
            
        Returns:
            反转后的32位无符号整数
        """
        result = 0
        # Python中整数可能超过32位，需要限制为32位
        n = n & 0xFFFFFFFF
        
        for i in range(32):
            # 取最低位
            bit = n & 1
            # 将最低位移动到对应的高位位置
            result = (result << 1) | bit
            # 右移处理下一位
            n = n >> 1
        
        return result
    
    @staticmethod
    def reverse_bits2(n: int) -> int:
        """
        方法2：分治反转（更高效）
        时间复杂度：O(1) - 固定5次操作
        空间复杂度：O(1)
        
        Args:
            n: 32位无符号整数
            
        Returns:
            反转后的32位无符号整数
        """
        # Python中整数可能超过32位，需要限制为32位
        n = n & 0xFFFFFFFF
        
        # 分治思想：先交换16位块，再交换8位块，依此类推
        n = ((n >> 16) | (n << 16)) & 0xFFFFFFFF  # 交换16位块
        n = (((n & 0xFF00FF00) >> 8) | ((n & 0x00FF00FF) << 8)) & 0xFFFFFFFF  # 交换8位块
        n = (((n & 0xF0F0F0F0) >> 4) | ((n & 0x0F0F0F0F) << 4)) & 0xFFFFFFFF  # 交换4位块
        n = (((n & 0xCCCCCCCC) >> 2) | ((n & 0x33333333) << 2)) & 0xFFFFFFFF  # 交换2位块
        n = (((n & 0xAAAAAAAA) >> 1) | ((n & 0x55555555) << 1)) & 0xFFFFFFFF  # 交换1位块
        
        return n
    
    @staticmethod
    def reverse_bits3(n: int) -> int:
        """
        方法3：查表法（最优解，适合多次调用）
        时间复杂度：O(1) - 固定4次查表操作
        空间复杂度：O(256) - 预计算表
        
        Args:
            n: 32位无符号整数
            
        Returns:
            反转后的32位无符号整数
        """
        # 预计算0-255的8位反转结果
        if not hasattr(Code27_ReverseBits, '_reverse_table'):
            Code27_ReverseBits._reverse_table = [
                Code27_ReverseBits._reverse_byte(i) for i in range(256)
            ]
        
        n = n & 0xFFFFFFFF
        
        # 将32位分成4个8位字节，分别反转后重新组合
        return ((Code27_ReverseBits._reverse_table[n & 0xFF] << 24) |
                (Code27_ReverseBits._reverse_table[(n >> 8) & 0xFF] << 16) |
                (Code27_ReverseBits._reverse_table[(n >> 16) & 0xFF] << 8) |
                (Code27_ReverseBits._reverse_table[(n >> 24) & 0xFF])) & 0xFFFFFFFF
    
    @staticmethod
    def _reverse_byte(b: int) -> int:
        """
        反转8位字节
        
        Args:
            b: 8位字节（0-255）
            
        Returns:
            反转后的8位字节
        """
        result = 0
        for i in range(8):
            result = (result << 1) | (b & 1)
            b = b >> 1
        return result
    
    @staticmethod
    def test():
        """测试方法"""
        # 测试用例1：正常情况
        n1 = 43261596  # 二进制: 00000010100101000001111010011100
        result1 = Code27_ReverseBits.reverse_bits1(n1)
        result2 = Code27_ReverseBits.reverse_bits2(n1)
        result3 = Code27_ReverseBits.reverse_bits3(n1)
        print(f"测试用例1 - 输入: {n1}")
        print(f"方法1结果: {result1} (预期: 964176192)")
        print(f"方法2结果: {result2} (预期: 964176192)")
        print(f"方法3结果: {result3} (预期: 964176192)")
        
        # 测试用例2：边界情况（全0）
        n2 = 0
        result4 = Code27_ReverseBits.reverse_bits1(n2)
        print(f"测试用例2 - 输入: {n2}")
        print(f"方法1结果: {result4} (预期: 0)")
        
        # 测试用例3：边界情况（全1）
        n3 = 0xFFFFFFFF  # 二进制全1
        result5 = Code27_ReverseBits.reverse_bits1(n3)
        print(f"测试用例3 - 输入: {n3}")
        print(f"方法1结果: {result5} (预期: 0xFFFFFFFF)")
        
        # 复杂度分析
        print("\n=== 复杂度分析 ===")
        print("方法1 - 逐位反转:")
        print("  时间复杂度: O(1) - 固定32次操作")
        print("  空间复杂度: O(1)")
        
        print("方法2 - 分治反转:")
        print("  时间复杂度: O(1) - 固定5次位操作")
        print("  空间复杂度: O(1)")
        
        print("方法3 - 查表法:")
        print("  时间复杂度: O(1) - 固定4次查表操作")
        print("  空间复杂度: O(256) - 预计算表")
        
        # 工程化考量
        print("\n=== 工程化考量 ===")
        print("1. 方法选择：")
        print("   - 单次调用：方法2（分治）最优")
        print("   - 多次调用：方法3（查表）最优")
        print("2. 边界处理：Python整数可能超过32位，需要限制")
        print("3. 性能优化：避免不必要的循环")
        print("4. 可读性：添加详细注释说明位操作原理")
        
        # Python特性考量
        print("\n=== Python特性考量 ===")
        print("1. 整数表示：Python整数是动态大小的，需要手动限制为32位")
        print("2. 位操作：使用& 0xFFFFFFFF确保32位操作")
        print("3. 类属性：使用类属性存储预计算表")
        print("4. 静态方法：使用@staticmethod装饰器")
        
        # 算法技巧总结
        print("\n=== 算法技巧总结 ===")
        print("1. 位掩码技巧：使用十六进制常量作为位掩码")
        print("2. 分治思想：将大问题分解为小问题解决")
        print("3. 查表优化：空间换时间，适合重复计算")
        print("4. 无符号处理：确保正确处理无符号整数")

if __name__ == "__main__":
    Code27_ReverseBits.test()