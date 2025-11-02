package class129;

import java.util.Arrays;

/**
 * LeetCode 466. 统计重复个数
 * 
 * 题目描述：
 * 如果字符串x删除一些字符，可以得到字符串y，那么就说y可以从x中获得
 * 给定s1和a，代表s1拼接a次，记为字符串x
 * 给定s2和b，代表s2拼接b次，记为字符串y
 * 现在把y拼接m次之后，得到的字符串依然可能从x中获得，返回尽可能大的m
 * 
 * 解题思路：
 * 这是一个字符串匹配与倍增优化相结合的问题。
 * 
 * 核心思想：
 * 1. 预处理：计算从s1的每个位置开始，匹配s2中每个字符需要的最小长度
 * 2. 倍增优化：使用倍增思想计算从s1的每个位置开始，匹配多个s2需要的长度
 * 3. 贪心匹配：尽可能多地匹配s2的重复串
 * 
 * 具体步骤：
 * 1. 预处理next数组：next[i][j]表示从s1的第i个位置开始，至少需要多少长度才能找到字符('a'+j)
 * 2. 预处理st数组：st[i][p]表示从s1的第i个位置开始，至少需要多少长度才能获得2^p个s2
 * 3. 使用倍增思想贪心匹配：从s1的开头开始，尽可能多地匹配s2的重复串
 * 
 * 时间复杂度：O(s1长度 * s2长度)
 * 空间复杂度：O(s1长度 * log(最大匹配数))
 * 
 * 相关题目：
 * 1. LeetCode 416. 分割等和子集 (动态规划)
 * 2. LeetCode 32. 最长有效括号 (动态规划)
 * 3. LeetCode 72. 编辑距离 (动态规划)
 * 4. LeetCode 115. 不同的子序列 (动态规划)
 * 5. LeetCode 686. 重复叠加字符串匹配 (字符串匹配)
 * 6. Codeforces 1083F. The Fair Nut and Amusing Xor
 * 7. AtCoder ABC128D. equeue
 * 8. 牛客网 NC46. 加起来和为目标值的组合
 * 9. 杭电OJ 3572. Task Schedule
 * 10. UVa 10158. War
 * 11. CodeChef - MAXSEGMENTS
 * 12. SPOJ - BUSYMAN
 * 13. Project Euler 318. Cutting Game
 * 14. HackerEarth - Job Scheduling Problem
 * 15. 计蒜客 - 工作安排
 * 16. ZOJ 3623. Battle Ships
 * 17. acwing 2068. 整数拼接
 * 
 * 工程化考量：
 * 1. 在实际应用中，这类字符串匹配算法常用于：
 *    - 文本编辑器中的查找替换功能
 *    - 生物信息学中的序列匹配
 *    - 数据压缩算法
 * 2. 实现优化：
 *    - 对于大规模数据，可以使用KMP算法优化字符串匹配
 *    - 可以使用滚动哈希优化重复计算
 *    - 对于多次查询，可以预处理更多数据以加速查询
 * 3. 可扩展性：
 *    - 支持动态添加和删除字符
 *    - 处理多种匹配模式（不仅仅是重复串）
 *    - 扩展到多模式匹配问题
 * 4. 鲁棒性考虑：
 *    - 处理空字符串和边界情况
 *    - 处理大规模数据时的内存管理
 *    - 优化极端情况下的性能
 */
public class Code03_CountRepetitions {

	/**
	 * 计算最大重复次数
	 * 
	 * @param str1 字符串s1
	 * @param a s1重复次数
	 * @param str2 字符串s2
	 * @param b s2重复次数
	 * @return 最大重复次数m
	 */
	// 该题的题解中有很多打败比例优异，但是时间复杂度不是最优的方法
	// 如果数据苛刻一些，就通过不了，所以一定要做到时间复杂度与a、b的值无关
	// 本方法时间复杂度O(s1长度 * s2长度)，一定是最优解，而且比其他方法更好理解
	public static int getMaxRepetitions(String str1, int a, String str2, int b) {
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;
		// next[i][j] : 从i位置出发，至少需要多少长度，能找到j字符
		int[][] next = new int[n][26];
		// 时间复杂度O(s1长度 + s2长度)
		if (!find(s1, n, next, s2)) {
			return 0;
		}
		// st[i][p] : 从i位置出发，至少需要多少长度，可以获得2^p个s2
		long[][] st = new long[n][30];
		// 时间复杂度O(s1长度 * s2长度)
		for (int i = 0, cur, len; i < n; i++) {
			cur = i;
			len = 0;
			for (char c : s2) {
				len += next[cur][c - 'a'];
				cur = (cur + next[cur][c - 'a']) % n;
			}
			st[i][0] = len;
		}
		// 时间复杂度O(s1长度)
		for (int p = 1; p <= 29; p++) {
			for (int i = 0; i < n; i++) {
				st[i][p] = st[i][p - 1] + st[(int) ((st[i][p - 1] + i) % n)][p - 1];
			}
		}
		long ans = 0;
		// 时间复杂度O(1)
		for (int p = 29, start = 0; p >= 0; p--) {
			if (st[start % n][p] + start <= n * a) {
				ans += 1 << p;
				start += st[start % n][p];
			}
		}
		return (int) (ans / b);
	}

	/**
	 * 预处理next数组
	 * 
	 * @param s1 字符串s1的字符数组
	 * @param n s1的长度
	 * @param next next数组
	 * @param s2 字符串s2的字符数组
	 * @return 是否可以找到s2中的所有字符
	 */
	// 时间复杂度O(s1长度 + s2长度)
	public static boolean find(char[] s1, int n, int[][] next, char[] s2) {
		int[] right = new int[26];
		Arrays.fill(right, -1);
		for (int i = n - 1; i >= 0; i--) {
			right[s1[i] - 'a'] = i + n;
		}
		for (int i = n - 1; i >= 0; i--) {
			right[s1[i] - 'a'] = i;
			for (int j = 0; j < 26; j++) {
				if (right[j] != -1) {
					next[i][j] = right[j] - i + 1;
				} else {
					next[i][j] = -1;
				}
			}
		}
		for (char c : s2) {
			if (next[0][c - 'a'] == -1) {
				return false;
			}
		}
		return true;
	}

}