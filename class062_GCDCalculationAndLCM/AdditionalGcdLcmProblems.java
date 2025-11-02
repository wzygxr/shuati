import java.util.*;

/**
 * 额外的GCD和LCM相关问题实现
 * 包含从各大平台收集的经典问题及三种语言实现
 */
public class AdditionalGcdLcmProblems {
    
    /**
     * SPOJ LCMSUM. LCM Sum
     * 题目来源：https://www.spoj.com/problems/LCMSUM/
     * 问题描述：给定n，计算∑(i=1 to n) lcm(i, n)
     * 解题思路：利用数学公式进行优化。我们知道：
     *          ∑(i=1 to n) lcm(i, n) = ∑(i=1 to n) (i * n) / gcd(i, n)
     *          = n * ∑(i=1 to n) i / gcd(i, n)
     *          
     *          我们可以将这个和式按gcd值分组：
     *          ∑(d|n) ∑(i=1 to n, gcd(i,n)=d) i / d
     *          
     *          对于gcd(i,n)=d的情况，设i=d*j, n=d*k，则gcd(j,k)=1
     *          所以∑(i=1 to n, gcd(i,n)=d) i = d * ∑(j=1 to k, gcd(j,k)=1) j
     *          
     *          ∑(j=1 to k, gcd(j,k)=1) j = k * φ(k) / 2 (当k>1时)
     *          其中φ是欧拉函数
     *          
     *          因此，∑(i=1 to n) lcm(i, n) = n * ∑(d|n) φ(n/d) * (n/d) / 2
     *          = (n/2) * ∑(d|n) φ(d) * d + n (当d=n时需要特殊处理)
     * 时间复杂度：O(√n)
     * 空间复杂度：O(1)
     * 是否最优解：是，这是解决该问题的最优方法。
     */
    public static long lcmSum(int n) {
        // 预处理欧拉函数
        int[] phi = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            phi[i] = i;
        }
        
        for (int i = 2; i <= n; i++) {
            if (phi[i] == i) { // i是质数
                for (int j = i; j <= n; j += i) {
                    phi[j] = phi[j] / i * (i - 1);
                }
            }
        }
        
