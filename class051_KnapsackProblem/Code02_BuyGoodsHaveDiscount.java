package class073;

// 夏季特惠
// 某公司游戏平台的夏季特惠开始了，你决定入手一些游戏
// 现在你一共有X元的预算，平台上所有的 n 个游戏均有折扣
// 标号为 i 的游戏的原价a_i元，现价只要b_i元
// 也就是说该游戏可以优惠 a_i - b_i，并且你购买该游戏能获得快乐值为w_i
// 由于优惠的存在，你可能做出一些冲动消费导致最终买游戏的总费用超过预算
// 只要满足 : 获得的总优惠金额不低于超过预算的总金额
// 那在心理上就不会觉得吃亏。
// 现在你希望在心理上不觉得吃亏的前提下，获得尽可能多的快乐值。
// 测试链接 : https://leetcode.cn/problems/tJau2o/
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的所有代码，并把主类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

/**
 * 夏季特惠购物问题
 * 
 * 问题描述：
 * 给定预算X元，有n个游戏可供购买，每个游戏有原价、现价和快乐值。
 * 购买游戏可以获得优惠金额（原价-现价），但实际花费是现价。
 * 在心理上不觉得吃亏的前提下（总优惠金额≥超预算金额），求能获得的最大快乐值。
 * 
 * 解题思路：
 * 这是一个变形的01背包问题。我们需要将问题转换为标准的背包问题形式：
 * 1. 将商品分为两类：
 *    - "一定要买的商品"：优惠金额 ≥ 现价，即 (原价-现价) ≥ 现价，这样购买会增加预算
 *    - "需要考虑的商品"：优惠金额 < 现价，这类商品需要在预算范围内进行选择
 * 2. 对于"一定要买的商品"，直接购买并累加其快乐值，同时更新预算
 * 3. 对于"需要考虑的商品"，将其转化为标准背包问题：
 *    - 成本(cost) = 现价 - (原价 - 现价) = 2*现价 - 原价
 *    - 价值(val) = 快乐值
 * 4. 使用01背包算法求解在更新后预算内能获得的最大快乐值
 * 
 * 时间复杂度：O(n * x)，其中n是商品数量，x是预算
 * 空间复杂度：O(x)
 */
public class Code02_BuyGoodsHaveDiscount {

	public static int MAXN = 501;

	public static int MAXX = 100001;

	// 对于"一定要买的商品"，直接买！
	// 只把"需要考虑的商品"放入cost、val数组
	public static int[] cost = new int[MAXN];

	public static long[] val = new long[MAXN];

	public static long[] dp = new long[MAXX];

	public static int n, m, x;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			m = 1;
			in.nextToken();
			x = (int) in.nval;
			long ans = 0;
			long happy = 0;
			for (int i = 1, pre, cur, well; i <= n; i++) {
				// 原价
				in.nextToken(); pre = (int) in.nval;
				// 现价
				in.nextToken(); cur = (int) in.nval;
				// 快乐值
				in.nextToken(); happy = (long) in.nval;
				well = pre - cur - cur;
				// 如下是一件"一定要买的商品"
				// 预算 = 100，商品原价 = 10，打折后 = 3
				// 那么好处(well) = (10 - 3) - 3 = 4
				// 所以，可以认为这件商品把预算增加到了104！一定要买！
				// 如下是一件"需要考虑的商品"
				// 预算 = 104，商品原价 = 10，打折后 = 8
				// 那么好处(well) = (10 - 8) - 8 = -6
				// 所以，可以认为这件商品就花掉6元！
				// 也就是说以后花的不是打折后的值，是"坏处"
				if (well >= 0) {
					x += well;
					ans += happy;
				} else {
					cost[m] = -well;
					val[m++] = happy;
				}
			}
			ans += compute();
			out.println(ans);
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 01背包算法求解在预算内能获得的最大快乐值
	 * 
	 * @return 最大快乐值
	 */
	public static long compute() {
		// 初始化dp数组，dp[j]表示预算为j时能获得的最大快乐值
		Arrays.fill(dp, 0, x + 1, 0);
		
		// 遍历每件需要考虑的商品
		for (int i = 1; i < m; i++) {
			// 从后往前遍历预算，避免重复选择同一件商品
			for (int j = x; j >= cost[i]; j--) {
				// 状态转移方程：
				// dp[j] = max(不选择第i件商品, 选择第i件商品)
				// 不选择：dp[j]（保持原值）
				// 选择：dp[j - cost[i]] + val[i]（选择第i件商品后的最大快乐值）
				dp[j] = Math.max(dp[j], dp[j - cost[i]] + val[i]);
			}
		}
		
		// 返回预算为x时能获得的最大快乐值
		return dp[x];
	}

}