/**
 * 分发糖果
 * 
 * 题目描述：
 * 老师想给孩子们分发糖果，有 N 个孩子站成了一条直线，老师会根据每个孩子的表现，预先给他们评分。
 * 你需要按照以下要求，给这些孩子分发糖果：
 * 1. 每个孩子至少分配到 1 个糖果。
 * 2. 相邻的孩子中，评分高的孩子必须获得更多的糖果。
 * 
 * 来源：LeetCode 135
 * 链接：https://leetcode.cn/problems/candy/
 * 
 * 算法思路：
 * 使用贪心算法：
 * 1. 从左到右遍历，保证右边评分高的孩子糖果更多
 * 2. 从右到左遍历，保证左边评分高的孩子糖果更多
 * 3. 取两次遍历的最大值
 * 
 * 时间复杂度：O(n) - 两次遍历数组
 * 空间复杂度：O(n) - 存储糖果分配
 * 
 * 关键点分析：
 * - 贪心策略：分别处理左右关系
 * - 两次遍历：确保两个方向的条件都满足
 * - 边界处理：处理数组边界情况
 * 
 * 工程化考量：
 * - 输入验证：检查数组是否为空
 * - 性能优化：避免不必要的计算
 * - 可读性：清晰的变量命名和注释
 */

import java.util.*;

public class Code37_Candy {
    
    /**
     * 分发糖果的最小数量
     * 
     * @param ratings 孩子的评分数组
     * @return 最少需要的糖果数量
     */
    public static int candy(int[] ratings) {
        // 输入验证
        if (ratings == null || ratings.length == 0) {
            return 0;
        }
        if (ratings.length == 1) {
            return 1;
        }
        
        int n = ratings.length;
        int[] candies = new int[n];
        Arrays.fill(candies, 1);  // 每个孩子至少1个糖果
        
        // 从左到右遍历：保证右边评分高的孩子糖果更多
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }
        
