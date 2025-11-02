package class049;

/**
 * 可获得的最大点数问题解决方案
 * 
 * 问题描述：
 * 几张卡牌排成一行，每张卡牌都有一个对应的点数。点数由整数数组 cardPoints 给出。
 * 每次行动，你可以从行的开头或者末尾拿一张卡牌，最终你必须正好拿 k 张卡牌。
 * 你的点数就是你拿到手中的所有卡牌的点数之和。
 * 给你一个整数数组 cardPoints 和整数 k，请你返回可以获得的最大点数。
 * 
 * 解题思路：
 * 这是一个转换思路的滑动窗口问题：
 * 1. 问题等价于：从数组中拿走 k 个数，使得拿走的数之和最大
 * 2. 由于只能从两端拿，所以剩下的 n-k 个数必然是连续的子数组
 * 3. 要使拿走的数之和最大，就要使剩下的连续子数组之和最小
 * 4. 使用滑动窗口找出长度为 n-k 的子数组的最小和
 * 5. 最大点数 = 总和 - 最小子数组和
 * 
 * 算法复杂度分析：
 * 时间复杂度: O(n) - 需要遍历数组两次
 * 空间复杂度: O(1) - 只使用常数额外空间
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 相关题目链接：
 * LeetCode 1423. 可获得的最大点数
 * https://leetcode.cn/problems/maximum-points-you-can-obtain-from-cards/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 可获得的最大点数
 *    https://www.nowcoder.com/practice/1266570c4a06487981ed50e84e8b720d
 * 2. LintCode 1423. 可获得的最大点数
 *    https://www.lintcode.com/problem/1423/
 * 3. HackerRank - Maximum Points You Can Obtain
 *    https://www.hackerrank.com/challenges/maximum-points-you-can-obtain/problem
 * 4. CodeChef - CARDGAME - Card Game
 *    https://www.codechef.com/problems/CARDGAME
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1886 滑动窗口
 *    https://www.luogu.com.cn/problem/P1886
 * 7. 杭电OJ 4193 Sliding Window
 *    http://acm.hdu.edu.cn/showproblem.php?pid=4193
 * 8. POJ 2823 Sliding Window
 *    http://poj.org/problem?id=2823
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空数组、k为负数或0等边界情况
 * 2. 性能优化：使用滑动窗口避免重复计算，达到线性时间复杂度
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 */
public class Code16_MaximumPointsYouCanObtain {
    
    /**
     * 计算可获得的最大点数
     * 
     * @param cardPoints 卡牌点数数组
     * @param k          需要拿取的卡牌张数
     * @return 可获得的最大点数
     */
    public static int maxScore(int[] cardPoints, int k) {
        // 异常情况处理
        if (cardPoints == null || cardPoints.length == 0 || k <= 0) {
            return 0;
        }
        
        int n = cardPoints.length;
        // 如果k大于等于数组长度，拿走所有卡牌
        if (k >= n) {
            int sum = 0;
            for (int point : cardPoints) {
                sum += point;
            }
            return sum;
        }
        
        // 计算总和
        int totalSum = 0;
        for (int point : cardPoints) {
            totalSum += point;
        }
        
        // 滑动窗口大小为 n-k，找出子数组的最小和
        // 由于只能从两端拿牌，所以剩下的n-k张牌必然是连续的子数组
        int windowSize = n - k;
        int windowSum = 0;
        
        // 初始化第一个窗口（前windowSize个元素）
        for (int i = 0; i < windowSize; i++) {
            windowSum += cardPoints[i];
        }
        int minWindowSum = windowSum;
        
        // 滑动窗口，窗口大小为windowSize
        for (int i = windowSize; i < n; i++) {
            // 添加新元素（窗口右边界），移除旧元素（窗口左边界）
            windowSum += cardPoints[i] - cardPoints[i - windowSize];
            // 更新最小子数组和
            minWindowSum = Math.min(minWindowSum, windowSum);
        }
        
        // 最大点数 = 总和 - 最小子数组和
        // 因为拿走k张牌的最大点数等于总点数减去剩下连续n-k张牌的最小点数
        return totalSum - minWindowSum;
    }
    
    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] cardPoints1 = {1, 2, 3, 4, 5, 6, 1};
        int k1 = 3;
        int result1 = maxScore(cardPoints1, k1);
        System.out.println("卡牌点数: " + java.util.Arrays.toString(cardPoints1));
        System.out.println("拿取张数: " + k1);
        System.out.println("最大点数: " + result1);
        // 预期输出: 12 (拿取 1, 6, 5)
        // 解释：总点数22，剩下连续4张牌的最小点数是1+2+3+4=10，所以最大点数是22-10=12
        
        // 测试用例2
        int[] cardPoints2 = {2, 2, 2};
        int k2 = 2;
        int result2 = maxScore(cardPoints2, k2);
        System.out.println("\n卡牌点数: " + java.util.Arrays.toString(cardPoints2));
        System.out.println("拿取张数: " + k2);
        System.out.println("最大点数: " + result2);
        // 预期输出: 4
        // 解释：总点数6，剩下连续1张牌的最小点数是2，所以最大点数是6-2=4
        
        // 测试用例3
        int[] cardPoints3 = {9, 7, 7, 9, 7, 7, 9};
        int k3 = 7;
        int result3 = maxScore(cardPoints3, k3);
        System.out.println("\n卡牌点数: " + java.util.Arrays.toString(cardPoints3));
        System.out.println("拿取张数: " + k3);
        System.out.println("最大点数: " + result3);
        // 预期输出: 55 (拿取所有卡牌)
        // 解释：k等于数组长度，拿取所有卡牌，点数为55
        
        // 测试用例4：空数组
        int[] cardPoints4 = {};
        int k4 = 1;
        int result4 = maxScore(cardPoints4, k4);
        System.out.println("\n卡牌点数: " + java.util.Arrays.toString(cardPoints4));
        System.out.println("拿取张数: " + k4);
        System.out.println("最大点数: " + result4);
        // 预期输出: 0
    }
}