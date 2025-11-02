package class141;

/*
 * NOI 2018 屠龙勇士
 * 链接：https://www.luogu.com.cn/problem/P4774
 * 
 * 题目大意：
 * 勇士需要击败n条龙，每条龙有血量hp[i]和恢复能力recovery[i]。
 * 初始有m把剑，每把剑有攻击力init[i]。
 * 攻击龙的策略：选择攻击力不超过龙血量的最大剑，如果没有则选攻击力最小的剑。
 * 击杀龙的条件：攻击若干次后龙血量≤0，然后在恢复过程中血量恰好为0时击杀。
 * 目标：求最小攻击次数ans，使得所有龙都能被击杀。
 * 
 * 解题思路：
 * 1. 首先根据规则确定每条龙使用的剑的攻击力attack[i]
 * 2. 对于第i条龙，需要满足：attack[i] * ans >= hp[i] + k * recovery[i]，其中k为非负整数
 * 3. 移项得：attack[i] * ans ≡ hp[i] (mod recovery[i])
 * 4. 这样就转化为了一个线性同余方程组，可以用扩展中国剩余定理求解
 * 5. 特殊处理：需要确保每条龙都被砍到血量≤0，即ans >= max{ceil(hp[i]/attack[i])}
 * 
 * 算法原理：
 * 这是一道综合性的题目，结合了贪心算法和扩展中国剩余定理。
 * 通过贪心策略选择剑，然后将问题转化为线性同余方程组求解。
 * 
 * 算法分析：
 * 时间复杂度：O(n log max(recovery[i]))
 * 空间复杂度：O(n)
 * 
 * 关键点：
 * 1. 使用TreeMap维护剑的有序性，便于查找合适的剑
 * 2. 转化为线性同余方程组后使用扩展中国剩余定理求解
 * 3. 特殊处理：确保解满足实际意义（攻击次数足够砍死每条龙）
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入数据的合法性
 * 2. 边界处理：处理无解的情况
 * 3. 大数运算：使用long long类型防止溢出
 * 4. 数据结构选择：使用TreeMap维护剑的有序集合
 * 
 * 与其他算法的关联：
 * 1. 扩展中国剩余定理：核心算法
 * 2. 贪心算法：选择剑的过程
 * 3. 扩展欧几里得算法：EXCRT的子过程
 * 4. 数据结构：TreeMap的使用
 * 
 * 实际应用：
 * 1. 资源调度问题
 * 2. 周期性任务的协调
 * 3. 游戏设计中的数值计算
 * 
 * 解题技巧总结：
 * 1. 问题转化：将实际问题转化为数学模型（线性同余方程组）
 * 2. 特殊约束处理：考虑实际意义对数学解的约束
 * 3. 数据结构选择：根据操作需求选择合适的数据结构
 * 4. 精度控制：使用龟速乘防止大数运算溢出
 * 
 * 相关题目：
 * 1. 洛谷 P4774 - 屠龙勇士
 *    链接：https://www.luogu.com.cn/problem/P4774
 *    题目大意：与本题相同
 * 
 * 2. Codeforces 707D Two chandeliers
 *    链接：https://codeforces.com/contest/1483/problem/D
 *    题目大意：有两个循环亮灯的序列，每天亮一种颜色的灯，老板会在两个灯颜色相同时生气，求第k次生气在第几天
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.TreeMap;

public class NOI2018_DragonSlayer_Java {

    public static int MAXN = 100001;

    public static long[] hp = new long[MAXN];

    public static long[] recovery = new long[MAXN];

    public static long[] reward = new long[MAXN];

    public static long[] init = new long[MAXN];

    public static long[] attack = new long[MAXN];

    public static TreeMap<Long, Integer> sorted = new TreeMap<>();

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

    // 每只怪物根据血量找到攻击的剑
    // 哪只龙需要砍最多次，才能让其血量<=0，返回最多的次数
    public static long allocate(int n, int m) {
        sorted.clear();
        for (int i = 1; i <= m; i++) {
            sorted.put(init[i], sorted.getOrDefault(init[i], 0) + 1);
        }
        long max = 0;
        for (int i = 1; i <= n; i++) {
            Long sword = sorted.floorKey(hp[i]);
            if (sword == null) {
                sword = sorted.firstKey();
            }
            attack[i] = sword;
            sorted.put(sword, sorted.get(sword) - 1);
            if (sorted.get(sword) == 0) {
                sorted.remove(sword);
            }
            sorted.put(reward[i], sorted.getOrDefault(reward[i], 0) + 1);
            max = Math.max(max, (hp[i] + attack[i] - 1) / attack[i]);
            // 血量 = 血量 % 恢复力，变成余数形式
            hp[i] %= recovery[i];
        }
        return max;
    }

    // bi * ans ≡ ri(% mi)方程组求解 + 本题对解的特殊处理
    public static long compute(int n, int m) {
        // max变量很关键，最后的逻辑需要用到
        // 哪只龙需要砍最多次，才能让其血量<=0，这个最多的次数就是max
        long max = allocate(n, m);
        long tail = 0, lcm = 1, tmp, a, b, c, x0;
        // ans = lcm * x + tail
        for (int i = 1; i <= n; i++) {
            // ai * ans = ai * lcm * x + tail * ai  1号方程
            // ai * ans = ri * y + hi               2号方程
            // ai * lcm * x + ri * y = hi - tail * ai
            a = multiply(attack[i], lcm, recovery[i]);
            b = recovery[i];
            c = ((hp[i] - attack[i] * tail) % b + b) % b;
            exgcd(a, b);
            if (c % d != 0) {
                return -1;
            }
            x0 = multiply(x, c / d, b / d);
            tmp = lcm * (b / d);
            tail = (tail + multiply(x0, lcm, tmp)) % tmp;
            lcm = tmp;
        }
        // 通解 ans = ? * lcm + tail
        // 下面属于本题的特殊处理，注意max变量的含义
        // 上面的大思路是，对每只怪兽，根据如下的公式，整理出同余式
        // ans * a[i] = h[i] + 每只怪兽若干恢复次数 * r[i]
        // 同余式为，ans * a[i] ≡ h[i] (% r[i])
        // 注意！能建立起的同余式，需要默认"每只怪兽若干恢复次数"的范围是整数
        // 最终解出，ans = k * lcm + tail，tail是最小正数解
        // 但实际情况是，"每只怪兽若干恢复次数"毫无疑问是非负的，并不是整个整数域
        // 也就是说，需要确保把每只怪兽的血量砍到<=0，然后才能保证，每只怪兽若干恢复次数>=0
        // 也就是说，ans = k * lcm + tail，需要确保，ans >= max，注意max变量的含义！
        // 如果tail >= max，那么答案就是tail，此时k==0
        // 如果tail < max，想保证ans >= max，其实就是tail + k * lcm >= max
        // k = (max - tail) / lcm，向上取整，也就是k = (max - tail + lcm - 1) / lcm
        // 所以，ans = (max - tail + lcm - 1) / lcm * lcm + tail
        long ans;
        if (tail >= max) {
            ans = tail;
        } else {
            ans = (max - tail + lcm - 1) / lcm * lcm + tail;
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        int cases = (int) in.nval;
        for (int t = 1; t <= cases; t++) {
            in.nextToken();
            int n = (int) in.nval;
            in.nextToken();
            int m = (int) in.nval;
            for (int i = 1; i <= n; i++) {
                in.nextToken();
                hp[i] = (long) in.nval;
            }
            for (int i = 1; i <= n; i++) {
                in.nextToken();
                recovery[i] = (long) in.nval;
            }
            for (int i = 1; i <= n; i++) {
                in.nextToken();
                reward[i] = (long) in.nval;
            }
            for (int i = 1; i <= m; i++) {
                in.nextToken();
                init[i] = (long) in.nval;
            }
            out.println(compute(n, m));
        }
        
        out.flush();
        out.close();
        br.close();
    }
}