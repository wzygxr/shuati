package class086;

// LeetCode 1986. 完成任务的最少工作时间段
// 给你一个任务数组 tasks ，其中 tasks[i] 是一个正整数，表示第 i 个任务的持续时间。
// 同时给你一个正整数 sessionTime ，表示单个会话中可以完成任务的最长时间。
// 你可以按照任意顺序完成任务。
// 返回完成所有任务所需的最少会话数。
// 测试链接 : https://leetcode.cn/problems/minimum-number-of-work-sessions-to-finish-the-tasks/

public class LeetCode1986_Minimum_Number_of_Work_Sessions_to_Finish_Tasks {
    
    /*
     * 算法详解：完成任务的最少工作时间段（LeetCode 1986）
     * 
     * 问题描述：
     * 给你一个任务数组 tasks ，其中 tasks[i] 是一个正整数，表示第 i 个任务的持续时间。
     * 同时给你一个正整数 sessionTime ，表示单个会话中可以完成任务的最长时间。
     * 你可以按照任意顺序完成任务。
     * 返回完成所有任务所需的最少会话数。
     * 
     * 算法思路：
     * 这是一个典型的分组覆盖问题，可以使用状态压缩动态规划解决。
     * 1. 使用状态压缩表示任务选择状态
     * 2. 预处理每个状态是否可以在一个会话内完成
     * 3. 使用动态规划计算完成所有任务的最少会话数
     * 
     * 时间复杂度分析：
     * 1. 预处理所有状态：O(2^n * n)
     * 2. 动态规划：O(2^n * n)
     * 3. 总体时间复杂度：O(2^n * n)
     * 
     * 空间复杂度分析：
     * 1. 状态数组：O(2^n)
     * 2. dp数组：O(2^n)
     * 3. 总体空间复杂度：O(2^n)
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入参数的有效性
     * 2. 边界处理：正确处理空任务列表的情况
     * 3. 性能优化：预处理可在一个会话内完成的状态
     * 4. 状态压缩：使用位运算优化状态表示
     * 
     * 极端场景验证：
     * 1. 任务数量达到14个（题目限制）的情况
     * 2. 所有任务时间都等于会话时间的情况
     * 3. 所有任务时间都远小于会话时间的情况
     * 4. 所有任务时间都接近会话时间的情况
     */
    
    public static int minSessions(int[] tasks, int sessionTime) {
        // 异常处理：检查输入参数的有效性
        if (tasks == null || tasks.length == 0) {
            return 0;
        }
        
        if (sessionTime <= 0) {
            throw new IllegalArgumentException("会话时间必须为正整数");
        }
        
        int n = tasks.length;
        
        // 限制检查：根据题目描述，任务数不超过14
        if (n > 14) {
            throw new IllegalArgumentException("任务数不能超过14个");
        }
        
        // 预处理：计算每个状态是否可以在一个会话内完成
        boolean[] valid = new boolean[1 << n];
        for (int mask = 0; mask < (1 << n); mask++) {
            int totalTime = 0;
            for (int i = 0; i < n; i++) {
                // 检查第i个任务是否被选中
                if ((mask & (1 << i)) != 0) {
                    totalTime += tasks[i];
                }
            }
            // 如果总时间不超过会话时间，则该状态有效
            valid[mask] = totalTime <= sessionTime;
        }
        
        // dp[mask] 表示完成任务状态为mask时所需的最少会话数
        int[] dp = new int[1 << n];
        // 初始化为最大值
        for (int i = 0; i < (1 << n); i++) {
            dp[i] = Integer.MAX_VALUE;
        }
        // 空状态需要0个会话
        dp[0] = 0;
        
        // 动态规划填表
        for (int mask = 1; mask < (1 << n); mask++) {
            // 枚举mask的所有子集
            for (int subset = mask; subset > 0; subset = (subset - 1) & mask) {
                // 如果子集可以在一个会话内完成
                if (valid[subset]) {
                    // 更新状态：dp[mask] = min(dp[mask], dp[mask ^ subset] + 1)
                    // mask ^ subset 表示从mask中去掉subset后的状态
                    if (dp[mask ^ subset] != Integer.MAX_VALUE) {
                        dp[mask] = Math.min(dp[mask], dp[mask ^ subset] + 1);
                    }
                }
            }
        }
        
        // 返回完成所有任务的最少会话数
        return dp[(1 << n) - 1];
    }
    
