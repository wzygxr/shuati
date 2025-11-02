package class105;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;

/**
 * Codeforces 271D - Good Substrings 问题实现
 * <p>
 * 题目链接：https://codeforces.com/contest/271/problem/D
 * <p>
 * 题目描述：
 * 给定一个字符串s，由小写英文字母组成。有些英文字母是好的，其余的是坏的。
 * 字符串s[l...r]是好的，当且仅当其中最多有k个坏字母。
 * 任务是找出字符串s中不同好子串的数量（内容不同的子串视为不同）。
 * <p>
 * 算法核心思想：
 * 1. 滑动窗口枚举：从每个起始位置开始，向右扩展并统计坏字母数量
 * 2. 哈希去重：使用多项式滚动哈希和HashSet高效存储不同子串
 * 3. 早期剪枝：当坏字母数量超过k时立即停止扩展
 * 4. 预计算优化：预先计算哈希值和幂次数组，支持O(1)时间子串哈希值查询
 * <p>
 * 多项式滚动哈希数学原理：
 * - 哈希函数定义：H(s) = (s[0]*base^(n-1) + s[1]*base^(n-2) + ... + s[n-1]*base^0) mod mod_value
 * - 在本题中，为了简化实现，我们没有使用模数（mod_value），这在字符串较短时是安全的
 * - 前缀哈希：hash[i] = hash[i-1] * base + s[i]，其中s[i]被映射为一个整数（如s[i]-'a'+1）
 * - 子串哈希：hash(l,r) = hash[r] - hash[l-1] * base^(r-l+1)（当l>0时）
 * <p>
 * 算法详细步骤：
 * 1. 预处理阶段：
 *    - 构建坏字母标记数组：根据输入的好字母标记字符串，标记哪些字母是坏字母
 *    - 预计算幂次数组：pow[i] = base^i，用于O(1)时间计算子串哈希值
 *    - 计算前缀哈希数组：hash[i]表示s[0...i]的哈希值
 * 2. 枚举阶段（核心）：
 *    - 遍历每个可能的起始位置i (0 ≤ i < n)
 *    - 对于每个i，从j=i开始向右扩展子串，同时维护坏字母计数cnt
 *    - 对于每个j，检查s[j]是否为坏字母，若是则cnt++
 *    - 剪枝优化：当cnt > k时，立即终止该起始位置的扩展（由于后续子串只会包含更多坏字母）
 *    - 对每个有效子串s[i...j]，计算其哈希值并加入HashSet
 * 3. 结果输出：HashSet的大小即为不同好子串的数量
 * <p>
 * 算法正确性证明：
 * - 对于任意起始位置i，随着j的增加，坏字母计数cnt是非递减的
 * - 因此，一旦cnt超过k，对于所有j' > j，s[i...j']也必定包含超过k个坏字母
 * - 这证明了我们的剪枝策略的正确性
 * - 哈希去重机制确保我们只统计不同的子串，HashSet自动处理重复情况
 * <p>
 * 算法优势：
 * - 内存效率：通过哈希技术避免存储所有子串的原始内容，仅存储哈希值
 * - 时间效率：预计算策略和O(1)时间的子串哈希查询
 * - 剪枝优化：早期终止无效扩展，减少不必要的计算
 * - 实现简洁：算法逻辑清晰，易于理解和维护
 * <p>
 * 时间复杂度分析：
 * - 预处理阶段：O(n + 26)，其中n是字符串长度，26是字母表大小
 * - 枚举阶段：
 *   - 最坏情况下为O(n²)（当k较大时）
 *   - 平均情况下由于剪枝优化，实际时间远小于O(n²)
 *   - 每个子串哈希值计算为O(1)
 *   - HashSet的插入操作为平均O(1)时间
 * - 总体时间复杂度：O(n²)
 * <p>
 * 空间复杂度分析：
 * - 前缀哈希数组：O(n)
 * - 幂次数组：O(n)
 * - 坏字母标记数组：O(1)，固定大小26
 * - HashSet存储：O(m)，其中m是不同好子串的数量，最坏情况为O(n²)
 * - 总体空间复杂度：O(n + m) = O(n²)
 * <p>
 * 哈希冲突分析：
 * - 本题没有使用模数，理论上可能发生哈希冲突（不同子串计算得到相同哈希值）
 * - 对于Codeforces 271D的数据规模（n≤1500），这种冲突概率非常低
 * - 在实际应用中，可以考虑使用双哈希（两个不同的base和mod组合）进一步降低冲突概率
 * <p>
 * 相似题目：
 * 1. LeetCode 1698: Number of Distinct Substrings in a String - 计算不同子串数量
 * 2. LeetCode 2707: Extra Characters in a String - 字符串处理与哈希应用
 * 3. LintCode 1850: 好字符串 - 类似的好子串统计问题
 * 4. HackerRank: String Construction - 字符串构建与去重
 * 5. CodeChef SUBINC - 子序列问题
 * 6. UVa 11752: The Super Powers - 哈希应用
 * 7. 牛客 NC158: 最大回文子串 - 字符串子串处理
 * <p>
 * 测试链接：https://codeforces.com/contest/271/problem/D
 * <p>
 * 三种语言实现参考：
 * - Java实现：当前文件
 * - Python实现：Code07_GoodSubstrings.py
 * - C++实现：Code07_GoodSubstrings.cpp（待实现）
 * 
 * @author Algorithm Journey
 * @note 提交时请将类名改为"Main"以通过评测
 * @see <a href="https://codeforces.com/contest/271/problem/D">Codeforces 271D - Good Substrings</a>
 */
