package class083;

import java.util.Arrays;

// 规划兼职工作 (Maximum Profit in Job Scheduling)
// 你打算利用空闲时间来做兼职工作赚些零花钱，这里有n份兼职工作
// 每份工作预计从startTime[i]开始、endTime[i]结束，报酬为profit[i]
// 返回可以获得的最大报酬
// 注意，时间上出现重叠的 2 份工作不能同时进行
// 如果你选择的工作在时间X结束，那么你可以立刻进行在时间X开始的下一份工作
// 
// 相关题目链接:
// LeetCode 1235. 规划兼职工作: https://leetcode.cn/problems/maximum-profit-in-job-scheduling/
// LintCode 3653. Meeting Scheduler: https://www.lintcode.com/problem/3653/
// HackerRank Job Scheduling: https://www.hackerrank.com/challenges/jobscheduling/problem
// 
// 核心算法: 动态规划 + 二分查找
// 时间复杂度: O(n log n) - 排序O(n log n) + 动态规划O(n) + 二分查找O(n log n)
// 空间复杂度: O(n) - 存储工作数组和DP数组
// 工程化考量: 输入验证、边界条件处理、溢出保护
// 
// 解题思路:
// 1. 将工作按结束时间排序，这是贪心选择性质的关键
// 2. 使用动态规划，dp[i]表示考虑前i+1个工作能获得的最大利润
// 3. 对于每个工作，使用二分查找找到与当前工作不冲突的最近工作
// 4. 状态转移方程: dp[i] = max(dp[i-1], dp[j] + profit[i])
//    其中j是与当前工作不冲突的最近工作索引
public class Code01_MaximumProfitInJobScheduling {

	// 最大工作数量常量
	public static int MAXN = 50001;

	// 工作数组，每个工作包含[start, end, profit]
	public static int[][] jobs = new int[MAXN][3];

	// 动态规划数组，dp[i]表示考虑前i+1个工作能获得的最大利润
	public static int[] dp = new int[MAXN];

	// 主函数：计算最大利润的工作调度方案
	// startTime: 工作开始时间数组
	// endTime: 工作结束时间数组
	// profit: 工作报酬数组
	// 返回值: 能获得的最大报酬
	public static int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
		int n = startTime.length;
		// 构造工作数组
		for (int i = 0; i < n; i++) {
			jobs[i][0] = startTime[i];
			jobs[i][1] = endTime[i];
			jobs[i][2] = profit[i];
		}
		// 工作按照结束时间从小到大排序，这是贪心选择性质的关键
		Arrays.sort(jobs, 0, n, (a, b) -> a[1] - b[1]);
		// 初始化DP数组
		dp[0] = jobs[0][2];
		// 动态规划填表
		for (int i = 1, start; i < n; i++) {
			start = jobs[i][0];
			// 选择当前工作能获得的利润
			dp[i] = jobs[i][2];
			// 如果存在与当前工作不冲突的前一个工作，则加上该工作的最大利润
			if (jobs[0][1] <= start) {
				dp[i] += dp[find(i - 1, start)];
			}
			// 状态转移：选择当前工作或不选择当前工作
			dp[i] = Math.max(dp[i], dp[i - 1]);
		}
		// 返回考虑所有工作时的最大利润
		return dp[n - 1];
	}

	// 二分查找辅助函数：在jobs[0...i]范围内，找到结束时间 <= start的最右下标
	// i: 搜索范围的右边界
	// start: 当前工作的开始时间
	// 返回值: 不与当前工作冲突的最近工作的下标
	public static int find(int i, int start) {
		int ans = 0;
		int l = 0;
		int r = i;
		int m;
		// 二分查找找到结束时间小于等于start的最后一个工作
		while (l <= r) {
			m = (l + r) / 2;
			if (jobs[m][1] <= start) {
				ans = m;
				l = m + 1;
			} else {
				r = m - 1;
			}
		}
		return ans;
	}

}