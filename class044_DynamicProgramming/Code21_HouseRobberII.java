// 打家劫舍II (House Robber II)
// 你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。
// 这个地方所有的房屋都围成一圈，这意味着第一个房屋和最后一个房屋是紧挨着的。
// 同时，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
// 给定一个代表每个房屋存放金额的非负整数数组，计算你在不触动警报装置的情况下，能够偷窃到的最高金额。
// 测试链接 : https://leetcode.cn/problems/house-robber-ii/

package class066;

import java.util.Arrays;

/**
 * 打家劫舍II - 环形数组的动态规划问题
 * 时间复杂度分析：
 * - 暴力递归：O(2^n) 指数级，存在大量重复计算
 * - 记忆化搜索：O(n) 每个状态只计算一次
 * - 动态规划：O(n) 线性时间复杂度
 * - 空间优化：O(1) 只保存必要的前两个状态
 * 
 * 空间复杂度分析：
 * - 暴力递归：O(n) 递归调用栈深度
 * - 记忆化搜索：O(n) 递归栈 + 记忆化数组
 * - 动态规划：O(n) dp数组存储所有状态
 * - 空间优化：O(1) 工程首选
 * 
 * 工程化考量：
 * 1. 环形数组处理：分解为两个线性问题
 * 2. 边界处理：空数组、单元素数组等
 * 3. 性能优化：空间优化版本应对大规模数据
 * 4. 代码复用：重用打家劫舍I的解决方案
 */
public class Code21_HouseRobberII {

    // 方法1：分解为两个线性问题
    // 时间复杂度：O(n) - 解决两个线性问题
    // 空间复杂度：O(n) - 使用辅助数组
    // 核心思路：环形问题分解为[0, n-2]和[1, n-1]两个线性问题
    public static int rob1(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        
        int n = nums.length;
        // 情况1：不偷最后一间房（偷第一间房）
        int max1 = robLinear(Arrays.copyOfRange(nums, 0, n - 1));
        // 情况2：不偷第一间房（偷最后一间房）
        int max2 = robLinear(Arrays.copyOfRange(nums, 1, n));
        
        return Math.max(max1, max2);
    }
    
    // 线性打家劫舍问题的解决方案（打家劫舍I）
    private static int robLinear(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        
        return dp[n - 1];
    }

    // 方法2：空间优化的分解方案
    // 时间复杂度：O(n) - 解决两个线性问题
    // 空间复杂度：O(1) - 只使用常数空间
    // 优化：避免创建新数组，直接在原数组上操作
    public static int rob2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        
        int n = nums.length;
        // 情况1：不偷最后一间房
        int max1 = robLinearOptimized(nums, 0, n - 2);
        // 情况2：不偷第一间房
        int max2 = robLinearOptimized(nums, 1, n - 1);
        
