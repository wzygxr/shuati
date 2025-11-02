package class086;

// 潜水的最大时间与方案
// 一共有n个工具，每个工具都有自己的重量a、阻力b、提升的停留时间c
// 因为背包有限，所以只能背重量不超过m的工具
// 因为力气有限，所以只能背阻力不超过v的工具
// 希望能在水下停留的时间最久
// 返回最久的停留时间和下标字典序最小的选择工具的方案
// 注意这道题的字典序设定（根据提交的结果推论的）：
// 下标方案整体构成的字符串保证字典序最小
// 比如下标方案"1 120"比下标方案"1 2"字典序小
// 测试链接 : https://www.luogu.com.cn/problem/P1759
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

// 讲解069 - 多维费用背包
// 不做空间压缩的版本
// 无法通过全部测试用例
// 这个题必须做空间压缩
// 空间压缩的实现在Code04_Diving2
public class Code04_Diving1 {

	public static int MAXN = 101;

	public static int MAXM = 201;

	public static int[] a = new int[MAXN];

	public static int[] b = new int[MAXN];

	public static int[] c = new int[MAXN];

	public static int[][][] dp = new int[MAXN][MAXM][MAXM];

	public static String[][][] path = new String[MAXN][MAXM][MAXM];

	public static int m, v, n;

