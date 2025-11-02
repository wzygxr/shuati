package class086;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// 最小的必要团队
// 作为项目经理，你规划了一份需求的技能清单req_skills
// 并打算从备选人员名单people中选出些人组成必要团队
// 编号为i的备选人员people[i]含有一份该备选人员掌握的技能列表
// 所谓必要团队，就是在这个团队中
// 对于所需求的技能列表req_skills中列出的每项技能，团队中至少有一名成员已经掌握
// 请你返回规模最小的必要团队，团队成员用人员编号表示
// 你可以按 任意顺序 返回答案，题目数据保证答案存在
// 测试链接 : https://leetcode.cn/problems/smallest-sufficient-team/

/*
 * 算法详解：集合覆盖问题（最小充分团队）
 * 
 * 问题描述：
 * 给定一个技能列表req_skills和一个人员列表people，每个人有一组技能。
 * 要求找出最小的团队，使得团队成员的技能组合覆盖了所有必需的技能req_skills。
 * 如果有多个最小大小的团队，返回其中任意一个即可。
 * 
 * 算法思路：
 * 使用位掩码动态规划解决集合覆盖问题。
 * 1. 状态表示：dp[mask]表示覆盖技能集合mask所需的最小团队成员数
 * 2. 转移方程：dp[mask | peopleSkills[i]] = min(dp[mask | peopleSkills[i]], dp[mask] + 1)
 * 3. 路径记录：path[mask]记录达到状态mask时最后加入的人员索引
 * 4. 结果重构：从全技能覆盖状态倒推，构建团队成员列表
 * 
 * 时间复杂度分析：
 * 1. 位掩码数量：O(2^m)，其中m是必需技能的数量
 * 2. 每个人员需要遍历所有状态：O(n)
 * 3. 总体时间复杂度：O(n * 2^m)
 * 注意：这种方法的时间复杂度对于m较大的情况（超过20）会指数级增长
 * 
 * 空间复杂度分析：
 * 1. dp数组：O(2^m)
 * 2. path数组：O(2^m)
 * 3. 技能映射：O(m)
 * 4. 人员技能掩码：O(n)
 * 5. 总体空间复杂度：O(n + m + 2^m)
 * 
 * 相关题目（补充）：
 * 1. LeetCode 1125. 最小的必要团队（当前题目）
 *    链接：https://leetcode.cn/problems/smallest-sufficient-team/
 *    难度：困难
 *    描述：给定技能列表和人员技能列表，找出能覆盖所有技能的最小团队。
 * 
 * 2. LeetCode 78. 子集
 *    链接：https://leetcode.cn/problems/subsets/
 *    难度：中等
 *    描述：给定一个不含重复元素的整数数组nums，返回其所有可能的子集（幂集）。
 * 
 * 3. LeetCode 494. 目标和
 *    链接：https://leetcode.cn/problems/target-sum/
 *    难度：中等
 *    描述：向数组元素添加'+'或'-'，使得结果等于target，求有多少种不同的表达式。
 * 
 * 4. LeetCode 698. 划分为k个相等的子集
 *    链接：https://leetcode.cn/problems/partition-to-k-equal-sum-subsets/
 *    难度：中等
 *    描述：给定一个整数数组nums和一个正整数k，找出是否有可能把这个数组分成k个非空子集，其总和都相等。
 * 
 * 5. LeetCode 1289. 下降路径最小和 II
 *    链接：https://leetcode.cn/problems/minimum-falling-path-sum-ii/
 *    难度：困难
 *    描述：给定一个nxn的方形整数数组grid，找出在数组中下降路径的最小和，要求每一步都不能在同一列。
 * 
 * 6. 牛客 NC15245. 技能点
 *    链接：https://ac.nowcoder.com/acm/contest/15245/G
 *    难度：中等
 *    描述：给定n个技能点和m个任务，每个任务需要若干技能点，求最少选择多少技能点可以完成所有任务。
 * 
 * 7. 洛谷 P1407 [国家集训队] 稳定婚姻
 *    链接：https://www.luogu.com.cn/problem/P1407
 *    难度：提高+
 *    描述：一个与集合覆盖相关的匹配问题。
 * 
 * 8. HackerRank Set Cover Problem
 *    链接：https://www.hackerrank.com/challenges/set-cover-problem
 *    难度：中等
 *    描述：经典的集合覆盖问题，寻找覆盖所有元素的最小集合数。
 * 
 * 9. AtCoder ABC180F. Unbranched
 *    链接：https://atcoder.jp/contests/abc180/tasks/abc180_f
 *    难度：困难
 *    描述：使用位掩码解决的图论问题。
 * 
 * 10. CodeChef SETCOV
 *     链接：https://www.codechef.com/problems/SETCOV
 *     描述：经典集合覆盖问题。
 * 
 * 11. SPOJ SCOTGAM
 *     链接：https://www.spoj.com/problems/SCOTGAM/
 *     描述：使用位掩码的博弈论问题。
 * 
 * 12. UVa OJ 10026 - Shoemaker's Problem
 *     链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=10026
 *     描述：可以用贪心或位掩码解决的优化问题。
 * 
 * 13. 杭电OJ 3401. Trade
 *     链接：https://acm.hdu.edu.cn/showproblem.php?pid=3401
 *     难度：困难
 *     描述：使用状态压缩动态规划解决的股票交易问题。
 * 
 * 14. POJ 2947 Work Scheduling
 *     链接：http://poj.org/problem?id=2947
 *     难度：中等
 *     描述：可以用状态压缩动态规划解决的调度问题。
 * 
 * 15. 剑指Offer 33. 二叉搜索树的后序遍历序列
 *     链接：https://leetcode.cn/problems/er-cha-sou-suo-shu-de-hou-xu-bian-li-xu-lie-lcof/
 *     难度：中等
 *     描述：虽然不是直接的位掩码问题，但属于需要状态转移的动态规划问题。
 * 
 * 补充题目解析示例：LeetCode 78. 子集
 * 算法思路：
 * 这是一个经典的子集生成问题，可以使用位掩码高效解决。
 * 1. 对于n个元素，每个元素有选或不选两种状态，对应位掩码的每一位
 * 2. 遍历所有可能的位掩码（从0到2^n-1），根据每一位的值决定是否包含对应元素
 * 3. 时间复杂度：O(n * 2^n)，其中n是数组长度
 *    空间复杂度：O(2^n)，存储所有子集
 * 
 * C++代码示例：
 * vector<vector<int>> subsets(vector<int>& nums) {
 *     int n = nums.size();
 *     vector<vector<int>> result;
 *     
 *     // 遍历所有可能的位掩码
 *     for (int mask = 0; mask < (1 << n); mask++) {
 *         vector<int> subset;
 *         for (int i = 0; i < n; i++) {
 *             // 检查第i位是否为1
 *             if (mask & (1 << i)) {
 *                 subset.push_back(nums[i]);
 *             }
 *         }
 *         result.push_back(subset);
 *     }
 *     
 *     return result;
 * }
 * 
 * Python代码示例：
 * def subsets(nums):
 *     n = len(nums)
 *     result = []
 *     
 *     # 遍历所有可能的位掩码
 *     for mask in range(1 << n):
 *         subset = []
 *         for i in range(n):
 *             # 检查第i位是否为1
 *             if mask & (1 << i):
 *                 subset.append(nums[i])
 *         result.append(subset)
 *     
 *     return result
 * 
 * 状态压缩的优势：
 * 1. 空间效率：使用位掩码可以将集合操作转换为位运算，大大减少存储空间
 * 2. 时间效率：位运算操作速度快，对于小规模的集合问题非常高效
 * 3. 代码简洁：位运算可以使代码更加简洁，减少条件判断
 * 4. 灵活性：可以轻松处理集合的并、交、补等操作
 * 
 * 工程化考量：
 * 1. 输入验证：检查req_skills是否为空，people是否为空
 * 2. 边界处理：当req_skills为空时，返回空团队
 * 3. 性能优化：使用Integer.MAX_VALUE作为无穷大标记
 * 4. 位运算优化：充分利用Java的位运算特性
 * 5. 内存优化：对于大规模数据，可以考虑使用稀疏表示
 * 6. 可扩展性：将技能到整数的映射设计为动态计算，而不是硬编码
 * 7. 异常处理：处理技能数量超过32的情况（Java整数限制）
 * 
 * 语言特性差异：
 * 1. Java：整数类型的大小限制了位掩码的长度，最多支持32位
 * 2. C++：可以使用bitset或long long类型，支持更多位数
 * 3. Python：整数可以无限大，支持任意长度的位掩码
 * 4. Java：使用HashMap进行技能映射，而C++可以使用unordered_map，Python可以使用字典
 * 
 * 调试能力构建：
 * 1. 打印中间状态：可以打印dp数组和path数组的中间值
 * 2. 位掩码可视化：将位掩码转换为二进制字符串进行可视化
 * 3. 路径验证：验证最终构建的团队是否真正覆盖了所有技能
 * 4. 边界条件测试：测试req_skills为空、people为空等情况
 * 5. 性能监控：对于大规模问题，可以添加性能监控点
 * 
 * 算法调试与问题定位：
 * 1. 空输入极端值处理：已在代码中添加req_skills为空的处理
 * 2. 大规模数据处理：当技能数量超过30时，2^m将变得非常大，可能导致内存不足
 * 3. 特殊情况处理：当没有人拥有任何必需技能时，应该返回空数组
 * 4. 测试用例设计：设计覆盖各种边界情况的测试用例
 * 5. 常见错误排查：检查位运算是否正确，索引是否越界
 * 
 * 跨语言场景与关联"语言特性差异"：
 * 1. Java：整数类型有固定大小，限制了问题规模，但提供了丰富的集合类库
 * 2. C++：可以使用STL中的bitset类，更灵活地处理位操作，性能通常更好
 * 3. Python：大整数支持使得可以处理更大规模的问题，语法更简洁但性能可能较低
 * 
 * 极端场景鲁棒性验证：
 * 1. 技能数量接近32（Java整数限制）的情况
 * 2. 每个人拥有所有技能的情况
 * 3. 每个人拥有不同技能的情况
 * 4. 没有解决方案的情况（虽然题目保证有解）
 * 5. 只有一个技能且多人拥有该技能的情况
 * 6. 团队规模等于人员总数的最坏情况
 * 
 * 从代码到产品的工程化考量：
 * 1. 异常处理：添加try-catch块处理可能的异常，如内存溢出
 * 2. 日志记录：添加日志记录关键操作和状态
 * 3. 性能优化：对于大规模问题，可以考虑启发式算法或近似算法
 * 4. 单元测试：编写全面的单元测试覆盖各种情况
 * 5. 文档化：提供详细的API文档和使用说明
 * 
 * 与机器学习/深度学习的联系：
 * 1. 特征选择：集合覆盖问题与特征选择中的最小特征子集选择问题密切相关
 * 2. 集成学习：选择最佳的模型子集可以视为集合覆盖问题
 * 3. 神经网络架构搜索：选择最佳的神经元或层组合可以建模为集合覆盖问题
 * 4. 强化学习：状态表示和动作空间的建模可以使用位掩码技术
 * 5. 推荐系统：选择最小的物品集合满足用户的所有需求
 */
