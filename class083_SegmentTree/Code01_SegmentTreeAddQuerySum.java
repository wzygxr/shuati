package class110;

/**
 * 线段树实现 - 支持区间加法和区间求和查询
 * 
 * 该实现使用静态数组方式构建线段树，支持高效的区间修改和区间查询操作。
 * 适用于需要频繁进行区间更新和区间求和的场景，如LeetCode 307. Range Sum Query - Mutable等题目。
 * 
 * 时间复杂度分析：
 * - 建树：O(n)
 * - 区间加法更新：O(log n)
 * - 区间求和查询：O(log n)
 * 
 * 空间复杂度：O(n) - 使用4*MAXN大小的数组存储线段树节点和懒标记
 * 
 * 题目来源：
 * - Luogu P3372. 【模板】线段树 1 - https://www.luogu.com.cn/problem/P3372
 * - LeetCode 307. Range Sum Query - Mutable - https://leetcode.cn/problems/range-sum-query-mutable/
 * - HDU 1166. 敌兵布阵 - http://acm.hdu.edu.cn/showproblem.php?pid=1166
 * - HDU 1754. I Hate It - http://acm.hdu.edu.cn/showproblem.php?pid=1754
 * - Codeforces 339D. Xor - https://codeforces.com/problemset/problem/339/D
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_SegmentTreeAddQuerySum {

	/**
	 * 线段树数组最大长度，根据题目要求设置
	 * 实际应用中应根据数据规模调整，避免内存溢出或浪费
	 */
	public static int MAXN = 100001;

	/**
	 * 原始数组（1-based索引）
	 * 使用long类型避免整数溢出问题
	 */
	public static long[] arr = new long[MAXN];

	/**
	 * 线段树数组，存储每个区间的和
	 * 大小为4*MAXN，确保足够存储所有节点
	 */
	public static long[] sum = new long[MAXN << 2];

	/**
	 * 懒标记数组，记录待传递的区间加法操作
	 * 与sum数组对应，每个节点有一个对应的懒标记
	 */
	public static long[] add = new long[MAXN << 2];

	/**
	 * 向上合并子节点的信息到父节点
	 * 时间复杂度：O(1)
	 * @param i 当前节点索引
	 * 工程化考虑：此方法将左右子节点的区间和合并到父节点，确保父节点的值始终正确
	 */
	public static void up(int i) {
		// 父范围的累加和 = 左范围累加和 + 右范围累加和
		// 左子节点索引：i << 1 = 2*i
		// 右子节点索引：i << 1 | 1 = 2*i + 1
		sum[i] = sum[i << 1] + sum[i << 1 | 1];
	}

	/**
	 * 向下传递懒标记（核心操作）
	 * 时间复杂度：O(1)
	 * @param i 当前节点索引
	 * @param ln 左子树的节点数
	 * @param rn 右子树的节点数
	 * 懒标记原理：当需要访问子节点时，才将父节点的未处理更新操作传递下去
	 * 这样避免了不必要的递归操作，大大提高了区间更新的效率
	 */
	public static void down(int i, int ln, int rn) {
		// 只有当存在未处理的懒标记时才需要传递
		if (add[i] != 0) {
			// 处理左子树
			lazy(i << 1, add[i], ln);
			// 处理右子树
			lazy(i << 1 | 1, add[i], rn);
			// 清除当前节点的懒标记，表示更新操作已传递
			add[i] = 0;
		}
	}

	/**
	 * 处理懒标记更新
	 * 时间复杂度：O(1)
	 * @param i 当前节点索引
	 * @param v 要增加的值
	 * @param n 当前区间的节点数
	 * 实现细节：
	 * 1. 更新当前节点的区间和：每个元素增加v，总共n个元素
	 * 2. 记录懒标记：保存待传递的更新操作
	 * 注意：只有非叶子节点的懒标记才有意义，叶子节点不需要懒标记
	 */
	public static void lazy(int i, long v, int n) {
		// 更新区间和：v乘以节点数
		sum[i] += v * n;
		// 记录懒标记
		add[i] += v;
	}

	/**
	 * 构建线段树
	 * 时间复杂度：O(n)
	 * @param l 当前区间左边界（1-based）
	 * @param r 当前区间右边界（1-based）
	 * @param i 当前节点索引
	 * 递归构建线段树的过程：
	 * 1. 基本情况：区间长度为1（叶子节点），直接赋值
	 * 2. 递归构建左右子树
	 * 3. 合并子节点信息到当前节点
	 * 4. 初始化懒标记为0
	 */
	public static void build(int l, int r, int i) {
		// 到达叶子节点
		if (l == r) {
			// 叶子节点的值等于原始数组对应位置的值
			sum[i] = arr[l];
		} else {
			// 计算区间中点，将区间分为左右两部分
			int mid = (l + r) >> 1; // 等价于 (l + r) / 2，但使用位运算效率更高
			// 递归构建左子树
			build(l, mid, i << 1);
			// 递归构建右子树
			build(mid + 1, r, i << 1 | 1);
			// 合并左右子树的信息到当前节点
			up(i);
		}
		// 初始化懒标记为0
		add[i] = 0;
	}

	/**
	 * 区间加法更新操作
	 * 时间复杂度：O(log n)
	 * @param jobl 更新区间左边界（1-based）
	 * @param jobr 更新区间右边界（1-based）
	 * @param jobv 要增加的值
	 * @param l 当前节点表示的区间左边界
	 * @param r 当前节点表示的区间右边界
	 * @param i 当前节点索引
	 * 区间更新策略：
	 * 1. 当前区间完全包含在目标区间内：使用懒标记延迟更新
	 * 2. 部分重叠：先下发懒标记，再递归处理左右子树
	 */
	public static void add(int jobl, int jobr, long jobv, int l, int r, int i) {
		// 情况1：当前区间完全包含在目标更新区间内
		if (jobl <= l && r <= jobr) {
			// 使用懒标记进行延迟更新，不再递归到子节点
			lazy(i, jobv, r - l + 1);
		} else {
			// 情况2：当前区间与目标更新区间部分重叠
			// 先计算中点
			int mid = (l + r) >> 1;
			// 下发懒标记，确保子节点的数据正确性
			down(i, mid - l + 1, r - mid);
			// 递归处理左子树（如果左子树区间与目标区间有交集）
			if (jobl <= mid) {
				add(jobl, jobr, jobv, l, mid, i << 1);
			}
			// 递归处理右子树（如果右子树区间与目标区间有交集）
			if (jobr > mid) {
				add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
			// 更新完成后，合并子节点信息
			up(i);
		}
	}

	/**
	 * 区间求和查询操作
	 * 时间复杂度：O(log n)
	 * @param jobl 查询区间左边界（1-based）
	 * @param jobr 查询区间右边界（1-based）
	 * @param l 当前节点表示的区间左边界
	 * @param r 当前节点表示的区间右边界
	 * @param i 当前节点索引
	 * @return 查询区间的和
	 * 区间查询策略：
	 * 1. 当前区间完全包含在目标查询区间内：直接返回当前节点的值
	 * 2. 部分重叠：先下发懒标记，再递归查询左右子树并累加结果
	 */
	public static long query(int jobl, int jobr, int l, int r, int i) {
		// 情况1：当前区间完全包含在目标查询区间内
		if (jobl <= l && r <= jobr) {
			// 直接返回当前节点存储的区间和
			return sum[i];
		}
		// 情况2：当前区间与目标查询区间部分重叠
		// 计算中点
		int mid = (l + r) >> 1;
		// 下发懒标记，确保子节点的数据是最新的
		down(i, mid - l + 1, r - mid);
		// 初始化结果
		long ans = 0;
		// 递归查询左子树（如果左子树区间与查询区间有交集）
		if (jobl <= mid) {
			ans += query(jobl, jobr, l, mid, i << 1);
		}
		// 递归查询右子树（如果右子树区间与查询区间有交集）
		if (jobr > mid) {
			ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
		}
		return ans;
	}

	/**
	 * 主方法 - 用于处理标准输入输出，解决Luogu P3372题目
	 * 该方法演示了如何在实际编程竞赛中使用线段树解决区间更新和区间查询问题
	 * 
	 * 输入格式：
	 * 第一行：n m (数组长度，操作次数)
	 * 第二行：n个整数 (原始数组)
	 * 接下来m行：操作类型 操作参数
	 *   - 类型1：1 l r v (将区间[l,r]中的每个数加上v)
	 *   - 类型2：2 l r (查询区间[l,r]的和)
	 */
	public static void main(String[] args) throws IOException {
		// 使用高效的输入方式，避免超时
		// 在编程竞赛中，BufferedReader和StreamTokenizer比Scanner更快
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取数组长度n和操作次数m
		in.nextToken(); int n = (int) in.nval;
		in.nextToken(); int m = (int) in.nval;
		
		// 读取原始数组（1-based索引）
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (long) in.nval;
		}
		
		// 构建线段树
		build(1, n, 1);
		
		// 处理m个操作
		long jobv;
		for (int i = 1, op, jobl, jobr; i <= m; i++) {
			in.nextToken();
			op = (int) in.nval; // 操作类型
			
			if (op == 1) {
				// 类型1：区间加法更新
				in.nextToken(); jobl = (int) in.nval;
				in.nextToken(); jobr = (int) in.nval;
				in.nextToken(); jobv = (long) in.nval;
				add(jobl, jobr, jobv, 1, n, 1);
			} else {
				// 类型2：区间求和查询
				in.nextToken(); jobl = (int) in.nval;
				in.nextToken(); jobr = (int) in.nval;
				// 输出查询结果
				out.println(query(jobl, jobr, 1, n, 1));
			}
		}
		
		// 刷新输出流，确保所有结果都被输出
		out.flush();
		// 关闭资源
		out.close();
		br.close();
	}

	/**
	 * 线段树扩展知识和工程化考虑
	 * 
	 * 1. 数据类型选择：
	 *    - 使用long类型避免大整数溢出问题，特别是在区间求和场景
	 *    - 对于不同的问题，可以根据数据范围选择合适的数据类型
	 * 
	 * 2. 索引处理：
	 *    - 采用1-based索引简化线段树的实现，避免处理0的边界情况
	 *    - 在实际应用中，需要注意输入输出与内部表示的索引转换
	 * 
	 * 3. 性能优化：
	 *    - 使用位运算代替乘除法（如 >> 1 代替 / 2）
	 *    - 高效的输入输出方式（BufferedReader + StreamTokenizer + PrintWriter）
	 *    - 懒标记技术避免不必要的递归操作
	 * 
	 * 4. 错误处理：
	 *    - 在工程应用中，应添加参数验证，避免无效的区间操作
	 *    - 考虑内存限制，根据实际数据规模调整MAXN的值
	 * 
	 * 5. 线段树的变体：
	 *    - 区间最大值/最小值线段树
	 *    - 区间异或线段树
	 *    - 区间赋值线段树（需要处理懒标记的覆盖问题）
	 *    - 二维线段树（处理二维区间查询）
	 * 
	 * 6. 线段树与其他数据结构的对比：
	 *    - 树状数组：实现更简单，常数更小，但功能相对有限
	 *    - 稀疏表：查询更快（O(1)），但不支持更新操作
	 *    - ST表：适用于静态数组的区间查询，预处理O(n log n)，查询O(1)
	 * 
	 * 7. 工程应用场景：
	 *    - 金融数据分析中的区间统计
	 *    - 图像处理中的区域操作
	 *    - 分布式系统中的范围查询
	 *    - 游戏开发中的区域效果计算
	 */

}
