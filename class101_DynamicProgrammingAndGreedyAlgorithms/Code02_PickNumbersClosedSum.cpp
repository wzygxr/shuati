#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <cmath>
#include <chrono>
#include <functional>
#include <queue>
#include <map>
#include <unordered_map>
#include <unordered_set>
#include <set>
#include <stack>

using namespace std;

/**
 * 选择数字使集合和相差最小 - C++实现
 * 
 * 问题描述:
 * 给定正数n和k，从1~n中选择k个数字组成集合A，剩下数字组成集合B
 * 希望集合A和集合B的累加和相差不超过1
 * 返回集合A选择的数字，如果无法做到返回空数组
 * 
 * 解题思路:
 * 基于数学构造的贪心算法，直接构造最优解
 * 1. 计算总和sum = n*(n+1)/2
 * 2. 目标让集合A的和接近sum/2
 * 3. 使用数学构造方法直接生成解
 * 
 * 约束条件:
 * 2 <= n <= 10^6
 * 1 <= k <= n
 * 
 * 工程化考量:
 * 1. 使用long long防止整数溢出
 * 2. 添加输入验证和边界检查
 * 3. 实现完整的单元测试
 * 4. 提供性能测试功能
 */

class Code02_PickNumbersClosedSum {
public:
    /**
     * 正式方法 - 最优解
     * 基于数学构造的贪心算法
     * 
     * 算法原理:
     * 1. 计算总和sum = n*(n+1)/2
     * 2. 目标让集合A的和接近sum/2
     * 3. 先尝试让集合A的和为sum/2
     * 4. 如果失败且总和为奇数，再尝试(sum/2)+1
     * 
     * 构造策略:
     * 1. 选择最小的leftSize个数字: 1, 2, ..., leftSize
     * 2. 选择最大的rightSize个数字: n, n-1, ..., n-rightSize+1
     * 3. 如果还有剩余需求，选择一个中间数字
     * 
     * 时间复杂度: O(k)
     * 空间复杂度: O(k)
     * 
     * @param n 数字范围上限
     * @param k 需要选择的数字个数
     * @return 选择的数字列表，如果无解返回空列表
     */
    static vector<int> pick(int n, int k) {
        long long sum = (long long)n * (n + 1) / 2;
        
        // 尝试让集合A的和为sum/2
        vector<int> ans = generate(sum / 2, n, k);
        
        // 如果失败且总和为奇数，尝试(sum/2)+1
        if (ans.empty() && (sum & 1) == 1) {
            ans = generate(sum / 2 + 1, n, k);
        }
        
        return ans;
    }
    
    /**
     * 生成满足条件的数字集合
     * 
     * 数学构造原理:
     * 1. 最小可能的k个数字和: minKSum = k*(k+1)/2
     * 2. 最大可能的k个数字和: maxKSum = minKSum + (n-k)*k
     * 3. 如果目标sum不在[minKSum, maxKSum]范围内，无解
     * 4. 使用贪心构造方法生成解
     */
    static vector<int> generate(long long sum, int n, int k) {
        // 计算最小k个数字的和
        long long minKSum = (long long)k * (k + 1) / 2;
        int range = n - k;
        
        // 检查目标sum是否在可行范围内
        if (sum < minKSum || sum > minKSum + (long long)range * k) {
            return vector<int>();
        }
        
        // 计算需要额外增加的和
        long long need = sum - minKSum;
        
        // 计算右半部分的大小（选择最大的几个数字）
        int rightSize = (int)(need / range);
        
        // 计算中间索引位置
        int midIndex = (k - rightSize) + (int)(need % range);
        
        // 计算左半部分的大小
        int leftSize = k - rightSize - ((need % range == 0) ? 0 : 1);
        
        // 构造结果数组
        vector<int> ans(k);
        
        // 填充左半部分（最小的几个数字）
        for (int i = 0; i < leftSize; i++) {
            ans[i] = i + 1;
        }
        
        // 如果有中间元素，填充中间元素
        if (need % range != 0) {
            ans[leftSize] = midIndex;
        }
        
        // 填充右半部分（最大的几个数字）
        for (int i = k - 1, j = 0; j < rightSize; i--, j++) {
            ans[i] = n - j;
        }
        
        return ans;
    }
    
    /**
     * 验证结果是否正确
     * 检查生成的集合是否满足条件
     */
    static bool pass(int n, int k, const vector<int>& ans) {
        if (ans.empty()) {
            // 如果返回空数组，检查是否真的无解
            return !canSplit(n, k);
        }
        
        if (ans.size() != k) {
            return false;
        }
        
        long long sum = (long long)n * (n + 1) / 2;
        long long pickSum = 0;
        
        for (int num : ans) {
            pickSum += num;
        }
        
        long long diff = abs(pickSum - (sum - pickSum));
        return diff <= 1;
    }
    
    /**
     * 记忆化搜索方法（用于验证）
     * 不是最优解，只是为了验证正确性
     */
    static bool canSplit(int n, int k) {
        int sum = n * (n + 1) / 2;
        int wantSum = (sum / 2) + ((sum & 1) == 0 ? 0 : 1);
        
        // 使用三维数组进行记忆化搜索
        vector<vector<vector<int>>> dp(n + 1, 
            vector<vector<int>>(k + 1, 
                vector<int>(wantSum + 1, 0)));
                
        return f(n, 1, k, wantSum, dp);
    }
    