public class Code07_GoodSubstrings {

	/**
	 * 最大字符串长度
	 * 设置为1501，根据Codeforces题目约束，字符串长度不超过1500
	 */
	public static int MAXN = 1501;

	/**
	 * 哈希基数，选择499作为大质数以减少哈希冲突
	 * 使用质数作为基数可以使哈希分布更均匀
	 */
	public static int base = 499;

	/**
	 * bad数组标记每个字母是否是坏字母
	 * bad[i]为true表示字母'a'+i是坏字母
	 * 大小固定为26，对应26个小写英文字母
	 */
	public static boolean[] bad = new boolean[26];

	/**
	 * 存储base的幂次，避免重复计算
	 * pow[i] = base^i，用于快速计算子串哈希值
	 */
	public static long[] pow = new long[MAXN];

	/**
	 * 存储字符串的前缀哈希值
	 * hash[i]表示字符串前i+1个字符（s[0...i]）的哈希值
	 */
	public static long[] hash = new long[MAXN];

	/**
	 * 主方法，处理输入输出并执行算法的核心逻辑
	 * <p>
	 * 处理流程详解：
	 * 1. 输入处理阶段：
	 *    - 使用BufferedReader高效读取输入数据
	 *    - 读取字符串s并转换为字符数组以便快速访问
	 *    - 读取好字母标记字符串（26个字符，'1'表示好字母，'0'表示坏字母）
	 *    - 读取整数k，表示允许的最大坏字母数量
	 *    
	 * 2. 预处理阶段：
	 *    - 构建坏字母标记数组：将好字母标记转换为布尔数组
	 *    - 预计算幂次数组：pow[i] = base^i，支持O(1)时间子串哈希计算
	 *    - 构建前缀哈希数组：使用多项式哈希算法计算前缀哈希值
	 *    
	 * 3. 子串枚举与去重阶段：
	 *    - 使用双重循环枚举所有可能的好子串
	 *    - 外层循环遍历起始位置i
	 *    - 内层循环从i开始向右扩展，同时维护坏字母计数
	 *    - 使用HashSet自动去重，确保每个不同子串只被统计一次
	 *    
	 * 4. 结果输出：
	 *    - 输出HashSet的大小，即为不同好子串的数量
	 *    - 使用PrintWriter高效输出结果
	 * 
	 * @param args 命令行参数（未使用）
	 * @throws IOException 当输入输出操作发生错误时抛出
	 * 
	 * 输入格式：
	 * 第一行：字符串s（由小写英文字母组成，长度≤1500）
	 * 第二行：长度为26的字符串，'1'表示对应位置字母是好的，'0'表示坏的
	 * 第三行：整数k（0≤k≤1500），表示允许的最大坏字母数量
	 * 
	 * 输出格式：
	 * 一个整数，表示不同好子串的数量
	 * 
	 * 示例输入：
	 * abcabc
	 * 101010101010101010101010101
	 * 1
	 * 
	 * 示例输出：
	 * 9
	 */
	public static void main(String[] args) throws IOException {
		// 使用BufferedReader和PrintWriter进行高效的输入输出
		// 在大规模数据输入输出场景下，比Scanner和System.out.println效率高得多
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		// 读取输入字符串并转换为字符数组，方便处理
		char[] s = in.readLine().toCharArray();
		int n = s.length; // 字符串长度

		// 读取好字母标记字符串，'1'表示好字母，'0'表示坏字母
		char[] mark = in.readLine().toCharArray();

		// 构建坏字母标记数组
		// bad[i]为true表示字母'a'+i是坏字母
		for (int i = 0; i < 26; i++) {
			bad[i] = mark[i] == '0'; // '0'表示坏字母
		}

		// 读取k值，即允许的最大坏字母数量
		int k = Integer.valueOf(in.readLine());

		// 预处理阶段1：预计算base的幂次数组
		// 幂次数组pow是多项式哈希算法的关键组成部分，用于O(1)时间计算子串哈希值
		pow[0] = 1; // 基础情况：base^0 = 1
		for (int i = 1; i < n; i++) {
			pow[i] = pow[i - 1] * base; // 递推计算：pow[i] = pow[i-1] * base
			// 注意：这里没有进行模运算，对于n≤1500的约束，long类型可以容纳
		}

		// 预处理阶段2：构建前缀哈希数组
		// 实现多项式滚动哈希算法，为每个前缀计算哈希值
		hash[0] = s[0] - 'a' + 1; // 第一个字符的哈希值
		// 加1的目的是避免字符'a'被映射为0，从而导致不同前缀可能产生相同的哈希值
		// 例如："a"和空字符串，如果不+1都可能计算为0
		for (int i = 1; i < n; i++) {
			// 哈希值递推公式：hash[i] = hash[i-1] * base + s[i]的映射值
			// 这构建了一个多项式表示：hash[i] = s[0]*base^i + s[1]*base^(i-1) + ... + s[i]
			hash[i] = hash[i - 1] * base + (s[i] - 'a' + 1);
		}

		// 使用HashSet存储不同好子串的哈希值，实现自动去重
		// 这是算法去重的核心，HashSet的特性确保相同的哈希值只会存储一次
		// 使用Long类型存储哈希值，可以容纳较大的数值
		HashSet<Long> set = new HashSet<>();

		// 枚举所有可能的子串起始位置i
		// 这是算法的核心部分，实现了子串枚举和剪枝优化
		for (int i = 0; i < n; i++) {
			// 从位置i开始，向右扩展子串，同时统计坏字母数量
			// j是子串的结束位置，cnt是当前子串中的坏字母计数
			for (int j = i, cnt = 0; j < n; j++) {
				// 检查当前字符s[j]是否是坏字母
				// bad数组的索引为字符减去'a'的值
				if (bad[s[j] - 'a']) {
					cnt++; // 坏字母计数加1
				}

				// 剪枝优化：如果坏字母数量超过k，停止向右扩展
				// 这是一个关键的优化，基于以下观察：
				// - 当j增加时，子串s[i...j]只在右侧增加了一个字符
				// - 因此，坏字母计数cnt在j增加时只能保持不变或增加
				// - 一旦cnt超过k，对于所有j' > j，s[i...j']也必然包含超过k个坏字母
				if (cnt > k) {
					break; // 提前终止内层循环，避免不必要的计算
				}

				// 计算子串s[i...j]的哈希值并加入set
				// hash方法在O(1)时间内计算子串的哈希值
				// 如果是重复的子串，set会自动去重（基于哈希值）
				set.add(hash(i, j));
			}
		}

		// 输出不同好子串的数量，即set的大小
		// 由于HashSet自动去重，set.size()正好是不同子串的数量
		out.println(set.size());
		// 刷新输出缓冲区并关闭流
		out.flush();
		out.close();
		in.close();
	}

