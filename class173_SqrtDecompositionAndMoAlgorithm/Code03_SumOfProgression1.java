package class175;

// 等差数列求和问题 - 分块算法实现 (Java版本)
// 题目来源: https://www.luogu.com.cn/problem/CF1921F
// 题目来源: https://codeforces.com/problemset/problem/1921/F
// 题目大意: 给定一个长度为n的数组arr，支持查询操作：
// 查询 s d k : arr[s]作为第1项、arr[s + 1*d]作为第2项、arr[s + 2*d]作为第3项...
//             每项的值 * 项的编号，一共k项都累加起来，打印累加和
// 约束条件: 
// 1 <= n <= 10^5
// 1 <= q <= 2 * 10^5

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code03_SumOfProgression1 {

    // 定义最大数组长度和块大小
    public static int MAXN = 100001;
    public static int MAXB = 401;
    
    // t: 测试用例数, n: 数组长度, q: 查询次数, blen: 块大小
    public static int t, n, q, blen;
    
    // arr: 原始数组
    public static int[] arr = new int[MAXN];
    
    // f[d][i]: 从位置i开始，公差为d的等差数列的前缀和
    // f[d][i] = arr[i] + arr[i+d] + arr[i+2*d] + ...
    public static long[][] f = new long[MAXB][MAXN];
    
    // g[d][i]: 从位置i开始，公差为d的等差数列的加权前缀和
    // g[d][i] = 1*arr[i] + 2*arr[i+d] + 3*arr[i+2*d] + ...
    public static long[][] g = new long[MAXB][MAXN];

    /**
     * 查询操作
     * arr[s]作为第1项、arr[s + 1*d]作为第2项、arr[s + 2*d]作为第3项...
     * 每项的值 * 项的编号，一共k项都累加起来，返回累加和
     * @param s 起始位置
     * @param d 公差
     * @param k 项数
     * @return 加权和
     */
    public static long query(int s, int d, int k) {
        long ans = 0;
        
        // 如果d小于等于块大小，则使用预处理的结果
        if (d <= blen) {
            // g[d][s] 是从位置s开始的加权前缀和
            ans = g[d][s];
            
            // 如果s + d * k没有超出数组范围，则需要减去后面的部分
            if (s + d * k <= n) {
                // 减去从s + d * k开始的部分
                ans = ans - g[d][s + d * k] - f[d][s + d * k] * k;
            }
        } else {
            // 否则暴力计算（适用于d较大的情况）
            for (int i = 1; i <= k; i++) {
                ans += 1L * arr[s + (i - 1) * d] * i;
            }
        }
        return ans;
    }

    /**
     * 预处理函数
     * 预处理f和g数组
     */
    public static void prepare() {
        // 计算块大小，通常选择sqrt(n)
        blen = (int) Math.sqrt(n);
        
        // 预处理f数组
        // 对于每个公差d <= sqrt(n)
        for (int d = 1; d <= blen; d++) {
            // 从后往前计算前缀和
            for (int i = n; i >= 1; i--) {
                // f[d][i] = arr[i] + f[d][i+d]
                f[d][i] = arr[i] + (i + d > n ? 0 : f[d][i + d]);
            }
        }
        
        // 预处理g数组
        // 对于每个公差d <= sqrt(n)
        for (int d = 1; d <= blen; d++) {
            // 从后往前计算加权前缀和
            for (int i = n; i >= 1; i--) {
                // g[d][i] = arr[i] + g[d][i+d] + f[d][i+d]
                g[d][i] = f[d][i] + (i + d > n ? 0 : g[d][i + d]);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取测试用例数t
        t = in.nextInt();
        
        // 处理每个测试用例
        for (int c = 1; c <= t; c++) {
            // 读取数组长度n和查询次数q
            n = in.nextInt();
            q = in.nextInt();
            
            // 读取初始数组
            for (int i = 1; i <= n; i++) {
                arr[i] = in.nextInt();
            }
            
            // 进行预处理
            prepare();
            
            // 处理q次查询
            for (int i = 1, s, d, k; i <= q; i++) {
                s = in.nextInt();
                d = in.nextInt();
                k = in.nextInt();
                out.println(query(s, d, k));
            }
        }
        
        out.flush();
        out.close();
    }

    // 高效读取工具类，用于加快输入输出速度
    static class FastReader {
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;
        private final InputStream in;

        FastReader(InputStream in) {
            this.in = in;
        }

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = readByte();
            }
            int val = 0;
            while (c > ' ' && c != -1) {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return neg ? -val : val;
        }
    }

}