package class189; // 声明包名

// 间谍网络，java版
// 一共有n个间谍，其中有p个间谍可以贿赂，给定各自的价格，剩下的间谍不能贿赂
// 然后给定m个控制关系，控制关系是单向的，也是可传递的
// 如果间谍a能控制间谍b，间谍b能控制间谍c，那么间谍a就能控制间谍c
// 当你贿赂了某个间谍，该间谍连同他能控制的所有人，都能被你控制
// 如果你不能控制所有间谍，打印"NO"，然后打印不能控制的间谍中，最小的编号
// 如果你可以控制所有间谍，打印"YES"，然后打印需要花费的最少钱数
// 1 <= n <= 3000    0 <= 各自的价格 <= 20000    1 <= m <= 8000
// 测试链接 : https://www.luogu.com.cn/problem/P1262
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // IO异常
import java.io.InputStream; // 输入流
import java.io.OutputStreamWriter; // 输出流写入器
import java.io.PrintWriter; // 打印写入器

/**
 * Code07_Spy1 类 - 间谍网络问题
 * 
 * 【问题分析】
 * 需要控制所有间谍，可以通过贿赂某些间谍来实现
 * 贿赂一个间谍后，可以控制该间谍及其能到达的所有间谍
 * 
 * 【解题思路】
 * 1. 使用Tarjan算法找出所有强连通分量
 * 2. 对于每个SCC，如果其中至少有一个间谍可以贿赂，记录最小贿赂价格
 * 3. 缩点后，入度为0的SCC必须被贿赂（因为没有其他SCC能控制它们）
 * 4. 如果某个入度为0的SCC中没有可贿赂的间谍，则无法控制所有间谍
 * 
 * 【关键步骤】
 * 1. 建图时只从可贿赂的间谍开始DFS（或建图后检查）
 * 2. 检查是否有间谍未被分配到SCC（不可达）
 * 3. 计算入度为0的SCC的最小贿赂价格之和
 */
public class Code07_Spy1 {

	// ==================== 常量定义 ====================
	public static int MAXN = 3001;      // 最大节点数
	public static int MAXM = 8001;      // 最大边数
	public static int INF = 1000000001; // 无穷大，表示不可贿赂
	public static int n, p, m;          // 间谍数、可贿赂数、控制关系数

	// 存储每个间谍的贿赂价格
	public static int[] cost = new int[MAXN];
	
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
	public static int[] minVal = new int[MAXN];  // 每个SCC的最小贿赂价格
	public static int sccCnt;

	// ==================== 缩点后入度 ====================
	public static int[] indegree = new int[MAXN];

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
			minVal[sccCnt] = INF;  // 初始化为无穷大
			int pop;
			do {
				pop = sta[top--];
				belong[pop] = sccCnt;
				minVal[sccCnt] = Math.min(minVal[sccCnt], cost[pop]);  // 更新最小价格
			} while (pop != u);
		}
	}

	// ==================== 主函数 ====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		n = in.nextInt();  // 间谍总数
		p = in.nextInt();  // 可贿赂间谍数
		
		// 初始化所有间谍的贿赂价格为无穷大（不可贿赂）
		for (int i = 1; i <= n; i++) {
			cost[i] = INF;
		}
		
		// 读取可贿赂间谍及其价格
		for (int i = 1; i <= p; i++) {
			int u = in.nextInt();  // 间谍编号
			int c = in.nextInt();  // 贿赂价格
			cost[u] = c;           // 设置价格
		}
		
		m = in.nextInt();  // 控制关系数
		
		// 读取控制关系
		for (int i = 1; i <= m; i++) {
			a[i] = in.nextInt();
			b[i] = in.nextInt();
			addEdge(a[i], b[i]);
		}
		
		// 从可贿赂的间谍开始运行Tarjan算法
		for (int i = 1; i <= n; i++) {
			if (cost[i] != INF && dfn[i] == 0) {
				tarjan(i);
			}
		}
		
		// 检查是否所有间谍都被控制
		boolean check = true;
		int ans = 0;
		for (int i = 1; i <= n; i++) {
			if (belong[i] == 0) {  // 间谍i未被控制
				check = false;
				ans = i;           // 记录最小的未被控制间谍编号
				break;
			}
		}
		
		if (!check) {
			// 无法控制所有间谍
			out.println("NO");
			out.println(ans);
		} else {
			// 计算缩点后各SCC的入度
			for (int i = 1; i <= m; i++) {
				int scc1 = belong[a[i]];
				int scc2 = belong[b[i]];
				if (scc1 != scc2) {
					indegree[scc2]++;
				}
			}
			
			// 计算总费用：入度为0的SCC需要贿赂
			for (int i = 1; i <= sccCnt; i++) {
				if (indegree[i] == 0) {
					ans += minVal[i];
				}
			}
			
			out.println("YES");
			out.println(ans);
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