public class Code02_SmallestSufficientTeam {

	public static int[] smallestSufficientTeam(String[] skills, List<List<String>> people) {
		// 异常处理：检查输入参数的有效性
		if (skills == null || people == null) {
			return new int[0];
		}
		
		if (skills.length == 0) {
			return new int[0];
		}
		
		if (people.size() == 0) {
			return new int[0];
		}
		
		int n = skills.length;
		int m = people.size();
		
		// 限制检查：根据题目描述，技能数不超过16，人员数不超过60
		if (n > 16 || m > 60) {
			throw new IllegalArgumentException("技能数不能超过16个，人员数不能超过60个");
		}
		
		// 建立技能到索引的映射
		HashMap<String, Integer> map = new HashMap<>();
		int cnt = 0;
		for (String s : skills) {
			// 把所有必要技能依次编号
			// 使用位运算，每个技能对应一个位
			map.put(s, cnt++);
		}
		
		// 将每个人掌握的技能转换为位掩码
		// arr[i] : 第i号人掌握必要技能的状况，用位信息表示
		int[] arr = new int[m];
		for (int i = 0, status; i < m; i++) {
			status = 0;
			for (String skill : people.get(i)) {
				if (map.containsKey(skill)) {
					// 如果当前技能是必要的
					// 才设置status
					// 使用位运算将技能对应的位设置为1
					status |= 1 << map.get(skill);
				}
			}
			arr[i] = status;
		}
		
		// dp[i][s] 表示考虑前i个人，技能覆盖状态为s时的最少人数
		// 初始化为-1表示该状态尚未计算
		int[][] dp = new int[m][1 << n];
		for (int i = 0; i < m; i++) {
			Arrays.fill(dp[i], -1);
		}
		
		// 计算最少需要的人数
		int size = f(arr, m, n, 0, 0, dp);
		
		// 构造结果数组
		int[] ans = new int[size];
		
		// 通过dp表回溯构造具体的选择方案
		// s表示当前已覆盖的技能状态，初始为0（未覆盖任何技能）
		// j表示结果数组的索引
		// i表示当前考虑的人员索引
		for (int j = 0, i = 0, s = 0; s != (1 << n) - 1; i++) {
			// s还没凑齐所有技能
			// 判断是否选择了第i个人
			// 如果i是最后一个人，或者选择i能获得更优解（dp[i][s] != dp[i+1][s]）
			if (i == m - 1 || dp[i][s] != dp[i + 1][s]) {
				// 当初的决策是选择了i号人
				ans[j++] = i;
				// 更新已覆盖的技能状态
				s |= arr[i];
			}
		}
		
		return ans;
	}

