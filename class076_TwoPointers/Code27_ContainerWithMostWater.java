package class050;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * LeetCode 11. 盛最多水的容器 (Container With Most Water)
 * 
 * 题目描述:
 * 给定一个长度为 n 的整数数组 height。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i])。
 * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 * 返回容器可以储存的最大水量。
 * 说明：你不能倾斜容器。
 * 
 * 示例1:
 * 输入: [1,8,6,2,5,4,8,3,7]
 * 输出: 49
 * 解释: 图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。
 * 
 * 示例2:
 * 输入: height = [1,1]
 * 输出: 1
 * 
 * 提示:
 * n == height.length
 * 2 <= n <= 10^5
 * 0 <= height[i] <= 10^4
 * 
 * 题目链接: https://leetcode.com/problems/container-with-most-water/
 * 
 * 解题思路:
 * 这道题可以使用双指针的方法来解决：
 * 
 * 方法一（暴力解法）：
 * 遍历所有可能的两条线的组合，计算每个组合能容纳的水量，找出最大值。
 * 时间复杂度：O(n^2)，空间复杂度：O(1)
 * 
 * 方法二（双指针）：
 * 1. 初始化两个指针 left 和 right 分别指向数组的开头和结尾
 * 2. 计算当前指针所指两条线能容纳的水量：min(height[left], height[right]) * (right - left)
 * 3. 更新最大水量
 * 4. 移动较短的那条线对应的指针（因为如果移动较长的线，容纳的水量只会更小）
 * 5. 重复步骤2-4，直到两个指针相遇
 * 时间复杂度：O(n)，空间复杂度：O(1)
 * 
 * 最优解是方法二，时间复杂度 O(n)，空间复杂度 O(1)。
 */

public class Code27_ContainerWithMostWater {
    
