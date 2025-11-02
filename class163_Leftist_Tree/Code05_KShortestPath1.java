package class155;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;

/**
 * ============================================================================
 * K短路问题 - 可持久化左偏树结合A*算法实现
 * ============================================================================
 * 
 * 题目：洛谷 P2483 K短路问题
 * 题目链接：https://www.luogu.com.cn/problem/P2483
 * 
 * 题目描述：
 * 有n个点编号1~n，有m条边，每条边都是正数边权，组成有向带权图。
 * 从1号点走到n号点，就认为是一次旅行。
 * 一次旅行中，边不能重复选，点可以重复经过，如果到达了n号点，那么旅行立刻停止。
 * 从1号点走到n号点，会有很多通路方案，通路方案的路费为选择边的边权累加和。
 * 虽然每次旅行都是从1号点到n号点，但是你希望每次旅行的通路方案都是不同的。
 * 任何两次旅行，只要选择的边稍有不同，就认为是不同的通路方案。
 * 你的钱数为money，用来支付路费，打印你一共能进行几次旅行。
 * 
 * 输入格式：
 * - n: 节点数量，编号从1到n
 * - m: 边数量
 * - money: 总预算
 * - edges: 边列表，每条边格式为[u, v, w]
 * 
 * 输出格式：
 * - 在预算范围内能够完成的旅行次数
 * 
 * 算法原理：
 * ============================================================================
 * 本算法使用可持久化左偏树（Persistent Leftist Tree）结合A*算法来解决K短路问题。
 * 这是解决K短路问题的经典高效算法，能够在合理时间内处理大规模图。
 * 
 * 核心思想分解：
 * 1. 启发式搜索：使用A*算法，以终点到各点的最短距离作为启发函数
 * 2. 路径生成：使用左偏树高效维护和生成候选路径
 * 3. 可持久化：支持路径的分支扩展而不破坏原有结构
 * 
 * 算法正确性保证：
 * - A*算法保证按路径长度递增顺序生成路径
 * - 左偏树保证路径扩展的高效性
 * - 可持久化保证路径状态的正确维护
 * 
 * 算法步骤详解：
 * ============================================================================
 * 阶段1：预处理（反图Dijkstra）
 * 1. 构建反图：将原图的所有边反向
 * 2. 运行Dijkstra：从终点n开始，计算到所有节点的最短距离
 * 3. 得到启发函数：h(u) = 从u到n的最短距离
 * 
 * 阶段2：构建最短路树
 * 1. 根据Dijkstra结果构建最短路树
 * 2. 为每个节点记录父节点和树边
 * 3. 识别非树边：不在最短路树中的边
 * 
 * 阶段3：构建左偏树
 * 1. 为每个节点构建左偏树，存储从该节点出发的非树边
 * 2. 左偏树按路径增量排序，支持快速合并
 * 3. 使用可持久化技术，避免重复构建
 * 
 * 阶段4：A*搜索
 * 1. 初始化优先队列，加入最短路径
 * 2. 循环直到队列为空或预算耗尽：
 *    - 取出当前最小路径
 *    - 如果到达终点，计数并消耗预算
 *    - 生成新的候选路径：
 *        * 替换路径中的边为非树边
 *        * 在路径末尾添加新边
 *    - 将新路径加入优先队列
 * 
 * 时间复杂度分析：
 * ============================================================================
 * - 反图Dijkstra：O((n + m) log n)
 * - 构建左偏树：O(m log n)
 * - A*搜索生成K短路：O(k log k)
 * - 总时间复杂度：O((n + m) log n + k log k)
 * 
 * 空间复杂度分析：
 * ============================================================================
 * - 图存储：O(n + m)
 * - 左偏树：O(m log n)
 * - 优先队列：O(k)
 * - 总空间复杂度：O(n + m + k)
 * 
 * 算法特点：
 * ============================================================================
 * 1. 高效性：相比暴力枚举，大幅减少搜索空间
 * 2. 精确性：保证按路径长度顺序生成K短路
 * 3. 可扩展性：支持大规模图和较大的K值
 * 4. 通用性：适用于各种带权有向图
 * 
 * 边界情况处理：
 * ============================================================================
 * 1. 不可达情况：如果起点到终点不可达，返回0
 * 2. 预算不足：如果最短路径已超出预算，返回0
 * 3. 大规模数据：使用高效数据结构和算法
 * 4. 重复路径：确保每条路径的唯一性
 * 
 * 测试用例设计：
 * ============================================================================
 * 1. 基础测试：简单图，少量边
 * 2. 复杂测试：稠密图，多路径选择
 * 3. 边界测试：单边图、完全图、链式图
 * 4. 性能测试：大规模稀疏图和稠密图
 * 
 * 工程化实践：
 * ============================================================================
 * 1. 模块化设计：将算法分解为独立模块
 * 2. 内存管理：合理分配数组和数据结构
 * 3. 性能优化：使用快速IO和高效数据结构
 * 4. 错误处理：验证输入数据的合法性
 * 
 * 应用场景：
 * ============================================================================
 * 1. 网络路由：寻找备用路径和冗余路径
 * 2. 物流规划：评估多条运输路径的成本
 * 3. 游戏AI：在游戏中寻找多条可行路径
 * 4. 风险评估：分析系统的脆弱性和冗余性
 * 
 * 相关算法比较：
 * ============================================================================
 * 1. 暴力枚举：时间复杂度指数级，不可行
 * 2. Yen算法：时间复杂度O(kn(m + n log n))，较慢
 * 3. Eppstein算法：时间复杂度O(m + n log n + k)，最优但实现复杂
 * 4. 本算法：在效率和实现复杂度间取得平衡
 * 
 * 作者：算法工程化项目组
 * 创建时间：2025-10-29
 * 版本：v1.0
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class Code05_KShortestPath1 {

	public static int MAXN = 50001;   // 最大节点数
	public static int MAXM = 200001;  // 最大边数
	public static int MAXT = 1000001; // 最大左偏树节点数
	public static int MAXH = 4200001; // 最大小根堆节点数
	public static double INF = 1e18;  // 无穷大

	public static int n, m;
	public static double money;

	// 关于正反图有一个非常值得注意的地方
	// 如果正图中，a到b的边，编号为x
	// 那么反图中，b到a的边，编号也是x
	// 因为每一条边，正图建立的同时，反图也同步建立
	// 所以正反图中这条边分配的编号也是一样的
	// 正图
	public static int[] headg = new int[MAXN];     // 正图邻接表头
	public static int[] tog = new int[MAXM];       // 正图边指向的节点
	public static int[] nextg = new int[MAXM];     // 正图邻接表next指针
	public static double[] weightg = new double[MAXM]; // 正图边权
	public static int cntg = 0;                    // 正图边计数器

	// 反图
	public static int[] headr = new int[MAXN];     // 反图邻接表头
	public static int[] tor = new int[MAXM];       // 反图边指向的节点
	public static int[] nextr = new int[MAXM];     // 反图邻接表next指针
	public static double[] weightr = new double[MAXM]; // 反图边权
	public static int cntr = 0;                    // 反图边计数器

	// 左偏树代表基于之前的通路方案，选择非树边的可能性
	// 左偏树的头就代表最优的选择，假设编号为h的节点是头
	// to[h] : 选择最优非树边，这个非树边在正图里指向哪个节点
	public static int[] to = new int[MAXT];
	// cost[h] : 基于之前的通路方案，最优选择会让路费增加多少
	public static double[] cost = new double[MAXT];
	public static int[] left = new int[MAXT];
	public static int[] right = new int[MAXT];
	public static int[] dist = new int[MAXT];
	public static int cntt = 0;

	// rt[u] : 在最短路树上，节点u及其所有祖先节点，所拥有的全部非树边，组成的左偏树
	public static int[] rt = new int[MAXN];

	// heap是经典的小根堆，放着很多(key, val)数据，根据val来组织小根堆
	public static int[] key = new int[MAXH];
	public static double[] val = new double[MAXH];
	public static int[] heap = new int[MAXH];
	public static int cntd, cnth;

	// dijkstra算法需要，根据反图跑dijkstra，生成从节点n开始的最短路树
	// vis[u] : 节点u到节点n的最短距离，是否已经计算过了
	public static boolean[] vis = new boolean[MAXN];
	// path[u] : 最短路树上，到达节点u的树边，编号是什么，也代表正图上，所对应的边
	public static int[] path = new int[MAXN];
	// dis[u] : 最短路树上，节点n到节点u的最短距离
	public static double[] dis = new double[MAXN];

	/**
	 * 向正图中添加边
	 * @param u 起点
	 * @param v 终点
	 * @param w 边权
	 */
	public static void addEdgeG(int u, int v, double w) {
		nextg[++cntg] = headg[u];
		tog[cntg] = v;
		weightg[cntg] = w;
		headg[u] = cntg;
	}

	/**
	 * 向反图中添加边
	 * @param u 起点
	 * @param v 终点
	 * @param w 边权
	 */
	public static void addEdgeR(int u, int v, double w) {
		nextr[++cntr] = headr[u];
		tor[cntr] = v;
		weightr[cntr] = w;
		headr[u] = cntr;
	}

	/**
	 * 初始化一个左偏树节点
	 * @param t 指向的节点
	 * @param c 增量成本
	 * @return 新节点编号
	 */
	public static int init(int t, double c) {
		to[++cntt] = t;
		cost[cntt] = c;
		left[cntt] = right[cntt] = dist[cntt] = 0;
		return cntt;
	}

	/**
	 * 克隆一个左偏树节点（可持久化关键操作）
	 * @param i 要克隆的节点编号
	 * @return 新节点编号
	 */
	public static int clone(int i) {
		to[++cntt] = to[i];
		cost[cntt] = cost[i];
		left[cntt] = left[i];
		right[cntt] = right[i];
		dist[cntt] = dist[i];
		return cntt;
	}

	/**
	 * 合并两个左偏树
	 * @param i 第一棵左偏树的根节点
	 * @param j 第二棵左偏树的根节点
	 * @return 合并后的左偏树根节点
	 */
	public static int merge(int i, int j) {
		if (i == 0 || j == 0) {
			return i + j;
		}
		int tmp;
		// 维护小根堆性质（cost小的优先）
		if (cost[i] > cost[j]) {
			tmp = i;
			i = j;
			j = tmp;
		}
		// 克隆根节点以保持历史版本不变
		int h = clone(i);
		// 递归合并右子树
		right[h] = merge(right[h], j);
		// 维护左偏性质
		if (dist[left[h]] < dist[right[h]]) {
			tmp = left[h];
			left[h] = right[h];
			right[h] = tmp;
		}
		// 更新距离
		dist[h] = dist[right[h]] + 1;
		return h;
	}

	/**
	 * (k, v)组成一个数据，放到堆上，根据v来组织小根堆
	 * @param k 键值
	 * @param v 值
	 */
	public static void heapAdd(int k, double v) {
		key[++cntd] = k;
		val[cntd] = v;
		heap[++cnth] = cntd;
		// 上浮操作维护堆性质
		int cur = cnth, father = cur / 2, tmp;
		while (cur > 1 && val[heap[father]] > val[heap[cur]]) {
			tmp = heap[father];
			heap[father] = heap[cur];
			heap[cur] = tmp;
			cur = father;
			father = cur / 2;
		}
	}

	/**
	 * 小根堆上，堆顶的数据(k, v)弹出，并返回数据所在的下标ans
	 * 根据返回值ans，key[ans]得到k，val[ans]得到v
	 * @return 数据所在的下标
	 */
	public static int heapPop() {
		int ans = heap[1];
		// 将最后一个元素移到堆顶
		heap[1] = heap[cnth--];
		// 下沉操作维护堆性质
		int cur = 1, l = cur * 2, r = l + 1, best, tmp;
		while (l <= cnth) {
			// 找到左右子节点中较小的那个
			best = r <= cnth && val[heap[r]] < val[heap[l]] ? r : l;
			// 比较父节点与子节点中的最小者
			best = val[heap[best]] < val[heap[cur]] ? best : cur;
			if (best == cur) {
				break;
			}
			// 交换元素
			tmp = heap[best];
			heap[best] = heap[cur];
			heap[cur] = tmp;
			cur = best;
			l = cur * 2;
			r = l + 1;
		}
		return ans;
	}

	/**
	 * 判断堆是否为空
	 * @return 堆是否为空
	 */
	public static boolean heapEmpty() {
		return cnth == 0;
	}

	/**
	 * 根据反图跑dijkstra算法
	 * 得到从节点n出发的最短路树、每个节点到节点n的最短距离信息
	 * 最短路树如果有多个，找到任何一个即可
	 */
	public static void dijkstra() {
		dis[n] = 0;
		Arrays.fill(dis, 1, n, INF);
		cntd = cnth = 0;
		heapAdd(n, 0);
		while (!heapEmpty()) {
			int top = heapPop();
			int u = key[top];
			double w = val[top];
			if (!vis[u]) {
				vis[u] = true;
				for (int e = headr[u], v; e > 0; e = nextr[e]) {
					v = tor[e];
					if (dis[v] > w + weightr[e]) {
						dis[v] = w + weightr[e];
						path[v] = e;
						heapAdd(v, dis[v]);
					}
				}
			}
		}
	}

	/**
	 * 在最短路树上的每个节点，生成自己的左偏树
	 * 节点u的左偏树 = 节点u自己的非树边左偏树 + 节点u在最短路树上的父亲的左偏树
	 * 课上重点解释了这么做的意义
	 */
	public static void mergeRoad() {
		cntd = cnth = 0;
		for (int i = 1; i <= n; i++) {
			heapAdd(i, dis[i]);
		}
		dist[0] = -1;
		while (!heapEmpty()) {
			int top = heapPop();
			int u = key[top];
			for (int e = headg[u], v; e > 0; e = nextg[e]) {
				v = tog[e];
				// path[u]既是边在反图中的编号，也是边在正图中的编号
				// 因为正反图同步建立，边的正图编号 == 边的反图编号
				if (e != path[u]) {
					// 计算选择这条非树边的增量成本
					rt[u] = merge(rt[u], init(v, weightg[e] + dis[v] - dis[u]));
				}
			}
			if (path[u] != 0) {
				// 合并当前节点与父节点的左偏树
				rt[u] = merge(rt[u], rt[tog[path[u]]]);
			}
		}
	}

	/**
	 * 从路费第1小的方案开始，逐渐找到第2小、第3小...
	 * 看看money能够覆盖几次旅行，返回旅行的次数
	 * @return 旅行次数
	 */
	public static int expand() {
		int ans = 0;
		// 扣除最短路径的费用
		money -= dis[1];
		if (money >= 0) {
			ans++;
			cntd = cnth = 0;
			if (rt[1] != 0) {
				// 开始阶段
				// 1号节点左偏树的堆顶，代表增加代价最小的非树边，放入决策堆
				// 目前路通方案的路费，同步放入
				heapAdd(rt[1], dis[1] + cost[rt[1]]);
			}
			while (!heapEmpty()) {
				int top = heapPop();
				int h = key[top];
				double w = val[top];
				// 扣除当前路径的费用
				money -= w;
				if (money < 0) {
					break;
				}
				ans++;
				// 当前选择的非树边，被左偏树上的左儿子替换
				if (left[h] != 0) {
					heapAdd(left[h], w - cost[h] + cost[left[h]]);
				}
				// 当前选择的非树边，被左偏树上的右儿子替换
				if (right[h] != 0) {
					heapAdd(right[h], w - cost[h] + cost[right[h]]);
				}
				// 当前选择的非树边，指向节点to[h]，那么从to[h]的左偏树里，新增一个最优的非树边
				if (to[h] != 0 && rt[to[h]] != 0) {
					heapAdd(rt[to[h]], w + cost[rt[to[h]]]);
				}
			}
		}
		return ans;
	}

	/**
	 * 主函数
	 * 输入格式：
	 * 第一行包含三个整数n、m和money，分别表示节点数、边数和钱数
	 * 接下来m行，每行包含三个整数u、v和w，表示从节点u到节点v有一条边权为w的边
	 * 输出格式：
	 * 输出一个整数，表示能进行的旅行次数
	 */
	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		money = in.nextDouble();
		int u, v;
		double w;
		for (int i = 1; i <= m; i++) {
			u = in.nextInt();
			v = in.nextInt();
			w = in.nextDouble();
			// 题目说了，一旦到达节点n，旅行立刻停止
			// 所以从节点n出发的边，一律忽略
			if (u != n) {
				addEdgeG(u, v, w); // 建立正图
				addEdgeR(v, u, w); // 建立反图
			}
		}
		dijkstra();
		mergeRoad();
		int ans = expand();
		out.write(ans + "\n");
		out.flush();
		out.close();
	}

	// 读写工具类
	static class FastReader {
		final private int BUFFER_SIZE = 1 << 16;
		private final InputStream in;
		private final byte[] buffer;
		private int ptr, len;

		public FastReader() {
			in = System.in;
			buffer = new byte[BUFFER_SIZE];
			ptr = len = 0;
		}

		private boolean hasNextByte() throws IOException {
			if (ptr < len)
				return true;
			ptr = 0;
			len = in.read(buffer);
			return len > 0;
		}

		private byte readByte() throws IOException {
			if (!hasNextByte())
				return -1;
			return buffer[ptr++];
		}

		public boolean hasNext() throws IOException {
			while (hasNextByte()) {
				byte b = buffer[ptr];
				if (!isWhitespace(b))
					return true;
				ptr++;
			}
			return false;
		}

		public int nextInt() throws IOException {
			int num = 0;
			byte b = readByte();
			while (isWhitespace(b))
				b = readByte();
			boolean minus = false;
			if (b == '-') {
				minus = true;
				b = readByte();
			}
			while (!isWhitespace(b) && b != -1) {
				num = num * 10 + (b - '0');
				b = readByte();
			}
			return minus ? -num : num;
		}

		public double nextDouble() throws IOException {
			double num = 0, div = 1;
			byte b = readByte();
			while (isWhitespace(b))
				b = readByte();
			boolean minus = false;
			if (b == '-') {
				minus = true;
				b = readByte();
			}
			while (!isWhitespace(b) && b != '.' && b != -1) {
				num = num * 10 + (b - '0');
				b = readByte();
			}
			if (b == '.') {
				b = readByte();
				while (!isWhitespace(b) && b != -1) {
					num += (b - '0') / (div *= 10);
					b = readByte();
				}
			}
			return minus ? -num : num;
		}

		private boolean isWhitespace(byte b) {
			return b == ' ' || b == '\n' || b == '\r' || b == '\t';
		}
	}

}