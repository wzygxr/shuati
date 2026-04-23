package class192;

// 最多的桥，java版
// 给定一张无向图，一共n个点、m条边，保证所有点连通
// 沿途的边只能经过一次，找到能通过最多割边的路径，打印割边的数量
// 1 <= n、m <= 3 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/CF1000E
// 测试链接 : https://codeforces.com/problemset/problem/1000/E
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 引入Java IO相关的类
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Code02_MostBridges1 - 最多的桥问题
 * 
 * 问题描述：
 * - 给定一张无向图，n个节点，m条边，所有节点连通
 * - 路径要求：边只能经过一次
 * - 找到一条能通过最多割边（桥）的路径
 * - 输出最多能经过的割边数量
 * 
 * 算法思路：
 * 1. 使用Tarjan算法求边双连通分量（E-DCC）
 * 2. 将边双连通分量缩点，形成一棵树（树中的边都是割边）
 * 3. 在这棵树上求树的直径（最长路径）
 * 4. 树的直径长度即为最多能经过的割边数量
 * 
 * 证明：
 * - 在原图中，每条割边连接两个不同的边双连通分量
 * - 在边双连通分量内部，可以任意经过所有边而不重复
 * - 所以从一点到另一点，经过的割边数量 = 缩点后树上的简单路径上的边数
 * - 要经过最多割边，就是求缩点后树的直径
 */
public class Code02_MostBridges1 {

	// ------------------- 静态常量定义 -------------------
	
	// 最大节点数量，加1用于数组下标从1开始
	public static int MAXN = 300001;
	// 最大边数量，加1用于数组下标从1开始
	public static int MAXM = 300001;
	
	// ------------------- 图的输入数据 -------------------
	
	// 节点数量和边数量
	public static int n, m;
	
	// 边的端点数组，a[i]和b[i]表示第i条边的两个端点
	public static int[] a = new int[MAXM];
	public static int[] b = new int[MAXM];

	// ------------------- 邻接表数据结构 -------------------
	
	// 头指针数组，head[u]表示节点u的第一条边的编号
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

	// ------------------- 树的直径相关变量 -------------------
	
	// dist[u]表示从根节点到节点u的距离（经过的割边数）
	public static int[] dist = new int[MAXN];
	// 树的直径（最长路径的长度）
	public static int diameter;

	/**
	 * 添加一条无向边到邻接表中
	 * 
	 * @param u 边的第一个端点
	 * @param v 边的第二个端点
	 * 
	 * 使用链式前向星（邻接表）存储图
	 */
	public static void addEdge(int u, int v) {
		// 将当前边插入到节点u的邻接表头部
		nxt[++cntg] = head[u];
		// 设置当前边的终点为v
		to[cntg] = v;
		// 更新节点u的头指针，指向新添加的边
		head[u] = cntg;
	}

	/**
	 * Tarjan算法求边双连通分量（E-DCC）
	 * 
	 * 边双连通分量是指图中删除任意一条边后仍然连通的极大子图
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
	 * 
	 * 缩点后，原来的割边成为连接新节点的边
	 * 新图是一棵树（因为割边不可能形成环）
	 */
	public static void condense() {
		// 重置边计数器
		cntg = 0;
		
		// 清空新图的邻接表
		for (int i = 1; i <= ebccCnt; i++) {
			head[i] = 0;
		}
		
		// 遍历原图的所有边
		for (int i = 1; i <= m; i++) {
			// 获取边的两端点所属的边双连通分量编号
			int ebcc1 = belong[a[i]];
			int ebcc2 = belong[b[i]];
			
			// 如果两端点属于不同的边双连通分量，添加边
			if (ebcc1 != ebcc2) {
				addEdge(ebcc1, ebcc2);
				addEdge(ebcc2, ebcc1);
			}
		}
	}

