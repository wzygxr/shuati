import java.util.PriorityQueue;

/**
 * 相关题目24: LeetCode 295. 数据流的中位数
 * 题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
 * 题目描述: 设计一个支持以下两种操作的数据结构：
 * - void addNum(int num) - 从数据流中添加一个整数到数据结构中
 * - double findMedian() - 返回目前所有元素的中位数
 * 解题思路: 使用两个堆（最大堆和最小堆）维护数据流的中位数
 * 时间复杂度: addNum() O(log n)，findMedian() O(1)
 * 空间复杂度: O(n)
 * 是否最优解: 是，这种解法在时间和空间上都是最优的
 */
public class Code24_FindMedianFromDataStream {
    
    /**
     * MedianFinder类：使用两个堆实现的数据流中位数查找器
     */
    static class MedianFinder {
        // 最大堆存储较小的一半元素
        private PriorityQueue<Integer> maxHeap; // 存储较小的一半元素
        // 最小堆存储较大的一半元素
        private PriorityQueue<Integer> minHeap; // 存储较大的一半元素
        
        /**
         * 初始化数据结构
         * 使用最大堆存储较小的一半元素，最小堆存储较大的一半元素
         * 确保最大堆的大小等于或比最小堆大1
         */
        public MedianFinder() {
            // 创建最大堆（默认是最小堆，需要自定义比较器）
            maxHeap = new PriorityQueue<>((a, b) -> b - a);
            // 创建最小堆
            minHeap = new PriorityQueue<>();
        }
        
        /**
         * 向数据结构中添加一个整数
         * 
         * @param num 要添加的整数
         */
        public void addNum(int num) {
            // 首先将元素添加到最大堆中
            maxHeap.offer(num);
            
            // 确保最大堆顶元素（较小一半中的最大值）不大于最小堆顶元素（较大一半中的最小值）
            if (!minHeap.isEmpty() && maxHeap.peek() > minHeap.peek()) {
                // 将最大堆顶元素移动到最小堆
                minHeap.offer(maxHeap.poll());
            }
            
            // 平衡两个堆的大小，确保最大堆的大小等于或比最小堆大1
            // 如果最大堆比最小堆大超过1，则移动一个元素到最小堆
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.offer(maxHeap.poll());
            }
            // 如果最小堆比最大堆大，则移动一个元素到最大堆
            else if (minHeap.size() > maxHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
        }
        
        /**
         * 返回目前所有元素的中位数
         * 
         * @return 当前所有元素的中位数
         * @throws IllegalStateException 当没有元素时抛出异常
         */
        public double findMedian() {
            // 如果没有元素，抛出异常
            if (maxHeap.isEmpty() && minHeap.isEmpty()) {
                throw new IllegalStateException("没有元素，无法计算中位数");
            }
            
            // 如果最大堆的大小比最小堆大1，则中位数是最大堆的堆顶元素
            if (maxHeap.size() > minHeap.size()) {
                return maxHeap.peek();
            }
            // 否则，中位数是两个堆顶元素的平均值
            else {
                return (maxHeap.peek() + minHeap.peek()) / 2.0;
            }
        }
    }
    
    /**
     * AlternativeApproach类：使用更简洁的方式实现两个堆的平衡
     */
    static class AlternativeApproach {
        private PriorityQueue<Integer> small; // 最大堆（存储较小的一半元素）
        private PriorityQueue<Integer> large; // 最小堆（存储较大的一半元素）
        
        /**
         * 初始化数据结构
         */
        public AlternativeApproach() {
            small = new PriorityQueue<>((a, b) -> b - a); // 最大堆
            large = new PriorityQueue<>(); // 最小堆
        }
        
        /**
         * 更简洁的添加元素实现
         * 
         * @param num 要添加的整数
         */
        public void addNum(int num) {
            // 先添加到small堆，然后将small堆的最大值移到large堆
            small.offer(num);
            
            // 确保small堆的最大值不大于large堆的最小值
            if (!small.isEmpty() && !large.isEmpty() && small.peek() > large.peek()) {
                large.offer(small.poll());
            }
            
            // 平衡两个堆的大小
            if (small.size() > large.size() + 1) {
                large.offer(small.poll());
            }
            if (large.size() > small.size()) {
                small.offer(large.poll());
            }
        }
        
        /**
         * 返回中位数
         * 
         * @return 当前所有元素的中位数
         */
        public double findMedian() {
            if (small.size() > large.size()) {
                return small.peek();
            }
            return (small.peek() + large.peek()) / 2.0;
        }
    }
    
