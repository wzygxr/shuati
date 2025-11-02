#include <vector>
#include <algorithm>
#include <stdexcept>
#include <iostream>

using namespace std;

/**
 * 打家劫舍问题（数组中不能选相邻元素的最大累加和）- C++实现
 * 题目描述：给定一个数组，可以随意选择数字，但是不能选择相邻的数字，返回能得到的最大累加和。
 * 从另一个角度理解：你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，
 * 影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
 * 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你在不触动警报装置的情况下，
 * 一夜之内能够偷窃到的最高金额。
 * 
 * 测试链接：https://leetcode.cn/problems/house-robber/
 * 
 * 算法核心思想：
 * 这是一个经典的动态规划问题，对于每个位置，我们有两种选择：选或者不选。
 * 如果选当前元素，那么前一个元素就不能选；如果不选当前元素，那么可以选择前一个元素或不选。
 * 我们需要在每一步做出最优选择，使得最终的累加和最大。
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n) - 只需遍历数组一次
 * - 空间复杂度：O(1) - 优化后只需常数空间
 * 
 * 工程化考量：
 * 1. 异常处理：对空数组和边界情况进行处理
 * 2. 鲁棒性：处理极端输入（单元素、双元素、全零等）
 * 3. 性能优化：使用引用避免拷贝，使用const保证安全性
 */

/**
 * 方法一：动态规划（空间优化版本）
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * @param nums 输入数组（使用引用避免拷贝）
 * @return 最大可偷窃金额
 * @throws invalid_argument 如果输入为nullptr
 */
int rob(vector<int>& nums) {
    // 边界检查：处理空数组情况
    if (nums.empty()) {
        return 0;
    }
    
    int n = nums.size();
    // 处理特殊情况
    if (n == 1) {
        return nums[0];
    }
    if (n == 2) {
        return max(nums[0], nums[1]);
    }
    
    // 空间优化：只保存前两个状态
    int prevPrev = nums[0];  // dp[i-2]
    int prev = max(nums[0], nums[1]);  // dp[i-1]
    
    for (int i = 2; i < n; ++i) {
        // 状态转移：选择偷或不偷当前房屋
        int current = max(prev, prevPrev + nums[i]);
        // 更新状态
        prevPrev = prev;
        prev = current;
    }
    
    return prev;
}

/**
 * 方法二：动态规划（基础版本）- 用于教学和理解
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * @param nums 输入数组
 * @return 最大可偷窃金额
 */
int robDP(vector<int>& nums) {
    if (nums.empty()) {
        return 0;
    }
    
    int n = nums.size();
    if (n == 1) {
        return nums[0];
    }
    
    vector<int> dp(n, 0);
    dp[0] = nums[0];
    dp[1] = max(nums[0], nums[1]);
    
    for (int i = 2; i < n; ++i) {
        dp[i] = max(dp[i-1], dp[i-2] + nums[i]);
    }
    
    return dp[n-1];
}

/**
 * 方法三：另一种状态定义方式
 * 使用两个变量分别记录偷和不偷当前房屋的最大金额
 * 
 * @param nums 输入数组
 * @return 最大可偷窃金额
 */
int robStateMachine(vector<int>& nums) {
    if (nums.empty()) {
        return 0;
    }
    
    int notRob = 0;      // 不偷当前房屋的最大金额
    int rob = nums[0];   // 偷当前房屋的最大金额
    
    for (int i = 1; i < nums.size(); ++i) {
        // 保存上一轮的状态
        int prevNotRob = notRob;
        int prevRob = rob;
        
        // 当前不偷的最大金额 = 上一轮偷或不偷的最大值
        notRob = max(prevNotRob, prevRob);
        // 当前偷的最大金额 = 上一轮不偷的最大金额 + 当前房屋金额
        rob = prevNotRob + nums[i];
    }
    
    return max(notRob, rob);
}

/**
 * 测试函数：验证算法正确性
 */
void testRob() {
    vector<vector<int>> testCases = {
        {1, 2, 3, 1},        // 期望: 4
        {2, 7, 9, 3, 1},      // 期望: 12
        {5},                   // 期望: 5
        {},                    // 期望: 0
        {2, 1, 1, 2},         // 期望: 4
        {1, 3, 1},            // 期望: 3
        {4, 1, 2, 7, 5, 3, 1} // 期望: 14
    };
    
    vector<int> expected = {4, 12, 5, 0, 4, 3, 14};
    
    cout << "=== 打家劫舍算法测试 ===" << endl;
    
    for (size_t i = 0; i < testCases.size(); ++i) {
        int result1 = rob(testCases[i]);
        int result2 = robDP(testCases[i]);
        int result3 = robStateMachine(testCases[i]);
        
        cout << "测试用例 " << i+1 << ": ";
        cout << "结果=" << result1 << ", 期望=" << expected[i];
        cout << ", 状态=" << (result1 == expected[i] ? "通过" : "失败");
        cout << ", 方法一致性=";
        
        if (result1 == result2 && result2 == result3) {
            cout << "一致";
        } else {
            cout << "不一致(方法1:" << result1 << ", 方法2:" << result2 
                 << ", 方法3:" << result3 << ")";
        }
        cout << endl;
    }
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * 性能测试：大数据量验证
 */
void performanceTest() {
    const int SIZE = 1000000;
    vector<int> largeArray(SIZE, 1);  // 全1数组
    
    cout << "=== 性能测试开始 ===" << endl;
    cout << "数据量: " << SIZE << " 个元素" << endl;
    
    auto start = chrono::high_resolution_clock::now();
    int result = rob(largeArray);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "计算结果: " << result << endl;
    cout << "执行时间: " << duration.count() << " 微秒" << endl;
    cout << "=== 性能测试结束 ===" << endl;
}

int main() {
    // 运行功能测试
    testRob();
    
    // 运行性能测试（可选）
    // performanceTest();
    
    return 0;
}

/*
 * 扩展思考与工程化考量：
 * 
 * 1. 算法变体分析：
 *    - 环形房屋（LeetCode 213）：需要处理首尾相连的情况
 *    - 树形房屋（LeetCode 337）：扩展到树形结构
 *    - 删除并获得点数（LeetCode 740）：转化为打家劫舍问题
 * 
 * 2. 工程应用场景：
 *    - 资源分配：在约束条件下选择资源最大化收益
 *    - 任务调度：某些任务不能连续执行时的最优安排
 *    - 投资组合：存在互斥关系的投资项目选择
 * 
 * 3. 性能优化策略：
 *    - 空间优化：从O(n)到O(1)的空间复杂度优化
 *    - 缓存友好：减少内存访问，提高缓存命中率
 *    - 并行计算：对于大规模数据可以考虑并行处理
 * 
 * 4. 代码质量保证：
 *    - 单元测试：覆盖各种边界情况
 *    - 性能测试：验证大数据量下的表现
 *    - 代码审查：确保逻辑正确性和可读性
 */