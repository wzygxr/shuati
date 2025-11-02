package class073;

// LeetCode 474. 一和零
// 题目描述：给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
// 请你找出并返回 strs 的最大子集的大小，该子集中 最多 有 m 个 0 和 n 个 1 。
// 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集 。
// 链接：https://leetcode.cn/problems/ones-and-zeroes/
// 
// 解题思路：
// 这是一个多维背包问题，我们需要同时考虑两种资源限制：0的数量和1的数量。
// 每个字符串相当于一个物品，占用的空间是它包含的0的数量和1的数量，价值为1（因为我们想最大化子集的大小）。
// 目标是在不超过m个0和n个1的限制下，选择尽可能多的字符串。
// 
// 状态定义：dp[i][j] 表示使用i个0和j个1时，可以选择的最大字符串数量
// 状态转移方程：对于每个字符串s，其中有zeros个0和ones个1，
//              dp[i][j] = max(dp[i][j], dp[i-zeros][j-ones] + 1)，当i >= zeros且j >= ones时
// 初始状态：dp[0][0] = 0，表示不使用任何0和1时，可以选择0个字符串
// 其他初始值也为0，表示还没有选择任何字符串
// 
// 时间复杂度：O(l * m * n)，其中l是字符串数组的长度，m和n是给定的整数
// 空间复杂度：O(m * n)，使用二维DP数组

public class Code23_OnesAndZeros {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        String[] strs1 = {"10", "0001", "111001", "1", "0"};
        int m1 = 5, n1 = 3;
        System.out.println("测试用例1结果: " + findMaxForm(strs1, m1, n1)); // 预期输出: 4
        // 解释: 最多有5个0和3个1的最大子集是 {"10", "0001", "1", "0"}
        // 这个子集最多包含4个0（来自"0001"）和3个1（来自"10", "1", "0001"）。
        
        // 测试用例2
        String[] strs2 = {"10", "0", "1"};
        int m2 = 1, n2 = 1;
        System.out.println("测试用例2结果: " + findMaxForm(strs2, m2, n2)); // 预期输出: 2
        // 解释: 最大的子集是 {"0", "1"}，这两个字符串最多有1个0和1个1。
        
        // 测试用例3
        String[] strs3 = {"00", "000"};
        int m3 = 1, n3 = 0;
        System.out.println("测试用例3结果: " + findMaxForm(strs3, m3, n3)); // 预期输出: 0
        
