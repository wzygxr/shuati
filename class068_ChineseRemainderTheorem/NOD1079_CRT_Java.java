package class141;

/*
 * 51Nod 1079 中国剩余定理
 * 链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=1079
 * 题目大意：给定一些质数p和对应余数m，求满足所有条件的最小正整数K
 *           一个正整数K，给出K Mod 一些质数的结果，求符合条件的最小的K。
 *           例如，K % 2 = 1, K % 3 = 2, K % 5 = 3。符合条件的最小的K = 23。
 * 
 * 算法思路：
 * 这是一个标准的中国剩余定理应用题。题目保证给出的质数两两互质，可以直接应用中国剩余定理。
 * 
 * 解法步骤：
 * 1. 使用中国剩余定理求解同余方程组
 * 2. 注意输入的质数都是质数，所以两两互质
 * 
 * 算法原理：
 * 这是一道基础的中国剩余定理应用题，展示了CRT在解决同余方程组中的应用。
 * 由于题目保证所有模数都是质数，所以可以直接使用CRT。
 * 
 * 时间复杂度：O(n^2 log max(mi))，其中n为方程个数，log项来自于扩展欧几里得算法
 * 空间复杂度：O(n)
 * 
 * 适用场景：
 * 1. 数论问题求解
 * 2. 密码学中的大数计算
 * 
 * 注意事项：
 * 1. 题目保证所有模数都是质数，所以两两互质
 * 2. 需要处理大数运算
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入是否合法
 * 2. 异常处理：处理无解的情况
 * 3. 大数处理：使用long long类型防止溢出
 * 
 * 与其他算法的关联：
 * 1. 中国剩余定理：核心算法
 * 2. 扩展欧几里得算法：用于求逆元
 * 
 * 实际应用：
 * 1. RSA加密算法中的中国剩余定理加速
 * 2. 多精度整数运算的并行处理
 * 
 * 相关题目：
 * 1. 洛谷 P1495 - 曹冲养猪
 *    链接：https://www.luogu.com.cn/problem/P1495
 *    题目大意：求解同余方程组 x ≡ ai (mod mi)，其中mi两两互质
 * 
 * 2. POJ 1006 Biorhythms
 *    链接：http://poj.org/problem?id=1006
 *    题目大意：人的体力、情感和智力周期分别为23天、28天和33天，已知某一天三个指标的数值，求下一次三个指标同时达到峰值的天数
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class NOD1079_CRT_Java {

    public static int MAXN = 11;

    public static long m[] = new long[MAXN];

    public static long r[] = new long[MAXN];

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