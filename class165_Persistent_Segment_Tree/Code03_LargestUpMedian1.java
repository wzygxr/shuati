package class158;

/**
 * 浮动区间的最大上中位数，java版
 * 
 * 题目来源：洛谷 P2839 - Middle
 * 题目链接：https://www.luogu.com.cn/problem/P2839
 * 
 * 题目描述:
 * 为了方便理解，改写题意（与原始题意等效）：
 * 给定一个长度为n的数组arr，下标1~n，一共有m条查询
 * 每条查询 a b c d : 左端点在[a,b]之间、右端点在[c,d]之间，保证a<b<c<d
 *                   哪个区间有最大的上中位数，打印最大的上中位数
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）结合二分答案解决该问题。
 * 1. 对数组元素进行离散化处理
 * 2. 按照元素值从小到大排序，建立主席树
 * 3. 对于每个查询，二分答案，判断是否存在满足条件的区间
 * 4. 利用线段树维护前缀和、前缀最大值、后缀最大值等信息
 * 
 * 强制在线处理:
 * 题目有强制在线的要求，上一次打印的答案为lastAns，初始时lastAns = 0
 * 每次给定四个参数，按照如下方式得到a、b、c、d，查询完成后更新lastAns
 * (给定的每个参数 + lastAns) % n + 1，得到四个值，从小到大对应a、b、c、d
 * 
 * 时间复杂度: O(n log²n + m log²n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 4
 * 1 2 3 4
 * 1
 * 1 2 3 4
 * 
 * 输出:
 * 3
 * 
 * 解释:
 * 查询[1,2,3,4]：左端点在[1,2]之间，右端点在[3,4]之间
 * 可能的区间有[1,3],[1,4],[2,3],[2,4]
 * 对应的上中位数分别为2,2,3,3，最大值为3
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code03_LargestUpMedian1 {

	public static int MAXN = 20001;

	public static int MAXT = MAXN * 20;

	public static int INF = 10000001;

	public static int n, m;

	// 原始位置、数值
	public static int[][] arr = new int[MAXN][2];

	// 可持久化线段树需要
	public static int[] root = new int[MAXN];

	public static int[] left = new int[MAXT];

	public static int[] right = new int[MAXT];

	// 区间内最大前缀和，前缀不能为空
	public static int[] pre = new int[MAXT];

	// 区间内最大后缀和，后缀不能为空
	public static int[] suf = new int[MAXT];

	// 区间内累加和
	public static int[] sum = new int[MAXT];

	public static int cnt;

	// 查询的问题，a、b、c、d
	public static int[] ques = new int[4];

	// 收集区间信息，pre、suf、sum
	public static int[] info = new int[3];

	/**
	 * 构建空线段树
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @return 根节点编号
	 */
	public static int build(int l, int r) {
		int rt = ++cnt;
		pre[rt] = suf[rt] = sum[rt] = r - l + 1;
		if (l < r) {
			int mid = (l + r) / 2;
			left[rt] = build(l, mid);
			right[rt] = build(mid + 1, r);
		}
		return rt;
	}

	/**
	 * 向上更新节点信息
	 * @param i 节点编号
	 */
	public static void up(int i) {
		// 最大前缀和 = max(左子树最大前缀和, 左子树和 + 右子树最大前缀和)
		pre[i] = Math.max(pre[left[i]], sum[left[i]] + pre[right[i]]);
		// 最大后缀和 = max(右子树最大后缀和, 右子树和 + 左子树最大后缀和)
		suf[i] = Math.max(suf[right[i]], suf[left[i]] + sum[right[i]]);
		// 区间和 = 左子树和 + 右子树和
		sum[i] = sum[left[i]] + sum[right[i]];
	}

	/**
	 * 更新线段树节点
	 * @param jobi 要更新的位置
	 * @param l 当前区间左端点
	 * @param r 当前区间右端点
	 * @param i 前一个版本的节点编号
	 * @return 新版本的根节点编号
	 */
	public static int update(int jobi, int l, int r, int i) {
		int rt = ++cnt;
		left[rt] = left[i];
		right[rt] = right[i];
		pre[rt] = pre[i];
		suf[rt] = suf[i];
		sum[rt] = sum[i];
		if (l == r) {
			// 将位置jobi的值从1改为-1
			pre[rt] = suf[rt] = sum[rt] = -1;
		} else {
			int mid = (l + r) / 2;
			if (jobi <= mid) {
				left[rt] = update(jobi, l, mid, left[rt]);
			} else {
				right[rt] = update(jobi, mid + 1, r, right[rt]);
			}
			up(rt);
		}
		return rt;
	}

	/**
	 * 初始化info数组
	 */
	public static void initInfo() {
		info[0] = info[1] = -INF;
		info[2] = 0;
	}

	/**
	 * 合并右侧区间信息
	 * @param r 右侧区间节点编号
	 */
	public static void mergeRight(int r) {
		// 更新最大前缀和
		info[0] = Math.max(info[0], info[2] + pre[r]);
		// 更新最大后缀和
		info[1] = Math.max(suf[r], info[1] + sum[r]);
		// 更新区间和
		info[2] += sum[r];
	}

	/**
	 * 查询区间[jobl,jobr]的信息
	 * @param jobl 查询区间左端点
	 * @param jobr 查询区间右端点
	 * @param l 当前区间左端点
	 * @param r 当前区间右端点
	 * @param i 当前节点编号
	 */
	public static void query(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			mergeRight(i);
		} else {
			int mid = (l + r) / 2;
			if (jobl <= mid) {
				query(jobl, jobr, l, mid, left[i]);
			}
			if (jobr > mid) {
				query(jobl, jobr, mid + 1, r, right[i]);
			}
		}
	}

	/**
	 * 预处理，建立主席树
	 */
	public static void prepare() {
		// 按照数值从小到大排序
		Arrays.sort(arr, 1, n + 1, (a, b) -> a[1] - b[1]);
		cnt = 0;
		root[1] = build(1, n);
		for (int i = 2; i <= n; i++) {
			// 将位置arr[i-1][0]的值从1改为-1
			root[i] = update(arr[i - 1][0], 1, n, root[i - 1]);
		}
	}

	/**
	 * 检查是否存在满足条件的区间，其上中位数大于等于v
	 * @param a 左端点下界
	 * @param b 左端点上界
	 * @param c 右端点下界
	 * @param d 右端点上界
	 * @param v 要检查的上中位数值
	 * @return 是否存在满足条件的区间
	 */
	public static boolean check(int a, int b, int c, int d, int v) {
		initInfo();
		// 查询[a,b]区间的信息
		query(a, b, 1, n, root[v]);
		int best = info[1];
		initInfo();
		// 查询[c,d]区间的信息
		query(c, d, 1, n, root[v]);
		best += info[0];
		if (b + 1 <= c - 1) {
			initInfo();
			// 查询[b+1,c-1]区间的信息
			query(b + 1, c - 1, 1, n, root[v]);
			best += info[2];
		}
		// 如果best >= 0，说明存在满足条件的区间
		return best >= 0;
	}

	/**
	 * 计算查询[a,b,c,d]的最大上中位数
	 * @param a 左端点下界
	 * @param b 左端点上界
	 * @param c 右端点下界
	 * @param d 右端点上界
	 * @return 最大上中位数
	 */
	public static int compute(int a, int b, int c, int d) {
		int left = 1, right = n, mid, ans = 0;
		// 二分答案
		while (left <= right) {
			mid = (left + right) / 2;
			if (check(a, b, c, d, mid)) {
				// 如果存在满足条件的区间，更新答案并继续向右查找
				ans = arr[mid][1];
				left = mid + 1;
			} else {
				// 否则向左查找
				right = mid - 1;
			}
		}
		return ans;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		for (int i = 1; i <= n; i++) {
			arr[i][0] = i;
			in.nextToken();
			arr[i][1] = (int) in.nval;
		}
		prepare();
		in.nextToken();
		m = (int) in.nval;
		for (int i = 1, lastAns = 0; i <= m; i++) {
			in.nextToken();
			ques[0] = ((int) in.nval + lastAns) % n + 1;
			in.nextToken();
			ques[1] = ((int) in.nval + lastAns) % n + 1;
			in.nextToken();
			ques[2] = ((int) in.nval + lastAns) % n + 1;
			in.nextToken();
			ques[3] = ((int) in.nval + lastAns) % n + 1;
			// 对四个值进行排序
			Arrays.sort(ques);
			// 计算最大上中位数
			lastAns = compute(ques[0], ques[1], ques[2], ques[3]);
			out.println(lastAns);
		}
		out.flush();
		out.close();
		br.close();
	}

}