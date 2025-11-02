package class170;

// 园丁的烦恼问题的Java版本实现
// 题目来源: 洛谷P2163 [SHOI2007]园丁的烦恼
// 题目链接: https://www.luogu.com.cn/problem/P2163
// 难度等级: 省选/NOI-
// 标签: CDQ分治, 二维数点

// 题目描述:
// 有n棵树，每棵树给定位置坐标(x, y)，接下来有m条查询，格式如下
// 查询 a b c d : 打印左上角(a, b)、右下角(c, d)的区域里有几棵树
// 约束条件: 
// 0 <= n <= 5 * 10^5
// 1 <= m <= 5 * 10^5
// 0 <= 坐标值 <= 10^7

// 解题思路:
// 使用CDQ分治解决二维数点问题。
// 
// 1. 将查询操作拆分为前缀和的形式
// 2. 使用CDQ分治处理时间维度
// 3. 在合并过程中使用双指针处理y坐标维度
// 
// 具体步骤：
// 1. 将每棵树的插入操作和查询操作都看作事件
// 2. 将二维区域查询转化为四个前缀和查询的组合
// 3. 按照x坐标排序
// 4. 使用CDQ分治处理时间维度，在合并过程中统计y坐标维度上的数量
// 
// 时间复杂度：O((n+m) log^2 (n+m))
// 空间复杂度：O(n+m)

// 注意：java实现的逻辑一定是正确的，但无法通过测试用例，内存使用过大
// 因为这道题只考虑C++能通过的空间极限，根本没考虑java的用户
// 想通过用C++实现，本节课Code03_GardenerTrouble2文件就是C++的实现
// 两个版本的逻辑完全一样，C++版本可以通过所有测试

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code03_GardenerTrouble1 {

	public static int MAXN = 500001 * 5;
	public static int n, m;

	// op == 1代表树木，x、y
	// op == 2代表查询，x、y、效果v、查询编号q
	// arr数组存储所有事件：树木插入事件和查询事件
	public static int[][] arr = new int[MAXN][5];
	public static int cnt = 0;

	// 归并排序需要的临时数组
	public static int[][] tmp = new int[MAXN][5];

	// 问题的答案，ans[i]表示第i个查询的答案
	public static int[] ans = new int[MAXN];

	/**
	 * 复制一个事件数组元素到另一个位置
	 * @param a 目标数组元素
	 * @param b 源数组元素
	 */
	public static void clone(int[] a, int[] b) {
		a[0] = b[0];
		a[1] = b[1];
		a[2] = b[2];
		a[3] = b[3];
		a[4] = b[4];
	}

	/**
	 * 添加一棵树的插入事件
	 * @param x 树的x坐标
	 * @param y 树的y坐标
	 */
	public static void addTree(int x, int y) {
		arr[++cnt][0] = 1;  // 操作类型：1表示树木
		arr[cnt][1] = x;    // x坐标
		arr[cnt][2] = y;    // y坐标
	}

	/**
	 * 添加一个查询事件
	 * @param x 查询点的x坐标
	 * @param y 查询点的y坐标
	 * @param v 效果值(+1/-1)
	 * @param q 查询编号
	 */
	public static void addQuery(int x, int y, int v, int q) {
		arr[++cnt][0] = 2;  // 操作类型：2表示查询
		arr[cnt][1] = x;    // x坐标
		arr[cnt][2] = y;    // y坐标
		arr[cnt][3] = v;    // 效果值
		arr[cnt][4] = q;    // 查询编号
	}

	/**
	 * CDQ分治的合并过程
	 * @param l 左边界
	 * @param m 中点
	 * @param r 右边界
	 */
	public static void merge(int l, int m, int r) {
		int p1, p2, tree = 0;
		// 利用左侧和右侧各自在y坐标上的有序性
		for (p1 = l - 1, p2 = m + 1; p2 <= r; p2++) {
			// 双指针移动，找到所有y坐标小于等于当前右侧元素y坐标的左侧元素
			while (p1 + 1 <= m && arr[p1 + 1][2] <= arr[p2][2]) {
				p1++;
				// 如果是树木插入事件，增加计数
				if (arr[p1][0] == 1) {
					tree++;
				}
			}
			// 如果是查询事件，累加答案
			if (arr[p2][0] == 2) {
				// tree表示y坐标小于等于当前查询点y坐标的树木数量
				// arr[p2][3]是效果值，用于处理前缀和
				ans[arr[p2][4]] += tree * arr[p2][3];
			}
		}
		// 下面是经典归并的过程，为啥不直接排序了？
		// 因为没有用到高级数据结构，复杂度可以做到O(n * log n)
		// 那么就维持最好的复杂度，不用排序
		p1 = l;
		p2 = m + 1;
		int i = l;
		while (p1 <= m && p2 <= r) {
			clone(tmp[i++], arr[p1][2] <= arr[p2][2] ? arr[p1++] : arr[p2++]);
		}
		while (p1 <= m) {
			clone(tmp[i++], arr[p1++]);
		}
		while (p2 <= r) {
			clone(tmp[i++], arr[p2++]);
		}
		for (i = l; i <= r; i++) {
			clone(arr[i], tmp[i]);
		}
	}

	/**
	 * CDQ分治主函数
	 * @param l 左边界
	 * @param r 右边界
	 */
	public static void cdq(int l, int r) {
		if (l == r) {
			return;
		}
		int mid = (l + r) / 2;
		cdq(l, mid);
		cdq(mid + 1, r);
		merge(l, mid, r);
	}

	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		// 读取所有树木的坐标并添加插入事件
		for (int i = 1, x, y; i <= n; i++) {
			x = in.nextInt();
			y = in.nextInt();
			addTree(x, y);
		}
		// 读取所有查询，将二维区域查询转化为四个前缀和查询的组合
		for (int i = 1, a, b, c, d; i <= m; i++) {
			a = in.nextInt();
			b = in.nextInt();
			c = in.nextInt();
			d = in.nextInt();
			// 使用容斥原理将矩形区域查询转换为四个前缀和查询
			// 右上角区域加1
			addQuery(c, d, 1, i);
			// 左下角区域加1
			addQuery(a - 1, b - 1, 1, i);
			// 左上角区域减1
			addQuery(a - 1, d, -1, i);
			// 右下角区域减1
			addQuery(c, b - 1, -1, i);
		}
		// 按照x坐标排序，x坐标相同的按照操作类型排序(树木在前)
		Arrays.sort(arr, 1, cnt + 1, (a, b) -> a[1] != b[1] ? a[1] - b[1] : a[0] - b[0]);
		// 执行CDQ分治
		cdq(1, cnt);
		// 输出所有查询的答案
		for (int i = 1; i <= m; i++) {
			out.println(ans[i]);
		}
		out.flush();
		out.close();
	}

	// 读写工具类
	static class FastReader {
		private final byte[] buffer = new byte[1 << 20];
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