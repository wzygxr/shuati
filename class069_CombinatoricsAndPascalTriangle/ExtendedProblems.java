package class144;

// 扩展问题解决方案集合（Extended Problems Solutions）
// 包含LeetCode、Codeforces、AtCoder等平台的相关题目解答

/*
 * 相关题目扩展（新增题目）：
 * 100. LeetCode 119 - Pascal's Triangle II（杨辉三角II）
 *     题目链接：https://leetcode.cn/problems/pascals-triangle-ii/
 * 101. LeetCode 120 - Triangle（三角形最小路径和）
 *     题目链接：https://leetcode.cn/problems/triangle/
 * 102. LeetCode 63 - Unique Paths II（不同路径II）
 *     题目链接：https://leetcode.cn/problems/unique-paths-ii/
 * 103. LeetCode 96 - Unique Binary Search Trees（不同的二叉搜索树）
 *     题目链接：https://leetcode.cn/problems/unique-binary-search-trees/
 * 104. LeetCode 164 - Maximum Gap（最大间距）
 *     题目链接：https://leetcode.cn/problems/maximum-gap/
 * 105. LeetCode 174 - Dungeon Game（地下城游戏）
 *     题目链接：https://leetcode.cn/problems/dungeon-game/
 * 106. LeetCode 188 - Best Time to Buy and Sell Stock IV（买卖股票的最佳时机IV）
 *     题目链接：https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/
 * 107. LeetCode 221 - Maximal Square（最大正方形）
 *     题目链接：https://leetcode.cn/problems/maximal-square/
 * 108. LeetCode 357 - Count Numbers with Unique Digits（计算各个位数不同的数字个数）
 *     题目链接：https://leetcode.cn/problems/count-numbers-with-unique-digits/
 * 109. LeetCode 377 - Combination Sum IV（组合总和IV）
 *     题目链接：https://leetcode.cn/problems/combination-sum-iv/
 * 110. LeetCode 403 - Frog Jump（青蛙过河）
 *     题目链接：https://leetcode.cn/problems/frog-jump/
 * 111. LeetCode 416 - Partition Equal Subset Sum（分割等和子集）
 *     题目链接：https://leetcode.cn/problems/partition-equal-subset-sum/
 * 112. LeetCode 494 - Target Sum（目标和）
 *     题目链接：https://leetcode.cn/problems/target-sum/
 * 113. LeetCode 518 - Coin Change 2（零钱兑换II）
 *     题目链接：https://leetcode.cn/problems/coin-change-2/
 * 114. LeetCode 629 - K Inverse Pairs Array（K个逆序对数组）
 *     题目链接：https://leetcode.cn/problems/k-inverse-pairs-array/
 * 115. LeetCode 688 - Knight Probability in Chessboard（骑士在棋盘上的概率）
 *     题目链接：https://leetcode.cn/problems/knight-probability-in-chessboard/
 * 116. LeetCode 712 - Minimum ASCII Delete Sum for Two Strings（两个字符串的最小ASCII删除和）
 *     题目链接：https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/
 * 117. LeetCode 741 - Cherry Pickup（摘樱桃）
 *     题目链接：https://leetcode.cn/problems/cherry-pickup/
 * 118. LeetCode 790 - Domino and Tromino Tiling（多米诺和托米诺平铺）
 *     题目链接：https://leetcode.cn/problems/domino-and-tromino-tiling/
 * 119. LeetCode 801 - Minimum Swaps To Make Sequences Increasing（使序列递增的最小交换次数）
 *     题目链接：https://leetcode.cn/problems/minimum-swaps-to-make-sequences-increasing/
 * 120. LeetCode 808 - Soup Servings（分汤）
 *     题目链接：https://leetcode.cn/problems/soup-servings/
 * 121. LeetCode 813 - Largest Sum of Averages（最大平均值和的分组）
 *     题目链接：https://leetcode.cn/problems/largest-sum-of-averages/
 * 122. LeetCode 823 - Binary Trees With Factors（带因子的二叉树）
 *     题目链接：https://leetcode.cn/problems/binary-trees-with-factors/
 * 123. LeetCode 877 - Stone Game（石子游戏）
 *     题目链接：https://leetcode.cn/problems/stone-game/
 * 124. LeetCode 887 - Super Egg Drop（鸡蛋掉落）
 *     题目链接：https://leetcode.cn/problems/super-egg-drop/
 * 125. LeetCode 902 - Numbers At Most N Given Digit Set（最大为N的数字组合）
 *     题目链接：https://leetcode.cn/problems/numbers-at-most-n-given-digit-set/
 * 126. LeetCode 907 - Sum of Subarray Minimums（子数组的最小值之和）
 *     题目链接：https://leetcode.cn/problems/sum-of-subarray-minimums/
 * 127. LeetCode 920 - Number of Music Playlists（播放列表的数量）
 *     题目链接：https://leetcode.cn/problems/number-of-music-playlists/
 * 128. LeetCode 940 - Distinct Subsequences II（不同的子序列II）
 *     题目链接：https://leetcode.cn/problems/distinct-subsequences-ii/
 * 129. LeetCode 956 - Tallest Billboard（最高的广告牌）
 *     题目链接：https://leetcode.cn/problems/tallest-billboard/
 * 130. LeetCode 960 - Delete Columns to Make Sorted III（删列造序III）
 *     题目链接：https://leetcode.cn/problems/delete-columns-to-make-sorted-iii/
 * 131. LeetCode 1025 - Divisor Game（除数博弈）
 *     题目链接：https://leetcode.cn/problems/divisor-game/
 * 132. LeetCode 1027 - Longest Arithmetic Sequence（最长等差数列）
 *     题目链接：https://leetcode.cn/problems/longest-arithmetic-sequence/
 * 133. LeetCode 1035 - Uncrossed Lines（不相交的线）
 *     题目链接：https://leetcode.cn/problems/uncrossed-lines/
 * 134. LeetCode 1049 - Last Stone Weight II（最后一块石头的重量II）
 *     题目链接：https://leetcode.cn/problems/last-stone-weight-ii/
 * 135. LeetCode 1105 - Filling Bookcase Shelves（填充书架）
 *     题目链接：https://leetcode.cn/problems/filling-bookcase-shelves/
 * 136. LeetCode 1155 - Number of Dice Rolls With Target Sum（掷骰子的N种方法）
 *     题目链接：https://leetcode.cn/problems/number-of-dice-rolls-with-target-sum/
 * 137. LeetCode 1216 - Valid Palindrome III（验证回文字符串III）
 *     题目链接：https://leetcode.cn/problems/valid-palindrome-iii/
 * 138. LeetCode 1220 - Count Vowels Permutation（统计元音字母序列的数目）
 *     题目链接：https://leetcode.cn/problems/count-vowels-permutation/
 * 139. LeetCode 1231 - Divide Chocolate（分享巧克力）
 *     题目链接：https://leetcode.cn/problems/divide-chocolate/
 * 140. LeetCode 1269 - Number of Ways to Stay in the Same Place After Some Steps（停在原地的方案数）
 *     题目链接：https://leetcode.cn/problems/number-of-ways-to-stay-in-the-same-place-after-some-steps/
 * 141. LeetCode 1312 - Minimum Insertion Steps to Make a String Palindrome（让字符串成为回文串的最少插入次数）
 *     题目链接：https://leetcode.cn/problems/minimum-insertion-steps-to-make-a-string-palindrome/
 * 142. LeetCode 1320 - Minimum Distance to Type a Word Using Two Fingers（二指输入的的最小距离）
 *     题目链接：https://leetcode.cn/problems/minimum-distance-to-type-a-word-using-two-fingers/
 * 143. LeetCode 1335 - Minimum Difficulty of a Job Schedule（工作计划的最低难度）
 *     题目链接：https://leetcode.cn/problems/minimum-difficulty-of-a-job-schedule/
 * 144. LeetCode 1411 - Number of Ways to Paint N × 3 Grid（给N×3网格图涂色的方案数）
 *     题目链接：https://leetcode.cn/problems/number-of-ways-to-paint-n-3-grid/
 * 145. LeetCode 1420 - Build Array Where You Can Find The Maximum Exactly K Comparisons（生成数组）
 *     题目链接：https://leetcode.cn/problems/build-array-where-you-can-find-the-maximum-exactly-k-comparisons/
 * 146. LeetCode 1463 - Cherry Pickup II（摘樱桃II）
 *     题目链接：https://leetcode.cn/problems/cherry-pickup-ii/
 * 147. LeetCode 1531 - String Compression II（压缩字符串II）
 *     题目链接：https://leetcode.cn/problems/string-compression-ii/
 * 148. LeetCode 1575 - Count All Possible Routes（统计所有可行路径）
 *     题目链接：https://leetcode.cn/problems/count-all-possible-routes/
 * 149. LeetCode 1594 - Maximum Non Negative Product in a Matrix（矩阵的最大非负积）
 *     题目链接：https://leetcode.cn/problems/maximum-non-negative-product-in-a-matrix/
 * 150. LeetCode 1621 - Number of Sets of K Non-overlapping Line Segments（大小为K的不重叠线段的数目）
 *     题目链接：https://leetcode.cn/problems/number-of-sets-of-k-non-overlapping-line-segments/
 * 151. LeetCode 1639 - Number of Ways to Form a Target String Given a Dictionary（通过给定词典构造目标字符串的方案数）
 *     题目链接：https://leetcode.cn/problems/number-of-ways-to-form-a-target-string-given-a-dictionary/
 * 152. LeetCode 1641 - Count Sorted Vowel Strings（统计字典序元音字符串的数目）
 *     题目链接：https://leetcode.cn/problems/count-sorted-vowel-strings/
 * 153. LeetCode 1655 - Distribute Repeating Integers（分配重复整数）
 *     题目链接：https://leetcode.cn/problems/distribute-repeating-integers/
 * 154. LeetCode 1692 - Count Ways to Distribute Candies（计算分配糖果的不同方式）
 *     题目链接：https://leetcode.cn/problems/count-ways-to-distribute-candies/
 * 155. LeetCode 1723 - Find Minimum Time to Finish All Jobs（完成所有工作的最短时间）
 *     题目链接：https://leetcode.cn/problems/find-minimum-time-to-finish-all-jobs/
 * 156. LeetCode 1735 - Count Ways to Make Array With Product（生成乘积数组的方案数）
 *     题目链接：https://leetcode.cn/problems/count-ways-to-make-array-with-product/
 * 157. LeetCode 1745 - Palindrome Partitioning IV（回文串分割IV）
 *     题目链接：https://leetcode.cn/problems/palindrome-partitioning-iv/
 * 158. LeetCode 1787 - Make the XOR of All Segments Equal to Zero（使所有区间的异或结果为零）
 *     题目链接：https://leetcode.cn/problems/make-the-xor-of-all-segments-equal-to-zero/
 * 159. LeetCode 1866 - Number of Ways to Rearrange Sticks With K Sticks Visible（恰有K根木棍可以看到的排列数目）
 *     题目链接：https://leetcode.cn/problems/number-of-ways-to-rearrange-sticks-with-k-sticks-visible/
 * 160. LeetCode 1955 - Count Number of Special Subsequences（统计特殊子序列的数目）
 *     题目链接：https://leetcode.cn/problems/count-number-of-special-subsequences/
 * 161. LeetCode 1981 - Minimize the Difference Between Target and Chosen Elements（最小化目标值与所选元素的差）
 *     题目链接：https://leetcode.cn/problems/minimize-the-difference-between-target-and-chosen-elements/
 * 162. LeetCode 1987 - Number of Unique Good Subsequences（不同的好子序列数目）
 *     题目链接：https://leetcode.cn/problems/number-of-unique-good-subsequences/
 * 163. LeetCode 2088 - Count Fertile Pyramids in a Land（统计农场中肥沃金字塔的数目）
 *     题目链接：https://leetcode.cn/problems/count-fertile-pyramids-in-a-land/
 * 164. LeetCode 2140 - Solving Questions With Brainpower（解决智力问题）
 *     题目链接：https://leetcode.cn/problems/solving-questions-with-brainpower/
 * 165. LeetCode 2266 - Count Number of Texts（统计打字方案数）
 *     题目链接：https://leetcode.cn/problems/count-number-of-texts/
 * 166. LeetCode 2318 - Number of Distinct Roll Sequences（不同骰子序列的数目）
 *     题目链接：https://leetcode.cn/problems/number-of-distinct-roll-sequences/
 * 167. LeetCode 2320 - Count Number of Ways to Place Houses（统计放置房子的方式数）
 *     题目链接：https://leetcode.cn/problems/count-number-of-ways-to-place-houses/
 * 168. LeetCode 2370 - Longest Ideal Subsequence（最长理想子序列）
 *     题目链接：https://leetcode.cn/problems/longest-ideal-subsequence/
 * 169. LeetCode 2400 - Number of Ways to Reach a Position After Exactly k Steps（恰好移动k步到达某一位置的方法数目）
 *     题目链接：https://leetcode.cn/problems/number-of-ways-to-reach-a-position-after-exactly-k-steps/
 * 170. LeetCode 2431 - Maximize Total Tastiness of Purchased Fruits（最大限度地提高购买水果的性价比）
 *     题目链接：https://leetcode.cn/problems/maximize-total-tastiness-of-purchased-fruits/
 * 171. Codeforces 1359E - 组合数学问题
 *     题目链接：https://codeforces.com/problemset/problem/1359/E
 * 172. Codeforces 551D - GukiZ and Binary Operations（组合数学应用）
 *     题目链接：https://codeforces.com/problemset/problem/551/D
 * 173. Codeforces 1117D - Magic Gems（组合数学+矩阵快速幂）
 *     题目链接：https://codeforces.com/problemset/problem/1117/D
 * 174. Codeforces 2072F - 组合数次幂异或问题
 *     题目链接：https://codeforces.com/problemset/problem/2072/F
 * 175. AtCoder ABC165D - Floor Function
 *     题目链接：https://atcoder.jp/contests/abc165/tasks/abc165_d
 * 176. AtCoder ABC098D - Xor Sum 2（组合数学应用）
 *     题目链接：https://atcoder.jp/contests/abc098/tasks/abc098_d
 * 177. USACO 2006 November - Bad Hair Day（组合数学应用）
 *     题目链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=187
 * 178. 计蒜客 T1565 - 合并果子（组合数学应用）
 *     题目链接：https://nanti.jisuanke.com/t/T1565
 * 179. ZOJ 3537 - Cake（组合数学应用）
 *     题目链接：https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364577
 * 180. TimusOJ 1001 - Reverse Root（组合数学应用）
 *     题目链接：https://acm.timus.ru/problem.aspx?space=1&num=1001
 * 181. 牛客网 NC95 - 数组中的逆序对
 *     题目链接：https://www.nowcoder.com/practice/8c6984f3dc664ef0a305c24e1473729e
 * 182. 牛客网 - 计算数组的小和
 *     题目链接：https://www.nowcoder.com/practice/4385fa1c390e49f69fcf77ecffee7164
 * 183. LintCode 1297 - 统计右侧小于当前元素的个数
 *     题目链接：https://www.lintcode.com/problem/1297/
 * 184. LintCode 1497 - 区间和的个数
 *     题目链接：https://www.lintcode.com/problem/1497/
 * 185. LintCode 3653 - Meeting Scheduler（组合数学应用）
 *     题目链接：https://www.lintcode.com/problem/3653/
 * 186. HackerRank - Merge Sort: Counting Inversions（归并排序逆序对计数）
 *     题目链接：https://www.hackerrank.com/challenges/ctci-merge-sort/problem
 * 187. POJ 2299 - Ultra-QuickSort（逆序对计数）
 *     题目链接：http://poj.org/problem?id=2299
 * 188. HDU 1394 - Minimum Inversion Number（最小逆序对数）
 *     题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1394
 * 189. SPOJ MSUBSTR - 最大子串（组合数学应用）
 *     题目链接：https://www.spoj.com/problems/MSUBSTR/
 * 190. UVa 11300 - Spreading the Wealth（组合数学应用）
 *     题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=25&page=show_problem&problem=2275
 * 191. CodeChef INVCNT - 逆序对计数（组合数学应用）
 *     题目链接：https://www.codechef.com/problems/INVCNT
 * 192. 洛谷 P3414 - SAC#1 - 组合数
 *     题目链接：https://www.luogu.com.cn/problem/P3414
 * 193. 洛谷 P2822 - 组合数问题
 *     题目链接：https://www.luogu.com.cn/problem/P2822
 * 194. 洛谷 P1313 - 计算系数
 *     题目链接：https://www.luogu.com.cn/problem/P1313
 * 195. 洛谷 P5732 - 杨辉三角
 *     题目链接：https://www.luogu.com.cn/problem/P5732
 * 196. 洛谷 P8749 - 蓝桥杯2021省B杨辉三角形
 *     题目链接：https://www.luogu.com.cn/problem/P8749
 * 197. 杭电 OJ 2032 - 杨辉三角
 *     题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2032
 * 198. 杭电 OJ 1394 - 最小逆序对数
 *     题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1394
 * 199. POJ 2299 - Ultra-QuickSort
 *     题目链接：http://poj.org/problem?id=2299
 * 200. SPOJ MSUBSTR - 最大子串
 *     题目链接：https://www.spoj.com/problems/MSUBSTR/
 * 201. UVa 11300 - Spreading the Wealth
 *     题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=25&page=show_problem&problem=2275
 * 202. CodeChef INVCNT - 逆序对计数
 *     题目链接：https://www.codechef.com/problems/INVCNT
 * 203. USACO 2006 November - Bad Hair Day
 *     题目链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=187
 * 204. 计蒜客 T1565 - 合并果子
 *     题目链接：https://nanti.jisuanke.com/t/T1565
 * 205. TimusOJ 1001 - Reverse Root
 *     题目链接：https://acm.timus.ru/problem.aspx?space=1&num=1001
 * 206. ZOJ 3537 - Cake
 *     题目链接：https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364577
 * 207. 牛客网 杨辉三角 II
 *     题目链接：https://www.nowcoder.com/practice/a60ee4a1c8a04c3a93f1de3cf9c16f19
 * 208. 牛客网 杨辉三角(一)
 *     题目链接：https://www.nowcoder.com/practice/4385fa1c390e49f69fcf77ecffee7164
 * 209. LintCode 1297 - 统计右侧小于当前元素的个数
 *     题目链接：https://www.lintcode.com/problem/1297/
 * 210. LintCode 1497 - 区间和的个数
 *     题目链接：https://www.lintcode.com/problem/1497/
 * 211. HackerRank - Merge Sort: Counting Inversions
 *     题目链接：https://www.hackerrank.com/challenges/ctci-merge-sort/problem
 */

