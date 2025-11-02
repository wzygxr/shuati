import java.util.*;

/**
 * AtCoder ABC174 F Range Set Query
 * 题目要求：区间查询不同元素个数
 * 核心技巧：莫队算法 + 分块
 * 时间复杂度：O(n√n)
 * 测试链接：https://atcoder.jp/contests/abc174/tasks/abc174_f
 * 
 * 莫队算法是一种离线查询的优化算法，适用于处理大量区间查询问题。
 * 它的核心思想是将查询按照块进行排序，然后逐个处理查询，利用之前计算的结果进行增量更新。
 * 对于不同元素个数查询，我们可以维护一个计数数组，记录当前区间内各元素出现的次数。
 */

public class Code38_AtCoder174F_Java {
    private static int n; // 数组长度
    private static int q; // 查询次数
    private static int[] a; // 原始数组
    private static Query[] queries; // 查询数组
    private static int blockSize; // 块大小
    private static int[] count; // 元素计数数组
    private static int currentDistinct; // 当前区间不同元素个数
    private static int[] answers; // 存储查询答案
    
    /**
     * 查询类，存储查询的左右边界和索引
     */
    private static class Query implements Comparable<Query> {
        int l, r, idx;
        
        public Query(int l, int r, int idx) {
            this.l = l;
            this.r = r;
            this.idx = idx;
        }
        
        @Override
        public int compareTo(Query other) {
            // 先按左端点所在块排序，同块内按右端点排序
            // 对于偶数块，右端点升序；对于奇数块，右端点降序（奇偶优化）
            int block1 = l / blockSize;
            int block2 = other.l / blockSize;
            if (block1 != block2) {
                return Integer.compare(block1, block2);
            }
            // 奇偶优化：不同块的奇偶性决定右端点排序方式
            return (block1 % 2 == 0) ? Integer.compare(r, other.r) : Integer.compare(other.r, r);
        }
    }
    
    /**
     * 向当前区间添加元素x
     */
    private static void add(int x) {
        if (count[x] == 0) {
            currentDistinct++;
        }
        count[x]++;
    }
    
    /**
     * 从当前区间移除元素x
     */
    private static void remove(int x) {
        count[x]--;
        if (count[x] == 0) {
            currentDistinct--;
        }
    }
    
    /**
     * 运行莫队算法处理所有查询
     */
    private static void solve() {
        // 初始化答案数组
        answers = new int[q];
        
        // 设置块大小，一般取√n
        blockSize = (int) Math.sqrt(n);
        
        // 找出数组中的最大值，用于确定count数组大小
        int maxValue = 0;
        for (int i = 0; i < n; i++) {
            maxValue = Math.max(maxValue, a[i]);
        }
        
        // 初始化计数数组
        count = new int[maxValue + 1];
        currentDistinct = 0;
        
        // 对查询进行排序
        Arrays.sort(queries);
        
        // 初始指针位置
        int currentL = 0, currentR = -1;
        
        // 处理每个查询
        for (Query query : queries) {
            // 调整左右指针，移动到目标区间
            while (currentL > query.l) {
                currentL--;
                add(a[currentL]);
            }
            while (currentR < query.r) {
                currentR++;
                add(a[currentR]);
            }
            while (currentL < query.l) {
                remove(a[currentL]);
                currentL++;
            }
            while (currentR > query.r) {
                remove(a[currentR]);
                currentR--;
            }
            
            // 保存当前查询的答案
            answers[query.idx] = currentDistinct;
        }
    }
    
    /**
     * 标准测试函数
     */
    public static void runTest() {
        Scanner scanner = new Scanner(System.in);
        
        // 读取输入
        n = scanner.nextInt();
        q = scanner.nextInt();
        
        a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        
        queries = new Query[q];
        for (int i = 0; i < q; i++) {
            int l = scanner.nextInt() - 1; // 注意题目输入可能是1-based，这里转为0-based
            int r = scanner.nextInt() - 1;
            queries[i] = new Query(l, r, i);
        }
        
        scanner.close();
        
        // 解决问题
        solve();
        
        // 输出答案，按照原始查询顺序
        for (int ans : answers) {
            System.out.println(ans);
        }
    }
    
