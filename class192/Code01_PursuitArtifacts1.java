package class192;

// 追寻文物，java版
// 给定一张无向图，一共n个点、m条边，保证所有点连通
// 每条边除了端点之外，还有一个属性，值为1表示该边上有商品，值为0表示该边上无商品
// 给定起点s和终点t，路途怎么走随意，但是沿途每条边只能经过一次
// 从s到t的路途中能遇到商品打印"YES"，否则打印"NO"
// 1 <= n、m <= 3 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/CF652E
// 测试链接 : https://codeforces.com/problemset/problem/652/E
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 引入Java IO相关的类，用于快速输入输出
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Code01_PursuitArtifacts1 - 追寻文物问题
 * 
 * 问题描述：
 * - 给定一张无向图，n个节点，m条边，所有节点连通
 * - 每条边有一个属性：1表示该边上有商品，0表示无商品
 * - 给定起点s和终点t，路径可以随意选择，但每条边只能经过一次
 * - 判断从s到t的路径上是否能遇到商品
 * 
 * 算法思路：
 * 1. 使用Tarjan算法求边双连通分量（E-DCC）
 * 2. 将边双连通分量缩点，形成一棵树
 * 3. 在缩点后的树上进行DFS，检查从s所在的块到t所在的块路径上是否有商品
 * 4. 如果路径上有商品输出YES，否则输出NO
 */
public class Code01_PursuitArtifacts1 {

	// ------------------- 静态常量定义 -------------------
	
	// 最大节点数量，加1用于数组下标从1开始
	public static int MAXN = 300001;
	// 最大边数量，加1用于数组下标从1开始
	public static int MAXM = 300001;
	
	// ------------------- 图的输入数据 -------------------
	
	// 节点数量
	public static int n, m;
	// 起点s和终点t
	public static int s, t;
	
	// 边的端点数组，a[i]和b[i]表示第i条边的两个端点
	public static int[] a = new int[MAXM];
	// 边的另一个端点数组
	public static int[] b = new int[MAXM];
	// 边的商品标记数组，c[i]表示第i条边上是否有商品（1表示有，0表示无）
	public static int[] c = new int[MAXM];

	// ------------------- 邻接表数据结构 -------------------
	
	// 头指针数组，head[u]表示节点u的第一条边的编号
	public static int[] head = new int[MAXN];
	// 下一条边指针数组，nxt[e]表示编号为e的边的下一条边
	public static int[] nxt = new int[MAXM << 1];
	// 边的终点数组，to[e]表示编号为e的边的终点节点
	public static int[] to = new int[MAXM << 1];
	// 边的权重数组，weight[e]表示编号为e的边的商品标记
	public static int[] weight = new int[MAXM << 1];
	// 当前边的编号计数器，从1开始
	public static int cntg;

	// ------------------- Tarjan算法相关变量 -------------------
	
	// 深度优先搜索编号数组，dfn[u]表示节点u被访问的时间戳
	public static int[] dfn = new int[MAXN];
	// Low值数组，low[u]表示节点u及其子树中所有节点能追溯到的最小dfn值
	public static int[] low = new int[MAXN];
	// 时间戳计数器
	public static int cntd;

	// ------------------- 栈相关变量 -------------------
	
	// Tarjan算法使用的栈，用于存储当前搜索路径上的节点
	public static int[] sta = new int[MAXN];
	// 栈顶指针
	public static int top;

	// ------------------- 边双连通分量相关变量 -------------------
	
	// 节点所属的边双连通分量编号，belong[u]表示节点u属于哪个E-DCC
	public static int[] belong = new int[MAXN];
	// 边双连通分量的价值，val[e]表示第e个E-DCC内是否包含商品边
	public static int[] val = new int[MAXN];
	// 边双连通分量的数量计数器
	public static int ebccCnt;

	/**
	 * 添加一条无向边到邻接表中
	 * 
	 * @param u 边的第一个端点
	 * @param v 边的第二个端点
	 * @param w 边的权重（商品标记）
	 * 
	 * 使用链式前向星（邻接表）存储图
	 * 每条无向边会添加两条有向边，编号分别为cntg和cntg^1
	 */
	public static void addEdge(int u, int v, int w) {
		// 将当前边插入到节点u的邻接表头部
		nxt[++cntg] = head[u];
		// 设置当前边的终点为v
		to[cntg] = v;
		// 设置当前边的权重（商品标记）
		weight[cntg] = w;
		// 更新节点u的头指针，指向新添加的边
		head[u] = cntg;
	}

