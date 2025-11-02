package class081;

import java.util.Arrays;

// 分配重复整数
// 给你一个长度为n的整数数组nums，这个数组中至多有50个不同的值
// 同时你有m个顾客的订单quantity，其中整数quantity[i]是第i位顾客订单的数目
// 请你判断是否能将nums中的整数分配给这些顾客，且满足：
// 第i位顾客恰好有quantity[i]个整数、第i位顾客拿到的整数都是相同的
// 每位顾客都要满足上述两个要求，返回是否能都满足
// 测试链接 : https://leetcode.cn/problems/distribute-repeating-integers/

// 补充题目1: 目标和 (Target Sum)
// 题目来源: LeetCode 494 目标和
// 题目链接: https://leetcode.cn/problems/target-sum/
// 题目描述:
// 给你一个非负整数数组 nums 和一个整数 target 。
// 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：
// 例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，然后串联起来得到表达式 "+2-1" 。
// 返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。
// 解题思路:
// 1. 动态规划解法
// 2. sumP - sumN = target
//    sumP + sumN = sumNums
//    解得: sumP = (target + sumNums) / 2
// 3. 转化为背包问题，求选出若干元素使得和为sumP的方案数
// 时间复杂度: O(n * sumP)
// 空间复杂度: O(sumP)

// 补充题目2: 划分为k个相等的子集 (Partition to K Equal Sum Subsets)
// 题目来源: LeetCode 698 划分为k个相等的子集
// 题目链接: https://leetcode.cn/problems/partition-to-k-equal-sum-subsets/
// 题目描述:
// 给定一个整数数组 nums 和一个正整数 k，找出是否有可能把这个数组分成 k 个非空子集，其总和都相等。
// 解题思路:
// 1. 状态压缩动态规划解法
// 2. 预处理数组和，检查是否能被k整除
// 3. dp[mask] 表示当前使用mask表示的元素，能组成多少个完整的子集，以及当前子集的和
// 4. 枚举每个元素，更新状态
// 时间复杂度: O(n * 2^n)
// 空间复杂度: O(2^n)

// 补充题目3: 火柴拼正方形 (Matchsticks to Square)
// 题目来源: LeetCode 473 火柴拼正方形
// 题目链接: https://leetcode.cn/problems/matchsticks-to-square/
// 题目描述:
// 你将得到一个整数数组 matchsticks ，其中 matchsticks[i] 是第i个火柴棒的长度。
// 你要用 所有的火柴棍 拼成一个正方形。
// 你 不能折断 任何一根火柴棒，但你可以把它们连接起来，而且每根火柴棒必须 使用一次 。
// 如果你能使这个正方形，则返回 true ，否则返回 false 。
// 解题思路:
// 1. 转化为划分为4个相等子集的问题
// 2. 使用状态压缩动态规划或回溯剪枝
// 时间复杂度: O(n * 2^n)
// 空间复杂度: O(2^n)

// 补充题目4: 参加考试的最大学生数 (Maximum Students Taking Exam)
// 给你一个 m * n 的矩阵 seats 表示教室中的座位分布。如果座位是坏的（不可用），就用 '#' 表示；否则，用 '.' 表示。
// 学生可以看到左侧、右侧、左上、右上这四个方向上直接相邻的学生的答卷，但是看不到直接坐在我前面或者后面的学生的答卷。
// 请你计算并返回该考场可以容纳的一起参加考试且无法作弊的最大学生人数。
// 测试链接 : https://leetcode.cn/problems/maximum-students-taking-exam/

import java.util.Arrays;

public class Code04_DistributeRepeatingIntegers {
	
	// 时间复杂度O(n * 3的m次方)，空间复杂度O(n * 2的m次方)
	public static boolean canDistribute(int[] nums, int[] quantity) {
		Arrays.sort(nums);
		int n = 1;
		for (int i = 1; i < nums.length; i++) {
			if (nums[i - 1] != nums[i]) {
				n++;
			}
		}
		int[] cnt = new int[n];
		int c = 1;
		for (int i = 1, j = 0; i < nums.length; i++) {
			if (nums[i - 1] != nums[i]) {
				cnt[j++] = c;
				c = 1;
			} else {
				c++;
			}
		}
		cnt[n - 1] = c;
		int m = quantity.length;
		int[] sum = new int[1 << m];
		// 下面这个枚举是生成quantity中的每个子集，所需要数字的个数
		for (int i = 0, v, h; i < quantity.length; i++) {
			v = quantity[i];
			h = 1 << i;
			for (int j = 0; j < h; j++) {
				sum[h | j] = sum[j] + v;
			}
		}
		int[][] dp = new int[1 << m][n];
		return f(cnt, sum, (1 << m) - 1, 0, dp);
	}

