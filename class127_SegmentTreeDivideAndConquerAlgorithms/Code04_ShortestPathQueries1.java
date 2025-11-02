package class167;

/**
 * 线段树分治 + 可撤销线性基 + 带权并查集 解决动态异或最短路问题
 * 题目来源: Codeforces 938G - Shortest Path Queries
 * 题目描述:
 * - 给定n个节点，m条初始边，每条边有边权
 * - 接下来有q条操作，操作类型分为：
 *   1. 添加边：操作 1 x y d - 加入点x到点y权值为d的边
 *   2. 删除边：操作 2 x y - 删除点x到点y的边
 *   3. 查询操作：操作 3 x y - 查询点x到点y的所有路径中，异或和的最小值
 * - 约束条件：
 *   - x < y
 *   - 任意操作后，图连通、无重边、无自环
 *   - 所有操作均合法
 *   - 1 <= n、m、q <= 2 * 10^5
 * - 测试链接:
 *   - https://www.luogu.com.cn/problem/CF938G
 *   - https://codeforces.com/problemset/problem/938/G
 * 
 * 算法核心思想:
 * 1. 使用线段树分治处理动态加边/删边操作
 * 2. 通过带权并查集维护节点间的连通性和异或路径
 * 3. 利用可撤销线性基记录环的异或值，以快速计算异或最小值
 * 4. 通过DFS遍历线段树，处理每个时间点的查询
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * 动态异或最短路问题的高效解决方案
 * <p>
 * 题目来源：Codeforces 938G / 洛谷对应题目
 * <p>
 * 问题描述：维护一个带权无向图，支持动态添加和删除边，以及查询两个点之间的异或最短路径。
 * <p>
 * 算法思路：使用线段树分治将动态问题转化为静态问题，结合可撤销线性基和带权并查集处理异或路径查询。
 * <p>
 * 核心思想：
 * 1. 线段树分治：将所有操作离线处理，把每条边的存在时间区间分解到线段树的节点上
 * 2. 可撤销线性基：维护环的异或值，用于查询时的路径优化
 * 3. 带权并查集：维护节点之间的异或路径权值
 * <p>
 * 数据结构说明：
 * - 事件数组：记录所有边的添加和删除时间
 * - 可撤销线性基：支持插入和撤销操作，用于维护异或环
 * - 带权并查集：维护节点连通性和异或路径值
 * - 线段树：使用链式前向星存储每个时间区间的边
 * <p>
 * 时间复杂度分析：
 * - 事件排序：O((m+q)log(m+q))
 * - 线段树分治：O((m+q)log q)
 * - 线性基操作：O(BIT)，其中BIT=30
 * - 总体时间复杂度：O((m+q)log q * BIT)
 * <p>
 * 空间复杂度分析：
 * - 线段树存储：O((m+q)log q)
 * - 并查集和线性基：O(n + BIT)
 * - 总体空间复杂度：O((m+q)log q)
 * 
 * @author algorithm-journey
 * @date 2023-11-10
 * @see <a href="https://www.luogu.com.cn/problem/CF938G">洛谷链接</a>
 * @see <a href="https://codeforces.com/problemset/problem/938/G">Codeforces链接</a>
 */
public class Code04_ShortestPathQueries1 {

	// 常量定义
	public static int MAXN = 200001;   // 最大节点数
	public static int MAXT = 5000001;  // 最大线段树任务数
	public static int BIT = 29;        // 二进制位数（0~29共30位，因为权值最大为1e9）
	public static int n, m, q;         // 节点数、边数、查询数

	// 事件数组：记录所有边的添加和删除事件
	// event[i][0]: 边的左端点x
	// event[i][1]: 边的右端点y
	// event[i][2]: 事件发生的时间点t
	// event[i][3]: 边的权值w
	public static int[][] event = new int[MAXN << 1][4];
	public static int eventCnt;  // 事件计数器

	// 记录每个时间点的操作信息
	public static int[] op = new int[MAXN];  // 操作类型：1(添加边)、2(删除边)、3(查询)
	public static int[] x = new int[MAXN];   // 操作涉及的第一个节点
	public static int[] y = new int[MAXN];   // 操作涉及的第二个节点
	public static int[] d = new int[MAXN];   // 操作1的边权值

	// 可撤销线性基：用于计算异或最小值
	public static int[] basis = new int[BIT + 1];    // 线性基数组
	public static int[] inspos = new int[BIT + 1];   // 记录插入顺序的位置
	public static int basiz = 0;                     // 线性基当前大小

	// 带权并查集 + 可撤销并查集：维护连通性和异或路径
	public static int[] father = new int[MAXN];     // 父节点数组
	public static int[] siz = new int[MAXN];        // 集合大小数组
	public static int[] eor = new int[MAXN];        // 异或路径数组（从节点到父节点的异或和）
	public static int[][] rollback = new int[MAXN][2]; // 回滚栈，记录合并操作
	public static int opsize = 0;                   // 操作计数

