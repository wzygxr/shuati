package class071;

// 删掉1个数字后长度为k的子数组最大累加和
// 给定一个数组nums，求必须删除一个数字后的新数组中
// 长度为k的子数组最大累加和，删除哪个数字随意
// 对数器验证
public class Code06_DeleteOneNumberLengthKMaxSum {

	/*
	 * 解题思路:
	 * 这个问题结合了删除元素和固定长度子数组最大和两个概念。
	 * 
	 * 暴力解法是枚举删除的元素，然后计算新数组中长度为k的子数组最大和，但时间复杂度较高。
	 * 
	 * 优化解法使用滑动窗口和单调队列：
	 * 1. 枚举删除的元素位置
	 * 2. 对于每个删除位置，使用滑动窗口计算长度为k的子数组最大和
	 * 3. 使用单调队列优化滑动窗口的最大值查询
	 * 
	 * 更进一步的优化：
	 * 我们可以转换思路，不是枚举删除哪个元素，而是枚举长度为k的子数组，
	 * 然后在这个子数组中删除一个元素使得剩余元素和最大。
	 * 
	 * 但这道题要求的是在删除一个元素后的新数组中找长度为k的子数组最大和，
	 * 所以我们需要枚举删除位置，然后在新数组中用滑动窗口找最大和。
	 * 
	 * 使用单调队列优化：
	 * 单调队列可以维护滑动窗口中的最小值，这样删除最小值就能得到最大和。
	 * 
	 * 时间复杂度: O(n) - 每个元素最多入队和出队一次
	 * 空间复杂度: O(n) - 单调队列的空间
	 * 
	 * 是否最优解: 是，这是该问题的最优解法
	 */

	// 暴力方法
	// 为了测试
	public static int maxSum1(int[] nums, int k) {
		int n = nums.length;
		if (n <= k) {
			return 0;
		}
		int ans = Integer.MIN_VALUE;
		for (int i = 0; i < n; i++) {
			int[] rest = delete(nums, i);
			ans = Math.max(ans, lenKmaxSum(rest, k));
		}
		return ans;
	}

	// 暴力方法
	// 为了测试
	// 删掉index位置的元素，然后返回新数组
	public static int[] delete(int[] nums, int index) {
		int len = nums.length - 1;
		int[] ans = new int[len];
		int i = 0;
		for (int j = 0; j < nums.length; j++) {
			if (j != index) {
				ans[i++] = nums[j];
			}
		}
		return ans;
	}

	// 暴力方法
	// 为了测试
	// 枚举每一个子数组找到最大累加和
	public static int lenKmaxSum(int[] nums, int k) {
		int n = nums.length;
		int ans = Integer.MIN_VALUE;
		for (int i = 0; i <= n - k; i++) {
			int cur = 0;
			for (int j = i, cnt = 0; cnt < k; j++, cnt++) {
				cur += nums[j];
			}
			ans = Math.max(ans, cur);
		}
		return ans;
	}

	// 正式方法
	// 时间复杂度O(N)
	public static int maxSum2(int[] nums, int k) {
		int n = nums.length;
		if (n <= k) {
			return 0;
		}
		// 单调队列 : 维持窗口内最小值的更新结构，讲解054的内容
		int[] window = new int[n];
		int l = 0;
		int r = 0;
		// 窗口累加和
		long sum = 0;
		int ans = Integer.MIN_VALUE;
		for (int i = 0; i < n; i++) {
			// 单调队列 : i位置进入单调队列
			while (l < r && nums[window[r - 1]] >= nums[i]) {
				r--;
			}
			window[r++] = i;
			sum += nums[i];
			if (i >= k) {
				ans = Math.max(ans, (int) (sum - nums[window[l]]));
				if (window[l] == i - k) {
					// 单调队列 : 如果单调队列最左侧的位置过期了，从队列中弹出
					l++;
				}
				sum -= nums[i - k];
			}
		}
		return ans;
	}

	// 为了测试
	// 生成长度为n，值在[-v, +v]之间的随机数组
	public static int[] randomArray(int n, int v) {
		int[] ans = new int[n];
		for (int i = 0; i < n; i++) {
			ans[i] = (int) (Math.random() * (2 * v + 1)) - v;
		}
		return ans;
	}

	// 为了测试
	// 对数器
	public static void main(String[] args) {
		int n = 200;
		int v = 1000;
		int testTimes = 10000;
		System.out.println("测试开始");
		for (int i = 0; i < testTimes; i++) {
			int len = (int) (Math.random() * n) + 1;
			int[] nums = randomArray(len, v);
			int k = (int) (Math.random() * n) + 1;
			int ans1 = maxSum1(nums, k);
			int ans2 = maxSum2(nums, k);
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
	 * 5. LeetCode 239. 滑动窗口最大值 - https://leetcode.cn/problems/sliding-window-maximum/
	 */

}