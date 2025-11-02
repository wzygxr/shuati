package class173;

/**
 * 序列，java版
 * 
 * 题目来源：洛谷 P3863
 * 题目描述：
 * 给定一个长度为n的数组arr，初始时刻认为是第0秒
 * 接下来发生m条操作，第i条操作发生在第i秒，操作类型如下
 * 操作 1 l r v : arr[l..r]范围上每个数加v，v可能是负数
 * 操作 2 x v   : 不包括当前这一秒，查询过去多少秒内，arr[x] >= v
 * 
 * 数据范围：
 * 2 <= n、m <= 10^5
 * -10^9 <= 数组中的值 <= +10^9
 * 
 * 解题思路：
 * 这是一个时间轴上的分块问题。我们需要处理两种操作：
 * 1. 区间加法操作：对时间轴上的区间进行加法操作
 * 2. 查询操作：查询在某个时间点之前，满足条件的时间点数量
 * 
 * 关键思路是将所有事件离线处理，按位置排序后使用分块算法：
 * 1. 将所有修改和查询事件存储下来
 * 2. 按位置排序，相同位置时修改事件优先于查询事件
 * 3. 使用分块维护时间轴上的信息
 * 4. 对于每个位置，维护时间轴上该位置的值变化情况
 * 
 * 时间复杂度分析：
 * - 预处理（排序）：O((m+n) * log(m+n))
 * - 每次区间加法操作：O(√m)
 * - 每次查询操作：O(√m)
 * - 总体时间复杂度：O((m+n) * log(m+n) + (m+n) * √m)
 * 
 * 空间复杂度：O(m+n)
 * 
 * 工程化考量：
 * 1. 异常处理：
 *    - 处理空区间情况
 *    - 处理边界条件
 * 2. 性能优化：
 *    - 使用分块算法优化区间操作
 *    - 离线处理减少重复计算
 * 3. 鲁棒性：
 *    - 处理大数值运算（使用long类型）
 *    - 保证在各种数据分布下的稳定性能
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P3863
 * 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code02_Sequence1 {

	public static int MAXN = 100001;
	public static int MAXB = 501;
	public static int n, m;
	public static int[] arr = new int[MAXN];

	// 事件数组，存储所有操作事件
	// op == 1 表示修改事件、位置x、时刻t、修改效果v、空缺
	// op == 2 表示查询事件、位置x、时刻t、查询标准v、问题编号q
	public static int[][] event = new int[MAXN << 2][5];
	public static int cnte = 0; // 事件计数器
	public static int cntq = 0; // 查询计数器

	// tim[i] = v，表示在i号时间点，所有数字都增加v
	public static long[] tim = new long[MAXN];
	// 时间块内的所有值要排序，方便查询 >= v的数字个数
	public static long[] sortv = new long[MAXN];

	// 时间分块相关数组
	public static int blen, bnum; // 块大小和块数量
	public static int[] bi = new int[MAXN]; // 每个时间点所属的块
	public static int[] bl = new int[MAXB]; // 每个块的左边界
	public static int[] br = new int[MAXB]; // 每个块的右边界
	public static long[] lazy = new long[MAXB]; // 每个块的懒惰标记

	// 每个查询的答案
	public static int[] ans = new int[MAXN];

	/**
	 * 对指定时间区间进行加法操作并维护排序数组
	 * 
	 * @param l 时间区间左端点
	 * @param r 时间区间右端点
	 * @param v 要增加的值
	 * 
	 * 时间复杂度：O(√m + √m*log(√m)) = O(√m*log(√m))
	 * 空间复杂度：O(1)
	 */
	public static void innerAdd(int l, int r, long v) {
		// 对时间区间内每个时间点加上v
		for (int i = l; i <= r; i++) {
			tim[i] += v;
		}
		// 更新该块的排序数组
		for (int i = bl[bi[l]]; i <= br[bi[l]]; i++) {
			sortv[i] = tim[i];
		}
		// 对块内时间点重新排序
		Arrays.sort(sortv, bl[bi[l]], br[bi[l]] + 1);
	}

	/**
	 * 时间区间加法操作
	 * 
	 * @param l 时间区间左端点
	 * @param r 时间区间右端点
	 * @param v 要增加的值
	 * 
	 * 时间复杂度：O(√m)
	 * 空间复杂度：O(1)
	 */
	public static void add(int l, int r, long v) {
		// 处理空区间
		if (l > r) {
			return;
		}
		// 如果区间在同一个块内
		if (bi[l] == bi[r]) {
			innerAdd(l, r, v);
		} else {
			// 处理左边不完整块
			innerAdd(l, br[bi[l]], v);
			// 处理右边不完整块
			innerAdd(bl[bi[r]], r, v);
			// 处理中间完整块
			for (int i = bi[l] + 1; i <= bi[r] - 1; i++) {
				lazy[i] += v;
			}
		}
	}

	/**
	 * 在指定时间区间内查询大于等于v的数字个数（暴力方法）
	 * 
	 * @param l 时间区间左端点
	 * @param r 时间区间右端点
	 * @param v 比较值
	 * @return 大于等于v的数字个数
	 * 
	 * 时间复杂度：O(√m)
	 * 空间复杂度：O(1)
	 */
	public static int innerQuery(int l, int r, long v) {
		v -= lazy[bi[l]]; // 考虑块的懒惰标记
		int ans = 0;
		for (int i = l; i <= r; i++) {
			if (tim[i] >= v) {
				ans++;
			}
		}
		return ans;
	}

	/**
	 * 第i块内>= v的数字个数（使用二分查找）
	 * 
	 * @param i 块编号
	 * @param v 比较值
	 * @return 第i块内>= v的数字个数
	 * 
	 * 时间复杂度：O(log(√m)) = O(log m)
	 * 空间复杂度：O(1)
	 */
	// 第i块内>= v的数字个数
	public static int getCnt(int i, long v) {
		v -= lazy[i]; // 考虑块的懒惰标记
		int l = bl[i], r = br[i], m, pos = br[i] + 1;
		// 二分查找第一个大于等于v的位置
		while (l <= r) {
			m = (l + r) >> 1;
			if (sortv[m] >= v) {
				pos = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return br[i] - pos + 1;
	}

	/**
	 * 查询时间区间内大于等于v的数字个数
	 * 
	 * @param l 时间区间左端点
	 * @param r 时间区间右端点
	 * @param v 比较值
	 * @return 大于等于v的数字个数
	 * 
	 * 时间复杂度：O(√m)
	 * 空间复杂度：O(1)
	 */
	public static int query(int l, int r, long v) {
		// 处理空区间
		if (l > r) {
			return 0;
		}
		int ans = 0;
		// 如果区间在同一个块内
		if (bi[l] == bi[r]) {
			ans += innerQuery(l, r, v);
		} else {
			// 处理左边不完整块
			ans += innerQuery(l, br[bi[l]], v);
			// 处理右边不完整块
			ans += innerQuery(bl[bi[r]], r, v);
			// 处理中间完整块
			for (int i = bi[l] + 1; i <= bi[r] - 1; i++) {
				ans += getCnt(i, v);
			}
		}
		return ans;
	}

	/**
	 * 添加修改事件
	 * 
	 * @param x 位置
	 * @param t 时间
	 * @param v 修改值
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 */
	public static void addChange(int x, int t, int v) {
		event[++cnte][0] = 1; // 操作类型：修改
		event[cnte][1] = x;   // 位置
		event[cnte][2] = t;   // 时间
		event[cnte][3] = v;   // 修改值
	}

	/**
	 * 添加查询事件
	 * 
	 * @param x 位置
	 * @param t 时间
	 * @param v 查询标准
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 */
	public static void addQuery(int x, int t, int v) {
		event[++cnte][0] = 2;     // 操作类型：查询
		event[cnte][1] = x;       // 位置
		event[cnte][2] = t;       // 时间
		event[cnte][3] = v;       // 查询标准
		event[cnte][4] = ++cntq;  // 查询编号
	}

	/**
	 * 初始化分块结构和事件排序
	 * 
	 * 时间复杂度：O((m+n) * log(m+n))
	 * 空间复杂度：O(m+n)
	 */
	public static void prepare() {
		// 设置块大小为sqrt(m)
		blen = (int) Math.sqrt(m);
		// 计算块数量
		bnum = (m + blen - 1) / blen;
		// 初始化每个时间点所属的块
		for (int i = 1; i <= m; i++) {
			bi[i] = (i - 1) / blen + 1;
		}
		// 初始化每个块的边界
		for (int i = 1; i <= bnum; i++) {
			bl[i] = (i - 1) * blen + 1;
			br[i] = Math.min(i * blen, m);
		}
		// 所有事件根据位置x排序，位置一样的事件，修改事件先执行，查询事件后执行
		Arrays.sort(event, 1, cnte + 1, (a, b) -> a[1] != b[1] ? a[1] - b[1] : a[2] - b[2]);
	}

	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt();
		}
		m++; // 时间轴重新定义，1是初始时刻、2、3 ... m+1
		// 读取所有操作
		for (int t = 2, op, l, r, v, x; t <= m; t++) {
			op = in.nextInt();
			if (op == 1) {
				l = in.nextInt();
				r = in.nextInt();
				v = in.nextInt();
				// 使用差分数组技巧处理区间加法
				addChange(l, t, v);
				addChange(r + 1, t, -v);
			} else {
				x = in.nextInt();
				v = in.nextInt();
				addQuery(x, t, v);
			}
		}
		prepare();
		// 处理所有事件
		for (int i = 1, op, x, t, v, q; i <= cnte; i++) {
			op = event[i][0];
			x = event[i][1];
			t = event[i][2];
			v = event[i][3];
			q = event[i][4];
			if (op == 1) {
				// 处理修改事件
				add(t, m, v);
			} else {
				// 处理查询事件
				ans[q] = query(1, t - 1, v - arr[x]);
			}
		}
		// 输出所有查询结果
		for (int i = 1; i <= cntq; i++) {
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