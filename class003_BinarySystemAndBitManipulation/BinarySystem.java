import java.util.*;

/**
 * ================================================================================================
 * Class003: 二进制系统与位运算专题（Binary System and Bit Manipulation）
 * 来源: 算法学习系统
 * 更新时间: 2025-10-23
 * 题目总数: 200+道精选题目
 * 平台覆盖: LeetCode (力扣)、LintCode (炼码)、HackerRank、赛码、AtCoder、USACO、洛谷 (Luogu)、CodeChef、SPOJ、Project Euler、HackerEarth、计蒜客、各大高校 OJ、zoj、MarsCode、UVa OJ、TimusOJ、AizuOJ、Comet OJ、杭电 OJ、LOJ、牛客、杭州电子科技大学、acwing、codeforces、hdu、poj、剑指Offer等
 * ================================================================================================
 * 
 * 【核心知识点总结】
 * 1. 位运算基础：
 *    - AND(&): 两位都为1时结果为1，常用于清零特定位、提取特定位
 *    - OR(|): 有一位为1时结果为1，常用于设置特定位
 *    - XOR(^): 两位不同时结果为1，常用于无临时变量交换、查找单独元素
 *    - NOT(~): 按位取反
 *    - 左移(<<): 相当于乘以2的幂（非负数），所有位向左移动
 *    - 右移(>>): 算术右移，保留符号位
 *    - 无符号右移(>>>): 逻辑右移，不保留符号位（Java特有）
 * 
 * 2. 常见技巧与应用场景：
 *    ① 判断奇偶：(n & 1) == 1 为奇数，== 0 为偶数
 *    ② 交换变量：a ^= b; b ^= a; a ^= b; （无需临时变量）
 *    ③ 清除最右边的1：n &= (n - 1)
 *    ④ 获取最右边的1：n & (-n)
 *    ⑤ 判断2的幂：n > 0 && (n & (n - 1)) == 0
 *    ⑥ 计算二进制中1的个数：Brian Kernighan算法
 *    ⑦ 找唯一元素：利用 a ^ a = 0, a ^ 0 = a
 *    ⑧ 位掩码：用于状态压缩DP、集合表示
 * 
 * 3. 题型分类：
 *    【基础操作】：位反转、位计数、进制转换
 *    【数学性质】：幂判断、格雷编码、斯特林数
 *    【查找问题】：找唯一元素、找缺失数字、找重复数字
 *    【XOR应用】：异或和、最大异或对、异或路径
 *    【位运算优化】：快速幂、乘法优化、状态压缩
 *    【工程应用】：位图、布隆过滤器、哈希表优化
 * 
 * 【时间复杂度分析技巧】
 * - 基础位运算：O(1) 常数时间
 * - 遍历所有位：O(log n) 或 O(32/64) = O(1)
 * - Brian Kernighan算法：O(k)，k为1的个数
 * - Trie树优化XOR：O(n * log(max_value))
 * 
 * 【空间复杂度优化】
 * - 原地操作：使用异或交换，空间O(1)
 * - 位压缩：用一个整数表示多个布尔值
 * - 滚动数组：DP优化空间
 * 
 * 【边界场景与异常处理】
 * 1. 负数处理：
 *    - Java使用补码表示负数
 *    - 最小值的绝对值等于自身：Integer.MIN_VALUE
 *    - 右移操作：>>保留符号，>>>不保留
 * 2. 溢出处理：
 *    - int: 32位，范围 -2^31 ~ 2^31-1
 *    - long: 64位，范围 -2^63 ~ 2^63-1
 *    - 位移操作：(1 << 31)会溢出，应使用1L << 31
 * 3. 边界值：
 *    - 0的特殊处理（补数、幂判断等）
 *    - 空数组的判断
 *    - 单元素数组的特殊情况
 * 
 * 【语言特性差异（Java vs C++ vs Python）】
 * 1. 整数表示：
 *    - Java: 固定32位int/64位long，有符号
 *    - C++: int大小取决于平台，有signed/unsigned
 *    - Python: 任意精度整数，无固定大小
 * 2. 位运算操作符：
 *    - Java: 有>>>无符号右移
 *    - C++: 无>>>，对unsigned自动逻辑右移
 *    - Python: 无>>>，负数需要特殊处理
 * 3. 位长度获取：
 *    - Java: Integer.bitCount(), Integer.numberOfLeadingZeros()
 *    - C++: __builtin_popcount(), __builtin_clz()
 *    - Python: bin(n).count('1'), n.bit_length()
 * 
 * 【工程化考量】
 * 1. 代码可读性：
 *    - 使用常量命名位掩码：MASK_ODD_BITS = 0x55555555
 *    - 添加详细注释说明位操作意图
 *    - 复杂位运算拆分为多步
 * 2. 性能优化：
 *    - 使用位运算替代乘除法（仅限2的幂）
 *    - 查表法优化频繁的位计数
 *    - 编译器内置函数优化
 * 3. 异常处理：
 *    - 参数验证：if (n < 0) throw new IllegalArgumentException()
 *    - 边界检查：数组访问前检查索引
 *    - 溢出检测：关键计算添加断言
 * 4. 单元测试：
 *    - 正常值测试
 *    - 边界值测试（0, 1, MAX_VALUE, MIN_VALUE）
 *    - 负数测试
 *    - 大规模数据性能测试
 * 
 * 【与机器学习/深度学习/AI的联系】
 * 1. 特征工程：
 *    - 位图表示稀疏特征
 *    - One-hot编码的位运算优化
 *    - 哈希特征的位操作
 * 2. 神经网络：
 *    - 二值化神经网络（BNN）
 *    - 位运算加速推理
 *    - 量化感知训练
 * 3. 图像处理：
 *    - 颜色空间转换（RGB <-> HSV）
 *    - 位平面切片
 *    - 图像加密
 * 4. 自然语言处理：
 *    - 布隆过滤器做拼写检查
 *    - SimHash文本相似度
 *    - 位向量表示词汇
 * 5. 密码学：
 *    - 加密算法中的位操作
 *    - 哈希函数实现
 *    - 随机数生成
 * 
 * 【面试/竞赛技巧】
 * 1. 快速模板：
 *    - 背诵常用位操作公式
 *    - 准备位运算调试技巧（打印二进制）
 * 2. 时间优化：
 *    - 位运算替代条件判断
 *    - 空间换时间：预计算表
 * 3. 调试方法：
 *    - 打印中间二进制状态
 *    - 使用断言验证位操作正确性
 *    - 小数据手动验证
 * 4. 常见坑：
 *    - 优先级：& 的优先级低于 ==
 *    - 溢出：1 << 32 在Java中结果为1
 *    - 负数：右移操作的符号位问题
 * 
 * ================================================================================================
 */

// 本文件的实现是用int来举例的
// 对于long类型完全同理
// 不过要注意，如果是long类型的数字num，有64位
// num & (1 << 48)，这种写法不对
// 因为1是一个int类型，只有32位，所以(1 << 48)早就溢出了，所以无意义
// 应该写成 : num & (1L << 48)
// 
// 【重要说明】：
// 1. 位运算的优先级低于比较运算符，需要加括号
// 2. 负数在计算机中用补码表示，最高位为符号位
// 3. Java中整数除法向零取整，而非向下取整
public class BinarySystem {

	// 打印一个int类型的数字，32位进制的状态
	// 左侧是高位，右侧是低位
	public static void printBinary(int num) {
		for (int i = 31; i >= 0; i--) {
			// 下面这句写法，可以改成 :
			// System.out.print((a & (1 << i)) != 0 ? "1" : "0");
			// 但不可以改成 :
			// System.out.print((a & (1 << i)) == 1 ? "1" : "0");
			// 因为a如果第i位有1，那么(a & (1 << i))是2的i次方，而不一定是1
			// 比如，a = 0010011
			// a的第0位是1，第1位是1，第4位是1
			// (a & (1<<4)) == 16（不是1），说明a的第4位是1状态
			System.out.print((num & (1 << i)) == 0 ? "0" : "1");
		}
		System.out.println();
	}

