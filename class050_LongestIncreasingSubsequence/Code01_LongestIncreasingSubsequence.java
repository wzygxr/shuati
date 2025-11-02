package class072;

// 最长递增子序列和最长不下降子序列
// 给定一个整数数组nums
// 找到其中最长严格递增子序列长度、最长不下降子序列长度
// 测试链接 : https://leetcode.cn/problems/longest-increasing-subsequence/
public class Code01_LongestIncreasingSubsequence {

	// 普通解法的动态规划
	// 时间复杂度O(n^2)，数组稍大就会超时
	/**
	 * 使用动态规划计算最长严格递增子序列的长度
	 * 
	 * 算法思路：
	 * 1. dp[i]表示以nums[i]结尾的最长严格递增子序列的长度
	 * 2. 对于每个位置i，遍历前面所有位置j，如果nums[j] < nums[i]，
	 *    则可以将nums[i]接到以nums[j]结尾的递增子序列后面
	 * 3. 状态转移方程：dp[i] = max(dp[j] + 1) for all j < i and nums[j] < nums[i]
	 * 4. 初始值：每个元素单独成序列，dp[i] = 1
	 * 
	 * 时间复杂度：O(n^2) - 外层循环n次，内层循环最多n次
	 * 空间复杂度：O(n) - 需要dp数组存储状态
	 * 是否最优解：否，存在O(n*logn)的优化解法
	 * 
	 * @param nums 输入的整数数组
	 * @return 最长严格递增子序列的长度
	 */
	public static int lengthOfLIS1(int[] nums) {
		int n = nums.length;
		// dp[i]表示以nums[i]结尾的最长严格递增子序列的长度
		int[] dp = new int[n];
		int ans = 0;
		// 遍历每个位置
		for (int i = 0; i < n; i++) {
			// 每个元素至少可以单独构成一个长度为1的子序列
			dp[i] = 1;
			// 遍历前面所有位置，寻找可以接在后面的递增子序列
			for (int j = 0; j < i; j++) {
				// 如果nums[j] < nums[i]，说明可以将nums[i]接到以nums[j]结尾的子序列后面
				if (nums[j] < nums[i]) {
					// 更新dp[i]为所有可能情况中的最大值
					dp[i] = Math.max(dp[i], dp[j] + 1);
				}
			}
			// 更新全局最大值
			ans = Math.max(ans, dp[i]);
		}
		return ans;
	}

	// 最优解
	// 时间复杂度O(n * logn)
	/**
	 * 使用贪心+二分查找计算最长严格递增子序列的长度
	 * 
	 * 算法思路：
	 * 1. 维护一个数组ends，ends[i]表示长度为i+1的所有递增子序列中，结尾元素的最小值
	 * 2. 贪心思想：为了让递增子序列尽可能长，我们希望结尾元素尽可能小
	 * 3. 对于每个元素num，在ends数组中二分查找>=num的最左位置
	 *    - 如果找不到，说明num比所有元素都大，可以延长递增子序列
	 *    - 如果找到了位置find，将ends[find]更新为num
	 * 
	 * 时间复杂度：O(n*logn) - 遍历n个元素，每次二分查找O(logn)
	 * 空间复杂度：O(n) - 需要ends数组存储状态
	 * 是否最优解：是，这是目前求LIS长度的最优解法
	 * 
	 * @param nums 输入的整数数组
	 * @return 最长严格递增子序列的长度
	 */
	public static int lengthOfLIS2(int[] nums) {
		int n = nums.length;
		// ends[i]表示长度为i+1的所有递增子序列中，结尾元素的最小值
		int[] ends = new int[n];
		// len表示ends数组目前的有效区长度
		// ends[0...len-1]是有效区，有效区内的数字一定严格升序
		int len = 0;
		// 遍历数组中的每个元素
		for (int i = 0, find; i < n; i++) {
			// 在ends数组中查找>=nums[i]的最左位置
			find = bs1(ends, len, nums[i]);
			// 如果找不到，说明nums[i]比所有元素都大，可以延长递增子序列
			if (find == -1) {
				ends[len++] = nums[i];
			} else {
				// 如果找到了位置，更新该位置的值为nums[i]
				ends[find] = nums[i];
			}
		}
		return len;
	}

	// "最长递增子序列"使用如下二分搜索 :
	// ends[0...len-1]是严格升序的，找到>=num的最左位置
	// 如果不存在返回-1
	/**
	 * 在严格升序数组ends中查找>=num的最左位置
	 * 
	 * 算法思路：
	 * 1. 使用二分查找在有序数组中查找目标值
	 * 2. 维护左边界l和右边界r
	 * 3. 计算中间位置m，比较ends[m]与num的大小关系
	 * 4. 如果ends[m] >= num，说明目标位置在左半部分（包括m），更新ans和r
	 * 5. 否则目标位置在右半部分，更新l
	 * 
	 * 时间复杂度：O(logn) - 标准二分查找
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 是否最优解：是，这是标准的二分查找实现
	 * 
	 * @param ends 严格升序数组
	 * @param len 有效长度
	 * @param num 目标值
	 * @return >=num的最左位置，如果不存在返回-1
	 */
	public static int bs1(int[] ends, int len, int num) {
		int l = 0, r = len - 1, m, ans = -1;
		while (l <= r) {
			m = (l + r) / 2;
			// 如果ends[m] >= num，记录当前位置并继续在左半部分查找
			if (ends[m] >= num) {
				ans = m;
				r = m - 1;
			} else {
				// 否则在右半部分查找
				l = m + 1;
			}
		}
		return ans;
	}

	// 如果求最长不下降子序列，那么使用如下的二分搜索 :
	// ends[0...len-1]是不降序的
	// 在其中找到>num的最左位置，如果不存在返回-1
	// 如果求最长不下降子序列，就在lengthOfLIS中把bs1方法换成bs2方法
	// 已经用对数器验证了，是正确的
	/**
	 * 在不降序数组ends中查找>num的最左位置
	 * 
	 * 算法思路：
	 * 1. 与bs1类似，但查找条件变为>num
	 * 2. 用于计算最长不下降子序列（允许相邻元素相等）
	 * 
	 * 时间复杂度：O(logn) - 标准二分查找
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 是否最优解：是，这是标准的二分查找实现
	 * 
	 * @param ends 不降序数组
	 * @param len 有效长度
	 * @param num 目标值
	 * @return >num的最左位置，如果不存在返回-1
	 */
	public static int bs2(int[] ends, int len, int num) {
		int l = 0, r = len - 1, m, ans = -1;
		while (l <= r) {
			m = (l + r) / 2;
			// 如果ends[m] > num，记录当前位置并继续在左半部分查找
			if (ends[m] > num) {
				ans = m;
				r = m - 1;
			} else {
				// 否则在右半部分查找
				l = m + 1;
			}
		}
		return ans;
	}

}