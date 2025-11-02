// 判断较大的数字是否是质数(Miller-Rabin测试)
// 测试链接 : https://www.luogu.com.cn/problem/U148828
// 本文件可以搞定10^9范围内数字的质数检查
// 时间复杂度O(s * (logn)的三次方)，很快
// 为什么不能搞定所有long类型的数字检查
// 原因在于long类型位数不够，乘法同余的时候会溢出

// Miller-Rabin算法详解：
// Miller-Rabin是一种概率性素性测试算法，基于费马小定理和二次探测定理
// 算法原理：
// 1. 将n-1表示为u*2^t的形式，其中u是奇数
// 2. 随机选择一个底数a(1<a<n-1)
// 3. 计算a^u mod n，如果结果为1或-1，则n可能是素数
// 4. 否则，重复计算(a^u)^(2^i) mod n，i从1到t-1，如果结果为-1，则n可能是素数
// 5. 如果以上条件都不满足，则n是合数
// 
// 相关题目：
// 1. POJ 1811 Prime Test
//    链接：http://poj.org/problem?id=1811
//    题目描述：给定一个大整数(2 <= N < 2^54)，判断它是否为素数，如果不是输出最小质因子
// 2. Luogu U148828 大数质数判断
//    链接：https://www.luogu.com.cn/problem/U148828
//    题目描述：判断给定的大整数是否为质数
// 3. Codeforces 679A Bear and Prime 100 (交互题)
//    链接：https://codeforces.com/problemset/problem/679/A
//    题目描述：系统想了一个2到100之间的数，你需要通过最多20次询问判断这个数是否为质数

// 由于编译环境问题，不使用<iostream>等标准库头文件
// 使用基本的C++语法实现

// 质数的个数代表测试次数
// 如果想增加测试次数就继续增加更大的质数
// 使用前12个质数作为测试底数，可以有效降低误判率
long long p[12] = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37 };

/**
 * 快速幂运算：计算 n^p mod mod
 * 时间复杂度：O(log p)
 * 空间复杂度：O(1)
 * 
 * @param n 底数
 * @param p 指数
 * @param mod 模数
 * @return n^p mod mod
 * 
 * 算法原理：
 * 1. 将指数p用二进制表示
 * 2. 从低位到高位，如果该位为1，则将当前底数乘入结果
 * 3. 每次将底数平方，指数右移一位
 */
long long power(long long n, long long p, long long mod) {
    long long ans = 1;
    while (p > 0) {
        if ((p & 1) == 1) {
            ans = (ans * n) % mod;
        }
        n = (n * n) % mod;
        p >>= 1;
    }
    return ans;
}

/**
 * Miller-Rabin单次测试函数
 * 
 * @param a 测试底数
 * @param n 待测试数
 * @return 如果n是合数返回true，否则返回false
 * 
 * 算法原理：
 * 1. 将n-1表示为u*2^t的形式，其中u是奇数
 * 2. 计算a^u mod n
 * 3. 如果结果为1或n-1，则通过本次测试
 * 4. 否则，重复计算平方模运算t-1次
 * 5. 如果在过程中得到n-1，则通过本次测试
 * 6. 否则，n是合数
 */
bool witness(long long a, long long n) {
    long long u = n - 1;
    int t = 0;
    // 将n-1分解为u*2^t的形式，其中u是奇数
    while ((u & 1) == 0) {
        t++;
        u >>= 1;
    }
    // 计算a^u mod n
    long long x1 = power(a, u, n), x2;
    for (int i = 1; i <= t; i++) {
        x2 = power(x1, 2, n);
        // 二次探测：如果x2=1但x1既不是1也不是n-1，则存在非平凡平方根，n是合数
        if (x2 == 1 && x1 != 1 && x1 != n - 1) {
            return true;
        }
        x1 = x2;
    }
    // 如果最后结果不是1，则违反费马小定理，n是合数
    if (x1 != 1) {
        return true;
    }
    return false;
}

/**
 * Miller-Rabin素性测试主函数
 * 时间复杂度：O(s * (logn)^3)，其中s是测试轮数
 * 空间复杂度：O(1)
 * 
 * @param n 待测试的数
 * @return 如果是质数返回true，否则返回false
 * 
 * 算法特点：
 * 1. 这是一个概率算法，有一定误判率
 * 2. 对于合数，误判为质数的概率不超过(1/4)^s
 * 3. 对于质数，永远不会误判
 * 
 * 工程化考虑：
 * 1. 使用固定的质数作为底数，提高稳定性
 * 2. 对于小数和偶数进行特殊处理，提高效率
 */
bool millerRabin(long long n) {
    if (n <= 2) {
        return n == 2;
    }
    // 偶数(除了2)都不是质数
    if ((n & 1) == 0) {
        return false;
    }
    for (int i = 0; i < 12 && p[i] < n; i++) {
        // witness函数用于单次测试
        if (witness(p[i], n)) {
            return false;
        }
    }
    return true;
}