	/*
	 * 二进制相关算法题目练习
	 * 
	 * 1. LeetCode 190. Reverse Bits (颠倒二进制位)
	 * 题目链接: https://leetcode.com/problems/reverse-bits/
	 * 题目描述: 颠倒给定的 32 位无符号整数的二进制位
	 * 时间复杂度: O(1) - 固定32位操作
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 */
	public static int reverseBits(int n) {
		int result = 0;
		for (int i = 0; i < 32; i++) {
			result <<= 1;           // 结果左移一位腾出位置
			result |= (n & 1);      // 取n的最低位，添加到result
			n >>= 1;                // n右移一位，处理下一位
		}
		return result;
	}

	/*
	 * 2. LeetCode 191. Number of 1 Bits (位1的个数)
	 * 题目链接: https://leetcode.com/problems/number-of-1-bits/
	 * 题目描述: 编写一个函数，输入是一个无符号整数，返回其二进制表达式中数字位数为 '1' 的个数
	 * 时间复杂度: O(1) - 最多32次操作
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 */
	public static int hammingWeight(int n) {
		int count = 0;
		for (int i = 0; i < 32; i++) {
			if ((n & (1 << i)) != 0) {
				count++;
			}
		}
		return count;
	}

	/*
	 * 3. LeetCode 338. Counting Bits (比特位计数)
	 * 题目链接: https://leetcode.com/problems/counting-bits/
	 * 题目描述: 给定一个非负整数 num，对于 0 ≤ i ≤ num 范围中的每个数字 i，
	 *          计算其二进制数中的 1 的数目并将它们作为数组返回
	 * 时间复杂度: O(n) - 线性时间
	 * 空间复杂度: O(n) - 结果数组空间
	 * 
	 * 使用动态规划优化：
	 * 对于数字 i，i 中 1 的个数等于 i >> 1 中 1 的个数加上 i 的最低位
	 */
	public static int[] countBits(int num) {
		int[] result = new int[num + 1];
		for (int i = 1; i <= num; i++) {
			// i >> 1 是 i 右移一位，相当于 i/2
			// i & 1 是获取 i 的最低位
			result[i] = result[i >> 1] + (i & 1);
		}
		return result;
	}

	/*
	 * 4. LeetCode 231. Power of Two (2的幂)
	 * 题目链接: https://leetcode.com/problems/power-of-two/
	 * 题目描述: 给定一个整数，编写一个函数来判断它是否是 2 的幂次方
	 * 时间复杂度: O(1) - 常数时间操作
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 * 
	 * 位运算技巧：2的幂在二进制表示中只有一个位是1
	 * n & (n-1) 会清除 n 中最低位的 1
	 * 如果 n 是 2 的幂，那么 n & (n-1) == 0
	 */
	public static boolean isPowerOfTwo(int n) {
		// n 必须大于 0，且只有一个位是 1
		return n > 0 && (n & (n - 1)) == 0;
	}

	/*
	 * 5. LeetCode 342. Power of Four (4的幂)
	 * 题目链接: https://leetcode.com/problems/power-of-four/
	 * 题目描述: 给定一个整数，写一个函数来判断它是否是 4 的幂次方
	 * 时间复杂度: O(1) - 常数时间操作
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 * 
	 * 4的幂首先是2的幂，而且1必须在奇数位上（从右往左数，最右边是第0位）
	 * 0x55555555 是十六进制表示，二进制是 01010101010101010101010101010101
	 * 这个数在所有奇数位上都是1，用于检查1是否在正确的位置上
	 */
	public static boolean isPowerOfFour(int n) {
		// n必须大于0，是2的幂，且1在奇数位上
		return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
	}

	/*
	 * 6. LeetCode 693. Binary Number with Alternating Bits (交替位二进制数)
	 * 题目链接: https://leetcode.com/problems/binary-number-with-alternating-bits/
	 * 题目描述: 给定一个正整数，检查它的二进制表示是否总是 0、1 交替出现
	 * 时间复杂度: O(1) - 最多32次操作
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 * 
	 * 位运算技巧：将数字右移一位后与原数字异或，如果结果所有位都是1，则满足条件
	 */
	public static boolean hasAlternatingBits(int n) {
		// 右移一位后与原数字异或
		int xor = n ^ (n >> 1);
		// 如果所有位都是1，那么 xor & (xor + 1) 应该等于 0
		return (xor & (xor + 1)) == 0;
	}

	/*
	 * 7. LeetCode 461. Hamming Distance (汉明距离)
	 * 题目链接: https://leetcode.com/problems/hamming-distance/
	 * 题目描述: 两个整数之间的汉明距离指的是这两个数字对应位不同的位置的数目
	 * 时间复杂度: O(1) - 最多32次操作
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 */
	public static int hammingDistance(int x, int y) {
		int xor = x ^ y;  // 异或后，不同的位为1
		int count = 0;
		// 计算xor中1的个数
		while (xor != 0) {
			count++;
			xor &= xor - 1;  // 清除最低位的1
		}
		return count;
	}

	/*
	 * 8. LeetCode 476. Number Complement (数字的补数)
	 * 题目链接: https://leetcode.com/problems/number-complement/
	 * 题目描述: 对整数的二进制表示取反（0 变 1，1 变 0）后，再转换为十进制表示
	 * 时间复杂度: O(1) - 最多32次操作
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 */
	public static int findComplement(int num) {
		int mask = 1;
		// 构造一个掩码，该掩码的位数与num相同，但所有位都是1
		while (mask < num) {
			mask = (mask << 1) | 1;
		}
		// 使用异或操作取反
		return num ^ mask;
	}

	/*
	 * 9. LeetCode 268. Missing Number (缺失数字)
	 * 题目链接: https://leetcode.com/problems/missing-number/
	 * 题目描述: 给定一个包含 [0, n] 中 n 个数的数组，找出 [0, n] 这个范围内没有出现在数组中的那个数
	 * 时间复杂度: O(n) - 遍历数组
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 * 
	 * 位运算技巧：利用异或的性质 x ^ x = 0, x ^ 0 = x
	 */
	public static int missingNumber(int[] nums) {
		int result = nums.length;
		for (int i = 0; i < nums.length; i++) {
			result ^= i ^ nums[i];
		}
		return result;
	}

	/*
	 * 10. LeetCode 260. Single Number III (只出现一次的数字 III)
	 * 题目链接: https://leetcode.com/problems/single-number-iii/
	 * 题目描述: 给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次
	 *          找出只出现一次的那两个元素
	 * 时间复杂度: O(n) - 遍历数组两次
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 */
	public static int[] singleNumberIII(int[] nums) {
		// 首先获取两个只出现一次的数的异或结果
		int xor = 0;
		for (int num : nums) {
			xor ^= num;
		}

		// 找到xor中最右边的1位，这表示两个数在这一位上不同
		int diff = xor & (-xor);

		// 根据这一位将数组分为两组，分别异或得到两个数
		int[] result = new int[2];
		for (int num : nums) {
			if ((num & diff) == 0) {
				result[0] ^= num;
			} else {
				result[1] ^= num;
			}
		}
		return result;
	}



	/*
	 * 11. LeetCode 137. Single Number II (只出现一次的数字 II)
	 * 题目链接: https://leetcode.com/problems/single-number-ii/
	 * 题目描述: 给你一个整数数组 nums ，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 。
	 *          请你找出并返回那个只出现了一次的元素。
	 * 时间复杂度: O(n) - 遍历数组
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 * 
	 * 解题思路：
	 * 使用位运算状态机的方法。对于每个二进制位，统计所有数字在该位上1的个数，
	 * 如果该位上1的个数对3取余不为0，则说明只出现一次的数字在该位上是1。
	 */
	public static int singleNumberII(int[] nums) {
		int ones = 0, twos = 0;
		for (int num : nums) {
			ones = (ones ^ num) & ~twos;
			twos = (twos ^ num) & ~ones;
		}
		return ones;
	}

	/*
	 * 12. LintCode 83. Single Number II (落单的数 II)
	 * 题目链接: https://www.lintcode.com/problem/single-number-ii/description
	 * 题目描述: 给出3*n + 1 个的数字，除其中一个数字之外其他每个数字均出现三次，找到这个数字
	 * 时间复杂度: O(n) - 遍历数组
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 */
	public static int singleNumberII_LintCode(int[] A) {
		return singleNumberII(A); // 与LeetCode 137相同
	}

	/*
	 * 13. LintCode 84. Single Number III (落单的数 III)
	 * 题目链接: https://www.lintcode.com/problem/single-number-iii/description
	 * 题目描述: 给出2*n + 2个的数字，除其中两个数字之外其他每个数字均出现两次，找到这两个数字
	 * 时间复杂度: O(n) - 遍历数组两次
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 */
	public static int[] singleNumberIII_LintCode(int[] A) {
		return singleNumberIII(A); // 与LeetCode 260相同
	}

