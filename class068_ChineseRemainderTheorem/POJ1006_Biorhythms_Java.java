package class141;

/*
 * POJ 1006 Biorhythms
 * 链接：http://poj.org/problem?id=1006
 * 题目大意：人的体力、情感和智力周期分别为23天、28天和33天，已知某一天三个指标的数值，
 *           求下一次三个指标同时达到峰值的天数
 * 
 * 算法思路：
 * 这是一个标准的中国剩余定理应用题。三个生理周期分别为23、28、33天，它们两两互质，
 * 可以直接应用中国剩余定理。
 * 
 * 设从出生开始的第x天，三个指标分别为p、e、i，要求从第d天开始，下一个三个指标同时达到峰值的天数。
 * 体力周期：x ≡ p (mod 23)
 * 情感周期：x ≡ e (mod 28)
 * 智力周期：x ≡ i (mod 33)
 * 
 * 解法步骤：
 * 1. 使用中国剩余定理求解同余方程组
 * 2. 计算结果与d的差值，确保是下一个峰值
 * 
 * 算法原理：
 * 这是一个经典的中国剩余定理应用题，展示了CRT在实际问题中的应用。
 * 由于23、28、33两两互质，可以直接使用CRT求解。
 * 
 * 时间复杂度：O(1)，因为模数固定
 * 空间复杂度：O(1)
 * 
 * 适用场景：
 * 1. 生物节律计算
 * 2. 周期性事件预测
 * 3. 调度问题
 * 
 * 注意事项：
 * 1. 23、28、33两两互质，可以直接使用CRT
 * 2. 需要处理特殊情况，如当天就是峰值
 * 3. 注意结果必须大于d
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入是否合法
 * 2. 异常处理：处理无解的情况
 * 3. 边界处理：处理d=0等特殊情况
 * 
 * 与其他算法的关联：
 * 1. 中国剩余定理：核心算法
 * 2. 最小公倍数：用于验证周期
 * 
 * 实际应用：
 * 1. 生物节律分析
 * 2. 周期性事件预测
 * 3. 资源调度
 * 
 * 相关题目：
 * 1. UVA 756 Biorhythms
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=697
 *    题目大意：与POJ 1006相同
 * 
 * 2. 51Nod 1079 - 中国剩余定理
 *    链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=1079
 *    题目大意：给定一些质数p和对应余数m，求满足所有条件的最小正整数K
 * 
 * 3. 洛谷 P1495 - 曹冲养猪
 *    链接：https://www.luogu.com.cn/problem/P1495
 *    题目大意：求解同余方程组 x ≡ ai (mod mi)，其中mi两两互质
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class POJ1006_Biorhythms_Java {

    // 生理周期
    static final int PHYSICAL_CYCLE = 23;
    static final int EMOTIONAL_CYCLE = 28;
    static final int INTELLECTUAL_CYCLE = 33;
    
    // 周期的最小公倍数
    static final int LCM = 21252; // lcm(23, 28, 33) = 21252

    // 扩展欧几里得算法
    public static long exgcd(long a, long b, long[] result) {
        if (b == 0) {
            result[0] = a;
            result[1] = 1;
            result[2] = 0;
            return a;
        } else {
            long d = exgcd(b, a % b, result);
            long px = result[1], py = result[2];
            result[1] = py;
            result[2] = px - py * (a / b);
            return d;
        }
    }

    // 求逆元
    public static long modInverse(long a, long m) {
        long[] result = new long[3];
        long d = exgcd(a, m, result);
        if (d != 1) {
            return -1; // 逆元不存在
        } else {
            return (result[1] % m + m) % m;
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

    // 中国剩余定理求解
    public static long crt(long p, long e, long i) {
        // M = 23 * 28 * 33 = 21252
        long M = LCM;
        
        // M1 = M / 23 = 924
        long M1 = M / PHYSICAL_CYCLE;
        // M2 = M / 28 = 759
        long M2 = M / EMOTIONAL_CYCLE;
        // M3 = M / 33 = 644
        long M3 = M / INTELLECTUAL_CYCLE;
        
        // 求逆元
        long inv_M1 = modInverse(M1, PHYSICAL_CYCLE);
        long inv_M2 = modInverse(M2, EMOTIONAL_CYCLE);
        long inv_M3 = modInverse(M3, INTELLECTUAL_CYCLE);
        
        // 计算解
        long x = (multiply(p, multiply(M1, inv_M1, M), M) +
                  multiply(e, multiply(M2, inv_M2, M), M) +
                  multiply(i, multiply(M3, inv_M3, M), M)) % M;
        
        return x;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int caseNum = 1;
        while (true) {
            in.nextToken();
            long p = (long) in.nval;
            in.nextToken();
            long e = (long) in.nval;
            in.nextToken();
            long i = (long) in.nval;
            in.nextToken();
            long d = (long) in.nval;
            
            // 结束条件
            if (p == -1 && e == -1 && i == -1 && d == -1) {
                break;
            }
            
            // 使用中国剩余定理求解
            long x = crt(p, e, i);
            
            // 计算下一个峰值日期
            long days = (x - d + LCM) % LCM;
            if (days == 0) {
                days = LCM;
            }
            
            out.println("Case " + caseNum + ": the next triple peak occurs in " + days + " days.");
            caseNum++;
        }
        
        out.flush();
        out.close();
        br.close();
    }
}