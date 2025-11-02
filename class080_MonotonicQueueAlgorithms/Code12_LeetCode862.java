import java.util.*;

/**
 * 题目名称：LeetCode 862. 和至少为K的最短子数组
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
 * 题目难度：困难
 * 
 * 题目描述：
 * 给定一个数组arr，其中的值有可能正、负、0
 * 给定一个正数k
 * 返回累加和>=k的所有子数组中，最短的子数组长度
 * 
 * 解题思路：
 * 使用单调队列解决该问题。核心思想是利用前缀和将问题转化为寻找满足条件的两个前缀和之差。
 * 对于前缀和数组，我们需要找到最小的 j-i，使得 sum[j] - sum[i] >= k。
 * 为了高效查找，我们维护一个单调递增队列，队列中存储前缀和的索引。
 *
 * 算法步骤：
 * 1. 计算前缀和数组
 * 2. 遍历前缀和数组，维护单调递增队列
 * 3. 对于每个前缀和，检查是否能与队首元素构成满足条件的子数组
 * 4. 维护队列的单调性
 *
 * 时间复杂度分析：
 * O(n) - 每个元素最多入队出队一次
 *
 * 空间复杂度分析：
 * O(n) - 存储前缀和和单调队列
 *
 * 是否最优解：
 * ✅ 是，这是处理此类问题的最优解法
 * 
 * 工程化考量：
 * - 使用数组模拟双端队列以提高性能
 * - 考虑边界条件处理（k=0, 数组长度为1等）
 * - 处理极端输入情况（大数组、极限值等）
 */

import java.util.*;

public class Code12_LeetCode862 {
    
    /**
     * 计算和至少为K的最短子数组长度
     * @param nums 输入数组
     * @param k 目标和
     * @return 最短子数组长度，如果不存在返回-1
     */
    public static int shortestSubarray(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return -1;
        }
        
        int n = nums.length;
        // 计算前缀和数组，sum[i]表示前i个元素的和
        long[] prefixSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        // 使用双端队列维护单调递增的前缀和索引
        Deque<Integer> deque = new ArrayDeque<>();
        int minLength = Integer.MAX_VALUE;
        
        for (int i = 0; i <= n; i++) {
            // 检查当前前缀和与队首前缀和的差是否>=k
            // 如果满足条件，更新最小长度并移除队首元素
            while (!deque.isEmpty() && prefixSum[i] - prefixSum[deque.peekFirst()] >= k) {
                minLength = Math.min(minLength, i - deque.pollFirst());
            }
            
            // 维护队列的单调递增性质
            // 从队尾开始，移除所有前缀和大于等于当前前缀和的索引
            while (!deque.isEmpty() && prefixSum[deque.peekLast()] >= prefixSum[i]) {
                deque.pollLast();
            }
            
            // 将当前索引加入队列
            deque.offerLast(i);
        }
        
        return minLength != Integer.MAX_VALUE ? minLength : -1;
    }
    
    /**
     * 优化版本：使用数组模拟双端队列，提高性能
     */
    public static int shortestSubarrayOptimized(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return -1;
        }
        
        int n = nums.length;
        long[] prefixSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        int[] deque = new int[n + 1];
        int head = 0, tail = 0;
        int minLength = Integer.MAX_VALUE;
        
        for (int i = 0; i <= n; i++) {
            // 检查是否满足条件
            while (head < tail && prefixSum[i] - prefixSum[deque[head]] >= k) {
                minLength = Math.min(minLength, i - deque[head++]);
            }
            
            // 维护单调递增性质
            while (head < tail && prefixSum[deque[tail - 1]] >= prefixSum[i]) {
                tail--;
            }
            
            deque[tail++] = i;
        }
        
        return minLength != Integer.MAX_VALUE ? minLength : -1;
    }
    
    /**
     * 测试方法 - 包含多种边界情况和测试用例
     */
    public static void testShortestSubarray() {
        System.out.println("=== LeetCode 862 测试用例 ===");
        
        // 测试用例1：基础示例
        int[] nums1 = {2, -1, 2};
        int k1 = 3;
        int result1 = shortestSubarray(nums1, k1);
        System.out.println("测试用例1 - 输入: [2,-1,2], k=3");
        System.out.println("预期输出: 3, 实际输出: " + result1);
        System.out.println("测试结果: " + (result1 == 3 ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例2：包含负数
        int[] nums2 = {1, 2, -3, 4, 5};
        int k2 = 7;
        int result2 = shortestSubarray(nums2, k2);
        System.out.println("\n测试用例2 - 输入: [1,2,-3,4,5], k=7");
        System.out.println("预期输出: 2, 实际输出: " + result2);
        System.out.println("测试结果: " + (result2 == 2 ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例3：单个元素
        int[] nums3 = {5};
        int k3 = 5;
        int result3 = shortestSubarray(nums3, k3);
        System.out.println("\n测试用例3 - 输入: [5], k=5");
        System.out.println("预期输出: 1, 实际输出: " + result3);
        System.out.println("测试结果: " + (result3 == 1 ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例4：不存在满足条件的子数组
        int[] nums4 = {-1, -2, -3};
        int k4 = 5;
        int result4 = shortestSubarray(nums4, k4);
        System.out.println("\n测试用例4 - 输入: [-1,-2,-3], k=5");
        System.out.println("预期输出: -1, 实际输出: " + result4);
        System.out.println("测试结果: " + (result4 == -1 ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例5：优化版本对比
        int[] nums5 = {2, -1, 2};
        int k5 = 3;
        int result5 = shortestSubarrayOptimized(nums5, k5);
        System.out.println("\n测试用例5 - 优化版本对比");
        System.out.println("输入: [2,-1,2], k=3");
        System.out.println("优化版本输出: " + result5);
        System.out.println("测试结果: " + (result5 == 3 ? "✓ 通过" : "✗ 失败"));
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(n) - 每个元素最多入队出队一次");
        System.out.println("空间复杂度: O(n) - 前缀和数组和单调队列");
        System.out.println("最优解: ✅ 是");
        
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 异常处理: 处理空数组和无效k值");
        System.out.println("2. 性能优化: 提供数组模拟队列的优化版本");
        System.out.println("3. 内存管理: 使用long类型防止整数溢出");
        System.out.println("4. 可读性: 清晰的变量命名和详细注释");
    }
    
    public static void main(String[] args) {
        testShortestSubarray();
    }
}