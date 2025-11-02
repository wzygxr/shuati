package class142;

// 小k的农场
// 一共有n个农场，编号1~n，给定m条关系，每条关系是如下三种形式中的一种
// 关系1 a b c : 表示农场a比农场b至少多种植了c个作物
// 关系2 a b c : 表示农场a比农场b至多多种植了c个作物
// 关系3 a b   : 表示农场a和农场b种植了一样多的作物
// 如果关系之间能推出矛盾，打印"No"，不存在矛盾，打印"Yes"
// 1 <= n、m <= 5 * 10^3
// 1 <= c <= 5 * 10^3
// 测试链接 : https://www.luogu.com.cn/problem/P1993
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/**
 * 小K的农场问题详解：
 * 
 * 1. 问题分析：
 *    这是一个典型的差分约束系统问题，需要将自然语言描述的约束条件转化为数学不等式
 *    然后通过图论方法判断是否有解
 * 
 * 2. 约束条件转化：
 *    - 关系1: 农场a比农场b至少多种植了c个作物 => a - b >= c => b - a <= -c
 *    - 关系2: 农场a比农场b至多多种植了c个作物 => a - b <= c
 *    - 关系3: 农场a和农场b种植了一样多的作物 => a - b = 0 => a - b <= 0 且 b - a <= 0
 * 
 * 3. 差分约束建图：
 *    - 关系1: 添加边 b -> a，权值为 -c
 *    - 关系2: 添加边 a -> b，权值为 c
 *    - 关系3: 添加边 a -> b，权值为 0 和边 b -> a，权值为 0
 * 
 * 4. 超级源点：
 *    为了确保图的连通性，添加超级源点0，向所有节点连权值为0的边
 * 
 * 5. 解的存在性判断：
 *    使用SPFA算法检测负环，如果存在负环则无解，否则有解
 * 
 * 6. 算法实现细节：
 *    - 使用链式前向星存储图结构，提高内存访问效率
 *    - 使用SPFA算法求最短路径，检测负环
 *    - dist数组初始化为Integer.MAX_VALUE表示无穷大距离
 *    - update数组记录每个节点入队次数，用于检测负环
 *    - enter数组标记节点是否在队列中，避免重复入队
 * 
 * 时间复杂度分析：
 * - 建图：O(m)，其中m是关系数量
 * - SPFA算法：平均O(k*m)，最坏O(n*m)，其中k是常数，n是农场数量
 * - 总体：O(n + m)
 * 
 * 空间复杂度分析：
 * - 链式前向星存储图：O(n + m)
 * - dist数组、update数组、enter数组：O(n)
 * - 队列：O(n*m)（最坏情况）
 * - 总体：O(n + m)
 * 
 * 相关题目：
 * 1. 洛谷 P1993 小K的农场
 *    链接：https://www.luogu.com.cn/problem/P1993
 *    题意：本题
 * 
 * 2. 洛谷 P5960 【模板】差分约束算法
 *    链接：https://www.luogu.com.cn/problem/P5960
 *    题意：差分约束模板题
 * 
 * 3. POJ 1201 Intervals
 *    链接：http://poj.org/problem?id=1201
 *    题意：区间选点问题
 * 
 * 4. POJ 1716 Integer Intervals
 *    链接：http://poj.org/problem?id=1716
 *    题意：POJ 1201的简化版本
 * 
 * 5. POJ 2983 Is the Information Reliable?
 *    链接：http://poj.org/problem?id=2983
 *    题意：判断信息可靠性
 * 
 * 6. 洛谷 P1250 种树
 *    链接：https://www.luogu.com.cn/problem/P1250
 *    题意：区间种树问题
 * 
 * 7. 洛谷 P2294 [HNOI2005]狡猾的商人
 *    链接：https://www.luogu.com.cn/problem/P2294
 *    题意：商人账本合理性判断
 * 
 * 8. 洛谷 P4926 [1007]倍杀测量者
 *    链接：https://www.luogu.com.cn/problem/P4926
 *    题意：倍杀测量问题，需要对数变换
 * 
 * 9. 洛谷 P3275 [SCOI2011]糖果
 *    链接：https://www.luogu.com.cn/problem/P3275
 *    题意：分糖果问题
 * 
 * 10. POJ 3169 Layout
 *     链接：http://poj.org/problem?id=3169
 *     题意：奶牛布局问题
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
 *    - 输入校验：检查n、m范围，c范围
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
 *    - 可以扩展为求解具体数值而非仅判断有无解
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

public class Code02_KsFarm {

	public static int MAXN = 5001;

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
	public static int MAXQ = 20000001;

	// 循环队列，用于SPFA算法
	public static int[] queue = new int[MAXQ];

	// 队列的头指针和尾指针
	public static int h, t;

	// enter[i]表示节点i是否在队列中
	public static boolean[] enter = new boolean[MAXN];

	// 农场数量n和关系数量m
	public static int n, m;

	/**
	 * 初始化函数，用于初始化所有数组和变量
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 */
	public static void prepare() {
		// 边的计数器重置为1
		cnt = 1;
		// 队列的头指针和尾指针重置为0
		h = t = 0;
		// 将head数组的前n+1个元素初始化为0
		Arrays.fill(head, 0, n + 1, 0);
		// 所有距离先设置成最大值，表示不可达
		Arrays.fill(dist, 0, n + 1, Integer.MAX_VALUE);
		// 将update数组的前n+1个元素初始化为0
		Arrays.fill(update, 0, n + 1, 0);
		// 将enter数组的前n+1个元素初始化为false
		Arrays.fill(enter, 0, n + 1, false);
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
	 * 时间复杂度：平均O(k*m)，最坏O(n*m)，其中k是常数
	 * 空间复杂度：O(n)
	 * 
	 * @param s 起始节点（超级源点）
	 * @return 如果存在负环返回true，否则返回false
	 */
	public static boolean spfa(int s) {
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
							return true;
						}
						// 将节点v加入队列
						queue[t++] = v;
						// 标记节点v已在队列中
						enter[v] = true;
					}
				}
			}
		}
		// 不存在负环
		return false;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		prepare();
		for (int i = 1; i <= n; i++) {
			addEdge(0, i, 0);
		}
		for (int i = 1, type, u, v, w; i <= m; i++) {
			in.nextToken(); type = (int) in.nval;
			in.nextToken(); u = (int) in.nval;
			in.nextToken(); v = (int) in.nval;
			if (type == 1) {
				in.nextToken();
				w = (int) in.nval;
				addEdge(u, v, -w);
			} else if (type == 2) {
				in.nextToken();
				w = (int) in.nval;
				addEdge(v, u, w);
			} else {
				addEdge(u, v, 0);
				addEdge(v, u, 0);
			}
		}
		if (spfa(0)) {
			out.println("No");
		} else {
			out.println("Yes");
		}
		out.flush();
		out.close();
		br.close();
	}

}