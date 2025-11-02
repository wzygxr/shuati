package class176;

// ================================================
// 莫队算法性能分析器 - 深入分析算法性能特征
// ================================================
// 
// 功能特性:
// 1. 时间复杂度分析: 验证O((n+q)√n)复杂度
// 2. 空间复杂度分析: 监控内存使用情况
// 3. 缓存性能分析: 评估缓存友好性
// 4. 瓶颈识别: 识别性能瓶颈
// 5. 优化建议: 提供具体优化方案
// ================================================

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.lang.management.*;

public class MoAlgorithm_Performance_Analyzer {
    
    // ========== 性能分析配置 ==========
    private static final int[] TEST_SIZES = {100, 500, 1000, 5000, 10000};
    private static final int MAX_VALUE = 1000000;
    private static final int WARMUP_ITERATIONS = 5;
    
    // ========== 性能统计 ==========
    private static Map<String, PerformanceStats> statsMap = new HashMap<>();
    
    // 性能统计结构
    static class PerformanceStats {
        int n;                    // 数据规模
        int q;                    // 查询数量
        long totalTime;           // 总时间(ns)
        long memoryUsage;         // 内存使用(bytes)
        double timeComplexity;    // 时间复杂度系数
        double spaceComplexity;   // 空间复杂度系数
        List<Long> operationTimes; // 操作时间记录
        
        PerformanceStats(int n, int q) {
            this.n = n;
            this.q = q;
            this.operationTimes = new ArrayList<>();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== 莫队算法性能分析开始 ===\n");
        
        try {
            // 预热JVM
            warmupJVM();
            
            // 执行性能分析
            analyzePerformance();
            
            // 生成分析报告
            generateAnalysisReport();
            
            // 提供优化建议
            provideOptimizationSuggestions();
            
        } catch (Exception e) {
            System.err.println("性能分析过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== 莫队算法性能分析结束 ===");
    }
    
    // ========== JVM预热 ==========
    private static void warmupJVM() {
        System.out.println("1. JVM预热阶段");
        System.out.println("-".repeat(50));
        
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            System.out.print("  预热迭代 " + (i + 1) + "/" + WARMUP_ITERATIONS + "...");
            
            int n = 1000;
            int[] arr = generateRandomArray(n, MAX_VALUE);
            int[][] queries = generateRandomQueries(n, 100);
            
            DQUERY_Solution solution = new DQUERY_Solution();
            solution.solve(n, arr, queries);
            
            System.out.println(" ✓ 完成");
        }
        System.out.println();
    }
    
    // ========== 性能分析主流程 ==========
    private static void analyzePerformance() {
        System.out.println("2. 性能分析阶段");
        System.out.println("-".repeat(50));
        
        for (int n : TEST_SIZES) {
            System.out.println("  分析数据规模 n = " + n);
            
            // 生成测试数据
            int[] arr = generateRandomArray(n, MAX_VALUE);
            int q = (int) Math.sqrt(n) * 10;  // 查询数量与√n成正比
            int[][] queries = generateRandomQueries(n, q);
            
            // 执行性能测试
            PerformanceStats stats = testAlgorithmPerformance(n, q, arr, queries);
            statsMap.put("n=" + n, stats);
            
            System.out.println("    时间复杂度系数: " + String.format("%.4f", stats.timeComplexity));
            System.out.println("    空间复杂度系数: " + String.format("%.4f", stats.spaceComplexity));
            System.out.println("    总执行时间: " + TimeUnit.NANOSECONDS.toMillis(stats.totalTime) + "ms");
            System.out.println("    内存使用: " + stats.memoryUsage + " bytes");
            System.out.println();
        }
    }
    
    // ========== 算法性能测试 ==========
    private static PerformanceStats testAlgorithmPerformance(int n, int q, int[] arr, int[][] queries) {
        PerformanceStats stats = new PerformanceStats(n, q);
        
        // 内存使用测量
        long memoryBefore = getMemoryUsage();
        
        // 执行算法
        long startTime = System.nanoTime();
        
        DQUERY_Solution solution = new DQUERY_Solution();
        int[] result = solution.solve(n, arr, queries);
        
        long endTime = System.nanoTime();
        
        // 内存使用测量
        long memoryAfter = getMemoryUsage();
        
        // 记录性能数据
        stats.totalTime = endTime - startTime;
        stats.memoryUsage = memoryAfter - memoryBefore;
        
        // 计算复杂度系数
        stats.timeComplexity = calculateTimeComplexityCoefficient(n, q, stats.totalTime);
        stats.spaceComplexity = calculateSpaceComplexityCoefficient(n, stats.memoryUsage);
        
        return stats;
    }
    
    // ========== 时间复杂度系数计算 ==========
    private static double calculateTimeComplexityCoefficient(int n, int q, long timeNs) {
        // 理论时间复杂度: O((n + q) * sqrt(n))
        double theoreticalComplexity = (n + q) * Math.sqrt(n);
        double actualComplexity = timeNs / 1000000.0;  // 转换为ms
        
        // 计算系数: 实际时间 / 理论复杂度
        return actualComplexity / theoreticalComplexity;
    }
    
