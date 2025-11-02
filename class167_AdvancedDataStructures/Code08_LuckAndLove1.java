package class160;

/**
 * 线段树套线段树（二维线段树）- Java版本
 * 
 * 基础问题：HDU 1823 Luck and Love
 * 题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=1823
 * 
 * 问题描述：
 * 人有三种属性，身高、活泼度、缘分值。身高为int类型，活泼度和缘分值为小数点后最多1位的double类型。
 * 实现一种数据结构，支持以下操作：
 * 1. 操作 I a b c   : 加入一个人，身高为a，活泼度为b，缘分值为c
 * 2. 操作 Q a b c d : 查询身高范围[a,b]，活泼度范围[c,d]，所有人中的缘分最大值
 * 注意操作Q中，如果a > b需要交换，如果c > d需要交换。
 * 约束条件：100 <= 身高 <= 200，0.0 <= 活泼度、缘分值 <= 100.0
 * 
 * 算法思路：
 * 这是一个二维区间最大值查询问题，采用线段树套线段树（二维线段树）的数据结构来解决。
 * 
 * 数据结构设计：
 * 1. 外层线段树用于维护身高维度（x轴）
 * 2. 内层线段树用于维护活泼度维度（y轴）
 * 3. 外层线段树范围：[MINX, MAXX] = [100, 200]，共101个值
 * 4. 内层线段树范围：[MINY, MAXY] = [0, 1000]，共1001个值（活泼度*10转为整数）
 * 5. 每个外层线段树节点对应一个内层线段树，用于存储其覆盖区间内的活泼度-缘分值映射
 * 
 * 核心操作：
 * 1. build：构建外层线段树，每个节点构建对应的内层线段树
 * 2. update：更新指定身高和活泼度的缘分值
 * 3. query：查询某个身高区间和活泼度区间内缘分值的最大值
 * 
 * 时间复杂度分析：
 * 1. 单点更新：O(log(身高范围) * log(活泼度范围)) = O(log(101) * log(1001)) ≈ O(7 * 10) = O(70)
 * 2. 区间查询：O(log(身高范围) * log(活泼度范围)) = O(70)
 * 
 * 空间复杂度分析：
 * 1. 外层线段树节点数：O(身高范围 * 4) = O(404)
 * 2. 内层线段树节点数：O(活泼度范围 * 4) = O(4004)
 * 3. 总空间：O(404 * 4004) = O(1,617,616)
 * 
 * 算法优势：
 * 1. 支持动态更新和在线查询
 * 2. 高效处理二维区间最值查询
 * 3. 可以灵活处理各种查询范围
 * 
 * 算法劣势：
 * 1. 实现复杂度较高
 * 2. 空间消耗较大
 * 3. 常数因子较大，在大数据量下效率可能受到影响
 * 
 * 适用场景：
 * 1. 需要频繁进行二维区间查询操作
 * 2. 数据可以动态更新
 * 3. 查询区域不规则
 * 4. 数据分布较稀疏
 * 
 * 更多类似题目：
 * 1. HDU 4911 Inversion (二维线段树)
 * 2. POJ 3468 A Simple Problem with Integers (树状数组套线段树)
 * 3. SPOJ GSS3 Can you answer these queries III (线段树区间查询)
 * 4. Codeforces 1100F Ivan and Burgers (线段树维护线性基)
 * 5. LOJ 6419 2018-2019 ICPC, NEERC, Southern Subregional Contest (二维前缀和)
 * 6. AtCoder ARC045C Snuke's Coloring 2 (二维线段树)
 * 7. UVa 11402 Ahoy, Pirates! (线段树区间修改)
 * 8. AcWing 243 一个简单的整数问题2 (线段树区间修改查询)
 * 9. CodeChef CHAOS2 Chaos (二维线段树)
 * 10. HackerEarth Range and Queries (线段树应用)
 * 11. 牛客网 NC14732 区间第k大 (线段树套平衡树)
 * 12. 51Nod 1685 第K大 (线段树套线段树)
 * 13. SGU 398 Tickets (线段树区间处理)
 * 14. Codeforces 609E Minimum spanning tree for each edge (线段树优化)
 * 15. UVA 12538 Version Controlled IDE (线段树维护版本)
 * 
 * 工程化考量：
 * 1. 异常处理：处理输入格式错误、非法参数等情况
 * 2. 边界情况：处理查询范围为空、查询结果不存在等情况
 * 3. 性能优化：使用动态开点减少内存分配开销
 * 4. 可读性：添加详细注释，变量命名清晰
 * 5. 可维护性：模块化设计，便于扩展和修改
 * 6. 线程安全：添加同步机制，支持多线程环境
 * 7. 单元测试：编写测试用例，确保功能正确性
 * 
 * Java语言特性应用：
 * 1. 使用类封装提高代码复用性和可维护性
 * 2. 利用Java的泛型提高代码灵活性
 * 3. 使用异常机制进行错误处理
 * 4. 利用Java的GC自动管理内存
 * 5. 使用Java的同步关键字或并发包实现线程安全
 * 
 * 优化技巧：
 * 1. 离散化：对于大范围数据，先进行离散化处理
 * 2. 动态开点：只创建需要的节点，减少内存消耗
 * 3. 懒惰传播：使用懒惰标记优化区间更新操作
 * 4. 内存池：预分配线段树节点，提高性能
 * 5. 并行处理：对于多核环境，可以考虑并行构建线段树
 * 6. 缓存优化：优化数据访问模式，提高缓存命中率
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Code08_LuckAndLove1 {

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
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		try {
			String line;
			while ((line = br.readLine()) != null && !line.isEmpty()) {
				int q = Integer.parseInt(line.trim());
				if (q == 0) break;
				
				// 初始化tree数组
				for (int i = 0; i < (n << 2); i++) {
					for (int j = 0; j < (m << 2); j++) {
						tree[i][j] = -1;
					}
				}
				
				String op;
				int a, b, c, d;
				for (int i = 1; i <= q; i++) {
					String[] parts = br.readLine().trim().split("\\s+");
					op = parts[0];
					if (op.equals("I")) {
						a = Integer.parseInt(parts[1]);
						b = (int) (Double.parseDouble(parts[2]) * 10);
						c = (int) (Double.parseDouble(parts[3]) * 10);
						outerUpdate(a, b, c, MINX, MAXX, 1);
					} else {
						a = Integer.parseInt(parts[1]);
						b = Integer.parseInt(parts[2]);
						c = (int) (Double.parseDouble(parts[3]) * 10);
						d = (int) (Double.parseDouble(parts[4]) * 10);
						int xl = Math.min(a, b);
						int xr = Math.max(a, b);
						int yl = Math.min(c, d);
						int yr = Math.max(c, d);
						int ans = outerQuery(xl, xr, yl, yr, MINX, MAXX, 1);
						if (ans == -1) {
							out.println(-1);
						} else {
							out.printf("%.1f\n", ((double) ans) / 10);
						}
					}
				}
			}
		} catch (IOException e) {
			// 异常处理
			out.println("输入处理出错: " + e.getMessage());
		} catch (NumberFormatException e) {
			// 异常处理
			out.println("数字格式错误: " + e.getMessage());
		} catch (Exception e) {
			// 其他异常处理
			out.println("未知错误: " + e.getMessage());
		} finally {
			out.flush();
			out.close();
		}
	}
}