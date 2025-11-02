package class105;

/**
 * LeetCode 28 字符串子串查找实现 - 基于多项式滚动哈希算法
 * <p>
 * 题目链接：https://leetcode.cn/problems/find-the-index-of-the-first-occurrence-in-a-string/
 * <p>
 * 题目描述：
 * 给你两个字符串 haystack 和 needle，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）。
 * 如果 needle 不是 haystack 的一部分，则返回 -1。
 * <p>
 * 示例：
 * 输入：haystack = "sadbutsad", needle = "sad"
 * 输出：0
 * 解释："sad" 在下标 0 和 6 处匹配，但第一个匹配项在 0 位置
 * <p>
 * 输入：haystack = "leetcode", needle = "leeto"
 * 输出：-1
 * 解释："leeto" 不是 "leetcode" 的一部分
 * <p>
 * 算法核心思想：
 * 使用多项式滚动哈希（Polynomial Rolling Hash）算法将字符串转换为数值表示，
 * 通过哈希值比较实现高效的子串匹配
 * <p>
 * 算法详细步骤：
 * 1. 预处理阶段：
 *    - 将主串转换为字符数组，提高访问效率
 *    - 处理边界条件（空串、长度不匹配等情况）
 *    - 构建前缀哈希数组和幂次数组
 * 2. 目标哈希计算：
 *    - 计算子串needle的哈希值
 *    - 使用与主串相同的哈希函数，确保相同字符串产生相同哈希值
 * 3. 滑动窗口匹配：
 *    - 在主串中滑动固定长度为m的窗口
 *    - 对于每个窗口，O(1)时间计算其哈希值
 *    - 比较窗口哈希值与子串哈希值
 * 4. 结果返回：
 *    - 当找到匹配时，返回窗口起始位置
 *    - 遍历完所有窗口后仍无匹配，返回-1
 * <p>
 * 多项式滚动哈希原理详解：
 * - 基本定义：对于字符串s = s[0]s[1]...s[n-1]，其哈希值定义为：
 *   hash(s) = s[0]*base^(n-1) + s[1]*base^(n-2) + ... + s[n-1]*base^0
 * - 核心思想：将字符串视为base进制数，每个字符的值作为该进制下的数字
 * - 字符映射：将'a'-'z'映射为1-26，避免0值导致的哈希冲突
 * - 滚动计算：利用递推关系高效计算前缀哈希
 * <p>
 * 前缀哈希和子串哈希的数学关系：
 * - 前缀哈希：hash[i] = s[0]*base^i + s[1]*base^(i-1) + ... + s[i]*base^0
 * - 递推公式：hash[i] = hash[i-1] * base + s[i]的映射值
 * - 子串哈希推导：
 *   hash(l,r) = hash[r] - hash[l-1] * base^(r-l+1)
 *   其中pow[r-l+1]存储了base^(r-l+1)的预计算值
 * <p>
 * 时间复杂度分析：
 * - 预处理阶段：O(n)，构建前缀哈希数组和幂次数组
 * - 子串哈希计算：O(m)，计算needle字符串的哈希值
 * - 滑动窗口匹配：O(n-m+1) = O(n)，对每个窗口进行O(1)时间的哈希比较
 * - 总体时间复杂度：O(n + m)
 * <p>
 * 空间复杂度分析：
 * - 前缀哈希数组：O(n)
 * - 幂次数组：O(n)
 * - 总体空间复杂度：O(n)
 * <p>
 * 哈希冲突问题：
 * - 当前实现未使用取模操作，理论上可能存在哈希冲突
 * - 对于LeetCode测试用例，这种实现通常足够准确
 * - 在生产环境中，建议进行以下改进：
 *   1. 使用取模操作：将哈希值对大质数取模
 *   2. 实现双重哈希：使用两个不同的哈希函数和模数
 *   3. 哈希值相等时进行字符串直接比较，确保正确性
 * <p>
 * 与KMP算法比较：
 * - 时间复杂度：两者都是O(n+m)
 * - 空间复杂度：哈希方法O(n)，KMP方法O(m)
 * - 实现难度：哈希方法更简单直观，KMP算法实现更复杂
 * - 适用场景：哈希方法适用范围更广，可用于各种子串查询问题
 * <p>
 * 测试链接：https://leetcode.cn/problems/find-the-index-of-the-first-occurrence-in-a-string/
 * 
 * @author Algorithm Journey
 */
