"""
Codeforces 7C. Line
给定直线方程Ax + By + C = 0，求直线上任意一个整数点(x,y)
如果不存在整数点，输出-1
测试链接：https://codeforces.com/problemset/problem/7/C

Codeforces 7C. Line

问题描述：
给定直线方程Ax + By + C = 0，求直线上任意一个整数点(x,y)
如果不存在整数点，输出-1

解题思路：
1. 将直线方程转换为标准形式：Ax + By = -C
2. 使用扩展欧几里得算法求解方程Ax + By = gcd(A,B)的一组特解
3. 判断方程是否有整数解：当-C能被gcd(A,B)整除时有解
4. 如果有解，将特解乘以(-C)/gcd(A,B)得到原方程的一组特解

数学原理：
1. 裴蜀定理：方程Ax + By = -C有整数解当且仅当gcd(A,B)能整除-C
2. 扩展欧几里得算法：求解Ax + By = gcd(A,B)的一组特解
3. 通解公式：如果(x0,y0)是Ax + By = -C的一组特解，那么通解为：
   x = x0 + (B/gcd(A,B)) * t
   y = y0 - (A/gcd(A,B)) * t
   t为任意整数

时间复杂度：O(log(min(A,B)))，主要消耗在扩展欧几里得算法上
空间复杂度：O(1)

相关题目：
1. Codeforces 7C. Line
   链接：https://codeforces.com/problemset/problem/7C
2. POJ 2142 The Balance
   链接：http://poj.org/problem?id=2142
3. UVA 10090 Marbles
   链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1031
"""

def extended_gcd(a: int, b: int) -> tuple:
    """
    扩展欧几里得算法，求解ax + by = gcd(a,b)的一组特解
    
    Args:
        a: 第一个系数
        b: 第二个系数
        
    Returns:
        (x, y, gcd) 的元组
    """
    if b == 0:
        return (1, 0, a)
    x, y, g = extended_gcd(b, a % b)
    return (y, x - (a // b) * y, g)

def solve_line_equation(A: int, B: int, C: int) -> tuple:
    """
    求解直线方程Ax + By + C = 0的整数解
    
    Args:
        A: 系数A
        B: 系数B
        C: 常数C
        
    Returns:
        (x, y) 的元组，如果没有整数解返回None
    """
    # 特殊情况处理
    if A == 0 and B == 0:
        return (0, 0) if C == 0 else None
    
    # 将方程转换为标准形式：Ax + By = -C
    target = -C
    
    # 处理A或B为0的情况
    if A == 0:
        if target % B == 0:
            # 任意x都可以，y = -C/B
            return (0, target // B)
        else:
            return None
    
    if B == 0:
        if target % A == 0:
            # 任意y都可以，x = -C/A
            return (target // A, 0)
        else:
            return None
    
    # 使用扩展欧几里得算法
    x0, y0, gcd_val = extended_gcd(abs(A), abs(B))
    
    # 判断是否有解
    if target % gcd_val != 0:
        return None
    
    # 计算原方程的特解
    factor = target // gcd_val
    x0 *= factor
    y0 *= factor
    
    # 处理A或B为负数的情况
    if A < 0:
        x0 = -x0
    if B < 0:
        y0 = -y0
    
    return (x0, y0)

def main():
    """主函数，用于测试"""
    A, B, C = map(int, input().split())
    
    result = solve_line_equation(A, B, C)
    if result is None:
        print(-1)
    else:
        print(f"{result[0]} {result[1]}")

if __name__ == "__main__":
    # 测试用例1：Codeforces 7C示例
    print("测试用例1: A=2, B=5, C=3")
    result1 = solve_line_equation(2, 5, 3)
    if result1 is not None:
        print(f"x={result1[0]}, y={result1[1]}")
        # 验证：2*x + 5*y + 3 = 0
        print(f"验证: 2*{result1[0]} + 5*{result1[1]} + 3 = {2*result1[0] + 5*result1[1] + 3}")
    
    # 测试用例2：简单情况
    print("\\n测试用例2: A=1, B=1, C=1")
    result2 = solve_line_equation(1, 1, 1)
    if result2 is not None:
        print(f"x={result2[0]}, y={result2[1]}")
        print(f"验证: 1*{result2[0]} + 1*{result2[1]} + 1 = {result2[0] + result2[1] + 1}")
    
    # 测试用例3：无解情况
    print("\\n测试用例3: A=2, B=4, C=1")
    result3 = solve_line_equation(2, 4, 1)
    if result3 is None:
        print("No solution (expected)")
    
    # 测试用例4：A为0的情况
    print("\\n测试用例4: A=0, B=3, C=6")
    result4 = solve_line_equation(0, 3, 6)
    if result4 is not None:
        print(f"x={result4[0]}, y={result4[1]}")
        print(f"验证: 0*{result4[0]} + 3*{result4[1]} + 6 = {3*result4[1] + 6}")
    
    # 测试用例5：B为0的情况
    print("\\n测试用例5: A=4, B=0, C=8")
    result5 = solve_line_equation(4, 0, 8)
    if result5 is not None:
        print(f"x={result5[0]}, y={result5[1]}")
        print(f"验证: 4*{result5[0]} + 0*{result5[1]} + 8 = {4*result5[0] + 8}")
    
    # 测试扩展欧几里得算法
    print("\\n测试扩展欧几里得算法:")
    x, y, g = extended_gcd(12, 18)
    print(f"12*{x} + 18*{y} = {g}")
    
    # 运行主函数进行交互式测试
    # main()