    // 优化版本：使用记忆化搜索
    public static int minSessionsOptimized(int[] tasks, int sessionTime) {
        // 异常处理：检查输入参数的有效性
        if (tasks == null || tasks.length == 0) {
            return 0;
        }
        
        if (sessionTime <= 0) {
            throw new IllegalArgumentException("会话时间必须为正整数");
        }
        
        int n = tasks.length;
        
        // 限制检查：根据题目描述，任务数不超过14
        if (n > 14) {
            throw new IllegalArgumentException("任务数不能超过14个");
        }
        
        // 预处理：计算每个状态是否可以在一个会话内完成
        Boolean[] valid = new Boolean[1 << n];
        for (int mask = 0; mask < (1 << n); mask++) {
            valid[mask] = null; // 初始化为null表示未计算
        }
        
        // dp[mask] 表示完成任务状态为mask时所需的最少会话数
        Integer[] dp = new Integer[1 << n];
        
        // 记忆化搜索
        return dfs(tasks, sessionTime, (1 << n) - 1, valid, dp);
    }
    
    // 记忆化搜索函数
    private static int dfs(int[] tasks, int sessionTime, int mask, Boolean[] valid, Integer[] dp) {
        // 基础情况：空状态
        if (mask == 0) {
            return 0;
        }
        
        // 记忆化：如果已经计算过，直接返回结果
        if (dp[mask] != null) {
            return dp[mask];
        }
        
        // 计算当前状态是否可以在一个会话内完成
        if (valid[mask] == null) {
            int totalTime = 0;
            for (int i = 0; i < tasks.length; i++) {
                // 检查第i个任务是否被选中
                if ((mask & (1 << i)) != 0) {
                    totalTime += tasks[i];
                }
            }
            // 如果总时间不超过会话时间，则该状态有效
            valid[mask] = totalTime <= sessionTime;
        }
        
        // 如果当前状态可以在一个会话内完成，直接返回1
        if (valid[mask]) {
            dp[mask] = 1;
            return 1;
        }
        
        // 枚举所有子集，找到最优解
        int result = Integer.MAX_VALUE;
        for (int subset = mask; subset > 0; subset = (subset - 1) & mask) {
            // 如果子集可以在一个会话内完成
            if (valid[subset] == null) {
                int totalTime = 0;
                for (int i = 0; i < tasks.length; i++) {
                    // 检查第i个任务是否被选中
                    if ((subset & (1 << i)) != 0) {
                        totalTime += tasks[i];
                    }
                }
                // 如果总时间不超过会话时间，则该状态有效
                valid[subset] = totalTime <= sessionTime;
            }
            
            if (valid[subset]) {
                // 递归计算剩余任务的最少会话数
                result = Math.min(result, 1 + dfs(tasks, sessionTime, mask ^ subset, valid, dp));
            }
        }
        
        // 缓存结果
        dp[mask] = result;
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] tasks1 = {1, 2, 3};
        int sessionTime1 = 3;
        System.out.println("Test 1 (DP method): " + minSessions(tasks1, sessionTime1));
        System.out.println("Test 1 (DFS method): " + minSessionsOptimized(tasks1, sessionTime1));
        // 期望输出: 2
        
        // 测试用例2
        int[] tasks2 = {3, 1, 3, 1, 1};
        int sessionTime2 = 8;
        System.out.println("Test 2 (DP method): " + minSessions(tasks2, sessionTime2));
        System.out.println("Test 2 (DFS method): " + minSessionsOptimized(tasks2, sessionTime2));
        // 期望输出: 2
        
        // 测试用例3
        int[] tasks3 = {1, 2, 3, 4, 5};
        int sessionTime3 = 15;
        System.out.println("Test 3 (DP method): " + minSessions(tasks3, sessionTime3));
        System.out.println("Test 3 (DFS method): " + minSessionsOptimized(tasks3, sessionTime3));
        // 期望输出: 1
        
        // 测试用例4
        int[] tasks4 = {};
        int sessionTime4 = 5;
        System.out.println("Test 4 (DP method): " + minSessions(tasks4, sessionTime4));
        System.out.println("Test 4 (DFS method): " + minSessionsOptimized(tasks4, sessionTime4));
        // 期望输出: 0
    }
}