package class175;

// Powerful array问题 - Mo算法实现 (Java版本)
// 题目来源: https://codeforces.com/problemset/problem/86/D
// 题目大意: 给定一个长度为n的数组arr，有q次查询，每次查询[l,r]区间的加权和
// 权重计算: 对于区间[l,r]内的每个数字x，如果它出现了c[x]次，则贡献为c[x]^2*x
// 约束条件: 1 <= n, q <= 2*10^5, 1 <= arr[i] <= 10^6
// 解法: Mo算法（离线分块）
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n + V), 其中V为值域大小(10^6)
// 是否最优解: 是，Mo算法是解决此类区间查询问题的经典方法

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code11_PowerfulArray1 {

    // 定义最大数组长度和值域大小
    public static int MAXN = 200001;
    public static int MAXV = 1000001;
    
    // n: 数组长度, q: 查询次数, blen: 块大小
    public static int n, q, blen;
    
    // arr: 原始数组
    public static int[] arr = new int[MAXN];
    
    // ans: 存储每个查询的答案
    public static long[] ans = new long[MAXN];
    
    // count: 计数数组，记录当前窗口中每个数字的出现次数
    public static int[] count = new int[MAXV];
    
    // curAns: 当前窗口的加权和
    public static long curAns = 0;

    // 查询结构
    static class Query {
        int l, r, id;

        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }

    // queries: 存储所有查询
    public static Query[] queries = new Query[MAXN];

    /**
     * 添加元素到当前窗口
     * 时间复杂度: O(1)
     * 设计思路: 当向窗口中添加一个元素时，需要更新该元素的计数和总贡献值
     * 由于贡献计算公式为c[x]^2*x，添加元素时需要先减去旧贡献，更新计数，再加新贡献
     * @param pos 位置
     */
    public static void add(int pos) {
        // 移除旧贡献: count[arr[pos]]^2 * arr[pos]
        curAns -= (long) count[arr[pos]] * count[arr[pos]] * arr[pos];
        count[arr[pos]]++;
        // 添加新贡献: count[arr[pos]]^2 * arr[pos]
        curAns += (long) count[arr[pos]] * count[arr[pos]] * arr[pos];
    }

    /**
     * 从当前窗口移除元素
     * 时间复杂度: O(1)
     * 设计思路: 当从窗口中移除一个元素时，需要更新该元素的计数和总贡献值
     * 由于贡献计算公式为c[x]^2*x，移除元素时需要先减去旧贡献，更新计数，再加新贡献
     * @param pos 位置
     */
    public static void remove(int pos) {
        // 移除旧贡献: count[arr[pos]]^2 * arr[pos]
        curAns -= (long) count[arr[pos]] * count[arr[pos]] * arr[pos];
        count[arr[pos]]--;
        // 添加新贡献: count[arr[pos]]^2 * arr[pos]
        curAns += (long) count[arr[pos]] * count[arr[pos]] * arr[pos];
    }

    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度n和查询次数q
        n = in.nextInt();
        q = in.nextInt();
        
        // 读取初始数组
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }

        // 读取所有查询
        for (int i = 1; i <= q; i++) {
            int l = in.nextInt();
            int r = in.nextInt();
            queries[i] = new Query(l, r, i);
        }

        // 使用Mo算法进行排序
        // 块大小选择: sqrt(n)，这是经过理论分析得出的最优块大小
        // 时间复杂度: O(q * log(q))，主要是排序的复杂度
        // 排序策略: 按照左端点所在的块编号排序，块内按右端点排序
        // 这样可以最小化指针移动的次数，从而优化整体时间复杂度
        blen = (int) Math.sqrt(n);
        
        Arrays.sort(queries, 1, q + 1, (a, b) -> {
            int blockA = (a.l - 1) / blen;
            int blockB = (b.l - 1) / blen;
            if (blockA != blockB) {
                return blockA - blockB;
            }
            return a.r - b.r;
        });

        // Mo算法处理
        int curL = 1, curR = 0;
        for (int i = 1; i <= q; i++) {
            int l = queries[i].l;
            int r = queries[i].r;
            int id = queries[i].id;

            // 扩展右边界
            while (curR < r) {
                curR++;
                add(curR);
            }

            // 收缩左边界
            while (curL > l) {
                curL--;
                add(curL);
            }

            // 收缩右边界
            while (curR > r) {
                remove(curR);
                curR--;
            }

            // 扩展左边界
            while (curL < l) {
                remove(curL);
                curL++;
            }

            // 记录当前查询的答案
            ans[id] = curAns;
        }

        // 输出所有查询的结果
        for (int i = 1; i <= q; i++) {
            out.println(ans[i]);
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