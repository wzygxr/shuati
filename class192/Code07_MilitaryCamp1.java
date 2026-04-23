package class192;

// 建造军营，java版
// 一共n个城市、m条道路，道路是无向边，保证所有城市连通
// 你可以选择任何城市，在城市内建造军营，至少要选一座城市
// 你可以选择任何道路，在道路上派兵看守，也可以一条都不选
// 选择的城市集合 + 选择的道路集合，被认为是一种方案
// 敌人会袭击任意一条道路，如果有兵看守就不会被切断，否则会被切断
// 敌人袭击之后，如果造成任意两座军营无法连通，那么算你失败
// 确保不会失败的情况下，计算方案数，答案对 1000000007 取余
// 1 <= n <= 5 * 10^5
// 1 <= m <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P8867
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 引入Java IO相关的类
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Code07_MilitaryCamp1 - 建造军营问题
 * 
 * 问题描述：
 * - n个城市，m条道路（无向边），保证连通
 * - 可以选择一些城市建造军营（至少选一个）
 * - 可以选择一些道路派兵看守（也可以不选）
 * - 选择的城市集合 + 选择的道路集合 = 一种方案
 * - 敌人会袭击任意一条道路：
 *   - 如果有兵看守，道路不会被切断
 *   - 如果没有兵看守，道路会被切断
 * - 袭击后，如果任意两座军营无法连通，则失败
 * - 求在确保不会失败的情况下，有多少种方案
 * - 答案对1000000007取模
 * 
 * 算法思路：
 * 1. 使用Tarjan算法求边双连通分量（E-DCC）
 * 2. 将边双连通分量缩点，形成一棵树（割边树）
 * 3. 在树上进行DP计算方案数
 * 4. 考虑每个E-DCC内部的选择：
 *    - 在一个大小为s的E-DCC中，可以选择建造军营的方式有 2^s - 1 种
 *      （不能所有城市都不建，所以减去全不选的情况）
 * 5. 考虑割边的选择：
 *    - 每条割边可以派兵看守，也可以不派兵看守（2种选择）
 *    - 但如果某个子树中没有任何军营，则该子树与父节点之间的割边必须派兵
 * 6. 使用DP合并子树的方案数
 */
public class Code07_MilitaryCamp1 {

	// ------------------- 静态常量定义 -------------------
	
	// 最大节点数量
	public static int MAXN = 500001;
	// 最大边数量
	public static int MAXM = 1000001;
	// 取模数值（1000000007，1e9+7）
	public static int MOD = 1000000007;
	
	// ------------------- 图的输入数据 -------------------
	
	// 节点数量、边数量
	public static int n, m;
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
	// ebccSiz[e]表示第e个E-DCC包含的节点数量
	public static int[] ebccSiz = new int[MAXN];
	// 边双连通分量的数量计数器
	public static int ebccCnt;

	// ------------------- DP相关变量 -------------------
	
	// power2[i] = 2^i % MOD，预计算2的幂次
	public static long[] power2 = new long[MAXM];
	
	// dp[u] : 子树u上的方案数
	//   条件：子树u内部考虑了建造军营的情况（至少选一个城市）
	//   条件：从子树u中的军营到节点u之间的割边都派兵，其他割边自由选择
	// dp[u] = (2^ebccSiz[u] - 1) * f(子树u中割边的选择)
	public static long[] dp = new long[MAXN];
	
	// bridge[u] : 子树u上割边的数量
	public static int[] bridge = new int[MAXN];

	// ------------------- 迭代版Tarjan需要的栈 -------------------
	
	// 自定义栈，用于将递归Tarjan改为迭代版本
	public static int[][] stack = new int[MAXN][5];
	public static int u, preEdge, status, fa, e;
	public static int stacksize;

	/**
	 * 将当前状态压入自定义栈中
	 */
	public static void push(int u, int preEdge, int status, int fa, int e) {
		stack[stacksize][0] = u;
		stack[stacksize][1] = preEdge;
		stack[stacksize][2] = status;
		stack[stacksize][3] = fa;
		stack[stacksize][4] = e;
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
		fa = stack[stacksize][3];
		e = stack[stacksize][4];
	}

