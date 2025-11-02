package class119;

import java.util.List;

// 在传球游戏中最大化函数值问题
// 问题描述：
// 给定一个长度为n的数组receiver和一个整数k
// 总共有n名玩家，编号0 ~ n-1，这些玩家在玩一个传球游戏
// receiver[i]表示编号为i的玩家会传球给下一个人的编号
// 玩家可以传球给自己，也就是说receiver[i]可能等于i
// 你需要选择一名开始玩家，然后开始传球，球会被传恰好k次
// 如果选择编号为x的玩家作为开始玩家
// 函数f(x)表示从x玩家开始，k次传球内所有接触过球的玩家编号之和
// 如果某位玩家多次触球，则累加多次
// f(x) = x + receiver[x] + receiver[receiver[x]] + ... 
// 你的任务时选择开始玩家x，目的是最大化f(x)，返回函数的最大值
// 测试链接 : https://leetcode.cn/problems/maximize-value-of-function-in-a-ball-passing-game/
// 
// 解题思路：
// 使用树上倍增算法预处理每个节点跳2^i步能到达的位置和路径和
// 然后通过二进制分解计算k步后的结果
// 对于每个起始点，计算k次传球后经过的所有玩家编号之和，找出最大值

public class Code04_PassingBallMaximizeValue {

	// 最大节点数
	public static int MAXN = 100001;

	// 最大跳跃级别
	public static int LIMIT = 34;

	// 实际使用的最大跳跃级别
	public static int power;

	// 给定k的二进制位上有几个1
	public static int m;

	// 收集k的二进制上哪些位有1
	public static int[] kbits = new int[LIMIT];

	// stjump[i][j] 表示从节点i开始跳2^j步能到达的节点
	public static int[][] stjump = new int[MAXN][LIMIT];

	// stsum[i][j] 表示从节点i开始跳2^j步经过的节点编号之和
	public static long[][] stsum = new long[MAXN][LIMIT];

	/**
	 * 预处理k的二进制表示和相关参数
	 * @param k 传球次数
	 */
	public static void build(long k) {
		// 计算k的最高位
		power = 0;
		while ((1L << power) <= (k >> 1)) {
			power++;
		}
		m = 0;
		// 收集k的二进制表示中为1的位
		for (int p = power; p >= 0; p--) {
			if ((1L << p) <= k) {
				kbits[m++] = p;
				k -= 1L << p;
			}
		}
	}

	/**
	 * 使用树上倍增算法计算传球游戏的最大值
	 * 算法思路：
	 * 1. 预处理每个节点跳2^i步能到达的位置和路径和
	 * 2. 对每个起始点，通过二进制分解计算k步后的结果
	 * 3. 找到最大值
	 * 
	 * 时间复杂度：O(n log k + n log k) = O(n log k)
	 * 空间复杂度：O(n log k)
	 * 
	 * 注意：这是树上倍增的解法，虽然时间复杂度不是最优的，但非常好理解和实现
	 * 最优解来自对基环树的分析，后续课程会安排相关内容
	 * 
	 * @param receiver 传球规则数组，receiver[i]表示i传给谁
	 * @param k 传球次数
	 * @return 函数f(x)的最大值
	 */
	public static long getMaxFunctionValue(List<Integer> receiver, long k) {
		// 预处理k的二进制表示
		build(k);
		int n = receiver.size();
		// 初始化跳1步的信息
		for (int i = 0; i < n; i++) {
			stjump[i][0] = receiver.get(i);
			stsum[i][0] = receiver.get(i);
		}
		// 倍增预处理
		// stjump[i][p] 表示从节点i跳2^p步到达的节点
		// stsum[i][p] 表示从节点i跳2^p步经过的节点编号之和
		for (int p = 1; p <= power; p++) {
			for (int i = 0; i < n; i++) {
				// 跳2^p步 = 跳2^(p-1)步后再跳2^(p-1)步
				stjump[i][p] = stjump[stjump[i][p - 1]][p - 1];
				// 路径和 = 前半段路径和 + 后半段路径和
				stsum[i][p] = stsum[i][p - 1] + stsum[stjump[i][p - 1]][p - 1];
			}
		}
		long sum, ans = 0;
		// 枚举每个起始点
		for (int i = 0, cur; i < n; i++) {
			cur = i;
			// 起始点自己也算在内
			sum = i;
			// 通过二进制分解计算k步后的结果
			// 将k分解为2的幂次之和，然后依次跳跃
			for (int j = 0; j < m; j++) {
				// 累加路径和
				sum += stsum[cur][kbits[j]];
				// 更新当前位置
				cur = stjump[cur][kbits[j]];
			}
			// 更新最大值
			ans = Math.max(ans, sum);
		}
		return ans;
	}

}