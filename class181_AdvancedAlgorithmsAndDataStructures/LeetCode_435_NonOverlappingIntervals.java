package class008_AdvancedAlgorithmsAndDataStructures.sweep_line_problems;

import java.util.*;

/**
 * LeetCode 435. Non-overlapping Intervals
 * 
 * 题目描述：
 * 给定一个区间的集合 intervals ，其中 intervals[i] = [start_i, end_i] 。
 * 返回需要移除区间的最小数量，使剩余区间互不重叠。
 * 
 * 解题思路：
 * 这是一个经典的贪心算法问题，也可以用扫描线算法的思想来解决。
 * 我们按照区间的结束时间进行排序，然后贪心地选择结束时间最早的区间，
 * 这样可以为后续的区间留出更多空间。
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(1)
 */
public class LeetCode_435_NonOverlappingIntervals {
    
    static class Solution {
        public int eraseOverlapIntervals(int[][] intervals) {
            if (intervals == null || intervals.length == 0) {
                return 0;
            }
            
            // 按照区间结束时间排序
            Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));
            
            int count = 0; // 需要移除的区间数量
            int end = intervals[0][1]; // 当前选择区间的结束时间
            
            // 遍历所有区间
            for (int i = 1; i < intervals.length; i++) {
                // 如果当前区间的开始时间小于前一个区间的结束时间，说明有重叠
                if (intervals[i][0] < end) {
                    count++; // 移除当前区间
                } else {
                    // 更新结束时间
                    end = intervals[i][1];
                }
            }
            
            return count;
        }
        
        // 另一种解法：计算最多能保留的区间数量
        public int eraseOverlapIntervals2(int[][] intervals) {
            if (intervals == null || intervals.length == 0) {
                return 0;
            }
            
            // 按照区间结束时间排序
            Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));
            
            int count = 1; // 能保留的区间数量，至少能保留第一个区间
            int end = intervals[0][1]; // 当前保留区间的结束时间
            
            // 遍历所有区间
            for (int i = 1; i < intervals.length; i++) {
                // 如果当前区间的开始时间大于等于前一个保留区间的结束时间，说明不重叠
                if (intervals[i][0] >= end) {
                    count++; // 保留当前区间
                    end = intervals[i][1]; // 更新结束时间
                }
            }
            
            return intervals.length - count; // 需要移除的数量 = 总数 - 保留的数量
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int[][] intervals1 = {{1,2},{2,3},{3,4},{1,3}};
        System.out.println("测试用例1:");
        System.out.println("区间: " + Arrays.deepToString(intervals1));
        System.out.println("需要移除的最小区间数: " + solution.eraseOverlapIntervals(intervals1));
        System.out.println("另一种解法结果: " + solution.eraseOverlapIntervals2(intervals1));
        System.out.println();
        
        // 测试用例2
        int[][] intervals2 = {{1,2},{1,2},{1,2}};
        System.out.println("测试用例2:");
        System.out.println("区间: " + Arrays.deepToString(intervals2));
        System.out.println("需要移除的最小区间数: " + solution.eraseOverlapIntervals(intervals2));
        System.out.println("另一种解法结果: " + solution.eraseOverlapIntervals2(intervals2));
        System.out.println();
        
        // 测试用例3
        int[][] intervals3 = {{1,2},{2,3}};
        System.out.println("测试用例3:");
        System.out.println("区间: " + Arrays.deepToString(intervals3));
        System.out.println("需要移除的最小区间数: " + solution.eraseOverlapIntervals(intervals3));
        System.out.println("另一种解法结果: " + solution.eraseOverlapIntervals2(intervals3));
        System.out.println();
        
        // 测试用例4
        int[][] intervals4 = {{1,100},{11,22},{1,11},{2,12}};
        System.out.println("测试用例4:");
        System.out.println("区间: " + Arrays.deepToString(intervals4));
        System.out.println("需要移除的最小区间数: " + solution.eraseOverlapIntervals(intervals4));
        System.out.println("另一种解法结果: " + solution.eraseOverlapIntervals2(intervals4));
    }
}