	// 时间轴线段树上的区间任务列表：链式前向星结构
	public static int[] head = new int[MAXN << 2];  // 线段树节点的头指针
	public static int[] next = new int[MAXT];       // 下一个任务的指针
	public static int[] tox = new int[MAXT];        // 任务边的起点
	public static int[] toy = new int[MAXT];        // 任务边的终点
	public static int[] tow = new int[MAXT];        // 任务边的权值
	public static int cnt = 0;                      // 任务计数

	// 存储查询操作的答案
	public static int[] ans = new int[MAXN];

	/**
	 * 将一个数插入线性基
	 * @param num 要插入的数值
	 * @note 线性基用于维护环的异或值，支持撤销操作
	 */
	public static void insert(int num) {
		// 从高位到低位遍历
		for (int i = BIT; i >= 0; i--) {
			// 检查当前位是否为1
			if ((num >> i & 1) == 1) {
				// 如果当前位没有基向量，则插入
				if (basis[i] == 0) {
					basis[i] = num;
					// 记录插入位置，用于撤销操作
					inspos[basiz++] = i;
					return;
				}
				// 否则异或基向量，继续处理低位
				num ^= basis[i];
			}
		}
		// 如果num变为0，说明可以被当前线性基表示，不插入
	}

	/**
	 * 计算num与线性基中元素异或后能得到的最小值
	 * @param num 初始异或值（两点间的异或路径和）
	 * @return 最小异或值
	 * @note 贪心策略：从高位到低位，如果异或后的值更小，则选择异或
	 */
	public static int minEor(int num) {
		// 从高位到低位尝试异或，选择更小的结果
		for (int i = BIT; i >= 0; i--) {
			num = Math.min(num, num ^ basis[i]);
		}
		return num;
	}

	/**
	 * 撤销线性基的操作，恢复到之前的状态
	 * @param oldsiz 要恢复到的线性基大小
	 */
	public static void cancel(int oldsiz) {
		// 将超出的部分重置为0
		while (basiz > oldsiz) {
			basis[inspos[--basiz]] = 0;
		}
	}

	/**
	 * 并查集的find操作：查找集合代表元素
	 * @param i 要查找的节点
	 * @return 节点所在集合的代表元素（根节点）
	 * @note 注意：此实现没有路径压缩，以支持撤销操作
	 */
	public static int find(int i) {
		// 非路径压缩版本，以支持撤销操作
		while (i != father[i]) {
			i = father[i];
		}
		return i;
	}

	/**
	 * 计算节点i到集合代表点（根节点）的异或路径和
	 * @param i 要计算的节点
	 * @return 节点i到根节点的异或和
	 */
	public static int getEor(int i) {
		int ans = 0;
		// 沿父节点链向上，累加异或值
		while (i != father[i]) {
			ans ^= eor[i];
			i = father[i];
		}
		return ans;
	}

	/**
	 * 可撤销并查集的合并操作，在节点u和v之间添加一条权值为w的边
	 * @param u 第一个节点
	 * @param v 第二个节点
	 * @param w 边的权值
	 * @return 如果合并了两个不同的集合，返回true；否则返回false
	 * @note 合并时同时维护带权并查集，并记录操作以支持撤销
	 */
	public static boolean union(int u, int v, int w) {
		// 查找u和v的根节点
		int fu = find(u);
		int fv = find(v);
		
		// 计算u到v的路径异或和应该为w
		// 当前路径异或和为getEor(u) ^ getEor(v)，所以需要异或w得到环的异或值
		w = getEor(u) ^ getEor(v) ^ w;
		
		if (fu == fv) {
			// 如果在同一集合中，将环的异或值插入线性基
			insert(w);
			return false; // 没有合并新的集合
		}
		
		// 按秩合并，始终将较小的树合并到较大的树中
		if (siz[fu] < siz[fv]) {
			int tmp = fu;
			fu = fv;
			fv = tmp;
		}
		
		// 合并操作
		father[fv] = fu;
		siz[fu] += siz[fv];
		// 设置异或路径值
		eor[fv] = w;
		
		// 记录操作，用于撤销
		rollback[++opsize][0] = fu;
		rollback[opsize][1] = fv;
		
		return true; // 成功合并两个集合
	}

	/**
	 * 撤销最近的一次合并操作
	 * @note 恢复并查集的状态
	 */
	public static void undo() {
		// 获取最后一次合并操作的信息
		int fx = rollback[opsize][0];  // 父节点
		int fy = rollback[opsize--][1]; // 子节点
		
		// 恢复fy的父节点为自己
		father[fy] = fy;
		// 清除异或路径值
		eor[fy] = 0;
		// 恢复父节点集合的大小
		siz[fx] -= siz[fy];
	}

	/**
	 * 给线段树节点i添加一个任务：在节点x和y之间添加权值为w的边
	 * @param i 线段树节点编号
	 * @param x 边的起点
	 * @param y 边的终点
	 * @param w 边的权值
	 * @note 使用链式前向星存储任务
	 */
	public static void addEdge(int i, int x, int y, int w) {
		// 创建新任务
		cnt++;
		next[cnt] = head[i];  // 指向前一个任务
		tox[cnt] = x;         // 边的起点
		toy[cnt] = y;         // 边的终点
		tow[cnt] = w;         // 边的权值
		head[i] = cnt;        // 更新头指针
	}

