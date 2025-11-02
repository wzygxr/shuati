package class045;

import java.util.HashSet;

/**
 * LeetCode 421. 数组中两个数的最大异或值
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0<=i<=j<=n
 * 
 * 约束条件：
 * - 1 <= nums.length <= 2 * 10^5
 * - 0 <= nums[i] <= 2^31 - 1
 * 
 * 测试链接：https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
 * 
 * 算法思路：
 * 1. 使用前缀树（Trie）存储所有数字的二进制表示
 * 2. 对于每个数字，在前缀树中贪心地查找能产生最大异或值的数字
 * 3. 异或运算的性质：相同为0，不同为1，因此要使异或结果最大，应尽量使对应位不同
 * 
 * 时间复杂度分析：
 * - 构建前缀树：O(n * log(max))，其中n是数组长度，max是数组中的最大值
 * - 查询过程：O(n * log(max))
 * - 总体时间复杂度：O(n * log(max))
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(n * log(max))，用于存储所有数字的二进制表示
 * - 总体空间复杂度：O(n * log(max))
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以在线性时间内查找最大异或值，避免了暴力枚举O(n^2)
 * 
 * 工程化考虑：
 * 1. 异常处理：输入为空或数组长度小于2的情况
 * 2. 边界情况：数组中所有数字相同的情况
 * 3. 极端输入：大量数字或数字很大的情况
 * 4. 鲁棒性：处理负数和0的情况
 * 
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，利用位运算提高效率
 * C++：可使用指针实现前缀树节点，更节省空间
 * Python：可使用字典实现前缀树，代码更简洁
 * 
 * 相关题目扩展：
 * 1. LeetCode 421. 数组中两个数的最大异或值 (本题)
 * 2. LeetCode 1310. 子数组异或查询
 * 3. LeetCode 1707. 与数组中元素的最大异或值
 * 4. LeetCode 1803. 统计异或值在范围内的数对有多少
 * 5. LintCode 1490. 最大异或值
 * 6. 牛客网 NC152. 数组中两个数的最大异或值
 * 7. HackerRank - XOR Maximization
 * 8. CodeChef - MAXXOR
 * 9. SPOJ - XORX
 * 10. AtCoder - Maximum XOR
 */
public class Code02_TwoNumbersMaximumXor {

	/**
	 * 使用前缀树查找最大异或值
	 * 
	 * 算法步骤详解：
	 * 1. 构建前缀树：
	 *    a. 找到数组中的最大值，确定需要考虑的二进制位数
	 *    b. 将所有数字的二进制表示插入前缀树
	 * 2. 对于每个数字，在前缀树中查找能产生最大异或值的数字：
	 *    a. 从最高位开始，贪心地选择能使异或结果最大的路径
	 *    b. 如果期望的路径存在，则选择该路径；否则选择另一条路径
	 * 3. 返回所有数字对中异或值的最大值
	 * 
	 * 贪心策略原理：
	 * 异或运算的性质是相同为0，不同为1。要使异或结果最大，
	 * 应该从高位到低位尽量使对应位不同。
	 * 
	 * 时间复杂度分析：
	 * - 构建前缀树：O(n * log(max))，其中n是数组长度，max是数组中的最大值
	 * - 查询过程：O(n * log(max))
	 * - 总体时间复杂度：O(n * log(max))
	 * 
	 * 空间复杂度分析：
	 * - 前缀树空间：O(n * log(max))，用于存储所有数字的二进制表示
	 * - 总体空间复杂度：O(n * log(max))
	 * 
	 * 是否最优解：是
	 * 理由：使用前缀树可以在线性时间内查找最大异或值，避免了暴力枚举O(n^2)
	 * 
	 * 工程化考虑：
	 * 1. 异常处理：输入为空或数组长度小于2的情况
	 * 2. 边界情况：数组中所有数字相同的情况
	 * 3. 极端输入：大量数字或数字很大的情况
	 * 4. 鲁棒性：处理负数和0的情况
	 * 
	 * 语言特性差异：
	 * Java：使用二维数组实现前缀树，利用位运算提高效率
	 * C++：可使用指针实现前缀树节点，更节省空间
	 * Python：可使用字典实现前缀树，代码更简洁
	 * 
	 * @param nums 整数数组
	 * @return 最大异或值
	 */
	// 前缀树的做法
	// 好想
	public static int findMaximumXOR1(int[] nums) {
		build(nums);
		int ans = 0;
		for (int num : nums) {
			ans = Math.max(ans, maxXor(num));
		}
		clear();
		return ans;
	}

	// 准备这么多静态空间就够了，实验出来的
	// 如果测试数据升级了规模，就改大这个值
	public static int MAXN = 3000001;

	public static int[][] tree = new int[MAXN][2];

	// 前缀树目前使用了多少空间
	public static int cnt;

	// 数字只需要从哪一位开始考虑
	public static int high;

	/**
	 * 构建前缀树
	 * 
	 * 算法步骤：
	 * 1. 找到数组中的最大值
	 * 2. 计算最大值的二进制表示中最高有效位的位置
	 * 3. 从该位开始，将所有数字的二进制表示插入前缀树
	 * 
	 * 优化策略：
	 * 忽略前导0位，只从最高有效位开始考虑，减少不必要的计算
	 * 
	 * 时间复杂度：O(n * log(max))，其中n是数组长度，max是数组中的最大值
	 * 空间复杂度：O(n * log(max))
	 * 
	 * @param nums 整数数组
	 */
	public static void build(int[] nums) {
		cnt = 1;
		// 找个最大值
		int max = Integer.MIN_VALUE;
		for (int num : nums) {
			max = Math.max(num, max);
		}
		// 计算数组最大值的二进制状态，有多少个前缀的0
		// 可以忽略这些前置的0，从left位开始考虑
		high = 31 - Integer.numberOfLeadingZeros(max);
		for (int num : nums) {
			insert(num);
		}
	}

