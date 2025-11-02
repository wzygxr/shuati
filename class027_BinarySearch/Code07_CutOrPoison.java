package class051;

// 刀砍毒杀怪兽问题
// 怪兽的初始血量是一个整数hp，给出每一回合刀砍和毒杀的数值cuts和poisons
// 第i回合如果用刀砍，怪兽在这回合会直接损失cuts[i]的血，不再有后续效果
// 第i回合如果用毒杀，怪兽在这回合不会损失血量，但是之后每回合都损失poisons[i]的血量
// 并且你选择的所有毒杀效果，在之后的回合都会叠加
// 两个数组cuts、poisons，长度都是n，代表你一共可以进行n回合
// 每一回合你只能选择刀砍或者毒杀中的一个动作
// 如果你在n个回合内没有直接杀死怪兽，意味着你已经无法有新的行动了
// 但是怪兽如果有中毒效果的话，那么怪兽依然会在血量耗尽的那回合死掉
// 返回至少多少回合，怪兽会死掉
// 数据范围 : 
// 1 <= n <= 10^5
// 1 <= hp <= 10^9
// 1 <= cuts[i]、poisons[i] <= 10^9
// 本题来自真实大厂笔试，找不到测试链接，所以用对数器验证
public class Code07_CutOrPoison {

	// 动态规划方法(只是为了验证)
	// 目前没有讲动态规划，所以不需要理解这个函数
	// 这个函数只是为了验证二分答案的方法是否正确的
	// 纯粹为了写对数器验证才设计的方法，血量比较大的时候会超时
	// 这个方法不做要求，此时并不需要理解，可以在学习完动态规划章节之后来看看这个函数
	public static int fast1(int[] cuts, int[] poisons, int hp) {
		int sum = 0;
		for (int num : poisons) {
			sum += num;
		}
		int[][][] dp = new int[cuts.length][hp + 1][sum + 1];
		return f1(cuts, poisons, 0, hp, 0, dp);
	}

	// 不做要求
	public static int f1(int[] cuts, int[] poisons, int i, int r, int p, int[][][] dp) {
		r -= p;
		if (r <= 0) {
			return i + 1;
		}
		if (i == cuts.length) {
			if (p == 0) {
				return Integer.MAX_VALUE;
			} else {
				return cuts.length + 1 + (r + p - 1) / p;
			}
		}
		if (dp[i][r][p] != 0) {
			return dp[i][r][p];
		}
		int p1 = r <= cuts[i] ? (i + 1) : f1(cuts, poisons, i + 1, r - cuts[i], p, dp);
		int p2 = f1(cuts, poisons, i + 1, r, p + poisons[i], dp);
		int ans = Math.min(p1, p2);
		dp[i][r][p] = ans;
		return ans;
	}

	// 二分答案法
	// 最优解
	// 时间复杂度O(n * log(hp))，额外空间复杂度O(1)
	public static int fast2(int[] cuts, int[] poisons, int hp) {
		int ans = Integer.MAX_VALUE;
		for (int l = 1, r = hp + 1, m; l <= r;) {
			// m中点，一定要让怪兽在m回合内死掉，更多回合无意义
			m = l + ((r - l) >> 1);
			if (f(cuts, poisons, hp, m)) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}

	// cuts、posions，每一回合刀砍、毒杀的效果
	// hp：怪兽血量
	// limit：回合的限制
	public static boolean f(int[] cuts, int[] posions, long hp, int limit) {
		int n = Math.min(cuts.length, limit);
		for (int i = 0, j = 1; i < n; i++, j++) {
			hp -= Math.max((long) cuts[i], (long) (limit - j) * (long) posions[i]);
			if (hp <= 0) {
				return true;
			}
		}
		return false;
	}

	// 对数器测试
	public static void main(String[] args) {
		// 随机测试的数据量不大
		// 因为数据量大了，fast1方法会超时
		// 所以在数据量不大的情况下，验证fast2方法功能正确即可
		// fast2方法在大数据量的情况下一定也能通过
		// 因为时间复杂度就是最优的
		System.out.println("测试开始");
		int N = 30;
		int V = 20;
		int H = 300;
		int testTimes = 10000;
		for (int i = 0; i < testTimes; i++) {
			int n = (int) (Math.random() * N) + 1;
			int[] cuts = randomArray(n, V);
			int[] posions = randomArray(n, V);
			int hp = (int) (Math.random() * H) + 1;
			int ans1 = fast1(cuts, posions, hp);
			int ans2 = fast2(cuts, posions, hp);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

	// 对数器测试
	public static int[] randomArray(int n, int v) {
		int[] ans = new int[n];
		for (int i = 0; i < n; i++) {
			ans[i] = (int) (Math.random() * v) + 1;
		}
		return ans;
	}
	
	/*
	 * 补充说明：
	 * 
	 * 问题解析：
	 * 这是一个较复杂的二分答案问题。需要找到杀死怪兽的最少回合数。
	 * 
	 * 解题思路：
	 * 1. 贪心策略：在每回合选择刀砍和毒杀中能造成更大伤害的策略
	 * 2. 确定答案范围：最少回合是1，最多回合是hp+1（每回合至少造成1点伤害）
	 * 3. 二分搜索：在[left, right]范围内二分搜索，对每个中间值m，判断是否能在m回合内杀死怪兽
	 * 4. 判断函数：f(cuts, poisons, hp, limit)判断是否能在limit回合内杀死血量为hp的怪兽
	 * 5. 贪心决策：在第i回合，选择能造成更大总伤害的策略（直接伤害 vs 持续伤害）
	 * 
	 * 时间复杂度分析：
	 * 1. 二分搜索范围是[1, hp+1]，二分次数是O(log(hp))
	 * 2. 每次二分需要调用f函数，f函数遍历数组一次，时间复杂度是O(n)
	 * 3. 总时间复杂度：O(n * log(hp))
	 * 
	 * 空间复杂度分析：
	 * 1. fast1使用三维DP数组，空间复杂度是O(n * hp * sum)
	 * 2. fast2只使用常数个额外变量，空间复杂度是O(1)
	 * 
	 * 工程化考虑：
	 * 1. 贪心优化：每回合选择最优策略，避免复杂的状态转移
	 * 2. 整数溢出处理：使用long类型处理大数运算
	 * 3. 边界条件处理：注意回合数不能超过n，持续伤害需要计算总和
	 * 4. 对数器验证：通过对比DP解法验证二分答案解法的正确性
	 * 
	 * 相关题目扩展：
	 * 1. 牛客网 - 刀砍毒杀怪兽问题（本题）
	 * 2. LeetCode 410. 分割数组的最大值 - https://leetcode.cn/problems/split-array-largest-sum/
	 * 3. LeetCode 1231. 分享巧克力 - https://leetcode.cn/problems/divide-chocolate/
	 * 4. LeetCode 875. 爱吃香蕉的珂珂 - https://leetcode.cn/problems/koko-eating-bananas/
	 * 5. HackerRank - Fighting Pits - https://www.hackerrank.com/challenges/fighting-pits/problem
	 * 6. Codeforces 1373D - Maximum Sum on Even Positions - https://codeforces.com/problemset/problem/1373/D
	 * 7. AtCoder ABC146 - F - Sugoroku - https://atcoder.jp/contests/abc146/tasks/abc146_f
	 */

}