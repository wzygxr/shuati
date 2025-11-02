package class027;

import java.util.*;

/**
 * 相关题目13: LeetCode 703. 数据流中的第 K 大元素
 * 题目链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
 * 题目描述: 设计一个找到数据流中第 k 大元素的类（class）。注意是排序后的第 k 大元素，不是第 k 个不同的元素。
 * 实现 KthLargest 类:
 * KthLargest(int k, int[] nums) 使用整数 k 和整数流 nums 初始化对象
 * int add(int val) 将 val 插入数据流 nums 后，返回当前数据流中第 k 大的元素
 * 解题思路: 使用最小堆维护数据流中最大的k个元素，堆顶即为第k大元素
 * 时间复杂度: add() O(log k)，初始化 O(n log k)
 * 空间复杂度: O(k)，堆中最多存储k个元素
 * 是否最优解: 是，这是求解数据流中第K大元素的最优解法之一
 * 
 * 本题属于堆的典型应用场景：需要在动态数据流中维护前K个最大值
 */
public class Code21_KthLargestElementInAStream {
    
    /**
     * KthLargest类：支持在数据流中查找第K大元素
     */
    public static class KthLargest {
        // 最小堆，用于存储最大的k个元素
        private PriorityQueue<Integer> minHeap; 
        // 第K大元素的K值
        private int k; 
        
        /**
         * 初始化KthLargest类
         * @param k 第K大元素的K值
         * @param nums 初始数据流数组
         * @throws IllegalArgumentException 当k或nums参数无效时抛出异常
         */
        public KthLargest(int k, int[] nums) {
            // 异常处理：检查k是否有效
            if (k <= 0) {
                throw new IllegalArgumentException("k的值必须大于0");
            }
            
            // 异常处理：检查nums是否为null
            if (nums == null) {
                throw new IllegalArgumentException("输入数组不能为null");
            }
            
            this.k = k;
            // 创建最小堆，最多存储k个元素
            this.minHeap = new PriorityQueue<>(k);
            
            // 将初始数组中的元素添加到堆中
            for (int num : nums) {
                add(num);
            }
        }
        
        /**
         * 将val插入数据流nums后，返回当前数据流中第k大的元素
         * @param val 要插入的整数
         * @return 当前数据流中第k大的元素
         */
        public int add(int val) {
            // 如果堆的大小小于k，直接添加元素到堆中
            if (minHeap.size() < k) {
                minHeap.offer(val);
            } else if (val > minHeap.peek()) {
                // 如果当前元素大于堆顶元素（第k大元素），则移除堆顶元素，添加当前元素
                minHeap.poll();
                minHeap.offer(val);
            }
            // 否则，不做任何操作，因为当前元素小于第k大元素，不会影响结果
            
            // 如果堆中不足k个元素，返回Integer.MIN_VALUE表示没有第k大元素
            // 但根据题目描述，初始化时nums可能为空，所以这种情况是允许的
            return minHeap.isEmpty() ? Integer.MIN_VALUE : minHeap.peek();
        }
        
        /**
         * 获取当前堆的大小
         * @return 堆的大小
         */
        public int getHeapSize() {
            return minHeap.size();
        }
    }
    
    /**
     * 测试函数，验证算法在不同输入情况下的正确性
     */
    public static void main(String[] args) {
        // 测试用例1：基本操作
        System.out.println("测试用例1：");
        int k1 = 3;
        int[] nums1 = {4, 5, 8, 2};
        KthLargest kthLargest1 = new KthLargest(k1, nums1);
        
        System.out.println("添加3后，第3大的元素 = " + kthLargest1.add(3));   // 期望输出: 4
        System.out.println("添加5后，第3大的元素 = " + kthLargest1.add(5));   // 期望输出: 5
        System.out.println("添加10后，第3大的元素 = " + kthLargest1.add(10)); // 期望输出: 5
        System.out.println("添加9后，第3大的元素 = " + kthLargest1.add(9));   // 期望输出: 8
        System.out.println("添加4后，第3大的元素 = " + kthLargest1.add(4));   // 期望输出: 8
        
        // 测试用例2：初始数组为空
        System.out.println("\n测试用例2：");
        int k2 = 1;
        int[] nums2 = {};
        KthLargest kthLargest2 = new KthLargest(k2, nums2);
        
        System.out.println("添加-3后，第1大的元素 = " + kthLargest2.add(-3)); // 期望输出: -3
        System.out.println("添加-2后，第1大的元素 = " + kthLargest2.add(-2)); // 期望输出: -2
        System.out.println("添加-4后，第1大的元素 = " + kthLargest2.add(-4)); // 期望输出: -2
        System.out.println("添加0后，第1大的元素 = " + kthLargest2.add(0));   // 期望输出: 0
        System.out.println("添加4后，第1大的元素 = " + kthLargest2.add(4));   // 期望输出: 4
        
        // 测试用例3：初始数组长度大于k
        System.out.println("\n测试用例3：");
        int k3 = 2;
        int[] nums3 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        KthLargest kthLargest3 = new KthLargest(k3, nums3);
        
        System.out.println("初始第2大的元素 = " + kthLargest3.add(-1)); // 期望输出: 8
        System.out.println("添加10后，第2大的元素 = " + kthLargest3.add(10)); // 期望输出: 9
        
        // 测试异常情况
        System.out.println("\n测试异常情况：");
        try {
            KthLargest invalidK = new KthLargest(0, new int[]{1, 2, 3});
            System.out.println("异常测试失败：未抛出预期的异常");
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
    }
}