package class091;

import java.util.*;

/**
 * 任务调度器
 * 
 * 题目描述：
 * 给定一个用字符数组表示的 CPU 需要执行的任务列表。其中包含使用大写的 A-Z 字母表示的26 种不同种类的任务。
 * 任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。
 * 在任何一个单位时间，CPU 可以完成一个任务，或者处于待命状态。
 * 然而，两个相同种类的任务之间必须有长度为 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。
 * 你需要计算完成所有任务所需要的最短时间。
 * 
 * 来源：LeetCode 621
 * 链接：https://leetcode.cn/problems/task-scheduler/
 * 
 * 相关题目链接：
 * https://leetcode.cn/problems/task-scheduler/ (LeetCode 621)
 * https://www.lintcode.com/problem/task-scheduler/ (LintCode 1482)
 * https://practice.geeksforgeeks.org/problems/task-scheduler/ (GeeksforGeeks)
 * https://www.nowcoder.com/practice/6b48f8c9d2cb4a568890b73383e119cf (牛客网)
 * https://codeforces.com/problemset/problem/1165/F2 (Codeforces)
 * https://atcoder.jp/contests/abc153/tasks/abc153_e (AtCoder)
 * https://www.hackerrank.com/challenges/task-scheduler/problem (HackerRank)
 * https://www.luogu.com.cn/problem/P1043 (洛谷)
 * https://vjudge.net/problem/HDU-2023 (HDU)
 * https://www.spoj.com/problems/TASKSCHED/ (SPOJ)
 * https://www.codechef.com/problems/TASKSCHEDULE (CodeChef)
 * 
 * 算法思路：
 * 使用贪心算法 + 桶思想：
 * 1. 统计每个任务的出现频率
 * 2. 找到出现次数最多的任务，假设出现次数为 maxCount
 * 3. 计算至少需要的时间：maxCount + (maxCount - 1) * n
 * 4. 如果存在多个任务出现次数都为 maxCount，需要额外加上这些任务
 * 5. 最终结果为 max(总任务数, 计算出的最小时间)
 * 
 * 时间复杂度：O(n) - 需要遍历任务数组统计频率
 * 空间复杂度：O(1) - 使用固定大小的数组存储频率（26个字母）
 * 
 * 关键点分析：
 * - 桶思想：将任务分配到桶中，每个桶的大小为 n+1
 * - 贪心策略：优先安排出现次数最多的任务
 * - 边界处理：n=0 的特殊情况
 * 
 * 工程化考量：
 * - 输入验证：检查任务数组是否为空
 * - 边界处理：处理 n=0 的情况
 * - 性能优化：使用数组而非HashMap统计频率
 */
public class Code27_TaskScheduler {
    
    /**
     * 计算完成任务的最短时间
     * 
     * @param tasks 任务数组
     * @param n 冷却时间
     * @return 最短完成时间
     */
    public static int leastInterval(char[] tasks, int n) {
        // 输入验证
        if (tasks == null || tasks.length == 0) {
            return 0;
        }
        
        // 特殊情况：如果冷却时间为0，可以直接执行所有任务
        if (n == 0) {
            return tasks.length;
        }
        
        // 统计每个任务的频率
        int[] freq = new int[26];
        for (char task : tasks) {
            freq[task - 'A']++;
        }
        
        // 找到最大频率
        int maxFreq = 0;
        for (int count : freq) {
            maxFreq = Math.max(maxFreq, count);
        }
        
        // 统计有多少个任务具有最大频率
        int maxCount = 0;
        for (int count : freq) {
            if (count == maxFreq) {
                maxCount++;
            }
        }
        
        // 计算最小时间
        // 公式：maxCount + (maxFreq - 1) * (n + 1)
        // 解释：第一个桶需要 maxCount 个位置，后面每个桶需要 n+1 个位置
        int minTime = (maxFreq - 1) * (n + 1) + maxCount;
        
        // 如果任务数量大于计算出的最小时间，说明需要更多时间
        return Math.max(minTime, tasks.length);
    }
    
