package class146_CombinatorialAndMathematicalAlgorithms;

// 逆康托展开
// 数字从1到n，可以有很多排列，给定一个长度为n的数组s，表示具体的一个排列
// 求出这个排列的排名假设为x，打印第x+m名的排列是什么
// 1 <= n <= 10^5
// 1 <= m <= 10^15
// 题目保证s是一个由1~n数字组成的正确排列，题目保证x+m不会超过排列的总数
// 测试链接 : https://www.luogu.com.cn/problem/U72177
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 逆康托展开算法实现
 * 
 * 算法原理:
 * 逆康托展开是康托展开的逆过程，用于根据排列的排名恢复对应的排列。
 * 康托展开将一个排列映射为唯一的自然数（排名），而逆康托展开则根据排名恢复排列。
 * 
 * 核心思想:
 * 1. 预处理阶乘数组，用于计算每个位置的权重
 * 2. 使用线段树维护可用数字集合，支持高效查询和删除操作
 * 3. 从高位到低位依次确定每个位置的元素
 * 
 * 算法步骤:
 * 1. 预处理阶乘数组 fac[i] = i!
 * 2. 将输入排列转换为阶乘进制表示
 * 3. 在阶乘进制基础上加上m，得到目标排名
 * 4. 根据目标排名使用线段树构造对应的排列
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 
 * 相关题目:
 * 1. Luogu U72177 逆康托展开
 *    链接: https://www.luogu.com.cn/problem/U72177
 *    题目描述: 给定一个排列和一个增量m，求第x+m名的排列，其中x是给定排列的排名
 *    解题思路: 使用逆康托展开算法，通过线段树维护可用数字集合
 * 
 * 2. LeetCode 60. Permutation Sequence (排列序列)
 *    链接: https://leetcode.cn/problems/permutation-sequence/
 *    题目描述: 给出集合 [1,2,3,...,n]，其所有元素有 n! 种排列。按大小顺序列出所有排列情况，并一一标记，当 n = 3 时, 所有排列如下:
 *             "123"
 *             "132"
 *             "213"
 *             "231"
 *             "312"
 *             "321"
 *             给定 n 和 k，返回第 k 个排列。
 *    解题思路: 使用逆康托展开直接计算第k个排列，避免生成所有排列
 * 
 * 3. POJ 1256 Anagram
 *    链接: http://poj.org/problem?id=1256
 *    题目描述: 给定一个字符串，输出它的所有排列，按字典序排序
 *    解题思路: 可以使用逆康托展开生成下一个排列
 * 
 * 4. Codeforces 501D Misha and Permutations Summation
 *    链接: https://codeforces.com/problemset/problem/501/D
 *    题目描述: 给出两个排列，定义ord(p)为排列p的顺序（字典顺从小到大），定义perm(x)为顺序为x的排列，现在要求计算两个排列的序号之和对应的排列
 *    解题思路: 使用康托展开将排列转换为数字，相加后再使用逆康托展开转换回排列
 * 
 * 5. AtCoder ABC041C 背番号
 *    链接: https://atcoder.jp/contests/abc041/tasks/abc041_c
 *    题目描述: 有N个选手，每个选手有一个背番号，背番号是1到N的排列。现在从观众席上可以看到一排选手，他们的背番号构成一个排列。请计算这个排列在所有可能的排列中，字典序排第几（从1开始）
 *    解题思路: 直接应用康托展开计算排列的字典序排名
 * 
 * 6. HDU 2645 Treasure Map
 *    链接: http://acm.hdu.edu.cn/showproblem.php?pid=2645
 *    题目描述: 给定一个地图，每个格子有一个值，需要按照一定规则排列这些值
 *    解题思路: 使用康托展开进行状态压缩
 * 
 * 7. SPOJ PERMUT2 Checking anagrams
 *    链接: https://www.spoj.com/problems/PERMUT2/
 *    题目描述: 判断一个排列是否是自反的，即排列两次后回到原排列
 *    解题思路: 可以结合康托展开的思想理解排列的性质
 * 
 * 8. 牛客网 NC14261 排列的排名
 *    链接: https://ac.nowcoder.com/acm/problem/14261
 *    题目描述: 给定一个n的排列，求其在字典序中的排名，结果对1e9+7取模
 *    解题思路: 使用康托展开计算排名，注意取模操作
 * 
 * 9. HackerRank Next Permutation
 *    链接: https://www.hackerrank.com/challenges/next-permutation/problem
 *    题目描述: 给定一个排列，求字典序的下一个排列
 *    解题思路: 可以结合康托展开的思想求解
 * 
 * 10. Luogu P1379 八数码难题
 *     链接: https://www.luogu.com.cn/problem/P1379
 *     题目描述: 在3×3的棋盘上，摆有八个棋子，每个棋子上标有1至8的某一数字。棋盘中留有一个空格，空格用0来表示。空格周围的棋子可以移到空格中。要求解的问题是：给出一种初始状态和目标状态，计算最少移动步数
 *     解题思路: 使用康托展开作为状态压缩方法，结合BFS求解最短路径
 */
