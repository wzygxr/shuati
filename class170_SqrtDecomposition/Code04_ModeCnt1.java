package class172;

// 空间少求众数的次数，java版
// 题目来源：洛谷P5048 [Ynoi2019 模拟赛] Yuno loves sqrt technology III
// 题目链接：https://www.luogu.com.cn/problem/P5048
// 题目大意：
// 给定一个长度为n的数组arr，接下来有m条操作，每条操作格式如下
// 操作 l r : 打印arr[l..r]范围上，众数到底出现了几次
// 1 <= 所有数值 <= 5 * 10^5
// 内存空间只有64MB，题目要求强制在线，具体规则可以打开测试链接查看
// 测试链接 : https://www.luogu.com.cn/problem/P5048
// 提交以下的code，提交时请把类名改成"Main"
// java实现的逻辑一定是正确的，但是内存占用过大，无法通过测试用例
// 因为这道题只考虑C++能通过的空间标准，根本没考虑java的用户
// 想通过用C++实现，本节课Code04_ModeCnt2文件就是C++的实现
// 两个版本的逻辑完全一样，C++版本可以通过所有测试

// 解题思路：
// 使用分块算法解决此问题，采用预处理优化查询
// 1. 将数组分成sqrt(n)大小的块
// 2. 对数组元素按值和下标进行排序，构建sortList数组
// 3. 预处理modeCnt[i][j]表示从第i块到第j块中众数的出现次数
// 4. 对于查询操作：
//    - 如果在同一个块内，直接暴力统计
//    - 如果跨多个块，结合预处理信息和两端不完整块统计

// 时间复杂度分析：
// 1. 预处理：O(n*sqrt(n))，构建modeCnt数组
// 2. 查询操作：O(sqrt(n))，处理两端不完整块
// 空间复杂度：O(n*sqrt(n))，存储sortList、listIdx和modeCnt数组

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code04_ModeCnt1 {

	// 最大数组大小
	public static int MAXN = 500001;
	// 最大块数
	public static int MAXB = 801;
	// 数组长度和操作数
	public static int n, m;
	// 原数组
	public static int[] arr = new int[MAXN];

	// 块大小和块数量
	public static int blen, bnum;
	// 每个元素所属的块
	public static int[] bi = new int[MAXN];
	// 每个块的左右边界
	public static int[] bl = new int[MAXB];
	public static int[] br = new int[MAXB];

	// (值、下标)，用来收集同一种数的下标列表
	public static int[][] sortList = new int[MAXN][2];
	// listIdx[i] = j，表示arr[i]这个元素在sortList里的j位置
	public static int[] listIdx = new int[MAXN];
	// modeCnt[i][j]表示从i块到j块中众数的出现次数
	public static int[][] modeCnt = new int[MAXB][MAXB];
	// 数字词频统计
	public static int[] numCnt = new int[MAXN];

	/**
	 * 预处理函数，构建分块结构和预处理数组
	 * 时间复杂度：O(n*sqrt(n))
	 */
	public static void prepare() {
		// 建块
		blen = (int) Math.sqrt(n);
		bnum = (n + blen - 1) / blen;
		
		// 计算每个元素属于哪个块
		for (int i = 1; i <= n; i++) {
			bi[i] = (i - 1) / blen + 1;
		}
		
		// 计算每个块的左右边界
		for (int i = 1; i <= bnum; i++) {
			bl[i] = (i - 1) * blen + 1;
			br[i] = Math.min(i * blen, n);
		}
		
		// 构建sortList数组，存储(值, 下标)对
		for (int i = 1; i <= n; i++) {
			sortList[i][0] = arr[i];  // 值
			sortList[i][1] = i;       // 下标
		}
		
		// 按值和下标排序
		Arrays.sort(sortList, 1, n + 1, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
		
		// 构建listIdx数组，记录每个元素在sortList中的位置
		for (int i = 1; i <= n; i++) {
			listIdx[sortList[i][1]] = i;
		}
		
		// 填好modeCnt数组
		// 预处理从第i块到第j块中众数的出现次数
		for (int i = 1; i <= bnum; i++) {
			for (int j = i; j <= bnum; j++) {
				// 初始众数出现次数为从第i块到第j-1块的众数出现次数
				int cnt = modeCnt[i][j - 1];
				// 遍历第j块中的所有元素，更新众数出现次数
				for (int k = bl[j]; k <= br[j]; k++) {
					cnt = Math.max(cnt, ++numCnt[arr[k]]);
				}
				modeCnt[i][j] = cnt;
			}
			// 清空统计数组
			for (int j = 1; j <= n; j++) {
				numCnt[j] = 0;
			}
		}
	}

	/**
	 * 查询区间[l,r]中众数的出现次数
	 * 时间复杂度：O(sqrt(n))
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @return 众数的出现次数
	 */
	public static int query(int l, int r) {
		int ans = 0;
		// 如果在同一个块内，直接暴力统计
		if (bi[l] == bi[r]) {
			// 统计各数字出现次数，同时更新最大出现次数
			for (int i = l; i <= r; i++) {
				ans = Math.max(ans, ++numCnt[arr[i]]);
			}
			// 清空统计数组
			for (int i = l; i <= r; i++) {
				numCnt[arr[i]] = 0;
			}
		} else {
			// 获取中间完整块的众数出现次数
			ans = modeCnt[bi[l] + 1][bi[r] - 1];
			
			// 处理左端不完整块
			// 通过listIdx找到该元素在sortList中的位置，然后向后查找连续相同值的元素
			for (int i = l, idx; i <= br[bi[l]]; i++) {
				idx = listIdx[i];
				// 向后查找连续相同值的元素，直到超出范围或下标大于r
				while (idx + ans <= n && sortList[idx + ans][0] == arr[i] && sortList[idx + ans][1] <= r) {
					ans++;
				}
			}
			
			// 处理右端不完整块
			// 通过listIdx找到该元素在sortList中的位置，然后向前查找连续相同值的元素
			for (int i = bl[bi[r]], idx; i <= r; i++) {
				idx = listIdx[i];
				// 向前查找连续相同值的元素，直到超出范围或下标小于l
				while (idx - ans >= 1 && sortList[idx - ans][0] == arr[i] && sortList[idx - ans][1] >= l) {
					ans++;
				}
			}
		}
		return ans;
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
		// 强制在线处理
		for (int i = 1, l, r, lastAns = 0; i <= m; i++) {
			l = in.nextInt() ^ lastAns;
			r = in.nextInt() ^ lastAns;
			lastAns = query(l, r);
			out.println(lastAns);
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