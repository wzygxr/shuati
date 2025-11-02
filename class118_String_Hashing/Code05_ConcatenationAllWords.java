package class105;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * LeetCode 30 串联所有单词的子串问题实现
 * <p>
 * 题目链接：https://leetcode.cn/problems/substring-with-concatenation-of-all-words/
 * <p>
 * 题目描述：
 * 给定一个字符串s和一个字符串数组words，所有单词的长度相同。
 * 找出s中恰好串联words中所有单词的子串的起始位置。
 * 子串必须完全包含words中的所有单词，中间不能有其他字符，且不考虑words中单词的顺序。
 * <p>
 * 具体示例：
 * 输入：s = "barfoothefoobarman", words = ["foo","bar"]
 * 输出：[0,9]
 * 解释：
 * 从索引0开始的子串是"barfoo"，它由"bar"和"foo"串联而成
 * 从索引9开始的子串是"foobar"，它由"foo"和"bar"串联而成
 * <p>
 * 输入：s = "wordgoodgoodgoodbestword", words = ["word","good","best","word"]
 * 输出：[]
 * 解释：无法找到任何包含所有单词的子串
 * <p>
 * 算法核心思想：
 * 结合同余分组、滑动窗口、债务计数和字符串哈希技术，实现高效的子串匹配
 * <p>
 * 算法详细设计：
 * 1. 同余分组策略：
 *    - 将所有可能的起始位置按单词长度wordLen分为wordLen组
 *    - 每组起始位置为0, 1, 2, ..., wordLen-1
 *    - 这样可以确保每个窗口内的单词边界严格对齐，避免重叠分割
 * 2. 滑动窗口机制：
 *    - 对每组起始位置，维护一个大小为allLen(wordLen * wordNum)的窗口
 *    - 窗口每次滑动一个单词长度，高效更新窗口内容
 * 3. 债务计数优化：
 *    - 使用debt变量实时跟踪未匹配的单词数量
 *    - 避免了每次都需要比较两个哈希表的所有键值对
 * 4. 字符串哈希技术：
 *    - 使用多项式滚动哈希算法快速计算子串哈希值
 *    - 以哈希值为键进行词频统计，避免字符串比较的开销
 * <p>
 * 同余分组的数学原理：
 * 对于任何有效子串的起始位置i，必定满足i ≡ r (mod wordLen)，其中r ∈ [0, wordLen-1]
 * 因此可以将起始位置按模wordLen分组，每组独立处理，无需检查所有可能的起始位置
 * <p>
 * 债务计数的工作原理：
 * 1. 初始债务debt = wordNum，表示需要匹配的单词总数
 * 2. 当一个单词被加入窗口且满足频率约束时，debt减1
 * 3. 当一个单词被移出窗口且破坏频率约束时，debt加1
 * 4. 当debt=0时，表示窗口内恰好包含所有需要的单词
 * <p>
 * 时间复杂度分析：
 * - 预处理阶段：
 *   - 计算单词哈希值和构建目标词频表：O(m)，m为words中所有单词的总长度
 *   - 构建字符串s的哈希数组：O(n)，n为s的长度
 * - 匹配阶段：
 *   - 同余分组：wordLen组，每组独立处理
 *   - 每组处理：初始化窗口O(wordNum)，滑动窗口O((n - allLen)/wordLen)
 *   - 总体时间复杂度：O(m + n + wordLen * ((n - allLen)/wordLen + wordNum)) = O(m + n)
 * <p>
 * 空间复杂度分析：
 * - 哈希表：O(k)，k为words中不同单词的数量
 * - 哈希数组和幂次数组：O(n)
 * - 结果列表：O(n)（最坏情况下每个位置都匹配）
 * - 总体空间复杂度：O(n + k)
 * <p>
 * 算法优化策略：
 * 1. 同余分组：将问题分解为wordLen个独立子问题，避免重复计算
 * 2. 滑动窗口：窗口滑动时仅更新边界单词，时间复杂度降为O(1)每步
 * 3. 债务计数：将哈希表比较转化为单个整数检查，大幅降低常数因子
 * 4. 字符串哈希：将字符串比较转换为整数比较，提高效率
 * 5. 预分配内存：使用预定义的静态数组存储哈希值和幂次数组
 * <p>
 * 算法正确性证明：
 * 假设存在一个有效子串起始于位置i，那么i必定属于某个同余类r = i % wordLen
 * 在处理该同余类时，窗口将滑动到i位置，并通过债务计数机制检测到匹配
 * 因此，算法不会遗漏任何有效解，也不会包含无效解
 * <p>
 * 测试链接：https://leetcode.cn/problems/substring-with-concatenation-of-all-words/
 * 
 * @author Algorithm Journey
 */
