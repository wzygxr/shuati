package class081;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// 最短超级串 (Shortest Superstring)
// 题目来源: LeetCode 943. Find the Shortest Superstring
// 题目链接: https://leetcode.cn/problems/find-the-shortest-superstring/
// 题目描述:
// 给定一个字符串数组 words ，找到以 words 中每个字符串作为子字符串的最短字符串。
// 如果有多个有效最短字符串满足题目条件，返回其中 任意一个 即可。
// 我们可以假设 words 中没有字符串是 words 中另一个字符串的子字符串。
//
// 解题思路:
// 这是一个经典的旅行商问题(TSP)变种，可以使用状态压缩DP解决。
// 1. 预处理计算重叠部分 overlap[i][j] 表示字符串words[i]的尾部与字符串words[j]的头部的最大重叠长度
// 2. dp[mask][i] 表示使用mask代表的字符串集合，且最后一个字符串是words[i]时的最短超级字符串
// 3. 状态转移: 对于每个状态，尝试添加一个新的字符串
//
// 时间复杂度: O(n^2 * 2^n + n * sum(len))
// 空间复杂度: O(n * 2^n)
// 其中n是字符串的数量，sum(len)是所有字符串长度之和
//
// 补充题目1: 最小必要团队 (Smallest Sufficient Team)
// 题目来源: LeetCode 1125. Smallest Sufficient Team
// 题目链接: https://leetcode.cn/problems/smallest-sufficient-team/
// 题目描述:
// 给定一个人数组和一个技能需求列表，找出最小的团队使得团队成员掌握的技能能够覆盖所有需求技能。
// 解题思路:
// 1. 状态压缩动态规划解法
// 2. 建立技能到索引的映射，便于位运算
// 3. 将每个人掌握的技能转换为位掩码表示
// 4. dp[mask] 表示覆盖技能集合mask所需的最小团队，使用List存储团队成员索引
// 时间复杂度: O(2^m * n) 其中m是需求技能数，n是人员数
// 空间复杂度: O(2^m)

// 补充题目2: 火柴拼正方形 (Matchsticks to Square)
// 题目来源: LeetCode 473. Matchsticks to Square
// 题目链接: https://leetcode.cn/problems/matchsticks-to-square/
// 题目描述:
// 还记得童话《卖火柴的小女孩》吗？现在，你知道小女孩有多少根火柴，请找出一种能使用所有火柴拼成一个正方形的方法。
// 不能折断火柴，可以把它们连接起来，每根火柴都必须用到。
// 给定一个整数数组 matchsticks ，其中 matchsticks[i] 是第 i 根火柴的长度。
// 如果你能拼出正方形，则返回 true，否则返回 false。
// 解题思路:
// 1. 使用状态压缩DP解决划分问题
// 2. 先检查总长度是否能被4整除，如果不能则无法拼成正方形
// 3. 用二进制位表示火柴的使用状态，第i位为1表示第i根火柴已被使用
// 4. dp[status] 表示使用status状态的火柴能否解决当前边的构造问题
// 5. 递归尝试添加每根未使用的火柴，如果当前边构造完成则开始构造下一条边
// 时间复杂度: O(n * 2^n)
// 空间复杂度: O(2^n)

// 补充题目3: 划分为k个相等的子集 (Partition to K Equal Sum Subsets)
// 题目来源: LeetCode 698. Partition to K Equal Sum Subsets
// 题目链接: https://leetcode.cn/problems/partition-to-k-equal-sum-subsets/
// 题目描述:
// 给定一个整数数组 nums 和一个正整数 k，找出是否有可能把这个数组分成 k 个非空子集，其总和都相等。
// 解题思路:
// 1. 使用状态压缩DP解决集合划分问题
// 2. 先检查总和是否能被k整除，如果不能则无法划分
// 3. 用二进制位表示数字的使用状态，第i位为1表示第i个数字已被使用
// 4. dp[status] 表示使用status状态的数字能否解决当前子集的构造问题
// 5. 递归尝试添加每个未使用的数字，如果当前子集构造完成则开始构造下一个子集
// 时间复杂度: O(n * 2^n)
// 空间复杂度: O(2^n)

