package class177;

// 小B的询问 /【模板】莫队
// 给定一个长为n的整数序列a，值域为[1,k]
// m个询问，每个询问给定一个区间[l,r]，求∑(i=1 to k) ci^2
// 其中ci表示数字i在[l,r]中的出现次数
// 1 <= n,m,k <= 5*10^4
// 测试链接 : https://www.luogu.com.cn/problem/P2709

// 解题思路：
// 这是普通莫队的经典模板题
// 关键在于如何维护区间内每种数字出现次数的平方和
// 当添加一个数字时：如果该数字原来出现了x次，现在出现了x+1次
// 那么答案的变化为：(x+1)^2 - x^2 = 2*x + 1
// 当删除一个数字时：如果该数字原来出现了x次，现在出现了x-1次
// 那么答案的变化为：(x-1)^2 - x^2 = -2*x + 1

// 时间复杂度分析：
// 1. 预处理排序：O(m * log m)
// 2. 莫队算法处理：O((n + m) * sqrt(n))
// 3. 总时间复杂度：O(m * log m + (n + m) * sqrt(n))
// 空间复杂度分析：
// 1. 存储原数组：O(n)
// 2. 存储查询：O(m)
// 3. 计数数组：O(k)
// 4. 总空间复杂度：O(n + m + k)

// 是否最优解：
// 这是该问题的最优解之一，莫队算法在处理这类离线区间查询问题时具有很好的时间复杂度
// 对于在线查询问题，可以使用主席树等数据结构，但对于离线问题，莫队算法是首选

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code13_LittleBQueries1 {

    public static int MAXN = 50010;
    public static int MAXK = 50010;
    
    public static int n, m, k;
    public static int[] arr = new int[MAXN];
    public static int[][] queries = new int[MAXN][3];
    
    // 分块相关
    public static int blockSize;
    public static int[] belong = new int[MAXN];
    
    // 计数数组，记录每个数字在当前窗口中的出现次数
    public static int[] count = new int[MAXK];
    // 当前答案，即∑ci^2
    public static long currentAnswer = 0;
    public static long[] answers = new long[MAXN];
    
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
        // 原来出现了count[val]次，现在出现count[val]+1次
        // 答案变化：(count[val]+1)^2 - count[val]^2 = 2*count[val] + 1
        currentAnswer += 2 * count[val] + 1;
        count[val]++;
    }
    
    // 从窗口删除元素
    public static void remove(int pos) {
        int val = arr[pos];
        // 原来出现了count[val]次，现在出现count[val]-1次
        // 答案变化：(count[val]-1)^2 - count[val]^2 = -2*count[val] + 1
        currentAnswer -= 2 * count[val] - 1;
        count[val]--;
    }
    
    // 主计算函数
    public static void compute() {
        // 初始化计数数组
        Arrays.fill(count, 0);
        currentAnswer = 0;
        
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
            
            answers[id] = currentAnswer;
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
        m = in.nextInt();
        k = in.nextInt();
        
        // 读取数组
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        
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