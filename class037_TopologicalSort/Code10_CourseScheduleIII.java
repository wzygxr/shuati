package class060;

// 课程表 III
// 这里有 n 门不同的在线课程，按从 1 到 n 编号
// 给你一个数组 courses ，其中 courses[i] = [durationi, lastDayi]
// 表示第 i 门课将会持续上 durationi 天课，并且必须在不晚于 lastDayi 的时候完成
// 你的学期从第 1 天开始，且不能同时修读两门及两门以上的课程
// 返回你最多可以修读的课程数目
// 测试链接 : https://leetcode.cn/problems/course-schedule-iii/
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下所有代码，把主类名改成Main，可以直接通过

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 题目解析：
 * 这是一道贪心 + 优先队列的题目，虽然不是严格的拓扑排序，但涉及课程安排问题。
 * 我们需要在截止时间前尽可能多地完成课程。
 * 
 * 算法思路：
 * 1. 按照截止时间对课程进行排序
 * 2. 使用优先队列（最大堆）记录已选课程的持续时间
 * 3. 遍历课程，如果当前时间 + 课程持续时间 <= 截止时间，则直接选择
 * 4. 否则，如果队列中最大持续时间 > 当前课程持续时间，则替换
 * 
 * 时间复杂度：O(n log n)，排序和堆操作
 * 空间复杂度：O(n)，用于存储课程和优先队列
 * 
 * 相关题目扩展：
 * 1. LeetCode 630. 课程表 III - https://leetcode.cn/problems/course-schedule-iii/
 * 2. LeetCode 621. 任务调度器 - https://leetcode.cn/problems/task-scheduler/
 * 3. LeetCode 452. 用最少数量的箭引爆气球 - https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
 * 4. LeetCode 435. 无重叠区间 - https://leetcode.cn/problems/non-overlapping-intervals/
 * 
 * 工程化考虑：
 * 1. 边界处理：空输入、单课程等情况
 * 2. 性能优化：使用优先队列优化贪心策略
 * 3. 异常处理：验证输入数据的有效性
 * 4. 模块化设计：将排序、贪心选择分离
 */
public class Code10_CourseScheduleIII {

    public static int scheduleCourse(int[][] courses) {
        // 按照截止时间排序
        Arrays.sort(courses, (a, b) -> a[1] - b[1]);
        
        // 最大堆，存储已选课程的持续时间
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        int time = 0; // 当前已用时间
        
        for (int[] course : courses) {
            int duration = course[0];
            int lastDay = course[1];
            
            if (time + duration <= lastDay) {
                // 可以直接选择这门课程
                time += duration;
                maxHeap.offer(duration);
            } else if (!maxHeap.isEmpty() && maxHeap.peek() > duration) {
                // 替换掉持续时间最长的课程
                time = time - maxHeap.poll() + duration;
                maxHeap.offer(duration);
            }
        }
        
        return maxHeap.size();
    }

    public static void main(String[] args) {
        // 测试用例1
        int[][] courses1 = {{100, 200}, {200, 1300}, {1000, 1250}, {2000, 3200}};
        System.out.println(scheduleCourse(courses1)); // 输出: 3
        
        // 测试用例2
        int[][] courses2 = {{1, 2}};
        System.out.println(scheduleCourse(courses2)); // 输出: 1
        
        // 测试用例3
        int[][] courses3 = {{3, 2}, {4, 3}};
        System.out.println(scheduleCourse(courses3)); // 输出: 0
    }
}