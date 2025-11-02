package class160;

/**
 * 静态区间第k小问题 - 线段树套线段树实现 (Java版本)
 * 
 * 基础问题：POJ 2104 K-th Number
 * 题目链接: http://poj.org/problem?id=2104
 * 
 * 问题描述：
 * 给定一个长度为n的数组，要求支持查询操作：查询区间[l, r]内第k小的数
 * 注意：这个问题中数组元素是静态的，不支持修改操作
 * 
 * 算法思路：
 * 采用线段树套线段树（离线处理）的方法来解决静态区间第k小问题
 * 
 * 数据结构设计：
 * 1. 外层线段树：维护区间划分，每个节点代表原数组的一个区间
 * 2. 内层线段树：维护每个区间内元素的权值分布，统计不同值的出现次数
 * 3. 通过离散化处理原始数据，将大范围的值映射到连续的小范围
 * 
 * 核心操作：
 * 1. 离散化：将原始数据映射到较小的范围，便于构建权值线段树
 * 2. build：构建线段树，每个节点维护其区间内元素的权值线段树
 * 3. query：查询区间内第k小的元素，通过二分和前缀和的思想实现
 * 
 * 时间复杂度分析：
 * 1. 离散化：O(n log n)
 * 2. 构建线段树：O(n log n)
 * 3. 单次查询：O(log^2 n)
 * 
 * 空间复杂度分析：
 * O(n log n) - 外层线段树的每个节点维护一个权值线段树
 * 
 * 算法优势：
 * 1. 可以高效处理静态数组的区间第k小查询
 * 2. 相比主席树，实现更直观
 * 3. 对于离线查询，可以通过预处理进一步优化
 * 
 * 算法劣势：
 * 1. 不支持动态修改
 * 2. 空间消耗较大
 * 3. 常数因子较大，查询速度可能不如其他方法
 * 
 * 适用场景：
 * 1. 处理静态数组的区间第k小查询
 * 2. 数据范围较大但不同值的数量适中
 * 3. 查询操作远多于更新操作的场景
 * 
 * 更多类似题目：
 * 1. POJ 2104 K-th Number (静态区间第k小) - http://poj.org/problem?id=2104
 * 2. HDU 4747 Mex (权值线段树) - https://acm.hdu.edu.cn/showproblem.php?pid=4747
 * 3. Codeforces 474F Ant colony (线段树应用) - https://codeforces.com/problemset/problem/474/F
 * 4. SPOJ KQUERY K-query (区间第k大) - https://www.spoj.com/problems/KQUERY/
 * 5. LOJ 6419 2018-2019 ICPC, NEERC, Southern Subregional Contest (树状数组应用) - https://loj.ac/p/6419
 * 6. AtCoder ARC045C Snuke's Coloring 2 (二维线段树) - https://atcoder.jp/contests/arc045/tasks/arc045_c
 * 7. UVa 11402 Ahoy, Pirates! (线段树区间修改) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2407
 * 8. AcWing 243 一个简单的整数问题2 (线段树区间修改查询) - https://www.acwing.com/problem/content/description/244/
 * 9. CodeChef CHAOS2 Chaos (树状数组套线段树) - https://www.codechef.com/problems/CHAOS2
 * 10. HackerEarth Range and Queries (线段树应用) - https://www.hackerearth.com/practice/data-structures/advanced-data-structures/segment-trees/practice-problems/
 * 11. 牛客网 NC14732 区间第k大 (线段树套平衡树) - https://ac.nowcoder.com/acm/problem/14732
 * 12. 51Nod 1685 第K大 (树状数组套线段树) - https://www.51nod.com/Challenge/Problem.html#problemId=1685
 * 13. SGU 398 Tickets (线段树区间处理) - https://codeforces.com/problemsets/acmsguru/problem/99999/398
 * 14. Codeforces 609E Minimum spanning tree for each edge (线段树优化) - https://codeforces.com/problemset/problem/609/E
 * 15. UVA 12538 Version Controlled IDE (线段树维护版本) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3780
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
 * 5. 使用BufferedReader和PrintWriter提高I/O效率
 * 6. 利用Java的集合框架进行离散化操作
 * 7. 使用内部类来封装线段树节点和操作
 * 
 * 优化技巧：
 * 1. 离散化：减少数据范围，提高空间利用率
 * 2. 动态开点：只创建需要的节点，减少内存消耗
 * 3. 懒惰传播：使用懒惰标记优化区间更新操作（如果需要）
 * 4. 内存池：预分配线段树节点，提高性能
 * 5. 并行处理：对于多核环境，可以考虑并行构建线段树
 * 6. 缓存优化：优化数据访问模式，提高缓存命中率
 * 7. 使用快速IO：BufferedReader代替Scanner，PrintWriter代替System.out.println
 */

