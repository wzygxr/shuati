package class122;

/**
 * 树上点差分模版(递归版)
 * 
 * 题目来源：洛谷 P3128 [USACO15DEC] Max Flow P
 * 题目链接：https://www.luogu.com.cn/problem/P3128
 * 
 * 题目描述：
 * 给定一棵包含N个节点的树，以及K次操作。每次操作需要将一条路径上的所有点的点权加1。
 * 所有操作完成后，返回树上点权的最大值。
 * 
 * 算法原理：树上点差分
 * 树上点差分是普通差分数组在树结构上的扩展应用，用于高效处理树上路径的区间修改问题。
 * 对于每次路径u到v的操作：
 * 1. diff[u] += 1
 * 2. diff[v] += 1
 * 3. diff[lca(u,v)] -= 1
 * 4. diff[father[lca(u,v)]] -= 1
 * 最后通过一次DFS回溯累加子节点的差分标记，得到每个节点的最终点权。
 * 
 * 时间复杂度分析：
 * - 预处理LCA：O(N log N)
 * - 差分标记：O(K log N)，其中K是操作次数，每次需要计算LCA
 * - DFS回溯统计：O(N)
 * 总时间复杂度：O((N + K) log N)
 * 
 * 空间复杂度分析：
 * - 树的存储：O(N)
 * - LCA倍增数组：O(N log N)
 * - 差分数组：O(N)
 * 总空间复杂度：O(N log N)
 * 
 * 工程化考量：
 * 1. 使用链式前向星存储树结构，提高空间效率和遍历速度
 * 2. 使用StreamTokenizer进行高效输入，处理大量数据时性能优于Scanner
 * 3. 使用PrintWriter进行高效输出，支持缓冲
 * 4. 采用静态成员变量减少对象创建，在算法竞赛中常用此技巧
 * 
 * 最优解分析：
 * 树上点差分是解决此类问题的最优解，通过O(1)的操作标记每条路径的影响范围，
 * 避免了暴力遍历每条路径上的所有节点，时间复杂度比暴力方法的O(K*N)有极大提升。
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code01_MaxFlow1 {

	/**
	 * 最大节点数量
	 * 题目中节点数量范围：2 <= N <= 50000
	 */
	public static int MAXN = 50001;

	/**
	 * 倍增数组的层数限制
	 * 2^16 = 65536 > 50000，足够处理题目中的最大节点数
	 */
	public static int LIMIT = 16;

	/**
	 * 最大幂次，根据节点数量动态计算
	 */
	public static int power;

	/**
	 * 计算log2(n)的整数部分
	 * 
	 * @param n 输入整数
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
	 * 差分数组
	 * num[i]表示节点i被路径覆盖的次数的差分值
	 */
	public static int[] num = new int[MAXN];

	/**
	 * 链式前向星存储树结构
	 * head[u]: 节点u的第一个边的索引
	 * next[e]: 边e的下一个边的索引
	 * to[e]: 边e指向的节点
	 * cnt: 当前可用的边索引
	 */
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXN << 1];
	public static int[] to = new int[MAXN << 1];
	public static int cnt;

	/**
	 * LCA相关数组
	 * deep[u]: 节点u的深度
	 * stjump[u][p]: 节点u的2^p级祖先
	 */
	public static int[] deep = new int[MAXN];
	public static int[][] stjump = new int[MAXN][LIMIT];

	/**
	 * 初始化算法所需的数据结构
	 * 设置数组初始值，准备处理新的测试用例
	 * 
	 * @param n 节点数量
	 */
	public static void build(int n) {
		power = log2(n);
		// 初始化差分数组，从1开始因为节点编号从1开始
		Arrays.fill(num, 1, n + 1, 0);
		// 边索引从1开始，0表示没有边
		cnt = 1;
		// 初始化链式前向星的head数组
		Arrays.fill(head, 1, n + 1, 0);
	}

	/**
	 * 向链式前向星中添加一条无向边
	 * 
	 * @param u 边的一个端点
	 * @param v 边的另一个端点
	 */
	public static void addEdge(int u, int v) {
		// 添加u到v的边
		next[cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt++;
	}

	/**
	 * 第一次DFS，预处理每个节点的深度和倍增跳跃数组
	 * 
	 * @param u 当前处理的节点
	 * @param f 当前节点的父节点
	 * 
	 * 时间复杂度：O(N log N)
	 * 空间复杂度：O(log N) - 递归调用栈深度
	 */
	public static void dfs1(int u, int f) {
		// 设置当前节点的深度，父节点深度+1
		deep[u] = deep[f] + 1;
		// 设置当前节点的直接父节点（2^0级祖先）
		stjump[u][0] = f;
		
		// 预处理倍增数组
		// 利用动态规划思想：u的2^p级祖先 = u的2^(p-1)级祖先的2^(p-1)级祖先
		for (int p = 1; p <= power; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		
		// 递归处理所有子节点
		for (int e = head[u]; e != 0; e = next[e]) {
			if (to[e] != f) {
				dfs1(to[e], u);
			}
		}
	}

	/**
	 * 使用倍增法计算两个节点的最近公共祖先
	 * 
	 * @param a 第一个节点
	 * @param b 第二个节点
	 * @return a和b的最近公共祖先
	 * 
	 * 时间复杂度：O(log N)
	 * 空间复杂度：O(1)
	 */
	public static int lca(int a, int b) {
		// 确保a的深度不小于b
		if (deep[a] < deep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		
		// 将a向上跳到与b同一深度
		for (int p = power; p >= 0; p--) {
			if (deep[stjump[a][p]] >= deep[b]) {
				a = stjump[a][p];
			}
		}
		
		// 如果此时a==b，则找到了LCA
		if (a == b) {
			return a;
		}
		
		// 继续向上跳，直到找到LCA
		for (int p = power; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		
		// 返回它们的父节点
		return stjump[a][0];
	}

	/**
	 * 第二次DFS，计算每个节点被覆盖的次数，并找出最大值
	 * 
	 * @param u 当前处理的节点
	 * @param f 当前节点的父节点
	 * 
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(log N) - 递归调用栈深度
	 * 
	 * 算法逻辑：
	 * 1. 先递归处理所有子节点
	 * 2. 累加子节点的覆盖次数到当前节点
	 * 3. 当前节点的最终覆盖次数即为答案
	 */
	public static void dfs2(int u, int f) {
		// 递归处理所有子节点
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				dfs2(v, u);
			}
		}
		
		// 将子节点的覆盖次数累加到父节点，完成差分标记的传播
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				num[u] += num[v];
			}
		}
	}

	/**
	 * 主函数，处理输入输出并调用相应的算法函数
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 使用高效的输入方式
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		// 使用高效的输出方式
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取节点数和操作数
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int m = (int) in.nval;
		
		// 初始化数据结构
		build(n);
		
		// 构建树结构，添加n-1条树边
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);  // 无向图，添加反向边
		}
		
		// 预处理LCA所需的数据
		// 以节点1为根节点进行DFS
		dfs1(1, 0);
		
		// 处理所有操作，执行树上点差分操作
		for (int i = 1, u, v, lca_node, lca_father; i <= m; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			// 计算u和v的LCA
			lca_node = lca(u, v);
			// 计算LCA的父节点
			lca_father = stjump[lca_node][0];
			
			/**
			 * 树上点差分核心操作
			 * 对于路径(u,v)，它在原树上会形成一条路径
			 * 通过点差分，我们标记路径上的所有节点
			 * 1. 在u处+1
			 * 2. 在v处+1
			 * 3. 在LCA处-1
			 * 4. 在LCA的父节点处-1
			 */
			num[u]++;
			num[v]++;
			num[lca_node]--;
			num[lca_father]--;
		}
		
		// 计算每个节点的最终覆盖次数
		dfs2(1, 0);
		
		// 找出最大的覆盖次数
		int max = 0;
		for (int i = 1; i <= n; i++) {
			max = Math.max(max, num[i]);
		}
		
		// 输出结果
		out.println(max);
		out.flush();
		out.close();
		br.close();
	}

}