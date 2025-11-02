package class129;

import java.util.Arrays;

/**
 * LeetCode 646. Maximum Length of Pair Chain
 * 
 * 题目描述：
 * 给出 n 个数对 pairs，其中 pairs[i] = [left_i, right_i] 且 left_i < right_i。
 * 现在，我们定义一种“跟随”关系，当且仅当 b < c 时，数对 [c, d] 可以跟在 [a, b] 后面。
 * 我们可以构造一个数对链，链中每两个相邻的数对都满足“跟随”关系。
 * 找出并返回能够形成的最长数对链的长度。
 * 
 * 解题思路：
 * 这是一个经典的贪心算法问题，类似于活动选择问题。
 * 
 * 算法步骤：
 * 1. 将所有数对按结束值排序
 * 2. 使用贪心策略：总是选择结束值最小的数对
 * 3. 遍历排序后的数对，统计可以组成的最长链长度
 * 
 * 贪心策略的正确性：
 * 选择结束值最小的数对可以为后续数对留下更多空间，从而最大化链的长度。
 * 
 * 时间复杂度：O(n * log n)
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * - LeetCode 435. 无重叠区间 (贪心)
 * - LeetCode 1353. 最多可以参加的会议数目 (贪心)
 * - LeetCode 1235. 最大盈利的工作调度 (动态规划 + 二分查找)
 */
public class LeetCode646_MaximumLengthOfPairChain {
    
    /**
     * 计算最长数对链的长度
     * 
     * @param pairs 数对数组
     * @return 最长数对链的长度
     */
    public static int findLongestChain(int[][] pairs) {
        // 边界情况处理
        if (pairs == null || pairs.length == 0) {
            return 0;
        }
        
        int n = pairs.length;
        
        // 按结束值排序
        Arrays.sort(pairs, (a, b) -> a[1] - b[1]);
        
        // 初始化计数器和上一个选择数对的结束值
        int count = 1;  // 至少可以选择一个数对
        int end = pairs[0][1];  // 第一个数对的结束值
        
        // 遍历剩余数对
        for (int i = 1; i < n; i++) {
            // 如果当前数对的开始值 > 上一个选择数对的结束值
            // 说明可以连接，可以选择当前数对
            if (pairs[i][0] > end) {
                count++;
                end = pairs[i][1];  // 更新结束值
            }
            // 如果不能连接，则跳过当前数对
        }
        
        return count;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[][] pairs1 = {{1,2},{2,3},{3,4}};
        System.out.println("测试用例1:");
        System.out.println("输入: pairs = [[1,2],[2,3],[3,4]]");
        System.out.println("输出: " + findLongestChain(pairs1)); // 期望输出: 2
        
        // 测试用例2
        int[][] pairs2 = {{1,2},{7,8},{4,5}};
        System.out.println("\n测试用例2:");
        System.out.println("输入: pairs = [[1,2],[7,8],[4,5]]");
        System.out.println("输出: " + findLongestChain(pairs2)); // 期望输出: 3
        
        // 测试用例3
        int[][] pairs3 = {{1,2}};
        System.out.println("\n测试用例3:");
        System.out.println("输入: pairs = [[1,2]]");
        System.out.println("输出: " + findLongestChain(pairs3)); // 期望输出: 1
        
        // 测试用例4
        int[][] pairs4 = {};
        System.out.println("\n测试用例4:");
        System.out.println("输入: pairs = []");
        System.out.println("输出: " + findLongestChain(pairs4)); // 期望输出: 0
    }
}