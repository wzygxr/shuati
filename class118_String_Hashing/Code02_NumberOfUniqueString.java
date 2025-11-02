package class105;

import java.util.Arrays;
import java.util.HashSet;

/**
 * LeetCode 1220 等数字频率的独特子串数量问题实现
 * <p>
 * 题目链接：https://leetcode.cn/problems/unique-substrings-with-equal-digit-frequency/
 * <p>
 * 题目描述：
 * 给你一个由数字组成的字符串 s，返回 s 中独特子字符串数量，
 * 其中子字符串中的每一个数字出现的频率都相同。
 * 例如，对于字符串"1212"，满足条件的子串有：
 * - "1", "2", "1", "2"（单个字符）
 * - "12", "21", "12"（两个字符，每个数字出现一次）
 * - "1212"（四个字符，1和2各出现两次）
 * 但注意要去重，最终结果为6个独特子串。
 * <p>
 * 算法核心思想：
 * 使用枚举法结合哈希技术和频率统计，高效找出所有满足条件的独特子串
 * <p>
 * 算法详细步骤：
 * 1. 枚举所有可能的子串：
 *    - 使用双重循环，外层i遍历子串起始位置
 *    - 内层j遍历子串结束位置（j >= i）
 * 2. 动态计算子串哈希值：
 *    - 对于以i为起点的子串，随着j的增加，增量计算哈希值
 *    - 使用多项式滚动哈希算法确保相同子串生成相同哈希值
 * 3. 动态维护频率信息：
 *    - 统计每个数字(0-9)在当前子串中的出现次数
 *    - 跟踪最大频率maxCnt
 *    - 统计具有最大频率的数字种类数maxCntKinds
 *    - 统计当前子串中出现的不同数字种类数allKinds
 * 4. 条件判断：
 *    - 当maxCntKinds == allKinds时，表示所有数字出现频率相等
 *    - 这种判断方式避免了遍历所有数字进行比较
 * 5. 去重处理：
 *    - 使用HashSet存储满足条件的子串哈希值
 *    - 自动去除重复的子串
 * 6. 结果返回：
 *    - HashSet的大小即为满足条件的独特子串数量
 * <p>
 * 哈希算法原理详解：
 * - 使用多项式滚动哈希函数：hash(s) = (s[0] * base^(len-1) + s[1] * base^(len-2) + ... + s[len-1])
 * - 滚动计算形式：hash = ((...((s[0] * base) + s[1]) * base + ... ) * base + s[len-1])
 * - 基数选择：使用499（质数）作为基数，减少哈希冲突
 * - 字符处理：数字字符值+1作为系数，避免0值导致的哈希冲突
 * <p>
 * 算法核心优化：
 * 1. 频率判断优化：
 *    - 传统方法：需要遍历所有数字并比较频率，时间复杂度O(10)
 *    - 优化方法：通过maxCntKinds和allKinds比较，O(1)时间判断
 *    - 原理：如果所有数字频率相同，则maxCntKinds必须等于allKinds
 * 2. 哈希计算优化：
 *    - 使用滚动哈希技术，避免重复计算
 *    - 每个子串的哈希值计算时间为O(1)
 * 3. 空间复用：
 *    - 对于每个起始位置i，复用同一个频率计数数组
 *    - 避免为每个子串单独分配空间
 * <p>
 * 时间复杂度分析：
 * - 双重循环枚举所有子串：O(n²)，其中n是字符串长度
 * - 每个子串的内部操作：O(1)，包括哈希计算和频率更新
 * - 总体时间复杂度：O(n²)
 * <p>
 * 空间复杂度分析：
 * - HashSet存储哈希值：最坏情况O(n²)，所有子串都满足条件
 * - 频率计数数组：O(1)，固定大小为10
 * - 其他变量：O(1)
 * - 总体空间复杂度：O(n²)
 * <p>
 * 哈希冲突处理：
 * - 当前实现使用单哈希，没有取模操作，可能存在哈希冲突风险
 * - 对于LeetCode测试用例，这种实现通常足够准确
 * - 在生产环境中，可以考虑以下优化：
 *   1. 使用取模操作：将哈希值对大质数取模
 *   2. 实现双哈希：使用两个不同的哈希函数，只有当两个哈希值都相等时才认为子串相同
 *   3. 哈希值相同时进行字符串比较，确保正确性
 * <p>
 * 相似题目对比：
 * 1. LeetCode 1698: 字符串不同子串数量 - 只关注子串唯一性，不需要频率条件
 * 2. LeetCode 929: 唯一邮箱地址数量 - 不同的去重场景，但思想相似
 * 3. POJ 1200: Crazy Search - 固定长度子串去重，更简单的场景
 * <p>
 * 测试链接：https://leetcode.cn/problems/unique-substrings-with-equal-digit-frequency/
 * 
 * @author Algorithm Journey
 */
public class Code02_NumberOfUniqueString {

