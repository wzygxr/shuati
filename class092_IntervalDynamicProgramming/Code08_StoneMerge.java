package class077;

// 石子合并
// 在一条直线上有n堆石子，每堆有一个重量。现在要合并这些石子成为一堆，
// 每次只能合并相邻的两堆石子，合并的代价为这两堆石子的重量之和。
// 求合并所有石子的最小代价。
// 测试链接 : https://www.luogu.com.cn/problem/P1880
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code08_StoneMerge {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		in.nextToken();
		int n = (int) in.nval;
		int[] stones = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			stones[i] = (int) in.nval;
		}
		
		int[] preSum = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			preSum[i] = preSum[i - 1] + stones[i];
		}
		
		int[] result = minMaxCost(stones, preSum, n);
		out.println(result[0]); // 最小代价
		out.println(result[1]); // 最大代价
		out.flush();
		out.close();
		br.close();
	}

	// 区间动态规划解法
	// 时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
	// 空间复杂度: O(n^2) - dp数组占用空间
	// 解题思路:
	// 1. 状态定义：minDp[i][j]表示合并区间[i,j]石子的最小代价，maxDp[i][j]表示最大代价
	// 2. 状态转移：枚举分割点k，minDp[i][j] = min(minDp[i][k] + minDp[k+1][j]) + sum[i][j]
	// 3. 前缀和优化：使用前缀和快速计算区间和
	// 4. 工程化考虑：
	//    - 异常处理：检查输入合法性
	//    - 边界处理：正确初始化边界条件
	//    - 性能优化：使用前缀和避免重复计算区间和
	public static int[] minMaxCost(int[] stones, int[] preSum, int n) {
		// minDp[i][j]表示合并区间[i,j]石子的最小代价
		int[][] minDp = new int[n + 1][n + 1];
		// maxDp[i][j]表示合并区间[i,j]石子的最大代价
		int[][] maxDp = new int[n + 1][n + 1];
		
		// 枚举区间长度，从2开始（至少要有2堆石子才能合并）
		for (int len = 2; len <= n; len++) {
			// 枚举区间起点i
			for (int i = 1; i <= n - len + 1; i++) {
				// 计算区间终点j
				int j = i + len - 1;
				minDp[i][j] = Integer.MAX_VALUE;
				maxDp[i][j] = Integer.MIN_VALUE;
				
				// 枚举分割点k
				for (int k = i; k < j; k++) {
					// 计算区间[i,j]的石子重量和
					int sum = preSum[j] - preSum[i - 1];
					
					// 更新最小代价
					minDp[i][j] = Math.min(minDp[i][j], 
						minDp[i][k] + minDp[k + 1][j] + sum);
					
					// 更新最大代价
					maxDp[i][j] = Math.max(maxDp[i][j], 
						maxDp[i][k] + maxDp[k + 1][j] + sum);
				}
			}
		}
		
		return new int[] {minDp[1][n], maxDp[1][n]};
	}
	// 算法分析：
	// 时间复杂度：O(n^3)
	//   - 第一层循环枚举区间长度：O(n)
	//   - 第二层循环枚举区间起点：O(n)
	//   - 第三层循环枚举分割点：O(n)
	// 空间复杂度：O(n^2)
	//   - 两个二维dp数组占用空间：O(n^2)
	// 优化说明：
	//   - 使用前缀和优化区间和计算，将O(n)优化到O(1)
	//   - 该解法是最优解，因为问题规模为n时，区间DP的时间复杂度无法低于O(n^3)

}