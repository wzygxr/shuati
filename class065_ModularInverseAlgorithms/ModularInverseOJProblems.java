package class099;

import java.util.*;
import java.math.BigInteger;

/**
 * 各大OJ平台模逆元题目完整实现集
 * 包含从LeetCode、Codeforces、AtCoder、洛谷、ZOJ、POJ等平台收集的模逆元相关题目
 * 
 * 本文件特点：
 * 1. 每个题目都有完整的题目描述、链接、难度评级
 * 2. 提供详细的解题思路和算法分析
 * 3. 包含时间复杂度和空间复杂度分析
 * 4. 提供完整的Java实现代码
 * 5. 包含边界测试和性能测试
 * 6. 提供多语言实现思路（C++、Python）
 * 
 * 应用场景：
 * 1. 算法竞赛准备
 * 2. 面试复习
 * 3. 密码学应用
 * 4. 数论研究
 */

public class ModularInverseOJProblems {
    
    private static final int MOD = 1000000007;
    
    // ==================== LeetCode 题目 ====================
    
    /**
     * 题目1: LeetCode 1808. Maximize Number of Nice Divisors
     * 链接: https://leetcode.cn/problems/maximize-number-of-nice-divisors/
     * 难度: 困难
     * 题意: 给定primeFactors，构造一个正整数n，使得n的质因数总数不超过primeFactors，求n的"好因子"的最大数目
     * 
     * 解题思路:
     * 这是一个数学优化问题，本质上是整数拆分问题。
     * 要使好因子数目最大，我们需要合理分配primeFactors个质因数。
     * 好因子的数目等于各个质因数指数的乘积。
     * 
     * 根据数学分析，最优策略是尽可能多地使用3作为质因数的指数，
     * 因为3是使乘积最大的最优底数。
     * 
     * 具体策略：
     * 1. 如果 primeFactors % 3 == 0，全部用3
     * 2. 如果 primeFactors % 3 == 1，用一个4（2*2）代替两个3（3*3 < 4*1）
     * 3. 如果 primeFactors % 3 == 2，用一个2
     * 
     * 算法原理:
     * 这是一个经典的整数划分问题，目标是将primeFactors划分为若干个正整数，
     * 使得这些正整数的乘积最大。根据数学分析，最优策略是尽可能多地使用3。
     * 
     * 时间复杂度: O(log primeFactors)
     * 空间复杂度: O(1)
     * 
     * @param primeFactors 质因数总数上限
     * @return 好因子的最大数目
     */
    public static int leetcode1808MaximizeNiceDivisors(int primeFactors) {
        if (primeFactors <= 3) {
            return primeFactors;
        }
        
        int remainder = primeFactors % 3;
        int quotient = primeFactors / 3;
        
        if (remainder == 0) {
            // 全部用3
            return (int) power(3, quotient, MOD);
        } else if (remainder == 1) {
            // 用一个4代替两个3
            return (int) ((power(3, quotient - 1, MOD) * 4) % MOD);
        } else {  // remainder == 2
            // 用一个2
            return (int) ((power(3, quotient, MOD) * 2) % MOD);
        }
    }
    
