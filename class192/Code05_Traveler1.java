package class192;

// 旅行家，java版
// 给定一张无向图，一共n个点、m条边，每个点给定点权，保证所有点连通
// 一条路径要求，点可以重复经过，边不能重复经过
// 一共有q条操作，格式 x y : 从点x到点y所有可能的路径都走一遍
// 一共q条操作，可能涉及非常多的路径，如果一条路径通过了某个点
// 该点的点权就算入收益，但是以后再有其他路径通过该点，不重复获得收益
// 打印总收益是多少
// 1 <= n <= 5 * 10^5
// 1 <= m <= 2 * 10^6
// 1 <= q <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P7924
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 引入Java IO相关的类
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Code05_Traveler1 - 旅行家问题
 * 
 * 问题描述：
 * - 给定一张无向图，n个节点，m条边，所有节点连通
 * - 每个节点有权值（点权）
 * - 有q条操作，每条操作为(x, y)：从点x到点y的所有可能路径都走一遍
 * - 点可以重复经过，边不能重复经过
 * - 如果一条路径通过了某个点，该点的权值计入收益
 * - 同一个点只计算一次收益（不重复计算）
 * - 求所有操作完成后获得的总收益
 * 
 * 算法思路：
 * 1. 使用Tarjan算法求边双连通分量（E-DCC）
 * 2. 将边双连通分量缩点，形成一棵树
 * 3. 在缩点后的树上统计每个E-DCC的点权之和
 * 4. 对于每个操作(x, y)，将路径上的E-DCC标记为需要计算收益
 * 5. 使用树上差分技术：
 *    - useCnt[x]++, useCnt[y]++, useCnt[lca]--, useCnt[fa(lca)]--
 * 6. 最后DFS累加useCnt，计算哪些E-DCC被至少一条路径经过
 * 7. 将被经过的E-DCC的点权之和相加即为答案
 */
public class Code05_Traveler1 {

	// ------------------- 静态常量定义 -------------------
	
	// 最大节点数量
	public static int MAXN = 500001;
	// 最大边数量的两倍（因为要存储双向边）
	public static int MAXM = 2000001;
	// 最大二进制位数
	public static int MAXP = 20;
	
	// ------------------- 图的输入数据 -------------------
	
	// 节点数量、边数量、查询数量
	public static int n, m, q;
	
	// arr[i]表示节点i的点权
	public static int[] arr = new int[MAXN];
	// 边的端点数组
	public static int[] a = new int[MAXM];
	public static int[] b = new int[MAXM];

	// ------------------- 邻接表数据结构 -------------------
	
	// 头指针数组
	public static int[] head = new int[MAXN];
	// 下一条边指针数组
	public static int[] nxt = new int[MAXM << 1];
	// 边的终点数组
	public static int[] to = new int[MAXM << 1];
	// 当前边的编号计数器
	public static int cntg;

	// ------------------- Tarjan算法相关变量 -------------------
	
	// 深度优先搜索编号数组
	public static int[] dfn = new int[MAXN];
	// Low值数组
	public static int[] low = new int[MAXN];
	// 时间戳计数器
	public static int cntd;

	// ------------------- 栈相关变量 -------------------
	
	// Tarjan算法使用的栈
	public static int[] sta = new int[MAXN];
	// 栈顶指针
	public static int top;

	// ------------------- 边双连通分量相关变量 -------------------
	
	// 节点所属的边双连通分量编号
	public static int[] belong = new int[MAXN];
	// sum[e]表示第e个边双连通分量内所有节点的点权之和
	public static int[] sum = new int[MAXN];
	// 边双连通分量的数量计数器
	public static int ebccCnt;

	// ------------------- RMQ/LCA相关变量 -------------------
	
	// lg2[i]表示log2(i)的向下取整
	public static int[] lg2 = new int[MAXN];
	// rmq[i][j]用于ST表，存储区间[i, i+2^j-1]的最小值对应的节点
	public static int[][] rmq = new int[MAXN][MAXP];
	
	// useCnt[u]表示节点u被路径经过的次数（差分值）
	public static int[] useCnt = new int[MAXN];

	// ------------------- 迭代版Tarjan需要的栈 -------------------
	
	// 自定义栈，用于将递归Tarjan改为迭代版本（防止栈溢出）
	// 栈中每个元素存储4个值：u, preEdge, status, e
	public static int[][] stack = new int[MAXN][4];
	// 当前Tarjan处理的节点
	public static int u, preEdge, status, e;
	// 栈的大小
	public static int stacksize;

