package class159;

// 路径和子树的异或，java版
// 一共有n个节点，n-1条边，组成一棵树，1号节点为树头，每个节点给定点权
// 一共有m条查询，每条查询是如下两种类型中的一种
// 1 x y   : 以x为头的子树中任选一个值，希望异或y之后的值最大，打印最大值
// 2 x y z : 节点x到节点y的路径中任选一个值，希望异或z之后的值最大，打印最大值
// 2 <= n、m <= 10^5
// 1 <= 点权、z < 2^30
// 测试链接 : https://www.luogu.com.cn/problem/P4592
// java实现的逻辑一定是正确的，但是通过不了
// 因为这道题根据C++的运行空间，制定通过标准，根本没考虑java的用户
// 想通过用C++实现，本节课Code03_PathDfnXor2文件就是C++的实现
// 两个版本的逻辑完全一样，C++版本可以通过所有测试

// 补充题目1: 树上子树异或最大值查询
// 在树结构中，每个节点有权值，查询以某节点为根的子树中与给定值异或的最大值
// 相关题目:
// - https://www.luogu.com.cn/problem/P4592
// - https://codeforces.com/problemset/problem/1175/G
// - https://www.hdu.edu.cn/problem/4757

// 补充题目2: 树上路径异或最大值查询
// 在树结构中，每个节点有权值，查询两点间路径上与给定值异或的最大值
// 相关题目:
// - https://www.luogu.com.cn/problem/P4592
// - https://www.hdu.edu.cn/problem/4757
// - https://codeforces.com/problemset/problem/1175/G

// 补充题目3: 树上DFS序应用
// 利用DFS序将树上子树问题转化为区间问题
// 相关题目:
// - https://www.luogu.com.cn/problem/P4592
// - https://codeforces.com/problemset/problem/620/E
// - https://www.spoj.com/problems/DQUERY/

