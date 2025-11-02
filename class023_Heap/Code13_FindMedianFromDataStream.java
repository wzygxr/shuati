package class027;

import java.util.PriorityQueue;

/**
 * 相关题目5: LeetCode 295. 数据流的中位数
 * 题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
 * 题目描述: 设计一个支持以下两种操作的数据结构：
 * 1. void addNum(int num) - 从数据流中添加一个整数到数据结构中
 * 2. double findMedian() - 返回目前所有元素的中位数
 * 解题思路: 使用两个堆，一个最大堆保存较小的一半元素，一个最小堆保存较大的一半元素
 * 时间复杂度: addNum() - O(log n)，findMedian() - O(1)
 * 空间复杂度: O(n)，其中n是添加的元素数量
 * 是否最优解: 是，这是解决数据流中位数问题的最优解法
 * 
 * 本题属于堆的典型应用场景：需要在动态数据中快速获取中间值
 */
public class Code13_FindMedianFromDataStream {
    
    // 最大堆，保存较小的一半元素（左半部分）
    private PriorityQueue<Integer> maxHeap;
    // 最小堆，保存较大的一半元素（右半部分）
    private PriorityQueue<Integer> minHeap;
    
    /**
     * 初始化数据结构
     */
    public Code13_FindMedianFromDataStream() {
        // 初始化最大堆，使用Lambda表达式定义比较器
        maxHeap = new PriorityQueue<>((a, b) -> b - a);
        // 初始化最小堆，使用默认的自然顺序（升序）
        minHeap = new PriorityQueue<>();
    }
    
    /**
     * 从数据流中添加一个整数到数据结构中
     * @param num 要添加的整数
     */
    public void addNum(int num) {
        // 策略：保持 maxHeap.size() >= minHeap.size() 不超过1个元素
        
        // 1. 首先将num添加到合适的堆中
        // 如果num小于等于maxHeap的最大值，添加到maxHeap；否则添加到minHeap
        if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
            maxHeap.offer(num);
        } else {
            minHeap.offer(num);
        }
        
        // 调试信息：打印添加元素后的堆状态
        // System.out.println("添加元素 " + num + " 后:");
        // System.out.println("maxHeap: " + maxHeap);
        // System.out.println("minHeap: " + minHeap);
        
        // 2. 重新平衡两个堆的大小，确保maxHeap.size() == minHeap.size() 或 maxHeap.size() == minHeap.size() + 1
        // 如果maxHeap的大小比minHeap大2或更多，将maxHeap的堆顶元素移动到minHeap
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        }
        // 如果minHeap的大小比maxHeap大，将minHeap的堆顶元素移动到maxHeap
        else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
        
        // 调试信息：打印重新平衡后的堆状态
        // System.out.println("重新平衡后:");
        // System.out.println("maxHeap: " + maxHeap);
        // System.out.println("minHeap: " + minHeap);
    }
    
    /**
     * 返回目前所有元素的中位数
     * @return 所有元素的中位数
     */
    public double findMedian() {
        // 如果总元素个数为奇数，中位数是maxHeap的堆顶元素
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        }
        // 如果总元素个数为偶数，中位数是两个堆顶元素的平均值
        else {
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        }
    }
    
    /**
     * 测试方法，验证算法在不同输入情况下的正确性
     */
    public static void main(String[] args) {
        // 测试用例1：基本操作
        Code13_FindMedianFromDataStream medianFinder = new Code13_FindMedianFromDataStream();
        medianFinder.addNum(1);    // 添加 1
        System.out.println("添加1后，中位数: " + medianFinder.findMedian()); // 返回 1.0
        
        medianFinder.addNum(2);    // 添加 2
        System.out.println("添加2后，中位数: " + medianFinder.findMedian()); // 返回 1.5
        
        medianFinder.addNum(3);    // 添加 3
        System.out.println("添加3后，中位数: " + medianFinder.findMedian()); // 返回 2.0
        
        // 测试用例2：逆序添加
        Code13_FindMedianFromDataStream medianFinder2 = new Code13_FindMedianFromDataStream();
        medianFinder2.addNum(5);
        medianFinder2.addNum(4);
        medianFinder2.addNum(3);
        medianFinder2.addNum(2);
        medianFinder2.addNum(1);
        System.out.println("逆序添加1-5后，中位数: " + medianFinder2.findMedian()); // 返回 3.0
        
        // 测试用例3：随机顺序添加
        Code13_FindMedianFromDataStream medianFinder3 = new Code13_FindMedianFromDataStream();
        medianFinder3.addNum(3);
        medianFinder3.addNum(1);
        medianFinder3.addNum(5);
        medianFinder3.addNum(2);
        System.out.println("随机添加3,1,5,2后，中位数: " + medianFinder3.findMedian()); // 返回 2.5
        medianFinder3.addNum(4);
        System.out.println("再添加4后，中位数: " + medianFinder3.findMedian()); // 返回 3.0
    }
}