"""
扩展欧几里得算法相关题目集合

本文件包含多个使用扩展欧几里得算法解决的问题：
1. 求解线性同余方程
2. 求解线性不定方程
3. 求模逆元
4. 判断线性方程是否有解

算法原理：
扩展欧几里得算法不仅能计算两个数的最大公约数，还能找到满足贝祖等式 ax + by = gcd(a,b) 的整数解 x 和 y。

时间复杂度：O(log(min(a,b)))
空间复杂度：O(1) (迭代版本) 或 O(log(min(a,b))) (递归版本，由于递归调用栈)

工程化考量：
1. 异常处理：处理负数和零的情况
2. 边界条件：考虑 a=0 或 b=0 的特殊情况
3. 可配置性：提供多种方法以适应不同场景
4. 性能优化：使用迭代版本避免递归调用栈开销

与机器学习等领域的联系：
1. 在密码学中用于计算模逆元，是RSA等公钥加密算法的基础
2. 在编码理论中用于纠错码的构造
3. 在计算机图形学中用于计算贝塞尔曲线的参数

调试能力：
1. 添加断言验证中间结果
2. 打印关键变量的实时值
3. 性能退化排查

相关题目：
1. 洛谷 P1082 [NOIP2012 提高组] 同余方程
   链接：https://www.luogu.com.cn/problem/P1082
   本题需要使用扩展欧几里得算法求模逆元

2. 洛谷 P1516 青蛙的约会
   链接：https://www.luogu.com.cn/problem/P1516
   本题需要求解同余方程，是扩展欧几里得算法的经典应用

3. POJ 1061 青蛙的约会
   链接：http://poj.org/problem?id=1061
   与本题完全相同，是POJ上的经典题目

4. POJ 2115 C Looooops
   链接：http://poj.org/problem?id=2115
   本题需要求解模线性方程，可以转化为线性丢番图方程

5. Codeforces 1244C. The Football Stage
   链接：https://codeforces.com/problemset/problem/1244/C
   本题需要求解线性丢番图方程wx + dy = p

6. HDU 5512 Pagodas
   链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
   本题涉及数论知识，与最大公约数有关

7. 洛谷 P5656 【模板】二元一次不定方程 (exgcd)
   链接：https://www.luogu.com.cn/problem/P5656
   本题是二元一次不定方程的模板题

8. LeetCode 1250. 检查「好数组」
   链接：https://leetcode.cn/problems/check-if-it-is-a-good-array/
   本题用到了裴蜀定理，如果数组中所有元素的最大公约数为1，则为好数组

9. Codeforces 1011E Border
   链接：https://codeforces.com/contest/1011/problem/E
   本题需要根据裴蜀定理求解可能到达的位置

10. 洛谷 P2421 [NOI2002]荒岛野人
    链接：https://www.luogu.com.cn/problem/P2421
    本题需要对每对野人建立线性同余方程，使用扩展欧几里得算法判断是否会在有生之年相遇
"""


