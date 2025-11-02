package class129;

import java.util.*;

/**
 * LeetCode 239. Sliding Window Maximum
 * 
 * 题目描述：
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * 返回滑动窗口中的最大值。
 * 
 * 解题思路：
 * 这是一个经典的滑动窗口问题，可以使用多种方法解决。
 * 
 * 方法一：使用优先队列（最大堆）
 * 1. 使用优先队列维护窗口内的元素，队列中存储 (value, index) 对
 * 2. 按照值的大小排序，最大值在队首
 * 3. 当窗口滑动时，添加新元素并移除超出窗口范围的元素
 * 
 * 方法二：使用双端队列（单调队列）
 * 1. 维护一个单调递减的双端队列
 * 2. 队列中存储数组下标，对应的值单调递减
 * 3. 当新元素加入时，从队尾移除所有小于等于新元素的元素
 * 4. 从队首移除超出窗口范围的元素
 * 
 * 时间复杂度：
 * - 优先队列方法：O(n log k)
 * - 双端队列方法：O(n) - 每个元素最多进出队列一次
 * 空间复杂度：O(k)
 * 
 * 滑动窗口算法总结：
 * 1. 滑动窗口是一种在数组或字符串上进行区间操作的高效方法
 * 2. 通常可以将时间复杂度从 O(n²) 优化到 O(n)
 * 3. 常用于解决：子数组/子串问题、最值问题、存在性问题
 * 4. 关键技巧：
 *    - 维护窗口内的状态（如最大值、最小值、元素频率等）
 *    - 高效地添加新元素和移除过期元素
 *    - 根据题目选择合适的数据结构（双端队列、哈希表、TreeSet等）
 * 
 * 补充题目汇总：
 * 1. LeetCode 220. 存在重复元素 III（TreeSet滑动窗口）
 * 2. LeetCode 219. 存在重复元素 II（哈希表滑动窗口）
 * 3. LeetCode 480. 滑动窗口中位数
 * 4. LeetCode 992. K 个不同整数的子数组
 * 5. LeetCode 76. 最小覆盖子串
 * 6. LeetCode 3. 无重复字符的最长子串
 * 7. LintCode 363. 接雨水
 * 8. HackerRank - Sliding Window Median
 * 9. Codeforces 372C. Watching Fireworks is Fun
 * 10. AtCoder ABC134F. Permutation Oddness
 * 11. 牛客网 NC123. 滑动窗口的最大值
 * 12. 杭电OJ 6827. Master of Subgraph
 * 13. POJ 2823. Sliding Window
 * 14. UVa 11572. Unique Snowflakes
 * 15. CodeChef - CHEFCOMP
 * 
 * 工程化考量：
 * 1. 在实际应用中，滑动窗口算法常用于：
 *    - 网络流量监控（实时计算网络流量的统计信息）
 *    - 金融数据分析（计算股票价格的移动平均）
 *    - 文本处理（关键词提取、模式匹配）
 * 2. 优化技巧：
 *    - 使用基本数据类型而非包装类以提高性能
 *    - 对于固定窗口大小的问题，可以使用数组代替集合以减少开销
 *    - 考虑内存占用，特别是处理大规模数据时
 */
public class LeetCode239_SlidingWindowMaximum {
    
    /**
     * 使用优先队列（最大堆）解决滑动窗口最大值问题
     * 
     * @param nums 整数数组
     * @param k 滑动窗口大小
     * @return 每个滑动窗口中的最大值数组
     */
    public static int[] maxSlidingWindowWithHeap(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        
        int n = nums.length;
        if (k == 1) {
            return nums.clone();
        }
        
        // 结果数组
        int[] result = new int[n - k + 1];
        
        // 最大堆，存储 (value, index) 对
        // 按值降序排列，值相同时按索引升序排列
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) {
                return b[0] - a[0]; // 值降序
            }
            return a[1] - b[1]; // 索引升序
        });
        
        // 初始化堆，添加前 k-1 个元素
        for (int i = 0; i < k - 1; i++) {
            maxHeap.offer(new int[]{nums[i], i});
        }
        
        // 处理每个窗口
        for (int i = k - 1; i < n; i++) {
            // 添加当前元素
            maxHeap.offer(new int[]{nums[i], i});
            
            // 移除超出窗口范围的元素
            while (maxHeap.peek()[1] <= i - k) {
                maxHeap.poll();
            }
            
            // 记录当前窗口的最大值
            result[i - k + 1] = maxHeap.peek()[0];
        }
        
        return result;
    }
    
    /**
     * 使用双端队列（单调队列）解决滑动窗口最大值问题
     * 
     * @param nums 整数数组
     * @param k 滑动窗口大小
     * @return 每个滑动窗口中的最大值数组
     */
    public static int[] maxSlidingWindowWithDeque(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        
        int n = nums.length;
        if (k == 1) {
            return nums.clone();
        }
        
        // 结果数组
        int[] result = new int[n - k + 1];
        
        // 双端队列，存储数组下标
        // 队列中对应的值保持单调递减
        Deque<Integer> deque = new LinkedList<>();
        
        for (int i = 0; i < n; i++) {
            // 移除队首超出窗口范围的元素
            while (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }
            
            // 从队尾移除所有小于当前元素的元素
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }
            
            // 添加当前元素的下标
            deque.offerLast(i);
            
            // 当窗口大小达到 k 时，记录最大值
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        
        return result;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        System.out.println("测试用例1:");
        System.out.println("输入: nums = " + Arrays.toString(nums1) + ", k = " + k1);
        System.out.println("使用优先队列: " + Arrays.toString(maxSlidingWindowWithHeap(nums1, k1)));
        System.out.println("使用双端队列: " + Arrays.toString(maxSlidingWindowWithDeque(nums1, k1)));
        // 期望输出: [3, 3, 5, 5, 6, 7]
        
        // 测试用例2
        int[] nums2 = {1};
        int k2 = 1;
        System.out.println("\n测试用例2:");
        System.out.println("输入: nums = " + Arrays.toString(nums2) + ", k = " + k2);
        System.out.println("使用优先队列: " + Arrays.toString(maxSlidingWindowWithHeap(nums2, k2)));
        System.out.println("使用双端队列: " + Arrays.toString(maxSlidingWindowWithDeque(nums2, k2)));
        // 期望输出: [1]
        
        // 测试用例3
        int[] nums3 = {1, -1};
        int k3 = 1;
        System.out.println("\n测试用例3:");
        System.out.println("输入: nums = " + Arrays.toString(nums3) + ", k = " + k3);
        System.out.println("使用优先队列: " + Arrays.toString(maxSlidingWindowWithHeap(nums3, k3)));
        System.out.println("使用双端队列: " + Arrays.toString(maxSlidingWindowWithDeque(nums3, k3)));
        // 期望输出: [1, -1]
        
        // 测试用例4
        int[] nums4 = {9, 11};
        int k4 = 2;
        System.out.println("\n测试用例4:");
        System.out.println("输入: nums = " + Arrays.toString(nums4) + ", k = " + k4);
        System.out.println("使用优先队列: " + Arrays.toString(maxSlidingWindowWithHeap(nums4, k4)));
        System.out.println("使用双端队列: " + Arrays.toString(maxSlidingWindowWithDeque(nums4, k4)));
        // 期望输出: [11]
    }
}