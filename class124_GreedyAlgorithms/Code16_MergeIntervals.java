package class093;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 合并区间（Merge Intervals）
 * 题目来源：LeetCode 56
 * 题目链接：https://leetcode.cn/problems/merge-intervals/
 * 
 * 问题描述：
 * 以数组intervals表示若干个区间的集合，请合并所有重叠的区间，并返回一个不重叠的区间数组。
 * 
 * 算法思路：
 * 使用贪心策略，按照区间开始时间排序：
 * 1. 将区间按照开始时间从小到大排序
 * 2. 遍历排序后的区间，维护当前合并区间
 * 3. 如果当前区间与合并区间重叠，更新合并区间的结束时间
 * 4. 否则，将当前合并区间加入结果，开始新的合并区间
 * 
 * 时间复杂度：O(n log n) - 排序的时间复杂度
 * 空间复杂度：O(n) - 需要存储结果区间
 * 
 * 是否最优解：是。这是该问题的最优解法。
 * 
 * 适用场景：
 * 1. 区间合并问题
 * 2. 重叠区间处理
 * 
 * 异常处理：
 * 1. 处理空数组情况
 * 2. 处理单元素数组
 * 
 * 工程化考量：
 * 1. 输入验证：检查数组是否为空
 * 2. 边界条件：处理单元素和双元素数组
 * 3. 性能优化：使用快速排序提高效率
 * 
 * 相关题目：
 * 1. LeetCode 57. 插入区间 - 区间插入问题
 * 2. LeetCode 252. 会议室 - 区间重叠判断
 * 3. LeetCode 253. 会议室 II - 区间重叠计数
 * 4. 牛客网 NC135 买票需要多少时间 - 队列模拟相关
 * 5. LintCode 391. 数飞机 - 区间调度相关
 * 6. HackerRank - Jim and the Orders - 贪心调度问题
 * 7. CodeChef - TACHSTCK - 区间配对问题
 * 8. AtCoder ABC104C - All Green - 动态规划相关
 * 9. Codeforces 1363C - Game On Leaves - 博弈论相关
 * 10. POJ 3169 - Layout - 差分约束系统
 */
public class Code16_MergeIntervals {
    
    /**
     * 合并重叠区间
     * 
     * @param intervals 区间数组，每个区间包含开始和结束时间
     * @return 合并后的不重叠区间数组
     */
    public static int[][] merge(int[][] intervals) {
        // 边界条件检查
        if (intervals == null || intervals.length == 0) {
            return new int[0][];
        }
        
        int n = intervals.length;
        if (n == 1) {
            return intervals; // 只有一个区间，直接返回
        }
        
        // 按照区间开始时间排序
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[0], b[0]);
            }
        });
        
        List<int[]> result = new ArrayList<>();
        int[] currentInterval = intervals[0];
        result.add(currentInterval);
        
        for (int i = 1; i < n; i++) {
            int currentEnd = currentInterval[1];
            int nextStart = intervals[i][0];
            int nextEnd = intervals[i][1];
            
            // 如果当前区间与下一个区间重叠
            if (currentEnd >= nextStart) {
                // 合并区间，取较大的结束时间
                currentInterval[1] = Math.max(currentEnd, nextEnd);
            } else {
                // 不重叠，开始新的合并区间
                currentInterval = intervals[i];
                result.add(currentInterval);
            }
        }
        
        return result.toArray(new int[result.size()][]);
    }
    
    /**
     * 测试函数，验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1: 基本情况 - 有重叠区间
        int[][] intervals1 = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] result1 = merge(intervals1);
        System.out.println("测试用例1:");
        System.out.print("输入区间: [");
        for (int i = 0; i < intervals1.length; i++) {
            System.out.print("[" + intervals1[i][0] + "," + intervals1[i][1] + "]");
            if (i < intervals1.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.print("合并结果: [");
        for (int i = 0; i < result1.length; i++) {
            System.out.print("[" + result1[i][0] + "," + result1[i][1] + "]");
            if (i < result1.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("期望输出: [[1,6],[8,10],[15,18]]");
        System.out.println();
        
        // 测试用例2: 基本情况 - 无重叠区间
        int[][] intervals2 = {{1, 4}, {5, 8}, {9, 12}};
        int[][] result2 = merge(intervals2);
        System.out.println("测试用例2:");
        System.out.print("输入区间: [");
        for (int i = 0; i < intervals2.length; i++) {
            System.out.print("[" + intervals2[i][0] + "," + intervals2[i][1] + "]");
            if (i < intervals2.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.print("合并结果: [");
        for (int i = 0; i < result2.length; i++) {
            System.out.print("[" + result2[i][0] + "," + result2[i][1] + "]");
            if (i < result2.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("期望输出: [[1,4],[5,8],[9,12]]");
        System.out.println();
        
        // 测试用例3: 复杂情况 - 完全重叠
        int[][] intervals3 = {{1, 4}, {2, 3}, {3, 5}};
        int[][] result3 = merge(intervals3);
        System.out.println("测试用例3:");
        System.out.print("输入区间: [");
        for (int i = 0; i < intervals3.length; i++) {
            System.out.print("[" + intervals3[i][0] + "," + intervals3[i][1] + "]");
            if (i < intervals3.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.print("合并结果: [");
        for (int i = 0; i < result3.length; i++) {
            System.out.print("[" + result3[i][0] + "," + result3[i][1] + "]");
            if (i < result3.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("期望输出: [[1,5]]");
        System.out.println();
        
        // 测试用例4: 边界情况 - 单元素数组
        int[][] intervals4 = {{1, 3}};
        int[][] result4 = merge(intervals4);
        System.out.println("测试用例4:");
        System.out.print("输入区间: [");
        System.out.print("[" + intervals4[0][0] + "," + intervals4[0][1] + "]");
        System.out.println("]");
        System.out.print("合并结果: [");
        System.out.print("[" + result4[0][0] + "," + result4[0][1] + "]");
        System.out.println("]");
        System.out.println("期望输出: [[1,3]]");
        System.out.println();
        
        // 测试用例5: 边界情况 - 空数组
        int[][] intervals5 = {};
        int[][] result5 = merge(intervals5);
        System.out.println("测试用例5:");
        System.out.println("输入区间: []");
        System.out.println("合并结果: []");
        System.out.println("期望输出: []");
        System.out.println();
        
        // 测试用例6: 复杂情况 - 包含区间
        int[][] intervals6 = {{1, 10}, {2, 3}, {4, 5}, {6, 7}, {8, 9}};
        int[][] result6 = merge(intervals6);
        System.out.println("测试用例6:");
        System.out.print("输入区间: [");
        for (int i = 0; i < intervals6.length; i++) {
            System.out.print("[" + intervals6[i][0] + "," + intervals6[i][1] + "]");
            if (i < intervals6.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.print("合并结果: [");
        for (int i = 0; i < result6.length; i++) {
            System.out.print("[" + result6[i][0] + "," + result6[i][1] + "]");
            if (i < result6.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("期望输出: [[1,10]]");
    }
}