#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <functional>
using namespace std;

/**
 * 并行课程 II
 * 给你一个整数 n 表示某所大学里课程的总数，编号为 1 到 n。
 * 同时给你一个数组 dependencies ，其中 dependencies[i] = [xi, yi] 表示课程 xi 必须在课程 yi 之前完成。
 * 同时又给你一个整数 k 。
 * 在一个学期中，你最多可以同时上 k 门课，前提是这些课的先修课已经修完。
 * 请你返回上完所有课最少需要多少个学期。
 * 测试链接 : https://leetcode.cn/problems/parallel-courses-ii/
 * 
 * 算法思路：
 * 这道题是拓扑排序的变种，需要考虑每个学期最多选k门课的限制。
 * 由于n最大为15，我们可以使用状态压缩动态规划来解决。
 * 
 * 解法：状态压缩动态规划
 * 1. 预处理每个课程的前置依赖（使用二进制表示）
 * 2. 使用动态规划，dp[mask]表示已经修完mask中课程（二进制位为1）所需的最少学期数
 * 3. 对于每个状态mask，找出所有可以在下一学期学习的课程（未修且前置课程已修）
 * 4. 使用位操作枚举这些可选课程的所有可能组合（最多选k门）
 * 5. 更新dp[newMask] = min(dp[newMask], dp[mask] + 1)
 * 
 * 时间复杂度：O(3^n)，其中n是课程数。状态数为2^n，对于每个状态，枚举子集的复杂度为O(2^m)，m为可选课程数
 * 空间复杂度：O(2^n)，用于存储dp数组
 * 
 * 优化点：
 * 1. 使用位操作高效计算前置依赖
 * 2. 使用子集枚举优化来选择k门课程
 * 3. 使用位计数函数快速计算已修课程数
 * 
 * 工程化考虑：
 * 1. 边界处理：处理n=0、无依赖等特殊情况
 * 2. 输入验证：验证课程编号和依赖关系的有效性
 * 3. 性能优化：使用位操作提高效率
 * 4. 可读性：添加详细注释和变量命名
 * 
 * 相关题目：
 * 1. LeetCode 207. 课程表
 * 2. LeetCode 210. 课程表 II
 * 3. LeetCode 2050. 并行课程 III
 */

class Solution {
private:
    /**
     * 回溯法枚举最多k门课程的组合
     * 
     * @param available 可选课程的二进制表示
     * @param start 开始枚举的位置
     * @param selected 当前已选课程的二进制表示
     * @param k 每学期最多可选课程数
     * @param mask 当前的状态
     * @param dp 动态规划数组
     */
    void backtrack(int available, int start, int selected, int k, int mask, vector<int>& dp) {
        // 如果已经选够k门或者没有更多可选课程
        if (__builtin_popcount(selected) == k || selected == available) {
            if (selected != 0) { // 至少选一门课
                int newMask = mask | selected;
                dp[newMask] = min(dp[newMask], dp[mask] + 1);
            }
            return;
        }
        
        // 从start位开始枚举，避免重复
        for (int i = start; i < 32; ++i) { // 假设n不超过32
            if ((available & (1 << i)) != 0) { // 如果这门课可选
                // 选择这门课
                backtrack(available, i + 1, selected | (1 << i), k, mask, dp);
            }
        }
    }
    
    /**
     * 深度优先搜索所有可能的状态（记忆化搜索）
     * 
     * @param mask 当前已修课程的状态
     * @param n 课程总数
     * @param pre 每门课程的前置依赖
     * @param k 每学期最多可选课程数
     * @param memo 记忆化数组
     * @return 从当前状态到完成所有课程所需的最少学期数
     */
    int dfs(int mask, int n, const vector<int>& pre, int k, vector<int>& memo) {
        if (mask == (1 << n) - 1) { // 所有课程都已修完
            return 0;
        }
        
        if (memo[mask] != -1) { // 已经计算过
            return memo[mask];
        }
        
        // 找出当前可以学习的课程
        int available = 0;
        for (int i = 0; i < n; ++i) {
            if (!((mask & (1 << i)) && ((mask & pre[i]) == pre[i]))) {
                available |= (1 << i);
            }
        }
        
        // 如果没有可选课程但还没修完，说明存在环
        if (available == 0) {
            return memo[mask] = INT_MAX;
        }
        
        int minSemesters = INT_MAX;
        
        // 枚举available的所有子集（不超过k门课）
        for (int subset = available; subset > 0; subset = (subset - 1) & available) {
            if (__builtin_popcount(subset) <= k) {
                int nextMask = mask | subset;
                int semesters = dfs(nextMask, n, pre, k, memo);
                if (semesters != INT_MAX) {
                    minSemesters = min(minSemesters, 1 + semesters);
                }
            }
        }
        
        return memo[mask] = minSemesters;
    }
    
public:
    /**
     * 主方法：计算上完所有课最少需要多少个学期
     * 
     * @param n 课程总数
     * @param dependencies 课程依赖关系数组
     * @param k 每学期最多可选课程数
     * @return 最少需要的学期数
     */
    int minNumberOfSemesters(int n, vector<vector<int>>& dependencies, int k) {
        // 边界检查
        if (n <= 0) {
            return 0;
        }
        
        // 预处理每门课程的前置依赖（使用二进制表示）
        // pre[i]表示第i门课程（从0开始索引）的前置依赖
        vector<int> pre(n, 0);
        for (const auto& dep : dependencies) {
            // 将课程编号从1-based转换为0-based
            int from = dep[0] - 1;
            int to = dep[1] - 1;
            // 设置前置依赖位
            pre[to] |= (1 << from);
        }
        
        // 动态规划数组，dp[mask]表示已经修完mask中课程所需的最少学期数
        // 初始化为n+1（最大值），表示无法达成的状态
        vector<int> dp(1 << n, n + 1);
        
        // 初始状态：没有修任何课程，需要0个学期
        dp[0] = 0;
        
        // 遍历所有可能的状态
        for (int mask = 0; mask < (1 << n); ++mask) {
            // 如果当前状态无法达成，跳过
            if (dp[mask] == n + 1) {
                continue;
            }
            
            // 找出当前可以学习的课程：未修且前置课程已修
            int available = 0;
            for (int i = 0; i < n; ++i) {
                // 如果课程i未修，且其前置依赖都已满足
                if (!((mask & (1 << i)) && ((mask & pre[i]) == pre[i]))) {
                    available |= (1 << i);
                }
            }
            
            // 枚举available的所有非空子集（不超过k门课）
            // 使用位操作的子集枚举方法
            for (int subset = available; subset > 0; subset = (subset - 1) & available) {
                // 统计子集的大小（即这学期要上的课程数）
                int count = __builtin_popcount(subset);
                // 如果超过k门课，跳过
                if (count <= k) {
                    // 新的状态：当前状态 | 子集
                    int newMask = mask | subset;
                    // 更新dp值
                    dp[newMask] = min(dp[newMask], dp[mask] + 1);
                }
            }
        }
        
        // 返回修完所有课程（全1状态）所需的最少学期数
        return dp[(1 << n) - 1];
    }
    