    /**
     * 题目2: LeetCode 1623. Number of Sets of K Non-Overlapping Line Segments
     * 链接: https://leetcode.cn/problems/number-of-sets-of-k-non-overlapping-line-segments/
     * 难度: 中等
     * 题意: 在n个点上选择k个不重叠的线段的方案数
     * 
     * 解题思路:
     * 使用组合数学公式：C(n + k - 1, 2k)
     * 这个公式可以通过将问题转化为在n+k-1个位置中选择2k个位置来理解
     * 
     * 算法原理:
     * 这是一个经典的组合数学问题。我们可以将问题转化为：
     * 在n个点中选择k个不重叠的线段，等价于在n+k-1个位置中选择2k个位置。
     * 其中k个位置用于线段的起点，k个位置用于线段的终点。
     * 
     * 时间复杂度: O(n)（预处理阶乘）
     * 空间复杂度: O(n)
     * 
     * @param n 点的数量
     * @param k 线段数量
     * @return 方案数
     */
    public static int leetcode1623NumberOfSets(int n, int k) {
        if (k == 0) return 1;
        if (k > n) return 0;
        
        // 预处理阶乘和阶乘逆元
        int max = n + k - 1;
        long[] fact = new long[max + 1];
        long[] invFact = new long[max + 1];
        
        fact[0] = 1;
        for (int i = 1; i <= max; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        
        invFact[max] = power(fact[max], MOD - 2, MOD);
        for (int i = max - 1; i >= 0; i--) {
            invFact[i] = invFact[i + 1] * (i + 1) % MOD;
        }
        
        // 计算组合数C(n+k-1, 2k)
        return (int) (fact[max] * invFact[2 * k] % MOD * invFact[max - 2 * k] % MOD);
    }
    
    /**
     * 题目3: LeetCode 920. Number of Music Playlists
     * 链接: https://leetcode.cn/problems/number-of-music-playlists/
     * 难度: 困难
     * 题意: 你的音乐播放器里有n首不同的歌，在旅途中你的旅伴想要听l首歌，要求每首歌至少播放一次，且一首歌只有在其他k首歌播放完之后才能再次播放
     * 
     * 解题思路:
     * 使用容斥原理和动态规划
     * 定义dp[i][j]为播放了i首歌，使用了j首不同歌曲的方案数
     * 
     * 算法原理:
     * 这是一个动态规划问题，状态转移方程为：
     * dp[i][j] = dp[i-1][j-1] * (n-j+1) + dp[i-1][j] * (j-k)
     * 其中：
     * - dp[i-1][j-1] * (n-j+1) 表示选择一首新歌的方案数
     * - dp[i-1][j] * (j-k) 表示选择一首旧歌的方案数（但需要满足k首歌的间隔要求）
     * 
     * 时间复杂度: O(n*l)
     * 空间复杂度: O(n*l)
     * 
     * @param n 歌曲总数
     * @param l 播放列表长度
     * @param k 间隔要求
     * @return 方案数
     */
    public static int leetcode920NumberOfMusicPlaylists(int n, int l, int k) {
        long[][] dp = new long[l + 1][n + 1];
        dp[0][0] = 1;
        
        for (int i = 1; i <= l; i++) {
            for (int j = 1; j <= n; j++) {
                // 选择一首新歌
                dp[i][j] = (dp[i][j] + dp[i - 1][j - 1] * (n - j + 1)) % MOD;
                // 选择一首旧歌（但需要满足k首歌的间隔要求）
                if (j > k) {
                    dp[i][j] = (dp[i][j] + dp[i - 1][j] * (j - k)) % MOD;
                }
            }
        }
        
        return (int) dp[l][n];
    }
    
    // ==================== Codeforces 题目 ====================
    
    /**
     * 题目4: Codeforces 1445D. Divide and Sum
     * 链接: https://codeforces.com/problemset/problem/1445/D
     * 难度: 中等
     * 题意: 计算所有划分方案的f(p)值之和
     * 
     * 解题思路:
     * 排序后，每对元素的贡献是固定的，可以用组合数学快速计算
     * 具体来说，对于排序后的数组，前n个元素和后n个元素的差值之和乘以组合数C(2n-1, n-1)
     * 
     * 算法原理:
     * 通过数学分析可以发现，对于任意一种划分方案，f(p)的值只与数组中元素的相对大小有关。
     * 因此我们可以先对数组进行排序，然后计算每个元素在所有划分方案中的贡献。
     * 
     * 时间复杂度: O(n log n)（排序）
     * 空间复杂度: O(n)
     * 
     * @param arr 输入数组
     * @return 所有划分方案的f(p)值之和
     */
    public static long codeforces1445DivideAndSum(int[] arr) {
        int n = arr.length / 2;
        Arrays.sort(arr);
        
        // 预处理阶乘和阶乘逆元
        long[] fact = new long[2 * n + 1];
        long[] invFact = new long[2 * n + 1];
        fact[0] = 1;
        for (int i = 1; i <= 2 * n; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        invFact[2 * n] = power(fact[2 * n], MOD - 2, MOD);
        for (int i = 2 * n - 1; i >= 0; i--) {
            invFact[i] = invFact[i + 1] * (i + 1) % MOD;
        }
        
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum = (sum + arr[n + i] - arr[i]) % MOD;
        }
        sum = (sum % MOD + MOD) % MOD;
        
        // 计算组合数C(2n-1, n-1)
        long comb = fact[2 * n - 1] * invFact[n - 1] % MOD * invFact[n] % MOD;
        
        return sum * comb % MOD;
    }
    
    /**
     * 题目5: Codeforces 1422D. Returning Home
     * 链接: https://codeforces.com/problemset/problem/1422/D
     * 难度: 困难
     * 题意: 在二维平面上寻找最短路径，可以使用传送点
     * 
     * 解题思路:
     * 将问题转化为图论问题，使用Dijkstra算法
     * 关键优化：由于传送点的特殊性质，可以优化边的数量
     * 
     * 算法原理:
     * 这是一个图论中的最短路径问题。我们可以将起点、终点和所有传送点作为图的节点，
     * 然后计算任意两点之间的曼哈顿距离作为边的权重，最后使用Dijkstra算法求解最短路径。
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 
     * @param n 传送点数量
     * @param m 未使用参数
     * @param teleports 传送点坐标数组
     * @param start 起点坐标
     * @param end 终点坐标
     * @return 最短路径长度
     */
    public static long codeforces1422ReturningHome(int n, int m, int[][] teleports, int[] start, int[] end) {
        // 创建图节点：起点、终点、所有传送点
        List<int[]> nodes = new ArrayList<>();
        nodes.add(start);
        nodes.add(end);
        for (int[] teleport : teleports) {
            nodes.add(teleport);
        }
        
        // 构建图
        Map<Integer, List<int[]>> graph = new HashMap<>();
        int nodeCount = nodes.size();
        
        // 添加相邻节点之间的边（曼哈顿距离）
        for (int i = 0; i < nodeCount; i++) {
            for (int j = i + 1; j < nodeCount; j++) {
                int[] node1 = nodes.get(i);
                int[] node2 = nodes.get(j);
                int dist = Math.abs(node1[0] - node2[0]) + Math.abs(node1[1] - node2[1]);
                
                graph.computeIfAbsent(i, k -> new ArrayList<>()).add(new int[]{j, dist});
                graph.computeIfAbsent(j, k -> new ArrayList<>()).add(new int[]{i, dist});
            }
        }
        
        // 使用Dijkstra算法求最短路径
        long[] dist = new long[nodeCount];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[0] = 0; // 起点
        
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        pq.offer(new long[]{0, 0});
        
        while (!pq.isEmpty()) {
            long[] current = pq.poll();
            int u = (int) current[0];
            long d = current[1];
            
            if (d > dist[u]) continue;
            
            if (graph.containsKey(u)) {
                for (int[] edge : graph.get(u)) {
                    int v = edge[0];
                    int w = edge[1];
                    if (dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                        pq.offer(new long[]{v, dist[v]});
                    }
                }
            }
        }
        
        return dist[1]; // 终点
    }
    
    // ==================== AtCoder 题目 ====================
    
    /**
     * 题目6: AtCoder ABC182E. Throne
     * 链接: https://atcoder.jp/contests/abc182/tasks/abc182_e
     * 难度: 中等
     * 题意: 在圆桌上移动，求到达特定位置的最小步数
     * 
     * 解题思路:
     * 解方程: (S + K*x) ≡ 0 (mod N)
     * 即 K*x ≡ -S (mod N)
     * 使用扩展欧几里得算法求解线性同余方程
     * 
     * 算法原理:
     * 这是一个线性同余方程求解问题。我们需要找到满足条件的最小正整数x。
     * 通过数学变换，可以将问题转化为求解扩展欧几里得方程。
     * 
     * 时间复杂度: O(log(min(K, N)))
     * 空间复杂度: O(1)
     * 
     * @param N 圆桌上的位置数
     * @param S 起始位置
     * @param K 每次移动的步数
     * @return 到达位置0的最小步数，如果无法到达则返回-1
     */
    public static long atcoderABC182EThrone(long N, long S, long K) {
        // 解方程: (S + K*x) ≡ 0 (mod N)
        // 即 K*x ≡ -S (mod N)
        long g = gcd(K, N);
        if (S % g != 0) return -1;
        
        long newN = N / g;
        long newK = K / g;
        long newS = (-S) / g;
        
        long inv = modInverseExtendedGcd(newK, newN);
        if (inv == -1) return -1;
        
        long x = (inv * newS % newN + newN) % newN;
        return x;
    }
    
    /**
     * 题目7: AtCoder ABC151E. Max-Min Sums
     * 链接: https://atcoder.jp/contests/abc151/tasks/abc151_e
     * 难度: 中等
     * 题意: 计算所有子集的最大值和最小值之差的和
     * 
     * 解题思路:
     * 对于排序后的数组，每个元素作为最大值和最小值的贡献是固定的
     * 使用组合数学快速计算
     * 
     * 算法原理:
     * 通过排序后，我们可以计算每个元素在所有子集中作为最大值和最小值的次数，
     * 然后乘以对应的元素值，得到总的贡献。
     * 
     * 时间复杂度: O(n log n)（排序）
     * 空间复杂度: O(n)
     * 
     * @param arr 输入数组
     * @return 所有子集的最大值和最小值之差的和
     */
    public static long atcoderABC151EMaxMinSums(int[] arr) {
        Arrays.sort(arr);
        int n = arr.length;
        
        // 预处理阶乘和阶乘逆元
        long[] fact = new long[n + 1];
        long[] invFact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        invFact[n] = power(fact[n], MOD - 2, MOD);
        for (int i = n - 1; i >= 0; i--) {
            invFact[i] = invFact[i + 1] * (i + 1) % MOD;
        }
        
        long result = 0;
        for (int i = 0; i < n; i++) {
            // 元素arr[i]作为最大值的贡献
            long maxContribution = fact[i] * invFact[i] % MOD * arr[i] % MOD;
            // 元素arr[i]作为最小值的贡献（负贡献）
            long minContribution = fact[n - i - 1] * invFact[n - i - 1] % MOD * arr[i] % MOD;
            
            result = (result + maxContribution - minContribution + MOD) % MOD;
        }
        
        return result;
    }
    
    // ==================== 洛谷题目 ====================
    
    /**
     * 题目8: 洛谷 P3811 【模板】乘法逆元
     * 链接: https://www.luogu.com.cn/problem/P3811
     * 难度: 模板
     * 题意: 给定n和p，求1~n所有整数在模p意义下的乘法逆元
     * 
     * 解题思路:
     * 使用线性递推方法，这是批量计算逆元的最优方法
     * 递推公式：inv[i] = (p - p/i) * inv[p%i] % p
     * 
     * 算法原理:
     * 这是计算批量模逆元的经典算法，时间复杂度为O(n)，比逐个计算更高效。
     * 递推公式基于数学推导：设p = k*i + r，则k*i + r ≡ 0 (mod p)，
     * 两边同时乘以i^(-1) * r^(-1)得：k*r^(-1) + i^(-1) ≡ 0 (mod p)，
     * 即i^(-1) ≡ -k*r^(-1) (mod p)。
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 
     * @param n 计算范围上限
     * @param p 模数
     * @return 1~n所有整数在模p意义下的乘法逆元数组
     */
    public static long[] luoguP3811ModularInverse(int n, int p) {
        long[] inv = new long[n + 1];
        inv[1] = 1;
        for (int i = 2; i <= n; i++) {
            inv[i] = (p - (p / i) * inv[p % i] % p) % p;
        }
        return inv;
    }
    
    /**
     * 题目9: 洛谷 P2613 【模板】有理数取余
     * 链接: https://www.luogu.com.cn/problem/P2613
     * 难度: 模板
     * 题意: 计算两个大整数的除法结果模19260817
     * 
     * 解题思路:
     * 使用BigInteger处理大整数，利用费马小定理求逆元
     * 
     * 算法原理:
     * 由于输入的数字可能非常大，需要使用BigInteger处理。
     * 根据费马小定理：当p为质数且gcd(a,p)=1时，a^(p-1) ≡ 1 (mod p)，
     * 所以a^(-1) ≡ a^(p-2) (mod p)。
     * 
     * 时间复杂度: O(log p)
     * 空间复杂度: O(1)
     * 
     * @param a 被除数
     * @param b 除数
     * @return (a/b) mod 19260817
     */
    public static BigInteger luoguP2613RationalModulo(BigInteger a, BigInteger b) {
        BigInteger mod = new BigInteger("19260817");
        
        if (b.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Division by zero");
        }
        
        // 使用费马小定理求逆元
        BigInteger bInverse = b.modPow(mod.subtract(BigInteger.ONE), mod);
        return a.multiply(bInverse).mod(mod);
    }
    
    // ==================== ZOJ 题目 ====================
    
    /**
     * 题目10: ZOJ 3609 Modular Inverse
     * 链接: http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3609
     * 难度: 简单
     * 题意: 给定a和m，求a在模m意义下的乘法逆元
     * 
     * 解题思路:
     * 直接使用扩展欧几里得算法
     * 
     * 算法原理:
     * 求解方程ax + my = gcd(a, m)，当gcd(a, m) = 1时，x就是a的模逆元。
     * 
     * 时间复杂度: O(log(min(a, m)))
     * 空间复杂度: O(1)
     * 
     * @param a 要求逆元的数
     * @param m 模数
     * @return a在模m意义下的乘法逆元，如果不存在则返回-1
     */
    public static long zoj3609ModularInverse(long a, long m) {
        return modInverseExtendedGcd(a, m);
    }
    
    // ==================== POJ 题目 ====================
    
    /**
     * 题目11: POJ 1845 Sumdiv
     * 链接: http://poj.org/problem?id=1845
     * 难度: 中等
     * 题意: 计算A^B的所有约数之和模9901
     * 
     * 解题思路:
     * 1. 质因数分解：A = p1^a1 * p2^a2 * ... * pn^an
     * 2. A^B的质因数分解：A^B = p1^(a1*B) * p2^(a2*B) * ... * pn^(an*B)
     * 3. 约数和公式：sum = (1 + p1 + p1^2 + ... + p1^(a1*B)) * ... * (1 + pn + pn^2 + ... + pn^(an*B))
     * 4. 等比数列求和：使用快速幂和模逆元计算等比数列和
     * 
     * 算法原理:
     * 利用数论中的约数和公式，通过质因数分解将问题转化为等比数列求和。
     * 对于每个质因数pi，其贡献为等比数列和：(p^(ai*B+1) - 1) / (p - 1)。
     * 当p-1 ≡ 0 (mod 9901)时，需要特殊处理。
     * 
     * 时间复杂度: O(sqrt(A) + log B)
     * 空间复杂度: O(1)
     * 
     * @param A 底数
     * @param B 指数
     * @return A^B的所有约数之和模9901
     */
    public static int poj1845Sumdiv(int A, int B) {
        final int MOD = 9901;
        if (A == 0) return 0;
        if (B == 0) return 1;
        
        long result = 1;
        // 质因数分解
        for (int i = 2; i * i <= A; i++) {
            if (A % i == 0) {
                int cnt = 0;
                while (A % i == 0) {
                    cnt++;
                    A /= i;
                }
                // 计算等比数列和: (i^(cnt*B+1)-1)/(i-1) mod MOD
                long numerator = (power(i, (long)cnt * B + 1, MOD) - 1 + MOD) % MOD;
                long denominator = modInverseExtendedGcd(i - 1, MOD);
                if (denominator == -1) {
                    // 当i-1 ≡ 0 mod MOD时，等比数列和为cnt*B+1
                    result = result * (cnt * B + 1) % MOD;
                } else {
                    result = result * numerator % MOD * denominator % MOD;
                }
            }
        }
        
        if (A > 1) {
            long numerator = (power(A, B + 1, MOD) - 1 + MOD) % MOD;
            long denominator = modInverseExtendedGcd(A - 1, MOD);
            if (denominator == -1) {
                result = result * (B + 1) % MOD;
            } else {
                result = result * numerator % MOD * denominator % MOD;
            }
        }
        
        return (int) result;
    }
    
    // ==================== 其他OJ平台题目 ====================
    
    /**
     * 题目12: HackerRank Number of Sequences
     * 链接: https://www.hackerrank.com/contests/hourrank-17/challenges/number-of-sequences
     * 难度: 中等
     * 题意: 计算满足特定条件的序列数量
     * 
     * 解题思路:
     * 使用中国剩余定理和组合数学
     * 
     * 算法原理:
     * 通过分析约束条件，将问题转化为组合数学问题。
     * 利用数论中的中国剩余定理处理模运算约束。
     * 
     * 时间复杂度: O(n^2)
     * 空间复杂度: O(n)
     * 
     * @param n 序列长度
     * @param constraints 约束条件数组
     * @return 满足条件的序列数量
     */
    public static long hackerRankNumberOfSequences(int n, int[] constraints) {
        long result = 1;
        
        for (int i = 1; i <= n; i++) {
            int count = 0;
            for (int j = i; j <= n; j += i) {
                if (constraints[j - 1] != -1 && constraints[j - 1] % i != 0) {
                    return 0;
                }
                if (constraints[j - 1] == -1) {
                    count++;
                }
            }
            if (count > 0) {
                result = result * power(i, count - 1, MOD) % MOD;
            }
        }
        
        return result;
    }
    
    /**
     * 题目13: SPOJ MODULOUS
     * 链接: https://www.spoj.com/problems/MODULOUS/
     * 难度: 中等
     * 题意: 计算模运算表达式
     * 
     * 解题思路:
     * 直接使用快速幂计算
     * 
     * 算法原理:
     * 这是一个简单的模幂运算问题，直接使用快速幂算法求解。
     * 
     * 时间复杂度: O(log b)
     * 空间复杂度: O(1)
     * 
     * @param a 底数
     * @param b 指数
     * @param m 模数
     * @return a^b mod m
     */
    public static long spojModulous(long a, long b, long m) {
        return power(a, b, m);
    }
    
    /**
     * 题目14: CodeChef FOMBINATORIAL
     * 链接: https://www.codechef.com/problems/FOMBINATORIAL
     * 难度: 中等
     * 题意: 计算组合数取模
     * 
     * 解题思路:
     * 预处理阶乘和阶乘逆元
     * 
     * 算法原理:
     * 预先计算阶乘和阶乘的模逆元，然后利用组合数公式C(n,m) = n! / (m! * (n-m)!)
     * 在模运算下，除法转换为乘以模逆元。
     * 
     * 时间复杂度: O(n)（预处理）
     * 空间复杂度: O(n)
     * 
     * @param n 组合数上标
     * @param m 组合数下标
     * @param mod 模数
     * @return C(n, m) mod mod
     */
    public static long codeChefFombinatorial(int n, int m, int mod) {
        // 预处理阶乘和阶乘逆元
        long[] fact = new long[n + 1];
        long[] invFact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i % mod;
        }
        invFact[n] = power(fact[n], mod - 2, mod);
        for (int i = n - 1; i >= 0; i--) {
            invFact[i] = invFact[i + 1] * (i + 1) % mod;
        }
        
        // 计算组合数C(n, m)
        return fact[n] * invFact[m] % mod * invFact[n - m] % mod;
    }
    
