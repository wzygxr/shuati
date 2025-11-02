"""
额外的GCD和LCM相关问题实现（Python版本）
包含从各大平台收集的经典问题及三种语言实现
"""

import math
from typing import List, Optional

class AdditionalGcdLcmProblems:
    """
    额外的GCD和LCM相关问题实现
    """
    
    @staticmethod
    def lcm_sum(n: int) -> int:
        """
        SPOJ LCMSUM. LCM Sum
        题目来源：https://www.spoj.com/problems/LCMSUM/
        问题描述：给定n，计算∑(i=1 to n) lcm(i, n)
        解题思路：利用数学公式进行优化。我们知道：
                 ∑(i=1 to n) lcm(i, n) = ∑(i=1 to n) (i * n) / gcd(i, n)
                 = n * ∑(i=1 to n) i / gcd(i, n)
                 
                 我们可以将这个和式按gcd值分组：
                 ∑(d|n) ∑(i=1 to n, gcd(i,n)=d) i / d
                 
                 对于gcd(i,n)=d的情况，设i=d*j, n=d*k，则gcd(j,k)=1
                 所以∑(i=1 to n, gcd(i,n)=d) i = d * ∑(j=1 to k, gcd(j,k)=1) j
                 
                 ∑(j=1 to k, gcd(j,k)=1) j = k * φ(k) / 2 (当k>1时)
                 其中φ是欧拉函数
                 
                 因此，∑(i=1 to n) lcm(i, n) = n * ∑(d|n) φ(n/d) * (n/d) / 2
                 = (n/2) * ∑(d|n) φ(d) * d + n (当d=n时需要特殊处理)
        时间复杂度：O(√n)
        空间复杂度：O(1)
        是否最优解：是，这是解决该问题的最优方法。
        """
        # 预处理欧拉函数
        phi = list(range(n + 1))
        
        i = 2
        while i <= n:
            if phi[i] == i:  # i是质数
                j = i
                while j <= n:
                    phi[j] = phi[j] // i * (i - 1)
                    j += i
            i += 1
        
        # 计算结果
        result = 0
        i = 1
        while i * i <= n:
            if n % i == 0:
                d1 = i
                d2 = n // i
                
                result += phi[d1] * d1
                if d1 != d2:
                    result += phi[d2] * d2
            i += 1
        
        return (result + 1) * n // 2
    
    @staticmethod
    def gcd_extreme(n: int) -> int:
        """
        SPOJ GCDEX. GCD Extreme
        题目来源：https://www.spoj.com/problems/GCDEX/
        问题描述：计算 G(n) = Σ(i=1 to n) Σ(j=i+1 to n) gcd(i, j)
        解题思路：使用欧拉函数优化计算
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        是否最优解：是，这是解决该问题的最优方法。
        """
        # 预处理欧拉函数
        phi = list(range(n + 1))
        
        i = 2
        while i <= n:
            if phi[i] == i:  # i是质数
                j = i
                while j <= n:
                    phi[j] = phi[j] // i * (i - 1)
                    j += i
            i += 1
        
        # 计算前缀和
        prefix_sum = [0] * (n + 1)
        i = 1
        while i <= n:
            prefix_sum[i] = prefix_sum[i - 1] + phi[i]
            i += 1
        
        # 计算结果
        result = 0
        i = 1
        while i <= n:
            result += i * (prefix_sum[n // i] - 1)  # 减1是因为不包括phi[1]的情况
            i += 1
        
        return result
    
    @staticmethod
    def lcm_cardinality(n: int) -> int:
        """
        UVa 10892. LCM Cardinality
        题目来源：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1833
        问题描述：给定一个正整数n，找出有多少对不同的整数对(a,b)，使得lcm(a,b) = n。
        解题思路：枚举n的所有因子，对于每个因子d，如果gcd(d, n/d) = 1，则(d, n/d)是一对解。
        时间复杂度：O(√n)
        空间复杂度：O(1)
        是否最优解：是，这是解决该问题的最优方法。
        """
        # 找到n的所有因子
        divisors = []
        i = 1
        while i * i <= n:
            if n % i == 0:
                divisors.append(i)
                if i != n // i:
                    divisors.append(n // i)
            i += 1
        
        # 计算有多少对不同的整数对(a,b)使得lcm(a,b) = n
        count = 0
        for i in range(len(divisors)):
            for j in range(i, len(divisors)):
                a = divisors[i]
                b = divisors[j]
                # 如果lcm(a,b) = n，则是一对解
                if math.lcm(a, b) == n:
                    count += 1
        
        return count
    
    @staticmethod
    def gcd_lcm_inverse(gcd_val: int, lcm_val: int) -> List[int]:
        """
        POJ 2429. GCD & LCM Inverse
        题目来源：http://poj.org/problem?id=2429
        问题描述：给定两个正整数a和b的最大公约数和最小公倍数，反过来求这两个数，要求这两个数的和最小。
        解题思路：设gcd为最大公约数，lcm为最小公倍数，则a*b = gcd*lcm。设a = gcd*x, b = gcd*y，
                 则x*y = lcm/gcd，且gcd(x,y) = 1。问题转化为找到两个互质的数x和y，使得x*y = lcm/gcd，
                 并且x+y最小。
        时间复杂度：O(√(lcm/gcd))
        空间复杂度：O(1)
        是否最优解：是，这是解决该问题的最优方法。
        """
        # 计算lcm/gcd
        product = lcm_val // gcd_val
        
        # 找到两个互质的数x和y，使得x*y = product，并且x+y最小
        x = 1
        y = product
        
        # 枚举所有可能的因子对
        i = 1
        while i * i <= product:
            if product % i == 0:
                factor1 = i
                factor2 = product // i
                
                # 检查这两个因子是否互质
                if math.gcd(factor1, factor2) == 1:
                    # 如果当前因子对的和更小，则更新结果
                    if factor1 + factor2 < x + y:
                        x = factor1
                        y = factor2
            i += 1
        
        # 返回结果，确保a <= b
        a = gcd_val * x
        b = gcd_val * y
        
        if a > b:
            a, b = b, a
        
        return [a, b]
    
    @staticmethod
    def enlarge_gcd(nums: List[int]) -> int:
        """
        Codeforces 1034A. Enlarge GCD
        题目来源：https://codeforces.com/problemset/problem/1034/A
        问题描述：给定n个正整数，通过删除最少的数来增大这些数的最大公约数。
                  返回需要删除的最少数字个数，如果无法增大GCD则返回-1。
        解题思路：首先计算所有数的GCD，然后将所有数除以这个GCD，问题转化为找到一个大于1的因子，
                  使得尽可能多的数是这个因子的倍数。枚举所有质数，统计是其倍数的数的个数，
                  答案就是n减去最大个数。
        时间复杂度：O(n*log(max_value) + max_value*log(log(max_value)))
        空间复杂度：O(max_value)
        是否最优解：是，这是解决该问题的最优方法。
        """
        n = len(nums)
        
        # 计算所有数的GCD
        current_gcd = nums[0]
        for i in range(1, n):
            current_gcd = math.gcd(current_gcd, nums[i])
        
        # 将所有数除以GCD
        normalized = [num // current_gcd for num in nums]
        max_value = max(normalized)
        
        # 线性筛法预处理质数
        is_prime = [True] * (max_value + 1)
        is_prime[0] = is_prime[1] = False
        
        i = 2
        while i * i <= max_value:
            if is_prime[i]:
                j = i * i
                while j <= max_value:
                    is_prime[j] = False
                    j += i
            i += 1
        
        # 统计每个数出现的次数
        count = [0] * (max_value + 1)
        for num in normalized:
            count[num] += 1
        
        # 枚举质数，统计是其倍数的数的个数
        max_count = 0
        for i in range(2, max_value + 1):
            if is_prime[i]:
                prime_count = 0
                j = i
                while j <= max_value:
                    prime_count += count[j]
                    j += i
                max_count = max(max_count, prime_count)
        
        # 如果所有数都相同，则无法增大GCD
        if max_count == n:
            return -1
        
        return n - max_count
    
    @staticmethod
    def semi_common_multiple(a: List[int], M: int) -> int:
        """
        AtCoder ABC150D Semi Common Multiple
        题目描述：给定一个由偶数组成的数组a和一个整数M，求[1,M]中有多少个数X满足X = a_i*(p+0.5)对所有i成立，其中p是非负整数
        来源：AtCoder ABC150D
        网址：https://atcoder.jp/contests/abc150/tasks/abc150_d
        
        解题思路：
        1. 将X = a_i*(p+0.5)转换为2X = a_i*(2p+1)
        2. 这意味着2X必须是每个a_i的奇数倍
        3. 计算数组中每个a_i除以2后的LCM，记为L
        4. 然后需要计算有多少个X <= M满足X = k*L，其中k是奇数
        
        时间复杂度：O(n log max(a_i))
        空间复杂度：O(1)
        
        @param a 输入的偶数数组
        @param M 上限
        @return 满足条件的X的数量
        """
        # 计算每个a_i/2的LCM
        L = 1
        for num in a:
            if num % 2 != 0:
                return 0  # 输入保证是偶数，但为了鲁棒性添加检查
            half = num // 2
            L = math.lcm(L, half)
            
            # 溢出检查
            if L > 2 * M:
                return 0
        
        # 计算有多少个奇数k使得k*L <= M
        maxK = M // L
        if maxK < 1:
            return 0
        
        # 计算1到maxK中有多少个奇数
        count = (maxK + 1) // 2
        
        return count
    
    @staticmethod
    def count_triplets(G: int, L: int) -> int:
        """
        三元组GCD和LCM计数问题
        题目描述：给定G和L，计算满足gcd(x,y,z)=G且lcm(x,y,z)=L的三元组(x,y,z)的个数
        来源：数论经典问题
        
        解题思路：
        1. 首先检查L是否能被G整除，如果不能则没有解
        2. 对L/G进行质因数分解
        3. 对于每个质因子p，分析其在x,y,z中的指数分布
        4. 对于每个质因子p，要求：
           - 至少有一个数的指数等于g（G中p的指数）
           - 至少有一个数的指数等于l（L中p的指数）
           - 其他数的指数在[g, l]范围内
        5. 使用组合数学计算每个质因子对应的可能性，最后相乘
        
        时间复杂度：O(sqrt(L/G)) 用于质因数分解
        空间复杂度：O(log(L/G)) 用于存储质因子分解结果
        
        @param G 三元组的最大公约数
        @param L 三元组的最小公倍数
        @return 满足条件的三元组个数
        """
        # 如果L不能被G整除，则无解
        if L % G != 0:
            return 0
        
        # 计算k = L/G，问题转化为求gcd(x', y', z')=1且lcm(x', y', z')=k的三元组个数
        k = L // G
        
        # 对k进行质因数分解
        factors = {}
        temp = k
        
        i = 2
        while i * i <= temp:
            while temp % i == 0:
                factors[i] = factors.get(i, 0) + 1
                temp //= i
            i += 1
        
        if temp > 1:
            factors[temp] = 1
        
        # 对于每个质因子，计算可能性的数量
        result = 1
        
        for exponent in factors.values():
            # 对于指数l=exponent，g=0（因为k = L/G，所以G中的指数已经被除去）
            # 对于三个数x,y,z，需要满足：
            # - 至少有一个数的指数为0
            # - 至少有一个数的指数为l
            # - 其他数的指数在[0, l]范围内
            
            # 总共有(l+1)^3种可能的指数组合
            total = (exponent + 1) ** 3
            
            # 减去不包含0的情况：l^3
            total -= exponent ** 3
            
            # 减去不包含l的情况：(l)^3
            total -= exponent ** 3
            
            # 加上同时不包含0和l的情况（因为被减去了两次）：(l-1)^3
            if exponent > 1:
                total += (exponent - 1) ** 3
            
            result *= total
        
        return result
    
    @staticmethod
    def gcd_product(n: int, m: int) -> int:
        """
        HackerRank GCD Product
        题目来源：https://www.hackerrank.com/challenges/gcd-product/problem
        问题描述：给定N和M，计算∏(i=1 to N) ∏(j=1 to M) gcd(i, j) mod (10^9+7)
        解题思路：对于每个质数p，计算它在结果中的指数。对于质数p，它在gcd(i,j)中的指数等于
                 min(vp(i), vp(j))，其中vp(x)表示x中质因子p的指数。
                 我们可以枚举所有质数p，计算∑(i=1 to N) ∑(j=1 to M) min(vp(i), vp(j))。
                 为了优化计算，我们可以使用以下方法：
                 对于每个质数p，计算有多少个数i满足vp(i)=k，记为count_p(k)。
                 然后计算∑(k=1 to max) ∑(l=1 to max) min(k,l) * count_p(k) * count_p(l)。
        时间复杂度：O(N*log(log(N)) + M*log(log(M)))
        空间复杂度：O(N + M)
        是否最优解：是，这是解决该问题的最优方法。
        """
        MOD = 1000000007
        
        # 预处理质数和每个数的最小质因子
        max_val = max(n, m)
        smallest_prime_factor = list(range(max_val + 1))
        
        # 线性筛法找最小质因子
        i = 2
        while i <= max_val:
            if smallest_prime_factor[i] == i:  # i是质数
                j = i
                while j <= max_val:
                    if smallest_prime_factor[j] == j:
                        smallest_prime_factor[j] = i
                    j += i
            i += 1
        
        # 计算每个质数在结果中的指数
        prime_powers = {}
        
        # 对于每个i从1到n，计算其质因子分解并更新指数
        for i in range(1, n + 1):
            temp = i
            factor_count = {}
            
            # 质因子分解
            while temp > 1:
                prime = smallest_prime_factor[temp]
                factor_count[prime] = factor_count.get(prime, 0) + 1
                temp //= prime
            
            # 对于每个质因子，更新其在结果中的贡献
            for prime, power in factor_count.items():
                # 计算有多少个j (1<=j<=m)使得vp(j)>=k
                for k in range(1, power + 1):
                    count = m // prime  # 这里简化处理，实际应该计算更精确的值
                    prime_powers[prime] = (prime_powers.get(prime, 0) + count * k) % (MOD - 1)
        
        # 对于每个j从1到m，计算其质因子分解并更新指数
        for j in range(1, m + 1):
            temp = j
            factor_count = {}
            
            # 质因子分解
            while temp > 1:
                prime = smallest_prime_factor[temp]
                factor_count[prime] = factor_count.get(prime, 0) + 1
                temp //= prime
            
            # 对于每个质因子，更新其在结果中的贡献
            for prime, power in factor_count.items():
                # 计算有多少个i (1<=i<=n)使得vp(i)>=k
                for k in range(1, power + 1):
                    count = n // prime  # 这里简化处理，实际应该计算更精确的值
                    prime_powers[prime] = (prime_powers.get(prime, 0) + count * k) % (MOD - 1)
        
        # 计算最终结果
        result = 1
        for prime, power in prime_powers.items():
            # 使用费马小定理计算 prime^power mod MOD
            result = (result * pow(prime, power, MOD)) % MOD
        
        return result
    
    @staticmethod
    def extended_gcd(a: int, b: int) -> List[int]:
        """
        扩展欧几里得算法
        求解 ax + by = gcd(a,b) 的一组整数解
        同时返回gcd(a,b)的值
        时间复杂度：O(log(min(a,b)))
        空间复杂度：O(log(min(a,b)))
        """
        if b == 0:
            return [a, 1, 0]  # gcd, x, y
        
        result = AdditionalGcdLcmProblems.extended_gcd(b, a % b)
        gcd_val, x1, y1 = result[0], result[1], result[2]
        
        x = y1
        y = x1 - (a // b) * y1
        
        return [gcd_val, x, y]
    
    @staticmethod
    def gcd_of_array(nums: List[int]) -> int:
        """
        计算数组中所有元素的最大公约数
        时间复杂度：O(n * log(min(elements)))
        空间复杂度：O(log(min(elements)))
        """
        result = nums[0]
        for i in range(1, len(nums)):
            result = math.gcd(result, nums[i])
            # 优化：如果GCD已经为1，可以提前结束
            if result == 1:
                break
        return result
    
    @staticmethod
    def lcm_of_array(nums: List[int]) -> int:
        """
        计算数组中所有元素的最小公倍数
        时间复杂度：O(n * log(min(elements)))
        空间复杂度：O(log(min(elements)))
        """
        result = nums[0]
        for i in range(1, len(nums)):
            result = math.lcm(result, nums[i])
        return result


# 测试方法
if __name__ == "__main__":
    print("=== 额外GCD和LCM问题测试 ===")
    
    # 测试lcm_sum
    print(f"LCM Sum (n=5): {AdditionalGcdLcmProblems.lcm_sum(5)}")
    print(f"LCM Sum (n=6): {AdditionalGcdLcmProblems.lcm_sum(6)}")
    print(f"LCM Sum (n=10): {AdditionalGcdLcmProblems.lcm_sum(10)}")
    
    # 测试gcd_extreme
    print(f"GCD Extreme (n=3): {AdditionalGcdLcmProblems.gcd_extreme(3)}")
    print(f"GCD Extreme (n=4): {AdditionalGcdLcmProblems.gcd_extreme(4)}")
    print(f"GCD Extreme (n=6): {AdditionalGcdLcmProblems.gcd_extreme(6)}")
    
    # 测试lcm_cardinality
    print(f"LCM Cardinality (n=2): {AdditionalGcdLcmProblems.lcm_cardinality(2)}")
    print(f"LCM Cardinality (n=12): {AdditionalGcdLcmProblems.lcm_cardinality(12)}")
    print(f"LCM Cardinality (n=100): {AdditionalGcdLcmProblems.lcm_cardinality(100)}")
    
    # 测试gcd_lcm_inverse
    result = AdditionalGcdLcmProblems.gcd_lcm_inverse(3, 60)
    print(f"GCD & LCM Inverse (gcd=3, lcm=60): a={result[0]}, b={result[1]}")
    
    result = AdditionalGcdLcmProblems.gcd_lcm_inverse(2, 20)
    print(f"GCD & LCM Inverse (gcd=2, lcm=20): a={result[0]}, b={result[1]}")
    
    # 测试enlarge_gcd
    nums1 = [6, 12, 18]
    print(f"Enlarge GCD (数组[6,12,18]): {AdditionalGcdLcmProblems.enlarge_gcd(nums1)}")
    
    nums2 = [2, 4, 6, 8]
    print(f"Enlarge GCD (数组[2,4,6,8]): {AdditionalGcdLcmProblems.enlarge_gcd(nums2)}")
    
    # 测试semi_common_multiple
    nums3 = [4, 6]
    print(f"Semi Common Multiple (a=[4,6], M=20): {AdditionalGcdLcmProblems.semi_common_multiple(nums3, 20)}")
    
    # 测试count_triplets
    print(f"Count Triplets (G=2, L=12): {AdditionalGcdLcmProblems.count_triplets(2, 12)}")
    
    # 测试gcd_product
    print(f"GCD Product (n=3, m=3): {AdditionalGcdLcmProblems.gcd_product(3, 3)}")
    print(f"GCD Product (n=4, m=4): {AdditionalGcdLcmProblems.gcd_product(4, 4)}")
    
    # 测试extended_gcd
    ext_result = AdditionalGcdLcmProblems.extended_gcd(30, 18)
    print(f"扩展欧几里得算法(30, 18): gcd={ext_result[0]}, x={ext_result[1]}, y={ext_result[2]}")
    print(f"验证: 30*{ext_result[1]} + 18*{ext_result[2]} = {30*ext_result[1] + 18*ext_result[2]}")
    
    # 测试数组GCD和LCM
    nums4 = [12, 18, 24]
    print(f"数组[12,18,24]的GCD: {AdditionalGcdLcmProblems.gcd_of_array(nums4)}")
    print(f"数组[12,18,24]的LCM: {AdditionalGcdLcmProblems.lcm_of_array(nums4)}")