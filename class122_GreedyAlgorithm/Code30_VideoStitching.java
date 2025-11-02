package class091;

import java.util.*;

/**
 * 视频拼接
 * 
 * 题目描述：
 * 你将会获得一系列视频片段，这些片段来自于一项持续时长为 time 秒的体育赛事。
 * 这些片段可能有所重叠，也可能长度不一。使用数组 clips 描述所有的视频片段，
 * 其中 clips[i] = [starti, endi] 表示：某个视频片段开始于 starti 并于 endi 结束。
 * 甚至可以对这些片段自由地再剪辑。例如，片段 [0, 7] 可以剪切成 [0, 1] + [1, 3] + [3, 7] 三部分。
 * 我们需要将这些片段进行再剪辑，并将剪辑后的内容拼接成覆盖整个运动过程的片段（[0, time]）。
 * 返回所需片段的最小数目，如果无法完成该任务，则返回 -1。
 * 
 * 来源：LeetCode 1024
 * 链接：https://leetcode.cn/problems/video-stitching/
 * 
 * 算法思路：
 * 使用贪心算法：
 * 1. 将片段按开始时间排序，如果开始时间相同则按结束时间降序排序
 * 2. 维护当前覆盖的最远位置 curEnd 和下一个要覆盖的位置 nextEnd
 * 3. 遍历排序后的片段：
 *    - 如果片段的开始时间大于当前覆盖的最远位置，说明有间隔，无法拼接
 *    - 如果片段的开始时间小于等于当前覆盖的最远位置，更新下一个要覆盖的位置
 *    - 当遍历到当前覆盖范围的边界时，增加片段计数并更新当前覆盖范围
 * 
 * 时间复杂度：O(n * logn) - 排序的时间复杂度
 * 空间复杂度：O(1) - 只使用常数空间
 * 
 * 关键点分析：
 * - 贪心策略：每次选择能覆盖当前范围且延伸最远的片段
 * - 排序策略：按开始时间排序，开始时间相同时按结束时间降序
 * - 边界处理：检查是否能覆盖整个区间 [0, time]
 * 
 * 工程化考量：
 * - 输入验证：检查clips数组和time参数的有效性
 * - 边界处理：处理time=0的情况
 * - 性能优化：避免不必要的排序操作
 */
public class Code30_VideoStitching {
    