// 补充题目4: 参加考试的最大学生数 (Maximum Students Taking Exam)
// 题目来源: LeetCode 1349. Maximum Students Taking Exam
// 题目链接: https://leetcode.cn/problems/maximum-students-taking-exam/
// 题目描述:
// 给你一个 m * n 的矩阵表示教室的座位，其中的 '#' 表示坏座位，'.' 表示好座位。
// 要求安排学生考试，使得任何两个学生不能互相看见，也就是说：
// 1. 不能在同一行相邻的位置
// 2. 不能在不同行的斜对角位置
// 求最多能安排多少学生参加考试。
// 解题思路:
// 1. 使用状态压缩DP解决网格问题
// 2. 预处理每行的可用座位（好座位）
// 3. 对于每行，枚举所有可能的合法状态（只使用好座位且不相邻）
// 4. dp[mask] 表示当前行座位分布为mask时的最大学生数
// 5. 状态转移时，检查当前行和前一行是否有斜对角冲突
// 时间复杂度: O(m * 2^(2n)) 其中m是行数，n是列数
// 空间复杂度: O(2^n)

/*
 * C++版本实现:
 * 
 * class Solution {
 * public:
 *     bool canPartitionKSubsets(vector<int>& nums, int k) {
 *         int sum = 0;
 *         for (int num : nums) {
 *             sum += num;
 *         }
 *         if (sum % k != 0) {
 *             return false;
 *         }
 *         int n = nums.size();
 *         vector<int> dp(1 << n, 0);
 *         return dfs(nums, sum / k, (1 << n) - 1, 0, k, dp);
 *     }
 * 
 * private:
 *     // limit : 每个子集规定的和
 *     // status : 表示哪些数字还可以选
 *     // cur : 当前要解决的这个子集已经形成的和
 *     // rest : 一共还有几个子集没有解决
 *     // 返回 : 能否用光所有数字去解决剩下的所有子集
 *     bool dfs(vector<int>& nums, int limit, int status, int cur, int rest, vector<int>& dp) {
 *         if (rest == 0) {
 *             return status == 0;
 *         }
 *         if (dp[status] != 0) {
 *             return dp[status] == 1;
 *         }
 *         bool ans = false;
 *         for (int i = 0; i < nums.size(); i++) {
 *             // 考察每一个数字，只能使用状态为1的数字
 *             if ((status & (1 << i)) != 0 && cur + nums[i] <= limit) {
 *                 if (cur + nums[i] == limit) {
 *                     ans = dfs(nums, limit, status ^ (1 << i), 0, rest - 1, dp);
 *                 } else {
 *                     ans = dfs(nums, limit, status ^ (1 << i), cur + nums[i], rest, dp);
 *                 }
 *                 if (ans) {
 *                     break;
 *                 }
 *             }
 *         }
 *         dp[status] = ans ? 1 : -1;
 *         return ans;
 *     }
 * };
 */

/*
 * Python版本实现:
 * 
 * class Solution:
 *     def canPartitionKSubsets(self, nums: List[int], k: int) -> bool:
 *         total = sum(nums)
 *         if total % k != 0:
 *             return False
 *         
 *         target = total // k
 *         n = len(nums)
 *         dp = [0] * (1 << n)
 *         
 *         def dfs(status, current_sum, remaining_subsets):
 *             if remaining_subsets == 0:
 *                 return status == 0
 *             
 *             if dp[status] != 0:
 *             return dp[status] == 1
 *             
 *             result = False
 *             for i in range(n):
 *                 // 考察每一个数字，只能使用状态为1的数字
 *                 if (status & (1 << i)) != 0 and current_sum + nums[i] <= target:
 *                     if current_sum + nums[i] == target:
 *                         result = dfs(status ^ (1 << i), 0, remaining_subsets - 1)
 *                     else:
 *                         result = dfs(status ^ (1 << i), current_sum + nums[i], remaining_subsets)
 *                     
 *                     if result:
 *                         break
 *             
 *             dp[status] = 1 if result else -1
 *             return result
 *         
 *         return dfs((1 << n) - 1, 0, k)
 */

public class Code07_ShortestSuperstring {
    
