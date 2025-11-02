package class129;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 洛谷 P1613 跑路
 * 
 * 题目描述：
 * 一共有n个节点，编号1~n，一共有m条有向边，每条边1公里
 * 有一个空间跑路器，每秒你都可以直接移动2^k公里，每秒钟可以随意决定k的值
 * 题目保证1到n之间一定可以到达，返回1到n最少用几秒
 * 
 * 解题思路：
 * 这是一个结合了倍增思想和最短路径算法的图论问题。
 * 
 * 核心思想：
 * 1. 预处理：使用倍增思想找出所有可以通过2^k步到达的点对
 * 2. 最短路径：在预处理后的图上使用Floyd算法计算最短时间
 * 
 * 具体步骤：
 * 1. 初始化：对于每条原始边，标记为可以通过2^0=1步到达
 * 2. 倍增预处理：对于每个k，计算哪些点对可以通过2^k步到达
 *    - 如果点i可以通过2^(k-1)步到达点jump，且点jump可以通过2^(k-1)步到达点j
 *    - 那么点i可以通过2^k步到达点j
 * 3. 最短路径计算：在新图上使用Floyd算法计算1到n的最短时间
 * 
 * 时间复杂度：O(n^3 * log k + n^3) = O(n^3 * log k)
 * 空间复杂度：O(n^2 * log k)
 * 
 * 相关题目：
 * 1. LeetCode 1334. 阈值距离内邻居最少的城市 (Floyd算法)
 * 2. LeetCode 743. 网络延迟时间 (Dijkstra算法)
 * 3. POJ 1613 - Run Away (相同题目)
 * 4. Codeforces 1083F. The Fair Nut and Amusing Xor
 * 5. AtCoder ABC128D. equeue
 * 6. 牛客网 NC370. 会议室安排
 * 7. 杭电OJ 5171. GTY's birthday gift
 * 8. UVa 10382. Watering Grass
 * 9. CodeChef - STABLEMP
 * 10. SPOJ - ACTIV
 * 
 * 工程化考量：
 * 1. 在实际应用中，这类算法常用于：
 *    - 网络路由优化
 *    - 交通路径规划
 *    - 游戏中角色移动路径计算
 * 2. 实现优化：
 *    - 对于稀疏图，可以考虑使用Dijkstra算法替代Floyd算法
 *    - 可以使用位运算优化2^k的计算
 *    - 对于大规模数据，可以考虑分块处理
 * 3. 可扩展性：
 *    - 支持动态添加和删除边
 *    - 处理多种移动方式（不仅仅是2^k）
 *    - 扩展到多源最短路径问题
 * 4. 鲁棒性考虑：
 *    - 处理图不连通的情况
 *    - 处理节点和边数超限的情况
 *    - 优化极端情况下的性能
 */
public class Code02_RanAway {

	public static int MAXN = 61;

	public static int MAXP = 64;

	public static int NA = Integer.MAX_VALUE;

	// st[i][j][p] : i到j的距离是不是2^p
	public static boolean[][][] st = new boolean[MAXN][MAXN][MAXP + 1];

	// time[i][j] : i到j的最短时间
	public static int[][] time = new int[MAXN][MAXN];

	public static int n, m;

	/**
	 * 初始化数据结构
	 */
	public static void build() {
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				st[i][j][0] = false;
				time[i][j] = NA;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		build();
		in.nextToken();
		m = (int) in.nval;
		for (int i = 1, u, v; i <= m; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			st[u][v][0] = true;
			time[u][v] = 1;
		}
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算从节点1到节点n的最短时间
	 * 
	 * @return 最短时间（秒数）
	 */
	// 需要先掌握，讲解065 - Floyd算法
	public static int compute() {
		// 先枚举次方
		// 再枚举跳板
		// 最后枚举每一组(i,j)
		for (int p = 1; p <= MAXP; p++) {
			for (int jump = 1; jump <= n; jump++) {
				for (int i = 1; i <= n; i++) {
					for (int j = 1; j <= n; j++) {
						// 如果点i可以通过2^(p-1)步到达点jump，且点jump可以通过2^(p-1)步到达点j
						// 那么点i可以通过2^p步到达点j
						if (st[i][jump][p - 1] && st[jump][j][p - 1]) {
							st[i][j][p] = true;
							time[i][j] = 1;
						}
					}
				}
			}
		}
		// 如果1到n不能通过一步到达，则使用Floyd算法计算最短路径
		if (time[1][n] != 1) {
			// 先枚举跳板
			// 最后枚举每一组(i,j)
			for (int jump = 1; jump <= n; jump++) {
				for (int i = 1; i <= n; i++) {
					for (int j = 1; j <= n; j++) {
						// 如果i到jump和jump到j都可达，则更新i到j的最短时间
						if (time[i][jump] != NA && time[jump][j] != NA) {
							time[i][j] = Math.min(time[i][j], time[i][jump] + time[jump][j]);
						}
					}
				}
			}
		}
		return time[1][n];
	}

}