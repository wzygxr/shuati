package class195;

// 遗产，java版
// 一共n个点，给定起点s，一共q条操作，操作类型如下
// 操作 1 x y w   : 从点x到点y增加有向边，边权是w
// 操作 2 x l r w : 从点x到l~r范围的每个点增加有向边，边权都是w
// 操作 3 x l r w : 从l~r范围的每个点到点x增加有向边，边权都是w
// 所有操作完成后，计算起点s到每个点的最短距离并打印，如果不连通打印-1
// 1 <= n、q <= 10^5
// 1 <= w <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/CF786B
// 测试链接 : https://codeforces.com/problemset/problem/786B
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// ===================== 线段树优化建图核心知识点 =====================
// 【优化建图核心思想】
// 当题目需要建立"单点→区间"或"区间→单点"的边时，直接建图边数为O(n*m)，会超时超内存
// 线段树优化建图通过构建两棵线段树（入树+出树），将边数优化到O(m*logn)
// 
// 【入树（In-Tree）设计原理】
// 功能：实现"区间→单点"的连边，父节点向子节点连0边
// 原理：区间包含子区间，区间约束可通过根节点向下传递到目标单点
// 应用：操作3中"区间到单点"的连边，通过入树只需O(logn)条边
//
// 【出树（Out-Tree）设计原理】
// 功能：实现"单点→区间"的连边，子节点向父节点连0边
// 原理：子区间属于父区间，单点约束可通过子节点向上传递到区间根节点
// 应用：操作2中"单点到区间"的连边，通过出树只需O(logn)条边
//
// 【ML/DL关联价值】
// 1. 大规模图神经网络(GNN)中，用线段树优化邻居采样，将稠密图转化为稀疏图
// 2. 知识图谱中万亿级关系的压缩存储，降低内存占用
// 3. 时序图神经网络中，区间关系的批量处理优化

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.PriorityQueue;

public class Code01_Legacy1 {

	// ===================== 常量定义区 =====================
	// MAXN: 原始节点最大数量，根据题目数据范围1e5设定，笔试面试需根据题目调整
	public static int MAXN = 100001;
	// MAXT: 线段树节点最大数量，约为原始节点的4倍，用于存储入树+出树的内部节点
	// 线段树节点数 = 原始节点数 + 内部节点数，内部节点数不超过3*n，所以取10倍安全
	public static int MAXT = MAXN * 10;
	// MAXE: 最大边数，线段树内部边约2*n，操作边约q*logn，取30倍安全
	public static int MAXE = MAXN * 30;
	// INF: 无穷大值，用于最短路初始化，取1L<<60确保不会溢出且足够大
	// 笔试面试注意：如果边权是int，INF可取0x3f3f3f3f；如果是long，取1L<<60
	public static long INF = 1L << 60;
	
	// ===================== 输入变量区 =====================
	// n: 原始图的节点数量，对应题目给定的点数
	public static int n;
	// q: 操作数量，对应题目给定的操作次数
	public static int q;
	// s: 起点，Dijkstra算法的起始节点
	public static int s;

	// ===================== 链式前向星存图区 =====================
	// 【链式前向星原理】用数组模拟邻接表，适合大规模图，比ArrayList更高效
	// head[u]: 节点u的第一条边的编号，0表示没有边
	public static int[] head = new int[MAXT];
	// nxt[e]: 边e的下一条边的编号，形成链表结构
	public static int[] nxt = new int[MAXE];
	// to[e]: 边e指向的目标节点
	public static int[] to = new int[MAXE];
	// weight[e]: 边e的权值
	public static int[] weight = new int[MAXE];
	// cntg: 边的计数器，从1开始计数（0表示空）
	public static int cntg;

	// ===================== 线段树优化建图核心变量区 =====================
	// 【线段树节点编号规则】
	// 1. 叶子节点（l==r）编号等于原始节点编号（1~n），节省空间
	// 2. 内部节点从n+1开始分配，cntt记录当前最大节点编号
	// 3. 入树和出树共用ls、rs数组，通过不同的根节点区分
	
	// ls[i]: 节点i的左子节点编号，0表示无左子
	public static int[] ls = new int[MAXT];
	// rs[i]: 节点i的右子节点编号，0表示无右子
	public static int[] rs = new int[MAXT];
	// rootOut: 出树的根节点编号，用于"单点→区间"连边
	public static int rootOut;
	// rootIn: 入树的根节点编号，用于"区间→单点"连边
	public static int rootIn;
	// cntt: 当前总节点数（原始节点+线段树新增节点），初始为n
	public static int cntt;

