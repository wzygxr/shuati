package class081;

import java.util.Arrays;

// 玉米田 (Corn Fields)
// 题目来源: POJ 3254 Corn Fields
// 题目链接: http://poj.org/problem?id=3254
// 题目描述:
// Farmer John 放牧cow，有些草地上的草是不能吃的，用0表示，然后规定两头牛不能相邻放牧。
// 问题要求计算在给定的网格中，有多少种合法的放牧方案。
//
// 解题思路:
// 这是一道经典的状态压缩DP问题。我们可以按行进行状态压缩，用二进制位表示每一行的放牧状态。
// 对于每一行，我们需要考虑：
// 1. 当前行的地形是否允许在某个位置放牧（草地为1，不能放牧的为0）
// 2. 当前行的放牧状态是否合法（相邻位置不能同时放牧）
// 3. 当前行与前一行的放牧状态是否冲突（上下相邻位置不能同时放牧）
//
// 状态定义:
// dp[i][mask] 表示处理到第i行，且第i行的放牧状态为mask时的方案数
//
// 状态转移:
// 对于每一行，我们枚举所有可能的合法状态，然后检查与前一行是否冲突
//
// 时间复杂度: O(m * 2^(2*n)) 其中m是行数，n是列数
// 空间复杂度: O(2^n)
//
// 补充题目1: 格雷编码 (Gray Code)
// 题目来源: LeetCode 89. Gray Code
// 题目链接: https://leetcode.cn/problems/gray-code/
// 题目描述:
// 格雷编码是一个二进制数字系统，在该系统中，两个连续的数值仅有一个位数的差异。
// 给定一个代表编码总位数的非负整数n，打印其格雷编码序列。
// 解题思路:
// 1. 观察格雷编码的生成规律
// 2. 递推关系: G(i) = i XOR (i >> 1)
// 3. 也可以使用动态规划方法，从n-1位的格雷编码生成n位的格雷编码
// 时间复杂度: O(2^n)
// 空间复杂度: O(1)

// 补充题目2: 优美的排列 (Beautiful Arrangement)
// 题目来源: LeetCode 526. Beautiful Arrangement
// 题目链接: https://leetcode.cn/problems/beautiful-arrangement/
// 题目描述:
// 假设有从 1 到 n 的 n 个整数。
// 用这些整数构造一个数组 perm（下标从 1 开始），只要满足下述条件 之一 ，该数组就是一个 优美的排列 ：
// perm[i] 能够被 i 整除
// i 能够被 perm[i] 整除
// 给你一个整数 n ，返回可以构造的 优美排列 的 数量 。
// 解题思路:
// 1. 使用状态压缩DP解决排列问题
// 2. 用二进制位表示数字的使用状态，第i位为1表示数字(i+1)已被使用
// 3. dp[mask] 表示使用mask代表的数字集合能构造的优美排列数量
// 4. 枚举所有可能的状态，对于每个状态尝试填充每一个未使用的数字到下一个位置
// 时间复杂度: O(n * 2^n)
// 空间复杂度: O(2^n)

// 补充题目3: 划分为k个相等的子集 (Partition to K Equal Sum Subsets)
// 题目来源: LeetCode 698. Partition to K Equal Sum Subsets
// 题目链接: https://leetcode.cn/problems/partition-to-k-equal-sum-subsets/
// 题目描述:
// 给定一个整数数组 nums 和一个正整数 k，找出是否有可能把这个数组分成 k 个非空子集，其总和都相等。
// 解题思路:
// 1. 先判断总和是否能被k整除，如果不能直接返回false
// 2. 使用状态压缩DP，dp[mask]表示当前分组情况，mask的第i位为1表示nums[i]已被使用
// 3. 用memo数组记录当前分组的剩余容量
// 4. 回溯+记忆化搜索，对每个可能的数尝试放入当前子集或开始新的子集
// 时间复杂度: O(n * 2^n)
// 空间复杂度: O(2^n)