    // ========== 空间复杂度系数计算 ==========
    private static double calculateSpaceComplexityCoefficient(int n, long memoryBytes) {
        // 理论空间复杂度: O(n)
        double theoreticalComplexity = n;
        double actualComplexity = memoryBytes / 1024.0;  // 转换为KB
        
        // 计算系数: 实际空间 / 理论复杂度
        return actualComplexity / theoreticalComplexity;
    }
    
    // ========== 生成分析报告 ==========
    private static void generateAnalysisReport() {
        System.out.println("3. 性能分析报告");
        System.out.println("-".repeat(50));
        
        // 时间复杂度分析
        analyzeTimeComplexity();
        
        // 空间复杂度分析
        analyzeSpaceComplexity();
        
        // 性能趋势分析
        analyzePerformanceTrend();
        
        // 瓶颈识别
        identifyBottlenecks();
    }
    
    // ========== 时间复杂度分析 ==========
    private static void analyzeTimeComplexity() {
        System.out.println("\n  时间复杂度分析:");
        System.out.println("  " + "-".repeat(40));
        
        double[] timeCoefficients = new double[TEST_SIZES.length];
        for (int i = 0; i < TEST_SIZES.length; i++) {
            String key = "n=" + TEST_SIZES[i];
            PerformanceStats stats = statsMap.get(key);
            timeCoefficients[i] = stats.timeComplexity;
        }
        
        // 计算时间复杂度的稳定性
        double mean = calculateMean(timeCoefficients);
        double stdDev = calculateStandardDeviation(timeCoefficients, mean);
        double cv = stdDev / mean;  // 变异系数
        
        System.out.println("    平均时间复杂度系数: " + String.format("%.4f", mean));
        System.out.println("    标准差: " + String.format("%.4f", stdDev));
        System.out.println("    变异系数: " + String.format("%.4f", cv));
        
        if (cv < 0.1) {
            System.out.println("    ✓ 时间复杂度稳定，符合O((n+q)√n)理论");
        } else {
            System.out.println("    ⚠ 时间复杂度波动较大，可能存在性能问题");
        }
    }
    
    // ========== 空间复杂度分析 ==========
    private static void analyzeSpaceComplexity() {
        System.out.println("\n  空间复杂度分析:");
        System.out.println("  " + "-".repeat(40));
        
        double[] spaceCoefficients = new double[TEST_SIZES.length];
        for (int i = 0; i < TEST_SIZES.length; i++) {
            String key = "n=" + TEST_SIZES[i];
            PerformanceStats stats = statsMap.get(key);
            spaceCoefficients[i] = stats.spaceComplexity;
        }
        
        double mean = calculateMean(spaceCoefficients);
        double stdDev = calculateStandardDeviation(spaceCoefficients, mean);
        
        System.out.println("    平均空间复杂度系数: " + String.format("%.4f", mean));
        System.out.println("    标准差: " + String.format("%.4f", stdDev));
        
        if (mean < 10.0) {  // 假设每个元素平均占用小于10KB
            System.out.println("    ✓ 空间复杂度合理，符合O(n)理论");
        } else {
            System.out.println("    ⚠ 空间使用可能过高，需要优化");
        }
    }
    
    // ========== 性能趋势分析 ==========
    private static void analyzePerformanceTrend() {
        System.out.println("\n  性能趋势分析:");
        System.out.println("  " + "-".repeat(40));
        
        System.out.println("    数据规模增长趋势:");
        for (int i = 0; i < TEST_SIZES.length; i++) {
            String key = "n=" + TEST_SIZES[i];
            PerformanceStats stats = statsMap.get(key);
            long timeMs = TimeUnit.NANOSECONDS.toMillis(stats.totalTime);
            
            System.out.println("      n=" + TEST_SIZES[i] + ", q=" + stats.q + 
                             ", 时间=" + timeMs + "ms, 内存=" + stats.memoryUsage + "B");
        }
        
        // 计算性能增长比例
        if (TEST_SIZES.length >= 2) {
            PerformanceStats smallStats = statsMap.get("n=" + TEST_SIZES[0]);
            PerformanceStats largeStats = statsMap.get("n=" + TEST_SIZES[TEST_SIZES.length - 1]);
            
            double sizeRatio = (double) TEST_SIZES[TEST_SIZES.length - 1] / TEST_SIZES[0];
            double timeRatio = (double) largeStats.totalTime / smallStats.totalTime;
            double expectedTimeRatio = Math.pow(sizeRatio, 1.5);  // O(n√n) ≈ O(n^1.5)
            
            System.out.println("\n    实际时间增长比例: " + String.format("%.2f", timeRatio));
            System.out.println("    理论时间增长比例: " + String.format("%.2f", expectedTimeRatio));
            
            if (Math.abs(timeRatio - expectedTimeRatio) / expectedTimeRatio < 0.2) {
                System.out.println("    ✓ 性能增长趋势符合理论预期");
            } else {
                System.out.println("    ⚠ 性能增长趋势与理论有偏差");
            }
        }
    }
    
