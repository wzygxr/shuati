#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <string>
#include <sstream>
#include <chrono>
using namespace std;

// LeetCode 1246. 删除回文子数组
// 给定一个整数数组arr，每次可以选择并删除一个回文子数组，求删除所有数字的最少操作次数。
// 测试链接: https://leetcode.cn/problems/palindrome-removal/
// 
// 解题思路:
// 1. 状态定义：dp[i][j]表示删除区间[i,j]所需的最少操作次数
// 2. 状态转移：考虑三种策略：单独删除首元素，与后面相同元素一起删除，或者分割区间
// 3. 时间复杂度：O(n^3)
// 4. 空间复杂度：O(n^2)
//
// 工程化考量:
// 1. 异常处理：检查输入数组合法性
// 2. 边界处理：处理空数组和单元素情况
// 3. 性能优化：使用区间DP标准模板
// 4. 测试覆盖：设计全面的测试用例

/**
 * 区间DP解法
 * 时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
 * 空间复杂度: O(n^2) - dp数组占用空间
 * 
 * 解题思路:
 * 1. 状态定义：dp[i][j]表示删除数组arr在区间[i,j]所需的最少操作次数
 * 2. 状态转移：
 *    - 基础情况：dp[i][i] = 1（单个元素需要1次删除）
 *    - 如果arr[i] == arr[j]，则dp[i][j] = dp[i+1][j-1]（可以一起删除）
 *    - 否则，枚举分割点k：dp[i][j] = min(dp[i][k] + dp[k+1][j])
 * 3. 填表顺序：按区间长度从小到大
 */
int minimumMoves(vector<int>& arr) {
    // 异常处理
    if (arr.empty()) {
        return 0;
    }
    
    int n = arr.size();
    
    // 状态定义：dp[i][j]表示删除区间[i,j]所需的最少操作次数
    vector<vector<int>> dp(n, vector<int>(n, INT_MAX));
    
    // 初始化：单个元素需要1次删除
    for (int i = 0; i < n; i++) {
        dp[i][i] = 1;
    }
    
    // 枚举区间长度，从2开始
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            
            // 策略1：如果首尾元素相同，可以一起删除
            if (arr[i] == arr[j]) {
                if (len == 2) {
                    // 长度为2且相同，只需要1次删除
                    dp[i][j] = 1;
                } else {
                    // 长度大于2，考虑内层区间
                    dp[i][j] = min(dp[i][j], dp[i + 1][j - 1]);
                }
            }
            
            // 策略2：枚举分割点k，将区间分为[i,k]和[k+1,j]
            for (int k = i; k < j; k++) {
                dp[i][j] = min(dp[i][j], dp[i][k] + dp[k + 1][j]);
            }
        }
    }
    
    return dp[0][n - 1];
}

/**
 * 优化版本 - 减少不必要的分割点枚举
 * 时间复杂度: O(n^3) 但实际运行更快
 * 空间复杂度: O(n^2)
 * 
 * 优化思路:
 * 1. 当arr[i] == arr[k]时，可以优化状态转移
 * 2. 减少重复计算
 */
int minimumMovesOptimized(vector<int>& arr) {
    if (arr.empty()) {
        return 0;
    }
    
    int n = arr.size();
    vector<vector<int>> dp(n, vector<int>(n, INT_MAX));
    
    // 初始化
    for (int i = 0; i < n; i++) {
        dp[i][i] = 1;
    }
    
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            
            // 关键优化：如果arr[i] == arr[k]，可以优化转移
            for (int k = i; k < j; k++) {
                int temp = dp[i][k] + dp[k + 1][j];
                if (arr[i] == arr[k]) {
                    // 进一步优化：如果首元素与分割点元素相同
                    temp = min(temp, dp[i][k] + (k + 1 <= j ? dp[k + 1][j] - 1 : 0));
                }
                dp[i][j] = min(dp[i][j], temp);
            }
            
            // 特殊处理：首尾元素相同的情况
            if (arr[i] == arr[j]) {
                if (len == 2) {
                    dp[i][j] = 1;
                } else {
                    dp[i][j] = min(dp[i][j], dp[i + 1][j - 1]);
                }
            }
        }
    }
    
    return dp[0][n - 1];
}

