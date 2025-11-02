package class131;

/**
 * LeetCode 327. 区间和的个数 (Count of Range Sum)
 * 题目链接: https://leetcode.cn/problems/count-of-range-sum/
 * 
 * 题目描述:
 * 给定一个整数数组 nums 以及两个整数 lower 和 upper，
 * 求出数组中所有子数组的和在 [lower, upper] 范围内的个数。
 * 
 * 解题思路:
 * 使用归并排序的思想解决区间和计数问题。
 * 1. 首先计算前缀和数组，将问题转化为：对于每个前缀和sum[i]，
 *    统计在它之前的前缀和sum[j] (j < i) 中，有多少个满足
 *    lower <= sum[i] - sum[j] <= upper，即 sum[i] - upper <= sum[j] <= sum[i] - lower
 * 2. 利用归并排序的分治思想，在合并过程中统计满足条件的区间和个数
 * 3. 在合并两个有序数组时，使用滑动窗口技术统计满足条件的元素对
 * 
 * 时间复杂度分析:
 * - 计算前缀和: O(n)
 * - 归并排序: O(n log n)
 * - 总时间复杂度: O(n log n)
 * 空间复杂度: O(n) 用于存储前缀和数组和辅助数组
 */
public class Code01_CountOfRangeSum1 {

	/**
	 * 计算数组中区间和在指定范围内的子数组个数
	 * 
	 * @param nums   输入数组
	 * @param lower  区间下界
	 * @param upper  区间上界
	 * @return       满足条件的子数组个数
	 * @throws IllegalArgumentException 如果输入参数不合法
	 */
	public static int countRangeSum(int[] nums, int lower, int upper) {
		// 输入验证
		if (nums == null) {
			throw new IllegalArgumentException("输入数组不能为null");
		}
		if (nums.length == 0) {
			return 0; // 空数组直接返回0
		}
		if (lower > upper) {
			throw new IllegalArgumentException("下界不能大于上界: lower=" + lower + ", upper=" + upper);
		}
		
		int n = nums.length;
		// 边界检查：处理整数溢出
		if (n > MAXN) {
			throw new IllegalArgumentException("数组长度超过最大限制: " + n + " > " + MAXN);
		}
		
		// 计算前缀和数组
		sum[0] = nums[0];
		for (int i = 1; i < n; i++) {
			sum[i] = sum[i - 1] + nums[i];
		}
		low = lower;
		up = upper;
		// 使用归并排序分治求解
		return f(0, n - 1);
	}

	// 最大数组长度常量
	public static int MAXN = 100001;

	// 前缀和数组，使用long类型防止溢出
	public static long[] sum = new long[MAXN];

	// 归并排序辅助数组
	public static long[] help = new long[MAXN];

	// 全局变量存储区间边界
	public static int low, up;

	/**
	 * 归并排序分治函数
	 * 
	 * @param l 左边界
	 * @param r 右边界
	 * @return  区间[l,r]内满足条件的子数组个数
	 */
	public static int f(int l, int r) {
		// 递归终止条件：只有一个元素
		if (l == r) {
			// 判断单个元素（即前缀和）是否在指定范围内
			return low <= sum[l] && sum[l] <= up ? 1 : 0;
		}
		// 分治：计算中点
		int m = (l + r) / 2;
		// 递归计算左半部分、右半部分和跨越中点的区间个数
		return f(l, m) + f(m + 1, r) + merge(l, m, r);
	}

	/**
	 * 归并过程，同时统计满足条件的区间个数
	 * 
	 * @param l 左边界
	 * @param m 中点
	 * @param r 右边界
	 * @return  跨越中点的满足条件的区间个数
	 */
	public static int merge(int l, int m, int r) {
		// 归并分治的统计过程
		int ans = 0;
		// 滑动窗口的左右指针
		int wl = l, wr = l;
		long max, min;
		
		// 遍历右半部分的每个元素
		for (int i = m + 1; i <= r; i++) {
			// 计算满足条件的左半部分元素范围
			// sum[i] - sum[j] >= lower  =>  sum[j] <= sum[i] - lower
			// sum[i] - sum[j] <= upper  =>  sum[j] >= sum[i] - upper
			max = sum[i] - low;  // 上界
			min = sum[i] - up;   // 下界
			
			// 调整滑动窗口右边界
			// 找到所有 <= max 的元素
			while (wr <= m && sum[wr] <= max) {
				wr++;
			}
			
			// 调整滑动窗口左边界
			// 找到第一个 >= min 的元素
			while (wl <= m && sum[wl] < min) {
				wl++;
			}
			
			// 统计满足条件的元素个数
			ans += wr - wl;
		}
		
		// 正常排序的合并过程
		int p1 = l;
		int p2 = m + 1;
		int i = l;
		
		// 合并两个有序数组
		while (p1 <= m && p2 <= r) {
			help[i++] = sum[p1] <= sum[p2] ? sum[p1++] : sum[p2++];
		}
		while (p1 <= m) {
			help[i++] = sum[p1++];
		}
		while (p2 <= r) {
			help[i++] = sum[p2++];
		}
		
		// 将合并结果复制回原数组
		for (i = l; i <= r; i++) {
			sum[i] = help[i];
		}
		
		return ans;
	}

}
