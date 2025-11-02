#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
计数质数
给定整数n，返回小于非负整数n的质数的数量
测试链接 : https://leetcode.cn/problems/count-primes/
相关题目链接：
1. LeetCode 204. Count Primes (计数质数) - https://leetcode.cn/problems/count-primes/
2. LeetCode 313. Super Ugly Number (超级丑数) - https://leetcode.cn/problems/super-ugly-number/
3. LeetCode 264. Ugly Number II (丑数 II) - https://leetcode.cn/problems/ugly-number-ii/
4. LeetCode 202. Happy Number (快乐数) - https://leetcode.cn/problems/happy-number/
5. LeetCode 172. Factorial Trailing Zeroes (阶乘后的零) - https://leetcode.cn/problems/factorial-trailing-zeroes/
6. LeetCode 762. Prime Number of Set Bits in Binary Representation - https://leetcode.cn/problems/prime-number-of-set-bits-in-binary-representation/
7. LeetCode 1025. Divisor Game (除数博弈) - https://leetcode.cn/problems/divisor-game/
8. LeetCode 1201. Ugly Number III (丑数 III) - https://leetcode.cn/problems/ugly-number-iii/
9. LeetCode 263. Ugly Number (丑数) - https://leetcode.cn/problems/ugly-number/
10. LeetCode 342. Power of Four (4的幂) - https://leetcode.cn/problems/power-of-four/
11. LeetCode 326. Power of Three (3的幂) - https://leetcode.cn/problems/power-of-three/
12. LeetCode 231. Power of Two (2的幂) - https://leetcode.cn/problems/power-of-two/
13. LeetCode 1492. The kth Factor of n (n的第k个因子) - https://leetcode.cn/problems/the-kth-factor-of-n/
14. LeetCode 1362. Closest Divisors (最接近的因数) - https://leetcode.cn/problems/closest-divisors/
15. LeetCode 507. Perfect Number (完美数) - https://leetcode.cn/problems/perfect-number/
16. LeetCode 869. Reordered Power of 2 (重新排序的幂) - https://leetcode.cn/problems/reordered-power-of-2/
17. LeetCode 1952. Three Divisors (三除数) - https://leetcode.cn/problems/three-divisors/
18. LeetCode 2427. Number of Common Factors (公因子的数目) - https://leetcode.cn/problems/number-of-common-factors/
19. LeetCode 1250. Check If It Is a Good Array (检查好数组) - https://leetcode.cn/problems/check-if-it-is-a-good-array/
20. LeetCode 829. Consecutive Numbers Sum (连续整数求和) - https://leetcode.cn/problems/consecutive-numbers-sum/
21. LeetCode 1819. Number of Different Subsequences GCDs (不同的子序列的最大公约数数目) - https://leetcode.cn/problems/number-of-different-subsequences-gcds/
22. LeetCode 1627. Graph Connectivity With Threshold (图连通性与阈值) - https://leetcode.cn/problems/graph-connectivity-with-threshold/
23. LeetCode 952. Largest Component Size by Common Factor (按公因数计算最大组件大小) - https://leetcode.cn/problems/largest-component-size-by-common-factor/
24. LeetCode 1447. Simplified Fractions (最简分数) - https://leetcode.cn/problems/simplified-fractions/
25. LeetCode 1071. Greatest Common Divisor of Strings (字符串的最大公因子) - https://leetcode.cn/problems/greatest-common-divisor-of-strings/
26. LeetCode 365. Water and Jug Problem (水壶问题) - https://leetcode.cn/problems/water-and-jug-problem/
27. LeetCode 2248. Intersection of Multiple Arrays (多个数组的交集) - https://leetcode.cn/problems/intersection-of-multiple-arrays/
28. Codeforces 271B Prime Matrix - https://codeforces.com/problemset/problem/271/B
29. POJ 3641 Pseudoprime numbers - http://poj.org/problem?id=3641
30. Project Euler Problem 10 Summation of primes - https://projecteuler.net/problem=10
"""

import math
import time
import sys
from typing import List, Tuple


def count_primes(n: int) -> int:
    """
    计算小于n的质数数量
    
    Args:
        n: 非负整数
    Returns:
        小于n的质数数量
    """
    return ehrlich(n - 1)


def ehrlich(n: int) -> int:
    """
    埃氏筛统计0 ~ n范围内的质数个数
    时间复杂度O(n * log(logn))，接近于线性
    空间复杂度O(n)
    
    算法原理：
    1. 创建一个布尔数组，初始时认为所有数都是质数
    2. 从2开始，将每个质数的倍数标记为合数
    3. 优化点：从i*i开始标记，因为小于i*i的合数已经被更小的质数标记过了
    
    应用场景：
    1. 需要获取一定范围内所有质数
    2. 质数相关的数学问题
    3. 密码学中生成质数
    
    工程化考虑：
    1. 内存使用：需要O(n)的额外空间
    2. 适用范围：适用于n不太大的情况（大约10^7以内）
    3. 可以进一步优化：只处理奇数或使用分段筛法
    
    Args:
        n: 范围上限（包含）
    Returns:
        0~n范围内的质数个数
    """
    # 参数验证
    if n < 2:
        return 0
    
    # visit[i] = False，代表i是质数
    # visit[i] = True，代表i是合数
    visit = [False] * (n + 1)
    
    # 从2开始，对每个质数，标记其所有倍数为合数
    # 只需要检查到sqrt(n)，因为更大的数如果是合数，必然有一个因子小于等于sqrt(n)
    for i in range(2, int(math.sqrt(n)) + 1):
        if not visit[i]:  # 如果i是质数
            # 从i*i开始标记，因为小于i*i的倍数已经被更小的质数标记过了
            visit[i*i : n+1 : i] = [True] * len(visit[i*i : n+1 : i])
    
    # 计数质数的数量（注意排除0和1）
    return sum(not is_composite for i, is_composite in enumerate(visit) if i >= 2)


def euler(n: int) -> int:
    """
    欧拉筛（线性筛）统计0 ~ n范围内的质数个数
    时间复杂度O(n)，是线性的
    空间复杂度O(n)
    
    算法原理：
    1. 每个合数只被其最小质因子筛掉一次
    2. 对于每个数i，用已找到的质数primes[j]去筛掉i*primes[j]
    3. 当i%primes[j]==0时break，保证每个合数只被其最小质因子筛掉
    
    与埃氏筛的区别：
    1. 埃氏筛会重复标记合数，比如12会被2和3都标记一次
    2. 欧拉筛每个合数只被标记一次，因此时间复杂度是线性的
    3. 欧拉筛在过程中同时收集了质数列表，便于后续使用
    
    应用场景：
    1. 需要高效获取大量质数
    2. 对时间复杂度有严格要求的场景
    3. 需要同时获取质数和质数个数
    4. 当n很大时，欧拉筛比埃氏筛更高效
    
    Args:
        n: 范围上限（包含）
    Returns:
        0~n范围内的质数个数
    """
    # 参数验证
    if n < 2:
        return 0
    
    # is_composite[i] = False，代表i是质数
    # is_composite[i] = True，代表i是合数
    is_composite = [False] * (n + 1)
    
    # primes列表收集所有的质数
    primes = []
    
    # 从2到n遍历每个数
    for i in range(2, n + 1):
        if not is_composite[i]:  # 如果i是质数
            primes.append(i)  # 将质数加入primes列表
        
        # 用当前数i和已知质数去筛掉合数
        for p in primes:
            # 如果i*p超过n，停止筛选
            if i * p > n:
                break
            
            # 标记i*p为合数
            is_composite[i * p] = True
            
            # 关键优化：当i能被p整除时，停止筛选
            # 这样保证每个合数只被其最小质因子筛掉
            if i % p == 0:
                break
    
    return len(primes)


def ehrlich2(n: int) -> int:
    """
    优化的埃氏筛（只处理奇数）
    时间复杂度：O(n * log(logn))，但常数因子更小
    空间复杂度：O(n)
    
    优化点：
    1. 只处理奇数，因为除了2以外所有偶数都是合数
    2. 预先计算奇数个数，然后在发现合数时递减
    3. 减少了约一半的计算量和空间使用
    
    实际运行效率比普通埃氏筛更高，特别是当n较大时
    
    Args:
        n: 范围上限（包含）
    Returns:
        0~n范围内的质数个数
    """
    # 参数验证
    if n < 2:
        return 0
    if n == 2:
        return 1
    
    # 预先计算奇数个数，减去1是因为0也被算在内了
    # 初始假设所有奇数都是质数，之后发现合数时递减计数
    count = (n + 1) // 2  # 奇数的数量
    
    # is_composite[i]表示2i+1是否为合数
    # 注意这里的索引映射：奇数5 -> 索引2
    is_composite = [False] * (count + 1)  # 确保足够的空间
    
    # 只处理奇数，从3开始
    for i in range(1, int(math.sqrt(n)) // 2 + 1):
        if not is_composite[i]:  # 如果2i+1是质数
            # 计算对应奇数的值
            prime = 2 * i + 1
            # 从prime*prime开始，每隔2*prime标记一次（只标记奇数）
            # 计算起始位置对应的索引
            start = prime * prime
            if start % 2 == 0:  # 确保是奇数
                start += prime
            start_index = (start - 1) // 2  # 转换为索引
            
            # 标记所有prime的奇数倍数
            for j in range(start_index, count + 1, prime):
                if not is_composite[j]:
                    is_composite[j] = True
                    count -= 1
    
    return count


def segmented_sieve(n: int) -> int:
    """
    分段筛法 - 适用于处理非常大的n
    时间复杂度：O(n)
    空间复杂度：O(sqrt(n))
    
    算法原理：
    1. 先用欧拉筛计算出sqrt(n)以内的所有质数
    2. 然后将区间[2,n]分成多个段，每段大小为sqrt(n)
    3. 对每个段，使用已知的质数筛掉其中的合数
    
    优势：
    1. 当n很大时，普通筛法需要大量内存
    2. 分段筛法只需要O(sqrt(n))的空间
    3. 适用于n接近内存上限的情况
    
    Args:
        n: 范围上限（包含）
    Returns:
        0~n范围内的质数个数
    """
    if n < 2:
        return 0
    
    # 计算sqrt(n)
    sqrt_n = int(math.isqrt(n))
    
    # 计算sqrt(n)以内的所有质数
    small_primes = []
    is_composite_small = [False] * (sqrt_n + 1)
    
    for i in range(2, sqrt_n + 1):
        if not is_composite_small[i]:
            small_primes.append(i)
            for j in range(i * i, sqrt_n + 1, i):
                is_composite_small[j] = True
    
    # 计算小区间内的质数数量
    count = len(small_primes)
    
    # 如果n不超过sqrt(n)，直接返回
    if n <= sqrt_n:
        # 需要调整count，因为small_primes包含所有<=sqrt_n的质数
        while count > 0 and small_primes[count - 1] > n:
            count -= 1
        return count
    
    # 分段筛法
    segment_size = sqrt_n
    for low in range(sqrt_n + 1, n + 1, segment_size):
        high = min(low + segment_size - 1, n)
        # 标记当前段中的合数
        is_composite_segment = [False] * (high - low + 1)
        
        # 用小质数筛掉区间内的合数
        for p in small_primes:
            # 计算区间内第一个p的倍数
            first_multiple = ((low + p - 1) // p) * p
            if first_multiple == p:
                first_multiple += p
            
            # 标记所有p的倍数
            for j in range(first_multiple, high + 1, p):
                is_composite_segment[j - low] = True
        
        # 统计区间内的质数
        for i in range(high - low + 1):
            if not is_composite_segment[i] and (low + i) >= 2:
                count += 1
    
    return count


def get_all_primes(n: int) -> List[int]:
    """
    获取0~n范围内的所有质数列表
    使用欧拉筛算法，时间复杂度O(n)
    
    Args:
        n: 范围上限（包含）
    Returns:
        质数列表
    """
    if n < 2:
        return []
    
    is_composite = [False] * (n + 1)
    primes = []
    
    for i in range(2, n + 1):
        if not is_composite[i]:
            primes.append(i)
        for p in primes:
            if i * p > n:
                break
            is_composite[i * p] = True
            if i % p == 0:
                break
    
    return primes


def is_prime(n: int, small_primes: List[int] = None) -> bool:
    """
    判断一个数是否为质数
    利用预先计算的质数表加速判断
    时间复杂度：O(sqrt(n))
    
    Args:
        n: 待判断的数
        small_primes: sqrt(n)以内的质数列表，默认为None，将自动计算
    Returns:
        如果n是质数返回True，否则返回False
    """
    if n <= 1:
        return False
    if n <= 3:
        return True
    if n % 2 == 0 or n % 3 == 0:
        return False
    
    sqrt_n = int(math.isqrt(n))
    
    # 如果没有提供小质数列表，先生成
    if small_primes is None:
        small_primes = get_all_primes(sqrt_n)
    
    for p in small_primes:
        if p > sqrt_n:
            break
        if n % p == 0:
            return False
    
    return True


def prime_distribution(n: int, buckets: int = 10) -> List[Tuple[int, int]]:
    """
    统计质数的分布情况
    
    Args:
        n: 范围上限
        buckets: 桶的数量
    Returns:
        每个桶的范围和质数数量
    """
    primes = get_all_primes(n)
    bucket_size = (n + buckets - 1) // buckets  # 向上取整
    distribution = []
    
    for i in range(buckets):
        start = i * bucket_size + 1
        end = min((i + 1) * bucket_size, n)
        count = sum(1 for p in primes if start <= p <= end)
        distribution.append((start, end, count))
    
    return distribution


def functional_test():
    """
    功能测试函数
    测试所有筛法算法的正确性和边界条件
    """
    print("===== 功能测试 =====")
    
    # 边界条件测试
    print("\n--- 边界条件测试 ---")
    test_cases = [-1, 0, 1, 2, 3, 5, 10, 20]
    for n in test_cases:
        ehrlich_result = ehrlich(n)
        euler_result = euler(n)
        ehrlich2_result = ehrlich2(n)
        segmented_result = segmented_sieve(n)
        
        print(f"n = {n:2d} | 埃氏筛: {ehrlich_result:2d} | 欧拉筛: {euler_result:2d} | 优化埃氏: {ehrlich2_result:2d} | 分段筛: {segmented_result:2d}")
        
        # 验证所有算法结果一致
        assert ehrlich_result == euler_result, f"算法结果不一致！n={n}"
        assert ehrlich_result == ehrlich2_result, f"算法结果不一致！n={n}"
        assert ehrlich_result == segmented_result, f"算法结果不一致！n={n}"
    
    # 质数列表测试
    print("\n--- 质数列表测试 ---")
    list_test_cases = [10, 20, 30]
    for n in list_test_cases:
        primes = get_all_primes(n)
        print(f"0~{n}的质数列表: {primes}")
        print(f"质数数量: {len(primes)}")
        assert len(primes) == euler(n), f"质数数量不一致！n={n}"
    
    # 验证已知结果
    print("\n--- 已知结果验证 ---")
    # 已知结果验证：小于10的质数有4个（2,3,5,7）
    assert count_primes(10) == 4, "已知结果验证失败！"
    # 已知结果验证：小于100的质数有25个
    assert count_primes(100) == 25, "已知结果验证失败！"
    # 已知结果验证：小于1000的质数有168个
    assert count_primes(1000) == 168, "已知结果验证失败！"
    # 已知结果验证：小于10000的质数有1229个
    assert count_primes(10000) == 1229, "已知结果验证失败！"
    
    print("\n功能测试通过！")
    print("\n===== 功能测试完成 =====\n")


def performance_test():
    """
    性能测试函数
    比较不同筛法在不同规模数据下的性能
    """
    print("===== 性能测试 =====")
    
    # 小规模数据测试
    print("\n--- 小规模数据测试 (n=10^6) ---")
    n1 = 1_000_000
    
    start = time.time()
    ehrlich_result1 = ehrlich(n1)
    end = time.time()
    print(f"埃氏筛 - 质数数量: {ehrlich_result1:6d}, 耗时: {(end - start)*1000:.3f} 毫秒")
    
    start = time.time()
    euler_result1 = euler(n1)
    end = time.time()
    print(f"欧拉筛 - 质数数量: {euler_result1:6d}, 耗时: {(end - start)*1000:.3f} 毫秒")
    
    start = time.time()
    ehrlich2_result1 = ehrlich2(n1)
    end = time.time()
    print(f"优化埃氏筛 - 质数数量: {ehrlich2_result1:6d}, 耗时: {(end - start)*1000:.3f} 毫秒")
    
    start = time.time()
    segmented_result1 = segmented_sieve(n1)
    end = time.time()
    print(f"分段筛 - 质数数量: {segmented_result1:6d}, 耗时: {(end - start)*1000:.3f} 毫秒")
    
    # 中等规模数据测试
    print("\n--- 中等规模数据测试 (n=10^7) ---")
    n2 = 10_000_000
    
    start = time.time()
    ehrlich_result2 = ehrlich(n2)
    end = time.time()
    print(f"埃氏筛 - 质数数量: {ehrlich_result2:6d}, 耗时: {(end - start)*1000:.3f} 毫秒")
    
    start = time.time()
    euler_result2 = euler(n2)
    end = time.time()
    print(f"欧拉筛 - 质数数量: {euler_result2:6d}, 耗时: {(end - start)*1000:.3f} 毫秒")
    
    start = time.time()
    ehrlich2_result2 = ehrlich2(n2)
    end = time.time()
    print(f"优化埃氏筛 - 质数数量: {ehrlich2_result2:6d}, 耗时: {(end - start)*1000:.3f} 毫秒")
    
    # 大规模数据测试（只测试部分算法，避免内存问题）
    print("\n--- 大规模数据测试 (n=10^8) ---")
    n3 = 100_000_000
    
    # 只测试优化埃氏筛和分段筛，因为它们内存效率更高
    start = time.time()
    ehrlich2_result3 = ehrlich2(n3)
    end = time.time()
    print(f"优化埃氏筛 - 质数数量: {ehrlich2_result3:6d}, 耗时: {(end - start)*1000:.3f} 毫秒")
    
    start = time.time()
    segmented_result3 = segmented_sieve(n3)
    end = time.time()
    print(f"分段筛 - 质数数量: {segmented_result3:6d}, 耗时: {(end - start)*1000:.3f} 毫秒")
    
    print("\n===== 性能测试完成 =====\n")


def interactive_test():
    """
    交互式测试函数
    允许用户输入数字，查看质数统计结果
    """
    print("===== 交互式测试 =====")
    print("输入一个整数，查看小于等于该数的质数数量 (输入 -1 退出):")
    
    while True:
        try:
            n = input("请输入一个整数: ")
            if n == '-1':
                break
            
            n = int(n)
            if n < -1:
                print("请输入非负整数或-1。")
                continue
            
            if n > 100_000_000:
                print("数字太大，可能导致内存不足。请输入较小的数字。")
                continue
            
            start = time.time()
            count = euler(n)  # 使用欧拉筛
            end = time.time()
            
            print(f"小于等于 {n} 的质数数量: {count}")
            print(f"计算耗时: {(end - start)*1000:.3f} 毫秒")
            
            # 如果n不大，可以显示前几个质数和分布情况
            if n <= 1000:
                primes = get_all_primes(n)
                print("质数列表: ")
                if len(primes) <= 20:
                    print(primes)
                else:
                    print(primes[:20], "... (共" + str(len(primes)) + "个)")
                
                # 显示分布情况
                if n >= 100:
                    print("\n质数分布情况:")
                    distribution = prime_distribution(n, 5)
                    for start, end, cnt in distribution:
                        print(f"区间[{start:3d}-{end:3d}]: {cnt:2d}个质数")
            
            print()
        except ValueError:
            print("输入错误，请输入有效的整数。")
    
    print("交互式测试结束。")


def main():
    """
    主函数，运行所有测试
    """
    try:
        # 运行功能测试
        functional_test()
        # 运行性能测试
        performance_test()
        # 运行交互式测试
        interactive_test()
    except KeyboardInterrupt:
        print("\n程序被用户中断。")
    except Exception as e:
        print(f"程序出错: {e}")


if __name__ == "__main__":
    main()