public class Code05_ConcatenationAllWords {

/**
	 * 找出字符串s中所有串联子串的起始索引
	 * <p>
	 * 该方法实现了LeetCode 30题的高效解决方案，采用同余分组、滑动窗口、
	 * 债务计数和字符串哈希的组合策略，达到O(n + m)的时间复杂度。
	 * <p>
	 * 详细实现步骤：
	 * 1. 边界条件处理：
	 *    - 检查s或words是否为空
	 *    - 为空时直接返回空列表
	 * 
	 * 2. 构建目标词频表：
	 *    - 使用HashMap存储每个单词的哈希值及其出现次数
	 *    - 通过hash(String)方法计算每个单词的哈希值
	 *    - 使用getOrDefault方法高效更新词频计数
	 * 
	 * 3. 预处理字符串哈希：
	 *    - 调用build(String)方法构建字符串s的前缀哈希数组
	 *    - 这使得后续可以在O(1)时间内计算任意子串的哈希值
	 * 
	 * 4. 同余分组处理：
	 *    - 外层循环遍历每个可能的初始偏移量(0到wordLen-1)
	 *    - 每个偏移量对应一个同余类
	 *    - 只有当init + allLen <= n时才处理，避免无效计算
	 * 
	 * 5. 窗口初始化：
	 *    - 对每组起始位置，初始化debt为wordNum（需要匹配的单词总数）
	 *    - 初始化窗口哈希表，记录当前窗口内的单词频率
	 *    - 将窗口内的前wordNum个单词加入哈希表并更新债务计数
	 *    - 债务计数规则：如果单词在目标词频中且未超出频率，则债务减1
	 * 
	 * 6. 初始窗口检查：
	 *    - 如果债务为0，表示初始窗口完全匹配，添加起始位置到结果
	 * 
	 * 7. 滑动窗口处理：
	 *    - 使用双指针系统：l1/r1表示要移出的单词，l2/r2表示要移入的单词
	 *    - 移出左侧单词：
	 *      - 计算该单词的哈希值并从窗口中移除
	 *      - 如果移除后导致频率不足（需要重新匹配），债务加1
	 *    - 移入右侧单词：
	 *      - 计算该单词的哈希值并加入窗口
	 *      - 如果加入后仍满足频率约束，债务减1
	 *    - 检查滑动后的窗口：如果债务为0，添加新窗口的起始位置
	 * 
	 * 8. 窗口重置：
	 *    - 每组处理完成后，清空窗口哈希表，准备下一组
	 * 
	 * 算法核心思想深度解析：
	 * - 为什么同余分组有效？
	 *   任何有效的串联子串必须由完整的单词组成，因此起始位置i必须满足i ≡ r (mod wordLen)
	 *   因此只需要检查wordLen个可能的起始位置类型，而不是所有n个位置
	 * 
	 * - 债务计数机制如何工作？
	 *   debt变量精确跟踪了当前窗口中需要匹配的单词数量
	 *   当单词被加入窗口时，如果它满足目标词频约束（数量未超出），则债务减1
	 *   当单词被移出窗口时，如果它之前满足约束但现在不满足，则债务加1
	 *   这种机制避免了每次都需要比较两个哈希表的所有键值对
	 * 
	 * 示例执行流程：
	 * 对于s = "barfoothefoobarman", words = ["foo","bar"]
	 * - wordLen = 3, wordNum = 2, allLen = 6
	 * - 处理初始偏移量0：
	 *   - 初始窗口包含"bar"和"foo"，债务变为0，记录位置0
	 *   - 滑动窗口：移出"bar"，移入"the"，债务变为1
	 *   - 继续滑动：移出"foo"，移入"foo"，债务仍为1
	 *   - ...
	 * - 处理初始偏移量1：
	 *   - 窗口无法包含完整的单词组合，跳过
	 * - 处理初始偏移量2：
	 *   - 窗口无法包含完整的单词组合，跳过
	 * - 处理初始偏移量9（实际通过后续的滑动窗口处理）：
	 *   - 窗口包含"foo"和"bar"，债务变为0，记录位置9
	 * 
	 * 边界情况处理：
	 * - 空输入：直接返回空列表
	 * - 单词长度大于s长度：无法匹配，返回空列表
	 * - 所有单词连接后的长度大于s长度：无法匹配，返回空列表
	 * - words包含重复单词：正确处理，通过词频计数实现
	 * 
	 * @param s 输入字符串，可能包含任何字符
	 * @param words 单词数组，所有单词长度相同，非空
	 * @return 所有满足条件的起始索引列表，按升序排列
	 * 
	 * 时间复杂度：O(n + m)
	 * - 其中n是s的长度，m是words中所有单词的总长度
	 * - 预处理阶段：O(n + m)
	 * - 匹配阶段：O(n)，尽管有嵌套循环，但总操作次数与n成正比
	 * 
	 * 空间复杂度：O(k + n)
	 * - 其中k是words中不同单词的数量
	 * - 哈希表：O(k)
	 * - 哈希数组和幂次数组：O(n)
	 * - 结果列表：最坏情况下O(n)
	 */
	public static List<Integer> findSubstring(String s, String[] words) {
		List<Integer> ans = new ArrayList<>(); // 存储结果的列表
		
		// 处理边界情况：输入为空
		if (s == null || s.length() == 0 || words == null || words.length == 0) {
			return ans;
		}
		
		// 构建words的词频表，使用单词哈希值作为键
		// 这样可以避免字符串比较，提高效率
		HashMap<Long, Integer> map = new HashMap<>();
		for (String key : words) {
			long v = hash(key); // 计算每个单词的哈希值
			// 更新词频，存在则加1，不存在则设为1
			map.put(v, map.getOrDefault(v, 0) + 1);
		}
		
		// 预处理字符串s的哈希数组，用于快速计算子串哈希值
		build(s);
		
		// 关键参数计算
		int n = s.length();             // 输入字符串s的长度
		int wordLen = words[0].length(); // 每个单词的长度
		int wordNum = words.length;     // 单词的数量
		int allLen = wordLen * wordNum; // 所有单词连接后的总长度
		
		// 窗口的词频表，用于记录当前窗口内各单词的出现次数
		HashMap<Long, Integer> window = new HashMap<>();
		
		// 同余分组：将起始位置按单词长度分组处理
		// 例如：wordLen=3时，分为0,1,2三组，每组内的起始位置相差wordLen
		// 这样可以确保每个窗口内的单词边界是严格对齐的
		for (int init = 0; init < wordLen && init + allLen <= n; init++) {
			// debt表示还需要匹配的单词数量
			// 初始值为wordNum，当debt=0时表示所有单词都已匹配
			int debt = wordNum;
			
			// 初始化窗口：将前wordNum个单词加入窗口
			// l和r分别表示当前单词的左右边界（左闭右开）
			for (int l = init, r = init + wordLen, part = 0; part < wordNum; 
					l += wordLen, r += wordLen, part++) {
				// 计算当前单词的哈希值
				long cur = hash(l, r);
				
				// 更新窗口词频表
				window.put(cur, window.getOrDefault(cur, 0) + 1);
				
				// 关键逻辑：如果当前单词在目标词频表中且数量未超过目标，则债务减1
				// 这表示该单词已经被正确匹配
				if (window.get(cur) <= map.getOrDefault(cur, 0)) {
					debt--;
				}
			}
			
			// 检查初始窗口是否完全匹配
			if (debt == 0) {
				ans.add(init); // 记录匹配位置
			}
			
			// 滑动窗口：每次向右滑动一个单词位置
			// l1,r1：左侧要移出的单词边界
			// l2,r2：右侧要移入的单词边界
			for (int l1 = init, r1 = init + wordLen, l2 = init + allLen,
					r2 = init + allLen + wordLen; r2 <= n; 
					l1 += wordLen, r1 += wordLen, l2 += wordLen, r2 += wordLen) {
				// 步骤1：移出左侧单词
				long out = hash(l1, r1);
				// 更新词频
				window.put(out, window.get(out) - 1);
				// 关键逻辑：如果移出后导致该单词数量不足，则债务加1
				// 这表示该单词需要重新匹配
				if (window.get(out) < map.getOrDefault(out, 0)) {
					debt++;
				}
				
				// 步骤2：移入右侧单词
				long in = hash(l2, r2);
				// 更新词频
				window.put(in, window.getOrDefault(in, 0) + 1);
				// 关键逻辑：如果移入后该单词数量仍未超过目标，则债务减1
				// 这表示该单词已经被正确匹配
				if (window.get(in) <= map.getOrDefault(in, 0)) {
					debt--;
				}
				
				// 检查滑动后的窗口是否完全匹配
				if (debt == 0) {
					ans.add(r1); // r1是新窗口的起始位置
				}
			}
			
			// 清空窗口，准备处理下一组起始位置
			window.clear();
		}
		
		return ans;
	}

