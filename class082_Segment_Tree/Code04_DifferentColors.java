package class109;

// HH的项链
// 一共有n个位置，每个位置颜色给定，i位置的颜色是arr[i]
// 一共有m个查询，question[i] = {li, ri}
// 表示第i条查询想查arr[li..ri]范围上一共有多少种不同颜色
// 返回每条查询的答案
// 1 <= n、m、arr[i] <= 10^6
// 1 <= li <= ri <= n
// 测试链接 : https://www.luogu.com.cn/problem/P1972
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例
// 代码逻辑和课上讲的完全一致，但是重写了读写工具类，增加了io效率

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;

/**
 * 使用树状数组解决HH的项链问题
 * 
 * 解题思路：
 * 1. 离线处理：将所有查询按照右端点排序
 * 2. 从左到右遍历数组，维护每个颜色最后出现的位置
 * 3. 使用树状数组维护区间和：
 *    - 当遇到一个颜色时，如果之前出现过，则在之前出现的位置-1
 *    - 在当前位置+1
 * 4. 对于每个查询，答案就是区间[li, ri]的和
 * 
 * 时间复杂度分析：
 * - 排序查询：O(m log m)
 * - 遍历数组，每次操作树状数组：O(n log n)
 * - 处理查询：O(m log n)
 * - 总时间复杂度：O((n + m) * log n)
 * 
 * 空间复杂度分析：
 * - 需要额外数组存储原始数据、查询数据、树状数组等：O(n + m)
 * - 所以总空间复杂度为O(n + m)
 */
public class Code04_DifferentColors {

    // 最大数组长度
    public static int MAXN = 1000001;

    // 原数组，arr[i]表示位置i的颜色
    public static int[] arr = new int[MAXN];

    // 查询数组，query[i][0]表示左端点，query[i][1]表示右端点，query[i][2]表示查询编号
    public static int[][] query = new int[MAXN][3];

    // 答案数组
    public static int[] ans = new int[MAXN];

    // 颜色映射数组，map[color]表示颜色color最后出现的位置
    public static int[] map = new int[MAXN];

    // 树状数组，用于维护区间和
    public static int[] tree = new int[MAXN];

    // 数组长度和查询数量
    public static int n, m;

    /**
     * lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
     * 例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
     * 
     * @param i 输入数字
     * @return 最低位的1所代表的数值
     */
    public static int lowbit(int i) {
        return i & -i;
    }

    /**
     * 单点增加操作：在位置i上增加v
     * 
     * @param i 位置（从1开始）
     * @param v 增加的值
     */
    public static void add(int i, int v) {
        while (i <= n) {
            tree[i] += v;
            i += lowbit(i);
        }
    }

    /**
     * 查询前缀和：计算从位置1到位置i的所有元素之和
     * 
     * @param i 查询的结束位置
     * @return 前缀和
     */
    public static int sum(int i) {
        int ans = 0;
        while (i > 0) {
            ans += tree[i];
            i -= lowbit(i);
        }
        return ans;
    }

    /**
     * 查询区间和：计算从位置l到位置r的所有元素之和
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 区间和
     */
    public static int range(int l, int r) {
        return sum(r) - sum(l - 1);
    }

    /**
     * 计算所有查询的答案
     */
    public static void compute() {
        // 按照查询的右端点排序
        Arrays.sort(query, 1, m + 1, (a, b) -> a[1] - b[1]);
        
        // s表示当前处理到的数组位置，q表示当前处理到的查询编号
        for (int s = 1, q = 1, l, r, i; q <= m; q++) {
            // 当前查询的右端点
            r = query[q][1];
            
            // 处理从s到r位置的元素
            for (; s <= r; s++) {
                // 当前位置的颜色
                int color = arr[s];
                
                // 如果该颜色之前出现过，则在之前出现的位置-1
                if (map[color] != 0) {
                    add(map[color], -1);
                }
                
                // 在当前位置+1
                add(s, 1);
                
                // 更新该颜色最后出现的位置
                map[color] = s;
            }
            
            // 当前查询的左端点
            l = query[q][0];
            // 当前查询的编号
            i = query[q][2];
            // 计算区间[l, r]的不同颜色数
            ans[i] = range(l, r);
        }
    }

    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        n = in.nextInt();
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        m = in.nextInt();
        for (int i = 1; i <= m; i++) {
            query[i][0] = in.nextInt();
            query[i][1] = in.nextInt();
            query[i][2] = i;
        }
        compute();
        for (int i = 1; i <= m; i++) {
            out.write(ans[i] + "\n");
        }
        out.flush();
        out.close();
    }

    // 读写工具类
    static class FastReader {
        final private int BUFFER_SIZE = 1 << 16;
        private final InputStream in;
        private final byte[] buffer;
        private int ptr, len;

        public FastReader() {
            in = System.in;
            buffer = new byte[BUFFER_SIZE];
            ptr = len = 0;
        }

        private boolean hasNextByte() throws IOException {
            if (ptr < len)
                return true;
            ptr = 0;
            len = in.read(buffer);
            return len > 0;
        }

        private byte readByte() throws IOException {
            if (!hasNextByte())
                return -1;
            return buffer[ptr++];
        }

        public boolean hasNext() throws IOException {
            while (hasNextByte()) {
                byte b = buffer[ptr];
                if (!isWhitespace(b))
                    return true;
                ptr++;
            }
            return false;
        }

        public String next() throws IOException {
            byte c;
            do {
                c = readByte();
                if (c == -1)
                    return null;
            } while (c <= ' ');
            StringBuilder sb = new StringBuilder();
            while (c > ' ') {
                sb.append((char) c);
                c = readByte();
            }
            return sb.toString();
        }

        public int nextInt() throws IOException {
            int num = 0;
            byte b = readByte();
            while (isWhitespace(b))
                b = readByte();
            boolean minus = false;
            if (b == '-') {
                minus = true;
                b = readByte();
            }
            while (!isWhitespace(b) && b != -1) {
                num = num * 10 + (b - '0');
                b = readByte();
            }
            return minus ? -num : num;
        }

        public double nextDouble() throws IOException {
            double num = 0, div = 1;
            byte b = readByte();
            while (isWhitespace(b))
                b = readByte();
            boolean minus = false;
            if (b == '-') {
                minus = true;
                b = readByte();
            }
            while (!isWhitespace(b) && b != '.' && b != -1) {
                num = num * 10 + (b - '0');
                b = readByte();
            }
            if (b == '.') {
                b = readByte();
                while (!isWhitespace(b) && b != -1) {
                    num += (b - '0') / (div *= 10);
                    b = readByte();
                }
            }
            return minus ? -num : num;
        }

        private boolean isWhitespace(byte b) {
            return b == ' ' || b == '\n' || b == '\r' || b == '\t';
        }
    }

}