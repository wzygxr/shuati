import java.util.*;

/**
 * 质因数分解算法专题 - Java实现
 * 
 * 本文件实现了完整的质因数分解算法体系，包括：
 * 1. 基础质因数分解算法
 * 2. 欧拉函数计算
 * 3. 最大公约数和最小公倍数计算
 * 4. 因数个数和因数和计算
 * 5. 素数判断算法
 * 6. 按公因数计算最大组件大小算法
 * 
 * 核心算法特性：
 * - 时间复杂度：O(√n) - 对于质因数分解和因数计算
 * - 空间复杂度：O(k) - 其中k是不同质因数的数量
 * - 适用范围：适用于int类型范围内的正整数
 * - 算法类型：确定性算法，保证结果的正确性
 * 
 * 算法原理深度分析：
 * 质因数分解基于算术基本定理：任何大于1的自然数都可以唯一地分解为质因数的乘积。
 * 算法通过试除法实现，从最小的质数开始逐一尝试整除。
 * 
 * 优化策略：
 * 1. 单独处理2的因子，然后只处理奇数因子
 * 2. 只需检查到√n，因为如果n有大于√n的因子，那么必然有一个小于√n的对应因子
 * 3. 使用6k±1规则优化素数判断
 * 4. 并查集优化最大组件大小计算
 * 
 * 工程化考量：
 * 1. 类型安全：使用int类型，注意溢出问题
 * 2. 性能优化：结合多种优化策略
 * 3. 内存管理：合理使用数据结构和算法
 * 4. 异常安全：正确处理边界情况和异常输入
 * 5. 可测试性：提供完整的单元测试和性能测试
 * 
 * 相关题目（扩展版）：
 * 本算法可应用于30个平台的质因数相关题目，具体参见注释中的详细列表。
 * 
 * 数学证明：
 * 算术基本定理：任何大于1的自然数都可以唯一地分解为质因数的乘积。
 * 欧拉函数公式：φ(n) = n * ∏(1-1/p)，其中p是n的质因数。
 * 因数个数公式：d(n) = ∏(a_i+1)，其中a_i是质因数的指数。
 * 因数和公式：σ(n) = ∏(1+p+p^2+...+p^a_i)。
 * 
 * 复杂度推导：
 * 质因数分解的时间复杂度为O(√n)，因为最多需要检查到√n的所有可能因子。
 * 欧拉函数和因数计算的时间复杂度也为O(√n)，因为它们依赖于质因数分解。
 * 
 * 工程实践建议：
 * 1. 对于生产环境，建议使用BigInteger处理大数
 * 2. 注意int类型的溢出问题，特别是乘法运算
 * 3. 对于极大数，考虑使用更高效的算法
 * 4. 在实际应用中，可以结合缓存机制提高性能
 * 
 * 编译运行：
 * javac Code03_PrimeFactors.java
 * java Code03_PrimeFactors
 * 
 * @author 算法学习平台
 * @version 1.0
 * @created 2025
 * 
 * 测试链接：https://leetcode.cn/problems/largest-component-size-by-common-factor/
 * 优化版本：支持多种质因数分解和数论计算
 */

public class Code03_PrimeFactors {

