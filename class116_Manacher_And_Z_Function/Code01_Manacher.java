package class103;

// Manacher算法模版 - 高效解决回文子串问题
// 求字符串s中最长回文子串的长度
// 测试链接 : https://www.luogu.com.cn/problem/P3805
// 提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Manacher算法的全面实现类
 * 提供高效的回文子串检测、计数和处理功能
 * 包含多个算法题目的最优解实现
 * 时间复杂度：O(n) - 线性时间复杂度，每个字符最多被访问两次
 * 空间复杂度：O(n) - 需要预处理和辅助数组
 * 
 * 算法应用场景：
 * 1. LeetCode 5. 最长回文子串 - https://leetcode.com/problems/longest-palindromic-substring/
 * 2. LeetCode 647. 回文子串 - https://leetcode.com/problems/palindromic-substrings/
 * 3. LeetCode 214. 最短回文串 - https://leetcode.com/problems/shortest-palindrome/
 * 4. 洛谷 P3805 【模板】manacher - https://www.luogu.com.cn/problem/P3805
 * 5. UVa 11475 - Extend to Palindrome - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2470
 * 6. Codeforces 1326D2 - Prefix-Suffix Palindrome - https://codeforces.com/problemset/problem/1326/D2
 * 7. HackerRank - Palindromic Substrings
 * 8. AcWing 141. 周期 - https://www.acwing.com/problem/content/143/
 * 9. POJ 3240 - 回文串
 * 10. LeetCode 336. 回文对 - https://leetcode.com/problems/palindrome-pairs/
 * 11. LeetCode 131. 分割回文串 - https://leetcode.com/problems/palindrome-partitioning/
 * 12. LeetCode 132. 分割回文串 II - https://leetcode.com/problems/palindrome-partitioning-ii/
 */
public class Code01_Manacher {
	// 日志记录器 - 用于调试和问题定位
	private static final Logger logger = Logger.getLogger(Code01_Manacher.class.getName());
	
	// 性能监控开关
	private static boolean performanceMonitoring = false;
	
	// 最大字符串长度常量 - 预分配内存以提高性能
	public static int MAXN = 11000001;

	// 预处理后的字符串数组
	public static char[] ss = new char[MAXN << 1];

	// 回文半径数组
	public static int[] p = new int[MAXN << 1];

	// 当前处理的字符串长度
	public static int n;

	/**
	 * 主方法 - 用于在线评测系统
	 * 使用高效的输入输出方式以处理大规模数据
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 高效IO处理 - 处理大规模输入时性能优化
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取输入并处理
		out.println(manacher(in.readLine()));
		
		// 确保所有输出被刷新
		out.flush();
		out.close();
		in.close();
	}
	
	/**
	 * 增强版主方法 - 用于本地测试和调试
	 * 提供更丰富的测试功能和调试信息
	 * @param args 命令行参数
	 */
	public static void enhancedMain(String[] args) {
		testAllFunctionality();
	}
	
	/**
	 * 全面功能测试方法
	 * 测试所有实现的算法功能
	 */
	public static void testAllFunctionality() {
		System.out.println("===== Manacher算法功能测试 =====");
		
		// 测试用例集
		String[] testCases = {
			"abc12321cba",  // 包含回文的字符串
			"",             // 空字符串
			"a",            // 单个字符
			"aaaaa",        // 全相同字符
			"abcdefg"       // 无回文
		};
		
		// 测试最长回文子串
		System.out.println("\n1. 最长回文子串测试:");
		for (String s : testCases) {
			System.out.println("输入: \"" + s + "\"");
			System.out.println("结果: \"" + longestPalindrome(s) + "\"");
		}
		
		// 测试回文子串计数
		System.out.println("\n2. 回文子串计数测试:");
		for (String s : testCases) {
			System.out.println("输入: \"" + s + "\"");
			System.out.println("结果: " + countSubstrings(s));
		}
		
		// 测试最短回文串
		System.out.println("\n3. 最短回文串测试:");
		String[] shortestTestCases = {"aacecaaa", "abcd", ""};
		for (String s : shortestTestCases) {
			System.out.println("输入: \"" + s + "\"");
			System.out.println("结果: \"" + shortestPalindrome(s) + "\"");
		}
		
		System.out.println("\n===== 测试完成 =====");
	}

