package class074;

// 一和零
// 给你一个二进制字符串数组 strs 和两个整数 m 和 n
// 请你找出并返回 strs 的最大子集的长度，该子集中 最多 有 m 个 0 和 n 个 1
// 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集
// 测试链接 : https://leetcode.cn/problems/ones-and-zeroes/

/*
 * 算法详解：
 * 这是一个二维背包问题，其中每个字符串有两个维度的"重量"：0的个数和1的个数。
 * 我们需要在不超过m个0和n个1的限制下，选择尽可能多的字符串。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i][j][k]表示前i个字符串，使用不超过j个0和k个1的最大子集大小
 * 2. 状态转移：对于每个字符串，可以选择或不选择
 *    - 不选择：dp[i][j][k] = dp[i-1][j][k]
 *    - 选择：dp[i][j][k] = dp[i-1][j-zeros][k-ones] + 1
 * 3. 空间优化：使用滚动数组将三维优化到二维
 * 
 * 时间复杂度分析：
 * 设字符串数量为L，m和n为背包容量
 * 1. 预处理每个字符串的0和1数量：O(L * avg_len)
 * 2. 动态规划计算：O(L * m * n)
 * 总时间复杂度：O(L * m * n)
 * 
 * 空间复杂度分析：
 * 1. 三维DP数组：O(L * m * n)
 * 2. 空间优化后：O(m * n)
 * 
 * 相关题目扩展：
 * 1. LeetCode 474. 一和零（本题）
 * 2. LeetCode 494. 目标和（背包变种）
 * 3. LeetCode 1049. 最后一块石头的重量 II（背包问题）
 * 4. LeetCode 416. 分割等和子集（背包问题）
 * 5. 洛谷 P1757 通天之分组背包（分组背包）
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理空数组、非法参数等边界情况
 * 3. 可配置性：可以将m和n作为配置参数传入
 * 4. 单元测试：为findMaxForm方法编写测试用例
 * 5. 性能优化：对于大数据量场景，考虑使用空间优化版本
 * 
 * 语言特性差异：
 * 1. Java：使用静态数组提高访问速度
 * 2. C++：可以使用vector，但要注意内存分配开销
 * 3. Python：列表推导式简洁但性能较低
 * 
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 * 
 * 优化点：
 * 1. 空间压缩：从三维dp优化到二维dp
 * 2. 预处理优化：提前计算每个字符串的0和1数量
 * 3. 剪枝优化：当字符串的0或1数量超过限制时跳过
 * 
 * 与标准背包问题的区别：
 * 1. 二维约束：同时受到0和1两个维度的限制
 * 2. 目标函数：求最大子集大小，而不是最大价值
 * 3. 物品特性：每个物品有两个重量维度
 */

public class Code12_OnesAndZeroes {
    
    public static int findMaxForm(String[] strs, int m, int n) {
        int len = strs.length;
        // 预处理每个字符串的0和1数量
        int[][] counts = new int[len][2];
        for (int i = 0; i < len; i++) {
            String s = strs[i];
            int zeros = 0, ones = 0;
            for (char c : s.toCharArray()) {
                if (c == '0') zeros++;
                else ones++;
            }
            counts[i][0] = zeros;
            counts[i][1] = ones;
        }
        
        // 三维DP版本（便于理解）
        int[][][] dp = new int[len + 1][m + 1][n + 1];
        
        for (int i = 1; i <= len; i++) {
            int zeros = counts[i - 1][0];
            int ones = counts[i - 1][1];
            for (int j = 0; j <= m; j++) {
                for (int k = 0; k <= n; k++) {
                    // 不选择当前字符串
                    dp[i][j][k] = dp[i - 1][j][k];
                    // 如果可以选择当前字符串
                    if (j >= zeros && k >= ones) {
                        dp[i][j][k] = Math.max(dp[i][j][k], dp[i - 1][j - zeros][k - ones] + 1);
                    }
                }
            }
        }
        
        return dp[len][m][n];
    }
    
