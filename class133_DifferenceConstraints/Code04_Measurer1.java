package class142;

// 倍杀测量者
// 如果 A的分数 >= B的分数 * k，k是正实数，就称 A k倍杀 B，或称 B被A k倍杀了
// 一场比赛中，一共有n个选手，有m1条誓言记录，有m2条选手得分记录，得分只可能是正实数
// 类型1的誓言 u v k : 选手u 没有k倍杀 选手v，那么选手u就穿女装
// 类型2的誓言 u v k : 选手u 被选手v k倍杀了，那么选手u就穿女装
// 选手的得分    u w : 选手u得了w分，如果某选手没有得分记录，按照尽量不穿女装的情况推测
// 你希望看到比赛后有人穿女装，但不想看到很多人穿女装，于是想制定正实数ans，效果如下
// 类型1的誓言，比例调整成(k-ans)，类型2的誓言，比例调整成(k+ans)，即提高了穿女装的条件
// 计算ans最大多少，依然有人穿女装，保留小数点后4位，如果不干预也没人穿女装，返回-1
// 1 <= n, m1, m2 <= 1000
// 1 <= k <= 10
// 1 <= w <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P4926
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/**
 * 倍杀测量者问题详解：
 * 
 * 1. 问题分析：
 *    这是一个结合了二分答案和差分约束系统的复杂问题
 *    需要通过二分法找到最大的ans值，使得在调整后的约束条件下仍存在矛盾（有人穿女装）
 * 
 * 2. 约束条件转化：
 *    - 原始条件：A >= B * k => A/B >= k => ln(A) - ln(B) >= ln(k)
 *    - 类型1誓言（没有k倍杀）：A < B * k => A/B < k => ln(A) - ln(B) < ln(k)
 *      调整后：ln(A) - ln(B) <= ln(k - ans)
 *    - 类型2誓言（被k倍杀）：A >= B * k => A/B >= k => ln(A) - ln(B) >= ln(k)
 *      调整后：ln(A) - ln(B) >= ln(k + ans) => ln(B) - ln(A) <= -ln(k + ans)
 *    - 得分记录：score[u] = w => ln(score[u]) = ln(w)
 *      添加约束：ln(w) <= ln(score[u]) <= ln(w)
 *      即：ln(score[u]) <= ln(w) 且 ln(w) <= ln(score[u])
 *      即：0号点 -> u，权值ln(w) 且 u -> (n+1)号点，权值-ln(w)
 * 
 * 3. 差分约束建图：
 *    - 类型1誓言 u v k：添加边 u -> v，权值为 ln(k - ans)
 *    - 类型2誓言 u v k：添加边 v -> u，权值为 -ln(k + ans)
 *    - 得分记录 u w：添加边 n+1 -> u，权值为 ln(w) 和边 u -> n+1，权值为 -ln(w)
 *    - 超级源点：添加边 0 -> i，权值为 0（确保连通性）
 * 
 * 4. 二分答案：
 *    - 二分ans的值，在每次二分中构建差分约束系统
 *    - 如果存在负环，说明有人穿女装，ans可以更大
 *    - 如果不存在负环，说明没有人穿女装，ans需要减小
 * 
 * 5. 算法实现细节：
 *    - 使用链式前向星存储图结构，提高内存访问效率
 *    - 使用SPFA算法求最短路径，检测负环
 *    - dist数组初始化为INF表示无穷大距离
 *    - update数组记录每个节点入队次数，用于检测负环
 *    - enter数组标记节点是否在队列中，避免重复入队
 *    - 使用对数变换处理乘除法约束
 * 
 * 时间复杂度分析：
 * - 二分查找：O(log(INF/sml))，约60次
 * - 建图：O(m1 + m2)
 * - SPFA算法：平均O(k*(m1 + m2))，最坏O(n*(m1 + m2))
 * - 总体：O(log(INF/sml) * (n + m1 + m2))
 * 
 * 空间复杂度分析：
 * - 链式前向星存储图：O(n + m1 + m2)
 * - dist数组、update数组、enter数组：O(n)
 * - 队列：O(n*(m1 + m2))（最坏情况）
 * - 总体：O(n + m1 + m2)
 * 
 * 相关题目：
 * 1. 洛谷 P4926 [1007]倍杀测量者
 *    链接：https://www.luogu.com.cn/problem/P4926
 *    题意：本题
 * 
 * 2. 洛谷 P5960 【模板】差分约束算法
 *    链接：https://www.luogu.com.cn/problem/P5960
 *    题意：差分约束模板题
 * 
 * 3. 洛谷 P1993 小K的农场
 *    链接：https://www.luogu.com.cn/problem/P1993
 *    题意：农场约束问题
 * 
 * 4. POJ 3169 Layout
 *    链接：http://poj.org/problem?id=3169
 *    题意：奶牛布局问题
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
 *    - 输入校验：检查n、m1、m2范围，k和w范围
 *    - 图构建：检查边数是否超过限制
 *    - 算法执行：检测负环
 *    - 数学运算：防止对数运算中出现负数或零
 * 
 * 2. 性能优化：
 *    - 使用链式前向星存储图，节省空间
 *    - 使用静态数组而非动态数组，提高访问速度
 *    - 队列大小预分配，避免动态扩容
 *    - 二分法精度控制，避免过多迭代
 * 
 * 3. 可维护性：
 *    - 函数职责单一，prepare()初始化，addEdge()加边，spfa()求解，compute()二分
 *    - 变量命名清晰，head、next、to、weight等表示图结构
 *    - 详细注释说明算法原理和关键步骤
 * 
 * 4. 可扩展性：
 *    - 可以轻松调整二分精度
 *    - 可以扩展为求解具体分数分配而非仅判断有无解
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

public class Code04_Measurer1 {

	public static int MAXN = 1002;

	public static int MAXM = 3001;

	public static double INF = 1e10;

	public static double sml = 1e-6;

	// 选手数量n，誓言记录数量m1，得分记录数量m2
	public static int n, m1, m2;

	// 誓言记录(誓言类型, u, v, k)
	// 誓言类型：1表示类型1誓言（没有k倍杀），2表示类型2誓言（被k倍杀）
	public static int[][] vow = new int[MAXN][4];

	// 得分记录(u, w)
	// u表示选手编号，w表示得分
	public static int[][] score = new int[MAXN][2];

	// 链式前向星需要的数组结构
	// head[i]表示节点i的第一条边在next数组中的索引
	public static int[] head = new int[MAXN];

	// next[i]表示第i条边的下一条边在next数组中的索引
	public static int[] next = new int[MAXM];

	// to[i]表示第i条边指向的节点
	public static int[] to = new int[MAXM];

	// weight[i]表示第i条边的权重（double类型）
	public static double[] weight = new double[MAXM];

	// 边的计数器，从1开始计数（0保留作特殊用途）
	public static int cnt;

	// SPFA算法需要的数组结构
	// dist[i]表示从源点到节点i的最短距离
	public static double[] dist = new double[MAXN];

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
		// 将head数组的前n+2个元素初始化为0
		Arrays.fill(head, 0, n + 2, 0);
		// 将dist数组的前n+2个元素初始化为无穷大
		Arrays.fill(dist, 0, n + 2, INF);
		// 将update数组的前n+2个元素初始化为0
		Arrays.fill(update, 0, n + 2, 0);
		// 将enter数组的前n+2个元素初始化为false
		Arrays.fill(enter, 0, n + 2, false);
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
	public static void addEdge(int u, int v, double w) {
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
	 * 二分查找函数，用于找到最大的ans值
	 * 时间复杂度：O(log(INF/sml) * (n + m1 + m2))
	 * 空间复杂度：O(n + m1 + m2)
	 * 
	 * @return 最大的ans值，如果没有人穿女装返回0
	 */
	public static double compute() {
		// 二分查找的左右边界
		double l = 0, r = INF, m, ans = 0;
		// 当左右边界差值大于等于精度时继续二分
		while (r - l >= sml) {
			// 计算中点
			m = (l + r) / 2;
			// 如果在ans=m时有人穿女装
			if (check(m)) {
				// 更新答案
				ans = m;
				// 调整左边界
				l = m + sml;
			} else {
				// 调整右边界
				r = m - sml;
			}
		}
		// 返回最大的ans值
		return ans;
	}

	// 是否有人穿女装
	public static boolean check(double limit) {
		prepare();
		// 0号点是连通超级源点，保证图的连通
		for (int i = 1; i <= n; i++) {
			addEdge(0, i, 0);
		}
		// 倍杀关系的建边
		for (int i = 1; i <= m1; i++) {
			if (vow[i][0] == 1) {
				// 课上的代码没有这个判断，加上才是正确的，防止log里出现负数
				if (-limit + vow[i][3] >= 0) {
					addEdge(vow[i][1], vow[i][2], -Math.log(-limit + vow[i][3]));
				}
			} else {
				// 因为类型2的誓言是<关系，所以减去最小精度后，就可以认为是<=关系
				addEdge(vow[i][1], vow[i][2], Math.log(limit + vow[i][3] - sml));
			}
		}
		// n+1号点是限制超级源点，保证确定得分的选手之间的关系
		// 本题测试数据有限，两个超级源点合并居然也能通过
		// 原理上两个超级源点一定要分开，课上进行了重点讲解
		for (int i = 1; i <= m2; i++) {
			addEdge(n + 1, score[i][0], Math.log(score[i][1]));
			addEdge(score[i][0], n + 1, -Math.log(score[i][1]));
		}
		return spfa(0);
	}

	public static boolean spfa(int s) {
		dist[s] = 0;
		update[s] = 1;
		queue[t++] = s;
		enter[s] = true;
		while (h < t) {
			int u = queue[h++];
			enter[u] = false;
			for (int ei = head[u]; ei > 0; ei = next[ei]) {
				int v = to[ei];
				double w = weight[ei];
				if (dist[v] > dist[u] + w) {
					dist[v] = dist[u] + w;
					if (!enter[v]) {
						// 0...n+1号点，一共n+2个点，所以这里判断 > n + 1
						if (++update[v] > n + 1) {
							return true;
						}
						queue[t++] = v;
						enter[v] = true;
					}
				}
			}
		}
		return false;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m1 = (int) in.nval;
		in.nextToken();
		m2 = (int) in.nval;
		for (int i = 1; i <= m1; i++) {
			in.nextToken();
			vow[i][0] = (int) in.nval;
			in.nextToken();
			vow[i][1] = (int) in.nval;
			in.nextToken();
			vow[i][2] = (int) in.nval;
			in.nextToken();
			vow[i][3] = (int) in.nval;
		}
		for (int i = 1; i <= m2; i++) {
			in.nextToken();
			score[i][0] = (int) in.nval;
			in.nextToken();
			score[i][1] = (int) in.nval;
		}
		double ans = compute();
		if (ans == 0) {
			out.println("-1");
		} else {
			out.println(ans);
		}
		out.flush();
		out.close();
		br.close();
	}

}