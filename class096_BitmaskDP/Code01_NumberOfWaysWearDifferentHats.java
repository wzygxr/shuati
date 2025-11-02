package class081;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 状态压缩动态规划专题：每个人戴不同帽子的方案数
 * 
 * 状态压缩动态规划是一种将状态用二进制位表示的动态规划方法，适用于状态数较大但仍然在可处理范围内的问题。
 * 通常当问题的状态数不超过2^20（约100万）时，状态压缩动态规划是可行的。
 * 
 * 本文件包含：
 * 1. 主题目：每个人戴不同帽子的方案数
 * 2. 补充题目：
 *    - 我能赢吗
 *    - 灯泡开关 IV
 *    - 翻转游戏 II
 *    - 最小成本爬楼梯 II
 *    - 数组的最大子集和
 *    - 目标和
 */

// 主题目: 每个人戴不同帽子的方案数
// 题目来源: LeetCode 1434. Number of Ways to Wear Different Hats to Each Other
// 题目链接: https://leetcode.cn/problems/number-of-ways-to-wear-different-hats-to-each-other/
// 题目描述:
// 总共有 n 个人和 40 种不同的帽子，帽子编号从 1 到 40 
// 给你一个整数列表的列表 hats ，其中 hats[i] 是第 i 个人所有喜欢帽子的列表
// 请你给每个人安排一顶他喜欢的帽子，确保每个人戴的帽子跟别人都不一样，并返回方案数
// 由于答案可能很大，答案对 1000000007 取模

// 补充题目1: 我能赢吗 (Can I Win)
// 题目来源: LeetCode 464. Can I Win
// 题目链接: https://leetcode.cn/problems/can-i-win/
// 题目描述:
// 在"100 game"这个游戏中，两名玩家轮流选择从1到maxChoosableInteger的任意整数，累计整数和，
// 先使得累计整数和达到或超过desiredTotal的玩家，即为胜者。
// 如果我们将游戏规则改为"玩家不能重复使用整数"呢？
// 例如，两个玩家可以轮流从公共整数池中抽取从1到15的整数（不放回），直到累计整数和>=100。
// 给定两个整数maxChoosableInteger和desiredTotal，若先出手的玩家能稳赢则返回true，否则返回false。
// 假设两位玩家游戏时都绝顶聪明，可以全盘为自己打算。
// 解题思路:
// 1. 使用状态压缩DP和记忆化搜索解决博弈问题
// 2. 用二进制位表示数字的使用状态，第i位为1表示数字i已被使用
// 3. 对于每个状态，尝试选择未使用的数字，如果存在一种选择能让对手必败，则当前玩家必胜
// 时间复杂度: O(2^n * n)，其中n是maxChoosableInteger
// 空间复杂度: O(2^n)

// 补充题目2: 灯泡开关 IV (Bulb Switcher IV)
// 题目来源: LeetCode 1529. Bulb Switcher IV
// 题目链接: https://leetcode.cn/problems/bulb-switcher-iv/
// 题目描述:
// 房间中有n个灯泡，编号从0到n-1，自左向右排成一排。最初，所有的灯泡都是关着的。
// 请你设法使得灯泡的状态和target描述的状态一致，其中target[i]等于1表示第i个灯泡是开着的，等于0表示第i个灯泡是关着的。
// 有一个开关可以按动，每按一次，将会改变从当前位置到所有后面灯泡的状态。
// 请返回达成目标所需的最少操作次数。
// 解题思路:
// 1. 贪心算法 + 状态跟踪
// 2. 从左到右遍历target字符串，记录当前的翻转状态
// 3. 每当遇到与当前状态不符的字符时，增加操作次数并翻转当前状态
// 时间复杂度: O(n)
// 空间复杂度: O(1)

// 补充题目3: 翻转游戏 II (Flip Game II)
// 题目来源: LeetCode 294. Flip Game II
// 题目链接: https://leetcode.cn/problems/flip-game-ii/
// 题目描述:
// 给定一个字符串s，其中只包含+和-，你和你的朋友轮流翻转连续的两个"++"变成"--"。
// 当一个人无法执行翻转操作时，他将输掉游戏。
// 请你编写一个函数，判断你是否能在游戏中获胜。
// 解题思路:
// 1. 状态压缩DP + 记忆化搜索
// 2. 将字符串转换为整数表示状态
// 3. 对于每个状态，尝试所有可能的翻转操作，如果存在一种操作能让对手必败，则当前玩家必胜
// 时间复杂度: O(n * 2^n)，其中n是字符串长度
// 空间复杂度: O(2^n)

