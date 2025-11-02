"""
题目：线性丢番图方程
来源：综合题目
内容：求解形如 ax + by = c 的线性丢番图方程

算法：扩展欧几里得算法
时间复杂度：O(log min(a, b))
空间复杂度：O(1)

思路：
1. 使用扩展欧几里得算法求解 ax + by = gcd(a,b)
2. 如果c不能被gcd(a,b)整除，则方程无解
3. 否则，将解乘以c/gcd(a,b)得到特解
4. 通解为：x = x0 + k*(b/g), y = y0 - k*(a/g)

工程化考量：
- 异常处理：处理除零、溢出等情况
- 边界条件：a=0或b=0的特殊情况
- 性能优化：使用迭代版本避免递归深度限制
"""

import math

class DiophantineEquation:
    """
    线性丢番图方程解决类
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
        return a if b == 0 else DiophantineEquation.gcd(b, a % b)
    
    @staticmethod
    def solve_diophantine(a, b, c):
        """
        求解线性丢番图方程
        求解 ax + by = c 的一组整数解
        
        :param a: 系数a
        :param b: 系数b
        :param c: 等式右边
        :return: (has_solution, x, y) 是否有解及解的值
        """
        # 特殊情况处理
        if a == 0 and b == 0:
            return c == 0, 0, 0  # 0x + 0y = c 有解当且仅当c=0
        
        if a == 0:
            if c % b == 0:
                return True, 0, c // b
            else:
                return False, 0, 0
        
        if b == 0:
            if c % a == 0:
                return True, c // a, 0
            else:
                return False, 0, 0
        
        # 使用扩展欧几里得算法
        g, x, y = DiophantineEquation.extended_gcd(a, b)
        
        if c % g != 0:
            return False, 0, 0  # 无解
        
        # 将解乘以c/g得到特解
        k = c // g
        x *= k
        y *= k
        
        return True, x, y
    
    @staticmethod
    def find_positive_solutions(a, b, c):
        """
        求线性丢番图方程的所有正整数解
        
        :param a: 系数a
        :param b: 系数b
        :param c: 等式右边
        :return: 正整数解列表
        """
        solutions = []
        
        has_solution, x0, y0 = DiophantineEquation.solve_diophantine(a, b, c)
        if not has_solution:
            return solutions
        
        g, _, _ = DiophantineEquation.extended_gcd(a, b)
        a1 = a // g
        b1 = b // g
        
        # 通解：x = x0 + k*b1, y = y0 - k*a1
        # 需要满足 x > 0 且 y > 0
        
        # 计算k的范围
        # x > 0 => x0 + k*b1 > 0 => k > -x0/b1
        # y > 0 => y0 - k*a1 > 0 => k < y0/a1
        
        k_min = -x0 / b1
        k_max = y0 / a1
        
        k_start = math.ceil(k_min + 1e-9)  # 向上取整
        k_end = math.floor(k_max - 1e-9)   # 向下取整
        
        if k_start > k_end:
            return solutions  # 无正整数解
        
        # 生成所有正整数解
        for k in range(int(k_start), int(k_end) + 1):
            x_sol = x0 + k * b1
            y_sol = y0 - k * a1
            
            if x_sol > 0 and y_sol > 0:
                solutions.append((x_sol, y_sol))
        
        return solutions
    
    @staticmethod
    def has_solution(a, b, c):
        """
        判断线性丢番图方程是否有解
        根据裴蜀定理：ax + by = c 有整数解当且仅当 gcd(a,b) 整除 c
        """
        if a == 0 and b == 0:
            return c == 0
        
        if a == 0:
            return c % b == 0
        
        if b == 0:
            return c % a == 0
        
        g = DiophantineEquation.gcd(a, b)
        return c % g == 0
    
    @staticmethod
    def run_tests():
        """
        主方法 - 测试线性丢番图方程
        """
        print("=== 线性丢番图方程测试 ===\n")
        
        # 测试1：基本求解
        print("1. 基本求解测试:")
        a1, b1, c1 = 6, 9, 15
        has_sol1, x1, y1 = DiophantineEquation.solve_diophantine(a1, b1, c1)
        
        if has_sol1:
            print(f"方程 {a1}x + {b1}y = {c1} 有解:")
            print(f"特解: x = {x1}, y = {y1}")
            print(f"验证: {a1}*{x1} + {b1}*{y1} = {a1*x1 + b1*y1}")
        else:
            print(f"方程 {a1}x + {b1}y = {c1} 无解")
        
        # 测试2：无解情况
        print("\n2. 无解情况测试:")
        a2, b2, c2 = 4, 6, 9
        has_sol2, x2, y2 = DiophantineEquation.solve_diophantine(a2, b2, c2)
        
        if has_sol2:
            print(f"方程 {a2}x + {b2}y = {c2} 有解")
        else:
            print(f"方程 {a2}x + {b2}y = {c2} 无解")
            g = DiophantineEquation.gcd(a2, b2)
            print(f"验证: gcd({a2}, {b2}) = {g}, {c2} % gcd = {c2 % g}")
        
        # 测试3：正整数解
        print("\n3. 正整数解测试:")
        a3, b3, c3 = 3, 5, 20
        solutions = DiophantineEquation.find_positive_solutions(a3, b3, c3)
        
        print(f"方程 {a3}x + {b3}y = {c3} 的正整数解个数: {len(solutions)}")
        if solutions:
            print("正整数解:")
            for i, (x, y) in enumerate(solutions, 1):
                print(f"  解{i}: x = {x}, y = {y}")
        
        # 测试4：裴蜀定理验证
        print("\n4. 裴蜀定理验证:")
        print(f"方程 3x + 5y = 1 是否有解: {'是' if DiophantineEquation.has_solution(3, 5, 1) else '否'}")
        print(f"方程 4x + 6y = 1 是否有解: {'是' if DiophantineEquation.has_solution(4, 6, 1) else '否'}")
        print(f"方程 6x + 9y = 3 是否有解: {'是' if DiophantineEquation.has_solution(6, 9, 3) else '否'}")
        
        print("\n=== 测试完成 ===")


if __name__ == "__main__":
    DiophantineEquation.run_tests()