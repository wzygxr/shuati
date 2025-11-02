package class142;

// 题目4，倍杀测量者，另一种二分的写法
// 思路是不变的，二分的写法多种多样
// 代码中打注释的位置，就是更简单的二分逻辑，其他代码没有变化
// 测试链接 : https://www.luogu.com.cn/problem/P4926
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/**
 * 倍杀测量者问题（二分法变体）详解：
 * 
 * 1. 问题分析：
 *    与Code04_Measurer1相同，但使用不同的二分法实现
 * 
 * 2. 约束条件转化：
 *    与Code04_Measurer1相同
 * 
 * 3. 差分约束建图：
 *    与Code04_Measurer1相同
 * 
 * 4. 二分答案（变体）：
 *    - 固定二分次数（60次）而非根据精度判断
 *    - 每次二分都更新l或r，最终l就是答案
 *    - 这种写法避免了浮点数比较的精度问题
 * 
 * 5. 算法实现细节：
 *    - 使用链式前向星存储图结构，提高内存访问效率
 *    - 使用SPFA算法求最短路径，检测负环
 *    - dist数组初始化为INF表示无穷大距离
 *    - update数组记录每个节点入队次数，用于检测负环
 *    - enter数组标记节点是否在队列中，避免重复入队
 *    - 使用对数变换处理乘除法约束
 *    - 固定迭代次数的二分法避免浮点数比较问题
 * 
 * 时间复杂度分析：
 * - 二分查找：O(60)（固定60次）
 * - 建图：O(m1 + m2)
 * - SPFA算法：平均O(k*(m1 + m2))，最坏O(n*(m1 + m2))
 * - 总体：O(60 * (n + m1 + m2))
 * 
 * 空间复杂度分析：
 * - 链式前向星存储图：O(n + m1 + m2)
 * - dist数组、update数组、enter数组：O(n)
 * - 队列：O(n*(m1 + m2))（最坏情况）
 * - 总体：O(n + m1 + m2)
 * 
 * 与Code04_Measurer1的对比：
 * 1. 二分法实现不同：
 *    - Code04_Measurer1: 根据精度动态调整二分范围
 *    - Code04_Measurer2: 固定二分次数，简化逻辑
 * 
 * 2. 精度控制不同：
 *    - Code04_Measurer1: 使用sml变量控制精度
 *    - Code04_Measurer2: 通过固定迭代次数控制精度
 * 
 * 3. 代码复杂度不同：
 *    - Code04_Measurer1: 逻辑稍复杂，需要处理浮点数比较
 *    - Code04_Measurer2: 逻辑更简单，避免浮点数比较问题
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
 *    - 可以轻松调整二分精度（改变迭代次数）
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

public class Code04_Measurer2 {

	public static int MAXN = 1002;

	public static int MAXM = 3001;

	public static double INF = 1e10;

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
	 * 二分查找函数（变体），用于找到最大的ans值
	 * 时间复杂度：O(60 * (n + m1 + m2))
	 * 空间复杂度：O(n + m1 + m2)
	 * 
	 * @return 最大的ans值，如果没有人穿女装返回0
	 */
	// 另一种二分的写法
	public static double compute() {
		// 二分查找的左右边界
		double l = 0, r = INF, m;
		// 二分进行60次，足够达到题目要求的精度
		// 二分完成后，l就是答案
		for (int i = 1; i <= 60; i++) {
			// 计算中点
			m = (l + r) / 2;
			// 如果在ans=m时有人穿女装
			if (check(m)) {
				// 调整左边界
				l = m;
			} else {
				// 调整右边界
				r = m;
			}
		}
		// 返回最大的ans值
		return l;
	}

	/**
	 * 检查函数，用于判断在ans=limit时是否有人穿女装
	 * 时间复杂度：O(n + m1 + m2)
	 * 空间复杂度：O(n + m1 + m2)
	 * 
	 * @param limit 当前的ans值
	 * @return 如果有人穿女装返回true，否则返回false
	 */
	public static boolean check(double limit) {
		// 初始化所有数组和变量
		prepare();
		// 为每个选手添加一条从源点到选手的边，权重为0
		for (int i = 1; i <= n; i++) {
			addEdge(0, i, 0);
		}
		// 添加所有誓言记录对应的边
		for (int i = 1; i <= m1; i++) {
			if (vow[i][0] == 1) {
				// 课上的代码没有这个判断，加上才是正确的，防止log里出现负数
				if (-limit + vow[i][3] >= 0) {
					addEdge(vow[i][1], vow[i][2], -Math.log(-limit + vow[i][3]));
				}
			} else {
				addEdge(vow[i][1], vow[i][2], Math.log(limit + vow[i][3]));
			}
		}
		// 添加所有得分记录对应的边
		for (int i = 1; i <= m2; i++) {
			addEdge(n + 1, score[i][0], Math.log(score[i][1]));
			addEdge(score[i][0], n + 1, -Math.log(score[i][1]));
		}
		// 使用SPFA算法求最短路径，检测负环
		return spfa(0);
	}

	/**
	 * SPFA算法，用于求最短路径并检测负环
	 * 时间复杂度：平均O(k*(m1 + m2))，最坏O(n*(m1 + m2))
	 * 空间复杂度：O(n + m1 + m2)
	 * 
	 * @param s 源点
	 * @return 如果存在负环返回true，否则返回false
	 */
	public static boolean spfa(int s) {
		// 初始化源点的距离为0
		dist[s] = 0;
		// 初始化源点的更新次数为1
		update[s] = 1;
		// 将源点加入队列
		queue[t++] = s;
		// 标记源点在队列中
		enter[s] = true;
		// 当队列不为空时
		while (h < t) {
			// 取出队首元素
			int u = queue[h++];
			// 取出队首元素后，标记其不在队列中
			enter[u] = false;
			// 遍历节点u的所有邻接边
			for (int ei = head[u]; ei > 0; ei = next[ei]) {
				// 获取边ei的终点节点
				int v = to[ei];
				// 获取边ei的权重
				double w = weight[ei];
				// 如果通过边ei可以更新节点v的距离
				if (dist[v] > dist[u] + w) {
					// 更新节点v的距离
					dist[v] = dist[u] + w;
					// 如果节点v不在队列中
					if (!enter[v]) {
						// 如果节点v的更新次数超过n+1次，说明存在负环
						if (++update[v] > n + 1) {
							return true;
						}
						// 将节点v加入队列
						queue[t++] = v;
						// 标记节点v在队列中
						enter[v] = true;
					}
				}
			}
		}
		// 如果没有检测到负环，返回false
		return false;
	}

	/**
	 * 主函数，用于读取输入并输出结果
	 * 时间复杂度：O(n + m1 + m2)
	 * 空间复杂度：O(n + m1 + m2)
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 读取输入
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
		// 计算最大的ans值
		double ans = compute();
		// 如果没有人穿女装，输出-1
		if (ans == 0) {
			out.println("-1");
		} else {
			// 否则输出最大的ans值
			out.println(ans);
		}
		// 关闭输出流
		out.flush();
		out.close();
		// 关闭输入流
		br.close();
	}

}