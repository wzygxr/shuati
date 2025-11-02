package class051;

// 完成旅途的最少时间(题目6的在线测试)
// 有同学找到了在线测试链接，和课上讲的题目6几乎是一个意思，但是有细微差别
// 实现的代码，除了一些变量需要改成long类型之外，仅有两处关键逻辑不同，都打上了注释
// 除此之外，和课上讲的题目6的实现，再无区别
// 可以仔细阅读如下测试链接里的题目，重点关注此题和题目6，在题意上的差别
// 测试链接 : https://leetcode.cn/problems/minimum-time-to-complete-trips/
public class Code06_WaitingTime2 {

	public static long minimumTime(int[] arr, int w) {
		int min = Integer.MAX_VALUE;
		for (int x : arr) {
			min = Math.min(min, x);
		}
		long ans = 0;
		for (long l = 0, r = (long) min * w, m; l <= r;) {
			m = l + ((r - l) >> 1);
			// 这里逻辑和课上讲的不同
			// 课上讲的题意，是需要等多少人才能获得服务，你是第w+1个
			// 在线测试的题意，是需要完成w趟旅行
			if (f(arr, m) >= w) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}

	public static long f(int[] arr, long time) {
		long ans = 0;
		for (int num : arr) {
			// 这里逻辑和课上讲的不同
			// 课上讲的题意，计算time时间内，(完成 + 开始)服务的人数，需要+1
			// 在线测试的题意，计算time时间内，能完成多少旅行，不需要+1
			ans += (time / num);
		}
		return ans;
	}
	
	/*
	 * 补充说明：
	 * 
	 * 问题解析：
	 * 这是Code06_WaitingTime的在线测试版本。需要计算完成指定旅行次数的最少时间。
	 * 
	 * 解题思路：
	 * 1. 与Code06_WaitingTime基本相同，但题意有细微差别
	 * 2. 确定答案范围：最少时间是0，最多时间是min * w（最慢车完成所有旅行）
	 * 3. 二分搜索：在[left, right]范围内二分搜索，对每个中间值t，计算在时间t内能完成多少旅行
	 * 4. 判断函数：f(arr, time)计算在time时间内所有车能完成的旅行总数
	 * 5. 根据f的结果调整搜索范围，最终找到能完成w次旅行的最少时间
	 * 
	 * 时间复杂度分析：
	 * 1. 二分搜索范围是[0, min * w]，二分次数是O(log(min * w))
	 * 2. 每次二分需要调用f函数，f函数遍历数组一次，时间复杂度是O(n)
	 * 3. 总时间复杂度：O(n * log(min * w))
	 * 
	 * 空间复杂度分析：
	 * 只使用了常数个额外变量，空间复杂度是O(1)
	 * 
	 * 工程化考虑：
	 * 1. 变量类型：由于数据范围较大，使用long类型避免溢出
	 * 2. 题意理解：仔细区分"完成w趟旅行"和"服务w+1个客人"的差别
	 * 3. 计算方式：完成旅行数不需要+1，而服务客人数需要+1
	 * 
	 * 相关题目扩展：
	 * 1. LeetCode 2187. 完成旅途的最少时间 - https://leetcode.cn/problems/minimum-time-to-complete-trips/
	 * 2. LeetCode 875. 爱吃香蕉的珂珂 - https://leetcode.cn/problems/koko-eating-bananas/
	 * 3. LeetCode 1283. 使结果不超过阈值的最小除数 - https://leetcode.cn/problems/find-the-smallest-divisor-given-a-threshold/
	 * 4. LeetCode 1011. 在D天内送达包裹的能力 - https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
	 * 5. HackerRank - Maximum Subarray Sum - https://www.hackerrank.com/challenges/maximum-subarray-sum/problem
	 * 6. Codeforces 1363C - Game On Leaves - https://codeforces.com/problemset/problem/1363/C
	 * 7. AtCoder ABC146 - E - Rem of Sum is Num - https://atcoder.jp/contests/abc146/tasks/abc146_e
	 */

}