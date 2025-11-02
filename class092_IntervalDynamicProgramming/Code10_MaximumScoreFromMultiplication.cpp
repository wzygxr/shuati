#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <string>
#include <sstream>
using namespace std;

// LeetCode 1770. 执行乘法运算的最大分数
// 给定两个数组nums和multipliers，每次从nums的头部或尾部取一个数与multipliers[i]相乘，求最大得分。
// 测试链接: https://leetcode.cn/problems/maximum-score-from-performing-multiplication-operations/
// 
// 解题思路:
// 1. 状态定义：dp[i][j]表示使用前i个multipliers，从nums左端取了j个元素的最大分数
// 2. 状态转移：考虑两种选择：取左端或取右端
// 3. 时间复杂度：O(m^2)，其中m是multipliers的长度
// 4. 空间复杂度：O(m^2)，可以优化到O(m)
//
// 工程化考量:
// 1. 异常处理：检查输入合法性
// 2. 边界处理：处理空数组和边界情况
// 3. 性能优化：使用滚动数组优化空间复杂度
// 4. 测试覆盖：设计全面的测试用例

/**
 * 区间DP解法 - 基本版本（空间复杂度O(m^2)）
 * 时间复杂度: O(m^2) - 其中m是multipliers的长度
 * 空间复杂度: O(m^2) - dp数组占用空间
 * 
 * 解题思路:
 * 1. 状态定义：dp[i][j]表示使用前i个multipliers，从nums左端取了j个元素的最大分数
 * 2. 状态转移：
 *    - 取左端：dp[i][j] = dp[i-1][j-1] + multipliers[i-1] * nums[j-1]
 *    - 取右端：dp[i][j] = dp[i-1][j] + multipliers[i-1] * nums[n - (i - j)]
 * 3. 初始化：dp[0][0] = 0
 * 4. 结果：max(dp[m][j]) for j in [0, m]
 */
int maximumScore(vector<int>& nums, vector<int>& multipliers) {
    // 异常处理
    if (nums.empty() || multipliers.empty()) {
        return 0;
    }
    
    int n = nums.size();
    int m = multipliers.size();
    
    // 状态定义：dp[i][j]表示使用前i个multipliers，从nums左端取了j个元素的最大分数
    vector<vector<int>> dp(m + 1, vector<int>(m + 1, INT_MIN));
    
    // 初始化：dp[0][0] = 0
    dp[0][0] = 0;
    
    int maxScore = INT_MIN;
    
    // 动态规划填表
    for (int i = 1; i <= m; i++) { // 使用前i个multipliers
        for (int j = 0; j <= i; j++) { // 从左端取了j个元素
            // 计算右端取的元素数量
            int rightCount = i - j;
            
            // 检查左端取j个元素是否合法
            if (j > 0) {
                // 选择取左端：第i个multiplier乘以nums中左端第j个元素
                int leftScore = dp[i - 1][j - 1] + multipliers[i - 1] * nums[j - 1];
                if (dp[i][j] < leftScore) {
                    dp[i][j] = leftScore;
                }
            }
            
            // 检查右端取rightCount个元素是否合法
            if (rightCount > 0 && j <= i - 1) {
                // 选择取右端：第i个multiplier乘以nums中右端第rightCount个元素
                int rightIndex = n - rightCount;
                int rightScore = dp[i - 1][j] + multipliers[i - 1] * nums[rightIndex];
                if (dp[i][j] < rightScore) {
                    dp[i][j] = rightScore;
                }
            }
            
            // 更新最大分数
            if (i == m) {
                if (maxScore < dp[i][j]) {
                    maxScore = dp[i][j];
                }
            }
        }
    }
    
    return maxScore;
}

/**
 * 优化版本 - 使用滚动数组将空间复杂度优化到O(m)
 * 时间复杂度: O(m^2)
 * 空间复杂度: O(m)
 * 
 * 优化思路:
 * 1. 观察状态转移方程，发现dp[i]只依赖于dp[i-1]
 * 2. 可以使用两个数组交替计算，减少空间占用
 * 3. 适用于大规模数据场景
 */
