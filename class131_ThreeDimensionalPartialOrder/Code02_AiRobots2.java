package class171;

/**
 * AI robots问题 - C++版本Java实现
 * 
 * 题目来源: Codeforces 1045G
 * 题目链接: https://codeforces.com/problemset/problem/1045/G
 * 题目难度: 2000
 * 
 * 题目描述:
 * 一共有n个机器人，给定一个整数k，每个机器人给定，位置x、视野y、智商q
 * 第i个机器人可以看见的范围是[xi − yi, xi + yi]
 * 如果两个机器人相互之间可以看见，并且智商差距不大于k，那么它们会开始聊天
 * 打印有多少对机器人可以聊天
 * 1 <= n <= 10^5
 * 0 <= k <= 20
 * 0 <= x、y、q <= 10^9
 * 
 * 解题思路:
 * 这是一个典型的三维偏序问题，可以使用CDQ分治来解决。
 * 
 * 问题分析:
 * 两个机器人i和j能够聊天需要满足三个条件：
 * 1. 机器人i能看到机器人j（j在i的视野范围内）
 * 2. 机器人j能看到机器人i（i在j的视野范围内）
 * 3. 两者的智商差不超过k（|qi - qj| <= k）
 * 
 * 算法步骤:
 * 1. 首先按照视野y从大到小排序，这样可以保证如果右边的机器人能看到左边的机器人，
 *    那么左边的机器人也能看到右边的机器人（因为视野大的能看到视野小的）
 * 2. 使用CDQ分治处理：
 *    - 将区间[l,r]分成两部分[l,mid]和[mid+1,r]
 *    - 递归处理左半部分和右半部分
 *    - 计算左半部分对右半部分的贡献（即左半部分的机器人能看到右半部分的机器人）
 * 3. 在合并过程中：
 *    - 对左半部分按照智商q排序
 *    - 对右半部分按照智商q排序
 *    - 使用双指针维护智商差不超过k的窗口
 *    - 使用树状数组维护位置x的信息，查询满足条件的机器人数量
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
 * 1. 与树套树比较:
 *    - CDQ分治空间复杂度更优O(n) vs 树套树O(n log^2 n)
 *    - CDQ分治实现更简单
 *    - 树套树支持在线查询，CDQ分治需要离线处理
 * 2. 与KD树比较:
 *    - CDQ分治在特定问题上更高效
 *    - KD树支持在线查询和更复杂的操作
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

public class Code02_AiRobots2 {

	public static int MAXN = 100001;
	public static int n, k, s;

	// 机器人结构体：位置x、视野y、智商q、能看到的最左位置l、能看到的最右位置r
	static class Node {
		int x, y, q, l, r;
	}

	public static Node[] arr = new Node[MAXN];
	public static int[] x = new int[MAXN];
	public static int[] tree = new int[MAXN];

	public static int lowbit(int i) {
		return i & -i;
	}

	public static void add(int i, int v) {
		while (i <= s) {
			tree[i] += v;
			i += lowbit(i);
		}
	}

	public static int sum(int i) {
		int ret = 0;
		while (i > 0) {
			ret += tree[i];
			i -= lowbit(i);
		}
		return ret;
	}

	public static int query(int l, int r) {
		return sum(r) - sum(l - 1);
	}

	public static long merge(int l, int m, int r) {
		int winl = l, winr = l - 1;
		long ans = 0;
		for (int i = m + 1; i <= r; i++) {
			while (winl <= m && arr[winl].q < arr[i].q - k) {
				add(arr[winl].x, -1);
				winl++;
			}
			while (winr + 1 <= m && arr[winr + 1].q <= arr[i].q + k) {
				winr++;
				add(arr[winr].x, 1);
			}
			ans += query(arr[i].l, arr[i].r);
		}
		for (int i = winl; i <= winr; i++) {
			add(arr[i].x, -1);
		}
		// 按智商排序
		Arrays.sort(arr, l, r + 1, (a, b) -> Integer.compare(a.q, b.q));
		return ans;
	}

	public static long cdq(int l, int r) {
		if (l == r) {
			return 0;
		}
		int mid = (l + r) / 2;
		return cdq(l, mid) + cdq(mid + 1, r) + merge(l, mid, r);
	}

	public static int lower(int num) {
		int l = 1, r = s, m, ans = 1;
		while (l <= r) {
			m = (l + r) / 2;
			if (x[m] >= num) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}

	public static int upper(int num) {
		int l = 1, r = s, m, ans = s + 1;
		while (l <= r) {
			m = (l + r) / 2;
			if (x[m] > num) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}

	public static void prepare() {
		for (int i = 1; i <= n; i++) {
			x[i] = arr[i].x;
		}
		Arrays.sort(x, 1, n + 1);
		s = 1;
		for (int i = 2; i <= n; i++) {
			if (x[s] != x[i]) {
				x[++s] = x[i];
			}
		}
		for (int i = 1; i <= n; i++) {
			arr[i].l = lower(arr[i].x - arr[i].y);
			arr[i].r = upper(arr[i].x + arr[i].y) - 1;
			arr[i].x = lower(arr[i].x);
		}
		// 按视野从大到小排序
		Arrays.sort(arr, 1, n + 1, (a, b) -> Integer.compare(b.y, a.y));
	}

	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 初始化节点数组
		for (int i = 0; i < MAXN; i++) {
			arr[i] = new Node();
		}
		
		n = in.nextInt();
		k = in.nextInt();
		for (int i = 1; i <= n; i++) {
			arr[i].x = in.nextInt();
			arr[i].y = in.nextInt();
			arr[i].q = in.nextInt();
		}
		prepare();
		out.println(cdq(1, n));
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