package class177;

// 树上莫队入门题，java版
// 题目来源：SPOJ COT2 - Count on a tree II
// 题目链接：https://www.spoj.com/problems/COT2/
// 题目链接：https://www.luogu.com.cn/problem/SP10707
// 题目大意：
// 一共有n个节点，每个节点给定颜色值，给定n-1条边，所有节点连成一棵树
// 一共有m条查询，格式 u v : 打印点u到点v的简单路径上，有几种不同的颜色
// 1 <= n <= 4 * 10^4
// 1 <= m <= 10^5
// 1 <= 颜色值 <= 2 * 10^9
// 
// 解题思路：
// 树上莫队是莫队算法在树上的扩展
// 核心思想：
// 1. 使用欧拉序将树上问题转化为序列问题
// 2. 利用莫队算法处理转化后的序列问题
// 3. 通过特定的处理方式，解决树上路径查询问题
// 
// 算法要点：
// 1. 使用DFS生成欧拉序（括号序），每个节点会在进入和离开时各记录一次
// 2. 利用倍增法预处理LCA（最近公共祖先）
// 3. 将树上路径查询转化为欧拉序上的区间查询
// 4. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置排序
// 5. 通过翻转操作维护当前窗口中的节点状态
//
// 时间复杂度：O((n+m)*sqrt(n))
// 空间复杂度：O(n)
// 
// 相关题目：
// 1. SPOJ COT2 Count on a tree II - https://www.spoj.com/problems/COT2/
// 2. 洛谷 SP10707 COT2 - Count on a tree II - https://www.luogu.com.cn/problem/SP10707
// 3. 洛谷 P2495 [SDOI2011] 消耗战 - https://www.luogu.com.cn/problem/P2495 (树上问题)
//
// 莫队算法变种题目推荐：
// 1. 普通莫队：
//    - 洛谷 P1494 小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    - SPOJ DQUERY - https://www.luogu.com.cn/problem/SP3267
//    - Codeforces 617E XOR and Favorite Number - https://codeforces.com/contest/617/problem/E
//    - 洛谷 P2709 小B的询问 - https://www.luogu.com.cn/problem/P2709
//
// 2. 带修莫队：
//    - 洛谷 P1903 数颜色 - https://www.luogu.com.cn/problem/P1903
//    - LibreOJ 2874 历史研究 - https://loj.ac/p/2874
//    - Codeforces 940F Machine Learning - https://codeforces.com/contest/940/problem/F
//
// 3. 树上莫队：
//    - SPOJ COT2 Count on a tree II - https://www.luogu.com.cn/problem/SP10707
//    - 洛谷 P4074 糖果公园 - https://www.luogu.com.cn/problem/P4074
//
// 4. 二次离线莫队：
//    - 洛谷 P4887 第十四分块(前体) - https://www.luogu.com.cn/problem/P4887
//    - 洛谷 P5398 GCD - https://www.luogu.com.cn/problem/P5398
//
// 5. 回滚莫队：
//    - 洛谷 P5906 相同数最远距离 - https://www.luogu.com.cn/problem/P5906
//    - SPOJ ZQUERY Zero Query - https://www.spoj.com/problems/ZQUERY/
//    - AtCoder JOISC 2014 C 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code07_MoOnTree1 {

	public static int MAXN = 40001;
	public static int MAXM = 100001;
	public static int MAXP = 20;
	public static int n, m;
	public static int[] color = new int[MAXN];
	public static int[] sorted = new int[MAXN];
	public static int cntv;

	// 查询的参数，jobl、jobr、lca、id
	// 如果是类型1，那么lca == 0，表示空缺
	// 如果是类型2，那么lca > 0，查询结果需要补充这个单点信息
	public static int[][] query = new int[MAXM][4];

	// 链式前向星存储树结构
	public static int[] head = new int[MAXN];
	public static int[] to = new int[MAXN << 1];
	public static int[] next = new int[MAXN << 1];
	public static int cntg;

	// dep是深度
	// seg是括号序（欧拉序）
	// st是节点开始序
	// ed是节点结束序
	// stjump是倍增表（用于求LCA）
	// cntd是括号序列的长度
	public static int[] dep = new int[MAXN];
	public static int[] seg = new int[MAXN << 1];
	public static int[] st = new int[MAXN];
	public static int[] ed = new int[MAXN];
	public static int[][] stjump = new int[MAXN][MAXP];
	public static int cntd;

	// 分块
	public static int[] bi = new int[MAXN << 1];

	// 窗口信息
	// vis[i]表示节点i是否在当前窗口中
	public static boolean[] vis = new boolean[MAXN];
	// cnt[i]表示颜色i在当前窗口中的出现次数
	public static int[] cnt = new int[MAXN];
	// 当前窗口中不同颜色的种类数
	public static int kind = 0;

	public static int[] ans = new int[MAXM];

	// 添加边到链式前向星结构中
	public static void addEdge(int u, int v) {
		next[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	// 二分查找离散化值
	public static int kth(int num) {
		int left = 1, right = cntv, mid, ret = 0;
		while (left <= right) {
			mid = (left + right) / 2;
			if (sorted[mid] <= num) {
				ret = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return ret;
	}

	// DFS生成欧拉序和预处理LCA信息
	public static void dfs(int u, int fa) {
		dep[u] = dep[fa] + 1;  // 记录节点深度
		seg[++cntd] = u;       // 记录进入节点u的时间戳
		st[u] = cntd;          // 记录节点u的进入时间
		stjump[u][0] = fa;     // 初始化倍增表
		
		// 填充倍增表
		for (int p = 1; p < MAXP; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		
		// 遍历u的所有子节点
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != fa) {
				dfs(v, u);
			}
		}
		
		seg[++cntd] = u;  // 记录离开节点u的时间戳
		ed[u] = cntd;     // 记录节点u的离开时间
	}

	// 使用倍增法求两个节点的最近公共祖先(LCA)
	public static int lca(int a, int b) {
		// 确保a的深度不小于b
		if (dep[a] < dep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		
		// 将a向上跳到与b同一深度
		for (int p = MAXP - 1; p >= 0; p--) {
			if (dep[stjump[a][p]] >= dep[b]) {
				a = stjump[a][p];
			}
		}
		
		// 如果a就是b，说明b是a的祖先
		if (a == b) {
			return a;
		}
		
		// a和b一起向上跳，直到它们的父节点相同
		for (int p = MAXP - 1; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		
		// 返回最近公共祖先
		return stjump[a][0];
	}

	// 普通莫队经典排序
	public static class QueryCmp implements Comparator<int[]> {

		@Override
		public int compare(int[] a, int[] b) {
			if (bi[a[0]] != bi[b[0]]) {
				return bi[a[0]] - bi[b[0]];
			}
			return a[1] - b[1];
		}

	}

	// 翻转节点node的状态（添加或删除）
	// 这是树上莫队的核心操作
	public static void invert(int node) {
		int val = color[node];  // 获取节点颜色
		if (vis[node]) {
			// 如果节点在当前窗口中，删除它
			if (--cnt[val] == 0) {
				kind--;  // 如果该颜色的出现次数变为0，种类数减1
			}
		} else {
			// 如果节点不在当前窗口中，添加它
			if (++cnt[val] == 1) {
				kind++;  // 如果该颜色首次出现，种类数加1
			}
		}
		// 更新节点访问状态
		vis[node] = !vis[node];
	}

	// 核心计算函数
	public static void compute() {
		// 当前窗口在欧拉序中的左右边界
		int winl = 1, winr = 0;
		
		// 依次处理所有查询
		for (int i = 1; i <= m; i++) {
			int jobl = query[i][0];  // 查询左边界（欧拉序中的位置）
			int jobr = query[i][1];  // 查询右边界（欧拉序中的位置）
			int lca = query[i][2];   // 查询路径的LCA
			int id = query[i][3];    // 查询编号
			
			// 调整窗口左边界
			while (winl > jobl) {
				invert(seg[--winl]);
			}
			
			// 调整窗口右边界
			while (winr < jobr) {
				invert(seg[++winr]);
			}
			
			// 继续调整窗口左边界
			while (winl < jobl) {
				invert(seg[winl++]);
			}
			
			// 继续调整窗口右边界
			while (winr > jobr) {
				invert(seg[winr--]);
			}
			
			// 如果LCA不在查询路径的端点上，需要特殊处理
			if (lca > 0) {
				invert(lca);
			}
			
			// 记录答案
			ans[id] = kind;
			
			// 恢复LCA的状态
			if (lca > 0) {
				invert(lca);
			}
		}
	}

	// 预处理函数
	public static void prepare() {
		// 复制颜色数组用于离散化
		for (int i = 1; i <= n; i++) {
			sorted[i] = color[i];
		}
		
		// 排序去重，实现离散化
		Arrays.sort(sorted, 1, n + 1);
		cntv = 1;
		for (int i = 2; i <= n; i++) {
			if (sorted[cntv] != sorted[i]) {
				sorted[++cntv] = sorted[i];
			}
		}
		
		// 将颜色数组元素替换为离散化后的值
		for (int i = 1; i <= n; i++) {
			color[i] = kth(color[i]);
		}
		
		// 对欧拉序分块
		int blen = (int) Math.sqrt(cntd);
		for (int i = 1; i <= cntd; i++) {
			bi[i] = (i - 1) / blen + 1;
		}
		
		// 对查询进行排序
		Arrays.sort(query, 1, m + 1, new QueryCmp());
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		
		// 读取每个节点的颜色值
		for (int i = 1; i <= n; i++) {
			color[i] = in.nextInt();
		}
		
		// 读取树的边，构建链式前向星
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt();
			v = in.nextInt();
			addEdge(u, v);
			addEdge(v, u);
		}
		
		// 从节点1开始DFS，生成欧拉序
		dfs(1, 0);
		
		// 处理查询
		for (int i = 1, u, v, uvlca; i <= m; i++) {
			u = in.nextInt();
			v = in.nextInt();
			
			// 确保u的进入时间不大于v的进入时间
			if (st[v] < st[u]) {
				int tmp = u;
				u = v;
				v = tmp;
			}
			
			// 计算u和v的LCA
			uvlca = lca(u, v);
			
			// 根据u和LCA的关系确定查询在欧拉序中的范围
			if (u == uvlca) {
				// u是LCA，查询范围是[u的进入时间, v的进入时间]
				query[i][0] = st[u];
				query[i][1] = st[v];
				query[i][2] = 0;  // LCA是端点，不需要特殊处理
			} else {
				// u不是LCA，查询范围是[u的离开时间, v的进入时间]
				// 需要特殊处理LCA节点
				query[i][0] = ed[u];
				query[i][1] = st[v];
				query[i][2] = uvlca;  // 记录LCA
			}
			query[i][3] = i;  // 查询编号
		}
		
		prepare();
		compute();
		
		// 输出结果
		for (int i = 1; i <= m; i++) {
			out.println(ans[i]);
		}
		out.flush();
		out.close();
	}

	// 读写工具类
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