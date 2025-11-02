import java.io.*;
import java.util.*;

/**
 * Codeforces 617E XOR and Favorite Number
 * 题目链接：https://codeforces.com/contest/617/problem/E
 * 
 * 题目描述：
 * 给定一个长度为n的数组arr和一个目标值k，有m次查询。
 * 每次查询给出区间[l,r]，求区间内有多少个连续子数组的异或和等于k。
 * 
 * 输入格式：
 * 第一行三个整数n, m, k
 * 第二行n个整数表示数组arr
 * 接下来m行，每行两个整数l, r表示查询区间
 * 
 * 输出格式：
 * 对于每个查询，输出一行一个整数表示答案
 * 
 * 数据范围：
 * 1 <= n, m <= 100000
 * 0 <= arr[i], k < 1000000
 * 
 * 解题思路：
 * 1. 前缀异或数组：使用前缀异或数组xor，其中xor[i]表示前i个元素的异或和
 * 2. 对于子数组[l,r]的异或和为xor[r] ^ xor[l-1]
 * 3. 我们需要统计在区间[l-1,r]中有多少对(i,j)满足xor[j] ^ xor[i] == k，其中i < j
 * 4. 使用莫队算法维护当前区间内各个xor值的出现次数
 * 5. 当扩展区间时，更新计数并累加答案
 * 
 * 时间复杂度：O((n + m) * sqrt(n))
 * 空间复杂度：O(n + max_xor_value)
 */
public class Codeforces617E_XORAndFavoriteNumber1 {
    static final int MAXN = 100010;
    static final int MAXK = 1000010;
    
    // 输入数据
    static int n, m, k;
    static int[] arr = new int[MAXN];
    static long[] xor = new long[MAXN];  // 前缀异或数组
    
    // 莫队算法相关
    static class Query {
        int l, r, id;  // 查询区间和索引
        long ans;      // 存储答案
    }
    static Query[] q = new Query[MAXN];
    static int blockSize;  // 块的大小
    static long[] cnt = new long[MAXK];  // 统计每个异或值的出现次数
    
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
        long current_xor = xor[pos];
        if (add) {
            // 添加一个元素时，增加对应的配对数量
            res[0] += cnt[(current_xor ^ k) % MAXK];
            cnt[(current_xor) % MAXK]++;
        } else {
            // 删除一个元素时，减少对应的配对数量
            cnt[(current_xor) % MAXK]--;
            res[0] -= cnt[(current_xor ^ k) % MAXK];
        }
    }
    
    public static void main(String[] args) throws IOException {
        FastReader reader = new FastReader();
        n = reader.nextInt();
        m = reader.nextInt();
        k = reader.nextInt();
        
        // 计算前缀异或数组
        xor[0] = 0;
        for (int i = 1; i <= n; i++) {
            arr[i] = reader.nextInt();
            xor[i] = xor[i - 1] ^ arr[i];
        }
        
        // 读取查询
        for (int i = 0; i < m; i++) {
            q[i] = new Query();
            q[i].l = reader.nextInt() - 1;  // 转换为前缀异或数组的索引
            q[i].r = reader.nextInt();
            q[i].id = i;
        }
        
        // 设置块的大小，通常为sqrt(n)
        blockSize = (int) Math.sqrt(n) + 1;
        
        // 对查询进行排序
        Arrays.sort(q, 0, m, new QueryComparator());
        
        // 初始化指针和计数器
        int curL = 0, curR = -1;
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
     * 空间复杂度：O(n + MAXK)，其中MAXK是最大可能的异或值
     * - 前缀异或数组：O(n)
     * - 查询数组：O(m)
     * - 计数数组：O(MAXK)
     * 
     * 优化点：
     * 1. 使用了奇偶优化，减少块间转移的时间
     * 2. 使用了快速输入类，提高输入效率
     * 3. 模数处理防止数组越界
     * 
     * 边界情况处理：
     * 1. 查询区间的转换：原问题中的[l,r]对应前缀异或数组的[l-1,r]
     * 2. 异或值可能超过计数数组大小，使用模运算处理
     * 
     * 工程化考量：
     * 1. 使用静态数组代替动态分配，提高内存访问效率
     * 2. 使用PrintWriter进行批量输出，提高输出效率
     * 3. 资源管理：及时关闭输入输出流
     * 
     * 调试技巧：
     * 1. 可以在update函数中输出中间状态，检查计数是否正确
     * 2. 测试用例：如n=3, m=1, k=3, arr=[1, 2, 3]，预期结果为2（子数组[1,2]和[3]）
     */
}