package class194;

// 警察问题 Java 版
// 问题描述：给定一张无向图，包含 n 个节点和 m 条边，保证图是连通的且无重边
// 查询类型：共有 q 条查询，分为两种类型
//           类型 1 a b c d：已知 c 和 d 之间有边，如果删除这条边，判断 a 和 b 是否连通
//           类型 2 a b c：如果删除点 c，判断 a 和 b 是否连通
// 数据范围：1 <= n <= 10^5，1 <= m <= 5 * 10^5，1 <= q <= 3 * 10^5
// 测试链接：https://www.luogu.com.cn/problem/P4334
// 提交说明：提交时请将类名改为 "Main"
// 注意事项：本题对时间复杂度要求较高，Java 版本可能因卡常无法通过
//           推荐使用 C++ 版本（Code07_Policija2.java），逻辑完全一致且可通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;

public class Code07_Policija1 {

	// 最大节点数，根据题目数据范围设置为 100001
	public static int MAXN = 100001;
	// 最大边数，根据题目数据范围设置为 500001
	public static int MAXM = 500001;
	// n：节点数，m：边数，q：查询数，cntn：圆方树的总节点数（初始为原图节点数）
	public static int n, m, q, cntn;

	// 原图的链式前向星存储结构
	// head1[u]：节点 u 的第一条边的索引
	// next1[e]：边 e 的下一条边的索引
	// to1[e]：边 e 指向的节点
	// cnt1：边的计数器，初始为 1（避免异或 0 出错）
	public static int[] head1 = new int[MAXN];
	public static int[] next1 = new int[MAXM << 1];  // 无向图，边数翻倍
	public static int[] to1 = new int[MAXM << 1];
	public static int cnt1;

	// 圆方树的链式前向星存储结构
	// head2[u]：圆方树中节点 u 的第一条边的索引
	// next2[e]：圆方树中边 e 的下一条边的索引
	// to2[e]：圆方树中边 e 指向的节点
	// cnt2：圆方树边的计数器，初始为 0
	public static int[] head2 = new int[MAXN << 1];  // 圆方树节点数最多为 2n
	public static int[] next2 = new int[MAXM << 2];  // 圆方树边数最多为 4m
	public static int[] to2 = new int[MAXM << 2];
	public static int cnt2;

	// Tarjan 算法相关变量
	// dfn[u]：节点 u 的深度优先搜索时间戳
	// low[u]：节点 u 能够回溯到的最早的时间戳
	// cntd：时间戳计数器，初始为 0
	public static int[] dfn = new int[MAXN];
	public static int[] low = new int[MAXN];
	public static int cntd;

	// Tarjan 算法中使用的栈，存储当前连通分量的节点
	// cnts：栈顶指针，初始为 0
	public static int[] sta = new int[MAXN];
	public static int cnts;

	// 存储桥边对应的方点
	// 键：(min(u, v) << 32) | max(u, v)，值：对应的方点编号
	public static HashMap<Long, Integer> cutMap = new HashMap<>();

	// 树链剖分相关变量
	// fa[u]：节点 u 的父节点
	// dep[u]：节点 u 的深度
	// siz[u]：以节点 u 为根的子树大小
	// son[u]：节点 u 的重儿子
	// top[u]：节点 u 所在链的顶端节点
	public static int[] fa = new int[MAXN << 1];
	public static int[] dep = new int[MAXN << 1];
	public static int[] siz = new int[MAXN << 1];
	public static int[] son = new int[MAXN << 1];
	public static int[] top = new int[MAXN << 1];

	// 向原图添加一条无向边
	// u：边的一个端点
	// v：边的另一个端点
	public static void addEdge1(int u, int v) {
		next1[++cnt1] = head1[u];  // 新边的 next 指向当前节点的第一条边
		to1[cnt1] = v;              // 新边指向节点 v
		head1[u] = cnt1;             // 更新当前节点的第一条边为新边
	}

	// 向圆方树添加一条无向边
	// u：边的一个端点
	// v：边的另一个端点
	public static void addEdge2(int u, int v) {
		next2[++cnt2] = head2[u];  // 新边的 next 指向当前节点的第一条边
		to2[cnt2] = v;              // 新边指向节点 v
		head2[u] = cnt2;             // 更新当前节点的第一条边为新边
	}

	// 记录桥边对应的方点
	// x：桥边的一个端点
	// y：桥边的另一个端点
	// cut：桥边对应的方点编号
	public static void addCut(int x, int y, int cut) {
		int a = Math.min(x, y);  // 确保 a <= b
		int b = Math.max(x, y);
		// 将 a 和 b 合并为一个长整型作为键
		cutMap.put(((long) a << 32) | b, cut);
	}

