package class189; // 声明包名

// 消息扩散，java版
// 一共有n个城市，给定m条道路，道路可以传递消息，但道路是单向
// 你有一个消息，需要让所有城市都收到，计算至少要在几个城市发布该消息
// 1 <= n <= 10^5
// 1 <= m <= 5 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P2002
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // IO异常
import java.io.InputStream; // 输入流
import java.io.OutputStreamWriter; // 输出流写入器
import java.io.PrintWriter; // 打印写入器

/**
 * Code05_NewsSpread1 类 - 消息扩散问题
 * 
 * 【问题分析】
 * 需要让所有城市都收到消息，求最少需要在几个城市发布
 * 
 * 【关键观察】
 * 1. 一个强连通分量内的城市可以互相到达，只需在一个城市发布即可
 * 2. 缩点后形成DAG，需要考虑入度为0的强连通分量
 * 3. 消息只能从入度为0的SCC开始传播
 * 4. 答案 = 入度为0的强连通分量数量
 * 
 * 【解题步骤】
 * 1. 使用Tarjan算法找出所有强连通分量
 * 2. 缩点，统计每个SCC的入度
 * 3. 统计入度为0的SCC数量，即为答案
 */
public class Code05_NewsSpread1 {

	// ==================== 常量定义 ====================
	public static int MAXN = 100001;  // 最大节点数 10^5+1
	public static int MAXM = 500001;  // 最大边数 5*10^5+1
	public static int n, m;           // 节点数和边数
	
	// 存储原始边信息
	public static int[] a = new int[MAXM];  // 边的起点
	public static int[] b = new int[MAXM];  // 边的终点

	// ==================== 邻接表 ====================
	public static int[] head = new int[MAXN];  // 邻接表头
	public static int[] nxt = new int[MAXM];   // 下一条边
	public static int[] to = new int[MAXM];    // 目标节点
	public static int cntg;                    // 边计数器

	// ==================== Tarjan算法 ====================
	public static int[] dfn = new int[MAXN];   // DFS序
	public static int[] low = new int[MAXN];   // Low Link
	public static int cntd;                    // 时间戳
	public static int[] sta = new int[MAXN];   // 栈
	public static int top;                     // 栈顶指针

	// ==================== SCC结果 ====================
	public static int[] belong = new int[MAXN]; // 节点所属SCC
	public static int sccCnt;                   // SCC数量

	// ==================== 缩点后入度 ====================
	public static int[] indegree = new int[MAXN]; // 每个SCC的入度

	// ==================== 迭代版辅助结构 ====================
	public static int[][] stack = new int[MAXN][3];
	public static int u, status, e;
	public static int stacksize;

	// ==================== 迭代版辅助方法 ====================
	public static void push(int u, int status, int e) {
		stack[stacksize][0] = u;
		stack[stacksize][1] = status;
		stack[stacksize][2] = e;
		stacksize++;
	}

	public static void pop() {
		stacksize--;
		u = stack[stacksize][0];
		status = stack[stacksize][1];
		e = stack[stacksize][2];
	}

	// ==================== 添加边 ====================
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	// ==================== 递归版Tarjan ====================
	public static void tarjan1(int u) {
		dfn[u] = low[u] = ++cntd;
		sta[++top] = u;
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (dfn[v] == 0) {
				tarjan1(v);
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

	// ==================== 迭代版Tarjan ====================
	public static void tarjan2(int node) {
		stacksize = 0;
		push(node, -1, -1);
		int v;
		while (stacksize > 0) {
			pop();
			if (status == -1) {
				dfn[u] = low[u] = ++cntd;
				sta[++top] = u;
				e = head[u];
			} else {
				v = to[e];
				if (status == 0) {
					low[u] = Math.min(low[u], low[v]);
				}
				if (status == 1 && belong[v] == 0) {
					low[u] = Math.min(low[u], dfn[v]);
				}
				e = nxt[e];
			}
			if (e != 0) {
				v = to[e];
				if (dfn[v] == 0) {
					push(u, 0, e);
					push(v, -1, -1);
				} else {
					push(u, 1, e);
				}
			} else {
				if (dfn[u] == low[u]) {
					sccCnt++;
					int pop;
					do {
						pop = sta[top--];
						belong[pop] = sccCnt;
					} while (pop != u);
				}
			}
		}
	}

	// ==================== 主函数 ====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		n = in.nextInt();  // 城市数
		m = in.nextInt();  // 道路数
		
		// 读取边
		for (int i = 1; i <= m; i++) {
			a[i] = in.nextInt();
			b[i] = in.nextInt();
			addEdge(a[i], b[i]);
		}
		
		// 运行Tarjan算法（使用迭代版避免栈溢出）
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0) tarjan2(i);
		}
		
		// 计算缩点后各SCC的入度
		for (int i = 1; i <= m; i++) {
			int scc1 = belong[a[i]];
			int scc2 = belong[b[i]];
			if (scc1 != scc2) {
				indegree[scc2]++;  // scc2的入度加1
			}
		}
		
		// 统计入度为0的SCC数量
		int ans = 0;
		for (int i = 1; i <= sccCnt; i++) {
			if (indegree[i] == 0) ans++;
		}
		
		out.println(ans);  // 输出答案
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
