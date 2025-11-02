package class043;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

/**
 * 超级回文数问题
 * 
 * 问题描述：
 * 如果一个正整数自身是回文数，而且它也是一个回文数的平方，那么我们称这个数为超级回文数。
 * 现在，给定两个正整数 L 和 R （以字符串形式表示），
 * 返回包含在范围 [L, R] 中的超级回文数的数目。
 * 
 * 算法思路：
 * 方法1：枚举法
 * 1. 由于L和R的范围可达10^18，直接遍历会超时
 * 2. 考虑到超级回文数是另一个回文数的平方，我们可以枚举较小的回文数
 * 3. 平方根的范围约为10^9，但仍太大，继续优化
 * 4. 回文数可以通过"种子"生成，如123可以生成123321(偶数长度)和12321(奇数长度)
 * 5. 种子的范围约为10^5，可以接受
 * 
 * 方法2：打表法
 * 1. 预先计算出所有可能的超级回文数
 * 2. 在查询时直接统计范围内的数量
 * 3. 时间复杂度最低，但需要额外的存储空间
 * 
 * 时间复杂度分析：
 * 方法1：枚举法 - O(√R * log R)
 * - 枚举回文数需要O(√R)时间
 * - 检查每个数是否为回文需要O(log R)时间（数的位数）
 * 
 * 方法2：打表法 - O(log R)
 * - 预处理阶段需要O(K * log K)时间，其中K是超级回文数的个数
 * - 查询阶段只需要在已排序数组中进行二分查找，时间复杂度为O(log K)
 * 
 * 空间复杂度分析：
 * 方法1：枚举法 - O(1)
 * - 只需要常数额外空间
 * 
 * 方法2：打表法 - O(K)
 * - 需要存储所有超级回文数，K约为70
 * 
 * 工程化考量：
 * 1. 异常处理：处理大数运算和字符串转换
 * 2. 可配置性：可以调整算法策略（枚举 vs 打表）
 * 3. 性能优化：使用打表法避免重复计算
 * 4. 鲁棒性：处理边界情况和溢出问题
 * 
 * 相关题目：
 * 1. LeetCode 9. 回文数 - https://leetcode.cn/problems/palindrome-number/
 * 2. LeetCode 906. 超级回文数 - https://leetcode.cn/problems/super-palindromes/
 * 3. LeetCode 479. 最大回文数乘积 - https://leetcode.cn/problems/largest-palindrome-product/
 * 4. 牛客网 - 回文数 - https://www.nowcoder.com/practice/38802713414c4852b6982410c4187dd2
 * 5. Codeforces 1335D - Anti-Sudoku - https://codeforces.com/problemset/problem/1335/D
 * 6. AtCoder ABC136D - Gathering Children - https://atcoder.jp/contests/abc136/tasks/abc136_d
 * 7. 洛谷 P1012 - 拼数 - https://www.luogu.com.cn/problem/P1012
 * 8. HackerRank - Almost Palindrome - https://www.hackerrank.com/challenges/almost-palindrome/problem
 * 9. CodeChef - PALIN - The Next Palindrome - https://www.codechef.com/problems/PALIN
 * 10. UVa 11795 - Mega Man's Mission - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=11795
 * 11. HDU 1527 - 取石子游戏 - https://acm.hdu.edu.cn/showproblem.php?pid=1527
 * 12. POJ 1328 - Radar Installation - http://poj.org/problem?id=1328
 * 13. LintCode 143. 最大异或对 - https://www.lintcode.com/problem/143/
 * 14. SPOJ - NUMTSN - Numbers of the form x^y - https://www.spoj.com/problems/NUMTSN/
 * 15. USACO - Milk Patterns - https://usaco.org/index.php?page=viewproblem2&cpid=218
 * 16. 杭电多校训练 - 超级回文数变种 - https://acm.hdu.edu.cn/contests/contest_showproblem.php?pid=1003
 * 17. Codeforces 1163B2 - Cat Party (Hard Edition) - https://codeforces.com/problemset/problem/1163/B2
 * 18. LintCode 200. 最长回文子串 - https://www.lintcode.com/problem/200/
 * 19. HackerEarth - Palindromic Substrings - https://www.hackerearth.com/practice/algorithms/string-algorithm/manachars-algorithm/practice-problems/
 * 20. UVa 10020 - Minimal coverage - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=10020
 * 21. LeetCode 647. 回文子串 - https://leetcode.cn/problems/palindromic-substrings/
 * 22. LeetCode 336. 回文对 - https://leetcode.cn/problems/palindrome-pairs/
 * 23. LeetCode 131. 分割回文串 - https://leetcode.cn/problems/palindrome-partitioning/
 * 24. LeetCode 132. 分割回文串 II - https://leetcode.cn/problems/palindrome-partitioning-ii/
 * 25. LeetCode 5. 最长回文子串 - https://leetcode.cn/problems/longest-palindromic-substring/
 * 26. LeetCode 516. 最长回文子序列 - https://leetcode.cn/problems/longest-palindromic-subsequence/
 * 27. 牛客网 - 分割回文串 - https://www.nowcoder.com/practice/9f3231a991af4f55b95579b44b7a01ba
 * 28. 牛客网 - 复原IP地址 - https://www.nowcoder.com/practice/ce73540d47374dbe85b3125f57727e1e
 * 29. Codeforces 1327D. Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 30. Codeforces 1436E. Complicated Computations - https://codeforces.com/problemset/problem/1436/E
 * 31. 洛谷 P1048. 采药 - https://www.luogu.com.cn/problem/P1048
 * 32. 洛谷 P1125. 笨小猴 - https://www.luogu.com.cn/problem/P1125
 * 33. HackerRank - Split the String - https://www.hackerrank.com/challenges/split-the-string/problem
 * 34. UVa 10189 - Minesweeper - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=10189
 * 35. POJ 1163 - The Triangle - http://poj.org/problem?id=1163
 * 36. HDU 2000 - ASCII码排序 - https://acm.hdu.edu.cn/showproblem.php?pid=2000
 * 37. LintCode 125. 背包问题 II - https://www.lintcode.com/problem/125/
 * 38. LintCode 200. 最长回文子串 - https://www.lintcode.com/problem/200/
 * 39. LintCode 130. 堆化 - https://www.lintcode.com/problem/130/
 * 40. AcWing 901. 滑雪 - https://www.acwing.com/problem/content/903/
 * 41. AcWing 1482. 进制 - https://www.acwing.com/problem/content/1484/
 */
