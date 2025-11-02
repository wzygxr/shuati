package class060;

// 任务调度器 - 贪心+优先队列
// 给定一个字符数组表示任务，每个任务用大写字母表示，执行每个任务需要1单位时间
// 两个相同任务之间必须有长度为n的冷却时间
// 计算完成所有任务所需的最少时间
// 测试链接 : https://leetcode.cn/problems/task-scheduler/
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下所有代码，把主类名改成Main，可以直接通过

import java.util.*;

/**
 * 题目解析：
 * 这是任务调度问题的经典题目，虽然不是严格的拓扑排序，但涉及任务安排和间隔约束。
 * 使用贪心策略和优先队列来安排任务执行顺序。
 * 
 * 算法思路：
 * 1. 统计每个任务的频率
 * 2. 使用最大堆（优先队列）存储任务频率
 * 3. 每次选择频率最高的n+1个任务执行
 * 4. 更新剩余任务频率并重新加入堆中
 * 5. 重复直到所有任务完成
 * 
 * 时间复杂度：O(n log 26)，其中n是任务数量
 * 空间复杂度：O(26)
 * 
 * 相关题目扩展：
 * 1. LeetCode 621. 任务调度器 - https://leetcode.cn/problems/task-scheduler/
 * 2. LeetCode 358. 安排任务 - https://leetcode.cn/problems/rearrange-string-k-distance-apart/
 * 3. LeetCode 767. 重构字符串 - https://leetcode.cn/problems/reorganize-string/
 * 
 * 工程化考虑：
 * 1. 频率统计：使用数组统计任务出现次数
 * 2. 贪心策略：优先安排频率高的任务
 * 3. 冷却时间：处理任务间隔约束
 * 4. 边界处理：空任务、单任务等情况
 */
public class Code19_TaskScheduler {

    public static int leastInterval(char[] tasks, int n) {
        if (tasks.length == 0) return 0;
        if (n == 0) return tasks.length;
        
        // 统计任务频率
        int[] freq = new int[26];
        for (char task : tasks) {
            freq[task - 'A']++;
        }
        
        // 使用最大堆存储任务频率
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        for (int count : freq) {
            if (count > 0) {
                maxHeap.offer(count);
            }
        }
        
        int time = 0;
        
        while (!maxHeap.isEmpty()) {
            List<Integer> temp = new ArrayList<>();
            int cycle = n + 1; // 每个周期可以执行的任务数
            
            // 执行一个周期的任务
            for (int i = 0; i < cycle; i++) {
                if (!maxHeap.isEmpty()) {
                    int count = maxHeap.poll();
                    if (count > 1) {
                        temp.add(count - 1);
                    }
                }
                time++;
                
                // 如果堆为空且没有剩余任务，结束
                if (maxHeap.isEmpty() && temp.isEmpty()) {
                    break;
                }
            }
            
            // 将剩余任务重新加入堆中
            for (int count : temp) {
                maxHeap.offer(count);
            }
        }
        
        return time;
    }

    public static void main(String[] args) {
        // 测试用例1
        char[] tasks1 = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n1 = 2;
        System.out.println("测试用例1: " + leastInterval(tasks1, n1)); // 输出: 8
        
        // 测试用例2
        char[] tasks2 = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n2 = 0;
        System.out.println("测试用例2: " + leastInterval(tasks2, n2)); // 输出: 6
        
        // 测试用例3
        char[] tasks3 = {'A', 'A', 'A', 'A', 'A', 'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        int n3 = 2;
        System.out.println("测试用例3: " + leastInterval(tasks3, n3)); // 输出: 16
    }
}