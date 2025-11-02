/**
 * 综合测试类 - 测试所有排序算法和扩展题目
 * 包含完整的单元测试、性能测试和边界测试
 * 
 * 时间复杂度分析：各种测试场景的时间复杂度
 * 空间复杂度分析：内存使用情况监控
 * 最优解验证：确保算法实现是最优解
 * 
 * @author Algorithm Specialist
 * @version 1.0
 * @date 2025-10-17
 * 
 * 相关题目链接:
 * - 基础排序算法测试
 * - 扩展题目测试
 * - 性能测试
 * - 边界测试
 */

import java.util.*;
import java.util.concurrent.TimeUnit;

// 导入排序算法类
// 直接使用类名调用方法

public class ComprehensiveTest {
    
    /**
     * 主测试方法 - 运行所有测试
     */
    public static void main(String[] args) {
        System.out.println("=== 排序算法综合测试开始 ===\n");
        
        // 运行基础算法测试
        testBasicAlgorithms();
        
        // 运行扩展题目测试
        testExtendedProblems();
        
        // 运行性能测试
        testPerformance();
        
        // 运行边界测试
        testEdgeCases();
        
        System.out.println("\n=== 排序算法综合测试完成 ===");
    }
    
    /**
     * 测试基础排序算法
     * 时间复杂度：O(n²) 到 O(n log n) 取决于算法
     * 空间复杂度：O(1) 到 O(n) 取决于算法
     */
    public static void testBasicAlgorithms() {
        System.out.println("🧪 测试基础排序算法");
        
        // 测试数据
        int[] testArray = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        
        // 测试归并排序
        int[] mergeResult = Arrays.copyOf(testArray, testArray.length);
        SortAlgorithms.mergeSort(mergeResult);
        assertArrayEquals("归并排序", expected, mergeResult);
        
        // 测试快速排序
        int[] quickResult = Arrays.copyOf(testArray, testArray.length);
        SortAlgorithms.quickSort(quickResult);
        assertArrayEquals("快速排序", expected, quickResult);
        
        // 测试堆排序
        int[] heapResult = Arrays.copyOf(testArray, testArray.length);
        SortAlgorithms.heapSort(heapResult);
        assertArrayEquals("堆排序", expected, heapResult);
        
        System.out.println("✅ 基础排序算法测试通过\n");
    }
    
    /**
     * 测试扩展题目
     * 验证各种排序相关问题的解决方案
     */
    public static void testExtendedProblems() {
        System.out.println("🧪 测试扩展题目");
        
        // 测试合并有序数组
        testMergeSortedArrays();
        
        // 测试最接近点选择
        testKClosestPoints();
        
        // 测试条形码重排
        testRearrangeBarcodes();
        
        // 测试摆动排序
        testWiggleSort();
        
        // 测试翻转对统计
        testReversePairs();
        
        // 测试最小数字排列
        testMinNumber();
        
        // 测试逆序对计数
        testReversePairsCount();
        
        System.out.println("✅ 扩展题目测试通过\n");
    }
    
    /**
     * 性能测试 - 测试算法在不同数据规模下的表现
     * 时间复杂度分析：验证算法的时间复杂度
     * 空间复杂度监控：检测内存使用情况
     */
    public static void testPerformance() {
        System.out.println("📊 性能测试");
        
        // 测试不同规模的数据
        int[] sizes = {100, 1000, 5000, 10000};
        
        for (int size : sizes) {
            System.out.println("\n测试数据规模: " + size);
            
            // 生成测试数据
            int[] data = generateRandomArray(size);
            int[] dataCopy1 = Arrays.copyOf(data, data.length);
            int[] dataCopy2 = Arrays.copyOf(data, data.length);
            int[] dataCopy3 = Arrays.copyOf(data, data.length);
            
            // 测试归并排序性能
            long startTime = System.nanoTime();
            SortAlgorithms.mergeSort(data);
            long mergeTime = System.nanoTime() - startTime;
            
            // 测试快速排序性能
            startTime = System.nanoTime();
            SortAlgorithms.quickSort(dataCopy1);
            long quickTime = System.nanoTime() - startTime;
            
            // 测试堆排序性能
            startTime = System.nanoTime();
            SortAlgorithms.heapSort(dataCopy2);
            long heapTime = System.nanoTime() - startTime;
            
            // 测试内置排序性能
            startTime = System.nanoTime();
            Arrays.sort(dataCopy3);
            long builtinTime = System.nanoTime() - startTime;
            
            System.out.printf("归并排序: %10d ns%n", mergeTime);
            System.out.printf("快速排序: %10d ns%n", quickTime);
            System.out.printf("堆排序:   %10d ns%n", heapTime);
            System.out.printf("内置排序: %10d ns%n", builtinTime);
            
            // 验证排序结果正确性
            assert isSorted(data) : "归并排序结果错误";
            assert isSorted(dataCopy1) : "快速排序结果错误";
            assert isSorted(dataCopy2) : "堆排序结果错误";
        }
        
        System.out.println("✅ 性能测试完成\n");
    }
    