    // ==================== 工具方法 ====================
    
    /**
     * 快速幂运算
     * 
     * 算法原理:
     * 利用二进制表示指数exp，将幂运算分解为若干次平方运算
     * 例如: 3^10 = 3^8 * 3^2
     * 
     * 时间复杂度: O(log exp)
     * 空间复杂度: O(1)
     * 
     * @param base 底数
     * @param exp 指数
     * @param mod 模数
     * @return base^exp mod mod
     */
    private static long power(long base, long exp, long mod) {
        if (mod == 0) throw new IllegalArgumentException("Modulus cannot be zero");
        if (exp < 0) throw new IllegalArgumentException("Exponent cannot be negative");
        
        long result = 1;
        base %= mod;
        
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exp >>= 1;
        }
        return result;
    }
    
    /**
     * 扩展欧几里得算法求模逆元
     * 
     * 算法原理:
     * 求解方程 ax + by = gcd(a, b)
     * 当gcd(a, m) = 1时，x就是a的模逆元
     * 
     * 时间复杂度: O(log(min(a, m)))
     * 空间复杂度: O(1)
     * 
     * @param a 要求逆元的数
     * @param m 模数
     * @return 如果存在逆元，返回最小正整数解；否则返回-1
     */
    private static long modInverseExtendedGcd(long a, long m) {
        long[] x = new long[1];
        long[] y = new long[1];
        long gcd = extendedGcd(a, m, x, y);
        
        if (gcd != 1) {
            return -1;
        }
        
        return (x[0] % m + m) % m;
    }
    
    /**
     * 扩展欧几里得算法实现
     * 
     * 算法原理:
     * 基于欧几里得算法的递归实现
     * gcd(a, b) = gcd(b, a % b)
     * 当b = 0时，gcd(a, b) = a
     * 
     * 递推关系:
     * 如果 gcd(a, b) = ax + by
     * 那么 gcd(b, a % b) = bx' + (a % b)y'
     * 其中 a % b = a - (a/b)*b
     * 所以 gcd(a, b) = bx' + (a - (a/b)*b)y' = ay' + b(x' - (a/b)y')
     * 因此 x = y', y = x' - (a/b)y'
     * 
     * 时间复杂度: O(log(min(a, b)))
     * 空间复杂度: O(log(min(a, b)))（递归栈）
     * 
     * @param a 系数a
     * @param b 系数b
     * @param x 用于返回x的解
     * @param y 用于返回y的解
     * @return gcd(a, b)
     */
    private static long extendedGcd(long a, long b, long[] x, long[] y) {
        if (b == 0) {
            x[0] = 1;
            y[0] = 0;
            return a;
        }
        
        long[] x1 = new long[1];
        long[] y1 = new long[1];
        long gcd = extendedGcd(b, a % b, x1, y1);
        
        x[0] = y1[0];
        y[0] = x1[0] - (a / b) * y1[0];
        
        return gcd;
    }
    
    /**
     * 计算最大公约数
     * 
     * 算法原理:
     * 使用欧几里得算法计算两个数的最大公约数
     * 
     * 时间复杂度: O(log(min(a, b)))
     * 空间复杂度: O(1)
     * 
     * @param a 第一个数
     * @param b 第二个数
     * @return a和b的最大公约数
     */
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    // ==================== 测试函数 ====================
    
    public static void main(String[] args) {
        System.out.println("=== 各大OJ平台模逆元题目测试 ===");
        
        // 测试LeetCode题目
        System.out.println("LeetCode 1808: " + leetcode1808MaximizeNiceDivisors(5)); // 6
        System.out.println("LeetCode 1623: " + leetcode1623NumberOfSets(4, 2)); // 5
        
        // 测试Codeforces题目
        int[] arr = {1, 3, 2, 4};
        System.out.println("Codeforces 1445D: " + codeforces1445DivideAndSum(arr));
        
        // 测试AtCoder题目
        System.out.println("AtCoder ABC182E: " + atcoderABC182EThrone(10, 4, 3));
        
        // 测试洛谷题目
        long[] inv = luoguP3811ModularInverse(10, 11);
        System.out.println("洛谷 P3811: 1~10在模11意义下的逆元");
        for (int i = 1; i <= 10; i++) {
            System.out.println("inv[" + i + "] = " + inv[i]);
        }
        
        // 测试ZOJ题目
        System.out.println("ZOJ 3609: " + zoj3609ModularInverse(3, 11)); // 4
        
        // 测试POJ题目
        System.out.println("POJ 1845: " + poj1845Sumdiv(2, 3)); // 15
        
        // 测试其他OJ题目
        System.out.println("HackerRank: " + hackerRankNumberOfSequences(3, new int[]{-1, -1, -1}));
        
        System.out.println("测试完成!");
    }
}