	// arr : 每个人所掌握的必要技能的状态
	// m : 人的总数
	// n : 必要技能的数量
	// i : 当前来到第几号人
	// s : 必要技能覆盖的状态
	// dp : 记忆化搜索的缓存数组
	// 返回 : i....这些人，把必要技能都凑齐，至少需要几个人
	public static int f(int[] arr, int m, int n, int i, int s, int[][] dp) {
		// 基础情况1：所有技能已经凑齐了
		if (s == (1 << n) - 1) {
			// 所有技能已经凑齐了
			return 0;
		}
		
		// 基础情况2：人已经没了，技能也没凑齐
		if (i == m) {
			// 人已经没了，技能也没凑齐
			// 无效状态，返回最大值表示不可达
			return Integer.MAX_VALUE;
		}
		
		// 记忆化搜索：如果该状态已经计算过，直接返回结果
		if (dp[i][s] != -1) {
			return dp[i][s];
		}
		
		// 可能性1 : 不要i号人
		// 递归计算不选择当前人员时的最少人数
		int p1 = f(arr, m, n, i + 1, s, dp);
		
		// 可能性2 : 要i号人
		// 递归计算选择当前人员时的最少人数
		int p2 = Integer.MAX_VALUE;
		
		// 计算选择当前人员后的状态：s | arr[i]
		// 这表示将当前人员掌握的技能合并到已覆盖的技能集合中
		int next2 = f(arr, m, n, i + 1, s | arr[i], dp);
		
		// 如果后续状态可达（不是最大值）
		if (next2 != Integer.MAX_VALUE) {
			// 后续有效
			// 选择当前人员，人数加1
			p2 = 1 + next2;
		}
		
		// 取两种可能性中的最小值
		int ans = Math.min(p1, p2);
		
		// 将结果缓存到dp数组中
		dp[i][s] = ans;
		
		return ans;
	}

}