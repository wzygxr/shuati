// 不用任何算术运算，只用位运算实现加减乘除及相关位运算算法
// 移除了包声明，便于直接运行

import java.util.*;

/**
 * 位运算实现四则运算及相关算法题解
 * 
 * 本类使用纯位运算实现加减乘除四则运算，以及多种与位运算相关的算法题目解答。
 * 所有实现都避免使用任何算术运算符（+、-、*、/），仅使用位运算符。
 * 
 * 核心思想：
 * 1. 加法：利用异或运算实现无进位加法，利用与运算和左移实现进位
 * 2. 减法：基于加法和相反数实现，a - b = a + (-b)
 * 3. 乘法：基于二进制分解，检查乘数每一位是否为1，为1则将被乘数左移相应位数后累加
 * 4. 除法：从高位到低位尝试减法，使用位移优化性能
 * 
 * 测试链接 : https://leetcode.cn/problems/divide-two-integers/
 * 
 * @author Algorithm Journey
 * @version 1.0
 */
public class BitOperationAddMinusMultiplyDivide {

	public static int MIN = Integer.MIN_VALUE;

	public static int divide(int a, int b) {
		if (a == MIN && b == MIN) {
			// a和b都是整数最小
			return 1;
		}
		if (a != MIN && b != MIN) {
			// a和b都不是整数最小，那么正常去除
			return div(a, b);
		}
		if (b == MIN) {
			// a不是整数最小，b是整数最小
			return 0;
		}
		// a是整数最小，b是-1，返回整数最大，因为题目里明确这么说了
		if (b == neg(1)) {
			return Integer.MAX_VALUE;
		}
		// a是整数最小，b不是整数最小，b也不是-1
		a = add(a, b > 0 ? b : neg(b));
		int ans = div(a, b);
		int offset = b > 0 ? neg(1) : 1;
		return add(ans, offset);
	}

	/**
	 * 除法辅助函数：必须保证a和b都不是整数最小值，返回a除以b的结果
	 * 
	 * 算法原理：
	 * 1. 将a和b都转换为正数处理（取绝对值）
	 * 2. 从最高位开始，尝试将被除数减去除数的倍数
	 * 3. 使用位移优化性能，避免逐个减法
	 * 
	 * 时间复杂度：O(1) - 固定位数的整数
	 * 空间复杂度：O(1) - 只使用常数级额外空间
	 * 
	 * @param a 被除数（非整数最小值）
	 * @param b 除数（非整数最小值）
	 * @return a除以b的结果
	 */
	public static int div(int a, int b) {
		int x = a < 0 ? neg(a) : a;
		int y = b < 0 ? neg(b) : b;
		int ans = 0;
		for (int i = 30; i >= 0; i = minus(i, 1)) {
			if ((x >> i) >= y) {
				ans |= (1 << i);
				x = minus(x, y << i);
			}
		}
		return a < 0 ^ b < 0 ? neg(ans) : ans;
	}

	/**
	 * 加法实现
	 * 
	 * 算法原理：
	 * 1. 异或运算(^)实现无进位加法
	 * 2. 与运算(&)和左移(<<)实现进位
	 * 3. 循环直到没有进位
	 * 
	 * 例如：计算 5 + 3
	 * 5 的二进制: 101
	 * 3 的二进制: 011
	 * 第一次循环:
	 *   无进位加法: 101 ^ 011 = 110
	 *   进位: (101 & 011) << 1 = 001 << 1 = 010
	 * 第二次循环:
	 *   无进位加法: 110 ^ 010 = 100
	 *   进位: (110 & 010) << 1 = 010 << 1 = 100
	 * 第三次循环:
	 *   无进位加法: 100 ^ 100 = 000
	 *   进位: (100 & 100) << 1 = 100 << 1 = 1000
	 * 第四次循环:
	 *   无进位加法: 000 ^ 1000 = 1000
	 *   进位: (000 & 1000) << 1 = 000 << 1 = 000
	 * 进位为0，循环结束，结果为 1000 (二进制) = 8 (十进制)
	 * 
	 * 时间复杂度：O(1) - 固定位数的整数
	 * 空间复杂度：O(1) - 只使用常数级额外空间
	 * 
	 * @param a 第一个加数
	 * @param b 第二个加数
	 * @return a与b的和
	 */
	public static int add(int a, int b) {
		int ans = a;
		while (b != 0) {
			// ans : a和b无进位相加的结果
			ans = a ^ b;
			// b : a和b相加时的进位信息
			b = (a & b) << 1;
			a = ans;
		}
		return ans;
	}

	/**
	 * 递归版本的加法实现（LeetCode 371. 两整数之和）
	 * 
	 * 题目链接: https://leetcode.cn/problems/sum-of-two-integers/
	 * 题目描述: 给你两个整数 a 和 b ，不使用运算符 + 和 - ，计算并返回两整数之和。
	 * 
	 * 算法原理：
	 * 1. 递归终止条件：当没有进位时，异或结果就是最终结果
	 * 2. 递归计算：无进位相加的结果 + 进位
	 * 
	 * 时间复杂度: O(1) - 因为整数的位数是固定的
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param a 第一个整数
	 * @param b 第二个整数
	 * @return a与b的和
	 */
	public static int addRecursive(int a, int b) {
		// 递归终止条件：当没有进位时，异或结果就是最终结果
		if (b == 0) {
			return a;
		}
		// 递归计算：无进位相加的结果 + 进位
		return addRecursive(a ^ b, (a & b) << 1);
	}

	/**
	 * 减法实现
	 * 
	 * 算法原理：
	 * 基于加法和相反数实现
	 * a - b = a + (-b)
	 * 
	 * 时间复杂度：O(1) - 固定位数的整数
	 * 空间复杂度：O(1) - 只使用常数级额外空间
	 * 
	 * @param a 被减数
	 * @param b 减数
	 * @return a与b的差
	 */
	public static int minus(int a, int b) {
		return add(a, neg(b));
	}

	/**
	 * 求相反数
	 * 
	 * 算法原理：
	 * 基于补码表示法
	 * -n = ~n + 1
	 * 
	 * 时间复杂度：O(1) - 固定位数的整数
	 * 空间复杂度：O(1) - 只使用常数级额外空间
	 * 
	 * @param n 待求相反数的整数
	 * @return n的相反数
	 */
	public static int neg(int n) {
		return add(~n, 1);
	}

	/**
	 * 乘法实现（龟速乘）
	 * 
	 * 算法原理：
	 * 基于二进制分解
	 * 检查乘数b的每一位是否为1
	 * 如果为1，则将被乘数a左移相应位数后累加到结果中
	 * 
	 * 例如：计算 5 * 3
	 * 5 的二进制: 101
	 * 3 的二进制: 011
	 * 检查3的每一位：
	 *   第0位：1，将5左移0位(5)累加到结果中
	 *   第1位：1，将5左移1位(10)累加到结果中
	 *   第2位：0，不累加
	 * 结果：5 + 10 = 15
	 * 
	 * 时间复杂度：O(log b) - b的二进制位数
	 * 空间复杂度：O(1) - 只使用常数级额外空间
	 * 
	 * @param a 被乘数
	 * @param b 乘数
	 * @return a与b的积
	 */
	public static int multiply(int a, int b) {
		int ans = 0;
		while (b != 0) {
			if ((b & 1) != 0) {
				// 考察b当前最右的状态！
				ans = add(ans, a);
			}
			a <<= 1;
			b >>>= 1;
		}
		return ans;
	}

