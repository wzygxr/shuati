package class081;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

// 好子集的数目
// 给你一个整数数组 nums，好子集的定义如下：
// nums的某个子集，所有元素的乘积可以表示为一个或多个互不相同质数的乘积
// 比如nums = [1, 2, 3, 4]
// [2, 3]，[1, 2, 3]，[1, 3] 是好子集
// 乘积分别为6=2*3，6=2*3，3=3
// [1, 4]和[4]不是好子集，因为乘积分别为4=2*2和4=2*2
// 返回nums中不同的好子集的数目，答案对 1000000007 取模
// 如果两个子集删除的下标不同，那么它们被视为不同的子集
// 测试链接 : https://leetcode.cn/problems/the-number-of-good-subsets/

// 补充题目1: 最小的必要团队 (Smallest Sufficient Team)
// 给定一个人数组和一个技能需求列表，找出最小的团队使得团队成员掌握的技能能够覆盖所有需求技能。
// 测试链接 : https://leetcode.cn/problems/smallest-sufficient-team/
// 解题思路:
// 1. 状态压缩动态规划解法
// 2. 建立技能到索引的映射，便于位运算
// 3. 将每个人掌握的技能转换为位掩码表示
// 4. dp[mask] 表示覆盖技能集合mask所需的最小团队，使用List存储团队成员索引
// 时间复杂度: O(2^m * n) 其中m是需求技能数，n是人员数
// 空间复杂度: O(2^m)

// 补充题目2: 目标和 (Target Sum)
// 给定一个非负整数数组和一个目标数S，为数组添加'+'或'-'符号，使得和等于S的方式数目。
// 测试链接 : https://leetcode.cn/problems/target-sum/
// 解题思路:
// 1. 状态压缩动态规划解法
// 2. 使用哈希表存储中间状态和对应的路径数目
// 3. 从空子集开始，逐步添加元素并更新可能的和
// 时间复杂度: O(n * sum) 其中n是数组长度，sum是数组元素和
// 空间复杂度: O(sum)

// 补充题目3: 划分为k个相等的子集 (Partition to K Equal Sum Subsets)
// 给定一个整数数组nums和一个整数k，判断是否能将数组分割成k个和相等的子集。
// 测试链接 : https://leetcode.cn/problems/partition-to-k-equal-sum-subsets/
// 解题思路:
// 1. 状态压缩动态规划解法
// 2. 预处理数组和，排除不可能的情况
// 3. 使用位掩码表示已选元素，dp[mask]表示当前子集的和
// 时间复杂度: O(n * 2^n) 其中n是数组长度
// 空间复杂度: O(2^n)

// 补充题目4: 匹配子序列的单词数 (Number of Matching Subsequences)
// 给定字符串s和一个单词数组words，返回words中是s的子序列的单词数目。
// 测试链接 : https://leetcode.cn/problems/number-of-matching-subsequences/
// 解题思路:
// 1. 预处理字符串s中每个字符的出现位置
// 2. 对每个单词，使用二分查找判断是否为s的子序列
// 时间复杂度: O(s.length + words.length * word.length * log(s.length))
// 空间复杂度: O(26 * s.length)

/*
 * C++版本实现:
 * 
 * class Solution {
 * public:
 *     vector<int> smallestSufficientTeam(vector<string>& req_skills, vector<vector<string>>& people) {
 *         int m = req_skills.size(), n = people.size();
 *         
 *         unordered_map<string, int> skillIndex;
 *         for (int i = 0; i < m; i++) {
 *             skillIndex[req_skills[i]] = i;
 *         }
 *         
 *         vector<int> peopleSkills(n, 0);
 *         for (int i = 0; i < n; i++) {
 *             for (const string& skill : people[i]) {
 *                 peopleSkills[i] |= 1 << skillIndex[skill];
 *             }
 *         }
 *         
 *         vector<vector<int>> dp(1 << m);
 *         dp[0] = vector<int>();
 *         
 *         for (int mask = 0; mask < (1 << m); mask++) {
 *             if (dp[mask].empty() && mask != 0) continue;
 *             
 *             for (int i = 0; i < n; i++) {
 *                 int newMask = mask | peopleSkills[i];
 *                 
 *                 if (dp[newMask].empty() || dp[newMask].size() > dp[mask].size() + 1) {
 *                     dp[newMask] = dp[mask];
 *                     dp[newMask].push_back(i);
 *                 }
 *             }
 *         }
 *         
 *         return dp[(1 << m) - 1];
 *     }
 * };
 */

