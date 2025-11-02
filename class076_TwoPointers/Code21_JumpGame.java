import java.util.*;
import java.util.stream.Collectors;

/**
 * LeetCode 55. 跳跃游戏 (Jump Game)
 * 
 * 题目描述:
 * 给定一个非负整数数组 nums，你最初位于数组的第一个位置。
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * 判断你是否能够到达最后一个位置。
 * 
 * 示例1:
 * 输入: nums = [2,3,1,1,4]
 * 输出: true
 * 解释: 可以先跳 1 步，从位置 0 到达位置 1, 然后再从位置 1 跳 3 步到达最后一个位置。
 * 
 * 示例2:
 * 输入: nums = [3,2,1,0,4]
 * 输出: false
 * 解释: 无论怎样，总会到达索引为 3 的位置。但该位置的最大跳跃长度是 0 ， 所以永远不能到达最后一个位置。
 * 
 * 提示:
 * 1 <= nums.length <= 10^4
 * 0 <= nums[i] <= 10^5
 * 
 * 题目链接: https://leetcode.com/problems/jump-game/
 * 
 * 解题思路:
 * 这道题可以使用贪心算法来解决。我们的目标是判断是否能够到达最后一个位置。
 * 
 * 贪心策略：维护一个变量表示当前能够到达的最远位置。遍历数组，不断更新这个最远位置。
 * 如果在任何时候，当前能够到达的最远位置小于当前遍历到的索引，说明无法到达该位置，也就无法到达最后一个位置。
 * 
 * 具体来说，我们维护一个变量 maxReach，表示当前能够到达的最远位置。初始时，maxReach = 0。
 * 遍历数组，对于每个位置 i，如果 i > maxReach，说明无法到达位置 i，返回 false。
 * 否则，更新 maxReach = max(maxReach, i + nums[i])。
 * 如果 maxReach >= nums.length - 1，说明已经可以到达最后一个位置，返回 true。
 * 
 * 时间复杂度: O(n)，其中 n 是数组的长度。我们只需要遍历数组一次。
 * 空间复杂度: O(1)，只使用了常数级别的额外空间。
 * 
 * 此外，我们还提供两种其他解法：
 * 1. 动态规划解法：时间复杂度 O(n^2)，空间复杂度 O(n)
 * 2. 回溯解法：时间复杂度 O(2^n)，空间复杂度 O(n)，但在大数��输入时可能会超时
 */

public class Code21_JumpGame {
    
    /**
     * 解法一: 贪心算法（最优解）
     * 
     * @param nums 非负整数数组
     * @return 是否能够到达最后一个位置
     */
    public static boolean canJumpGreedy(int[] nums) {
        // 参数校验
        if (nums == null || nums.length <= 1) {
            return true; // 如果数组为空或只有一个元素，已经在终点
        }
        
        int maxReach = 0; // 当前能够到达的最远位置
        
        // 遍历数组
        for (int i = 0; i < nums.length; i++) {
            // 如果当前位置已经无法到达，返回false
            if (i > maxReach) {
                return false;
            }
            
            // 更新能够到达的最远位置
            maxReach = Math.max(maxReach, i + nums[i]);
            
            // 如果已经可以到达或超过最后一个位置，可以提前返回true
            if (maxReach >= nums.length - 1) {
                return true;
            }
        }
        
        // 遍历完整个数组后，判断是否能够到达最后一个位置
        return maxReach >= nums.length - 1;
    }
    
    /**
     * 解法二: 动态规划 - 自顶向下
     * 
     * @param nums 非负整数数组
     * @return 是否能够到达最后一个位置
     */
    public static boolean canJumpDynamicProgrammingTopDown(int[] nums) {
        // 参数校验
        if (nums == null || nums.length <= 1) {
            return true; // 如果数组为空或只有一个元素，已经在终点
        }
        
        int n = nums.length;
        // memo[i]表示从位置i是否可以到达最后一个位置
        // 0: 未计算, 1: 可以到达, 2: 无法到达
        int[] memo = new int[n];
        // 最后一个位置可以到达自身
        memo[n - 1] = 1;
        
        return canJumpFromPosition(0, nums, memo);
    }
    
    /**
     * 辅助方法：判断从位置pos是否可以到达最后一个位置
     * 
     * @param pos 当前位置
     * @param nums 非负整数数组
     * @param memo 记忆化数组
     * @return 是否能够到达最后一个位置
     */
    private static boolean canJumpFromPosition(int pos, int[] nums, int[] memo) {
        // 如果已经计算过，直接返回结果
        if (memo[pos] != 0) {
            return memo[pos] == 1;
        }
        
        // 计算从当前位置可以到达的最远位置
        int furthestJump = Math.min(pos + nums[pos], nums.length - 1);
        
        // 尝试从当前位置跳到所有可能的位置
        for (int nextPos = pos + 1; nextPos <= furthestJump; nextPos++) {
            if (canJumpFromPosition(nextPos, nums, memo)) {
                memo[pos] = 1; // 可以到达
                return true;
            }
        }
        
        memo[pos] = 2; // 无法到达
        return false;
    }
    
