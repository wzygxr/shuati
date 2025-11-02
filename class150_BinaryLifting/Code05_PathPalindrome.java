package class119;

// 检查树上两节点间的路径是否是回文问题
// 问题描述：
// 一颗树上有n个节点，编号1~n
// 给定长度为n的数组parent, parent[i]表示节点i的父节点编号
// 给定长度为n的数组s, s[i]表示节点i上是什么字符
// 从节点a到节点b经过节点最少的路，叫做a和b的路径
// 一共有m条查询，每条查询(a,b)，a和b的路径字符串是否是回文
// 是回文打印"YES"，不是回文打印"NO"
// 1 <= n <= 10^5
// 1 <= m <= 10^5
// parent[1] = 0，即整棵树的头节点一定是1号节点
// 每个节点上的字符一定是小写字母a~z
// 测试链接 : https://ac.nowcoder.com/acm/contest/78807/G
// 
// 解题思路：
// 使用树上倍增算法预处理每个节点的祖先信息和字符串哈希值
// 对于每次查询，找到两点的LCA，然后分别计算从a到LCA和从b到LCA的路径字符串哈希值
// 比较两个哈希值是否相等来判断路径字符串是否为回文

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * 树上路径回文检查问题解决方案
 * 算法思路：
 * 1. 使用树上倍增算法预处理每个节点的祖先信息
 * 2. 使用字符串哈希技术，预处理向上和向下路径的哈希值
 * 3. 对于每次查询，找到两点的LCA，然后分别计算从a到LCA和从b到LCA的路径字符串哈希值
 * 4. 比较两个哈希值是否相等来判断路径字符串是否为回文
 * 
 * 时间复杂度：
 * - 预处理：O(n log n)
 * - 单次查询：O(log n)
 * 空间复杂度：O(n log n)
 */
public class Code05_PathPalindrome {

	// 最大节点数
	public static int MAXN = 100001;

	// 最大跳跃级别
	public static int LIMIT = 17;

	// 实际使用的最大跳跃级别
	public static int power;

	// 存储每个节点的字符（转换为数字）
	public static int[] s = new int[MAXN];

	// 链式前向星存储树的邻接表
	// head[i] 表示节点i的第一条边的索引
	public static int[] head = new int[MAXN];

	// to[i] 表示第i条边指向的节点
	public static int[] to = new int[MAXN << 1];

	// next[i] 表示第i条边的下一条边的索引
	public static int[] next = new int[MAXN << 1];

	// 边的计数器
	public static int cnt;

	// deep[i] : i节点在第几层
	public static int[] deep = new int[MAXN];

	// jump[i][j] : i节点往上跳2的j次方步，到达什么节点
	public static int[][] jump = new int[MAXN][LIMIT];

	// 哈希参数K
	public static long K = 499;

	// kpow[i] = k的i次方，用于字符串哈希计算
	public static long[] kpow = new long[MAXN];

	// stup[i][j] : i节点往上跳2的j次方步的路径字符串哈希值（向上方向）
	public static long[][] stup = new long[MAXN][LIMIT];

	// stdown[i][j] : i节点往上跳2的j次方步的路径字符串哈希值（向下方向）
	public static long[][] stdown = new long[MAXN][LIMIT];

