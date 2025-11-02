package class008_AdvancedAlgorithmsAndDataStructures.game_of_life_problems;

import java.util.*;

/**
 * LeetCode 351. 安卓系统手势解锁 (Android Unlock Patterns)
 * 
 * 题目来源：https://leetcode.cn/problems/android-unlock-patterns/
 * 
 * 题目描述：
 * 我们都知道安卓有个手势解锁的界面，是一个 3 x 3 的点所绘制出来的网格。
 * 用户可以设置一个 "解锁模式"，通过连接特定序列中的点，形成一个解锁手势。
 * 一个有效的解锁模式需要满足以下两个条件：
 * 1. 所有点是不同的
 * 2. 如果两个点之间有其他点，则必须先经过这些点
 * 
 * 算法思路：
 * 这是一个回溯算法问题，可以使用以下方法解决：
 * 1. 回溯法：枚举所有可能的路径
 * 2. 动态规划：记忆化搜索优化
 * 3. 状态压缩：使用位运算优化空间
 * 
 * 虽然这不是经典的生命游戏问题，但可以看作是在一个网格上模拟状态变化的问题，
 * 与生命游戏有相似的网格状态处理思想。
 * 
 * 时间复杂度：
 * - 回溯法：O(9!)
 * - 空间复杂度：O(9)
 * 
 * 应用场景：
 * 1. 密码学：模式识别和安全验证
 * 2. 游戏开发：路径搜索和状态机
 * 3. 图论：哈密顿路径问题
 * 
 * 相关题目：
 * 1. LeetCode 289. 生命游戏
 * 2. LeetCode 79. 单词搜索
 * 3. LeetCode 212. 单词搜索 II
 */
public class LeetCode_351_AndroidUnlockPatterns {
    
    // 记录两个点之间必须经过的中间点
    private static int[][] skip = new int[10][10];
    
    // 静态初始化块，设置跳跃规则
    static {
        // 初始化跳跃规则
        skip[1][3] = skip[3][1] = 2;
        skip[1][7] = skip[7][1] = 4;
        skip[3][9] = skip[9][3] = 6;
        skip[7][9] = skip[9][7] = 8;
        skip[1][9] = skip[9][1] = skip[3][7] = skip[7][3] = skip[2][8] = skip[8][2] = skip[4][6] = skip[6][4] = 5;
    }
    
    /**
     * 方法1：回溯法
     * 时间复杂度：O(9!)
     * 空间复杂度：O(9)
     * @param m 最小步数
     * @param n 最大步数
     * @return 满足条件的解锁模式数量
     */
    public static int numberOfPatternsBacktrack(int m, int n) {
        boolean[] visited = new boolean[10];
        int count = 0;
        
        // 从1,2,5开始搜索（利用对称性优化）
        // 1,3,7,9是对称的，2,4,6,8是对称的
        count += dfsBacktrack(1, visited, 1, m, n) * 4;  // 1,3,7,9
        count += dfsBacktrack(2, visited, 1, m, n) * 4;  // 2,4,6,8
        count += dfsBacktrack(5, visited, 1, m, n);      // 5
        
        return count;
    }
    
    /**
     * 回溯搜索函数
     * @param current 当前位置
     * @param visited 已访问位置数组
     * @param len 当前路径长度
     * @param m 最小步数
     * @param n 最大步数
     * @return 从当前位置开始的有效路径数量
     */
    private static int dfsBacktrack(int current, boolean[] visited, int len, int m, int n) {
        if (len > n) {
            return 0;
        }
        
        int count = 0;
        // 如果当前长度满足要求，计数加1
        if (len >= m) {
            count++;
        }
        
        // 标记当前位置为已访问
        visited[current] = true;
        
        // 尝试移动到下一个位置
        for (int next = 1; next <= 9; next++) {
            // 如果下一个位置已访问，跳过
            if (visited[next]) {
                continue;
            }
            
            // 检查是否可以移动到下一个位置
            int skipPoint = skip[current][next];
            // 如果不需要跳跃，或者跳跃点已被访问，可以移动
            if (skipPoint == 0 || visited[skipPoint]) {
                count += dfsBacktrack(next, visited, len + 1, m, n);
            }
        }
        
        // 回溯：取消标记当前位置
        visited[current] = false;
        
        return count;
    }
    
    /**
     * 方法2：带记忆化的回溯法
     * 时间复杂度：O(9 * 2^9)
     * 空间复杂度：O(2^9)
     * @param m 最小步数
     * @param n 最大步数
     * @return 满足条件的解锁模式数量
     */
    public static int numberOfPatternsMemoization(int m, int n) {
        // 使用位掩码表示已访问的点
        int[][] memo = new int[1 << 9][10];
        for (int i = 0; i < (1 << 9); i++) {
            Arrays.fill(memo[i], -1);
        }
        
        int count = 0;
        // 从1,2,5开始搜索（利用对称性优化）
        count += dfsMemoization(1, 1 << (1-1), 1, m, n, memo) * 4;  // 1,3,7,9
        count += dfsMemoization(2, 1 << (2-1), 1, m, n, memo) * 4;  // 2,4,6,8
        count += dfsMemoization(5, 1 << (5-1), 1, m, n, memo);      // 5
        
        return count;
    }
    
