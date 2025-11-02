package class157;

import java.io.*;

/**
 * 单点修改的可持久化线段树模版题1，java版
 * 
 * 题目来源: 洛谷 P3919 【模板】可持久化线段树1（可持久化数组）
 * 题目链接: https://www.luogu.com.cn/problem/P3919
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，下标1~n，原始数组认为是0号版本
 * 一共有m条操作，每条操作是如下两种类型中的一种
 * v 1 x y : 基于v号版本的数组，把x位置的值设置成y，生成新版本的数组
 * v 2 x   : 基于v号版本的数组，打印x位置的值，生成新版本的数组和v版本一致
 * 每条操作后得到的新版本数组，版本编号为操作的计数
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决可持久化数组问题。
 * 1. 对于每次修改操作，只创建被修改路径上的新节点，共享未修改的部分
 * 2. 对于查询操作，直接在对应版本的线段树上查询
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 1 <= n, m <= 10^6
 * 
 * 示例:
 * 输入:
 * 5 10
 * 59 64 65 97 51
 * 0 1 1 10
 * 0 2 2 20
 * 0 3 3 30
 * 0 4 4 40
 * 0 5 5 50
 * 1 2 1 100
 * 1 2 2 200
 * 1 2 3 300
 * 1 2 4 400
 * 1 2 5 500
 * 
 * 输出:
 * 10
 * 20
 * 30
 * 40
 * 50
 * 100
 * 200
 * 300
 * 400
 * 500
 */
public class Code01_PointPersistent2 {

	public static int MAXN = 1000001;

	public static int MAXT = MAXN * 23;

	public static int n, m;

	// 原始数组
	public static int[] arr = new int[MAXN];

	// 可持久化线段树需要
	// root[i] : i号版本线段树的头节点编号
	public static int[] root = new int[MAXN];

	public static int[] left = new int[MAXT];

	public static int[] right = new int[MAXT];

	// value[i] : 节点i的值信息，只有叶节点有这个信息
	public static int[] value = new int[MAXT];

	// 可持久化线段树的节点空间计数
	public static int cnt = 0;

	/**
	 * 建树，返回头节点编号
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @return 头节点编号
	 */
	public static int build(int l, int r) {
		int rt = ++cnt;
		if (l == r) {
			value[rt] = arr[l];
		} else {
			int mid = (l + r) >> 1;
			left[rt] = build(l, mid);
			right[rt] = build(mid + 1, r);
		}
		return rt;
	}

	/**
	 * 线段树范围l~r，信息在i号节点里
	 * 在l~r范围上，jobi位置的值，设置成jobv
	 * 生成的新节点编号返回
	 * @param jobi 要修改的位置
	 * @param jobv 要设置的值
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param i 当前节点编号
	 * @return 新节点编号
	 */
	public static int update(int jobi, int jobv, int l, int r, int i) {
		int rt = ++cnt;
		left[rt] = left[i];
		right[rt] = right[i];
		value[rt] = value[i];
		if (l == r) {
			value[rt] = jobv;
		} else {
			int mid = (l + r) >> 1;
			if (jobi <= mid) {
				left[rt] = update(jobi, jobv, l, mid, left[rt]);
			} else {
				right[rt] = update(jobi, jobv, mid + 1, r, right[rt]);
			}
		}
		return rt;
	}

	/**
	 * 线段树范围l~r，信息在i号节点里
	 * 返回l~r范围上jobi位置的值
	 * @param jobi 要查询的位置
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param i 当前节点编号
	 * @return 位置jobi的值
	 */
	public static int query(int jobi, int l, int r, int i) {
		if (l == r) {
			return value[i];
		}
		int mid = (l + r) >> 1;
		if (jobi <= mid) {
			return query(jobi, l, mid, left[i]);
		} else {
			return query(jobi, mid + 1, r, right[i]);
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
		
		String[] line = reader.readLine().split(" ");
		n = Integer.parseInt(line[0]);
		m = Integer.parseInt(line[1]);
		
		line = reader.readLine().split(" ");
		for (int i = 1; i <= n; i++) {
			arr[i] = Integer.parseInt(line[i - 1]);
		}
		
		root[0] = build(1, n);
		
		for (int i = 1, version, op, x, y; i <= m; i++) {
			line = reader.readLine().split(" ");
			version = Integer.parseInt(line[0]);
			op = Integer.parseInt(line[1]);
			x = Integer.parseInt(line[2]);
			
			if (op == 1) {
				y = Integer.parseInt(line[3]);
				root[i] = update(x, y, 1, n, root[version]);
			} else {
				root[i] = root[version];
				writer.println(query(x, 1, n, root[i]));
			}
		}
		
		writer.flush();
		writer.close();
		reader.close();
	}

}