// -*- coding: utf-8 -*-
/*
区间染色算法（结合贪心）

问题描述：
给定多个区间，每个区间代表一个需要染色的区域，每次可以选择一个区间进行染色，
求最少需要多少次染色才能覆盖所有给定的区间。

贪心策略：
按照区间的右端点进行排序，每次选择能够覆盖当前未染色区域的最右端点的区间。

时间复杂度：O(n log n)，其中n是区间的数量，主要是排序的时间复杂度
空间复杂度：O(1)，只需要常量级的额外空间

相关题目：
1. LeetCode 435. 无重叠区间
2. LeetCode 452. 用最少数量的箭引爆气球
3. LeetCode 253. 会议室 II
*/

import java.util.*;

public class IntervalColoring {
    /**
     * 区间染色算法实现
     * 
     * @param intervals 区间列表，每个区间是一个int数组 [start, end]
     * @return 最少需要的染色次数
     * @throws IllegalArgumentException 当输入无效时抛出异常
     */
    public static int intervalColoring(int[][] intervals) {
        // 参数验证
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        for (int[] interval : intervals) {
            if (interval == null || interval.length != 2 || interval[0] > interval[1]) {
                throw new IllegalArgumentException("每个区间必须是有效的[start, end]数组，且start <= end");
            }
        }
        
        // 按照区间的右端点进行排序
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));
        
        // 贪心选择
        int count = 1;  // 至少需要一次染色
        int end = intervals[0][1];  // 当前已经覆盖到的最右端点
        
        for (int i = 1; i < intervals.length; i++) {
            // 如果当前区间的左端点大于已经覆盖到的最右端点，需要新的染色
            if (intervals[i][0] > end) {
                count++;
                end = intervals[i][1];
            }
        }
        
        return count;
    }

    /**
     * LeetCode 452. 用最少数量的箭引爆气球
     * 题目链接：https://leetcode-cn.com/problems/minimum-number-of-arrows-to-burst-balloons/
     * 
     * 问题描述：
     * 在二维空间中有许多球形的气球。对于每个气球，提供的输入是水平方向上，气球直径的开始和结束坐标。
     * 由于它是水平的，所以y坐标并不重要，因此只要知道开始和结束的x坐标就足够了。开始坐标总是小于结束坐标。
     * 一支弓箭可以沿着x轴从不同点完全垂直地射出。在坐标x处射出一支箭，若有一个气球的直径的开始和结束坐标为x_start，x_end，
     * 且满足x_start ≤ x ≤ x_end，则该气球会被引爆。可以射出的弓箭的数量没有限制。
     * 弓箭一旦被射出之后，可以无限地前进。我们想找到使得所有气球全部被引爆，所需的弓箭的最小数量。
     * 
     * 解题思路：
     * 这是区间染色问题的一个变种，相当于求最少需要多少个点才能覆盖所有区间。
     * 我们可以按照区间的右端点进行排序，然后每次选择当前区间的右端点作为箭的发射位置，
     * 这样可以尽可能多地引爆后面的气球。
     * 
     * @param points 气球的坐标列表，每个气球表示为 [x_start, x_end]
     * @return 所需的最小弓箭数量
     */
    public static int findMinArrowShots(int[][] points) {
        if (points == null || points.length == 0) {
            return 0;
        }
        
        // 按照右端点排序
        Arrays.sort(points, Comparator.comparingInt(a -> a[1]));
        
        int arrows = 1;
        int pos = points[0][1];  // 第一支箭的位置
        
        for (int i = 1; i < points.length; i++) {
            // 如果当前气球的左端点大于箭的位置，需要新的箭
            if (points[i][0] > pos) {
                arrows++;
                pos = points[i][1];
            }
        }
        
        return arrows;
    }

    /**
     * LeetCode 435. 无重叠区间
     * 题目链接：https://leetcode-cn.com/problems/non-overlapping-intervals/
     * 
     * 问题描述：
     * 给定一个区间的集合，找到需要移除区间的最小数量，使得剩余区间互不重叠。
     * 
     * 解题思路：
     * 这个问题可以转换为找到最大不重叠区间数，然后用总区间数减去这个值。
     * 求最大不重叠区间数的方法与区间染色类似，我们按照区间的右端点排序，
     * 然后贪心选择不重叠的区间。
     * 
     * @param intervals 区间列表，每个区间是一个int数组 [start, end]
     * @return 需要移除的最小区间数量
     */
    public static int eraseOverlapIntervals(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        // 按照右端点排序
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));
        
        int count = 1;  // 最大不重叠区间数
        int end = intervals[0][1];
        
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] >= end) {
                count++;
                end = intervals[i][1];
            }
        }
        
        return intervals.length - count;
    }

    /**
     * LeetCode 253. 会议室 II
     * 题目链接：https://leetcode-cn.com/problems/meeting-rooms-ii/
     * 
     * 问题描述：
     * 给定一个会议时间安排的数组，每个会议时间都会包括开始和结束的时间 [[s1,e1],[s2,e2],...] (si < ei)，
     * 为避免会议冲突，同时要考虑充分利用会议室资源，请你计算至少需要多少间会议室，才能满足这些会议安排。
     * 
     * 解题思路：
     * 我们可以将所有的开始时间和结束时间分别排序，然后使用双指针的方法来计算所需的会议室数量。
     * 每当一个会议开始时，我们需要一个新的会议室；每当一个会议结束时，我们释放一个会议室。
     * 我们只需要跟踪当前正在进行的会议数量，最大值即为所需的会议室数量。
     * 
     * @param intervals 会议时间列表，每个会议是一个int数组 [start, end]
     * @return 所需的最少会议室数量
     */
    public static int minMeetingRooms(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        // 分别提取开始时间和结束时间并排序
        int[] startTimes = new int[intervals.length];
        int[] endTimes = new int[intervals.length];
        
        for (int i = 0; i < intervals.length; i++) {
            startTimes[i] = intervals[i][0];
            endTimes[i] = intervals[i][1];
        }
        
        Arrays.sort(startTimes);
        Arrays.sort(endTimes);
        
        int i = 0, j = 0;
        int maxRooms = 0, currentRooms = 0;
        
        while (i < startTimes.length && j < endTimes.length) {
            // 如果当前会议开始时间小于结束时间，需要一个新的会议室
            if (startTimes[i] < endTimes[j]) {
                currentRooms++;
                maxRooms = Math.max(maxRooms, currentRooms);
                i++;
            }
            // 否则，释放一个会议室
            else {
                currentRooms--;
                j++;
            }
        }
        
        return maxRooms;
    }

    // 测试代码
    public static void main(String[] args) {
        try {
            // 测试区间染色算法
            int[][] intervals1 = {{1, 4}, {2, 5}, {7, 9}, {8, 10}};
            System.out.println("区间染色最少次数: " + intervalColoring(intervals1));  // 应该输出 2
            
            // 测试LeetCode 452
            int[][] points = {{10, 16}, {2, 8}, {1, 6}, {7, 12}};
            System.out.println("最少需要的箭数量: " + findMinArrowShots(points));  // 应该输出 2
            
            // 测试LeetCode 435
            int[][] intervals2 = {{1, 2}, {2, 3}, {3, 4}, {1, 3}};
            System.out.println("需要移除的最小区间数量: " + eraseOverlapIntervals(intervals2));  // 应该输出 1
            
            // 测试LeetCode 253
            int[][] intervals3 = {{0, 30}, {5, 10}, {15, 20}};
            System.out.println("最少需要的会议室数量: " + minMeetingRooms(intervals3));  // 应该输出 2
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}