    /**
     * 正确性测试
     */
    public static void correctnessTest() {
        System.out.println("=== 正确性测试 ===");
        
        // 测试用例1
        n = 5;
        a = new int[]{1, 2, 3, 2, 1};
        q = 3;
        queries = new Query[q];
        queries[0] = new Query(0, 4, 0); // [1,2,3,2,1] 不同元素个数：3
        queries[1] = new Query(1, 3, 1); // [2,3,2] 不同元素个数：2
        queries[2] = new Query(0, 0, 2); // [1] 不同元素个数：1
        
        solve();
        
        System.out.println("测试用例1结果：");
        for (int i = 0; i < q; i++) {
            System.out.println("查询 " + (i+1) + ": " + answers[i]);
        }
        
        // 测试用例2
        n = 10;
        a = new int[]{1, 1, 1, 2, 2, 3, 3, 3, 3, 4};
        q = 4;
        queries = new Query[q];
        queries[0] = new Query(0, 9, 0); // 全部元素 不同元素个数：4
        queries[1] = new Query(0, 2, 1); // 前三个1 不同元素个数：1
        queries[2] = new Query(3, 8, 2); // [2,2,3,3,3,3] 不同元素个数：2
        queries[3] = new Query(5, 9, 3); // [3,3,3,3,4] 不同元素个数：2
        
        solve();
        
        System.out.println("\n测试用例2结果：");
        for (int i = 0; i < q; i++) {
            System.out.println("查询 " + (i+1) + ": " + answers[i]);
        }
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 测试不同规模的数据
        int[] sizes = {1000, 10000, 100000};
        int[] queryCounts = {1000, 10000, 100000};
        
        for (int i = 0; i < sizes.length; i++) {
            int size = sizes[i];
            int qCount = queryCounts[i];
            
            // 生成随机数据
            n = size;
            a = new int[n];
            Random random = new Random(42); // 固定种子，保证可复现性
            for (int j = 0; j < n; j++) {
                a[j] = random.nextInt(size) + 1; // 元素范围1~size
            }
            
            // 生成随机查询
            q = qCount;
            queries = new Query[q];
            for (int j = 0; j < q; j++) {
                int l = random.nextInt(n);
                int r = random.nextInt(n - l) + l;
                queries[j] = new Query(l, r, j);
            }
            
            // 测量运行时间
            long startTime = System.currentTimeMillis();
            solve();
            long endTime = System.currentTimeMillis();
            
            System.out.println("数组大小: " + size + ", 查询次数: " + qCount + ", 耗时: " + (endTime - startTime) + " ms");
        }
    }
    
    /**
     * 边界测试
     */
    public static void boundaryTest() {
        System.out.println("=== 边界测试 ===");
        
        // 测试1：所有元素相同
        n = 1000;
        a = new int[n];
        Arrays.fill(a, 42);
        q = 3;
        queries = new Query[q];
        queries[0] = new Query(0, 999, 0); // 不同元素个数：1
        queries[1] = new Query(0, 0, 1);   // 不同元素个数：1
        queries[2] = new Query(500, 999, 2); // 不同元素个数：1
        
        solve();
        System.out.println("所有元素相同测试结果：");
        for (int i = 0; i < q; i++) {
            System.out.println(answers[i]);
        }
        
        // 测试2：所有元素不同
        n = 1000;
        a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i + 1;
        }
        q = 3;
        queries = new Query[q];
        queries[0] = new Query(0, 999, 0); // 不同元素个数：1000
        queries[1] = new Query(0, 499, 1); // 不同元素个数：500
        queries[2] = new Query(500, 500, 2); // 不同元素个数：1
        