	/**
	 * 线段树区间更新：将边(jobx, joby, jobw)添加到时间区间[jobl, jobr]内
	 * @param jobl 任务开始时间
	 * @param jobr 任务结束时间
	 * @param jobx 边的起点
	 * @param joby 边的终点
	 * @param jobw 边的权值
	 * @param l 当前线段树节点的左区间
	 * @param r 当前线段树节点的右区间
	 * @param i 当前线段树节点编号
	 */
	public static void add(int jobl, int jobr, int jobx, int joby, int jobw, int l, int r, int i) {
		// 如果当前区间完全包含在目标区间内，直接添加到当前节点
		if (jobl <= l && r <= jobr) {
			addEdge(i, jobx, joby, jobw);
		} else {
			// 否则递归到左右子树
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				add(jobl, jobr, jobx, joby, jobw, l, mid, i << 1);
			}
			if (jobr > mid) {
				add(jobl, jobr, jobx, joby, jobw, mid + 1, r, i << 1 | 1);
			}
		}
	}

	/**
	 * 线段树分治的深度优先搜索核心方法
	 * <p>
	 * 工作原理：使用深度优先搜索遍历线段树，在每个节点处应用该节点上的所有边，
	 * 然后递归处理子节点，最后回溯撤销所有修改。这种方法实现了时间维度上的分治。
	 * <p>
	 * 算法流程：
	 * 1. 保存当前线性基的大小，用于回溯
	 * 2. 应用当前节点的所有边（合并集合或插入环的异或值）
	 * 3. 如果是叶子节点（对应具体时间点），处理查询操作
	 * 4. 否则递归处理左右子树
	 * 5. 回溯：撤销所有修改，恢复到进入当前节点前的状态
	 * <p>
	 * 时间复杂度：每个边会被处理O(log q)次，每次处理需要O(BIT)时间，
	 * 总体时间复杂度为O((m+q)log q * BIT)
	 * 
	 * @param l 当前线段树节点的左时间区间边界
	 * @param r 当前线段树节点的右时间区间边界
	 * @param i 当前线段树节点编号（根节点为1，左子节点为2*i，右子节点为2*i+1）
	 */
	public static void dfs(int l, int r, int i) {
		// 保存当前线性基的大小，用于后续撤销操作
		// 这是回溯的关键步骤，确保状态能够正确恢复
		int oldsiz = basiz;
		
		// 记录合并操作的数量，用于后续撤销
		int unionCnt = 0;
		
		// 处理当前节点上的所有边
		// 这些边在[l, r]时间区间内都是活跃的
		for (int e = head[i]; e > 0; e = next[e]) {
			// 尝试合并两个集合
			// 如果成功合并（两个不同的集合），增加计数
			if (union(tox[e], toy[e], tow[e])) {
				unionCnt++;
			}
			// 如果合并失败（形成环），union方法内部已将环的异或值插入线性基
		}
		
		// 处理叶子节点（对应具体的时间点）
		if (l == r) {
			// 如果当前时间点是查询操作（类型3）
			if (op[l] == 3) {
				// 计算x[l]到y[l]的异或路径和：
				// 1. 首先获取x[l]到根节点的异或路径和
				// 2. 获取y[l]到根节点的异或路径和
				// 3. 异或这两个值，得到x[l]到y[l]的异或路径和
				int pathEor = getEor(x[l]) ^ getEor(y[l]);
				
				// 通过线性基优化，找到能与pathEor异或得到的最小值
				// 这一步利用了所有已知环的异或值来优化路径
				ans[l] = minEor(pathEor);
			}
		} else {
			// 非叶子节点，递归处理左右子树
			int mid = (l + r) >> 1;  // 计算中间点
			dfs(l, mid, i << 1);     // 处理左子区间
			dfs(mid + 1, r, i << 1 | 1);  // 处理右子区间
		}
		
		// 回溯：撤销所有修改，恢复到进入当前节点前的状态
		// 这是确保分治正确性的关键步骤
		cancel(oldsiz);  // 撤销线性基的修改，恢复到之前的大小
		
		// 撤销所有合并操作，按逆序撤销
		for (int k = 1; k <= unionCnt; k++) {
			undo();  // 撤销并查集的合并操作
		}
	}

	/**
 * 预处理函数：初始化并查集、排序事件、构建线段树
 * <p>
 * 核心功能：
 * 1. 初始化并查集，为每个节点设置初始父节点和集合大小
 * 2. 对所有边事件进行排序，确保相同边的添加和删除事件相邻
 * 3. 处理每条边的生命周期，确定其有效时间区间
 * 4. 将边按照有效时间区间挂载到线段树的相应节点上，为线段树分治做准备
 * <p>
 * 算法详解：
 * - 线段树分治要求我们将动态问题转换为静态问题，通过离线处理并利用时间轴上的分治策略
 * - 每条边都有一个有效时间区间，在该区间内这条边存在于图中
 * - 排序是为了让相同边的所有事件（添加和删除）集中在一起，便于处理其生命周期
 * - 线段树的每个节点表示一个时间区间，存储在该区间内所有有效的边
 * <p>
 * 时间复杂度：
 * - 排序事件：O((m+q)log(m+q))
 * - 处理边生命周期：O((m+q))
 * - 构建线段树：O((m+q)log q)
 * - 总体时间复杂度：O((m+q)log(m+q))
 */