	/**
	 * 向前缀树中插入数字
	 * 
	 * 算法步骤：
	 * 1. 从最高有效位开始，逐位处理数字的二进制表示
	 * 2. 对于每一位：
	 *    a. 计算该位的值（0或1）
	 *    b. 如果对应的子节点不存在，则创建新节点
	 *    c. 移动到子节点
	 * 3. 插入完成后，数字的二进制表示已存储在前缀树中
	 * 
	 * 时间复杂度：O(log(max))，其中max是数组中的最大值
	 * 空间复杂度：O(log(max))，最坏情况下需要创建新节点
	 * 
	 * @param num 待插入的数字
	 */
	public static void insert(int num) {
		int cur = 1;
		for (int i = high, path; i >= 0; i--) {
			path = (num >> i) & 1;
			if (tree[cur][path] == 0) {
				tree[cur][path] = ++cnt;
			}
			cur = tree[cur][path];
		}
	}

	/**
	 * 查找与num异或能得到最大值的数字
	 * 
	 * 算法步骤：
	 * 1. 从最高有效位开始，逐位处理数字的二进制表示
	 * 2. 对于每一位：
	 *    a. 获取当前数字该位的值
	 *    b. 计算期望的相反值（使异或结果为1）
	 *    c. 如果前缀树中存在该路径，则选择该路径
	 *    d. 否则选择另一条路径
	 *    e. 更新异或结果
	 * 3. 返回最大异或值
	 * 
	 * 贪心策略：
	 * 尽量选择能使异或结果为1的路径，从高位到低位贪心选择
	 * 
	 * 时间复杂度：O(log(max))，其中max是数组中的最大值
	 * 空间复杂度：O(1)
	 * 
	 * @param num 待查询的数字
	 * @return 最大异或值
	 */
	public static int maxXor(int num) {
		// 最终异或的结果(尽量大)
		int ans = 0;
		// 前缀树目前来到的节点编号
		int cur = 1;
		for (int i = high, status, want; i >= 0; i--) {
			// status : num第i位的状态
			status = (num >> i) & 1;
			// want : num第i位希望遇到的状态
			want = status ^ 1;
			if (tree[cur][want] == 0) { // 询问前缀树，能不能达成
				// 不能达成
				want ^= 1;
			}
			// want变成真的往下走的路
			ans |= (status ^ want) << i;
			cur = tree[cur][want];
		}
		return ans;
	}

	/**
	 * 清空前缀树
	 * 
	 * 算法步骤：
	 * 1. 遍历所有已使用的节点
	 * 2. 将每个节点的子节点数组清零
	 * 
	 * 时间复杂度：O(cnt)，其中cnt是使用的节点数量
	 * 空间复杂度：O(1)
	 */
	public static void clear() {
		for (int i = 1; i <= cnt; i++) {
			tree[i][0] = tree[i][1] = 0;
		}
	}

	/**
	 * 使用哈希表查找最大异或值
	 * 
	 * 算法步骤详解：
	 * 1. 从最高位开始，逐位构建最大异或值
	 * 2. 对于每一位：
	 *    a. 尝试将该位设为1，形成期望的目标值
	 *    b. 将所有数字右移i位后存入哈希表
	 *    c. 检查是否存在两个数字异或能得到目标值
	 *    d. 如果存在，则更新结果；否则该位为0
	 * 3. 返回最大异或值
	 * 
	 * 核心思想：
	 * 利用异或运算的性质：如果a ^ b = c，则a ^ c = b
	 * 因此，对于目标值target，如果存在a ^ b = target，
	 * 则必有target ^ a = b，即target ^ a应在哈希表中
	 * 
	 * 时间复杂度分析：
	 * - 查询过程：O(n * log(max))，其中n是数组长度，max是数组中的最大值
	 * - 总体时间复杂度：O(n * log(max))
	 * 
	 * 空间复杂度分析：
	 * - 哈希表空间：O(n)，用于存储数字
	 * - 总体空间复杂度：O(n)
	 * 
	 * 是否最优解：是
	 * 理由：使用哈希表可以在线性时间内查找最大异或值，避免了构建前缀树
	 * 
	 * 工程化考虑：
	 * 1. 异常处理：输入为空或数组长度小于2的情况
	 * 2. 边界情况：数组中所有数字相同的情况
	 * 3. 极端输入：大量数字或数字很大的情况
	 * 4. 鲁棒性：处理负数和0的情况
	 * 
	 * 语言特性差异：
	 * Java：使用HashSet存储数字，利用位运算提高效率
	 * C++：可使用unordered_set存储数字，性能更优
	 * Python：可使用set存储数字，代码更简洁
	 * 
	 * @param nums 整数数组
	 * @return 最大异或值
	 */
	// 用哈希表的做法
	// 难想
	public int findMaximumXOR2(int[] nums) {
		int max = Integer.MIN_VALUE;
		for (int num : nums) {
			max = Math.max(num, max);
		}
		int ans = 0;
		HashSet<Integer> set = new HashSet<>();
		for (int i = 31 - Integer.numberOfLeadingZeros(max); i >= 0; i--) {
			// ans : 31....i+1 已经达成的目标
			int better = ans | (1 << i);
			set.clear();
			for (int num : nums) {
				// num : 31.....i 这些状态保留，剩下全成0
				num = (num >> i) << i;
				set.add(num);
				// num ^ 某状态 是否能 达成better目标，就在set中找 某状态 : better ^ num
				if (set.contains(better ^ num)) {
					ans = better;
					break;
				}
			}
		}
		return ans;
	}

}