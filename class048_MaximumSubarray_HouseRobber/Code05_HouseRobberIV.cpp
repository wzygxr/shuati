#include <vector>
#include <algorithm>
#include <climits>
#include <stdexcept>
#include <iostream>

using namespace std;

/**
 * 打家劫舍 IV - C++实现
 * 沿街有一排连续的房屋。每间房屋内都藏有一定的现金
 * 现在有一位小偷计划从这些房屋中窃取现金
 * 由于相邻的房屋装有相互连通的防盗系统，所以小偷不会窃取相邻的房屋
 * 小偷的 窃取能力 定义为他在窃取过程中能从单间房屋中窃取的 最大金额
 * 给你一个整数数组 nums 表示每间房屋存放的现金金额
 * 第i间房屋中放有nums[i]的钱数
 * 另给你一个整数k，表示小偷需要窃取至少 k 间房屋
 * 返回小偷需要的最小窃取能力值
 * 测试链接 : https://leetcode.cn/problems/house-robber-iv/
 * 
 * 算法核心思想：
 * 1. 这是一个二分搜索 + 动态规划（或贪心）的问题
 * 2. 关键观察：窃取能力值越大，能偷的房屋越多（单调性）
 * 3. 使用二分搜索在[min(nums), max(nums)]范围内寻找最小满足条件的ability
 * 4. 对于每个候选ability，计算最多能偷多少间房屋
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n * log(max-min)) - 二分搜索 + 线性验证
 * - 空间复杂度：O(1) - 优化后只需常数空间
 * 
 * 工程化考量：
 * 1. 边界处理：处理空数组、k=0等特殊情况
 * 2. 性能优化：使用贪心算法代替动态规划进行验证
 * 3. 鲁棒性：处理数值范围和边界条件
 */

/**
 * 贪心算法：计算给定ability时最多能偷多少间房屋
 * 
 * 算法原理：
 * - 贪心策略：只要能偷就偷，然后跳过下一个房屋
 * - 这种策略在打家劫舍约束下是最优的
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * @param nums 房屋金额数组
 * @param ability 窃取能力值
 * @return 最多能偷的房屋数量
 */
int mostRobGreedy(vector<int>& nums, int ability) {
    int count = 0;
    int i = 0;
    int n = nums.size();
    
    while (i < n) {
        if (nums[i] <= ability) {
            // 偷当前房屋，然后跳过下一个
            count++;
            i += 2; // 跳过下一个房屋
        } else {
            // 不能偷当前房屋，检查下一个
            i++;
        }
    }
    
    return count;
}

/**
 * 计算小偷需要的最小窃取能力值
 * 
 * 算法原理：
 * - 二分搜索：在房屋金额的最小值和最大值之间搜索
 * - 验证函数：对于每个候选ability，计算最多能偷多少间房屋
 * - 单调性：ability越大，能偷的房屋越多
 * 
 * 时间复杂度：O(n * log(max-min))
 * 空间复杂度：O(1)
 * 
 * @param nums 房屋金额数组
 * @param k 需要窃取的最小房屋数量
 * @return 最小窃取能力值
 * @throws invalid_argument 如果输入无效
 */
int minCapability(vector<int>& nums, int k) {
    // 边界检查
    if (nums.empty()) {
        throw invalid_argument("输入数组不能为空");
    }
    if (k <= 0) {
        throw invalid_argument("k必须大于0");
    }
    if (k > nums.size()) {
        throw invalid_argument("k不能大于数组长度");
    }
    
    int n = nums.size();
    // 确定二分搜索的范围 [min, max]
    int minVal = nums[0];
    int maxVal = nums[0];
    for (int i = 1; i < n; i++) {
        minVal = min(minVal, nums[i]);
        maxVal = max(maxVal, nums[i]);
    }
    
    // 特殊情况：k=1时，最小能力值就是数组中的最小值
    if (k == 1) {
        return minVal;
    }
    
    // 二分搜索：在[minVal, maxVal]范围内寻找最小满足条件的ability
    int left = minVal;
    int right = maxVal;
    int answer = maxVal; // 初始化为最大值
    
    while (left <= right) {
        int mid = left + (right - left) / 2; // 防止溢出
        // 验证当前ability是否能偷至少k间房屋
        if (mostRobGreedy(nums, mid) >= k) {
            answer = mid; // 当前ability满足条件，尝试更小的值
            right = mid - 1;
        } else {
            left = mid + 1; // 当前ability不满足条件，需要更大的值
        }
    }
    
    return answer;
}

