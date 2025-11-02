package class122;

/**
 * 运输计划，java迭代版
 * 
 * 题目来源：洛谷 P2680 运输计划
 * 题目链接：https://www.luogu.com.cn/problem/P2680
 * 
 * 算法原理：
 * 这是一道经典的树上问题，结合了以下几种关键技术：
 * 1. 二分答案：答案具有单调性，可以通过二分法寻找最优解
 * 2. 树上边差分：用于统计每条边被超过限制的路径覆盖的次数
 * 3. Tarjan离线LCA算法：用于快速计算树上任意两点间的最近公共祖先
 * 4. DFS遍历：用于检查是否存在一条边满足删除条件
 * 
 * 解题思路：
 * 1. 二分答案：二分最大运输代价，检查能否通过删除一条边达到该代价
 * 2. 对于每个二分的值limit，找出所有超过limit的运输计划
 * 3. 使用树上边差分技术统计每条边被这些超限路径覆盖的次数
 * 4. 通过DFS遍历检查是否存在一条边，满足：
 *    a. 被所有超限路径覆盖（即覆盖次数等于超限路径数）
 *    b. 边权不小于需要减少的值（maxCost - limit）
 * 5. 如果存在这样的边，则当前limit可行，尝试更小的值；否则尝试更大的值
 * 
 * 树上边差分原理：
 * 对于从u到v的路径，我们执行以下操作：
 * num[u]++; num[v]++; num[lca] -= 2;
 * 然后通过DFS累加子树和，得到每条边的真实覆盖次数
 * 注意：这里统计的是点的标记，实际应用中需要转换为边的覆盖次数
 * 
 * 时间复杂度分析：
 * 1. 二分答案：O(log(maxCost))
 * 2. Tarjan离线LCA：O(N + M)
 * 3. 树上边差分标记：O(M)
 * 4. DFS检查：O(N)
 * 总体时间复杂度：O((N + M) * log(maxCost))
 * 
 * 空间复杂度分析：
 * 1. 图存储：O(N + M)
 * 2. Tarjan相关数组：O(N + M)
 * 3. 差分数组：O(N)
 * 总体空间复杂度：O(N + M)
 * 
 * 工程化考量：
 * 1. 由于Java的运行效率相对较低，有时会有一个测试用例超时
 * 2. 使用迭代版实现避免递归深度过大导致的栈溢出
 * 3. 使用快速IO提高输入效率
 * 4. 使用链式前向星存储树结构，提高遍历效率
 * 5. Tarjan算法中使用并查集优化，保证查询效率
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code05_TransportPlan2 {

	// 常量定义
	public static int MAXN = 300001; // 最大节点数
	public static int MAXM = 300001; // 最大查询数

	// 全局变量
	public static int n; // 节点数
	public static int m; // 运输计划数

	// 节点访问次数数组，用于树上边差分
	public static int[] num = new int[MAXN];

	// 链式前向星存储树的边
	public static int[] headEdge = new int[MAXN];     // 边的头指针数组
	public static int[] edgeNext = new int[MAXN << 1]; // 边的下一个指针数组
	public static int[] edgeTo = new int[MAXN << 1];   // 边指向的节点数组
	public static int[] edgeWeight = new int[MAXN << 1]; // 边权数组

	// 边计数器
	public static int tcnt;

	// 链式前向星存储查询
	public static int[] headQuery = new int[MAXN];      // 查询的头指针数组
	public static int[] queryNext = new int[MAXM << 1]; // 查询的下一个指针数组
	public static int[] queryTo = new int[MAXM << 1];   // 查询指向的节点数组
	public static int[] queryIndex = new int[MAXM << 1]; // 查询索引数组

	// 查询计数器
	public static int qcnt;

	// 访问标记数组，用于Tarjan算法中标记节点是否已访问
	public static boolean[] visited = new boolean[MAXN];

	// 并查集数组，用于Tarjan算法中
	public static int[] unionfind = new int[MAXN];

	// 运输计划起点和终点数组
	public static int[] quesu = new int[MAXM]; // 运输计划起点
	public static int[] quesv = new int[MAXM]; // 运输计划终点

	// 距离数组，distance[i]表示节点i到根节点的距离
	public static int[] distance = new int[MAXN];

	// LCA数组，lca[i]表示第i个运输计划的最近公共祖先
	public static int[] lca = new int[MAXM];

	// 运输计划代价数组，cost[i]表示第i个运输计划的代价
	public static int[] cost = new int[MAXM];

	// 最大运输代价
	public static int maxCost;

	/**
	 * 初始化函数：初始化所有数据结构
	 */
	public static void build() {
		tcnt = qcnt = 1;
		Arrays.fill(headEdge, 1, n + 1, 0);
		Arrays.fill(headQuery, 1, n + 1, 0);
		Arrays.fill(visited, 1, n + 1, false);
		for (int i = 1; i <= n; i++) {
			unionfind[i] = i;
		}
		maxCost = 0;
	}

	/**
	 * 添加边：向链式前向星中添加一条边
	 * 
	 * @param u 起始节点
	 * @param v 终止节点
	 * @param w 边权
	 */
	public static void addEdge(int u, int v, int w) {
		edgeNext[tcnt] = headEdge[u];
		edgeTo[tcnt] = v;
		edgeWeight[tcnt] = w;
		headEdge[u] = tcnt++;
	}

	/**
	 * 添加查询：向链式前向星中添加一个查询
	 * 
	 * @param u 查询起始节点
	 * @param v 查询终止节点
	 * @param i 查询索引
	 */
	public static void addQuery(int u, int v, int i) {
		queryNext[qcnt] = headQuery[u];
		queryTo[qcnt] = v;
		queryIndex[qcnt] = i;
		headQuery[u] = qcnt++;
	}

	// find方法的递归版改迭代版
	public static int[] stack = new int[MAXN]; // 用于迭代版find的栈

	/**
	 * 并查集查找函数：迭代版实现，带路径压缩
	 * 
	 * @param i 要查找的节点
	 * @return 节点i所在集合的代表元素
	 */
	public static int find(int i) {
		int size = 0;
		while (i != unionfind[i]) {
			stack[size++] = i;
			i = unionfind[i];
		}
		while (size > 0) {
			unionfind[stack[--size]] = i;
		}
		return i;
	}

	// tarjan方法的递归版改迭代版
	// 不会改，看讲解118，讲了怎么从递归版改成迭代版
	public static int[][] ufwe = new int[MAXN][4]; // 用于迭代版tarjan的栈元素

	public static int stackSize, u, f, w, e; // 迭代版tarjan的栈相关变量

	/**
	 * 向栈中压入元素
	 * 
	 * @param u 当前节点
	 * @param f 父节点
	 * @param w 边权
	 * @param e 边索引
	 */
	public static void push(int u, int f, int w, int e) {
		ufwe[stackSize][0] = u;
		ufwe[stackSize][1] = f;
		ufwe[stackSize][2] = w;
		ufwe[stackSize][3] = e;
		stackSize++;
	}

	/**
	 * 从栈中弹出元素
	 */
	public static void pop() {
		--stackSize;
		u = ufwe[stackSize][0];
		f = ufwe[stackSize][1];
		w = ufwe[stackSize][2];
		e = ufwe[stackSize][3];
	}

	/**
	 * Tarjan离线LCA算法：迭代版实现
	 * 
	 * 算法原理：
	 * 1. 使用显式栈模拟递归过程
	 * 2. 当e == -1时，表示第一次访问节点u，进行初始化操作
	 * 3. 当e != 0时，表示正在遍历u的邻接边，处理下一条边
	 * 4. 当e == 0时，表示已处理完u的所有邻接边，进行收尾操作
	 * 
	 * @param root 根节点
	 */
	public static void tarjan(int root) {
		stackSize = 0;
		push(root, 0, 0, -1);
		while (stackSize > 0) {
			pop();
			if (e == -1) {
				visited[u] = true;
				distance[u] = distance[f] + w;
				e = headEdge[u];
			} else {
				e = edgeNext[e];
			}
			if (e != 0) {
				push(u, f, w, e);
				if (edgeTo[e] != f) {
					push(edgeTo[e], u, edgeWeight[e], -1);
				}
			} else {
				for (int q = headQuery[u], v, i; q != 0; q = queryNext[q]) {
					v = queryTo[q];
					if (visited[v]) {
						i = queryIndex[q];
						lca[i] = find(v);
						cost[i] = distance[u] + distance[v] - 2 * distance[lca[i]];
						maxCost = Math.max(maxCost, cost[i]);
					}
				}
				unionfind[u] = f;
			}
		}
	}

	/**
	 * 检查函数：检查给定的limit是否可行
	 * 
	 * @param limit 当前二分的运输代价上限
	 * @return 如果可行返回true，否则返回false
	 */
	public static boolean f(int limit) {
		atLeast = maxCost - limit;
		Arrays.fill(num, 1, n + 1, 0);
		beyond = 0;
		for (int i = 1; i <= m; i++) {
			if (cost[i] > limit) {
				num[quesu[i]]++;
				num[quesv[i]]++;
				num[lca[i]] -= 2;
				beyond++;
			}
		}
		return beyond == 0 || dfs(1);
	}

	// 至少要减少多少边权
	public static int atLeast;

	// 超过要求的运输计划有几个
	public static int beyond;

	/**
	 * DFS遍历函数：迭代版实现，检查是否存在满足条件的边
	 * 
	 * 算法原理：
	 * 1. 使用显式栈模拟递归过程
	 * 2. 遍历过程中累加子树信息
	 * 3. 检查当前边是否满足条件：
	 *    a. 被所有超限路径覆盖（num[u] == beyond）
	 *    b. 边权不小于需要减少的值（w >= atLeast）
	 * 
	 * @param root 根节点
	 * @return 如果存在满足条件的边返回true，否则返回false
	 */
	// dfs方法的递归版改迭代版
	// 不会改，看讲解118，讲了怎么从递归版改成迭代版
	public static boolean dfs(int root) {
		stackSize = 0;
		push(root, 0, 0, -1);
		while (stackSize > 0) {
			pop();
			if (e == -1) {
				e = headEdge[u];
			} else {
				e = edgeNext[e];
			}
			if (e != 0) {
				push(u, f, w, e);
				if (edgeTo[e] != f) {
					push(edgeTo[e], u, edgeWeight[e], -1);
				}
			} else {
				for (int e = headEdge[u], v; e != 0; e = edgeNext[e]) {
					v = edgeTo[e];
					if (v != f) {
						num[u] += num[v];
					}
				}
				if (num[u] == beyond && w >= atLeast) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 计算函数：通过二分答案计算最小运输代价
	 * 
	 * @return 最小运输代价
	 */
	public static int compute() {
		tarjan(1);
		int l = 0, r = maxCost, mid;
		int ans = 0;
		while (l <= r) {
			mid = (l + r) / 2;
			if (f(mid)) {
				ans = mid;
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		return ans;
	}

	/**
	 * 主函数：程序入口
	 */
	public static void main(String[] args) throws IOException {
		FastIO in = new FastIO();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out), false);
		n = in.nextInt();
		build();
		m = in.nextInt();
		for (int i = 1, u, v, w; i < n; i++) {
			u = in.nextInt();
			v = in.nextInt();
			w = in.nextInt();
			addEdge(u, v, w);
			addEdge(v, u, w);
		}
		for (int i = 1, u, v; i <= m; i++) {
			u = in.nextInt();
			v = in.nextInt();
			quesu[i] = u;
			quesv[i] = v;
			addQuery(u, v, i);
			addQuery(v, u, i);
		}
		out.println(compute());
		out.flush();
		out.close();
	}

	/**
	 * IO工具类：用于快速输入输出
	 */
	static class FastIO {
		private final int SIZE = 1 << 20; // 缓冲区大小
		private byte[] buf;               // 输入缓冲区
		private int pos;                  // 当前读取位置
		private int count;                // 缓冲区中有效字节数
		private InputStream is;           // 输入流

		/**
		 * 构造函数：初始化IO工具类
		 */
		public FastIO() {
			buf = new byte[SIZE];
			pos = 0;
			count = 0;
			is = System.in;
		}

		/**
		 * 读取一个字节
		 * 
		 * @return 读取的字节，如果到达文件末尾返回-1
		 * @throws IOException IO异常
		 */
		private int readByte() throws IOException {
			if (count == -1) {
				return -1;
			}
			if (pos >= count) {
				pos = 0;
				count = is.read(buf);
				if (count == -1) {
					return -1;
				}
			}
			return buf[pos++] & 0xff;
		}

		/**
		 * 读取一个整数
		 * 
		 * @return 读取的整数，如果到达文件末尾返回-1
		 * @throws IOException IO异常
		 */
		public int nextInt() throws IOException {
			int c, value = 0;
			boolean neg = false;
			do {
				c = readByte();
				if (c == -1) {
					return -1;
				}
			} while (c <= ' ');
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			for (; c >= '0' && c <= '9'; c = readByte()) {
				value = value * 10 + (c - '0');
			}
			return neg ? -value : value;
		}
	}

}