package class081;

import java.util.Arrays;

// 主题目: 最优账单平衡
// 题目来源: LeetCode 465. Optimal Account Balancing
// 题目链接: https://leetcode.cn/problems/optimal-account-balancing/
// 题目描述:
// 给你一个表示交易的数组 transactions
// 其中 transactions[i] = [fromi, toi, amounti]
// 表示 ID = fromi 的人给 ID = toi 的人共计 amounti
// 请你计算并返回还清所有债务的最小交易笔数

// 状态压缩动态规划专题 - 适用于小规模集合状态表示的问题
// 核心思想：使用位掩码（bitmask）表示集合状态，通过位运算高效处理子集枚举和状态转移
// 适用场景：
// 1. 小规模的元素集合（通常n≤20）
// 2. 需要枚举所有可能的子集或状态组合
// 3. 状态转移依赖于子集的信息

// 补充题目1: 并行课程 II (Parallel Courses II)
// 题目来源: LeetCode 1494. Parallel Courses II
// 题目链接: https://leetcode.cn/problems/parallel-courses-ii/
// 题目描述:
// 给定 n 门课程，编号从 1 到 n，以及若干先修课程关系。
// 每学期可以选择任意数量的课程，但前提是这些课程的先修课程都已经完成。
// 返回上完所有课程所需的最少学期数。
// 解题思路:
// 1. 状态压缩动态规划 + 拓扑排序解法
// 2. pre[i] 表示课程i的先修课程集合（用位掩码表示）
// 3. dp[mask] 表示完成课程集合mask所需的最少学期数
// 时间复杂度: O(3^n + n * 2^n)
// 空间复杂度: O(2^n)

// 补充题目2: 参加考试的最大学生数 (Maximum Students Taking Exam)
// 题目来源: LeetCode 1349. Maximum Students Taking Exam
// 题目链接: https://leetcode.cn/problems/maximum-students-taking-exam/
// 题目描述:
// 给你一个 m * n 的矩阵 seats 表示教室中的座位分布。
// seats[i][j] = '# 表示该座位被占用，否则可坐。
// 返回最多可以坐下的学生数，要求学生不能相邻（横向、纵向、对角）。
// 解题思路：
// 1. 按行状态压缩DP，每行的状态用位掩码表示
// 2. 预处理每行合法的座位状态（没有被占用且行内不相邻）
// 3. dp[i][state] 表示处理到第i行，状态为state时的最大学生数
// 时间复杂度: O(m * 2^n * 2^n)
// 空间复杂度: O(m * 2^n)

// 补充题目3: 匹配子序列的单词数 (Number of Matching Subsequences)
// 题目来源: LeetCode 792. Number of Matching Subsequences
// 题目链接: https://leetcode.cn/problems/number-of-matching-subsequences/
// 题目描述:
// 给定字符串 s 和单词数组 words，返回 words 中是 s 的子序列的单词数目。
// 解题思路：
// 1. 预处理每个字符在s中出现的所有位置
// 2. 对每个单词，二分查找判断是否为子序列
// 时间复杂度: O(s.length + words.length * word.length * log(s.length))
// 空间复杂度: O(26 * s.length)

// 补充题目4: 划分为k个相等的子集 (Partition to K Equal Sum Subsets)
// 题目来源: LeetCode 698. Partition to K Equal Sum Subsets
// 题目链接: https://leetcode.cn/problems/partition-to-k-equal-sum-subsets/
// 题目描述:
// 给定一个整数数组 nums 和一个整数 k，判断是否可以将数组分割成k个相等的非空子集。
// 解题思路：
// 1. 状态压缩动态规划，mask表示已选择的元素集合
// 2. dp[mask] 表示当前已选择元素的和模 target 的值
// 3. 剪枝优化：排序、跳过重复元素等
// 时间复杂度: O(n * 2^n)
// 空间复杂度: O(2^n)

// 补充题目5: 单词搜索 II (Word Search II)
// 题目来源: LeetCode 212. Word Search II
// 题目链接: https://leetcode.cn/problems/word-search-ii/
// 题目描述:
// 给定一个 m x n 二维字符网格 board 和一个单词（字符串）列表 words，
// 返回所有二维网格上的单词。单词必须按照字母顺序，通过相邻的单元格内的字母构成，
// 其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。
// 解题思路：
// 1. 构建Trie树存储所有单词
// 2. DFS搜索每个单元格，并结合Trie树剪枝
// 时间复杂度: O(m * n * 4^L)，其中L是单词的最大长度
// 空间复杂度: O(Total letters in words)

