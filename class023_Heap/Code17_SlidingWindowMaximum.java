package class027;

import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 * 相关题目9: LeetCode 239. 滑动窗口最大值
 * 题目链接: https://leetcode.cn/problems/sliding-window-maximum/
 * 题目描述: 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。返回滑动窗口中的最大值。
 * 解题思路: 使用优先队列（最大堆）维护当前窗口内的元素，堆顶始终是最大值
 * 时间复杂度: O(n log k)，每个元素入堆和出堆的时间复杂度为O(log k)
 * 空间复杂度: O(k)，堆中最多存储k个元素
 * 是否最优解: 不是最优解，最优解是使用单调队列，时间复杂度为O(n)，但这里使用堆作为实现方案
 * 
 * 本题属于堆的典型应用场景：需要在滑动窗口中快速获取最大值
 */
public class Code17_SlidingWindowMaximum {
    
    /**
     * 使用最大堆解决滑动窗口最大值问题
     * @param nums 输入的整数数组
     * @param k 滑动窗口的大小
     * @return 每个滑动窗口的最大值组成的数组
     * @throws IllegalArgumentException 当输入参数无效时抛出异常
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        // 异常处理：检查输入数组是否为null或空
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("输入数组不能为空");
        }
        
        // 异常处理：检查k是否在有效范围内
        if (k <= 0 || k > nums.length) {
            throw new IllegalArgumentException("k的值必须在1到数组长度之间");
        }
        
        int n = nums.length;
        // 结果数组的长度是n - k + 1
        int[] result = new int[n - k + 1];
        
        // 使用优先队列实现最大堆，存储(值, 索引)的数组，按值降序排列
        // 如果值相同，则按索引降序排列（这样可以确保删除的是最左边的重复最大值）
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) {
                return b[0] - a[0]; // 值降序
            } else {
                return b[1] - a[1]; // 索引降序
            }
        });
        
        // 首先将前k个元素加入堆
        for (int i = 0; i < k; i++) {
            maxHeap.offer(new int[]{nums[i], i});
        }
        
        // 第一个窗口的最大值
        result[0] = maxHeap.peek()[0];
        
        // 滑动窗口从k开始，逐个处理剩余元素
        for (int i = k; i < n; i++) {
            // 将当前元素加入堆
            maxHeap.offer(new int[]{nums[i], i});
            
            // 移除堆中不在当前窗口范围内的元素（索引小于i - k + 1的元素）
            while (maxHeap.peek()[1] < i - k + 1) {
                maxHeap.poll();
            }
            
            // 当前堆顶就是当前窗口的最大值
            result[i - k + 1] = maxHeap.peek()[0];
        }
        
        return result;
    }
    
    /**
     * 使用TreeSet解决滑动窗口最大值问题（另一种实现方式）
     * TreeSet在Java中可以实现类似平衡二叉搜索树的功能，也可以用于解决此类问题
     * @param nums 输入的整数数组
     * @param k 滑动窗口的大小
     * @return 每个滑动窗口的最大值组成的数组
     */
    public static int[] maxSlidingWindowWithTreeSet(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n - k + 1];
        
        // 使用TreeSet实现最大堆，存储(值, 索引)的字符串，按值降序，索引降序排列
        // 注意：使用TreeSet时，如果直接存储整数，相同值会被去重，所以需要结合索引
        // 这里使用格式为"-值:索引"的字符串，这样可以按字典序降序排列
        TreeSet<String> treeSet = new TreeSet<>();
        
        // 首先将前k个元素加入TreeSet
        for (int i = 0; i < k; i++) {
            treeSet.add(String.format("%09d:%d", -nums[i], i)); // 使用9位确保正确排序
        }
        
        // 第一个窗口的最大值
        String firstMax = treeSet.first();
        result[0] = -Integer.parseInt(firstMax.split(":")[0]);
        
        // 滑动窗口
        for (int i = k; i < n; i++) {
            // 移除窗口左边的元素（不在当前窗口范围内的元素）
            String leftElement = String.format("%09d:%d", -nums[i - k], i - k);
            treeSet.remove(leftElement);
            
            // 添加当前元素
            treeSet.add(String.format("%09d:%d", -nums[i], i));
            
            // 当前最大值
            String currentMax = treeSet.first();
            result[i - k + 1] = -Integer.parseInt(currentMax.split(":")[0]);
        }
        
        return result;
    }
    
    /**
     * 打印数组的辅助方法
     */
    public static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    
    /**
     * 测试方法，验证算法在不同输入情况下的正确性
     */
    public static void main(String[] args) {
        // 测试用例1：基本情况
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        System.out.print("示例1输出: ");
        int[] result1 = maxSlidingWindow(nums1, k1);
        printArray(result1); // 期望输出: [3, 3, 5, 5, 6, 7]
        
        // 测试用例2：k=1，只有一个元素的窗口
        int[] nums2 = {1, -1};
        int k2 = 1;
        System.out.print("示例2输出: ");
        int[] result2 = maxSlidingWindow(nums2, k2);
        printArray(result2); // 期望输出: [1, -1]
        
        // 测试用例3：k等于数组长度，整个数组为一个窗口
        int[] nums3 = {9, 10, 9, -7, -4, -8, 2, -6};
        int k3 = 5;
        System.out.print("示例3输出: ");
        int[] result3 = maxSlidingWindow(nums3, k3);
        printArray(result3); // 期望输出: [10, 10, 9, 2]
        
        // 测试用例4：边界情况 - 数组只有一个元素
        int[] nums4 = {1};
        int k4 = 1;
        System.out.print("示例4输出: ");
        int[] result4 = maxSlidingWindow(nums4, k4);
        printArray(result4); // 期望输出: [1]
        
        // 测试TreeSet实现
        System.out.print("TreeSet实现示例1输出: ");
        int[] result1WithTreeSet = maxSlidingWindowWithTreeSet(nums1, k1);
        printArray(result1WithTreeSet);
    }
}