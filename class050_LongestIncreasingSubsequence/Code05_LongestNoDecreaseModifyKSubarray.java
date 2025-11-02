package class072;

// 有一次修改机会的最长不下降子序列
// 给定一个长度为n的数组arr，和一个整数k
// 只有一次机会可以将其中连续的k个数全修改成任意一个值
// 这次机会你可以用也可以不用，请返回最长不下降子序列长度
// 1 <= k, n <= 10^5
// 1 <= arr[i] <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P8776
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的所有代码，并把主类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code05_LongestNoDecreaseModifyKSubarray {

	public static int MAXN = 100001;

	public static int[] arr = new int[MAXN];

	public static int[] right = new int[MAXN];

	public static int[] ends = new int[MAXN];

	public static int n, k;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			in.nextToken();
			k = (int) (in.nval);
			for (int i = 0; i < n; i++) {
				in.nextToken();
				arr[i] = (int) in.nval;
			}
			if (k >= n) {
				out.println(n);
			} else {
				out.println(compute());
			}
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算有一次修改机会的最长不下降子序列长度
	 * 
	 * 算法思路：
	 * 1. 预处理right数组，right[i]表示以arr[i]开头的arr[i...]上的最长不下降子序列长度
	 * 2. 枚举修改的区间[i, i+k-1]，计算修改后的最长不下降子序列长度
	 * 3. 修改后的最长不下降子序列可以分为三部分：
	 *    - 修改区间前的最长不下降子序列长度left
	 *    - 修改区间长度k
	 *    - 修改区间后的最长不下降子序列长度right[i+k]
	 * 4. 特殊情况：不修改任何区间的最长不下降子序列长度
	 * 
	 * 时间复杂度：O(n*logn) - 预处理right数组O(n*logn) + 枚举区间O(n*logn)
	 * 空间复杂度：O(n) - 需要辅助数组存储状态
	 * 是否最优解：是，这是目前最优解法
	 * 
	 * @return 最长不下降子序列长度
	 */
	public static int compute() {
		right();
		int len = 0;
		int ans = 0;
		// 枚举修改的区间[i, i+k-1]
		for (int i = 0, j = k, find, left; j < n; i++, j++) {
			// 计算修改区间前的最长不下降子序列长度
			find = bs2(len, arr[j]);
			left = find == -1 ? len : find;
			// 更新答案：left + k + right[j]
			ans = Math.max(ans, left + k + right[j]);
			// 更新修改区间前的部分
			find = bs2(len, arr[i]);
			if (find == -1) {
				ends[len++] = arr[i];
			} else {
				ends[find] = arr[i];
			}
		}
		// 特殊情况：不修改任何区间的最长不下降子序列长度
		ans = Math.max(ans, len + k);
		return ans;
	}

	// 生成辅助数组right
	// right[j] :
	// 一定以arr[j]做开头的情况下，arr[j...]上最长不下降子序列长度是多少
	// 关键逻辑 :
	// 一定以arr[i]做开头的情况下，arr[i...]上最长不下降子序列
	// 就是！从n-1出发来看(从右往左遍历)，以arr[i]做结尾的情况下的最长不上升子序列
	/**
	 * 预处理辅助数组right
	 * 
	 * 算法思路：
	 * 1. 从右往左遍历数组
	 * 2. 对于每个位置i，计算以arr[i]开头的arr[i...]上的最长不下降子序列长度
	 * 3. 转化为计算以arr[i]结尾的arr[...i]上的最长不上升子序列长度
	 * 4. 使用贪心+二分查找维护最长不上升子序列
	 * 
	 * 时间复杂度：O(n*logn) - 遍历n个元素，每次二分查找O(logn)
	 * 空间复杂度：O(n) - 需要辅助数组存储状态
	 * 是否最优解：是，这是目前最优解法
	 */
	public static void right() {
		int len = 0;
		// 从右往左遍历数组
		for (int i = n - 1, find; i >= 0; i--) {
			// 计算以arr[i]结尾的最长不上升子序列长度
			find = bs1(len, arr[i]);
			if (find == -1) {
				ends[len++] = arr[i];
				right[i] = len;
			} else {
				ends[find] = arr[i];
				right[i] = find + 1;
			}
		}
	}

	// 求最长不上升子序列长度的二分
	// ends[0...len-1]是降序的，找到<num的最左位置
	// 不存在返回-1
	/**
	 * 在严格降序数组ends中查找<num的最左位置
	 * 
	 * 算法思路：
	 * 1. 使用二分查找在有序数组中查找目标值
	 * 2. 维护左边界l和右边界r
	 * 3. 计算中间位置m，比较ends[m]与num的大小关系
	 * 4. 如果ends[m] < num，说明目标位置在左半部分（包括m），更新ans和r
	 * 5. 否则目标位置在右半部分，更新l
	 * 
	 * 时间复杂度：O(logn) - 标准二分查找
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 是否最优解：是，这是标准的二分查找实现
	 * 
	 * @param len 有效长度
	 * @param num 目标值
	 * @return <num的最左位置，如果不存在返回-1
	 */
	public static int bs1(int len, int num) {
		int l = 0, r = len - 1, m, ans = -1;
		while (l <= r) {
			m = (l + r) / 2;
			// 如果ends[m] < num，记录当前位置并继续在左半部分查找
			if (ends[m] < num) {
				ans = m;
				r = m - 1;
			} else {
				// 否则在右半部分查找
				l = m + 1;
			}
		}
		return ans;
	}

	// 求最长不下降子序列长度的二分
	// ends[0...len-1]是升序的，找到>num的最左位置
	// 不存在返回-1
	/**
	 * 在不降序数组ends中查找>num的最左位置
	 * 
	 * 算法思路：
	 * 1. 使用二分查找在有序数组中查找目标值
	 * 2. 维护左边界l和右边界r
	 * 3. 计算中间位置m，比较ends[m]与num的大小关系
	 * 4. 如果ends[m] > num，说明目标位置在左半部分（包括m），更新ans和r
	 * 5. 否则目标位置在右半部分，更新l
	 * 
	 * 时间复杂度：O(logn) - 标准二分查找
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 是否最优解：是，这是标准的二分查找实现
	 * 
	 * @param len 有效长度
	 * @param num 目标值
	 * @return >num的最左位置，如果不存在返回-1
	 */
	public static int bs2(int len, int num) {
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