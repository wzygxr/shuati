package class143;

// 墨墨的等式(dijkstra算法)
// 一共有n种正数，每种数可以选择任意个，个数不能是负数
// 那么一定有某些数值可以由这些数字累加得到
// 请问在[l...r]范围上，有多少个数能被累加得到
// 0 <= n <= 12
// 0 <= 数值范围 <= 5 * 10^5
// 1 <= l <= r <= 10^12
// 测试链接 : https://www.luogu.com.cn/problem/P2371
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
 * 算法思路：
 * 这道题可以转化为图论问题，用Dijkstra算法解决。
 * 选择数组中最小的数作为基准数x，构建模x意义下的最短路图。
 * 每个点i表示模x余数为i的所有数中能被表示的最小值。
 * 通过其他数字在不同余数之间建立边，权值为数字值。
 * 最后统计[l,r]范围内能被表示的数的个数。
 * 
 * 时间复杂度：O(x * log x + n)
 * 空间复杂度：O(x)
 * 
 * 题目来源：洛谷P2371 墨墨的等式 (https://www.luogu.com.cn/problem/P2371)
 * 相关题目：
 * 1. 洛谷P3403 跳楼机 - 与本题思路相同 (https://www.luogu.com.cn/problem/P3403)
 * 2. POJ 2371 Counting Capacities - 经典同余最短路问题 (http://poj.org/problem?id=2371)
 * 3. Codeforces 1117D Magic Gems - 矩阵快速幂+最短路优化DP (https://codeforces.com/problemset/problem/1117/D)
 * 4. 洛谷P2662 牛场围栏 - 同余最短路应用 (https://www.luogu.com.cn/problem/P2662)
 * 5. POJ 1061 青蛙的约会 - 扩展欧几里得算法 (http://poj.org/problem?id=1061)
 * 6. Codeforces 986F Oppa Funcan Style Remastered - 同余最短路 (https://codeforces.com/problemset/problem/986/F)
 * 7. 洛谷P2421 荒岛野人 - 数论问题 (https://www.luogu.com.cn/problem/P2421)
 * 8. POJ 3250 Bad Hair Day - 单调栈问题 (http://poj.org/problem?id=3250)
 * 9. 洛谷P9140 背包 - 同余最短路应用 (https://www.luogu.com.cn/problem/P9140)
 * 10. 洛谷P1776 数列分段 - 动态规划问题 (https://www.luogu.com.cn/problem/P1776)
 * 11. 洛谷P1948 数学作业 - 同余最短路 (https://www.luogu.com.cn/problem/P1948)
 * 12. LeetCode 743 Network Delay Time - Dijkstra算法应用 (https://leetcode.cn/problems/network-delay-time/)
 * 13. LeetCode 1631 Path With Minimum Effort - Dijkstra算法应用 (https://leetcode.cn/problems/path-with-minimum-effort/)
 * 14. LeetCode 773 Sliding Puzzle - BFS/最短路问题 (https://leetcode.cn/problems/sliding-puzzle/)
 * 15. AtCoder ARC084_B Small Multiple - 01-BFS问题 (https://atcoder.jp/contests/abc077/tasks/arc084_b)
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Code04_MomoEquation1 {

	public static int MAXN = 500001;

	public static int MAXM = 5000001;

	public static int n, x;

	public static long l, r;

	// 链式前向星需要
	public static int[] head = new int[MAXN];

	public static int[] next = new int[MAXM];

	public static int[] to = new int[MAXM];

	public static long[] weight = new long[MAXM];

	public static int cnt;

	// dijkstra算法需要
	// 0 : 当前节点
	// 1 : 源点到当前点距离
	public static PriorityQueue<long[]> heap = new PriorityQueue<>((a, b) -> a[1] <= b[1] ? -1 : 1);

	public static long[] distance = new long[MAXN];

	public static boolean[] visited = new boolean[MAXN];

	public static void prepare() {
		cnt = 1;
		heap.clear();
		Arrays.fill(head, 0, x, 0);
		Arrays.fill(distance, 0, x, Long.MAX_VALUE);
		Arrays.fill(visited, 0, x, false);
	}

	public static void addEdge(int u, int v, long w) {
		next[cnt] = head[u];
		to[cnt] = v;
		weight[cnt] = w;
		head[u] = cnt++;
	}

	public static void dijkstra() {
		heap.add(new long[] { 0, 0 });
		distance[0] = 0;
		long[] cur;
		int u;
		long w;
		while (!heap.isEmpty()) {
			cur = heap.poll();
			u = (int) cur[0];
			w = cur[1];
			if (visited[u]) {
				continue;
			}
			visited[u] = true;
			for (int ei = head[u], v; ei > 0; ei = next[ei]) {
				v = to[ei];
				if (!visited[v] && distance[v] > w + weight[ei]) {
					distance[v] = w + weight[ei];
					heap.add(new long[] { v, distance[v] });
				}
			}
		}
	}

	public static long compute() {
		dijkstra();
		long ans = 0;
		for (int i = 0; i < x; i++) {
			if (r >= distance[i]) {
				ans += (r - distance[i]) / x + 1;
			}
			if (l >= distance[i]) {
				ans -= (l - distance[i]) / x + 1;
			}
		}
		return ans;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		l = (long) in.nval - 1;
		in.nextToken();
		r = (long) in.nval;
		x = 0;
		for (int i = 1, vi; i <= n; i++) {
			in.nextToken();
			vi = (int) in.nval;
			if (vi != 0) {
				if (x == 0) {
					x = vi;
					prepare();
				} else {
					for (int j = 0; j < x; j++) {
						addEdge(j, (j + vi) % x, vi);
					}
				}
			}
		}
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

}