package class038;

/**
 * LeetCode 494. 目标和
 * 
 * 题目描述：
 * 给你一个非负整数数组 nums 和一个整数 target 。
 * 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个表达式 。
 * 返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。
 * 
 * 示例：
 * 输入：nums = [1,1,1,1,1], target = 3
 * 输出：5
 * 
 * 输入：nums = [1], target = 1
 * 输出：1
 * 
 * 提示：
 * 1 <= nums.length <= 20
 * 0 <= nums[i] <= 1000
 * 0 <= sum(nums[i]) <= 1000
 * -1000 <= target <= 1000
 * 
 * 链接：https://leetcode.cn/problems/target-sum/
 */
public class Code12_TargetSum {

    /**
     * 计算目标和的表达式数目（回溯算法）
     * 
     * 算法思路：
     * 1. 使用回溯算法遍历所有可能的符号组合
     * 2. 对于每个数字，有两种选择：加号或减号
     * 3. 递归处理下一个数字
     * 4. 当处理完所有数字时，检查结果是否等于目标值
     * 
     * 时间复杂度：O(2^n)
     * 空间复杂度：O(n)
     * 
     * @param nums 数组
     * @param target 目标值
     * @return 表达式数目
     */
    public static int findTargetSumWays(int[] nums, int target) {
        return backtrack(nums, target, 0, 0);
    }

    /**
     * 回溯函数计算目标和的表达式数目
     * 
     * @param nums 数组
     * @param target 目标值
     * @param index 当前处理的索引
     * @param sum 当前和
     * @return 表达式数目
     */
    private static int backtrack(int[] nums, int target, int index, int sum) {
        // 终止条件：已处理完所有数字
        if (index == nums.length) {
            return sum == target ? 1 : 0;
        }
        
        // 选择加号
        int add = backtrack(nums, target, index + 1, sum + nums[index]);
        // 选择减号
        int subtract = backtrack(nums, target, index + 1, sum - nums[index]);
        
        return add + subtract;
    }

    /**
     * 计算目标和的表达式数目（动态规划优化）
     * 
     * 算法思路：
     * 1. 将问题转换为子集和问题
     * 2. 假设正数集合和为P，负数集合和为N，则P-N=target，P+N=sum
     * 3. 联立得P=(target+sum)/2，问题转化为找出和为P的子集数目
     * 4. 使用动态规划求解子集和问题
     * 
     * 时间复杂度：O(n * sum)
     * 空间复杂度：O(sum)
     * 
     * @param nums 数组
     * @param target 目标值
     * @return 表达式数目
     */
    public static int findTargetSumWaysDP(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) sum += num;
        
        // 边界情况
        if (sum < Math.abs(target) || (sum + target) % 2 != 0) return 0;
        
        // 转换为子集和问题
        int P = (sum + target) / 2;
        int[] dp = new int[P + 1];
        dp[0] = 1;
        
        for (int num : nums) {
            for (int i = P; i >= num; i--) {
                dp[i] += dp[i - num];
            }
        }
        
        return dp[P];
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 1, 1, 1, 1};
        int target1 = 3;
        int result1 = findTargetSumWays(nums1, target1);
        int result1DP = findTargetSumWaysDP(nums1, target1);
        System.out.println("输入: nums = [1,1,1,1,1], target = " + target1);
        System.out.println("输出 (回溯): " + result1);
        System.out.println("输出 (动态规划): " + result1DP);
        
        // 测试用例2
        int[] nums2 = {1};
        int target2 = 1;
        int result2 = findTargetSumWays(nums2, target2);
        int result2DP = findTargetSumWaysDP(nums2, target2);
        System.out.println("\n输入: nums = [1], target = " + target2);
        System.out.println("输出 (回溯): " + result2);
        System.out.println("输出 (动态规划): " + result2DP);
        
        // 测试用例3
        int[] nums3 = {1, 0};
        int target3 = 1;
        int result3 = findTargetSumWays(nums3, target3);
        int result3DP = findTargetSumWaysDP(nums3, target3);
        System.out.println("\n输入: nums = [1,0], target = " + target3);
        System.out.println("输出 (回溯): " + result3);
        System.out.println("输出 (动态规划): " + result3DP);
    }
}