	/**
	 * 计算满足数字频率相等条件的独特子串数量
	 * 
	 * @param str 输入的数字字符串
	 * @return 满足条件的独特子串数量
	 * @time complexity O(n^2)，其中n是字符串长度
	 * @space complexity O(n^2)，最坏情况下所有子串都满足条件
	 */
	/**
	 * 计算满足数字频率相等条件的独特子串数量
	 * <p>
	 * 核心实现思路：
	 * 1. 枚举所有可能的子串（i为起始位置，j为结束位置）
	 * 2. 对每个子串动态计算哈希值和数字频率统计
	 * 3. 使用高效的条件判断方法验证数字频率是否相等
	 * 4. 通过HashSet自动对满足条件的子串进行去重
	 * <p>
	 * 算法关键技术点：
	 * - 滚动哈希计算：O(1)时间更新子串哈希值
	 * - 频率统计优化：动态维护频率相关指标
	 * - 条件判断技巧：利用maxCntKinds和allKinds的关系快速判断
	 * - 自动去重：利用HashSet的特性确保子串唯一性
	 * <p>
	 * 条件判断的数学证明：
	 * 1. 必要性：如果所有数字频率相同
	 *    - 设共有k个不同的数字，每个数字的频率都是m
	 *    - 那么最大频率maxCnt = m
	 *    - 具有最大频率的数字种类数maxCntKinds = k = allKinds
	 *    - 因此，maxCntKinds == allKinds 成立
	 * 
	 * 2. 充分性：如果maxCntKinds == allKinds
	 *    - 假设存在某个数字x的频率不等于maxCnt
	 *    - 则x的频率必定小于maxCnt（因为maxCnt是最大频率）
	 *    - 那么具有最大频率的数字种类数maxCntKinds < allKinds
	 *    - 这与前提条件矛盾
	 *    - 因此，所有数字的频率必须等于maxCnt
	 * 
	 * 3. 结论：maxCntKinds == allKinds 当且仅当 所有数字频率相等
	 * 
	 * 示例：
	 * 输入: "1212"
	 * 处理过程:
	 * - 检查子串"1": 频率[1,0,0,...]，满足条件，加入集合
	 * - 检查子串"12": 频率[1,1,0,...]，满足条件，加入集合
	 * - 检查子串"121": 频率[2,1,0,...]，不满足条件
	 * - 检查子串"1212": 频率[2,2,0,...]，满足条件，加入集合
	 * - ... 其他子串类似处理
	 * 最终返回满足条件的独特子串数量
	 * 
	 * @param str 输入的数字字符串，仅包含数字字符'0'-'9'
	 * @return 满足条件的独特子串数量
	 * 
	 * 时间复杂度：O(n²)，其中n是字符串长度
	 * - 双重循环遍历所有子串：O(n²)
	 * - 每个子串的哈希计算和频率统计：O(1)
	 * <p>
	 * 空间复杂度：O(n² + 1)
	 * - HashSet存储哈希值：最坏情况O(n²)
	 * - 频率计数数组：O(1)，固定大小为10
	 */
	public static int equalDigitFrequency(String str) {
		// 哈希基数，选择499（质数）以减少哈希冲突
		long base = 499;
		
		// 将字符串转换为字符数组，方便访问单个字符
		char[] s = str.toCharArray();
		
		// 获取字符串长度
		int n = s.length;
		
		// 使用HashSet存储满足条件的子串哈希值，自动去重
		HashSet<Long> set = new HashSet<>();
		
		// 用于统计0-9每个数字出现的次数
		int[] cnt = new int[10];
		
		// 枚举所有可能的子串起始位置i
		for (int i = 0; i < n; i++) {
			// 每次开始新的起始位置时，重置计数数组
			Arrays.fill(cnt, 0);
			
			// 初始化当前子串的哈希值
			long hashCode = 0;
			
			// 当前处理的数字值
			int curVal = 0;
			
			// 当前子串中数字出现的最大频率
			int maxCnt = 0;
			
			// 具有最大频率的数字种类数
			int maxCntKinds = 0;
			
			// 当前子串中出现的不同数字种类数
			int allKinds = 0;
			
			// 枚举所有可能的子串结束位置j
			for (int j = i; j < n; j++) {
				// 将字符转换为数字值（'0'~'9' -> 0~9）
				curVal = s[j] - '0';

				// 计算当前子串的哈希值
				// +1避免0值导致的哈希冲突
				hashCode = hashCode * base + curVal + 1;

				// 增加当前数字的计数
				cnt[curVal]++;

				// 如果是第一次出现该数字，增加不同数字种类数
				if (cnt[curVal] == 1) {
					allKinds++;
				}

				// 更新最大频率和具有最大频率的数字种类数
				if (cnt[curVal] > maxCnt) {
					// 当前数字频率成为新的最大值
					maxCnt = cnt[curVal];
					maxCntKinds = 1; // 重置具有最大频率的数字种类数
				} else if (cnt[curVal] == maxCnt) {
					// 当前数字频率等于最大频率，增加具有最大频率的数字种类数
					maxCntKinds++;
				}

				// 关键判断：当具有最大频率的数字种类数等于总数字种类数时，
				// 说明所有出现的数字都具有相同的频率
				// 详细证明见方法注释中的数学证明部分
				if (maxCntKinds == allKinds) {
					// 将满足条件的子串哈希值添加到集合中
					set.add(hashCode);
				}
			}
		}
		
		// 集合的大小即为满足条件的独特子串数量
		return set.size();
	}