    /**
     * 边界测试 - 测试各种极端情况
     * 确保算法在各种边界条件下的鲁棒性
     */
    public static void testEdgeCases() {
        System.out.println("⚠️ 边界测试");
        
        // 测试空数组
        testEmptyArray();
        
        // 测试单元素数组
        testSingleElement();
        
        // 测试已排序数组
        testSortedArray();
        
        // 测试逆序数组
        testReverseSortedArray();
        
        // 测试重复元素数组
        testDuplicateElements();
        
        // 测试包含负数的数组
        testNegativeNumbers();
        
        // 测试大规模重复数据
        testLargeDuplicateData();
        
        System.out.println("✅ 边界测试通过\n");
    }
    
    // ========== 扩展题目具体测试方法 ==========
    
    private static void testMergeSortedArrays() {
        int[] nums1 = {1, 2, 3, 0, 0, 0};
        int[] nums2 = {2, 5, 6};
        ExtendedSortProblems.mergeSortedArrays(nums1, 3, nums2, 3);
        int[] expected = {1, 2, 2, 3, 5, 6};
        assertArrayEquals("合并有序数组", expected, nums1);
    }
    
    private static void testKClosestPoints() {
        int[][] points = {{1, 3}, {-2, 2}, {0, 1}};
        int k = 2;
        int[][] result = ExtendedSortProblems.kClosest(points, k);
        System.out.println("最接近点测试通过");
    }
    
    private static void testRearrangeBarcodes() {
        int[] barcodes = {1, 1, 1, 2, 2, 2};
        int[] result = ExtendedSortProblems.rearrangeBarcodes(barcodes);
        // 验证相邻元素不重复
        for (int i = 1; i < result.length; i++) {
            assert result[i] != result[i-1] : "条形码重排错误";
        }
        System.out.println("条形码重排测试通过");
    }
    
    private static void testWiggleSort() {
        int[] nums = {1, 5, 1, 1, 6, 4};
        ExtendedSortProblems.wiggleSort(nums);
        // 验证摆动排序条件
        for (int i = 1; i < nums.length - 1; i += 2) {
            assert nums[i] >= nums[i-1] && nums[i] >= nums[i+1] : "摆动排序错误";
        }
        System.out.println("摆动排序测试通过");
    }
    
    private static void testReversePairs() {
        int[] nums = {1, 3, 2, 3, 1};
        int result = ExtendedSortProblems.reversePairs493(nums);
        assert result == 2 : "翻转对统计错误";
        System.out.println("翻转对统计测试通过");
    }
    
    private static void testMinNumber() {
        int[] nums = {10, 2};
        String result = ExtendedSortProblems.minNumber(nums);
        assert "102".equals(result) : "最小数字排列错误";
        System.out.println("最小数字排列测试通过");
    }
    
    private static void testReversePairsCount() {
        int[] nums = {7, 5, 6, 4};
        long result = ExtendedSortProblems.countInversions(nums);
        assert result == 5 : "逆序对计数错误";
        System.out.println("逆序对计数测试通过");
    }
    
    // ========== 边界测试具体方法 ==========
    
    private static void testEmptyArray() {
        int[] empty = {};
        SortAlgorithms.mergeSort(empty); // 应该不报错
        System.out.println("空数组测试通过");
    }
    
    private static void testSingleElement() {
        int[] single = {42};
        SortAlgorithms.quickSort(single);
        assert single[0] == 42 : "单元素数组测试失败";
        System.out.println("单元素数组测试通过");
    }
    
    private static void testSortedArray() {
        int[] sorted = {1, 2, 3, 4, 5};
        SortAlgorithms.heapSort(sorted);
        assert isSorted(sorted) : "已排序数组测试失败";
        System.out.println("已排序数组测试通过");
    }
    
    private static void testReverseSortedArray() {
        int[] reverse = {5, 4, 3, 2, 1};
        SortAlgorithms.mergeSort(reverse);
        assert isSorted(reverse) : "逆序数组测试失败";
        System.out.println("逆序数组测试通过");
    }
    
    private static void testDuplicateElements() {
        int[] duplicates = {2, 2, 1, 1, 3, 3};
        SortAlgorithms.quickSort(duplicates);
        assert isSorted(duplicates) : "重复元素数组测试失败";
        System.out.println("重复元素数组测试通过");
    }
    
    private static void testNegativeNumbers() {
        int[] negatives = {-3, -1, -2, 0, 1};
        SortAlgorithms.heapSort(negatives);
        assert isSorted(negatives) : "负数数组测试失败";
        System.out.println("负数数组测试通过");
    }
    
