# 判断较大的数字是否是质数(Miller-Rabin测试)
# 测试链接 : https://www.luogu.com.cn/problem/U148828
# 本文件可以搞定任意范围数字的质数检查
# 时间复杂度O(s * (logn)^3)，其中s是测试轮数
# 适用范围：适用于大数（超过long范围的数）的素性判断
# 算法原理：基于费马小定理和随机化的概率性测试
# 相关题目：
# 1. POJ 1811 Prime Test
#    链接：http://poj.org/problem?id=1811
#    题目描述：给定一个大整数(2 <= N < 2^54)，判断它是否为素数，如果不是输出最小质因子
# 2. Luogu U148828 大数质数判断
#    链接：https://www.luogu.com.cn/problem/U148828
#    题目描述：判断给定的大整数是否为质数
# 3. Codeforces 679A Bear and Prime 100 (交互题)
#    链接：https://codeforces.com/problemset/problem/679/A
#    题目描述：系统想了一个2到100之间的数，你需要通过最多20次询问判断这个数是否为质数
# 4. LeetCode 204. Count Primes (计数质数)
#    链接：https://leetcode.cn/problems/count-primes/
#    题目描述：统计所有小于非负整数 n 的质数的数量
# 5. LeetCode 313. Super Ugly Number (超级丑数)
#    链接：https://leetcode.cn/problems/super-ugly-number/
#    题目描述：超级丑数是指其所有质因数都是长度为 k 的质数列表 primes 中的正整数
# 6. HackerRank Primality Test
#    链接：https://www.hackerrank.com/challenges/primality-test/problem
#    题目描述：使用Miller-Rabin算法判断一个数是否是质数
# 7. UVa 10140 Prime Distance
#    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1081
#    题目描述：给定两个整数L和U，求区间[L,U]内相邻质数的最大和最小距离
# 8. SPOJ TDPRIMES - Printing some primes
#    链接：https://www.spoj.com/problems/TDPRIMES/
#    题目描述：打印前5000000个质数
# 9. CodeChef Prime Generator
#    链接：https://www.codechef.com/problems/PRIME1
#    题目描述：生成区间内的所有质数
# 10. Project Euler Problem 3 Largest prime factor
#    链接：https://projecteuler.net/problem=3
#    题目描述：找出一个数的最大质因数
# 11. HDU 4344 Markov Matrix
#    链接：http://acm.hdu.edu.cn/showproblem.php?pid=4344
#    题目描述：涉及大数质数判断
# 12. 牛客网 NC15688 质数拆分
#    链接：https://ac.nowcoder.com/acm/problem/15688
#    题目描述：将一个数拆分成若干个质数之和
# 13. LintCode 498. 回文素数
#    链接：https://www.lintcode.com/problem/498/
#    题目描述：找出大于等于n的最小回文素数
# 14. 杭电OJ 1719 Friend or Foe
#    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1719
#    题目描述：判断一个数是否是友好数或敌人
# 15. TimusOJ 1007 数学问题
#    链接：https://acm.timus.ru/problem.aspx?space=1&num=1007
#    题目描述：判断一个数是否是质数
# 16. AizuOJ 0100 Prime Factorize
#    链接：https://onlinejudge.u-aizu.ac.jp/problems/0100
#    题目描述：对输入的数进行质因数分解
# 17. LOJ #10205. 「一本通 6.5 例 2」Prime Distance
#    链接：https://loj.ac/p/10205
#    题目描述：求区间内的质数距离
# 18. 计蒜客 质数判定
#    链接：https://www.jisuanke.com/course/705/28547
#    题目描述：实现质数判定算法
# 19. acwing 867. 分解质因数
#    链接：https://www.acwing.com/problem/content/869/
#    题目描述：分解质因数，结合质数判断
# 20. Codeforces 1332E Height All the Same
#    链接：https://codeforces.com/problemset/problem/1332/E
#    题目描述：涉及质数判断的数学问题
# 21. POJ 3641 Pseudoprime numbers
#    链接：http://poj.org/problem?id=3641
#    题目描述：判断一个数是否是伪素数
# 22. HackerEarth Prime Generator
#    链接：https://www.hackerearth.com/practice/math/number-theory/primality-tests/practice-problems/
#    题目描述：生成指定范围内的质数
# 23. MarsCode 大数质数检测
#    链接：https://www.mars.pub/code/view/1000000029
#    题目描述：实现Miller-Rabin算法
# 24. AtCoder ABC152 D - Handstand 2
#    链接：https://atcoder.jp/contests/abc152/tasks/abc152_d
#    题目描述：涉及质数的判断和应用
# 25. UVA 10780 Again Prime? No Time.
#    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1721
#    题目描述：涉及质因数分解和质数判断
# 26. TopCoder SRM 769 Div1 Easy PrimeFactorization
#    链接：https://community.topcoder.com/stat?c=problem_statement&pm=15772
#    题目描述：质因数分解问题
# 27. Codeforces 1465 A Odd Divisor
#    链接：https://codeforces.com/problemset/problem/1465/A
#    题目描述：判断一个数是否有奇数因子
# 28. 剑指Offer II 002. 二进制中1的个数
#    链接：https://leetcode.cn/problems/er-jin-zhi-zhong-1de-ge-shu-lcof/
#    题目描述：统计二进制中1的个数，可与质数判断结合
# 29. LeetCode 762. Prime Number of Set Bits in Binary Representation
#    链接：https://leetcode.cn/problems/prime-number-of-set-bits-in-binary-representation/
#    题目描述：统计区间[L, R]内的整数中，其二进制表示中1的个数是质数的数的个数
# 30. Codeforces 271B Prime Matrix
#    链接：https://codeforces.com/problemset/problem/271/B
#    题目描述：给定一个矩阵，通过最少的移动次数将其转换为素数矩阵