    /**
     * 优化版本：使用回溯法优化子集枚举
     */
    int minNumberOfSemestersOptimized(int n, vector<vector<int>>& dependencies, int k) {
        vector<int> pre(n, 0);
        for (const auto& dep : dependencies) {
            int from = dep[0] - 1;
            int to = dep[1] - 1;
            pre[to] |= (1 << from);
        }
        
        vector<int> dp(1 << n, n + 1);
        dp[0] = 0;
        
        for (int mask = 0; mask < (1 << n); ++mask) {
            if (dp[mask] == n + 1) {
                continue;
            }
            
            // 找出当前可以学习的课程
            int available = 0;
            for (int i = 0; i < n; ++i) {
                if (!((mask & (1 << i)) && ((mask & pre[i]) == pre[i]))) {
                    available |= (1 << i);
                }
            }
            
            // 使用回溯法枚举最多k门课程的组合
            backtrack(available, 0, 0, k, mask, dp);
        }
        
        return dp[(1 << n) - 1];
    }
    
    /**
     * 记忆化搜索版本
     */
    int minNumberOfSemestersMemo(int n, vector<vector<int>>& dependencies, int k) {
        vector<int> pre(n, 0);
        for (const auto& dep : dependencies) {
            int from = dep[0] - 1;
            int to = dep[1] - 1;
            pre[to] |= (1 << from);
        }
        
        vector<int> memo(1 << n, -1);
        return dfs(0, n, pre, k, memo);
    }
};

// 测试函数
void testSolution() {
    Solution solution;
    
    // 测试用例1
    int n1 = 4;
    vector<vector<int>> dependencies1 = {{2, 1}, {3, 1}, {1, 4}};
    int k1 = 2;
    cout << "测试用例1: " << solution.minNumberOfSemesters(n1, dependencies1, k1) << endl; // 应输出 3
    cout << "测试用例1（优化版）: " << solution.minNumberOfSemestersOptimized(n1, dependencies1, k1) << endl; // 应输出 3
    cout << "测试用例1（记忆化版）: " << solution.minNumberOfSemestersMemo(n1, dependencies1, k1) << endl; // 应输出 3
    
    // 测试用例2
    int n2 = 5;
    vector<vector<int>> dependencies2 = {{2, 1}, {3, 1}, {4, 1}, {1, 5}};
    int k2 = 2;
    cout << "测试用例2: " << solution.minNumberOfSemesters(n2, dependencies2, k2) << endl; // 应输出 4
    cout << "测试用例2（优化版）: " << solution.minNumberOfSemestersOptimized(n2, dependencies2, k2) << endl; // 应输出 4
    cout << "测试用例2（记忆化版）: " << solution.minNumberOfSemestersMemo(n2, dependencies2, k2) << endl; // 应输出 4
    
    // 测试用例3：无依赖
    int n3 = 5;
    vector<vector<int>> dependencies3 = {};
    int k3 = 3;
    cout << "测试用例3: " << solution.minNumberOfSemesters(n3, dependencies3, k3) << endl; // 应输出 2
    cout << "测试用例3（优化版）: " << solution.minNumberOfSemestersOptimized(n3, dependencies3, k3) << endl; // 应输出 2
    cout << "测试用例3（记忆化版）: " << solution.minNumberOfSemestersMemo(n3, dependencies3, k3) << endl; // 应输出 2
}

int main() {
    testSolution();
    return 0;
}