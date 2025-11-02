package class069;

/**
 * 打家劫舍 (House Robber) - 线性动态规划
 * 
 * 题目描述：
 * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，
 * 影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
 * 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你不触动警报装置的情况下，
 * 一夜之内能够偷窃到的最高金额。
 * 
 * 题目来源：LeetCode 198. 打家劫舍
 * 测试链接：https://leetcode.cn/problems/house-robber/
 * 
 * 解题思路：
 * 这是一个经典的线性动态规划问题。
 * 设 dp[i] 表示偷窃前 i 个房屋能获得的最大金额。
 * 对于第 i 个房屋，有两种选择：
 * 1. 偷窃第 i 个房屋：dp[i] = dp[i-2] + nums[i]
 * 2. 不偷窃第 i 个房屋：dp[i] = dp[i-1]
 * 状态转移方程：dp[i] = max(dp[i-2] + nums[i], dp[i-1])
 * 边界条件：dp[0] = nums[0], dp[1] = max(nums[0], nums[1])
 * 
 * 算法实现：
 * 1. 动态规划：使用数组存储每一步的结果
 * 2. 空间优化：只保存前两个状态值
 * 3. 记忆化搜索：递归计算，使用记忆化避免重复计算
 * 
 * 时间复杂度分析：
 * - 动态规划：O(n)
 * - 空间优化：O(n)
 * - 记忆化搜索：O(n)
 * 
 * 空间复杂度分析：
 * - 动态规划：O(n)
 * - 空间优化：O(1)
 * - 记忆化搜索：O(n)
 * 
 * 关键技巧：
 * 1. 状态转移：当前状态依赖于前两个状态
 * 2. 边界处理：处理数组长度为1和2的特殊情况
 * 3. 空间优化：使用滚动变量降低空间复杂度
 * 
 * 工程化考量：
 * 1. 输入验证：检查数组是否为空
 * 2. 边界条件：处理特殊情况
 * 3. 性能优化：空间优化版本最优
 * 4. 可读性：清晰的状态定义和转移方程
 */
public class Code10_HouseRobber {
    
    /**
     * 动态规划解法
     * 
     * @param nums 每个房屋存放金额的数组
     * @return 能够偷窃到的最高金额
     */
    public static int rob1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        if (n == 1) {
            return nums[0];
        }
        
        if (n == 2) {
            return Math.max(nums[0], nums[1]);
        }
        
        // dp[i] 表示偷窃前 i+1 个房屋能获得的最大金额
        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        
        // 状态转移
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
        }
        
        return dp[n - 1];
    }
    
    /**
     * 空间优化的动态规划解法
     * 
     * @param nums 每个房屋存放金额的数组
     * @return 能够偷窃到的最高金额
     */
    public static int rob2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        if (n == 1) {
            return nums[0];
        }
        
        // 只需要保存前两个状态
        int prev2 = nums[0];  // dp[i-2]
        int prev1 = Math.max(nums[0], nums[1]);  // dp[i-1]
        
        if (n == 2) {
            return prev1;
        }
        
        int current = 0; // dp[i]
        
        // 状态转移
        for (int i = 2; i < n; i++) {
            current = Math.max(prev2 + nums[i], prev1);
            prev2 = prev1;
            prev1 = current;
        }
        
        return current;
    }
    
    /**
     * 记忆化搜索解法
     * 
     * @param nums 每个房屋存放金额的数组
     * @return 能够偷窃到的最高金额
     */
    public static int rob3(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        // 记忆化数组
        int[] memo = new int[n];
        for (int i = 0; i < n; i++) {
            memo[i] = -1;
        }
        
        return dfs(nums, n - 1, memo);
    }
    
    /**
     * 深度优先搜索 + 记忆化
     * 
     * @param nums 每个房屋存放金额的数组
     * @param i 当前处理到第几个房屋
     * @param memo 记忆化数组
     * @return 能够偷窃到的最高金额
     */
    private static int dfs(int[] nums, int i, int[] memo) {
        // 边界条件
        if (i < 0) {
            return 0;
        }
        
        if (i == 0) {
            return nums[0];
        }
        
        // 检查是否已经计算过
        if (memo[i] != -1) {
            return memo[i];
        }
        
        // 状态转移：偷窃当前房屋或不偷窃当前房屋
        int ans = Math.max(dfs(nums, i - 2, memo) + nums[i], dfs(nums, i - 1, memo));
        
        // 记忆化存储
        memo[i] = ans;
        return ans;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 2, 3, 1};
        System.out.println("测试用例1:");
        System.out.println("房屋金额: [1,2,3,1]");
        System.out.println("方法1结果: " + rob1(nums1));
        System.out.println("方法2结果: " + rob2(nums1));
        System.out.println("方法3结果: " + rob3(nums1));
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {2, 7, 9, 3, 1};
        System.out.println("测试用例2:");
        System.out.println("房屋金额: [2,7,9,3,1]");
        System.out.println("方法1结果: " + rob1(nums2));
        System.out.println("方法2结果: " + rob2(nums2));
        System.out.println("方法3结果: " + rob3(nums2));
        System.out.println();
        
        // 测试用例3
        int[] nums3 = {5};
        System.out.println("测试用例3:");
        System.out.println("房屋金额: [5]");
        System.out.println("方法1结果: " + rob1(nums3));
        System.out.println("方法2结果: " + rob2(nums3));
        System.out.println("方法3结果: " + rob3(nums3));
    }
}