	// 获取桥边对应的方点
	// x：桥边的一个端点
	// y：桥边的另一个端点
	// 返回值：桥边对应的方点编号，0 表示不是桥边
	public static int getCut(int x, int y) {
		int a = Math.min(x, y);  // 确保 a <= b
		int b = Math.max(x, y);
		// 从 cutMap 中获取对应的方点编号
		Integer ans = cutMap.get(((long) a << 32) | b);
		return ans == null ? 0 : ans;
	}

	// Tarjan 算法，用于建立圆方树
	// u：当前处理的节点
	// preEdge：父边的索引，用于避免重复访问父节点
	public static void tarjan(int u, int preEdge) {
		dfn[u] = low[u] = ++cntd;  // 初始化时间戳和 low 值
		sta[++cnts] = u;             // 将当前节点压入栈
		// 遍历当前节点的所有邻边
		for (int e = head1[u]; e > 0; e = next1[e]) {
			if ((e ^ 1) == preEdge) {  // 跳过父边（无向图的反向边）
				continue;
			}
			int v = to1[e];  // 获取邻边指向的节点
			if (dfn[v] == 0) {  // 如果邻节点未被访问过
				tarjan(v, e);  // 递归处理邻节点
				// 更新当前节点的 low 值
				low[u] = Math.min(low[u], low[v]);
				// 如果邻节点的 low 值大于等于当前节点的 dfn 值
				// 说明当前节点是一个割点，需要建立圆方树的方点
				if (low[v] >= dfn[u]) {
					cntn++;  // 圆方树节点数加 1（新增一个方点）
					// 如果邻节点的 low 值大于当前节点的 dfn 值
					// 说明当前边是桥边，记录桥边对应的方点
					if (low[v] > dfn[u]) {
						addCut(u, v, cntn);
					}
					// 建立方点与当前节点的双向边
					addEdge2(cntn, u);
					addEdge2(u, cntn);
					int pop;
					// 将栈中从 v 到当前节点的所有节点弹出
					// 并建立这些节点与新方点的双向边
					do {
						pop = sta[cnts--];  // 弹出栈顶节点
						addEdge2(cntn, pop);  // 建立方点与弹出节点的边
						addEdge2(pop, cntn);  // 建立弹出节点与方点的边
					} while (pop != v);  // 直到弹出节点为 v
				}
			} else {  // 如果邻节点已被访问过
				// 更新当前节点的 low 值
				low[u] = Math.min(low[u], dfn[v]);
			}
		}
	}

	// 深度优先搜索，用于树链剖分的第一遍处理
	// 计算节点的父节点、深度、子树大小和重儿子
	// u：当前处理的节点
	// f：当前节点的父节点
	public static void dfs1(int u, int f) {
		fa[u] = f;      // 记录父节点
		dep[u] = dep[f] + 1;  // 计算深度
		siz[u] = 1;     // 初始化子树大小
		// 遍历当前节点的所有邻边
		for (int e = head2[u], v; e > 0; e = next2[e]) {
			v = to2[e];  // 获取邻边指向的节点
			if (v != f) {  // 如果邻节点不是父节点
				dfs1(v, u);  // 递归处理邻节点
				siz[u] += siz[v];  // 更新子树大小
				// 更新重儿子（选择子树最大的节点作为重儿子）
				if (son[u] == 0 || siz[son[u]] < siz[v]) {
					son[u] = v;
				}
			}
		}
	}

	// 深度优先搜索，用于树链剖分的第二遍处理
	// 计算节点所在链的顶端节点
	// u：当前处理的节点
	// t：当前节点所在链的顶端节点
	public static void dfs2(int u, int t) {
		top[u] = t;  // 记录当前节点所在链的顶端节点
		if (son[u] == 0) {  // 如果是叶子节点，返回
			return;
		}
		dfs2(son[u], t);  // 递归处理重儿子，保持链的顶端节点不变
		// 遍历当前节点的所有邻边
		for (int e = head2[u], v; e > 0; e = next2[e]) {
			v = to2[e];  // 获取邻边指向的节点
			// 如果邻节点不是父节点且不是重儿子
			if (v != fa[u] && v != son[u]) {
				dfs2(v, v);  // 递归处理轻儿子，链的顶端节点为自身
			}
		}
	}

