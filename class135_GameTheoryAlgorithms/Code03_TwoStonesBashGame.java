package class096;

import java.util.Arrays;

// 两堆石头的巴什博弈
// 有两堆石头，数量分别为a、b
// 两个人轮流拿，每次可以选择其中一堆石头，拿1~m颗
// 拿到最后一颗石子的人获胜，根据a、b、m返回谁赢
// 来自真实大厂笔试，没有在线测试，对数器验证
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
// 3. 数学规律方法：通过数学推导发现规律，(a % (m+1)) != (b % (m+1))时先手必胜
// 
// 时间复杂度分析：
// 1. win1方法：O(a*b*m) - 递归计算每个状态
// 2. win2方法：O(max(a,b)*m) - 计算SG值
// 3. win3方法：O(1) - 直接使用数学公式
// 
// 空间复杂度分析：
// 1. win1方法：O(a*b) - 记忆化搜索数组
// 2. win2方法：O(max(a,b)) - SG值数组
// 3. win3方法：O(1) - 常数空间
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：win3方法是数学规律的最优解
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：提供了多种解法，可根据需求选择
public class Code03_TwoStonesBashGame {

	public static int MAXN = 101;

	public static String[][][] dp = new String[MAXN][MAXN][MAXN];

	// 动态规划方法彻底尝试
	// 为了验证
	// 
	// 算法原理：
	// 使用递归+记忆化搜索计算每个状态的胜负情况
	// 1. 终止状态：当所有堆都为0时，当前玩家败
	// 2. 递推关系：如果存在一种操作使得对手处于必败态，则当前玩家必胜
	// 3. 记忆化：避免重复计算相同状态
	public static String win1(int a, int b, int m) {
		// 异常处理：处理非法输入
		if (a < 0 || b < 0 || m <= 0) {
			return "输入非法";
		}
		
		// 特殊情况优化
		if (m >= Math.max(a, b)) {
			return a != b ? "先手" : "后手";
		}
		
		// 平局情况
		if (a == b) {
			return "后手";
		}
		
		// 记忆化搜索
		if (dp[a][b][m] != null) {
			return dp[a][b][m];
		}
		
		// 默认当前玩家败
		String ans = "后手";
		
		// 尝试从第一堆取石子
		for (int pick = 1; pick <= Math.min(a, m); pick++) {
			// 如果对手在新状态下必败，则当前玩家必胜
			if (win1(a - pick, b, m).equals("后手")) {
				ans = "先手";
				break;
			}
		}
		
		// 如果还未找到必胜策略，尝试从第二堆取石子
		if (ans.equals("后手")) {
			for (int pick = 1; pick <= Math.min(b, m); pick++) {
				// 如果对手在新状态下必败，则当前玩家必胜
				if (win1(a, b - pick, m).equals("后手")) {
					ans = "先手";
					break;
				}
			}
		}
		
		// 记忆化结果
		dp[a][b][m] = ans;
		return ans;
	}

	// sg定理
	// 
	// 算法原理：
	// 使用SG定理计算每个状态的SG值
	// 1. 对每个石子数计算其SG值
	// 2. 整个游戏的SG值等于各堆SG值的异或和
	// 3. SG值不为0表示必胜态，为0表示必败态
	public static String win2(int a, int b, int m) {
		// 异常处理：处理非法输入
		if (a < 0 || b < 0 || m <= 0) {
			return "输入非法";
		}
		
		// 计算最大石子数
		int n = Math.max(a, b);
		
		// SG数组
		int[] sg = new int[n + 1];
		// appear数组用于计算mex值
		boolean[] appear = new boolean[m + 1];
		
		// 计算每个石子数对应的SG值
		for (int i = 1; i <= n; i++) {
			// 初始化appear数组
			Arrays.fill(appear, false);
			
			// 计算状态i的所有后继状态的SG值
			for (int j = 1; j <= m && i - j >= 0; j++) {
				appear[sg[i - j]] = true;
			}
			
			// 计算mex值
			for (int s = 0; s <= m; s++) {
				if (!appear[s]) {
					sg[i] = s;
					break;
				}
			}
		}
		
		// 根据SG定理计算整个游戏的SG值
		return (sg[a] ^ sg[b]) != 0 ? "先手" : "后手";
	}

	// 时间复杂度O(1)的最优解
	// 其实是根据方法2中的sg表观察出来的
	// 
	// 算法原理：
	// 通过数学推导发现规律
	// 当a % (m+1) != b % (m+1)时先手必胜，否则后手必胜
	// 
	// 证明思路：
	// 1. 当a % (m+1) == b % (m+1)时，无论先手如何操作
	// 2. 后手总能模仿先手的操作，使得两堆石子仍满足该条件
	// 3. 最终后手取走最后的石子获胜
	// 
	// 反之，当a % (m+1) != b % (m+1)时
	// 4. 先手可以操作使得两堆石子满足该条件，转化为后手必败态
	public static String win3(int a, int b, int m) {
		// 异常处理：处理非法输入
		if (a < 0 || b < 0 || m <= 0) {
			return "输入非法";
		}
		
		// 核心判断逻辑
		return a % (m + 1) != b % (m + 1) ? "先手" : "后手";
	}

	public static void main(String[] args) {
		System.out.println("测试开始");
		for (int a = 0; a < MAXN; a++) {
			for (int b = 0; b < MAXN; b++) {
				for (int m = 1; m < MAXN; m++) {
					String ans1 = win1(a, b, m);
					String ans2 = win2(a, b, m);
					String ans3 = win3(a, b, m);
					if (!ans1.equals(ans2) || !ans1.equals(ans3)) {
						System.out.println("出错了！");
					}
				}
			}
		}
		System.out.println("测试结束");
	}

}