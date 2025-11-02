package class083;

// 自由之路 (Freedom Trail)
// 题目描述比较多，打开链接查看
// 测试链接 : https://leetcode.cn/problems/freedom-trail/
// 
// 相关题目链接:
// LeetCode 1423. 可获得的最大点数: https://leetcode.cn/problems/maximum-points-you-can-obtain-from-cards/
// LeetCode 134. 加油站: https://leetcode.cn/problems/gas-station/
// LeetCode 213. 打家劫舍 II: https://leetcode.cn/problems/house-robber-ii/
// LeetCode 503. 下一个更大元素 II: https://leetcode.cn/problems/next-greater-element-ii/
// 洛谷 P1880. [NOI1995] 石子合并: https://www.luogu.com.cn/problem/P1880
// 
// 核心算法: 记忆化搜索 / 动态规划
// 时间复杂度: O(mn²) - 其中m是key长度，n是ring长度
// 空间复杂度: O(mn) - DP数组和预处理数组
// 工程化考量: 字符索引预处理、记忆化优化、边界条件处理
// 
// 解题思路:
// 1. 预处理字符索引，记录每个字符在环中的位置
// 2. 使用记忆化搜索，dp[i][j]表示指针在环位置i，需要搞定key[j...]的最小代价
// 3. 对于每个状态，考虑顺时针和逆时针两种移动方式
// 4. 选择代价较小的方案进行状态转移
public class Code03_FreedomTrail {

	// 为了让所有语言的同学都可以理解
	// 不会使用任何java语言自带的数据结构
	// 只使用最简单的数组结构
	
	// 最大环长度常量
	public static int MAXN = 101;

	// 字符集大小常量
	public static int MAXC = 26;

	// 环数组，存储ring中每个位置的字符（转换为0-25的数字）
	public static int[] ring = new int[MAXN];

	// 目标键数组，存储key中每个位置的字符（转换为0-25的数字）
	public static int[] key = new int[MAXN];

	// 每个字符在环中出现的次数
	public static int[] size = new int[MAXC];

	// where[c][i]表示字符c在环中第i次出现的位置
	public static int[][] where = new int[MAXC][MAXN];

	// 记忆化搜索DP数组，dp[i][j]表示指针在环位置i，需要搞定key[j...]的最小代价
	public static int[][] dp = new int[MAXN][MAXN];

	// 环长度和键长度
	public static int n, m;

	// 预处理函数：构建字符索引和初始化DP数组
	// r: 环字符串
	// k: 目标键字符串
	public static void build(String r, String k) {
		// 初始化每个字符的出现次数为0
		for (int i = 0; i < MAXC; i++) {
			size[i] = 0;
		}
		// 获取环和键的长度
		n = r.length();
		m = k.length();
		// 构建环字符索引：记录每个字符在环中的位置
		for (int i = 0, v; i < n; i++) {
			v = r.charAt(i) - 'a';
			where[v][size[v]++] = i;
			ring[i] = v;
		}
		// 构建键字符数组
		for (int i = 0; i < m; i++) {
			key[i] = k.charAt(i) - 'a';
		}
		// 初始化DP数组为-1，表示未计算
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				dp[i][j] = -1;
			}
		}
	}

	// 主函数：计算拼写关键词所需的最小步数
	// r: 环字符串
	// k: 目标键字符串
	// 返回值: 拼写关键词所需的最小步数
	public static int findRotateSteps(String r, String k) {
		// 预处理
		build(r, k);
		// 从环位置0开始，搞定key[0...]的所有字符
		return f(0, 0);
	}

	// 记忆化搜索函数：计算指针在环位置i，搞定key[j...]所需最小代价
	// i: 当前指针在环上的位置
	// j: 当前需要搞定的key中字符的索引
	// 返回值: 搞定key[j...]所需最小代价
	public static int f(int i, int j) {
		// 递归终止条件：所有字符都已搞定
		if (j == m) {
			// key长度是m
			// 都搞定
			return 0;
		}
		// 记忆化优化：如果已计算过直接返回
		if (dp[i][j] != -1) {
			return dp[i][j];
		}
		int ans;
		// 如果当前环位置字符与目标字符相同
		if (ring[i] == key[j]) {
			// ring b
			//      i
			// key  b
			//      j
			// 只需按下按钮（1步），然后搞定key[j+1...]的所有字符
			ans = 1 + f(i, j + 1);
		} else {
			// 轮盘处在i位置，ring[i] != key[j]
			// jump1 : 顺时针找到最近的key[j]字符在轮盘的什么位置
			// distance1 : 从i顺时针走向jump1有多远
			int jump1 = clock(i, key[j]);
			int distance1 = (jump1 > i ? (jump1 - i) : (n - i + jump1));
			// jump2 : 逆时针找到最近的key[j]字符在轮盘的什么位置
			// distance2 : 从i逆时针走向jump2有多远
			int jump2 = counterClock(i, key[j]);
			int distance2 = (i > jump2 ? (i - jump2) : (i + n - jump2));
			// 选择顺时针或逆时针中代价较小的方案
			ans = Math.min(distance1 + f(jump1, j), distance2 + f(jump2, j));
		}
		// 记录结果并返回
		dp[i][j] = ans;
		return ans;
	}

	// 从i开始，顺时针找到最近的v在轮盘的什么位置
	// i: 当前位置
	// v: 目标字符（转换为0-25的数字）
	// 返回值: 顺时针找到的最近的字符v的位置
	public static int clock(int i, int v) {
		int l = 0;
		// size[v] : 属于v这个字符的下标有几个
		int r = size[v] - 1, m;
		// sorted[0...size[v]-1]收集了所有的下标，并且有序
		int[] sorted = where[v];
		int find = -1;
		// 有序数组中，找>i尽量靠左的下标
		while (l <= r) {
			m = (l + r) / 2;
			if (sorted[m] > i) {
				find = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		// 找到了就返回
		// 没找到，那i顺指针一定先走到最小的下标
		return find != -1 ? sorted[find] : sorted[0];
	}

	// 从i开始，逆时针找到最近的v在轮盘的什么位置
	// i: 当前位置
	// v: 目标字符（转换为0-25的数字）
	// 返回值: 逆时针找到的最近的字符v的位置
	public static int counterClock(int i, int v) {
		int l = 0;
		int r = size[v] - 1, m;
		int[] sorted = where[v];
		int find = -1;
		// 有序数组中，找<i尽量靠右的下标
		while (l <= r) {
			m = (l + r) / 2;
			if (sorted[m] < i) {
				find = m;
				l = m + 1;
			} else {
				r = m - 1;
			}
		}
		// 找到了就返回
		// 没找到，那i逆指针一定先走到最大的下标
		return find != -1 ? sorted[find] : sorted[size[v] - 1];
	}

}