	/**
	 * 哈希冲突概率分析
	 * 
	 * 1. 生日悖论在本问题中的应用
	 *    - 对于长度为n的字符串，子串数量为n(n+1)/2
	 *    - 假设n=100，子串数量约为5000
	 *    - 使用64位哈希值，冲突概率约为(5000²)/(2*2^64) ≈ 1.15e-10，几乎可以忽略不计
	 *    - 在LeetCode约束下(n≤100)，单哈希实现足够安全
	 * 
	 * 2. 哈希参数选择的数学依据
	 *    - 基数base=499：质数，接近500，分布相对均匀
	 *    - 为什么不使用2^k作为基数？多项式哈希中使用质数作为基数可以减少乘法冲突
	 *    - 为什么+1处理数字？避免'0'值导致的哈希值计算问题
	 * 
	 * 3. 哈希溢出处理
	 *    - 使用long类型可以容纳较大的中间哈希值
	 *    - 对于非常长的字符串，仍可能发生溢出
	 *    - 在生产环境中，建议添加大质数模数：
	 *      private static final long MOD = 1000000007L; // 10^9+7
	 *      hashCode = (hashCode * base + curVal + 1) % MOD;
	 * 
	 * 4. 双哈希实现方案
	 *    - 使用两个不同的哈希函数：
	 *      hashCode1 = (hashCode1 * base1 + curVal + 1) % MOD1;
	 *      hashCode2 = (hashCode2 * base2 + curVal + 1) % MOD2;
	 *    - 将两个哈希值组合：set.add(Objects.hash(hashCode1, hashCode2))
	 *    - 或使用Pair类存储两个哈希值作为键
	 */
	
	/**
	 * 推荐的测试用例实现
	 * 
	 * 测试代码示例：
	 * ```java
	 * public class NumberOfUniqueStringTest {
	 *     
	 *     @Test
	 *     public void testExampleCase() {
	 *         // 示例测试：LeetCode官方示例
	 *         assertEquals(6, Code02_NumberOfUniqueString.equalDigitFrequency("1212"));
	 *     }
	 *     
	 *     @Test
	 *     public void testSingleCharacter() {
	 *         // 单个字符测试
	 *         assertEquals(1, Code02_NumberOfUniqueString.equalDigitFrequency("5"));
	 *     }
	 *     
	 *     @Test
	 *     public void testAllSameDigits() {
	 *         // 所有字符相同
	 *         // 对于"222"，满足条件的子串有："2", "2", "2", "22", "22", "222"
	 *         // 去重后为3个独特子串
	 *         assertEquals(3, Code02_NumberOfUniqueString.equalDigitFrequency("222"));
	 *     }
	 *     
	 *     @Test
	 *     public void testDifferentDigits() {
	 *         // 所有字符不同
	 *         // 对于"123"，满足条件的子串有：单个字符(3个)和没有长度>=2的子串
	 *         assertEquals(3, Code02_NumberOfUniqueString.equalDigitFrequency("123"));
	 *     }
	 *     
	 *     @Test
	 *     public void testHashCollision() {
	 *         // 哈希冲突测试：构造两个不同但可能产生相同哈希值的子串
	 *         // 注意：这需要针对具体的哈希函数实现进行测试
	 *         // 例如：在实际应用中，可能需要构造特定的测试用例
	 *     }
	 * }
	 * ```
	 */
	
	/**
	 * 与其他算法的比较
	 * 
	 * 1. 多项式哈希 vs 内置哈希函数
	 *    - Java String的hashCode()：使用类似的多项式哈希，但参数固定
	 *    - 自定义实现：可以针对特定问题优化参数选择
	 *    - 对于本题，自定义实现更加高效，因为可以滚动计算
	 * 
	 * 2. 哈希集合 vs Trie树
	 *    - 哈希集合：查找和插入操作平均O(1)，但需要存储哈希值
	 *    - Trie树：可以节省空间，特别是对于大量有前缀关系的子串
	 *    - 时间效率：哈希集合在本题中通常更快
	 * 
	 * 3. 其他可能的算法实现
	 *    - 暴力解法：枚举所有子串，单独检查频率并保存到集合 - O(n³)时间复杂度
	 *    - 滑动窗口：对于固定长度的子串，可以使用滑动窗口 - 但本题子串长度可变
	 *    - 动态规划：难以应用，因为频率条件不具有明显的递推关系
	 * 
	 * 4. 语言特性利用
	 *    - 在Python中，可以直接使用set()存储子串字符串本身（但对长字符串效率低）
	 *    - 在C++中，可以使用unordered_set<long long>存储哈希值
	 *    - 在Java中，HashSet<Long>提供了良好的性能和自动装箱支持
	 */
}