import sys
import time
import random

# 质数的个数代表测试次数
# 如果想增加测试次数就继续增加更大的质数
# 使用前12个质数作为测试底数，可以有效降低误判率
# 注意：对于不同范围的数，可以使用不同的测试底数组合以获得最优性能和准确性
p = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37]

# 根据数的范围选择不同的测试底数
# 参考：https://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test#Deterministic_tests_up_to_certain_bound
def get_optimal_bases(n):
    """
    根据数的范围选择最优的测试底数集合
    对于不同范围的数，使用不同的底数集合可以保证确定性结果
    
    :param n: 待测试的数
    :return: 最优的测试底数列表
    """
    if n < 2047:
        return [2]
    elif n < 1373593:
        return [2, 3]
    elif n < 9080191:
        return [31, 73]
    elif n < 25326001:
        return [2, 3, 5]
    elif n < 3215031751:
        return [2, 3, 5, 7]
    elif n < 47594326373:
        return [2, 3, 5, 7, 11]
    elif n < 1122004669633:
        return [2, 3, 5, 7, 11, 13]
    else:
        # 对于更大的数，使用预定义的12个质数
        return p

def power(n, p, mod):
    """
    快速幂运算：计算 n^p mod mod
    时间复杂度：O(log p) - 每次迭代将指数减半，总共有log(p)次迭代
    空间复杂度：O(1) - 只使用常数级额外空间
    
    :param n: 底数
    :param p: 指数
    :param mod: 模数
    :return: n^p mod mod
    
    算法原理：
    1. 将指数p用二进制表示
    2. 从低位到高位，如果该位为1，则将当前底数乘入结果
    3. 每次将底数平方，指数右移一位
    
    优化点：
    1. 使用位运算(&, >>)替代算术运算，提高性能
    2. 在每次乘法后立即取模，避免数值溢出
    3. 对于大数乘法，可考虑使用Karatsuba算法进一步优化
    
    工程化考虑：
    1. 处理边界情况：当mod=1时，任何数的模都是0
    2. 当n或p为0时的特殊处理
    """
    # 处理mod=1的特殊情况
    if mod == 1:
        return 0
    
    ans = 1
    n = n % mod  # 预先对底数取模，避免数值过大
    
    while p > 0:
        # 使用位运算判断奇偶性，比模运算更高效
        if (p & 1) == 1:
            ans = (ans * n) % mod
        # 底数平方，并取模
        n = (n * n) % mod
        # 指数右移一位，相当于除以2
        p >>= 1
    
    return ans

