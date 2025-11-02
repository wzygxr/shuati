package class092;

import java.util.*;

/**
 * LeetCode 621. 任务调度器
 * 题目链接：https://leetcode.cn/problems/task-scheduler/
 * 难度：中等
 * 
 * 问题描述：
 * 给你一个用字符数组 tasks 表示的 CPU 需要执行的任务列表。其中每个字母表示一种不同种类的任务。任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。在任何一个单位时间，CPU 可以完成一个任务，或者处于待命状态。
 * 然而，两个相同种类的任务之间必须有长度为整数 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。
 * 你需要计算完成所有任务所需要的最短时间。
 * 
 * 示例：
 * 输入：tasks = ["A","A","A","B","B","B"], n = 2
 * 输出：8
 * 解释：A -> B -> (待命) -> A -> B -> (待命) -> A -> B
 *      在本示例中，两个相同类型任务之间必须间隔长度为 n = 2 的冷却时间，而执行一个任务只需要一个单位时间，所以中间出现了（待命）状态。 
 * 
 * 解题思路：
 * 贪心算法 + 优先队列（最大堆）
 * 1. 统计每个任务的频率
 * 2. 使用最大堆存储任务频率，确保每次优先处理频率最高的任务
 * 3. 维护一个时间计数器，在每个时间单位：
 *    a. 从堆中取出最多n+1个任务（确保同类型任务间隔n个时间单位）
 *    b. 将取出的任务频率减1，如果还有剩余频率则暂时保存
 *    c. 计算该时间片实际消耗的时间（如果有任务执行，消耗的时间等于取出的任务数；否则消耗1个时间单位）
 *    d. 将还有剩余频率的任务重新放回堆中
 * 4. 重复步骤3直到堆为空
 * 
 * 时间复杂度分析：
 * - 统计频率：O(n)
 * - 堆操作：O(m log k)，其中m是任务总数，k是不同任务的数量
 * 总体时间复杂度：O(n + m log k)
 * 
 * 空间复杂度分析：
 * - 统计频率的哈希表：O(k)
 * - 最大堆：O(k)
 * 总体空间复杂度：O(k)
 * 
 * 最优性证明：
 * 贪心策略确保每次处理剩余频率最高的任务，这样可以最小化空闲时间。通过优先处理高频任务，可以最大程度地填充任务之间的冷却时间，避免不必要的等待。
 */
public class Code19_TaskScheduler {
    
    /**
     * 计算完成所有任务所需的最短时间
     * @param tasks 任务数组
     * @param n 冷却时间
     * @return 最短时间
     */
    public int leastInterval(char[] tasks, int n) {
        // 特殊情况处理
        if (tasks == null || tasks.length == 0) {
            return 0;
        }
        
        if (n == 0) {
            return tasks.length;  // 没有冷却时间，直接返回任务数量
        }
        
        // 统计每个任务的频率
        Map<Character, Integer> taskCounts = new HashMap<>();
        for (char task : tasks) {
            taskCounts.put(task, taskCounts.getOrDefault(task, 0) + 1);
        }
        
        // 使用最大堆存储任务频率
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        maxHeap.addAll(taskCounts.values());
        
        int time = 0;  // 总时间计数器
        
        // 当堆不为空时继续处理
        while (!maxHeap.isEmpty()) {
            int currentTimeSlot = 0;  // 当前时间片中处理的任务数
            List<Integer> temp = new ArrayList<>();  // 临时保存本时间片中处理过的任务频率
            
            // 尝试在当前时间片（n+1个连续时间单位）中处理尽可能多的任务
            while (currentTimeSlot <= n && !maxHeap.isEmpty()) {
                int count = maxHeap.poll();  // 取出频率最高的任务
                count--;  // 执行一次该任务，频率减1
                
                if (count > 0) {  // 如果任务还有剩余次数，保存到临时列表
                    temp.add(count);
                }
                
                currentTimeSlot++;  // 当前时间片处理的任务数加1
            }
            
            // 将剩余任务放回堆中
            maxHeap.addAll(temp);
            
            // 计算本次时间片消耗的时间：
            // 如果堆不为空，说明还有任务需要处理，本次时间片消耗n+1个时间单位
            // 如果堆为空，说明所有任务都处理完了，本次时间片只消耗实际处理的任务数
            if (!maxHeap.isEmpty()) {
                time += (n + 1);
            } else {
                time += currentTimeSlot;
            }
        }
        
        return time;
    }
    
