package class049;

import java.util.*;

/**
 * 480. 滑动窗口中位数问题解决方案
 * 
 * 问题描述：
 * 中位数是有序序列最中间的那个数。如果序列的长度是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。
 * 给你一个数组 nums，有一个长度为 k 的窗口从最左端滑动到最右端。窗口中有 k 个数，每次窗口向右移动 1 位。
 * 你的任务是找出每次窗口移动后得到的新窗口中元素的中位数，并输出由它们组成的数组。
 * 
 * 解题思路：
 * 使用两个堆（最大堆和最小堆）来维护滑动窗口的中位数
 * 最大堆存储窗口左半部分（较小的一半），最小堆存储窗口右半部分（较大的一半）
 * 保持两个堆的大小平衡，最大堆的大小等于最小堆的大小或比最小堆大1
 * 
 * 算法复杂度分析：
 * 时间复杂度：O(n*log k)，其中n是数组长度，k是窗口大小
 * 空间复杂度：O(k)，用于存储窗口内的元素
 * 
 * 是否最优解：是，这是处理滑动窗口中位数的最优解法
 * 
 * 相关题目链接：
 * LeetCode 480. 滑动窗口中位数
 * https://leetcode.cn/problems/sliding-window-median/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 滑动窗口中位数
 *    https://www.nowcoder.com/practice/1266570c4a06487981ed50e84e8b720d
 * 2. LintCode 480. 滑动窗口中位数
 *    https://www.lintcode.com/problem/480/
 * 3. HackerRank - Sliding Window Median
 *    https://www.hackerrank.com/challenges/sliding-window-median/problem
 * 4. CodeChef - MEDIAN - Window Median
 *    https://www.codechef.com/problems/MEDIAN
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1886 滑动窗口
 *    https://www.luogu.com.cn/problem/P1886
 * 7. 杭电OJ 4193 Sliding Window
 *    http://acm.hdu.edu.cn/showproblem.php?pid=4193
 * 8. POJ 2823 Sliding Window
 *    http://poj.org/problem?id=2823
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空数组、k为负数或0等边界情况
 * 2. 性能优化：使用双堆维护中位数，避免重复排序
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 */
public class Code18_SlidingWindowMedian {
    
    /**
     * 计算滑动窗口中位数
     * 
     * @param nums 输入数组
     * @param k 窗口大小
     * @return 每个窗口中位数的数组
     */
    public static double[] medianSlidingWindow(int[] nums, int k) {
        // 异常情况处理
        if (nums == null || nums.length == 0 || k <= 0) {
            return new double[0];
        }
        
        int n = nums.length;
        double[] result = new double[n - k + 1];
        
        // 最大堆（存储较小的一半），最小堆（存储较大的一半）
        // Java的PriorityQueue默认是最小堆，使用Collections.reverseOrder()创建最大堆
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder()); // 存储较小的一半
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // 存储较大的一半
        
        // 初始化第一个窗口（前k个元素）
        for (int i = 0; i < k; i++) {
            addNumber(nums[i], maxHeap, minHeap);
        }
        
        // 计算第一个窗口的中位数
        result[0] = getMedian(maxHeap, minHeap, k);
        
        // 滑动窗口处理后续元素
        for (int i = k; i < n; i++) {
            // 移除窗口最左边的元素（i-k位置的元素）
            removeNumber(nums[i - k], maxHeap, minHeap);
            // 添加新元素（i位置的元素）
            addNumber(nums[i], maxHeap, minHeap);
            // 计算当前窗口中位数
            result[i - k + 1] = getMedian(maxHeap, minHeap, k);
        }
        
