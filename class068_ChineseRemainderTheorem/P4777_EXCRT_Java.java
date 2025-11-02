package class141;

/*
 * 洛谷 P4777【模板】扩展中国剩余定理（EXCRT）
 * 链接：https://www.luogu.com.cn/problem/P4777
 * 题目大意：求解同余方程组 x ≡ ri (mod mi)，其中mi不一定两两互质
 * 
 * 算法思路：
 * 扩展中国剩余定理用于求解如下形式的一元线性同余方程组（其中m1,m2,...,mk不一定两两互质）：
 * x ≡ a1 (mod m1)
 * x ≡ a2 (mod m2)
 * ...
 * x ≡ ak (mod mk)
 * 
 * 解法思路：合并方程
 * 假设我们已经求出前k-1个方程组成的同余方程组的一个解为x，且前k-1个方程模数的最小公倍数为M，
 * 那么前k-1个方程的通解为 x + t * M (t为整数)。
 * 考虑第k个方程 x ≡ ak (mod mk)，将其与前面的通解合并：
 * x + t * M ≡ ak (mod mk)
 * t * M ≡ ak - x (mod mk)
 * 这是一个线性同余方程，可以用扩展欧几里得算法求解t。
 * 解出t后，将通解代入得到新的解和新的模数（最小公倍数）。
 * 
 * 算法原理：
 * 扩展中国剩余定理是普通中国剩余定理的扩展，用于处理模数不一定两两互质的情况。
 * 其核心思想是通过逐步合并方程的方式求解同余方程组。
 * 
 * 时间复杂度：O(n log max(mi))，其中n为方程个数，log项来自于扩展欧几里得算法
 * 空间复杂度：O(n)
 * 
 * 与普通中国剩余定理的区别：
 * 1. 普通CRT要求模数两两互质，EXCRT不要求
 * 2. EXCRT通过合并方程的方式求解，普通CRT通过构造解的方式求解
 * 3. EXCRT的核心是解线性同余方程，普通CRT的核心是求逆元
 * 
 * 适用场景：
 * 1. 模数不互质的同余方程组求解
 * 2. 一些实际问题的建模，如调度问题、周期性问题等
 * 
 * 注意事项：
 * 1. 需要熟练掌握扩展欧几里得算法
 * 2. 注意处理无解的情况（当线性同余方程无解时）
 * 3. 大数运算时要注意防止溢出
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入是否合法
 * 2. 异常处理：处理无解的情况
 * 3. 大数处理：使用long long类型防止溢出
 * 4. 乘法优化：使用龟速乘防止乘法溢出
 * 
 * 与其他算法的关联：
 * 1. 扩展欧几里得算法：核心算法
 * 2. 线性同余方程：EXCRT本质上是解线性同余方程组
 * 3. 最小公倍数：用于合并方程
 * 
 * 实际应用：
 * 1. 资源调度问题
 * 2. 周期性任务的协调
 * 3. 密码学中的大数计算
 * 
 * 相关题目：
 * 1. POJ 2891 - Strange Way to Express Integers
 *    链接：http://poj.org/problem?id=2891
 *    题目大意：给定n个形如 x ≡ ri (mod mi) 的同余方程，求最小非负整数解，mi不一定两两互质
 * 
 * 2. HDU 3579 Hello Kiki
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=3579
 *    题目大意：求解同余方程组，模数不一定互质
 * 
 * 3. UVa 11754 Code Feat
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2854
 *    题目大意：给定C个条件，每个条件形如N除以X的余数在集合Y中，求前S个满足条件的数
 * 
 * 4. AtCoder Beginner Contest 186 F. Rook on Grid
 *    链接：https://atcoder.jp/contests/abc186/tasks/abc186_f
 *    解题思路：可使用EXCRT解决的周期性问题
 * 
 * 5. SPOJ - MOD
 *    链接：https://www.spoj.com/problems/MOD/
 *    题目大意：求解同余方程组，模数不一定互质
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class P4777_EXCRT_Java {

    public static int MAXN = 100001;

    public static long m[] = new long[MAXN];

    public static long r[] = new long[MAXN];

    // 扩展中国剩余定理模版
    public static long excrt(int n) {
        long tail = 0, lcm = 1, tmp, b, c, x0;
        // ans = lcm * x + tail
        for (int i = 1; i <= n; i++) {
            // ans = m[i] * y + ri
            // lcm * x + m[i] * y = ri - tail
            // a = lcm
            // b = m[i]
            // c = ri - tail
            b = m[i];
            c = ((r[i] - tail) % b + b) % b;
            exgcd(lcm, b);
            if (c % d != 0) {
                return -1;
            }
            // ax + by = gcd(a,b)，特解是，x变量
            // ax + by = c，特解是，x变量 * (c/d)
            // ax + by = c，最小非负特解x0 = (x * (c/d)) % (b/d) 取非负余数
            // 通解 = x0 + (b/d) * n
            x0 = multiply(x, c / d, b / d);
            // ans = lcm * x + tail，带入通解
            // ans = lcm * (x0 + (b/d) * n) + tail
            // ans = lcm * (b/d) * n + lcm * x0 + tail
            // tail' = tail' % lcm'
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
            m[i] = (long) in.nval;
            in.nextToken();
            r[i] = (long) in.nval;
        }
        out.println(excrt(n));
        
        out.flush();
        out.close();
        br.close();
    }
}