// 补充题目6: 并行课程 II (Parallel Courses II)
// 题目来源: LeetCode 1494. Parallel Courses II
// 题目链接: https://leetcode.cn/problems/parallel-courses-ii/
// 题目描述:
// 给定 n 门课程，编号从 1 到 n，以及若干先修课程关系。
// 每学期可以选择任意数量的课程，但前提是这些课程的先修课程都已经完成。
// 返回上完所有课程所需的最少学期数。
// 解题思路:
// 1. 状态压缩动态规划 + 拓扑排序解法
// 2. pre[i] 表示课程i的先修课程集合（用位掩码表示）
// 3. dp[mask] 表示完成课程集合mask所需的最少学期数
// 时间复杂度: O(3^n + n * 2^n)
// 空间复杂度: O(2^n)

/*
 * C++版本实现:
 * 
 * #include <iostream>
 * #include <vector>
 * #include <string>
 * #include <algorithm>
 * #include <climits>
 * #include <unordered_map>
 * using namespace std;
 * 
 * // 主题目：最优账单平衡
 * // 时间复杂度: O(2^n)
 * // 空间复杂度: O(2^n)
 * class Solution {
 * public:
 *     int minTransfers(vector<vector<int>>& transactions) {
 *         vector<int> debt = processDebt(transactions);
 *         int n = debt.size();
 *         vector<int> dp(1 << n, -1);
 *         return n - f(debt, (1 << n) - 1, 0, n, dp);
 *     }
 * 
 * private:
 *     vector<int> processDebt(vector<vector<int>>& transactions) {
 *         vector<int> help(13, 0); // 人员编号最大为12
 *         for (const auto& tran : transactions) {
 *             help[tran[0]] -= tran[2];
 *             help[tran[1]] += tran[2];
 *         }
 *         vector<int> debt;
 *         for (int num : help) {
 *             if (num != 0) {
 *                 debt.push_back(num);
 *             }
 *         }
 *         return debt;
 *     }
 * 
 *     int f(vector<int>& debt, int set, int sum, int n, vector<int>& dp) {
 *         if (dp[set] != -1) {
 *             return dp[set];
 *         }
 *         int ans = 0;
 *         if ((set & (set - 1)) != 0) { // 集合中不只一个元素
 *             if (sum == 0) {
 *                 for (int i = 0; i < n; i++) {
 *                     if ((set & (1 << i)) != 0) {
 *                         ans = f(debt, set ^ (1 << i), sum - debt[i], n, dp) + 1;
 *                         break;
 *                     }
 *                 }
 *             } else {
 *                 for (int i = 0; i < n; i++) {
 *                     if ((set & (1 << i)) != 0) {
 *                         ans = max(ans, f(debt, set ^ (1 << i), sum - debt[i], n, dp));
 *                     }
 *                 }
 *             }
 *         }
 *         dp[set] = ans;
 *         return ans;
 *     }
 * };
 * 
 * // 补充题目1: 并行课程 II
 * // 时间复杂度: O(3^n + n * 2^n)
 * // 空间复杂度: O(2^n)
 * int minimumTime(int n, int k, vector<vector<int>>& relations) {
 *     vector<int> pre(n, 0);
 *     for (const auto& relation : relations) {
 *         pre[relation[1] - 1] |= 1 << (relation[0] - 1);
 *     }
 * 
 *     vector<int> dp(1 << n, n);
 *     dp[0] = 0;
 * 
 *     for (int mask = 0; mask < (1 << n); mask++) {
 *         if (dp[mask] == n) {
 *             continue;
 *         }
 * 
 *         int available = 0;
 *         for (int i = 0; i < n; i++) {
 *             if ((mask & (1 << i)) == 0 && (mask & pre[i]) == pre[i]) {
 *                 available |= 1 << i;
 *             }
 *         }
 * 
 *         for (int subset = available; subset > 0; subset = (subset - 1) & available) {
 *             if (__builtin_popcount(subset) <= k) {
 *                 dp[mask | subset] = min(dp[mask | subset], dp[mask] + 1);
 *             }
 *         }
 *     }
 * 
 *     return dp[(1 << n) - 1];
 * }
 * 
 * // 补充题目2: 参加考试的最大学生数
 * // 时间复杂度: O(m * 2^n * 2^n)
 * // 空间复杂度: O(m * 2^n)
 * int maxStudents(vector<vector<char>>& seats) {
 *     int m = seats.size();
 *     int n = seats[0].size();
 *     vector<int> validRows(m, 0);
 *     
 *     // 预处理每一行的有效座位
 *     for (int i = 0; i < m; i++) {
 *         for (int j = 0; j < n; j++) {
 *             if (seats[i][j] == '.') {
 *                 validRows[i] |= 1 << j;
 *             }
 *         }
 *     }
 *     
 *     vector<int> validStates;
 *     for (int state = 0; state < (1 << n); state++) {
 *         if ((state & (state << 1)) == 0) {
 *             validStates.push_back(state);
 *         }
 *     }
 *     
 *     vector<vector<int>> dp(m, vector<int>(validStates.size(), 0));
 *     
 *     // 初始化第一行
 *     for (int j = 0; j < validStates.size(); j++) {
 *         int state = validStates[j];
 *         if ((state & validRows[0]) == state) {
 *             dp[0][j] = __builtin_popcount(state);
 *         }
 *     }
 *     
 *     // 动态规划转移
 *     for (int i = 1; i < m; i++) {
 *         for (int j = 0; j < validStates.size(); j++) {
 *             int currState = validStates[j];
 *             if ((currState & validRows[i]) != currState) {
 *                 continue;
 *             }
 *             
 *             for (int k = 0; k < validStates.size(); k++) {
 *                 int prevState = validStates[k];
 *                 if ((prevState & (currState << 1)) == 0 && (prevState & (currState >> 1)) == 0) {
 *                     dp[i][j] = max(dp[i][j], dp[i-1][k] + __builtin_popcount(currState));
 *                 }
 *             }
 *         }
 *     }
 *     
 *     int maxVal = 0;
 *     for (int cnt : dp.back()) {
 *         maxVal = max(maxVal, cnt);
 *     }
 *     return maxVal;
 * }
 * 
 * // 补充题目4: 划分为k个相等的子集
 * // 时间复杂度: O(n * 2^n)
 * // 空间复杂度: O(2^n)
 * bool canPartitionKSubsets(vector<int>& nums, int k) {
 *     int sum = 0;
 *     for (int num : nums) {
 *         sum += num;
 *     }
 *     
 *     if (sum % k != 0) {
 *         return false;
 *     }
 *     
 *     int target = sum / k;
 *     int n = nums.size();
 *     
 *     sort(nums.rbegin(), nums.rend());
 *     if (nums[0] > target) {
 *         return false;
 *     }
 *     
 *     vector<int> dp(1 << n, -1);
 *     dp[0] = 0;
 *     
 *     for (int mask = 0; mask < (1 << n); mask++) {
 *         if (dp[mask] == -1) {
 *             continue;
 *         }
 *         
 *         for (int i = 0; i < n; i++) {
 *             if (!(mask & (1 << i)) && dp[mask] + nums[i] <= target) {
 *                 dp[mask | (1 << i)] = (dp[mask] + nums[i]) % target;
 *             }
 *         }
 *     }
 *     
 *     return dp[(1 << n) - 1] == 0;
 * }
 * 
 * int main() {
 *     // 测试最优账单平衡
 *     vector<vector<int>> transactions = {{0, 1, 10}, {2, 0, 5}};
 *     Solution sol;
 *     cout << "最小交易笔数: " << sol.minTransfers(transactions) << endl;
 *     
 *     // 测试并行课程II
 *     vector<vector<int>> relations = {{1, 2}, {2, 3}, {3, 1}};
 *     cout << "最少学期数: " << minimumTime(3, 2, relations) << endl;
 *     
 *     // 测试划分为k个相等的子集
 *     vector<int> nums = {4, 3, 2, 3, 5, 2, 1};
 *     cout << "能否划分为4个子集: " << (canPartitionKSubsets(nums, 4) ? "true" : "false") << endl;
 *     
 *     return 0;
 * }
 */