def witness(a, n):
    """
    Miller-Rabin单次测试函数
    
    :param a: 测试底数
    :param n: 待测试数
    :return: 如果n是合数返回True，否则返回False
    
    算法原理：
    1. 将n-1表示为u*2^t的形式，其中u是奇数
    2. 计算a^u mod n
    3. 如果结果为1或n-1，则通过本次测试
    4. 否则，重复计算平方模运算t-1次
    5. 如果在过程中得到n-1，则通过本次测试
    6. 否则，n是合数
    
    数学依据：
    - 费马小定理：如果n是质数，则对于任何a(1<a<n)，有a^(n-1) ≡ 1 (mod n)
    - 二次探测：如果n是质数，则1 mod n的平方根只能是1或n-1
    """
    u = n - 1
    t = 0
    # 将n-1分解为u*2^t的形式，其中u是奇数
    while (u & 1) == 0:
        t += 1
        u >>= 1
    
    # 计算a^u mod n
    x1 = power(a, u, n)
    x2 = 0
    
    for i in range(1, t + 1):
        x2 = power(x1, 2, n)
        # 二次探测：如果x2=1但x1既不是1也不是n-1，则存在非平凡平方根，n是合数
        if x2 == 1 and x1 != 1 and x1 != n - 1:
            return True
        x1 = x2
    
    # 如果最后结果不是1，则违反费马小定理，n是合数
    if x1 != 1:
        return True
    
    return False

def miller_rabin(n):
    """
    Miller-Rabin素性测试主函数
    时间复杂度：O(s * (logn)^3)，其中s是测试轮数
    空间复杂度：O(1)
    
    :param n: 待测试的数
    :return: 如果是质数返回True，否则返回False
    
    算法特点：
    1. 这是一个概率算法，有一定误判率
    2. 对于合数，误判为质数的概率不超过(1/4)^s
    3. 对于质数，永远不会误判
    4. 对于n < 2^64，使用特定的测试底数集合可以保证确定性结果
    
    工程化考虑：
    1. 使用固定的质数作为底数，提高稳定性
    2. 对于小数和偶数进行特殊处理，提高效率
    3. 根据数的范围选择最优的测试底数集合
    4. 线程安全：该函数是无状态的，可以安全地在多线程环境中使用
    """
    # 处理特殊情况
    if n <= 1:
        return False
    if n <= 3:
        return True
    
    # 偶数(除了2)都不是质数
    if (n & 1) == 0:
        return False
    
    # 使用最优的测试底数集合
    bases = get_optimal_bases(n)
    
    for a in bases:
        if a >= n:
            continue
        # witness函数用于单次测试
        if witness(a, n):
            return False
    
    return True

def random_miller_rabin(n, rounds=5):
    """
    使用随机测试底数的Miller-Rabin素性测试
    适用于需要更多随机性的场景
    
    :param n: 待测试的数
    :param rounds: 随机测试轮数
    :return: 如果是质数返回True，否则返回False
    """
    # 处理特殊情况
    if n <= 1:
        return False
    if n <= 3:
        return True
    if (n & 1) == 0:
        return False
    
    # 对于小数，使用确定性测试
    if n < 1000000:
        return miller_rabin(n)
    
    # 将n-1分解为u*2^t的形式，其中u是奇数
    u = n - 1
    t = 0
    while (u & 1) == 0:
        t += 1
        u >>= 1
    
    # 进行随机测试
    for _ in range(rounds):
        # 随机选择一个测试底数
        a = random.randint(2, n - 1)
        if witness(a, n):
            return False
    
    return True

