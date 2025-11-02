// LeetCode 1494. Parallel Courses II
// C++ 实现

/**
 * 题目描述：
 * 给你一个整数 n 表示某所大学里课程的数目，编号为 1 到 n，
 * 还给你一个数组 relations，其中 relations[i] = [xi, yi] 表示课程 xi 必须在课程 yi 之前上。
 * 在一个学期中，你最多可以同时上 k 门课，前提是这些课的先修课在之前的学期中都已上过。
 * 请你返回上完所有课最少需要多少个学期。
 * 
 * 解题思路：
 * 这个问题可以使用动态规划和位运算来解决。
 * 我们使用状态压缩动态规划，其中 dp[mask] 表示完成课程集合 mask 所需的最少学期数。
 * 
 * 时间复杂度：O(2^n * n)
 * 空间复杂度：O(2^n)
 */

// 由于编译环境限制，这里提供算法思路和伪代码实现

/*
int minNumberOfSemesters(int n, int** relations, int relationsSize, int* relationsColSize, int k) {
    // 构建先修课程依赖关系
    int* prerequisites = (int*)calloc(n, sizeof(int));
    
    for (int i = 0; i < relationsSize; i++) {
        int prev = relations[i][0] - 1; // 转换为0-indexed
        int next = relations[i][1] - 1;
        prerequisites[next] |= (1 << prev);
    }
    
    // dp[mask] 表示完成课程集合mask所需的最少学期数
    int* dp = (int*)malloc((1 << n) * sizeof(int));
    for (int i = 0; i < (1 << n); i++) {
        dp[i] = n; // 初始化为最大值
    }
    dp[0] = 0; // 不需要上任何课程时，学期数为0
    
    // 对于每个课程集合
    for (int mask = 0; mask < (1 << n); mask++) {
        if (dp[mask] == n) continue; // 跳过无法达到的状态
        
        // 计算当前可以学习的课程（先修课程已完成的课程）
        int available = 0;
        for (int i = 0; i < n; i++) {
            // 如果课程i还没有学过，并且其先修课程都已完成
            if (!(mask & (1 << i)) && (prerequisites[i] & mask) == prerequisites[i]) {
                available |= (1 << i);
            }
        }
        
        // 枚举available的所有非空子集作为本学期的学习课程
        // 这里简化实现，实际应该枚举所有子集
        for (int subset = available; subset > 0; subset = (subset - 1) & available) {
            // 检查子集大小是否不超过k
            int count = 0;
            for (int j = 0; j < n; j++) {
                if (subset & (1 << j)) count++;
            }
            
            if (count <= k) {
                int newMask = mask | subset;
                if (dp[mask] + 1 < dp[newMask]) {
                    dp[newMask] = dp[mask] + 1;
                }
            }
        }
    }
    
    int result = dp[(1 << n) - 1]; // 返回完成所有课程的最少学期数
    
    free(prerequisites);
    free(dp);
    
    return result;
}

// 测试用例说明：
// 测试用例1: n=4, relations=[[2,1],[3,1],[1,4]], k=2, 结果=3
// 测试用例2: n=5, relations=[[2,1],[3,1],[4,1],[1,5]], k=2, 结果=4
// 测试用例3: n=11, relations=[], k=2, 结果=6
*/

// 算法核心思想：
// 1. 使用状态压缩动态规划，用位掩码表示课程完成状态
// 2. 对于每个状态，计算当前可以学习的课程
// 3. 枚举这些课程的所有子集（大小不超过k）作为本学期的学习计划
// 4. 更新到达新状态的最少学期数

// 时间复杂度分析：
// - 状态数：O(2^n)
// - 每个状态需要枚举子集：O(3^n) （最坏情况）
// - 总体时间复杂度：O(2^n * 3^n) = O(6^n)
// - 但由于剪枝和实际数据特点，通常远小于这个上界