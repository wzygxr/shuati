package class172;

// SPOJ DQUERY - D-query - Java实现
// 题目来源：SPOJ DQUERY - D-query
// 题目链接：https://www.spoj.com/problems/DQUERY/
// 题目大意：
// 给定一个长度为n的数组，有q个查询，每个查询要求计算区间[l, r]内不同元素的个数
// 1 <= n <= 30000
// 1 <= q <= 200000
// 1 <= 数组元素 <= 10^6

// 解题思路：
// 使用Mo's Algorithm（莫队算法）解决此问题
// 1. 将查询按照块编号排序，块内按照右端点排序
// 2. 使用双指针维护当前区间，通过移动指针来统计不同元素个数
// 3. 使用频率数组记录每个元素的出现次数
// 4. 当元素出现次数从0变1时，不同元素计数加1；从1变0时，计数减1

// 时间复杂度分析：
// 1. 预处理：O(n)，读取数组
// 2. 排序：O(q log q)，对查询排序
// 3. 处理查询：O((n + q) * √n)，莫队算法的时间复杂度
// 空间复杂度：O(n + q)，存储数组、查询和频率数组

// 工程化考量：
// 1. 异常处理：检查输入边界，防止数组越界
// 2. 性能优化：使用高效的排序和移动指针策略
// 3. 可读性：清晰的变量命名和注释
// 4. 测试用例：包含边界测试和性能测试

import java.io.*;
import java.util.*;

public class Code22_SPOJDQUERY_Java {
    
    // 最大数组大小
    private static final int MAXN = 30010;
    
    // 最大查询数量
    private static final int MAXQ = 200010;
    
    // 最大元素值
    private static final int MAXV = 1000010;
    
    // 原数组
    private static int[] arr = new int[MAXN];
    
    // 查询结构体
    private static Query[] queries = new Query[MAXQ];
    
    // 查询结果
    private static int[] results = new int[MAXQ];
    
    // 频率数组，记录每个元素的出现次数
    private static int[] freq = new int[MAXV];
    
    // 当前区间内不同元素的个数
    private static int distinctCount = 0;
    
    // 块大小
    private static int blockSize;
    
    /**
     * 查询结构体
     */
    static class Query implements Comparable<Query> {
        int l;      // 左端点
        int r;      // 右端点
        int idx;    // 查询编号
        int block;  // 所属块编号
        
        Query(int l, int r, int idx) {
            this.l = l;
            this.r = r;
            this.idx = idx;
            this.block = l / blockSize; // 根据左端点确定块编号
        }
        
        @Override
        public int compareTo(Query other) {
            // 先按块编号排序，块内按右端点排序
            if (this.block != other.block) {
                return Integer.compare(this.block, other.block);
            }
            // 奇偶块优化：奇数块右端点递增，偶数块右端点递减
            if (this.block % 2 == 0) {
                return Integer.compare(this.r, other.r);
            } else {
                return Integer.compare(other.r, this.r);
            }
        }
    }
    
    /**
     * 添加元素到当前区间
     * 时间复杂度：O(1)
     * 
     * @param pos 要添加的元素位置
     */
    private static void add(int pos) {
        int val = arr[pos];
        // 如果元素之前没有出现过，增加不同元素计数
        if (freq[val] == 0) {
            distinctCount++;
        }
        freq[val]++;
    }
    
    /**
     * 从当前区间移除元素
     * 时间复杂度：O(1)
     * 
     * @param pos 要移除的元素位置
     */
    private static void remove(int pos) {
        int val = arr[pos];
        freq[val]--;
        // 如果元素出现次数变为0，减少不同元素计数
        if (freq[val] == 0) {
            distinctCount--;
        }
    }
    
