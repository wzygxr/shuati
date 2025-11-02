package class051;

// 同时运行N台电脑的最长时间
// 你有 n 台电脑。给你整数 n 和一个下标从 0 开始的整数数组 batteries
// 其中第 i 个电池可以让一台电脑 运行 batteries[i] 分钟
// 你想使用这些电池让 全部 n 台电脑 同时 运行。
// 一开始，你可以给每台电脑连接 至多一个电池
// 然后在任意整数时刻，你都可以将一台电脑与它的电池断开连接，并连接另一个电池，你可以进行这个操作 任意次
// 新连接的电池可以是一个全新的电池，也可以是别的电脑用过的电池
// 断开连接和连接新的电池不会花费任何时间。
// 注意，你不能给电池充电。
// 请你返回你可以让 n 台电脑同时运行的 最长 分钟数。
// 测试链接 : https://leetcode.cn/problems/maximum-running-time-of-n-computers/
public class Code05_MaximumRunningTimeOfNComputers {

	// 单纯的二分答案法
	// 提交时把函数名改为maxRunTime
	// 时间复杂度O(n * log(sum))，额外空间复杂度O(1)
	public static long maxRunTime1(int num, int[] arr) {
		long sum = 0;
		for (int x : arr) {
			sum += x;
		}
		long ans = 0;
		// [0, sum]，不停二分
		for (long l = 0, r = sum, m; l <= r;) {
			// m中点，让num台电脑共同运行m分钟，能不能做到
			m = l + ((r - l) >> 1);
			if (f1(arr, num, m)) {
				ans = m;
				l = m + 1;
			} else {
				r = m - 1;
			}
		}
		return ans;
	}

	// 让num台电脑共同运行time分钟，能不能做到
	public static boolean f1(int[] arr, int num, long time) {
		// 碎片电量总和
		long sum = 0;
		for (int x : arr) {
			if (x > time) {
				num--;
			} else {
				// x <= time，是碎片电池
				sum += x;
			}
			if (sum >= (long) num * time) {
				// 碎片电量 >= 台数 * 要求
				return true;
			}
		}
		return false;
	}

	// 二分答案法 + 增加分析(贪心)
	// 提交时把函数名改为maxRunTime
	// 时间复杂度O(n * log(max))，额外空间复杂度O(1)
	public static long maxRunTime2(int num, int[] arr) {
		int max = 0;
		long sum = 0;
		for (int x : arr) {
			max = Math.max(max, x);
			sum += x;
		}
		// 就是增加了这里的逻辑
		if (sum > (long) max * num) {
			// 所有电池的最大电量是max
			// 如果此时sum > (long) max * num，
			// 说明 : 最终的供电时间一定在 >= max，而如果最终的供电时间 >= max
			// 说明 : 对于最终的答案X来说，所有电池都是课上讲的"碎片拼接"的概念
			// 那么寻找 ? * num <= sum 的情况中，尽量大的 ? 即可
			// 即sum / num
			return sum / num;
		}
		// 最终的供电时间一定在 < max范围上
		// [0, sum]二分范围，可能定的比较粗，虽然不影响，但毕竟是有点慢
		// [0, max]二分范围！更精细的范围，二分次数会变少
		int ans = 0;
		for (int l = 0, r = max, m; l <= r;) {
			m = l + ((r - l) >> 1);
			if (f2(arr, num, m)) {
				ans = m;
				l = m + 1;
			} else {
				r = m - 1;
			}
		}
		return ans;
	}

	public static boolean f2(int[] arr, int num, int time) {
		// 碎片电量总和
		long sum = 0;
		for (int x : arr) {
			if (x > time) {
				num--;
			} else {
				sum += x;
			}
			if (sum >= (long) num * time) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 补充说明：
	 * 
	 * 问题解析：
	 * 这是一个较复杂的二分答案问题。需要找到n台电脑同时运行的最长时间。
	 * 
	 * 解题思路：
	 * 1. 关键观察：如果一个电池电量超过目标时间，它就只能给一台电脑持续供电
	 * 2. 其他电量不超过目标时间的电池可以灵活分配，称为"碎片电池"
	 * 3. 确定答案范围：最小运行时间是0，最大运行时间是sum/n（所有电池平均分配）
	 * 4. 二分搜索：在[left, right]范围内二分搜索，对每个中间值m，判断是否能让n台电脑运行m分钟
	 * 5. 判断函数：f(arr, num, time)判断是否能让num台电脑同时运行time分钟
	 * 6. 贪心策略：电量超过time的电池单独给一台电脑供电，其余电池灵活分配
	 * 
	 * 时间复杂度分析：
	 * 1. 二分搜索范围是[0, sum]或[0, max]，二分次数是O(log(sum))或O(log(max))
	 * 2. 每次二分需要调用f函数，f函数遍历数组一次，时间复杂度是O(n)
	 * 3. 总时间复杂度：O(n * log(sum))或O(n * log(max))
	 * 
	 * 空间复杂度分析：
	 * 只使用了常数个额外变量，空间复杂度是O(1)
	 * 
	 * 工程化考虑：
	 * 1. 贪心优化：通过数学分析减少搜索范围，提高效率
	 * 2. 整数溢出处理：使用long类型处理大数运算
	 * 3. 边界条件处理：区分"大电池"和"碎片电池"，采用不同策略
	 * 
	 * 相关题目扩展：
	 * 1. LeetCode 2141. 同时运行N台电脑的最长时间 - https://leetcode.cn/problems/maximum-running-time-of-n-computers/
	 * 2. LeetCode 875. 爱吃香蕉的珂珂 - https://leetcode.cn/problems/koko-eating-bananas/
	 * 3. LeetCode 410. 分割数组的最大值 - https://leetcode.cn/problems/split-array-largest-sum/
	 * 4. LeetCode 1011. 在D天内送达包裹的能力 - https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
	 * 5. HackerRank - Max Min - https://www.hackerrank.com/challenges/angry-children/problem
	 * 6. Codeforces 1373B - 01 Game - https://codeforces.com/problemset/problem/1373/B
	 * 7. AtCoder ABC146 - E - Rem of Sum is Num - https://atcoder.jp/contests/abc146/tasks/abc146_e
	 */

}