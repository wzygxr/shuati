package class185.sweep_line_problems;

import java.util.*;

/**
 * LeetCode 759. 员工空闲时间 (Employee Free Time)
 * 
 * 题目来源：https://leetcode.cn/problems/employee-free-time/
 * 
 * 题目描述：
 * 给定员工的 schedule 列表，表示每个员工的工作时间。
 * 每个员工都有一个非重叠的时间段 Intervals 列表，这些时间段已经排好序。
 * 返回表示所有员工的共同，有限时间段的列表，且该时间段需均为空闲时间，同时该时间段需要按升序排列。
 * 
 * 示例 1：
 * 输入：schedule = [[[1,2],[5,6]],[[1,3]],[[4,10]]]
 * 输出：[[3,4]]
 * 解释：在所有员工工作时间之外的共同空闲时间是[3,4]
 * 
 * 示例 2：
 * 输入：schedule = [[[1,3],[6,7]],[[2,4]],[[2,5],[9,12]]]
 * 输出：[[5,6],[7,9]]
 * 
 * 提示：
 * 1 <= schedule.length , schedule[i].length <= 50
 * 0 <= schedule[i].start < schedule[i].end <= 10^8
 * 
 * 解题思路：
 * 使用扫描线算法解决员工空闲时间问题。核心思想是：
 * 1. 将所有员工的工作时间转换为事件点
 * 2. 对所有事件点按时间排序
 * 3. 扫描所有事件点，维护当前正在工作的员工数量
 * 4. 当员工数量从大于0变为0时，开始空闲时间；从0变为大于0时，结束空闲时间
 * 
 * 时间复杂度：O(n log n)，其中 n 是所有工作时间段的数量
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * - LeetCode 56. 合并区间
 * - LeetCode 253. 会议室II
 */
public class LeetCode_759_EmployeeFreeTime {
    
    // 定义区间类
    static class Interval {
        int start;
        int end;
        
        Interval() { start = 0; end = 0; }
        Interval(int s, int e) { start = s; end = e; }
    }
    
    /**
     * 员工空闲时间的扫描线解法
     * @param schedule 员工工作时间安排
     * @return 所有员工的共同空闲时间
     */
    public static List<Interval> employeeFreeTime(List<List<Interval>> schedule) {
        if (schedule == null || schedule.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 创建事件点列表：[时间, 类型]
        // 类型：0表示工作开始，1表示工作结束
        List<int[]> events = new ArrayList<>();
        
        // 为每个工作时间段创建开始和结束事件
        for (List<Interval> employee : schedule) {
            for (Interval interval : employee) {
                events.add(new int[]{interval.start, 0});  // 开始事件
                events.add(new int[]{interval.end, 1});    // 结束事件
            }
        }
        
        // 按照时间排序事件点
        // 如果时间相同，结束事件优先于开始事件
        events.sort((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(b[1], a[1]);
        });
        
        List<Interval> result = new ArrayList<>();
        int workingCount = 0;
        int freeStart = 0;
        
        // 扫描所有事件点
        for (int[] event : events) {
            int time = event[0];
            int type = event[1];
            
            if (type == 0) {
                // 工作开始事件
                if (workingCount == 0 && freeStart < time) {
                    // 结束空闲时间
                    result.add(new Interval(freeStart, time));
                }
                workingCount++;
            } else {
                // 工作结束事件
                workingCount--;
                if (workingCount == 0) {
                    // 开始空闲时间
                    freeStart = time;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 测试员工空闲时间解法
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 759. 员工空闲时间 ===");
        
        // 测试用例1
        System.out.println("测试用例1:");
        List<List<Interval>> schedule1 = new ArrayList<>();
        List<Interval> emp1 = Arrays.asList(new Interval(1, 2), new Interval(5, 6));
        List<Interval> emp2 = Arrays.asList(new Interval(1, 3));
        List<Interval> emp3 = Arrays.asList(new Interval(4, 10));
        schedule1.add(emp1);
        schedule1.add(emp2);
        schedule1.add(emp3);
        
        List<Interval> result1 = employeeFreeTime(schedule1);
        System.out.print("输入: ");
        for (List<Interval> emp : schedule1) {
            System.out.print("[");
            for (int i = 0; i < emp.size(); i++) {
                System.out.print("[" + emp.get(i).start + "," + emp.get(i).end + "]");
                if (i < emp.size() - 1) System.out.print(",");
            }
            System.out.print("] ");
        }
        System.out.println();
        System.out.print("输出: ");
        for (Interval interval : result1) {
            System.out.print("[" + interval.start + "," + interval.end + "] ");
        }
        System.out.println();
        System.out.println("期望: [[3,4]]");
        System.out.println();
        
        // 测试用例2
        System.out.println("测试用例2:");
        List<List<Interval>> schedule2 = new ArrayList<>();
        List<Interval> emp21 = Arrays.asList(new Interval(1, 3), new Interval(6, 7));
        List<Interval> emp22 = Arrays.asList(new Interval(2, 4));
        List<Interval> emp23 = Arrays.asList(new Interval(2, 5), new Interval(9, 12));
        schedule2.add(emp21);
        schedule2.add(emp22);
        schedule2.add(emp23);
        
        List<Interval> result2 = employeeFreeTime(schedule2);
        System.out.print("输入: ");
        for (List<Interval> emp : schedule2) {
            System.out.print("[");
            for (int i = 0; i < emp.size(); i++) {
                System.out.print("[" + emp.get(i).start + "," + emp.get(i).end + "]");
                if (i < emp.size() - 1) System.out.print(",");
            }
            System.out.print("] ");
        }
        System.out.println();
        System.out.print("输出: ");
        for (Interval interval : result2) {
            System.out.print("[" + interval.start + "," + interval.end + "] ");
        }
        System.out.println();
        System.out.println("期望: [[5,6],[7,9]]");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        List<List<Interval>> schedule = new ArrayList<>();
        int employeeCount = 50;
        int intervalsPerEmployee = 50;
        
        for (int i = 0; i < employeeCount; i++) {
            List<Interval> employee = new ArrayList<>();
            int currentTime = 0;
            for (int j = 0; j < intervalsPerEmployee; j++) {
                int start = currentTime + random.nextInt(1000);
                int end = start + random.nextInt(1000) + 1;
                employee.add(new Interval(start, end));
                currentTime = end + random.nextInt(1000);
            }
            schedule.add(employee);
        }
        
        long startTime = System.nanoTime();
        List<Interval> result = employeeFreeTime(schedule);
        long endTime = System.nanoTime();
        
        System.out.println("50个员工，每个员工50个工作时间段的空闲时间计算完成");
        System.out.println("共同空闲时间段数量: " + result.size());
        System.out.println("运行时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }
}