    /**
     * 使用优先队列的解法（另一种思路）
     * 时间复杂度：O(n * log26) ≈ O(n)
     * 空间复杂度：O(26) ≈ O(1)
     */
    public static int leastIntervalWithPQ(char[] tasks, int n) {
        if (tasks == null || tasks.length == 0) {
            return 0;
        }
        if (n == 0) {
            return tasks.length;
        }
        
        // 统计频率
        int[] freq = new int[26];
        for (char task : tasks) {
            freq[task - 'A']++;
        }
        
        // 使用最大堆存储频率
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        for (int count : freq) {
            if (count > 0) {
                maxHeap.offer(count);
            }
        }
        
        int time = 0;
        // 用于存储当前周期内执行的任务
        Queue<int[]> coolingQueue = new LinkedList<>();
        
        while (!maxHeap.isEmpty() || !coolingQueue.isEmpty()) {
            time++;
            
            // 从最大堆中取出一个任务执行
            if (!maxHeap.isEmpty()) {
                int count = maxHeap.poll();
                count--;
                if (count > 0) {
                    // 将任务加入冷却队列，记录冷却结束时间
                    coolingQueue.offer(new int[]{count, time + n});
                }
            }
            
            // 检查冷却队列中是否有任务可以重新加入最大堆
            while (!coolingQueue.isEmpty() && coolingQueue.peek()[1] <= time) {
                maxHeap.offer(coolingQueue.poll()[0]);
            }
        }
        
        return time;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1: tasks = ["A","A","A","B","B","B"], n = 2
        // 期望输出: 8
        // 解释: A -> B -> (待命) -> A -> B -> (待命) -> A -> B
        char[] tasks1 = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n1 = 2;
        System.out.println("测试用例1:");
        System.out.println("任务: " + Arrays.toString(tasks1) + ", n = " + n1);
        System.out.println("结果1: " + leastInterval(tasks1, n1)); // 期望: 8
        System.out.println("结果2: " + leastIntervalWithPQ(tasks1, n1)); // 期望: 8
        
        // 测试用例2: tasks = ["A","A","A","B","B","B"], n = 0
        // 期望输出: 6
        char[] tasks2 = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n2 = 0;
        System.out.println("\n测试用例2:");
        System.out.println("任务: " + Arrays.toString(tasks2) + ", n = " + n2);
        System.out.println("结果: " + leastInterval(tasks2, n2)); // 期望: 6
        
        // 测试用例3: tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
        // 期望输出: 16
        char[] tasks3 = {'A','A','A','A','A','A','B','C','D','E','F','G'};
        int n3 = 2;
        System.out.println("\n测试用例3:");
        System.out.println("任务: " + Arrays.toString(tasks3) + ", n = " + n3);
        System.out.println("结果: " + leastInterval(tasks3, n3)); // 期望: 16
        
        // 测试用例4: 空数组
        char[] tasks4 = {};
        int n4 = 2;
        System.out.println("\n测试用例4:");
        System.out.println("任务: " + Arrays.toString(tasks4) + ", n = " + n4);
        System.out.println("结果: " + leastInterval(tasks4, n4)); // 期望: 0
        
        // 边界测试：单个任务
        char[] tasks5 = {'A'};
        int n5 = 3;
        System.out.println("\n测试用例5:");
        System.out.println("任务: " + Arrays.toString(tasks5) + ", n = " + n5);
        System.out.println("结果: " + leastInterval(tasks5, n5)); // 期望: 1
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        // 生成大规模测试数据
        char[] largeTasks = new char[10000];
        Random random = new Random();
        for (int i = 0; i < largeTasks.length; i++) {
            largeTasks[i] = (char) ('A' + random.nextInt(26));
        }
        int n = 10;
        
        long startTime = System.currentTimeMillis();
        int result = leastInterval(largeTasks, n);
        long endTime = System.currentTimeMillis();
        
        System.out.println("大规模测试结果: " + result);
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
    }
}