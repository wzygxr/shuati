import java.util.*;

public class Code14_TaskScheduler {
    """
    相关题目14: LeetCode 621. 任务调度器
    题目链接: https://leetcode.cn/problems/task-scheduler/
    题目描述: 给你一个用字符数组 tasks 表示的 CPU 需要执行的任务列表。其中每个字母表示一种不同种类的任务。
    任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。在任何一个单位时间，CPU 可以完成一个任务，或者处于待命状态。
    然而，两个相同种类的任务之间必须有长度为 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。
    请你计算完成所有任务所需要的最短时间。
    解题思路: 使用最大堆按任务频率排序，然后贪心安排任务
    时间复杂度: O(m log k)，其中m是总任务数，k是不同任务的数量（最大为26）
    空间复杂度: O(k)，用于存储任务频率和堆
    是否最优解: 此方法是最优解，还有数学公式可以直接计算
    
    本题属于堆的应用场景：任务调度问题
    """
    
    static class Solution {
        /**
         * 计算完成所有任务所需要的最短时间
         * 
         * @param tasks 任务列表，每个元素是一个字符表示的任务
         * @param n 相同任务之间的冷却时间
         * @return 完成所有任务所需的最短时间
         */
        public int leastInterval(char[] tasks, int n) {
            // 异常处理：检查输入参数
            if (tasks == null || tasks.length == 0) {
                return 0;
            }
            
            if (n < 0) {
                throw new IllegalArgumentException("冷却时间不能为负数");
            }
            
            // 统计每个任务的频率
            Map<Character, Integer> taskCounts = new HashMap<>();
            for (char task : tasks) {
                taskCounts.put(task, taskCounts.getOrDefault(task, 0) + 1);
            }
            
            // 创建最大堆
            PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
            maxHeap.addAll(taskCounts.values());
            
            // 当前时间
            int time = 0;
            
            // 当堆不为空时，继续安排任务
            while (!maxHeap.isEmpty()) {
                // 用于暂时存储本轮安排的任务（执行后需要放回堆中的任务）
                List<Integer> temp = new ArrayList<>();
                // 当前轮次需要安排的任务数（最多n+1个不同任务）
                int cycle = n + 1;
                
                // 尝试安排cycle个任务
                while (cycle > 0 && !maxHeap.isEmpty()) {
                    // 获取当前频率最高的任务
                    int count = maxHeap.poll();
                    // 如果任务执行后还有剩余次数，将剩余次数保存到temp中
                    if (count > 1) {
                        temp.add(count - 1);
                    }
                    // 时间增加1
                    time++;
                    // 减少本轮可安排的任务数
                    cycle--;
                }
                
                // 将本轮执行后还有剩余次数的任务放回堆中
                for (int count : temp) {
                    maxHeap.offer(count);
                }
                
                // 如果堆不为空，说明还有任务未完成，需要添加冷却时间
                // 即当前轮次剩下的cycle个时间单位都需要待命
                if (!maxHeap.isEmpty()) {
                    time += cycle;
                }
            }
            
            return time;
        }
    }
    
