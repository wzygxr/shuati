package class192;

// 神会作弊，java版
// 给定一张无向图，一共n个点、m条边，保证所有点连通，两点间出现重边只连一次
// 边双连通分量缩点后，如果点x和点y属于同一个边双连通分量，认为两点距离是1
// 如果不属于同一个边双连通分量，距离为缩点后的树上，两点简单路径上的节点个数
// 一共有q条查询，格式 x y : 计算点x和点y的距离，打印距离的二进制形式
// 1 <= n <= 10000
// 1 <= m <= 50000
// 测试链接 : https://www.luogu.com.cn/problem/P2783
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 引入Java IO相关的类
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Code04_GodCheat1 - 神会作弊问题
 * 
 * 问题描述：
 * - 给定一张无向图，n个节点，m条边，所有节点连通
 * - 两点之间如果有多条边，只连接一次（去重）
 * - 定义距离：
 *   - 如果两点在同一个边双连通分量内，距离为1
 *   - 如果不在同一个边双连通分量内，距离为缩点后树上两点路径上的节点数
 * - q条查询，求两点距离的二进制表示
 * 
 * 算法思路：
 * 1. 读取边并去重（相同的边只保留一条）
 * 2. 使用Tarjan算法求边双连通分量（E-DCC）
 * 3. 将边双连通分量缩点，形成一棵树
 * 4. 在缩点后的树上进行DFS，预处理深度和ST表
 * 5. 使用LCA（最近公共祖先）求两点距离
 * 6. 将距离+1后转换为二进制输出（+1是因为同E-DCC内距离为1）
 */
public class Code04_GodCheat1 {

	// ------------------- 静态常量定义 -------------------
	
	// 最大节点数量
	public static int MAXN = 10001;
	// 最大边数量
	public static int MAXM = 50001;
	// 最大二进制位数（log2(10000) < 15）
	public static int MAXP = 15;
	
	// ------------------- 图的输入数据 -------------------
	
	// 节点数量、边数量、查询数量
	public static int n, m, q;
	
	// 边的端点数组，使用二维数组存储
	// edgeArr[i][0]和edgeArr[i][1]表示第i条边的两个端点（已排序）
	public static int[][] edgeArr = new int[MAXM][2];

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
	// 边双连通分量的数量计数器
	public static int ebccCnt;

	// ------------------- LCA相关变量 -------------------
	
	// dep[u]表示节点u在树中的深度（根节点深度为0）
	public static int[] dep = new int[MAXN];
	// stjump[u][p]表示节点u的第2^p个祖先
	public static int[][] stjump = new int[MAXN][MAXP];

	/**
	 * 添加一条无向边到邻接表中
	 * 
	 * @param u 边的第一个端点
	 * @param v 边的第二个端点
	 */
	public static void addEdge(int u, int v) {
		// 将当前边插入到节点u的邻接表头部
		nxt[++cntg] = head[u];
		// 设置当前边的终点为v
		to[cntg] = v;
		// 更新节点u的头指针
		head[u] = cntg;
	}

	/**
	 * 对边进行去重排序
	 * 
	 * 题目要求：两点之间出现重边只连一次
	 * 
	 * 步骤：
	 * 1. 先将所有边的端点按 (u, v) 字典序排序
	 * 2. 遍历排序后的边，相同的边只保留一条
	 */
	public static void buildGraph() {
		// 使用Arrays.sort对边进行排序
		// 自定义比较器：先按第一个端点排序，再按第二个端点排序
		Arrays.sort(edgeArr, 1, m + 1, (a, b) -> a[0] != b[0] ? (a[0] - b[0]) : (a[1] - b[1]));
		
		// 去重：k表示保留的边的数量
		int k = 1;
		
		// 从第二条边开始遍历
		for (int i = 2; i <= m; i++) {
			// 如果当前边与前一条边不同，则保留
			if (edgeArr[k][0] != edgeArr[i][0] || edgeArr[k][1] != edgeArr[i][1]) {
				// 将当前边的信息复制到edgeArr[k+1]
				edgeArr[++k][0] = edgeArr[i][0];
				edgeArr[k][1] = edgeArr[i][1];
			}
		}
		
		// 将去重后的边添加到邻接表中
		for (int i = 1; i <= k; i++) {
			addEdge(edgeArr[i][0], edgeArr[i][1]);
			addEdge(edgeArr[i][1], edgeArr[i][0]);
		}
		
		// 更新边的数量为去重后的数量
		m = k;
	}

	/**
	 * Tarjan算法求边双连通分量（E-DCC）
	 * 
	 * @param u 当前正在访问的节点
	 * @param preEdge 来自父节点的边编号
	 */
	public static void tarjan(int u, int preEdge) {
		// 初始化dfn和low值为当前时间戳
		dfn[u] = low[u] = ++cntd;
		// 将当前节点入栈
		sta[++top] = u;
		
		// 遍历节点u的所有邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			// 如果这条边是来自父节点的边，则跳过
			if ((e ^ 1) == preEdge) {
				continue;
			}
			
			// 获取边的终点节点
			int v = to[e];
			
			// 如果节点v还没有被访问过
			if (dfn[v] == 0) {
				// 递归访问子节点
				tarjan(v, e);
				// 更新low值
				low[u] = Math.min(low[u], low[v]);
			} else {
				// 如果节点v已经被访问过
				low[u] = Math.min(low[u], dfn[v]);
			}
		}
		