/*
 * Python版本实现:
 * 
 * class Solution:
 *     def smallestSufficientTeam(self, req_skills: List[str], people: List[List[str]]) -> List[int]:
 *         m, n = len(req_skills), len(people)
 *         
 *         # 建立技能到索引的映射，便于位运算
 *         skill_index = {skill: i for i, skill in enumerate(req_skills)}
 *         
 *         # 将每个人掌握的技能转换为位掩码表示
 *         people_skills = [0] * n
 *         for i in range(n):
 *             for skill in people[i]:
 *                 people_skills[i] |= 1 << skill_index[skill]
 *         
 *         # dp[mask] 表示覆盖技能集合mask所需的最小团队
 *         # 使用列表存储团队成员索引
 *         dp = [None] * (1 << m)
 *         dp[0] = []  # 空技能集不需要任何人
 *         
 *         # 遍历所有可能的技能组合状态
 *         for mask in range(1 << m):
 *             # 如果当前状态不可达，跳过
 *             if dp[mask] is None:
 *                 continue
 *             
 *             # 尝试添加每个人员
 *             for i in range(n):
 *                 # 添加人员i后的新技能集合
 *                 new_mask = mask | people_skills[i]
 *                 
 *                 # 如果新状态未被访问过，或者通过当前路径能得到更小的团队
 *                 if dp[new_mask] is None or len(dp[new_mask]) > len(dp[mask]) + 1:
 *                     dp[new_mask] = dp[mask] + [i]
 *         
 *         return dp[(1 << m) - 1] if dp[(1 << m) - 1] is not None else []
 */

public class Code03_TheNumberOfGoodSubsets {

	public static int MAXV = 30;

	public static int LIMIT = (1 << 10);

	public static int MOD = 1000000007;

	// 打个表来加速判断
	// 如果一个数字拥有某一种质数因子不只1个
	// 那么认为这个数字无效，状态全是0，0b0000000000
	// 如果一个数字拥有任何一种质数因子都不超过1个
	// 那么认为这个数字有效，用位信息表示这个数字拥有质数因子的状态
	// 比如12，拥有2这种质数因子不只1个，所以无效，用0b0000000000表示
	// 比如14，拥有2这种质数因子不超过1个，拥有7这种质数因子不超过1个，有效
	// 从高位到低位依次表示：...13 11 7 5 3 2
	// 所以用0b0000001001表示14拥有质数因子的状态
	// 质数: 29 23 19 17 13 11 7 5 3 2
	// 位置: 9 8 7 6 5 4 3 2 1 0
	public static int[] own = { 0b0000000000, // 0
			0b0000000000, // 1
			0b0000000001, // 2
			0b0000000010, // 3
			0b0000000000, // 4
			0b0000000100, // 5
			0b0000000011, // 6
			0b0000001000, // 7
			0b0000000000, // 8
			0b0000000000, // 9
			0b0000000101, // 10
			0b0000010000, // 11
			0b0000000000, // 12
			0b0000100000, // 13
			0b0000001001, // 14
			0b0000000110, // 15
			0b0000000000, // 16
			0b0001000000, // 17
			0b0000000000, // 18
			0b0010000000, // 19
			0b0000000000, // 20
			0b0000001010, // 21
			0b0000010001, // 22
			0b0100000000, // 23
			0b0000000000, // 24
			0b0000000000, // 25
			0b0000100001, // 26
			0b0000000000, // 27
			0b0000000000, // 28
			0b1000000000, // 29
			0b0000000111 // 30
	};