	// 判断节点 c 是否在 a 到 b 的路径上
	// a：路径的起点
	// b：路径的终点
	// c：需要判断的节点
	// 返回值：true 表示 c 在 a 到 b 的路径上，false 表示不在
	public static boolean mustPass(int a, int b, int c) {
		// 当 a 和 b 不在同一条链上时
		while (top[a] != top[b]) {
			// 确保 a 所在链的顶端节点深度大于等于 b 所在链的顶端节点深度
			if (dep[top[a]] < dep[top[b]]) {
				int tmp = a;
				a = b;
				b = tmp;
			}
			// 判断 c 是否在 a 到 top[a] 的路径上
			if (top[c] == top[a] && dep[c] <= dep[a]) {
				return true;
			}
			a = fa[top[a]];  // 将 a 跳转到所在链的顶端节点的父节点
		}
		// 当 a 和 b 在同一条链上时
		if (dep[a] < dep[b]) {  // 确保 a 的深度大于等于 b 的深度
			int tmp = a;
			a = b;
			b = tmp;
		}
		// 判断 c 是否在 b 到 a 的路径上
		return top[a] == top[c] && dep[b] <= dep[c] && dep[c] <= dep[a];
	}

	// 主函数，程序入口
	public static void main(String[] args) throws Exception {
		// 初始化快速读取器和输出器
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读取节点数和边数
		n = in.nextInt();
		m = in.nextInt();
		cntn = n;  // 圆方树初始节点数为原图节点数
		cnt1 = 1;  // 边计数器初始化为 1，避免异或 0 出错
		// 读取 m 条边并添加到原图
		for (int i = 1, u, v; i <= m; i++) {
			u = in.nextInt();
			v = in.nextInt();
			addEdge1(u, v);  // 添加 u 到 v 的边
			addEdge1(v, u);  // 添加 v 到 u 的边（无向图）
		}
		tarjan(1, 0);  // 使用 Tarjan 算法建立圆方树
		dfs1(1, 0);   // 树链剖分第一遍处理
		dfs2(1, 1);   // 树链剖分第二遍处理
		q = in.nextInt();  // 读取查询数
		// 处理 q 条查询
		for (int i = 1, op, a, b, c, d; i <= q; i++) {
			op = in.nextInt();  // 读取查询类型
			if (op == 1) {  // 类型 1：删除边 c-d，判断 a 和 b 是否连通
				a = in.nextInt();
				b = in.nextInt();
				c = in.nextInt();
				d = in.nextInt();
				int cut = getCut(c, d);  // 获取边 c-d 对应的方点
				if (cut == 0) {  // 如果边 c-d 不是桥边
					out.println("yes");  // 删除后 a 和 b 仍然连通
				} else {  // 如果边 c-d 是桥边
					// 判断 cut 是否在 a 到 b 的路径上
					out.println(mustPass(a, b, cut) ? "no" : "yes");
				}
			} else {  // 类型 2：删除点 c，判断 a 和 b 是否连通
				a = in.nextInt();
				b = in.nextInt();
				c = in.nextInt();
				// 判断 c 是否在 a 到 b 的路径上
				out.println(mustPass(a, b, c) ? "no" : "yes");
			}
		}
		// 刷新输出缓冲区
		out.flush();
		// 关闭输出器
		out.close();
	}

	// 快速读取工具类，用于高效读取输入数据
	// 解决 Scanner 在大数据量下读取速度慢的问题
	static class FastReader {
		// 缓冲区大小为 1<<16 = 65536
		private final byte[] buffer = new byte[1 << 16];
		// ptr：缓冲区当前读取位置，len：缓冲区中已读取的字节数
		private int ptr = 0, len = 0;
		// 输入流
		private final InputStream in;

		// 构造函数，初始化输入流
		FastReader(InputStream in) {
			this.in = in;
		}

		// 读取一个字节
		// 返回值：读取到的字节，-1 表示到达输入末尾
		private int readByte() throws IOException {
			if (ptr >= len) {  // 如果缓冲区已读完
				len = in.read(buffer);  // 从输入流读取新的数据到缓冲区
				ptr = 0;  // 重置读取位置
				if (len <= 0)  // 如果读取到末尾
					return -1;
			}
			return buffer[ptr++];  // 返回当前字节并移动指针
		}

		// 读取一个整数
		// 返回值：读取到的整数
		int nextInt() throws IOException {
			int c;
			// 跳过空白字符（空格、换行等）
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			// 判断是否为负数
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();  // 跳过负号
			}
			// 读取数字部分
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');  // 累加数字
				c = readByte();  // 读取下一个字符
			}
			// 根据正负号返回结果
			return neg ? -val : val;
		}
	}

}