	/**
	 * 初始化数据结构
	 * @param n 节点数量
	 */
	public static void build(int n) {
		power = log2(n);
		cnt = 1;
		// 初始化邻接表头
		Arrays.fill(head, 1, n + 1, 0);
		// 预计算K的幂次
		kpow[0] = 1;
		for (int i = 1; i <= n; i++) {
			kpow[i] = kpow[i - 1] * K;
		}
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
	 * DFS遍历构建倍增表和字符串哈希信息
	 * 算法思路：
	 * 1. 遍历树的每个节点
	 * 2. 构建深度、跳跃表
	 * 3. 构建向上和向下路径的字符串哈希值
	 * 
	 * 时间复杂度：O(n log n)
	 * 空间复杂度：O(n log n)
	 * 
	 * @param u 当前节点
	 * @param f 父节点
	 */
	public static void dfs(int u, int f) {
		// 记录深度和直接父节点
		deep[u] = deep[f] + 1;
		jump[u][0] = f;
		// 记录到父节点的字符（用于哈希计算）
		stup[u][0] = stdown[u][0] = s[f];
		// 构建倍增表和哈希值表
		for (int p = 1, v; p <= power; p++) {
			v = jump[u][p - 1];
			// 跳2^p步 = 跳2^(p-1)步后再跳2^(p-1)步
			jump[u][p] = jump[v][p - 1];
			// 计算向上路径的哈希值
			// 向上路径哈希 = 前半段哈希 * K^(后半段长度) + 后半段哈希
			stup[u][p] = stup[u][p - 1] * kpow[1 << (p - 1)] + stup[v][p - 1];
			// 计算向下路径的哈希值
			// 向下路径哈希 = 前半段哈希 * K^(后半段长度) + 后半段哈希
			stdown[u][p] = stdown[v][p - 1] * kpow[1 << (p - 1)] + stdown[u][p - 1];
		}
		// 递归处理子节点
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				dfs(v, u);
			}
		}
	}

	/**
	 * 判断路径是否为回文
	 * 算法思路：
	 * 1. 找到a和b的LCA
	 * 2. 分别计算从a到LCA和从b到LCA的路径字符串哈希值
	 * 3. 比较两个哈希值是否相等
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(1)
	 * 
	 * @param a 起点
	 * @param b 终点
	 * @return 如果路径字符串是回文返回true，否则返回false
	 */
	public static boolean isPalindrome(int a, int b) {
		// 计算a和b的LCA
		int lca = lca(a, b);
		// 计算从a到LCA的路径字符串哈希值
		long hash1 = hash(a, lca, b);
		// 计算从b到LCA的路径字符串哈希值
		long hash2 = hash(b, lca, a);
		// 比较两个哈希值是否相等
		return hash1 == hash2;
	}

	/**
	 * 计算树上两点间路径的字符串哈希值
	 * 算法思路：
	 * 1. 分成上坡和下坡两部分
	 * 2. 分别计算两部分的哈希值
	 * 3. 合并得到完整路径的哈希值
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(1)
	 * 
	 * @param from 起点
	 * @param lca 最近公共祖先
	 * @param to 终点（用于判断是否需要计算下坡部分）
	 * @return 路径字符串的哈希值
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
			if (deep[jump[a][p]] >= deep[b]) {
				a = jump[a][p];
			}
		}
		// 如果a和b已经在同一节点，直接返回
		if (a == b) {
			return a;
		}
		// 同时向上跳跃找到LCA
		for (int p = power; p >= 0; p--) {
			if (jump[a][p] != jump[b][p]) {
				a = jump[a][p];
				b = jump[b][p];
			}
		}
		// 返回最近公共祖先
		return jump[a][0];
	}

	/**
	 * 计算从from节点到lca节点再到to节点的路径字符串哈希值
	 * @param from 起始节点
	 * @param lca 最近公共祖先
	 * @param to 目标节点
	 * @return 路径字符串的哈希值
	 */
	public static long hash(int from, int lca, int to) {
		// up是上坡hash值（从from到lca）
		long up = s[from];
		// 计算上坡部分的哈希值
		for (int p = power; p >= 0; p--) {
			if (deep[jump[from][p]] >= deep[lca]) {
				// 向上路径哈希 = 前半段哈希 * K^(后半段长度) + 后半段哈希
				up = up * kpow[1 << p] + stup[from][p];
				from = jump[from][p];
			}
		}
		// 如果终点就是LCA，只需要返回上坡部分的哈希值
		if (to == lca) {
			return up;
		}
		// down是下坡hash值（从lca到to）
		long down = s[to];
		// height是目前下坡的总高度
		int height = 1;
		// 计算下坡部分的哈希值
		for (int p = power; p >= 0; p--) {
			// 注意这里是 > 而不是 >=，因为不需要包含LCA节点
			if (deep[jump[to][p]] > deep[lca]) {
				// 向下路径哈希 = 前半段哈希 * K^(后半段长度) + 后半段哈希
				down = stdown[to][p] * kpow[height] + down;
				height += 1 << p;
				to = jump[to][p];
			}
		}
		// 完整路径哈希 = 上坡哈希 * K^(下坡长度) + 下坡哈希
		return up * kpow[height] + down;
	}

	/**
	 * 主函数，处理输入和输出
	 * @param args 命令行参数
	 * @throws IOException IO异常
	 */
	public static void main(String[] args) throws IOException {
		Kattio io = new Kattio();
		int n = io.nextInt();
		// 初始化数据结构
		build(n);
		// 读取节点字符
		int si = 1;
		for (char c : io.next().toCharArray()) {
			s[si++] = c - 'a' + 1;
		}
		// 读取边信息并构建邻接表
		for (int u = 1, v; u <= n; u++) {
			v = io.nextInt();
			addEdge(u, v);
			addEdge(v, u);
		}
		// DFS预处理
		dfs(1, 0);
		int m = io.nextInt();
		// 处理查询
		for (int i = 1, a, b; i <= m; i++) {
			a = io.nextInt();
			b = io.nextInt();
			io.println(isPalindrome(a, b) ? "YES" : "NO");
		}
		io.flush();
		io.close();
	}

	// Kattio类IO效率很好，但还是不如StreamTokenizer
	// 只有StreamTokenizer无法正确处理时，才考虑使用这个类
	// 参考链接 : https://oi-wiki.org/lang/java-pro/
	/**
	 * 高效IO类，用于提高输入输出效率
	 */
	public static class Kattio extends PrintWriter {
		private BufferedReader r;
		private StringTokenizer st;

		public Kattio() {
			this(System.in, System.out);
		}

		public Kattio(InputStream i, OutputStream o) {
			super(o);
			r = new BufferedReader(new InputStreamReader(i));
		}

		public Kattio(String intput, String output) throws IOException {
			super(output);
			r = new BufferedReader(new FileReader(intput));
		}

		public String next() {
			try {
				while (st == null || !st.hasMoreTokens())
					st = new StringTokenizer(r.readLine());
				return st.nextToken();
			} catch (Exception e) {
			}
			return null;
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}
	}

}