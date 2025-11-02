// 分割等和子集 (Partition Equal Subset Sum)
// 给你一个只包含正整数的非空数组 nums。请你判断是否可以将这个数组分割成两个子集，
// 使得两个子集的元素和相等。
// 测试链接 : https://leetcode.cn/problems/partition-equal-subset-sum/
public class Code13_PartitionEqualSubsetSum {

    // 方法1：暴力递归解法
    // 时间复杂度：O(2^n) - 指数级时间复杂度，效率极低
    // 空间复杂度：O(n) - 递归调用栈的深度
    // 问题：存在大量重复计算，效率低下
    public static boolean canPartition1(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // 如果总和是奇数，无法分割成两个相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        return f1(nums, 0, sum / 2);
    }

    // 从index位置开始，能否选出一些数字，使得和为target
    public static boolean f1(int[] nums, int index, int target) {
        // base case
        if (target == 0) {
            return true;
        }
        if (index == nums.length || target < 0) {
            return false;
        }
        // 选择1：选当前数字
        boolean select = f1(nums, index + 1, target - nums[index]);
        // 选择2：不选当前数字
        boolean notSelect = f1(nums, index + 1, target);
        return select || notSelect;
    }

    // 方法2：记忆化搜索（自顶向下动态规划）
    // 时间复杂度：O(n*sum) - 每个状态只计算一次
    // 空间复杂度：O(n*sum) - dp数组和递归调用栈
    // 优化：通过缓存已经计算的结果避免重复计算
    public static boolean canPartition2(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // 如果总和是奇数，无法分割成两个相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        int target = sum / 2;
        int[][] dp = new int[nums.length][target + 1];
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j <= target; j++) {
                dp[i][j] = -1;
            }
        }
        return f2(nums, 0, target, dp) == 1;
    }

    // 从index位置开始，能否选出一些数字，使得和为target
    // 返回值：1表示可以，0表示不可以
    public static int f2(int[] nums, int index, int target, int[][] dp) {
        if (target == 0) {
            return 1;
        }
        if (index == nums.length || target < 0) {
            return 0;
        }
        if (dp[index][target] != -1) {
            return dp[index][target];
        }
        // 选择1：选当前数字
        int select = f2(nums, index + 1, target - nums[index], dp);
        // 选择2：不选当前数字
        int notSelect = f2(nums, index + 1, target, dp);
        int ans = (select == 1 || notSelect == 1) ? 1 : 0;
        dp[index][target] = ans;
        return ans;
    }

    // 方法3：动态规划（自底向上）- 01背包问题
    // 时间复杂度：O(n*sum) - 需要填满整个dp表
    // 空间复杂度：O(n*sum) - dp数组存储所有状态
    // 优化：避免了递归调用的开销
    public static boolean canPartition3(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // 如果总和是奇数，无法分割成两个相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        int target = sum / 2;
        int n = nums.length;
        // dp[i][j] 表示前i个数字能否组成和为j
        boolean[][] dp = new boolean[n + 1][target + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= n; i++) {
            dp[i][0] = true; // 和为0总是可以达到（不选任何数字）
        }
        
        // 填表过程
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                // 不选当前数字
                dp[i][j] = dp[i - 1][j];
                // 如果当前数字不超过目标和，考虑选当前数字
                if (nums[i - 1] <= j) {
                    dp[i][j] = dp[i][j] || dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        return dp[n][target];
    }

    // 方法4：空间优化的动态规划
    // 时间复杂度：O(n*sum) - 仍然需要计算所有状态
    // 空间复杂度：O(sum) - 只保存必要的状态值
    // 优化：只保存必要的状态，大幅减少空间使用
    public static boolean canPartition4(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // 如果总和是奇数，无法分割成两个相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        int target = sum / 2;
        
        // dp[j] 表示能否组成和为j
        boolean[] dp = new boolean[target + 1];
        dp[0] = true; // 和为0总是可以达到
        
        // 填表过程
        for (int i = 0; i < nums.length; i++) {
            // 从后往前遍历，避免重复使用当前数字
            for (int j = target; j >= nums[i]; j--) {
                dp[j] = dp[j] || dp[j - nums[i]];
            }
        }
        return dp[target];
    }

    // 测试用例和性能对比
    public static void main(String[] args) {
        System.out.println("测试分割等和子集实现：");
        
        // 测试用例1
        int[] nums1 = {1, 5, 11, 5};
        System.out.println("nums = [" + arrayToString(nums1) + "]");
        System.out.println("方法3 (动态规划): " + canPartition3(nums1));
        System.out.println("方法4 (空间优化): " + canPartition4(nums1));
        
        // 测试用例2
        int[] nums2 = {1, 2, 3, 5};
        System.out.println("\nnums = [" + arrayToString(nums2) + "]");
        System.out.println("方法3 (动态规划): " + canPartition3(nums2));
        System.out.println("方法4 (空间优化): " + canPartition4(nums2));
        
        // 测试用例3
        int[] nums3 = {1, 1};
        System.out.println("\nnums = [" + arrayToString(nums3) + "]");
        System.out.println("方法3 (动态规划): " + canPartition3(nums3));
        System.out.println("方法4 (空间优化): " + canPartition4(nums3));
    }
    
    // 辅助方法：将数组转换为字符串
    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}