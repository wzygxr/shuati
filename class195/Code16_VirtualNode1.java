package class195;

// 虚点优化建图基础模板，Java 版
// 本代码展示虚点优化建图的核心模板，用于解决多源多汇最短路问题
// 测试链接 : https://www.luogu.com.cn/problem/P1144（改编）
// 本模板展示了虚点优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 虚点优化建图核心知识点 =====================
// 【问题分析】
// 虚点优化建图用于解决多源多汇、分组连边等问题
// 通过引入虚拟节点，减少边的数量，优化建图复杂度
// 主要应用于多源最短路、分组约束、中间节点等问题
//
// 【核心原理】
// 虚拟源点：从虚拟源点向所有真实源点连 0 边
// 虚拟汇点：从所有真实汇点向虚拟汇点连 0 边
// 分组节点：为每组创建虚拟节点，减少边数
//
// 【复杂度分析】
// 节点数：O(n + k)（k 为虚点数）
// 边数：O(m + n)（虚点优化后的边数）
// 最短路复杂度：O((n+k)log(n+k))
//
// 【ML/DL 关联价值】
// 1. 多任务学习中的共享表示
// 2. 图神经网络中的超级节点
// 3. 分布式计算中的协调节点

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Code16_VirtualNode1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 200001;
	public static int MAXE = 800001;
	public static int INF = 1 << 30;

	// ===================== 图存储区 =====================
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXE];
	public static int[] to = new int[MAXE];
	public static int[] weight = new int[MAXE];
	public static int cnt;

	// ===================== 虚点优化建图变量区 =====================
	public static int n, m;
	public static int virtualSource, virtualSink;

	// ===================== Dijkstra 变量区 =====================
	public static int[] dist = new int[MAXN];
	public static boolean[] visited = new boolean[MAXN];

	// ===================== 核心函数：图加边 =====================
	// 功能：向图中添加一条从 u 到 v、权值为 w 的有向边
	// 笔试面试考察点：链式前向星的插入操作
	// 面试高频提问：为什么使用链式前向星？相比邻接表有什么优势？
	public static void addEdge(int u, int v, int w) {
		next[++cnt] = head[u];
		to[cnt] = v;
		weight[cnt] = w;
		head[u] = cnt;
	}

	// ===================== 核心函数：构建虚拟源点 =====================
	// 功能：从虚拟源点向所有真实源点连 0 边
	// 核心思想：将多源问题转化为单源问题
	// 面试高频提问：为什么虚拟源点的边权为 0？如何处理多个起点？
	public static void buildVirtualSource(int[] sources) {
		for (int source : sources) {
			// 虚拟源点向每个真实源点连 0 边
			addEdge(virtualSource, source, 0);
		}
	}

	// ===================== 核心函数：构建虚拟汇点 =====================
	// 功能：从所有真实汇点向虚拟汇点连 0 边
	// 核心思想：将多汇问题转化为单汇问题
	// 面试高频提问：为什么汇点向虚拟汇点连边？方向如何确定？
	public static void buildVirtualSink(int[] sinks) {
		for (int sink : sinks) {
			// 每个真实汇点向虚拟汇点连 0 边
			addEdge(sink, virtualSink, 0);
		}
	}

	// ===================== 核心函数：构建分组虚点 =====================
	// 功能：为一组节点创建虚拟节点，减少边数
	// 核心思想：组内节点先连到虚点，虚点再连到目标
	// 面试高频提问：分组虚点如何减少边数？时间复杂度优化多少？
	public static void buildGroupNode(int groupId, int[] nodes, boolean isOut) {
		if (isOut) {
			// 出组：组内节点向虚点连边
			for (int node : nodes) {
				addEdge(node, groupId, 0);
			}
		} else {
			// 入组：虚点向组内节点连边
			for (int node : nodes) {
				addEdge(groupId, node, 0);
			}
		}
	}

	// ===================== 核心函数：Dijkstra 求最短路 =====================
	// 功能：在构建的图上运行 Dijkstra 算法，求从源点到所有节点的最短距离
	// 核心思想：贪心策略，每次选择距离最小的未访问节点
	// 面试高频提问：Dijkstra 为什么不能处理负权边？如何扩展？
	public static void dijkstra(int start) {
		// 初始化距离数组
		for (int i = 1; i <= virtualSink; i++) {
			dist[i] = INF;
			visited[i] = false;
		}
		dist[start] = 0;

		// 优先队列（小根堆）
		List<int[]> heap = new ArrayList<>();
		heap.add(new int[]{0, start});

		while (!heap.isEmpty()) {
			// 取出当前距离最小的节点
			int[] cur = heap.remove(heap.size() - 1);
			int u = cur[1];
			int d = cur[0];

			// 如果已访问，跳过
			if (visited[u]) {
				continue;
			}
			visited[u] = true;

			// 遍历所有邻接边
			for (int e = head[u]; e > 0; e = next[e]) {
				int v = to[e];
				int w = weight[e];
				// 松弛操作
				if (!visited[v] && dist[v] > d + w) {
					dist[v] = d + w;
					heap.add(new int[]{dist[v], v});
				}
			}
		}
	}

	// ===================== 核心函数：多源多汇最短路 =====================
	// 功能：求从任意源点到任意汇点的最短距离
	// 核心思想：通过虚拟源点和虚拟汇点转化为单源单汇问题
	// 面试高频提问：多源多汇问题还有哪些解法？复杂度如何？
	public static int multiSourceMultiSink(int[] sources, int[] sinks) {
		// 构建虚拟源点和汇点
		buildVirtualSource(sources);
		buildVirtualSink(sinks);

		// 从虚拟源点运行 Dijkstra
		dijkstra(virtualSource);

		// 返回到虚拟汇点的距离
		return dist[virtualSink] == INF ? -1 : dist[virtualSink];
	}

	// ===================== 快速读入类 =====================
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
				if (len <= 0) {
					return -1;
				}
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

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		// 读入节点数和边数
		n = in.nextInt();
		m = in.nextInt();

		// 设置虚拟源点和汇点编号
		virtualSource = n + 1;
		virtualSink = n + 2;

		// 读入 m 条边
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();
			addEdge(u, v, w);
		}

		// 读入源点集合
		int sourceCount = in.nextInt();
		int[] sources = new int[sourceCount];
		for (int i = 0; i < sourceCount; i++) {
			sources[i] = in.nextInt();
		}

		// 读入汇点集合
		int sinkCount = in.nextInt();
		int[] sinks = new int[sinkCount];
		for (int i = 0; i < sinkCount; i++) {
			sinks[i] = in.nextInt();
		}

		// 求多源多汇最短路
		int result = multiSourceMultiSink(sources, sinks);
		out.println("多源多汇最短路：" + (result == -1 ? "不可达" : result));

		out.flush();
		out.close();
	}
}