/*
 * Python版本实现:
 * 
 * # 主题目：最优账单平衡
 * # 时间复杂度: O(2^n)
 * # 空间复杂度: O(2^n)
 * class Solution:
 *     def minTransfers(self, transactions: List[List[int]]) -> int:
 *         # 处理债务
 *         help = [0] * 13  # 人员编号最大为12
 *         for tran in transactions:
 *             help[tran[0]] -= tran[2]
 *             help[tran[1]] += tran[2]
 *         
 *         # 过滤掉债务为0的人
 *         debt = []
 *         for num in help:
 *             if num != 0:
 *                 debt.append(num)
 *         
 *         n = len(debt)
 *         dp = [-1] * (1 << n)
 *         
 *         def f(mask, sum_val):
 *             if dp[mask] != -1:
 *                 return dp[mask]
 *             
 *             ans = 0
 *             if mask & (mask - 1) != 0:  # 集合中不只一个元素
 *                 if sum_val == 0:
 *                     for i in range(n):
 *                         if mask & (1 << i):
 *                             ans = f(mask ^ (1 << i), sum_val - debt[i]) + 1
 *                             break
 *                 else:
 *                     for i in range(n):
 *                         if mask & (1 << i):
 *                             ans = max(ans, f(mask ^ (1 << i), sum_val - debt[i]))
 *             
 *             dp[mask] = ans
 *             return ans
 *         
 *         return n - f((1 << n) - 1, 0)
 * 
 * # 补充题目1: 并行课程 II
 * # 时间复杂度: O(3^n + n * 2^n)
 * # 空间复杂度: O(2^n)
 * def minimumTime(n: int, k: int, relations: List[List[int]]) -> int:
 *     pre = [0] * n
 *     for relation in relations:
 *         pre[relation[1] - 1] |= 1 << (relation[0] - 1)
 * 
 *     dp = [n] * (1 << n)
 *     dp[0] = 0
 * 
 *     for mask in range(1 << n):
 *         if dp[mask] == n:
 *             continue
 * 
 *         available = 0
 *         for i in range(n):
 *             if (mask & (1 << i)) == 0 and (mask & pre[i]) == pre[i]:
 *                 available |= 1 << i
 * 
 *         subset = available
 *         while subset > 0:
 *             if bin(subset).count('1') <= k:
 *                 dp[mask | subset] = min(dp[mask | subset], dp[mask] + 1)
 *             subset = (subset - 1) & available
 * 
 *     return dp[(1 << n) - 1]
 * 
 * # 补充题目2: 参加考试的最大学生数
 * # 时间复杂度: O(m * 2^n * 2^n)
 * # 空间复杂度: O(m * 2^n)
 * def maxStudents(seats: List[List[str]]) -> int:
 *     m = len(seats)
 *     n = len(seats[0])
 *     valid_rows = [0] * m
 *     
 *     # 预处理每一行的有效座位
 *     for i in range(m):
 *         for j in range(n):
 *             if seats[i][j] == '.':
 *                 valid_rows[i] |= 1 << j
 *     
 *     # 预处理所有可能的合法行状态
 *     valid_states = []
 *     for state in range(1 << n):
 *         if (state & (state << 1)) == 0:
 *             valid_states.append(state)
 *     
 *     # 初始化dp数组
 *     dp = [[0] * len(valid_states) for _ in range(m)]
 *     
 *     # 初始化第一行
 *     for j in range(len(valid_states)):
 *         state = valid_states[j]
 *         if (state & valid_rows[0]) == state:
 *             dp[0][j] = bin(state).count('1')
 *     
 *     # 动态规划转移
 *     for i in range(1, m):
 *         for j in range(len(valid_states)):
 *             curr_state = valid_states[j]
 *             if (curr_state & valid_rows[i]) != curr_state:
 *                 continue
 *             
 *             for k in range(len(valid_states)):
 *                 prev_state = valid_states[k]
 *                 if (prev_state & (curr_state << 1)) == 0 and (prev_state & (curr_state >> 1)) == 0:
 *                     dp[i][j] = max(dp[i][j], dp[i-1][k] + bin(curr_state).count('1'))
 *     
 *     return max(dp[-1])
 * 
 * # 补充题目3: 匹配子序列的单词数
 * # 时间复杂度: O(s.length + words.length * word.length * log(s.length))
 * # 空间复杂度: O(26 * s.length)
 * def numMatchingSubseq(s: str, words: List[str]) -> int:
 *     from collections import defaultdict
 *     import bisect
 *     
 *     # 预处理每个字符在s中出现的所有位置
 *     char_pos = defaultdict(list)
 *     for idx, c in enumerate(s):
 *         char_pos[c].append(idx)
 *     
 *     def is_subsequence(word):
 *         current_pos = -1
 *         for c in word:
 *             if c not in char_pos:
 *                 return False
 *             
 *             # 二分查找大于current_pos的最小位置
 *             idx = bisect.bisect_right(char_pos[c], current_pos)
 *             if idx == len(char_pos[c]):
 *                 return False
 *             current_pos = char_pos[c][idx]
 *         return True
 *     
 *     count = 0
 *     for word in words:
 *         if is_subsequence(word):
 *             count += 1
 *     return count
 * 
 * # 补充题目4: 划分为k个相等的子集
 * # 时间复杂度: O(n * 2^n)
 * # 空间复杂度: O(2^n)
 * def canPartitionKSubsets(nums: List[int], k: int) -> bool:
 *     total = sum(nums)
 *     if total % k != 0:
 *         return False
 *     
 *     target = total // k
 *     nums.sort(reverse=True)  # 降序排序，优化剪枝
 *     
 *     if nums[0] > target:
 *         return False
 *     
 *     n = len(nums)
 *     dp = [-1] * (1 << n)
 *     dp[0] = 0
 *     
 *     for mask in range(1 << n):
 *         if dp[mask] == -1:
 *             continue
 *         
 *         for i in range(n):
 *             if not (mask & (1 << i)) and dp[mask] + nums[i] <= target:
 *                 dp[mask | (1 << i)] = (dp[mask] + nums[i]) % target
 *     
 *     return dp[(1 << n) - 1] == 0
 * 
 * # 测试代码
 * if __name__ == "__main__":
 *     # 测试最优账单平衡
 *     transactions = [[0, 1, 10], [2, 0, 5]]
 *     solution = Solution()
 *     print("最小交易笔数:", solution.minTransfers(transactions))  # 预期输出: 2
 *     
 *     # 测试并行课程II
 *     relations = [[1, 2], [2, 3], [3, 1]]
 *     print("最少学期数:", minimumTime(3, 2, relations))  # 预期输出: 3
 *     
 *     # 测试划分为k个相等的子集
 *     nums = [4, 3, 2, 3, 5, 2, 1]
 *     print("能否划分为4个子集:", canPartitionKSubsets(nums, 4))  # 预期输出: True
 */

