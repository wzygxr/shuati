package class176_MoAlgorithm;

import java.util.*;

/**
 * Mo's Algorithm 综合示例
 * 展示各种应用场景和最佳实践
 */
public class MoAlgorithm_Examples {
    
    /**
     * 示例1: 基础区间查询
     */
    public static void exampleBasicQueries() {
        System.out.println("=== 示例1: 基础区间查询 ===");
        
        // 测试数据
        int[] arr = {1, 2, 3, 1, 2, 3, 4, 5, 1, 2};
        int[][] queries = {
            {0, 2},  // [1,2,3] -> 3个不同元素
            {1, 4},  // [2,3,1,2] -> 3个不同元素  
            {3, 6},  // [1,2,3,4] -> 4个不同元素
            {5, 8}   // [3,4,5,1] -> 4个不同元素
        };
        
        // 使用基础Mo's Algorithm
        Code01_MoAlgorithm1_Fixed mo = new Code01_MoAlgorithm1_Fixed();
        int[] result = mo.processQueries(arr, queries);
        
        System.out.println("数组: " + Arrays.toString(arr));
        System.out.println("查询结果: " + Arrays.toString(result));
        
        // 验证结果
        for (int i = 0; i < queries.length; i++) {
            int l = queries[i][0];
            int r = queries[i][1];
            int expected = countDistinctNaive(arr, l, r);
            System.out.printf("查询[%d,%d]: 期望=%d, 实际=%d %s\n", 
                l, r, expected, result[i], 
                expected == result[i] ? "✓" : "✗");
        }
    }
    
    /**
     * 示例2: 大规模数据性能测试
     */
    public static void exampleLargeScalePerformance() {
        System.out.println("\n=== 示例2: 大规模数据性能测试 ===");
        
        // 生成大规模测试数据
        int n = 10000;
        int q = 1000;
        int[] arr = generateRandomArray(n, 100); // 100种不同值
        int[][] queries = generateRandomQueries(n, q);
        
        System.out.printf("数据规模: n=%d, q=%d\n", n, q);
        
        // 测试基础版本
        long startTime = System.currentTimeMillis();
        Code01_MoAlgorithm1_Fixed mo = new Code01_MoAlgorithm1_Fixed();
        int[] result1 = mo.processQueries(arr, queries);
        long moTime = System.currentTimeMillis() - startTime;
        
        // 测试高级优化版本
        startTime = System.currentTimeMillis();
        MoAlgorithm_Advanced_Optimized.AdvancedMoAlgorithm advancedMo = 
            new MoAlgorithm_Advanced_Optimized.AdvancedMoAlgorithm(arr);
        int[] result2 = advancedMo.processQueries(queries, 
            MoAlgorithm_Advanced_Optimized.OptimizationStrategy.STANDARD);
        long advancedTime = System.currentTimeMillis() - startTime;
        
        // 验证结果一致性
        boolean consistent = Arrays.equals(result1, result2);
        
        System.out.printf("基础版本: %d ms\n", moTime);
        System.out.printf("高级版本: %d ms\n", advancedTime);
        System.out.printf("结果一致性: %s\n", consistent ? "✓" : "✗");
        System.out.printf("性能提升: %.2f%%\n", 
            (double)(moTime - advancedTime) / moTime * 100);
    }
    
    /**
     * 示例3: 带修改的Mo's Algorithm
     */
    public static void exampleWithUpdates() {
        System.out.println("\n=== 示例3: 带修改的Mo's Algorithm ===");
        
        int[] arr = {1, 2, 3, 4, 5};
        int[][] queries = {
            {0, 2},  // [1,2,3] -> 3
            {1, 3},  // [2,3,4] -> 3
            {2, 4}   // [3,4,5] -> 3
        };
        
        MoAlgorithm_Advanced_Optimized.MoWithUpdates moWithUpdates = 
            new MoAlgorithm_Advanced_Optimized.MoWithUpdates(arr);
        
        // 执行修改操作
        System.out.println("原始数组: " + Arrays.toString(arr));
        
        // 修改位置2的值从3改为10
        moWithUpdates.addUpdate(2, 10);
        System.out.println("修改后数组: [1, 2, 10, 4, 5]");
        
        // 修改位置4的值从5改为20
        moWithUpdates.addUpdate(4, 20);
        System.out.println("修改后数组: [1, 2, 10, 4, 20]");
        
        // 处理查询
        int[] result = moWithUpdates.processQueriesWithUpdates(queries);
        
        System.out.println("查询结果: " + Arrays.toString(result));
        
        // 验证修改后的结果
        int[] modifiedArr = {1, 2, 10, 4, 20};
        for (int i = 0; i < queries.length; i++) {
            int l = queries[i][0];
            int r = queries[i][1];
            int expected = countDistinctNaive(modifiedArr, l, r);
            System.out.printf("查询[%d,%d]: 期望=%d, 实际=%d %s\n", 
                l, r, expected, result[i],
                expected == result[i] ? "✓" : "✗");
        }
    }
    
