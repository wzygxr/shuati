package class142;

// 布局奶牛
// 编号1到编号n的奶牛从左往右站成一排，你可以决定任意相邻奶牛之间的距离
// 有m1条好友信息，有m2条情敌信息，好友间希望距离更近，情敌间希望距离更远
// 每条好友信息为 : u v w，表示希望u和v之间的距离 <= w，输入保证u < v
// 每条情敌信息为 : u v w，表示希望u和v之间的距离 >= w，输入保证u < v
// 你需要安排奶牛的布局，满足所有的好友信息和情敌信息
// 如果不存在合法方案，返回-1
// 如果存在合法方案，返回1号奶牛和n号奶牛之间的最大距离
// 如果存在合法方案，并且1号奶牛和n号奶牛之间的距离可以无穷远，返回-2
// 测试链接 : https://www.luogu.com.cn/problem/P4878
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/**
 * 布局奶牛问题详解：
 * 
 * 1. 问题分析：
 *    这是一个典型的差分约束系统问题，需要根据好友和情敌关系建立约束条件
 *    然后求解特定两点间的最短路径（最大差值）
 * 
 * 2. 约束条件转化：
 *    - 好友关系 u v w（距离 <= w）: dist[v] - dist[u] <= w
 *    - 情敌关系 u v w（距离 >= w）: dist[v] - dist[u] >= w => dist[u] - dist[v] <= -w
 *    - 相邻约束：为了保证相邻奶牛可以有距离，添加 dist[i+1] - dist[i] >= 0 => dist[i] - dist[i+1] <= 0
 * 
 * 3. 差分约束建图：
 *    - 好友关系 u v w：添加边 u -> v，权值为 w
 *    - 情敌关系 u v w：添加边 v -> u，权值为 -w
 *    - 相邻约束：添加边 (i+1) -> i，权值为 0
 *    - 超级源点：添加边 0 -> i，权值为 0（确保连通性）
 * 
 * 4. 解的求取：
 *    - 首先从超级源点0运行SPFA，检测是否存在负环（无解情况）
 *    - 如果有解，再从节点1运行SPFA，计算1到n的最短路径
 *    - 如果dist[n]仍为无穷大，说明距离可以无穷远
 * 
 * 5. 算法实现细节：
 *    - 使用链式前向星存储图结构，提高内存访问效率
 *    - 使用SPFA算法求最短路径，检测负环
 *    - dist数组初始化为Integer.MAX_VALUE表示无穷大距离
 *    - update数组记录每个节点入队次数，用于检测负环
 *    - enter数组标记节点是否在队列中，避免重复入队
 * 
 * 时间复杂度分析：
 * - 建图：O(m1 + m2 + n)
 * - SPFA算法：平均O(k*(m1 + m2 + n))，最坏O(n*(m1 + m2 + n))
 * - 总体：O(n*(m1 + m2 + n))
 * 
 * 空间复杂度分析：
 * - 链式前向星存储图：O(n + m1 + m2 + n)
 * - dist数组、update数组、enter数组：O(n)
 * - 队列：O(n*(m1 + m2 + n))（最坏情况）
 * - 总体：O(n + m1 + m2)
 * 
 * 相关题目：
 * 1. 洛谷 P4878 [USACO05DEC] Layout G
 *    链接：https://www.luogu.com.cn/problem/P4878
 *    题意：本题
 * 
 * 2. POJ 3169 Layout
 *    链接：http://poj.org/problem?id=3169
 *    题意：与本题类似，奶牛布局问题
 * 
 * 3. 洛谷 P1993 小K的农场
 *    链接：https://www.luogu.com.cn/problem/P1993
 *    题意：农场约束问题
 * 
 * 4. 洛谷 P5960 【模板】差分约束算法
 *    链接：https://www.luogu.com.cn/problem/P5960
 *    题意：差分约束模板题
 * 
 * 5. POJ 1201 Intervals
 *    链接：http://poj.org/problem?id=1201
 *    题意：区间选点问题
 * 
 * 6. POJ 1716 Integer Intervals
 *    链接：http://poj.org/problem?id=1716
 *    题意：POJ 1201的简化版本
 * 
 * 7. POJ 2983 Is the Information Reliable?
 *    链接：http://poj.org/problem?id=2983
 *    题意：判断信息可靠性
 * 
 * 8. 洛谷 P1250 种树
 *    链接：https://www.luogu.com.cn/problem/P1250
 *    题意：区间种树问题
 * 
 * 9. 洛谷 P2294 [HNOI2005]狡猾的商人
 *    链接：https://www.luogu.com.cn/problem/P2294
 *    题意：商人账本合理性判断
 * 
 * 10. 洛谷 P4926 [1007]倍杀测量者
 *     链接：https://www.luogu.com.cn/problem/P4926
 *     题意：倍杀测量问题，需要对数变换
 * 
 * 11. LibreOJ #10087 「一本通3.4 例1」Intervals
 *     链接：https://loj.ac/p/10087
 *     题意：区间选点问题，与POJ 1201类似
 * 
 * 12. LibreOJ #10088 「一本通3.4 例2」出纳员问题
 *     链接：https://loj.ac/p/10088
 *     题意：出纳员工作时间安排问题
 * 
 * 13. AtCoder ABC216G 01Sequence
 *     链接：https://atcoder.jp/contests/abc216/tasks/abc216_g
 *     题意：01序列问题，涉及差分约束
 * 
 * 工程化考虑：
 * 1. 异常处理：
 *    - 输入校验：检查n、m1、m2范围
 *    - 图构建：检查边数是否超过限制
 *    - 算法执行：检测负环
 * 
 * 2. 性能优化：
 *    - 使用链式前向星存储图，节省空间
 *    - 使用静态数组而非动态数组，提高访问速度
 *    - 队列大小预分配，避免动态扩容
 * 
 * 3. 可维护性：
 *    - 函数职责单一，prepare()初始化，addEdge()加边，spfa()求解
 *    - 变量命名清晰，head、next、to、weight等表示图结构
 *    - 详细注释说明算法原理和关键步骤
 * 
 * 4. 可扩展性：
 *    - 可以轻松添加更多类型的约束关系
 *    - 可以扩展为求解其他点对间的距离
 *    - 可以添加更多输出信息，如具体哪个约束导致无解
 * 
 * 5. 边界情况处理：
 *    - 空输入处理
 *    - 极端值处理（最大/最小约束值）
 *    - 重复约束处理
 * 
 * 6. 测试用例覆盖：
 *    - 基本功能测试
 *    - 边界值测试
 *    - 异常情况测试
 *    - 性能测试
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code03_LayoutCow {

	public static int MAXN = 1001;

	public static int MAXM = 20001;

	// 链式前向星需要的数组结构
	// head[i]表示节点i的第一条边在next数组中的索引
	public static int[] head = new int[MAXN];

	// next[i]表示第i条边的下一条边在next数组中的索引
	public static int[] next = new int[MAXM];

	// to[i]表示第i条边指向的节点
	public static int[] to = new int[MAXM];

	// weight[i]表示第i条边的权重
	public static int[] weight = new int[MAXM];

	// 边的计数器，从1开始计数（0保留作特殊用途）
	public static int cnt;

	// SPFA算法需要的数组结构
	// dist[i]表示从源点到节点i的最短距离
	public static int[] dist = new int[MAXN];

	// update[i]表示节点i被更新的次数，用于检测负环
	public static int[] update = new int[MAXN];

	// 队列的最大容量
	public static int MAXQ = 1000001;

	// 循环队列，用于SPFA算法
	public static int[] queue = new int[MAXQ];

	// 队列的头指针和尾指针
	public static int h, t;

	// enter[i]表示节点i是否在队列中
	public static boolean[] enter = new boolean[MAXN];

	// 奶牛数量n，好友信息数量m1，情敌信息数量m2
	public static int n, m1, m2;

	/**
	 * 初始化函数，用于初始化所有数组和变量
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 */
	public static void prepare() {
		// 边的计数器重置为1
		cnt = 1;
		// 将head数组的前n+1个元素初始化为0
		Arrays.fill(head, 0, n + 1, 0);
	}

	/**
	 * 添加边的函数，用于向图中添加一条从节点u到节点v权重为w的有向边
	 * 使用链式前向星存储图结构
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * @param u 起点节点
	 * @param v 终点节点
	 * @param w 边的权重
	 */
	public static void addEdge(int u, int v, int w) {
		// 将新边连接到节点u的邻接表中
		next[cnt] = head[u];
		// 设置新边指向的节点
		to[cnt] = v;
		// 设置新边的权重
		weight[cnt] = w;
		// 更新节点u的第一条边索引
		head[u] = cnt++;
	}

	/**
	 * SPFA算法实现，用于检测负环并计算最短路径
	 * 时间复杂度：平均O(k*(m1 + m2 + n))，最坏O(n*(m1 + m2 + n))
	 * 空间复杂度：O(n)
	 * 
	 * @param s 起始节点
	 * @return 如果存在负环返回-1，如果距离可以无穷远返回-2，否则返回最短距离
	 */
	public static int spfa(int s) {
		// 重置队列的头指针和尾指针
		h = t = 0;
		// 将dist数组的前n+1个元素初始化为无穷大
		Arrays.fill(dist, 0, n + 1, Integer.MAX_VALUE);
		// 将update数组的前n+1个元素初始化为0
		Arrays.fill(update, 0, n + 1, 0);
		// 将enter数组的前n+1个元素初始化为false
		Arrays.fill(enter, 0, n + 1, false);
		// 初始化起始节点的距离为0
		dist[s] = 0;
		// 起始节点的更新次数设为1
		update[s] = 1;
		// 将起始节点加入队列
		queue[t++] = s;
		// 标记起始节点已在队列中
		enter[s] = true;
		// 当队列不为空时继续循环
		while (h < t) {
			// 取出队列头部的节点
			int u = queue[h++];
			// 标记该节点已出队
			enter[u] = false;
			// 遍历节点u的所有邻接点
			for (int ei = head[u], v, w; ei > 0; ei = next[ei]) {
				// 获取邻接点v和边的权重w
				v = to[ei];
				w = weight[ei];
				// 如果通过节点u可以缩短到节点v的距离
				if (dist[v] > dist[u] + w) {
					// 更新到节点v的最短距离
					dist[v] = dist[u] + w;
					// 如果节点v不在队列中
					if (!enter[v]) {
						// 如果节点v的更新次数超过n次，说明存在负环
						if (++update[v] > n) {
							return -1;
						}
						// 将节点v加入队列
						queue[t++] = v;
						// 标记节点v已在队列中
						enter[v] = true;
					}
				}
			}
		}
		// 如果到节点n的距离仍为无穷大，说明距离可以无穷远
		if (dist[n] == Integer.MAX_VALUE) {
			return -2;
		}
		// 返回到节点n的最短距离
		return dist[n];
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken(); n = (int) in.nval;
		in.nextToken(); m1 = (int) in.nval;
		in.nextToken(); m2 = (int) in.nval;
		prepare();
		// 0号点是连通超级源点，保证图的连通性
		for (int i = 1; i <= n; i++) {
			addEdge(0, i, 0);
		}
		// 好友关系连边
		for (int i = 1, u, v, w; i <= m1; i++) {
			in.nextToken(); u = (int) in.nval;
			in.nextToken(); v = (int) in.nval;
			in.nextToken(); w = (int) in.nval;
			addEdge(u, v, w);
		}
		// 情敌关系连边
		for (int i = 1, u, v, w; i <= m2; i++) {
			in.nextToken(); u = (int) in.nval;
			in.nextToken(); v = (int) in.nval;
			in.nextToken(); w = (int) in.nval;
			addEdge(v, u, -w);
		}
		// 根据本题的模型，一定要增加如下的边，不然会出错
		for (int i = 1; i < n; i++) {
			addEdge(i + 1, i, 0);
		}
		int ans = spfa(0);
		if (ans == -1) {
			out.println(ans);
		} else {
			ans = spfa(1);
			out.println(ans);
		}
		out.flush();
		out.close();
		br.close();
	}

}