    static class MathSolution {
        /**
         * 使用数学公式直接计算最短时间的解决方案
         * 这种方法更高效，时间复杂度为O(m)，其中m是任务总数
         */
        public int leastInterval(char[] tasks, int n) {
            /**
             * 使用数学公式计算完成所有任务所需要的最短时间
             * 
             * 公式推导：
             * 1. 假设频率最高的任务的频率为max_freq
             * 2. 至少需要(max_freq - 1) * (n + 1) + 1的时间
             * 3. 如果有多个频率为max_freq的任务，需要加上这些任务的数量-1
             * 4. 最终结果取上述值和任务总数的最大值
             */
            // 异常处理
            if (tasks == null || tasks.length == 0) {
                return 0;
            }
            
            if (n < 0) {
                throw new IllegalArgumentException("冷却时间不能为负数");
            }
            
            // 统计任务频率
            Map<Character, Integer> taskCounts = new HashMap<>();
            for (char task : tasks) {
                taskCounts.put(task, taskCounts.getOrDefault(task, 0) + 1);
            }
            
            // 找到最高频率
            int maxFreq = 0;
            for (int count : taskCounts.values()) {
                maxFreq = Math.max(maxFreq, count);
            }
            
            // 计算有多少个任务有最高频率
            int maxFreqTasks = 0;
            for (int count : taskCounts.values()) {
                if (count == maxFreq) {
                    maxFreqTasks++;
                }
            }
            
            // 计算根据公式的最短时间
            // (maxFreq - 1) * (n + 1) 是安排maxFreq-1个批次的时间
            // 每个批次有n+1个时间单位（执行一个任务，然后冷却n个单位）
            // 最后加上maxFreqTasks个任务（最后一个批次）
            int formulaTime = (maxFreq - 1) * (n + 1) + maxFreqTasks;
            
            // 实际最短时间不能少于任务总数
            return Math.max(formulaTime, tasks.length);
        }
    }
    
    static class OptimizedHeapSolution {
        /**
         * 优化的堆实现，使用队列来跟踪冷却中的任务
         * 这种方法更直观地模拟了任务调度的过程
         */
        public int leastInterval(char[] tasks, int n) {
            // 异常处理
            if (tasks == null || tasks.length == 0) {
                return 0;
            }
            
            if (n < 0) {
                throw new IllegalArgumentException("冷却时间不能为负数");
            }
            
            // 统计任务频率
            Map<Character, Integer> taskCounts = new HashMap<>();
            for (char task : tasks) {
                taskCounts.put(task, taskCounts.getOrDefault(task, 0) + 1);
            }
            
            // 创建最大堆
            PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
            maxHeap.addAll(taskCounts.values());
            
            // 使用队列跟踪冷却中的任务
            // 每个元素是(剩余次数, 可用时间)
            Queue<int[]> cooldown = new LinkedList<>();
            
            int time = 0;
            
            while (!maxHeap.isEmpty() || !cooldown.isEmpty()) {
                // 将冷却时间到期的任务放回堆中
                while (!cooldown.isEmpty() && cooldown.peek()[1] == time) {
                    maxHeap.offer(cooldown.poll()[0]);
                }
                
                // 尝试执行一个任务
                if (!maxHeap.isEmpty()) {
                    // 获取并减少最高频率任务的次数
                    int count = maxHeap.poll();
                    count--;
                    
                    // 如果还有剩余次数，将其加入冷却队列
                    if (count > 0) {
                        // 可用时间 = 当前时间 + 冷却时间 + 1（执行时间）
                        int nextAvailableTime = time + n + 1;
                        cooldown.offer(new int[]{count, nextAvailableTime});
                    }
                }
                
                // 时间增加1
                time++;
            }
            
            return time;
        }
    }
    
