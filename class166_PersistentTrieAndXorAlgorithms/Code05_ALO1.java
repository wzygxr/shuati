package class159;

// 生成能量密度最大的宝石，java版
// 给定一个长度为n的数组arr，数组中没有重复数字
// 你可以随意选择一个子数组，长度要求大于等于2，因为这样一来，子数组必存在次大值
// 子数组的次大值 ^ 子数组中除了次大值之外随意选一个数字
// 所能得到的最大结果，叫做子数组的能量密度
// 那么必有某个子数组，拥有最大的能量密度，打印这个最大的能量密度
// 2 <= n <= 5 * 10^4
// 0 <= arr[i] <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P4098
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 补充题目1: 子数组次大值异或最大值
// 给定一个数组，选择一个子数组，用子数组的次大值与子数组中其他任意元素异或，求最大值
// 相关题目:
// - https://www.luogu.com.cn/problem/P4098
// - https://codeforces.com/problemset/problem/1715/E
// - https://www.hdu.edu.cn/problem/5325

// 补充题目2: 可持久化Trie树应用
// 利用可持久化Trie树解决区间异或最大值问题
// 相关题目:
// - https://www.luogu.com.cn/problem/P4735
// - https://www.luogu.com.cn/problem/P4592
// - https://codeforces.com/problemset/problem/1175/G

// 补充题目3: 贪心策略优化
// 通过排序和链表优化减少不必要的计算
// 相关题目:
// - https://www.luogu.com.cn/problem/P4098
// - https://codeforces.com/problemset/problem/1354/D
// - https://www.spoj.com/problems/MKTHNUM/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code05_ALO1 {

	// 最大数组长度
	public static int MAXN = 50002;

	// Trie树最大节点数
	public static int MAXT = MAXN * 32;

	// 位数，由于数字范围是0 <= arr[i] <= 10^9，所以最多需要30位（2^30 > 10^9）
	public static int BIT = 30;

	// 数组长度
	public static int n;

	// arr[i][0]表示第i个元素的原始索引
	// arr[i][1]表示第i个元素的值
	public static int[][] arr = new int[MAXN][2];

	// 可持久化Trie树的根节点数组
	// root[i]表示前i个数构成的可持久化Trie树的根节点编号
	public static int[] root = new int[MAXN];

	// Trie树节点的子节点数组
	// tree[i][0/1]表示Trie树节点i的左右子节点编号
	public static int[][] tree = new int[MAXT][2];

	// 经过Trie树节点的数字个数
	// pass[i]表示经过Trie树节点i的数字个数
	public static int[] pass = new int[MAXT];

	// Trie树节点计数器
	public static int cnt;

	// 链表相关数组
	// last[i]表示位置i的前一个位置
	public static int[] last = new int[MAXN];

	// next[i]表示位置i的后一个位置
	public static int[] next = new int[MAXN];

	/**
	 * 在可持久化Trie树中插入一个数字
	 * @param num 要插入的数字
	 * @param i 前一个版本的根节点编号
	 * @return 新版本的根节点编号
	 */
	public static int insert(int num, int i) {
		// 创建新根节点
		int rt = ++cnt;
		// 复用前一个版本的左右子树
		tree[rt][0] = tree[i][0];
		tree[rt][1] = tree[i][1];
		// 经过该节点的数字个数加1
		pass[rt] = pass[i] + 1;
		
		// 从高位到低位处理数字的每一位
		for (int b = BIT, path, pre = rt, cur; b >= 0; b--, pre = cur) {
			// 提取第b位的值（0或1）
			path = (num >> b) & 1;
			// 获取前一个版本中对应子节点
			i = tree[i][path];
			// 创建新节点
			cur = ++cnt;
			// 复用前一个版本的子节点信息
			tree[cur][0] = tree[i][0];
			tree[cur][1] = tree[i][1];
			// 更新经过该节点的数字个数
			pass[cur] = pass[i] + 1;
			// 连接父子节点
			tree[pre][path] = cur;
		}
		return rt;
	}

	/**
	 * 在可持久化Trie树中查询区间[u,v]与num异或的最大值
	 * @param num 查询的数字
	 * @param u 区间左边界对应版本的根节点编号
	 * @param v 区间右边界对应版本的根节点编号
	 * @return 最大异或值
	 */
	public static int query(int num, int u, int v) {
		int ans = 0;
		// 从高位到低位贪心选择使异或结果最大的路径
		for (int b = BIT, path, best; b >= 0; b--) {
			// 提取第b位的值
			path = (num >> b) & 1;
			// 贪心策略：尽量选择与当前位相反的路径
			best = path ^ 1;
			// 如果在区间[u,v]中存在best路径，则选择该路径
			if (pass[tree[v][best]] > pass[tree[u][best]]) {
				// 将第b位置为1
				ans += 1 << b;
				// 移动到best子节点
				u = tree[u][best];
				v = tree[v][best];
			} else {
				// 否则只能选择相同路径
				u = tree[u][path];
				v = tree[v][path];
			}
		}
		return ans;
	}

	/**
	 * 预处理函数，构建可持久化Trie树和链表
	 */
	public static void prepare() {
		// 初始化链表边界
		last[0] = 0;
		next[0] = 1;
		last[n + 1] = n;
		next[n + 1] = n + 1;
		
		// 构建可持久化Trie树
		for (int i = 1; i <= n; i++) {
			root[i] = insert(arr[i][1], root[i - 1]);
			// 初始化链表
			last[i] = i - 1;
			next[i] = i + 1;
		}
		
		// 按值排序数组
		Arrays.sort(arr, 1, n + 1, (a, b) -> a[1] - b[1]);
	}

	/**
	 * 计算最大能量密度
	 * @return 最大能量密度
	 */
	public static int compute() {
		int ans = 0;
		// 按值从小到大处理每个元素
		for (int i = 1, index, value, l1, l2, r1, r2; i <= n; i++) {
			// 获取元素的原始索引和值
			index = arr[i][0];
			value = arr[i][1];
			
			// 获取链表中的相邻位置
			l1 = last[index];  // 左边第一个位置
			l2 = last[l1];     // 左边第二个位置
			r1 = next[index];  // 右边第一个位置
			r2 = next[r1];     // 右边第二个位置
			
			// 如果左边有元素，计算以value为次大值的子数组能量密度
			if (l1 != 0) {
				// 在区间[l2, r1-1]中查找与value异或的最大值
				ans = Math.max(ans, query(value, root[l2], root[r1 - 1]));
			}
			
			// 如果右边有元素，计算以value为次大值的子数组能量密度
			if (r1 != n + 1) {
				// 在区间[l1, r2-1]中查找与value异或的最大值
				ans = Math.max(ans, query(value, root[l1], root[r2 - 1]));
			}
			
			// 更新链表，将当前位置从链表中移除
			next[l1] = r1;
			last[r1] = l1;
		}
		return ans;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		// 读入数组元素，同时记录原始索引
		for (int i = 1; i <= n; i++) {
			arr[i][0] = i;  // 记录原始索引
			in.nextToken();
			arr[i][1] = (int) in.nval;  // 记录值
		}
		// 预处理
		prepare();
		// 输出最大能量密度
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

}