#include <vector>
#include <algorithm>
#include <stdexcept>
#include <iostream>

using namespace std;

/**
 * 环形数组中不能选相邻元素的最大累加和（打家劫舍 II）- C++实现
 * 给定一个数组nums，长度为n
 * nums是一个环形数组，下标0和下标n-1是连在一起的
 * 可以随意选择数字，但是不能选择相邻的数字
 * 返回能得到的最大累加和
 * 测试链接 : https://leetcode.cn/problems/house-robber-ii/
 * 
 * 算法核心思想：
 * 1. 环形数组的打家劫舍问题可以分解为两个线性问题：
 *    a) 不偷第一个房屋（考虑nums[1...n-1]）
 *    b) 偷第一个房屋（考虑nums[0] + nums[2...n-2]）
 * 2. 取这两种情况的最大值作为最终结果
 * 3. 使用动态规划解决线性打家劫舍问题
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n) - 需要遍历数组两次
 * - 空间复杂度：O(1) - 优化后只需常数空间
 * 
 * 工程化考量：
 * 1. 边界处理：处理空数组、单元素数组等特殊情况
 * 2. 异常防御：处理索引越界等错误情况
 * 3. 性能优化：使用空间优化的动态规划
 */

/**
 * 计算线性数组的打家劫舍最大金额（空间优化版本）
 * 
 * @param nums 原始数组
 * @param l 起始索引（包含）
 * @param r 结束索引（包含）
 * @return 指定范围内的最大打家劫舍金额
 */
int best(vector<int>& nums, int l, int r) {
    // 边界检查：空范围
    if (l > r) {
        return 0;
    }
    // 单元素范围
    if (l == r) {
        return nums[l];
    }
    // 双元素范围
    if (l + 1 == r) {
        return max(nums[l], nums[r]);
    }
    
    // 空间优化的动态规划
    int prepre = nums[l];  // dp[i-2]
    int pre = max(nums[l], nums[l + 1]);  // dp[i-1]
    
    // 从第三个元素开始遍历
    for (int i = l + 2; i <= r; i++) {
        // 状态转移：选择偷或不偷当前房屋
        int current = max(pre, nums[i] + max(0, prepre));
        // 更新状态
        prepre = pre;
        pre = current;
    }
    
    return pre;
}

/**
 * 计算环形数组的打家劫舍最大金额
 * 
 * @param nums 环形数组，表示每个房屋的金额
 * @return 最大可偷窃金额
 * @throws invalid_argument 如果输入数组为空
 */
int rob(vector<int>& nums) {
    // 边界检查
    if (nums.empty()) {
        throw invalid_argument("输入数组不能为空");
    }
    
    int n = nums.size();
    // 特殊情况：单元素数组
    if (n == 1) {
        return nums[0];
    }
    
    // 情况1：不偷第一个房屋（考虑nums[1...n-1]）
    int case1 = best(nums, 1, n - 1);
    // 情况2：偷第一个房屋（考虑nums[0] + nums[2...n-2]）
    int case2 = nums[0] + best(nums, 2, n - 2);
    
    // 返回两种情况的最大值
    return max(case1, case2);
}

/**
 * 方法二：使用状态机思想的另一种实现
 * 分别计算包含第一个房屋和不包含第一个房屋的情况
 */
int robStateMachine(vector<int>& nums) {
    if (nums.empty()) {
        return 0;
    }
    
    int n = nums.size();
    if (n == 1) {
        return nums[0];
    }
    
    // 情况1：不包含第一个房屋
    int notRobFirst = 0;
    int robFirst = 0;
    
    for (int i = 1; i < n; i++) {
        int temp = notRobFirst;
        notRobFirst = max(notRobFirst, robFirst);
        robFirst = temp + nums[i];
    }
    int case1 = max(notRobFirst, robFirst);
    
    // 情况2：包含第一个房屋（不能包含最后一个房屋）
    notRobFirst = 0;
    robFirst = nums[0];
    
    for (int i = 1; i < n - 1; i++) {
        int temp = notRobFirst;
        notRobFirst = max(notRobFirst, robFirst);
        robFirst = temp + nums[i];
    }
    int case2 = max(notRobFirst, robFirst);
    
    return max(case1, case2);
}

/**
 * 测试函数：验证算法正确性
 */
void testHouseRobberII() {
    vector<vector<int>> testCases = {
        {2, 3, 2},           // 期望: 3
        {1},                 // 期望: 1
        {1, 2},              // 期望: 2
        {1, 2, 3, 1},        // 期望: 4
        {5, 10, 5, 10, 5},   // 期望: 20
        {2, 7, 9, 3, 1},     // 期望: 11
        {4, 1, 2, 7, 5, 3, 1} // 期望: 14
    };
    
    vector<int> expected = {3, 1, 2, 4, 20, 11, 14};
    
    cout << "=== 环形打家劫舍算法测试 ===" << endl;
    
    for (size_t i = 0; i < testCases.size(); ++i) {
        int result1 = rob(testCases[i]);
        int result2 = robStateMachine(testCases[i]);
        
        cout << "测试用例 " << i+1 << ": ";
        cout << "输入: [";
        for (size_t j = 0; j < testCases[i].size(); ++j) {
            cout << testCases[i][j];
            if (j < testCases[i].size() - 1) cout << ", ";
        }
        cout << "]";
        cout << ", 结果=" << result1 << ", 期望=" << expected[i];
        cout << ", 状态=" << (result1 == expected[i] ? "通过" : "失败");
        cout << ", 方法一致性=" << (result1 == result2 ? "一致" : "不一致") << endl;
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
    testHouseRobberII();
    
    // 运行性能测试（可选）
    // performanceTest();
    
    return 0;
}

/*
 * 扩展思考与工程化考量：
 * 
 * 1. 算法变体分析：
 *    - 树形打家劫舍（LeetCode 337）：扩展到树形结构
 *    - 打家劫舍 IV（LeetCode 2560）：增加约束条件
 *    - 删除并获得点数（LeetCode 740）：转化为打家劫舍问题
 * 
 * 2. 工程应用场景：
 *    - 环形资源分配：优化资源利用效率
 *    - 任务调度：避免相邻任务冲突
 *    - 投资组合：环形约束下的最优投资
 * 
 * 3. 性能优化策略：
 *    - 空间优化：使用常数空间代替数组
 *    - 并行计算：两个子问题可以并行处理
 *    - 缓存友好：顺序访问数组元素
 * 
 * 4. 代码质量保证：
 *    - 单元测试：覆盖各种边界情况
 *    - 性能测试：验证大数据量下的表现
 *    - 异常处理：确保程序的健壮性
 */