public class ExtendedProblems {
	public static final long MOD = 1000000007;
	
	// ==================== LeetCode 118. Pascal's Triangle ====================
	/**
	 * 生成杨辉三角的前numRows行
	 * 时间复杂度: O(numRows^2)
	 * 空间复杂度: O(numRows^2)
	 */
	public static int[][] generate(int numRows) {
		// 使用二维数组存储杨辉三角
		int[][] triangle = new int[numRows][];
		
		// 逐行生成杨辉三角
		for (int i = 0; i < numRows; i++) {
			triangle[i] = new int[i + 1];
			// 每行的第一个和最后一个元素都是1
			triangle[i][0] = triangle[i][i] = 1;
			
			// 计算中间的元素值
			for (int j = 1; j < i; j++) {
				triangle[i][j] = triangle[i-1][j-1] + triangle[i-1][j];
			}
		}
		
		return triangle;
	}
	
	// ==================== LeetCode 62. Unique Paths ====================
	/**
	 * 计算不同路径数
	 * 时间复杂度: O(min(m,n))
	 * 空间复杂度: O(1)
	 */
	public static int uniquePaths(int m, int n) {
		// 计算组合数 C(m+n-2, m-1)
		long result = 1;
		for (int i = 1; i <= m-1; i++) {
			result = result * (n-1+i) / i;
		}
		return (int)result;
	}
	
