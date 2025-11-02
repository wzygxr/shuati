package class038;

import java.util.Arrays;

/**
 * LeetCode 698. 划分为k个相等的子集
 * 
 * 题目描述：
 * 给定一个整数数组 nums 和一个正整数 k，找出是否有可能把这个数组分成 k 个非空子集，其总和都相等。
 * 
 * 示例：
 * 输入：nums = [4, 3, 2, 3, 5, 2, 1], k = 4
 * 输出：true
 * 解释：有可能将其分成 4 个子集（5），（1,4），（2,3），（2,3）等于总和。
 * 
 * 输入：nums = [1,2,3,4], k = 3
 * 输出：false
 * 
 * 提示：
 * 1 <= k <= len(nums) <= 16
 * 0 < nums[i] < 10000
 * 每个元素的频率在 [1,4] 范围内
 * 
 * 链接：https://leetcode.cn/problems/partition-to-k-equal-sum-subsets/
 * 
 * 算法思路：
 * 1. 计算数组总和，如果不能被k整除，直接返回false
 * 2. 计算每个子集的目标和（总和/k）
 * 3. 使用回溯算法尝试将元素分配到k个子集
 * 4. 使用剪枝优化：排序、提前终止等
 * 
 * 时间复杂度：O(k^n)，其中n是数组长度
 * 空间复杂度：O(n)，递归栈深度
 */
public class Code28_PartitionToKEqualSumSubsets {

    /**
     * 判断是否能将数组划分为k个和相等的子集
     * 
     * @param nums 整数数组
     * @param k 子集个数
     * @return 是否能成功划分
     */
    public static boolean canPartitionKSubsets(int[] nums, int k) {
        // 计算总和
        int total = 0;
        for (int num : nums) {
            total += num;
        }
        
        // 如果不能被k整除，直接返回false
        if (total % k != 0) {
            return false;
        }
        
        int target = total / k;
        
        // 排序（从大到小），便于剪枝
        Arrays.sort(nums);
        reverse(nums);
        
        // 如果有元素大于目标值，直接返回false
        if (nums[0] > target) {
            return false;
        }
        
        // 初始化k个子集的当前和
        int[] subsets = new int[k];
        
        return backtrack(nums, 0, subsets, target);
    }

    /**
     * 回溯函数分配元素到子集
     * 
     * @param nums 整数数组
     * @param index 当前处理的元素索引
     * @param subsets k个子集的当前和
     * @param target 目标和
     * @return 是否能成功分配
     */
    private static boolean backtrack(int[] nums, int index, int[] subsets, int target) {
        // 终止条件：所有元素都已分配
        if (index == nums.length) {
            // 检查所有子集的和是否都等于目标值
            for (int sum : subsets) {
                if (sum != target) {
                    return false;
                }
            }
            return true;
        }
        
        int currentNum = nums[index];
        
        // 尝试将当前元素分配到k个子集
        for (int i = 0; i < subsets.length; i++) {
            // 剪枝：如果当前子集加上元素超过目标值，跳过
            if (subsets[i] + currentNum > target) {
                continue;
            }
            
            // 剪枝：如果当前子集与前一个子集和相同，且前一个子集分配失败，跳过
            // 这样可以避免重复计算相同的情况
            if (i > 0 && subsets[i] == subsets[i - 1]) {
                continue;
            }
            
            subsets[i] += currentNum;
            if (backtrack(nums, index + 1, subsets, target)) {
                return true;
            }
            subsets[i] -= currentNum;  // 回溯
            
            // 剪枝：如果当前子集和为0，且分配失败，说明无法成功
            if (subsets[i] == 0) {
                break;
            }
        }
        
        return false;
    }

    /**
     * 解法二：使用位运算 + 动态规划
     * 适用于需要高效计算的情况
     * 
     * @param nums 整数数组
     * @param k 子集个数
     * @return 是否能成功划分
     */
    public static boolean canPartitionKSubsetsDP(int[] nums, int k) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }
        
        if (total % k != 0) {
            return false;
        }
        
        int target = total / k;
        int n = nums.length;
        int totalStates = 1 << n;
        
        // dp[mask] 表示使用mask对应的元素能组成的和模target的余数
        int[] dp = new int[totalStates];
        Arrays.fill(dp, -1);
        dp[0] = 0;
        
        for (int mask = 0; mask < totalStates; mask++) {
            if (dp[mask] == -1) {
                continue;
            }
            
            for (int i = 0; i < n; i++) {
                // 如果第i个元素还未使用
                if ((mask & (1 << i)) == 0) {
                    int nextMask = mask | (1 << i);
                    int remainder = dp[mask] + nums[i];
                    
                    // 如果当前和超过目标值，跳过
                    if (remainder > target) {
                        continue;
                    }
                    
                    dp[nextMask] = (remainder == target) ? 0 : remainder;
                }
            }
        }
        
        return dp[totalStates - 1] == 0;
    }

    /**
     * 解法三：使用DFS + 剪枝优化
     * 更直观的实现方式
     * 
     * @param nums 整数数组
     * @param k 子集个数
     * @return 是否能成功划分
     */
    public static boolean canPartitionKSubsetsDFS(int[] nums, int k) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }
        
        if (total % k != 0) {
            return false;
        }
        
        int target = total / k;
        
        // 排序（从大到小）
        Arrays.sort(nums);
        reverse(nums);
        
        // 如果有元素大于目标值，直接返回false
        if (nums[0] > target) {
            return false;
        }
        
        boolean[] used = new boolean[nums.length];
        return dfs(nums, used, 0, k, 0, target);
    }

    private static boolean dfs(int[] nums, boolean[] used, int start, int k, int currentSum, int target) {
        // 如果已经成功构建了k-1个子集，剩下的自然能构成第k个子集
        if (k == 1) {
            return true;
        }
        
        // 如果当前子集和等于目标值，开始构建下一个子集
        if (currentSum == target) {
            return dfs(nums, used, 0, k - 1, 0, target);
        }
        
        for (int i = start; i < nums.length; i++) {
            if (!used[i] && currentSum + nums[i] <= target) {
                used[i] = true;
                if (dfs(nums, used, i + 1, k, currentSum + nums[i], target)) {
                    return true;
                }
                used[i] = false;  // 回溯
            }
        }
        
        return false;
    }

    // 反转数组（从大到小排序）
    private static void reverse(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {4, 3, 2, 3, 5, 2, 1};
        int k1 = 4;
        boolean result1 = canPartitionKSubsets(nums1, k1);
        System.out.println("输入: nums = [4,3,2,3,5,2,1], k = " + k1);
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int[] nums2 = {1, 2, 3, 4};
        int k2 = 3;
        boolean result2 = canPartitionKSubsets(nums2, k2);
        System.out.println("\n输入: nums = [1,2,3,4], k = " + k2);
        System.out.println("输出: " + result2);
        
        // 测试用例3
        int[] nums3 = {2, 2, 2, 2, 3, 4, 5};
        int k3 = 4;
        boolean result3 = canPartitionKSubsets(nums3, k3);
        System.out.println("\n输入: nums = [2,2,2,2,3,4,5], k = " + k3);
        System.out.println("输出: " + result3);
        
        // 测试动态规划解法
        System.out.println("\n=== 动态规划解法测试 ===");
        boolean result4 = canPartitionKSubsetsDP(nums1, k1);
        System.out.println("输入: nums = [4,3,2,3,5,2,1], k = " + k1);
        System.out.println("输出: " + result4);
    }
}