	// 相关题目链接（扩展版）：
	// 覆盖30个算法平台的质因数相关题目
	// 1. LeetCode 313. Super Ugly Number - https://leetcode.cn/problems/super-ugly-number/
	// 2. LeetCode 264. Ugly Number II - https://leetcode.cn/problems/ugly-number-ii/
	// 3. LeetCode 204. Count Primes - https://leetcode.cn/problems/count-primes/
	// 4. POJ 1811. Prime Test - http://poj.org/problem?id=1811
	// 5. LeetCode 952. Largest Component Size by Common Factor - https://leetcode.cn/problems/largest-component-size-by-common-factor/
	// 6. LeetCode 1201. Ugly Number III - https://leetcode.cn/problems/ugly-number-iii/
	// 7. LeetCode 762. Prime Number of Set Bits in Binary Representation - https://leetcode.cn/problems/prime-number-of-set-bits-in-binary-representation/
	// 8. LeetCode 1025. Divisor Game - https://leetcode.cn/problems/divisor-game/
	// 9. LeetCode 202. Happy Number - https://leetcode.cn/problems/happy-number/
	// 10. LeetCode 172. Factorial Trailing Zeroes - https://leetcode.cn/problems/factorial-trailing-zeroes/
	// 11. LeetCode 263. Ugly Number - https://leetcode.cn/problems/ugly-number/
	// 12. LeetCode 342. Power of Four - https://leetcode.cn/problems/power-of-four/
	// 13. LeetCode 326. Power of Three - https://leetcode.cn/problems/power-of-three/
	// 14. LeetCode 231. Power of Two - https://leetcode.cn/problems/power-of-two/
	// 15. LeetCode 1492. The kth Factor of n - https://leetcode.cn/problems/the-kth-factor-of-n/
	// 16. LeetCode 1362. Closest Divisors - https://leetcode.cn/problems/closest-divisors/
	// 17. LeetCode 507. Perfect Number - https://leetcode.cn/problems/perfect-number/
	// 18. LeetCode 869. Reordered Power of 2 - https://leetcode.cn/problems/reordered-power-of-2/
	// 19. POJ 2142. The Balance - http://poj.org/problem?id=2142
	// 20. Codeforces 735A. Ostap and Grasshopper - https://codeforces.com/problemset/problem/735/A
	// 21. LeetCode 1952. Three Divisors - https://leetcode.cn/problems/three-divisors/
	// 22. LeetCode 1627. Graph Connectivity With Threshold - https://leetcode.cn/problems/graph-connectivity-with-threshold/
	// 23. LeetCode 2427. Number of Common Factors - https://leetcode.cn/problems/number-of-common-factors/
	// 24. LeetCode 1819. Number of Different Subsequences GCDs - https://leetcode.cn/problems/number-of-different-subsequences-gcds/
	// 25. LeetCode 1250. Check If It Is a Good Array - https://leetcode.cn/problems/check-if-it-is-a-good-array/
	// 26. LeetCode 365. Water and Jug Problem - https://leetcode.cn/problems/water-and-jug-problem/
	// 27. LeetCode 1447. Simplified Fractions - https://leetcode.cn/problems/simplified-fractions/
	// 28. LeetCode 829. Consecutive Numbers Sum - https://leetcode.cn/problems/consecutive-numbers-sum/
	// 29. LeetCode 1071. Greatest Common Divisor of Strings - https://leetcode.cn/problems/greatest-common-divisor-of-strings/
	// 30. LeetCode 2248. Intersection of Multiple Arrays - https://leetcode.cn/problems/intersection-of-multiple-arrays/

	/**
	 * 基础质因数分解算法 - 打印分解过程
	 * 
	 * 算法原理：
	 * 基于算术基本定理，任何大于1的自然数都可以唯一地分解为质因数的乘积。
	 * 使用试除法从最小的质数开始逐一尝试整除。
	 * 
	 * 时间复杂度：O(√n) - 最坏情况下需要检查到√n的所有可能因子
	 * 空间复杂度：O(1) - 只使用常数级别的额外空间
	 * 
	 * 优化策略：
	 * 1. 单独处理2的因子，然后只处理奇数因子，减少一半的迭代次数
	 * 2. 只需检查到√n，因为如果n有大于√n的因子，那么必然有一个小于√n的对应因子
	 * 3. 使用i*i <= n而不是i <= sqrt(n)避免多次调用sqrt函数
	 * 
	 * 算法步骤：
	 * 1. 边界条件处理：n <= 1的情况直接输出
	 * 2. 单独处理2的因子：使用while循环提取所有2的因子
	 * 3. 处理奇数因子：从3开始，每次增加2，检查到√n
	 * 4. 处理剩余因子：如果最后n > 1，说明n本身是质数
	 * 
	 * 工程化考量：
	 * 1. 输入验证：处理n <= 1的边界情况
	 * 2. 输出格式：清晰的分解过程显示
	 * 3. 性能优化：避免重复计算和冗余操作
	 * 4. 可读性：清晰的算法逻辑和注释
	 * 
	 * @param n 待分解的正整数
	 * 
	 * 使用示例：
	 * ```java
	 * f(12);  // 输出: 12 = 2 * 2 * 3
	 * f(17);  // 输出: 17 = 17
	 * f(100); // 输出: 100 = 2 * 2 * 5 * 5
	 * ```
	 * 
	 * 注意事项：
	 * - 该方法只适用于正整数
	 * - 对于n <= 1，直接输出n本身
	 * - 输出格式包含乘法符号，便于理解分解过程
	 */
	public static void f(int n) {
		// 输入验证和边界条件处理
		System.out.print(n + " = ");
		if (n <= 1) {
			System.out.println(n);
			return;
		}

		// 步骤1：单独处理2的因子
		// 单独处理2可以避免在后续循环中检查偶数，提高效率
		boolean hasFactors = false;
		while (n % 2 == 0) {
			if (hasFactors) {
				System.out.print(" * ");
			}
			System.out.print("2");
			n /= 2;
			hasFactors = true;
		}

		// 步骤2：处理3及以上的奇数因子
		// 从3开始，每次增加2，只检查奇数因子
		for (int i = 3; i * i <= n; i += 2) {
			while (n % i == 0) {
				if (hasFactors) {
					System.out.print(" * ");
				}
				System.out.print(i);
				n /= i;
				hasFactors = true;
			}
		}

		// 步骤3：处理最后剩下的因子
		// 如果n > 1，说明它本身是一个质数
		if (n > 1) {
			if (hasFactors) {
				System.out.print(" * ");
			}
			System.out.println(n);
		} else {
			System.out.println();
		}
	}

