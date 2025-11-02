package class074;

// 购买足量干草的最小花费
// 有n个提供干草的公司，每个公司都有两个信息
// cost[i]代表购买1次产品需要花的钱
// val[i]代表购买1次产品所获得的干草数量
// 每个公司的产品都可以购买任意次
// 你一定要至少购买h数量的干草，返回最少要花多少钱
// 测试链接 : https://www.luogu.com.cn/problem/P2918
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的所有代码，并把主类名改成"Main"，可以直接通过

/*
 * 算法详解：
 * 这是一个完全背包问题的变种。与标准完全背包不同的是，这里要求背包至少装满h单位的物品，
 * 而不是不超过背包容量。因此，我们需要调整状态定义和转移方程。
 * 
 * 解题思路：
 * 1. 状态定义：dp[j]表示恰好购买j单位干草的最小花费
 * 2. 状态转移方程：dp[j] = min(dp[j], dp[j-val[i]] + cost[i])
 * 3. 初始化：dp[0] = 0，其余初始化为无穷大
 * 4. 答案：min(dp[h], dp[h+1], ..., dp[m])，其中m = h + maxv
 * 
 * 关键点解析：
 * 1. 为什么背包容量要扩展到h + maxv？
 *    因为我们要求至少购买h单位干草，所以可能需要购买超过h单位的干草才能达到最小花费。
 *    但是超过h + maxv的部分是没有意义的，因为我们可以用价值最大的物品来补足。
 * 
 * 时间复杂度分析：
 * 设有n个公司，需要购买h单位干草，背包容量为m = h + maxv
 * 1. 动态规划计算：O(n * m)
 * 总时间复杂度：O(n * m)
 * 
 * 空间复杂度分析：
 * 1. 一维DP数组：O(m)
 * 
 * 相关题目扩展：
 * 1. 洛谷 P2918 购买干草（本题）
 * 2. LeetCode 322. 零钱兑换
 * 3. LeetCode 279. 完全平方数
 * 4. 洛谷 P1616 疯狂的采药
 * 5. LeetCode 518. 零钱兑换 II
 * 6. 洛谷 P1198 洛谷校门外的树
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理空输入、非法输入等边界情况
 * 3. 可配置性：可以将MAXN和MAXM作为配置参数传入
 * 4. 单元测试：为compute1和compute2方法编写测试用例
 * 5. 性能优化：对于大数据量场景，考虑使用位运算优化
 * 
 * 语言特性差异：
 * 1. Java：使用静态数组提高访问速度，StreamTokenizer优化输入
 * 2. C++：可以使用vector，但要注意内存分配开销
 * 3. Python：列表推导式简洁但性能较低，可使用numpy优化
 * 
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 * 
 * 优化点：
 * 1. 空间压缩：从二维dp优化到一维dp
 * 2. 遍历顺序优化：内层循环从val[i]开始，避免不必要的判断
 * 3. 数据类型优化：使用long避免整数溢出
 * 
 * 与标准完全背包的区别：
 * 1. 目标函数：标准完全背包求最大价值，本题求最小花费
 * 2. 状态初始化：标准完全背包dp[0] = 0，其余为0；本题dp[0] = 0，其余为无穷大
 * 3. 答案获取：标准完全背包答案是dp[m]；本题答案是min(dp[h..m])
 * 4. 背包容量：标准完全背包容量固定；本题容量需要扩展
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code06_BuyingHayMinimumCost {

	public static int MAXN = 101;

	public static int MAXM = 55001;

	public static int[] val = new int[MAXN];

	public static int[] cost = new int[MAXN];

	public static int[] dp = new int[MAXM];

	public static int n, h, maxv, m;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			in.nextToken();
			h = (int) in.nval;
			maxv = 0;
			for (int i = 1; i <= n; i++) {
				in.nextToken();
				val[i] = (int) in.nval;
				maxv = Math.max(maxv, val[i]);
				in.nextToken();
				cost[i] = (int) in.nval;
			}
			// 最核心的一句
			// 包含重要分析
			m = h + maxv;
			out.println(compute2());
		}
		out.flush();
		out.close();
		br.close();
	}

	// dp[i][j] : 1...i里挑公司，购买严格j磅干草，需要的最少花费
	// 1) dp[i-1][j]
	// 2) dp[i][j-val[i]] + cost[i]
	// 两种可能性中选最小
	// 但是关于j需要进行一定的扩充，原因视频里讲了
	public static int compute1() {
		int[][] dp = new int[n + 1][m + 1];
		Arrays.fill(dp[0], 1, m + 1, Integer.MAX_VALUE);
		for (int i = 1; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				dp[i][j] = dp[i - 1][j];
				if (j - val[i] >= 0 && dp[i][j - val[i]] != Integer.MAX_VALUE) {
					dp[i][j] = Math.min(dp[i][j], dp[i][j - val[i]] + cost[i]);
				}
			}
		}
		int ans = Integer.MAX_VALUE;
		// >= h
		// h h+1 h+2 ... m
		for (int j = h; j <= m; j++) {
			ans = Math.min(ans, dp[n][j]);
		}
		return ans;
	}

	// 空间压缩
	public static int compute2() {
		Arrays.fill(dp, 1, m + 1, Integer.MAX_VALUE);
		for (int i = 1; i <= n; i++) {
			for (int j = val[i]; j <= m; j++) {
				if (dp[j - val[i]] != Integer.MAX_VALUE) {
					dp[j] = Math.min(dp[j], dp[j - val[i]] + cost[i]);
				}
			}
		}
		int ans = Integer.MAX_VALUE;
		for (int j = h; j <= m; j++) {
			ans = Math.min(ans, dp[j]);
		}
		return ans;
	}

}