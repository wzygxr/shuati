package class189; // 声明包名

// 任意两点都有路，java版
// 给定一张n个点，m条边的有向图
// 两点u、v，不管是从u出发能到达v，还是从v出发能到达u，都叫两点间有路
// 判断这个有向图是否能做到，任意两点都有路，能打印"Yes"，不能打印"No"
// 1 <= n <= 1000
// 1 <= m <= 6000
// 测试链接 : https://www.luogu.com.cn/problem/P10944
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // IO异常
import java.io.InputStream; // 输入流
import java.io.OutputStreamWriter; // 输出流写入器
import java.io.PrintWriter; // 打印写入器

/**
 * Code09_HasPath1 类 - 任意两点都有路问题
 * 
 * 【问题分析】
 * 判断有向图是否满足：对于任意两点u、v，u可以到达v或者v可以到达u
 * 这是一个半连通图的判定问题
 * 
 * 【关键观察】
 * 1. 缩点后形成DAG
 * 2. 如果DAG的拓扑序唯一，则原图满足条件
 * 3. 拓扑序唯一的条件：每次只有一个入度为0的节点
 * 
 * 【解题步骤】
 * 1. 使用Tarjan算法找出所有强连通分量
 * 2. 缩点建图
 * 3. 拓扑排序，检查是否每次只有一个入度为0的节点
 */
public class Code09_HasPath1 {

	// ==================== 常量定义 ====================
	public static int MAXN = 1001;   // 最大节点数
	public static int MAXM = 6001;   // 最大边数
	public static int t, n, m;       // 测试用例数、节点数、边数
	
	// 存储原始边
	public static int[] a = new int[MAXM];
	public static int[] b = new int[MAXM];

	// ==================== 邻接表 ====================
	public static int[] head = new int[MAXN];
	public static int[] nxt = new int[MAXM];
	public static int[] to = new int[MAXM];
	public static int cntg;

	// ==================== Tarjan算法 ====================
	public static int[] dfn = new int[MAXN];
	public static int[] low = new int[MAXN];
	public static int cntd;
	public static int[] sta = new int[MAXN];
	public static int top;

	// ==================== SCC结果 ====================
	public static int[] belong = new int[MAXN];
	public static int sccCnt;

	// ==================== 缩点后入度和拓扑排序 ====================
	public static int[] indegree = new int[MAXN];  // 入度
	public static int[] que = new int[MAXN];       // 拓扑排序队列

	// ==================== 添加边 ====================
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	// ==================== Tarjan算法 ====================
	public static void tarjan(int u) {
		dfn[u] = low[u] = ++cntd;
		sta[++top] = u;
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (dfn[v] == 0) {
				tarjan(v);
				low[u] = Math.min(low[u], low[v]);
			} else if (belong[v] == 0) {
				low[u] = Math.min(low[u], dfn[v]);
			}
		}
		if (dfn[u] == low[u]) {
			sccCnt++;
			int pop;
			do {
				pop = sta[top--];
				belong[pop] = sccCnt;
			} while (pop != u);
		}
	}

	// ==================== 缩点操作 ====================
	public static void condense() {
		cntg = 0;  // 重置边计数器
		for (int i = 1; i <= sccCnt; i++) {
			head[i] = 0;  // 重置邻接表头
		}
		// 遍历原图的所有边，建立缩点后的图
		for (int i = 1; i <= m; i++) {
			int scc1 = belong[a[i]];
			int scc2 = belong[b[i]];
			if (scc1 != scc2) {
				indegree[scc2]++;   // scc2的入度加1
				addEdge(scc1, scc2);  // 添加边
			}
		}
	}

	// ==================== 拓扑排序 ====================
	public static boolean topo() {
		int l = 1, r = 0;  // 队列首尾指针
		// 将所有入度为0的节点加入队列
		for (int i = 1; i <= sccCnt; i++) {
			if (indegree[i] == 0) {
				que[++r] = i;
			}
		}
		// 拓扑排序
		while (l <= r) {
			int siz = r - l + 1;  // 当前队列中的节点数
			if (siz > 1) return false;  // 多于一个入度为0的节点，拓扑序不唯一
			int u = que[l++];  // 取出队首
			// 遍历邻接边
			for (int e = head[u]; e > 0; e = nxt[e]) {
				int v = to[e];
				if (--indegree[v] == 0) {  // 入度减1后为0
					que[++r] = v;  // 加入队列
				}
			}
		}
		return true;  // 拓扑序唯一
	}

	// ==================== 主函数 ====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		t = in.nextInt();  // 测试用例数
		
		while (t-- > 0) {
			n = in.nextInt();  // 节点数
			m = in.nextInt();  // 边数
			
			// 重置
			cntg = cntd = sccCnt = 0;
			for (int i = 1; i <= n; i++) {
				head[i] = dfn[i] = belong[i] = indegree[i] = 0;
			}
			
			// 读取边
			for (int i = 1; i <= m; i++) {
				a[i] = in.nextInt();
				b[i] = in.nextInt();
				addEdge(a[i], b[i]);
			}
			
			// 运行Tarjan算法
			for (int i = 1; i <= n; i++) {
				if (dfn[i] == 0) tarjan(i);
			}
			
			// 缩点
			condense();
			
			// 拓扑排序判断
			boolean ans = topo();
			out.println(ans ? "Yes" : "No");
		}
		
		out.flush();
		out.close();
	}

	// ==================== 快速读入 ====================
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
				if (len <= 0) return -1;
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
