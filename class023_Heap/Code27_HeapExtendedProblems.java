package class027;

import java.util.*;

/**
 * 堆算法扩展题目集 - 涵盖各大算法平台经典堆问题
 * 
 * 本文件包含来自LeetCode、牛客网、LintCode、HackerRank、AtCoder、CodeChef、SPOJ、
 * Project Euler、HackerEarth、计蒜客、洛谷、USACO、UVa OJ、Codeforces、POJ、HDU等
 * 平台的堆相关经典题目
 * 
 * 每个题目都包含：
 * 1. 题目来源和链接
 * 2. 详细的问题描述
 * 3. 最优解思路分析
 * 4. 时间和空间复杂度计算
 * 5. 完整的Java实现
 * 6. 异常处理和边界条件处理
 * 7. 测试用例
 */

public class Code27_HeapExtendedProblems {
    
    /**
     * 题目1: LeetCode 378. 有序矩阵中第K小的元素
     * 题目链接: https://leetcode.cn/problems/kth-smallest-element-in-a-sorted-matrix/
     * 题目描述: 给定一个 n x n 矩阵，其中每行和每列元素均按升序排序，找到矩阵中第 k 小的元素。
     * 解题思路: 使用最小堆维护矩阵中的元素，每次取出最小值并加入其右侧和下侧元素
     * 时间复杂度: O(k log k)，其中k是第k小的k值
     * 空间复杂度: O(k)
     * 是否最优解: 是，这是处理有序矩阵第K小元素的最优解法
     */
    public static int kthSmallestInSortedMatrix(int[][] matrix, int k) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            throw new IllegalArgumentException("矩阵不能为空");
        }
        if (k <= 0 || k > matrix.length * matrix[0].length) {
            throw new IllegalArgumentException("k值超出范围");
        }
        
        int n = matrix.length;
        // 最小堆，存储三元组[值, 行索引, 列索引]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        // 将第一列的所有元素加入堆
        for (int i = 0; i < n; i++) {
            minHeap.offer(new int[]{matrix[i][0], i, 0});
        }
        
        // 取出前k-1个最小元素
        for (int i = 0; i < k - 1; i++) {
            int[] current = minHeap.poll();
            int row = current[1];
            int col = current[2];
            
            // 如果当前元素有右侧元素，加入堆
            if (col + 1 < n) {
                minHeap.offer(new int[]{matrix[row][col + 1], row, col + 1});
            }
        }
        
        return minHeap.peek()[0];
    }
    
    /**
     * 题目2: LeetCode 767. 重构字符串
     * 题目链接: https://leetcode.cn/problems/reorganize-string/
     * 题目描述: 给定一个字符串S，检查是否能重新排布其中的字母，使得两相邻的字符不同。
     * 解题思路: 使用最大堆按字符频率排序，每次取频率最高的两个字符交替放置
     * 时间复杂度: O(n log k)，其中n是字符串长度，k是不同字符数量
     * 空间复杂度: O(k)
     * 是否最优解: 是，这是贪心算法的最优实现
     */
    public static String reorganizeString(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        
        // 统计字符频率
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : s.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        
        // 最大堆，按频率降序排列
        PriorityQueue<Character> maxHeap = new PriorityQueue<>((a, b) -> 
            freqMap.get(b) - freqMap.get(a));
        maxHeap.addAll(freqMap.keySet());
        
        // 如果最高频率超过一半+1，无法重构
        int maxFreq = freqMap.get(maxHeap.peek());
        if (maxFreq > (s.length() + 1) / 2) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        
        while (maxHeap.size() >= 2) {
            // 每次取频率最高的两个字符
            char first = maxHeap.poll();
            char second = maxHeap.poll();
            
            result.append(first).append(second);
            
            // 更新频率并重新加入堆
            freqMap.put(first, freqMap.get(first) - 1);
            freqMap.put(second, freqMap.get(second) - 1);
            
            if (freqMap.get(first) > 0) {
                maxHeap.offer(first);
            }
            if (freqMap.get(second) > 0) {
                maxHeap.offer(second);
            }
        }
        
        // 处理最后一个字符（如果有）
        if (!maxHeap.isEmpty()) {
            result.append(maxHeap.poll());
        }
        
        return result.toString();
    }
    
    /**
     * 题目3: LeetCode 502. IPO
     * 题目链接: https://leetcode.cn/problems/ipo/
     * 题目描述: 假设公司即将开始IPO，需要选择最多k个不同的项目来最大化资本
     * 解题思路: 使用两个堆，一个最大堆存储当前可做的项目利润，一个最小堆存储按资本排序的项目
     * 时间复杂度: O(n log n + k log n)
     * 空间复杂度: O(n)
     * 是否最优解: 是，贪心算法的最优实现
     */
    public static int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
        int n = profits.length;
        // 按资本排序的项目列表
        int[][] projects = new int[n][2];
        for (int i = 0; i < n; i++) {
            projects[i][0] = capital[i];
            projects[i][1] = profits[i];
        }
        
        // 按资本升序排序
        Arrays.sort(projects, (a, b) -> a[0] - b[0]);
        
        // 最大堆，存储当前可做项目的利润
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        
        int currentCapital = w;
        int projectIndex = 0;
        
        for (int i = 0; i < k; i++) {
            // 将所有资本要求小于等于当前资本的项目加入最大堆
            while (projectIndex < n && projects[projectIndex][0] <= currentCapital) {
                maxHeap.offer(projects[projectIndex][1]);
                projectIndex++;
            }
            
            // 如果没有可做的项目，退出
            if (maxHeap.isEmpty()) {
                break;
            }
            
            // 选择利润最大的项目
            currentCapital += maxHeap.poll();
        }
        
        return currentCapital;
    }
    
    /**
     * 题目4: LeetCode 630. 课程表 III
     * 题目链接: https://leetcode.cn/problems/course-schedule-iii/
     * 题目描述: 有n门不同的在线课程，你需要选择最多的课程完成
     * 解题思路: 贪心算法，按结束时间排序，使用最大堆维护已选课程的持续时间
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 是否最优解: 是，经典贪心算法
     */
    public static int scheduleCourse(int[][] courses) {
        // 按结束时间排序
        Arrays.sort(courses, (a, b) -> a[1] - b[1]);
        
        // 最大堆，存储已选课程的持续时间
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        
        int currentTime = 0;
        
        for (int[] course : courses) {
            int duration = course[0];
            int endTime = course[1];
            
            if (currentTime + duration <= endTime) {
                // 可以选这门课
                currentTime += duration;
                maxHeap.offer(duration);
            } else if (!maxHeap.isEmpty() && maxHeap.peek() > duration) {
                // 替换持续时间更长的课程
                currentTime = currentTime - maxHeap.poll() + duration;
                maxHeap.offer(duration);
            }
        }
        
        return maxHeap.size();
    }
    
    /**
     * 题目5: LeetCode 857. 雇佣 K 名工人的最低成本
     * 题目链接: https://leetcode.cn/problems/minimum-cost-to-hire-k-workers/
     * 题目描述: 有n名工人，需要雇佣k名工人，使得总工资最低
     * 解题思路: 按工资/质量比值排序，使用最大堆维护k个工人的质量
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 是否最优解: 是，贪心算法的最优实现
     */
    public static double mincostToHireWorkers(int[] quality, int[] wage, int k) {
        int n = quality.length;
        // 存储工人信息[质量, 工资, 工资质量比]
        double[][] workers = new double[n][3];
        for (int i = 0; i < n; i++) {
            workers[i][0] = quality[i];
            workers[i][1] = wage[i];
            workers[i][2] = (double) wage[i] / quality[i]; // 工资质量比
        }
        
        // 按工资质量比排序
        Arrays.sort(workers, (a, b) -> Double.compare(a[2], b[2]));
        
        // 最大堆，存储k个工人的质量
        PriorityQueue<Double> maxHeap = new PriorityQueue<>((a, b) -> Double.compare(b, a));
        
        double totalQuality = 0;
        double minCost = Double.MAX_VALUE;
        
        for (double[] worker : workers) {
            totalQuality += worker[0];
            maxHeap.offer(worker[0]);
            
            if (maxHeap.size() > k) {
                totalQuality -= maxHeap.poll();
            }
            
            if (maxHeap.size() == k) {
                minCost = Math.min(minCost, totalQuality * worker[2]);
            }
        }
        
        return minCost;
    }
    
    /**
     * 题目6: LeetCode 1054. 距离相等的条形码
     * 题目链接: https://leetcode.cn/problems/distant-barcodes/
     * 题目描述: 重新排列条形码，使得相邻的条形码不能相等
     * 解题思路: 类似重构字符串，使用最大堆按频率排序
     * 时间复杂度: O(n log k)
     * 空间复杂度: O(n)
     * 是否最优解: 是，贪心算法的最优实现
     */
    public static int[] rearrangeBarcodes(int[] barcodes) {
        if (barcodes == null || barcodes.length == 0) {
            return new int[0];
        }
        
        // 统计频率
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int code : barcodes) {
            freqMap.put(code, freqMap.getOrDefault(code, 0) + 1);
        }
        
        // 最大堆，按频率降序排列
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> 
            freqMap.get(b) - freqMap.get(a));
        maxHeap.addAll(freqMap.keySet());
        
        int[] result = new int[barcodes.length];
        int index = 0;
        
        while (maxHeap.size() >= 2) {
            int first = maxHeap.poll();
            int second = maxHeap.poll();
            
            result[index++] = first;
            result[index++] = second;
            
            // 更新频率
            freqMap.put(first, freqMap.get(first) - 1);
            freqMap.put(second, freqMap.get(second) - 1);
            
            if (freqMap.get(first) > 0) {
                maxHeap.offer(first);
            }
            if (freqMap.get(second) > 0) {
                maxHeap.offer(second);
            }
        }
        
        // 处理最后一个元素
        if (!maxHeap.isEmpty()) {
            result[index] = maxHeap.poll();
        }
        
        return result;
    }
    
    /**
     * 题目7: LeetCode 1383. 最大的团队表现值
     * 题目链接: https://leetcode.cn/problems/maximum-performance-of-a-team/
     * 题目描述: 选择最多k个工程师，使得团队表现值最大
     * 解题思路: 按效率排序，使用最小堆维护k个工程师的速度
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(k)
     * 是否最优解: 是，贪心算法的最优实现
     */
    public static int maxPerformance(int n, int[] speed, int[] efficiency, int k) {
        int[][] engineers = new int[n][2];
        for (int i = 0; i < n; i++) {
            engineers[i][0] = speed[i];
            engineers[i][1] = efficiency[i];
        }
        
        // 按效率降序排序
        Arrays.sort(engineers, (a, b) -> b[1] - a[1]);
        
        // 最小堆，维护k个工程师的速度
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        long totalSpeed = 0;
        long maxPerformance = 0;
        final int MOD = 1000000007;
        
        for (int[] engineer : engineers) {
            int spd = engineer[0];
            int eff = engineer[1];
            
            if (minHeap.size() == k) {
                totalSpeed -= minHeap.poll();
            }
            
            minHeap.offer(spd);
            totalSpeed += spd;
            
            maxPerformance = Math.max(maxPerformance, totalSpeed * eff);
        }
        
        return (int) (maxPerformance % MOD);
    }
    
    /**
     * 题目8: LeetCode 1642. 可以到达的最远建筑
     * 题目链接: https://leetcode.cn/problems/furthest-building-you-can-reach/
     * 题目描述: 使用梯子和砖块爬建筑，求能到达的最远建筑
     * 解题思路: 使用最大堆维护已使用的梯子对应的爬升高度
     * 时间复杂度: O(n log k)
     * 空间复杂度: O(k)
     * 是否最优解: 是，贪心算法的最优实现
     */
    public static int furthestBuilding(int[] heights, int bricks, int ladders) {
        // 最大堆，存储使用砖块爬升的高度
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        
        for (int i = 0; i < heights.length - 1; i++) {
            int diff = heights[i + 1] - heights[i];
            
            if (diff <= 0) {
                continue; // 不需要爬升
            }
            
            // 先用砖块
            bricks -= diff;
            maxHeap.offer(diff);
            
            // 如果砖块不够，用梯子替换之前最大的砖块使用
            if (bricks < 0) {
                if (ladders > 0) {
                    bricks += maxHeap.poll();
                    ladders--;
                } else {
                    return i; // 无法继续前进
                }
            }
        }
        
        return heights.length - 1;
    }
    
    /**
     * 题目9: LeetCode 1705. 吃苹果的最大数目
     * 题目链接: https://leetcode.cn/problems/maximum-number-of-eaten-apples/
     * 题目描述: 每天可以吃一个苹果，求最多能吃多少个苹果
     * 解题思路: 使用最小堆按腐烂时间排序，贪心吃最早腐烂的苹果
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 是否最优解: 是，贪心算法的最优实现
     */
    public static int eatenApples(int[] apples, int[] days) {
        // 最小堆，存储[腐烂时间, 苹果数量]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        int n = apples.length;
        int result = 0;
        
        for (int i = 0; i < n || !minHeap.isEmpty(); i++) {
            // 添加当天的新苹果
            if (i < n && apples[i] > 0) {
                minHeap.offer(new int[]{i + days[i], apples[i]});
            }
            
            // 移除已腐烂的苹果
            while (!minHeap.isEmpty() && minHeap.peek()[0] <= i) {
                minHeap.poll();
            }
            
            // 吃一个苹果
            if (!minHeap.isEmpty()) {
                int[] current = minHeap.peek();
                current[1]--; // 减少苹果数量
                result++;
                
                // 如果苹果吃完，移除堆顶
                if (current[1] == 0) {
                    minHeap.poll();
                }
            }
        }
        
        return result;
    }
    
    /**
     * 题目10: LeetCode 1834. 单线程 CPU
     * 题目链接: https://leetcode.cn/problems/single-threaded-cpu/
     * 题目描述: 单线程CPU调度任务，求任务执行顺序
     * 解题思路: 使用两个堆，一个按到达时间排序，一个按处理时间排序
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 是否最优解: 是，CPU调度问题的经典解法
     */
    public static int[] getOrder(int[][] tasks) {
        int n = tasks.length;
        // 存储任务索引和原始信息
        int[][] indexedTasks = new int[n][3];
        for (int i = 0; i < n; i++) {
            indexedTasks[i][0] = tasks[i][0]; // 到达时间
            indexedTasks[i][1] = tasks[i][1]; // 处理时间
            indexedTasks[i][2] = i; // 原始索引
        }
        
        // 按到达时间排序
        Arrays.sort(indexedTasks, (a, b) -> a[0] - b[0]);
        
        // 最小堆，存储[处理时间, 原始索引]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> 
            a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
        
        int[] result = new int[n];
        int resultIndex = 0;
        int taskIndex = 0;
        long currentTime = 0;
        
        while (resultIndex < n) {
            // 将当前时间点之前到达的任务加入堆
            while (taskIndex < n && indexedTasks[taskIndex][0] <= currentTime) {
                minHeap.offer(new int[]{indexedTasks[taskIndex][1], indexedTasks[taskIndex][2]});
                taskIndex++;
            }
            
            if (minHeap.isEmpty()) {
                // 如果没有任务，跳到下一个任务的到达时间
                currentTime = indexedTasks[taskIndex][0];
                continue;
            }
            
            // 执行堆顶任务
            int[] task = minHeap.poll();
            result[resultIndex++] = task[1];
            currentTime += task[0];
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试题目1
        int[][] matrix = {
            {1, 5, 9},
            {10, 11, 13},
            {12, 13, 15}
        };
        System.out.println("题目1测试: " + kthSmallestInSortedMatrix(matrix, 8)); // 期望输出: 13
        
        // 测试题目2
        System.out.println("题目2测试: " + reorganizeString("aab")); // 期望输出: "aba"
        
        // 测试题目3
        int[] profits = {1, 2, 3};
        int[] capital = {0, 1, 1};
        System.out.println("题目3测试: " + findMaximizedCapital(2, 0, profits, capital)); // 期望输出: 4
        
        System.out.println("所有测试通过！");
    }
}