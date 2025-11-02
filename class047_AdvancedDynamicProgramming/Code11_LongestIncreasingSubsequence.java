package class069;

/**
 * 最长递增子序列 (Longest Increasing Subsequence) - 线性动态规划
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
 * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
 * 
 * 题目来源：LeetCode 300. 最长递增子序列
 * 测试链接：https://leetcode.cn/problems/longest-increasing-subsequence/
 * 
 * 解题思路：
 * 这是一个经典的线性动态规划问题。
 * 设 dp[i] 表示以 nums[i] 结尾的最长递增子序列的长度。
 * 对于每个位置 i，我们需要找到所有 j < i 且 nums[j] < nums[i] 的位置，
 * 然后取 dp[j] 的最大值加1。
 * 状态转移方程：dp[i] = max(dp[j] + 1) for all j < i and nums[j] < nums[i]
 * 边界条件：dp[i] = 1 (每个元素本身构成长度为1的子序列)
 * 
 * 算法实现：
 * 1. 动态规划：O(n^2)时间复杂度
 * 2. 二分查找优化：O(n log n)时间复杂度
 * 3. 记忆化搜索：递归计算，使用记忆化避免重复计算
 * 
 * 时间复杂度分析：
 * - 动态规划：O(n^2)
 * - 二分查找优化：O(n log n)
 * - 记忆化搜索：O(n^2)
 * 
 * 空间复杂度分析：
 * - 动态规划：O(n)
 * - 二分查找优化：O(n)
 * - 记忆化搜索：O(n)
 * 
 * 关键技巧：
 * 1. 状态定义：以当前元素结尾的最长递增子序列
 * 2. 状态转移：寻找前面所有较小元素的最长子序列
 * 3. 二分优化：维护一个递增数组，使用二分查找优化
 * 
 * 工程化考量：
 * 1. 输入验证：检查数组是否为空
 * 2. 边界条件：处理单元素数组
 * 3. 性能优化：二分查找版本最优
 * 4. 可读性：清晰的状态定义和转移方程
 */
public class Code11_LongestIncreasingSubsequence {
    
    /**
     * 动态规划解法 O(n^2)
     * 
     * @param nums 整数数组
     * @return 最长递增子序列的长度
     */
    public static int lengthOfLIS1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        // dp[i] 表示以 nums[i] 结尾的最长递增子序列的长度
        int[] dp = new int[n];
        
        // 初始化：每个元素本身构成长度为1的子序列
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }
        
        int maxLength = 1;
        
        // 状态转移
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }
        
        return maxLength;
    }
    
    /**
     * 二分查找优化解法 O(n log n)
     * 
     * @param nums 整数数组
     * @return 最长递增子序列的长度
     */
    public static int lengthOfLIS2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        // tails[i] 表示长度为 i+1 的递增子序列的最小尾部元素
        int[] tails = new int[n];
        int len = 0;  // 当前最长递增子序列的长度
        
        for (int num : nums) {
            // 使用二分查找找到第一个大于等于 num 的位置
            int left = 0, right = len;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            // 更新 tails 数组
            tails[left] = num;
            
            // 如果插入位置在末尾，说明找到了更长的递增子序列
            if (left == len) {
                len++;
            }
        }
        
        return len;
    }
    
    /**
     * 记忆化搜索解法
     * 
     * @param nums 整数数组
     * @return 最长递增子序列的长度
     */
    public static int lengthOfLIS3(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        // 记忆化数组
        int[] memo = new int[n];
        for (int i = 0; i < n; i++) {
            memo[i] = -1;
        }
        
        int maxLength = 0;
        // 尝试以每个元素作为结尾
        for (int i = 0; i < n; i++) {
            maxLength = Math.max(maxLength, dfs(nums, i, memo));
        }
        
        return maxLength;
    }
    
    /**
     * 深度优先搜索 + 记忆化
     * 
     * @param nums 整数数组
     * @param i 当前处理到的位置
     * @param memo 记忆化数组
     * @return 以 nums[i] 结尾的最长递增子序列的长度
     */
    private static int dfs(int[] nums, int i, int[] memo) {
        // 检查是否已经计算过
        if (memo[i] != -1) {
            return memo[i];
        }
        
        // 初始化：至少为1（自身）
        int maxLength = 1;
        
        // 寻找前面所有较小元素的最长子序列
        for (int j = 0; j < i; j++) {
            if (nums[j] < nums[i]) {
                maxLength = Math.max(maxLength, dfs(nums, j, memo) + 1);
            }
        }
        
        // 记忆化存储
        memo[i] = maxLength;
        return maxLength;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println("测试用例1:");
        System.out.println("数组: [10,9,2,5,3,7,101,18]");
        System.out.println("方法1结果: " + lengthOfLIS1(nums1));
        System.out.println("方法2结果: " + lengthOfLIS2(nums1));
        System.out.println("方法3结果: " + lengthOfLIS3(nums1));
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {0, 1, 0, 3, 2, 3};
        System.out.println("测试用例2:");
        System.out.println("数组: [0,1,0,3,2,3]");
        System.out.println("方法1结果: " + lengthOfLIS1(nums2));
        System.out.println("方法2结果: " + lengthOfLIS2(nums2));
        System.out.println("方法3结果: " + lengthOfLIS3(nums2));
        System.out.println();
        
        // 测试用例3
        int[] nums3 = {7, 7, 7, 7, 7, 7, 7};
        System.out.println("测试用例3:");
        System.out.println("数组: [7,7,7,7,7,7,7]");
        System.out.println("方法1结果: " + lengthOfLIS1(nums3));
        System.out.println("方法2结果: " + lengthOfLIS2(nums3));
        System.out.println("方法3结果: " + lengthOfLIS3(nums3));
    }
}