	// 当前剩余需要满足的集合是s，当前来到第i种数字
	// 返回是否能满足要求
	public static boolean f(int[] cnt, int[] sum, int s, int i, int[][] dp) {
		if (s == 0) {
			return true;
		}
		if (i == cnt.length) {
			return false;
		}
		if (dp[s][i] != 0) {
			return dp[s][i] == 1;
		}
		boolean ans = f(cnt, sum, s, i + 1, dp);
		// 枚举第i种数字可以满足的子集
		int sub = s;
		// 位运算技巧：枚举子集
		// 例如s=1101，那么枚举会得到：1101、1001、1100、1000、0101、0100、0001
		// 但这个过程比较低效
		// 下面是高效枚举子集的方式
		for (int j = sub; j > 0; j = (j - 1) & sub) {
			if (sum[j] <= cnt[i] && f(cnt, sum, s ^ j, i + 1, dp)) {
				ans = true;
				break;
			}
		}
		dp[s][i] = ans ? 1 : -1;
		return ans;
	}
	
	public static void main(String[] args) {
		// 测试主问题：分配重复整数
		int[] nums1 = {1, 2, 3, 4};
		int[] quantity1 = {2};
		System.out.println("分配重复整数测试1: " + canDistribute(nums1, quantity1)); // 应输出 true
		
		int[] nums2 = {1, 2, 3, 3};
		int[] quantity2 = {2};
		System.out.println("分配重复整数测试2: " + canDistribute(nums2, quantity2)); // 应输出 true
		
		// 测试补充题目1: 目标和
		System.out.println("\n目标和测试:");
		int[] nums3 = {1, 1, 1, 1, 1};
		int target1 = 3;
		System.out.println("nums = [1,1,1,1,1], target = 3: " + findTargetSumWays(nums3, target1)); // 应输出 5
		
		// 测试补充题目2: 划分为k个相等的子集
		System.out.println("\n划分为k个相等的子集测试:");
		int[] nums4 = {4, 3, 2, 3, 5, 2, 1};
		int k1 = 4;
		System.out.println("nums = [4,3,2,3,5,2,1], k = 4: " + canPartitionKSubsets(nums4, k1)); // 应输出 true
		
		// 测试补充题目3: 火柴拼正方形
		System.out.println("\n火柴拼正方形测试:");
		int[] matchsticks = {1, 1, 2, 2, 2};
		System.out.println("matchsticks = [1,1,2,2,2]: " + makesquare(matchsticks)); // 应输出 true
		
		// 测试补充题目4: 参加考试的最大学生数
		System.out.println("\n参加考试的最大学生数测试:");
		char[][] seats = {
			{'.', '#', '.'}, 
			{'.', '.', '.'}, 
			{'.', '.', '.'}
		};
		System.out.println("seats = [[.,#,.,], [.,.,.,], [.,.,.,]]: " + maxStudents(seats)); // 应输出 4
	}
	
	// 补充题目1: 目标和解法
	public static int findTargetSumWays(int[] nums, int target) {
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		
		// 检查是否有解
		if ((sum + target) % 2 != 0 || sum < Math.abs(target)) {
			return 0;
		}
		
		int targetSum = (sum + target) / 2;
		// 动态规划数组，dp[i]表示和为i的方案数
		int[] dp = new int[targetSum + 1];
		dp[0] = 1; // 基础情况：和为0的方案数为1（不选任何元素）
		
		for (int num : nums) {
			for (int j = targetSum; j >= num; j--) {
				dp[j] += dp[j - num];
			}
		}
		
		return dp[targetSum];
	}
	
	// 补充题目1: 目标和解法 - C++代码
	/*
	int findTargetSumWays(vector<int>& nums, int target) {
	    int sum = 0;
	    for (int num : nums) {
	        sum += num;
	    }
	    
	    if ((sum + target) % 2 != 0 || sum < abs(target)) {
	        return 0;
	    }
	    
	    int targetSum = (sum + target) / 2;
	    vector<int> dp(targetSum + 1, 0);
	    dp[0] = 1;
	    
	    for (int num : nums) {
	        for (int j = targetSum; j >= num; j--) {
	            dp[j] += dp[j - num];
	        }
	    }
	    
	    return dp[targetSum];
	}
	*/
	
	// 补充题目1: 目标和解法 - Python代码
	/*
	def findTargetSumWays(nums, target):
	    sum_total = sum(nums)
	    
	    if (sum_total + target) % 2 != 0 or sum_total < abs(target):
	        return 0
	    
	    target_sum = (sum_total + target) // 2
	    dp = [0] * (target_sum + 1)
	    dp[0] = 1
	    
	    for num in nums:
	        for j in range(target_sum, num - 1, -1):
	            dp[j] += dp[j - num]
	    
	    return dp[target_sum]
	*/
	
