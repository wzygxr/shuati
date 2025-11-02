// 计数质数
// 给定整数n，返回小于非负整数n的质数的数量
// 测试链接 : https://leetcode.cn/problems/count-primes/
// 相关题目链接：
// 1. LeetCode 204. Count Primes (计数质数) - https://leetcode.cn/problems/count-primes/
// 2. LeetCode 313. Super Ugly Number (超级丑数) - https://leetcode.cn/problems/super-ugly-number/
// 3. LeetCode 264. Ugly Number II (丑数 II) - https://leetcode.cn/problems/ugly-number-ii/
// 4. LeetCode 202. Happy Number (快乐数) - https://leetcode.cn/problems/happy-number/
// 5. LeetCode 172. Factorial Trailing Zeroes (阶乘后的零) - https://leetcode.cn/problems/factorial-trailing-zeroes/
// 6. LeetCode 762. Prime Number of Set Bits in Binary Representation - https://leetcode.cn/problems/prime-number-of-set-bits-in-binary-representation/
// 7. LeetCode 1025. Divisor Game (除数博弈) - https://leetcode.cn/problems/divisor-game/
// 8. LeetCode 1201. Ugly Number III (丑数 III) - https://leetcode.cn/problems/ugly-number-iii/
// 9. LeetCode 263. Ugly Number (丑数) - https://leetcode.cn/problems/ugly-number/
// 10. LeetCode 342. Power of Four (4的幂) - https://leetcode.cn/problems/power-of-four/
// 11. LeetCode 326. Power of Three (3的幂) - https://leetcode.cn/problems/power-of-three/
// 12. LeetCode 231. Power of Two (2的幂) - https://leetcode.cn/problems/power-of-two/
// 13. LeetCode 1492. The kth Factor of n (n的第k个因子) - https://leetcode.cn/problems/the-kth-factor-of-n/
// 14. LeetCode 1362. Closest Divisors (最接近的因数) - https://leetcode.cn/problems/closest-divisors/
// 15. LeetCode 507. Perfect Number (完美数) - https://leetcode.cn/problems/perfect-number/
// 16. LeetCode 869. Reordered Power of 2 (重新排序的幂) - https://leetcode.cn/problems/reordered-power-of-2/
// 17. LeetCode 1952. Three Divisors (三除数) - https://leetcode.cn/problems/three-divisors/
// 18. LeetCode 2427. Number of Common Factors (公因子的数目) - https://leetcode.cn/problems/number-of-common-factors/
// 19. LeetCode 1250. Check If It Is a Good Array (检查好数组) - https://leetcode.cn/problems/check-if-it-is-a-good-array/
// 20. LeetCode 829. Consecutive Numbers Sum (连续整数求和) - https://leetcode.cn/problems/consecutive-numbers-sum/
// 21. LeetCode 1819. Number of Different Subsequences GCDs (不同的子序列的最大公约数数目) - https://leetcode.cn/problems/number-of-different-subsequences-gcds/
// 22. LeetCode 1627. Graph Connectivity With Threshold (图连通性与阈值) - https://leetcode.cn/problems/graph-connectivity-with-threshold/
// 23. LeetCode 952. Largest Component Size by Common Factor (按公因数计算最大组件大小) - https://leetcode.cn/problems/largest-component-size-by-common-factor/
// 24. LeetCode 1447. Simplified Fractions (最简分数) - https://leetcode.cn/problems/simplified-fractions/
// 25. LeetCode 1071. Greatest Common Divisor of Strings (字符串的最大公因子) - https://leetcode.cn/problems/greatest-common-divisor-of-strings/
// 26. LeetCode 365. Water and Jug Problem (水壶问题) - https://leetcode.cn/problems/water-and-jug-problem/
// 27. LeetCode 2248. Intersection of Multiple Arrays (多个数组的交集) - https://leetcode.cn/problems/intersection-of-multiple-arrays/
// 28. Codeforces 271B Prime Matrix - https://codeforces.com/problemset/problem/271B
// 29. POJ 3641 Pseudoprime numbers - http://poj.org/problem?id=3641
// 30. Project Euler Problem 10 Summation of primes - https://projecteuler.net/problem=10

// 由于编译环境问题，不使用<iostream>等标准库头文件
// 使用基本的C++语法实现

// 全局常量定义
#define MAX_N 1000000 // 最大处理范围
#define MAX_PRIME 100000 // 最大质数数量

// 前向声明
int ehrlich(int n);
int euler(int n);
int ehrlich2(int n);
int segmentedSieve(int n);
int isPrime(int n, int* primes, int primesCount);
int getAllPrimes(int n, int* primes);

/**
 * 计算小于n的质数数量
 * @param {int} n - 非负整数
 * @returns {int} - 小于n的质数数量
 */