	// ===================== Dijkstra算法变量区 =====================
	// dist[i]: 从起点s到节点i的最短距离，使用long防止溢出
	public static long[] dist = new long[MAXT];
	// vis[i]: 节点i是否已被确定最短距离（Dijkstra的访问标记）
	public static boolean[] vis = new boolean[MAXT];
	// heap: 优先队列，存储[节点编号, 当前距离]，按距离升序排列
	// 使用long[]存储，0号位存节点，1号位存距离
	public static PriorityQueue<long[]> heap = new PriorityQueue<>((x, y) -> Long.compare(x[1], y[1]));

	// ===================== 核心函数：链式前向星加边 =====================
	// 功能：添加一条从u到v、权值为w的有向边
	// 笔试面试注意：链式前向星是标准模板，务必熟练掌握
	// ML/DL关联：高效存图结构可应用于大规模图神经网络的边存储优化
	public static void addEdge(int u, int v, int w) {
		// ++cntg: 边计数器+1，获取新边的编号（从1开始）
		// nxt[cntg] = head[u]: 新边的下一条边指向u原来的第一条边
		nxt[++cntg] = head[u];
		// to[cntg] = v: 新边指向目标节点v
		to[cntg] = v;
		// weight[cntg] = w: 设置新边的权值
		weight[cntg] = w;
		// head[u] = cntg: 更新u的第一条边为新边
		head[u] = cntg;
	}

	// ===================== 核心函数：构建出树（Out-Tree） =====================
	// 【出树功能】实现"单点→区间"的连边，子节点向父节点连0边
	// 【设计原理】子区间属于父区间，单点约束可通过子节点向上传递到区间根节点
	// 【边权为0的含义】线段树内部边仅用于传递连通性，不产生额外代价
	// 参数：l-当前区间左端点，r-当前区间右端点
	// 返回：当前线段树节点的编号
	public static int buildOut(int l, int r) {
		// rt: 当前节点的编号
		int rt;
		if (l == r) {
			// 【叶子节点处理】叶子节点对应原始节点，编号直接等于原始节点编号l
			// 这样设计节省空间，避免为叶子节点分配新编号
			rt = l;
		} else {
			// 【内部节点处理】分配新节点编号
			rt = ++cntt;
			// mid: 区间中点，用于二分分割
			int mid = (l + r) >> 1;
			// 递归构建左子树，区间[l, mid]
			ls[rt] = buildOut(l, mid);
			// 递归构建右子树，区间[mid+1, r]
			rs[rt] = buildOut(mid + 1, r);
			// 【核心边】左子节点→当前节点，边权0（子区间属于父区间）
			addEdge(ls[rt], rt, 0);
			// 【核心边】右子节点→当前节点，边权0
			addEdge(rs[rt], rt, 0);
			// 【原理说明】子节点向父节点连0边，表示从叶子节点（原始点）可以走到代表大区间的父节点
			// 这样"单点→区间"的连边就可以通过：单点→叶子→父节点→...→区间根节点 实现
		}
		// 返回当前节点编号
		return rt;
	}

	// ===================== 核心函数：构建入树（In-Tree） =====================
	// 【入树功能】实现"区间→单点"的连边，父节点向子节点连0边
	// 【设计原理】区间包含子区间，区间约束可通过根节点向下传递到目标单点
	// 参数：l-当前区间左端点，r-当前区间右端点
	// 返回：当前线段树节点的编号
	public static int buildIn(int l, int r) {
		// rt: 当前节点的编号
		int rt;
		if (l == r) {
			// 【叶子节点处理】叶子节点对应原始节点，编号直接等于原始节点编号l
			rt = l;
		} else {
			// 【内部节点处理】分配新节点编号
			rt = ++cntt;
			// mid: 区间中点
			int mid = (l + r) >> 1;
			// 递归构建左子树
			ls[rt] = buildIn(l, mid);
			// 递归构建右子树
			rs[rt] = buildIn(mid + 1, r);
			// 【核心边】当前节点→左子节点，边权0（父区间包含子区间）
			addEdge(rt, ls[rt], 0);
			// 【核心边】当前节点→右子节点，边权0
			addEdge(rt, rs[rt], 0);
			// 【原理说明】父节点向子节点连0边，表示从代表大区间的节点可以走到子区间
			// 这样"区间→单点"的连边就可以通过：区间根节点→...→叶子节点（原始点）实现
		}
		// 返回当前节点编号
		return rt;
	}

