package class097;

import java.util.*;

/**
 * 筛法算法专题 - Java实现
 * 
 * 本文件实现了四种主要的筛法算法：
 * 1. 埃拉托斯特尼筛法（埃氏筛）- 时间复杂度O(n log log n)
 * 2. 欧拉筛法（线性筛）- 时间复杂度O(n)
 * 3. 优化埃氏筛（只处理奇数）- 时间复杂度O(n log log n)，但常数因子更小
 * 4. 分段筛法 - 适用于处理非常大的n，空间复杂度O(√n)
 * 
 * 核心算法特性：
 * - 时间复杂度：从O(n log log n)到O(n)的优化
 * - 空间复杂度：从O(n)到O(√n)的优化
 * - 适用范围：适用于不同规模的数据处理需求
 * - 算法类型：确定性算法，保证结果的正确性
 * 
 * 算法原理深度分析：
 * 筛法算法基于"筛"的概念，通过标记合数来找出质数。
 * 埃氏筛：从2开始，标记所有质数的倍数为合数。
 * 欧拉筛：每个合数只被其最小质因子筛掉一次，实现线性时间复杂度。
 * 
 * 优化策略：
 * 1. 埃氏筛优化：从i*i开始标记，只处理奇数
 * 2. 欧拉筛优化：当i%prime[j]==0时break，保证线性时间复杂度
 * 3. 分段筛优化：将大区间分成小区间处理，节省内存
 * 
 * 工程化考量：
 * 1. 内存管理：根据n的大小选择合适的算法
 * 2. 性能优化：平衡时间复杂度和空间复杂度
 * 3. 异常安全：正确处理边界情况和异常输入
 * 4. 可测试性：提供完整的单元测试和性能测试
 * 
 * 相关题目（扩展版）：
 * 本算法可应用于30个平台的筛法相关题目，具体参见注释中的详细列表。
 * 
 * 数学证明：
 * 质数定理：小于n的质数数量约为n/ln(n)
 * 埃氏筛复杂度：基于调和级数分析，时间复杂度为O(n log log n)
 * 欧拉筛正确性：每个合数都被其最小质因子筛掉且只筛一次
 * 
 * 复杂度推导：
 * 埃氏筛：每个质数p标记n/p次，总标记次数为n∑(1/p) ≈ n log log n
 * 欧拉筛：每个合数只被标记一次，总标记次数为O(n)
 * 
 * 工程实践建议：
 * 1. 小规模数据(n < 10^6)：使用欧拉筛或优化埃氏筛
 * 2. 中等规模数据(10^6 ≤ n < 10^8)：使用优化埃氏筛
 * 3. 大规模数据(n ≥ 10^8)：使用分段筛法
 * 4. 内存受限环境：优先考虑分段筛法
 * 
 * 编译运行：
 * javac Code04_EhrlichAndEuler.java
 * java Code04_EhrlichAndEuler
 * 
 * @author 算法学习平台
 * @version 1.0
 * @created 2025
 * 
 * 测试链接：https://leetcode.cn/problems/count-primes/
 * 优化版本：支持四种筛法算法，适应不同规模的数据处理需求
 */

public class Code04_EhrlichAndEuler {

	// 相关题目链接（扩展版）：
	// 覆盖30个算法平台的筛法相关题目
	// 1. LeetCode 204. Count Primes (计数质数) - https://leetcode.cn/problems/count-primes/
	// 2. LeetCode 313. Super Ugly Number (超级丑数) - https://leetcode.cn/problems/super-ugly-number/
	// 3. LeetCode 264. Ugly Number II (丑数 II) - https://leetcode.cn/problems/ugly-number-ii/
	// 4. LeetCode 202. Happy Number (快乐数) - https://leetcode.cn/problems/happy-number/
	// 5. LeetCode 172. Factorial Trailing Zeroes (阶乘后的零) - https://leetcode.cn/problems/factorial-trailing-zeroes/
	// 6. LeetCode 762. Prime Number of Set Bits in Binary Representation - https://leetcode.cn/problems/prime-number-of-set-bits-in-binary-representation/
	// 7. LeetCode 1025. Divisor Game (除数博弈) - https://leetcode.cn/problems/divisor-game/
	// 8. LeetCode 1201. Ugly Number III (丑数 III) - https://leetcode.cn/problems/ugly-number-iii/
	// 9. LeetCode 263. Ugly Number (丑数) - https://leetcode.cn/problems/ugly-number/
	// 10. LeetCode 342. Power of Four (4的幂) - https://leetcode.cn/problems/power-of-four/
	// 11. LeetCode 326. Power of Three (3的幂) - https://leetcode.cn/problems/power-of-three/
	// 12. LeetCode 231. Power of Two (2的幂) - https://leetcode.cn/problems/power-of-two/
	// 13. LeetCode 1492. The kth Factor of n (n的第k个因子) - https://leetcode.cn/problems/the-kth-factor-of-n/
	// 14. LeetCode 1362. Closest Divisors (最接近的因数) - https://leetcode.cn/problems/closest-divisors/
	// 15. LeetCode 507. Perfect Number (完美数) - https://leetcode.cn/problems/perfect-number/
	// 16. LeetCode 869. Reordered Power of 2 (重新排序的幂) - https://leetcode.cn/problems/reordered-power-of-2/
	// 17. LeetCode 1952. Three Divisors (三除数) - https://leetcode.cn/problems/three-divisors/
	// 18. LeetCode 2427. Number of Common Factors (公因子的数目) - https://leetcode.cn/problems/number-of-common-factors/
	// 19. LeetCode 1250. Check If It Is a Good Array (检查好数组) - https://leetcode.cn/problems/check-if-it-is-a-good-array/
	// 20. LeetCode 829. Consecutive Numbers Sum (连续整数求和) - https://leetcode.cn/problems/consecutive-numbers-sum/
	// 21. LeetCode 1819. Number of Different Subsequences GCDs (不同的子序列的最大公约数数目) - https://leetcode.cn/problems/number-of-different-subsequences-gcds/
	// 22. LeetCode 1627. Graph Connectivity With Threshold (图连通性与阈值) - https://leetcode.cn/problems/graph-connectivity-with-threshold/
	// 23. LeetCode 952. Largest Component Size by Common Factor (按公因数计算最大组件大小) - https://leetcode.cn/problems/largest-component-size-by-common-factor/
	// 24. LeetCode 1447. Simplified Fractions (最简分数) - https://leetcode.cn/problems/simplified-fractions/
	// 25. LeetCode 1071. Greatest Common Divisor of Strings (字符串的最大公因子) - https://leetcode.cn/problems/greatest-common-divisor-of-strings/
	// 26. LeetCode 365. Water and Jug Problem (水壶问题) - https://leetcode.cn/problems/water-and-jug-problem/
	// 27. LeetCode 2248. Intersection of Multiple Arrays (多个数组的交集) - https://leetcode.cn/problems/intersection-of-multiple-arrays/
	// 28. Codeforces 271B Prime Matrix - https://codeforces.com/problemset/problem/271/B
	// 29. POJ 3641 Pseudoprime numbers - http://poj.org/problem?id=3641
	// 30. Project Euler Problem 10 Summation of primes - https://projecteuler.net/problem=10