	// 补充题目2: 划分为k个相等的子集解法
	public static boolean canPartitionKSubsets(int[] nums, int k) {
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		
		// 检查总和是否能被k整除
		if (sum % k != 0) {
			return false;
		}
		
		int target = sum / k;
		int n = nums.length;
		
		// 排序，从大到小，方便剪枝
		Arrays.sort(nums);
		reverse(nums);
		
		// 如果最大元素超过目标和，直接返回false
		if (nums[0] > target) {
			return false;
		}
		
		// 状态压缩DP
		// dp[mask] = current sum of the subset we are building
		int[] dp = new int[1 << n];
		Arrays.fill(dp, -1);
		dp[0] = 0;
		
		for (int mask = 0; mask < (1 << n); mask++) {
			if (dp[mask] == -1) {
				continue; // 不可达状态
			}
			
			for (int i = 0; i < n; i++) {
				if ((mask & (1 << i)) == 0) { // 第i个元素还没选
					// 当前子集的和加上nums[i]不超过target
					if (dp[mask] + nums[i] <= target) {
						// 更新新状态
						int newMask = mask | (1 << i);
						// 新子集的和
						int newSum = (dp[mask] + nums[i]) % target;
						if (dp[newMask] == -1 || newSum < dp[newMask]) {
							dp[newMask] = newSum;
						}
					}
				}
			}
		}
		
		// 如果全部元素都被选了，且最后一个子集的和为0（表示刚好填满）
		return dp[(1 << n) - 1] == 0;
	}
	
	// 补充题目2: 划分为k个相等的子集解法 - C++代码
	/*
	bool canPartitionKSubsets(vector<int>& nums, int k) {
	    int sum = accumulate(nums.begin(), nums.end(), 0);
	    
	    if (sum % k != 0) {
	        return false;
	    }
	    
	    int target = sum / k;
	    int n = nums.size();
	    
	    // 排序，从大到小
	    sort(nums.begin(), nums.end(), greater<int>());
	    
	    if (nums[0] > target) {
	        return false;
	    }
	    
	    vector<int> dp(1 << n, -1);
	    dp[0] = 0;
	    
	    for (int mask = 0; mask < (1 << n); mask++) {
	        if (dp[mask] == -1) {
	            continue;
	        }
	        
	        for (int i = 0; i < n; i++) {
	            if (!(mask & (1 << i))) {
	                if (dp[mask] + nums[i] <= target) {
	                    int newMask = mask | (1 << i);
	                    int newSum = (dp[mask] + nums[i]) % target;
	                    if (dp[newMask] == -1 || newSum < dp[newMask]) {
	                        dp[newMask] = newSum;
	                    }
	                }
	            }
	        }
	    }
	    
	    return dp[(1 << n) - 1] == 0;
	}
	*/
	
	// 补充题目2: 划分为k个相等的子集解法 - Python代码
	/*
	def canPartitionKSubsets(nums, k):
	    total_sum = sum(nums)
	    
	    if total_sum % k != 0:
	        return False
	    
	    target = total_sum // k
	    n = len(nums)
	    
	    # 排序，从大到小
	    nums.sort(reverse=True)
	    
	    if nums[0] > target:
	        return False
	    
	    # 初始化dp数组，-1表示不可达
	    dp = [-1] * (1 << n)
	    dp[0] = 0
	    
	    for mask in range(1 << n):
	        if dp[mask] == -1:
	            continue
	        
	        for i in range(n):
	            if not (mask & (1 << i)):
	                if dp[mask] + nums[i] <= target:
	                    new_mask = mask | (1 << i)
	                    new_sum = (dp[mask] + nums[i]) % target
	                    if dp[new_mask] == -1 or new_sum < dp[new_mask]:
	                        dp[new_mask] = new_sum
	    
	    return dp[(1 << n) - 1] == 0
	*/
	
	// 辅助函数：反转数组
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
	
	// 补充题目3: 火柴拼正方形解法
	public static boolean makesquare(int[] matchsticks) {
		return canPartitionKSubsets(matchsticks, 4);
	}
	
	// 补充题目3: 火柴拼正方形解法 - C++代码
	/*
	bool makesquare(vector<int>& matchsticks) {
	    return canPartitionKSubsets(matchsticks, 4);
	}
	*/
	
	// 补充题目3: 火柴拼正方形解法 - Python代码
	/*
	def makesquare(matchsticks):
	    return canPartitionKSubsets(matchsticks, 4)
	*/
	
