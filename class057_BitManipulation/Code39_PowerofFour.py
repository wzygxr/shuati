"""
4的幂（位运算解法）
测试链接：https://leetcode.cn/problems/power-of-four/

题目描述：
给定一个整数，写一个函数来判断它是否是 4 的幂次方。如果是，返回 true；否则，返回 false。
整数 n 是 4 的幂次方需满足：存在整数 x 使得 n == 4^x

解题思路：
1. 数学方法：循环除以4
2. 位运算法：利用4的幂的二进制特性
3. 对数方法：使用对数函数判断
4. 位运算优化：结合2的幂和特殊位置判断
5. 查表法：预计算所有4的幂

时间复杂度分析：
- 数学方法：O(log₄n)
- 位运算法：O(1)
- 对数方法：O(1)
- 位运算优化：O(1)
- 查表法：O(1)

空间复杂度分析：
- 数学方法：O(1)
- 位运算法：O(1)
- 对数方法：O(1)
- 位运算优化：O(1)
- 查表法：O(16) = O(1)
"""

import math

class Solution:
    def isPowerOfFour1(self, n: int) -> bool:
        """
        方法1：数学方法（循环除以4）
        时间复杂度：O(log₄n)
        空间复杂度：O(1)
        """
        if n <= 0:
            return False
        
        # Python整数除法需要注意
        while n % 4 == 0:
            n = n // 4
        
        return n == 1
    
    def isPowerOfFour2(self, n: int) -> bool:
        """
        方法2：位运算法（推荐）
        核心思想：4的幂一定是2的幂，且1出现在奇数位
        4的幂的二进制特点：
        1. 只有一个1（是2的幂）
        2. 1出现在奇数位（从1开始计数）
        3. 满足 n & (n-1) == 0 且 n & 0x55555555 != 0
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        # 检查n是否为正数，且是2的幂，且1出现在奇数位
        return n > 0 and (n & (n - 1)) == 0 and (n & 0x55555555) != 0
    
    def isPowerOfFour3(self, n: int) -> bool:
        """
        方法3：对数方法
        使用换底公式：log₄n = logn / log4
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        if n <= 0:
            return False
        
        log_result = math.log(n) / math.log(4)
        # 检查结果是否为整数（考虑浮点数精度）
        return abs(log_result - round(log_result)) < 1e-10
    
    def isPowerOfFour4(self, n: int) -> bool:
        """
        方法4：位运算优化版
        结合多种位运算技巧
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        if n <= 0:
            return False
        
        # 检查是否是2的幂
        if (n & (n - 1)) != 0:
            return False
        
        # 检查1是否出现在奇数位
        # 0x55555555 = 01010101010101010101010101010101
        return (n & 0x55555555) != 0
    
    def isPowerOfFour5(self, n: int) -> bool:
        """
        方法5：查表法
        预计算所有32位整数范围内的4的幂
        时间复杂度：O(1)
        空间复杂度：O(16) = O(1)
        """
        # 32位整数范围内所有4的幂
        powers_of_four = {
            1,          # 4^0
            4,          # 4^1
            16,         # 4^2
            64,         # 4^3
            256,        # 4^4
            1024,       # 4^5
            4096,       # 4^6
            16384,      # 4^7
            65536,      # 4^8
            262144,     # 4^9
            1048576,    # 4^10
            4194304,    # 4^11
            16777216,   # 4^12
            67108864,   # 4^13
            268435456,  # 4^14
            1073741824  # 4^15
        }
        
        return n in powers_of_four
    
    def isPowerOfFour6(self, n: int) -> bool:
        """
        方法6：递归方法
        时间复杂度：O(log₄n)
        空间复杂度：O(log₄n) - 递归栈深度
        """
        if n <= 0:
            return False
        if n == 1:
            return True
        if n % 4 != 0:
            return False
        return self.isPowerOfFour6(n // 4)
    
    def isPowerOfFour7(self, n: int) -> bool:
        """
        方法7：位运算+数学验证
        结合位运算和数学验证
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        # 检查n是否为正数且是2的幂
        if n <= 0 or (n & (n - 1)) != 0:
            return False
        
        # 4的幂除以3余数为1
        # 2的幂但不是4的幂除以3余数为2
        return n % 3 == 1
    
    def isPowerOfFour8(self, n: int) -> bool:
        """
        方法8：位计数法
        统计1后面的0的个数，检查是否为偶数
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        if n <= 0:
            return False
        
        # 检查是否是2的幂
        if (n & (n - 1)) != 0:
            return False
        
        # 统计末尾0的个数（从最低位开始）
        count = 0
        temp = n
        while temp > 1:
            temp = temp >> 1
            count += 1
        
        # 4的幂要求0的个数为偶数
        return count % 2 == 0

def test_solution():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：4的幂
    n1 = 16
    result1 = solution.isPowerOfFour2(n1)
    print(f"测试用例1 - 输入: {n1}")
    print(f"结果: {result1} (预期: True)")
    
    # 测试用例2：不是4的幂
    n2 = 5
    result2 = solution.isPowerOfFour2(n2)
    print(f"测试用例2 - 输入: {n2}")
    print(f"结果: {result2} (预期: False)")
    
    # 测试用例3：1（4^0）
    n3 = 1
    result3 = solution.isPowerOfFour2(n3)
    print(f"测试用例3 - 输入: {n3}")
    print(f"结果: {result3} (预期: True)")
    
    # 测试用例4：2的幂但不是4的幂
    n4 = 2
    result4 = solution.isPowerOfFour2(n4)
    print(f"测试用例4 - 输入: {n4}")
    print(f"结果: {result4} (预期: False)")
    
    # 测试用例5：边界情况（0）
    n5 = 0
    result5 = solution.isPowerOfFour2(n5)
    print(f"测试用例5 - 输入: {n5}")
    print(f"结果: {result5} (预期: False)")
    
    # 测试用例6：负数
    n6 = -16
    result6 = solution.isPowerOfFour2(n6)
    print(f"测试用例6 - 输入: {n6}")
    print(f"结果: {result6} (预期: False)")
    
    # 所有方法结果对比
    print("\n=== 所有方法结果对比 ===")
    test_num = 64  # 4^3
    print(f"测试数字: {test_num} (二进制: {bin(test_num)})")
    print(f"方法1 (数学方法): {solution.isPowerOfFour1(test_num)}")
    print(f"方法2 (位运算法): {solution.isPowerOfFour2(test_num)}")
    print(f"方法3 (对数方法): {solution.isPowerOfFour3(test_num)}")
    print(f"方法4 (位运算优化): {solution.isPowerOfFour4(test_num)}")
    print(f"方法5 (查表法): {solution.isPowerOfFour5(test_num)}")
    print(f"方法6 (递归方法): {solution.isPowerOfFour6(test_num)}")
    print(f"方法7 (位运算+数学): {solution.isPowerOfFour7(test_num)}")
    print(f"方法8 (位计数法): {solution.isPowerOfFour8(test_num)}")
    
    # 复杂度分析
    print("\n=== 复杂度分析 ===")
    print("方法1 - 数学方法:")
    print("  时间复杂度: O(log₄n)")
    print("  空间复杂度: O(1)")
    
    print("方法2 - 位运算法:")
    print("  时间复杂度: O(1)")
    print("  空间复杂度: O(1)")
    
    print("方法3 - 对数方法:")
    print("  时间复杂度: O(1)")
    print("  空间复杂度: O(1)")
    
    print("方法4 - 位运算优化:")
    print("  时间复杂度: O(1)")
    print("  空间复杂度: O(1)")
    
    print("方法5 - 查表法:")
    print("  时间复杂度: O(1)")
    print("  空间复杂度: O(16) = O(1)")
    
    # 工程化考量
    print("\n=== 工程化考量 ===")
    print("1. 算法选择：方法2 (位运算法) 最优")
    print("2. 性能优化：避免循环和函数调用")
    print("3. 边界处理：处理0和负数")
    print("4. 可读性：清晰的位运算逻辑")
    
    # 算法技巧总结
    print("\n=== 算法技巧总结 ===")
    print("1. 4的幂特性：是2的幂且1在奇数位")
    print("2. 位运算技巧：n & (n-1) 判断2的幂")
    print("3. 掩码应用：0x55555555 检查奇数位")
    print("4. 数学特性：4的幂除以3余数为1")
    
    # 二进制特性分析
    print("\n=== 二进制特性分析 ===")
    print("4的幂的二进制表示特点：")
    print("  4^0 = 1: 二进制 1")
    print("  4^1 = 4: 二进制 100")
    print("  4^2 = 16: 二进制 10000")
    print("  4^3 = 64: 二进制 1000000")
    print("规律：1后面跟着偶数个0")
    
    # Python特殊处理说明
    print("\n=== Python特殊处理说明 ===")
    print("Python整数是动态大小的，但位运算仍然有效：")
    print("  使用 n & 0x55555555 确保32位操作")
    print("  位运算在Python中自动处理大整数")
    print("  注意整数除法使用 // 而不是 /")

if __name__ == "__main__":
    test_solution()