    /**
     * 解法三: 动态规划 - 自底向上
     * 
     * @param nums 非负整数数组
     * @return 是否能够到达最后一个位置
     */
    public static boolean canJumpDynamicProgrammingBottomUp(int[] nums) {
        // 参数校验
        if (nums == null || nums.length <= 1) {
            return true; // 如果数组为空或只有一个元素，已经在终点
        }
        
        int n = nums.length;
        // dp[i]表示从位置i是否可以到达最后一个位置
        boolean[] dp = new boolean[n];
        // 最后一个位置可以到达自身
        dp[n - 1] = true;
        
        // 从后往前遍历
        for (int i = n - 2; i >= 0; i--) {
            // 计算从当前位置可以到达的最远位置
            int furthestJump = Math.min(i + nums[i], n - 1);
            
            // 检查从当前位置能否跳到一个可以到达终点的位置
            for (int j = i + 1; j <= furthestJump; j++) {
                if (dp[j]) {
                    dp[i] = true;
                    break; // 一旦找到一个可达的位置，就可以停止检查
                }
            }
        }
        
        // 返回是否可以从起始位置到达终点
        return dp[0];
    }
    
    /**
     * 解法四: 回溯（暴力解法，在大规模输入时可能会超时）
     * 
     * @param nums 非负整数数组
     * @return 是否能够到达最后一个位置
     */
    public static boolean canJumpBacktracking(int[] nums) {
        // 参数校验
        if (nums == null || nums.length <= 1) {
            return true; // 如果数组为空或只有一个元素，已经在终点
        }
        
        return canJumpFromPositionBacktracking(0, nums);
    }
    
