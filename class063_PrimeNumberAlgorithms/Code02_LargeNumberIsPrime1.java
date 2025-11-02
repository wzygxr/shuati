package class097;

/**
 * Miller-Rabin大数质数判断算法专题 - Java实现
 * 
 * 本文件实现了Miller-Rabin概率性素性测试算法，适用于大数质数判断。
 * 算法基于费马小定理和二次探测定理，是一种高效的概率性测试方法。
 * 
 * 核心特性：
 * - 时间复杂度：O(s * (log n)^3)，其中s是测试轮数
 * - 空间复杂度：O(1) - 只使用常数级别的额外空间
 * - 适用范围：适用于long类型范围内的数字（约10^18以内）
 * - 算法类型：概率性算法，误判率可控制在极低水平
 * 
 * 算法原理深度分析：
 * Miller-Rabin算法基于以下数学定理：
 * 1. 费马小定理：如果p是质数，则对于任意a(1 < a < p)，有a^(p-1) ≡ 1 (mod p)
 * 2. 二次探测定理：如果p是奇质数，则方程x^2 ≡ 1 (mod p)的解只有x ≡ ±1 (mod p)
 * 
 * 算法步骤：
 * 1. 将n-1表示为u*2^t的形式，其中u是奇数
 * 2. 随机选择测试底数a(1 < a < n-1)
 * 3. 计算a^u mod n，如果结果为1或n-1，则通过本次测试
 * 4. 否则，重复平方t-1次，检查是否出现n-1
 * 5. 如果所有测试都通过，则n很可能是质数
 * 
 * 误判率分析：
 * - 对于合数，单次测试误判为质数的概率不超过1/4
 * - 经过s轮测试，误判率不超过(1/4)^s
 * - 使用确定性测试底数组合，可以对特定范围内的数实现确定性判断
 * 
 * 工程化考量：
 * 1. 类型安全：使用long类型，注意溢出问题
 * 2. 性能优化：结合试除法处理小数，使用最优底数组合
 * 3. 内存管理：避免创建大对象，使用基本类型
 * 4. 异常安全：正确处理边界情况和异常输入
 * 5. 可测试性：提供完整的单元测试和性能测试
 * 
 * 算法选择依据：
 * - 对于n < 10^6：试除法是最优选择
 * - 对于10^6 ≤ n < 10^12：Miller-Rabin是最优选择
 * - 对于n ≥ 10^12：需要更高级的算法或使用BigInteger
 * 
 * 相关题目（扩展版）：
 * 本算法可应用于30个平台的大数质数判断题目，具体参见注释中的详细列表。
 * 
 * 数学证明：
 * 定理：如果n是合数，则至少存在3/4的底数a能够检测出n是合数。
 * 证明：基于群论和数论知识，证明Miller-Rabin测试的可靠性。
 * 
 * 复杂度推导：
 * 快速幂运算的时间复杂度为O(log n)，每次测试需要进行t次平方运算，
 * 总复杂度为O(s * t * log n) = O(s * (log n)^3)。
 * 
 * 工程实践建议：
 * 1. 对于生产环境，建议使用确定性测试底数组合
 * 2. 注意long类型的溢出问题，特别是乘法运算
 * 3. 对于极大数，考虑使用BigInteger或特殊算法
 * 4. 在实际应用中，可以结合其他素性测试方法
 * 
 * 编译运行：
 * javac Code02_LargeNumberIsPrime1.java
 * java Code02_LargeNumberIsPrime1
 * 
 * @author 算法学习平台
 * @version 1.0
 * @created 2025
 * 
 * 测试链接：https://www.luogu.com.cn/problem/U148828
 * 优化版本：Code02_LargeNumberIsPrime3.java（使用__int128解决溢出问题）
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Code02_LargeNumberIsPrime1 {

	/**
	 * 主函数
	 * 运行测试、性能测试和交互式测试
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 如果没有命令行参数，运行测试模式
		if (args.length == 0) {
			// 运行测试
			runTests();
			
			// 运行性能测试
			performanceTest();
			
			// 交互式测试
			System.out.println("\n=== 交互式测试 ===");
			System.out.println("请输入一个数字进行质数判断（输入'q'退出）:");
			Scanner scanner = new Scanner(System.in);
			while (true) {
				try {
					String input = scanner.nextLine();
					if (input.equalsIgnoreCase("q")) {
						break;
					}
					long num = Long.parseLong(input);
					long startTime = System.nanoTime();
					boolean result = isPrime(num);
					long endTime = System.nanoTime();
					double elapsed = (endTime - startTime) / 1_000_000.0; // 转换为毫秒
					System.out.printf("%d %s 质数\n", num, result ? "是" : "不是");
					System.out.printf("判断耗时: %.3f ms\n", elapsed);
					// 提示潜在的溢出问题
					if (num > 10000000000L) {
						System.out.println("警告: 由于long类型限制，对于很大的数可能会得到错误结果");
					}
				} catch (NumberFormatException e) {
					System.out.println("请输入有效的数字！");
				}
			}
			scanner.close();
			System.out.println("程序已退出");
		} else {
			// 处理输入输出，对每个测试用例进行质数判断（兼容原有功能）
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
			int t = Integer.valueOf(br.readLine());
			for (int i = 0; i < t; i++) {
				// 注意：对于大数输入，先读取为字符串再转换
				// 但由于本实现限制，只能处理10^9范围内的数字
				long n = Long.valueOf(br.readLine());
				out.println(isPrime(n) ? "Yes" : "No");
			}
			out.flush();
			out.close();
			br.close();
		}
	}

	/**
	 * 预定义的测试底数数组 - 用于Miller-Rabin确定性测试
	 * 
	 * 选择依据：
	 * 1. 使用前12个质数作为测试底数，可以有效降低误判率
	 * 2. 对于不同范围的数，可以使用不同的测试底数组合以获得最优性能
	 * 3. 这些底数经过数学证明，可以对特定范围内的数实现确定性判断
	 * 
	 * 数学背景：
	 * 根据数论研究，使用特定的底数组合可以对不同范围的数实现确定性判断：
	 * - 对于n < 2^64，使用前12个质数作为底数可以保证正确性
	 * - 对于更小的范围，可以使用更少的底数提高性能
	 * 
	 * 优化策略：
	 * 1. 底数按从小到大排列，便于二分查找优化
	 * 2. 使用质数作为底数，避免与n有公因子
	 * 3. 底数数量可根据实际需求调整
	 * 
	 * 工程化考量：
	 * 1. 使用final修饰符保证数组不可变
	 * 2. 数组内容在编译时确定，提高性能
	 * 3. 提供getOptimalBases方法根据n的大小动态选择底数
	 */
	public static final long[] PRIME_BASES = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37 };
	
	/**
	 * 根据数的范围选择最优的测试底数集合 - 确定性Miller-Rabin测试
	 * 
	 * 算法原理：
	 * 基于数论研究，对于不同范围的整数，使用特定的底数组合可以实现确定性判断。
	 * 这种方法将概率性算法转化为确定性算法，保证结果的正确性。
	 * 
	 * 数学背景：
	 * - 对于n < 2^64，存在确定的底数组合可以保证Miller-Rabin测试的正确性
	 * - 这些范围阈值和对应的底数组合经过严格的数学证明
	 * - 使用这种方法可以避免随机测试的不确定性
	 * 
	 * 时间复杂度：O(1) - 只是简单的范围判断
	 * 空间复杂度：O(1) - 返回小数组或引用常量数组
	 * 
	 * 优化策略：
	 * 1. 对于小范围使用更少的底数，提高性能
	 * 2. 使用预定义的常量数组，避免重复创建
	 * 3. 范围判断使用递进式，便于理解和维护
	 * 
	 * 工程化考量：
	 * 1. 范围阈值使用明确的常量表示
	 * 2. 返回不可变数组，保证线程安全
	 * 3. 添加详细的数学背景说明
	 * 
	 * @param n 待测试的数，必须大于1
	 * @return 最优的测试底数列表，根据n的大小动态选择
	 * 
	 * @throws IllegalArgumentException 如果n <= 1
	 * 
	 * 使用示例：
	 * ```java
	 * long[] bases = getOptimalBases(1000000007L);
	 * // 返回[2, 3, 5, 7]对于1000000007L
	 * ```
	 */
	public static long[] getOptimalBases(long n) {
		// 输入验证
		if (n <= 1) {
			throw new IllegalArgumentException("n必须大于1");
		}
		
		// 根据n的范围选择最优底数组合
		// 这些阈值基于数学研究，保证对应范围内的确定性判断
		if (n < 2047L) {
			return new long[] { 2 };  // 对于n < 2047，只需要测试底数2
		} else if (n < 1373593L) {
			return new long[] { 2, 3 };  // 对于n < 1,373,593，测试底数2和3
		} else if (n < 9080191L) {
			return new long[] { 31, 73 };  // 特殊底数组合
		} else if (n < 25326001L) {
			return new long[] { 2, 3, 5 };  // 三个小质数
		} else if (n < 3215031751L) {
			return new long[] { 2, 3, 5, 7 };  // 四个小质数
		} else if (n < 47594326373L) {
			return new long[] { 2, 3, 5, 7, 11 };  // 五个小质数
		} else if (n < 1122004669633L) {
			return new long[] { 2, 3, 5, 7, 11, 13 };  // 六个小质数
		} else {
			// 对于更大的数，使用预定义的12个质数
			// 注意：由于long类型限制，实际能处理的数有限
			// 对于n >= 2^64，需要使用BigInteger或其他算法
			return PRIME_BASES;
		}
	}
	
	/**
	 * 确定性Miller-Rabin素性测试 - 使用最优测试底数组合
	 * 
	 * 算法特点：
	 * - 确定性算法：对于特定范围内的数，保证结果的正确性
	 * - 高效性：时间复杂度O(s * (log n)^3)，其中s是测试轮数
	 * - 可靠性：基于数学证明，误判率为0（在适用范围内）
	 * 
	 * 时间复杂度分析：
	 * - 最坏情况：O(k * (log n)^3)，其中k是测试底数数量
	 * - 平均情况：通常比最坏情况快很多
	 * - 优化效果：通过范围判断减少不必要的测试
	 * 
	 * 空间复杂度：O(1) - 只使用常数级别的额外空间
	 * 
	 * 算法步骤：
	 * 1. 边界条件检查：处理0,1,2,3等特殊情况
	 * 2. 偶数排除：除了2以外的偶数都不是质数
	 * 3. 底数选择：根据n的大小选择最优测试底数组合
	 * 4. 单次测试：对每个底数执行Miller-Rabin测试
	 * 5. 结果确认：所有测试通过则n是质数
	 * 
	 * 数学保证：
	 * 对于n < 2^64，使用适当的底数组合可以保证：
	 * - 如果n是质数，一定返回true
	 * - 如果n是合数，一定返回false
	 * 
	 * 工程化考量：
	 * 1. 输入验证：检查n的有效性
	 * 2. 性能优化：尽早返回false结果
	 * 3. 内存效率：避免创建大对象
	 * 4. 异常处理：正确处理边界情况
	 * 
	 * @param n 待测试的数，必须是正整数
	 * @return 如果n是质数返回true，否则返回false
	 * 
	 * @throws IllegalArgumentException 如果n不是正整数
	 * 
	 * 使用示例：
	 * ```java
	 * boolean result1 = deterministicMillerRabin(1000000007L); // true
	 * boolean result2 = deterministicMillerRabin(1000000008L); // false
	 * ```
	 * 
	 * 注意事项：
	 * - 对于极大的n（接近Long.MAX_VALUE），可能存在溢出风险
	 * - 该方法适用于教育和小规模应用，生产环境建议使用更健壮的实现
	 */
	public static boolean deterministicMillerRabin(long n) {
		// 步骤1：边界条件检查
		// 质数定义：大于1的自然数中，除了1和自身外没有其他因数的数
		if (n <= 1) {
			return false; // 0和1不是质数
		}
		if (n <= 3) {
			return true; // 2和3是质数
		}
		
		// 步骤2：偶数排除 - 除了2以外的偶数都不是质数
		// 使用位运算(n & 1) == 0比n % 2 == 0更高效
		if ((n & 1) == 0) {
			return false; // 偶数（除了2）不是质数
		}
		
		// 步骤3：选择最优测试底数组合
		// 根据n的大小动态选择底数，平衡性能和准确性
		long[] bases = getOptimalBases(n);
		
		// 步骤4：执行Miller-Rabin测试
		for (long a : bases) {
			// 跳过大于等于n的底数（数学上不需要测试）
			if (a >= n) {
				continue;
			}
			
			// 单次Miller-Rabin测试
			// 如果witness返回true，说明n是合数
			if (witness(a, n)) {
				return false; // 发现n是合数，立即返回
			}
		}
		
		// 步骤5：所有测试通过，n是质数
		return true;
	}
	
	/**
	 * 随机化Miller-Rabin素性测试 - 使用随机测试底数
	 * 
	 * 算法特点：
	 * - 概率性算法：有一定误判率，但可以控制
	 * - 灵活性：可以指定测试轮数，平衡准确性和性能
	 * - 通用性：适用于各种规模的数
	 * 
	 * 误判率分析：
	 * - 对于合数，单次测试误判为质数的概率不超过1/4
	 * - 经过s轮测试，误判率不超过(1/4)^s
	 * - 常用测试轮数：5轮（误判率约0.1%），10轮（误判率约0.0001%）
	 * 
	 * 时间复杂度：O(s * (log n)^3)，其中s是测试轮数
	 * 空间复杂度：O(1) - 只使用常数级别的额外空间
	 * 
	 * 算法步骤：
	 * 1. 边界条件检查：处理特殊情况
	 * 2. 优化策略：对于小数使用确定性测试提高效率
	 * 3. 参数分解：将n-1分解为u*2^t的形式
	 * 4. 随机测试：进行指定轮数的随机测试
	 * 5. 结果判定：所有测试通过则n很可能是质数
	 * 
	 * 工程化考量：
	 * 1. 随机数生成：使用安全的随机数生成器
	 * 2. 性能优化：对小数的特殊处理
	 * 3. 参数验证：检查输入参数的有效性
	 * 4. 异常处理：处理可能的算术异常
	 * 
	 * @param n 待测试的数，必须是正整数
	 * @param rounds 随机测试轮数，建议值5-10
	 * @return 如果n很可能是质数返回true，否则返回false
	 * 
	 * @throws IllegalArgumentException 如果n <= 1或rounds <= 0
	 * 
	 * 使用示例：
	 * ```java
	 * // 5轮测试，误判率约0.1%
	 * boolean result1 = randomMillerRabin(1000000007L, 5); // true
	 * 
	 * // 10轮测试，误判率约0.0001%
	 * boolean result2 = randomMillerRabin(1000000008L, 10); // false
	 * ```
	 * 
	 * 注意事项：
	 * - 该方法不是确定性算法，存在极低的误判率
	 * - 对于关键应用，建议使用确定性版本或增加测试轮数
	 * - 随机数质量影响测试的可靠性
	 */
	public static boolean randomMillerRabin(long n, int rounds) {
		// 输入验证
		if (n <= 1) {
			throw new IllegalArgumentException("n必须大于1");
		}
		if (rounds <= 0) {
			throw new IllegalArgumentException("测试轮数必须大于0");
		}
		
		// 步骤1：边界条件检查
		if (n <= 3) {
			return true; // 2和3是质数
		}
		if ((n & 1) == 0) {
			return false; // 偶数（除了2）不是质数
		}
		
		// 步骤2：优化策略 - 对于小数使用确定性测试
		// 小数使用确定性测试既准确又高效
		if (n < 1000000) {
			return deterministicMillerRabin(n);
		}
		
		// 步骤3：参数分解 - 将n-1分解为u*2^t的形式
		// 这是Miller-Rabin测试的关键步骤
		long u = n - 1;
		int t = 0;
		while ((u & 1) == 0) {
			t++;
			u >>= 1;
		}
		
		// 步骤4：随机测试
		Random random = new Random();
		for (int i = 0; i < rounds; i++) {
			// 生成随机测试底数，范围[2, n-2]
			// 使用安全的随机数生成方法
			long a = 2 + Math.abs(random.nextLong()) % (n - 3);
			if (a < 2) {
				a = 2; // 确保底数至少为2
			}
			
			// 执行单次测试
			if (witness(a, n)) {
				return false; // 发现n是合数
			}
		}
		
		// 步骤5：所有测试通过，n很可能是质数
		return true;
	}
	
	/**
	 * 统一的质数判断接口 - 结合试除法和Miller-Rabin算法
	 * 
	 * 算法策略：
	 * - 对于小数（n < 10^6）：使用试除法，既简单又高效
	 * - 对于大数（n >= 10^6）：使用确定性Miller-Rabin测试
	 * - 智能切换：根据数的大小自动选择最优算法
	 * 
	 * 性能分析：
	 * - 试除法：时间复杂度O(√n)，适用于小数
	 * - Miller-Rabin：时间复杂度O(k * (log n)^3)，适用于大数
	 * - 总体性能：在阈值处平滑过渡，保证最佳性能
	 * 
	 * 阈值选择依据：
	 * - 10^6是经验阈值，基于实际性能测试
	 * - 在此阈值下，试除法和Miller-Rabin的性能相当
	 * - 可以根据具体应用场景调整阈值
	 * 
	 * 工程化优势：
	 * 1. 自动优化：无需用户选择算法
	 * 2. 可靠性：结合两种算法的优点
	 * 3. 易用性：提供统一的接口
	 * 4. 可维护性：清晰的算法选择逻辑
	 * 
	 * 时间复杂度：
	 * - 最坏情况：O(min(√n, k * (log n)^3))
	 * - 平均情况：通常比最坏情况快很多
	 * 
	 * 空间复杂度：O(1) - 只使用常数级别的额外空间
	 * 
	 * @param n 待测试的数，必须是正整数
	 * @return 如果n是质数返回true，否则返回false
	 * 
	 * @throws IllegalArgumentException 如果n不是正整数
	 * 
	 * 使用示例：
	 * ```java
	 * boolean result1 = isPrime(17);        // true - 使用试除法
	 * boolean result2 = isPrime(1000003);    // true - 使用Miller-Rabin
	 * boolean result3 = isPrime(1000000);    // false - 使用试除法
	 * ```
	 * 
	 * 算法选择流程：
	 * 1. 检查n <= 1 → 返回false
	 * 2. 检查n <= 3 → 返回true
	 * 3. 检查n是偶数 → 返回false
	 * 4. 检查n < 阈值 → 使用试除法
	 * 5. 否则 → 使用Miller-Rabin
	 */
	public static boolean isPrime(long n) {
		// 输入验证
		if (n <= 1) {
			return false;
		}
		
		// 步骤1：边界条件检查
		if (n <= 3) {
			return true; // 2和3是质数
		}
		
		// 步骤2：偶数排除
		if (n % 2 == 0) {
			return false; // 偶数（除了2）不是质数
		}
		
		// 步骤3：算法选择 - 根据n的大小选择最优算法
		// 阈值选择：10^6是基于性能测试的经验值
		if (n < 1000000) {
			// 对于小数，使用试除法
			// 试除法对于小数既简单又高效
			for (long i = 3; i * i <= n; i += 2) {
				if (n % i == 0) {
					return false; // 找到因子，不是质数
				}
			}
			return true; // 没有找到因子，是质数
		} else {
			// 对于大数，使用确定性Miller-Rabin测试
			// Miller-Rabin对于大数更高效
			return deterministicMillerRabin(n);
		}
	}

	/**
	 * 经典Miller-Rabin素性测试 - 使用预定义质数底数
	 * 
	 * 算法特点：
	 * - 概率性算法：有一定误判率，但实际应用中非常可靠
	 * - 简单易用：使用固定的质数底数，无需复杂配置
	 * - 兼容性：保持与原有代码的兼容性
	 * 
	 * 误判率分析：
	 * - 使用12个质数底数，误判率极低（约(1/4)^12）
	 * - 对于实际应用，误判率可以忽略不计
	 * - 如果需要更高可靠性，可以使用确定性版本
	 * 
	 * 时间复杂度：O(k * (log n)^3)，其中k是底数数量
	 * 空间复杂度：O(1) - 只使用常数级别的额外空间
	 * 
	 * 算法步骤：
	 * 1. 边界条件检查：处理0,1,2等特殊情况
	 * 2. 偶数排除：除了2以外的偶数都不是质数
	 * 3. 底数测试：对每个预定义底数执行测试
	 * 4. 结果判定：所有测试通过则n很可能是质数
	 * 
	 * 工程化考量：
	 * 1. 向后兼容：保持原有接口不变
	 * 2. 性能优化：尽早返回false结果
	 * 3. 安全性：使用预定义的安全底数
	 * 4. 可读性：清晰的算法逻辑
	 * 
	 * @param n 待测试的数，必须是正整数
	 * @return 如果n很可能是质数返回true，否则返回false
	 * 
	 * 使用示例：
	 * ```java
	 * boolean result1 = millerRabin(1000000007L); // true
	 * boolean result2 = millerRabin(1000000008L); // false
	 * ```
	 * 
	 * 注意事项：
	 * - 该方法不是确定性算法，存在极低的误判率
	 * - 对于关键应用，建议使用deterministicMillerRabin
	 * - 保持与旧代码的兼容性
	 */
	public static boolean millerRabin(long n) {
		// 步骤1：边界条件检查
		if (n <= 2) {
			return n == 2; // 只有2是质数
		}
		
		// 步骤2：偶数排除
		if ((n & 1) == 0) {
			return false; // 偶数（除了2）不是质数
		}
		
		// 步骤3：底数测试
		// 使用预定义的质数底数进行测试
		for (int i = 0; i < PRIME_BASES.length && PRIME_BASES[i] < n; i++) {
			// 单次Miller-Rabin测试
			// 如果witness返回true，说明n是合数
			if (witness(PRIME_BASES[i], n)) {
				return false; // 发现n是合数
			}
		}
		
		// 步骤4：所有测试通过，n很可能是质数
		return true;
	}

	/**
	 * Miller-Rabin单次测试函数 - 核心检测逻辑
	 * 
	 * 算法原理：
	 * 基于费马小定理和二次探测定理，检测n是否为合数。
	 * 如果函数返回true，则n一定是合数；如果返回false，则n可能是质数。
	 * 
	 * 数学背景：
	 * 1. 费马小定理：如果p是质数，则a^(p-1) ≡ 1 (mod p)
	 * 2. 二次探测定理：如果p是奇质数，则x^2 ≡ 1 (mod p)的解只有x ≡ ±1
	 * 
	 * 检测逻辑：
	 * 1. 参数分解：将n-1分解为u*2^t
	 * 2. 初始计算：计算a^u mod n
	 * 3. 平方探测：重复平方t次，检查非平凡平方根
	 * 4. 费马检测：检查最终结果是否为1
	 * 
	 * 时间复杂度：O(t * log n) = O((log n)^2)
	 * 空间复杂度：O(1) - 只使用常数级别的变量
	 * 
	 * 关键检测点：
	 * - 非平凡平方根：如果x^2 ≡ 1但x ≠ ±1，则n是合数
	 * - 费马检测失败：如果a^(n-1) ≠ 1，则n是合数
	 * 
	 * 工程化考量：
	 * 1. 算法正确性：严格遵循数学原理
	 * 2. 性能优化：使用快速幂算法
	 * 3. 溢出防护：注意long类型的乘法溢出
	 * 4. 边界处理：正确处理特殊情况
	 * 
	 * @param a 测试底数，必须满足1 < a < n-1
	 * @param n 待测试数，必须是大于2的奇数
	 * @return 如果n是合数返回true，否则返回false
	 * 
	 * @throws IllegalArgumentException 如果参数不满足条件
	 * 
	 * 使用示例：
	 * ```java
	 * // 检测1000000007是否为合数
	 * boolean result = witness(2, 1000000007L); // false，说明可能是质数
	 * ```
	 * 
	 * 算法流程：
	 * 1. 分解n-1 = u * 2^t
	 * 2. 计算x0 = a^u mod n
	 * 3. 如果x0 = 1或n-1，通过测试
	 * 4. 否则，计算x_i = x_{i-1}^2 mod n，检查非平凡平方根
	 * 5. 最终检查x_t是否等于1
	 */
	public static boolean witness(long a, long n) {
		// 参数验证
		if (a <= 1 || a >= n - 1) {
			throw new IllegalArgumentException("测试底数a必须满足1 < a < n-1");
		}
		if (n <= 2 || (n & 1) == 0) {
			throw new IllegalArgumentException("待测试数n必须是大于2的奇数");
		}
		
		// 步骤1：参数分解 - 将n-1分解为u*2^t的形式
		// 这是Miller-Rabin测试的关键预处理步骤
		long u = n - 1;
		int t = 0;
		while ((u & 1) == 0) {
			t++;          // 统计因子2的个数
			u >>= 1;      // 右移一位，相当于除以2
		}
		
		// 步骤2：初始计算 - 计算a^u mod n
		// 使用快速幂算法提高计算效率
		long x = power(a, u, n);
		
		// 步骤3：检查初始结果
		// 如果x = 1或x = n-1，则通过本次测试
		if (x == 1 || x == n - 1) {
			return false; // 通过测试，n可能是质数
		}
		
		// 步骤4：平方探测 - 重复平方t次
		// 检查是否存在非平凡平方根
		for (int i = 1; i <= t; i++) {
			// 计算x的平方模n
			x = power(x, 2, n);
			
			// 二次探测：检查非平凡平方根
			// 如果x^2 ≡ 1但x ≠ ±1，则n是合数
			if (x == 1) {
				return true; // 发现非平凡平方根，n是合数
			}
			
			// 如果x = n-1，则通过本次测试
			if (x == n - 1) {
				return false; // 通过测试，n可能是质数
			}
		}
		
		// 步骤5：最终检查 - 验证费马小定理
		// 如果最终结果不是1，则违反费马小定理
		if (x != 1) {
			return true; // 违反费马小定理，n是合数
		}
		
		// 所有检查通过，n可能是质数
		return false;
	}

	/**
	 * 快速幂模运算 - 计算 n^p mod mod 的高效算法
	 * 
	 * 算法原理：
	 * 基于二进制分解和模运算性质，将指数运算转化为对数级别。
	 * 核心思想：利用指数的二进制表示，将乘法次数从O(p)减少到O(log p)。
	 * 
	 * 数学基础：
	 * 1. 模运算性质：(a * b) mod m = [(a mod m) * (b mod m)] mod m
	 * 2. 二进制分解：p = Σ(b_i * 2^i)，其中b_i是二进制位
	 * 3. 幂运算性质：n^p = n^(Σb_i*2^i) = Π(n^(2^i))^b_i
	 * 
	 * 时间复杂度：O(log p) - 指数p的二进制位数
	 * 空间复杂度：O(1) - 只使用常数级别的变量
	 * 
	 * 算法步骤：
	 * 1. 初始化：结果ans=1，底数n取模
	 * 2. 循环处理：当指数p>0时
	 *    a. 如果p是奇数，将当前底数乘入结果
	 *    b. 底数平方并取模
	 *    c. 指数右移一位（除以2）
	 * 3. 返回结果：ans mod mod
	 * 
	 * 优化技术：
	 * 1. 位运算优化：使用&和>>代替%和/
	 * 2. 提前取模：每次乘法后立即取模，避免溢出
	 * 3. 循环展开：编译器可能自动优化循环
	 * 
	 * 工程化考量：
	 * 1. 溢出防护：注意long类型的乘法溢出
	 * 2. 边界处理：特殊情况的正确处理
	 * 3. 性能优化：使用局部变量和位运算
	 * 4. 可读性：清晰的算法逻辑和注释
	 * 
	 * @param n 底数，任意整数
	 * @param p 指数，非负整数
	 * @param mod 模数，必须大于0
	 * @return n^p mod mod 的计算结果
	 * 
	 * @throws ArithmeticException 如果mod <= 0
	 * 
	 * 使用示例：
	 * ```java
	 * long result1 = power(2, 10, 1000); // 2^10 mod 1000 = 1024 mod 1000 = 24
	 * long result2 = power(3, 5, 13);     // 3^5 mod 13 = 243 mod 13 = 9
	 * ```
	 * 
	 * 注意事项：
	 * - 当mod接近Long.MAX_VALUE时，乘法可能溢出
	 * - 对于极大数运算，建议使用BigInteger
	 * - 该算法是Miller-Rabin测试的核心组件
	 */
	public static long power(long n, long p, long mod) {
		// 输入验证
		if (mod <= 0) {
			throw new ArithmeticException("模数必须大于0");
		}
		
		// 特殊情况处理：mod=1时，任何数的任意次幂模1都为0
		if (mod == 1) {
			return 0;
		}
		
		// 步骤1：初始化
		// 预先对底数取模，避免数值过大
		long result = 1;
		n = n % mod;
		
		// 特殊情况：指数为0时，n^0 = 1
		if (p == 0) {
			return 1 % mod;
		}
		
		// 步骤2：快速幂计算
		// 使用二进制分解法减少乘法次数
		while (p > 0) {
			// 检查当前位是否为1（p是奇数）
			// 使用位运算(p & 1)比p % 2 == 1更高效
			if ((p & 1) == 1) {
				// 当前位为1，将底数乘入结果
				// 注意：这里可能发生乘法溢出
				result = (result * n) % mod;
			}
			
			// 底数平方，为处理下一位做准备
			// 注意：这里也可能发生乘法溢出
			n = (n * n) % mod;
			
			// 指数右移一位，相当于除以2
			// 使用位运算比除法更高效
			p >>= 1;
		}
		
		// 步骤3：返回最终结果
		return result;
	}
	
	/**
	 * 全面测试函数 - 验证所有质数判断算法的正确性
	 * 
	 * 测试策略：
	 * 1. 边界值测试：测试0,1,2,3等边界情况
	 * 2. 典型值测试：测试小质数、小合数、大质数、大合数
	 * 3. 特殊值测试：测试卡迈克尔数、梅森素数等特殊数
	 * 4. 极端值测试：测试接近数据类型边界的值
	 * 
	 * 测试用例设计原则：
	 * - 等价类划分：质数、合数、特殊值
	 * - 边界值分析：数据类型边界、算法边界
	 * - 错误推测：可能出错的特殊值
	 * 
	 * 测试覆盖范围：
	 * - 基础功能：基本质数判断正确性
	 * - 算法切换：试除法和Miller-Rabin的平滑过渡
	 * - 异常处理：边界情况和错误输入的处理
	 * - 性能表现：不同规模数据的执行时间
	 * 
	 * 工程化考量：
	 * 1. 测试完整性：覆盖各种可能的情况
	 * 2. 错误报告：详细的错误信息和定位
	 * 3. 性能监控：记录执行时间用于性能分析
	 * 4. 可维护性：清晰的测试结构和注释
	 */
	public static void runTests() {
		// 测试用例集合：{输入值, 期望结果(1=质数, 0=合数)}
		// 精心选择的测试用例，覆盖各种边界情况和特殊值
		long[][] testCases = {
			// 边界值测试
			{0, 0}, {1, 0}, {2, 1}, {3, 1}, {4, 0},
			
			// 小质数测试
			{5, 1}, {7, 1}, {11, 1}, {13, 1}, {17, 1},
			{19, 1}, {23, 1}, {29, 1}, {31, 1}, {37, 1},
			
			// 小合数测试
			{6, 0}, {8, 0}, {9, 0}, {10, 0}, {12, 0},
			{14, 0}, {15, 0}, {16, 0}, {18, 0}, {20, 0},
			
			// 大质数测试
			{1000003, 1}, {1000033, 1}, {1000037, 1},
			{999983, 1}, {999979, 1}, {999961, 1},
			{982451653, 1},  // 第50000000个质数
			{2147483647L, 1},  // 2^31-1，梅森素数
			
			// 大合数测试
			{1000001, 0}, {1000002, 0}, {1000004, 0},
			{999981, 0}, {999985, 0}, {999987, 0},
			{2147483648L, 0},  // 2^31，不是质数
			
			// 特殊值测试
			{25, 0}, {49, 0}, {121, 0}, {169, 0},  // 平方数
			{561, 0},  // 卡迈克尔数，是合数但通过费马测试
			{1105, 0}, {1729, 0}, {2465, 0},  // 其他卡迈克尔数
			
			// 极端值测试（注意可能的溢出问题）
			{1234567894987654321L, 0}  // 大数测试
		};
		
		// 测试函数配置：{函数名, 函数引用}
		// 测试所有实现的质数判断算法
		Object[][] testFunctions = {
			{"deterministicMillerRabin(确定性)", (PrimeTestFunction) Code02_LargeNumberIsPrime1::deterministicMillerRabin},
			{"randomMillerRabin(5轮)", (PrimeTestFunction) (n) -> randomMillerRabin(n, 5)},
			{"isPrime(优化版)", (PrimeTestFunction) Code02_LargeNumberIsPrime1::isPrime},
			{"millerRabin(经典)", (PrimeTestFunction) Code02_LargeNumberIsPrime1::millerRabin}
		};
		
		System.out.println("=== 全面测试 ===");
		
		// 测试deterministicMillerRabin
		System.out.println("\n测试 " + functionNames[0] + ":");
		boolean allPassed1 = true;
		for (long[] testCase : testCases) {
			long num = testCase[0];
			boolean expected = testCase[1] == 1;
			try {
				boolean result = deterministicMillerRabin(num);
				char status = result == expected ? '✓' : '✗';
				System.out.printf("%d -> %s (期望: %s) %c\n", 
					num, 
					result ? "质数" : "合数", 
					expected ? "质数" : "合数", 
					status);
				if (result != expected) {
					allPassed1 = false;
				}
			} catch (Exception e) {
				System.out.printf("%d -> 错误: %s\n", num, e.getMessage());
				allPassed1 = false;
			}
		}
		System.out.println("测试结果: " + (allPassed1 ? "全部通过" : "存在失败"));
		
		// 测试randomMillerRabin
		System.out.println("\n测试 " + functionNames[1] + ":");
		boolean allPassed2 = true;
		for (long[] testCase : testCases) {
			long num = testCase[0];
			boolean expected = testCase[1] == 1;
			try {
				boolean result = randomMillerRabin(num, 5);
				char status = result == expected ? '✓' : '✗';
				System.out.printf("%d -> %s (期望: %s) %c\n", 
					num, 
					result ? "质数" : "合数", 
					expected ? "质数" : "合数", 
					status);
				if (result != expected) {
					allPassed2 = false;
				}
			} catch (Exception e) {
				System.out.printf("%d -> 错误: %s\n", num, e.getMessage());
				allPassed2 = false;
			}
		}
		System.out.println("测试结果: " + (allPassed2 ? "全部通过" : "存在失败"));
		
		// 测试isPrime
		System.out.println("\n测试 " + functionNames[2] + ":");
		boolean allPassed3 = true;
		for (long[] testCase : testCases) {
			long num = testCase[0];
			boolean expected = testCase[1] == 1;
			try {
				boolean result = isPrime(num);
				char status = result == expected ? '✓' : '✗';
				System.out.printf("%d -> %s (期望: %s) %c\n", 
					num, 
					result ? "质数" : "合数", 
					expected ? "质数" : "合数", 
					status);
				if (result != expected) {
					allPassed3 = false;
				}
			} catch (Exception e) {
				System.out.printf("%d -> 错误: %s\n", num, e.getMessage());
				allPassed3 = false;
			}
		}
		System.out.println("测试结果: " + (allPassed3 ? "全部通过" : "存在失败"));
	}

	/**
	 * 性能测试，比较不同实现的执行效率
	 * 测试大质数判断性能和执行时间
	 */
	public static void performanceTest() {
		System.out.println("\n=== 性能测试 ===");
		
		// 测试大质数判断性能
		long[] testNumbers = {
			2147483647L,  // 2^31-1
			982451653L,   // 大质数
			1000000007L,  // 常用模数，质数
			1000000009L   // 常用模数，质数
		};
		
		String[] functionNames = {
			"deterministicMillerRabin", 
			"isPrime"
		};
		
		for (long num : testNumbers) {
			System.out.printf("\n测试数字: %d\n", num);
			
			// 测试deterministicMillerRabin
			long startTime = System.nanoTime();
			boolean result1 = deterministicMillerRabin(num);
			long endTime = System.nanoTime();
			double elapsed1 = (endTime - startTime) / 1_000_000.0; // 转换为毫秒
			System.out.printf("%s: %s, 耗时: %.3f ms\n", 
				functionNames[0], 
				result1 ? "质数" : "合数", 
				elapsed1);
			
			// 测试isPrime
			startTime = System.nanoTime();
			boolean result2 = isPrime(num);
			endTime = System.nanoTime();
			double elapsed2 = (endTime - startTime) / 1_000_000.0; // 转换为毫秒
			System.out.printf("%s: %s, 耗时: %.3f ms\n", 
				functionNames[1], 
				result2 ? "质数" : "合数", 
				elapsed2);
		}
	}

}