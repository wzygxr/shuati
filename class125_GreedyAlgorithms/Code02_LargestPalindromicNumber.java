package class094;

import java.util.Arrays;

// 最大回文数字
// 给你一个仅由数字（0 - 9）组成的字符串num
// 请你找出能够使用num中数字形成的最大回文整数
// 并以字符串形式返回，该整数不含前导零
// 你无需使用num中的所有数字，但你必须使用至少一个数字，数字可以重新排列
// 测试链接 : https://leetcode.cn/problems/largest-palindromic-number/

/*
 * 题目解析：
 * 这是一个构造性问题，要求使用给定数字构造最大的回文数。
 * 关键在于理解回文数的特性：
 * 1. 回文数从左到右读和从右到左读是一样的
 * 2. 除了中心字符（奇数长度回文）外，其他字符都成对出现
 * 3. 要构造最大的回文数，应该将较大的数字放在高位
 *
 * 解题思路：
 * 1. 贪心策略：优先使用较大的数字构造回文数的高位
 * 2. 统计每个数字的出现次数
 * 3. 构造回文数的左半部分
 * 4. 确定中心数字（如果有奇数个的数字）
 * 5. 构造完整的回文数
 */
public class Code02_LargestPalindromicNumber {

	/*
	 * 算法思路：
	 * 1. 贪心策略：要构造最大的回文数，应该从高位到低位选择尽可能大的数字
	 * 2. 统计每个数字的出现次数
	 * 3. 构造回文数的左半部分：
	 *    - 从数字9到1，尽可能多地使用数字（每次使用2个）
	 *    - 特殊处理数字0：只有在左半部分已经有其他数字时才使用0
	 * 4. 确定中心数字：
	 *    - 从9到0，选择剩余数量为奇数的最大数字作为中心
	 *    - 如果没有奇数个的数字，且左半部分为空，则使用0作为中心
	 * 5. 构造完整的回文数：左半部分 + 中心 + 左半部分的逆序
	 *
	 * 时间复杂度：O(n) - n是字符串长度，统计和构造都是线性时间
	 * 空间复杂度：O(n) - 存储结果字符数组
	 * 是否最优解：是，这是处理此类问题的最优解法
	 *
	 * 工程化考量：
	 * 1. 异常处理：检查输入是否为空
	 * 2. 边界条件：处理全0、单个数字等特殊情况
	 * 3. 性能优化：使用字符数组避免字符串频繁拼接
	 * 4. 可读性：清晰的变量命名和注释
	 *
	 * 算法详解：
	 * 1. 统计频次：遍历字符串统计每个数字出现的次数
	 * 2. 构造左半部分：从大到小使用成对的数字
	 * 3. 确定中心：选择最大的奇数个数字作为中心
	 * 4. 构造右半部分：将左半部分逆序
	 * 5. 特殊处理：处理全0和无成对数字的情况
	 */
	public static String largestPalindromic(String num) {
		// 异常处理：检查输入是否为空
		if (num == null || num.length() == 0) {
			return "";
		}
		
		int n = num.length();
		// 统计每个数字的出现次数，'0'~'9'的ASCII码是48~57
		// 使用大小为58的数组是为了覆盖ASCII码范围，虽然实际只需要10个位置
		int[] cnts = new int[58];
		for (char a : num.toCharArray()) {
			cnts[a]++; // 直接使用字符的ASCII码作为索引
		}
		
		// ans数组用于存储构造的回文数
		char[] ans = new char[n];
		int leftSize = 0; // 左半部分的大小
		char mid = 0; // 中心字符，初始化为0表示还没有确定
		
		// 从数字9到1构造回文数的左半部分
		// 优先使用较大的数字构造高位，实现贪心策略
		for (char i = '9'; i >= '1'; i--) {
			// 如果当前数字有奇数个，且还没有确定中心数字，则将其作为中心
			// 使用位运算 (cnts[i] & 1) == 1 判断奇偶性，比 % 2 更高效
			if ((cnts[i] & 1) == 1 && mid == 0) {
				mid = i; // 选择最大的奇数个数字作为中心
			}
			
			// 将当前数字的一半数量添加到左半部分
			// 每次使用2个数字构造回文，所以添加 cnts[i] / 2 个
			for (int j = cnts[i] / 2; j > 0; j--) {
				ans[leftSize++] = i;
			}
		}
		
		// 处理特殊情况
		// 如果leftSize为0，说明'1'~'9'每一种字符出现次数 <= 1
		if (leftSize == 0) { 
			if (mid == 0) { // '1'~'9'每一种字符出现次数 == 0，即全是0
				return "0"; // 特殊情况：输入全是0，返回"0"而不是空字符串
			} else { // '1'~'9'有若干字符出现次数 == 1，其中最大的字符是mid
				return String.valueOf(mid); // 只有一个非0数字，直接返回
			}
		}
		
		// '1'~'9'有若干字符出现次数 >= 2，左部分已经建立，再考虑把'0'字符填进来
		// 数字0只能在左半部分已经有其他数字时才使用，避免前导零
		for (int j = cnts['0'] / 2; j > 0; j--) {
			ans[leftSize++] = '0';
		}
		
		int len = leftSize; // 当前构造的长度
		// 确定中心数字
		// 如果还没有确定中心且数字0有奇数个，则使用0作为中心
		if (mid == 0 && (cnts['0'] & 1) == 1) {
			mid = '0';
		}
		
		// 添加中心数字（如果存在）
		// 只有在回文数长度为奇数时才有中心字符
		if (mid != 0) {
			ans[len++] = mid;
		}
		
		// 构造右半部分：左部分逆序拷贝给右部分
		// 从左半部分的最后一个字符开始，逆序复制到右半部分
		for (int i = leftSize - 1; i >= 0; i--) {
			ans[len++] = ans[i];
		}
		
		// 返回构造的回文数
		return new String(ans, 0, len);
	}
	
