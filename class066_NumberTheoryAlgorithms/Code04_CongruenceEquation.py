"""
同余方程 - Python实现

题目描述：
求关于x的同余方程 ax ≡ 1(mod b) 的最小正整数解
题目保证一定有解，也就是a和b互质

解题思路：
1. 将同余方程转化为不定方程：ax + by = 1
2. 使用扩展欧几里得算法求解不定方程
3. 得到特解后调整为最小正整数解

算法复杂度：
时间复杂度：O(log(min(a, b)))
空间复杂度：O(log(min(a, b)))

题目链接：
洛谷 P1082 [NOIP2012 提高组] 同余方程
https://www.luogu.com.cn/problem/P1082

相关题目：
1. 洛谷 P1516 青蛙的约会
   链接：https://www.luogu.com.cn/problem/P1516
   本题需要求解同余方程，是扩展欧几里得算法的经典应用

2. POJ 1061 青蛙的约会
   链接：http://poj.org/problem?id=1061
   与本题类似，需要求解同余方程

3. POJ 2115 C Looooops
   链接：http://poj.org/problem?id=2115
   本题需要求解模线性方程，可以转化为线性丢番图方程

4. Codeforces 1244C. The Football Stage
   链接：https://codeforces.com/problemset/problem/1244/C
   本题需要求解线性丢番图方程wx + dy = p

工程化考虑：
1. 异常处理：需要处理输入非法、大数等情况
2. 边界条件：需要考虑a、b的边界值
3. 性能优化：使用扩展欧几里得算法求解

调试能力：
1. 添加断言验证中间结果
2. 打印关键变量的实时值
3. 性能退化排查
"""


def exgcd(a, b):
    """
    扩展欧几里得算法
    
    算法原理：
    1. 当b=0时，gcd(a,0)=a，此时x=1, y=0
    2. 当b≠0时，递归计算gcd(b, a%b)的解x1, y1
    3. 根据等式推导：x = y1, y = x1 - (a/b) * y1
    
    时间复杂度：O(log(min(a, b)))
    空间复杂度：O(log(min(a, b)))（递归调用栈）
    
    :param a: 系数a
    :param b: 系数b
    :return: (gcd, x, y) 其中gcd为最大公约数，x, y为方程的解
    """
    if b == 0:
        return a, 1, 0
    
    gcd, x1, y1 = exgcd(b, a % b)
    x = y1
    y = x1 - (a // b) * y1
    
    return gcd, x, y


def solve_congruence_equation(a, b):
    """
    求解同余方程 ax ≡ 1 (mod b) 的最小正整数解
    
    :param a: 系数a
    :param b: 模数b
    :return: 最小正整数解x
    """
    gcd, x, y = exgcd(a, b)
    
    # 调整为最小正整数解
    return (x % b + b) % b


def main():
    """
    主方法 - 同余方程问题测试
    """
    print("=== 同余方程问题测试 ===")
    
    # 测试用例1
    a1, b1 = 3, 11
    result1 = solve_congruence_equation(a1, b1)
    print(f"测试1: {a1}x ≡ 1 (mod {b1}) 的解为: {result1}")
    print(f"验证: ({a1} * {result1}) % {b1} = {(a1 * result1) % b1}")
    
    # 测试用例2
    a2, b2 = 5, 13
    result2 = solve_congruence_equation(a2, b2)
    print(f"测试2: {a2}x ≡ 1 (mod {b2}) 的解为: {result2}")
    print(f"验证: ({a2} * {result2}) % {b2} = {(a2 * result2) % b2}")
    
    # 测试用例3
    a3, b3 = 7, 17
    result3 = solve_congruence_equation(a3, b3)
    print(f"测试3: {a3}x ≡ 1 (mod {b3}) 的解为: {result3}")
    print(f"验证: ({a3} * {result3}) % {b3} = {(a3 * result3) % b3}")
    
    # 测试用例4：边界情况
    a4, b4 = 1, 10
    result4 = solve_congruence_equation(a4, b4)
    print(f"测试4: {a4}x ≡ 1 (mod {b4}) 的解为: {result4}")
    print(f"验证: ({a4} * {result4}) % {b4} = {(a4 * result4) % b4}")
    
    print("=== 测试完成 ===")


if __name__ == "__main__":
    main()