	// 记忆化搜索
	public static int numberOfGoodSubsets1(int[] nums) {
		// 1 ~ 30
		int[] cnt = new int[MAXV + 1];
		for (int num : nums) {
			cnt[num]++;
		}
		int[][] dp = new int[MAXV + 1][LIMIT];
		for (int i = 0; i <= MAXV; i++) {
			Arrays.fill(dp[i], -1);
		}
		int ans = 0;
		for (int s = 1; s < LIMIT; s++) {
			ans = (ans + f1(MAXV, s, cnt, dp)) % MOD;
		}
		return ans;
	}

	// 1....i范围的数字，每种数字cnt[i]个
	// 最终相乘的结果一定要让质因子的状态为s，且每种质因子只能有1个
	// 请问子集的数量是多少
	// s每一位代表的质因子如下
	// 质数: 29 23 19 17 13 11 7 5 3 2
	// 位置: 9 8 7 6 5 4 3 2 1 0
	public static int f1(int i, int s, int[] cnt, int[][] dp) {
		if (dp[i][s] != -1) {
			return dp[i][s];
		}
		int ans = 0;
		if (i == 1) {
			if (s == 0) {
				ans = 1;
				for (int j = 0; j < cnt[1]; j++) {
					ans = (ans << 1) % MOD;
				}
			}
		} else {
			ans = f1(i - 1, s, cnt, dp);
			int cur = own[i];
			int times = cnt[i];
			if (cur != 0 && times != 0 && (s & cur) == cur) {
				// 能要i这个数字
				ans = (int) (((long) f1(i - 1, s ^ cur, cnt, dp) * times + ans) % MOD);
			}
		}
		dp[i][s] = ans;
		return ans;
	}

	// 空间压缩优化
	public static int[] cnt = new int[MAXV + 1];

	public static int[] dp = new int[LIMIT];

	public static int numberOfGoodSubsets2(int[] nums) {
		Arrays.fill(cnt, 0);
		Arrays.fill(dp, 0);
		for (int num : nums) {
			cnt[num]++;
		}
		dp[0] = 1;
		for (int i = 0; i < cnt[1]; i++) {
			dp[0] = (dp[0] << 1) % MOD;
		}
		for (int i = 2, cur, times; i <= MAXV; i++) {
			cur = own[i];
			times = cnt[i];
			if (cur != 0 && times != 0) {
				for (int status = LIMIT - 1; status >= 0; status--) {
					if ((status & cur) == cur) {
						dp[status] = (int) (((long) dp[status ^ cur] * times + dp[status]) % MOD);
					}
				}
			}
		}
		int ans = 0;
		for (int s = 1; s < LIMIT; s++) {
			ans = (ans + dp[s]) % MOD;
		}
		return ans;
	}

	// 补充题目1: 最小的必要团队
	// 时间复杂度: O(2^m * n)
	// 空间复杂度: O(2^m)
	public static List<Integer> smallestSufficientTeam(List<String> reqSkills, List<List<String>> people) {
		int m = reqSkills.size();
		int n = people.size();
		
		// 建立技能到索引的映射，便于位运算
		Map<String, Integer> skillIndex = new HashMap<>();
		for (int i = 0; i < m; i++) {
			skillIndex.put(reqSkills.get(i), i);
		}
		
		// 将每个人掌握的技能转换为位掩码表示
		int[] peopleSkills = new int[n];
		for (int i = 0; i < n; i++) {
			for (String skill : people.get(i)) {
				peopleSkills[i] |= 1 << skillIndex.get(skill);
			}
		}
		
		// dp[mask] 表示覆盖技能集合mask所需的最小团队
		List<Integer>[] dp = new List[1 << m];
		dp[0] = new ArrayList<>();
		
		// 遍历所有可能的技能组合状态
		for (int mask = 0; mask < (1 << m); mask++) {
			// 如果当前状态不可达，跳过
			if (dp[mask] == null) {
				continue;
			}
			
			// 尝试添加每个人员
			for (int i = 0; i < n; i++) {
				// 添加人员i后的新技能集合
				int newMask = mask | peopleSkills[i];
				
				// 如果新状态未被访问过，或者通过当前路径能得到更小的团队
				if (dp[newMask] == null || dp[newMask].size() > dp[mask].size() + 1) {
					dp[newMask] = new ArrayList<>(dp[mask]);
					dp[newMask].add(i);
				}
			}
		}
		
		return dp[(1 << m) - 1];
	}
	