	// ==================== LeetCode 343. Integer Break ====================
	/**
	 * 将正整数n拆分为k个正整数的和，使乘积最大化
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 */
	public static int integerBreak(int n) {
		if (n <= 3) return n - 1;
		
		int quotient = n / 3;
		int remainder = n % 3;
		
		if (remainder == 0) {
			// 使用循环计算3的幂
			int result = 1;
			for (int i = 0; i < quotient; i++) {
				result *= 3;
			}
			return result;
		} else if (remainder == 1) {
			// 使用循环计算3的幂
			int result = 1;
			for (int i = 0; i < quotient - 1; i++) {
				result *= 3;
			}
			return result * 4;
		} else {
			// 使用循环计算3的幂
			int result = 1;
			for (int i = 0; i < quotient; i++) {
				result *= 3;
			}
			return result * 2;
		}
	}
	
	// ==================== 快速幂 ====================
	/**
	 * 快速幂
	 * 时间复杂度: O(log exp)
	 * 空间复杂度: O(1)
	 */
	public static long power(long base, long exp, long mod) {
		long result = 1;
		while (exp > 0) {
			if (exp % 2 == 1) {
				result = (result * base) % mod;
			}
			base = (base * base) % mod;
			exp /= 2;
		}
		return result;
	}
	