        // 测试用例4
        String[] strs4 = {"111", "1000", "1000", "1000"};
        int m4 = 9, n4 = 3;
        System.out.println("测试用例4结果: " + findMaxForm(strs4, m4, n4)); // 预期输出: 3
    }
    
    /**
     * 找出最大子集的大小，该子集中最多有m个0和n个1
     * @param strs 二进制字符串数组
     * @param m 最大0的数量
     * @param n 最大1的数量
     * @return 最大子集的大小
     */
    public static int findMaxForm(String[] strs, int m, int n) {
        // 参数验证
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        // 创建二维DP数组，dp[i][j]表示使用i个0和j个1时，可以选择的最大字符串数量
        int[][] dp = new int[m + 1][n + 1];
        
        // 遍历每个字符串（物品）
        for (String s : strs) {
            // 统计当前字符串中0和1的数量
            int zeros = 0, ones = 0;
            for (char c : s.toCharArray()) {
                if (c == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            
            // 逆序遍历m和n，避免重复使用同一个字符串
            // 从大到小遍历0的数量
            for (int i = m; i >= zeros; i--) {
                // 从大到小遍历1的数量
                for (int j = n; j >= ones; j--) {
                    // 状态转移：选择当前字符串或不选择当前字符串
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeros][j - ones] + 1);
                }
            }
        }
        
        // 返回结果：使用最多m个0和n个1时，可以选择的最大字符串数量
        return dp[m][n];
    }
    
    /**
     * 优化版本：预处理字符串的0和1数量，避免重复计算
     */
    public static int findMaxFormOptimized(String[] strs, int m, int n) {
        // 参数验证
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        // 预处理：统计每个字符串中0和1的数量
        int[][] counts = new int[strs.length][2];
        for (int i = 0; i < strs.length; i++) {
            String s = strs[i];
            int zeros = 0, ones = 0;
            for (char c : s.toCharArray()) {
                if (c == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            counts[i][0] = zeros;
            counts[i][1] = ones;
        }
        
        // 创建二维DP数组
        int[][] dp = new int[m + 1][n + 1];
        
        // 遍历每个字符串（物品）
        for (int[] count : counts) {
            int zeros = count[0];
            int ones = count[1];
            
            // 剪枝：如果当前字符串需要的0或1超过限制，则跳过
            if (zeros > m || ones > n) {
                continue;
            }
            
            // 逆序遍历
            for (int i = m; i >= zeros; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeros][j - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 另一种实现方式，使用滚动数组优化空间（虽然在这里空间复杂度已经是O(m*n)，但展示了一种优化思路）
     */
    public static int findMaxFormWithRollingArray(String[] strs, int m, int n) {
        // 参数验证
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        // 创建DP数组
        int[][] dp = new int[m + 1][n + 1];
        
        for (String s : strs) {
            int zeros = 0, ones = 0;
            for (char c : s.toCharArray()) {
                if (c == '0') zeros++;
                else ones++;
            }
            
            // 使用临时数组保存上一状态
            // 实际上，逆序遍历就可以避免使用临时数组，但这里展示这种方法
            int[][] temp = new int[m + 1][n + 1];
            for (int i = 0; i <= m; i++) {
                System.arraycopy(dp[i], 0, temp[i], 0, n + 1);
            }
            
            for (int i = zeros; i <= m; i++) {
                for (int j = ones; j <= n; j++) {
                    dp[i][j] = Math.max(dp[i][j], temp[i - zeros][j - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 递归+记忆化搜索实现
     */
    public static int findMaxFormDFS(String[] strs, int m, int n) {
        // 参数验证
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        // 预处理：统计每个字符串中0和1的数量
        int[][] counts = new int[strs.length][2];
        for (int i = 0; i < strs.length; i++) {
            String s = strs[i];
            int zeros = 0, ones = 0;
            for (char c : s.toCharArray()) {
                if (c == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            counts[i][0] = zeros;
            counts[i][1] = ones;
        }
        
        // 使用三维记忆化数组：dp[index][i][j] 表示从index开始，使用i个0和j个1时，可以选择的最大字符串数量
        Integer[][][] memo = new Integer[strs.length][m + 1][n + 1];
        
        // 调用递归辅助函数
        return dfs(counts, 0, m, n, memo);
    }
    
    /**
     * 递归辅助函数
     */
    private static int dfs(int[][] counts, int index, int m, int n, Integer[][][] memo) {
        // 基础情况：已经处理完所有字符串
        if (index == counts.length) {
            return 0;
        }
        
        // 如果已经计算过，直接返回结果
        if (memo[index][m][n] != null) {
            return memo[index][m][n];
        }
        
        // 获取当前字符串的0和1数量
        int zeros = counts[index][0];
        int ones = counts[index][1];
        
        // 选择不使用当前字符串
        int notTake = dfs(counts, index + 1, m, n, memo);
        
        // 选择使用当前字符串（如果有足够的0和1）
        int take = 0;
        if (zeros <= m && ones <= n) {
            take = 1 + dfs(counts, index + 1, m - zeros, n - ones, memo);
        }
        
        // 记录结果
        memo[index][m][n] = Math.max(notTake, take);
        
        return memo[index][m][n];
    }
    
    /**
     * 优化的DFS实现，避免使用过大的三维数组
     */
    public static int findMaxFormDFSOptimized(String[] strs, int m, int n) {
        // 预处理：统计每个字符串中0和1的数量，并过滤掉不可能选择的字符串
        java.util.List<int[]> validCounts = new java.util.ArrayList<>();
        for (String s : strs) {
            int zeros = 0, ones = 0;
            for (char c : s.toCharArray()) {
                if (c == '0') zeros++;
                else ones++;
            }
            // 只有当字符串需要的0和1都不超过限制时，才可能被选中
            if (zeros <= m && ones <= n) {
                validCounts.add(new int[]{zeros, ones});
            }
        }
        
        // 使用Map作为缓存，键为(index, m, n)的组合，值为对应的最大子集大小
        java.util.Map<String, Integer> memo = new java.util.HashMap<>();
        
        return dfsOptimized(validCounts, 0, m, n, memo);
    }
    
    private static int dfsOptimized(java.util.List<int[]> counts, int index, int m, int n, 
                                   java.util.Map<String, Integer> memo) {
        if (index == counts.size()) {
            return 0;
        }
        
        // 生成缓存键
        String key = index + "," + m + "," + n;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        
        int[] current = counts.get(index);
        int zeros = current[0];
        int ones = current[1];
        
        // 不选择当前字符串
        int notTake = dfsOptimized(counts, index + 1, m, n, memo);
        
        // 选择当前字符串
        int take = 0;
        if (zeros <= m && ones <= n) {
            take = 1 + dfsOptimized(counts, index + 1, m - zeros, n - ones, memo);
        }
        
        // 取最大值并缓存
        int result = Math.max(notTake, take);
        memo.put(key, result);
        
        return result;
    }
}