        return Math.max(max1, max2);
    }
    
    // 空间优化的线性打家劫舍
    private static int robLinearOptimized(int[] nums, int start, int end) {
        if (start > end) return 0;
        if (start == end) return nums[start];
        
        int prev2 = nums[start];  // dp[i-2]
        int prev1 = Math.max(nums[start], nums[start + 1]);  // dp[i-1]
        
        for (int i = start + 2; i <= end; i++) {
            int current = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }

    // 方法3：动态规划（统一处理）
    // 时间复杂度：O(n) - 遍历数组两次
    // 空间复杂度：O(n) - 使用dp数组
    // 核心思路：分别计算包含第一个元素和不包含第一个元素的情况
    public static int rob3(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        
        int n = nums.length;
        // dp1: 不偷第一间房的情况
        int[] dp1 = new int[n];
        // dp2: 偷第一间房的情况（不能偷最后一间房）
        int[] dp2 = new int[n];
        
        // 初始化dp1（不偷第一间房）
        dp1[0] = 0;
        dp1[1] = nums[1];
        for (int i = 2; i < n; i++) {
            dp1[i] = Math.max(dp1[i - 1], dp1[i - 2] + nums[i]);
        }
        
        // 初始化dp2（偷第一间房，不能偷最后一间房）
        dp2[0] = nums[0];
        dp2[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < n - 1; i++) {
            dp2[i] = Math.max(dp2[i - 1], dp2[i - 2] + nums[i]);
        }
        
        return Math.max(dp1[n - 1], dp2[n - 2]);
    }

    // 方法4：记忆化搜索（自顶向下）
    // 时间复杂度：O(n) - 每个状态只计算一次
    // 空间复杂度：O(n) - 递归栈和记忆化数组
    // 核心思路：递归解决两个子问题，使用记忆化避免重复计算
    public static int rob4(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        
        int n = nums.length;
        int[] memo1 = new int[n];  // 记忆化数组1（不偷最后一间房）
        int[] memo2 = new int[n];  // 记忆化数组2（不偷第一间房）
        Arrays.fill(memo1, -1);
        Arrays.fill(memo2, -1);
        
        // 情况1：不偷最后一间房
        int max1 = dfs(nums, 0, n - 2, memo1);
        // 情况2：不偷第一间房
        int max2 = dfs(nums, 1, n - 1, memo2);
        
        return Math.max(max1, max2);
    }
    
    private static int dfs(int[] nums, int start, int end, int[] memo) {
        if (start > end) return 0;
        if (memo[start] != -1) return memo[start];
        
        if (start == end) {
            memo[start] = nums[start];
            return nums[start];
        }
        
        if (start + 1 == end) {
            int max = Math.max(nums[start], nums[end]);
            memo[start] = max;
            return max;
        }
        
        // 选择1：偷当前房屋，跳过下一个
        int robCurrent = nums[start] + dfs(nums, start + 2, end, memo);
        // 选择2：不偷当前房屋，考虑下一个
        int skipCurrent = dfs(nums, start + 1, end, memo);
        
        int max = Math.max(robCurrent, skipCurrent);
        memo[start] = max;
        return max;
    }

    // 方法5：暴力递归（用于对比）
    // 时间复杂度：O(2^n) - 指数级，效率极低
    // 空间复杂度：O(n) - 递归调用栈深度
    // 问题：存在大量重复计算，仅用于教学目的
    public static int rob5(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        
        int n = nums.length;
        // 分解为两个线性问题
        int max1 = robLinearBruteForce(Arrays.copyOfRange(nums, 0, n - 1));
        int max2 = robLinearBruteForce(Arrays.copyOfRange(nums, 1, n));
        
        return Math.max(max1, max2);
    }
    
    private static int robLinearBruteForce(int[] nums) {
        return dfsBruteForce(nums, 0);
    }
    
    private static int dfsBruteForce(int[] nums, int index) {
        if (index >= nums.length) return 0;
        
        // 选择1：偷当前房屋，跳过下一个
        int robCurrent = nums[index] + dfsBruteForce(nums, index + 2);
        // 选择2：不偷当前房屋，考虑下一个
        int skipCurrent = dfsBruteForce(nums, index + 1);
        
        return Math.max(robCurrent, skipCurrent);
    }

    // 全面的测试用例
    public static void main(String[] args) {
        System.out.println("=== 打家劫舍II测试 ===");
        
        // 边界测试
        testCase(new int[]{}, 0, "空数组");
        testCase(new int[]{5}, 5, "单元素数组");
        testCase(new int[]{2, 3}, 3, "双元素数组");
        
        // LeetCode示例测试
        testCase(new int[]{2, 3, 2}, 3, "示例1");
        testCase(new int[]{1, 2, 3, 1}, 4, "示例2");
        testCase(new int[]{1, 2, 3}, 3, "示例3");
        
        // 常规测试
        testCase(new int[]{1, 2, 3, 4, 5}, 8, "递增金额");
        testCase(new int[]{5, 4, 3, 2, 1}, 8, "递减金额");
        testCase(new int[]{2, 7, 9, 3, 1}, 11, "混合金额");
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        int[] largeNums = new int[100];
        for (int i = 0; i < largeNums.length; i++) {
            largeNums[i] = i % 10 + 1;  // 1-10的循环金额
        }
        
        long start = System.currentTimeMillis();
        int result2 = rob2(largeNums);
        long end = System.currentTimeMillis();
        System.out.println("空间优化方法: " + result2 + ", 耗时: " + (end - start) + "ms");
        
        start = System.currentTimeMillis();
        int result3 = rob3(largeNums);
        end = System.currentTimeMillis();
        System.out.println("统一处理方法: " + result3 + ", 耗时: " + (end - start) + "ms");
        
        // 暴力方法太慢，不测试
        System.out.println("暴力方法在n=100时太慢，跳过测试");
    }
    
    private static void testCase(int[] nums, int expected, String description) {
        int result1 = rob1(nums);
        int result2 = rob2(nums);
        int result3 = rob3(nums);
        int result4 = rob4(nums);
        
        boolean allCorrect = (result1 == expected && result2 == expected && 
                            result3 == expected && result4 == expected);
        
        System.out.println(description + ": " + (allCorrect ? "✓" : "✗"));
        if (!allCorrect) {
            System.out.println("  方法1: " + result1 + " | 方法2: " + result2 + 
                             " | 方法3: " + result3 + " | 方法4: " + result4 + 
                             " | 预期: " + expected);
        }
    }
    
    /**
     * 算法总结与工程化思考：
     * 
     * 1. 问题本质：环形数组的最大不相邻子序列和问题
     *    - 关键洞察：环形问题可以分解为两个线性问题
     *    - 情况1：不偷最后一间房（可以偷第一间房）
     *    - 情况2：不偷第一间房（可以偷最后一间房）
     * 
     * 2. 时间复杂度对比：
     *    - 暴力递归：O(2^n) - 不可接受
     *    - 记忆化搜索：O(n) - 可接受
     *    - 动态规划：O(n) - 推荐
     *    - 空间优化：O(n) - 工程首选
     * 
     * 3. 空间复杂度对比：
     *    - 暴力递归：O(n) - 栈深度
     *    - 记忆化搜索：O(n) - 递归栈+缓存
     *    - 动态规划：O(n) - 数组存储
     *    - 空间优化：O(1) - 最优
     * 
     * 4. 工程选择依据：
     *    - 面试笔试：方法2（空间优化分解）
     *    - 大规模数据：方法2或方法3
     *    - 代码清晰：方法1（分解思路明确）
     * 
     * 5. 调试技巧：
     *    - 分别验证两个子问题的正确性
     *    - 边界测试确保环形处理正确
     *    - 性能测试选择最优算法
     * 
     * 6. 关联题目：
     *    - 打家劫舍I（线性数组版本）
     *    - 打家劫舍III（树形结构版本）
     *    - 最大子序列和问题
     * 
     * 7. 环形问题通用解法：
     *    - 分解为多个线性子问题
     *    - 分别求解后取最优解
     *    - 适用于环形房屋、环形道路等问题
     */
}