	/**
	 * 将当前状态压入自定义栈中
	 * 
	 * @param u 当前节点编号
	 * @param preEdge 来自父节点的边编号
	 * @param status 当前状态：-1表示刚进入节点，0表示从子节点返回，1表示从回边返回
	 * @param e 当前遍历到的边编号
	 */
	public static void push(int u, int preEdge, int status, int e) {
		stack[stacksize][0] = u;
		stack[stacksize][1] = preEdge;
		stack[stacksize][2] = status;
		stack[stacksize][3] = e;
		stacksize++;
	}

	/**
	 * 从自定义栈中弹出状态
	 */
	public static void pop() {
		stacksize--;
		u = stack[stacksize][0];
		preEdge = stack[stacksize][1];
		status = stack[stacksize][2];
		e = stack[stacksize][3];
	}

	/**
	 * 添加一条无向边到邻接表中
	 * 
	 * @param u 边的第一个端点
	 * @param v 边的第二个端点
	 */
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	/**
	 * 递归版Tarjan算法（参考）
	 * 
	 * @param u 当前节点
	 * @param preEdge 来自父节点的边编号
	 */
	public static void tarjan1(int u, int preEdge) {
		dfn[u] = low[u] = ++cntd;
		sta[++top] = u;
		for (int e = head[u]; e > 0; e = nxt[e]) {
			if ((e ^ 1) == preEdge) {
				continue;
			}
			int v = to[e];
			if (dfn[v] == 0) {
				tarjan1(v, e);
				low[u] = Math.min(low[u], low[v]);
			} else {
				low[u] = Math.min(low[u], dfn[v]);
			}
		}
		if (dfn[u] == low[u]) {
			ebccCnt++;
			int pop;
			do {
				pop = sta[top--];
				belong[pop] = ebccCnt;
				sum[ebccCnt] += arr[pop];
			} while (pop != u);
		}
	}

	/**
	 * 迭代版Tarjan算法（使用自定义栈模拟递归）
	 * 
	 * 防止递归深度过大导致栈溢出
	 * 
	 * @param node 起始节点
	 * @param pree 来自父节点的边编号
	 */
	public static void tarjan2(int node, int pree) {
		// 初始化自定义栈
		stacksize = 0;
		// 将起始节点压栈
		push(node, pree, -1, -1);
		
		int v;
		// 当栈不为空时循环
		while (stacksize > 0) {
			// 弹出一个状态
			pop();
			
			// status == -1：表示刚进入节点u，需要处理进入操作
			if (status == -1) {
				// 初始化dfn和low值
				dfn[u] = low[u] = ++cntd;
				// 将当前节点入栈
				sta[++top] = u;
				// 从头开始遍历邻接边
				e = head[u];
			} else {
				// status == 0：表示从子节点返回，需要合并low值
				// status == 1：表示从回边返回，需要合并low值
				v = to[e];
				if (status == 0) {
					low[u] = Math.min(low[u], low[v]);
				} else {
					low[u] = Math.min(low[u], dfn[v]);
				}
				// 继续遍历下一条边
				e = nxt[e];
			}
			
			// 跳过来自父节点的边
			if ((e ^ 1) == preEdge) {
				e = nxt[e];
			}
			
			// 如果还有边没有遍历完
			if (e != 0) {
				v = to[e];
				if (dfn[v] == 0) {
					// 如果子节点没有被访问过，压入子节点的处理状态
					push(u, preEdge, 0, e);
					push(v, e, -1, -1);
				} else {
					// 如果子节点已经被访问过，压入回边的处理状态
					push(u, preEdge, 1, e);
				}
			} else {
				// 所有边都遍历完了，检查是否是E-DCC的根
				if (dfn[u] == low[u]) {
					ebccCnt++;
					int pop;
					do {
						pop = sta[top--];
						belong[pop] = ebccCnt;
						sum[ebccCnt] += arr[pop];
					} while (pop != u);
				}
			}
		}
	}

	/**
	 * 将原图缩点，构建边双连通分量树
	 */
	public static void condense() {
		cntg = 0;
		for (int i = 1; i <= ebccCnt; i++) {
			head[i] = 0;
		}
		for (int i = 1; i <= m; i++) {
			int ebcc1 = belong[a[i]];
			int ebcc2 = belong[b[i]];
			if (ebcc1 != ebcc2) {
				addEdge(ebcc1, ebcc2);
				addEdge(ebcc2, ebcc1);
			}
		}
	}

	/**
	 * RMQ比较函数：返回dfn值较小的节点
	 * 
	 * @param x 第一个节点
	 * @param y 第二个节点
	 * @return dfn值较小的节点
	 */
	public static int getUp(int x, int y) {
		return dfn[x] < dfn[y] ? x : y;
	}