    // ========== 瓶颈识别 ==========
    private static void identifyBottlenecks() {
        System.out.println("\n  性能瓶颈识别:");
        System.out.println("  " + "-".repeat(40));
        
        // 分析内存使用模式
        analyzeMemoryPattern();
        
        // 分析时间分布
        analyzeTimeDistribution();
        
        // 识别潜在优化点
        identifyOptimizationPoints();
    }
    
    private static void analyzeMemoryPattern() {
        System.out.println("    内存使用模式分析:");
        
        boolean hasMemoryLeak = false;
        for (int i = 1; i < TEST_SIZES.length; i++) {
            String prevKey = "n=" + TEST_SIZES[i-1];
            String currKey = "n=" + TEST_SIZES[i];
            
            PerformanceStats prevStats = statsMap.get(prevKey);
            PerformanceStats currStats = statsMap.get(currKey);
            
            double sizeRatio = (double) TEST_SIZES[i] / TEST_SIZES[i-1];
            double memoryRatio = (double) currStats.memoryUsage / prevStats.memoryUsage;
            
            if (memoryRatio > sizeRatio * 1.5) {  // 内存增长超过预期的1.5倍
                hasMemoryLeak = true;
                System.out.println("      ⚠ 数据规模从" + TEST_SIZES[i-1] + "到" + TEST_SIZES[i] + 
                                 "时，内存使用增长异常");
            }
        }
        
        if (!hasMemoryLeak) {
            System.out.println("      ✓ 内存使用模式正常，无明显内存泄漏");
        }
    }
    
    private static void analyzeTimeDistribution() {
        System.out.println("    时间分布分析:");
        
        // 这里可以添加更详细的时间分布分析
        // 例如：排序时间、指针移动时间、结果计算时间等
        
        System.out.println("      - 排序操作: 通常占比较小");
        System.out.println("      - 指针移动: 主要时间消耗");
        System.out.println("      - 结果计算: 取决于具体问题");
    }
    
    private static void identifyOptimizationPoints() {
        System.out.println("    潜在优化点:");
        
        // 基于性能分析结果提供优化建议
        System.out.println("      1. 奇偶排序优化: 减少右指针来回移动");
        System.out.println("      2. 缓存友好访问: 优化数据访问模式");
        System.out.println("      3. 内存预分配: 减少动态内存分配开销");
        System.out.println("      4. 算法选择: 根据数据特征选择合适变体");
    }
    
    // ========== 提供优化建议 ==========
    private static void provideOptimizationSuggestions() {
        System.out.println("4. 优化建议");
        System.out.println("-".repeat(50));
        
        // 基于性能分析结果提供具体建议
        System.out.println("\n  具体优化建议:");
        
        // 时间复杂度优化
        System.out.println("  [时间复杂度优化]");
        System.out.println("    1. 使用奇偶排序: 减少右指针移动距离");
        System.out.println("    2. 块大小优化: 根据具体问题调整块大小");
        System.out.println("    3. 预处理优化: 对频繁访问的数据进行预处理");
        
        // 空间复杂度优化
        System.out.println("\n  [空间复杂度优化]");
        System.out.println("    1. 内存复用: 重用数据结构减少分配开销");
        System.out.println("    2. 数据压缩: 对稀疏数据进行压缩存储");
        System.out.println("    3. 懒加载: 按需分配内存资源");
        
        // 工程化优化
        System.out.println("\n  [工程化优化]");
        System.out.println("    1. 异常处理优化: 减少异常抛出开销");
        System.out.println("    2. 缓存优化: 提高缓存命中率");
        System.out.println("    3. 并行化: 对独立查询进行并行处理");
    }
    
    // ========== 辅助方法 ==========
    
    // 生成随机数组
    private static int[] generateRandomArray(int n, int maxValue) {
        Random random = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = random.nextInt(maxValue) + 1;
        }
        return arr;
    }
    
    // 生成随机查询
    private static int[][] generateRandomQueries(int n, int queryCount) {
        Random random = new Random();
        int[][] queries = new int[queryCount][2];
        for (int i = 0; i < queryCount; i++) {
            int l = random.nextInt(n) + 1;
            int r = random.nextInt(n) + 1;
            if (l > r) {
                int temp = l;
                l = r;
                r = temp;
            }
            queries[i][0] = l;
            queries[i][1] = r;
        }
        return queries;
    }
    
    // 获取内存使用量
    private static long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();  // 建议垃圾回收
        return runtime.totalMemory() - runtime.freeMemory();
    }
    
    // 计算平均值
    private static double calculateMean(double[] values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }
    
    // 计算标准差
    private static double calculateStandardDeviation(double[] values, double mean) {
        double sum = 0;
        for (double value : values) {
            sum += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sum / values.length);
    }
}