package class175;

// Points on Plane问题 - 二维前缀和实现 (Java版本)
// 题目来源: https://codeforces.com/problemset/problem/1181/C
// 题目大意: 给定一个N×N的网格，每个格点上有一些点，多次查询矩形区域内点的数量
// 约束条件: 1 <= N <= 10^3, 1 <= Q <= 10^5

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code13_PointsOnPlane1 {

    // 定义最大网格大小
    public static int MAXN = 1001;
    
    // n: 网格大小, q: 查询次数
    public static int n, q;
    
    // arr: 原始网格数据
    public static int[][] arr = new int[MAXN][MAXN];
    
    // sum: 二维前缀和数组
    public static int[][] sum = new int[MAXN][MAXN];

    /**
     * 构建二维前缀和数组
     * 时间复杂度: O(N^2)
     * 设计思路: 利用二维前缀和公式计算每个位置的前缀和
     * sum[i][j] = arr[i][j] + sum[i-1][j] + sum[i][j-1] - sum[i-1][j-1]
     * 这样可以避免重复计算，提高查询效率
     */
    public static void build() {
        // 遍历网格的每个位置
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                // 应用二维前缀和公式
                sum[i][j] = arr[i][j] + sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1];
            }
        }
    }

    /**
     * 查询矩形区域[r1,c1]到[r2,c2]内点的数量
     * 时间复杂度: O(1)
     * 设计思路: 利用二维前缀和数组快速计算矩形区域和
     * 通过容斥原理计算矩形区域和：
     * 区域和 = sum[r2][c2] - sum[r1-1][c2] - sum[r2][c1-1] + sum[r1-1][c1-1]
     * @param r1 起始行
     * @param c1 起始列
     * @param r2 结束行
     * @param c2 结束列
     * @return 矩形区域内点的数量
     */
    public static int query(int r1, int c1, int r2, int c2) {
        // 应用容斥原理计算矩形区域和
        return sum[r2][c2] - sum[r1 - 1][c2] - sum[r2][c1 - 1] + sum[r1 - 1][c1 - 1];
    }

    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取网格大小n和查询次数q
        n = in.nextInt();
        q = in.nextInt();
        
        // 读取网格数据
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                arr[i][j] = in.nextInt();
            }
        }

        // 构建前缀和数组
        build();

        // 处理q次查询
        for (int i = 1; i <= q; i++) {
            int r1 = in.nextInt();
            int c1 = in.nextInt();
            int r2 = in.nextInt();
            int c2 = in.nextInt();
            // 输出查询结果
            out.println(query(r1, c1, r2, c2));
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

        public int nextInt() throws IOException {
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