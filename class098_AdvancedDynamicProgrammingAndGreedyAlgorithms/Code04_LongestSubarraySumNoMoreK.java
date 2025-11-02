package class083;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

// 累加和不大于k的最长子数组 (Longest Subarray Sum No More Than K)
// 给定一个无序数组arr，长度为n，其中元素可能是正、负、0
// 给定一个整数k，求arr所有的子数组中累加和不大于k的最长子数组长度
// 要求时间复杂度为O(n)
// 测试链接 : https://www.nowcoder.com/practice/3473e545d6924077a4f7cbc850408ade
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过
// 
// 相关题目链接:
// LeetCode 560. 和为 K 的子数组: https://leetcode.cn/problems/subarray-sum-equals-k/
// LeetCode 53. 最大子数组和: https://leetcode.cn/problems/maximum-subarray/
// LeetCode 152. 乘积最大子数组: https://leetcode.cn/problems/maximum-product-subarray/
// LeetCode 209. 长度最小的子数组: https://leetcode.cn/problems/minimum-size-subarray-sum/
// LeetCode 862. 和至少为 K 的最短子数组: https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
// HDU 1559. 最大子矩阵: http://acm.hdu.edu.cn/showproblem.php?pid=1559
// 
// 核心算法: 前缀和 + 贪心
// 时间复杂度: O(n)
// 空间复杂度: O(n)
// 工程化考量: 高效IO处理、边界条件处理、空间优化
// 
// 解题思路:
// 方法1: 使用二分查找，时间复杂度O(n log n)
// 方法2: 贪心优化解法，时间复杂度O(n)
// 1. 从右往左计算minSums和minSumEnds数组
// 2. 使用滑动窗口思想从左往右遍历
// 3. 尽可能扩展窗口，如果窗口有效则更新最大长度
public class Code04_LongestSubarraySumNoMoreK {

	// 最大数组长度常量
	public static int MAXN = 100001;

	// 原数组
	public static int[] nums = new int[MAXN];

	// minSums[i]表示从位置i开始向右的最小连续子数组和
	public static int[] minSums = new int[MAXN];

	// minSumEnds[i]表示从位置i开始向右的最小连续子数组和对应的结束位置
	public static int[] minSumEnds = new int[MAXN];

	// 数组长度和目标值
	public static int n, k;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			in.nextToken();
			k = (int) in.nval;
			for (int i = 0; i < n; i++) {
				in.nextToken();
				nums[i] = (int) in.nval;
			}
			out.println(compute2());
		}
		out.flush();
		out.close();
		br.close();
	}

	// 方法1：使用二分查找
	// 时间复杂度: O(n log n)
	// 空间复杂度: O(n)
	public static int compute1() {
		// sums[i]表示前i个元素的最大前缀和
		int[] sums = new int[n + 1];
		// 计算前缀和数组
		for (int i = 0, sum = 0; i < n; i++) {
			// sum : 0...i范围上，这前i+1个数字的累加和
			sum += nums[i];
			// sums[i + 1] : 前i+1个，包括一个数字也没有的时候，所有前缀和中的最大值
			sums[i + 1] = Math.max(sum, sums[i]);
		}
		int ans = 0;
		// 遍历每个位置，计算以该位置结尾的最长子数组
		for (int i = 0, sum = 0, pre, len; i < n; i++) {
			sum += nums[i];
			// 二分查找找到满足条件的最左边位置
			pre = find(sums, sum - k);
			// 计算子数组长度
			len = pre == -1 ? 0 : i - pre + 1;
			// 更新最大长度
			ans = Math.max(ans, len);
		}
		return ans;
	}

	// 二分查找辅助函数：在sums数组中找到>=num的最左边位置
	// sums: 前缀和数组
	// num: 目标值
	// 返回值: 找到的位置，未找到返回-1
	public static int find(int[] sums, int num) {
		int l = 0;
		int r = n;
		int m = 0;
		int ans = -1;
		// 二分查找
		while (l <= r) {
			m = (l + r) / 2;
			if (sums[m] >= num) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}

	// 方法2：贪心优化解法（推荐）
	// 时间复杂度: O(n)
	// 空间复杂度: O(n)
	public static int compute2() {
		// 从右往左计算minSums和minSumEnds数组
		minSums[n - 1] = nums[n - 1];
		minSumEnds[n - 1] = n - 1;
		for (int i = n - 2; i >= 0; i--) {
			// 如果右边的最小和是负数，则加上它
			if (minSums[i + 1] < 0) {
				minSums[i] = nums[i] + minSums[i + 1];
				minSumEnds[i] = minSumEnds[i + 1];
			} else {
				// 否则只取当前位置的值
				minSums[i] = nums[i];
				minSumEnds[i] = i;
			}
		}
		int ans = 0;
		// 使用滑动窗口思想从左往右遍历
		for (int i = 0, sum = 0, end = 0; i < n; i++) {
			// 尽可能扩展窗口
			while (end < n && sum + minSums[end] <= k) {
				sum += minSums[end];
				end = minSumEnds[end] + 1;
			}
			// 如果窗口有效，更新最大长度
			if (end > i) {
				// 如果end > i，
				// 窗口范围：i...end-1，那么窗口有效
				ans = Math.max(ans, end - i);
				// 移除窗口左边的元素
				sum -= nums[i];
			} else {
				// 如果end == i，那么说明窗口根本没扩出来，代表窗口无效
				// end来到i+1位置，然后i++了
				// 继续以新的i位置做开头去扩窗口
				end = i + 1;
			}
		}
		return ans;
	}

}