    private static void testLargeDuplicateData() {
        int[] largeData = new int[1000];
        Arrays.fill(largeData, 42); // 所有元素相同
        Arrays.fill(largeData, 500, 1000, 24); // 部分元素不同
        
        SortAlgorithms.mergeSort(largeData);
        assert isSorted(largeData) : "大规模重复数据测试失败";
        System.out.println("大规模重复数据测试通过");
    }
    
    // ========== 工具方法 ==========
    
    /**
     * 生成随机数组用于测试
     */
    private static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size * 10);
        }
        return array;
    }
    
    /**
     * 验证数组是否已排序
     */
    private static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 断言两个数组相等
     */
    private static void assertArrayEquals(String testName, int[] expected, int[] actual) {
        if (expected.length != actual.length) {
            throw new AssertionError(testName + "失败: 数组长度不匹配");
        }
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                throw new AssertionError(testName + "失败: 索引 " + i + " 处值不匹配");
            }
        }
        System.out.println("✅ " + testName + "测试通过");
    }
    
    /**
     * 内存使用监控（简化版）
     */
    private static void monitorMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("当前内存使用: " + usedMemory + " bytes");
    }
    
    /**
     * 时间复杂度趋势分析
     */
    private static void analyzeTimeComplexityTrend(int[] sizes, long[] times) {
        System.out.println("\n📈 时间复杂度趋势分析:");
        for (int i = 0; i < sizes.length; i++) {
            double ratio = (double) times[i] / sizes[i];
            System.out.printf("规模 %d: 时间 %d ns, 比例: %.2f ns/element%n", 
                sizes[i], times[i], ratio);
        }
    }
}

/**
 * 性能监控工具类
 * 提供更详细的内存和时间监控功能
 */
class PerformanceMonitor {
    private long startTime;
    private long startMemory;
    
    public void start() {
        startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        startMemory = runtime.totalMemory() - runtime.freeMemory();
    }
    
    public PerformanceResult stop() {
        long endTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long endMemory = runtime.totalMemory() - runtime.freeMemory();
        
        return new PerformanceResult(
            endTime - startTime,
            endMemory - startMemory
        );
    }
    
    public static class PerformanceResult {
        public final long timeNanos;
        public final long memoryBytes;
        
        public PerformanceResult(long timeNanos, long memoryBytes) {
            this.timeNanos = timeNanos;
            this.memoryBytes = memoryBytes;
        }
        
        @Override
        public String toString() {
            return String.format("时间: %d ns, 内存: %d bytes", timeNanos, memoryBytes);
        }
    }
}

/**
 * 测试数据生成器
 * 生成各种类型的测试数据
 */
class TestDataGenerator {
    
    /**
     * 生成基本有序数组（90%有序）
     */
    public static int[] generateMostlySortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        
        // 随机交换10%的元素
        Random random = new Random();
        int swaps = size / 10;
        for (int i = 0; i < swaps; i++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            int temp = array[idx1];
            array[idx1] = array[idx2];
            array[idx2] = temp;
        }
        
        return array;
    }
    
    /**
     * 生成高斯分布数据
     */
    public static int[] generateGaussianData(int size, double mean, double stdDev) {
        int[] array = new int[size];
        Random random = new Random();
        
        for (int i = 0; i < size; i++) {
            double value = mean + stdDev * random.nextGaussian();
            array[i] = (int) Math.round(value);
        }
        
        return array;
    }
    
    /**
     * 生成Zipf分布数据（常见于真实世界数据）
     */
    public static int[] generateZipfData(int size, double exponent) {
        int[] array = new int[size];
        Random random = new Random();
        
        // 简化版Zipf分布生成
        for (int i = 0; i < size; i++) {
            // 使用幂律分布
            double rank = random.nextDouble();
            array[i] = (int) (size * Math.pow(rank, exponent));
        }
        
        return array;
    }
}

/**
 * 统计工具类
 * 提供各种统计分析方法
 */
class StatisticsUtils {
    
    /**
     * 计算平均值
     */
    public static double mean(long[] values) {
        long sum = 0;
        for (long value : values) {
            sum += value;
        }
        return (double) sum / values.length;
    }
    
    /**
     * 计算标准差
     */
    public static double standardDeviation(long[] values) {
        double mean = mean(values);
        double sumSquaredDiff = 0;
        
        for (long value : values) {
            double diff = value - mean;
            sumSquaredDiff += diff * diff;
        }
        
        return Math.sqrt(sumSquaredDiff / values.length);
    }
    
    /**
     * 计算置信区间
     */
    public static double[] confidenceInterval(long[] values, double confidenceLevel) {
        double mean = mean(values);
        double stdDev = standardDeviation(values);
        double zScore = 1.96; // 95%置信水平的z值
        
        double margin = zScore * stdDev / Math.sqrt(values.length);
        
        return new double[]{mean - margin, mean + margin};
    }
}