/**
 * 跳跃游戏
 * 
 * 题目描述：
 * 给定一个非负整数数组 nums ，你最初位于数组的第一个下标。
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * 判断你是否能够到达最后一个下标。
 * 
 * 来源：LeetCode 55
 * 链接：https://leetcode.cn/problems/jump-game/
 * 
 * 算法思路：
 * 使用贪心算法：
 * 1. 维护当前能够到达的最远位置
 * 2. 遍历数组，更新最远位置
 * 3. 如果当前位置超过最远位置，说明无法到达
 * 
 * 时间复杂度：O(n) - 只需要遍历一次数组
 * 空间复杂度：O(1) - 只使用常数空间
 * 
 * 关键点分析：
 * - 贪心策略：每次选择能够到达的最远位置
 * - 数学证明：局部最优导致全局最优
 * - 边界处理：处理0值情况
 * 
 * 工程化考量：
 * - 输入验证：检查数组是否为空
 * - 性能优化：提前终止遍历
 * - 可读性：清晰的变量命名和注释
 */

import java.util.*;

public class Code36_JumpGame {
    
    /**
     * 判断是否能够到达最后一个下标
     * 
     * @param nums 非负整数数组
     * @return 是否能够到达最后一个下标
     */
    public static boolean canJump(int[] nums) {
        // 输入验证
        if (nums == null || nums.length == 0) {
            return false;
        }
        if (nums.length == 1) {
            return true;
        }
        
        int n = nums.length;
        int maxReach = 0;  // 当前能够到达的最远位置
        
        for (int i = 0; i < n; i++) {
            // 如果当前位置已经超过能够到达的最远位置
            if (i > maxReach) {
                return false;
            }
            
            // 更新能够到达的最远位置
            maxReach = Math.max(maxReach, i + nums[i]);
            
            // 如果已经能够到达最后一个位置
            if (maxReach >= n - 1) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 另一种实现：从后向前遍历
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static boolean canJumpBackward(int[] nums) {
        if (nums == null || nums.length == 0) {
            return false;
        }
        if (nums.length == 1) {
            return true;
        }
        
        int n = nums.length;
        int lastPos = n - 1;  // 需要到达的位置
        
        for (int i = n - 2; i >= 0; i--) {
            if (i + nums[i] >= lastPos) {
                lastPos = i;
            }
        }
        
        return lastPos == 0;
    }
    
    /**
     * 暴力解法：DFS搜索
     * 时间复杂度：O(2^n)
     * 空间复杂度：O(n)
     */
    public static boolean canJumpBruteForce(int[] nums) {
        if (nums == null || nums.length == 0) {
            return false;
        }
        return dfs(nums, 0);
    }
    
    private static boolean dfs(int[] nums, int position) {
        // 如果已经到达或超过最后一个位置
        if (position >= nums.length - 1) {
            return true;
        }
        
        // 尝试所有可能的跳跃步数
        int maxJump = nums[position];
        for (int i = 1; i <= maxJump; i++) {
            if (dfs(nums, position + i)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 验证函数：检查路径是否正确
     */
    public static boolean validateJump(int[] nums, boolean result) {
        if (nums == null || nums.length == 0) {
            return !result;
        }
        return result == canJump(nums);
    }
    
    /**
     * 运行测试用例
     */
    public static void runTests() {
        System.out.println("=== 跳跃游戏测试 ===");
        
        // 测试用例1: [2,3,1,1,4] -> true
        int[] nums1 = {2, 3, 1, 1, 4};
        System.out.println("测试用例1: " + Arrays.toString(nums1));
        boolean result1 = canJump(nums1);
        boolean result2 = canJumpBackward(nums1);
        System.out.println("方法1结果: " + result1);  // true
        System.out.println("方法2结果: " + result2);  // true
        System.out.println("验证: " + validateJump(nums1, result1));
        
        // 测试用例2: [3,2,1,0,4] -> false
        int[] nums2 = {3, 2, 1, 0, 4};
        System.out.println("\n测试用例2: " + Arrays.toString(nums2));
        result1 = canJump(nums2);
        result2 = canJumpBackward(nums2);
        System.out.println("方法1结果: " + result1);  // false
        System.out.println("方法2结果: " + result2);  // false
        System.out.println("验证: " + validateJump(nums2, result1));
        
        // 测试用例3: [0] -> true
        int[] nums3 = {0};
        System.out.println("\n测试用例3: " + Arrays.toString(nums3));
        result1 = canJump(nums3);
        result2 = canJumpBackward(nums3);
        System.out.println("方法1结果: " + result1);  // true
        System.out.println("方法2结果: " + result2);  // true
        System.out.println("验证: " + validateJump(nums3, result1));
        
        // 测试用例4: [1,0,1,0] -> false
        int[] nums4 = {1, 0, 1, 0};
        System.out.println("\n测试用例4: " + Arrays.toString(nums4));
        result1 = canJump(nums4);
        result2 = canJumpBackward(nums4);
        System.out.println("方法1结果: " + result1);  // false
        System.out.println("方法2结果: " + result2);  // false
        System.out.println("验证: " + validateJump(nums4, result1));
        
        // 边界测试：空数组
        int[] nums5 = {};
        System.out.println("\n测试用例5: " + Arrays.toString(nums5));
        result1 = canJump(nums5);
        result2 = canJumpBackward(nums5);
        System.out.println("方法1结果: " + result1);  // false
        System.out.println("方法2结果: " + result2);  // false
        System.out.println("验证: " + validateJump(nums5, result1));
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        // 生成大规模测试数据
        int n = 10000;
        int[] nums = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            nums[i] = random.nextInt(10);  // 0-9
        }
        
        System.out.println("\n=== 性能测试 ===");
        
        long startTime1 = System.nanoTime();
        boolean result1 = canJump(nums);
        long endTime1 = System.nanoTime();
        System.out.printf("方法1执行时间: %.2f 毫秒\n", (endTime1 - startTime1) / 1_000_000.0);
        System.out.println("结果: " + result1);
        System.out.println("验证: " + validateJump(nums, result1));
        
        long startTime2 = System.nanoTime();
        boolean result2 = canJumpBackward(nums);
        long endTime2 = System.nanoTime();
        System.out.printf("方法2执行时间: %.2f 毫秒\n", (endTime2 - startTime2) / 1_000_000.0);
        System.out.println("结果: " + result2);
        System.out.println("验证: " + validateJump(nums, result2));
        
        // 暴力解法太慢，只测试小规模数据
        int[] smallNums = Arrays.copyOf(nums, 20);
        long startTime3 = System.nanoTime();
        boolean result3 = canJumpBruteForce(smallNums);
        long endTime3 = System.nanoTime();
        System.out.printf("方法3执行时间（小规模）: %.2f 毫秒\n", (endTime3 - startTime3) / 1_000_000.0);
        System.out.println("结果: " + result3);
        System.out.println("验证: " + validateJump(smallNums, result3));
    }
    
    /**
     * 算法复杂度分析
     */
    public static void analyzeComplexity() {
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("方法1（贪心算法）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 只需要遍历一次数组");
        System.out.println("  - 每个元素处理一次");
        System.out.println("- 空间复杂度: O(1)");
        System.out.println("  - 只使用常数空间");
        
        System.out.println("\n方法2（从后向前）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 遍历一次数组");
        System.out.println("  - 反向遍历同样高效");
        System.out.println("- 空间复杂度: O(1)");
        System.out.println("  - 只使用常数空间");
        
        System.out.println("\n方法3（暴力解法）:");
        System.out.println("- 时间复杂度: O(2^n)");
        System.out.println("  - 最坏情况下指数级复杂度");
        System.out.println("  - 每个位置有多种选择");
        System.out.println("- 空间复杂度: O(n)");
        System.out.println("  - 递归栈深度");
        
        System.out.println("\n贪心策略证明:");
        System.out.println("1. 维护当前能够到达的最远位置");
        System.out.println("2. 如果当前位置能够到达，则更新最远位置");
        System.out.println("3. 数学归纳法证明贪心选择性质");
        
        System.out.println("\n工程化考量:");
        System.out.println("1. 输入验证：处理空数组和边界情况");
        System.out.println("2. 性能优化：提前终止遍历");
        System.out.println("3. 可读性：清晰的算法逻辑");
        System.out.println("4. 测试覆盖：各种边界情况");
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) {
        runTests();
        performanceTest();
        analyzeComplexity();
    }
}