package class080;

// 优美的排列 (Beautiful Arrangement)
// 假设有从 1 到 n 的 n 个整数。用这些整数构造一个数组 perm（下标从 1 开始），
// 只要满足下述条件之一，该数组就是一个优美的排列：
// 1. perm[i] 能够被 i 整除
// 2. i 能够被 perm[i] 整除
// 给你一个整数 n ，返回可以构造的优美排列的数量。
// 测试链接 : https://leetcode.cn/problems/beautiful-arrangement/
public class Code09_BeautifulArrangement {

    // 使用状态压缩动态规划解决优美排列问题
    // 核心思想：用二进制位表示已使用的数字集合，通过状态转移计算优美排列数量
    // 时间复杂度: O(n * 2^n)
    // 空间复杂度: O(2^n)
    public static int countArrangement(int n) {
        // dp[mask] 表示使用mask代表的数字集合能构成的优美排列数量
        int[] dp = new int[1 << n];
        // 初始状态：不使用任何数字，能构成1个优美排列（空排列）
        dp[0] = 1;
        
        // 状态转移：枚举所有可能的状态
        for (int mask = 0; mask < (1 << n); mask++) {
            // 如果当前状态不可达，跳过
            if (dp[mask] == 0) {
                continue;
            }
            
            // 计算已使用的数字个数（即当前要填充的位置）
            int pos = Integer.bitCount(mask) + 1;
            
            // 枚举下一个要使用的数字
            for (int i = 1; i <= n; i++) {
                // 如果数字i还未使用，且满足优美排列的条件
                if ((mask & (1 << (i - 1))) == 0 && (i % pos == 0 || pos % i == 0)) {
                    // 更新状态：使用mask+(1<<(i-1))代表的数字集合能构成的优美排列数量
                    dp[mask | (1 << (i - 1))] += dp[mask];
                }
            }
        }
        
        // 返回使用所有数字能构成的优美排列数量
        return dp[(1 << n) - 1];
    }

}