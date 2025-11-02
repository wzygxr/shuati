package class094;

import java.util.*;

/**
 * 贪心算法总结与实战练习 (Greedy Algorithm Summary and Practice)
 * 包含贪心算法的核心思想、常见题型、解题模板和综合练习
 * 
 * 算法标签: 贪心算法(Greedy Algorithm)、算法总结(Algorithm Summary)、实战练习(Practice Exercises)
 * 时间复杂度: 各练习题不同，详见具体函数注释
 * 空间复杂度: 各练习题不同，详见具体函数注释
 * 相关内容: 核心思想总结、题型分类、解题模板、调试技巧
 * 贪心算法专题 - 总结与练习
 */
public class Code19_GreedySummaryAndPractice {

    /**
     * 贪心算法核心思想总结 (Greedy Algorithm Core Concepts Summary)
     * 
     * 1. 贪心选择性质：每一步都选择当前最优解，希望通过局部最优达到全局最优
     * 2. 最优子结构：问题的最优解包含其子问题的最优解
     * 3. 无后效性：当前选择不会影响后续选择的最优性
     * 
     * 适用场景详解：
     * - 活动选择问题（区间调度）：选择最多的不重叠活动
     * - 分数背包问题：可分割物品的背包问题
     * - 哈夫曼编码：数据压缩的最优前缀码
     * - 最短路径问题（Dijkstra算法）：单源最短路径
     * - 最小生成树（Prim、Kruskal算法）：连通图的最小权重生成树
     * 
     * 算法特点：
     * - 高效性：通常具有较低的时间复杂度
     * - 简洁性：算法逻辑相对简单
     * - 局限性：并非所有问题都具有贪心选择性质
     * 
     * 证明方法：
     * - 交换论证法：通过交换证明贪心选择的最优性
     * - 数学归纳法：证明最优子结构
     */
    
    /**
     * 常见贪心算法题型分类
     */
    public static class GreedyProblemTypes {
        
        /**
         * 类型1: 区间调度问题 (Interval Scheduling Problem)
         * 特征：需要选择不重叠的区间或活动
         * 解题模板：按结束时间排序，贪心选择结束最早的
         * 
         * 问题特点：
         * - 区间表示：[start, end]形式
         * - 优化目标：选择最多的不重叠区间
         * - 约束条件：区间不能重叠
         * 
         * 算法思路：
         * 1. 按结束时间升序排序
         * 2. 贪心选择结束时间最早的区间
         * 3. 排除与已选区间重叠的区间
         * 
         * 时间复杂度: O(n log n) - 排序时间
         * 空间复杂度: O(1) - 常数空间
         * 是否最优解: 是
         * 
         * 实际应用：
         * - 会议室调度
         * - 任务安排
         * - 广告投放
         */
        public static int intervalScheduling(int[][] intervals) {
            if (intervals == null || intervals.length == 0) return 0;
            
            Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
            
            int count = 1;
            int end = intervals[0][1];
            
            for (int i = 1; i < intervals.length; i++) {
                if (intervals[i][0] >= end) {
                    count++;
                    end = intervals[i][1];
                }
            }
            
            return count;
        }
        
        /**
         * 类型2: 资源分配问题 (Resource Allocation Problem)
         * 特征：有限资源分配给多个任务，最大化收益
         * 解题模板：按单位资源收益排序，贪心分配
         * 
         * 问题特点：
         * - 资源约束：总量有限的资源
         * - 任务属性：每个任务有成本和收益
         * - 优化目标：最大化总收益
         * 
         * 算法思路：
         * 1. 计算每个任务的价值密度（收益/成本）
         * 2. 按价值密度降序排序
         * 3. 贪心选择价值密度最高的任务
         * 
         * 时间复杂度: O(n log n) - 排序时间
         * 空间复杂度: O(1) - 常数空间
         * 是否最优解: 对于分数背包是最优解
         * 
         * 实际应用：
         * - 投资组合
         * - 任务调度
         * - 预算分配
         */
        public static int resourceAllocation(int[][] tasks, int resource) {
            if (tasks == null || tasks.length == 0 || resource <= 0) return 0;
            
            Arrays.sort(tasks, (a, b) -> Double.compare(
                (double) b[1] / b[0], (double) a[1] / a[0]
            ));
            
            int profit = 0;
            int remaining = resource;
            
            for (int[] task : tasks) {
                int cost = task[0];
                int value = task[1];
                
                if (remaining >= cost) {
                    profit += value;
                    remaining -= cost;
                } else {
                    profit += value * remaining / cost;
                    break;
                }
            }
            
            return profit;
        }
        
