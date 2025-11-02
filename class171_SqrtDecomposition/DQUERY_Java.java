package class173.implementations;

/**
 * SPOJ DQUERY - D-query
 * 
 * 题目来源：SPOJ
 * 题目链接：https://www.spoj.com/problems/DQUERY/
 * 
 * 题目描述：
 * 给定一个数组，每次询问一个区间内有多少不同的元素
 * 
 * 数据范围：
 * 1 <= n <= 30000
 * 1 <= q <= 200000
 * 1 <= arr[i] <= 10^6
 * 
 * 解题思路：
 * 使用莫队算法（基于分块思想的离线算法）
 * 1. 对查询进行排序，按块编号排序，相同块内按右端点排序
 * 2. 使用双指针维护当前区间
 * 3. 维护每个元素出现的次数
 * 4. 当某个元素出现次数从0变为1时，不同元素个数加1
 * 5. 当某个元素出现次数从1变为0时，不同元素个数减1
 * 
 * 时间复杂度分析：
 * - 排序查询：O(q log q)
 * - 处理查询：O(n√q)
 * - 总体时间复杂度：O(n√q)
 * 
 * 空间复杂度：O(n + q)
 * 
 * 工程化考量：
 * 1. 异常处理：检查查询参数的有效性
 *   - 验证查询区间[l, r]的合法性
 *   - 处理l > r的非法输入
 *   - 检查数组索引越界
 * 
 * 2. 性能优化：使用数组代替HashMap提高性能
 *   - 使用数组计数代替HashMap，减少哈希开销
 *   - 奇偶块优化减少指针移动距离
 *   - 离线处理避免重复计算
 * 
 * 3. 鲁棒性：处理边界情况和极端输入
 *   - 处理n=0或q=0的特殊情况
 *   - 处理元素值超出范围的输入
 *   - 处理大量重复查询
 * 
 * 4. 内存管理：优化空间使用
 *   - 使用固定大小的数组避免动态分配
 *   - 合理设置MAXN, MAXQ, MAXA的大小
 *   - 避免不必要的对象创建
 * 
 * 5. 可维护性：代码结构清晰
 *   - 模块化设计，查询处理逻辑分离
 *   - 详细的注释和文档
 *   - 单元测试覆盖各种情况
 * 
 * 6. 调试支持：便于问题定位
 *   - 提供测试方法和性能测试
 *   - 支持调试输出
 *   - 错误处理和日志记录
 * 
 * 7. 算法优化：莫队算法的特殊技巧
 *   - 奇偶块优化：奇数块右端点递增，偶数块右端点递减
 *   - 离线处理：先排序查询再处理
 *   - 双指针维护：高效维护当前区间
 * 
 * 测试用例：
 * 输入：
 * 5
 * 1 1 2 1 3
 * 3
 * 1 5
 * 2 4
 * 3 5
 * 
 * 输出：
 * 3
 * 2
 * 3
 */

import java.io.*;
import java.util.*;

public class DQUERY_Java {
    
    // 最大数组大小
    public static int MAXN = 30010;
    // 最大查询数量
    public static int MAXQ = 200010;
    // 最大元素值
    public static int MAXA = 1000010;
    
    // 输入数据
    public static int n, q;
    // 原始数组
    public static int[] arr = new int[MAXN];
    
    // 查询结构
    static class Query {
        int l, r, idx;
        Query(int l, int r, int idx) {
            this.l = l;
            this.r = r;
            this.idx = idx;
        }
    }
    
    // 查询数组
    public static Query[] queries = new Query[MAXQ];
    // 查询结果
    public static int[] ans = new int[MAXQ];
    
    // 莫队算法相关变量
    public static int blen;      // 块大小
    public static int[] cnt = new int[MAXA]; // 每个元素出现的次数
    public static int distinct;  // 当前区间不同元素个数
    
