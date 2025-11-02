package class115;

// 矩形周长并 - 扫描线算法实现 (POJ平台版本)
// 问题描述：给定平面上的n个矩形，求这些矩形的并集周长
// 解题思路：使用扫描线算法分别计算水平边和垂直边的长度
// 算法复杂度：时间复杂度O(n log n)，空间复杂度O(n)
// 工程化考量：
// 1. 使用高效的IO处理，适用于竞赛环境
// 2. 线段树实现优化，利用问题特殊性避免懒更新
// 3. 离散化处理坐标，减少空间使用
// 4. 边界条件处理完善，避免数组越界
// 5. 由于POJ平台Java版本较老，不支持lambda表达式，需要自定义比较器
// 测试链接 : http://poj.org/problem?id=1177
// poj上的java版本较老，不支持lamda表达式定义比较器
// 所以需要自己定义比较器，除此之外没有区别
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.Comparator;

public class Code04_PerimeterSum2 {

	// 最大矩形数量
	public static int MAXN = 20001;

	// 存储矩形信息：[左下x, 左下y, 右上x, 右上y]
	public static int[][] rec = new int[MAXN][4];

	// 存储扫描线事件：[扫描线位置, 区间下界, 区间上界, 变化量(1或-1)]
	public static int[][] line = new int[MAXN][4];

	// 存储所有坐标用于离散化
	public static int[] vsort = new int[MAXN];

	// 线段树某范围总长度
	public static int[] length = new int[MAXN << 2];

	// 线段树某范围覆盖长度
	public static int[] cover = new int[MAXN << 2];

	// 线段树某范围覆盖次数
	public static int[] times = new int[MAXN << 2];

	/**
	 * 离散化坐标数组，去除重复元素
	 * @param n 原始元素个数
	 * @return 去重后的元素个数
	 */
	public static int prepare(int n) {
		Arrays.sort(vsort, 1, n + 1);
		int m = 1;
		for (int i = 2; i <= n; i++) {
			if (vsort[m] != vsort[i]) {
				vsort[++m] = vsort[i];
			}
		}
		vsort[m + 1] = vsort[m];
		return m;
	}