	/**
	 * Tarjan算法求边双连通分量（E-DCC）
	 * 
	 * 边双连通分量是指图中删除任意一条边后仍然连通的极大子图
	 * 
	 * @param u 当前正在访问的节点
	 * @param preEdge 来自父节点的边编号（用于避免走回头路）
	 * 
	 * 算法原理：
	 * 1. dfn[u]记录节点u被访问的时间戳
	 * 2. low[u]记录节点u及其子树中所有节点能追溯到的最小dfn值
	 * 3. 如果dfn[u] == low[u]，说明找到了一个边双连通分量的根
	 * 4. 栈中从u到栈顶的所有节点属于同一个边双连通分量
	 */
	public static void tarjan(int u, int preEdge) {
		// 初始化dfn和low值为当前时间戳
		dfn[u] = low[u] = ++cntd;
		// 将当前节点入栈
		sta[++top] = u;
		
		// 遍历节点u的所有邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			// 如果这条边是来自父节点的边（双向边），则跳过
			// 利用异或特性：无向边的两条有向边编号互为e^1
			if ((e ^ 1) == preEdge) {
				continue;
			}
			
			// 获取边的终点节点
			int v = to[e];
			
			// 如果节点v还没有被访问过（树边）
			if (dfn[v] == 0) {
				// 递归访问子节点v
				tarjan(v, e);
				// 更新low值，取子节点low值和当前low值的较小者
				low[u] = Math.min(low[u], low[v]);
			} else {
				// 如果节点v已经被访问过（回边），更新low值
				low[u] = Math.min(low[u], dfn[v]);
			}
		}
		
		// 如果dfn[u] == low[u]，说明u是某个边双连通分量的根
		if (dfn[u] == low[u]) {
			// 找到一个新的边双连通分量
			ebccCnt++;
			int pop;
			// 弹出栈中节点，直到弹出u为止
			do {
				// 弹出栈顶节点
				pop = sta[top--];
				// 标记该节点属于当前找到的边双连通分量
				belong[pop] = ebccCnt;
			} while (pop != u);
		}
	}

	/**
	 * 将原图缩点，构建边双连通分量树
	 * 
	 * 将原图中的每个边双连通分量收缩为一个节点
	 * 原来的桥（割边）成为连接这些新节点的边
	 */
	public static void condense() {
		// 重置边计数器，准备构建新图
		cntg = 0;
		
		// 清空新图（边双连通分量树）的邻接表
		for (int i =1; i <= ebccCnt; i++) {
			head[i] = 0;
		}
		
		// 遍历原图的所有边
		for (int i = 1; i <= m; i++) {
			// 获取边的两端点所属的边双连通分量编号
			int ebcc1 = belong[a[i]];
			int ebcc2 = belong[b[i]];
			// 获取边的商品标记
			int w = c[i];
			
			// 如果两端点属于同一个边双连通分量
			if (ebcc1 == ebcc2) {
				// 如果该边上有商品，则该边双连通分量的val设为1
				if (w == 1) {
					val[ebcc1] = 1;
				}
			} else {
				// 如果两端点属于不同的边双连通分量，添加边
				// 这条边是连接两个边双连通分量的桥
				addEdge(ebcc1, ebcc2, w);
				addEdge(ebcc2, ebcc1, w);
			}
		}
	}

	/**
	 * 在边双连通分量树上进行DFS检查
	 * 
	 * @param u 当前节点
	 * @param fa 父节点编号
	 * @param ok 累计状态，表示路径上是否已经遇到过商品
	 * @return 是否能在路径上遇到商品
	 */
	public static boolean check(int u, int fa, boolean ok) {
		// 如果当前边双连通分量内包含商品边，则标记为true
		ok |= val[u] > 0;
		
		// 如果到达目标节点t，返回当前状态
		if (u == t) {
			return ok;
		}
		
		// 遍历当前节点的所有邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			// 获取边的终点节点
			int v = to[e];
			// 获取边的商品标记
			int w = weight[e];
			
			// 避免走回头路
			if (v != fa) {
				// 递归检查子节点，累加路径上是否有商品
				if (check(v, u, ok || w > 0)) {
					return true;
				}
			}
		}
		
		// 遍历完所有邻接边都没有找到商品，返回false
		return false;
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
		
		// 初始化边计数器为1（因为0^1=1，可以用来标记无父边）
		cntg = 1;
		
		// 读取节点数量n和边数量m
		n = in.nextInt();
		m = in.nextInt();
		
		// 读取所有边的信息
		for (int i = 1; i <= m; i++) {
			// 读取边的两个端点
			a[i] = in.nextInt();
			b[i] = in.nextInt();
			// 读取边的商品标记
			c[i] = in.nextInt();
			// 添加无向边到图中（暂时忽略商品标记，用0填充）
			addEdge(a[i], b[i], 0);
			addEdge(b[i], a[i], 0);
		}
		
		// 运行Tarjan算法求边双连通分量
		// 从节点1开始（题目保证图连通）
		tarjan(1, 0);
		
		// 构建边双连通分量树
		condense();
		
		// 读取起点s和终点t
		s = in.nextInt();
		t = in.nextInt();
		
		// 将起点和终点转换为它们所属的边双连通分量编号
		s = belong[s];
		t = belong[t];
		
		// 检查从s到t的路径上是否有商品
		if (check(s, 0, false)) {
			out.println("YES");
		} else {
			out.println("NO");
		}
		
		// 刷新输出缓冲区
		out.flush();
		// 关闭输出流
		out.close();
	}

	/**
	 * FastReader - 高效输入读取器
	 * 
	 * 使用字节缓冲区实现快速读取整数，适用于大数据量输入
	 */
	static class FastReader {
		// 字节缓冲区，大小为64KB
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
			// 跳过空白字符（空格、制表符、换行符等）
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
				// 字符'0'到'9'的ASCII码为48到57
				val = val * 10 + (c - '0');
				c = readByte();
			}
			
			// 如果是负数，返回负值
			return neg ? -val : val;
		}
	}

}