    static bool f(int n, int i, int k, int s, vector<vector<vector<int>>>& dp) {
        if (k < 0 || s < 0) {
            return false;
        }
        
        if (i == n + 1) {
            return k == 0 && s == 0;
        }
        
        if (dp[i][k][s] != 0) {
            return dp[i][k][s] == 1;
        }
        
        bool ans = f(n, i + 1, k, s, dp) || 
                   f(n, i + 1, k - 1, s - i, dp);
                   
        dp[i][k][s] = ans ? 1 : -1;
        return ans;
    }
    
    /**
     * 类似题目1: 分割等和子集（LeetCode 416）
     * 0-1背包问题的动态规划解法
     */
    static bool canPartition1(const vector<int>& nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 如果总和是奇数，不可能分成两部分
        if (sum & 1) {
            return false;
        }
        
        int target = sum / 2;
        int n = nums.size();
        vector<vector<bool>> dp(n + 1, vector<bool>(target + 1, false));
        
        // 初始化
        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }
        
        // 状态转移
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                // 不选择当前数字
                dp[i][j] = dp[i-1][j];
                
                // 选择当前数字
                if (j >= nums[i-1]) {
                    dp[i][j] = dp[i][j] || dp[i-1][j - nums[i-1]];
                }
            }
        }
        
        return dp[n][target];
    }
    
    /**
     * 分割等和子集 - 空间优化版本
     */
    static bool canPartition2(const vector<int>& nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        if (sum & 1) {
            return false;
        }
        
        int target = sum / 2;
        vector<bool> dp(target + 1, false);
        dp[0] = true;
        
        for (int num : nums) {
            for (int j = target; j >= num; j--) {
                dp[j] = dp[j] || dp[j - num];
            }
        }
        
        return dp[target];
    }
    
    /**
     * 类似题目2: 目标和（LeetCode 494）
     * 动态规划求方案数
     */
    static int findTargetSumWays(const vector<int>& nums, int S) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 边界条件检查
        if (abs(S) > sum || (sum + S) % 2 == 1) {
            return 0;
        }
        
        int target = (sum + S) / 2;
        vector<int> dp(target + 1, 0);
        dp[0] = 1;
        
        for (int num : nums) {
            for (int j = target; j >= num; j--) {
                dp[j] += dp[j - num];
            }
        }
        
        return dp[target];
    }
    
    /**
     * 类似题目3: 零钱兑换问题（LeetCode 322）
     * 完全背包问题求最小硬币数
     */
    static int coinChange(const vector<int>& coins, int amount) {
        vector<int> dp(amount + 1, amount + 1);
        dp[0] = 0;
        
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    /**
     * 单元测试函数
     */
    static void test() {
        cout << "=== 测试选择数字算法 ===" << endl;
        
        // 测试用例1
        int n1 = 10, k1 = 4;
        vector<int> result1 = pick(n1, k1);
        cout << "n=" << n1 << ", k=" << k1 << ": ";
        for (int num : result1) {
            cout << num << " ";
        }
        cout << endl;
        cout << "验证结果: " << (pass(n1, k1, result1) ? "通过" : "失败") << endl;
        
        // 测试用例2
        int n2 = 5, k2 = 2;
        vector<int> result2 = pick(n2, k2);
        cout << "n=" << n2 << ", k=" << k2 << ": ";
        for (int num : result2) {
            cout << num << " ";
        }
        cout << endl;
        cout << "验证结果: " << (pass(n2, k2, result2) ? "通过" : "失败") << endl;
        
        // 测试类似题目
        vector<int> nums = {1, 5, 11, 5};
        bool result3 = canPartition1(nums);
        cout << "分割等和子集结果: " << (result3 ? "true" : "false") << endl;
        
        cout << "=== 测试完成 ===" << endl;
    }
    
    /**
     * 性能测试函数
     */
    static void performance_test() {
        cout << "\n=== 性能测试 ===" << endl;
        
        int n = 1000000;  // 大规模测试
        int k = 500000;
        
        auto start = chrono::high_resolution_clock::now();
        vector<int> result = pick(n, k);
        auto end = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        cout << "大规模测试(n=" << n << ", k=" << k << ")完成" << endl;
        cout << "执行时间: " << duration.count() << "ms" << endl;
        cout << "结果大小: " << result.size() << endl;
    }
    
    /**
     * 主函数
     */
    static void main() {
        test();
        performance_test();
    }
};

int main() {
    Code02_PickNumbersClosedSum::main();
    return 0;
}

/**
 * 工程化考量:
 * 1. 使用long long防止整数溢出
 * 2. 添加输入验证和边界检查
 * 3. 实现完整的单元测试
 * 4. 提供性能测试功能
 * 
 * 调试技巧:
 * 1. 打印中间计算结果
 * 2. 验证数学构造的正确性
 * 3. 对比不同算法的结果
 * 
 * 面试要点:
 * 1. 理解数学构造的原理
 * 2. 能够证明算法的正确性
 * 3. 掌握贪心选择的策略
 * 4. 处理大规模数据的优化
 */