public static void prepare() {
	// 初始化并查集结构
	// 每个节点初始时都是独立的集合，父节点指向自己，集合大小为1
	for (int i = 1; i <= n; i++) {
		father[i] = i;  // 每个节点初始是自己的父节点
		siz[i] = 1;     // 每个集合初始大小为1
	}
	
	// 按边的两个端点和时间排序事件，这是处理边生命周期的关键步骤
	// 排序规则：
	// 1. 首先按边的第一个端点x从小到大排序
	// 2. 然后按边的第二个端点y从小到大排序
	// 3. 最后按事件发生的时间t从小到大排序
	// 这种排序方式确保相同的边（x,y）的所有事件会集中在一起
	Arrays.sort(event, 1, eventCnt + 1,
			(a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] != b[1] ? a[1] - b[1] : a[2] - b[2]);
	
	int x, y, start, end, d;
	// 处理每条边的生命周期，确定边的有效时间段
	// 使用双指针技术，将相同边的所有事件分组处理
	for (int l = 1, r = 1; l <= eventCnt; l = ++r) {
		x = event[l][0];  // 当前处理的边的起点
		y = event[l][1];  // 当前处理的边的终点
		
		// 找到所有相同边(x,y)的事件，r指针指向最后一个相同边的事件
		while (r + 1 <= eventCnt && event[r + 1][0] == x && event[r + 1][1] == y) {
			r++;
		}
		
		// 处理每对添加和删除事件，确定边的有效时间区间
		// 由于事件已经排序，添加和删除事件会交替出现
		for (int i = l; i <= r; i += 2) {
			start = event[i][2];     // 边开始的时间点（添加事件的时间）
			
			// 确定边结束的时间点：
			// - 如果有对应的删除事件，则边在删除事件发生前结束（end = 删除时间-1）
			// - 如果没有对应的删除事件，则边会一直存在到最后一个查询（end = q）
			end = i + 1 <= r ? (event[i + 1][2] - 1) : q;
			
			d = event[i][3];  // 边的权值（从添加事件中获取）
			
			// 将边添加到线段树的相应时间区间[start, end]
			// 这里调用线段树的区间更新函数，将边挂载到覆盖该区间的最小节点集合上
			add(start, end, x, y, d, 0, q, 1);
		}
	}
}

	/**
	 * 主函数：程序入口，负责协调整个线段树分治算法的执行流程
	 * <p>
	 * 算法执行流程：
	 * 1. 输入处理：读取图的初始状态和所有操作，包括添加边、删除边和查询操作
	 * 2. 预处理：构建时间轴线段树，将边的生命周期分解到线段树节点
	 * 3. 分治执行：通过DFS遍历线段树，动态维护图的状态并处理查询
	 * 4. 结果输出：收集并输出所有查询操作的答案
	 * <p>
	 * 输入输出处理：
	 * - 使用FastReader类实现高效输入，应对大规模数据
	 * - 使用PrintWriter类进行输出缓冲，提高输出效率
	 * - 所有查询结果存储在ans数组中，最后按顺序输出
	 * <p>
	 * 时间线处理：
	 * - 初始边的时间点设为0（即在所有操作前就存在）
	 * - 每个操作对应一个时间点i（1<=i<=q）
	 * - 通过线段树分治处理时间维度上的动态变化
	 * 
	 * @param args 命令行参数（未使用）
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 使用快速输入输出工具类，提高处理大规模数据时的效率
		FastReader in = new FastReader();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取节点数和初始边数，初始化图的基本结构
		n = in.nextInt();
		m = in.nextInt();
		
		// 读取初始边信息，并为每条初始边创建一个时间点为0的添加事件
		for (int i = 1; i <= m; i++) {
			event[i][0] = in.nextInt();  // 边的起点
			event[i][1] = in.nextInt();  // 边的终点
			event[i][2] = 0;             // 初始边的时间点设为0（开始前就存在）
			event[i][3] = in.nextInt();  // 边的权值
		}
		eventCnt = m;  // 记录事件数量
		
		// 读取查询数量，这决定了时间轴的长度
		q = in.nextInt();
		
		// 读取每个操作，并为添加和删除操作记录相应事件
		for (int i = 1; i <= q; i++) {
			op[i] = in.nextInt();   // 操作类型：1(添加)、2(删除)、3(查询)
			x[i] = in.nextInt();    // 操作涉及的第一个节点
			y[i] = in.nextInt();    // 操作涉及的第二个节点
			
			if (op[i] == 1) {       // 添加边操作，需要读取边权
				d[i] = in.nextInt();  // 边的权值
			}
			
			// 对于添加和删除操作，记录事件信息
			if (op[i] != 3) {
				event[++eventCnt][0] = x[i];  // 边的起点
				event[eventCnt][1] = y[i];    // 边的终点
				event[eventCnt][2] = i;       // 事件发生的时间点（即操作序号）
				event[eventCnt][3] = d[i];    // 边的权值（删除操作时也保存该值）
			}
		}
		
		// 预处理阶段：初始化并查集，排序事件，构建线段树
		// 将每条边按照其有效时间区间挂载到线段树的相应节点上
		prepare();
		
		// 执行线段树分治的核心算法
		// 从时间区间[0, q]开始，以根节点（编号1）为起点进行DFS遍历
		// 在遍历过程中动态维护图的状态，并处理所有查询操作
		dfs(0, q, 1);
		
		// 输出所有查询操作的答案
		// 遍历所有时间点，如果该时间点是查询操作，则输出对应的结果
		for (int i = 1; i <= q; i++) {
			if (op[i] == 3) {
				out.println(ans[i]);
			}
		}
		
		// 确保所有输出都被写入到控制台
		out.flush();
		out.close();
	}
	
	// 时间复杂度分析：
	// - 事件排序: O((m+q)log(m+q))
	// - 线段树构建和区间更新: O((m+q)log q)
	// - DFS遍历线段树: O((m+q)log q * BIT)
	// - 并查集操作: O(α(n)) 近似常数时间
	// 总体时间复杂度: O((m+q)log q * BIT)
	// 
	// 空间复杂度分析：
	// - 存储事件和操作: O(m+q)
	// - 线段树任务列表: O((m+q)log q)
	// - 并查集和线性基: O(n + BIT)
	// 总体空间复杂度: O((m+q)log q + n)
	
	// 算法优势：
	// 1. 离线处理所有操作，支持动态加边和删边
	// 2. 利用线段树分治避免了直接处理删除操作
	// 3. 通过线性基高效计算异或最小值
	// 4. 可撤销数据结构保证了回溯时的正确性
	
	/**
	 * C++版本的核心实现思路
	 * 
	 * #include <bits/stdc++.h>
	 * using namespace std;
	 * 
	 * const int MAXN = 200001;
	 * const int MAXT = 5000001;
	 * const int BIT = 29;
	 * int n, m, q;
	 * 
	 * int event[MAXN << 1][4], eventCnt;
	 * int op[MAXN], x[MAXN], y[MAXN], d[MAXN];
	 * int basis[BIT + 1], inspos[BIT + 1], basiz;
	 * int father[MAXN], siz[MAXN], eor[MAXN];
	 * int rollback[MAXN][2], opsize;
	 * int head[MAXN << 2], next_[MAXT], tox[MAXT], toy[MAXT], tow[MAXT], cnt;
	 * int ans[MAXN];
	 * 
	 * void insert(int num) {
	 *     for (int i = BIT; i >= 0; --i) {
	 *         if ((num >> i) & 1) {
	 *             if (!basis[i]) {
	 *                 basis[i] = num;
	 *                 inspos[basiz++] = i;
	 *                 return;
	 *             }
	 *             num ^= basis[i];
	 *         }
	 *     }
	 * }
	 * 
	 * int minEor(int num) {
	 *     for (int i = BIT; i >= 0; --i) {
	 *         num = min(num, num ^ basis[i]);
	 *     }
	 *     return num;
	 * }
	 * 
	 * void cancel(int oldsiz) {
	 *     while (basiz > oldsiz) {
	 *         basis[inspos[--basiz]] = 0;
	 *     }
	 * }
	 * 
	 * int find(int i) {
	 *     while (i != father[i]) i = father[i];
	 *     return i;
	 * }
	 * 
	 * int getEor(int i) {
	 *     int ans = 0;
	 *     while (i != father[i]) {
	 *         ans ^= eor[i];
	 *         i = father[i];
	 *     }
	 *     return ans;
	 * }
	 * 
	 * bool unite(int u, int v, int w) {
	 *     int fu = find(u), fv = find(v);
	 *     w = getEor(u) ^ getEor(v) ^ w;
	 *     if (fu == fv) {
	 *         insert(w);
	 *         return false;
	 *     }
	 *     if (siz[fu] < siz[fv]) swap(fu, fv);
	 *     father[fv] = fu;
	 *     siz[fu] += siz[fv];
	 *     eor[fv] = w;
	 *     rollback[++opsize][0] = fu;
	 *     rollback[opsize][1] = fv;
	 *     return true;
	 * }
	 * 
	 * void undo() {
	 *     int fx = rollback[opsize][0], fy = rollback[opsize--][1];
	 *     father[fy] = fy;
	 *     eor[fy] = 0;
	 *     siz[fx] -= siz[fy];
	 * }
	 * 
	 * void addEdge(int i, int x, int y, int w) {
	 *     next_[++cnt] = head[i];
	 *     tox[cnt] = x;
	 *     toy[cnt] = y;
	 *     tow[cnt] = w;
	 *     head[i] = cnt;
	 * }
	 * 
	 * void add(int jobl, int jobr, int jobx, int joby, int jobw, int l, int r, int i) {
	 *     if (jobl <= l && r <= jobr) {
	 *         addEdge(i, jobx, joby, jobw);
	 *     } else {
	 *         int mid = (l + r) >> 1;
	 *         if (jobl <= mid) add(jobl, jobr, jobx, joby, jobw, l, mid, i << 1);
	 *         if (jobr > mid) add(jobl, jobr, jobx, joby, jobw, mid + 1, r, i << 1 | 1);
	 *     }
	 * }
	 * 
	 * void dfs(int l, int r, int i) {
	 *     int oldsiz = basiz, unionCnt = 0;
	 *     for (int e = head[i]; e; e = next_[e]) {
	 *         if (unite(tox[e], toy[e], tow[e])) {
	 *             unionCnt++;
	 *         }
	 *     }
	 *     if (l == r) {
	 *         if (op[l] == 3) {
	 *             ans[l] = minEor(getEor(x[l]) ^ getEor(y[l]));
	 *         }
	 *     } else {
	 *         int mid = (l + r) >> 1;
	 *         dfs(l, mid, i << 1);
	 *         dfs(mid + 1, r, i << 1 | 1);
	 *     }
	 *     cancel(oldsiz);
	 *     while (unionCnt--) undo();
	 * }
	 * 
	 * void prepare() {
	 *     for (int i = 1; i <= n; ++i) {
	 *         father[i] = i;
	 *         siz[i] = 1;
	 *     }
	 *     sort(event + 1, event + eventCnt + 1, [](int a[], int b[]) {
	 *         if (a[0] != b[0]) return a[0] < b[0];
	 *         if (a[1] != b[1]) return a[1] < b[1];
	 *         return a[2] < b[2];
	 *     });
	 *     int x, y, start, end, dist;
	 *     for (int l = 1, r = 1; l <= eventCnt; l = ++r) {
	 *         x = event[l][0];
	 *         y = event[l][1];
	 *         while (r + 1 <= eventCnt && event[r+1][0] == x && event[r+1][1] == y) r++;
	 *         for (int i = l; i <= r; i += 2) {
	 *             start = event[i][2];
	 *             end = i + 1 <= r ? (event[i+1][2] - 1) : q;
	 *             dist = event[i][3];
	 *             add(start, end, x, y, dist, 0, q, 1);
	 *         }
	 *     }
	 * }
	 * 
	 * int main() {
	 *     ios::sync_with_stdio(false);
	 *     cin.tie(0);
	 *     cin >> n >> m;
	 *     for (int i = 1; i <= m; ++i) {
	 *         cin >> event[i][0] >> event[i][1] >> event[i][3];
	 *         event[i][2] = 0;
	 *     }
	 *     eventCnt = m;
	 *     cin >> q;
	 *     for (int i = 1; i <= q; ++i) {
	 *         cin >> op[i] >> x[i] >> y[i];
	 *         if (op[i] == 1) {
	 *             cin >> d[i];
	 *         }
	 *         if (op[i] != 3) {
	 *             event[++eventCnt][0] = x[i];
	 *             event[eventCnt][1] = y[i];
	 *             event[eventCnt][2] = i;
	 *             event[eventCnt][3] = d[i];
	 *         }
	 *     }
	 *     prepare();
	 *     dfs(0, q, 1);
	 *     for (int i = 1; i <= q; ++i) {
	 *         if (op[i] == 3) {
	 *             cout << ans[i] << '\n';
	 *         }
	 *     }
	 *     return 0;
	 * }
	 */
	
	/**
	 * Python版本的核心实现思路
	 * 
	 * import sys
	 * sys.setrecursionlimit(1 << 25)
	 * 
	 * MAXN = 200001
	 * MAXT = 5000001
	 * BIT = 29
	 * 
	 * event = [[0]*4 for _ in range(MAXN << 1)]
	 * eventCnt = 0
	 * 
	 * op = [0]*MAXN
	 * x = [0]*MAXN
	 * y = [0]*MAXN
	 * d = [0]*MAXN
	 * 
	 * basis = [0]*(BIT + 1)
	 * inspos = [0]*(BIT + 1)
	 * basiz = 0
	 * 
	 * father = [0]*MAXN
	 * siz = [0]*MAXN
	 * eor = [0]*MAXN
	 * rollback = [[0]*2 for _ in range(MAXN)]
	 * opsize = 0
	 * 
	 * head = [0]*(MAXN << 2)
	 * next_ = [0]*MAXT
	 * tox = [0]*MAXT
	 * toy = [0]*MAXT
	 * tow = [0]*MAXT
	 * cnt = 0
	 * 
	 * ans = [0]*MAXN
	 * 
	 * def insert(num):
	 *     global basiz
	 *     for i in range(BIT, -1, -1):
	 *         if (num >> i) & 1:
	 *             if basis[i] == 0:
	 *                 basis[i] = num
	 *                 inspos[basiz] = i
	 *                 basiz += 1
	 *                 return
	 *             num ^= basis[i]
	 * 
	 * def minEor(num):
	 *     for i in range(BIT, -1, -1):
	 *         num = min(num, num ^ basis[i])
	 *     return num
	 * 
	 * def cancel(oldsiz):
	 *     global basiz
	 *     while basiz > oldsiz:
	 *         basiz -= 1
	 *         basis[inspos[basiz]] = 0
	 * 
	 * def find(i):
	 *     while i != father[i]:
	 *         i = father[i]
	 *     return i
	 * 
	 * def getEor(i):
	 *     res = 0
	 *     while i != father[i]:
	 *         res ^= eor[i]
	 *         i = father[i]
	 *     return res
	 * 
	 * def unite(u, v, w):
	 *     global opsize
	 *     fu = find(u)
	 *     fv = find(v)
	 *     w = getEor(u) ^ getEor(v) ^ w
	 *     if fu == fv:
	 *         insert(w)
	 *         return False
	 *     if siz[fu] < siz[fv]:
	 *         fu, fv = fv, fu
	 *     father[fv] = fu
	 *     siz[fu] += siz[fv]
	 *     eor[fv] = w
	 *     opsize += 1
	 *     rollback[opsize][0] = fu
	 *     rollback[opsize][1] = fv
	 *     return True
	 * 
	 * def undo():
	 *     global opsize
	 *     fx = rollback[opsize][0]
	 *     fy = rollback[opsize][1]
	 *     opsize -= 1
	 *     father[fy] = fy
	 *     eor[fy] = 0
	 *     siz[fx] -= siz[fy]
	 * 
	 * def addEdge(i, x, y, w):
	 *     global cnt
	 *     cnt += 1
	 *     next_[cnt] = head[i]
	 *     tox[cnt] = x
	 *     toy[cnt] = y
	 *     tow[cnt] = w
	 *     head[i] = cnt
	 * 
	 * def add(jobl, jobr, jobx, joby, jobw, l, r, i):
	 *     if jobl <= l and r <= jobr:
	 *         addEdge(i, jobx, joby, jobw)
	 *     else:
	 *         mid = (l + r) >> 1
	 *         if jobl <= mid:
	 *             add(jobl, jobr, jobx, joby, jobw, l, mid, i << 1)
	 *         if jobr > mid:
	 *             add(jobl, jobr, jobx, joby, jobw, mid + 1, r, i << 1 | 1)
	 * 
	 * def dfs(l, r, i):
	 *     oldsiz = basiz
	 *     unionCnt = 0
	 *     e = head[i]
	 *     while e > 0:
	 *         if unite(tox[e], toy[e], tow[e]):
	 *             unionCnt += 1
	 *         e = next_[e]
	 *     if l == r:
	 *         if op[l] == 3:
	 *             ans[l] = minEor(getEor(x[l]) ^ getEor(y[l]))
	 *     else:
	 *         mid = (l + r) >> 1
	 *         dfs(l, mid, i << 1)
	 *         dfs(mid + 1, r, i << 1 | 1)
	 *     cancel(oldsiz)
	 *     for _ in range(unionCnt):
	 *         undo()
	 * 
	 * def prepare(n_val):
	 *     for i in range(1, n_val + 1):
	 *         father[i] = i
	 *         siz[i] = 1
	 *     # 排序事件
	 *     event_list = []
	 *     for i in range(1, eventCnt + 1):
	 *         event_list.append(event[i])
	 *     event_list.sort(key=lambda e: (e[0], e[1], e[2]))
	 *     for i in range(eventCnt):
	 *         event[i + 1] = event_list[i]
	 *     l = 1
	 *     while l <= eventCnt:
	 *         r = l
	 *         x_val = event[l][0]
	 *         y_val = event[l][1]
	 *         while r + 1 <= eventCnt and event[r+1][0] == x_val and event[r+1][1] == y_val:
	 *             r += 1
	 *         i = l
	 *         while i <= r:
	 *             start = event[i][2]
	 *             end = event[i+1][2] - 1 if (i + 1 <= r) else q_val
	 *             dist = event[i][3]
	 *             add(start, end, x_val, y_val, dist, 0, q_val, 1)
	 *             i += 2
	 *         l = r + 1
	 * 
	 * def main():
	 *     global eventCnt, n, m, q, q_val
	 *     input = sys.stdin.read().split()
	 *     ptr = 0
	 *     n = int(input[ptr]); ptr +=1
	 *     m = int(input[ptr]); ptr +=1
	 *     for i in range(1, m + 1):
	 *         event[i][0] = int(input[ptr]); ptr +=1
	 *         event[i][1] = int(input[ptr]); ptr +=1
	 *         event[i][3] = int(input[ptr]); ptr +=1
	 *         event[i][2] = 0
	 *     eventCnt = m
	 *     q_val = int(input[ptr]); ptr +=1
	 *     q = q_val
	 *     for i in range(1, q + 1):
	 *         op[i] = int(input[ptr]); ptr +=1
	 *         x[i] = int(input[ptr]); ptr +=1
	 *         y[i] = int(input[ptr]); ptr +=1
	 *         if op[i] == 1:
	 *             d[i] = int(input[ptr]); ptr +=1
	 *         if op[i] != 3:
	 *             eventCnt += 1
	 *             event[eventCnt][0] = x[i]
	 *             event[eventCnt][1] = y[i]
	 *             event[eventCnt][2] = i
	 *             event[eventCnt][3] = d[i]
	 *     prepare(n)
	 *     dfs(0, q, 1)
	 *     for i in range(1, q + 1):
	 *         if op[i] == 3:
	 *             print(ans[i])
	 * 
	 * if __name__ == "__main__":
	 *     main()
	 */
	
	/**
	 * 语言特性差异与注意事项：
	 * 1. Java vs C++：
	 *    - C++使用数组的访问效率更高，但需要手动管理内存
	 *    - Java中的数组初始化更简洁，但性能略低
	 *    - C++中的位运算和位移操作与Java一致
	 *    
	 * 2. Java vs Python：
	 *    - Python中的递归深度限制需要设置（sys.setrecursionlimit）
	 *    - Python中的全局变量使用需要声明global
	 *    - Python的执行速度明显慢于Java和C++，对于大数据量可能会超时
	 *    - Java的FastReader类在处理大规模输入时性能优于Python的标准输入
	 * 
	 * 3. 优化技巧：
	 *    - 使用位运算代替部分数学运算
	 *    - 避免在递归中创建临时对象
	 *    - 对于大数据量，使用快速输入输出方法
	 *    - 注意数组的大小设置，避免数组越界
	 */

	/**
	 * 快速输入工具类：用于高效处理大规模输入数据
	 * <p>
	 * 优化原理：
	 * 1. 使用字节级别的缓冲区直接处理输入流，减少字符转换开销
	 * 2. 预读取大块数据到缓冲区，显著减少系统IO调用次数
	 * 3. 手动实现字符解析逻辑，避免使用高级解析器的额外开销
	 * 4. 为常用数据类型提供专门的读取方法，优化解析效率
	 * <p>
	 * 性能分析：
	 * - 相比Scanner：速度提升5-10倍，尤其在处理大量整数输入时
	 * - 相比BufferedReader+StringTokenizer：减少了字符串创建和分割的开销
	 * - 字节级处理：避免了不必要的字符编码转换
	 * - 适用于算法竞赛中常见的大数据量输入场景
	 */
	static class FastReader {
		final private int BUFFER_SIZE = 1 << 16;  // 缓冲区大小：64KB，平衡内存使用和IO次数
		private final InputStream in;       // 输入流
		private final byte[] buffer;       // 字节缓冲区
		private int ptr, len;              // 指针位置和当前缓冲区长度

		/**
		 * 构造函数：初始化输入流和缓冲区
		 */
		public FastReader() {
			in = System.in;
			buffer = new byte[BUFFER_SIZE];
			ptr = len = 0;  // 初始时缓冲区为空
		}

		/**
		 * 检查是否还有下一个字节可读
		 * @return 如果还有字节可读，返回true；否则返回false
		 * @throws IOException 输入输出异常
		 */
		private boolean hasNextByte() throws IOException {
			// 如果当前缓冲区还有未读字节，直接返回true
			if (ptr < len)
				return true;
			// 否则重新填充缓冲区
			ptr = 0;
			len = in.read(buffer);
			return len > 0;  // 如果读取到字节，返回true
		}

		/**
		 * 读取下一个字节
		 * @return 读取的字节值
		 * @throws IOException 输入输出异常
		 */
		private byte readByte() throws IOException {
			// 如果没有更多字节可读，返回-1
			if (!hasNextByte())
				return -1;
			// 否则返回当前字节并移动指针
			return buffer[ptr++];
		}

		/**
		 * 检查字符是否为空白字符
		 * @param b 要检查的字节
		 * @return 如果是空白字符，返回true
		 */
		private boolean isWhitespace(byte b) {
			// 常见空白字符：空格、制表符、换行符、回车符等
			return b <= ' ';
		}

		/**
		 * 读取下一个字符
		 * @return 读取的字符
		 * @throws IOException 输入输出异常
		 */
		public char nextChar() throws IOException {
			byte c;
			// 跳过空白字符
			do {
				c = readByte();
				if (c == -1)
					return 0;
			} while (c <= ' ');
			// 读取第一个非空白字符
			char ans = 0;
			while (c > ' ') {
				ans = (char) c;
				c = readByte();
			}
			return ans;
		}

		/**
		 * 读取下一个整数
		 * @return 读取的整数值
		 * @throws IOException 输入输出异常
		 */
		public int nextInt() throws IOException {
			int num = 0;
			// 读取一个字节
			byte b = readByte();
			// 跳过空白字符
			while (isWhitespace(b))
				b = readByte();
			// 处理负号
			boolean minus = false;
			if (b == '-') {
				minus = true;
				b = readByte();
			}
			// 读取数字部分
			while (!isWhitespace(b) && b != -1) {
				num = num * 10 + (b - '0');  // 将字符转换为数字
				b = readByte();
			}
			// 根据符号返回结果
			return minus ? -num : num;
		}
	}
}
	}

}
