package class027;

import java.util.*;

/**
 * 相关题目12: LeetCode 295. 数据流的中位数
 * 题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
 * 题目描述: 设计一个支持以下两种操作的数据结构：
 * 1. void addNum(int num) - 从数据流中添加一个整数到数据结构中
 * 2. double findMedian() - 返回目前所有元素的中位数
 * 解题思路: 使用两个堆维护数据流：最大堆存储较小的一半元素，最小堆存储较大的一半元素
 * 时间复杂度: addNum() O(log n)，findMedian() O(1)
 * 空间复杂度: O(n)，其中n是数据流中的元素个数
 * 是否最优解: 是，这是求解数据流中位数的最优解法之一
 * 
 * 本题属于堆的典型应用场景：需要动态维护数据的中间值
 */
public class Code20_FindMedianFromDataStream {
    
    /**
     * MedianFinder类：支持添加元素和查找中位数的数据结构
     */
    public static class MedianFinder {
        // 最大堆存储较小的一半元素
        private PriorityQueue<Integer> maxHeap; 
        // 最小堆存储较大的一半元素
        private PriorityQueue<Integer> minHeap; 
        
        /**
         * 初始化数据结构
         */
        public MedianFinder() {
            // 创建最大堆，使用lambda表达式自定义比较器
            maxHeap = new PriorityQueue<>((a, b) -> b - a);
            // 创建最小堆，默认就是最小堆，也可以显式指定比较器
            minHeap = new PriorityQueue<>((a, b) -> a - b);
        }
        
        /**
         * 从数据流中添加一个整数到数据结构中
         * @param num 要添加的整数
         */
        public void addNum(int num) {
            // 策略：保持两个堆的平衡，使maxHeap的大小等于minHeap或比minHeap大1
            
            // 先将num加入到maxHeap中
            maxHeap.offer(num);
            
            // 然后将maxHeap的最大值转移到minHeap中，确保maxHeap中的所有元素都小于或等于minHeap中的所有元素
            minHeap.offer(maxHeap.poll());
            
            // 如果minHeap的大小超过maxHeap，则将minHeap的最小值转移到maxHeap中
            // 这样可以保证maxHeap的大小等于minHeap或比minHeap大1
            if (minHeap.size() > maxHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
        }
        
        /**
         * 返回目前所有元素的中位数
         * @return 中位数
         * @throws IllegalStateException 当没有元素时抛出异常
         */
        public double findMedian() {
            // 异常处理：当没有元素时抛出异常
            if (maxHeap.isEmpty()) {
                throw new IllegalStateException("没有元素，无法计算中位数");
            }
            
            // 如果maxHeap的大小大于minHeap，说明总共有奇数个元素，中位数就是maxHeap的堆顶
            if (maxHeap.size() > minHeap.size()) {
                return maxHeap.peek();
            } else {
                // 如果maxHeap和minHeap的大小相等，说明总共有偶数个元素，中位数是两个堆顶的平均值
                return (maxHeap.peek() + minHeap.peek()) / 2.0;
            }
        }
        
        /**
         * 获取当前存储的元素数量
         * @return 元素数量
         */
        public int size() {
            return maxHeap.size() + minHeap.size();
        }
    }
    
    /**
     * 测试函数，验证算法在不同输入情况下的正确性
     */
    public static void main(String[] args) {
        // 测试用例1：基本操作
        System.out.println("测试用例1：");
        MedianFinder medianFinder1 = new MedianFinder();
        
        // 添加元素并打印中位数
        medianFinder1.addNum(1);
        System.out.println("添加1后，中位数 = " + medianFinder1.findMedian()); // 期望输出: 1.0
        
        medianFinder1.addNum(2);
        System.out.println("添加2后，中位数 = " + medianFinder1.findMedian()); // 期望输出: 1.5
        
        medianFinder1.addNum(3);
        System.out.println("添加3后，中位数 = " + medianFinder1.findMedian()); // 期望输出: 2.0
        
        medianFinder1.addNum(4);
        System.out.println("添加4后，中位数 = " + medianFinder1.findMedian()); // 期望输出: 2.5
        
        medianFinder1.addNum(5);
        System.out.println("添加5后，中位数 = " + medianFinder1.findMedian()); // 期望输出: 3.0
        
        // 测试用例2：无序输入
        System.out.println("\n测试用例2：");
        MedianFinder medianFinder2 = new MedianFinder();
        int[] nums = {5, 2, 8, 4, 1, 9, 3, 6, 7};
        
        for (int num : nums) {
            medianFinder2.addNum(num);
            System.out.println("添加" + num + "后，中位数 = " + medianFinder2.findMedian());
        }
        
        // 测试用例3：负数和零
        System.out.println("\n测试用例3：");
        MedianFinder medianFinder3 = new MedianFinder();
        int[] numsWithNegatives = {-1, 0, 5, -10, 2, 7};
        
        for (int num : numsWithNegatives) {
            medianFinder3.addNum(num);
            System.out.println("添加" + num + "后，中位数 = " + medianFinder3.findMedian());
        }
        
        // 测试异常情况
        System.out.println("\n测试异常情况：");
        MedianFinder emptyMedianFinder = new MedianFinder();
        try {
            emptyMedianFinder.findMedian();
            System.out.println("异常测试失败：未抛出预期的异常");
        } catch (IllegalStateException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
    }
}