        solve();
        System.out.println("\n所有元素不同测试结果：");
        for (int i = 0; i < q; i++) {
            System.out.println(answers[i]);
        }
    }
    
    /**
     * 莫队算法块大小优化分析
     */
    public static void blockSizeAnalysis() {
        System.out.println("=== 块大小优化分析 ===");
        
        n = 100000;
        a = new int[n];
        Random random = new Random(42);
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(n) + 1;
        }
        
        q = 100000;
        queries = new Query[q];
        for (int i = 0; i < q; i++) {
            int l = random.nextInt(n);
            int r = random.nextInt(n - l) + l;
            queries[i] = new Query(l, r, i);
        }
        
        int[] blockSizes = {(int)Math.sqrt(n)/2, (int)Math.sqrt(n), (int)Math.sqrt(n)*2, n/100, n/10};
        
        for (int bs : blockSizes) {
            // 保存原始块大小
            int originalBlockSize = blockSize;
            
            // 设置测试块大小
            blockSize = bs;
            
            // 重置计数和答案
            int maxValue = Arrays.stream(a).max().getAsInt();
            count = new int[maxValue + 1];
            currentDistinct = 0;
            answers = new int[q];
            
            // 排序查询
            Query[] tempQueries = Arrays.copyOf(queries, q);
            Arrays.sort(tempQueries);
            
            // 处理查询
            long startTime = System.currentTimeMillis();
            int currentL = 0, currentR = -1;
            for (Query query : tempQueries) {
                while (currentL > query.l) { currentL--; add(a[currentL]); }
                while (currentR < query.r) { currentR++; add(a[currentR]); }
                while (currentL < query.l) { remove(a[currentL]); currentL++; }
                while (currentR > query.r) { remove(a[currentR]); currentR--; }
                answers[query.idx] = currentDistinct;
            }
            long endTime = System.currentTimeMillis();
            
            System.out.println("块大小: " + bs + ", 耗时: " + (endTime - startTime) + " ms");
            
            // 恢复原始块大小
            blockSize = originalBlockSize;
        }
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) {
        System.out.println("AtCoder ABC174 F Range Set Query 解决方案");
        System.out.println("1. 运行标准测试（按题目输入格式）");
        System.out.println("2. 运行正确性测试");
        System.out.println("3. 运行性能测试");
        System.out.println("4. 运行边界测试");
        System.out.println("5. 运行块大小优化分析");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("请选择测试类型：");
        int choice = scanner.nextInt();
        scanner.close();
        
        switch (choice) {
            case 1:
                runTest();
                break;
            case 2:
                correctnessTest();
                break;
            case 3:
                performanceTest();
                break;
            case 4:
                boundaryTest();
                break;
            case 5:
                blockSizeAnalysis();
                break;
            default:
                System.out.println("无效选择，运行正确性测试");
                correctnessTest();
                break;
        }
    }
}

/*
 * 算法分析：
 * 
 * 莫队算法是一种离线查询的优化方法，特别适合处理区间查询问题。
 * 其核心思想是将查询按照特定顺序排序，然后逐个处理，利用增量更新的思想减少计算量。
 * 
 * 时间复杂度分析：
 * - 排序查询的时间复杂度：O(q log q)
 * - 处理查询的时间复杂度：
 *   - 对于块外的移动：每个查询最多移动O(√n)次块，每次块移动最多需要O(√n)次元素操作
 *   - 对于块内的移动：同一块内的右端点排序后，总共移动O(n)次
 *   - 总体时间复杂度：O((n + q)√n)
 *   - 在本题中，由于元素操作是O(1)的，所以时间复杂度为O(n√n)
 * 
 * 空间复杂度分析：
 * - 存储原始数组和查询：O(n + q)
 * - 存储计数数组：O(maxValue)，其中maxValue是数组中的最大值
 * - 总体空间复杂度：O(n + q + maxValue)
 * 
 * 优化技巧：
 * 1. 奇偶优化：对于偶数块，右端点升序排序；对于奇数块，右端点降序排序。这样可以减少缓存未命中。
 * 2. 块大小选择：通常选择√n作为块大小，但在实际应用中可能需要根据具体情况进行调整。
 * 3. 预处理：提前找出数组中的最大值，以便正确分配计数数组的大小。
 * 4. 指针移动顺序：按照统一的顺序移动指针，可以简化代码逻辑。
 * 
 * 莫队算法的优缺点：
 * 优点：
 * - 代码相对简单，容易实现
 * - 对于各种区间查询问题有较好的通用性
 * - 时间复杂度对于大多数问题来说是可接受的
 * 
 * 缺点：
 * - 只能处理离线查询，无法处理强制在线问题
 * - 对于某些特殊情况（如所有查询区间都很大），性能可能不如线段树等数据结构
 * 
 * 工程化考量：
 * 1. 输入输出效率：在大数据量情况下，需要使用快速的输入输出方法
 * 2. 内存管理：当数组元素值很大时，计数数组可能需要使用HashMap代替
 * 3. 并行处理：对于大量查询，可以考虑分块并行处理
 * 4. 边界处理：需要注意数组索引是否越界，尤其是在处理不同编程语言的数组索引习惯时
 */