    // 空间优化版本（推荐使用）
    public static int findMaxFormOptimized(String[] strs, int m, int n) {
        int len = strs.length;
        // 预处理每个字符串的0和1数量
        int[][] counts = new int[len][2];
        for (int i = 0; i < len; i++) {
            String s = strs[i];
            int zeros = 0, ones = 0;
            for (char c : s.toCharArray()) {
                if (c == '0') zeros++;
                else ones++;
            }
            counts[i][0] = zeros;
            counts[i][1] = ones;
        }
        
        // 二维DP数组，空间优化
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 0; i < len; i++) {
            int zeros = counts[i][0];
            int ones = counts[i][1];
            // 从后往前更新，避免重复使用
            for (int j = m; j >= zeros; j--) {
                for (int k = n; k >= ones; k--) {
                    dp[j][k] = Math.max(dp[j][k], dp[j - zeros][k - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String[] strs1 = {"10", "0001", "111001", "1", "0"};
        int m1 = 5, n1 = 3;
        System.out.println("测试用例1:");
        System.out.println("三维DP结果: " + findMaxForm(strs1, m1, n1));
        System.out.println("优化版本结果: " + findMaxFormOptimized(strs1, m1, n1));
        System.out.println("预期结果: 4");
        System.out.println();
        
        // 测试用例2
        String[] strs2 = {"10", "0", "1"};
        int m2 = 1, n2 = 1;
        System.out.println("测试用例2:");
        System.out.println("三维DP结果: " + findMaxForm(strs2, m2, n2));
        System.out.println("优化版本结果: " + findMaxFormOptimized(strs2, m2, n2));
        System.out.println("预期结果: 2");
        System.out.println();
        
        // 测试用例3：边界情况
        String[] strs3 = {};
        int m3 = 0, n3 = 0;
        System.out.println("测试用例3（空数组）:");
        System.out.println("三维DP结果: " + findMaxForm(strs3, m3, n3));
        System.out.println("优化版本结果: " + findMaxFormOptimized(strs3, m3, n3));
        System.out.println("预期结果: 0");
    }
    
    /*
     * =============================================================================================
     * 补充题目：LeetCode 494. 目标和
     * 题目链接：https://leetcode.cn/problems/target-sum/
     * 题目描述：给你一个整数数组 nums 和一个整数 target。
     * 向数组中的每个整数前添加 '+' 或 '-'，然后串联起所有整数，可以构造一个表达式。
     * 返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。
     * 
     * 解题思路：
     * 这是一个背包问题的变种，可以转化为子集和问题。
     * 设所有正数的和为P，所有负数的和为N，则有：P - N = target，P + N = sum
     * 解得：P = (target + sum) / 2
     * 问题转化为：在nums中找出和为P的子集数目。
     * 
     * 状态定义：dp[i][j]表示前i个数，和为j的子集数目
     * 状态转移：
     * - 不选第i个数：dp[i][j] = dp[i-1][j]
     * - 选第i个数：dp[i][j] += dp[i-1][j - nums[i-1]]
     * 
     * 时间复杂度：O(n * P)，其中n为数组长度，P为目标和
     * 空间复杂度：O(P)，使用一维数组优化
     * 
     * Java实现：
     * public int findTargetSumWays(int[] nums, int target) {
     *     int sum = 0;
     *     for (int num : nums) sum += num;
     *     
     *     // 边界条件检查
     *     if (Math.abs(target) > sum) return 0;
     *     if ((target + sum) % 2 != 0) return 0;
     *     
     *     int P = (target + sum) / 2;
     *     if (P < 0) return 0;
     *     
     *     int[] dp = new int[P + 1];
     *     dp[0] = 1; // 和为0的方案数为1（不选任何数）
     *     
     *     for (int num : nums) {
     *         for (int j = P; j >= num; j--) {
     *             dp[j] += dp[j - num];
     *         }
     *     }
     *     
     *     return dp[P];
     * }
     * 
     * 工程化考量：
     * 1. 边界检查：处理目标值过大、和为奇数等特殊情况
     * 2. 数值溢出：使用long类型存储中间结果
     * 3. 异常处理：处理空数组、非法参数等情况
     * 
     * 优化思路：
     * 1. 空间压缩：使用一维数组进行优化
     * 2. 剪枝优化：提前终止不可能的情况
     * 3. 记忆化搜索：使用DFS+记忆化作为替代方案
     */
}