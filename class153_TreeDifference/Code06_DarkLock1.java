/**
 * 暗的连锁 (LOJ 10131)
 * 题目来源：LibreOJ
 * 题目链接：https://loj.ac/problem/10131
 * 
 * 题目描述：
 * 给定一棵包含N个节点的树（N-1条边），以及M条额外的边（非树边）
 * 每条非树边连接两个节点，与树边一起构成一个连通图
 * 求有多少种方案，通过切断一条树边和一条非树边，使得图变得不连通
 * 
 * 算法原理：树上边差分
 * 树上边差分与点差分类似，但针对边进行操作。
 * 对于每条非树边(u,v)，它在原树上会形成一个环，环上的所有树边都被这条非树边覆盖。
 * 通过边差分，我们可以：
 * 1. num[u]++
 * 2. num[v]++
 * 3. num[lca(u,v)] -= 2
 * 最后通过一次DFS回溯累加子节点的差分标记，得到每条边被覆盖的次数。
 * 
 * 时间复杂度分析：
 * - 预处理LCA：O(N log N)
 * - 差分标记：O(M log N)，其中M是非树边数量，每次需要计算LCA
 * - DFS回溯统计：O(N)
 * 总时间复杂度：O((N + M) log N)
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
 * 树上边差分是解决此类问题的最优解，通过O(1)的操作标记每条非树边的影响范围，
 * 避免了暴力遍历每条环上的树边，时间复杂度比暴力方法的O(M*N)有极大提升。
 */
package class122;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code06_DarkLock1 {

	/**
	 * 最大节点数量
	 * 题目中节点数量范围：1 <= N <= 100000
	 */
	public static final int MAXN = 100001;

	/**
	 * 倍增数组的层数限制
	 * 2^17 = 131072 > 100000，足够处理题目中的最大节点数
	 */
	public static final int LIMIT = 17;

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
	 * 节点数量和非树边数量
	 */
	public static int n, m;

	/**
	 * 差分数组
	 * num[i]表示节点i到其父节点的边，被多少条非树边覆盖
	 * 注意：边被表示为子节点的属性，这样可以避免处理根节点的特殊情况
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
	public static int[] next_edge = new int[MAXN << 1]; // 避免与关键字next冲突
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
	 * 最终答案
	 */
	public static int ans;

	/**
	 * 初始化算法所需的数据结构
	 * 设置数组初始值，准备处理新的测试用例
	 */
	public static void build() {
		power = log2(n);
		// 初始化差分数组，从1开始因为节点编号从1开始
		Arrays.fill(num, 1, n + 1, 0);
		// 边索引从1开始，0表示没有边
		cnt = 1;
		// 初始化链式前向星的head数组
		Arrays.fill(head, 1, n + 1, 0);
		// 初始化答案
		ans = 0;
	}

	/**
	 * 向链式前向星中添加一条无向边
	 * 
	 * @param u 边的一个端点
	 * @param v 边的另一个端点
	 */
	public static void addEdge(int u, int v) {
		// 添加u到v的边
		next_edge[cnt] = head[u];
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
		for (int e = head[u]; e != 0; e = next_edge[e]) {
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
	 * 第二次DFS，计算每条边被覆盖的次数，并统计满足条件的方案数
	 * 
	 * @param u 当前处理的节点
	 * @param f 当前节点的父节点
	 * 
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(log N) - 递归调用栈深度
	 * 
	 * 算法逻辑：
	 * 1. 先递归处理所有子节点
	 * 2. 统计每个子节点到父节点这条边的覆盖次数
	 * 3. 根据覆盖次数计算切断这条树边的可行方案数
	 * 4. 累加子节点的覆盖次数到当前节点
	 */
	public static void dfs2(int u, int f) {
		// 递归处理所有子节点
		for (int e = head[u], v; e != 0; e = next_edge[e]) {
			v = to[e];
			if (v != f) {
				dfs2(v, u);
			}
		}
		
		// 统计每条边的覆盖次数和方案数
		for (int e = head[u], v, coverage; e != 0; e = next_edge[e]) {
			v = to[e];
			if (v != f) {
				// 获取边(u,v)的覆盖次数，存储在v的num数组中
				coverage = num[v];
				
				/**
				 * 方案数统计逻辑：
				 * - 覆盖次数为0：这条树边不在任何非树边形成的环中
				 *   切断它后，无论切断哪条非树边，图都会不连通
				 *   共有m种方案（m是非树边的数量）
				 * 
				 * - 覆盖次数为1：这条树边恰好只在一个非树边形成的环中
				 *   只有切断对应的那条非树边，图才会不连通
				 *   共有1种方案
				 * 
				 * - 覆盖次数>=2：这条树边在多个非树边形成的环中
				 *   切断它后，需要切断所有对应的非树边才能使图不连通
				 *   但题目要求只切断一条非树边，因此没有可行方案
				 *   共有0种方案
				 */
				if (coverage == 0) {
					ans += m;  // 可以与任意一条非树边配对
				} else if (coverage == 1) {
					ans += 1;  // 只能与对应的那条非树边配对
				}
				// coverage >= 2 时不需要增加ans
				
				// 将子节点的覆盖次数累加到父节点，完成差分标记的传播
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
		
		// 读取节点数
		in.nextToken();
		n = (int) in.nval;
		// 读取非树边数
		in.nextToken();
		m = (int) in.nval;
		
		// 初始化数据结构
		build();
		
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
		
		// 处理所有非树边，执行树上边差分操作
		for (int i = 1, u, v, lca_node; i <= m; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			// 计算u和v的LCA
			lca_node = lca(u, v);
			
			/**
			 * 树上边差分核心操作
			 * 对于非树边(u,v)，它在原树上会形成一个环
			 * 通过边差分，我们标记环上的所有树边
			 * 1. 在u处+1
			 * 2. 在v处+1
			 * 3. 在LCA处-2，抵消多余的标记
			 */
			num[u]++;
			num[v]++;
			num[lca_node] -= 2;
		}
		
		// 计算最终答案
		dfs2(1, 0);
		
		// 输出结果
		out.println(ans);
		// 确保输出被刷新
		out.flush();
		// 关闭资源
		out.close();
		br.close();
	}

}