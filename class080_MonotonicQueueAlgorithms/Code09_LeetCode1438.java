import java.util.Arrays;

/**
 * 题目名称：LeetCode 1438. 绝对差不超过限制的最长连续子数组
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
 * 题目难度：中等
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，和一个表示限制的整数 limit，
 * 请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit。
 * 如果不存在满足条件的子数组，则返回 0。
 * 
 * 解题思路：
 * 使用滑动窗口配合双单调队列解决该问题。
 * 1. 使用单调递增队列维护窗口内的最小值
 * 2. 使用单调递减队列维护窗口内的最大值
 * 3. 滑动窗口右边界不断扩展，当窗口内最大值与最小值的差超过limit时，收缩左边界
 * 4. 记录满足条件的最长窗口长度
 *
 * 算法步骤：
 * 1. 初始化双指针和双单调队列
 * 2. 右指针不断向右扩展窗口
 * 3. 维护两个单调队列的性质
 * 4. 当窗口不满足条件时，收缩左边界
 * 5. 记录最长窗口长度
 *
 * 时间复杂度分析：
 * O(n) - 每个元素最多入队出队一次
 *
 * 空间复杂度分析：
 * O(n) - 两个单调队列最多存储n个元素
 *
 * 是否最优解：
 * ✅ 是，这是处理此类问题的最优解法
 * 
 * 工程化考量：
 * - 使用数组模拟双端队列以提高性能
 * - 考虑边界条件处理（空数组、单个元素等）
 * - 处理极端输入情况（大数组、极限值等）
 */

public class Code09_LeetCode1438 {
    
    // 最大数组大小，根据题目约束设置
    public static final int MAXN = 100001;
    
    // 单调递增队列维护最小值
    public static int[] minDeque = new int[MAXN];
    public static int minH, minT;
    
    // 单调递减队列维护最大值
    public static int[] maxDeque = new int[MAXN];
    public static int maxH, maxT;
    
    /**
     * 计算绝对差不超过限制的最长连续子数组长度
     * @param nums 输入数组
     * @param limit 绝对差限制
     * @return 最长满足条件的子数组长度
     */
    public static int longestSubarray(int[] nums, int limit) {
        // 边界条件处理
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 初始化队列指针
        minH = minT = 0;
        maxH = maxT = 0;
        
        int n = nums.length;
        int left = 0; // 窗口左边界
        int maxLength = 0; // 记录最大长度
        
        // 遍历数组，扩展窗口右边界
        for (int right = 0; right < n; right++) {
            int current = nums[right];
            
            // 维护单调递增队列（最小值队列）
            // 从队尾开始，移除所有大于等于当前元素的索引
            while (minH < minT && nums[minDeque[minT - 1]] >= current) {
                minT--;
            }
            minDeque[minT++] = right;
            
            // 维护单调递减队列（最大值队列）
            // 从队尾开始，移除所有小于等于当前元素的索引
            while (maxH < maxT && nums[maxDeque[maxT - 1]] <= current) {
                maxT--;
            }
            maxDeque[maxT++] = right;
            
            // 检查当前窗口是否满足条件
            // 如果最大值与最小值的差超过limit，需要收缩左边界
            while (minH < minT && maxH < maxT && 
                   nums[maxDeque[maxH]] - nums[minDeque[minH]] > limit) {
                // 如果左边界指向的是最小值队列的头部，需要移除
                if (minDeque[minH] == left) {
                    minH++;
                }
                // 如果左边界指向的是最大值队列的头部，需要移除
                if (maxDeque[maxH] == left) {
                    maxH++;
                }
                left++; // 收缩左边界
            }
            
            // 更新最大窗口长度
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 测试方法 - 包含多种边界情况和测试用例
     */
    public static void testLongestSubarray() {
        System.out.println("=== LeetCode 1438 测试用例 ===");
        
        // 测试用例1：基础示例
        int[] nums1 = {8, 2, 4, 7};
        int limit1 = 4;
        int result1 = longestSubarray(nums1, limit1);
        System.out.println("测试用例1 - 输入: [8,2,4,7], limit=4");
        System.out.println("预期输出: 2, 实际输出: " + result1);
        System.out.println("测试结果: " + (result1 == 2 ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例2：包含重复元素
        int[] nums2 = {10, 1, 2, 4, 7, 2};
        int limit2 = 5;
        int result2 = longestSubarray(nums2, limit2);
        System.out.println("\n测试用例2 - 输入: [10,1,2,4,7,2], limit=5");
        System.out.println("预期输出: 4, 实际输出: " + result2);
        System.out.println("测试结果: " + (result2 == 4 ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例3：limit为0的特殊情况
        int[] nums3 = {4, 2, 2, 2, 4, 4, 2, 2};
        int limit3 = 0;
        int result3 = longestSubarray(nums3, limit3);
        System.out.println("\n测试用例3 - 输入: [4,2,2,2,4,4,2,2], limit=0");
        System.out.println("预期输出: 3, 实际输出: " + result3);
        System.out.println("测试结果: " + (result3 == 3 ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例4：单个元素
        int[] nums4 = {5};
        int limit4 = 10;
        int result4 = longestSubarray(nums4, limit4);
        System.out.println("\n测试用例4 - 输入: [5], limit=10");
        System.out.println("预期输出: 1, 实际输出: " + result4);
        System.out.println("测试结果: " + (result4 == 1 ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例5：空数组
        int[] nums5 = {};
        int limit5 = 5;
        int result5 = longestSubarray(nums5, limit5);
        System.out.println("\n测试用例5 - 输入: [], limit=5");
        System.out.println("预期输出: 0, 实际输出: " + result5);
        System.out.println("测试结果: " + (result5 == 0 ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例6：递减序列
        int[] nums6 = {5, 4, 3, 2, 1};
        int limit6 = 2;
        int result6 = longestSubarray(nums6, limit6);
        System.out.println("\n测试用例6 - 输入: [5,4,3,2,1], limit=2");
        System.out.println("预期输出: 3, 实际输出: " + result6);
        System.out.println("测试结果: " + (result6 == 3 ? "✓ 通过" : "✗ 失败"));
        
        System.out.println("\n=== 性能测试 ===");
        
        // 性能测试：大数组测试
        int[] largeNums = new int[10000];
        Arrays.fill(largeNums, 1);
        long startTime = System.currentTimeMillis();
        int largeResult = longestSubarray(largeNums, 0);
        long endTime = System.currentTimeMillis();
        System.out.println("大数组测试 (10000个元素): " + largeResult);
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(n) - 每个元素最多入队出队一次");
        System.out.println("空间复杂度: O(n) - 两个单调队列最多存储n个元素");
        System.out.println("最优解: ✅ 是");
    }
    
    public static void main(String[] args) {
        testLongestSubarray();
    }
}