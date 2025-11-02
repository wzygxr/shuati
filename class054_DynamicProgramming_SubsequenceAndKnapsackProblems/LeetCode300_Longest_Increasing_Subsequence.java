package class086;

// LeetCode 300. 最长递增子序列
// 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
// 子序列 是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
// 测试链接 : https://leetcode.cn/problems/longest-increasing-subsequence/

public class LeetCode300_Longest_Increasing_Subsequence {
    
    /*
     * 算法详解：最长递增子序列（LeetCode 300）
     * 
     * 问题描述：
     * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
     * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
     * 
     * 算法思路：
     * 使用贪心+二分查找的方法计算LIS长度，时间复杂度O(n log n)。
     * 1. 维护一个ends数组，ends[i]表示长度为i+1的递增子序列的最小末尾元素
     * 2. 遍历原数组，对于每个元素使用二分查找找到其在ends数组中的合适位置
     * 3. 更新ends数组并记录最长长度
     * 
     * 时间复杂度分析：
     * 1. 遍历数组：O(n)
     * 2. 二分查找：O(log n)
     * 3. 总体时间复杂度：O(n log n)
     * 
     * 空间复杂度分析：
     * 1. ends数组：O(n)
     * 2. 总体空间复杂度：O(n)
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入数组是否为空
     * 2. 边界处理：处理空数组和单元素数组的情况
     * 3. 性能优化：使用二分查找将时间复杂度从O(n^2)优化到O(n log n)
     * 
     * 极端场景验证：
     * 1. 输入数组为空的情况
     * 2. 输入数组只有一个元素的情况
     * 3. 输入数组元素全部相同的情况
     * 4. 输入数组严格递增的情况
     * 5. 输入数组严格递减的情况
     */
    
    public static int lengthOfLIS(int[] nums) {
        // 异常处理：检查输入数组是否为空
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        
        // 特殊情况：只有一个元素
        if (n == 1) {
            return 1;
        }
        
        // ends[i] 表示长度为i+1的递增子序列的最小末尾元素
        int[] ends = new int[n];
        // 当前最长LIS的长度
        int len = 0;
        
        // 遍历原数组
        for (int i = 0; i < n; i++) {
            // 使用二分查找找到nums[i]在ends数组中的合适位置
            int index = binarySearch(ends, len, nums[i]);
            
            // 如果index等于len，说明nums[i]比所有元素都大，需要扩展ends数组
            if (index == len) {
                len++;
            }
            
            // 更新ends数组
            ends[index] = nums[i];
        }
        
        // 返回最长LIS长度
        return len;
    }
    
    // 二分查找：在ends数组中找到第一个大于等于target的位置
    private static int binarySearch(int[] ends, int len, int target) {
        int left = 0, right = len;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (ends[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        return left;
    }
    
    // 动态规划解法：时间复杂度O(n^2)，空间复杂度O(n)
    public static int lengthOfLISDP(int[] nums) {
        // 异常处理：检查输入数组是否为空
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        
        // dp[i] 表示以nums[i]结尾的最长递增子序列长度
        int[] dp = new int[n];
        // 初始化：每个元素自身构成长度为1的子序列
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }
        
        // 记录最长长度
        int maxLen = 1;
        
        // 填充dp数组
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 如果nums[j] < nums[i]，可以将nums[i]接在以nums[j]结尾的子序列后面
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            // 更新最长长度
            maxLen = Math.max(maxLen, dp[i]);
        }
        
        return maxLen;
    }
    
    // 贪心+二分查找解法（优化空间）
    public static int lengthOfLISOptimized(int[] nums) {
        // 异常处理：检查输入数组是否为空
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        
        // 使用数组模拟栈，存储递增子序列
        int[] stack = new int[n];
        int top = 0; // 栈顶指针
        
        // 遍历原数组
        for (int i = 0; i < n; i++) {
            // 使用二分查找找到第一个大于等于nums[i]的位置
            int pos = binarySearch(stack, top, nums[i]);
            
            // 如果pos等于top，说明需要扩展栈
            if (pos == top) {
                top++;
            }
            
            // 更新栈
            stack[pos] = nums[i];
        }
        
        // 返回最长LIS长度
        return top;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println("Test 1 (Binary Search method): " + lengthOfLIS(nums1));
        System.out.println("Test 1 (DP method): " + lengthOfLISDP(nums1));
        System.out.println("Test 1 (Optimized method): " + lengthOfLISOptimized(nums1));
        // 期望输出: 4 ([2,3,7,18])
        
        // 测试用例2
        int[] nums2 = {0, 1, 0, 3, 2, 3};
        System.out.println("Test 2 (Binary Search method): " + lengthOfLIS(nums2));
        System.out.println("Test 2 (DP method): " + lengthOfLISDP(nums2));
        System.out.println("Test 2 (Optimized method): " + lengthOfLISOptimized(nums2));
        // 期望输出: 4 ([0,1,2,3])
        
        // 测试用例3
        int[] nums3 = {7, 7, 7, 7, 7, 7, 7};
        System.out.println("Test 3 (Binary Search method): " + lengthOfLIS(nums3));
        System.out.println("Test 3 (DP method): " + lengthOfLISDP(nums3));
        System.out.println("Test 3 (Optimized method): " + lengthOfLISOptimized(nums3));
        // 期望输出: 1
        
        // 测试用例4
        int[] nums4 = {};
        System.out.println("Test 4 (Binary Search method): " + lengthOfLIS(nums4));
        System.out.println("Test 4 (DP method): " + lengthOfLISDP(nums4));
        System.out.println("Test 4 (Optimized method): " + lengthOfLISOptimized(nums4));
        // 期望输出: 0
        
        // 测试用例5
        int[] nums5 = {1};
        System.out.println("Test 5 (Binary Search method): " + lengthOfLIS(nums5));
        System.out.println("Test 5 (DP method): " + lengthOfLISDP(nums5));
        System.out.println("Test 5 (Optimized method): " + lengthOfLISOptimized(nums5));
        // 期望输出: 1
    }
}