	/**
	 * 在边双连通分量树上进行DFS，构建RMQ/ST表
	 * 
	 * @param u 当前节点
	 * @param fa 父节点
	 */
	public static void dfs(int u, int fa) {
		dfn[u] = ++cntd;
		rmq[dfn[u]][0] = fa;
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (v != fa) {
				dfs(v, u);
			}
		}
	}

	/**
	 * 构建RMQ/ST表
	 */
	public static void buildRmq() {
		cntd = 0;
		dfs(1, 0);
		// 预处理lg2数组
		for (int i = 2; i <= ebccCnt; i++) {
			lg2[i] = lg2[i >> 1] + 1;
		}
		// 构建ST表
		for (int pre = 0, cur = 1; cur <= lg2[ebccCnt]; pre++, cur++) {
			for (int i = 1; i + (1 << cur) - 1 <= ebccCnt; i++) {
				rmq[i][cur] = getUp(rmq[i][pre], rmq[i + (1 << pre)][pre]);
			}
		}
	}

	/**
	 * 获取节点的父节点
	 * 
	 * @param x 节点编号
	 * @return 父节点编号
	 */
	public static int getFather(int x) {
		return rmq[dfn[x]][0];
	}

	/**
	 * 使用RMQ求最近公共祖先（LCA）
	 * 
	 * @param x 第一个节点
	 * @param y 第二个节点
	 * @return 最近公共祖先
	 */
	public static int getLCA(int x, int y) {
		// 如果两个节点相同，直接返回
		if (x == y) {
			return x;
		}
		// 转换为dfs序编号
		x = dfn[x];
		y = dfn[y];
		// 确保x <= y
		if (x > y) {
			int tmp = x; x = y; y = tmp;
		}
		// x需要加1，因为RMQ查询的是开区间
		x++;
		// 计算需要查询的区间长度
		int k = lg2[y - x + 1];
		// 返回区间[x, y]的最小值对应的节点
		return getUp(rmq[x][k], rmq[y - (1 << k) + 1][k]);
	}

	/**
	 * 树上差分的后序遍历
	 * 
	 * 累加子节点的useCnt到父节点
	 * 
	 * @param u 当前节点
	 * @param fa 父节点
	 */
	public static void dfsOnTree(int u, int fa) {
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (v != fa) {
				dfsOnTree(v, u);
				// 累加子节点的useCnt到父节点
				useCnt[u] += useCnt[v];
			}
		}
	}

	/**
	 * 主函数，程序入口
	 * 
	 * @param args 命令行参数
	 * @throws Exception 输入输出异常
	 */
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		cntg = 1;
		n = in.nextInt();
		m = in.nextInt();
		
		// 读取每个节点的点权
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt();
		}
		
		// 读取所有边
		for (int i = 1; i <= m; i++) {
			a[i] = in.nextInt();
			b[i] = in.nextInt();
			addEdge(a[i], b[i]);
			addEdge(b[i], a[i]);
		}
		
		// 使用迭代版Tarjan求边双连通分量
		// tarjan1(1, 0);  // 递归版
		tarjan2(1, 0);   // 迭代版
		
		// 构建边双连通分量树
		condense();
		
		// 构建RMQ表
		buildRmq();
		
		// 读取查询数量
		q = in.nextInt();
		
		// 处理每条查询，使用树上差分
		for (int i = 1, x, y, xylca, lcafa; i <= q; i++) {
			x = in.nextInt();
			y = in.nextInt();
			
			// 将节点转换为它们所属的边双连通分量编号
			x = belong[x];
			y = belong[y];
			
			// 求x和y的最近公共祖先
			xylca = getLCA(x, y);
			// 求lca的父节点
			lcafa = getFather(xylca);
			
			// 树上差分：
			// 路径(x, y)上的所有节点，useCnt++
			// x到根的路径上所有节点 +1
			// y到根的路径上所有节点 +1
			// lca到根的路径上所有节点 -2
			// 所以：x++, y++, xylca--, lcafa--
			useCnt[x]++;
			useCnt[y]++;
			useCnt[xylca]--;
			useCnt[lcafa]--;
		}
		
		// 后序遍历，累加useCnt
		dfsOnTree(1, 0);
		
		// 统计答案：所有useCnt > 0的E-DCC的点权之和
		int ans = 0;
		for (int i = 1; i <= ebccCnt; i++) {
			if (useCnt[i] > 0) {
				ans += sum[i];
			}
		}
		
		out.println(ans);
		out.flush();
		out.close();
	}

	/**
	 * FastReader - 高效输入读取器
	 */
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];
		}

		int nextInt() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}
	}

}