	/**
	 * 最大字符串长度
	 * 设置为10001，足够处理LeetCode题目的输入约束
	 */
	public static int MAXN = 10001;

	/**
	 * 哈希基数，选择499作为大质数以减少哈希冲突
	 * 质数作为基数的优势：分布更均匀，冲突概率更低
	 */
	public static int base = 499;

	/**
	 * 存储base的幂次，避免重复计算
	 * pow[i] = base^i，用于快速计算子串哈希值
	 */
	public static long[] pow = new long[MAXN];

	/**
	 * 存储字符串前缀哈希值
	 * hash[i]表示前i+1个字符的哈希值（即子串s[0...i]）
	 */
	public static long[] hash = new long[MAXN];

	/**
	 * 构建字符串的哈希数组和幂次数组
	 * <p>
	 * 这是多项式滚动哈希算法的关键预处理步骤，为后续O(1)时间的子串哈希查询奠定基础。
	 * 该方法同时完成两个重要的预处理任务：
	 * <p>
	 * 1. 幂次数组构建：
	 *    - 计算并存储base的0次幂到MAXN-1次幂
	 *    - 使用滚动计算避免重复计算base的幂次
	 *    - pow[i] = base^i，用于快速计算子串哈希时的权重
	 * <p>
	 * 2. 前缀哈希数组构建：
	 *    - 计算字符串str的前缀哈希值数组
	 *    - hash[i]表示子串str[0...i]的多项式哈希值
	 *    - 使用字符映射策略：'a'->1, 'b'->2, ..., 'z'->26
	 *    - 避免0值映射导致的哈希冲突问题
	 * <p>
	 * 数学原理详解：
	 * 多项式哈希函数定义：hash(s) = s[0] * base^(n-1) + s[1] * base^(n-2) + ... + s[n-1] * base^0
	 * 前缀哈希的递推公式：hash[i] = hash[i-1] * base + s[i]的映射值
	 * <p>
	 * 递推公式的数学证明：
	 * 假设hash[i-1] = s[0] * base^(i-1) + s[1] * base^(i-2) + ... + s[i-1] * base^0
	 * 则hash[i-1] * base = s[0] * base^i + s[1] * base^(i-1) + ... + s[i-1] * base^1
	 * 加上s[i]的映射值后：
	 * hash[i] = s[0] * base^i + s[1] * base^(i-1) + ... + s[i-1] * base^1 + s[i] * base^0
	 * 这正是前缀s[0...i]的多项式哈希值
	 * <p>
	 * 算法优化细节：
	 * - 预计算幂次数组：避免在计算子串哈希时重复计算base的幂次
	 * - 滚动哈希计算：将前缀哈希计算从O(n²)优化到O(n)
	 * - 固定大小数组：使用预定义的静态数组，避免动态内存分配
	 * - 字符映射优化：将字符映射到1-26而非0-25，避免多个零值字符导致的哈希冲突
	 * <p>
	 * 示例计算：
	 * 对于字符串"abc"，base=499
	 * pow数组：[1, 499, 249001, 124251499, ...]
	 * hash数组：
	 * hash[0] = 'a'-'a'+1 = 1
	 * hash[1] = 1 * 499 + ('b'-'a'+1) = 1 * 499 + 2 = 501
	 * hash[2] = 501 * 499 + ('c'-'a'+1) = 501 * 499 + 3 = 250002
	 * <p>
	 * 注意事项：
	 * - 该方法预计算到MAXN长度，确保足够处理任何可能的子串
	 * - 对于不同的输入字符串，会覆盖之前的哈希数组和幂次数组
	 * 
	 * @param str 输入字符串，将被预处理以支持O(1)时间的子串哈希查询
	 * 
	 * 时间复杂度：O(n + MAXN)
	 * - 计算幂次数组：O(MAXN)
	 * - 计算前缀哈希数组：O(n)，其中n是str的长度
	 * 
	 * 空间复杂度：O(MAXN)
	 * - 存储幂次数组和哈希数组：O(MAXN)
	 */
	public static void build(String str) {
		// 初始化幂次数组，pow[0] = 1（任何数的0次方都是1）
		pow[0] = 1;
		
		// 预计算所有可能需要的幂次值
		// 注意这里计算到MAXN，确保足够处理最长可能的子串
		for (int j = 1; j < MAXN; j++) {
			pow[j] = pow[j - 1] * base;
		}
		
		// 初始化哈希数组，第一个字符的哈希值
		// 将字符映射到1-26，避免0值导致的哈希冲突
		hash[0] = str.charAt(0) - 'a' + 1;
		
		// 滚动计算前缀哈希值
		// 哈希公式：hash[j] = hash[j-1] * base + str.charAt(j)的映射值
		// 这实现了多项式哈希函数
		for (int j = 1; j < str.length(); j++) {
			hash[j] = hash[j - 1] * base + str.charAt(j) - 'a' + 1;
		}
	}

/**
	 * 计算子串s[l,r)的哈希值（左闭右开区间）
	 * <p>
	 * 该方法是多项式滚动哈希算法的核心，实现了O(1)时间复杂度的任意子串哈希查询。
	 * 利用预处理好的前缀哈希数组和幂次数组，可以高效地计算任意子串的哈希值。
	 * <p>
	 * 详细数学推导：
	 * 假设我们已经预处理好了前缀哈希数组hash和幂次数组pow，
	 * 现在要计算子串s[l...r-1]的哈希值：
	 * <p>
	 * 1. hash[r-1]表示s[0...r-1]的哈希值：
	 *    hash[r-1] = s[0] * base^(r-1) + s[1] * base^(r-2) + ... + s[l-1] * base^(r-l) + 
	 *                s[l] * base^(r-l-1) + ... + s[r-1] * base^0
	 * <p>
	 * 2. hash[l-1]表示s[0...l-1]的哈希值：
	 *    hash[l-1] = s[0] * base^(l-1) + s[1] * base^(l-2) + ... + s[l-1] * base^0
	 * <p>
	 * 3. 将hash[l-1]乘以base^(r-l)，得到：
	 *    hash[l-1] * pow[r-l] = s[0] * base^(r-1) + s[1] * base^(r-2) + ... + s[l-1] * base^(r-l)
	 * <p>
	 * 4. 从hash[r-1]中减去这部分，得到：
	 *    hash[r-1] - hash[l-1] * pow[r-l] = s[l] * base^(r-l-1) + ... + s[r-1] * base^0
	 *    这正是子串s[l...r-1]的哈希值
	 * <p>
	 * 边界条件处理：
	 * - 当l=0时，表示从字符串开头开始的子串
	 * - 此时不需要减去任何前缀，直接返回hash[r-1]
	 * - 当l>0时，需要减去hash[l-1] * pow[r-l]以移除前缀影响
	 * <p>
	 * 实现细节：
	 * - 使用条件表达式l == 0 ? 0 : (hash[l-1] * pow[r-l])优雅处理边界情况
	 * - 计算过程简洁高效，仅包含常数次操作
	 * <p>
	 * 算法优势：
	 * - 时间复杂度：O(1)，不受子串长度影响
	 * - 空间复杂度：O(1)，仅使用常数级额外空间
	 * - 计算精确：完全按照多项式哈希函数的定义进行计算
	 * <p>
	 * 示例计算：
	 * 对于字符串"abcdef"，base=499
	 * 计算子串"cde"（l=2, r=5）的哈希值：
	 * hash[4] = a*499^4 + b*499^3 + c*499^2 + d*499^1 + e*499^0
	 * hash[1] = a*499^1 + b*499^0
	 * hash[1] * pow[3] = a*499^4 + b*499^3
	 * 子串哈希值 = hash[4] - hash[1] * pow[3] = c*499^2 + d*499^1 + e*499^0
	 * 这正是"cde"的多项式哈希值
	 * <p>
	 * <b>注意事项：</b>
	 * - 该实现未使用模运算，对于较长字符串可能导致数值溢出
	 * - 在实际应用中，建议添加模运算，如取模10^9+7等大质数
	 * - 由于哈希冲突可能发生，在哈希值相等后应进行字符串的实际比较以确保正确性
	 * - 该方法假设build(String)方法已经被调用过，哈希数组和幂次数组已初始化
	 * - 方法使用左闭右开区间表示，这与Java中常见的子串表示一致
	 * - 参数l和r必须满足0 <= l < r <= s.length()
	 * 
	 * @param l 子串起始位置（包含），0-based索引
	 * @param r 子串结束位置（不包含），0-based索引
	 * @return 子串s[l...r-1]的哈希值
	 * 
	 * 时间复杂度：O(1) - 常数时间操作
	 * 空间复杂度：O(1) - 无需额外空间
	 */
	public static long hash(int l, int r) {
		// 初始值为hash[r-1]（从0到r-1的前缀哈希值）
		long ans = hash[r - 1];
		
		// 当l>0时，需要减去0到l-1部分的影响
		// hash[l-1] * pow[r-l] 计算的是s[0...l-1]在hash[r-1]中的贡献
		ans -= l == 0 ? 0 : (hash[l - 1] * pow[r - l]);
		
		return ans;
	}

