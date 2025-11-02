package class131;

import java.util.Arrays;

/**
 * LeetCode 327. 区间和的个数 (Count of Range Sum) - 树状数组解法
 * 题目链接: https://leetcode.cn/problems/count-of-range-sum/
 * 
 * 题目描述:
 * 给定一个整数数组 nums 以及两个整数 lower 和 upper，
 * 求出数组中所有子数组的和在 [lower, upper] 范围内的个数。
 * 
 * 解题思路:
 * 使用树状数组 + 离散化的方法解决区间和计数问题。
 * 1. 首先计算前缀和数组，将问题转化为：对于每个前缀和sum[i]，
 *    统计在它之前的前缀和sum[j] (j < i) 中，有多少个满足
 *    lower <= sum[i] - sum[j] <= upper，即 sum[i] - upper <= sum[j] <= sum[i] - lower
 * 2. 对前缀和数组进行离散化处理，以减少空间复杂度
 * 3. 使用树状数组维护已处理的前缀和，支持快速查询和更新
 * 4. 遍历前缀和数组，在树状数组中查询满足条件的前缀和个数
 * 
 * 时间复杂度分析:
 * - 计算前缀和: O(n)
 * - 离散化: O(n log n)
 * - 遍历查询: O(n log n)
 * - 总时间复杂度: O(n log n)
 * 空间复杂度: O(n) 用于存储前缀和数组和树状数组
 */
public class Code01_CountOfRangeSum2 {

	/**
	 * 计算数组中区间和在指定范围内的子数组个数（树状数组解法）
	 * 
	 * @param nums   输入数组
	 * @param lower  区间下界
	 * @param upper  区间上界
	 * @return       满足条件的子数组个数
	 */
	// 树状数组 + 离散化的解法，理解难度较低
	public static int countRangeSum(int[] nums, int lower, int upper) {
		// 构建离散化前缀和数组
		build(nums);
		long sum = 0;  // 当前前缀和
		int ans = 0;   // 结果计数
		
		// 遍历原数组，逐个处理元素
		for (int i = 0; i < n; i++) {
			sum += nums[i];  // 更新前缀和
			
			// 查询满足条件的前缀和个数
			// sum[i] - sum[j] >= lower  =>  sum[j] <= sum[i] - lower
			// sum[i] - sum[j] <= upper  =>  sum[j] >= sum[i] - upper
			// 所以需要统计 [sum[i]-upper, sum[i]-lower] 范围内的前缀和个数
			ans += sum(rank(sum - lower)) - sum(rank(sum - upper - 1));
			
			// 特殊情况：当前前缀和本身是否满足条件
			if (lower <= sum && sum <= upper) {
				ans++;
			}
			
			// 将当前前缀和加入树状数组
			add(rank(sum), 1);
		}
		return ans;
	}

	// 最大数组长度常量
	public static int MAXN = 100002;

	// 全局变量
	public static int n, m;  // n:数组长度, m:去重后前缀和个数

	// 离散化数组，存储排序后的前缀和
	public static long[] sort = new long[MAXN];

	// 树状数组，用于维护前缀和的出现次数
	public static int[] tree = new int[MAXN];

	/**
	 * 构建离散化前缀和数组
	 * 
	 * @param nums 原始数组
	 */
	public static void build(int[] nums) {
		// 生成前缀和数组
		n = nums.length;
		for (int i = 1, j = 0; i <= n; i++, j++) {
			sort[i] = sort[i - 1] + nums[j];
		}
		
		// 前缀和数组排序和去重，最终有m个不同的前缀和
		Arrays.sort(sort, 1, n + 1);
		m = 1;
		for (int i = 2; i <= n; i++) {
			if (sort[m] != sort[i]) {
				sort[++m] = sort[i];
			}
		}
		
		// 初始化树状数组，下标1~m
		Arrays.fill(tree, 1, m + 1, 0);
	}

	/**
	 * 二分查找，返回 <=v 并且尽量大的前缀和是第几号前缀和
	 * 
	 * @param v 目标值
	 * @return  离散化后的排名
	 */
	public static int rank(long v) {
		int left = 1, right = m, mid;
		int ans = 0;
		while (left <= right) {
			mid = (left + right) / 2;
			if (sort[mid] <= v) {
				ans = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return ans;
	}

	/**
	 * 树状数组更新操作
	 * 在i号位置增加c个元素
	 * 
	 * @param i 位置索引
	 * @param c 增加的数量
	 */
	// 树状数组模版代码，没有任何修改
	// i号前缀和，个数增加c个
	public static void add(int i, int c) {
		while (i <= m) {
			tree[i] += c;
			i += i & -i;  // 更新父节点
		}
	}

	/**
	 * 树状数组查询操作
	 * 查询1~i号位置的元素总个数
	 * 
	 * @param i 查询位置
	 * @return  前缀和个数
	 */
	// 树状数组模版代码，没有任何修改
	// 查询1~i号前缀和一共有几个
	public static int sum(int i) {
		int ans = 0;
		while (i > 0) {
			ans += tree[i];
			i -= i & -i;  // 移动到父节点
		}
		return ans;
	}

}
