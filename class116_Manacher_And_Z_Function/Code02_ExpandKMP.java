package class103;

// 扩展KMP模版，又称Z算法或Z函数
// 给定两个字符串a、b，求出两个数组
// b与b每一个后缀串的最长公共前缀长度，z数组
// b与a每一个后缀串的最长公共前缀长度，e数组
// 计算出要求的两个数组后，输出这两个数组的权值即可
// 对于一个数组x，i位置的权值定义为 : (i * (x[i] + 1))
// 数组权值为所有位置权值的异或和
// 测试链接 : https://www.luogu.com.cn/problem/P5410
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 扩展KMP算法（Z函数）工程化实现
 * 
 * 该类提供了Z函数（扩展KMP）的高效实现，并集成了以下工程化特性：
 * 1. 参数验证与异常处理
 * 2. 性能监控与日志记录
 * 3. 可配置的调试选项
 * 4. 单元测试支持
 * 5. 边界情况处理
 * 
 * 算法应用场景：
 * 1. LeetCode 2223. 构造字符串的总得分和 - https://leetcode.com/problems/sum-of-scores-of-built-strings/
 * 2. LeetCode 3031. 将单词恢复初始状态所需的最短时间 II - https://leetcode.com/problems/minimum-time-to-revert-word-to-initial-state-ii/
 * 3. Codeforces 126B. Password - https://codeforces.com/problemset/problem/126/B
 * 4. 洛谷 P5410 【模板】扩展KMP/exKMP（Z 函数） - https://www.luogu.com.cn/problem/P5410
 * 5. SPOJ - Pattern Find
 * 6. HackerEarth - String Similarity - https://www.hackerearth.com/practice/algorithms/string-algorithm/z-algorithm/tutorial/
 * 7. AtCoder ABC141E - Who Says a Pun? - https://atcoder.jp/contests/abc141/tasks/abc141_e
 * 8. USACO 2011 November Contest, Bronze - Cow Photographs
 * 9. 牛客网 NC15051 - 字符串的匹配
 */
public class Code02_ExpandKMP {
	// 日志记录器
	private static final Logger logger = Logger.getLogger(Code02_ExpandKMP.class.getName());
	
	// 性能监控开关
	private static boolean performanceMonitoring = false;
	
	// 调试信息开关
	private static boolean debugMode = false;
	
	// 预分配的最大数组大小
	public static int MAXN = 20000001;

	// Z数组和E数组
	public static int[] z = new int[MAXN];
	public static int[] e = new int[MAXN];
	
	/**
	 * 设置性能监控状态
	 */
	public static void setPerformanceMonitoring(boolean enabled) {
		performanceMonitoring = enabled;
		logger.info("性能监控已" + (enabled ? "开启" : "关闭"));
	}
	
	/**
	 * 设置调试模式状态
	 */
	public static void setDebugMode(boolean enabled) {
		debugMode = enabled;
		logger.info("调试模式已" + (enabled ? "开启" : "关闭"));
	}
	
	/**
	 * 记录性能日志
	 */
	private static void logPerformance(String operation, long startTime, long endTime) {
		if (performanceMonitoring) {
			long duration = endTime - startTime;
			logger.info(String.format("操作 [%s] 耗时: %d ms", operation, duration));
		}
	}
	
	/**
	 * 记录调试日志
	 */
	private static void logDebug(String message) {
		if (debugMode) {
			logger.info("[调试] " + message);
		}
	}

	public static void main(String[] args) {
		// 初始化日志记录器
		logger.info("Z算法（扩展KMP）程序启动 - " + 
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		
		// 解析命令行参数
		for (String arg : args) {
			if (arg.equals("--debug")) {
				setDebugMode(true);
			} else if (arg.equals("--performance")) {
				setPerformanceMonitoring(true);
			} else if (arg.equals("--test")) {
				runUnitTests();
				return;
			}
		}
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
			
			char[] a = null;
			char[] b = null;
			
			// 读取输入
			String line = in.readLine();
			if (line != null) {
				a = line.toCharArray();
				line = in.readLine();
				if (line != null) {
					b = line.toCharArray();
				}
			}
			
			// 参数验证
			if (a == null || b == null) {
				throw new IllegalArgumentException("输入数据不完整，请提供两个字符串");
			}
			
			logDebug("输入字符串 a: " + new String(a));
			logDebug("输入字符串 b: " + new String(b));
			
			// 确保数组大小足够
			ensureArrayCapacity(Math.max(a.length, b.length));
			
			// 执行算法并记录性能
			long startTime = System.currentTimeMillis();
			zArray(b, b.length);
			logPerformance("Z数组计算", startTime, System.currentTimeMillis());
			
			startTime = System.currentTimeMillis();
			eArray(a, b, a.length, b.length);
			logPerformance("E数组计算", startTime, System.currentTimeMillis());
			
			// 计算并输出结果
			long resultZ = eor(z, b.length);
			long resultE = eor(e, a.length);
			
			out.println(resultZ);
			out.println(resultE);
			out.flush();
			
			logDebug("Z数组权值异或和: " + resultZ);
			logDebug("E数组权值异或和: " + resultE);
			
			// 关闭资源
			in.close();
			out.close();
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IO异常: " + e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, "参数错误: " + e.getMessage(), e);
			System.err.println("错误: " + e.getMessage());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "未预期的异常: " + e.getMessage(), e);
		}
		
