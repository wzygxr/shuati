package class195;

// 旅程，java版
// 一共n个点，给定起点p，一共m次操作，格式如下
// 操作 a b c d : a~b范围每个点与c~d范围每个点之间，都增加一条无向边
// 所有操作完成后，计算起点p到每个点经过的最少边数，题目保证整体连通
// 1 <= n <= 5 * 10^5
// 1 <= m <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P6348
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// ===================== 区间到区间连边优化建图核心知识点 =====================
// 【问题分析】
// 本题需要建立"区间→区间"的边，即a~b范围内的每个点都要与c~d范围内的每个点连边
// 如果直接建图，边数为O((b-a+1)*(d-c+1))，最坏情况下是O(n^2)，无法接受
//
// 【优化建图核心思想】
// 使用"虚拟节点"技巧，将"区间→区间"的边转化为"区间→虚拟节点→区间"的边
// 具体做法：
// 1. 创建虚拟节点x，从a~b区间向x连边（利用出树）
// 2. 创建虚拟节点y，从y向c~d区间连边（利用入树）
// 3. 从x向y连一条权值为1的边
// 这样只需要O(logn)条边就能表示区间之间的连接
//
// 【为什么用0-1 BFS】
// 本题所有边的权值只有0和1，适合使用0-1 BFS（双端队列BFS）
// 0-1 BFS的时间复杂度为O(V+E)，比Dijkstra的O((V+E)logV)更优
//
// 【ML/DL关联价值】
// 1. 图神经网络中批量处理节点间的消息传递，用虚拟节点减少计算量
// 2. 知识图谱中实体关系的批量建模，降低图卷积的计算复杂度
// 3. 推荐系统中用户-物品二分图的批量边构建优化

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;

public class Code02_Journeys1 {

	// ===================== 常量定义区 =====================
	// MAXN: 原始节点最大数量，根据题目数据范围5e5设定
	public static int MAXN = 500001;
	// MAXT: 线段树节点最大数量，约为原始节点的10倍
	// 需要存储：原始节点 + 入树内部节点 + 出树内部节点 + 虚拟节点
	public static int MAXT = MAXN * 10;
	// MAXE: 最大边数，线段树内部边约4*n，操作边约m*logn，取20倍安全
	public static int MAXE = MAXN * 20;
	// INF: 无穷大值，用于距离初始化
	public static int INF = 1 << 30;

	// ===================== 输入变量区 =====================
	// n: 原始图的节点数量
	public static int n;
	// m: 操作数量
	public static int m;
	// p: 起点
	public static int p;

	// ===================== 链式前向星存图区 =====================
	// head[u]: 节点u的第一条边的编号
	public static int[] head = new int[MAXT];
	// nxt[e]: 边e的下一条边的编号
	public static int[] nxt = new int[MAXE];
	// to[e]: 边e指向的目标节点
	public static int[] to = new int[MAXE];
	// weight[e]: 边e的权值（本题只有0或1）
	public static int[] weight = new int[MAXE];
	// cntg: 边的计数器
	public static int cntg;

	// ===================== 线段树优化建图核心变量区 =====================
	// ls[i]: 节点i的左子节点编号
	public static int[] ls = new int[MAXT];
	// rs[i]: 节点i的右子节点编号
	public static int[] rs = new int[MAXT];
	// rootOut: 出树的根节点编号
	public static int rootOut;
	// rootIn: 入树的根节点编号
	public static int rootIn;
	// cntt: 当前总节点数（原始节点+线段树节点+虚拟节点）
	public static int cntt;

	// ===================== 0-1 BFS变量区 =====================
	// dist[i]: 从起点p到节点i的最少边数
	public static int[] dist = new int[MAXT];
	// deq: 双端队列，用于0-1 BFS
	// 边权为0的边加入队首，边权为1的边加入队尾
	public static ArrayDeque<Integer> deq = new ArrayDeque<>();

	// ===================== 核心函数：链式前向星加边 =====================
	// 功能：添加一条从u到v、权值为w的有向边
	public static void addEdge(int u, int v, int w) {
		// 边计数器+1
		nxt[++cntg] = head[u];
		// 设置边的目标节点
		to[cntg] = v;
		// 设置边的权值
		weight[cntg] = w;
		// 更新头指针
		head[u] = cntg;
	}