	/**
	 * 计算子串s[l...r]的哈希值
	 * <p>
	 * 利用预处理好的前缀哈希数组和幂次数组，在O(1)时间内计算任意子串的哈希值。
	 * 这是多项式滚动哈希算法的核心优势，使我们能够高效地比较子串而无需显式存储它们。
	 * <p>
	 * 数学原理解析：
	 * - 前缀哈希定义：hash[r] = s[0]*base^r + s[1]*base^(r-1) + ... + s[r]*base^0
	 * - 子串哈希计算原理：要得到s[l...r]的哈希值，需要从hash[r]中减去s[0...l-1]部分的影响
	 * - 当l=0时，子串就是前缀本身，直接返回hash[r]
	 * - 当l>0时，需要将hash[l-1]乘以base^(r-l+1)，然后从hash[r]中减去
	 * <p>
	 * 数学推导：
	 * hash[r] = s[0]*base^r + s[1]*base^(r-1) + ... + s[l-1]*base^(r-l+1) + s[l]*base^(r-l) + ... + s[r]
	 * hash[l-1]*base^(r-l+1) = (s[0]*base^(l-1) + ... + s[l-1]) * base^(r-l+1)
	 *                         = s[0]*base^r + ... + s[l-1]*base^(r-l+1)
	 * 因此：hash(r) - hash(l-1)*base^(r-l+1) = s[l]*base^(r-l) + ... + s[r]
	 * 这正是子串s[l...r]的哈希值
	 * <p>
	 * 详细示例：
	 * 假设base=911，字符串为"abc"，则：
	 * - hash[0] = 'a'-'a'+1 = 1
	 * - hash[1] = hash[0]*911 + ('b'-'a'+1) = 1*911 + 2 = 913
	 * - hash[2] = hash[1]*911 + ('c'-'a'+1) = 913*911 + 3 = 831743 + 3 = 831746
	 * - 计算子串"bc"的哈希值：
	 *   hash(1,2) = hash[2] - hash[0] * base^2 = 831746 - 1*911² = 831746 - 829921 = 1825
	 *   而直接计算"bc"的哈希值：'b'-'a'+1)*base + ('c'-'a'+1) = 2*911 + 3 = 1822 + 3 = 1825
	 * <p>
	 * 注意事项：
	 * - 为避免整数溢出，在实际应用中通常会使用大质数作为模数
	 * - 本题中没有使用模数，因为题目约束下（n≤1500）使用long类型可以存储大部分情况
	 * - 在实际比赛中，可以考虑使用双哈希（两个不同的base和mod组合）降低哈希冲突概率
	 * 
	 * @param l 子串起始位置（包含，从0开始）
	 * @param r 子串结束位置（包含，从0开始）
	 * @return 子串s[l...r]的哈希值
	 * 
	 * 时间复杂度：O(1) 常量时间操作
	 * 空间复杂度：O(1) 不需要额外空间
	 */
	public static long hash(int l, int r) {
		long ans = hash[r]; // 获取前缀哈希值到r位置
		// 如果起始位置不是0，则需要减去前面部分的影响
		if (l > 0) {
			// 减去0到l-1的哈希值乘以base^(r-l+1)，得到l到r的哈希值
			// 这一步实际上是将hash[l-1]左移(r-l+1)位（乘以base的幂次），然后从hash[r]中减去
			// 数学上，这相当于从多项式表示中移除前l项
			ans -= hash[l - 1] * pow[r - l + 1];
		}
		return ans; // 返回子串s[l...r]的哈希值
	}

