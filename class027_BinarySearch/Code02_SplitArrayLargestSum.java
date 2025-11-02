package class051;

// 分割数组的最大值(画匠问题)
// 给定一个非负整数数组 nums 和一个整数 m
// 你需要将这个数组分成 m 个非空的连续子数组。
// 设计一个算法使得这 m 个子数组各自和的最大值最小。
// 测试链接 : https://leetcode.cn/problems/split-array-largest-sum/
public class Code02_SplitArrayLargestSum {

	// 时间复杂度O(n * log(sum))，额外空间复杂度O(1)
	public static int splitArray(int[] nums, int k) {
		long sum = 0;
		for (int num : nums) {
			sum += num;
		}
		long ans = 0;
		// [0,sum]二分
		for (long l = 0, r = sum, m, need; l <= r;) {
			// 中点m
			m = l + ((r - l) >> 1);
			// 必须让数组每一部分的累加和 <= m，请问划分成几个部分才够!
			need = f(nums, m);
			if (need <= k) {
				// 达标
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return (int) ans;
	}

	// 必须让数组arr每一部分的累加和 <= limit，请问划分成几个部分才够!
	// 返回需要的部分数量
	public static int f(int[] arr, long limit) {
		int parts = 1;
		int sum = 0;
		for (int num : arr) {
			if (num > limit) {
				return Integer.MAX_VALUE;
			}
			if (sum + num > limit) {
				parts++;
				sum = num;
			} else {
				sum += num;
			}
		}
		return parts;
	}
	
	/*
	 * 补充说明：
	 * 
	 * 问题解析：
	 * 这是一个经典的二分答案问题，也被称为"画匠问题"。目标是将数组分成k个连续子数组，
	 * 使得所有子数组和的最大值尽可能小。
	 * 
	 * 解题思路：
	 * 1. 确定答案范围：最小值是数组中最大元素（每个元素单独成一组），最大值是数组元素和（整个数组为一组）
	 * 2. 二分搜索：在[left, right]范围内二分搜索，对每个中间值m，计算最少需要划分成多少段才能保证每段和不超过m
	 * 3. 判断函数：f(arr, limit)计算数组arr中每段和不超过limit的最少段数
	 * 4. 根据f的结果调整搜索范围，最终找到满足划分成k段条件的最小最大值
	 * 
	 * 时间复杂度分析：
	 * 1. 二分搜索范围是[0, sum]，其中sum是数组元素和，二分次数是O(log(sum))
	 * 2. 每次二分需要调用f函数，f函数遍历数组一次，时间复杂度是O(n)
	 * 3. 总时间复杂度：O(n * log(sum))
	 * 
	 * 空间复杂度分析：
	 * 只使用了常数个额外变量，空间复杂度是O(1)
	 * 
	 * 工程化考虑：
	 * 1. 注意整数溢出：使用long类型处理sum，避免计算过程中溢出
	 * 2. 边界条件处理：当某个元素大于limit时，无法满足条件，返回Integer.MAX_VALUE
	 * 3. 贪心策略：在f函数中采用贪心策略，尽可能在每段中放更多元素
	 * 
	 * 相关题目扩展：
	 * 1. LeetCode 1011. 在D天内送达包裹的能力 - https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
	 * 2. LeetCode 875. 爱吃香蕉的珂珂 - https://leetcode.cn/problems/koko-eating-bananas/
	 * 3. LeetCode 1231. 分享巧克力 - https://leetcode.cn/problems/divide-chocolate/
	 * 4. LeetCode 1482. 制作m束花所需的时间 - https://leetcode.cn/problems/minimum-number-of-days-to-make-m-bouquets/
	 * 5. 牛客网 NC163 机器人跳跃问题 - https://www.nowcoder.com/practice/7037a3d57bbd4336856b8e16a9cafd71
	 * 6. HackerRank - Fair Rations - https://www.hackerrank.com/challenges/fair-rations/problem
	 * 7. Codeforces 460C - Present - https://codeforces.com/problemset/problem/460/C
	 * 8. AtCoder ABC146 - C - Buy an Integer - https://atcoder.jp/contests/abc146/tasks/abc146_c
	 */

}