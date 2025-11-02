package class127;

// 相邻与结果不为0的最长子序列
// 给定一个长度为n的数组arr，你可以随意选择数字组成子序列
// 但是要求任意相邻的两个数&的结果不能是0，这样的子序列才是合法的
// 返回最长合法子序列的长度
// 1 <= n <= 10^5
// 0 <= arr[i] <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P4310
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

/**
 * 算法思路：
 * 1. 这是一个动态规划问题
 * 2. 对于每个数，我们关注它的二进制表示中为1的位
 * 3. dp[i]表示以第i位为结尾的最长子序列长度
 * 4. 对于当前数num，我们找出它二进制表示中为1的位j
 * 5. 找到所有以位j结尾的最长子序列长度，取最大值+1作为新的长度
 * 6. 更新所有num中为1的位j对应的dp[j]
 *
 * 时间复杂度：O(n * 31) = O(n)，其中n是数组长度
 * 空间复杂度：O(1)，只使用了固定大小的数组
 */
public class Code04_LongestAddNotZero {

	public static int MAXN = 100001;

	public static int[] arr = new int[MAXN];

	public static int[] pre = new int[32];

	public static int n;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		for (int i = 0; i < n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算最长合法子序列的长度
	 * @return 最长合法子序列的长度
	 */
	public static int compute() {
		// 初始化pre数组
		Arrays.fill(pre, 0);
		// 遍历每个数
		for (int i = 0, num, cur; i < n; i++) {
			num = arr[i];
			cur = 1;
			// 找到当前数之前，以num中任意为1的位结尾的最长子序列长度的最大值
			for (int j = 0; j < 31; j++) {
				// 如果num的第j位为1
				if (((num >> j) & 1) == 1) {
					// 更新cur为以第j位结尾的最长子序列长度+1的最大值
					cur = Math.max(cur, pre[j] + 1);
				}
			}
			// 更新pre数组
			for (int j = 0; j < 31; j++) {
				// 如果num的第j位为1
				if (((num >> j) & 1) == 1) {
					// 更新以第j位结尾的最长子序列长度
					pre[j] = Math.max(pre[j], cur);
				}
			}
		}
		// 找到所有位结尾的最长子序列长度的最大值
		int ans = 0;
		for (int j = 0; j < 31; j++) {
			ans = Math.max(ans, pre[j]);
		}
		return ans;
	}

	// 相关题目：
	// 1. LeetCode 152 - Maximum Product Subarray (乘积最大子数组)
	//    链接：https://leetcode.cn/problems/maximum-product-subarray/
	//    区别：求连续子数组的最大乘积
	// 2. LeetCode 53 - Maximum Subarray (最大子数组和)
	//    链接：https://leetcode.cn/problems/maximum-subarray/
	//    区别：求连续子数组的最大和
	// 3. LeetCode 300 - Longest Increasing Subsequence (最长递增子序列)
	//    链接：https://leetcode.cn/problems/longest-increasing-subsequence/
	//    区别：求最长递增子序列
	// 4. LeetCode 128 - Longest Consecutive Sequence (最长连续序列)
	//    链接：https://leetcode.cn/problems/longest-consecutive-sequence/
	//    区别：求最长连续数字序列
}