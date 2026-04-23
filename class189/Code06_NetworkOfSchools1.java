package class189; // 声明包名

// 校园网络，java版
// 一共有n个节点，m条有向边，消息只能顺着有向边传递
// 打印至少需要在几个节点投放消息，才能让所有节点，都能收到消息
// 打印至少需要添加几条边，才能让任意两个节点之间，都能传递消息
// 1 <= n <= 10^4
// 1 <= m <= 5 * 10^4
// 测试链接 : https://www.luogu.com.cn/problem/P2812
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // IO异常
import java.io.InputStream; // 输入流
import java.io.OutputStreamWriter; // 输出流写入器
import java.io.PrintWriter; // 打印写入器

/**
 * Code06_NetworkOfSchools1 类 - 校园网络问题
 * 
 * 【问题分析】
 * 两个问题：
 * 1. 至少需要在几个节点投放消息，才能让所有节点都收到
 * 2. 至少需要添加几条边，才能让任意两个节点之间都能传递消息
 * 
 * 【问题1解答】
 * 缩点后，入度为0的强连通分量必须被选中
 * 答案 = 入度为0的SCC数量
 * 
 * 【问题2解答】
 * 要让图强连通，需要将DAG变成强连通图
 * 需要添加的边数 = max(入度为0的SCC数, 出度为0的SCC数)
 * 特殊情况：如果只有一个SCC，不需要添加边
 */
public class Code06_NetworkOfSchools1 {

	// ==================== 常量定义 ====================
	public static int MAXN = 100001;  // 最大节点数
	public static int MAXM = 500001;  // 最大边数
	public static int n, m;           // 节点数和边数
	
	// 存储原始边信息
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

	// ==================== 缩点后入度和出度 ====================
	public static int[] outdegree = new int[MAXN];  // 出度
	public static int[] indegree = new int[MAXN];   // 入度

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

	// ==================== 主函数 ====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		n = in.nextInt();  // 读取节点数
		m = 0;             // 边数初始化为0
		
		// 读取每个节点的邻接列表
		for (int i = 1; i <= n; i++) {
			int x = in.nextInt();  // 读取邻接节点
			while (x != 0) {       // 以0作为结束标志
				m++;
				a[m] = i;          // 记录起点
				b[m] = x;          // 记录终点
				addEdge(a[m], b[m]);  // 添加边
				x = in.nextInt();  // 读取下一个
			}
		}
		
		// 运行Tarjan算法
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0) tarjan(i);
		}
		
		// 特殊情况：如果只有一个SCC
		if (sccCnt == 1) {
			out.println("1");   // 问题1答案：只需在一个节点投放
			out.println("0");   // 问题2答案：不需要添加边
		} else {
			// 计算缩点后各SCC的入度和出度
			for (int i = 1; i <= m; i++) {
				int scc1 = belong[a[i]];
				int scc2 = belong[b[i]];
				if (scc1 != scc2) {
					outdegree[scc1]++;  // scc1的出度加1
					indegree[scc2]++;   // scc2的入度加1
				}
			}
			
			// 统计入度为0和出度为0的SCC数量
			int outZero = 0, inZero = 0;
			for (int i = 1; i <= sccCnt; i++) {
				if (outdegree[i] == 0) outZero++;
				if (indegree[i] == 0) inZero++;
			}
			
			// 问题1答案：入度为0的SCC数
			out.println(inZero);
			// 问题2答案：max(入度为0数, 出度为0数)
			out.println(Math.max(outZero, inZero));
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
