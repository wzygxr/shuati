package class175;

// Holes问题 - 分块算法实现 (Java版本)
// 题目来源: https://codeforces.com/problemset/problem/13/E
// 题目大意: 给定一个长度为n的数组arr，支持两种操作：
// 1. 查询从位置i开始跳出数组需要的步数
// 2. 修改某个位置的值
// 约束条件: 1 <= n, q <= 10^5, 1 <= arr[i] <= n

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code12_Holes1 {

    // 定义最大数组长度
    public static int MAXN = 100001;
    
    // n: 数组长度, q: 操作次数, blen: 块大小
    public static int n, q, blen;
    
    // arr: 原始数组
    public static int[] arr = new int[MAXN];
    
    // 分块相关数组
    public static int[] belong = new int[MAXN]; // 每个位置属于哪个块
    public static int[] blockL = new int[MAXN]; // 每个块的左边界
    public static int[] blockR = new int[MAXN]; // 每个块的右边界
    public static int[] to = new int[MAXN]; // 从块内某个位置跳出该块到达的位置
    public static int[] step = new int[MAXN]; // 从块内某个位置跳出该块需要的步数
    public static int bcnt = 0; // 块的数量

    /**
     * 构建分块结构
     * 时间复杂度: O(n)
     * 设计思路: 将数组分成大小约为sqrt(n)的块，预处理每个块内位置的跳跃信息
     * 块大小选择sqrt(n)是为了平衡预处理和查询的复杂度
     */
    public static void build() {
        // 计算块大小，选择sqrt(n)
        blen = (int) Math.sqrt(n);
        
        // 计算块数量
        bcnt = (n - 1) / blen + 1;
        
        // 初始化每个块的边界信息
        for (int i = 1; i <= bcnt; i++) {
            blockL[i] = (i - 1) * blen + 1;
            blockR[i] = Math.min(i * blen, n);
        }
        
        // 计算每个位置属于哪个块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blen + 1;
        }
        
        // 预处理每个块内的信息
        reset();
    }
    
    /**
     * 重置块内信息
     * 时间复杂度: O(n*sqrt(n))
     * 设计思路: 预处理每个位置的跳跃信息，包括跳出当前块需要的步数和到达的位置
     * 对于每个位置i，模拟跳跃过程直到跳出当前块或数组边界
     */
    public static void reset() {
        // 遍历所有位置
        for (int i = 1; i <= n; i++) {
            int b = belong[i];
            
            // 计算从位置i跳出块b需要的步数和到达的位置
            if (i + arr[i] > blockR[b]) {
                // 可以跳出当前块
                to[i] = i + arr[i];
                step[i] = 1;
            } else {
                // 不能跳出当前块，需要继续跳
                to[i] = i + arr[i];
                step[i] = 1;
                
                // 继续模拟直到跳出块
                int pos = i + arr[i];
                while (pos <= n && belong[pos] == b) {
                    pos += arr[pos];
                    step[i]++;
                }
                to[i] = pos;
            }
        }
    }

    /**
     * 查询从位置x跳出数组需要的步数和最终位置
     * 时间复杂度: O(sqrt(n))
     * 设计思路: 利用预处理的块内跳跃信息，每次跳跃可以跳过整个块
     * 最多需要跳O(sqrt(n))次，因为数组被分成了O(sqrt(n))个块
     * @param x 起始位置
     * @return 包含步数和最终位置的数组
     */
    public static int[] query(int x) {
        int steps = 0;
        int pos = x;
        
        // 利用预处理信息进行跳跃
        while (pos <= n) {
            steps += step[pos];
            pos = to[pos];
        }
        
        // 返回步数和最终位置
        return new int[]{steps, pos > n ? pos - 1 : pos};
    }

    /**
     * 修改位置x的值为y
     * 时间复杂度: O(sqrt(n))
     * 设计思路: 当修改某个位置的值时，需要重新计算包含该位置的整个块的跳跃信息
     * 由于块大小为O(sqrt(n))，所以更新复杂度为O(sqrt(n))
     * @param x 位置
     * @param y 新值
     */
    public static void update(int x, int y) {
        // 更新数组值
        arr[x] = y;
        
        // 重新计算包含位置x的整个块的信息
        int b = belong[x];
        for (int i = blockL[b]; i <= blockR[b]; i++) {
            if (i + arr[i] > blockR[b]) {
                // 可以跳出当前块
                to[i] = i + arr[i];
                step[i] = 1;
            } else {
                // 不能跳出当前块，需要继续跳
                to[i] = i + arr[i];
                step[i] = 1;
                
                // 继续模拟直到跳出块
                int pos = i + arr[i];
                while (pos <= n && belong[pos] == b) {
                    pos += arr[pos];
                    step[i]++;
                }
                to[i] = pos;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度n和操作次数q
        n = in.nextInt();
        q = in.nextInt();
        
        // 读取初始数组
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }

        // 构建分块结构
        build();

        // 处理q次操作
        for (int i = 1; i <= q; i++) {
            int op = in.nextInt();
            if (op == 0) {
                // 修改操作
                int x = in.nextInt();
                int y = in.nextInt();
                update(x, y);
            } else {
                // 查询操作
                int x = in.nextInt();
                int[] result = query(x);
                out.println(result[0] + " " + result[1]);
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