	// 补充题目2: 目标和
	// 时间复杂度: O(n * sum)
	// 空间复杂度: O(sum)
	public static int findTargetSumWays(int[] nums, int target) {
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		
		// 无法通过正负符号得到目标和的情况
		if (target < -sum || target > sum || (sum + target) % 2 != 0) {
			return 0;
		}
		
		int s = (sum + target) / 2;
		int[] dp = new int[s + 1];
		dp[0] = 1;
		
		for (int num : nums) {
			for (int j = s; j >= num; j--) {
				dp[j] += dp[j - num];
			}
		}
		
		return dp[s];
	}
	
	// 补充题目3: 划分为k个相等的子集
	// 时间复杂度: O(n * 2^n)
	// 空间复杂度: O(2^n)
	public static boolean canPartitionKSubsets(int[] nums, int k) {
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		
		// 无法平均分配的情况
		if (sum % k != 0) {
			return false;
		}
		
		int target = sum / k;
		int n = nums.length;
		
		// 降序排序，优化剪枝
		Arrays.sort(nums);
		reverse(nums);
		
		// 如果最大的数超过目标值，无法分配
		if (nums[0] > target) {
			return false;
		}
		
		// dp[mask] 表示当前选的元素集合的和模target的结果
		int[] dp = new int[1 << n];
		Arrays.fill(dp, -1);
		dp[0] = 0;
		
		for (int mask = 0; mask < (1 << n); mask++) {
			if (dp[mask] == -1) {
				continue;
			}
			
			for (int i = 0; i < n; i++) {
				if ((mask & (1 << i)) == 0 && dp[mask] + nums[i] <= target) {
					dp[mask | (1 << i)] = (dp[mask] + nums[i]) % target;
				}
			}
		}
		
		return dp[(1 << n) - 1] == 0;
	}
	
	// 辅助方法：反转数组
	private static void reverse(int[] nums) {
		int left = 0, right = nums.length - 1;
		while (left < right) {
			int temp = nums[left];
			nums[left] = nums[right];
			nums[right] = temp;
			left++;
			right--;
		}
	}
	
	// 补充题目4: 匹配子序列的单词数
	// 时间复杂度: O(s.length + words.length * word.length * log(s.length))
	// 空间复杂度: O(26 * s.length)
	public static int numMatchingSubseq(String s, String[] words) {
		// 预处理字符串s中每个字符的出现位置
		Map<Character, List<Integer>> charPos = new HashMap<>();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			charPos.putIfAbsent(c, new ArrayList<>());
			charPos.get(c).add(i);
		}
		
		int count = 0;
		for (String word : words) {
			if (isSubsequence(word, charPos)) {
				count++;
			}
		}
		return count;
	}
	
	private static boolean isSubsequence(String word, Map<Character, List<Integer>> charPos) {
		int currentPos = -1;
		for (char c : word.toCharArray()) {
			if (!charPos.containsKey(c)) {
				return false;
			}
			
			// 二分查找大于currentPos的最小位置
			List<Integer> positions = charPos.get(c);
			int left = 0, right = positions.size();
			while (left < right) {
				int mid = left + (right - left) / 2;
				if (positions.get(mid) > currentPos) {
					right = mid;
				} else {
					left = mid + 1;
				}
			}
			
			if (left == positions.size()) {
				return false;
			}
			currentPos = positions.get(left);
		}
		return true;
	}

	public static void main(String[] args) {
		// 测试好子集的数目
		int[] nums1 = {1, 2, 3, 4};
		System.out.println("好子集的数目: " + numberOfGoodSubsets2(nums1));
		
		// 测试目标和
		int[] nums2 = {1, 1, 1, 1, 1};
		int target = 3;
		System.out.println("目标和的方式数目: " + findTargetSumWays(nums2, target));
		
		// 测试划分为k个相等的子集
		int[] nums3 = {4, 3, 2, 3, 5, 2, 1};
		int k = 4;
		System.out.println("能否划分为" + k + "个子集: " + canPartitionKSubsets(nums3, k));
		
		// 测试匹配子序列的单词数
		String s = "abcde";
		String[] words = {"a", "bb", "acd", "ace"};
		System.out.println("匹配子序列的单词数: " + numMatchingSubseq(s, words));
	}
}

