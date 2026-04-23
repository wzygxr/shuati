package class194;

// 路径问题 Java 版
// 问题描述：给定一张无向图，包含 n 个节点和 m 条边，保证图是连通的，无重边和自环
// 查询要求：给定三个不同的节点 a、b、c
//           判断是否存在一条路径从 a 到达 b，然后从 b 到达 c，要求途中不能出现重复的节点
// 数据范围：1 <= n、m <= 2 * 10^5
// 测试链接：https://www.luogu.com.cn/problem/AT_abc318_g
// 测试链接：https://atcoder.jp/contests/abc318/tasks/abc318_g
// 提交说明：提交时请将类名改为 "Main"，即可通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code03_PathProblem1 {

	// 最大节点数，根据题目数据范围设置为 200001
	public static int MAXN = 200001;
	// 最大边数，根据题目数据范围设置为 200001
	public static int MAXM = 200001;
	// n：节点数，m：边数，a、b、c：查询的三个节点，cntn：圆方树的总节点数（初始为原图节点数）
	public static int n, m, a, b, c, cntn;

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
	// sta[]：Tarjan 算法中使用的栈，存储当前连通分量的节点
	// top：栈顶指针，初始为 0
	public static int[] dfn = new int[MAXN];
	public static int[] low = new int[MAXN];
	public static int cntd;
	public static int[] sta = new int[MAXN];
	public static int top;

	// 圆方树的深度表、父节点表和标记数组
	// dep[u]：圆方树中节点 u 的深度
	// fa[u]：圆方树中节点 u 的父节点
	// flag[u]：标记数组，用于标记与 b 相邻的节点
	public static int[] dep = new int[MAXN << 1];
	public static int[] fa = new int[MAXN << 1];
	public static boolean[] flag = new boolean[MAXN << 1];

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

	// Tarjan 算法，用于建立圆方树
	// u：当前处理的节点
	public static void tarjan(int u) {
		dfn[u] = low[u] = ++cntd;  // 初始化时间戳和 low 值
		sta[++top] = u;             // 将当前节点压入栈
		// 遍历当前节点的所有邻边
		for (int e = head1[u]; e > 0; e = next1[e]) {
			int v = to1[e];  // 获取邻边指向的节点
			if (dfn[v] == 0) {  // 如果邻节点未被访问过
				tarjan(v);  // 递归处理邻节点
				// 更新当前节点的 low 值
				low[u] = Math.min(low[u], low[v]);
				// 如果邻节点的 low 值大于等于当前节点的 dfn 值
				// 说明当前节点是一个割点，需要建立圆方树的方点
				if (low[v] >= dfn[u]) {
					cntn++;  // 圆方树节点数加 1（新增一个方点）
					// 建立方点与当前节点的双向边
					addEdge2(cntn, u);
					addEdge2(u, cntn);
					int pop;
					// 将栈中从 v 到当前节点的所有节点弹出
					// 并建立这些节点与新方点的双向边
					do {
						pop = sta[top--];  // 弹出栈顶节点
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

	// 深度优先搜索，用于建立圆方树的深度表和父节点表
	// u：当前处理的节点
	// f：当前节点的父节点
	public static void dfs(int u, int f) {
		dep[u] = dep[f] + 1;  // 计算当前节点的深度
		fa[u] = f;              // 记录当前节点的父节点
		// 遍历当前节点的所有邻边
		for (int e = head2[u]; e > 0; e = next2[e]) {
			int v = to2[e];  // 获取邻边指向的节点
			if (v != f) {  // 如果邻节点不是父节点
				dfs(v, u);  // 递归处理邻节点
			}
		}
	}

	// 检查是否存在满足条件的路径
	// 返回值：true 表示存在满足条件的路径，false 表示不存在
	public static boolean check() {
		// 标记与 b 相邻的所有节点
		for (int e = head2[b]; e > 0; e = next2[e]) {
			int v = to2[e];  // 获取与 b 相邻的节点
			flag[v] = true;   // 标记该节点
		}
		// 同时将 a 和 c 向上移动，直到它们相遇
		while (a != c) {
			// 确保 a 的深度大于等于 c 的深度
			if (dep[a] < dep[c]) {
				int tmp = a;
				a = c;
				c = tmp;
			}
			a = fa[a];  // 将 a 向上移动到父节点
			// 如果 a 被标记，说明存在满足条件的路径
			if (flag[a]) {
				return true;
			}
		}
		// 如果 a 和 c 相遇但未找到标记节点，说明不存在满足条件的路径
		return false;
	}

	// 主函数，程序入口
	public static void main(String[] args) throws Exception {
		// 初始化快速读取器和输出器
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读取节点数、边数和三个查询节点
		n = in.nextInt();
		m = in.nextInt();
		a = in.nextInt();
		b = in.nextInt();
		c = in.nextInt();
		cntn = n;  // 圆方树初始节点数为原图节点数
		// 读取 m 条边并添加到原图
		for (int i = 1, u, v; i <= m; i++) {
			u = in.nextInt();
			v = in.nextInt();
			addEdge1(u, v);  // 添加 u 到 v 的边
			addEdge1(v, u);  // 添加 v 到 u 的边（无向图）
		}
		tarjan(1);  // 使用 Tarjan 算法建立圆方树
		dfs(1, 0);  // 使用深度优先搜索建立深度表和父节点表
		// 检查是否存在满足条件的路径并输出结果
		out.println(check() ? "Yes" : "No");
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
