package class027;

import java.util.PriorityQueue;

/**
 * 相关题目5: LeetCode 703. 数据流的第K大元素
 * 题目链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
 * 题目描述: 设计一个找到数据流中第 k 大元素的类
 * 解题思路: 使用大小为k的最小堆维护数据流中前k个最大元素
 * 时间复杂度: 初始化: O(n log k), 添加元素: O(log k)
 * 空间复杂度: O(k)
 * 是否最优解: 是，这是处理动态第K大元素的经典解法
 */
public class Code08_KthLargestElementInStream {
    private int k;
    private PriorityQueue<Integer> minHeap;
    
    public Code08_KthLargestElementInStream(int k, int[] nums) {
        this.k = k;
        // 使用最小堆维护前k个最大元素
        this.minHeap = new PriorityQueue<>();
        
        // 将初始数组中的元素加入堆中
        for (int num : nums) {
            add(num);
        }
    }
    
    public int add(int val) {
        if (minHeap.size() < k) {
            minHeap.offer(val);
        } else if (val > minHeap.peek()) {
            minHeap.poll();
            minHeap.offer(val);
        }
        return minHeap.peek();
    }
    
    // 测试方法
    public static void main(String[] args) {
        int k = 3;
        int[] nums = {4, 5, 8, 2};
        Code08_KthLargestElementInStream kthLargest = new Code08_KthLargestElementInStream(k, nums);
        
        System.out.println("添加3后第3大的元素: " + kthLargest.add(3)); // 期望输出: 4
        System.out.println("添加5后第3大的元素: " + kthLargest.add(5)); // 期望输出: 5
        System.out.println("添加10后第3大的元素: " + kthLargest.add(10)); // 期望输出: 5
        System.out.println("添加9后第3大的元素: " + kthLargest.add(9)); // 期望输出: 8
        System.out.println("添加4后第3大的元素: " + kthLargest.add(4)); // 期望输出: 8
    }
}