package class171;

/**
 * 拦截导弹问题 - C++版本Java实现
 * 
 * 题目来源: 洛谷P2487
 * 题目链接: https://www.luogu.com.cn/problem/P2487
 * 题目难度: 省选/NOI-
 * 
 * 题目描述:
 * 一共有n个导弹，编号1~n，表示导弹从早到晚依次到达，每个导弹给定，高度h、速度v
 * 你有导弹拦截系统，第1次可以拦截任意参数的导弹
 * 但是之后拦截的导弹，高度和速度都不能比前一次拦截的导弹大
 * 你的目的是尽可能多的拦截导弹，如果有多个最优方案，会随机选一个执行
 * 打印最多能拦截几个导弹，并且打印每个导弹被拦截的概率
 * 1 <= n <= 5 * 10^4
 * 1 <= h、v <= 10^9
 * 
 * 解题思路:
 * 这是一个复杂的动态规划问题，结合了最长不降子序列和概率计算，可以使用CDQ分治来优化。
 * 
 * 问题分析:
 * 1. 首先需要找到最长的不上升子序列（高度和速度都不上升）
 * 2. 然后计算每个位置作为子序列一部分的方案数
 * 3. 最后计算每个导弹被拦截的概率
 * 
 * 算法步骤:
 * 1. 预处理：
 *    - 对速度进行离散化处理
 *    - 初始化dp数组
 * 2. 正向计算：
 *    - 计算以每个位置结尾的最长不上升子序列长度和方案数
 *    - 使用CDQ分治优化转移过程
 * 3. 反向计算：
 *    - 计算以每个位置开头的最长不下降子序列长度和方案数
 *    - 使用CDQ分治优化转移过程
 * 4. 概率计算：
 *    - 对于每个位置i，如果len1[i] + len2[i] - 1等于最长子序列长度，
 *      则该位置可能在最优解中
 *    - 概率 = (以i结尾的方案数 * 以i开头的方案数) / 总方案数
 * 
 * 时间复杂度: O(n log^2 n)
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理:
 *    - 处理输入异常，如非法数据格式
 *    - 处理边界情况，如空输入、极值输入
 * 2. 性能优化:
 *    - 使用快速IO提高输入输出效率
 *    - 合理使用离散化减少空间占用
 *    - 优化排序策略减少常数因子
 * 3. 代码可读性:
 *    - 添加详细注释说明算法思路
 *    - 使用有意义的变量命名
 *    - 模块化设计便于维护和扩展
 * 4. 调试能力:
 *    - 添加中间过程打印便于调试
 *    - 使用断言验证关键步骤正确性
 *    - 提供测试用例验证实现正确性
 * 
 * 与其他算法的比较:
 * 1. 与普通DP比较:
 *    - 普通DP时间复杂度O(n^2)，CDQ分治优化到O(n log^2 n)
 *    - CDQ分治空间复杂度更优
 * 2. 与树套树比较:
 *    - CDQ分治实现更简单
 *    - 树套树支持在线查询，CDQ分治需要离线处理
 * 
 * 优化策略:
 * 1. 使用离散化减少值域范围
 * 2. 优化排序策略减少常数
 * 3. 合理安排计算顺序避免重复计算
 * 4. 使用快速IO提高效率
 * 
 * 常见问题及解决方案:
 * 1. 答案错误:
 *    - 问题：贡献计算错误或边界处理不当
 *    - 解决方案：仔细检查贡献计算逻辑，验证边界条件
 * 2. 时间超限:
 *    - 问题：常数因子过大或算法复杂度分析错误
 *    - 解决方案：优化排序策略，减少不必要的操作
 * 3. 空间超限:
 *    - 问题：递归层数过深或数组开得过大
 *    - 解决方案：检查数组大小，使用全局数组，优化递归逻辑
 * 
 * 扩展应用:
 * 1. 可以处理更高维度的偏序问题
 * 2. 可以优化动态规划的转移过程
 * 3. 可以处理动态问题转静态的场景
 * 
 * 学习建议:
 * 1. 先掌握归并排序求逆序对
 * 2. 理解二维偏序问题的处理方法
 * 3. 学习三维偏序的标准处理流程
 * 4. 练习四维偏序问题
 * 5. 掌握CDQ分治优化DP的方法
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code04_Interceptor2 {

	public static int MAXN = 50001;
	public static int n, s;
	public static int[] h = new int[MAXN];
	public static int[] v = new int[MAXN];
	public static int[] sortv = new int[MAXN];

	// 节点结构体：位置i、高度h、速度v
	static class Node {
		int i, h, v;
	}

	public static Node[] arr = new Node[MAXN];

	// 树状数组维护前缀最大值、最大值出现的次数
	public static int[] treeVal = new int[MAXN];
	public static double[] treeCnt = new double[MAXN];

	// i位置结尾的情况下，最长不上升子序列的长度 及其 子序列个数
	public static int[] len1 = new int[MAXN];
	public static double[] cnt1 = new double[MAXN];

	// i位置开头的情况下，最长不上升子序列的长度 及其 子序列个数
	public static int[] len2 = new int[MAXN];
	public static double[] cnt2 = new double[MAXN];

	public static int lowbit(int i) {
		return i & -i;
	}

	public static void more(int i, int val, double cnt) {
		while (i <= s) {
			if (val > treeVal[i]) {
				treeVal[i] = val;
				treeCnt[i] = cnt;
			} else if (val == treeVal[i]) {
				treeCnt[i] += cnt;
			}
			i += lowbit(i);
		}
	}

	public static int queryVal;
	public static double queryCnt;

	public static void query(int i) {
		queryVal = 0;
		queryCnt = 0;
		while (i > 0) {
			if (treeVal[i] > queryVal) {
				queryVal = treeVal[i];
				queryCnt = treeCnt[i];
			} else if (treeVal[i] == queryVal) {
				queryCnt += treeCnt[i];
			}
			i -= lowbit(i);
		}
	}

	public static void clear(int i) {
		while (i <= s) {
			treeVal[i] = 0;
			treeCnt[i] = 0;
			i += lowbit(i);
		}
	}

	public static void merge1(int l, int m, int r) {
		for (int i = l; i <= r; i++) {
			arr[i].i = i;
			arr[i].h = h[i];
			arr[i].v = v[i];
		}
		// 按高度从大到小排序
		Arrays.sort(arr, l, m + 1, (a, b) -> Integer.compare(b.h, a.h));
		Arrays.sort(arr, m + 1, r + 1, (a, b) -> Integer.compare(b.h, a.h));
		int p1, p2;
		// 为了防止出现0下标，(s - v + 1)是树状数组的下标
		for (p1 = l - 1, p2 = m + 1; p2 <= r; p2++) {
			while (p1 + 1 <= m && arr[p1 + 1].h >= arr[p2].h) {
				p1++;
				more(s - arr[p1].v + 1, len1[arr[p1].i], cnt1[arr[p1].i]);
			}
			query(s - arr[p2].v + 1);
			if (queryVal + 1 > len1[arr[p2].i]) {
				len1[arr[p2].i] = queryVal + 1;
				cnt1[arr[p2].i] = queryCnt;
			} else if (queryVal + 1 == len1[arr[p2].i]) {
				cnt1[arr[p2].i] += queryCnt;
			}
		}
		for (int i = l; i <= p1; i++) {
			clear(s - arr[i].v + 1);
		}
	}

	// 最长不上升子序列的长度 及其 个数
	public static void cdq1(int l, int r) {
		if (l == r) {
			return;
		}
		int m = (l + r) / 2;
		cdq1(l, m);
		merge1(l, m, r);
		cdq1(m + 1, r);
	}

	public static void merge2(int l, int m, int r) {
		for (int i = l; i <= r; i++) {
			arr[i].i = i;
			arr[i].h = h[i];
			arr[i].v = v[i];
		}
		// 按高度从小到大排序
		Arrays.sort(arr, l, m + 1, (a, b) -> Integer.compare(a.h, b.h));
		Arrays.sort(arr, m + 1, r + 1, (a, b) -> Integer.compare(a.h, b.h));
		int p1, p2;
		for (p1 = l - 1, p2 = m + 1; p2 <= r; p2++) {
			while (p1 + 1 <= m && arr[p1 + 1].h <= arr[p2].h) {
				p1++;
				more(arr[p1].v, len2[arr[p1].i], cnt2[arr[p1].i]);
			}
			query(arr[p2].v);
			if (queryVal + 1 > len2[arr[p2].i]) {
				len2[arr[p2].i] = queryVal + 1;
				cnt2[arr[p2].i] = queryCnt;
			} else if (queryVal + 1 == len2[arr[p2].i]) {
				cnt2[arr[p2].i] += queryCnt;
			}
		}
		for (int i = l; i <= p1; i++) {
			clear(arr[i].v);
		}
	}

	// 最长不下降子序列的长度 及其 个数
	public static void cdq2(int l, int r) {
		if (l == r) {
			return;
		}
		int m = (l + r) / 2;
		cdq2(l, m);
		merge2(l, m, r);
		cdq2(m + 1, r);
	}

	public static int lower(int num) {
		int l = 1, r = s, ans = 1;
		while (l <= r) {
			int mid = (l + r) / 2;
			if (sortv[mid] >= num) {
				ans = mid;
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		return ans;
	}

	public static void prepare() {
		for (int i = 1; i <= n; i++) {
			sortv[i] = v[i];
		}
		Arrays.sort(sortv, 1, n + 1);
		s = 1;
		for (int i = 2; i <= n; i++) {
			if (sortv[s] != sortv[i]) {
				sortv[++s] = sortv[i];
			}
		}
		for (int i = 1; i <= n; i++) {
			v[i] = lower(v[i]);
		}
		for (int i = 1; i <= n; i++) {
			len1[i] = len2[i] = 1;
			cnt1[i] = cnt2[i] = 1.0;
		}
	}

	public static void compute() {
		cdq1(1, n);
		// 反转数组
		for (int l = 1, r = n; l < r; l++, r--) {
			int a = h[l];
			h[l] = h[r];
			h[r] = a;
			int b = v[l];
			v[l] = v[r];
			v[r] = b;
		}
		cdq2(1, n);
		// 反转结果
		for (int l = 1, r = n; l < r; l++, r--) {
			int a = len2[l];
			len2[l] = len2[r];
			len2[r] = a;
			double b = cnt2[l];
			cnt2[l] = cnt2[r];
			cnt2[r] = b;
		}
	}

	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 初始化节点数组
		for (int i = 0; i < MAXN; i++) {
			arr[i] = new Node();
		}
		
		n = in.nextInt();
		for (int i = 1; i <= n; i++) {
			h[i] = in.nextInt();
			v[i] = in.nextInt();
		}
		prepare();
		compute();
		int len = 0;
		double cnt = 0.0;
		for (int i = 1; i <= n; i++) {
			len = Math.max(len, len1[i]);
		}
		for (int i = 1; i <= n; i++) {
			if (len1[i] == len) {
				cnt += cnt1[i];
			}
		}
		out.println(len);
		for (int i = 1; i <= n; i++) {
			if (len1[i] + len2[i] - 1 < len) {
				out.print("0 ");
			} else {
				out.printf("%.5f ", cnt1[i] * cnt2[i] / cnt);
			}
		}
		out.println();
		out.flush();
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