	/**
	 * 计算一个数字的二进制表示中1的个数（汉明重量）
	 * LeetCode 191. 位1的个数
	 * 题目链接: https://leetcode.cn/problems/number-of-1-bits/
	 * 题目描述: 编写一个函数，输入是一个无符号整数（以二进制串的形式），返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。
	 * 
	 * 算法原理：
	 * 遍历32位，检查每一位是否为1
	 * 
	 * 时间复杂度: O(1) - 最多循环32次（32位整数）
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param n 输入的整数
	 * @return n的二进制表示中1的个数
	 */
	public static int hammingWeight(int n) {
		int count = 0;
		for (int i = 0; i < 32; i++) {
			// 检查n的第i位是否为1
			if ((n & (1 << i)) != 0) {
				count = add(count, 1);
			}
		}
		return count;
	}

	/**
	 * 优化版本的汉明重量计算（更高效）
	 * 
	 * 算法原理：
	 * 利用 n & (n-1) 可以清除n的二进制表示中最右边的1
	 * 每次操作都会清除最右边的一个1，直到n变为0
	 * 
	 * 例如：计算 12 的汉明重量
	 * 12 的二进制: 1100
	 * 第一次：1100 & 1011 = 1000 (清除最右边的1)
	 * 第二次：1000 & 0111 = 0000 (清除最右边的1)
	 * 循环2次，所以汉明重量为2
	 * 
	 * 时间复杂度: O(k) - k是二进制表示中1的个数
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param n 输入的整数
	 * @return n的二进制表示中1的个数
	 */
	public static int hammingWeightOptimized(int n) {
		int count = 0;
		while (n != 0) {
			count = add(count, 1);
			// 清除最右边的1
			n = n & (n - 1);
		}
		return count;
	}

	/**
	 * 判断一个数是否是2的幂
	 * LeetCode 231. 2的幂
	 * 题目链接: https://leetcode.cn/problems/power-of-two/
	 * 题目描述: 给你一个整数 n，请你判断该整数是否是 2 的幂次方。
	 * 
	 * 算法原理：
	 * 2的幂在二进制表示中只有一个1，且必须是正数
	 * n & (n-1) 会清除n的二进制表示中最右边的1
	 * 如果n是2的幂，那么n & (n-1)的结果应该是0
	 * 
	 * 例如：
	 * 8 的二进制: 1000
	 * 7 的二进制: 0111
	 * 8 & 7 = 0000
	 * 
	 * 时间复杂度: O(1) - 只进行一次位运算
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param n 待判断的整数
	 * @return 如果n是2的幂返回true，否则返回false
	 */
	public static boolean isPowerOfTwo(int n) {
		// 2的幂在二进制表示中只有一个1，且必须是正数
		// n & (n-1) 会清除n的二进制表示中最右边的1
		// 如果n是2的幂，那么n & (n-1)的结果应该是0
		return n > 0 && (n & (n - 1)) == 0;
	}

	/**
	 * 计算两个数字的汉明距离（对应二进制位不同的位置的数目）
	 * LeetCode 461. 汉明距离
	 * 题目链接: https://leetcode.cn/problems/hamming-distance/
	 * 题目描述: 两个整数之间的 汉明距离 指的是这两个数字对应二进制位不同的位置的数目。
	 * 
	 * 算法原理：
	 * 1. 先对两个数进行异或运算，相同为0，不同为1
	 * 2. 然后计算异或结果中1的个数
	 * 
	 * 时间复杂度: O(1) - 最多循环32次（32位整数）
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param x 第一个整数
	 * @param y 第二个整数
	 * @return x和y的汉明距离
	 */
	public static int hammingDistance(int x, int y) {
		// 先对两个数进行异或运算，相同为0，不同为1
		int xor = x ^ y;
		// 然后计算xor中1的个数
		return hammingWeight(xor);
	}

	/**
	 * 不用加减乘除做加法（剑指Offer 65）
	 * 题目链接: https://leetcode.cn/problems/bu-yong-jia-jian-cheng-chu-zuo-jia-fa-lcof/
	 * 题目描述: 写一个函数，求两个整数之和，要求在函数体内不得使用 “+”、“-”、“*”、“/”四则运算符号。
	 * 
	 * 算法原理：
	 * 与add方法原理相同，循环直到没有进位
	 * 
	 * 时间复杂度: O(1) - 最多循环32次（32位整数）
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param a 第一个整数
	 * @param b 第二个整数
	 * @return a与b的和
	 */
	public static int addWithoutArithmetic(int a, int b) {
		// 循环直到没有进位
		while (b != 0) {
			// 计算进位
			int carry = (a & b) << 1;
			// 计算无进位和
			a = a ^ b;
			// 将进位赋值给b，继续下一轮循环
			b = carry;
		}
		return a;
	}
	
	/**
	 * LeetCode 136. 只出现一次的数字
	 * 题目链接: https://leetcode.cn/problems/single-number/
	 * 题目描述: 给你一个非空整数数组 nums ，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
	 * 
	 * 算法原理:
	 * 利用异或运算的性质：
	 * 1. a ^ a = 0 (任何数与自己异或结果为0)
	 * 2. a ^ 0 = a (任何数与0异或结果为自己)
	 * 3. 异或运算满足交换律和结合律
	 * 
	 * 因此，将数组中所有元素异或，出现两次的元素会相互抵消为0，
	 * 最终只剩下只出现一次的元素。
	 * 
	 * 时间复杂度: O(n) - 需要遍历整个数组
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param nums 输入的整数数组
	 * @return 只出现一次的元素
	 */
	public static int singleNumber(int[] nums) {
		int result = 0;
		for (int num : nums) {
			result = result ^ num;
		}
		return result;
	}
	
	/**
	 * LeetCode 268. 缺失的数字
	 * 题目链接: https://leetcode.cn/problems/missing-number/
	 * 题目描述: 给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数。
	 * 
	 * 算法原理:
	 * 利用异或运算的性质：
	 * 1. 将索引0到n-1与数组元素nums[0]到nums[n-1]一起异或
	 * 2. 再异或n
	 * 3. 由于除了缺失的数字外，其他数字都会出现两次，最终结果就是缺失的数字
	 * 
	 * 例如：nums = [3, 0, 1]，n = 3
	 * 初始result = 0
	 * i=0: result = 0 ^ 0 ^ 3 = 3
	 * i=1: result = 3 ^ 1 ^ 0 = 2
	 * i=2: result = 2 ^ 2 ^ 1 = 3
	 * 最后: result = 3 ^ 3 = 0
	 * 但0在数组中存在，所以缺失的是另一个数字
	 * 正确做法是最后再异或n: result = 3 ^ 3 = 0
	 * 
	 * 时间复杂度: O(n) - 需要遍历整个数组
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param nums 输入的整数数组
	 * @return 缺失的数字
	 */
	public static int missingNumber(int[] nums) {
		int result = 0;
		int n = nums.length;
		for (int i = 0; i < n; i++) {
			result = result ^ i ^ nums[i];
		}
		return result ^ n;
	}
	
