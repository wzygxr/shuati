package class129;

import java.util.Arrays;

/**
 * LeetCode 1751. 最多可以参加的会议数目 II
 * 
 * 题目描述：
 * 给定n个会议，每个会议有开始时间、结束时间、收益三个值
 * 参加会议就能得到收益，但是同一时间只能参加一个会议
 * 一共能参加k个会议，如果选择参加某个会议，那么必须完整的参加完这个会议
 * 会议结束日期是包含在会议内的，一个会议的结束时间等于另一个会议的开始时间，不能两个会议都参加
 * 返回能得到的会议价值最大和
 * 
 * 解题思路：
 * 这是一个带权重的区间调度问题，使用动态规划结合二分查找来解决
 * 
 * 算法步骤：
 * 1. 将所有会议按结束时间排序
 * 2. 使用动态规划，dp[i][j]表示在前i个会议中最多选择j个会议能获得的最大收益
 * 3. 对于每个会议，我们可以选择参加或不参加
 * 4. 如果参加，需要找到最后一个不冲突的会议，这可以通过二分查找实现
 * 5. 状态转移方程：
 *    dp[i][j] = max(dp[i-1][j], dp[prev][j-1] + events[i][2])
 *    其中 prev 是最后一个结束时间 < 当前会议开始时间的会议索引
 * 
 * 时间复杂度分析：
 * - 排序需要 O(n log n)
 * - 动态规划过程中，每个状态的计算需要 O(log n) 的时间进行二分查找
 * - 总时间复杂度：O(n * k + n * log n)
 * 空间复杂度：O(n * k) - 存储动态规划数组
 * 
 * 相关题目：
 * 1. LeetCode 1235. 最大盈利的工作调度 (动态规划 + 二分查找)
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
 */
public class Code01_MaximumNumberOfEvents {

	// events[i][0] : 开始时间
	// events[i][1] : 结束时间
	// events[i][2] : 收益
	public static int maxValue(int[][] events, int k) {
		int n = events.length;
		// 按结束时间排序
		Arrays.sort(events, (a, b) -> a[1] - b[1]);
		// dp[i][j] : 0..i范围上最多选j个会议召开，最大收益是多少
		int[][] dp = new int[n][k + 1];
		for (int j = 1; j <= k; j++) {
			// 初始化：第一个会议单独参加的收益
			dp[0][j] = events[0][2];
		}
		for (int i = 1, pre; i < n; i++) {
			// 找到最后一个不冲突的会议
			pre = find(events, i - 1, events[i][0]);
			for (int j = 1; j <= k; j++) {
				// 状态转移：不参加当前会议 vs 参加当前会议
				dp[i][j] = Math.max(dp[i - 1][j], (pre == -1 ? 0 : dp[pre][j - 1]) + events[i][2]);
			}
		}
		return dp[n - 1][k];
	}

	/**
	 * 使用二分查找找到最后一个结束时间 < s 的会议
	 * 
	 * @param events 会议数组，已按结束时间排序
	 * @param i 搜索范围的右边界
	 * @param s 当前会议的开始时间
	 * @return 最后一个不冲突会议的索引，如果不存在则返回-1
	 */
	public static int find(int[][] events, int i, int s) {
		int l = 0, r = i, mid;
		int ans = -1;
		while (l <= r) {
			mid = (l + r) / 2;
			// 如果当前会议的结束时间 < s，可能是候选答案
			if (events[mid][1] < s) {
				ans = mid;
				l = mid + 1;
			} else {
				// 否则需要在左半部分查找
				r = mid - 1;
			}
		}
		return ans;
	}

}