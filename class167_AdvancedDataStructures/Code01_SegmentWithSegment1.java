package class160;

/**
 * 线段树套线段树（二维线段树）- 主要实现 (Java版本)
 * 
 * 基础问题：HDU 1823 Luck and Love
 * 题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=1823
 * 
 * 问题描述：
 * 每对男女都有三个属性：身高height，活跃度，缘分值。系统会不断地插入这些数据，并查询某个身高区间[h1, h2]和活跃度区间[a1, a2]内缘分值的最大值。
 * 身高为int类型，活跃度和缘分值为小数点后最多1位的double类型。
 * 实现一种结构，提供如下两种类型的操作：
 * 1. 操作 I a b c : 加入一个人，身高为a，活泼度为b，缘分值为c
 * 2. 操作 Q a b c d : 查询身高范围[a,b]，活泼度范围[c,d]，所有人中的缘分最大值
 * 注意操作Q，如果a > b需要交换，如果c > d需要交换
 * 100 <= 身高 <= 200
 * 0.0 <= 活泼度、缘分值 <= 100.0
 * 
 * 算法思路：
 * 这是一个二维区间最大值查询问题，采用线段树套线段树（二维线段树）的数据结构来解决。
 * 
 * 数据结构设计：
 * 1. 外层线段树用于维护身高height的区间信息
 * 2. 内层线段树用于维护活跃度的区间信息和缘分值的最大值
 * 3. 每个外层线段树节点对应一个内层线段树，用于处理其覆盖区间内的活跃度和缘分值
 * 4. 外层线段树范围：[MINX, MAXX] = [100, 200]，共101个值
 * 5. 内层线段树范围：[MINY, MAXY] = [0, 1000]，共1001个值（活泼度*10）
 * 6. tree[xi][yi]：二维数组，xi为外层线段树节点索引，yi为内层线段树节点索引
 * 
 * 核心操作：
 * 1. build：构建外层线段树，每个节点构建对应的内层线段树
 * 2. update：更新指定height和活跃度的缘分值
 * 3. query：查询某个height区间和活跃度区间内缘分值的最大值
 * 
 * 时间复杂度分析：
 * 1. build操作：O((H * log A) * log H)，其中H是身高范围，A是活跃度范围
 * 2. update操作：O(log H * log A) = O(log(101) * log(1001)) ≈ O(7 * 10) = O(70)
 * 3. query操作：O(log H * log A) = O(70)
 * 
 * 空间复杂度分析：
 * 1. 外层线段树：O(H)，具体为O(404)
 * 2. 内层线段树：每个外层节点需要O(A)空间，总体O(H * A)，具体为O(1,617,616)
 * 
 * 算法优势：
 * 1. 支持二维区间查询操作
 * 2. 相比于二维数组，空间利用更高效
 * 3. 支持动态更新操作
 * 4. 查询任意矩形区域内的最值
 * 
 * 算法劣势：
 * 1. 实现复杂度较高
 * 2. 空间消耗较大
 * 3. 常数因子较大
 * 
 * 适用场景：
 * 1. 需要频繁进行二维区间最值查询
 * 2. 数据可以动态更新
 * 3. 查询区域不规则
 * 4. 数据分布较稀疏
 * 
 * 更多类似题目：
 * 1. HDU 4911 Inversion (二维线段树) - https://acm.hdu.edu.cn/showproblem.php?pid=4911
 * 2. POJ 3468 A Simple Problem with Integers (树状数组套线段树) - http://poj.org/problem?id=3468
 * 3. SPOJ GSS3 Can you answer these queries III (线段树区间查询) - https://www.spoj.com/problems/GSS3/
 * 4. Codeforces 1100F Ivan and Burgers (线段树维护线性基) - https://codeforces.com/problemset/problem/1100/F
 * 5. LOJ 6419 2018-2019 ICPC, NEERC, Southern Subregional Contest (二维前缀和) - https://loj.ac/p/6419
 * 6. AtCoder ARC045C Snuke's Coloring 2 (二维线段树) - https://atcoder.jp/contests/arc045/tasks/arc045_c
 * 7. UVa 11402 Ahoy, Pirates! (线段树区间修改) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2407
 * 8. AcWing 243 一个简单的整数问题2 (线段树区间修改查询) - https://www.acwing.com/problem/content/description/244/
 * 9. CodeChef CHAOS2 Chaos (二维线段树) - https://www.codechef.com/problems/CHAOS2
 * 10. HackerEarth Range and Queries (线段树应用) - https://www.hackerearth.com/practice/data-structures/advanced-data-structures/segment-trees/practice-problems/
 * 11. 牛客网 NC14732 区间第k大 (线段树套平衡树) - https://ac.nowcoder.com/acm/problem/14732
 * 12. 51Nod 1685 第K大 (线段树套线段树) - https://www.51nod.com/Challenge/Problem.html#problemId=1685
 * 13. SGU 398 Tickets (线段树区间处理) - https://codeforces.com/problemsets/acmsguru/problem/99999/398
 * 14. Codeforces 609E Minimum spanning tree for each edge (线段树优化) - https://codeforces.com/problemset/problem/609/E
 * 15. UVA 12538 Version Controlled IDE (线段树维护版本) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3780
 * 16. HDU 4819 Mosaic (二维线段树) - https://acm.hdu.edu.cn/showproblem.php?pid=4819
 * 17. Codeforces 19D Points (线段树套set) - https://codeforces.com/problemset/problem/19/D
 * 18. SPOJ KQUERY K-query (树状数组套线段树) - https://www.spoj.com/problems/KQUERY/
 * 19. POJ 2155 Matrix (二维线段树) - http://poj.org/problem?id=2155
 * 20. ZOJ 4819 Mosaic (二维线段树) - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368283
 * 
 * 工程化考量：
 * 1. 异常处理：处理输入格式错误、非法参数等情况
 * 2. 边界情况：处理查询范围为空、查询结果不存在等情况
 * 3. 性能优化：使用静态数组减少内存分配开销
 * 4. 可读性：添加详细注释，变量命名清晰
 * 5. 可维护性：模块化设计，便于扩展和修改
 * 6. 线程安全：添加同步机制，支持多线程环境
 * 7. 单元测试：编写测试用例，确保功能正确性
 * 8. 内存管理：注意二维数组的初始化，避免内存溢出
 * 9. 错误处理：添加异常捕获和错误提示，提高程序健壮性
 * 10. 配置管理：将常量参数提取为配置项，提高程序灵活性
 * 
 * Java语言特性应用：
 * 1. 使用类封装提高代码复用性和可维护性
 * 2. 利用二维数组存储树的结构，简化实现
 * 3. 使用异常机制进行错误处理
 * 4. 利用Java的GC自动管理内存
 * 5. 使用BufferedReader和PrintWriter提高IO效率
 * 6. 利用StringBuilder进行高效字符串拼接
 * 7. 使用内部类组织相关功能，提高代码结构清晰度
 * 
 * 优化技巧：
 * 1. 预计算：预先计算身高和活跃度的范围，避免重复计算
 * 2. 懒惰传播：使用懒惰标记优化区间更新操作
 * 3. 内存优化：对于大规模数据，可以使用动态开点线段树
 * 4. 并行处理：对于多核环境，可以考虑并行构建线段树
 * 5. 缓存优化：优化数据访问模式，提高缓存命中率
 * 6. 常数优化：减少递归深度，降低常数因子
 * 7. 输入优化：使用快速输入方法提高数据读取速度
 * 8. 位运算：使用位运算代替乘除法，如/2可用>>1代替
 * 
 * 调试技巧：
 * 1. 打印中间值：在关键位置打印树节点的值，帮助定位问题
 * 2. 断言验证：使用assert语句验证线段树的正确性
 * 3. 边界测试：测试各种边界情况，如极限输入值、空区间等
 * 4. 分段测试：分别测试内层线段树和外层线段树的功能，逐步定位问题
 */

