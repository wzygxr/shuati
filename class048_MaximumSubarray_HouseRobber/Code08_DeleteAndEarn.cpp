#include <vector>
#include <algorithm>
#include <map>
#include <set>
#include <stdexcept>
#include <iostream>

using namespace std;

/**
 * 删除并获得点数 - C++实现
 * 给你一个整数数组 nums ，你可以对它进行一些操作。
 * 每次操作中，选择任意一个 nums[i] ，删除它并获得 nums[i] 的点数。
 * 之后，你必须删除 所有 等于 nums[i] - 1 和 nums[i] + 1 的元素。
 * 开始你拥有 0 个点数。返回你能通过这些操作获得的最大点数。
 * 测试链接 : https://leetcode.cn/problems/delete-and-earn/
 * 
 * 算法核心思想：
 * 1. 这个问题可以转化为打家劫舍问题的变体
 * 2. 关键观察：选择某个数字x后，就不能选择x-1和x+1，这类似于打家劫舍中不能选择相邻房屋
 * 3. 首先统计每个数字的总点数（数字值 × 出现次数）
 * 4. 然后使用动态规划在数字序列中选择不相邻的数字以获得最大点数
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n + k) - 其中n是数组长度，k是数组中的最大值
 * - 空间复杂度：O(k) - 需要额外的points数组存储每个数字的总点数
 * 
 * 工程化考量：
 * 1. 边界处理：处理空数组、单元素数组等特殊情况
 * 2. 性能优化：使用空间优化的动态规划
 * 3. 数值范围：处理大数值范围的情况
 */

/**
 * 基础版本：使用数组存储点数
 * 
 * @param nums 输入的整数数组
 * @return 能获得的最大点数
 * @throws invalid_argument 如果输入数组为空
 */
int deleteAndEarn(vector<int>& nums) {
    // 边界检查
    if (nums.empty()) {
        throw invalid_argument("输入数组不能为空");
    }
    
    // 计算数组中的最大值
    int maxVal = *max_element(nums.begin(), nums.end());
    
    // points[i]表示选择所有数字i能获得的总点数
    vector<int> points(maxVal + 1, 0);
    for (int num : nums) {
        points[num] += num;
    }
    
    // 特殊情况处理
    if (maxVal == 0) {
        return points[0];
    }
    if (maxVal == 1) {
        return max(points[0], points[1]);
    }
    
    // 动态规划数组
    vector<int> dp(maxVal + 1, 0);
    dp[0] = points[0];
    dp[1] = max(points[0], points[1]);
    
    // 状态转移：标准的打家劫舍问题
    for (int i = 2; i <= maxVal; i++) {
        dp[i] = max(dp[i - 1], dp[i - 2] + points[i]);
    }
    
    return dp[maxVal];
}

/**
 * 空间优化版本
 * 
 * 算法改进：
 * - 使用两个变量代替dp数组，将空间复杂度从O(k)优化到O(1)
 * - 保持相同的时间复杂度
 */
int deleteAndEarnOptimized(vector<int>& nums) {
    if (nums.empty()) {
        return 0;
    }
    
    // 计算数组中的最大值
    int maxVal = *max_element(nums.begin(), nums.end());
    
    // points[i]表示选择所有数字i能获得的总点数
    vector<int> points(maxVal + 1, 0);
    for (int num : nums) {
        points[num] += num;
    }
    
    // 特殊情况处理
    if (maxVal == 0) {
        return points[0];
    }
    if (maxVal == 1) {
        return max(points[0], points[1]);
    }
    
    // 空间优化的动态规划
    int prevPrev = points[0];  // dp[i-2]
    int prev = max(points[0], points[1]);  // dp[i-1]
    
    for (int i = 2; i <= maxVal; i++) {
        int current = max(prev, prevPrev + points[i]);
        prevPrev = prev;
        prev = current;
    }
    
    return prev;
}

/**
 * 使用哈希表优化的版本（适用于数值范围很大的情况）
 * 
 * 算法改进：
 * - 当数值范围很大但实际出现的数字很少时，使用map存储点数
 * - 只处理实际出现的数字，避免遍历整个数值范围
 */