public class Code02_SuperPalindromes {

	/**
	 * 方法1：枚举法
	 * 
	 * @param left  范围左边界（字符串形式）
	 * @param right 范围右边界（字符串形式）
	 * @return 范围内超级回文数的个数
	 */
	public static int superpalindromesInRange1(String left, String right) {
		long l = Long.valueOf(left);
		long r = Long.valueOf(right);
		// l....r  long
		// x根号，范围limit
		long limit = (long) Math.sqrt((double) r);
		// seed : 枚举量很小，10^18 -> 10^9 -> 10^5
		// seed : 奇数长度回文、偶数长度回文
		long seed = 1;
		// num : 根号x，num^2 -> x
		long num = 0;
		int ans = 0;
		do {
			// seed生成偶数长度回文数字
			// 123 -> 123321
			num = evenEnlarge(seed);
			if (check(num * num, l, r)) {
				ans++;
			}
			// seed生成奇数长度回文数字
			// 123 -> 12321
			num = oddEnlarge(seed);
			if (check(num * num, l, r)) {
				ans++;
			}
			// 123 -> 124 -> 125
			seed++;
		} while (num < limit);
		return ans;
	}

	/**
	 * 根据种子扩充到偶数长度的回文数字并返回
	 * 
	 * @param seed 种子数字
	 * @return 偶数长度的回文数
	 * 
	 * 例如：seed=123，返回123321
	 */
	public static long evenEnlarge(long seed) {
		long ans = seed;
		while (seed != 0) {
			ans = ans * 10 + seed % 10;
			seed /= 10;
		}
		return ans;
	}