	public static void build() {
		for (int i = 1; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				for (int k = 0; k <= v; k++) {
					dp[i][j][k] = 0;
					path[i][j][k] = null;
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			m = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			in.nextToken();
			n = (int) in.nval;
			build();
			for (int i = 1; i <= n; i++) {
				in.nextToken();
				a[i] = (int) in.nval;
				in.nextToken();
				b[i] = (int) in.nval;
				in.nextToken();
				c[i] = (int) in.nval;
			}
			compute();
			out.println(dp[n][m][v]);
			out.println(path[n][m][v]);
		}
		out.flush();
		out.close();
		br.close();
	}

	/*
 * 算法详解：多维费用背包问题（潜水运动员问题）
 * 
 * 问题描述：
 * 有n个物品（潜水员的各种活动），每个物品有两个费用维度：重量a[i]（氧气消耗量）和体积b[i]（氮气消耗量），
 * 每个物品有一个价值c[i]（活动时间）。需要选择一些物品放入背包，使得总重量不超过m（氧气总量），
 * 总体积不超过v（氮气总量），且总价值（总时间）最大。同时需要记录选择的物品路径。
 * 
 * 算法思路：
 * 使用三维动态规划解决多维费用背包问题。
 * 1. 定义状态：dp[i][j][k]表示前i个物品，重量不超过j，阻力不超过k时能获得的最大价值
 * 2. 状态转移：
 *    - 不选择第i个物品：dp[i][j][k] = dp[i-1][j][k]
 *    - 选择第i个物品（需满足j>=a[i]且k>=b[i]）：
 *      dp[i][j][k] = max(dp[i][j][k], dp[i-1][j-a[i]][k-b[i]] + c[i])
 * 3. 路径记录：使用path数组记录选择方案
 * 
 * 时间复杂度分析：
 * 1. 外层循环遍历所有物品：O(n)
 * 2. 内层循环遍历重量维度：O(m)
 * 3. 最内层循环遍历阻力维度：O(v)
 * 4. 总体时间复杂度：O(n * m * v)
 * 
 * 空间复杂度分析：
 * 1. dp数组：需要存储(n+1) * (m+1) * (v+1)个状态值，空间复杂度为O(n * m * v)
 * 2. path数组：需要存储(n+1) * (m+1) * (v+1)个路径字符串，空间复杂度为O(n * m * v * L)，其中L是路径字符串平均长度
 * 3. 总体空间复杂度：O(n * m * v * L)
 * 
 * 相关题目（补充）：
 * 1. LeetCode 474. 一和零
 *    链接：https://leetcode.cn/problems/ones-and-zeroes/
 *    难度：中等
 *    描述：给你一个二进制字符串数组strs和两个整数m和n。请你找出并返回strs的最大子集的大小，该子集中
 *    最多有m个0和n个1。如果x的所有元素也是y的元素，集合x是集合y的子集。
 * 
 * 2. LeetCode 494. 目标和
 *    链接：https://leetcode.cn/problems/target-sum/
 *    难度：中等
 *    描述：给你一个整数数组nums和一个整数target。向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，
 *    可以构造一个 表达式 ：例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，
 *    然后串联起来得到表达式 "+2-1" 。返回可以通过上述方法构造的、运算结果等于target的不同表达式的数目。
 * 
 * 3. LeetCode 879. 盈利计划
 *    链接：https://leetcode.cn/problems/profitable-schemes/
 *    难度：困难
 *    描述：集团里有 n 名员工，他们可以完成各种各样的工作创造利润。第 i 种工作会产生 profit[i] 的利润，
 *    它要求 group[i] 名成员共同参与。如果成员参与了其中一项工作，就不能参与另一项工作。
 *    工作的任何至少产生 minProfit 利润的子集称为 盈利计划 。并且工作的成员总数最多为 n 。
 *    有多少种计划可以选择？因为答案很大，所以返回结果模 10^9 + 7 的值。
 * 
 * 4. LintCode 440. 背包问题 III
 *    链接：https://www.lintcode.com/problem/440/
 *    难度：中等
 *    描述：给定n种物品, 每种物品可以使用无限次，第i个物品的体积为A[i]，价值为V[i]。
 *    再给定一个容量为m的背包，问：在不超过背包容量的前提下，最多能放入多少价值的物品？
 *    注：这是一个无限背包问题，但思路可以扩展到多维。
 * 
 * 5. 牛客 NC61. 两数之和
 *    链接：https://www.nowcoder.com/practice/20ef0972485e41019e39543e8e895b7f
 *    难度：简单
 *    描述：给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那两个整数，
 *    并返回它们的数组下标。这可以看作是二维背包的特殊情况。
 * 
 * 6. 洛谷 P1507 NASA的食物计划
 *    链接：https://www.luogu.com.cn/problem/P1507
 *    难度：普及+
 *    描述：NASA计划将一批食物运上太空，但火箭的容量和重量限制是有限的。已知每种食物的体积、重量和卡路里，
 *    需要在不超过容量和重量限制的情况下，选择一些食物使得总卡路里最大。
 * 
 * 补充题目解析示例：LeetCode 474. 一和零
 * 算法思路：
 * 这是一个典型的二维费用背包问题。
 * 1. 定义状态：dp[i][j]表示使用不超过i个0和j个1时可以包含的最多字符串数量
 * 2. 状态转移方程：对于每个字符串s，计算其中的0和1的数量zeros和ones，则dp[i][j] = max(dp[i][j], dp[i-zeros][j-ones] + 1)
 * 3. 时间复杂度：O(l * m * n)，其中l是字符串数组的长度
 *    空间复杂度：O(m * n)
 * 
 * C++代码示例：
 * int findMaxForm(vector<string>& strs, int m, int n) {
 *     vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
 *     
 *     for (const string& s : strs) {
 *         int zeros = 0, ones = 0;
 *         for (char c : s) {
 *             if (c == '0') zeros++;
 *             else ones++;
 *         }
 *         
 *         for (int i = m; i >= zeros; i--) {
 *             for (int j = n; j >= ones; j--) {
 *                 dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1);
 *             }
 *         }
 *     }
 *     
 *     return dp[m][n];
 * }
 * 
 * Python代码示例：
 * def findMaxForm(strs, m, n):
 *     # 初始化dp数组
 *     dp = [[0] * (n + 1) for _ in range(m + 1)]
 *     
 *     for s in strs:
 *         # 计算当前字符串中0和1的数量
 *         zeros, ones = 0, 0
 *         for c in s:
 *             if c == '0':
 *                 zeros += 1
 *             else:
 *                 ones += 1
 *         
 *         # 从后向前遍历，避免重复使用同一物品
 *         for i in range(m, zeros - 1, -1):
 *             for j in range(n, ones - 1, -1):
 *                 dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)
 *     
 *     return dp[m][n]
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入是否合法，如null数组或负数容量
 * 2. 边界处理：处理物品重量或体积为0的情况
 * 3. 线程安全：当前实现不是线程安全的，需避免在多线程环境中共享静态变量
 * 4. 内存优化：对于大规模数据，考虑使用滚动数组优化空间（如Code04_Diving2）
 * 5. 性能优化：预计算物品的费用和价值，避免重复计算
 * 6. 输入输出效率：使用BufferedReader和BufferedWriter提高IO效率
 * 
 * 语言特性差异：
 * 1. Java：使用三维数组存储状态，需要手动初始化边界条件，使用String.compareTo进行字典序比较
 * 2. C++：可使用vector<vector<vector<int>>>，支持更灵活的内存管理，字符串比较更简洁
 * 3. Python：列表推导式使初始化更简洁，但大规模数据可能效率较低
 * 
 * 调试能力构建：
 * 1. 打印"中间过程"定位错误：可在compute函数中添加打印语句查看dp数组填充过程
 * 2. 用"断言"验证中间结果：可验证dp值的正确性和路径的合理性
 * 3. 性能退化的排查方法：可通过profiler工具分析算法瓶颈
 * 4. 小例子测试法：使用简单的测试用例验证算法正确性
 */
	// 普通版本的多维费用背包
	// 为了好懂先实现不进行空间压缩的版本
	public static void compute() {
		String p2;
		for (int i = 1; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				for (int k = 0; k <= v; k++) {
					// 可能性1 : 不要i位置的货
					// 先把可能性1的答案设置上
					// 包括dp信息和path信息
					dp[i][j][k] = dp[i - 1][j][k];
					path[i][j][k] = path[i - 1][j][k];
					if (j >= a[i] && k >= b[i]) {
						// 可能性2 : 要i位置的货
						// 那么需要:
						// 背包总重量限制j >= a[i]
						// 背包总阻力限制k >= b[i]
						// 然后选了i位置的货，就可以获得收益c[i]了
						// 可能性2收益 : dp[i-1][j-a[i]][k-b[i]] + c[i]
						// 可能性2路径(p2) : path[i-1][j-a[i]][k-b[i]] + " " + i
						if (path[i - 1][j - a[i]][k - b[i]] == null) {
							p2 = String.valueOf(i);
						} else {
							p2 = path[i - 1][j - a[i]][k - b[i]] + " " + String.valueOf(i);
						}
						if (dp[i][j][k] < dp[i - 1][j - a[i]][k - b[i]] + c[i]) {
							dp[i][j][k] = dp[i - 1][j - a[i]][k - b[i]] + c[i];
							path[i][j][k] = p2;
						} else if (dp[i][j][k] == dp[i - 1][j - a[i]][k - b[i]] + c[i]) {
							if (p2.compareTo(path[i][j][k]) < 0) {
								// 如果可能性2的路径，字典序小于，可能性1的路径
								// 那么把路径设置成可能性2的路径
								path[i][j][k] = p2;
							}
						}
					}
				}
			}
		}
	}

}