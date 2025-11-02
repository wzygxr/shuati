package class129;

import java.util.Arrays;

/**
 * LeetCode 1751. 最多可以参加的会议数目 II
 * 
 * 题目描述：
 * 给你一个 events 数组，其中 events[i] = [startDayi, endDayi, valuei] ，表示第 i 个会议在 startDayi 天开始，
 * 第 endDayi 天结束，如果你参加这个会议，你能得到价值 valuei 。同时给你一个整数 k 表示你能参加的最多会议数目。
 * 你同一时间只能参加一个会议。如果你选择参加某个会议，那么你必须完整地参加完这个会议。
 * 会议结束日期是包含在会议内的，也就是说你不能同时参加一个开始日期与另一个结束日期相同的两个会议。
 * 请你返回能得到的会议价值最大和。
 * 
 * 示例：
 * 输入：events = [[1,2,4],[3,4,3],[2,3,1]], k = 2
 * 输出：7
 * 解释：选择绿色的活动会议 0 和 1，得到总价值和为 4 + 3 = 7 。
 * 
 * 解题思路：
 * 这是一个典型的动态规划问题，类似于背包问题的变种。我们需要在有限的会议数量k下，选择价值最大的会议组合。
 * 
 * 算法步骤：
 * 1. 按照会议结束时间对所有会议进行排序
 * 2. 使用动态规划，dp[i][j] 表示从前 i 个会议中最多参加 j 个会议所能获得的最大价值
 * 3. 对于每个会议，我们可以选择参加或不参加
 * 4. 如果参加，我们需要找到最后一个与其不冲突的会议，这可以通过二分查找实现
 * 5. 状态转移方程：
 *    dp[i][j] = max(dp[i-1][j], dp[pre][j-1] + events[i][2])
 *    其中 pre 是最后一个与会议 i 不冲突的会议索引
 * 
 * 时间复杂度：O(n * k + n * log n)
 * 空间复杂度：O(n * k)
 * 
 * 相关题目：
 * - LeetCode 1353. 最多可以参加的会议数目 (贪心解法)
 * - LeetCode 435. 无重叠区间 (贪心)
 * - LeetCode 646. 最长数对链 (动态规划 + 贪心)
 */
public class LeetCode1751_MaximumNumberOfEventsII {

    /**
     * 计算最多能参加k个会议获得的最大价值
     * 
     * @param events 会议数组，每个元素为 [开始时间, 结束时间, 价值]
     * @param k 最多能参加的会议数量
     * @return 能获得的最大价值
     */
    public static int maxValue(int[][] events, int k) {
        int n = events.length;
        // 按结束时间排序
        Arrays.sort(events, (a, b) -> a[1] - b[1]);
        
        // dp[i][j] 表示前i个会议中最多参加j个会议的最大价值
        int[][] dp = new int[n][k + 1];
        
        // 初始化：第一个会议的情况
        for (int j = 1; j <= k; j++) {
            dp[0][j] = events[0][2];
        }
        
        // 填充dp表
        for (int i = 1; i < n; i++) {
            // 找到最后一个与当前会议不冲突的会议索引
            int pre = find(events, i - 1, events[i][0]);
            
            // 对于每个可能的会议数量j
            for (int j = 1; j <= k; j++) {
                // 不参加当前会议 vs 参加当前会议
                dp[i][j] = Math.max(
                    dp[i - 1][j], 
                    (pre == -1 ? 0 : dp[pre][j - 1]) + events[i][2]
                );
            }
        }
        
        return dp[n - 1][k];
    }

    /**
     * 使用二分查找找到结束时间小于s的最右边的会议
     * 
     * @param events 会议数组
     * @param right 搜索范围的右边界
     * @param s 目标开始时间
     * @return 最后一个结束时间小于s的会议索引，如果不存在返回-1
     */
    public static int find(int[][] events, int right, int s) {
        int left = 0;
        int mid;
        int ans = -1;
        
        while (left <= right) {
            mid = (left + right) / 2;
            // 如果当前会议的结束时间小于s，可能是我们要找的会议
            if (events[mid][1] < s) {
                ans = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return ans;
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[][] events1 = {{1,2,4},{3,4,3},{2,3,1}};
        int k1 = 2;
        System.out.println("测试用例1:");
        System.out.println("输入: events = [[1,2,4],[3,4,3],[2,3,1]], k = 2");
        System.out.println("输出: " + maxValue(events1, k1)); // 期望输出: 7
        
        // 测试用例2
        int[][] events2 = {{1,2,4},{3,4,3},{2,3,10}};
        int k2 = 2;
        System.out.println("\n测试用例2:");
        System.out.println("输入: events = [[1,2,4],[3,4,3],[2,3,10]], k = 2");
        System.out.println("输出: " + maxValue(events2, k2)); // 期望输出: 14
        
        // 测试用例3
        int[][] events3 = {{1,1,1},{2,2,2},{3,3,3},{4,4,4}};
        int k3 = 3;
        System.out.println("\n测试用例3:");
        System.out.println("输入: events = [[1,1,1],[2,2,2],[3,3,3],[4,4,4]], k = 3");
        System.out.println("输出: " + maxValue(events3, k3)); // 期望输出: 9
    }
}