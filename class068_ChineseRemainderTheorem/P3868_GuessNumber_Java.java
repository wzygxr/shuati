package class141;

/*
 * 洛谷 P3868【TJOI2009】猜数字
 * 链接：https://www.luogu.com.cn/problem/P3868
 * 题目大意：
 * 现在有两组数字，a1,a2,...,an 和 b1,b2,...,bn。其中，ai两两互质。
 * 现在请求出一个最小的正整数N，使得bi | (N - ai) 对所有i成立。
 * 
 * 转化思路：
 * 条件bi | (N - ai)等价于N ≡ ai (mod bi)
 * 这就转化为了标准的中国剩余定理问题
 * 
 * 算法思路：
 * 中国剩余定理用于求解如下形式的一元线性同余方程组（其中m1,m2,...,mk两两互质）：
 * x ≡ a1 (mod m1)
 * x ≡ a2 (mod m2)
 * ...
 * x ≡ ak (mod mk)
 * 
 * 解法步骤：
 * 1. 计算所有模数的乘积 M = m1 * m2 * ... * mk
 * 2. 对于第i个方程：
 *    a. 计算 Mi = M / mi
 *    b. 计算 Mi 在模 mi 意义下的逆元 Mi^(-1)
 *    c. 计算 ci = Mi * Mi^(-1)
 * 3. 方程组在模 M 意义下的唯一解为 x = (a1*c1 + a2*c2 + ... + ak*ck) mod M
 * 
 * 算法原理：
 * 这是一道综合性的题目，既可以用中国剩余定理解决，也可以用扩展中国剩余定理解决。
 * 由于题目中说明ai两两互质，所以可以直接使用CRT。
 * 
 * 时间复杂度：O(n^2 log max(mi))，其中n为方程个数，log项来自于扩展欧几里得算法
 * 空间复杂度：O(n)
 * 
 * 特殊处理：
 * 1. 输入的ai可能是负数，需要转换为正数：ai = (ai % mi + mi) % mi
 * 2. 本题可以使用扩展中国剩余定理解决，因为扩展中国剩余定理不要求模数两两互质，适用范围更广
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入是否合法
 * 2. 异常处理：处理无解的情况
 * 3. 大数处理：使用long long类型防止溢出
 * 4. 乘法优化：使用龟速乘防止乘法溢出
 * 
 * 适用场景：
 * 1. 密码学中的大数计算
 * 2. 周期性问题的求解
 * 3. 数论问题的建模
 * 
 * 与其他算法的关联：
 * 1. 扩展欧几里得算法：用于求解逆元和线性同余方程
 * 2. 快速幂：在某些变种中用于求逆元
 * 3. 线性同余方程：CRT本质上是解线性同余方程组
 * 
 * 实际应用：
 * 1. RSA加密算法中的中国剩余定理加速
 * 2. 多精度整数运算的并行处理
 * 3. 信号处理中的采样定理
 * 
 * 相关题目：
 * 1. 洛谷 P1495 - 曹冲养猪
 *    链接：https://www.luogu.com.cn/problem/P1495
 *    题目大意：求解同余方程组 x ≡ ai (mod mi)，其中mi两两互质
 * 
 * 2. 洛谷 P4777【模板】扩展中国剩余定理
 *    链接：https://www.luogu.com.cn/problem/P4777
 *    题目大意：求解同余方程组 x ≡ ri (mod mi)，其中mi不一定两两互质
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class P3868_GuessNumber_Java {

    public static int MAXN = 11;

    public static long m[] = new long[MAXN];

    public static long r[] = new long[MAXN];

    // 中国剩余定理模版
    public static long crt(int n) {
        long lcm = 1;
        for (int i = 1; i <= n; i++) {
            lcm = lcm * m[i];
        }
        long ai, ci, ans = 0;
        for (int i = 1; i <= n; i++) {
            ai = lcm / m[i];
            exgcd(ai, m[i]);
            ci = multiply(r[i], multiply(ai, x, lcm), lcm);
            ans = (ans + ci) % lcm;
        }
        return ans;
    }

    // 扩展中国剩余定理模版
    public static long excrt(int n) {
        long tail = 0, lcm = 1, tmp, b, c, x0;
        for (int i = 1; i <= n; i++) {
            b = m[i];
            c = ((r[i] - tail) % b + b) % b;
            exgcd(lcm, b);
            if (c % d != 0) {
                return -1;
            }
            x0 = multiply(x, c / d, b / d);
            tmp = lcm * (b / d);
            tail = (tail + multiply(x0, lcm, tmp)) % tmp;
            lcm = tmp;
        }
        return tail;
    }

    // 扩展欧几里得算法
    public static long d, x, y, px, py;

    public static void exgcd(long a, long b) {
        if (b == 0) {
            d = a;
            x = 1;
            y = 0;
        } else {
            exgcd(b, a % b);
            px = x;
            py = y;
            x = py;
            y = px - py * (a / b);
        }
    }

    // 龟速乘法，防止溢出
    public static long multiply(long a, long b, long mod) {
        a = (a % mod + mod) % mod;
        b = (b % mod + mod) % mod;
        long ans = 0;
        while (b != 0) {
            if ((b & 1) != 0) {
                ans = (ans + a) % mod;
            }
            a = (a + a) % mod;
            b >>= 1;
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        int n = (int) in.nval;
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            r[i] = (long) in.nval;
        }
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            m[i] = (long) in.nval;
        }
        // 题目输入的余数可能为负所以转化成非负数
        for (int i = 1; i <= n; i++) {
            r[i] = (r[i] % m[i] + m[i]) % m[i];
        }
        // out.println(crt(n)); // 中国剩余定理解决
        out.println(excrt(n)); // 扩展中国剩余定理解决
        
        out.flush();
        out.close();
        br.close();
    }
}