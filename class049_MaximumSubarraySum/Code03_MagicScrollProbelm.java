package class071;

// 魔法卷轴
// 给定一个数组nums，其中可能有正、负、0
// 每个魔法卷轴可以把nums中连续的一段全变成0
// 你希望数组整体的累加和尽可能大
// 卷轴使不使用、使用多少随意，但一共只有2个魔法卷轴
// 请返回数组尽可能大的累加和
// 对数器验证
public class Code03_MagicScrollProbelm {

	/*
	 * 解题思路:
	 * 这是一个复杂的动态规划问题，需要考虑使用0、1或2个卷轴的情况。
	 * 
	 * 解法分为三部分：
	 * 1. 不使用卷轴：直接计算数组所有元素的和
	 * 2. 使用1个卷轴：找出一段连续子数组，将其变为0，使得剩余元素和最大
	 * 3. 使用2个卷轴：找出两段不重叠的连续子数组，将它们变为0，使得剩余元素和最大
	 * 
	 * 对于使用1个卷轴的情况，我们可以用前缀最大值和后缀最大值来优化：
	 * - prefix[i] 表示在 0~i 范围内使用1次卷轴能得到的最大累加和
	 * - suffix[i] 表示在 i~n-1 范围内使用1次卷轴能得到的最大累加和
	 * 
	 * 对于使用2个卷轴的情况，我们需要枚举分割点：
	 * - 枚举所有可能的分割点 i，使得 0~i-1 作为左半部分，i~n-1 作为右半部分
	 * - 左半部分使用1次卷轴的最大值为 prefix[i-1]
	 * - 右半部分使用1次卷轴的最大值为 suffix[i]
	 * - 两者之和就是使用2次卷轴的最大值
	 * 
	 * 时间复杂度: O(n) - 需要遍历数组常数次
	 * 空间复杂度: O(n) - 需要前缀和后缀数组
	 * 
	 * 是否最优解: 是，这是该问题的最优解法
	 */

	// 暴力方法
	// 为了测试
	public static int maxSum1(int[] nums) {
		int p1 = 0;
		for (int num : nums) {
			p1 += num;
		}
		int n = nums.length;
		int p2 = mustOneScroll(nums, 0, n - 1);
		int p3 = Integer.MIN_VALUE;
		for (int i = 1; i < n; i++) {
			p3 = Math.max(p3, mustOneScroll(nums, 0, i - 1) + mustOneScroll(nums, i, n - 1));
		}
		return Math.max(p1, Math.max(p2, p3));
	}

	// 暴力方法
	// 为了测试
	// nums[l...r]范围上一定要用一次卷轴情况下的最大累加和
	public static int mustOneScroll(int[] nums, int l, int r) {
		int ans = Integer.MIN_VALUE;
		// l...r范围上包含a...b范围
		// 如果a...b范围上的数字都变成0
		// 返回剩下数字的累加和
		// 所以枚举所有可能的a...b范围
		// 相当暴力，但是正确
		for (int a = l; a <= r; a++) {
			for (int b = a; b <= r; b++) {
				// l...a...b...r
				int curAns = 0;
				for (int i = l; i < a; i++) {
					curAns += nums[i];
				}
				for (int i = b + 1; i <= r; i++) {
					curAns += nums[i];
				}
				ans = Math.max(ans, curAns);
			}
		}
		return ans;
	}

	// 正式方法
	// 时间复杂度O(n)
	public static int maxSum2(int[] nums) {
		int n = nums.length;
		if (n == 0) {
			return 0;
		}
		// 情况1 : 完全不使用卷轴
		int p1 = 0;
		for (int num : nums) {
			p1 += num;
		}
		// prefix[i] : 0~i范围上一定要用1次卷轴的情况下，0~i范围上整体最大累加和多少
		int[] prefix = new int[n];
		// 每一步的前缀和
		int sum = nums[0];
		// maxPresum : 之前所有前缀和的最大值
		int maxPresum = Math.max(0, nums[0]);
		for (int i = 1; i < n; i++) {
			prefix[i] = Math.max(prefix[i - 1] + nums[i], maxPresum);
			sum += nums[i];
			maxPresum = Math.max(maxPresum, sum);
		}
		// 情况二 : 必须用1次卷轴
		int p2 = prefix[n - 1];
		// suffix[i] : i~n-1范围上一定要用1次卷轴的情况下，i~n-1范围上整体最大累加和多少
		int[] suffix = new int[n];
		sum = nums[n - 1];
		maxPresum = Math.max(0, sum);
		for (int i = n - 2; i >= 0; i--) {
			suffix[i] = Math.max(nums[i] + suffix[i + 1], maxPresum);
			sum += nums[i];
			maxPresum = Math.max(maxPresum, sum);
		}
		// 情况二 : 必须用2次卷轴
		int p3 = Integer.MIN_VALUE;
		for (int i = 1; i < n; i++) {
			// 枚举所有的划分点i
			// 0~i-1 左
			// i~n-1 右
			p3 = Math.max(p3, prefix[i - 1] + suffix[i]);
		}
		return Math.max(p1, Math.max(p2, p3));
	}

	// 为了测试
	public static int[] randomArray(int n, int v) {
		int[] ans = new int[n];
		for (int i = 0; i < n; i++) {
			ans[i] = (int) (Math.random() * (v * 2 + 1)) - v;
		}
		return ans;
	}

	// 为了测试
	public static void main(String[] args) {
		int n = 50;
		int v = 100;
		int testTime = 10000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int len = (int) (Math.random() * n);
			int[] nums = randomArray(len, v);
			int ans1 = maxSum1(nums);
			int ans2 = maxSum2(nums);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}
	
	/*
	 * 相关题目扩展:
	 * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
	 * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
	 * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
	 * 4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
	 * 5. LeetCode 487. 最大连续1的个数 II - https://leetcode.cn/problems/max-consecutive-ones-ii/
	 */

}