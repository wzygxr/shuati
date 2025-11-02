package class027;

import java.util.*;

/**
 * 堆算法题目集综合测试类
 * 用于验证所有堆相关题目的正确性
 */
public class TestAllSolutions {
    
    public static void main(String[] args) {
        System.out.println("开始测试堆算法题目集...");
        
        // 测试1: 最多线段重合问题
        testMaxCoverLines();
        
        // 测试2: 合并果子问题
        testMergeFruits();
        
        // 测试3: 运行中位数
        testRunningMedian();
        
        // 测试4: 任务调度器
        testTaskScheduler();
        
        System.out.println("所有测试完成！");
    }
    
    /**
     * 测试数组中的第K个最大元素
     */
    private static void testKthLargestElement() {
        System.out.println("\n=== 测试1: 数组中的第K个最大元素 ===");
        
        int[] nums = {3, 2, 1, 5, 6, 4};
        int k = 2;
        int expected = 5;
        
        int result = Code04_KthLargestElementInArray.findKthLargest(nums, k);
        System.out.println("输入数组: " + Arrays.toString(nums));
        System.out.println("K = " + k);
        System.out.println("期望结果: " + expected);
        System.out.println("实际结果: " + result);
        System.out.println("测试" + (result == expected ? "通过" : "失败"));
    }
    
    /**
     * 测试前K个高频元素
     */
    private static void testTopKFrequentElements() {
        System.out.println("\n=== 测试2: 前K个高频元素 ===");
        
        int[] nums = {1, 1, 1, 2, 2, 3};
        int k = 2;
        int[] expected = {1, 2};
        
        int[] result = Code05_TopKFrequentElements.topKFrequent(nums, k);
        System.out.println("输入数组: " + Arrays.toString(nums));
        System.out.println("K = " + k);
        System.out.println("期望结果: " + Arrays.toString(expected));
        System.out.println("实际结果: " + Arrays.toString(result));
        System.out.println("测试" + (Arrays.equals(result, expected) ? "通过" : "失败"));
    }
    
    /**
     * 测试最多线段重合问题
     */
    private static void testMaxCoverLines() {
        System.out.println("\n=== 测试3: 最多线段重合问题 ===");
        
        int[][] lines = {
            {1, 4}, {2, 5}, {3, 6}, {4, 7}
        };
        int expected = 3;
        
        int result = Code28_MoreHeapProblems.maxCoverLines(lines);
        System.out.println("线段数组: " + Arrays.deepToString(lines));
        System.out.println("期望结果: " + expected);
        System.out.println("实际结果: " + result);
        System.out.println("测试" + (result == expected ? "通过" : "失败"));
    }
    
    /**
     * 测试合并果子问题
     */
    private static void testMergeFruits() {
        System.out.println("\n=== 测试4: 合并果子问题 ===");
        
        int[] fruits = {1, 2, 9};
        int expected = 15;
        
        int result = Code28_MoreHeapProblems.mergeFruits(fruits);
        System.out.println("果子重量: " + Arrays.toString(fruits));
        System.out.println("期望结果: " + expected);
        System.out.println("实际结果: " + result);
        System.out.println("测试" + (result == expected ? "通过" : "失败"));
    }
    
    /**
     * 运行中位数测试
     */
    private static void testRunningMedian() {
        System.out.println("\n=== 测试5: 运行中位数 ===");
        
        int[] arr = {1, 2, 3, 4, 5};
        double[] expected = {1.0, 1.5, 2.0, 2.5, 3.0};
        
        double[] result = Code28_MoreHeapProblems.findRunningMedian(arr);
        System.out.println("输入数组: " + Arrays.toString(arr));
        System.out.println("期望结果: " + Arrays.toString(expected));
        System.out.println("实际结果: " + Arrays.toString(result));
        
        boolean passed = true;
        for (int i = 0; i < result.length; i++) {
            if (Math.abs(result[i] - expected[i]) > 0.001) {
                passed = false;
                break;
            }
        }
        System.out.println("测试" + (passed ? "通过" : "失败"));
    }
    
    /**
     * 测试任务调度器
     */
    private static void testTaskScheduler() {
        System.out.println("\n=== 测试6: 任务调度器 ===");
        
        char[] tasks = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n = 2;
        int expected = 8;
        
        int result = Code28_MoreHeapProblems.taskScheduler(tasks, n);
        System.out.println("任务序列: " + Arrays.toString(tasks));
        System.out.println("冷却时间: " + n);
        System.out.println("期望结果: " + expected);
        System.out.println("实际结果: " + result);
        System.out.println("测试" + (result == expected ? "通过" : "失败"));
    }
    
    /**
     * 性能测试：大规模数据下的堆操作
     */
    private static void performanceTest() {
        System.out.println("\n=== 性能测试: 大规模数据堆操作 ===");
        
        int size = 100000;
        int[] largeArray = new int[size];
        Random random = new Random();
        
        // 生成随机数组
        for (int i = 0; i < size; i++) {
            largeArray[i] = random.nextInt(1000);
        }
        
        long startTime = System.currentTimeMillis();
        int result = Code04_KthLargestElementInArray.findKthLargest(largeArray, 100);
        long endTime = System.currentTimeMillis();
        
        System.out.println("数据规模: " + size);
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
        System.out.println("第100大的元素: " + result);
        System.out.println("性能测试完成");
    }
}