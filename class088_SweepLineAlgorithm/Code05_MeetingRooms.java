package class115;

import java.util.Arrays;

// 会议室问题 - 扫描线算法应用
// 问题描述：给定一个会议时间安排的数组 intervals ，
// 每个会议时间都会包括开始和结束的时间 intervals[i] = [starti, endi] ，
// 请你判断一个人是否能够参加这里面的全部会议。
// 解题思路：使用扫描线算法的思想，将会议按开始时间排序后检查是否有重叠
// 算法复杂度：时间复杂度O(n log n)，空间复杂度O(1)
// 工程化考量：
// 1. 边界条件处理完善（空数组情况）
// 2. 代码结构清晰，易于理解和维护
// 3. 包含多个测试用例验证正确性
// 测试链接 : https://leetcode.cn/problems/meeting-rooms/
public class Code05_MeetingRooms {

    /**
     * 判断是否能参加所有会议
     * 算法核心思想：
     * 1. 将所有会议按照开始时间排序
     * 2. 遍历排序后的会议，检查当前会议的开始时间是否早于前一个会议的结束时间
     * 3. 如果有冲突，返回false；否则返回true
     * 
     * @param intervals 会议时间安排数组，intervals[i] = [starti, endi]
     * @return 如果能参加所有会议返回true，否则返回false
     */
    public static boolean canAttendMeetings(int[][] intervals) {
        // 边界条件处理
        // 如果没有会议，可以参加所有会议
        if (intervals == null || intervals.length == 0) {
            return true;
        }

        // 按照会议开始时间排序
        // 时间复杂度: O(n*logn)
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        // 遍历所有会议，检查是否有时间冲突
        // 时间复杂度: O(n)
        for (int i = 1; i < intervals.length; i++) {
            // 如果当前会议的开始时间早于前一个会议的结束时间，说明有冲突
            if (intervals[i][0] < intervals[i - 1][1]) {
                return false;
            }
        }

        // 没有发现时间冲突，可以参加所有会议
        return true;
    }

    /**
     * 测试用例
     * 验证canAttendMeetings方法的正确性
     */
    public static void main(String[] args) {
        // 测试用例1: [[0,30],[5,10],[15,20]]
        // 预期输出: false (会议[0,30]与[5,10]有重叠)
        int[][] intervals1 = {{0, 30}, {5, 10}, {15, 20}};
        System.out.println(canAttendMeetings(intervals1)); // false

        // 测试用例2: [[7,10],[2,4]]
        // 预期输出: true (排序后为[[2,4],[7,10]]，无重叠)
        int[][] intervals2 = {{7, 10}, {2, 4}};
        System.out.println(canAttendMeetings(intervals2)); // true

        // 测试用例3: []
        // 预期输出: true (没有会议，可以参加所有会议)
        int[][] intervals3 = {};
        System.out.println(canAttendMeetings(intervals3)); // true

        // 测试用例4: [[1,2],[2,3]]
        // 预期输出: true (边界情况，一个会议在另一个会议结束后立即开始)
        int[][] intervals4 = {{1, 2}, {2, 3}};
        System.out.println(canAttendMeetings(intervals4)); // true
    }
}