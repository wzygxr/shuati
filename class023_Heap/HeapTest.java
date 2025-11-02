package class027;

import java.util.*;

/**
 * 堆算法综合测试类 - 包含所有测试实现
 */
public class HeapTest {
    
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
     * 测试最多线段重合问题
     */
    private static void testMaxCoverLines() {
        System.out.println("\n=== 测试1: 最多线段重合问题 ===");
        
        int[][] lines = {
            {1, 4}, {2, 5}, {3, 6}, {4, 7}
        };
        int expected = 3;
        
        int result = maxCoverLines(lines);
        System.out.println("线段数组: " + Arrays.deepToString(lines));
        System.out.println("期望结果: " + expected);
        System.out.println("实际结果: " + result);
        System.out.println("测试" + (result == expected ? "通过" : "失败"));
    }
    
    /**
     * 最多线段重合问题的实现
     */
    private static int maxCoverLines(int[][] lines) {
        if (lines == null || lines.length == 0) return 0;
        
        // 按线段起点排序
        Arrays.sort(lines, (a, b) -> a[0] - b[0]);
        
        // 最小堆维护当前覆盖点的线段右端点
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int maxCover = 0;
        
        for (int[] line : lines) {
            int start = line[0];
            int end = line[1];
            
            // 移除已经结束的线段
            while (!minHeap.isEmpty() && minHeap.peek() <= start) {
                minHeap.poll();
            }
            
            minHeap.offer(end);
            maxCover = Math.max(maxCover, minHeap.size());
        }
        
        return maxCover;
    }
    
    /**
     * 测试合并果子问题
     */
    private static void testMergeFruits() {
        System.out.println("\n=== 测试2: 合并果子问题 ===");
        
        int[] fruits = {1, 2, 9};
        int expected = 15;
        
        int result = mergeFruits(fruits);
        System.out.println("果子重量: " + Arrays.toString(fruits));
        System.out.println("期望结果: " + expected);
        System.out.println("实际结果: " + result);
        System.out.println("测试" + (result == expected ? "通过" : "失败"));
    }
    
    /**
     * 合并果子问题的实现
     */
    private static int mergeFruits(int[] fruits) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int fruit : fruits) {
            minHeap.offer(fruit);
        }
        
        int totalCost = 0;
        while (minHeap.size() > 1) {
            int first = minHeap.poll();
            int second = minHeap.poll();
            int cost = first + second;
            totalCost += cost;
            minHeap.offer(cost);
        }
        
        return totalCost;
    }
    
    /**
     * 运行中位数测试
     */
    private static void testRunningMedian() {
        System.out.println("\n=== 测试3: 运行中位数 ===");
        
        int[] arr = {1, 2, 3, 4, 5};
        double[] expected = {1.0, 1.5, 2.0, 2.5, 3.0};
        
        double[] result = findRunningMedian(arr);
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
     * 查找运行中位数的实现
     */
    private static double[] findRunningMedian(int[] arr) {
        if (arr == null || arr.length == 0) return new double[0];
        
        // 最大堆存储较小的一半
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        // 最小堆存储较大的一半
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        double[] result = new double[arr.length];
        
        for (int i = 0; i < arr.length; i++) {
            int num = arr[i];
            
            if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
                maxHeap.offer(num);
            } else {
                minHeap.offer(num);
            }
            
            // 平衡堆大小
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.offer(maxHeap.poll());
            } else if (minHeap.size() > maxHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
            
            // 计算中位数
            if (maxHeap.size() == minHeap.size()) {
                result[i] = (maxHeap.peek() + minHeap.peek()) / 2.0;
            } else {
                result[i] = maxHeap.peek();
            }
        }
        
        return result;
    }
    
    /**
     * 测试任务调度器
     */
    private static void testTaskScheduler() {
        System.out.println("\n=== 测试4: 任务调度器 ===");
        
        char[] tasks = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n = 2;
        int expected = 8;
        
        int result = taskScheduler(tasks, n);
        System.out.println("任务序列: " + Arrays.toString(tasks));
        System.out.println("冷却时间: " + n);
        System.out.println("期望结果: " + expected);
        System.out.println("实际结果: " + result);
        System.out.println("测试" + (result == expected ? "通过" : "失败"));
    }
    
    /**
     * 任务调度器的实现
     */
    private static int taskScheduler(char[] tasks, int n) {
        if (tasks == null || tasks.length == 0) return 0;
        
        // 统计任务频率
        int[] freq = new int[26];
        for (char task : tasks) {
            freq[task - 'A']++;
        }
        
        // 最大堆按频率排序
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        for (int f : freq) {
            if (f > 0) maxHeap.offer(f);
        }
        
        int time = 0;
        while (!maxHeap.isEmpty()) {
            List<Integer> temp = new ArrayList<>();
            
            // 执行n+1个时间单位
            for (int i = 0; i <= n; i++) {
                if (!maxHeap.isEmpty()) {
                    int count = maxHeap.poll();
                    if (count > 1) {
                        temp.add(count - 1);
                    }
                }
                time++;
                
                if (maxHeap.isEmpty() && temp.isEmpty()) {
                    break;
                }
            }
            
            // 将剩余任务重新加入堆
            for (int count : temp) {
                maxHeap.offer(count);
            }
        }
        
        return time;
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
        int result = findKthLargest(largeArray, 100);
        long endTime = System.currentTimeMillis();
        
        System.out.println("数据规模: " + size);
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
        System.out.println("第100大的元素: " + result);
        System.out.println("性能测试完成");
    }
    
    /**
     * 数组中的第K个最大元素的实现
     */
    private static int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            if (minHeap.size() < k) {
                minHeap.offer(num);
            } else if (num > minHeap.peek()) {
                minHeap.poll();
                minHeap.offer(num);
            }
        }
        return minHeap.peek();
    }
}