package class093;

import java.util.*;

/**
 * 任务调度器（Task Scheduler）
 * 题目来源：LeetCode 621
 * 题目链接：https://leetcode.cn/problems/task-scheduler/
 * 
 * 问题描述：
 * 给定一个用字符数组表示的CPU需要执行的任务列表。其中包含使用大写的A-Z字母表示的26种不同种类的任务。
 * 任务可以以任意顺序执行，并且每个任务都可以在1个单位时间内执行完。
 * CPU在任何一个单位时间内都可以执行一个任务，或者在待命状态。
 * 然而，两个相同种类的任务之间必须有长度为n的冷却时间，因此至少有连续n个单位时间内CPU在执行不同的任务，或者在待命状态。
 * 你需要计算完成所有任务所需要的最短时间。
 * 
 * 算法思路：
 * 使用贪心策略，基于任务频率：
 * 1. 统计每个任务的频率
 * 2. 找到出现次数最多的任务，假设出现次数为maxCount
 * 3. 计算至少需要的时间：(maxCount - 1) * (n + 1) + 出现次数为maxCount的任务个数
 * 4. 如果计算结果小于任务总数，说明可以安排得更紧凑，返回任务总数
 * 
 * 时间复杂度：O(n) - 统计频率的时间复杂度
 * 空间复杂度：O(1) - 只需要常数空间存储频率
 * 
 * 是否最优解：是。这是该问题的最优解法。
 * 
 * 适用场景：
 * 1. 任务调度问题
 * 2. 资源分配问题
 * 
 * 异常处理：
 * 1. 处理空数组情况
 * 2. 处理n=0的情况
 * 
 * 工程化考量：
 * 1. 输入验证：检查参数是否合法
 * 2. 边界条件：处理单任务情况
 * 3. 性能优化：使用数组统计频率
 * 
 * 相关题目：
 * 1. LeetCode 767. 重构字符串 - 类似的任务安排问题
 * 2. LeetCode 358. 重排字符串k距离 apart - 更一般的任务调度
 * 3. LeetCode 253. 会议室 II - 区间调度问题
 * 4. 牛客网 NC140 排序 - 各种排序算法实现
 * 5. LintCode 945. 任务计划 - 类似问题
 * 6. HackerRank - Task Scheduling - 任务调度问题
 * 7. CodeChef - TASKS - 任务分配问题
 * 8. AtCoder ABC131D - Megalomania - 任务截止时间问题
 * 9. Codeforces 1324C - Frog Jumps - 贪心跳跃问题
 * 10. POJ 1700 - Crossing River - 经典过河问题
 */
public class Code19_TaskScheduler {
    
    /**
     * 计算完成任务的最短时间
     * 
     * @param tasks 任务数组
     * @param n 冷却时间
     * @return 最短完成时间
     */
    public static int leastInterval(char[] tasks, int n) {
        // 边界条件检查
        if (tasks == null || tasks.length == 0) {
            return 0;
        }
        
        if (n == 0) {
            return tasks.length; // 没有冷却时间，直接按顺序执行
        }
        
        // 统计任务频率
        int[] frequency = new int[26];
        for (char task : tasks) {
            frequency[task - 'A']++;
        }
        
        // 找到最大频率
        int maxFrequency = 0;
        for (int freq : frequency) {
            maxFrequency = Math.max(maxFrequency, freq);
        }
        
        // 统计出现最大频率的任务个数
        int maxCount = 0;
        for (int freq : frequency) {
            if (freq == maxFrequency) {
                maxCount++;
            }
        }
        
        // 计算最短时间
        int result = (maxFrequency - 1) * (n + 1) + maxCount;
        
        // 如果计算结果小于任务总数，说明可以安排得更紧凑
        return Math.max(result, tasks.length);
    }
    
    /**
     * 测试函数，验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1: 基本情况
        char[] tasks1 = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n1 = 2;
        int result1 = leastInterval(tasks1, n1);
        System.out.println("测试用例1:");
        System.out.println("任务数组: " + Arrays.toString(tasks1));
        System.out.println("冷却时间: " + n1);
        System.out.println("最短完成时间: " + result1);
        System.out.println("期望输出: 8");
        System.out.println();
        
        // 测试用例2: 简单情况
        char[] tasks2 = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n2 = 0;
        int result2 = leastInterval(tasks2, n2);
        System.out.println("测试用例2:");
        System.out.println("任务数组: " + Arrays.toString(tasks2));
        System.out.println("冷却时间: " + n2);
        System.out.println("最短完成时间: " + result2);
        System.out.println("期望输出: 6");
        System.out.println();
        
        // 测试用例3: 复杂情况
        char[] tasks3 = {'A', 'A', 'A', 'A', 'A', 'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        int n3 = 2;
        int result3 = leastInterval(tasks3, n3);
        System.out.println("测试用例3:");
        System.out.println("任务数组: " + Arrays.toString(tasks3));
        System.out.println("冷却时间: " + n3);
        System.out.println("最短完成时间: " + result3);
        System.out.println("期望输出: 16");
        System.out.println();
        
        // 测试用例4: 边界情况 - 单任务
        char[] tasks4 = {'A'};
        int n4 = 3;
        int result4 = leastInterval(tasks4, n4);
        System.out.println("测试用例4:");
        System.out.println("任务数组: " + Arrays.toString(tasks4));
        System.out.println("冷却时间: " + n4);
        System.out.println("最短完成时间: " + result4);
        System.out.println("期望输出: 1");
        System.out.println();
        
        // 测试用例5: 边界情况 - 空数组
        char[] tasks5 = {};
        int n5 = 5;
        int result5 = leastInterval(tasks5, n5);
        System.out.println("测试用例5:");
        System.out.println("任务数组: " + Arrays.toString(tasks5));
        System.out.println("冷却时间: " + n5);
        System.out.println("最短完成时间: " + result5);
        System.out.println("期望输出: 0");
        System.out.println();
        
        // 测试用例6: 复杂情况 - 多个相同频率
        char[] tasks6 = {'A', 'A', 'A', 'B', 'B', 'B', 'C', 'C', 'C', 'D', 'D', 'E'};
        int n6 = 2;
        int result6 = leastInterval(tasks6, n6);
        System.out.println("测试用例6:");
        System.out.println("任务数组: " + Arrays.toString(tasks6));
        System.out.println("冷却时间: " + n6);
        System.out.println("最短完成时间: " + result6);
        System.out.println("期望输出: 12");
    }
}