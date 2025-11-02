package class130;

// 子数组最大变序和
// 给定一个长度为n的数组arr，变序和定义如下
// 数组中每个值都可以减小或者不变，必须把整体变成严格升序的
// 所有方案中，能得到的最大累加和，叫做数组的变序和
// 比如[1,100,7]，变序和14，方案为变成[1,6,7]
// 比如[5,4,9]，变序和16，方案为变成[3,4,9]
// 比如[1,4,2]，变序和3，方案为变成[0,1,2]
// 返回arr所有子数组的变序和中，最大的那个
// 1 <= n、arr[i] <= 10^6
// 来自真实大厂笔试，对数器验证

/**
 * 子数组最大变序和问题 - 单调栈优化解法
 * 
 * 算法思路详解：
 * 1. 问题分析：
 *    - 需要找到所有子数组，将其变为严格递增序列后的最大累加和
 *    - 每个元素可以减小或保持不变，但必须满足严格递增
 *    - 目标是找到所有子数组变序和中的最大值
 *    
 * 2. 优化思路：
 *    - 使用单调栈维护可能的子数组结尾
 *    - 对于每个元素，计算以该元素结尾的子数组的最大变序和
 *    - 利用数学公式快速计算等差数列的和
 *    
 * 3. 时间复杂度分析：
 *    - 时间复杂度：O(n)，每个元素最多入栈和出栈一次
 *    - 空间复杂度：O(n)，栈和dp数组的空间
 *    
 * 4. 为什么是最优解：
 *    - 该解法将暴力O(n²)优化到O(n)
 *    - 利用单调栈和数学公式，是此类问题的最优解法
 */
public class Code07_MaximumOrderSum {

	/**
	 * 暴力方法 - 用于验证正确性
	 * 时间复杂度：O(n * v)，其中v是数组最大值
	 * 空间复杂度：O(n * v)
	 * 
	 * @param arr 输入数组
	 * @return 最大变序和
	 */
	public static long maxSum1(int[] arr) {
		int n = arr.length;
		int max = 0;
		for (int num : arr) {
			max = Math.max(max, num);
		}
		long ans = 0;
		long[][] dp = new long[n][max + 1];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j <= max; j++) {
				dp[i][j] = -1;
			}
		}
		for (int i = 0; i < n; i++) {
			ans = Math.max(ans, f1(arr, i, arr[i], dp));
		}
		return ans;
	}

	/**
	 * 递归辅助函数 - 计算以位置i结尾的子数组的最大变序和
	 * 
	 * @param arr 输入数组
	 * @param i 当前位置
	 * @param p 当前允许的最大值
	 * @param dp 记忆化数组
	 * @return 最大变序和
	 */
	public static long f1(int[] arr, int i, int p, long[][] dp) {
		if (p <= 0 || i == -1) {
			return 0;
		}
		if (dp[i][p] != -1) {
			return dp[i][p];
		}
		int cur = Math.min(arr[i], p);
		long next = f1(arr, i - 1, cur - 1, dp);
		long ans = (long) cur + next;
		dp[i][p] = ans;
		return cur + next;
	}

	/**
	 * 正式方法 - 单调栈优化
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 * 
	 * @param arr 输入数组
	 * @return 最大变序和
	 */
	public static long maxSum2(int[] arr) {
		int n = arr.length;
		int[] stack = new int[n];  // 单调栈，存储下标
		int size = 0;              // 栈大小
		long[] dp = new long[n];   // dp[i]表示以i结尾的子数组的最大变序和
		long ans = 0;
		
		for (int i = 0; i < n; i++) {
			int curIdx = i;
			int curVal = arr[curIdx];
			
			// 维护单调栈，处理栈顶元素
			while (curVal > 0 && size > 0) {
				int topIdx = stack[size - 1];
				int topVal = arr[topIdx];
				
				if (topVal >= curVal) {
					// 栈顶元素更大，直接弹出
					size--;
				} else {
					int idxDiff = curIdx - topIdx;  // 位置差
					int valDiff = curVal - topVal;  // 数值差
					
					if (valDiff >= idxDiff) {
						// 可以完全覆盖区间
						dp[i] += sum(curVal, idxDiff) + dp[topIdx];
						curVal = 0;
						curIdx = 0;
						break;
					} else {
						// 部分覆盖
						dp[i] += sum(curVal, idxDiff);
						curVal -= idxDiff;
						curIdx = topIdx;
						size--;
					}
				}
			}
			
			// 处理剩余部分
			if (curVal > 0) {
				dp[i] += sum(curVal, curIdx + 1);
			}
			
			// 当前元素入栈
			stack[size++] = i;
			ans = Math.max(ans, dp[i]);
		}
		return ans;
	}

	/**
	 * 计算等差数列的和
	 * 从max开始，递减1，共n项的正数部分的和
	 * 公式：sum = (首项 + 末项) * 项数 / 2
	 * 
	 * @param max 最大值
	 * @param n 项数
	 * @return 等差数列的和
	 */
	public static long sum(int max, int n) {
		n = Math.min(max, n);  // 确保不超过max
		return (((long) max * 2 - n + 1) * n) / 2;
	}

	// 为了验证
	public static int[] randomArray(int n, int v) {
		int[] ans = new int[n];
		for (int i = 0; i < n; i++) {
			ans[i] = (int) (Math.random() * v);
		}
		return ans;
	}

	// 为了验证
	public static void main(String[] args) {
		int n = 100;
		int v = 100;
		int testTimes = 50000;
		System.out.println("功能测试开始");
		for (int i = 0; i < testTimes; i++) {
			int size = (int) (Math.random() * n) + 1;
			int[] arr = randomArray(size, v);
			long ans1 = maxSum1(arr);
			long ans2 = maxSum2(arr);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("功能测试结束");

		System.out.println("性能测试开始");
		n = 1000000;
		v = 1000000;
		System.out.println("数组长度 : " + n);
		System.out.println("数值范围 : " + v);
		int[] arr = randomArray(n, v);
		long start = System.currentTimeMillis();
		maxSum2(arr);
		long end = System.currentTimeMillis();
		System.out.println("运行时间 : " + (end - start) + " 毫秒");
		System.out.println("性能测试结束");
	}

}