	/**
	 * 计算一个字符串的哈希值
	 * <p>
	 * 该方法直接计算给定字符串的多项式滚动哈希值，与前缀哈希数组使用相同的哈希函数。
	 * 主要用于单独计算words数组中每个单词的哈希值，以构建目标词频表。
	 * <p>
	 * 实现原理：
	 * - 使用与build(String)方法相同的多项式哈希函数
	 * - 采用相同的字符映射策略：'a'->1, 'b'->2, ..., 'z'->26
	 * - 通过滚动计算避免重复计算base的幂次
	 * <p>
	 * 算法步骤：
	 * 1. 边界检查：处理空字符串的特殊情况
	 * 2. 初始化哈希值：取第一个字符的映射值
	 * 3. 滚动计算：从第二个字符开始，依次计算哈希值
	 *    公式：ans = ans * base + s[i]的映射值
	 * <p>
	 * 数学解释：
	 * 对于字符串s = s[0]s[1]...s[n-1]，其哈希值计算如下：
	 * hash(s) = s[0] * base^(n-1) + s[1] * base^(n-2) + ... + s[n-1] * base^0
	 * 通过滚动计算可以高效实现：
	 * ans = ( ... ((s[0]) * base + s[1]) * base + ... ) * base + s[n-1]
	 * <p>
	 * 示例计算：
	 * 对于字符串"bar"，base=499
	 * hash = ('b'-'a'+1) * 499^2 + ('a'-'a'+1) * 499 + ('r'-'a'+1)
	 *      = 2 * 499^2 + 1 * 499 + 18
	 *      = 2 * 249001 + 499 + 18
	 *      = 498002 + 499 + 18
	 *      = 498519
	 * <p>
	 * 与前缀哈希的一致性：
	 * 该方法计算的哈希值与通过build和hash(l,r)方法计算的结果完全一致。
	 * 例如，对于字符串s和其前缀s[0...n-1]，两种方法计算的哈希值相同。
	 * <p>
	 * 设计考量：
	 * - 单独实现该方法避免了为每个单词构建完整前缀哈希数组的开销
	 * - 适用于需要计算少量独立字符串哈希值的场景
	 * - 保持了与前缀哈希的算法一致性，确保哈希计算的正确性
	 * 
	 * @param str 输入字符串，将计算其哈希值
	 * @return 字符串的多项式滚动哈希值
	 * 
	 * 时间复杂度：O(n)，其中n是字符串的长度
	 * 空间复杂度：O(1)，仅使用常数级额外空间
	 */
	public static long hash(String str) {
		// 处理空字符串的边界情况
		if (str.equals("")) {
			return 0;
		}
		
		int n = str.length();
		// 初始化哈希值，将第一个字符映射到1-26
		long ans = str.charAt(0) - 'a' + 1;
		
		// 滚动计算哈希值
		for (int j = 1; j < n; j++) {
			ans = ans * base + str.charAt(j) - 'a' + 1;
		}
		
		return ans;
	}

