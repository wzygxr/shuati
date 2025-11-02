package class171;

/**
 * 序列问题 - Java版本
 * 
 * 题目来源: 洛谷P4093
 * 题目链接: https://www.luogu.com.cn/problem/P4093
 * 题目难度: 省选/NOI-
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，一共有m条操作，格式为 x v 表示x位置的数变成v
 * 你可以选择不执行任何操作，或者只选择一个操作来执行，然后arr不再变动
 * 请在arr中选出一组下标序列，不管你做出什么选择，下标序列所代表的数字都是不下降的
 * 打印序列能达到的最大长度
 * 1 <= 所有数字 <= 10^5
 * 
 * 解题思路:
 * 这是一个动态规划优化问题，可以使用CDQ分治来解决。
 * 
 * 问题分析:
 * 我们需要找到一个最长不降子序列，但有一个特殊条件：
 * 每个位置的值可能在一定范围内变化（由操作决定），我们需要找到无论值如何变化
 * 都能保持不降性质的最长子序列。
 * 
 * 算法步骤:
 * 1. 首先预处理每个位置的值的变化范围：
 *    - lv[i]表示位置i的最小可能值
 *    - rv[i]表示位置i的最大可能值
 * 2. 定义dp[i]表示以位置i结尾的最长不降子序列长度
 * 3. 使用CDQ分治优化DP转移：
 *    - 将区间[l,r]分成两部分[l,mid]和[mid+1,r]
 *    - 递归处理左半部分和右半部分
 *    - 计算左半部分对右半部分的贡献
 * 4. 在合并过程中：
 *    - 对左半部分按照值v排序
 *    - 对右半部分按照最小值lv排序
 *    - 使用双指针维护满足条件的窗口
 *    - 使用树状数组维护最大值信息，查询满足条件的dp值
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

public class Code03_Sequence1 {

	public static int MAXN = 100001;
	public static int n, m;
	public static int[] v = new int[MAXN];
	public static int[] lv = new int[MAXN];
	public static int[] rv = new int[MAXN];

	// 位置i、数值v、最小值lv、最大值rv
	public static int[][] arr = new int[MAXN][4];
	// 树状数组维护前缀最大值
	public static int[] tree = new int[MAXN];
	public static int[] dp = new int[MAXN];

	public static int lowbit(int i) {
		return i & -i;
	}

	public static void more(int i, int num) {
		while (i <= n) {
			tree[i] = Math.max(tree[i], num);
			i += lowbit(i);
		}
	}

	public static int query(int i) {
		int ret = 0;
		while (i > 0) {
			ret = Math.max(ret, tree[i]);
			i -= lowbit(i);
		}
		return ret;
	}

	public static void clear(int i) {
		while (i <= n) {
			tree[i] = 0;
			i += lowbit(i);
		}
	}

	public static void merge(int l, int m, int r) {
		// 辅助数组arr拷贝l..r所有的对象
		// 接下来的排序都发生在arr中，不影响原始的次序
		for (int i = l; i <= r; i++) {
			arr[i][0] = i;
			arr[i][1] = v[i];
			arr[i][2] = lv[i];
			arr[i][3] = rv[i];
		}
		// 左侧根据v排序
		Arrays.sort(arr, l, m + 1, (a, b) -> a[1] - b[1]);
		// 右侧根据lv排序
		Arrays.sort(arr, m + 1, r + 1, (a, b) -> a[2] - b[2]);
		int p1, p2;
		for (p1 = l - 1, p2 = m + 1; p2 <= r; p2++) {
			// 左侧对象.v <= 右侧对象.lv 窗口扩充
			while (p1 + 1 <= m && arr[p1 + 1][1] <= arr[p2][2]) {
				p1++;
				// 树状数组中，下标是rv，加入的值是左侧对象的dp值
				more(arr[p1][3], dp[arr[p1][0]]);
			}
			// 右侧对象更新dp值，查出1..v范围上最大的dp值 + 1
			dp[arr[p2][0]] = Math.max(dp[arr[p2][0]], query(arr[p2][1]) + 1);
		}
		// 清空树状数组
		for (int i = l; i <= p1; i++) {
			clear(arr[i][3]);
		}
	}

	public static void cdq(int l, int r) {
		if (l == r) {
			return;
		}
		int mid = (l + r) / 2;
		cdq(l, mid);
		merge(l, mid, r);
		cdq(mid + 1, r);
	}

	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		for (int i = 1; i <= n; i++) {
			v[i] = in.nextInt();
			lv[i] = v[i];
			rv[i] = v[i];
		}
		for (int i = 1, idx, val; i <= m; i++) {
			idx = in.nextInt();
			val = in.nextInt();
			lv[idx] = Math.min(lv[idx], val);
			rv[idx] = Math.max(rv[idx], val);
		}
		for (int i = 1; i <= n; i++) {
			dp[i] = 1;
		}
		cdq(1, n);
		int ans = 0;
		for (int i = 1; i <= n; i++) {
			ans = Math.max(ans, dp[i]);
		}
		out.println(ans);
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