// 补充题目4: 最小成本爬楼梯 II (Minimum Cost to Paint Fence II)
// 题目来源: LeetCode 265. Paint House II (变种)
// 题目链接: https://leetcode.cn/problems/paint-house-ii/
// 题目描述:
// 给定一个整数数组cost，其中cost[i][j]表示给第i个房子刷第j种颜色的花费。
// 要求相邻房子的颜色不能相同，且所有房子必须被刷漆。
// 请返回给所有房子刷漆的最小花费。
// 解题思路:
// 1. 动态规划
// 2. dp[i][j]表示给前i个房子刷漆，且第i个房子刷第j种颜色的最小花费
// 3. 优化：记录前一行的最小值和次小值，避免重复计算
// 时间复杂度: O(n * k)，其中n是房子数量，k是颜色数量
// 空间复杂度: O(k)（优化后）

// 补充题目5: 数组的最大子集和 (Maximum Subset Sum With No Adjacent Elements)
// 题目来源: LeetCode 198. House Robber
// 题目链接: https://leetcode.cn/problems/house-robber/
// 题目描述:
// 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
// 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
// 给定一个代表每个房屋存放金额的非负整数数组，计算你不触动警报装置的情况下，一夜之内能够偷窃到的最高金额。
// 解题思路:
// 1. 动态规划
// 2. dp[i]表示考虑前i个房子能获得的最大金额
// 3. 状态转移：dp[i] = max(dp[i-1], dp[i-2] + nums[i])
// 时间复杂度: O(n)
// 空间复杂度: O(1)（优化后）

// 补充题目6: 目标和 (Target Sum)
// 题目来源: LeetCode 494. Target Sum
// 题目链接: https://leetcode.cn/problems/target-sum/
// 题目描述:
// 给你一个整数数组nums和一个整数target。
// 向数组中的每个整数前添加'+'或'-'，然后串联起所有整数，可以构造一个表达式：
// 例如，nums = [2, 1]，可以在2之前添加'+'，在1之前添加'-'，得到表达式"+2-1"。
// 返回可以通过上述方法构造的、运算结果等于target的不同表达式的数目。
// 解题思路:
// 1. 状态压缩DP
// 2. 由于数组长度不超过20，可以用二进制位表示选择的数（0表示减，1表示加）
// 3. 遍历所有可能的子集，计算满足条件的组合数
// 时间复杂度: O(2^n)
// 空间复杂度: O(n)

