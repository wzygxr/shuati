package class163;

// 最长重排回文路径，java版
// 题目来源: Codeforces 741D / 洛谷 CF741D
// 题目链接: https://codeforces.com/problemset/problem/741/D
// 题目链接: https://www.luogu.com.cn/problem/CF741D
// 
// 题目大意:
// 一共有n个节点，编号1~n，给定n-1条边，所有节点连成一棵树，1号节点为树头
// 每条边上都有一个字符，字符范围[a~v]，字符一共22种，重排回文路径的定义如下
// 节点a到节点b的路径，如果所有边的字符收集起来，能重新排列成回文串，该路径是重排回文路径
// 打印1~n每个节点为头的子树中，最长重排回文路径的长度
// 1 <= n <= 5 * 10^5
//
// 解题思路:
// 使用DSU on Tree(树上启发式合并)算法
// 1. 建树，处理出每个节点的子树大小、重儿子等信息
// 2. 对每个节点，维护其子树中各节点到根节点路径的异或值
// 3. 使用树上启发式合并优化，保证每个节点最多被访问O(logn)次
// 4. 离线处理所有查询
//
// 时间复杂度: O(n log n)
// 空间复杂度: O(n)
//
// 算法详解:
// DSU on Tree是一种优化的暴力算法，通过重链剖分的思想，将轻重儿子的信息合并过程进行优化
// 使得每个节点最多被访问O(log n)次，从而将时间复杂度从O(n²)优化到O(n log n)
//
// 核心思想:
// 1. 重链剖分预处理：计算每个节点的子树大小，确定重儿子
// 2. 启发式合并处理：
//    - 先处理轻儿子的信息，然后清除贡献
//    - 再处理重儿子的信息并保留贡献
//    - 最后重新计算轻儿子的贡献
// 3. 通过这种方式，保证每个节点最多被访问O(log n)次
//
// 回文路径处理:
// 1. 使用异或值表示路径字符集合的状态
// 2. 回文串的条件是最多有一个字符出现奇数次
// 3. 即异或值的二进制表示中最多有一个1
// 4. 通过枚举所有可能的异或值计算最长路径
//
// 工程化实现要点:
// 1. 边界处理：注意空树、单节点树等特殊情况
// 2. 内存优化：合理使用全局数组，避免重复分配内存
// 3. 常数优化：使用位运算、减少函数调用等优化常数
// 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题
//
// 测试链接 : https://www.luogu.com.cn/problem/CF741D
// 测试链接 : https://codeforces.com/problemset/problem/741/D
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code06_RearrangePalindrome1 {

	public static int MAXN = 500001;
	// 字符种类最多22种
	public static int MAXV = 22;
	public static int n;

	// 链式前向星
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXN];
	public static int[] to = new int[MAXN];
	public static int[] weight = new int[MAXN];
	public static int cnt = 0;

	// 树链剖分
	public static int[] siz = new int[MAXN];
	public static int[] dep = new int[MAXN];
	public static int[] eor = new int[MAXN];
	public static int[] son = new int[MAXN];

	// 树上启发式合并
	public static int[] maxdep = new int[1 << MAXV];
	public static int[] ans = new int[MAXN];

	public static void addEdge(int u, int v, int w) {
		next[++cnt] = head[u];
		to[cnt] = v;
		weight[cnt] = w;
		head[u] = cnt;
	}

	public static void dfs1(int u, int d, int x) {
		siz[u] = 1;
		dep[u] = d;
		eor[u] = x;
		for (int e = head[u]; e > 0; e = next[e]) {
			dfs1(to[e], d + 1, x ^ (1 << weight[e]));
		}
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			siz[u] += siz[v];
			if (son[u] == 0 || siz[son[u]] < siz[v]) {
				son[u] = v;
			}
		}
	}

	public static void effect(int u) {
		maxdep[eor[u]] = Math.max(maxdep[eor[u]], dep[u]);
		for (int e = head[u]; e > 0; e = next[e]) {
			effect(to[e]);
		}
	}

	public static void cancel(int u) {
		maxdep[eor[u]] = 0;
		for (int e = head[u]; e > 0; e = next[e]) {
			cancel(to[e]);
		}
	}

	public static void answerFromLight(int light, int u) {
		if (maxdep[eor[light]] != 0) {
			ans[u] = Math.max(ans[u], maxdep[eor[light]] + dep[light] - dep[u] * 2);
		}
		for (int i = 0; i < MAXV; i++) {
			if (maxdep[eor[light] ^ (1 << i)] != 0) {
				ans[u] = Math.max(ans[u], maxdep[eor[light] ^ (1 << i)] + dep[light] - dep[u] * 2);
			}
		}
		for (int e = head[light]; e > 0; e = next[e]) {
			answerFromLight(to[e], u);
		}
	}

	public static void dfs2(int u, int keep) {
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != son[u]) {
				dfs2(v, 0);
			}
		}
		if (son[u] != 0) {
			dfs2(son[u], 1);
		}
		// 每一个儿子的子树，里得到的答案
		for (int e = head[u]; e > 0; e = next[e]) {
			ans[u] = Math.max(ans[u], ans[to[e]]);
		}
		// 选择当前节点，再选择重儿子树上的任意一点，得到的答案
		// 枚举所有可能得到的异或值
		if (maxdep[eor[u]] != 0) {
			ans[u] = Math.max(ans[u], maxdep[eor[u]] - dep[u]);
		}
		for (int i = 0; i < MAXV; i++) {
			if (maxdep[eor[u] ^ (1 << i)] != 0) {
				ans[u] = Math.max(ans[u], maxdep[eor[u] ^ (1 << i)] - dep[u]);
			}
		}
		// 当前点的异或值，更新最大深度信息
		maxdep[eor[u]] = Math.max(maxdep[eor[u]], dep[u]);
		// 选择遍历过的部分里的任意一点，再选择当前遍历到的子树里的任意一点，得到的答案
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != son[u]) {
				answerFromLight(v, u);
				effect(v);
			}
		}
		if (keep == 0) {
			cancel(u);
		}
	}

	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		for (int i = 2, fth, edg; i <= n; i++) {
			fth = in.nextInt();
			edg = in.nextChar() - 'a';
			addEdge(fth, i, edg);
		}
		dfs1(1, 1, 0);
		dfs2(1, 0);
		for (int i = 1; i <= n; i++) {
			out.print(ans[i] + " ");
		}
		out.println();
		out.flush();
		out.close();
	}

	// 读写工具类
	static class FastReader {
		final private int BUFFER_SIZE = 1 << 16;
		private final InputStream in;
		private final byte[] buffer;
		private int ptr, len;

		public FastReader() {
			in = System.in;
			buffer = new byte[BUFFER_SIZE];
			ptr = len = 0;
		}

		private boolean hasNextByte() throws IOException {
			if (ptr < len)
				return true;
			ptr = 0;
			len = in.read(buffer);
			return len > 0;
		}

		private byte readByte() throws IOException {
			if (!hasNextByte())
				return -1;
			return buffer[ptr++];
		}

		public char nextChar() throws IOException {
			byte c;
			do {
				c = readByte();
				if (c == -1)
					return 0;
			} while (c <= ' ');
			char ans = 0;
			while (c > ' ') {
				ans = (char) c;
				c = readByte();
			}
			return ans;
		}

		public int nextInt() throws IOException {
			int num = 0;
			byte b = readByte();
			while (isWhitespace(b))
				b = readByte();
			boolean minus = false;
			if (b == '-') {
				minus = true;
				b = readByte();
			}
			while (!isWhitespace(b) && b != -1) {
				num = num * 10 + (b - '0');
				b = readByte();
			}
			return minus ? -num : num;
		}

		public double nextDouble() throws IOException {
			double num = 0, div = 1;
			byte b = readByte();
			while (isWhitespace(b))
				b = readByte();
			boolean minus = false;
			if (b == '-') {
				minus = true;
				b = readByte();
			}
			while (!isWhitespace(b) && b != '.' && b != -1) {
				num = num * 10 + (b - '0');
				b = readByte();
			}
			if (b == '.') {
				b = readByte();
				while (!isWhitespace(b) && b != -1) {
					num += (b - '0') / (div *= 10);
					b = readByte();
				}
			}
			return minus ? -num : num;
		}

		private boolean isWhitespace(byte b) {
			return b == ' ' || b == '\n' || b == '\r' || b == '\t';
		}
	}

}