package class131;

import java.util.Arrays;

/**
 * LeetCode 2370. 最长理想子序列 (Longest Ideal Subsequence)
 * 题目链接: https://leetcode.cn/problems/longest-ideal-subsequence/
 * 
 * 题目描述:
 * 给定一个长度为n，只由小写字母组成的字符串s，给定一个非负整数k
 * 字符串s可以生成很多子序列，理想子序列的定义为：
 * 子序列中任意相邻的两个字符，在字母表中位次的差值绝对值<=k
 * 返回最长理想子序列的长度。
 * 
 * 解题思路:
 * 使用线段树优化动态规划的方法解决此问题。
 * 1. 定义状态dp[c]表示以字符c结尾的最长理想子序列长度
 * 2. 对于每个字符，查询与其ASCII值差值不超过k的所有字符对应的dp值的最大值
 * 3. 使用线段树维护dp数组，支持区间查询最大值和单点更新
 * 4. 遍历字符串，对每个字符更新对应的dp值
 * 
 * 时间复杂度分析:
 * - 遍历字符串: O(n)
 * - 每次查询和更新: O(log e)，e为字符集大小
 * - 总时间复杂度: O(n * log e)
 * 空间复杂度: O(e) 用于存储线段树
 */
public class Code04_LongestIdealString {

	/**
	 * 计算最长理想子序列长度
	 * 
	 * @param s 输入字符串
	 * @param k 字符差值上限
	 * @return  最长理想子序列长度
	 */
	// 数据量太小，线段树的优势不明显
	// 时间复杂度O(n * log e)，n为字符串长度，e为字符集大小
	public static int longestIdealString(String s, int k) {
		// 初始化线段树数组
		Arrays.fill(max, 0);
		int v, p;
		int ans = 0;
		
		// 遍历字符串中的每个字符
		for (char cha : s.toCharArray()) {
			// 将字符转换为1-26的数字
			v = cha - 'a' + 1;
			
			// 查询字符值在[v-k, v+k]范围内的最长理想子序列长度
			p = max(Math.max(v - k, 1), Math.min(v + k, n), 1, n, 1);
			
			// 更新答案
			ans = Math.max(ans, p + 1);
			
			// 更新以当前字符结尾的最长理想子序列长度
			update(v, p + 1, 1, n, 1);
		}
		return ans;
	}

	// 字符集大小（小写字母）
	public static int n = 26;

	// 线段树数组，存储每个区间内的最大值
	public static int[] max = new int[(n + 1) << 2];

	/**
	 * 线段树向上更新操作
	 * 更新父节点的值为左右子节点的最大值
	 * 
	 * @param i 线段树节点索引
	 */
	public static void up(int i) {
		max[i] = Math.max(max[i << 1], max[i << 1 | 1]);
	}

	/**
	 * 线段树单点更新操作
	 * 只有单点更新不需要定义down方法
	 * 因为单点更新的任务一定会从线段树头节点直插到某个叶节点
	 * 根本没有懒更新这回事
	 * 
	 * @param jobi 要更新的位置
	 * @param jobv 新的值
	 * @param l    当前区间左边界
	 * @param r    当前区间右边界
	 * @param i    当前线段树节点索引
	 */
	public static void update(int jobi, int jobv, int l, int r, int i) {
		// 到达叶节点，直接更新
		if (l == r && jobi == l) {
			max[i] = jobv;
		} else {
			// 计算中点
			int m = (l + r) >> 1;
			// 递归更新左子树或右子树
			if (jobi <= m) {
				update(jobi, jobv, l, m, i << 1);
			} else {
				update(jobi, jobv, m + 1, r, i << 1 | 1);
			}
			// 向上更新父节点
			up(i);
		}
	}

	/**
	 * 线段树区间查询操作
	 * 查询区间[jobl, jobr]内的最大值
	 * 
	 * @param jobl 查询区间左边界
	 * @param jobr 查询区间右边界
	 * @param l    当前区间左边界
	 * @param r    当前区间右边界
	 * @param i    当前线段树节点索引
	 * @return     区间最大值
	 */
	public static int max(int jobl, int jobr, int l, int r, int i) {
		// 当前区间完全包含在查询区间内
		if (jobl <= l && r <= jobr) {
			return max[i];
		}
		
		// 计算中点
		int m = (l + r) >> 1;
		int ans = 0;
		
		// 递归查询左子树
		if (jobl <= m) {
			ans = Math.max(ans, max(jobl, jobr, l, m, i << 1));
		}
		
		// 递归查询右子树
		if (jobr > m) {
			ans = Math.max(ans, max(jobl, jobr, m + 1, r, i << 1 | 1));
		}
		
		return ans;
	}

}