/*
 * C++版本实现:
 * 
 * class Solution {
 * public:
 *     bool canIWin(int maxChoosableInteger, int desiredTotal) {
 *         // 边界情况：如果目标值为0，先手玩家直接获胜
 *         if (desiredTotal <= 0) return true;
 *         
 *         // 如果所有数字之和都小于目标值，则无法获胜
 *         int sum = maxChoosableInteger * (maxChoosableInteger + 1) / 2;
 *         if (sum < desiredTotal) return false;
 *         
 *         // 使用unordered_map进行记忆化搜索
 *         std::unordered_map<int, bool> memo;
 *         return dfs(maxChoosableInteger, desiredTotal, 0, memo);
 *     }

    /*
     * C++版本实现:
     * 
     * #include <iostream>
     * #include <vector>
     * #include <string>
     * #include <algorithm>
     * #include <unordered_map>
     * #include <climits>
     * using namespace std;
     * 
     * // 主题目：每个人戴不同帽子的方案数
     * // 时间复杂度: O(40 * 2^n)
     * // 空间复杂度: O(2^n)
     * int numberWays(vector<vector<int>>& hats) {
     *     int n = hats.size();
     *     const int MOD = 1e9 + 7;
     *     vector<long long> dp(1 << n, 0);
     *     dp[0] = 1;
     *     
     *     // 记录每顶帽子可以分配给哪些人
     *     vector<vector<int>> hatToPeople(41);
     *     for (int i = 0; i < n; i++) {
     *         for (int h : hats[i]) {
     *             hatToPeople[h].push_back(i);
     *         }
     *     }
     *     
     *     // 枚举每顶帽子
     *     for (int h = 1; h <= 40; h++) {
     *         // 从后往前更新，避免重复计算
     *         for (int mask = (1 << n) - 1; mask >= 0; mask--) {
     *             // 尝试将当前帽子分配给喜欢它的人
     *             for (int p : hatToPeople[h]) {
     *                 if ((mask & (1 << p)) == 0) {
     *                     dp[mask | (1 << p)] = (dp[mask | (1 << p)] + dp[mask]) % MOD;
     *                 }
     *             }
     *         }
     *     }
     *     
     *     return dp[(1 << n) - 1] % MOD;
     * }
     * 
     * // 补充题目1：我能赢吗
     * // 时间复杂度: O(n * 2^n)
     * // 空间复杂度: O(2^n)
     * bool dfs(int max, int desired, int mask, vector<int>& memo) {
     *     if (memo[mask] != 0) {
     *         return memo[mask] == 1;
     *     }
     *     for (int i = 1; i <= max; i++) {
     *         int bit = 1 << (i - 1);
     *         if ((mask & bit) == 0) {
     *             if (i >= desired || !dfs(max, desired - i, mask | bit, memo)) {
     *                 memo[mask] = 1;
     *                 return true;
     *             }
     *         }
     *     }
     *     memo[mask] = -1;
     *     return false;
     * }
     * 
     * bool canIWin(int maxChoosableInteger, int desiredTotal) {
     *     if (maxChoosableInteger >= desiredTotal) {
     *         return true;
     *     }
     *     if (maxChoosableInteger * (maxChoosableInteger + 1) / 2 < desiredTotal) {
     *         return false;
     *     }
     *     vector<int> memo(1 << maxChoosableInteger, 0);
     *     return dfs(maxChoosableInteger, desiredTotal, 0, memo);
     * }
     * 
     * // 补充题目2：灯泡开关 IV
     * // 时间复杂度: O(n)
     * // 空间复杂度: O(1)
     * int minFlips(string target) {
     *     int flips = 0;
     *     char current = '0';
     *     for (char c : target) {
     *         if (c != current) {
     *             flips++;
     *             current = c;
     *         }
     *     }
     *     return flips;
     * }
     * 
     * // 补充题目3：翻转游戏 II
     * // 时间复杂度: O(n * 2^n)
     * // 空间复杂度: O(2^n)
     * bool canWinHelper(string s, unordered_map<string, bool>& memo) {
     *     if (memo.find(s) != memo.end()) {
     *         return memo[s];
     *     }
     *     for (int i = 0; i < s.size() - 1; i++) {
     *         if (s[i] == '+' && s[i + 1] == '+') {
     *             string next = s.substr(0, i) + "--" + s.substr(i + 2);
     *             if (!canWinHelper(next, memo)) {
     *                 memo[s] = true;
     *                 return true;
     *             }
     *         }
     *     }
     *     memo[s] = false;
     *     return false;
     * }
     * 
     * bool canWin(string s) {
     *     if (s.size() < 2) {
     *         return false;
     *     }
     *     unordered_map<string, bool> memo;
     *     return canWinHelper(s, memo);
     * }
     * 
     * // 补充题目4：最小成本爬楼梯 II
     * // 时间复杂度: O(n * k)
     * // 空间复杂度: O(k)
     * int minCost(vector<vector<int>>& costs) {
     *     if (costs.empty()) {
     *         return 0;
     *     }
     *     int n = costs.size();
     *     int k = costs[0].size();
     *     vector<int> dp(k);
     *     
     *     for (int j = 0; j < k; j++) {
     *         dp[j] = costs[0][j];
     *     }
     *     
     *     for (int i = 1; i < n; i++) {
     *         int min1 = INT_MAX, min2 = INT_MAX;
     *         int idx1 = -1;
     *         for (int j = 0; j < k; j++) {
     *             if (dp[j] < min1) {
     *                 min2 = min1;
     *                 min1 = dp[j];
     *                 idx1 = j;
     *             } else if (dp[j] < min2) {
     *                 min2 = dp[j];
     *             }
     *         }
     *         
     *         vector<int> newDp(k);
     *         for (int j = 0; j < k; j++) {
     *             newDp[j] = costs[i][j] + (j == idx1 ? min2 : min1);
     *         }
     *         dp = move(newDp);
     *     }
     *     
     *     return *min_element(dp.begin(), dp.end());
     * }
     * 
     * // 补充题目5：数组的最大子集和
     * // 时间复杂度: O(n)
     * // 空间复杂度: O(1)
     * int rob(vector<int>& nums) {
     *     if (nums.empty()) {
     *         return 0;
     *     }
     *     if (nums.size() == 1) {
     *         return nums[0];
     *     }
     *     int prev2 = 0;
     *     int prev1 = nums[0];
     *     for (int i = 1; i < nums.size(); i++) {
     *         int curr = max(prev1, prev2 + nums[i]);
     *         prev2 = prev1;
     *         prev1 = curr;
     *     }
     *     return prev1;
     * }
     * 
     * // 补充题目6：目标和
     * // 时间复杂度: O(2^n)
     * // 空间复杂度: O(2^n)
     * int dfsTargetSum(vector<int>& nums, int target, int index, int currentSum, unordered_map<string, int>& memo) {
     *     string key = to_string(index) + "," + to_string(currentSum);
     *     
     *     if (memo.find(key) != memo.end()) {
     *         return memo[key];
     *     }
     *     
     *     if (index == nums.size()) {
     *         return currentSum == target ? 1 : 0;
     *     }
     *     
     *     int add = dfsTargetSum(nums, target, index + 1, currentSum + nums[index], memo);
     *     int subtract = dfsTargetSum(nums, target, index + 1, currentSum - nums[index], memo);
     *     
     *     memo[key] = add + subtract;
     *     return memo[key];
     * }
     * 
     * int findTargetSumWays(vector<int>& nums, int target) {
     *     int n = nums.size();
     *     if (n == 0) {
     *         return target == 0 ? 1 : 0;
     *     }
     *     
     *     // 使用记忆化搜索
     *     unordered_map<string, int> memo;
     *     return dfsTargetSum(nums, target, 0, 0, memo);
     * }
     * 
     * int main() {
     *     // 测试用例
     *     vector<vector<int>> hats = {{3,4}, {4,5}, {5}};
     *     cout << "戴帽子方案数: " << numberWays(hats) << endl;
     *     
     *     cout << "我能赢吗 (10, 11): " << canIWin(10, 11) << endl;
     *     cout << "灯泡开关IV: " << minFlips("10111") << endl;
     *     
     *     string s = "++++";
     *     cout << "翻转游戏II: " << canWin(s) << endl;
     *     
     *     vector<vector<int>> costs = {{1,5,3}, {2,9,4}};
     *     cout << "最小成本: " << minCost(costs) << endl;
     *     
     *     vector<int> nums = {1,2,3,1};
     *     cout << "最大子集和: " << rob(nums) << endl;
     *     
     *     // 测试目标和
     *     vector<int> nums2 = {1,1,1,1,1};
     *     cout << "目标和: " << findTargetSumWays(nums2, 3) << endl;
     *     vector<int> nums3 = {1};
     *     cout << "目标和: " << findTargetSumWays(nums3, 1) << endl;
     *     
     *     return 0;
     * }
     */

