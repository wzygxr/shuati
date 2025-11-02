package class027;

import java.util.PriorityQueue;

/**
 * 相关题目3: LeetCode 295. 数据流的中位数
 * 题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
 * 题目描述: 中位数是有序整数列表中的中间值。如果列表大小是偶数，则没有中间值，中位数是两个中间值的平均值。
 * 解题思路: 使用两个堆，一个最大堆维护较小的一半，一个最小堆维护较大的一半
 * 时间复杂度: addNum: O(log n), findMedian: O(1)
 * 空间复杂度: O(n)
 * 是否最优解: 是，这是处理动态中位数的经典解法
 */
public class Code06_FindMedianFromDataStream {
    // 最大堆，存储较小的一半元素
    private PriorityQueue<Integer> maxHeap;
    // 最小堆，存储较大的一半元素
    private PriorityQueue<Integer> minHeap;
    
    public Code06_FindMedianFromDataStream() {
        maxHeap = new PriorityQueue<>((a, b) -> b - a); // 最大堆
        minHeap = new PriorityQueue<>(); // 最小堆
    }
    
    public void addNum(int num) {
        // 保证maxHeap的元素数量不少于minHeap
        if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
            maxHeap.offer(num);
        } else {
            minHeap.offer(num);
        }
        
        // 平衡两个堆的大小
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        } else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }
    
    public double findMedian() {
        if (maxHeap.size() == minHeap.size()) {
            // 偶数个元素，返回两堆顶的平均值
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        } else {
            // 奇数个元素，返回maxHeap的堆顶
            return maxHeap.peek();
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code06_FindMedianFromDataStream medianFinder = new Code06_FindMedianFromDataStream();
        
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        System.out.println("添加1,2后中位数: " + medianFinder.findMedian()); // 期望输出: 1.5
        
        medianFinder.addNum(3);
        System.out.println("添加3后中位数: " + medianFinder.findMedian()); // 期望输出: 2.0
    }
}