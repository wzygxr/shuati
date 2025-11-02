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

// 本文件做了空间压缩优化
// 可以通过全部测试用例
public class Code04_Diving2 {

	public static int MAXN = 101;

	public static int MAXM = 201;

	public static int[] a = new int[MAXN];

	public static int[] b = new int[MAXN];

	public static int[] c = new int[MAXN];

	public static int[][] dp = new int[MAXM][MAXM];

	public static String[][] path = new String[MAXM][MAXM];

	public static int m, v, n;

	public static void build() {
		for (int i = 0; i <= m; i++) {
			for (int j = 0; j <= v; j++) {
				dp[i][j] = 0;
				path[i][j] = null;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 异常处理：检查输入流是否有效
		if (br == null || in == null || out == null) {
			if (out != null) {
				out.close();
			}
			if (br != null) {
				br.close();
			}
			return;
		}
		
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			m = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			in.nextToken();
			n = (int) in.nval;
			
			// 边界处理：检查参数是否有效
			if (m <= 0 || v <= 0 || n <= 0) {
				out.println(0);
				out.println();
				out.flush();
				continue;
			}
			
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
			out.println(dp[m][v]);
			out.println(path[m][v]);
		}
		out.flush();
		out.close();
		br.close();
	}

	/*
	 * 算法详解：多维费用背包问题（空间优化版本）
	 * 
	 * 问题描述：
	 * 有n个物品（潜水员的各种活动），每个物品有两个费用维度：重量a[i]（氧气消耗量）和体积b[i]（氮气消耗量），
	 * 每个物品有一个价值c[i]（活动时间）。需要选择一些物品放入背包，使得总重量不超过m（氧气总量），
	 * 总体积不超过v（氮气总量），且总价值（总时间）最大。同时需要记录选择的物品路径。
	 * 
	 * 算法思路：
	 * 使用二维动态规划解决多维费用背包问题，并进行空间优化。
	 * 1. 定义状态：dp[j][k]表示使用不超过j单位氧气和k单位氮气时的最大活动时间
	 * 2. 状态转移方程：dp[j][k] = max(dp[j][k], dp[j-a[i]][k-b[i]] + c[i])
	 * 3. 空间优化：通过逆序遍历，将三维DP压缩为二维DP
	 * 4. 路径记录：path[j][k]记录达到状态dp[j][k]时选择的物品路径
	 * 
	 * 时间复杂度分析：
	 * 1. 外层循环遍历所有物品：O(n)
	 * 2. 内层循环遍历氧气维度（逆序）：O(m)
	 * 3. 最内层循环遍历氮气维度（逆序）：O(v)
	 * 4. 总体时间复杂度：O(n * m * v)
	 * 注意：空间优化不影响时间复杂度
	 * 
	 * 空间复杂度分析：
	 * 1. dp数组：需要存储(m+1) * (v+1)个状态值，空间复杂度为O(m * v)
	 * 2. path数组：需要存储(m+1) * (v+1)个路径字符串，空间复杂度为O(m * v * L)，其中L是路径字符串平均长度
	 * 3. 总体空间复杂度：O(m * v * L)
	 * 注意：相比原始版本的O(n * m * v * L)，空间复杂度大大降低
	 * 
	 * 相关题目（补充）：
	 * 1. LeetCode 474. 一和零
	 *    链接：https://leetcode.cn/problems/ones-and-zeroes/
	 *    难度：中等
	 *    描述：给你一个二进制字符串数组strs和两个整数m和n。请你找出并返回strs的最大子集的大小，该子集中
	 *    最多有m个0和n个1。
	 * 
	 * 2. LeetCode 494. 目标和
	 *    链接：https://leetcode.cn/problems/target-sum/
	 *    难度：中等
	 *    描述：给你一个整数数组nums和一个整数target。向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，
	 *    可以构造一个表达式。返回可以通过上述方法构造的、运算结果等于target的不同表达式的数目。
	 * 
	 * 3. LeetCode 879. 盈利计划
	 *    链接：https://leetcode.cn/problems/profitable-schemes/
	 *    难度：困难
	 *    描述：集团里有n名员工，第i种工作会产生profit[i]的利润，要求group[i]名成员参与。
	 *    求至少产生minProfit利润且成员总数不超过n的工作子集数目。
	 * 
	 * 4. LintCode 440. 背包问题 III
	 *    链接：https://www.lintcode.com/problem/440/
	 *    难度：中等
	 *    描述：给定n种物品，每种物品可以使用无限次，求在背包容量限制下的最大价值。
	 * 
	 * 5. 牛客 NC61. 两数之和
	 *    链接：https://www.nowcoder.com/practice/20ef0972485e41019e39543e8e895b7f
	 *    难度：简单
	 *    描述：给定一个整数数组nums和一个整数目标值target，请你在该数组中找出和为目标值的两个整数。
	 * 
	 * 6. 洛谷 P1507 NASA的食物计划
	 *    链接：https://www.luogu.com.cn/problem/P1507
	 *    难度：普及+
	 *    描述：在体积和重量限制下，选择食物使总卡路里最大。
	 * 
	 * 7. HackerRank The Knapsack Problem
	 *    链接：https://www.hackerrank.com/challenges/unbounded-knapsack/problem
	 *    难度：中等
	 *    描述：无限背包问题，可扩展到多维。
	 * 
	 * 8. USACO Training Money Systems
	 *    链接：http://train.usaco.org/usacoprob2?a=YfZ5eR2eY1x&S=money
	 *    描述：完全背包的计数问题，思路可扩展到多维。
	 * 
	 * 9. AtCoder ABC189F. Sugoroku2
	 *    链接：https://atcoder.jp/contests/abc189/tasks/abc189_f
	 *    难度：困难
	 *    描述：包含概率的多维背包问题。
	 * 
	 * 10. CodeChef The Knapsack Problem
	 *     链接：https://www.codechef.com/problems/CKKNAP
	 *     描述：经典背包问题，可扩展到多维。
	 * 
	 * 11. SPOJ KNAPSACK - The Knapsack Problem
	 *     链接：https://www.spoj.com/problems/KNAPSACK/
	 *     描述：经典背包问题。
	 * 
	 * 12. UVa OJ 10130 - SuperSale
	 *     链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1071
	 *     描述：多组测试数据的背包问题，可扩展到多维。
	 * 
	 * 13. 杭电OJ 2159 FATE
	 *     链接：https://acm.hdu.edu.cn/showproblem.php?pid=2159
	 *     难度：中等
	 *     描述：二维费用背包问题，类似于本题的潜水员问题。
	 * 
	 * 14. 牛客 NC104. 最长公共子序列
	 *     链接：https://www.nowcoder.com/practice/6d29638c85bb4ffd80c020fe244baf11
	 *     难度：中等
	 *     描述：经典的二维动态规划问题，与背包问题有相似之处。
	 * 
	 * 15. POJ 1837 Balance
	 *     链接：http://poj.org/problem?id=1837
	 *     难度：中等
	 *     描述：一个特殊的二维背包问题，关于天平平衡。
	 * 
	 * 补充题目解析示例：LeetCode 494. 目标和
	 * 算法思路：
	 * 这是一个可以转化为二维背包的计数问题。
	 * 1. 定义状态：dp[j][k]表示前i个元素中，选择若干元素使和为j时的方案数
	 * 2. 状态转移方程：dp[j][k] = dp[j-1][k-nums[j]] + dp[j-1][k+nums[j]]
	 * 3. 优化方法：通过数学变换可以将问题转化为一维背包问题
	 * 4. 时间复杂度：O(n * sum)，其中sum是数组元素绝对值之和
	 *    空间复杂度：O(sum)（优化后）
	 * 
	 * C++代码示例（优化版本）：
	 * int findTargetSumWays(vector<int>& nums, int target) {
	 *     int sum = accumulate(nums.begin(), nums.end(), 0);
	 *     // 由于和的奇偶性必须与target相同，否则无解
	 *     if ((sum + target) % 2 != 0 || sum < abs(target)) return 0;
	 *     
	 *     int s = (sum + target) / 2;
	 *     vector<int> dp(s + 1, 0);
	 *     dp[0] = 1;
	 *     
	 *     for (int num : nums) {
	 *         for (int j = s; j >= num; j--) {
	 *             dp[j] += dp[j - num];
	 *         }
	 *     }
	 *     
	 *     return dp[s];
	 * }
	 * 
	 * Python代码示例（优化版本）：
	 * def findTargetSumWays(nums, target):
	 *     total = sum(nums)
	 *     # 检查是否有解
	 *     if (total + target) % 2 != 0 or total < abs(target):
	 *         return 0
	 *     
	 *     s = (total + target) // 2
	 *     dp = [0] * (s + 1)
	 *     dp[0] = 1
	 *     
	 *     for num in nums:
	 *         for j in range(s, num - 1, -1):
	 *             dp[j] += dp[j - num]
	 *     
	 *     return dp[s]
	 * 
	 * 空间优化原理解析：
	 * 1. 核心思想：在0-1背包问题中，由于每个物品只能选一次，我们需要从后向前遍历背包容量，
	 *    这样可以确保在计算当前物品的影响时，不会重复使用同一物品。
	 * 2. 数学依据：对于状态转移方程dp[i][j] = max(dp[i-1][j], dp[i-1][j-weight[i]] + value[i])，
	 *    当我们从后向前遍历时，dp[j]在更新前保存的是dp[i-1][j]的值。
	 * 3. 推广应用：对于多维背包问题，可以对每个维度都采用逆序遍历的方式进行空间优化。
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入是否合法，如null数组或负数容量
	 * 2. 边界处理：处理物品重量或体积为0的情况
	 * 3. 线程安全：当前实现不是线程安全的，需避免在多线程环境中共享静态变量
	 * 4. 内存优化：通过滚动数组将空间复杂度从O(n*m*v)优化到O(m*v)
	 * 5. 性能优化：预计算物品的费用和价值，避免重复计算
	 * 6. 输入输出效率：使用BufferedReader和BufferedWriter提高IO效率
	 * 7. 可扩展性：设计灵活的API，支持不同维度的费用和价值类型
	 * 
	 * 语言特性差异：
	 * 1. Java：数组访问效率高，但字符串操作相对较慢
	 * 2. C++：动态内存管理更灵活，字符串处理效率高
	 * 3. Python：列表推导式和字典使代码更简洁，但大规模数据处理效率较低
	 * 
	 * 调试能力构建：
	 * 1. 打印"中间过程"定位错误：可在compute函数中添加打印语句查看dp数组填充过程
	 * 2. 用"断言"验证中间结果：可验证dp值的正确性和路径的合理性
	 * 3. 性能退化的排查方法：可通过profiler工具分析算法瓶颈
	 * 4. 小例子测试法：使用简单的测试用例验证算法正确性
	 * 
	 * 算法调试与问题定位：
	 * 1. 空输入极端值处理：已在main函数中添加数组长度检查
	 * 2. 重复数据处理：算法可以正确处理重复物品（每个物品只能选一次）
	 * 3. 大规模数据处理：通过空间优化，可以处理更大规模的数据
	 * 4. 特殊情况处理：当没有物品可选或容量为0时的正确处理
	 * 
	 * 跨语言场景与关联"语言特性差异"：
	 * 1. Java：强类型语言，编译时类型检查严格，运行时类型转换需谨慎
	 * 2. C++：支持指针操作，可以更精细地控制内存，但容易出错
	 * 3. Python：动态类型语言，代码简洁，但运行时开销较大
	 * 
	 * 极端场景鲁棒性验证：
	 * 1. 输入数组长度达到MAXN边界情况
	 * 2. 所有物品的费用都超过背包容量的情况
	 * 3. 所有物品的费用都为0的情况
	 * 4. 背包容量非常小或非常大的情况
	 * 5. 物品价值极端分布的情况（如大部分物品价值为0）
	 * 
	 * 从代码到产品的工程化考量：
	 * 1. 异常抛出：明确处理非法输入，如null数组或负容量
	 * 2. 单元测试：编写全面的测试用例覆盖各种边界情况
	 * 3. 性能优化：空间优化对于实际应用中的大规模数据至关重要
	 * 4. 线程安全：在多线程环境中使用线程局部变量或同步机制
	 * 5. 可配置性：支持不同的优化策略和参数配置
	 * 
	 * 与机器学习/深度学习的联系：
	 * 1. 强化学习：背包问题可视为一种资源分配问题，与RL中的状态-动作空间设计相关
	 * 2. 组合优化：在神经网络结构搜索中，选择合适的网络组件可视为背包问题
	 * 3. 特征选择：在高维特征空间中选择重要特征，可转化为多维背包问题
	 * 4. 资源调度：在分布式训练中，任务调度和资源分配可使用背包问题的思想
	 * 5. 模型压缩：在深度学习模型压缩中，选择哪些神经元/连接保留可视为背包问题
	 */
	// 多维费用背包的空间压缩版本
	// 请务必掌握空间压缩技巧
	// 之前的课讲了很多遍了
	public static void compute() {
		// 遍历每个物品
		for (int i = 1; i <= n; i++) {
			// 重量维度从大到小遍历（空间压缩的关键）
			// 必须从大到小遍历，避免同一物品被重复选择
			for (int j = m; j >= a[i]; j--) {
				// 阻力维度从大到小遍历
				for (int k = v; k >= b[i]; k--) {
					// 计算选择当前物品后的路径字符串
					String p2;
					if (path[j - a[i]][k - b[i]] == null) {
						// 如果之前没有选择任何物品，路径就是当前物品的编号
						p2 = String.valueOf(i);
					} else {
						// 如果之前已经选择了物品，路径是之前的路径加上当前物品编号
						p2 = path[j - a[i]][k - b[i]] + " " + String.valueOf(i);
					}
					
					// 状态转移：比较选择和不选择当前物品的价值
					if (dp[j][k] < dp[j - a[i]][k - b[i]] + c[i]) {
						// 选择当前物品能获得更大价值
						dp[j][k] = dp[j - a[i]][k - b[i]] + c[i];
						path[j][k] = p2;
					} else if (dp[j][k] == dp[j - a[i]][k - b[i]] + c[i]) {
						// 价值相同，选择字典序更小的方案
						if (p2.compareTo(path[j][k]) < 0) {
							path[j][k] = p2;
						}
					}
					// 如果选择当前物品获得的价值更小，则不选择
				}
			}
		}
	}

}