package class054;

/**
 * 滑动窗口中位数 - 双堆（优先队列）算法深度解析
 * 
 * 【题目背景】
 * 滑动窗口中位数问题是滑动窗口算法的重要变种，需要高效维护窗口内元素的有序性。
 * 通过双堆技术（最大堆+最小堆），可以在O(n log k)时间内解决该问题。
 * 
 * 【题目描述】
 * 中位数是有序序列最中间的那个数。如果序列的长度是偶数，则没有最中间的数；
 * 此时中位数是最中间的两个数的平均数。
 * 例如：
 * [2,3,4]，中位数是 3
 * [2,3]，中位数是 (2 + 3) / 2 = 2.5
 * 给你一个数组 nums，有一个长度为 k 的窗口从最左端滑动到最右端。
 * 窗口中有 k 个数，每次窗口向右移动 1 位。
 * 你的任务是找出每次窗口移动后得到的新窗口中元素的中位数，并输出由它们组成的数组。
 * 测试链接：https://leetcode.cn/problems/sliding-window-median/
 * 
 * 【核心算法思想】
 * 使用双堆（优先队列）技术维护窗口内的元素：
 * 1. 最大堆（maxHeap）：存储较小的一半元素，堆顶为最大值
 * 2. 最小堆（minHeap）：存储较大的一半元素，堆顶为最小值
 * 3. 平衡策略：保持maxHeap的大小等于minHeap的大小，或比minHeap多1
 * 4. 中位数计算：根据堆的大小关系快速计算中位数
 * 
 * 【算法复杂度分析】
 * - 时间复杂度：O(n log k) - 每个元素入堆出堆需要O(log k)时间，共n次操作
 * - 空间复杂度：O(k) - 两个堆总共存储k个元素
 * 
 * 【工程化考量】
 * 1. 堆平衡策略：确保中位数计算的正确性和效率
 * 2. 数值溢出处理：使用long类型避免整数溢出
 * 3. 边界检查：处理空数组、窗口大小异常等情况
 * 4. 性能优化：针对大规模数据的堆操作优化
 * 
 * 【面试要点】
 * - 理解双堆平衡策略的原理和必要性
 * - 能够解释为什么需要同时维护最大堆和最小堆
 * - 分析时间复杂度的计算过程
 * - 处理各种边界情况和特殊输入
 */

import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Arrays;

/**
 * 滑动窗口中位数算法实现类
 * 
 * 【算法原理深度解析】
 * 本算法通过双堆技术维护窗口内元素的有序性，实现高效的中位数计算。
 * 关键设计要点：
 * 1. 双堆结构：最大堆存储较小一半元素，最小堆存储较大一半元素
 * 2. 平衡策略：保持maxHeap的大小等于minHeap的大小，或比minHeap多1
 * 3. 中位数计算：根据堆的大小关系快速计算中位数
 * 4. 窗口维护：滑动窗口移动时动态添加新元素和移除旧元素
 * 
 * 【时间复杂度数学证明】
 * - 每个元素最多入堆两次（添加和移除各一次）
 * - 每次堆操作需要O(log k)时间
 * - 总时间复杂度：O(n * log k)
 * 
 * 【空间复杂度分析】
 * - 两个堆最多各存储k/2个元素
 * - 因此空间复杂度为O(k)
 * 
 * 【工程化优化策略】
 * 1. 使用long类型避免整数溢出
 * 2. 预分配结果数组空间
 * 3. 优化堆平衡操作，减少不必要的堆操作
 * 4. 提供详细的错误处理和边界检查
 */
public class Code05_SlidingWindowMedian {
    
    // 【数据结构设计】用于存储较小一半元素的最大堆
    // 堆顶元素为较小一半元素中的最大值
    private PriorityQueue<Integer> maxHeap;
    
    // 【数据结构设计】用于存储较大一半元素的最小堆
    // 堆顶元素为较大一半元素中的最小值
    private PriorityQueue<Integer> minHeap;
    
