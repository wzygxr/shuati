package class175;

// 最少划分问题 - 分块算法优化 (Java版本)
// 题目来源: https://www.luogu.com.cn/problem/CF786C
// 题目来源: https://codeforces.com/problemset/problem/786/C
// 题目大意: 给定一个长度为n的数组arr，考虑如下问题的解
// 数组arr划分成若干段子数组，保证每段不同数字的种类 <= k，返回至少划分成几段
// 打印k = 1, 2, 3..n时，所有的答案
// 约束条件: 1 <= arr[i] <= n <= 10^5

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.IOException;

public class Code06_TillCollapse1 {

    // 定义最大数组长度
    public static int MAXN = 100001;
    
    // n: 数组长度, blen: 块大小
    public static int n, blen;
    
    // arr: 原始数组
    public static int[] arr = new int[MAXN];
    
    // vis: 记录数字是否出现过
    public static boolean[] vis = new boolean[MAXN];
    
    // ans: 存储每个k对应的答案
    public static int[] ans = new int[MAXN];

    /**
     * 查询当限制为limit时，最少需要划分成几段
     * @param limit 每段不同数字种类的上限
     * @return 最少段数
     */
    public static int query(int limit) {
        int kind = 0, cnt = 0, start = 1;
        
        // 遍历数组
        for (int i = 1; i <= n; i++) {
            // 如果当前数字没有出现过
            if (!vis[arr[i]]) {
                kind++; // 不同数字种类数+1
                
                // 如果超过了限制
                if (kind > limit) {
                    cnt++; // 段数+1
                    
                    // 清除之前段的标记
                    for (int j = start; j < i; j++) {
                        vis[arr[j]] = false;
                    }
                    
                    // 更新新段的起始位置
                    start = i;
                    kind = 1; // 重置种类数为1
                }
                
                // 标记当前数字已出现
                vis[arr[i]] = true;
            }
        }
        
        // 处理最后一段
        if (kind > 0) {
            cnt++;
            // 清除最后一段的标记
            for (int j = start; j <= n; j++) {
                vis[arr[j]] = false;
            }
        }
        return cnt;
    }

    /**
     * 跳跃函数，用于优化计算
     * @param l 左边界
     * @param r 右边界
     * @param curAns 当前答案
     * @return 下一个需要计算的位置
     */
    public static int jump(int l, int r, int curAns) {
        int find = l;
        while (l <= r) {
            int mid = (l + r) >> 1;
            int check = query(mid);
            
            if (check < curAns) {
                r = mid - 1;
            } else if (check > curAns) {
                l = mid + 1;
            } else {
                find = mid;
                l = mid + 1;
            }
        }
        return find + 1;
    }

    /**
     * 计算所有答案
     */
    public static void compute() {
        // 对于k <= sqrt(n)的情况，直接计算
        for (int i = 1; i <= blen; i++) {
            ans[i] = query(i);
        }
        
        // 对于k > sqrt(n)的情况，使用跳跃优化
        for (int i = blen + 1; i <= n; i = jump(i, n, ans[i])) {
            ans[i] = query(i);
        }
    }

    /**
     * 预处理函数
     */
    public static void prepare() {
        // 计算块大小，选择sqrt(n * log2(n))以优化性能
        int log2n = 0;
        while ((1 << log2n) <= (n >> 1)) {
            log2n++;
        }
        blen = Math.max(1, (int) Math.sqrt(n * log2n));
        
        // 初始化答案数组为-1，表示未计算
        Arrays.fill(ans, 1, n + 1, -1);
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(System.out);
        
        // 读取数组长度n
        n = in.nextInt();
        
        // 读取初始数组
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        
        // 进行预处理
        prepare();
        
        // 计算所有答案
        compute();
        
        // 输出所有答案
        for (int i = 1; i <= n; i++) {
            // 如果答案未计算，则继承前一个答案
            if (ans[i] == -1) {
                ans[i] = ans[i - 1];
            }
            out.print(ans[i] + " ");
        }
        out.println();
        
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