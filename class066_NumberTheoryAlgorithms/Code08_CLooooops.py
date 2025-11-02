"""
题目：C Looooops
来源：POJ 2115
内容：给定循环 for (i = A; i != B; i = (i + C) % 2^k)，问循环是否会在有限步内结束

算法：扩展欧几里得算法求解线性同余方程
时间复杂度：O(log min(C, 2^k))
空间复杂度：O(1)

思路：
1. 循环可以表示为：A + C*t ≡ B (mod 2^k)
2. 转化为线性同余方程：C*t ≡ (B-A) (mod 2^k)
3. 使用扩展欧几里得算法求解

工程化考量：
- 异常处理：处理除零、溢出等情况
- 边界条件：A=B的特殊情况
- 性能优化：使用迭代版本避免递归深度限制
"""

class CLLoops:
    """
    C Looooops问题解决类
    """
    
    @staticmethod
    def extended_gcd(a, b):
        """
        扩展欧几里得算法（迭代版本）
        求解 ax + by = gcd(a,b) 的一组整数解 x, y
        
        :param a: 系数a
        :param b: 系数b
        :return: (gcd, x, y) 其中gcd为最大公约数
        """
        if a == 0 and b == 0:
            raise ValueError("a和b不能同时为0")
        
        if b == 0:
            return a, 1, 0
        
        x0, y0 = 1, 0  # 初始解 (1, 0)
        x1, y1 = 0, 1  # 初始解 (0, 1)
        r0, r1 = a, b
        
        while r1 != 0:
            q = r0 // r1
            
            # 更新系数
            x_temp = x0 - q * x1
            y_temp = y0 - q * y1
            r_temp = r0 - q * r1
            
            # 更新变量
            x0, y0, r0 = x1, y1, r1
            x1, y1, r1 = x_temp, y_temp, r_temp
        
        return r0, x0, y0
    
    @staticmethod
    def gcd(a, b):
        """
        欧几里得算法求最大公约数
        """
        return a if b == 0 else CLLoops.gcd(b, a % b)
    
    @staticmethod
    def solve_linear_congruence(a, b, m):
        """
        求解线性同余方程
        求解 ax ≡ b (mod m) 的最小非负整数解
        
        :param a: 系数a
        :param b: 等式右边
        :param m: 模数
        :return: 方程的最小非负整数解，如果无解则返回-1
        """
        if m <= 0:
            raise ValueError("模数m必须为正数")
        
        # 特殊情况处理
        if a == 0:
            if b == 0:
                return 0  # 任意解
            else:
                return -1  # 无解
        
        # 简化方程
        g = CLLoops.gcd(a, m)
        if b % g != 0:
            return -1  # 无解
        
        # 简化方程
        a //= g
        b //= g
        m //= g
        
        # 求a在模m下的逆元
        gcd_val, x, y = CLLoops.extended_gcd(a, m)
        if gcd_val != 1:
            return -1  # 理论上不会发生
        
        sol = (x * b) % m
        if sol < 0:
            sol += m
        
        return sol
    
    @staticmethod
    def solve_cloops(A, B, C, k):
        """
        求解C Looooops问题
        
        :param A: 初始值
        :param B: 目标值
        :param C: 增量
        :param k: 模数为2^k
        :return: 循环次数，如果无限循环则返回-1
        """
        # 特殊情况：如果A等于B，直接返回0
        if A == B:
            return 0
        
        # 计算模数
        modulus = 1 << k
        
        # 方程：A + C*t ≡ B (mod modulus)
        # 转化为：C*t ≡ (B-A) (mod modulus)
        a = C
        b = (B - A) % modulus
        if b < 0:
            b += modulus
        
        # 求解线性同余方程
        result = CLLoops.solve_linear_congruence(a, b, modulus)
        
        return result
    
    @staticmethod
    def run_tests():
        """
        主方法 - 测试C Looooops问题
        """
        print("=== C Looooops 问题测试 ===\n")
        
        # 测试用例1
        A1, B1, C1, k1 = 1, 2, 3, 4
        result1 = CLLoops.solve_cloops(A1, B1, C1, k1)
        print(f"测试1: A={A1}, B={B1}, C={C1}, k={k1}")
        if result1 != -1:
            print(f"结果: {result1} 次循环")
        else:
            print("结果: 无限循环")
        
        # 测试用例2
        A2, B2, C2, k2 = 0, 0, 1, 3
        result2 = CLLoops.solve_cloops(A2, B2, C2, k2)
        print(f"\n测试2: A={A2}, B={B2}, C={C2}, k={k2}")
        if result2 != -1:
            print(f"结果: {result2} 次循环")
        else:
            print("结果: 无限循环")
        
        # 测试用例3
        A3, B3, C3, k3 = 1, 1, 2, 2
        result3 = CLLoops.solve_cloops(A3, B3, C3, k3)
        print(f"\n测试3: A={A3}, B={B3}, C={C3}, k={k3}")
        if result3 != -1:
            print(f"结果: {result3} 次循环")
        else:
            print("结果: 无限循环")
        
        print("\n=== 测试完成 ===")


if __name__ == "__main__":
    CLLoops.run_tests()