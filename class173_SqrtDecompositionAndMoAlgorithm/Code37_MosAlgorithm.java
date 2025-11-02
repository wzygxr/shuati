// 莫队算法 - 离线查询优化 (Java版本)
// 题目来源: HDU 3433
// 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=3433
// 题目大意: 多次查询区间[l,r]内满足条件的元素对
// 约束条件: 数组长度n ≤ 10000，查询次数m ≤ 100000

import java.io.*;
import java.util.*;

class Query {
    int l, r, id;
    long answer;
    
    Query(int l, int r, int id) {
        this.l = l;
        this.r = r;
        this.id = id;
        this.answer = 0;
    }
}

public class Code37_MosAlgorithm {
    static final int MAXN = 10005;
    static final int MAXM = 100005;
    static final int MAXK = 55; // 题目中元素的最大值
    
    static int n, m, blen;
    static int[] arr;
    static int[] count;
    static long currentAnswer;
    static Query[] queries;
    
    // 比较器：按块排序查询
    static class QueryComparator implements Comparator<Query> {
        @Override
        public int compare(Query a, Query b) {
            int blockA = a.l / blen;
            int blockB = b.l / blen;
            
            if (blockA != blockB) {
                return Integer.compare(blockA, blockB);
            }
            
            // 奇偶排序优化
            if (blockA % 2 == 0) {
                return Integer.compare(a.r, b.r);
            } else {
                return Integer.compare(b.r, a.r);
            }
        }
    }
    
    // 比较器：按id排序查询
    static class IdComparator implements Comparator<Query> {
        @Override
        public int compare(Query a, Query b) {
            return Integer.compare(a.id, b.id);
        }
    }
    
    // 添加一个元素
    static void add(int pos) {
        int x = arr[pos];
        // 计算添加x后对结果的贡献
        // 对于每个已存在的y，检查是否满足x<=y且x*2>=y或y<=x且y*2>=x
        for (int y = 1; y < MAXK; y++) {
            if (count[y] > 0) {
                if ((x <= y && 2 * x >= y) || (y <= x && 2 * y >= x)) {
                    currentAnswer += count[y];
                }
            }
        }
        // 更新元素计数
        count[x]++;
    }
    
    // 移除一个元素
    static void remove(int pos) {
        int x = arr[pos];
        // 先减少计数，因为要计算移除前的影响
        count[x]--;
        // 计算移除x后对结果的影响
        for (int y = 1; y < MAXK; y++) {
            if (count[y] > 0) {
                if ((x <= y && 2 * x >= y) || (y <= x && 2 * y >= x)) {
                    currentAnswer -= count[y];
                }
            }
        }
    }
    
    // 莫队算法主函数
    static void moAlgorithm() {
        // 初始化块大小
        blen = (int)Math.sqrt(n);
        if (blen == 0) blen = 1;
        
        // 按块排序查询
        Arrays.sort(queries, new QueryComparator());
        
        // 初始化计数器和当前答案
        Arrays.fill(count, 0);
        currentAnswer = 0;
        
        // 初始化左右指针
        int currentL = 1;
        int currentR = 0;
        
        // 处理每个查询
        for (int i = 0; i < m; i++) {
            int l = queries[i].l;
            int r = queries[i].r;
            
            // 移动指针，维护当前区间
            while (currentL > l) add(--currentL);
            while (currentR < r) add(++currentR);
            while (currentL < l) remove(currentL++);
            while (currentR > r) remove(currentR--);
            
            // 记录查询结果
            queries[i].answer = currentAnswer;
        }
        
        // 按id排序查询，恢复原顺序
        Arrays.sort(queries, new IdComparator());
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        int t = Integer.parseInt(br.readLine());
        
        while (t-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());
            
            arr = new int[n + 1]; // 1-based索引
            st = new StringTokenizer(br.readLine());
            for (int i = 1; i <= n; i++) {
                arr[i] = Integer.parseInt(st.nextToken());
            }
            
            queries = new Query[m];
            for (int i = 0; i < m; i++) {
                st = new StringTokenizer(br.readLine());
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                queries[i] = new Query(l, r, i);
            }
            
            count = new int[MAXK];
            
            // 运行莫队算法
            moAlgorithm();
            
            // 输出结果
            for (int i = 0; i < m; i++) {
                pw.println(queries[i].answer);
            }
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
    
    /*
     * 时间复杂度分析：
     * - 排序查询：O(m log m)
     * - 莫队算法主循环：
     *   - 指针移动的总次数：O((n / √n) * n + m * √n) = O(n√n + m√n)
     *   - 每次add/remove操作：O(k)，k是元素的取值范围
     *   - 总体时间复杂度：O(m log m + (n + m)√n * k)
     * 
     * 空间复杂度分析：
     * - 存储数组和查询：O(n + m)
     * - 计数数组：O(k)
     * - 总体空间复杂度：O(n + m + k)
     * 
     * Java语言特性注意事项：
     * 1. 使用自定义比较器对查询进行排序
     * 2. 注意数组索引的处理（使用1-based索引）
     * 3. 使用StringTokenizer处理输入
     * 4. 使用BufferedReader和PrintWriter提高输入输出效率
     * 
     * 算法说明：
     * 莫队算法是一种离线查询优化技术，通过巧妙地排序查询顺序，
     * 最小化处理连续查询时指针移动的总距离，从而在时间复杂度和代码复杂度之间取得平衡。
     */
}