        return result;
    }
    
    /**
     * 添加数字到堆中，保持堆的平衡
     * 
     * @param num 要添加的数字
     * @param maxHeap 最大堆（存储较小的一半）
     * @param minHeap 最小堆（存储较大的一半）
     */
    private static void addNumber(int num, PriorityQueue<Integer> maxHeap, PriorityQueue<Integer> minHeap) {
        // 先添加到最大堆（较小的一半）
        maxHeap.offer(num);
        // 将最大堆的最大值移动到最小堆（较大的一半）
        minHeap.offer(maxHeap.poll());
        
        // 如果最小堆的大小大于最大堆，重新平衡
        // 保持最大堆的大小等于最小堆的大小或比最小堆大1
        if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }
    
    /**
     * 从堆中移除数字，保持堆的平衡
     * 
     * @param num 要移除的数字
     * @param maxHeap 最大堆（存储较小的一半）
     * @param minHeap 最小堆（存储较大的一半）
     */
    private static void removeNumber(int num, PriorityQueue<Integer> maxHeap, PriorityQueue<Integer> minHeap) {
        // 判断数字在哪个堆中
        if (num <= maxHeap.peek()) {
            // 数字在最大堆中
            maxHeap.remove(num);
            // 如果最大堆的大小小于最小堆，从最小堆移动一个元素到最大堆
            if (maxHeap.size() < minHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
        } else {
            // 数字在最小堆中
            minHeap.remove(num);
            // 如果最大堆的大小比最小堆大1以上，从最大堆移动一个元素到最小堆
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.offer(maxHeap.poll());
            }
        }
    }
    
    /**
     * 获取当前中位数
     * 
     * @param maxHeap 最大堆（存储较小的一半）
     * @param minHeap 最小堆（存储较大的一半）
     * @param k 窗口大小
     * @return 当前窗口的中位数
     */
    private static double getMedian(PriorityQueue<Integer> maxHeap, PriorityQueue<Integer> minHeap, int k) {
        if (k % 2 == 1) {
            // 奇数长度，中位数是最大堆的堆顶（较小一半的最大值）
            return (double) maxHeap.peek();
        } else {
            // 偶数长度，中位数是两个堆顶的平均值
            // 注意：使用long避免整数溢出
            return ((double) maxHeap.peek() + (double) minHeap.peek()) / 2.0;
        }
    }
    
    /**
     * 优化版本：使用延迟删除技术处理重复元素
     * 时间复杂度：O(n*log k)，空间复杂度：O(k)
     * 
     * @param nums 输入数组
     * @param k 窗口大小
     * @return 每个窗口中位数的数组
     */
    public static double[] medianSlidingWindowOptimized(int[] nums, int k) {
        // 异常情况处理
        if (nums == null || nums.length == 0 || k <= 0) {
            return new double[0];
        }
        
        int n = nums.length;
        double[] result = new double[n - k + 1];
        
        // 使用延迟删除技术
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder()); // 存储较小的一半
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // 存储较大的一半
        Map<Integer, Integer> delayed = new HashMap<>(); // 延迟删除的计数器
        
        // 平衡因子：maxHeap.size() - minHeap.size()
        int balance = 0;
        
        // 初始化第一个窗口（前k个元素）
        for (int i = 0; i < k; i++) {
            addNumberOptimized(nums[i], maxHeap, minHeap, delayed, balance);
        }
        
        result[0] = getMedianOptimized(maxHeap, minHeap, k, delayed);
        
        // 滑动窗口处理后续元素
        for (int i = k; i < n; i++) {
            // 移除窗口最左边的元素（i-k位置的元素）
            removeNumberOptimized(nums[i - k], maxHeap, minHeap, delayed, balance);
            // 添加新元素（i位置的元素）
            addNumberOptimized(nums[i], maxHeap, minHeap, delayed, balance);
            // 清理延迟删除的元素
            pruneHeaps(maxHeap, minHeap, delayed);
            // 计算当前窗口中位数
            result[i - k + 1] = getMedianOptimized(maxHeap, minHeap, k, delayed);
        }
        
        return result;
    }
    
    /**
     * 优化版本：添加数字到堆中
     */
    private static void addNumberOptimized(int num, PriorityQueue<Integer> maxHeap, PriorityQueue<Integer> minHeap, 
                                         Map<Integer, Integer> delayed, int balance) {
        if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
            maxHeap.offer(num);
            balance++;
        } else {
            minHeap.offer(num);
            balance--;
        }
        
        // 重新平衡堆
        rebalanceHeaps(maxHeap, minHeap, delayed, balance);
    }
    
    /**
     * 优化版本：从堆中移除数字
     */
    private static void removeNumberOptimized(int num, PriorityQueue<Integer> maxHeap, PriorityQueue<Integer> minHeap, 
                                            Map<Integer, Integer> delayed, int balance) {
        delayed.put(num, delayed.getOrDefault(num, 0) + 1);
        
        if (!maxHeap.isEmpty() && num <= maxHeap.peek()) {
            balance--;
        } else {
            balance++;
        }
        
        // 重新平衡堆
        rebalanceHeaps(maxHeap, minHeap, delayed, balance);
    }
    
    /**
     * 重新平衡堆的大小
     */
    private static void rebalanceHeaps(PriorityQueue<Integer> maxHeap, PriorityQueue<Integer> minHeap, 
                                     Map<Integer, Integer> delayed, int balance) {
        // 平衡堆的大小
        if (balance > 1) {
            minHeap.offer(maxHeap.poll());
            balance -= 2;
        } else if (balance < -1) {
            maxHeap.offer(minHeap.poll());
            balance += 2;
        }
    }
    
    /**
     * 清理堆中延迟删除的元素
     */
    private static void pruneHeaps(PriorityQueue<Integer> maxHeap, PriorityQueue<Integer> minHeap, 
                                  Map<Integer, Integer> delayed) {
        // 清理最大堆顶部的延迟删除元素
        while (!maxHeap.isEmpty() && delayed.getOrDefault(maxHeap.peek(), 0) > 0) {
            int num = maxHeap.poll();
            delayed.put(num, delayed.get(num) - 1);
            if (delayed.get(num) == 0) {
                delayed.remove(num);
            }
        }
        
        // 清理最小堆顶部的延迟删除元素
        while (!minHeap.isEmpty() && delayed.getOrDefault(minHeap.peek(), 0) > 0) {
            int num = minHeap.poll();
            delayed.put(num, delayed.get(num) - 1);
            if (delayed.get(num) == 0) {
                delayed.remove(num);
            }
        }
    }
    
    /**
     * 优化版本：获取当前中位数
     */
    private static double getMedianOptimized(PriorityQueue<Integer> maxHeap, PriorityQueue<Integer> minHeap, 
                                           int k, Map<Integer, Integer> delayed) {
        pruneHeaps(maxHeap, minHeap, delayed);
        
        if (k % 2 == 1) {
            return (double) maxHeap.peek();
        } else {
            return ((double) maxHeap.peek() + (double) minHeap.peek()) / 2.0;
        }
    }
    
    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        double[] result1 = medianSlidingWindow(nums1, k1);
        System.out.println("输入数组: " + Arrays.toString(nums1));
        System.out.println("窗口大小: " + k1);
        System.out.println("中位数序列: " + Arrays.toString(result1));
        System.out.println("预期: [1.0, -1.0, -1.0, 3.0, 5.0, 6.0]");
        System.out.println("解释: 窗口[1,3,-1]中位数1.0, 窗口[3,-1,-3]中位数-1.0, ...");
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {1, 2, 3, 4, 2, 3, 1, 4, 2};
        int k2 = 3;
        double[] result2 = medianSlidingWindow(nums2, k2);
        System.out.println("输入数组: " + Arrays.toString(nums2));
        System.out.println("窗口大小: " + k2);
        System.out.println("中位数序列: " + Arrays.toString(result2));
        System.out.println();
        
        // 测试用例3：边界情况，k=1
        int[] nums3 = {5};
        int k3 = 1;
        double[] result3 = medianSlidingWindow(nums3, k3);
        System.out.println("输入数组: " + Arrays.toString(nums3));
        System.out.println("窗口大小: " + k3);
        System.out.println("中位数序列: " + Arrays.toString(result3));
        System.out.println("预期: [5.0]");
        System.out.println("解释: 每个窗口只有一个元素，中位数就是该元素");
        System.out.println();
        
        // 测试用例4：k等于数组长度
        int[] nums4 = {1, 2, 3, 4, 5};
        int k4 = 5;
        double[] result4 = medianSlidingWindow(nums4, k4);
        System.out.println("输入数组: " + Arrays.toString(nums4));
        System.out.println("窗口大小: " + k4);
        System.out.println("中位数序列: " + Arrays.toString(result4));
        System.out.println("预期: [3.0]");
        System.out.println("解释: 整个数组作为一个窗口，中位数是3.0");
        
        // 测试优化版本
        System.out.println("\n=== 优化版本测试 ===");
        double[] result1Opt = medianSlidingWindowOptimized(nums1, k1);
        System.out.println("优化版本结果1: " + Arrays.toString(result1Opt));
    }
}