package class071;

// 三个无重叠子数组的最大和
// 给你一个整数数组 nums 和一个整数 k
// 找出三个长度为 k 、互不重叠、且全部数字和（3 * k 项）最大的子数组
// 并返回这三个子数组
// 以下标的数组形式返回结果，数组中的每一项分别指示每个子数组的起始位置
// 如果有多个结果，返回字典序最小的一个
// 测试链接 : https://leetcode.cn/problems/maximum-sum-of-3-non-overlapping-subarrays/
public class Code04_MaximumSum3UnoverlappingSubarrays {

	/*
	 * 解题思路:
	 * 这是一个复杂的滑动窗口和动态规划结合的问题。
	 * 
	 * 解法步骤：
	 * 1. 首先计算所有长度为k的子数组的和，存储在sums数组中
	 * 2. 计算前缀最大值数组prefix，prefix[i]表示在0~i范围内和最大的子数组起始位置
	 * 3. 计算后缀最大值数组suffix，suffix[i]表示在i~n-1范围内和最大的子数组起始位置
	 * 4. 枚举中间子数组的位置，结合prefix和suffix数组找出三个子数组的最大和
	 * 
	 * 详细解释：
	 * - sums[i] 表示以i开头、长度为k的子数组的和
	 * - prefix[i] 表示在0~i范围内，和最大的长度为k的子数组的起始位置
	 * - suffix[i] 表示在i~n-1范围内，和最大的长度为k的子数组的起始位置
	 * 
	 * 枚举中间子数组的位置i（范围是[k, n-k-1]），那么：
	 * - 左边最优子数组起始位置为prefix[i-k]
	 * - 中间子数组起始位置为i
	 * - 右边最优子数组起始位置为suffix[i+k]
	 * 
	 * 时间复杂度: O(n) - 需要遍历数组常数次
	 * 空间复杂度: O(n) - 需要额外数组存储子数组和、前缀最大值和后缀最大值
	 * 
	 * 是否最优解: 是，这是该问题的最优解法
	 */

	public static int[] maxSumOfThreeSubarrays(int[] nums, int k) {
		int n = nums.length;
		// sums[i] : 以i开头并且长度为k的子数组的累加和
		int[] sums = new int[n];
		for (int l = 0, r = 0, sum = 0; r < n; r++) {
			// l....r
			sum += nums[r];
			if (r - l + 1 == k) {
				sums[l] = sum;
				sum -= nums[l];
				l++;
			}
		}
		// prefix[i] :
		// 0~i范围上所有长度为k的子数组中，拥有最大累加和的子数组，是以什么位置开头的
		int[] prefix = new int[n];
		for (int l = 1, r = k; r < n; l++, r++) {
			if (sums[l] > sums[prefix[r - 1]]) {
				// 注意>，为了同样最大累加和的情况下，最小的字典序
				prefix[r] = l;
			} else {
				prefix[r] = prefix[r - 1];
			}
		}
		// suffix[i] :
		// i~n-1范围上所有长度为k的子数组中，拥有最大累加和的子数组，是以什么位置开头的
		int[] suffix = new int[n];
		suffix[n - k] = n - k;
		for (int l = n - k - 1; l >= 0; l--) {
			if (sums[l] >= sums[suffix[l + 1]]) {
				// 注意>=，为了同样最大累加和的情况下，最小的字典序
				suffix[l] = l;
			} else {
				suffix[l] = suffix[l + 1];
			}
		}
		int a = 0, b = 0, c = 0, max = 0;
		// 0...i-1    i...j    j+1...n-1
		//   左     中(长度为k)     右
		for (int p, s, i = k, j = 2 * k - 1, sum; j < n - k; i++, j++) {
			// 0.....i-1   i.....j  j+1.....n-1
			// 最好开头p      i开头     最好开头s
			p = prefix[i - 1];
			s = suffix[j + 1];
			sum = sums[p] + sums[i] + sums[s];
			if (sum > max) {
				// 注意>，为了同样最大累加和的情况下，最小的字典序
				max = sum;
				a = p;
				b = i;
				c = s;
			}
		}
		return new int[] { a, b, c };
	}
	
	/*
	 * 相关题目扩展:
	 * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
	 * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
	 * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
	 * 4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
	 * 5. LeetCode 123. 买卖股票的最佳时机 III - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/
	 */

}