/*
 * Python版本实现:
 * 
 * class Solution:
 *     def canIWin(self, maxChoosableInteger: int, desiredTotal: int) -> bool:
 *         # 边界情况：如果目标值为0，先手玩家直接获胜
 *         if desiredTotal <= 0:
 *             return True
 *         
 *         # 如果所有数字之和都小于目标值，则无法获胜
 *         total_sum = maxChoosableInteger * (maxChoosableInteger + 1) // 2
 *         if total_sum < desiredTotal:
 *             return False
 *         
 *         # 使用字典进行记忆化搜索
 *         memo = {}
 *         return self.dfs(maxChoosableInteger, desiredTotal, 0, memo)
 *     
 *     def dfs(self, maxChoosableInteger: int, desiredTotal: int, used: int, memo: dict) -> bool:
 *         # 如果该状态已经计算过，直接返回结果
 *         if used in memo:
 *             return memo[used]
 *         
 *         # 尝试选择每一个未使用的数字
 *         for i in range(maxChoosableInteger):
 *             # 如果数字(i+1)未被使用
 *             if (used & (1 << i)) == 0:
 *                 # 如果选择该数字可以直接获胜，或者选择该数字后对手必败，则当前玩家必胜
 *                 if i + 1 >= desiredTotal or not self.dfs(maxChoosableInteger, desiredTotal - i - 1, used | (1 << i), memo):
 *                     memo[used] = True
 *                     return True
 *         
 *         # 如果所有选择都不能让当前玩家获胜，则当前玩家必败
 *         memo[used] = False
 *         return False
 */

