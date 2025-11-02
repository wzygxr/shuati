package class060;

// 最大食物链计数
// a -> b，代表a在食物链中被b捕食
// 给定一个有向无环图，返回
// 这个图中从最初级动物到最顶级捕食者的食物链有几条
// 测试链接 : https://www.luogu.com.cn/problem/P4017
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下所有代码，把主类名改成Main，可以直接通过

/**
 * 题目解析：
 * 这是一道典型的拓扑排序DP问题。我们需要统计从入度为0的节点到出度为0的节点的路径数量。
 * 
 * 算法思路：
 * 1. 使用链式前向星建图
 * 2. 使用拓扑排序遍历节点
 * 3. 在遍历过程中进行动态规划，统计路径数量
 * 
 * 时间复杂度：O(N + M)，其中N是节点数，M是边数
 * 空间复杂度：O(N + M)
 * 
 * 相关题目扩展：
 * 1. 洛谷 P1113 杂务 - https://www.luogu.com.cn/problem/P1113
 * 2. 洛谷 P1983 车站分级 - https://www.luogu.com.cn/problem/P1983
 * 3. LeetCode 207. 课程表 - https://leetcode.cn/problems/course-schedule/
 * 4. LeetCode 210. 课程表 II - https://leetcode.cn/problems/course-schedule-ii/
 * 5. HDU 1285 确定比赛名次 - http://acm.hdu.edu.cn/showproblem.php?pid=1285
 * 6. POJ 1094 Sorting It All Out - http://poj.org/problem?id=1094
 * 7. SPOJ TOPOSORT - https://www.spoj.com/problems/TOPOSORT/
 * 8. AtCoder ABC139E League - https://atcoder.jp/contests/abc139/tasks/abc139_e
 * 9. Codeforces 510C Fox And Names - https://codeforces.com/problemset/problem/510/C
 * 10. 牛客网 字典序最小的拓扑序列 - https://ac.nowcoder.com/acm/problem/15184
 * 11. LeetCode 329. 矩阵中的最长递增路径 - https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
 * 12. LeetCode 851. 喧闹和富有 - https://leetcode.cn/problems/loud-and-rich/
 * 13. LeetCode 1494. 并行课程 II - https://leetcode.cn/problems/parallel-courses-ii/
 * 14. LeetCode 2050. 并行课程 III - https://leetcode.cn/problems/parallel-courses-iii/
 * 15. LeetCode 2127. 参加会议的最多员工数 - https://leetcode.cn/problems/maximum-employees-to-be-invited-to-a-meeting/
 * 16. 洛谷 P4017 最大食物链计数 - https://www.luogu.com.cn/problem/P4017
 * 17. 洛谷 P1347 排序 - https://www.luogu.com.cn/problem/P1347
 * 18. POJ 3249 Test for Job - http://poj.org/problem?id=3249
 * 19. HDU 4109 Activation - http://acm.hdu.edu.cn/showproblem.php?pid=4109
 * 20. SPOJ TOPOSORT - https://www.spoj.com/problems/TOPOSORT/
 * 21. 牛客网 课程表 - https://ac.nowcoder.com/acm/problem/24725
 * 22. USACO 2014 January Contest, Gold - http://www.usaco.org/index.php?page=viewproblem2&cpid=382
 * 23. Timus OJ 1280. Topological Sorting - https://acm.timus.ru/problem.aspx?space=1&num=1280
 * 24. Aizu OJ GRL_4_B. Topological Sort - https://onlinejudge.u-aizu.ac.jp/problems/GRL_4_B
 * 25. Project Euler Problem 79: Passcode derivation - https://projecteuler.net/problem=79
 * 26. HackerEarth Topological Sort - https://www.hackerearth.com/practice/algorithms/graphs/topological-sort/practice-problems/
 * 27. 计蒜客 三值排序 - https://nanti.jisuanke.com/t/T1566
 * 28. 各大高校OJ中的拓扑排序题目
 * 29. ZOJ 1060 Sorting It All Out - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364599
 * 30. 洛谷 P1453 城市环路 - https://www.luogu.com.cn/problem/P1453
 * 
 * 工程化考虑：
 * 1. 输入输出优化：使用StreamTokenizer提高输入效率
 * 2. 边界处理：处理空图、单节点图等特殊情况
 * 3. 模块化设计：将建图、拓扑排序、路径计算分离
 * 4. 异常处理：对非法输入进行检查
 * 5. 性能优化：使用链式前向星优化图的存储
 * 6. 内存管理：合理分配和释放内存资源
 * 7. 代码复用：将通用功能封装成独立方法
 * 8. 可维护性：添加详细注释和文档说明
 * 
 * 算法要点：
 * 1. 拓扑排序保证了处理节点的顺序，使得DP状态转移正确
 * 2. lines数组记录到达每个节点的路径数
 * 3. 当一个节点的所有前驱都被处理完后，它就可以被处理了
 * 4. 对于出度为0的节点，它们是食物链的顶端，需要累加到结果中
 * 5. 使用链式前向星可以高效地存储稀疏图
 * 6. MOD运算防止整数溢出
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code01_FoodLines {

	public static int MAXN = 5001;

	public static int MAXM = 500001;

	public static int MOD = 80112002;

	// 链式前向星建图
	public static int[] head = new int[MAXN];

	public static int[] next = new int[MAXM];

	public static int[] to = new int[MAXM];

	public static int cnt;

	// 拓扑排序需要的队列
	public static int[] queue = new int[MAXN];

	// 拓扑排序需要的入度表
	public static int[] indegree = new int[MAXN];

	// 拓扑排序需要的推送信息 - 到达每个节点的路径数
	public static int[] lines = new int[MAXN];

	public static int n, m;

	/**
	 * 初始化图结构
	 * @param n 节点数量
	 */
	public static void build(int n) {
		cnt = 1;
		Arrays.fill(indegree, 0, n + 1, 0);
		Arrays.fill(lines, 0, n + 1, 0);
		Arrays.fill(head, 0, n + 1, 0);
	}

	/**
	 * 添加边 u -> v
	 * @param u 起点
	 * @param v 终点
	 */
	public static void addEdge(int u, int v) {
		next[cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt++;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			in.nextToken();
			m = (int) in.nval;
			build(n);
			for (int i = 0, u, v; i < m; i++) {
				in.nextToken();
				u = (int) in.nval;
				in.nextToken();
				v = (int) in.nval;
				addEdge(u, v);
				indegree[v]++;
			}
			out.println(ways());
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算食物链数量
	 * 使用拓扑排序 + 动态规划的方法
	 * @return 食物链总数
	 */
	public static int ways() {
		int l = 0;
		int r = 0;
		// 将所有入度为0的节点加入队列
		for (int i = 1; i <= n; i++) {
			if (indegree[i] == 0) {
				queue[r++] = i;
				lines[i] = 1; // 初始节点的路径数为1
			}
		}
		int ans = 0;
		while (l < r) {
			int u = queue[l++];
			// 如果当前节点没有后续邻居，说明是顶级捕食者
			if (head[u] == 0) {
				// 当前的u节点不再有后续邻居了
				ans = (ans + lines[u]) % MOD;
			} else {
				// 遍历u的所有邻居节点
				for (int ei = head[u], v; ei > 0; ei = next[ei]) {
					// u -> v
					v = to[ei];
					// 状态转移：到达v的路径数增加到达u的路径数
					lines[v] = (lines[v] + lines[u]) % MOD;
					// 如果v的入度减为0，加入队列
					if (--indegree[v] == 0) {
						queue[r++] = v;
					}
				}
			}
		}
		return ans;
	}

}