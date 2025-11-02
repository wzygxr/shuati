// 目标和 (Target Sum)
// 给你一个非负整数数组 nums 和一个整数 target 。
// 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个表达式 。
// 返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。
// 测试链接 : https://leetcode.cn/problems/target-sum/
public class Code14_TargetSum {

    // 方法1：暴力递归解法
    // 时间复杂度：O(2^n) - 指数级时间复杂度，效率极低
    // 空间复杂度：O(n) - 递归调用栈的深度
    // 问题：存在大量重复计算，效率低下
    public static int findTargetSumWays1(int[] nums, int target) {
        return f1(nums, 0, target);
    }

    // 从index位置开始，通过添加+/-符号，能否得到target
    public static int f1(int[] nums, int index, int target) {
        // base case
        if (index == nums.length) {
            return target == 0 ? 1 : 0;
        }
        // 选择1：给当前数字添加+号
        int add = f1(nums, index + 1, target - nums[index]);
        // 选择2：给当前数字添加-号
        int subtract = f1(nums, index + 1, target + nums[index]);
        return add + subtract;
    }

    // 方法2：记忆化搜索（自顶向下动态规划）
    // 时间复杂度：O(n*sum) - 每个状态只计算一次
    // 空间复杂度：O(n*sum) - dp数组和递归调用栈
    // 优化：通过缓存已经计算的结果避免重复计算
    public static int findTargetSumWays2(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // 如果target的绝对值超过数组总和，无法达到
        if (Math.abs(target) > sum) {
            return 0;
        }
        // dp[i][j] 表示处理到第i个数字时，和为j的方案数
        // 由于j可能为负数，需要偏移量处理
        int offset = sum;
        int[][] dp = new int[nums.length][2 * sum + 1];
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < 2 * sum + 1; j++) {
                dp[i][j] = -1;
            }
        }
        return f2(nums, 0, target, sum, dp);
    }

    // 从index位置开始，通过添加+/-符号，能否得到target
    public static int f2(int[] nums, int index, int target, int sum, int[][] dp) {
        if (index == nums.length) {
            return target == 0 ? 1 : 0;
        }
        int offset = sum;
        if (dp[index][target + offset] != -1) {
            return dp[index][target + offset];
        }
        // 选择1：给当前数字添加+号
        int add = f2(nums, index + 1, target - nums[index], sum, dp);
        // 选择2：给当前数字添加-号
        int subtract = f2(nums, index + 1, target + nums[index], sum, dp);
        int ans = add + subtract;
        dp[index][target + offset] = ans;
        return ans;
    }

    // 方法3：动态规划（自底向上）- 01背包问题
    // 时间复杂度：O(n*sum) - 需要填满整个dp表
    // 空间复杂度：O(n*sum) - dp数组存储所有状态
    // 优化：避免了递归调用的开销
    public static int findTargetSumWays3(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // 如果target的绝对值超过数组总和，无法达到
        if (Math.abs(target) > sum) {
            return 0;
        }
        // 如果(sum + target)是奇数，无法分割
        if ((sum + target) % 2 != 0) {
            return 0;
        }
        // 将问题转化为01背包问题
        // 设正数和为P，负数和为N，则P+N=sum，P-N=target
        // 解得P=(sum+target)/2
        int P = (sum + target) / 2;
        
        // dp[i][j] 表示前i个数字组成和为j的方案数
        int[][] dp = new int[nums.length + 1][P + 1];
        
        // 初始化边界条件
        dp[0][0] = 1; // 不选任何数字，和为0的方案数为1
        
        // 填表过程
        for (int i = 1; i <= nums.length; i++) {
            for (int j = 0; j <= P; j++) {
                // 不选当前数字
                dp[i][j] = dp[i - 1][j];
                // 如果当前数字不超过目标和，考虑选当前数字
                if (nums[i - 1] <= j) {
                    dp[i][j] += dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        return dp[nums.length][P];
    }

    // 方法4：空间优化的动态规划
    // 时间复杂度：O(n*sum) - 仍然需要计算所有状态
    // 空间复杂度：O(sum) - 只保存必要的状态值
    // 优化：只保存必要的状态，大幅减少空间使用
    public static int findTargetSumWays4(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // 如果target的绝对值超过数组总和，无法达到
        if (Math.abs(target) > sum) {
            return 0;
        }
        // 如果(sum + target)是奇数，无法分割
        if ((sum + target) % 2 != 0) {
            return 0;
        }
        // 将问题转化为01背包问题
        int P = (sum + target) / 2;
        
        // dp[j] 表示组成和为j的方案数
        int[] dp = new int[P + 1];
        dp[0] = 1; // 和为0的方案数为1
        
        // 填表过程
        for (int i = 0; i < nums.length; i++) {
            // 从后往前遍历，避免重复使用当前数字
            for (int j = P; j >= nums[i]; j--) {
                dp[j] += dp[j - nums[i]];
            }
        }
        return dp[P];
    }

    // 测试用例和性能对比
    public static void main(String[] args) {
        System.out.println("测试目标和实现：");
        
        // 测试用例1
        int[] nums1 = {1, 1, 1, 1, 1};
        int target1 = 3;
        System.out.println("nums = [" + arrayToString(nums1) + "], target = " + target1);
        System.out.println("方法3 (动态规划): " + findTargetSumWays3(nums1, target1));
        System.out.println("方法4 (空间优化): " + findTargetSumWays4(nums1, target1));
        
        // 测试用例2
        int[] nums2 = {1};
        int target2 = 1;
        System.out.println("\nnums = [" + arrayToString(nums2) + "], target = " + target2);
        System.out.println("方法3 (动态规划): " + findTargetSumWays3(nums2, target2));
        System.out.println("方法4 (空间优化): " + findTargetSumWays4(nums2, target2));
        
        // 测试用例3
        int[] nums3 = {0, 0, 0, 0, 0, 0, 0, 0, 1};
        int target3 = 1;
        System.out.println("\nnums = [" + arrayToString(nums3) + "], target = " + target3);
        System.out.println("方法3 (动态规划): " + findTargetSumWays3(nums3, target3));
        System.out.println("方法4 (空间优化): " + findTargetSumWays4(nums3, target3));
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