    /**
     * 优化解法：数学公式推导
     * 对于任务调度问题，最短时间由两个因素决定：
     * 1. 任务总数
     * 2. 由最高频率任务决定的最小时间
     * 最终结果是取两者的较大值
     * 
     * @param tasks 任务数组
     * @param n 冷却时间
     * @return 最短时间
     */
    public int leastIntervalOptimal(char[] tasks, int n) {
        // 特殊情况处理
        if (tasks == null || tasks.length == 0) {
            return 0;
        }
        
        if (n == 0) {
            return tasks.length;  // 没有冷却时间，直接返回任务数量
        }
        
        // 统计每个任务的频率
        int[] counts = new int[26];  // 假设任务只有26个大写字母
        int maxFreq = 0;  // 最高频率
        int maxFreqCount = 0;  // 具有最高频率的任务数量
        
        for (char task : tasks) {
            counts[task - 'A']++;
            maxFreq = Math.max(maxFreq, counts[task - 'A']);
        }
        
        // 统计有多少个任务具有最高频率
        for (int count : counts) {
            if (count == maxFreq) {
                maxFreqCount++;
            }
        }
        
        // 计算最小时间：由最高频率任务决定的最小时间
        int minTime = (maxFreq - 1) * (n + 1) + maxFreqCount;
        
        // 最终结果取任务总数和最小时间的较大值
        // 这是因为如果不同任务足够多，可能不需要任何空闲时间
        return Math.max(minTime, tasks.length);
    }
    
