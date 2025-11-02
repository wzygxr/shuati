package class177;

// 数列找不同
// 现有数列A1,A2,…,AN，Q个询问(Li,Ri)，询问ALi,ALi+1,…,ARi是否互不相同
// 1 <= N,Q <= 10^5
// 1 <= Ai <= N
// 1 <= Li <= Ri <= N
// 测试链接 : https://www.luogu.com.cn/problem/P3901

// 解题思路：
// 这是一个典型的莫队算法应用题
// 我们需要判断区间内是否有重复元素
// 可以维护一个计数器，记录当前窗口内重复元素的个数
// 如果重复元素个数为0，则说明区间内所有元素互不相同，输出"Yes"
// 否则输出"No"

// 时间复杂度分析：
// 1. 预处理排序：O(Q * log Q)
// 2. 莫队算法处理：O((N + Q) * sqrt(N))
// 3. 总时间复杂度：O(Q * log Q + (N + Q) * sqrt(N))
// 空间复杂度分析：
// 1. 存储原数组：O(N)
// 2. 存储查询：O(Q)
// 3. 计数数组：O(N)
// 4. 总空间复杂度：O(N + Q)

// 是否最优解：
// 这是该问题的最优解之一，莫队算法在处理这类离线区间查询问题时具有很好的时间复杂度
// 对于在线查询问题，可以使用主席树等数据结构，但对于离线问题，莫队算法是首选

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code14_FindDifferent1 {

    public static int MAXN = 100010;
    
    public static int n, q;
    public static int[] arr = new int[MAXN];
    public static int[][] queries = new int[MAXN][3];
    
    // 分块相关
    public static int blockSize;
    public static int[] belong = new int[MAXN];
    
    // 计数数组，记录每个数字在当前窗口中的出现次数
    public static int[] count = new int[MAXN];
    // 重复元素个数，记录当前窗口内有多少种数字出现了多次
    public static int duplicateCount = 0;
    public static String[] answers = new String[MAXN];
    
    // 普通莫队排序规则
    public static class QueryComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] a, int[] b) {
            // 按照左端点所在块排序
            if (belong[a[0]] != belong[b[0]]) {
                return belong[a[0]] - belong[b[0]];
            }
            // 同一块内按照右端点排序
            return a[1] - b[1];
        }
    }
    
    // 添加元素到窗口
    public static void add(int pos) {
        int val = arr[pos];
        // 如果该数字之前已经出现过一次，现在变成重复了
        if (count[val] == 1) {
            duplicateCount++;
        }
        count[val]++;
    }
    
    // 从窗口删除元素
    public static void remove(int pos) {
        int val = arr[pos];
        // 如果该数字之前出现了多次，现在变成只出现一次了
        if (count[val] == 2) {
            duplicateCount--;
        }
        count[val]--;
    }
    
    // 主计算函数
    public static void compute() {
        // 初始化计数数组
        Arrays.fill(count, 0);
        duplicateCount = 0;
        
        int l = 1, r = 0;
        
        for (int i = 1; i <= q; i++) {
            int ql = queries[i][0];
            int qr = queries[i][1];
            int id = queries[i][2];
            
            // 调整窗口边界
            while (r < qr) add(++r);
            while (r > qr) remove(r--);
            while (l < ql) remove(l++);
            while (l > ql) add(--l);
            
            // 如果没有重复元素，则区间内所有元素互不相同
            answers[id] = (duplicateCount == 0) ? "Yes" : "No";
        }
    }
    
    // 预处理函数
    public static void prepare() {
        // 计算分块大小
        blockSize = (int) Math.sqrt(n);
        
        // 计算每个位置所属的块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 对查询进行排序
        Arrays.sort(queries, 1, q + 1, new QueryComparator());
    }
    
    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = in.nextInt();
        q = in.nextInt();
        
        // 读取数组
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        
        // 读取查询
        for (int i = 1; i <= q; i++) {
            queries[i][0] = in.nextInt();
            queries[i][1] = in.nextInt();
            queries[i][2] = i;
        }
        
        prepare();
        compute();
        
        // 输出结果
        for (int i = 1; i <= q; i++) {
            out.println(answers[i]);
        }
        
        out.flush();
        out.close();
    }
    
    // 读写工具类
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