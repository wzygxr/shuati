package class027;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 相关题目4: LeetCode 239. 滑动窗口最大值
 * 题目链接: https://leetcode.cn/problems/sliding-window-maximum/
 * 题目描述: 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 解题思路: 使用双端队列维护窗口中的最大值
 * 时间复杂度: O(n)
 * 空间复杂度: O(k)
 * 是否最优解: 是，这是处理滑动窗口最大值的最优解法
 */
public class Code07_SlidingWindowMaximum {
    
    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        
        // 双端队列，存储数组索引，队首是当前窗口的最大值索引
        Deque<Integer> deque = new LinkedList<>();
        int[] result = new int[nums.length - k + 1];
        
        for (int i = 0; i < nums.length; i++) {
            // 移除队列中超出窗口范围的索引
            while (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }
            
            // 维护队列的单调性，移除所有小于当前元素的索引
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }
            
            // 将当前索引加入队列
            deque.offerLast(i);
            
            // 当窗口形成后，记录最大值
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        int[] result1 = maxSlidingWindow(nums1, k1);
        System.out.print("示例1输出: ");
        for (int num : result1) {
            System.out.print(num + " ");
        }
        System.out.println(); // 期望输出: 3 3 5 5 6 7
        
        int[] nums2 = {1};
        int k2 = 1;
        int[] result2 = maxSlidingWindow(nums2, k2);
        System.out.print("示例2输出: ");
        for (int num : result2) {
            System.out.print(num + " ");
        }
        System.out.println(); // 期望输出: 1
    }
}