	/**
	 * 哈希冲突概率的数学分析
	 * <p>
	 * 在多项式滚动哈希中，哈希冲突是不可避免的。冲突概率受以下因素影响：
	 * <ol>
	 * <li><b>基数选择(base)</b>：本实现选择499作为基数
	 * <li><b>模数选择</b>：本实现未使用模数，但生产环境应使用大质数模数
	 * <li><b>哈希空间大小</b>：对于long类型(64位)，理论哈希空间为2^64
	 * </ol>
	 * 
	 * <p>
	 * <b>生日悖论应用：</b>
	 * 当有m个不同的字符串时，冲突概率可近似为1 - e^(-m²/(2*H))，
	 * 其中H为哈希空间大小。对于64位哈希空间：
	 * <ul>
	 * <li>当m=10^6时，冲突概率约为1.1×10^-13
	 * <li>当m=10^7时，冲突概率约为1.1×10^-11
	 * <li>当m=10^8时，冲突概率约为1.1×10^-9
	 * <li>当m=10^9时，冲突概率约为1.1×10^-7
	 * </ul>
	 * 对于大多数应用场景，64位无符号整数哈希空间提供了足够低的冲突概率。
	 * 
	 * <p>
	 * <b>安全界限：</b>
	 * 理论上，当m超过约2^32时，哈希冲突概率将超过50%。
	 * 在实际开发中，为提高安全性，通常使用双哈希或多哈希策略。
	 */

