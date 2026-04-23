package class195;

// 沙漠，java版
// 一共n个数字，所有数字都在 1 ~ 10^9 的范围，这是范围说明
// 接下来给定s条设置说明，格式 x v ，表示第x个数的值确定是v
// 接下来给定m条关系说明，格式 l r k x1 x2 ... xk 含义如下
// 第l到第r个数字，其中有k个数字，分别是第x1、第x2 .. 第xk个数字
// 这k个数字中的每一个，都比剩下的(r - l + 1 - k)个数字要大，严格大于
// 根据上面的说明，找到没有矛盾的，给每个数字赋值的方案，任何一个方案即可
// 如果存在方案打印"TAK"，然后打印每个数字，不存在方案打印"NIE"
// 1 <= n、s <= 10^5
// 1 <= m <= 2 * 10^5
// 所有k的累加和 <= 3 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P3588
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// ===================== 差分约束+线段树优化建图核心知识点 =====================
// 【问题分析】
// 本题需要判断约束系统是否有解，如果有解则给出一组可行解
// 约束类型：某些数字必须严格大于其他数字
//
// 【差分约束建模】
// 将"a > b"转化为"a >= b + 1"，即差分约束系统中的边 b -> a，权值为1
// 本题中的约束：k个数字都严格大于区间[l,r]中其他数字
// 等价于：对于每个xi，xi >= y + 1，其中y是[l,r]中除xi外的任意数字
//
// 【线段树优化】
// 对于每个约束，需要建立O(k * (r-l+1-k))条边，最坏O(n^2)
// 优化方法：引入虚拟节点，将边数优化到O(k * logn)
//
// 【拓扑排序判环】
// 差分约束系统有解 <=> 图中无正权环
// 使用拓扑排序检测环，同时计算最长路得到可行解
//
// 【ML/DL关联价值】
// 1. 约束满足问题(CSP)的图论建模方法
// 2. 神经网络中的约束优化，如物理信息神经网络(PINN)
// 3. 知识图谱中的逻辑推理和一致性检验

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code04_Desert1 {

	// ===================== 常量定义区 =====================
	// MAXN: 原始节点最大数量
	public static int MAXN = 100001;
	// MAXT: 线段树节点最大数量
	public static int MAXT = MAXN * 5;
	// MAXE: 最大边数
	public static int MAXE = MAXN * 20;
	// LIMIT: 数字的最大值上限
	public static int LIMIT = 1000000000;

	// ===================== 输入变量区 =====================
	// n: 数字个数
	public static int n;
	// s: 确定值的数量
	public static int s;
	// m: 关系说明数量
	public static int m;

	// ===================== 链式前向星存图区 =====================
	// head[u]: 节点u的第一条边的编号
	public static int[] head = new int[MAXT];
	// nxt[e]: 边e的下一条边的编号
	public static int[] nxt = new int[MAXE];
	// to[e]: 边e指向的目标节点
	public static int[] to = new int[MAXE];
	// weight[e]: 边e的权值（差分约束中的差值）
	public static int[] weight = new int[MAXE];
	// cntg: 边的计数器
	public static int cntg;

	// ===================== 线段树优化建图核心变量区 =====================
	// ls[i]: 节点i的左子节点编号
	public static int[] ls = new int[MAXT];
	// rs[i]: 节点i的右子节点编号
	public static int[] rs = new int[MAXT];
	// root: 线段树根节点
	public static int root;
	// cntt: 当前总节点数
	public static int cntt;

	// ===================== 拓扑排序与差分约束变量区 =====================
	// val[i]: 节点i的确定值（0表示未确定）
	public static int[] val = new int[MAXT];
	// atMost[i]: 节点i的最大可行值（拓扑排序过程中计算）
	public static int[] atMost = new int[MAXT];
	// indegree[i]: 节点i的入度
	public static int[] indegree = new int[MAXT];
	// que: 拓扑排序队列
	public static int[] que = new int[MAXT];

	// ===================== 核心函数：链式前向星加边 =====================
	// 功能：添加一条从u到v、权值为w的有向边，并增加v的入度
	public static void addEdge(int u, int v, int w) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		weight[cntg] = w;
		head[u] = cntg;
		indegree[v]++;  // 目标节点入度+1
	}

	// ===================== 核心函数：构建线段树 =====================
	// 【功能】构建线段树，父节点向子节点连0边
	// 【边方向】父节点→子节点（边权0）
	public static int build(int l, int r) {
		int rt;
		if (l == r) {
			// 叶子节点对应原始节点
			rt = l;
		} else {
			// 内部节点分配新编号
			rt = ++cntt;
			int mid = (l + r) >> 1;
			ls[rt] = build(l, mid);
			rs[rt] = build(mid + 1, r);
			// 父节点向子节点连0边
			addEdge(rt, ls[rt], 0);
			addEdge(rt, rs[rt], 0);
		}
		return rt;
	}

	// ===================== 核心函数：单点→区间连边 =====================
	// 【功能】从单点jobx向区间[jobl, jobr]内的所有点连边，边权为jobw
	public static void xToRange(int jobx, int jobl, int jobr, int jobw, int l, int r, int i) {
		// 区间为空，直接返回
		if (jobl > jobr) {
			return;
		}
		// 完全覆盖判断
		if (jobl <= l && r <= jobr) {
			addEdge(jobx, i, jobw);
		} else {
			// 部分覆盖，递归处理
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				xToRange(jobx, jobl, jobr, jobw, l, mid, ls[i]);
			}
			if (jobr > mid) {
				xToRange(jobx, jobl, jobr, jobw, mid + 1, r, rs[i]);
			}
		}
	}

	// ===================== 核心函数：拓扑排序+最长路 =====================
	// 【功能】判断差分约束系统是否有解，如果有解则计算可行解
	// 【原理】差分约束有解 <=> 图中无正权环
	// 【返回值】true表示有解，false表示无解
	public static boolean topo() {
		// qi: 队列头指针，qsiz: 队列尾指针
		int qi = 1, qsiz = 0;
		// 【步骤1】将所有入度为0的节点入队
		for (int i = 1; i <= cntt; i++) {
			if (indegree[i] == 0) {
				que[++qsiz] = i;
			}
			// 初始化atMost：有确定值用确定值，否则用上限
			atMost[i] = val[i] == 0 ? LIMIT : val[i];
		}
		// 【步骤2】拓扑排序主循环
		while (qi <= qsiz) {
			int u = que[qi++];
			// 遍历u的所有邻接边
			for (int e = head[u]; e > 0; e = nxt[e]) {
				int v = to[e];
				int w = weight[e];
				// 【最长路更新】atMost[v] = max(atMost[v], atMost[u] + w)
				// 在差分约束中，这对应于取约束的并集
				if (atMost[v] > atMost[u] + w) {
					atMost[v] = atMost[u] + w;
					// 【矛盾检测】如果确定值超过上限，或低于1，则无解
					if ((val[v] != 0 && atMost[v] < val[v]) || atMost[v] < 1) {
						return false;
					}
				}
				// 入度减1，如果变为0则入队
				if (--indegree[v] == 0) {
					que[++qsiz] = v;
				}
			}
		}
		// 【步骤3】判断是否有环
		// 如果qsiz == cntt，说明所有节点都被访问，无环
		return qsiz == cntt;
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读入n, s, m
		n = in.nextInt();
		s = in.nextInt();
		m = in.nextInt();
		// 初始化节点计数器
		cntt = n;
		// 构建线段树
		root = build(1, n);
		// 处理s条确定值设置
		for (int i = 1; i <= s; i++) {
			int x = in.nextInt();
			int v = in.nextInt();
			val[x] = v;  // 记录确定值
		}
		// 处理m条关系说明
		for (int i = 1; i <= m; i++) {
			int l = in.nextInt();
			int r = in.nextInt();
			int k = in.nextInt();
			// 创建虚拟节点vnode，代表"这k个数字都大于其他数字"的约束
			int vnode = ++cntt;
			for (int j = 1; j <= k; j++) {
				int x = in.nextInt();
				// 【约束1】x >= vnode，即vnode -> x，权值0
				addEdge(x, vnode, 0);
				// 【约束2】vnode >= [l, x-1]区间内的数字 + 1
				// 即[l, x-1]区间内的数字 -> vnode，权值-1
				xToRange(vnode, l, x - 1, -1, 1, n, root);
				// 更新l，处理下一个区间
				l = x + 1;
			}
			// 【约束3】vnode >= [l, r]区间内的数字 + 1
			xToRange(vnode, l, r, -1, 1, n, root);
		}
		// 运行拓扑排序
		boolean check = topo();
		// 输出结果
		if (check) {
			out.println("TAK");  // 有解
			for (int i = 1; i <= n; i++) {
				out.print(atMost[i] + " ");
			}
			out.println();
		} else {
			out.println("NIE");  // 无解
		}
		out.flush();
		out.close();
	}

	// ===================== 快速读入工具类 =====================
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