    /**
     * 示例4: 不同优化策略对比
     */
    public static void exampleOptimizationStrategies() {
        System.out.println("\n=== 示例4: 不同优化策略对比 ===");
        
        int[] arr = generateRandomArray(1000, 50);
        int[][] queries = generateRandomQueries(1000, 100);
        
        MoAlgorithm_Advanced_Optimized.AdvancedMoAlgorithm mo = 
            new MoAlgorithm_Advanced_Optimized.AdvancedMoAlgorithm(arr);
        
        // 测试不同优化策略
        long[] times = new long[3];
        int[][] results = new int[3][];
        
        // 标准优化
        long startTime = System.nanoTime();
        results[0] = mo.processQueries(queries, 
            MoAlgorithm_Advanced_Optimized.OptimizationStrategy.STANDARD);
        times[0] = System.nanoTime() - startTime;
        
        // Hilbert优化
        startTime = System.nanoTime();
        results[1] = mo.processQueries(queries, 
            MoAlgorithm_Advanced_Optimized.OptimizationStrategy.HILBERT);
        times[1] = System.nanoTime() - startTime;
        
        // 块优化
        startTime = System.nanoTime();
        results[2] = mo.processQueries(queries, 
            MoAlgorithm_Advanced_Optimized.OptimizationStrategy.BLOCK_OPTIMIZED);
        times[2] = System.nanoTime() - startTime;
        
        // 输出结果
        System.out.println("优化策略性能对比:");
        System.out.printf("标准优化: %.3f ms\n", times[0] / 1e6);
        System.out.printf("Hilbert优化: %.3f ms\n", times[1] / 1e6);
        System.out.printf("块优化: %.3f ms\n", times[2] / 1e6);
        
        // 验证结果一致性
        boolean consistent01 = Arrays.equals(results[0], results[1]);
        boolean consistent12 = Arrays.equals(results[1], results[2]);
        
        System.out.println("标准 vs Hilbert: " + (consistent01 ? "✓" : "✗"));
        System.out.println("Hilbert vs 块优化: " + (consistent12 ? "✓" : "✗"));
        
        // 找出最优策略
        int bestIndex = 0;
        for (int i = 1; i < 3; i++) {
            if (times[i] < times[bestIndex]) {
                bestIndex = i;
            }
        }
        
        String[] strategyNames = {"标准优化", "Hilbert优化", "块优化"};
        System.out.println("最优策略: " + strategyNames[bestIndex]);
    }
    
    /**
     * 示例5: 实际应用场景 - 数据分析
     */
    public static void exampleRealWorldScenario() {
        System.out.println("\n=== 示例5: 实际应用场景 - 数据分析 ===");
        
        // 模拟用户行为数据: 用户ID序列
        int[] userActions = {
            101, 102, 101, 103, 104, 102, 105, 101, 103, 106,
            107, 104, 108, 102, 109, 101, 110, 103, 111, 112
        };
        
        // 分析查询: 不同时间段内的活跃用户数
        int[][] timeWindows = {
            {0, 4},   // 时间段1: 动作0-4
            {5, 9},   // 时间段2: 动作5-9  
            {10, 14}, // 时间段3: 动作10-14
            {15, 19}  // 时间段4: 动作15-19
        };
        
        System.out.println("用户行为序列: " + Arrays.toString(userActions));
        
        // 使用Mo's Algorithm分析
        Code01_MoAlgorithm1_Fixed mo = new Code01_MoAlgorithm1_Fixed();
        int[] activeUsers = mo.processQueries(userActions, timeWindows);
        
        // 输出分析结果
        System.out.println("\n时间段活跃用户分析:");
        for (int i = 0; i < timeWindows.length; i++) {
            int start = timeWindows[i][0];
            int end = timeWindows[i][1];
            System.out.printf("时间段%d [%d-%d]: %d个活跃用户\n", 
                i + 1, start, end, activeUsers[i]);
        }
        
        // 进一步分析: 计算总活跃用户
        Set<Integer> allUsers = new HashSet<>();
        for (int userId : userActions) {
            allUsers.add(userId);
        }
        System.out.println("总活跃用户数: " + allUsers.size());
    }
    