public class Code01_NumberOfWaysWearDifferentHats {

	public static int MOD = 1000000007;

	public static int numberWays(List<List<Integer>> arr) {
		// 帽子颜色的最大值
		int m = 0;
		for (List<Integer> person : arr) {
			for (int hat : person) {
				m = Math.max(m, hat);
			}
		}
		int n = arr.size();
		// 1 ~ m 帽子，能满足哪些人，状态信息 -> int
		int[] hats = new int[m + 1];
		for (int pi = 0; pi < n; pi++) {
			for (int hat : arr.get(pi)) {
				hats[hat] |= 1 << pi;
			}
		}
		int[][] dp = new int[m + 1][1 << n];
		for (int i = 0; i <= m; i++) {
			Arrays.fill(dp[i], -1);
		}
		return f2(hats, m, n, 1, 0, dp);
	}

	// m : 帽子颜色的最大值, 1 ~ m
	// n : 人的数量，0 ~ n-1
	// i : 来到了什么颜色的帽子
	// s : n个人，谁没满足状态就是0，谁满足了状态就是1
	// dp : 记忆化搜索的表
	// 返回 : 有多少种方法
	public static int f1(int[] hats, int m, int n, int i, int s, int[][] dp) {
		if (s == (1 << n) - 1) {
			return 1;
		}
		// 还有人没满足
		if (i == m + 1) {
			return 0;
		}
		if (dp[i][s] != -1) {
			return dp[i][s];
		}
		// 可能性1 : i颜色的帽子，不分配给任何人
		int ans = f1(hats, m, n, i + 1, s, dp);
		// 可能性2 : i颜色的帽子，分配给可能的每一个人
		int cur = hats[i];
		// 用for循环从0 ~ n-1枚举每个人
		for (int p = 0; p < n; p++) {
			if ((cur & (1 << p)) != 0 && (s & (1 << p)) == 0) {
				ans = (ans + f1(hats, m, n, i + 1, s | (1 << p), dp)) % MOD;
			}
		}
		dp[i][s] = ans;
		return ans;
	}

	public static int f2(int[] hats, int m, int n, int i, int s, int[][] dp) {
		if (s == (1 << n) - 1) {
			return 1;
		}
		if (i == m + 1) {
			return 0;
		}
		if (dp[i][s] != -1) {
			return dp[i][s];
		}
		int ans = f2(hats, m, n, i + 1, s, dp);
		int cur = hats[i];
		// 不用for循环枚举
		// 从当前帽子中依次提取能满足的人
		// 提取出二进制状态中最右侧的1，讲解030-异或运算，题目5，Brian Kernighan算法
		// cur :
		// 0 0 0 1 1 0 1 0
		// -> 0 0 0 0 0 0 1 0
		// -> 0 0 0 0 1 0 0 0
		// -> 0 0 0 1 0 0 0 0
		int rightOne;
		while (cur != 0) {
			rightOne = cur & -cur;
			if ((s & rightOne) == 0) {
				ans = (ans + f2(hats, m, n, i + 1, s | rightOne, dp)) % MOD;
			}
			cur ^= rightOne;
		}
		dp[i][s] = ans;
		return ans;
	}
	
	// LeetCode 464. 我能赢吗 解法
    public static boolean canIWin(int maxChoosableInteger, int desiredTotal) {
        // 边界情况：如果目标值为0，先手玩家直接获胜
        if (desiredTotal <= 0) return true;
        
        // 如果所有数字之和都小于目标值，则无法获胜
        int sum = maxChoosableInteger * (maxChoosableInteger + 1) / 2;
        if (sum < desiredTotal) return false;
        
        // dp[status] == 0 代表没算过
        // dp[status] == 1 算过，答案是true
        // dp[status] == -1 算过，答案是false
        int[] dp = new int[1 << (maxChoosableInteger + 1)];
        return dfs(maxChoosableInteger, desiredTotal, 0, dp);
    }
    