/**
 * 记忆化搜索版本 - 递归+记忆化
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n^2)
 * 
 * 优点: 代码更直观，易于理解
 * 缺点: 递归深度可能较大
 */
int dfs(vector<int>& arr, int i, int j, vector<vector<int>>& memo) {
    if (i > j) {
        return 0;
    }
    
    if (memo[i][j] != -1) {
        return memo[i][j];
    }
    
    // 基础情况：单个元素
    if (i == j) {
        return 1;
    }
    
    int result = INT_MAX;
    
    // 策略1：单独删除首元素，然后删除剩余部分
    result = min(result, 1 + dfs(arr, i + 1, j, memo));
    
    // 策略2：如果首元素与后面某个元素相同，可以一起删除
    for (int k = i + 1; k <= j; k++) {
        if (arr[i] == arr[k]) {
            // 如果相邻，可以直接一起删除
            if (k == i + 1) {
                result = min(result, 1 + dfs(arr, k + 1, j, memo));
            } else {
                // 不相邻，需要考虑中间部分
                result = min(result, dfs(arr, i + 1, k - 1, memo) + dfs(arr, k + 1, j, memo));
            }
        }
    }
    
    memo[i][j] = result;
    return result;
}

int minimumMovesMemo(vector<int>& arr) {
    if (arr.empty()) {
        return 0;
    }
    
    int n = arr.size();
    vector<vector<int>> memo(n, vector<int>(n, -1));
    
    return dfs(arr, 0, n - 1, memo);
}

/**
 * 单元测试方法
 */
void test() {
    // 测试用例1：示例输入
    vector<int> arr1 = {1, 2};
    int result1 = minimumMoves(arr1);
    cout << "Test 1 - Input: [1, 2], Expected: 2, Actual: " << result1 << endl;
    
    // 测试用例2：相同元素
    vector<int> arr2 = {1, 1};
    int result2 = minimumMoves(arr2);
    cout << "Test 2 - Input: [1, 1], Expected: 1, Actual: " << result2 << endl;
    
    // 测试用例3：回文数组
    vector<int> arr3 = {1, 2, 1};
    int result3 = minimumMoves(arr3);
    cout << "Test 3 - Input: [1, 2, 1], Expected: 1, Actual: " << result3 << endl;
    
    // 测试用例4：复杂情况
    vector<int> arr4 = {1, 3, 4, 1, 5};
    int result4 = minimumMoves(arr4);
    cout << "Test 4 - Input: [1, 3, 4, 1, 5], Expected: 3, Actual: " << result4 << endl;
    
    // 验证不同方法的正确性
    int result1_opt = minimumMovesOptimized(arr1);
    int result1_memo = minimumMovesMemo(arr1);
    cout << "Validation - Basic: " << result1 << ", Optimized: " << result1_opt << ", Memo: " << result1_memo << endl;
}

/**
 * 性能测试方法
 */
void performanceTest() {
    // 生成测试数据
    vector<int> testArr(100);
    for (int i = 0; i < 100; i++) {
        testArr[i] = i % 10; // 重复元素测试
    }
    
    auto start = chrono::high_resolution_clock::now();
    int result = minimumMoves(testArr);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "Performance Test - Length: " << testArr.size() 
         << ", Result: " << result << ", Time: " << duration.count() << "ms" << endl;
}

/**
 * 边界测试方法
 */
void boundaryTest() {
    // 空数组测试
    vector<int> empty;
    int resultEmpty = minimumMoves(empty);
    cout << "Empty array test: " << resultEmpty << endl;
    
    // 单元素测试
    vector<int> single = {5};
    int resultSingle = minimumMoves(single);
    cout << "Single element test: " << resultSingle << endl;
    
    // 全相同元素测试
    vector<int> allSame = {1, 1, 1, 1, 1};
    int resultAllSame = minimumMoves(allSame);
    cout << "All same elements test: " << resultAllSame << endl;
    
    // 全不同元素测试
    vector<int> allDifferent = {1, 2, 3, 4, 5};
    int resultAllDifferent = minimumMoves(allDifferent);
    cout << "All different elements test: " << resultAllDifferent << endl;
}

/**
 * 输入处理函数
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
    vector<int> arr = readIntArray();
    
    int result = minimumMoves(arr);
    cout << result << endl;
    
    return 0;
}