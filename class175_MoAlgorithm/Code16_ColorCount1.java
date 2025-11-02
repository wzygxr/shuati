package class177;

// 数颜色 / 维护队列 /【模板】带修莫队
// 给定一个大小为N的数组arr，有两种操作：
// 1. Q L R 代表询问从第L支画笔到第R支画笔中共有几种不同颜色的画笔
// 2. R P C 把第P支画笔替换为颜色C
// 1 <= N,M <= 133333
// 1 <= arr[i],C <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P1903

// 解题思路：
// 这是带修莫队的经典模板题
// 带修莫队是普通莫队的扩展，支持修改操作
// 在普通莫队的基础上，引入时间维度，排序规则增加时间关键字
// 排序规则：
// 1. 按照左端点所在块编号排序
// 2. 如果左端点在同一块内，按照右端点所在块编号排序
// 3. 如果右端点也在同一块内，按照时间排序

// 时间复杂度分析：
// 1. 预处理排序：O((Q + M) * log(Q + M))
// 2. 带修莫队算法处理：O((N + Q + M) * N^(2/3))
// 3. 总时间复杂度：O((Q + M) * log(Q + M) + (N + Q + M) * N^(2/3))
// 空间复杂度分析：
// 1. 存储原数组：O(N)
// 2. 存储查询和修改操作：O(Q + M)
// 3. 计数数组：O(max(arr[i], C))
// 4. 总空间复杂度：O(N + Q + M + max(arr[i], C))

// 是否最优解：
// 这是该问题的最优解之一，带修莫队算法在处理这类支持修改的离线区间查询问题时具有很好的时间复杂度
// 对于在线查询问题，可以使用树状数组套主席树等数据结构，但对于离线问题，带修莫队算法是首选

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code16_ColorCount1 {

    public static int MAXN = 133335;
    
    public static int n, m;
    public static int[] arr = new int[MAXN];
    
    // 查询操作：类型1，l, r, 时间戳, 查询编号
    public static int[][] queries = new int[MAXN][5];
    public static int queryCount = 0;
    
    // 修改操作：类型2，位置, 新值, 旧值, 时间戳
    public static int[][] updates = new int[MAXN][4];
    public static int updateCount = 0;
    
    // 分块相关
    public static int blockSize;
    public static int[] belong = new int[MAXN];
    
    // 计数数组，记录每个颜色在当前窗口中的出现次数
    public static int[] count = new int[MAXN];
    // 当前窗口中不同颜色的种类数
    public static int distinctCount = 0;
    public static int[] answers = new int[MAXN];
    
    // 带修莫队排序规则
    public static class QueryComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] a, int[] b) {
            // 按照左端点所在块排序
            if (belong[a[1]] != belong[b[1]]) {
                return belong[a[1]] - belong[b[1]];
            }
            // 按照右端点所在块排序
            if (belong[a[2]] != belong[b[2]]) {
                return belong[a[2]] - belong[b[2]];
            }
            // 按照时间排序
            return a[3] - b[3];
        }
    }
    
    // 添加元素到窗口
    public static void add(int pos) {
        int val = arr[pos];
        // 如果该颜色之前没有出现过，现在出现了，种类数增加
        if (count[val] == 0) {
            distinctCount++;
        }
        count[val]++;
    }
    
    // 从窗口删除元素
    public static void remove(int pos) {
        int val = arr[pos];
        // 如果该颜色之前只出现了一次，现在删除后就没有了，种类数减少
        if (count[val] == 1) {
            distinctCount--;
        }
        count[val]--;
    }
    
    // 应用修改操作
    public static void applyUpdate(int time) {
        int pos = updates[time][1];
        int newVal = updates[time][2];
        int oldVal = updates[time][3];
        
        // 如果该位置在当前窗口内，需要更新答案
        if (pos >= l && pos <= r) {
            // 删除旧值的影响
            if (count[oldVal] == 1) {
                distinctCount--;
            }
            count[oldVal]--;
            
            // 添加新值的影响
            if (count[newVal] == 0) {
                distinctCount++;
            }
            count[newVal]++;
        }
        
        // 更新数组
        arr[pos] = newVal;
    }
    
    // 撤销修改操作
    public static void undoUpdate(int time) {
        int pos = updates[time][1];
        int newVal = updates[time][2];
        int oldVal = updates[time][3];
        
        // 如果该位置在当前窗口内，需要更新答案
        if (pos >= l && pos <= r) {
            // 删除新值的影响
            if (count[newVal] == 1) {
                distinctCount--;
            }
            count[newVal]--;
            
            // 添加旧值的影响
            if (count[oldVal] == 0) {
                distinctCount++;
            }
            count[oldVal]++;
        }
        
        // 更新数组
        arr[pos] = oldVal;
    }
    
    // 当前窗口边界
    public static int l = 1, r = 0;
    // 当前时间戳
    public static int now = 0;
    
    // 主计算函数
    public static void compute() {
        // 初始化计数数组
        Arrays.fill(count, 0);
        distinctCount = 0;
        
        l = 1;
        r = 0;
        now = 0;
        
        for (int i = 1; i <= queryCount; i++) {
            int ql = queries[i][1];
            int qr = queries[i][2];
            int qt = queries[i][3];
            int id = queries[i][4];
            
            // 调整窗口边界
            while (r < qr) add(++r);
            while (r > qr) remove(r--);
            while (l < ql) remove(l++);
            while (l > ql) add(--l);
            
            // 调整时间戳
            while (now < qt) applyUpdate(++now);
            while (now > qt) undoUpdate(now--);
            
            answers[id] = distinctCount;
        }
    }
    
    // 预处理函数
    public static void prepare() {
        // 计算分块大小，带修莫队通常选择 N^(2/3)
        blockSize = (int) Math.pow(n, 2.0/3.0);
        
        // 计算每个位置所属的块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 对查询进行排序
        Arrays.sort(queries, 1, queryCount + 1, new QueryComparator());
    }
    
    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = in.nextInt();
        m = in.nextInt();
        
        // 读取数组
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        
        // 读取操作
        for (int i = 1; i <= m; i++) {
            String op = in.next();
            if (op.equals("Q")) {
                // 查询操作
                queryCount++;
                queries[queryCount][0] = 1; // 类型1表示查询
                queries[queryCount][1] = in.nextInt(); // l
                queries[queryCount][2] = in.nextInt(); // r
                queries[queryCount][3] = updateCount; // 时间戳
                queries[queryCount][4] = queryCount; // 查询编号
            } else {
                // 修改操作
                updateCount++;
                updates[updateCount][0] = 2; // 类型2表示修改
                updates[updateCount][1] = in.nextInt(); // 位置
                updates[updateCount][2] = in.nextInt(); // 新值
                updates[updateCount][3] = arr[updates[updateCount][1]]; // 旧值
            }
        }
        
        prepare();
        compute();
        
        // 输出查询结果
        for (int i = 1; i <= queryCount; i++) {
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
        
        String next() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            StringBuilder sb = new StringBuilder();
            while (c > ' ' && c != -1) {
                sb.append((char) c);
                c = readByte();
            }
            return sb.toString();
        }
    }
}