/*
 * C++ 实现
 */
// #include <iostream>
// #include <vector>
// #include <string>
// #include <unordered_map>
// #include <algorithm>
// using namespace std;

// // 主题目：好子集的数目
// class Solution {
// private:
//     const int MOD = 1e9 + 7;
//     vector<int> primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};
//     int primeMask(int num) {
//         int mask = 0;
//         for (int i = 0; i < primes.size(); i++) {
//             int cnt = 0;
//             while (num % primes[i] == 0) {
//                 cnt++;
//                 num /= primes[i];
//             }
//             if (cnt > 1) return -1; // 含有平方因子
//             if (cnt == 1) mask |= (1 << i);
//         }
//         return mask;
//     }
// 
// public:
//     int numberOfGoodSubsets(vector<int>& nums) {
//         vector<int> freq(31, 0);
//         for (int num : nums) {
//             freq[num]++;
//         }
//         
//         vector<long long> dp(1 << 10, 0);
//         dp[0] = 1;
//         
//         for (int i = 2; i <= 30; i++) {
//             if (freq[i] == 0) continue;
//             int mask = primeMask(i);
//             if (mask == -1) continue;
//             
//             for (int j = (1 << 10) - 1; j >= 0; j--) {
//                 if ((j & mask) == 0) {
//                     dp[j | mask] = (dp[j | mask] + dp[j] * freq[i]) % MOD;
//                 }
//             }
//         }
//         
//         long long ans = 0;
//         for (int i = 1; i < (1 << 10); i++) {
//             ans = (ans + dp[i]) % MOD;
//         }
//         
//         // 处理1的情况
//         for (int i = 0; i < freq[1]; i++) {
//             ans = ans * 2 % MOD;
//         }
//         
//         return ans;
//     }
// };

// // 补充题目1: 最小的必要团队
// vector<int> smallestSufficientTeam(vector<string>& reqSkills, vector<vector<string>>& people) {
//     int m = reqSkills.size();
//     int n = people.size();
//     
//     unordered_map<string, int> skillIndex;
//     for (int i = 0; i < m; i++) {
//         skillIndex[reqSkills[i]] = i;
//     }
//     
//     vector<int> peopleSkills(n, 0);
//     for (int i = 0; i < n; i++) {
//         for (string& skill : people[i]) {
//             peopleSkills[i] |= 1 << skillIndex[skill];
//         }
//     }
//     
//     vector<vector<int>> dp(1 << m);
//     vector<int> size(1 << m, INT_MAX);
//     dp[0] = {};
//     size[0] = 0;
//     
//     for (int mask = 0; mask < (1 << m); mask++) {
//         if (size[mask] == INT_MAX) continue;
//         
//         for (int i = 0; i < n; i++) {
//             int newMask = mask | peopleSkills[i];
//             if (size[newMask] > size[mask] + 1) {
//                 size[newMask] = size[mask] + 1;
//                 dp[newMask] = dp[mask];
//                 dp[newMask].push_back(i);
//             }
//         }
//     }
//     
//     return dp[(1 << m) - 1];
// }

