package class129;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

/**
 * 洛谷 P1081 开车旅行
 * 
 * 题目描述：
 * 给定一个长度为n的数组arr，下标1 ~ n范围，数组无重复值
 * 近的定义、距离的定义，和题目4一致
 * a和b同坐一辆车开始往右旅行，a先开车，b后开车，此后每到达一点都换人驾驶
 * 如果a在某点驾驶，那么车去往该点右侧第二近的点，如果b在某点驾驶，那么车去往该点右侧第一近的点
 * a和b从s位置出发，如果开车总距离超过x，或轮到某人时右侧无点可选，那么旅行停止
 * 问题1 : 给定距离x0，返回1 ~ n-1中从哪个点出发，a行驶距离 / b行驶距离，比值最小
 *         如果从多个点出发时，比值都为最小，那么返回arr中的值最大的点
 * 问题2 : 给定s、x，返回旅行停止时，a开了多少距离、b开了多少距离
 * 
 * 解题思路：
 * 这是一个结合了数据结构和倍增思想的复杂问题。
 * 
 * 核心思想：
 * 1. 预处理：对于每个城市，找到它右边的第一近和第二近城市
 * 2. 倍增优化：预处理2^k轮a和b交替开车能到达的位置和距离
 * 3. 查询处理：使用倍增快速计算任意起点和距离限制下的行驶情况
 * 
 * 具体步骤：
 * 1. 使用双向链表找到每个城市的第一近和第二近城市
 * 2. 使用倍增思想预处理状态转移表
 * 3. 对于查询，使用倍增快速计算结果
 * 
 * 时间复杂度：预处理O(n log n)，查询O(log x)
 * 空间复杂度：O(n log n)
 * 
 * 相关题目：
 * 1. LeetCode 220. 存在重复元素 III (TreeSet应用)
 * 2. POJ 1733 - Parity game (离散化 + 倍增)
 * 3. Codeforces 822D - My pretty girl Noora (数学 + 倍增)
 * 4. LeetCode 1353. 最多可以参加的会议数目 (贪心)
 * 5. LeetCode 646. 最长数对链 (贪心)
 * 6. LeetCode 1235. 最大盈利的工作调度 (动态规划 + 二分查找)
 * 7. LeetCode 1751. 最多可以参加的会议数目 II (动态规划 + 二分查找)
 * 8. LeetCode 452. 用最少数量的箭引爆气球 (贪心)
 * 9. LeetCode 253. 会议室 II (扫描线算法)
 * 10. LintCode 1923. 最多可参加的会议数量 II
 * 11. HackerRank - Job Scheduling
 * 12. AtCoder ABC091D. Two Faced Edges
 * 13. 洛谷 P2051 [AHOI2009]中国象棋
 * 14. 牛客网 NC46. 加起来和为目标值的组合
 * 15. 杭电OJ 3572. Task Schedule
 * 16. POJ 3616. Milking Time
 * 17. UVa 10158. War
 * 18. CodeChef - MAXSEGMENTS
 * 19. SPOJ - BUSYMAN
 * 20. Project Euler 318. Cutting Game
 * 
 * 工程化考量：
 * 1. 在实际应用中，这类算法常用于：
 *    - 路径规划和导航系统
 *    - 游戏中的AI寻路算法
 *    - 机器人路径规划
 * 2. 实现优化：
 *    - 对于大规模数据，可以使用更高效的数据结构
 *    - 可以使用并行处理加速预处理过程
 *    - 对于多次查询，可以缓存中间结果
 * 3. 可扩展性：
 *    - 支持动态添加和删除节点
 *    - 处理多种移动规则
 *    - 扩展到多维空间问题
 * 4. 鲁棒性考虑：
 *    - 处理无效输入（如负距离、无效节点）
 *    - 处理大规模数据时的内存管理
 *    - 优化极端情况下的性能
 */
public class Code05_RoadTrip {

	public static int MAXN = 100002;

	public static int MAXP = 20;

	public static int[] arr = new int[MAXN];

	public static int[] to1 = new int[MAXN];

	public static int[] dist1 = new int[MAXN];

	public static int[] to2 = new int[MAXN];

	public static int[] dist2 = new int[MAXN];

	public static int[][] rank = new int[MAXN][2];

	public static int[] last = new int[MAXN];

	public static int[] next = new int[MAXN];

	// stto[i][p] : 从i位置出发，a和b轮流开2^p轮之后，车到达了几号点
	public static int[][] stto = new int[MAXN][MAXP + 1];

	// stdist[i][p] : 从i位置出发，a和b轮流开2^p轮之后，总距离是多少
	public static int[][] stdist = new int[MAXN][MAXP + 1];

	// sta[i][p] : 从i位置出发，a和b轮流开2^p轮之后，a行驶了多少距离
	public static int[][] sta = new int[MAXN][MAXP + 1];

