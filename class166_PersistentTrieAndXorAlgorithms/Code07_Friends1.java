package class159;

// 前m大两两异或值的和，java版
// 本题只用到了经典前缀树，没有用到可持久化前缀树
// 给定一个长度为n的数组arr，下标1~n
// 你可以随意选两个不同位置的数字进行异或，得到两两异或值，顺序不同的话，算做一个两两异或值
// 那么，两两异或值，就有第1大、第2大...
// 返回前k大两两异或值的累加和，答案对1000000007取模
// 1 <= n <= 5 * 10^4
// 0 <= k <= n * (n-1) / 2
// 0 <= arr[i] <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/CF241B
// 测试链接 : https://codeforces.com/problemset/problem/241/B
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 补充题目1: 前k大两两异或值的和
// 给定一个数组，计算所有两两不同位置元素异或值中前k个最大的值的和
// 相关题目:
// - https://www.luogu.com.cn/problem/CF241B
// - https://codeforces.com/problemset/problem/241/B
// - https://www.hdu.edu.cn/problem/5325

// 补充题目2: Trie树应用
// 利用Trie树解决异或值相关问题
// 相关题目:
// - https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
// - https://www.luogu.com.cn/problem/P4551
// - https://codeforces.com/problemset/problem/282/E

// 补充题目3: 二分答案优化
// 通过二分答案和数学计算优化第k大值查询
// 相关题目:
// - https://www.luogu.com.cn/problem/CF241B
// - https://codeforces.com/problemset/problem/1715/E
// - https://www.spoj.com/problems/MKTHNUM/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code07_Friends1 {

	// 最大数组长度
	public static int MAXN = 50001;

	// Trie树最大节点数
	public static int MAXT = MAXN * 20;

	// 位数，由于数字范围是0 <= arr[i] <= 10^9，所以最多需要30位（2^30 > 10^9）
	public static int BIT = 30;

	// 模数
	public static int MOD = 1000000007;

	// 2的逆元，用于除法取模运算
	public static int INV2 = 500000004;

	// 数组长度和查询数
	public static int n, k;

	// 原数组
	public static int[] arr = new int[MAXN];

	// Trie树节点的子节点数组
	// tree[i][0/1]表示Trie树节点i的左右子节点编号
	public static int[][] tree = new int[MAXT][2];

	// 经过Trie树节点的数字个数
	// pass[i]表示经过Trie树节点i的数字个数
	public static int[] pass = new int[MAXT];

	// Trie树节点计数器，初始为1（根节点）
	public static int cnt = 1;

	// sum[i][j]表示以节点i为根的子树中，第j位为1的数字个数
	public static int[][] sum = new int[MAXT][BIT + 1];

	/**
	 * 在Trie树中插入一个数字
	 * @param num 要插入的数字
	 */
	public static void insert(int num) {
		// 从根节点开始
		int cur = 1;
		// 经过根节点的数字个数加1
		pass[1]++;
		// 从高位到低位处理数字的每一位
		for (int b = BIT, path; b >= 0; b--) {
			// 提取第b位的值（0或1）
			path = (num >> b) & 1;
			// 如果子节点不存在，创建新节点
			if (tree[cur][path] == 0) {
				tree[cur][path] = ++cnt;
			}
			// 移动到子节点
			cur = tree[cur][path];
			// 经过该节点的数字个数加1
			pass[cur]++;
		}
	}

	/**
	 * DFS遍历Trie树，计算每个节点的sum值
	 * @param i 当前节点编号
	 * @param h 当前节点深度
	 * @param s 当前路径表示的数字
	 */
	public static void dfs(int i, int h, int s) {
		// 如果节点不存在，直接返回
		if (i == 0) {
			return;
		}
		// 如果是叶子节点
		if (h == 0) {
			// 计算每一位的sum值
			for (int j = 0; j <= BIT; j++) {
				// 如果s的第j位是1，则sum[i][j]等于经过该节点的数字个数
				if (((s >> j) & 1) == 1) {
					sum[i][j] = pass[i];
				}
			}
		} else {
			// 递归处理左右子树
			dfs(tree[i][0], h - 1, s);
			dfs(tree[i][1], h - 1, s | (1 << (h - 1)));
			// 计算当前节点的sum值
			for (int j = 0; j <= BIT; j++) {
				sum[i][j] = sum[tree[i][0]][j] + sum[tree[i][1]][j];
			}
		}
	}

	/**
	 * 计算大于等于x的两两异或值的个数
	 * @param x 查询值
	 * @return 大于等于x的两两异或值的个数
	 */
	public static long moreEqual(int x) {
		long ans = 0;
		// 遍历每个数组元素
		for (int i = 1, num, cur; i <= n; i++) {
			num = arr[i];
			cur = 1;
			// 在Trie树中查找与num异或值大于等于x的数字个数
			for (int b = BIT, path, best, xpath; b >= 0; b--) {
				// 提取num的第b位
				path = (num >> b) & 1;
				// 贪心策略：尽量选择与当前位相反的路径
				best = path ^ 1;
				// 提取x的第b位
				xpath = (x >> b) & 1;
				// 根据xpath的值决定选择哪条路径
				if (xpath == 0) {
					// 如果x的第b位是0，则选择best路径的数字都满足条件
					ans += pass[tree[cur][best]];
					// 继续在path路径上查找
					cur = tree[cur][path];
				} else {
					// 如果x的第b位是1，则只能在best路径上查找
					cur = tree[cur][best];
				}
				// 如果节点不存在，跳出循环
				if (cur == 0) {
					break;
				}
			}
			// 加上当前节点的数字个数
			ans += pass[cur];
		}
		// 如果x为0，需要减去自己与自己异或的情况
		if (x == 0) {
			ans -= n;
		}
		// 由于每对数字被计算了两次，所以除以2
		return ans / 2;
	}

	/**
	 * 二分查找第k大的两两异或值
	 * @return 第k大的两两异或值
	 */
	public static int maxKth() {
		// 二分查找范围
		int l = 0, r = 1 << BIT, m;
		int ans = 0;
		// 二分查找
		while (l <= r) {
			m = (l + r) / 2;
			// 如果大于等于m的两两异或值个数大于等于k，则答案可能为m或更大
			if (moreEqual(m) >= k) {
				ans = m;
				l = m + 1;
			} else {
				// 否则答案小于m
				r = m - 1;
			}
		}
		return ans;
	}

	/**
	 * 计算前k大两两异或值的和
	 * @return 前k大两两异或值的和
	 */
	public static long compute() {
		// 查找第k大的两两异或值
		int kth = maxKth();
		long ans = 0;
		// 遍历每个数组元素
		for (int i = 1, cur; i <= n; i++) {
			cur = 1;
			// 在Trie树中计算与arr[i]异或值大于等于kth的数字的异或和
			for (int b = BIT, path, best, kpath; b >= 0; b--) {
				// 提取arr[i]的第b位
				path = (arr[i] >> b) & 1;
				// 贪心策略：尽量选择与当前位相反的路径
				best = path ^ 1;
				// 提取kth的第b位
				kpath = (kth >> b) & 1;
				// 根据kpath的值决定选择哪条路径
				if (kpath == 0) {
					// 如果kth的第b位是0，则计算best路径上所有数字与arr[i]的异或和
					for (int j = 0; j <= BIT; j++) {
						// 根据arr[i]的第j位决定异或结果
						if (((arr[i] >> j) & 1) == 1) {
							// 如果arr[i]的第j位是1，则异或结果为1的数字个数为pass[tree[cur][best]]-sum[tree[cur][best]][j]
							ans = (ans + ((long) pass[tree[cur][best]] - sum[tree[cur][best]][j]) * (1L << j)) % MOD;
						} else {
							// 如果arr[i]的第j位是0，则异或结果为1的数字个数为sum[tree[cur][best]][j]
							ans = (ans + (long) sum[tree[cur][best]][j] * (1L << j)) % MOD;
						}
					}
					// 继续在path路径上查找
					cur = tree[cur][path];
				} else {
					// 如果kth的第b位是1，则只能在best路径上查找
					cur = tree[cur][best];
				}
				// 如果节点不存在，跳出循环
				if (cur == 0) {
					break;
				}
			}
			// 加上当前节点的数字与arr[i]的异或和
			ans = (ans + (long) pass[cur] * kth) % MOD;
		}
		// 由于每对数字被计算了两次，所以除以2
		ans = ans * INV2 % MOD;
		// 减去多余的异或值
		ans = ((ans - (moreEqual(kth) - k) * kth % MOD) % MOD + MOD) % MOD;
		return ans;
	}

	/**
	 * 预处理函数，构建Trie树和sum数组
	 */
	public static void prepare() {
		// 构建Trie树
		for (int i = 1; i <= n; i++) {
			insert(arr[i]);
		}
		// 计算sum数组
		dfs(tree[1][0], BIT, 0);
		dfs(tree[1][1], BIT, 1 << BIT);
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		k = (int) in.nval;
		// 读入数组元素
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		// 如果k为0，直接输出0
		if (k == 0) {
			out.println(0);
		} else {
			// 预处理
			prepare();
			// 输出前k大两两异或值的和
			out.println(compute());
		}
		out.flush();
		out.close();
		br.close();
	}

}