	/*
	 * 相关题目1: LeetCode 9. 回文数
	 * 题目链接: https://leetcode.cn/problems/palindrome-number/
	 * 题目描述: 给你一个整数 x ，如果 x 是一个回文整数，返回 true ；否则，返回 false 。
	 * 回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
	 * 例如，121 是回文，而 123 不是。
	 * 解题思路: 数学方法，通过取余和除法操作反转整数的一半
	 * 时间复杂度: O(log n) - n是输入数字的值
	 * 空间复杂度: O(1)
	 * 是否最优解: 是，这是处理此类问题的最优解法
	 *
	 * 算法详解：
	 * 1. 特殊情况处理：负数和末尾为0但非0的数不是回文数
	 * 2. 数学反转：通过取余和除法操作反转整数的一半
	 * 3. 比较判断：将原数和反转数进行比较
	 * 4. 奇数长度处理：通过 revertedNumber/10 去除中位数字
	 */
	public static boolean isPalindrome(int x) {
		// 负数和末尾为0但非0的数不是回文数
		// 负数不是回文数，因为负号只在一边
		// 末尾为0但非0的数不是回文数，因为开头不能为0
		if (x < 0 || (x % 10 == 0 && x != 0)) {
			return false;
		}
		
		int revertedNumber = 0;
		// 反转数字的一半
		// 当x <= revertedNumber时，说明已经反转了一半数字
		while (x > revertedNumber) {
			// 将x的最后一位数字添加到revertedNumber的末尾
			revertedNumber = revertedNumber * 10 + x % 10;
			// 去掉x的最后一位数字
			x /= 10;
		}
		
		// 当数字长度为奇数时，我们可以通过 revertedNumber/10 去除处于中位的数字
		// 例如：12321，经过循环后x=12, revertedNumber=123，中位数3需要被忽略
		// 所以比较x == revertedNumber/10
		// 当数字长度为偶数时，直接比较x == revertedNumber
		return x == revertedNumber || x == revertedNumber / 10;
	}
	
	/*
	 * 相关题目2: LeetCode 5. 最长回文子串
	 * 题目链接: https://leetcode.cn/problems/longest-palindromic-substring/
	 * 题目描述: 给你一个字符串 s，找到 s 中最长的回文子串。
	 * 解题思路: 中心扩展法，枚举每个可能的中心位置，向两边扩展
	 * 时间复杂度: O(n^2) - n是字符串长度
	 * 空间复杂度: O(1)
	 * 是否最优解: 否，存在Manacher算法可以达到O(n)时间复杂度
	 *
	 * 算法详解：
	 * 1. 中心扩展：以每个字符和每两个字符之间为中点进行扩展
	 * 2. 两种情况：奇数长度回文（以字符为中心）和偶数长度回文（以字符间隙为中心）
	 * 3. 扩展过程：从中心向两边扩展，直到字符不匹配或越界
	 * 4. 记录最长：记录所有回文中长度最长的一个
	 */
	public static String longestPalindrome(String s) {
		// 输入验证：检查字符串是否为空
		if (s == null || s.length() < 1) return "";
		
		int start = 0, end = 0; // 记录最长回文子串的起始和结束位置
		// 遍历每个可能的中心位置
		for (int i = 0; i < s.length(); i++) {
			// 以i为中心的奇数长度回文，如"aba"
			int len1 = expandAroundCenter(s, i, i);
			// 以i和i+1为中心的偶数长度回文，如"abba"
			int len2 = expandAroundCenter(s, i, i + 1);
			// 取两种情况中的最大长度
			int len = Math.max(len1, len2);
			
			// 更新最长回文子串的起始和结束位置
			// 如果当前回文长度大于之前记录的最长回文长度
			if (len > end - start) {
				// 计算新的起始位置：i - (len - 1) / 2
				// 对于奇数长度：i - (len - 1) / 2 = i - (2k+1-1) / 2 = i - k
				// 对于偶数长度：i - (len - 1) / 2 = i - (2k-1) / 2 = i - (k-1)
				start = i - (len - 1) / 2;
				// 计算新的结束位置：i + len / 2
				// 对于奇数长度：i + len / 2 = i + 2k / 2 = i + k
				// 对于偶数长度：i + len / 2 = i + 2k / 2 = i + k
				end = i + len / 2;
			}
		}
		
		// 返回最长回文子串
		return s.substring(start, end + 1);
	}
	
	// 辅助函数：中心扩展
	// 从给定的left和right位置向两边扩展，找到以该位置为中心的最长回文
	private static int expandAroundCenter(String s, int left, int right) {
		// 从中心向两边扩展，直到字符不匹配或越界
		// left >= 0：左边界检查
		// right < s.length()：右边界检查
		// s.charAt(left) == s.charAt(right)：字符匹配检查
		while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
			left--;  // 左指针向左移动
			right++; // 右指针向右移动
		}
		// 返回回文长度
		// 循环结束时，left和right分别指向回文边界外的第一个字符
		// 所以回文长度为 right - left - 1
		return right - left - 1;
	}
}