// 补充题目4: 数字1的个数 (Number of Digit One)
// 题目来源: LeetCode 233. Number of Digit One
// 题目链接: https://leetcode.cn/problems/number-of-digit-one/
// 题目描述:
// 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。
// 解题思路:
// 1. 逐位分析数字1出现的次数
// 2. 对于每一位digit，计算其左边部分high、当前位current和右边部分low
// 3. 根据current的值分三种情况计算1的个数：
//    - current == 0: 贡献为high * 10^(digit-1)
//    - current == 1: 贡献为high * 10^(digit-1) + low + 1
//    - current > 1: 贡献为(high + 1) * 10^(digit-1)
// 时间复杂度: O(log n)
// 空间复杂度: O(1)

/*
 * C++版本实现:
 * 
 * class Solution {
 * public:
 *     int countArrangement(int n) {
 *         // dp[mask] 表示使用mask代表的数字集合能构造的优美排列数量
 *         // mask的第i位为1表示数字(i+1)已被使用
 *         std::vector<int> dp(1 << n, 0);
 *         dp[0] = 1; // 空集合能构造1种排列（空排列）
 *         
 *         // 枚举所有可能的状态
 *         for (int mask = 0; mask < (1 << n); mask++) {
 *             // 如果当前状态无法构造优美排列，跳过
 *             if (dp[mask] == 0) continue;
 *             
 *             // 计算当前已使用的数字个数，即下一个要填充的位置
 *             int pos = __builtin_popcount(mask) + 1;
 *             
 *             // 尝试填充每一个未使用的数字到位置pos
 *             for (int i = 0; i < n; i++) {
 *                 // 如果数字(i+1)未被使用，且满足优美排列条件
 *                 if ((mask & (1 << i)) == 0 && ((i + 1) % pos == 0 || pos % (i + 1) == 0)) {
 *                     // 更新新状态的方案数
 *                     dp[mask | (1 << i)] += dp[mask];
 *                 }
 *             }
 *         }
 *         
 *         // 返回使用所有数字的方案数
 *         return dp[(1 << n) - 1];
 *     }
 * };
 */

/*
 * Python版本实现:
 * 
 * class Solution:
 *     def countArrangement(self, n: int) -> int:
 *         # dp[mask] 表示使用mask代表的数字集合能构造的优美排列数量
 *         # mask的第i位为1表示数字(i+1)已被使用
 *         dp = [0] * (1 << n)
 *         dp[0] = 1  # 空集合能构造1种排列（空排列）
 *         
 *         # 枚举所有可能的状态
 *         for mask in range(1 << n):
 *             # 如果当前状态无法构造优美排列，跳过
 *             if dp[mask] == 0:
 *                 continue
 *             
 *             # 计算当前已使用的数字个数，即下一个要填充的位置
 *             pos = bin(mask).count('1') + 1
 *             
 *             // 尝试填充每一个未使用的数字到位置pos
 *             for i in range(n):
 *                 // 如果数字(i+1)未被使用，且满足优美排列条件
 *                 if (mask & (1 << i)) == 0 and ((i + 1) % pos == 0 or pos % (i + 1) == 0):
 *                     // 更新新状态的方案数
 *                     dp[mask | (1 << i)] += dp[mask]
 *         
 *         // 返回使用所有数字的方案数
 *         return dp[(1 << n) - 1]
 */

public class Code05_CornFields {
    public static final int MOD = 1000000007;