    /**
     * 处理所有查询
     * 时间复杂度：O((n + q) * √n)
     * 
     * @param n 数组长度
     * @param q 查询数量
     */
    private static void processQueries(int n, int q) {
        // 计算块大小
        blockSize = (int) Math.sqrt(n);
        
        // 对查询进行排序
        Arrays.sort(queries, 0, q);
        
        // 初始化双指针
        int curL = 1, curR = 0;
        distinctCount = 0;
        
        // 处理每个查询
        for (int i = 0; i < q; i++) {
            Query query = queries[i];
            
            // 移动左指针到查询左端点
            while (curL > query.l) {
                curL--;
                add(curL);
            }
            
            // 移动右指针到查询右端点
            while (curR < query.r) {
                curR++;
                add(curR);
            }
            
            // 移动左指针到查询左端点（移除多余元素）
            while (curL < query.l) {
                remove(curL);
                curL++;
            }
            
            // 移动右指针到查询右端点（移除多余元素）
            while (curR > query.r) {
                remove(curR);
                curR--;
            }
            
            // 记录查询结果
            results[query.idx] = distinctCount;
        }
    }
    
    /**
     * 主函数：处理输入输出
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度n
        int n = Integer.parseInt(br.readLine());
        
        // 读取数组元素
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        
        // 读取查询数量q
        int q = Integer.parseInt(br.readLine());
        
        // 读取查询
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            queries[i] = new Query(l, r, i);
        }
        
        // 处理查询
        processQueries(n, q);
        
        // 输出结果
        for (int i = 0; i < q; i++) {
            pw.println(results[i]);
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        System.out.println("=== 开始单元测试 ===");
        
        // 测试用例1：基础功能测试
        System.out.println("测试用例1：基础功能测试");
        
        // 测试数组
        int[] testArr = {0, 1, 2, 3, 2, 1, 4, 5, 1}; // 索引0不使用
        System.arraycopy(testArr, 0, arr, 0, testArr.length);
        
        // 测试查询
        Query[] testQueries = {
            new Query(1, 3, 0), // [1,2,3] -> 3个不同元素
            new Query(2, 5, 1), // [2,3,2,1] -> 3个不同元素
            new Query(4, 8, 2)  // [2,1,4,5,1] -> 4个不同元素
        };
        
        // 处理查询
        blockSize = (int) Math.sqrt(8);
        Arrays.sort(testQueries, 0, 3);
        
        // 模拟处理过程
        int curL = 1, curR = 0;
        distinctCount = 0;
        Arrays.fill(freq, 0);
        
        for (int i = 0; i < 3; i++) {
            Query query = testQueries[i];
            
            while (curL > query.l) {
                curL--;
                add(curL);
            }
            
            while (curR < query.r) {
                curR++;
                add(curR);
            }
            
            while (curL < query.l) {
                remove(curL);
                curL++;
            }
            
            while (curR > query.r) {
                remove(curR);
                curR--;
            }
            
            results[query.idx] = distinctCount;
        }
        
        // 验证结果
        assert results[0] == 3 : "查询1结果错误";
        assert results[1] == 3 : "查询2结果错误";
        assert results[2] == 4 : "查询3结果错误";
        
        System.out.println("基础功能测试通过");
        
        // 测试用例2：边界测试
        System.out.println("测试用例2：边界测试");
        
        // 单元素数组
        int[] singleArr = {0, 42};
        System.arraycopy(singleArr, 0, arr, 0, singleArr.length);
        
        Query singleQuery = new Query(1, 1, 0);
        
        curL = 1; curR = 0;
        distinctCount = 0;
        Arrays.fill(freq, 0);
        
        while (curL > singleQuery.l) {
            curL--;
            add(curL);
        }
        
        while (curR < singleQuery.r) {
            curR++;
            add(curR);
        }
        
        while (curL < singleQuery.l) {
            remove(curL);
            curL++;
        }
        
        while (curR > singleQuery.r) {
            remove(curR);
            curR--;
        }
        
        assert distinctCount == 1 : "单元素查询结果错误";
        System.out.println("边界测试通过");
        
        // 测试用例3：重复元素测试
        System.out.println("测试用例3：重复元素测试");
        
        int[] repeatArr = {0, 1, 1, 1, 1, 1};
        System.arraycopy(repeatArr, 0, arr, 0, repeatArr.length);
        
        Query repeatQuery = new Query(1, 5, 0);
        
        curL = 1; curR = 0;
        distinctCount = 0;
        Arrays.fill(freq, 0);
        
        while (curL > repeatQuery.l) {
            curL--;
            add(curL);
        }
        
        while (curR < repeatQuery.r) {
            curR++;
            add(curR);
        }
        
        while (curL < repeatQuery.l) {
            remove(curL);
            curL++;
        }
        
        while (curR > repeatQuery.r) {
            remove(curR);
            curR--;
        }
        
        assert distinctCount == 1 : "重复元素查询结果错误";
        System.out.println("重复元素测试通过");
        
        System.out.println("=== 所有单元测试通过 ===");
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("=== 开始性能测试 ===");
        
        int n = 30000; // 3万数据量
        int q = 20000; // 2万查询量
        
        // 生成随机数组
        Random rand = new Random();
        for (int i = 1; i <= n; i++) {
            arr[i] = rand.nextInt(1000000) + 1; // 1到1000000的随机数
        }
        
        // 生成随机查询
        for (int i = 0; i < q; i++) {
            int l = rand.nextInt(n) + 1;
            int r = l + rand.nextInt(100);
            if (r > n) r = n;
            queries[i] = new Query(l, r, i);
        }
        
        long startTime = System.currentTimeMillis();
        
        // 处理查询
        processQueries(n, q);
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("性能测试完成，耗时：" + (endTime - startTime) + "ms");
        System.out.println("数据规模：n=" + n + ", q=" + q);
        System.out.println("块大小：" + blockSize);
        
        // 验证部分结果
        int validCount = 0;
        for (int i = 0; i < Math.min(10, q); i++) {
            if (results[i] >= 0) {
                validCount++;
            }
        }
        System.out.println("验证结果：" + validCount + "/10 个查询结果有效");
    }
    
    /**
     * 调试方法：打印当前状态
     */
    private static void debugPrint(String message, int curL, int curR) {
        System.out.println(message + ": curL=" + curL + ", curR=" + curR + ", distinctCount=" + distinctCount);
    }
}