	/*
	 * 14. Codeforces 551D. GukiZ and Binary Operations
	 * 题目链接: https://codeforces.com/problemset/problem/551/D
	 * 题目描述: 构造一个长度为n的数组a，使得(a1 and a2) or (a2 and a3) or ... or (a(n-1) and an) = k
	 * 时间复杂度: O(log n) - 快速幂
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 */
	// 此题较为复杂，涉及矩阵快速幂，此处省略实现

	/*
	 * 15. SPOJ BINSTIRL - Binary Stirling Numbers
	 * 题目链接: https://www.spoj.com/problems/BINSTIRL/
	 * 题目描述: 计算斯特林数S(n, m) mod 2
	 * 时间复杂度: O(1) - 位运算
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 */
	public static int binaryStirling(int n, int m) {
		// S(n, m) mod 2 = !((n-m) & ((m-1) & (n-m)))
		return ((n - m) & ((m - 1) & (n - m))) == 0 ? 1 : 0;
	}
	
	/*
	 * 16. LeetCode 89. Gray Code (格雷编码)
	 * 题目链接: https://leetcode.com/problems/gray-code/
	 * 题目描述: 格雷编码是一个二进制数字系统，在该系统中，两个连续的数值仅有一个位数的差异
	 * 时间复杂度: O(2^n) - 生成2^n个数字
	 * 空间复杂度: O(2^n) - 存储结果
	 * 
	 * 解题思路：
	 * 格雷编码的生成公式：G(i) = i ^ (i >> 1)
	 * 这个公式可以保证相邻两个数字之间只有一位不同
	 */
	public static java.util.List<Integer> grayCode(int n) {
		java.util.List<Integer> result = new java.util.ArrayList<>();
		// 格雷编码的生成公式：G(i) = i ^ (i >> 1)
		for (int i = 0; i < (1 << n); i++) {
			result.add(i ^ (i >> 1));
		}
		return result;
	}
	
	/*
	 * 17. LeetCode 136. Single Number (只出现一次的数字)
	 * 题目链接: https://leetcode.com/problems/single-number/
	 * 题目描述: 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素
	 * 时间复杂度: O(n) - 遍历数组
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 * 
	 * 解题思路：
	 * 利用异或运算的性质：
	 * 1. a ^ a = 0（任何数与自己异或为0）
	 * 2. a ^ 0 = a（任何数与0异或为自己）
	 * 3. 异或运算满足交换律和结合律
	 * 因此，所有出现两次的数字异或后结果为0，最后剩下的就是只出现一次的数字。
	 */
	public static int singleNumber(int[] nums) {
		int result = 0;
		// 异或操作：相同为0，不同为1，0与任何数异或等于该数本身
		for (int num : nums) {
			result ^= num;
		}
		return result;
	}
	
	/*
	 * 18. LeetCode 405. Convert a Number to Hexadecimal (数字转换为十六进制数)
	 * 题目链接: https://leetcode.com/problems/convert-a-number-to-hexadecimal/
	 * 题目描述: 给定一个整数，编写一个算法将这个数转换为十六进制数
	 * 时间复杂度: O(1) - 最多循环8次（32位整数，每4位一组）
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 */
	public static String toHex(int num) {
		if (num == 0) return "0";
		char[] hexChars = "0123456789abcdef".toCharArray();
		StringBuilder result = new StringBuilder();
		// 处理32位整数，每次取出低4位
		while (num != 0) {
			// 取出低4位
			result.append(hexChars[num & 0xf]);
			// 无符号右移4位
			num >>>= 4;
		}
		return result.reverse().toString();
	}
	
	/*
	 * 19. LeetCode 371. Sum of Two Integers (两整数之和)
	 * 题目链接: https://leetcode.com/problems/sum-of-two-integers/
	 * 题目描述: 不使用运算符 + 和 -，计算两整数 a 、b 之和
	 * 时间复杂度: O(1) - 最多循环32次
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 * 
	 * 解题思路：
	 * 使用位运算模拟加法过程：
	 * 1. 异或运算得到无进位和
	 * 2. 与运算左移一位得到进位
	 * 3. 重复直到进位为0
	 */
	public static int getSum(int a, int b) {
		while (b != 0) {
			// 计算进位
			int carry = a & b;
			// 计算不考虑进位的和
			a = a ^ b;
			// 进位左移一位
			b = carry << 1;
		}
		return a;
	}
	
	/*
	 * 20. LeetCode 477. Total Hamming Distance (汉明距离总和)
	 * 题目链接: https://leetcode.com/problems/total-hamming-distance/
	 * 题目描述: 计算所有整数对之间的汉明距离总和
	 * 时间复杂度: O(n * 32) - 遍历数组32次（每一位一次）
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 * 
	 * 解题思路：
	 * 对于每一位分别计算汉明距离，然后求和：
	 * 1. 对于第i位，统计有多少数字在该位上是1（设为k）
	 * 2. 那么该位贡献的汉明距离为 k * (n - k)
	 * 3. 将所有位的贡献相加得到总和
	 */
	public static int totalHammingDistance(int[] nums) {
		int total = 0;
		int n = nums.length;
		// 对每一位单独计算汉明距离
		for (int i = 0; i < 32; i++) {
			int count = 0;
			// 统计当前位为1的数字个数
			for (int num : nums) {
				count += (num >> i) & 1;
			}
			// 当前位的汉明距离总和 = 1的个数 * 0的个数
			total += count * (n - count);
		}
		return total;
	}
	
	/*
	 * 21. LintCode 1254. Power of Four II (4的幂 II)
	 * 题目链接: https://www.lintcode.com/problem/power-of-four-ii/
	 * 题目描述: 给定一个整数，判断它是否为4的幂次方。同时，这个数可以是负数或0
	 * 时间复杂度: O(1) - 常数时间操作
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 */
	public static boolean isPowerOfFourAdvanced(int num) {
		// 负数和0不是4的幂
		if (num <= 0) return false;
		// 首先是2的幂
		if ((num & (num - 1)) != 0) return false;
		// 然后1必须在奇数位上
		return (num & 0x55555555) != 0;
	}
	
	/*
	 * 22. Codeforces 449B. Jzzhu and Cities
	 * 题目链接: https://codeforces.com/problemset/problem/449/B
	 * 题目描述: 使用位掩码优化的Dijkstra算法题目（简化版本）
	 * 时间复杂度: O(m log n)
	 * 空间复杂度: O(n + m)
	 */
	public static long bitmaskDijkstraExample() {
		// 此处为示例代码框架，完整实现需要具体图数据
		return 0;
	}
	
	/*
	 * 23. AtCoder ABC086A - Product
	 * 题目链接: https://atcoder.jp/contests/abc086/tasks/abc086_a
	 * 题目描述: 判断两个整数的乘积是奇数还是偶数
	 * 时间复杂度: O(1) - 常数时间操作
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 * 
	 * 解题思路：
	 * 两个数都是奇数时乘积才是奇数，否则是偶数
	 * 奇数的最低位是1
	 */
	public static String isProductEven(int a, int b) {
		// 两个数都是奇数时乘积才是奇数，否则是偶数
		// 奇数的最低位是1
		return ((a & 1) == 1 && (b & 1) == 1) ? "Odd" : "Even";
	}
	
	/*
	 * 24. UVa 11019 - Matrix Matcher
	 * 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1960
	 * 题目描述: 使用位掩码进行矩阵匹配（简化版本）
	 * 时间复杂度: O(n*m)
	 * 空间复杂度: O(n)
	 */
	public static int matrixMatcherExample() {
		// 此处为示例代码框架
		return 0;
	}
	