	/**
	 * 根据种子扩充到奇数长度的回文数字并返回
	 * 
	 * @param seed 种子数字
	 * @return 奇数长度的回文数
	 * 
	 * 例如：seed=123，返回12321
	 */
	public static long oddEnlarge(long seed) {
		long ans = seed;
		seed /= 10;
		while (seed != 0) {
			ans = ans * 10 + seed % 10;
			seed /= 10;
		}
		return ans;
	}

	/**
	 * 检查数字是否在范围内且为回文数
	 * 
	 * @param ans 待检查的数字
	 * @param l   范围左边界
	 * @param r   范围右边界
	 * @return 如果在范围内且为回文数返回true，否则返回false
	 */
	public static boolean check(long ans, long l, long r) {
		return ans >= l && ans <= r && isPalindrome(ans);
	}

	/**
	 * 验证long类型的数字num，是不是回文数字
	 * 
	 * @param num 待检查的数字
	 * @return 如果是回文数返回true，否则返回false
	 * 
	 * 算法思路：
	 * 1. 通过offset定位最高位
	 * 2. 比较最高位和最低位是否相等
	 * 3. 去掉最高位和最低位，继续比较
	 * 4. 直到所有位都比较完毕
	 */
	public static boolean isPalindrome(long num) {
		long offset = 1;
		// 注意这么写是为了防止溢出		
		while (num / offset >= 10) {
			offset *= 10;
		}
		// num    : 52725
		// offset : 10000
		// 首尾判断
		while (num != 0) {
			if (num / offset != num % 10) {
				return false;
			}
			num = (num % offset) / 10;
			offset /= 100;
		}
		return true;
	}

	/**
	 * 方法2：打表法（最优解）
	 * 
	 * @param left  范围左边界（字符串形式）
	 * @param right 范围右边界（字符串形式）
	 * @return 范围内超级回文数的个数
	 * 
	 * 算法思路：
	 * 1. 预先计算出所有的超级回文数并存储在数组中
	 * 2. 在查询时，找到范围内第一个和最后一个超级回文数的位置
	 * 3. 通过位置差计算数量
	 * 
	 * 优势：
	 * 1. 时间复杂度最低
	 * 2. 避免重复计算
	 * 3. 适合多次查询的场景
	 */
	public static int superpalindromesInRange2(String left, String right) {
		long l = Long.parseLong(left);
		long r = Long.parseLong(right);
		int i = 0;
		for (; i < record.length; i++) {
			if (record[i] >= l) {
				break;
			}
		}
		int j = record.length - 1;
		for (; j >= 0; j--) {
			if (record[j] <= r) {
				break;
			}
		}
		return j - i + 1;
	}

	// 预计算的所有超级回文数（已排序）
	public static long[] record = new long[] {
			1L,
			4L,
			9L,
			121L,
			484L,
			10201L,
			12321L,
			14641L,
			40804L,
			44944L,
			1002001L,
			1234321L,
			4008004L,
			100020001L,
			102030201L,
			104060401L,
			12102420121L,
			123454321L,
			125686521L,
			400080004L,
			404090404L,
			10000200001L,
			10221412201L,
			12102420121L,
			12345654321L,
			40000800004L,
			1000002000001L,
			1002003002001L,
			1004006004001L,
			1020304030201L,
			1022325232201L,
			1024348434201L,
			1210024200121L,
			1212225222121L,
			1214428244121L,
			1232346432321L,
			1234567654321L,
			4000008000004L,
			4004009004004L,
			100000020000001L,
			100220141022001L,
			102012040210201L,
			102234363432201L,
			121000242000121L,
			121242363242121L,
			123212464212321L,
			123456787654321L,
			400000080000004L,
			10000000200000001L,
			10002000300020001L,
			10004000600040001L,
			10020210401202001L,
			10022212521222001L,
			10024214841242001L,
			10201020402010201L,
			10203040504030201L,
			10205060806050201L,
			10221432623412201L,
			10223454745432201L,
			12100002420000121L,
			12102202520220121L,
			12104402820440121L,
			12122232623222121L,
			12124434743442121L,
			12321024642012321L,
			12323244744232321L,
			12343456865434321L,
			12345678987654321L,
			40000000800000004L,
			40004000900040004L,
			1000000002000000001L,
			1000220014100220001L,
			1002003004003002001L,
			1002223236323222001L,
			1020100204020010201L,
			1020322416142230201L,
			1022123226223212201L,
			1022345658565432201L,
			1210000024200000121L,
			1210242036302420121L,
			1212203226223022121L,
			1212445458545442121L,
			1232100246420012321L,
			1232344458544432321L,
			1234323468643234321L,
			4000000008000000004L
	};