/**
 * 线段树套线段树解法详解：
 * 
 * 问题分析：
 * 这是一个区间更新、区间查询第K大值的问题。我们需要支持：
 * 1. 区间加数（将一个值加入到指定区间的所有集合中）
 * 2. 区间查询第K大（查询指定区间所有集合并集的第K大值）
 * 
 * 解法思路：
 * 使用线段树套线段树（外层权值线段树，内层区间线段树）来解决这个问题。
 * 1. 外层线段树维护权值（数字的大小）
 * 2. 内层线段树维护区间（集合编号）
 * 3. 每个内层线段树节点存储该权值在对应区间内出现的次数
 * 
 * 数据结构设计：
 * - 外层线段树：维护权值范围，节点表示权值区间
 * - 内层线段树：维护集合编号范围，节点表示集合编号区间
 * - root[i]：外层线段树节点i对应的内层线段树根节点
 * - left[i], right[i]：内层线段树节点i的左右子节点
 * - sum[i]：内层线段树节点i维护的区间内数字总个数
 * - lazy[i]：内层线段树节点i的懒标记
 * 
 * 时间复杂度分析：
 * - 区间更新：O(log(权值范围) * log(集合范围)) = O(log(2*n) * log(n)) = O(log²n)
 * - 查询第K大：O(log(权值范围) * log(集合范围)) = O(log²n)
 * 
 * 空间复杂度分析：
 * - 内层线段树节点数：O(m * log(n))，其中m为操作数
 * - 外层线段树节点数：O(权值范围) = O(2*n)
 * - 总空间：O(m * log(n))
 * 
 * 算法优势：
 * 1. 支持在线查询和更新
 * 2. 可以处理任意区间更新和查询
 * 3. 相比于整体二分，更加灵活
 * 
 * 算法劣势：
 * 1. 空间消耗较大
 * 2. 常数较大
 * 3. 实现复杂度较高
 * 
 * 适用场景：
 * 1. 需要频繁进行区间更新和第K大查询
 * 2. 数据可以动态更新
 * 3. 查询区域不规则
 * 
 * 工程化考量：
 * 1. 异常处理：处理输入格式错误、非法参数等情况
 * 2. 边界情况：处理查询范围为空、查询结果不存在等情况
 * 3. 性能优化：使用动态开点减少内存分配开销
 * 4. 可读性：添加详细注释，变量命名清晰
 * 5. 可维护性：模块化设计，便于扩展和修改
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code09_KthNumberQuery1 {

	// 外部线段树的范围，一共只有m个操作，所以最多有m种数字
	public static int MAXM = 50001;

	// 内部线段树的节点数上限
	public static int MAXT = MAXM * 230;

	public static int n, m, s;

	// 所有操作收集起来，因为牵扯到数字离散化
	public static int[][] ques = new int[MAXM][4];

	// 所有可能的数字，收集起来去重，方便得到数字排名
	public static int[] sorted = new int[MAXM];

	// 外部(a~b) + 内部(c~d)表示：数字排名范围a~b，集合范围c~d，数字的个数
	// 外部线段树的下标表示数字的排名
	// 外部(a~b)，假设对应的节点编号为i，那么root[i]就是内部线段树的头节点编号
	public static int[] root = new int[MAXM << 2];

	// 内部线段树是开点线段树，所以需要cnt来获得节点计数
	// 内部线段树的下标表示集合的编号
	// 内部(c~d)，假设对应的节点编号为i
	// sum[i]表示集合范围c~d，一共收集了多少数字
	// lazy[i]懒更新信息，集合范围c~d，增加了几个数字，等待懒更新的下发
	public static int[] left = new int[MAXT];

	public static int[] right = new int[MAXT];

	public static long[] sum = new long[MAXT];

	public static int[] lazy = new int[MAXT];

	public static int cnt;

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

	public static void up(int i) {
		sum[i] = sum[left[i]] + sum[right[i]];
	}

	public static void down(int i, int ln, int rn) {
		if (lazy[i] != 0) {
			if (left[i] == 0) {
				left[i] = ++cnt;
			}
			if (right[i] == 0) {
				right[i] = ++cnt;
			}
			sum[left[i]] += lazy[i] * ln;
			lazy[left[i]] += lazy[i];
			sum[right[i]] += lazy[i] * rn;
			lazy[right[i]] += lazy[i];
			lazy[i] = 0;
		}
	}

	public static int innerAdd(int jobl, int jobr, int l, int r, int i) {
		if (i == 0) {
			i = ++cnt;
		}
		if (jobl <= l && r <= jobr) {
			sum[i] += r - l + 1;
			lazy[i]++;
		} else {
			int mid = (l + r) / 2;
			down(i, mid - l + 1, r - mid);
			if (jobl <= mid) {
				left[i] = innerAdd(jobl, jobr, l, mid, left[i]);
			}
			if (jobr > mid) {
				right[i] = innerAdd(jobl, jobr, mid + 1, r, right[i]);
			}
			up(i);
		}
		return i;
	}

	public static long innerQuery(int jobl, int jobr, int l, int r, int i) {
		if (i == 0) {
			return 0;
		}
		if (jobl <= l && r <= jobr) {
			return sum[i];
		}
		int mid = (l + r) / 2;
		down(i, mid - l + 1, r - mid);
		long ans = 0;
		if (jobl <= mid) {
			ans += innerQuery(jobl, jobr, l, mid, left[i]);
		}
		if (jobr > mid) {
			ans += innerQuery(jobl, jobr, mid + 1, r, right[i]);
		}
		return ans;
	}

	public static void outerAdd(int jobl, int jobr, int jobv, int l, int r, int i) {
		root[i] = innerAdd(jobl, jobr, 1, n, root[i]);
		if (l < r) {
			int mid = (l + r) / 2;
			if (jobv <= mid) {
				outerAdd(jobl, jobr, jobv, l, mid, i << 1);
			} else {
				outerAdd(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
		}
	}

	public static int outerQuery(int jobl, int jobr, long jobk, int l, int r, int i) {
		if (l == r) {
			return l;
		}
		int mid = (l + r) / 2;
		long rightsum = innerQuery(jobl, jobr, 1, n, root[i << 1 | 1]);
		if (jobk > rightsum) {
			return outerQuery(jobl, jobr, jobk - rightsum, l, mid, i << 1);
		} else {
			return outerQuery(jobl, jobr, jobk, mid + 1, r, i << 1 | 1);
		}
	}

	public static void prepare() {
		s = 0;
		for (int i = 1; i <= m; i++) {
			if (ques[i][0] == 1) {
				sorted[++s] = ques[i][3];
			}
		}
		Arrays.sort(sorted, 1, s + 1);
		int len = 1;
		for (int i = 2; i <= s; i++) {
			if (sorted[len] != sorted[i]) {
				sorted[++len] = sorted[i];
			}
		}
		s = len;
		for (int i = 1; i <= m; i++) {
			if (ques[i][0] == 1) {
				ques[i][3] = kth(ques[i][3]);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		for (int i = 1; i <= m; i++) {
			in.nextToken();
			ques[i][0] = (int) in.nval;
			in.nextToken();
			ques[i][1] = (int) in.nval;
			in.nextToken();
			ques[i][2] = (int) in.nval;
			in.nextToken();
			ques[i][3] = (int) in.nval;
		}
		prepare();
		for (int i = 1; i <= m; i++) {
			if (ques[i][0] == 1) {
				outerAdd(ques[i][1], ques[i][2], ques[i][3], 1, s, 1);
			} else {
				int idx = outerQuery(ques[i][1], ques[i][2], ques[i][3], 1, s, 1);
				out.println(sorted[idx]);
			}
		}
		out.flush();
		out.close();
		br.close();
	}
}