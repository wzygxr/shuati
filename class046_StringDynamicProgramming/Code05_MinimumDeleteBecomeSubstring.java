import java.util.ArrayList;
import java.util.List;

/**
 * 删除至少几个字符可以变成另一个字符串的子串（Minimum Delete Become Substring）
 * 给定两个字符串s1和s2，返回s1至少删除多少字符可以成为s2的子串
 * 
 * 算法核心思想：
 * 使用动态规划解决字符串子串匹配问题，通过构建二维DP表来计算最少删除字符数
 * 
 * 时间复杂度分析：
 * - 暴力版本：O(2^n * m)，其中n为s1的长度，m为s2的长度
 * - 动态规划版本：O(n*m)
 * 
 * 空间复杂度分析：
 * - 暴力版本：O(2^n)
 * - 动态规划版本：O(n*m)
 * 
 * 最优解判定：✅ 是最优解，时间复杂度无法进一步优化
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性
 * 2. 边界条件：处理空字符串和极端情况
 * 3. 性能优化：使用动态规划避免指数级时间复杂度
 * 4. 代码可读性：添加详细注释和测试用例
 * 
 * 与其他领域的联系：
 * - 文本处理：字符串匹配和编辑操作
 * - 生物信息学：序列比对和基因分析
 * - 数据压缩：最小编辑距离计算
 */
public class Code05_MinimumDeleteBecomeSubstring {

	/**
	 * 暴力方法（用于验证）
	 * 通过生成s1的所有子序列并检查是否为s2的子串来计算结果
	 * 由于时间复杂度为指数级，仅适用于小规模数据
	 * 
	 * @param s1 源字符串
	 * @param s2 目标字符串
	 * @return s1至少需要删除的字符数
	 */
	// 暴力方法
	// 为了验证
	public static int minDelete1(String s1, String s2) {
		// 输入验证
		if (s1 == null || s2 == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		List<String> list = new ArrayList<>();
		f(s1.toCharArray(), 0, "", list);
		// 排序 : 长度大的子序列先考虑
		// 因为如果长度大的子序列是s2的子串
		// 那么需要删掉的字符数量 = s1的长度 - s1子序列长度
		// 子序列长度越大，需要删掉的字符数量就越少
		// 所以长度大的子序列先考虑
		list.sort((a, b) -> b.length() - a.length());
		for (String str : list) {
			if (s2.indexOf(str) != -1) {
				// 检查s2中，是否包含当前的s1子序列str
				return s1.length() - str.length();
			}
		}
		return s1.length();
	}

	/**
	 * 递归生成字符串的所有子序列
	 * 
	 * @param s1   源字符串字符数组
	 * @param i    当前处理的字符索引
	 * @param path 当前生成的子序列
	 * @param list 存储所有子序列的列表
	 */
	// 生成s1字符串的所有子序列串
	public static void f(char[] s1, int i, String path, List<String> list) {
		if (i == s1.length) {
			list.add(path);
		} else {
			// 不选择当前字符
			f(s1, i + 1, path, list);
			// 选择当前字符
			f(s1, i + 1, path + s1[i], list);
		}
	}

	/*
	 * 最少删除字符成为子串问题 - 动态规划解法
	 * dp[i][j] 表示s1的前i个字符至少删除多少字符，可以变成s2的前j个字符的后缀
	 * 
	 * 状态转移方程：
	 * 如果 s1[i-1] == s2[j-1]
	 *   dp[i][j] = dp[i-1][j-1]  // 不需要删除
	 * 否则
	 *   dp[i][j] = dp[i-1][j] + 1  // 必须删除s1[i-1]
	 * 
	 * 解释：
	 * 我们的目标是让s1的一个子序列成为s2的子串
	 * 所以对于s1的前i个字符，我们要让它变成s2的某个后缀（这样就能成为子串）
	 * 
	 * 边界条件：
	 * dp[0][j] = 0，表示空字符串不需要删除就能成为任何字符串的后缀
	 * dp[i][0] = i，表示s1的前i个字符要变成空字符串需要删除i个字符
	 * 
	 * 最终答案：
	 * min{dp[n][j]} for j in [0, m]
	 * 
	 * 时间复杂度：O(n*m)，其中n为s1的长度，m为s2的长度
	 * 空间复杂度：O(n*m)
	 */
	// 正式方法，动态规划
	// 已经展示太多次从递归到动态规划了
	// 直接写动态规划吧
	// 也不做空间压缩了，因为千篇一律
	// 有兴趣的同学自己试试
	public static int minDelete2(String str1, String str2) {
		// 输入验证
		if (str1 == null || str2 == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;
		int m = s2.length;
		
		// 边界情况处理
		if (n == 0) return 0; // 空字符串不需要删除
		if (m == 0) return n; // 目标字符串为空，需要删除所有字符
		
		// dp[len1][len2] :
		// s1[前缀长度为i]至少删除多少字符，可以变成s2[前缀长度为j]的任意后缀串
		int[][] dp = new int[n + 1][m + 1];
		
		// 初始化边界条件
		for (int i = 1; i <= n; i++) {
			dp[i][0] = i; // s1的前i个字符要变成空字符串需要删除i个字符
		}
		// dp[0][j] = 0 默认初始化为0，表示空字符串不需要删除
		
		// 填充DP表
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				if (s1[i - 1] == s2[j - 1]) {
					// 字符相同，不需要删除
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					// 字符不同，必须删除s1[i-1]
					dp[i][j] = dp[i - 1][j] + 1;
				}
			}
		}
		
		// 寻找最小删除数
		int ans = Integer.MAX_VALUE;
		for (int j = 0; j <= m; j++) {
			ans = Math.min(ans, dp[n][j]);
		}
		return ans;
	}

	/**
	 * 生成随机字符串用于测试
	 * 
	 * @param n 字符串长度
	 * @param v 字符种类数
	 * @return 随机生成的字符串
	 */
	// 为了验证
	// 生成长度为n，有v种字符的随机字符串
	public static String randomString(int n, int v) {
		char[] ans = new char[n];
		for (int i = 0; i < n; i++) {
			ans[i] = (char) ('a' + (int) (Math.random() * v));
		}
		return String.valueOf(ans);
	}

	/**
	 * 主函数，用于测试和验证
	 */
	// 为了验证
	// 对数器
	public static void main(String[] args) {
		// 测试的数据量比较小
		// 那是因为数据量大了，暴力方法过不了
		// 但是这个数据量足够说明正式方法是正确的
		int n = 12;
		int v = 3;
		int testTime = 20000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int len1 = (int) (Math.random() * n) + 1;
			int len2 = (int) (Math.random() * n) + 1;
			String s1 = randomString(len1, v);
			String s2 = randomString(len2, v);
			int ans1 = minDelete1(s1, s2);
			int ans2 = minDelete2(s1, s2);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}