	// 补充题目4: 参加考试的最大学生数解法
	public static int maxStudents(char[][] seats) {
		int m = seats.length;
		if (m == 0) return 0;
		int n = seats[0].length;
		
		// 预处理每行可用座位的状态
		int[] available = new int[m];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (seats[i][j] == '.') {
					available[i] |= 1 << j;
				}
			}
		}
		
		// dp[mask] 表示当前行座位分布为mask时的最大学生数
		int[] dp = new int[1 << n];
		Arrays.fill(dp, -1);
		dp[0] = 0; // 初始状态，第0行之前没有学生
		
		// 逐行处理
		for (int i = 0; i < m; i++) {
			int[] newDp = new int[1 << n];
			Arrays.fill(newDp, -1);
			
			// 枚举当前行的可能状态
			for (int mask = 0; mask < (1 << n); mask++) {
				// 检查mask是否是available[i]的子集（即只在可用座位上安排学生）
				if ((mask & available[i]) != mask) {
					continue;
				}
				
				// 检查当前行内部是否有相邻的学生
				if ((mask & (mask << 1)) != 0 || (mask & (mask >> 1)) != 0) {
					continue;
				}
				
				// 枚举前一行的状态
				for (int prev = 0; prev < (1 << n); prev++) {
					if (dp[prev] == -1) {
						continue;
					}
					
					// 检查当前行和前一行是否有冲突（左前和右前）
					if ((mask & (prev << 1)) != 0 || (mask & (prev >> 1)) != 0) {
						continue;
					}
					
					// 更新状态
					int studentCount = Integer.bitCount(mask);
					if (newDp[mask] < dp[prev] + studentCount) {
						newDp[mask] = dp[prev] + studentCount;
					}
				}
			}
			
			dp = newDp;
		}
		
		// 返回最后一行所有可能状态中的最大值
		int result = 0;
		for (int count : dp) {
			result = Math.max(result, count);
		}
		return result;
	}
	
	// 补充题目4: 参加考试的最大学生数解法 - C++代码
	/*
	int maxStudents(vector<vector<char>>& seats) {
	    int m = seats.size();
	    if (m == 0) return 0;
	    int n = seats[0].size();
	    
	    vector<int> available(m, 0);
	    for (int i = 0; i < m; i++) {
	        for (int j = 0; j < n; j++) {
	            if (seats[i][j] == '.') {
	                available[i] |= 1 << j;
	            }
	        }
	    }
	    
	    vector<int> dp(1 << n, -1);
	    dp[0] = 0;
	    
	    for (int i = 0; i < m; i++) {
	        vector<int> newDp(1 << n, -1);
	        
	        for (int mask = 0; mask < (1 << n); mask++) {
	            // 检查mask是否是available[i]的子集
	            if ((mask & available[i]) != mask) {
	                continue;
	            }
	            
	            // 检查当前行内部是否有相邻的学生
	            if ((mask & (mask << 1)) || (mask & (mask >> 1))) {
	                continue;
	            }
	            
	            for (int prev = 0; prev < (1 << n); prev++) {
	                if (dp[prev] == -1) {
	                    continue;
	                }
	                
	                // 检查当前行和前一行是否有冲突
	                if ((mask & (prev << 1)) || (mask & (prev >> 1))) {
	                    continue;
	                }
	                
	                int cnt = __builtin_popcount(mask);
	                newDp[mask] = max(newDp[mask], dp[prev] + cnt);
	            }
	        }
	        
	        dp = move(newDp);
	    }
	    
	    int result = 0;
	    for (int count : dp) {
	        result = max(result, count);
	    }
	    return result;
	}
	*/
	
	// 补充题目4: 参加考试的最大学生数解法 - Python代码
	/*
	def maxStudents(seats):
	    m = len(seats)
	    if m == 0:
	        return 0
	    n = len(seats[0])
	    
	    available = [0] * m
	    for i in range(m):
	        for j in range(n):
	            if seats[i][j] == '.':
	                available[i] |= 1 << j
	    
	    dp = [-1] * (1 << n)
	    dp[0] = 0
	    
	    for i in range(m):
	        new_dp = [-1] * (1 << n)
	        
	        for mask in range(1 << n):
	            # 检查mask是否是available[i]的子集
	            if (mask & available[i]) != mask:
	                continue
	            
	            # 检查当前行内部是否有相邻的学生
	            if (mask & (mask << 1)) or (mask & (mask >> 1)):
	                continue
	            
	            for prev in range(1 << n):
	                if dp[prev] == -1:
	                    continue
	                
	                # 检查当前行和前一行是否有冲突
	                if (mask & (prev << 1)) or (mask & (prev >> 1)):
	                    continue
	                
	                cnt = bin(mask).count('1')
	                if new_dp[mask] < dp[prev] + cnt:
	                    new_dp[mask] = dp[prev] + cnt
	        
	        dp = new_dp
	    
	    return max(dp)
	*/
}