    /**
     * 辅助方法：使用回溯判断从位置pos是否可以到达最后一个位置
     * 
     * @param pos 当前位置
     * @param nums 非负整数数组
     * @return 是否能够到达最后一个位置
     */
    private static boolean canJumpFromPositionBacktracking(int pos, int[] nums) {
        // 基本情况：已经到达最后一个位置
        if (pos == nums.length - 1) {
            return true;
        }
        
        // 计算从当前位置可以到达的最远位置
        int furthestJump = Math.min(pos + nums[pos], nums.length - 1);
        
        // 尝试从当前位置跳到所有可能的位置
        for (int nextPos = furthestJump; nextPos > pos; nextPos--) {
            // 优先尝试跳得更远，这样可能更快找到解
            if (canJumpFromPositionBacktracking(nextPos, nums)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 打印数组
     */
    public static void printArray(int[] arr) {
        System.out.println(Arrays.stream(arr)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", ", "[", "]")));
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest(int[] nums) {
        // 测试贪心算法
        long startTime = System.currentTimeMillis();
        boolean result1 = canJumpGreedy(nums);
        long endTime = System.currentTimeMillis();
        System.out.println("贪心算法结果: " + result1);
        System.out.println("贪心算法耗时: " + (endTime - startTime) + "ms");
        
        // 测试动态规划自底向上
        startTime = System.currentTimeMillis();
        boolean result2 = canJumpDynamicProgrammingBottomUp(nums);
        endTime = System.currentTimeMillis();
        System.out.println("动态规划(自底向上)结果: " + result2);
        System.out.println("动态规划(自底向上)耗时: " + (endTime - startTime) + "ms");
        
        // 注意：以下两种方法在大规模数组上可能会超时，所以只在小规模数组上测试
        if (nums.length <= 1000) {
            // 测试动态规划自顶向下
            startTime = System.currentTimeMillis();
            boolean result3 = canJumpDynamicProgrammingTopDown(nums);
            endTime = System.currentTimeMillis();
            System.out.println("动态规划(自顶向下)结果: " + result3);
            System.out.println("动态规划(自顶向下)耗时: " + (endTime - startTime) + "ms");
            
            // 测试回溯算法
            if (nums.length <= 30) { // 回溯算法在小数据量下才能快速运行
                startTime = System.currentTimeMillis();
                boolean result4 = canJumpBacktracking(nums);
                endTime = System.currentTimeMillis();
                System.out.println("回溯算法结果: " + result4);
                System.out.println("回溯算法耗时: " + (endTime - startTime) + "ms");
            } else {
                System.out.println("数组过大，跳过回溯算法测试");
            }
        } else {
            System.out.println("数组过大，跳过动态规划(自顶向下)和回溯算法测试");
        }
    }
    
    /**
     * 生成测试用例
     */
    public static int[] generateTestCase(int n, boolean canReachEnd) {
        int[] nums = new int[n];
        Random rand = new Random();
        
        if (canReachEnd) {
            // 生成可以到达终点的数组
            for (int i = 0; i < n - 1; i++) {
                // 确保可以到达终点，当前位置的值至少为n-1-i
                nums[i] = Math.max(rand.nextInt(5) + 1, n - 1 - i);
            }
        } else {
            // 生成无法到达终点的数组
            // 创建一个0，使得无法越过
            int zeroPosition = rand.nextInt(n - 1) + 1; // 确保0不在最后一个位置
            nums[zeroPosition] = 0;
            
            // 填充0之前的位置
            for (int i = 0; i < zeroPosition; i++) {
                // 确保无法越过0
                nums[i] = Math.min(rand.nextInt(5) + 1, zeroPosition - i);
            }
            
            // 填充0之后的位置
            for (int i = zeroPosition + 1; i < n; i++) {
                nums[i] = rand.nextInt(5) + 1;
            }
        }
        
        nums[n - 1] = 0; // 最后一个元素不影响
        return nums;
    }
    
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {2, 3, 1, 1, 4};
        System.out.println("测试用例1:");
        System.out.print("nums = ");
        printArray(nums1);
        System.out.println("贪心算法结果: " + canJumpGreedy(nums1)); // 预期输出: true
        System.out.println("动态规划(自顶向下)结果: " + canJumpDynamicProgrammingTopDown(nums1)); // 预期输出: true
        System.out.println("动态规划(自底向上)结果: " + canJumpDynamicProgrammingBottomUp(nums1)); // 预期输出: true
        System.out.println("回溯算法结果: " + canJumpBacktracking(nums1)); // 预期输出: true
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {3, 2, 1, 0, 4};
        System.out.println("测试用例2:");
        System.out.print("nums = ");
        printArray(nums2);
        System.out.println("贪心算法结果: " + canJumpGreedy(nums2)); // 预期输出: false
        System.out.println("动态规划(自顶向下)结果: " + canJumpDynamicProgrammingTopDown(nums2)); // 预期输出: false
        System.out.println("动态规划(自底向上)结果: " + canJumpDynamicProgrammingBottomUp(nums2)); // 预期输出: false
        System.out.println("回溯算法结果: " + canJumpBacktracking(nums2)); // 预期输出: false
        System.out.println();
        
        // 测试用例3 - 边界情况：只有一个元素
        int[] nums3 = {0};
        System.out.println("测试用例3（单元素数组）:");
        System.out.print("nums = ");
        printArray(nums3);
        System.out.println("贪心算法结果: " + canJumpGreedy(nums3)); // 预期输出: true
        System.out.println("动态规划(自顶向下)结果: " + canJumpDynamicProgrammingTopDown(nums3)); // 预期输出: true
        System.out.println("动态规划(自底向上)结果: " + canJumpDynamicProgrammingBottomUp(nums3)); // 预期输出: true
        System.out.println("回溯算法结果: " + canJumpBacktracking(nums3)); // 预期输出: true
        System.out.println();
        
        // 测试用例4 - 边界情况：全是0
        int[] nums4 = {0, 0, 0, 0, 0};
        System.out.println("测试用例4（全是0）:");
        System.out.print("nums = ");
        printArray(nums4);
        System.out.println("贪心算法结果: " + canJumpGreedy(nums4)); // 预期输出: false (除了第一个元素为0且只有一个元素的情况)
        System.out.println("动态规划(自顶向下)结果: " + canJumpDynamicProgrammingTopDown(nums4)); // 预期输出: false
        System.out.println("动态规划(自底向上)结果: " + canJumpDynamicProgrammingBottomUp(nums4)); // 预期输出: false
        System.out.println("回溯算法结果: " + canJumpBacktracking(nums4)); // 预期输出: false
        System.out.println();
        
        // 测试用例5 - 边界情况：可以一次跳到终点
        int[] nums5 = {10, 0, 0, 0, 0};
        System.out.println("测试用例5（可以一次跳到终点）:");
        System.out.print("nums = ");
        printArray(nums5);
        System.out.println("贪心算法结果: " + canJumpGreedy(nums5)); // 预期输出: true
        System.out.println("动态规划(自顶向下)结果: " + canJumpDynamicProgrammingTopDown(nums5)); // 预期输出: true
        System.out.println("动态规划(自底向上)结果: " + canJumpDynamicProgrammingBottomUp(nums5)); // 预期输出: true
        System.out.println("回溯算法结果: " + canJumpBacktracking(nums5)); // 预期输出: true
        System.out.println();
        
        // 性能测试 - 小规模数组
        System.out.println("小规模数组性能测试（可以到达终点）:");
        int[] smallArray1 = generateTestCase(100, true);
        performanceTest(smallArray1);
        System.out.println();
        
        System.out.println("小规模数组性能测试（无法到达终点）:");
        int[] smallArray2 = generateTestCase(100, false);
        performanceTest(smallArray2);
        System.out.println();
        
        // 性能测试 - 大规模数组 - 只测试贪心算法，因为其他算法在大规模数组上会很慢
        System.out.println("大规模数组性能测试（只测试贪心算法）:");
        int[] largeArray = generateTestCase(10000, true);
        long startTime = System.currentTimeMillis();
        boolean result = canJumpGreedy(largeArray);
        long endTime = System.currentTimeMillis();
        System.out.println("贪心算法结果: " + result);
        System.out.println("贪心算法耗时: " + (endTime - startTime) + "ms");
    }
}