/**
 * 动态规划版本：用于对比验证
 */
int mostRobDP(vector<int>& nums, int ability) {
    int n = nums.size();
    if (n == 1) {
        return nums[0] <= ability ? 1 : 0;
    }
    if (n == 2) {
        return (nums[0] <= ability || nums[1] <= ability) ? 1 : 0;
    }
    
    vector<int> dp(n, 0);
    dp[0] = nums[0] <= ability ? 1 : 0;
    dp[1] = (nums[0] <= ability || nums[1] <= ability) ? 1 : 0;
    
    for (int i = 2; i < n; i++) {
        dp[i] = max(dp[i-1], (nums[i] <= ability ? 1 : 0) + dp[i-2]);
    }
    
    return dp[n-1];
}

/**
 * 测试函数：验证算法正确性
 */
void testHouseRobberIV() {
    vector<pair<vector<int>, int>> testCases = {
        {{2, 3, 5, 9}, 2},           // 期望: 5
        {{2, 7, 9, 3, 1}, 2},        // 期望: 2
        {{1, 2, 3}, 1},              // 期望: 1
        {{4, 1, 2, 7, 5, 3, 1}, 3},  // 期望: 4
        {{1, 1, 1, 1}, 2},           // 期望: 1
        {{10, 5, 2, 8, 3}, 3}        // 期望: 5
    };
    
    vector<int> expected = {5, 2, 1, 4, 1, 5};
    
    cout << "=== 打家劫舍 IV 算法测试 ===" << endl;
    
    for (size_t i = 0; i < testCases.size(); ++i) {
        auto& testCase = testCases[i];
        int result = minCapability(testCase.first, testCase.second);
        int greedyResult = mostRobGreedy(testCase.first, result);
        int dpResult = mostRobDP(testCase.first, result);
        
        cout << "测试用例 " << i+1 << ": ";
        cout << "nums = [";
        for (size_t j = 0; j < testCase.first.size(); ++j) {
            cout << testCase.first[j];
            if (j < testCase.first.size() - 1) cout << ", ";
        }
        cout << "], k = " << testCase.second;
        cout << ", 结果 = " << result << ", 期望 = " << expected[i];
        cout << ", 状态 = " << (result == expected[i] ? "通过" : "失败");
        cout << ", 验证 = " << greedyResult << "/" << dpResult << endl;
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
    int result = minCapability(largeArray, SIZE / 2);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "计算结果: " << result << endl;
    cout << "执行时间: " << duration.count() << " 微秒" << endl;
    cout << "=== 性能测试结束 ===" << endl;
}

int main() {
    // 运行功能测试
    testHouseRobberIV();
    
    // 运行性能测试（可选）
    // performanceTest();
    
    return 0;
}

/*
 * 扩展思考与工程化考量：
 * 
 * 1. 算法变体分析：
 *    - 二分搜索应用：在满足单调性的问题中广泛使用
 *    - 贪心优化：在特定约束下的最优策略
 *    - 动态规划对比：验证贪心算法的正确性
 * 
 * 2. 工程应用场景：
 *    - 阈值优化：寻找满足条件的最小阈值
 *    - 资源分配：在约束条件下的最优分配
 *    - 调度问题：最小化最大负载
 * 
 * 3. 性能优化策略：
 *    - 二分搜索：对数级别的时间复杂度
 *    - 贪心算法：线性时间验证
 *    - 空间优化：常数级别空间复杂度
 * 
 * 4. 代码质量保证：
 *    - 单元测试：覆盖各种边界情况
 *    - 性能测试：验证大数据量下的表现
 *    - 算法验证：对比不同实现方法的结果
 */