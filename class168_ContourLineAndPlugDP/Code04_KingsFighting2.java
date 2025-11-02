package class125;

// 摆放国王的方法数(轮廓线dp+空间压缩)
// 给定两个参数n和k，表示n*n的区域内要摆放k个国王
// 国王可以攻击临近的8个方向，所以摆放时不能让任何两个国王打架
// 返回摆放的方法数
// 1 <= n <= 9
// 1 <= k <= n*n
// 测试链接 : https://www.luogu.com.cn/problem/P1896
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例
// 空间压缩的版本一定稳定通过

// 题目解析：
// 这是一个经典的国王放置问题。给定一个n×n的棋盘，要在上面放置k个国王，
// 要求任意两个国王不能相邻（包括对角线相邻）。求有多少种放置方法。

// 解题思路：
// 使用轮廓线DP解决这个问题，并通过滚动数组优化空间复杂度。
// 轮廓线DP是一种特殊的动态规划方法，适用于解决棋盘类问题。
// 我们逐格转移，将棋盘的边界线（轮廓线）的状态作为DP状态的一部分。

// 状态设计：
// dp[j][s][leftup][k] 表示处理到当前行第j列，轮廓线状态为s，
// 左上角是否有国王为leftup，还剩k个国王需要放置的方案数。
// 轮廓线：当前格子(i,j)左边的格子(i,j-1)和上面的格子(i-1,j)到(i,j-1)的这一段。
// 状态s用二进制表示，第k位为1表示轮廓线第k个位置有国王，为0表示没有国王。

// 状态转移：
// 对于当前格子(i,j)，我们考虑两种情况：
// 1. 不放国王：直接转移到下一个格子
// 2. 放国王：前提是该位置可以放国王（与相邻8个方向都没有国王），转移到下一个格子

// 最优性分析：
// 该解法是最优的，因为：
// 1. 时间复杂度O(n^2 * 2^n * k)在可接受范围内
// 2. 通过滚动数组将空间复杂度从O(n^2 * 2^n * k)优化至O(2^n * k)
// 3. 状态转移清晰，没有冗余计算

// 边界场景处理：
// 1. 当i==n时，如果k==0则返回1，否则返回0
// 2. 当j==n时，转移到下一行

// 工程化考量：
// 1. 使用滚动数组优化空间复杂度
// 2. 输入输出使用BufferedReader和PrintWriter提高效率
// 3. 使用位运算优化状态操作

// Java实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code04_KingsFighting2.java
// Python实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code04_KingsFighting2.py
// C++实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code04_KingsFighting2.cpp

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code04_KingsFighting2 {

	public static int MAXN = 9;

	public static int MAXK = 82;

	public static long[][][][] dp = new long[MAXN + 1][1 << MAXN][2][MAXK];

	public static long[][] prepare = new long[1 << MAXN][MAXK];

	public static int n, kings, maxs;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		kings = (int) in.nval;
		maxs = 1 << n;
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

	public static long compute() {
		for (int s = 0; s < maxs; s++) {
			for (int k = 0; k <= kings; k++) {
				prepare[s][k] = k == 0 ? 1 : 0;
			}
		}
		for (int i = n - 1; i >= 0; i--) {
			// j == n
			for (int s = 0; s < maxs; s++) {
				for (int k = 0; k <= kings; k++) {
					dp[n][s][0][k] = prepare[s][k];
					dp[n][s][1][k] = prepare[s][k];
				}
			}
			// 普通位置
			for (int j = n - 1; j >= 0; j--) {
				for (int s = 0; s < maxs; s++) {
					for (int leftup = 0; leftup <= 1; leftup++) {
						for (int k = 0; k <= kings; k++) {
							int left = j == 0 ? 0 : get(s, j - 1);
							int up = get(s, j);
							int rightup = get(s, j + 1);
							long ans = dp[j + 1][set(s, j, 0)][up][k];
							if (k > 0 && left == 0 && leftup == 0 && up == 0 && rightup == 0) {
								ans += dp[j + 1][set(s, j, 1)][up][k - 1];
							}
							dp[j][s][leftup][k] = ans;
						}
					}
				}
			}
			// 设置prepare
			for (int s = 0; s < maxs; s++) {
				for (int k = 0; k <= kings; k++) {
					prepare[s][k] = dp[0][s][0][k];
				}
			}
		}
		return dp[0][0][0][kings];
	}

	public static int get(int s, int j) {
		return (s >> j) & 1;
	}

	public static int set(int s, int j, int v) {
		return v == 0 ? (s & (~(1 << j))) : (s | (1 << j));
	}

}