	/**
	 * 二分查找坐标在离散化数组中的位置
	 * @param n 离散化数组长度
	 * @param num 要查找的坐标值
	 * @return 离散化后的索引位置
	 */
	public static int rank(int n, int num) {
		int ans = 0;
		int l = 1, r = n, mid;
		while (l <= r) {
			mid = (l + r) >> 1;
			if (vsort[mid] >= num) {
				ans = mid;
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		return ans;
	}

	/**
	 * 构建线段树
	 * @param l 当前节点表示区间的左边界
	 * @param r 当前节点表示区间的右边界
	 * @param i 当前节点在线段树中的索引
	 */
	private static void build(int l, int r, int i) {
		if (l < r) {
			int mid = (l + r) >> 1;
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
		}
		length[i] = vsort[r + 1] - vsort[l];
		times[i] = 0;
		cover[i] = 0;
	}

	/**
	 * 更新线段树节点的覆盖长度
	 * @param i 当前节点在线段树中的索引
	 */
	public static void up(int i) {
		if (times[i] > 0) {
			cover[i] = length[i];
		} else {
			cover[i] = cover[i << 1] + cover[i << 1 | 1];
		}
	}

	/**
	 * 在线段树中添加或删除扫描线覆盖
	 * @param jobl 操作区间左边界
	 * @param jobr 操作区间右边界
	 * @param jobv 操作值(+1表示添加，-1表示删除)
	 * @param l 当前节点表示区间的左边界
	 * @param r 当前节点表示区间的右边界
	 * @param i 当前节点在线段树中的索引
	 */
	private static void add(int jobl, int jobr, int jobv, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			times[i] += jobv;
		} else {
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				add(jobl, jobr, jobv, l, mid, i << 1);
			}
			if (jobr > mid) {
				add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
		}
		up(i);
	}

	public static void main(String[] args) throws IOException {
		// 初始化输入输出流
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取矩形数量
		in.nextToken();
		int n = (int) in.nval;
		
		// 读取所有矩形的坐标信息
		for (int i = 1; i <= n; i++) {
			in.nextToken(); rec[i][0] = (int) in.nval;
			in.nextToken(); rec[i][1] = (int) in.nval;
			in.nextToken(); rec[i][2] = (int) in.nval;
			in.nextToken(); rec[i][3] = (int) in.nval;
		}
		
		// 计算并输出矩形周长并
		out.println(compute(n));
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算n个矩形的周长并
	 * 算法核心思想：
	 * 1. 分别计算水平边和垂直边的长度
	 * 2. 水平边长度 = 扫描y轴时的投影变化总和
	 * 3. 垂直边长度 = 扫描x轴时的投影变化总和
	 * @param n 矩形数量
	 * @return 矩形周长并
	 */
	public static long compute(int n) {
		return scanY(n) + scanX(n);
	}

	/**
	 * 扫描y轴方向计算水平边长度
	 * @param n 矩形数量
	 * @return 水平边长度总和
	 */
	public static long scanY(int n) {
		// 构造y轴方向的扫描线事件
		for (int i = 1, j = 1 + n, x1, y1, x2, y2; i <= n; i++, j++) {
			x1 = rec[i][0]; y1 = rec[i][1]; x2 = rec[i][2]; y2 = rec[i][3];
			vsort[i] = y1; vsort[j] = y2;
			line[i][0] = x1; line[i][1] = y1; line[i][2] = y2; line[i][3] = 1;
			line[j][0] = x2; line[j][1] = y1; line[j][2] = y2; line[j][3] = -1;
		}
		return scan(n << 1);
	}

	/**
	 * 扫描x轴方向计算垂直边长度
	 * @param n 矩形数量
	 * @return 垂直边长度总和
	 */
	public static long scanX(int n) {
		// 构造x轴方向的扫描线事件
		for (int i = 1, j = 1 + n, x1, y1, x2, y2; i <= n; i++, j++) {
			x1 = rec[i][0]; y1 = rec[i][1]; x2 = rec[i][2]; y2 = rec[i][3];
			vsort[i] = x1; vsort[j] = x2;
			line[i][0] = y1; line[i][1] = x1; line[i][2] = x2; line[i][3] = 1;
			line[j][0] = y2; line[j][1] = x1; line[j][2] = x2; line[j][3] = -1;
		}
		return scan(n << 1);
	}

	/**
	 * 执行扫描线算法计算投影长度变化总和
	 * @param n 扫描线事件数量
	 * @return 投影长度变化总和
	 */
	public static long scan(int n) {
		int m = prepare(n);
		build(1, m, 1);
		// 使用自定义比较器排序扫描线事件
		Arrays.sort(line, 1, n + 1, new LineComparator());
		long ans = 0;
		for (int i = 1, pre; i <= n; i++) {
			pre = cover[1];
			add(rank(m, line[i][1]), rank(m, line[i][2]) - 1, line[i][3], 1, m, 1);
			ans += Math.abs(cover[1] - pre);
		}
		return ans;
	}

	// 这里有个坑
	// 在排序时，如果同一个位置的扫描线有多条，也就是a[0] == b[0]时
	// 应该先处理区间覆盖+1的扫描线，然后再处理区间覆盖-1的扫描线
	// 不然投影长度会频繁变化，导致答案错误
	// 不过测试数据并没有安排这方面的测试
	// poj上的java版本较老，不支持lamda表达式定义比较器
	// 需要自己定义比较器，除此之外没有区别
	/**
	 * 扫描线事件比较器
	 * 比较规则：
	 * 1. 首先按扫描线位置升序排列
	 * 2. 位置相同时，先处理覆盖+1的事件，再处理覆盖-1的事件
	 */
	public static class LineComparator implements Comparator<int[]> {

		@Override
		public int compare(int[] a, int[] b) {
			return a[0] != b[0] ? (a[0] - b[0]) : (b[3] - a[3]);
		}

	}

}