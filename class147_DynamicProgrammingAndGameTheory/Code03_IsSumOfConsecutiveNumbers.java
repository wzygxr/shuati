import java.util.*;

// 连续正整数和判断
// 
// 题目描述：
// 判断一个数字是否是若干数量(数量>1)的连续正整数的和
// 
// 解题思路：
// 这是一个数论问题，可以使用暴力枚举、数学优化或数学公式来解决
// 1. 暴力枚举法：枚举所有可能的连续正整数序列
// 2. 数学优化法：通过数学推导发现规律
// 3. 数学公式法：利用等差数列求和公式
// 
// 相关题目：
// 1. LeetCode 829. Consecutive Numbers Sum：https://leetcode.com/problems/consecutive-numbers-sum/
// 2. POJ 2140. Sequence Sum Possibilities：http://poj.org/problem?id=2140
// 3. HDU 1977. Consecutive sum II：http://acm.hdu.edu.cn/showproblem.php?pid=1977
// 4. LeetCode 1446. Consecutive Characters：https://leetcode.com/problems/consecutive-characters/
// 5. LeetCode 128. Longest Consecutive Sequence：https://leetcode.com/problems/longest-consecutive-sequence/
// 6. LeetCode 523. Continuous Subarray Sum：https://leetcode.com/problems/continuous-subarray-sum/
// 
// 工程化考量：
// 1. 异常处理：处理负数和零输入
// 2. 边界条件：处理小规模数据
// 3. 性能优化：使用数学规律O(1)解法
// 4. 可读性：清晰的变量命名和注释
public class Code03_IsSumOfConsecutiveNumbers {

	/*
	 * 方法1：暴力枚举法
	 * 
	 * 解题思路：
	 * 枚举所有可能的连续正整数序列，计算其和是否等于目标数字
	 * 
	 * 时间复杂度：O(n^2)
	 * 空间复杂度：O(1)
	 * 
	 * 优缺点分析：
	 * 优点：思路直观，易于理解和实现
	 * 缺点：时间复杂度高，不适合大规模数据
	 * 
	 * 适用场景：小规模数据验证，教学演示
	 */
	public static boolean is1(int num) {
		for (int start = 1, sum; start <= num; start++) {
			sum = start;
			for (int j = start + 1; j <= num; j++) {
				if (sum + j > num) {
					break;
				}
				if (sum + j == num) {
					return true;
				}
				sum += j;
			}
		}
		return false;
	}

	/*
	 * 方法2：数学优化法（最优解）
	 * 
	 * 解题思路：
	 * 通过数学推导发现规律：
	 * 一个数可以表示为连续正整数和当且仅当它不是2的幂
	 * 
	 * 数学证明：
	 * 假设一个数n可以表示为从a开始的k个连续正整数的和：
	 * n = a + (a+1) + ... + (a+k-1)
	 * n = k*a + k*(k-1)/2
	 * n = k*(2a + k - 1)/2
	 * 
	 * 令m = 2a + k - 1，则：
	 * 2n = k*m
	 * 
	 * 由于k和m的奇偶性不同，且k < m
	 * 所以2n的因子中必须包含一个奇数因子（k或m为奇数）
	 * 因此n不能是2的幂（因为2的幂没有奇数因子）
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 优缺点分析：
	 * 优点：时间空间复杂度都是O(1)，性能最优
	 * 缺点：需要预先发现规律
	 * 
	 * 适用场景：大规模数据，对性能要求高的场景
	 */
	public static boolean is2(int num) {
		// 如果num是2的幂，则返回false；否则返回true
		return (num & (num - 1)) != 0;
	}

	/*
	 * 方法3：数学公式法
	 * 
	 * 解题思路：
	 * 利用等差数列求和公式：
	 * 设连续正整数序列的长度为k，起始值为a
	 * 则：num = k*a + k*(k-1)/2
	 * 整理得：a = (2*num/k - k + 1)/2
	 * 需要满足：a是正整数且k >= 2
	 * 
	 * 时间复杂度：O(√n)
	 * 空间复杂度：O(1)
	 * 
	 * 优缺点分析：
	 * 优点：时间复杂度较低，适合中等规模数据
	 * 缺点：需要一定的数学推导能力
	 * 
	 * 适用场景：中等规模数据，需要准确结果的场景
	 */
	public static boolean is3(int num) {
		// k为连续正整数的个数，k至少为2
		for (int k = 2; k * (k + 1) / 2 <= num; k++) {
			// 计算起始值a
			int numerator = 2 * num - k * (k - 1);
			if (numerator <= 0) break;
			
			if (numerator % (2 * k) == 0) {
				int a = numerator / (2 * k);
				if (a > 0) {
					return true;
				}
			}
		}
		return false;
	}

	// ==================== 扩展题目1: 连续整数求和 ====================
	/*
	 * LeetCode 829. Consecutive Numbers Sum (困难)
	 * 题目：给定正整数n，返回n可以表示为连续正整数和的方案数
	 * 网址：https://leetcode.com/problems/consecutive-numbers-sum/
	 * 
	 * 数学解法：
	 * 时间复杂度：O(√n)
	 * 空间复杂度：O(1)
	 */
	public static int consecutiveNumbersSum(int n) {
		int count = 0;
		
		// 根据等差数列求和公式：n = k*(2a + k - 1)/2
		// 其中k为连续正整数的个数，a为起始值
		// 整理得：2n = k*(2a + k - 1)
		// 所以k必须是2n的因子，且2a = (2n/k - k + 1)必须是正整数
		for (int k = 1; k * (k + 1) / 2 <= n; k++) {
			int numerator = 2 * n - k * (k - 1);
			if (numerator <= 0) break;
			
			if (numerator % (2 * k) == 0) {
				int a = numerator / (2 * k);
				if (a > 0) {
					count++;
				}
			}
		}
		
		return count;
	}

