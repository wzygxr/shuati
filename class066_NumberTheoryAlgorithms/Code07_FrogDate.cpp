#include <stdio.h>
#include <stdlib.h>

/**
 * 青蛙的约会
 * 
 * 题目描述：
 * 两只青蛙在网上相识了，它们聊得很开心，于是觉得很有必要见一面。
 * 它们很高兴地发现它们住在同一条纬度线上，于是它们约定各自朝西跳，直到碰面为止。
 * 两只青蛙初始位置分别为x和y，每次跳跃距离分别为m和n，纬度线总长为L。
 * 问它们跳多少次以后才能相遇。
 * 
 * 解题思路：
 * 1. 设跳了t次后相遇，则有：(x + m*t) % L = (y + n*t) % L
 * 2. 转化为：(x + m*t) ≡ (y + n*t) (mod L)
 * 3. 移项得：(m-n)*t ≡ (y-x) (mod L)
 * 4. 这是一个线性同余方程，可以用扩展欧几里得算法求解
 * 
 * 时间复杂度：O(log(min(a,b)))，其中a=m-n, b=L
 * 空间复杂度：O(1)
 * 
 * 测试链接：http://poj.org/problem?id=1061
 * 洛谷链接：https://www.luogu.com.cn/problem/P1516
 */

// 用于存储扩展欧几里得算法的结果
long result[3]; // [gcd, x, y]

/**
 * 扩展欧几里得算法 - 迭代实现
 * 求解 ax + by = gcd(a,b) 的一组整数解 x, y
 * 
 * 算法思路：
 * 1. 初始化 x0=1, y0=0, x1=0, y1=1
 * 2. 当 b≠0 时，计算 q=a/b, r=a%b
 * 3. 更新系数：x=x0-q*x1, y=y0-q*y1
 * 4. 更新变量：a=b, b=r, x0=x1, y0=y1, x1=x, y1=y
 * 5. 重复步骤2-4直到 b=0
 * 
 * 时间复杂度：O(log(min(a,b)))
 * 空间复杂度：O(1)
 * 
 * @param a 系数a
 * @param b 系数b
 * @param result 存储结果的数组，result[0]为gcd, result[1]为x, result[2]为y
 */
void exgcdIterative(long a, long b, long* result) {
    // 初始化系数
    long x0 = 1, y0 = 0;  // 初始解 (1, 0)
    long x1 = 0, y1 = 1;  // 初始解 (0, 1)
    
    // 迭代计算
    while (b != 0) {
        long q = a / b;  // 商
        long r = a % b;  // 余数
        
        // 更新系数
        long x = x0 - q * x1;
        long y = y0 - q * y1;
        
        // 更新变量
        a = b;
        b = r;
        x0 = x1;
        y0 = y1;
        x1 = x;
        y1 = y;
    }
    
    // 返回结果
    result[0] = a;   // gcd
    result[1] = x0;  // x
    result[2] = y0;  // y
}

/**
 * 求解线性同余方程
 * 求解 ax ≡ b (mod m) 的最小非负整数解 x
 * 
 * 算法思路：
 * 1. 将同余方程转换为不定方程：ax + my = b
 * 2. 使用扩展欧几里得算法求解 ax + my = gcd(a,m)
 * 3. 如果 b 不能被 gcd(a,m) 整除，则方程无解
 * 4. 否则，将解乘以 b/gcd(a,m) 得到特解
 * 5. 调整为最小非负整数解
 * 
 * 时间复杂度：O(log(min(a,m)))
 * 空间复杂度：O(1)
 * 
 * @param a 系数a
 * @param b 等式右边
 * @param m 模数
 * @return 方程的最小非负整数解，如果无解则返回 -1
 */
long linearCongruence(long a, long b, long m) {
    exgcdIterative(a, m, result);
    
    long gcd = result[0];
    long x = result[1];
    
    // 如果b不能被gcd整除，则方程无解
    if (b % gcd != 0) {
        return -1;
    }
    
    // 计算解
    long mod = m / gcd;
    long sol = ((x * (b / gcd)) % mod + mod) % mod;
    return sol;
}

/**
 * 求解青蛙约会问题
 * 
 * @param x 青蛙A的初始位置
 * @param y 青蛙B的初始位置
 * @param m 青蛙A每次跳跃距离
 * @param n 青蛙B每次跳跃距离
 * @param L 纬度线长度
 * @return 相遇所需跳跃次数，如果无法相遇则返回-1
 */
long solveFrogDate(long x, long y, long m, long n, long L) {
    // 方程转化为 (m-n)*t ≡ (y-x) (mod L)
    long a = m - n;
    long b = y - x;
    
    // 处理负数情况
    a = (a % L + L) % L;
    b = (b % L + L) % L;
    
    // 特殊情况处理
    if (a == 0) {
        if (b == 0) {
            return 0; // 初始位置就相同
        } else {
            return -1; // 无法相遇
        }
    }
    
    // 求解线性同余方程
    return linearCongruence(a, b, L);
}

/**
 * 主方法 - 测试青蛙约会问题
 */
int main() {
    printf("=== 青蛙的约会问题测试 ===\n\n");
    
    // 测试用例1
    long x1 = 1, y1 = 2, m1 = 3, n1 = 4, L1 = 5;
    long result1 = solveFrogDate(x1, y1, m1, n1, L1);
    printf("测试1: x=%ld, y=%ld, m=%ld, n=%ld, L=%ld\n", x1, y1, m1, n1, L1);
    if (result1 != -1) {
        printf("结果: %ld次后相遇\n", result1);
    } else {
        printf("结果: 无法相遇\n");
    }
    
    // 测试用例2
    long x2 = 5, y2 = 10, m2 = 2, n2 = 3, L2 = 10;
    long result2 = solveFrogDate(x2, y2, m2, n2, L2);
    printf("\n测试2: x=%ld, y=%ld, m=%ld, n=%ld, L=%ld\n", x2, y2, m2, n2, L2);
    if (result2 != -1) {
        printf("结果: %ld次后相遇\n", result2);
    } else {
        printf("结果: 无法相遇\n");
    }
    
    printf("\n=== 测试完成 ===\n");
    return 0;
    
    return 0;
}