        /**
         * 类型3: 路径优化问题 (Path Optimization Problem)
         * 特征：在路径上选择最优停留点或加油点
         * 解题模板：维护当前可达范围，贪心选择最远可达点
         * 
         * 问题特点：
         * - 路径表示：一系列位置点
         * - 资源约束：每次移动消耗资源
         * - 优化目标：最小化停留次数
         * 
         * 算法思路：
         * 1. 维护当前可达范围
         * 2. 贪心选择能到达的最远点
         * 3. 更新可达范围和停留次数
         * 
         * 时间复杂度: O(n) - 一次遍历
         * 空间复杂度: O(1) - 常数空间
         * 是否最优解: 是
         * 
         * 实际应用：
         * - 加油站选址
         * - 网络路由
         * - 旅行规划
         */
        public static int pathOptimization(int[] positions, int range) {
            if (positions == null || positions.length == 0) return 0;
            
            int count = 0;
            int currentPos = 0;
            int farthest = 0;
            
            for (int i = 0; i < positions.length; i++) {
                if (positions[i] > farthest) {
                    if (currentPos >= positions.length - 1) break;
                    count++;
                    currentPos = farthest;
                    if (currentPos >= positions.length - 1) break;
                }
                farthest = Math.max(farthest, positions[i] + range);
            }
            
            return count;
        }
        
        /**
         * 类型4: 字符串重构问题 (String Reconstruction Problem)
         * 特征：重新排列字符串满足特定条件
         * 解题模板：使用单调栈或自定义排序
         * 
         * 问题特点：
         * - 字符约束：字符频率和排列限制
         * - 优化目标：满足特定条件的字符串
         * - 约束条件：如避免连续相同字符
         * 
         * 算法思路：
         * 1. 统计字符频率
         * 2. 使用优先队列维护字符优先级
         * 3. 贪心选择满足约束的字符
         * 
         * 时间复杂度: O(n log k) - k是不同字符数
         * 空间复杂度: O(k) - 字符频率存储
         * 是否最优解: 是
         * 
         * 实际应用：
         * - 密码生成
         * - 数据编码
         * - 字符串处理
         */
        public static String stringReconstruction(String s, int k) {
            if (s == null || s.isEmpty()) return "";
            
            // 统计字符频率
            int[] freq = new int[26];
            for (char c : s.toCharArray()) {
                freq[c - 'a']++;
            }
            
            // 构建结果字符串
            StringBuilder result = new StringBuilder();
            while (result.length() < s.length()) {
                // 选择当前可用的最大字符
                for (int i = 25; i >= 0; i--) {
                    if (freq[i] > 0) {
                        // 检查是否可以添加（避免连续k个相同字符）
                        int len = result.length();
                        if (len >= k - 1) {
                            boolean valid = true;
                            for (int j = 1; j < k; j++) {
                                if (result.charAt(len - j) != (char) ('a' + i)) {
                                    valid = false;
                                    break;
                                }
                            }
                            if (valid) continue;
                        }
                        
                        result.append((char) ('a' + i));
                        freq[i]--;
                        break;
                    }
                }
            }
            
            return result.toString();
        }
    }
    
    /**
     * 贪心算法解题模板
     */
    public static class GreedyTemplates {
        
        /**
         * 模板1: 排序 + 贪心选择 (Sort + Greedy Selection Template)
         * 适用：需要按某种规则排序后选择的问题
         * 
         * 模板特点：
         * - 预处理：通过排序建立选择顺序
         * - 选择策略：按排序后的顺序贪心选择
         * - 适用问题：具有明确排序规则的问题
         * 
         * 实现步骤：
         * 1. 根据问题特点设计排序规则
         * 2. 对数据进行排序
         * 3. 按排序顺序进行贪心选择
         * 
         * 时间复杂度: O(n log n) - 排序时间
         * 空间复杂度: O(1) - 常数空间
         * 
         * 典型应用：
         * - 区间调度
         * - 资源分配
         * - 任务选择
         */
        public static void templateSortAndGreedy(int[] arr) {
            // 1. 排序数组
            Arrays.sort(arr);
            
            // 2. 贪心选择
            int result = 0;
            for (int i = 0; i < arr.length; i++) {
                // 根据问题需求进行选择
                if (/* 满足选择条件 */ true) {
                    result += arr[i];
                }
            }
        }
        