// 补充题目4: LCA应用 - 树上路径查询
// 利用最近公共祖先(LCA)算法解决树上路径查询问题
// 相关题目:
// - https://www.luogu.com.cn/problem/P3379
// - https://codeforces.com/problemset/problem/1304/E
// - https://www.spoj.com/problems/LCA/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_PathDfnXor1 {

	// 最大节点数
	public static int MAXN = 100001;

	// Trie树最大节点数
	public static int MAXT = MAXN * 62;

	// 倍增数组最大高度
	public static int MAXH = 16;

	// 位数，由于数字范围是1 <= 点权、z < 2^30，所以最多需要30位
	public static int BIT = 29;

	// 节点数和查询数
	public static int n, m;

	// 每个节点的点权
	public static int[] arr = new int[MAXN];

	// 链式前向星需要的数组
	// head[i]表示节点i的第一条边的编号
	public static int[] head = new int[MAXN];
	// next[i]表示第i条边的下一条边的编号
	public static int[] next = new int[MAXN << 1];
	// to[i]表示第i条边指向的节点
	public static int[] to = new int[MAXN << 1];
	// 链式前向星的边的计数器
	public static int cntg = 0;

	// 树上dfs求节点深度
	public static int[] deep = new int[MAXN];

	// 树上dfs求子树大小
	public static int[] size = new int[MAXN];

	// 树上dfs求st表（用于LCA计算）
	public static int[][] stjump = new int[MAXN][MAXH];

	// 树上dfs求每个节点的dfn序号（DFS序）
	public static int[] dfn = new int[MAXN];

	// dfn序号计数器
	public static int cntd = 0;

	// 1类型的可持久化01Trie，根据dfn序号的次序建树（用于子树查询）
	public static int[] root1 = new int[MAXN];

	// 2类型的可持久化01Trie，根据父节点的版本建新树（用于路径查询）
	public static int[] root2 = new int[MAXN];

	// 1类型和2类型都可以用这个tree结构
	// tree[i][0/1]表示Trie树节点i的左右子节点编号
	public static int[][] tree = new int[MAXT][2];

	// 1类型和2类型都可以用这个pass数组
	// pass[i]表示经过Trie树节点i的数字个数
	public static int[] pass = new int[MAXT];

	// 1类型和2类型一起的节点计数器
	public static int cntt = 0;

	/**
	 * 添加一条无向边到链式前向星
	 * @param u 起点
	 * @param v 终点
	 */
	public static void addEdge(int u, int v) {
		// 创建新边
		next[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	/**
	 * 在可持久化Trie树中插入一个数字
	 * @param num 要插入的数字
	 * @param i 前一个版本的根节点编号
	 * @return 新版本的根节点编号
	 */
	public static int insert(int num, int i) {
		// 创建新根节点
		int rt = ++cntt;
		// 复用前一个版本的左右子树
		tree[rt][0] = tree[i][0];
		tree[rt][1] = tree[i][1];
		// 经过该节点的数字个数加1
		pass[rt] = pass[i] + 1;
		
		// 从高位到低位处理数字的每一位
		for (int b = BIT, path, pre = rt, cur; b >= 0; b--, pre = cur) {
			// 提取第b位的值（0或1）
			path = (num >> b) & 1;
			// 获取前一个版本中对应子节点
			i = tree[i][path];
			// 创建新节点
			cur = ++cntt;
			// 复用前一个版本的子节点信息
			tree[cur][0] = tree[i][0];
			tree[cur][1] = tree[i][1];
			// 更新经过该节点的数字个数
			pass[cur] = pass[i] + 1;
			// 连接父子节点
			tree[pre][path] = cur;
		}
		return rt;
	}

	/**
	 * 在可持久化Trie树中查询区间[u,v]与num异或的最大值
	 * @param num 查询的数字
	 * @param u 区间左边界对应版本的根节点编号
	 * @param v 区间右边界对应版本的根节点编号
	 * @return 最大异或值
	 */
	public static int query(int num, int u, int v) {
		int ans = 0;
		// 从高位到低位贪心选择使异或结果最大的路径
		for (int b = BIT, path, best; b >= 0; b--) {
			// 提取第b位的值
			path = (num >> b) & 1;
			// 贪心策略：尽量选择与当前位相反的路径
			best = path ^ 1;
			// 如果在区间[u,v]中存在best路径，则选择该路径
			if (pass[tree[v][best]] > pass[tree[u][best]]) {
				// 将第b位置为1
				ans += 1 << b;
				// 移动到best子节点
				u = tree[u][best];
				v = tree[v][best];
			} else {
				// 否则只能选择相同路径
				u = tree[u][path];
				v = tree[v][path];
			}
		}
		return ans;
	}

	// 按道理说dfs1应该改成迭代版，防止递归爆栈
	// 不过本题给定的空间很小，java版怎么也无法通过，索性不改了
	// 有兴趣的同学可以看一下，讲解118，详解了树上dfs从递归版改迭代版
	/**
	 * 第一次DFS遍历树，计算节点深度、子树大小、ST表和DFS序
	 * @param u 当前节点
	 * @param fa 父节点
	 */
	public static void dfs1(int u, int fa) {
		// 计算节点深度
		deep[u] = deep[fa] + 1;
		// 初始化子树大小
		size[u] = 1;
		// 设置直接父节点
		stjump[u][0] = fa;
		// 记录DFS序号
		dfn[u] = ++cntd;
		// 倍增计算祖先节点（用于LCA）
		for (int p = 1; p < MAXH; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		// 遍历子节点
		for (int ei = head[u], v; ei > 0; ei = next[ei]) {
			v = to[ei];
			if (v != fa) {
				// 递归处理子节点
				dfs1(v, u);
				// 累加子树大小
				size[u] += size[v];
			}
		}
	}

	// 按道理说dfs2应该改成迭代版，防止递归爆栈
	// 不过本题给定的空间很小，java版怎么也无法通过，索性不改了
	// 有兴趣的同学可以看一下，讲解118，详解了树上dfs从递归版改迭代版
	/**
	 * 第二次DFS遍历树，构建两种可持久化Trie树
	 * @param u 当前节点
	 * @param fa 父节点
	 */
	public static void dfs2(int u, int fa) {
		// 根据DFS序构建Trie树（用于子树查询）
		root1[dfn[u]] = insert(arr[u], root1[dfn[u] - 1]);
		// 根据父节点版本构建Trie树（用于路径查询）
		root2[u] = insert(arr[u], root2[fa]);
		// 遍历子节点
		for (int ei = head[u]; ei > 0; ei = next[ei]) {
			if (to[ei] != fa) {
				// 递归处理子节点
				dfs2(to[ei], u);
			}
		}
	}

	/**
	 * 计算两个节点的最近公共祖先(LCA)
	 * @param a 节点a
	 * @param b 节点b
	 * @return 最近公共祖先节点编号
	 */
	public static int lca(int a, int b) {
		// 确保a节点深度不小于b节点
		if (deep[a] < deep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		// 将a节点向上跳到与b节点同一深度
		for (int p = MAXH - 1; p >= 0; p--) {
			if (deep[stjump[a][p]] >= deep[b]) {
				a = stjump[a][p];
			}
		}
		// 如果a和b在同一节点，直接返回
		if (a == b) {
			return a;
		}
		// 同时向上跳，直到找到最近公共祖先
		for (int p = MAXH - 1; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		// 返回最近公共祖先的父节点
		return stjump[a][0];
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		// 读入每个节点的点权
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		// 读入树的边信息
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			// 添加无向边
			addEdge(u, v);
			addEdge(v, u);
		}
		// 第一次DFS遍历
		dfs1(1, 0);
		// 第二次DFS遍历
		dfs2(1, 0);
		// 处理查询
		for (int i = 1, op, x, y, z; i <= m; i++) {
			in.nextToken();
			op = (int) in.nval;
			in.nextToken();
			x = (int) in.nval;
			in.nextToken();
			y = (int) in.nval;
			// 子树查询
			if (op == 1) {
				// 查询以x为根的子树中与y异或的最大值
				// 子树在DFS序中是连续的区间[dfn[x], dfn[x]+size[x]-1]
				out.println(query(y, root1[dfn[x] - 1], root1[dfn[x] + size[x] - 1]));
			} else {
				// 路径查询
				in.nextToken();
				z = (int) in.nval;
				// 计算x和y的最近公共祖先
				int lcafa = stjump[lca(x, y)][0];
				// 利用容斥原理计算路径上与z异或的最大值
				int ans = Math.max(query(z, root2[lcafa], root2[x]), query(z, root2[lcafa], root2[y]));
				out.println(ans);
			}
		}
		out.flush();
		out.close();
		br.close();
	}

}