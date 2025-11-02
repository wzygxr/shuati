package class182;

/**
 * 线段树合并专题 - Code02_Minimax1.java
 * 
 * 根节点的概率问题（PKUWC2018 Minimax），Java版
 * 测试链接：https://www.luogu.com.cn/problem/P5298
 * 提交时请把类名改成"Main"
 * 
 * 重要说明：
 * Java实现的逻辑一定是正确的，但是本题卡常，无法通过所有测试用例
 * 想通过用C++实现，本节课Code02_Minimax2文件就是C++的实现
 * 两个版本的逻辑完全一样，C++版本可以通过所有测试
 * 
 * 题目来源：PKUWC2018
 * 题目大意：给定一棵二叉树，叶子节点有权值，非叶子节点有权值概率，
 * 求根节点权值的期望值
 * 
 * 算法思路：
 * 1. 使用离散化技术处理权值范围
 * 2. 构建动态开点线段树维护权值分布
 * 3. 采用线段树合并技术计算子树期望值
 * 4. 通过树形DP自底向上计算根节点期望
 * 
 * 核心思想：
 * - 离散化：将大范围的权值映射到小范围，节省空间
 * - 动态开点：仅在需要时创建线段树节点，避免空间浪费
 * - 线段树合并：高效合并子树信息，支持快速查询
 * - 数学期望：利用概率论计算期望值
 * - 树形DP：自底向上处理，确保子节点信息先于父节点处理
 * 
 * 时间复杂度分析：
 * - 离散化：O(n log n)
 * - DFS遍历：O(n)
 * - 线段树合并：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 线段树节点：O(n log n)
 * - 离散化数组：O(n)
 * - 总空间复杂度：O(n log n)
 * 
 * 工程化考量：
 * 1. 使用动态开点线段树节省空间
 * 2. 离散化处理大范围权值
 * 3. 后序遍历确保正确的处理顺序
 * 4. 使用迭代DFS避免递归深度限制
 * 
 * 优化技巧：
 * - 离散化优化：减少线段树的值域范围
 * - 动态开点：避免预分配大量未使用的空间
 * - 线段树合并：高效处理子树信息合并
 * - 迭代DFS：避免递归深度限制，提高稳定性
 * 
 * 边界情况处理：
 * - 单节点树
 * - 完全二叉树
 * - 链状树结构
 * - 权值全部相同的情况
 * - 大规模数据输入
 * 
 * 测试用例设计：
 * 1. 基础测试：小规模树结构验证算法正确性
 * 2. 边界测试：单节点、链状树、完全二叉树
 * 3. 性能测试：n=300000的大规模数据
 * 4. 极端测试：权值全部相同或严格递增/递减
 * 
 * 性能对比：
 * - Java版本：逻辑正确但可能超时，适合学习算法思想
 * - C++版本：相同逻辑，性能更优，可以通过所有测试
 * 
 * 扩展应用：
 * 1. 可以扩展为处理多叉树的期望计算
 * 2. 支持动态插入和删除操作
 * 3. 可以处理带权重的期望计算
 * 4. 应用于概率论和随机过程分析
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code02_Minimax1 {

	public static int MAXN = 300001;
	public static int MAXT = MAXN * 40;
	public static int MOD = 998244353;

	public static int n;

	public static int[] fa = new int[MAXN];

	public static int[] val = new int[MAXN];
	public static int[] sorted = new int[MAXN];
	public static int cntv;

	public static int[] childCnt = new int[MAXN];
	public static int[][] child = new int[MAXN][2];

	public static int[] root = new int[MAXN];
	public static int[] ls = new int[MAXT];
	public static int[] rs = new int[MAXT];
	public static long[] sum = new long[MAXT];
	public static long[] mul = new long[MAXT];
	public static int cntt;

	public static long[] d = new long[MAXN];

	/**
	 * 快速幂算法 - 计算 x^p mod MOD
	 * 
	 * @param x 底数
	 * @param p 指数
	 * @return x^p mod MOD 的结果
	 * 
	 * 算法原理：
	 * 使用二进制分解法实现快速幂计算：
	 * 1. 将指数p分解为二进制形式
	 * 2. 根据二进制位决定是否乘入结果
	 * 3. 每次循环将底数平方，指数右移
	 * 
	 * 时间复杂度: O(log p) - 指数p的二进制位数
	 * 空间复杂度: O(1) - 只使用常数空间
	 * 
	 * 数学原理：
	 * x^p = x^(b0*2^0 + b1*2^1 + ... + bk*2^k)
	 *      = x^(b0*2^0) * x^(b1*2^1) * ... * x^(bk*2^k)
	 * 
	 * 优化技巧：
	 * - 位运算：使用p & 1判断最低位，p >>= 1右移
	 * - 模运算：每次乘法后取模，防止溢出
	 * - 循环展开：避免递归调用，提高效率
	 * 
	 * 边界情况处理：
	 * - p=0：返回1（任何数的0次方等于1）
	 * - p=1：返回x
	 * - x=0：返回0（0的任何正数次方等于0）
	 * - 模运算：确保结果在[0, MOD-1]范围内
	 */
	public static long power(long x, int p) {
		long ans = 1;
		while (p != 0) {
			if ((p & 1) != 0) {
				// 当前二进制位为1，将当前底数乘入结果
				ans = ans * x % MOD;
			}
			p >>= 1; // 指数右移一位
			x = x * x % MOD; // 底数平方
		}
		return ans;
	}

	public static int kth(int num) {
		int left = 1, right = cntv, mid, ret = 0;
		while (left <= right) {
			mid = (left + right) >> 1;
			if (sorted[mid] <= num) {
				ret = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return ret;
	}

	public static void up(int i) {
		sum[i] = (sum[ls[i]] + sum[rs[i]]) % MOD;
	}

	public static void lazy(int i, long v) {
		if (i != 0) {
			sum[i] = sum[i] * v % MOD;
			mul[i] = mul[i] * v % MOD;
		}
	}

	public static void down(int i) {
		if (mul[i] != 1) {
			lazy(ls[i], mul[i]);
			lazy(rs[i], mul[i]);
			mul[i] = 1;
		}
	}

	public static int update(int jobi, int jobv, int l, int r, int i) {
		int rt = i;
		if (rt == 0) {
			rt = ++cntt;
			mul[rt] = 1;
		}
		if (l == r) {
			sum[rt] = jobv % MOD;
		} else {
			down(rt);
			int mid = (l + r) >> 1;
			if (jobi <= mid) {
				ls[rt] = update(jobi, jobv, l, mid, ls[rt]);
			} else {
				rs[rt] = update(jobi, jobv, mid + 1, r, rs[rt]);
			}
			up(rt);
		}
		return rt;
	}

	public static int merge(int l, int r, int t1, int t2, long v, long mul1, long mul2) {
		if (t1 == 0 || t2 == 0) {
			if (t1 != 0) {
				lazy(t1, mul1);
			}
			if (t2 != 0) {
				lazy(t2, mul2);
			}
			return t1 + t2;
		}
		down(t1);
		down(t2);
		int mid = (l + r) >> 1;
		int ls1 = ls[t1];
		int rs1 = rs[t1];
		int ls2 = ls[t2];
		int rs2 = rs[t2];
		long lsum1 = sum[ls1];
		long rsum1 = sum[rs1];
		long lsum2 = sum[ls2];
		long rsum2 = sum[rs2];
		long tmp = (1 - v + MOD) % MOD;
		ls[t1] = merge(l, mid, ls1, ls2, v, (mul1 + rsum2 * tmp) % MOD, (mul2 + rsum1 * tmp) % MOD);
		rs[t1] = merge(mid + 1, r, rs1, rs2, v, (mul1 + lsum2 * v) % MOD, (mul2 + lsum1 * v) % MOD);
		up(t1);
		return t1;
	}

	// 迭代版，java会爆栈，C++可以通过
	public static void dfs1(int u) {
		if (childCnt[u] == 0) {
			root[u] = update(val[u], 1, 1, cntv, root[u]);
		} else if (childCnt[u] == 1) {
			dfs1(child[u][0]);
			root[u] = root[child[u][0]];
		} else {
			dfs1(child[u][0]);
			dfs1(child[u][1]);
			root[u] = merge(1, cntv, root[child[u][0]], root[child[u][1]], val[u], 0, 0);
		}
	}

	// dfs1改成迭代版
	public static void dfs2() {
		int[][] stack = new int[n][2];
		int siz = 0;
		stack[++siz][0] = 1;
		stack[siz][1] = 0;
		while (siz > 0) {
			int u = stack[siz][0];
			int s = stack[siz--][1];
			if (childCnt[u] == 0) {
				root[u] = update(val[u], 1, 1, cntv, root[u]);
			} else if (childCnt[u] == 1) {
				if (s == 0) {
					stack[++siz][0] = u;
					stack[siz][1] = 1;
					stack[++siz][0] = child[u][0];
					stack[siz][1] = 0;
				} else {
					root[u] = root[child[u][0]];
				}
			} else {
				if (s == 0) {
					stack[++siz][0] = u;
					stack[siz][1] = 1;
					stack[++siz][0] = child[u][1];
					stack[siz][1] = 0;
					stack[++siz][0] = child[u][0];
					stack[siz][1] = 0;
				} else {
					root[u] = merge(1, cntv, root[child[u][0]], root[child[u][1]], val[u], 0, 0);
				}
			}
		}
	}

	public static void getd(int l, int r, int i) {
		if (i == 0) {
			return;
		}
		if (l == r) {
			d[l] = sum[i] % MOD;
		} else {
			down(i);
			int mid = (l + r) >> 1;
			getd(l, mid, ls[i]);
			getd(mid + 1, r, rs[i]);
		}
	}

	public static void prepare() {
		for (int i = 1; i <= n; i++) {
			if (fa[i] != 0) {
				child[fa[i]][childCnt[fa[i]]++] = i;
			}
		}
		long inv = power(10000, MOD - 2);
		for (int i = 1; i <= n; i++) {
			if (childCnt[i] == 0) {
				sorted[++cntv] = val[i];
			} else {
				val[i] = (int) (inv * val[i] % MOD);
			}
		}
		Arrays.sort(sorted, 1, cntv + 1);
		int len = 1;
		for (int i = 2; i <= cntv; i++) {
			if (sorted[len] != sorted[i]) {
				sorted[++len] = sorted[i];
			}
		}
		cntv = len;
		for (int i = 1; i <= n; i++) {
			if (childCnt[i] == 0) {
				val[i] = kth(val[i]);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		for (int i = 1; i <= n; i++) {
			fa[i] = in.nextInt();
		}
		for (int i = 1; i <= n; i++) {
			val[i] = in.nextInt();
		}
		prepare();
		// dfs1(1);
		dfs2();
		getd(1, cntv, root[1]);
		long ans = 0;
		for (int i = 1; i <= cntv; i++) {
			ans = (ans + 1L * i * sorted[i] % MOD * d[i] % MOD * d[i]) % MOD;
		}
		out.println(ans);
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