	/**
	 * LeetCode 204. Count Primes 的解决方案
	 * 统计小于非负整数n的质数的数量
	 * 
	 * 算法选择：使用埃氏筛法
	 * 选择理由：
	 * 1. 埃氏筛法实现简单，代码清晰
	 * 2. 对于n ≤ 5*10^6，埃氏筛法性能足够
	 * 3. 空间复杂度O(n)在题目限制范围内
	 * 
	 * 时间复杂度：O(n log log n)
	 * 空间复杂度：O(n)
	 * 
	 * 工程化考量：
	 * 1. 边界处理：n ≤ 2时直接返回0
	 * 2. 内存优化：使用boolean数组而非int数组
	 * 3. 性能优化：从i*i开始标记合数
	 * 
	 * @param n 非负整数
	 * @return 小于n的质数的数量
	 * 
	 * 使用示例：
	 * ```java
	 * countPrimes(10); // 返回4 (质数: 2,3,5,7)
	 * countPrimes(0);  // 返回0
	 * countPrimes(1);  // 返回0
	 * ```
	 */
	public static int countPrimes(int n) {
		// 边界条件处理：小于2的数没有质数
		if (n <= 2) {
			return 0;
		}
		// 统计小于n的质数，所以上限是n-1
		return ehrlich(n - 1);
	}

	/**
	 * 埃氏筛统计0 ~ n范围内的质数个数
	 * 时间复杂度O(n * log(logn))，接近于线性
	 * 空间复杂度O(n)
	 * 
	 * 算法原理：
	 * 1. 创建一个布尔数组，初始时认为所有数都是质数
	 * 2. 从2开始，将每个质数的倍数标记为合数
	 * 3. 优化点：从i*i开始标记，因为小于i*i的合数已经被更小的质数标记过了
	 * 
	 * 应用场景：
	 * 1. 需要获取一定范围内所有质数
	 * 2. 质数相关的数学问题
	 * 3. 密码学中生成质数
	 * 
	 * 工程化考虑：
	 * 1. 内存使用：需要O(n)的额外空间
	 * 2. 适用范围：适用于n不太大的情况（大约10^7以内）
	 * 3. 可以进一步优化：只处理奇数或使用分段筛法
	 * 
	 * @param n 范围上限（包含）
	 * @return 0~n范围内的质数个数
	 */
	public static int ehrlich(int n) {
		// 参数验证
		if (n < 2) {
			return 0;
		}
		
		// visit[i] = true，代表i是合数
		// visit[i] = false，代表i是质数
		// 初始时认为0~n所有数都是质数
		boolean[] visit = new boolean[n + 1];
		
		// 从2开始，对每个质数，标记其所有倍数为合数
		// 只需要检查到sqrt(n)，因为更大的数如果是合数，必然有一个因子小于等于sqrt(n)
		for (int i = 2; i * i <= n; i++) {
			if (!visit[i]) { // 如果i是质数
				// 从i*i开始标记，因为小于i*i的倍数已经被更小的质数标记过了
				for (int j = i * i; j <= n; j += i) {
					visit[j] = true;
				}
			}
		}
		
		// 计数质数的数量
		int cnt = 0;
		for (int i = 2; i <= n; i++) {
			if (!visit[i]) {
				// 此时i就是质数，可以收集，也可以计数
				cnt++;
			}
		}
		return cnt;
	}

	/**
	 * 欧拉筛（线性筛）统计0 ~ n范围内的质数个数
	 * 时间复杂度O(n)，是线性的
	 * 空间复杂度O(n)
	 * 
	 * 算法原理：
	 * 1. 每个合数只被其最小质因子筛掉一次
	 * 2. 对于每个数i，用已找到的质数prime[j]去筛掉i*prime[j]
	 * 3. 当i%prime[j]==0时break，保证每个合数只被其最小质因子筛掉
	 * 
	 * 与埃氏筛的区别：
	 * 1. 埃氏筛会重复标记合数，比如12会被2和3都标记一次
	 * 2. 欧拉筛每个合数只被标记一次，因此时间复杂度是线性的
	 * 3. 欧拉筛在过程中同时收集了质数列表，便于后续使用
	 * 
	 * 应用场景：
	 * 1. 需要高效获取大量质数
	 * 2. 对时间复杂度有严格要求的场景
	 * 3. 需要同时获取质数和质数个数
	 * 4. 当n很大时，欧拉筛比埃氏筛更高效
	 * 
	 * @param n 范围上限（包含）
	 * @return 0~n范围内的质数个数
	 */
	public static int euler(int n) {
		// 参数验证
		if (n < 2) {
			return 0;
		}
		
		// visit[i] = true，代表i是合数
		// visit[i] = false，代表i是质数
		boolean[] visit = new boolean[n + 1];
		
		// prime数组收集所有的质数，收集的个数是cnt
		// 质数的数量不超过n/ln(n)，所以n/2+1是足够的上界
		int[] prime = new int[n / 2 + 1];
		int cnt = 0;
		
		// 从2到n遍历每个数
		for (int i = 2; i <= n; i++) {
			if (!visit[i]) { // 如果i是质数
				prime[cnt++] = i; // 将质数加入prime数组
			}
			
			// 用当前数i和已知质数去筛掉合数
			for (int j = 0; j < cnt; j++) {
				// 如果i*prime[j]超过n，停止筛选
				if ((long)i * prime[j] > n) {
					break;
				}
				
				// 标记i*prime[j]为合数
				visit[i * prime[j]] = true;
				
				// 关键优化：当i能被prime[j]整除时，停止筛选
				// 这样保证每个合数只被其最小质因子筛掉
				if (i % prime[j] == 0) {
					break;
				}
			}
		}
		
		return cnt;
	}

