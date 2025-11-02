import java.io.*;
import java.util.*;

/**
 * Codeforces 103D Time to Raid Cowavans
 * 题目要求：多次跳跃查询区间和
 * 核心技巧：分块预处理
 * 时间复杂度：O(n√n) 预处理，O(√n) 查询
 * 测试链接：https://codeforces.com/problemset/problem/103/D
 *
 * 该问题的核心思想是：对于每个查询(l, k)，我们需要计算从位置l开始，每隔k步取一个元素的和。
 * 直接暴力计算的时间复杂度为O(n)，对于大量查询会超时。
 * 使用分块预处理的方法，我们可以将时间复杂度优化到预处理O(n√n)，查询O(√n)。
 */

public class Code39_Codeforces103D_Java {
    private static final int MAXN = 100010;
    private static final int BLOCK_SIZE = 320; // sqrt(1e5) ≈ 316
    
    // 存储原始数组
    private static int[] a = new int[MAXN];
    // 分块预处理的结果，sum[k][i]表示步长为k时，从位置i开始的和（k <= BLOCK_SIZE）
    private static long[][] sum = new long[BLOCK_SIZE + 1][MAXN];
    
    /**
     * 预处理函数
     * 对于步长k <= BLOCK_SIZE的情况，预处理每个起始位置的和
     * 对于步长k > BLOCK_SIZE的情况，查询时暴力计算，因为这种情况下查询次数较少
     */
    private static void preprocess(int n) {
        // 预处理小步长的情况（k <= BLOCK_SIZE）
        for (int k = 1; k <= BLOCK_SIZE; k++) {
            for (int i = n; i >= 1; i--) {
                // sum[k][i] = a[i] + sum[k][i + k]（如果i + k <= n）
                sum[k][i] = a[i];
                if (i + k <= n) {
                    sum[k][i] += sum[k][i + k];
                }
            }
        }
    }
    
    /**
     * 查询函数
     * @param l 起始位置（1-based）
     * @param k 步长
     * @param n 数组长度
     * @return 从位置l开始，每隔k步取一个元素的和
     */
    private static long query(int l, int k, int n) {
        // 如果步长k很小，直接使用预处理的结果
        if (k <= BLOCK_SIZE) {
            return sum[k][l];
        }
        
        // 对于大步长，直接暴力计算，因为最多需要计算n/k次，而k > sqrt(n)，所以最多计算sqrt(n)次
        long res = 0;
        for (int i = l; i <= n; i += k) {
            res += a[i];
        }
        return res;
    }
    
    /**
     * 主函数，处理输入输出
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        int n = Integer.parseInt(br.readLine());
        String[] parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            a[i] = Integer.parseInt(parts[i - 1]);
        }
        
        // 预处理
        preprocess(n);
        
        int q = Integer.parseInt(br.readLine());
        while (q-- > 0) {
            parts = br.readLine().split(" ");
            int l = Integer.parseInt(parts[0]);
            int k = Integer.parseInt(parts[1]);
            // 注意题目中的位置是1-based的
            pw.println(query(l, k, n));
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
    
    /**
     * 正确性测试函数
     */
    public static void correctnessTest() {
        // 测试用例1：小步长查询
        int[] test1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}; // 索引从1开始
        int n1 = 10;
        System.arraycopy(test1, 0, a, 0, n1 + 1);
        preprocess(n1);
        
        System.out.println("正确性测试：");
        System.out.println("查询 (l=1, k=2): " + query(1, 2, n1)); // 应为1+3+5+7+9=25
        System.out.println("查询 (l=2, k=3): " + query(2, 3, n1)); // 应为2+5+8=15
        System.out.println("查询 (l=1, k=1): " + query(1, 1, n1)); // 应为55
        
        // 测试用例2：大步长查询
        int[] test2 = {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120};
        int n2 = 12;
        System.arraycopy(test2, 0, a, 0, n2 + 1);
        preprocess(n2);
        
        System.out.println("查询 (l=3, k=500): " + query(3, 500, n2)); // 应为30
        System.out.println("查询 (l=1, k=4): " + query(1, 4, n2)); // 应为10+50+90=150
        