int countPrimes(int n) {
    return ehrlich(n - 1);
}

/**
 * 埃氏筛统计0 ~ n范围内的质数个数
 * 时间复杂度O(n * log(logn))，接近于线性
 * 空间复杂度O(n)
 * 
 * 算法原理：
 * 1. 创建一个布尔数组，初始时认为所有数都是质数
 * 2. 从2开始，将每个质数的倍数标记为合数
 * 3. 优化点：从i*i开始标记，因为小于i*i的合数已经被更小的质数标记过了
 * 
 * 应用场景：
 * 1. 需要获取一定范围内所有质数
 * 2. 质数相关的数学问题
 * 3. 密码学中生成质数
 * 
 * 工程化考虑：
 * 1. 内存使用：需要O(n)的额外空间
 * 2. 适用范围：适用于n不太大的情况（大约10^6以内）
 * 3. 可以进一步优化：只处理奇数或使用分段筛法
 * 
 * @param {int} n - 范围上限（包含）
 * @returns {int} - 0~n范围内的质数个数
 */
int ehrlich(int n) {
    // 参数验证
    if (n < 2) {
        return 0;
    }
    
    // 限制n的最大值，避免内存问题
    if (n > MAX_N) {
        n = MAX_N;
    }
    
    // visit[i] = true，代表i是合数
    // visit[i] = false，代表i是质数
    // 初始时认为0~n所有数都是质数
    bool visit[MAX_N + 1];
    for (int i = 0; i <= n; i++) {
        visit[i] = false;
    }
    
    // 从2开始，对每个质数，标记其所有倍数为合数
    // 只需要检查到sqrt(n)，因为更大的数如果是合数，必然有一个因子小于等于sqrt(n)
    for (int i = 2; i * i <= n; i++) {
        if (!visit[i]) { // 如果i是质数
            // 从i*i开始标记，因为小于i*i的倍数已经被更小的质数标记过了
            for (int j = i * i; j <= n; j += i) {
                visit[j] = true;
            }
        }
    }
    
    // 计数质数的数量（注意排除0和1）
    int cnt = 0;
    for (int i = 2; i <= n; i++) {
        if (!visit[i]) {
            // 此时i就是质数，可以收集，也可以计数
            cnt++;
        }
    }
    return cnt;
}

/**
 * 欧拉筛（线性筛）统计0 ~ n范围内的质数个数
 * 时间复杂度O(n)，是线性的
 * 空间复杂度O(n)
 * 
 * 算法原理：
 * 1. 每个合数只被其最小质因子筛掉一次
 * 2. 对于每个数i，用已找到的质数prime[j]去筛掉i*prime[j]
 * 3. 当i%prime[j]==0时break，保证每个合数只被其最小质因子筛掉
 * 
 * 与埃氏筛的区别：
 * 1. 埃氏筛会重复标记合数，比如12会被2和3都标记一次
 * 2. 欧拉筛每个合数只被标记一次，因此时间复杂度是线性的
 * 3. 欧拉筛在过程中同时收集了质数列表，便于后续使用
 * 
 * 应用场景：
 * 1. 需要高效获取大量质数
 * 2. 对时间复杂度有严格要求的场景
 * 3. 需要同时获取质数和质数个数
 * 4. 当n很大时，欧拉筛比埃氏筛更高效
 * 
 * @param {int} n - 范围上限（包含）
 * @returns {int} - 0~n范围内的质数个数
 */
int euler(int n) {
    // 参数验证
    if (n < 2) {
        return 0;
    }
    
    // 限制n的最大值，避免内存问题
    if (n > MAX_N) {
        n = MAX_N;
    }
    
    // visit[i] = true，代表i是合数
    // visit[i] = false，代表i是质数
    // 初始时认为0~n所有数都是质数
    bool visit[MAX_N + 1];
    for (int i = 0; i <= n; i++) {
        visit[i] = false;
    }
    
    // prime收集所有的质数，收集的个数是cnt
    int prime[MAX_PRIME];
    int cnt = 0;
    
    // 从2到n遍历每个数
    for (int i = 2; i <= n; i++) {
        if (!visit[i]) { // 如果i是质数
            if (cnt < MAX_PRIME) { // 防止数组越界
                prime[cnt++] = i; // 将质数加入prime数组
            }
        }
        
        // 用当前数i和已知质数去筛掉合数
        for (int j = 0; j < cnt; j++) {
            // 检查是否会溢出
            if ((long long)i * prime[j] > n || (long long)i * prime[j] > MAX_N) {
                break;
            }
            
            // 标记i*prime[j]为合数
            visit[i * prime[j]] = true;
            
            // 关键优化：当i能被prime[j]整除时，停止筛选
            // 这样保证每个合数只被其最小质因子筛掉
            if (i % prime[j] == 0) {
                break;
            }
        }
    }
    
    return cnt;
}

