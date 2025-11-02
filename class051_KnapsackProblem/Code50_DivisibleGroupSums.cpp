#include <iostream>
#include <vector>
#include <algorithm>
#include <random>
#include <chrono>

using namespace std;

// UVA 10616 Divisible Group Sums
// 题目描述：给定N个整数，选择M个数字使得它们的和能被D整除，求方案数。
// 链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1557
// 
// 解题思路：
// 这是一个分组背包+模数运算的问题，需要计算选择M个数字的和能被D整除的方案数。
// 
// 状态定义：dp[i][j][k] 表示前i个数字，选择j个数字，和对D取模为k的方案数
// 状态转移方程：
//   dp[i][j][k] = dp[i-1][j][k] + dp[i-1][j-1][(k - num[i] % D + D) % D]
// 
// 关键点：
// 1. 模数运算：处理负数取模，使用(k - num[i] % D + D) % D
// 2. 分组背包：每个数字只能选择一次
// 3. 空间优化：使用滚动数组优化空间复杂度
// 
// 时间复杂度：O(N * M * D)，其中N是数字数量，M是选择数量，D是除数
// 空间复杂度：O(M * D)，使用滚动数组优化
// 
// 工程化考量：
// 1. 模数处理：正确处理负数取模
// 2. 边界条件：处理M=0、D=0等特殊情况
// 3. 性能优化：使用滚动数组和模数运算优化
// 4. 异常处理：处理除数为0的情况

class DivisibleGroupSums {
public:
    /**
     * 动态规划解法 - 分组背包+模数运算
     * @param nums 整数数组
     * @param M 需要选择的数字个数
     * @param D 除数
     * @return 方案数
     */
    static long long divisibleGroupSums(vector<int>& nums, int M, int D) {
        // 参数验证
        if (nums.empty()) {
            return M == 0 ? 1 : 0;
        }
        if (D == 0) {
            throw invalid_argument("Divisor D cannot be zero");
        }
        if (M < 0 || M > nums.size()) {
            return 0;
        }
        
        int n = nums.size();
        
        // 创建DP数组，使用滚动数组优化
        vector<vector<long long>> dp(M + 1, vector<long long>(D, 0));
        dp[0][0] = 1; // 选择0个数字，和为0（模D为0）的方案数为1
        
        // 遍历每个数字
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            int mod = (num % D + D) % D; // 处理负数取模
            
            // 倒序遍历选择数量，避免重复选择
            for (int j = M; j >= 1; j--) {
                // 创建临时数组保存当前状态
                vector<long long> temp = dp[j];
                for (int k = 0; k < D; k++) {
                    int prevMod = (k - mod + D) % D;
                    dp[j][k] += dp[j - 1][prevMod];
                }
            }
        }
        
        return dp[M][0];
    }
    
    /**
     * 优化的动态规划解法 - 二维数组
     */
    static long long divisibleGroupSums2D(vector<int>& nums, int M, int D) {
        if (nums.empty()) {
            return M == 0 ? 1 : 0;
        }
        if (D == 0) {
            throw invalid_argument("Divisor D cannot be zero");
        }
        if (M < 0 || M > nums.size()) {
            return 0;
        }
        
        int n = nums.size();
        
        // 创建三维DP数组
        vector<vector<vector<long long>>> dp(n + 1, 
            vector<vector<long long>>(M + 1, 
                vector<long long>(D, 0)));
        dp[0][0][0] = 1;
        
        for (int i = 1; i <= n; i++) {
            int num = nums[i - 1];
            int mod = (num % D + D) % D;
            
            for (int j = 0; j <= M; j++) {
                for (int k = 0; k < D; k++) {
                    // 不选当前数字
                    dp[i][j][k] += dp[i - 1][j][k];
                    
                    // 选当前数字
                    if (j >= 1) {
                        int prevMod = (k - mod + D) % D;
                        dp[i][j][k] += dp[i - 1][j - 1][prevMod];
                    }
                }
            }
        }
        
        return dp[n][M][0];
    }
    
    /**
     * 空间优化的解法 - 使用两个二维数组交替
     */
    static long long divisibleGroupSumsOptimized(vector<int>& nums, int M, int D) {
        if (nums.empty()) {
            return M == 0 ? 1 : 0;
        }
        if (D == 0) {
            throw invalid_argument("Divisor D cannot be zero");
        }
        if (M < 0 || M > nums.size()) {
            return 0;
        }
        
        int n = nums.size();
        
        // 使用两个二维数组交替
        vector<vector<long long>> dp(M + 1, vector<long long>(D, 0));
        vector<vector<long long>> next(M + 1, vector<long long>(D, 0));
        dp[0][0] = 1;
        
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            int mod = (num % D + D) % D;
            
            // 复制当前状态到next
            for (int j = 0; j <= M; j++) {
                next[j] = dp[j];
            }
            
            // 更新next数组
            for (int j = 1; j <= M; j++) {
                for (int k = 0; k < D; k++) {
                    int prevMod = (k - mod + D) % D;
                    next[j][k] += dp[j - 1][prevMod];
                }
            }
            
            // 交换数组
            swap(dp, next);
            
            // 清空next数组用于下一次迭代
            for (int j = 0; j <= M; j++) {
                fill(next[j].begin(), next[j].end(), 0);
            }
        }
        
        return dp[M][0];
    }
};

/**
 * 计算组合数C(n, k)
 */