    // LeetCode 943 最短超级串解法
    public static String shortestSuperstring(String[] words) {
        int n = words.length;
        
        // 预处理计算重叠部分
        int[][] overlap = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    overlap[i][j] = getOverlap(words[i], words[j]);
                }
            }
        }
        
        // dp[mask][i] 表示使用mask代表的字符串集合，且最后一个字符串是words[i]时的最短超级字符串长度
        int[][] dp = new int[1 << n][n];
        // parent[mask][i] 用于回溯路径，记录前一个字符串的索引
        int[][] parent = new int[1 << n][n];
        
        // 初始化
        for (int i = 0; i < (1 << n); i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        
        // 初始化：只包含一个字符串的情况
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = words[i].length();
        }
        
        // 状态转移
        for (int mask = 1; mask < (1 << n); mask++) {
            for (int last = 0; last < n; last++) {
                if ((mask & (1 << last)) == 0) {
                    continue;
                }
                
                if (dp[mask][last] == Integer.MAX_VALUE) {
                    continue;
                }
                
                for (int next = 0; next < n; next++) {
                    if ((mask & (1 << next)) != 0) {
                        continue;
                    }
                    
                    int newMask = mask | (1 << next);
                    int newLength = dp[mask][last] + words[next].length() - overlap[last][next];
                    
                    if (dp[newMask][next] > newLength) {
                        dp[newMask][next] = newLength;
                        parent[newMask][next] = last;
                    }
                }
            }
        }
        
        // 找到包含所有字符串的最短超级字符串
        int resultLength = Integer.MAX_VALUE;
        int lastWord = -1;
        for (int i = 0; i < n; i++) {
            if (dp[(1 << n) - 1][i] < resultLength) {
                resultLength = dp[(1 << n) - 1][i];
                lastWord = i;
            }
        }
        
        // 回溯构建结果字符串
        int mask = (1 << n) - 1;
        int[] path = new int[n];
        int idx = n - 1;
        while (mask > 0) {
            path[idx--] = lastWord;
            int prev = parent[mask][lastWord];
            mask ^= (1 << lastWord);
            lastWord = prev;
        }
        
        // 构建最终字符串
        StringBuilder result = new StringBuilder(words[path[0]]);
        for (int i = 1; i < n; i++) {
            int overlapLen = overlap[path[i - 1]][path[i]];
            result.append(words[path[i]].substring(overlapLen));
        }
        
        return result.toString();
    }
    
    // 计算字符串a的尾部与字符串b的头部的最大重叠长度
    private static int getOverlap(String a, String b) {
        // 重叠长度最大为两个字符串长度的较小值
        for (int i = Math.min(a.length(), b.length()); i >= 0; i--) {
            // 检查a的后i个字符是否与b的前i个字符相同
            if (a.substring(a.length() - i).equals(b.substring(0, i))) {
                return i;
            }
        }
        return 0;
    }
    
    // LeetCode 1125 最小必要团队解法
    public static int[] smallestSufficientTeam(String[] req_skills, String[][] people) {
        int m = req_skills.length;
        int n = people.length;
        
        // 建立技能到索引的映射，便于位运算
        Map<String, Integer> skillIndex = new HashMap<>();
        for (int i = 0; i < m; i++) {
            skillIndex.put(req_skills[i], i);
        }
        
        // 将每个人掌握的技能转换为位掩码表示
        int[] peopleSkills = new int[n];
        for (int i = 0; i < n; i++) {
            for (String skill : people[i]) {
                if (skillIndex.containsKey(skill)) {
                    peopleSkills[i] |= 1 << skillIndex.get(skill);
                }
            }
        }
        
        // dp[mask] 表示覆盖技能集合mask所需的最小团队大小
        int[] dp = new int[1 << m];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        
        // parent[mask] 用于回溯路径，记录选择的人员索引
        int[] parent = new int[1 << m];
        int[] prevState = new int[1 << m];
        
        // 遍历所有可能的技能组合状态
        for (int mask = 0; mask < (1 << m); mask++) {
            // 如果当前状态不可达，跳过
            if (dp[mask] == Integer.MAX_VALUE) {
                continue;
            }
            
            // 尝试添加每个人员
            for (int i = 0; i < n; i++) {
                // 添加人员i后的新技能集合
                int newMask = mask | peopleSkills[i];
                
                // 如果通过当前路径能得到更小的团队
                if (dp[newMask] > dp[mask] + 1) {
                    dp[newMask] = dp[mask] + 1;
                    parent[newMask] = i;
                    prevState[newMask] = mask;
                }
            }
        }
        
        // 回溯构建结果团队
        List<Integer> team = new ArrayList<>();
        int mask = (1 << m) - 1;
        while (mask > 0) {
            int person = parent[mask];
            team.add(person);
            mask = prevState[mask];
        }
        
        // 转换为数组返回
        return team.stream().mapToInt(Integer::intValue).toArray();
    }
    
    // LeetCode 473. 火柴拼正方形解法
    public static boolean makesquare(int[] matchsticks) {
        int sum = 0;
        for (int num : matchsticks) {
            sum += num;
        }
        if (sum % 4 != 0) {
            return false;
        }
        int n = matchsticks.length;
        int[] dp = new int[1 << n];
        return dfs(matchsticks, sum / 4, (1 << n) - 1, 0, 4, dp);
    }

    // limit : 每条边规定的长度
    // status : 表示哪些数字还可以选
    // cur : 当前要解决的这条边已经形成的长度
    // rest : 一共还有几条边没有解决
    // 返回 : 能否用光所有火柴去解决剩下的所有边
    // 因为调用子过程之前，一定保证每条边累加起来都不超过limit
    // 所以status是决定cur和rest的，关键可变参数只有status
    private static boolean dfs(int[] nums, int limit, int status, int cur, int rest, int[] dp) {
        if (rest == 0) {
            return status == 0;
        }
        if (dp[status] != 0) {
            return dp[status] == 1;
        }
        boolean ans = false;
        for (int i = 0; i < nums.length; i++) {
            // 考察每一根火柴，只能使用状态为1的火柴
            if ((status & (1 << i)) != 0 && cur + nums[i] <= limit) {
                if (cur + nums[i] == limit) {
                    ans = dfs(nums, limit, status ^ (1 << i), 0, rest - 1, dp);
                } else {
                    ans = dfs(nums, limit, status ^ (1 << i), cur + nums[i], rest, dp);
                }
                if (ans) {
                    break;
                }
            }
        }
        dp[status] = ans ? 1 : -1;
        return ans;
    }
    
    // LeetCode 698. 划分为k个相等的子集解法
    public static boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum % k != 0) {
            return false;
        }
        int n = nums.length;
        int[] dp = new int[1 << n];
        return dfsPartition(nums, sum / k, (1 << n) - 1, 0, k, dp);
    }

    // limit : 每个子集规定的和
    // status : 表示哪些数字还可以选
    // cur : 当前要解决的这个子集已经形成的和
    // rest : 一共还有几个子集没有解决
    // 返回 : 能否用光所有数字去解决剩下的所有子集
    private static boolean dfsPartition(int[] nums, int limit, int status, int cur, int rest, int[] dp) {
        if (rest == 0) {
            return status == 0;
        }
        if (dp[status] != 0) {
            return dp[status] == 1;
        }
        boolean ans = false;
        for (int i = 0; i < nums.length; i++) {
            // 考察每一个数字，只能使用状态为1的数字
            if ((status & (1 << i)) != 0 && cur + nums[i] <= limit) {
                if (cur + nums[i] == limit) {
                    ans = dfsPartition(nums, limit, status ^ (1 << i), 0, rest - 1, dp);
                } else {
                    ans = dfsPartition(nums, limit, status ^ (1 << i), cur + nums[i], rest, dp);
                }
                if (ans) {
                    break;
                }
            }
        }
        dp[status] = ans ? 1 : -1;
        return ans;
    }
    
    // LeetCode 1349. 参加考试的最大学生数解法
    public static int maxStudents(char[][] seats) {
        int m = seats.length;
        int n = seats[0].length;
        int[] validRows = new int[m];
        
        // 预处理每一行的可用座位（好座位），转换为位掩码
        for (int i = 0; i < m; i++) {
            int mask = 0;
            for (int j = 0; j < n; j++) {
                if (seats[i][j] == '.') {
                    mask |= (1 << j);
                }
            }
            validRows[i] = mask;
        }
        
        // dp[mask] 表示当前行座位分布为mask时的最大学生数
        Map<Integer, Integer> dp = new HashMap<>();
        dp.put(0, 0);
        
        // 逐行处理
        for (int i = 0; i < m; i++) {
            Map<Integer, Integer> newDp = new HashMap<>();
            int valid = validRows[i];
            
            // 枚举当前行的所有可能合法状态
            for (int mask = 0; mask < (1 << n); mask++) {
                // 检查是否只使用好座位，并且没有相邻学生
                if ((mask & valid) == mask && (mask & (mask << 1)) == 0) {
                    // 计算当前状态可以容纳的学生数
                    int count = Integer.bitCount(mask);
                    
                    // 遍历前一行的所有可能状态
                    for (Map.Entry<Integer, Integer> entry : dp.entrySet()) {
                        int prevMask = entry.getKey();
                        int prevMax = entry.getValue();
                        
                        // 检查是否有斜对角冲突
                        if ((mask & (prevMask << 1)) == 0 && (mask & (prevMask >> 1)) == 0) {
                            newDp.put(mask, Math.max(newDp.getOrDefault(mask, 0), prevMax + count));
                        }
                    }
                }
            }
            
            // 更新dp
            dp = newDp;
        }
        
        // 返回最大值
        return dp.values().stream().max(Integer::compare).orElse(0);
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试 LeetCode 943 最短超级串
        String[] words1 = {"alex", "loves", "leetcode"};
        System.out.println("LeetCode 943 最短超级串 测试:");
        System.out.println("输入: " + Arrays.toString(words1));
        System.out.println("结果: " + shortestSuperstring(words1));
        
        String[] words2 = {"catg", "ctaagt", "gcta", "ttca", "atgcatc"};
        System.out.println("\n输入: " + Arrays.toString(words2));
        System.out.println("结果: " + shortestSuperstring(words2));
        
        // 测试 LeetCode 1125 最小必要团队
        String[] req_skills1 = {"java", "nodejs", "reactjs"};
        String[][] people1 = {{"java"}, {"nodejs"}, {"nodejs", "reactjs"}};
        System.out.println("\nLeetCode 1125 最小必要团队 测试:");
        System.out.println("技能需求: " + Arrays.toString(req_skills1));
        System.out.print("人员技能: ");
        for (int i = 0; i < people1.length; i++) {
            System.out.print(Arrays.toString(people1[i]) + " ");
        }
        System.out.println();
        int[] result1 = smallestSufficientTeam(req_skills1, people1);
        System.out.println("结果团队: " + Arrays.toString(result1));
        
        // 测试 LeetCode 473 火柴拼正方形
        int[] matchsticks1 = {1, 1, 2, 2, 2};
        System.out.println("\nLeetCode 473 火柴拼正方形 测试:");
        System.out.println("输入: " + Arrays.toString(matchsticks1));
        System.out.println("结果: " + makesquare(matchsticks1));
        
        int[] matchsticks2 = {3, 3, 3, 3, 4};
        System.out.println("\n输入: " + Arrays.toString(matchsticks2));
        System.out.println("结果: " + makesquare(matchsticks2));
        
        // 测试 LeetCode 698 划分为k个相等的子集
        int[] nums1 = {4, 3, 2, 3, 5, 2, 1};
        int k1 = 4;
        System.out.println("\nLeetCode 698 划分为k个相等的子集 测试:");
        System.out.println("输入: " + Arrays.toString(nums1) + ", k = " + k1);
        System.out.println("结果: " + canPartitionKSubsets(nums1, k1));
        
        int[] nums2 = {1, 2, 3, 4};
        int k2 = 3;
        System.out.println("\n输入: " + Arrays.toString(nums2) + ", k = " + k2);
        System.out.println("结果: " + canPartitionKSubsets(nums2, k2));
        
        // 测试 LeetCode 1349 参加考试的最大学生数
        char[][] seats1 = {
            {'.', '#', '.'},
            {'.', '.', '.'},
            {'.', '#', '.'}
        };
        System.out.println("\nLeetCode 1349 参加考试的最大学生数 测试:");
        System.out.println("输入:");
        printSeats(seats1);
        System.out.println("结果: " + maxStudents(seats1));
        
        char[][] seats2 = {
            {'#', '.', '#', '#', '.', '#'},
            {'.', '#', '#', '#', '#', '.'},
            {'#', '.', '#', '#', '.', '#'}
        };
        System.out.println("\n输入:");
        printSeats(seats2);
        System.out.println("结果: " + maxStudents(seats2));
    }
    
    // 辅助方法：打印座位矩阵
    private static void printSeats(char[][] seats) {
        for (char[] row : seats) {
            System.out.println(Arrays.toString(row));
        }
    }
}