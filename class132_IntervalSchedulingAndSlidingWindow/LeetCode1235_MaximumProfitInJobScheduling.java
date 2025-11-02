package class129;

import java.util.*;

/**
 * LeetCode 1235. Maximum Profit in Job Scheduling
 * 
 * 题目描述：
 * 你有 n 个工作，每个工作有开始时间 startTime[i]，结束时间 endTime[i] 和利润 profit[i]。
 * 你需要选择一个工作子集，使得总利润最大化，且所选工作的时间范围不重叠。
 * 注意：如果一个工作在时间 X 结束，另一个工作可以在时间 X 开始（它们不重叠）。
 * 
 * 解题思路：
 * 这是一个经典的带权重区间调度问题，结合了动态规划和二分查找的方法。
 * 与简单的区间调度问题不同，这里每个工作有不同的权重（利润），我们需要在不重叠的前提下最大化总权重。
 * 
 * 算法步骤：
 * 1. 将所有工作按开始时间排序
 * 2. 使用动态规划，定义 dfs(i) 表示从第 i 个工作开始能得到的最大利润
 * 3. 对于每个工作，我们可以选择做或不做
 * 4. 如果做，我们需要找到下一个不冲突的工作，这可以通过二分查找实现
 * 5. 状态转移方程：
 *    dfs(i) = max(dfs(i+1), profit[i] + dfs(j))
 *    其中 j 是第一个开始时间 >= 当前工作结束时间的工作索引
 * 
 * 时间复杂度分析：
 * - 排序需要 O(n log n)
 * - 动态规划过程中，每个状态的计算需要 O(log n) 的时间进行二分查找
 * - 总时间复杂度：O(n log n)
 * 空间复杂度：O(n) - 存储动态规划数组和排序后的工作数组
 * 
 * 带权重区间调度算法总结：
 * 1. 带权重区间调度是区间调度问题的扩展，引入了权重概念
 * 2. 与贪心算法不同，带权重的情况下通常需要使用动态规划
 * 3. 关键技巧：
 *    - 排序策略：根据结束时间或开始时间排序
 *    - 使用二分查找快速定位下一个不冲突的区间
 *    - 动态规划状态转移
 * 4. 优化方向：
 *    - 记忆化搜索避免重复计算
 *    - 自底向上的动态规划实现
 *    - 预处理加速查找过程
 * 
 * 补充题目汇总：
 * 1. LeetCode 1751. 最多可以参加的会议数目 II (动态规划 + 二分查找)
 * 2. LeetCode 435. 无重叠区间 (贪心)
 * 3. LeetCode 646. 最长数对链 (贪心)
 * 4. LeetCode 253. 会议室 II (扫描线算法)
 * 5. LintCode 1923. 最多可参加的会议数量 II
 * 6. HackerRank - Job Scheduling
 * 7. Codeforces 1324D. Pair of Topics
 * 8. AtCoder ABC091D. Two Faced Edges
 * 9. 洛谷 P2051 [AHOI2009]中国象棋
 * 10. 牛客网 NC46. 加起来和为目标值的组合
 * 11. 杭电OJ 3572. Task Schedule
 * 12. POJ 3616. Milking Time
 * 13. UVa 10158. War
 * 14. CodeChef - MAXSEGMENTS
 * 15. SPOJ - BUSYMAN
 * 16. Project Euler 318. Cutting Game
 * 17. HackerEarth - Job Scheduling Problem
 * 18. 计蒜客 - 工作安排
 * 19. ZOJ 3623. Battle Ships
 * 20. acwing 2068. 整数拼接
 * 
 * 工程化考量：
 * 1. 在实际应用中，带权重区间调度常用于：
 *    - 项目管理和资源分配
 *    - 云计算中的任务调度
 *    - 金融投资组合优化
 *    - 广告投放策略
 * 2. 实现优化：
 *    - 对于大规模数据，可以使用更高效的排序算法
 *    - 考虑使用二分索引树（Fenwick Tree）或线段树优化查询
 *    - 使用空间换时间，预处理可能的查询结果
 * 3. 可扩展性：
 *    - 支持动态添加和删除工作
 *    - 处理多个约束条件（如资源限制）
 *    - 扩展到多维问题
 * 4. 鲁棒性考虑：
 *    - 处理无效输入（负利润、无效时间区间）
 *    - 处理大规模数据时的内存管理
 *    - 优化极端情况下的性能
public class LeetCode1235_MaximumProfitInJobScheduling {
    
    // 工作信息类
    static class Job {
        int start, end, profit;
        
        Job(int start, int end, int profit) {
            this.start = start;
            this.end = end;
            this.profit = profit;
        }
    }
    
    /**
     * 计算最大利润
     * 
     * @param startTime 工作开始时间数组
     * @param endTime 工作结束时间数组
     * @param profit 工作利润数组
     * @return 能获得的最大利润
     */
    public static int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;
        
        // 创建工作数组并按开始时间排序
        Job[] jobs = new Job[n];
        for (int i = 0; i < n; i++) {
            jobs[i] = new Job(startTime[i], endTime[i], profit[i]);
        }
        Arrays.sort(jobs, (a, b) -> a.start - b.start);
        
        // dp[i] 表示从第 i 个工作开始能得到的最大利润
        int[] dp = new int[n + 1];
        
        // 从后往前填充 dp 数组
        for (int i = n - 1; i >= 0; i--) {
            // 不选择当前工作
            int skip = dp[i + 1];
            
            // 选择当前工作，找到下一个不冲突的工作
            int next = findNextJob(jobs, i);
            int take = jobs[i].profit + dp[next];
            
            // 取最大值
            dp[i] = Math.max(skip, take);
        }
        
        return dp[0];
    }
    
    /**
     * 使用二分查找找到下一个不冲突的工作索引
     * 
     * @param jobs 工作数组
     * @param current 当前工作索引
     * @return 下一个不冲突的工作索引
     */
    public static int findNextJob(Job[] jobs, int current) {
        int left = current + 1;
        int right = jobs.length;
        int target = jobs[current].end;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (jobs[mid].start >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[] startTime1 = {1, 2, 3, 3};
        int[] endTime1 = {3, 4, 5, 6};
        int[] profit1 = {50, 10, 40, 70};
        System.out.println("测试用例1:");
        System.out.println("输入: startTime = [1,2,3,3], endTime = [3,4,5,6], profit = [50,10,40,70]");
        System.out.println("输出: " + jobScheduling(startTime1, endTime1, profit1)); // 期望输出: 120
        
        // 测试用例2
        int[] startTime2 = {1, 2, 3, 4, 6};
        int[] endTime2 = {3, 5, 10, 6, 9};
        int[] profit2 = {20, 20, 100, 70, 60};
        System.out.println("\n测试用例2:");
        System.out.println("输入: startTime = [1,2,3,4,6], endTime = [3,5,10,6,9], profit = [20,20,100,70,60]");
        System.out.println("输出: " + jobScheduling(startTime2, endTime2, profit2)); // 期望输出: 150
        
        // 测试用例3
        int[] startTime3 = {1, 1, 1};
        int[] endTime3 = {2, 3, 4};
        int[] profit3 = {5, 6, 4};
        System.out.println("\n测试用例3:");
        System.out.println("输入: startTime = [1,1,1], endTime = [2,3,4], profit = [5,6,4]");
        System.out.println("输出: " + jobScheduling(startTime3, endTime3, profit3)); // 期望输出: 6
    }
}