        // 从右到左遍历：保证左边评分高的孩子糖果更多
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
        }
        
        // 计算总糖果数
        int total = 0;
        for (int candy : candies) {
            total += candy;
        }
        
        return total;
    }
    
    /**
     * 另一种实现：一次遍历法
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static int candyOnePass(int[] ratings) {
        if (ratings == null || ratings.length == 0) {
            return 0;
        }
        if (ratings.length == 1) {
            return 1;
        }
        
        int n = ratings.length;
        int total = 1;  // 第一个孩子至少1个糖果
        int up = 0, down = 0;
        int peak = 0;
        
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                up++;
                down = 0;
                peak = up;
                total += up + 1;
            } else if (ratings[i] == ratings[i - 1]) {
                up = 0;
                down = 0;
                peak = 0;
                total += 1;
            } else {
                up = 0;
                down++;
                total += down + (down > peak ? 1 : 0);
            }
        }
        
        return total;
    }
    
    /**
     * 暴力解法：模拟分配过程
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n)
     */
    public static int candyBruteForce(int[] ratings) {
        if (ratings == null || ratings.length == 0) {
            return 0;
        }
        
        int n = ratings.length;
        int[] candies = new int[n];
        Arrays.fill(candies, 1);
        
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < n; i++) {
                if (i > 0 && ratings[i] > ratings[i - 1] && candies[i] <= candies[i - 1]) {
                    candies[i] = candies[i - 1] + 1;
                    changed = true;
                }
                if (i < n - 1 && ratings[i] > ratings[i + 1] && candies[i] <= candies[i + 1]) {
                    candies[i] = candies[i + 1] + 1;
                    changed = true;
                }
            }
        }
        
        int total = 0;
        for (int candy : candies) {
            total += candy;
        }
        return total;
    }
    
    /**
     * 验证函数：检查糖果分配是否满足条件
     */
    public static boolean validateCandy(int[] ratings, int result) {
        if (ratings == null || ratings.length == 0) {
            return result == 0;
        }
        
        // 重新计算糖果分配进行验证
        int n = ratings.length;
        int[] candies = new int[n];
        Arrays.fill(candies, 1);
        
        // 从左到右
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }
        
        // 从右到左
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
        }
        
        int total = 0;
        for (int candy : candies) {
            total += candy;
        }
        
        return total == result;
    }
    
    /**
     * 运行测试用例
     */
    public static void runTests() {
        System.out.println("=== 分发糖果测试 ===");
        
        // 测试用例1: [1,0,2] -> 5
        int[] ratings1 = {1, 0, 2};
        System.out.println("测试用例1: " + Arrays.toString(ratings1));
        int result1 = candy(ratings1);
        int result2 = candyOnePass(ratings1);
        System.out.println("方法1结果: " + result1);  // 5
        System.out.println("方法2结果: " + result2);  // 5
        System.out.println("验证: " + validateCandy(ratings1, result1));
        
        // 测试用例2: [1,2,2] -> 4
        int[] ratings2 = {1, 2, 2};
        System.out.println("\n测试用例2: " + Arrays.toString(ratings2));
        result1 = candy(ratings2);
        result2 = candyOnePass(ratings2);
        System.out.println("方法1结果: " + result1);  // 4
        System.out.println("方法2结果: " + result2);  // 4
        System.out.println("验证: " + validateCandy(ratings2, result1));
        
        // 测试用例3: [1,3,2,2,1] -> 7
        int[] ratings3 = {1, 3, 2, 2, 1};
        System.out.println("\n测试用例3: " + Arrays.toString(ratings3));
        result1 = candy(ratings3);
        result2 = candyOnePass(ratings3);
        System.out.println("方法1结果: " + result1);  // 7
        System.out.println("方法2结果: " + result2);  // 7
        System.out.println("验证: " + validateCandy(ratings3, result1));
        
        // 测试用例4: [1] -> 1
        int[] ratings4 = {1};
        System.out.println("\n测试用例4: " + Arrays.toString(ratings4));
        result1 = candy(ratings4);
        result2 = candyOnePass(ratings4);
        System.out.println("方法1结果: " + result1);  // 1
        System.out.println("方法2结果: " + result2);  // 1
        System.out.println("验证: " + validateCandy(ratings4, result1));
        
        // 边界测试：空数组
        int[] ratings5 = {};
        System.out.println("\n测试用例5: " + Arrays.toString(ratings5));
        result1 = candy(ratings5);
        result2 = candyOnePass(ratings5);
        System.out.println("方法1结果: " + result1);  // 0
        System.out.println("方法2结果: " + result2);  // 0
        System.out.println("验证: " + validateCandy(ratings5, result1));
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        // 生成大规模测试数据
        int n = 10000;
        int[] ratings = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            ratings[i] = random.nextInt(10);  // 0-9
        }
        
        System.out.println("\n=== 性能测试 ===");
        
        long startTime1 = System.nanoTime();
        int result1 = candy(ratings);
        long endTime1 = System.nanoTime();
        System.out.printf("方法1执行时间: %.2f 毫秒\n", (endTime1 - startTime1) / 1_000_000.0);
        System.out.println("结果: " + result1);
        System.out.println("验证: " + validateCandy(ratings, result1));
        
        long startTime2 = System.nanoTime();
        int result2 = candyOnePass(ratings);
        long endTime2 = System.nanoTime();
        System.out.printf("方法2执行时间: %.2f 毫秒\n", (endTime2 - startTime2) / 1_000_000.0);
        System.out.println("结果: " + result2);
        System.out.println("验证: " + validateCandy(ratings, result2));
        
        // 暴力解法太慢，只测试小规模数据
        int[] smallRatings = Arrays.copyOf(ratings, 100);
        long startTime3 = System.nanoTime();
        int result3 = candyBruteForce(smallRatings);
        long endTime3 = System.nanoTime();
        System.out.printf("方法3执行时间（小规模）: %.2f 毫秒\n", (endTime3 - startTime3) / 1_000_000.0);
        System.out.println("结果: " + result3);
        System.out.println("验证: " + validateCandy(smallRatings, result3));
    }
    
    /**
     * 算法复杂度分析
     */
    public static void analyzeComplexity() {
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("方法1（两次遍历）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 两次遍历数组");
        System.out.println("  - 总体线性时间复杂度");
        System.out.println("- 空间复杂度: O(n)");
        System.out.println("  - 需要存储糖果分配数组");
        
        System.out.println("\n方法2（一次遍历）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 一次遍历数组");
        System.out.println("  - 维护上升下降状态");
        System.out.println("- 空间复杂度: O(1)");
        System.out.println("  - 只使用常数空间");
        
        System.out.println("\n方法3（暴力解法）:");
        System.out.println("- 时间复杂度: O(n^2)");
        System.out.println("  - 最坏情况下需要多次遍历");
        System.out.println("  - 每次调整可能影响相邻元素");
        System.out.println("- 空间复杂度: O(n)");
        System.out.println("  - 需要存储糖果分配");
        
        System.out.println("\n贪心策略证明:");
        System.out.println("1. 分别处理左右两个方向的约束");
        System.out.println("2. 两次遍历确保两个条件都满足");
        System.out.println("3. 取最大值保证两个方向的最优性");
        
        System.out.println("\n工程化考量:");
        System.out.println("1. 输入验证：处理空数组和边界情况");
        System.out.println("2. 性能优化：避免暴力解法");
        System.out.println("3. 可读性：清晰的算法逻辑");
        System.out.println("4. 测试覆盖：各种评分模式");
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