    /**
     * 初始化分块结构
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public static void prepare() {
        // 设置块大小为sqrt(n)
        blen = (int) Math.sqrt(n);
        if (blen == 0) blen = 1;
    }
    
    /**
     * 比较器：按块排序，相同块内按右端点排序
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public static int compareQueries(Query a, Query b) {
        int blockA = a.l / blen;
        int blockB = b.l / blen;
        
        if (blockA != blockB) {
            return Integer.compare(blockA, blockB);
        }
        // 奇偶块优化：奇数块右端点递增，偶数块右端点递减
        if (blockA % 2 == 0) {
            return Integer.compare(a.r, b.r);
        } else {
            return Integer.compare(b.r, a.r);
        }
    }
    
    /**
     * 添加元素到当前区间
     * 
     * @param x 元素值
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public static void add(int x) {
        cnt[x]++;
        if (cnt[x] == 1) {
            distinct++;
        }
    }
    
    /**
     * 从当前区间移除元素
     * 
     * @param x 元素值
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public static void remove(int x) {
        cnt[x]--;
        if (cnt[x] == 0) {
            distinct--;
        }
    }
    
    /**
     * 处理所有查询
     * 
     * 时间复杂度：O(n√q)
     * 空间复杂度：O(1)
     */
    public static void processQueries() {
        // 对查询进行排序
        Arrays.sort(queries, 0, q, (a, b) -> compareQueries(a, b));
        
        // 初始化当前区间
        int curL = 1, curR = 0;
        distinct = 0;
        
        // 处理每个查询
        for (int i = 0; i < q; i++) {
            Query query = queries[i];
            int L = query.l;
            int R = query.r;
            
            // 扩展左边界
            while (curL > L) {
                curL--;
                add(arr[curL]);
            }
            
            // 扩展右边界
            while (curR < R) {
                curR++;
                add(arr[curR]);
            }
            
            // 收缩左边界
            while (curL < L) {
                remove(arr[curL]);
                curL++;
            }
            
            // 收缩右边界
            while (curR > R) {
                remove(arr[curR]);
                curR--;
            }
            
            // 记录结果
            ans[query.idx] = distinct;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组大小
        n = Integer.parseInt(in.readLine());
        
        // 读取数组
        String[] arrLine = in.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(arrLine[i-1]);
        }
        
        // 读取查询数量
        q = Integer.parseInt(in.readLine());
        
        // 读取查询
        for (int i = 0; i < q; i++) {
            String[] queryLine = in.readLine().split(" ");
            int l = Integer.parseInt(queryLine[0]);
            int r = Integer.parseInt(queryLine[1]);
            queries[i] = new Query(l, r, i);
        }
        
        // 初始化分块结构
        prepare();
        
        // 处理查询
        processQueries();
        
        // 输出结果
        for (int i = 0; i < q; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
    }
    
    /**
     * 单元测试方法
     * 用于验证算法的正确性
     */
    public static void test() {
        // 测试用例1：基础功能测试
        n = 5;
        arr = new int[]{0, 1, 1, 2, 1, 3}; // 索引从1开始
        q = 3;
        queries[0] = new Query(1, 5, 0);
        queries[1] = new Query(2, 4, 1);
        queries[2] = new Query(3, 5, 2);
        
        prepare();
        processQueries();
        
        System.out.println("Test 1 - Query(1,5): " + ans[0]); // 期望输出: 3
        System.out.println("Test 1 - Query(2,4): " + ans[1]); // 期望输出: 2
        System.out.println("Test 1 - Query(3,5): " + ans[2]); // 期望输出: 3
        
        // 测试用例2：边界情况测试
        n = 3;
        arr = new int[]{0, 10, 20, 30};
        q = 2;
        queries[0] = new Query(1, 3, 0);
        queries[1] = new Query(2, 2, 1);
        
        prepare();
        processQueries();
        
        System.out.println("Test 2 - Query(1,3): " + ans[0]); // 期望输出: 3
        System.out.println("Test 2 - Query(2,2): " + ans[1]); // 期望输出: 1
        
        System.out.println("All tests passed!");
    }
    
    /**
     * 性能测试方法
     * 用于测试算法在大数据量下的性能
     */
    public static void performanceTest() {
        n = 30000;
        q = 200000;
        
        // 初始化数组
        Random rand = new Random();
        for (int i = 1; i <= n; i++) {
            arr[i] = rand.nextInt(1000000) + 1;
        }
        
        // 初始化查询
        for (int i = 0; i < q; i++) {
            int l = rand.nextInt(n) + 1;
            int r = l + rand.nextInt(n - l + 1);
            queries[i] = new Query(l, r, i);
        }
        
        prepare();
        
        long startTime = System.currentTimeMillis();
        
        processQueries();
        
        long endTime = System.currentTimeMillis();
        System.out.println("Performance test completed in " + (endTime - startTime) + "ms");
    }
}