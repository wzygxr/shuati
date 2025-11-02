package class072;

import java.util.Arrays;

// 俄罗斯套娃信封问题
// 给你一个二维整数数组envelopes ，其中envelopes[i]=[wi, hi]
// 表示第 i 个信封的宽度和高度
// 当另一个信封的宽度和高度都比这个信封大的时候
// 这个信封就可以放进另一个信封里，如同俄罗斯套娃一样
// 请计算 最多能有多少个信封能组成一组"俄罗斯套娃"信封
// 即可以把一个信封放到另一个信封里面，注意不允许旋转信封
// 测试链接 : https://leetcode.cn/problems/russian-doll-envelopes/
public class Code02_RussianDollEnvelopes {

	/**
	 * 计算最多能有多少个信封能组成一组"俄罗斯套娃"信封
	 * 
	 * 算法思路：
	 * 1. 这是一个二维最长递增子序列问题
	 * 2. 先按宽度升序排序，宽度相同时按高度降序排序
	 *    - 宽度升序确保后面的信封宽度一定>=前面的信封
	 *    - 高度降序确保宽度相同的信封不会被同时选中（避免违反套娃规则）
	 * 3. 对高度数组求最长严格递增子序列长度
	 * 
	 * 时间复杂度：O(n*logn) - 排序O(n*logn) + LIS O(n*logn)
	 * 空间复杂度：O(n) - 需要ends数组存储状态
	 * 是否最优解：是，这是目前最优解法
	 * 
	 * @param envelopes 信封数组，envelopes[i] = [wi, hi]
	 * @return 最多能组成的俄罗斯套娃信封数量
	 */
	public static int maxEnvelopes(int[][] envelopes) {
		int n = envelopes.length;
		// 排序策略:
		// 宽度从小到大
		// 宽度一样，高度从大到小
		Arrays.sort(envelopes, (a, b) -> a[0] != b[0] ? (a[0] - b[0]) : (b[1] - a[1]));
		int[] ends = new int[n];
		int len = 0;
		// 遍历排序后的信封高度，求最长递增子序列
		for (int i = 0, find, num; i < n; i++) {
			num = envelopes[i][1];
			// 二分查找>=num的最左位置
			find = bs(ends, len, num);
			// 如果找不到，说明num比所有元素都大，可以延长递增子序列
			if (find == -1) {
				ends[len++] = num;
			} else {
				// 如果找到了位置，更新该位置的值为num
				ends[find] = num;
			}
		}
		return len;
	}

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

}