	/**
	 * 哈希冲突概率的数学分析
	 * <p>
	 * 在多项式滚动哈希中，哈希冲突是不可避免的。冲突概率受以下因素影响：
	 * <ol>
	 * <li><b>基数选择(base)</b>：本实现选择499作为基数
	 * <li><b>模数(mod)</b>：本实现未使用模数，可能增加大字符串的溢出风险
	 * <li><b>字符串长度</b>：较长的字符串增加哈希冲突的可能性
	 * <li><b>哈希值分布</b>：使用质数作为基数能更均匀地分布哈希值
	 * </ol>
	 * <p>
	 * 哈希冲突概率计算：
	 * 根据生日悖论，当有m个不同的子串和n个可能的哈希值时，冲突概率近似为：
	 * P ≈ 1 - e^(-m²/(2n))
	 * <p>
	 * 在本题中，由于我们使用long类型(64位)且字符串长度不超过1500，哈希值空间约为2^64，
	 * 对于最坏情况下约1500²=2.25×10^6个子串，冲突概率极低，可以忽略不计。
	 */

	/**
	 * 双哈希实现示例
	 * <p>
	 * 为了进一步降低哈希冲突的可能性，可以采用双哈希技术，使用两组不同的base和mod组合：
	 * <pre><code>
	 * // 定义两组哈希参数
	 * public static int base1 = 499;  // 第一组基数
	 * public static int mod1 = 1000000007; // 第一组模数（大质数）
	 * public static int base2 = 503;  // 第二组基数
	 * public static int mod2 = 1000000009; // 第二组模数（另一个大质数）
	 * 
	 * // 预计算两组幂次数组
	 * public static long[] pow1 = new long[MAXN];
	 * public static long[] pow2 = new long[MAXN];
	 * 
	 * // 预计算两组前缀哈希数组
	 * public static long[] hash1 = new long[MAXN];
	 * public static long[] hash2 = new long[MAXN];
	 * 
	 * // 预处理方法
	 * public static void preprocess(char[] s) {
	 *     int n = s.length;
	 *     
	 *     // 预处理第一组幂次数组
	 *     pow1[0] = 1;
	 *     for (int i = 1; i < n; i++) {
	 *         pow1[i] = (pow1[i-1] * base1) % mod1;
	 *     }
	 *     
	 *     // 预处理第二组幂次数组
	 *     pow2[0] = 1;
	 *     for (int i = 1; i < n; i++) {
	 *         pow2[i] = (pow2[i-1] * base2) % mod2;
	 *     }
	 *     
	 *     // 计算第一组前缀哈希
	 *     hash1[0] = (s[0] - 'a' + 1) % mod1;
	 *     for (int i = 1; i < n; i++) {
	 *         hash1[i] = (hash1[i-1] * base1 + (s[i] - 'a' + 1)) % mod1;
	 *     }
	 *     
	 *     // 计算第二组前缀哈希
	 *     hash2[0] = (s[0] - 'a' + 1) % mod2;
	 *     for (int i = 1; i < n; i++) {
	 *         hash2[i] = (hash2[i-1] * base2 + (s[i] - 'a' + 1)) % mod2;
	 *     }
	 * }
	 * 
	 * // 双哈希计算方法
	 * public static long getHash1(int l, int r) {
	 *     long ans = hash1[r];
	 *     if (l > 0) {
	 *         ans = (ans - (hash1[l-1] * pow1[r-l+1]) % mod1 + mod1) % mod1;
	 *     }
	 *     return ans;
	 * }
	 * 
	 * public static long getHash2(int l, int r) {
	 *     long ans = hash2[r];
	 *     if (l > 0) {
	 *         ans = (ans - (hash2[l-1] * pow2[r-l+1]) % mod2 + mod2) % mod2;
	 *     }
	 *     return ans;
	 * }
	 * 
	 * // 使用Pair存储双哈希值
	 * class Pair {
	 *     long hash1;
	 *     long hash2;
	 *     
	 *     Pair(long h1, long h2) {
	 *         this.hash1 = h1;
	 *         this.hash2 = h2;
	 *     }
	 *     
	 *     @Override
	 *     public boolean equals(Object obj) {
	 *         if (this == obj) return true;
	 *         if (obj == null || getClass() != obj.getClass()) return false;
	 *         Pair pair = (Pair) obj;
	 *         return hash1 == pair.hash1 && hash2 == pair.hash2;
	 *     }
	 *     
	 *     @Override
	 *     public int hashCode() {
	 *         return Objects.hash(hash1, hash2);
	 *     }
	 * }
	 * 
	 * // 在主方法中使用双哈希
	 * HashSet<Pair> set = new HashSet<>();
	 * // 计算子串哈希并存储
	 * set.add(new Pair(getHash1(i,j), getHash2(i,j)));
	 * </code></pre>
	 * <p>
	 * 双哈希的优势在于，两个不同字符串同时在两组哈希中发生冲突的概率极低，
	 * 即使每组哈希的冲突概率为1e-9，双哈希的总冲突概率约为1e-18，
	 * 对于大多数算法竞赛问题来说，这已经足够安全。
	 */

