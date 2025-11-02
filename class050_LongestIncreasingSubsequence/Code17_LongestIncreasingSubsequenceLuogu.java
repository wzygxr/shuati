package class072;

import java.util.Arrays;

/**
 * 洛谷最长上升子序列问题
 * 
 * 题目来源：洛谷 B3637 最长上升子序列
 * 题目链接：https://www.luogu.com.cn/problem/B3637
 * 题目描述：给出一个由 n 个不超过 10^6 的正整数组成的序列。请输出这个序列的最长上升子序列的长度。
 * 
 * 算法思路：
 * 1. 使用贪心+二分查找优化解法
 * 2. 维护ends数组，ends[i]表示长度为i+1的递增子序列的最小结尾元素
 * 3. 对于每个元素，在ends中二分查找>=num的最左位置
 * 
 * 时间复杂度：O(n*logn) - 每个元素二分查找O(logn)
 * 空间复杂度：O(n) - 需要ends数组存储状态
 * 是否最优解：是，这是最优解法
 * 
 * 示例：
 * 输入: 
 * 5
 * 1 3 2 4 5
 * 输出: 4
 * 解释: 最长上升子序列为[1,3,4,5]或[1,2,4,5]，长度为4。
 */

public class Code17_LongestIncreasingSubsequenceLuogu {

    /**
     * 计算最长上升子序列的长度（洛谷标准解法）
     * 
     * @param nums 输入的整数数组
     * @return 最长上升子序列的长度
     */
    public static int lengthOfLIS(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        
        int[] ends = new int[n];
        int len = 0;
        
        for (int i = 0; i < n; i++) {
            int find = binarySearch(ends, len, nums[i]);
            if (find == -1) {
                ends[len++] = nums[i];
            } else {
                ends[find] = nums[i];
            }
        }
        
        return len;
    }

    /**
     * 在严格升序数组ends中查找>=num的最左位置
     * 
     * @param ends 严格升序数组
     * @param len 有效长度
     * @param num 目标值
     * @return >=num的最左位置，如果不存在返回-1
     */
    private static int binarySearch(int[] ends, int len, int num) {
        int left = 0, right = len - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (ends[mid] >= num) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }

    /**
     * 动态规划解法（用于对比）
     * 
     * 算法思路：
     * 1. dp[i]表示以nums[i]结尾的最长上升子序列长度
     * 2. 对于每个位置i，遍历前面所有位置j，如果nums[j] < nums[i]，则更新dp[i]
     * 
     * 时间复杂度：O(n²) - 外层循环n次，内层循环最多n次
     * 空间复杂度：O(n) - 需要dp数组存储状态
     * 是否最优解：否，存在O(n*logn)的优化解法
     * 
     * @param nums 输入的整数数组
     * @return 最长上升子序列的长度
     */
    public static int lengthOfLISDP(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int maxLen = 1;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLen = Math.max(maxLen, dp[i]);
        }
        
        return maxLen;
    }

    /**
     * 洛谷标准输入输出格式处理
     * 
     * 算法思路：
     * 1. 模拟洛谷的输入格式（第一行n，第二行n个整数）
     * 2. 使用最优解法计算LIS长度
     * 3. 输出结果
     * 
     * @param n 序列长度
     * @param nums 序列元素
     * @return 最长上升子序列的长度
     */
    public static int solveLuoguProblem(int n, int[] nums) {
        return lengthOfLIS(nums);
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：洛谷示例
        int[] nums1 = {1, 3, 2, 4, 5};
        System.out.println("洛谷示例输入: [1,3,2,4,5]");
        System.out.println("优化方法输出: " + lengthOfLIS(nums1));
        System.out.println("DP方法输出: " + lengthOfLISDP(nums1));
        System.out.println("期望: 4");
        System.out.println();
        
        // 测试用例2：严格递增序列
        int[] nums2 = {1, 2, 3, 4, 5};
        System.out.println("严格递增序列: [1,2,3,4,5]");
        System.out.println("优化方法输出: " + lengthOfLIS(nums2));
        System.out.println("DP方法输出: " + lengthOfLISDP(nums2));
        System.out.println("期望: 5");
        System.out.println();
        
        // 测试用例3：严格递减序列
        int[] nums3 = {5, 4, 3, 2, 1};
        System.out.println("严格递减序列: [5,4,3,2,1]");
        System.out.println("优化方法输出: " + lengthOfLIS(nums3));
        System.out.println("DP方法输出: " + lengthOfLISDP(nums3));
        System.out.println("期望: 1");
        System.out.println();
        
        // 测试用例4：所有元素相同
        int[] nums4 = {2, 2, 2, 2, 2};
        System.out.println("所有元素相同: [2,2,2,2,2]");
        System.out.println("优化方法输出: " + lengthOfLIS(nums4));
        System.out.println("DP方法输出: " + lengthOfLISDP(nums4));
        System.out.println("期望: 1");
        System.out.println();
        
        // 测试用例5：复杂序列
        int[] nums5 = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println("复杂序列: [10,9,2,5,3,7,101,18]");
        System.out.println("优化方法输出: " + lengthOfLIS(nums5));
        System.out.println("DP方法输出: " + lengthOfLISDP(nums5));
        System.out.println("期望: 4");
        System.out.println();
        
        // 性能测试：大规模数据
        int[] largeNums = new int[10000];
        for (int i = 0; i < 10000; i++) {
            largeNums[i] = (int) (Math.random() * 1000000);
        }
        
        long startTime = System.currentTimeMillis();
        int result1 = lengthOfLIS(largeNums);
        long endTime = System.currentTimeMillis();
        System.out.println("优化方法处理10000个元素耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int result2 = lengthOfLISDP(largeNums);
        endTime = System.currentTimeMillis();
        System.out.println("DP方法处理10000个元素耗时: " + (endTime - startTime) + "ms");
        System.out.println("两种方法结果是否一致: " + (result1 == result2));
    }
}