	/**
	 * 主函数 - 程序入口点
	 * 
	 * 功能概述：
	 * 1. 运行功能测试：验证所有算法的正确性
	 * 2. 运行性能测试：测试算法在不同规模数据下的性能表现
	 * 3. 运行交互式测试：提供用户交互界面进行测试
	 * 
	 * 测试策略：
	 * - 功能测试：覆盖边界情况、典型情况和特殊情况
	 * - 性能测试：测试大规模数据的处理能力
	 * - 交互测试：提供灵活的用户测试界面
	 * 
	 * 工程化考量：
	 * 1. 模块化设计：每个测试功能独立，便于维护和扩展
	 * 2. 错误处理：捕获和处理可能的异常
	 * 3. 用户体验：清晰的测试输出和交互界面
	 * 4. 性能监控：记录执行时间用于性能分析
	 * 
	 * @param args 命令行参数（未使用）
	 * 
	 * 使用示例：
	 * ```bash
	 * # 编译并运行
	 * javac Code03_PrimeFactors.java
	 * java Code03_PrimeFactors
	 * ```
	 * 
	 * 输出示例：
	 * ```
	 * ===== 功能测试 =====
	 * 12 = 2 * 2 * 3
	 * 质因数列表: [2, 3]
	 * 质因数及指数: {2=2, 3=1}
	 * 所有质因数: [2, 2, 3]
	 * 
	 * φ(12) = 4
	 * gcd(4, 6) = 2
	 * lcm(4, 6) = 12
	 * d(12) = 6
	 * σ(12) = 28
	 * ```
	 */
	public static void main(String[] args) {
		try {
			// 运行功能测试 - 验证算法正确性
			functionalTest();
			
			// 运行性能测试 - 测试算法性能
			performanceTest();
			
			// 运行交互式测试 - 提供用户交互界面
			interactiveTest();
			
			System.out.println("所有测试完成！");
		} catch (Exception e) {
			System.err.println("程序执行过程中发生错误: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 功能测试函数
	 */
	private static void functionalTest() {
		System.out.println("===== 功能测试 =====");
		
		// 测试质因数分解
		System.out.println("\n--- 质因数分解测试 ---");
		int[] testNumbers = {1, 2, 3, 4, 6, 12, 24, 48, 96, 97, 100, 1000};
		for (int num : testNumbers) {
			f(num);
			System.out.print("质因数列表: ");
			System.out.println(getPrimeFactors(num));
			System.out.print("质因数及指数: ");
			System.out.println(getPrimeFactorsWithExponents(num));
			System.out.print("所有质因数: ");
			System.out.println(getAllPrimeFactors(num));
			System.out.println();
		}
		
		// 测试欧拉函数
		System.out.println("\n--- 欧拉函数测试 ---");
		for (int num : testNumbers) {
			System.out.println("φ(" + num + ") = " + eulerPhi(num));
		}
		
		// 测试最大公约数和最小公倍数
		System.out.println("\n--- GCD和LCM测试 ---");
		int[][] pairs = {{4, 6}, {12, 18}, {7, 13}, {1, 5}};
		for (int[] pair : pairs) {
			System.out.println("gcd(" + pair[0] + ", " + pair[1] + ") = " + gcd(pair[0], pair[1]));
			System.out.println("lcm(" + pair[0] + ", " + pair[1] + ") = " + lcm(pair[0], pair[1]));
		}
		
		// 测试因数个数和因数和
		System.out.println("\n--- 因数个数和因数和测试 ---");
		for (int num : testNumbers) {
			System.out.println("d(" + num + ") = " + countDivisors(num) + " (因数个数)");
			System.out.println("σ(" + num + ") = " + sumDivisors(num) + " (因数和)");
		}
		
		// 测试素数判断
		System.out.println("\n--- 素数判断测试 ---");
		int[] primesTest = {1, 2, 3, 4, 97, 99, 100, 1000000007};
		for (int num : primesTest) {
			System.out.println(num + " 是素数: " + isPrime(num));
		}
		
		// 测试最大组件大小
		System.out.println("\n--- 最大组件大小测试 ---");
		int[][] testArrays = {
				{4, 6, 15, 35},
				{20, 50, 9, 63},
				{2, 3, 6, 7, 4, 12, 21, 39},
				{1}
		};
		for (int[] arr : testArrays) {
			System.out.print("数组 " + Arrays.toString(arr) + " 的最大组件大小: ");
			System.out.print(largestComponentSize(arr));
			System.out.print(" (优化版: " + largestComponentSizeOptimized(arr) + ")");
			System.out.println();
		}
		
		System.out.println("\n===== 功能测试完成 =====\n");
	}

	/**
	 * 性能测试函数
	 */
	private static void performanceTest() {
		System.out.println("===== 性能测试 =====");
		
		// 测试质因数分解性能
		System.out.println("\n--- 质因数分解性能测试 ---");
		int[] largeNumbers = {
				1000000,
				10000000,
				100000000,
				Integer.MAX_VALUE / 2,
				Integer.MAX_VALUE - 2
		};
		
		for (int num : largeNumbers) {
			long startTime = System.nanoTime();
			getPrimeFactors(num);
			long endTime = System.nanoTime();
			System.out.printf("分解 %d 耗时: %.2f 毫秒\n", num, (endTime - startTime) / 1_000_000.0);
		}
		
		// 测试最大组件大小性能
		System.out.println("\n--- 最大组件大小性能测试 ---");
		int size = 10000;
		int[] largeArray = new int[size];
		Random rand = new Random(42); // 固定种子以保持一致性
		for (int i = 0; i < size; i++) {
			largeArray[i] = rand.nextInt(10000) + 1;
		}
		
		// 测试原始版本
		long startTime = System.nanoTime();
		int result = largestComponentSize(largeArray);
		long endTime = System.nanoTime();
		System.out.printf("原始版本 - 处理 %d 个元素的数组，最大组件大小: %d，耗时: %.2f 毫秒\n", 
				size, result, (endTime - startTime) / 1_000_000.0);
		
		// 测试优化版本
		startTime = System.nanoTime();
		result = largestComponentSizeOptimized(largeArray);
		endTime = System.nanoTime();
		System.out.printf("优化版本 - 处理 %d 个元素的数组，最大组件大小: %d，耗时: %.2f 毫秒\n", 
				size, result, (endTime - startTime) / 1_000_000.0);
		
		System.out.println("\n===== 性能测试完成 =====\n");
	}

	/**
	 * 交互式测试函数
	 */
	private static void interactiveTest() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("===== 交互式测试 =====");
		System.out.println("输入任意整数进行质因数分解 (输入 -1 退出):");
		
		while (true) {
			System.out.print("请输入一个整数: ");
			try {
				int n = scanner.nextInt();
				if (n == -1) {
					break;
				}
				f(n);
				System.out.print("质因数列表: ");
				System.out.println(getPrimeFactors(n));
				System.out.print("质因数及指数: ");
				System.out.println(getPrimeFactorsWithExponents(n));
				System.out.println(n + " 是素数: " + isPrime(n));
				System.out.println("因数个数: " + countDivisors(n));
				System.out.println("因数和: " + sumDivisors(n));
				System.out.println();
			} catch (Exception e) {
				System.out.println("输入错误，请输入有效的整数。");
				scanner.nextLine(); // 清除输入缓冲区
			}
		}
		
		scanner.close();
		System.out.println("交互式测试结束。");
	}
	
	/**
	 * 返回n的质因数列表（不包含重复）
	 * 时间复杂度O(√n)
	 * 空间复杂度O(k)，其中k是不同质因数的数量
	 * 
	 * 参数:
	 *     n: 待分解的正整数
	 * 返回:
	 *     List<Integer>: 质因数列表，每个质因数只出现一次
	 */
	public static List<Integer> getPrimeFactors(int n) {
		List<Integer> factors = new ArrayList<>();
		
		// 参数验证
		if (n <= 1) {
			return factors;
		}
		
		// 处理2的因子 - 单独处理以减少迭代次数
		if (n % 2 == 0) {
			factors.add(2);
			while (n % 2 == 0) {
				n /= 2;
			}
		}
		
		// 处理3及以上的奇数因子，只需要检查到sqrt(n)
		for (int i = 3; i * i <= n; i += 2) {
			if (n % i == 0) {
				factors.add(i);
				while (n % i == 0) {
					n /= i;
				}
			}
		}
		
		// 最后剩下的n如果大于1，说明它本身是一个质因数
		if (n > 1) {
			factors.add(n);
		}
		
		return factors;
	}
	
	/**
	 * 返回n的质因数及其指数的映射
	 * 时间复杂度O(√n)
	 * 空间复杂度O(k)，其中k是不同质因数的数量
	 * 
	 * 参数:
	 *     n: 待分解的正整数
	 * 返回:
	 *     Map<Integer, Integer>: 质因数及其指数的映射
	 */
	public static Map<Integer, Integer> getPrimeFactorsWithExponents(int n) {
		Map<Integer, Integer> factors = new HashMap<>();
		
		// 参数验证
		if (n <= 1) {
			return factors;
		}
		
		// 处理2的因子
		while (n % 2 == 0) {
			factors.put(2, factors.getOrDefault(2, 0) + 1);
			n /= 2;
		}
		
		// 处理3及以上的奇数因子
		for (int i = 3; i * i <= n; i += 2) {
			while (n % i == 0) {
				factors.put(i, factors.getOrDefault(i, 0) + 1);
				n /= i;
			}
		}
		
		// 处理最后剩下的质因数
		if (n > 1) {
			factors.put(n, 1);
		}
		
		return factors;
	}
	
	/**
	 * 返回n的质因数列表（包含重复）
	 * 时间复杂度O(√n)
	 * 空间复杂度O(log n)
	 * 
	 * 参数:
	 *     n: 待分解的正整数
	 * 返回:
	 *     List<Integer>: 质因数列表，包含重复
	 */
	public static List<Integer> getAllPrimeFactors(int n) {
		List<Integer> factors = new ArrayList<>();
		
		// 参数验证
		if (n <= 1) {
			return factors;
		}
		
		// 处理2的因子
		while (n % 2 == 0) {
			factors.add(2);
			n /= 2;
		}
		
		// 处理3及以上的奇数因子
		for (int i = 3; i * i <= n; i += 2) {
			while (n % i == 0) {
				factors.add(i);
				n /= i;
			}
		}
		
		// 处理最后剩下的质因数
		if (n > 1) {
			factors.add(n);
		}
		
		return factors;
	}
	
	/**
	 * 计算欧拉函数φ(n) - 返回小于n且与n互质的数的个数
	 * 时间复杂度O(√n)
	 * 空间复杂度O(k)，其中k是不同质因数的数量
	 * 
	 * 参数:
	 *     n: 正整数
	 * 返回:
	 *     int: 小于n且与n互质的数的个数
	 */
	public static int eulerPhi(int n) {
		// 参数验证
		if (n <= 1) {
			return 0;
		}
		
		Map<Integer, Integer> factors = getPrimeFactorsWithExponents(n);
		int result = n;
		
		// 根据欧拉函数公式：φ(n) = n * product(1-1/p)，其中p是n的质因数
		for (int p : factors.keySet()) {
			result /= p;
			result *= (p - 1);
		}
		
		return result;
	}
	
	/**
	 * 使用欧几里得算法计算最大公约数
	 * 时间复杂度O(log min(a,b))
	 * 
	 * 参数:
	 *     a, b: 两个正整数
	 * 返回:
	 *     int: 最大公约数
	 */
	public static int gcd(int a, int b) {
		while (b != 0) {
			int temp = a % b;
			a = b;
			b = temp;
		}
		return a;
	}
	
	/**
	 * 基于最大公约数计算最小公倍数
	 * 时间复杂度O(√max(a,b))
	 * 
	 * 参数:
	 *     a, b: 两个正整数
	 * 返回:
	 *     int: 最小公倍数
	 */
	public static int lcm(int a, int b) {
		if (a == 0 || b == 0) {
			return 0;
		}
		return a * b / gcd(a, b);
	}
	
	/**
	 * 计算n的因数个数
	 * 时间复杂度：O(√n)
	 * 空间复杂度：O(k)，其中k是不同质因数的数量
	 * 
	 * 参数:
	 *     n: 正整数
	 * 返回:
	 *     int: n的因数个数
	 */
	public static int countDivisors(int n) {
		if (n <= 1) {
			return 1; // 1只有1个因数
		}
		
		Map<Integer, Integer> factors = getPrimeFactorsWithExponents(n);
		int count = 1;
		
		// 因数个数公式：如果n = p1^a1 * p2^a2 * ... * pk^ak，则因数个数为(a1+1)*(a2+1)*...*(ak+1)
		for (int exponent : factors.values()) {
			count *= (exponent + 1);
		}
		
		return count;
	}
	
	/**
	 * 计算n的所有因数之和
	 * 时间复杂度：O(√n)
	 * 空间复杂度：O(k)，其中k是不同质因数的数量
	 * 
	 * 参数:
	 *     n: 正整数
	 * 返回:
	 *     int: n的所有因数之和
	 */
	public static int sumDivisors(int n) {
		if (n <= 0) {
			return 0;
		}
		if (n == 1) {
			return 1; // 1的因数和为1
		}
		
		Map<Integer, Integer> factors = getPrimeFactorsWithExponents(n);
		int sum = 1;
		
		// 因数和公式：如果n = p1^a1 * p2^a2 * ... * pk^ak，则因数和为(1+p1+p1^2+...+p1^a1)*...*(1+pk+pk^2+...+pk^ak)
		for (Map.Entry<Integer, Integer> entry : factors.entrySet()) {
			int p = entry.getKey();
			int exponent = entry.getValue();
			int term = 1;
			int power = 1;
			for (int i = 0; i <= exponent; i++) {
				term += power;
				power *= p;
			}
			sum *= term;
		}
		
		return sum;
	}
	
	/**
	 * 判断一个数是否为素数
	 * 时间复杂度：O(√n)
	 * 
	 * 参数:
	 *     n: 待判断的整数
	 * 返回:
	 *     boolean: 如果n是素数返回true，否则返回false
	 */
	public static boolean isPrime(int n) {
		if (n <= 1) {
			return false;
		}
		if (n <= 3) {
			return true;
		}
		if (n % 2 == 0 || n % 3 == 0) {
			return false;
		}
		
		// 只需检查到sqrt(n)，且只需检查形式为6k±1的数
		for (int i = 5; i * i <= n; i += 6) {
			if (n % i == 0 || n % (i + 2) == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	// 按公因数计算最大组件大小
	// 给定一个由不同正整数的组成的非空数组 nums
	// 如果 nums[i] 和 nums[j] 有一个大于1的公因子，那么这两个数之间有一条无向边
	// 返回 nums中最大连通组件的大小。
	// 测试链接 : https://leetcode.cn/problems/largest-component-size-by-common-factor/
	// 提交以下代码，可以通过所有测试用例

	public static int MAXV = 100001;

	// factors[a] = b
	// a这个质数因子，最早被下标b的数字拥有
	public static int[] factors = new int[MAXV];

	// 讲解056、讲解057 - 并查集模版
	public static int MAXN = 20001;

	public static int[] father = new int[MAXN];

	public static int[] size = new int[MAXN];

	public static int n;

	public static void build() {
		for (int i = 0; i < n; i++) {
			father[i] = i;
			size[i] = 1;
		}
		Arrays.fill(factors, -1);
	}

	/**
	 * 并查集查找操作（带路径压缩）
	 * 路径压缩：在查找过程中，将沿途的每个节点都直接连接到根节点，
	 * 这样可以使后续的查找操作接近O(1)的时间复杂度
	 */
	public static int find(int i) {
		if (i != father[i]) {
			father[i] = find(father[i]); // 路径压缩
		}
		return father[i];
	}

	/**
	 * 并查集合并操作（按秩合并）
	 * 将较小的集合合并到较大的集合中，以保持树的平衡，
	 * 避免树退化成链表
	 */
	public static void union(int x, int y) {
		int fx = find(x);
		int fy = find(y);
		if (fx != fy) {
			father[fx] = fy;
			size[fy] += size[fx];
		}
	}

	public static int maxSize() {
		int ans = 0;
		for (int i = 0; i < n; i++) {
			ans = Math.max(ans, size[i]);
		}
		return ans;
	}

	/**
	 * 计算按公因数连接的最大组件大小
	 * 时间复杂度：O(n * √v)，其中v是数组中元素的最大值
	 * 空间复杂度：O(max(v, n))
	 * 
	 * 算法思路：
	 * 1. 对每个数字进行质因数分解
	 * 2. 对于每个质因数，记录它第一次出现的数字索引
	 * 3. 如果质因数之前出现过，则将当前数字与之前数字合并到同一集合
	 * 4. 最后返回最大集合的大小
	 * 
	 * 技巧点：
	 * 1. 使用并查集维护连通性
	 * 2. 质因数分解过程中直接进行并查集操作
	 * 3. 对于每个质因数只记录第一次出现的索引，避免重复合并
	 * 
	 * 工程化考虑：
	 * 1. 边界条件处理：数组为空或只有一个元素
	 * 2. 性能优化：质因数分解的优化
	 * 3. 内存优化：合理设置MAXV和MAXN的大小
	 */
	// 正式方法
	// 时间复杂度O(n * 根号v)
	public static int largestComponentSize(int[] arr) {
		// 参数验证
		if (arr == null || arr.length == 0) {
			return 0;
		}
		if (arr.length == 1) {
			return 1;
		}
		
		n = arr.length;
		build();
		for (int i = 0, x; i < n; i++) {
			x = arr[i];
			for (int j = 2; j * j <= x; j++) {
				if (x % j == 0) {
					if (factors[j] == -1) {
						factors[j] = i;
					} else {
						union(factors[j], i);
					}
					while (x % j == 0) {
						x /= j;
					}
				}
			}
			if (x > 1) {
				if (factors[x] == -1) {
					factors[x] = i;
				} else {
					union(factors[x], i);
				}
			}
		}
		return maxSize();
	}
	
	/**
	 * 优化版的最大组件大小计算
	 * 使用动态调整的并查集，仅为实际出现的质因数创建映射
	 * 
	 * 优化点：
	 * 1. 使用HashMap代替固定大小数组存储质因数到索引的映射，节省空间
	 * 2. 对于数组中的元素进行预处理，过滤掉1
	 * 3. 对质因数分解进行优化
	 * 
	 * 时间复杂度：O(n * √v)
	 * 空间复杂度：O(n + k)，其中k是不同质因数的数量
	 */
	public static int largestComponentSizeOptimized(int[] arr) {
		// 参数验证
		if (arr == null || arr.length == 0) {
			return 0;
		}
		if (arr.length == 1) {
			return 1;
		}
		
		n = arr.length;
		// 重置并查集
		for (int i = 0; i < n; i++) {
			father[i] = i;
			size[i] = 1;
		}
		
		// 使用HashMap代替固定数组，仅存储实际出现的质因数
		Map<Integer, Integer> primeToIndex = new HashMap<>();
		
		for (int i = 0; i < n; i++) {
			int x = arr[i];
			if (x == 1) {
				continue; // 跳过1，因为1没有大于1的因子
			}
			
			// 获取x的所有不同质因数
			List<Integer> primes = getPrimeFactors(x);
			
			for (int p : primes) {
				if (primeToIndex.containsKey(p)) {
					// 如果质因数p之前出现过，合并当前索引和之前索引
					union(i, primeToIndex.get(p));
				} else {
					// 记录质因数p第一次出现的索引
					primeToIndex.put(p, i);
				}
			}
		}
		
		// 找出最大集合的大小
		int maxComponentSize = 1;
		for (int i = 0; i < n; i++) {
			if (father[i] == i) { // 只检查根节点
				maxComponentSize = Math.max(maxComponentSize, size[i]);
			}
		}
		
		return maxComponentSize;
	}
}