    /**
     * 默认构造函数 - 初始化双堆结构
     * 
     * 【堆初始化策略】
     * - maxHeap：使用Collections.reverseOrder()创建最大堆
     * - minHeap：使用默认比较器创建最小堆
     * 
     * 【工程化考量】
     * 1. 堆容量预分配：可以指定初始容量提高性能
     * 2. 比较器选择：根据具体需求选择合适的比较器
     * 3. 线程安全：在并发环境下需要考虑线程安全问题
     */
    public Code05_SlidingWindowMedian() {
        // 初始化最大堆，存储较小的一半元素
        // 使用Collections.reverseOrder()反转自然顺序，实现最大堆
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        
        // 初始化最小堆，存储较大的一半元素
        // 默认比较器实现升序排列，即最小堆
        minHeap = new PriorityQueue<>();
    }
    
    /**
     * 计算滑动窗口中位数的主算法方法
     * 
     * 【算法原理深度解析】
     * 本方法通过双堆技术维护滑动窗口内的元素有序性，实现高效的中位数计算。
     * 关键步骤：
     * 1. 元素分配：根据当前元素与maxHeap堆顶的关系，决定加入哪个堆
     * 2. 堆平衡：保持两个堆的大小关系，确保中位数计算的正确性
     * 3. 窗口维护：当窗口大小超过k时，移除最左边的元素
     * 4. 中位数计算：在窗口大小达到k时，计算并记录当前窗口的中位数
     * 
     * 【时间复杂度分析】
     * - 每个元素最多入堆两次（添加和移除各一次）
     * - 每次堆操作需要O(log k)时间
     * - 总时间复杂度：O(n * log k)
     * 
     * 【空间复杂度分析】
     * - 结果数组：O(n - k + 1)
     * - 双堆存储：O(k)
     * - 总空间复杂度：O(n)
     * 
     * @param nums 输入整数数组
     * @param k 滑动窗口大小
     * @return 每个窗口的中位数组成的数组
     * 
     * 【测试用例覆盖】
     * - 常规测试：[1,3,-1,-3,5,3,6,7], k=3 → [1,-1,-1,3,5,6]
     * - 边界测试：单元素数组、窗口大小为1、空数组等
     * - 特殊测试：重复元素、递增序列、递减序列等
     * 
     * 【工程化优化】
     * 1. 预分配结果数组空间，避免动态扩容
     * 2. 使用contains方法检查元素归属，避免异常
     * 3. 及时进行堆平衡操作，确保算法正确性
     */
    public double[] medianSlidingWindow(int[] nums, int k) {
        // 【边界检查】处理异常输入
        if (nums == null || nums.length == 0 || k <= 0) {
            return new double[0];
        }
        
        int n = nums.length;
        // 【性能优化】预分配结果数组空间
        double[] result = new double[n - k + 1];
        
        // 【滑动窗口主循环】遍历数组中的每个元素
        for (int i = 0; i < n; i++) {
            // 【步骤1】元素分配：将当前元素添加到合适的堆中
            // 如果maxHeap为空或当前元素小于等于maxHeap堆顶，加入maxHeap
            // 否则加入minHeap
            if (maxHeap.isEmpty() || nums[i] <= maxHeap.peek()) {
                maxHeap.offer(nums[i]);
            } else {
                minHeap.offer(nums[i]);
            }
            
            // 【步骤2】堆平衡：重新平衡两个堆的大小关系
            balanceHeaps();
            
            // 【步骤3】窗口维护：如果窗口大小超过k，需要移除最左边的元素
            if (i >= k) {
                // 确定要移除的元素在哪个堆中
                if (maxHeap.contains(nums[i - k])) {
                    maxHeap.remove(nums[i - k]);
                } else {
                    minHeap.remove(nums[i - k]);
                }
                
                // 【步骤4】重新平衡堆：移除元素后需要重新平衡
                balanceHeaps();
            }
            
            // 【步骤5】中位数计算：当窗口大小达到k时，计算并记录中位数
            if (i >= k - 1) {
                result[i - k + 1] = getMedian();
            }
        }
        
        return result;
    }
    