	/**
	 * 用于收集所有超级回文数的方法（生成record数组）
	 * 
	 * @return 包含所有超级回文数的列表
	 */
	public static List<Long> collect() {
		long l = 1;
		long r = Long.MAX_VALUE;
		long limit = (long) Math.sqrt((double) r);
		long seed = 1;
		long enlarge = 0;
		ArrayList<Long> ans = new ArrayList<>();
		do {
			enlarge = evenEnlarge(seed);
			if (check(enlarge * enlarge, l, r)) {
				ans.add(enlarge * enlarge);
			}
			enlarge = oddEnlarge(seed);
			if (check(enlarge * enlarge, l, r)) {
				ans.add(enlarge * enlarge);
			}
			seed++;
		} while (enlarge < limit);
		ans.sort((a, b) -> a.compareTo(b));
		return ans;
	}

	public static void main(String[] args) {
		// 用于生成record数组
		/*
		List<Long> ans = collect();
		for (long p : ans) {
			System.out.println(p + "L,");
		}
		System.out.println("size : " + ans.size());
		*/
	}

	// ======== 补充训练题目 ========
	
	/**
	 * 补充题目1：LeetCode 479. 最大回文数乘积
	 * 题目描述：给定一个整数 n，返回可表示为两个 n 位数的乘积的最大回文整数。因为答案可能非常大，所以返回它对 1337 取余。
	 * 链接：https://leetcode.cn/problems/largest-palindrome-product/
	 * 
	 * 解题思路：
	 * - 从最大的n位数开始，向下枚举所有可能的回文数
	 * - 检查每个回文数是否可以分解为两个n位数的乘积
	 * - 时间复杂度：O(10^n)，空间复杂度：O(1)
	 */
	public static int largestPalindrome(int n) {
		if (n == 1) {
			return 9;
		}
		// 计算n位数的范围
		long upper = (long) Math.pow(10, n) - 1;
		long lower = (long) Math.pow(10, n - 1);
		
		// 从大到小枚举回文数
		for (long left = upper; left >= lower; left--) {
			// 构造回文数
			String s = String.valueOf(left);
			String rev = new StringBuilder(s).reverse().toString();
			long palindrome = Long.parseLong(s + rev);
			
			// 检查是否可以分解为两个n位数的乘积
			for (long i = upper; i * i >= palindrome; i--) {
				if (palindrome % i == 0) {
					long j = palindrome / i;
					if (j >= lower && j <= upper) {
						return (int) (palindrome % 1337);
					}
				}
			}
		}
		return -1;
	}
	
	/**
	 * 补充题目2：LeetCode 680. 验证回文字符串 Ⅱ
	 * 题目描述：给你一个字符串 s，最多可以从中删除一个字符，请判断是否能成为回文字符串。
	 * 链接：https://leetcode.cn/problems/valid-palindrome-ii/
	 * 
	 * 解题思路：
	 * - 使用双指针法，从两端向中间比较
	 * - 当遇到不匹配的字符时，尝试删除左边或右边的字符后继续判断
	 * - 时间复杂度：O(n)，空间复杂度：O(1)
	 */
	public static boolean validPalindrome(String s) {
		int left = 0, right = s.length() - 1;
		while (left < right) {
			if (s.charAt(left) != s.charAt(right)) {
				// 尝试删除左边字符或右边字符后继续检查
				return isPalindrome(s, left + 1, right) || isPalindrome(s, left, right - 1);
			}
			left++;
			right--;
		}
		return true;
	}
	
