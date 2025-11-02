package class142;

// 负环和差分约束模版题(转化成形式2进而转化成判断无限增加的环)
// 一共有n个变量，编号1~n，给定m个不等式，每个不等式的形式为
// Xi - Xj <= Ci，其中Xi和Xj为变量，Ci为常量
// 如果不等式存在矛盾导致无解，打印"NO"
// 如果有解，打印满足所有不等式的其中一组解(X1, X2...)
// 1 <= n、m <= 5 * 10^3
// -10^4 <= Ci <= +10^4 
// 测试链接 : https://www.luogu.com.cn/problem/P5960
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/**
 * 差分约束系统详解（形式2 - 最长路解法）：
 * 
 * 1. 问题定义：
 *    差分约束系统是一种特殊的n元一次不等式组，包含n个变量x1, x2, ..., xn
 *    和m个约束条件，每个约束条件形如 xi - xj <= ck，其中ck是常量。
 *    目标是求出一组解使得所有约束条件都满足，或者判断无解。
 * 
 * 2. 核心思想：
 *    与Code01_DifferenceConstraints1相同，都是将差分约束系统转化为图论问题
 *    但这里使用最长路径算法而非最短路径算法
 * 
 * 3. 转化原理：
 *    差分约束 xi - xj <= ck 可以变形为 xi <= xj + ck
 *    这与最短路径中的三角不等式 dist[v] <= dist[u] + w(u,v) 对应
 *    也可以变形为 xj >= xi - ck
 *    这与最长路径中的三角不等式 dist[v] >= dist[u] + w(u,v) 对应
 * 
 * 4. 解的存在性：
 *    如果图中存在正环（权重和为正的环），则差分约束系统无解
 *    否则，从超级源点到各点的最长距离就是一组可行解
 * 
 * 5. 超级源点：
 *    为了确保图的连通性，添加一个超级源点0，向所有变量节点连权值为0的边
 *    这相当于添加约束 xi - x0 >= 0，即 xi >= x0
 * 
 * 6. 解的特性：
 *    如果{x1, x2, ..., xn}是一组解，那么{x1+d, x2+d, ..., xn+d}也是一组解
 *    因为做差后常数d会被消掉
 * 
 * 7. 算法实现细节：
 *    - 使用链式前向星存储图结构，提高内存访问效率
 *    - 使用SPFA算法求最长路径，检测正环
 *    - dist数组初始化为Integer.MIN_VALUE表示无穷小距离
 *    - update数组记录每个节点入队次数，用于检测正环
 *    - enter数组标记节点是否在队列中，避免重复入队
 * 
 * 时间复杂度分析：
 * - 建图：O(m)，其中m是约束条件数量
 * - SPFA算法：平均O(k*m)，最坏O(n*m)，其中k是常数，n是变量数量
 * - 总体：O(n + m)
 * 
 * 空间复杂度分析：
 * - 链式前向星存储图：O(n + m)
 * - dist数组、update数组、enter数组：O(n)
 * - 队列：O(n*m)（最坏情况）
 * - 总体：O(n + m)
 * 
 * 与Code01_DifferenceConstraints1的对比：
 * 1. 建图方式不同：
 *    - Code01: xi - xj <= ck => 添加边 j -> i，权值为 ck
 *    - Code02: xi - xj <= ck => 添加边 i -> j，权值为 -ck
 * 
 * 2. 算法不同：
 *    - Code01: 使用最短路径算法，松弛条件为 dist[v] > dist[u] + w
 *    - Code02: 使用最长路径算法，松弛条件为 dist[v] < dist[u] + w
 * 
 * 3. 初始化不同：
 *    - Code01: dist数组初始化为Integer.MAX_VALUE
 *    - Code02: dist数组初始化为Integer.MIN_VALUE
 * 
 * 4. 解的含义不同：
 *    - Code01: 解为从超级源点到各点的最短距离
 *    - Code02: 解为从超级源点到各点的最长距离
 * 
 * 相关题目：
 * 1. 洛谷 P5960 【模板】差分约束算法
 *    链接：https://www.luogu.com.cn/problem/P5960
 *    题意：标准的差分约束模板题
 * 
 * 2. POJ 1201 Intervals
 *    链接：http://poj.org/problem?id=1201
 *    题意：给定多个区间和每个区间内至少需要选择的整数个数，求满足条件的最少整数个数
 * 
 * 3. POJ 1716 Integer Intervals
 *    链接：http://poj.org/problem?id=1716
 *    题意：POJ 1201的简化版本
 * 
 * 4. POJ 2983 Is the Information Reliable?
 *    链接：http://poj.org/problem?id=2983
 *    题意：判断给定的信息是否一致
 * 
 * 5. POJ 3169 Layout
 *    链接：http://poj.org/problem?id=3169
 *    题意：奶牛排队问题，求1号和n号奶牛的最大距离
 * 
 * 6. 洛谷 P1993 小K的农场
 *    链接：https://www.luogu.com.cn/problem/P1993
 *    题意：农场作物数量约束问题
 * 
 * 7. 洛谷 P1250 种树
 *    链接：https://www.luogu.com.cn/problem/P1250
 *    题意：区间种树问题
 * 
 * 8. 洛谷 P2294 [HNOI2005]狡猾的商人
 *    链接：https://www.luogu.com.cn/problem/P2294
 *    题意：判断商人的账本是否合理
 * 
 * 9. 洛谷 P4926 [1007]倍杀测量者
 *    链接：https://www.luogu.com.cn/problem/P4926
 *    题意：倍杀测量问题，需要使用对数变换
 * 
 * 10. 洛谷 P3275 [SCOI2011]糖果
 *     链接：https://www.luogu.com.cn/problem/P3275
 *     题意：分糖果问题
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
 *    - 输入校验：检查n、m范围，Ci范围
 *    - 图构建：检查边数是否超过限制
 *    - 算法执行：检测正环
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
 *    - 可以轻松修改为求最短路径（处理<=约束）
 *    - 可以扩展支持更多类型的约束条件
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

public class Code01_DifferenceConstraints2 {

	public static int MAXN = 5001;

	public static int MAXM = 10001;

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
	// dist[i]表示从源点到节点i的最长距离
	public static int[] dist = new int[MAXN];

	// update[i]表示节点i被更新的次数，用于检测正环
	public static int[] update = new int[MAXN];

	// 队列的最大容量
	public static int MAXQ = 5000001;

	// 循环队列，用于SPFA算法
	public static int[] queue = new int[MAXQ];

	// 队列的头指针和尾指针
	public static int h, t;

	// enter[i]表示节点i是否在队列中
	public static boolean[] enter = new boolean[MAXN];

	// 变量数量n和约束条件数量m
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
		// 所有距离先设置成最小值，表示不可达
		Arrays.fill(dist, 0, n + 1, Integer.MIN_VALUE);
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
	 * SPFA算法实现，用于检测正环并计算最长路径
	 * 时间复杂度：平均O(k*m)，最坏O(n*m)，其中k是常数
	 * 空间复杂度：O(n)
	 * 
	 * @param s 起始节点（超级源点）
	 * @return 如果存在正环返回true，否则返回false
	 */
	// 来自讲解065，spfa判断无限增加环，s是超级源点
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
				// 如果通过节点u可以增加到节点v的距离（最长路径松弛）
				if (dist[v] < dist[u] + w) { // 变大才更新
					// 更新到节点v的最长距离
					dist[v] = dist[u] + w;
					// 如果节点v不在队列中
					if (!enter[v]) {
						// 注意判断逻辑和讲解065的代码不一样
						// 因为节点0是额外增加的超级源点
						// 所以节点数量增加了1个，所以这么判断
						if (++update[v] > n) {
							// 如果节点v的更新次数超过n次，说明存在正环
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
		// 不存在正环
		return false;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken(); n = (int) in.nval;
		in.nextToken(); m = (int) in.nval;
		prepare();
		// 0号点是连通超级源点，保证图的连通性
		for (int i = 1; i <= n; i++) {
			addEdge(0, i, 0);
		}
		for (int i = 1, u, v, w; i <= m; i++) {
			in.nextToken(); u = (int) in.nval;
			in.nextToken(); v = (int) in.nval;
			in.nextToken(); w = (int) in.nval;
			// 形式2的连边方式
			addEdge(u, v, -w);
		}
		if (spfa(0)) {
			out.println("NO");
		} else {
			for (int i = 1; i <= n; i++) {
				out.print(dist[i] + " ");
			}
			out.println();
		}
		out.flush();
		out.close();
		br.close();
	}

}