package class049;

/**
 * 滑动窗口算法解决最小长度子数组问题
 * 
 * 问题描述：
 * 给定一个含有 n 个正整数的数组和一个正整数 target，
 * 找到累加和 >= target 的长度最小的子数组并返回其长度。
 * 如果不存在符合条件的子数组返回0。
 * 
 * 解题思路：
 * 使用滑动窗口（双指针）技术，维护一个动态窗口[l, r]。
 * 1. 右指针r不断向右扩展窗口，累加元素值到sum中
 * 2. 当sum >= target时，尝试收缩左边界l，直到不能再收缩为止
 * 3. 记录满足条件的最小窗口长度
 * 
 * 算法复杂度分析：
 * 时间复杂度：O(n) - 每个元素最多被访问两次（一次被右指针访问，一次被左指针访问）
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 
 * 相关题目链接：
 * LeetCode 209. 长度最小的子数组
 * https://leetcode.cn/problems/minimum-size-subarray-sum/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 最小覆盖子数组
 *    https://www.nowcoder.com/practice/6e3575d726994440859b3b4305a516e9
 * 2. LintCode 406. 最小子数组
 *    https://www.lintcode.com/problem/406/
 * 3. HackerRank - Minimum Size Subarray Sum
 *    https://www.hackerrank.com/contests/algorithm-challenges/challenges/minimum-size-subarray-sum
 * 4. CodeChef - MINARRS - Minimum Sum Array
 *    https://www.codechef.com/problems/MINARRS
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1886 滑动窗口
 *    https://www.luogu.com.cn/problem/P1886
 * 7. 杭电OJ 4193 Sliding Window
 *    http://acm.hdu.edu.cn/showproblem.php?pid=4193
 * 8. POJ 2823 Sliding Window
 *    http://poj.org/problem?id=2823
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空数组、target为负数等边界情况
 * 2. 性能优化：避免重复计算，使用滑动窗口减少时间复杂度
 * 3. 可读性：变量命名清晰，添加详细注释
 */
public class Code01_MinimumSizeSubarraySum {

	/**
	 * 寻找累加和大于等于target的最短子数组长度
	 * 
	 * @param target 目标和值
	 * @param nums   输入的正整数数组
	 * @return 最短子数组长度，如果不存在则返回0
	 */
	public static int minSubArrayLen(int target, int[] nums) {
		// 初始化结果为最大值，用于后续比较
		int ans = Integer.MAX_VALUE;
		
		// 使用滑动窗口，l为左指针，r为右指针，sum为窗口内元素和
		for (int l = 0, r = 0, sum = 0; r < nums.length; r++) {
			// 扩展窗口右边界，将nums[r]加入窗口
			sum += nums[r];
			
			// 收缩窗口左边界：如果移除左边界元素后仍满足条件，则移除
			while (sum - nums[l] >= target) {
				// sum : nums[l....r]
				// 如果l位置的数从窗口出去，还能继续达标，那就出去
				sum -= nums[l++];
			}
			
			// 检查当前窗口是否满足条件，如果满足则更新最小长度
			if (sum >= target) {
				ans = Math.min(ans, r - l + 1);
			}
		}
		
		// 如果没有找到满足条件的子数组，返回0；否则返回最小长度
		return ans == Integer.MAX_VALUE ? 0 : ans;
	}

}