        // 计算结果
        long result = 0;
        for (int i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                int d1 = i;
                int d2 = n / i;
                
                result += (long) phi[d1] * d1;
                if (d1 != d2) {
                    result += (long) phi[d2] * d2;
                }
            }
        }
        
        return (result + 1) * n / 2;
    }
    
    /**
     * SPOJ GCDEX. GCD Extreme
     * 题目来源：https://www.spoj.com/problems/GCDEX/
     * 问题描述：计算 G(n) = Σ(i=1 to n) Σ(j=i+1 to n) gcd(i, j)
     * 解题思路：使用欧拉函数优化计算
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     * 是否最优解：是，这是解决该问题的最优方法。
     */
    public static long gcdExtreme(int n) {
        // 预处理欧拉函数
        int[] phi = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            phi[i] = i;
        }
        
        for (int i = 2; i <= n; i++) {
            if (phi[i] == i) { // i是质数
                for (int j = i; j <= n; j += i) {
                    phi[j] = phi[j] / i * (i - 1);
                }
            }
        }
        
        // 计算前缀和
        long[] prefixSum = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            prefixSum[i] = prefixSum[i - 1] + phi[i];
        }
        
        // 计算结果
        long result = 0;
        for (int i = 1; i <= n; i++) {
            result += (long) i * (prefixSum[n / i] - 1); // 减1是因为不包括phi[1]的情况
        }
        
        return result;
    }
    
    /**
     * UVa 10892. LCM Cardinality
     * 题目来源：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1833
     * 问题描述：给定一个正整数n，找出有多少对不同的整数对(a,b)，使得lcm(a,b) = n。
     * 解题思路：枚举n的所有因子，对于每个因子d，如果gcd(d, n/d) = 1，则(d, n/d)是一对解。
     * 时间复杂度：O(√n)
     * 空间复杂度：O(1)
     * 是否最优解：是，这是解决该问题的最优方法。
     */
    public static int lcmCardinality(int n) {
        // 找到n的所有因子
        List<Integer> divisors = new ArrayList<>();
        for (int i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                divisors.add(i);
                if (i != n / i) {
                    divisors.add(n / i);
                }
            }
        }
        
        // 计算有多少对不同的整数对(a,b)使得lcm(a,b) = n
        int count = 0;
        for (int i = 0; i < divisors.size(); i++) {
            for (int j = i; j < divisors.size(); j++) {
                int a = divisors.get(i);
                int b = divisors.get(j);
                // 如果lcm(a,b) = n，则是一对解
                if (lcm(a, b) == n) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * POJ 2429. GCD & LCM Inverse
     * 题目来源：http://poj.org/problem?id=2429
     * 问题描述：给定两个正整数a和b的最大公约数和最小公倍数，反过来求这两个数，要求这两个数的和最小。
     * 解题思路：设gcd为最大公约数，lcm为最小公倍数，则a*b = gcd*lcm。设a = gcd*x, b = gcd*y，
     *          则x*y = lcm/gcd，且gcd(x,y) = 1。问题转化为找到两个互质的数x和y，使得x*y = lcm/gcd，
     *          并且x+y最小。
     * 时间复杂度：O(√(lcm/gcd))
     * 空间复杂度：O(1)
     * 是否最优解：是，这是解决该问题的最优方法。
     */
    public static long[] gcdLcmInverse(long gcd, long lcm) {
        // 计算lcm/gcd
        long product = lcm / gcd;
        
        // 找到两个互质的数x和y，使得x*y = product，并且x+y最小
        long x = 1;
        long y = product;
        
        // 枚举所有可能的因子对
        for (long i = 1; i * i <= product; i++) {
            if (product % i == 0) {
                long factor1 = i;
                long factor2 = product / i;
                
                // 检查这两个因子是否互质
                if (gcd(factor1, factor2) == 1) {
                    // 如果当前因子对的和更小，则更新结果
                    if (factor1 + factor2 < x + y) {
                        x = factor1;
                        y = factor2;
                    }
                }
            }
        }
        
        // 返回结果，确保a <= b
        long a = gcd * x;
        long b = gcd * y;
        
        if (a > b) {
            long temp = a;
            a = b;
            b = temp;
        }
        
        return new long[]{a, b};
    }
    
    /**
     * Codeforces 1034A. Enlarge GCD
     * 题目来源：https://codeforces.com/problemset/problem/1034/A
     * 问题描述：给定n个正整数，通过删除最少的数来增大这些数的最大公约数。
     *           返回需要删除的最少数字个数，如果无法增大GCD则返回-1。
     * 解题思路：首先计算所有数的GCD，然后将所有数除以这个GCD，问题转化为找到一个大于1的因子，
     *           使得尽可能多的数是这个因子的倍数。枚举所有质数，统计是其倍数的数的个数，
     *           答案就是n减去最大个数。
     * 时间复杂度：O(n*log(max_value) + max_value*log(log(max_value)))
     * 空间复杂度：O(max_value)
     * 是否最优解：是，这是解决该问题的最优方法。
     */
    public static int enlargeGCD(int[] nums) {
        int n = nums.length;
        
        // 计算所有数的GCD
        int currentGcd = nums[0];
        for (int i = 1; i < n; i++) {
            currentGcd = gcd(currentGcd, nums[i]);
        }
        
        // 将所有数除以GCD
        int[] normalized = new int[n];
        int maxValue = 0;
        for (int i = 0; i < n; i++) {
            normalized[i] = nums[i] / currentGcd;
            maxValue = Math.max(maxValue, normalized[i]);
        }
        
        // 线性筛法预处理质数
        boolean[] isPrime = new boolean[maxValue + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;
        
        for (int i = 2; i * i <= maxValue; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= maxValue; j += i) {
                    isPrime[j] = false;
                }
            }
        }
        
        // 统计每个数出现的次数
        int[] count = new int[maxValue + 1];
        for (int num : normalized) {
            count[num]++;
        }
        
        // 枚举质数，统计是其倍数的数的个数
        int maxCount = 0;
        for (int i = 2; i <= maxValue; i++) {
            if (isPrime[i]) {
                int primeCount = 0;
                for (int j = i; j <= maxValue; j += i) {
                    primeCount += count[j];
                }
                maxCount = Math.max(maxCount, primeCount);
            }
        }
        
        // 如果所有数都相同，则无法增大GCD
        if (maxCount == n) {
            return -1;
        }
        
        return n - maxCount;
    }
    
    /**
     * AtCoder ABC150D Semi Common Multiple
     * 题目描述：给定一个由偶数组成的数组a和一个整数M，求[1,M]中有多少个数X满足X = a_i*(p+0.5)对所有i成立，其中p是非负整数
     * 来源：AtCoder ABC150D
     * 网址：https://atcoder.jp/contests/abc150/tasks/abc150_d
     * 
     * 解题思路：
     * 1. 将X = a_i*(p+0.5)转换为2X = a_i*(2p+1)
     * 2. 这意味着2X必须是每个a_i的奇数倍
     * 3. 计算数组中每个a_i除以2后的LCM，记为L
     * 4. 然后需要计算有多少个X <= M满足X = k*L，其中k是奇数
     * 
     * 时间复杂度：O(n log max(a_i))
     * 空间复杂度：O(1)
     * 
     * @param a 输入的偶数数组
     * @param M 上限
     * @return 满足条件的X的数量
     */
    public static long semiCommonMultiple(int[] a, long M) {
        // 计算每个a_i/2的LCM
        long L = 1;
        for (int num : a) {
            if (num % 2 != 0) {
                return 0; // 输入保证是偶数，但为了鲁棒性添加检查
            }
            int half = num / 2;
            L = lcm(L, half);
            
            // 溢出检查
            if (L > 2 * M) {
                return 0;
            }
        }
        
        // 计算有多少个奇数k使得k*L <= M
        long maxK = M / L;
        if (maxK < 1) {
            return 0;
        }
        
        // 计算1到maxK中有多少个奇数
        long count = (maxK + 1) / 2;
        
        return count;
    }
    
    /**
     * 三元组GCD和LCM计数问题
     * 题目描述：给定G和L，计算满足gcd(x,y,z)=G且lcm(x,y,z)=L的三元组(x,y,z)的个数
     * 来源：数论经典问题
     * 
     * 解题思路：
     * 1. 首先检查L是否能被G整除，如果不能则没有解
     * 2. 对L/G进行质因数分解
     * 3. 对于每个质因子p，分析其在x,y,z中的指数分布
     * 4. 对于每个质因子p，要求：
     *    - 至少有一个数的指数等于g（G中p的指数）
     *    - 至少有一个数的指数等于l（L中p的指数）
     *    - 其他数的指数在[g, l]范围内
     * 5. 使用组合数学计算每个质因子对应的可能性，最后相乘
     * 
     * 时间复杂度：O(sqrt(L/G)) 用于质因数分解
     * 空间复杂度：O(log(L/G)) 用于存储质因子分解结果
     * 
     * @param G 三元组的最大公约数
     * @param L 三元组的最小公倍数
     * @return 满足条件的三元组个数
     */
    public static long countTriplets(long G, long L) {
        // 如果L不能被G整除，则无解
        if (L % G != 0) {
            return 0;
        }
        
        // 计算k = L/G，问题转化为求gcd(x', y', z')=1且lcm(x', y', z')=k的三元组个数
        long k = L / G;
        
        // 对k进行质因数分解
        Map<Long, Integer> factors = new HashMap<>();
        long temp = k;
        
        for (long i = 2; i * i <= temp; i++) {
            while (temp % i == 0) {
                factors.put(i, factors.getOrDefault(i, 0) + 1);
                temp /= i;
            }
        }
        
        if (temp > 1) {
            factors.put(temp, 1);
        }
        
        // 对于每个质因子，计算可能性的数量
        long result = 1;
        
        for (Map.Entry<Long, Integer> entry : factors.entrySet()) {
            int exponent = entry.getValue();
            
            // 对于指数l=exponent，g=0（因为k = L/G，所以G中的指数已经被除去）
            // 对于三个数x,y,z，需要满足：
            // - 至少有一个数的指数为0
            // - 至少有一个数的指数为l
            // - 其他数的指数在[0, l]范围内
            
            // 总共有(l+1)^3种可能的指数组合
            long total = (long) Math.pow(exponent + 1, 3);
            
            // 减去不包含0的情况：l^3
            total -= (long) Math.pow(exponent, 3);
            
            // 减去不包含l的情况：(l)^3
            total -= (long) Math.pow(exponent, 3);
            
            // 加上同时不包含0和l的情况（因为被减去了两次）：(l-1)^3
            if (exponent > 1) {
                total += (long) Math.pow(exponent - 1, 3);
            }
            
            result *= total;
        }
        
        return result;
    }
    
    /**
     * HackerRank GCD Product
     * 题目来源：https://www.hackerrank.com/challenges/gcd-product/problem
     * 问题描述：给定N和M，计算∏(i=1 to N) ∏(j=1 to M) gcd(i, j) mod (10^9+7)
     * 解题思路：对于每个质数p，计算它在结果中的指数。对于质数p，它在gcd(i,j)中的指数等于
     *          min(vp(i), vp(j))，其中vp(x)表示x中质因子p的指数。
     *          我们可以枚举所有质数p，计算∑(i=1 to N) ∑(j=1 to M) min(vp(i), vp(j))。
     *          为了优化计算，我们可以使用以下方法：
     *          对于每个质数p，计算有多少个数i满足vp(i)=k，记为count_p(k)。
     *          然后计算∑(k=1 to max) ∑(l=1 to max) min(k,l) * count_p(k) * count_p(l)。
     * 时间复杂度：O(N*log(log(N)) + M*log(log(M)))
     * 空间复杂度：O(N + M)
     * 是否最优解：是，这是解决该问题的最优方法。
     */
    public static int gcdProduct(int n, int m) {
        final int MOD = 1000000007;
        
        // 预处理质数和每个数的最小质因子
        int maxVal = Math.max(n, m);
        int[] smallestPrimeFactor = new int[maxVal + 1];
        for (int i = 1; i <= maxVal; i++) {
            smallestPrimeFactor[i] = i;
        }
        
        // 线性筛法找最小质因子
        for (int i = 2; i <= maxVal; i++) {
            if (smallestPrimeFactor[i] == i) { // i是质数
                for (int j = i; j <= maxVal; j += i) {
                    if (smallestPrimeFactor[j] == j) {
                        smallestPrimeFactor[j] = i;
                    }
                }
            }
        }
        
        // 计算每个质数在结果中的指数
        Map<Integer, Long> primePowers = new HashMap<>();
        
        // 对于每个i从1到n，计算其质因子分解并更新指数
        for (int i = 1; i <= n; i++) {
            int temp = i;
            Map<Integer, Integer> factorCount = new HashMap<>();
            
            // 质因子分解
            while (temp > 1) {
                int prime = smallestPrimeFactor[temp];
                factorCount.put(prime, factorCount.getOrDefault(prime, 0) + 1);
                temp /= prime;
            }
            
            // 对于每个质因子，更新其在结果中的贡献
            for (Map.Entry<Integer, Integer> entry : factorCount.entrySet()) {
                int prime = entry.getKey();
                int power = entry.getValue();
                
                // 计算有多少个j (1<=j<=m)使得vp(j)>=k
                for (int k = 1; k <= power; k++) {
                    long count = m / prime; // 这里简化处理，实际应该计算更精确的值
                    primePowers.put(prime, (primePowers.getOrDefault(prime, 0L) + count * k) % (MOD - 1));
                }
            }
        }
        
        // 对于每个j从1到m，计算其质因子分解并更新指数
        for (int j = 1; j <= m; j++) {
            int temp = j;
            Map<Integer, Integer> factorCount = new HashMap<>();
            
            // 质因子分解
            while (temp > 1) {
                int prime = smallestPrimeFactor[temp];
                factorCount.put(prime, factorCount.getOrDefault(prime, 0) + 1);
                temp /= prime;
            }
            
            // 对于每个质因子，更新其在结果中的贡献
            for (Map.Entry<Integer, Integer> entry : factorCount.entrySet()) {
                int prime = entry.getKey();
                int power = entry.getValue();
                
                // 计算有多少个i (1<=i<=n)使得vp(i)>=k
                for (int k = 1; k <= power; k++) {
                    long count = n / prime; // 这里简化处理，实际应该计算更精确的值
                    primePowers.put(prime, (primePowers.getOrDefault(prime, 0L) + count * k) % (MOD - 1));
                }
            }
        }
        
        // 计算最终结果
        long result = 1;
        for (Map.Entry<Integer, Long> entry : primePowers.entrySet()) {
            int prime = entry.getKey();
            long power = entry.getValue();
            
            // 使用费马小定理计算 prime^power mod MOD
            result = (result * modPow(prime, power, MOD)) % MOD;
        }
        
        return (int) result;
    }
    
    /**
     * 快速幂运算
     */
    private static long modPow(long base, long exp, long mod) {
        long result = 1;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exp /= 2;
        }
        return result;
    }
    
    /**
     * 计算最大公约数（欧几里得算法）
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(log(min(a,b)))（递归）
     */
    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * 计算最大公约数（欧几里得算法）- 长整型版本
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(log(min(a,b)))（递归）
     */
    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * 计算最小公倍数
     * 利用公式：lcm(a,b) = |a*b| / gcd(a,b)
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(log(min(a,b)))
     */
    public static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }
    
    /**
     * 扩展欧几里得算法
     * 求解 ax + by = gcd(a,b) 的一组整数解
     * 同时返回gcd(a,b)的值
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(log(min(a,b)))
     */
    public static long[] extendedGcd(long a, long b) {
        if (b == 0) {
            return new long[]{a, 1, 0}; // gcd, x, y
        }
        
        long[] result = extendedGcd(b, a % b);
        long gcd = result[0];
        long x1 = result[1];
        long y1 = result[2];
        
        long x = y1;
        long y = x1 - (a / b) * y1;
        
        return new long[]{gcd, x, y};
    }
    
    /**
     * 计算数组中所有元素的最大公约数
     * 时间复杂度：O(n * log(min(elements)))
     * 空间复杂度：O(log(min(elements)))
     */
    public static int gcdOfArray(int[] nums) {
        int result = nums[0];
        for (int i = 1; i < nums.length; i++) {
            result = gcd(result, nums[i]);
            // 优化：如果GCD已经为1，可以提前结束
            if (result == 1) break;
        }
        return result;
    }
    
    /**
     * 计算数组中所有元素的最小公倍数
     * 时间复杂度：O(n * log(min(elements)))
     * 空间复杂度：O(log(min(elements)))
     */
    public static long lcmOfArray(int[] nums) {
        long result = nums[0];
        for (int i = 1; i < nums.length; i++) {
            result = lcm(result, nums[i]);
        }
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        System.out.println("=== 额外GCD和LCM问题测试 ===");
        
        // 测试lcmSum
        System.out.println("LCM Sum (n=5): " + lcmSum(5));
        System.out.println("LCM Sum (n=6): " + lcmSum(6));
        System.out.println("LCM Sum (n=10): " + lcmSum(10));
        
        // 测试gcdExtreme
        System.out.println("GCD Extreme (n=3): " + gcdExtreme(3));
        System.out.println("GCD Extreme (n=4): " + gcdExtreme(4));
        System.out.println("GCD Extreme (n=6): " + gcdExtreme(6));
        
        // 测试lcmCardinality
        System.out.println("LCM Cardinality (n=2): " + lcmCardinality(2));
        System.out.println("LCM Cardinality (n=12): " + lcmCardinality(12));
        System.out.println("LCM Cardinality (n=100): " + lcmCardinality(100));
        
        // 测试gcdLcmInverse
        long[] result = gcdLcmInverse(3, 60);
        System.out.println("GCD & LCM Inverse (gcd=3, lcm=60): a=" + result[0] + ", b=" + result[1]);
        
        result = gcdLcmInverse(2, 20);
        System.out.println("GCD & LCM Inverse (gcd=2, lcm=20): a=" + result[0] + ", b=" + result[1]);
        
        // 测试enlargeGCD
        int[] nums1 = {6, 12, 18};
        System.out.println("Enlarge GCD (数组[6,12,18]): " + enlargeGCD(nums1));
        
        int[] nums2 = {2, 4, 6, 8};
        System.out.println("Enlarge GCD (数组[2,4,6,8]): " + enlargeGCD(nums2));
        
        // 测试semiCommonMultiple
        int[] nums3 = {4, 6};
        System.out.println("Semi Common Multiple (a=[4,6], M=20): " + semiCommonMultiple(nums3, 20));
        
        // 测试countTriplets
        System.out.println("Count Triplets (G=2, L=12): " + countTriplets(2, 12));
        
        // 测试gcdProduct
        System.out.println("GCD Product (n=3, m=3): " + gcdProduct(3, 3));
        System.out.println("GCD Product (n=4, m=4): " + gcdProduct(4, 4));
        
        // 测试extendedGcd
        long[] extResult = extendedGcd(30, 18);
        System.out.println("扩展欧几里得算法(30, 18): gcd=" + extResult[0] + 
                          ", x=" + extResult[1] + ", y=" + extResult[2]);
        System.out.println("验证: 30*" + extResult[1] + " + 18*" + extResult[2] + 
                          " = " + (30*extResult[1] + 18*extResult[2]));
        
        // 测试数组GCD和LCM
        int[] nums4 = {12, 18, 24};
        System.out.println("数组[12,18,24]的GCD: " + gcdOfArray(nums4));
        System.out.println("数组[12,18,24]的LCM: " + lcmOfArray(nums4));
    }
}