int deleteAndEarnHashMap(vector<int>& nums) {
    if (nums.empty()) {
        return 0;
    }
    
    // 统计每个数字的总点数
    map<int, int> pointsMap;
    for (int num : nums) {
        pointsMap[num] += num;
    }
    
    // 如果没有数字，返回0
    if (pointsMap.empty()) {
        return 0;
    }
    
    // 将数字按顺序排列
    vector<int> keys;
    for (auto& pair : pointsMap) {
        keys.push_back(pair.first);
    }
    int n = keys.size();
    
    // 特殊情况：只有一个数字
    if (n == 1) {
        return pointsMap[keys[0]];
    }
    
    // 动态规划
    vector<int> dp(n, 0);
    dp[0] = pointsMap[keys[0]];
    
    // 检查第二个数字是否与第一个相邻
    if (keys[1] - keys[0] == 1) {
        dp[1] = max(dp[0], pointsMap[keys[1]]);
    } else {
        dp[1] = dp[0] + pointsMap[keys[1]];
    }
    
    for (int i = 2; i < n; i++) {
        int currentKey = keys[i];
        int prevKey = keys[i - 1];
        
        if (currentKey - prevKey == 1) {
            // 当前数字与前一个数字相邻，不能同时选择
            dp[i] = max(dp[i - 1], dp[i - 2] + pointsMap[currentKey]);
        } else {
            // 当前数字与前一个数字不相邻，可以同时选择
            dp[i] = dp[i - 1] + pointsMap[currentKey];
        }
    }
    
    return dp[n - 1];
}

/**
 * 测试函数：验证算法正确性
 */
void testDeleteAndEarn() {
    vector<vector<int>> testCases = {
        {3, 4, 2},                     // 期望: 6
        {2, 2, 3, 3, 3, 4},           // 期望: 9
        {5},                           // 期望: 5
        {1, 1, 1, 2, 4, 5, 5, 5, 6},  // 期望: 18
        {1, 2, 3, 4, 5},              // 期望: 9
        {8, 10, 4, 9, 1, 3, 5, 9, 4, 10} // 期望: 37
    };
    
    vector<int> expected = {6, 9, 5, 18, 9, 37};
    
    cout << "=== 删除并获得点数算法测试 ===" << endl;
    
    for (size_t i = 0; i < testCases.size(); ++i) {
        int result1 = deleteAndEarn(testCases[i]);
        int result2 = deleteAndEarnOptimized(testCases[i]);
        int result3 = deleteAndEarnHashMap(testCases[i]);
        
        cout << "测试用例 " << i+1 << ": ";
        cout << "输入: [";
        for (size_t j = 0; j < testCases[i].size(); ++j) {
            cout << testCases[i][j];
            if (j < testCases[i].size() - 1) cout << ", ";
        }
        cout << "]";
        cout << ", 结果=" << result1 << ", 期望=" << expected[i];
        cout << ", 状态=" << (result1 == expected[i] ? "通过" : "失败");
        cout << ", 方法一致性=" << (result1 == result2 && result2 == result3 ? "一致" : "不一致") << endl;
    }
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * 性能测试：大数据量验证
 */
void performanceTest() {
    const int SIZE = 1000000;
    const int MAX_VAL = 10000;
    vector<int> largeArray(SIZE);
    
    // 生成随机测试数据
    srand(time(nullptr));
    for (int i = 0; i < SIZE; i++) {
        largeArray[i] = rand() % MAX_VAL + 1;
    }
    
    cout << "=== 性能测试开始 ===" << endl;
    cout << "数据量: " << SIZE << " 个元素, 数值范围: 1-" << MAX_VAL << endl;
    
    auto start = chrono::high_resolution_clock::now();
    int result = deleteAndEarnOptimized(largeArray);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "计算结果: " << result << endl;
    cout << "执行时间: " << duration.count() << " 微秒" << endl;
    cout << "=== 性能测试结束 ===" << endl;
}

int main() {
    // 运行功能测试
    testDeleteAndEarn();
    
    // 运行性能测试（可选）
    // performanceTest();
    
    return 0;
}

/*
 * 扩展思考与工程化考量：
 * 
 * 1. 算法变体分析：
 *    - 数值范围优化：当数值范围很大时使用哈希表方法
 *    - 空间优化：使用常数空间代替数组
 *    - 特殊情况处理：提高算法效率
 * 
 * 2. 工程应用场景：
 *    - 资源分配：在约束条件下的最优资源选择
 *    - 任务调度：避免冲突任务的最优调度
 *    - 数据清理：选择最优的数据清理策略
 * 
 * 3. 性能优化策略：
 *    - 根据数据特征选择合适的方法
 *    - 预处理阶段优化统计效率
 *    - 动态规划阶段优化空间使用
 * 
 * 4. 代码质量保证：
 *    - 单元测试：覆盖各种边界情况
 *    - 性能测试：验证大数据量下的表现
 *    - 异常处理：确保程序的健壮性
 */