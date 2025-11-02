package class141;

/*
 * 洛谷 P1495【模板】中国剩余定理（CRT）/ 曹冲养猪
 * 链接：https://www.luogu.com.cn/problem/P1495
 * 题目大意：求解同余方程组 x ≡ ai (mod mi)，其中mi两两互质
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
 * 中国剩余定理是数论中的一个基本定理，用于求解模数两两互质的一元线性同余方程组。
 * 其核心思想是利用模数两两互质的性质，通过构造特定的解来求出方程组的解。
 * 
 * 时间复杂度：O(n^2 log max(mi))，其中n为方程个数，log项来自于扩展欧几里得算法
 * 空间复杂度：O(n)
 * 
 * 适用场景：
 * 1. 密码学中的一些算法（如RSA）
 * 2. 大整数计算的并行处理
 * 3. 一些数论问题
 * 
 * 注意事项：
 * 1. 要求所有模数两两互质
 * 2. 当模数较大时要注意防止溢出
 * 3. 需要实现扩展欧几里得算法来求逆元
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入是否合法
 * 2. 异常处理：处理无解的情况
 * 3. 大数处理：使用long long类型防止溢出
 * 4. 乘法优化：使用龟速乘防止乘法溢出
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
 * 1. 51Nod 1079 - 中国剩余定理
 *    链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=1079
 *    题目大意：给定一些质数p和对应余数m，求满足所有条件的最小正整数K
 * 
 * 2. POJ 1006 Biorhythms
 *    链接：http://poj.org/problem?id=1006
 *    题目大意：人的体力、情感和智力周期分别为23天、28天和33天，已知某一天三个指标的数值，求下一次三个指标同时达到峰值的天数
 * 
 * 3. UVA 756 Biorhythms
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=697
 *    题目大意：与POJ 1006相同
 * 
 * 4. 牛客网 - NC15857 同余方程
 *    链接：https://ac.nowcoder.com/acm/problem/15857
 *    题目大意：求解同余方程组，模数两两互质
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class P1495_CRT_Java {

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
            // ai = lcm / m[i]
            ai = lcm / m[i];
            // ai逆元，在%m[i]意义下的逆元
            exgcd(ai, m[i]);
            // ci = (ri * ai * ai逆元) % lcm
            ci = multiply(r[i], multiply(ai, x, lcm), lcm);
            ans = (ans + ci) % lcm;
        }
        return ans;
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
            m[i] = (long) in.nval;
            in.nextToken();
            r[i] = (long) in.nval;
        }
        out.println(crt(n));
        out.flush();
        out.close();
        br.close();
    }
}