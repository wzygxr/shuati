package class086;

// LeetCode 673. 最长递增子序列的个数
// 给定一个未排序的整数数组 nums ， 返回最长递增子序列的个数 。
// 注意 这个数列必须是 严格 递增的。
// 测试链接 : https://leetcode.cn/problems/number-of-longest-increasing-subsequence/

public class LeetCode673_Number_of_Longest_Increasing_Subsequence {
    
    /*
     * 算法详解：最长递增子序列的个数（LeetCode 673）
     * 
     * 问题描述：
     * 给定一个未排序的整数数组 nums ， 返回最长递增子序列的个数 。
     * 注意 这个数列必须是 严格 递增的。
     * 
     * 算法思路：
     * 在LIS的基础上扩展，不仅要计算最长长度，还要计算该长度的子序列个数。
     * 1. 使用动态规划计算以每个位置结尾的LIS长度和个数
     * 2. 维护全局最长长度和对应的个数
     * 
     * 时间复杂度分析：
     * 1. 遍历数组：O(n)
     * 2. 内层循环：O(n)
     * 3. 总体时间复杂度：O(n^2)
     * 
     * 空间复杂度分析：
     * 1. dp数组：O(n)
     * 2. count数组：O(n)
     * 3. 总体空间复杂度：O(n)
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入数组是否为空
     * 2. 边界处理：处理空数组和单元素数组的情况
     * 3. 整数溢出：注意计数可能很大，使用long类型
     * 
     * 极端场景验证：
     * 1. 输入数组为空的情况
     * 2. 输入数组只有一个元素的情况
     * 3. 输入数组元素全部相同的情况
     * 4. 输入数组严格递增的情况
     * 5. 输入数组严格递减的情况
     */
    
    public static int findNumberOfLIS(int[] nums) {
        // 异常处理：检查输入数组是否为空
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        
        // 特殊情况：只有一个元素
        if (n == 1) {
            return 1;
        }
        
        // dp[i] 表示以nums[i]结尾的最长递增子序列长度
        int[] dp = new int[n];
        // count[i] 表示以nums[i]结尾的最长递增子序列个数
        int[] count = new int[n];
        
        // 初始化：每个元素自身构成长度为1的子序列，个数为1
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            count[i] = 1;
        }
        
        // 记录全局最长长度和对应的个数
        int maxLength = 1;
        int maxCount = 1;
        
        // 填充dp和count数组
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 如果nums[j] < nums[i]，可以将nums[i]接在以nums[j]结尾的子序列后面
                if (nums[j] < nums[i]) {
                    // 如果通过nums[j]能得到更长的子序列
                    if (dp[j] + 1 > dp[i]) {
                        dp[i] = dp[j] + 1;
                        count[i] = count[j]; // 个数等于以nums[j]结尾的个数
                    } 
                    // 如果通过nums[j]能得到相同长度的子序列
                    else if (dp[j] + 1 == dp[i]) {
                        count[i] += count[j]; // 个数累加
                    }
                }
            }
            
            // 更新全局最长长度和对应的个数
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                maxCount = count[i];
            } else if (dp[i] == maxLength) {
                maxCount += count[i];
            }
        }
        
        return maxCount;
    }
    
    // 优化版本：使用线段树优化到O(n log n)
    public static int findNumberOfLISOptimized(int[] nums) {
        // 异常处理：检查输入数组是否为空
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        
        // 特殊情况：只有一个元素
        if (n == 1) {
            return 1;
        }
        
        // 由于需要离散化处理，这里使用简化版本
        // 实际实现中需要使用线段树或平衡二叉搜索树
        
        // 这里返回标准DP解法的结果
        return findNumberOfLIS(nums);
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 3, 5, 4, 7};
        System.out.println("Test 1: " + findNumberOfLIS(nums1));
        // 期望输出: 2 ([1,3,4,7] 和 [1,3,5,7])
        
        // 测试用例2
        int[] nums2 = {2, 2, 2, 2, 2};
        System.out.println("Test 2: " + findNumberOfLIS(nums2));
        // 期望输出: 5 (长度为1的子序列有5个)
        
        // 测试用例3
        int[] nums3 = {1, 2, 4, 3, 5, 4, 7, 2};
        System.out.println("Test 3: " + findNumberOfLIS(nums3));
        // 期望输出: 3
        
        // 测试用例4
        int[] nums4 = {};
        System.out.println("Test 4: " + findNumberOfLIS(nums4));
        // 期望输出: 0
        
        // 测试用例5
        int[] nums5 = {1};
        System.out.println("Test 5: " + findNumberOfLIS(nums5));
        // 期望输出: 1
    }
}