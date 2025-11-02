package class019;

// 展示acm风格的测试方式
// 子矩阵的最大累加和问题，不要求会解题思路，后面的课会讲
// 每一组测试都给定数据规模
// 任何空间都提前生成好，一律都是静态空间，然后自己去复用，推荐这种方式
// 测试链接 : https://www.nowcoder.com/practice/cb82a97dcd0d48a7b1f4ee917e2c0409?
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过
//
// 题目描述：
// 给定一个矩阵，求其所有子矩阵中元素和的最大值
// 输入：
// 第一行包含两个整数n和m(1 <= n, m <= 100)，表示矩阵的行数和列数
// 接下来n行，每行包含m个整数，表示矩阵中的元素(-1000 <= matrix[i][j] <= 1000)
// 输出：
// 一个整数，表示所有子矩阵中元素和的最大值
//
// 示例：
// 输入：
// 3 3
// 1 2 3
// -4 5 -6
// 7 8 9
// 输出：
// 27
//
// 解题思路：
// 1. 使用压缩数组技巧，将二维问题转化为一维最大子数组和问题
// 2. 枚举所有可能的上下边界组合(i,j)，将第i行到第j行的每列元素相加，得到一个一维数组
// 3. 对这个一维数组求最大子数组和，这就是以(i,j)为上下边界的所有子矩阵中的最大和
// 4. 遍历所有上下边界组合，记录全局最大值
//
// 时间复杂度分析：
// - 枚举上下边界：O(n^2)
// - 计算压缩数组并求最大子数组和：O(m)
// - 总时间复杂度：O(n^2 * m)
//
// 空间复杂度分析：
// - 需要一个辅助数组存储压缩后的结果：O(m)
// - 总空间复杂度：O(m)
//
// 适用场景：
// - 需要在一个二维矩阵中找到和最大的子矩阵
// - 数据规模较小(n,m <= 100)的情况
//
// 优化点：
// - 可以在计算压缩数组时同时进行最大子数组和的计算，避免两次遍历
// - 可以使用前缀和优化压缩数组的计算过程

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code03_StaticSpace {

	// 题目给定的行的最大数据量
	public static int MAXN = 201;

	// 题目给定的列的最大数据量
	public static int MAXM = 201;

	// 申请这么大的矩阵空间，一定够用了
	// 静态的空间，不停复用
	public static int[][] mat = new int[MAXN][MAXM];

	// 需要的所有辅助空间也提前生成
	// 静态的空间，不停复用
	public static int[] arr = new int[MAXM];

	// 当前测试数据行的数量是n
	// 当前测试数据列的数量是m
	// 这两个变量可以把代码运行的边界规定下来
	public static int n, m;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			in.nextToken();
			m = (int) in.nval;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					in.nextToken();
					mat[i][j] = (int) in.nval;
				}
			}
			out.println(maxSumSubmatrix());
		}
		out.flush();
		br.close();
		out.close();
	}

	// 求子矩阵的最大累加和，后面的课会讲
	// 算法思路：
	// 1. 枚举所有可能的上下边界(i,j)
	// 2. 将第i行到第j行的每列元素相加，形成一个一维数组
	// 3. 对这个一维数组求最大子数组和
	// 4. 记录所有情况下的最大值
	//
	// 时间复杂度：O(n^2 * m)，其中n是行数，m是列数
	// 空间复杂度：O(m)，用于存储压缩后的数组
	public static int maxSumSubmatrix() {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < n; i++) {
			// 因为之前的过程可能用过辅助数组
			// 为了让之前结果不干扰到这次运行，需要自己清空辅助数组需要用到的部分
			Arrays.fill(arr, 0, m, 0);
			for (int j = i; j < n; j++) {
				for (int k = 0; k < m; k++) {
					arr[k] += mat[j][k];
				}
				max = Math.max(max, maxSumSubarray());
			}
		}
		return max;
	}

	// 求子数组的最大累加和，使用Kadane算法
	// 算法思路：
	// 1. 维护当前子数组的和(cur)和全局最大值(max)
	// 2. 遍历数组，将当前元素加入到当前子数组和中
	// 3. 更新全局最大值
	// 4. 如果当前子数组和变为负数，则重新开始计算(置为0)
	//
	// 时间复杂度：O(m)，其中m是数组长度
	// 空间复杂度：O(1)
	public static int maxSumSubarray() {
		int max = Integer.MIN_VALUE;
		int cur = 0;
		for (int i = 0; i < m; i++) {
			cur += arr[i];
			max = Math.max(max, cur);
			cur = cur < 0 ? 0 : cur;
		}
		return max;
	}

}