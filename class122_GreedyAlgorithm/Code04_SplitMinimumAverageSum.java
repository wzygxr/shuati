package class091;

import java.util.Arrays;

// 平均值最小累加和
// 给定一个数组arr，长度为n
// 再给定一个数字k，表示一定要将arr划分成k个集合
// 每个数字只能进一个集合
// 返回每个集合的平均值都累加起来的最小值
// 平均值向下取整
// 1 <= n <= 10^5
// 0 <= arr[i] <= 10^5
// 1 <= k <= n
// 来自真实大厂笔试，没有在线测试，对数器验证
public class Code04_SplitMinimumAverageSum {

	// 暴力方法
	// 为了验证
	// 每个数字做出所有可能的选择
	// 时间复杂度O(k的n次方)
	public static int minAverageSum1(int[] arr, int k) {
		int[] sum = new int[k];
		int[] cnt = new int[k];
		return f(arr, 0, sum, cnt);
	}

	// 暴力方法
	// 为了验证
	public static int f(int[] arr, int i, int[] sum, int[] cnt) {
		if (i == arr.length) {
			int ans = 0;
			for (int j = 0; j < sum.length; j++) {
				if (cnt[j] == 0) {
					return Integer.MAX_VALUE;
				}
				ans += sum[j] / cnt[j];
			}
			return ans;
		} else {
			int ans = Integer.MAX_VALUE;
			for (int j = 0; j < sum.length; j++) {
				sum[j] += arr[i];
				cnt[j]++;
				ans = Math.min(ans, f(arr, i + 1, sum, cnt));
				sum[j] -= arr[i];
				cnt[j]--;
			}
			return ans;
		}
	}

	/**
	 * 计算划分数组后的最小平均值累加和
	 * 
	 * 算法思路：
	 * 贪心策略：
	 * 1. 将数组排序
	 * 2. 前k-1个最小元素各自成一组（因为单个元素的平均值就是元素值本身）
	 * 3. 剩余元素组成最后一组
	 * 
	 * 正确性证明：
	 * 1. 平均值的计算是向下取整，所以元素越少的组，单个元素对平均值的影响越大
	 * 2. 为了最小化总和，应该让较小的元素尽可能独立成组
	 * 3. 由于必须分成k组，所以前k-1个最小元素各自成组是最优策略
	 * 
	 * 时间复杂度：O(n * logn) - 主要是排序的时间复杂度
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 
	 * @param arr 输入数组
	 * @param k 划分的组数
	 * @return 最小平均值累加和
	 */
	public static int minAverageSum2(int[] arr, int k) {
		Arrays.sort(arr);
		int ans = 0;
		for (int i = 0; i < k - 1; i++) {
			// 最小的k-1个数，每个数独自成一个集合
			ans += arr[i];
		}
		int sum = 0;
		for (int i = k - 1; i < arr.length; i++) {
			sum += arr[i];
		}
		ans += sum / (arr.length - k + 1);
		return ans;
	}

	// 为了测试
	public static int[] randomArray(int n, int v) {
		int[] ans = new int[n];
		for (int i = 0; i < n; i++) {
			ans[i] = (int) (Math.random() * v);
		}
		return ans;
	}

	// 为了测试
	public static void main(String[] args) {
		int N = 8;
		int V = 10000;
		int testTimes = 2000;
		System.out.println("测试开始");
		for (int i = 1; i <= testTimes; i++) {
			int n = (int) (Math.random() * N) + 1;
			int[] arr = randomArray(n, V);
			int k = (int) (Math.random() * n) + 1;
			int ans1 = minAverageSum1(arr, k);
			int ans2 = minAverageSum2(arr, k);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
			if (i % 100 == 0) {
				System.out.println("测试到第" + i + "组");
			}
		}
		System.out.println("测试结束");
		
		// 额外测试用例
		int[] testArr = {1, 2, 3, 4, 5};
		int testK = 3;
		System.out.println("\n额外测试用例:");
		System.out.println("数组: " + Arrays.toString(testArr));
		System.out.println("划分组数: " + testK);
		System.out.println("最小平均值累加和: " + minAverageSum2(testArr, testK));
	}
}