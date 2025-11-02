"""
POJ 2142 The Balance
给定a、b、c，求解方程ax + by = c
要求找到一组解(x,y)，使得|x| + |y|最小
如果有多个解，选择x最小的解
测试链接：http://poj.org/problem?id=2142

POJ 2142 The Balance

问题描述：
给定a、b、c，求解方程ax + by = c
要求找到一组解(x,y)，使得|x| + |y|最小
如果有多个解，选择x最小的解

解题思路：
1. 使用扩展欧几里得算法求解ax + by = gcd(a,b)的一组特解
2. 判断方程是否有解：当c能被gcd(a,b)整除时有解
3. 如果有解，将特解乘以c/gcd(a,b)得到原方程的一组特解
4. 根据通解公式求出满足条件的解
5. 在所有解中寻找|x| + |y|最小的解

数学原理：
1. 裴蜀定理：方程ax + by = c有整数解当且仅当gcd(a,b)能整除c
2. 扩展欧几里得算法：求解ax + by = gcd(a,b)的一组特解
3. 通解公式：如果(x0,y0)是ax + by = c的一组特解，那么通解为：
   x = x0 + (b/gcd(a,b)) * t
   y = y0 - (a/gcd(a,b)) * t
   t为任意整数

时间复杂度：O(log(min(a,b)))，主要消耗在扩展欧几里得算法上
空间复杂度：O(1)

相关题目：
1. POJ 2142 The Balance
   链接：http://poj.org/problem?id=2142
2. UVA 10090 Marbles
   链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1031
3. Codeforces 7C. Line
   链接：https://codeforces.com/problemset/problem/7/C
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

def solve_equation(a: int, b: int, c: int) -> tuple:
    """
    求解方程ax + by = c，并找到|x| + |y|最小的解
    
    Args:
        a: 第一个系数
        b: 第二个系数
        c: 常数项
        
    Returns:
        (x, y) 的元组，如果没有解返回None
    """
    # 特殊情况处理
    if a == 0 and b == 0:
        return (0, 0) if c == 0 else None
    
    # 使用扩展欧几里得算法
    x0, y0, gcd_val = extended_gcd(a, b)
    
    # 判断是否有解
    if c % gcd_val != 0:
        return None
    
    # 计算原方程的特解
    factor = c // gcd_val
    x0 *= factor
    y0 *= factor
    
    # 通解公式参数
    k1 = b // gcd_val
    k2 = a // gcd_val
    
    # 寻找|x| + |y|最小的解
    # 通解：x = x0 + k1 * t, y = y0 - k2 * t
    # 我们需要最小化 |x0 + k1*t| + |y0 - k2*t|
    
    # 使用数学方法找到最优的t值
    # 最优t应该在x0/k1和y0/k2附近
    t1 = int((-x0) / k1)
    t2 = int((y0 + k2 - 1) / k2)  # 向上取整
    
    # 检查几个候选t值
    best_x, best_y = 0, 0
    min_sum = float('inf')
    
    # 检查t1-1, t1, t1+1, t2-1, t2, t2+1
    for t in range(t1 - 1, t1 + 2):
        x_val = x0 + k1 * t
        y_val = y0 - k2 * t
        current_sum = abs(x_val) + abs(y_val)
        if current_sum < min_sum or (current_sum == min_sum and x_val < best_x):
            min_sum = current_sum
            best_x = x_val
            best_y = y_val
    
    for t in range(t2 - 1, t2 + 2):
        x_val = x0 + k1 * t
        y_val = y0 - k2 * t
        current_sum = abs(x_val) + abs(y_val)
        if current_sum < min_sum or (current_sum == min_sum and x_val < best_x):
            min_sum = current_sum
            best_x = x_val
            best_y = y_val
    
    return (best_x, best_y)

def main():
    """主函数，用于测试"""
    while True:
        try:
            a, b, c = map(int, input().split())
            if a == 0 and b == 0 and c == 0:
                break
            
            result = solve_equation(a, b, c)
            if result is None:
                print("No solution")
            else:
                print(f"{result[0]} {result[1]}")
        except EOFError:
            break

if __name__ == "__main__":
    # 测试用例1：POJ 2142示例
    print("测试用例1: a=700, b=300, c=200")
    result1 = solve_equation(700, 300, 200)
    if result1 is not None:
        print(f"x={result1[0]}, y={result1[1]}")
        print(f"|x| + |y| = {abs(result1[0]) + abs(result1[1])}")
    
    # 测试用例2：简单情况
    print("\\n测试用例2: a=2, b=3, c=5")
    result2 = solve_equation(2, 3, 5)
    if result2 is not None:
        print(f"x={result2[0]}, y={result2[1]}")
        print(f"|x| + |y| = {abs(result2[0]) + abs(result2[1])}")
    
    # 测试用例3：无解情况
    print("\\n测试用例3: a=2, b=4, c=1")
    result3 = solve_equation(2, 4, 1)
    if result3 is None:
        print("No solution (expected)")
    
    # 测试扩展欧几里得算法
    print("\\n测试扩展欧几里得算法:")
    x, y, g = extended_gcd(12, 18)
    print(f"12*{x} + 18*{y} = {g}")
    
    # 运行主函数进行交互式测试
    # main()