	// ===================== 核心函数：单点→区间连边 =====================
	// 【功能】从单点jobx向区间[jobl, jobr]内的所有点连边，边权为jobw
	// 【复杂度】O(logn)条边，替代O(n)条直接连边
	// 【原理】利用出树的结构，单点→出树节点→区间内的所有叶子节点
	// 参数：jobx-源单点，jobl-目标区间左端点，jobr-目标区间右端点，jobw-边权
	//       l-当前线段树节点代表的区间左端点，r-右端点，i-当前线段树节点编号
	public static void xToRange(int jobx, int jobl, int jobr, int jobw, int l, int r, int i) {
		// 【完全覆盖判断】当前节点代表的区间完全包含在目标区间内
		if (jobl <= l && r <= jobr) {
			// 直接从源点向当前线段树节点连边，边权jobw
			// 由于出树的子节点→父节点边权为0，从当前节点可以到达区间内所有叶子节点
			addEdge(jobx, i, jobw);
		} else {
			// 【部分覆盖处理】需要递归到子节点
			// mid: 当前区间中点
			int mid = (l + r) >> 1;
			// 如果目标区间与左子树有交集，递归左子树
			if (jobl <= mid) {
				xToRange(jobx, jobl, jobr, jobw, l, mid, ls[i]);
			}
			// 如果目标区间与右子树有交集，递归右子树
			if (jobr > mid) {
				xToRange(jobx, jobl, jobr, jobw, mid + 1, r, rs[i]);
			}
		}
	}

	// ===================== 核心函数：区间→单点连边 =====================
	// 【功能】从区间[jobl, jobr]内的所有点向单点jobx连边，边权为jobw
	// 【复杂度】O(logn)条边，替代O(n)条直接连边
	// 【原理】利用入树的结构，区间内的所有叶子节点→入树节点→单点
	// 参数：jobl-源区间左端点，jobr-源区间右端点，jobx-目标单点，jobw-边权
	//       l-当前线段树节点代表的区间左端点，r-右端点，i-当前线段树节点编号
	public static void rangeToX(int jobl, int jobr, int jobx, int jobw, int l, int r, int i) {
		// 【完全覆盖判断】当前节点代表的区间完全包含在源区间内
		if (jobl <= l && r <= jobr) {
			// 直接从当前线段树节点向目标单点连边，边权jobw
			// 由于入树的父节点→子节点边权为0，区间内所有叶子节点可以到达当前节点
			addEdge(i, jobx, jobw);
		} else {
			// 【部分覆盖处理】需要递归到子节点
			// mid: 当前区间中点
			int mid = (l + r) >> 1;
			// 如果源区间与左子树有交集，递归左子树
			if (jobl <= mid) {
				rangeToX(jobl, jobr, jobx, jobw, l, mid, ls[i]);
			}
			// 如果源区间与右子树有交集，递归右子树
			if (jobr > mid) {
				rangeToX(jobl, jobr, jobx, jobw, mid + 1, r, rs[i]);
			}
		}
	}