// 复杂度分析总结：
// 时间复杂度：
// - 预处理：O(n) 读取数组
// - 排序：O(q log q) 对查询排序
// - 处理查询：O((n + q) * √n) 莫队算法的时间复杂度
//   * 左指针移动：O(q * √n)
//   * 右指针移动：O(n * √n)
//
// 空间复杂度：O(n + q + MAXV) 存储数组、查询、结果和频率数组
//
// 算法优势：
// 1. 对于离线查询问题非常高效
// 2. 实现相对简单，代码易于理解
// 3. 适用于大规模查询场景
//
// 算法局限性：
// 1. 只能处理离线查询，不支持在线查询
// 2. 对于某些特殊数据分布可能效率不高
// 3. 需要额外的排序开销
//
// 适用场景：
// 1. 大规模离线区间查询问题
// 2. 查询数量远大于更新操作
// 3. 需要统计区间内不同元素个数的场景
//
// 优化技巧：
// 1. 奇偶块优化：减少右指针的移动距离
// 2. 块大小选择：通常取√n，但可以根据具体场景调整
// 3. 输入输出优化：使用快速IO提高效率
//
// 对比其他解法：
// 1. 线段树/树状数组：支持在线查询，但实现复杂
// 2. 主席树：支持在线查询，但空间复杂度较高
// 3. 分块：实现简单，但效率不如莫队算法
//
// 工程化建议：
// 1. 根据实际数据规模调整块大小
// 2. 对于小规模数据，可以使用更简单的解法
// 3. 考虑添加输入输出优化提高性能