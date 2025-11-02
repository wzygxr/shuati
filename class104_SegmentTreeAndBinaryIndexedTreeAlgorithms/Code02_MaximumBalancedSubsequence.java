package class131;

import java.util.Arrays;

/**
 * LeetCode 2784. 平衡子序列的最大和 (Maximum Balanced Subsequence Sum)
 * 题目链接: https://leetcode.cn/problems/maximum-balanced-subsequence-sum/
 * 
 * 题目描述:
 * 给定一个长度为n的数组nums，定义平衡子序列为满足以下条件的子序列：
 * 对于子序列中任意两个下标i和j（i在j的左边），必须满足nums[j] - nums[i] >= j - i
 * 求所有平衡子序列中元素和的最大值。
 * 
 * 解题思路:
 * 使用树状数组优化动态规划的方法解决此问题。
 * 1. 首先将约束条件nums[j] - nums[i] >= j - i变形为nums[j] - j >= nums[i] - i
 *    这样我们定义一个新的指标值：nums[i] - i
 * 2. 对于每个元素nums[i]，我们计算其指标值nums[i] - i
 * 3. 使用树状数组维护以指标值为维度的动态规划状态
 *    dp[k]表示以指标值不超过sort[k]的元素结尾的平衡子序列的最大和
 * 4. 遍历数组，对于每个元素，查询之前指标值不超过当前指标值的最大dp值
 *    然后更新当前指标值对应的dp值
 * 
 * 时间复杂度分析:
 * - 离散化: O(n log n)
 * - 遍历更新: O(n log n)
 * - 总时间复杂度: O(n log n)
 * 空间复杂度: O(n) 用于存储离散化数组和树状数组
 */
public class Code02_MaximumBalancedSubsequence {

	/**
	 * 计算平衡子序列的最大和
	 * 
	 * @param nums 输入数组
	 * @return     平衡子序列的最大和
	 */
	public static long maxBalancedSubsequenceSum(int[] nums) {
		// 构建离散化数组
		build(nums);
		long pre;  // 之前的最优解
		
		// 遍历数组中的每个元素
		for (int i = 0, k; i < n; i++) {
			// k的含义为当前的指标(nums[i]-i)是第几号指标
			k = rank(nums[i] - i);
			
			// 查询dp[1号..k号指标]中的最大值
			// 即查询之前指标值不超过当前指标值的平衡子序列最大和
			pre = max(k);
			
			if (pre < 0) {
				// 如果之前的最好情况是负数，那么不要之前的数了
				// 当前数字自己单独形成平衡子序列
				// 去更新dp[k号指标]，看能不能变得更大
				update(k, nums[i]);
			} else {
				// 如果之前的最好情况不是负数，那么和当前数字一起形成更大的累加和
				// 去更新dp[k号指标]，看能不能变得更大
				update(k, pre + nums[i]);
			}
		}
		
		// 返回dp[1号..m号指标]中的最大值
		// 即所有可能的平衡子序列的最大和
		return max(m);
	}

	// 最大数组长度常量
	public static int MAXN = 100001;

	// 离散化数组，存储排序后的指标值(nums[i]-i)
	public static int[] sort = new int[MAXN];

	// 树状数组，tree[i]表示以指标值不超过sort[i]的元素结尾的平衡子序列的最大和
	public static long[] tree = new long[MAXN];

	// 全局变量
	public static int n, m;  // n:数组长度, m:去重后指标值个数

	/**
	 * 构建离散化数组
	 * 
	 * @param nums 原始数组
	 */
	public static void build(int[] nums) {
		n = nums.length;
		// 计算每个元素的指标值nums[i]-i
		for (int i = 1, j = 0; i <= n; i++, j++) {
			sort[i] = nums[j] - j;
		}
		
		// 对指标值数组进行排序和去重
		Arrays.sort(sort, 1, n + 1);
		m = 1;
		for (int i = 2; i <= n; i++) {
			if (sort[m] != sort[i]) {
				sort[++m] = sort[i];
			}
		}
		
		// 初始化树状数组，初始值设为Long.MIN_VALUE表示不可达
		Arrays.fill(tree, 1, m + 1, Long.MIN_VALUE);
	}

	/**
	 * 二分查找，返回指标值v是第几号指标
	 * 
	 * @param v 指标值
	 * @return  离散化后的排名
	 */
	// 当前的指标值是v，返回这是第几号指标
	public static int rank(int v) {
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
	 * 更新dp[i号指标]的值为v（取最大值）
	 * 
	 * @param i 位置索引
	 * @param v 新的值
	 */
	// dp[i号指标]，当前算出的值是v
	public static void update(int i, long v) {
		while (i <= m) {
			// 只有当新值更大时才更新
			tree[i] = Math.max(tree[i], v);
			i += i & -i;  // 更新父节点
		}
	}

	/**
	 * 树状数组查询操作
	 * 查询dp[1..i]中的最大值
	 * 
	 * @param i 查询位置
	 * @return  前缀最大值
	 */
	// dp[1..i]，最大值多少返回
	public static long max(int i) {
		long ans = Long.MIN_VALUE;
		while (i > 0) {
			// 在所有祖先节点中找最大值
			ans = Math.max(ans, tree[i]);
			i -= i & -i;  // 移动到父节点
		}
		return ans;
	}

}