	/**
	 * Manacher算法主函数，用于计算字符串中最长回文子串的长度
	 * 这是算法竞赛中常用的优化实现版本
	 * 
	 * 算法核心原理：
	 * 1. 预处理：在原字符串的每个字符之间插入特殊字符'#'，并在首尾也添加'#'
	 *    这样可以将奇数长度和偶数长度的回文串统一处理为奇数长度的回文串
	 * 2. 利用回文串的对称性，避免重复计算 - 这是算法线性时间复杂度的关键
	 * 3. 维护当前最右回文边界r和对应的中心c，通过已计算的信息加速新位置的计算
	 * 
	 * 时间复杂度证明：
	 * - 每个字符最多被访问两次：第一次作为中心扩展，第二次作为右侧字符被检查
	 * - 虽然有嵌套循环，但内层循环的总执行次数被右边界r的单调增长所限制
	 * - 因此整体时间复杂度为O(n)
	 * 
	 * 空间复杂度：O(n)，需要预处理字符串数组和回文半径数组
	 * 
	 * @param str 输入字符串
	 * @return 最长回文子串的长度
	 * @throws IllegalArgumentException 如果输入为null
	 */
	public static int manacher(String str) {
		// 参数验证 - 防御性编程
		if (str == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		// 边界情况快速处理
		if (str.isEmpty()) {
			return 0;
		}
		
		long startTime = 0;
		if (performanceMonitoring) {
			startTime = System.nanoTime();
		}
		
		// 预处理字符串
		manacherss(str.toCharArray());
		
		int max = 0;
		// c: 当前最右回文子串的中心
		// r: 当前最右回文子串的右边界
		// len: 当前位置i为中心的回文半径
		for (int i = 0, c = 0, r = 0, len; i < n; i++) {
			// 调试辅助 - 记录中间变量状态
			if (logger.isLoggable(Level.FINER)) {
				logger.finer("i=" + i + ", c=" + c + ", r=" + r);
			}
			
			// 算法核心优化步骤：利用回文对称性减少重复计算
			// 有三种情况：
			// 1. i在r外：无法利用对称性，初始半径为1
			// 2. i在r内且对称点的回文完全在c的回文内：直接使用对称点的回文半径
			// 3. i在r内但对称点的回文部分超出c的回文：使用r-i作为初始半径
			len = r > i ? Math.min(p[2 * c - i], r - i) : 1;
			
			// 断言验证 - 确保len的有效性（调试时使用）
			assert len > 0 : "回文半径必须为正整数";
			
			// 尝试扩展回文串
			// 从当前半径开始，尝试向两边扩展
			while (i + len < n && i - len >= 0 && ss[i + len] == ss[i - len]) {
				len++;
				// 性能监控点 - 极端情况下可以监控扩展次数
				if (performanceMonitoring && len > 10000) {
					logger.info("大规模回文扩展：" + len);
				}
			}
			
			// 更新最右回文边界和中心
			// 这里体现了算法的贪心策略：总是维护最右的回文边界
			if (i + len > r) {
				r = i + len;
				c = i;
			}
			
			// 更新最大回文半径
			if (len > max) {
				max = len;
				// 记录最长回文的位置信息（可选）
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("找到更长回文，中心位置:" + i + ", 半径:" + len);
				}
			}
			
			// 保存当前位置的回文半径
			p[i] = len;
		}
		
		// 性能统计
		if (performanceMonitoring) {
			long endTime = System.nanoTime();
			logger.info("Manacher算法执行时间: " + (endTime - startTime) + " 纳秒");
		}
		
		// 由于我们插入了'#'字符，实际回文长度是半径减1
		return max - 1;
	}

	/**
	 * 预处理函数，将原字符串转换为插入'#'的格式
	 * 例如："abc" -> "#a#b#c#"
	 * 
	 * 预处理的目的：
	 * 1. 统一处理奇数长度和偶数长度的回文串
	 * 2. 避免处理边界情况时的数组越界检查
	 * 3. 确保每个回文串的中心都是一个字符，而不是字符之间的间隙
	 * 
	 * @param a 原字符串的字符数组
	 * @throws IllegalArgumentException 如果输入为null
	 */
	public static void manacherss(char[] a) {
		// 参数验证
		if (a == null) {
			throw new IllegalArgumentException("输入字符数组不能为null");
		}
		
		// 计算预处理后的字符串长度
		// 原长度n -> 2n+1（每个字符间插入'#'）
		n = a.length * 2 + 1;
		
		// 边界检查 - 避免数组越界
		if (n > ss.length) {
			// 如果预分配的数组不够大，需要重新分配
			// 注意：在实际竞赛中通常预分配足够大的空间
			ss = new char[n];
			p = new int[n];
		}
		
		// 填充预处理后的字符串
		// 使用位运算 (i & 1) 比取模运算 (i % 2) 效率更高
		for (int i = 0, j = 0; i < n; i++) {
			// 偶数位置放'#'，奇数位置放原字符
			ss[i] = (i & 1) == 0 ? '#' : a[j++];
		}
		
		// 调试信息
		if (logger.isLoggable(Level.FINER)) {
			logger.finer("预处理后的字符串: " + new String(ss, 0, n));
		}
	}
	