    /**
     * 测试函数，验证算法在不同输入情况下的正确性
     */
    public static void testLeastInterval() {
        System.out.println("=== 测试任务调度器算法 ===");
        Solution heapSolution = new Solution();
        MathSolution mathSolution = new MathSolution();
        OptimizedHeapSolution optimizedSolution = new OptimizedHeapSolution();
        
        // 测试用例1：基本用例
        System.out.println("\n测试用例1：基本用例");
        char[] tasks1 = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n1 = 2;
        int result1Heap = heapSolution.leastInterval(tasks1, n1);
        int result1Math = mathSolution.leastInterval(tasks1, n1);
        int result1Opt = optimizedSolution.leastInterval(tasks1, n1);
        
        System.out.println("任务列表: " + Arrays.toString(tasks1) + ", 冷却时间: " + n1);
        System.out.println("堆方法结果: " + result1Heap);
        System.out.println("数学公式结果: " + result1Math);
        System.out.println("优化堆方法结果: " + result1Opt);
        
        // 测试用例2：所有任务都相同
        System.out.println("\n测试用例2：所有任务都相同");
        char[] tasks2 = {'A', 'A', 'A', 'A'};
        int n2 = 2;
        int result2Heap = heapSolution.leastInterval(tasks2, n2);
        int result2Math = mathSolution.leastInterval(tasks2, n2);
        int result2Opt = optimizedSolution.leastInterval(tasks2, n2);
        
        System.out.println("任务列表: " + Arrays.toString(tasks2) + ", 冷却时间: " + n2);
        System.out.println("堆方法结果: " + result2Heap);
        System.out.println("数学公式结果: " + result2Math);
        System.out.println("优化堆方法结果: " + result2Opt);
        
        // 测试用例3：冷却时间为0
        System.out.println("\n测试用例3：冷却时间为0");
        char[] tasks3 = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n3 = 0;
        int result3Heap = heapSolution.leastInterval(tasks3, n3);
        int result3Math = mathSolution.leastInterval(tasks3, n3);
        int result3Opt = optimizedSolution.leastInterval(tasks3, n3);
        
        System.out.println("任务列表: " + Arrays.toString(tasks3) + ", 冷却时间: " + n3);
        System.out.println("堆方法结果: " + result3Heap);
        System.out.println("数学公式结果: " + result3Math);
        System.out.println("优化堆方法结果: " + result3Opt);
        
        // 测试用例4：多种任务且频率不同
        System.out.println("\n测试用例4：多种任务且频率不同");
        char[] tasks4 = {'A', 'A', 'A', 'B', 'B', 'B', 'C', 'C', 'C', 'D', 'D'};
        int n4 = 2;
        int result4Heap = heapSolution.leastInterval(tasks4, n4);
        int result4Math = mathSolution.leastInterval(tasks4, n4);
        int result4Opt = optimizedSolution.leastInterval(tasks4, n4);
        
        System.out.println("任务列表: " + Arrays.toString(tasks4) + ", 冷却时间: " + n4);
        System.out.println("堆方法结果: " + result4Heap);
        System.out.println("数学公式结果: " + result4Math);
        System.out.println("优化堆方法结果: " + result4Opt);
        
        // 测试用例5：只有一种任务
        System.out.println("\n测试用例5：只有一种任务");
        char[] tasks5 = {'A'};
        int n5 = 100;
        int result5Heap = heapSolution.leastInterval(tasks5, n5);
        int result5Math = mathSolution.leastInterval(tasks5, n5);
        int result5Opt = optimizedSolution.leastInterval(tasks5, n5);
        
        System.out.println("任务列表: " + Arrays.toString(tasks5) + ", 冷却时间: " + n5);
        System.out.println("堆方法结果: " + result5Heap);
        System.out.println("数学公式结果: " + result5Math);
        System.out.println("优化堆方法结果: " + result5Opt);
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        
        // 创建一个大任务列表
        StringBuilder largeTasksBuilder = new StringBuilder();
        // 添加1000个A任务
        for (int i = 0; i < 1000; i++) {
            largeTasksBuilder.append('A');
        }
        // 添加500个B任务
        for (int i = 0; i < 500; i++) {
            largeTasksBuilder.append('B');
        }
        // 添加300个C任务
        for (int i = 0; i < 300; i++) {
            largeTasksBuilder.append('C');
        }
        // 添加200个D任务
        for (int i = 0; i < 200; i++) {
            largeTasksBuilder.append('D');
        }
        // 添加100个E任务
        for (int i = 0; i < 100; i++) {
            largeTasksBuilder.append('E');
        }
        
        char[] largeTasks = largeTasksBuilder.toString().toCharArray();
        int nLarge = 5;
        
        // 测试堆方法性能
        long startTime = System.currentTimeMillis();
        int largeResultHeap = heapSolution.leastInterval(largeTasks, nLarge);
        long heapTime = System.currentTimeMillis() - startTime;
        System.out.println("堆方法处理大任务列表用时: " + heapTime + "毫秒, 结果: " + largeResultHeap);
        
        // 测试数学公式方法性能
        startTime = System.currentTimeMillis();
        int largeResultMath = mathSolution.leastInterval(largeTasks, nLarge);
        long mathTime = System.currentTimeMillis() - startTime;
        System.out.println("数学公式方法处理大任务列表用时: " + mathTime + "毫秒, 结果: " + largeResultMath);
        
        // 测试优化堆方法性能
        startTime = System.currentTimeMillis();
        int largeResultOpt = optimizedSolution.leastInterval(largeTasks, nLarge);
        long optTime = System.currentTimeMillis() - startTime;
        System.out.println("优化堆方法处理大任务列表用时: " + optTime + "毫秒, 结果: " + largeResultOpt);
        
        // 性能比较
        System.out.println("\n性能比较:");
        System.out.println("堆方法 vs 数学公式方法: 数学公式方法更快 约 " + 
                          String.format("%.2f", (double)heapTime / mathTime) + "倍");
        System.out.println("堆方法 vs 优化堆方法: " + 
                          (optTime < heapTime ? "优化堆方法更快" : "堆方法更快") + " 约 " + 
                          String.format("%.2f", (double)Math.max(heapTime, optTime) / Math.min(heapTime, optTime)) + "倍");
        System.out.println("数学公式方法 vs 优化堆方法: 数学公式方法更快 约 " + 
                          String.format("%.2f", (double)optTime / mathTime) + "倍");
    }
    
