package class194;

// 国土规划问题 Java 版
// 问题描述：给定一张无向图，包含 n 个点和 m 条边，所有点保证连通，图中可能有重边，但无自环
// 初始时所有点的状态都是 0，支持 q 次操作，每次操作只有一个参数 x，含义如下：
// 点 x 的状态翻转（0 变 1，1 变 0），然后计算无所谓点的数量
// 无所谓点定义：如果某个状态为 0 的点删除后，所有状态为 1 的点依然连通，这样的点叫无所谓点
// 数据范围：2 <= n <= 10^5，1 <= m、q <= 2 * 10^5
// 测试链接：https://www.luogu.com.cn/problem/P10517
// 提交说明：提交时请将类名改为 "Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.TreeSet;

public class Code10_LandPlanning1 {

	// 最大节点数，根据题目数据范围设置为 100001
	public static int MAXN = 100001;
	// 最大边数，根据题目数据范围设置为 200001
	public static int MAXM = 200001;
	// 倍增数组的最大幂次，用于 LCA 计算
	public static int MAXP = 20;
	// n：节点数，m：边数，q：操作数，cntn：圆方树的总节点数（初始为原图节点数）
	// arr[u]：节点 u 的状态（true 表示状态为 1，false 表示状态为 0）
	public static int n, m, q, cntn;
	public static boolean[] arr = new boolean[MAXN];

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

	// 圆方树相关变量
	// dist[u]：圆方树中从根节点到节点 u 的圆点数量
	// dep[u]：圆方树中节点 u 的深度
	// stjump[u][p]：圆方树中节点 u 的 2^p 级祖先
	// nid[u]：圆方树节点 u 的 dfn 序号
	// seg[i]：圆方树中 dfn 序号为 i 的节点编号
	// cnti：圆方树 dfn 序号计数器，初始为 0
	public static int[] dist = new int[MAXN << 1];
	public static int[] dep = new int[MAXN << 1];
	public static int[][] stjump = new int[MAXN << 1][MAXP];
	public static int[] nid = new int[MAXN << 1];
	public static int[] seg = new int[MAXN << 1];
	public static int cnti;

	// 存储状态为 1 的节点的 dfn 序号（按升序排列）
	// sumDist：状态为 1 的节点之间的圆点数量之和
	public static TreeSet<Integer> set = new TreeSet<>();
	public static int sumDist;

	// 迭代版 DFS/Tarjan 算法需要的栈
	// 栈中每个元素包含四个值：u（当前节点）、status（状态）、fa（父节点）、e（当前边索引）
	// stacksize：栈顶指针，初始为 0
	public static int[][] stack = new int[MAXN << 1][4];
	public static int u, status, fa, e;
	public static int stacksize;

	// 将元素压入迭代栈
	// u：当前节点
	// status：状态（-1 表示首次访问，0 表示处理子节点返回，1 表示处理回边）
	// fa：父节点
	// e：当前边索引
	public static void push(int u, int status, int fa, int e) {
		stack[stacksize][0] = u;
		stack[stacksize][1] = status;
		stack[stacksize][2] = fa;
		stack[stacksize][3] = e;
		stacksize++;
	}