	/**
	 * 设置性能监控开关
	 * @param enabled 是否启用性能监控
	 */
	public static void setPerformanceMonitoring(boolean enabled) {
		performanceMonitoring = enabled;
	}
	
	/**
	 * 调试辅助方法：打印算法执行的详细过程
	 * 用于算法学习和问题定位
	 * 
	 * @param str 输入字符串
	 */
	public static void debugProcess(String str) {
		System.out.println("\n===== Manacher算法执行过程调试 =====");
		System.out.println("原始字符串: " + str);
		
		// 预处理字符串
		char[] a = str.toCharArray();
		manacherss(a);
		System.out.println("预处理后: " + new String(ss, 0, n));
		
		// 重置p数组
		Arrays.fill(p, 0);
		
		int max = 0;
		for (int i = 0, c = 0, r = 0, len; i < n; i++) {
			System.out.println("\n处理位置 i = " + i + ", 字符 = '" + ss[i] + "'");
			System.out.println("当前状态: c = " + c + ", r = " + r);
			
			len = r > i ? Math.min(p[2 * c - i], r - i) : 1;
			System.out.println("初始len = " + len + " (基于对称性优化)");
			
			while (i + len < n && i - len >= 0 && ss[i + len] == ss[i - len]) {
				len++;
				System.out.println("扩展len到 " + len);
			}
			
			if (i + len > r) {
				r = i + len;
				c = i;
				System.out.println("更新r = " + r + ", c = " + c);
			}
			
			if (len > max) {
				max = len;
				System.out.println("更新最大回文半径: " + max);
			}
			
			p[i] = len;
			System.out.println("p[" + i + "] = " + len);
		}
		
		System.out.println("\n最终最大回文长度: " + (max - 1));
		System.out.println("===== 调试结束 =====");
	}
	
