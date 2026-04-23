package class192;

// 越狱老虎桥，java版
// 给定一张无向图，一共n个点、m条边，保证所有点连通
// 每条边给定边权，表示破坏这条边需要花费的钱数
// 敌人可能在任意两点之间新增一条边，新增的这条边无法被破坏
// 敌人新增一条边之后，你的目标是只破坏一条边，就让图变成两个连通区
// 你不知道敌人会选择哪两个端点来新增这条边，你需要尽可能的做好准备
// 假设遭遇最差情况，打印你至少准备多少钱才能完成目标，无法完成目标打印-1
// 1 <= n <= 5 * 10^5
// 1 <= m <= 10^6
// 1 <= 边权 <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P5234
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 引入Java IO相关的类
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Code03_PrisonBreak1 - 越狱老虎桥问题
 * 
 * 问题描述：
 * - 给定一张无向图，n个节点，m条边，所有节点连通
 * - 每条边有权值，表示破坏该边需要的花费
 * - 敌人可以在任意两点之间新增一条无法被破坏的边
 * - 敌人新增边后，你需要只破坏一条边就能让图不连通
 * - 求在最坏情况下，你需要准备的最少资金
 * - 如果无法做到，输出-1
 * 
 * 算法思路：
 * 1. 使用Tarjan算法求边双连通分量（E-DCC）
 * 2. 将边双连通分量缩点，形成一棵树（割边树）
 * 3. 问题转化为：在割边树中，敌人可能添加一条边
 * 4. 添加一条边后，会在树上形成一个环
 * 5. 为了让图不连通，我们需要破坏环上的一条边
 * 6. 使用二分搜索找到最小的花费阈值
 * 7. 使用树形DP验证某个阈值是否可行
 * 
 * 关键观察：
 * - 在边双连通分量内，任意两点间至少有两条独立路径
 * - 只有割边才是必须破坏的边
 * - 如果敌人添加的边连接了树的两个节点，会形成环
 * - 要让图不连通，需要破坏环上的一条割边
 * - 所以我们需要在所有可能的环中，找一条花费最小的割边
 * - 但题目要求只破坏一条边，所以我们需要准备足够的钱来破坏
 *   任意一条可能出现在敌人添加的边所形成的环上的割边
 */
public class Code03_PrisonBreak1 {

	// ------------------- 静态常量定义 -------------------
	
	// 最大节点数量
	public static int MAXN = 500001;
	// 最大边数量
	public static int MAXM = 1000001;
	
	// ------------------- 图的输入数据 -------------------
	
	// 节点数量、边数量、边权最大值
	public static int n, m, maxv;
	
	// 边的端点数组
	public static int[] a = new int[MAXM];
	public static int[] b = new int[MAXM];
	// 边的权值数组（破坏该边需要的花费）
	public static int[] c = new int[MAXM];

	// ------------------- 邻接表数据结构 -------------------
	
	// 头指针数组
	public static int[] head = new int[MAXN];
	// 下一条边指针数组
	public static int[] nxt = new int[MAXM << 1];
	// 边的终点数组
	public static int[] to = new int[MAXM << 1];
	// 边的权值数组
	public static int[] weight = new int[MAXM << 1];
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

	// ------------------- 树的直径和割边计数相关变量 -------------------
	
	// dist[u]表示从当前节点到其子树中某个最远节点的距离
	public static int[] dist = new int[MAXN];
	// 树的直径（最长路径的长度）
	public static int diameter;
	// edgeCnt表示在某个阈值下，树中被视为"有效割边"的数量
	public static int edgeCnt;