        // 测试边界情况
        System.out.println("查询 (l=10, k=1): " + query(10, 1, n2)); // 应为100+110+120=330
        System.out.println("查询 (l=12, k=100): " + query(12, 100, n2)); // 应为120
    }
    
    /**
     * 性能测试函数
     */
    public static void performanceTest() {
        System.out.println("\n性能测试：");
        
        // 测试大规模数据
        int n = 100000;
        for (int i = 1; i <= n; i++) {
            a[i] = i; // 简单的数据模式
        }
        
        long startTime = System.currentTimeMillis();
        preprocess(n);
        long endTime = System.currentTimeMillis();
        System.out.println("预处理1e5数据耗时：" + (endTime - startTime) + "ms");
        
        // 测试不同步长的查询性能
        int q = 100000;
        long queryStartTime = System.currentTimeMillis();
        
        // 混合小步长和大步长查询
        Random rand = new Random(42);
        long totalResult = 0;
        for (int i = 0; i < q; i++) {
            int l = rand.nextInt(n) + 1;
            int k = rand.nextInt(n) + 1;
            totalResult += query(l, k, n);
        }
        
        long queryEndTime = System.currentTimeMillis();
        System.out.println("处理1e5查询耗时：" + (queryEndTime - queryStartTime) + "ms");
        System.out.println("总结果（避免编译器优化）：" + totalResult);
    }
    
    /**
     * 块大小优化分析函数
     */
    public static void blockSizeAnalysis() {
        System.out.println("\n块大小优化分析：");
        
        int n = 100000;
        for (int i = 1; i <= n; i++) {
            a[i] = i;
        }
        
        int[] blockSizes = {100, 200, 300, 320, 400, 500, 600, 1000};
        
        for (int bs : blockSizes) {
            // 临时修改块大小进行测试
            long[][] tempSum = new long[bs + 1][n + 2];
            
            long startTime = System.currentTimeMillis();
            // 预处理
            for (int k = 1; k <= bs; k++) {
                for (int i = n; i >= 1; i--) {
                    tempSum[k][i] = a[i];
                    if (i + k <= n) {
                        tempSum[k][i] += tempSum[k][i + k];
                    }
                }
            }
            
            long preprocessTime = System.currentTimeMillis() - startTime;
            
            // 测试查询性能
            int q = 100000;
            Random rand = new Random(42);
            long queryStartTime = System.currentTimeMillis();
            long totalResult = 0;
            
            for (int i = 0; i < q; i++) {
                int l = rand.nextInt(n) + 1;
                int k = rand.nextInt(n) + 1;
                
                if (k <= bs) {
                    totalResult += tempSum[k][l];
                } else {
                    long res = 0;
                    for (int j = l; j <= n; j += k) {
                        res += a[j];
                    }
                    totalResult += res;
                }
            }
            
            long queryTime = System.currentTimeMillis() - queryStartTime;
            
            System.out.printf("块大小=%d: 预处理耗时=%dms, 查询耗时=%dms\n", 
                             bs, preprocessTime, queryTime);
        }
    }
    
    /**
     * 边界情况测试
     */
    public static void boundaryTest() {
        System.out.println("\n边界情况测试：");
        
        // 测试n=1的情况
        int n1 = 1;
        a[1] = 100;
        preprocess(n1);
        System.out.println("n=1, 查询(1,1): " + query(1, 1, n1)); // 应为100
        System.out.println("n=1, 查询(1,100): " + query(1, 100, n1)); // 应为100
        
        // 测试l=n的情况
        int n2 = 1000;
        for (int i = 1; i <= n2; i++) {
            a[i] = i;
        }
        preprocess(n2);
        System.out.println("l=n=1000, k=1: " + query(n2, 1, n2)); // 应为1000
        System.out.println("l=n=1000, k=500: " + query(n2, 500, n2)); // 应为1000
        
        // 测试k=0的情况（题目中k应该是正数，这里进行健壮性测试）
        try {
            query(1, 0, n2);
        } catch (Exception e) {
            System.out.println("k=0异常处理正常");
        }
    }
    
    /**
     * 运行所有测试的函数
     */
    public static void runAllTests() {
        correctnessTest();
        performanceTest();
        blockSizeAnalysis();
        boundaryTest();
    }
}

/**
 * 算法原理解析：
 *
 * 1. 问题分析：
 *    - 给定一个数组，多次查询从某个位置l开始，每隔k步取一个元素的和
 *    - 直接暴力解法：对于每个查询，遍历所有符合条件的位置，时间复杂度O(n) per query
 *    - 当n和q都很大时，暴力解法会超时
 *
 * 2. 分块思想：
 *    - 将步长k分为两类：小步长(k <= √n)和大步长(k > √n)
 *    - 对于小步长：预处理所有可能的起始位置的和
 *    - 对于大步长：由于k > √n，每个查询最多需要访问√n个元素，直接暴力计算
 *
 * 3. 时间复杂度分析：
 *    - 预处理时间：O(√n * n)
 *    - 查询时间：
 *      - 小步长查询：O(1)
 *      - 大步长查询：O(√n)
 *    - 总体时间复杂度：O(n√n) 预处理 + O(q√n) 查询
 *
 * 4. 空间复杂度分析：
 *    - O(n√n) 用于存储预处理的结果
 *    - 在实际实现中，可以根据内存限制调整块大小
 *
 * 5. 优化技巧：
 *    - 选择合适的块大小：一般取√n，但可以根据具体测试数据进行调优
 *    - 预处理时从后往前计算，避免重复计算
 *    - 使用1-based索引，方便处理边界情况
 *    - 对于Java，使用数组而不是集合类可以提高性能
 *
 * 6. 适用场景：
 *    - 这种分块预处理方法适用于需要处理大量跳跃查询的场景
 *    - 当步长分布不均匀时，该方法特别有效
 *    - 相比线段树或树状数组，该方法实现更简单，且常数较小
 *
 * 7. 代码优化：
 *    - 预处理顺序优化：从后往前计算可以避免重复计算
 *    - 内存优化：只存储小步长的预处理结果
 *    - 循环展开：对于核心循环可以考虑循环展开优化
 *    - 避免边界检查：在预处理时确保索引安全
 *
 * 8. 最优解分析：
 *    - 该分块预处理方法是该问题的最优解之一
 *    - 时间复杂度达到了理论下界，无法再进一步优化
 *    - 对于特定的数据分布，可能有更优的算法，但对于一般情况，分块方法已经很高效
 */