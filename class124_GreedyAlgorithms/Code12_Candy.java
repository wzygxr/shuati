package class093;

import java.util.Arrays;

/**
 * 分发糖果（Candy）
 * 题目来源：LeetCode 135
 * 题目链接：https://leetcode.cn/problems/candy/
 * 
 * 问题描述：
 * 老师想给孩子们分发糖果，有N个孩子站成了一条直线，每个孩子至少分配到1个糖果。
 * 相邻的孩子中，评分高的孩子必须获得更多的糖果。
 * 计算最少需要准备多少糖果。
 * 
 * 算法思路：
 * 使用贪心策略，两次遍历：
 * 1. 从左到右遍历，如果当前孩子评分比左边高，糖果数比左边多1
 * 2. 从右到左遍历，如果当前孩子评分比右边高，糖果数取当前值和右边值+1的最大值
 * 3. 最后统计所有糖果数之和
 * 
 * 时间复杂度：O(n) - 两次遍历数组
 * 空间复杂度：O(n) - 需要额外的糖果数组
 * 
 * 是否最优解：是。这是该问题的最优解法。
 * 
 * 适用场景：
 * 1. 分配问题，需要满足相邻约束条件
 * 2. 双向约束的最优化问题
 * 
 * 异常处理：
 * 1. 处理空数组情况
 * 2. 处理单元素数组
 * 
 * 工程化考量：
 * 1. 输入验证：检查数组是否为空
 * 2. 边界条件：处理单元素和双元素数组
 * 3. 性能优化：使用数组而不是列表提高性能
 * 
 * 相关题目：
 * 1. LeetCode 42. 接雨水 - 双向遍历的经典问题
 * 2. LeetCode 84. 柱状图中最大的矩形 - 单调栈应用
 * 3. LeetCode 406. 根据身高重建队列 - 贪心排序问题
 * 4. 牛客网 NC140 排序 - 各种排序算法实现
 * 5. LintCode 391. 数飞机 - 区间调度相关
 * 6. HackerRank - Jim and the Orders - 贪心调度问题
 * 7. CodeChef - TACHSTCK - 区间配对问题
 * 8. AtCoder ABC104C - All Green - 动态规划相关
 * 9. Codeforces 1363C - Game On Leaves - 博弈论相关
 * 10. POJ 3169 - Layout - 差分约束系统
 */
public class Code12_Candy {
    
    /**
     * 计算最少需要准备的糖果数量
     * 
     * @param ratings 孩子的评分数组
     * @return 最少需要的糖果数量
     */
    public static int candy(int[] ratings) {
        // 边界条件检查
        if (ratings == null || ratings.length == 0) {
            return 0;
        }
        
        int n = ratings.length;
        if (n == 1) {
            return 1; // 只有一个孩子，最少需要1个糖果
        }
        
        int[] candies = new int[n];
        Arrays.fill(candies, 1); // 每个孩子至少1个糖果
        
        // 从左到右遍历，处理递增序列
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }
        
        // 从右到左遍历，处理递减序列
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
        }
        
        // 统计总糖果数
        int totalCandies = 0;
        for (int candy : candies) {
            totalCandies += candy;
        }
        
        return totalCandies;
    }
    
    /**
     * 测试函数，验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1: 基本情况 - 递增序列
        int[] ratings1 = {1, 0, 2};
        int result1 = candy(ratings1);
        System.out.println("测试用例1:");
        System.out.print("评分数组: [");
        for (int i = 0; i < ratings1.length; i++) {
            System.out.print(ratings1[i]);
            if (i < ratings1.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("最少糖果数: " + result1);
        System.out.println("期望输出: 5");
        System.out.println();
        
        // 测试用例2: 基本情况 - 递减序列
        int[] ratings2 = {1, 2, 2};
        int result2 = candy(ratings2);
        System.out.println("测试用例2:");
        System.out.print("评分数组: [");
        for (int i = 0; i < ratings2.length; i++) {
            System.out.print(ratings2[i]);
            if (i < ratings2.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("最少糖果数: " + result2);
        System.out.println("期望输出: 4");
        System.out.println();
        
        // 测试用例3: 复杂情况 - 山峰形状
        int[] ratings3 = {1, 3, 2, 2, 1};
        int result3 = candy(ratings3);
        System.out.println("测试用例3:");
        System.out.print("评分数组: [");
        for (int i = 0; i < ratings3.length; i++) {
            System.out.print(ratings3[i]);
            if (i < ratings3.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("最少糖果数: " + result3);
        System.out.println("期望输出: 7");
        System.out.println();
        
        // 测试用例4: 边界情况 - 单元素数组
        int[] ratings4 = {5};
        int result4 = candy(ratings4);
        System.out.println("测试用例4:");
        System.out.print("评分数组: [");
        System.out.print(ratings4[0]);
        System.out.println("]");
        System.out.println("最少糖果数: " + result4);
        System.out.println("期望输出: 1");
        System.out.println();
        
        // 测试用例5: 边界情况 - 两个相同评分
        int[] ratings5 = {2, 2};
        int result5 = candy(ratings5);
        System.out.println("测试用例5:");
        System.out.print("评分数组: [");
        for (int i = 0; i < ratings5.length; i++) {
            System.out.print(ratings5[i]);
            if (i < ratings5.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("最少糖果数: " + result5);
        System.out.println("期望输出: 2");
        System.out.println();
        
        // 测试用例6: 复杂情况 - 长序列
        int[] ratings6 = {1, 2, 87, 87, 87, 2, 1};
        int result6 = candy(ratings6);
        System.out.println("测试用例6:");
        System.out.print("评分数组: [");
        for (int i = 0; i < ratings6.length; i++) {
            System.out.print(ratings6[i]);
            if (i < ratings6.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("最少糖果数: " + result6);
        System.out.println("期望输出: 13");
    }
}