    /**
     * 测试函数，验证算法在不同输入情况下的正确性
     */
    public static void testMedianFinder() {
        System.out.println("=== 测试数据流的中位数算法 ===");
        
        // 测试基本实现
        System.out.println("\n=== 测试基本实现 ===");
        MedianFinder medianFinder = new MedianFinder();
        
        // 测试用例1：添加元素并计算中位数
        System.out.println("测试用例1：添加元素并计算中位数");
        int[] nums1 = {1, 2, 3, 4, 5, 6};
        double[] expectedResults = {1.0, 1.5, 2.0, 2.5, 3.0, 3.5};
        
        for (int i = 0; i < nums1.length; i++) {
            medianFinder.addNum(nums1[i]);
            double median = medianFinder.findMedian();
            System.out.println("当前中位数: " + median);
            
            if (Math.abs(median - expectedResults[i]) > 1e-9) {
                System.out.println("测试用例1 第" + (i+1) + "步失败: 期望" + expectedResults[i] + ", 实际" + median);
            }
        }
        System.out.println("测试用例1 完成 ✓");
        
        // 测试用例2：负数和零
        System.out.println("\n测试用例2：负数和零");
        MedianFinder medianFinder2 = new MedianFinder();
        medianFinder2.addNum(-1);
        medianFinder2.addNum(0);
        medianFinder2.addNum(-2);
        
        double result2 = medianFinder2.findMedian();
        double expected2 = -1.0;
        System.out.println("当前中位数: " + result2 + ", 期望: " + expected2 + ", " + 
                          (Math.abs(result2 - expected2) < 1e-9 ? "✓" : "✗"));
        
        // 测试用例3：重复元素
        System.out.println("\n测试用例3：重复元素");
        MedianFinder medianFinder3 = new MedianFinder();
        for (int i = 0; i < 5; i++) {
            medianFinder3.addNum(2);
        }
        
        double result3 = medianFinder3.findMedian();
        double expected3 = 2.0;
        System.out.println("当前中位数: " + result3 + ", 期望: " + expected3 + ", " + 
                          (Math.abs(result3 - expected3) < 1e-9 ? "✓" : "✗"));
        
        // 测试异常情况
        System.out.println("\n=== 测试异常情况 ===");
        MedianFinder medianFinder4 = new MedianFinder();
        try {
            medianFinder4.findMedian();
            System.out.println("异常测试失败：未抛出预期的异常");
        } catch (IllegalStateException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
        
        // 测试替代实现
        System.out.println("\n=== 测试替代实现 ===");
        AlternativeApproach altFinder = new AlternativeApproach();
        
        for (int num : new int[]{1, 2, 3, 4, 5}) {
            altFinder.addNum(num);
        }
        
        double resultAlt = altFinder.findMedian();
        double expectedAlt = 3.0;
        System.out.println("替代实现中位数: " + resultAlt + ", 期望: " + expectedAlt + ", " + 
                          (Math.abs(resultAlt - expectedAlt) < 1e-9 ? "✓" : "✗"));
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        
        // 测试大规模输入
        MedianFinder largeFinder = new MedianFinder();
        int n = 100000;
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            largeFinder.addNum(i);
        }
        double median = largeFinder.findMedian();
        long endTime = System.currentTimeMillis();
        
        System.out.println("添加" + n + "个元素后中位数: " + median);
        System.out.println("总耗时: " + (endTime - startTime) + "毫秒");
        System.out.println("平均每个操作耗时: " + (double)(endTime - startTime) / n * 1000 + "微秒");
    }
    
    // 主方法
    public static void main(String[] args) {
        testMedianFinder();
    }
    
    /*
     * 解题思路总结：
     * 1. 双堆方法：
     *    - 使用最大堆存储较小的一半元素，最小堆存储较大的一半元素
     *    - 维护两个堆的大小关系，确保最大堆的大小等于或比最小堆大1
     *    - 这样，如果元素总数是奇数，中位数就是最大堆的堆顶；如果是偶数，中位数是两个堆顶的平均值
     *    - 时间复杂度：addNum() O(log n)，findMedian() O(1)
     *    - 空间复杂度：O(n)
     * 
     * 2. 优化技巧：
     *    - 在Java中，使用PriorityQueue作为堆的实现，需要为最大堆提供自定义比较器
     *    - 注意添加元素后的平衡调整步骤，确保两个堆的大小关系和元素有序性
     *    - 使用更简洁的实现方式可以减少代码行数，但核心逻辑保持不变
     * 
     * 3. 应用场景：
     *    - 当需要频繁地从动态变化的数据集中获取中位数时，双堆方法是一个很好的选择
     *    - 这种方法在金融数据分析、实时统计等场景中非常有用
     * 
     * 4. 边界情况处理：
     *    - 空数据集时返回适当的错误
     *    - 处理负数和零的情况
     *    - 处理重复元素的情况
     * 
     * 5. Java实现注意事项：
     *    - 使用Integer类型可以处理较大范围的整数
     *    - 使用Math.abs和误差范围比较浮点数的相等性
     *    - 使用System.currentTimeMillis()进行性能测量
     */
}