	/**
	 * 添加一条无向边到邻接表中
	 */
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	/**
	 * 递归版Tarjan算法（参考）
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
			ebccSiz[ebccCnt] = 0;
			int pop;
			do {
				pop = sta[top--];
				belong[pop] = ebccCnt;
				ebccSiz[ebccCnt]++;
			} while (pop != u);
		}
	}

	/**
	 * 迭代版Tarjan算法
	 */
	public static void tarjan2(int node, int pree) {
		stacksize = 0;
		push(node, pree, -1, 0, -1);
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
				} else {
					low[u] = Math.min(low[u], dfn[v]);
				}
				e = nxt[e];
			}
			if ((e ^ 1) == preEdge) {
				e = nxt[e];
			}
			if (e != 0) {
				v = to[e];
				if (dfn[v] == 0) {
					push(u, preEdge, 0, 0, e);
					push(v, e, -1, 0, -1);
				} else {
					push(u, preEdge, 1, 0, e);
				}
			} else {
				if (dfn[u] == low[u]) {
					ebccCnt++;
					ebccSiz[ebccCnt] = 0;
					int pop;
					do {
						pop = sta[top--];
						belong[pop] = ebccCnt;
						ebccSiz[ebccCnt]++;
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
	 * 递归版树形DP（参考）
	 * 
	 * 计算每个子树的方案数
	 */
	public static void dpOnTree1(int u, int fa) {
		// 情况1：不考虑下方的节点，在u自己的E-DCC里选点造军营
		// 2^ebccSiz[u] - 1 表示至少选一个城市的方案数
		dp[u] = power2[ebccSiz[u]] - 1;
		bridge[u] = 0;
		
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (v != fa) {
				dpOnTree1(v, u);
				
				// 合并子树的方案数
				// 情况2：之前部分的方案数 * 子树v完全不造军营时割边的自由发挥 * u到v的割边自由发挥
				// 情况3：之前部分完全不造军营时割边的自由发挥 * 子树v的方案数
				// 情况4：之前部分的方案数 * 子树v的方案数
				dp[u] = (dp[u] * power2[bridge[v]] * 2 % MOD
						+ power2[bridge[u]] * dp[v] % MOD
						+ dp[u] * dp[v] % MOD)
						% MOD;
				
				// 累加子树v中的割边数量，加上u到v之间的这条割边
				bridge[u] += bridge[v] + 1;
			}
		}
	}

	/**
	 * 迭代版树形DP
	 */
	public static void dpOnTree2(int cur, int father) {
		stacksize = 0;
		push(cur, 0, 0, father, -1);
		while (stacksize > 0) {
			pop();
			if (e == -1) {
				// 进入新节点，初始化
				dp[u] = power2[ebccSiz[u]] - 1;
				bridge[u] = 0;
				e = head[u];
			} else {
				// 继续遍历邻接边
				e = nxt[e];
			}
			if (e != 0) {
				push(u, 0, 0, fa, e);
				int v = to[e];
				if (v != fa) {
					push(v, 0, 0, u, -1);
				}
			} else {
				// 处理子树的合并
				for (int ei = head[u]; ei > 0; ei = nxt[ei]) {
					int v = to[ei];
					if (v != fa) {
						dp[u] = (dp[u] * power2[bridge[v] + 1] % MOD
								+ power2[bridge[u]] * dp[v] % MOD
								+ dp[u] * dp[v] % MOD) % MOD;
						bridge[u] += bridge[v] + 1;
					}
				}
			}
		}
	}

	/**
	 * 计算最终答案
	 */
	public static long compute() {
		// 预计算2的幂次
		power2[0] = 1;
		for (int i = 1; i <= m; i++) {
			power2[i] = power2[i - 1] * 2 % MOD;
		}
		
		// 使用迭代版DP
		// dpOnTree1(1, 0);
		dpOnTree2(1, 0);
		
		// 总割边数量
		int total = bridge[1];
		// 根节点的方案数
		long ans = dp[1];
		
		// 考虑其他连通块的情况
		// 如果选择某个子树的根节点作为唯一有军营的E-DCC
		for (int i = 2; i <= ebccCnt; i++) {
			ans = (ans + dp[i] * power2[total - bridge[i] - 1] % MOD) % MOD;
		}
		
		// 考虑非割边的选择（不在树中的边，即E-DCC内部的边）
		// 共有 m - total 条边
		ans = ans * power2[m - total] % MOD;
		
		return ans;
	}

	/**
	 * 主函数，程序入口
	 */
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		cntg = 1;
		n = in.nextInt();
		m = in.nextInt();
		
		// 读取所有边
		for (int i = 1; i <= m; i++) {
			a[i] = in.nextInt();
			b[i] = in.nextInt();
			addEdge(a[i], b[i]);
			addEdge(b[i], a[i]);
		}
		
		// 使用迭代版Tarjan求边双连通分量
		// tarjan1(1, 0);
		tarjan2(1, 0);
		
		// 构建边双连通分量树
		condense();
		
		// 计算答案
		long ans = compute();
		
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