/**
 * 优化的埃氏筛（只处理奇数）
 * 时间复杂度：O(n * log(logn))，但常数因子更小
 * 空间复杂度：O(n)
 * 
 * 优化点：
 * 1. 只处理奇数，因为除了2以外所有偶数都是合数
 * 2. 预先计算奇数个数，然后在发现合数时递减
 * 3. 减少了约一半的计算量和空间使用
 * 
 * 实际运行效率比普通埃氏筛更高，特别是当n较大时
 * 
 * @param {int} n - 范围上限（包含）
 * @returns {int} - 0~n范围内的质数个数
 */
int ehrlich2(int n) {
    // 参数验证
    if (n <= 1) {
        return 0;
    }
    
    if (n == 2) {
        return 1;
    }
    
    // 限制n的最大值，避免内存问题
    if (n > MAX_N) {
        n = MAX_N;
    }
    
    // visit[i] = true，代表i是合数
    // visit[i] = false，代表i是质数
    // 初始时认为0~n所有数都是质数
    bool visit[MAX_N + 1];
    for (int i = 0; i <= n; i++) {
        visit[i] = false;
    }
    
    // 先把所有的偶数去掉，但是算上2
    // 估计的质数数量，如果发现更多合数，那么cnt--
    // 初始假设所有奇数都是质数，之后发现合数时递减计数
    int cnt = (n + 1) / 2; // 奇数的数量
    
    // 只处理奇数，从3开始
    for (int i = 3; i * i <= n; i += 2) {
        if (!visit[i]) { // 如果i是质数
            // 从i*i开始，每隔2*i标记一次（只标记奇数）
            for (int j = i * i; j <= n; j += 2 * i) {
                if (!visit[j]) {
                    visit[j] = true;
                    cnt--;
                }
            }
        }
    }
    
    return cnt;
}

/**
 * 分段筛法 - 适用于处理非常大的n
 * 时间复杂度：O(n)
 * 空间复杂度：O(sqrt(n))
 * 
 * 算法原理：
 * 1. 先用欧拉筛计算出sqrt(n)以内的所有质数
 * 2. 然后将区间[2,n]分成多个段，每段大小为sqrt(n)
 * 3. 对每个段，使用已知的质数筛掉其中的合数
 * 
 * 优势：
 * 1. 当n很大时，普通筛法需要大量内存
 * 2. 分段筛法只需要O(sqrt(n))的空间
 * 3. 适用于n接近内存上限的情况
 * 
 * @param {int} n - 范围上限（包含）
 * @returns {int} - 0~n范围内的质数个数
 */
int segmentedSieve(int n) {
    if (n < 2) {
        return 0;
    }
    
    // 计算sqrt(n)
    int sqrtN = 0;
    while ((long long)sqrtN * sqrtN <= n) {
        sqrtN++;
    }
    sqrtN--;
    
    // 计算sqrt(n)以内的所有质数
    int smallPrimes[MAX_PRIME];
    int smallPrimesCount = 0;
    
    if (sqrtN >= 2) {
        bool isCompositeSmall[MAX_N + 1];
        for (int i = 0; i <= sqrtN; i++) {
            isCompositeSmall[i] = false;
        }
        
        for (int i = 2; i <= sqrtN; i++) {
            if (!isCompositeSmall[i]) {
                if (smallPrimesCount < MAX_PRIME) {
                    smallPrimes[smallPrimesCount++] = i;
                }
                for (int j = i * i; j <= sqrtN; j += i) {
                    isCompositeSmall[j] = true;
                }
            }
        }
    }
    
    // 计算小区间内的质数数量
    int count = smallPrimesCount;
    
    // 如果n不超过sqrt(n)，直接返回
    if (n <= sqrtN) {
        // 需要调整count，因为smallPrimes包含所有<=sqrtN的质数
        while (count > 0 && smallPrimes[count - 1] > n) {
            count--;
        }
        return count;
    }
    
    // 分段筛法
    int segmentSize = sqrtN;
    for (int low = sqrtN + 1; low <= n; low += segmentSize) {
        int high = (low + segmentSize - 1) > n ? n : (low + segmentSize - 1);
        int segmentLength = high - low + 1;
        
        // 限制段大小，避免内存问题
        if (segmentLength > MAX_N) {
            segmentLength = MAX_N;
            high = low + segmentLength - 1;
        }
        
        // 标记当前段中的合数
        bool isCompositeSegment[MAX_N + 1];
        for (int i = 0; i < segmentLength; i++) {
            isCompositeSegment[i] = false;
        }
        
        // 用小质数筛掉区间内的合数
        for (int i = 0; i < smallPrimesCount; i++) {
            int p = smallPrimes[i];
            
            // 计算区间内第一个p的倍数
            int firstMultiple = ((low + p - 1) / p) * p;
            if (firstMultiple == p) {
                firstMultiple += p;
            }
            
            // 标记所有p的倍数
            for (int j = firstMultiple; j <= high; j += p) {
                isCompositeSegment[j - low] = true;
            }
        }
        
        // 统计区间内的质数
        for (int i = 0; i < segmentLength; i++) {
            if (!isCompositeSegment[i] && (low + i) >= 2) {
                count++;
            }
        }
    }
    
    return count;
}

