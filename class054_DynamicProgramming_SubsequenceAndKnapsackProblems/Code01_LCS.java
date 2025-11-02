package class086;

// 最长公共子序列其中一个结果
// 给定两个字符串str1和str2
// 输出两个字符串的最长公共子序列
// 如果最长公共子序列为空，则输出-1
// 测试链接 : https://www.nowcoder.com/practice/4727c06b9ee9446cab2e859b4bb86bb8
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

// 讲解067 - 题目3，最长公共子序列长度
public class Code01_LCS {

	public static int MAXN = 5001;

	public static int[][] dp = new int[MAXN][MAXN];

	public static char[] ans = new char[MAXN];

	public static char[] s1;

	public static char[] s2;

	public static int n, m, k;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		s1 = br.readLine().toCharArray();
		s2 = br.readLine().toCharArray();
		n = s1.length;
		m = s2.length;
		
		// 异常处理：检查输入是否为空
		if (n == 0 || m == 0) {
			out.println(-1);
			out.flush();
			out.close();
			br.close();
			return;
		}
		
		lcs();
		if (k == 0) {
			out.println(-1);
		} else {
			for (int i = 0; i < k; i++) {
				out.print(ans[i]);
			}
			out.println();
		}
		out.flush();
		out.close();
		br.close();
	}

	/*
	 * 算法详解：最长公共子序列（LCS）
	 * 
	 * 问题描述：
	 * 给定两个字符串str1和str2，找出它们的最长公共子序列。
	 * 子序列是指在不改变字符相对顺序的前提下，删除某些字符后得到的新序列。
	 * 
	 * 算法思路：
	 * 使用动态规划方法解决。
	 * 1. 定义状态：dp[i][j]表示str1[0..i-1]和str2[0..j-1]的最长公共子序列长度
	 * 2. 状态转移方程：
	 *    - 如果s1[i-1] == s2[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
	 *    - 否则dp[i][j] = max(dp[i-1][j], dp[i][j-1])
	 * 3. 构造结果：通过回溯dp表构造一个具体的LCS
	 * 
	 * 时间复杂度分析：
	 * 1. 填充dp表：需要遍历两个字符串的所有字符组合，时间复杂度为O(n*m)
	 * 2. 回溯构造LCS：最坏情况下需要遍历整个dp表，时间复杂度为O(n+m)
	 * 3. 总体时间复杂度：O(n*m)
	 * 
	 * 空间复杂度分析：
	 * 1. dp数组：需要存储n*m个状态值，空间复杂度为O(n*m)
	 * 2. ans数组：最多存储min(n,m)个字符，空间复杂度为O(min(n,m))
	 * 3. 总体空间复杂度：O(n*m)
	 * 
	 * 相关题目（补充）：
	 * 1. LeetCode 1143. 最长公共子序列
	 *    链接：https://leetcode.cn/problems/longest-common-subsequence/
	 *    难度：中等
	 *    描述：给定两个字符串text1和text2，返回这两个字符串的最长公共子序列的长度。
	 *    注意：子序列定义为通过删除一些字符而不改变其余字符的相对顺序所形成的新字符串。
	 * 
	 * 2. LeetCode 1092. 最短公共超序列
	 *    链接：https://leetcode.cn/problems/shortest-common-supersequence/
	 *    难度：困难
	 *    描述：给你两个字符串str1和str2，返回同时以str1和str2作为子序列的最短字符串。
	 *    如果答案不止一个，则可以返回满足条件的任意一个答案。
	 * 
	 * 3. LeetCode 583. 两个字符串的删除操作
	 *    链接：https://leetcode.cn/problems/delete-operation-for-two-strings/
	 *    难度：中等
	 *    描述：给定两个单词word1和word2，找到使得word1和word2相同所需的最小步数，
	 *    每步可以删除任意一个字符串中的一个字符。
	 * 
	 * 4. LeetCode 712. 两个字符串的最小ASCII删除和
	 *    链接：https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/
	 *    难度：中等
	 *    描述：给定两个字符串s1和s2，返回使两个字符串相等所需删除字符的ASCII值的最小和。
	 * 
	 * 5. LeetCode 72. 编辑距离
	 *    链接：https://leetcode.cn/problems/edit-distance/
	 *    难度：困难
	 *    描述：给你两个单词word1和word2，计算出将word1转换成word2所使用的最少操作数。
	 *    你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
	 * 
	 * 6. LintCode 77. 最长公共子序列
	 *    链接：https://www.lintcode.com/problem/77/
	 *    难度：中等
	 *    描述：给定两个字符串，求它们的最长公共子序列的长度。
	 * 
	 * 7. 牛客 NC127. 最长公共子串
	 *    链接：https://www.nowcoder.com/practice/f33f5adc55f444baa0e0ca87ad8a6aac
	 *    难度：中等
	 *    描述：给定两个字符串str1和str2，输出两个字符串的最长公共子串长度。
	 *    子串是连续的子序列。
	 * 
	 * 8. CodeForces 1637E. Best Pair
	 *    链接：https://codeforces.com/contest/1637/problem/E
	 *    难度：中等
	 *    描述：给定一个数组，找出两个数x和y，使得x和y的二进制表示的LCS长度最大。
	 * 
	 * 9. 洛谷 P1439 最长公共子序列
	 *    链接：https://www.luogu.com.cn/problem/P1439
	 *    难度：普及+/提高
	 *    描述：给定两个序列，求它们的最长公共子序列的长度。
	 *    提示：可以利用LIS优化。
	 * 
	 * 10. HackerRank Common Child
	 *     链接：https://www.hackerrank.com/challenges/common-child/problem
	 *     难度：中等
	 *     描述：给定两个字符串，求它们的最长公共子序列的长度。
	 * 
	 * 11. USACO Training LCS
	 *     链接：http://train.usaco.org/usacoprob2?a=QnPm3K79&S=lcs
	 *     描述：求两个字符串的最长公共子序列。
	 * 
	 * 12. AtCoder ABC144E. Gluttony
	 *     链接：https://atcoder.jp/contests/abc144/tasks/abc144_e
	 *     难度：中等
	 *     描述：给定两个数组，求LCS的变种问题。
	 * 
	 * 13. Project Euler Problem 421
	 *     链接：https://projecteuler.net/problem=421
	 *     描述：涉及到LCS的数论问题。
	 * 
	 * 14. SPOJ LCS
	 *     链接：https://www.spoj.com/problems/LCS/
	 *     描述：求两个字符串的最长公共子序列。
	 * 
	 * 15. UVa OJ 111 - History Grading
	 *     链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&page=show_problem&problem=47
	 *     描述：将问题转化为LCS求解。
	 * 
	 * 补充题目解析示例：LeetCode 72. 编辑距离
	 * 算法思路：
	 * 编辑距离问题是LCS的一个扩展，可以使用动态规划解决。
	 * 1. 定义状态：dp[i][j]表示将word1的前i个字符转换为word2的前j个字符所需的最小操作数
	 * 2. 状态转移方程：
	 *    - 如果word1[i-1] == word2[j-1]：dp[i][j] = dp[i-1][j-1]
	 *    - 否则：dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
	 *      分别对应删除、插入和替换操作
	 * 3. 时间复杂度：O(m*n)，空间复杂度：O(m*n)
	 * 
	 * C++代码示例：
	 * int minDistance(string word1, string word2) {
	 *     int m = word1.size(), n = word2.size();
	 *     vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
	 *     
	 *     // 初始化边界条件
	 *     for (int i = 0; i <= m; i++) dp[i][0] = i;
	 *     for (int j = 0; j <= n; j++) dp[0][j] = j;
	 *     
	 *     for (int i = 1; i <= m; i++) {
	 *         for (int j = 1; j <= n; j++) {
	 *             if (word1[i-1] == word2[j-1]) {
	 *                 dp[i][j] = dp[i-1][j-1];
	 *             } else {
	 *                 dp[i][j] = min({dp[i-1][j], dp[i][j-1], dp[i-1][j-1]}) + 1;
	 *             }
	 *         }
	 *     }
	 *     return dp[m][n];
	 * }
	 * 
	 * Python代码示例：
	 * def minDistance(word1, word2):
	 *     m, n = len(word1), len(word2)
	 *     # 空间优化，只使用两行
	 *     if m < n:  # 确保n是较小的，减少空间使用
	 *         word1, word2, m, n = word2, word1, n, m
	 *     
	 *     prev = list(range(n + 1))
	 *     curr = [0] * (n + 1)
	 *     
	 *     for i in range(1, m + 1):
	 *         curr[0] = i
	 *         for j in range(1, n + 1):
	 *             if word1[i-1] == word2[j-1]:
	 *                 curr[j] = prev[j-1]
	 *             else:
	 *                 curr[j] = min(prev[j], curr[j-1], prev[j-1]) + 1
	 *         prev, curr = curr, prev
	 *     
	 *     return prev[n]
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入是否为空
	 * 2. 空间优化：可以使用滚动数组将空间复杂度从O(n*m)优化到O(min(n,m))
	 * 3. 线程安全：当前实现不是线程安全的，如需线程安全应避免使用静态变量
	 * 4. 可配置性：MAXN常量定义了最大输入长度，可根据实际需求调整
	 * 5. 性能优化：使用字符数组而非字符串操作提高访问效率
	 * 6. 测试用例：应覆盖空输入、相同字符串、完全不同字符串等边界情况
	 * 7. 文档化：提供算法说明、时间空间复杂度分析、使用示例
	 * 
	 * 语言特性差异：
	 * 1. Java：使用字符数组提高访问效率，需要手动管理数组边界
	 * 2. C++：可使用vector或原生数组，支持STL算法如min_element
	 * 3. Python：字符串操作简洁但效率较低，可使用numpy优化数组操作
	 * 
	 * 调试能力构建：
	 * 1. 打印"中间过程"定位错误：可在dp函数中添加打印语句查看dp表填充过程
	 * 2. 用"断言"验证中间结果：可在回溯过程中添加断言验证状态转移正确性
	 * 3. 性能退化的排查方法：可通过profiler工具分析算法瓶颈
	 * 4. 小例子测试法：使用简单的测试用例（如"abcde"和"ace"）验证算法正确性
	 * 
	 * 算法调试与问题定位：
	 * 1. 空输入极端值处理：已在main函数中添加输入为空的检查
	 * 2. 重复数据处理：算法天然支持处理重复字符
	 * 3. 有序逆序数据处理：算法对输入数据顺序不敏感
	 * 4. 特殊格式处理：算法适用于任何ASCII字符
	 * 5. 大规模数据处理：对于超长字符串，需要考虑空间优化
	 * 
	 * 跨语言场景与关联"语言特性差异"：
	 * 1. Java：字符数组访问效率高，但需要注意数组边界和内存使用
	 * 2. C++：可使用原生数组获得更好性能，但需手动管理内存和对象生命周期
	 * 3. Python：字符串操作简洁但效率较低，对于大规模数据可能需要优化
	 * 
	 * 极端场景鲁棒性验证：
	 * 1. 输入字符串长度达到MAXN边界情况
	 * 2. 两个字符串完全相同的情况
	 * 3. 两个字符串完全不同的情况
	 * 4. 一个字符串为空的情况
	 * 5. 两个字符串都为空的情况
	 * 6. 字符串包含特殊字符或非ASCII字符的情况
	 * 
	 * 从代码到产品的工程化考量：
	 * 1. 异常抛出：明确处理非法输入，如null指针或超大输入
	 * 2. 单元测试：编写全面的单元测试用例，覆盖各种边界情况
	 * 3. 性能优化：对于大规模数据，实现空间优化版本
	 * 4. 线程安全：考虑多线程环境下的并发访问问题
	 * 5. 可扩展性：设计灵活的API，支持不同类型的输入和扩展需求
	 * 
	 * 与机器学习/深度学习的联系：
	 * 1. 序列比对：LCS算法在生物信息学中的DNA序列比对有重要应用
	 * 2. 自然语言处理：在文本相似度计算、机器翻译评估中使用LCS
	 * 3. 推荐系统：用于计算用户行为序列的相似度
	 * 4. 图像识别：在图像特征序列比较中应用
	 */
	public static void lcs() {
		dp();
		k = dp[n][m];
		if (k > 0) {
			// 通过dp表回溯构造LCS
			// 回溯过程从dp[n][m]开始，逐步寻找构成LCS的字符
			// len表示当前还需要确定的LCS字符数量
			// i和j分别表示在s1和s2中的当前位置
			for (int len = k, i = n, j = m; len > 0;) {
				// 如果当前字符相等，说明该字符是LCS的一部分
				if (s1[i - 1] == s2[j - 1]) {
					// 将字符添加到结果数组的正确位置
					ans[--len] = s1[i - 1];
					// 同时在两个字符串中向前移动
					i--;
					j--;
				} else {
					// 如果当前字符不相等，选择较大的方向继续回溯
					// 这保证了我们能找到长度为dp[n][m]的LCS
					if (dp[i - 1][j] >= dp[i][j - 1]) {
						// 选择向上移动（在s1中向前移动）
						i--;
					} else {
						// 选择向左移动（在s2中向前移动）
						j--;
					}
				}
			}
		}
	}

	// 填好dp表
	// 使用动态规划填充二维数组dp，其中dp[i][j]表示s1[0..i-1]和s2[0..j-1]的LCS长度
	public static void dp() {
		// 初始化边界条件：空字符串与任何字符串的LCS长度为0
		for (int i = 0; i <= n; i++) {
			dp[i][0] = 0;
		}
		for (int j = 0; j <= m; j++) {
			dp[0][j] = 0;
		}
		
		// 填充dp表
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				// 状态转移方程的核心逻辑
				if (s1[i - 1] == s2[j - 1]) {
					// 如果当前字符相等，则LCS长度为前缀LCS长度加1
					dp[i][j] = 1 + dp[i - 1][j - 1];
				} else {
					// 如果当前字符不相等，则取两种情况的最大值
					// 1. 不包含s1[i-1]的LCS长度：dp[i-1][j]
					// 2. 不包含s2[j-1]的LCS长度：dp[i][j-1]
					dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
				}
			}
		}
	}

}