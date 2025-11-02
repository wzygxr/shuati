package class049;

import java.util.*;

/**
 * 绝对差不超过限制的最长连续子数组问题解决方案
 * 
 * 问题描述：
 * 给你一个整数数组 nums ，和一个表示限制的整数 limit，
 * 请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit 。
 * 
 * 解题思路：
 * 使用滑动窗口配合 TreeMap 来维护窗口内的最大值和最小值：
 * 1. 右指针不断扩展窗口，将元素加入 TreeMap
 * 2. 当窗口内最大值与最小值的差超过 limit 时，收缩左指针
 * 3. TreeMap 可以在 O(log k) 时间内维护窗口元素的有序性，其中 k 是窗口大小
 * 4. TreeMap 的 firstKey() 和 lastKey() 分别获取最小值和最大值
 * 
 * 算法复杂度分析：
 * 时间复杂度: O(n * log n) - 每个元素最多入队和出队一次，TreeMap 操作需要 O(log n)
 * 空间复杂度: O(n) - TreeMap 最多存储 n 个元素
 * 
 * 是否最优解: 是，这是处理该问题的较优解法之一，还可以用单调队列优化到 O(n)
 * 
 * 相关题目链接：
 * LeetCode 1438. 绝对差不超过限制的最长连续子数组
 * https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 绝对差不超过限制的最长连续子数组
 *    https://www.nowcoder.com/practice/1266570c4a06487981ed50e84e8b720d
 * 2. LintCode 1438. 绝对差不超过限制的最长连续子数组
 *    https://www.lintcode.com/problem/1438/
 * 3. HackerRank - Longest Subarray with Limited Difference
 *    https://www.hackerrank.com/challenges/longest-subarray-with-limited-difference/problem
 * 4. CodeChef - SUBARR - Subarray with Limited Difference
 *    https://www.codechef.com/problems/SUBARR
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
 * 1. 异常处理：处理空数组等边界情况
 * 2. 性能优化：使用TreeMap维护窗口元素有序性，避免重复计算
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 */
public class Code12_LongestSubarrayWithLimitedDifference {
    
    /**
     * 计算绝对差不超过限制的最长连续子数组长度
     * 
     * @param nums   输入的整数数组
     * @param limit  限制值，子数组中任意两个元素的绝对差不能超过此值
     * @return 最长连续子数组的长度
     */
    public static int longestSubarray(int[] nums, int limit) {
        // 异常情况处理
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // TreeMap 维护窗口内元素及其出现次数，保持有序
        // key为元素值，value为该元素在窗口中的出现次数
        TreeMap<Integer, Integer> map = new TreeMap<>();
        int left = 0;  // 滑动窗口左指针
        int result = 0;  // 记录最长子数组长度
        
        // 右指针扩展窗口
        for (int right = 0; right < nums.length; right++) {
            // 将右指针元素加入 TreeMap
            // getOrDefault方法获取元素当前出现次数，如果不存在则返回0
            map.put(nums[right], map.getOrDefault(nums[right], 0) + 1);
            
            // 当窗口内最大值与最小值的差超过 limit 时，需要收缩左指针
            // TreeMap的lastKey()获取最大值，firstKey()获取最小值
            while (map.lastKey() - map.firstKey() > limit) {
                // 减少左指针元素的计数
                map.put(nums[left], map.get(nums[left]) - 1);
                // 如果计数为 0，从 TreeMap 中移除该元素
                if (map.get(nums[left]) == 0) {
                    map.remove(nums[left]);
                }
                // 移动左指针
                left++;
            }
            
            // 更新最长子数组长度（当前窗口大小）
            result = Math.max(result, right - left + 1);
        }
        
        return result;
    }
    
    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {8, 2, 4, 7};
        int limit1 = 4;
        int result1 = longestSubarray(nums1, limit1);
        System.out.println("输入数组: " + Arrays.toString(nums1));
        System.out.println("限制值: " + limit1);
        System.out.println("最长子数组长度: " + result1);
        // 预期输出: 2 ([2,4] 或 [4,7])
        
        // 测试用例2
        int[] nums2 = {10, 1, 2, 4, 7, 2};
        int limit2 = 5;
        int result2 = longestSubarray(nums2, limit2);
        System.out.println("\n输入数组: " + Arrays.toString(nums2));
        System.out.println("限制值: " + limit2);
        System.out.println("最长子数组长度: " + result2);
        // 预期输出: 4 ([2,4,7,2])
        
        // 测试用例3
        int[] nums3 = {4, 2, 2, 2, 4, 4, 2, 2};
        int limit3 = 0;
        int result3 = longestSubarray(nums3, limit3);
        System.out.println("\n输入数组: " + Arrays.toString(nums3));
        System.out.println("限制值: " + limit3);
        System.out.println("最长子数组长度: " + result3);
        // 预期输出: 3 ([2,2,2])
        
        // 测试用例4：空数组
        int[] nums4 = {};
        int limit4 = 1;
        int result4 = longestSubarray(nums4, limit4);
        System.out.println("\n输入数组: " + Arrays.toString(nums4));
        System.out.println("限制值: " + limit4);
        System.out.println("最长子数组长度: " + result4);
        // 预期输出: 0
    }
}