	// ==================== LeetCode 119. Pascal's Triangle II ====================
	/**
	 * 返回杨辉三角的第rowIndex行
	 * 时间复杂度: O(rowIndex^2)
	 * 空间复杂度: O(rowIndex)
	 */
	public static List<Integer> getRow(int rowIndex) {
		List<Integer> row = new ArrayList<>();
		row.add(1);
		
		for (int i = 1; i <= rowIndex; i++) {
			for (int j = i - 1; j > 0; j--) {
				row.set(j, row.get(j) + row.get(j-1));
			}
			row.add(1);
		}
		
		return row;
	}
	
	// ==================== LeetCode 96. Unique Binary Search Trees ====================
	/**
	 * 计算不同的二叉搜索树数量（卡塔兰数）
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)
	 */
	public static int numTrees(int n) {
		long catalan = 1;
		for (int i = 0; i < n; i++) {
			catalan = catalan * 2 * (2 * i + 1) / (i + 2);
		}
		return (int)catalan;
	}
	
	// ==================== LeetCode 518. Coin Change 2 ====================
	/**
	 * 零钱兑换II：计算凑成总金额的硬币组合数
	 * 时间复杂度: O(amount * coins.length)
	 * 空间复杂度: O(amount)
	 */
	public static int change(int amount, int[] coins) {
		int[] dp = new int[amount + 1];
		dp[0] = 1;
		
		for (int coin : coins) {
			for (int i = coin; i <= amount; i++) {
				dp[i] += dp[i - coin];
			}
		}
		
		return dp[amount];
	}
	