    /**
     * 测试代码
     */
    public static void main(String[] args) {
        Code19_TaskScheduler scheduler = new Code19_TaskScheduler();
        
        // 测试用例1：基本情况
        char[] tasks1 = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n1 = 2;
        int result1 = scheduler.leastInterval(tasks1, n1);
        int result1Optimal = scheduler.leastIntervalOptimal(tasks1, n1);
        System.out.println("测试用例1结果（普通解法）：" + result1 + "，期望：8");
        System.out.println("测试用例1结果（优化解法）：" + result1Optimal + "，期望：8");
        
        // 测试用例2：没有冷却时间
        char[] tasks2 = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n2 = 0;
        int result2 = scheduler.leastInterval(tasks2, n2);
        int result2Optimal = scheduler.leastIntervalOptimal(tasks2, n2);
        System.out.println("测试用例2结果（普通解法）：" + result2 + "，期望：6");
        System.out.println("测试用例2结果（优化解法）：" + result2Optimal + "，期望：6");
        
        // 测试用例3：只有一种任务
        char[] tasks3 = {'A', 'A', 'A', 'A', 'A', 'A'};
        int n3 = 2;
        int result3 = scheduler.leastInterval(tasks3, n3);
        int result3Optimal = scheduler.leastIntervalOptimal(tasks3, n3);
        System.out.println("测试用例3结果（普通解法）：" + result3 + "，期望：16");
        System.out.println("测试用例3结果（优化解法）：" + result3Optimal + "，期望：16");
        
        // 测试用例4：任务种类足够多，无需等待
        char[] tasks4 = {'A', 'A', 'A', 'B', 'B', 'B', 'C', 'C', 'D', 'D'};
        int n4 = 2;
        int result4 = scheduler.leastInterval(tasks4, n4);
        int result4Optimal = scheduler.leastIntervalOptimal(tasks4, n4);
        System.out.println("测试用例4结果（普通解法）：" + result4 + "，期望：10");
        System.out.println("测试用例4结果（优化解法）：" + result4Optimal + "，期望：10");
        
        // 测试用例5：边界情况 - 空数组
        char[] tasks5 = {};
        int n5 = 2;
        int result5 = scheduler.leastInterval(tasks5, n5);
        int result5Optimal = scheduler.leastIntervalOptimal(tasks5, n5);
        System.out.println("测试用例5结果（普通解法）：" + result5 + "，期望：0");
        System.out.println("测试用例5结果（优化解法）：" + result5Optimal + "，期望：0");
        
        // 性能测试
        System.out.println("\n性能测试：");
        char[] largeTasks = new char[10000];
        for (int i = 0; i < largeTasks.length; i++) {
            largeTasks[i] = (char) ('A' + (i % 10));  // 创建10种不同的任务
        }
        int largeN = 5;
        
        long startTime = System.currentTimeMillis();
        int largeResult = scheduler.leastInterval(largeTasks, largeN);
        long endTime = System.currentTimeMillis();
        System.out.println("大规模任务测试（普通解法）结果：" + largeResult + "，耗时：" + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int largeResultOptimal = scheduler.leastIntervalOptimal(largeTasks, largeN);
        endTime = System.currentTimeMillis();
        System.out.println("大规模任务测试（优化解法）结果：" + largeResultOptimal + "，耗时：" + (endTime - startTime) + "ms");
    }
}

/*
工程化考量：

1. 异常处理与边界情况：
   - 处理了空数组输入
   - 处理了冷却时间为0的特殊情况
   - 处理了只有一种任务的极端情况

2. 性能优化：
   - 提供了两种解法：基于优先队列的通用解法和基于数学公式的优化解法
   - 优化解法在时间和空间复杂度上都优于通用解法
   - 通用解法的时间复杂度为O(m log k)，空间复杂度为O(k)
   - 优化解法的时间复杂度为O(m)，空间复杂度为O(1)

3. 代码可读性：
   - 方法和变量命名清晰，符合Java命名规范
   - 提供了详细的注释，解释算法思路和关键步骤
   - 代码结构清晰，逻辑分明

4. 健壮性：
   - 测试用例覆盖了多种情况，包括基本情况、边界情况和特殊情况
   - 提供了性能测试，验证算法在大规模数据下的表现

5. 工程实践建议：
   - 在实际应用中，应优先使用优化解法，因为其性能更好
   - 如果任务类型不限于大写字母，可以使用HashMap代替固定大小的数组
   - 在多线程环境中，需要注意线程安全问题

6. 算法调试技巧：
   - 在关键步骤添加日志输出，如堆的状态、时间片的处理等
   - 使用小数据集测试，手动验证算法正确性
   - 比较不同解法的结果，确保一致性

7. 跨语言实现差异：
   - Java中使用PriorityQueue实现最大堆时需要使用Collections.reverseOrder()
   - 在Python中可以使用heapq模块，但需要将频率取负值以模拟最大堆
   - C++中可以直接使用priority_queue作为最大堆

8. 扩展性考虑：
   - 可以扩展支持任务优先级不同的情况
   - 可以扩展支持任务执行时间不同的情况
   - 可以扩展支持动态添加任务的情况

9. 从代码到产品的思考：
   - 在实际调度系统中，还需要考虑系统负载均衡
   - 可以添加监控和日志记录，便于问题诊断
   - 可以实现自适应调度策略，根据系统状态动态调整

10. 算法本质理解：
    - 该问题的核心是如何安排任务，使得同类型任务之间间隔至少n个时间单位
    - 贪心策略是每次选择当前剩余次数最多的任务执行，以最小化空闲时间
    - 数学公式解法基于对问题特性的深入分析，通过找到关键因素直接计算结果
*/