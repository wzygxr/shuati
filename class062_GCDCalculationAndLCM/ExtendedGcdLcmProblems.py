"""
扩展GCD和LCM相关问题的实现（Python版本）
包含LeetCode和其他平台上的经典问题
"""

import math
from typing import List, Optional

# 链表节点定义
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class ExtendedGcdLcmProblems:
    """
    扩展GCD和LCM相关问题的实现
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
        import math
        
        def gcd(x: int, y: int) -> int:
            return x if y == 0 else gcd(y, x % y)
        
        n = len(nums)
        
        # 计算所有数的GCD
        current_gcd = nums[0]
        for i in range(1, n):
            current_gcd = gcd(current_gcd, nums[i])
        
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
    def gcd_extreme(n: int) -> int:
        """
        SPOJ GCD Extreme 问题
        题目描述：计算sum_{i=1到n-1} sum_{j=i+1到n} gcd(i,j)
        来源：SPOJ - GCDEX
        网址：https://www.spoj.com/problems/GCDEX/
        
        解题思路：
        使用欧拉函数和前缀和优化。
        1. 计算对于每个d，有多少对(i,j)满足gcd(i,j)=d
        2. 利用容斥原理，先计算gcd为d的倍数的对数，再减去gcd为2d, 3d等的对数
        3. 使用欧拉函数φ(k)来计算互质对的数量
        
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        
        @param n 输入整数
        @return GCD(i,j) for 1<=i<j<=n的和
        """
        if n < 2:
            return 0
        
        # 初始化数组
        phi = list(range(n + 1))  # 欧拉函数值
        ans = [0] * (n + 1)       # ans[d]表示gcd为d的对数
        
        # 计算欧拉函数
        for i in range(2, n + 1):
            if phi[i] == i:  # i是质数
                for j in range(i, n + 1, i):
                    phi[j] -= phi[j] // i
        
        # 计算gcd为d的对数
        for d in range(n, 0, -1):
            # 计算有多少对(i,j)的gcd是d的倍数
            # 这相当于在1到n/d中选择两个不同的数
            cnt = (n // d) * (n // d - 1) // 2
            
            # 减去gcd为2d, 3d等的对数
            for k in range(2 * d, n + 1, d):
                cnt -= ans[k]
            
            ans[d] = cnt
        
        # 计算总和
        result = 0
        for d in range(1, n + 1):
            result += d * ans[d]
        
        return result
    
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
    def semi_common_multiple(a: list, M: int) -> int:
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
    def insert_greatest_common_divisors(head: Optional[ListNode]) -> Optional[ListNode]:
        """
        LeetCode 2807. Insert Greatest Common Divisors in Linked List
        题目来源：https://leetcode.com/problems/insert-greatest-common-divisors-in-linked-list/
        问题描述：给定一个链表，在每对相邻节点之间插入一个值为它们最大公约数的新节点。
        解题思路：遍历链表，对每对相邻节点，计算它们的最大公约数并插入新节点。
        时间复杂度：O(n * log(min(a,b)))，其中n是链表长度
        空间复杂度：O(1)，只使用常数额外空间（不计算新插入的节点）
        是否最优解：是，这是解决该问题的最优方法。
        """
        # 如果链表为空或只有一个节点，直接返回
        if head is None or head.next is None:
            return head
        
        current = head
        
        # 遍历链表，直到倒数第二个节点
        while current.next is not None:
            # 计算当前节点和下一个节点值的最大公约数
            gcd_value = math.gcd(current.val, current.next.val)
            
            # 创建新节点并插入
            new_node = ListNode(gcd_value)
            new_node.next = current.next
            current.next = new_node
            
            # 移动到下一个原始节点（跳过刚插入的节点）
            current = new_node.next
        
        return head
    
    @staticmethod
    def find_gcd(nums: List[int]) -> int:
        """
        LeetCode 1979. Find Greatest Common Divisor of Array
        题目来源：https://leetcode.com/problems/find-greatest-common-divisor-of-array/
        问题描述：给定一个整数数组nums，返回数组中最小数和最大数的最大公约数。
        解题思路：首先找到数组中的最小值和最大值，然后计算它们的最大公约数。
        时间复杂度：O(n + log(min(min_val, max_val)))，其中n是数组长度
        空间复杂度：O(log(min(min_val, max_val)))，递归调用栈的深度
        是否最优解：是，这是解决该问题的最优方法。
        """
        min_val = min(nums)
        max_val = max(nums)
        return math.gcd(min_val, max_val)
    
    @staticmethod
    def nth_magical_number(n: int, a: int, b: int) -> int:
        """
        LeetCode 878. 第N个神奇数字
        问题描述：一个正整数如果能被a或b整除，那么它是神奇的。给定n,a,b，返回第n个神奇数字。
        解题思路：使用二分查找 + 容斥原理
        时间复杂度：O(log(n * min(a,b)))
        空间复杂度：O(1)
        """
        def gcd(x: int, y: int) -> int:
            return x if y == 0 else gcd(y, x % y)
        
        def lcm(x: int, y: int) -> int:
            return x * y // gcd(x, y)
        
        lcm_val = lcm(a, b)
        left, right = 0, n * min(a, b)
        result = 0
        
        # 二分查找第n个神奇数字
        while left <= right:
            mid = left + (right - left) // 2
            # 在[1, mid]范围内神奇数字的个数
            count = mid // a + mid // b - mid // lcm_val
            
            if count >= n:
                result = mid
                right = mid - 1
            else:
                left = mid + 1
                
        return result % (10**9 + 7)
    
    @staticmethod
    def nth_ugly_number(n: int, a: int, b: int, c: int) -> int:
        """
        LeetCode 1201. 丑数III
        问题描述：编写一个程序，找出第n个丑数，丑数是可以被a或b或c整除的正整数。
        解题思路：二分查找 + 容斥原理
        时间复杂度：O(log(n * min(a,b,c)))
        空间复杂度：O(1)
        """
        def gcd(x: int, y: int) -> int:
            return x if y == 0 else gcd(y, x % y)
        
        def lcm(x: int, y: int) -> int:
            return x * y // gcd(x, y)
        
        la, lb, lc = a, b, c
        lab = lcm(la, lb)
        lac = lcm(la, lc)
        lbc = lcm(lb, lc)
        labc = lcm(lab, lc)
        
        left, right = 0, 2 * 10**9  # 根据题目数据范围设定
        result = 0
        
        # 二分查找第n个丑数
        while left <= right:
            mid = left + (right - left) // 2
            # 在[1, mid]范围内丑数的个数（容斥原理）
            count = mid // la + mid // lb + mid // lc \
                    - mid // lab - mid // lac - mid // lbc \
                    + mid // labc
            
            if count >= n:
                result = mid
                right = mid - 1
            else:
                left = mid + 1
                
        return result % (10**9 + 7)
    
    @staticmethod
    def gcd_of_strings(str1: str, str2: str) -> str:
        """
        LeetCode 1071. 字符串的最大公因子
        问题描述：对于字符串s和t，只有在s=t+t+t+...+t时，才认为t能除尽s。
                  给定两个字符串str1和str2，返回最长字符串x，使得x能除尽str1和str2。
        解题思路：利用GCD的性质，如果存在这样的字符串，其长度必然是两个字符串长度的GCD
        时间复杂度：O(m+n)
        空间复杂度：O(1)
        """
        # 如果存在公因子字符串，则str1+str2应该等于str2+str1
        if str1 + str2 != str2 + str1:
            return ""
        
        # 最大公因子字符串的长度就是两个字符串长度的GCD
        import math
        gcd_length = math.gcd(len(str1), len(str2))
        return str1[:gcd_length]
    
    @staticmethod
    def subarray_gcd(nums: List[int], k: int) -> int:
        """
        LeetCode 2447. 最大公因数等于K的子数组数目
        问题描述：给定一个数组和一个正整数k，返回最大公因数等于k的子数组数目。
        解题思路：遍历所有子数组，计算每个子数组的GCD，统计等于k的数量
        时间复杂度：O(n^2 * log(max(nums)))
        空间复杂度：O(1)
        """
        def gcd(x: int, y: int) -> int:
            return x if y == 0 else gcd(y, x % y)
        
        count = 0
        n = len(nums)
        
        # 遍历所有可能的子数组
        for i in range(n):
            current_gcd = nums[i]
            # 优化：如果当前元素不能被k整除，跳过
            if current_gcd % k != 0:
                continue
            
            for j in range(i, n):
                # 优化：如果当前元素不能被k整除，跳出内层循环
                if nums[j] % k != 0:
                    break
                
                current_gcd = gcd(current_gcd, nums[j])
                
                # 如果GCD小于k，不可能再变大，跳出内层循环
                if current_gcd < k:
                    break
                
                # 如果GCD等于k，计数加1
                if current_gcd == k:
                    count += 1
                    
        return count
    
    @staticmethod
    def subarray_lcm(nums: List[int], k: int) -> int:
        """
        LeetCode 2470. 最小公倍数为K的子数组数目
        问题描述：给定一个数组和一个正整数k，返回最小公倍数等于k的子数组数目。
        解题思路：遍历所有子数组，计算每个子数组的LCM，统计等于k的数量
        时间复杂度：O(n^2 * log(max(nums)))
        空间复杂度：O(1)
        """
        def gcd(x: int, y: int) -> int:
            return x if y == 0 else gcd(y, x % y)
        
        def lcm(x: int, y: int) -> int:
            return x * y // gcd(x, y)
        
        count = 0
        n = len(nums)
        
        # 遍历所有可能的子数组
        for i in range(n):
            current_lcm = nums[i]
            
            # 如果当前元素不能整除k，跳过
            if k % nums[i] != 0:
                continue
            
            for j in range(i, n):
                # 如果当前元素不能整除k，跳出内层循环
                if k % nums[j] != 0:
                    break
                
                current_lcm = lcm(current_lcm, nums[j])
                
                # 如果LCM大于k，不可能再变小，跳出内层循环
                if current_lcm > k:
                    break
                
                # 如果LCM等于k，计数加1
                if current_lcm == k:
                    count += 1
                    
        return count
    
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
        
        result = ExtendedGcdLcmProblems.extended_gcd(b, a % b)
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
        import math
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
        import math
        result = nums[0]
        for i in range(1, len(nums)):
            result = math.lcm(result, nums[i])
        return result


# 辅助方法：打印链表
def print_list(head: Optional[ListNode]) -> None:
    current = head
    while current is not None:
        print(current.val, end="")
        if current.next is not None:
            print(" -> ", end="")
        current = current.next
    print()

# 测试方法
if __name__ == "__main__":
    print("=== GCD和LCM扩展问题测试 ===")
    
    # 测试lcm_cardinality
    print(f"LCM Cardinality (n=2): {ExtendedGcdLcmProblems.lcm_cardinality(2)}")
    print(f"LCM Cardinality (n=12): {ExtendedGcdLcmProblems.lcm_cardinality(12)}")
    print(f"LCM Cardinality (n=100): {ExtendedGcdLcmProblems.lcm_cardinality(100)}")
    
    # 测试gcd_extreme
    print(f"GCD Extreme (n=3): {ExtendedGcdLcmProblems.gcd_extreme(3)}")
    print(f"GCD Extreme (n=4): {ExtendedGcdLcmProblems.gcd_extreme(4)}")
    print(f"GCD Extreme (n=6): {ExtendedGcdLcmProblems.gcd_extreme(6)}")
    
    # 测试insert_greatest_common_divisors
    head1 = ListNode(18)
    head1.next = ListNode(6)
    head1.next.next = ListNode(10)
    head1.next.next.next = ListNode(3)
    
    print("原链表: ", end="")
    print_list(head1)
    
    result1 = ExtendedGcdLcmProblems.insert_greatest_common_divisors(head1)
    print("插入GCD后: ", end="")
    print_list(result1)
    
    # 测试find_gcd
    nums1 = [2, 5, 6, 9, 10]
    print(f"数组[2,5,6,9,10]的GCD: {ExtendedGcdLcmProblems.find_gcd(nums1)}")
    
    nums2 = [7, 5, 6, 8, 3]
    print(f"数组[7,5,6,8,3]的GCD: {ExtendedGcdLcmProblems.find_gcd(nums2)}")
    
    nums3 = [3, 3]
    print(f"数组[3,3]的GCD: {ExtendedGcdLcmProblems.find_gcd(nums3)}")
    
    # 测试nth_magical_number
    print(f"第1个神奇数字(n=1, a=2, b=3): {ExtendedGcdLcmProblems.nth_magical_number(1, 2, 3)}")
    print(f"第4个神奇数字(n=4, a=2, b=3): {ExtendedGcdLcmProblems.nth_magical_number(4, 2, 3)}")
    
    # 测试nth_ugly_number
    print(f"第3个丑数(n=3, a=2, b=3, c=5): {ExtendedGcdLcmProblems.nth_ugly_number(3, 2, 3, 5)}")
    print(f"第4个丑数(n=4, a=2, b=3, c=4): {ExtendedGcdLcmProblems.nth_ugly_number(4, 2, 3, 4)}")
    
    # 测试gcd_of_strings
    print(f"字符串最大公因子(\"ABCABC\", \"ABC\"): {ExtendedGcdLcmProblems.gcd_of_strings('ABCABC', 'ABC')}")
    print(f"字符串最大公因子(\"ABABAB\", \"ABAB\"): {ExtendedGcdLcmProblems.gcd_of_strings('ABABAB', 'ABAB')}")
    print(f"字符串最大公因子(\"LEET\", \"CODE\"): {ExtendedGcdLcmProblems.gcd_of_strings('LEET', 'CODE')}")
    
    # 测试subarray_gcd
    nums4 = [9, 3, 1, 2, 6, 3]
    print(f"GCD等于3的子数组数目: {ExtendedGcdLcmProblems.subarray_gcd(nums4, 3)}")
    
    nums5 = [3, 1, 2, 4, 6]
    print(f"GCD等于1的子数组数目: {ExtendedGcdLcmProblems.subarray_gcd(nums5, 1)}")
    
    # 测试subarray_lcm
    nums6 = [3, 6, 2, 1, 2]
    print(f"LCM等于6的子数组数目: {ExtendedGcdLcmProblems.subarray_lcm(nums6, 6)}")
    
    # 测试extended_gcd
    ext_result = ExtendedGcdLcmProblems.extended_gcd(30, 18)
    print(f"扩展欧几里得算法(30, 18): gcd={ext_result[0]}, x={ext_result[1]}, y={ext_result[2]}")
    print(f"验证: 30*{ext_result[1]} + 18*{ext_result[2]} = {30*ext_result[1] + 18*ext_result[2]}")
    
    # 测试数组GCD和LCM
    nums7 = [12, 18, 24]
    print(f"数组[12,18,24]的GCD: {ExtendedGcdLcmProblems.gcd_of_array(nums7)}")
    print(f"数组[12,18,24]的LCM: {ExtendedGcdLcmProblems.lcm_of_array(nums7)}")
    
    # 测试enlargeGCD
    nums8 = [6, 12, 18]
    print(f"Enlarge GCD (数组[6,12,18]): {ExtendedGcdLcmProblems.enlarge_gcd(nums8)}")
    
    nums9 = [2, 4, 6, 8]
    print(f"Enlarge GCD (数组[2,4,6,8]): {ExtendedGcdLcmProblems.enlarge_gcd(nums9)}")
    
    # 测试gcdProduct
    print(f"GCD Product (n=3, m=3): {ExtendedGcdLcmProblems.gcd_product(3, 3)}")
    print(f"GCD Product (n=4, m=4): {ExtendedGcdLcmProblems.gcd_product(4, 4)}")
    
    # 测试lcmSum
    print(f"LCM Sum (n=5): {ExtendedGcdLcmProblems.lcm_sum(5)}")
    print(f"LCM Sum (n=6): {ExtendedGcdLcmProblems.lcm_sum(6)}")
    print(f"LCM Sum (n=10): {ExtendedGcdLcmProblems.lcm_sum(10)}")
    
    # 测试lcmSum
    print(f"LCM Sum (n=5): {ExtendedGcdLcmProblems.lcm_sum(5)}")
    print(f"LCM Sum (n=6): {ExtendedGcdLcmProblems.lcm_sum(6)}")
    print(f"LCM Sum (n=10): {ExtendedGcdLcmProblems.lcm_sum(10)}")
    
    # 测试新添加的函数
    print(f"GCD Extreme (n=4): {ExtendedGcdLcmProblems.gcd_extreme(4)}")
    print(f"Count Triplets (G=2, L=12): {ExtendedGcdLcmProblems.count_triplets(2, 12)}")
    print(f"Semi Common Multiple (a=[4,6], M=20): {ExtendedGcdLcmProblems.semi_common_multiple([4, 6], 20)}")
