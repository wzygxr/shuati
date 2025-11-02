package class175;

// 哈希冲突问题 - 分块算法实现 (Java版本)
// 题目来源: https://www.luogu.com.cn/problem/P3396
// 题目大意: 给定一个长度为n的数组arr，支持两种操作：
// 1. 查询操作 A x y: 查询所有满足 i % x == y 的位置i对应的arr[i]之和
// 2. 更新操作 C x y: 将arr[x]的值更新为y
// 约束条件: 1 <= n、m <= 1.5 * 10^5
// 相关解答: 
// - C++版本: Code01_HashCollision.cpp
// - Java版本: Code01_HashCollision1.java, Code01_HashCollision2.java
// - Python版本: Code01_HashCollision.py

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code01_HashCollision1 {

    // 定义常量
    public static int MAXN = 150001;    // 最大数组长度
    public static int MAXB = 401;       // 最大块大小，约等于sqrt(MAXN)
    
    // 全局变量
    public static int n, m;             // n: 数组长度, m: 操作次数
    public static int blen;             // 块大小，通常取sqrt(n)
    public static int[] arr = new int[MAXN];  // 原始数组
    
    // dp[x][y]: 存储所有满足 i % x == y 的位置i对应的arr[i]之和 (预处理结果)
    // 只对 x <= sqrt(n) 的情况进行预处理，以节省空间和时间
    // 这样设计可以使查询操作的时间复杂度为O(1)当x较小时，而当x较大时为O(sqrt(n))
    public static long[][] dp = new long[MAXB][MAXB];

    /**
     * 查询操作 A x y
     * 查询所有满足 i % x == y 的位置i对应的arr[i]之和
     * 
     * @param x 除数
     * @param y 余数
     * @return 满足条件的位置对应的元素之和
     * 
     * 算法策略:
     * - 对于x <= sqrt(n)的情况: O(1)时间复杂度，直接返回预处理好的dp[x][y]
     * - 对于x > sqrt(n)的情况: O(n/x)时间复杂度，由于x较大，最多执行sqrt(n)次循环
     * 这种分块处理的策略使得查询操作的时间复杂度为O(sqrt(n))
     */
    public static long query(int x, int y) {
        // 当x较小时(x <= blen)，直接使用预处理结果，O(1)时间
        if (x <= blen) {
            return dp[x][y];
        }
        
        // 当x较大时(x > blen)，暴力枚举所有满足条件的位置
        // 由于x > sqrt(n)，所以最多执行n/x < sqrt(n)次循环，总时间复杂度为O(sqrt(n))
        long ans = 0;
        for (int i = y; i <= n; i += x) {
            ans += arr[i];
        }
        return ans;
    }

    /**
     * 更新操作 C x y
     * 将arr[x]的值更新为y，并更新相关的预处理结果
     * 
     * @param i 要更新的位置
     * @param v 新的值
     * 
     * 算法策略:
     * - 计算值的变化量delta
     * - 更新原始数组arr[i]
     * - 更新所有受影响的预处理结果
     * - 时间复杂度: O(sqrt(n))，因为只需要更新x <= sqrt(n)的预处理结果
     * 这种设计确保了更新操作在可接受的时间复杂度内完成
     */
    public static void update(int i, int v) {
        // 计算值的变化量
        int delta = v - arr[i];
        
        // 更新原始数组
        arr[i] = v;
        
        // 更新所有相关的预处理结果
        // 只需要更新x <= sqrt(n)的情况，因为这些情况被预处理了
        // 对于每个x <= blen，位置i对x的余数是i % x，所以需要更新dp[x][i % x]
        for (int x = 1; x <= blen; x++) {
            dp[x][i % x] += delta;
        }
    }

    /**
     * 预处理函数
     * 对于所有x <= sqrt(n)的情况，预处理dp[x][y]的值
     * 
     * 预处理策略:
     * - 计算块大小blen = sqrt(n)
     * - 对每个x (1 <= x <= blen)，计算所有可能的余数y (0 <= y < x)对应的arr[i]之和
     * - 时间复杂度: O(n*sqrt(n))
     * 虽然预处理的时间复杂度较高，但后续的查询和更新操作都可以在O(sqrt(n))时间内完成
     */
    public static void prepare() {
        // 计算块大小，通常选择sqrt(n)
        blen = (int) Math.sqrt(n);
        
        // 对每个x <= sqrt(n)，预处理dp[x][y]的值
        // dp[x][y]存储了所有满足i % x == y的arr[i]之和
        for (int x = 1; x <= blen; x++) {
            for (int i = 1; i <= n; i++) {
                // 计算位置i对x取余的结果y
                int y = i % x;
                // 将arr[i]累加到dp[x][y]中
                dp[x][y] += arr[i];
            }
        }
    }

    /**
     * 主函数
     * 读取输入数据，初始化数组，预处理数据，处理所有操作
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 创建FastReader实例用于高效读取输入
        FastReader in = new FastReader();
        // 创建PrintWriter实例用于高效输出结果
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度n和操作次数m
        n = in.nextInt();
        m = in.nextInt();
        
        // 读取初始数组元素，注意这里数组索引从1开始
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        
        // 进行预处理，计算所有x <= sqrt(n)情况下的dp[x][y]值
        prepare();
        
        // 处理m次操作
        char op;       // 操作类型：'A'表示查询，'C'表示更新
        int x, y;      // 操作参数
        for (int i = 1; i <= m; i++) {
            op = in.nextChar();  // 读取操作类型
            x = in.nextInt();   // 读取第一个参数
            y = in.nextInt();   // 读取第二个参数
            
            if (op == 'A') {
                // 查询操作：计算所有满足i % x == y的位置i对应的arr[i]之和
                out.println(query(x, y));
            } else {
                // 更新操作：将arr[x]的值更新为y，并更新相关的预处理结果
                update(x, y);
            }
        }
        
        // 确保所有输出被刷新到控制台
        out.flush();
        // 关闭输出流
        out.close();
    }

    /**
     * 高效读取工具类
     * 用于加速输入读取，比标准Scanner类速度更快，适用于大数据量输入的场景
     * 采用缓冲区读取策略，减少I/O操作次数
     */
    static class FastReader {
        /**
         * 缓冲区大小，设为2^16以平衡内存使用和读取效率
         */
        final private int BUFFER_SIZE = 1 << 16;
        /**
         * 输入流对象，默认使用System.in
         */
        private final InputStream in;
        /**
         * 字节缓冲区，用于存储批量读取的数据
         */
        private final byte[] buffer;
        /**
         * ptr: 当前读取位置指针
         * len: 当前缓冲区中有效数据的长度
         */
        private int ptr, len;

        /**
         * 构造函数，初始化输入流和缓冲区
         */
        public FastReader() {
            in = System.in;
            buffer = new byte[BUFFER_SIZE];
            ptr = len = 0;  // 初始化指针位置为0
        }

        /**
         * 检查缓冲区是否还有可读字节
         * 如果缓冲区已读完，尝试从输入流读取新数据到缓冲区
         * 
         * @return 如果有可读字节则返回true，否则返回false
         * @throws IOException 输入异常
         */
        private boolean hasNextByte() throws IOException {
            if (ptr < len)  // 缓冲区还有数据
                return true;
            ptr = 0;  // 重置指针到缓冲区开头
            len = in.read(buffer);  // 从输入流读取数据填充缓冲区
            return len > 0;  // 如果读取到数据则返回true
        }

        /**
         * 读取单个字节
         * 
         * @return 读取的字节值，如果到达流末尾返回-1
         * @throws IOException 输入异常
         */
        private byte readByte() throws IOException {
            if (!hasNextByte())
                return -1;
            return buffer[ptr++];  // 返回当前字节并移动指针
        }

        /**
         * 读取单个字符，跳过空白字符
         * 
         * @return 读取的非空白字符
         * @throws IOException 输入异常
         */
        public char nextChar() throws IOException {
            byte c;
            // 跳过所有空白字符
            do {
                c = readByte();
                if (c == -1)
                    return 0;
            } while (c <= ' ');
            char ans = 0;
            // 读取非空白字符
            while (c > ' ') {
                ans = (char) c;
                c = readByte();
            }
            return ans;
        }

        /**
         * 读取整数，支持正负数
         * 
         * @return 读取的整数值
         * @throws IOException 输入异常
         */
        public int nextInt() throws IOException {
            int num = 0;
            byte b = readByte();
            // 跳过前导空白字符
            while (isWhitespace(b))
                b = readByte();
            
            // 处理负号
            boolean minus = false;
            if (b == '-') {
                minus = true;
                b = readByte();
            }
            
            // 读取数字部分
            while (!isWhitespace(b) && b != -1) {
                num = num * 10 + (b - '0');  // 累积数字
                b = readByte();
            }
            
            return minus ? -num : num;  // 根据是否有负号返回结果
        }

        /**
         * 判断一个字节是否为空白字符
         * 空白字符包括空格、换行符、回车符和制表符
         * 
         * @param b 要判断的字节
         * @return 如果是空白字符则返回true，否则返回false
         */
        private boolean isWhitespace(byte b) {
            return b == ' ' || b == '\n' || b == '\r' || b == '\t';
        }
    }

}