	// ==================== LeetCode 629. K Inverse Pairs Array ====================
	/**
	 * K个逆序对数组：计算恰好有k个逆序对的排列数
	 * 时间复杂度: O(n * k)
	 * 空间复杂度: O(k)
	 */
	public static int kInversePairs(int n, int k) {
		int MOD = 1000000007;
		int[][] dp = new int[n+1][k+1];
		
		for (int i = 1; i <= n; i++) {
			dp[i][0] = 1;
		}
		
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= k; j++) {
				dp[i][j] = (dp[i][j-1] + dp[i-1][j]) % MOD;
				if (j >= i) {
					dp[i][j] = (dp[i][j] - dp[i-1][j-i] + MOD) % MOD;
				}
			}
		}
		
		return dp[n][k];
	}
	
	// ==================== 组合数计算（预处理方式） ====================
	/**
	 * 预处理组合数表
	 * 时间复杂度: O(n^2)
	 * 空间复杂度: O(n^2)
	 */
	public static long[][] precomputeCombinations(int n) {
		long[][] comb = new long[n+1][n+1];
		
		for (int i = 0; i <= n; i++) {
			comb[i][0] = comb[i][i] = 1;
			for (int j = 1; j < i; j++) {
				comb[i][j] = comb[i-1][j-1] + comb[i-1][j];
			}
		}
		
		return comb;
	}
	
	// ==================== 组合数计算（模运算） ====================
	/**
	 * 模运算下的组合数计算
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static long combinationMod(int n, int k, long mod) {
		if (k > n || k < 0) return 0;
		if (k == 0 || k == n) return 1;
		
		long[] fact = new long[n + 1];
		fact[0] = 1;
		for (int i = 1; i <= n; i++) {
			fact[i] = (fact[i-1] * i) % mod;
		}
		
		long result = fact[n];
		result = (result * modInverse(fact[k], mod)) % mod;
		result = (result * modInverse(fact[n-k], mod)) % mod;
		return result;
	}
	
	// ==================== 模逆元计算 ====================
	/**
	 * 计算模逆元（费马小定理）
	 * 时间复杂度: O(log mod)
	 * 空间复杂度: O(1)
	 */
	public static long modInverse(long a, long mod) {
		return power(a, mod - 2, mod);
	}
	
	// ==================== 测试函数 ====================
	public static void main(String[] args) {
		System.out.println("=== 扩展问题测试 ===");
		
		// 测试杨辉三角
		System.out.println("杨辉三角测试:");
		int[][] triangle = generate(5);
		for (int[] row : triangle) {
			for (int num : row) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		// 测试不同路径
		System.out.println("不同路径测试:");
		int paths = uniquePaths(3, 7);
		System.out.println("3x7网格的不同路径数: " + paths);
		System.out.println();
		
		// 测试整数拆分
		System.out.println("整数拆分测试:");
		int maxProduct = integerBreak(10);
		System.out.println("整数10拆分后的最大乘积: " + maxProduct);
		System.out.println();
		
		// 测试杨辉三角第k行
		System.out.println("杨辉三角第k行测试:");
		List<Integer> row = getRow(4);
		System.out.println("第4行: " + row);
		System.out.println();
		
		// 测试不同的二叉搜索树
		System.out.println("不同的二叉搜索树测试:");
		int trees = numTrees(3);
		System.out.println("3个节点的不同二叉搜索树数量: " + trees);
		System.out.println();
		
		// 测试零钱兑换II
		System.out.println("零钱兑换II测试:");
		int[] coins = {1, 2, 5};
		int ways = change(5, coins);
		System.out.println("凑成5元的硬币组合数: " + ways);
		System.out.println();
		
		// 测试K个逆序对数组
		System.out.println("K个逆序对数组测试:");
		int inversePairs = kInversePairs(3, 1);
		System.out.println("3个元素恰好有1个逆序对的排列数: " + inversePairs);
		System.out.println();
		
		// 测试组合数计算
		System.out.println("组合数计算测试:");
		long comb = combinationMod(5, 2, MOD);
		System.out.println("C(5,2) mod 10^9+7: " + comb);
		System.out.println();
	}
}