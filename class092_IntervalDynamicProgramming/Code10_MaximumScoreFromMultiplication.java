package class077;

// LeetCode 1770. 执行乘法运算的最大分数
// 给定两个数组nums和multipliers，每次从nums的头部或尾部取一个数与multipliers[i]相乘，求最大得分。
// 测试链接: https://leetcode.cn/problems/maximum-score-from-performing-multiplication-operations/
// 
// 解题思路:
// 1. 状态定义：dp[i][j]表示使用前i个multipliers，从nums左端取了j个元素的最大分数
// 2. 状态转移：考虑两种选择：取左端或取右端
// 3. 时间复杂度：O(m^2)，其中m是multipliers的长度
// 4. 空间复杂度：O(m^2)，可以优化到O(m)
//
// 工程化考量:
// 1. 异常处理：检查输入合法性
// 2. 边界处理：处理空数组和边界情况
// 3. 性能优化：使用滚动数组优化空间复杂度
// 4. 测试覆盖：设计全面的测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code10_MaximumScoreFromMultiplication {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        String[] numsStr = br.readLine().split(" ");
        int[] nums = new int[numsStr.length];
        for (int i = 0; i < numsStr.length; i++) {
            nums[i] = Integer.parseInt(numsStr[i]);
        }
        
        String[] multipliersStr = br.readLine().split(" ");
        int[] multipliers = new int[multipliersStr.length];
        for (int i = 0; i < multipliersStr.length; i++) {
            multipliers[i] = Integer.parseInt(multipliersStr[i]);
        }
        
        out.println(maximumScore(nums, multipliers));
        out.flush();
        out.close();
        br.close();
    }

    /**
     * 区间DP解法 - 基本版本（空间复杂度O(m^2)）
     * 时间复杂度: O(m^2) - 其中m是multipliers的长度
     * 空间复杂度: O(m^2) - dp数组占用空间
     * 
     * 解题思路:
     * 1. 状态定义：dp[i][j]表示使用前i个multipliers，从nums左端取了j个元素的最大分数
     * 2. 状态转移：
     *    - 取左端：dp[i][j] = dp[i-1][j-1] + multipliers[i-1] * nums[j-1]
     *    - 取右端：dp[i][j] = dp[i-1][j] + multipliers[i-1] * nums[n - (i - j)]
     * 3. 初始化：dp[0][0] = 0
     * 4. 结果：max(dp[m][j]) for j in [0, m]
     */
    public static int maximumScore(int[] nums, int[] multipliers) {
        // 异常处理
        if (nums == null || multipliers == null || nums.length == 0 || multipliers.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        int m = multipliers.length;
        
        // 状态定义：dp[i][j]表示使用前i个multipliers，从nums左端取了j个元素的最大分数
        int[][] dp = new int[m + 1][m + 1];
        
        // 初始化：将dp数组初始化为最小整数，表示不可达状态
        for (int i = 0; i <= m; i++) {
            Arrays.fill(dp[i], Integer.MIN_VALUE);
        }
        dp[0][0] = 0; // 初始状态：没有使用任何multiplier
        
        int maxScore = Integer.MIN_VALUE;
        
        // 动态规划填表
        for (int i = 1; i <= m; i++) { // 使用前i个multipliers
            for (int j = 0; j <= i; j++) { // 从左端取了j个元素
                // 计算右端取的元素数量
                int rightCount = i - j;
                
                // 检查左端取j个元素是否合法
                if (j > 0) {
                    // 选择取左端：第i个multiplier乘以nums中左端第j个元素
                    int leftScore = dp[i - 1][j - 1] + multipliers[i - 1] * nums[j - 1];
                    dp[i][j] = Math.max(dp[i][j], leftScore);
                }
                
                // 检查右端取rightCount个元素是否合法
                if (rightCount > 0 && j <= i - 1) {
                    // 选择取右端：第i个multiplier乘以nums中右端第rightCount个元素
                    int rightIndex = n - rightCount;
                    int rightScore = dp[i - 1][j] + multipliers[i - 1] * nums[rightIndex];
                    dp[i][j] = Math.max(dp[i][j], rightScore);
                }
                
                // 更新最大分数
                if (i == m) {
                    maxScore = Math.max(maxScore, dp[i][j]);
                }
            }
        }
        
        return maxScore;
    }
    
    /**
     * 优化版本 - 使用滚动数组将空间复杂度优化到O(m)
     * 时间复杂度: O(m^2)
     * 空间复杂度: O(m)
     * 
     * 优化思路:
     * 1. 观察状态转移方程，发现dp[i]只依赖于dp[i-1]
     * 2. 可以使用两个数组交替计算，减少空间占用
     * 3. 适用于大规模数据场景
     */
    public static int maximumScoreOptimized(int[] nums, int[] multipliers) {
        // 异常处理
        if (nums == null || multipliers == null || nums.length == 0 || multipliers.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        int m = multipliers.length;
        
        // 使用两个数组进行滚动计算
        int[] dp = new int[m + 1]; // 当前状态
        int[] prev = new int[m + 1]; // 前一个状态
        
        // 初始化prev数组
        Arrays.fill(prev, Integer.MIN_VALUE);
        prev[0] = 0;
        
        // 动态规划填表
        for (int i = 1; i <= m; i++) {
            Arrays.fill(dp, Integer.MIN_VALUE);
            
            for (int j = 0; j <= i; j++) {
                int rightCount = i - j;
                
                // 取左端
                if (j > 0 && prev[j - 1] != Integer.MIN_VALUE) {
                    dp[j] = Math.max(dp[j], prev[j - 1] + multipliers[i - 1] * nums[j - 1]);
                }
                
                // 取右端
                if (rightCount > 0 && j <= i - 1 && prev[j] != Integer.MIN_VALUE) {
                    int rightIndex = n - rightCount;
                    dp[j] = Math.max(dp[j], prev[j] + multipliers[i - 1] * nums[rightIndex]);
                }
            }
            
            // 交换数组，准备下一轮计算
            int[] temp = prev;
            prev = dp;
            dp = temp;
        }
        
        // 寻找最大分数
        int maxScore = Integer.MIN_VALUE;
        for (int j = 0; j <= m; j++) {
            maxScore = Math.max(maxScore, prev[j]);
        }
        
        return maxScore;
    }
    
    /**
     * 记忆化搜索版本 - 递归+记忆化
     * 时间复杂度: O(m^2)
     * 空间复杂度: O(m^2)
     * 
     * 优点:
     * 1. 代码更直观，易于理解
     * 2. 避免不必要的状态计算
     * 缺点:
     * 1. 递归深度可能较大
     * 2. 栈空间开销
     */
    public static int maximumScoreMemo(int[] nums, int[] multipliers) {
        if (nums == null || multipliers == null || nums.length == 0 || multipliers.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        int m = multipliers.length;
        
        // 记忆化数组
        Integer[][] memo = new Integer[m][m];
        
        return dfs(nums, multipliers, 0, n - 1, 0, memo);
    }
    
    private static int dfs(int[] nums, int[] multipliers, int left, int right, int idx, Integer[][] memo) {
        // 边界条件：所有multipliers都已使用
        if (idx == multipliers.length) {
            return 0;
        }
        
        // 检查记忆化结果
        if (memo[left][idx] != null) {
            return memo[left][idx];
        }
        
        // 选择取左端
        int takeLeft = multipliers[idx] * nums[left] + 
                      dfs(nums, multipliers, left + 1, right, idx + 1, memo);
        
        // 选择取右端
        int takeRight = multipliers[idx] * nums[right] + 
                       dfs(nums, multipliers, left, right - 1, idx + 1, memo);
        
        // 取较大值并记忆化
        int result = Math.max(takeLeft, takeRight);
        memo[left][idx] = result;
        
        return result;
    }
    
    /**
     * 单元测试方法 - 验证算法正确性
     */
    public static void test() {
        // 测试用例1：示例输入
        int[] nums1 = {1, 2, 3};
        int[] multipliers1 = {3, 2, 1};
        int result1 = maximumScore(nums1, multipliers1);
        System.out.println("Test 1 - Expected: 14, Actual: " + result1);
        
        // 测试用例2：边界情况
        int[] nums2 = {1};
        int[] multipliers2 = {1};
        int result2 = maximumScore(nums2, multipliers2);
        System.out.println("Test 2 - Expected: 1, Actual: " + result2);
        
        // 测试用例3：大规模数据测试
        int[] nums3 = {1, 2, 3, 4, 5};
        int[] multipliers3 = {1, 2, 3, 4, 5};
        int result3 = maximumScore(nums3, multipliers3);
        System.out.println("Test 3 - Expected: 最大分数, Actual: " + result3);
    }
}