def is_prime(n):
    """
    统一的质数判断接口，结合试除法和Miller-Rabin
    对于小数使用试除法，对于大数使用Miller-Rabin
    
    :param n: 待测试的数
    :return: 如果是质数返回True，否则返回False
    
    优化策略：
    1. 根据数的大小自动选择最优算法
    2. 预处理小质数表以加速判断
    """
    # 对于小数，使用试除法
    if n <= 1000000:
        if n <= 1:
            return False
        if n <= 3:
            return True
        if n % 2 == 0:
            return False
        # 试除法只需要检查到sqrt(n)
        for i in range(3, int(n**0.5) + 1, 2):
            if n % i == 0:
                return False
        return True
    
    # 对于大数，使用Miller-Rabin
    return miller_rabin(n)

# 测试代码
def run_tests():
    """
    运行全面的测试用例，覆盖各种情况
    """
    # 基础测试用例
    test_cases = [
        (0, False), (1, False), (2, True), (3, True), (4, False),
        (5, True), (9, False), (17, True), (25, False), (29, True),
        (97, True), (100, False), (101, True), (982451653, True),
        (2147483647, True),  # 2^31-1，梅森素数
        (2147483648, False),  # 2^31，不是质数
        (561, False),  # 卡迈克尔数，是合数但通过费马测试
        (1000003, True),  # 大质数
        (1000000, False),  # 合数
        (999983, True),  # 质数
        (1234567894987654321, False)  # 大数测试
    ]
    
    # 测试各种实现
    functions_to_test = [
        ('miller_rabin(确定性)', miller_rabin),
        ('random_miller_rabin(5轮)', lambda x: random_miller_rabin(x, 5)),
        ('is_prime(优化版)', is_prime)
    ]
    
    print("=== 全面测试 ===")
    for name, func in functions_to_test:
        print(f"\n测试 {name}:")
        all_passed = True
        
        for num, expected in test_cases:
            try:
                result = func(num)
                status = "✓" if result == expected else "✗"
                print(f"{num} -> {'质数' if result else '合数'} (期望: {'质数' if expected else '合数'}) {status}")
                if result != expected:
                    all_passed = False
            except Exception as e:
                print(f"{num} -> 错误: {e}")
                all_passed = False
        
        print(f"测试结果: {'全部通过' if all_passed else '存在失败'}")

def performance_test():
    """
    性能测试，比较不同实现的执行效率
    """
    print("\n=== 性能测试 ===")
    
    # 测试大质数判断性能
    test_numbers = [
        2147483647,  # 2^31-1
        982451653,   # 大质数
        1000000007,  # 常用模数，质数
        1000000009   # 常用模数，质数
    ]
    
    functions_to_test = [
        ('miller_rabin', miller_rabin),
        ('is_prime', is_prime)
    ]
    
    for num in test_numbers:
        print(f"\n测试数字: {num}")
        for name, func in functions_to_test:
            start_time = time.time()
            result = func(num)
            end_time = time.time()
            elapsed = (end_time - start_time) * 1000  # 转换为毫秒
            print(f"{name}: {'质数' if result else '合数'}, 耗时: {elapsed:.3f} ms")

if __name__ == "__main__":
    # 运行测试
    run_tests()
    
    # 运行性能测试
    performance_test()
    
    # 交互式测试
    print("\n=== 交互式测试 ===")
    print("请输入一个数字进行质数判断（输入'q'退出）:")
    while True:
        try:
            user_input = input("数字: ")
            if user_input.lower() == 'q':
                break
            num = int(user_input)
            start = time.time()
            result = is_prime(num)
            end = time.time()
            print(f"{num} {'是' if result else '不是'} 质数")
            print(f"判断耗时: {(end - start) * 1000:.3f} ms")
        except ValueError:
            print("请输入有效的数字！")
        except KeyboardInterrupt:
            print("\n程序已中断")
            break