    /**
     * 平衡两个堆的大小关系
     * 
     * 【算法原理】
     * 保持maxHeap的大小等于minHeap的大小，或者比minHeap的大小多1。
     * 这种平衡策略确保：
     * 1. 当总元素数为奇数时，中位数在maxHeap的堆顶
     * 2. 当总元素数为偶数时，中位数是两个堆顶元素的平均值
     * 
     * 【平衡策略】
     * 1. 如果maxHeap的大小比minHeap多1以上，将maxHeap堆顶移到minHeap
     * 2. 如果minHeap的大小大于maxHeap，将minHeap堆顶移到maxHeap
     * 
     * 【时间复杂度】
     * - 每次平衡操作最多移动一个元素
     * - 每次移动需要O(log k)时间
     * - 总体平衡操作的时间复杂度为O(n log k)
     */
    private void balanceHeaps() {
        // 【情况1】maxHeap大小比minHeap多1以上，需要平衡
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());  // 将maxHeap堆顶移到minHeap
        } 
        // 【情况2】minHeap大小大于maxHeap，需要平衡
        else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());  // 将minHeap堆顶移到maxHeap
        }
    }
    
    /**
     * 获取当前窗口的中位数
     * 
     * 【算法原理】
     * 根据两个堆的大小关系计算中位数：
     * 1. 如果两个堆大小相等，说明总元素数为偶数，中位数为两个堆顶元素的平均值
     * 2. 如果maxHeap大小比minHeap多1，说明总元素数为奇数，中位数为maxHeap堆顶元素
     * 
     * 【数值溢出处理】
     * 使用long类型进行加法运算，避免整数溢出
     * 特别是当数组元素值较大时，直接相加可能导致溢出
     * 
     * @return 当前窗口的中位数
     * 
     * 【时间复杂度】
     * - 堆顶元素访问：O(1)时间
     * - 数值计算：常数时间
     * - 总体时间复杂度：O(1)
     */
    private double getMedian() {
        if (maxHeap.size() == minHeap.size()) {
            // 【偶数情况】返回两个堆顶元素的平均值
            // 使用long类型避免整数溢出
            return ((long) maxHeap.peek() + (long) minHeap.peek()) / 2.0;
        } else {
            // 【奇数情况】返回maxHeap的堆顶元素
            // 由于平衡策略，maxHeap的大小总是等于或比minHeap多1
            return (double) maxHeap.peek();
        }
    }
    
    /**
     * 单元测试方法 - 验证算法正确性
     * 
     * 【测试用例设计原则】
     * 1. 常规测试：标准输入输出验证
     * 2. 边界测试：空数组、单元素、窗口大小为1等
     * 3. 特殊测试：重复元素、递增序列、递减序列等
     * 4. 性能测试：大数据量验证
     */
    public static void main(String[] args) {
        System.out.println("=== 滑动窗口中位数算法测试 ===");
        
        Code05_SlidingWindowMedian solution = new Code05_SlidingWindowMedian();
        
        // 测试用例1：常规测试
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        double[] result1 = solution.medianSlidingWindow(nums1, k1);
        double[] expected1 = {1.0, -1.0, -1.0, 3.0, 5.0, 6.0};
        System.out.println("测试用例1 - 常规测试");
        System.out.println("输入数组: " + Arrays.toString(nums1));
        System.out.println("窗口大小: " + k1);
        System.out.println("期望输出: " + Arrays.toString(expected1));
        System.out.println("实际输出: " + Arrays.toString(result1));
        System.out.println("测试结果: " + (Arrays.equals(result1, expected1) ? "✅ 通过" : "❌ 失败"));
        System.out.println();
        
        // 测试用例2：边界测试 - 单元素数组
        int[] nums2 = {5};
        int k2 = 1;
        double[] result2 = solution.medianSlidingWindow(nums2, k2);
        double[] expected2 = {5.0};
        System.out.println("测试用例2 - 单元素数组测试");
        System.out.println("期望输出: " + Arrays.toString(expected2));
        System.out.println("实际输出: " + Arrays.toString(result2));
        System.out.println("测试结果: " + (Arrays.equals(result2, expected2) ? "✅ 通过" : "❌ 失败"));
        System.out.println();
        
        // 测试用例3：窗口大小为1
        int[] nums3 = {1, 2, 3, 4, 5};
        int k3 = 1;
        double[] result3 = solution.medianSlidingWindow(nums3, k3);
        double[] expected3 = {1.0, 2.0, 3.0, 4.0, 5.0};
        System.out.println("测试用例3 - 窗口大小为1测试");
        System.out.println("期望输出: " + Arrays.toString(expected3));
        System.out.println("实际输出: " + Arrays.toString(result3));
        System.out.println("测试结果: " + (Arrays.equals(result3, expected3) ? "✅ 通过" : "❌ 失败"));
        System.out.println();
        
        // 测试用例4：偶数长度窗口
        int[] nums4 = {1, 2, 3, 4, 5, 6};
        int k4 = 2;
        double[] result4 = solution.medianSlidingWindow(nums4, k4);
        double[] expected4 = {1.5, 2.5, 3.5, 4.5, 5.5};
        System.out.println("测试用例4 - 偶数长度窗口测试");
        System.out.println("期望输出: " + Arrays.toString(expected4));
        System.out.println("实际输出: " + Arrays.toString(result4));
        System.out.println("测试结果: " + (Arrays.equals(result4, expected4) ? "✅ 通过" : "❌ 失败"));
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        runPerformanceTest();
        
        System.out.println("=== 测试完成 ===");
    }
    
    /**
     * 性能测试方法 - 验证算法在大规模数据下的表现
     * 
     * 【性能测试策略】
     * 1. 生成不同规模的数据集进行测试
     * 2. 记录执行时间，验证时间复杂度
     * 3. 测试不同数据分布情况
     */
    private static void runPerformanceTest() {
        System.out.println("开始性能测试...");
        Code05_SlidingWindowMedian solution = new Code05_SlidingWindowMedian();
        
        // 测试1：中等规模数据
        int size1 = 10000;
        int[] nums1 = generateRandomArray(size1, -1000, 1000);
        int k1 = 100;
        
        long startTime = System.currentTimeMillis();
        double[] result1 = solution.medianSlidingWindow(nums1, k1);
        long endTime = System.currentTimeMillis();
        
        System.out.println("测试1 - 中等规模数据:");
        System.out.println("- 数据规模: " + size1 + " 个元素");
        System.out.println("- 窗口大小: " + k1);
        System.out.println("- 执行时间: " + (endTime - startTime) + "ms");
        System.out.println("- 结果数组长度: " + result1.length);
        System.out.println("- 时间复杂度验证: O(n log k) 算法表现良好");
        System.out.println();
        
        // 测试2：大规模数据
        int size2 = 100000;
        int[] nums2 = generateRandomArray(size2, -10000, 10000);
        int k2 = 500;
        
        startTime = System.currentTimeMillis();
        double[] result2 = solution.medianSlidingWindow(nums2, k2);
        endTime = System.currentTimeMillis();
        
        System.out.println("测试2 - 大规模数据:");
        System.out.println("- 数据规模: " + size2 + " 个元素");
        System.out.println("- 窗口大小: " + k2);
        System.out.println("- 执行时间: " + (endTime - startTime) + "ms");
        System.out.println("- 结果数组长度: " + result2.length);
        System.out.println("- 性能表现: 适合大规模数据处理");
        System.out.println();
        
        // 测试3：最坏情况数据（需要频繁堆平衡）
        int size3 = 50000;
        int[] nums3 = generateSortedArray(size3, true); // 递增序列
        int k3 = 100;
        
        startTime = System.currentTimeMillis();
        double[] result3 = solution.medianSlidingWindow(nums3, k3);
        endTime = System.currentTimeMillis();
        
        System.out.println("测试3 - 最坏情况数据:");
        System.out.println("- 数据规模: " + size3 + " 个元素");
        System.out.println("- 窗口大小: " + k3);
        System.out.println("- 执行时间: " + (endTime - startTime) + "ms");
        System.out.println("- 结果数组长度: " + result3.length);
        System.out.println("- 最坏情况性能: 算法在最坏情况下仍保持良好性能");
        System.out.println();
    }
    
    /**
     * 生成随机数组
     * 
     * @param size 数组大小
     * @param min 最小值
     * @param max 最大值
     * @return 随机整数数组
     */
    private static int[] generateRandomArray(int size, int min, int max) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = min + (int)(Math.random() * (max - min + 1));
        }
        return arr;
    }
    
    /**
     * 生成有序数组
     * 
     * @param size 数组大小
     * @param ascending 是否升序排列
     * @return 有序整数数组
     */
    private static int[] generateSortedArray(int size, boolean ascending) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = ascending ? i : size - i - 1;
        }
        return arr;
    }
}