public class Code03_SubstringHash {

	/**
	 * 查找子串在主串中第一次出现的位置
	 * 实现了LeetCode 28题的核心功能
	 * <p>
	 * 实现步骤详解：
	 * 1. 输入处理与边界条件检查：
	 *    - 将输入字符串转换为字符数组以提高访问效率
	 *    - 处理空串情况：根据题目定义，空字符串是任何字符串的子串，返回0
	 *    - 快速失败：如果主串长度小于子串长度，不可能匹配，直接返回-1
	 * 2. 主串预处理：
	 *    - 调用build()方法构建前缀哈希数组和幂次数组
	 *    - 这些数组使我们能够在O(1)时间内计算任意子串的哈希值
	 * 3. 子串哈希值计算：
	 *    - 使用与主串相同的哈希函数计算needle的哈希值
	 *    - 字符映射为1-26，避免0值导致的哈希冲突
	 *    - 使用滚动计算方式高效累加哈希值
	 * 4. 滑动窗口匹配过程：
	 *    - 使用双指针l和r定义长度为m的窗口
	 *    - 对每个窗口计算其哈希值（O(1)时间）
	 *    - 比较窗口哈希值与子串哈希值
	 *    - 找到第一个匹配时立即返回窗口起始位置
	 * 5. 结果处理：
	 *    - 遍历完所有可能窗口后仍无匹配，返回-1
	 * <p>
	 * 哈希计算示例（以needle计算为例）：
	 * 对于字符串"abc"，base=499
	 * hash = (( ('a'-'a'+1) * 499 ) + ('b'-'a'+1)) * 499 + ('c'-'a'+1)
	 *      = ((1 * 499) + 2) * 499 + 3
	 *      = (499 + 2) * 499 + 3
	 *      = 501 * 499 + 3
	 *      = 249999 + 3
	 *      = 250002
	 * <p>
	 * 潜在问题与改进：
	 * - 哈希冲突：当前实现没有使用取模，可能存在不同字符串哈希值相同的情况
	 * - 数值溢出：对于长字符串，哈希值可能超出long类型范围
	 * - 优化方向：在哈希值相等时增加字符串直接比较，确保匹配正确性
	 * 
	 * @param str1 主字符串（haystack）
	 * @param str2 子字符串（needle）
	 * @return 子串第一次出现的下标，如果不存在则返回-1
	 * 
	 * 时间复杂度：O(n + m)
	 * - 预处理主串：O(n)
	 * - 计算子串哈希：O(m)
	 * - 滑动窗口匹配：O(n)
	 * <p>
	 * 空间复杂度：O(n)
	 * - 存储前缀哈希数组：O(n)
	 * - 存储幂次数组：O(n)
	 */
	public static int strStr(String str1, String str2) {
		// 将字符串转换为字符数组，提高访问效率
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;  // 主串长度
		int m = s2.length;  // 子串长度
		
		// 边界条件处理：空串匹配规则
		// 根据LeetCode题目要求，空字符串是任何字符串的子串，起始位置为0
		if (m == 0) {
			return 0;
		}
		
		// 边界条件处理：如果主串长度小于子串长度，不可能匹配成功
		if (n < m) {
			return -1;
		}
		
		// 预处理：构建主串的前缀哈希和幂次数组
		build(s1, n);
		
		// 计算子串(needle)的哈希值
		// 注意：字符映射为1-26，避免0值导致的哈希冲突
		long h2 = s2[0] - 'a' + 1;  // 初始化第一个字符的哈希值
		for (int i = 1; i < m; i++) {
			// 使用多项式哈希公式：hash = hash * base + current_char_value
			h2 = h2 * base + s2[i] - 'a' + 1;
		}
		
		// 滑动窗口算法：在主串中查找匹配的子串
		// 使用双指针l和r表示当前窗口的左右边界
		for (int l = 0, r = m - 1; r < n; l++, r++) {
			// 调用hash方法计算当前窗口的哈希值，并与子串哈希值比较
			if (hash(l, r) == h2) {
				// 找到匹配，返回窗口起始位置
				return l;
			}
		}
		
		// 遍历完整个主串后仍未找到匹配，返回-1
		return -1;
	}

