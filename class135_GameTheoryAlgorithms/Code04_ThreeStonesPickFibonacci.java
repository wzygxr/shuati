package class096;

import java.util.Arrays;

// 三堆石头拿取斐波那契数博弈
// 有三堆石头，数量分别为a、b、c
// 两个人轮流拿，每次可以选择其中一堆石头，拿取斐波那契数的石头
// 拿到最后一颗石子的人获胜，根据a、b、c返回谁赢
// 来自真实大厂笔试，每堆石子的数量在10^5以内
// 没有在线测试，对数器验证
// 
// 题目来源：
// 1. 洛谷 P1247 取火柴游戏 - https://www.luogu.com.cn/problem/P1247
// 2. LeetCode 292. Nim Game - https://leetcode.com/problems/nim-game/
// 3. 牛客网 NC13685 取石子游戏 - https://www.nowcoder.com/practice/f6153503169545229c77481040056a63
// 4. HDU 1846 Brave Game - http://acm.hdu.edu.cn/showproblem.php?pid=1846
// 5. POJ 2313. Bash Game - http://poj.org/problem?id=2313
// 
// 算法核心思想：
// 1. 动态规划方法：通过递归+记忆化搜索计算每个状态的胜负情况
// 2. SG函数方法：通过SG定理计算每个状态的SG值
// 
// 时间复杂度分析：
// 1. win1方法：O(a*b*c*|fib|) - 递归计算每个状态
// 2. win2方法：O(max(a,b,c)*|fib|) - 计算SG值
// 
// 空间复杂度分析：
// 1. win1方法：O(a*b*c) - 记忆化搜索数组
// 2. win2方法：O(max(a,b,c)) - SG值数组
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：预计算斐波那契数列
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：提供了多种解法，可根据需求选择
public class Code04_ThreeStonesPickFibonacci {

	// 如果MAXN变大
	// 相应的要修改f数组
	public static int MAXN = 201;

	// MAXN以内的斐波那契数
	public static int[] f = { 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144 };

	public static String[][][] dp = new String[MAXN][MAXN][MAXN];

	// 动态规划方法彻底尝试
	// 为了验证
	// 
	// 算法原理：
	// 使用递归+记忆化搜索计算每个状态的胜负情况
	// 1. 终止状态：当所有堆都为0时，当前玩家败
	// 2. 递推关系：如果存在一种操作使得对手处于必败态，则当前玩家必胜
	// 3. 记忆化：避免重复计算相同状态
	public static String win1(int a, int b, int c) {
		// 异常处理：处理非法输入
		if (a < 0 || b < 0 || c < 0) {
			return "输入非法";
		}
		
		// 终止状态：当所有堆都为0时，当前玩家败
		// 注意不是全局的先手，是当前的先手来行动！
		// 当前！当前！当前！
		if (a + b + c == 0) {
			// 当前的先手，面对这个局面
			// 返回当前的后手赢
			return "后手";
		}
		
		// 记忆化搜索
		if (dp[a][b][c] != null) {
			return dp[a][b][c];
		}
		
		// 默认当前玩家败
		String ans = "后手"; // ans : 赢的是当前的先手，还是当前的后手
		
		// 尝试从第一堆取石子
		for (int i = 0; i < f.length; i++) {
			if (f[i] <= a) {
				// 如果对手在新状态下必败，则当前玩家必胜
				if (win1(a - f[i], b, c).equals("后手")) {
					// 后续过程的赢家是后续过程的后手
					// 那就表示当前的先手，通过这个后续过程，能赢
					ans = "先手";
					break;
				}
			}
		}
		
		// 如果还未找到必胜策略，尝试从第二堆取石子
		if (ans.equals("后手")) {
			for (int i = 0; i < f.length; i++) {
				if (f[i] <= b) {
					// 如果对手在新状态下必败，则当前玩家必胜
					if (win1(a, b - f[i], c).equals("后手")) {
						// 后续过程的赢家是后续过程的后手
						// 那就表示当前的先手，通过这个后续过程，能赢
						ans = "先手";
						break;
					}
				}
			}
		}
		
		// 如果还未找到必胜策略，尝试从第三堆取石子
		if (ans.equals("后手")) {
			for (int i = 0; i < f.length; i++) {
				if (f[i] <= c) {
					// 如果对手在新状态下必败，则当前玩家必胜
					if (win1(a, b, c - f[i]).equals("后手")) {
						// 后续过程的赢家是后续过程的后手
						// 那就表示当前的先手，通过这个后续过程，能赢
						ans = "先手";
						break;
					}
				}
			}
		}
		
		// 记忆化结果
		dp[a][b][c] = ans;
		return ans;
	}

	// sg定理
	public static int[] sg = new int[MAXN];

	public static boolean[] appear = new boolean[MAXN];

	// O(10^5 * 24 * 2)
	// 
	// 算法原理：
	// 使用SG定理计算每个状态的SG值
	// 1. 对每个石子数计算其SG值
	// 2. 整个游戏的SG值等于各堆SG值的异或和
	// 3. SG值不为0表示必胜态，为0表示必败态
	public static void build() {
		// 计算每个石子数对应的SG值
		for (int i = 1; i < MAXN; i++) {
			// 初始化appear数组
			Arrays.fill(appear, false);
			
			// 计算状态i的所有后继状态的SG值
			for (int j = 0; j < f.length && i - f[j] >= 0; j++) {
				appear[sg[i - f[j]]] = true;
			}
			
			// 计算mex值
			for (int s = 0; s < MAXN; s++) {
				if (!appear[s]) {
					sg[i] = s;
					break;
				}
			}
		}
	}

	public static String win2(int a, int b, int c) {
		// 异常处理：处理非法输入
		if (a < 0 || b < 0 || c < 0) {
			return "输入非法";
		}
		
		// 根据SG定理计算整个游戏的SG值
		return (sg[a] ^ sg[b] ^ sg[c]) != 0 ? "先手" : "后手";
	}

	public static void main(String[] args) {
		build();
		System.out.println("测试开始");
		for (int a = 0; a < MAXN; a++) {
			for (int b = 0; b < MAXN; b++) {
				for (int c = 0; c < MAXN; c++) {
					String ans1 = win1(a, b, c);
					String ans2 = win2(a, b, c);
					if (!ans1.equals(ans2)) {
						System.out.println("出错了！");
					}
				}
			}
		}
		System.out.println("测试结束");

		// 试图找到简洁规律，想通过O(1)的过程就得到sg(x)
		// 于是打印200以内的sg值，开始观察
		// 刚开始有规律，但是在sg(138)之后开始发生异常波动
		// 这道题在考的时候，数据量并没有大到需要O(1)的过程才能通过
		// 那就用build方法计算sg值，不再找寻简洁规律
		// 考试时一切根据题目数据量来决定是否继续优化
		for (int i = 0; i < MAXN; i++) {
			System.out.println("sg(" + i + ") : " + sg[i]);
		}
	}

}