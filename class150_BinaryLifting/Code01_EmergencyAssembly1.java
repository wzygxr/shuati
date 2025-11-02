package class119;

// 紧急集合问题
// 问题描述：
// 一共有n个节点，编号1 ~ n，一定有n-1条边连接形成一颗树
// 从一个点到另一个点的路径上有几条边，就需要耗费几个金币
// 每条查询(a, b, c)表示有三个人分别站在a、b、c点上
// 他们想集合在树上的某个点，并且想花费的金币总数最少
// 一共有m条查询，打印m个答案
// 1 <= n <= 5 * 10^5
// 1 <= m <= 5 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P4281
// 如下实现是正确的，但是洛谷平台对空间卡的很严，只有使用C++能全部通过
// C++版本就是本节代码中的Code01_EmergencyAssembly2文件
// C++版本和java版本逻辑完全一样，但只有C++版本可以通过所有测试用例
// 这是洛谷平台没有照顾各种语言的实现所导致的
// 在真正笔试、比赛时，一定是兼顾各种语言的，该实现是一定正确的
// 提交以下的code，提交时请把类名改成"Main"

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

/**
 * 紧急集合问题解决方案
 * 算法思路：
 * 1. 使用树上倍增算法预处理每个节点的祖先信息
 * 2. 对于每次查询的三个点，计算它们两两之间的LCA
 * 3. 找到深度最深的LCA作为集合点，使得总花费最少
 * 
 * 时间复杂度：
 * - 预处理：O(n log n)
 * - 单次查询：O(log n)
 * 空间复杂度：O(n log n)
 */
public class Code01_EmergencyAssembly1 {

	// 最大节点数
	public static int MAXN = 500001;

	// 最大跳跃级别（log2(500001) ≈ 19）
	public static int LIMIT = 19;

	// 实际使用的最大跳跃级别
	public static int power;

	// 链式前向星存储树的邻接表
	// head[i] 表示节点i的第一条边的索引
	public static int[] head = new int[MAXN];

	// next[i] 表示第i条边的下一条边的索引
	public static int[] next = new int[MAXN << 1];

	// to[i] 表示第i条边指向的节点
	public static int[] to = new int[MAXN << 1];

	// 边的计数器
	public static int cnt;

	// deep[i] : i节点在第几层，用于计算距离
	public static int[] deep = new int[MAXN];

	// 利用stjump求最低公共祖先
	// stjump[i][j] 表示节点i向上跳2^j步到达的节点
	public static int[][] stjump = new int[MAXN][LIMIT];

	// 最优集合点
	public static int togather;

	// 最小总花费
	public static long cost;

	/**
	 * 初始化数据结构
	 * @param n 节点数量
	 */
	public static void build(int n) {
		power = log2(n);
		cnt = 1;
		Arrays.fill(head, 1, n + 1, 0);
	}

	/**
	 * 计算log2(n)的值
	 * @param n 输入值
	 * @return log2(n)的整数部分
	 */
	public static int log2(int n) {
		int ans = 0;
		while ((1 << ans) <= (n >> 1)) {
			ans++;
		}
		return ans;
	}

	/**
	 * 添加一条无向边到邻接表中
	 * @param u 起点
	 * @param v 终点
	 */
	public static void addEdge(int u, int v) {
		next[cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt++;
	}

	/**
	 * 深度优先搜索，构建深度信息和倍增表
	 * @param u 当前节点
	 * @param f 父节点
	 */
	public static void dfs(int u, int f) {
		// 记录当前节点的深度
		deep[u] = deep[f] + 1;
		// 记录直接父节点（跳1步）
		stjump[u][0] = f;
		// 构建倍增表：stjump[u][p] = stjump[stjump[u][p-1]][p-1]
		// 即：向上跳2^p步 = 向上跳2^(p-1)步后再跳2^(p-1)步
		for (int p = 1; p <= power; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		// 递归处理子节点
		for (int e = head[u]; e != 0; e = next[e]) {
			if (to[e] != f) {
				dfs(to[e], u);
			}
		}
	}

	/**
	 * 使用倍增算法计算两个节点的最近公共祖先(LCA)
	 * @param a 节点a
	 * @param b 节点b
	 * @return 节点a和b的最近公共祖先
	 */
	public static int lca(int a, int b) {
		// 确保a是深度更深的节点
		if (deep[a] < deep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		// 将a提升到与b相同的深度
		for (int p = power; p >= 0; p--) {
			if (deep[stjump[a][p]] >= deep[b]) {
				a = stjump[a][p];
			}
		}
		// 如果a和b已经在同一节点，直接返回
		if (a == b) {
			return a;
		}
		// 同时向上跳跃，直到找到公共祖先
		for (int p = power; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		// 返回最近公共祖先
		return stjump[a][0];
	}

	/**
	 * 主函数，处理输入和输出
	 * @param args 命令行参数
	 * @throws IOException IO异常
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int m = (int) in.nval;
		build(n);
		// 读取边信息并构建邻接表
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);
		}
		// 从节点1开始DFS，构建深度信息和倍增表
		dfs(1, 0);
		// 处理m次查询
		for (int i = 1, a, b, c; i <= m; i++) {
			in.nextToken();
			a = (int) in.nval;
			in.nextToken();
			b = (int) in.nval;
			in.nextToken();
			c = (int) in.nval;
			compute(a, b, c);
			out.println(togather + " " + cost);
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算三个点的最优集合点
	 * 算法思路：
	 * 1. 计算三个点两两之间的LCA
	 * 2. 找到深度最深的LCA作为集合点
	 * 3. 计算总花费
	 * 
	 * 时间复杂度：O(log n) - 每次查询
	 * 空间复杂度：O(1)
	 * 
	 * @param a 第一个点
	 * @param b 第二个点
	 * @param c 第三个点
	 */
	public static void compute(int a, int b, int c) {
		// 来自对结构关系的深入分析，课上重点解释
		// 计算三个点两两之间的LCA
		int h1 = lca(a, b), h2 = lca(a, c), h3 = lca(b, c);
		// 找到深度最浅的LCA
		int high = h1 != h2 ? (deep[h1] < deep[h2] ? h1 : h2) : h1;
		// 找到深度最深的LCA
		int low = h1 != h2 ? (deep[h1] > deep[h2] ? h1 : h2) : h3;
		// 最优集合点是深度最深的LCA
		togather = low;
		// 计算总花费：三个点到集合点的距离之和
		// 距离公式：deep[a] + deep[b] + deep[c] - deep[high] * 2 - deep[low]
		cost = (long) deep[a] + deep[b] + deep[c] - deep[high] * 2 - deep[low];
	}

}