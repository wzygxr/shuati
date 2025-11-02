package class115;

import java.util.Arrays;
import java.util.PriorityQueue;

// 会议室 II - 扫描线算法应用
// 问题描述：给你一个会议时间安排的数组 intervals ，
// 每个会议时间包括开始和结束的时间 intervals[i] = [starti, endi] ，
// 返回所需会议室的最小数量。
// 解题思路：使用扫描线算法结合最小堆来计算所需会议室的最小数量
// 算法复杂度：时间复杂度O(n log n)，空间复杂度O(n)
// 工程化考量：
// 1. 边界条件处理完善（空数组情况）
// 2. 使用优先队列优化会议室分配策略
// 3. 代码结构清晰，易于理解和维护
// 4. 包含多个测试用例验证正确性
// 测试链接 : https://leetcode.cn/problems/meeting-rooms-ii/
public class Code06_MeetingRoomsII {

    /**
     * 计算所需会议室的最小数量
     * 算法核心思想：
     * 1. 将所有会议按照开始时间排序
     * 2. 使用最小堆维护当前正在使用的会议室的结束时间
     * 3. 遍历排序后的会议:
     *    - 如果堆顶的结束时间小于等于当前会议的开始时间，说明有会议室空闲，可以复用
     *    - 否则需要新的会议室
     * 4. 堆的大小就是所需的最少会议室数量
     * 
     * @param intervals 会议时间安排数组，intervals[i] = [starti, endi]
     * @return 所需会议室的最小数量
     */
    public static int minMeetingRooms(int[][] intervals) {
        // 边界条件处理
        if (intervals == null || intervals.length == 0) {
            return 0;
        }

        // 按照会议开始时间排序
        // 时间复杂度: O(n*logn)
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        // 使用最小堆维护当前正在使用的会议室的结束时间
        // 堆顶是最早结束的会议室
        PriorityQueue<Integer> heap = new PriorityQueue<>();

        // 遍历所有会议
        // 时间复杂度: O(n*logn)
        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];

            // 如果堆不为空且堆顶的结束时间小于等于当前会议的开始时间
            // 说明有会议室空闲，可以复用
            if (!heap.isEmpty() && heap.peek() <= start) {
                heap.poll(); // 释放会议室
            }

            // 当前会议需要占用一个会议室
            heap.offer(end);
        }

        // 堆的大小就是所需的最少会议室数量
        return heap.size();
    }

    /**
     * 测试用例
     * 验证minMeetingRooms方法的正确性
     */
    public static void main(String[] args) {
        // 测试用例1: [[0,30],[5,10],[15,20]]
        // 预期输出: 2
        // 解释: 会议[0,30]需要一个会议室，会议[5,10]与[0,30]重叠需要另一个会议室，
        //      会议[15,20]与[0,30]重叠但与[5,10]不重叠，可以复用[5,10]的会议室
        int[][] intervals1 = {{0, 30}, {5, 10}, {15, 20}};
        System.out.println(minMeetingRooms(intervals1)); // 2

        // 测试用例2: [[7,10],[2,4]]
        // 预期输出: 1
        // 解释: 排序后为[[2,4],[7,10]]，两个会议不重叠，只需要一个会议室
        int[][] intervals2 = {{7, 10}, {2, 4}};
        System.out.println(minMeetingRooms(intervals2)); // 1

        // 测试用例3: [[9,10],[4,9],[4,17]]
        // 预期输出: 2
        // 解释: 排序后为[[4,9],[4,17],[9,10]]，会议[4,9]和[4,17]重叠需要两个会议室，
        //      会议[9,10]可以复用[4,9]的会议室
        int[][] intervals3 = {{9, 10}, {4, 9}, {4, 17}};
        System.out.println(minMeetingRooms(intervals3)); // 2

        // 测试用例4: []
        // 预期输出: 0
        // 解释: 没有会议，不需要会议室
        int[][] intervals4 = {};
        System.out.println(minMeetingRooms(intervals4)); // 0
    }
}