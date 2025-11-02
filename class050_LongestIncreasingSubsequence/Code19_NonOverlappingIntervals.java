package class072;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 无重叠区间
 * 
 * 题目来源：LeetCode 435. 无重叠区间
 * 题目链接：https://leetcode.cn/problems/non-overlapping-intervals/
 * 题目描述：给定一个区间的集合 intervals，其中 intervals[i] = [starti, endi]。
 * 返回需要移除区间的最小数量，使剩余区间互不重叠。
 * 
 * 算法思路：
 * 1. 这是一个区间调度问题，可以使用贪心算法解决
 * 2. 按结束时间排序，优先选择结束时间早的区间
 * 3. 这样可以为后续区间留下更多空间
 * 4. 需要移除的区间数量 = 总区间数 - 最大不重叠区间数
 * 
 * 时间复杂度：O(n*logn) - 排序时间复杂度
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 是否最优解：是，这是最优解法
 * 
 * 示例：
 * 输入: intervals = [[1,2],[2,3],[3,4],[1,3]]
 * 输出: 1
 * 解释: 移除 [1,3] 后，剩下的区间没有重叠。
 * 
 * 输入: intervals = [[1,2],[1,2],[1,2]]
 * 输出: 2
 * 解释: 你需要移除两个 [1,2] 来使剩下的区间没有重叠。
 */

public class Code19_NonOverlappingIntervals {

    /**
     * 计算需要移除的最小区间数量
     * 
     * @param intervals 区间数组
     * @return 需要移除的最小区间数量
     */
    public static int eraseOverlapIntervals(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        int n = intervals.length;
        
        // 按结束时间升序排序
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return a[1] - b[1];
            }
        });
        
        // 记录不重叠区间的数量
        int count = 1;
        int end = intervals[0][1];
        
        for (int i = 1; i < n; i++) {
            // 如果当前区间的开始时间 >= 前一个区间的结束时间，不重叠
            if (intervals[i][0] >= end) {
                count++;
                end = intervals[i][1];
            }
        }
        
        // 需要移除的区间数量 = 总区间数 - 不重叠区间数
        return n - count;
    }

    /**
     * 使用LIS思路的解法（用于对比）
     * 
     * 算法思路：
     * 1. 将问题转化为求最长不重叠区间序列
     * 2. 按开始时间排序，然后求最长不重叠区间序列
     * 3. 使用动态规划计算最长序列长度
     * 
     * 时间复杂度：O(n²) - 需要双重循环
     * 空间复杂度：O(n) - 需要dp数组
     * 是否最优解：否，存在O(n*logn)的贪心解法
     * 
     * @param intervals 区间数组
     * @return 需要移除的最小区间数量
     */
    public static int eraseOverlapIntervalsDP(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        int n = intervals.length;
        
        // 按开始时间排序
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return a[0] - b[0];
            }
        });
        
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int maxLen = 1;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 如果区间j和区间i不重叠
                if (intervals[j][1] <= intervals[i][0]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLen = Math.max(maxLen, dp[i]);
        }
        
        return n - maxLen;
    }

    /**
     * 使用开始时间排序的贪心解法（错误示范，用于对比）
     * 
     * 算法思路：
     * 1. 按开始时间排序，然后选择开始时间最早的区间
     * 2. 这种策略可能不是最优的
     * 
     * 时间复杂度：O(n*logn)
     * 空间复杂度：O(1)
     * 是否最优解：否，可能得到次优解
     * 
     * @param intervals 区间数组
     * @return 需要移除的最小区间数量
     */
    public static int eraseOverlapIntervalsWrong(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        int n = intervals.length;
        
        // 按开始时间排序（错误策略）
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return a[0] - b[0];
            }
        });
        
        int count = 1;
        int end = intervals[0][1];
        
        for (int i = 1; i < n; i++) {
            if (intervals[i][0] >= end) {
                count++;
                end = intervals[i][1];
            } else {
                // 如果重叠，选择结束时间更早的区间
                end = Math.min(end, intervals[i][1]);
            }
        }
        
        return n - count;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[][] intervals1 = {{1,2}, {2,3}, {3,4}, {1,3}};
        System.out.println("输入: [[1,2],[2,3],[3,4],[1,3]]");
        System.out.println("贪心方法输出: " + eraseOverlapIntervals(intervals1));
        System.out.println("DP方法输出: " + eraseOverlapIntervalsDP(intervals1));
        System.out.println("错误方法输出: " + eraseOverlapIntervalsWrong(intervals1));
        System.out.println("期望: 1");
        System.out.println();
        
        // 测试用例2
        int[][] intervals2 = {{1,2}, {1,2}, {1,2}};
        System.out.println("输入: [[1,2],[1,2],[1,2]]");
        System.out.println("贪心方法输出: " + eraseOverlapIntervals(intervals2));
        System.out.println("DP方法输出: " + eraseOverlapIntervalsDP(intervals2));
        System.out.println("错误方法输出: " + eraseOverlapIntervalsWrong(intervals2));
        System.out.println("期望: 2");
        System.out.println();
        
        // 测试用例3
        int[][] intervals3 = {{1,2}, {2,3}};
        System.out.println("输入: [[1,2],[2,3]]");
        System.out.println("贪心方法输出: " + eraseOverlapIntervals(intervals3));
        System.out.println("DP方法输出: " + eraseOverlapIntervalsDP(intervals3));
        System.out.println("错误方法输出: " + eraseOverlapIntervalsWrong(intervals3));
        System.out.println("期望: 0");
        System.out.println();
        
        // 测试用例4：展示错误策略的问题
        int[][] intervals4 = {{1,100}, {11,22}, {1,11}, {2,12}};
        System.out.println("输入: [[1,100],[11,22],[1,11],[2,12]]");
        System.out.println("贪心方法输出: " + eraseOverlapIntervals(intervals4));
        System.out.println("DP方法输出: " + eraseOverlapIntervalsDP(intervals4));
        System.out.println("错误方法输出: " + eraseOverlapIntervalsWrong(intervals4));
        System.out.println("说明：错误策略可能得到次优解");
        System.out.println();
        
        // 性能测试
        int[][] largeIntervals = new int[10000][2];
        for (int i = 0; i < 10000; i++) {
            largeIntervals[i][0] = (int) (Math.random() * 100000);
            largeIntervals[i][1] = largeIntervals[i][0] + (int) (Math.random() * 1000) + 1;
        }
        
        long startTime = System.currentTimeMillis();
        int result1 = eraseOverlapIntervals(largeIntervals);
        long endTime = System.currentTimeMillis();
        System.out.println("贪心方法处理10000个区间耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int result2 = eraseOverlapIntervalsDP(largeIntervals);
        endTime = System.currentTimeMillis();
        System.out.println("DP方法处理10000个区间耗时: " + (endTime - startTime) + "ms");
        System.out.println("两种方法结果是否一致: " + (result1 == result2));
    }
}