	/**
	 * 双哈希实现示例
	 * <p>
	 * 为进一步降低哈希冲突的概率，可以实现双哈希策略：
	 * <pre>
	 * // 定义两组哈希参数
	 * public static int base1 = 499;
	 * public static int mod1 = 1000000007;
	 * public static int base2 = 1009;
	 * public static int mod2 = 1000000009;
	 * 
	 * // 定义两组哈希数组和幂次数组
	 * public static long[] pow1 = new long[MAXN];
	 * public static long[] hash1 = new long[MAXN];
	 * public static long[] pow2 = new long[MAXN];
	 * public static long[] hash2 = new long[MAXN];
	 * 
	 * // 构建双哈希
	 * public static void buildDoubleHash(String str) {
	 *     // 第一组哈希构建
	 *     pow1[0] = 1;
	 *     for (int j = 1; j < MAXN; j++) {
	 *         pow1[j] = (pow1[j - 1] * base1) % mod1;
	 *     }
	 *     hash1[0] = (str.charAt(0) - 'a' + 1) % mod1;
	 *     for (int j = 1; j < str.length(); j++) {
	 *         hash1[j] = (hash1[j - 1] * base1 + str.charAt(j) - 'a' + 1) % mod1;
	 *     }
	 *     
	 *     // 第二组哈希构建
	 *     pow2[0] = 1;
	 *     for (int j = 1; j < MAXN; j++) {
	 *         pow2[j] = (pow2[j - 1] * base2) % mod2;
	 *     }
	 *     hash2[0] = (str.charAt(0) - 'a' + 1) % mod2;
	 *     for (int j = 1; j < str.length(); j++) {
	 *         hash2[j] = (hash2[j - 1] * base2 + str.charAt(j) - 'a' + 1) % mod2;
	 *     }
	 * }
	 * 
	 * // 计算子串的双哈希值
	 * public static Pair<Long, Long> doubleHash(int l, int r) {
	 *     // 计算第一组哈希
	 *     long h1 = hash1[r - 1];
	 *     if (l > 0) {
	 *         h1 = (h1 - hash1[l - 1] * pow1[r - l] % mod1 + mod1) % mod1;
	 *     }
	 *     
	 *     // 计算第二组哈希
	 *     long h2 = hash2[r - 1];
	 *     if (l > 0) {
	 *         h2 = (h2 - hash2[l - 1] * pow2[r - l] % mod2 + mod2) % mod2;
	 *     }
	 *     
	 *     return new Pair<>(h1, h2);
	 * }
	 * 
	 * // 计算字符串的双哈希值
	 * public static Pair<Long, Long> doubleHash(String str) {
	 *     if (str.equals("")) {
	 *         return new Pair<>(0L, 0L);
	 *     }
	 *     
	 *     int n = str.length();
	 *     long h1 = (str.charAt(0) - 'a' + 1) % mod1;
	 *     long h2 = (str.charAt(0) - 'a' + 1) % mod2;
	 *     
	 *     for (int j = 1; j < n; j++) {
	 *         h1 = (h1 * base1 + str.charAt(j) - 'a' + 1) % mod1;
	 *         h2 = (h2 * base2 + str.charAt(j) - 'a' + 1) % mod2;
	 *     }
	 *     
	 *     return new Pair<>(h1, h2);
	 * }
	 * </pre>
	 * 
	 * 双哈希可以将冲突概率降至接近零，因为两组独立的哈希同时冲突的概率极低。
	 * 在实际应用中，应使用Pair类或自定义类来存储双重哈希值。
	 */