public class Code02_OptimalAccountBalancing {

	// 题目说了人员编号的最大范围：0 ~ 12
	public static int MAXN = 13;

	public static int minTransfers(int[][] transactions) {
		// 加工出来的debt数组中一定不含有0
		int[] debt = debts(transactions);
		int n = debt.length;
		int[] dp = new int[1 << n];
		Arrays.fill(dp, -1);
		return n - f(debt, (1 << n) - 1, 0, n, dp);
	}

	public static int[] debts(int[][] transactions) {
		int[] help = new int[MAXN];
		for (int[] tran : transactions) {
			help[tran[0]] -= tran[2];
			help[tran[1]] += tran[2];
		}
		int n = 0;
		for (int num : help) {
			if (num != 0) {
				n++;
			}
		}
		int[] debt = new int[n];
		int index = 0;
		for (int num : help) {
			if (num != 0) {
				debt[index++] = num;
			}
		}
		return debt;
	}

	public static int f(int[] debt, int set, int sum, int n, int[] dp) {
		if (dp[set] != -1) {
			return dp[set];
		}
		int ans = 0;
		if ((set & (set - 1)) != 0) { // 集合中不只一个元素
			if (sum == 0) {
				for (int i = 0; i < n; i++) {
					if ((set & (1 << i)) != 0) {
						// 找到任何一个元素，去除这个元素
						// 剩下的集合进行尝试，返回值 + 1
						ans = f(debt, set ^ (1 << i), sum - debt[i], n, dp) + 1;
						// 然后不需要再尝试下一个元素了，因为答案一定是一样的
						// 所以直接break
						break;
					}
				}
			} else {
				for (int i = 0; i < n; i++) {
					if ((set & (1 << i)) != 0) {
						ans = Math.max(ans, f(debt, set ^ (1 << i), sum - debt[i], n, dp));
					}
				}
			}
		}
		dp[set] = ans;
		return ans;
	}

