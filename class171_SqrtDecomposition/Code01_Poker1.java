package class173;

/**
 * 由乃打扑克，java版
 * 
 * 题目来源：洛谷 P5356
 * 题目描述：
 * 给定一个长度为n的数组arr，接下来有m条操作，操作类型如下
 * 操作 1 l r v : 查询arr[l..r]范围上，第v小的数
 * 操作 2 l r v : arr[l..r]范围上每个数加v，v可能是负数
 * 
 * 数据范围：
 * 1 <= n、m <= 10^5
 * -2 * 10^4 <= 数组中的值 <= +2 * 10^4
 * 
 * 解题思路：
 * 使用分块算法解决此问题。将数组分成大小约为√(n/2)的块，对每个块维护以下信息：
 * 1. 原数组arr：存储实际值
 * 2. 排序数组sortv：存储块内元素排序后的结果
 * 3. 懒惰标记lazy：记录块内所有元素需要增加的值
 * 
 * 对于操作2（区间加法）：
 * - 对于完整块，直接更新懒惰标记
 * - 对于不完整块，暴力更新元素值并重新排序块内元素
 * 
 * 对于操作1（查询第k小）：
 * - 使用二分答案的方法，通过统计小于等于某值的元素个数来确定第k小的值
 * - 统计时利用分块结构优化计算
 * 
 * 时间复杂度分析：
 * - 区间加法操作：O(√n)
 *   - 完整块：O(1)更新标记
 *   - 不完整块：O(√n)暴力更新并排序
 * - 查询第k小：O(√n * log(max_val - min_val))
 *   - 二分答案：O(log(max_val - min_val))
 *   - 每次统计：O(√n)
 * 
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：
 *    - 检查查询参数k的有效性（1 <= k <= 区间长度）
 *    - 处理空区间等边界情况
 * 2. 性能优化：
 *    - 使用懒惰标记避免重复计算
 *    - 合理设置块大小为√(n/2)以平衡完整块和不完整块的处理时间
 * 3. 鲁棒性：
 *    - 处理负数加法操作
 *    - 保证在各种数据分布下的稳定性能
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P5356
 * 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code01_Poker1 {

	public static int MAXN = 100001;
	public static int MAXB = 1001;
	public static int n, m;

	public static int[] arr = new int[MAXN];
	public static int[] sortv = new int[MAXN];

	public static int blen, bnum;
	public static int[] bi = new int[MAXN];
	public static int[] bl = new int[MAXB];
	public static int[] br = new int[MAXB];
	public static int[] lazy = new int[MAXB];

	/**
	 * 对指定区间进行加法操作并维护排序数组
	 * 
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param v 要增加的值
	 * 
	 * 时间复杂度：O(√n + √n*log(√n)) = O(√n*log(√n))
	 * 空间复杂度：O(1)
	 */
	public static void innerAdd(int l, int r, int v) {
		// 对区间内每个元素加上v
		for (int i = l; i <= r; i++) {
			arr[i] += v;
		}
		// 更新该块的排序数组
		for (int i = bl[bi[l]]; i <= br[bi[l]]; i++) {
			sortv[i] = arr[i];
		}
		// 对块内元素重新排序
		Arrays.sort(sortv, bl[bi[l]], br[bi[l]] + 1);
	}

	/**
	 * 区间加法操作
	 * 
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param v 要增加的值
	 * 
	 * 时间复杂度：O(√n)
	 * 空间复杂度：O(1)
	 */
	public static void add(int l, int r, int v) {
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
	 * 获取区间最小值
	 * 
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @return 区间最小值
	 * 
	 * 时间复杂度：O(√n)
	 * 空间复杂度：O(1)
	 */
	public static int getMin(int l, int r) {
		int lb = bi[l], rb = bi[r], ans = 10000000;
		// 如果区间在同一个块内
		if (lb == rb) {
			for (int i = l; i <= r; i++) {
				ans = Math.min(ans, arr[i] + lazy[lb]);
			}
		} else {
			// 处理左边不完整块
			for (int i = l; i <= br[lb]; i++) {
				ans = Math.min(ans, arr[i] + lazy[lb]);
			}
			// 处理右边不完整块
			for (int i = bl[rb]; i <= r; i++) {
				ans = Math.min(ans, arr[i] + lazy[rb]);
			}
			// 处理中间完整块
			for (int i = lb + 1; i <= rb - 1; i++) {
				ans = Math.min(ans, sortv[bl[i]] + lazy[i]);
			}
		}
		return ans;
	}

	/**
	 * 获取区间最大值
	 * 
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @return 区间最大值
	 * 
	 * 时间复杂度：O(√n)
	 * 空间复杂度：O(1)
	 */
	public static int getMax(int l, int r) {
		int lb = bi[l], rb = bi[r], ans = -10000000;
		// 如果区间在同一个块内
		if (lb == rb) {
			for (int i = l; i <= r; i++) {
				ans = Math.max(ans, arr[i] + lazy[lb]);
			}
		} else {
			// 处理左边不完整块
			for (int i = l; i <= br[lb]; i++) {
				ans = Math.max(ans, arr[i] + lazy[lb]);
			}
			// 处理右边不完整块
			for (int i = bl[rb]; i <= r; i++) {
				ans = Math.max(ans, arr[i] + lazy[rb]);
			}
			// 处理中间完整块
			for (int i = lb + 1; i <= rb - 1; i++) {
				ans = Math.max(ans, sortv[br[i]] + lazy[i]);
			}
		}
		return ans;
	}

	/**
	 * 返回第i块内<= v的数字个数
	 * 
	 * @param i 块编号
	 * @param v 比较值
	 * @return 第i块内<= v的数字个数
	 * 
	 * 时间复杂度：O(log(√n)) = O(log n)
	 * 空间复杂度：O(1)
	 */
	// 返回第i块内<= v的数字个数
	public static int blockCnt(int i, int v) {
		v -= lazy[i];
		int l = bl[i], r = br[i];
		if (sortv[l] > v) {
			return 0;
		}
		if (sortv[r] <= v) {
			return r - l + 1;
		}
		int m, find = l;
		while (l <= r) {
			m = (l + r) / 2;
			if (sortv[m] <= v) {
				find = m;
				l = m + 1;
			} else {
				r = m - 1;
			}
		}
		return find - bl[i] + 1;
	}

	/**
	 * 返回arr[l..r]范围上<= v的数字个数
	 * 
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param v 比较值
	 * @return arr[l..r]范围上<= v的数字个数
	 * 
	 * 时间复杂度：O(√n)
	 * 空间复杂度：O(1)
	 */
	// 返回arr[l..r]范围上<= v的数字个数
	public static int getCnt(int l, int r, int v) {
		int lb = bi[l], rb = bi[r], ans = 0;
		// 如果区间在同一个块内
		if (lb == rb) {
			for (int i = l; i <= r; i++) {
				if (arr[i] + lazy[lb] <= v) {
					ans++;
				}
			}
		} else {
			// 处理左边不完整块
			for (int i = l; i <= br[lb]; i++) {
				if (arr[i] + lazy[lb] <= v) {
					ans++;
				}
			}
			// 处理右边不完整块
			for (int i = bl[rb]; i <= r; i++) {
				if (arr[i] + lazy[rb] <= v) {
					ans++;
				}
			}
			// 处理中间完整块
			for (int i = lb + 1; i <= rb - 1; i++) {
				ans += blockCnt(i, v);
			}
		}
		return ans;
	}

	/**
	 * 查询区间第k小的数
	 * 
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param k 第k小
	 * @return 第k小的数，如果k无效则返回-1
	 * 
	 * 时间复杂度：O(√n * log(max_val - min_val))
	 * 空间复杂度：O(1)
	 */
	public static int query(int l, int r, int k) {
		// 检查k的有效性
		if (k < 1 || k > r - l + 1) {
			return -1;
		}
		// 获取区间最小值和最大值作为二分的边界
		int minv = getMin(l, r);
		int maxv = getMax(l, r);
		int midv;
		int ans = -1;
		// 二分答案
		while (minv <= maxv) {
			midv = minv + (maxv - minv) / 2;
			// 如果小于等于midv的元素个数>=k，说明第k小的数<=midv
			if (getCnt(l, r, midv) >= k) {
				ans = midv;
				maxv = midv - 1;
			} else {
				minv = midv + 1;
			}
		}
		return ans;
	}

	/**
	 * 初始化分块结构
	 * 
	 * 时间复杂度：O(n*√n*log(√n)) = O(n*√n*log n)
	 * 空间复杂度：O(n)
	 */
	// 注意调整块长
	public static void prepare() {
		// 设置块大小为sqrt(n/2)，这是一个经验性的优化
		blen = (int) Math.sqrt(n / 2);
		// 计算块数量
		bnum = (n + blen - 1) / blen;
		// 初始化每个元素所属的块
		for (int i = 1; i <= n; i++) {
			bi[i] = (i - 1) / blen + 1;
		}
		// 初始化每个块的边界
		for (int i = 1; i <= bnum; i++) {
			bl[i] = (i - 1) * blen + 1;
			br[i] = Math.min(i * blen, n);
		}
		// 初始化排序数组
		for (int i = 1; i <= n; i++) {
			sortv[i] = arr[i];
		}
		// 对每个块内的元素进行排序
		for (int i = 1; i <= bnum; i++) {
			Arrays.sort(sortv, bl[i], br[i] + 1);
		}
	}

	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt();
		}
		prepare();
		for (int i = 1, op, l, r, v; i <= m; i++) {
			op = in.nextInt();
			l = in.nextInt();
			r = in.nextInt();
			v = in.nextInt();
			if (op == 1) {
				out.println(query(l, r, v));
			} else {
				add(l, r, v);
			}
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