    /**
     * 示例6: 错误处理和边界情况
     */
    public static void exampleErrorHandling() {
        System.out.println("\n=== 示例6: 错误处理和边界情况 ===");
        
        // 测试1: 空数组
        try {
            int[] emptyArr = {};
            int[][] emptyQueries = {};
            Code01_MoAlgorithm1_Fixed mo = new Code01_MoAlgorithm1_Fixed();
            int[] result = mo.processQueries(emptyArr, emptyQueries);
            System.out.println("空数组测试: ✓ 通过");
        } catch (Exception e) {
            System.out.println("空数组测试: ✗ 失败 - " + e.getMessage());
        }
        
        // 测试2: 单元素数组
        try {
            int[] singleArr = {42};
            int[][] singleQuery = {{0, 0}};
            Code01_MoAlgorithm1_Fixed mo = new Code01_MoAlgorithm1_Fixed();
            int[] result = mo.processQueries(singleArr, singleQuery);
            System.out.println("单元素数组测试: ✓ 通过");
        } catch (Exception e) {
            System.out.println("单元素数组测试: ✗ 失败 - " + e.getMessage());
        }
        
        // 测试3: 无效查询区间
        try {
            int[] arr = {1, 2, 3};
            int[][] invalidQueries = {{5, 10}}; // 超出数组边界
            Code01_MoAlgorithm1_Fixed mo = new Code01_MoAlgorithm1_Fixed();
            int[] result = mo.processQueries(arr, invalidQueries);
            System.out.println("无效查询测试: ✗ 应该抛出异常但未抛出");
        } catch (Exception e) {
            System.out.println("无效查询测试: ✓ 正确抛出异常");
        }
        
        // 测试4: 大数值范围
        try {
            int[] largeValueArr = {1, 1000000, 500000, 1000000, 1};
            int[][] queries = {{0, 4}};
            Code01_MoAlgorithm1_Fixed mo = new Code01_MoAlgorithm1_Fixed();
            int[] result = mo.processQueries(largeValueArr, queries);
            System.out.println("大数值范围测试: ✓ 通过");
        } catch (Exception e) {
            System.out.println("大数值范围测试: ✗ 失败 - " + e.getMessage());
        }
    }
    
    /**
     * 示例7: 性能优化技巧展示
     */
    public static void examplePerformanceOptimization() {
        System.out.println("\n=== 示例7: 性能优化技巧展示 ===");
        
        // 生成测试数据
        int n = 5000;
        int[] arr = generateRandomArray(n, 1000);
        int[][] queries = generateRandomQueries(n, 500);
        
        System.out.println("优化技巧对比:");
        
        // 技巧1: 值域压缩
        long startTime = System.nanoTime();
        int[] compressedArr = compressValues(arr);
        Code01_MoAlgorithm1_Fixed mo1 = new Code01_MoAlgorithm1_Fixed();
        int[] result1 = mo1.processQueries(compressedArr, queries);
        long time1 = System.nanoTime() - startTime;
        
        // 技巧2: 直接处理
        startTime = System.nanoTime();
        Code01_MoAlgorithm1_Fixed mo2 = new Code01_MoAlgorithm1_Fixed();
        int[] result2 = mo2.processQueries(arr, queries);
        long time2 = System.nanoTime() - startTime;
        
        System.out.printf("值域压缩: %.3f ms\n", time1 / 1e6);
        System.out.printf("直接处理: %.3f ms\n", time2 / 1e6);
        System.out.printf("压缩收益: %.2f%%\n", (double)(time2 - time1) / time2 * 100);
        
        // 验证结果一致性
        boolean consistent = Arrays.equals(result1, result2);
        System.out.println("结果一致性: " + (consistent ? "✓" : "✗"));
    }
    
    // ========== 辅助方法 ==========
    
    /**
     * 朴素方法计算区间内不同元素个数
     */
    private static int countDistinctNaive(int[] arr, int l, int r) {
        Set<Integer> set = new HashSet<>();
        for (int i = l; i <= r; i++) {
            set.add(arr[i]);
        }
        return set.size();
    }
    
    /**
     * 生成随机数组
     */
    private static int[] generateRandomArray(int n, int maxValue) {
        Random rand = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = rand.nextInt(maxValue) + 1;
        }
        return arr;
    }
    
    /**
     * 生成随机查询
     */
    private static int[][] generateRandomQueries(int n, int q) {
        Random rand = new Random();
        int[][] queries = new int[q][2];
        for (int i = 0; i < q; i++) {
            int l = rand.nextInt(n);
            int r = l + rand.nextInt(Math.min(100, n - l));
            queries[i][0] = l;
            queries[i][1] = r;
        }
        return queries;
    }
    
    /**
     * 值域压缩
     */
    private static int[] compressValues(int[] arr) {
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        
        Map<Integer, Integer> mapping = new HashMap<>();
        int idx = 1;
        for (int i = 0; i < sorted.length; i++) {
            if (i == 0 || sorted[i] != sorted[i-1]) {
                mapping.put(sorted[i], idx++);
            }
        }
        
        int[] compressed = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            compressed[i] = mapping.get(arr[i]);
        }
        return compressed;
    }
    
    /**
     * 主方法 - 运行所有示例
     */
    public static void main(String[] args) {
        System.out.println("=== Mo's Algorithm 综合示例 ===\n");
        
        // 运行所有示例
        exampleBasicQueries();
        exampleLargeScalePerformance();
        exampleWithUpdates();
        exampleOptimizationStrategies();
        exampleRealWorldScenario();
        exampleErrorHandling();
        examplePerformanceOptimization();
        
        System.out.println("\n=== 所有示例执行完成 ===");
    }
}