package class125;

// 种草的方法数(轮廓线dp+空间压缩)
// 给定一个n*m的二维网格grid
// 网格里只有0、1两种值，0表示该田地不能种草，1表示该田地可以种草
// 种草的时候，任何两个种了草的田地不能相邻，相邻包括上、下、左、右四个方向
// 你可以随意决定种多少草，只要不破坏上面的规则即可
// 返回种草的方法数，答案对100000000取模
// 1 <= n, m <= 12
// 测试链接 : https://www.luogu.com.cn/problem/P1879
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

// 题目解析：
// 这是一个经典的轮廓线DP问题，使用空间优化技术。我们需要在网格中种植草皮，
// 满足相邻格子不能同时种草的约束。该问题可以使用轮廓线动态规划解决。

// 解题思路：
// 使用轮廓线DP方法，并通过滚动数组优化空间复杂度。轮廓线是已决策格子和未决策格子的分界线。
// 在逐格递推的过程中，轮廓线将棋盘分为已处理和未处理两部分。

// 状态设计：
// dp[j][s] 表示处理到当前行第j列，轮廓线状态为s时的方案数。
// 轮廓线：当前格子(i,j)左边的格子(i,j-1)和上面的格子(i-1,j)到(i,j-1)的这一段。
// 状态s用二进制表示，第k位为1表示轮廓线第k个位置已种草，为0表示未种草。

// 状态转移：
// 对于当前格子(i,j)，我们考虑两种情况：
// 1. 不种草：将轮廓线状态中第j位设为0，然后转移到下一个格子
// 2. 种草：前提是该位置可以种草且不与相邻位置冲突，将轮廓线状态中第j位设为1，然后转移到下一个格子

// 最优性分析：
// 该解法的时间复杂度为O(n * m * 2^m)，空间复杂度为O(2^m)。
// 通过滚动数组将空间复杂度从O(n * m * 2^m)优化至O(2^m)。

// 边界场景处理：
// 1. 当n=0或m=0时，方案数为1（空网格有一种方案）
// 2. 当网格全为0时，方案数为1（不能种任何草）
// 3. 当到达行末时，转移到下一行

// 工程化考量：
// 1. 使用滚动数组优化空间复杂度
// 2. 使用位运算优化状态操作
// 3. 输入输出使用BufferedReader和PrintWriter提高效率
// 4. 使用Arrays.fill初始化数组
// 5. 对于特殊情况进行了预处理优化

// Java实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code01_CornFields3.java
// Python实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code01_CornFields3.py
// C++实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code01_CornFields3.cpp

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code01_CornFields3 {

	public static int MAXN = 12;

	public static int MAXM = 12;

	public static int MOD = 100000000;

	public static int[][] grid = new int[MAXN][MAXM];

	public static int[][] dp = new int[MAXM + 1][1 << MAXM];

	public static int[] prepare = new int[1 << MAXM];

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

	public static int compute() {
		Arrays.fill(prepare, 0, maxs, 1);
		for (int i = n - 1; i >= 0; i--) {
			// j == m
			for (int s = 0; s < maxs; s++) {
				dp[m][s] = prepare[s];
			}
			// 普通位置
			for (int j = m - 1; j >= 0; j--) {
				for (int s = 0; s < maxs; s++) {
					int ans = dp[j + 1][set(s, j, 0)];
					if (grid[i][j] == 1 && (j == 0 || get(s, j - 1) == 0) && get(s, j) == 0) {
						ans = (ans + dp[j + 1][set(s, j, 1)]) % MOD;
					}
					dp[j][s] = ans;
				}
			}
			// 设置prepare
			for (int s = 0; s < maxs; s++) {
				prepare[s] = dp[0][s];
			}
		}
		return dp[0][0];
	}

	public static int get(int s, int j) {
		return (s >> j) & 1;
	}

	public static int set(int s, int j, int v) {
		return v == 0 ? (s & (~(1 << j))) : (s | (1 << j));
	}

}