	/**
	 * 最大字符串长度上限
	 * 用于预分配数组空间，避免频繁重新分配内存
	 * 这里设置为100005，足够处理大多数字符串问题
	 */
	public static int MAXN = 100005;

	/**
	 * 哈希基数
	 * 选择499（质数）作为基数，能有效减少哈希冲突
	 * 质数作为基数的优势：分布更均匀，冲突概率更低
	 */
	public static int base = 499;

	/**
	 * 存储base的幂次结果的数组
	 * pow[i] = base^i
	 * 用于快速计算子串哈希值时的权重
	 */
	public static long[] pow = new long[MAXN];

	/**
	 * 存储前缀哈希值的数组
	 * hash[i]表示子串s[0...i]的哈希值
	 * 基于该数组可以在O(1)时间内计算任意子串的哈希值
	 */
	public static long[] hash = new long[MAXN];

	/**
	 * 构建前缀哈希数组和幂次数组
	 * 这是整个字符串哈希算法的关键预处理步骤
	 * <p>
	 * 方法功能分解：
	 * 1. 幂次数组构建：
	 *    - 计算并存储base的0次幂到n-1次幂
	 *    - pow[0] = 1（任何数的0次方都是1）
	 *    - 后续幂次通过前一次幂次乘以base滚动计算
	 * 2. 前缀哈希数组构建：
	 *    - 计算每个前缀s[0...i]的哈希值
	 *    - 字符映射为1-26，避免0值导致的哈希冲突
	 *    - 使用滚动计算方式高效累加哈希值
	 * <p>
	 * 数学原理深度解析：
	 * - 前缀哈希递推公式：hash[i] = hash[i-1] * base + s[i]的映射值
	 * - 这个公式等价于多项式展开：
	 *   hash[i] = s[0]*base^i + s[1]*base^(i-1) + ... + s[i]*base^0
	 * - 证明：
	 *   假设hash[i-1] = s[0]*base^(i-1) + s[1]*base^(i-2) + ... + s[i-1]*base^0
	 *   则hash[i-1] * base = s[0]*base^i + s[1]*base^(i-1) + ... + s[i-1]*base^1
	 *   加上s[i]的映射值后：
	 *   hash[i] = s[0]*base^i + s[1]*base^(i-1) + ... + s[i-1]*base^1 + s[i]*base^0
	 *   这正是前缀s[0...i]的多项式哈希值
	 * <p>
	 * 计算优化：
	 * - 使用滚动计算避免重复计算，将时间复杂度从O(n²)优化到O(n)
	 * - 两个数组的计算可以在一次遍历中完成，提高效率
	 * <p>
	 * 示例：
	 * 对于字符串"abc"，base=499
	 * pow数组：[1, 499, 499*499=249001]
	 * hash数组：
	 * hash[0] = 'a'-'a'+1 = 1
	 * hash[1] = hash[0] * 499 + ('b'-'a'+1) = 1*499 + 2 = 501
	 * hash[2] = hash[1] * 499 + ('c'-'a'+1) = 501*499 + 3 = 250002
	 * 
	 * @param s 输入字符数组，代表要处理的字符串
	 * @param n 字符数组的长度
	 * 
	 * 时间复杂度：O(n) - 仅需一次线性遍历
	 * 空间复杂度：O(n) - 使用两个长度为n的数组存储计算结果
	 */
	public static void build(char[] s, int n) {
		// 初始化pow数组，pow[0] = 1（任何数的0次方都是1）
		pow[0] = 1;
		
		// 预计算所有需要的幂次值
		// 注意：这里使用了滚动计算，避免重复计算
		for (int i = 1; i < n; i++) {
			pow[i] = pow[i - 1] * base;
		}
		
		// 初始化hash数组，hash[0]为第一个字符的映射值
		// 将字符映射到1-26，避免0值导致的哈希冲突
		hash[0] = s[0] - 'a' + 1;
		
		// 滚动计算前缀哈希值
		// 哈希公式：hash[i] = hash[i-1] * base + s[i]的映射值
		// 这实现了多项式哈希：hash(s[0...i]) = s[0]*base^i + s[1]*base^(i-1) + ... + s[i]*base^0
		for (int i = 1; i < n; i++) {
			hash[i] = hash[i - 1] * base + s[i] - 'a' + 1;
		}
	}