// // 补充题目2: 目标和
// int findTargetSumWays(vector<int>& nums, int target) {
//     int sum = 0;
//     for (int num : nums) sum += num;
//     
//     if (target < -sum || target > sum || (sum + target) % 2 != 0) {
//         return 0;
//     }
//     
//     int s = (sum + target) / 2;
//     vector<int> dp(s + 1, 0);
//     dp[0] = 1;
//     
//     for (int num : nums) {
//         for (int j = s; j >= num; j--) {
//             dp[j] += dp[j - num];
//         }
//     }
//     
//     return dp[s];
// }

// // 补充题目3: 划分为k个相等的子集
// bool canPartitionKSubsets(vector<int>& nums, int k) {
//     int sum = 0;
//     for (int num : nums) sum += num;
//     
//     if (sum % k != 0) return false;
//     
//     int target = sum / k;
//     int n = nums.size();
//     
//     sort(nums.begin(), nums.end(), greater<int>());
//     if (nums[0] > target) return false;
//     
//     vector<int> dp(1 << n, -1);
//     dp[0] = 0;
//     
//     for (int mask = 0; mask < (1 << n); mask++) {
//         if (dp[mask] == -1) continue;
//         
//         for (int i = 0; i < n; i++) {
//             if (!(mask & (1 << i)) && dp[mask] + nums[i] <= target) {
//                 dp[mask | (1 << i)] = (dp[mask] + nums[i]) % target;
//             }
//         }
//     }
//     
//     return dp[(1 << n) - 1] == 0;
// }

// // 补充题目4: 匹配子序列的单词数
// int numMatchingSubseq(string s, vector<string>& words) {
//     unordered_map<char, vector<int>> charPos;
//     for (int i = 0; i < s.size(); i++) {
//         charPos[s[i]].push_back(i);
//     }
//     
//     int count = 0;
//     for (string& word : words) {
//         int currentPos = -1;
//         bool isSub = true;
//         
//         for (char c : word) {
//             if (charPos.find(c) == charPos.end()) {
//                 isSub = false;
//                 break;
//             }
//             
//             auto& positions = charPos[c];
//             auto it = upper_bound(positions.begin(), positions.end(), currentPos);
//             if (it == positions.end()) {
//                 isSub = false;
//                 break;
//             }
//             
//             currentPos = *it;
//         }
//         
//         if (isSub) count++;
//     }
//     
//     return count;
// }

// int main() {
//     // 测试好子集的数目
//     vector<int> nums1 = {1, 2, 3, 4};
//     Solution sol;
//     cout << "好子集的数目: " << sol.numberOfGoodSubsets(nums1) << endl;
//     
//     // 测试目标和
//     vector<int> nums2 = {1, 1, 1, 1, 1};
//     int target = 3;
//     cout << "目标和的方式数目: " << findTargetSumWays(nums2, target) << endl;
//     
//     // 测试划分为k个相等的子集
//     vector<int> nums3 = {4, 3, 2, 3, 5, 2, 1};
//     int k = 4;
//     cout << "能否划分为" << k << "个子集: " << (canPartitionKSubsets(nums3, k) ? "true" : "false") << endl;
//     
//     // 测试匹配子序列的单词数
//     string s = "abcde";
//     vector<string> words = {"a", "bb", "acd", "ace"};
//     cout << "匹配子序列的单词数: " << numMatchingSubseq(s, words) << endl;
//     
//     return 0;
// }

/*
 * Python 实现
 */
// import bisect

// // 主题目：好子集的数目
// class Solution:
//     def numberOfGoodSubsets(self, nums):
//         MOD = 10**9 + 7
//         primes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
//         
//         def prime_mask(num):
//             mask = 0
//             for i, p in enumerate(primes):
//                 cnt = 0
//                 while num % p == 0:
//                     cnt += 1
//                     num //= p
//                 if cnt > 1:
//                     return -1
//                 if cnt == 1:
//                     mask |= 1 << i
//             return mask
//         
//         freq = [0] * 31
//         for num in nums:
//             freq[num] += 1
//         
//         dp = [0] * (1 << 10)
//         dp[0] = 1
//         
//         for i in range(2, 31):
//             if freq[i] == 0:
//                 continue
//             mask = prime_mask(i)
//             if mask == -1:
//                 continue
//             
//             for j in range((1 << 10) - 1, -1, -1):
//                 if not (j & mask):
//                     dp[j | mask] = (dp[j | mask] + dp[j] * freq[i]) % MOD
//         
//         ans = sum(dp[1:]) % MOD
//         
//         // 处理1的情况
//         ans = ans * pow(2, freq[1], MOD) % MOD
//         
//         return ans

