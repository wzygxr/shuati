#include <iostream>
#include <vector>

/**
 * 洗牌 - C++实现
 * 
 * 题目描述：
 * 一共有n张牌，n一定是偶数，每张牌的牌面从1到n，洗牌规则如下
 * 比如n = 6，牌面最初排列为1 2 3 4 5 6
 * 先分成左堆1 2 3，右堆4 5 6，然后按照右堆第i张在前，左堆第i张在后的方式依次放置
 * 所以洗一次后，得到 4 1 5 2 6 3
 * 如果再洗一次，得到 2 4 6 1 3 5
 * 如果再洗一次，得到 1 2 3 4 5 6
 * 想知道n张牌洗m次的之后，第l张牌，是什么牌面
 * 
 * 解题思路：
 * 1. 通过数学推导找到洗牌的规律
 * 2. 使用快速幂和扩展欧几里得算法求解
 * 
 * 算法复杂度：
 * 时间复杂度：O(log m)
 * 空间复杂度：O(log m)
 * 
 * 题目链接：
 * 洛谷 P2054 [AHOI2005] 洗牌
 * https://www.luogu.com.cn/problem/P2054
 * 
 * 相关题目：
 * 1. 洛谷 P1082 [NOIP2012 提高组] 同余方程
 *    链接：https://www.luogu.com.cn/problem/P1082
 *    本题需要使用扩展欧几里得算法求模逆元
 * 
 * 2. 洛谷 P1516 青蛙的约会
 *    链接：https://www.luogu.com.cn/problem/P1516
 *    本题需要求解同余方程，是扩展欧几里得算法的经典应用
 * 
 * 3. Codeforces 1244C. The Football Stage
 *    链接：https://codeforces.com/problemset/problem/1244/C
 *    本题需要求解线性丢番图方程
 * 
 * 工程化考虑：
 * 1. 异常处理：需要处理输入非法、大数等情况
 * 2. 边界条件：需要考虑n、m、l的边界值
 * 3. 性能优化：使用快速幂和扩展欧几里得算法
 * 
 * 调试能力：
 * 1. 添加断言验证中间结果
 * 2. 打印关键变量的实时值
 * 3. 性能退化排查
 */

/**
 * 扩展欧几里得算法
 * 
 * 算法原理：
 * 1. 当b=0时，gcd(a,0)=a，此时x=1, y=0
 * 2. 当b≠0时，递归计算gcd(b, a%b)的解x1, y1
 * 3. 根据等式推导：x = y1, y = x1 - (a/b) * y1
 * 
 * 时间复杂度：O(log(min(a, b)))
 * 空间复杂度：O(log(min(a, b)))（递归调用栈）
 * 
 * @param a 系数a
 * @param b 系数b
 * @param x 存储x解的引用
 * @param y 存储y解的引用
 * @return a和b的最大公约数
 */
long long exgcd(long long a, long long b, long long &x, long long &y) {
    if (b == 0) {
        x = 1;
        y = 0;
        return a;
    }
    long long x1, y1;
    long long gcd = exgcd(b, a % b, x1, y1);
    x = y1;
    y = x1 - (a / b) * y1;
    return gcd;
}

/**
 * 龟速乘法（防止溢出的乘法实现）
 * 
 * 算法原理：
 * 通过位运算实现乘法，每个中间过程都取模，防止溢出
 * 
 * 时间复杂度：O(log b)
 * 空间复杂度：O(1)
 * 
 * @param a 乘数a
 * @param b 乘数b
 * @param mod 模数
 * @return (a * b) % mod
 */
long long multiply(long long a, long long b, long long mod) {
    // 既然是在%mod的意义下，那么a和b可以都转化成非负的
    // 本题不转化无所谓，但是其他题目可能需要转化
    // 尤其是b需要转化，否则while循环会跑不完
    a = (a % mod + mod) % mod;
    b = (b % mod + mod) % mod;
    long long ans = 0;
    while (b != 0) {
        if (b & 1) {
            ans = (ans + a) % mod;
        }
        a = (a + a) % mod;
        b >>= 1;
    }
    return ans;
}

/**
 * 快速幂算法
 * 
 * 算法原理：
 * 通过位运算实现快速幂，每个中间过程都取模，防止溢出
 * 
 * 时间复杂度：O(log b)
 * 空间复杂度：O(1)
 * 
 * @param a 底数
 * @param b 指数
 * @param mod 模数
 * @return (a^b) % mod
 */
long long power(long long a, long long b, long long mod) {
    long long ans = 1;
    while (b > 0) {
        if (b & 1) {
            ans = multiply(ans, a, mod);
        }
        a = multiply(a, a, mod);
        b >>= 1;
    }
    return ans;
}

/**
 * 计算洗牌后第l张牌的牌面
 * 
 * 算法思路：
 * 1. 根据数学推导，洗m次后第l张牌的牌面为 (2^m * l) % (n+1)
 * 2. 由于n和m可能很大，需要使用快速幂和模逆元
 * 
 * @param n 牌的数量
 * @param m 洗牌次数
 * @param l 位置
 * @return 洗牌后第l张牌的牌面
 */
long long compute(long long n, long long m, long long l) {
    long long mod = n + 1;
    long long x, y;
    exgcd(power(2, m, mod), mod, x, y);
    long long x0 = (x % mod + mod) % mod;
    return multiply(x0, l, mod);
}

/**
 * 主方法 - 洗牌问题测试
 */
int main() {
    std::cout << "=== 洗牌问题测试 ===" << std::endl;
    
    // 测试用例1
    long long n1 = 6, m1 = 1, l1 = 1;
    long long result1 = compute(n1, m1, l1);
    std::cout << "测试1: n=" << n1 << ", m=" << m1 << ", l=" << l1 
              << ", 结果: " << result1 << std::endl;
    
    // 测试用例2
    long long n2 = 6, m2 = 2, l2 = 1;
    long long result2 = compute(n2, m2, l2);
    std::cout << "测试2: n=" << n2 << ", m=" << m2 << ", l=" << l2 
              << ", 结果: " << result2 << std::endl;
    
    // 测试用例3
    long long n3 = 6, m3 = 3, l3 = 1;
    long long result3 = compute(n3, m3, l3);
    std::cout << "测试3: n=" << n3 << ", m=" << m3 << ", l=" << l3 
              << ", 结果: " << result3 << std::endl;
    
    std::cout << "=== 测试完成 ===" << std::endl;
    
    return 0;
}