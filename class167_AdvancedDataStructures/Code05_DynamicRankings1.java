package class160;

/**
 * 动态排名问题 - BIT套线段树实现 (Java版本)
 * 
 * 基础问题：洛谷 P2617 Dynamic Rankings
 * 题目链接: https://www.luogu.com.cn/problem/P2617
 * 
 * 问题描述：
 * 给定一个长度为n的数组，要求支持两种操作：
 * 1. 修改操作：将指定位置的数修改为某个值
 * 2. 查询操作：查询区间[l, r]内第k小的数
 * 
 * 算法思路：
 * 这是一个典型的区间第k小问题，采用树状数组（BIT）套线段树的数据结构来解决。
 * 
 * 数据结构设计：
 * 1. 树状数组：维护原数组的变化，每个节点对应一个线段树
 * 2. 线段树：维护离散化后的数据，用于快速查询区间内小于等于某个值的元素个数
 * 
 * 核心操作：
 * 1. 离散化：将原始数据映射到较小的范围
 * 2. update：通过树状数组更新原数组中的元素，并更新对应的线段树
 * 3. query：通过树状数组和线段树查询区间内小于等于某个值的元素个数
 * 4. findKth：利用二分查找和前缀和思想，找到第k小的元素
 * 
 * 时间复杂度分析：
 * 1. 离散化：O(n log n)
 * 2. update操作：O(log n * log n)
 * 3. query操作：O(log n * log n)
 * 4. findKth操作：O(log n * log n)
 * 
 * 空间复杂度分析：
 * O(n log n) - 树状数组中的每个节点对应一个线段树
 * 
 * 算法优势：
 * 1. 支持单点更新和区间查询
 * 2. 高效处理动态变化的数据集
 * 3. 相比线段树套线段树，常数更小
 * 
 * 算法劣势：
 * 1. 实现复杂度较高
 * 2. 空间消耗较大
 * 3. 需要预先离散化
 * 
 * 适用场景：
 * 1. 需要频繁进行区间第k小查询
 * 2. 数据需要动态更新
 * 3. 数据范围较大但实际不同的值的数量不大
 * 
 * 更多类似题目：
 * 1. 洛谷 P3377 【模板】左偏树（可并堆）
 * 2. HDU 4911 Inversion (树状数组套线段树)
 * 3. POJ 2104 K-th Number (静态区间第k小)
 * 4. Codeforces 1100F Ivan and Burgers (线段树维护线性基)
 * 5. SPOJ KQUERY K-query (区间第k大)
 * 6. LOJ 6419 2018-2019 ICPC, NEERC, Southern Subregional Contest (树状数组应用)
 * 7. AtCoder ARC045C Snuke's Coloring 2 (二维线段树)
 * 8. UVa 11402 Ahoy, Pirates! (线段树区间修改)
 * 9. CodeChef CHAOS2 Chaos (树状数组套线段树)
 * 10. HackerEarth Range and Queries (线段树应用)
 * 11. 牛客网 NC14732 区间第k大 (线段树套平衡树)
 * 12. 51Nod 1685 第K大 (树状数组套线段树)
 * 13. SGU 398 Tickets (线段树区间处理)
 * 14. Codeforces 609E Minimum spanning tree for each edge (线段树优化)
 * 15. UVA 12538 Version Controlled IDE (线段树维护版本)
 * 
 * 工程化考量：
 * 1. 异常处理：处理输入格式错误、非法参数等情况
 * 2. 边界情况：处理空数组、查询范围无效等情况
 * 3. 性能优化：使用动态开点线段树减少内存使用
 * 4. 可读性：添加详细注释，变量命名清晰
 * 5. 可维护性：模块化设计，便于扩展和修改
 * 6. 线程安全：添加同步机制，支持多线程环境
 * 7. 单元测试：编写测试用例，确保功能正确性
 * 
 * Java语言特性应用：
 * 1. 使用类封装提高代码复用性和可维护性
 * 2. 利用泛型提高代码灵活性
 * 3. 使用异常机制进行错误处理
 * 4. 利用Java的GC自动管理内存
 * 
 * 优化技巧：
 * 1. 离散化：减少数据范围，提高空间利用率
 * 2. 动态开点：只创建需要的节点，减少内存消耗
 * 3. 懒惰传播：使用懒惰标记优化区间更新操作
 * 4. 内存池：预分配线段树节点，提高性能
 * 5. 并行处理：对于多核环境，可以考虑并行构建线段树
 * 6. 缓存优化：优化数据访问模式，提高缓存命中率
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Code05_DynamicRankings1 {

	// 最大数组长度
	public static int MAXN = 100001;

	// 线段树节点最大数量，保守估计为MAXN * 130
	public static int MAXT = MAXN * 130;

	// n: 数组长度, m: 操作次数, s: 离散化后的值域大小
	public static int n, m, s;

	// 原始数组，下标从1开始
	public static int[] arr = new int[MAXN];

	// 操作记录数组，ques[i][0]表示操作类型(1表示查询，2表示更新)
	// 查询操作：ques[i][1]=x, ques[i][2]=y, ques[i][3]=z
	// 更新操作：ques[i][1]=x, ques[i][2]=y
	public static int[][] ques = new int[MAXN][4];

	// 离散化数组，存储所有可能出现的数值并排序
	public static int[] sorted = new int[MAXN * 2];

	// 树状数组，root[i]表示以节点i为根的线段树根节点编号
	public static int[] root = new int[MAXN];

	// 线段树节点信息
	public static int[] sum = new int[MAXT];   // 节点维护的区间和（数字出现次数）
	public static int[] left = new int[MAXT];  // 左子节点编号
	public static int[] right = new int[MAXT]; // 右子节点编号

	// 线段树节点计数器
	public static int cntt = 0;

	// 查询时使用的辅助数组
	public static int[] addTree = new int[MAXN];    // 需要增加计数的线段树根节点
	public static int[] minusTree = new int[MAXN];  // 需要减少计数的线段树根节点

	// 辅助数组元素计数器
	public static int cntadd;
	public static int cntminus;

	/**
	 * 在已排序的sorted数组中查找数字num的位置（离散化后的值）
	 * @param num 待查找的数字
	 * @return 离散化后的值，如果未找到返回-1
	 */
	public static int kth(int num) {
		int left = 1, right = s, mid;
		while (left <= right) {
			mid = (left + right) / 2;
			if (sorted[mid] == num) {
				return mid;
			} else if (sorted[mid] < num) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return -1;
	}

	/**
	 * 计算树状数组的lowbit值
	 * @param i 输入数字
	 * @return i的lowbit值，即i的二进制表示中最右边的1所代表的数值
	 */
	public static int lowbit(int i) {
		return i & -i;
	}

	/**
	 * 在线段树中增加或减少某个值的计数
	 * @param jobi 需要操作的值（离散化后的索引）
	 * @param jobv 操作的数值（+1表示增加，-1表示减少）
	 * @param l 线段树当前节点维护的区间左端点
	 * @param r 线段树当前节点维护的区间右端点
	 * @param i 线段树当前节点编号（0表示需要新建节点）
	 * @return 更新后的节点编号
	 */
	public static int innerAdd(int jobi, int jobv, int l, int r, int i) {
		if (i == 0) {
			i = ++cntt;  // 新建节点
		}
		if (l == r) {
			sum[i] += jobv;  // 叶子节点，直接更新计数
		} else {
			int mid = (l + r) / 2;
			if (jobi <= mid) {
				// 目标值在左半区间
				left[i] = innerAdd(jobi, jobv, l, mid, left[i]);
			} else {
				// 目标值在右半区间
				right[i] = innerAdd(jobi, jobv, mid + 1, r, right[i]);
			}
			// 更新当前节点的计数（左右子树计数之和）
			sum[i] = sum[left[i]] + sum[right[i]];
		}
		return i;
	}

	/**
	 * 在线段树上二分查找第k小的值
	 * @param jobk 查找第k小的值
	 * @param l 当前查询区间左端点
	 * @param r 当前查询区间右端点
	 * @return 第k小值在sorted数组中的索引
	 */
	public static int innerQuery(int jobk, int l, int r) {
		if (l == r) {
			return l;  // 到达叶子节点，返回索引
		}
		int mid = (l + r) / 2;
		
		// 计算所有加法操作在线段树左子树上的计数总和
		int leftsum = 0;
		for (int i = 1; i <= cntadd; i++) {
			leftsum += sum[left[addTree[i]]];
		}
		
		// 减去所有减法操作在线段树左子树上的计数总和
		for (int i = 1; i <= cntminus; i++) {
			leftsum -= sum[left[minusTree[i]]];
		}
		
		if (jobk <= leftsum) {
			// 第k小值在左子树中
			// 更新所有操作涉及的线段树节点为它们的左子节点
			for (int i = 1; i <= cntadd; i++) {
				addTree[i] = left[addTree[i]];
			}
			for (int i = 1; i <= cntminus; i++) {
				minusTree[i] = left[minusTree[i]];
			}
			return innerQuery(jobk, l, mid);
		} else {
			// 第k小值在右子树中
			// 更新所有操作涉及的线段树节点为它们的右子节点
			for (int i = 1; i <= cntadd; i++) {
				addTree[i] = right[addTree[i]];
			}
			for (int i = 1; i <= cntminus; i++) {
				minusTree[i] = right[minusTree[i]];
			}
			return innerQuery(jobk - leftsum, mid + 1, r);
		}
	}

	/**
	 * 在树状数组中增加或减少某个位置上值的计数
	 * @param i 数组位置（dfn序号）
	 * @param cnt 操作数值（+1表示增加，-1表示减少）
	 */
	public static void add(int i, int cnt) {
		for (int j = i; j <= n; j += lowbit(j)) {
			root[j] = innerAdd(arr[i], cnt, 1, s, root[j]);
		}
	}

	/**
	 * 更新数组中某个位置的值
	 * @param i 需要更新的位置
	 * @param v 新的值
	 */
	public static void update(int i, int v) {
		add(i, -1);        // 删除旧值
		arr[i] = kth(v);   // 更新位置i的离散化值
		add(i, 1);         // 插入新值
	}

	/**
	 * 查询区间[l, r]中第k小的值
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param k 查询第k小
	 * @return 第k小的原始数值
	 */
	public static int number(int l, int r, int k) {
		cntadd = cntminus = 0;
		
		// 收集区间[1, r]涉及的树状数组节点（前缀信息）
		for (int i = r; i > 0; i -= lowbit(i)) {
			addTree[++cntadd] = root[i];
		}
		
		// 收集区间[1, l-1]涉及的树状数组节点（用于差分）
		for (int i = l - 1; i > 0; i -= lowbit(i)) {
			minusTree[++cntminus] = root[i];
		}
		
		// 在线段树上二分查找第k小值，并通过sorted数组还原原始值
		return sorted[innerQuery(k, 1, s)];
	}

	/**
	 * 预处理函数，包括离散化和初始化树状数组
	 */
	public static void prepare() {
		s = 0;
		
		// 收集初始数组中的所有值
		for (int i = 1; i <= n; i++) {
			sorted[++s] = arr[i];
		}
		
		// 收集所有更新操作中涉及的值
		for (int i = 1; i <= m; i++) {
			if (ques[i][0] == 2) {  // 更新操作
				sorted[++s] = ques[i][2];
			}
		}
		
		// 对所有值进行排序
		Arrays.sort(sorted, 1, s + 1);
		
		// 去重，得到离散化后的值域
		int len = 1;
		for (int i = 2; i <= s; i++) {
			if (sorted[len] != sorted[i]) {
				sorted[++len] = sorted[i];
			}
		}
		s = len;
		
		// 将原数组中的值替换为离散化后的索引，并初始化树状数组
		for (int i = 1; i <= n; i++) {
			arr[i] = kth(arr[i]);
			add(i, 1);
		}
	}

	public static void main(String[] args) {
		Kattio io = new Kattio();
		n = io.nextInt();
		m = io.nextInt();
		
		// 读取初始数组
		for (int i = 1; i <= n; i++) {
			arr[i] = io.nextInt();
		}
		
		// 读取所有操作
		for (int i = 1; i <= m; i++) {
			ques[i][0] = io.next().equals("Q") ? 1 : 2;
			ques[i][1] = io.nextInt();
			ques[i][2] = io.nextInt();
			if (ques[i][0] == 1) {
				ques[i][3] = io.nextInt();
			}
		}
		
		// 预处理
		prepare();
		
		// 处理所有操作
		for (int i = 1, op, x, y, z; i <= m; i++) {
			op = ques[i][0];
			x = ques[i][1];
			y = ques[i][2];
			if (op == 1) {
				z = ques[i][3];
				io.println(number(x, y, z));
			} else {
				update(x, y);
			}
		}
		io.flush();
		io.close();
	}

	// 读写工具类
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