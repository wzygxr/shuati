package class125;

// 种草的方法数(轮廓线dp)
// 给定一个n*m的二维网格grid
// 网格里只有0、1两种值，0表示该田地不能种草，1表示该田地可以种草
// 种草的时候，任何两个种了草的田地不能相邻，相邻包括上、下、左、右四个方向
// 你可以随意决定种多少草，只要不破坏上面的规则即可
// 返回种草的方法数，答案对100000000取模
// 1 <= n, m <= 12
// 测试链接 : https://www.luogu.com.cn/problem/P1879
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

// 题目解析：
// 这是一个经典的轮廓线DP问题。我们需要在网格中种植草皮，满足相邻格子不能同时种草的约束。
// 该问题可以使用轮廓线动态规划解决，通过逐格递推并记录轮廓线状态来计算方案数。

// 解题思路：
// 使用轮廓线DP方法，通过记忆化搜索实现。轮廓线是已决策格子和未决策格子的分界线。
// 在逐格递推的过程中，轮廓线将棋盘分为已处理和未处理两部分。

// 状态设计：
// dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s时的方案数。
// 轮廓线：当前格子(i,j)左边的格子(i,j-1)和上面的格子(i-1,j)到(i,j-1)的这一段。
// 状态s用二进制表示，第k位为1表示轮廓线第k个位置已种草，为0表示未种草。

// 状态转移：
// 对于当前格子(i,j)，我们考虑两种情况：
// 1. 不种草：将轮廓线状态中第j位设为0，然后转移到下一个格子
// 2. 种草：前提是该位置可以种草且不与相邻位置冲突，将轮廓线状态中第j位设为1，然后转移到下一个格子

// 最优性分析：
// 该解法的时间复杂度为O(n * m * 2^m)，空间复杂度为O(n * m * 2^m)。
// 通过滚动数组可以将空间复杂度优化至O(m * 2^m)。

// 边界场景处理：
// 1. 当n=0或m=0时，方案数为1（空网格有一种方案）
// 2. 当网格全为0时，方案数为1（不能种任何草）
// 3. 当到达行末时，转移到下一行

// 工程化考量：
// 1. 使用滚动数组优化空间复杂度
// 2. 使用位运算优化状态操作
// 3. 输入输出使用BufferedReader和PrintWriter提高效率
// 4. 对于特殊情况进行了预处理优化

// Java实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code01_CornFields2.java
// Python实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code01_CornFields2.py
// C++实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code01_CornFields2.cpp

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_CornFields2 {

	public static int MAXN = 12;

	public static int MAXM = 12;

	public static int MOD = 100000000;

	public static int[][] grid = new int[MAXN][MAXM];

	public static int[][][] dp = new int[MAXN][MAXM][1 << MAXM];

	public static int n, m, maxs;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		maxs = 1 << m;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				in.nextToken();
				grid[i][j] = (int) in.nval;
			}
		}
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

	// 时间复杂度O(n * 2的m次方 * m)
	public static int compute() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				for (int s = 0; s < maxs; s++) {
					dp[i][j][s] = -1;
				}
			}
		}
		return f(0, 0, 0);
	}

	// 当前来到i行j列
	// i-1行中，[j..m-1]列的种草状况用s[j..m-1]表示
	// i行中，[0..j-1]列的种草状况用s[0..j-1]表示
	// s表示轮廓线的状况
	// 返回后续有几种种草方法
	public static int f(int i, int j, int s) {
		if (i == n) {
			return 1;
		}
		if (j == m) {
			return f(i + 1, 0, s);
		}
		if (dp[i][j][s] != -1) {
			return dp[i][j][s];
		}
		int ans = f(i, j + 1, set(s, j, 0));
		if (grid[i][j] == 1 && (j == 0 || get(s, j - 1) == 0) && get(s, j) == 0) {
			ans = (ans + f(i, j + 1, set(s, j, 1))) % MOD;
		}
		dp[i][j][s] = ans;
		return ans;
	}

	public static int get(int s, int j) {
		return (s >> j) & 1;
	}

	public static int set(int s, int j, int v) {
		return v == 0 ? (s & (~(1 << j))) : (s | (1 << j));
	}

}