	/**
	 * 推荐测试用例实现
	 * <p>
	 * 以下是针对本算法的推荐测试用例，覆盖不同的边界情况和关键场景：
	 * <pre><code>
	 * // 测试用例1：空字符串边界情况
	 * // 预期输出：0
	 * 
	 * // 测试用例2：k=0（不允许任何坏字母）
	 * String s2 = "abcdefg";
	 * String mark2 = "11110001111111111111111111";
	 * int k2 = 0;
	 * // 预期输出：仅包含好字母的最长连续子串中的不同子串数
	 * 
	 * // 测试用例3：k=n（允许所有字母）
	 * String s3 = "xyzxyz";
	 * String mark3 = "00000000000000000000000000";
	 * int k3 = 6;
	 * // 预期输出：所有可能的不同子串数（对于"xyzxyz"为15）
	 * 
	 * // 测试用例4：所有字母都是好的
	 * String s4 = "abcdef";
	 * String mark4 = "11111111111111111111111111";
	 * int k4 = 3;
	 * // 预期输出：6*7/2 = 21（所有子串都是好的）
	 * 
	 * // 测试用例5：频繁重复的子串
	 * String s5 = "aaaaa";
	 * String mark5 = "11111111111111111111111111";
	 * int k5 = 2;
	 * // 预期输出：5（每个长度的子串各一个）
	 * 
	 * // 测试用例6：最大约束情况
	 * // s长度为1500，k=1500，所有字母都是坏的
	 * // 预期输出：计算所有可能的不同子串数
	 * 
	 * // 测试用例7：哈希冲突测试
	 * // 构造两个不同但哈希值相同的子串（在当前实现中可能很难构造，但理论上存在）
	 * </code></pre>
	 * <p>
	 * 测试用例设计原则：
	 * 1. 边界情况测试：空字符串、k=0、k=最大可能值
	 * 2. 特殊模式测试：全好字母、全坏字母、重复子串
	 * 3. 性能测试：最大输入规模下的算法表现
	 * 4. 正确性测试：已知答案的标准测试用例
	 * <p>
	 * 测试方法建议：
	 * - 使用JUnit或其他测试框架编写单元测试
	 * - 将算法与暴力解法（枚举所有子串并手动去重）进行对比
	 * - 对每组测试用例记录算法运行时间，监控性能
	 */