	/**
	 * LeetCode 338. 比特位计数
	 * 题目链接: https://leetcode.cn/problems/counting-bits/
	 * 题目描述: 给你一个整数 n ，对于 0 <= i <= n 中的每个 i ，计算其二进制表示中 1 的个数，返回一个长度为 n + 1 的数组 ans 作为答案。
	 * 
	 * 算法原理:
	 * 利用动态规划思想：
	 * 对于数字i，其1的个数等于 i>>1 的1的个数加上 i 的最低位
	 * i>>1 相当于i除以2，(i & 1)判断i的最低位是否为1
	 * 
	 * 例如：
	 * i=5 (二进制: 101)
	 * i>>1 = 2 (二进制: 10)
	 * (i & 1) = 1 (最低位是1)
	 * 所以countBits(5) = countBits(2) + 1 = 1 + 1 = 2
	 * 
	 * 时间复杂度: O(n) - 只需要遍历一次
	 * 空间复杂度: O(1) - 除了返回数组外，只使用常数级额外空间
	 * 
	 * @param n 输入的整数
	 * @return 长度为n+1的数组，ans[i]表示i的二进制中1的个数
	 */
	public static int[] countBits(int n) {
		int[] result = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			result[i] = result[i >> 1] + (i & 1);
		}
		return result;
	}
	
	/**
	 * LeetCode 260. 只出现一次的数字 III
	 * 题目链接: https://leetcode.cn/problems/single-number-iii/
	 * 题目描述: 给你一个整数数组 nums ，其中恰好有两个元素只出现一次，其余所有元素均出现两次。找出只出现一次的那两个元素。
	 * 
	 * 算法原理:
	 * 1. 先对所有数字异或，得到两个只出现一次数字的异或结果
	 * 2. 找到异或结果中任意一个为1的位，这个位在两个只出现一次的数字中必然不同
	 * 3. 根据这一位将数组分为两组分别异或，得到两个只出现一次的数字
	 * 
	 * 例如：nums = [1, 2, 1, 3, 2, 5]
	 * 1. 所有数字异或：1^2^1^3^2^5 = 3^5 = 6 (二进制: 110)
	 * 2. 找到最右边的1：6 & (-6) = 2 (二进制: 10)
	 * 3. 根据第1位是否为1分组：
	 *    第1位为1的组：[2, 2, 3] -> 异或结果为3
	 *    第1位为0的组：[1, 1, 5] -> 异或结果为5
	 * 
	 * 时间复杂度: O(n) - 需要遍历整个数组两次
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param nums 输入的整数数组
	 * @return 包含两个只出现一次元素的数组
	 */
	public static int[] singleNumberIII(int[] nums) {
		// 对所有数字异或，得到两个只出现一次数字的异或结果
		int xor = 0;
		for (int num : nums) {
			xor ^= num;
		}
		
		// 找到xor中最右边的1，这个位在两个只出现一次的数字中必然不同
		int rightMostBit = xor & (-xor);
		
		// 根据rightMostBit将数组分为两组分别异或
		int num1 = 0, num2 = 0;
		for (int num : nums) {
			if ((num & rightMostBit) != 0) {
				num1 ^= num;
			} else {
				num2 ^= num;
			}
		}
		
		return new int[]{num1, num2};
	}
	
	/**
	 * LeetCode 137. 只出现一次的数字 II
	 * 题目链接: https://leetcode.cn/problems/single-number-ii/
	 * 题目描述: 给你一个整数数组 nums ，除了某个元素只出现一次外，其余每个元素均出现三次。找出那个只出现了一次的元素。
	 * 
	 * 算法原理:
	 * 使用位运算统计每一位上1出现的次数，对3取模，剩下的就是只出现一次的数字在该位的值
	 * 
	 * 对于32位整数的每一位：
	 * 1. 统计所有数字在该位上1出现的次数
	 * 2. 对次数对3取模
	 * 3. 如果结果不为0，说明只出现一次的数字在该位为1
	 * 
	 * 例如：nums = [2, 2, 3, 2]
	 * 2的二进制: 010
	 * 3的二进制: 011
	 * 第0位：1出现1次，1%3=1，所以结果的第0位为1
	 * 第1位：1出现4次，4%3=1，所以结果的第1位为1
	 * 第2位：1出现0次，0%3=0，所以结果的第2位为0
	 * 结果：011 (二进制) = 3 (十进制)
	 * 
	 * 时间复杂度: O(n) - 需要遍历整个数组一次
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param nums 输入的整数数组
	 * @return 只出现一次的元素
	 */
	public static int singleNumberII(int[] nums) {
		int result = 0;
		// 遍历每一位（32位整数）
		for (int i = 0; i < 32; i++) {
			int count = 0;
			// 统计该位上1出现的次数
			for (int num : nums) {
				// 检查num的第i位是否为1
				if ((num >> i & 1) == 1) {
					count = add(count, 1);
				}
			}
			// 如果该位上1出现的次数不是3的倍数，则说明只出现一次的数字在该位为1
			if (count % 3 != 0) {
				result |= (1 << i);
			}
		}
		return result;
	}
	
	/**
	 * LeetCode 201. 数字范围按位与
	 * 题目链接: https://leetcode.cn/problems/bitwise-and-of-numbers-range/
	 * 题目描述: 给你两个整数 left 和 right ，表示区间 [left, right] ，返回此区间内所有数字 按位与 的结果（包含 left 和 right 端点）。
	 * 
	 * 算法原理:
	 * 找到left和right的最长公共前缀，后面补0
	 * 
	 * 在一个连续的数字范围内，低位的变化会导致按位与的结果为0，
	 * 只有最高位的公共前缀会在最终结果中保留。
	 * 
	 * 例如：left=5, right=7
	 * 5: 101
	 * 6: 110
	 * 7: 111
	 * 5&6&7 = 100 (二进制) = 4 (十进制)
	 * 公共前缀是最高位的1，后面补0
	 * 
	 * 时间复杂度: O(1) - 最多循环32次
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param left 区间左端点
	 * @param right 区间右端点
	 * @return 区间内所有数字按位与的结果
	 */
	public static int rangeBitwiseAnd(int left, int right) {
		int shift = 0;
		// 找到left和right的最长公共前缀
		while (left < right) {
			left >>= 1;
			right >>= 1;
			shift = add(shift, 1);
		}
		// 左移shift位，后面补0
		return left << shift;
	}
	
	/**
	 * LeetCode 389. 找不同
	 * 题目链接: https://leetcode.cn/problems/find-the-difference/
	 * 题目描述: 给定两个字符串 s 和 t ，它们只包含小写字母。字符串 t 由字符串 s 随机重排，然后在随机位置添加一个字母。请找出在 t 中被添加的字母。
	 * 
	 * 算法原理:
	 * 利用异或运算的性质：
	 * 1. 字符串s中的每个字符在t中都会出现一次
	 * 2. 除了被添加的字符外，其他字符都会出现两次
	 * 3. 将两个字符串的所有字符异或，出现两次的字符会相互抵消为0
	 * 4. 最终只剩下被添加的字符
	 * 
	 * 例如：s = "abcd", t = "abcde"
	 * s中字符异或：a^b^c^d
	 * t中字符异或：a^b^c^d^e
	 * 最终结果：(a^b^c^d) ^ (a^b^c^d^e) = e
	 * 
	 * 时间复杂度: O(n) - n是字符串长度
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param s 原始字符串
	 * @param t 添加了一个字符的字符串
	 * @return 被添加的字符
	 */
	public static char findTheDifference(String s, String t) {
		char result = 0;
		// 异或s中的所有字符
		for (char c : s.toCharArray()) {
			result ^= c;
		}
		// 异或t中的所有字符
		for (char c : t.toCharArray()) {
			result ^= c;
		}
		// 最终结果就是被添加的字符
		return result;
	}
	
	/**
	 * LeetCode 78. 子集
	 * 题目链接: https://leetcode.cn/problems/subsets/
	 * 题目描述: 给你一个整数数组 nums ，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
	 * 
	 * 算法原理:
	 * 使用位运算枚举所有可能的子集：
	 * 1. 对于n个元素的数组，共有2^n个子集
	 * 2. 用0到2^n-1的每个数字的二进制表示来表示一个子集
	 * 3. 二进制表示中第j位为1表示包含第j个元素，为0表示不包含
	 * 
	 * 例如：nums = [1, 2, 3]
	 * 0: 000 -> []
	 * 1: 001 -> [1]
	 * 2: 010 -> [2]
	 * 3: 011 -> [1,2]
	 * 4: 100 -> [3]
	 * 5: 101 -> [1,3]
	 * 6: 110 -> [2,3]
	 * 7: 111 -> [1,2,3]
	 * 
	 * 时间复杂度: O(n * 2^n) - 共有2^n个子集，每个子集需要O(n)时间构造
	 * 空间复杂度: O(n) - 除了返回结果外，只使用常数级额外空间
	 * 
	 * @param nums 输入的整数数组
	 * @return 所有可能的子集
	 */
	public static List<List<Integer>> subsets(int[] nums) {
		List<List<Integer>> result = new ArrayList<>();
		int n = nums.length;
		// 枚举从0到2^n-1的所有数，表示所有可能的子集
		for (int i = 0; i < (1 << n); i++) {
			List<Integer> subset = new ArrayList<>();
			for (int j = 0; j < n; j++) {
				// 检查第j位是否为1
				if ((i >> j & 1) == 1) {
					subset.add(nums[j]);
				}
			}
			result.add(subset);
		}
		return result;
	}
	
	/**
	 * LeetCode 2680. 最大或值
	 * 题目链接: https://leetcode.cn/problems/maximum-or/
	 * 题目描述: 给你一个下标从 0 开始长度为 n 的整数数组 nums 和一个整数 k 。每一次操作中，你可以选择一个数并将它乘 2 。你最多可以进行 k 次操作，请你返回 nums[0] | nums[1] | ... | nums[n - 1] 的最大值。
	 * 
	 * 算法原理:
	 * 贪心策略，尽可能让高位变为1，优先选择能使最高位为1的数字进行多次左移
	 * 
	 * 使用前缀和后缀数组优化计算：
	 * 1. prefix[i] 表示 nums[0] 到 nums[i-1] 的或值
	 * 2. suffix[i] 表示 nums[i+1] 到 nums[n-1] 的或值
	 * 3. 对于每个nums[i]左移k位后，结果为 current | prefix[i] | suffix[i]
	 * 
	 * 时间复杂度: O(n^2) - 枚举每个位置作为乘2的主要候选，然后进行最多k次左移
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param nums 输入的整数数组
	 * @param k 最多可以进行的操作次数
	 * @return 最大或值
	 */
	public static long maximumOr(int[] nums, int k) {
		int n = nums.length;
		// 前缀或数组
		long[] prefix = new long[n];
		// 后缀或数组
		long[] suffix = new long[n];
		
		// 计算前缀或
		prefix[0] = 0;
		for (int i = 1; i < n; i++) {
			prefix[i] = prefix[i - 1] | nums[i - 1];
		}
		
		// 计算后缀或
		suffix[n - 1] = 0;
		for (int i = n - 2; i >= 0; i--) {
			suffix[i] = suffix[i + 1] | nums[i + 1];
		}
		
		long maxResult = 0;
		// 枚举每个数字作为可能要进行k次左移的数字
		for (int i = 0; i < n; i++) {
			// 当前数字左移k次后的结果
			long current = (long) nums[i] << k;
			// 与前缀和后缀或操作，得到当前情况下的最大或值
			long currentOr = current | prefix[i] | suffix[i];
			maxResult = Math.max(maxResult, currentOr);
		}
		
		return maxResult;
	}
	
	/**
	 * LeetCode 421. 数组中两个数的最大异或值
	 * 题目链接: https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
	 * 题目描述: 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。
	 * 
	 * 算法原理:
	 * 使用字典树存储所有数字的二进制表示，然后对每个数字贪心地寻找能产生最大异或值的数字
	 * 
	 * 1. 构建字典树：将所有数字的32位二进制表示插入字典树
	 * 2. 对每个数字寻找最大异或值：
	 *    贪心策略：从高位到低位，优先选择与当前位不同的路径
	 *    如果当前位是0，优先选择1的路径；如果是1，优先选择0的路径
	 * 
	 * 时间复杂度: O(n) - 使用字典树优化
	 * 空间复杂度: O(n) - 需要构建字典树
	 * 
	 * @param nums 输入的整数数组
	 * @return 数组中两个数的最大异或值
	 */
	public static int findMaximumXOR(int[] nums) {
		// 构建字典树
		TrieNode root = new TrieNode();
		for (int num : nums) {
			TrieNode node = root;
			for (int i = 31; i >= 0; i--) {
				int bit = (num >> i) & 1;
				if (node.children[bit] == null) {
					node.children[bit] = new TrieNode();
				}
				node = node.children[bit];
			}
		}
		
		// 对每个数字寻找最大异或值
		int maxResult = 0;
		for (int num : nums) {
			TrieNode node = root;
			int currentXOR = 0;
			for (int i = 31; i >= 0; i--) {
				int bit = (num >> i) & 1;
				// 贪心策略：优先选择与当前位不同的路径
				int toggledBit = bit ^ 1;
				if (node.children[toggledBit] != null) {
					currentXOR = (currentXOR << 1) | 1;
					node = node.children[toggledBit];
				} else {
					currentXOR = currentXOR << 1;
					node = node.children[bit];
				}
			}
			maxResult = Math.max(maxResult, currentXOR);
		}
		
		return maxResult;
	}
	
	/**
	 * LeetCode 190. 颠倒二进制位
	 * 题目链接: https://leetcode.cn/problems/reverse-bits/
	 * 题目描述: 颠倒给定的 32 位无符号整数的二进制位
	 * 
	 * 算法原理:
	 * 逐位颠倒，从最低位开始，将每一位移动到对应的高位位置
	 * 
	 * 例如：n = 12 (二进制: 1100)
	 * i=0: result = (0<<1) | (1100&1) = 0 | 0 = 0, n = 0110
	 * i=1: result = (0<<1) | (0110&1) = 0 | 0 = 0, n = 0011
	 * i=2: result = (0<<1) | (0011&1) = 0 | 1 = 1, n = 0001
	 * i=3: result = (1<<1) | (0001&1) = 10 | 1 = 11, n = 0000
	 * ...
	 * 
	 * 时间复杂度: O(1) - 固定32次循环
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param n 输入的32位整数
	 * @return 颠倒二进制位后的结果
	 */
	public static int reverseBits(int n) {
		int result = 0;
		for (int i = 0; i < 32; i++) {
			// 将n的最低位取出，然后左移到对应的高位位置
			result = (result << 1) | (n & 1);
			n >>>= 1; // 无符号右移
		}
		return result;
	}
	
	/**
	 * LeetCode 693. 交替位二进制数
	 * 题目链接: https://leetcode.cn/problems/binary-number-with-alternating-bits/
	 * 题目描述: 给定一个正整数，检查它的二进制表示是否总是 0、1 交替出现
	 * 
	 * 算法原理:
	 * 检查 n ^ (n >> 1) 是否所有位都是1
	 * 
	 * 对于交替位二进制数，右移1位后与原数异或，结果应该是全1
	 * 例如：n = 10 (二进制: 1010)
	 * n>>1 = 0101
	 * n^(n>>1) = 1111 (全1)
	 * 全1的数字加1后是2的幂，与原数相与结果为0
	 * 
	 * 时间复杂度: O(1) - 最多循环32次
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param n 输入的正整数
	 * @return 如果二进制表示是交替位返回true，否则返回false
	 */
	public static boolean hasAlternatingBits(int n) {
		// 将n右移1位后与n异或，如果结果是全1，则说明是交替位
		int xor = n ^ (n >> 1);
		// 检查xor+1是否是2的幂（即xor是否全为1）
		return (xor & (xor + 1)) == 0;
	}
	
	/**
	 * LeetCode 476. 数字的补数
	 * 题目链接: https://leetcode.cn/problems/number-complement/
	 * 题目描述: 对整数的二进制表示取反（0 变 1 ，1 变 0）后，再转换为十进制表示，可以得到这个整数的补数
	 * 
	 * 算法原理:
	 * 找到最高位的1，然后构造掩码，最后异或得到补数
	 * 
	 * 例如：num = 5 (二进制: 101)
	 * 1. 找到最高位的1：mask = 111
	 * 2. 异或得到补数：101 ^ 111 = 010 (十进制: 2)
	 * 
	 * 时间复杂度: O(1) - 固定操作
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param num 输入的整数
	 * @return num的补数
	 */
	public static int findComplement(int num) {
		// 找到最高位的1
		int mask = 1;
		while (mask < num) {
			mask = (mask << 1) | 1;
		}
		// 用掩码异或得到补数
		return num ^ mask;
	}
	
	/**
	 * LeetCode 405. 数字转换为十六进制数
	 * 题目链接: https://leetcode.cn/problems/convert-a-number-to-hexadecimal/
	 * 题目描述: 给定一个整数，编写一个算法将这个数转换为十六进制数
	 * 
	 * 算法原理:
	 * 每4位一组转换为十六进制字符
	 * 
	 * 十六进制中，每4位二进制对应一个十六进制字符：
	 * 0000->0, 0001->1, ..., 1010->a, ..., 1111->f
	 * 
	 * 例如：num = 26 (二进制: 11010)
	 * 第一次：digit = 11010 & 1111 = 1010 (十进制: 10) -> 'a'
	 * num >>>= 4 后，num = 1 (二进制: 1)
	 * 第二次：digit = 0001 & 1111 = 0001 (十进制: 1) -> '1'
	 * num >>>= 4 后，num = 0，循环结束
	 * 结果："a1"，反转后为"1a"
	 * 
	 * 时间复杂度: O(1) - 最多循环8次
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * @param num 输入的整数
	 * @return num的十六进制表示
	 */
	public static String toHex(int num) {
		if (num == 0) return "0";
		
		char[] hexChars = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		StringBuilder sb = new StringBuilder();
		
		while (num != 0) {
			// 取最低4位
			int digit = num & 0xf;
			sb.append(hexChars[digit]);
			num >>>= 4; // 无符号右移4位
		}
		
		return sb.reverse().toString();
	}
	
	// LeetCode 318. 最大单词长度乘积
	// 题目链接: https://leetcode.cn/problems/maximum-product-of-word-lengths/
	// 题目描述: 给定一个字符串数组 words，返回 length(word[i]) * length(word[j]) 的最大值，并且这两个单词不含有公共字母
	// 时间复杂度: O(n² + L) - n是单词数量，L是所有单词的总长度
	// 空间复杂度: O(n) - 存储每个单词的位掩码
	// 解题思路: 使用位掩码表示每个单词包含的字母，然后检查两个单词的位掩码是否有交集
	public static int maxProduct(String[] words) {
		int n = words.length;
		int[] masks = new int[n]; // 存储每个单词的字母位掩码
		int[] lengths = new int[n]; // 存储每个单词的长度
		
		// 预处理每个单词的位掩码
		for (int i = 0; i < n; i++) {
			int mask = 0;
			for (char c : words[i].toCharArray()) {
				mask |= 1 << (c - 'a');
			}
			masks[i] = mask;
			lengths[i] = words[i].length();
		}
		
		int maxProduct = 0;
		// 检查每对单词
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				// 如果两个单词没有公共字母
				if ((masks[i] & masks[j]) == 0) {
					maxProduct = Math.max(maxProduct, lengths[i] * lengths[j]);
				}
			}
		}
		
		return maxProduct;
	}
	
	// LeetCode 393. UTF-8 编码验证
	// 题目链接: https://leetcode.cn/problems/utf-8-validation/
	// 题目描述: 给定一个表示数据的整数数组，返回它是否为有效的 utf-8 编码
	// 时间复杂度: O(n) - 遍历数组一次
	// 空间复杂度: O(1) - 只使用常数级额外空间
	// 解题思路: 根据UTF-8编码规则验证每个字节
	public static boolean validUtf8(int[] data) {
		int count = 0; // 跟踪后续字节的数量
		
		for (int num : data) {
			if (count == 0) {
				// 检查首字节
				if ((num >> 5) == 0b110) { // 2字节字符
					count = 1;
				} else if ((num >> 4) == 0b1110) { // 3字节字符
					count = 2;
				} else if ((num >> 3) == 0b11110) { // 4字节字符
					count = 3;
				} else if ((num >> 7) != 0) { // 无效的首字节
					return false;
				}
			} else {
				// 检查后续字节
				if ((num >> 6) != 0b10) {
					return false;
				}
				count--;
			}
		}
		
		return count == 0; // 所有后续字节都正确匹配
	}
	
	// LeetCode 397. 整数替换
	// 题目链接: https://leetcode.cn/problems/integer-replacement/
	// 题目描述: 给定一个正整数 n ，你可以做如下操作：如果 n 是偶数，则用 n / 2替换 n ；如果 n 是奇数，则可以用 n + 1或n - 1替换 n 。n 变为 1 所需的最小替换次数是多少？
	// 时间复杂度: O(log n) - 每次操作至少减少一半
	// 空间复杂度: O(1) - 只使用常数级额外空间
	// 解题思路: 贪心策略，当n是奇数时，选择能产生更多偶数因子的操作
	public static int integerReplacement(int n) {
		int count = 0;
		long num = n; // 使用long防止溢出
		
		while (num != 1) {
			if ((num & 1) == 0) {
				// 偶数，直接除以2
				num >>= 1;
			} else {
				// 奇数，选择n+1或n-1
				if (num == 3) {
					// 特殊情况：3 -> 2 -> 1 比 3 -> 4 -> 2 -> 1 更优
					num--;
				} else if ((num & 3) == 3) {
					// 如果n+1能被4整除，选择n+1
					num++;
				} else {
					// 否则选择n-1
					num--;
				}
			}
			count++;
		}
		
		return count;
	}
	
	// LeetCode 401. 二进制手表
	// 题目链接: https://leetcode.cn/problems/binary-watch/
	// 题目描述: 二进制手表顶部有 4 个 LED 代表 小时（0-11），底部的 6 个 LED 代表 分钟（0-59）。每个 LED 代表一个 0 或 1，最低位在右侧。给定一个非负整数 n 代表当前 LED 亮着的数量，返回所有可能的时间
	// 时间复杂度: O(1) - 固定枚举12*60种可能
	// 空间复杂度: O(1) - 只使用常数级额外空间
	// 解题思路: 枚举所有可能的小时和分钟组合，检查LED亮灯数量是否等于n
	public static List<String> readBinaryWatch(int turnedOn) {
		List<String> result = new ArrayList<>();
		
		for (int h = 0; h < 12; h++) {
			for (int m = 0; m < 60; m++) {
				// 计算小时和分钟的二进制中1的个数
				if (Integer.bitCount(h) + Integer.bitCount(m) == turnedOn) {
					result.add(String.format("%d:%02d", h, m));
				}
			}
		}
		
		return result;
	}
	
	// LeetCode 477. 汉明距离总和
	// 题目链接: https://leetcode.cn/problems/total-hamming-distance/
	// 题目描述: 两个整数的汉明距离指的是这两个数字的二进制数对应位不同的数量。计算一个数组中任意两个数之间汉明距离的总和
	// 时间复杂度: O(n) - 遍历32位，对每一位统计1和0的数量
	// 空间复杂度: O(1) - 只使用常数级额外空间
	// 解题思路: 对每一位单独计算，该位的总汉明距离 = 1的数量 * 0的数量
	public static int totalHammingDistance(int[] nums) {
		int total = 0;
		int n = nums.length;
		
		// 遍历每一位（32位整数）
		for (int i = 0; i < 32; i++) {
			int countOnes = 0;
			// 统计该位为1的数量
			for (int num : nums) {
				countOnes += (num >> i) & 1;
			}
			// 该位的总汉明距离 = 1的数量 * 0的数量
			total += countOnes * (n - countOnes);
		}
		
		return total;
	}
	
	// LeetCode 868. 二进制间距
	// 题目链接: https://leetcode.cn/problems/binary-gap/
	// 题目描述: 给定一个正整数 n，找到并返回 n 的二进制表示中两个相邻的 1 之间的最长距离
	// 时间复杂度: O(log n) - 遍历n的二进制位
	// 空间复杂度: O(1) - 只使用常数级额外空间
	// 解题思路: 记录上一个1的位置，计算当前1与上一个1的距离
	public static int binaryGap(int n) {
		int maxGap = 0;
		int lastPos = -1; // 上一个1的位置
		int pos = 0; // 当前位置
		
		while (n > 0) {
			if ((n & 1) == 1) {
				if (lastPos != -1) {
					maxGap = Math.max(maxGap, pos - lastPos);
				}
				lastPos = pos;
			}
			pos++;
			n >>= 1;
		}
		
		return maxGap;
	}
	
	// LeetCode 1009. 十进制整数的反码
	// 题目链接: https://leetcode.cn/problems/complement-of-base-10-integer/
	// 题目描述: 每个非负整数 N 都有其二进制表示。例如， 5 可以被表示为二进制 "101"，11 可以用二进制 "1011" 表示，依此类推。注意，除 N = 0 外，任何二进制表示中都不含前导零。二进制的反码表示是将每个 1 改为 0 且每个 0 变为 1。例如，二进制数 "101" 的二进制反码为 "010"。给你一个十进制数 N，请你返回其二进制表示的反码所对应的十进制整数
	// 时间复杂度: O(1) - 固定操作
	// 空间复杂度: O(1) - 只使用常数级额外空间
	// 解题思路: 找到最高位的1，构造掩码，然后异或
	public static int bitwiseComplement(int n) {
		if (n == 0) return 1;
		
		int mask = 1;
		while (mask < n) {
			mask = (mask << 1) | 1;
		}
		
		return n ^ mask;
	}
	
	// LeetCode 1310. 子数组异或查询
	// 题目链接: https://leetcode.cn/problems/xor-queries-for-a-subarray/
	// 题目描述: 有一个正整数数组 arr，现给你一个对应的查询数组 queries，其中 queries[i] = [Li, Ri]。对于每个查询 i，请你计算从 Li 到 Ri 的 XOR 值（即 arr[Li] xor arr[Li+1] xor ... xor arr[Ri]）作为本次查询的结果
	// 时间复杂度: O(n + q) - n是数组长度，q是查询数量
	// 空间复杂度: O(n) - 前缀异或数组
	// 解题思路: 使用前缀异或数组，区间异或 = prefix[R] ^ prefix[L-1]
	public static int[] xorQueries(int[] arr, int[][] queries) {
		int n = arr.length;
		int[] prefixXor = new int[n + 1];
		
		// 构建前缀异或数组
		for (int i = 0; i < n; i++) {
			prefixXor[i + 1] = prefixXor[i] ^ arr[i];
		}
		
		int[] result = new int[queries.length];
		for (int i = 0; i < queries.length; i++) {
			int l = queries[i][0], r = queries[i][1];
			result[i] = prefixXor[r + 1] ^ prefixXor[l];
		}
		
		return result;
	}
	
	// LeetCode 1442. 形成两个异或相等数组的三元组数目
	// 题目链接: https://leetcode.cn/problems/count-triplets-that-can-form-two-arrays-of-equal-xor/
	// 题目描述: 给你一个整数数组 arr 。现需要从数组中取三个下标 i、j 和 k ，其中 (0 <= i < j <= k < arr.length) 。a 和 b 定义如下：a = arr[i] ^ arr[i + 1] ^ ... ^ arr[j - 1]；b = arr[j] ^ arr[j + 1] ^ ... ^ arr[k]；请返回能够令 a == b 成立的三元组 (i, j , k) 的数目
	// 时间复杂度: O(n²) - 优化后的双重循环
	// 空间复杂度: O(n) - 前缀异或数组
	// 解题思路: 利用前缀异或和异或性质，a == b 等价于 arr[i] ^ ... ^ arr[k] == 0
	public static int countTriplets(int[] arr) {
		int n = arr.length;
		int[] prefix = new int[n + 1];
		
		// 构建前缀异或数组
		for (int i = 0; i < n; i++) {
			prefix[i + 1] = prefix[i] ^ arr[i];
		}
		
		int count = 0;
		// 优化后的双重循环
		for (int i = 0; i < n; i++) {
			for (int k = i + 1; k < n; k++) {
				if ((prefix[i] ^ prefix[k + 1]) == 0) {
					// 对于固定的i和k，j可以是i+1到k之间的任意位置
					count += (k - i);
				}
			}
		}
		
		return count;
	}
	
	// LeetCode 1461. 检查一个字符串是否包含所有长度为 K 的二进制子串
	// 题目链接: https://leetcode.cn/problems/check-if-a-string-contains-all-binary-codes-of-size-k/
	// 题目描述: 给你一个二进制字符串 s 和一个整数 k 。如果所有长度为 k 的二进制字符串都是 s 的子串，请返回 true ，否则请返回 false
	// 时间复杂度: O(n) - 滑动窗口遍历字符串
	// 空间复杂度: O(2^k) - 存储所有可能的子串
	// 解题思路: 使用滑动窗口和哈希集合记录所有出现过的长度为k的子串
	public static boolean hasAllCodes(String s, int k) {
		int n = s.length();
		if (n < k) return false;
		
		Set<Integer> seen = new HashSet<>();
		int num = 0;
		int mask = (1 << k) - 1; // 掩码，用于取k位
		
		// 初始化第一个窗口
		for (int i = 0; i < k; i++) {
			num = (num << 1) | (s.charAt(i) - '0');
		}
		seen.add(num);
		
		// 滑动窗口
		for (int i = k; i < n; i++) {
			// 移除最高位，添加新位
			num = ((num << 1) | (s.charAt(i) - '0')) & mask;
			seen.add(num);
		}
		
		return seen.size() == (1 << k);
	}
	
	// LeetCode 1486. 数组异或操作
	// 题目链接: https://leetcode.cn/problems/xor-operation-in-an-array/
	// 题目描述: 给你两个整数，n 和 start 。数组 nums 定义为：nums[i] = start + 2*i（下标从 0 开始）且 n == nums.length 。请返回 nums 中所有元素按位异或（XOR）后得到的结果
	// 时间复杂度: O(n) - 遍历数组一次
	// 空间复杂度: O(1) - 只使用常数级额外空间
	// 解题思路: 直接模拟计算
	public static int xorOperation(int n, int start) {
		int result = 0;
		for (int i = 0; i < n; i++) {
			result ^= (start + 2 * i);
		}
		return result;
	}
	
	// LeetCode 1720. 解码异或后的数组
	// 题目链接: https://leetcode.cn/problems/decode-xored-array/
	// 题目描述: 未知 整数数组 arr 由 n 个非负整数组成。经编码后变为长度为 n - 1 的另一个整数数组 encoded ，其中 encoded[i] = arr[i] XOR arr[i + 1] 。例如，arr = [1,0,2,1] 经编码后得到 encoded = [1,2,3] 。给你编码后的数组 encoded 和原数组 arr 的第一个元素 first（arr[0]）。请解码返回原数组 arr
	// 时间复杂度: O(n) - 遍历数组一次
	// 空间复杂度: O(n) - 结果数组
	// 解题思路: 利用异或性质，arr[i+1] = encoded[i] ^ arr[i]
	public static int[] decode(int[] encoded, int first) {
		int n = encoded.length;
		int[] arr = new int[n + 1];
		arr[0] = first;
		
		for (int i = 0; i < n; i++) {
			arr[i + 1] = encoded[i] ^ arr[i];
		}
		
		return arr;
	}
	
	// LeetCode 1734. 解码异或后的排列
	// 题目链接: https://leetcode.cn/problems/decode-xored-permutation/
	// 题目描述: 给你一个整数数组 perm ，它是前 n 个正整数的排列，且 n 是个 奇数 。它被加密成另一个长度为 n - 1 的整数数组 encoded ，满足 encoded[i] = perm[i] XOR perm[i + 1] 。比方说，如果 perm = [1,3,2] ，那么 encoded = [2,1] 。给你 encoded 数组，请你返回原始数组 perm
	// 时间复杂度: O(n) - 遍历数组两次
	// 空间复杂度: O(n) - 结果数组
	// 解题思路: 利用前n个正整数异或和的性质
	public static int[] decode(int[] encoded) {
		int n = encoded.length + 1;
		int total = 0;
		// 计算1到n的异或和
		for (int i = 1; i <= n; i++) {
			total ^= i;
		}
		
		// 计算encoded中奇数位置的异或和
		int oddXor = 0;
		for (int i = 1; i < encoded.length; i += 2) {
			oddXor ^= encoded[i];
		}
		
		// perm[0] = total ^ oddXor
		int[] perm = new int[n];
		perm[0] = total ^ oddXor;
		
		// 解码剩余元素
		for (int i = 0; i < encoded.length; i++) {
			perm[i + 1] = encoded[i] ^ perm[i];
		}
		
		return perm;
	}
	
	// LeetCode 2220. 转换数字的最少位翻转次数
	// 题目链接: https://leetcode.cn/problems/minimum-bit-flips-to-convert-number/
	// 题目描述: 一次位翻转定义为将数字 x 二进制中的一个位进行翻转操作，即将 0 变成 1 ，或者将 1 变成 0 。比方说，x = 7 ，二进制表示为 111 ，我们可以选择任意一个位（包含没有显示的前导 0）并进行翻转。比方说我们可以翻转最右边一位得到 110 ，或者翻转右边起第二位得到 101 ，或者翻转右边起第三位得到 011 ，等等。给你两个整数 start 和 goal ，请你返回将 start 转变成 goal 的最少位翻转次数
	// 时间复杂度: O(1) - 固定32位比较
	// 空间复杂度: O(1) - 只使用常数级额外空间
	// 解题思路: 计算两个数字的汉明距离
	public static int minBitFlips(int start, int goal) {
		// 计算异或结果中1的个数
		return Integer.bitCount(start ^ goal);
	}
	
	// LeetCode 2275. 按位与结果大于零的最长组合
	// 题目链接: https://leetcode.cn/problems/largest-combination-with-bitwise-and-greater-than-zero/
	// 题目描述: 对数组 nums 执行按位与运算得到的值大于 0 的组合的最大长度
	// 时间复杂度: O(n) - 遍历32位，对每一位统计
	// 空间复杂度: O(1) - 只使用常数级额外空间
	// 解题思路: 对每一位统计该位为1的数字数量，取最大值
	public static int largestCombination(int[] candidates) {
		int maxLength = 0;
		
		// 遍历每一位（24位足够，因为10^7 < 2^24）
		for (int i = 0; i < 24; i++) {
			int count = 0;
			for (int num : candidates) {
				if (((num >> i) & 1) == 1) {
					count++;
				}
			}
			maxLength = Math.max(maxLength, count);
		}
		
		return maxLength;
	}
	
	// LeetCode 2425. 所有数对的异或和
	// 题目链接: https://leetcode.cn/problems/bitwise-xor-of-all-pairings/
	// 题目描述: 给你两个下标从 0 开始的数组 nums1 和 nums2 ，两个数组都只包含非负整数。请你求出另外一个数组 nums3 ，包含 nums1.length x nums2.length 个整数，分别为 nums1 中每个整数和 nums2 中每个整数按位异或（XOR）的结果。请你返回 nums3 中所有整数的异或和
	// 时间复杂度: O(n + m) - 遍历两个数组
	// 空间复杂度: O(1) - 只使用常数级额外空间
	// 解题思路: 利用异或性质，结果只与数组长度的奇偶性有关
	public static int xorAllNums(int[] nums1, int[] nums2) {
		int result = 0;
		int n = nums1.length, m = nums2.length;
		
		// 如果nums2的长度是奇数，则nums1中每个元素都会出现奇数次
		if (m % 2 == 1) {
			for (int num : nums1) {
				result ^= num;
			}
		}
		
		// 如果nums1的长度是奇数，则nums2中每个元素都会出现奇数次
		if (n % 2 == 1) {
			for (int num : nums2) {
				result ^= num;
			}
		}
		
		return result;
	}
	
	// LeetCode 2433. 找出前缀异或的原始数组
	// 题目链接: https://leetcode.cn/problems/find-the-original-array-of-prefix-xor/
	// 题目描述: 给你一个长度为 n 的整数数组 pref 。找出并返回满足下述条件的数组 arr ：pref[i] = arr[0] ^ arr[1] ^ ... ^ arr[i]
	// 时间复杂度: O(n) - 遍历数组一次
	// 空间复杂度: O(n) - 结果数组
	// 解题思路: 利用前缀异或的性质，arr[i] = pref[i] ^ pref[i-1]
	public static int[] findArray(int[] pref) {
		int n = pref.length;
		int[] arr = new int[n];
		arr[0] = pref[0];
		
		for (int i = 1; i < n; i++) {
			arr[i] = pref[i] ^ pref[i - 1];
		}
		
		return arr;
	}
	
	// LeetCode 2527. 查询数组 Xor 美丽值
	// 题目链接: https://leetcode.cn/problems/find-xor-beauty-of-array/
	// 题目描述: 给你一个下标从 0 开始的整数数组 nums 。三个下标 i，j 和 k 的 有效值 定义为 ((nums[i] | nums[j]) & nums[k]) 。数组的 xor 美丽值 是数组中所有满足 0 <= i, j, k < n 的三元组 (i, j, k) 的 有效值 的异或结果
	// 时间复杂度: O(n) - 遍历数组一次
	// 空间复杂度: O(1) - 只使用常数级额外空间
	// 解题思路: 经过数学推导，结果等于所有元素的异或和
	public static int xorBeauty(int[] nums) {
		int result = 0;
		for (int num : nums) {
			result ^= num;
		}
		return result;
	}
	
	// LeetCode 2683. 相邻值的按位异或
	// 题目链接: https://leetcode.cn/problems/neighboring-bitwise-xor/
	// 题目描述: 下标从 0 开始、长度为 n 的数组 derived 可以由同样长度为 n 的原始二进制数组 original 通过以下方式计算得出：对于每个下标 i（0 <= i < n）：如果 i = n - 1 ，那么 derived[i] = original[i] ^ original[0] ；否则 derived[i] = original[i] ^ original[i + 1] 。给你一个数组 derived ，请判断是否存在一个能够生成 derived 的原始二进制数组 original
	// 时间复杂度: O(n) - 遍历数组一次
	// 空间复杂度: O(1) - 只使用常数级额外空间
	// 解题思路: 检查 derived 所有元素的异或和是否为0
	public static boolean doesValidArrayExist(int[] derived) {
		int xor = 0;
		for (int num : derived) {
			xor ^= num;
		}
		return xor == 0;
	}
	
	// LeetCode 2997. 使数组异或和等于 K 的最少操作次数
	// 题目链接: https://leetcode.cn/problems/minimum-number-of-operations-to-make-array-xor-equal-to-k/
	// 题目描述: 给你一个下标从 0 开始的整数数组 nums 和一个正整数 k 。你可以对数组执行以下操作任意次：选择数组里的任意元素，并将它的二进制表示中的任意一位翻转一次，翻转操作指将 0 变成 1 或 1 变成 0 。你的目标是让数组所有元素的异或和等于 k 。请你返回达成目标所需的最少操作次数
	// 时间复杂度: O(n) - 遍历数组一次
	// 空间复杂度: O(1) - 只使用常数级额外空间
	// 解题思路: 计算当前异或和与目标k的汉明距离
	public static int minOperations(int[] nums, int k) {
		int currentXor = 0;
		for (int num : nums) {
			currentXor ^= num;
		}
		
		// 需要翻转的位数就是汉明距离
		return Integer.bitCount(currentXor ^ k);
	}
	
	// 字典树节点类，用于LeetCode 421
	static class TrieNode {
		TrieNode[] children = new TrieNode[2]; // 0和1两个子节点
	}
	
	// 测试主方法
	public static void main(String[] args) {
		System.out.println("Bit Operation Test:");
		
		// 测试加法
		System.out.println("Addition Test:");
		System.out.println("10 + 15 = " + add(10, 15));
		System.out.println("(-10) + 15 = " + add(-10, 15));
		
		// 测试减法
		System.out.println("Subtraction Test:");
		System.out.println("15 - 10 = " + minus(15, 10));
		System.out.println("10 - 15 = " + minus(10, 15));
		
		// 测试乘法
		System.out.println("Multiplication Test:");
		System.out.println("10 * 15 = " + multiply(10, 15));
		System.out.println("(-10) * 15 = " + multiply(-10, 15));
		
		// 测试除法
		System.out.println("Division Test:");
		System.out.println("15 / 10 = " + divide(15, 10));
		System.out.println("15 / (-10) = " + divide(15, -10));
		System.out.println("(-15) / (-10) = " + divide(-15, -10));
		
		// 测试其他位运算相关函数
		System.out.println("Other Bit Operation Tests:");
		System.out.println("Hamming Weight (15): " + hammingWeight(15));
		System.out.println("Is Power of Two (16): " + isPowerOfTwo(16));
		System.out.println("Is Power of Two (15): " + isPowerOfTwo(15));
		System.out.println("Hamming Distance (1, 4): " + hammingDistance(1, 4));
		
		// 测试数组相关函数
		int[] nums1 = {2, 2, 1};
		System.out.println("Single Number ([2, 2, 1]): " + singleNumber(nums1));
		
		int[] nums2 = {3, 0, 1};
		System.out.println("Missing Number ([3, 0, 1]): " + missingNumber(nums2));
		
		int[] nums3 = {1, 2, 1, 3, 2, 5};
		int[] result = singleNumberIII(nums3);
		System.out.println("Single Number III ([1, 2, 1, 3, 2, 5]): " + result[0] + ", " + result[1]);
		
		System.out.println("Test Completed!");
	}
}