    // 深度优先搜索 + 记忆化
    // maxChoosableInteger: 可选择的最大整数
    // desiredTotal: 目标累计和
    // used: 用二进制位表示数字的使用状态
    // dp: 记忆化数组
    // 返回: 当前玩家是否能获胜
    private static boolean dfs(int maxChoosableInteger, int desiredTotal, int used, int[] dp) {
        // 如果该状态已经计算过，直接返回结果
        if (dp[used] != 0) {
            return dp[used] == 1;
        }
        
        // 尝试选择每一个未使用的数字
        for (int i = 1; i <= maxChoosableInteger; i++) {
            // 如果数字i未被使用
            if ((used & (1 << i)) == 0) {
                // 如果选择该数字可以直接获胜，或者选择该数字后对手必败，则当前玩家必胜
                if (i >= desiredTotal || !dfs(maxChoosableInteger, desiredTotal - i, used | (1 << i), dp)) {
                    dp[used] = 1;
                    return true;
                }
            }
        }
        
        // 如果所有选择都不能让当前玩家获胜，则当前玩家必败
        dp[used] = -1;
        return false;
    }

    // 补充题目2：灯泡开关 IV - Java实现
    public static int minFlips(String target) {
        if (target == null || target.isEmpty()) {
            return 0;
        }
        
        int flips = 0;
        char currentState = '0'; // 初始状态是关(0)
        
        for (char c : target.toCharArray()) {
            if (c != currentState) {
                // 需要翻转
                flips++;
                currentState = c; // 更新当前状态
            }
        }
        
        return flips;
    }
    
    // 补充题目3：翻转游戏 II - Java实现
    public static boolean canWin(String s) {
        if (s == null || s.length() < 2) {
            return false;
        }
        
        // 使用Map缓存中间结果，优化性能
        Map<String, Boolean> memo = new HashMap<>();
        return canWinHelper(s, memo);
    }
    
