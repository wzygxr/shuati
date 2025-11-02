#include <iostream>
#include <vector>
#include <deque>
#include <algorithm>
#include <climits>

using namespace std;

/**
 * 题目名称：LeetCode 1696. 跳跃游戏 VI
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/jump-game-vi/
 * 题目难度：中等
 * 
 * 题目描述：
 * 给你一个下标从 0 开始的整数数组 nums 和一个整数 k。
 * 一开始你在下标 0 处。每一步，你最多可以往前跳 k 步，但你不能跳出数组的边界。
 * 也就是说，你可以从下标 i 跳到 [i+1, min(n-1, i+k)] 包含两个端点的任意位置。
 * 你的目标是到达数组最后一个位置（下标为 n-1 处），你得到的分数为经过的所有数字之和。
 * 请你返回你能得到的 最大分数。
 * 
 * 解题思路：
 * 使用动态规划 + 单调队列优化
 * 1. dp[i] 表示到达位置 i 时能获得的最大分数
 * 2. 状态转移：dp[i] = nums[i] + max(dp[j])，其中 j ∈ [max(0, i-k), i-1]
 * 3. 使用单调递减队列维护窗口 [i-k, i-1] 内的最大 dp 值
 * 4. 队列中存储索引，对应的 dp 值保持单调递减
 *
 * 算法步骤：
 * 1. 初始化 dp 数组，dp[0] = nums[0]
 * 2. 初始化单调递减队列，将 0 加入队列
 * 3. 遍历数组从 1 到 n-1：
 *    a. 移除队列中超出窗口范围的索引
 *    b. dp[i] = nums[i] + dp[队列头部]
 *    c. 维护队列单调性，移除尾部所有 dp 值小于等于 dp[i] 的索引
 *    d. 将 i 加入队列
 * 4. 返回 dp[n-1]
 *
 * 时间复杂度分析：
 * O(n) - 每个元素最多入队出队一次
 *
 * 空间复杂度分析：
 * O(n) - dp 数组和单调队列
 *
 * 是否最优解：
 * ✅ 是，这是处理此类问题的最优时间复杂度解法
 * 
 * 工程化考量：
 * - 使用STL deque提高代码可读性
 * - 考虑边界条件处理（k=0, 数组长度为1等）
 * - 处理极端输入情况（大数组、极限值等）
 */

class Solution {
public:
    int maxResult(vector<int>& nums, int k) {
        if (nums.empty()) {
            return 0;
        }
        
        int n = nums.size();
        if (n == 1) {
            return nums[0];
        }
        
        // dp[i] 表示到达位置 i 时的最大分数
        vector<int> dp(n);
        dp[0] = nums[0];
        
        // 使用单调递减队列维护窗口内的最大 dp 值
        // 队列中存储索引，对应的 dp 值保持单调递减
        deque<int> dq;
        dq.push_back(0);
        
        for (int i = 1; i < n; i++) {
            // 移除队列中超出窗口范围的索引
            // 窗口范围为 [i-k, i-1]
            while (!dq.empty() && dq.front() < i - k) {
                dq.pop_front();
            }
            
            // 计算当前位置的最大分数
            // dp[i] = 当前值 + 窗口内的最大 dp 值
            dp[i] = nums[i] + dp[dq.front()];
            
            // 维护队列的单调递减性质
            // 从队尾开始，移除所有 dp 值小于等于当前 dp[i] 的索引
            while (!dq.empty() && dp[dq.back()] <= dp[i]) {
                dq.pop_back();
            }
            
            // 将当前索引加入队列
            dq.push_back(i);
        }
        
        return dp[n - 1];
    }
};

/**
 * 测试函数 - 包含多种边界情况和测试用例
 */
void testMaxResult() {
    Solution solution;
    cout << "=== LeetCode 1696 测试用例 ===" << endl;
    
    // 测试用例1：基础示例
    vector<int> nums1 = {1, -1, -2, 4, -7, 3};
    int k1 = 2;
    int result1 = solution.maxResult(nums1, k1);
    cout << "测试用例1 - 输入: [1,-1,-2,4,-7,3], k=2" << endl;
    cout << "预期输出: 7, 实际输出: " << result1 << endl;
    cout << "测试结果: " << (result1 == 7 ? "✓ 通过" : "✗ 失败") << endl;
    
    // 测试用例2：全部为正数
    vector<int> nums2 = {10, -5, -2, 4, 0, 3};
    int k2 = 3;
    int result2 = solution.maxResult(nums2, k2);
    cout << "\n测试用例2 - 输入: [10,-5,-2,4,0,3], k=3" << endl;
    cout << "预期输出: 17, 实际输出: " << result2 << endl;
    cout << "测试结果: " << (result2 == 17 ? "✓ 通过" : "✗ 失败") << endl;
    
    // 测试用例3：单个元素
    vector<int> nums3 = {100};
    int k3 = 1;
    int result3 = solution.maxResult(nums3, k3);
    cout << "\n测试用例3 - 输入: [100], k=1" << endl;
    cout << "预期输出: 100, 实际输出: " << result3 << endl;
    cout << "测试结果: " << (result3 == 100 ? "✓ 通过" : "✗ 失败") << endl;
    
    // 测试用例4：k=1的特殊情况
    vector<int> nums4 = {1, -5, -20, 4, -1, 3, -6, -4};
    int k4 = 1;
    int result4 = solution.maxResult(nums4, k4);
    cout << "\n测试用例4 - 输入: [1,-5,-20,4,-1,3,-6,-4], k=1" << endl;
    cout << "预期输出: -3, 实际输出: " << result4 << endl;
    cout << "测试结果: " << (result4 == -3 ? "✓ 通过" : "✗ 失败") << endl;
    
    // 测试用例5：k等于数组长度
    vector<int> nums5 = {1, -1, -2, 4, -7, 3};
    int k5 = 6;
    int result5 = solution.maxResult(nums5, k5);
    cout << "\n测试用例5 - 输入: [1,-1,-2,4,-7,3], k=6" << endl;
    cout << "预期输出: 7, 实际输出: " << result5 << endl;
    cout << "测试结果: " << (result5 == 7 ? "✓ 通过" : "✗ 失败") << endl;
    
    cout << "\n=== 算法分析 ===" << endl;
    cout << "时间复杂度: O(n) - 每个元素最多入队出队一次" << endl;
    cout << "空间复杂度: O(n) - dp数组和单调队列" << endl;
    cout << "最优解: ✅ 是" << endl;
    
    cout << "\n=== C++语言特性分析 ===" << endl;
    cout << "1. 使用STL deque容器，自动管理内存" << endl;
    cout << "2. 强类型系统，编译时类型检查" << endl;
    cout << "3. 模板编程，泛型支持" << endl;
    cout << "4. RAII机制，自动资源管理" << endl;
}

int main() {
    testMaxResult();
    return 0;
}