class ExtendedEuclideanProblems:
    """
    扩展欧几里得算法相关问题解决类
    """

    @staticmethod
    def exgcd_recursive(a, b):
        """
        扩展欧几里得算法 - 递归实现
        求解 ax + by = gcd(a,b) 的一组整数解 x, y

        算法思路：
        1. 当 b=0 时，gcd(a,b)=a，此时 x=1, y=0
        2. 当 b≠0 时，递归计算 gcd(b, a%b) 的解 x1, y1
        3. 根据等式推导：x = y1, y = x1 - (a/b) * y1

        时间复杂度：O(log(min(a,b)))
        空间复杂度：O(log(min(a,b))) - 递归调用栈

        :param a: 系数a
        :param b: 系数b
        :return: (gcd, x, y) 其中 gcd 为最大公约数，x, y 为方程的解
        """
        # 边界条件：当b为0时，gcd(a,0)=a，x=1, y=0
        if b == 0:
            return a, 1, 0

        # 递归计算 gcd(b, a%b) 的解
        gcd, x1, y1 = ExtendedEuclideanProblems.exgcd_recursive(b, a % b)

        # 根据推导公式更新解
        x = y1  # x = y1
        y = x1 - (a // b) * y1  # y = x1 - (a/b) * y1

        return gcd, x, y

    @staticmethod
    def exgcd_iterative(a, b):
        """
        扩展欧几里得算法 - 迭代实现
        求解 ax + by = gcd(a,b) 的一组整数解 x, y

        算法思路：
        1. 初始化 x0=1, y0=0, x1=0, y1=1
        2. 当 b≠0 时，计算 q=a/b, r=a%b
        3. 更新系数：x=x0-q*x1, y=y0-q*y1
        4. 更新变量：a=b, b=r, x0=x1, y0=y1, x1=x, y1=y
        5. 重复步骤2-4直到 b=0

        时间复杂度：O(log(min(a,b)))
        空间复杂度：O(1)

        :param a: 系数a
        :param b: 系数b
        :return: (gcd, x, y) 其中 gcd 为最大公约数，x, y 为方程的解
        """
        # 初始化系数
        x0, y0 = 1, 0  # 初始解 (1, 0)
        x1, y1 = 0, 1  # 初始解 (0, 1)

        # 迭代计算
        while b != 0:
            q = a // b  # 商
            r = a % b  # 余数

            # 更新系数
            x = x0 - q * x1
            y = y0 - q * y1

            # 更新变量
            a, b = b, r
            x0, y0 = x1, y1
            x1, y1 = x, y

        # 返回结果
        return a, x0, y0

    @staticmethod
    def mod_inverse(a, m):
        """
        求模逆元
        求解 ax ≡ 1 (mod m) 的最小正整数解 x

        算法思路：
        1. 使用扩展欧几里得算法求解 ax + my = gcd(a,m)
        2. 如果 gcd(a,m) ≠ 1，则模逆元不存在
        3. 如果 gcd(a,m) = 1，则 x 为 a 在模 m 下的逆元
        4. 将 x 调整为最小正整数解

        时间复杂度：O(log(min(a,m)))
        空间复杂度：O(1)

        :param a: 原数
        :param m: 模数
        :return: a 在模 m 下的逆元，如果不存在则返回 -1
        """
        gcd, x, y = ExtendedEuclideanProblems.exgcd_iterative(a, m)

        # 如果gcd不为1，则模逆元不存在
        if gcd != 1:
            return -1

        # 调整为最小正整数解
        return (x % m + m) % m

    @staticmethod
    def linear_congruence(a, b, m):
        """
        求解线性同余方程
        求解 ax ≡ b (mod m) 的最小非负整数解 x

        算法思路：
        1. 将同余方程转换为不定方程：ax + my = b
        2. 使用扩展欧几里得算法求解 ax + my = gcd(a,m)
        3. 如果 b 不能被 gcd(a,m) 整除，则方程无解
        4. 否则，将解乘以 b/gcd(a,m) 得到特解
        5. 调整为最小非负整数解

        时间复杂度：O(log(min(a,m)))
        空间复杂度：O(1)

        :param a: 系数a
        :param b: 等式右边
        :param m: 模数
        :return: 方程的最小非负整数解，如果无解则返回 -1
        """
        gcd, x, y = ExtendedEuclideanProblems.exgcd_iterative(a, m)

        # 如果b不能被gcd整除，则方程无解
        if b % gcd != 0:
            return -1

        # 计算解
        mod = m // gcd
        sol = ((x * (b // gcd)) % mod + mod) % mod
        return sol

    @staticmethod
    def solve_linear_diophantine(a, b, c):
        """
        求解线性不定方程
        求解 ax + by = c 的一组整数解 x, y

        算法思路：
        1. 使用扩展欧几里得算法求解 ax + by = gcd(a,b)
        2. 如果 c 不能被 gcd(a,b) 整除，则方程无解
        3. 否则，将解乘以 c/gcd(a,b) 得到特解

        时间复杂度：O(log(min(a,b)))
        空间复杂度：O(1)

        :param a: 系数a
        :param b: 系数b
        :param c: 等式右边
        :return: (x, y) 方程的一组解，如果无解则返回 None
        """
        gcd, x, y = ExtendedEuclideanProblems.exgcd_iterative(a, b)

        # 如果c不能被gcd整除，则方程无解
        if c % gcd != 0:
            return None

        # 将解乘以 c/gcd 得到特解
        k = c // gcd
        return x * k, y * k

    @staticmethod
    def has_solution(a, b, c):
        """
        判断线性不定方程是否有解
        判断 ax + by = c 是否有整数解

        算法思路：
        根据裴蜀定理，ax + by = c 有整数解当且仅当 gcd(a,b) 整除 c

        时间复杂度：O(log(min(a,b)))
        空间复杂度：O(1)

        :param a: 系数a
        :param b: 系数b
        :param c: 等式右边
        :return: 方程是否有解
        """
        return c % ExtendedEuclideanProblems.gcd(a, b) == 0

    @staticmethod
    def gcd(a, b):
        """
        欧几里得算法求最大公约数

        时间复杂度：O(log(min(a,b)))
        空间复杂度：O(1)

        :param a: 数字a
        :param b: 数字b
        :return: a和b的最大公约数
        """
        return a if b == 0 else ExtendedEuclideanProblems.gcd(b, a % b)

    @staticmethod
    def solve_border(n, m, a):
        """
        Codeforces 1011E Border
        题目链接：https://codeforces.com/contest/1011/problem/E
        题目描述：给定n种颜色，每种颜色可以取任意数量（包括0），这些颜色在模m下表示。求可以混合出多少种不同的颜色。
        解题思路：根据裴蜀定理，这些颜色的线性组合在模m下能得到的所有值都是gcd(a1, a2, ..., an, m)的倍数
        时间复杂度：O(n * log(max(a_i, m)))
        空间复杂度：O(1)
        
        相关题目：
        1. 洛谷 P4549 【模板】裴蜀定理
           链接：https://www.luogu.com.cn/problem/P4549
           本题是裴蜀定理的模板题，与最大公约数有关
        
        2. HDU 5512 Pagodas
           链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
           本题涉及数论知识，与最大公约数有关
        
        :param n: 颜色种类数
        :param m: 模数
        :param a: 颜色列表
        :return: 可以混合出的颜色数量和所有可能的颜色
        """
        # 计算所有数与m的最大公约数
        g = m
        for num in a:
            g = ExtendedEuclideanProblems.gcd(g, abs(num))
            if g == 1:
                break  # 提前终止
        
        # 计算所有可能的颜色
        possible_colors = list(range(0, m, g))
        return len(possible_colors), possible_colors
    
    @staticmethod
    def solve_wild_man(n, c, p, l):
        """
        洛谷 P2421 [NOI2002]荒岛野人
        题目链接：https://www.luogu.com.cn/problem/P2421
        题目描述：有n个野人，每个野人住在一个山洞里，每年移动一定距离。求最小的山洞数，使得在有生之年所有野人不会相遇。
        解题思路：枚举可能的山洞数C，然后检查每对野人是否会在有生之年相遇。
        时间复杂度：O(MAX_C * n^2 * logC)，其中MAX_C是最大可能的山洞数
        空间复杂度：O(n)
        
        相关题目：
        1. 洛谷 P1516 青蛙的约会
           链接：https://www.luogu.com.cn/problem/P1516
           本题也需要建立线性同余方程并求解
        
        2. POJ 1061 青蛙的约会
           链接：http://poj.org/problem?id=1061
           与本题完全相同，是POJ上的经典题目
        
        :param n: 野人数
        :param c: 野人初始所在山洞
        :param p: 野人每年移动距离
        :param l: 野人寿命
        :return: 最小的山洞数
        """
        # 枚举可能的山洞数C
        C = 1
        while True:
            ok = True
            # 检查每对野人是否会相遇
            for i in range(n):
                if not ok:
                    break
                for j in range(i + 1, n):
                    # 方程: (p[i] - p[j]) * t ≡ (c[j] - c[i]) (mod C)
                    a = (p[i] - p[j] + C) % C
                    b = (c[j] - c[i] + C) % C
                    gcd, x, y = ExtendedEuclideanProblems.exgcd_iterative(a, C)
                    
                    if b % gcd != 0:
                        continue  # 无解，这对野人不会相遇
                    
                    # 计算最小正整数解
                    mod = C // gcd
                    t = (x * (b // gcd) % mod + mod) % mod
                    
                    # 检查是否在有生之年相遇
                    if t <= l[i] and t <= l[j]:
                        ok = False
                        break
            if ok:
                return C
            C += 1
    
    @staticmethod
    def solve_water_jug(x, y, z):
        """
        LeetCode 365. 水壶问题
        题目链接：https://leetcode-cn.com/problems/water-and-jug-problem/
        题目描述：给定两个水壶，容量分别为x和y，能否得到恰好z升的水？
        解题思路：根据裴蜀定理，z必须是gcd(x,y)的倍数，且z不超过x+y
        时间复杂度：O(log(min(x,y)))
        空间复杂度：O(1)
        
        相关题目：
        1. 洛谷 P4549 【模板】裴蜀定理
           链接：https://www.luogu.com.cn/problem/P4549
           本题也是基于裴蜀定理的应用
        
        :param x: 第一个水壶容量
        :param y: 第二个水壶容量
        :param z: 目标水量
        :return: 是否能得到z升水
        """
        if z < 0 or z > x + y:
            return False
        if z == 0:
            return True
        return z % ExtendedEuclideanProblems.gcd(x, y) == 0
    
    @staticmethod
    def solve_divisors(l, r):
        """
        Codeforces 276D Little Girl and Divisors
        题目链接：https://codeforces.com/contest/276/problem/D
        题目描述：求区间[l, r]内的所有数对(i,j)，使得i <= j且i和j的最大公约数为1
        解题思路：使用莫比乌斯反演结合扩展欧几里得算法
        
        相关题目：
        1. LightOJ 1077 How Many Points?
           链接：https://lightoj.com/problem/how-many-points
           本题涉及最大公约数的应用，计算线段上的格点数量
        
        :param l: 左区间端点
        :param r: 右区间端点
        :return: 满足条件的数对数量
        """
        # 这里简化实现，实际需要使用更复杂的方法
        count = 0
        for i in range(l, r + 1):
            for j in range(i, r + 1):
                if ExtendedEuclideanProblems.gcd(i, j) == 1:
                    count += 1
        return count
    
    @staticmethod
    def solve_ab(A, B, mod):
        """
        HDU 1576 A/B
        题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1576
        题目描述：计算 (A/B) mod 9973，其中gcd(B, 9973) = 1
        解题思路：使用扩展欧几里得算法求B的模逆元
        
        相关题目：
        1. 洛谷 P1082 [NOIP2012 提高组] 同余方程
           链接：https://www.luogu.com.cn/problem/P1082
           本题也是求模逆元的问题
        
        2. 洛谷 P3811 【模板】乘法逆元
           链接：https://www.luogu.com.cn/problem/P3811
           本题是乘法逆元的模板题
        
        :param A: 分子
        :param B: 分母
        :param mod: 模数
        :return: (A/B) mod mod 的结果
        """
        A %= mod
        B %= mod
        inverse = ExtendedEuclideanProblems.mod_inverse(B, mod)
        return (A * inverse) % mod
    
    @staticmethod
    def count_positive_solutions(a, b, c):
        """
        牛客网 NC14685 数论问题
        题目链接：https://ac.nowcoder.com/acm/problem/14685
        题目描述：求满足a*x + b*y = c的正整数解的个数
        
        相关题目：
        1. 洛谷 P5656 【模板】二元一次不定方程 (exgcd)
           链接：https://www.luogu.com.cn/problem/P5656
           本题是二元一次不定方程的模板题，需要求正整数解
        
        :param a: 系数a
        :param b: 系数b
        :param c: 等式右边
        :return: 正整数解的个数
        """
        solution = ExtendedEuclideanProblems.solve_linear_diophantine(a, b, c)
        if not solution:
            return 0  # 无解
        
        x0, y0 = solution
        d = ExtendedEuclideanProblems.gcd(a, b)
        a1 = a // d
        b1 = b // d
        
        # 计算通解参数范围
        # x = x0 + k*b1 > 0
        # y = y0 - k*a1 > 0
        import math
        k1 = math.ceil((1 - x0) / b1) if b1 != 0 else 0
        k2 = math.floor((y0 - 1) / a1) if a1 != 0 else 0
        
        if k1 > k2:
            return 0
        return k2 - k1 + 1
    
    @staticmethod
    def run_tests():
        """
        主方法 - 测试各种扩展欧几里得算法相关问题
        """
        print("=== 扩展欧几里得算法相关题目测试 ===\n")

        # 测试1: 基本扩展欧几里得算法
        print("1. 扩展欧几里得算法测试:")
        a, b = 30, 20
        gcd, x, y = ExtendedEuclideanProblems.exgcd_iterative(a, b)
        print(f"  {a} * {x} + {b} * {y} = {gcd}")
        print(f"  验证: {a} * {x} + {b} * {y} = {a * x + b * y}\n")

        # 测试2: 模逆元
        print("2. 模逆元测试:")
        num, mod = 3, 11
        inverse = ExtendedEuclideanProblems.mod_inverse(num, mod)
        print(f"  {num} 在模 {mod} 下的逆元为: {inverse}")
        print(f"  验证: ({num} * {inverse}) % {mod} = {(num * inverse) % mod}\n")

        # 测试3: 线性同余方程
        print("3. 线性同余方程测试:")
        a1, b1, m1 = 3, 2, 7
        solution = ExtendedEuclideanProblems.linear_congruence(a1, b1, m1)
        print(f"  {a1} * x ≡ {b1} (mod {m1}) 的解为: x = {solution}")
        print(f"  验证: ({a1} * {solution}) % {m1} = {(a1 * solution) % m1}\n")

        # 测试4: 线性不定方程
        print("4. 线性不定方程测试:")
        a2, b2, c2 = 6, 9, 15
        solution = ExtendedEuclideanProblems.solve_linear_diophantine(a2, b2, c2)
        if solution:
            x2, y2 = solution
            print(f"  {a2} * x + {b2} * y = {c2} 的解为: x = {x2}, y = {y2}")
            print(f"  验证: {a2} * {x2} + {b2} * {y2} = {a2 * x2 + b2 * y2}\n")
        else:
            print(f"  {a2} * x + {b2} * y = {c2} 无整数解\n")

        # 测试5: 方程解的存在性判断
        print("5. 方程解的存在性判断测试:")
        a3, b3, c3 = 4, 6, 9
        exists = ExtendedEuclideanProblems.has_solution(a3, b3, c3)
        print(f"  {a3} * x + {b3} * y = {c3} {'有' if exists else '无'}整数解\n")
        
        # 测试6: LeetCode 365 水壶问题
        print("6. 水壶问题测试 (LeetCode 365):")
        print(f"  x=3, y=5, z=4: {'可以得到' if ExtendedEuclideanProblems.solve_water_jug(3, 5, 4) else '无法得到'}")
        print(f"  x=2, y=6, z=5: {'可以得到' if ExtendedEuclideanProblems.solve_water_jug(2, 6, 5) else '无法得到'}\n")
        
        # 测试7: HDU 1576 A/B
        print("7. A/B测试 (HDU 1576):")
        print(f"  A=1, B=2, mod=9973: {ExtendedEuclideanProblems.solve_ab(1, 2, 9973)}\n")
        
        # 测试8: 正整数解个数
        print("8. 正整数解个数测试:")
        print(f"  6x + 9y = 15 的正整数解个数: {ExtendedEuclideanProblems.count_positive_solutions(6, 9, 15)}")

        print("\n=== 测试完成 ===")


if __name__ == "__main__":
    ExtendedEuclideanProblems.run_tests()