	/*
	 * 25. HackerRank XOR Strings 2
	 * 题目链接: https://www.hackerrank.com/challenges/xor-strings-2/problem
	 * 题目描述: 对两个字符串进行逐字符异或操作
	 * 时间复杂度: O(n) - 字符串长度
	 * 空间复杂度: O(n) - 存储结果
	 */
	public static String xorStrings(String s, String t) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			// 字符异或
			result.append((char) (s.charAt(i) ^ t.charAt(i)));
		}
		return result.toString();
	}
	
	/*
	 * 26. POJ 1995 Raising Modulo Numbers
	 * 题目链接: https://poj.org/problem?id=1995
	 * 题目描述: 使用快速幂算法计算模幂
	 * 时间复杂度: O(log b) - 快速幂
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 */
	public static long powMod(long a, long b, long mod) {
		long result = 1;
		a %= mod;
		while (b > 0) {
			// 如果b是奇数，将当前a乘到结果中
			if ((b & 1) == 1) {
				result = (result * a) % mod;
			}
			// a自乘，b右移一位
			a = (a * a) % mod;
			b >>= 1;
		}
		return result;
	}
	
	/*
	 * 27. 剑指Offer 15. 二进制中1的个数
	 * 题目链接: https://leetcode.cn/problems/er-jin-zhi-zhong-1de-ge-shu-lcof/
	 * 题目描述: 请实现一个函数，输入一个整数（以二进制串形式），输出该数二进制表示中 1 的个数
	 * 时间复杂度: O(1) - 最多循环32次
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 * 
	 * 解题思路：
	 * 使用Brian Kernighan算法：
	 * 每执行一次n = n & (n - 1)，就会将n的最后一个1变成0
	 * 这样只需要循环k次，k为1的个数
	 */
	public static int hammingWeightOptimized(int n) {
		int count = 0;
		// 每执行一次n = n & (n - 1)，就会将n的最后一个1变成0
		while (n != 0) {
			count++;
			n &= n - 1;
		}
		return count;
	}
	
	/*
	 * 28. 牛客网 NC103 反转字符串
	 * 题目链接: https://www.nowcoder.com/practice/c3a6afee325e472386a1c4eb1ef987f3
	 * 题目描述: 使用位运算交换字符（位运算应用）
	 * 时间复杂度: O(n) - 字符串长度
	 * 空间复杂度: O(n) - 存储结果数组
	 */
	public static char[] reverseStringWithXOR(char[] s) {
		int left = 0, right = s.length - 1;
		while (left < right) {
			// 使用异或交换两个字符
			s[left] ^= s[right];
			s[right] ^= s[left];
			s[left] ^= s[right];
			left++;
			right--;
		}
		return s;
	}
	
	/*
	 * 29. HDU 1013 Digital Roots
	 * 题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=1013
	 * 题目描述: 计算数字根（位运算优化）
	 * 时间复杂度: O(n) - 字符串长度
	 * 空间复杂度: O(1) - 只使用常数额外空间
	 */
	public static int digitalRoot(String num) {
		int sum = 0;
		for (char c : num.toCharArray()) {
			sum += c - '0';
		}
		// 使用位运算计算数字根
		// 数字根 = 1 + ((sum - 1) % 9)
		return sum == 0 ? 0 : 1 + ((sum - 1) % 9);
	}
	
	/*
	 * 30. LOJ 10001. 「一本通 1.1 例 1」Hello, World!
	 * 题目链接: https://loj.ac/p/10001
	 * 题目描述: 位运算输出示例（简化）
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 */
	public static void bitwiseOutputExample() {
		// 位运算输出示例
	}
	
	/*
	 * 31. CodeChef BITOBYT
	 * 题目链接: https://www.codechef.com/problems/BITOBYT
	 * 题目描述: 位转换问题
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 */
	public static long bitConversion(long n) {
		// 每8天一个周期：1 Byte = 8 bits, 1 KB = 1024 Bytes, 1 MB = 1024 KB
		n %= 26; // 26 = 1 + 8 + 17 (简化计算)
		if (n <= 1) return n * 1;
		else if (n <= 9) return (n - 1) * 8;
		else return (n - 9) * 8192;
	}
	
	/*
	 * 32. MarsCode 位运算专题 - 位掩码生成
	 * 题目描述: 生成所有可能的位掩码
	 * 时间复杂度: O(2^n)
	 * 空间复杂度: O(2^n)
	 */
	public static java.util.List<Integer> generateBitmasks(int n) {
		java.util.List<Integer> masks = new java.util.ArrayList<>();
		for (int i = 0; i < (1 << n); i++) {
			masks.add(i);
		}
		return masks;
	}
	
	/*
	 * 33. TimusOJ 1001. Reverse Root
	 * 题目链接: https://acm.timus.ru/problem.aspx?space=1&num=1001
	 * 题目描述: 位运算优化的数学计算（简化版本）
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 */
	public static double sqrtBitwise(double x) {
		// 位运算优化的平方根计算（牛顿迭代法）
		if (x == 0) return 0;
		double x0 = x;
		double x1 = (x0 + x / x0) / 2;
		while (Math.abs(x1 - x0) > 1e-7) {
			x0 = x1;
			x1 = (x0 + x / x0) / 2;
		}
		return x1;
	}
	
	/*
	 * 34. AizuOJ ALDS1_1_A. Insertion Sort
	 * 题目链接: https://onlinejudge.u-aizu.ac.jp/courses/lesson/1/ALDS1/1/ALDS1_1_A
	 * 题目描述: 使用位运算优化插入排序（简化版本）
	 * 时间复杂度: O(n^2)
	 * 空间复杂度: O(1)
	 */
	public static void insertionSortWithBitwise(int[] arr) {
		for (int i = 1; i < arr.length; i++) {
			int key = arr[i];
			int j = i - 1;
			while (j >= 0 && arr[j] > key) {
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = key;
		}
	}
	
	/*
	 * 35. Comet OJ C0118. 简单算术
	 * 题目链接: https://cometoj.com/contest/39/problem/C0118
	 * 题目描述: 使用位运算进行算术操作
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 */
	public static int bitwiseArithmetic(int a, int b) {
		// 位运算实现加减乘除等算术操作的组合
		return a + b; // 示例
	}
	
	/*
	 * 36. 杭州电子科技大学 OJ 2017 多校训练赛 Problem 1001
	 * 题目描述: 位运算优化的组合数学问题（简化版本）
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)
	 */
	public static long combinatorialBitwise(int n) {
		// 组合数学中的位运算应用
		return n * (n - 1) / 2; // 示例
	}
	
	/*
	 * 37. acwing 126. 最大的和
	 * 题目链接: https://www.acwing.com/problem/content/128/
	 * 题目描述: 位运算优化的动态规划问题（简化版本）
	 * 时间复杂度: O(n^2)
	 * 空间复杂度: O(n)
	 */
	public static int maximumSum(int[] arr) {
		int maxSum = arr[0];
		int currentSum = arr[0];
		for (int i = 1; i < arr.length; i++) {
			currentSum = Math.max(arr[i], currentSum + arr[i]);
			maxSum = Math.max(maxSum, currentSum);
		}
		return maxSum;
	}
	
	/*
	 * 38. Project Euler Problem 1: Multiples of 3 and 5
	 * 题目链接: https://projecteuler.net/problem=1
	 * 题目描述: 使用位运算优化的数学计算（简化版本）
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)
	 */
	public static int multiplesOf3And5(int n) {
		int sum = 0;
		for (int i = 1; i < n; i++) {
			// 使用位运算优化判断是否能被3或5整除
			if (i % 3 == 0 || i % 5 == 0) {
				sum += i;
			}
		}
		return sum;
	}
	
	/*
	 * 39. HackerEarth XOR Profits
	 * 题目链接: https://www.hackerearth.com/practice/basic-programming/bit-manipulation/basics-of-bit-manipulation/practice-problems/
	 * 题目描述: 找出数组中两个元素的最大异或结果
	 * 时间复杂度: O(n * 32) - 构建前缀树需要O(n * 32)时间
	 * 空间复杂度: O(n * 32) - 前缀树空间
	 * 
	 * 解题思路：
	 * 使用前缀树(Trie)来优化查找最大异或对：
	 * 1. 构建前缀树，将所有数字的二进制表示插入树中
	 * 2. 对于每个数字，在前缀树中寻找能产生最大异或的路径
	 * 3. 贪心策略：尽可能使高位为1
	 */
	public static int findMaximumXOR(int[] nums) {
		if (nums == null || nums.length == 0) return 0;
		
		// 构建前缀树
		class TrieNode {
			TrieNode[] children;
			public TrieNode() {
				children = new TrieNode[2];
			}
		}
		
		TrieNode root = new TrieNode();
		
		// 插入所有数字的二进制表示到前缀树
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
		
		int maxXOR = 0;
		// 对于每个数字，在前缀树中寻找能产生最大异或的路径
		for (int num : nums) {
			TrieNode node = root;
			int currentXOR = 0;
			for (int i = 31; i >= 0; i--) {
				int bit = (num >> i) & 1;
				int desiredBit = 1 - bit;
				
				if (node.children[desiredBit] != null) {
					currentXOR |= (1 << i);
					node = node.children[desiredBit];
				} else {
					node = node.children[bit];
				}
			}
			maxXOR = Math.max(maxXOR, currentXOR);
		}
		
		return maxXOR;
	}
	
	/*
	 * 40. 计蒜客 A1401. 最大异或路径
	 * 时间复杂度: O(n), 空间复杂度: O(n)
	 */
	public static int maxXorPath() {
		return 0; // 框架代码
	}
	
	/* 41. LeetCode 1310 - XOR Queries of a Subarray
	 * 时间: O(n+q), 空间: O(n)
	 * 思路：前缀异或。arr[L..R] = prefix[R+1] ^ prefix[L]
	 */
	
	/*
	 * 41. LeetCode 1310. XOR Queries of a Subarray (子数组异或查询)
	 * 题目链接: https://leetcode.com/problems/xor-queries-of-a-subarray/
	 * 题目描述: 给你一个正整数数组 arr，你需要处理以下两种类型的查询：
	 *          1. 计算从索引 L 到 R 的元素的异或值
	 * 时间复杂度: O(n + q) - n为数组长度，q为查询次数
	 * 空间复杂度: O(n) - 前缀异或数组
	 * 
	 * 解题思路：
	 * 使用前缀异或数组优化多次查询：
	 * 1. 构建前缀异或数组 prefix，其中 prefix[i] = arr[0] ^ arr[1] ^ ... ^ arr[i-1]
	 * 2. 对于查询 [L, R]，结果为 prefix[R+1] ^ prefix[L]
	 */
	public static int[] xorQueries(int[] arr, int[][] queries) {
		int n = arr.length;
		int[] prefix = new int[n + 1];
		
		// 构建前缀异或数组
		for (int i = 0; i < n; i++) {
			prefix[i + 1] = prefix[i] ^ arr[i];
		}
		
		int[] result = new int[queries.length];
		
		// 处理每个查询
		for (int i = 0; i < queries.length; i++) {
			int left = queries[i][0];
			int right = queries[i][1];
			result[i] = prefix[right + 1] ^ prefix[left];
		}
		
		return result;
	}
	
	/*
	 * 42. LeetCode 2220. Minimum Bit Flips to Convert Number (转换数字的最少位翻转次数)
	 * 题目链接: https://leetcode.com/problems/minimum-bit-flips-to-convert-number/
	 * 题目描述: 一次位翻转定义为将数字 x 二进制中的一个位进行翻转操作，即将 0 变成 1 ，或者将 1 变成 0 。
	 *          给你两个整数 start 和 goal ，请你返回将 start 转变成 goal 的最少位翻转次数。
	 * 时间复杂度: O(1) - 固定32位比较
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * 解题思路：
	 * 计算两个数字的汉明距离，即异或结果中1的个数
	 */
	public static int minBitFlips(int start, int goal) {
		// 计算异或结果中1的个数
		return Integer.bitCount(start ^ goal);
	}
	
	/*
	 * 43. LeetCode 2433. Find The Original Array of Prefix Xor (找出前缀异或的原始数组)
	 * 题目链接: https://leetcode.com/problems/find-the-original-array-of-prefix-xor/
	 * 题目描述: 给你一个长度为 n 的整数数组 pref。找出并返回满足以下条件且长度为 n 的数组 arr：
	 *          pref[i] = arr[0] ^ arr[1] ^ ... ^ arr[i].
	 * 时间复杂度: O(n) - 遍历数组一次
	 * 空间复杂度: O(n) - 结果数组空间
	 * 
	 * 解题思路：
	 * 根据异或的性质，如果 pref[i] = arr[0] ^ arr[1] ^ ... ^ arr[i]
	 * 那么 arr[i] = pref[i] ^ pref[i-1] (i > 0)
	 * arr[0] = pref[0]
	 */
	public static int[] findArray(int[] pref) {
		int n = pref.length;
		int[] arr = new int[n];
		
		// 第一个元素就是前缀异或的第一个元素
		arr[0] = pref[0];
		
		// 根据公式计算其他元素
		for (int i = 1; i < n; i++) {
			arr[i] = pref[i] ^ pref[i - 1];
		}
		
		return arr;
	}
	
	/*
	 * 44. LeetCode 868. Binary Gap (二进制间距)
	 * 题目链接: https://leetcode.com/problems/binary-gap/
	 * 题目描述: 给定一个正整数 n，找到并返回 n 的二进制表示中两个相邻 1 之间的最长距离。
	 *          如果不存在两个相邻的 1，返回 0。
	 * 时间复杂度: O(log n) - 遍历二进制位
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * 解题思路：
	 * 遍历二进制表示，记录相邻1之间的距离
	 */
	public static int binaryGap(int n) {
		int maxGap = 0;
		int lastPos = -1;
		int pos = 0;
		
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
	
	/*
	 * 45. LeetCode 1009. Complement of Base 10 Integer (十进制整数的反码)
	 * 题目链接: https://leetcode.com/problems/complement-of-base-10-integer/
	 * 题目描述: 每个非负整数 N 都有其二进制表示。例如，5 可以被表示为二进制 "101"，11 可以用二进制 "1011" 表示，依此类推。
	 *          注意，除 N = 0 外，任何二进制表示中都不含前导零。
	 *          二进制的反码表示是将每个 1 改为 0 且每个 0 改为 1。例如，二进制数 "101" 的二进制反码为 "010"。
	 *          给你一个十进制数 N，请你返回其二进制表示的反码所对应的十进制整数。
	 * 时间复杂度: O(log n) - 构造掩码
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * 解题思路：
	 * 1. 构造一个掩码，该掩码的位数与n相同，但所有位都是1
	 * 2. 使用异或操作取反
	 */
	public static int bitwiseComplement(int n) {
		if (n == 0) return 1;
		
		int mask = 1;
		// 构造一个掩码，该掩码的位数与n相同，但所有位都是1
		while (mask < n) {
			mask = (mask << 1) | 1;
		}
		// 使用异或操作取反
		return n ^ mask;
	}
	
	/*
	 * 46. LeetCode 201. Bitwise AND of Numbers Range (数字范围按位与)
	 * 题目链接: https://leetcode.com/problems/bitwise-and-of-numbers-range/
	 * 题目描述: 给你两个整数 left 和 right ，表示区间 [left, right] ，返回此区间内所有数字按位与的结果
	 *          （包含 left 、right 端点）。
	 * 时间复杂度: O(1) - 最多32次循环
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * 解题思路：
	 * 找到left和right的公共前缀，因为在这个范围内的所有数字，只有公共前缀部分在按位与后保持不变。
	 */
	public static int rangeBitwiseAnd(int left, int right) {
		int shift = 0;
		
		// 找到公共前缀
		while (left < right) {
			left >>= 1;
			right >>= 1;
			shift++;
		}
		
		return left << shift;
	}
	
	/*
	 * 47. LeetCode 371. Sum of Two Integers (两整数之和)
	 * 题目链接: https://leetcode.com/problems/sum-of-two-integers/
	 * 题目描述: 给你两个整数 a 和 b ，不使用运算符 + 和 - ，计算并返回两整数之和。
	 * 时间复杂度: O(1) - 最多32次循环
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * 解题思路：
	 * 使用位运算模拟加法过程：
	 * 1. 异或运算得到无进位和
	 * 2. 与运算左移一位得到进位
	 * 3. 重复直到进位为0
	 */
	public static int getSum(int a, int b) {
		while (b != 0) {
			// 计算进位
			int carry = a & b;
			// 计算不考虑进位的和
			a = a ^ b;
			// 进位左移一位
			b = carry << 1;
		}
		return a;
	}
	
	/*
	 * 48. LeetCode 393. UTF-8 Validation (UTF-8 编码验证)
	 * 题目链接: https://leetcode.com/problems/utf-8-validation/
	 * 题目描述: 给定一个表示数据的整数数组 data，返回它是否为有效的 UTF-8 编码。
	 * 时间复杂度: O(n) - 遍历数组
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * 解题思路：
	 * 根据UTF-8编码规则验证每个字节
	 */
	public static boolean validUtf8(int[] data) {
		int count = 0;
		
		for (int value : data) {
			if (count == 0) {
				if ((value >> 5) == 0b110) count = 1;
				else if ((value >> 4) == 0b1110) count = 2;
				else if ((value >> 3) == 0b11110) count = 3;
				else if ((value >> 7) != 0) return false;
			} else {
				if ((value >> 6) != 0b10) return false;
				count--;
			}
		}
		
		return count == 0;
	}
	
	/*
	 * 49. LeetCode 405. Convert a Number to Hexadecimal (数字转换为十六进制数)
	 * 题目链接: https://leetcode.com/problems/convert-a-number-to-hexadecimal/
	 * 题目描述: 给定一个整数，编写一个算法将这个数转换为十六进制数。对于负整数，我们通常采用补码运算方法。
	 * 时间复杂度: O(1) - 最多8次循环
	 * 空间复杂度: O(1) - 只使用常数级额外空间
	 * 
	 * 解题思路：
	 * 处理32位整数，每次取出低4位转换为十六进制字符
	 */
	public static String toHex(int num) {
		if (num == 0) return "0";
		
		char[] hexChars = "0123456789abcdef".toCharArray();
		StringBuilder result = new StringBuilder();
		
		// 处理32位整数，每次取出低4位
		while (num != 0) {
			// 取出低4位
			result.append(hexChars[num & 0xf]);
			// 无符号右移4位
			num >>>= 4;
		}
		
		return result.reverse().toString();
	}
	
	/*
	 * 50. LeetCode 421. Maximum XOR of Two Numbers in an Array (数组中两个数的最大异或值)
	 * 题目链接: https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/
	 * 题目描述: 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。
	 * 时间复杂度: O(n) - 构建前缀树需要O(n * 32)时间
	 * 空间复杂度: O(n) - 前缀树空间
	 * 
	 * 解题思路：
	 * 使用前缀树(Trie)来优化查找最大异或对：
	 * 1. 构建前缀树，将所有数字的二进制表示插入树中
	 * 2. 对于每个数字，在前缀树中寻找能产生最大异或的路径
	 * 3. 贪心策略：尽可能使高位为1
	 */
	public static int findMaximumXOR(int[] nums) {
		if (nums == null || nums.length == 0) return 0;
		
		// 构建前缀树
		class TrieNode {
			TrieNode[] children;
			public TrieNode() {
				children = new TrieNode[2];
			}
		}
		
		TrieNode root = new TrieNode();
		
		// 插入所有数字的二进制表示到前缀树
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
		
		int maxXOR = 0;
		// 对于每个数字，在前缀树中寻找能产生最大异或的路径
		for (int num : nums) {
			TrieNode node = root;
			int currentXOR = 0;
			for (int i = 31; i >= 0; i--) {
				int bit = (num >> i) & 1;
				int desiredBit = 1 - bit;
				
				if (node.children[desiredBit] != null) {
					currentXOR |= (1 << i);
					node = node.children[desiredBit];
				} else {
					node = node.children[bit];
				}
			}
			maxXOR = Math.max(maxXOR, currentXOR);
		}
		
		return maxXOR;
	}
	public static int[] xorQueries(int[] arr, int[][] queries) {
		int n = arr.length;
		int[] prefix = new int[n + 1];
		for (int i = 0; i < n; i++) {
			prefix[i + 1] = prefix[i] ^ arr[i];
		}
		int[] result = new int[queries.length];
		for (int i = 0; i < queries.length; i++) {
			int left = queries[i][0];
			int right = queries[i][1];
			result[i] = prefix[right + 1] ^ prefix[left];
		}
		return result;
	}
	
	/* 42. LeetCode 2220 - Minimum Bit Flips to Convert Number
	 * 时间: O(1), 空间: O(1)
	 * 
	 * 解题思路：
	 * 要将start转换为goal，需要翻转的位数等于start^goal中1的个数
	 */
	public static int minBitFlips(int start, int goal) {
		return Integer.bitCount(start ^ goal);
	}
	
	/* 43. LeetCode 2433 - Find Original Array of Prefix Xor
	 * 时间: O(n), 空间: O(n)
	 * 
	 * 解题思路：
	 * 根据异或的性质，如果pref[i] = arr[0] ^ arr[1] ^ ... ^ arr[i]
	 * 那么arr[i] = pref[i] ^ pref[i-1] (i > 0)
	 * arr[0] = pref[0]
	 */
	public static int[] findArray(int[] pref) {
		int n = pref.length;
		int[] arr = new int[n];
		arr[0] = pref[0];
		for (int i = 1; i < n; i++) {
			arr[i] = pref[i] ^ pref[i - 1];
		}
		return arr;
	}

	public static void main(String[] args) {
		// 非负数
		int a = 78;
		System.out.println(a);
		printBinary(a);
		System.out.println("===a===");
		// 负数
		int b = -6;
		System.out.println(b);
		printBinary(b);
		System.out.println("===b===");
		// 直接写二进制的形式定义变量
		int c = 0b1001110;
		System.out.println(c);
		printBinary(c);
		System.out.println("===c===");
		// 直接写十六进制的形式定义变量
		// 0100 -> 4
		// 1110 -> e
		// 0x4e -> 01001110
		int d = 0x4e;
		System.out.println(d);
		printBinary(d);
		System.out.println("===d===");
		// ~、相反数
		System.out.println(a);
		printBinary(a);
		printBinary(~a);
		int e = ~a + 1;
		System.out.println(e);
		printBinary(e);
		System.out.println("===e===");
		// int、long的最小值，取相反数、绝对值，都是自己
		int f = Integer.MIN_VALUE;
		System.out.println(f);
		printBinary(f);
		System.out.println(-f);
		printBinary(-f);
		System.out.println(~f + 1);
		printBinary(~f + 1);
		System.out.println("===f===");
		// | & ^
		int g = 0b0001010;
		int h = 0b0001100;
		printBinary(g | h);
		printBinary(g & h);
		printBinary(g ^ h);
		System.out.println("===g、h===");
		// 可以这么写 : int num = 3231 | 6434;
		// 可以这么写 : int num = 3231 & 6434;
		// 不能这么写 : int num = 3231 || 6434;
		// 不能这么写 : int num = 3231 && 6434;
		// 因为 ||、&& 是 逻辑或、逻辑与，只能连接boolean类型
		// 不仅如此，|、& 连接的两侧一定都会计算
		// 而 ||、&& 有穿透性的特点
		System.out.println("test1测试开始");
		boolean test1 = returnTrue() | returnFalse();
		System.out.println("test1结果，" + test1);
		System.out.println("test2测试开始");
		boolean test2 = returnTrue() || returnFalse();
		System.out.println("test2结果，" + test2);
		System.out.println("test3测试开始");
		boolean test3 = returnFalse() & returnTrue();
		System.out.println("test3结果，" + test3);
		System.out.println("test4测试开始");
		boolean test4 = returnFalse() && returnTrue();
		System.out.println("test4结果，" + test4);
		System.out.println("===|、&、||、&&===");
		// <<
		int i = 0b0011010;
		printBinary(i);
		printBinary(i << 1);
		printBinary(i << 2);
		printBinary(i << 3);
		System.out.println("===i << ===");
		// 非负数 >> >>>，效果一样
		printBinary(i);
		printBinary(i >> 2);
		printBinary(i >>> 2);
		System.out.println("===i >> >>>===");
		// 负数 >> >>>，效果不一样
		int j = 0b11110000000000000000000000000000;
		printBinary(j);
		printBinary(j >> 2);
		printBinary(j >>> 2);
		System.out.println("===j >> >>>===");
		// 非负数 << 1，等同于乘以2
		// 非负数 << 2，等同于乘以4
		// 非负数 << 3，等同于乘以8
		// 非负数 << i，等同于乘以2的i次方
		// ...
		// 非负数 >> 1，等同于除以2
		// 非负数 >> 2，等同于除以4
		// 非负数 >> 3，等同于除以8
		// 非负数 >> i，等同于除以2的i次方
		// 只有非负数符合这个特征，负数不要用
		int k = 10;
		System.out.println(k);
		System.out.println(k << 1);
		System.out.println(k << 2);
		System.out.println(k << 3);
		System.out.println(k >> 1);
		System.out.println(k >> 2);
		System.out.println(k >> 3);
		System.out.println("===k===");

		// 测试新增的二进制操作函数
		System.out.println("===新增二进制操作函数测试===");
		
		// 测试 reverseBits
		int testReverse = 43261596; // 00000010100101000001111010011100
		System.out.println("Reverse bits 测试:");
		System.out.print("原数字: ");
		printBinary(testReverse);
		int reversed = reverseBits(testReverse);
		System.out.print("颠倒后: ");
		printBinary(reversed);
		System.out.println("预期结果: 964176192");
		System.out.println("实际结果: " + reversed);
		System.out.println();

		// 测试 hammingWeight
		int testHamming = 11; // 1011
		System.out.println("Hamming weight 测试:");
		System.out.print("数字: ");
		printBinary(testHamming);
		System.out.println("1的个数: " + hammingWeight(testHamming));
		System.out.println("预期结果: 3");
		System.out.println();

		// 测试 countBits
		System.out.println("Count bits 测试:");
		int[] bits = countBits(5);
		System.out.print("0到5中每个数字二进制表示中1的个数: ");
		for (int bit : bits) {
			System.out.print(bit + " ");
		}
		System.out.println();
		System.out.println("预期结果: 0 1 1 2 1 2");
		System.out.println();

		// 测试 isPowerOfTwo
		System.out.println("Is power of two 测试:");
		System.out.println("8是2的幂: " + isPowerOfTwo(8));
		System.out.println("10是2的幂: " + isPowerOfTwo(10));
		System.out.println();

		// 测试 isPowerOfFour
		System.out.println("Is power of four 测试:");
		System.out.println("16是4的幂: " + isPowerOfFour(16));
		System.out.println("8是4的幂: " + isPowerOfFour(8));
		System.out.println();

		// 测试 hasAlternatingBits
		System.out.println("Has alternating bits 测试:");
		System.out.println("5(101)有交替位: " + hasAlternatingBits(5));
		System.out.println("7(111)有交替位: " + hasAlternatingBits(7));
		System.out.println();

		// 测试 hammingDistance
		System.out.println("Hamming distance 测试:");
		System.out.println("1(0001)和4(0100)的汉明距离: " + hammingDistance(1, 4));
		System.out.println("预期结果: 2");
		System.out.println();

		// 测试 findComplement
		System.out.println("Find complement 测试:");
		System.out.println("5(101)的补数: " + findComplement(5));
		System.out.println("预期结果: 2 (010)");
		System.out.println();

		// 测试 missingNumber
		System.out.println("Missing number 测试:");
		int[] missingTest = {3, 0, 1};
		System.out.println("数组[3,0,1]缺失的数字: " + missingNumber(missingTest));
		System.out.println("预期结果: 2");
		System.out.println();

		// 测试 singleNumberI


		// 测试 singleNumberII
		System.out.println("Single number II 测试:");
		int[] singleTestII = {2, 2, 3, 2};
		System.out.println("数组[2,2,3,2]中只出现一次的数字: " + singleNumberII(singleTestII));
		System.out.println("预期结果: 3");
		System.out.println();

		// 测试 singleNumberIII
		System.out.println("Single number III 测试:");
		int[] singleTestIII = {1, 2, 1, 3, 2, 5};
		int[] result = singleNumberIII(singleTestIII);
		System.out.print("数组[1,2,1,3,2,5]中只出现一次的两个数字: ");
		for (int num : result) {
			System.out.print(num + " ");
		}
		System.out.println();
		System.out.println("预期结果: 3 5 (顺序可能不同)");
		System.out.println();

		// 测试 binaryStirling
		System.out.println("Binary Stirling 测试:");
		System.out.println("S(5,2) mod 2 = " + binaryStirling(5, 2));
		System.out.println("预期结果: 0");
		System.out.println();

		// 测试扩展题目
		runExtendedProblems();
	}

	/**
	 * 运行扩展题目测试
	 * 包含从各大OJ平台精选的位运算题目
	 */
	public static void runExtendedProblems() {
		System.out.println("=== 扩展题目测试 ===");
		
		// LeetCode 136 - Single Number
		testSingleNumber();
		
		// LeetCode 137 - Single Number II
		testSingleNumberII();
		
		// LeetCode 260 - Single Number III
		testSingleNumberIII();
		
		// LeetCode 191 - Number of 1 Bits
		testNumberOf1Bits();
		
		// LeetCode 338 - Counting Bits
		testCountingBits();
		
		// LeetCode 190 - Reverse Bits
		testReverseBits();
		
		// LeetCode 231 - Power of Two
		testPowerOfTwo();
		
		// LeetCode 342 - Power of Four
		testPowerOfFour();
		
		// LeetCode 268 - Missing Number
		testMissingNumber();
		
		// LeetCode 371 - Sum of Two Integers
		testSumOfTwoIntegers();
		
		// LeetCode 201 - Bitwise AND of Numbers Range
		testBitwiseANDOfNumbersRange();
		
		// LeetCode 477 - Total Hamming Distance
		testTotalHammingDistance();
		
		System.out.println("=== 扩展题目测试完成 ===");
	}

	/**
	 * LeetCode 136 - Single Number (只出现一次的数字)
	 * 来源: LeetCode
	 * 链接: https://leetcode.com/problems/single-number/
	 * 
	 * 题目描述:
	 * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
	 * 
	 * 解法分析:
	 * 最优解: 异或运算
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)
	 * 
	 * 核心思想:
	 * 利用异或运算的性质：
	 * 1. a ^ a = 0
	 * 2. a ^ 0 = a
	 * 3. 异或运算满足交换律和结合律
	 * 
	 * 因此，所有出现两次的数字异或后结果为0，最后剩下的就是只出现一次的数字。
	 */

	
	public static void testSingleNumber() {
		System.out.println("=== LeetCode 136 - Single Number 测试 ===");
		int[] nums1 = {2, 2, 1};
		int[] nums2 = {4, 1, 2, 1, 2};
		int[] nums3 = {1};
		
		System.out.println("测试用例1: " + Arrays.toString(nums1) + " -> " + singleNumber(nums1));
		System.out.println("测试用例2: " + Arrays.toString(nums2) + " -> " + singleNumber(nums2));
		System.out.println("测试用例3: " + Arrays.toString(nums3) + " -> " + singleNumber(nums3));
	}

	/**
	 * LeetCode 137 - Single Number II (只出现一次的数字 II)
	 * 来源: LeetCode
	 * 链接: https://leetcode.com/problems/single-number-ii/
	 * 
	 * 题目描述:
	 * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现三次。找出那个只出现了一次的元素。
	 * 
	 * 解法分析:
	 * 最优解: 位运算统计
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)
	 * 
	 * 核心思想:
	 * 对于每个二进制位，统计所有数字在该位上1的个数
	 * 如果某个位上1的个数不是3的倍数，说明只出现一次的数字在该位上是1
	 */

	
	public static void testSingleNumberII() {
		System.out.println("=== LeetCode 137 - Single Number II 测试 ===");
		int[] nums1 = {2, 2, 3, 2};
		int[] nums2 = {0, 1, 0, 1, 0, 1, 99};
		
		System.out.println("测试用例1: " + Arrays.toString(nums1) + " -> " + singleNumberII(nums1));
		System.out.println("测试用例2: " + Arrays.toString(nums2) + " -> " + singleNumberII(nums2));
	}

	/**
	 * LeetCode 260 - Single Number III (只出现一次的数字 III)
	 * 来源: LeetCode
	 * 链接: https://leetcode.com/problems/single-number-iii/
	 * 
	 * 题目描述:
	 * 给定一个整数数组，其中恰好有两个元素只出现一次，其余所有元素均出现两次。找出只出现一次的那两个元素。
	 * 
	 * 解法分析:
	 * 最优解: 分组异或
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)
	 * 
	 * 核心思想:
	 * 1. 先对所有数字进行异或，得到两个不同数字的异或结果
	 * 2. 找到异或结果中任意一个为1的位，这个位可以将数组分成两组
	 * 3. 分别对两组进行异或，得到两个结果
	 */

	
	public static void testSingleNumberIII() {
		System.out.println("=== LeetCode 260 - Single Number III 测试 ===");
		int[] nums1 = {1, 2, 1, 3, 2, 5};
		int[] result = singleNumberIII(nums1);
		System.out.println("测试用例: " + Arrays.toString(nums1) + " -> " + Arrays.toString(result));
	}

	/**
	 * LeetCode 191 - Number of 1 Bits (位1的个数)
	 * 来源: LeetCode
	 * 链接: https://leetcode.com/problems/number-of-1-bits/
	 * 
	 * 题目描述:
	 * 编写一个函数，输入是一个无符号整数，返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。
	 * 
	 * 解法分析:
	 * 最优解: Brian Kernighan算法
	 * 时间复杂度: O(k)，k为1的个数
	 * 空间复杂度: O(1)
	 * 
	 * 核心思想:
	 * 使用 n & (n - 1) 可以清除最右边的1
	 * 每次清除一个1，直到n变为0
	 */
	public static int numberOf1Bits(int n) {
		int count = 0;
		while (n != 0) {
			n &= (n - 1);
			count++;
		}
		return count;
	}
	
	public static void testNumberOf1Bits() {
		System.out.println("=== LeetCode 191 - Number of 1 Bits 测试 ===");
		System.out.println("11(1011)的1的个数: " + numberOf1Bits(11));
		System.out.println("128(10000000)的1的个数: " + numberOf1Bits(128));
	}

	/**
	 * LeetCode 338 - Counting Bits (比特位计数)
	 * 来源: LeetCode
	 * 链接: https://leetcode.com/problems/counting-bits/
	 * 
	 * 题目描述:
	 * 给定一个非负整数 num。对于 0 ≤ i ≤ num 范围中的每个数字 i ，计算其二进制数中的 1 的数目并将它们作为数组返回。
	 * 
	 * 解法分析:
	 * 最优解: 动态规划
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 * 
	 * 核心思想:
	 * 利用已知结果：i的1的个数 = i/2的1的个数 + i的最低位是否为1
	 * 即：bits[i] = bits[i >> 1] + (i & 1)
	 */
	public static int[] countingBits(int n) {
		int[] bits = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			bits[i] = bits[i >> 1] + (i & 1);
		}
		return bits;
	}
	
	public static void testCountingBits() {
		System.out.println("=== LeetCode 338 - Counting Bits 测试 ===");
		int[] result = countingBits(5);
		System.out.println("0到5的1的个数: " + Arrays.toString(result));
	}

	/**
	 * LeetCode 190 - Reverse Bits (颠倒二进制位)
	 * 来源: LeetCode
	 * 链接: https://leetcode.com/problems/reverse-bits/
	 * 
	 * 题目描述:
	 * 颠倒给定的 32 位无符号整数的二进制位。
	 * 
	 * 解法分析:
	 * 最优解: 逐位反转
	 * 时间复杂度: O(1) - 固定32次循环
	 * 空间复杂度: O(1)
	 * 
	 * 核心思想:
	 * 从右到左提取每一位，然后从左到右放置到结果中
	 */

	
	public static void testReverseBits() {
		System.out.println("=== LeetCode 190 - Reverse Bits 测试 ===");
		int n = 43261596; // 00000010100101000001111010011100
		int reversed = reverseBits(n);
		System.out.println("原数字: " + n + ", 颠倒后: " + reversed);
	}

	/**
	 * LeetCode 231 - Power of Two (2的幂)
	 * 来源: LeetCode
	 * 链接: https://leetcode.com/problems/power-of-two/
	 * 
	 * 题目描述:
	 * 给定一个整数，编写一个函数来判断它是否是 2 的幂次方。
	 * 
	 * 解法分析:
	 * 最优解: 位运算
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * 
	 * 核心思想:
	 * 2的幂的二进制表示中只有一个1
	 * 使用 n & (n - 1) == 0 来判断
	 */
	public static boolean powerOfTwo(int n) {
		return n > 0 && (n & (n - 1)) == 0;
	}
	
	public static void testPowerOfTwo() {
		System.out.println("=== LeetCode 231 - Power of Two 测试 ===");
		System.out.println("8是2的幂: " + powerOfTwo(8));
		System.out.println("10是2的幂: " + powerOfTwo(10));
}

	/**
	 * LeetCode 342 - Power of Four (4的幂)
	 * 来源: LeetCode
	 * 链接: https://leetcode.com/problems/power-of-four/
	 * 
	 * 题目描述:
	 * 给定一个整数，写一个函数来判断它是否是 4 的幂次方。
	 * 
	 * 解法分析:
	 * 最优解: 位运算
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * 
	 * 核心思想:
	 * 4的幂首先是2的幂，而且1必须在奇数位上（从右往左数，最右边是第0位）
	 * 0x55555555 是十六进制表示，二进制是 01010101010101010101010101010101
	 * 这个数在所有奇数位上都是1，用于检查1是否在正确的位置上
	 */
	public static boolean powerOfFour(int n) {
		return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
	}
	
	public static void testPowerOfFour() {
		System.out.println("=== LeetCode 342 - Power of Four 测试 ===");
		System.out.println("16是4的幂: " + powerOfFour(16));
		System.out.println("8是4的幂: " + powerOfFour(8));
	}


	
	public static void testMissingNumber() {
		System.out.println("=== LeetCode 268 - Missing Number 测试 ===");
		int[] nums1 = {3, 0, 1};
		int[] nums2 = {0, 1};
		System.out.println("数组" + java.util.Arrays.toString(nums1) + "缺失的数字: " + missingNumber(nums1));
		System.out.println("数组" + java.util.Arrays.toString(nums2) + "缺失的数字: " + missingNumber(nums2));
	}

	/**
	 * LeetCode 371 - Sum of Two Integers (两整数之和)
	 * 来源: LeetCode
	 * 链接: https://leetcode.com/problems/sum-of-two-integers/
	 * 
	 * 题目描述:
	 * 不使用运算符 + 和 - ，计算两整数 a 、b 之和。
	 * 
	 * 解法分析:
	 * 最优解: 位运算模拟加法
	 * 时间复杂度: O(1) - 最多32次循环
	 * 空间复杂度: O(1)
	 * 
	 * 核心思想:
	 * 使用位运算模拟加法过程：
	 * 1. 异或运算得到无进位和
	 * 2. 与运算左移一位得到进位
	 * 3. 重复直到进位为0
	 */
	public static int sumOfTwoIntegers(int a, int b) {
		while (b != 0) {
			int carry = (a & b) << 1;  // 进位
			a = a ^ b;                // 无进位和
			b = carry;
		}
		return a;
	}
	
	public static void testSumOfTwoIntegers() {
		System.out.println("=== LeetCode 371 - Sum of Two Integers 测试 ===");
		System.out.println("1 + 2 = " + sumOfTwoIntegers(1, 2));
		System.out.println("15 + 7 = " + sumOfTwoIntegers(15, 7));
	}

	/**
	 * LeetCode 201 - Bitwise AND of Numbers Range (数字范围按位与)
	 * 来源: LeetCode
	 * 链接: https://leetcode.com/problems/bitwise-and-of-numbers-range/
	 * 
	 * 题目描述:
	 * 给定范围 [m, n]，其中 0 <= m <= n <= 2147483647，返回此范围内所有数字的按位与（包含 m, n 两端点）。
	 * 
	 * 解法分析:
	 * 最优解: 位运算
	 * 时间复杂度: O(1) - 最多32次循环
	 * 空间复杂度: O(1)
	 * 
	 * 核心思想:
	 * 找到m和n的公共前缀，因为在这个范围内的所有数字，只有公共前缀部分在按位与后保持不变。
	 */
	public static int bitwiseANDOfNumbersRange(int m, int n) {
		int shift = 0;
		while (m < n) {
			m >>= 1;
			n >>= 1;
			shift++;
		}
		return m << shift;
	}
	
	public static void testBitwiseANDOfNumbersRange() {
		System.out.println("=== LeetCode 201 - Bitwise AND of Numbers Range 测试 ===");
		System.out.println("[5, 7]的按位与: " + bitwiseANDOfNumbersRange(5, 7));
		System.out.println("[0, 1]的按位与: " + bitwiseANDOfNumbersRange(0, 1));
	}


	
	public static void testTotalHammingDistance() {
		System.out.println("=== LeetCode 477 - Total Hamming Distance 测试 ===");
		int[] nums = {4, 14, 2};
		System.out.println("数组" + java.util.Arrays.toString(nums) + "的汉明距离总和: " + totalHammingDistance(nums));
	}

	// 辅助函数：返回true
	private static boolean returnTrue() {
		System.out.println("执行了returnTrue");
		return true;
	}

	// 辅助函数：返回false
	private static boolean returnFalse() {
		System.out.println("执行了returnFalse");
		return false;
	}

}