long long combination(int n, int k) {
    if (k < 0 || k > n) return 0;
    if (k == 0 || k == n) return 1;
    
    long long result = 1;
    for (int i = 1; i <= k; i++) {
        result = result * (n - i + 1) / i;
    }
    return result;
}

/**
 * 测试函数
 */
void testDivisibleGroupSums() {
    // 测试用例
    vector<vector<int>> numsCases = {
        {1, 2, 3, 4, 5},
        {2, 4, 6, 8, 10},
        {-1, 1, -2, 2, -3, 3},
        {10, 20, 30, 40, 50}
    };
    vector<int> MList = {2, 3, 2, 3};
    vector<int> DList = {3, 2, 3, 10};
    
    cout << "可整除组和问题测试：" << endl;
    for (size_t i = 0; i < numsCases.size(); i++) {
        vector<int> nums = numsCases[i];
        int M = MList[i];
        int D = DList[i];
        
        long long result1 = DivisibleGroupSums::divisibleGroupSums(nums, M, D);
        long long result2 = DivisibleGroupSums::divisibleGroupSums2D(nums, M, D);
        long long result3 = DivisibleGroupSums::divisibleGroupSumsOptimized(nums, M, D);
        
        cout << "nums=[";
        for (size_t j = 0; j < nums.size(); j++) {
            cout << nums[j];
            if (j < nums.size() - 1) cout << ", ";
        }
        cout << "], M=" << M << ", D=" << D 
             << ": 方法1=" << result1 << ", 方法2=" << result2 
             << ", 方法3=" << result3 << endl;
        
        // 验证结果一致性
        if (result1 != result2 || result2 != result3) {
            cout << "警告：不同方法结果不一致！" << endl;
        }
    }
    
    // 性能测试 - 大规模数据
    int n = 50;
    vector<int> largeNums(n);
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dist(-500, 500);
    
    for (int i = 0; i < n; i++) {
        largeNums[i] = dist(gen); // 包含负数
    }
    int largeM = 10;
    int largeD = 7;
    
    auto startTime = chrono::high_resolution_clock::now();
    long long largeResult = DivisibleGroupSums::divisibleGroupSumsOptimized(largeNums, largeM, largeD);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    
    cout << "大规模测试: 数字数量=" << n << ", M=" << largeM 
         << ", D=" << largeD << ", 结果=" << largeResult 
         << ", 耗时=" << duration.count() << "ms" << endl;
    
    // 边界情况测试
    cout << "边界情况测试：" << endl;
    vector<int> emptyNums;
    cout << "空数组, M=0: " << DivisibleGroupSums::divisibleGroupSums(emptyNums, 0, 5) << endl;
    cout << "空数组, M=1: " << DivisibleGroupSums::divisibleGroupSums(emptyNums, 1, 5) << endl;
    cout << "M=0: " << DivisibleGroupSums::divisibleGroupSums({1, 2, 3}, 0, 5) << endl;
    cout << "M>n: " << DivisibleGroupSums::divisibleGroupSums({1, 2}, 3, 5) << endl;
    
    // 特殊测试：D=1（所有组合都满足）
    cout << "D=1特殊测试：" << endl;
    vector<int> testNums = {1, 2, 3};
    long long specialResult = DivisibleGroupSums::divisibleGroupSums(testNums, 2, 1);
    long long expected = combination(3, 2);
    cout << "D=1, 预期=C(3,2)=" << expected << ", 实际=" << specialResult << endl;
    
    if (specialResult == expected) {
        cout << "D=1测试验证通过" << endl;
    } else {
        cout << "D=1测试验证失败" << endl;
    }
}

int main() {
    try {
        testDivisibleGroupSums();
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    return 0;
}

/*
 * 复杂度分析：
 * 
 * 方法1：动态规划（滚动数组）
 * - 时间复杂度：O(N * M * D)
 *   - N: 数字数量
 *   - M: 选择数量
 *   - D: 除数
 * - 空间复杂度：O(M * D)
 * 
 * 方法2：三维动态规划
 * - 时间复杂度：O(N * M * D)
 * - 空间复杂度：O(N * M * D)
 * 
 * 方法3：空间优化的动态规划
 * - 时间复杂度：O(N * M * D)
 * - 空间复杂度：O(M * D)（使用两个二维数组）
 * 
 * C++特定优化：
 * 1. 使用vector代替数组，更安全
 * 2. 使用STL算法进行填充和交换
 * 3. 使用随机数生成器进行性能测试
 * 4. 使用chrono进行精确性能测量
 * 
 * 关键点分析：
 * 1. 模数运算：正确处理负数取模，使用(k - mod + D) % D
 * 2. 状态定义：dp[i][j][k]表示前i个数字选j个模D为k的方案数
 * 3. 空间优化：使用滚动数组减少空间复杂度
 * 4. 边界处理：M=0时方案数为1（空选择）
 * 
 * 工程化考量：
 * 1. 模块化设计：将不同解法封装为静态方法
 * 2. 异常处理：使用try-catch处理异常
 * 3. 性能优化：利用STL容器和算法
 * 4. 测试覆盖：包含各种边界情况和性能测试
 * 
 * 面试要点：
 * 1. 理解分组背包+模数运算的组合
 * 2. 掌握模数运算的处理技巧
 * 3. 了解不同DP实现的空间优化
 * 4. 能够分析算法的时空复杂度
 */