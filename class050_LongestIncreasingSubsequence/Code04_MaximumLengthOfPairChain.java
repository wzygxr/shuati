package class072;

import java.util.Arrays;

// 最长数对链
// 给你一个由n个数对组成的数对数组pairs
// 其中 pairs[i] = [lefti, righti] 且 lefti < righti
// 现在，我们定义一种 跟随 关系，当且仅当 b < c 时
// 数对 p2 = [c, d] 才可以跟在 p1 = [a, b] 后面
// 我们用这种形式来构造 数对链
// 找出并返回能够形成的最长数对链的长度
// 测试链接 : https://leetcode.cn/problems/maximum-length-of-pair-chain/
public class Code04_MaximumLengthOfPairChain {

	/**
	 * 使用LIS方法计算最长数对链的长度
	 * 
	 * 算法思路：
	 * 1. 先按开始位置排序
	 * 2. 转化为最长递增子序列问题，维护ends数组表示长度为i的数对链的最小结束位置
	 * 3. 遍历数对，使用二分查找维护ends数组
	 * 
	 * 时间复杂度：O(n*logn) - 排序O(n*logn) + 遍历并二分查找O(n*logn)
	 * 空间复杂度：O(n) - 需要ends数组存储状态
	 * 是否最优解：否，存在更优的贪心解法
	 * 
	 * @param pairs 数对数组
	 * @return 最长数对链的长度
	 */
	public static int findLongestChain(int[][] pairs) {
		int n = pairs.length;
		// 数对根据开始位置排序，从小到大
		// 结束位置无所谓！
		Arrays.sort(pairs, (a, b) -> a[0] - b[0]);
		// ends[i]表示长度为i+1的数对链的最小结束位置
		int[] ends = new int[n];
		int len = 0;
		// 遍历所有数对
		for (int[] pair : pairs) {
			// 二分查找>=pair[0]的最左位置
			int find = bs(ends, len, pair[0]);
			// 如果找不到，说明可以延长数对链
			if (find == -1) {
				ends[len++] = pair[1];
			} else {
				// 如果找到了，更新该位置的最小结束位置
				ends[find] = Math.min(ends[find], pair[1]);
			}
		}
		return len;
	}

	// >= num最左位置
	/**
	 * 在升序数组ends中查找>=num的最左位置
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
	 * @param ends 升序数组
	 * @param len 有效长度
	 * @param num 目标值
	 * @return >=num的最左位置，如果不存在返回-1
	 */
	public static int bs(int[] ends, int len, int num) {
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

	// 最优解利用贪心
	/**
	 * 使用贪心算法计算最长数对链的长度
	 * 
	 * 算法思路：
	 * 1. 按结束位置排序，优先选择结束位置小的数对
	 * 2. 贪心选择：每次选择能接在当前数对链后面的、结束位置最小的数对
	 * 3. 这样可以为后续选择留下更多空间
	 * 
	 * 时间复杂度：O(n*logn) - 排序O(n*logn)
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 是否最优解：是，这是最优解法
	 * 
	 * @param pairs 数对数组
	 * @return 最长数对链的长度
	 */
	public static int findLongestChain2(int[][] pairs) {
		// pre表示当前数对链的结束位置
		int pre = Integer.MIN_VALUE, ans = 0;
		// 按结束位置排序
		Arrays.sort(pairs, (a, b) -> a[1] - b[1]);
		// 贪心选择数对
		for (int[] pair : pairs) {
			// 如果当前数对的开始位置大于前一个数对的结束位置，可以接在后面
			if (pre < pair[0]) {
				pre = pair[1];  // 更新数对链的结束位置
				ans++;          // 数对链长度加1
			}
		}
		return ans;
	}

}