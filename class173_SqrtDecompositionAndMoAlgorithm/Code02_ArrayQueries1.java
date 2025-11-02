package class175;

// 数组查询问题 - 分块算法实现 (Java版本)
// 题目来源: https://www.luogu.com.cn/problem/CF797E
// 题目来源: https://codeforces.com/problemset/problem/797/E
// 题目大意: 给定一个长度为n的数组arr，支持查询操作：
// 查询 p k : 从位置p开始，每次跳跃arr[p] + k步，直到越界，返回跳跃次数
// 约束条件: 
// 1 <= n、q <= 10^5
// 1 <= arr[i] <= n

// 相关解答:
// C++版本: class175/Code02_ArrayQueries.cpp
// Java版本: class175/Code02_ArrayQueries1.java
// Python版本: class175/Code02_ArrayQueries.py

// 分块算法分析:
// - 时间复杂度：预处理O(n*sqrt(n)) + 查询O(q*sqrt(n))
// - 空间复杂度：O(n*sqrt(n))
// - 分块思想：将k分为k<=sqrt(n)和k>sqrt(n)两种情况处理
//   - 当k<=sqrt(n)时：预处理所有位置p和k的结果，查询时间O(1)
//   - 当k>sqrt(n)时：直接暴力计算，由于k>sqrt(n)，每次跳跃步长至少sqrt(n)+1
//     因此最多只会跳sqrt(n)次，时间复杂度O(sqrt(n))

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code02_ArrayQueries1 {

    // 定义最大数组长度和块大小
    public static int MAXN = 100001;
    public static int MAXB = 401;
    
    // n: 数组长度, q: 查询次数, blen: 块大小
    public static int n, q, blen;
    
    // arr: 原始数组
    public static int[] arr = new int[MAXN];
    
    // dp[p][k]: 从位置p开始，每次跳跃arr[p] + k步，直到越界需要的步数（预处理结果）
    // 只对 k <= sqrt(n) 的情况进行预处理
    public static int[][] dp = new int[MAXN][MAXB];

    /**
     * 查询操作
     * 从位置p开始，每次跳跃arr[p] + k步，直到越界，返回跳跃次数
     * 
     * 算法策略：
     * - 当k <= sqrt(n)时：使用预处理的结果，时间复杂度O(1)
     * - 当k > sqrt(n)时：暴力计算，由于每次跳跃步长至少为sqrt(n)+1，
     *   最多只会跳sqrt(n)次，因此时间复杂度为O(sqrt(n))
     * 
     * @param p 起始位置（从1开始索引）
     * @param k 跳跃增量
     * @return 跳跃次数
     */
    public static int query(int p, int k) {
        // 当k较小时(k <= sqrt(n))，直接使用预处理结果，O(1)时间
        if (k <= blen) {
            return dp[p][k];
        }
        
        // 当k较大时(k > sqrt(n))，暴力计算
        // 由于每次跳跃步长至少为sqrt(n)+1，最多只会执行sqrt(n)次跳跃
        // 因此时间复杂度为O(sqrt(n))
        int ans = 0;
        while (p <= n) {  // 当位置p未越界时继续跳跃
            ans++;         // 跳跃次数加1
            p += arr[p] + k;  // 计算下一个位置：当前位置 + 当前位置的值 + k
        }
        return ans;
    }

    /**
     * 预处理函数
     * 对于所有k <= sqrt(n)的情况，预处理dp[p][k]的值
     * 
     * 预处理策略：
     * - 计算块大小blen = sqrt(n)
     * - 从后往前遍历所有位置p (从n到1)
     * - 对每个位置p，计算所有k <= blen对应的dp[p][k]
     * - 使用动态规划的思路：dp[p][k] = 1 + (如果下一步越界则0，否则dp[p + arr[p] + k][k])
     * 
     * 实现细节：
     * - 从后往前计算是关键，这样可以确保在计算dp[p][k]时，dp[p + arr[p] + k][k]已经计算过了
     * - dp[p][k]表示从位置p开始，每次跳跃增量k时的总跳跃次数
     * - 时间复杂度：O(n*sqrt(n))，虽然预处理时间较高，但可以确保后续查询操作的高效性
     */
    public static void prepare() {
        // 计算块大小，通常选择sqrt(n)作为分块的阈值
        blen = (int) Math.sqrt(n);
        
        // 从后往前计算dp值 - 这是动态规划的关键部分
        // 从数组末尾开始，向数组头部遍历
        // 这样设计可以确保在计算dp[p][k]时，dp[p + arr[p] + k][k]已经被计算过了
        for (int p = n; p >= 1; p--) {
            // 对每个k <= sqrt(n)的情况进行预处理
            for (int k = 1; k <= blen; k++) {
                // 计算从位置p跳一步后的新位置
                int next_pos = p + arr[p] + k;
                
                // 动态规划转移方程：
                // - 如果下一步越界(n+1或更大)，则dp[p][k] = 1（只跳转一次）
                // - 否则dp[p][k] = 1 + dp[next_pos][k]（一次跳转加上从next_pos开始的跳转次数）
                dp[p][k] = 1 + (next_pos > n ? 0 : dp[next_pos][k]);
            }
        }
    }

    /**
     * 主函数
     * 程序执行流程：
     * 1. 初始化高效输入输出工具类
     * 2. 读取数组长度n
     * 3. 读取初始数组arr的值
     * 4. 调用prepare()函数进行预处理
     * 5. 读取查询次数q
     * 6. 处理每个查询，输出结果
     * 
     * 注意事项：
     * - 数组索引从1开始，而不是0
     * - 使用FastReader类提高输入速度
     * - 每个查询的结果立即输出
     * 
     * @param args 命令行参数
     * @throws Exception 输入输出异常
     */
    public static void main(String[] args) throws Exception {
        // 初始化高效输入输出工具类，用于处理大规模数据
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度n
        n = in.nextInt();
        
        // 读取初始数组，注意数组索引从1开始
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        
        // 进行预处理，为所有k <= sqrt(n)的情况预计算dp数组
        prepare();
        
        // 读取查询次数q
        q = in.nextInt();
        
        // 处理q次查询
        // 对于每个查询，读取起始位置p和跳跃增量k，然后调用query函数计算结果并输出
        for (int i = 1, p, k; i <= q; i++) {
            p = in.nextInt();
            k = in.nextInt();
            out.println(query(p, k));
        }
        
        // 刷新输出缓冲区并关闭输出流
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