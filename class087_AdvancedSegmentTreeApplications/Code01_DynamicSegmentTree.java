package class114;

/**
 * 洛谷 P2781 域名分析
 * 
 * 题目描述：
 * 一共有n个位置，编号从1~n，一开始所有位置的值为0
 * 实现如下两个操作，一共会调用m次
 * 操作 1 l r v : 把l~r范围的每个数增加v
 * 操作 2 l r   : 返回l~r范围的累加和
 * 
 * 解题思路：
 * 使用动态开点线段树处理大数据范围的区间更新和查询问题。
 * 由于n的范围可达10^9，不能预先建立完整的线段树，需要按需创建节点。
 * 
 * 关键技术：
 * 1. 动态开点：只在需要时创建节点，节省空间
 * 2. 懒惰标记：延迟更新子区间，提高效率
 * 3. 静态数组实现：避免频繁内存分配
 * 
 * 时间复杂度分析：
 * 1. 建树：O(1) - 按需创建
 * 2. 区间更新：O(log n)
 * 3. 区间查询：O(log n)
 * 4. 空间复杂度：O(m log n)，其中m是操作次数
 * 
 * 是否最优解：是
 * 动态开点线段树是处理大数据范围区间操作问题的最优解法
 * 
 * 工程化考量：
 * 1. 输入输出优化：使用StreamTokenizer和PrintWriter提高效率
 * 2. 内存管理：静态数组避免频繁内存分配
 * 3. 边界处理：处理节点创建和查询边界情况
 * 4. 异常处理：处理输入异常和数组越界
 * 
 * 题目链接：https://www.luogu.com.cn/problem/P2781
 * 
 * @author Algorithm Journey
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code01_DynamicSegmentTree {

	// 静态数组大小，根据题目约束计算得出
	// 范围1 ~ 10^9，线段树高度差不多30
	// 查询次数1000，每次查询都有左右两条边线
	// 所以空间占用差不多1000 * 30 * 2 = 60000
	// 适当调大以保证安全
	public static int LIMIT = 80001;

	// 当前使用的节点数
	public static int cnt;

	// 左子节点数组
	public static int[] left = new int[LIMIT];

	// 右子节点数组
	public static int[] right = new int[LIMIT];

	// 区间和数组
	public static long[] sum = new long[LIMIT];

	// 懒惰标记数组（区间加法标记）
	public static long[] add = new long[LIMIT];

	/**
	 * 向上更新节点信息
	 * 将左右子节点的信息合并到父节点
	 * 
	 * @param h 父节点索引
	 * @param l 左子节点索引
	 * @param r 右子节点索引
	 */
	public static void up(int h, int l, int r) {
		sum[h] = sum[l] + sum[r];
	}

	/**
	 * 向下传递懒惰标记
	 * 在访问子节点前，将当前节点的懒惰标记传递给子节点
	 * 
	 * @param i  当前节点索引
	 * @param ln 左子树节点数
	 * @param rn 右子树节点数
	 */
	public static void down(int i, int ln, int rn) {
		if (add[i] != 0) {
			// 懒更新任务下发
			// 那左右两侧的空间需要准备好
			if (left[i] == 0) {
				left[i] = ++cnt;
			}
			if (right[i] == 0) {
				right[i] = ++cnt;
			}
			lazy(left[i], add[i], ln);
			lazy(right[i], add[i], rn);
			add[i] = 0;
		}
	}

	/**
	 * 懒惰标记应用
	 * 将懒惰标记应用到指定节点
	 * 
	 * @param i 节点索引
	 * @param v 懒惰标记值
	 * @param n 节点表示的区间长度
	 */
	public static void lazy(int i, long v, int n) {
		sum[i] += v * n;
		add[i] += v;
	}

	/**
	 * 区间加法操作
	 * 将区间[jobl, jobr]内的所有元素增加jobv
	 * 
	 * @param jobl 操作区间左端点
	 * @param jobr 操作区间右端点
	 * @param jobv 增加的值
	 * @param l    当前节点表示的区间左端点
	 * @param r    当前节点表示的区间右端点
	 * @param i    当前节点索引
	 */
	public static void add(int jobl, int jobr, long jobv, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			lazy(i, jobv, r - l + 1);
		} else {
			int mid = (l + r) >> 1;
			down(i, mid - l + 1, r - mid);
			if (jobl <= mid) {
				// 不得不去左侧才会申请
				if (left[i] == 0) {
					left[i] = ++cnt;
				}
				add(jobl, jobr, jobv, l, mid, left[i]);
			}
			if (jobr > mid) {
				// 不得不去右侧才会申请
				if (right[i] == 0) {
					right[i] = ++cnt;
				}
				add(jobl, jobr, jobv, mid + 1, r, right[i]);
			}
			up(i, left[i], right[i]);
		}
	}

	/**
	 * 区间查询操作
	 * 查询区间[jobl, jobr]内所有元素的和
	 * 
	 * @param jobl 查询区间左端点
	 * @param jobr 查询区间右端点
	 * @param l    当前节点表示的区间左端点
	 * @param r    当前节点表示的区间右端点
	 * @param i    当前节点索引
	 * @return 区间和
	 */
	public static long query(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			return sum[i];
		}
		int mid = (l + r) >> 1;
		down(i, mid - l + 1, r - mid);
		long ans = 0;
		if (jobl <= mid) {
			// 发现左侧申请过空间才有必要去查询
			// 如果左侧从来没有申请过空间那查询结果就是0
			if (left[i] != 0) {
				ans += query(jobl, jobr, l, mid, left[i]);
			}
		}
		if (jobr > mid) {
			// 发现右侧申请过空间才有必要去查询
			// 如果右侧从来没有申请过空间那查询结果就是0
			if (right[i] != 0) {
				ans += query(jobl, jobr, mid + 1, r, right[i]);
			}
		}
		return ans;
	}

	/**
	 * 清空线段树
	 * 如果一次会执行多组测试数组，那么每组测试完成后要clear空间
	 */
	public static void clear() {
		Arrays.fill(left, 1, cnt + 1, 0);
		Arrays.fill(right, 1, cnt + 1, 0);
		Arrays.fill(sum, 1, cnt + 1, 0);
		Arrays.fill(add, 1, cnt + 1, 0);
	}

	/**
	 * 主方法
	 * 处理输入输出，执行区间操作
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int m = (int) in.nval;
		cnt = 1;
		long jobv;
		for (int i = 1, op, jobl, jobr; i <= m; i++) {
			in.nextToken();
			op = (int) in.nval;
			if (op == 1) {
				in.nextToken();
				jobl = (int) in.nval;
				in.nextToken();
				jobr = (int) in.nval;
				in.nextToken();
				jobv = (long) in.nval;
				add(jobl, jobr, jobv, 1, n, 1);
			} else {
				in.nextToken();
				jobl = (int) in.nval;
				in.nextToken();
				jobr = (int) in.nval;
				out.println(query(jobl, jobr, 1, n, 1));
			}
		}
		// 本题每组测试数据都单独运行
		// 可以不写clear方法
		// 但是如果多组测试数据串行调用
		// 就需要加上清空逻辑
		clear();
		out.flush();
		out.close();
		br.close();
	}

}
