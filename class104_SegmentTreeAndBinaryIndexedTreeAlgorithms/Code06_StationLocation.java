package class131;

/** 
 * 洛谷 P2605 [JLOI2011]基站选址
 * 题目链接: https://www.luogu.com.cn/problem/P2605
 * 
 * 题目描述:
 * 一共有n个村庄排成一排，从左往右依次出现1号、2号、3号..n号村庄
 * dist[i]表示i号村庄到1号村庄的距离，该数组一定有序且无重复值
 * fix[i]表示i号村庄建立基站的安装费用
 * range[i]表示i号村庄的接收范围，任何基站和i号村庄的距离不超过这个数字，i号村庄就能得到服务
 * warranty[i]表示如果i号村庄最终没有得到任何基站的服务，需要给多少赔偿费用
 * 最多可以选择k个村庄安装基站，返回总花费最少是多少，总花费包括安装费用和赔偿费用
 * 
 * 解题思路:
 * 使用线段树优化动态规划的方法解决此问题。
 * 1. 定义状态dp[t][i]表示最多建t个基站，并且最右的基站一定要建在i号村庄，1..i号村庄的最少花费
 * 2. 由于dp[t][i]只依赖dp[t-1][..]，所以能空间压缩变成一维数组
 * 3. 对于每个村庄，预处理其能被服务的最左和最右基站位置
 * 4. 使用链式前向星存储预警列表，当基站从位置i移动到i+1时，哪些村庄会失去服务
 * 5. 使用线段树维护dp值，支持区间加法和区间查询最小值
 * 
 * 时间复杂度分析:
 * - 预处理: O(n log n)
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

public class Code06_StationLocation {

	public static int n, k;

	// 因为要补充一个村庄(无穷远处)，所以村庄编号1~20001，那么空间为20002
	public static int MAXN = 20002;

	// 和1号村庄之间的距离
	public static int[] dist = new int[MAXN];

	// 安装费用
	public static int[] fix = new int[MAXN];

	// 接收范围
	public static int[] range = new int[MAXN];

	// 赔偿费用
	public static int[] warranty = new int[MAXN];

	// left[i]表示最左在第几号村庄建基站，i号村庄依然能获得服务
	public static int[] left = new int[MAXN];

	// right[i]表示最右在第几号村庄建基站，i号村庄依然能获得服务
	public static int[] right = new int[MAXN];

	// 链式前向星
	// 保存每个村庄的预警列表，i号村庄的预警列表是指
	// 如果只有一个基站建在i号村庄，现在这个基站要移动到i+1号村庄
	// 哪些村庄会从有服务变成无服务的状态
	public static int[] head = new int[MAXN];

	public static int[] next = new int[MAXN];

	public static int[] to = new int[MAXN];

	public static int cnt;

	// 线段树维护最小值信息
	public static int[] min = new int[MAXN << 2];

	// 线段树维护加的懒更新
	public static int[] add = new int[MAXN << 2];

	// 动态规划表
	// dp[t][i]表示最多建t个基站，并且最右的基站一定要建在i号村庄，1..i号村庄的最少花费
	// 因为dp[t][i]，只依赖dp[t-1][..]，所以能空间压缩变成一维数组
	public static int[] dp = new int[MAXN];

	/** 
	 * 主函数，读取输入并输出结果
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		k = (int) in.nval;
		for (int i = 2; i <= n; i++) {
			in.nextToken();
			dist[i] = (int) in.nval;
		}
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			fix[i] = (int) in.nval;
		}
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			range[i] = (int) in.nval;
		}
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			warranty[i] = (int) in.nval;
		}
		// 补充了一个村庄，认为在无穷远的位置，其他数据都是0
		dist[++n] = Integer.MAX_VALUE;
		fix[n] = range[n] = warranty[n] = 0;
		prepare();
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

	/** 
	 * 计算基站选址的最小花费
	 * n是加上补充村庄(无穷远处)之后的村子数量
	 * 所以dp[t][n]的值代表
	 * 最右有一个单独的基站，去负责补充村庄，这一部分的花费是0
	 * 剩余有最多t-1个基站，去负责真实出现的村庄，最少的总费用
	 * 所以t一定要从1枚举到k+1，对应真实村子最多分到0个~k个基站的情况
	 * 这么做可以减少边界讨论，课上进行了图解
	 * 
	 * @return 最小花费
	 */
	public static int compute() {
		// 最多建t=1个基站的情况
		for (int i = 1, w = 0; i <= n; i++) {
			dp[i] = w + fix[i];
			for (int ei = head[i]; ei != 0; ei = next[ei]) {
				w += warranty[to[ei]];
			}
		}
		// 最多建t>=2个基站的情况
		for (int t = 2; t <= k + 1; t++) {
			build(1, n, 1);
			for (int i = 1; i <= n; i++) {
				if (i >= t) {
					dp[i] = Math.min(dp[i], query(1, i - 1, 1, n, 1) + fix[i]);
				}
				for (int ei = head[i], uncover; ei != 0; ei = next[ei]) {
					uncover = to[ei];
					if (left[uncover] > 1) {
						add(1, left[uncover] - 1, warranty[uncover], 1, n, 1);
					}
				}
			}
		}
		return dp[n];
	}

	/** 
	 * 预处理函数
	 * 生成left[0..n]和right[0..n]
	 * 生成预警[0..n]
	 */
	public static void prepare() {
		cnt = 1;
		for (int i = 1; i <= n; i++) {
			// 计算i号村庄能被服务的最左基站位置
			left[i] = search(dist[i] - range[i]);
			// 计算i号村庄能被服务的最右基站位置
			right[i] = search(dist[i] + range[i]);
			if (dist[right[i]] > dist[i] + range[i]) {
				// 如果if逻辑命中
				// 说明此时right[i]上建基站，其实i号村庄是收不到信号的
				// 此时right[i]要减1
				right[i]--;
			}
			// 生成预警列表
			// 比如right[3] = 17
			// 那么17号村庄的预警列表里有3
			addEdge(right[i], i);
		}
	}

	/** 
	 * 在dist数组中二分查找>=d的最左位置
	 * 
	 * @param d 目标距离
	 * @return  最左位置
	 */
	public static int search(int d) {
		int l = 1, r = n, m;
		int ans = 0;
		while (l <= r) {
			m = (l + r) / 2;
			if (dist[m] >= d) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}

	/** 
	 * 链式前向星加边
	 * 其实就是u的预警列表里增加v
	 * 
	 * @param u 起点村庄
	 * @param v 终点村庄
	 */
	public static void addEdge(int u, int v) {
		next[cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt++;
	}

	/** 
	 * 线段树向上更新操作
	 * 更新父节点的值为左右子节点的最小值
	 * 
	 * @param i 线段树节点索引
	 */
	public static void up(int i) {
		min[i] = Math.min(min[i << 1], min[i << 1 | 1]);
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
		// 更新节点最小值
		min[i] += v;
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
			// 叶节点存储dp[l]的值
			min[i] = dp[l];
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
	 * 查询区间[jobl, jobr]内的最小值
	 * 
	 * @param jobl 查询区间左边界
	 * @param jobr 查询区间右边界
	 * @param l    当前区间左边界
	 * @param r    当前区间右边界
	 * @param i    当前线段树节点索引
	 * @return     区间最小值
	 */
	public static int query(int jobl, int jobr, int l, int r, int i) {
		// 当前区间完全包含在查询区间内
		if (jobl <= l && r <= jobr) {
			return min[i];
		}
		// 下传懒惰标记
		down(i);
		// 计算中点
		int mid = (l + r) >> 1;
		int ans = Integer.MAX_VALUE;
		// 递归查询左子树
		if (jobl <= mid) {
			ans = Math.min(ans, query(jobl, jobr, l, mid, i << 1));
		}
		// 递归查询右子树
		if (jobr > mid) {
			ans = Math.min(ans, query(jobl, jobr, mid + 1, r, i << 1 | 1));
		}
		return ans;
	}

}