    private static boolean canWinHelper(String s, Map<String, Boolean> memo) {
        // 检查缓存
        if (memo.containsKey(s)) {
            return memo.get(s);
        }
        
        // 尝试所有可能的翻转操作
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == '+' && s.charAt(i + 1) == '+') {
                // 翻转这两个字符
                String nextState = s.substring(0, i) + "--" + s.substring(i + 2);
                
                // 如果对手无法赢，那么当前玩家可以赢
                if (!canWinHelper(nextState, memo)) {
                    memo.put(s, true);
                    return true;
                }
            }
        }
        
        // 所有可能的操作都试过了，当前玩家无法赢
        memo.put(s, false);
        return false;
    }
    
    // 补充题目4：最小成本爬楼梯 II - Java实现
    public static int minCost(int[][] costs) {
        if (costs == null || costs.length == 0) {
            return 0;
        }
        
        int n = costs.length; // 房子数量
        int k = costs[0].length; // 颜色数量
        
        // dp数组，dp[j]表示当前房子涂第j种颜色的最小成本
        int[] dp = new int[k];
        
        // 初始化第一栋房子的成本
        for (int j = 0; j < k; j++) {
            dp[j] = costs[0][j];
        }
        
        // 动态规划填表
        for (int i = 1; i < n; i++) {
            // 找到前一栋房子的最小成本和次小成本（优化）
            int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
            int min1Index = -1;
            
            for (int j = 0; j < k; j++) {
                if (dp[j] < min1) {
                    min2 = min1;
                    min1 = dp[j];
                    min1Index = j;
                } else if (dp[j] < min2) {
                    min2 = dp[j];
                }
            }
            
            // 计算当前房子的成本
            int[] newDp = new int[k];
            for (int j = 0; j < k; j++) {
                // 如果前一栋房子的最小成本颜色和当前颜色不同，直接使用最小成本
                // 否则使用次小成本
                newDp[j] = costs[i][j] + (j == min1Index ? min2 : min1);
            }
            
            dp = newDp;
        }
        
        // 找到最后一栋房子的最小成本
        int result = Integer.MAX_VALUE;
        for (int cost : dp) {
            result = Math.min(result, cost);
        }
        
        return result;
    }
    
    // 补充题目5：数组的最大子集和 - Java实现
    public static int rob(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        
        // 优化空间复杂度，只记录前两个状态
        int prevMax = nums[0]; // dp[i-1]
        int currMax = Math.max(nums[0], nums[1]); // dp[i]
        
        for (int i = 2; i < nums.length; i++) {
            int temp = currMax;
            // 状态转移方程：dp[i] = max(dp[i-1], dp[i-2] + nums[i])
            currMax = Math.max(currMax, prevMax + nums[i]);
            prevMax = temp;
        }
        
        return currMax;
    }
    
    // 补充题目6：目标和 - Java实现
    public static int findTargetSumWays(int[] nums, int target) {
        int n = nums.length;
        if (n == 0) {
            return target == 0 ? 1 : 0;
        }
        
        // 可以用状态压缩DP，但对于n=20，2^20约为1百万，直接遍历所有可能也可行
        // 使用递归来实现状态压缩
        return dfsTargetSum(nums, target, 0, 0, new HashMap<>());
    }
    
    // 递归 + 记忆化搜索
    // nums: 输入数组
    // target: 目标值
    // index: 当前处理的索引
    // currentSum: 当前计算的和
    // memo: 缓存中间结果
    private static int dfsTargetSum(int[] nums, int target, int index, int currentSum, Map<String, Integer> memo) {
        // 构建缓存键
        String key = index + "," + currentSum;
        
        // 检查缓存
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        
        // 递归终止条件
        if (index == nums.length) {
            return currentSum == target ? 1 : 0;
        }
        
        // 选择添加 '+'
        int add = dfsTargetSum(nums, target, index + 1, currentSum + nums[index], memo);
        
        // 选择添加 '-'
        int subtract = dfsTargetSum(nums, target, index + 1, currentSum - nums[index], memo);
        
        // 计算总数并缓存结果
        int total = add + subtract;
        memo.put(key, total);
        
        return total;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试戴帽子方案数
        List<List<Integer>> hats1 = Arrays.asList(
            Arrays.asList(3, 4),
            Arrays.asList(4, 5),
            Arrays.asList(5)
        );
        System.out.println("LeetCode 1434 戴帽子方案数测试:");
        System.out.println("输入: [[3,4], [4,5], [5]]");
        System.out.println("结果: " + numberWays(hats1));
        
        // 测试我能赢吗
        System.out.println("\nLeetCode 464 我能赢吗测试:");
        System.out.println("输入: maxChoosableInteger = 10, desiredTotal = 11");
        System.out.println("结果: " + canIWin(10, 11));
        System.out.println("输入: maxChoosableInteger = 10, desiredTotal = 0");
        System.out.println("结果: " + canIWin(10, 0));
        
        // 测试灯泡开关IV
        System.out.println("\nLeetCode 1529 灯泡开关IV测试:");
        System.out.println("输入: target = '10111'");
        System.out.println("结果: " + minFlips("10111"));
        System.out.println("输入: target = '101'");
        System.out.println("结果: " + minFlips("101"));
        
        // 测试翻转游戏II
        System.out.println("\nLeetCode 294 翻转游戏II测试:");
        System.out.println("输入: s = '++++'");
        System.out.println("结果: " + canWin("++++"));
        System.out.println("输入: s = '++--'");
        System.out.println("结果: " + canWin("++--"));
        
        // 测试最小成本
        int[][] costs = {{1, 5, 3}, {2, 9, 4}};
        System.out.println("\nLeetCode 265 最小成本测试:");
        System.out.println("输入: [[1,5,3], [2,9,4]]");
        System.out.println("结果: " + minCost(costs));
        
        // 测试最大子集和
        int[] nums1 = {1, 2, 3, 1};
        System.out.println("\nLeetCode 198 最大子集和测试:");
        System.out.println("输入: [1,2,3,1]");
        System.out.println("结果: " + rob(nums1));
        int[] nums2 = {2, 7, 9, 3, 1};
        System.out.println("输入: [2,7,9,3,1]");
        System.out.println("结果: " + rob(nums2));
        
        // 测试目标和
        int[] nums3 = {1, 1, 1, 1, 1};
        System.out.println("\nLeetCode 494 目标和测试:");
        System.out.println("输入: nums = [1,1,1,1,1], target = 3");
        System.out.println("结果: " + findTargetSumWays(nums3, 3));
        int[] nums4 = {1};
        System.out.println("输入: nums = [1], target = 1");
        System.out.println("结果: " + findTargetSumWays(nums4, 1));
    }

}