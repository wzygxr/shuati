import time
from typing import List

class FastExponentiation:
    """
    位运算实现快速幂算法
    测试链接：https://leetcode.cn/problems/powx-n/
    
    题目描述：
    实现 pow(x, n)，即计算 x 的 n 次幂函数。
    
    解题思路：
    快速幂算法（Exponentiation by Squaring）利用位运算和分治思想，将时间复杂度从O(n)降低到O(log n)。
    核心思想：x^n = (x^2)^(n/2) * x^(n % 2)
    
    时间复杂度：O(log n) - 每次将指数减半
    空间复杂度：O(1) - 只使用常数个变量
    """
    
    @staticmethod
    def my_pow(x: float, n: int) -> float:
        """
        使用快速幂算法计算x的n次方
        
        Args:
            x: 底数
            n: 指数
            
        Returns:
            x的n次方结果
        """
        # 处理特殊情况
        if n == 0:
            return 1.0
        if x == 0.0:
            return 0.0
        if x == 1.0:
            return 1.0
        if x == -1.0:
            return 1.0 if n % 2 == 0 else -1.0
        
        # 处理n为负数的情况
        N = n
        if N < 0:
            x = 1 / x
            N = -N
        
        result = 1.0
        current = x
        
        # 使用位运算快速计算幂
        while N > 0:
            # 如果当前位为1，则乘以对应的幂
            if N & 1:
                result *= current
            # 平方当前值
            current *= current
            # 右移一位（相当于除以2）
            N >>= 1
        
        return result
    
    @staticmethod
    def my_pow_recursive(x: float, n: int) -> float:
        """
        递归实现快速幂算法
        
        Args:
            x: 底数
            n: 指数
            
        Returns:
            x的n次方结果
        """
        # 处理特殊情况
        if n == 0:
            return 1.0
        if x == 0.0:
            return 0.0
        
        N = n
        if N < 0:
            x = 1 / x
            N = -N
        
        return FastExponentiation._fast_pow(x, N)
    
    @staticmethod
    def _fast_pow(x: float, n: int) -> float:
        """递归辅助函数"""
        if n == 0:
            return 1.0
        
        half = FastExponentiation._fast_pow(x, n // 2)
        
        if n % 2 == 0:
            return half * half
        else:
            return half * half * x
    
    @staticmethod
    def mod_pow(x: int, n: int, mod: int) -> int:
        """
        处理大数取模的快速幂算法（常用于密码学）
        
        Args:
            x: 底数
            n: 指数
            mod: 模数
            
        Returns:
            (x^n) % mod
        """
        if mod == 1:
            return 0
        if n == 0:
            return 1
        
        x = x % mod
        result = 1
        
        while n > 0:
            if n & 1:
                result = (result * x) % mod
            x = (x * x) % mod
            n >>= 1
        
        return result
    
    @staticmethod
    def matrix_pow(matrix: List[List[int]], n: int) -> List[List[int]]:
        """
        矩阵快速幂算法（用于斐波那契数列等）
        计算矩阵的n次幂
        
        Args:
            matrix: 2x2矩阵
            n: 指数
            
        Returns:
            矩阵的n次幂
        """
        # 单位矩阵
        result = [[1, 0], [0, 1]]
        current = [row[:] for row in matrix]  # 深拷贝
        
        while n > 0:
            if n & 1:
                result = FastExponentiation._multiply_matrix(result, current)
            current = FastExponentiation._multiply_matrix(current, current)
            n >>= 1
        
        return result
    
    @staticmethod
    def fibonacci(n: int) -> int:
        """
        使用矩阵快速幂计算斐波那契数列第n项
        
        Args:
            n: 项数
            
        Returns:
            斐波那契数列第n项
        """
        if n <= 1:
            return n
        
        base = [[1, 1], [1, 0]]
        result = FastExponentiation.matrix_pow(base, n - 1)
        return result[0][0]
    
    @staticmethod
    def _multiply_matrix(a: List[List[int]], b: List[List[int]]) -> List[List[int]]:
        """
        2x2矩阵乘法
        
        Args:
            a: 第一个矩阵
            b: 第二个矩阵
            
        Returns:
            矩阵乘积
        """
        result = [[0, 0], [0, 0]]
        result[0][0] = a[0][0] * b[0][0] + a[0][1] * b[1][0]
        result[0][1] = a[0][0] * b[0][1] + a[0][1] * b[1][1]
        result[1][0] = a[1][0] * b[0][0] + a[1][1] * b[1][0]
        result[1][1] = a[1][0] * b[0][1] + a[1][1] * b[1][1]
        return result

# 测试代码
if __name__ == "__main__":
    # 测试基本快速幂
    print("=== 基本快速幂测试 ===")
    print(f"2^10 = {FastExponentiation.my_pow(2.0, 10)}")  # 1024.0
    print(f"2.1^3 = {FastExponentiation.my_pow(2.1, 3)}")  # 9.261
    print(f"2^-2 = {FastExponentiation.my_pow(2.0, -2)}")  # 0.25
    print(f"0^5 = {FastExponentiation.my_pow(0.0, 5)}")  # 0.0
    print(f"1^100 = {FastExponentiation.my_pow(1.0, 100)}")  # 1.0
    
    # 测试递归实现
    print("\n=== 递归快速幂测试 ===")
    print(f"2^10 = {FastExponentiation.my_pow_recursive(2.0, 10)}")  # 1024.0
    print(f"2^-2 = {FastExponentiation.my_pow_recursive(2.0, -2)}")  # 0.25
    
    # 测试模幂运算
    print("\n=== 模幂运算测试 ===")
    print(f"3^10 mod 7 = {FastExponentiation.mod_pow(3, 10, 7)}")  # 4
    print(f"2^100 mod 13 = {FastExponentiation.mod_pow(2, 100, 13)}")  # 3
    
    # 测试矩阵快速幂（斐波那契数列）
    print("\n=== 矩阵快速幂测试（斐波那契） ===")
    print(f"F(10) = {FastExponentiation.fibonacci(10)}")  # 55
    print(f"F(20) = {FastExponentiation.fibonacci(20)}")  # 6765
    
    # 性能对比测试
    print("\n=== 性能对比测试 ===")
    
    # 快速幂
    start_time = time.time()
    result1 = FastExponentiation.my_pow(2.0, 1000000)
    end_time = time.time()
    print(f"快速幂耗时: {(end_time - start_time) * 1000:.3f} 毫秒")
    
    # 普通幂运算（对比）
    start_time = time.time()
    result2 = 1.0
    for i in range(1000000):
        result2 *= 2.0
    end_time = time.time()
    print(f"普通幂运算耗时: {(end_time - start_time) * 1000:.3f} 毫秒")
    
    # 算法原理说明
    print("\n=== 算法原理说明 ===")
    print("快速幂算法的核心思想是将指数n分解为二进制形式，然后通过平方和乘法组合结果。")
    print("例如：计算3^13")
    print("13的二进制：1101")
    print("3^13 = 3^(8+4+1) = 3^8 * 3^4 * 3^1")
    print("通过不断平方：3^1, 3^2, 3^4, 3^8...")
    print("然后根据二进制位选择需要乘入结果的幂")