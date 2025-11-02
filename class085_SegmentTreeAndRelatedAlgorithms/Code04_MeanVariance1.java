package class112;

// 平均数和方差 - 线段树实现
// 题目来源：洛谷 P1471 https://www.luogu.com.cn/problem/P1471
// 
// 题目描述：
// 给定一个长度为n的数组arr，操作分为三种类型，一共调用m次
// 操作 1 l r : arr数组中[l, r]范围上每个数字加上k，k为double类型
// 操作 2 l r : 查询arr数组中[l, r]范围上所有数字的平均数，返回double类型
// 操作 3 l r : 查询arr数组中[l, r]范围上所有数字的方差，返回double类型
// 
// 解题思路：
// 使用线段树维护区间和与区间平方和来计算平均数和方差
// 1. 维护两个信息：区间和sum1和区间平方和sum2
// 2. 利用数学公式：平均数 = 区间和 / 区间长度，方差 = 区间平方和/区间长度 - (区间和/区间长度)^2
// 3. 区间加法操作时，需要同时更新sum1和sum2
// 
// 核心思想：
// 数学原理：
// 1. 平均数公式：mean = Σxi / n
// 2. 方差公式：variance = Σ(xi - mean)^2 / n = Σxi^2 / n - (Σxi / n)^2
// 
// 当区间内每个数都加上v时：
// 假设原区间有n个数，和为S，平方和为S2
// 新的区间和 S' = S + n*v
// 新的区间平方和 S2' = Σ(xi + v)^2 = Σ(xi^2 + 2*v*xi + v^2) = S2 + 2*v*S + n*v^2
// 
// 时间复杂度分析：
// - 建树：O(n)
// - 区间更新：O(log n)
// - 区间查询：O(log n)
// 空间复杂度：O(n)
//
// 如下实现是正确的，但是洛谷平台对空间卡的很严，只有使用C++能全部通过
// C++版本就是本节代码中的Code04_MeanVariance2文件
// C++版本和java版本逻辑完全一样，但只有C++版本可以通过所有测试用例
// java的版本就是无法完全通过，空间会超过限制，主要是IO空间占用大
// 这是洛谷平台没有照顾各种语言的实现所导致的
// 在真正笔试、比赛时，一定是兼顾各种语言的，该实现是一定正确的

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Code04_MeanVariance1 {

	// 最大节点数，设置为100001*4以确保足够的空间
	public static int MAXN = 100001;

	// arr[i] 表示原数组第i个元素的值
	public static double[] arr = new double[MAXN];

	// sum1[i] 表示线段树节点i维护的区间内元素的和
	public static double[] sum1 = new double[MAXN << 2];

	// sum2[i] 表示线段树节点i维护的区间内元素平方的和
	public static double[] sum2 = new double[MAXN << 2];

	// addv[i] 表示线段树节点i的懒惰标记，记录区间内每个数字需要增加的值
	public static double[] addv = new double[MAXN << 2];

	/**
	 * 向上更新函数
	 * 更新当前节点的值为左右子节点值的和
	 * 在线段树中，父节点的值通常由子节点的值计算得出
	 * 对于本问题，父节点维护的区间和等于左右子节点维护区间和之和
	 * 父节点维护的区间平方和等于左右子节点维护区间平方和之和
	 * @param i 当前节点在线段树数组中的索引
	 */
	public static void up(int i) {
		// 更新区间和：当前节点的区间和等于左右子节点区间和之和
		sum1[i] = sum1[i << 1] + sum1[i << 1 | 1];
		// 更新区间平方和：当前节点的区间平方和等于左右子节点区间平方和之和
		sum2[i] = sum2[i << 1] + sum2[i << 1 | 1];
	}

	/**
	 * 向下传递懒惰标记
	 * 将当前节点的懒惰标记传递给左右子节点
	 * 懒惰传播是线段树优化的重要技术，用于延迟更新操作
	 * 只有在真正需要访问子节点时才将更新操作传递下去，避免不必要的计算
	 * @param i 当前节点在线段树数组中的索引
	 * @param ln 左子树节点数量
	 * @param rn 右子树节点数量
	 */
	public static void down(int i, int ln, int rn) {
		if (addv[i] != 0) {
			// 将加法标记传递给左右子节点
			// 左子树区间长度为ln，右子树区间长度为rn
			lazy(i << 1, addv[i], ln);
			lazy(i << 1 | 1, addv[i], rn);
			// 清除当前节点的加法标记
			// 标记已传递，当前节点的懒惰标记清零
			addv[i] = 0;
		}
	}

	/**
	 * 懒惰标记处理函数
	 * 对节点i维护的区间内每个数字加上值v
	 * 这是区间加法操作的核心实现
	 * 当对长度为n的区间加上值v时，需要同时更新区间和与区间平方和
	 * @param i 要更新的节点在线段树数组中的索引
	 * @param v 要加上的值
	 * @param n 该节点维护的区间长度
	 */
	public static void lazy(int i, double v, int n) {
		// 更新平方和：新平方和 = 原平方和 + 2*v*原和 + v*v*区间长度
		// 数学推导：Σ(xi + v)^2 = Σxi^2 + 2*v*Σxi + n*v^2
		sum2[i] += sum1[i] * v * 2 + v * v * n;
		// 更新和：新和 = 原和 + v*区间长度
		// 数学推导：Σ(xi + v) = Σxi + n*v
		sum1[i] += v * n;
		// 更新懒惰标记
		// 记录区间需要加上的值，用于后续的懒惰传播
		addv[i] += v;
	}

	/**
	 * 构建线段树
	 * 采用递归方式构建线段树，每个节点维护一个区间的信息
	 * 叶子节点对应数组中的单个元素，非叶子节点对应区间的合并结果
	 * @param l 区间左边界
	 * @param r 区间右边界
	 * @param i 当前节点在线段树数组中的索引
	 */
	public static void build(int l, int r, int i) {
		if (l == r) {
			// 叶子节点，初始化为原数组的值和平方值
			// 叶子节点对应原数组中的一个具体元素
			sum1[i] = arr[l];          // 区间和即为该元素的值
			sum2[i] = arr[l] * arr[l]; // 区间平方和即为该元素的平方值
		} else {
			int mid = (l + r) >> 1;
			// 递归构建左子树
			build(l, mid, i << 1);
			// 递归构建右子树
			build(mid + 1, r, i << 1 | 1);
			// 向上更新当前节点的值
			// 将左右子节点的值合并到当前节点
			up(i);
		}
		// 初始化懒惰标记为0
		// 初始时没有任何区间更新操作，懒惰标记为0
		addv[i] = 0;
	}

	/**
	 * 区间加法操作
	 * 对区间[jobl, jobr]内的所有元素加上值jobv
	 * 利用懒惰传播优化，避免对每个元素逐一加法
	 * @param jobl 操作区间左边界
	 * @param jobr 操作区间右边界
	 * @param jobv 要加上的值
	 * @param l 当前节点维护的区间左边界
	 * @param r 当前节点维护的区间右边界
	 * @param i 当前节点在线段树数组中的索引
	 */
	public static void add(int jobl, int jobr, double jobv, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			// 当前节点维护的区间完全包含在操作区间内，直接打懒惰标记
			// 这是懒惰传播的关键：只标记不立即执行
			// 区间长度为r-l+1，要加上的值为jobv
			lazy(i, jobv, r - l + 1);
		} else {
			int mid = (l + r) >> 1;
			// 向下传递懒惰标记
			// 在递归处理子节点之前，需要确保当前节点的懒惰标记已经传递
			down(i, mid - l + 1, r - mid);
			// 递归处理左子树
			// 只有当操作区间与左子树区间有交集时才继续处理
			if (jobl <= mid) {
				add(jobl, jobr, jobv, l, mid, i << 1);
			}
			// 递归处理右子树
			// 只有当操作区间与右子树区间有交集时才继续处理
			if (jobr > mid) {
				add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
			// 向上更新当前节点的值
			// 将子节点的更新结果合并到当前节点
			up(i);
		}
	}

	/**
	 * 区间查询操作
	 * 查询区间[jobl, jobr]内sum数组的和
	 * 在查询过程中需要确保懒惰标记已经正确传递
	 * @param sum 要查询的数组(sum1或sum2)
	 * @param jobl 查询区间左边界
	 * @param jobr 查询区间右边界
	 * @param l 当前节点维护的区间左边界
	 * @param r 当前节点维护的区间右边界
	 * @param i 当前节点在线段树数组中的索引
	 * @return 区间内sum数组的和
	 */
	public static double query(double[] sum, int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			// 当前节点维护的区间完全包含在查询区间内，直接返回节点值
			// 这是线段树查询的优化点：如果当前区间完全在查询区间内，直接返回结果
			return sum[i];
		}
		int mid = (l + r) >> 1;
		// 向下传递懒惰标记
		// 在查询时必须确保懒惰标记已经传递，以保证结果正确
		down(i, mid - l + 1, r - mid);
		double ans = 0;
		// 递归查询左子树
		// 只有当查询区间与左子树区间有交集时才继续查询
		if (jobl <= mid) {
			ans += query(sum, jobl, jobr, l, mid, i << 1);
		}
		// 递归查询右子树
		// 只有当查询区间与右子树区间有交集时才继续查询
		if (jobr > mid) {
			ans += query(sum, jobl, jobr, mid + 1, r, i << 1 | 1);
		}
		return ans;
	}

	public static void main(String[] args) throws IOException {
		// 使用Kattio类进行高效的输入输出处理
		Kattio io = new Kattio();
		
		// 读取输入参数
		int n = io.nextInt();   // 数组长度
		int m = io.nextInt();   // 操作数量
		
		// 读取初始数组
		// 数组下标从1开始，便于线段树处理
		for (int i = 1; i <= n; i++) {
			arr[i] = io.nextDouble();
		}
		
		// 构建线段树
		// 初始化线段树，维护区间和与区间平方和
		build(1, n, 1);
		
		double jobv;
		// 处理m个操作
		for (int i = 1, op, jobl, jobr; i <= m; i++) {
			op = io.nextInt();  // 操作类型
			
			if (op == 1) {
				// 区间加法操作
				// 对区间[jobl, jobr]内的所有元素加上值jobv
				jobl = io.nextInt();   // 操作区间左边界
				jobr = io.nextInt();   // 操作区间右边界
				jobv = io.nextDouble(); // 要加上的值
				add(jobl, jobr, jobv, 1, n, 1);
			} else if (op == 2) {
				// 查询平均数操作
				// 平均数 = 区间和 / 区间长度
				jobl = io.nextInt();   // 查询区间左边界
				jobr = io.nextInt();   // 查询区间右边界
				// 查询区间和
				double sum = query(sum1, jobl, jobr, 1, n, 1);
				// 计算区间长度
				double size = jobr - jobl + 1;
				// 平均数 = 区间和 / 区间长度
				double ans = sum / size;
				io.println(String.format("%.4f", ans));
			} else {
				// 查询方差操作
				// 方差 = 区间平方和/区间长度 - (区间和/区间长度)^2
				jobl = io.nextInt();   // 查询区间左边界
				jobr = io.nextInt();   // 查询区间右边界
				
				// 查询区间和
				double a = query(sum1, jobl, jobr, 1, n, 1);
				// 查询区间平方和
				double b = query(sum2, jobl, jobr, 1, n, 1);
				// 计算区间长度
				double size = jobr - jobl + 1;
				
				// 方差 = 区间平方和/区间长度 - (区间和/区间长度)^2
				// 根据数学公式：variance = E(X^2) - [E(X)]^2
				double ans = b / size - (a / size) * (a / size);
				io.println(String.format("%.4f", ans));
			}
		}
		
		// 刷新输出缓冲区并关闭资源
		io.flush();
		io.close();
	}

	// Kattio类IO效率很好，但还是不如StreamTokenizer
	// 只有StreamTokenizer无法正确处理时，才考虑使用这个类
	// 参考链接 : https://oi-wiki.org/lang/java-pro/
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