package class185.sweep_line_problems;

import java.util.*;

/**
 * LeetCode 253. 会议室II (Meeting Rooms II)
 * 
 * 题目来源：https://leetcode.cn/problems/meeting-rooms-ii/
 * 
 * 题目描述：
 * 给定一个会议时间安排的数组 intervals ，每个会议时间都会包括开始和结束的时间 intervals[i] = [starti, endi] ，
 * 请你计算至少需要多少间会议室，才能满足这些会议安排。
 * 
 * 示例 1：
 * 输入：intervals = [[0,30],[5,10],[15,20]]
 * 输出：2
 * 
 * 示例 2：
 * 输入：intervals = [[7,10],[2,4]]
 * 输出：1
 * 
 * 提示：
 * 1 <= intervals.length <= 10^4
 * 0 <= starti < endi <= 10^6
 * 
 * 解题思路：
 * 使用扫描线算法解决会议室安排问题。核心思想是：
 * 1. 将每个会议的开始和结束时间转换为事件点
 * 2. 对所有事件点按时间排序
 * 3. 扫描所有事件点，维护当前正在进行的会议数量
 * 4. 最大同时进行的会议数量就是所需的最小会议室数量
 * 
 * 时间复杂度：O(n log n)，主要来自排序
 * 空间复杂度：O(n)，存储事件点
 * 
 * 相关题目：
 * - LeetCode 252. 会议室
 * - LeetCode 56. 合并区间
 * - LeetCode 759. 员工空闲时间
 */
public class LeetCode_253_MeetingRoomsII {
    
    /**
     * 计算所需会议室数量的扫描线解法
     * @param intervals 会议时间安排数组
     * @return 所需的最小会议室数量
     */
    public static int minMeetingRooms(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        // 创建事件点列表：[时间, 类型]
        // 类型：0表示会议开始，1表示会议结束
        List<int[]> events = new ArrayList<>();
        
        // 为每个会议创建开始和结束事件
        for (int[] interval : intervals) {
            events.add(new int[]{interval[0], 0});  // 开始事件
            events.add(new int[]{interval[1], 1});  // 结束事件
        }
        
        // 按照时间排序事件点
        // 如果时间相同，结束事件优先于开始事件
        events.sort((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(b[1], a[1]);
        });
        
        int maxRooms = 0;
        int currentRooms = 0;
        
        // 扫描所有事件点
        for (int[] event : events) {
            if (event[1] == 0) {
                // 会议开始事件
                currentRooms++;
                maxRooms = Math.max(maxRooms, currentRooms);
            } else {
                // 会议结束事件
                currentRooms--;
            }
        }
        
        return maxRooms;
    }
    
    /**
     * 测试会议室II解法
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 253. 会议室II ===");
        
        // 测试用例1
        System.out.println("测试用例1:");
        int[][] intervals1 = {{0, 30}, {5, 10}, {15, 20}};
        int result1 = minMeetingRooms(intervals1);
        System.out.println("输入: " + Arrays.deepToString(intervals1));
        System.out.println("输出: " + result1);
        System.out.println("期望: 2");
        System.out.println();
        
        // 测试用例2
        System.out.println("测试用例2:");
        int[][] intervals2 = {{7, 10}, {2, 4}};
        int result2 = minMeetingRooms(intervals2);
        System.out.println("输入: " + Arrays.deepToString(intervals2));
        System.out.println("输出: " + result2);
        System.out.println("期望: 1");
        System.out.println();
        
        // 边界测试
        System.out.println("边界测试:");
        int[][] intervals3 = {{1, 2}, {2, 3}, {3, 4}};
        int result3 = minMeetingRooms(intervals3);
        System.out.println("输入: " + Arrays.deepToString(intervals3));
        System.out.println("输出: " + result3);
        System.out.println("期望: 1");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int n = 10000;
        int[][] largeIntervals = new int[n][2];
        
        for (int i = 0; i < n; i++) {
            int start = random.nextInt(1000000);
            int end = start + random.nextInt(1000) + 1;
            largeIntervals[i] = new int[]{start, end};
        }
        
        long startTime = System.nanoTime();
        int largeResult = minMeetingRooms(largeIntervals);
        long endTime = System.nanoTime();
        
        System.out.println("10000个会议的会议室计算完成");
        System.out.println("所需会议室数量: " + largeResult);
        System.out.println("运行时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 异常处理：处理空输入和null输入");
        System.out.println("2. 边界情况：处理时间边界和重叠情况");
        System.out.println("3. 性能优化：排序时间复杂度O(n log n)是最优解");
        System.out.println("4. 内存优化：使用最小额外空间存储事件点");
        
        // 算法复杂度分析
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("时间复杂度: O(n log n)");
        System.out.println("  - 排序: O(n log n)");
        System.out.println("  - 扫描: O(n)");
        System.out.println("空间复杂度: O(n)");
        System.out.println("  - 存储事件点: O(n)");
    }
}