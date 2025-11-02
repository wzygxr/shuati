package class072;

import java.util.Arrays;

/**
 * 摆动序列 - LeetCode 376
 * 题目来源：https://leetcode.cn/problems/wiggle-subsequence/
 * 难度：中等
 * 题目描述：如果连续数字之间的差严格地在正数和负数之间交替，则数字序列称为摆动序列。
 * 第一个差（如果存在的话）可能是正数或负数。少于两个元素的序列也是摆动序列。
 * 例如，[1,7,4,9,2,5] 是一个摆动序列，因为差值 (6,-3,5,-7,3) 是正负交替出现的。
 * 相反, [1,4,7,2,5] 和 [1,7,4,5,5] 不是摆动序列，第一个序列是因为它的前两个差值都是正数，
 * 第二个序列是因为它的最后一个差值为零。
 * 
 * 核心思路：
 * 1. 这道题可以使用贪心算法来解决，因为我们只需要记录序列的趋势变化
 * 2. 摆动序列的关键在于相邻元素的差值交替变化
 * 3. 我们可以维护当前的趋势（上升、下降或初始状态），然后遍历数组，统计趋势变化的次数
 * 
 * 复杂度分析：
 * 时间复杂度：O(n)，其中n是数组的长度，我们只需要遍历一次数组
 * 空间复杂度：O(1)，只使用了常数级别的额外空间
 */
public class Code07_WiggleSubsequence {

    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 7, 4, 9, 2, 5};
        System.out.println("测试用例1结果：" + wiggleMaxLength(nums1) + "，预期结果：6");
        
        // 测试用例2
        int[] nums2 = {1, 17, 5, 10, 13, 15, 10, 5, 16, 8};
        System.out.println("测试用例2结果：" + wiggleMaxLength(nums2) + "，预期结果：7");
        
        // 测试用例3
        int[] nums3 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.println("测试用例3结果：" + wiggleMaxLength(nums3) + "，预期结果：2");
        
        // 测试用例4：边界情况
        int[] nums4 = {1}; // 只有一个元素
        System.out.println("测试用例4结果：" + wiggleMaxLength(nums4) + "，预期结果：1");
        
        // 测试用例5：边界情况
        int[] nums5 = {1, 1}; // 所有元素相同
        System.out.println("测试用例5结果：" + wiggleMaxLength(nums5) + "，预期结果：1");
    }
    
    /**
     * 计算最长摆动子序列的长度
     * @param nums 输入数组
     * @return 最长摆动子序列的长度
     */
    public static int wiggleMaxLength(int[] nums) {
        // 边界情况：如果数组长度小于2，直接返回数组长度
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return 1;
        }
        
        // 初始化
        int up = 1;   // 以最后一个差值为正的最长摆动子序列长度
        int down = 1; // 以最后一个差值为负的最长摆动子序列长度
        
        // 遍历数组，从第二个元素开始
        for (int i = 1; i < nums.length; i++) {
            // 如果当前元素大于前一个元素，更新up
            if (nums[i] > nums[i - 1]) {
                // 当前是上升趋势，最长摆动子序列长度等于之前下降趋势的长度加1
                up = down + 1;
            }
            // 如果当前元素小于前一个元素，更新down
            else if (nums[i] < nums[i - 1]) {
                // 当前是下降趋势，最长摆动子序列长度等于之前上升趋势的长度加1
                down = up + 1;
            }
            // 如果相等，不做任何操作，保持up和down不变
        }
        
        // 返回较大的值，因为最后一个差值可能是正也可能是负
        return Math.max(up, down);
    }
    
    /**
     * 另一种解法：贪心算法，记录趋势变化
     * @param nums 输入数组
     * @return 最长摆动子序列的长度
     */
    public static int wiggleMaxLengthGreedy(int[] nums) {
        // 边界情况处理
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return 1;
        }
        
        // 去除连续重复元素，这些不会对摆动序列产生影响
        // 例如：[1,1,2,2] 等价于 [1,2] 对于摆动序列的计算
        int n = nums.length;
        int count = 1; // 至少有一个元素
        int prevDiff = 0; // 前一个差值
        int currDiff = 0; // 当前差值
        
        for (int i = 1; i < n; i++) {
            // 计算当前差值
            currDiff = nums[i] - nums[i - 1];
            
            // 如果当前差值与前一个差值符号不同，说明出现了摆动
            // 注意prevDiff可以是0（初始状态），这时只要currDiff不为0就计入
            if ((currDiff > 0 && prevDiff <= 0) || (currDiff < 0 && prevDiff >= 0)) {
                count++;
                prevDiff = currDiff; // 更新前一个差值
            }
        }
        
        return count;
    }
    
    /**
     * 动态规划解法
     * @param nums 输入数组
     * @return 最长摆动子序列的长度
     */
    public static int wiggleMaxLengthDP(int[] nums) {
        // 边界情况处理
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;
        if (n == 1) {
            return 1;
        }
        
        // dp[i][0]: 以nums[i]结尾且最后一个差值为正的最长摆动子序列长度
        // dp[i][1]: 以nums[i]结尾且最后一个差值为负的最长摆动子序列长度
        int[][] dp = new int[n][2];
        
        // 初始化：每个元素自身可以形成长度为1的摆动子序列
        for (int i = 0; i < n; i++) {
            dp[i][0] = 1;
            dp[i][1] = 1;
        }
        
        // 填充dp数组
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    // 如果nums[i] > nums[j]，可以接在以nums[j]结尾且最后一个差值为负的序列后面
                    dp[i][0] = Math.max(dp[i][0], dp[j][1] + 1);
                } else if (nums[i] < nums[j]) {
                    // 如果nums[i] < nums[j]，可以接在以nums[j]结尾且最后一个差值为正的序列后面
                    dp[i][1] = Math.max(dp[i][1], dp[j][0] + 1);
                }
                // 如果相等，不更新
            }
        }
        
        // 找出最大值
        int maxLen = 0;
        for (int i = 0; i < n; i++) {
            maxLen = Math.max(maxLen, Math.max(dp[i][0], dp[i][1]));
        }
        
        return maxLen;
    }
    
    /**
     * 测试所有解法并比较结果
     * @param nums 输入数组
     */
    public static void testAllSolutions(int[] nums) {
        System.out.println("输入数组: " + Arrays.toString(nums));
        System.out.println("解法1（动态规划优化版）: " + wiggleMaxLength(nums));
        System.out.println("解法2（贪心算法）: " + wiggleMaxLengthGreedy(nums));
        System.out.println("解法3（常规动态规划）: " + wiggleMaxLengthDP(nums));
        System.out.println();
    }
}