int maximumScoreOptimized(vector<int>& nums, vector<int>& multipliers) {
    // 异常处理
    if (nums.empty() || multipliers.empty()) {
        return 0;
    }
    
    int n = nums.size();
    int m = multipliers.size();
    
    // 使用两个数组进行滚动计算
    vector<int> dp(m + 1, INT_MIN); // 当前状态
    vector<int> prev(m + 1, INT_MIN); // 前一个状态
    
    // 初始化prev数组
    prev[0] = 0;
    
    // 动态规划填表
    for (int i = 1; i <= m; i++) {
        fill(dp.begin(), dp.end(), INT_MIN);
        
        for (int j = 0; j <= i; j++) {
            int rightCount = i - j;
            
            // 取左端
            if (j > 0 && prev[j - 1] != INT_MIN) {
                int leftScore = prev[j - 1] + multipliers[i - 1] * nums[j - 1];
                if (dp[j] < leftScore) {
                    dp[j] = leftScore;
                }
            }
            
            // 取右端
            if (rightCount > 0 && j <= i - 1 && prev[j] != INT_MIN) {
                int rightIndex = n - rightCount;
                int rightScore = prev[j] + multipliers[i - 1] * nums[rightIndex];
                if (dp[j] < rightScore) {
                    dp[j] = rightScore;
                }
            }
        }
        
        // 交换数组，准备下一轮计算
        swap(prev, dp);
    }
    
    // 寻找最大分数
    int maxScore = INT_MIN;
    for (int j = 0; j <= m; j++) {
        if (maxScore < prev[j]) {
            maxScore = prev[j];
        }
    }
    
    return maxScore;
}

/**
 * 记忆化搜索版本 - 递归+记忆化
 * 时间复杂度: O(m^2)
 * 空间复杂度: O(m^2)
 * 
 * 优点:
 * 1. 代码更直观，易于理解
 * 2. 避免不必要的状态计算
 * 缺点:
 * 1. 递归深度可能较大
 * 2. 栈空间开销
 */
int dfs(vector<int>& nums, vector<int>& multipliers, int left, int right, int idx, vector<vector<int>>& memo) {
    // 边界条件：所有multipliers都已使用
    if (idx == multipliers.size()) {
        return 0;
    }
    
    // 检查记忆化结果
    if (memo[left][idx] != INT_MIN) {
        return memo[left][idx];
    }
    
    // 选择取左端
    int takeLeft = multipliers[idx] * nums[left] + 
                  dfs(nums, multipliers, left + 1, right, idx + 1, memo);
    
    // 选择取右端
    int takeRight = multipliers[idx] * nums[right] + 
                   dfs(nums, multipliers, left, right - 1, idx + 1, memo);
    
    // 取较大值并记忆化
    int result = max(takeLeft, takeRight);
    memo[left][idx] = result;
    
    return result;
}

int maximumScoreMemo(vector<int>& nums, vector<int>& multipliers) {
    if (nums.empty() || multipliers.empty()) {
        return 0;
    }
    
    int n = nums.size();
    int m = multipliers.size();
    
    // 记忆化数组
    vector<vector<int>> memo(m, vector<int>(m, INT_MIN));
    
    return dfs(nums, multipliers, 0, n - 1, 0, memo);
}

/**
 * 单元测试方法 - 验证算法正确性
 */
void test() {
    // 测试用例1：示例输入
    vector<int> nums1 = {1, 2, 3};
    vector<int> multipliers1 = {3, 2, 1};
    int result1 = maximumScore(nums1, multipliers1);
    cout << "Test 1 - Expected: 14, Actual: " << result1 << endl;
    
    // 测试用例2：边界情况
    vector<int> nums2 = {1};
    vector<int> multipliers2 = {1};
    int result2 = maximumScore(nums2, multipliers2);
    cout << "Test 2 - Expected: 1, Actual: " << result2 << endl;
    
    // 测试用例3：大规模数据测试
    vector<int> nums3 = {1, 2, 3, 4, 5};
    vector<int> multipliers3 = {1, 2, 3, 4, 5};
    int result3 = maximumScore(nums3, multipliers3);
    cout << "Test 3 - Expected: 最大分数, Actual: " << result3 << endl;
}

/**
 * 输入处理函数 - 从标准输入读取数据
 */
vector<int> readIntArray() {
    string line;
    getline(cin, line);
    stringstream ss(line);
    vector<int> result;
    int num;
    while (ss >> num) {
        result.push_back(num);
    }
    return result;
}

int main() {
    // 读取输入
    vector<int> nums = readIntArray();
    vector<int> multipliers = readIntArray();
    
    // 计算结果
    int result = maximumScore(nums, multipliers);
    
    // 输出结果
    cout << result << endl;
    
    // 运行测试
    // test();
    
    return 0;
}