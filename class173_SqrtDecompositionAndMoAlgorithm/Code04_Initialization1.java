package class175;

// 初始化问题 - 分块算法实现 (Java版本)
// 题目来源: https://www.luogu.com.cn/problem/P5309
// 题目大意: 给定一个长度为n的数组arr，支持两种操作：
// 操作 1 x y z : 从arr[y]开始，下标每次+x，所有相应位置的数都+z，题目保证 y <= x
// 操作 2 x y   : 打印arr[x..y]的累加和，答案对1000000007取余
// 约束条件: 1 <= n、m <= 2 * 10^5

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code04_Initialization1 {

    // 定义最大数组长度和块大小
    public static int MAXN = 200001;
    public static int MAXB = 501;
    public static int MOD = 1000000007;
    
    // n: 数组长度, m: 操作次数
    public static int n, m;

    // pre[x][y]: 对于步长为x，起始位置为y的序列，前缀和的增量
    public static long[][] pre = new long[MAXB][MAXB];
    
    // suf[x][y]: 对于步长为x，起始位置为y的序列，后缀和的增量
    public static long[][] suf = new long[MAXB][MAXB];
    
    // arr: 原始数组
    public static long[] arr = new long[MAXN];
    
    // sum: 每个块的和
    public static long[] sum = new long[MAXB];

    // blen: 块大小, bnum: 块数量
    public static int blen, bnum;
    
    // bi[i]: 位置i属于哪个块
    public static int[] bi = new int[MAXN];
    
    // bl[b]: 块b的左边界
    public static int[] bl = new int[MAXB];
    
    // br[b]: 块b的右边界
    public static int[] br = new int[MAXB];

    /**
     * 操作 1 x y z
     * 从arr[y]开始，下标每次+x，所有相应位置的数都+z
     * @param x 步长
     * @param y 起始位置
     * @param z 增量
     */
    public static void add(int x, int y, int z) {
        // 如果步长x小于等于块大小，则更新pre和suf数组
        if (x <= blen) {
            // 更新前缀和增量
            for (int i = y; i <= x; i++) {
                pre[x][i] += z;
            }
            // 更新后缀和增量
            for (int i = y; i >= 1; i--) {
                suf[x][i] += z;
            }
        } else {
            // 否则直接更新原数组和块和
            for (int i = y; i <= n; i += x) {
                arr[i] += z;
                sum[bi[i]] += z;
            }
        }
    }

    /**
     * 查询区间和
     * @param l 左边界
     * @param r 右边界
     * @return 区间和
     */
    public static long querySum(int l, int r) {
        // 获取左右边界所在的块
        int lb = bi[l], rb = bi[r];
        long ans = 0;
        
        // 如果左右边界在同一个块内
        if (lb == rb) {
            // 直接遍历计算
            for (int i = l; i <= r; i++) {
                ans += arr[i];
            }
        } else {
            // 否则分三部分计算
            // 1. 左边不完整块
            for (int i = l; i <= br[lb]; i++) {
                ans += arr[i];
            }
            // 2. 右边不完整块
            for (int i = bl[rb]; i <= r; i++) {
                ans += arr[i];
            }
            // 3. 中间完整块
            for (int b = lb + 1; b <= rb - 1; b++) {
                ans += sum[b];
            }
        }
        return ans;
    }

    /**
     * 操作 2 x y
     * 查询arr[x..y]的累加和
     * @param l 左边界
     * @param r 右边界
     * @return 区间和对MOD取余
     */
    public static long query(int l, int r) {
        long ans = querySum(l, r);
        
        // 对于所有步长x <= sqrt(n)，累加其对区间和的贡献
        for (int x = 1, lth, rth, num; x <= blen; x++) {
            // 计算起始位置和结束位置在步长为x时对应的编号
            lth = (l - 1) / x + 1;
            rth = (r - 1) / x + 1;
            
            // 计算中间完整段的数量
            num = rth - lth - 1;
            
            // 如果起始和结束位置在同一段
            if (lth == rth) {
                // 只需要计算起始段的贡献
                ans = ans + pre[x][(r - 1) % x + 1] - pre[x][(l - 1) % x];
            } else {
                // 否则需要计算三部分的贡献
                // 1. 起始段的后缀贡献
                // 2. 中间完整段的贡献
                // 3. 结束段的前缀贡献
                ans = ans + suf[x][(l - 1) % x + 1] + pre[x][x] * num + pre[x][(r - 1) % x + 1];
            }
        }
        return ans % MOD;
    }

    /**
     * 预处理函数
     * 初始化分块信息
     */
    public static void prepare() {
        // 计算块大小，通常选择sqrt(n)
        blen = (int) Math.sqrt(n);
        
        // 计算块数量
        bnum = (n + blen - 1) / blen;
        
        // 计算每个位置属于哪个块
        for (int i = 1; i <= n; i++) {
            bi[i] = (i - 1) / blen + 1;
        }
        
        // 计算每个块的边界
        for (int b = 1; b <= bnum; b++) {
            // 块的左边界
            bl[b] = (b - 1) * blen + 1;
            // 块的右边界
            br[b] = Math.min(b * blen, n);
            
            // 计算块的初始和
            for (int i = bl[b]; i <= br[b]; i++) {
                sum[b] += arr[i];
            }
        }
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度n和操作次数m
        n = in.nextInt();
        m = in.nextInt();
        
        // 读取初始数组
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        
        // 进行预处理
        prepare();
        
        // 处理m次操作
        for (int i = 1, op, x, y, z; i <= m; i++) {
            op = in.nextInt();
            x = in.nextInt();
            y = in.nextInt();
            if (op == 1) {
                z = in.nextInt();
                add(x, y, z);
            } else {
                out.println(query(x, y));
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