	// ==================== 扩展题目2: 序列和可能性 ====================
	/*
	 * POJ 2140. Sequence Sum Possibilities (简单)
	 * 题目：给定正整数n，求n可以表示为连续正整数和的方案数
	 * 网址：http://poj.org/problem?id=2140
	 * 
	 * 数学解法：
	 * 时间复杂度：O(√n)
	 * 空间复杂度：O(1)
	 */
	public static int poj2140(int n) {
		return consecutiveNumbersSum(n);
	}

	// ==================== 扩展题目3: 连续和II ====================
	/*
	 * HDU 1977. Consecutive sum II (中等)
	 * 题目：求1^3 + 2^3 + ... + n^3的和
	 * 网址：http://acm.hdu.edu.cn/showproblem.php?pid=1977
	 * 
	 * 数学公式：1^3 + 2^3 + ... + n^3 = (n*(n+1)/2)^2
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 */
	public static long hdu1977(int n) {
		long sum = (long)n * (n + 1) / 2;
		return sum * sum;
	}

	// ==================== 扩展题目4: 连续字符 ====================
	/*
	 * LeetCode 1446. Consecutive Characters (简单)
	 * 题目：求字符串中最长连续相同字符的子串长度
	 * 网址：https://leetcode.com/problems/consecutive-characters/
	 * 
	 * 一次遍历解法：
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(1)
	 */
	public static int maxPower(String s) {
		if (s == null || s.length() == 0) return 0;
		
		int maxLen = 1;
		int currentLen = 1;
		
		for (int i = 1; i < s.length(); i++) {
			if (s.charAt(i) == s.charAt(i - 1)) {
				currentLen++;
				maxLen = Math.max(maxLen, currentLen);
			} else {
				currentLen = 1;
			}
		}
		
		return maxLen;
	}

	// ==================== 扩展题目5: 连续序列 ====================
	/*
	 * LeetCode 128. Longest Consecutive Sequence (困难)
	 * 题目：求未排序数组中最长连续序列的长度
	 * 网址：https://leetcode.com/problems/longest-consecutive-sequence/
	 * 
	 * 哈希集合解法：
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 */
	public static int longestConsecutive(int[] nums) {
		if (nums == null || nums.length == 0) return 0;
		
		Set<Integer> set = new HashSet<>();
		for (int num : nums) {
			set.add(num);
		}
		
		int maxLen = 0;
		
		for (int num : set) {
			// 只从序列的起点开始计算
			if (!set.contains(num - 1)) {
				int currentNum = num;
				int currentLen = 1;
				
				while (set.contains(currentNum + 1)) {
					currentNum++;
					currentLen++;
				}
				
				maxLen = Math.max(maxLen, currentLen);
			}
		}
		
		return maxLen;
	}

	// ==================== 扩展题目6: 连续子数组和 ====================
	/*
	 * LeetCode 523. Continuous Subarray Sum (中等)
	 * 题目：判断数组中是否存在长度至少为2的连续子数组，其和是k的倍数
	 * 网址：https://leetcode.com/problems/continuous-subarray-sum/
	 * 
	 * 前缀和+哈希表解法：
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(min(n, k))
	 */
	public static boolean checkSubarraySum(int[] nums, int k) {
		if (nums == null || nums.length < 2) return false;
		
		Map<Integer, Integer> map = new HashMap<>();
		map.put(0, -1); // 处理从0开始的情况
		
		int prefixSum = 0;
		
		for (int i = 0; i < nums.length; i++) {
			prefixSum += nums[i];
			int mod = prefixSum % k;
			
			if (map.containsKey(mod)) {
				if (i - map.get(mod) >= 2) {
					return true;
				}
			} else {
				map.put(mod, i);
			}
		}
		
		return false;
	}

	// ==================== 测试方法 ====================
	public static void main(String[] args) {
		// 测试连续正整数和判断
		System.out.println("=== 连续正整数和判断测试 ===");
		for (int i = 1; i <= 20; i++) {
			boolean result1 = is1(i);
			boolean result2 = is2(i);
			boolean result3 = is3(i);
			System.out.println(i + ": " + result1 + " / " + result2 + " / " + result3);
		}
		
		// 测试扩展题目
		System.out.println("\n=== 扩展题目测试 ===");
		
		// 测试连续整数求和
		System.out.println("Consecutive Numbers Sum (15): " + consecutiveNumbersSum(15)); // 4
		
		// 测试立方和
		System.out.println("HDU 1977 (5): " + hdu1977(5)); // 225
		
		// 测试连续字符
		System.out.println("Max Power (\"leetcode\"): " + maxPower("leetcode")); // 2
		
		// 测试最长连续序列
		int[] nums2 = {100, 4, 200, 1, 3, 2};
		System.out.println("Longest Consecutive: " + longestConsecutive(nums2)); // 4
		
		// 测试连续子数组和
		int[] nums3 = {23, 2, 4, 6, 7};
		System.out.println("Check Subarray Sum (6): " + checkSubarraySum(nums3, 6)); // true
	}
}