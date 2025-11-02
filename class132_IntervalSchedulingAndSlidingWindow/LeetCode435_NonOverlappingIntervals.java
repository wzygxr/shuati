package class129;

import java.util.Arrays;

/**
 * LeetCode 435. Non-overlapping Intervals
 * 
 * 题目描述：
 * 给定一个区间的集合 intervals，其中 intervals[i] = [start_i, end_i]。
 * 返回需要移除区间的最小数量，使剩余区间互不重叠。
 * 
 * 解题思路：
 * 这是一个经典的贪心算法问题。为了移除最少的区间，我们应该保留尽可能多的不重叠区间。
 * 
 * 算法步骤：
 * 1. 将所有区间按结束时间排序
 * 2. 使用贪心策略：总是选择结束时间最早的区间
 * 3. 遍历排序后的区间，统计可以保留的区间数量
 * 4. 返回总区间数减去保留的区间数，即为需要移除的区间数
 * 
 * 贪心策略的正确性：
 * 选择结束时间最早的区间可以为后续区间留下更多空间，从而最大化保留的区间数量。
 * 
 * 时间复杂度：O(n * log n) - 主要开销来自排序
 * 空间复杂度：O(1) - 只使用了常数级额外空间
 * 
 * 区间调度算法总结：
 * 1. 区间调度是贪心算法的经典应用场景
 * 2. 常见问题类型：
 *    - 选择最大不重叠区间数
 *    - 最小化移除的区间数
 *    - 寻找最长区间链
 *    - 带权重的区间选择问题
 * 3. 关键技巧：
 *    - 排序策略：根据结束时间、开始时间或其他关键属性排序
 *    - 贪心选择：每次选择局部最优解
 *    - 动态规划：处理带权重的区间调度问题
 *    - 二分查找：优化动态规划的查找过程
 * 
 * 补充题目汇总：
 * 1. LeetCode 1353. 最多可以参加的会议数目 (贪心)
 * 2. LeetCode 646. 最长数对链 (贪心)
 * 3. LeetCode 1235. 最大盈利的工作调度 (动态规划 + 二分查找)
 * 4. LeetCode 1751. 最多可以参加的会议数目 II (动态规划 + 二分查找)
 * 5. LeetCode 452. 用最少数量的箭引爆气球 (贪心)
 * 6. LeetCode 253. 会议室 II (扫描线算法)
 * 7. LeetCode 919. 会议室 II (扫描线算法)
 * 8. LintCode 919. 会议室 II (扫描线算法)
 * 9. HackerRank - Interval Selection
 * 10. Codeforces 1083F. The Fair Nut and Amusing Xor
 * 11. AtCoder ABC128D. equeue
 * 12. 洛谷 P1803 凌乱的yyy
 * 13. 牛客网 NC370. 会议室安排
 * 14. 杭电OJ 5171. GTY's birthday gift
 * 15. POJ 1089. Intervals (贪心)
 * 16. UVa 10382. Watering Grass
 * 17. CodeChef - STABLEMP
 * 18. SPOJ - ACTIV
 * 19. 剑指Offer II 074. 合并区间
 * 
 * 工程化考量：
 * 1. 在实际应用中，区间调度算法常用于：
 *    - 任务调度系统（CPU任务分配）
 *    - 资源分配问题（会议室预约、教室排课）
 *    - 时间管理系统
 *    - 交通流量优化
 * 2. 优化技巧：
 *    - 对于大规模数据，可以考虑使用更高效的排序算法
 *    - 当需要频繁查询时，可以建立区间树或线段树以加速查询
 *    - 考虑区间合并操作的优化
 * 3. 异常处理：
 *    - 处理无效区间（如开始时间大于结束时间）
 *    - 处理空输入
 *    - 处理重复区间
 * 4. 可扩展性：
 *    - 支持区间的动态添加和删除
 *    - 支持多维区间调度
 */
public class LeetCode435_NonOverlappingIntervals {
    
    /**
     * 计算需要移除的最小区间数
     * 
     * @param intervals 区间数组
     * @return 需要移除的区间数
     */
    public static int eraseOverlapIntervals(int[][] intervals) {
        // 边界情况处理
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        int n = intervals.length;
        
        // 按结束时间排序
        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        
        // 初始化计数器和上一个保留区间的结束时间
        int count = 1;  // 至少可以保留一个区间
        int end = intervals[0][1];  // 第一个区间的结束时间
        
        // 遍历剩余区间
        for (int i = 1; i < n; i++) {
            // 如果当前区间的开始时间 >= 上一个保留区间的结束时间
            // 说明不重叠，可以保留
            if (intervals[i][0] >= end) {
                count++;
                end = intervals[i][1];  // 更新结束时间
            }
            // 如果重叠，则跳过当前区间（相当于移除）
        }
        
        // 返回需要移除的区间数
        return n - count;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[][] intervals1 = {{1,2},{2,3},{3,4},{1,3}};
        System.out.println("测试用例1:");
        System.out.println("输入: intervals = [[1,2],[2,3],[3,4],[1,3]]");
        System.out.println("输出: " + eraseOverlapIntervals(intervals1)); // 期望输出: 1
        
        // 测试用例2
        int[][] intervals2 = {{1,2},{1,2},{1,2}};
        System.out.println("\n测试用例2:");
        System.out.println("输入: intervals = [[1,2],[1,2],[1,2]]");
        System.out.println("输出: " + eraseOverlapIntervals(intervals2)); // 期望输出: 2
        
        // 测试用例3
        int[][] intervals3 = {{1,2},{2,3}};
        System.out.println("\n测试用例3:");
        System.out.println("输入: intervals = [[1,2],[2,3]]");
        System.out.println("输出: " + eraseOverlapIntervals(intervals3)); // 期望输出: 0
        
        // 测试用例4
        int[][] intervals4 = {};
        System.out.println("\n测试用例4:");
        System.out.println("输入: intervals = []");
        System.out.println("输出: " + eraseOverlapIntervals(intervals4)); // 期望输出: 0
    }
}