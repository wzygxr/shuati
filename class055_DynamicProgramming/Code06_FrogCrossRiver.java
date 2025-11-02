package class127;

// 过河踩过的最少石子数
// 在河上有一座独木桥，一只青蛙想沿着独木桥从河的一侧跳到另一侧
// 在桥上有一些石子，青蛙很讨厌踩在这些石子上
// 我们可以把独木桥上青蛙可能到达的点看成数轴上的一串整点0...n
// 其中n是桥的长度，坐标为0的点表示桥的起点，坐标为n的点表示桥的终点
// 青蛙从桥的起点开始，不停的向终点方向跳跃，一次跳跃的距离是[s,t]之间的任意正整数
// 当青蛙跳到或跳过坐标为n的点时，就算青蛙已经跳出了独木桥
// 题目给出独木桥的长度n，青蛙跳跃的距离范围s、t，题目还给定m个桥上石子的位置
// 你的任务是确定青蛙要想过河，最少需要踩到的石子数
// 1 <= n <= 10^7
// 1 <= s <= t <= 10
// 1 <= m <= 100
// 测试链接 : https://www.luogu.com.cn/problem/P1052
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

/**
 * 算法思路：
 * 1. 这是一个动态规划问题
 * 2. dp[i]表示到达位置i时最少踩到的石子数
 * 3. 状态转移方程：
 *    dp[i] = min(dp[j] + stone[i] ? 1 : 0) for j in [i-t, i-s]
 * 4. 优化：当s<t时，可以对距离进行压缩，因为青蛙可以跳到足够远的位置
 *    通过实验确定安全距离，超过这个距离后就不会再遇到新的石子
 *
 * 时间复杂度：O(n * (t-s+1))
 * 空间复杂度：O(n)
 */
public class Code06_FrogCrossRiver {

	public static int MAXN = 101;

	public static int MAXL = 100001;

	public static int[] arr = new int[MAXN];

	public static int[] where = new int[MAXN];

	public static int[] dp = new int[MAXL];

	public static boolean[] stone = new boolean[MAXL];

	public static int n, s, t, m, safe;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		s = (int) in.nval;
		in.nextToken();
		t = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		for (int i = 1; i <= m; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算青蛙过河最少踩到的石子数
	 * @return 最少踩到的石子数
	 */
	public static int compute() {
		// 对石子位置进行排序
		Arrays.sort(arr, 1, m + 1);
		// 特殊情况：s == t
		if (s == t) {
			int ans = 0;
			for (int i = 1; i <= m; i++) {
				if (arr[i] % s == 0) {
					ans++;
				}
			}
			return ans;
		} else { // s < t
			// 计算安全距离
			safe = reduce(s, t);
			// 重新计算石子位置
			for (int i = 1; i <= m; i++) {
				where[i] = where[i - 1] + Math.min(arr[i] - arr[i - 1], safe);
				stone[where[i]] = true;
			}
			// 更新桥的长度
			n = where[m] + safe;
			// 初始化dp数组
			Arrays.fill(dp, 1, n + 1, MAXN);
			dp[0] = 0;
			// 动态规划
			for (int i = 1; i <= n; i++) {
				for (int j = Math.max(i - t, 0); j <= i - s; j++) {
					dp[i] = Math.min(dp[i], dp[j] + (stone[i] ? 1 : 0));
				}
			}
			// 找到最小值
			int ans = MAXN;
			for (int i = where[m] + 1; i <= n; i++) {
				ans = Math.min(ans, dp[i]);
			}
			return ans;
		}
	}

	public static int MAXK = 201;

	public static boolean[] reach = new boolean[MAXK];

	// 一旦s和t定了，那么距离多远就可以缩减呢？
	// 做实验！
	public static int reduce(int s, int t) {
		Arrays.fill(reach, false);
		int cnt = 0;
		int ans = 0;
		for (int i = 0; i < MAXK; i++) {
			for (int j = i + s; j < Math.min(i + t + 1, MAXK); j++) {
				reach[j] = true;
			}
			if (!reach[i]) {
				cnt = 0;
			} else {
				cnt++;
			}
			if (cnt == t) {
				ans = i;
				break;
			}
		}
		return ans;
	}

	// 相关题目：
	// 1. LeetCode 403 - Frog Jump
	//    链接：https://leetcode.cn/problems/frog-jump/
	//    区别：青蛙在河中跳跃，每个位置可能有石头，需要判断能否到达最后一块石头
	// 2. Codeforces 965D - Single Wildcard Pattern Matching
	//    链接：https://codeforces.com/problemset/problem/965/D
	//    区别：青蛙在河中跳跃，河中有一些石头，需要计算能否到达对岸
	// 3. LeetCode 1340 - Jump Game V
	//    链接：https://leetcode.cn/problems/jump-game-v/
	//    区别：在数组中跳跃，每次跳跃不能超过固定距离，且需要满足特定条件
	// 4. LeetCode 1306 - Jump Game III
	//    链接：https://leetcode.cn/problems/jump-game-iii/
	//    区别：在数组中跳跃，从起始位置开始，判断能否到达值为0的位置
}