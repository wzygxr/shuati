package class125;

// 贴瓷砖的方法数(轮廓线dp)
// 给定两个参数n和m，表示n行m列的空白区域
// 有无限多的1*2规格的瓷砖，目标是严丝合缝的铺满所有的空白区域
// 返回有多少种铺满的方法
// 1 <= n, m <= 11
// 测试链接 : http://poj.org/problem?id=2411
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

// 题目解析：
// 这是一个经典的骨牌覆盖问题，也称为Mondriaan's Dream问题。
// 给定一个n×m的棋盘，需要用1×2或2×1的多米诺骨牌完全覆盖它，求有多少种不同的覆盖方案。

// 解题思路：
// 使用轮廓线DP解决这个问题。轮廓线DP是一种特殊的动态规划方法，
// 适用于解决棋盘类问题。我们逐格转移，将棋盘的边界线（轮廓线）
// 的状态作为DP状态的一部分。

// 状态设计：
// dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s的方案数。
// 轮廓线：当前格子(i,j)左边的格子(i,j-1)和上面的格子(i-1,j)到(i,j-1)的这一段。
// 状态s用二进制表示，第k位为1表示轮廓线第k个位置已被占用（作为竖砖的上端点），为0表示未被占用。

// 状态转移：
// 对于当前格子(i,j)，我们考虑三种放置骨牌的方式：
// 1. 上方已有竖砖：不能放置新砖，直接转移到下一列
// 2. 上方没有竖砖：
//    a. 竖着放：在当前位置和下一行同一位置放置竖砖
//    b. 横着放：在当前位置和右一列位置放置横砖（前提是右一列未被占用）

// 最优性分析：
// 该解法是最优的，因为：
// 1. 时间复杂度O(n * m * 2^m)在可接受范围内
// 2. 空间复杂度通过滚动数组优化至O(2^m)
// 3. 状态转移清晰，没有冗余计算

// 边界场景处理：
// 1. 当n=0或m=0时，方案数为1（空棋盘有一种覆盖方案）
// 2. 当n或m为奇数且另一个为偶数时，方案数为0
// 3. 通过交换n和m确保m较小，优化时间复杂度

// 工程化考量：
// 1. 使用滚动数组优化空间复杂度
// 2. 输入输出使用BufferedReader和PrintWriter提高效率
// 3. 对于特殊情况进行了预处理优化

// Java实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code02_PavingTile1.java
// Python实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code02_PavingTile1.py
// C++实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code02_PavingTile1.cpp

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code02_PavingTile1 {

	public static int MAXN = 11;

	public static long[][][] dp = new long[MAXN][MAXN][1 << MAXN];

	public static int n, m, maxs;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			in.nextToken();
			m = (int) in.nval;
			maxs = 1 << m;
			if (n != 0 || m != 0) {
				out.println(compute());
			}
		}
		out.flush();
		out.close();
		br.close();
	}

	public static long compute() {
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
	// i-1行中，[j..m-1]列有没有作为竖砖的上点，状况用s[j..m-1]表示
	// i行中，[0..j-1]列有没有作为竖砖的上点，状况用s[0..j-1]表示
	// s表示轮廓线的状况
	// 返回后续有几种摆放的方法
	public static long f(int i, int j, int s) {
		if (i == n) {
			return 1;
		}
		if (j == m) {
			return f(i + 1, 0, s);
		}
		if (dp[i][j][s] != -1) {
			return dp[i][j][s];
		}
		long ans = 0;
		if (get(s, j) == 1) {
			ans = f(i, j + 1, set(s, j, 0));
		} else { // 上方没有竖着摆砖
			if (i + 1 < n) { // 当前竖着摆砖
				ans = f(i, j + 1, set(s, j, 1));
			}
			if (j + 1 < m && get(s, j + 1) == 0) { // 当前横着摆砖
				ans += f(i, j + 2, s);
			}
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