	/**
	 * 计算子串s[l...r]的哈希值
	 * 该方法是字符串哈希算法的核心，实现了O(1)时间复杂度的任意子串哈希查询
	 * <p>
	 * 数学原理详解：
	 * 假设我们已经预处理好了前缀哈希数组hash和幂次数组pow，
	 * 如何从中提取子串s[l...r]的哈希值？
	 * <p>
	 * 分解推导过程：
	 * 1. 首先，hash[r] = s[0]*base^r + s[1]*base^(r-1) + ... + s[l-1]*base^(r-l+1) + s[l]*base^(r-l) + ... + s[r]*base^0
	 * 2. hash[l-1] = s[0]*base^(l-1) + s[1]*base^(l-2) + ... + s[l-1]*base^0
	 * 3. 将hash[l-1]乘以base^(r-l+1)，得到：
	 *    hash[l-1] * pow[r-l+1] = s[0]*base^r + s[1]*base^(r-1) + ... + s[l-1]*base^(r-l+1)
	 * 4. 从hash[r]中减去这部分，得到：
	 *    hash[r] - hash[l-1] * pow[r-l+1] = s[l]*base^(r-l) + ... + s[r]*base^0
	 *    这正是子串s[l...r]的哈希值
	 * <p>
	 * 边界条件处理：
	 * - 当l=0时，表示从字符串开头开始的子串
	 * - 此时不需要减去任何前缀，直接返回hash[r]
	 * - 当l>0时，需要减去hash[l-1] * pow[r-l+1]
	 * <p>
	 * 算法优势：
	 * - 时间复杂度：O(1)，不受子串长度影响
	 * - 空间复杂度：O(1)，仅使用常数级额外空间
	 * - 计算高效：利用预计算结果，避免重复计算
	 * <p>
	 * 注意事项：
	 * - 对于长字符串，由于没有使用模运算，可能发生数值溢出
	 * - 在生产环境中，应该添加模运算和哈希冲突处理机制
	 * - 对于哈希值相等的情况，最好进行字符串直接比较以确保正确性
	 * <p>
	 * 示例计算：
	 * 对于字符串"abcde"，base=499
	 * 假设我们要计算子串"bcd"（l=1, r=3）的哈希值
	 * hash[3] = a*499^3 + b*499^2 + c*499^1 + d*499^0
	 * hash[0] = a*499^0
	 * hash[0] * pow[3] = a*499^3
	 * 子串哈希值 = hash[3] - hash[0] * pow[3] = b*499^2 + c*499^1 + d*499^0
	 * 这正是"bcd"的哈希值
	 * 
	 * @param l 子串起始位置（包含），0-based索引
	 * @param r 子串结束位置（包含），0-based索引
	 * @return 子串s[l...r]的哈希值
	 * 
	 * 时间复杂度：O(1) - 常数时间操作
	 * 空间复杂度：O(1) - 无需额外空间
	 */
	public static long hash(int l, int r) {
		// 初始值为hash[r]（整个前缀的哈希值）
		long ans = hash[r];
		
		// 如果起始位置不是0，需要减去前面部分的影响
		if (l > 0) {
			// hash[l-1] * pow[r-l+1] 计算的是s[0...l-1]在hash[r]中的贡献
			// 减去这部分贡献后，得到的就是s[l...r]的哈希值
			ans -= hash[l - 1] * pow[r - l + 1];
		}
		
		return ans;
	}

	/**
	 * 哈希冲突概率的数学分析
	 * <p>
	 * 1. 生日悖论与哈希冲突关系
	 *    - 对于m个可能的哈希值和n个字符串，至少出现一次冲突的概率约为：
	 *      P(n, m) ≈ 1 - e^(-n²/(2m))
	 *    - 当n ≈ √(2m ln(1/(1-p))) 时，冲突概率为p
	 *    - 例如，当m=2^64（使用long类型），n≈2^32时，冲突概率约为14%
	 * <p>
	 * 2. 在LeetCode 28题中的具体分析
	 *    - 题目约束：haystack长度最多5*10^4，needle长度最多5*10^4
	 *    - 可能的子串数量：对于haystack长度n，最多有n-m+1个子串需要比较
	 *    - 使用long类型哈希值时，冲突概率非常低，对于LeetCode测试用例足够安全
	 *    - 但在生产环境中，尤其是处理敏感数据时，仍需考虑冲突问题
	 * <p>
	 * 3. 幂运算的数值溢出风险
	 *    - 对于长字符串，base的高次幂可能导致long类型溢出
	 *    - 例如：499^50 ≈ 1e125，远大于2^63-1（约9e18）
	 *    - 解决方案：使用模运算，选择两个大质数作为模数，实现双哈希
	 */
	