	// 从迭代栈中弹出元素
	public static void pop() {
		stacksize--;
		u = stack[stacksize][0];
		status = stack[stacksize][1];
		fa = stack[stacksize][2];
		e = stack[stacksize][3];
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

	// Tarjan 算法递归版，用于建立圆方树
	// u：当前处理的节点
	public static void tarjan1(int u) {
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

	// Tarjan 算法迭代版，用于建立圆方树
	// node：起始节点（通常为 1）
	public static void tarjan2(int node) {
		stacksize = 0;
		push(node, -1, 0, -1);  // 将起始节点压入栈
		int v;
		while (stacksize > 0) {
			pop();  // 弹出栈顶元素
			if (status == -1) {  // 首次访问当前节点
				dfn[u] = low[u] = ++cntd;  // 初始化时间戳和 low 值
				sta[++top] = u;             // 将当前节点压入栈
				e = head1[u];                // 获取当前节点的第一条边
			} else {  // 处理子节点返回或回边
				v = to1[e];  // 获取当前边指向的节点
				if (status == 0) {  // 处理子节点返回
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
				} else {  // 处理回边
					// 更新当前节点的 low 值
					low[u] = Math.min(low[u], dfn[v]);
				}
				e = next1[e];  // 移动到下一条边
			}
			if (e != 0) {  // 如果还有未处理的边
				v = to1[e];  // 获取当前边指向的节点
				if (dfn[v] == 0) {  // 如果邻节点未被访问过
					push(u, 0, 0, e);  // 将当前节点压入栈，状态为 0
					push(v, -1, 0, -1);  // 将邻节点压入栈，状态为 -1
				} else {  // 如果邻节点已被访问过（回边）
					push(u, 1, 0, e);  // 将当前节点压入栈，状态为 1
				}
			}
		}
	}

	// 深度优先搜索递归版，用于初始化圆方树的 LCA 相关信息
	// u：当前处理的节点
	// fa：当前节点的父节点
	public static void dfs1(int u, int fa) {
		nid[u] = ++cnti;  // 为当前节点分配 dfn 序号
		seg[cnti] = u;     // 记录 dfn 序号对应的节点编号
		// 计算从根节点到当前节点的圆点数量
		dist[u] = dist[fa] + (u <= n ? 1 : 0);
		dep[u] = dep[fa] + 1;  // 计算当前节点的深度
		stjump[u][0] = fa;  // 初始化当前节点的 2^0 级祖先
		// 初始化倍增数组
		for (int p = 1; p < MAXP; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		// 遍历当前节点的所有邻边
		for (int e = head2[u]; e > 0; e = next2[e]) {
			int v = to2[e];  // 获取邻边指向的节点
			if (v != fa) {  // 如果邻节点不是父节点
				dfs1(v, u);  // 递归处理邻节点
			}
		}
	}

	// 深度优先搜索迭代版，用于初始化圆方树的 LCA 相关信息
	// cur：起始节点（通常为 1）
	// father：起始节点的父节点（通常为 0）
	public static void dfs2(int cur, int father) {
		stacksize = 0;
		push(cur, 0, father, -1);  // 将起始节点压入栈
		while (stacksize > 0) {
			pop();  // 弹出栈顶元素
			if (e == -1) {  // 首次访问当前节点
				nid[u] = ++cnti;  // 为当前节点分配 dfn 序号
				seg[cnti] = u;     // 记录 dfn 序号对应的节点编号
				// 计算从根节点到当前节点的圆点数量
				dist[u] = dist[fa] + (u <= n ? 1 : 0);
				dep[u] = dep[fa] + 1;  // 计算当前节点的深度
				stjump[u][0] = fa;  // 初始化当前节点的 2^0 级祖先
				// 初始化倍增数组
				for (int p = 1; p < MAXP; p++) {
					stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
				}
				e = head2[u];  // 获取当前节点的第一条边
			} else {  // 处理下一条边
				e = next2[e];  // 移动到下一条边
			}
			if (e != 0) {  // 如果还有未处理的边
				push(u, 0, fa, e);  // 将当前节点压入栈
				if (to2[e] != fa) {  // 如果邻节点不是父节点
					push(to2[e], 0, u, -1);  // 将邻节点压入栈
				}
			}
		}
	}

	// 计算圆方树中两个节点的最近公共祖先（LCA）
	// a：节点 a
	// b：节点 b
	// 返回值：节点 a 和节点 b 的最近公共祖先
	public static int getLca(int a, int b) {
		// 如果节点 a 的深度小于节点 b 的深度，交换 a 和 b
		if (dep[a] < dep[b]) {
			int tmp = a; a = b; b = tmp;
		}
		// 将节点 a 提升到与节点 b 相同的深度
		for (int p = MAXP - 1; p >= 0; p--) {
			if (dep[stjump[a][p]] >= dep[b]) {
				a = stjump[a][p];
			}
		}
		if (a == b) {  // 如果节点 a 和节点 b 相同，直接返回
			return a;
		}
		// 同时提升节点 a 和节点 b，直到它们的父节点相同
		for (int p = MAXP - 1; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		return stjump[a][0];  // 返回节点 a 的父节点（即 LCA）
	}

	// 计算圆方树中两个节点之间的圆点数量（路径上的节点数）
	// x：节点 x
	// y：节点 y
	// 返回值：节点 x 和节点 y 之间的圆点数量
	public static int getDist(int x, int y) {
		int lca = getLca(x, y);  // 获取节点 x 和节点 y 的最近公共祖先
		// 计算公式：dist[x] + dist[y] - 2 * dist[lca]
		return dist[x] + dist[y] - 2 * dist[lca];
	}

	// 计算当前操作后的无所谓点数量
	// u：当前操作的节点
	// 返回值：无所谓点的数量
	public static int compute(int u) {
		int id = nid[u];  // 获取节点 u 的 dfn 序号
		// 翻转节点 u 的状态
		if (!arr[u]) {
			arr[u] = true;
			set.add(id);  // 将节点 u 的 dfn 序号加入 set
		} else {
			arr[u] = false;
			set.remove(id);  // 将节点 u 的 dfn 序号从 set 中移除
		}
		// 如果 set 中元素数量小于等于 1，sumDist 为 0
		if (set.size() <= 1) {
			sumDist = 0;
		} else {
			// 获取节点 u 的前驱和后继节点
			int low = seg[set.lower(id) != null ? set.lower(id) : set.last()];
			int high = seg[set.higher(id) != null ? set.higher(id) : set.first()];
			// 计算 delta 值
			int delta = getDist(u, low) + getDist(u, high) - getDist(low, high);
			// 更新 sumDist
			if (arr[u]) {
				sumDist += delta;
			} else {
				sumDist -= delta;
			}
		}
		// 如果 set 为空，无所谓点数量为 0
		if (set.isEmpty()) {
			return 0;
		}
		// 计算额外的圆点数量（如果首尾节点的 LCA 是圆点）
		int extra = getLca(seg[set.first()], seg[set.last()]) <= n ? 1 : 0;
		// 计算公式：sumDist / 2 + extra
		return (int) (sumDist / 2 + extra);
	}

	// 主函数，程序入口
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();  // 读取节点数
		m = in.nextInt();  // 读取边数
		q = in.nextInt();  // 读取操作数
		cntn = n;  // 圆方树初始节点数为原图节点数
		// 读取 m 条边并添加到原图
		for (int i = 1, u, v; i <= m; i++) {
			u = in.nextInt();
			v = in.nextInt();
			addEdge1(u, v);  // 添加 u 到 v 的边
			addEdge1(v, u);  // 添加 v 到 u 的边（无向图）
		}
		// tarjan1(1);  // 递归版 Tarjan 算法
		tarjan2(1);  // 迭代版 Tarjan 算法
		// dfs1(1, 0);  // 递归版 DFS
		dfs2(1, 0);  // 迭代版 DFS
		// 处理 q 条操作
		for (int i = 1, x; i <= q; i++) {
			x = in.nextInt();  // 读取当前操作的节点
			int ans = n - compute(x);  // 计算无所谓点的数量
			out.println(ans);  // 输出答案
		}
		out.flush();  // 刷新输出缓冲区
		out.close();  // 关闭输出流
	}

	// 快速输入工具类，用于加速输入
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];  // 输入缓冲区
		private int ptr = 0, len = 0;  // ptr：缓冲区指针，len：缓冲区长度
		private final InputStream in;  // 输入流

		// 构造函数，初始化输入流
		FastReader(InputStream in) {
			this.in = in;
		}

		// 读取一个字节
		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);  // 从输入流中读取数据到缓冲区
				ptr = 0;
				if (len <= 0)
					return -1;  // 如果读取失败，返回 -1
			}
			return buffer[ptr++];  // 返回当前字节并移动指针
		}

		// 读取一个整数
		int nextInt() throws IOException {
			int c;
			// 跳过空白字符
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {  // 如果是负数
				neg = true;
				c = readByte();
			}
			int val = 0;
			// 读取数字字符并转换为整数
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;  // 返回整数（考虑符号）
		}
	}

}
