import java.io.*;
import java.util.*;

/**
 * Codeforces 86D Powerful array
 * 题目链接：https://codeforces.com/problemset/problem/86/D
 * 
 * 题目描述：
 * 给定一个数组a[1...n]，有m次查询。每次查询[l,r]区间内，
 * 每种数字出现次数的平方和乘以该数字的值的总和。
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
 * 1 <= n, m <= 200000
 * 1 <= a[i] <= 10^6
 * 
 * 解题思路：
 * 1. 使用莫队算法处理区间查询
 * 2. 维护当前区间内每个数字的出现次数cnt[x]
 * 3. 维护当前答案ans，当添加或删除一个元素x时，更新ans：
 *    - 添加x：ans += x * (2 * cnt[x] + 1)，然后cnt[x]++
 *    - 删除x：cnt[x]--，然后ans -= x * (2 * cnt[x] + 1)
 * 4. 使用分块排序优化莫队算法的效率
 * 
 * 时间复杂度：O((n + m) * sqrt(n))
 * 空间复杂度：O(n + max_value)
 */
public class Codeforces86D_PowerfulArray1 {
    static final int MAXN = 200010;
    static final int MAXV = 1000010;
    
    // 输入数据
    static int n, m;
    static int[] arr = new int[MAXN];
    
    // 莫队算法相关
    static class Query {
        int l, r, id;  // 查询区间和索引
        long ans;      // 存储答案
    }
    static Query[] q = new Query[MAXN];
    static int blockSize;  // 块的大小
    static long[] cnt = new long[MAXV];  // 统计每个数字的出现次数
    
    // 快速输入类，提高输入效率
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
    
    // 比较器，用于莫队查询的排序
    static class QueryComparator implements Comparator<Query> {
        @Override
        public int compare(Query a, Query b) {
            // 按左端点所在块排序，同一块按右端点排序
            if (a.l / blockSize != b.l / blockSize) {
                return a.l / blockSize - b.l / blockSize;
            }
            // 奇偶优化：奇数块右端点升序，偶数块右端点降序
            return (a.l / blockSize & 1) == 0 ? a.r - b.r : b.r - a.r;
        }
    }
    
    // 更新当前区间的统计信息和答案
    static void update(int pos, long[] res, boolean add) {
        int x = arr[pos];
        if (add) {
            // 添加一个元素x，先更新答案，再增加计数
            res[0] += (long) x * (2 * cnt[x] + 1);
            cnt[x]++;
        } else {
            // 删除一个元素x，先减少计数，再更新答案
            cnt[x]--;
            res[0] -= (long) x * (2 * cnt[x] + 1);
        }
    }
    
    public static void main(String[] args) throws IOException {
        FastReader reader = new FastReader();
        n = reader.nextInt();
        m = reader.nextInt();
        
        // 读取数组
        for (int i = 1; i <= n; i++) {
            arr[i] = reader.nextInt();
        }
        
        // 读取查询
        for (int i = 0; i < m; i++) {
            q[i] = new Query();
            q[i].l = reader.nextInt();
            q[i].r = reader.nextInt();
            q[i].id = i;
        }
        
        // 设置块的大小，通常为sqrt(n)
        blockSize = (int) Math.sqrt(n) + 1;
        
        // 对查询进行排序
        Arrays.sort(q, 0, m, new QueryComparator());
        
        // 初始化指针和计数器
        int curL = 1, curR = 0;
        long[] res = new long[1];  // 使用数组存储结果，以便在update中修改
        
        // 莫队算法处理
        for (int i = 0; i < m; i++) {
            Query query = q[i];
            
            // 扩展或收缩区间
            while (curL > query.l) update(--curL, res, true);
            while (curR < query.r) update(++curR, res, true);
            while (curL < query.l) update(curL++, res, false);
            while (curR > query.r) update(curR--, res, false);
            
            // 保存当前查询的答案
            query.ans = res[0];
        }
        
        // 将答案按原顺序输出
        long[] output = new long[m];
        for (int i = 0; i < m; i++) {
            output[q[i].id] = q[i].ans;
        }
        
        PrintWriter writer = new PrintWriter(System.out);
        for (int i = 0; i < m; i++) {
            writer.println(output[i]);
        }
        writer.flush();
        writer.close();
        reader.close();
    }
    
    /*
     * 算法分析：
     * 时间复杂度：O((n + m) * sqrt(n))
     * - 排序查询的时间复杂度：O(m * log m)
     * - 莫队算法处理的时间复杂度：每个元素最多被访问O(sqrt(n))次，总时间为O(n * sqrt(n))
     * - 整体时间复杂度：O(m * log m + n * sqrt(n))，通常m和n同阶，所以为O((n + m) * sqrt(n))
     * 
     * 空间复杂度：O(n + MAXV)，其中MAXV是最大可能的数组元素值
     * - 数组存储：O(n)
     * - 查询数组：O(m)
     * - 计数数组：O(MAXV)
     * 
     * 优化点：
     * 1. 使用了奇偶优化，减少块间转移的时间
     * 2. 使用了快速输入类，提高输入效率
     * 3. 使用long类型存储结果，避免溢出
     * 
     * 边界情况处理：
     * 1. 数组元素的值可能很大，但通过使用适当大小的计数数组处理
     * 2. 结果可能超出int范围，使用long类型存储
     * 
     * 工程化考量：
     * 1. 使用静态数组代替动态分配，提高内存访问效率
     * 2. 使用PrintWriter进行批量输出，提高输出效率
     * 3. 资源管理：及时关闭输入输出流
     * 
     * 调试技巧：
     * 1. 可以在update函数中输出中间状态，检查计数和答案是否正确
     * 2. 测试用例：如n=3, m=1, arr=[1, 2, 1]，查询[1,3]，预期结果为1*2^2 + 2*1^2 = 4 + 2 = 6
     */
}