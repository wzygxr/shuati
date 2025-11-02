package class171;

/**
 * 寻找宝藏问题 - Java版本
 * 
 * 题目来源: 洛谷P4849
 * 题目链接: https://www.luogu.com.cn/problem/P4849
 * 题目难度: 省选/NOI-
 * 
 * 题目描述:
 * 一共有n个宝藏，每个宝藏有a、b、c、d四个属性值，以及拿取之后的收益v
 * 可以选择任意顺序拿取宝藏，每次拿的宝藏的四种属性值都不能小于上次拿的宝藏
 * 打印能获得的最大收益，打印有多少种最佳拿取方法，方法数对 998244353 取余
 * 1 <= n <= 8 * 10^4
 * 1 <= a、b、c、d、v <= 10^9
 * 
 * 解题思路:
 * 这是一个四维偏序问题，结合了动态规划和计数，可以使用CDQ分治套CDQ分治来解决。
 * 
 * 问题分析:
 * 1. 需要按照四个属性值非递减的顺序拿取宝藏
 * 2. 求最大收益及对应的方案数
 * 3. 这是一个四维偏序问题
 * 
 * 算法步骤:
 * 1. 预处理：
 *    - 对属性d进行离散化处理
 *    - 按照属性a排序，合并相同属性的宝藏
 * 2. 使用CDQ分治套CDQ分治处理四维偏序：
 *    - 第一层CDQ分治处理属性a和b
 *    - 第二层CDQ分治处理属性c和d
 * 3. 在合并过程中：
 *    - 使用树状数组维护前缀最大值和对应的方案数
 *    - 更新dp值和cnt值
 * 
 * 时间复杂度: O(n log^3 n)
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理:
 *    - 处理输入异常，如非法数据格式
 *    - 处理边界情况，如空输入、极值输入
 * 2. 性能优化:
 *    - 使用快速IO提高输入输出效率
 *    - 合理使用离散化减少空间占用
 *    - 优化排序策略减少常数因子
 * 3. 代码可读性:
 *    - 添加详细注释说明算法思路
 *    - 使用有意义的变量命名
 *    - 模块化设计便于维护和扩展
 * 4. 调试能力:
 *    - 添加中间过程打印便于调试
 *    - 使用断言验证关键步骤正确性
 *    - 提供测试用例验证实现正确性
 * 
 * 与其他算法的比较:
 * 1. 与普通DP比较:
 *    - 普通DP时间复杂度O(n^2)，CDQ分治优化到O(n log^3 n)
 *    - CDQ分治空间复杂度更优
 * 2. 与树套树比较:
 *    - CDQ分治实现更简单
 *    - 树套树支持在线查询，CDQ分治需要离线处理
 * 
 * 优化策略:
 * 1. 使用离散化减少值域范围
 * 2. 优化排序策略减少常数
 * 3. 合理安排计算顺序避免重复计算
 * 4. 使用快速IO提高效率
 * 
 * 常见问题及解决方案:
 * 1. 答案错误:
 *    - 问题：贡献计算错误或边界处理不当
 *    - 解决方案：仔细检查贡献计算逻辑，验证边界条件
 * 2. 时间超限:
 *    - 问题：常数因子过大或算法复杂度分析错误
 *    - 解决方案：优化排序策略，减少不必要的操作
 * 3. 空间超限:
 *    - 问题：递归层数过深或数组开得过大
 *    - 解决方案：检查数组大小，使用全局数组，优化递归逻辑
 * 
 * 扩展应用:
 * 1. 可以处理更高维度的偏序问题
 * 2. 可以优化动态规划的转移过程
 * 3. 可以处理动态问题转静态的场景
 * 
 * 学习建议:
 * 1. 先掌握归并排序求逆序对
 * 2. 理解二维偏序问题的处理方法
 * 3. 学习三维偏序的标准处理流程
 * 4. 练习四维偏序问题
 * 5. 掌握CDQ分治优化DP的方法
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code06_Treasure1 {

	public static class Node {
		int a, b, c, d;
		int i;
		long v;
		boolean left;

		public Node(int a_, int b_, int c_, int d_, long v_) {
			a = a_;
			b = b_;
			c = c_;
			d = d_;
			v = v_;
		}
	}

	public static class Cmp1 implements Comparator<Node> {
		@Override
		public int compare(Node x, Node y) {
			if (x.a != y.a) {
				return x.a - y.a;
			}
			if (x.b != y.b) {
				return x.b - y.b;
			}
			if (x.c != y.c) {
				return x.c - y.c;
			}
			return x.d - y.d;
		}
	}

	// 根据属性b进行排序，b一样的对象，保持原始次序
	public static class Cmp2 implements Comparator<Node> {
		@Override
		public int compare(Node x, Node y) {
			if (x.b != y.b) {
				return x.b - y.b;
			}
			return x.i - y.i;
		}
	}

	// 根据属性c进行排序，c一样的对象，保持原始次序
	public static class Cmp3 implements Comparator<Node> {
		@Override
		public int compare(Node x, Node y) {
			if (x.c != y.c) {
				return x.c - y.c;
			}
			return x.i - y.i;
		}
	}

	public static Cmp1 cmp1 = new Cmp1();
	public static Cmp2 cmp2 = new Cmp2();
	public static Cmp3 cmp3 = new Cmp3();

	public static int MAXN = 80001;
	public static long INF = (long) (1e18 + 1);
	public static int MOD = 998244353;
	public static int n, s;

	public static Node[] arr = new Node[MAXN];
	public static Node[] tmp1 = new Node[MAXN];
	public static Node[] tmp2 = new Node[MAXN];
	public static int[] sortd = new int[MAXN];

	public static long[] treeVal = new long[MAXN];
	public static int[] treeCnt = new int[MAXN];

	public static long[] dp = new long[MAXN];
	public static int[] cnt = new int[MAXN];

	public static int lowbit(int i) {
		return i & -i;
	}

	public static void more(int i, long v, int c) {
		while (i <= s) {
			if (v > treeVal[i]) {
				treeVal[i] = v;
				treeCnt[i] = c % MOD;
			} else if (v == treeVal[i]) {
				treeCnt[i] = (treeCnt[i] + c) % MOD;
			}
			i += lowbit(i);
		}
	}

	public static long queryVal;
	public static int queryCnt;

	public static void query(int i) {
		queryVal = -INF;
		queryCnt = 0;
		while (i > 0) {
			if (treeVal[i] > queryVal) {
				queryVal = treeVal[i];
				queryCnt = treeCnt[i];
			} else if (treeVal[i] == queryVal) {
				queryCnt = (queryCnt + treeCnt[i]) % MOD;
			}
			i -= lowbit(i);
		}
	}

	public static void clear(int i) {
		while (i <= s) {
			treeVal[i] = -INF;
			treeCnt[i] = 0;
			i += lowbit(i);
		}
	}

	public static void merge(int l, int mid, int r) {
		for (int i = l; i <= r; i++) {
			tmp2[i] = tmp1[i];
		}
		Arrays.sort(tmp2, l, mid + 1, cmp3);
		Arrays.sort(tmp2, mid + 1, r + 1, cmp3);
		int p1, p2, id;
		for (p1 = l - 1, p2 = mid + 1; p2 <= r; p2++) {
			while (p1 + 1 <= mid && tmp2[p1 + 1].c <= tmp2[p2].c) {
				p1++;
				if (tmp2[p1].left) {
					more(tmp2[p1].d, dp[tmp2[p1].i], cnt[tmp2[p1].i]);
				}
			}
			if (!tmp2[p2].left) {
				query(tmp2[p2].d);
				id = tmp2[p2].i;
				if (queryVal + tmp2[p2].v > dp[id]) {
					dp[id] = queryVal + tmp2[p2].v;
					cnt[id] = queryCnt;
				} else if (queryVal + tmp2[p2].v == dp[id]) {
					cnt[id] = (cnt[id] + queryCnt) % MOD;
				}
			}
		}
		for (int i = l; i <= p1; i++) {
			if (tmp2[i].left) {
				clear(tmp2[i].d);
			}
		}
	}

	public static void cdq2(int l, int r) {
		if (l == r) {
			return;
		}
		int mid = (l + r) / 2;
		cdq2(l, mid);
		merge(l, mid, r);
		cdq2(mid + 1, r);
	}

	public static void cdq1(int l, int r) {
		if (l == r) {
			return;
		}
		int mid = (l + r) / 2;
		cdq1(l, mid);
		for (int i = l; i <= r; i++) {
			tmp1[i] = arr[i];
			tmp1[i].left = i <= mid;
		}
		Arrays.sort(tmp1, l, r + 1, cmp2);
		cdq2(l, r);
		cdq1(mid + 1, r);
	}

	public static int lower(int x) {
		int l = 1, r = s, ans = 1;
		while (l <= r) {
			int mid = (l + r) / 2;
			if (sortd[mid] >= x) {
				ans = mid;
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		return ans;
	}

	public static void prepare() {
		for (int i = 1; i <= n; i++) {
			sortd[i] = arr[i].d;
		}
		Arrays.sort(sortd, 1, n + 1);
		s = 1;
		for (int i = 2; i <= n; i++) {
			if (sortd[s] != sortd[i]) {
				sortd[++s] = sortd[i];
			}
		}
		for (int i = 1; i <= n; i++) {
			arr[i].d = lower(arr[i].d);
		}
		Arrays.sort(arr, 1, n + 1, cmp1);
		int m = 1;
		for (int i = 2; i <= n; i++) {
			if (arr[m].a == arr[i].a && arr[m].b == arr[i].b && arr[m].c == arr[i].c && arr[m].d == arr[i].d) {
				arr[m].v += arr[i].v;
			} else {
				arr[++m] = arr[i];
			}
		}
		n = m;
		for (int i = 1; i <= n; i++) {
			arr[i].i = i;
			dp[i] = arr[i].v;
			cnt[i] = 1;
		}
		for (int i = 1; i <= s; i++) {
			treeVal[i] = -INF;
			treeCnt[i] = 0;
		}
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 初始化节点数组
		for (int i = 0; i < MAXN; i++) {
			arr[i] = new Node(0, 0, 0, 0, 0);
			tmp1[i] = new Node(0, 0, 0, 0, 0);
			tmp2[i] = new Node(0, 0, 0, 0, 0);
		}
		
		n = in.nextInt();
		in.nextInt(); // 读取但不使用第二个参数
		for (int i = 1, a, b, c, d, v; i <= n; i++) {
			a = in.nextInt();
			b = in.nextInt();
			c = in.nextInt();
			d = in.nextInt();
			v = in.nextInt();
			arr[i] = new Node(a, b, c, d, v);
		}
		prepare();
		cdq1(1, n);
		long best = 0;
		int ways = 0;
		for (int i = 1; i <= n; i++) {
			best = Math.max(best, dp[i]);
		}
		for (int i = 1; i <= n; i++) {
			if (dp[i] == best) {
				ways = (ways + cnt[i]) % MOD;
			}
		}
		out.println(best);
		out.println(ways % MOD);
		out.flush();
		out.close();
	}

	// 读写工具类
	static class FastReader {
		private final byte[] buffer = new byte[1 << 12];
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