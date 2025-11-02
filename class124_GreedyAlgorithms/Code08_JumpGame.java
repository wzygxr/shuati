package class093;

/**
 * 跳跃游戏（Jump Game）
 * 题目来源：LeetCode 55
 * 题目链接：https://leetcode.cn/problems/jump-game/
 * 
 * 问题描述：
 * 给定一个非负整数数组 nums，你最初位于数组的第一个位置。
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * 判断你是否能够到达最后一个位置。
 * 
 * 算法思路：
 * 使用贪心策略，维护当前能到达的最远位置。
 * 具体步骤：
 * 1. 遍历数组，对于每个位置，更新能到达的最远位置
 * 2. 如果最远位置超过或等于数组的最后一个位置，返回true
 * 3. 如果在遍历过程中发现当前位置已经无法到达（即当前位置大于能到达的最远位置），返回false
 * 
 * 时间复杂度：O(n)，其中n是数组长度，只需遍历数组一次
 * 空间复杂度：O(1)，只使用了常数额外空间
 * 
 * 是否最优解：是。贪心策略在此问题中能得到最优解。
 * 
 * 适用场景：
 * 1. 路径可达性问题
 * 2. 资源约束下的可达性判断
 * 
 * 异常处理：
 * 1. 处理空数组情况
 * 2. 处理数组长度为1的边界情况（已经在终点）
 * 
 * 工程化考量：
 * 1. 输入验证：检查数组是否为空
 * 2. 边界条件：处理单元素数组
 * 3. 性能优化：提前返回，一旦确定可以到达终点就立即返回
 */
public class Code08_JumpGame {
    
    /**
     * 判断是否能够到达数组的最后一个位置
     * 
     * @param nums 非负整数数组，每个元素表示在该位置可以跳跃的最大长度
     * @return 如果能够到达最后一个位置返回true，否则返回false
     */
    public static boolean canJump(int[] nums) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return false;
        }
        
        // 如果数组只有一个元素，已经在终点
        if (nums.length == 1) {
            return true;
        }
        
        int maxReach = 0; // 当前能到达的最远位置
        
        // 遍历数组中的每个位置
        for (int i = 0; i < nums.length; i++) {
            // 如果当前位置已经无法到达，直接返回false
            if (i > maxReach) {
                return false;
            }
            
            // 更新能到达的最远位置
            maxReach = Math.max(maxReach, i + nums[i]);
            
            // 如果最远位置已经可以到达或超过最后一个位置，直接返回true
            if (maxReach >= nums.length - 1) {
                return true;
            }
        }
        
        // 理论上不会执行到这里，因为前面已经有检查
        return maxReach >= nums.length - 1;
    }
    
    /**
     * 测试函数，验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1: 基本情况 - 能到达终点
        int[] nums1 = {2, 3, 1, 1, 4};
        boolean result1 = canJump(nums1);
        System.out.println("测试用例1:");
        System.out.print("数组: [");
        for (int i = 0; i < nums1.length; i++) {
            System.out.print(nums1[i]);
            if (i < nums1.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("能否到达最后一个位置: " + result1);
        System.out.println("期望输出: true");
        System.out.println();
        
        // 测试用例2: 基本情况 - 不能到达终点
        int[] nums2 = {3, 2, 1, 0, 4};
        boolean result2 = canJump(nums2);
        System.out.println("测试用例2:");
        System.out.print("数组: [");
        for (int i = 0; i < nums2.length; i++) {
            System.out.print(nums2[i]);
            if (i < nums2.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("能否到达最后一个位置: " + result2);
        System.out.println("期望输出: false");
        System.out.println();
        
        // 测试用例3: 边界情况 - 单元素数组
        int[] nums3 = {0};
        boolean result3 = canJump(nums3);
        System.out.println("测试用例3:");
        System.out.println("数组: [0]");
        System.out.println("能否到达最后一个位置: " + result3);
        System.out.println("期望输出: true");
        System.out.println();
        
        // 测试用例4: 边界情况 - 所有元素都是0
        int[] nums4 = {0, 0, 0, 0};
        boolean result4 = canJump(nums4);
        System.out.println("测试用例4:");
        System.out.print("数组: [");
        for (int i = 0; i < nums4.length; i++) {
            System.out.print(nums4[i]);
            if (i < nums4.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("能否到达最后一个位置: " + result4);
        System.out.println("期望输出: false");
        System.out.println();
        
        // 测试用例5: 复杂情况 - 大跳跃
        int[] nums5 = {2, 0, 0};
        boolean result5 = canJump(nums5);
        System.out.println("测试用例5:");
        System.out.print("数组: [");
        for (int i = 0; i < nums5.length; i++) {
            System.out.print(nums5[i]);
            if (i < nums5.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("能否到达最后一个位置: " + result5);
        System.out.println("期望输出: true");
    }
}