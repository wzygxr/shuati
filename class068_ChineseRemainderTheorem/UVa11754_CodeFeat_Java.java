package class141;

/*
 * UVa 11754 Code Feat
 * 链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2854
 * 题目大意：给定C个条件，每个条件形如N除以X的余数在集合Y中，求前S个满足条件的数
 * 
 * 算法思路：
 * 这道题是扩展中国剩余定理的变种。
 * 题目给出C个条件，每个条件是N % X_i ∈ Y_i，求前S个满足所有条件的正整数N。
 * 
 * 解法思路：
 * 1. 对于每个条件，我们有N ≡ y (mod x)，其中y ∈ Y_i
 * 2. 我们可以枚举所有可能的组合，对每个组合使用扩展中国剩余定理求解
 * 3. 由于解可能很大，我们需要找到最小正解，然后生成后续解
 * 
 * 算法原理：
 * 这是一道综合性的题目，结合了枚举和扩展中国剩余定理，
 * 展示了EXCRT在处理多个约束条件问题中的应用。
 * 
 * 时间复杂度：取决于枚举的组合数和EXCRT的复杂度
 * 空间复杂度：O(C + sum of |Y_i|)
 * 
 * 适用场景：
 * 1. 约束满足问题
 * 2. 数论问题
 * 
 * 注意事项：
 * 1. 需要处理多个余数集合的情况
 * 2. 需要生成前S个解
 * 3. 需要考虑解的周期性
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入是否合法
 * 2. 异常处理：处理无解的情况
 * 3. 大数处理：使用long long类型防止溢出
 * 4. 优化：可以使用优先队列优化
 * 
 * 与其他算法的关联：
 * 1. 扩展中国剩余定理：核心算法
 * 2. 枚举：用于处理多个余数集合
 * 3. 优先队列：可选的优化方法
 * 
 * 实际应用：
 * 1. 密码学中的约束满足问题
 * 2. 调度问题
 * 
 * 相关题目：
 * 1. Codeforces 707D Two chandeliers
 *    链接：https://codeforces.com/contest/1483/problem/D
 *    题目大意：有两个循环亮灯的序列，每天亮一种颜色的灯，老板会在两个灯颜色相同时生气，求第k次生气在第几天
 * 
 * 2. HDU 3579 Hello Kiki
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=3579
 *    题目大意：求解同余方程组，模数不一定互质
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.*;

public class UVa11754_CodeFeat_Java {

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
        
        while (true) {
            in.nextToken();
            int C = (int) in.nval;
            in.nextToken();
            int S = (int) in.nval;
            
            if (C == 0 && S == 0) {
                break;
            }
            
            long[] mods = new long[C];
            List<Set<Long>> remainders = new ArrayList<>();
            
            for (int i = 0; i < C; i++) {
                in.nextToken();
                mods[i] = (long) in.nval;
                in.nextToken();
                int k = (int) in.nval;
                
                Set<Long> remainderSet = new TreeSet<>();
                for (int j = 0; j < k; j++) {
                    in.nextToken();
                    remainderSet.add((long) in.nval);
                }
                remainders.add(remainderSet);
            }
            
            // 枚举所有可能的组合
            List<Long> solutions = new ArrayList<>();
            
            // 生成所有组合的笛卡尔积
            List<List<Long>> combinations = new ArrayList<>();
            generateCombinations(remainders, 0, new ArrayList<>(), combinations);
            
            // 对每个组合使用扩展中国剩余定理
            for (List<Long> combination : combinations) {
                long[] r = new long[C];
                for (int i = 0; i < C; i++) {
                    r[i] = combination.get(i);
                }
                
                long solution = excrt(mods, r);
                if (solution != -1) {
                    // 生成所有解：solution + k * lcm(mods)
                    long period = 1;
                    for (long mod : mods) {
                        period = lcm(period, mod);
                    }
                    
                    long current = solution;
                    while (current > 0 && solutions.size() < S * 10) { // 多生成一些解用于排序
                        solutions.add(current);
                        current += period;
                    }
                }
            }
            
            // 排序并取前S个解
            Collections.sort(solutions);
            for (int i = 0; i < Math.min(S, solutions.size()); i++) {
                out.println(solutions.get(i));
            }
            out.println();
        }
        
        out.flush();
        out.close();
        br.close();
    }
    
    // 生成所有组合的笛卡尔积
    private static void generateCombinations(List<Set<Long>> remainders, int index, 
                                           List<Long> current, List<List<Long>> result) {
        if (index == remainders.size()) {
            result.add(new ArrayList<>(current));
            return;
        }
        
        for (long remainder : remainders.get(index)) {
            current.add(remainder);
            generateCombinations(remainders, index + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}