	// ===================== 核心函数：构建出树（Out-Tree） =====================
	// 【功能】构建用于"单点→区间"连边的出树
	// 【边方向】子节点→父节点（边权0）
	// 参数：l-当前区间左端点，r-当前区间右端点
	// 返回：当前线段树节点的编号
	public static int buildOut(int l, int r) {
		// rt: 当前节点编号
		int rt;
		if (l == r) {
			// 叶子节点对应原始节点
			rt = l;
		} else {
			// 内部节点分配新编号
			rt = ++cntt;
			// 计算中点
			int mid = (l + r) >> 1;
			// 递归构建左子树
			ls[rt] = buildOut(l, mid);
			// 递归构建右子树
			rs[rt] = buildOut(mid + 1, r);
			// 【核心边】左子节点→当前节点，边权0
			addEdge(ls[rt], rt, 0);
			// 【核心边】右子节点→当前节点，边权0
			addEdge(rs[rt], rt, 0);
		}
		return rt;
	}

	// ===================== 核心函数：构建入树（In-Tree） =====================
	// 【功能】构建用于"区间→单点"连边的入树
	// 【边方向】父节点→子节点（边权0）
	// 参数：l-当前区间左端点，r-当前区间右端点
	// 返回：当前线段树节点的编号
	public static int buildIn(int l, int r) {
		// rt: 当前节点编号
		int rt;
		if (l == r) {
			// 叶子节点对应原始节点
			rt = l;
		} else {
			// 内部节点分配新编号
			rt = ++cntt;
			// 计算中点
			int mid = (l + r) >> 1;
			// 递归构建左子树
			ls[rt] = buildIn(l, mid);
			// 递归构建右子树
			rs[rt] = buildIn(mid + 1, r);
			// 【核心边】当前节点→左子节点，边权0
			addEdge(rt, ls[rt], 0);
			// 【核心边】当前节点→右子节点，边权0
			addEdge(rt, rs[rt], 0);
		}
		return rt;
	}