		logger.info("程序执行完成 - " + 
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	}
	
	/**
	 * 确保预分配的数组容量足够大
	 */
	private static void ensureArrayCapacity(int requiredSize) {
		if (requiredSize > MAXN) {
			MAXN = requiredSize + 100; // 添加一些缓冲
			z = Arrays.copyOf(z, MAXN);
			e = Arrays.copyOf(e, MAXN);
			logDebug("已调整数组大小为: " + MAXN);
		}
	}

	/**
	 * Z函数计算
	 * Z函数z[i]表示字符串s从位置i开始与字符串s从位置0开始的最长公共前缀长度
	 * 
	 * 算法原理：
	 * 1. 维护一个匹配区间[l, r]，表示当前已知的最右匹配区间
	 * 2. 对于当前位置i，如果i <= r，可以利用已计算的信息优化
	 * 3. 利用对称性，z[i]至少为min(r - i + 1, z[i - l])
	 * 4. 在此基础之上继续向右扩展匹配
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 * 
	 * @param s 输入字符串
	 * @param n 字符串长度
	 * @throws IllegalArgumentException 当输入参数无效时抛出
	 */
	// 非常像Manacher算法
	public static void zArray(char[] s, int n) {
		// 参数验证
		if (s == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		if (n < 0 || n > s.length) {
			throw new IllegalArgumentException("字符串长度无效: " + n);
		}
		
		// 处理边界情况
		if (n == 0) {
			return;
		}
		
		logDebug("开始计算Z数组，字符串长度: " + n);
		
		z[0] = n;
		// l: 当前最右匹配区间的左边界
		// r: 当前最右匹配区间的右边界
		// len: 当前位置的Z值（最长公共前缀长度）
		for (int i = 1, c = 1, r = 1, len; i < n; i++) {
			// 利用已计算的信息优化
			// 如果i在当前匹配区间内
			len = r > i ? Math.min(r - i, z[i - c]) : 0;
			
			// 继续向右扩展匹配
			// 使用局部变量s来减少数组访问开销
			final char[] localS = s;
			while (i + len < n && localS[i + len] == localS[len]) {
				len++;
			}
			
			// 更新最右匹配区间
			if (i + len > r) {
				r = i + len;
				c = i;
			}
			
			z[i] = len;
		}
		
		if (debugMode && n <= 1000) { // 避免大数据量时日志过多
			logDebug("Z数组计算完成，前10个值: " + 
					Arrays.toString(Arrays.copyOfRange(z, 0, Math.min(n, 10))));
		}
	}

	/**
	 * 扩展KMP计算
	 * 计算字符串a的每个后缀与字符串b的最长公共前缀长度
	 * 
	 * 时间复杂度：O(n + m)，其中n是a的长度，m是b的长度
	 * 空间复杂度：O(n + m)
	 * 
	 * @param a 字符串a
	 * @param b 字符串b
	 * @param n 字符串a的长度
	 * @param m 字符串b的长度
	 * @throws IllegalArgumentException 当输入参数无效时抛出
	 */
	// 非常像Manacher算法
	public static void eArray(char[] a, char[] b, int n, int m) {
		// 参数验证
		if (a == null || b == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		if (n < 0 || n > a.length || m < 0 || m > b.length) {
			throw new IllegalArgumentException("字符串长度无效");
		}
		
		// 处理边界情况
		if (n == 0 || m == 0) {
			return;
		}
		
		logDebug("开始计算E数组，a长度: " + n + ", b长度: " + m);
		
		// 使用局部变量减少数组访问开销
		final char[] localA = a;
		final char[] localB = b;
		final int[] localZ = z;
		final int[] localE = e;
		
		for (int i = 0, c = 0, r = 0, len; i < n; i++) {
			// 利用已计算的信息优化
			len = r > i ? Math.min(r - i, localZ[i - c]) : 0;
			
			// 继续向右扩展匹配
			while (i + len < n && len < m && localA[i + len] == localB[len]) {
				len++;
			}
			
			// 更新最右匹配区间
			if (i + len > r) {
				r = i + len;
				c = i;
			}
			
			localE[i] = len;
		}
		
		if (debugMode && n <= 1000) { // 避免大数据量时日志过多
			logDebug("E数组计算完成，前10个值: " + 
					Arrays.toString(Arrays.copyOfRange(e, 0, Math.min(n, 10))));
		}
	}

	/**
	 * 计算数组的权值异或和
	 * 对于数组中的每个元素arr[i]，权值为 (i+1) * (arr[i] + 1)
	 * 
	 * @param arr 输入数组
	 * @param n 数组长度
	 * @return 所有权值的异或和
	 * @throws IllegalArgumentException 当输入参数无效时抛出
	 */
	public static long eor(int[] arr, int n) {
		// 参数验证
		if (arr == null) {
			throw new IllegalArgumentException("输入数组不能为null");
		}
		if (n < 0 || n > arr.length) {
			throw new IllegalArgumentException("数组长度无效: " + n);
		}
		
		// 处理边界情况
		if (n == 0) {
			return 0;
		}
		
		long ans = 0;
		// 使用局部变量减少数组访问开销
		final int[] localArr = arr;
		
		for (int i = 0; i < n; i++) {
			ans ^= (long) (i + 1) * (localArr[i] + 1);
		}
		
		return ans;
	}
	
	/**
	 * LeetCode 2223. 构造字符串的总得分和
	 * 你需要从空字符串开始构造一个长度为n的字符串s，构造过程为每次给当前字符串前面添加一个字符。
	 * 构造过程中得到的所有字符串编号为1到n，其中长度为i的字符串编号为si。
	 * si的得分为si和sn的最长公共前缀的长度（注意s == sn）。
	 * 请你返回每一个si的得分之和。
	 * 
	 * 示例:
	 * 输入: s = "babab"
	 * 输出: 9
	 * 解释: 
	 * s1 == "b"，得分1
	 * s2 == "ab"，得分0
	 * s3 == "bab"，得分3
	 * s4 == "abab"，得分0
	 * s5 == "babab"，得分5
	 * 总和为1+0+3+0+5=9
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 * 
	 * @param s 输入字符串
	 * @return 得分总和
	 * @throws IllegalArgumentException 当输入参数无效时抛出
	 */
	public static long sumScores(String s) {
		// 参数验证
		if (s == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		logDebug("计算构造字符串的总得分和，输入: " + s);
		
		int n = s.length();
		char[] chars = s.toCharArray();
		
		// 计算Z函数
		int[] z = new int[n];
		z[0] = n;
		
		long sum = n; // s5的得分就是整个字符串的长度
		
		for (int i = 1, l = 0, r = 0; i < n; i++) {
			// 利用之前计算的结果
			if (i <= r) {
				z[i] = Math.min(r - i + 1, z[i - l]);
			}
			
			// 扩展匹配
			while (i + z[i] < n && chars[z[i]] == chars[i + z[i]]) {
				z[i]++;
			}
			
			// 更新匹配区间
			if (i + z[i] - 1 > r) {
				l = i;
				r = i + z[i] - 1;
			}
			
			// 累加得分
			sum += z[i];
		}
		
		logDebug("总得分和计算结果: " + sum);
		return sum;
	}
	
	/**
	 * LeetCode 3031. 将单词恢复初始状态所需的最短时间 II
	 * 给你一个下标从0开始的字符串word和一个整数k。
	 * 每一秒执行以下操作：
	 * 1. 移除word的前k个字符
	 * 2. 在word的末尾添加k个任意字符
	 * 返回将word恢复到初始状态所需的最短时间（该时间必须大于零）。
	 * 
	 * 示例:
	 * 输入: word = "abacaba", k = 3
	 * 输出: 2
	 * 解释:
	 * 第1秒后，word变成"acaba**"（用*表示添加的字符）
	 * 第2秒后，word变成"aba****"
	 * 如果添加的字符分别为"cac"和"caba"，word就恢复为"abacaba"
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 * 
	 * @param word 输入字符串
	 * @param k 每次操作移除和添加的字符数
	 * @return 恢复初始状态所需的最短时间
	 * @throws IllegalArgumentException 当输入参数无效时抛出
	 */
	public static int minimumTimeToInitialState(String word, int k) {
		// 参数验证
		if (word == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		if (k <= 0) {
			throw new IllegalArgumentException("k必须为正整数: " + k);
		}
		
		logDebug("计算恢复初始状态所需时间，word: " + word + ", k: " + k);
		
		int n = word.length();
		char[] chars = word.toCharArray();
		
		// 计算Z函数
		int[] z = new int[n];
		z[0] = n;
		
		for (int i = 1, l = 0, r = 0; i < n; i++) {
			if (i <= r) {
				z[i] = Math.min(r - i + 1, z[i - l]);
			}
			
			while (i + z[i] < n && chars[z[i]] == chars[i + z[i]]) {
				z[i]++;
			}
			
			if (i + z[i] - 1 > r) {
				l = i;
				r = i + z[i] - 1;
			}
		}
		
		// 查找满足条件的最小时间
		for (int i = k; i < n; i += k) {
			// 如果从位置i开始的后缀与原字符串的最长公共前缀长度等于后缀长度
			// 说明在第(i/k)步后可以恢复原字符串
			if (z[i] >= n - i) {
				int result = i / k;
				logDebug("找到最短时间: " + result);
				return result;
			}
		}
		
		// 最坏情况需要完全替换
		int worstCase = (n + k - 1) / k;
		logDebug("最坏情况时间: " + worstCase);
		return worstCase;
	}
	
	/**
	 * Codeforces 126B. Password
	 * 
	 * 题目描述：
	 * 给定一个字符串s，找出最长的子串t，它既是s的前缀，也是s的后缀，还在s的中间出现过。
	 * 如果存在这样的子串，输出最长的那个；否则输出"Just a legend"。
	 * 
	 * 解题思路：
	 * 使用Z函数（扩展KMP）算法解决此问题：
	 * 1. 计算字符串s的Z函数数组z，其中z[i]表示以位置i开始的后缀与原字符串的最长公共前缀长度
	 * 2. 遍历z数组，找到既是前缀又是后缀的子串（即z[i] == n-i的情况）
	 * 3. 同时记录在中间出现过的前缀长度
	 * 4. 找到满足所有条件的最长子串
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 * 
	 * @param s 输入字符串
	 * @return 满足条件的最长子串，如果不存在则返回"Just a legend"
	 */
	public static String solvePassword(String s) {
		int n = s.length();
		if (n <= 2) return "Just a legend";
		
		// 计算Z函数
		int[] z = zFunction(s);
		
		// 记录在中间出现过的前缀长度
		boolean[] hasPrefix = new boolean[n + 1];
		
		// 标记在中间出现过的前缀长度
		for (int i = 1; i < n; i++) {
			if (z[i] > 0) {
				hasPrefix[z[i]] = true;
			}
		}
		
		// 查找既是前缀又是后缀且在中间出现过的最长子串
		int maxLen = 0;
		for (int i = 1; i < n; i++) {
			// 如果从位置i开始的后缀与原字符串的最长公共前缀长度等于后缀长度
			// 说明这个后缀与原字符串的前缀完全匹配
			if (z[i] == n - i && hasPrefix[z[i]]) {
				maxLen = Math.max(maxLen, z[i]);
			}
		}
		
		// 如果找到了满足条件的子串，返回它；否则返回"Just a legend"
		return maxLen > 0 ? s.substring(0, maxLen) : "Just a legend";
	}
	
	/**
	 * Z函数计算
	 * Z函数z[i]表示字符串s从位置i开始与字符串s从位置0开始的最长公共前缀长度
	 * 
	 * 算法原理：
	 * 1. 维护一个匹配区间[l, r]，表示当前已知的最右匹配区间
	 * 2. 对于当前位置i，如果i <= r，可以利用已计算的信息优化
	 * 3. 利用对称性，z[i]至少为min(r - i + 1, z[i - l])
	 * 4. 在此基础之上继续向右扩展匹配
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 * 
	 * @param s 输入字符串
	 * @return Z函数数组
	 */
	public static int[] zFunction(String s) {
		int n = s.length();
		int[] z = new int[n];
		z[0] = n;
		
		// l: 当前最右匹配区间的左边界
		// r: 当前最右匹配区间的右边界
		for (int i = 1, l = 0, r = 0; i < n; i++) {
			// 利用已计算的信息优化
			// 如果i在当前匹配区间内
			if (i <= r) {
				z[i] = Math.min(r - i + 1, z[i - l]);
			}
			
			// 继续向右扩展匹配
			while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) {
				z[i]++;
			}
			
			// 更新最右匹配区间
			if (i + z[i] - 1 > r) {
				l = i;
				r = i + z[i] - 1;
			}
		}
		
		return z;
	}
	
	/**
	 * 运行单元测试
	 */
	public static void runUnitTests() {
		logger.info("开始运行单元测试...");
		
		// 测试Z函数
		testZArray();
		
		// 测试LeetCode 2223
		testSumScores();
		
		// 测试LeetCode 3031
		testMinimumTimeToInitialState();
		
		// 测试Codeforces 126B
		testSolvePassword();
		
		logger.info("单元测试完成");
	}
	
	/**
	 * 测试Z数组计算
	 */
	private static void testZArray() {
		String[] testCases = {
			"aaaaa",
			"ababc",
			"babab",
			"abcdef"
		};
		
		int[][] expectedResults = {
			{5, 4, 3, 2, 1},
			{5, 0, 2, 0, 1},
			{5, 0, 3, 0, 1},
			{6, 0, 0, 0, 0, 0}
		};
		
		for (int i = 0; i < testCases.length; i++) {
			String s = testCases[i];
			char[] chars = s.toCharArray();
			int n = s.length();
			
			// 确保数组足够大
			if (n > MAXN) {
				MAXN = n;
				z = Arrays.copyOf(z, MAXN);
			}
			
			// 计算Z数组
			zArray(chars, n);
			
			// 验证结果
			boolean passed = true;
			for (int j = 0; j < n; j++) {
				if (z[j] != expectedResults[i][j]) {
					passed = false;
					logger.warning("Z数组测试失败: s=" + s + ", 位置=" + j + ", 期望=" + expectedResults[i][j] + ", 实际=" + z[j]);
					break;
				}
			}
			
			if (passed) {
				logger.info("Z数组测试通过: s=" + s);
			}
		}
	}
	
	/**
	 * 测试LeetCode 2223
	 */
	private static void testSumScores() {
		String[] testCases = {"babab", "azbazbzaz"};
		long[] expectedResults = {9, 14};
		
		for (int i = 0; i < testCases.length; i++) {
			long result = sumScores(testCases[i]);
			if (result == expectedResults[i]) {
				logger.info("sumScores测试通过: s=" + testCases[i] + ", 结果=" + result);
			} else {
				logger.warning("sumScores测试失败: s=" + testCases[i] + ", 期望=" + expectedResults[i] + ", 实际=" + result);
			}
		}
	}
	
	/**
	 * 测试LeetCode 3031
	 */
	private static void testMinimumTimeToInitialState() {
		String[] words = {"abacaba", "abacaba", "abcdef"};
		int[] ks = {3, 4, 2};
		int[] expectedResults = {2, 1, 3};
		
		for (int i = 0; i < words.length; i++) {
			int result = minimumTimeToInitialState(words[i], ks[i]);
			if (result == expectedResults[i]) {
				logger.info("minimumTimeToInitialState测试通过: word=" + words[i] + ", k=" + ks[i] + ", 结果=" + result);
			} else {
				logger.warning("minimumTimeToInitialState测试失败: word=" + words[i] + ", k=" + ks[i] + ", 期望=" + expectedResults[i] + ", 实际=" + result);
			}
		}
	}
	
	/**
	 * 测试Codeforces 126B
	 */
	private static void testSolvePassword() {
		String[] testCases = {"fixprefixsuffix", "abcdabc", "abcab"};
		String[] expectedResults = {"fix", "Just a legend", "ab"};
		
		for (int i = 0; i < testCases.length; i++) {
			String result = solvePassword(testCases[i]);
			if (result.equals(expectedResults[i])) {
				logger.info("solvePassword测试通过: s=" + testCases[i] + ", 结果=" + result);
			} else {
				logger.warning("solvePassword测试失败: s=" + testCases[i] + ", 期望=" + expectedResults[i] + ", 实际=" + result);
			}
		}
	}
}