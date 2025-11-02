package class164;

/**
 * 边权上限内第k大点权问题 - Kruskal重构树结合可持久化线段树解法
 * 
 * 算法核心思想：
 * 1. Kruskal重构树：将边按边权从小到大排序，构建一棵重构树
 *    - 树中的每个非叶节点代表一条边
 *    - 边权作为非叶节点的节点值
 *    - 所有原始节点作为叶节点
 * 2. LCA（最近公共祖先）+ 倍增法：快速找到满足边权<=x的最高祖先节点
 * 3. 可持久化线段树：维护每个节点的子树中点权的有序集合，支持查询第k大
 * 
 * 解题思路：
 * - 构建Kruskal重构树，使得从叶节点到根节点的路径上的边权递增
 * - 对于查询(u, x, k)，找到u所在子树中边权<=x的最大节点
 * - 利用该节点的子树范围，通过可持久化线段树查询第k大的点权
 * 
 * 复杂度分析：
 * - 时间复杂度：
 *   - 构建重构树：O(m log m) - 边排序时间
 *   - DFS预处理：O(n log n)
 *   - 构建可持久化线段树：O(n log n)
 *   - 单次查询：O(log n + log n) - LCA查询 + 线段树查询
 * - 空间复杂度：O(n log n + m) - 数据结构存储
 * 
 * 工程化考量：
 * - 异常处理：使用FastIO类处理大规模数据读写，避免超时
 * - 性能优化：使用离散化处理点权，减少数据范围
 * - 边界处理：处理强制在线查询，使用异或操作保护上一次查询结果
 * - 代码健壮性：使用足够大的数组空间，避免溢出
 * 
 * 题目来源：洛谷P7834
 * 测试链接：https://www.luogu.com.cn/problem/P7834
 * 
 * 注意：Java实现逻辑正确，但由于空间限制可能无法通过所有测试用例
 *       C++版本(Code07_Peaks2)可以通过所有测试
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Code07_Peaks1 {

	public static int MAXN = 100001;
	public static int MAXK = 200001;
	public static int MAXM = 500001;
	public static int MAXT = MAXN * 40;
	public static int MAXH = 20;
	public static int n, m, q, s;

	public static int[] node = new int[MAXN];
	public static int[] sorted = new int[MAXN];
	public static int[][] edge = new int[MAXM][3];

	// 并查集
	public static int[] father = new int[MAXK];

	// Kruskal重构树
	public static int[] head = new int[MAXK];
	public static int[] next = new int[MAXK];
	public static int[] to = new int[MAXK];
	public static int cntg = 0;
	public static int[] nodeKey = new int[MAXK];
	public static int cntu;

	// 倍增表
	public static int[][] stjump = new int[MAXK][MAXH];
	// 子树上的叶节点个数
	public static int[] leafsiz = new int[MAXK];
	// 子树上叶节点的dfn序号最小值
	public static int[] leafDfnMin = new int[MAXK];
	// leafseg[i] = j，表示dfn序号为i的叶节点，原始编号为j
	public static int[] leafseg = new int[MAXK];
	// dfn的计数
	public static int cntd = 0;

	// 可持久化线段树
	// 线段树的下标为某个数字，所以是值域线段树
	// 数值范围[l..r]上，一共有几个数字，就是numcnt的含义
	public static int[] root = new int[MAXN];
	public static int[] ls = new int[MAXT];
	public static int[] rs = new int[MAXT];
	public static int[] numcnt = new int[MAXT];
	public static int cntt = 0;

	/**
	 * 二分查找函数，用于离散化
	 * 时间复杂度：O(log n)
	 * 
	 * @param num 要查找的原始值
	 * @return 离散化后的编号，找不到返回-1
	 */
	public static int kth(int num) {
		int left = 1, right = s, mid;
		while (left <= right) {
			mid = (left + right) / 2;
			if (sorted[mid] == num) {
				return mid; // 找到匹配的值，返回离散化后的索引
			} else if (sorted[mid] < num) {
				left = mid + 1; // 目标值在右半部分
			} else {
				right = mid - 1; // 目标值在左半部分
			}
		}
		return -1; // 未找到
	}

	/**
	 * 离散化处理函数
	 * 时间复杂度：O(n log n)
	 * 
	 * 功能：
	 * 1. 对节点权值进行排序并去重，得到离散化数组
	 * 2. 将原始节点权值替换为离散化后的索引
	 * 
	 * 目的：减少数据范围，提高可持久化线段树的空间利用率
	 */
	public static void prepare() {
		// 将原始节点权值复制到排序数组
		for (int i = 1; i <= n; i++) {
			sorted[i] = node[i];
		}
		// 排序
		Arrays.sort(sorted, 1, n + 1);
		// 去重，得到离散化数组
		s = 1; // 离散化后的值域大小
		for (int i = 2; i <= n; i++) {
			if (sorted[s] != sorted[i]) {
				sorted[++s] = sorted[i];
			}
		}
		// 将原始节点权值替换为离散化后的索引
		for (int i = 1; i <= n; i++) {
			node[i] = kth(node[i]);
		}
	}

	/**
	 * 向邻接表中添加无向边
	 * 时间复杂度：O(1)
	 * 
	 * @param u 边的起点
	 * @param v 边的终点
	 */
	public static void addEdge(int u, int v) {
		next[++cntg] = head[u]; // 链式前向星存储
		to[cntg] = v;
		head[u] = cntg;
	}

	/**
	 * 并查集查找函数，带路径压缩优化
	 * 时间复杂度：平均O(α(n))，α是阿克曼函数的反函数，近似常数
	 * 
	 * @param i 要查找的元素
	 * @return 元素i所在集合的代表元素（根节点）
	 */
	public static int find(int i) {
		if (i != father[i]) {
			father[i] = find(father[i]); // 路径压缩
		}
		return father[i];
	}

	/**
	 * Kruskal重构树构建函数
	 * 时间复杂度：O(m log m) 主要开销来自边的排序
	 * 
	 * 关键性质：
	 * 1. 每个非叶节点对应一条边，其权值为边的权值
	 * 2. 所有原始节点都是叶节点
	 * 3. 从叶节点到根节点的路径上的节点权值严格递增
	 * 4. 任意两个叶节点的LCA对应的边权是它们之间路径上的最大边权
	 */
	public static void kruskalRebuild() {
		// 初始化并查集，每个节点初始时是自己的父节点
		for (int i = 1; i <= n; i++) {
			father[i] = i;
		}
		
		// 按边权从小到大排序
		Arrays.sort(edge, 1, m + 1, (a, b) -> a[2] - b[2]);
		
		cntu = n; // cntu表示当前重构树中的节点数，初始为原始节点数
		
		// 遍历所有边，进行Kruskal重构
		for (int i = 1, fx, fy; i <= m; i++) {
			fx = find(edge[i][0]);
			fy = find(edge[i][1]);
			
			// 如果两个节点不在同一集合中，则合并
			if (fx != fy) {
				// 创建新节点作为两个集合的父节点
				father[fx] = father[fy] = ++cntu;
				father[cntu] = cntu; // 新节点的父节点是自己
				
				// 新节点的权值为当前边的权值
				nodeKey[cntu] = edge[i][2];
				
				// 在重构树中添加边：新节点连接两个子树的根
				addEdge(cntu, fx);
				addEdge(cntu, fy);
			}
		}
	}

	/**
	 * DFS深度优先搜索函数
	 * 时间复杂度：O(n log n)，每个节点需要构建log n层倍增表
	 * 
	 * 功能：
	 * 1. 构建倍增表，用于后续快速查询LCA
	 * 2. 计算每个节点的子树中叶节点数量
	 * 3. 为叶节点分配dfn序号，用于可持久化线段树
	 * 
	 * @param u 当前节点
	 * @param fa 父节点
	 */
	public static void dfs(int u, int fa) {
		// 构建倍增表，stjump[u][p]表示u的2^p级祖先
		stjump[u][0] = fa;
		for (int p = 1; p < MAXH; p++) {
			// 动态规划构建倍增表
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		
		// 先递归处理所有子节点
		for (int e = head[u]; e > 0; e = next[e]) {
			dfs(to[e], u);
		}
		
		// 初始化叶节点信息
		if (u <= n) { // u是原始节点（叶节点）
			leafsiz[u] = 1; // 叶节点的子树大小为1
			leafDfnMin[u] = ++cntd; // 分配dfn序号
			leafseg[cntd] = u; // 记录dfn序号对应的原始节点编号
		} else { // u是非叶节点
			leafsiz[u] = 0; // 初始化为0，后续累加子节点的叶节点数
			leafDfnMin[u] = n + 1; // 初始化为一个较大值
		}
		
		// 后序遍历处理，计算子树信息
		for (int e = head[u]; e > 0; e = next[e]) {
			// 累加子节点的叶节点数量
			leafsiz[u] += leafsiz[to[e]];
			// 更新当前节点的最小dfn序号
			leafDfnMin[u] = Math.min(leafDfnMin[u], leafDfnMin[to[e]]);
		}
	}

	/**
	 * 可持久化线段树的构建函数
	 * 时间复杂度：O(log n)
	 * 
	 * @param l 线段树区间左端点
	 * @param r 线段树区间右端点
	 * @return 新构建的线段树根节点编号
	 */
	public static int build(int l, int r) {
		int rt = ++cntt; // 创建新节点
		numcnt[rt] = 0; // 初始时计数为0
		
		if (l < r) {
			int mid = (l + r) / 2;
			// 递归构建左右子树
			ls[rt] = build(l, mid);
			rs[rt] = build(mid + 1, r);
		}
		return rt;
	}

	/**
	 * 可持久化线段树插入操作
	 * 时间复杂度：O(log n)
	 * 
	 * 功能：在现有版本的基础上插入一个新元素，生成新版本的线段树
	 * 采用路径复制技术，只有被修改的路径上的节点会被复制
	 * 
	 * @param jobi 要插入的元素值（离散化后的）
	 * @param l 当前节点表示的区间左端点
	 * @param r 当前节点表示的区间右端点
	 * @param i 旧版本的线段树根节点
	 * @return 新版本的线段树根节点
	 */
	public static int insert(int jobi, int l, int r, int i) {
		// 创建新节点，复制旧节点的左右子节点
		int rt = ++cntt;
		ls[rt] = ls[i];
		rs[rt] = rs[i];
		numcnt[rt] = numcnt[i] + 1; // 计数加1
		
		if (l < r) {
			int mid = (l + r) / 2;
			// 根据元素值决定在左子树还是右子树插入
			if (jobi <= mid) {
				ls[rt] = insert(jobi, l, mid, ls[rt]);
			} else {
				rs[rt] = insert(jobi, mid + 1, r, rs[rt]);
			}
		}
		return rt;
	}

	/**
	 * 可持久化线段树查询第k大元素
	 * 时间复杂度：O(log n)
	 * 
	 * 功能：通过版本差计算区间内的元素分布，返回第k大的元素
	 * 
	 * @param jobk 要查询的第k大
	 * @param l 当前节点表示的区间左端点
	 * @param r 当前节点表示的区间右端点
	 * @param pre 区间前的版本根节点
	 * @param post 区间后的版本根节点
	 * @return 离散化后的第k大元素值
	 */
	public static int query(int jobk, int l, int r, int pre, int post) {
		// 到达叶子节点，直接返回该值
		if (l == r) {
			return l;
		}
		
		// 计算右子树中的元素个数
		int rsize = numcnt[rs[post]] - numcnt[rs[pre]];
		int mid = (l + r) / 2;
		
		// 如果右子树的元素个数大于等于k，则在右子树中找第k大
		if (rsize >= jobk) {
			return query(jobk, mid + 1, r, rs[pre], rs[post]);
		} else {
			// 否则在左子树中找第k-rsize大
			return query(jobk - rsize, l, mid, ls[pre], ls[post]);
		}
	}

	/**
	 * 核心查询函数：查询从u出发，只经过边权<=x的边能到达的所有节点中第k大的点权
	 * 时间复杂度：O(log n)
	 * 
	 * 解题思路：
	 * 1. 利用倍增法找到u的最高祖先v，使得从u到v的所有边权都<=x
	 * 2. v的子树包含了所有从u出发、只经过边权<=x的边能到达的节点
	 * 3. 通过可持久化线段树查询v的子树中的第k大节点权值
	 * 
	 * @param u 起始节点
	 * @param x 边权上限
	 * @param k 要查询的第k大
	 * @return 第k大的点权值，若不存在返回0
	 */
	public static int kthMax(int u, int x, int k) {
		// 倍增法找到满足条件的最高祖先节点
		for (int p = MAXH - 1; p >= 0; p--) {
			if (stjump[u][p] > 0 && nodeKey[stjump[u][p]] <= x) {
				u = stjump[u][p]; // 向上跳2^p步
			}
		}
		
		// 检查子树中叶节点数量是否足够
		if (leafsiz[u] < k) {
			return 0; // 不存在第k大
		}
		
		// 利用可持久化线段树查询区间[leafDfnMin[u], leafDfnMin[u]+leafsiz[u]-1]中的第k大
		int idx = query(k, 1, s, root[leafDfnMin[u] - 1], root[leafDfnMin[u] + leafsiz[u] - 1]);
		
		// 将离散化的值转回原始值
		return sorted[idx];
	}

	/**
	 * 主函数
	 * 功能：处理输入数据，构建Kruskal重构树，初始化可持久化线段树，处理查询
	 * 时间复杂度：O(m log m + n log n + q log n)
	 * 空间复杂度：O(n log n + m)
	 */
	public static void main(String[] args) {
		// 初始化快速IO工具类
		FastIO io = new FastIO(System.in, System.out);
		
		// 读取输入参数：节点数、边数、查询数
		n = io.nextInt();
		m = io.nextInt();
		q = io.nextInt();
		
		// 读取每个节点的点权
		for (int i = 1; i <= n; i++) {
			node[i] = io.nextInt();
		}
		
		// 读取每条边的信息：起点、终点、边权
		for (int i = 1; i <= m; i++) {
			edge[i][0] = io.nextInt();
			edge[i][1] = io.nextInt();
			edge[i][2] = io.nextInt();
		}
		
		// 离散化点权，减少数据范围
		prepare();
		
		// 构建Kruskal重构树，将边按边权从小到大排序并合并
		kruskalRebuild();
		
		// 对重构树的根节点进行DFS，构建倍增表和子树信息
		// 注意：重构树可能是森林，需要对每个根节点都进行DFS
		for (int i = 1; i <= cntu; i++) {
			if (i == father[i]) { // 找到根节点
				dfs(i, 0);
			}
		}
		
		// 构建可持久化线段树
		root[0] = build(1, s); // 构建空树
		// 按照dfn序号插入所有节点的点权
		for (int i = 1; i <= n; i++) {
			root[i] = insert(node[leafseg[i]], 1, s, root[i - 1]);
		}
		
		// 处理所有查询
		int lastAns = 0; // 记录上一次查询结果，用于强制在线
		for (int i = 1, u, x, k; i <= q; i++) {
			// 读取查询参数
			u = io.nextInt();
			x = io.nextInt();
			k = io.nextInt();
			
			// 强制在线处理：通过异或操作处理真实查询参数
			u = (u ^ lastAns) % n + 1; // 节点编号转换为1~n范围
			x = x ^ lastAns; // 边权上限直接异或
			k = (k ^ lastAns) % n + 1; // 查询第k大，转换为1~n范围
			
			// 执行核心查询：在u可达的边权<=x的区域内找第k大的点权
			lastAns = kthMax(u, x, k);
			
			// 输出查询结果
			if (lastAns == 0) { // 不存在第k大
				io.writelnInt(-1);
			} else { // 存在第k大
				io.writelnInt(lastAns);
			}
		}
		
		// 刷新输出缓冲区
		io.flush();
	}

	// 读写工具类
	static class FastIO {
		private final InputStream is;
		private final OutputStream os;
		private final byte[] inbuf = new byte[1 << 16];
		private int lenbuf = 0;
		private int ptrbuf = 0;
		private final StringBuilder outBuf = new StringBuilder();

		public FastIO(InputStream is, OutputStream os) {
			this.is = is;
			this.os = os;
		}

		private int readByte() {
			if (ptrbuf >= lenbuf) {
				ptrbuf = 0;
				try {
					lenbuf = is.read(inbuf);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (lenbuf == -1) {
					return -1;
				}
			}
			return inbuf[ptrbuf++] & 0xff;
		}

		private int skip() {
			int b;
			while ((b = readByte()) != -1) {
				if (b > ' ') {
					return b;
				}
			}
			return -1;
		}

		public int nextInt() {
			int b = skip();
			if (b == -1) {
				throw new RuntimeException("No more integers (EOF)");
			}
			boolean negative = false;
			if (b == '-') {
				negative = true;
				b = readByte();
			}
			int val = 0;
			while (b >= '0' && b <= '9') {
				val = val * 10 + (b - '0');
				b = readByte();
			}
			return negative ? -val : val;
		}

		public void write(String s) {
			outBuf.append(s);
		}

		public void writeInt(int x) {
			outBuf.append(x);
		}

		public void writelnInt(int x) {
			outBuf.append(x).append('\n');
		}

		public void flush() {
			try {
				os.write(outBuf.toString().getBytes());
				os.flush();
				outBuf.setLength(0);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