    /**
     * 带记忆化的回溯搜索函数
     * @param current 当前位置
     * @param visitedMask 已访问位置的位掩码
     * @param len 当前路径长度
     * @param m 最小步数
     * @param n 最大步数
     * @param memo 记忆化数组
     * @return 从当前位置开始的有效路径数量
     */
    private static int dfsMemoization(int current, int visitedMask, int len, int m, int n, int[][] memo) {
        if (len > n) {
            return 0;
        }
        
        // 如果已经计算过，直接返回结果
        if (memo[visitedMask][current] != -1) {
            return memo[visitedMask][current];
        }
        
        int count = 0;
        // 如果当前长度满足要求，计数加1
        if (len >= m) {
            count++;
        }
        
        // 尝试移动到下一个位置
        for (int next = 1; next <= 9; next++) {
            // 如果下一个位置已访问，跳过
            if ((visitedMask & (1 << (next - 1))) != 0) {
                continue;
            }
            
            // 检查是否可以移动到下一个位置
            int skipPoint = skip[current][next];
            // 如果不需要跳跃，或者跳跃点已被访问，可以移动
            if (skipPoint == 0 || (visitedMask & (1 << (skipPoint - 1))) != 0) {
                count += dfsMemoization(next, visitedMask | (1 << (next - 1)), len + 1, m, n, memo);
            }
        }
        
        // 记忆化结果
        memo[visitedMask][current] = count;
        return count;
    }
    
    /**
     * 方法3：动态规划解法
     * 时间复杂度：O(n * 9 * 2^9)
     * 空间复杂度：O(9 * 2^9)
     * @param m 最小步数
     * @param n 最大步数
     * @return 满足条件的解锁模式数量
     */
    public static int numberOfPatternsDP(int m, int n) {
        // dp[mask][last] 表示使用mask表示的点集，以last结尾的路径数量
        int[][] dp = new int[1 << 9][10];
        
        // 初始化：每个点作为起点的路径数量为1
        for (int i = 1; i <= 9; i++) {
            dp[1 << (i - 1)][i] = 1;
        }
        
        int result = 0;
        
        // 按照路径长度进行动态规划
        for (int len = 1; len <= n; len++) {
            // 计算长度为len的路径数量
            if (len >= m) {
                for (int mask = 0; mask < (1 << 9); mask++) {
                    for (int last = 1; last <= 9; last++) {
                        if ((mask & (1 << (last - 1))) != 0) {
                            result += dp[mask][last];
                        }
                    }
                }
            }
            
            // 如果还没到最大长度，继续扩展
            if (len < n) {
                int[][] newDp = new int[1 << 9][10];
                
                for (int mask = 0; mask < (1 << 9); mask++) {
                    for (int last = 1; last <= 9; last++) {
                        if (dp[mask][last] > 0) {
                            // 尝试从last移动到下一个点
                            for (int next = 1; next <= 9; next++) {
                                // 如果下一个点已访问，跳过
                                if ((mask & (1 << (next - 1))) != 0) {
                                    continue;
                                }
                                
                                // 检查是否可以移动到下一个位置
                                int skipPoint = skip[last][next];
                                // 如果不需要跳跃，或者跳跃点已被访问，可以移动
                                if (skipPoint == 0 || (mask & (1 << (skipPoint - 1))) != 0) {
                                    newDp[mask | (1 << (next - 1))][next] += dp[mask][last];
                                }
                            }
                        }
                    }
                }
                
                dp = newDp;
            }
        }
        
        return result;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 351. 安卓系统手势解锁 ===");
        
        // 测试用例1
        int m1 = 1, n1 = 1;
        System.out.println("测试用例1:");
        System.out.println("m: " + m1 + ", n: " + n1);
        System.out.println("回溯法结果: " + numberOfPatternsBacktrack(m1, n1));
        System.out.println("记忆化回溯法结果: " + numberOfPatternsMemoization(m1, n1));
        System.out.println("动态规划法结果: " + numberOfPatternsDP(m1, n1));
        System.out.println("期望结果: 9");
        System.out.println();
        
        // 测试用例2
        int m2 = 1, n2 = 2;
        System.out.println("测试用例2:");
        System.out.println("m: " + m2 + ", n: " + n2);
        System.out.println("回溯法结果: " + numberOfPatternsBacktrack(m2, n2));
        System.out.println("记忆化回溯法结果: " + numberOfPatternsMemoization(m2, n2));
        System.out.println("动态规划法结果: " + numberOfPatternsDP(m2, n2));
        System.out.println("期望结果: 65");
        System.out.println();
        
        // 测试用例3
        int m3 = 2, n3 = 3;
        System.out.println("测试用例3:");
        System.out.println("m: " + m3 + ", n: " + n3);
        System.out.println("回溯法结果: " + numberOfPatternsBacktrack(m3, n3));
        System.out.println("记忆化回溯法结果: " + numberOfPatternsMemoization(m3, n3));
        System.out.println("动态规划法结果: " + numberOfPatternsDP(m3, n3));
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int m = 3, n = 7;
        
        long startTime = System.nanoTime();
        int result1 = numberOfPatternsBacktrack(m, n);
        long endTime = System.nanoTime();
        System.out.println("回溯法计算m=" + m + ",n=" + n + "时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result1);
        
        startTime = System.nanoTime();
        int result2 = numberOfPatternsMemoization(m, n);
        endTime = System.nanoTime();
        System.out.println("记忆化回溯法计算m=" + m + ",n=" + n + "时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result2);
        
        startTime = System.nanoTime();
        int result3 = numberOfPatternsDP(m, n);
        endTime = System.nanoTime();
        System.out.println("动态规划法计算m=" + m + ",n=" + n + "时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result3);
    }
}