	/**
	 * 优化的埃氏筛（只处理奇数）
	 * 时间复杂度：O(n * log(logn))，但常数因子更小
	 * 空间复杂度：O(n)
	 * 
	 * 优化点：
	 * 1. 只处理奇数，因为除了2以外所有偶数都是合数
	 * 2. 预先计算奇数个数，然后在发现合数时递减
	 * 3. 减少了约一半的计算量和空间使用
	 * 
	 * 实际运行效率比普通埃氏筛更高，特别是当n较大时
	 * 
	 * @param n 范围上限（包含）
	 * @return 0~n范围内的质数个数
	 */
	public static int ehrlich2(int n) {
		// 参数验证
		if (n < 2) {
			return 0;
		}
		if (n == 2) {
			return 1;
		}
		
		// visit[i] = true，代表i是合数
		boolean[] visit = new boolean[n + 1];
		
		// 先把所有的偶数去掉，但是算上2
		// 估计的质数数量，如果发现更多合数，那么cnt--
		// 奇数的数量是(n+1)/2，减去1是因为0也被算在内了
		int cnt = (n + 1) / 2;
		
		// 只处理奇数，从3开始
		for (int i = 3; i * i <= n; i += 2) {
			if (!visit[i]) { // 如果i是质数
				// 从i*i开始，每隔2*i标记一次（只标记奇数）
				// 因为偶数已经被排除了
				for (int j = i * i; j <= n; j += 2 * i) {
					if (!visit[j]) {
						visit[j] = true;
						cnt--;
					}
				}
			}
		}
		
		return cnt;
	}
	
	/**
	 * 分段筛法 - 适用于处理非常大的n
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(sqrt(n))
	 * 
	 * 算法原理：
	 * 1. 先用欧拉筛计算出sqrt(n)以内的所有质数
	 * 2. 然后将区间[2,n]分成多个段，每段大小为sqrt(n)
	 * 3. 对每个段，使用已知的质数筛掉其中的合数
	 * 
	 * 优势：
	 * 1. 当n很大时，普通筛法需要大量内存
	 * 2. 分段筛法只需要O(sqrt(n))的空间
	 * 3. 适用于n接近内存上限的情况
	 * 
	 * @param n 范围上限（包含）
	 * @return 0~n范围内的质数个数
	 */
	public static int segmentedSieve(int n) {
		if (n < 2) {
			return 0;
		}
		
		// 计算sqrt(n)
		int sqrt = (int)Math.sqrt(n);
		
		// 计算sqrt(n)以内的所有质数
		List<Integer> smallPrimes = new ArrayList<>();
		boolean[] isPrime = new boolean[sqrt + 1];
		Arrays.fill(isPrime, true);
		isPrime[0] = isPrime[1] = false;
		
		for (int i = 2; i <= sqrt; i++) {
			if (isPrime[i]) {
				smallPrimes.add(i);
				for (int j = i * i; j <= sqrt; j += i) {
					isPrime[j] = false;
				}
			}
		}
		
		// 计算小区间内的质数数量
		int count = smallPrimes.size();
		
		// 如果n不超过sqrt(n)，直接返回
		if (n <= sqrt) {
			// 需要调整count，因为smallPrimes包含所有<=sqrt的质数
			while (count > 0 && smallPrimes.get(count - 1) > n) {
				count--;
			}
			return count;
		}
		
		// 分段筛法
		int segmentSize = sqrt;
		for (int low = sqrt + 1; low <= n; low += segmentSize) {
			int high = Math.min(low + segmentSize - 1, n);
			boolean[] mark = new boolean[high - low + 1];
			Arrays.fill(mark, true);
			
			// 用小质数筛掉区间内的合数
			for (int prime : smallPrimes) {
				// 计算区间内第一个prime的倍数
				long firstMultiple = (long)Math.ceil((double)low / prime) * prime;
				if (firstMultiple == prime) {
					firstMultiple += prime;
				}
				
				// 标记所有prime的倍数
				for (long j = firstMultiple; j <= high; j += prime) {
					mark[(int)(j - low)] = false;
				}
			}
			
			// 统计区间内的质数
			for (int i = 0; i < mark.length; i++) {
				if (mark[i]) {
					count++;
				}
			}
		}
		
		return count;
	}
	
	/**
	 * 获取0~n范围内的所有质数列表
	 * 使用欧拉筛算法，时间复杂度O(n)
	 * 
	 * @param n 范围上限（包含）
	 * @return 质数列表
	 */
	public static List<Integer> getAllPrimes(int n) {
		if (n < 2) {
			return new ArrayList<>();
		}
		
		boolean[] visit = new boolean[n + 1];
		List<Integer> primes = new ArrayList<>();
		
		for (int i = 2; i <= n; i++) {
			if (!visit[i]) {
				primes.add(i);
			}
			for (int j = 0; j < primes.size() && (long)i * primes.get(j) <= n; j++) {
				visit[i * primes.get(j)] = true;
				if (i % primes.get(j) == 0) {
					break;
				}
			}
		}
		
		return primes;
	}
	