	/**
	 * LeetCode 5. 最长回文子串
	 * 给你一个字符串 s，找到 s 中最长的回文子串
	 * 
	 * 示例 1:
	 * 输入: s = "babad"
	 * 输出: "bab" 或 "aba"
	 * 
	 * 示例 2:
	 * 输入: s = "cbbd"
	 * 输出: "bb"
	 * 
	 * 算法分析：
	 * - 时间复杂度: O(n) - Manacher算法的线性时间复杂度
	 * - 空间复杂度: O(n) - 需要存储预处理字符串和回文半径数组
	 * 
	 * 解题思路：
	 * 1. 预处理字符串，插入特殊字符统一处理奇偶数长度回文
	 * 2. 使用Manacher算法计算每个位置的回文半径
	 * 3. 找到最长回文的中心位置和半径
	 * 4. 将处理后的位置映射回原字符串，提取对应的子串
	 * 
	 * 优化点：
	 * - 使用try-catch处理边界情况，提高鲁棒性
	 * - 预处理时添加^和$边界符，避免额外的边界检查
	 * 
	 * @param s 输入字符串
	 * @return 最长回文子串
	 * @throws IllegalArgumentException 如果输入为null
	 */
	public static String longestPalindrome(String s) {
		// 参数验证
		if (s == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		// 边界情况快速处理
		if (s.length() <= 1) {
			return s;
		}
		
		// 使用Manacher算法处理
		String processed = preprocess(s);
		char[] chars = processed.toCharArray();
		int len = chars.length;
		int[] radius = new int[len];
		
		int center = 0, right = 0;
		int maxLen = 0, centerIndex = 0;
		
		for (int i = 0; i < len; i++) {
			// 算法核心：利用回文对称性减少重复计算
			int mirror = 2 * center - i;
			
			if (i < right) {
				// 三种情况的统一处理：
				// 1. 对称点的回文完全在当前回文内部
				// 2. 对称点的回文部分超出当前回文
				// 3. 对称点的回文恰好到达当前回文边界
				radius[i] = Math.min(right - i, radius[mirror]);
			}
			
			// 尝试扩展回文
			// 使用try-catch处理边界情况，比每次检查更高效
			try {
				while (i - radius[i] - 1 >= 0 && 
				       i + radius[i] + 1 < len && 
				       chars[i - radius[i] - 1] == chars[i + radius[i] + 1]) {
					radius[i]++;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				// 边界情况处理 - 当数组越界时停止扩展
				// 在生产环境中，应该记录异常信息
				if (logger.isLoggable(Level.FINEST)) {
					logger.finest("边界扩展异常: " + e.getMessage());
				}
			}
			
			// 更新最右边界和中心
			if (i + radius[i] > right) {
				center = i;
				right = i + radius[i];
			}
			
			// 更新最长回文信息
			if (radius[i] > maxLen) {
				maxLen = radius[i];
				centerIndex = i;
			}
		}
		
		// 从处理后的字符串中提取原始回文子串
		// 映射公式：原始起始位置 = (处理后的中心 - 最大半径) / 2
		int start = (centerIndex - maxLen) / 2;
		
		// 安全检查 - 确保索引有效
		start = Math.max(0, start);
		int end = Math.min(s.length(), start + maxLen);
		
		String result = s.substring(start, end);
		
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("最长回文子串: " + result + ", 长度: " + result.length());
		}
		
		return result;
	}
	
	/**
	 * LeetCode 647. 回文子串
	 * 给定一个字符串，计算其中回文子串的数目
	 * 
	 * 示例:
	 * 输入: "abc" -> 输出: 3 ("a", "b", "c")
	 * 输入: "aaa" -> 输出: 6 ("a", "a", "a", "aa", "aa", "aaa")
	 * 
	 * 算法分析：
	 * - 时间复杂度: O(n) - Manacher算法的线性时间复杂度
	 * - 空间复杂度: O(n) - 需要存储预处理字符串和回文半径数组
	 * 
	 * 数学原理：
	 * 对于处理后的字符串中的每个位置i，回文半径为r：
	 * - 如果i是奇数位置（对应原字符串的字符），则贡献 r 个回文子串
	 * - 如果i是偶数位置（对应插入的'#'），则贡献 r 个回文子串
	 * 通过公式 (radius[i] + 1) / 2 可以统一计算贡献数量
	 * 
	 * 优化点：
	 * - 利用预处理和Manacher算法避免了暴力枚举的O(n²)时间复杂度
	 * - 使用异常处理简化边界检查逻辑
	 * 
	 * @param s 输入字符串
	 * @return 回文子串的数目
	 * @throws IllegalArgumentException 如果输入为null
	 */
	public static int countSubstrings(String s) {
		// 参数验证
		if (s == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		// 边界情况快速处理
		if (s.isEmpty()) {
			return 0;
		}
		if (s.length() == 1) {
			return 1;
		}
		
		// 预处理字符串
		String processed = preprocess(s);
		char[] chars = processed.toCharArray();
		int len = chars.length;
		int[] radius = new int[len];
		
		int center = 0, right = 0;
		int count = 0;
		
		for (int i = 0; i < len; i++) {
			// 利用回文对称性优化
			int mirror = 2 * center - i;
			
			if (i < right) {
				radius[i] = Math.min(right - i, radius[mirror]);
			}
			
			// 扩展回文
			try {
				while (i - radius[i] - 1 >= 0 && 
				       i + radius[i] + 1 < len && 
				       chars[i - radius[i] - 1] == chars[i + radius[i] + 1]) {
					radius[i]++;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				// 边界处理
			}
			
			// 更新右边界
			if (i + radius[i] > right) {
				center = i;
				right = i + radius[i];
			}
			
			// 计算当前位置贡献的回文子串数量
			// 数学公式：(radius[i] + 1) / 2
			// 例如：radius=3时，贡献2个回文子串
			//       radius=4时，贡献2个回文子串
			int contribution = (radius[i] + 1) / 2;
			count += contribution;
			
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("位置i=" + i + ", 半径=" + radius[i] + ", 贡献=" + contribution);
			}
		}
		
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("字符串: " + s + ", 回文子串总数: " + count);
		}
		
		return count;
	}
	
	/**
	 * LeetCode 214. 最短回文串
	 * 给定一个字符串 s，可以通过在字符串前面添加字符将其转换为回文串。
	 * 找到并返回可以用这种方式转换的最短回文串。
	 * 
	 * 示例:
	 * 输入: "aacecaaa" -> 输出: "aaacecaaa"
	 * 输入: "abcd" -> 输出: "dcbabcd"
	 * 
	 * 算法分析：
	 * - 时间复杂度: O(n) - KMP算法的线性时间复杂度
	 * - 空间复杂度: O(n) - 需要存储组合字符串和LPS数组
	 * 
	 * 解题思路（KMP方法）：
	 * 1. 问题转化：找到s的最长前缀，该前缀也是s的后缀
	 * 2. 构造字符串：s + "#" + reverse(s)，使用'#'确保匹配不会跨边界
	 * 3. 计算LPS数组：找到最长公共前后缀
	 * 4. 构建结果：将未匹配部分反转后添加到原字符串前
	 * 
	 * 为什么KMP比Manacher更适合此题：
	 * - 题目要求的是前缀回文，而不是任意位置的回文
	 * - KMP算法能更直接地找到字符串的前缀后缀匹配关系
	 * - 实现更简洁，且时间复杂度同样为O(n)
	 * 
	 * @param s 输入字符串
	 * @return 最短回文串
	 * @throws IllegalArgumentException 如果输入为null
	 */
	public static String shortestPalindrome(String s) {
		// 参数验证
		if (s == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		// 边界情况快速处理
		if (s.length() <= 1) {
			return s;
		}
		
		// 核心思路：找到s的最长前缀，该前缀也是s的后缀
		// 构造组合字符串：原字符串 + 特殊字符 + 反转后的字符串
		// 使用'#'作为分隔符，确保匹配不会跨过原字符串和反转字符串的边界
		String reversed = new StringBuilder(s).reverse().toString();
		String combined = s + "#" + reversed;
		
		// 计算LPS数组
		int[] lps = computeLPS(combined);
		
		// LPS数组的最后一个元素表示原字符串的最长前缀回文长度
		int maxPrefixPalindromeLen = lps[combined.length() - 1];
		
		// 构建最短回文串：
		// 1. 取出原字符串中不属于最长前缀回文的部分
		// 2. 反转这部分
		// 3. 添加到原字符串前面
		String suffixToReverse = s.substring(maxPrefixPalindromeLen);
		String reversedPrefix = new StringBuilder(suffixToReverse).reverse().toString();
		String result = reversedPrefix + s;
		
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("原始字符串: " + s);
			logger.fine("最长前缀回文长度: " + maxPrefixPalindromeLen);
			logger.fine("最短回文串: " + result);
		}
		
		return result;
	}
	
	/**
	 * KMP算法中的LPS数组计算（最长公共前后缀数组）
	 * LPS[i]表示字符串pattern[0...i]的最长公共前后缀长度
	 * 
	 * 算法原理：
	 * - i从1开始遍历字符串
	 * - j表示当前已匹配的前缀长度
	 * - 当字符匹配时，j增加并记录到lps[i]
	 * - 当字符不匹配时，回退j到lps[j-1]
	 * 
	 * 时间复杂度：O(m)，其中m为模式串长度
	 * 
	 * @param pattern 模式字符串
	 * @return LPS数组
	 * @throws IllegalArgumentException 如果输入为null
	 */
	private static int[] computeLPS(String pattern) {
		// 参数验证
		if (pattern == null) {
			throw new IllegalArgumentException("模式字符串不能为null");
		}
		
		int len = pattern.length();
		int[] lps = new int[len];
		
		// 边界情况处理
		if (len == 0) {
			return lps;
		}
		
		// i: 当前处理的位置
		// j: 当前已匹配的前缀长度
		int i = 1, j = 0;
		
		while (i < len) {
			if (pattern.charAt(i) == pattern.charAt(j)) {
				// 字符匹配，增加已匹配长度并记录
				lps[i++] = ++j;
			} else {
				// 字符不匹配
				if (j != 0) {
					// 回退到前一个可能的匹配位置
					j = lps[j - 1];
				} else {
					// 无法回退，当前位置LPS值为0
					lps[i++] = 0;
				}
			}
		}
		
		if (logger.isLoggable(Level.FINER)) {
			logger.finest("模式串: " + pattern);
			logger.finest("LPS数组: " + Arrays.toString(lps));
		}
		
		return lps;
	}
	
	/**
	 * 预处理函数，用于在字符间插入'#'
	 * 同时在首尾添加^和$作为边界标记
	 * 
	 * 预处理格式："abc" -> "^#a#b#c#$"
	 * 
	 * 预处理的优点：
	 * 1. 统一处理奇数长度和偶数长度的回文串
	 * 2. ^和$边界符避免了扩展时的边界检查
	 * 3. 确保回文扩展不会越界
	 * 
	 * @param s 原始字符串
	 * @return 预处理后的字符串
	 * @throws IllegalArgumentException 如果输入为null
	 */
	private static String preprocess(String s) {
		// 参数验证
		if (s == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		int n = s.length();
		if (n == 0) {
			return "^$";
		}
		
		// 使用StringBuilder提高字符串拼接效率
		StringBuilder sb = new StringBuilder(2 * n + 3); // 预分配足够空间
		sb.append("^"); // 开头边界符
		
		for (int i = 0; i < n; i++) {
			sb.append("#").append(s.charAt(i));
		}
		
		sb.append("#$").append("$"); // 结尾边界符
		
		return sb.toString();
	}
	
	/**
	 * 洛谷 P3805 【模板】manacher
	 * 题目描述：给出一个只由小写英文字符 a,b,c...y,z 组成的字符串 S ,
	 * 求 S 中最长回文串的长度 。
	 * 
	 * 输入格式：一行小写英文字符 a,b,c,⋯,y,z 组成的字符串 S
	 * 输出格式：一个整数表示答案
	 * 
	 * 时间复杂度: O(n) - 线性时间算法
	 * 空间复杂度: O(n) - 需要O(n)的辅助空间
	 * 
	 * 解题思路：
	 * 1. 直接使用Manacher算法模板
	 * 2. 返回计算得到的最长回文子串长度
	 * 
	 * @param s 输入字符串
	 * @return 最长回文子串的长度
	 * @throws IllegalArgumentException 如果输入为null
	 */
	public static int longestPalindromeLength(String s) {
		return manacher(s);
	}
	
	/**
	 * 单元测试方法
	 * 用于验证所有算法功能的正确性
	 * 支持自动化测试和持续集成
	 * 
	 * @return 测试是否全部通过
	 */
	public static boolean runUnitTests() {
		boolean allPassed = true;
		
		// 1. 测试最长回文子串
		System.out.println("\n===== 最长回文子串测试 =====");
		String[][] palindromeTests = {
			{"babad", "bab"},       // 奇数长度回文，有多个解
			{"cbbd", "bb"},         // 偶数长度回文
			{"a", "a"},             // 单个字符
			{"", ""},                // 空字符串
			{"aaaaa", "aaaaa"}      // 全相同字符
		};
		
		for (int i = 0; i < palindromeTests.length; i++) {
			String input = palindromeTests[i][0];
			String expected = palindromeTests[i][1];
			String result = longestPalindrome(input);
			boolean passed = result.equals(expected) || 
			               (expected.equals("bab") && result.equals("aba")); // 处理多个正确答案
			
			System.out.println("测试 " + (i + 1) + ": " + (passed ? "通过" : "失败"));
			if (!passed) allPassed = false;
		}
		
		// 2. 测试回文子串计数
		System.out.println("\n===== 回文子串计数测试 =====");
		Object[][] countTests = {
			{"abc", 3},       // 基本测试
			{"aaa", 6},       // 全相同字符
			{"a", 1},         // 单个字符
			{"", 0}           // 空字符串
		};
		
		for (int i = 0; i < countTests.length; i++) {
			String input = (String) countTests[i][0];
			int expected = (int) countTests[i][1];
			int result = countSubstrings(input);
			boolean passed = result == expected;
			
			System.out.println("测试 " + (i + 1) + ": " + (passed ? "通过" : "失败"));
			if (!passed) allPassed = false;
		}
		
		// 3. 测试最短回文串
		System.out.println("\n===== 最短回文串测试 =====");
		String[][] shortestTests = {
			{"aacecaaa", "aaacecaaa"}, // 有部分前缀回文
			{"abcd", "dcbabcd"},      // 无前缀回文
			{"a", "a"},               // 单个字符
			{"", ""}                   // 空字符串
		};
		
		for (int i = 0; i < shortestTests.length; i++) {
			String input = shortestTests[i][0];
			String expected = shortestTests[i][1];
			String result = shortestPalindrome(input);
			boolean passed = result.equals(expected);
			
			System.out.println("测试 " + (i + 1) + ": " + (passed ? "通过" : "失败"));
			if (!passed) allPassed = false;
		}
		
		System.out.println("\n单元测试结果: " + (allPassed ? "全部通过" : "存在失败"));
		return allPassed;
	}
}