	/**
	 * 双哈希实现示例
	 * <p>
	 * 在生产环境中，为了降低哈希冲突的风险，通常会实现双哈希策略，
	 * 使用两个不同的哈希函数和模数，只有当两个哈希值都匹配时才认为字符串相同。
	 * <p>
	 * 双哈希实现代码示例：
	 * ```java
	 * // 双哈希参数定义
	 * private static final int BASE1 = 499;
	 * private static final int BASE2 = 911;
	 * private static final long MOD1 = 1000000007L;
	 * private static final long MOD2 = 1000000009L;
	 * 
	 * // 双哈希数组
	 * private static long[] hash1 = new long[MAXN];
	 * private static long[] hash2 = new long[MAXN];
	 * private static long[] pow1 = new long[MAXN];
	 * private static long[] pow2 = new long[MAXN];
	 * 
	 * // 双哈希构建方法
	 * public static void buildDoubleHash(char[] s, int n) {
	 *     // 初始化幂次数组
	 *     pow1[0] = 1;
	 *     pow2[0] = 1;
	 *     for (int i = 1; i < n; i++) {
	 *         pow1[i] = (pow1[i-1] * BASE1) % MOD1;
	 *         pow2[i] = (pow2[i-1] * BASE2) % MOD2;
	 *     }
	 *     
	 *     // 初始化哈希数组
	 *     hash1[0] = (s[0] - 'a' + 1) % MOD1;
	 *     hash2[0] = (s[0] - 'a' + 1) % MOD2;
	 *     for (int i = 1; i < n; i++) {
	 *         hash1[i] = (hash1[i-1] * BASE1 + (s[i] - 'a' + 1)) % MOD1;
	 *         hash2[i] = (hash2[i-1] * BASE2 + (s[i] - 'a' + 1)) % MOD2;
	 *     }
	 * }
	 * 
	 * // 双哈希子串查询
	 * public static Pair<Long, Long> getHash(int l, int r) {
	 *     long h1 = hash1[r];
	 *     long h2 = hash2[r];
	 *     
	 *     if (l > 0) {
	 *         h1 = (h1 - hash1[l-1] * pow1[r-l+1] % MOD1 + MOD1) % MOD1;
	 *         h2 = (h2 - hash2[l-1] * pow2[r-l+1] % MOD2 + MOD2) % MOD2;
	 *     }
	 *     
	 *     return new Pair<>(h1, h2);
	 * }
	 * ```
	 */
	
	/**
	 * 推荐的测试用例实现
	 * <p>
	 * 测试代码示例：
	 * ```java
	 * public class SubstringHashTest {
	 *     
	 *     @Test
	 *     public void testBasicCase() {
	 *         // 基本测试用例
	 *         assertEquals(0, Code03_SubstringHash.strStr("sadbutsad", "sad"));
	 *         assertEquals(-1, Code03_SubstringHash.strStr("leetcode", "leeto"));
	 *     }
	 *     
	 *     @Test
	 *     public void testEmptyNeedle() {
	 *         // 空子串测试
	 *         assertEquals(0, Code03_SubstringHash.strStr("hello", ""));
	 *     }
	 *     
	 *     @Test
	 *     public void testEmptyHaystack() {
	 *         // 空主串测试
	 *         assertEquals(-1, Code03_SubstringHash.strStr("", "a"));
	 *         assertEquals(0, Code03_SubstringHash.strStr("", ""));
	 *     }
	 *     
	 *     @Test
	 *     public void testNeedleLongerThanHaystack() {
	 *         // 子串比主串长
	 *         assertEquals(-1, Code03_SubstringHash.strStr("a", "ab"));
	 *     }
	 *     
	 *     @Test
	 *     public void testExactMatch() {
	 *         // 完全匹配
	 *         assertEquals(0, Code03_SubstringHash.strStr("abc", "abc"));
	 *     }
	 *     
	 *     @Test
	 *     public void testMultipleOccurrences() {
	 *         // 多次出现，应返回第一个
	 *         assertEquals(0, Code03_SubstringHash.strStr("abababa", "aba"));
	 *     }
	 * }
	 * ```
	 */
	
