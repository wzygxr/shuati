package class086;

// 最长递增子序列字典序最小的结果
// 给定数组arr，设长度为n
// 输出arr的最长递增子序列
// 如果有多个答案，请输出其中字典序最小的
// 注意这道题的字典序设定（根据提交的结果推论的）：
// 每个数字看作是单独的字符，比如120认为比36的字典序大
// 保证从左到右每个数字尽量小
// 测试链接 : https://www.nowcoder.com/practice/30fb9b3cab9742ecae9acda1c75bf927
// 测试链接 : https://www.luogu.com.cn/problem/T386911
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

// 讲解072 - 最长递增子序列及其扩展
public class Code03_LIS {

	public static int MAXN = 100001;

	public static int[] nums = new int[MAXN];

	public static int[] dp = new int[MAXN];

	public static int[] ends = new int[MAXN];

	public static int[] ans = new int[MAXN];

	public static int n, k;

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
			n = (int) in.nval;
			
			// 边界处理：检查数组长度是否有效
			if (n <= 0 || n > MAXN) {
				out.println();
				out.flush();
				continue;
			}
			
			for (int i = 0; i < n; i++) {
				in.nextToken();
				nums[i] = (int) in.nval;
			}
			lis();
			for (int i = 0; i < k - 1; i++) {
				out.print(ans[i] + " ");
			}
			out.println(ans[k - 1]);
		}
		out.flush();
		out.close();
		br.close();
	}

	/*
	 * 算法详解：最长递增子序列（LIS）
	 * 
	 * 问题描述：
	 * 给定一个整数数组，找出其中最长严格递增子序列的长度，并构造字典序最小的一个结果。
	 * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
	 * 
	 * 算法思路：
	 * 1. 使用贪心+二分查找的方法计算LIS长度，时间复杂度O(n log n)
	 * 2. 通过从右到左遍历数组，结合dp数组构造字典序最小的LIS
	 * 
	 * 核心思想：
	 * - dp[i]表示以nums[i]开头的最长递增子序列长度
	 * - ends数组维护长度为i+1的递增子序列的最小末尾元素
	 * - 通过二分查找快速定位位置
	 * 
	 * 时间复杂度分析：
	 * 1. dp函数：O(n log n)
	 *    - 外层循环：O(n)
	 *    - 内层二分查找：O(log n)
	 * 2. lis函数构造结果：O(n)
	 * 3. 总体时间复杂度：O(n log n)
	 * 
	 * 空间复杂度分析：
	 * 1. nums数组：O(n)
	 * 2. dp数组：O(n)
	 * 3. ends数组：O(n)
	 * 4. ans数组：O(n)
	 * 5. 总体空间复杂度：O(n)
	 * 
	 * 相关题目（补充）：
	 * 1. LeetCode 300. 最长递增子序列
	 *    链接：https://leetcode.cn/problems/longest-increasing-subsequence/
	 *    难度：中等
	 *    描述：给定一个无序的整数数组，找到其中最长上升子序列的长度。
	 * 
	 * 2. LeetCode 673. 最长递增子序列的个数
	 *    链接：https://leetcode.cn/problems/number-of-longest-increasing-subsequence/
	 *    难度：中等
	 *    描述：给定一个未排序的整数数组，找到最长递增子序列的个数。
	 * 
	 * 3. LeetCode 354. 俄罗斯套娃信封问题
	 *    链接：https://leetcode.cn/problems/russian-doll-envelopes/
	 *    难度：困难
	 *    描述：给定一些标记了宽度和高度的信封，求最多能有多少个信封能组成俄罗斯套娃信封序列。
	 * 
	 * 4. LeetCode 646. 最长数对链
	 *    链接：https://leetcode.cn/problems/maximum-length-of-pair-chain/
	 *    难度：中等
	 *    描述：给出 n 个数对。在每一个数对中，第一个数字总是比第二个数字小。
	 *    现在，我们定义一种跟随关系，当且仅当 b < c 时，数对(c, d) 可以跟在 (a, b) 后面。我们用这种形式来构造一个数对链。
	 *    找出能够形成的最长数对链的长度。
	 * 
	 * 5. LeetCode 1964. 找出到每个位置为止最长的有效障碍赛跑路线
	 *    链接：https://leetcode.cn/problems/find-the-longest-valid-obstacle-course-at-each-position/
	 *    难度：困难
	 *    描述：给定一个障碍物数组，找到每个位置的最长递增子序列长度（允许相等）。
	 * 
	 * 6. LeetCode 491. 递增子序列
	 *    链接：https://leetcode.cn/problems/increasing-subsequences/
	 *    难度：中等
	 *    描述：给定一个整型数组, 你的任务是找到所有该数组的递增子序列，递增子序列的长度至少是2。
	 * 
	 * 7. LintCode 76. 最长上升子序列
	 *    链接：https://www.lintcode.com/problem/76/
	 *    难度：中等
	 *    描述：给定一个整数序列，找到最长上升子序列（LIS），返回LIS的长度。
	 * 
	 * 8. 牛客 NC134. 最长递增子序列(二)
	 *    链接：https://www.nowcoder.com/practice/22e9ff2b08874e08b81c2161a71d9da8
	 *    难度：中等
	 *    描述：给定一个数组，输出字典序最小的最长递增子序列。
	 * 
	 * 9. 洛谷 P1020. 导弹拦截
	 *    链接：https://www.luogu.com.cn/problem/P1020
	 *    难度：普及+/提高
	 *    描述：计算拦截所有导弹所需的最少拦截系统数量，这是一个经典的LIS应用。
	 * 
	 * 10. HackerRank The Longest Increasing Subsequence
	 *     链接：https://www.hackerrank.com/challenges/longest-increasing-subsequence/problem
	 *     难度：中等
	 *     描述：求最长递增子序列的长度。
	 * 
	 * 11. USACO Silver Longest Increasing Subsequence
	 *     链接：http://train.usaco.org/usacoprob2?a=7kF3V6eJ73V&S=lis
	 *     描述：经典LIS问题。
	 * 
	 * 12. AtCoder ABC164D. Multiple of 2019
	 *     链接：https://atcoder.jp/contests/abc164/tasks/abc164_d
	 *     难度：中等
	 *     描述：涉及LIS思想的变种问题。
	 * 
	 * 13. CodeChef Longest Increasing Subsequence
	 *     链接：https://www.codechef.com/problems/ILUL
	 *     描述：求最长递增子序列。
	 * 
	 * 14. SPOJ ELIS - Easy Longest Increasing Subsequence
	 *     链接：https://www.spoj.com/problems/ELIS/
	 *     描述：求最长递增子序列的长度。
	 * 
	 * 15. UVa OJ 481 - What Goes Up
	 *     链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=422
	 *     描述：求最长递增子序列并输出。
	 * 
	 * 补充题目解析示例：LeetCode 354. 俄罗斯套娃信封问题
	 * 算法思路：
	 * 这是一个二维LIS问题，可以通过排序和一维LIS解决。
	 * 1. 按宽度从小到大排序，如果宽度相同，则按高度从大到小排序
	 * 2. 对排序后的高度数组求LIS长度
	 * 3. 时间复杂度：排序O(n log n) + LIS O(n log n) = O(n log n)
	 *    空间复杂度：O(n)
	 * 
	 * C++代码示例：
	 * int maxEnvelopes(vector<vector<int>>& envelopes) {
	 *     if (envelopes.empty()) return 0;
	 *     
	 *     // 按宽度升序，高度降序排序
	 *     sort(envelopes.begin(), envelopes.end(), [](const vector<int>& a, const vector<int>& b) {
	 *         return a[0] < b[0] || (a[0] == b[0] && a[1] > b[1]);
	 *     });
	 *     
	 *     vector<int> tails;
	 *     for (const auto& env : envelopes) {
	 *         int h = env[1];
	 *         auto it = lower_bound(tails.begin(), tails.end(), h);
	 *         if (it == tails.end()) {
	 *             tails.push_back(h);
	 *         } else {
	 *             *it = h;
	 *         }
	 *     }
	 *     return tails.size();
	 * }
	 * 
	 * Python代码示例：
	 * def maxEnvelopes(envelopes):
	 *     if not envelopes:
	 *         return 0
	 *     
	 *     # 按宽度升序，高度降序排序
	 *     envelopes.sort(key=lambda x: (x[0], -x[1]))
	 *     
	 *     # 对高度数组求LIS
	 *     tails = []
	 *     for _, h in envelopes:
	 *         left, right = 0, len(tails)
	 *         while left < right:
	 *             mid = (left + right) // 2
	 *             if tails[mid] < h:
	 *                 left = mid + 1
	 *             else:
	 *                 right = mid
	 *         if left == len(tails):
	 *             tails.append(h)
	 *         else:
	 *             tails[left] = h
	 *     
	 *     return len(tails)
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入数组是否为空或长度为0
	 * 2. 边界处理：处理重复元素和相同长度的情况
	 * 3. 线程安全：当前实现不是线程安全的
	 * 4. 性能优化：使用二分查找将时间复杂度从O(n^2)优化到O(n log n)
	 * 5. 内存管理：合理使用静态数组减少内存分配开销
	 * 6. 代码可读性：使用有意义的变量名，添加清晰的注释
	 * 7. 可扩展性：设计灵活的API，支持不同类型的输入
	 * 
	 * 语言特性差异：
	 * 1. Java：使用数组和二分查找实现，需要手动实现二分逻辑
	 * 2. C++：可使用vector和lower_bound函数，代码更简洁
	 * 3. Python：可使用bisect模块简化二分查找，语法更优雅
	 * 
	 * 调试能力构建：
	 * 1. 打印"中间过程"定位错误：可在dp函数中添加打印语句查看ends数组变化
	 * 2. 用"断言"验证中间结果：可验证dp数组和ends数组的正确性
	 * 3. 性能退化的排查方法：可通过profiler工具分析算法瓶颈
	 * 4. 断点式打印：在关键循环中打印变量值，观察算法执行过程
	 * 
	 * 算法调试与问题定位：
	 * 1. 空输入极端值处理：已在main函数中添加数组长度检查
	 * 2. 重复数据处理：算法能正确处理重复元素
	 * 3. 有序逆序数据处理：对有序和逆序数组都有良好性能
	 * 4. 特殊格式处理：适用于任何整数数组
	 * 5. 极端大规模数据：对于接近MAXN的数组，需确保内存足够
	 * 
	 * 跨语言场景与关联"语言特性差异"：
	 * 1. Java：数组访问效率高，但需要注意数组边界和索引计算
	 * 2. C++：STL容器提供了更丰富的功能，但需要注意迭代器失效问题
	 * 3. Python：列表操作简洁但效率相对较低，大规模数据可考虑使用numpy
	 * 
	 * 极端场景鲁棒性验证：
	 * 1. 输入数组长度达到MAXN边界情况
	 * 2. 数组元素全部相同的情况
	 * 3. 数组元素严格递增的情况
	 * 4. 数组元素严格递减的情况
	 * 5. 数组元素随机分布的情况
	 * 6. 数组包含负数的情况
	 * 
	 * 从代码到产品的工程化考量：
	 * 1. 异常抛出：明确处理非法输入，如null数组或超大输入
	 * 2. 单元测试：编写全面的测试用例覆盖各种边界情况
	 * 3. 性能优化：对大规模数据实现高效的二分查找
	 * 4. 线程安全：在多线程环境中使用线程局部变量或同步机制
	 * 5. 代码重构：将算法封装为可复用的组件
	 * 
	 * 与机器学习/深度学习的联系：
	 * 1. 特征选择：LIS思想可用于时间序列特征的重要性排序
	 * 2. 序列预测：在序列预测任务中，LIS可用于评估预测质量
	 * 3. 推荐系统：用户行为序列的模式识别
	 * 4. 自然语言处理：在句子结构分析中的应用
	 */
	public static void lis() {
		// 计算LIS长度
		k = dp();
		
		// 初始化结果数组为最大值，确保字典序最小
		Arrays.fill(ans, 0, k, Integer.MAX_VALUE);
		
		// 构造字典序最小的LIS
		// 从左到右遍历原数组
		for (int i = 0; i < n; i++) {
			// 如果以nums[i]开头的LIS长度等于最长长度
			if (dp[i] == k) {
				// 注意这里为什么不用判断直接设置
				// 因为我们是从左到右遍历，且ans数组初始化为最大值
				// 第一个满足条件的元素一定是字典序最小的选择
				ans[0] = nums[i];
			} else {
				// 如果以nums[i]开头的LIS长度小于最长长度
				// 检查是否可以将nums[i]放在当前LIS的合适位置
				// ans[k - dp[i] - 1]表示LIS中倒数第dp[i]+1个位置的元素
				if (ans[k - dp[i] - 1] < nums[i]) {
					// 注意这里为什么只需要判断比前一位大即可
					// 因为我们要构造字典序最小的LIS
					// 如果当前元素比前一位大，说明可以作为LIS的一部分
					// 且由于是从左到右遍历，保证了字典序最小
					ans[k - dp[i]] = nums[i];
				}
			}
		}
	}

	// dp[i] : 必须以i位置的数字开头的情况下，最长递增子序列长度
	// 填好dp表 + 返回最长递增子序列长度
	public static int dp() {
		// len表示当前最长LIS的长度
		int len = 0;
		
		// 从右到左遍历数组（这是为了计算以每个位置开头的LIS长度）
		for (int i = n - 1, find; i >= 0; i--) {
			// 使用二分查找在ends数组中找到<=nums[i]的最左位置
			find = bs(len, nums[i]);
			
			// 如果没有找到<=nums[i]的元素
			if (find == -1) {
				// 将nums[i]添加到ends数组末尾
				ends[len++] = nums[i];
				// 以nums[i]开头的LIS长度为len
				dp[i] = len;
			} else {
				// 如果找到了<=nums[i]的元素
				// 将nums[i]替换到ends数组的find位置
				ends[find] = nums[i];
				// 以nums[i]开头的LIS长度为find+1
				dp[i] = find + 1;
			}
		}
		
		// 返回最长LIS长度
		return len;
	}

	// ends[有效区]从大到小的
	// 二分的方式找<=num的最左位置
	public static int bs(int len, int num) {
		// 初始化搜索区间
		int l = 0, r = len - 1, m, ans = -1;
		
		// 二分查找
		while (l <= r) {
			// 计算中点
			m = (l + r) / 2;
			
			// 如果ends[m] <= num
			if (ends[m] <= num) {
				// 记录可能的答案位置
				ans = m;
				// 在左半部分继续搜索更左的位置
				r = m - 1;
			} else {
				// 在右半部分继续搜索
				l = m + 1;
			}
		}
		
		// 返回最左位置
		return ans;
	}

}