		// 如果dfn[u] == low[u]，找到一个新的边双连通分量
		if (dfn[u] == low[u]) {
			ebccCnt++;
			int pop;
			// 弹出栈中节点
			do {
				pop = sta[top--];
				belong[pop] = ebccCnt;
			} while (pop != u);
		}
	}

	/**
	 * 将原图缩点，构建边双连通分量树
	 */
	public static void condense() {
		// 重置边计数器
		cntg = 0;
		
		// 清空新图的邻接表
		for (int i = 1; i <= ebccCnt; i++) {
			head[i] = 0;
		}
		
		// 遍历去重后的所有边
		for (int i = 1; i <= m; i++) {
			// 获取边的两端点所属的边双连通分量编号
			int ebcc1 = belong[edgeArr[i][0]];
			int ebcc2 = belong[edgeArr[i][1]];
			
			// 如果两端点属于不同的边双连通分量，添加边
			if (ebcc1 != ebcc2) {
				addEdge(ebcc1, ebcc2);
				addEdge(ebcc2, ebcc1);
			}
		}
	}

	/**
	 * 在边双连通分量树上进行DFS，预处理深度和ST表
	 * 
	 * @param u 当前节点
	 * @param fa 父节点编号
	 */
	public static void dfs(int u, int fa) {
		// 设置当前节点的深度为父节点深度+1
		dep[u] = dep[fa] + 1;
		
		// 设置第2^0 = 1个祖先为父节点
		stjump[u][0] = fa;
		
		// 预处理ST表
		// stjump[u][p] = stjump[stjump[u][p-1]][p-1]
		// 即u的第2^p个祖先 = u的第2^(p-1)个祖先的第2^(p-1)个祖先
		for (int p = 1; p < MAXP; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		
		// 遍历当前节点的所有邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			// 获取边的终点节点
			int v = to[e];
			
			// 避免走回头路
			if (v != fa) {
				// 递归处理子树
				dfs(v, u);
			}
		}
	}

	/**
	 * 求两个节点的最近公共祖先（LCA）
	 * 
	 * 使用二分跳跃法：
	 * 1. 先让较深的节点跳到与较浅节点同一深度
	 * 2. 然后同时从高位到低位跳，找到最近公共祖先
	 * 
	 * @param a 第一个节点
	 * @param b 第二个节点
	 * @return 两个节点的最近公共祖先
	 */
	public static int getLca(int a, int b) {
		// 确保a是深度较大的节点
		if (dep[a] < dep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		
		// 第一步：让节点a跳到与节点b同一深度
		// 从高位到低位遍历
		for (int p = MAXP - 1; p >= 0; p--) {
			// 如果a的第2^p个祖先的深度仍然大于等于b的深度
			if (dep[stjump[a][p]] >= dep[b]) {
				// 跳到该祖先
				a = stjump[a][p];
			}
		}
		
		// 如果此时a和b相同，说明b是a的祖先
		if (a == b) {
			return a;
		}
		
		// 第二步：同时从高位到低位跳，找最近公共祖先
		for (int p = MAXP - 1; p >= 0; p--) {
			// 如果a和b的第2^p个祖先不同，则同时往上跳
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		
		// 此时a和b是相邻的节点，它们的父节点就是最近公共祖先
		return stjump[a][0];
	}

	/**
	 * 计算两个节点之间的距离
	 * 
	 * 距离 = 深度[a] + 深度[b] - 2 * 深度[LCA]
	 * 
	 * @param x 第一个节点（在E-DCC树中）
	 * @param y 第二个节点（在E-DCC树中）
	 * @return 两点之间的距离
	 */
	public static int getDist(int x, int y) {
		return dep[x] + dep[y] - 2 * dep[getLca(x, y)];
	}

	/**
	 * 主函数，程序入口
	 * 
	 * @param args 命令行参数
	 * @throws Exception 输入输出异常
	 */
	public static void main(String[] args) throws Exception {
		// 创建快速输入读取器
		FastReader in = new FastReader(System.in);
		// 创建快速输出写入器
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 初始化边计数器为1
		cntg = 1;
		
		// 读取节点数量n和边数量m
		n = in.nextInt();
		m = in.nextInt();
		
		// 读取边的信息
		for (int i = 1, u, v; i <= m; i++) {
			u = in.nextInt();
			v = in.nextInt();
			// 将边的端点排序，小的在前，大的在后（方便去重）
			edgeArr[i][0] = Math.min(u, v);
			edgeArr[i][1] = Math.max(u, v);
		}
		
		// 对边进行去重，并构建邻接表
		buildGraph();
		
		// 运行Tarjan算法求边双连通分量
		tarjan(1, 0);
		
		// 构建边双连通分量树
		condense();
		
		// 在边双连通分量树上进行DFS，预处理LCA
		dfs(1, 0);
		
		// 读取查询数量
		q = in.nextInt();
		
		// 处理每条查询
		for (int i = 1, x, y; i <= q; i++) {
			// 读取查询的两个节点
			x = in.nextInt();
			y = in.nextInt();
			
			// 将节点转换为它们所属的边双连通分量编号
			x = belong[x];
			y = belong[y];
			
			// 计算距离，然后+1（因为同E-DCC内距离为1）
			// 转换为二进制字符串输出
			out.println(Integer.toBinaryString(getDist(x, y) + 1));
		}
		
		// 刷新输出缓冲区
		out.flush();
		// 关闭输出流
		out.close();
	}

	/**
	 * FastReader - 高效输入读取器
	 */
	static class FastReader {
		// 字节缓冲区
		private final byte[] buffer = new byte[1 << 16];
		// 缓冲区指针和当前缓冲区长度
		private int ptr = 0, len = 0;
		// 输入流
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		/**
		 * 从缓冲区读取一个字节
		 */
		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];
		}

		/**
		 * 读取下一个整数
		 */
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