// // 补充题目1: 最小的必要团队
// def smallest_sufficient_team(req_skills, people):
//     m = len(req_skills)
//     n = len(people)
//     
//     skill_index = {skill: i for i, skill in enumerate(req_skills)}
//     
//     people_skills = [0] * n
//     for i in range(n):
//         for skill in people[i]:
//             people_skills[i] |= 1 << skill_index[skill]
//     
//     dp = [None] * (1 << m)
//     dp[0] = []
//     
//     for mask in range(1 << m):
//         if dp[mask] is None:
//             continue
//         
//         for i in range(n):
//             new_mask = mask | people_skills[i]
//             if dp[new_mask] is None or len(dp[new_mask]) > len(dp[mask]) + 1:
//                 dp[new_mask] = dp[mask] + [i]
//     
//     return dp[(1 << m) - 1]

// // 补充题目2: 目标和
// def find_target_sum_ways(nums, target):
//     total = sum(nums)
//     
//     if target < -total or target > total or (total + target) % 2 != 0:
//         return 0
//     
//     s = (total + target) // 2
//     dp = [0] * (s + 1)
//     dp[0] = 1
//     
//     for num in nums:
//         for j in range(s, num - 1, -1):
//             dp[j] += dp[j - num]
//     
//     return dp[s]

// // 补充题目3: 划分为k个相等的子集
// def can_partition_k_subsets(nums, k):
//     total = sum(nums)
//     
//     if total % k != 0:
//         return False
//     
//     target = total // k
//     n = len(nums)
//     
//     nums.sort(reverse=True)
//     if nums[0] > target:
//         return False
//     
//     dp = [-1] * (1 << n)
//     dp[0] = 0
//     
//     for mask in range(1 << n):
//         if dp[mask] == -1:
//             continue
//         
//         for i in range(n):
//             if not (mask & (1 << i)) and dp[mask] + nums[i] <= target:
//                 dp[mask | (1 << i)] = (dp[mask] + nums[i]) % target
//     
//     return dp[(1 << n) - 1] == 0

// // 补充题目4: 匹配子序列的单词数
// def num_matching_subseq(s, words):
//     char_pos = {}
//     for i, c in enumerate(s):
//         if c not in char_pos:
//             char_pos[c] = []
//         char_pos[c].append(i)
//     
//     count = 0
//     for word in words:
//         current_pos = -1
//         is_sub = True
//         
//         for c in word:
//             if c not in char_pos:
//                 is_sub = False
//                 break
//             
//             positions = char_pos[c]
//             idx = bisect.bisect_right(positions, current_pos)
//             if idx == len(positions):
//                 is_sub = False
//                 break
//             
//             current_pos = positions[idx]
//         
//         if is_sub:
//             count += 1
//     
//     return count

// // 测试代码
// if __name__ == "__main__":
//     // 测试好子集的数目
//     nums1 = [1, 2, 3, 4]
//     sol = Solution()
//     print("好子集的数目:", sol.numberOfGoodSubsets(nums1))
//     
//     // 测试目标和
//     nums2 = [1, 1, 1, 1, 1]
//     target = 3
//     print("目标和的方式数目:", find_target_sum_ways(nums2, target))
//     
//     // 测试划分为k个相等的子集
//     nums3 = [4, 3, 2, 3, 5, 2, 1]
//     k = 4
//     print("能否划分为" + str(k) + "个子集:", can_partition_k_subsets(nums3, k))
//     
//     // 测试匹配子序列的单词数
//     s = "abcde"
//     words = ["a", "bb", "acd", "ace"]
//     print("匹配子序列的单词数:", num_matching_subseq(s, words))