	/**
	 * 在边双连通分量树上进行树形DP，计算树的直径
	 * 
	 * 树的直径：树中任意两点之间最短路径的最大值
	 * 
	 * 使用一次DFS可以找到直径的一端
	 * 然后从该端点出发进行第二次DFS即可得到直径长度
	 * 
	 * 这里使用一次DFS同时计算直径：
	 * - 维护dist[u]表示从当前节点到其子树中某个最远节点的距离
	 * - 遍历所有子树，计算两个子树的dist之和，更新直径
	 * 
	 * @param u 当前节点
	 * @param fa 父节点编号
	 */
	public static void dpOnTree(int u, int fa) {
		// 遍历当前节点的所有邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			// 获取边的终点节点
			int v = to[e];
			
			// 避免走回头路
			if (v != fa) {
				// 递归处理子树
				dpOnTree(v, u);
				
				// 更新树的直径：取dist[u] + dist[v] + 1的最大值
				// dist[u]是当前节点到其子树中最远节点的距离
				// dist[v]是子节点v到其子树中最远节点的距离
				// +1表示加上u到v这条边（一条割边）
				diameter = Math.max(diameter, dist[u] + dist[v] + 1);
				
				// 更新dist[u]：取经过子节点v的最长距离
				// dist[v] + 1表示从u出发，经过v能到达的最远距离
				dist[u] = Math.max(dist[u], dist[v] + 1);
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
		// 创建快速输入读取器
		FastReader in = new FastReader(System.in);
		// 创建快速输出写入器
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 初始化边计数器为1
		cntg = 1;
		
		// 读取节点数量n和边数量m
		n = in.nextInt();
		m = in.nextInt();
		
		// 读取所有边的信息
		for (int i = 1; i <= m; i++) {
			// 读取边的两个端点
			a[i] = in.nextInt();
			b[i] = in.nextInt();
			// 添加无向边到图中
			addEdge(a[i], b[i]);
			addEdge(b[i], a[i]);
		}
		
		// 运行Tarjan算法求边双连通分量
		// 从节点1开始（题目保证图连通）
		tarjan(1, 0);
		
		// 构建边双连通分量树
		condense();
		
		// 在边双连通分量树上进行DP，计算树的直径
		dpOnTree(1, 0);
		
		// 输出树的直径（最多能经过的割边数量）
		out.println(diameter);
		
		// 刷新输出缓冲区
		out.flush();
		// 关闭输出流
		out.close();
	}

	/**
	 * FastReader - 高效输入读取器
	 * 
	 * 使用字节缓冲区实现快速读取整数
	 */
	static class FastReader {
		// 字节缓冲区
		private final byte[] buffer = new byte[1 << 16];
		// 缓冲区指针和当前缓冲区长度
		private int ptr = 0, len = 0;
		// 输入流
		private final InputStream in;

		/**
		 * 构造函数
		 * 
		 * @param in 输入流
		 */
		FastReader(InputStream in) {
			this.in = in;
		}

		/**
		 * 从缓冲区读取一个字节
		 * 
		 * @return 读取的字节值，如果到达文件末尾返回-1
		 * @throws IOException IO异常
		 */
		private int readByte() throws IOException {
			// 如果缓冲区指针已达到缓冲区末尾
			if (ptr >= len) {
				// 尝试从输入流读取数据到缓冲区
				len = in.read(buffer);
				// 重置指针到缓冲区开始
				ptr = 0;
				// 如果读取到的数据长度<=0，说明到达文件末尾
				if (len <= 0)
					return -1;
			}
			// 返回缓冲区中指针位置的字节，并让指针后移
			return buffer[ptr++];
		}

		/**
		 * 读取下一个整数
		 * 
		 * @return 读取的整数值
		 * @throws IOException IO异常
		 */
		int nextInt() throws IOException {
			int c;
			// 跳过空白字符
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			
			// 处理负数情况
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			
			// 读取数字字符并转换为整数
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			
			// 如果是负数，返回负值
			return neg ? -val : val;
		}
	}

}