    // POJ 3254 Corn Fields 解法
    public static int cornFields(int m, int n, int[][] grid) {
        // 预处理每一行的合法状态
        int[] validStates = new int[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    validStates[i] |= (1 << j);
                }
            }
        }

        // 预处理所有可能的行状态（不考虑地形，只考虑相邻位置不冲突）
        int[] allStates = new int[1 << n];
        int stateCount = 0;
        for (int i = 0; i < (1 << n); i++) {
            // 检查相邻位置是否冲突
            if ((i & (i << 1)) == 0 && (i & (i >> 1)) == 0) {
                allStates[stateCount++] = i;
            }
        }

        // dp[i][mask] 表示处理到第i行，且第i行的放牧状态为mask时的方案数
        int[][] dp = new int[m + 1][1 << n];
        dp[0][0] = 1;

        for (int i = 1; i <= m; i++) {
            for (int j = 0; j < stateCount; j++) {
                int state = allStates[j];
                // 检查当前状态是否在当前行的合法地形内
                if ((state & validStates[i - 1]) != state) {
                    continue;
                }

                // 检查与前一行是否冲突
                for (int k = 0; k < (1 << n); k++) {
                    if ((state & k) == 0) { // 上下不相邻
                        dp[i][state] = (dp[i][state] + dp[i - 1][k]) % MOD;
                    }
                }
            }
        }

        // 计算最终结果
        int result = 0;
        for (int i = 0; i < (1 << n); i++) {
            result = (result + dp[m][i]) % MOD;
        }
        return result;
    }

    // 空间优化版本
    public static int cornFieldsOptimized(int m, int n, int[][] grid) {
        // 预处理每一行的合法状态
        int[] validStates = new int[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    validStates[i] |= (1 << j);
                }
            }
        }

        // 预处理所有可能的行状态（不考虑地形，只考虑相邻位置不冲突）
        int[] allStates = new int[1 << n];
        int stateCount = 0;
        for (int i = 0; i < (1 << n); i++) {
            // 检查相邻位置是否冲突
            if ((i & (i << 1)) == 0 && (i & (i >> 1)) == 0) {
                allStates[stateCount++] = i;
            }
        }

        // 空间优化的DP数组
        int[] dp = new int[1 << n];
        int[] nextDp = new int[1 << n];
        dp[0] = 1;

        for (int i = 1; i <= m; i++) {
            Arrays.fill(nextDp, 0);
            for (int j = 0; j < stateCount; j++) {
                int state = allStates[j];
                // 检查当前状态是否在当前行的合法地形内
                if ((state & validStates[i - 1]) != state) {
                    continue;
                }

                // 检查与前一行是否冲突
                for (int k = 0; k < (1 << n); k++) {
                    if ((state & k) == 0) { // 上下不相邻
                        nextDp[state] = (nextDp[state] + dp[k]) % MOD;
                    }
                }
            }
            // 交换dp数组
            int[] temp = dp;
            dp = nextDp;
            nextDp = temp;
        }

        // 计算最终结果
        int result = 0;
        for (int i = 0; i < (1 << n); i++) {
            result = (result + dp[i]) % MOD;
        }
        return result;
    }

    // LeetCode 89. Gray Code 解法
    public static int[] grayCode(int n) {
        int[] result = new int[1 << n];
        for (int i = 0; i < (1 << n); i++) {
            result[i] = i ^ (i >> 1);
        }
        return result;
    }

    // 动态规划方法生成格雷编码
    public static int[] grayCodeDP(int n) {
        if (n == 0) {
            return new int[]{0};
        }

        // dp[i] 表示i位格雷编码序列
        int[][] dp = new int[n + 1][];
        dp[0] = new int[]{0};

        for (int i = 1; i <= n; i++) {
            int len = 1 << i;
            dp[i] = new int[len];
            // 前半部分是i-1位的格雷编码
            for (int j = 0; j < (1 << (i - 1)); j++) {
                dp[i][j] = dp[i - 1][j];
            }
            // 后半部分是i-1位的格雷编码逆序，再加上2^(i-1)
            for (int j = 0; j < (1 << (i - 1)); j++) {
                dp[i][(1 << i) - 1 - j] = dp[i - 1][j] | (1 << (i - 1));
            }
        }

        return dp[n];
    }
    
    // LeetCode 526. 优美的排列 解法
    public static int countArrangement(int n) {
        // dp[mask] 表示使用mask代表的数字集合能构造的优美排列数量
        // mask的第i位为1表示数字(i+1)已被使用
        int[] dp = new int[1 << n];
        dp[0] = 1; // 空集合能构造1种排列（空排列）
        
        // 枚举所有可能的状态
        for (int mask = 0; mask < (1 << n); mask++) {
            // 如果当前状态无法构造优美排列，跳过
            if (dp[mask] == 0) continue;
            
            // 计算当前已使用的数字个数，即下一个要填充的位置
            int pos = Integer.bitCount(mask) + 1;
            
            // 尝试填充每一个未使用的数字到位置pos
            for (int i = 0; i < n; i++) {
                // 如果数字(i+1)未被使用，且满足优美排列条件
                if ((mask & (1 << i)) == 0 && ((i + 1) % pos == 0 || pos % (i + 1) == 0)) {
                    // 更新新状态的方案数
                    dp[mask | (1 << i)] += dp[mask];
                }
            }
        }
        
        // 返回使用所有数字的方案数
        return dp[(1 << n) - 1];
    }
    
    // LeetCode 698. 划分为k个相等的子集 解法
    public static boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // 如果总和不能被k整除，直接返回false
        if (sum % k != 0) {
            return false;
        }
        int target = sum / k;
        
        // 排序，从大到小尝试
        Arrays.sort(nums);
        reverse(nums);
        
        // 剪枝：如果最大的数大于目标和，无法分割
        if (nums[0] > target) {
            return false;
        }
        
        // 使用状态压缩DP
        boolean[] dp = new boolean[1 << nums.length];
        int[] memo = new int[1 << nums.length];
        Arrays.fill(memo, -1);
        dp[0] = true;
        
        return backtrack(nums, dp, memo, 0, target, 0, k);
    }
    
    private static boolean backtrack(int[] nums, boolean[] dp, int[] memo, int mask, int target, int currentSum, int k) {
        // 如果已经使用了所有数字，且k个子集都分配完毕
        if (mask == (1 << nums.length) - 1) {
            return true;
        }
        
        // 尝试每个未使用的数字
        for (int i = 0; i < nums.length; i++) {
            // 如果当前数字已经使用过，跳过
            if ((mask & (1 << i)) != 0) {
                continue;
            }
            
            // 计算当前子集的新和
            int newSum = currentSum + nums[i];
            
            // 如果新和超过目标和，跳过
            if (newSum > target) {
                continue;
            }
            
            // 标记当前数字为已使用
            int newMask = mask | (1 << i);
            
            // 重置当前子集的和（开始新的子集）
            int nextSum = (newSum == target) ? 0 : newSum;
            
            // 如果新状态有效，继续回溯
            if (dp[newMask] || backtrack(nums, dp, memo, newMask, target, nextSum, k)) {
                dp[newMask] = true;
                return true;
            }
            
            // 剪枝：如果当前子集和为0，且当前数字无法使用，说明这个数字在任何位置都无法使用
            if (currentSum == 0) {
                return false;
            }
            
            // 剪枝：跳过相同的数字
            while (i + 1 < nums.length && nums[i] == nums[i + 1]) {
                i++;
            }
        }
        
        return false;
    }
    
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
    
    // LeetCode 233. 数字1的个数 解法
    public static int countDigitOne(int n) {
        if (n <= 0) {
            return 0;
        }
        
        int count = 0;
        long digit = 1; // 当前处理的位（个位、十位、百位...）
        
        // 逐位分析数字1出现的次数
        while (digit <= n) {
            long high = n / (digit * 10); // 高位数字
            long current = (n / digit) % 10; // 当前位数字
            long low = n % digit; // 低位数字
            
            // 根据当前位的值分情况处理
            if (current == 0) {
                // 当前位为0，贡献为high * digit
                count += high * digit;
            } else if (current == 1) {
                // 当前位为1，贡献为high * digit + low + 1
                count += high * digit + low + 1;
            } else {
                // 当前位大于1，贡献为(high + 1) * digit
                count += (high + 1) * digit;
            }
            
            digit *= 10;
        }
        
        return count;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试 POJ 3254 Corn Fields
        int[][] grid1 = {
            {1, 1, 1},
            {0, 1, 0},
            {1, 1, 1}
        };
        System.out.println("POJ 3254 Corn Fields 测试:");
        System.out.println("结果: " + cornFields(3, 3, grid1));
        System.out.println("优化版结果: " + cornFieldsOptimized(3, 3, grid1));

        // 测试 LeetCode 89. Gray Code
        System.out.println("\nLeetCode 89. Gray Code 测试:");
        int[] gray1 = grayCode(2);
        System.out.print("n=2: ");
        for (int num : gray1) {
            System.out.print(num + " ");
        }
        System.out.println();

        int[] gray2 = grayCodeDP(3);
        System.out.print("n=3: ");
        for (int num : gray2) {
            System.out.print(num + " ");
        }
        System.out.println();
        
        // 测试 LeetCode 526. 优美的排列
        System.out.println("\nLeetCode 526. 优美的排列 测试:");
        System.out.println("n=2: " + countArrangement(2)); // 期望输出: 2
        System.out.println("n=1: " + countArrangement(1)); // 期望输出: 1
        System.out.println("n=3: " + countArrangement(3)); // 期望输出: 3
        
        // 测试 LeetCode 698. 划分为k个相等的子集
        System.out.println("\nLeetCode 698. 划分为k个相等的子集 测试:");
        int[] nums1 = {4, 3, 2, 3, 5, 2, 1};
        System.out.println("[4,3,2,3,5,2,1], k=4: " + canPartitionKSubsets(nums1, 4)); // 期望输出: true
        
        int[] nums2 = {1, 2, 3, 4};
        System.out.println("[1,2,3,4], k=3: " + canPartitionKSubsets(nums2, 3)); // 期望输出: false
        
        // 测试 LeetCode 233. 数字1的个数
        System.out.println("\nLeetCode 233. 数字1的个数 测试:");
        System.out.println("n=13: " + countDigitOne(13)); // 期望输出: 6
        System.out.println("n=0: " + countDigitOne(0)); // 期望输出: 0
        System.out.println("n=100: " + countDigitOne(100)); // 期望输出: 21
    }
    
    /*
     * C++版本完整实现
     */
    /*
    // 玉米田 (Corn Fields) 解法
    #include <iostream>
    #include <vector>
    #include <algorithm>
    using namespace std;
    
    const int MOD = 1000000007;
    
    int cornFields(int m, int n, vector<vector<int>>& grid) {
        vector<int> validStates(m);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    validStates[i] |= (1 << j);
                }
            }
        }
        
        vector<int> allStates;
        for (int i = 0; i < (1 << n); i++) {
            if ((i & (i << 1)) == 0 && (i & (i >> 1)) == 0) {
                allStates.push_back(i);
            }
        }
        
        vector<vector<int>> dp(m + 1, vector<int>(1 << n, 0));
        dp[0][0] = 1;
        
        for (int i = 1; i <= m; i++) {
            for (int state : allStates) {
                if ((state & validStates[i - 1]) != state) {
                    continue;
                }
                for (int k = 0; k < (1 << n); k++) {
                    if ((state & k) == 0) {
                        dp[i][state] = (dp[i][state] + dp[i - 1][k]) % MOD;
                    }
                }
            }
        }
        
        int result = 0;
        for (int i = 0; i < (1 << n); i++) {
            result = (result + dp[m][i]) % MOD;
        }
        return result;
    }
    
    // 空间优化版本
    int cornFieldsOptimized(int m, int n, vector<vector<int>>& grid) {
        vector<int> validStates(m);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    validStates[i] |= (1 << j);
                }
            }
        }
        
        vector<int> allStates;
        for (int i = 0; i < (1 << n); i++) {
            if ((i & (i << 1)) == 0 && (i & (i >> 1)) == 0) {
                allStates.push_back(i);
            }
        }
        
        vector<int> dp(1 << n, 0);
        vector<int> nextDp(1 << n, 0);
        dp[0] = 1;
        
        for (int i = 1; i <= m; i++) {
            fill(nextDp.begin(), nextDp.end(), 0);
            for (int state : allStates) {
                if ((state & validStates[i - 1]) != state) {
                    continue;
                }
                for (int k = 0; k < (1 << n); k++) {
                    if ((state & k) == 0) {
                        nextDp[state] = (nextDp[state] + dp[k]) % MOD;
                    }
                }
            }
            swap(dp, nextDp);
        }
        
        int result = 0;
        for (int i = 0; i < (1 << n); i++) {
            result = (result + dp[i]) % MOD;
        }
        return result;
    }
    
    // 格雷编码 (Gray Code) 解法
    vector<int> grayCode(int n) {
        vector<int> result(1 << n);
        for (int i = 0; i < (1 << n); i++) {
            result[i] = i ^ (i >> 1);
        }
        return result;
    }
    
    vector<int> grayCodeDP(int n) {
        if (n == 0) {
            return {0};
        }
        
        vector<vector<int>> dp(n + 1);
        dp[0] = {0};
        
        for (int i = 1; i <= n; i++) {
            int len = 1 << i;
            dp[i].resize(len);
            int prevLen = 1 << (i - 1);
            for (int j = 0; j < prevLen; j++) {
                dp[i][j] = dp[i - 1][j];
            }
            for (int j = 0; j < prevLen; j++) {
                dp[i][len - 1 - j] = dp[i - 1][j] | (1 << (i - 1));
            }
        }
        
        return dp[n];
    }
    
    // 优美的排列 (Beautiful Arrangement) 解法
    int countArrangement(int n) {
        vector<int> dp(1 << n, 0);
        dp[0] = 1;
        
        for (int mask = 0; mask < (1 << n); mask++) {
            if (dp[mask] == 0) continue;
            
            int pos = __builtin_popcount(mask) + 1;
            
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0 && ((i + 1) % pos == 0 || pos % (i + 1) == 0)) {
                    dp[mask | (1 << i)] += dp[mask];
                }
            }
        }
        
        return dp[(1 << n) - 1];
    }
    
    // 划分为k个相等的子集 (Partition to K Equal Sum Subsets) 解法
    bool canPartitionKSubsets(vector<int>& nums, int k) {
        int sum = 0;
        for (int num : nums) sum += num;
        if (sum % k != 0) return false;
        int target = sum / k;
        
        sort(nums.rbegin(), nums.rend());
        if (nums[0] > target) return false;
        
        vector<bool> dp(1 << nums.size(), false);
        vector<int> memo(1 << nums.size(), -1);
        dp[0] = true;
        
        function<bool(int, int)> backtrack = [&](int mask, int currentSum) {
            if (mask == (1 << nums.size()) - 1) {
                return true;
            }
            
            for (int i = 0; i < nums.size(); i++) {
                if ((mask & (1 << i)) != 0) continue;
                
                int newSum = currentSum + nums[i];
                if (newSum > target) continue;
                
                int newMask = mask | (1 << i);
                int nextSum = (newSum == target) ? 0 : newSum;
                
                if (dp[newMask] || backtrack(newMask, nextSum)) {
                    dp[newMask] = true;
                    return true;
                }
                
                if (currentSum == 0) return false;
                
                while (i + 1 < nums.size() && nums[i] == nums[i + 1]) i++;
            }
            
            return false;
        };
        
        return backtrack(0, 0);
    }
    
    // 数字1的个数 (Number of Digit One) 解法
    int countDigitOne(int n) {
        if (n <= 0) return 0;
        
        int count = 0;
        long digit = 1;
        
        while (digit <= n) {
            long high = n / (digit * 10);
            long current = (n / digit) % 10;
            long low = n % digit;
            
            if (current == 0) {
                count += high * digit;
            } else if (current == 1) {
                count += high * digit + low + 1;
            } else {
                count += (high + 1) * digit;
            }
            
            digit *= 10;
        }
        
        return count;
    }
    
    int main() {
        // 测试 Corn Fields
        vector<vector<int>> grid = {
            {1, 1, 1},
            {0, 1, 0},
            {1, 1, 1}
        };
        cout << "Corn Fields: " << cornFields(3, 3, grid) << endl;
        
        // 测试 Gray Code
        vector<int> gray = grayCode(3);
        cout << "Gray Code: ";
        for (int num : gray) cout << num << " ";
        cout << endl;
        
        // 测试 Beautiful Arrangement
        cout << "Beautiful Arrangement (n=3): " << countArrangement(3) << endl;
        
        // 测试 Partition to K Equal Sum Subsets
        vector<int> nums = {4, 3, 2, 3, 5, 2, 1};
        cout << "Partition to K Equal Sum Subsets: " << canPartitionKSubsets(nums, 4) << endl;
        
        // 测试 Number of Digit One
        cout << "Number of Digit One (n=13): " << countDigitOne(13) << endl;
        
        return 0;
    }
    */
    
    /*
     * Python版本完整实现
     */
    /*
    # 玉米田 (Corn Fields) 解法
    MOD = 10**9 + 7
    
    def cornFields(m, n, grid):
        validStates = [0] * m
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 1:
                    validStates[i] |= (1 << j)
        
        allStates = []
        for i in range(1 << n):
            if (i & (i << 1)) == 0 and (i & (i >> 1)) == 0:
                allStates.append(i)
        
        dp = [[0] * (1 << n) for _ in range(m + 1)]
        dp[0][0] = 1
        
        for i in range(1, m + 1):
            for state in allStates:
                if (state & validStates[i - 1]) != state:
                    continue
                for k in range(1 << n):
                    if (state & k) == 0:
                        dp[i][state] = (dp[i][state] + dp[i - 1][k]) % MOD
        
        return sum(dp[m]) % MOD
    
    # 空间优化版本
    def cornFieldsOptimized(m, n, grid):
        validStates = [0] * m
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 1:
                    validStates[i] |= (1 << j)
        
        allStates = []
        for i in range(1 << n):
            if (i & (i << 1)) == 0 and (i & (i >> 1)) == 0:
                allStates.append(i)
        
        dp = [0] * (1 << n)
        dp[0] = 1
        
        for i in range(1, m + 1):
            nextDp = [0] * (1 << n)
            for state in allStates:
                if (state & validStates[i - 1]) != state:
                    continue
                for k in range(1 << n):
                    if (state & k) == 0:
                        nextDp[state] = (nextDp[state] + dp[k]) % MOD
            dp = nextDp
        
        return sum(dp) % MOD
    
    # 格雷编码 (Gray Code) 解法
    def grayCode(n):
        result = [0] * (1 << n)
        for i in range(1 << n):
            result[i] = i ^ (i >> 1)
        return result
    
    def grayCodeDP(n):
        if n == 0:
            return [0]
        
        dp = [[0] * (1 << i) for i in range(n + 1)]
        dp[0][0] = 0
        
        for i in range(1, n + 1):
            prevLen = 1 << (i - 1)
            for j in range(prevLen):
                dp[i][j] = dp[i-1][j]
            for j in range(prevLen):
                dp[i][(1 << i) - 1 - j] = dp[i-1][j] | (1 << (i - 1))
        
        return dp[n]
    
    # 优美的排列 (Beautiful Arrangement) 解法
    def countArrangement(n):
        dp = [0] * (1 << n)
        dp[0] = 1
        
        for mask in range(1 << n):
            if dp[mask] == 0:
                continue
            
            pos = bin(mask).count('1') + 1
            
            for i in range(n):
                if (mask & (1 << i)) == 0 and ((i + 1) % pos == 0 or pos % (i + 1) == 0):
                    dp[mask | (1 << i)] += dp[mask]
        
        return dp[(1 << n) - 1]
    
    # 划分为k个相等的子集 (Partition to K Equal Sum Subsets) 解法
    def canPartitionKSubsets(nums, k):
        total_sum = sum(nums)
        if total_sum % k != 0:
            return False
        target = total_sum // k
        
        nums.sort(reverse=True)
        if nums[0] > target:
            return False
        
        n = len(nums)
        dp = [False] * (1 << n)
        dp[0] = True
        
        def backtrack(mask, current_sum):
            if mask == (1 << n) - 1:
                return True
            
            for i in range(n):
                if (mask & (1 << i)) != 0:
                    continue
                
                new_sum = current_sum + nums[i]
                if new_sum > target:
                    continue
                
                new_mask = mask | (1 << i)
                next_sum = 0 if new_sum == target else new_sum
                
                if dp[new_mask] or backtrack(new_mask, next_sum):
                    dp[new_mask] = True
                    return True
                
                if current_sum == 0:
                    return False
                
                while i + 1 < n and nums[i] == nums[i + 1]:
                    i += 1
            
            return False
        
        return backtrack(0, 0)
    
    # 数字1的个数 (Number of Digit One) 解法
    def countDigitOne(n):
        if n <= 0:
            return 0
        
        count = 0
        digit = 1
        
        while digit <= n:
            high = n // (digit * 10)
            current = (n // digit) % 10
            low = n % digit
            
            if current == 0:
                count += high * digit
            elif current == 1:
                count += high * digit + low + 1
            else:
                count += (high + 1) * digit
            
            digit *= 10
        
        return count
    
    # 测试代码
    if __name__ == "__main__":
        # 测试 Corn Fields
        grid = [
            [1, 1, 1],
            [0, 1, 0],
            [1, 1, 1]
        ]
        print(f"Corn Fields: {cornFields(3, 3, grid)}")
        
        # 测试 Gray Code
        print(f"Gray Code (n=3): {grayCode(3)}")
        
        # 测试 Beautiful Arrangement
        print(f"Beautiful Arrangement (n=3): {countArrangement(3)}")
        
        # 测试 Partition to K Equal Sum Subsets
        nums = [4, 3, 2, 3, 5, 2, 1]
        print(f"Partition to K Equal Sum Subsets: {canPartitionKSubsets(nums, 4)}")
        
        # 测试 Number of Digit One
        print(f"Number of Digit One (n=13): {countDigitOne(13)}")
    */
}