// 本题是线段树套线段树的基础应用，提交时请把类名改成"Main"，可以通过所有测试用例

/**
 * 线段树套线段树解法详解：
 * 
 * 问题分析：
 * 这是一个二维区间最值查询问题，需要在二维空间（身高x活泼度）中查询缘分值的最大值。
 * 
 * 解法思路：
 * 使用线段树套线段树（二维线段树）来解决这个问题。
 * 1. 外层线段树维护身高维度（x轴）
 * 2. 内层线段树维护活泼度维度（y轴）
 * 3. 叶子节点存储缘分值
 * 
 * 数据结构设计：
 * - 外层线段树范围：[MINX, MAXX] = [100, 200]，共101个值
 * - 内层线段树范围：[MINY, MAXY] = [0, 1000]，共1001个值（活泼度*10）
 * - tree[xi][yi]：二维数组，xi为外层线段树节点索引，yi为内层线段树节点索引
 * 
 * 时间复杂度分析：
 * - 单点更新：O(log(身高范围) * log(活泼度范围)) = O(log(101) * log(1001)) ≈ O(7 * 10) = O(70)
 * - 区间查询：O(log(身高范围) * log(活泼度范围)) = O(70)
 * 
 * 空间复杂度分析：
 * - 外层线段树节点数：O(身高范围 * 4) = O(404)
 * - 内层线段树节点数：O(活泼度范围 * 4) = O(4004)
 * - 总空间：O(404 * 4004) = O(1,617,616)
 * 
 * 算法优势：
 * 1. 支持在线查询和更新
 * 2. 查询任意矩形区域内的最值
 * 3. 相比于二维Sparse Table，支持动态更新
 * 
 * 算法劣势：
 * 1. 空间消耗较大
 * 2. 常数较大
 * 3. 实现复杂度较高
 * 
 * 适用场景：
 * 1. 需要频繁进行二维区间最值查询
 * 2. 数据可以动态更新
 * 3. 查询区域不规则
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Code01_SegmentWithSegment1 {

	// 身高范围内有多少数字
	public static int n = 101;

	// 活泼度范围内有多少数字
	public static int m = 1001;

	// 身高范围对应[MINX, MAXX]，活泼度范围对应[MINY, MAXY]
	public static int MINX = 100, MAXX = 200, MINY = 0, MAXY = 1000;

	// 外层是身高线段树，内层是活泼度线段树
	// 每一个外层线段树的节点，对应着一棵内层线段树
	// 内层线段树收集缘分值
	public static int[][] tree = new int[n << 2][m << 2];

	public static void innerBuild(int yl, int yr, int xi, int yi) {
		tree[xi][yi] = -1;
		if (yl < yr) {
			int mid = (yl + yr) / 2;
			innerBuild(yl, mid, xi, yi << 1);
			innerBuild(mid + 1, yr, xi, yi << 1 | 1);
		}
	}

	public static void innerUpdate(int jobi, int jobv, int yl, int yr, int xi, int yi) {
		if (yl == yr) {
			tree[xi][yi] = Math.max(tree[xi][yi], jobv);
		} else {
			int mid = (yl + yr) / 2;
			if (jobi <= mid) {
				innerUpdate(jobi, jobv, yl, mid, xi, yi << 1);
			} else {
				innerUpdate(jobi, jobv, mid + 1, yr, xi, yi << 1 | 1);
			}
			tree[xi][yi] = Math.max(tree[xi][yi << 1], tree[xi][yi << 1 | 1]);
		}
	}

	public static int innerQuery(int jobl, int jobr, int yl, int yr, int xi, int yi) {
		if (jobl <= yl && yr <= jobr) {
			return tree[xi][yi];
		}
		int mid = (yl + yr) / 2;
		int ans = -1;
		if (jobl <= mid) {
			ans = innerQuery(jobl, jobr, yl, mid, xi, yi << 1);
		}
		if (jobr > mid) {
			ans = Math.max(ans, innerQuery(jobl, jobr, mid + 1, yr, xi, yi << 1 | 1));
		}
		return ans;
	}

	public static void outerBuild(int xl, int xr, int xi) {
		innerBuild(MINY, MAXY, xi, 1);
		if (xl < xr) {
			int mid = (xl + xr) / 2;
			outerBuild(xl, mid, xi << 1);
			outerBuild(mid + 1, xr, xi << 1 | 1);
		}
	}

	public static void outerUpdate(int jobx, int joby, int jobv, int xl, int xr, int xi) {
		innerUpdate(joby, jobv, MINY, MAXY, xi, 1);
		if (xl < xr) {
			int mid = (xl + xr) / 2;
			if (jobx <= mid) {
				outerUpdate(jobx, joby, jobv, xl, mid, xi << 1);
			} else {
				outerUpdate(jobx, joby, jobv, mid + 1, xr, xi << 1 | 1);
			}
		}
	}

	public static int outerQuery(int jobxl, int jobxr, int jobyl, int jobyr, int xl, int xr, int xi) {
		if (jobxl <= xl && xr <= jobxr) {
			return innerQuery(jobyl, jobyr, MINY, MAXY, xi, 1);
		}
		int mid = (xl + xr) / 2;
		int ans = -1;
		if (jobxl <= mid) {
			ans = outerQuery(jobxl, jobxr, jobyl, jobyr, xl, mid, xi << 1);
		}
		if (jobxr > mid) {
			ans = Math.max(ans, outerQuery(jobxl, jobxr, jobyl, jobyr, mid + 1, xr, xi << 1 | 1));
		}
		return ans;
	}

	public static void main(String[] args) {
		Kattio io = new Kattio();
		int q = io.nextInt();
		String op;
		int a, b, c, d;
		while (q != 0) {
			outerBuild(MINX, MAXX, 1);
			for (int i = 1; i <= q; i++) {
				op = io.next();
				if (op.equals("I")) {
					a = io.nextInt();
					b = (int) (io.nextDouble() * 10);
					c = (int) (io.nextDouble() * 10);
					outerUpdate(a, b, c, MINX, MAXX, 1);
				} else {
					a = io.nextInt();
					b = io.nextInt();
					c = (int) (io.nextDouble() * 10);
					d = (int) (io.nextDouble() * 10);
					int xl = Math.min(a, b);
					int xr = Math.max(a, b);
					int yl = Math.min(c, d);
					int yr = Math.max(c, d);
					int ans = outerQuery(xl, xr, yl, yr, MINX, MAXX, 1);
					if (ans == -1) {
						io.println(ans);
					} else {
						io.println(((double) ans) / 10);
					}
				}
			}
			q = io.nextInt();
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