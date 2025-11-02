package class157;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

/**
 * 单点修改的可持久化线段树模版题2，java版
 * 
 * 题目来源: 洛谷 P3834 【模板】可持久化线段树2
 * 题目链接: https://www.luogu.com.cn/problem/P3834
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，下标1~n，一共有m条查询
 * 每条查询 l r k : 打印arr[l..r]中第k小的数字
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决静态区间第K小问题。
 * 1. 对数组元素进行离散化处理，将大范围的值映射到小范围的排名
 * 2. 对于每个位置i，建立一个线段树版本，维护前i个元素中每个排名的出现次数
 * 3. 利用前缀和的思想，通过两个版本的线段树相减得到区间信息
 * 4. 在线段树上二分查找第K小的元素
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 1 <= n、m <= 2 * 10^5
 * 0 <= arr[i] <= 10^9
 * 
 * 示例:
 * 输入:
 * 5 5
 * 25957 6405 15770 26287 6556
 * 2 2 1
 * 3 4 1
 * 4 5 1
 * 1 2 2
 * 4 4 1
 * 
 * 输出:
 * 6405
 * 15770
 * 26287
 * 25957
 * 26287
 */
public class Code02_PointPersistent1 {

	public static int MAXN = 200001;

	public static int MAXT = MAXN * 22;

	public static int n, m, s;

	// 原始数组
	public static int[] arr = new int[MAXN];

	// 收集权值排序并且去重做离散化
	public static int[] sorted = new int[MAXN];

	// 可持久化线段树需要
	// root[i] : 插入arr[i]之后形成新版本的线段树，记录头节点编号
	// 0号版本的线段树代表一个数字也没有时，每种名次的数字出现的次数
	// i号版本的线段树代表arr[1..i]范围内，每种名次的数字出现的次数
	public static int[] root = new int[MAXN];

	public static int[] left = new int[MAXT];

	public static int[] right = new int[MAXT];

	// 排名范围内收集了多少个数字
	public static int[] size = new int[MAXT];

	public static int cnt;

	/**
	 * 返回num在所有值中排名多少
	 * @param num 要查询排名的数值
	 * @return num的排名
	 */
	public static int kth(int num) {
		int left = 1, right = s, mid, ans = 0;
		while (left <= right) {
			mid = (left + right) / 2;
			if (sorted[mid] <= num) {
				ans = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return ans;
	}

	/**
	 * 排名范围l~r，建立线段树，返回头节点编号
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @return 头节点编号
	 */
	public static int build(int l, int r) {
		int rt = ++cnt;
		size[rt] = 0;
		if (l < r) {
			int mid = (l + r) / 2;
			left[rt] = build(l, mid);
			right[rt] = build(mid + 1, r);
		}
		return rt;
	}

	/**
	 * 排名范围l~r，信息在i号节点，增加一个排名为jobi的数字
	 * 返回新的头节点编号
	 * @param jobi 要插入的数字的排名
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param i 当前节点编号
	 * @return 新节点编号
	 */
	public static int insert(int jobi, int l, int r, int i) {
		int rt = ++cnt;
		left[rt] = left[i];
		right[rt] = right[i];
		size[rt] = size[i] + 1;
		if (l < r) {
			int mid = (l + r) / 2;
			if (jobi <= mid) {
				left[rt] = insert(jobi, l, mid, left[rt]);
			} else {
				right[rt] = insert(jobi, mid + 1, r, right[rt]);
			}
		}
		return rt;
	}

	/**
	 * 排名范围l~r，老版本信息在u号节点，新版本信息在v号节点
	 * 返回，第jobk小的数字，排名多少
	 * @param jobk 要查询的第几小
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param u 老版本节点编号
	 * @param v 新版本节点编号
	 * @return 第jobk小的数字的排名
	 */
	public static int query(int jobk, int l, int r, int u, int v) {
		if (l == r) {
			return l;
		}
		int lsize = size[left[v]] - size[left[u]];
		int mid = (l + r) / 2;
		if (lsize >= jobk) {
			return query(jobk, l, mid, left[u], left[v]);
		} else {
			return query(jobk - lsize, mid + 1, r, right[u], right[v]);
		}
	}

	/**
	 * 权值做离散化并且去重 + 生成各版本的线段树
	 */
	public static void prepare() {
		cnt = 0;
		for (int i = 1; i <= n; i++) {
			sorted[i] = arr[i];
		}
		Arrays.sort(sorted, 1, n + 1);
		s = 1;
		for (int i = 2; i <= n; i++) {
			if (sorted[s] != sorted[i]) {
				sorted[++s] = sorted[i];
			}
		}
		root[0] = build(1, s);
		for (int i = 1, x; i <= n; i++) {
			x = kth(arr[i]);
			root[i] = insert(x, 1, s, root[i - 1]);
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		prepare();
		for (int i = 1, l, r, k, rank; i <= m; i++) {
			in.nextToken();
			l = (int) in.nval;
			in.nextToken();
			r = (int) in.nval;
			in.nextToken();
			k = (int) in.nval;
			rank = query(k, 1, s, root[l - 1], root[r]);
			out.println(sorted[rank]);
		}
		out.flush();
		out.close();
		br.close();
	}

}