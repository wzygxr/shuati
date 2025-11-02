package class131;

/**
 * 洛谷 P3287 [SCOI2014]方伯伯的玉米田
 * 题目链接: https://www.luogu.com.cn/problem/P3287
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，每次可以选择一个区间[l,r]，区间内的数字都+1，最多执行k次
 * 返回执行完成后，最长的不下降子序列长度。
 * 
 * 解题思路:
 * 使用二维树状数组优化动态规划的方法解决此问题。
 * 1. 定义状态dp[i][j][h]表示处理前i个元素，使用j次操作，以高度h结尾的最长不下降子序列长度
 * 2. 由于高度范围较大，我们使用二维树状数组来维护状态
 * 3. 树状数组的第一维表示高度，第二维表示操作次数
 * 4. 对于每个元素，枚举可能的操作次数，查询最优解并更新状态
 * 
 * 时间复杂度分析:
 * - 状态转移: O(n*k*log(MAXH)*log(k))
 * - 总时间复杂度: O(n*k*log(MAXH)*log(k))
 * 空间复杂度: O(MAXH*k) 用于存储二维树状数组
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_CornField {

	// 最大数组长度常量
	public static int MAXN = 10001;  // 最大元素个数

	public static int MAXK = 501;    // 最大操作次数+1

	public static int MAXH = 5500;   // 最大高度值

	// 输入数组
	public static int[] arr = new int[MAXN];

	// 二维树状数组，tree[x][y]表示高度不超过x、操作次数不超过y时的最长不下降子序列长度
	public static int[][] tree = new int[MAXH + 1][MAXK + 1];

	// 全局变量
	public static int n, k;  // n:数组长度, k:最大操作次数

	/**
	 * 二维树状数组更新操作
	 * 在位置(x,y)更新值为v（取最大值）
	 * 
	 * @param x 第一维位置（高度）
	 * @param y 第二维位置（操作次数）
	 * @param v 新的值
	 */
	public static void update(int x, int y, int v) {
		// 更新第一维
		for (int i = x; i <= MAXH; i += i & -i) {
			// 更新第二维
			for (int j = y; j <= k + 1; j += j & -j) {
				// 只有当新值更大时才更新
				tree[i][j] = Math.max(tree[i][j], v);
			}
		}
	}

	/**
	 * 二维树状数组查询操作
	 * 查询位置(1..x, 1..y)范围内的最大值
	 * 
	 * @param x 第一维查询范围
	 * @param y 第二维查询范围
	 * @return  前缀最大值
	 */
	public static int max(int x, int y) {
		int ans = 0;
		// 查询第一维
		for (int i = x; i > 0; i -= i & -i) {
			// 查询第二维
			for (int j = y; j > 0; j -= j & -j) {
				// 在所有祖先节点中找最大值
				ans = Math.max(ans, tree[i][j]);
			}
		}
		return ans;
	}

	/**
	 * 主函数，读取输入并输出结果
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 使用BufferedReader提高输入效率
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取数组长度n和最大操作次数k
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		k = (int) in.nval;
		
		// 读取数组元素
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		
		// 计算并输出结果
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算最长不下降子序列长度
	 * 
	 * @return 最长不下降子序列长度
	 */
	public static int compute() {
		// 注意这里第二层for循环，j一定是从k~0的枚举
		// 课上进行了重点图解，防止同一个i产生的记录之间相互影响
		int v, dp;
		
		// 遍历每个元素
		for (int i = 1; i <= n; i++) {
			// 枚举操作次数（从k到0）
			// 注意必须从k到0枚举，防止同一个i产生的记录之间相互影响
			for (int j = k; j >= 0; j--) {
				// 当前元素经过j次操作后的高度
				v = arr[i] + j;
				
				// 修改次数j，树状数组中对应的下标是j+1
				// 查询以高度不超过v、操作次数不超过j的元素结尾的最长不下降子序列长度
				dp = max(v, j + 1) + 1;
				
				// 更新以高度v、操作次数j+1结尾的最长不下降子序列长度
				update(v, j + 1, dp);
			}
		}
		
		// 修改次数k，树状数组中对应的下标是k+1
		// 返回所有可能情况下的最长不下降子序列长度
		return max(MAXH, k + 1);
	}

}