	// ===================== 核心函数：单点→区间连边 =====================
	// 【功能】从单点jobx向区间[jobl, jobr]内的所有点连边，边权为0
	// 【原理】利用出树结构，单点→出树节点→区间内所有点
	public static void xToRange(int jobx, int jobl, int jobr, int l, int r, int i) {
		// 完全覆盖判断
		if (jobl <= l && r <= jobr) {
			// 向当前线段树节点连边
			addEdge(jobx, i, 0);
		} else {
			// 部分覆盖，递归处理
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				xToRange(jobx, jobl, jobr, l, mid, ls[i]);
			}
			if (jobr > mid) {
				xToRange(jobx, jobl, jobr, mid + 1, r, rs[i]);
			}
		}
	}

	// ===================== 核心函数：区间→单点连边 =====================
	// 【功能】从区间[jobl, jobr]内的所有点向单点jobx连边，边权为0
	// 【原理】利用入树结构，区间内所有点→入树节点→单点
	public static void rangeToX(int jobl, int jobr, int jobx, int l, int r, int i) {
		// 完全覆盖判断
		if (jobl <= l && r <= jobr) {
			// 从当前线段树节点向目标点连边
			addEdge(i, jobx, 0);
		} else {
			// 部分覆盖，递归处理
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				rangeToX(jobl, jobr, jobx, l, mid, ls[i]);
			}
			if (jobr > mid) {
				rangeToX(jobl, jobr, jobx, mid + 1, r, rs[i]);
			}
		}
	}

	// ===================== 核心函数：区间→区间连边 =====================
	// 【功能】建立a~b区间与c~d区间之间的无向边（实际上是两条有向边）
	// 【优化原理】使用两个虚拟节点x和y，将O(n^2)条边优化为O(logn)条边
	// 参数：a,b-第一个区间的左右端点，c,d-第二个区间的左右端点
	public static void rangeToRange(int a, int b, int c, int d) {
		// 【创建虚拟节点x】用于接收来自a~b区间的边
		int x = ++cntt;
		// 【创建虚拟节点y】用于向c~d区间发出边
		int y = ++cntt;
		// 【步骤1】a~b区间→x（利用出树，边权0）
		// 出树结构：区间内的点→出树节点→...→根节点
		// 这里我们反向利用：从区间向虚拟节点连边
		rangeToX(a, b, x, 1, n, rootOut);
		// 【步骤2】y→c~d区间（利用入树，边权0）
		// 入树结构：根节点→...→入树节点→区间内的点
		// 这里我们正向利用：从虚拟节点向区间连边
		xToRange(y, c, d, 1, n, rootIn);
		// 【步骤3】x→y，边权1（这是实际"走一步"的代价）
		// 这条边代表从第一个区间走到第二个区间需要经过一条边
		addEdge(x, y, 1);
	}

	// ===================== 核心函数：0-1 BFS算法 =====================
	// 【功能】计算从起点p到所有节点的最少边数
	// 【适用条件】边权只有0和1的图
	// 【算法原理】使用双端队列，边权为0加队首，边权为1加队尾
	// 【复杂度】O(V+E)，比Dijkstra更高效
	// ML/DL关联：0-1 BFS可用于二值化图神经网络中的最短路径计算
	public static void bfs01() {
		// 【初始化】所有节点距离设为无穷大
		for (int i = 1; i <= cntt; i++) {
			dist[i] = INF;
		}
		// 起点距离设为0
		dist[p] = 0;
		// 起点加入队首
		deq.addFirst(p);
		// 【主循环】队列不为空时继续
		while (!deq.isEmpty()) {
			// 取出队首元素
			int u = deq.pollFirst();
			// 遍历u的所有邻接边
			for (int e = head[u]; e > 0; e = nxt[e]) {
				// v: 目标节点
				int v = to[e];
				// w: 边权（0或1）
				int w = weight[e];
				// 【松弛条件】如果可以通过u获得更短距离
				if (dist[v] > dist[u] + w) {
					// 更新距离
					dist[v] = dist[u] + w;
					// 【双端队列策略】根据边权决定加入队首还是队尾
					if (w == 0) {
						// 边权为0，加入队首（优先处理）
						deq.addFirst(v);
					} else {
						// 边权为1，加入队尾
						deq.addLast(v);
					}
				}
			}
		}
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		// 快速读入
		FastReader in = new FastReader(System.in);
		// 快速输出
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读入n（节点数）
		n = in.nextInt();
		// 读入m（操作数）
		m = in.nextInt();
		// 读入p（起点）
		p = in.nextInt();
		// 初始化节点计数器，1~n是原始节点
		cntt = n;
		// 构建出树
		rootOut = buildOut(1, n);
		// 构建入树
		rootIn = buildIn(1, n);
		// 处理m个操作
		for (int i = 1, a, b, c, d; i <= m; i++) {
			// 读入两个区间
			a = in.nextInt();
			b = in.nextInt();
			c = in.nextInt();
			d = in.nextInt();
			// 【建立双向边】因为是无向边，需要建立两个方向的连接
			// 方向1：a~b → c~d
			rangeToRange(a, b, c, d);
			// 方向2：c~d → a~b
			rangeToRange(c, d, a, b);
		}
		// 运行0-1 BFS
		bfs01();
		// 输出结果（只输出原始节点1~n）
		for (int i = 1; i <= n; i++) {
			out.println(dist[i]);
		}
		// 刷新并关闭输出
		out.flush();
		out.close();
	}

	// ===================== 快速读入工具类 =====================
	static class FastReader {
		// 读入缓冲区
		private final byte[] buffer = new byte[1 << 16];
		// 当前读取位置
		private int ptr = 0;
		// 缓冲区有效长度
		private int len = 0;
		// 输入流
		private final InputStream in;

		// 构造方法
		FastReader(InputStream in) {
			this.in = in;
		}

		// 读入一个字节
		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];
		}

		// 读入一个整数
		int nextInt() throws IOException {
			int c;
			// 跳过空白字符
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			// 处理负号
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			// 读取数字
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}
	}

}