	// stb[i][p] : 从i位置出发，a和b轮流开2^p轮之后，b行驶了多少距离
	public static int[][] stb = new int[MAXN][MAXP + 1];

	public static int n, m;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		near();
		st();
		in.nextToken();
		int x0 = (int) in.nval;
		out.println(best(x0));
		in.nextToken();
		m = (int) in.nval;
		for (int i = 1; i <= m; i++) {
			in.nextToken();
			int s = (int) in.nval;
			in.nextToken();
			int x = (int) in.nval;
			travel(s, x);
			out.println(a + " " + b);
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 预处理每个城市的第一近和第二近城市
	 */
	public static void near() {
		for (int i = 1; i <= n; i++) {
			rank[i][0] = i;
			rank[i][1] = arr[i];
		}
		Arrays.sort(rank, 1, n + 1, (a, b) -> a[1] - b[1]);
		rank[0][0] = 0;
		rank[n + 1][0] = 0;
		for (int i = 1; i <= n; i++) {
			last[rank[i][0]] = rank[i - 1][0];
			next[rank[i][0]] = rank[i + 1][0];
		}
		for (int i = 1; i <= n; i++) {
			to1[i] = 0;
			dist1[i] = 0;
			to2[i] = 0;
			dist2[i] = 0;
			update(i, last[i]);
			update(i, last[last[i]]);
			update(i, next[i]);
			update(i, next[next[i]]);
			delete(i);
		}
	}

	/**
	 * 更新城市i的最近和次近城市信息
	 * 
	 * @param i 城市编号
	 * @param j 可能的最近或次近城市编号
	 */
	public static void update(int i, int j) {
		if (j == 0) {
			return;
		}
		int dist = Math.abs(arr[i] - arr[j]);
		if (to1[i] == 0 || dist < dist1[i] || (dist == dist1[i] && arr[j] < arr[to1[i]])) {
			to2[i] = to1[i];
			dist2[i] = dist1[i];
			to1[i] = j;
			dist1[i] = dist;
		} else if (to2[i] == 0 || dist < dist2[i] || (dist == dist2[i] && arr[j] < arr[to2[i]])) {
			to2[i] = j;
			dist2[i] = dist;
		}
	}

	/**
	 * 删除双向链表中的指定节点
	 * 
	 * @param i 要删除的节点
	 */
	public static void delete(int i) {
		int l = last[i];
		int r = next[i];
		if (l != 0) {
			next[l] = r;
		}
		if (r != 0) {
			last[r] = l;
		}
	}

	/**
	 * 倍增预处理
	 */
	public static void st() {
		// 倍增初始化
		for (int i = 1; i <= n; i++) {
			// 一轮：a开到第二近，b开到第一近
			stto[i][0] = to1[to2[i]];
			stdist[i][0] = dist2[i] + dist1[to2[i]];
			sta[i][0] = dist2[i];
			stb[i][0] = dist1[to2[i]];
		}
		// 生成倍增表
		for (int p = 1; p <= MAXP; p++) {
			for (int i = 1; i <= n; i++) {
				stto[i][p] = stto[stto[i][p - 1]][p - 1];
				if (stto[i][p] != 0) {
					stdist[i][p] = stdist[i][p - 1] + stdist[stto[i][p - 1]][p - 1];
					sta[i][p] = sta[i][p - 1] + sta[stto[i][p - 1]][p - 1];
					stb[i][p] = stb[i][p - 1] + stb[stto[i][p - 1]][p - 1];
				}
			}
		}
	}

	/**
	 * 找到最优起点
	 * 
	 * @param x0 最大行驶距离
	 * @return 最优起点编号
	 */
	public static int best(int x0) {
		int ans = 0;
		double min = Double.MAX_VALUE;
		double cur;
		for (int i = 1; i < n; i++) {
			travel(i, x0);
			// cur这么设置更安全一些
			cur = b == 0 ? Double.MAX_VALUE : ((double) a / (double) b);
			if (ans == 0 || cur < min || (cur == min && arr[i] > arr[ans])) {
				ans = i;
				min = cur;
			}
		}
		return ans;
	}

	public static int a, b;

	/**
	 * 计算从城市s出发，最多行驶x距离时，a和b各自行驶的距离
	 * 
	 * @param s 起始城市
	 * @param x 最大行驶距离
	 */
	public static void travel(int s, int x) {
		a = 0;
		b = 0;
		for (int p = MAXP; p >= 0; p--) {
			if (stto[s][p] != 0 && x >= stdist[s][p]) {
				x -= stdist[s][p];
				a += sta[s][p];
				b += stb[s][p];
				s = stto[s][p];
			}
		}
		// 处理最后一步（如果a还能开）
		if (dist2[s] <= x) {
			a += dist2[s];
		}
	}

}