    /**
     * 视频拼接的最小片段数
     * 
     * @param clips 视频片段数组
     * @param time 目标时长
     * @return 最小片段数，如果无法拼接返回-1
     */
    public static int videoStitching(int[][] clips, int time) {
        // 输入验证
        if (clips == null || clips.length == 0) {
            return time == 0 ? 0 : -1;
        }
        if (time < 0) {
            throw new IllegalArgumentException("时间不能为负数");
        }
        if (time == 0) {
            return 0;
        }
        
        // 按开始时间排序，开始时间相同时按结束时间降序
        Arrays.sort(clips, (a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0];
            } else {
                return b[1] - a[1];
            }
        });
        
        int count = 0; // 片段计数
        int curEnd = 0; // 当前覆盖的最远位置
        int nextEnd = 0; // 下一个要覆盖的位置
        int i = 0; // 当前处理的片段索引
        int n = clips.length;
        
        while (i < n && curEnd < time) {
            // 找到所有开始时间小于等于curEnd的片段中，结束时间最远的
            while (i < n && clips[i][0] <= curEnd) {
                nextEnd = Math.max(nextEnd, clips[i][1]);
                i++;
            }
            
            // 如果没有找到可以扩展的片段，说明无法拼接
            if (curEnd == nextEnd) {
                return -1;
            }
            
            // 选择当前片段，更新当前覆盖范围
            count++;
            curEnd = nextEnd;
            
            // 如果已经覆盖了目标范围，提前结束
            if (curEnd >= time) {
                break;
            }
        }
        
        // 检查是否覆盖了整个区间 [0, time]
        return curEnd >= time ? count : -1;
    }
    
    /**
     * 另一种实现：使用动态规划思想
     * 时间复杂度：O(n * time)
     * 空间复杂度：O(time)
     */
    public static int videoStitchingDP(int[][] clips, int time) {
        if (clips == null || clips.length == 0) {
            return time == 0 ? 0 : -1;
        }
        if (time < 0) {
            throw new IllegalArgumentException("时间不能为负数");
        }
        if (time == 0) {
            return 0;
        }
        
        // dp[i] 表示覆盖区间 [0, i] 所需的最小片段数
        int[] dp = new int[time + 1];
        Arrays.fill(dp, Integer.MAX_VALUE - 1);
        dp[0] = 0;
        
        for (int i = 1; i <= time; i++) {
            for (int[] clip : clips) {
                int start = clip[0];
                int end = clip[1];
                
                // 如果当前片段可以覆盖到i
                if (start < i && i <= end) {
                    dp[i] = Math.min(dp[i], dp[start] + 1);
                }
            }
        }
        
        return dp[time] == Integer.MAX_VALUE - 1 ? -1 : dp[time];
    }
    
    /**
     * 使用区间合并的思路
     * 时间复杂度：O(n * logn)
     * 空间复杂度：O(1)
     */
    public static int videoStitchingMerge(int[][] clips, int time) {
        if (clips == null || clips.length == 0) {
            return time == 0 ? 0 : -1;
        }
        if (time < 0) {
            throw new IllegalArgumentException("时间不能为负数");
        }
        if (time == 0) {
            return 0;
        }
        
        // 按开始时间排序
        Arrays.sort(clips, (a, b) -> a[0] - b[0]);
        
        int count = 0;
        int currentEnd = 0;
        int nextEnd = 0;
        int index = 0;
        
        while (currentEnd < time) {
            count++;
            
            // 找到所有开始时间小于等于currentEnd的片段中，结束时间最大的
            while (index < clips.length && clips[index][0] <= currentEnd) {
                nextEnd = Math.max(nextEnd, clips[index][1]);
                index++;
            }
            
            // 如果没有进展，说明无法拼接
            if (nextEnd == currentEnd) {
                return -1;
            }
            
            currentEnd = nextEnd;
            
            // 如果已经覆盖了目标范围，提前结束
            if (currentEnd >= time) {
                break;
            }
            
            // 如果已经处理完所有片段但还没有覆盖完，返回-1
            if (index >= clips.length) {
                return -1;
            }
        }
        
        return count;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1: clips = [[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]], time = 10
        // 期望输出: 3
        int[][] clips1 = {{0,2},{4,6},{8,10},{1,9},{1,5},{5,9}};
        int time1 = 10;
        System.out.println("测试用例1:");
        System.out.println("Clips: " + Arrays.deepToString(clips1));
        System.out.println("Time: " + time1);
        System.out.println("贪心结果: " + videoStitching(clips1, time1)); // 期望: 3
        System.out.println("DP结果: " + videoStitchingDP(clips1, time1)); // 期望: 3
        System.out.println("合并结果: " + videoStitchingMerge(clips1, time1)); // 期望: 3
        
        // 测试用例2: clips = [[0,1],[1,2]], time = 5
        // 期望输出: -1 (无法覆盖到5)
        int[][] clips2 = {{0,1},{1,2}};
        int time2 = 5;
        System.out.println("\n测试用例2:");
        System.out.println("Clips: " + Arrays.deepToString(clips2));
        System.out.println("Time: " + time2);
        System.out.println("贪心结果: " + videoStitching(clips2, time2)); // 期望: -1
        System.out.println("DP结果: " + videoStitchingDP(clips2, time2)); // 期望: -1
        System.out.println("合并结果: " + videoStitchingMerge(clips2, time2)); // 期望: -1
        
        // 测试用例3: clips = [[0,4],[2,8]], time = 5
        // 期望输出: 2
        int[][] clips3 = {{0,4},{2,8}};
        int time3 = 5;
        System.out.println("\n测试用例3:");
        System.out.println("Clips: " + Arrays.deepToString(clips3));
        System.out.println("Time: " + time3);
        System.out.println("贪心结果: " + videoStitching(clips3, time3)); // 期望: 2
        System.out.println("DP结果: " + videoStitchingDP(clips3, time3)); // 期望: 2
        System.out.println("合并结果: " + videoStitchingMerge(clips3, time3)); // 期望: 2
        
        // 测试用例4: 空数组, time = 0
        int[][] clips4 = {};
        int time4 = 0;
        System.out.println("\n测试用例4:");
        System.out.println("Clips: " + Arrays.deepToString(clips4));
        System.out.println("Time: " + time4);
        System.out.println("贪心结果: " + videoStitching(clips4, time4)); // 期望: 0
        System.out.println("DP结果: " + videoStitchingDP(clips4, time4)); // 期望: 0
        System.out.println("合并结果: " + videoStitchingMerge(clips4, time4)); // 期望: 0
        
        // 测试用例5: 单个片段覆盖整个区间
        int[][] clips5 = {{0,10}};
        int time5 = 10;
        System.out.println("\n测试用例5:");
        System.out.println("Clips: " + Arrays.deepToString(clips5));
        System.out.println("Time: " + time5);
        System.out.println("贪心结果: " + videoStitching(clips5, time5)); // 期望: 1
        System.out.println("DP结果: " + videoStitchingDP(clips5, time5)); // 期望: 1
        System.out.println("合并结果: " + videoStitchingMerge(clips5, time5)); // 期望: 1
        
        // 性能测试
        performanceTest();
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        // 生成大规模测试数据
        int[][] largeClips = new int[10000][2];
        Random random = new Random();
        for (int i = 0; i < largeClips.length; i++) {
            int start = random.nextInt(1000);
            int end = start + random.nextInt(100) + 1;
            largeClips[i] = new int[]{start, end};
        }
        int time = 1000;
        
        System.out.println("\n=== 性能测试 ===");
        
        long startTime1 = System.currentTimeMillis();
        int result1 = videoStitching(largeClips, time);
        long endTime1 = System.currentTimeMillis();
        System.out.println("贪心算法执行时间: " + (endTime1 - startTime1) + "ms");
        System.out.println("结果: " + result1);
        
        long startTime2 = System.currentTimeMillis();
        int result2 = videoStitchingDP(largeClips, time);
        long endTime2 = System.currentTimeMillis();
        System.out.println("动态规划执行时间: " + (endTime2 - startTime2) + "ms");
        System.out.println("结果: " + result2);
        
        long startTime3 = System.currentTimeMillis();
        int result3 = videoStitchingMerge(largeClips, time);
        long endTime3 = System.currentTimeMillis();
        System.out.println("合并算法执行时间: " + (endTime3 - startTime3) + "ms");
        System.out.println("结果: " + result3);
    }
    
    /**
     * 算法复杂度分析
     */
    public static void analyzeComplexity() {
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("贪心算法:");
        System.out.println("- 时间复杂度: O(n * logn)");
        System.out.println("  - 排序: O(n * logn)");
        System.out.println("  - 遍历: O(n)");
        System.out.println("- 空间复杂度: O(1)");
        
        System.out.println("\n动态规划:");
        System.out.println("- 时间复杂度: O(n * time)");
        System.out.println("  - 外层循环: O(time)");
        System.out.println("  - 内层循环: O(n)");
        System.out.println("- 空间复杂度: O(time)");
        
        System.out.println("\n合并算法:");
        System.out.println("- 时间复杂度: O(n * logn)");
        System.out.println("  - 排序: O(n * logn)");
        System.out.println("  - 遍历: O(n)");
        System.out.println("- 空间复杂度: O(1)");
        
        System.out.println("\n贪心策略证明:");
        System.out.println("1. 按开始时间排序可以确保覆盖连续性");
        System.out.println("2. 选择结束时间最远的片段是最优选择");
        System.out.println("3. 数学归纳法证明贪心选择性质");
    }
}