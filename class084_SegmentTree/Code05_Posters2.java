package class111;

import java.io.*;
import java.util.Arrays;

// 贴海报(数据加强版) (Posters Enhanced Version)
// 题目来源: POJ 2528. Mayor's posters
// 题目链接: http://poj.org/problem?id=2528
// 
// 题目描述:
// 有一面墙，有固定高度，无限的宽度，有n张海报，所有海报的高度都和墙的高度相同
// 从第1张海报开始，一张一张往墙上贴，直到n张海报贴完
// 每张海报都给出张贴位置(xi, yi)，表示第i张海报从墙的左边界xi一直延伸到右边界yi
// 有可能发生后面的海报把前面的海报完全覆盖，导致看不到的情况
// 当所有海报贴完，返回能看到海报的数量，哪怕只漏出一点的海报都算
// 1 <= n <= 10^5，1 <= xi、yi <= 10^7
//
// 解题思路:
// 1. 使用线段树维护区间覆盖情况，记录每个区间被哪张海报完全覆盖
// 2. 由于坐标范围很大，需要进行离散化处理
// 3. 从第一张海报开始依次张贴，每次更新对应区间的海报编号
// 4. 最后查询整个墙面上可见的海报数量
// 5. 利用懒惰传播优化区间更新操作
// 6. 与Code05_Posters1.java相比，支持多组测试用例
//
// 时间复杂度: O(n log n)，其中n为海报数量
// 空间复杂度: O(n)
public class Code05_Posters2 {

	public static int MAXN = 10001;

	public static int[] pl = new int[MAXN];

	public static int[] pr = new int[MAXN];

	public static int[] num = new int[MAXN << 2];

	// 线段树的某个范围上是否被设置成了统一的海报
	// 如果poster[i] != 0，poster[i]表示统一海报的编号
	// 如果poster[i] == 0，表示该范围上没有海报或者海报编号没统一
	public static int[] poster = new int[MAXN << 4];

	// 某种海报编号是否已经被统计过了
	// 只在最后一次查询，最后统计海报数量的阶段时候使用
	public static boolean[] visited = new boolean[MAXN];

	// 收集所有坐标点并进行离散化处理
	public static int collect(int m) {
		Arrays.sort(num, 1, m + 1);
		int size = 1;
		for (int i = 2; i <= m; i++) {
			if (num[size] != num[i]) {
				num[++size] = num[i];
			}
		}
		int cnt = size;
		for (int i = 2; i <= size; i++) {
			if (num[i - 1] + 1 < num[i]) {
				num[++cnt] = num[i - 1] + 1;
			}
		}
		Arrays.sort(num, 1, cnt + 1);
		return cnt;
	}

	// 二分查找值v在离散化数组中的位置
	public static int rank(int l, int r, int v) {
		int m;
		int ans = 0;
		while (l <= r) {
			m = (l + r) >> 1;
			if (num[m] >= v) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}

	// 下发懒惰标记
	public static void down(int i) {
		if (poster[i] != 0) {
			poster[i << 1] = poster[i];
			poster[i << 1 | 1] = poster[i];
			poster[i] = 0;
		}
	}

	// 构建线段树
	public static void build(int l, int r, int i) {
		if (l < r) {
			int mid = (l + r) >> 1;
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
		}
		poster[i] = 0;
	}

	// 区间更新操作（张贴海报）
	public static void update(int jobl, int jobr, int jobv, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			poster[i] = jobv;
		} else {
			down(i);
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				update(jobl, jobr, jobv, l, mid, i << 1);
			}
			if (jobr > mid) {
				update(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
		}
	}

	// 区间查询操作（统计可见海报数量）
	public static int query(int jobl, int jobr, int l, int r, int i) {
		if (l == r) {
			if (poster[i] != 0 && !visited[poster[i]]) {
				visited[poster[i]] = true;
				return 1;
			} else {
				return 0;
			}
		} else {
			down(i);
			int mid = (l + r) >> 1;
			int ans = 0;
			if (jobl <= mid) {
				ans += query(jobl, jobr, l, mid, i << 1);
			}
			if (jobr > mid) {
				ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
			}
			return ans;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int cases = (int) in.nval;
		for (int t = 1; t <= cases; t++) {
			in.nextToken();
			int n = (int) in.nval;
			int m = 0;
			for (int i = 1; i <= n; i++) {
				in.nextToken();
				pl[i] = (int) in.nval;
				in.nextToken();
				pr[i] = (int) in.nval;
				num[++m] = pl[i];
				num[++m] = pr[i];
			}
			m = collect(m);
			build(1, m, 1);
			for (int i = 1, jobl, jobr; i <= n; i++) {
				jobl = rank(1, m, pl[i]);
				jobr = rank(1, m, pr[i]);
				update(jobl, jobr, i, 1, m, 1);
			}
			out.println(query(1, m, 1, m, 1));
			Arrays.fill(visited, 1, n + 1, false);
		}
		out.flush();
		out.close();
		br.close();
	}

}