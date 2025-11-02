package class038;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 526. 优美的排列
 * 
 * 题目描述：
 * 假设有从 1 到 n 的 n 个整数。用这些整数构造一个数组 perm（下标从 1 开始），
 * 只要满足下述条件之一，该数组就是一个优美的排列：
 * 1. perm[i] 能够被 i 整除
 * 2. i 能够被 perm[i] 整除
 * 给你一个整数 n ，返回可以构造的优美排列的数量。
 * 
 * 示例：
 * 输入：n = 2
 * 输出：2
 * 解释：第1个优美的排列是 [1,2]；第2个优美的排列是 [2,1]
 * 
 * 输入：n = 1
 * 输出：1
 * 
 * 提示：
 * 1 <= n <= 15
 * 
 * 链接：https://leetcode.cn/problems/beautiful-arrangement/
 * 
 * 算法思路：
 * 1. 使用回溯算法生成所有可能的排列
 * 2. 在生成排列的过程中进行剪枝：只有满足优美排列条件的数字才被选择
 * 3. 使用布尔数组标记已使用的数字
 * 4. 通过提前终止无效分支优化性能
 * 
 * 时间复杂度：O(n!)，但通过剪枝可以大大减少实际计算量
 * 空间复杂度：O(n)，递归栈深度
 */
public class Code26_BeautifulArrangement {

    private static int count = 0;

    /**
     * 计算优美排列的数量
     * 
     * @param n 数字个数
     * @return 优美排列的数量
     */
    public static int countArrangement(int n) {
        count = 0;
        boolean[] used = new boolean[n + 1];
        backtrack(n, 1, used);
        return count;
    }

    /**
     * 回溯函数生成优美排列
     * 
     * @param n 数字个数
     * @param pos 当前位置（从1开始）
     * @param used 标记已使用数字的数组
     */
    private static void backtrack(int n, int pos, boolean[] used) {
        // 终止条件：已生成完整排列
        if (pos > n) {
            count++;
            return;
        }
        
        // 尝试所有可能的数字
        for (int num = 1; num <= n; num++) {
            if (!used[num] && (num % pos == 0 || pos % num == 0)) {
                used[num] = true;
                backtrack(n, pos + 1, used);
                used[num] = false;  // 回溯
            }
        }
    }

    /**
     * 解法二：返回所有优美的排列（而不仅仅是数量）
     * 
     * @param n 数字个数
     * @return 所有优美的排列
     */
    public static List<List<Integer>> getBeautifulArrangements(int n) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[n + 1];
        backtrackWithResult(n, 1, used, new ArrayList<>(), result);
        return result;
    }

    private static void backtrackWithResult(int n, int pos, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        // 终止条件：已生成完整排列
        if (pos > n) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        // 尝试所有可能的数字
        for (int num = 1; num <= n; num++) {
            if (!used[num] && (num % pos == 0 || pos % num == 0)) {
                used[num] = true;
                path.add(num);
                backtrackWithResult(n, pos + 1, used, path, result);
                path.remove(path.size() - 1);  // 回溯
                used[num] = false;
            }
        }
    }

    /**
     * 解法三：使用位运算优化空间复杂度
     * 使用整数位掩码代替布尔数组
     * 
     * @param n 数字个数
     * @return 优美排列的数量
     */
    public static int countArrangementBitmask(int n) {
        return backtrackBitmask(n, 1, 0);
    }

    private static int backtrackBitmask(int n, int pos, int used) {
        // 终止条件：已生成完整排列
        if (pos > n) {
            return 1;
        }
        
        int count = 0;
        
        // 尝试所有可能的数字
        for (int num = 1; num <= n; num++) {
            int mask = 1 << num;
            if ((used & mask) == 0 && (num % pos == 0 || pos % num == 0)) {
                count += backtrackBitmask(n, pos + 1, used | mask);
            }
        }
        
        return count;
    }

    /**
     * 解法四：使用动态规划 + 状态压缩
     * 适用于需要高效计算的情况
     * 
     * @param n 数字个数
     * @return 优美排列的数量
     */
    public static int countArrangementDP(int n) {
        int totalStates = 1 << n;
        int[] dp = new int[totalStates];
        dp[0] = 1;
        
        for (int state = 0; state < totalStates; state++) {
            int pos = Integer.bitCount(state) + 1;  // 当前位置
            
            for (int num = 1; num <= n; num++) {
                int mask = 1 << (num - 1);
                if ((state & mask) == 0 && (num % pos == 0 || pos % num == 0)) {
                    dp[state | mask] += dp[state];
                }
            }
        }
        
        return dp[totalStates - 1];
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 2;
        int result1 = countArrangement(n1);
        System.out.println("输入: n = " + n1);
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int n2 = 1;
        int result2 = countArrangement(n2);
        System.out.println("\n输入: n = " + n2);
        System.out.println("输出: " + result2);
        
        // 测试用例3
        int n3 = 3;
        int result3 = countArrangement(n3);
        System.out.println("\n输入: n = " + n3);
        System.out.println("输出: " + result3);
        
        // 测试返回所有排列
        System.out.println("\n=== 所有优美排列 ===");
        List<List<Integer>> arrangements = getBeautifulArrangements(n1);
        System.out.println("输入: n = " + n1);
        System.out.println("输出: " + arrangements);
        
        // 测试位运算解法
        System.out.println("\n=== 位运算解法测试 ===");
        int result4 = countArrangementBitmask(n1);
        System.out.println("输入: n = " + n1);
        System.out.println("输出: " + result4);
        
        // 测试动态规划解法
        System.out.println("\n=== 动态规划解法测试 ===");
        int result5 = countArrangementDP(n1);
        System.out.println("输入: n = " + n1);
        System.out.println("输出: " + result5);
    }
}