	/**
	 * 字符串哈希算法的算法比较分析
	 * <p>
	 * 1. 多项式滚动哈希 vs KMP算法
	 *    - 时间复杂度：两者都是O(n+m)
	 *    - 实现复杂度：哈希方法更简单直观，代码量更少
	 *    - 空间复杂度：哈希方法O(n)，KMP方法O(m)
	 *    - 可靠性：KMP无冲突风险，哈希方法存在理论冲突风险
	 *    - 适用场景：KMP仅适用于子串匹配，哈希方法可扩展到更多字符串问题
	 * <p>
	 * 2. 多项式滚动哈希 vs Rabin-Karp算法
	 *    - 本质联系：Rabin-Karp算法就是基于多项式滚动哈希的子串查找算法
	 *    - 区别：本题实现是Rabin-Karp算法的一种简化版本
	 *    - Rabin-Karp的完整实现通常包含冲突处理和模数运算
	 * <p>
	 * 3. 多项式滚动哈希 vs Java内置String.indexOf()
	 *    - 实现差异：Java的indexOf()使用的是优化的暴力算法，最坏O(n*m)，平均O(n+m)
	 *    - 性能对比：对于大多数实际情况，Java内置方法经过高度优化，性能可能更好
	 *    - 优势：本实现提供了更灵活的哈希功能，可扩展到其他字符串问题
	 */
	
	/**
	 * 字符串哈希算法理论深度解析
	 * <p>
	 * 字符串哈希是一种将可变长度的字符串映射为固定长度数值的技术，
	 * 是字符串处理和算法设计中的基础工具。
	 * <p>
	 * 本实现使用的多项式滚动哈希是一种经典的字符串哈希方法，下面对其核心原理进行深入剖析：
	 * 
	 * 1. 多项式哈希函数的数学基础：
	 *    - 核心定义：hash(s) = s[0]*base^(n-1) + s[1]*base^(n-2) + ... + s[n-1]*base^0
	 *    - 数学意义：将字符串视为base进制表示的数，每个字符是该进制下的一个数字
	 *    - 设计考量：
	 *      - base选择：通常为质数（如499, 911, 919），提供更好的分布性
	 *      - 字符映射：避免使用0值，防止前缀0导致的哈希冲突
	 *      - 哈希空间：选择足够大以减少冲突概率
	 * <p>
	 * 2. 高效计算技术 - 滚动哈希：
	 *    - 常规方法：直接计算每个字符串哈希需O(n²)时间，效率低下
	 *    - 滚动优化：利用递推关系，将时间复杂度降至O(n)
	 *    - 递推公式：hash[i] = hash[i-1] * base + s[i]的映射值
	 *    - 正确性证明：
	 *      假设hash[i-1] = s[0]*base^(i-1) + s[1]*base^(i-2) + ... + s[i-1]*base^0
	 *      则hash[i-1] * base = s[0]*base^i + s[1]*base^(i-1) + ... + s[i-1]*base^1
	 *      加上s[i]后：hash[i] = hash[i-1] * base + s[i] = s[0]*base^i + ... + s[i]
	 *      这正是前缀s[0...i]的哈希值
	 * <p>
	 * 3. 子串哈希查询的数学推导：
	 *    - 目标：从预处理好的前缀哈希中提取任意子串s[l...r]的哈希值
	 *    - 数学分解：
	 *      1. hash[r] = s[0]*base^r + s[1]*base^(r-1) + ... + s[r]*base^0
	 *      2. 我们需要移除s[0...l-1]的影响
	 *      3. 这些字符在hash[r]中的权重相当于乘以base^(r-l+1)
	 *      4. 因此：hash(l,r) = hash[r] - hash[l-1] * base^(r-l+1)
	 *    - 算法价值：将子串哈希查询时间从O(n)降至O(1)
	 * <p>
	 * 总结：多项式滚动哈希是一种高效、灵活的字符串哈希方法，
	 * 在字符串匹配、子串查询、去重等多种场景中有广泛应用。
	 * 正确实现和优化的哈希算法能够在保持代码简洁性的同时，提供出色的性能。
	 */
}
