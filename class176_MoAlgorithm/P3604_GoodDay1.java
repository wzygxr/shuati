import java.io.*;
import java.util.*;

/**
 * 洛谷 P3604 美好的每一天
 * 题目链接：https://www.luogu.com.cn/problem/P3604
 * 
 * 题目描述：
 * 给定一个字符串，查询区间内能重排成回文串的子串个数。
 * 
 * 输入格式：
 * 第一行一个字符串s
 * 第二行一个整数m
 * 接下来m行，每行两个整数l, r表示查询区间
 * 
 * 输出格式：
 * 对于每个查询，输出一行一个整数表示答案
 * 
 * 数据范围：
 * 1 <= |s| <= 60000
 * 1 <= m <= 60000
 * 
 * 解题思路：
 * 1. 一个字符串能重排成回文串的条件是：最多有一个字符的出现次数是奇数
 * 2. 使用异或前缀和来记录每个字符的奇偶性（出现偶数次为0，奇数次为1）
 * 3. 对于子串[i+1,j]，如果其对应的异或值xor[j] ^ xor[i]有0或1个1，则可以重排成回文串
 * 4. 使用莫队算法维护当前区间内各个异或值的出现次数
 * 5. 对于每个异或值，统计有多少个其他异或值与其相差不超过1个1位
 * 
 * 时间复杂度：O((n + m) * sqrt(n) * 26) 或 O((n + m) * sqrt(n) * 2^26)（取决于实现方式）
 * 空间复杂度：O(n + 2^26)
 */
public class P3604_GoodDay1 {
    static final int MAXN = 60010;
    static final int MAX_MASK = 1 << 26;  // 26个小写字母，最多2^26种状态
    
    // 输入数据
    static int n, m;
    static char[] s;
    static int[] xor = new int[MAXN];  // 前缀异或数组
    
    // 莫队算法相关
    static class Query {
        int l, r, id;  // 查询区间和索引
        long ans;      // 存储答案
    }
    static Query[] q = new Query[MAXN];
    static int blockSize;  // 块的大小
    static long[] cnt = new long[MAX_MASK];  // 统计每个异或值的出现次数
    
    // 快速输入类，提高输入效率
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;
        
        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
            st = null;
        }
        
        public String next() throws IOException {
            while (st == null || !st.hasMoreTokens()) {
                st = new StringTokenizer(br.readLine());
            }
            return st.nextToken();
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
    
    // 计算与mask相差不超过1位的所有可能的异或值的出现次数之和
    static long count(int mask) {
        long res = cnt[mask];  // 相同掩码的情况
        // 枚举每一位，尝试翻转该位
        for (int i = 0; i < 26; i++) {
            res += cnt[mask ^ (1 << i)];
        }
        return res;
    }
    
    // 更新当前区间的统计信息和答案
    static void update(int pos, long[] res, boolean add) {
        int mask = xor[pos];
        if (add) {
            // 添加一个元素时，先统计可以配对的数量，再增加计数
            res[0] += count(mask);
            cnt[mask]++;
        } else {
            // 删除一个元素时，先减少计数，再减少对应的配对数量
            cnt[mask]--;
            res[0] -= count(mask);
        }
    }
    
    public static void main(String[] args) throws IOException {
        FastReader reader = new FastReader();
        s = reader.next().toCharArray();
        n = s.length;
        m = reader.nextInt();
        
        // 计算前缀异或数组
        xor[0] = 0;
        for (int i = 1; i <= n; i++) {
            int c = s[i - 1] - 'a';  // 将字符转换为0-25的数字
            xor[i] = xor[i - 1] ^ (1 << c);  // 异或操作记录奇偶性
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
        cnt[0] = 1;  // 初始时xor[0]出现一次
        
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
     * 时间复杂度：O((n + m) * sqrt(n) * 26)
     * - 排序查询的时间复杂度：O(m * log m)
     * - 莫队算法处理的时间复杂度：每个元素最多被访问O(sqrt(n))次，每次访问需要O(26)的时间进行位操作
     * - 整体时间复杂度：O(m * log m + n * sqrt(n) * 26)，通常m和n同阶，所以为O((n + m) * sqrt(n) * 26)
     * 
     * 空间复杂度：O(n + MAX_MASK)，其中MAX_MASK = 2^26
     * - 前缀异或数组：O(n)
     * - 查询数组：O(m)
     * - 计数数组：O(MAX_MASK)，但在实际中可能会占用较大的内存
     * 
     * 优化点：
     * 1. 使用了奇偶优化，减少块间转移的时间
     * 2. 使用了快速输入类，提高输入效率
     * 3. 通过位运算高效表示字符奇偶性状态
     * 
     * 边界情况处理：
     * 1. 初始时cnt[0] = 1，因为xor[0]本身也是一个前缀异或值
     * 2. 查询区间的转换：原问题中的[l,r]对应前缀异或数组的[l-1,r]
     * 
     * 工程化考量：
     * 1. 使用静态数组代替动态分配，提高内存访问效率
     * 2. 使用PrintWriter进行批量输出，提高输出效率
     * 3. 资源管理：及时关闭输入输出流
     * 
     * 调试技巧：
     * 1. 可以在update函数中输出中间状态，检查计数和答案是否正确
     * 2. 测试用例：如s="abba"，查询[1,4]，预期结果为6（所有子串都可以重排成回文串）
     * 3. 注意内存使用，MAX_MASK=2^26可能占用较大内存，可以考虑使用HashMap来优化空间，但会增加时间复杂度
     */
}