	// 补充题目1: 并行课程 II - Java实现
	public static int minimumTimeRequired(int n, int k, int[][] relations) {
		int[] pre = new int[n];
		for (int[] relation : relations) {
			pre[relation[1] - 1] |= 1 << (relation[0] - 1);
		}

		int[] dp = new int[1 << n];
		for (int i = 0; i < dp.length; i++) {
			dp[i] = n; // 初始化为最大可能值
		}
		dp[0] = 0;

		for (int mask = 0; mask < (1 << n); mask++) {
			if (dp[mask] == n) {
				continue;
			}

			int available = 0;
			for (int i = 0; i < n; i++) {
				if ((mask & (1 << i)) == 0 && (mask & pre[i]) == pre[i]) {
					available |= 1 << i;
				}
			}

			// 枚举available的所有非空子集
			for (int subset = available; subset > 0; subset = (subset - 1) & available) {
				if (Integer.bitCount(subset) <= k) {
					dp[mask | subset] = Math.min(dp[mask | subset], dp[mask] + 1);
				}
			}
		}

		return dp[(1 << n) - 1];
	}

	// 补充题目2: 参加考试的最大学生数 - Java实现
	public static int maxStudents(char[][] seats) {
		int m = seats.length;
		int n = seats[0].length;
		int[] validRows = new int[m];
		
		// 预处理每一行的有效座位（可以坐的位置用1表示）
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (seats[i][j] == '.') {
					validRows[i] |= 1 << j;
				}
			}
		}
		
		// 预处理所有可能的合法行状态（不能有相邻的1）
		java.util.List<Integer> validStates = new java.util.ArrayList<>();
		for (int state = 0; state < (1 << n); state++) {
			if ((state & (state << 1)) == 0) {
				validStates.add(state);
			}
		}
		
		// dp[i][j]表示处理到第i行，状态为validStates.get(j)时的最大学生数
		int[][] dp = new int[m][validStates.size()];
		
		// 初始化第一行
		for (int j = 0; j < validStates.size(); j++) {
			int state = validStates.get(j);
			if ((state & validRows[0]) == state) {
				dp[0][j] = Integer.bitCount(state);
			}
		}
		
		// 动态规划转移
		for (int i = 1; i < m; i++) {
			for (int j = 0; j < validStates.size(); j++) {
				int currState = validStates.get(j);
				// 当前状态必须与当前行的有效座位兼容
				if ((currState & validRows[i]) != currState) {
					continue;
				}
				
				for (int k = 0; k < validStates.size(); k++) {
					int prevState = validStates.get(k);
					// 前一行状态必须与当前行状态兼容（没有对角相邻）
					if ((prevState & (currState << 1)) == 0 && (prevState & (currState >> 1)) == 0) {
						dp[i][j] = Math.max(dp[i][j], dp[i-1][k] + Integer.bitCount(currState));
					}
				}
			}
		}
		
		// 找出最后一行的最大值
		int max = 0;
		for (int count : dp[m-1]) {
			max = Math.max(max, count);
		}
		return max;
	}

	// 补充题目4: 划分为k个相等的子集 - Java实现
	public static boolean canPartitionKSubsets(int[] nums, int k) {
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		
		// 总和不能被k整除，直接返回false
		if (sum % k != 0) {
			return false;
		}
		
		int target = sum / k;
		int n = nums.length;
		
		// 优化：降序排序，先尝试较大的数
		Arrays.sort(nums);
		for (int i = 0, j = n - 1; i < j; i++, j--) {
			int temp = nums[i];
			nums[i] = nums[j];
			nums[j] = temp;
		}
		
		// 如果最大的数超过target，不可能分割
		if (nums[0] > target) {
			return false;
		}
		
		// 状态压缩DP
		// dp[mask]表示当前已选元素集合的和模target的值
		int[] dp = new int[1 << n];
		Arrays.fill(dp, -1);
		dp[0] = 0;
		
		for (int mask = 0; mask < (1 << n); mask++) {
			if (dp[mask] == -1) {
				continue;
			}
			
			for (int i = 0; i < n; i++) {
				// 如果当前元素未选
				if ((mask & (1 << i)) == 0) {
					// 如果加上当前元素不会超过target
					if (dp[mask] + nums[i] <= target) {
						dp[mask | (1 << i)] = (dp[mask] + nums[i]) % target;
					}
				}
			}
		}
		
		return dp[(1 << n) - 1] == 0;
	}

	// 主方法用于测试
	public static void main(String[] args) {
		// 测试最优账单平衡
		int[][] transactions = {{0, 1, 10}, {2, 0, 5}};
		System.out.println("最小交易笔数: " + minTransfers(transactions));
		
		// 测试并行课程II
		int[][] relations = {{1, 2}, {2, 3}, {3, 1}};
		System.out.println("最少学期数: " + minimumTimeRequired(3, 2, relations));
		
		// 测试划分为k个相等的子集
		int[] nums = {4, 3, 2, 3, 5, 2, 1};
		System.out.println("能否划分为4个子集: " + canPartitionKSubsets(nums, 4));
	}
}