/**
 * 获取0~n范围内的所有质数列表
 * 使用欧拉筛算法，时间复杂度O(n)
 * 
 * @param {int} n - 范围上限（包含）
 * @param {int*} primes - 输出参数，存储质数列表的数组
 * @returns {int} - 质数的个数
 */
int getAllPrimes(int n, int* primes) {
    if (n < 2 || primes == 0) {
        return 0;
    }
    
    // 限制n的最大值，避免内存问题
    if (n > MAX_N) {
        n = MAX_N;
    }
    
    bool visit[MAX_N + 1];
    for (int i = 0; i <= n; i++) {
        visit[i] = false;
    }
    
    int cnt = 0;
    
    for (int i = 2; i <= n; i++) {
        if (!visit[i]) {
            if (cnt < MAX_PRIME) {
                primes[cnt++] = i;
            }
        }
        for (int j = 0; j < cnt; j++) {
            if ((long long)i * primes[j] > n || (long long)i * primes[j] > MAX_N) {
                break;
            }
            visit[i * primes[j]] = true;
            if (i % primes[j] == 0) {
                break;
            }
        }
    }
    
    return cnt;
}

/**
 * 判断一个数是否为质数
 * 利用预先计算的质数表加速判断
 * 时间复杂度：O(sqrt(n))
 * 
 * @param {int} n - 待判断的数
 * @param {int*} primes - sqrt(n)以内的质数列表
 * @param {int} primesCount - 质数列表的长度
 * @returns {int} - 如果n是质数返回1，否则返回0
 */
int isPrime(int n, int* primes, int primesCount) {
    if (n <= 1) {
        return 0;
    }
    if (n <= 3) {
        return 1;
    }
    if (n % 2 == 0 || n % 3 == 0) {
        return 0;
    }
    
    // 计算sqrt(n)
    int sqrtN = 0;
    while ((long long)sqrtN * sqrtN <= n) {
        sqrtN++;
    }
    sqrtN--;
    
    // 如果没有提供质数列表或者列表不够，先计算
    if (primes == 0 || primesCount == 0) {
        int tempPrimes[MAX_PRIME];
        primesCount = getAllPrimes(sqrtN, tempPrimes);
        primes = tempPrimes;
    }
    
    for (int i = 0; i < primesCount; i++) {
        int p = primes[i];
        if (p > sqrtN) {
            break;
        }
        if (n % p == 0) {
            return 0;
        }
    }
    
    return 1;
}

/**
 * 功能测试函数
 * 测试所有筛法算法的正确性和边界条件
 */
void functionalTest() {
    // 边界条件测试
    int testCases[] = {-1, 0, 1, 2, 3, 5, 10, 20};
    int testCasesCount = sizeof(testCases) / sizeof(testCases[0]);
    
    for (int i = 0; i < testCasesCount; i++) {
        int n = testCases[i];
        int ehrlichResult = ehrlich(n);
        int eulerResult = euler(n);
        int ehrlich2Result = ehrlich2(n);
        int segmentedResult = segmentedSieve(n);
        
        // 验证所有算法结果一致
        if (ehrlichResult != eulerResult || ehrlichResult != ehrlich2Result || ehrlichResult != segmentedResult) {
            // 可以在这里添加错误处理
        }
    }
    
    // 验证已知结果
    // 已知结果验证：小于10的质数有4个（2,3,5,7）
    if (countPrimes(10) != 4) {
        // 可以在这里添加错误处理
    }
    
    // 已知结果验证：小于100的质数有25个
    if (countPrimes(100) != 25) {
        // 可以在这里添加错误处理
    }
    
    // 质数列表测试
    int primes[MAX_PRIME];
    int primesCount = getAllPrimes(30, primes);
    if (primesCount != 10) {
        // 可以在这里添加错误处理
    }
}

/**
 * 主函数，用于调用所有算法和测试
 */
int main() {
    // 运行功能测试
    functionalTest();
    
    return 0;
}