	// ===================== 核心函数：Dijkstra最短路算法 =====================
	// 【功能】计算从起点s到所有节点的最短距离
	// 【适用条件】边权非负（本题边权w>=1，满足条件）
	// 【复杂度】O((V+E)logV)，V为节点数，E为边数
	// ML/DL关联：最短路算法是图神经网络中消息传递的基础操作
	public static void dijkstra() {
		// 【初始化】所有节点的距离设为无穷大
		// 注意：cntt是优化建图后的总节点数，不是原始节点数n
		for (int i = 1; i <= cntt; i++) {
			dist[i] = INF;
		}
		// 起点距离设为0
		dist[s] = 0;
		// 起点入堆，格式为[节点编号, 距离]
		heap.add(new long[] { s, 0 });
		// 【主循环】堆不为空时继续
		while (!heap.isEmpty()) {
			// 取出距离最小的节点
			long[] cur = heap.poll();
			// u: 当前节点编号
			int u = (int) cur[0];
			// d: 当前距离
			long d = cur[1];
			// 【访问判断】如果该节点已被处理过，跳过
			// 注意：Java的PriorityQueue不支持decrease-key操作，可能有重复元素
			if (!vis[u]) {
				// 标记为已访问
				vis[u] = true;
				// 【松弛操作】遍历u的所有邻接边
				for (int e = head[u]; e > 0; e = nxt[e]) {
					// v: 边的目标节点
					int v = to[e];
					// w: 边的权值
					int w = weight[e];
					// 【松弛条件】如果v未被访问且可以通过u获得更短距离
					if (!vis[v] && dist[v] > d + w) {
						// 更新v的最短距离
						dist[v] = d + w;
						// 将v入堆
						heap.add(new long[] { v, dist[v] });
					}
				}
			}
		}
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		// 【快速读入】使用FastReader提高读入速度，避免Scanner超时
		FastReader in = new FastReader(System.in);
		// 【快速输出】使用PrintWriter提高输出速度
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 【读入n】原始节点数量
		n = in.nextInt();
		// 【读入q】操作数量
		q = in.nextInt();
		// 【读入s】起点
		s = in.nextInt();
		// 【初始化】cntt从n开始，1~n是原始节点编号
		cntt = n;
		// 【构建出树】用于操作2的"单点→区间"连边
		rootOut = buildOut(1, n);
		// 【构建入树】用于操作3的"区间→单点"连边
		rootIn = buildIn(1, n);
		// 【处理q个操作】
		for (int i = 1, op, x, y, l, r, w; i <= q; i++) {
			// 读入操作类型
			op = in.nextInt();
			if (op == 1) {
				// 【操作1】从点x到点y增加有向边，边权是w
				x = in.nextInt();
				y = in.nextInt();
				w = in.nextInt();
				// 直接加边，无需优化
				addEdge(x, y, w);
			} else if (op == 2) {
				// 【操作2】从点x到l~r范围的每个点增加有向边，边权都是w
				// 使用出树优化，将O(n)条边优化为O(logn)条边
				x = in.nextInt();
				l = in.nextInt();
				r = in.nextInt();
				w = in.nextInt();
				// 调用xToRange，利用出树实现"单点→区间"连边
				xToRange(x, l, r, w, 1, n, rootIn);
			} else {
				// 【操作3】从l~r范围的每个点到点x增加有向边，边权都是w
				// 使用入树优化，将O(n)条边优化为O(logn)条边
				x = in.nextInt();
				l = in.nextInt();
				r = in.nextInt();
				w = in.nextInt();
				// 调用rangeToX，利用入树实现"区间→单点"连边
				rangeToX(l, r, x, w, 1, n, rootOut);
			}
		}
		// 【运行Dijkstra】计算从s到所有节点的最短距离
		dijkstra();
		// 【输出结果】只输出原始节点1~n的最短距离
		for (int i = 1; i <= n; i++) {
			// 如果距离为INF，说明不可达，输出-1
			out.print(dist[i] == INF ? -1 : dist[i]);
			out.print(" ");
		}
		// 【刷新并关闭输出流】
		out.flush();
		out.close();
	}

	// ===================== 快速读入工具类 =====================
	// 【功能】提供快速整数读入，比Scanner快约10倍
	// 【原理】使用缓冲区批量读入，减少IO次数
	// 笔试面试建议：竞赛中务必使用快速读入，避免超时
	static class FastReader {
		// buffer: 读入缓冲区，大小为64KB（1<<16）
		private final byte[] buffer = new byte[1 << 16];
		// ptr: 当前读取位置
		private int ptr = 0;
		// len: 缓冲区有效长度
		private int len = 0;
		// in: 输入流
		private final InputStream in;

		// 构造方法
		FastReader(InputStream in) {
			this.in = in;
		}

		// 【读入一个字节】
		private int readByte() throws IOException {
			// 如果当前位置超过有效长度，重新读入缓冲区
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				// 如果读到文件末尾，返回-1
				if (len <= 0)
					return -1;
			}
			// 返回当前字节并移动指针
			return buffer[ptr++];
		}

		// 【读入一个整数】
		int nextInt() throws IOException {
			// c: 当前读取的字符
			int c;
			// 跳过空白字符（空格、换行等）
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			// neg: 是否为负数
			boolean neg = false;
			// 处理负号
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			// val: 最终结果
			int val = 0;
			// 读取数字字符并转换为整数
			while (c > ' ' && c != -1) {
				// 数字字符转数字：'0'的ASCII码是48
				val = val * 10 + (c - '0');
				c = readByte();
			}
			// 返回结果，考虑符号
			return neg ? -val : val;
		}
	}

}