	/**
	 * 字符串哈希算法比较分析
	 * <p>
	 * 本题实现了多项式滚动哈希，下面将其与其他常用的字符串哈希方法进行比较：
	 * <table border="1">
	 * <tr>
	 *   <th>哈希方法</th>
	 *   <th>优点</th>
	 *   <th>缺点</th>
	 *   <th>时间复杂度</th>
	 *   <th>空间复杂度</th>
	 * </tr>
	 * <tr>
	 *   <td><b>多项式滚动哈希</b></td>
	 *   <td>
	 *     - O(1)时间子串哈希查询<br>
	 *     - 实现简单直观<br>
	 *     - 支持增量计算<br>
	 *     - 适用于字符串匹配和子串查询问题
	 *   </td>
	 *   <td>
	 *     - 可能发生哈希冲突<br>
	 *     - 大数运算可能导致溢出<br>
	 *     - 需要适当选择参数(base和mod)
	 *   </td>
	 *   <td>预处理O(n)，查询O(1)</td>
	 *   <td>O(n)</td>
	 * </tr>
	 * <tr>
	 *   <td><b>KMP算法</b></td>
	 *   <td>
	 *     - 精确匹配，无哈希冲突<br>
	 *     - 线性时间复杂度<br>
	 *     - 适用于精确模式匹配
	 *   </td>
	 *   <td>
	 *     - 不支持快速计算任意子串哈希<br>
	 *     - 预处理较为复杂<br>
	 *     - 不适合子串去重问题
	 *   </td>
	 *   <td>O(n+m)</td>
	 *   <td>O(m)</td>
	 * </tr>
	 * <tr>
	 *   <td><b>Rabin-Karp算法</b></td>
	 *   <td>
	 *     - 结合了滑动窗口和哈希的优点<br>
	 *     - 适用于多模式匹配<br>
	 *     - 实现相对简单
	 *   </td>
	 *   <td>
	 *     - 平均时间复杂度依赖于哈希冲突概率<br>
	 *     - 最坏情况O(nm)<br>
	 *     - 不直接支持任意子串哈希
	 *   </td>
	 *   <td>平均O(n+m)，最坏O(nm)</td>
	 *   <td>O(1)</td>
	 * </tr>
	 * <tr>
	 *   <td><b>Suffix Automaton</b></td>
	 *   <td>
	 *     - 空间效率高（O(n)）<br>
	 *     - 可以有效计算不同子串数量<br>
	 *     - 支持多种字符串查询操作
	 *   </td>
	 *   <td>
	 *     - 实现复杂<br>
	 *     - 概念抽象，理解难度大<br>
	 *     - 构建时间相对较长
	 *   </td>
	 *   <td>O(n)</td>
	 *   <td>O(n)</td>
	 * </tr>
	 * <tr>
	 *   <td><b>Trie树</b></td>
	 *   <td>
	 *     - 精确存储每个子串<br>
	 *     - 支持前缀查询<br>
	 *     - 无哈希冲突问题
	 *   </td>
	 *   <td>
	 *     - 空间消耗大（最坏O(n²)）<br>
	 *     - 构建时间长<br>
	 *     - 不支持子串哈希查询
	 *   </td>
	 *   <td>O(n²)</td>
	 *   <td>O(n²)</td>
	 * </tr>
	 * </table>
	 * <p>
	 * 本题选择多项式滚动哈希的理由：
	 * 1. 问题需要高效地存储不同子串，哈希技术可以有效去重
	 * 2. 滑动窗口+剪枝策略与哈希结合可以达到良好的时间效率
	 * 3. 实现相对简单，代码量少，便于调试和优化
	 * 4. 对于题目约束下的数据规模，冲突概率极低，可以接受
	 * <p>
	 * 算法优化建议：
	 * 1. 对于更大规模的数据集，建议采用双哈希技术
	 * 2. 可以考虑使用模数运算来避免溢出，选择大质数作为模数
	 * 3. 对于特殊情况（如全重复字符串），可以增加额外的剪枝策略
	 * 4. 在内存受限情况下，可以考虑使用Suffix Automaton代替哈希方法
	 */
}