package class194;

// 铁人两项问题 Java 版
// 问题描述：给定一张无向图，包含 n 个节点和 m 条边，图中可能存在多个连通分量
// 查询要求：计算有效的三元组 (a, b, c) 的数量，满足以下条件：
//           1. a、b、c 是不同的节点
//           2. 存在一条从 a 出发，经过 b，最终到达 c 的路径，沿途无重复节点
// 数据范围：1 <= n <= 10^5，1 <= m <= 2 * 10^5
// 测试链接：https://www.luogu.com.cn/problem/P4630
// 提交说明：提交时请将类名改为 "Main"，即可通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code05_Duathlon1 {

	// 最大节点数，根据题目数据范围设置为 100001
	public static int MAXN = 100001;
	// 最大边数，根据题目数据范围设置为 200001
	public static int MAXM = 200001;
	// n：节点数，m：边数，cntn：圆方树的总节点数（初始为原图节点数）
	public static int n, m, cntn;

	// 原图的链式前向星存储结构
	// head1[u]：节点 u 的第一条边的索引
	// next1[e]：边 e 的下一条边的索引
	// to1[e]：边 e 指向的节点
	// cnt1：边的计数器，初始为 0
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
	// top：栈顶指针，初始为 0
	public static int[] sta = new int[MAXN];
	public static int top;

	// 方点的度数组，记录点双连通分量的节点数
	// deg[u]：当 u 是方点时，表示该点双连通分量包含的节点数
	public static int[] deg = new int[MAXN << 1];

	// 圆方树中每个节点的子树中包含的原图节点数量
	public static int[] siz = new int[MAXN << 1];

	// 当前连通分量的总节点数
	public static int total;

	// 答案变量，存储有效的三元组数量
	public static long ans;

	// 迭代版算法需要的栈结构（用于递归改迭代）
	// stack[][]：存储迭代过程中的状态，每个元素包含四个值：u（当前节点）、status（状态）、fa（父节点）、e（边索引）
	// u, status, fa, e：当前迭代的状态变量
	// stacksize：栈的大小，初始为 0
	public static int[][] stack = new int[MAXN << 1][4];
	public static int u, status, fa, e;
	public static int stacksize;

	// 将状态压入迭代栈
	// u：当前节点
	// status：当前处理状态
	// fa：父节点
	// e：当前处理的边索引
	public static void push(int u, int status, int fa, int e) {
		stack[stacksize][0] = u;      // 存储当前节点
		stack[stacksize][1] = status;  // 存储当前处理状态
		stack[stacksize][2] = fa;      // 存储父节点
		stack[stacksize][3] = e;       // 存储当前边索引
		stacksize++;  // 栈大小加 1
	}

	// 从迭代栈弹出状态
	// 弹出后，当前状态变量 u, status, fa, e 会被更新为弹出的值
	public static void pop() {
		stacksize--;  // 栈大小减 1
		u = stack[stacksize][0];      // 恢复当前节点
		status = stack[stacksize][1];  // 恢复当前处理状态
		fa = stack[stacksize][2];      // 恢复父节点
		e = stack[stacksize][3];       // 恢复当前边索引
	}

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

	// 递归版 Tarjan 算法，用于建立圆方树
	// u：当前处理的节点
	public static void tarjan1(int u) {
		total++;  // 当前连通分量的节点数加 1
		dfn[u] = low[u] = ++cntd;  // 初始化时间戳和 low 值
		sta[++top] = u;             // 将当前节点压入栈
		// 遍历当前节点的所有邻边
		for (int e = head1[u]; e > 0; e = next1[e]) {
			int v = to1[e];  // 获取邻边指向的节点
			if (dfn[v] == 0) {  // 如果邻节点未被访问过
				tarjan1(v);  // 递归处理邻节点
				// 更新当前节点的 low 值
				low[u] = Math.min(low[u], low[v]);
				// 如果邻节点的 low 值大于等于当前节点的 dfn 值
				// 说明当前节点是一个割点，需要建立圆方树的方点
				if (low[v] >= dfn[u]) {
					cntn++;  // 圆方树节点数加 1（新增一个方点）
					// 建立方点与当前节点的双向边
					addEdge2(cntn, u);
					addEdge2(u, cntn);
					deg[cntn]++;  // 方点的度加 1（当前节点）
					int pop;
					// 将栈中从 v 到当前节点的所有节点弹出
					// 并建立这些节点与新方点的双向边
					do {
						pop = sta[top--];  // 弹出栈顶节点
						addEdge2(cntn, pop);  // 建立方点与弹出节点的边
						addEdge2(pop, cntn);  // 建立弹出节点与方点的边
						deg[cntn]++;  // 方点的度加 1（弹出节点）
					} while (pop != v);  // 直到弹出节点为 v
				}
			} else {  // 如果邻节点已被访问过
				// 更新当前节点的 low 值
				low[u] = Math.min(low[u], dfn[v]);
			}
		}
	}

	// 迭代版 Tarjan 算法，用于建立圆方树
	// 解决递归版在大数据量下栈溢出的问题
	// node：起始节点
	public static void tarjan2(int node) {
		stacksize = 0;  // 初始化迭代栈大小
		push(node, -1, 0, -1);  // 将起始节点压入栈，状态为 -1（未处理）
		int v;
		// 迭代处理栈中的状态
		while (stacksize > 0) {
			pop();  // 弹出栈顶状态
			if (status == -1) {  // 第一次处理该节点
				total++;  // 当前连通分量的节点数加 1
				dfn[u] = low[u] = ++cntd;  // 初始化时间戳和 low 值
				sta[++top] = u;             // 将当前节点压入 Tarjan 栈
				e = head1[u];  // 获取当前节点的第一条边
			} else {  // 非第一次处理该节点
				v = to1[e];  // 获取当前边指向的节点
				if (status == 0) {  // 处理完子节点后的状态
					// 更新当前节点的 low 值
					low[u] = Math.min(low[u], low[v]);
					// 如果子节点的 low 值大于等于当前节点的 dfn 值
					// 说明当前节点是割点，需要建立圆方树的方点
					if (low[v] >= dfn[u]) {
						cntn++;  // 新增方点
						// 建立方点与当前节点的双向边
						addEdge2(cntn, u);
						addEdge2(u, cntn);
						deg[cntn]++;  // 方点的度加 1（当前节点）
						int pop;
						// 将栈中从 v 到当前节点的所有节点弹出
						// 并建立这些节点与新方点的双向边
						do {
							pop = sta[top--];  // 弹出栈顶节点
							addEdge2(cntn, pop);  // 建立方点与弹出节点的边
							addEdge2(pop, cntn);  // 建立弹出节点与方点的边
							deg[cntn]++;  // 方点的度加 1（弹出节点）
						} while (pop != v);  // 直到弹出节点为 v
					}
				} else {  // 处理回边的状态
					// 更新当前节点的 low 值
					low[u] = Math.min(low[u], dfn[v]);
				}
				e = next1[e];  // 处理下一条边
			}
			if (e != 0) {  // 如果还有未处理的边
				v = to1[e];  // 获取当前边指向的节点
				if (dfn[v] == 0) {  // 如果邻节点未被访问过
					// 将当前节点压入栈，状态为 0（处理子节点前）
					push(u, 0, 0, e);
					// 将邻节点压入栈，状态为 -1（未处理）
					push(v, -1, 0, -1);
				} else {  // 如果邻节点已被访问过
					// 将当前节点压入栈，状态为 1（处理回边）
					push(u, 1, 0, e);
				}
			}
		}
	}

	// 递归版树上动态规划，用于计算有效的三元组数量
	// u：当前处理的节点
	// fa：当前节点的父节点
	public static void dpOnTree1(int u, int fa) {
		// 遍历当前节点的所有邻边
		for (int e = head2[u]; e > 0; e = next2[e]) {
			int v = to2[e];  // 获取邻边指向的节点
			if (v != fa) {  // 如果邻节点不是父节点
				dpOnTree1(v, u);  // 递归处理邻节点
				// 根据当前节点类型（圆点或方点）计算贡献
				if (u <= n) {  // 当前节点是圆点
					// 累加子树贡献的有效三元组数量
					ans += 1L * siz[v] * (total - siz[v] - 1);
				} else {  // 当前节点是方点
					// 累加子树贡献的有效三元组数量（考虑点双连通分量的特性）
					ans += 1L * siz[v] * (deg[u] - 2) * (total - siz[v]);
				}
				siz[u] += siz[v];  // 更新当前节点的子树大小
			}
		}
		// 如果当前节点是原图节点，子树大小加 1（包含自身）
		siz[u] += u <= n ? 1 : 0;
		// 根据当前节点类型（圆点或方点）计算剩余贡献
		if (u <= n) {  // 当前节点是圆点
			// 累加剩余部分贡献的有效三元组数量
			ans += 1L * (siz[u] - 1) * (total - siz[u]);
		} else {  // 当前节点是方点
			// 累加剩余部分贡献的有效三元组数量（考虑点双连通分量的特性）
			ans += 1L * siz[u] * (deg[u] - 2) * (total - siz[u]);
		}
	}

	// 迭代版树上动态规划，用于计算有效的三元组数量
	// 解决递归版在大数据量下栈溢出的问题
	// cur：起始节点
	// father：起始节点的父节点
	public static void dpOnTree2(int cur, int father) {
		stacksize = 0;  // 初始化迭代栈大小
		// 将起始节点压入栈，状态为 0，父节点为 father，边索引为 -1
		push(cur, 0, father, -1);
		// 迭代处理栈中的状态
		while (stacksize > 0) {
			pop();  // 弹出栈顶状态
			if (e == -1) {  // 第一次处理该节点
				e = head2[u];  // 获取当前节点的第一条边
			} else {  // 非第一次处理该节点
				e = next2[e];  // 处理下一条边
			}
			if (e != 0) {  // 如果还有未处理的边
				// 将当前节点压入栈，状态为 0，父节点为 fa，边索引为 e
				push(u, 0, fa, e);
				if (to2[e] != fa) {  // 如果邻节点不是父节点
					// 将邻节点压入栈，状态为 0，父节点为 u，边索引为 -1
					push(to2[e], 0, u, -1);
				}
			} else {  // 如果所有边都已处理
				// 遍历当前节点的所有邻边，更新子树大小和答案
				for (int ei = head2[u]; ei > 0; ei = next2[ei]) {
					int v = to2[ei];  // 获取邻边指向的节点
					if (v != fa) {  // 如果邻节点不是父节点
						// 根据当前节点类型（圆点或方点）计算贡献
						if (u <= n) {  // 当前节点是圆点
							// 累加子树贡献的有效三元组数量
							ans += 1L * siz[v] * (total - siz[v] - 1);
						} else {  // 当前节点是方点
							// 累加子树贡献的有效三元组数量（考虑点双连通分量的特性）
							ans += 1L * siz[v] * (deg[u] - 2) * (total - siz[v]);
						}
						siz[u] += siz[v];  // 更新当前节点的子树大小
					}
				}
				// 如果当前节点是原图节点，子树大小加 1（包含自身）
				siz[u] += u <= n ? 1 : 0;
				// 根据当前节点类型（圆点或方点）计算剩余贡献
				if (u <= n) {  // 当前节点是圆点
					// 累加剩余部分贡献的有效三元组数量
					ans += 1L * (siz[u] - 1) * (total - siz[u]);
				} else {  // 当前节点是方点
					// 累加剩余部分贡献的有效三元组数量（考虑点双连通分量的特性）
					ans += 1L * siz[u] * (deg[u] - 2) * (total - siz[u]);
				}
			}
		}
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
		// 读取 m 条边并添加到原图
		for (int i = 1, u, v; i <= m; i++) {
			u = in.nextInt();
			v = in.nextInt();
			addEdge1(u, v);  // 添加 u 到 v 的边
			addEdge1(v, u);  // 添加 v 到 u 的边（无向图）
		}
		// 处理每个连通分量
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0) {  // 如果节点 i 未被访问过
				total = 0;  // 初始化当前连通分量的总节点数
				// 注释掉递归版 Tarjan，使用迭代版避免栈溢出
				// tarjan1(i);
				tarjan2(i);  // 使用迭代版 Tarjan 建立圆方树
				// 注释掉递归版动态规划，使用迭代版避免栈溢出
				// dpOnTree1(i, 0);
				dpOnTree2(i, 0);  // 使用迭代版动态规划计算答案
			}
		}
		// 输出答案
		out.println(ans);
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
