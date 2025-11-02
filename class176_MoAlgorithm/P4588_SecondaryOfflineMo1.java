import java.io.*;
import java.util.*;

/**
 * 洛谷 P4588 【模板】二次离线莫队
 * 题目链接：https://www.luogu.com.cn/problem/P4588
 * 
 * 题目描述：
 * 给定一个数组a[1...n]，有m次查询。每次查询[l,r]区间内满足a[i] + a[j]的二进制表示中1的个数是奇数的无序对(i,j)的数量。
 * 
 * 输入格式：
 * 第一行两个整数n和m，表示数组长度和查询次数。
 * 第二行n个整数表示数组元素。
 * 接下来m行，每行两个整数l, r表示查询区间。
 * 
 * 输出格式：
 * 对于每个查询，输出一行一个整数表示答案。
 * 
 * 数据范围：
 * 1 <= n, m <= 100000
 * 1 <= a[i] <= 100000
 * 
 * 解题思路：
 * 1. 首先，我们需要知道两个数的异或结果中1的个数的奇偶性等于它们和的二进制中1的个数的奇偶性
 * 2. 因此，问题转化为求区间内满足a[i] ^ a[j]的二进制表示中1的个数是奇数的无序对(i,j)的数量
 * 3. 对于每个位置j，我们可以维护一个前缀和sum[j]，表示前j个元素中，二进制中1的个数为奇数的元素个数
 * 4. 然后，我们可以使用二次离线莫队算法来高效处理这些查询
 * 
 * 时间复杂度：O(n * sqrt(n))
 * 空间复杂度：O(n + m)
 */
public class P4588_SecondaryOfflineMo1 {
    static final int MAXN = 100010;
    static final int MAX_VAL = 100010;
    
    // 输入数据
    static int n, m;
    static int[] a = new int[MAXN];
    static int[] cnt = new int[MAXN];  // 记录每个值的出现次数
    static int[] popcount = new int[MAX_VAL];  // 预处理每个数的二进制中1的个数的奇偶性
    static long[] ans = new long[MAXN];  // 存储每个查询的答案
    
    // 原始查询
    static class Query {
        int l, r, id;
    }
    static Query[] q = new Query[MAXN];
    static int blockSize;
    
    // 二次离线的查询
    static class Update {
        int l, r, x, id, type;
    }
    static List<Update>[] events = new ArrayList[MAXN];
    
    // 预处理每个数的二进制中1的个数的奇偶性
    static void preprocessPopcount() {
        for (int i = 1; i < MAX_VAL; i++) {
            popcount[i] = popcount[i >> 1] ^ (i & 1);  // 如果最后一位是1，奇偶性翻转
        }
    }
    
    // 比较器，用于莫队查询的排序
    static class QueryComparator implements Comparator<Query> {
        @Override
        public int compare(Query a, Query b) {
            if (a.l / blockSize != b.l / blockSize) {
                return a.l / blockSize - b.l / blockSize;
            }
            // 奇偶优化
            return (a.l / blockSize & 1) == 0 ? a.r - b.r : b.r - a.r;
        }
    }
    
