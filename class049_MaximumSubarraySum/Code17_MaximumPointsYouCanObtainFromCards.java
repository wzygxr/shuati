package class071;

// LeetCode 1423. 可获得的最大点数
// 几张卡牌排成一行，每张卡牌都有一个对应的点数。点数由整数数组 cardPoints 给出。
// 每次行动，你可以从行的开头或者结尾拿一张卡牌。最终你必须正好拿 k 张卡牌。
// 你的点数就是你拿到手中的所有卡牌的点数之和。
// 给你一个整数数组 cardPoints 和整数 k，请你返回可以获得的最大点数。
// 测试链接 : https://leetcode.cn/problems/maximum-points-you-can-obtain-from-cards/

/**
 * 解题思路:
 * 这是一个滑动窗口问题的逆向思维应用。我们可以将问题转化为：
 * 找到长度为 n-k 的最小子数组和，然后用总和减去这个最小和就是最大点数。
 * 
 * 核心思想:
 * 1. 计算整个数组的总和
 * 2. 问题转化为：找到长度为 n-k 的连续子数组，使其和最小
 * 3. 用总和减去这个最小和就是最大点数
 * 4. 使用滑动窗口来找到长度为 n-k 的最小子数组和
 * 
 * 时间复杂度: O(n) - 需要遍历数组两次（计算总和和滑动窗口）
 * 空间复杂度: O(1) - 只需要常数个变量存储状态
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么可以转化为找长度为 n-k 的最小子数组和？
 *    - 因为拿k张牌的最大点数等价于不拿n-k张牌的最小点数
 *    - 剩下的n-k张牌必须是连续的（因为只能从两端拿牌）
 * 2. 滑动窗口的大小为什么是 n-k？
 *    - 我们要找不拿的n-k张牌，它们必须是连续的
 *    - 这个连续子数组的和最小，意味着我们拿的k张牌的和最大
 * 
 * 工程化考量:
 * 1. 异常处理：k大于数组长度、空数组等情况
 * 2. 边界处理：k等于0或等于数组长度的情况
 * 3. 性能优化：使用滑动窗口避免重复计算
 */

import java.util.Arrays;

public class Code17_MaximumPointsYouCanObtainFromCards {
    
    public static int maxScore(int[] cardPoints, int k) {
        // 异常防御
        if (cardPoints == null || cardPoints.length == 0 || k <= 0) {
            return 0;
        }
        
        int n = cardPoints.length;
        
        // 如果k大于等于数组长度，直接返回总和
        if (k >= n) {
            return Arrays.stream(cardPoints).sum();
        }
        
        // 计算整个数组的总和
        int totalSum = 0;
        for (int point : cardPoints) {
            totalSum += point;
        }
        
        // 如果k等于0，返回0（实际上k>0，这里是为了完整性）
        if (k == 0) {
            return 0;
        }
        
        // 滑动窗口大小：n - k
        int windowSize = n - k;
        int windowSum = 0;
        
        // 计算第一个窗口的和
        for (int i = 0; i < windowSize; i++) {
            windowSum += cardPoints[i];
        }
        
        int minWindowSum = windowSum;
        
        // 滑动窗口，寻找最小窗口和
        for (int i = windowSize; i < n; i++) {
            windowSum = windowSum - cardPoints[i - windowSize] + cardPoints[i];
            minWindowSum = Math.min(minWindowSum, windowSum);
        }
        
        // 最大点数 = 总和 - 最小窗口和
        return totalSum - minWindowSum;
    }
    
    // 方法二：直接计算前缀后缀和（另一种思路）
    public static int maxScore2(int[] cardPoints, int k) {
        if (cardPoints == null || cardPoints.length == 0 || k <= 0) {
            return 0;
        }
        
        int n = cardPoints.length;
        if (k >= n) {
            return Arrays.stream(cardPoints).sum();
        }
        
        // 计算前缀和
        int[] prefixSum = new int[k + 1];
        for (int i = 0; i < k; i++) {
            prefixSum[i + 1] = prefixSum[i] + cardPoints[i];
        }
        
        // 计算后缀和
        int[] suffixSum = new int[k + 1];
        for (int i = 0; i < k; i++) {
            suffixSum[i + 1] = suffixSum[i] + cardPoints[n - 1 - i];
        }
        
        // 枚举从前面取i张，从后面取k-i张
        int maxScore = 0;
        for (int i = 0; i <= k; i++) {
            int currentScore = prefixSum[i] + suffixSum[k - i];
            maxScore = Math.max(maxScore, currentScore);
        }
        
        return maxScore;
    }
    