        /**
         * 模板2: 堆 + 贪心选择 (Heap + Greedy Selection Template)
         * 适用：需要动态维护最优选择的问题
         * 
         * 模板特点：
         * - 数据结构：使用堆维护元素优先级
         * - 动态选择：实时获取最优元素
         * - 适用问题：需要动态调整选择策略
         * 
         * 实现步骤：
         * 1. 根据问题特点构建最大堆或最小堆
         * 2. 将元素加入堆中
         * 3. 动态获取并处理最优元素
         * 
         * 时间复杂度: O(n log n) - 堆操作
         * 空间复杂度: O(n) - 堆存储
         * 
         * 典型应用：
         * - 任务调度
         * - 资源管理
         * - 优先队列
         */
        public static void templateHeapAndGreedy(int[] arr) {
            // 1. 构建堆（最大堆或最小堆）
            PriorityQueue<Integer> heap = new PriorityQueue<>();
            for (int num : arr) {
                heap.offer(num);
            }
            
            // 2. 贪心选择
            int result = 0;
            while (!heap.isEmpty()) {
                int current = heap.poll();
                // 根据问题需求进行选择
                result += current;
            }
        }
        
        /**
         * 模板3: 双指针 + 贪心选择 (Two Pointers + Greedy Selection Template)
         * 适用：需要同时处理两个序列的问题
         * 
         * 模板特点：
         * - 双序列处理：同时遍历两个有序序列
         * - 指针移动：根据匹配条件移动指针
         * - 适用问题：序列匹配和优化问题
         * 
         * 实现步骤：
         * 1. 对两个序列进行排序
         * 2. 初始化双指针
         * 3. 根据匹配条件移动指针
         * 
         * 时间复杂度: O(n log n) - 排序时间
         * 空间复杂度: O(1) - 常数空间
         * 
         * 典型应用：
         * - 序列匹配
         * - 任务分配
         * - 优化选择
         */
        public static void templateTwoPointers(int[] arr1, int[] arr2) {
            // 1. 排序两个数组
            Arrays.sort(arr1);
            Arrays.sort(arr2);
            
            // 2. 双指针遍历
            int i = 0, j = 0;
            int result = 0;
            
            while (i < arr1.length && j < arr2.length) {
                if (/* 满足匹配条件 */ arr1[i] <= arr2[j]) {
                    result++;
                    i++;
                    j++;
                } else {
                    j++;
                }
            }
        }
    }
    
    /**
     * 综合实战练习题目 (Comprehensive Practice Problems)
     * 通过综合题目加深对贪心算法的理解和应用
     * 
     * 练习目标：
     * - 掌握不同类型问题的解法
     * - 理解算法间的联系和区别
     * - 提高问题分析和解决能力
     * 
     * 练习方法：
     * - 分析问题特点
     * - 选择合适模板
     * - 实现算法细节
     * - 验证结果正确性
     */
    