	private static boolean isPalindrome(String s, int left, int right) {
		while (left < right) {
			if (s.charAt(left) != s.charAt(right)) {
				return false;
			}
			left++;
			right--;
		}
		return true;
	}
	
	/**
	 * 补充题目3：CodeChef - PALIN - The Next Palindrome
	 * 题目描述：给定一个正整数N，找出大于N的最小回文数。
	 * 链接：https://www.codechef.com/problems/PALIN
	 * 
	 * 解题思路：
	 * - 生成下一个回文数的高效方法
	 * - 不需要检查每个数，而是直接构造下一个可能的回文数
	 * - 时间复杂度：O(n)，其中n是数字的位数
	 */
	public static String nextPalindrome(String n) {
		char[] digits = n.toCharArray();
		int len = digits.length;
		int mid = len / 2;
		int i = mid - 1;
		int j = len % 2 == 0 ? mid : mid + 1;
		
		// 检查是否所有数字都是9
		boolean allNines = true;
		for (char c : digits) {
			if (c != '9') {
				allNines = false;
				break;
			}
		}
		if (allNines) {
			StringBuilder sb = new StringBuilder();
			sb.append('1');
			for (i = 0; i < len - 1; i++) {
				sb.append('0');
			}
			sb.append('1');
			return sb.toString();
		}
		
		// 找到第一个可以增加的位置
		while (i >= 0 && digits[i] == digits[j]) {
			i--;
			j++;
		}
		
		boolean leftLess = false;
		if (i < 0 || digits[i] < digits[j]) {
			leftLess = true;
		}
		
		// 复制左侧到右侧
		while (i >= 0) {
			digits[j++] = digits[i--];
		}
		
		// 如果需要进位
		if (leftLess) {
			i = mid - 1;
			int carry = 1;
			if (len % 2 == 1) {
				digits[mid] += carry;
				carry = digits[mid] / 10;
				digits[mid] %= 10;
				j = mid + 1;
			} else {
				j = mid;
			}
			
			while (i >= 0 && carry > 0) {
				digits[i] += carry;
				carry = digits[i] / 10;
				digits[i] %= 10;
				digits[j++] = digits[i--];
			}
		}
		
		return new String(digits);
	}
	
	/**
	 * 补充题目4：HackerRank - The Longest Palindromic Subsequence
	 * 题目描述：给定一个字符串，找出其中最长的回文子序列的长度。
	 * 链接：https://www.hackerrank.com/challenges/longest-palindromic-subsequence/problem
	 * 
	 * 解题思路：
	 * - 使用动态规划，dp[i][j]表示子串s[i...j]的最长回文子序列长度
	 * - 状态转移方程：如果s[i] == s[j]，则dp[i][j] = dp[i+1][j-1] + 2；否则dp[i][j] = max(dp[i+1][j], dp[i][j-1])
	 * - 时间复杂度：O(n^2)，空间复杂度：O(n^2)
	 */
	public static int longestPalindromeSubseq(String s) {
		int n = s.length();
		int[][] dp = new int[n][n];
		
		// 单个字符的回文子序列长度为1
		for (int i = 0; i < n; i++) {
			dp[i][i] = 1;
		}
		
		// 填充dp数组
		for (int len = 2; len <= n; len++) {
			for (int i = 0; i <= n - len; i++) {
				int j = i + len - 1;
				if (s.charAt(i) == s.charAt(j)) {
					dp[i][j] = dp[i + 1][j - 1] + 2;
				} else {
					dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
				}
			}
		}
		
		return dp[0][n - 1];
	}
	
	/**
	 * 补充题目5：LintCode 1178. 有效的回文
	 * 题目描述：给定一个字符串，判断它是否是回文串。只考虑字母和数字字符，可以忽略字母的大小写。
	 * 链接：https://www.lintcode.com/problem/1178/
	 * 
	 * 解题思路：
	 * - 使用双指针法，从两端向中间比较
	 * - 只考虑字母和数字字符，忽略大小写
	 * - 时间复杂度：O(n)，空间复杂度：O(1)
	 */
	public static boolean isPalindrome(String s) {
		if (s == null || s.isEmpty()) {
			return true;
		}
		
		int left = 0, right = s.length() - 1;
		while (left < right) {
			// 跳过非字母数字字符
			while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
				left++;
			}
			while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
				right--;
			}
			