    // 主方法
    public static void main(String[] args) {
        testLeastInterval();
    }
    
    /*
     * 解题思路总结：
     * 1. 问题分析：
     *    - 需要安排任务使得相同任务之间至少有n个冷却时间
     *    - 目标是找到完成所有任务的最短时间
     *    - 关键观察：频率最高的任务决定了整体调度的框架
     * 
     * 2. 堆方法（优先队列）：
     *    - 统计每个任务的频率
     *    - 将任务频率放入最大堆
     *    - 每次从堆中取出频率最高的任务执行
     *    - 执行后，如果任务还有剩余次数，将其保存并在冷却时间后放回堆中
     *    - 时间复杂度：O(m log k)，其中m是任务总数，k是不同任务的数量
     *    - 空间复杂度：O(k)
     * 
     * 3. 数学公式方法：
     *    - 观察到最短时间由两个因素决定：
     *      a) 最高频率任务所需要的时间框架
     *      b) 任务的总数
     *    - 公式：max( (maxFreq - 1) * (n + 1) + maxFreqTasks, totalTasks )
     *    - 时间复杂度：O(m)
     *    - 空间复杂度：O(k)
     *    - 这是最优解法
     * 
     * 4. 优化的堆实现（使用队列）：
     *    - 使用堆跟踪可执行的任务
     *    - 使用队列跟踪冷却中的任务
     *    - 每个时间单位，先检查冷却中的任务是否可以执行，然后执行一个任务（如果有）
     *    - 这种方法更直观地模拟了任务调度的过程
     *    - 时间复杂度：O(m log k)
     *    - 空间复杂度：O(k)
     * 
     * 5. 边界情况处理：
     *    - 空任务列表
     *    - 冷却时间为0
     *    - 只有一种任务
     *    - 任务数量少于最大频率 * (n + 1)
     * 
     * 6. 堆方法的优势：
     *    - 可以灵活处理不同优先级的任务调度
     *    - 能够直观地模拟任务执行和冷却的过程
     *    - 适用于更复杂的调度场景
     * 
     * 7. 应用场景：
     *    - CPU任务调度
     *    - 网络请求调度
     *    - 资源分配问题
     *    - 任何需要考虑冷却时间或优先级的调度问题
     */
}