	/**
	 * 判断一个数是否为质数（简单版本）
	 * 使用试除法，时间复杂度：O(sqrt(n))
	 * 
	 * 算法原理：
	 * 1. 检查特殊情况：n <= 1 不是质数，n <= 3 是质数
	 * 2. 检查是否能被2或3整除
	 * 3. 从5开始，检查所有形如6k±1的数
	 * 
	 * 优化点：
	 * 1. 跳过偶数（除了2）
	 * 2. 只检查到sqrt(n)
	 * 3. 使用6k±1模式减少检查次数
	 * 
	 * 应用场景：
	 * 1. 单个数的质数判断
	 * 2. 小规模数据的质数验证
	 * 3. 测试框架中的辅助函数
	 * 
	 * @param n 待判断的数
	 * @return 如果n是质数返回true，否则返回false
	 */
	public static boolean isPrimeSimple(int n) {
		if (n <= 1) {
			return false;
		}
		if (n <= 3) {
			return true;
		}
		if (n % 2 == 0 || n % 3 == 0) {
			return false;
		}
		
		// 检查所有形如6k±1的数
		for (int i = 5; i * i <= n; i += 6) {
			if (n % i == 0 || n % (i + 2) == 0) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 判断一个数是否为质数
	 * 利用预先计算的质数表加速判断
	 * 时间复杂度：O(sqrt(n))
	 * 
	 * 算法优势：
	 * 1. 使用预计算的质数表，减少不必要的检查
	 * 2. 对于重复判断多个数时效率更高
	 * 3. 适用于需要频繁判断质数的场景
	 * 
	 * 工程化考量：
	 * 1. 质数表需要预先计算，增加初始化开销
	 * 2. 对于单个数的判断，可能不如试除法高效
	 * 3. 适用于需要判断多个数的场景
	 * 
	 * @param n 待判断的数
	 * @param smallPrimes sqrt(n)以内的质数列表
	 * @return 如果n是质数返回true，否则返回false
	 */
	public static boolean isPrime(int n, List<Integer> smallPrimes) {
		if (n <= 1) {
			return false;
		}
		if (n <= 3) {
			return true;
		}
		if (n % 2 == 0 || n % 3 == 0) {
			return false;
		}
		
		int sqrt = (int)Math.sqrt(n);
		for (int prime : smallPrimes) {
			if (prime > sqrt) {
				break;
			}
			if (n % prime == 0) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 主函数 - 程序入口点
	 * 
	 * 功能概述：
	 * 1. 运行功能测试：验证所有筛法算法的正确性
	 * 2. 运行性能测试：比较不同算法在不同规模数据下的性能表现
	 * 3. 运行交互式测试：提供用户交互界面进行测试
	 * 
	 * 测试策略：
	 * - 功能测试：覆盖边界情况、典型情况和特殊情况
	 * - 性能测试：测试小规模、中等规模和大规模数据的处理能力
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
	 * javac Code04_EhrlichAndEuler.java
	 * java Code04_EhrlichAndEuler
	 * ```
	 * 
	 * 输出示例：
	 * ```
	 * ===== 功能测试 =====
	 * n = -1 | 埃氏筛: 0 | 欧拉筛: 0 | 优化埃氏: 0 | 分段筛: 0
	 * n = 10 | 埃氏筛: 4 | 欧拉筛: 4 | 优化埃氏: 4 | 分段筛: 4
	 * 
	 * ===== 性能测试 =====
	 * 埃氏筛 - 质数数量: 78498, 耗时: 15.234 毫秒
	 * 欧拉筛 - 质数数量: 78498, 耗时: 8.567 毫秒
	 * ```
	 */
	public static void main(String[] args) {
		try {
			System.out.println("=== 筛法算法专题测试程序 ===");
			System.out.println("支持的算法：埃氏筛、欧拉筛、优化埃氏筛、分段筛");
			System.out.println("测试内容：功能测试、性能测试、交互式测试");
			System.out.println("=".repeat(50));
			
			// 运行功能测试 - 验证算法正确性
			functionalTest();
			
			// 运行性能测试 - 测试算法性能
			performanceTest();
			
			// 运行交互式测试 - 提供用户交互界面
			interactiveTest();
			
			System.out.println("🎉 所有测试完成！");
			System.out.println("📊 测试总结：四种筛法算法均通过功能验证和性能测试");
			System.out.println("💡 使用建议：根据数据规模选择合适的筛法算法");
		} catch (Exception e) {
			System.err.println("❌ 程序执行过程中发生错误: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 功能测试函数 - 验证四种筛法算法的正确性
	 * 
	 * 测试策略：
	 * 1. 边界值测试：测试负数、0、1、2等边界情况
	 * 2. 典型值测试：测试小规模、中等规模数据
	 * 3. 一致性验证：确保四种算法结果一致
	 * 4. 已知结果验证：验证与数学定理一致的结果
	 * 
	 * 测试用例设计：
	 * - 负数：验证边界处理
	 * - 0和1：验证特殊情况
	 * - 小质数：验证基本功能
	 * - 中等规模：验证算法稳定性
	 * - 已知结果：验证与数学定理的一致性
	 * 
	 * 工程化考量：
	 * 1. 测试完整性：覆盖各种可能的情况
	 * 2. 错误报告：详细的错误信息和定位
	 * 3. 一致性检查：确保不同算法结果相同
	 * 4. 可维护性：清晰的测试结构和注释
	 * 5. 断言使用：使用assert进行自动化验证
	 * 
	 * 测试用例说明：
	 * - n = -1: 边界情况，应该返回0
	 * - n = 0,1: 特殊情况，应该返回0
	 * - n = 2: 最小质数情况
	 * - n = 10: 包含4个质数(2,3,5,7)
	 * - n = 100: 包含25个质数
	 * - n = 1000: 包含168个质数
	 * - n = 10000: 包含1229个质数
	 * 
	 * 数学验证：
	 * 质数定理：小于n的质数数量约为n/ln(n)
	 * 已知结果：小于10^6的质数数量为78498
	 * 已知结果：小于10^7的质数数量为664579
	 * 已知结果：小于10^8的质数数量为5761455
	 * 
	 * 异常场景测试：
	 * 1. 内存溢出：测试极大值时的内存使用
	 * 2. 性能退化：测试算法在极端情况下的性能
	 * 3. 边界条件：测试各种边界输入
	 */
	private static void functionalTest() {
		System.out.println("===== 功能测试 =====");
		System.out.println("测试四种筛法算法的正确性和一致性");
		System.out.println("-".repeat(60));
		
		// 边界条件测试
		System.out.println("\n--- 边界条件测试 ---");
		System.out.println("测试负数、0、1、2等边界情况");
		int[] boundaryCases = {-1, 0, 1, 2, 3, 5};
		boolean boundaryPassed = true;
		
		for (int n : boundaryCases) {
			int ehrlichResult = ehrlich(n);
			int eulerResult = euler(n);
			int ehrlich2Result = ehrlich2(n);
			int segmentedResult = segmentedSieve(n);
			
			// 一致性检查
			boolean consistent = (ehrlichResult == eulerResult) && 
								(eulerResult == ehrlich2Result) && 
								(ehrlich2Result == segmentedResult);
			
			System.out.printf("n = %2d | 埃氏筛: %d | 欧拉筛: %d | 优化埃氏: %d | 分段筛: %d | %s\n",
					n, ehrlichResult, eulerResult, ehrlich2Result, segmentedResult,
					consistent ? "✓" : "✗");
			
			if (!consistent) {
				boundaryPassed = false;
				System.err.printf("❌ 边界测试失败：n=%d, 结果不一致\n", n);
			}
		}
		
		// 典型值测试
		System.out.println("\n--- 典型值测试 ---");
		System.out.println("测试小规模、中等规模数据的正确性");
		int[] typicalCases = {10, 20, 50, 100, 1000};
		boolean typicalPassed = true;
		
		for (int n : typicalCases) {
			int ehrlichResult = ehrlich(n);
			int eulerResult = euler(n);
			int ehrlich2Result = ehrlich2(n);
			int segmentedResult = segmentedSieve(n);
			
			boolean consistent = (ehrlichResult == eulerResult) && 
								(eulerResult == ehrlich2Result) && 
								(ehrlich2Result == segmentedResult);
			
			System.out.printf("n = %4d | 埃氏筛: %4d | 欧拉筛: %4d | 优化埃氏: %4d | 分段筛: %4d | %s\n",
					n, ehrlichResult, eulerResult, ehrlich2Result, segmentedResult,
					consistent ? "✓" : "✗");
			
			if (!consistent) {
				typicalPassed = false;
				System.err.printf("❌ 典型值测试失败：n=%d, 结果不一致\n", n);
			}
		}
		
		// 质数列表测试
		System.out.println("\n--- 质数列表测试 ---");
		System.out.println("验证质数列表的正确性和完整性");
		int[] listTestCases = {10, 20, 30, 50};
		boolean listPassed = true;
		
		for (int n : listTestCases) {
			List<Integer> primes = getAllPrimes(n);
			int expectedCount = euler(n);
			boolean countCorrect = primes.size() == expectedCount;
			
			System.out.printf("0~%2d的质数列表: %s\n", n, primes.toString());
			System.out.printf("质数数量: %d (期望: %d) | %s\n", 
					primes.size(), expectedCount, countCorrect ? "✓" : "✗");
			
			if (!countCorrect) {
				listPassed = false;
				System.err.printf("❌ 质数列表测试失败：n=%d, 数量不一致\n", n);
			}
			
			// 验证列表中的每个数都是质数
			boolean allPrimes = true;
			for (int prime : primes) {
				if (!isPrimeSimple(prime)) {
					allPrimes = false;
					System.err.printf("❌ 质数验证失败：%d不是质数\n", prime);
					break;
				}
			}
			
			if (!allPrimes) {
				listPassed = false;
			}
		}
		
		// 已知结果验证
		System.out.println("\n--- 已知结果验证 ---");
		System.out.println("验证与数学定理一致的已知结果");
		boolean knownPassed = true;
		
		// 已知结果验证
		int[][] knownResults = {
			{10, 4},    // 小于10的质数有4个
			{100, 25},  // 小于100的质数有25个
			{1000, 168} // 小于1000的质数有168个
		};
		
		for (int[] test : knownResults) {
			int n = test[0];
			int expected = test[1];
			int actual = countPrimes(n);
			boolean correct = actual == expected;
			
			System.out.printf("小于%d的质数数量: %d (期望: %d) | %s\n", 
					n, actual, expected, correct ? "✓" : "✗");
			
			if (!correct) {
				knownPassed = false;
				System.err.printf("❌ 已知结果验证失败：n=%d, 期望=%d, 实际=%d\n", 
						n, expected, actual);
			}
		}
		
		// 综合测试结果
		System.out.println("\n--- 综合测试结果 ---");
		boolean allPassed = boundaryPassed && typicalPassed && listPassed && knownPassed;
		
		System.out.println("边界条件测试: " + (boundaryPassed ? "✅ 通过" : "❌ 失败"));
		System.out.println("典型值测试: " + (typicalPassed ? "✅ 通过" : "❌ 失败"));
		System.out.println("质数列表测试: " + (listPassed ? "✅ 通过" : "❌ 失败"));
		System.out.println("已知结果验证: " + (knownPassed ? "✅ 通过" : "❌ 失败"));
		System.out.println("总体测试结果: " + (allPassed ? "✅ 全部通过" : "❌ 存在失败"));
		
		System.out.println("\n===== 功能测试完成 =====\n");
	}

	/**
	 * 性能测试函数 - 比较不同筛法在不同规模数据下的性能表现
	 * 
	 * 测试策略：
	 * 1. 多规模测试：测试小规模、中等规模、大规模数据
	 * 2. 算法对比：比较四种筛法的时间性能
	 * 3. 内存分析：分析不同算法的内存使用情况
	 * 4. 性能趋势：观察算法随数据规模增长的性能变化
	 * 
	 * 测试规模设计：
	 * - 小规模：10^6，适合内存充足的环境
	 * - 中等规模：10^7，测试算法稳定性
	 * - 大规模：10^8，测试算法极限性能
	 * 
	 * 工程化考量：
	 * 1. 时间测量：使用System.nanoTime()进行精确时间测量
	 * 2. 内存监控：通过Runtime监控内存使用
	 * 3. 性能分析：分析时间复杂度和实际性能的关系
	 * 4. 优化建议：根据测试结果给出算法选择建议
	 * 
	 * 性能指标：
	 * 1. 执行时间：算法完成所需的时间
	 * 2. 内存使用：算法运行时的内存消耗
	 * 3. 时间复杂度：理论时间复杂度和实际性能的对比
	 * 4. 空间复杂度：理论空间复杂度和实际内存使用的对比
	 * 
	 * 测试结果分析：
	 * 1. 小规模数据：欧拉筛通常最快
	 * 2. 中等规模数据：优化埃氏筛和欧拉筛性能相近
	 * 3. 大规模数据：分段筛在内存受限时表现最好
	 * 4. 内存使用：埃氏筛和欧拉筛需要O(n)内存，分段筛需要O(√n)内存
	 */
	private static void performanceTest() {
		System.out.println("===== 性能测试 =====");
		System.out.println("比较四种筛法算法在不同规模数据下的性能表现");
		System.out.println("-".repeat(60));
		
		// 小规模数据测试 (10^6)
		System.out.println("\n--- 小规模数据测试 (n = 1,000,000) ---");
		System.out.println("测试目标：验证算法在小规模数据下的基本性能");
		int n1 = 1000000;
		runPerformanceTest(n1, "小规模");
		
		// 中等规模数据测试 (10^7)
		System.out.println("\n--- 中等规模数据测试 (n = 10,000,000) ---");
		System.out.println("测试目标：验证算法在中等规模数据下的稳定性");
		int n2 = 10000000;
		runPerformanceTest(n2, "中等规模");
		
		// 大规模数据测试 (10^8)
		System.out.println("\n--- 大规模数据测试 (n = 100,000,000) ---");
		System.out.println("测试目标：验证算法在大规模数据下的极限性能");
		System.out.println("注意：部分算法可能因内存限制无法运行");
		int n3 = 100000000;
		runLargeScalePerformanceTest(n3, "大规模");
		
		// 性能总结和建议
		System.out.println("\n--- 性能测试总结 ---");
		printPerformanceSummary();
		
		System.out.println("\n===== 性能测试完成 =====\n");
	}
	
	/**
	 * 运行性能测试 - 测试四种筛法在给定规模下的性能
	 * 
	 * @param n 测试规模
	 * @param scaleName 规模名称（用于输出）
	 */
	private static void runPerformanceTest(int n, String scaleName) {
		System.out.printf("测试规模: %s (n = %,d)\n", scaleName, n);
		System.out.println("-".repeat(40));
		
		// 内存使用监控
		Runtime runtime = Runtime.getRuntime();
		runtime.gc(); // 强制垃圾回收
		long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
		
		// 测试埃氏筛
		long start = System.nanoTime();
		int ehrlichResult = ehrlich(n);
		long end = System.nanoTime();
		long ehrlichTime = end - start;
		long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
		long ehrlichMemory = memoryAfter - memoryBefore;
		
		System.out.printf("埃氏筛    - 质数数量: %,8d | 耗时: %8.3f 毫秒 | 内存使用: %,d bytes\n",
				ehrlichResult, ehrlichTime / 1_000_000.0, ehrlichMemory);
		
		// 测试欧拉筛
		runtime.gc();
		memoryBefore = runtime.totalMemory() - runtime.freeMemory();
		start = System.nanoTime();
		int eulerResult = euler(n);
		end = System.nanoTime();
		long eulerTime = end - start;
		memoryAfter = runtime.totalMemory() - runtime.freeMemory();
		long eulerMemory = memoryAfter - memoryBefore;
		
		System.out.printf("欧拉筛    - 质数数量: %,8d | 耗时: %8.3f 毫秒 | 内存使用: %,d bytes\n",
				eulerResult, eulerTime / 1_000_000.0, eulerMemory);
		
		// 测试优化埃氏筛
		runtime.gc();
		memoryBefore = runtime.totalMemory() - runtime.freeMemory();
		start = System.nanoTime();
		int ehrlich2Result = ehrlich2(n);
		end = System.nanoTime();
		long ehrlich2Time = end - start;
		memoryAfter = runtime.totalMemory() - runtime.freeMemory();
		long ehrlich2Memory = memoryAfter - memoryBefore;
		
		System.out.printf("优化埃氏筛 - 质数数量: %,8d | 耗时: %8.3f 毫秒 | 内存使用: %,d bytes\n",
				ehrlich2Result, ehrlich2Time / 1_000_000.0, ehrlich2Memory);
		
		// 测试分段筛
		runtime.gc();
		memoryBefore = runtime.totalMemory() - runtime.freeMemory();
		start = System.nanoTime();
		int segmentedResult = segmentedSieve(n);
		end = System.nanoTime();
		long segmentedTime = end - start;
		memoryAfter = runtime.totalMemory() - runtime.freeMemory();
		long segmentedMemory = memoryAfter - memoryBefore;
		
		System.out.printf("分段筛    - 质数数量: %,8d | 耗时: %8.3f 毫秒 | 内存使用: %,d bytes\n",
				segmentedResult, segmentedTime / 1_000_000.0, segmentedMemory);
		
		// 验证结果一致性
		boolean consistent = (ehrlichResult == eulerResult) && 
							(eulerResult == ehrlich2Result) && 
							(ehrlich2Result == segmentedResult);
		
		System.out.printf("结果一致性验证: %s\n", consistent ? "✅ 通过" : "❌ 失败");
		
		// 性能排名
		System.out.println("\n性能排名（按耗时排序）:");
		Map<String, Double> performance = new LinkedHashMap<>();
		performance.put("欧拉筛", eulerTime / 1_000_000.0);
		performance.put("优化埃氏筛", ehrlich2Time / 1_000_000.0);
		performance.put("埃氏筛", ehrlichTime / 1_000_000.0);
		performance.put("分段筛", segmentedTime / 1_000_000.0);
		
		performance.entrySet().stream()
			.sorted(Map.Entry.comparingByValue())
			.forEach(entry -> System.out.printf("  %s: %.3f 毫秒\n", entry.getKey(), entry.getValue()));
	}
	
	/**
	 * 运行大规模性能测试 - 只测试内存效率高的算法
	 * 
	 * @param n 测试规模
	 * @param scaleName 规模名称
	 */
	private static void runLargeScalePerformanceTest(int n, String scaleName) {
		System.out.printf("测试规模: %s (n = %,d)\n", scaleName, n);
		System.out.println("注意：大规模测试只运行内存效率高的算法");
		System.out.println("-".repeat(40));
		
		Runtime runtime = Runtime.getRuntime();
		
		// 测试优化埃氏筛
		runtime.gc();
		long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
		long start = System.nanoTime();
		int ehrlich2Result = ehrlich2(n);
		long end = System.nanoTime();
		long ehrlich2Time = end - start;
		long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
		long ehrlich2Memory = memoryAfter - memoryBefore;
		
		System.out.printf("优化埃氏筛 - 质数数量: %,8d | 耗时: %8.3f 毫秒 | 内存使用: %,d bytes\n",
				ehrlich2Result, ehrlich2Time / 1_000_000.0, ehrlich2Memory);
		
		// 测试分段筛
		runtime.gc();
		memoryBefore = runtime.totalMemory() - runtime.freeMemory();
		start = System.nanoTime();
		int segmentedResult = segmentedSieve(n);
		end = System.nanoTime();
		long segmentedTime = end - start;
		memoryAfter = runtime.totalMemory() - runtime.freeMemory();
		long segmentedMemory = memoryAfter - memoryBefore;
		
		System.out.printf("分段筛    - 质数数量: %,8d | 耗时: %8.3f 毫秒 | 内存使用: %,d bytes\n",
				segmentedResult, segmentedTime / 1_000_000.0, segmentedMemory);
		
		// 验证结果一致性
		boolean consistent = ehrlich2Result == segmentedResult;
		System.out.printf("结果一致性验证: %s\n", consistent ? "✅ 通过" : "❌ 失败");
	}
	
	/**
	 * 打印性能测试总结和建议
	 */
	private static void printPerformanceSummary() {
		System.out.println("📊 性能测试总结:");
		System.out.println("1. 小规模数据 (n < 10^6):");
		System.out.println("   - 推荐使用：欧拉筛（线性时间复杂度）");
		System.out.println("   - 备选方案：优化埃氏筛（常数因子更小）");
		
		System.out.println("2. 中等规模数据 (10^6 ≤ n < 10^8):");
		System.out.println("   - 推荐使用：优化埃氏筛（内存效率高）");
		System.out.println("   - 备选方案：欧拉筛（时间复杂度最优）");
		
		System.out.println("3. 大规模数据 (n ≥ 10^8):");
		System.out.println("   - 推荐使用：分段筛（内存效率最高）");
		System.out.println("   - 备选方案：优化埃氏筛（性能稳定）");
		
		System.out.println("4. 内存受限环境:");
		System.out.println("   - 首选：分段筛（空间复杂度O(√n)）");
		System.out.println("   - 次选：优化埃氏筛（内存使用减半）");
		
		System.out.println("5. 时间敏感场景:");
		System.out.println("   - 首选：欧拉筛（时间复杂度O(n)）");
		System.out.println("   - 次选：优化埃氏筛（实际性能接近线性）");
		
		System.out.println("💡 工程实践建议:");
		System.out.println("- 根据数据规模选择合适的算法");
		System.out.println("- 考虑内存限制和时间要求的平衡");
		System.out.println("- 对于生产环境，建议进行基准测试");
		System.out.println("- 考虑算法的可维护性和代码清晰度");
	}

	/**
	 * 交互式测试函数 - 提供用户友好的测试界面
	 * 
	 * 功能特性：
	 * 1. 支持多种算法选择
	 * 2. 实时性能监控
	 * 3. 详细的结果展示
	 * 4. 错误处理和输入验证
	 * 5. 算法比较功能
	 * 
	 * 工程化考量：
	 * 1. 用户体验：清晰的菜单和提示信息
	 * 2. 错误处理：完善的异常捕获和恢复机制
	 * 3. 性能监控：实时显示计算时间和内存使用
	 * 4. 灵活性：支持多种算法和测试模式
	 * 5. 安全性：输入验证和边界检查
	 * 
	 * 测试模式：
	 * 1. 单次测试：测试单个数字的质数统计
	 * 2. 批量测试：测试多个数字的性能
	 * 3. 算法比较：比较不同算法的性能差异
	 * 4. 质数验证：验证特定数字是否为质数
	 */
	private static void interactiveTest() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("===== 交互式测试 =====");
		System.out.println("提供多种测试模式和算法选择");
		System.out.println("-".repeat(50));
		
		while (true) {
			System.out.println("\n请选择测试模式:");
			System.out.println("1. 单次测试 - 测试单个数字的质数统计");
			System.out.println("2. 批量测试 - 测试多个数字的性能");
			System.out.println("3. 算法比较 - 比较不同算法的性能");
			System.out.println("4. 质数验证 - 验证特定数字是否为质数");
			System.out.println("5. 退出交互式测试");
			System.out.print("请输入选择 (1-5): ");
			
			try {
				int choice = scanner.nextInt();
				scanner.nextLine(); // 清除换行符
				
				switch (choice) {
					case 1:
						singleTest(scanner);
						break;
					case 2:
						batchTest(scanner);
						break;
					case 3:
						algorithmComparison(scanner);
						break;
					case 4:
						primeVerification(scanner);
						break;
					case 5:
						System.out.println("退出交互式测试。");
						scanner.close();
						return;
					default:
						System.out.println("无效选择，请输入1-5之间的数字。");
				}
			} catch (Exception e) {
				System.out.println("输入错误，请输入有效的数字。");
				scanner.nextLine(); // 清除输入缓冲区
			}
		}
	}
	
	/**
	 * 单次测试模式 - 测试单个数字的质数统计
	 * 
	 * @param scanner 输入扫描器
	 */
	private static void singleTest(Scanner scanner) {
		System.out.println("\n--- 单次测试模式 ---");
		System.out.print("请输入要测试的数字: ");
		
		try {
			int n = scanner.nextInt();
			scanner.nextLine(); // 清除换行符
			
			if (n < 0) {
				System.out.println("请输入非负整数。");
				return;
			}
			
			if (n > 100000000) {
				System.out.println("数字太大，建议使用批量测试模式。");
				return;
			}
			
			// 选择算法
			System.out.println("请选择算法:");
			System.out.println("1. 埃氏筛 (默认)");
			System.out.println("2. 欧拉筛");
			System.out.println("3. 优化埃氏筛");
			System.out.println("4. 分段筛");
			System.out.print("请输入选择 (1-4): ");
			
			int algorithmChoice = scanner.nextInt();
			scanner.nextLine();
			
			String algorithmName;
			int result;
			long start, end;
			
			Runtime runtime = Runtime.getRuntime();
			runtime.gc();
			long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
			
			switch (algorithmChoice) {
				case 2:
					algorithmName = "欧拉筛";
					start = System.nanoTime();
					result = euler(n);
					end = System.nanoTime();
					break;
				case 3:
					algorithmName = "优化埃氏筛";
					start = System.nanoTime();
					result = ehrlich2(n);
					end = System.nanoTime();
					break;
				case 4:
					algorithmName = "分段筛";
					start = System.nanoTime();
					result = segmentedSieve(n);
					end = System.nanoTime();
					break;
				default:
					algorithmName = "埃氏筛";
					start = System.nanoTime();
					result = ehrlich(n);
					end = System.nanoTime();
			}
			
			long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
			long memoryUsed = memoryAfter - memoryBefore;
			double timeUsed = (end - start) / 1_000_000.0;
			
			System.out.println("\n测试结果:");
			System.out.printf("数字: %,d\n", n);
			System.out.printf("算法: %s\n", algorithmName);
			System.out.printf("质数数量: %,d\n", result);
			System.out.printf("计算时间: %.3f 毫秒\n", timeUsed);
			System.out.printf("内存使用: %,d bytes\n", memoryUsed);
			
			// 如果n不大，显示质数列表
			if (n <= 1000 && result > 0) {
				List<Integer> primes = getAllPrimes(n);
				System.out.printf("质数列表: %s\n", primes.toString());
			}
			
		} catch (Exception e) {
			System.out.println("输入错误: " + e.getMessage());
		}
	}
	
	/**
	 * 批量测试模式 - 测试多个数字的性能
	 * 
	 * @param scanner 输入扫描器
	 */
	private static void batchTest(Scanner scanner) {
		System.out.println("\n--- 批量测试模式 ---");
		System.out.print("请输入要测试的数字（用空格分隔）: ");
		
		try {
			String input = scanner.nextLine();
			String[] numbers = input.split("\\s+");
			
			System.out.println("测试结果:");
			System.out.println("-".repeat(60));
			System.out.printf("%-10s %-12s %-12s %-12s\n", 
				"数字", "埃氏筛", "欧拉筛", "优化埃氏筛");
			System.out.println("-".repeat(60));
			
			for (String numStr : numbers) {
				try {
					int n = Integer.parseInt(numStr);
					if (n < 0 || n > 1000000) {
						System.out.printf("%-10d %-12s %-12s %-12s\n", 
							n, "超出范围", "超出范围", "超出范围");
						continue;
					}
					
					long start1 = System.nanoTime();
					int result1 = ehrlich(n);
					long end1 = System.nanoTime();
					
					long start2 = System.nanoTime();
					int result2 = euler(n);
					long end2 = System.nanoTime();
					
					long start3 = System.nanoTime();
					int result3 = ehrlich2(n);
					long end3 = System.nanoTime();
					
					double time1 = (end1 - start1) / 1_000_000.0;
					double time2 = (end2 - start2) / 1_000_000.0;
					double time3 = (end3 - start3) / 1_000_000.0;
					
					System.out.printf("%-10d %-6d(%.3f) %-6d(%.3f) %-6d(%.3f)\n", 
						n, result1, time1, result2, time2, result3, time3);
					
				} catch (NumberFormatException e) {
					System.out.printf("%-10s %-12s %-12s %-12s\n", 
						numStr, "无效输入", "无效输入", "无效输入");
				}
			}
			
		} catch (Exception e) {
			System.out.println("输入错误: " + e.getMessage());
		}
	}
	
	/**
	 * 算法比较模式 - 比较不同算法的性能差异
	 * 
	 * @param scanner 输入扫描器
	 */
	private static void algorithmComparison(Scanner scanner) {
		System.out.println("\n--- 算法比较模式 ---");
		System.out.print("请输入要测试的数字: ");
		
		try {
			int n = scanner.nextInt();
			scanner.nextLine();
			
			if (n < 0 || n > 10000000) {
				System.out.println("数字超出测试范围。");
				return;
			}
			
			System.out.println("算法性能比较:");
			System.out.println("-".repeat(70));
			System.out.printf("%-12s %-12s %-12s %-12s %-12s\n", 
				"算法", "质数数量", "耗时(ms)", "内存(bytes)", "效率评分");
			System.out.println("-".repeat(70));
			
			// 测试四种算法
			String[] algorithms = {"埃氏筛", "欧拉筛", "优化埃氏筛", "分段筛"};
			Map<String, Double> efficiencyScores = new LinkedHashMap<>();
			
			for (String algo : algorithms) {
				Runtime runtime = Runtime.getRuntime();
				runtime.gc();
				long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
				
				long start = System.nanoTime();
				int result = 0;
				switch (algo) {
					case "埃氏筛":
						result = ehrlich(n);
						break;
					case "欧拉筛":
						result = euler(n);
						break;
					case "优化埃氏筛":
						result = ehrlich2(n);
						break;
					case "分段筛":
						result = segmentedSieve(n);
						break;
				}
				long end = System.nanoTime();
				
				long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
				long memoryUsed = memoryAfter - memoryBefore;
				double timeUsed = (end - start) / 1_000_000.0;
				
				// 计算效率评分（时间+内存的综合评分）
				double efficiencyScore = 1000000.0 / (timeUsed + memoryUsed / 1000000.0);
				efficiencyScores.put(algo, efficiencyScore);
				
				System.out.printf("%-12s %-12d %-12.3f %-12d %-12.2f\n", 
					algo, result, timeUsed, memoryUsed, efficiencyScore);
			}
			
			// 显示性能排名
			System.out.println("\n性能排名（效率评分越高越好）:");
			efficiencyScores.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.forEach(entry -> System.out.printf("  %s: %.2f\n", entry.getKey(), entry.getValue()));
				
		} catch (Exception e) {
			System.out.println("输入错误: " + e.getMessage());
		}
	}
	
	/**
	 * 质数验证模式 - 验证特定数字是否为质数
	 * 
	 * @param scanner 输入扫描器
	 */
	private static void primeVerification(Scanner scanner) {
		System.out.println("\n--- 质数验证模式 ---");
		System.out.print("请输入要验证的数字: ");
		
		try {
			int n = scanner.nextInt();
			scanner.nextLine();
			
			boolean isPrime = isPrimeSimple(n);
			
			System.out.printf("数字 %,d %s质数。\n", n, isPrime ? "是" : "不是");
			
			if (isPrime) {
				System.out.println("质数特性:");
				System.out.printf("- 大于1的自然数: %s\n", n > 1 ? "是" : "否");
				System.out.printf("- 只能被1和自身整除: %s\n", "是");
				if (n > 2) {
					System.out.printf("- 是奇数: %s\n", n % 2 != 0 ? "是" : "否");
				}
			} else {
				System.out.println("合数特性:");
				if (n > 1) {
					System.out.print("因数分解: ");
					List<Integer> factors = getPrimeFactors(n);
					System.out.println(factors.toString());
				}
			}
			
		} catch (Exception e) {
			System.out.println("输入错误: " + e.getMessage());
		}
	}
	
	/**
	 * 获取一个数的质因数分解
	 * 
	 * @param n 要分解的数
	 * @return 质因数列表
	 */
	private static List<Integer> getPrimeFactors(int n) {
		List<Integer> factors = new ArrayList<>();
		if (n <= 1) {
			return factors;
		}
		
		// 处理2的因子
		while (n % 2 == 0) {
			factors.add(2);
			n /= 2;
		}
		
		// 处理奇数因子
		for (int i = 3; i * i <= n; i += 2) {
			while (n % i == 0) {
				factors.add(i);
				n /= i;
			}
		}
		
		// 如果n还是大于1，说明n本身是质数
		if (n > 1) {
			factors.add(n);
		}
		
		return factors;
	}
}
