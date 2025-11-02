package class027;

import java.util.PriorityQueue;

/**
 * 相关题目1: LeetCode 215. 数组中的第K个最大元素
 * 题目链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 * 题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 * 解题思路: 使用大小为k的最小堆维护前k个最大元素，遍历数组时保持堆的大小不超过k
 * 时间复杂度: O(n log k)，其中n是数组长度，每个元素最多入堆出堆一次，每次堆操作复杂度为log k
 * 空间复杂度: O(k)，堆的大小始终保持为k
 * 是否最优解: 是，这是处理动态第K大元素的经典解法，虽然理论上可以用快速选择算法达到O(n)的平均时间复杂度，但堆解法在数据流场景更有优势
 * 
 * 本题属于Top K问题的典型应用，堆算法是解决此类问题的最优选择之一
 */
public class Code04_KthLargestElementInArray {
    
    /**
     * 查找数组中第K个最大元素
     * @param nums 输入整数数组
     * @param k 第K大的元素的位置（从1开始计数）
     * @return 第K大的元素值
     * @throws IllegalArgumentException 当输入参数无效时抛出异常
     */
    public static int findKthLargest(int[] nums, int k) {
        // 异常处理：检查输入数组是否为空
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("输入数组不能为空");
        }
        
        // 异常处理：检查k是否在有效范围内
        if (k <= 0 || k > nums.length) {
            throw new IllegalArgumentException("k的值必须在1到数组长度之间");
        }
        
        // 使用最小堆维护前k个最大元素
        // Java中的PriorityQueue默认是最小堆
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        // 遍历数组中的每个元素
        for (int num : nums) {
            // 调试信息：打印当前处理的元素和堆的状态
            // System.out.println("Processing: " + num + ", Heap: " + minHeap);
            
            if (minHeap.size() < k) {
                // 如果堆的大小小于k，直接将当前元素加入堆
                minHeap.offer(num);
            } else if (num > minHeap.peek()) {
                // 如果堆的大小已达到k，且当前元素大于堆顶元素
                // 则移除堆顶元素（当前k个元素中最小的），并加入新元素
                minHeap.poll();
                minHeap.offer(num);
            }
            // 否则（当前元素小于等于堆顶元素），不做任何操作
        }
        
        // 此时堆顶元素就是第k个最大元素
        return minHeap.peek();
    }
    
    /**
     * 测试方法，验证算法在不同输入情况下的正确性
     */
    public static void main(String[] args) {
        // 测试用例1：普通情况
        int[] nums1 = {3, 2, 1, 5, 6, 4};
        int k1 = 2;
        System.out.println("示例1输出: " + findKthLargest(nums1, k1)); // 期望输出: 5
        
        // 测试用例2：包含重复元素
        int[] nums2 = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int k2 = 4;
        System.out.println("示例2输出: " + findKthLargest(nums2, k2)); // 期望输出: 4
        
        // 测试用例3：边界情况 - k等于数组长度
        int[] nums3 = {3, 2, 1};
        int k3 = 3;
        System.out.println("示例3输出: " + findKthLargest(nums3, k3)); // 期望输出: 1
        
        // 测试用例4：边界情况 - k等于1
        int[] nums4 = {3, 2, 1};
        int k4 = 1;
        System.out.println("示例4输出: " + findKthLargest(nums4, k4)); // 期望输出: 3
        
        // 注意：以下测试用例会抛出异常
        /*
        try {
            int[] nums5 = null;
            findKthLargest(nums5, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
        
        try {
            int[] nums6 = {1, 2, 3};
            findKthLargest(nums6, 4);
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
        */
    }
}