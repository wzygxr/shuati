// LeetCode 1986. 完成任务的最少工作时间段
// 给你一个任务数组 tasks ，其中 tasks[i] 是一个正整数，表示第 i 个任务的持续时间。
// 同时给你一个正整数 sessionTime ，表示单个会话中可以完成任务的最长时间。
// 你可以按照任意顺序完成任务。
// 返回完成所有任务所需的最少会话数。
// 测试链接 : https://leetcode.cn/problems/minimum-number-of-work-sessions-to-finish-the-tasks/

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

// 由于环境限制，此处只提供算法核心实现思路，不包含完整的可编译代码
// 在实际使用中，需要根据具体环境添加适当的头文件和类型定义

/*
int minSessions(int* tasks, int tasksSize, int sessionTime) {
    // 异常处理：检查输入参数的有效性
    if (tasks == 0 || tasksSize == 0) {
        return 0;
    }
    
    if (sessionTime <= 0) {
        // 会话时间必须为正整数
        return -1;
    }
    
    int n = tasksSize;
    
    // 限制检查：根据题目描述，任务数不超过14
    if (n > 14) {
        // 任务数不能超过14个
        return -1;
    }
    
    // 预处理：计算每个状态是否可以在一个会话内完成
    int valid[1 << 14] = {0}; // 假设最大任务数为14
    for (int mask = 0; mask < (1 << n); mask++) {
        int totalTime = 0;
        for (int i = 0; i < n; i++) {
            // 检查第i个任务是否被选中
            if ((mask & (1 << i)) != 0) {
                totalTime += tasks[i];
            }
        }
        // 如果总时间不超过会话时间，则该状态有效
        valid[mask] = (totalTime <= sessionTime) ? 1 : 0;
    }
    
    // dp[mask] 表示完成任务状态为mask时所需的最少会话数
    int dp[1 << 14]; // 假设最大任务数为14
    // 初始化为最大值
    for (int i = 0; i < (1 << n); i++) {
        dp[i] = 1000000; // 使用大数表示无穷大
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
                if (dp[mask ^ subset] != 1000000) {
                    int a = dp[mask];
                    int b = dp[mask ^ subset] + 1;
                    dp[mask] = (a < b) ? a : b;
                }
            }
        }
    }
    
    // 返回完成所有任务的最少会话数
    return dp[(1 << n) - 1];
}
*/