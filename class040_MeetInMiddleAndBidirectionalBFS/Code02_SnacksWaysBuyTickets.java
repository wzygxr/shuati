package class063;

// 牛牛的背包问题 & 世界冰球锦标赛
// 牛牛准备参加学校组织的春游, 出发前牛牛准备往背包里装入一些零食, 牛牛的背包容量为w。
// 牛牛家里一共有n袋零食, 第i袋零食体积为v[i]。
// 牛牛想知道在总体积不超过背包容量的情况下,他一共有多少种零食放法(总体积为0也算一种放法)。
// 输入描述：
// 输入包括两行
// 第一行为两个正整数n和w(1 <= n <= 30, 1 <= w <= 2 * 10^9),表示零食的数量和背包的容量
// 第二行n个正整数v[i](0 <= v[i] <= 10^9),表示每袋零食的体积
// 输出描述：
// 输出一个正整数, 表示牛牛一共有多少种零食放法。
// 测试链接 : https://www.luogu.com.cn/problem/P4799
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下所有代码，把主类名改成Main，可以直接通过
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和，
// 然后通过双指针技术合并结果
// 时间复杂度：O(2^(n/2) * log(2^(n/2))) = O(n * 2^(n/2))
// 空间复杂度：O(2^(n/2))

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code02_SnacksWaysBuyTickets {

	public static int MAXN = 40;

	public static int MAXM = 1 << 20;

	public static long[] arr = new long[MAXN];

	public static long[] lsum = new long[MAXM];

	public static long[] rsum = new long[MAXM];

	public static int n;

	public static long w;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			in.nextToken();
			w = (long) in.nval;
			for (int i = 0; i < n; i++) {
				in.nextToken();
				arr[i] = (long) in.nval;
			}
			out.println(compute());
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算满足条件的零食放法数量
	 * 使用折半搜索算法，将数组分为两部分分别处理
	 * @return 满足条件的方案数
	 */
	public static long compute() {
		// 分别计算前半部分和后半部分的所有可能和
		int lsize = f(0, n >> 1, 0, w, lsum, 0);
		int rsize = f(n >> 1, n, 0, w, rsum, 0);
		
		// 对两个数组进行排序，为双指针合并做准备
		Arrays.sort(lsum, 0, lsize);
		Arrays.sort(rsum, 0, rsize);
		
		long ans = 0;
		// 使用双指针技术计算满足条件的组合数
		for (int i = lsize - 1, j = 0; i >= 0; i--) {
			// 移动右指针，找到所有满足条件的组合
			while (j < rsize && lsum[i] + rsum[j] <= w) {
				j++;
			}
			// 累加满足条件的组合数
			ans += j;
		}
		return ans;
	}

	/**
	 * 递归计算数组指定范围内所有可能的和
	 * @param i 当前处理的元素索引
	 * @param e 结束索引
	 * @param s 当前累积和
	 * @param w 背包容量上限
	 * @param ans 存储结果的数组
	 * @param j 当前在结果数组中的位置
	 * @return 结果数组的新位置
	 */
	// arr[i..e-1]范围上展开，到达e就停止
	// 返回值 : ans数组填到了什么位置！
	public static int f(int i, int e, long s, long w, long[] ans, int j) {
		// 剪枝：如果当前和已经超过背包容量，直接返回
		if (s > w) {
			return j;
		}
		// s <= w
		if (i == e) {
			// 到达边界，将当前和加入结果数组
			ans[j++] = s;
		} else {
			// 不要arr[i]位置的数
			j = f(i + 1, e, s, w, ans, j);
			// 要arr[i]位置的数
			j = f(i + 1, e, s + arr[i], w, ans, j);
		}
		return j;
	}
	
	// 测试方法
	public static void test() {
		// 测试用例：n=5, w=1000, arr=[100, 1500, 500, 500, 1000]
		// 预期输出：8
		n = 5;
		w = 1000;
		arr[0] = 100;
		arr[1] = 1500;
		arr[2] = 500;
		arr[3] = 500;
		arr[4] = 1000;
		
		long result = compute();
		System.out.println("测试用例:");
		System.out.println("n=5, w=1000");
		System.out.println("arr=[100, 1500, 500, 500, 1000]");
		System.out.println("预期输出: 8");
		System.out.println("实际输出: " + result);
	}
}