    // 新增：测试方法
    public static void main(String[] args) {
        // 测试用例1：正常情况
        int[] cards1 = {1, 2, 3, 4, 5, 6, 1};
        int k1 = 3;
        System.out.println("测试用例1:");
        System.out.println("卡牌点数: [1, 2, 3, 4, 5, 6, 1], k=3");
        System.out.println("最大点数（方法1）: " + maxScore(cards1, k1)); // 预期输出: 12
        System.out.println("最大点数（方法2）: " + maxScore2(cards1, k1)); // 预期输出: 12
        
        // 测试用例2：从两端取牌
        int[] cards2 = {2, 2, 2};
        int k2 = 2;
        System.out.println("\n测试用例2:");
        System.out.println("卡牌点数: [2, 2, 2], k=2");
        System.out.println("最大点数（方法1）: " + maxScore(cards2, k2)); // 预期输出: 4
        System.out.println("最大点数（方法2）: " + maxScore2(cards2, k2)); // 预期输出: 4
        
        // 测试用例3：k等于数组长度
        int[] cards3 = {9, 7, 7, 9, 7, 7, 9};
        int k3 = 7;
        System.out.println("\n测试用例3:");
        System.out.println("卡牌点数: [9, 7, 7, 9, 7, 7, 9], k=7");
        System.out.println("最大点数（方法1）: " + maxScore(cards3, k3)); // 预期输出: 55
        System.out.println("最大点数（方法2）: " + maxScore2(cards3, k3)); // 预期输出: 55
        
        // 测试用例4：包含负数（实际题目中点数都是正数）
        int[] cards4 = {1, 1000, 1};
        int k4 = 1;
        System.out.println("\n测试用例4:");
        System.out.println("卡牌点数: [1, 1000, 1], k=1");
        System.out.println("最大点数（方法1）: " + maxScore(cards4, k4)); // 预期输出: 1
        System.out.println("最大点数（方法2）: " + maxScore2(cards4, k4)); // 预期输出: 1
    }
    
    /*
     * 相关题目扩展:
     * 1. LeetCode 1423. 可获得的最大点数 - https://leetcode.cn/problems/maximum-points-you-can-obtain-from-cards/
     * 2. LeetCode 1658. 将 x 减到 0 的最小操作数 - https://leetcode.cn/problems/minimum-operations-to-reduce-x-to-zero/
     * 3. LeetCode 209. 长度最小的子数组 - https://leetcode.cn/problems/minimum-size-subarray-sum/
     * 4. LeetCode 862. 和至少为 K 的最短子数组 - https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
     * 5. LeetCode 1004. 最大连续1的个数 III - https://leetcode.cn/problems/max-consecutive-ones-iii/
     * 
     * 算法技巧总结:
     * 1. 逆向思维：将拿k张牌的最大点数转化为不拿n-k张牌的最小点数
     * 2. 滑动窗口：适用于固定窗口大小的最值问题
     * 3. 前缀后缀和：另一种直接计算的方法，枚举所有可能的取牌组合
     * 
     * 工程化思考:
     * 1. 方法1更简洁，方法2更直观，可以根据实际情况选择
     * 2. 对于大规模数据，两种方法的时间复杂度都是O(n)
     * 3. 在实际应用中，可能需要考虑数值溢出问题
     */
}

// C++ 实现
/*
#include <vector>
#include <algorithm>
#include <numeric>
using namespace std;

class Solution {
public:
    int maxScore(vector<int>& cardPoints, int k) {
        int n = cardPoints.size();
        if (n == 0 || k <= 0) return 0;
        if (k >= n) return accumulate(cardPoints.begin(), cardPoints.end(), 0);
        
        int totalSum = accumulate(cardPoints.begin(), cardPoints.end(), 0);
        int windowSize = n - k;
        
        int windowSum = 0;
        for (int i = 0; i < windowSize; i++) {
            windowSum += cardPoints[i];
        }
        
        int minWindowSum = windowSum;
        for (int i = windowSize; i < n; i++) {
            windowSum = windowSum - cardPoints[i - windowSize] + cardPoints[i];
            minWindowSum = min(minWindowSum, windowSum);
        }
        
        return totalSum - minWindowSum;
    }
};

// 时间复杂度: O(n)
// 空间复杂度: O(1)
// 是否最优解: 是
*/

// Python 实现
"""
from typing import List

class Solution:
    def maxScore(self, cardPoints: List[int], k: int) -> int:
        n = len(cardPoints)
        if n == 0 or k <= 0:
            return 0
        if k >= n:
            return sum(cardPoints)
            
        total_sum = sum(cardPoints)
        window_size = n - k
        
        window_sum = sum(cardPoints[:window_size])
        min_window_sum = window_sum
        
        for i in range(window_size, n):
            window_sum = window_sum - cardPoints[i - window_size] + cardPoints[i]
            min_window_sum = min(min_window_sum, window_sum)
            
        return total_sum - min_window_sum

# 时间复杂度: O(n)
# 空间复杂度: O(1)
# 是否最优解: 是
"""