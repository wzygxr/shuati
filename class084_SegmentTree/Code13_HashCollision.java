package class111;

// 哈希冲突问题 - 分块算法实现 (Java版本)
// 题目来源: https://www.luogu.com.cn/problem/P3396
// 题目大意: 给定一个长度为n的数组arr，支持两种操作：
// 1. 查询操作 A x y: 查询所有满足 i % x == y 的位置i对应的arr[i]之和
// 2. 更新操作 C x y: 将arr[x]的值更新为y
// 约束条件: 1 <= n、m <= 1.5 * 10^5
// 
// 解题思路:
// 1. 对于x <= sqrt(n)的情况，预处理dp[x][y]的值
// 2. 对于x > sqrt(n)的情况，直接暴力计算
// 3. 更新操作时，同时更新预处理结果
// 
// 时间复杂度分析:
// - 预处理: O(n * sqrt(n))
// - 查询: O(1) 对于x <= sqrt(n)，O(n/x) 对于x > sqrt(n)
// - 更新: O(sqrt(n))
// 
// 空间复杂度: O(n + sqrt(n)^2) = O(n)
// 
// 工程化考量:
// 1. 异常处理: 验证输入参数的有效性
// 2. 性能优化: 使用分块思想平衡预处理和查询的开销
// 3. 边界处理: 处理x=0或y>=x等边界情况
// 4. 内存管理: 合理设置数组大小避免内存溢出

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code13_HashCollision {

    // 定义最大数组长度和块大小
    public static int MAXN = 150001;
    public static int MAXB = 401;
    
    // n: 数组长度, m: 操作次数, blen: 块大小
    public static int n, m, blen;
    
    // arr: 原始数组
    public static int[] arr = new int[MAXN];
    
    // dp[x][y]: 存储所有满足 i % x == y 的位置i对应的arr[i]之和 (预处理结果)
    // 只对 x <= sqrt(n) 的情况进行预处理，以节省空间和时间
    public static long[][] dp = new long[MAXB][MAXB];

    /**
     * 查询操作 A x y
     * 查询所有满足 i % x == y 的位置i对应的arr[i]之和
     * @param x 除数，必须大于0
     * @param y 余数，必须满足 0 <= y < x
     * @return 满足条件的位置对应的元素之和
     * @throws IllegalArgumentException 如果x <= 0 或 y < 0 或 y >= x
     */
    public static long query(int x, int y) {
        // 参数验证
        if (x <= 0) {
            throw new IllegalArgumentException("除数x必须大于0");
        }
        if (y < 0 || y >= x) {
            throw new IllegalArgumentException("余数y必须满足0 <= y < x");
        }
        
        // 如果x小于等于块大小，则直接返回预处理结果
        if (x <= blen) {
            return dp[x][y];
        }
        
        // 否则暴力计算（适用于x较大的情况）
        long ans = 0;
        for (int i = y; i <= n; i += x) {
            ans += arr[i];
        }
        return ans;
    }

    /**
     * 更新操作 C x y
     * 将arr[x]的值更新为y，并更新相关的预处理结果
     * @param i 要更新的位置，必须满足1 <= i <= n
     * @param v 新的值
     * @throws IllegalArgumentException 如果位置i超出有效范围
     */
    public static void update(int i, int v) {
        // 参数验证
        if (i < 1 || i > n) {
            throw new IllegalArgumentException("位置i必须在1到n之间");
        }
        
        // 计算值的变化量
        int delta = v - arr[i];
        // 更新原数组
        arr[i] = v;
        
        // 更新所有相关的预处理结果
        // 只需要更新x <= sqrt(n)的情况，因为这些被预处理了
        for (int x = 1; x <= blen; x++) {
            dp[x][i % x] += delta;
        }
    }

    /**
     * 预处理函数
     * 对于所有x <= sqrt(n)的情况，预处理dp[x][y]的值
     */
    public static void prepare() {
        // 计算块大小，通常选择sqrt(n)
        blen = (int) Math.sqrt(n);
        
        // 初始化dp数组为0（Java中默认初始化为0）
        // 对于每个x <= sqrt(n)，计算所有y对应的dp[x][y]值
        for (int x = 1; x <= blen; x++) {
            for (int i = 1; i <= n; i++) {
                // i % x 表示位置i对x取余的结果
                // dp[x][i % x] 累加arr[i]的值
                dp[x][i % x] += arr[i];
            }
        }
    }

    /**
     * 单元测试方法
     * 验证算法的正确性
     */
    public static void test() {
        // 测试用例1: 小规模数据测试
        n = 10;
        for (int i = 1; i <= n; i++) {
            arr[i] = i;
        }
        prepare();
        
        // 验证查询结果
        assert query(3, 0) == (3 + 6 + 9) : "查询测试失败";
        assert query(3, 1) == (1 + 4 + 7 + 10) : "查询测试失败";
        assert query(3, 2) == (2 + 5 + 8) : "查询测试失败";
        
        // 验证更新结果
        update(5, 50);
        assert query(3, 1) == (1 + 4 + 7 + 10) + (50 - 5) : "更新测试失败";
        
        System.out.println("所有测试用例通过！");
    }

    public static void main(String[] args) throws IOException {
        // 如果传入参数包含"test"，则运行单元测试
        if (args.length > 0 && "test".equals(args[0])) {
            test();
            return;
        }
        
        FastReader in = new FastReader();
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
        char op;
        int x, y;
        for (int i = 1; i <= m; i++) {
            op = in.nextChar();
            x = in.nextInt();
            y = in.nextInt();
            
            try {
                if (op == 'A') {
                    // 查询操作
                    out.println(query(x, y));
                } else {
                    // 更新操作
                    update(x, y);
                }
            } catch (IllegalArgumentException e) {
                out.println("错误: " + e.getMessage());
            }
        }
        
        out.flush();
        out.close();
    }

    // 高效读取工具类，用于加快输入输出速度
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

        public char nextChar() throws IOException {
            byte c;
            do {
                c = readByte();
                if (c == -1)
                    return 0;
            } while (c <= ' ');
            char ans = 0;
            while (c > ' ') {
                ans = (char) c;
                c = readByte();
            }
            return ans;
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

        private boolean isWhitespace(byte b) {
            return b == ' ' || b == '\n' || b == '\r' || b == '\t';
        }
    }

}