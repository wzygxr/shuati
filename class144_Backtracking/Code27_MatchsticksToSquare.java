package class038;

import java.util.Arrays;

/**
 * LeetCode 473. 火柴拼正方形
 * 
 * 题目描述：
 * 你将得到一个整数数组 matchsticks ，其中 matchsticks[i] 是第 i 个火柴棒的长度。
 * 你要用所有的火柴棍拼成一个正方形。你不能折断任何一根火柴棒，但你可以把它们连接在一起，
 * 而且每根火柴棒必须使用一次。
 * 如果你能使这个正方形，则返回 true ，否则返回 false 。
 * 
 * 示例：
 * 输入: matchsticks = [1,1,2,2,2]
 * 输出: true
 * 解释: 能拼成一个边长为2的正方形，每边两根火柴棒
 * 
 * 输入: matchsticks = [3,3,3,3,4]
 * 输出: false
 * 解释: 不能用所有火柴棒拼成一个正方形
 * 
 * 提示：
 * 1 <= matchsticks.length <= 15
 * 1 <= matchsticks[i] <= 10^8
 * 
 * 链接：https://leetcode.cn/problems/matchsticks-to-square/
 * 
 * 算法思路：
 * 1. 计算所有火柴棒的总长度，如果不能被4整除，直接返回false
 * 2. 计算每条边的目标长度（总长度/4）
 * 3. 使用回溯算法尝试将火柴棒分配到四条边
 * 4. 使用剪枝优化：排序、提前终止等
 * 
 * 时间复杂度：O(4^n)，其中n是火柴棒数量
 * 空间复杂度：O(n)，递归栈深度
 */
public class Code27_MatchsticksToSquare {

    /**
     * 判断是否能用火柴棒拼成正方形
     * 
     * @param matchsticks 火柴棒长度数组
     * @return 是否能拼成正方形
     */
    public static boolean makesquare(int[] matchsticks) {
        // 计算总长度
        int total = 0;
        for (int stick : matchsticks) {
            total += stick;
        }
        
        // 如果不能被4整除，直接返回false
        if (total % 4 != 0) {
            return false;
        }
        
        int sideLength = total / 4;
        
        // 排序（从大到小），便于剪枝
        Arrays.sort(matchsticks);
        reverse(matchsticks);
        
        // 初始化四条边的当前长度
        int[] sides = new int[4];
        
        return backtrack(matchsticks, 0, sides, sideLength);
    }

    /**
     * 回溯函数分配火柴棒
     * 
     * @param matchsticks 火柴棒长度数组
     * @param index 当前处理的火柴棒索引
     * @param sides 四条边的当前长度
     * @param target 目标边长
     * @return 是否能成功分配
     */
    private static boolean backtrack(int[] matchsticks, int index, int[] sides, int target) {
        // 终止条件：所有火柴棒都已分配
        if (index == matchsticks.length) {
            return sides[0] == target && sides[1] == target && sides[2] == target && sides[3] == target;
        }
        
        int currentStick = matchsticks[index];
        
        // 尝试将当前火柴棒分配到四条边
        for (int i = 0; i < 4; i++) {
            // 剪枝：如果当前边加上火柴棒长度超过目标值，跳过
            if (sides[i] + currentStick > target) {
                continue;
            }
            
            // 剪枝：如果当前边与前一条边长度相同，且前一条边分配失败，跳过
            // 这样可以避免重复计算相同的情况
            if (i > 0 && sides[i] == sides[i - 1]) {
                continue;
            }
            
            sides[i] += currentStick;
            if (backtrack(matchsticks, index + 1, sides, target)) {
                return true;
            }
            sides[i] -= currentStick;  // 回溯
        }
        
        return false;
    }

    /**
     * 解法二：使用位运算 + 动态规划
     * 适用于需要高效计算的情况
     * 
     * @param matchsticks 火柴棒长度数组
     * @return 是否能拼成正方形
     */
    public static boolean makesquareDP(int[] matchsticks) {
        int total = 0;
        for (int stick : matchsticks) {
            total += stick;
        }
        
        if (total % 4 != 0) {
            return false;
        }
        
        int sideLength = total / 4;
        int n = matchsticks.length;
        int totalStates = 1 << n;
        
        // dp[mask] 表示使用mask对应的火柴棒能组成的边长模sideLength的余数
        int[] dp = new int[totalStates];
        Arrays.fill(dp, -1);
        dp[0] = 0;
        
        for (int mask = 0; mask < totalStates; mask++) {
            if (dp[mask] == -1) {
                continue;
            }
            
            for (int i = 0; i < n; i++) {
                // 如果第i根火柴棒还未使用
                if ((mask & (1 << i)) == 0) {
                    int nextMask = mask | (1 << i);
                    int remainder = dp[mask] + matchsticks[i];
                    
                    // 如果当前边长超过目标值，重置
                    if (remainder > sideLength) {
                        continue;
                    }
                    
                    dp[nextMask] = (remainder == sideLength) ? 0 : remainder;
                }
            }
        }
        
        return dp[totalStates - 1] == 0;
    }

    /**
     * 解法三：使用DFS + 剪枝优化
     * 更直观的实现方式
     * 
     * @param matchsticks 火柴棒长度数组
     * @return 是否能拼成正方形
     */
    public static boolean makesquareDFS(int[] matchsticks) {
        int total = 0;
        for (int stick : matchsticks) {
            total += stick;
        }
        
        if (total % 4 != 0) {
            return false;
        }
        
        int sideLength = total / 4;
        
        // 排序（从大到小）
        Arrays.sort(matchsticks);
        reverse(matchsticks);
        
        // 如果有火柴棒长度大于边长，直接返回false
        if (matchsticks[0] > sideLength) {
            return false;
        }
        
        return dfs(matchsticks, new int[4], 0, sideLength);
    }

    private static boolean dfs(int[] matchsticks, int[] sides, int index, int target) {
        if (index == matchsticks.length) {
            return true;
        }
        
        for (int i = 0; i < 4; i++) {
            if (sides[i] + matchsticks[index] <= target) {
                sides[i] += matchsticks[index];
                if (dfs(matchsticks, sides, index + 1, target)) {
                    return true;
                }
                sides[i] -= matchsticks[index];
            }
            
            // 剪枝：如果当前边长度为0，且分配失败，说明无法成功
            if (sides[i] == 0) {
                break;
            }
        }
        
        return false;
    }

    // 反转数组（从大到小排序）
    private static void reverse(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] matchsticks1 = {1, 1, 2, 2, 2};
        boolean result1 = makesquare(matchsticks1);
        System.out.println("输入: matchsticks = [1,1,2,2,2]");
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int[] matchsticks2 = {3, 3, 3, 3, 4};
        boolean result2 = makesquare(matchsticks2);
        System.out.println("\n输入: matchsticks = [3,3,3,3,4]");
        System.out.println("输出: " + result2);
        
        // 测试用例3
        int[] matchsticks3 = {5, 5, 5, 5, 4, 4, 4, 4, 3, 3, 3, 3};
        boolean result3 = makesquare(matchsticks3);
        System.out.println("\n输入: matchsticks = [5,5,5,5,4,4,4,4,3,3,3,3]");
        System.out.println("输出: " + result3);
        
        // 测试动态规划解法
        System.out.println("\n=== 动态规划解法测试 ===");
        boolean result4 = makesquareDP(matchsticks1);
        System.out.println("输入: matchsticks = [1,1,2,2,2]");
        System.out.println("输出: " + result4);
    }
}