"""
2的幂
测试链接：https://leetcode.cn/problems/power-of-two/

题目描述：
给你一个整数 n，请你判断该整数是否是 2 的幂次方。如果是，返回 true ；否则，返回 false 。
如果存在一个整数 x 使得 n == 2^x ，则认为 n 是 2 的幂次方。

解题思路：
1. 循环除法：不断除以2直到结果为1
2. 位运算技巧：利用 n & (n-1) == 0 的性质
3. 数学方法：利用对数运算
4. 查表法：预计算所有2的幂

时间复杂度：O(1) - 最多32次操作
空间复杂度：O(1) - 只使用常数个变量
"""
import math

class Code29_PowerOfTwo:
    """
    2的幂解决方案
    """
    
    @staticmethod
    def is_power_of_two1(n: int) -> bool:
        """
        方法1：循环除法
        时间复杂度：O(log n)
        空间复杂度：O(1)
        
        Args:
            n: 整数
            
        Returns:
            是否是2的幂
        """
        if n <= 0:
            return False
        
        while n % 2 == 0:
            n = n // 2
        
        return n == 1
    
    @staticmethod
    def is_power_of_two2(n: int) -> bool:
        """
        方法2：位运算技巧（最优解）
        利用性质：2的幂的二进制表示中只有一个1
        n & (n-1) 可以消除最低位的1，如果结果为0则是2的幂
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        Args:
            n: 整数
            
        Returns:
            是否是2的幂
        """
        return n > 0 and (n & (n - 1)) == 0
    
    @staticmethod
    def is_power_of_two3(n: int) -> bool:
        """
        方法3：数学方法
        利用对数运算：log2(n) 应该是整数
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        Args:
            n: 整数
            
        Returns:
            是否是2的幂
        """
        if n <= 0:
            return False
        
        log_result = math.log2(n)
        # 检查是否为整数（考虑浮点数精度）
        return abs(log_result - round(log_result)) < 1e-10
    
    @staticmethod
    def is_power_of_two4(n: int) -> bool:
        """
        方法4：查表法（适合多次调用）
        预计算所有32位有符号整数范围内的2的幂
        时间复杂度：O(1)
        空间复杂度：O(32)
        
        Args:
            n: 整数
            
        Returns:
            是否是2的幂
        """
        if n <= 0:
            return False
        
        # 预计算所有2的幂（32位有符号整数范围内）
        if not hasattr(Code29_PowerOfTwo, '_power_of_two_table'):
            Code29_PowerOfTwo._power_of_two_table = {1 << i for i in range(31)}  # 2^30是最大正数2的幂
        
        return n in Code29_PowerOfTwo._power_of_two_table
    
    @staticmethod
    def is_power_of_two5(n: int) -> bool:
        """
        方法5：利用最大2的幂的约数性质
        在32位有符号整数范围内，最大的2的幂是 2^30 = 1073741824
        如果n是2的幂，那么最大2的幂应该能被n整除
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        Args:
            n: 整数
            
        Returns:
            是否是2的幂
        """
        return n > 0 and (1073741824 % n == 0)
    
    @staticmethod
    def is_power_of_two6(n: int) -> bool:
        """
        方法6：利用bin函数
        检查二进制表示中1的个数是否为1
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        Args:
            n: 整数
            
        Returns:
            是否是2的幂
        """
        return n > 0 and bin(n).count('1') == 1
    
    @staticmethod
    def test():
        """测试方法"""
        # 测试用例1：正常情况（是2的幂）
        n1 = 16
        result1 = Code29_PowerOfTwo.is_power_of_two1(n1)
        result2 = Code29_PowerOfTwo.is_power_of_two2(n1)
        result3 = Code29_PowerOfTwo.is_power_of_two3(n1)
        result4 = Code29_PowerOfTwo.is_power_of_two4(n1)
        result5 = Code29_PowerOfTwo.is_power_of_two5(n1)
        result6 = Code29_PowerOfTwo.is_power_of_two6(n1)
        print(f"测试用例1 - 输入: {n1} (是2的幂)")
        print(f"方法1结果: {result1} (预期: True)")
        print(f"方法2结果: {result2} (预期: True)")
        print(f"方法3结果: {result3} (预期: True)")
        print(f"方法4结果: {result4} (预期: True)")
        print(f"方法5结果: {result5} (预期: True)")
        print(f"方法6结果: {result6} (预期: True)")
        
        # 测试用例2：正常情况（不是2的幂）
        n2 = 18
        result7 = Code29_PowerOfTwo.is_power_of_two2(n2)
        print(f"测试用例2 - 输入: {n2} (不是2的幂)")
        print(f"方法2结果: {result7} (预期: False)")
        
        # 测试用例3：边界情况（0）
        n3 = 0
        result8 = Code29_PowerOfTwo.is_power_of_two2(n3)
        print(f"测试用例3 - 输入: {n3}")
        print(f"方法2结果: {result8} (预期: False)")
        
        # 测试用例4：边界情况（负数）
        n4 = -8
        result9 = Code29_PowerOfTwo.is_power_of_two2(n4)
        print(f"测试用例4 - 输入: {n4}")
        print(f"方法2结果: {result9} (预期: False)")
        
        # 测试用例5：边界情况（1）
        n5 = 1
        result10 = Code29_PowerOfTwo.is_power_of_two2(n5)
        print(f"测试用例5 - 输入: {n5}")
        print(f"方法2结果: {result10} (预期: True)")
        
        # 复杂度分析
        print("\n=== 复杂度分析 ===")
        print("方法1 - 循环除法:")
        print("  时间复杂度: O(log n)")
        print("  空间复杂度: O(1)")
        
        print("方法2 - 位运算技巧:")
        print("  时间复杂度: O(1)")
        print("  空间复杂度: O(1)")
        
        print("方法3 - 数学方法:")
        print("  时间复杂度: O(1)")
        print("  空间复杂度: O(1)")
        
        print("方法4 - 查表法:")
        print("  时间复杂度: O(1)")
        print("  空间复杂度: O(32)")
        
        print("方法5 - 约数性质:")
        print("  时间复杂度: O(1)")
        print("  空间复杂度: O(1)")
        
        print("方法6 - bin函数方法:")
        print("  时间复杂度: O(1)")
        print("  空间复杂度: O(1)")
        
        # 工程化考量
        print("\n=== 工程化考量 ===")
        print("1. 方法选择：")
        print("   - 实际工程：方法2（位运算）最优")
        print("   - 面试场景：方法2（位运算）最优")
        print("   - 多次调用：方法4（查表法）最优")
        print("2. 边界处理：必须检查n>0")
        print("3. 性能优化：位运算最快，只需一次操作")
        print("4. 可读性：方法2代码简洁易懂")
        
        # Python特性考量
        print("\n=== Python特性考量 ===")
        print("1. 整数除法：使用//进行整数除法")
        print("2. 位运算：Python支持标准的位运算符")
        print("3. 数学函数：使用math.log2进行对数运算")
        print("4. 集合操作：使用集合进行快速查找")
        
        # 算法技巧总结
        print("\n=== 算法技巧总结 ===")
        print("1. n & (n-1) 技巧：判断是否只有一个1")
        print("2. 位运算性质：2的幂的二进制特性")
        print("3. 查表优化：预计算所有可能值")
        print("4. 数学性质：利用对数运算")
        print("5. 边界情况：0和负数都不是2的幂")

if __name__ == "__main__":
    Code29_PowerOfTwo.test()