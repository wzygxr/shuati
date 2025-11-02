package class170;

// 动态逆序对问题的Java版本实现
// 题目来源: 洛谷P3157 [CQOI2011]动态逆序对
// 题目链接: https://www.luogu.com.cn/problem/P3157
// 难度等级: 省选/NOI-
// 标签: CDQ分治, 动态逆序对

// 题目描述:
// 给定一个长度为n的排列，1~n所有数字都出现一次
// 如果，前面的数 > 后面的数，那么这两个数就组成一个逆序对
// 给定一个长度为m的数组，表示依次删除的数字
// 打印每次删除数字前，排列中一共有多少逆序对，一共m条打印
// 约束条件: 1 <= n <= 10^5, 1 <= m <= 5 * 10^4

// 解题思路:
// 使用CDQ分治解决动态逆序对问题。
// 
// 1. 将删除操作转化为时间维度
// 2. 使用CDQ分治处理时间、位置和数值三个维度
// 3. 分别计算每个删除操作对逆序对数量的影响
// 
// 具体步骤：
// 1. 将初始序列和删除操作都看作事件，分别标记为+1和-1
// 2. 使用CDQ分治处理时间维度
// 3. 在合并过程中，分别计算左侧值大的数量和右侧值小的数量
// 4. 通过树状数组维护数值维度的前缀和
// 
// 时间复杂度：O(n log^2 n)
// 空间复杂度：O(n)

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code02_DynamicInversion1 {

	public static int MAXN = 100001;
	public static int MAXM = 50001;
	public static int n, m;

	// num : 原始序列依次的值
	// pos : 每个值在什么位置
	// del : 每一步删掉的值
	public static int[] num = new int[MAXN];
	public static int[] pos = new int[MAXN];
	public static int[] del = new int[MAXM];

	// 数值v、位置i、效果d、问题编号q
	// arr数组存储所有事件：初始序列元素和删除操作
	// 每个事件包含四个属性：数值、位置、效果(+1/-1)、问题编号
	public static int[][] arr = new int[MAXN + MAXM][4];
	public static int cnt = 0;

	// 树状数组，用于维护数值维度的前缀和
	public static int[] tree = new int[MAXN];

	// 每次逆序对的变化量
	public static long[] ans = new long[MAXM];

	/**
	 * 计算一个数的lowbit值，用于树状数组操作
	 * @param i 输入的整数
	 * @return i的lowbit值
	 */
	public static int lowbit(int i) {
		return i & -i;
	}

	/**
	 * 在树状数组的位置i上增加v
	 * @param i 位置
	 * @param v 增加的值
	 */
	public static void add(int i, int v) {
		while (i <= n) {
			tree[i] += v;
			i += lowbit(i);
		}
	}

	/**
	 * 查询树状数组[1, i]范围内的前缀和
	 * @param i 查询的右端点
	 * @return 前缀和
	 */
	public static int query(int i) {
		int ret = 0;
		while (i > 0) {
			ret += tree[i];
			i -= lowbit(i);
		}
		return ret;
	}

	/**
	 * CDQ分治的合并过程
	 * @param l 左边界
	 * @param m 中点
	 * @param r 右边界
	 */
	public static void merge(int l, int m, int r) {
		int p1, p2;
		// 从左到右统计左侧值大的数量
		// 利用左侧和右侧各自在位置维度上的有序性
		for (p1 = l - 1, p2 = m + 1; p2 <= r; p2++) {
			// 双指针移动，找到所有位置小于当前右侧元素位置的左侧元素
			while (p1 + 1 <= m && arr[p1 + 1][1] < arr[p2][1]) {
				p1++;
				// 将左侧元素的数值加入树状数组，权重为其效果值
				add(arr[p1][0], arr[p1][2]);
			}
			// 计算当前右侧元素对答案的贡献
			// arr[p2][2]是当前元素的效果值，(query(n) - query(arr[p2][0]))是数值大于当前元素的左侧元素数量
			ans[arr[p2][3]] += arr[p2][2] * (query(n) - query(arr[p2][0]));
		}
		// 清除树状数组，为下一次统计做准备
		for (int i = l; i <= p1; i++) {
			add(arr[i][0], -arr[i][2]);
		}
		// 从右到左统计右侧值小的数量
		for (p1 = m + 1, p2 = r; p2 > m; p2--) {
			// 双指针移动，找到所有位置大于当前左侧元素位置的右侧元素
			while (p1 - 1 >= l && arr[p1 - 1][1] > arr[p2][1]) {
				p1--;
				// 将右侧元素的数值加入树状数组，权重为其效果值
				add(arr[p1][0], arr[p1][2]);
			}
			// 计算当前左侧元素对答案的贡献
			// arr[p2][2]是当前元素的效果值，query(arr[p2][0] - 1)是数值小于当前元素的右侧元素数量
			ans[arr[p2][3]] += arr[p2][2] * query(arr[p2][0] - 1);
		}
		// 清除树状数组
		for (int i = m; i >= p1; i--) {
			add(arr[i][0], -arr[i][2]);
		}
		// 直接排序，按位置维度排序
		Arrays.sort(arr, l, r + 1, (a, b) -> a[1] - b[1]);
	}

	/**
	 * CDQ分治主函数，按时序组织
	 * @param l 左边界
	 * @param r 右边界
	 */
	// 整体按时序组织，cdq分治里根据下标重新排序
	public static void cdq(int l, int r) {
		if (l == r) {
			return;
		}
		int mid = (l + r) / 2;
		cdq(l, mid);
		cdq(mid + 1, r);
		merge(l, mid, r);
	}

	/**
	 * 准备函数，将初始序列和删除操作转化为事件数组
	 */
	public static void prepare() {
		// 将初始序列元素转化为事件，效果值为+1，问题编号为0
		for (int i = 1; i <= n; i++) {
			arr[++cnt][0] = num[i];
			arr[cnt][1] = i;
			arr[cnt][2] = 1;
			arr[cnt][3] = 0;
		}
		// 将删除操作转化为事件，效果值为-1，问题编号为操作序号
		for (int i = 1; i <= m; i++) {
			arr[++cnt][0] = del[i];
			arr[cnt][1] = pos[del[i]];
			arr[cnt][2] = -1;
			arr[cnt][3] = i;
		}
	}

	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		for (int i = 1; i <= n; i++) {
			num[i] = in.nextInt();
			pos[num[i]] = i;
		}
		for (int i = 1; i <= m; i++) {
			del[i] = in.nextInt();
		}
		prepare();
		cdq(1, cnt);
		// 计算前缀和，得到每次删除前的逆序对数量
		for (int i = 1; i < m; i++) {
			ans[i] += ans[i - 1];
		}
		for (int i = 0; i < m; i++) {
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