			if (left < right) {
				// 比较字符（忽略大小写）
				if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
					return false;
				}
				left++;
				right--;
			}
		}
		
		return true;
	}
	
	/**
	 * 补充题目6：LeetCode 647. 回文子串
	 * 题目描述：给你一个字符串 s ，请你统计并返回这个字符串中 回文子串 的数目。
	 * 回文字符串 是正着读和倒过来读一样的字符串。子字符串 是字符串中的由连续字符组成的一个序列。
	 * 链接：https://leetcode.cn/problems/palindromic-substrings/
	 * 
	 * 解题思路：
	 * - 使用中心扩展法，遍历每个可能的中心点
	 * - 考虑奇数长度和偶数长度的回文串
	 * - 时间复杂度：O(n^2)，空间复杂度：O(1)
	 */
	public static int countSubstrings(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			// 以单个字符为中心的奇数长度回文串
			count += expandAroundCenter(s, i, i);
			// 以两个字符之间为中心的偶数长度回文串
			count += expandAroundCenter(s, i, i + 1);
		}
		return count;
	}
	
	private static int expandAroundCenter(String s, int left, int right) {
		int count = 0;
		while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
			count++;
			left--;
			right++;
		}
		return count;
	}
	
	/**
	 * 补充题目7：LeetCode 336. 回文对
	 * 题目描述：给定一组互不相同的单词，找出所有不同的索引对 (i, j)，使得单词 words[i] + words[j] 是一个回文串。
	 * 链接：https://leetcode.cn/problems/palindrome-pairs/
	 * 
	 * 解题思路：
	 * - 使用哈希表存储单词的逆序和索引
	 * - 对于每个单词，尝试在哈希表中查找可以拼接形成回文的部分
	 * - 时间复杂度：O(n * k^2)，其中n是单词数量，k是单词的最大长度
	 * - 空间复杂度：O(n * k)
	 */
	public static List<List<Integer>> palindromePairs(String[] words) {
		List<List<Integer>> result = new ArrayList<>();
		Map<String, Integer> map = new HashMap<>();
		
		// 存储单词的逆序和索引
		for (int i = 0; i < words.length; i++) {
			map.put(new StringBuilder(words[i]).reverse().toString(), i);
		}
		
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			int len = word.length();
			
			// 情况1：空字符串与回文单词拼接
			if (map.containsKey("") && !word.isEmpty() && isPalindrome(word)) {
				result.add(Arrays.asList(i, map.get("")));
				result.add(Arrays.asList(map.get(""), i));
			}
			
			// 情况2：一个单词是另一个单词的逆序
			if (map.containsKey(word) && map.get(word) != i) {
				result.add(Arrays.asList(i, map.get(word)));
			}
			
			// 情况3：单词的前缀是回文，查找剩余部分的逆序
			for (int j = 1; j < len; j++) {
				String left = word.substring(0, j);
				String right = word.substring(j);
				
				// 检查左半部分是否为回文，如果是，则查找右半部分的逆序
				if (isPalindrome(left) && map.containsKey(right)) {
					result.add(Arrays.asList(map.get(right), i));
				}
				
				// 检查右半部分是否为回文，如果是，则查找左半部分的逆序
				if (isPalindrome(right) && map.containsKey(left)) {
					result.add(Arrays.asList(i, map.get(left)));
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 补充题目8：HackerRank - Split the String
	 * 题目描述：给定一个字符串，将其分割为k个回文子串，求最大可能的k值。
	 * 链接：https://www.hackerrank.com/challenges/split-the-string/problem
	 * 
	 * 解题思路：
	 * - 使用贪心策略，尽可能多地分割
	 * - 每次都尝试从当前位置分割出最短的回文子串
	 * - 时间复杂度：O(n^2)，空间复杂度：O(n)
	 */
	public static int splitTheString(String s) {
		int n = s.length();
		int count = 0;
		int start = 0;
		
		while (start < n) {
			// 尝试分割出最短的回文子串（长度为1或2）
			if (start + 1 < n && s.charAt(start) == s.charAt(start + 1)) {
				// 长度为2的回文
				start += 2;
				count++;
			} else {
				// 长度为1的回文
				start++;
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * 超级回文数与回文序列生成的算法技巧总结
	 * 
	 * 核心概念与理论基础：
	 * 1. 回文数理论：
	 *    - 定义：正序和倒序读都是一样的数字
	 *    - 分类：根据长度分为奇数长度和偶数长度回文数
	 *    - 分布：回文数在数字空间中呈稀疏分布
	 *    - 数学性质：回文数的平方不一定是回文数，反之亦然
	 * 
	 * 2. 超级回文数特性：
	 *    - 定义：既是回文数，又是某个回文数的平方
	 *    - 结构特征：通过种子数生成，具有高度对称性
	 *    - 数值范围：在10^18范围内仅有有限数量的超级回文数
	 *    - 唯一性：每个超级回文数都有唯一的生成方式
	 * 
	 * 3. 回文序列理论：
	 *    - 子串vs子序列：回文子串要求连续，回文子序列不要求连续
	 *    - 最优子结构：回文问题通常具有最优子结构性质
	 *    - 重叠子问题：动态规划方法的理论基础
	 *    - 对偶性：字符串反转后的性质与原字符串的关系
	 * 
	 * 算法设计与策略选择：
	 * 1. 生成策略：
	 *    - 种子扩展法：通过构建回文数种子，扩展生成完整回文数
	 *    - 中间扩展法：从中心向两侧扩展生成回文串
	 *    - 递推构造法：基于已知回文数构造更大的回文数
	 *    - 二分查找法：在回文数集合中进行高效查询
	 * 
	 * 2. 判断算法：
	 *    - 双指针法：从两端向中间比较，时间O(n)，空间O(1)
	 *    - 反转比较法：生成反转字符串进行比较，时间O(n)，空间O(n)
	 *    - 数值反转法：直接操作数字进行反转，避免字符串转换
	 *    - Manacher算法：高效查找所有回文子串，时间O(n)
	 * 
	 * 3. 优化技术：
	 *    - 预计算优化：通过预计算和打表法加速查询
	 *    - 剪枝策略：在生成过程中提前排除无效情况
	 *    - 位运算优化：利用位操作减少计算复杂度
	 *    - 并行计算：将生成和验证过程并行化
	 * 
	 * 高级优化技术：
	 * 1. 数学优化：
	 *    - 模运算加速：利用模运算性质减少计算量
	 *    - 数值溢出防护：提前检测可能的溢出情况
	 *    - 周期性利用：利用回文数生成的周期性规律
	 * 
	 * 2. 数据结构优化：
	 *    - 哈希表应用：存储逆序字符串以快速查找匹配
	 *    - 字典树结构：高效存储和查询前缀/后缀信息
	 *    - 稀疏数组：优化存储大规模稀疏数据
	 * 
	 * 3. 算法融合：
	 *    - 回溯与剪枝：结合使用以减少搜索空间
	 *    - 动态规划与贪心：根据问题特性选择合适方法
	 *    - 分支限界：结合启发式函数指导搜索
	 * 
	 * 工程化最佳实践：
	 * 1. 代码组织与模块化：
	 *    - 关注点分离：将生成、判断和查询逻辑分离
	 *    - 可重用组件：设计通用的回文处理组件
	 *    - 接口抽象：定义清晰的接口便于扩展
	 * 
	 * 2. 性能调优技巧：
	 *    - 减少对象创建：复用对象和数组
	 *    - 缓存中间结果：使用记忆化避免重复计算
	 *    - 批量处理：合并小操作提高吞吐量
	 *    - JVM优化：针对Java特性进行代码优化
	 * 
	 * 3. 健壮性保障：
	 *    - 输入验证：全面检查输入边界和异常情况
	 *    - 异常处理：妥善处理各种错误情况
	 *    - 资源管理：避免内存泄漏和资源浪费
	 *    - 并发安全：确保多线程环境下的正确性
	 * 
	 * 调试与测试技术：
	 * 1. 测试用例设计：
	 *    - 边界测试：空字符串、单字符、全相同字符
	 *    - 典型场景：各种长度和结构的回文数
	 *    - 性能测试：大规模数据下的性能表现
	 *    - 压力测试：边界条件和极限情况
	 * 
	 * 2. 调试技巧：
	 *    - 可视化输出：打印中间状态辅助调试
	 *    - 断言检查：使用断言验证关键假设
	 *    - 性能分析：识别瓶颈并优化
	 *    - 日志记录：详细记录执行流程
	 * 
	 * 跨语言实现比较：
	 * 1. Java实现：
	 *    - 优势：丰富的集合库，自动内存管理
	 *    - 限制：处理大整数可能需要BigInteger，性能开销较大
	 *    - 最佳实践：使用StringBuilder进行字符串操作，避免频繁字符串拼接
	 * 
	 * 2. C++实现：
	 *    - 优势：更高的执行效率，直接内存访问
	 *    - 限制：需要手动管理内存，代码复杂度较高
	 *    - 最佳实践：使用std::string_view避免不必要的拷贝，使用智能指针管理资源
	 * 
	 * 3. Python实现：
	 *    - 优势：简洁的语法，强大的字符串处理功能
	 *    - 限制：执行速度相对较慢，递归深度受限
	 *    - 最佳实践：使用字符串切片和生成器表达式，利用lru_cache进行记忆化
	 * 
	 * 4. Go实现：
	 *    - 优势：并发支持，编译型性能，简洁语法
	 *    - 限制：泛型支持相对有限，库生态不如Java丰富
	 *    - 最佳实践：使用切片而非数组，利用goroutine进行并行处理
	 * 
	 * 与现代技术的融合：
	 * 1. 算法与AI结合：
	 *    - 深度学习应用：使用神经网络识别复杂模式的回文结构
	 *    - 强化学习优化：通过学习自动调整搜索策略
	 *    - 生成对抗网络：生成符合特定模式的回文序列
	 * 
	 * 2. 分布式计算：
	 *    - MapReduce模式：将大规模回文搜索任务分布式处理
	 *    - 并行生成：利用多节点并行生成和验证回文数
	 *    - 分布式缓存：构建分布式缓存系统存储中间结果
	 * 
	 * 3. 区块链技术：
	 *    - 哈希链构造：利用回文结构构造特殊哈希链
	 *    - 共识算法：基于回文验证的共识机制
	 *    - 数据完整性：利用回文特性验证数据完整性
	 * 
	 * 进阶研究方向：
	 * 1. 理论研究：
	 *    - 回文数分布规律：探索回文数在大数域中的分布特性
	 *    - 超级回文数生成函数：寻找超级回文数的数学生成公式
	 *    - 计算复杂度分析：深入研究不同回文问题的复杂度
	 * 
	 * 2. 应用拓展：
	 *    - 密码学应用：基于回文特性设计安全机制
	 *    - 数据压缩：利用回文结构进行数据压缩
	 *    - 模式识别：将回文检测技术应用于更广泛的领域
	 * 
	 * 3. 算法创新：
	 *    - 量子算法：探索量子计算在回文问题中的应用
	 *    - 近似算法：设计针对NP难回文问题的高效近似算法
	 *    - 随机化算法：结合概率方法提高回文搜索效率
	 * 
	 * 总结：
	 * 超级回文数问题虽然看似简单，但涉及到广泛的算法技术和优化策略。通过系统学习和掌握这些技巧，
	 * 不仅可以高效解决回文相关问题，还能将这些思想应用到更广泛的算法设计中。在实际工程中，
	 * 应根据具体问题特性选择合适的算法和优化策略，平衡时间复杂度、空间复杂度和实现复杂度，
	 * 构建高效、健壮、可维护的解决方案。
	 */

}