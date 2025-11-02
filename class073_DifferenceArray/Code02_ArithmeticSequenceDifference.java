package class047;

// 一开始1~n范围上的数字都是0，一共有m个操作，每次操作为(l,r,s,e,d)
// 表示在l~r范围上依次加上首项为s、末项为e、公差为d的数列
// m个操作做完之后，统计1~n范围上所有数字的最大值和异或和
// 测试链接 : https://www.luogu.com.cn/problem/P4231
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过
//
// 相关题目:
// 1. 洛谷 P4231 三步必杀 (Three Steps Kill)
//    链接: https://www.luogu.com.cn/problem/P4231
//    题目描述: 在1~n范围上的数字初始都为0，有m个操作，每次操作为(l,r,s,e,d)，
//             表示在l~r范围上依次加上首项为s、末项为e、公差为d的等差数列。
//             所有操作完成后，统计1~n范围上所有数字的最大值和异或和。
//
// 2. 洛谷 P5026 Lycanthropy
//    链接: https://www.luogu.com.cn/problem/P5026
//    题目描述: 朋友落水后，会对水面产生影响，形成特定的水位分布。
//             需要使用二阶差分来处理这种复杂的区间更新问题。
//
// 3. Codeforces 296C Greg and Array
//    链接: https://codeforces.com/contest/296/problem/C
//    题目描述: 给定一个数组和一系列操作，每个操作是对数组的一个区间进行加法操作。
//             然后给定一些指令，每个指令指定执行哪些操作各多少次。
//             最后输出执行完所有指令后的数组。
//
// 等差数列差分核心思想:
// 对于等差数列的区间更新，我们需要使用二阶差分。
// 一阶差分数组 b 定义为: b[i] = a[i] - a[i-1]
// 二阶差分数组 c 定义为: c[i] = b[i] - b[i-1] = a[i] - 2*a[i-1] + a[i-2]
//
// 对于在区间[l,r]上加上首项为s、末项为e、公差为d的等差数列:
// 原数组变化: a[l] += s, a[l+1] += s+d, ..., a[r] += e
// 一阶差分变化: b[l] += s, b[l+1] += d, ..., b[r+1] -= (e+d), b[r+2] += e
// 二阶差分变化: c[l] += s, c[l+1] += d-s, c[r+1] -= d+e, c[r+2] += e
//
// 但在这个实现中，我们直接使用一阶差分，通过特定的公式来处理等差数列:
// arr[l] += s;
// arr[l + 1] += d - s;
// arr[r + 1] -= d + e;
// arr[r + 2] += e;
//
// 时间复杂度分析:
// 每次操作: O(1)
// 构造结果数组(两次前缀和): O(n)
// 总时间复杂度: O(m + n)
//
// 空间复杂度分析:
// 需要额外的数组空间: O(n)
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code02_ArithmeticSequenceDifference {

	public static int MAXN = 10000005;

	public static long[] arr = new long[MAXN];

	public static int n, m;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			in.nextToken();
			m = (int) in.nval;
			// 处理m个操作
			for (int i = 0, l, r, s, e; i < m; i++) {
				in.nextToken(); l = (int) in.nval;
				in.nextToken(); r = (int) in.nval;
				in.nextToken(); s = (int) in.nval;
				in.nextToken(); e = (int) in.nval;
				// 对区间[l,r]加上首项为s、末项为e、公差为d的等差数列
				// 其中公差d = (e - s) / (r - l)
				set(l, r, s, e, (e - s) / (r - l));
			}
			// 通过两次前缀和操作构建最终结果数组
			build();
			long max = 0, xor = 0;
			// 计算最大值和异或和
			for (int i = 1; i <= n; i++) {
				max = Math.max(max, arr[i]);
				xor ^= arr[i];
			}
			out.println(xor + " " + max);
		}
		out.flush();
		out.close();
		br.close();
	}

	// 在区间[l,r]上加上首项为s、末项为e、公差为d的等差数列
	// 这是等差数列差分的核心操作
	public static void set(int l, int r, int s, int e, int d) {
		// 差分数组更新
		// arr[l] += s 表示从位置l开始增加首项s
		arr[l] += s;
		// arr[l + 1] += d - s 表示从位置l+1开始相对于前一个位置增加公差d
		arr[l + 1] += d - s;
		// arr[r + 1] -= d + e 表示从位置r+1开始减少(d+e)
		arr[r + 1] -= d + e;
		// arr[r + 2] += e 表示从位置r+2开始增加e
		arr[r + 2] += e;
	}

	// 通过两次前缀和操作构建最终结果数组
	public static void build() {
		// 第一次前缀和，得到一阶差分的结果
		for (int i = 1; i <= n; i++) {
			arr[i] += arr[i - 1];
		}
		// 第二次前缀和，得到最终结果
		for (int i = 1; i <= n; i++) {
			arr[i] += arr[i - 1];
		}
	}

}