	/**
	 * 推荐测试用例实现
	 * <p>
	 * 以下是针对该算法的JUnit测试用例示例：
	 * <pre>
	 * import org.junit.Test;
	 * import static org.junit.Assert.*;
	 * import java.util.List;
	 * 
	 * public class Code05_ConcatenationAllWordsTest {
	 *     
	 *     @Test
	 *     public void testBasicCases() {
	 *         // 基本匹配测试
	 *         String s1 = "barfoothefoobarman";
	 *         String[] words1 = {"foo", "bar"};
	 *         List<Integer> expected1 = List.of(0, 9);
	 *         assertEquals(expected1, Code05_ConcatenationAllWords.findSubstring(s1, words1));
	 *         
	 *         // 无匹配情况
	 *         String s2 = "wordgoodgoodgoodbestword";
	 *         String[] words2 = {"word", "good", "best", "word"};
	 *         List<Integer> expected2 = List.of();
	 *         assertEquals(expected2, Code05_ConcatenationAllWords.findSubstring(s2, words2));
	 *     }
	 *     
	 *     @Test
	 *     public void testComplexCases() {
	 *         // 重复单词测试
	 *         String s3 = "barfoofoobarthefoobarman";
	 *         String[] words3 = {"bar", "foo", "the"};
	 *         List<Integer> expected3 = List.of(6, 9, 12);
	 *         assertEquals(expected3, Code05_ConcatenationAllWords.findSubstring(s3, words3));
	 *     }
	 *     
	 *     @Test
	 *     public void testEdgeCases() {
	 *         // 边界情况测试
	 *         String s4 = "a";
	 *         String[] words4 = {"a"};
	 *         List<Integer> expected4 = List.of(0);
	 *         assertEquals(expected4, Code05_ConcatenationAllWords.findSubstring(s4, words4));
	 *         
	 *         // 空输入测试
	 *         String s5 = "";
	 *         String[] words5 = {"a"};
	 *         List<Integer> expected5 = List.of();
	 *         assertEquals(expected5, Code05_ConcatenationAllWords.findSubstring(s5, words5));
	 *     }
	 *     
	 *     @Test
	 *     public void testSingleWord() {
	 *         // 单个单词测试
	 *         String s6 = "aaa";
	 *         String[] words6 = {"a", "a"};
	 *         List<Integer> expected6 = List.of(0, 1);
	 *         assertEquals(expected6, Code05_ConcatenationAllWords.findSubstring(s6, words6));
	 *     }
	 *     
	 *     @Test
	 *     public void testHashCollision() {
	 *         // 哈希冲突测试（需要设计可能导致冲突的测试用例）
	 *         // 此处仅为示例，实际测试需根据具体哈希实现设计
	 *         // 在生产环境中，应添加更多测试用例以验证算法在各种情况下的正确性
	 *     }
	 * }
	 * </pre>
	 * 
	 * 这些测试用例涵盖了基本功能测试、复杂匹配测试、边界情况测试、重复单词测试等多种情况，
	 * 有助于确保算法在各种情况下的正确性和性能。
	 */

