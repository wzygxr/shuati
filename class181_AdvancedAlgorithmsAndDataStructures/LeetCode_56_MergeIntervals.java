package class185.sweep_line_problems;

import java.util.*;

/**
 * LeetCode 56. 合并区间 (Merge Intervals)
 * 
 * 题目来源：https://leetcode.cn/problems/merge-intervals/
 * 
 * 题目描述：
 * 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi]。
 * 请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
 * 
 * 示例 1：
 * 输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
 * 输出：[[1,6],[8,10],[15,18]]
 * 解释：区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6]
 * 
 * 示例 2：
 * 输入：intervals = [[1,4],[4,5]]
 * 输出：[[1,5]]
 * 解释：区间 [1,4] 和 [4,5] 可被视为重叠区间
 * 
 * 提示：
 * 1 <= intervals.length <= 10^4
 * intervals[i].length == 2
 * 0 <= starti <= endi <= 10^4
 * 
 * 解题思路：
 * 使用扫描线算法解决区间合并问题。核心思想是：
 * 1. 将所有区间按照开始时间排序
 * 2. 维护当前合并区间的起始和结束时间
 * 3. 遍历排序后的区间，如果当前区间与合并区间重叠则合并，否则将合并区间加入结果
 * 
 * 时间复杂度：O(n log n)，主要来自排序
 * 空间复杂度：O(n)，存储结果
 * 
 * 相关题目：
 * - LeetCode 57. 插入区间
 * - LeetCode 252. 会议室
 * - LeetCode 253. 会议室II
 */
public class LeetCode_56_MergeIntervals {
    
    /**
     * 合并区间的扫描线解法
     * @param intervals 输入区间数组
     * @return 合并后的区间数组
     */
    public static int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return new int[0][2];
        }
        
        // 按照区间开始时间排序
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        
        List<int[]> merged = new ArrayList<>();
        int[] currentInterval = intervals[0];
        
        for (int i = 1; i < intervals.length; i++) {
            int[] interval = intervals[i];
            
            if (interval[0] <= currentInterval[1]) {
                // 当前区间与合并区间重叠，合并
                currentInterval[1] = Math.max(currentInterval[1], interval[1]);
            } else {
                // 不重叠，将当前合并区间加入结果，开始新的合并区间
                merged.add(currentInterval);
                currentInterval = interval;
            }
        }
        
        // 添加最后一个合并区间
        merged.add(currentInterval);
        
        return merged.toArray(new int[merged.size()][]);
    }
    
    /**
     * 测试合并区间解法
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 56. 合并区间 ===");
        
        // 测试用例1
        System.out.println("测试用例1:");
        int[][] intervals1 = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] result1 = merge(intervals1);
        System.out.println("输入: " + Arrays.deepToString(intervals1));
        System.out.println("输出: " + Arrays.deepToString(result1));
        System.out.println("期望: [[1,6],[8,10],[15,18]]");
        System.out.println();
        
        // 测试用例2
        System.out.println("测试用例2:");
        int[][] intervals2 = {{1, 4}, {4, 5}};
        int[][] result2 = merge(intervals2);
        System.out.println("输入: " + Arrays.deepToString(intervals2));
        System.out.println("输出: " + Arrays.deepToString(result2));
        System.out.println("期望: [[1,5]]");
        System.out.println();
        
        // 边界测试
        System.out.println("边界测试:");
        int[][] intervals3 = {{1, 4}, {0, 4}};
        int[][] result3 = merge(intervals3);
        System.out.println("输入: " + Arrays.deepToString(intervals3));
        System.out.println("输出: " + Arrays.deepToString(result3));
        System.out.println("期望: [[0,4]]");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int n = 10000;
        int[][] largeIntervals = new int[n][2];
        
        for (int i = 0; i < n; i++) {
            int start = random.nextInt(100000);
            int end = start + random.nextInt(1000) + 1;
            largeIntervals[i] = new int[]{start, end};
        }
        
        long startTime = System.nanoTime();
        int[][] largeResult = merge(largeIntervals);
        long endTime = System.nanoTime();
        
        System.out.println("10000个区间的合并计算完成");
        System.out.println("合并后区间数量: " + largeResult.length);
        System.out.println("运行时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 异常处理：处理空输入和null输入");
        System.out.println("2. 边界情况：处理单个区间、完全重叠区间");
        System.out.println("3. 性能优化：排序时间复杂度O(n log n)是最优解");
        System.out.println("4. 内存优化：原地合并或使用最小额外空间");
    }
}