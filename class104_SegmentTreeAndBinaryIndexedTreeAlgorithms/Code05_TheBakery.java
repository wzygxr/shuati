package class131;

/** 
 * Codeforces 833B The Bakery
 * 题目链接: https://codeforces.com/problemset/problem/833/B
 * 洛谷链接: https://www.luogu.com.cn/problem/CF833B
 * 
 * 题目描述:
 * 给定一个长度为n的数组，最多可以分成k段不重合的子数组
 * 每个子数组获得的分值为内部不同数字的个数
 * 返回能获得的最大分值。
 * 
 * 解题思路:
 * 使用线段树优化动态规划的方法解决此问题。
 * 1. 定义状态dp[i][j]表示将前j个元素分成i段的最大得分
 * 2. 状态转移方程：dp[i][j] = max{dp[i-1][k] + cost(k+1, j)}，其中k < j
 *    cost(k+1, j)表示区间[k+1, j]内不同数字的个数
 * 3. 使用线段树维护dp[i-1][k]的值，支持区间加法和区间查询最大值
 * 4. 对于每个新元素，更新其对之前所有位置的影响
 * 
 * 时间复杂度分析:
 * - 状态转移: O(k*n*log n)
 * - 总时间复杂度: O(k*n*log n)
 * 空间复杂度: O(n) 用于存储线段树和辅助数组
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code05_TheBakery {

	// 最大数组长度常量
	public static int MAXN = 35001;

	// 全局变量
	public static int n, k;  // n:数组长度, k:最大段数

	// 输入数组
	public static int[] arr = new int[MAXN];

	// 动态规划数组，dp[i]表示以第i个元素结尾的最大得分
	public static int[] dp = new int[MAXN];

	// 记录每个数字上一次出现的位置
	public static int[] pre = new int[MAXN];

	// 线段树数组
	public static int[] max = new int[MAXN << 2];  // 存储区间最大值
	public static int[] add = new int[MAXN << 2];  // 存储懒惰标记

	/** 
	 * 主函数，读取输入并输出结果
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 使用BufferedReader提高输入效率
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取数组长度n和最大段数k
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		k = (int) in.nval;
		
		// 读取数组元素
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		
		// 计算并输出结果
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

	/** 
	 * 计算划分k段的最大得分
	 * 注意本题的线段树范围不是1~n，而是0~n
	 * 因为线段树需要维护0号~n号指标
	 * 
	 * @return 最大得分
	 */
	public static int compute() {
		// 初始化dp数组
		Arrays.fill(dp, 1, n + 1, 0);
		
		// 枚举段数
		for (int t = 1; t <= k; t++) {
			// 构建线段树
			build(0, n, 1);
			// 初始化pre数组
			Arrays.fill(pre, 1, n + 1, 0);
			
			// 遍历数组元素
			for (int i = 1; i <= n; i++) {
				// 区间加法操作，更新pre[arr[i]]到i-1范围内的值
				add(pre[arr[i]], i - 1, 1, 0, n, 1);
				
				// 如果当前位置可以形成t段
				if (i >= t) {
					// 查询0到i-1范围内的最大值
					dp[i] = query(0, i - 1, 0, n, 1);
				}
				
				// 更新当前数字的上一次出现位置
				pre[arr[i]] = i;
			}
		}
		
		return dp[n];
	}

	/** 
	 * 线段树向上更新操作
	 * 更新父节点的值为左右子节点的最大值
	 * 
	 * @param i 线段树节点索引
	 */
	public static void up(int i) {
		max[i] = Math.max(max[i << 1], max[i << 1 | 1]);
	}

	/** 
	 * 线段树懒惰标记下传操作
	 * 
	 * @param i 线段树节点索引
	 */
	public static void down(int i) {
		if (add[i] != 0) {
			// 将懒惰标记下传给左右子节点
			lazy(i << 1, add[i]);
			lazy(i << 1 | 1, add[i]);
			// 清空当前节点的懒惰标记
			add[i] = 0;
		}
	}

	/** 
	 * 线段树懒惰标记操作
	 * 
	 * @param i 线段树节点索引
	 * @param v 要增加的值
	 */
	public static void lazy(int i, int v) {
		// 更新节点最大值
		max[i] += v;
		// 更新懒惰标记
		add[i] += v;
	}

	/** 
	 * 线段树构建操作
	 * 
	 * @param l 当前区间左边界
	 * @param r 当前区间右边界
	 * @param i 当前线段树节点索引
	 */
	public static void build(int l, int r, int i) {
		if (l == r) {
			// 用dp[0..n]来build线段树
			// 叶节点存储dp[l]的值
			max[i] = dp[l];
		} else {
			// 计算中点
			int mid = (l + r) >> 1;
			// 递归构建左右子树
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
			// 向上更新父节点
			up(i);
		}
		// 初始化懒惰标记
		add[i] = 0;
	}

	/** 
	 * 线段树区间加法操作
	 * 
	 * @param jobl 操作区间左边界
	 * @param jobr 操作区间右边界
	 * @param jobv 要增加的值
	 * @param l    当前区间左边界
	 * @param r    当前区间右边界
	 * @param i    当前线段树节点索引
	 */
	public static void add(int jobl, int jobr, int jobv, int l, int r, int i) {
		// 当前区间完全包含在操作区间内
		if (jobl <= l && r <= jobr) {
			lazy(i, jobv);
		} else {
			// 下传懒惰标记
			down(i);
			// 计算中点
			int mid = (l + r) >> 1;
			// 递归更新左子树
			if (jobl <= mid) {
				add(jobl, jobr, jobv, l, mid, i << 1);
			}
			// 递归更新右子树
			if (jobr > mid) {
				add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
			// 向上更新父节点
			up(i);
		}
	}

	/** 
	 * 线段树区间查询操作
	 * 查询区间[jobl, jobr]内的最大值
	 * 
	 * @param jobl 查询区间左边界
	 * @param jobr 查询区间右边界
	 * @param l    当前区间左边界
	 * @param r    当前区间右边界
	 * @param i    当前线段树节点索引
	 * @return     区间最大值
	 */
	public static int query(int jobl, int jobr, int l, int r, int i) {
		// 当前区间完全包含在查询区间内
		if (jobl <= l && r <= jobr) {
			return max[i];
		}
		
		// 下传懒惰标记
		down(i);
		
		// 计算中点
		int mid = (l + r) >> 1;
		int ans = Integer.MIN_VALUE;
		
		// 递归查询左子树
		if (jobl <= mid) {
			ans = Math.max(ans, query(jobl, jobr, l, mid, i << 1));
		}
		
		// 递归查询右子树
		if (jobr > mid) {
			ans = Math.max(ans, query(jobl, jobr, mid + 1, r, i << 1 | 1));
		}
		
		return ans;
	}

}