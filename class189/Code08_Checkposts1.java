package class189; // 声明包名

// 检查站，java版
// 给定一张n个点，m条边的有向图，每个点有点权
// 一个强连通分量内部，选点权最小的点，就能控制该连通分量里的所有点
// 你的目的是选择若干点之后，图上所有的点都能控制，打印最小点权和
// 打印选择点的方案数，方案数可能较大，对 1000000007 取余
// 1 <= n <= 10^5
// 1 <= m <= 3 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/CF427C
// 测试链接 : https://codeforces.com/problemset/problem/427/C
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // IO异常
import java.io.InputStream; // 输入流
import java.io.OutputStreamWriter; // 输出流写入器
import java.io.PrintWriter; // 打印写入器

/**
 * Code08_Checkposts1 类 - 检查站问题
 * 
 * 【问题分析】
 * 需要选择一些点，使得所有点都被控制
 * 一个SCC内只需要选一个点（选点权最小的）就能控制整个SCC
 * 
 * 【解题思路】
 * 1. 使用Tarjan算法找出所有强连通分量
 * 2. 对每个SCC，找到最小点权值minVal和该最小值的出现次数minCnt
 * 3. 最小点权和 = 所有SCC的minVal之和
 * 4. 方案数 = 所有SCC的minCnt之积（对MOD取模）
 */
public class Code08_Checkposts1 {

	// ==================== 常量定义 ====================
	public static int MAXN = 100001;       // 最大节点数
	public static int MAXM = 300001;       // 最大边数
	public static int MOD = 1000000007;    // 模数
	public static int n, m;                // 节点数和边数
	public static int[] val = new int[MAXN];  // 每个节点的点权

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
	public static int[] minVal = new int[MAXN];  // 每个SCC的最小点权
	public static int[] minCnt = new int[MAXN];  // 每个SCC最小点权的出现次数
	public static int sccCnt;

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
			minVal[sccCnt] = 1000000001;  // 初始化为极大值
			minCnt[sccCnt] = 0;           // 初始化出现次数为0
			int pop;
			do {
				pop = sta[top--];
				belong[pop] = sccCnt;
				// 更新最小点权和出现次数
				if (minVal[sccCnt] > val[pop]) {
					minVal[sccCnt] = val[pop];
					minCnt[sccCnt] = 1;
				} else if (minVal[sccCnt] == val[pop]) {
					minCnt[sccCnt]++;
				}
			} while (pop != u);
		}
	}

	// ==================== 主函数 ====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		n = in.nextInt();  // 节点数
		
		// 读取每个节点的点权
		for (int i = 1; i <= n; i++) {
			val[i] = in.nextInt();
		}
		
		m = in.nextInt();  // 边数
		
		// 读取边
		for (int i = 1, u, v; i <= m; i++) {
			u = in.nextInt();
			v = in.nextInt();
			addEdge(u, v);
		}
		
		// 运行Tarjan算法
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0) tarjan(i);
		}
		
		// 计算答案
		long ans1 = 0;  // 最小点权和
		long ans2 = 1;  // 方案数
		for (int i = 1; i <= sccCnt; i++) {
			ans1 += minVal[i];                    // 累加最小点权
			ans2 = (ans2 * minCnt[i]) % MOD;      // 累乘方案数（取模）
		}
		
		out.println(ans1 + " " + ans2);
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