	/**
	 * 字符串哈希算法比较分析
	 * <p>
	 * <b>多项式滚动哈希 vs 其他哈希算法：</b>
	 * <table border="1">
	 * <tr><th>算法类型</th><th>优点</th><th>缺点</th><th>适用场景</th></tr>
	 * <tr>
	 * <td>多项式滚动哈希</td>
	 * <td>
	 * - 实现简单<br>
	 * - 支持O(1)子串哈希计算<br>
	 * - 高效的滑动窗口匹配<br>
	 * - 预处理时间O(n)
	 * </td>
	 * <td>
	 * - 可能存在哈希冲突<br>
	 * - 需要选择合适的基数和模数<br>
	 * - 对长字符串可能溢出
	 * </td>
	 * <td>
	 * - 子串搜索<br>
	 * - 重复字符串检测<br>
	 * - 滑动窗口问题<br>
	 * - 单词串联匹配（本题）
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>MD5/SHA-1</td>
	 * <td>
	 * - 极低的冲突概率<br>
	 * - 安全哈希算法<br>
	 * - 标准化实现
	 * </td>
	 * <td>
	 * - 计算开销大<br>
	 * - 不支持O(1)子串哈希<br>
	 * - 性能较低
	 * </td>
	 * <td>
	 * - 数据完整性校验<br>
	 * - 密码存储（使用盐值）<br>
	 * - 数字签名
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>Rabin-Karp算法</td>
	 * <td>
	 * - 滑动窗口O(n)时间复杂度<br>
	 * - 适合多模式匹配<br>
	 * - 易于实现
	 * </td>
	 * <td>
	 * - 最差情况下退化为O(n*m)<br>
	 * - 需要处理哈希冲突<br>
	 * </td>
	 * <td>
	 * - 字符串搜索<br>
	 * - 多模式匹配<br>
	 * - 重复检测
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>xxHash/MurmurHash</td>
	 * <td>
	 * - 极快的计算速度<br>
	 * - 良好的分布特性<br>
	 * - 适合大规模数据处理
	 * </td>
	 * <td>
	 * - 不支持O(1)子串哈希<br>
	 * - 需额外实现子串查询功能
	 * </td>
	 * <td>
	 * - 大规模哈希表<br>
	 * - 数据索引<br>
	 * - 高速缓存
	 * </td>
	 * </tr>
	 * </table>
	 * 
	 * <p>
	 * <b>在本问题中的选择理由：</b>
	 * 对于"串联所有单词的子串"问题，多项式滚动哈希是理想选择，因为：
	 * <ol>
	 * <li>需要高效地计算固定长度子串的哈希值以进行单词匹配</li>
	 * <li>滑动窗口技术与滚动哈希完美结合，支持快速窗口更新</li>
	 * <li>同余分组策略与哈希技术协同工作，大幅减少不必要的计算</li>
	 * <li>债务计数机制与哈希值比较结合，实现高效的词频验证</li>
	 * </ol>
	 * 
	 * <p>
	 * <b>生产环境优化建议：</b>
	 * 1. 添加模数运算以避免数值溢出，推荐使用10^9+7或10^9+9等大质数
	 * 2. 实现双哈希策略以进一步降低冲突概率，特别是在处理大量数据时
	 * 3. 在哈希值相等后进行字符串的实际比较以确保正确性
	 * 4. 使用long类型存储哈希值以提供更大的哈希空间
	 * 5. 考虑使用并行计算技术处理同余分组，进一步提高性能
	 */

}