    /**
     * 解法一: 暴力解法（不推荐，可能会超时）
     * 
     * @param height 输入数组
     * @return 最大盛水量
     * @throws IllegalArgumentException 如果输入数组为null或长度小于2
     */
    public static int maxAreaBruteForce(int[] height) {
        // 参数校验
        if (height == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        if (height.length < 2) {
            throw new IllegalArgumentException("输入数组长度必须至少为2");
        }
        
        int maxArea = 0;
        // 遍历所有可能的两条线的组合
        for (int i = 0; i < height.length; i++) {
            for (int j = i + 1; j < height.length; j++) {
                // 计算当前组合的盛水量
                int currentArea = Math.min(height[i], height[j]) * (j - i);
                // 更新最大盛水量
                maxArea = Math.max(maxArea, currentArea);
            }
        }
        return maxArea;
    }
    
    /**
     * 解法二: 双指针（最优解）
     * 
     * @param height 输入数组
     * @return 最大盛水量
     */
    public static int maxArea(int[] height) {
        // 参数校验
        if (height == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        if (height.length < 2) {
            throw new IllegalArgumentException("输入数组长度必须至少为2");
        }
        
        int maxArea = 0;
        int left = 0;  // 左指针，初始指向数组开头
        int right = height.length - 1;  // 右指针，初始指向数组结尾
        
        while (left < right) {
            // 计算当前盛水量
            int currentHeight = Math.min(height[left], height[right]);
            int currentWidth = right - left;
            int currentArea = currentHeight * currentWidth;
            
            // 更新最大盛水量
            maxArea = Math.max(maxArea, currentArea);
            
            // 移动较短的那条线对应的指针
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxArea;
    }
    
    /**
     * 解法三: 双指针优化版
     * 跳过相同高度的柱子，减少不必要的计算
     * 
     * @param height 输入数组
     * @return 最大盛水量
     */
    public static int maxAreaOptimized(int[] height) {
        // 参数校验
        if (height == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        if (height.length < 2) {
            throw new IllegalArgumentException("输入数组长度必须至少为2");
        }
        
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;
        
        while (left < right) {
            // 计算当前盛水量
            int currentHeight = Math.min(height[left], height[right]);
            int currentWidth = right - left;
            int currentArea = currentHeight * currentWidth;
            
            // 更新最大盛水量
            maxArea = Math.max(maxArea, currentArea);
            
            // 移动较短的那条线对应的指针
            // 跳过相同高度的柱子
            if (height[left] < height[right]) {
                int currentLeftHeight = height[left];
                while (left < right && height[left] <= currentLeftHeight) {
                    left++;
                }
            } else {
                int currentRightHeight = height[right];
                while (left < right && height[right] <= currentRightHeight) {
                    right--;
                }
            }
        }
        
        return maxArea;
    }
    
    /**
     * 测试函数
     */
    public static void test() {
        // 测试用例1
        int[] height1 = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int expected1 = 49;
        System.out.println("测试用例1:");
        System.out.print("输入数组: ");
        System.out.println(Arrays.toString(height1));
        int result1 = maxArea(height1);
        System.out.println("最大盛水量: " + result1);
        System.out.println("验证结果: " + (result1 == expected1));
        System.out.println();
        
        // 测试用例2
        int[] height2 = {1, 1};
        int expected2 = 1;
        System.out.println("测试用例2:");
        System.out.print("输入数组: ");
        System.out.println(Arrays.toString(height2));
        int result2 = maxArea(height2);
        System.out.println("最大盛水量: " + result2);
        System.out.println("验证结果: " + (result2 == expected2));
        System.out.println();
        
        // 测试用例3 - 边界情况：所有元素递增
        int[] height3 = {1, 2, 3, 4, 5};
        int expected3 = 6; // 由索引0和4的元素组成的容器
        System.out.println("测试用例3（递增数组）:");
        System.out.print("输入数组: ");
        System.out.println(Arrays.toString(height3));
        int result3 = maxArea(height3);
        System.out.println("最大盛水量: " + result3);
        System.out.println("验证结果: " + (result3 == expected3));
        System.out.println();
        
        // 测试用例4 - 边界情况：所有元素递减
        int[] height4 = {5, 4, 3, 2, 1};
        int expected4 = 6; // 由索引0和4的元素组成的容器
        System.out.println("测试用例4（递减数组）:");
        System.out.print("输入数组: ");
        System.out.println(Arrays.toString(height4));
        int result4 = maxArea(height4);
        System.out.println("最大盛水量: " + result4);
        System.out.println("验证结果: " + (result4 == expected4));
        System.out.println();
        
        // 测试用例5 - 边界情况：只有两个元素，高度不同
        int[] height5 = {3, 5};
        int expected5 = 3; // 由索引0和1的元素组成的容器
        System.out.println("测试用例5（两个元素）:");
        System.out.print("输入数组: ");
        System.out.println(Arrays.toString(height5));
        int result5 = maxArea(height5);
        System.out.println("最大盛水量: " + result5);
        System.out.println("验证结果: " + (result5 == expected5));
        System.out.println();
        
        // 测试用例6 - 边界情况：包含0
        int[] height6 = {0, 0, 0, 0, 0};
        int expected6 = 0; // 所有元素都是0，盛水量为0
        System.out.println("测试用例6（全零数组）:");
        System.out.print("输入数组: ");
        System.out.println(Arrays.toString(height6));
        int result6 = maxArea(height6);
        System.out.println("最大盛水量: " + result6);
        System.out.println("验证结果: " + (result6 == expected6));
        System.out.println();
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        // 创建一个大数组进行性能测试
        int size = 100000;
        int[] largeArray = new int[size];
        
        // 生成测试数据：交替增加和减少
        for (int i = 0; i < size; i++) {
            largeArray[i] = i % 100;  // 0-99循环
        }
        
        // 测试解法二的性能
        long startTime = System.nanoTime();
        int result2 = maxArea(largeArray);
        long endTime = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("解法二（双指针）耗时: " + duration + "ms, 最大盛水量: " + result2);
        
        // 测试解法三的性能
        startTime = System.nanoTime();
        int result3 = maxAreaOptimized(largeArray);
        endTime = System.nanoTime();
        duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("解法三（优化双指针）耗时: " + duration + "ms, 最大盛水量: " + result3);
        
        // 验证两种解法结果一致
        boolean resultsConsistent = (result2 == result3);
        System.out.println("所有解法结果一致: " + resultsConsistent);
    }
    
    /**
     * 边界条件测试
     */
    public static void boundaryTest() {
        try {
            // 测试null输入
            maxArea(null);
            System.out.println("边界测试失败：null输入没有抛出异常");
        } catch (IllegalArgumentException e) {
            System.out.println("边界测试通过：null输入正确抛出异常");
        }
        
        try {
            // 测试长度为1的输入
            maxArea(new int[]{5});
            System.out.println("边界测试失败：长度为1的输入没有抛出异常");
        } catch (IllegalArgumentException e) {
            System.out.println("边界测试通过：长度为1的输入正确抛出异常");
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== 测试用例 ===");
        test();
        
        System.out.println("=== 性能测试 ===");
        performanceTest();
        
        System.out.println("=== 边界条件测试 ===");
        boundaryTest();
    }
}