    // 快速输入类
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;
        
        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
            st = null;
        }
        
        public int nextInt() throws IOException {
            while (st == null || !st.hasMoreTokens()) {
                st = new StringTokenizer(br.readLine());
            }
            return Integer.parseInt(st.nextToken());
        }
        
        public void close() throws IOException {
            br.close();
        }
    }
    
    public static void main(String[] args) throws IOException {
        FastReader reader = new FastReader();
        preprocessPopcount();
        
        n = reader.nextInt();
        m = reader.nextInt();
        
        // 初始化事件列表
        for (int i = 0; i < MAXN; i++) {
            events[i] = new ArrayList<>();
        }
        
        // 读取数组
        for (int i = 1; i <= n; i++) {
            a[i] = reader.nextInt();
        }
        
        // 读取查询
        for (int i = 0; i < m; i++) {
            q[i] = new Query();
            q[i].l = reader.nextInt();
            q[i].r = reader.nextInt();
            q[i].id = i;
        }
        
        // 设置块的大小
        blockSize = (int) Math.sqrt(n) + 1;
        
        // 对查询进行排序
        Arrays.sort(q, 0, m, new QueryComparator());
        
        // 二次离线莫队处理
        // 第一部分：莫队处理
        int curL = 1, curR = 0;
        long now = 0;  // 当前的答案
        
        for (int i = 0; i < m; i++) {
            int l = q[i].l, r = q[i].r;
            
            // 处理右边界的扩展
            if (r > curR) {
                events[curR].add(new Update(l, r, curR, i, 1));
                now += (long) (r - curR) * (l - 1);
                curR = r;
            }
            
            // 处理右边界的收缩
            if (r < curR) {
                events[r + 1].add(new Update(l, curR, l - 1, i, -1));
                now -= (long) (curR - r) * (l - 1);
                curR = r;
            }
            
            // 处理左边界的扩展
            if (l < curL) {
                events[curL - 1].add(new Update(l, curL - 1, r, i, 1));
                now += (long) (curL - l) * (n - r);
                curL = l;
            }
            
            // 处理左边界的收缩
            if (l > curL) {
                events[curL].add(new Update(l, l, r, i, -1));
                now -= (long) (l - curL) * (n - r);
                curL = l;
            }
            
            // 保存当前的中间结果
            ans[q[i].id] = now;
        }
        
        // 第二部分：离线处理事件
        Arrays.fill(cnt, 0);
        
        for (int i = 1; i <= n; i++) {
            // 处理所有与当前位置i相关的事件
            for (Update update : events[i]) {
                int l = update.l, r = update.r;
                int x = update.x;
                int id = update.id;
                int type = update.type;
                
                // 计算区间[l,r]中满足条件的元素个数
                int res = 0;
                for (int j = l; j <= r; j++) {
                    res += popcount[a[j]] ^ popcount[a[x]];
                }
                
                ans[q[id].id] += (long) res * type;
            }
            
            // 更新计数器
            cnt[a[i]]++;
        }
        
        // 处理最终的答案，计算无序对的数量
        for (int i = 0; i < m; i++) {
            int l = q[i].l, r = q[i].r;
            long total = (long) (r - l + 1) * (r - l) / 2;
            ans[q[i].id] = total - ans[q[i].id];
        }
        
        // 输出结果
        PrintWriter writer = new PrintWriter(System.out);
        for (int i = 0; i < m; i++) {
            writer.println(ans[i]);
        }
        writer.flush();
        writer.close();
        reader.close();
    }
    
    /*
     * 算法分析：
     * 时间复杂度：O(n * sqrt(n))
     * - 第一次莫队排序的时间复杂度：O(m * log m)
     * - 第一次莫队处理的时间复杂度：O((n + m) * sqrt(n))
     * - 第二次离线处理的时间复杂度：O(n * sqrt(n))
     * - 整体时间复杂度：O(n * sqrt(n))
     * 
     * 空间复杂度：O(n + m)
     * - 数组存储：O(n)
     * - 查询数组和答案数组：O(m)
     * - 事件列表：O(n + m)
     * 
     * 优化点：
     * 1. 使用了奇偶优化，减少块间转移的时间
     * 2. 使用了快速输入类，提高输入效率
     * 3. 通过预处理二进制中1的个数的奇偶性，加速计算
     * 4. 使用二次离线莫队算法，将时间复杂度从O(n * sqrt(n) * log n)优化到O(n * sqrt(n))
     * 
     * 边界情况处理：
     * 1. 确保查询区间的有效性
     * 2. 处理空区间的情况
     * 3. 使用long类型存储答案，避免溢出
     * 
     * 工程化考量：
     * 1. 使用ArrayList存储事件，提高代码灵活性
     * 2. 使用PrintWriter进行批量输出，提高输出效率
     * 3. 资源管理：及时关闭输入输出流
     * 
     * 调试技巧：
     * 1. 可以输出中间变量now的值，检查是否正确
     * 2. 测试用例：如n=3, m=1, a=[1,2,3]，查询[1,3]，预期结果为3（所有无序对都满足条件）
     * 3. 注意处理大数值，避免整数溢出
     */
}