	/**
	 * 添加一条无向边到邻接表中
	 * 
	 * @param u 边的第一个端点
	 * @param v 边的第二个端点
	 * @param w 边的权值
	 */
	public static void addEdge(int u, int v, int w) {
		// 将当前边插入到节点u的邻接表头部
		nxt[++cntg] = head[u];
		// 设置当前边的终点为v
		to[cntg] = v;
		// 设置当前边的权值
		weight[cntg] = w;
		// 更新节点u的头指针
		head[u] = cntg;
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
		
		// 遍历原图的所有边
		for (int i = 1; i <= m; i++) {
			// 获取边的两端点所属的边双连通分量编号
			int ebcc1 = belong[a[i]];
			int ebcc2 = belong[b[i]];
			// 获取边的权值
			int w = c[i];
			
			// 如果两端点属于不同的边双连通分量，添加边
			if (ebcc1 != ebcc2) {
				addEdge(ebcc1, ebcc2, w);
				addEdge(ebcc2, ebcc1, w);
			}
		}
	}

	/**
	 * 在边双连通分量树上进行树形DP
	 * 
	 * @param u 当前节点
	 * @param fa 父节点编号
	 * @param limit 当前花费阈值
	 * 
	 * 如果边的权值 <= limit，则认为这条边需要派兵看守
	 * 有效割边：权值 <= limit 的割边
	 */
	public static void dpOnTree(int u, int fa, int limit) {
		// 遍历当前节点的所有邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			// 获取边的终点节点
			int v = to[e];
			
			// 避免走回头路
			if (v != fa) {
				// 递归处理子树
				dpOnTree(v, u, limit);
				
				// 如果这条边的权值 <= limit，则视为需要派兵看守的割边
				// weight[e]存储的是边的原始权值（不是两倍）
				// 注意：邻接表中每条无向边有两条有向边，weight[e]可能需要取原值
				int w = weight[e] <= limit ? 1 : 0;
				
				// 累加有效割边数量
				edgeCnt += w;
				
				// 更新树的直径
				diameter = Math.max(diameter, dist[u] + dist[v] + w);
				
				// 更新dist[u]
				dist[u] = Math.max(dist[u], dist[v] + w);
			}
		}
	}

	/**
	 * 检查给定的花费阈值limit是否可行
	 * 
	 * @param limit 花费阈值
	 * @return 如果阈值可行返回true，否则返回false
	 * 
	 * 可行条件：diameter < edgeCnt
	 * 即最长路径上的有效割边数小于总的有效割边数
	 * 这意味着存在某条有效割边不在最长路径上
	 * 敌人添加的边形成的环必然包含最长路径
	 * 所以我们需要一条不在最长路径上的有效割边来应对敌人
	 */
	public static boolean check(int limit) {
		// 初始化dist数组
		for (int i = 1; i <= ebccCnt; i++) {
			dist[i] = 0;
		}
		
		// 初始化直径和有效割边计数
		diameter = edgeCnt = 0;
		
		// 从根节点开始DP
		dpOnTree(1, 0, limit);
		
		// 如果最长路径上的有效割边数小于总的有效割边数，则可行
		return diameter < edgeCnt;
	}

	/**
	 * 使用二分搜索找到最小的可行花费
	 * 
	 * @return 最小的可行花费，如果不存在可行解返回-1
	 */
	public static int compute() {
		// 二分搜索的范围是[1, maxv]
		int l = 1, r = maxv, mid, ans = -1;
		
		while (l <= r) {
			// 取中间值
			mid = (l + r) / 2;
			
			// 如果阈值mid可行
			if (check(mid)) {
				// 记录答案，尝试更小的阈值
				ans = mid;
				r = mid - 1;
			} else {
				// 如果阈值mid不可行，增加阈值
				l = mid + 1;
			}
		}
		
		// 返回答案
		return ans;
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
		
		// 初始化最大权值
		maxv = 0;
		
		// 读取所有边的信息
		for (int i = 1; i <= m; i++) {
			// 读取边的两个端点和权值
			a[i] = in.nextInt();
			b[i] = in.nextInt();
			c[i] = in.nextInt();
			
			// 添加无向边到图中（暂时用0填充权值）
			addEdge(a[i], b[i], 0);
			addEdge(b[i], a[i], 0);
			
			// 更新最大权值
			maxv = Math.max(maxv, c[i]);
		}
		
		// 运行Tarjan算法求边双连通分量
		tarjan(1, 0);
		
		// 构建边双连通分量树
		condense();
		
		// 使用二分搜索计算最小花费
		out.println(compute());
		
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