public class Code02_InverseCantorExpansion {

	public static int MAXN = 100001;

	public static long[] arr = new long[MAXN];

	// 线段树
	// 这种使用线段树的方式叫线段树二分
	// 讲解169的题目1，也涉及线段树二分
	public static int[] sum = new int[MAXN << 2];

	public static int n;

	public static long m;

	// 初始化线段树，单点范围的初始累加和为1，认为所有数字都可用
	public static void build(int l, int r, int i) {
		if (l == r) {
			sum[i] = 1;
		} else {
			int mid = (l + r) >> 1;
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
			sum[i] = sum[i << 1] + sum[i << 1 | 1];
		}
	}

	// 单点jobi上，增加jobv，因为是单点更新，所以不需要建立懒更新机制
	public static void add(int jobi, int jobv, int l, int r, int i) {
		if (l == r) {
			sum[i] += jobv;
		} else {
			int mid = (l + r) >> 1;
			if (jobi <= mid) {
				add(jobi, jobv, l, mid, i << 1);
			} else {
				add(jobi, jobv, mid + 1, r, i << 1 | 1);
			}
			sum[i] = sum[i << 1] + sum[i << 1 | 1];
		}
	}

	// 查询jobl~jobr范围的累加和
	public static int sum(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			return sum[i];
		}
		int mid = (l + r) >> 1;
		int ans = 0;
		if (jobl <= mid) {
			ans += sum(jobl, jobr, l, mid, i << 1);
		}
		if (jobr > mid) {
			ans += sum(jobl, jobr, mid + 1, r, i << 1 | 1);
		}
		return ans;
	}

	// 线段树上找到第k名的是什么，找到后删掉词频，返回的过程修改累加和
	public static int getAndDelete(int k, int l, int r, int i) {
		int ans;
		if (l == r) {
			sum[i]--;
			ans = l;
		} else {
			int mid = (l + r) >> 1;
			if (sum[i << 1] >= k) {
				ans = getAndDelete(k, l, mid, i << 1);
			} else {
				ans = getAndDelete(k - sum[i << 1], mid + 1, r, i << 1 | 1);
			}
			sum[i] = sum[i << 1] + sum[i << 1 | 1];
		}
		return ans;
	}

	public static void compute() {
		// 当前排列转化为阶乘进制的排名
		build(1, n, 1);
		for (int i = 1, x; i <= n; i++) {
			x = (int) arr[i];
			if (x == 1) {
				arr[i] = 0;
			} else {
				arr[i] = sum(1, x - 1, 1, n, 1);
			}
			add(x, -1, 1, n, 1);
		}
		// 当前排名加上m之后，得到新的排名，用阶乘进制表示
		arr[n] += m; // 最低位获得增加的幅度
		for (int i = n; i >= 1; i--) {
			// 往上进位多少
			arr[i - 1] += arr[i] / (n - i + 1);
			// 当前位是多少
			arr[i] %= n - i + 1;
		}
		// 根据阶乘进制转化为具体的排列
		build(1, n, 1);
		for (int i = 1; i <= n; i++) {
			arr[i] = getAndDelete((int) arr[i] + 1, 1, n, 1);
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (long) in.nval;
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		compute();
		for (int i = 1; i <= n; i++) {
			out.print(arr[i] + " ");
		}
		out.flush();
		out.close();
		br.close();
	}

}