    /**
     * 练习1: 综合区间调度 (Comprehensive Interval Scheduling)
     * 结合多种区间问题的综合解法
     * 
     * 问题描述：
     * - 计算最多不重叠区间数
     * - 计算需要移除的最少区间数
     * - 合并所有重叠区间
     * 
     * 算法思路：
     * 1. 按结束时间排序解决区间选择
     * 2. 通过总数减去选择数得到移除数
     * 3. 遍历合并重叠区间
     * 
     * 时间复杂度: O(n log n) - 排序和遍历
     * 空间复杂度: O(n) - 存储合并结果
     * 是否最优解: 是
     * 
     * 实际应用：
     * - 会议安排优化
     * - 任务调度管理
     * - 资源分配规划
     */
    public static int comprehensiveIntervalProblems(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return 0;
        
        // 问题1: 最多不重叠区间数
        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        int nonOverlapCount = 1;
        int end = intervals[0][1];
        
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] >= end) {
                nonOverlapCount++;
                end = intervals[i][1];
            }
        }
        
        // 问题2: 需要移除的最少区间数
        int removeCount = intervals.length - nonOverlapCount;
        
        // 问题3: 合并所有重叠区间
        List<int[]> merged = new ArrayList<>();
        merged.add(intervals[0]);
        
        for (int i = 1; i < intervals.length; i++) {
            int[] last = merged.get(merged.size() - 1);
            if (intervals[i][0] <= last[1]) {
                last[1] = Math.max(last[1], intervals[i][1]);
            } else {
                merged.add(intervals[i]);
            }
        }
        
        return merged.size(); // 返回合并后的区间数
    }
    
    /**
     * 练习2: 资源分配综合问题 (Comprehensive Resource Allocation)
     * 多种资源分配策略的比较
     * 
     * 问题描述：
     * - 按价值排序策略
     * - 按价值密度排序策略
     * - 按重量排序策略
     * 
     * 算法思路：
     * 1. 实现三种不同排序策略
     * 2. 分别计算每种策略的结果
     * 3. 返回最优策略的结果
     * 
     * 时间复杂度: O(n log n) - 排序时间
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 实际应用：
     * - 投资组合优化
     * - 任务分配策略
     * - 预算管理方案
     */
    public static int comprehensiveResourceAllocation(int[][] items, int capacity) {
        if (items == null || items.length == 0 || capacity <= 0) return 0;
        
        // 策略1: 按价值排序（0-1背包贪心近似）
        Arrays.sort(items, (a, b) -> b[1] - a[1]);
        int valueStrategy = 0;
        int remaining = capacity;
        
        for (int[] item : items) {
            if (item[0] <= remaining) {
                valueStrategy += item[1];
                remaining -= item[0];
            }
        }
        
        // 策略2: 按价值密度排序（分数背包最优）
        Arrays.sort(items, (a, b) -> 
            Double.compare((double) b[1] / b[0], (double) a[1] / a[0])
        );
        
        int densityStrategy = 0;
        remaining = capacity;
        
        for (int[] item : items) {
            if (item[0] <= remaining) {
                densityStrategy += item[1];
                remaining -= item[0];
            } else {
                densityStrategy += item[1] * remaining / item[0];
                break;
            }
        }
        
        // 策略3: 按重量排序（尽量多装）
        Arrays.sort(items, (a, b) -> a[0] - b[0]);
        int weightStrategy = 0;
        remaining = capacity;
        
        for (int[] item : items) {
            if (item[0] <= remaining) {
                weightStrategy += item[1];
                remaining -= item[0];
            }
        }
        
        // 返回三种策略中的最大值
        return Math.max(valueStrategy, Math.max(densityStrategy, weightStrategy));
    }
    
    /**
     * 练习3: 字符串处理综合问题 (Comprehensive String Processing)
     * 多种字符串重构和优化问题
     * 
     * 问题描述：
     * - 去除重复字母并使字典序最小
     * - 重构字符串避免连续k个相同字符
     * 
     * 算法思路：
     * 1. 使用单调栈去除重复字母
     * 2. 使用优先队列重构字符串
     * 3. 确保满足连续字符约束
     * 
     * 时间复杂度: O(n log k) - k是不同字符数
     * 空间复杂度: O(k) - 字符频率存储
     * 是否最优解: 是
     * 
     * 实际应用：
     * - 密码生成优化
     * - 数据编码处理
     * - 字符串规范化
     */
    public static String comprehensiveStringProblems(String s, int k) {
        if (s == null || s.isEmpty()) return "";
        
        // 问题1: 去除重复字母并使字典序最小
        int[] lastPos = new int[26];
        for (int i = 0; i < s.length(); i++) {
            lastPos[s.charAt(i) - 'a'] = i;
        }
        
        boolean[] visited = new boolean[26];
        Deque<Character> stack = new ArrayDeque<>();
        
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (visited[c - 'a']) continue;
            
            while (!stack.isEmpty() && stack.peek() > c && 
                   lastPos[stack.peek() - 'a'] > i) {
                visited[stack.pop() - 'a'] = false;
            }
            
            stack.push(c);
            visited[c - 'a'] = true;
        }
        
        StringBuilder result = new StringBuilder();
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }
        String removeDuplicate = result.reverse().toString();
        
        // 问题2: 重构字符串避免连续k个相同字符
        int[] freq = new int[26];
        for (char c : removeDuplicate.toCharArray()) {
            freq[c - 'a']++;
        }
        
        PriorityQueue<Character> maxHeap = new PriorityQueue<>(
            (a, b) -> freq[b - 'a'] - freq[a - 'a']
        );
        
        for (char c = 'a'; c <= 'z'; c++) {
            if (freq[c - 'a'] > 0) {
                maxHeap.offer(c);
            }
        }
        
        StringBuilder finalResult = new StringBuilder();
        while (!maxHeap.isEmpty()) {
            List<Character> temp = new ArrayList<>();
            
            for (int i = 0; i < k && !maxHeap.isEmpty(); i++) {
                char current = maxHeap.poll();
                finalResult.append(current);
                freq[current - 'a']--;
                
                if (freq[current - 'a'] > 0) {
                    temp.add(current);
                }
            }
            
            for (char c : temp) {
                maxHeap.offer(c);
            }
        }
        
        return finalResult.toString();
    }
    
    /**
     * 贪心算法调试技巧 (Greedy Algorithm Debugging Techniques)
     * 提高算法实现正确性和效率的方法
     * 
     * 调试目标：
     * - 验证算法正确性
     * - 分析性能瓶颈
     * - 优化实现细节
     * 
     * 调试方法：
     * - 中间结果打印
     * - 测试用例验证
     * - 性能分析评估
     */
    public static class DebuggingTechniques {
        
        /**
         * 技巧1: 打印中间结果 (Print Intermediate Results)
         * 在关键步骤打印变量值，帮助理解算法执行过程
         * 
         * 调试价值：
         * - 追踪算法执行流程
         * - 验证中间状态正确性
         * - 发现逻辑错误位置
         * 
         * 实施方法：
         * 1. 在关键步骤添加打印语句
         * 2. 输出关键变量的值
         * 3. 分析输出结果
         * 
         * 注意事项：
         * - 避免打印过多信息
         * - 使用清晰的输出格式
         * - 及时移除调试代码
         */
        public static void printIntermediateResults(int[] arr) {
            System.out.println("原始数组: " + Arrays.toString(arr));
            
            // 排序后
            int[] sorted = arr.clone();
            Arrays.sort(sorted);
            System.out.println("排序后数组: " + Arrays.toString(sorted));
            
            // 贪心选择过程
            int sum = 0;
            for (int i = 0; i < sorted.length; i++) {
                sum += sorted[i];
                System.out.println("第" + (i + 1) + "步选择: " + sorted[i] + 
                                 ", 当前总和: " + sum);
            }
        }
        
        /**
         * 技巧2: 验证贪心选择正确性 (Verify Greedy Choice Correctness)
         * 通过小规模测试用例验证算法正确性
         * 
         * 验证方法：
         * - 设计边界测试用例
         * - 构造典型问题实例
         * - 比较期望与实际结果
         * 
         * 测试策略：
         * 1. 边界条件测试
         * 2. 典型场景测试
         * 3. 异常情况测试
         * 
         * 验证要点：
         * - 确保算法逻辑正确
         * - 验证结果符合预期
         * - 检查边界处理
         */
        public static boolean verifyGreedyChoice(int[][] testCases) {
            for (int[] testCase : testCases) {
                int expected = testCase[0]; // 期望结果
                int[] input = Arrays.copyOfRange(testCase, 1, testCase.length);
                
                // 执行贪心算法
                int actual = greedyAlgorithm(input);
                
                if (actual != expected) {
                    System.out.println("测试失败: 输入=" + Arrays.toString(input) +
                                     ", 期望=" + expected + ", 实际=" + actual);
                    return false;
                }
            }
            return true;
        }
        
        private static int greedyAlgorithm(int[] arr) {
            // 示例贪心算法实现
            Arrays.sort(arr);
            return arr.length > 0 ? arr[arr.length - 1] : 0;
        }
        
        /**
         * 技巧3: 性能分析 (Performance Analysis)
         * 分析算法的时间复杂度和空间复杂度
         * 
         * 分析内容：
         * - 时间复杂度评估
         * - 空间复杂度评估
         * - 实际运行时间
         * 
         * 分析方法：
         * 1. 理论复杂度计算
         * 2. 实际运行时间测量
         * 3. 瓶颈识别和优化
         * 
         * 优化建议：
         * - 选择高效数据结构
         * - 减少不必要的计算
         * - 优化算法实现
         */
        public static void analyzePerformance(int[] arr) {
            long startTime = System.nanoTime();
            
            // 执行算法
            Arrays.sort(arr);
            int result = 0;
            for (int num : arr) {
                result += num;
            }
            
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            
            System.out.println("算法执行时间: " + duration + " 纳秒");
            System.out.println("输入规模: " + arr.length);
            System.out.println("时间复杂度: O(n log n)");
            System.out.println("空间复杂度: O(1)");
        }
    }
    
    // 测试函数
    public static void main(String[] args) {
        // 测试综合区间调度
        int[][] intervals = {{1, 3}, {2, 4}, {3, 5}, {4, 6}};
        System.out.println("综合区间调度结果: " + comprehensiveIntervalProblems(intervals));
        
        // 测试资源分配综合问题
        int[][] items = {{2, 10}, {3, 15}, {5, 20}};
        System.out.println("资源分配结果: " + comprehensiveResourceAllocation(items, 7));
        
        // 测试调试技巧
        int[] testArray = {3, 1, 4, 1, 5};
        DebuggingTechniques.printIntermediateResults(testArray);
    }
}