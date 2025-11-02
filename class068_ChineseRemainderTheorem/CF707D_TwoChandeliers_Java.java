package class141;

/*
 * Codeforces 707D Two chandeliers
 * 链接：https://codeforces.com/contest/1483/problem/D
 * 题目大意：有两个循环亮灯的序列，每天亮一种颜色的灯，老板会在两个灯颜色相同时生气，
 *           求第k次生气在第几天
 * 
 * 算法思路：
 * 这道题可以转化为扩展中国剩余定理问题。
 * 设两个序列的长度分别为n和m，第i天第一个序列亮第((i-1) mod n + 1)个灯，
 * 第二个序列亮第((i-1) mod m + 1)个灯。
 * 当两个灯颜色相同时老板生气。
 * 
 * 我们可以枚举所有颜色相同的配对，对于每一对(i,j)满足a[i] = b[j]，
 * 我们需要找到满足以下条件的第k个正整数x：
 * x ≡ i (mod n)
 * x ≡ j (mod m)
 * 
 * 这就转化为了扩展中国剩余定理问题。
 * 
 * 算法原理：
 * 这道题展示了扩展中国剩余定理在解决周期性问题中的应用，
 * 通过枚举所有可能的配对并使用EXCRT求解同余方程组。
 * 
 * 时间复杂度：O(n*m*log(max(n,m)))
 * 空间复杂度：O(n+m)
 * 
 * 适用场景：
 * 1. 周期性事件分析
 * 2. 调度问题
 * 
 * 注意事项：
 * 1. 需要使用扩展中国剩余定理处理模数不互质的情况
 * 2. 需要处理大数运算
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入是否合法
 * 2. 异常处理：处理无解的情况
 * 3. 大数处理：使用long long类型防止溢出
 * 4. 优化：可以使用二分查找优化
 * 
 * 与其他算法的关联：
 * 1. 扩展中国剩余定理：核心算法
 * 2. 二分查找：可选的优化方法
 * 
 * 实际应用：
 * 1. 资源调度
 * 2. 周期性任务协调
 * 
 * 相关题目：
 * 1. NOI 2018 屠龙勇士
 *    链接：https://www.luogu.com.cn/problem/P4774
 *    题目大意：游戏中需要击败n条龙，每条龙有血量hp[i]和恢复能力recovery[i]，勇士有m把剑，每把剑有攻击力，求最少攻击次数
 * 
 * 2. UVa 11754 Code Feat
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2854
 *    题目大意：给定C个条件，每个条件形如N除以X的余数在集合Y中，求前S个满足条件的数
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.*;

public class CF707D_TwoChandeliers_Java {

    // 扩展欧几里得算法
    public static long[] exgcd(long a, long b) {
        if (b == 0) {
            return new long[]{a, 1, 0};
        } else {
            long[] result = exgcd(b, a % b);
            long d = result[0];
            long x = result[1];
            long y = result[2];
            result[1] = y;
            result[2] = x - (a / b) * y;
            return result;
        }
    }

    // 求最大公约数
    public static long gcd(long a, long b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    // 求最小公倍数
    public static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
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

    // 线性同余方程 ax ≡ b (mod m) 求解
    public static long linearCongruence(long a, long b, long m) {
        long[] result = exgcd(a, m);
        long d = result[0];
        long x = result[1];
        if (b % d != 0) {
            return -1; // 无解
        }
        x = x * (b / d) % (m / d);
        return x;
    }

    // 扩展中国剩余定理模版
    public static long excrt(long[] m, long[] r) {
        int n = m.length;
        // 初始解为0，模数为1
        long ans = 0;
        long mod = 1;
        
        for (int i = 0; i < n; i++) {
            // 当前方程：x ≡ r[i] (mod m[i])
            // 之前的通解：x = ans + k * mod
            // 合并：ans + k * mod ≡ r[i] (mod m[i])
            // 即：k * mod ≡ r[i] - ans (mod m[i])
            
            // 计算 b = r[i] - ans
            long b = (r[i] - ans) % m[i];
            b = (b + m[i]) % m[i];
            
            // 求解线性同余方程：k * mod ≡ b (mod m[i])
            long[] exResult = exgcd(mod, m[i]);
            long d = exResult[0];
            long x = exResult[1];
            if (b % d != 0) {
                return -1; // 无解
            }
            x = x * (b / d) % (m[i] / d);
            
            // 更新解和模数
            ans = ans + x * mod;
            mod = lcm(mod, m[i]);
            ans = (ans % mod + mod) % mod;
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int m = (int) in.nval;
        in.nextToken();
        long k = (long) in.nval;
        
        int[] a = new int[n + 1];
        int[] b = new int[m + 1];
        
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            a[i] = (int) in.nval;
        }
        
        for (int i = 1; i <= m; i++) {
            in.nextToken();
            b[i] = (int) in.nval;
        }
        
        // 构建颜色到位置的映射
        Map<Integer, List<Integer>> colorToPosA = new HashMap<>();
        Map<Integer, List<Integer>> colorToPosB = new HashMap<>();
        
        for (int i = 1; i <= n; i++) {
            colorToPosA.computeIfAbsent(a[i], x -> new ArrayList<>()).add(i);
        }
        
        for (int i = 1; i <= m; i++) {
            colorToPosB.computeIfAbsent(b[i], x -> new ArrayList<>()).add(i);
        }
        
        // 收集所有可能的解
        List<Long> solutions = new ArrayList<>();
        
        // 枚举所有颜色相同的配对
        for (Map.Entry<Integer, List<Integer>> entry : colorToPosA.entrySet()) {
            int color = entry.getKey();
            List<Integer> posA = entry.getValue();
            
            if (colorToPosB.containsKey(color)) {
                List<Integer> posB = colorToPosB.get(color);
                
                // 对每一对位置，求解同余方程组
                for (int i : posA) {
                    for (int j : posB) {
                        // x ≡ i (mod n)
                        // x ≡ j (mod m)
                        long[] mods = {n, m};
                        long[] remainders = {i, j};
                        long solution = excrt(mods, remainders);
                        if (solution != -1) {
                            // 生成所有解：solution + k * lcm(n, m)
                            long period = lcm(n, m);
                            long current = solution;
                            while (current <= k * period) {
                                solutions.add(current);
                                current += period;
                                if (solutions.size() > k) break;
                            }
                        }
                    }
                }
            }
        }
        
        // 排序并取第k个解
        Collections.sort(solutions);
        if (k <= solutions.size()) {
            out.println(solutions.get((int)k - 1));
        } else {
            out.println(-1);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}