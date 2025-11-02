package class177;

// HH的项链
// 给定一个长度为n的正整数序列，m次询问，每次询问一个区间内不同数字的种类数
// 1 <= n,m,ai <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P1972

// 解题思路：
// 这是莫队算法的经典应用题
// 我们需要维护区间内不同数字的种类数
// 关键在于如何处理数字的添加和删除操作
// 对于每个数字，我们只关心它是否在当前窗口中出现过
// 可以使用计数数组记录每个数字的出现次数，用一个变量记录不同数字的种类数

// 时间复杂度分析：
// 1. 预处理排序：O(m * log m)
// 2. 莫队算法处理：O((n + m) * sqrt(n))
// 3. 总时间复杂度：O(m * log m + (n + m) * sqrt(n))
// 空间复杂度分析：
// 1. 存储原数组：O(n)
// 2. 存储查询：O(m)
// 3. 计数数组：O(max(ai)) = O(10^6)
// 4. 总空间复杂度：O(n + m + 10^6)

// 是否最优解：
// 这是该问题的最优解之一，莫队算法在处理这类离线区间查询问题时具有很好的时间复杂度
// 对于在线查询问题，可以使用主席树等数据结构，但对于离线问题，莫队算法是首选

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code15_HHNecklace1 {

    public static int MAXN = 1000010;
    
    public static int n, m;
    public static int[] arr = new int[MAXN];
    public static int[][] queries = new int[MAXN][3];
    
    // 分块相关
    public static int blockSize;
    public static int[] belong = new int[MAXN];
    
    // 计数数组，记录每个数字在当前窗口中的出现次数
    public static int[] count = new int[MAXN];
    // 当前窗口中不同数字的种类数
    public static int distinctCount = 0;
    public static int[] answers = new int[MAXN];
    
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
        // 如果该数字之前没有出现过，现在出现了，种类数增加
        if (count[val] == 0) {
            distinctCount++;
        }
        count[val]++;
    }
    
    // 从窗口删除元素
    public static void remove(int pos) {
        int val = arr[pos];
        // 如果该数字之前只出现了一次，现在删除后就没有了，种类数减少
        if (count[val] == 1) {
            distinctCount--;
        }
        count[val]--;
    }
    
    // 主计算函数
    public static void compute() {
        // 初始化计数数组
        Arrays.fill(count, 0);
        distinctCount = 0;
        
        int l = 1, r = 0;
        
        for (int i = 1; i <= m; i++) {
            int ql = queries[i][0];
            int qr = queries[i][1];
            int id = queries[i][2];
            
            // 调整窗口边界
            while (r < qr) add(++r);
            while (r > qr) remove(r--);
            while (l < ql) remove(l++);
            while (l > ql) add(--l);
            
            answers[id] = distinctCount;
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
        Arrays.sort(queries, 1, m + 1, new QueryComparator());
    }
    
    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = in.nextInt();
        
        // 读取数组
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        
        m = in.nextInt();
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            queries[i][0] = in.nextInt();
            queries[i][1] = in.nextInt();
            queries[i][2] = i;
        }
        
        prepare();
        compute();
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
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